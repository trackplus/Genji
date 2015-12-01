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

package com.aurel.track.admin.customize.lists;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.lists.customList.CustomListBL;
import com.aurel.track.admin.customize.lists.customList.CustomListDetailTO;
import com.aurel.track.admin.customize.lists.customOption.CustomOptionBaseBL;
import com.aurel.track.admin.customize.lists.customOption.CustomOptionCascadingSelectBL;
import com.aurel.track.admin.customize.lists.customOption.CustomOptionDetailTO;
import com.aurel.track.admin.customize.lists.customOption.CustomOptionSimpleSelectBL;
import com.aurel.track.admin.customize.lists.customOption.OptionBL;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeConfigBL;
import com.aurel.track.admin.customize.lists.systemOption.SystemOptionBaseBL;
import com.aurel.track.admin.customize.lists.systemOption.SystemOptionDetailTO;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TListBean;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ListDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.custom.select.CascadingHierarchy;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.EqualUtils;

/**
 * BL for system and custom lists
 * @author Tamas
 *
 */
public class ListBL {
	
	static int SYSTEM_LIST = 1;
	static int CUSTOM_LIST = 2;
	private static String LINK_CHAR = "_";
	static ListDAO listDAO = DAOFactory.getFactory().getListDAO();
	
	public static interface ICONS_CLS {
		static String GLOBAL_CONFIG_ICON = "config-global-ticon";
		static String PROJECT_CONFIG_ICON = "config-project-ticon";
        static String PROJECT_ICON = "project-ticon";
		static String STATUS_ICON = "status-ticon";
		static String PRIORITY_ICON = "priority-ticon";
		static String SEVERITY_ICON = "severity-ticon";
		static String ISSUETYPE_ICON = "issueType-ticon";
		static String CUSTOM_LIST_ICON = "customFields-ticon";
	}
	
	/**
	 * The repository type
	 *
	 */
	public static interface REPOSITORY_TYPE {
		public static final int PUBLIC = 2;
		public static final int PROJECT = 3;
	}
	
	public static interface RESOURCE_TYPES {
		//negative  because there can be custom list with the same listID
		public static int ISSUETYPE = -SystemFields.ISSUETYPE;//-2
		public static int STATUS = -SystemFields.STATE;//-4
		public static int PRIORITY = -SystemFields.PRIORITY;//-10
		public static int SEVERITY = -SystemFields.SEVERITY;//-11
		//public static int PROJECT_STATUS = -60;
		//public static int RELEASE_STATUS = -61;
		//public static int ACCOUNT_STATUS = -62;
		public static int GLOBAL_LIST = -20;
		public static int PROJECT_LIST = -21;
		public static int CUSTOM_ENTRY = -22;
	}
	
	public static interface RESOURCE_KEYS {
		public static String CUSTOM_ENTRY = "admin.customize.list.lbl.customEntry";
		public static String GLOBAL_LIST = "admin.customize.list.lbl.globalList";
		public static String GLOBAL_LISTS = "admin.customize.list.lbl.globalLists";
		public static String PROJECT_LIST = "admin.customize.list.lbl.projectList";
		public static String PROJECT_LISTS = "admin.customize.list.lbl.projectLists";
	}
	
	private static boolean isSys(TPersonBean personBean) {
		return personBean.isSys();
	}
	
	

	/**
	 * Loads a custom list by primary key
	 * @param listID
	 * @return
	 */
	public static TListBean loadByPrimaryKey(Integer listID) {
		return listDAO.loadByPrimaryKey(listID);
	}
	
	/**
	 * Loads all lists
	 * @return
	 */
	public static List<TListBean> loadAll() {
		return listDAO.loadAll();
	}

	
	/**
	 * Gets the project spcific lists
	 * @param projectID
	 * @param whether to include only the not deleted list
	 * @return
	 */
	public static List<TListBean> getListsByProject(Integer projectID, boolean onlyNotDeleted) {
		return listDAO.getListsByProject(projectID, onlyNotDeleted);
	}
	
	/**
	 * Loads the public lists
	 * @return
	 */
	public static List<TListBean> loadPublicLists() {
		return listDAO.loadPublicLists();
	}
	
	/**
	 * Gets the child lists of a list
	 * Only one level
	 * @return
	 */
	public static List<TListBean> getChildLists(Integer listID) {
		return listDAO.getChildLists(listID);
	}
	
	/**
	 * Gets a listBean with a certain parent and childnumber
	 * @param parentListID
	 * @param childNumber
	 * @return
	 */
	public static TListBean getChildList(Integer parentListID, Integer childNumber) {
		return listDAO.getChildList(parentListID, childNumber);
	}
	
	/**
	 * Load by parentID
	 * @param parentID
	 * @return 
	 */
	public static List<TListBean> loadByParent(Integer parentID) {
		return listDAO.loadByParent(parentID);
	}
	
	/**
	 * Saves the list bean
	 * @param listBean
	 * @return
	 */
	public static Integer save(TListBean listBean) {
		return listDAO.save(listBean);
	}
	
	private static List<ListOptionTreeNodeTO> getFirstLevelNodes(TPersonBean personBean, Locale locale) {
		boolean isSys = isSys(personBean);
		List<ListOptionTreeNodeTO> listOptionTreeNodeTOs = new LinkedList<ListOptionTreeNodeTO>();
		ListOptionTreeNodeTO publicLists = createTreeNode(new ListOptionIDTokens(Integer.valueOf(REPOSITORY_TYPE.PUBLIC), null, null, null, false),
				LocalizeUtil.getLocalizedTextFromApplicationResources(RESOURCE_KEYS.GLOBAL_LISTS, locale), null,
				false, isSys, false, isSys, null, 
				isSys, RESOURCE_TYPES.GLOBAL_LIST, false, false, false, false, false);
		publicLists.setIconCls(ICONS_CLS.GLOBAL_CONFIG_ICON);
		publicLists.setMightCopyChild(isSys);
		listOptionTreeNodeTOs.add(publicLists);
		ListOptionTreeNodeTO projectLists = createTreeNode(new ListOptionIDTokens(Integer.valueOf(REPOSITORY_TYPE.PROJECT), null, null, null, false),
				LocalizeUtil.getLocalizedTextFromApplicationResources(RESOURCE_KEYS.PROJECT_LISTS, locale), null,
				false, false, false, false, null, 
				false, null, false, false, false, false, false);
		projectLists.setIconCls(ICONS_CLS.PROJECT_CONFIG_ICON);
		listOptionTreeNodeTOs.add(projectLists);
		return listOptionTreeNodeTOs;
	}
	
	/**
	 * Get the children node lists of a node having the given id
	 * @param node
	 * @param fromProjectConfig
	 * @param personBean
	 * @param locale
	 * @return
	 */
	public static List<ListOptionTreeNodeTO> getChildren(String node, boolean fromProjectConfig,
			 TPersonBean personBean,  Locale locale, ApplicationBean applicationBean){
		ListOptionIDTokens listOptionIDTokens = decodeNode(node);
		Integer repository = listOptionIDTokens.getRepository();
		Integer type = listOptionIDTokens.getType();
		if (repository==null) {
			//root node
			return getFirstLevelNodes(personBean, locale);
		} else {
			if (type==null) {
				switch (repository.intValue()) {
				case REPOSITORY_TYPE.PROJECT:
					Integer projectID = listOptionIDTokens.getProjectID();
					if (projectID==null) {
						return getProjectTreeNodes(ProjectBL.getActiveInactiveFirstLevelProjectsByProjectAdmin(personBean, true));
					} else {
						List<ListOptionTreeNodeTO> treeNodes = new LinkedList<ListOptionTreeNodeTO>();
						if (!fromProjectConfig) {
							//get the subprojects
							treeNodes.addAll(getProjectTreeNodes(ProjectBL.loadActiveInactiveSubrojects(projectID)));
						}
						treeNodes.addAll(getProjectCustomListTreeNodes(projectID));
						return treeNodes;
					}
				case REPOSITORY_TYPE.PUBLIC:
					List<ListOptionTreeNodeTO> children = getSystemListTreeNodes(personBean, locale, applicationBean);
					children.addAll(getPublicCustomListTreeNodes(personBean));
					return children;
				default:
					return null;
				}
			} else {
				Integer childListID = listOptionIDTokens.getChildListID();
				if (SYSTEM_LIST==type.intValue()) {
					boolean childIssueType = false;
					if (childListID ==RESOURCE_TYPES.ISSUETYPE) {
						childIssueType = true;
					}
					return getSystemListOptionTreeNodes(childListID, personBean, locale, childIssueType, applicationBean);
				} else {
					return getCustomListOptionTreeNodes(listOptionIDTokens, personBean, locale);		
				}
			}
		}
	}
	
	/**
	 * Get the grid rows children of node having the given id
	 * @param node
	 * @param personBean
	 * @param locale
	 * @return
	 */
	public static String getLoadList(String node, TPersonBean personBean, 
			Locale locale, ApplicationBean applicationBean) {
		ListOptionIDTokens listOptionIDTokens = decodeNode(node);
		Integer repository = listOptionIDTokens.getRepository();
		if (repository==null) {
			//wrong or no node: should never happen 
			return  null;
		}
		Integer type = listOptionIDTokens.getType();
		Integer listID = listOptionIDTokens.getChildListID();
		Integer optionID = listOptionIDTokens.getOptionID();
		if (type==null) {
			switch (repository.intValue()) {
			case REPOSITORY_TYPE.PROJECT:
				Integer projectID = listOptionIDTokens.getProjectID();
				if (projectID==null) {
					//project rows
					return ListOptionsJSON.createListGridRowsJSON(getProjectGridRows(ProjectBL.getActiveInactiveFirstLevelProjectsByProjectAdmin(personBean, true), locale));
				} else {
					//subproject and list rows
					//get the subprojects
					List<ListGridRowTO> gridRows = getProjectGridRows(ProjectBL.loadActiveInactiveSubrojects(projectID), locale);
					//get the project specific lists
					gridRows.addAll(getProjectCustomListGridRows(projectID, locale));
					return ListOptionsJSON.createListGridRowsJSON(gridRows);
				}
			case REPOSITORY_TYPE.PUBLIC:
				List<ListGridRowTO> rows = getSystemListGridRows(locale);
				rows.addAll(getPublicCustomListGridRows(personBean, locale));
				return ListOptionsJSON.createListGridRowsJSON(rows);
			default:
				return null;
			}
		} else {		
			if (SYSTEM_LIST==type.intValue()) {
				boolean childIssueType = false;
				if (listID ==RESOURCE_TYPES.ISSUETYPE) {
					childIssueType = true;
				}
				return ListOptionsJSON.createListOptionGridRowsJSON(
							getSystemListOptionGridRows(listID, optionID, personBean, childIssueType, locale, applicationBean));
			} else {
				/*if (listID==null) {
					return ListOptionsJSON.createCustomListGridRowsJSON(getCustomListGridRows(personBean, locale));
				} else {*/
					return  ListOptionsJSON.createListOptionGridRowsJSON(
							getCustomListOptionGridRows(listOptionIDTokens, personBean, locale));
			}
		}
	}
	
