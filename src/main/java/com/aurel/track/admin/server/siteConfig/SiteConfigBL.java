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


package com.aurel.track.admin.server.siteConfig;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.NamingException;



import com.aurel.track.admin.project.ProjectBL;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.lucene.analysis.Analyzer;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Pattern;

import com.aurel.track.Constants;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.SiteDAO;
import com.aurel.track.dbase.HandleHome;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.item.ItemBL;
import com.aurel.track.json.ControlError;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.LuceneIndexer;
import com.aurel.track.lucene.util.FileUtil;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.LdapUtil;
import com.aurel.track.util.Support;
import com.trackplus.license.LicenseManager;

/**
 * Business logic class for TSite. It manages handling of the server configuration
 * like license management, incoming and outgoing e-mail connections, LDAP, etc.
 *
 * @author Adrian Bojani
 *
 */
public class SiteConfigBL {
	protected static final Logger LOGGER = LogManager.getLogger(SiteConfigBL.class);

	private static SiteDAO siteDAO=DAOFactory.getFactory().getSiteDAO();

	/**
	 * This method gets all data relevant for license management
	 * from the beans where it is stored and returns the data in
	 * a suitable transfer object. The transfer object can be used
	 * to move this data back and forth from the user interface.
	 *
	 * @param siteBean contains all data for the server configuration
	 * @param appBean contains data stored in the application context
	 * @return a transfer object for the license data
	 */
	static LicenseTO getLicenseTO(TSiteBean siteBean, ApplicationBean appBean, Locale locale){
		LicenseTO licenseTO=new LicenseTO();
		licenseTO.setSystemVersion(siteBean.getTrackVersion()+" "+appBean.getAppTypeString()+" "+appBean.getEditionString());
		licenseTO.setDbaseVersion(siteBean.getDbaseVersion());
		licenseTO.setIpNumber(ApplicationBean.getIpNumbersString());
		//licenseTO.setLicenseKey(siteBean.getLicenseKey());
		licenseTO.setLicenseExtension(siteBean.getLicenseExtension());
		licenseTO.setLicenseHolder(appBean.getLicenseHolder());
		licenseTO.setExpDateDisplay(DateTimeUtils.getInstance().formatGUIDate(siteBean.getExpDate(), locale));
		LicenseManager licenseManager = appBean.getLicenseManager();
		if (licenseManager!=null) {
			licenseTO.setLicensedFeatures(licenseManager.getLicensedFeatures(true));
		}
		licenseTO.setNumberOfUsers(appBean.getMaxNumberOfFullUsers()+appBean.getMaxNumberOfLimitedUsers());
		licenseTO.setNumberOfFullUsers(appBean.getMaxNumberOfFullUsers());
		licenseTO.setNumberOfLimitedUsers(appBean.getMaxNumberOfLimitedUsers());
		return licenseTO;
	}

	/**
	 * Updates the license in the site bean (server configuration bean) with
	 * the new license data.
	 *
	 * @param siteBean contains all data for the server configuration
	 * @param licenseTO the transfer object with the license data
	 */
	public static void updateLicense(TSiteBean siteBean, LicenseTO licenseTO){
		if (licenseTO==null) {
			licenseTO = new LicenseTO();
		}
		siteBean.setLicenseExtension(licenseTO.getLicenseExtension());
	}


	/**
	 *
	 * @param siteBean contains all data for the server configuration
	 * @return
	 */
	static FullTextSearchTO getFullTextSearchTO(TSiteBean siteBean){
		FullTextSearchTO fullTextSearchTO=new FullTextSearchTO();

		String useLuceneStr=siteBean.getUseLucene();
		boolean useLucene=Boolean.valueOf(useLuceneStr).booleanValue();
		fullTextSearchTO.setUseLucene(useLucene);

		String indexAttachmentsStr=siteBean.getIndexAttachments();
		boolean indexAttachments = Boolean.valueOf(indexAttachmentsStr).booleanValue();
		fullTextSearchTO.setIndexAttachments(indexAttachments);

		String reindexOnStartupStr=siteBean.getReindexOnStartup();
		boolean reindexOnStartup=Boolean.valueOf(reindexOnStartupStr).booleanValue();
		fullTextSearchTO.setReindexOnStartup(reindexOnStartup);

		fullTextSearchTO.setAnalyzer(siteBean.getAnalyzer());
		fullTextSearchTO.setIndexPath(siteBean.getIndexPath());

		fullTextSearchTO.setAnalyzers(LuceneUtil.getFoundAnalysers());

		return fullTextSearchTO;
	}

