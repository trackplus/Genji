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

package com.aurel.track.persist;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.admin.customize.notify.trigger.NotifyTriggerBL;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.dao.FieldDAO;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.history.HistoryDAOUtils;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TFieldPeer
	extends com.aurel.track.persist.BaseTFieldPeer implements FieldDAO
{
	private static final Logger LOGGER = LogManager.getLogger(TFieldPeer.class);
	
	public static final long serialVersionUID = 400L;
	
	private static Class[] dependentPeerClasses = {
		TAttributeValuePeer.class,
	};
	
	private static String[] dependentFields = {
		TAttributeValuePeer.FIELDKEY,
	};

	//TNotifyFieldPeer needs extra care to remove the related entities also
	private static Class[] deletePeerClasses = {
		TReportLayoutPeer.class,
		TAttributeValuePeer.class,
		TRoleFieldPeer.class,
		TFieldConfigPeer.class,//overrided doDelete
		TScreenFieldPeer.class,
		TCardFieldPeer.class,
		TCardGroupingFieldPeer.class,
		TNavigatorColumnPeer.class,
		TNavigatorGroupingSortingPeer.class,
		TWorkflowActivityPeer.class,
		TWorkflowGuardPeer.class,
		BaseTFieldPeer.class
	};
	
	private static String[] deleteFields = {
		TReportLayoutPeer.REPORTFIELD,
		TAttributeValuePeer.FIELDKEY,
		TRoleFieldPeer.FIELDKEY,
		TFieldConfigPeer.FIELDKEY,
		TScreenFieldPeer.FIELDKEY,
		TCardFieldPeer.FIELDKEY,
		TCardGroupingFieldPeer.CARDFIELD,
		TNavigatorColumnPeer.FIELD,
		TNavigatorGroupingSortingPeer.FIELD,
		TWorkflowActivityPeer.ACTIVITYTYPE,
		TWorkflowGuardPeer.GUARDTYPE,
		BaseTFieldPeer.OBJECTID
	};
	
	
	/*private static Class[] replacePeerClasses = {
		TReportLayoutPeer.class,
		TAttributeValuePeer.class,
		TRoleFieldPeer.class,
		TFieldConfigPeer.class, //overrided doDelete
		TScreenFieldPeer.class,
		TFieldPeer.class	
	};
	
	private static String[] replaceFields = {
		TReportLayoutPeer.REPORTFIELD,
		TAttributeValuePeer.FIELDKEY,
		TRoleFieldPeer.FIELDKEY,
		TFieldConfigPeer.FIELDKEY,
		TScreenFieldPeer.FIELDKEY,
		TFieldPeer.OBJECTID
	};*/
	
	/**
	 * Loads the field by primary key
	 * @param objectID
	 * @return
	 */
	public TFieldBean loadByPrimaryKey(Integer objectID) {
		TField tField = null;
		try {
			tField = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.info("Loading of a field by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (tField!=null) {
			return tField.getBean();
		}
		return null;
	}
	
	/**
	 * Loads a field by name 
	 * @param name
	 * @return
	 */
	public List<TFieldBean> loadByName(String name) {
		Criteria crit = new Criteria();
		crit.add(NAME, name);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all fields failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads the fields by names
	 * @param names
	 * @return
	 */
	public List<TFieldBean> loadByNames(List<String> names) {
		if (names==null || names.isEmpty()) {
			return new LinkedList<TFieldBean>();
		}
		Criteria crit = new Criteria();
		crit.addIn(NAME, names);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all fields failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads all fields
	 * @return 
	 */
	public List<TFieldBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all fields failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Loads all active fields from TField table 
	 * @return 
	 */
	public List<TFieldBean> loadActive() {
		Criteria crit = new Criteria();
		crit.add(DEPRECATED, (Object)BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading active fields failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads all custom fields from TField table
	 * @return
	 */
	public List<TFieldBean> loadCustom() {
		Criteria crit = new Criteria();
		crit.add(ISCUSTOM, BooleanFields.TRUE_VALUE);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading custom fields failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Loads all system fields from TField table
	 * @return
	 */
	public List<TFieldBean> loadSystem() {
		Criteria crit = new Criteria();
		crit.add(ISCUSTOM, (Object)BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading system fields failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads all system fields from TField table
	 * @return
	 */
	public List<TFieldBean> loadFilterFields() {
		Criteria crit = new Criteria();
		crit.add(FILTERFIELD, BooleanFields.TRUE_VALUE);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading filter fields failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads all custom fieldBeans which are specified for a workItem 
	 * @param workItemID
	 * @return
	 */
	public List<TFieldBean> loadSpecifiedCustomFields(Integer workItemID) {
		Criteria crit = new Criteria();
		crit.addJoin(OBJECTID, BaseTAttributeValuePeer.FIELDKEY);
		crit.add(BaseTAttributeValuePeer.WORKITEM, workItemID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading custom fields failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Returns whether the name is unique for the field
	 * @param name
	 * @param fieldID
	 * @return
	 */
	public boolean isNameUnique(String name, Integer fieldID) {
		List<TField> fieldsList = null;
		Criteria crit = new Criteria();
		if (fieldID!=null) {
			crit.add(OBJECTID, fieldID, Criteria.NOT_EQUAL);
		}
		crit.add(NAME, name);
		try {
			fieldsList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Verifying the uniqueness of the name " + name + " and field " + fieldID + " failed with " + e.getMessage(), e);
		}
		if (fieldsList==null || fieldsList.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Saves a field in the TField table
	 * @param fieldBean
	 * @return
	 */
	public Integer save(TFieldBean fieldBean) {
		TField tField;
		try {
			tField = BaseTField.createTField(fieldBean);
			tField.save();
			return tField.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a field failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Deletes a field by primary key
	 * First deletes all configs related to the field
	 * then deletes the field itself
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	public void delete(Integer objectID) {
		//TODO make it into a transaction
		//delete from triggers
		new TNotifyFieldPeer().deleteByField(objectID, 
				NotifyTriggerBL.ACTIONTYPE.EDIT_ISSUE, NotifyTriggerBL.FIELDTYPE.ISSUE_FIELD);
		//delete from history
		HistoryDAOUtils.deleteFieldChangesByFieldID(objectID);
		//delete from other dependences
		ReflectionHelper.delete(deletePeerClasses, deleteFields, objectID);
	}
	
	/**
	 * delete workItems with the given criteria and delete all dependent database entries
	 * Called by reflection
	 * @param crit
	 */
	public static void doDelete(Criteria crit) {
		try {
			// retrieve all the fields first
			List<TField> list = doSelect(crit);
			if (list==null || list.size()<1) {
				return;
			}
			for (TField field : list) {
				new TFieldPeer().delete(field.getObjectID());
			}
		} catch (TorqueException e) {
			LOGGER.error("Cascade deleteing the field configs failed with " + e.getMessage(), e);
		}
		FieldTypeManager.getInstance().invalidateCache();
	}
	
	
	
	/**
	 * Verifies whether there are already values saved for this field in workItems
	 * If configs exist it does not matter (it will be deleted) 
	 * @param fieldId
	 */
	public boolean isDeletable(Integer objectID) {
		return !ReflectionHelper.hasDependentData(dependentPeerClasses, dependentFields, objectID);
	}
	
	/*public void replaceField(Integer oldFieldID, Integer newFieldID) {
		new TNotifyFieldPeer().updateByField(oldFieldID, newFieldID, 
				NotifyTriggerBL.ACTIONTYPE.EDIT_ISSUE, NotifyTriggerBL.FIELDTYPE.ISSUE_FIELD);
		ReflectionHelper.replace(replacePeerClasses, replaceFields, oldFieldID, newFieldID);
	}*/
	
	/**
	 * Sets the deprecated flag
	 * @param fieldId
	 */
	public void setDeprecated(Integer objectID, boolean deactivate) {
		TFieldBean fieldBean = loadByPrimaryKey(objectID); 
		if (deactivate) {
			fieldBean.setDeprecated("Y");
		} else {
			fieldBean.setDeprecated("N");
		}
		try {
			save(fieldBean);
		} catch (Exception e) {
			LOGGER.error("Deprecating the field " + objectID + " failed with: " + e);
		}
	}
	/**
	 * Load all  fields from screen
	 * @param screenID
	 * @return
	 */
	public List<TFieldBean> loadAllFields(Integer screenID){
		Criteria crit = new Criteria();
		crit.add(BaseTScreenPeer.OBJECTID,screenID);
		crit.addJoin(BaseTScreenPeer.OBJECTID, BaseTScreenTabPeer.PARENT);
		crit.addJoin(BaseTScreenTabPeer.OBJECTID, BaseTScreenPanelPeer.PARENT);
		crit.addJoin(BaseTScreenPanelPeer.OBJECTID, BaseTScreenFieldPeer.PARENT);
		crit.addJoin(BaseTScreenFieldPeer.FIELDKEY, OBJECTID);
		crit.setDistinct();
		try	{
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(TorqueException e){
			LOGGER.error("Load all  fields from screen:"+screenID+" failed with " + e.getMessage(), e);
			return null;
		}
	}

	public List<TFieldBean> loadAllFieldsOnCard(Integer cardPanelID){
		Criteria crit = new Criteria();
		crit.add(BaseTCardPanelPeer.OBJECTID,cardPanelID);
		crit.addJoin(BaseTCardPanelPeer.OBJECTID, BaseTCardFieldPeer.CARDPANEL);
		crit.addJoin(BaseTCardFieldPeer.FIELDKEY, OBJECTID);
		crit.setDistinct();
		try	{
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(TorqueException e){
			LOGGER.error("Load all  fields from card :"+cardPanelID+" failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Load all fields from screens
	 * @param screenIDs
	 * @return
	 */
	public List<TFieldBean> loadByScreens(Object[] screenIDs) {
		if (screenIDs!=null && screenIDs.length>0) {
			Criteria crit = new Criteria();
			crit.addIn(BaseTScreenPeer.OBJECTID,screenIDs);
			crit.addJoin(BaseTScreenPeer.OBJECTID, BaseTScreenTabPeer.PARENT);
			crit.addJoin(BaseTScreenTabPeer.OBJECTID, BaseTScreenPanelPeer.PARENT);
			crit.addJoin(BaseTScreenPanelPeer.OBJECTID, BaseTScreenFieldPeer.PARENT);
			crit.addJoin(BaseTScreenFieldPeer.FIELDKEY, OBJECTID);
			crit.setDistinct();
			try	{
				return convertTorqueListToBeanList(doSelect(crit));
			} catch(TorqueException e){
				LOGGER.error("Load all  fields from screens: "+screenIDs.length+" failed with " + e.getMessage(), e);
			}
		}
		return null;
	}
	
	/**
	 * Load fields by fieldID 
	 * @param fieldIDs
	 * @return
	 */
	public List<TFieldBean> loadByFieldIDs(Object[] fieldIDs) {
		if (fieldIDs!=null && fieldIDs.length>0) {
			Criteria crit = new Criteria();
			crit.addIn(OBJECTID,fieldIDs);
			try	{
				return convertTorqueListToBeanList(doSelect(crit));
			}
			catch(TorqueException e){
				LOGGER.error("Load all  fields by fieldIDs: "+fieldIDs.length+" failed with " + e.getMessage(), e);
			}
		}
		return null;
	}
	
	
	/**
	 * Load all custom fields from screen
	 * @param screenID
	 * @return
	 */
	public List<TFieldBean> loadAllCustomFields(Integer screenID){
		Criteria crit = new Criteria();
		crit.add(BaseTScreenPeer.OBJECTID,screenID);
		crit.addJoin(BaseTScreenPeer.OBJECTID, BaseTScreenTabPeer.PARENT);
		crit.addJoin(BaseTScreenTabPeer.OBJECTID, BaseTScreenPanelPeer.PARENT);
		crit.addJoin(BaseTScreenPanelPeer.OBJECTID, BaseTScreenFieldPeer.PARENT);
		crit.addJoin(BaseTScreenFieldPeer.FIELDKEY, OBJECTID);
		crit.add(BaseTFieldPeer.ISCUSTOM,"Y");
		try	{
			return convertTorqueListToBeanList(doSelect(crit));
		}
		catch(TorqueException e){
			LOGGER.error("Load all custom fields from screen:"+screenID+" failed with " + e.getMessage(), e);
			return null;
		}
	}
	/**
	 * Load the custom fields from tab
	 * @param screenTab
	 * @return
	 */
	public List<TFieldBean> loadCustomFieldsFromTab(Integer screenTabID) {
		Criteria crit = new Criteria();
		crit.add(ISCUSTOM, BooleanFields.TRUE_VALUE);
		crit.add(BaseTScreenPanelPeer.PARENT,screenTabID);
		crit.addJoin(BaseTScreenTabPeer.OBJECTID, BaseTScreenPanelPeer.PARENT);
		crit.addJoin(BaseTScreenPanelPeer.OBJECTID, BaseTScreenFieldPeer.PARENT);
		crit.addJoin(BaseTScreenFieldPeer.FIELDKEY, OBJECTID);
		try	{
			return convertTorqueListToBeanList(doSelect(crit));
		}
		catch(TorqueException e){
			LOGGER.error("Load all custom fields from screenTan:"+screenTabID+" failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Load the fields defined only in a project type
	 * @return
	 */
	public List<TFieldBean> loadByProjectType(Integer projectTypeID) {
		Criteria crit = new Criteria();
		crit.add(PROJECTTYPE, projectTypeID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading project type fields for " + projectTypeID + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Load the fields defined only in a project
	 * @return
	 */
	public List<TFieldBean> loadByProject(Integer projectID) {
		Criteria crit = new Criteria();
		crit.add(PROJECT, projectID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading project fields for " + projectID + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Converts a list of TField torque objects to a list of TFieldBean objects 
	 * @param torqueList
	 * @return
	 */
	private List<TFieldBean> convertTorqueListToBeanList(List<TField> torqueList) {
		List<TFieldBean> beanList = new LinkedList<TFieldBean>();
		if (torqueList!=null) {
			for (TField tField : torqueList) {
				beanList.add(tField.getBean());
			}
		}
		return beanList;
	}
}