	/**
	 * Gets the option detail for a list entry  
	 * @param node
	 * @param add
     * @param copy
     * @param personBean
	 * @param locale
	 * @return
	 */
	public static String load(String node, boolean add, boolean copy,
			TPersonBean personBean, Locale locale) {
		ListOptionIDTokens listOptionIDTokens = decodeNode(node);
		Integer repository = listOptionIDTokens.getRepository();
		Integer listID = listOptionIDTokens.getChildListID();
		Integer optionID = listOptionIDTokens.getOptionID();
		boolean leaf = listOptionIDTokens.isLeaf();
		ListOrOptionBaseBL listOrOptionBaseBL = getListOrOptionBaseInstance(listID, optionID, leaf, add, copy);
		DetailBaseTO detailBaseTO = listOrOptionBaseBL.loadDetailTO(
				optionID, listID, add, copy, personBean, locale);
		String label = detailBaseTO.getLabel();
		switch (listOrOptionBaseBL.getEntityType(repository)) {
		case OptionBaseBL.ENTRY_TYPE.SYSTEM_OPTION:
			SystemOptionDetailTO systemOptionDetailTO = (SystemOptionDetailTO)detailBaseTO;
				return ListOptionsJSON.createSystemOptionDetailJSON(label, 
					systemOptionDetailTO.getTypeflag(), systemOptionDetailTO.getTypeflagsList(), 
					CssStyleBean.decodeCssStyle(systemOptionDetailTO.getCssStyle()),
					systemOptionDetailTO.getPercentComplete());
		case OptionBaseBL.ENTRY_TYPE.CUSTOM_OPTION:
			CustomOptionDetailTO customOptionDetailTO = (CustomOptionDetailTO)detailBaseTO;
			return ListOptionsJSON.createCustomOptionDetailJSON(label,
					CssStyleBean.decodeCssStyle(customOptionDetailTO.getCssStyle()),
					customOptionDetailTO.isDefaultOption());
		case OptionBaseBL.ENTRY_TYPE.GLOBAL_LIST:
		case OptionBaseBL.ENTRY_TYPE.PROJECT_LIST:
			CustomListDetailTO customListDetailTO = (CustomListDetailTO)detailBaseTO;
			return  ListOptionsJSON.createCustomListDetailJSON(label, customListDetailTO.getDescription());
		}
		return null;
	}
	
	/**
	 * Whether the label is valid
	 * @param node
	 * @param label
	 * @param add
	 * @param copy
	 * @param locale
	 * @return
	 */
	static String isValidLabel(String node, String label,
			boolean add, boolean copy, Locale locale) {
		ListOptionIDTokens listOptionIDTokens = decodeNode(node);
		Integer repository = listOptionIDTokens.getRepository();
		Integer listID = listOptionIDTokens.getChildListID();
		Integer optionID = listOptionIDTokens.getOptionID();
		boolean leaf = listOptionIDTokens.isLeaf();
		ListOrOptionBaseBL listOrOptionBaseBL = getListOrOptionBaseInstance(listID, optionID, leaf, add, copy);
		String errorKey = null;
		Integer resourceType = null;
		int type = listOrOptionBaseBL.getEntityType(repository);
		switch (type) {
		case OptionBaseBL.ENTRY_TYPE.SYSTEM_OPTION:
			SystemOptionBaseBL systemOptionBaseBL = (SystemOptionBaseBL)listOrOptionBaseBL;
			errorKey = systemOptionBaseBL.isValidLabel(listID, optionID, label);
			if (errorKey!=null) {
				resourceType = listID;
			}
			break;
		case OptionBaseBL.ENTRY_TYPE.CUSTOM_OPTION:
			CustomOptionBaseBL customOptionBaseBL = (CustomOptionBaseBL)listOrOptionBaseBL;
			errorKey = customOptionBaseBL.isValidLabel(listID, optionID, label, add, leaf);
			if (errorKey!=null) {
				resourceType = RESOURCE_TYPES.CUSTOM_ENTRY;
			}
			break;
		case OptionBaseBL.ENTRY_TYPE.GLOBAL_LIST:
		case OptionBaseBL.ENTRY_TYPE.PROJECT_LIST:
			CustomListBL customListBL = (CustomListBL)listOrOptionBaseBL;
			errorKey = customListBL.isValidLabel(listID, label, repository, listOptionIDTokens.getProjectID()/*, copy*/);
			if (errorKey!=null) {
				if (type==OptionBaseBL.ENTRY_TYPE.GLOBAL_LIST) {
					resourceType = RESOURCE_TYPES.GLOBAL_LIST;
				} else {
					resourceType = RESOURCE_TYPES.PROJECT_LIST;
				}
			}
			break;
		}	
		if (errorKey!=null) {
			return LocalizeUtil.getParametrizedString(errorKey, 
					new Object[] {getLocalizedLabel(resourceType, locale)} , locale);
		}
		return null;
	}
	
	/**
	 * Saves a new or existing custom list or system or custom list entry
	 * @param node
	 * @param label
     * @param cssStyleBean
	 * @param typeFlag
     * @param percentComplete
     * @param defaultOption
	 * @param add
     * @param copy
	 * @param description
	 * @param personBean
	 * @param locale
	 */
	static String save(String node, String label, CssStyleBean cssStyleBean, 
			Integer typeFlag, Integer percentComplete, boolean defaultOption,
			boolean add, boolean copy, String description, TPersonBean personBean, Locale locale) {
		ListOptionIDTokens listOptionIDTokens = decodeNode(node);
		Integer repository = listOptionIDTokens.getRepository();
		Integer listID = listOptionIDTokens.getChildListID();
		Integer optionID = listOptionIDTokens.getOptionID();
		boolean leaf = listOptionIDTokens.isLeaf();
		ListOrOptionBaseBL listOrOptionBaseBL = getListOrOptionBaseInstance(listID, optionID, leaf, add, copy);
		ILabelBean labelBean = null;
		String encodedCssStyle = null;
		if (cssStyleBean!=null) {
			encodedCssStyle = cssStyleBean.encodeCssStyle();
		}
		//boolean isSystemOption = false;
		switch (listOrOptionBaseBL.getEntityType(repository)) {
		case OptionBaseBL.ENTRY_TYPE.SYSTEM_OPTION:
			SystemOptionBaseBL systemOptionBaseBL = (SystemOptionBaseBL)listOrOptionBaseBL;
			labelBean = systemOptionBaseBL.prepareLabelBean(optionID, listID, label, encodedCssStyle, typeFlag, percentComplete, leaf);
			//isSystemOption = true;
			break;
		case OptionBaseBL.ENTRY_TYPE.CUSTOM_OPTION:
			CustomOptionBaseBL customOptionBaseBL = (CustomOptionBaseBL)listOrOptionBaseBL;
			labelBean = customOptionBaseBL.prepareLabelBean(optionID, listID, label, encodedCssStyle, defaultOption, add, leaf);
			if (leaf && add) {
				//leaf option added from a leaf option -> extend the cascading hierarchy by creating a child list
				List<TListBean> childLists = getChildLists(listID);
				if (childLists==null || childLists.isEmpty()) {
					TListBean currentList = (TListBean)CustomListBL.getInstance().getLocalizedLabelBean(optionID, listID, locale);
					if (currentList.getObjectID()!=null) {
						//modify the "simple list" type to "cascading parent" type
						Integer listType = currentList.getListType();
						if (Integer.valueOf(TListBean.LIST_TYPE.SIMPLE).equals(listType)) {
							currentList.setListType(Integer.valueOf(TListBean.LIST_TYPE.CASCADINGPARENT));
							CustomListBL.getInstance().save(currentList, false, personBean.getObjectID(), locale);
						}
					}
					//add a cascading child
					TListBean listBean = new TListBean();
					listBean.setName(LocalizeUtil.getParametrizedString("admin.customize.list.bl.opt.lbl.childListPrefix", new Object[] {1}, locale));			
					listBean.setParentList(listID);
					listBean.setChildNumber(Integer.valueOf(1));
					listBean.setListType(Integer.valueOf(TListBean.LIST_TYPE.CASCADINGCHILD));
					listID = CustomListBL.getInstance().save(listBean, false, personBean.getObjectID(), locale);
					TOptionBean optionBean = (TOptionBean)labelBean;
					optionBean.setList(listID);
				}
			}
			break;
		case OptionBaseBL.ENTRY_TYPE.GLOBAL_LIST:
		case OptionBaseBL.ENTRY_TYPE.PROJECT_LIST:	
			CustomListBL customListBL = (CustomListBL)listOrOptionBaseBL;
			labelBean = customListBL.prepareLabelBean(listID, label, description, 
					repository, listOptionIDTokens.getProjectID(), personBean);
			break;
		}
		if (labelBean!=null) {
			Integer objectID = listOrOptionBaseBL.save(labelBean, copy, personBean.getObjectID(), locale);
			//force reloading in lookup container
			
			ListOptionIDTokens listOptionIDTokensReturn = new ListOptionIDTokens();
			listOptionIDTokensReturn.setRepository(repository);
			listOptionIDTokensReturn.setProjectID(listOptionIDTokens.getProjectID());
			switch (listOrOptionBaseBL.getEntityType(repository)) {
			case OptionBaseBL.ENTRY_TYPE.SYSTEM_OPTION:
				listOptionIDTokensReturn.setType(OptionBaseBL.ENTRY_TYPE.SYSTEM_OPTION);
				listOptionIDTokensReturn.setChildListID(listID);
				listOptionIDTokensReturn.setOptionID(objectID);
				listOptionIDTokensReturn.setLeaf(true);
				break;
			case OptionBaseBL.ENTRY_TYPE.CUSTOM_OPTION:
				if (add && leaf) {
					//parent hierarchy extended: the node to select after reload is the parent of the new node 
					//(the used to be leaf), not the new node
					listOptionIDTokensReturn = listOptionIDTokens;
					//the parent is not a leaf any more, so after refreshing the grandparent of the new node
					//the nodeID of the parent node changes: leaf -> false
					listOptionIDTokensReturn.setLeaf(false);
				} else {
					listOptionIDTokensReturn.setOptionID(objectID);
					TOptionBean optionBean = (TOptionBean)labelBean;
					List<TListBean> childLists = getChildLists(optionBean.getList());
					listOptionIDTokensReturn.setLeaf(childLists==null || childLists.isEmpty());
				}
				listOptionIDTokensReturn.setType(OptionBaseBL.ENTRY_TYPE.CUSTOM_OPTION);
				listOptionIDTokensReturn.setChildListID(listID);
				break;
			case OptionBaseBL.ENTRY_TYPE.GLOBAL_LIST:
			case OptionBaseBL.ENTRY_TYPE.PROJECT_LIST:
				listOptionIDTokensReturn.setType(OptionBaseBL.ENTRY_TYPE.CUSTOM_OPTION);
				listOptionIDTokensReturn.setChildListID(objectID);
				listOptionIDTokensReturn.setOptionID(null);
				listOptionIDTokensReturn.setLeaf(false);
				break;
			}
			return ListOptionsJSON.createSaveResultJSON(true, encodeNode(listOptionIDTokensReturn), objectID);
		}
		return "";
	}
	