	/**
	 *
	 * @param siteBean contains all data for the server configuration
	 * @param fullTextSearchTO
	 *
	 */
	static void updateFullTextSearch(TSiteBean siteBean, FullTextSearchTO fullTextSearchTO) {
		if (fullTextSearchTO==null) {
			fullTextSearchTO = new FullTextSearchTO();
		}
		siteBean.setUseLucene(Boolean.toString(fullTextSearchTO.isUseLucene()));
		siteBean.setIndexAttachments(Boolean.toString(fullTextSearchTO.isIndexAttachments()));
		siteBean.setReindexOnStartup(Boolean.toString(fullTextSearchTO.isReindexOnStartup()));
		siteBean.setAnalyzer(fullTextSearchTO.getAnalyzer());
		siteBean.setIndexPath(fullTextSearchTO.getIndexPath());
	}
	public static void validateFullTextSearch(FullTextSearchTO fullTextSearchTO,List<ControlError> errors, Locale locale){
		if (fullTextSearchTO!=null && fullTextSearchTO.isUseLucene()) {
			String errorMessage = createAndCheckDirectory(fullTextSearchTO.getIndexPath(), locale);
			if (errorMessage!=null) {
				List<String> controlPath = new LinkedList<String>();
				controlPath.add(OtherSiteConfigTO.JSONFIELDS.tabOtherSiteConfig);
				controlPath.add(OtherSiteConfigTO.JSONFIELDS.fsDirectories);
				controlPath.addAll(JSONUtility.getPathInHelpWrapper(FullTextSearchTO.JSONFIELDS.indexPath));
				errors.add(new ControlError(controlPath, errorMessage));
			}
		}
	}

	/**
	 *
	 * @param siteBean contains all data for the server configuration
	 * @return
	 */
	static LdapTO getLdapTO(TSiteBean siteBean){
		LdapTO ldapTO=new LdapTO();
		ldapTO.setServerURL(siteBean.getLdapServerURL());
		ldapTO.setAttributeLoginName(siteBean.getLdapAttributeLoginName());
		ldapTO.setBindDN(siteBean.getLdapBindDN());
		ldapTO.setEnabled(siteBean.getIsLDAPOnBool());
		ldapTO.setForce(siteBean.getIsForceLdap());
		return ldapTO;
	}

	/**
	 *
	 * @param siteBean contains all data for the server configuration
	 * @param ldapTO
	 */
	static void updateLdapTO(TSiteBean siteBean, LdapTO ldapTO){
		// The transfer object will be null if everything is disabled...
		if (ldapTO == null) {
			ldapTO = new LdapTO();
			ldapTO.setEnabled(false);
		}
		siteBean.setIsLDAPOnBool(ldapTO.isEnabled());
		//if (siteBean.getIsLDAPOnBool()) {
			siteBean.setLdapServerURL(ldapTO.getServerURL());
			siteBean.setLdapAttributeLoginName(ldapTO.getAttributeLoginName());
			siteBean.setLdapBindDN(ldapTO.getBindDN());
			siteBean.setIsForceLdap(ldapTO.isForce());

			if(ldapTO.getPassword() !=null && ldapTO.getPassword().length() > 0){
				siteBean.setLdapBindPassword(ldapTO.getPassword());
			}
		//}
	}
	static void validateLdapTO(TSiteBean siteBean, LdapTO ldapTO, List<ControlError> errors, Locale locale){
		// The transfer object will be null if everything is disabled...
		if (ldapTO == null) {
			ldapTO = new LdapTO();
			ldapTO.setEnabled(false);
		}
		if (ldapTO.isEnabled()) {
			if (ldapTO.getServerURL() == null || ldapTO.getServerURL().trim().length()==0) {
				List<String> controlPath = new LinkedList<String>();
				controlPath.add(LdapTO.JSONFIELDS.tabLdap);
				controlPath.add(LdapTO.JSONFIELDS.fsLdap);
				controlPath.addAll(JSONUtility.getPathInHelpWrapper(LdapTO.JSONFIELDS.serverURL));
				errors.add(new ControlError(controlPath, getText("admin.server.config.err.invalidLdapServerName", locale)));
			}
		}
	}


