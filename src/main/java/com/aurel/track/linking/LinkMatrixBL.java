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

package com.aurel.track.linking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.aurel.track.admin.customize.category.CategoryBL;
import com.aurel.track.admin.customize.category.CategoryPickerBL;
import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.category.filter.QNode;
import com.aurel.track.admin.customize.category.filter.TreeFilterFacade;
import com.aurel.track.admin.customize.category.filter.execute.TreeFilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperFromQNodeTransformer;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.TreeFilterLoaderBL;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TQueryRepositoryBean.QUERY_PURPOSE;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.item.link.InlineItemLinkBL;
import com.aurel.track.item.link.ItemLinkBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.linkType.ILinkType;
import com.aurel.track.linkType.LinkTypeBL;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.PropertiesHelper;
import com.aurel.track.util.TreeNode;

/**
 * Business logic for link matrix
 * @author Tamas
 *
 */
public class LinkMatrixBL {
	private static final Logger LOGGER = LogManager.getLogger(LinkMatrixBL.class);

	/**
	 * Which items to show in rows/columns
	 * @author Tamas
	 *
	 */
	public static interface LINKED_FLAG {
		public static final int ALL = 1;
		public static final int ONLY_LINKED = 2;
		public static final int ONLY_NOT_LINKED = 3;
	}

	/**
	 * Render the link matrix page
	 */
	static String loadConfig(TPersonBean personBean, Locale locale) {
		Integer columnFilterID=null;
		Integer rowsFilterID=null;
		Integer columnLinkedFlag = LINKED_FLAG.ALL;
		Integer rowLinkedFlag = LINKED_FLAG.ALL;
		String linkTypeWithDirection=null;
		String lastQuery= PropertiesHelper.getProperty(personBean.getMoreProperties(), TPersonBean.LINKING_LAST_SEARCH);
		if(lastQuery!=null){
			String[] stringArr = lastQuery.split(";");
			if (stringArr!=null && stringArr.length==5){
				try {
					columnFilterID=Integer.parseInt(stringArr[0]);
					if (columnFilterID!=null) {
						if (FilterBL.loadByPrimaryKey(columnFilterID)==null) {
							LOGGER.debug("Column filter " + columnFilterID + " was deleted");
							columnFilterID = null;
						}
					}
				} catch (Exception ex) {
					LOGGER.warn("Getting the column filter failed with"  + ex.getMessage());
				}
				try {
					columnLinkedFlag = Integer.parseInt(stringArr[1]);
				} catch (Exception ex) {
					LOGGER.warn("Getting the column linked flag failed with"  + ex.getMessage());
				}
				try {
					rowsFilterID=Integer.parseInt(stringArr[2]);
					if (rowsFilterID!=null) {
						if (FilterBL.loadByPrimaryKey(rowsFilterID)==null) {
							LOGGER.debug("Row filter " + rowsFilterID + " was deleted");
							rowsFilterID = null;
						}
					}
				} catch (Exception ex) {
					LOGGER.warn("Getting the row filter failed with"  + ex.getMessage());
				}
				try {
					rowLinkedFlag = Integer.parseInt(stringArr[3]);
				} catch (Exception ex) {
					LOGGER.warn("Getting the row linked flag failed with"  + ex.getMessage());
				}
				linkTypeWithDirection=stringArr[4];
			}
		}
		List<LabelValueBean> linkTypes = LinkTypeBL.getLinkTypeNamesList(locale, false, false);
		boolean found=false;
		if (linkTypeWithDirection!=null && linkTypes!=null) {
			for (LabelValueBean labelValueBean : linkTypes) {
				if (linkTypeWithDirection.equals(labelValueBean.getValue())) {
					found = true;
					break;
				}
			}
			if (!found) {
				//link type was deleted
				LOGGER.debug("Link type " + linkTypeWithDirection + " was deleted");
				linkTypeWithDirection = null;
			}
		}

		List<TreeNode> filterTree = CategoryPickerBL.getPickerNodesEager(personBean, false, false, null,
	            true, null, null, locale, CategoryBL.CATEGORY_TYPE.ISSUE_FILTER_CATEGORY);
		List<IntegerStringBean> linkFlags = new ArrayList<IntegerStringBean>(3);
		linkFlags.add(new IntegerStringBean("linking.opt.all", LINKED_FLAG.ALL));
		linkFlags.add(new IntegerStringBean("linking.opt.onlyLinked", LINKED_FLAG.ONLY_LINKED));
		linkFlags.add(new IntegerStringBean("linking.opt.onlyNotLinked", LINKED_FLAG.ONLY_NOT_LINKED));
		return LinkMatrixJSON.getConfigJSON(filterTree, columnFilterID, columnLinkedFlag, rowsFilterID, rowLinkedFlag, linkFlags, linkTypeWithDirection, linkTypes);
	}

