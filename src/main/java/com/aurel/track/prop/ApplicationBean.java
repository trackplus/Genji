/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */



package com.aurel.track.prop;

import java.io.File;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TClusterNodeBean;
import com.aurel.track.beans.TLoggedInUsersBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.dbase.HandleHome;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.lucene.index.LuceneIndexer;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.SimpleEncryption;
import com.trackplus.license.LicenseManager;
/*
 * This class keeps track on the number of users connected to the database.
 * @version $Revision: 1439 $ $Date: 2015-09-28 13:37:25 +0200 (Mon, 28 Sep 2015) $
 * @author Joerg Friedrich
 */
public final class ApplicationBean implements Serializable {

	private String licenseVersion = "5.0";
	private int dbVersion = 503;

	public static String OPSTATE_RUNNING = "Running";
	public static String OPSTATE_MAINTENNANCE = "Maintenance";
	private static final long serialVersionUID = 500L;

	public static final int APPTYPE_FULL = 1;
	public static final int APPTYPE_DESK = 2;
	public static final int APPTYPE_BUGS = 3;


	// ----------------------------------------------------- Instance Variables
	private static final Logger LOGGER = LogManager.getLogger(ApplicationBean.class);
	private int maxNumberOfFullUsers = 100000;
	private int maxNumberOfLimitedUsers = 100000;
	private String licenseHolder = "Trial License";
	private Date expDate = DateTimeUtils.getInstance().parseISODate("2099-12-31");
	private long instDate = 0;

	private int edition = 3; // 3 = "ent", 2 = "prof", 1 = "std";
	private String editionString = "Enterprise";
	private int appType = APPTYPE_BUGS;  // this and the following will be overwritten by values in Version.properties
	private String appTypeString = "";  // during startup

	private String extendedKey = null;

	private static ApplicationBean ref = null;
	private Integer fullInactive = null;
	private Integer fullActive = null;
	private Integer limitedActive = null;
	private Integer limitedInactive = null;
	private Boolean isCluster = false;

	private String latexCommand = null;

	private String phantomJSCommand = null;

	private String imageMagicCommand = null;

	private LicenseManager licenseManager = null;

	private boolean backupInProgress=false;
	private boolean restoreInProgress=false;

	private transient ServletContext servletContext = null;

	private TClusterNodeBean clusterNodeBean = null;

	private TSiteBean siteBean = null;

	private transient ExecutorService executor = null;

	/**
	 * Reuse the lucene indexer instance by reindexing (finished should be true)
	 */
	private transient LuceneIndexer luceneIndexer;
	/*
	 * Make the constructor private, this is a singleton
	 */
	private ApplicationBean() {

	}

	/**
	 * Singleton <code>ApplicationBean</code> instance. Create one if
	 * it does not yet exist. Has been deprecated, use <code>getInstance()</code> instead.
	 * @return the one and only <code>ApplicationBean</code> instance
	 */
	@Deprecated
	public static synchronized ApplicationBean getApplicationBean()
	{
		if (ref == null)
			// it's ok, we can call this constructor
			ref = new ApplicationBean();
		return ref;
	}

	/**
	 * Singleton <code>ApplicationBean</code> instance. Create one if
	 * it does not yet exist.
	 * @return the one and only <code>ApplicationBean</code> instance
	 */
	public static synchronized ApplicationBean getInstance()
	{
		if (ref == null)
			// it's ok, we can call this constructor
			ref = new ApplicationBean();
		return ref;
	}

