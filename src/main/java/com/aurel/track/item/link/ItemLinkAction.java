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

package com.aurel.track.item.link;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.WBSComparable;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.item.ItemAction;
import com.aurel.track.item.ItemDetailBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.linkType.ILinkType;
import com.aurel.track.linkType.LinkTypeBL;
import com.aurel.track.plugin.PluginManager;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.LabelValueBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Manage the item links
 *
 */
public class ItemLinkAction extends ActionSupport
	implements Preparable, SessionAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	private Map<String,Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale;
	private TPersonBean personBean;
	private Integer workItemID;
	private Integer linkID;

	private String deletedItems;
	private boolean confirmSave;
	private Map<String, String> parametersMap;
	private Integer linkedWorkItemID;
	private String description;
	private String linkTypeWithDirection;
	private boolean includeLayout;
	//add link(s) from issue navigator
	private String workItemIDs;
	//add link from Gantt
	private boolean fromGantt = false;
	//add a new item linked to me
	private boolean addMeAsLinkToNewItem = false;
	private WorkItemContext workItemContext = null;

	/**
	 * reorder the links: move up, move down, drag and drop
	 */
	private String draggedLinkIDs;
	private Integer droppedToLinkID;
	private boolean before;

	/**
	 * Prepare the parameters
	 */
	@Override
	public void prepare() throws Exception {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		if (workItemID==null) {
			workItemContext=(WorkItemContext)session.get("workItemContext");
		}
	}
	/**
	 * Getting all the links to show in the links tab
	 * @return
	 */
	@Override
	public String execute() {
		List<ItemLinkListEntry> links = ItemLinkConfigBL.getItemLinksInTab(workItemID, workItemContext, personBean.getObjectID(), locale);
		String itemsJSON=ItemLinkJSON.encodeLinkList(links);
		session.put(ItemAction.LAST_SELECTED_ITEMD_DETAIL_TAB, ItemDetailBL.TAB_LINKS);
		ItemDetailBL.encodeTabJSON(itemsJSON, includeLayout, ItemDetailBL.TAB_LINKS, personBean.getObjectID(), locale);
		return null;
	}

	/**
	 * Open an item link for edit either
	 * 1. from item's link tab or
	 * 2. from Gantt view or
	 * 3. from item navigator add links (for selected items)
	 * @return
	 */
	public String editItemLink() {
        JSONUtility.encodeJSON(servletResponse, ItemLinkConfigBL.editItemLink(workItemID, workItemContext, linkID, fromGantt, addMeAsLinkToNewItem, personBean, locale));
		return null;
	}

	/**
	 * Encode the specific part after changing the link type
	 * @return
	 */
	public String getSpecificPart() {
		JSONUtility.encodeJSON(servletResponse,
				ItemLinkConfigBL.getSpecificPart(linkTypeWithDirection, workItemID, workItemContext, linkID, personBean, locale));
		return null;
	}

	/**
	 * Saves an item link from an item detail tab or from Gantt view
	 * @return
	 */
	public String saveItemLink() {
		Integer linkTypeID = null;
		Integer linkDirection = null;
		if (linkTypeWithDirection!=null) {
			linkTypeID = MergeUtil.getFieldID(linkTypeWithDirection);
			linkDirection = MergeUtil.getParameterCode(linkTypeWithDirection);
		}
		List<ErrorData> errors = ItemLinkConfigBL.saveItemLink(workItemID, linkedWorkItemID, linkTypeID, linkDirection,
				linkID, description, personBean, locale, parametersMap, workItemContext);
		if (errors!=null && !errors.isEmpty()) {
			JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeErrorDataList(errors, locale));
		} else {
			JSONUtility.encodeJSONSuccess(servletResponse);
		}
		return null;
	}

	/**
	 * Saves an item link from the issue navigator
	 * @return
	 */
	public String saveLinkFromIssueNavigator() {
		Integer createdLinkID = -1;
		Double createdLinkLag = -1.0;
		TWorkItemLinkBean createdWorkItemLinkBean = null;
		List<LabelValueBean> noLinkTypeErrors = new LinkedList<LabelValueBean>();
		if (linkTypeWithDirection==null || linkTypeWithDirection.trim().length()==0) {
			String label=LocalizeUtil.getLocalizedTextFromApplicationResources("item.tabs.itemLink.lbl.linkType",locale);
			noLinkTypeErrors.add(new LabelValueBean(LocalizeUtil.getParametrizedString("common.err.required",
					new Object[]{label},locale) ,"linkTypeWithDirection"));
		}
		if(!noLinkTypeErrors.isEmpty()){
			JSONUtility.encodeJSONErrorsExtJS(ServletActionContext.getResponse(), noLinkTypeErrors);
			return null;
		}
		List<Integer> workItemIDsList = null;
		if (workItemIDs!=null) {
			workItemIDsList = GeneralUtils.createIntegerListFromStringArr(workItemIDs.split(","));
			//get the order according to the wbs (workItemIDs now contains the ID in the order clicked by user in item navigator)
			//load the repotBeans only to set the wbs show value
			List<ReportBean> reportBeans = LoadItemIDListItems.getReportBeansByWorkItemIDs(
					GeneralUtils.createIntArrFromIntegerList(workItemIDsList), true, personBean.getObjectID(), locale, false);
			List<WBSComparable> comparableList = new LinkedList<WBSComparable>();
			if (reportBeans!=null) {
				for (ReportBean reportBean : reportBeans) {
					WBSComparable wbsComparable = new WBSComparable(reportBean.getShowValue(SystemFields.INTEGER_WBS), "\\.", reportBean.getWorkItemBean().getObjectID());
					comparableList.add(wbsComparable);
				}
				Collections.sort(comparableList);
			}
			if(!fromGantt) {
				workItemIDsList = new LinkedList<Integer>();
				for (WBSComparable wbsComparable : comparableList) {
					workItemIDsList.add(wbsComparable.getWorkItemID());
				}
			}
			if (!workItemIDsList.isEmpty()) {
				Integer linkTypeID=MergeUtil.getFieldID(linkTypeWithDirection);
				Integer linkDirection=MergeUtil.getParameterCode(linkTypeWithDirection);
				String linkTypePluginString = LinkTypeBL.getLinkTypePluginString(linkTypeID);
				ILinkType linkType = (ILinkType)PluginManager.getInstance().getPluginClass(
						PluginManager.LINKTYPE_ELEMENT, linkTypePluginString);
				if (linkType!=null) {
					List<TWorkItemLinkBean> linksToAdd = new LinkedList<TWorkItemLinkBean>();
					Map<String, List<LabelValueBean>> errorMap = new HashMap<String, List<LabelValueBean>>();
					List<LabelValueBean> specificParameterErrors = new LinkedList<LabelValueBean>();
					Integer linkPred = null;
					Integer linkSucc = null;
					for (Integer workItemID : workItemIDsList) {
						linkPred = linkSucc;
						linkSucc = workItemID;
						ItemLinkBL.prepareLink(linkPred, linkSucc, linkTypeID, linkDirection, description, linkType,
								parametersMap, personBean, locale, linksToAdd, errorMap, specificParameterErrors);
					}
					if (specificParameterErrors!=null && !specificParameterErrors.isEmpty()) {
						JSONUtility.encodeJSONErrorsExtJS(servletResponse, specificParameterErrors);
						return null;
					} else {
						if (confirmSave || errorMap==null || errorMap.isEmpty()) {
							for (TWorkItemLinkBean workItemLinkBean : linksToAdd) {
								createdLinkID = ItemLinkBL.saveLink(workItemLinkBean);
								createdLinkLag = workItemLinkBean.getLinkLag();
								createdWorkItemLinkBean = workItemLinkBean;
							}
						} else {
							JSONUtility.encodeJSON(servletResponse, ItemLinkJSON.encodeAddLinkFromItemNavigatorErrors(errorMap, linksToAdd.size(), locale));
							return null;
						}
					}
				}
			}
			String response = ItemLinkBL.getGanttCreatedLinkJSONResponse(createdWorkItemLinkBean, reportBeans, createdLinkID, createdLinkLag);
			JSONUtility.encodeJSON(servletResponse, response);
		}else {
			JSONUtility.encodeJSONFailure(servletResponse, LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.ganttView.createLinkError", locale));
		}
		return null;
	}

	/**
	 * Delete the selected links
	 * @return
	 */
	public String deleteLinks() {
		ItemLinkConfigBL.deleteLinks(workItemID, workItemContext, deletedItems);
		JSONUtility.encodeJSONSuccess(servletResponse);
		return null;
	}

	/**
	 * Drag and drop links to reorder
	 * @return
	 */
	public String droppedNear() {
		if (workItemID==null) {
			//new item
			ItemLinkSortOrderBL.dragAndDrop(workItemContext.getWorkItemsLinksMap(), draggedLinkIDs, droppedToLinkID, before);
		} else {
			ItemLinkSortOrderBL.dragAndDrop(workItemID, draggedLinkIDs, droppedToLinkID, before);
		}
		JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONSuccess());
		return null;
	}

	/**
	 * Move links up
	 * @return
	 */
	public String moveUp() {
		if (workItemID==null) {
			//new item
			ItemLinkSortOrderBL.moveUp(workItemContext.getWorkItemsLinksMap(), draggedLinkIDs);
		} else {
			ItemLinkSortOrderBL.moveUp(workItemID, draggedLinkIDs);
		}
		JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONSuccess());
		return null;
	}

	/**
	 * Move links down
	 * @return
	 */
	public String moveDown() {
		if (workItemID==null) {
			//new item
			ItemLinkSortOrderBL.moveDown(workItemContext.getWorkItemsLinksMap(), draggedLinkIDs);
		} else {
			ItemLinkSortOrderBL.moveDown(workItemID, draggedLinkIDs);
		}
		JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONSuccess());
		return null;
	}

	/**
	 * @param session the session to set
	 */
	@Override
	public void setSession(Map<String,Object> session) {
		this.session = session;
	}
	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	/**
	 * @param workItemID the itemID to set
	 */
	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}
	/**
	 * @param linkID the linkID to set
	 */
	public void setLinkID(Integer linkID) {
		this.linkID = linkID;
	}

	public void setDraggedLinkIDs(String draggedLinkIDs) {
		this.draggedLinkIDs = draggedLinkIDs;
	}

	public void setDroppedToLinkID(Integer droppedToLinkID) {
		this.droppedToLinkID = droppedToLinkID;
	}

	public void setBefore(boolean before) {
		this.before = before;
	}

	/**
	 * @param deletedItems the deletedItems to set
	 */
	public void setDeletedItems(String deletedItems) {
		this.deletedItems = deletedItems;
	}

	public Map<String, String> getParametersMap() {
		return parametersMap;
	}

	public void setParametersMap(Map<String, String> parametersMap) {
		this.parametersMap = parametersMap;
	}

	public void setLinkTypeWithDirection(String linkTypeWithDirection) {
		this.linkTypeWithDirection = linkTypeWithDirection;
	}

	public void setIncludeLayout(boolean includeLayout) {
		this.includeLayout = includeLayout;
	}

	public void setLinkedWorkItemID(Integer linkedWorkItemID) {
		this.linkedWorkItemID = linkedWorkItemID;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setWorkItemIDs(String workItemIDs) {
		this.workItemIDs = workItemIDs;
	}

	public void setConfirmSave(boolean confirmSave) {
		this.confirmSave = confirmSave;
	}

	public void setFromGantt(boolean fromGantt) {
		this.fromGantt = fromGantt;
	}
	
	public void setAddMeAsLinkToNewItem(boolean addMeAsLinkToNewItem) {
		this.addMeAsLinkToNewItem = addMeAsLinkToNewItem;
	}

	
}