	/**
	 *
	 * @param siteBean contains all data for the server configuration
	 * @return
	 */
	static OtherSiteConfigTO getOtherSiteConfigTO(TSiteBean siteBean){
		OtherSiteConfigTO otherSiteConfigTO=new OtherSiteConfigTO();
		otherSiteConfigTO.setCbaAllowed(siteBean.getIsCbaAllowed());
		otherSiteConfigTO.setAttachmentRootDir(siteBean.getAttachmentRootDir());
		otherSiteConfigTO.setBackupDir(siteBean.getBackupDir());
		otherSiteConfigTO.setMaxAttachmentSize(siteBean.getMaxAttachmentSize());
		otherSiteConfigTO.setServerURL(siteBean.getServerURL());
		otherSiteConfigTO.setDescriptionLength(siteBean.getDescriptionLength());
		otherSiteConfigTO.setSelfRegisterAllowed(siteBean.getIsSelfRegisterAllowedBool());
		otherSiteConfigTO.setAutomaticGuestLogin(siteBean.getAutomaticGuestLogin());
		otherSiteConfigTO.setDemoSite(siteBean.getIsDemoSiteBool());
		otherSiteConfigTO.setVersionReminder(siteBean.getIsVersionReminderOn());
		//otherSiteConfigTO.setGraphvizPath(siteBean.getExecutable1());
		otherSiteConfigTO.setWebserviceEnabled(siteBean.getIsWSOn());
		otherSiteConfigTO.setAutomatedDatabaseBackup(siteBean.getIsDatabaseBackupJobOn());
		otherSiteConfigTO.setProjectSpecificIDsOn(siteBean.getProjectSpecificIDsOn());
		otherSiteConfigTO.setSummaryItemsBehavior(siteBean.getSummaryItemsBehavior());
		otherSiteConfigTO.setBudgetActive(siteBean.getBudgetActive());
		return otherSiteConfigTO;
	}

