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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.dao.IssueTypeDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.workingdogs.village.Record;

/**
* Implementation of <code>TListTypePeer</code>. This class represents
* the <code>TCATEGORY</code> table in the database and provides convenient
* methods for operations on this table.
*
* @author Joerg Friedrich
* @version $Revision: 1229 $
*/

public class TListTypePeer
extends com.aurel.track.persist.BaseTListTypePeer implements IssueTypeDAO
{

	private static final long serialVersionUID = -2213384834906817350L;
	private static final Logger LOGGER = LogManager.getLogger(TListTypePeer.class);

	private static Class[] replacePeerClasses = {
		TWorkItemPeer.class,
		TInitStatePeer.class
	};

	private static String[] replaceFields = {
		TWorkItemPeer.CATEGORYKEY,
		TInitStatePeer.LISTTYPE
	};
	
	private static Class[] deletePeerClasses = {
		TRoleListTypePeer.class,
		TPstatePeer.class,
		TWorkFlowCategoryPeer.class,
		TPseverityPeer.class,
		TPlistTypePeer.class,
		TPpriorityPeer.class,
		TDisableFieldPeer.class,
		TFieldConfigPeer.class, //overrided doDelete
		TScreenConfigPeer.class,
		TChildIssueTypePeer.class,
		TChildIssueTypePeer.class,
		TWorkflowConnectPeer.class,
		TListTypePeer.class
	};

	private static String[] deleteFields = {
		TRoleListTypePeer.LISTTYPE,
		TPstatePeer.LISTTYPE,
		TWorkFlowCategoryPeer.CATEGORY,
		TPseverityPeer.LISTTYPE,
		TPlistTypePeer.CATEGORY,
		TPpriorityPeer.LISTTYPE,
		TDisableFieldPeer.LISTTYPE,
		TFieldConfigPeer.ISSUETYPE,
		TScreenConfigPeer.ISSUETYPE,
		TChildIssueTypePeer.ISSUETYPECHILD,
		TChildIssueTypePeer.ISSUETYPEPARENT,
		TWorkflowConnectPeer.ISSUETYPE,
		TListTypePeer.PKEY
	};

	/**
	 * Loads an issueType by primary key
	 * @param objectID
	 * @return
	 */
	public TListTypeBean loadByPrimaryKey(Integer objectID) {
		TListType tListType = null;
		try {
			tListType = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.info("Loading of an issueType by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
		if (tListType!=null) {
			return tListType.getBean();
		}
		return null;
	}
	
	/**
	 * Gets an issueTypeBean by label
	 * @param label
	 * @return
	 */
	public List<TListTypeBean> loadByLabel(String label) {
		Criteria crit = new Criteria();
		crit.add(LABEL, label);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading the issueType by label " + label +  " failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Loads all issueTypes
	 * @return
	 */
	public List<TListTypeBean> loadAll() {
		Criteria crit = new Criteria();
		crit.add(PKEY, 0, Criteria.NOT_EQUAL);
		crit.addAscendingOrderByColumn(SORTORDER);
		crit.addAscendingOrderByColumn(LABEL);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading of all issueTypes failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	
	/**
	 * Load all selectable item types
	 * @return
	 */
	public List<TListTypeBean> loadAllSelectable() {
		Criteria crit = new Criteria();
		crit.add(PKEY, 0, Criteria.NOT_EQUAL);
		addNonDocumentTypeFlags(crit);
		crit.addAscendingOrderByColumn(SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading issue types by typeFlag failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads all document type item types
	 */
	public List<TListTypeBean> loadAllDocumentTypes(){
		Criteria crit = new Criteria();
		addDocumentTypeFlags(crit);
		crit.addAscendingOrderByColumn(SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all documents failed with " + e.getMessage(), e);
			return null;
		}
	}

	
	/**
	 * Loads strict document type item types
	 * @return
	 */
	public List<TListTypeBean> loadStrictDocumentTypes() {
		Criteria crit = new Criteria();
		addDocumentTypeFlag(crit);
		crit.addAscendingOrderByColumn(SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all documents failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Adds only the non-document typeflags
	 * @param criteria
	 */
	private void addNonDocumentTypeFlags(Criteria criteria) {
		criteria.addNotIn(TYPEFLAG, new int[] {TListTypeBean.TYPEFLAGS.DOCUMENT_FOLDER,
				TListTypeBean.TYPEFLAGS.DOCUMENT, TListTypeBean.TYPEFLAGS.DOCUMENT_SECTION});
	}
	/**
	 * Adds only the document typeflags
	 * @param criteria
	 */
	private void addDocumentTypeFlags(Criteria criteria) {
		criteria.addIn(TYPEFLAG, new int[] {TListTypeBean.TYPEFLAGS.DOCUMENT_FOLDER,
				TListTypeBean.TYPEFLAGS.DOCUMENT, TListTypeBean.TYPEFLAGS.DOCUMENT_SECTION});
	}
	
	/**
	 * Adds only the document typeflags
	 * @param criteria
	 */
	private void addDocumentTypeFlag(Criteria criteria) {
		criteria.addIn(TYPEFLAG, new int[] {TListTypeBean.TYPEFLAGS.DOCUMENT});
	}
	
	/**
	 * Loads the issue types by IDs
	 * @param issueTypeIDs
	 */
	public List<TListTypeBean> loadByIssueTypeIDs(List<Integer> issueTypeIDs) {
		List<TListType> torqueList = new ArrayList<TListType>();
		if (issueTypeIDs==null || issueTypeIDs.isEmpty()) {
			LOGGER.info("No issueTypeIDs specified " + issueTypeIDs);
			return convertTorqueListToBeanList(torqueList);
		}
		Criteria crit = new Criteria();
		crit.addAscendingOrderByColumn(LABEL);
		crit.addIn(PKEY, issueTypeIDs); 
		try {
			torqueList = doSelect(crit);
		}
		catch (Exception e) {
			LOGGER.error("Loading of issueTypes by IDs failed with " + e.getMessage(), e);
			return convertTorqueListToBeanList(torqueList);
		}
		return convertTorqueListToBeanList(torqueList);
	}
	
	/**
	 * Return the issueTypeBean with a specific typeFlag
	 * @param typeFlag
	 * @return
	 */
	public List<TListTypeBean> loadByTypeFlag(int typeFlag) {
		Criteria crit = new Criteria();
		crit.add(TYPEFLAG, typeFlag);
		crit.addAscendingOrderByColumn(SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading issue types by typeFlag failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Load all issue types allowed for the project type
	 * @param projectType
	 * @return
	 */
	public List<TListTypeBean> loadAllowedByProjectType(Integer projectType) {
		return loadAllowedByProjectType(projectType, null);
	}
	
	/**
	 * Load all non document issue types allowed for the project type
	 * @param projectType
	 * @return
	 */
	public List<TListTypeBean> loadAllowedNonDocumentTypesByProjectType(Integer projectType) {
		return loadAllowedByProjectType(projectType, Boolean.FALSE);
	}
	
	/**
	 * Load all document issue types allowed for the project type
	 * @param projectType
	 * @return
	 */
	public List<TListTypeBean> loadAllowedDocumentTypesByProjectType(Integer projectType){
		return loadAllowedByProjectType(projectType, Boolean.TRUE);
	}

	/**
	 * if documents==true only documents else other than documents
	 * @param projectType
	 * @param documents
	 * @return
	 */
	private List<TListTypeBean> loadAllowedByProjectType(Integer projectType, Boolean documents) {
		if (projectType==null) {
			return new LinkedList<TListTypeBean>();
		}
		Criteria crit = new Criteria();
		crit.addJoin(TPlistTypePeer.CATEGORY, PKEY);
		crit.add(TPlistTypePeer.PROJECTTYPE, projectType);
		crit.add(PKEY, Integer.valueOf(0), Criteria.NOT_EQUAL);
		if (documents!=null) {
			if (documents.booleanValue()) {
				addDocumentTypeFlags(crit);
			} else {
				addNonDocumentTypeFlags(crit);
			}
		}
		crit.addAscendingOrderByColumn(SORTORDER);
		crit.addAscendingOrderByColumn(LABEL);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Getting the issueTypes allowed by projectType " + projectType + " failed with " + e.getMessage(), e);
			return new ArrayList<TListTypeBean>();
		}
	}
	
	/**
	 * Load all issueTypes which are allowed in the projectTypes
	 * @param projectTypeIDs
	 * @return
	 */
	public List<TListTypeBean> loadAllowedByProjectTypes(Object[] projectTypeIDs) {
		return loadAllowedByProjectTypes(projectTypeIDs,false);
	}
	public List<TListTypeBean> loadAllowedDocumentsByProjectTypes(Object[] projectTypeIDs){
		return loadAllowedByProjectTypes(projectTypeIDs,true);
	}

	/**
	 * if documents==true only documents else other than documents
	 * @param projectTypeIDs
	 * @param documets
	 * @return
	 */
	private  List<TListTypeBean> loadAllowedByProjectTypes(Object[] projectTypeIDs,boolean documets) {
		Criteria crit = new Criteria();
		crit.addJoin(PKEY, BaseTPlistTypePeer.CATEGORY);
		crit.addIn(BaseTPlistTypePeer.PROJECTTYPE, projectTypeIDs);
		crit.add(PKEY, Integer.valueOf(0), Criteria.NOT_EQUAL);
		if(documets){
			addDocumentTypeFlags(crit);
		}else{
			addNonDocumentTypeFlags(crit);
		}


		crit.addAscendingOrderByColumn(SORTORDER);
		crit.addAscendingOrderByColumn(LABEL);
		crit.setDistinct();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		}
		catch (Exception e) {
			LOGGER.error("Loading issueTypes which have project type restrictions failed with " + e.getMessage(), e);
			return new ArrayList<TListTypeBean>();
		}
	}
	
	/**
	 * Gets the next available sortorder
	 * @return
	 */
	public Integer getNextSortOrder() {
		Integer sortOrder = null;
		String max = "max(" + SORTORDER + ")";
		Criteria crit = new Criteria();
		crit.addSelectColumn(max);
		try {
			sortOrder = ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asIntegerObj();
		} catch (Exception e) {
			LOGGER.error("Getting the next sortorder for issue type failed with: " + e);
		}
		if (sortOrder==null) {
			return Integer.valueOf(1);
		} else {
			return Integer.valueOf(sortOrder.intValue()+1);
		}
	}
	
	/**
	 * Saves an issue type bean in the TListType table
	 * @param fieldBean
	 * @return
	 */
	public Integer save(TListTypeBean listTypeBean) {
		TListType tListType;
		try {
			tListType = BaseTListType.createTListType(listTypeBean);
			tListType.save();
			return tListType.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of an issue type bean failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	public boolean hasDependentData(Integer pkey) {
		return ReflectionHelper.hasDependentData(replacePeerClasses, replaceFields, pkey);
	}
	
	
	/** 
	 * This method replaces all occurrences of state value oldOID with
	 * state value newOID.
	 * @param oldOID
	 * @param newOID
	 */
	public void replace(Integer oldOID, Integer newOID) {
		ReflectionHelper.replace(replacePeerClasses, replaceFields, oldOID, newOID);
	}
	
	/**
	 * Deletes a state from the TState table 
	 * @param objectID
	 */
	public void delete(Integer objectID) {
		new TCardFieldOptionPeer().deleteOptionForField(SystemFields.INTEGER_ISSUETYPE, objectID);
		ReflectionHelper.delete(deletePeerClasses, deleteFields, objectID);
	}
	
	
	
	/**
	 * Load all issueTypes assigned to roles
	 * @param roleIDs
	 * @return
	 */
	public List<TListTypeBean> loadForRoles(Object[] roleIDs) {
		if (roleIDs==null || roleIDs.length==0) {
			return new ArrayList<TListTypeBean>();
		}
		Criteria crit = new Criteria();
		crit.addJoin(PKEY, BaseTRoleListTypePeer.LISTTYPE);
		crit.addIn(BaseTRoleListTypePeer.PROLE, roleIDs);
		crit.addAscendingOrderByColumn(SORTORDER);
		crit.setDistinct();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading the explicit list types failed with " + e.getMessage(), e);
			return new ArrayList<TListTypeBean>();
		}	
	}
	
	/**
	 * Returns the sort order column name
	 * @return
	 */
	public String getSortOrderColumn() {
		return "SORTORDER";
	}
	
	/**
	 * Returns the table name
	 * @return
	 */
	public String getTableName() {
		return TABLE_NAME;
	}
	
	private List<TListTypeBean> convertTorqueListToBeanList(List<TListType> torqueList) {
		List<TListTypeBean> beanList = new LinkedList<TListTypeBean>();
		if (torqueList!=null){
			Iterator<TListType> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(itrTorqueList.next().getBean());
			}
		}
		return beanList;
	}
	
}
