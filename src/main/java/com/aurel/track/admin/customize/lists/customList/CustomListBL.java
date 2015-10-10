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


package com.aurel.track.admin.customize.lists.customList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.lists.DetailBaseTO;
import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.customize.lists.ListOrOptionBaseBL;
import com.aurel.track.admin.customize.lists.OptionBaseBL;
import com.aurel.track.admin.customize.lists.customOption.CustomOptionBaseBL;
import com.aurel.track.admin.customize.localize.LocalizeBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TListBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ListDAO;
import com.aurel.track.dao.OptionDAO;
import com.aurel.track.fieldType.design.custom.select.config.ListHierarchy;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.resources.LocalizeUtil;

/**
 * Business logic lass for custom lists
 * @author Tamas Ruff
 *
 */
public class CustomListBL implements ListOrOptionBaseBL {
	
	public static ListDAO listDAO = DAOFactory.getFactory().getListDAO();
	private static OptionDAO optionDAO = DAOFactory.getFactory().getOptionDAO();
	
	
	private static CustomListBL instance;
	
	public static CustomListBL getInstance(){
		if (instance==null){
			 instance=new CustomListBL();
		}
		return instance;
	}
	
	
	
	/**
	 * Gets the type of the entry
	 * @param repository
	 * @return
	 */
	public int getEntityType(Integer repository) {
		if (repository!=null && repository.intValue() == ListBL.REPOSITORY_TYPE.PUBLIC) {
			return OptionBaseBL.ENTRY_TYPE.GLOBAL_LIST;
		} else {
			return OptionBaseBL.ENTRY_TYPE.PROJECT_LIST;
		}
	}
	
	public boolean isModifiable(TListBean listBean,  TPersonBean personBean) {
		if (personBean.isSys()) {
			return true;
		}
		boolean modifiable = false;
		if (listBean!=null) {
			Integer parentListID = listBean.getParentList();
			while (parentListID!=null) {
				listBean = ListBL.loadByPrimaryKey(parentListID);
				parentListID = listBean.getParentList();
			}
			Integer repositoryType = listBean.getRepositoryType();
			Integer projectID = listBean.getProject();
			if (repositoryType!=null && repositoryType.equals(TListBean.REPOSITORY_TYPE.PROJECT)) {
				if (projectID!=null && PersonBL.isProjectAdmin(personBean.getObjectID(), projectID)) {
					modifiable = true;
				}
			}/* else {
				//new (public or project) or edit public
				modifiable = personBean.isSys();
			}*/
		}
		return modifiable;
	}
	
	/**
	 * Gets the bean by optionID and list or creates a new bean
	 * @param optionID
	 * @param listID
	 * @return
	 */
	public ILabelBean getLabelBean(Integer optionID, Integer listID) {
		ILabelBean labelBean = null;
		if (listID!=null) {
			labelBean = ListBL.loadByPrimaryKey(listID);
			//existing bean
			return labelBean;
		}
		if (labelBean==null) {
			//new bean
			labelBean = new TListBean();
		}
		return labelBean;
	}
	
	/**
	 *  Gets the localized bean by optionID and list or creates a new bean
	 * @param optionID
	 * @param listID
	 * @param locale
	 * @return
	 */
	public ILabelBean getLocalizedLabelBean(Integer optionID, Integer listID, Locale locale) {
		return getLabelBean(optionID, listID);
	}
	
	/**
	 * Creates a detail transfer object
	 * @param optionID
	 * @param listID	
	 * @param add
	 * @param copy
	 * @param personBean
	 * @param locale
	 * @return
	 */
	public DetailBaseTO loadDetailTO(Integer optionID, Integer listID,
			boolean add, boolean copy, TPersonBean personBean, Locale locale) {
		TListBean listBean = (TListBean)getLocalizedLabelBean(optionID, listID, locale);
		//boolean modifiable = isModifiable(listBean, personBean);
		CustomListDetailTO customListDetailTO = new CustomListDetailTO();
		customListDetailTO.setListID(listID);
		String label = listBean.getLabel();
		if (copy) {
			label = LocalizeUtil.getParametrizedString("common.lbl.copy", new Object[] {label}, locale);
		}
		customListDetailTO.setLabel(label);		
		//customListDetailTO.setCanEdit(modifiable);
		//customListDetailTO.setDeletable(modifiable);
		//customListDetailTO.setCustomListTypesList(getCustomListTypesList(locale));
		/*if (listBean.getListType()==null) {
			//by add
			customListDetailTO.setCustomListType(Integer.valueOf(TListBean.LIST_TYPE.SIMPLE));
		} else {
			customListDetailTO.setCustomListType(listBean.getListType());
		}*/
		//customListDetailTO.setCascadingTypesList(getCascadingTypesList(locale));
		/*customListDetailTO.setCascadingType(CustomListBL.calculateCascadingType(listBean));
		customListDetailTO.setRepositoryTypesList(getRepositoryTypesList(personBean, locale));
		customListDetailTO.setRepositoryType(listBean.getRepositoryType());
		if (listBean.getRepositoryType()==null) {
			customListDetailTO.setRepositoryType(TListBean.REPOSITORY_TYPE.PUBLIC);
		}
		customListDetailTO.setProjectsList((List<ILabelBean>)(List)
				projectDAO.loadActiveInactiveProjectsByProjectAdmin(personBean.getObjectID()));
		customListDetailTO.setProject(listBean.getProject());*/
		customListDetailTO.setDescription(listBean.getDescription());
		return customListDetailTO;
	}
		