	/**
	 * Copies a filter category with the entire subtree
	 * @param nodeFrom
	 * @param nodeTo
	 * @param locale
	 * @return
	 * 
	 */
	static String copy(String nodeFrom, String nodeTo, Integer personID, Locale locale){
		ListOptionIDTokens listTokenFrom = decodeNode(nodeFrom);
		ListOptionIDTokens listTokenTo = decodeNode(nodeTo);
		Integer repositoryFrom = listTokenFrom.getRepository();
		Integer repositoryTo = listTokenTo.getRepository();
		Integer projectFrom = listTokenFrom.getProjectID();
		Integer projectTo = listTokenTo.getProjectID();
		Integer optionFrom = listTokenFrom.getOptionID();
		Integer optionTo = listTokenTo.getOptionID();
		Integer childListFrom = listTokenFrom.getChildListID();
		Integer childListTo = listTokenTo.getChildListID();
		boolean listFrom = listTokenFrom.isLeaf();
		if (optionFrom==null) {
			//move a list
			if (EqualUtils.equal(repositoryFrom, repositoryTo) && EqualUtils.equal(projectFrom, projectTo)) {
				//optionTo==null means list, but it was
				return JSONUtility.encodeJSONSuccessAndNode(nodeFrom);
			}
			Integer listID = listTokenFrom.getChildListID();
			if (listID!=null) {
				TListBean listBean = loadByPrimaryKey(listID);
				if (listBean!=null) {
					String errorMessage = isValidLabel(nodeTo, listBean.getLabel(), false, true, locale);
					if (errorMessage!=null) {
						return  ListOptionsJSON.createNodeResultJSON(false, nodeTo, errorMessage);
					}
					listBean.setRepositoryType(repositoryTo);
					listBean.setProject(projectTo);
					save(listBean);
					listTokenFrom.setRepository(repositoryTo);
					listTokenFrom.setProjectID(projectTo);
				}
			}
		} else {
			//move an option. Possible only within a composite list
			TOptionBean optionBeanFrom = OptionBL.loadByPrimaryKey(optionFrom);
			TOptionBean optionBeanTo = null;
			if (optionBeanFrom!=null && optionTo!=null) {
				optionBeanTo = OptionBL.loadByPrimaryKey(optionTo);
				if (optionBeanTo!=null) {
					optionBeanFrom.setParentOption(optionTo);
					optionBeanFrom.setList(childListTo);
					CustomOptionCascadingSelectBL.getInstance().save(optionBeanFrom, false, null, locale);
					ListOptionIDTokens listOptionIDTokensReturn = removeChildListIfLastOption(childListFrom, optionFrom, optionBeanFrom.getParentOption(), listFrom, personID, locale);
					if (listOptionIDTokensReturn!=null) {
						listTokenFrom.setLeaf(true);
					}
					listTokenFrom.setChildListID(childListTo);
				}
			}
		}
		return JSONUtility.encodeJSONSuccessAndNode(encodeNode(listTokenFrom));
	}
	
	/**
	 * Delete a list or a list option
	 * @param node
	 * @param personBean
	 * @param locale
	 * @return
	 */
	static String delete(String node, TPersonBean personBean, Locale locale) {
		ListOptionIDTokens listOptionIDTokens = decodeNode(node);
		Integer repository = listOptionIDTokens.getRepository();
		Integer listID = listOptionIDTokens.getChildListID();
		Integer optionID = listOptionIDTokens.getOptionID();
		boolean leaf = listOptionIDTokens.isLeaf();
		ListOrOptionBaseBL listOrOptionBaseBL = getListOrOptionBaseInstance(listID, optionID, leaf, false, false);
		boolean cascadingChildDeleted = false;
		switch (listOrOptionBaseBL.getEntityType(repository)) {
		case OptionBaseBL.ENTRY_TYPE.SYSTEM_OPTION:
			SystemOptionBaseBL systemOptionBaseBL = (SystemOptionBaseBL)listOrOptionBaseBL;
			if (systemOptionBaseBL.hasDependentData(optionID)) {
				return JSONUtility.encodeJSONFailure(JSONUtility.DELETE_ERROR_CODES.NEED_REPLACE);
			} else {
				systemOptionBaseBL.delete(optionID);
				//LookupContainer.resetLocalizedLookupMaps(Integer.valueOf(-listID.intValue()));
			}
			break;
		case OptionBaseBL.ENTRY_TYPE.CUSTOM_OPTION:
			TOptionBean optionBean = (TOptionBean)listOrOptionBaseBL.getLocalizedLabelBean(optionID, listID, locale);
			if (optionBean!=null) {
				listOrOptionBaseBL.delete(optionID);
				ListOptionIDTokens listOptionIDTokensReturn = removeChildListIfLastOption(listID, optionID, optionBean.getParentOption(), leaf, personBean.getObjectID(), locale);
				if (listOptionIDTokensReturn!=null) {
					cascadingChildDeleted = true;
					node = encodeNode(listOptionIDTokensReturn);
				}
				/*if (leaf) {
					//leaf option deleted
					List<TOptionBean> options = CustomOptionSimpleSelectBL.getInstance().getOptions(listID, null);
					if (options==null || options.isEmpty()) {
						//it was the last leaf from the lsit
						TListBean currentList = (TListBean)CustomListBL.getInstance().getLocalizedLabelBean(null, listID, locale);
						if (currentList!=null) {
							Integer parentList = currentList.getParentList();
							if (parentList!=null) {
								TListBean parentListBean = (TListBean)CustomListBL.getInstance().getLocalizedLabelBean(null, parentList, locale);
								if (parentListBean.getObjectID()!=null) {
									if (Integer.valueOf(TListBean.LIST_TYPE.CASCADINGPARENT).equals(parentListBean.getListType())) {
										//if the parent is the topmost parent then set it to simple list
										parentListBean.setListType(Integer.valueOf(TListBean.LIST_TYPE.SIMPLE));
										CustomListBL.getInstance().save(parentListBean, false, personBean.getObjectID(), locale);
									}
									if (Integer.valueOf(TListBean.LIST_TYPE.CASCADINGCHILD).equals(currentList.getListType())) {
										CustomListBL.getInstance().delete(listID);
										cascadingChildDeleted = true;
										ListOptionIDTokens listOptionIDTokensReturn = new ListOptionIDTokens();
										listOptionIDTokensReturn.setType(OptionBaseBL.ENTRY_TYPE.CUSTOM_OPTION);
										listOptionIDTokensReturn.setChildListID(parentList);
										listOptionIDTokensReturn.setOptionID(optionBean.getParentOption());
										listOptionIDTokensReturn.setLeaf(true);
										node = encodeNode(listOptionIDTokensReturn);
									}
								}
							}
						}
					}
				}*/
			}
			break;
		case OptionBaseBL.ENTRY_TYPE.GLOBAL_LIST:
		case OptionBaseBL.ENTRY_TYPE.PROJECT_LIST:
			listOrOptionBaseBL.delete(listID);
			break;
		}
		return ListOptionsJSON.createDeleteResultJSON(true, node, null, cascadingChildDeleted);
	}
	
	/**
	 * Removes a custom option node and returns the parent node's token if the child list is also deleted
	 * (if the option was the last one from the child list) 
	 * @param listID
	 * @param optionID
	 * @param leaf
	 * @param locale
	 * @return
	 */
	private static ListOptionIDTokens removeChildListIfLastOption(Integer listID, Integer optionID, Integer parentOptionID, boolean leaf, Integer personID, Locale locale) {

			if (leaf) {
				//leaf option deleted
				List<TOptionBean> options = CustomOptionSimpleSelectBL.getInstance().getOptions(listID, null);
				if (options==null || options.isEmpty()) {
					//it was the last leaf from the list
					TListBean currentList = (TListBean)CustomListBL.getInstance().getLocalizedLabelBean(null, listID, locale);
					if (currentList!=null) {
						Integer parentList = currentList.getParentList();
						if (parentList!=null) {
							TListBean parentListBean = (TListBean)CustomListBL.getInstance().getLocalizedLabelBean(null, parentList, locale);
							if (parentListBean.getObjectID()!=null) {
								if (Integer.valueOf(TListBean.LIST_TYPE.CASCADINGPARENT).equals(parentListBean.getListType())) {
									//if the parent is the topmost parent then set it to simple list
									parentListBean.setListType(Integer.valueOf(TListBean.LIST_TYPE.SIMPLE));
									CustomListBL.getInstance().save(parentListBean, false, personID, locale);
								}
								if (Integer.valueOf(TListBean.LIST_TYPE.CASCADINGCHILD).equals(currentList.getListType())) {
									CustomListBL.getInstance().delete(listID);
									ListOptionIDTokens listOptionIDTokensReturn = new ListOptionIDTokens();
									listOptionIDTokensReturn.setType(OptionBaseBL.ENTRY_TYPE.CUSTOM_OPTION);
									listOptionIDTokensReturn.setChildListID(parentList);
									listOptionIDTokensReturn.setOptionID(parentOptionID);
									listOptionIDTokensReturn.setLeaf(true);
									return listOptionIDTokensReturn;
								}
							}
						}
					}
				}
			}
		return null;
	}
	
	/**
	 * Render the replacement option 
	 * @param node
	 * @param errorMessage
	 * @param locale
	 * @return
	 */
	static String renderReplace(String node, String errorMessage, Locale locale) {
		ListOptionIDTokens listOptionIDTokens = decodeNode(node);
		Integer listID = listOptionIDTokens.getChildListID();
		Integer optionID = listOptionIDTokens.getOptionID();
		SystemOptionBaseBL systemOptionBaseBL = SystemOptionBaseBL.getInstance(listID);
		ILabelBean labelBean = systemOptionBaseBL.getLocalizedLabelBean(optionID, listID, locale);
		String label = null;
		if (labelBean!=null) {
			label = labelBean.getLabel();
		}
		List<ILabelBean> replacementsList = getReplacementList(listID, optionID, locale);
		String localizedLabel=getLocalizedLabel(listID, locale);
		return JSONUtility.createReplacementListJSON(true, label, localizedLabel, localizedLabel, replacementsList, errorMessage, locale);	
	}
	