	/**
	 * Make sure we can't create a second instance by cloning.
	 */
	@Override
	public Object clone()
	throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
		// that'll teach 'em
	}

	/**
	 * This <code>ApplicationBean belongs to a single <code>TClusterNode</code>.
	 * If there is no cluster, this is the single running server instance.
	 * @return this server node
	 */
	public TClusterNodeBean getClusterNodeBean() {
		return clusterNodeBean;
	}

	/**
	 * Sets the <code>TClusterNode</code> to this node
	 * @param clusterNode
	 */
	public void setClusterNodeBean(TClusterNodeBean clusterNodeBean) {
		this.clusterNodeBean = clusterNodeBean;
	}

	/**
	 * Whether we are in a cluster or not
	 * @return
	 */
	public boolean isCluster() {
		return isCluster;
	}

	/**
	 * Whether we are in a cluster or not
	 * @return
	 */
	public void setCluster(Boolean _isCluster) {
		isCluster = _isCluster;
	}

	/**
	 * Get the Torque configuration
	 * @return
	 * @throws ServletException
	 */
	public PropertiesConfiguration getDbConfig() {
		PropertiesConfiguration tcfg = null;
		try {
			tcfg = HandleHome.getTorqueProperties(servletContext, false);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		return tcfg;
	}


	/**
	 * Set the servlet context
	 * @param _servletContext
	 */
	public void setServletContext(ServletContext _servletContext){
		this.servletContext = _servletContext;
	}

	/**
	 * Returns the servlet context
	 * @return
	 */
	public ServletContext getServletContext(){
		return this.servletContext;
	}

	public void setLicenseManager(LicenseManager lm) {
		licenseManager = lm;
	}

	public LicenseManager getLicenseManager() {
		return licenseManager;
	}

	//----------------------- User section ----------------------------
	/**
	 * Get fully licensed users that are inactive
	 * @return
	 */
	public Integer getFullInactive() {
		return fullInactive;
	}

	private void setFullInactive(Integer fullInactive) {
		this.fullInactive = fullInactive;
	}

	public Integer getFullActive() {
		return fullActive;
	}

	private void setFullActive(Integer fullActive) {
		this.fullActive = fullActive;
	}

	public Integer getLimitedActive() {
		return limitedActive;
	}

	private void setLimitedActive(Integer limitedActive) {
		this.limitedActive = limitedActive;
	}

	public Integer getLimitedInactive() {
		return limitedInactive;
	}

	private void setLimitedInactive(Integer limitedInactive) {
		this.limitedInactive = limitedInactive;
	}

	/**
	 * Sets the user counts for the actual users. Considers
	 * fully licensed, active and inactive users as well as easy
	 * licensed active and inactive users.
	 */
	public void setActualUsers() {
		setFullActive(PersonBL.countFullActive());
		setFullInactive(PersonBL.countFullInactive());
		setLimitedActive(PersonBL.countLimitedActive());
		setLimitedInactive(PersonBL.countLimitedInactive());
	}


	// ------
	public int getMaxNumberOfFullUsers() {
		if (licenseManager != null &&
		   licenseManager.getLicensedFeature("users") != null) {
			maxNumberOfFullUsers = licenseManager.getLicensedFeature("users").getNumberOfUsers();
		} else {
			return 2;
		}
		return maxNumberOfFullUsers;
	}

	public String getLicenseHolder() {
		if (licenseManager != null) {
			licenseHolder = licenseManager.getLicenseOwner();
		}
		return licenseHolder;
	}

	public int getMaxNumberOfLimitedUsers() {
		if (licenseManager != null &&
			licenseManager.getLicensedFeature("ext") != null	) {
			maxNumberOfLimitedUsers = licenseManager.getLicensedFeature("ext").getNumberOfUsers();
		} else {
			return 0;
		}
		return maxNumberOfLimitedUsers;
	}

	//------------------ End of user section -------------------------

	public int getDbVersion() {
		return dbVersion;
	}

	public void setInstDate(long theDate) {
		instDate = theDate;
	}

	public long getInstDate() {
		return instDate;
	}


	public synchronized void addUser(TPersonBean user, String sessionId) {
		TLoggedInUsersBean loggedInUsersBean = new TLoggedInUsersBean();
		TClusterNodeBean clusterNodeBean = getClusterNodeBean();
		if (clusterNodeBean!=null) {
			loggedInUsersBean.setNodeAddress(clusterNodeBean.getObjectID());
		} else {
			LOGGER.error("clusterNodeBean is null, this should never happen.");
		}
		loggedInUsersBean.setSessionId(sessionId);
		loggedInUsersBean.setLoggedUser(user.getObjectID());
		loggedInUsersBean.setUserLevel(user.getUserLevel());
		loggedInUsersBean.setLastUpdate(new Date());
		ClusterBL.saveLoggedInUser(loggedInUsersBean);
		LOGGER.debug("Added user >"
				+ user.getFullName()+ "< to loggedIn list");
	}


	/**
	 * session gets invalid
	 */
	public synchronized void removeUser(String sessionId) {
		ClusterBL.removeLoggedInUserBySession(sessionId);
	}

	public void removeClusterNode() {
		TClusterNodeBean clusterNodeBean = getClusterNodeBean();
		if (clusterNodeBean!=null) {
			//remove the cluster node
			ClusterBL.delete(clusterNodeBean.getObjectID());
		}
	}

	public int getSignature() {
		return 503311;
	}

	/**
	 * Initialize the license and check if it is valid. In case it is valid
	 * update all the user counts and limits.
	 * @param key - the license key
	 */
	public void initLic(String key) {

		setExtendedKey(key);

		if (licenseManager != null) {
			licenseManager.setLicenseKey(key);
		}
	}

	/**
	 * Check if this license is still valid, in case it is a temporary license.
	 * @return the number of days this license is still valid
	 */
	public int getDaysLicenseValid() {
		if (licenseManager != null) {
			return licenseManager.getDaysValid();
		}
		return 7300;  // 20 years
	}


	public Date getExpDate() {
		if (licenseManager != null) {
			expDate = licenseManager.getLicenseExpirationDate();
		}
		return expDate;
	}

	/**
	 * @return Returns the extendedKey.
	 */
	public String getExtendedKey() {
		return this.extendedKey;
	}
	/*
	 * @param extendedKey The extendedKey to set.
	 */
	private void setExtendedKey(String pExtendedKey) {
		this.extendedKey = pExtendedKey;
	}
	/**
	 * @return Returns the licenseVersion.
	 */
	public String getLicenseVersion() {
		return licenseVersion;
	}
	/**
	 * @param licenseVersion The licenseVersion to set.
	 */
	public void setLicenseVersion(String licenseVersion) {
		this.licenseVersion = licenseVersion;
	}

	/**
	 * @return Returns the edition.
	 */
	public int getEdition() {
		return edition;
	}
	/**
	 * @param _edition The program edition (Enterprise, Professional, Standard)
	 */
	public void setEdition(String _edition) {
		if (_edition != null) {
			if (_edition.equals("ent")) {
				this.edition = 3;
				this.editionString = "Enterprise";
			}
			else if (_edition.equals("prof")) {
				this.edition = 2;
				this.editionString = "Professional";
			}
			else if (_edition.equals("std")) {
				this.edition = 1;
				this.editionString = "Standard";
			}
		}
	}

	public String getEditionString() {
		return this.editionString;
	}

	public ErrorData getErrors() {
		if (licenseManager != null) {
			return licenseManager.getLicenseErrorText();
		}
	   return null;
	}

	public int getErrorCode(HttpSession session) {
		if (licenseManager != null) {
			return licenseManager.getErrorCode();
		}
		return 0;
	}

	public TSiteBean setSiteParams(TSiteBean siteBean) {
		siteBean.setNumberOfUsers(new Integer(getMaxNumberOfFullUsers()
				+ getMaxNumberOfLimitedUsers()));
		siteBean.setNumberOfFullUsers(getMaxNumberOfFullUsers());
		siteBean.setNumberOfLimitedUsers(getMaxNumberOfLimitedUsers());
		siteBean.setExpDate(getExpDate());
		this.siteBean = siteBean;
		return siteBean;
	}

	public void setSiteBean(TSiteBean _siteBean) {
		this.siteBean = _siteBean;

	}

	public TSiteBean getSiteBean() {
		return this.siteBean;
	}

	public synchronized boolean isBackupInProgress() {
		return backupInProgress;
	}

	public synchronized void setBackupInProgress(boolean backupInProgress) {
		this.backupInProgress = backupInProgress;
	}

	public synchronized boolean isRestoreInProgress() {
		return restoreInProgress;
	}

	public synchronized void setRestoreInProgress(boolean restoreInProgress) {
		this.restoreInProgress = restoreInProgress;
	}

	private boolean newerVersion = false;

	public boolean getNewerVersion() {
		if (siteBean != null && siteBean.getIsVersionReminderOn()) {
			return newerVersion;
		}
		else {
			return false;
		}
	}

	public void setNewerVersion(boolean nv) {
		newerVersion = nv;
	}

	private Integer mostActualVersion = new Integer(0);

	public Integer getMostActualVersion() {
	  return mostActualVersion;
	}

	public void setMostActualVersion(Integer mav) {
		if (mav.intValue() > getVersionNo().intValue()){
			setNewerVersion(true);
		} else {
			setNewerVersion(false);
		}
		mostActualVersion = mav;
	}

	private String mostActualVersionString = "";

	public void setMostActualVersionString(String vs) {
		mostActualVersionString = vs;
	}

	public String getMostActualVersionString() {
		return mostActualVersionString;
	}

	// Salt
	protected byte[] salt = {
				(byte)0xc2, (byte)0x71, (byte)0x21, (byte)0x8b,
				(byte)0x7e, (byte)0xc7, (byte)0xef, (byte)0x9a
	};

	protected int count = 15;
	private   String CRLF = "\r\n";

	/**
	 * Get license properties for this server.
	 * @return
	 */
	public String getProperties() {
		StringBuffer props = new StringBuffer();
		for (int i=0; i < getInetAddress().length; ++i) {
			props.append("ip"+i+"=" + getInetAddress()[i].getHostAddress() + CRLF);
		}
		props.append("fa=" + getFullActive().toString() + CRLF);
		props.append("la=" + getLimitedActive().toString() + CRLF);
		props.append("ve=" + getVersion() + CRLF);
		props.append("bu=" + ApplicationBean.getApplicationBean().getBuild() + CRLF);
		if (getSiteBean().getServerURL() != null) {
			props.append("ur=" + getSiteBean().getServerURL() + CRLF);
		}
		props.append("em=" + getSiteBean().getTrackEmail() + CRLF);
		return encryptUpdateInfo(props.toString(), "311");
	}

	private String encryptUpdateInfo(String clearText, String password) {
		SimpleEncryption se = new SimpleEncryption();
		String encText = se.encrypt(password, clearText);
		return Base64.encodeBase64String(encText.getBytes());
	}

	/**
	 * Get a comma separated <code>String</code> with IP numbers for this server.
	 * @return a comma separated <code>String</code> with IP numbers for this server.
	 */
	public static String getIpNumbersString() {
		String ipNumbers = "";
		for (int i=0; i < ApplicationBean.getInetAddress().length; ++i) {
			if (i!=0) {
				ipNumbers = ipNumbers +(", ");
			}
			ipNumbers = ipNumbers + ApplicationBean.getInetAddress()[i].getHostAddress();
		}

		return ipNumbers;
	}

	private Integer theVersionNo;

	public void setVersionNo(Integer _versionNo) {
		theVersionNo = _versionNo;
	}

	private String theVersionDate;

	public String getVersionDate(){
		return theVersionDate;
	}

	public void setVersionDate(String _versionDate) {
		theVersionDate = _versionDate;
	}

	public Integer getVersionNo(){
		return theVersionNo;
	}

	private String theBuild = null;

	public void setBuild(String _build) {
		theBuild = _build;
	}

	public String getBuild() {
		return theBuild;
	}

	private String theVersion = null;

	public void setVersion(String _version) {
		theVersion = _version;
	}

	public String getVersion(){
		return theVersion;
	}

	public int getAppType() {
		return appType;
	}

	public void setAppType(int appType) {
	}

	public String getAppTypeString() {
		if (appTypeString.equals("{TYPE}")) {
			switch (appType) {
			case APPTYPE_FULL:
				return "Genji";
			case APPTYPE_DESK:
				return "Teamdesk";
			case APPTYPE_BUGS:
				return "Genji";
			default: return appTypeString;
			}
		}
		return appTypeString;
	}

	public boolean isGenji() {
		return ApplicationBean.getApplicationBean().getAppType()==ApplicationBean.APPTYPE_BUGS;
	}

	public void setAppTypeString(String appTypeString) {
		this.appTypeString = appTypeString;
	}

	public int getAppTypeDesk() {
		return APPTYPE_DESK;
	}

	public int getAppTypeBugs() {
		return APPTYPE_BUGS;
	}

	public int getAppTypeFull() {
		return APPTYPE_FULL;
	}

	private List<LabelValueBean> backupErrors;

	/**
	 * Get a list with errors that occurred during backup.
	 * @return a list of <code>LabelValueBean</code>s with backup errors
	 */
	public List<LabelValueBean> getBackupErrors() {
		return backupErrors;
	}

	public void setBackupErrors(List<LabelValueBean> errors) {
		backupErrors = errors;
	}

	private List<LabelValueBean> designs = new ArrayList<LabelValueBean>();

	public List<LabelValueBean> getDesigns() {
		return designs;
	}

	public void setDesigns(List<LabelValueBean> _designs) {
		designs = _designs;
	}

	/**
	 * Retrieve all non-loopback IP addresses from all network interfaces
	 * @return
	 */
	public static InetAddress[] getInetAddress() {
		ArrayList<InetAddress> allIPs = new ArrayList<InetAddress>();
		InetAddress[] allAds = null;

		try {
			InetAddress candidateAddress = null;
			// Iterate all NICs (network interface cards)...
			for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
				NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
				// Iterate all IP addresses assigned to each card...
				for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
					InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
					if (!inetAddr.isLoopbackAddress()) {

						if (inetAddr.isSiteLocalAddress()) {
							// Found non-loopback site-local address. Return it immediately...
							allIPs.add(inetAddr);
							continue;
						}
						else if (candidateAddress == null) {
							// Found non-loopback address, but not necessarily site-local.
							// Store it as a candidate to be returned if site-local address is not subsequently found...
							candidateAddress = inetAddr;
							// Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
							// only the first. For subsequent iterations, candidate will be non-null.
						}
					}
				}
			}

			// At this point, we did not find a non-loopback address.
			// Fall back to returning whatever InetAddress.getLocalHost() returns...
			if (allIPs.size() < 1) {
				allIPs.add(InetAddress.getLocalHost());
			}

			allAds = new InetAddress[allIPs.size()];
			allAds = allIPs.toArray(allAds);

		}
		catch (Exception uhn) {
			LOGGER.error("An exception occurred trying to get "
					+ "all IP addresses for this host: " + uhn.getMessage());
		}
		return allAds;
	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}

	public LuceneIndexer getLuceneIndexer() {
		return luceneIndexer;
	}

	public void setLuceneIndexer(LuceneIndexer luceneIndexer) {
		this.luceneIndexer = luceneIndexer;
	}

	public String getLatexCommand() {

		if (this.latexCommand == null || "".equals(this.latexCommand)) {

			String OS = System.getProperty("os.name").toLowerCase();

			String cmdPath = HandleHome.getTrackplus_Home()+"/ExportTemplates/latexTemplates/latex.sh";

			if (OS.indexOf("mac") >= 0) {
				this.latexCommand = "/usr/texbin/xelatex"; //cmdPath;
			}

			if (OS.indexOf("win") >= 0) {
				this.latexCommand = getLatexCommandPathWin();
			}

			if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0) {
				this.latexCommand = cmdPath;
			}
		}

		if(this.latexCommand == null) {
			if (System.getProperty(HandleHome.LATEX_HOME) != null) {
				this.latexCommand = System.getProperty(HandleHome.LATEX_HOME) + File.separator + "xelatex";
			}
		}

		File test = null;
		if (this.latexCommand != null && !"".equals(this.latexCommand)) {
			test = new File(this.latexCommand);
		}

		if (test != null && !test.exists()) {
			this.latexCommand = null;
		}

		return this.latexCommand;
	}

	public void setLatexCommand(String cmd) {
		this.latexCommand  = cmd;
	}

	private String getLatexCommandPathWin() {
		String path = null;
		for (String ix: System.getenv().keySet()) {
			String value =  System.getenv().get(ix);
			if(ix.toUpperCase().contains("MIKTEX")) {
				path = value;
				break;
			}
			if(ix.equals("Path")) {
				String[] parts = value.split(";");
				for (String aPart : parts) {
					if(aPart.toUpperCase().contains("MIKTEX")) {
						path = aPart;
						break;
					}
				}
			}
		}
		if(path != null && path.length() > 1) {
			if(!path.endsWith("\\")) {
				path += "\\";
			}
			path += "xelatex.exe";
		}
		if(path == null) {
			if (System.getProperty(HandleHome.LATEX_HOME) != null) {
				path = System.getProperty(HandleHome.LATEX_HOME);
			}
		}
		return path;
	}

	public String getImageMagicCommand() {
		if (this.imageMagicCommand == null || "".equals(this.imageMagicCommand)) {

			String OS = System.getProperty("os.name").toLowerCase();

			if (OS.indexOf("mac") >= 0) {
				this.imageMagicCommand = "/usr/local/bin/convert";
			}

			if (OS.indexOf("win") >= 0) {
				this.imageMagicCommand = getImageMagicCommandPathWin();
			}

			if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0) {
				this.imageMagicCommand = "/usr/bin/convert";
			}
		}
		if(this.imageMagicCommand != null) {
			this.imageMagicCommand = this.imageMagicCommand.replace("\\", "/");
		}
		File test = new File(this.imageMagicCommand);
		if (!test.exists()) {
			this.imageMagicCommand = null;
		}
		if(this.imageMagicCommand == null) {
			if (System.getProperty(HandleHome.IMAGEMAGICK_HOME) != null) {
				this.imageMagicCommand= System.getProperty(HandleHome.IMAGEMAGICK_HOME);
			}

		}
		return this.imageMagicCommand;
	}



	public void setImageMagicCommaned(String imageMagicCommaned) {
		this.imageMagicCommand = imageMagicCommaned;
	}

	private String getImageMagicCommandPathWin() {
		String path = null;
		for (String ix: System.getenv().keySet()) {
			String value =  System.getenv().get(ix);
			if(ix.toUpperCase().contains("IMAGEMAGICK")) {
				path = value;
				break;
			}
			if(ix.equals("Path")) {
				String[] parts = value.split(";");
				for (String aPart : parts) {
					if(aPart.toUpperCase().contains("IMAGEMAGICK")) {
						path = aPart;
						break;
					}
				}
			}
		}
		if(path != null && path.length() > 1) {
			if(!path.endsWith("\\")) {
				path += File.separator;
			}
			path += "convert.exe";
		}
		if(path == null) {
			if (System.getProperty(HandleHome.IMAGEMAGICK_HOME) != null) {
				path = System.getProperty(HandleHome.IMAGEMAGICK_HOME);
			}
		}
		if(path != null) {
			path = path.replace("\\", "/");
		}
		return path;
	}

	public String getPhantomJSCommand() {
		if (this.phantomJSCommand == null || "".equals(this.phantomJSCommand)) {

			String OS = System.getProperty("os.name").toLowerCase();

			if (OS.indexOf("mac") >= 0) {
				this.phantomJSCommand = "/usr/local/bin/phantomjs";
			}

			if (OS.indexOf("win") >= 0) {
				this.phantomJSCommand = getPhantomJSCommandPathWin();
			}

			if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0) {
				this.phantomJSCommand = "/usr/bin/phantomjs";
			}
		}
		File test = new File(this.phantomJSCommand);
		if (!test.exists()) {
			this.phantomJSCommand = null;
		}
		if(this.phantomJSCommand == null) {
			if (System.getProperty(HandleHome.PHANTOMJS_HOME) != null) {
				this.phantomJSCommand= System.getProperty(HandleHome.PHANTOMJS_HOME);
			}

		}
		return this.phantomJSCommand;
	}

	public void setPhantomJSCommand(String cmd) {
		this.phantomJSCommand = cmd;
	}

	private String getPhantomJSCommandPathWin() {
		String path = null;
		for (String ix: System.getenv().keySet()) {
			String value =  System.getenv().get(ix);
			if(ix.toUpperCase().contains("PHANTOMJS")) {
				path = value;
				break;
			}
			if(ix.equals("Path")) {
				String[] parts = value.split(";");
				for (String aPart : parts) {
					if(aPart.toUpperCase().contains("PHANTOMJS")) {
						path = aPart;
						break;
					}
				}
			}
		}
		if(path != null && path.length() > 1) {
			if(!path.endsWith("\\")) {
				path += File.separator;
			}
			path += "phantomjs.exe";
		}
		if(path == null) {
			if (System.getProperty(HandleHome.PHANTOMJS_HOME) != null) {
				path = System.getProperty(HandleHome.PHANTOMJS_HOME);
			}
		}
		if(path != null) {
			path = path.replace("\\", "/");
		}
		return path;
	}


	private boolean inTestMode = false;

	public boolean isInTestMode() {
		return inTestMode;
	}

	public void setInTestMode(boolean inTestMode) {
		this.inTestMode = inTestMode;
	}

	/**
	 * Whether budget (top down plan) is available
	 * @return
	 */
	public boolean getBudgetActive() {
		return !getInstance().isGenji() && getInstance().getSiteBean().getBudgetActive();
	}

	private ArrayList<String> installProblem;

	public void setInstallProblem(ArrayList<String> p) {
       installProblem = p;
	}

	public ArrayList<String> getInstallProblem() {
	   return installProblem;
	}

    /*
     * The maximum length of the description field in an issue.
     * This is database dependent.
     */
    private int descriptionMaxLength = 4000;

    public int getDescriptionMaxLength() {
        return descriptionMaxLength;
    }

    public void setDescriptionMaxLength(int length) {
        descriptionMaxLength = length;
    }

    /*
     * The maximum length of a comment in the trail of an issue.
     * This is database dependent.
     */
    private int commentMaxLength = 4000;

    public int getCommentMaxLength() {
        return commentMaxLength;
    }

    public void setCommentMaxLength(int length) {
        commentMaxLength = length;
    }


}
