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

import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.exchange.track.ExchangeFieldNames;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.util.PropertiesHelper;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TProjectBean
	extends com.aurel.track.beans.base.BaseTProjectBean
	implements Serializable, IHierarchicalBean, ISortedBean, ISerializableLabelBean {
	
	private static final long serialVersionUID = 1L;
	
	public static interface MOREPPROPS {
		
		public static String RECEIVING_SERVER_PROPERTY = "PopServer";
		public static String RECEIVING_PORT_PROPERTY = "PopPort";
		public static String RECEIVING_SECURITY_PROPERTY = "PopSecurity";
		public static String RECEIVING_USER_PROPERTY = "PopUser";
		public static String RECEIVING_PASSWORD_PROPERTY = "PopPassword";
		public static String TRACK_EMAIL_PROPERTY = "trackEmail";
		public static String EMAIL_PERSONAL_NAME = "emailPersonalName";
		public static String USE_TRACK_FROM_ADDRESS_DISPLAY = "useTrackFromAddressDisplay";
		public static String USE_TRACK_FROM_AS_REPLAY_TO = "useTrackFromAsReplayTo";
		public static String RECEIVING_PROTOCOL_PROPERTY = "receivingProtocol";
		public static String KEEP_MESSAGES_ON_SERVER = "keepMessagesOnServer";	
		public static String USE_VERSION_CONTROL_PROPERTY="useVersionControl";
		public static String VERSION_CONTROL_TYPE_PROPERTY="versionControlType";
		public static String VC_CHANGESET_LINK="vcChangeSetLink";
		public static String VC_ADDED_LINK="vcAddedLink";
		public static String VC_MODIFIED_LINK="vcModifiedLink";
		public static String VC_REPLACED_LINK="vcReplacedLink";
		public static String VC_DELETED_LINK="vcDeletedLink";
		public static String LINKING_PROPERTY = "linking";
		public static String USE_RELEASE = "useRelease";
		public static String DEFAULT_ISSUETYPE = "defaultIssueType";
		public static String DEFAULT_PRIORITY = "defaultPriority";
		public static String DEFAULT_SEVERITY = "defaultSeverity";
		public static String DEFAULT_CLASS = "defaultClass";
		public static String DEFAULT_SUBPROJECT = "defaultSubproject";
		public static String PREFILL = "prefill";
		public static String SHOW_ARCHIVED_RELEASE = "showArchivedRelease";
		
		public static String ACCOUNTING_INHERITED = "accountingInherited";
		public static String ACCOUNTING_PROPERTY = "Accounting";
		public static String WORK_TRACKING = "workTracking";
		public static String WORK_DEFAULT_UNIT = "workUnit";
		public static String COST_TRACKING = "costTracking";
		public static String DEFAULT_ACCOUNT = "defaultAccount";
		
		
		//MS Project specific properties		
		//needed to be stored here because changing a LagFormat for a link should 
		//result in recalculating the tenths of minute according to these values 
		public static String MINUTES_PER_DAY = "MinutesPerDay"; //used by changing the LagFormat to d 
		public static String MINUTES_PER_WEEK = "MinutesPerWeek"; //used by changing the LagFormat w
		public static String DAYS_PER_MONTH = "DaysPerMonth"; //used by changing the LagFormat mo
		//these properties are right now not needed to be stored here since they are available in the last 
		//import file at export time. Not needed because the MSProjectTaskBeans are not created at the time
		//of creating new Task type workItems, but are needed as soon they will be created. (When
		//at track+ UI there will be available to enter some  MSPRoject specific data: duration, constraintType, deadline etc.) 
		public static String DEFAULT_TASK_TYPE = "DefaultTaskType";//used default task type by exporting new tasks
		public static String DURATION_FORMAT = "DurationFormat";//the default DurationFormat for the duration for new tasks
		public static String RESOURCE_PERSON_MAPPINGS = "ResourcePersonMappings";
		public static String EMAIL_ENABLED = "EMEnabled";
	}

	public static interface PREFILL {
		public static int LASTWORKITEM = 1;
		public static int PROJECTDEFAULT = 2;
	}
	
	@Override
	public Comparable getSortOrderValue() {
		return getLabel();
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

	public boolean isLinkingActive() {
		return PropertiesHelper.getBooleanProperty(this.getMoreProps(), TProjectBean.MOREPPROPS.LINKING_PROPERTY);
	}
	
	public boolean isWokTrackingActive() {
		return PropertiesHelper.getBooleanProperty(this.getMoreProps(), TProjectBean.MOREPPROPS.WORK_TRACKING);
	}
	
	public boolean isCostTrackingActive() {
		return PropertiesHelper.getBooleanProperty(this.getMoreProps(), TProjectBean.MOREPPROPS.COST_TRACKING);
	}
	
	public boolean isPrivate() {
		return BooleanFields.TRUE_VALUE.equals(this.getIsPrivate());
	}
	
	public void setPrivate(boolean isPrivate) {
		setIsPrivate(BooleanFields.fromBooleanToString(isPrivate));
	}
	
	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("label", getLabel());
		attributesMap.put("uuid", getUuid());
		Integer defaultOwnerID = getDefaultOwnerID();
		if (defaultOwnerID!=null) {
			attributesMap.put("defaultOwnerID", defaultOwnerID.toString());
		}
		Integer defaultManagerID = getDefaultManagerID();
		if (defaultManagerID!=null) {
			attributesMap.put("defaultManagerID", defaultManagerID.toString());
		}
		Integer defaultInitStateID = getDefaultInitStateID();
		if (defaultInitStateID!=null) {
			attributesMap.put("defaultInitStateID", defaultInitStateID.toString());
		}
		Integer projectType = getProjectType();
		if (projectType!=null) {
			attributesMap.put("projectType", projectType.toString());
		}
		String versionSystemField0 = getVersionSystemField0();
		if (versionSystemField0!=null && !"".equals(versionSystemField0)) {	
			attributesMap.put("versionSystemField0", versionSystemField0);
		}
		String versionSystemField1 = getVersionSystemField1();
		if (versionSystemField1!=null && !"".equals(versionSystemField1)) {
			attributesMap.put("versionSystemField1", versionSystemField1);
		}
		String versionSystemField2 = getVersionSystemField2();
		if (versionSystemField2!=null && !"".equals(versionSystemField2)) {
			attributesMap.put("versionSystemField2", versionSystemField2);
		}
		String versionSystemField3 = getVersionSystemField3();
		if (versionSystemField3!=null && !"".equals(versionSystemField3)) {
			attributesMap.put("versionSystemField3", versionSystemField3);
		}
		attributesMap.put("deleted", getDeleted());
		Integer status = getStatus();
		if (status!=null) {
			attributesMap.put("status", status.toString());
		}
		String currencyName = getCurrencyName();
		if (currencyName!=null && !"".equals(currencyName)) {
			attributesMap.put("currencyName", currencyName);
		}
		String currencySymbol = getCurrencySymbol();
		if (currencySymbol!=null && !"".equals(currencySymbol)) {
			attributesMap.put("currencySymbol", currencySymbol);
		}
		Double hoursPerWorkDay = getHoursPerWorkDay();
		if (hoursPerWorkDay!=null) {
			attributesMap.put("hoursPerWorkDay", hoursPerWorkDay.toString());
		}
		String moreProps = getMoreProps();
		if (moreProps!=null && !"".equals(moreProps)) {
			attributesMap.put("moreProps", getMoreProps());
		}
		String tagLabel = getTagLabel();
		if (tagLabel!=null && !"".equals(tagLabel)) {
			attributesMap.put("tagLabel", tagLabel);
		}
		String prefix = getPrefix();
		if (prefix!=null && !"".equals(prefix)) {
			attributesMap.put("prefix", prefix);
		}
		Integer nextItemID = getNextItemID();
		if (nextItemID!=null) {
			attributesMap.put("nextItemID", nextItemID.toString());
		}
		Integer lastID = getLastID();
		if (lastID!=null) {
			attributesMap.put("lastID", lastID.toString());
		}
		Integer parent = getParent();
		if (parent!=null) {
			attributesMap.put("parent", parent.toString());
		}
		String isPrivate = getIsPrivate();
		if (isPrivate!=null) {
			attributesMap.put("isPrivate", isPrivate);
		}
		String description = getDescription();
		if (description!=null && !"".equals(description)) {
			attributesMap.put("description", description);
		}
		return attributesMap;
	}
	
	/**
	 * De-serialze the labelBean 
	 * @param attributes
	 * @return
	 */
	@Override
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TProjectBean projectBean = new TProjectBean();	
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			projectBean.setObjectID(new Integer(strObjectID));
		}
		projectBean.setUuid(attributes.get("uuid"));
		projectBean.setLabel(attributes.get("label"));
		
		String strDefaultOwner = attributes.get("defaultOwnerID");
		if (strDefaultOwner!=null) {
			projectBean.setDefaultOwnerID(new Integer(strDefaultOwner));
		}
		String strDefaultManager = attributes.get("defaultManagerID");
		if (strDefaultManager!=null) {
			projectBean.setDefaultManagerID(new Integer(strDefaultManager));
		}		
		String strDefaultInitStateID = attributes.get("defaultInitStateID");
		if (strDefaultInitStateID!=null) {
			projectBean.setDefaultInitStateID(new Integer(strDefaultInitStateID));
		}
		String strProjectType = attributes.get("projectType");
		if (strProjectType!=null) {
			projectBean.setProjectType(new Integer(strProjectType));
		}
		projectBean.setVersionSystemField0(attributes.get("versionSystemField0"));
		projectBean.setVersionSystemField1(attributes.get("versionSystemField1"));
		projectBean.setVersionSystemField2(attributes.get("versionSystemField2"));
		projectBean.setVersionSystemField3(attributes.get("versionSystemField3"));
		projectBean.setDeleted(attributes.get("deleted"));
		String strStatus = attributes.get("status");
		if (strStatus!=null) {
			projectBean.setStatus(new Integer(strStatus));
		}
		projectBean.setCurrencyName(attributes.get("currencyName"));
		projectBean.setCurrencySymbol(attributes.get("currencySymbol"));
		String strHoursPerWorkDay = attributes.get("hoursPerWorkDay");
		if (strHoursPerWorkDay!=null) {
			projectBean.setHoursPerWorkDay(new Double(strHoursPerWorkDay));
		}
		projectBean.setMoreProps(attributes.get("moreProps"));
		
		projectBean.setTagLabel(attributes.get("tagLabel"));
		projectBean.setPrefix(attributes.get("prefix"));
		
		String strNextItemID = attributes.get("nextItemID");
		if (strNextItemID!=null) {
			projectBean.setNextItemID(Integer.valueOf(strNextItemID));

		}
		String strLastID = attributes.get("lastID");
		if (strLastID!=null) {
			projectBean.setLastID(new Integer(strLastID));
		}
		projectBean.setDescription(attributes.get("description"));
		
		String strParent = attributes.get("parent");
		if (strParent!=null) {
			projectBean.setParent(Integer.valueOf(strParent));
		}
		projectBean.setIsPrivate(attributes.get("isPrivate"));
		return projectBean;
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
		TProjectBean projectBean = (TProjectBean)serializableLabelBean;
		String externalLabel = getLabel();
		String internalLabel = projectBean.getLabel();
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
		TProjectBean projectBean = (TProjectBean)serializableLabelBean;
		Integer projectTypeID = projectBean.getProjectType();
		if (projectTypeID!=null) {
			Map<Integer, Integer> projectTypeMap = 
				matchesMap.get(ExchangeFieldNames.PROJECTTYPE);
			projectBean.setProjectType(projectTypeMap.get(projectTypeID));
		}
		Integer projectStatus = projectBean.getStatus();
		if (projectStatus!=null) {
			Map<Integer, Integer> systemStatusMap = 
				matchesMap.get(ExchangeFieldNames.SYSTEMSTATE);
			projectBean.setStatus(systemStatusMap.get(projectStatus));
		}
		Integer initialStatus = projectBean.getDefaultInitStateID();
		if (initialStatus!=null) {
			Map<Integer, Integer> statusMap = 
				matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_STATE, null));
			projectBean.setDefaultInitStateID(statusMap.get(initialStatus));
		}
		Integer defaultManager = projectBean.getDefaultManagerID();
		Integer defaultResponsible = projectBean.getDefaultOwnerID();
		if (defaultManager!=null || defaultResponsible!=null) {
			Map<Integer, Integer> personMap = 
				matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PERSON, null));
			if (defaultManager!=null) {
				projectBean.setDefaultManagerID(personMap.get(defaultManager));
			}
			if (defaultResponsible!=null) {
				projectBean.setDefaultOwnerID(personMap.get(defaultResponsible));
			}
		}
		//actualize moreProps dependencies
		String moreProps = projectBean.getMoreProps();
		Integer defaultAccount = PropertiesHelper.getIntegerProperty(projectBean.getMoreProperties(), MOREPPROPS.DEFAULT_ACCOUNT);
		if (defaultAccount!=null) {
			Map<Integer, Integer> accountMap = matchesMap.get(ExchangeFieldNames.ACCOUNT);
			if (accountMap!=null && accountMap.get(defaultAccount)!=null) {
				moreProps = PropertiesHelper.setIntegerProperty(moreProps, MOREPPROPS.DEFAULT_ACCOUNT, accountMap.get(defaultAccount));
			}
		}
		Integer defaultIssueType = PropertiesHelper.getIntegerProperty(projectBean.getMoreProperties(), MOREPPROPS.DEFAULT_ISSUETYPE);
		if (defaultIssueType!=null) {
			Map<Integer, Integer> issueTypeMap = matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_ISSUETYPE, null));
			if (issueTypeMap!=null && issueTypeMap.get(defaultIssueType)!=null) {
				moreProps = PropertiesHelper.setIntegerProperty(moreProps, MOREPPROPS.DEFAULT_ISSUETYPE,issueTypeMap.get(defaultIssueType));
			}
		}
		Integer defaultPriority = PropertiesHelper.getIntegerProperty(projectBean.getMoreProperties(), MOREPPROPS.DEFAULT_PRIORITY);
		if (defaultPriority!=null) {
			Map<Integer, Integer> priorityMap = matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PRIORITY, null));
			if (priorityMap!=null && priorityMap.get(defaultPriority)!=null) {
				moreProps = PropertiesHelper.setIntegerProperty(moreProps, MOREPPROPS.DEFAULT_PRIORITY, priorityMap.get(defaultPriority));
			}
		}
		Integer defaultSeverity = PropertiesHelper.getIntegerProperty(projectBean.getMoreProperties(), MOREPPROPS.DEFAULT_SEVERITY);
		if (defaultSeverity!=null) {
			Map<Integer, Integer> severityMap = matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_SEVERITY, null));
			if (severityMap!=null && severityMap.get(defaultSeverity)!=null) {
				moreProps = PropertiesHelper.setIntegerProperty(moreProps, MOREPPROPS.DEFAULT_SEVERITY, severityMap.get(defaultSeverity));
			}
		}
		projectBean.setMoreProps(moreProps);
		
		Integer parentID = projectBean.getParent();
		if (parentID!=null) {
			Map<Integer, Integer> projectMap = 
					matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PROJECT, null));
			if (projectMap!=null) {
				projectBean.setParent(projectMap.get(parentID));
			}
		}
		return ProjectBL.save((TProjectBean)serializableLabelBean);
	}

}