	/**
	 * gets the available custom types list
	 * @param locale
	 * @return
	 */
	/*private List<IntegerStringBean> getCustomListTypesList(Locale locale) {
		List<IntegerStringBean> listTypes = new ArrayList();		
		IntegerStringBean integerStringBean = new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.list.listType.simple", locale), 
				Integer.valueOf(TListBean.LIST_TYPE.SIMPLE));
		listTypes.add(integerStringBean);
		//integerStringBean = new IntegerStringBean(
		//		LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.list.listType.tree", locale),  
		//		Integer.valueOf(TListBean.LIST_TYPE.TREE));
		//listTypes.add(integerStringBean);
		integerStringBean = new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.list.listType.cascading", locale),  
				Integer.valueOf(TListBean.LIST_TYPE.CASCADINGPARENT));
		listTypes.add(integerStringBean);		
		return listTypes;
	}*/
	
	/**
	 * Gets the localized custom types list
	 * @param listType
	 * @param locale
	 * @return
	 */
	public String getCustomListTypesString(Integer listType, Locale locale) {
		if (listType!=null) {
			switch(listType) {
			case TListBean.LIST_TYPE.SIMPLE:
				return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.list.listType.simple", locale);
			case TListBean.LIST_TYPE.CASCADINGPARENT:
				return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.list.listType.cascading", locale);
			case TListBean.LIST_TYPE.TREE:
				return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.list.listType.tree", locale);	
			}
		}
		return "";		
	}
	
	
	/**
	 * Gets the available cascading types list
	 * @param locale
	 * @return
	 */
	/*private List<IntegerStringBean> getCascadingTypesList(Locale locale) {
		List listTypes = new ArrayList();		
		IntegerStringBean integerStringBean = new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.customize.list.listType.cascading.parentChild", locale), 
				Integer.valueOf(TListBean.CASCADING_TYPE.PARENT_CHILD));
		listTypes.add(integerStringBean);
		integerStringBean = new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
					"admin.customize.list.listType.cascading.parentMoreChild", locale), 
				Integer.valueOf(TListBean.CASCADING_TYPE.PARENT_CHILDREN));
		listTypes.add(integerStringBean);
		integerStringBean = new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
					"admin.customize.list.listType.cascading.parentChildGrandchild", locale), 
				Integer.valueOf(TListBean.CASCADING_TYPE.PARENT_CHILD_GRANDCHILD));
		listTypes.add(integerStringBean);		
		return listTypes;		
	}*/
	
	/**
	 * Gets the localized cascadingType
	 * @param cascadingType
	 * @param locale
	 * @return
	 */
	public String getCascadingTypeString(Integer cascadingType, Locale locale) {
		if (cascadingType!=null) {
			switch(cascadingType.intValue()) {
			case TListBean.CASCADING_TYPE.PARENT_CHILD:
				return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.list.listType.cascading.parentChild", locale);
			case TListBean.CASCADING_TYPE.PARENT_CHILDREN:
				return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.list.listType.cascading.parentMoreChild", locale);
			case TListBean.CASCADING_TYPE.PARENT_CHILD_GRANDCHILD:
				return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.list.listType.cascading.parentChildGrandchild", locale);	
			}
		}
		return "";
	}
	
