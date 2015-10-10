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


package com.aurel.track.report.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.user.userLevel.UserLevelBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.plugin.DashboardDescriptor;
import com.aurel.track.report.dashboardConfig.IPluginDashboard;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.screen.dashboard.bl.DashboardUtil;
import com.aurel.track.util.LabelValueBean;

/**
 * An base implementation for dashboard plugin
 * This class should be extended if you want to develop new dashboard plugin
 *
 * @author Adrian Bojani
 *
 */
public abstract class BasePluginDashboardView implements IPluginDashboard {

	public interface RENDERING_PARAMS {
		public static String BUNDLE_NAME="bundleName";
		public static String TITLE="title";
		public static String HAVE_TITLE="haveTitle";
		//configuration tool is available
		public static String USE_CONFIG="useConfig";
		//refresh tool is available
		public static String USE_REFRESH="useRefresh";
		//automatic refresh rate in seconds
		public static String REFRESH_RATE = "refresh";
		//whether general cockpit or project/release specific cockpit
		public static String BROWSE_PROJECT = "browseProject";

	}

	public interface CONFIGURATION_PARAMS {
		public static String LOCALE_STRING="localeString";
		public static String PROJECTID="projectID";
		public static String RELEASEID="releaseID";
	}

	/**
	 * Return the className as the plugin id
	 */
	public String getPluginID() {
		return this.getClass().getName();
	}


	/**
	 *  Convert/obtain the parameters obtain from configuration page in to the persistent parameters.
	 *
	 *	This implementation create a new map form configuration map, using  toString from values.
	 *	This implementation is usefully if you use only string values in configuration for dashboard.
	 *	If you need some parameters in configuration that are not of type String,
	 *	you should re-implement this method using proper conversion
	 */
	public Map<String,String> convertMapFromPageConfig(Map<String,String> properties, TPersonBean user,
			Locale locale, Integer entityId, Integer entityType, List<LabelValueBean> errors) {
		Map<String,String> map=new HashMap<String,String>();
		if (properties!=null) {
			for (Map.Entry<String, String> propertyEntry : properties.entrySet()) {
				String key = propertyEntry.getKey();
				String value = propertyEntry.getValue();
				if (value!=null) {
					value = convertValueByKey(key, value, locale);
					map.put(key, value);
				}
			}
			/*Iterator<String> it=properties.keySet().iterator();
			while (it.hasNext()) {
				String key=it.next();
				String value=properties.get(key);
				if(value!=null){
					map.put(key, value);
				}
			}*/
		}
		validateConfig(properties, user, locale, entityId, entityType, errors);
		return map;
	}

	/**
	 * Overwrite when a parameter value submitted as a string should be modified before save
	 * For ex. date should be saved always in a locale independent ISO format to be interpreted it same for each user
	 * @param key
	 * @param value
	 * @param locale
	 * @return
	 */
	protected String convertValueByKey(String key, String value, Locale locale) {
		return value;
	}

	/**
	 * Validates the submitted configuration parameters and add possible errors into errors list
	 * @param parameters
	 * @param personBean
	 * @param locale
	 * @param entityId
	 * @param entityType
	 * @param errors
	 */
	protected void validateConfig(Map<String,String> parameters, TPersonBean personBean,
			Locale locale, Integer entityId, Integer entityType, List<LabelValueBean> errors) {
	}

	protected DashboardDescriptor getDescriptor() {
		return DashboardUtil.getDescriptor(getPluginID());
	}
	protected boolean isUseConfig(){
		return false;
	}
	protected String localizeText(String key,Locale locale){
		DashboardDescriptor dashboardDescriptor = getDescriptor();
		String result;
		if(dashboardDescriptor!=null){
			result=LocalizeUtil.getLocalizedText(key, locale, dashboardDescriptor.getBundleName());
		}else{
			result=key;
		}
		return result;
	}
	protected String localizeText(String key,Locale locale,Object[] params){
		DashboardDescriptor dashboardDescriptor = getDescriptor();
		String result;
		if(dashboardDescriptor!=null){
			result=LocalizeUtil.getLocalizedTextWithParams(key, locale, dashboardDescriptor.getBundleName(),params);
		}else{
			result=key;
		}
		return result;
	}

