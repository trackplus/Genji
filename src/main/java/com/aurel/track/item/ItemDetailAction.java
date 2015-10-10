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



package com.aurel.track.item;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.errors.ErrorHandlerJSONAdapter;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.item.consInf.ConsInfBL;
import com.aurel.track.item.consInf.ConsInfShow;
import com.aurel.track.item.consInf.RaciRole;
import com.aurel.track.item.history.HistoryComparator;
import com.aurel.track.item.history.HistoryLoaderBL;
import com.aurel.track.item.history.HistoryLoaderBL.LONG_TEXT_TYPE;
import com.aurel.track.item.history.HistorySaverBL;
import com.aurel.track.item.history.HistoryValues;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.PropertiesHelper;
import com.aurel.track.vc.RepositoryFileViewer;
import com.aurel.track.vc.Revision;
import com.aurel.track.vc.VersionControlMap;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class ItemDetailAction extends ActionSupport implements Preparable, SessionAware/*, ServletRequestAware*/ {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(ItemDetailAction.class);
	
	private Map<String, Object> session;
	private Locale locale;
	private TPersonBean person;
	private Integer workItemID;
	private Integer projectID;
	private Map updateMap;
	private String deletedItems;
	private String deletedConsultants;
	private String deletedInformants;
	private String deletedConsultantGroups;
	private String deletedInformantGroups;
	private String comment;
	private Integer parentID;
	private Integer commentID;
	private String description;
	private Integer attachmentID;
	private Integer gridID;
	private String columns;
	private boolean includeLayout;
	
	
	private Integer tabID;

	private String iconsPath;
	private List<RevisionTO> revisions;
	private String lastModified;
	
	public void prepare() {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		person = (TPersonBean) session.get(Constants.USER_KEY);
	}
	
	public String formatDateTime(Date date){
		return DateTimeUtils.getInstance().formatGUIDateTime(date, locale);
	}

	/**
	 * Gets the history for the workItem
	 * @return
	 */
	public String doGetHistory(){
		boolean historyFull=true;
		String historyFullStr=PropertiesHelper.getProperty(person.getMoreProperties(), TPersonBean.HISTORY_FULL);
		if(historyFullStr!=null&&historyFullStr.equalsIgnoreCase("false")){
			historyFull=false;
		}else{
			historyFull=true;
		}
		
		boolean showCommentsHistory=true;
		String showCommentsHistoryStr=PropertiesHelper.getProperty(person.getMoreProperties(), TPersonBean.SHOW_COMMENTS_HISTORY);
		if(showCommentsHistoryStr!=null&&showCommentsHistoryStr.equalsIgnoreCase("true")){
			showCommentsHistory=true;
		}else{
			showCommentsHistory=false;
		}
		Integer issueType = null;
		TWorkItemBean workItemBean = null;
		try {
			workItemBean = ItemBL.loadWorkItem(workItemID);
			if (workItemBean!=null) {
				issueType = workItemBean.getListTypeID();
			}
		} catch (ItemLoaderException e) {
		}
		
		//get the history entire FieldChange history 
		//(comments also because the might be shown also in the first tab)

		SortedMap<Integer, Map<Integer, HistoryValues>> allHistoryMap=HistoryLoaderBL.getRestrictedWorkItemHistory(person.getObjectID(),
				workItemBean, locale, false, LONG_TEXT_TYPE.ISFULLHTML);

		List<HistoryValues> historyList= HistoryLoaderBL.getHistoryValuesList(allHistoryMap, showCommentsHistory);
		Collections.sort(historyList, new HistoryComparator());
		String history=ItemDetailBL.encodeJSON_HistoryList(historyList,historyFull,locale);
		encodeTabJSON(history);
		return null;
		
	}

	private void encodeTabJSON(String itemsJSON) {
		session.put(ItemAction.LAST_SELECTED_ITEMD_DETAIL_TAB, tabID);
		ItemDetailBL.encodeTabJSON(itemsJSON,includeLayout, tabID, person.getObjectID(),locale);
	}

	/**
	 * Gets the list of comments added to the workItem
	 * @return
	 */
	public String doGetComments() {
		List<HistoryValues> comments = HistoryLoaderBL.getRestrictedWorkItemComments(person.getObjectID(), workItemID, locale, false, LONG_TEXT_TYPE.ISFULLHTML);
		boolean isProjectAdmin = PersonBL.isProjectAdmin(person.getObjectID(), projectID);
		boolean isProjectActive = ProjectBL.projectIsActive(projectID);
		encodeTabJSON(ItemDetailBL.encodeJSON_Comments(comments,locale, person.getObjectID(), isProjectActive, isProjectAdmin));
		return null;
	}
	public String saveGridConfig(){
		List<TReportLayoutBean> layout=new ArrayList<TReportLayoutBean>();
		List<TReportLayoutBean> groupingList=new ArrayList<TReportLayoutBean>();
		StringTokenizer st=new StringTokenizer(columns,";");
		int i=0;
		int j=0;
		while (st.hasMoreTokens()) {
			String col = st.nextToken();
			if(col!=null&&col.length()>0){
				StringTokenizer stCol=new StringTokenizer(col,":");
				String id=stCol.nextToken();
				String hidden=stCol.nextToken();
				String width=stCol.nextToken();
				//String sortable=stCol.nextToken();
				String direction=stCol.nextToken();
				if(direction.equals("-")){
					direction=null;
				}
				String grouping=stCol.nextToken();
				TReportLayoutBean reportLayoutBean = new TReportLayoutBean();
				reportLayoutBean.setPerson(person.getObjectID());
				reportLayoutBean.setFieldPosition(++i);
				int widthInt= Integer.parseInt(width);
				if(widthInt==0){
					ItemGridLayoutBL.ILayout hl=ItemGridLayoutBL.getLayoutFromID(Integer.parseInt(id),gridID);
					if(hl!=null){
						widthInt=hl.getWidth();
					}else{
						widthInt=75;
					}
				}
				reportLayoutBean.setFieldWidth(widthInt);
				reportLayoutBean.setReportField(Integer.parseInt(id));
				reportLayoutBean.setFieldSortDirection(direction); //null, Y, N
				reportLayoutBean.setFieldType(getFieldType(hidden));
				//reportLayoutBean.setFieldSortOrder(grouping.equalsIgnoreCase("true"));
				layout.add(reportLayoutBean);
				boolean isGrouping = getGrouping(grouping);
				if (isGrouping) {
					reportLayoutBean = new TReportLayoutBean();
					reportLayoutBean.setPerson(person.getObjectID());
					reportLayoutBean.setFieldPosition(++j);
					reportLayoutBean.setFieldWidth(-1);
					reportLayoutBean.setReportField(Integer.parseInt(id));
					//reportLayoutBean.setFieldSortDirection(direction); //null, Y, N
					reportLayoutBean.setFieldType(TReportLayoutBean.FIELD_TYPE.GROUP);
					//reportLayoutBean.setExpanding(???);
					groupingList.add(reportLayoutBean);
				}
			}
		}
		ItemGridLayoutBL.removeGridLayout(person.getObjectID(), gridID);
		ItemGridLayoutBL.saveGridLayout(layout, gridID);
		ItemGridLayoutBL.saveGridLayout(groupingList, gridID);
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}
		  
	private static Integer getFieldType(String hiddenString) {
		if (hiddenString.equalsIgnoreCase("true")) {
			return TReportLayoutBean.FIELD_TYPE.HIDDEN;
		} else {
			return TReportLayoutBean.FIELD_TYPE.VISIBLE;
		}		
	}
	
	private static boolean getGrouping(String groupingString) {
		return groupingString.equalsIgnoreCase("true");
	}
	
	/**
	 * Saves a new/modified comment
	 * @return
	 */
	public String saveComment(){
		List<ErrorData> errorList=new ArrayList<ErrorData>();
		Date originalLastModifiedDate=null;
		Date lastModifiedDate= DateTimeUtils.getInstance().parseISODateTime(lastModified);
		TWorkItemBean workItemBean=ItemBL.loadWorkItemSystemAttributes(workItemID);
		if(workItemBean!=null){
			originalLastModifiedDate=workItemBean.getLastEdit();
		}
		if(commentID!=null){
			//change existing comment
			HistorySaverBL.changeComment(workItemID, person.getObjectID(), locale, commentID, comment,errorList);
		}else{
			//add new comment
			HistorySaverBL.addComment(workItemID,person.getObjectID(), locale, comment, false,errorList,parentID);
		}
		if (errorList!=null && !errorList.isEmpty()) {
			JSONUtility.encodeJSON(ServletActionContext.getResponse(),  JSONUtility.encodeJSONFailure(ErrorHandlerJSONAdapter.handleErrorListAsString(errorList, locale, ",")));
		} else {
			StringBuilder sb=new StringBuilder();
			sb.append("{");
			JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
			JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
			if(originalLastModifiedDate!=null&&lastModifiedDate!=null){
				if(!lastModifiedDate.before(originalLastModifiedDate)){
					workItemBean=ItemBL.loadWorkItemSystemAttributes(workItemID);
					Date lastModified=null;
					if(workItemBean!=null){
						lastModified=workItemBean.getLastEdit();
					}
					JSONUtility.appendStringValue(sb, "lastModified",DateTimeUtils.getInstance().formatISODateTime(lastModified),true);
				}
			}
			sb.append("}}");
			try {
				JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
				PrintWriter out = ServletActionContext.getResponse().getWriter();
				out.println(sb);
			} catch (IOException e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}


		}
		return null;
	}
	
	/**
	 * Deletes the selected comments
	 * @return
	 */
	public String deleteComments(){
		Date originalLastModifiedDate=null;
		Date lastModifiedDate= DateTimeUtils.getInstance().parseISODateTime(lastModified);
		TWorkItemBean workItemBean=ItemBL.loadWorkItemSystemAttributes(workItemID);
		if(workItemBean!=null){
			originalLastModifiedDate=workItemBean.getLastEdit();
		}
		Integer[] commentsToDelete = ItemDetailBL.getIntegerTokens(deletedItems);
		for (int i = 0; i < commentsToDelete.length; i++) {
			HistorySaverBL.deleteComment(workItemID, person.getObjectID(), locale, commentsToDelete[i]);
		}
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
		if(originalLastModifiedDate!=null&&lastModifiedDate!=null){
			if(!lastModifiedDate.before(originalLastModifiedDate)){
				workItemBean=ItemBL.loadWorkItemSystemAttributes(workItemID);
				Date lastModified=null;
				if(workItemBean!=null){
					lastModified=workItemBean.getLastEdit();
				}
				JSONUtility.appendStringValue(sb, "lastModified",DateTimeUtils.getInstance().formatISODateTime(lastModified),true);
			}
		}

		sb.append("}}");
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(sb);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	/**
	 * Gets the list of attachments for the workItem
	 * @return
	 */
	public String doGetAttachments(){
		String sessionID=null;
		List<TAttachmentBean> attachList;
		if(workItemID==null){
			HttpServletRequest request = org.apache.struts2.ServletActionContext.getRequest();
			HttpSession httpSession = request.getSession();
			sessionID=httpSession.getId();
			WorkItemContext workItemContext=((WorkItemContext)session.get("workItemContext"));
			attachList=workItemContext.getAttachmentsList();
			if(attachList!=null && !attachList.isEmpty()){
				for (int i = 0; i < attachList.size(); i++) {
					TAttachmentBean attachmentBean=attachList.get(i);
					if(attachmentBean.getChangedByName()==null||attachmentBean.getChangedByName().length()==0){
						attachmentBean.setChangedByName(person.getFullName());
					}
				}
			}
		}else{
			attachList=AttachBL.getAttachments(workItemID);
		}
		boolean isProjectActive = ProjectBL.projectIsActive(projectID);
		boolean editable=true;
		if(workItemID!=null) {
			TWorkItemBean workItemBean= null;
			try {
				workItemBean = ItemBL.loadWorkItem(workItemID);
				ErrorData editPermissionErrorData = ItemBL.hasEditPermission(person.getObjectID(), workItemBean);
				if (editPermissionErrorData != null) {
					editable = false;
				}
			} catch (ItemLoaderException e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}

		}

		encodeTabJSON(ItemDetailBL.encodeJSON_Attachments(sessionID, attachList,locale, person.getObjectID(),isProjectActive,editable));
		return null;
	}
	

	/**
	 * Gets the selected watchers: consulted and informed persons/groups
	 * @return
	 */
	public String doGetWatcher(){
		ConsInfShow consInfShow = new ConsInfShow();
		if (workItemID==null) {
			WorkItemContext workItemContext=((WorkItemContext)session.get("workItemContext"));
			consInfShow = workItemContext.getConsInfShow();
			if(consInfShow==null){
				consInfShow=new ConsInfShow();
			}
			workItemContext.setConsInfShow(consInfShow);
			List<TPersonBean> consultantPersons = consInfShow.getRealConsultantPersons();
			List<TPersonBean> informantPersons = consInfShow.getRealInformantPersons();
			consInfShow.setAmIConsultant(ConsInfBL.foundMeInRole(consultantPersons, person.getObjectID()));
			consInfShow.setAmIInformant(ConsInfBL.foundMeInRole(informantPersons, person.getObjectID()));
		} else {
			ConsInfBL.loadConsInfFromDb(workItemID, person.getObjectID(), consInfShow);
		}
		String consPrompt=getText("item.tabs.watchers.lbl.header.consultants");
		String infPrompt=getText("item.tabs.watchers.lbl.header.informants");


		encodeTabJSON(ItemDetailBL.encodeJSON_Watchers(consInfShow,person.getObjectID(),consPrompt,infPrompt));
		return null;
	}
	
	/**
	 * Removes the selected watchers
	 * @return
	 */
	public String deleteWatchers(){
		ConsInfShow consInfShow;
		Integer[] selectedConsultantPersons=ItemDetailBL.getIntegerTokens(deletedConsultants);
		Integer[] selectedInformantPersons = ItemDetailBL.getIntegerTokens(deletedInformants);
		Integer[] selectedConsultantGroups=ItemDetailBL.getIntegerTokens(deletedConsultantGroups);
		Integer[] selectedInformantGroups=ItemDetailBL.getIntegerTokens(deletedInformantGroups);
		
		if (workItemID==null) {
			WorkItemContext workItemContext=((WorkItemContext)session.get("workItemContext"));
			consInfShow = workItemContext.getConsInfShow();
			if(consInfShow==null){
				consInfShow=new ConsInfShow();
			}
			workItemContext.setConsInfShow(consInfShow);
			
			//reset me if present and selected
			if (ConsInfBL.foundMeAsSelected(selectedConsultantPersons, person.getObjectID())) {
				consInfShow.setAmIConsultant(false);
			}
			if (ConsInfBL.foundMeAsSelected(selectedInformantPersons, person.getObjectID())) {
				consInfShow.setAmIInformant(false);
			}			
			//remove the selected persons/groups from the session object
			ConsInfBL.deleteSelected(consInfShow.getRealConsultantPersons(), selectedConsultantPersons);
			ConsInfBL.deleteSelected(consInfShow.getRealConsultantGroups(), selectedConsultantGroups);  
			ConsInfBL.deleteSelected(consInfShow.getRealInformantPersons(), selectedInformantPersons);
			ConsInfBL.deleteSelected(consInfShow.getRealInformantGroups(), selectedInformantGroups);
		} else {
			ConsInfBL.deleteByWorkItemAndPersonsAndRaciRole(workItemID, selectedConsultantPersons, RaciRole.CONSULTANT);
			ConsInfBL.deleteByWorkItemAndPersonsAndRaciRole(workItemID, selectedInformantPersons, RaciRole.INFORMANT);
			ConsInfBL.deleteByWorkItemAndPersonsAndRaciRole(workItemID, selectedConsultantGroups, RaciRole.CONSULTANT);
			ConsInfBL.deleteByWorkItemAndPersonsAndRaciRole(workItemID, selectedInformantGroups, RaciRole.INFORMANT);
		}
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(JSONUtility.encodeJSONSuccess());
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	
	/**
	 * Gets the version control texts for the workItem 
	 * @return
	 */
	public String doGetVersionControl(){
		Map<String,RepositoryFileViewer>  mapViewer;
		synchronized(VersionControlMap.class){
			mapViewer=VersionControlMap.getMapViewer();
		}
		List<Revision> revisions= ItemVersionControlBL.getVersionControlRevisions(workItemID);
		encodeTabJSON(ItemVersionControlBL.encodeJSON_versionControl(revisions, mapViewer, locale));
		return null;
	}
	public String itemDetailVersionControl(){
		HttpServletRequest request = org.apache.struts2.ServletActionContext.getRequest();
		String personPath=person.getDesignPath();
		//if(personPath==null){
		personPath="silver";
		//}
		iconsPath= request.getContextPath()+"/design/"+personPath;
		Map<String,RepositoryFileViewer>  mapViewer;
		synchronized(VersionControlMap.class){
			mapViewer=VersionControlMap.getMapViewer();
		}
		revisions= ItemVersionControlBL.getVersionControlRevisionsTO(workItemID,mapViewer,locale);
		return SUCCESS;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	public Map getUpdateMap() {
		return updateMap;
	}
	public void setUpdateMap(Map updateMap) {
		this.updateMap = updateMap;
	}
	public String getDeletedItems() {
		return deletedItems;
	}
	public void setDeletedItems(String deletedItems) {
		this.deletedItems = deletedItems;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getCommentID() {
		return commentID;
	}
	public void setCommentID(Integer commentID) {
		this.commentID = commentID;
	}
	public String getDeletedConsultants() {
		return deletedConsultants;
	}
	public void setDeletedConsultants(String deletedConsultants) {
		this.deletedConsultants = deletedConsultants;
	}
	public String getDeletedInformants() {
		return deletedInformants;
	}
	public void setDeletedInformants(String deletedInformants) {
		this.deletedInformants = deletedInformants;
	}
	public String getDeletedGroupConsultants() {
		return deletedConsultantGroups;
	}
	public void setDeletedGroupConsultants(String deletedGroupConsultants) {
		this.deletedConsultantGroups = deletedGroupConsultants;
	}
	public String getDeletedGroupInformants() {
		return deletedInformantGroups;
	}
	public void setDeletedGroupInformants(String deletedGroupInformants) {
		this.deletedInformantGroups = deletedGroupInformants;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getAttachmentID() {
		return attachmentID;
	}
	public void setAttachmentID(Integer attachmentID) {
		this.attachmentID = attachmentID;
	}
	public String getDeletedConsultantGroups() {
		return deletedConsultantGroups;
	}
	public void setDeletedConsultantGroups(String deletedConsultantGroups) {
		this.deletedConsultantGroups = deletedConsultantGroups;
	}
	public String getDeletedInformantGroups() {
		return deletedInformantGroups;
	}
	public void setDeletedInformantGroups(String deletedInformantGroups) {
		this.deletedInformantGroups = deletedInformantGroups;
	}
	public Integer getGridID() {
		return gridID;
	}
	public void setGridID(Integer gridID) {
		this.gridID = gridID;
	}
	public String getColumns() {
		return columns;
	}
	public void setColumns(String columns) {
		this.columns = columns;
	}

	public Integer getTabID() {
		return tabID;
	}

	public void setTabID(Integer tabID) {
		this.tabID = tabID;
	}

	public Integer getWorkItemID() {
		return workItemID;
	}

	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}

	public Integer getProjectID() {
		return projectID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public boolean isIncludeLayout() {
		return includeLayout;
	}

	public void setIncludeLayout(boolean includeLayout) {
		this.includeLayout = includeLayout;
	}

	public String getIconsPath() {
		return iconsPath;
	}

	public List<RevisionTO> getRevisions() {
		return revisions;
	}

	public Integer getParentID() {
		return parentID;
	}

	public void setParentID(Integer parentID) {
		this.parentID = parentID;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
}