	/**
	 *
	 * @param siteBean contains all data for the server configuration
	 * @param otherSiteConfigTO
	 *
	 */
	static void updateOtherSiteConfig(TSiteBean siteBean, OtherSiteConfigTO otherSiteConfigTO){
		if (otherSiteConfigTO==null) {
			otherSiteConfigTO = new OtherSiteConfigTO();
		}
		siteBean.setIsCbaAllowed(otherSiteConfigTO.isCbaAllowed());
		String originalAttachRootDir=siteBean.getAttachmentRootDir();
		String newAttachRootDir=otherSiteConfigTO.getAttachmentRootDir();


		if(EqualUtils.notEqual(originalAttachRootDir, newAttachRootDir)){
			copyAttach(originalAttachRootDir, newAttachRootDir, true);
		}

		String originalDbBackupDir = siteBean.getBackupDir();
		String newDbBackupDir = otherSiteConfigTO.getBackupDir();
		if (originalDbBackupDir!=null && EqualUtils.notEqual(originalDbBackupDir, newDbBackupDir)) {
			try{
				File source=new File(originalDbBackupDir);
				File target=new File(newDbBackupDir);
				FileUtil.copyDirectory(source, target);
				FileUtil.deltree(source);
			}catch (Exception e) {
				LOGGER.error("Error moving db Backup dir from " + originalDbBackupDir + " to " + newDbBackupDir + " failed with " + e.getMessage(), e);
				LOGGER.error(Support.readStackTrace(e));
			}
		}

		siteBean.setAttachmentRootDir(otherSiteConfigTO.getAttachmentRootDir());

		if (otherSiteConfigTO.getBackupDir() == null || otherSiteConfigTO.getBackupDir().trim().length() < 1) {
			otherSiteConfigTO.setBackupDir(otherSiteConfigTO.getAttachmentRootDir() + File.separator + HandleHome.DB_BACKUP_DIR);
		}

		siteBean.setBackupDir(otherSiteConfigTO.getBackupDir());

		siteBean.setMaxAttachmentSize(otherSiteConfigTO.getMaxAttachmentSize());
		siteBean.setServerURL(otherSiteConfigTO.getServerURL());
		siteBean.setDescriptionLength(otherSiteConfigTO.getDescriptionLength());

		siteBean.setIsSelfRegisterAllowedBool(otherSiteConfigTO.isSelfRegisterAllowed());

		siteBean.setAutomaticGuestLogin(otherSiteConfigTO.isAutomaticGuestLogin());

		siteBean.setIsDemoSiteBool(otherSiteConfigTO.isDemoSite());

		siteBean.setIsVersionReminderOn(otherSiteConfigTO.isVersionReminder());

		//String theGraphvizExe = otherSiteConfigTO.getGraphvizPath();
		//siteBean.setExecutable1(otherSiteConfigTO.getGraphvizPath());

		siteBean.setIsWSOn(otherSiteConfigTO.isWebserviceEnabled());
		siteBean.setIsDatabaseBackupJobOn(otherSiteConfigTO.isAutomatedDatabaseBackup());
		siteBean.setProjectSpecificIDsOn(otherSiteConfigTO.isProjectSpecificIDsOn());
		if(otherSiteConfigTO.isProjectSpecificIDsOn()) {
			setDefaultPrefixToWorkspaceIfNotExists();
		}

		boolean oldSummaryItemsBehavior = siteBean.getSummaryItemsBehavior();
		boolean newSummaryItemsBehavior;
		if (ApplicationBean.getApplicationBean().isGenji() ||
				ApplicationBean.getApplicationBean().getAppType()==ApplicationBean.APPTYPE_DESK) {
			newSummaryItemsBehavior = true;
		} else {
			newSummaryItemsBehavior = otherSiteConfigTO.isSummaryItemsBehavior();
		}
		siteBean.setSummaryItemsBehavior(newSummaryItemsBehavior);
		if (!oldSummaryItemsBehavior && newSummaryItemsBehavior) {
			ItemBL.computeSummaryItems();
		} else {
			if (oldSummaryItemsBehavior && !newSummaryItemsBehavior) {
				ItemBL.resetSummaryItems();
			}
		}
		siteBean.setBudgetActive(otherSiteConfigTO.isBudgetActive());
	}

	static void validateOtherSiteConfig(TSiteBean siteBean,OtherSiteConfigTO otherSiteConfigTO,List<ControlError> errors, Locale locale){
		if (otherSiteConfigTO==null) {
			return;
		}
		String errorMessage = createAndCheckDirectory(otherSiteConfigTO.getAttachmentRootDir(), locale);
		if (errorMessage == null) {
			handleAttachRootDir(siteBean.getAttachmentRootDir(), otherSiteConfigTO.getAttachmentRootDir(), errors, locale );
		} else {
			List<String> controlPath = new LinkedList<String>();
			controlPath.add(OtherSiteConfigTO.JSONFIELDS.tabOtherSiteConfig);
			controlPath.add(OtherSiteConfigTO.JSONFIELDS.fsDirectories);
			controlPath.addAll(JSONUtility.getPathInHelpWrapper(OtherSiteConfigTO.JSONFIELDS.attachmentRootDir));
			errors.add(new ControlError(controlPath, errorMessage));
		}
		errorMessage = createAndCheckDirectory(otherSiteConfigTO.getBackupDir(), locale);
		if (errorMessage!=null) {
			List<String> controlPath = new LinkedList<String>();
			controlPath.add(OtherSiteConfigTO.JSONFIELDS.tabOtherSiteConfig);
			controlPath.add(OtherSiteConfigTO.JSONFIELDS.fsDirectories);
			controlPath.addAll(JSONUtility.getPathInHelpWrapper(OtherSiteConfigTO.JSONFIELDS.backupDir));
			errors.add(new ControlError(controlPath, errorMessage));
		}
	}