	public String createJsonData(Integer dashboardID, Map<String, Object> session, Map<String, String> parameters,
								 Integer projectID, Integer releaseID,Map<String,String> ajaxParams){
		String title=parameters.get(RENDERING_PARAMS.TITLE);
		DashboardDescriptor desc=this.getDescriptor();
		if(title==null){
			title=desc.getLabel();
		}
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb, "dashboardID", dashboardID);
		TPersonBean personBean = (TPersonBean) session.get(Constants.USER_KEY);
		Boolean userConfig=personBean.getUserLevelMap().get(UserLevelBL.USER_LEVEL_ACTION_IDS.CONFIGURE_COCKPIT);
		if(userConfig!=null&&userConfig.booleanValue()){
			JSONUtility.appendBooleanValue(sb, RENDERING_PARAMS.USE_CONFIG, isUseConfig());
		}else{
			JSONUtility.appendBooleanValue(sb, RENDERING_PARAMS.USE_CONFIG, false);
		}
		Integer refresh=parseInteger(parameters, RENDERING_PARAMS.REFRESH_RATE);
		if (refresh!=null) {
			if (refresh>0 && refresh<60){
				refresh=60;
			}
		}
		if (refresh!=null) {
			JSONUtility.appendIntegerValue(sb, RENDERING_PARAMS.REFRESH_RATE, (refresh * 1000));
		}
		if(projectID!=null){
			JSONUtility.appendBooleanValue(sb, RENDERING_PARAMS.BROWSE_PROJECT, true);
		}else{
			JSONUtility.appendBooleanValue(sb, RENDERING_PARAMS.BROWSE_PROJECT, false);
		}
		try{
			sb.append(encodeJSONExtraData(dashboardID, session, parameters, projectID, releaseID,ajaxParams));
		}catch (TooManyItemsToLoadException e){
			//LOGGER.info("Number of items to load " + e.getItemCount());
			JSONUtility.appendBooleanValue(sb,"tooManyItems",true);
		} catch (Exception ex){
			List<LabelValueBean> localizedErrors=new ArrayList<LabelValueBean>();
			localizedErrors.add(new LabelValueBean(ex.getMessage(),"-1"));
			JSONUtility.appendLabelValueBeanList(sb,"errors",localizedErrors);
		}
		appendProjectJSON(sb, projectID, releaseID);
		JSONUtility.appendStringValue(sb, "title",title, true);
		sb.append("}");
		return sb.toString();

	}
	protected String encodeJSONExtraData(Integer dashboardID,Map<String, Object> session, Map<String, String> parameters,
		Integer projectID, Integer releaseID,Map<String,String> ajaxParams) throws TooManyItemsToLoadException {
		return "";
	}
	public void appendProjectJSON(StringBuilder sb,Integer projectID,Integer releaseID){
		if(projectID!=null){
			if(releaseID!=null){
				JSONUtility.appendIntegerValue(sb, "projectID", projectID);
				JSONUtility.appendIntegerValue(sb, "entityType", SystemFields.RELEASESCHEDULED);
				JSONUtility.appendIntegerValue(sb, "releaseID", releaseID);
			}else{
				JSONUtility.appendIntegerValue(sb, "projectID", projectID);
				JSONUtility.appendIntegerValue(sb, "entityType", SystemFields.PROJECT);
			}
		}
	}

	public String createJsonDataConfig(Map<String,String> parameters, TPersonBean user, Integer entityId, Integer entityType){
		String title=parameters.get(RENDERING_PARAMS.TITLE);
		DashboardDescriptor desc=this.getDescriptor();
		if(title==null){
			title=desc.getLabel();
		}
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		sb.append(encodeJSONExtraDataConfig(parameters,user,entityId,entityType));
		JSONUtility.appendStringValue(sb, "title",title, true);
		sb.append("}");
		return sb.toString();
	}
	public String encodeJSONExtraDataConfig(Map<String,String> parameters, TPersonBean user, Integer entityId, Integer entityType){
		return "";
	}
	public List<ReportBean> getIssues(Map<String, String> configParams, Map<String, String> filterParams, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException {
		return null;
	}

	/**
	 * Parses an integer from the string map
	 * @param params
	 * @param fieldName
	 * @return
	 */
	protected Integer parseInteger(Map<String,String> params, String fieldName){
		Integer result=null;
		if (params!=null && fieldName!=null) {
			String strValue=params.get(fieldName);
			if (strValue!=null){
				try{
					result=Integer.valueOf(strValue);
				} catch (Exception ex){
				}
			}
		}
		return result;
	}

	/**
	 * Parse an integer from string map.
	 * Return the default value if not found
	 * @param params
	 * @param fieldName
	 * @param defaultValue
	 * @return
	 */
	protected Integer parseInteger(Map<String, String> params, String fieldName, int defaultValue) {
		Integer result = parseInteger(params, fieldName);
		if (result==null) {
			result = defaultValue;
		}
		return result;
	}

	/**
	 * Parse a boolean value
	 * @param params
	 * @param key
	 * @return
	 */
	protected boolean parseBoolean(Map<String,String> params, String key){
		return parseBoolean(params, key, false);
	}

	/**
	 * Parse a boolean value
	 * @param params
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	protected boolean parseBoolean(Map<String,String> params, String key, boolean defaultValue){
		boolean result=defaultValue;
		if(params!=null&&key!=null){
			String strValue=params.get(key);
			if(strValue!=null){
				try{
					result=Boolean.valueOf(strValue);
				}catch (Exception ex){
					//ignore
				}
			}
		}
		return result;
	}

}