	/**
	 * Prepares the custom list for save after edit
	 * @param listID
	 * @param newName
	 * @param description
	 * @param repository
	 * @param projectID
	 * @param personBean
	 * @return
	 */
	public ILabelBean prepareLabelBean(Integer listID, String newName, String description, 
			Integer repository, Integer projectID, TPersonBean personBean) {
		TListBean listBean;
		boolean create = true; 
		if (listID==null) {
			listBean = new TListBean();
		} else {
			listBean = ListBL.loadByPrimaryKey(listID);
			if (listBean==null) {
				listBean = new TListBean();
			} else {
				create = false;
			}
		}
		//set values by both create and edit
		listBean.setName(newName);
		listBean.setDescription(description);		
		if (create) {
			//new list added
			//some values can be set only for new lists: listType, cascadingType, owner
			//by creation it is s simple list
			listBean.setListType(Integer.valueOf(TListBean.LIST_TYPE.SIMPLE));
			listBean.setOwner(personBean.getObjectID());
			//the repository might be set also for an existing list
			if (repository==null) {
				//new list added from tree
				if (personBean.isSys()) {
					//system admin
					listBean.setRepositoryType(Integer.valueOf(TListBean.REPOSITORY_TYPE.PUBLIC));
				} else {
					listBean.setRepositoryType(Integer.valueOf(TListBean.REPOSITORY_TYPE.PROJECT));
					listBean.setProject(projectID);
				}
			} else {
				//new list added from grid
				listBean.setRepositoryType(repository);
				listBean.setProject(projectID);
			}
		} else {
			//existing list modified: 
			if (repository!=null) {
				//modified from grid (not from tree)
				listBean.setRepositoryType(repository);
				if (!Integer.valueOf(TListBean.REPOSITORY_TYPE.PROJECT).equals(repository)) {
					//set the project to null when the repository is set to something else than project
					//(the project is not submitted, consequently it sghould be set manually to null
					listBean.setProject(null);
				} else {
					if (projectID!=null) {
						listBean.setProject(projectID);
					}
				}
			}
		}
		return listBean;
	}
	
	/**
	 * Whether the new label is valid (not empty not duplicate)
	 * @param optionID
	 * @param label
	 * @param repositoryType
	 * @param project
	 * @return
	 */
	public String isValidLabel(Integer optionID, String label, Integer repositoryType, Integer project/*, boolean copy*/) {
		if (label==null || "".equals(label)) {
			return "common.err.required";
		}
		List<TListBean> listBeans = listDAO.loadByNameInContext(label, repositoryType, project);
		TListBean listBean = null;
		if (listBeans!=null && !listBeans.isEmpty()) {
			if (listBeans.size()>1) {
				return "common.err.unique";
			} else {
				listBean = listBeans.get(0);
				if (optionID==null || (!optionID.equals(listBean.getObjectID()) /*|| 
						copy*/)) {
					return "common.err.unique";
				}
			
			}
		}
		return null;
	}
	
	/**
	 * Saves  the custom list 
	 * If sortOrder is not set it will be set to be the next available sortOrder
	 * @param labelBean
	 * @param copy
	 * @param personID
	 * @param locale 
	 * @return
	 */
	public Integer save(ILabelBean labelBean, boolean copy, Integer personID, Locale locale) {
		TListBean listBean = (TListBean)labelBean;
		//boolean create = labelBean.getObjectID()==null; 
		//Integer cascadingType = listBean.getCascadingType();
		Integer parentListID = null;
		if (copy) {
			return copyList(listBean, personID, new HashMap<Integer, Integer>());
		} else {
			parentListID = ListBL.save(listBean);
		}
		return parentListID; 
	}
	
	
	/**
	 * Whether the list entry has dependent data
	 * @param objectID
	 * @return
	 */
	public boolean hasDependentData(Integer objectID) {
		//was any workItem or history assigned to a member of this list
		if (listDAO.isListAssignedToWorkitem(objectID) || 
				optionDAO.isListAssignedToHistoryEntry(objectID, true) ||
				optionDAO.isListAssignedToHistoryEntry(objectID, false) || 
				listDAO.isListAssignedToConfig(objectID)) {
			return true;
		}		
		List<TListBean> childListIDs = ListBL.getChildLists(objectID);
		if (childListIDs!=null) {
			Iterator<TListBean> itrChildListIDs = childListIDs.iterator();
			//workItem assigned to a member of a child list
			//probably not the case because a child option will be assigned 
			//only if a parent option is assigned 
			while (itrChildListIDs.hasNext()) {
				TListBean listBean = itrChildListIDs.next();
				if (hasDependentData(listBean.getObjectID())) {
					return true;
				}
			}
		}
		//config assigned to this list
		return false;
	}
	
	
	/**
	 * Deletes an option from the list when 
	 * no workItem refers has this option (or one of their suboptions)
	 * (All descendant lists of this option will also be deleted!)
	 * Otherwise the deleted flag will be set using the setDeleted() method 
	 * @param listID
	 * @param listID
	 * @return	- true if delete is successfull
	 * 			- false if delete is unsuccessful: workItem refers to this option or one of their suboption
	 * 			In this case the deleted flag will be set
	 */	
	public void delete(Integer listID) {
		if (hasDependentData(listID)) {
			//set the list as deleted	
			listDAO.setDeleted(listID, true);
		} else {
			//delete all entries of the list
			deleteAllEntriesAndList(listID);
		}
	}
	

