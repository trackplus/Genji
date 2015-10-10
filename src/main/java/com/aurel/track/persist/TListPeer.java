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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;

import com.aurel.track.beans.TListBean;
import com.aurel.track.dao.ListDAO;
import com.aurel.track.fieldType.constants.BooleanFields;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TListPeer
	extends com.aurel.track.persist.BaseTListPeer implements ListDAO
{
	private static final Logger LOGGER = LogManager.getLogger(TListPeer.class);
	
	/**
	 * Loads a list by primary key
	 * @param objectID
	 * @return
	 */
	public TListBean loadByPrimaryKey(Integer objectID) {
		TList tList = null;
		try {
			tList = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.info("Loading of a list by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
		if (tList!=null) {
			return tList.getBean();
		}
		return null;
	}
	
	/**
	 * Loads all lists
	 * @return
	 */
	public List<TListBean> loadAll() {
		Criteria criteria = new Criteria();
		criteria.addAscendingOrderByColumn(NAME);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		}
		catch (TorqueException e) {
			LOGGER.error("Loading the all lists failed with " + e.getMessage(), e);
			return null;
		}		
	}
	
	/**
	 * Loads all active public lists
	 * @deprecated
	 * @return
	 */
	/*public List loadActivePublicLists() {
		List activeLists = null;
		Criteria criteria;
		criteria = new Criteria();
		criteria.add(DELETED, (Object)"Y", Criteria.NOT_EQUAL);
		criteria.add(LISTTYPE, TListBean.LIST_TYPE.CASCADINGCHILD, Criteria.NOT_EQUAL);		
		Criterion publicRepositoryType = criteria.getNewCriterion(REPOSITORYTYPE, new Integer(TListBean.REPOSITORY_TYPE.PUBLIC), Criteria.EQUAL);
		Criterion noRepositoryType = criteria.getNewCriterion(REPOSITORYTYPE, (Object)null, Criteria.ISNULL);
		criteria.add(publicRepositoryType.or(noRepositoryType));		
		criteria.addAscendingOrderByColumn(NAME);
		try {
			activeLists = doSelect(criteria);
		}
		catch (TorqueException e) {
			LOGGER.error("Loading the active lists failed with " + e.getMessage(), e);		
		}
		List listBeanList = convertTorqueListToBeanList(activeLists);
		
		//sets the has entries flag
		List activeListsWithEntries = new ArrayList();
		Set activeSetWithEntries = new HashSet();
		criteria = new Criteria();
		criteria.add(DELETED, (Object)"Y", Criteria.NOT_EQUAL);
		criteria.add(LISTTYPE, TListBean.LIST_TYPE.CASCADINGCHILD, Criteria.NOT_EQUAL);
		publicRepositoryType = criteria.getNewCriterion(REPOSITORYTYPE, new Integer(TListBean.REPOSITORY_TYPE.PUBLIC), Criteria.EQUAL);
		noRepositoryType = criteria.getNewCriterion(REPOSITORYTYPE, (Object)null, Criteria.ISNULL);
		criteria.add(publicRepositoryType.or(noRepositoryType));
		criteria.addJoin(OBJECTID, TOptionPeer.LIST);
		criteria.setDistinct();
		try {
			activeListsWithEntries = doSelect(criteria);
		}
		catch (TorqueException e) {
			LOGGER.error("Loading the active lists failed with " + e.getMessage(), e);		
		}
		Iterator iterator = activeListsWithEntries.iterator();
		while (iterator.hasNext()) {
			TList tList = (TList) iterator.next();
			activeSetWithEntries.add(tList.getObjectID());
		}
		
		iterator = listBeanList.iterator();
		while (iterator.hasNext()) {
			TListBean listBean = (TListBean) iterator.next();			
			listBean.setHasEntries(activeSetWithEntries.contains(listBean.getObjectID()));
		}		
		
		return listBeanList;
	}*/
	
	/**
	 * Load the public lists 
	 * @return
	 */
	public List<TListBean> loadPublicLists() {
		Criteria criteria = new Criteria();
		criteria.add(DELETED, (Object)BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
		criteria.add(LISTTYPE, TListBean.LIST_TYPE.CASCADINGCHILD, Criteria.NOT_EQUAL);
		Criterion publicRepositoryType = criteria.getNewCriterion(REPOSITORYTYPE, new Integer(TListBean.REPOSITORY_TYPE.PUBLIC), Criteria.EQUAL);
		Criterion noRepositoryType = criteria.getNewCriterion(REPOSITORYTYPE, (Object)null, Criteria.ISNULL);
		criteria.add(publicRepositoryType.or(noRepositoryType));
		criteria.addAscendingOrderByColumn(NAME);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the public lists failed with " + e.getMessage(), e);
			return null;
		}		
	}
	
	/**
	 * Loads all active project lists by person
	 * @deprecated
	 * @param personID
	 * @return
	 */
	/*public List loadActiveProjectListsByUser(Integer personID) {
		List listBeanList = new ArrayList();
		List projectsBeans = DAOFactory.getFactory().getProjectDAO().loadActiveInactiveProjectsByProjectAdmin(personID);
		List projectIDs = new ArrayList();
		if (projectsBeans!=null) {
			Iterator iterator = projectsBeans.iterator();
			while (iterator.hasNext()) {
				TProjectBean project = (TProjectBean) iterator.next();
				projectIDs.add(project.getObjectID());
			}
		}
		if (projectIDs.isEmpty()) {
			return listBeanList;
		}
		
		List activeLists = null;
		Criteria criteria;
		criteria = new Criteria();
		criteria.add(DELETED, (Object)"Y", Criteria.NOT_EQUAL);
		criteria.add(LISTTYPE, TListBean.LIST_TYPE.CASCADINGCHILD, Criteria.NOT_EQUAL);
		criteria.addIn(PROJECT, projectIDs);
		criteria.add(REPOSITORYTYPE, TListBean.REPOSITORY_TYPE.PROJECT);
		criteria.addAscendingOrderByColumn(NAME);
		try {
			activeLists = doSelect(criteria);
		}
		catch (TorqueException e) {
			LOGGER.error("Loading the active lists failed with " + e.getMessage(), e);		
		}
		listBeanList = convertTorqueListToBeanList(activeLists);
		
		//sets the has entries flag
		List activeListsWithEntries = new ArrayList();
		Set activeSetWithEntries = new HashSet();
		criteria = new Criteria();
		criteria.add(DELETED, (Object)"Y", Criteria.NOT_EQUAL);
		criteria.add(LISTTYPE, TListBean.LIST_TYPE.CASCADINGCHILD, Criteria.NOT_EQUAL);
		criteria.addIn(PROJECT, projectIDs);
		criteria.add(REPOSITORYTYPE, TListBean.REPOSITORY_TYPE.PROJECT);
		criteria.addJoin(OBJECTID, TOptionPeer.LIST);
		criteria.setDistinct();
		try {
			activeListsWithEntries = doSelect(criteria);
		}
		catch (TorqueException e) {
			LOGGER.error("Loading the active lists failed with " + e.getMessage(), e);		
		}
		Iterator iterator = activeListsWithEntries.iterator();
		while (iterator.hasNext()) {
			TList tList = (TList) iterator.next();
			activeSetWithEntries.add(tList.getObjectID());
		}
		
		iterator = listBeanList.iterator();
		while (iterator.hasNext()) {
			TListBean listBean = (TListBean) iterator.next();			
			listBean.setHasEntries(activeSetWithEntries.contains(listBean.getObjectID()));
		}		
		
		return listBeanList;
	}*/
	
	/**
	 * Load the lists for projects
	 * @param projectIDs
	 * @return
	 */
	/*public List<TListBean> loadProjectLists(List<Integer> projectIDs) {
		if (projectIDs==null || projectIDs.isEmpty()) {
			return new ArrayList();
		}						
		Criteria criteria = new Criteria();
		criteria.add(DELETED, (Object)BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
		criteria.add(LISTTYPE, TListBean.LIST_TYPE.CASCADINGCHILD, Criteria.NOT_EQUAL);
		criteria.addIn(PROJECT, projectIDs);
		criteria.add(REPOSITORYTYPE, TListBean.REPOSITORY_TYPE.PROJECT);
		criteria.addAscendingOrderByColumn(NAME);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		}
		catch (TorqueException e) {
			LOGGER.error("Loading the project lists failed with " + e.getMessage(), e);
			return new ArrayList();
		}
	}*/
	
	/**
	 * Loads a list by name
	 * @param name
	 * @param repositoryType
	 * @param project
	 */
	public List<TListBean> loadByNameInContext(String name, Integer repositoryType, Integer project) {
		Criteria crit = new Criteria();
		crit.add(DELETED, (Object)BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
		crit.add(NAME, name);
		crit.add(LISTTYPE, TListBean.LIST_TYPE.CASCADINGCHILD, Criteria.NOT_EQUAL);
		if (repositoryType==null || repositoryType.intValue() == TListBean.REPOSITORY_TYPE.PUBLIC) {
			crit.add(REPOSITORYTYPE, TListBean.REPOSITORY_TYPE.PUBLIC);
		} else {
			crit.add(REPOSITORYTYPE, TListBean.REPOSITORY_TYPE.PROJECT);
			crit.add(PROJECT, project);
		}
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Verifying the uniqueness of the name " + name + 
					" and repositoryType " + repositoryType + " and project "  + project + " failed with " + e.getMessage(), e);
			return null;
		}		
	}	
	
	/**
	 * Inserts/updates a new/existing list in the TList table
	 * @param listBean
	 * @return the created optionID
	 */
	public Integer save(TListBean listBean) {
		TList tList;		
		try {
			tList = BaseTList.createTList(listBean);
			tList.save();
			return tList.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a list failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Deletes a list by primary key
	 * @param objectID
	 */
	public void delete(Integer objectID) {
		Criteria crit = new Criteria();
		crit.add(OBJECTID, objectID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting a list by objectID " + objectID + " failed with: " + e);
		}
		//ReflectionHelper.delete(deletePeerClasses, deleteFields, objectID);
	}
	
	/**
	 * Sets the deleted flag
	 * @param fieldId
	 */
	public void setDeleted(Integer objectID, boolean deactivate) {
		TListBean listBean = loadByPrimaryKey(objectID);
		if (listBean!=null) {
			if (deactivate) {
				listBean.setDeleted(BooleanFields.TRUE_VALUE);
			} else {
				listBean.setDeleted(BooleanFields.FALSE_VALUE);
			}
		}
		try {
			save(listBean);
		} catch (Exception e) {
			LOGGER.error("Setting the deleted flag for the list " + objectID + " failed with: " + e);
		}
	}
	
	/**
	 * Returns the child lists of a list
	 * Only one level
	 * @param listID
	 * @return
	 */
	public List<TListBean> getChildLists(Integer listID) {
		Criteria criteria = new Criteria();
		criteria.add(PARENTLIST, listID);
		//criteria.add(DELETED, (Object)BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		}
		catch (TorqueException e) {
			LOGGER.error("Getting the child lists for list " + listID + " failed with " + e.getMessage(), e);
			return null;
		}		
	}
	
	
	/**
	 * Gets a listBean with a certain parent and childnumber
	 * @param parentListID
	 * @param childNumber
	 * @return
	 */
	public TListBean getChildList(Integer parentListID, Integer childNumber) {
		List childLists = null;
		Criteria criteria = new Criteria();
		criteria.add(PARENTLIST, parentListID);
		criteria.add(CHILDNUMBER, childNumber);
		try {
			childLists = doSelect(criteria);
		}
		catch (TorqueException e) {
			LOGGER.error("Getting the child list for parent " + parentListID + 
					" and childnumber " + childNumber + " failed with " + e.getMessage(), e);
		}
		if (childLists==null || childLists.isEmpty()) {
			LOGGER.error("No child list found for parent " + parentListID + 
					" and childnumber " + childNumber);
			return null;
		}
		if (childLists.size()>1)
		{
			LOGGER.error("More than one child list found for parent " + parentListID + 
					" and childnumber " + childNumber);
		}
		return ((TList)childLists.get(0)).getBean();
	}

	/**
	 * Gets the lists of specific type: single select or tree select
	 * @param type
	 * @return
	 */
	public List<TListBean> getPublicListsOfType(Integer type) {
		Criteria criteria = new Criteria();
		criteria.add(LISTTYPE, type);
		criteria.add(REPOSITORYTYPE, TListBean.REPOSITORY_TYPE.PUBLIC);
		criteria.add(DELETED, (Object)BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
		criteria.addAscendingOrderByColumn(NAME);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		}
		catch (TorqueException e) {
			LOGGER.error("Getting the of type " + type + " failed with " + e.getMessage(), e);
			return null;
		}		
	}
	
	/**
	 * Gets the project specific lists of specific type: single select or tree select
	 * @param projectIDs
	 * @param type
	 * @return
	 */
	public List<TListBean> getProjectListsOfType(List<Integer> projectIDs, Integer type) {
		Criteria criteria = new Criteria();
		criteria.add(LISTTYPE, type);
		criteria.add(REPOSITORYTYPE, TListBean.REPOSITORY_TYPE.PROJECT);
		criteria.addIn(PROJECT, projectIDs);
		criteria.add(DELETED, (Object)BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
		criteria.addAscendingOrderByColumn(NAME);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		}
		catch (TorqueException e) {
			LOGGER.error("Getting the of type " + type + " failed with " + e.getMessage(), e);
			return null;
		}		
	}
	
	/**
	 * Whether any option from a list is assigned to workItem(s)
	 * @param listID
	 * @return
	 */
	public boolean isListAssignedToWorkitem(Integer listID) {
		List childLists = null;
		Criteria criteria = new Criteria();
		criteria.addJoin(TAttributeValuePeer.CUSTOMOPTIONID, TOptionPeer.OBJECTID);
		criteria.add(TOptionPeer.LIST, listID);
		try {
			childLists = doSelect(criteria);
		}
		catch (TorqueException e) {
			LOGGER.error("Is assigned to workitem for list " + listID + " failed with " + e.getMessage(), e);		
		}
		return (childLists!=null && !childLists.isEmpty());
	}
	
	
	
	/**
	 * Whether a list is assigned to config(s)
	 * @param optionID
	 * @return
	 */
	public boolean isListAssignedToConfig(Integer listID) {
		List childLists = null;
		Criteria criteria = new Criteria();
		criteria.addJoin(TOptionSettingsPeer.LIST, OBJECTID);
		criteria.add(TOptionSettingsPeer.LIST, listID);
		try {
			childLists = doSelect(criteria);
		}
		catch (TorqueException e) {
			LOGGER.error("Is assigned to field config for list " + listID + " failed with " + e.getMessage(), e);		
		}
		return (childLists!=null && !childLists.isEmpty());
	}
	
	/**
	 * Get the lists for a project
	 * @param projectID
	 * @param whether to include only the not deleted list
	 * @return
	 */
	public List<TListBean> getListsByProject(Integer projectID, boolean onlyNotDeleted) {
		Criteria criteria = new Criteria();
		criteria.add(PROJECT, projectID);
		if (onlyNotDeleted) {
			criteria.add(DELETED, (Object)BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
		}
		criteria.add(LISTTYPE, TListBean.LIST_TYPE.CASCADINGCHILD, Criteria.NOT_EQUAL);
		criteria.add(REPOSITORYTYPE, TListBean.REPOSITORY_TYPE.PROJECT);
		criteria.addAscendingOrderByColumn(NAME);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Getting the lists for project " + projectID + " failed with " + e.getMessage(), e);
			return null;
		}		
	}
	
	/**
	 * Converts a list of TField torque objects to a list of TFieldBean objects 
	 * @param torqueList
	 * @return
	 */
	private List<TListBean> convertTorqueListToBeanList(List<TList> torqueList) {
		List<TListBean> beanList = new LinkedList<TListBean>();
		if (torqueList!=null) {
			Iterator<TList> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				TList tList = itrTorqueList.next();
				beanList.add(tList.getBean());
			}
		}
		return beanList;
	}
	
	
	/**A
	 * Load all lists using fieldIDs list
	 * @param screenID
	 * @return
	 */
	public List<TListBean> loadAllForField(List<Integer> fieldIDs){
		Criteria crit = new Criteria();
		crit.addIn(BaseTFieldPeer.OBJECTID, fieldIDs);
		crit.addJoin(BaseTFieldPeer.OBJECTID, BaseTFieldConfigPeer.FIELDKEY);
		crit.addJoin(BaseTFieldConfigPeer.OBJECTID, BaseTOptionSettingsPeer.CONFIG);
		crit.addJoin(BaseTOptionSettingsPeer.LIST, OBJECTID);
		try	{
			return convertTorqueListToBeanList(doSelect(crit));
		}
		catch(TorqueException e){
			LOGGER.error("Loading the custom fields failed with:" + e.getMessage(), e);
			return null;
		}		
	}
		

		
	/**A
	 * Load by parentID
	 * @param parentID
	 * @return 
	 */
		public List<TListBean> loadByParent(Integer parentID){
			Criteria crit = new Criteria();
			if (parentID == null) {
				crit.add(PARENTLIST, (Object)null, Criteria.ISNULL);
			}
			else {
				crit.add(PARENTLIST, parentID);
			}
			try	{
				return convertTorqueListToBeanList(doSelect(crit));
			}
			catch(TorqueException e){
				LOGGER.error("Loading the custom fields failed with:" + e.getMessage(), e);
				return null;
			}
		}
		
		/**
		 * Load a Bean by attribute
		 * @param name
		 * @param ListType
		 * @param repositoryType
		 * @param ChildNumber
		 * @return
		 */
		public TListBean loadByAttribute(String name, Integer listType, Integer repositoryType,
							Integer childNumber){
			List<TListBean> list = null;
			Criteria crit = new Criteria();
			crit.add(NAME, name);
			crit.add(LISTTYPE, listType);
			crit.add(REPOSITORYTYPE, repositoryType);
			crit.add(CHILDNUMBER, childNumber);
			crit.add(PARENTLIST, (Object)"PARENTLIST is NULL", Criteria.CUSTOM);
			try	{
				list = convertTorqueListToBeanList(doSelect(crit));
				if (list.size() > 0) {
					return list.get(0);
				}
				else return null;
			}
			catch(TorqueException e){
				LOGGER.error("Loading the custom fields failed with:" + e.getMessage(), e);
				return null;
			}	
		}
	
}
