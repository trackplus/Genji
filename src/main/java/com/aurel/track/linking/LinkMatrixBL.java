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

package com.aurel.track.linking;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;

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
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.PropertiesHelper;

/**
 * Business logic for link matrix
 * @author Tamas
 *
 */
public class LinkMatrixBL {
	private static final Logger LOGGER = LogManager.getLogger(LinkMatrixBL.class);
	/**
	 * Render the link matrix page
	 */
	static String loadConfig(TPersonBean personBean, Locale locale) {
		Integer columnFilterID=null;
		Integer rowsFilterID=null;
		String linkTypeWithDirection=null;
		String lastQuery= PropertiesHelper.getProperty(personBean.getMoreProperties(), TPersonBean.LINKING_LAST_SEARCH);
		if(lastQuery!=null){
			String[] stringArr = lastQuery.split(";");
			if(stringArr!=null&&stringArr.length==3){
				try{
					columnFilterID=Integer.parseInt(stringArr[0]);
					if (columnFilterID!=null) {
						if (FilterBL.loadByPrimaryKey(columnFilterID)==null) {
							LOGGER.debug("Column filter " + columnFilterID + " was deleted");
							columnFilterID = null;
						}
					}
				}catch (Exception ex){}
				try{
					rowsFilterID=Integer.parseInt(stringArr[1]);
					if (rowsFilterID!=null) {
						if (FilterBL.loadByPrimaryKey(rowsFilterID)==null) {
							LOGGER.debug("Row filter " + rowsFilterID + " was deleted");
							rowsFilterID = null;
						}
					}
				}catch (Exception ex){}
				linkTypeWithDirection=stringArr[2];
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
		return LinkMatrixJSON.getConfigJSON(columnFilterID, rowsFilterID, linkTypeWithDirection, linkTypes);
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
	static String loadLinks(Integer columnsQueryID, Integer rowsQueryID, String linkTypeWithDirection, TPersonBean personBean, Locale locale) {
		if(columnsQueryID==null&&rowsQueryID==null&&linkTypeWithDirection==null){
			String errorMessage="All fields are required";
			JSONUtility.encodeJSONFailure(ServletActionContext.getResponse(),errorMessage);
			return  null;
		}
		//store last query
		StringBuffer lastSearch=new StringBuffer();
		lastSearch.append(columnsQueryID).append(";");
		lastSearch.append(rowsQueryID).append(";");
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
		List<TWorkItemBean> itemsColumns = null;
		try {
			if (isInline) {
				itemsColumns = getItemsIncludingDocumentItemType(columnsQueryID, personBean, locale);
			} else {
				itemsColumns = TreeFilterExecuterFacade.getSavedFilterWorkItemBeans(columnsQueryID, locale, personBean, new LinkedList<ErrorData>(), false);
			}
		} catch (TooManyItemsToLoadException e1) {
			LOGGER.info("Number of items to load " + e1.getItemCount());
			String errorMessage="tooManyItems";
			JSONUtility.encodeJSONFailure(ServletActionContext.getResponse(),errorMessage);
			return  null;
		}
		List<TWorkItemBean> itemsRows = null;
		try {
			if (isInline) {
				itemsRows = getItemsIncludingDocumentItemType(rowsQueryID, personBean, locale);
			} else {
				itemsRows = TreeFilterExecuterFacade.getSavedFilterWorkItemBeans(rowsQueryID, locale, personBean, new LinkedList<ErrorData>(), false);
			}
		} catch (TooManyItemsToLoadException e1) {
			LOGGER.info("Number of items to load " + e1.getItemCount());
			String errorMessage="tooManyItems";
			JSONUtility.encodeJSONFailure(ServletActionContext.getResponse(),errorMessage);
			return null;
		}
		List<Integer> workItemIDsColumns=GeneralUtils.createIntegerListFromBeanList(itemsColumns);
		List<Integer> workItemIDsRows=GeneralUtils.createIntegerListFromBeanList(itemsRows);
		LOGGER.debug("Vertical items found(columns):" + itemsColumns.size());
		LOGGER.debug("Horizontal items found(rows):" + itemsRows.size());
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
		return LinkMatrixJSON.getLoadMatrixJSON(itemsColumns, itemsRows, linkList, reverseLinkList, workItemIDsRows, linkDirection,readOnly);
	}
}