	/**
	 * Deletes all entries and the list (also the children lists and children entries)
	 * @param listID
	 */
	private static void deleteAllEntriesAndList(Integer listID) {
		List<TListBean> childListIDs = ListBL.getChildLists(listID);
		if (childListIDs!=null) {
			Iterator<TListBean> itrChildListIDs = childListIDs.iterator();
			while (itrChildListIDs.hasNext()) {
				TListBean listBean = itrChildListIDs.next();
				deleteAllEntriesAndList(listBean.getObjectID());
			}
		}
		//delete the options of the parent list
		optionDAO.deleteByList(listID);
		listDAO.delete(listID);	
		LocalizeBL.deleteLocalizedResourcesForFieldName(
				LocalizationKeyPrefixes.FIELD_CUSTOMSELECT_KEY_PREFIX+listID);
	}

	public static void deleteAllEntries(Integer listID) {
		List<TListBean> childListIDs = ListBL.getChildLists(listID);
		if (childListIDs!=null) {
			Iterator<TListBean> itrChildListIDs = childListIDs.iterator();
			while (itrChildListIDs.hasNext()) {
				TListBean listBean = itrChildListIDs.next();
				deleteAllEntriesAndList(listBean.getObjectID());
			}
		}
		optionDAO.deleteByList(listID);
	}
	
	
	/**
	 * Makes a copy of a list (hierarchiclaly if it is the case) 
	 * and a copy of all entries (also hierarchiclaly if it is the case)
	 * @param listBean
	 * @param personID
	 * @param copiedOptionsMap
	 */
	public static Integer copyList(TListBean listBean, Integer personID, Map<Integer, Integer> copiedOptionsMap) {
		//copy the list
		ListHierarchy listHierarchy = copyListHierarchy(listBean, null, personID);
		CustomOptionBaseBL customOptionBaseBL =  CustomOptionBaseBL.getInstance(listBean.getListType());
		//copy the entries
		customOptionBaseBL.copyOptions(listHierarchy, copiedOptionsMap);
		return listHierarchy.getDestListID();
	}
	
	/**
	 * Makes a copy of a list (and their child lists if the case) in the TList table 
	 * and loads a listHierarchy with the hierarchical lists
	 * @param srcListBean
	 * @param parentID
	 * @param personID
	 * @return
	 */
	private static ListHierarchy copyListHierarchy(TListBean srcListBean, Integer parentID, Integer personID) {
		ListHierarchy listHierarchy = new ListHierarchy();
		listHierarchy.setSrcListID(srcListBean.getObjectID());
		//set as new
		TListBean destListBean = new TListBean();
		destListBean.setName(srcListBean.getName());
		destListBean.setDescription(srcListBean.getDescription());
		destListBean.setParentList(parentID);
		destListBean.setListType(srcListBean.getListType());
		destListBean.setRepositoryType(srcListBean.getRepositoryType());
		destListBean.setProject(srcListBean.getProject());
		destListBean.setOwner(personID);
		destListBean.setChildNumber(srcListBean.getChildNumber());
		
		//inserts the new list as a copy of the source list
		Integer destListID = ListBL.save(destListBean);
		listHierarchy.setDestListID(destListID);
		
		//gets the child lists of the source copy (when it is the case)
		List<TListBean> childListIDs = ListBL.getChildLists(srcListBean.getObjectID());
		if (childListIDs!=null && !childListIDs.isEmpty()) {
			List<ListHierarchy> childLists = new LinkedList<ListHierarchy>();
			listHierarchy.setDependentLists(childLists);
			for (TListBean srcChildListBean : childListIDs) {
				//makes the copies of the child lists recursively and actualizes the listHierarchy
				childLists.add(copyListHierarchy(srcChildListBean, destListID, personID));
			}
		}
		return listHierarchy;
	}
	
}
