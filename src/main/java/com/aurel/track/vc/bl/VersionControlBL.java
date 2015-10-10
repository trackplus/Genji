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

package com.aurel.track.vc.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TVersionControlParameterBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.plugin.ModuleDescriptor;
import com.aurel.track.plugin.PluginDescriptor;
import com.aurel.track.plugin.PluginManager;
import com.aurel.track.plugin.VersionControlDescriptor;
import com.aurel.track.plugin.VersionControlDescriptor.BrowserDescriptor;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.PropertiesHelper;
import com.aurel.track.util.StringArrayParameterUtils;
import com.aurel.track.vc.VersionControlPlugin;
import com.aurel.track.vc.VersionControlTO;

/**
 *
 */
public class VersionControlBL {
	private static final Logger LOGGER = LogManager.getLogger(VersionControlBL.class);

	private static Map cacheVCPluginClass;
	private static List<PluginDescriptor> versionControlDescriptors;

	public static List<LabelValueBean> validate(VersionControlTO vc,Map<String,String> vcMap,Locale locale){
		LOGGER.debug("Validate version Control....");
		LOGGER.debug("UseVersionControl:"+vc.isUseVersionControl()+" versionControlType="+vc.getVersionControlType());
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("VersionControlTO:\n"+vc);
			LOGGER.debug("vcMap:\n"+debugMap(vcMap));
		}
		List<LabelValueBean> errors=new ArrayList<LabelValueBean>();
		if(vc.isUseVersionControl()){
			String versionControlType=vc.getVersionControlType();
			VersionControlPlugin vcPlugin=getVersionPlugin4Type(versionControlType);
			if(vcPlugin==null){
				errors.add(new LabelValueBean("No version control plugin found for :"+versionControlType,"versionControlType"));
			}else{
				errors=vcPlugin.verify(vcMap,locale);
			}
		}
		return errors;
	}

	private static String debugMap(Map<String,String> vcMap){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(vcMap!=null){
			Iterator<String> it= vcMap.keySet().iterator();
			while(it.hasNext()){
				String key=it.next();
				String value=vcMap.get(key);
				if(key.equalsIgnoreCase("password")||
					key.equalsIgnoreCase("passphrase")||
					key.equalsIgnoreCase("psswd")){
					if(value!=null){
						value=value.replaceAll(".","*");
					}
				}
				sb.append(key).append(":").append(value);
				if(it.hasNext()){
					sb.append(",");
				}
				sb.append("\n");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	public static void save(Integer projectID,VersionControlTO vc,Map<String,String> vcMap){
		LOGGER.debug("Saving version control for project:"+projectID);
		TProjectBean projectBean=LookupContainer.getProjectBean(projectID);
		updateProject(projectBean,vc);
		ProjectBL.saveSimple(projectBean);
		if(vc.isUseVersionControl()){
			VersionControlBL.saveMapVersionControl(projectID,vcMap);
		}
	}
	public static void updateProject(TProjectBean project,VersionControlTO vc){
		//version Control
		if(vc.isUseVersionControl()){
			project.setMoreProps(PropertiesHelper.setProperty(project.getMoreProps(),
					TProjectBean.MOREPPROPS.USE_VERSION_CONTROL_PROPERTY,"true"));
			project.setMoreProps(PropertiesHelper.setProperty(project.getMoreProps(),
					TProjectBean.MOREPPROPS.VERSION_CONTROL_TYPE_PROPERTY,vc.getVersionControlType()));
		}else{
			project.setMoreProps(PropertiesHelper.setProperty(project.getMoreProps(), TProjectBean.MOREPPROPS.USE_VERSION_CONTROL_PROPERTY,"false"));
			project.setMoreProps(PropertiesHelper.removeProperty(project.getMoreProps(), TProjectBean.MOREPPROPS.VERSION_CONTROL_TYPE_PROPERTY));
		}
		project.setVersionSystemField1(vc.getBrowserID());
		project.setMoreProps(PropertiesHelper.setProperty(project.getMoreProps(),
				TProjectBean.MOREPPROPS.VC_CHANGESET_LINK,vc.getChangesetLink()));
		project.setMoreProps(PropertiesHelper.setProperty(project.getMoreProps(),
				TProjectBean.MOREPPROPS.VC_ADDED_LINK,vc.getAddedLink()));
		project.setMoreProps(PropertiesHelper.setProperty(project.getMoreProps(),
				TProjectBean.MOREPPROPS.VC_MODIFIED_LINK,vc.getModifiedLink()));
		project.setMoreProps(PropertiesHelper.setProperty(project.getMoreProps(),
				TProjectBean.MOREPPROPS.VC_REPLACED_LINK,vc.getReplacedLink()));
		project.setMoreProps(PropertiesHelper.setProperty(project.getMoreProps(),
				TProjectBean.MOREPPROPS.VC_DELETED_LINK,vc.getDeletedLink()));
	}
	public static VersionControlTO loadVersionControl(Integer projectID){
		TProjectBean projectBean=LookupContainer.getProjectBean(projectID);
		return extractVersionControl(projectBean);
	}
	public static VersionControlTO extractVersionControl(TProjectBean project){
		VersionControlTO vc=new VersionControlTO();
		boolean usingvc="true".equalsIgnoreCase(PropertiesHelper.getProperty(project.getMoreProperties(), TProjectBean.MOREPPROPS.USE_VERSION_CONTROL_PROPERTY));
		if(usingvc){
			vc.setUseVersionControl(new Boolean(true));
			String vcType=PropertiesHelper.getProperty(project.getMoreProperties(), TProjectBean.MOREPPROPS.VERSION_CONTROL_TYPE_PROPERTY);
			vc.setVersionControlType(vcType);
			vc.setMissing(missingVCType(getVersionControlPlugins(),vcType));

			vc.setBrowsers(getBrowserList(vcType));
			String browserID=project.getVersionSystemField1();
			if(browserID==null||browserID.trim().equals("")){
				browserID="-1";
			}
			vc.setBrowserID(browserID);
			String baseURL=project.getVersionSystemField0();
			vc.setBaseURL(baseURL);
			BrowserDescriptor browser=VersionControlBL.findBrowser(vcType,browserID);
			String editChangesetLink=PropertiesHelper.getProperty(project.getMoreProperties(), TProjectBean.MOREPPROPS.VC_CHANGESET_LINK);
			String editAddedLink=PropertiesHelper.getProperty(project.getMoreProperties(), TProjectBean.MOREPPROPS.VC_ADDED_LINK);
			String editModifiedLink=PropertiesHelper.getProperty(project.getMoreProperties(), TProjectBean.MOREPPROPS.VC_MODIFIED_LINK);
			String editReplacedLink=PropertiesHelper.getProperty(project.getMoreProperties(), TProjectBean.MOREPPROPS.VC_REPLACED_LINK);
			String editDeletedLink=PropertiesHelper.getProperty(project.getMoreProperties(), TProjectBean.MOREPPROPS.VC_DELETED_LINK);
			if(browser!=null){
				if(editChangesetLink==null||editChangesetLink.trim().length()==0){
					editChangesetLink=baseURL+browser.getChangesetLink();
				}
				if(editAddedLink==null||editAddedLink.trim().length()==0){
					editAddedLink=baseURL+browser.getAddedLink();
				}
				if(editModifiedLink==null||editModifiedLink.trim().length()==0){
					editModifiedLink=baseURL+browser.getModifiedLink();
				}
				if(editReplacedLink==null||editReplacedLink.trim().length()==0){
					editReplacedLink=baseURL+browser.getReplacedLink();
				}
				if(editDeletedLink==null||editDeletedLink.trim().length()==0){
					editDeletedLink=baseURL+browser.getDeletedLink();
				}
			}
			vc.setChangesetLink(editChangesetLink);
			vc.setAddedLink(editAddedLink);
			vc.setModifiedLink(editModifiedLink);
			vc.setReplacedLink(editReplacedLink);
			vc.setDeletedLink(editDeletedLink);
		}else{
			vc.setUseVersionControl(new Boolean(false));
			vc.setVersionControlType(null);
			vc.setBrowsers(new ArrayList());
		}
		Map params=laodMapVerisonControl(project.getObjectID());
		vc.setParameters(params);
		return vc;
	}

	private static Boolean missingVCType(List vcplugins,String pluginID){
		if(vcplugins==null||pluginID==null||pluginID.length()==0){
			return Boolean.FALSE;
		}
		for (int i = 0; i < vcplugins.size(); i++) {
			VersionControlDescriptor plugin=(VersionControlDescriptor) vcplugins.get(i);
			if(plugin.getId().equals(pluginID)){
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;

	}

	public static List<PluginDescriptor> getVersionControlPlugins() {
		if(versionControlDescriptors==null){
			versionControlDescriptors=PluginManager.getInstance().getVersionControlDescriptors();
		}
		return versionControlDescriptors;
	}

	public static VersionControlDescriptor getVersionControlDescriptor(
			String vcDescriptorID) {
		List descriptord = getVersionControlPlugins();
		VersionControlDescriptor result = null;
		for (int i = 0; i < descriptord.size(); i++) {
			VersionControlDescriptor descriptor = (VersionControlDescriptor) descriptord
					.get(i);
			if (descriptor.getId().equals(vcDescriptorID)) {
				result = descriptor;
				break;
			}
		}
		return result;
	}
	public static VersionControlPlugin getVersionPlugin4Type(String vcType){
		VersionControlDescriptor descriptor=getVersionControlDescriptor(vcType);
		if(descriptor==null){
			LOGGER.error("Version control plugin with  ID: "+vcType+" not found");
			return null;
		}
		String theClass=descriptor.getTheClassName();
		VersionControlPlugin vcplugin=VersionControlBL.getVersionControlPlugin(theClass);
		return vcplugin;
	}

	public static void saveMapVersionControl(Integer projectID, Map vcmap) {
		//encript password and passphrase
		char pass[]=("1"+"S"+"o"+"m"+"E"+"["+"K"+"e"+"y"+"]"+"ReD"+"17").toCharArray();
		String passd= (String) vcmap.get("password");
		if(passd!=null){
			String passCrypt=encrypt(passd,pass);
			vcmap.put("password",passCrypt);
		}
		String passphrase= (String) vcmap.get("passphrase");
		if(passphrase!=null){
			String passphraseCrypt=encrypt(passphrase,pass);
			vcmap.put("passphrase",passphraseCrypt);
		}
		DAOFactory.getFactory().getVersionControlParameterDAO().save(projectID,vcmap);
	}

	public static Map<String,String> laodMapVerisonControl(Integer projectID) {
		Map<String,String> vcmap = new HashMap<String, String>();
		List<TVersionControlParameterBean> params = DAOFactory.getFactory().getVersionControlParameterDAO().getByProject(projectID);
		if (params != null) {
			for (Iterator it = params.iterator(); it.hasNext();) {
				TVersionControlParameterBean param = (TVersionControlParameterBean) it
						.next();
				if (param.getParamValue() != null) {
					vcmap.put(param.getName(), param.getParamValue());
				}
			}
		}
		//decript password and passphrase
		char pass[]=("1"+"S"+"o"+"m"+"E"+"["+"K"+"e"+"y"+"]"+"ReD"+"17").toCharArray();
		String passd= vcmap.get("password");
		if(passd!=null){
			String passdcl=dcl(passd,pass);
			vcmap.put("password",passdcl);
		}
		String passphrase= vcmap.get("passphrase");
		if(passphrase!=null){
			String passdcl=dcl(passphrase,pass);
			vcmap.put("passphrase",passdcl);
		}
		return vcmap;
	}

	public static VersionControlPlugin getVersionControlPlugin(String className) {
		if (cacheVCPluginClass == null) {
			cacheVCPluginClass = new HashMap();
		}
		VersionControlPlugin plugin = (VersionControlPlugin) cacheVCPluginClass
				.get(className);
		if (plugin == null) {
			try {
				plugin = (VersionControlPlugin) Class.forName(className)
						.newInstance();
				cacheVCPluginClass.put(className, plugin);
			} catch (InstantiationException e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			} catch (IllegalAccessException e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			} catch (ClassNotFoundException e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return plugin;
	}

	 // Salt
	private static byte[] salt = {
				(byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
				(byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
	};

	// Iteration count
	static int  count = 20;
	private static String encrypt(String clearText, char[] password) {
		// Create PBE parameter set
		PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);
		byte[] ciphertext = {0};

		PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
		try {
			SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

			// Create PBE Cipher
			Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

			// Initialize PBE Cipher with key and parameters
			pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

			// Encrypt the cleartext
			ciphertext = pbeCipher.doFinal(clearText.getBytes());
		}
		catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return new String(Base64.encodeBase64String(ciphertext));
	}

	private static String dcl(String encryptedText, char[] password) {
		byte[] clearText = {' '};
		int count = 20;
		PBEKeySpec pbeKeySpec;
		PBEParameterSpec pbeParamSpec;
		SecretKeyFactory keyFac;
		// Create PBE parameter set
		pbeParamSpec = new PBEParameterSpec(salt, count);
		pbeKeySpec = new PBEKeySpec(password);
		try {
			keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

			// Create PBE Cipher
			Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

			// Initialize PBE Cipher with key and parameters
			pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);

		byte[] ciphertext = Base64.decodeBase64(encryptedText);

		// Decrypt the cleartext
			clearText = pbeCipher.doFinal(ciphertext);
		}
		catch (Exception e) {
			// LOGGER.debug(e.getStackTrace());
		}
		return new String(clearText);
	}
	public static BrowserDescriptor findBrowser(String pluginID,String browserID){
		if(browserID==null){
			return null;
		}
		VersionControlDescriptor descriptor=getVersionControlDescriptor(pluginID);
		if(descriptor!=null){
			List<BrowserDescriptor> browsers= descriptor.getBrowsers();
			if(browsers!=null){
				for (Iterator iterator = browsers.iterator(); iterator.hasNext();) {
					BrowserDescriptor browser = (BrowserDescriptor) iterator.next();
					if(browser.getId().equals(browserID)){
						return browser;
					}
				}
			}
		}
		return null;
	}
	public static List<BrowserDescriptor> getBrowserList(String pluginID){
		VersionControlDescriptor descriptor=getVersionControlDescriptor(pluginID);
		List<BrowserDescriptor> options=new ArrayList<VersionControlDescriptor.BrowserDescriptor>();
		BrowserDescriptor o=new BrowserDescriptor();
		o.setAddedLink("");
		o.setChangesetLink("");
		o.setDeletedLink("");
		o.setId("-1");
		o.setModifiedLink("");
		o.setName("");
		o.setReplacedLink("");
		options.add(o);
		if(descriptor!=null){
			List<BrowserDescriptor> browsers= descriptor.getBrowsers();
			if(browsers!=null){
				for (Iterator iterator = browsers.iterator(); iterator.hasNext();) {
					BrowserDescriptor browser = (BrowserDescriptor) iterator.next();
					o=new BrowserDescriptor();
					String baseUrl=browser.getBaseURL();
					if(baseUrl==null){
						baseUrl="";
					}
					o.setId(StringEscapeUtils.escapeJavaScript(browser.getId()));
					o.setName(StringEscapeUtils.escapeJavaScript(browser.getName()));

					o.setAddedLink(StringEscapeUtils.escapeJavaScript(baseUrl+browser.getAddedLink()));
					o.setChangesetLink(StringEscapeUtils.escapeJavaScript(baseUrl+browser.getChangesetLink()));
					o.setDeletedLink(StringEscapeUtils.escapeJavaScript(baseUrl+browser.getDeletedLink()));
					o.setModifiedLink(StringEscapeUtils.escapeJavaScript(baseUrl+browser.getModifiedLink()));
					o.setReplacedLink(StringEscapeUtils.escapeJavaScript(baseUrl+browser.getReplacedLink()));
					options.add(o);
				}
			}
		}
		return options;
	}

	public static void setIntegratedVersionControlBrowserValues(VersionControlDescriptor versionControlDesciptor, Integer projectID) {
		ModuleDescriptor webSVNModule = PluginManager.getInstance().getModuleByID("websvn");
		String webSVNBaseURL = "WEB_SVN_URL/";
		if(webSVNModule != null) {
			webSVNBaseURL = webSVNModule.getCleanedUrl();
			webSVNBaseURL = StringArrayParameterUtils.appendSlashToURLString(webSVNBaseURL);
		}
		BrowserDescriptor browser = versionControlDesciptor.findBrowser("integratedwebsvn");
		if(browser != null) {
			TProjectBean project = ProjectBL.loadByPrimaryKey(projectID);
			browser.setChangesetLink("listing.php?repname=" + project.getLabel() + "&rev=${rev}");
			browser.setAddedLink("filedetails.php?repname=" + project.getLabel() + "&path=${encodedPath}&rev=${rev}");
			browser.setModifiedLink("filedetails.php?repname=" + project.getLabel() + "&path=${encodedPath}&rev=${rev}");
			browser.setReplacedLink("filedetails.php?repname=" + project.getLabel() + "&path=${encodedPath}&rev=${rev}");
			browser.setDeletedLink("filedetails.php?repname=" + project.getLabel() + "&path=${encodedPath}&rev=${rev-1}");
			browser.setBaseURL(webSVNBaseURL);
		}
	}
}