	public static String getText(String s, Locale locale){
		return LocalizeUtil.getLocalizedTextFromApplicationResources(s, locale);
	}

	static List<ControlError> save(TSiteBean siteApp, LicenseTO licenseTo,
			OutgoingEmailTO outgoingEmailTo,
			FullTextSearchTO fullTextSearchTo, LdapTO ldapTo, OtherSiteConfigTO otherSiteConfigTo,
			ApplicationBean appBean, Map application, Locale locale) {

		List<ControlError> errors = new LinkedList<ControlError>();
		//OutgoingEmailBL.validateOutgoingEmail(outgoingEmailTo, errors, locale);
		//IncomingEmailBL.validateIncomingEmail(incomingEmailTo, siteApp, errors, locale);
		SiteConfigBL.validateFullTextSearch(fullTextSearchTo,errors,locale);
		SiteConfigBL.validateLdapTO(siteApp, ldapTo,errors,locale);
		SiteConfigBL.validateOtherSiteConfig(siteApp, otherSiteConfigTo, errors, locale);

		if(!errors.isEmpty()){
			return  errors;
		}
		//we allow to save if license is invalid


		OutgoingEmailBL.updateOutgoingEmail(siteApp, outgoingEmailTo);


		SiteConfigBL.updateLicense(siteApp, licenseTo);
		SiteConfigBL.updateFullTextSearch(siteApp, fullTextSearchTo);
		SiteConfigBL.updateLdapTO(siteApp, ldapTo);
		SiteConfigBL.updateOtherSiteConfig(siteApp, otherSiteConfigTo);

		SiteConfigBL.saveTSite(siteApp, appBean, errors, application);
		return errors;
	}

