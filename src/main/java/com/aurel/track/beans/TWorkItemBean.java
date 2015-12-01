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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.aurel.track.dao.DAOFactory;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.CustomCompositeBaseRT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.types.FieldTypeManager;

/**
 * This class represents the core objects in the Genji system, a work item.
 * The work item carries two classes of attributes: those that are not logged
 * with their own history, and those that are logged with their own history.
 * The latter class is made up of state changes, comments, and the start and
 * end dates.
 *
 */
public class TWorkItemBean
	extends com.aurel.track.beans.base.BaseTWorkItemBean
	implements Serializable, IBeanID, ILabelBean, ISerializableLabelBean {

	/**
	 * Definitions for time related behavior.
	 */
	public interface DUE_FLAG {
		/**
		 * There is no planning information associated with this issue.
		 */
		public static int NOT_PLANNED = 0;
		/**
		 * This issue has been closed on or before its due date.
		 */
		public static int CLOSED_ON_PLAN = 1;
		/**
		 * This issue has been closed after its due date.
		 */
		public static int CLOSED_LATE = 2;
		/**
		 * This issues due date is still in the future.
		 */
		public static int ON_PLAN = 3;
		/**
		 * This issue is due soon. What soon means is defined in the users profile by the
		 * parameter for advanced notification (number of days in advance).
		 */
		public static int DUE_SOON = 4;
		/**
		 * This issues due date has already been passed.
		 */
		public static int OVERDUE = 5;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 340L;

	public static String LINKCHAR = "_";
	public static String PREFIX_LETTER = "f";

	/**
	 * An issue can be marked as public or private. Private issues can only
	 * be seen by their originators, and nobody else. Public issues
	 * can be seen by anyone having proper access permissions.
	 */
	public static Integer ACCESS_LEVEL_PUBLIC = Integer.valueOf(0);
	/**
	 * An issue can be marked as public or private. Private issues can only
	 * be seen by their originators, and nobody else. Public issues
	 * can be seen by anyone having proper access permissions.
	 */
	public static Integer ACCESS_LEVEL_PRIVATE = Integer.valueOf(1);

	/**
	 * Issue can be archived and deleted, and thereby removed from all regular search results.
	 * Archiving and deleting issues does not remove them from the database, however. It just
	 * marks them so they can be filtered by regular filters. Project administrators can also
	 * include archived and deleted issues in their result sets, and retrieve these issues back.
	 */
	public static Integer ARCHIVE_LEVEL_UNARCHIVED = 0;
	/**
	 * This marks an issue as archived.
	 */
	public static Integer ARCHIVE_LEVEL_ARCHIVED = 1;
	/**
	 * This marks an issue as deleted.
	 */
	public static Integer ARCHIVE_LEVEL_DELETED = 2;

	/**
	 * The outdent flag in context information
	 */
	public static String OUTDENT = "outdent";

	/**
	 * Next siblings in context informationm
	 */
	public static String NEXT_SIBLINGS = "nextSiblings";

	/**
	 * Map to store the customAttributes
	 */
	private Map<String, Object> customAttributeValues;

	/**
	 * The duration of the task
	 */
	private Double duration;

	/**
	 * The top down duration of the task
	 */
	private Double topDownDuration;

	/**
	 *  The comment added to the item.
	 */
	private String comment;

	/**
	 * The attachment field for history (by adding/deleting/editing a history).
	 */
	private String attachment;

	/**
	 * Is the copy deep, that is does it include the entire issue history?
	 */
	private boolean deepCopy;

	/**
	 * Is the copy of the attachments required?
	 */
	private boolean copyAttachments;

	/**
	 * is the copy of watchers required?
	 */
	private boolean copyWatchers;

	/**
	 * Is the copy of the issues children required?
	 */
	private boolean copyChildren;

	/**
	 * Is moving of the issue children required?
	 */
	private boolean moveChildren;
	
	/**
	 * Whether this workItem is editable
	 * TODO this should be moved into a WorkItemBeanTO
	 */
	private boolean editable = false;

	/**
	 * The constructor.
	 */
	public TWorkItemBean() {
		super();
	}

	/**
	 * Check if this is a public or a private issue.
	 * @return True if this is a private issue, false if it is a publicly visible issue.
	 */
	public boolean isAccessLevelFlag() {
		if (getAccessLevel()==null) {
			return false;
		}
		if (ACCESS_LEVEL_PRIVATE.equals(getAccessLevel())) {
			return true;
		}
		return false;
	}

	/**
	 * Mark this issue as private (can only be seen by its originator) or public.
	 * @param isPrivate true, if this issue should be marked private, false if it should be marked as public
	 */
	public void setAccessLevelFlag(boolean isPrivate) {
		if (isPrivate) {
			setAccessLevel(ACCESS_LEVEL_PRIVATE);
		} else {
			setAccessLevel(null);
		}
	}

	/**
	 * @return whether the workItem is archived or deleted
	 */
	public boolean isArchivedOrDeleted() {
		if (getArchiveLevel()==null) {
			return false;
		}
		if (getArchiveLevel().intValue()==ARCHIVE_LEVEL_ARCHIVED.intValue() ||
				getArchiveLevel().intValue()==ARCHIVE_LEVEL_DELETED.intValue()) {
			return true;
		}
		return false;
	}

	/**
	 * @return whether the issue is marked as archived
	 */
	public boolean isArchived() {
		if (getArchiveLevel()==null) {
			return false;
		}
		if (getArchiveLevel().intValue()==ARCHIVE_LEVEL_ARCHIVED.intValue()) {
			return true;
		}
		return false;
	}

	/**
	 * @return whether the issue is marked as deleted
	 */
	public boolean isDeleted() {
		if (getArchiveLevel()==null) {
			return false;
		}
		if (getArchiveLevel().intValue()==ARCHIVE_LEVEL_DELETED.intValue()) {
			return true;
		}
		return false;
	}

	public boolean isMilestone() {
		return BooleanFields.TRUE_VALUE.equals(getTaskIsMilestone());
	}

	public void setMilestone(boolean isMilestone) {
		setTaskIsMilestone(BooleanFields.fromBooleanToString(isMilestone));
	}

	/**
	 * Used for getting the label for parent workItem
	 */
	@Override
	public String getLabel() {
		return getSynopsis();
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	public Double getTopDownDuration() {
		return topDownDuration;
	}

	public void setTopDownDuration(Double topDownDuration) {
		this.topDownDuration = topDownDuration;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}


	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	/**
	 * @return the customAttributeValues
	 */
	public Map<String, Object> getCustomAttributeValues() {
		if(customAttributeValues==null){
			customAttributeValues=new HashMap<String, Object>();
		}
		return customAttributeValues;
	}

	/**
	 * @param customAttributeValues the customAttributeValues to set
	 */
	public void setCustomAttributeValues(Map<String, Object> customAttributeValues) {
		this.customAttributeValues = customAttributeValues;
	}

	/**
	 * Gets the value of a field:
	 * <ul>
	 *   <li>system fields: get the value directly from the workItemBean</li>
	 *   <li>custom fields: get the value from the customAttributeValues map:
	 *	   <ul>
	 *		 <li>single custom fields: a single value from map</li>
	 *		 <li>composite custom fields: a list of values from the map</li>
	 *	   </ul>
	 *   </li>
	 * @param fieldID the object id of the field (custom attribute)
	 * @return the value of the attribute
	 */
	public Object getAttribute(Integer fieldID){
		switch(fieldID.intValue()){
		case SystemFields.PROJECT:
			return getProjectID();
		case SystemFields.ISSUETYPE:
			return getListTypeID();
		case SystemFields.STATE:
			return getStateID();
		case SystemFields.MANAGER:
			return getOwnerID();
		case SystemFields.RESPONSIBLE:
			return getResponsibleID();
		case SystemFields.RELEASENOTICED:
			return getReleaseNoticedID();
		case SystemFields.RELEASESCHEDULED:
			return getReleaseScheduledID();
		case SystemFields.PRIORITY:
			return getPriorityID();
		case SystemFields.SEVERITY:
			return getSeverityID();
		case SystemFields.ISSUENO:
			return getObjectID();
		case SystemFields.ORIGINATOR:
			return getOriginatorID();
		case SystemFields.CREATEDATE:
			return getCreated();
		case SystemFields.LASTMODIFIEDDATE:
			return getLastEdit();
		case SystemFields.SYNOPSIS:
			return getSynopsis();
		case SystemFields.BUILD:
			return getBuild();
		case SystemFields.STARTDATE:
			return getStartDate();
		case SystemFields.ENDDATE:
			return getEndDate();
		case SystemFields.DESCRIPTION:
			return getDescription();
		case SystemFields.SUPERIORWORKITEM:
			return getSuperiorworkitem();
		case SystemFields.ACCESSLEVEL:
			return getAccessLevel();
		case SystemFields.COMMENT:
			return getComment();
		case SystemFields.CHANGEDBY:
			return getChangedByID();
		case SystemFields.ARCHIVELEVEL:
			return getArchiveLevel();
		case SystemFields.SUBMITTEREMAIL:
			return getSubmitterEmail();
		case SystemFields.WBS:
			return getWBSOnLevel();
		case SystemFields.PROJECT_SPECIFIC_ISSUENO:
			return getIDNumber();
		case SystemFields.TOP_DOWN_START_DATE:
			return getTopDownStartDate();
		case SystemFields.TOP_DOWN_END_DATE:
			return getTopDownEndDate();
		case SystemFields.TASK_IS_MILESTONE:
			return isMilestone();
		case SystemFields.DURATION:
			return getDuration();
		case SystemFields.TOP_DOWN_DURATION:
			return getTopDownDuration();
		default:
			Map<String, Object> customAttributesMap = getCustomAttributeValues();
			if (customAttributesMap.isEmpty()) {
				return null;
			}
			String prefix = PREFIX_LETTER + fieldID + LINKCHAR;
			if (customAttributesMap.containsKey(prefix)) {
				//single custom field
				return customAttributesMap.get(prefix);
			} else {
				//composite custom field
				int i=1;
				Map<Integer, Object> compositeValueMap = new HashMap<Integer, Object>();
				//get the parts till keys are found (no problem if the value for key is null)
				while (customAttributesMap.containsKey(prefix + i)) {
					compositeValueMap.put(Integer.valueOf(i), customAttributesMap.get(prefix + i));
					i++;
				}
				if (!compositeValueMap.isEmpty()) {
					return compositeValueMap;
				}
			}
			return null;
		}
	}

	/**
	 * Get the set of all the fieldIDs for the specified custom fields
	 * @return
	 */
	public Set<Integer> getCustomFieldIDSet() {
		Set<Integer> customFieldIDSet = new HashSet<Integer>();
		if (customAttributeValues==null || customAttributeValues.isEmpty()) {
			return customFieldIDSet;
		}
		for (String key : customAttributeValues.keySet()) {
			key = key.substring(LINKCHAR.length());
			String[] parts = MergeUtil.splitKey(key);
			Integer fieldID = Integer.valueOf(parts[0]);
			customFieldIDSet.add(fieldID);
		}
		return customFieldIDSet;
	}

	/**
	 * Get the set of all the keys for the specified custom fields
	 * @return
	 */
	public static String getPropertyName(Integer fieldID){
		switch(fieldID.intValue()){
		case SystemFields.PROJECT:
			return "projectID";
		case SystemFields.ISSUETYPE:
			return "listTypeID";
		case SystemFields.STATE:
			return "stateID";
		case SystemFields.MANAGER:
			return "ownerID";
		case SystemFields.RESPONSIBLE:
			return "responsibleID";
		case SystemFields.RELEASENOTICED:
			return "releaseNoticedID";
		case SystemFields.RELEASESCHEDULED:
			return "releaseScheduledID";
		case SystemFields.PRIORITY:
			return "priorityID";
		case SystemFields.SEVERITY:
			return "severityID";
		case SystemFields.ISSUENO:
			return "objectID";
		case SystemFields.ORIGINATOR:
			return "originatorID";
		case SystemFields.CREATEDATE:
			return "created";
		case SystemFields.LASTMODIFIEDDATE:
			return "lastEdit";
		case SystemFields.SYNOPSIS:
			return "synopsis";
		case SystemFields.BUILD:
			return "build";
		case SystemFields.STARTDATE:
			return "startDate";
		case SystemFields.ENDDATE:
			return "endDate";
		case SystemFields.DESCRIPTION:
			return "description";
		case SystemFields.SUPERIORWORKITEM:
			return "superiorworkitem";
		case SystemFields.ACCESSLEVEL:
			return "accessLevelFlag";
		case SystemFields.COMMENT:
			return "comment";
		case SystemFields.CHANGEDBY:
			return "changedByID";
		case SystemFields.ARCHIVELEVEL:
			return "archiveLevel";
		case SystemFields.SUBMITTEREMAIL:
			return "submitterEmail";
		case SystemFields.WBS:
			return "WBS";
		case SystemFields.PROJECT_SPECIFIC_ISSUENO:
			return "IDNumber";
		case SystemFields.TOP_DOWN_START_DATE:
			return "topDownStartDate";
		case SystemFields.TOP_DOWN_END_DATE:
			return "topDownEndDate";
		case SystemFields.TASK_IS_MILESTONE:
			return "isMilestone";
		case SystemFields.DURATION:
			return "duration";
		case SystemFields.TOP_DOWN_DURATION:
			return "topDownDuration";
		default:
			return "customAttributeValues."+PREFIX_LETTER+fieldID+LINKCHAR;
		}
	}

	@Override
	public String toString(){
		StringBuffer str = new StringBuffer();
		str.append("TWorkItem:\n");
		str.append("ObjectID = ").append(getObjectID()).append("\n");
		str.append("ProjectID = ").append(getProjectID()).append("\n");
		str.append("OwnerID = ").append(getOwnerID()).append("\n");
		str.append("ChangedByID = ").append(getChangedByID()).append("\n");
		str.append("OriginatorID = ").append(getOriginatorID()).append("\n");
		str.append("ResponsibleID = ").append(getResponsibleID()).append("\n");
		str.append("ListTypeID = ").append(getListTypeID()).append("\n");
		str.append("PriorityID = ").append(getPriorityID()).append("\n");
		str.append("SeverityID = ").append(getSeverityID()).append("\n");
		str.append("Superiorworkitem = ").append(getSuperiorworkitem()).append("\n");
		str.append("Synopsis = ").append(getSynopsis()).append("\n");
		str.append("Description = ").append(getDescription()).append("\n");
		str.append("Reference = ").append(getReference()).append("\n");
		str.append("LastEdit = ").append(getLastEdit()).append("\n");
		str.append("ReleaseNoticedID = ").append(getReleaseNoticedID()).append("\n");
		str.append("ReleaseScheduledID = ").append(getReleaseScheduledID()).append("\n");
		str.append("Build = ").append(getBuild()).append("\n");
		str.append("StateID = ").append(getStateID()).append("\n");
		str.append("StartDate = ").append(getStartDate()).append("\n");
		str.append("EndDate = ").append(getEndDate()).append("\n");
		str.append("SubmitterEmail = ").append(getSubmitterEmail()).append("\n");
		str.append("Created = ").append(getCreated()).append("\n");
		str.append("ActualStartDate = ").append(getActualStartDate()).append("\n");
		str.append("ActualEndDate = ").append(getActualEndDate()).append("\n");
		str.append("Level = ").append(getLevel()).append("\n");
		str.append("AccessLevel = ").append(getAccessLevel()).append("\n");
		str.append("SubmitterEmail = ").append(getSubmitterEmail()).append("\n");
		str.append("WBSLevel = ").append(getWBSOnLevel()).append("\n");
		str.append("IDNumber = ").append(getIDNumber()).append("\n");
		str.append("TargetStartDate = ").append(getTopDownStartDate()).append("\n");
		str.append("TargetEndDate = ").append(getTopDownEndDate()).append("\n");
		str.append("TaskIsMilestone = ").append(isMilestone()).append("\n");
		str.append("Duration = ").append(getDuration()).append("\n");
		str.append("Top down duration = ").append(getTopDownDuration()).append("\n");
		str.append("CustomAttributes:\n");
		if(customAttributeValues!=null){
			Iterator<String> itrCustomAttributeKeys = customAttributeValues.keySet().iterator();
			while (itrCustomAttributeKeys.hasNext()){
				String mapKey = itrCustomAttributeKeys.next();
				str.append("\t"+mapKey+"="+customAttributeValues.get(mapKey)+"\n");
			}
		}
		return(str.toString());
	}

	/**
	 * Gets the value according to fieldID and parameterCode
	 * For system and single custom fields the result is the same as by getAttribute(Integer fieldID)
	 * For composite custom fields only a specific part is returned according to the parameterCode
	 * @param fieldID
	 * @param parameterCode can be null
	 * @return
	 */
	public Object getAttribute(Integer fieldID, Integer parameterCode) {
		switch(fieldID.intValue()) {
		case SystemFields.PROJECT:
			return getProjectID();
		case SystemFields.ISSUETYPE:
			return getListTypeID();
		case SystemFields.STATE:
			return getStateID();
		case SystemFields.MANAGER:
			return getOwnerID();
		case SystemFields.RESPONSIBLE:
			return getResponsibleID();
		case SystemFields.RELEASENOTICED:
			return getReleaseNoticedID();
		case SystemFields.RELEASESCHEDULED:
			return getReleaseScheduledID();
		case SystemFields.PRIORITY:
			return getPriorityID();
		case SystemFields.SEVERITY:
			return getSeverityID();
		case SystemFields.ISSUENO:
			return getObjectID();
		case SystemFields.ORIGINATOR:
			return getOriginatorID();
		case SystemFields.CREATEDATE:
			return getCreated();
		case SystemFields.LASTMODIFIEDDATE:
			return getLastEdit();
		case SystemFields.SYNOPSIS:
			return getSynopsis();
		case SystemFields.BUILD:
			return getBuild();
		case SystemFields.STARTDATE:
			return getStartDate();
		case SystemFields.ENDDATE:
			return getEndDate();
		case SystemFields.DESCRIPTION:
			return getDescription();
		case SystemFields.SUPERIORWORKITEM:
			return getSuperiorworkitem();
		case SystemFields.ACCESSLEVEL:
			return getAccessLevel();
		case SystemFields.COMMENT:
			return getComment();
		case SystemFields.CHANGEDBY:
			return getChangedByID();
		case SystemFields.ARCHIVELEVEL:
			return getArchiveLevel();
		case SystemFields.SUBMITTEREMAIL:
			return getSubmitterEmail();
		case SystemFields.WBS:
			return getWBSOnLevel();
		case SystemFields.PROJECT_SPECIFIC_ISSUENO:
			return getIDNumber();
		case SystemFields.TOP_DOWN_START_DATE:
			return getTopDownStartDate();
		case SystemFields.TOP_DOWN_END_DATE:
			return getTopDownEndDate();
		case SystemFields.TASK_IS_MILESTONE:
			return isMilestone();
		case SystemFields.DURATION:
			return getDuration();
		case SystemFields.TOP_DOWN_DURATION:
			return getTopDownDuration();
		default:
			if (parameterCode==null) {
				return getCustomAttributeValues().get(
						PREFIX_LETTER + fieldID + LINKCHAR);
			} else {
				return getCustomAttributeValues().get(
						PREFIX_LETTER + fieldID + LINKCHAR + parameterCode);
			}
		}
	}

	/**
	 * Sets the attribute according to fieldID and parameterCode
	 * @param fieldID
	 * @param parameterCode can be null
	 * @param attribute
	 */
	public void setAttribute(Integer fieldID, Integer parameterCode, Object attribute) {
		switch(fieldID.intValue()){
		case SystemFields.PROJECT:{
			setProjectID((Integer)attribute);
			break;
		}
		case SystemFields.ISSUETYPE:{
			setListTypeID((Integer)attribute);
			break;
		}
		case SystemFields.STATE:{
			setStateID((Integer)attribute);
			break;
		}
		case SystemFields.MANAGER:{
			setOwnerID((Integer)attribute);
			break;
		}
		case SystemFields.RESPONSIBLE:{
			setResponsibleID((Integer)attribute);
			break;
		}
		case SystemFields.RELEASENOTICED:{
			setReleaseNoticedID((Integer)attribute);
			break;
		}
		case SystemFields.RELEASESCHEDULED:{
			setReleaseScheduledID((Integer)attribute);
			break;
		}
		case SystemFields.PRIORITY:{
			setPriorityID((Integer)attribute);
			break;
		}
		case SystemFields.SEVERITY:{
			setSeverityID((Integer)attribute);
			break;
		}
		case SystemFields.ISSUENO:{
			setObjectID((Integer)attribute);
			break;
		}
		case SystemFields.ORIGINATOR:{
			setOriginatorID((Integer)attribute);
			break;
		}
		case SystemFields.CREATEDATE:{
			setCreated((Date)attribute);
			break;
		}
		case SystemFields.LASTMODIFIEDDATE:{
			setLastEdit((Date)attribute);
			break;
		}
		case SystemFields.SYNOPSIS:{
			setSynopsis((String)attribute);
			break;
		}
		case SystemFields.BUILD:{
			setBuild((String)attribute);
			break;
		}
		case SystemFields.STARTDATE:{
			setStartDate((Date)attribute);
			break;
		}
		case SystemFields.ENDDATE:{
			setEndDate((Date)attribute);
			break;
		}
		case SystemFields.DESCRIPTION:{
			setDescription((String)attribute);
			break;
		}
		case SystemFields.SUPERIORWORKITEM:{
			setSuperiorworkitem((Integer)attribute);
			break;
		}
		case SystemFields.ACCESSLEVEL:{
			setAccessLevel((Integer)attribute);
			break;
		}
		case SystemFields.COMMENT:
			setComment((String)attribute);
			break;
		case SystemFields.CHANGEDBY:
			setChangedByID((Integer)attribute);
			break;
		case SystemFields.ARCHIVELEVEL:{
			setArchiveLevel((Integer)attribute);
			break;
		}
		case SystemFields.SUBMITTEREMAIL:
			setSubmitterEmail((String)attribute);
			break;
		case SystemFields.WBS:
			setWBSOnLevel((Integer)attribute);
			break;
		case SystemFields.PROJECT_SPECIFIC_ISSUENO:
			setIDNumber((Integer)attribute);
			break;
		case SystemFields.TOP_DOWN_START_DATE:
			setTopDownStartDate((Date)attribute);
			break;
		case SystemFields.TOP_DOWN_END_DATE:
			setTopDownEndDate((Date)attribute);
			break;
		case SystemFields.TASK_IS_MILESTONE:
			setMilestone(attribute==null?false:(Boolean)attribute);
			break;
		case SystemFields.DURATION:
			setDuration((Double)attribute);
			break;
		case SystemFields.TOP_DOWN_DURATION:
			setTopDownDuration((Double)attribute);
			break;
		default:
			if (parameterCode==null) {
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);

				if (fieldTypeRT!=null && fieldTypeRT.isComposite()){
					//clear composite
					CustomCompositeBaseRT customCompositeBaseRT = (CustomCompositeBaseRT)fieldTypeRT;
					for (int i=0; i<customCompositeBaseRT.getNumberOfParts();i++) {
						getCustomAttributeValues().remove(PREFIX_LETTER+fieldID.toString() + LINKCHAR +(i+1));
					}
					if(attribute!=null) {
						try {
							Map<Integer, Object> map = (Map<Integer, Object>) attribute;
							setAttributeComposite(fieldID, map);
						} catch (Exception e) {
						}
					}
				} else {
					getCustomAttributeValues().put(
							PREFIX_LETTER+fieldID.toString()+ LINKCHAR, attribute);
				}
			} else {
				getCustomAttributeValues().put(
						PREFIX_LETTER+fieldID.toString() + LINKCHAR + parameterCode.toString(), attribute);
			}
		}
	}
	/**
	 * Sets the attribute according to fieldID and parameterCode
	 * @param fieldID
	 * @param attribute
	 */
	public void setAttribute(Integer fieldID, Object attribute) {
		setAttribute(fieldID, null, attribute);
	}

	/**
	 * Sets the attribute according to fieldID and parameterCode
	 * @param fieldID
	 * @param map
	 */
	public void setAttributeComposite(Integer fieldID, Map map) {
		Iterator it= map.keySet().iterator();
		while (it.hasNext()) {
			Integer parameterCode = (Integer) it.next();
			setAttribute(fieldID, parameterCode, map.get(parameterCode));
		}
	}

	/**
	 * Removes the attribute according to fieldID and parameterCode
	 * @param fieldID
	 * @param parameterCode can be null
	 */
	public void removeAttribute(Integer fieldID, Integer parameterCode) {
		switch(fieldID.intValue()){
		case SystemFields.PROJECT:{
			setProjectID(null);
			break;
		}
		case SystemFields.ISSUETYPE:{
			setListTypeID(null);
			break;
		}
		case SystemFields.STATE:{
			setStateID(null);
			break;
		}
		case SystemFields.MANAGER:{
			setOwnerID(null);
			break;
		}
		case SystemFields.RESPONSIBLE:{
			setResponsibleID(null);
			break;
		}
		case SystemFields.RELEASENOTICED:{
			setReleaseNoticedID(null);
			break;
		}
		case SystemFields.RELEASESCHEDULED:{
			setReleaseScheduledID(null);
			break;
		}
		case SystemFields.PRIORITY:{
			setPriorityID(null);
			break;
		}
		case SystemFields.SEVERITY:{
			setSeverityID(null);
			break;
		}
		case SystemFields.ISSUENO:{
			setObjectID(null);
			break;
		}
		case SystemFields.ORIGINATOR:{
			setOriginatorID(null);
			break;
		}
		case SystemFields.CREATEDATE:{
			setCreated(null);
			break;
		}
		case SystemFields.LASTMODIFIEDDATE:{
			setLastEdit(null);
			break;
		}
		case SystemFields.SYNOPSIS:{
			setSynopsis(null);
			break;
		}
		case SystemFields.BUILD:{
			setBuild(null);
			break;
		}
		case SystemFields.STARTDATE:{
			setStartDate(null);
			break;
		}
		case SystemFields.ENDDATE:{
			setEndDate(null);
			break;
		}
		case SystemFields.DESCRIPTION:{
			setDescription(null);
			break;
		}
		case SystemFields.SUPERIORWORKITEM:{
			setSuperiorworkitem(null);
			break;
		}
		case SystemFields.ACCESSLEVEL:{
			setAccessLevel(null);
			break;
		}
		case SystemFields.COMMENT:{
			setComment(null);
			break;
		}
		case SystemFields.CHANGEDBY:{
			setChangedByID(null);
			break;
		}
		case SystemFields.ARCHIVELEVEL:{
			setArchiveLevel(null);
			break;
		}
		case SystemFields.SUBMITTEREMAIL:{
			setSubmitterEmail(null);
			break;
		}
		case SystemFields.WBS:{
			setWBSOnLevel(null);
			break;
		}
		case SystemFields.PROJECT_SPECIFIC_ISSUENO:{
			setIDNumber(null);
			break;
		}
		case SystemFields.TOP_DOWN_START_DATE:
			setTopDownStartDate(null);
			break;
		case SystemFields.TOP_DOWN_END_DATE:
			setTopDownEndDate(null);
			break;
		case SystemFields.TASK_IS_MILESTONE:
			setMilestone(false);
			break;
		case SystemFields.DURATION:
			setDuration(null);
			break;
		case SystemFields.TOP_DOWN_DURATION:
			setTopDownDuration(null);
			break;
		default:
			if (parameterCode==null) {
				getCustomAttributeValues().remove(PREFIX_LETTER+
						fieldID.toString() + LINKCHAR);
			} else {
				getCustomAttributeValues().remove(PREFIX_LETTER+
						fieldID.toString() + LINKCHAR + parameterCode.toString());
			}
		}
	}

	/**
	 * Copies the content of the current object (system and custom attributes
	 * but no other dependent lists) to a new object
	 * No database access is needed
	 * @return
	 */
	public TWorkItemBean copyShallow() {
		TWorkItemBean workItemBeanCopy = new TWorkItemBean();
		int[] systemFields = SystemFields.getSystemFieldsArray();
		for (int systemField : systemFields) {
				workItemBeanCopy.setAttribute(systemField, this.getAttribute(systemField));
		}
		workItemBeanCopy.setComment(this.getComment());
		workItemBeanCopy.setDeepCopy(this.isDeepCopy());
		workItemBeanCopy.setCopyAttachments(this.isCopyAttachments());
		workItemBeanCopy.setCopyWatchers(this.isCopyWatchers());
		workItemBeanCopy.setCopyChildren(this.isCopyChildren());
		Map<String, Object> customAttributeClone=new HashMap<String, Object>();
		customAttributeClone.putAll(this.getCustomAttributeValues());
		workItemBeanCopy.setCustomAttributeValues(customAttributeClone);
		return workItemBeanCopy;
	}

	/**
	 * Copies the full content of the current object to a new object
	 * (also the dependent lists are taken into account)
	 * Database access is needed
	 * @return
	 */
	public TWorkItemBean copyDeep() {
		//get a deep copy of the original
		TWorkItemBean workItemBeanDeepCopy = DAOFactory.getFactory().getWorkItemDAO().copyDeep(this);
		if (workItemBeanDeepCopy!=null) {
			//remove the custom attribute values because
			//they are managed by the business logic explicitly
			//(to avoid a double saving)
			workItemBeanDeepCopy.setTAttributeValueBeans(null);
			//The custom attributes are explicitly managed (in performSave) and
			//the torque TAttributeValues dependency list is set to null to avoid
			//double saving of custom attributes
			Map<String, Object> customAttributeClone=new HashMap<String, Object>();
			customAttributeClone.putAll(this.getCustomAttributeValues());
			workItemBeanDeepCopy.setCustomAttributeValues(customAttributeClone);
			//the copy should has the configuration of the copy
			workItemBeanDeepCopy.setDeepCopy(this.isDeepCopy());
			workItemBeanDeepCopy.setCopyAttachments(this.isCopyAttachments());
			workItemBeanDeepCopy.setCopyChildren(this.isCopyChildren());
			workItemBeanDeepCopy.setCopyWatchers(this.isCopyWatchers());
			//as the attachments are managed by the business logic explicitly
			workItemBeanDeepCopy.setTAttachmentBeans(null);
			//the watchers are managed by the business logic explicitly
			workItemBeanDeepCopy.setTNotifyBeans(null);
			//not used any more
			workItemBeanDeepCopy.setTBaseLineBeans(null);
			workItemBeanDeepCopy.setTStateChangeBeans(null);
			workItemBeanDeepCopy.setTTrailBeans(null);
			//do not copy this
			workItemBeanDeepCopy.setTSummaryMailBeans(null);
			workItemBeanDeepCopy.setTPersonBasketBeans(null);
			workItemBeanDeepCopy.setTLastVisitedItemBeans(null);
			workItemBeanDeepCopy.setTReadIssueBeans(null);
			workItemBeanDeepCopy.setTItemTransitionBeans(null);

			//TODO uncomment to handle budget/costs by explicit business logic (with check box)

			//TODO uncomment to handle links by explicit business logic (with check box)

			return workItemBeanDeepCopy;
		} else {
			return null;
		}
	}

	public boolean isDeepCopy() {
		return deepCopy;
	}

	public void setDeepCopy(boolean deepCopy) {
		this.deepCopy = deepCopy;
	}

	public void setCopyAttachments(boolean copyAttachments) {
		this.copyAttachments = copyAttachments;
	}

	public boolean isCopyAttachments() {
		return copyAttachments;
	}

	public void setCopyChildren(boolean copyChildren) {
		this.copyChildren = copyChildren;
	}

	public boolean isCopyChildren() {
		return copyChildren;
	}

	public boolean isCopyWatchers() {
		return copyWatchers;
	}

	public void setCopyWatchers(boolean copyWatchers) {
		this.copyWatchers = copyWatchers;
	}

	public boolean isMoveChildren() {
		return moveChildren;
	}

	public void setMoveChildren(boolean moveChildren) {
		this.moveChildren = moveChildren;
	}

	public int calculateBottomUpDueDateOnPlan(Integer stateFlag, Integer daysBefore) {
		Date referenceDate = null;
		if (this.isMilestone()) {
			//milestone normally has only start date
			referenceDate = getStartDate();
			if (referenceDate==null) {
				//although should never happen, if only endDate is specified, then take that
				referenceDate = getEndDate();
			}
		} else {
			referenceDate = getEndDate();
		}
		return calculateDueDateOnPlan(referenceDate, stateFlag, daysBefore);
	}

	public int calculateTopDownDueDateOnPlan(Integer stateFlag, Integer daysBefore) {
		Date referenceDate = null;
		if (this.isMilestone()) {
			//milestone normally has only start date
			referenceDate = getTopDownStartDate();
			if (referenceDate==null) {
				//although should never happen, if only endDate is specified, then take that
				referenceDate = getTopDownEndDate();
			}
		} else {
			referenceDate = getTopDownEndDate();
		}
		return calculateDueDateOnPlan(referenceDate, stateFlag, daysBefore);
	}

	/**
	 * Calculate the due flag for a due date
	 * @param endDate
	 * @param stateFlag
	 * @param daysBefore
	 * @return
	 */
	private int calculateDueDateOnPlan(Date endDate, Integer stateFlag, Integer daysBefore) {
		if (endDate!=null) {
			// make sure that we wait until the last second of the
			// due date!
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(endDate);
			calendar.add(Calendar.DATE, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			Date exactEndDate = calendar.getTime();
			if (Integer.valueOf(TStateBean.STATEFLAGS.CLOSED).equals(stateFlag)) {
				//closed item
				if (getLastEdit().before(exactEndDate)) {
					return DUE_FLAG.CLOSED_ON_PLAN;
				} else {
					return DUE_FLAG.CLOSED_LATE;
				}
			} else {
				Date currentDate = new Date();
				if (currentDate.before(exactEndDate)) {
					if (daysBefore!=null) {
						calendar.add(Calendar.DATE, -daysBefore.intValue()-1);
						Date dueSoonDate = calendar.getTime();
						if (currentDate.before(dueSoonDate)) {
							return DUE_FLAG.ON_PLAN;
						} else {
							return DUE_FLAG.DUE_SOON;
						}
					} else {
						//no daysBefore specified
						return DUE_FLAG.ON_PLAN;
					}
				} else {
					return DUE_FLAG.OVERDUE;
				}
			}
		} else {
			//return true
			return DUE_FLAG.NOT_PLANNED;
		}
	}


	/**
	 * @param onPlanFlag the onPlan to set
	 */
	public static boolean isDateConflict(int onPlanFlag) {
		switch (onPlanFlag) {
		case TWorkItemBean.DUE_FLAG.CLOSED_LATE:
		case TWorkItemBean.DUE_FLAG.OVERDUE:
			return true;
		case TWorkItemBean.DUE_FLAG.CLOSED_ON_PLAN:
		case TWorkItemBean.DUE_FLAG.DUE_SOON:
		case TWorkItemBean.DUE_FLAG.NOT_PLANNED:
		case TWorkItemBean.DUE_FLAG.ON_PLAN:
			return false;
		default:
			return false;
		}
	}

	public boolean calculateTargetDateConflict(boolean targetStartDateHidden, boolean targetEndDateHidden) {
		Date endDate = getEndDate();
		Date targetEndDate = getTopDownEndDate();
		if (!targetEndDateHidden && endDate!=null && targetEndDate!=null && endDate.after(targetEndDate)) {
			return true;
		}
		Date startDate = getStartDate();
		Date targetStartDate = getTopDownStartDate();
		if (!targetStartDateHidden && startDate!=null && targetStartDate!=null && startDate.before(targetStartDate)) {
			return true;
		}
		return false;
	}

	public boolean isOverdue() {
		if (calculateBottomUpDueDateOnPlan(TStateBean.STATEFLAGS.ACTIVE, 0) == TWorkItemBean.DUE_FLAG.OVERDUE) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * Serialize a label bean to a dom element
	 * @return
	 */
	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}

	/**
	 * Deserialize the labelBean
	 * @param attributes
	 * @return
	 */
	@Override
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TWorkItemBean workItemBean = new TWorkItemBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			workItemBean.setObjectID(new Integer(strObjectID));
		}
		workItemBean.setUuid(attributes.get("uuid"));
		return workItemBean;
	}


	/**
	 * Just to satisfy the interface. Returns always false. Only when the UUID matches the beans
	 * are considered equivalent.
	 * @param serializableLabelBean
	 * @param matchesMap	key: fieldID_paramaterCode
	 * 						value: map of already mapped external vs. internal objectIDs
	 * @return
	 */
	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		return false;
	}

	/**
	 * Just to satisfy the interface, will never be called.
	 * @param serializableLabelBean
	 * @param matchesMap
	 * @return
	 */
	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		return null;
	}

}