	/**
	 * Replace and delete
	 * @param replacementID
	 * @param node
	 * @param locale
	 * @return
	 */
	static String replaceAndDelete(Integer replacementID, String node, Locale locale) {
		if (replacementID==null) {
			ListOptionIDTokens listOptionIDTokens = decodeNode(node);
			Integer listID = listOptionIDTokens.getChildListID();
			String errorMessage =  LocalizeUtil.getParametrizedString("common.err.replacementRequired", 
					new Object[] {getLocalizedLabel(listID, locale)}, locale);
			return renderReplace(node, errorMessage, locale);
		} else {
			replaceAndDeleteListEntry(node, replacementID);
			return ListOptionsJSON.createNodeResultJSON(true, node, null);
		}
	}
	
	/**
	 * Replace and delete a list entry
	 * @param node
	 * @param replacementID
	 */
	private static void replaceAndDeleteListEntry(String node, Integer replacementID) {
		ListOptionIDTokens listOptionIDTokens = decodeNode(node);
		Integer repository = listOptionIDTokens.getRepository();
		Integer listID = listOptionIDTokens.getChildListID();
		Integer optionID = listOptionIDTokens.getOptionID();
		boolean leaf = listOptionIDTokens.isLeaf();
		ListOrOptionBaseBL listOrOptionBaseBL = getListOrOptionBaseInstance(listID, optionID, leaf, false, false);
		switch (listOrOptionBaseBL.getEntityType(repository)) {
		case OptionBaseBL.ENTRY_TYPE.SYSTEM_OPTION:
			SystemOptionBaseBL systemOptionBaseBL = (SystemOptionBaseBL)listOrOptionBaseBL;
			systemOptionBaseBL.replaceAndDelete(optionID, replacementID);
			//LookupContainer.resetLocalizedLookupMaps(Integer.valueOf(-listID.intValue()));
			break;
		case OptionBaseBL.ENTRY_TYPE.CUSTOM_OPTION:
			listOrOptionBaseBL.delete(optionID);
			//LookupContainer.resetLocalizedLookupMaps(LookupContainer.CUSTOM_OPTIONS_KEY);
			break;
		case OptionBaseBL.ENTRY_TYPE.GLOBAL_LIST:
		case OptionBaseBL.ENTRY_TYPE.PROJECT_LIST:
			listOrOptionBaseBL.delete(listID);
			//LookupContainer.resetLocalizedLookupMaps(LookupContainer.CUSTOM_OPTIONS_KEY);
			break;
		}
	}
	
	private static List<ILabelBean> getReplacementList(Integer listID, Integer optionID, Locale locale) {
		List<ILabelBean> replacementOptions = null;
		if (listID!=null) {
			SystemOptionBaseBL systemOptionsBaseBL = SystemOptionBaseBL.getInstance(listID);
			replacementOptions = systemOptionsBaseBL.getOptions(listID, locale, null);
			for (Iterator<ILabelBean> iterator = replacementOptions.iterator(); iterator.hasNext();) {
				ILabelBean labelBean = iterator.next();
				if (labelBean.getObjectID().equals(optionID)) {
					iterator.remove();
					break;
				}
			}
		}
		return replacementOptions;
	}
		
	
	/**
	 * Encode a node
	 * @param listOptionIDTokens
	 * @return
	 */
	public static String encodeNode(ListOptionIDTokens listOptionIDTokens){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(listOptionIDTokens.getRepository());
		Integer projectID = listOptionIDTokens.getProjectID();
		Integer type = listOptionIDTokens.getType();
		if (projectID!=null || type!=null) {
			//if not first level node
			stringBuffer.append(LINK_CHAR);	
		}
		if (projectID!=null) {
			stringBuffer.append(projectID);
		}
		if (type!=null) {
			stringBuffer.append(LINK_CHAR);
			stringBuffer.append(type);
			Integer childListID = listOptionIDTokens.getChildListID();
			Integer optionID = listOptionIDTokens.getOptionID();
			if (childListID==null && optionID==null) {
				//root node
				return stringBuffer.toString();
			}
			boolean leaf = listOptionIDTokens.isLeaf();
			stringBuffer.append(LINK_CHAR);
			if (childListID!=null) {
				stringBuffer.append(childListID);
			}
			stringBuffer.append(LINK_CHAR);
			if (optionID!=null) {
				stringBuffer.append(optionID);
			}
			stringBuffer.append(LINK_CHAR);
			stringBuffer.append(leaf);
		}
		return stringBuffer.toString();
	}
	
	/**
	 * Decode a node
	 * @param id
	 * @return
	 */
	public static ListOptionIDTokens decodeNode(String id){
		ListOptionIDTokens listOptionIDTokens = new ListOptionIDTokens();
		if (id!=null && !"".equals(id)) {
			String[] tokens = id.split(LINK_CHAR);
			if (tokens!=null && tokens.length>0) {	
				listOptionIDTokens.setRepository(Integer.valueOf(tokens[0]));
				if (tokens.length>1) {
					if (tokens[1]!=null && !"".equals(tokens[1])) {
						listOptionIDTokens.setProjectID(Integer.valueOf(tokens[1]));
					}
					if (tokens.length>2) {
						if (tokens[2]!=null && !"".equals(tokens[2])) {
							listOptionIDTokens.setType(Integer.valueOf(tokens[2]));
							if (tokens.length>3) {
								if (tokens[3]!=null && !"".equals(tokens[3])) {
									listOptionIDTokens.setChildListID(Integer.valueOf(tokens[3]));
								}
								if (tokens.length>4) {
									if (tokens[4]!=null && !"".equals(tokens[4])) {
										listOptionIDTokens.setOptionID(Integer.valueOf(tokens[4]));
									}
									if (tokens.length>5) {
										listOptionIDTokens.setLeaf(Boolean.valueOf(tokens[5]));
									}
								}
							}
						}
					}
				}
			}
		}
		return listOptionIDTokens;
	}
	
	/**
	 * Get the entries for a system list
	 * Can't add new entry (only form the parent node), but if i'm system admin then I can edit/delete an entry
	 * @param listID
	 * @param personBean
	 * @param locale
	 * @return
	 */
	private static List<ListOptionTreeNodeTO> getSystemListOptionTreeNodes(Integer listID, 
			TPersonBean personBean, Locale locale, boolean childIssueType, ApplicationBean applicationBean) {
		List<ListOptionTreeNodeTO> listOptionTreeNodeTOs = new LinkedList<ListOptionTreeNodeTO>();
		SystemOptionBaseBL systemOptionsBaseBL = SystemOptionBaseBL.getInstance(listID);
		List<ILabelBean> labelBeans = systemOptionsBaseBL.getOptions(listID, locale, applicationBean);
		boolean isSys = isSys(personBean);
		if (labelBeans!=null) {
			 for (ILabelBean labelBean : labelBeans) {
				ListOptionIDTokens listOptionIDTokens = new ListOptionIDTokens(Integer.valueOf(REPOSITORY_TYPE.PUBLIC), null, SYSTEM_LIST, listID, true);				
				listOptionIDTokens.setOptionID(labelBean.getObjectID());
				ListOptionTreeNodeTO listOptionTreeNodeTO = createTreeNode(listOptionIDTokens, labelBean.getLabel(), null,
						isSys, false, isSys && systemOptionsBaseBL.isDeletable(labelBean), false, listID,
						false, null, true, true, true, true, false);
				listOptionTreeNodeTO.setIcon("listOptionIcon!serveIconStream.action?node="+encodeNode(listOptionIDTokens)+"&time="+new Date().getTime());
				listOptionTreeNodeTO.setHasPercentComplete(systemOptionsBaseBL.hasPercentComplete());
				listOptionTreeNodeTO.setHasTypeflag(systemOptionsBaseBL.hasTypeFlag());
				listOptionTreeNodeTO.setDisableTypeflag(systemOptionsBaseBL.isDisableTypeflag());
				listOptionTreeNodeTO.setChildrenHaveIssueChildFilter(childIssueType);
				listOptionTreeNodeTOs.add(listOptionTreeNodeTO);
			}
		}
		return listOptionTreeNodeTOs;
	}
	
	/**
	 * Get the entries for a system list
	 * @param listID
	 * @param personBean
	 * @param locale
	 * @param applicationBean
	 * @return
	 */
	static List<ListOptionGridRowTO> getSystemListOptionGridRows(Integer listID, Integer optionID,
			TPersonBean personBean, boolean hasChildFilter, Locale locale, ApplicationBean applicationBean) {
		List<ListOptionGridRowTO> listOptionGridRowTOs = new LinkedList<ListOptionGridRowTO>();
		SystemOptionBaseBL systemOptionsBaseBL = SystemOptionBaseBL.getInstance(listID);
		List<ILabelBean> labelBeans = null;
		if (optionID==null) {
			labelBeans = systemOptionsBaseBL.getOptions(listID, locale, applicationBean);
		} else {
			labelBeans = new LinkedList<ILabelBean>();
			ILabelBean labelBean = systemOptionsBaseBL.getLocalizedLabelBean(optionID, listID, locale);
			if (labelBean!=null) {
				labelBeans.add(labelBean);
			}
		}
		boolean isSys = isSys(personBean);
		if (labelBeans!=null) {
			for (ILabelBean labelBean : labelBeans) {
				//it is leaf and each have typeflag and symbol, but only the 
				//status, priority, severity and issue type option has background color
				//do we need symbol for projectType releaseType and accountType?
				listOptionGridRowTOs.add(createListOptionGridRow(REPOSITORY_TYPE.PUBLIC, null,
						SYSTEM_LIST, labelBean, listID, true, isSys,
						isSys && systemOptionsBaseBL.isDeletable(labelBean), hasChildFilter, listID, locale));
			}
		}
		return listOptionGridRowTOs;
	}
	