	/**
	 * Add the item types with typeflag document to the filter (normally they are not allowed to see in Genji)
	 * @param filterID
	 * @param personBean
	 * @param locale
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	private static List<TWorkItemBean> getItemsIncludingDocumentItemType(Integer filterID, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException {
		TQueryRepositoryBean queryRepositoryBean = (TQueryRepositoryBean)TreeFilterFacade.getInstance().getByKey(filterID);
		Integer queryType = queryRepositoryBean.getQueryType();
		if (queryType!=null && queryType.intValue()==QUERY_PURPOSE.TREE_FILTER) {
			QNode extendedRootNode = FilterBL.loadNode(queryRepositoryBean);
			FilterUpperTO filterUpperTO = FilterUpperFromQNodeTransformer.getFilterSelectsFromTree(extendedRootNode, true, true, personBean, locale, true);
			TreeFilterExecuterFacade.prepareFilterUpperTO(filterUpperTO, personBean, locale, null, null);
			QNode rootNode = TreeFilterLoaderBL.getOriginalTree(extendedRootNode);
			Integer[] selectedItemTypes = filterUpperTO.getSelectedIssueTypes();
			List<TListTypeBean> documentItemTypeBeans = IssueTypeBL.loadStrictDocumentTypes();
			if (documentItemTypeBeans!=null && documentItemTypeBeans.size()>0) {
				List<Integer> selectedItemTypeIDs = null;
				List<Integer> documentItemTypes = GeneralUtils.createIntegerListFromBeanList(documentItemTypeBeans);
				if (selectedItemTypes==null || selectedItemTypes.length==0) {
					Integer[] projectTypeIDs = ProjectTypesBL.getProjectTypeIDsForProjectIDs(filterUpperTO.getSelectedProjects());
					List<TListTypeBean> selectableItemTypeBeans = null;
					if (projectTypeIDs == null || projectTypeIDs.length==0) {
						selectableItemTypeBeans = IssueTypeBL.loadAllSelectable();
					} else {
						selectableItemTypeBeans = IssueTypeBL.loadAllowedByProjectTypes(projectTypeIDs);
					}
					selectedItemTypeIDs = GeneralUtils.createIntegerListFromBeanList(selectableItemTypeBeans);
					LOGGER.debug("No item types explicitly selected. Add all " + LookupContainer.getLocalizedLabelBeanListLabels(
							SystemFields.INTEGER_ISSUETYPE, GeneralUtils.createIntegerSetFromIntegerList(selectedItemTypeIDs), locale));
				} else {
					selectedItemTypeIDs = GeneralUtils.createIntegerListFromIntegerArr(selectedItemTypes);
					LOGGER.debug("Item types already selected: " + LookupContainer.getLocalizedLabelBeanListLabels(
							SystemFields.INTEGER_ISSUETYPE, GeneralUtils.createIntegerSetFromIntegerList(selectedItemTypeIDs), locale));
				}
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Add Document item types: " + LookupContainer.getLocalizedLabelBeanListLabels(
							SystemFields.INTEGER_ISSUETYPE, GeneralUtils.createIntegerSetFromIntegerList(documentItemTypes), locale));
				}
				selectedItemTypeIDs.addAll(documentItemTypes);
				filterUpperTO.setSelectedIssueTypes(GeneralUtils.createIntegerArrFromCollection(selectedItemTypeIDs));
			}
			return TreeFilterExecuterFacade.getInstantTreeFilterWorkItemBeans(filterUpperTO, rootNode, filterID, personBean, locale, false, null, null, null, null);
		}
		return null;
	}

	/**
	 * Load the link matrix
	 * @return
	 */
	static String loadLinks(Integer columnFilterID, Integer columnLinkedFlag, Integer rowFilterID, Integer rowLinkedFlag, String linkTypeWithDirection, TPersonBean personBean, Locale locale) {
		if (columnFilterID==null && rowFilterID==null && linkTypeWithDirection==null){
			String errorMessage="All fields are required";
			JSONUtility.encodeJSONFailure(ServletActionContext.getResponse(),errorMessage);
			return null;
		}
		if (columnLinkedFlag==null) {
			columnLinkedFlag = LINKED_FLAG.ALL;
		}
		if (rowLinkedFlag==null) {
			rowLinkedFlag = LINKED_FLAG.ALL;
		}
		//store last query
		StringBuffer lastSearch=new StringBuffer();
		lastSearch.append(columnFilterID).append(";");
		lastSearch.append(columnLinkedFlag).append(";");
		lastSearch.append(rowFilterID).append(";");
		lastSearch.append(rowLinkedFlag).append(";");
		lastSearch.append(linkTypeWithDirection);
		String preferences = personBean.getPreferences();
		personBean.setPreferences(PropertiesHelper.setProperty(preferences, TPersonBean.LINKING_LAST_SEARCH, lastSearch.toString()));
		PersonBL.saveSimple(personBean);
		Integer linkTypeID = MergeUtil.getFieldID(linkTypeWithDirection);
		Integer linkDirection = MergeUtil.getParameterCode(linkTypeWithDirection);
		ILinkType linkType = LinkTypeBL.getLinkTypePluginInstanceByLinkTypeKey(linkTypeID);
		boolean isInline = false;
		if (linkType!=null) {
			isInline = linkType.isInline();
			LOGGER.debug("Link type is inline: " + isInline);
		}
		List<TWorkItemBean> columnItemBeans = null;
		try {
			if (isInline) {
				columnItemBeans = getItemsIncludingDocumentItemType(columnFilterID, personBean, locale);
			} else {
				columnItemBeans = TreeFilterExecuterFacade.getSavedFilterWorkItemBeans(columnFilterID, locale, personBean, new LinkedList<ErrorData>(), false);
			}
		} catch (TooManyItemsToLoadException e1) {
			LOGGER.info("Number of items to load " + e1.getItemCount());
			String errorMessage="tooManyItems";
			JSONUtility.encodeJSONFailure(ServletActionContext.getResponse(),errorMessage);
			return  null;
		}
		List<TWorkItemBean> rowItemBeans = null;
		try {
			if (isInline) {
				rowItemBeans = getItemsIncludingDocumentItemType(rowFilterID, personBean, locale);
			} else {
				rowItemBeans = TreeFilterExecuterFacade.getSavedFilterWorkItemBeans(rowFilterID, locale, personBean, new LinkedList<ErrorData>(), false);
			}
		} catch (TooManyItemsToLoadException e1) {
			LOGGER.info("Number of items to load " + e1.getItemCount());
			String errorMessage="tooManyItems";
			JSONUtility.encodeJSONFailure(ServletActionContext.getResponse(),errorMessage);
			return null;
		}
		List<Integer> workItemIDsColumns=GeneralUtils.createIntegerListFromBeanList(columnItemBeans);
		List<Integer> workItemIDsRows=GeneralUtils.createIntegerListFromBeanList(rowItemBeans);
		LOGGER.debug("Vertical items found (columns):" + columnItemBeans.size());
		LOGGER.debug("Horizontal items found (rows):" + rowItemBeans.size());
		List<Integer> linkTypes=new ArrayList<Integer>();
		linkTypes.add(linkTypeID);
		boolean bidirectional=LinkTypeBL.isBidirectional(linkTypeID);
		List<int[]> rowChunks = GeneralUtils.getListOfChunks(workItemIDsRows);
		List<int[]> columnChunks = GeneralUtils.getListOfChunks(workItemIDsColumns);
		List<TWorkItemLinkBean> linkList = null;
		List<TWorkItemLinkBean> reverseLinkList = null;
		if (bidirectional) {
			if (linkDirection.intValue()==ILinkType.LINK_DIRECTION.UNIDIRECTIONAL_OUTWARD) {
				linkList = ItemLinkBL.getLinkedChunks(rowChunks, columnChunks, linkTypeID, linkDirection);
				int reverseDirection = LinkTypeBL.getReverseDirection(linkDirection);
				reverseLinkList = ItemLinkBL.getLinkedChunks(columnChunks, rowChunks, linkTypeID, reverseDirection);
			} else {
				if (linkDirection.intValue()==ILinkType.LINK_DIRECTION.UNIDIRECTIONAL_INWARD) {
					reverseLinkList = ItemLinkBL.getLinkedChunks(rowChunks, columnChunks,  linkTypeID, linkDirection);
					int reverseDirection = LinkTypeBL.getReverseDirection(linkDirection);
					linkList = ItemLinkBL.getLinkedChunks(columnChunks, rowChunks, linkTypeID, reverseDirection);
				}
			}
		} else {
			if (linkDirection.intValue()==ILinkType.LINK_DIRECTION.UNIDIRECTIONAL_OUTWARD) {
				linkList = ItemLinkBL.getLinkedChunks(rowChunks, columnChunks, linkTypeID, linkDirection);
			} else {
				if (linkDirection.intValue()==ILinkType.LINK_DIRECTION.UNIDIRECTIONAL_INWARD) {
					linkList = ItemLinkBL.getLinkedChunks(columnChunks, rowChunks, linkTypeID, linkDirection);
				}
			}
		}
		Integer inlineLinkType=InlineItemLinkBL.getInlineItemLinkType();
		boolean readOnly=linkTypeID.equals(inlineLinkType);
		Map<String, String> linksDescription = new HashMap<String, String>();
		Map<String, String> linksMap = getLinksMap(columnItemBeans,  columnLinkedFlag, rowItemBeans, rowLinkedFlag, linkList, reverseLinkList, linkDirection, /*workItemIDsRows,*/ linksDescription);
		return LinkMatrixJSON.getLoadMatrixJSON(columnItemBeans, rowItemBeans, linksMap, linksDescription, readOnly);
	}