	/**
	 *
	 * @param siteBean contains all data for the server configuration
	 * @param appBean
	 * @param errors
	 * @param application
	 */
	public static void saveTSite(TSiteBean siteBean, ApplicationBean appBean,
			List<ControlError> errors, Map application){
		boolean luceneBeforeActiv;
		boolean luceneAfterActiv;
		Analyzer analyzerBefore;
		Analyzer analyzerAfter;
		//the analyzer names will be compared because the equal() is not implemented for analyzers
		String analyzerBeforeName = "";
		String analyzerAfterName = null;
		try {
			if (siteBean != null) {
				LOGGER.debug("\n\r\n\r");
				LOGGER.debug("Before save: " + siteBean.toString());

				luceneBeforeActiv = LuceneUtil.isUseLucene();
				analyzerBefore = LuceneUtil.getAnalyzer();
				if (analyzerBefore!=null) {
					analyzerBeforeName = analyzerBefore.getClass().getName();
				}
				//the upgrade code runs parallel. The database version might get actualized
				//by the upgrade
				TSiteBean siteBeanSaved = siteDAO.load1();
				siteBean.setDbVersion(siteBeanSaved.getDbVersion());
				siteBean.setHistoryEntity(siteBeanSaved.getHistoryEntity());
				siteBean.setHistoryMigrationID(siteBeanSaved.getHistoryMigrationID());
				siteBean.setLicenseHolder(appBean.getLicenseHolder());
				siteDAO.save(siteBean);

				siteBean = appBean.setSiteParams(siteBean);
				appBean.setSiteBean(siteBean);

				siteDAO.save(siteBean);
				LOGGER.debug("saved site data to DB");

				LOGGER.debug("------------------------------------\r\n\r\n");
				application.put("SITECONFIG",siteBean);
				//initialize the lucene parameters
				LuceneUtil.configLuceneParameters(siteBean);
				luceneAfterActiv = LuceneUtil.isUseLucene();
				analyzerAfter = LuceneUtil.getAnalyzer();
				if (analyzerAfter!=null) {
					analyzerAfterName = analyzerAfter.getClass().getName();
				}
				//lucene was activated or analyzer changed when lucene was active
				if ((!luceneBeforeActiv && luceneAfterActiv) ||
						(luceneAfterActiv && !analyzerBeforeName.equals(analyzerAfterName))) {
					LuceneIndexer luceneIndexer = ApplicationBean.getApplicationBean().getLuceneIndexer(); //(LuceneIndexer)application.get(LuceneUtil.LUCENEINDEXER);
					if (luceneIndexer == null) {
						luceneIndexer = new LuceneIndexer();
						//application.put(LuceneUtil.LUCENEINDEXER, luceneIndexer);
						ApplicationBean.getApplicationBean().setLuceneIndexer(luceneIndexer);
					}
					if (ClusterBL.isSharedLuceneIndex()) {
						if (ClusterBL.getIAmTheMaster()) {
							if (luceneIndexer.isFinished()) {
								//is finished means not yet run or already finished indexing
								//reindex in a new thread (the modifiers are initialized inside run)
								ApplicationBean.getApplicationBean().getExecutor().execute(luceneIndexer);
								// Thread indexerThread = new Thread(luceneIndexer);
								// indexerThread.start();
							}
						} else {
							//mark full reindex for the master
							ClusterMarkChangesBL.markReindex();
						}
					} else {
						if (luceneIndexer.isFinished()) {
							//is finished means not yet run or already finished indexing
							ApplicationBean.getApplicationBean().getExecutor().execute(luceneIndexer);
						}
						//mark full reindex for the other cluster nodes
						ClusterMarkChangesBL.markReindex();
					}
				}

				//else Lucene was deactivated: LuceneUtil.isUseLucene() is false
				//that should stop the thread by itself if one runs

			}
			else {
				LOGGER.error("No site bean: this should never happen");
			}
		}
		catch (Exception e) {
			LOGGER.error("Problem getting form copied and saved to site " + e.getMessage());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
	}


	public static boolean testLdap(TSiteBean siteApp,String loginName, String ppassword, List<ControlError> errors,Locale locale){
		boolean b=false;
		try{
			b=LdapUtil.authenticate(siteApp,loginName,ppassword);
		}catch (NamingException ex){
			LOGGER.error("authenticate failed with " + ex);
			if(LOGGER.isDebugEnabled()){
				LOGGER.error(ExceptionUtils.getStackTrace(ex));
			}
			List<String> controlPath = new LinkedList<String>();
			errors.add(new ControlError(controlPath, ex.getMessage()));
		}
		if(!b){
			List<String> controlPath = new LinkedList<String>();
			controlPath.add(LdapTO.JSONFIELDS.tabLdap);
			controlPath.add(LdapTO.JSONFIELDS.fsLdap);
			controlPath.addAll(JSONUtility.getPathInHelpWrapper(LdapTO.JSONFIELDS.serverURL));
			errors.add(new ControlError(controlPath, getText("admin.server.config.err.invalidLdapServerName", locale)));
		}
		return b;
	}


	private static void handleAttachRootDir(String originalAttachRootDir,
			String newAttachRootDir, List<ControlError> errors, Locale locale) {
		File f=new File(newAttachRootDir);
		if(originalAttachRootDir!=null && originalAttachRootDir.trim().length()>0){
			File fileOriginal = new  File(originalAttachRootDir);
			if(fileOriginal.exists()){
				String originalAbsolutePath=fileOriginal.getAbsolutePath();
				String newAbsolutePath=f.getAbsolutePath();
				String[] dirsToCopy=new String[]{HandleHome.DATA_DIR,//attachments
						//HandleHome.DB_BACKUP_DIR,//database backups
						HandleHome.LOG_DIR,//logs
						HandleHome.LOGOS_DIR,//logos
						HandleHome.PLUGINS_DIR,//plugins
						HandleHome.REPORT_TEMPLATES_DIR,//reports
						HandleHome.WORD_TEMPLATES_DIR,
						HandleHome.XRESOURCES_DIR//xresources
				};
				boolean invalidDirNames=false;

				for (int i = 0; i < dirsToCopy.length; i++) {
					if(newAbsolutePath.startsWith(originalAbsolutePath+File.separator+dirsToCopy[i])){
						invalidDirNames=true;
						break;
					}
				}
				if(invalidDirNames){
					List<String> controlPath = new LinkedList<String>();
					controlPath.add(OtherSiteConfigTO.JSONFIELDS.tabOtherSiteConfig);
					controlPath.add(OtherSiteConfigTO.JSONFIELDS.fsDirectories);
					controlPath.addAll(JSONUtility.getPathInHelpWrapper(OtherSiteConfigTO.JSONFIELDS.attachmentRootDir));
					errors.add(new ControlError(controlPath, getText("admin.server.config.err.directory.InvalidName", locale)));
				}

			}
		}
	}


	private static void copyAttach(final String orginalDir, final String newDir, final boolean move){
		ApplicationBean.getApplicationBean().getExecutor().execute(new Runnable() {
				public void run() {
					String[] dirsToCopy=new String[]{HandleHome.DATA_DIR,//attachments
							//HandleHome.DB_BACKUP_DIR,//database backups
							HandleHome.LOG_DIR,//logs
							HandleHome.LOGOS_DIR,//logos
							HandleHome.PLUGINS_DIR,//plugins
							HandleHome.REPORT_TEMPLATES_DIR,//reports
							HandleHome.XRESOURCES_DIR//xresources
					};
					for (int i = 0; i < dirsToCopy.length; i++) {
						try{
							File source=new File(orginalDir+File.separator+dirsToCopy[i]);
							File target=new File(newDir+File.separator+dirsToCopy[i]);
							FileUtil.copyDirectory(source, target);
							if(move){
								FileUtil.deltree(source);
							}
						}catch (Exception e) {
							LOGGER.error("Error moving dir:"+dirsToCopy[i]);
							LOGGER.error(Support.readStackTrace(e));
						}
					}
					String[] filesToCopy = new String[] {
							HandleHome.TORQUE_FILE,
							HandleHome.QUARTZ_JOBS_FILE,
							HandleHome.FILTER_SUBSCRIPTIONS_FILE,
							HandleHome.GENERAL_SETTINGS_FILE,
							HandleHome.PDF_EXCEL_EXPORT_FILE/*,
							HandleHome.USER_LEVELS_FILE*/
					};
					for (String fileToCopy : filesToCopy) {
						File source=new File(orginalDir+File.separator+fileToCopy);
						File target=new File(newDir+File.separator+fileToCopy);
						if (move) {
							source.renameTo(target);
						} else {
							FileUtil.copyFile(source, target);
						}
					}
				}
			}
		);

//		new Thread(new Runnable() {
//			public void run() {
//				String[] dirsToCopy=new String[]{AttachBL.DataName,//attachments
//						DatabaseBackupBL.DB_BACKUP_DIR,//database backups
//						Constants.TEMPLATE_DIR//reports
//				};
//				for (int i = 0; i < dirsToCopy.length; i++) {
//					try{
//						File source=new File(orginalDir+File.separator+dirsToCopy[i]);
//						File target=new File(newDir+File.separator+dirsToCopy[i]);
//						FileUtil.copyDirectory(source, target);
//						if(move){
//							FileUtil.deltree(source);
//						}
//					}catch (Exception e) {
//						LOGGER.error("Error moving dir:"+dirsToCopy[i]);
//						LOGGER.error(Support.readStackTrace(e));
//					}
//				}
//			}
//		}).start();
	}


	public static TSiteBean loadTSite() {
		return siteDAO.load1();
	}

	public static void clearSMTPPassword() {
		siteDAO.clearSMTPPassword();
	}

	/*
	 * Helper routine to create a Lucene directory and see if it is writable
	 *
	 */
	public static String createAndCheckDirectory(String directoryName,
			/*List<ControlError> errors, List<String> controlPath,*/ Locale locale) {
		String keyBase = "admin.server.config.err.directory";
		if (directoryName==null || directoryName.trim().length()==0) {
			/*errors.add(new ControlError(controlPath, getText(keyBase+".DirectoryRequired", locale)));
			return errors;*/
			return getText(keyBase+".DirIsRequired", locale);
		}

		File f=new File(directoryName);

		if(f.isFile()) {
			/*errors.add(new ControlError(controlPath, getText(keyBase+".DirIsFile", locale)));
			return errors;*/
			return getText(keyBase+".DirIsFile", locale);
		}

		if(!f.isAbsolute()) {
			/*errors.add(new ControlError(controlPath, getText(keyBase+".DirIsRelative", locale)));
			return errors;*/
			return getText(keyBase+".DirIsRelative", locale);
		}

		// if the directory does not exist, create it
		if (!f.exists()) {
			LOGGER.info("Creating directory " + directoryName);
			try {
				f.mkdir();
			} catch (Exception e) {
				/*errors.add(new ControlError(controlPath, getText(keyBase+".CantWrite", locale)));
				return errors;*/
				return getText(keyBase+".CantWrite", locale);
			}
		}

		if (!f.canWrite() || !f.canRead()) {
			/*errors.add(new ControlError(controlPath, getText(keyBase+".CantAccess", locale)));
			return errors;*/
			return getText(keyBase+".CantAccess", locale);
		}
		//return errors;
		return null;
	}

	/*
	 * Helper routine to check for correct e-mail format
	 */
	public static boolean validateEmail(String email){
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher m = p.matcher(email);
		return m.matches();
	}

	public static Perl5Pattern getSiteAllowedEmailPattern(TSiteBean site){
		Perl5Pattern pattern=null;
		String patternString = site.getAllowedEmailPattern();
		if (patternString == null) {
			//patternString = "\\w[-.\\w]+\\@[-.\\w]+\\.\\w{2,3}";
			patternString =  "[^@ \t]@(\\w|-|_)+\\.\\w+";
		}
		Perl5Compiler pc = new Perl5Compiler();
		try {
			pattern=(Perl5Pattern)pc.compile(patternString,Perl5Compiler.CASE_INSENSITIVE_MASK |Perl5Compiler.SINGLELINE_MASK);
		} catch (MalformedPatternException e) {
			LOGGER.error("Malformed Email Domain Pattern.\n"
					+ "Setting default value");
			patternString = "\\w[-.\\w]+\\@[-.\\w]+\\.\\w{2,3}";
			try {
				pattern=(Perl5Pattern)pc.compile(patternString,Perl5Compiler.CASE_INSENSITIVE_MASK |Perl5Compiler.SINGLELINE_MASK);
			}
			catch (Exception me) {
				// now we better give up ...
			}
		}
		return pattern;
	}

	/**
	 * This method set a default value for workspace prefix in case if not exists,
	 * when project specific id (Workspace specific item no.) is enabled.
	 * @param otherSiteConfigTO
	 */
	public static void setDefaultPrefixToWorkspaceIfNotExists() {
		List<TProjectBean>projects = ProjectBL.loadAll();
		for (TProjectBean tProjectBean : projects) {
			if(tProjectBean.getPrefix() == null || "".equals(tProjectBean.getPrefix())) {
				String prefix = "WS" + tProjectBean.getObjectID();
				if(tProjectBean.isPrivate()) {
					prefix = "PW" + tProjectBean.getObjectID();
				}
				tProjectBean.setPrefix(prefix);
				ProjectBL.save(tProjectBean);
			}
		}
	}
}