	/**
	 * Get the entries for a custom list
	 * @param listOptionIDTokens
	 * @param personBean
	 * @param locale
	 * @return
	 */
	private static List<ListOptionTreeNodeTO> getCustomListOptionTreeNodes(
			ListOptionIDTokens listOptionIDTokens, TPersonBean personBean, Locale locale) {
		List<ListOptionTreeNodeTO> listOptionTreeNodeTOs = new LinkedList<ListOptionTreeNodeTO>();
		Integer childListID = listOptionIDTokens.getChildListID();
		Integer parentOptionID = listOptionIDTokens.getOptionID();
		TListBean listBean = loadByPrimaryKey(childListID);
		if (listBean!=null) {
			Integer topParentListID = listBean.getObjectID();
			Integer parentListID = listBean.getParentList();
			while (parentListID!=null) {
				topParentListID = parentListID;
				TListBean parentListBean = loadByPrimaryKey(parentListID);
				if (parentListBean==null) {
					parentListID = null;
				} else {
					parentListID = parentListBean.getParentList();
				}	
			}
			//if the cascading child listBean was not deleted automatically with the last option deleted
			CustomOptionBaseBL customOptionBaseBL = CustomOptionBaseBL.getInstance(listBean.getListType());
			List<TOptionBean> optionBeanList = customOptionBaseBL.getLocalizedOptions(childListID, parentOptionID, locale);
			boolean canEdit = CustomListBL.getInstance().isModifiable(listBean, personBean);
			List<TListBean> childLists = getChildLists(listBean.getObjectID());
			if (optionBeanList!=null) {
				for (TOptionBean optionBean : optionBeanList) {
					boolean leaf = true;
					if (childLists!=null && !childLists.isEmpty()) {
						//parent node
						leaf = false;
						for (int i = 0; i < childLists.size(); i++) {
							listOptionIDTokens.setOptionID(optionBean.getObjectID());
							TListBean childListBean = childLists.get(i);
							listOptionIDTokens.setChildListID(childListBean.getObjectID());
							String childNo = "";
							if (childLists.size()>1) {
								childNo = LocalizeUtil.getParametrizedString("admin.customize.list.lbl.childNo", new Object[] {(i+1)}, locale);							
							}
							boolean childIsLeaf =  Integer.valueOf(TListBean.LIST_TYPE.CASCADINGCHILD).equals(childListBean.getListType()) &&
								//the child has no further children
								getChildLists(childListBean.getObjectID()).isEmpty();
							Integer addChildOrCopyLabelList = Integer.valueOf(RESOURCE_TYPES.CUSTOM_ENTRY);
							/*if (childIsLeaf) {
								addChildOrCopyLabelList = Integer.valueOf(RESOURCE_TYPES.CUSTOM_CHILD);
							}*/
							ListOptionTreeNodeTO listOptionTreeNodeTO = createTreeNode(listOptionIDTokens, optionBean.getLabel() + 
										childNo, topParentListID, canEdit, canEdit, canEdit, canEdit, Integer.valueOf(RESOURCE_TYPES.CUSTOM_ENTRY), 
										canEdit, addChildOrCopyLabelList, leaf, 
										false, childIsLeaf, leaf, childIsLeaf);
							listOptionTreeNodeTO.setChildrenHaveDefaultOption(true);
							listOptionTreeNodeTO.setHasDefaultOption(true);
							listOptionTreeNodeTOs.add(listOptionTreeNodeTO);
							}
					} else {
						//leaf node
						Integer editOrDeleteChildLabelList = Integer.valueOf(RESOURCE_TYPES.CUSTOM_ENTRY);
						listOptionIDTokens.setOptionID(optionBean.getObjectID());
						//listOptionIDTokens.setChildListID(null);
						listOptionIDTokens.setLeaf(leaf);
						ListOptionTreeNodeTO listOptionTreeNodeTO = createTreeNode(listOptionIDTokens, optionBean.getLabel(), topParentListID,
								canEdit, canEdit, canEdit, canEdit, editOrDeleteChildLabelList, canEdit, editOrDeleteChildLabelList,
								leaf, true, true, true, true); 
						listOptionTreeNodeTO.setIcon("listOptionIcon!serveIconStream.action?node="+encodeNode(listOptionIDTokens)+"&time="+new Date().getTime());
						listOptionTreeNodeTO.setChildrenHaveDefaultOption(true);
						listOptionTreeNodeTO.setHasDefaultOption(true);
						listOptionTreeNodeTOs.add(listOptionTreeNodeTO);
					}
				}
			}
		}
		return listOptionTreeNodeTOs;
	}
	
	/**
	 * Get the entries for a custom list
	 * @param listOptionIDTokens
	 * @param personBean
	 * @param locale
	 * @return
	 */
	static List<ListOptionGridRowTO> getCustomListOptionGridRows(
			ListOptionIDTokens listOptionIDTokens, TPersonBean personBean, Locale locale) {
		List<ListOptionGridRowTO> listOptionGridRowTOs = new LinkedList<ListOptionGridRowTO>();
		Integer repostory = listOptionIDTokens.getRepository();
		Integer projectID = listOptionIDTokens.getProjectID();
		Integer childListID = listOptionIDTokens.getChildListID();
		boolean leaf = listOptionIDTokens.isLeaf();
		Integer parentOptionID = listOptionIDTokens.getOptionID();
		TListBean listBean = loadByPrimaryKey(childListID);
		if (listBean!=null) {
			//if the cascading child listBean was not deleted automatically with the last option deleted
			List<TListBean> childLists = getChildLists(childListID);
			List<TOptionBean> optionBeanList = null;
			CustomOptionBaseBL customOptionBaseBL = CustomOptionBaseBL.getInstance(listBean.getListType());
			if (!leaf) {
				//folder
				optionBeanList = customOptionBaseBL.getLocalizedOptions(childListID, parentOptionID, locale);
			} else {
				//grid with one element representing the node
				TOptionBean optionBean = (TOptionBean)customOptionBaseBL.getLocalizedLabelBean(parentOptionID, childListID, locale);
				if (optionBean!=null) {
					optionBeanList = new LinkedList<TOptionBean>();
					optionBeanList.add(optionBean);
				}
			}
			boolean modifiable = CustomListBL.getInstance().isModifiable(listBean, personBean);
			if (optionBeanList!=null) {
				for (TOptionBean optionBean : optionBeanList) {
					if (childLists!=null && !childLists.isEmpty()) {
						//parent node
						for (int i = 0; i < childLists.size(); i++) {
							//not a leaf, no typeflag, no symbol and no background color
							//do we need symbol for parent options?
							listOptionGridRowTOs.add(createListOptionGridRow(repostory, projectID,
									CUSTOM_LIST, optionBean, childListID, 
									false, modifiable, modifiable, false, RESOURCE_TYPES.CUSTOM_ENTRY, locale));
						}
					} else {
						//leaf, no typeflag has symbol and background color
						Integer rowListForLabel = Integer.valueOf(RESOURCE_TYPES.CUSTOM_ENTRY);
						/*if (TListBean.LIST_TYPE.SIMPLE != listBean.getListType().intValue()) {
							rowListForLabel = Integer.valueOf(RESOURCE_TYPES.CUSTOM_CHILD);
						}*/
						listOptionGridRowTOs.add(createListOptionGridRow(repostory, projectID, CUSTOM_LIST, optionBean,
								childListID, true, modifiable, modifiable, false, rowListForLabel,locale));
					}
				}
			}
		}
		return listOptionGridRowTOs;
	}
	
	/**
	 * Gets the hardcoded system lists nodes
	 * Can add new entries to lists, but can't edit or delete the system list
	 * @param locale
	 * @return
	 */
	private static List<ListOptionTreeNodeTO> getSystemListTreeNodes(
			TPersonBean personBean, Locale locale, ApplicationBean applicationBean) {
		boolean isSys = isSys(personBean);
		SystemOptionBaseBL systemOptionsBaseBL;
		List<ListOptionTreeNodeTO> listOptionTreeNodeTOs = new LinkedList<ListOptionTreeNodeTO>();
		Map<Integer, String> localizedLabels = getLocalizedLabels(locale);
		ListOptionTreeNodeTO statusListNode = createTreeNode(new ListOptionIDTokens(Integer.valueOf(REPOSITORY_TYPE.PUBLIC), null, SYSTEM_LIST, RESOURCE_TYPES.STATUS, false),
				localizedLabels.get(RESOURCE_TYPES.STATUS), null, false, isSys, false, isSys, null, 
				isSys, RESOURCE_TYPES.STATUS, false, false, true, false, true);
		systemOptionsBaseBL = SystemOptionBaseBL.getInstance(RESOURCE_TYPES.STATUS);
		statusListNode.setIconCls(ICONS_CLS.STATUS_ICON);
		statusListNode.setChildrenHavePercentComplete(systemOptionsBaseBL.hasPercentComplete());
		statusListNode.setChildrenHaveTypeflag(systemOptionsBaseBL.hasTypeFlag());
		listOptionTreeNodeTOs.add(statusListNode);
		
		ListOptionTreeNodeTO priorityNode = createTreeNode(new ListOptionIDTokens(Integer.valueOf(REPOSITORY_TYPE.PUBLIC), null, SYSTEM_LIST, RESOURCE_TYPES.PRIORITY, false),
				localizedLabels.get(RESOURCE_TYPES.PRIORITY), null, false, isSys, false, isSys, null,
				isSys, RESOURCE_TYPES.PRIORITY, false, false, true, false, true);
		systemOptionsBaseBL = SystemOptionBaseBL.getInstance(RESOURCE_TYPES.PRIORITY);
		priorityNode.setIconCls(ICONS_CLS.PRIORITY_ICON);
		priorityNode.setChildrenHaveTypeflag(systemOptionsBaseBL.hasTypeFlag());
		listOptionTreeNodeTOs.add(priorityNode);
		
		ListOptionTreeNodeTO severityNode = createTreeNode(new ListOptionIDTokens(Integer.valueOf(REPOSITORY_TYPE.PUBLIC), null, SYSTEM_LIST, RESOURCE_TYPES.SEVERITY, false),
				localizedLabels.get(RESOURCE_TYPES.SEVERITY), null, false, isSys, false, isSys, null,
				isSys, RESOURCE_TYPES.SEVERITY, false, false, true, false, true);
		systemOptionsBaseBL = SystemOptionBaseBL.getInstance(RESOURCE_TYPES.SEVERITY);
		severityNode.setIconCls(ICONS_CLS.SEVERITY_ICON);
		severityNode.setChildrenHaveTypeflag(systemOptionsBaseBL.hasTypeFlag());
		listOptionTreeNodeTOs.add(severityNode);
		
		ListOptionTreeNodeTO issueTypeListNode = createTreeNode(new ListOptionIDTokens(Integer.valueOf(REPOSITORY_TYPE.PUBLIC), null, SYSTEM_LIST, RESOURCE_TYPES.ISSUETYPE, false),
				localizedLabels.get(RESOURCE_TYPES.ISSUETYPE), null, false, isSys, false, isSys, null, 
				isSys && IssueTypeConfigBL.getInstance().addIssueTypeAllowed(applicationBean, locale), RESOURCE_TYPES.ISSUETYPE,
				false, false, true, false, true);
		systemOptionsBaseBL = SystemOptionBaseBL.getInstance(RESOURCE_TYPES.ISSUETYPE);
		issueTypeListNode.setIconCls(ICONS_CLS.ISSUETYPE_ICON);
		issueTypeListNode.setChildrenHaveTypeflag(systemOptionsBaseBL.hasTypeFlag());
		issueTypeListNode.setChildrenHaveIssueChildFilter(true);
		//issueTypeListNode.setDisableTypeflag(true);
		listOptionTreeNodeTOs.add(issueTypeListNode);
		return listOptionTreeNodeTOs;
	}
	