	/**
	 * Gets the linked itemID_itemID -> itemLinkID map
	 * @param columnItemBeans
	 * @param columnLinkedFlag
	 * @param rowItemBeans
	 * @param rowLinkedFlag
	 * @param linkList
	 * @param reverseLinkList
	 * @param linkDirection
	 * @param linksDescription
	 * @return
	 */
	private static Map<String, String> getLinksMap(List<TWorkItemBean> columnItemBeans, Integer columnLinkedFlag, List<TWorkItemBean> rowItemBeans, Integer rowLinkedFlag,
			List<TWorkItemLinkBean> linkList, List<TWorkItemLinkBean> reverseLinkList,
			Integer linkDirection, /*List<Integer> workItemIDsRows,*/ Map<String, String> linksDescription) {
		Map<String, String> linksMap = new HashMap<String, String>();
		Set<Integer> rowIDSet = new HashSet<Integer>();
		Set<Integer> columnIDSet = new HashSet<Integer>();
		if (linkList!=null && !linkList.isEmpty()){
			for(TWorkItemLinkBean workItemLinkBean:linkList) {
				Integer rowItemID = null;
				Integer columnItemID = null;
				if (linkDirection.intValue()==ILinkType.LINK_DIRECTION.UNIDIRECTIONAL_OUTWARD) {
					rowItemID = workItemLinkBean.getLinkPred();
					columnItemID = workItemLinkBean.getLinkSucc();
				} else {
					rowItemID = workItemLinkBean.getLinkSucc();
					columnItemID = workItemLinkBean.getLinkPred();
				}
				if (rowItemID!=null) {
					rowIDSet.add(rowItemID);
				}
				if (columnItemID!=null) {
					columnIDSet.add(columnItemID);
				}

				if (columnLinkedFlag.intValue()!=LINKED_FLAG.ONLY_NOT_LINKED && rowLinkedFlag.intValue()!=LINKED_FLAG.ONLY_NOT_LINKED) {
					if (workItemLinkBean.getDescription()!=null) {
						linksDescription.put(workItemLinkBean.getObjectID().toString(), workItemLinkBean.getDescription());
					}
					String key = rowItemID + "_" + columnItemID;

					linksMap.put(key,workItemLinkBean.getObjectID().toString());
				}

			}
		}
		if (reverseLinkList!=null && !reverseLinkList.isEmpty()) {
			for(TWorkItemLinkBean workItemLinkBean:reverseLinkList) {
				Integer rowItemID = null;
				Integer columnItemID = null;
				if (linkDirection.intValue()==ILinkType.LINK_DIRECTION.UNIDIRECTIONAL_OUTWARD) {
					rowItemID = workItemLinkBean.getLinkSucc();
					columnItemID = workItemLinkBean.getLinkPred();
				} else {
					rowItemID = workItemLinkBean.getLinkPred();
					columnItemID = workItemLinkBean.getLinkSucc();
				}
				if (rowItemID!=null) {
					rowIDSet.add(rowItemID);
				}
				if (columnItemID!=null) {
					columnIDSet.add(columnItemID);
				}
				if (columnLinkedFlag.intValue()!=LINKED_FLAG.ONLY_NOT_LINKED && rowLinkedFlag.intValue()!=LINKED_FLAG.ONLY_NOT_LINKED) {
					if (workItemLinkBean.getDescription()!=null) {
						linksDescription.put(workItemLinkBean.getObjectID().toString(),workItemLinkBean.getDescription());
					}
					String key = rowItemID + "_" + columnItemID;;
					linksMap.put(key,workItemLinkBean.getObjectID().toString());
				}
			}
		}
		switch (rowLinkedFlag) {
		case LINKED_FLAG.ONLY_LINKED:
			for (Iterator<TWorkItemBean> iterator = rowItemBeans.iterator(); iterator.hasNext();) {
				TWorkItemBean rowItemBean = iterator.next();
				if (!rowIDSet.contains(rowItemBean.getObjectID())) {
					iterator.remove();
				}
			}
			break;
		case LINKED_FLAG.ONLY_NOT_LINKED:
			for (Iterator<TWorkItemBean> iterator = rowItemBeans.iterator(); iterator.hasNext();) {
				TWorkItemBean rowItemBean = iterator.next();
				if (rowIDSet.contains(rowItemBean.getObjectID())) {
					iterator.remove();
				}
			}
			break;
		}
		switch (columnLinkedFlag) {
		case LINKED_FLAG.ONLY_LINKED:
			for (Iterator<TWorkItemBean> iterator = columnItemBeans.iterator(); iterator.hasNext();) {
				TWorkItemBean columnItemBean = iterator.next();
				if (!columnIDSet.contains(columnItemBean.getObjectID())) {
					iterator.remove();
				}
			}
			break;
		case LINKED_FLAG.ONLY_NOT_LINKED:
			for (Iterator<TWorkItemBean> iterator = columnItemBeans.iterator(); iterator.hasNext();) {
				TWorkItemBean columnItemBean = iterator.next();
				if (columnIDSet.contains(columnItemBean.getObjectID())) {
					iterator.remove();
				}
			}
			break;
		}
		return linksMap;
	}
}
