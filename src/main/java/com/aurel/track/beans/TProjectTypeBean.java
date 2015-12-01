/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */


package com.aurel.track.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ProjectTypeDAO;
import com.aurel.track.util.PropertiesHelper;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TProjectTypeBean
    extends com.aurel.track.beans.base.BaseTProjectTypeBean
    implements Serializable, ISerializableLabelBean
{

	private static final long serialVersionUID = 1L;
	
	public static interface MOREPPROPS {
		public static String USE_SUBSYSTEMS = "useSubsystems";
		public static String USE_RELEASES = "useReleases";
		public static String USE_ACCOUNTS = "useAccounts";
		public static String USE_VERSION_CONTROL = "useVersionControl";
		public static String USE_MS_PROJECT_EXPORT_IMPORT = "useMsProjectExportImport";
		public static String PROJECT_TYPE_FLAG = "projectTypeFlag";
		public static String CURRENCY_NAME = "currencyName";
		public static String CURRENCY_SYMBOL = "currencySymbol";
		public static String DEFAULT_WORK_UNIT = "workUnit";
		
		
	}
	private static ProjectTypeDAO projectTypeDAO = DAOFactory.getFactory().getProjectTypeDAO();
	
	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("label", getLabel());
		Double hoursPerWorkDay = getHoursPerWorkDay();
		if (hoursPerWorkDay!=null) {
			attributesMap.put("hoursPerWorkDay", hoursPerWorkDay.toString());
		}
		attributesMap.put("moreprops", getMoreProps());
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}
		
	/**
	 * Deserialze the labelBean 
	 * @param attributes
	 * @return
	 */
	@Override
	public  ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TProjectTypeBean projectTypeBean = new TProjectTypeBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			projectTypeBean.setObjectID(new Integer(strObjectID));
		}
		projectTypeBean.setLabel(attributes.get("label"));
		String strHoursPerWorkingDay = attributes.get("hoursPerWorkDay");
		if (strHoursPerWorkingDay!=null) {
			projectTypeBean.setHoursPerWorkDay(new Double(strHoursPerWorkingDay));
		}
		projectTypeBean.setMoreProps(attributes.get("moreprops"));
		projectTypeBean.setUuid(attributes.get("uuid"));
		return projectTypeBean;
	}
	
	/**
	 * Whether two label beans are equivalent 
	 * @param serializableLabelBean
	 * @param matchesMap	key: fieldID_paramaterCode
	 * 						value: map of already mapped external vs. internal objectIDs 
	 * @return
	 */
	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean, 
			Map<String, Map<Integer, Integer>> matchesMap) {
		if (serializableLabelBean==null) {
			return false;
		}
		TProjectTypeBean projectTypeBean = (TProjectTypeBean)serializableLabelBean;
		String externalLabel = getLabel();
		String internalLabel = projectTypeBean.getLabel();
		if (externalLabel!=null && internalLabel!=null) {
			if (externalLabel.equals(internalLabel)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Saves a serializableLabelBean into the database
	 * @param serializableLabelBean
	 * @param matchesMap
	 * @return
	 */
	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, 
			Map<String, Map<Integer, Integer>> matchesMap) {		
		return projectTypeDAO.save((TProjectTypeBean)serializableLabelBean);
	}
	
	@Override
	public void setMoreProps(String _moreProps) {
		moreProperties = PropertiesHelper.getProperties(_moreProps);
		super.setMoreProps(_moreProps);
	}
	
	protected Properties moreProperties = null;
	
	public Properties getMoreProperties() {
		return moreProperties;
	}

	public Boolean getUseSubsystems() {
		Boolean b=new Boolean(true);
		String usebString = PropertiesHelper.getProperty(getMoreProperties(), TProjectTypeBean.MOREPPROPS.USE_SUBSYSTEMS);
		if(usebString!=null&&usebString.equalsIgnoreCase("false")){
			b=new Boolean(false);
		}
		return b;
	}

	public void setUseSubsystems(Boolean useSubsystems) {
		String useStr=(useSubsystems==null?"false":useSubsystems.toString());
		setMoreProps(PropertiesHelper.setProperty(getMoreProps(), TProjectTypeBean.MOREPPROPS.USE_SUBSYSTEMS, useStr));
	}

	public Boolean getUseReleases() {
		Boolean b=null;
		if (getObjectID()!=null && getObjectID().intValue()<0) {
			//for private project type false 
			b = Boolean.FALSE;
		} else {
			//null means true for backward compatibility
			b=Boolean.TRUE;
		}
		String usebString = PropertiesHelper.getProperty(getMoreProperties(), TProjectTypeBean.MOREPPROPS.USE_RELEASES);
		if(usebString!=null&&usebString.equalsIgnoreCase("false")){
			b=new Boolean(false);
		}
		return b;
	}

	public void setUseReleases(Boolean useReleases) {
		String useStr=(useReleases==null?"false":useReleases.toString());
		setMoreProps(PropertiesHelper.setProperty(getMoreProps(), TProjectTypeBean.MOREPPROPS.USE_RELEASES, useStr));
	}

	public Boolean getUseAccounting() {
		if (getObjectID()!=null) {
			//for private project type false 
			return PropertiesHelper.getBooleanProperty(getMoreProperties(), TProjectTypeBean.MOREPPROPS.USE_ACCOUNTS);
		} else {
			//null means true for backward compatibility
			return Boolean.TRUE;
		}
		
	}

	public void setUseAccounting(Boolean useAccounts) {
		setMoreProps(PropertiesHelper.setBooleanProperty(getMoreProps(), TProjectTypeBean.MOREPPROPS.USE_ACCOUNTS, useAccounts));
	}
	
	public Boolean getUseVersionControl() {
		Boolean b=new Boolean(true);
		String usebString = PropertiesHelper.getProperty(getMoreProperties(), TProjectTypeBean.MOREPPROPS.USE_VERSION_CONTROL);
		if(usebString!=null&&usebString.equalsIgnoreCase("false")){
			b=new Boolean(false);
		}
		return b;
	}

	public void setUseVersionControl(Boolean useVersionControl) {
		String useStr=(useVersionControl==null?"false":useVersionControl.toString());
		setMoreProps(PropertiesHelper.setProperty(getMoreProps(), TProjectTypeBean.MOREPPROPS.USE_VERSION_CONTROL, useStr));
	}
	
	public Boolean getUseMsProjectExportImport() {
		if (getObjectID()!=null) {
			//for private project type false 
			return PropertiesHelper.getBooleanProperty(getMoreProperties(), TProjectTypeBean.MOREPPROPS.USE_MS_PROJECT_EXPORT_IMPORT);
		} else {
			//null means true for backward compatibility
			return Boolean.TRUE;
		}
		
	}

	public void setUseMsProjectExportImport(Boolean useMsProjectExportImport) {
		setMoreProps(PropertiesHelper.setBooleanProperty(getMoreProps(), TProjectTypeBean.MOREPPROPS.USE_MS_PROJECT_EXPORT_IMPORT, useMsProjectExportImport));
	}
	
	public Integer getProjectTypeFlag() {
		return PropertiesHelper.getIntegerProperty(getMoreProperties(), TProjectTypeBean.MOREPPROPS.PROJECT_TYPE_FLAG);
	}

	public void setProjectTypeFlag(Integer projectTypeFlag) {
		setMoreProps(PropertiesHelper.setIntegerProperty(getMoreProps(), TProjectTypeBean.MOREPPROPS.PROJECT_TYPE_FLAG, projectTypeFlag));
	}

	public Integer getDefaultWorkUnit() {
		return PropertiesHelper.getIntegerProperty(getMoreProperties(), TProjectTypeBean.MOREPPROPS.DEFAULT_WORK_UNIT);
	}

	public void setDefaultWorkUnit(Integer defaultWorkUnit) {
		setMoreProps(PropertiesHelper.setIntegerProperty(getMoreProps(), TProjectTypeBean.MOREPPROPS.DEFAULT_WORK_UNIT, defaultWorkUnit));
	}
	
	public String getCurrencyName() {
		return PropertiesHelper.getProperty(getMoreProperties(), TProjectTypeBean.MOREPPROPS.CURRENCY_NAME);
	}

	public void setCurrencyName(String currencyName) {
		setMoreProps(PropertiesHelper.setProperty(getMoreProps(), TProjectTypeBean.MOREPPROPS.CURRENCY_NAME, currencyName));
	}
	
	public String getCurrencySymbol() {
		return PropertiesHelper.getProperty(getMoreProperties(), TProjectTypeBean.MOREPPROPS.CURRENCY_SYMBOL);
	}

	public void setCurrencySymbol(String currencyName) {
		setMoreProps(PropertiesHelper.setProperty(getMoreProps(), TProjectTypeBean.MOREPPROPS.CURRENCY_SYMBOL, currencyName));
	}

}