	/**
	 * Gets the hardcoded system lists nodes
	 * @param locale
	 * @return
	 */
	static List<ListGridRowTO> getSystemListGridRows(Locale locale) {
		List<ListGridRowTO> listOptionGridRowTOs = new LinkedList<ListGridRowTO>();
		Map<Integer, String> localizedLabels = getLocalizedLabels(locale);	
		//not leaf, no typeflag, have hardcoded icons, no background color
		listOptionGridRowTOs.add(createListGridRow(REPOSITORY_TYPE.PUBLIC, null, SYSTEM_LIST, null,
				localizedLabels.get(RESOURCE_TYPES.STATUS), SystemFields.INTEGER_STATE, 
				false, false, false, false, null, null, null, ICONS_CLS.STATUS_ICON, locale));	
		listOptionGridRowTOs.add(createListGridRow(REPOSITORY_TYPE.PUBLIC, null, SYSTEM_LIST, null,
				localizedLabels.get(RESOURCE_TYPES.PRIORITY), SystemFields.INTEGER_PRIORITY, 
				false, false, false, false, null, null, null, ICONS_CLS.PRIORITY_ICON, locale));
		listOptionGridRowTOs.add(createListGridRow(REPOSITORY_TYPE.PUBLIC, null, SYSTEM_LIST, null,
				localizedLabels.get(RESOURCE_TYPES.SEVERITY), SystemFields.INTEGER_SEVERITY, 
				false, false, false, false, null, null, null, ICONS_CLS.SEVERITY_ICON, locale));
		listOptionGridRowTOs.add(createListGridRow(REPOSITORY_TYPE.PUBLIC, null, SYSTEM_LIST, null,
				localizedLabels.get(RESOURCE_TYPES.ISSUETYPE), SystemFields.INTEGER_ISSUETYPE, 
				false, false, false, false, null, null, null, ICONS_CLS.ISSUETYPE_ICON, locale));
		return listOptionGridRowTOs;	
	}
	
	static Map<Integer, String> getLocalizedLabels(Locale locale) {
		Map<Integer, String> localizedLists = new HashMap<Integer, String>();
		List<Integer> fieldIDs = new LinkedList<Integer>();
		//project is added for localizing the column header for project specific custom lists 
		fieldIDs.add(SystemFields.INTEGER_PROJECT);
		fieldIDs.add(SystemFields.INTEGER_STATE);
		fieldIDs.add(SystemFields.INTEGER_ISSUETYPE);
		fieldIDs.add(SystemFields.INTEGER_PRIORITY);
		fieldIDs.add(SystemFields.INTEGER_SEVERITY);
		Map<Integer, String> localizedFieldconfigLabels = FieldRuntimeBL.getLocalizedDefaultFieldLabels(fieldIDs, locale);
		localizedLists.put(SystemFields.INTEGER_PROJECT, localizedFieldconfigLabels.get(SystemFields.INTEGER_PROJECT));
		localizedLists.put(RESOURCE_TYPES.STATUS, localizedFieldconfigLabels.get(SystemFields.INTEGER_STATE));
		localizedLists.put(RESOURCE_TYPES.ISSUETYPE, localizedFieldconfigLabels.get(SystemFields.INTEGER_ISSUETYPE));
		localizedLists.put(RESOURCE_TYPES.PRIORITY, localizedFieldconfigLabels.get(SystemFields.INTEGER_PRIORITY));
		localizedLists.put(RESOURCE_TYPES.SEVERITY, localizedFieldconfigLabels.get(SystemFields.INTEGER_SEVERITY));
		localizedLists.put(RESOURCE_TYPES.GLOBAL_LIST,
				LocalizeUtil.getLocalizedTextFromApplicationResources(RESOURCE_KEYS.GLOBAL_LIST, locale));
		localizedLists.put(RESOURCE_TYPES.PROJECT_LIST,
				LocalizeUtil.getLocalizedTextFromApplicationResources(RESOURCE_KEYS.PROJECT_LIST, locale));
		localizedLists.put(RESOURCE_TYPES.CUSTOM_ENTRY,
				LocalizeUtil.getLocalizedTextFromApplicationResources(RESOURCE_KEYS.CUSTOM_ENTRY, locale));
		return localizedLists;
	}
	
	static String getLocalizedLabel(Integer listID, Locale locale) {
		if (listID!=null) {
			switch (listID.intValue()) {
			case RESOURCE_TYPES.STATUS:
				return FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_STATE, locale);
			case RESOURCE_TYPES.ISSUETYPE:
				return FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_ISSUETYPE, locale);
			case RESOURCE_TYPES.PRIORITY:
				return FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_PRIORITY, locale);
			case RESOURCE_TYPES.SEVERITY:
				return FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_SEVERITY, locale);
			case RESOURCE_TYPES.PROJECT_LIST:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(RESOURCE_KEYS.PROJECT_LIST, locale);
			case RESOURCE_TYPES.GLOBAL_LIST:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(RESOURCE_KEYS.GLOBAL_LIST, locale);
			case RESOURCE_TYPES.CUSTOM_ENTRY:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(RESOURCE_KEYS.CUSTOM_ENTRY, locale);
			}
		}
		return "";
	}
		
	/**
	 * Gets the public custom list nodes 
	 * @param personBean
	 * @return
	 */
	private static List<ListOptionTreeNodeTO> getPublicCustomListTreeNodes(TPersonBean personBean) {
		List<ListOptionTreeNodeTO> listOptionTreeNodeTOs = new LinkedList<ListOptionTreeNodeTO>();
		//public lists
		List<TListBean> publicLists = loadPublicLists();
		if (publicLists!=null) {
			boolean isSys = isSys(personBean);
			for (TListBean listBean : publicLists) {
				boolean childIsLeaf = Integer.valueOf(TListBean.LIST_TYPE.SIMPLE).equals(listBean.getListType());
				ListOptionTreeNodeTO listOptionTreeNodeTO = createTreeNode(new ListOptionIDTokens(
						REPOSITORY_TYPE.PUBLIC, null, CUSTOM_LIST, listBean.getObjectID(), false), 
						listBean.getLabel(), listBean.getObjectID(), isSys, isSys, isSys, isSys, RESOURCE_TYPES.GLOBAL_LIST, 
						isSys, RESOURCE_TYPES.CUSTOM_ENTRY, false,
						false, childIsLeaf, false, childIsLeaf);
				listOptionTreeNodeTO.setIconCls(ICONS_CLS.CUSTOM_LIST_ICON);
				listOptionTreeNodeTO.setChildrenHaveDefaultOption(true);
				listOptionTreeNodeTO.setCanCopy(true);
				listOptionTreeNodeTOs.add(listOptionTreeNodeTO);
			}
		}
		return listOptionTreeNodeTOs;
	}
	
	/**
	 * Gets the project nodes 
	 * @param projectBeans
	 * @return
	 */
	private static List<ListOptionTreeNodeTO> getProjectTreeNodes(List<TProjectBean> projectBeans) {
		List<ListOptionTreeNodeTO> listOptionTreeNodeTOs = new LinkedList<ListOptionTreeNodeTO>();
		//project specific lists for project admins
		//List<TProjectBean> adminProjects = ProjectBL.loadNotClosedFirstLevelProjectsByProjectAdmin(personID);
		if (projectBeans!=null && !projectBeans.isEmpty()) {
			for (TProjectBean projectBean : projectBeans) {
				Integer projectID = projectBean.getObjectID();
				String text = projectBean.getLabel();
				ListOptionTreeNodeTO listOptionTreeNodeTO = createTreeNode(
						new ListOptionIDTokens(REPOSITORY_TYPE.PROJECT, projectID, null, null, false),
						text, null, false, true, false, true, null, 
						true, RESOURCE_TYPES.PROJECT_LIST, false,
						false, false, false, false);
				listOptionTreeNodeTO.setIconCls(ICONS_CLS.PROJECT_ICON);
				listOptionTreeNodeTO.setMightCopyChild(true);
				/*ListOptionTreeNodeTO listOptionTreeNodeTO = createProjectTreeNode(
						projectBean.getObjectID(), projectBean.getLabel(), ICONS_CLS.PROJECT_CONFIG_ICON, true);*/
				listOptionTreeNodeTOs.add(listOptionTreeNodeTO);
			}
		}
		return listOptionTreeNodeTOs;
	}
	
	/**
	 * Get the nodeID of the (not visible) root node for project specific list configurations
	 * @param projectID
	 * @return
	 */
	public static String getProjectBranchNodeID(Integer projectID) {
		ListOptionIDTokens listOptionIDTokens =  new ListOptionIDTokens(REPOSITORY_TYPE.PROJECT, projectID, null, null, false);
		return encodeNode(listOptionIDTokens);
	}
	
	/**
	 * Gets the custom list nodes for a project 
	 * @param projectID
	 * @return
	 */
	private static List<ListOptionTreeNodeTO> getProjectCustomListTreeNodes(Integer projectID) {
		List<ListOptionTreeNodeTO> listOptionTreeNodeTOs = new LinkedList<ListOptionTreeNodeTO>();
		List<TListBean> projectLists = getListsByProject(projectID, true);
		for (TListBean listBean : projectLists) {
			boolean childIsLeaf = Integer.valueOf(TListBean.LIST_TYPE.SIMPLE).equals(listBean.getListType());
			ListOptionTreeNodeTO listOptionTreeNodeTO = createTreeNode(
					new ListOptionIDTokens(REPOSITORY_TYPE.PROJECT, projectID, CUSTOM_LIST, listBean.getObjectID(), false),
					listBean.getLabel(), listBean.getObjectID(), true, true, true, true, RESOURCE_TYPES.PROJECT_LIST, 
					true, RESOURCE_TYPES.CUSTOM_ENTRY, false,
					false, childIsLeaf, false, childIsLeaf);
			listOptionTreeNodeTO.setIconCls(ICONS_CLS.CUSTOM_LIST_ICON);
			listOptionTreeNodeTO.setChildrenHaveDefaultOption(true);
			listOptionTreeNodeTO.setCanCopy(true);
			listOptionTreeNodeTOs.add(listOptionTreeNodeTO);
		}
		return listOptionTreeNodeTOs;
	}
	
	/**
	 * Gets the global custom list rows
	 * @param personBean
	 * @param locale
	 * @return
	 */
	static List<ListGridRowTO> getPublicCustomListGridRows(TPersonBean personBean, Locale locale) {
		List<ListGridRowTO> listOptionGridRowTOs = new LinkedList<ListGridRowTO>();
		//public lists
		List<TListBean> publicLists = loadPublicLists();
		if (publicLists!=null) {
			boolean isSys = isSys(personBean);
			for (TListBean listBean : publicLists) {
				listOptionGridRowTOs.add(createListGridRow(REPOSITORY_TYPE.PUBLIC, null, CUSTOM_LIST, null,
					listBean.getLabel(), listBean.getObjectID(),  
					false, isSys, true, isSys, RESOURCE_TYPES.GLOBAL_LIST, listBean.getListType(), 
					calculateCascadingType(listBean), ICONS_CLS.CUSTOM_LIST_ICON, locale));
			}
		}
		return listOptionGridRowTOs;
	}	
	
	/**
	 * Gets the project nodes 
	 * @param projectBeans
     * @param locale
	 * @return
	 */
	private static List<ListGridRowTO> getProjectGridRows(List<TProjectBean> projectBeans, Locale locale) {
		List<ListGridRowTO> listOptionTreeNodeTOs = new LinkedList<ListGridRowTO>();
		if (projectBeans!=null && !projectBeans.isEmpty()) {
			for (TProjectBean projectBean : projectBeans) {
				ListGridRowTO listGridRowTO = createListGridRow(Integer.valueOf(REPOSITORY_TYPE.PROJECT), projectBean.getObjectID(), null, null, projectBean.getLabel(),
						null, false, false, false, false, null, null, null, ICONS_CLS.PROJECT_ICON, locale);
				listOptionTreeNodeTOs.add(listGridRowTO);
			}
		}
		return listOptionTreeNodeTOs;
	}
	
	/**
	 * Gets the custom lists grid rows 
	 * @param projectID
	 * @param locale
	 * @return
	 */
	static List<ListGridRowTO> getProjectCustomListGridRows(Integer projectID, Locale locale) {
		List<ListGridRowTO> listOptionGridRowTOs = new LinkedList<ListGridRowTO>();
		//project specific lists
		List<TListBean> projectLists = getListsByProject(projectID, true);
		for (TListBean listBean : projectLists) {
			listOptionGridRowTOs.add(createListGridRow(REPOSITORY_TYPE.PROJECT, projectID, CUSTOM_LIST, null, 
				listBean.getLabel(), listBean.getObjectID(),
				false, true, true, true, RESOURCE_TYPES.PROJECT_LIST, listBean.getListType(), 
				calculateCascadingType(listBean), ICONS_CLS.CUSTOM_LIST_ICON, locale));
		}
		return listOptionGridRowTOs;
	}
	
	/**	 
	 *  TODO change it with a more general mechanism when the cascading select will be
	 *	more configurable (not just these 3 options)
	 *
	 */
	private static Integer calculateCascadingType(TListBean listBean) {
		if (listBean!=null && listBean.getObjectID()!=null && listBean.getListType()!=null && 
				listBean.getListType().intValue()==TListBean.LIST_TYPE.CASCADINGPARENT) {
			//cascading list: now set the cascadingType
			List<TListBean> childLists = getChildLists(listBean.getObjectID());
			if (childLists!=null && !childLists.isEmpty()) {
				if (childLists.size()>1) {
					//more childs
					return Integer.valueOf(TListBean.CASCADING_TYPE.PARENT_CHILDREN);
				}
				TListBean childListBean = childLists.get(0);
				if (childListBean!=null) {
					//grandchild
					childLists = getChildLists(childListBean.getObjectID());
					if (childLists!=null && !childLists.isEmpty()) {
						return Integer.valueOf(TListBean.CASCADING_TYPE.PARENT_CHILD_GRANDCHILD);
					} else {
						return Integer.valueOf(TListBean.CASCADING_TYPE.PARENT_CHILD);
					}
				}
			}
		}
		return  null;
	}
	
	/**
	 * Creates a new option node@p
     * @param listOptionIDTokens
	 * @param label
	 * @param canEdit
	 * @param mightEditChild
	 * @param canDelete
     * @param mightDeleteChild
     * @param nodeListForLabel
	 * @param canAddChild
     * @param nodeChildrenListForLabel
	 * @param leaf
	 * @param hasIcon
     * @param childrenHaveIcon
     * @param hasCssStyle
     * @param childrenHaveCssStyle
	 * @return
	 */
	private static ListOptionTreeNodeTO createTreeNode(ListOptionIDTokens listOptionIDTokens, String label, Integer topParentListID,
			boolean canEdit, boolean mightEditChild, boolean canDelete, boolean mightDeleteChild,
			Integer nodeListForLabel, boolean canAddChild, Integer nodeChildrenListForLabel,
			boolean leaf, boolean hasIcon,
			boolean childrenHaveIcon, boolean hasCssStyle, boolean childrenHaveCssStyle) {
		ListOptionTreeNodeTO listOptionTreeNodeTO = new ListOptionTreeNodeTO();
		listOptionTreeNodeTO.setId(encodeNode(listOptionIDTokens));
		listOptionTreeNodeTO.setLabel(label);
		listOptionTreeNodeTO.setTopParentListID(topParentListID);
		listOptionTreeNodeTO.setType(listOptionIDTokens.getType());
		listOptionTreeNodeTO.setChildListID(listOptionIDTokens.getChildListID());
		listOptionTreeNodeTO.setOptionID(listOptionIDTokens.getOptionID());
		//edit/delete
		listOptionTreeNodeTO.setCanEdit(canEdit);
		listOptionTreeNodeTO.setMightEditChild(mightEditChild);
		listOptionTreeNodeTO.setCanDelete(canDelete);
		listOptionTreeNodeTO.setMightDeleteChild(mightDeleteChild);
		listOptionTreeNodeTO.setNodeListForLabel(nodeListForLabel);
		//add/copy
		listOptionTreeNodeTO.setCanAddChild(canAddChild);
		listOptionTreeNodeTO.setNodeChildrenListForLabel(nodeChildrenListForLabel);
		listOptionTreeNodeTO.setLeaf(leaf);
		//add icon column for non leaf nodes which have childListID (not for main nodes)
		listOptionTreeNodeTO.setHasIcon(hasIcon);
		listOptionTreeNodeTO.setChildrenHaveIcon(childrenHaveIcon);
		listOptionTreeNodeTO.setHasBgrColor(hasCssStyle);
		listOptionTreeNodeTO.setChildrenHaveCssStyle(childrenHaveCssStyle);
		return listOptionTreeNodeTO;
	}
	
	/**
	 * Creates a new grid row
	 * @param repository
	 * @param projectID
	 * @param type
	 * @param labelBean
	 * @param listID
	 * @param leaf
	 * @param canEdit
	 * @param canDelete
	 * @param rowListForLabel
	 * @param locale
	 * @return
	 */
	private static ListOptionGridRowTO createListOptionGridRow(Integer repository, Integer projectID,
			int type, ILabelBean labelBean, Integer listID, boolean leaf,
			boolean canEdit, boolean canDelete, boolean hasChildFilter, Integer rowListForLabel, Locale locale) {
		ListOptionGridRowTO listOptionGridRowTO = new ListOptionGridRowTO();
		ListOptionIDTokens listOptionIDTokens = new ListOptionIDTokens(repository, projectID, type, listID, leaf);
		listOptionIDTokens.setOptionID(labelBean.getObjectID());
		listOptionIDTokens.setLeaf(leaf);
		String node = encodeNode(listOptionIDTokens);
		listOptionGridRowTO.setNode(node);
		listOptionGridRowTO.setObjectID(labelBean.getObjectID());
		listOptionGridRowTO.setLabel(labelBean.getLabel());
		listOptionGridRowTO.setCanEdit(canEdit);
		listOptionGridRowTO.setCanDelete(canDelete);
		listOptionGridRowTO.setHasChildFilter(hasChildFilter);
		listOptionGridRowTO.setRowListForLabel(rowListForLabel);
		OptionBaseBL optionBaseBL = getOptionBaseInstance(listID, labelBean.getObjectID());
		if (optionBaseBL.hasTypeFlag()) {
			listOptionGridRowTO.setHasTypeflag(true);
			listOptionGridRowTO.setTypeflagLabel(optionBaseBL.getTypeflagLabel(listID, labelBean, locale));
			listOptionGridRowTO.setDisableTypeflag(optionBaseBL.isDisableTypeflag());
		}
		if (optionBaseBL.hasPercentComplete()) {
			listOptionGridRowTO.setHasPercentComplete(true);
			listOptionGridRowTO.setPercentComplete(optionBaseBL.getPercentComplete(labelBean));
		}
		if (optionBaseBL.hasDefaultOption()) {
			listOptionGridRowTO.setHasDefaultOption(true);
			listOptionGridRowTO.setDefaultOption(optionBaseBL.isDefaultOption(labelBean));
		}
		listOptionGridRowTO.setHasIcon(optionBaseBL.hasSymbol(leaf));
		String iconName = optionBaseBL.getSymbol(labelBean);
		listOptionGridRowTO.setIconName(iconName);
		listOptionGridRowTO.setIcon("listOptionIcon!serveIconStream.action?node="+node+"&time="+new Date().getTime());
		listOptionGridRowTO.setHasCssStyle(optionBaseBL.hasCssStyle(leaf));
		listOptionGridRowTO.setCssStyle(optionBaseBL.getCSSStyle(labelBean));
		listOptionGridRowTO.setLeaf(leaf);
		return listOptionGridRowTO;
	}
	
	/**
	 * Creates a new grid row
     * @param repository
     * @param projectID
	 * @param type
	 * @param objectID
	 * @param label
	 * @param listID
	 * @param leaf
	 * @param canEdit
     * @param canCopy
	 * @param canDelete
     * @param rowListForLabel
     * @param customListType
     * @param cascadingType
	 * @param iconName
	 * @param locale
	 * @return
	 */
	private static ListGridRowTO createListGridRow(Integer repository, Integer projectID, Integer type, Integer objectID,
			String label, Integer listID, boolean leaf,
			boolean canEdit, boolean canCopy, boolean canDelete, Integer rowListForLabel,
			Integer customListType, Integer cascadingType, 
			String iconName, Locale locale) {
		ListGridRowTO listGridRowTO = new ListGridRowTO();
		ListOptionIDTokens listOptionIDTokens = new ListOptionIDTokens(repository, projectID, type, listID, leaf);
		listOptionIDTokens.setOptionID(objectID);
		listOptionIDTokens.setLeaf(leaf);
		listGridRowTO.setNode(encodeNode(listOptionIDTokens));
		listGridRowTO.setObjectID(objectID);
		listGridRowTO.setLabel(label);
		listGridRowTO.setCanEdit(canEdit);
		listGridRowTO.setCanCopy(canCopy);
		listGridRowTO.setCanDelete(canDelete);
		listGridRowTO.setRowListForLabel(rowListForLabel);
		if (iconName!=null) {
			listGridRowTO.setIconCls(iconName);
		}
		if (customListType!=null) {
			listGridRowTO.setCustomListType(CustomListBL.getInstance().getCustomListTypesString(customListType, locale));
		}
		if (cascadingType!=null) {
			listGridRowTO.setCascadingType(CustomListBL.getInstance().getCascadingTypeString(cascadingType, locale));
		}
		return listGridRowTO;
	}
	
	/**
	 * Get the corresponding implementation for list or system/custom option
	 * @param listID
	 * @param optionID
	 * @param leaf
	 * @param add
	 * @param copy
	 * @return
	 */
	static ListOrOptionBaseBL getListOrOptionBaseInstance(Integer listID, Integer optionID, boolean leaf, boolean add, boolean copy) {
		if (!leaf && (listID==null || //add custom list
				(listID!=null && listID.intValue()>0 && optionID==null && (!add || copy)))) { //edit/delete custom list
			//list
			return CustomListBL.getInstance();
		} else {
			//option
			if (listID!=null) {
				//system or custom list option
				switch (listID.intValue()) {
				case RESOURCE_TYPES.STATUS:
				case RESOURCE_TYPES.PRIORITY:
				case RESOURCE_TYPES.SEVERITY:
				case RESOURCE_TYPES.ISSUETYPE:
				/*case RESOURCE_TYPES.PROJECT_STATUS:
				case RESOURCE_TYPES.RELEASE_STATUS:
				case RESOURCE_TYPES.ACCOUNT_STATUS:*/
					//system option
					return SystemOptionBaseBL.getInstance(listID);
				default:
					//custom parent option
					TListBean listBean = loadByPrimaryKey(listID);
					return CustomOptionBaseBL.getInstance(listBean.getListType());
				}
			} else {
				//custom child option
				TOptionBean optionBean = OptionBL.loadByPrimaryKey(optionID);
				TListBean listBean = loadByPrimaryKey(optionBean.getList());
				return CustomOptionBaseBL.getInstance(listBean.getListType());
			}
		}
	}
	
	/**
	 * Get the corresponding implementation for system/custom option
	 * @param listID
	 * @param optionID
	 * @return
	 */
	static OptionBaseBL getOptionBaseInstance(Integer listID, Integer optionID) {
		if (listID!=null) {
			//system or custom list option
			switch (listID.intValue()) {
			case RESOURCE_TYPES.STATUS:
			case RESOURCE_TYPES.PRIORITY:
			case RESOURCE_TYPES.SEVERITY:
			case RESOURCE_TYPES.ISSUETYPE:
				//system option
				return SystemOptionBaseBL.getInstance(listID);
			default:
				//custom parent option
				if (listID.intValue()<0) {
					listID = Integer.valueOf(-listID.intValue());
				}
				TListBean listBean = loadByPrimaryKey(listID);
				return CustomOptionBaseBL.getInstance(listBean.getListType());
			}
		} else {
			//custom child option
			TOptionBean optionBean = OptionBL.loadByPrimaryKey(optionID);
			TListBean listBean = loadByPrimaryKey(optionBean.getList());
			return CustomOptionBaseBL.getInstance(listBean.getListType());
		}
	}
	
	/**
	 * Drop after drag
	 * @param draggedNodeID
	 * @param droppedToNodeID
	 * @param before
	 * @param locale
	 * @param applicationBean
	 */
	static Integer droppedNear(String draggedNodeID, String droppedToNodeID, boolean before, Locale locale, ApplicationBean applicationBean) {
		ListOptionIDTokens draggedTokens = decodeNode(draggedNodeID);
		Integer listID = draggedTokens.getChildListID();
		Integer draggedOptionID = draggedTokens.getOptionID();
		ListOptionIDTokens droppedToTokens = decodeNode(droppedToNodeID);
		Integer droppedToOptionID = droppedToTokens.getOptionID();
		OptionBaseBL optionBaseBL = getOptionBaseInstance(listID, draggedOptionID);
		//hopefully not needed, but just to be sure
		normalizeSortOrder(draggedNodeID, locale, applicationBean);
		optionBaseBL.dropNear(draggedOptionID, droppedToOptionID, before, listID);
		return draggedOptionID;
	}
	
	/**
	 * Move up the option
	 * @param draggedNodeID
	 * @param locale
	 * @param applicationBean
	 */
	static Integer moveUp(String draggedNodeID, Locale locale, ApplicationBean applicationBean) {
		ListOptionIDTokens draggedTokens = decodeNode(draggedNodeID);
		Integer listID = draggedTokens.getChildListID();
		Integer draggedOptionID = draggedTokens.getOptionID();
		OptionBaseBL optionBaseBL = getOptionBaseInstance(listID, draggedOptionID);
		List<ILabelBean> sortOrderBeans = normalizeSortOrder(draggedNodeID, locale, applicationBean);
		ILabelBean previousLabelBean = null;
		boolean found = false;
		for (ILabelBean labelBean : sortOrderBeans) {
			if (labelBean.getObjectID().equals(draggedOptionID)) {
				found = true;
				break;
			}
			previousLabelBean = labelBean;
		}
		if (found && previousLabelBean!=null) {
			optionBaseBL.dropNear(draggedOptionID, previousLabelBean.getObjectID(), true, listID);
		}
		return draggedOptionID;
	}
	
	/**
	 * Move down the option
	 * @param draggedNodeID
	 * @param locale
	 * @param applicationBean
	 */
	static Integer moveDown(String draggedNodeID, Locale locale, ApplicationBean applicationBean) {
		ListOptionIDTokens draggedTokens = decodeNode(draggedNodeID);
		Integer listID = draggedTokens.getChildListID();
		Integer draggedOptionID = draggedTokens.getOptionID();
		OptionBaseBL optionBaseBL = getOptionBaseInstance(listID, draggedOptionID);
		List<ILabelBean> sortOrderBeans = normalizeSortOrder(draggedNodeID, locale, applicationBean);
		ILabelBean nextSortedBean = null;
		for (Iterator<ILabelBean> iterator = sortOrderBeans.iterator(); iterator.hasNext();) {
			ILabelBean labelBean = iterator.next();
			if (labelBean.getObjectID().equals(draggedOptionID)) {
				if (iterator.hasNext()) {
					nextSortedBean = iterator.next();
					break;
				}
			}
		}
		if (nextSortedBean!=null) {
			optionBaseBL.dropNear(draggedOptionID, nextSortedBean.getObjectID(), false, listID);
		}
		return draggedOptionID;
	}
	
	/**
	 * Normalize the sortorder before change  
	 * @param node
	 * @param locale
	 * @param applicationBean
	 * @return
	 */
	private static List<ILabelBean> normalizeSortOrder(String node, Locale locale, ApplicationBean applicationBean) {
		List<ILabelBean> sortedBeans = null;
		ListOptionIDTokens listOptionIDTokens = decodeNode(node);
		Integer type = listOptionIDTokens.getType();
		Integer listID = listOptionIDTokens.getChildListID();
		OptionBaseBL optionBaseBL = null;
		if (SYSTEM_LIST==type.intValue()) {
			SystemOptionBaseBL systemOptionsBaseBL = SystemOptionBaseBL.getInstance(listID);
			optionBaseBL = systemOptionsBaseBL;
			sortedBeans = systemOptionsBaseBL.getOptions(listID, locale, applicationBean);
		} else {
			Integer optionID = listOptionIDTokens.getOptionID();
			TOptionBean optionBean = OptionBL.loadByPrimaryKey(optionID);
			TListBean listBean = loadByPrimaryKey(listID);
			CustomOptionBaseBL customOptionBaseBL = CustomOptionBaseBL.getInstance(listBean.getListType());
			optionBaseBL = customOptionBaseBL;
			sortedBeans = (List)customOptionBaseBL.getOptions(listID, optionBean.getParentOption());
		}
		return optionBaseBL.normalizeSortOrder(sortedBeans);
	}

	/**
	 * Get all defined list of a type
	 * @param selectType
	 * @param cascadingHierarchy
	 * @param currentValue
	 * @return
	 */
	public static List<TListBean> getSelectsOfType(Integer projectID, int selectType, 
			CascadingHierarchy cascadingHierarchy, Integer currentValue) {
		List<TListBean> listBeans = new LinkedList<TListBean>();
		if (projectID!=null) {
			//get also the ancestor projects' lists to possibly reuse in descendant projects
			List<Integer> projectIDs = ProjectBL.getAncestorProjects(projectID);
			projectIDs.add(projectID);
			List<TListBean> projectListBeans = listDAO.getProjectListsOfType(projectIDs, Integer.valueOf(selectType));
			if (projectListBeans!=null) {
				listBeans.addAll(projectListBeans);
			}
		}
		List<TListBean> publicListBeans = listDAO.getPublicListsOfType(Integer.valueOf(selectType));
		if (publicListBeans!=null) {
			listBeans.addAll(publicListBeans);
		}
		List<TListBean> matchingList;	
		if (cascadingHierarchy==null) {
			matchingList = listBeans;
		} else {
			matchingList = new LinkedList<TListBean>();
			if (listBeans!=null) {
				for (TListBean listBean : listBeans) {
					if (matches(listBean, cascadingHierarchy)) {
						matchingList.add(listBean);
					}
				}
			}			
		}		
		if (currentValue!=null) {
			boolean found = false; 
			for (TListBean listBean : matchingList) {
				if (listBean.getObjectID().equals(currentValue)) {
					found = true;
				}
			}
			if (!found) {
				TListBean listBean = loadByPrimaryKey(currentValue);
				listBean.setName(listBean.getName()+ "!");
				matchingList.add(listBean);
			}
		}
		return matchingList;
	}

	/**
	 * Compares a saved cascading list hierarchy with a CascadingHierarchy i.e. compares two trees
	 * TODO the order of the child lists (subtrees) is the natural order, but the order should not matter
	 * rewrite this method in such a way   
	 * @param listBean
	 * @param cascadingHierarchy
	 * @return
	 */
	private static boolean matches(TListBean listBean, CascadingHierarchy cascadingHierarchy) {
		//get the needed child lists
		List<CascadingHierarchy> neededChildLists = cascadingHierarchy.getChildLists();
		//get the real child lists
		List<TListBean> realChildLists = getChildLists(listBean.getObjectID());
		if (neededChildLists==null) {
			neededChildLists = new LinkedList<CascadingHierarchy>();
		}
		if (realChildLists==null) {
			realChildLists = new LinkedList<TListBean>();
		}
		//number of children differs
		if (neededChildLists.size()!=realChildLists.size()) {
			return false;
		}
		for (int i = 0; i < neededChildLists.size(); i++) {
			CascadingHierarchy cascadingHierarchyChild =  neededChildLists.get(i);
			TListBean listBeanChild =  realChildLists.get(i);
			if (!matches(listBeanChild, cascadingHierarchyChild)) {
				return false;
			}
		}
		//all children match
		return true;
	}



	



	
	
	
	
	
}
