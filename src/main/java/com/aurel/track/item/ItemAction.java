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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TScreenBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.errors.ErrorHandlerJSONAdapter;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.FieldsManagerRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.item.action.IPluginItemAction;
import com.aurel.track.item.action.ItemActionUtil;
import com.aurel.track.item.action.PluginItemActionException;
import com.aurel.track.item.lock.ItemLockBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.mobile.MobileBL;
import com.aurel.track.plugin.ItemActionDescription;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.screen.SystemActions;
import com.aurel.track.screen.item.bl.runtime.ItemScreenRuntimeJSON;
import com.aurel.track.toolbar.ToolbarItem;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.LabelValueBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * @author adib
 *
 */
public class ItemAction extends ActionSupport implements Preparable, SessionAware, ApplicationAware/*, RequestAware */{
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LogManager.getLogger(ItemAction.class);

	public static final String FORWARD_TO_BROWS_PROJECTS = "browseProjects";
	public static final String LAST_SELECTED_ITEMD_DETAIL_TAB="lastSelectedItemDetailTab";


	//session map
	private Map<String,Object> session;
	//private Map request;
	private Locale locale;
	private TPersonBean tPerson;
	private Integer personID;

	//the id of the item
	private Integer key;
	private Integer workItemID;
	private boolean anonym;

	//for new item
	private Integer parentID;
	private Integer projectID;
	private Integer issueTypeID;
	private Integer releaseID;
	private String synopsis;
	private String description;

	//forward to a given action after edit item
	private String forwardAction;

	private Integer actionID;


	private Map<String,Object> params;
	TPersonBean personBeanPermLink;
	Locale localePermLink;

	/* Links */
	private String linkTypeWithDirection;
	private Integer linkedWorkItemID;
	private Map<Integer, String> parametersMap;

	private Map application;


	private boolean hasInitData=true;
	private String initData;
	private String layoutCls="com.trackplus.layout.PrintItemLayout";
	private String pageTitle="item.view.title";

	public void setSession(Map<String, Object> session) {
		this.session=session;
	}

	/**
	 * prepare the item
	 */
	public void prepare() throws Exception {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		tPerson = (TPersonBean) session.get(Constants.USER_KEY);
		if (tPerson!=null) {
			personID = tPerson.getObjectID();
		}else{
			personBeanPermLink=(TPersonBean)session.get(Constants.USER_KEY+"-permLink");
			localePermLink = (Locale) session.get(Constants.LOCALE_KEY+"-permLink");
		}
		if(workItemID==null){
			workItemID=key;
		}
		if(actionID==null){
			actionID=SystemActions.EDIT;
		}
	}

	/**
	 * Render the first screen: wizard or item depending on action
	 * @return
	 */
	@Override
	public String execute(){
		if(tPerson==null){
			return "logon";
		}
		if(SystemActions.PRINT==actionID.intValue()){
			return printItem();
		}
		return INPUT;
	}
	public String richTextPreview(){
		return SUCCESS;
	}
	/**
	 * Go to the next page (from wizard page to item page)
	 * @return
	 */
	public String next(){
		if(tPerson==null){
			encodeResponseLogon();
			return null;
		}
		ItemActionDescription itemPluginDescriptor=ItemActionUtil.getDescriptor(actionID.toString());
		IPluginItemAction itemActionPlugin=null;
		if(itemPluginDescriptor.getTheClassName()!=null){
			itemActionPlugin=ItemActionUtil.getPlugin(itemPluginDescriptor.getTheClassName());
		}
		if (itemActionPlugin == null) {
			return null;
		}
		StringBuilder sb=new StringBuilder();


		sb.append("{");
		try {
			if(params==null){
				params=new HashMap<String, Object>();
			}
			//In case of Add linked item (Item navigator context menu):
			//The following code block handle link config from client (form parameters) and
			//puts into params map as a HashMap
			Map<String, Object>newlyCreatedLinkSettings = new HashMap<String, Object>();
			newlyCreatedLinkSettings.put("linkedWorkItemID", linkedWorkItemID);
			newlyCreatedLinkSettings.put("linkTypeWithDirection", linkTypeWithDirection);
			newlyCreatedLinkSettings.put("parametersMap", parametersMap);
			newlyCreatedLinkSettings.put("description", description);
			params.put("newlyCreatedLinkSettings", newlyCreatedLinkSettings);
			//ends of handling link submit params

			WorkItemContext workItemContext=itemActionPlugin.next(locale, tPerson, workItemID, parentID, params, synopsis, description);



			boolean isNew = ItemBL.itemIsNew(actionID);
			LOGGER.debug("Next: isNew="+isNew);
			if(isNew){
				session.put("workItemContext", workItemContext);
			} else {
				if (ItemBL.isMove(actionID)) {
					//TODO send as hidden parameter
					TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
					if (workItemBean!=null) {
						session.put("moveChildren" + workItemBean.getObjectID(), workItemContext.getWorkItemBean().isMoveChildren());
					}
				}
			}
			JSONUtility.appendBooleanValue(sb, "success", true);
			sb.append("\"data\":");
			boolean readOnlyMode=false;
			TScreenBean screenBean=ItemBL.loadFullRuntimeScreenBean(workItemContext.getScreenID());
			sb.append(encodeCtx(itemPluginDescriptor, workItemContext,screenBean,actionID, readOnlyMode,false));
		} catch (PluginItemActionException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			JSONUtility.appendBooleanValue(sb, "success", false);
			JSONUtility.appendStringValue(sb, "fieldName", e.getFieldName());
			JSONUtility.appendStringValue(sb, "message", e.getMessage());
			JSONUtility.appendStringValue(sb, "localizedErrorKey", e.getLocalizedErrorKey(),true);
		}
		sb.append("}");
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(sb);
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	public String checkUserRightForCreatingNewItem() {
		boolean userCanCreateNewItem = false;
		if(projectID != null && issueTypeID != null) {
			userCanCreateNewItem = AccessBeans.isAllowedToCreate(personID, projectID, issueTypeID);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, "userCanCreateNewItem", userCanCreateNewItem, true);
		sb.append("}");
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), sb.toString());
		return null;

	}


	public String executeAJAX(){
		boolean lite=false;
		if(tPerson==null){
			if(personBeanPermLink==null){
				encodeResponseLogon();
				return null;
			}else{
				tPerson=personBeanPermLink;
				locale=localePermLink;
				lite=true;
			}
		}
		ItemActionDescription itemPluginDescriptor=ItemActionUtil.getDescriptor(actionID.toString());
		IPluginItemAction itemActionPlugin=null;
		if(itemPluginDescriptor.getTheClassName()!=null){
			itemActionPlugin=ItemActionUtil.getPlugin(itemPluginDescriptor.getTheClassName());
		}
		if (itemActionPlugin == null) {
			return null;
		}
		boolean isNew = ItemBL.itemIsNew(actionID);

		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb,"success",true);
		sb.append("\"data\":");
		String title=getText(itemPluginDescriptor.getTitle());
		boolean useWizard=itemPluginDescriptor.isUseWizard();
		boolean forceNextStep=false;
		if(SystemActions.NEW==actionID.intValue()&&projectID!=null&&issueTypeID!=null){
			forceNextStep=true;
		}
		if(useWizard&&forceNextStep==false){
			String jsonData=null;
			try{
				jsonData=itemActionPlugin.encodeJsonDataStep1(locale,tPerson, workItemID, parentID, projectID, issueTypeID, synopsis, description);
			}catch (PluginItemActionException ex){
				LOGGER.error("Error execute item action:"+ex.getMessage());
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug("executeAJAX: actionID="+actionID+", workItemID="+workItemID+", projectID="+projectID+",issueTypeID="+projectID);
					LOGGER.error(ExceptionUtils.getStackTrace(ex));
				}
				return encodeItemException(ex);
			}
			sb.append("{");
			String firstPageTemplate=itemPluginDescriptor.getFirstPageTemplate();
			boolean canFinish=itemActionPlugin.canFinishInFirstStep();
			String finishLabel=getText(itemPluginDescriptor.getFinishLabelKey());
			JSONUtility.appendBooleanValue(sb, "useWizard", true);
			JSONUtility.appendStringValue(sb, "title1","<span class=\"itemTitle\">"+getText(itemPluginDescriptor.getTitle1())+"</span>");
			JSONUtility.appendStringValue(sb, "title","<span class=\"itemTitle\">"+title+"</span>");
			JSONUtility.appendBooleanValue(sb, "useWizard", true);
			JSONUtility.appendBooleanValue(sb, "canFinish", canFinish);
			JSONUtility.appendStringValue(sb, "cssClass", itemPluginDescriptor.getCssClass());
			JSONUtility.appendStringValue(sb, "extClassName", firstPageTemplate);
			JSONUtility.appendBooleanValue(sb, "canFinish", canFinish);
			JSONUtility.appendStringValue(sb, "finishLabel", finishLabel);
			JSONUtility.appendFieldName(sb, "jsonData").append(":").append(jsonData);
			//sb.append("jsonData:").append(jsonData);
			sb.append("}");
		}else{
			Boolean accessLevelFlag = Boolean.FALSE;
			Integer stateID=null;
			boolean dropDownsNeeded=true;
			WorkItemContext workItemContext=ItemBL.getWorkItemContext(actionID, workItemID, projectID, issueTypeID, stateID, accessLevelFlag, tPerson.getObjectID(), locale,dropDownsNeeded);
			if(isNew){
				if(releaseID!=null){
					workItemContext.getWorkItemBean().setReleaseScheduledID(releaseID);
				}
				if(parentID!=null){
					workItemContext.getWorkItemBean().setSuperiorworkitem(parentID);
				}
				if(synopsis!=null){
					workItemContext.getWorkItemBean().setSynopsis(synopsis);
				}
				if(description!=null){
					workItemContext.getWorkItemBean().setDescription(description);
				}
				session.put("workItemContext", workItemContext);
			}else{
				TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
				if (workItemBean==null) {
					List<Integer> params=new LinkedList<Integer>();
					params.add(workItemID);
					return encodeError(getText("report.reportError.error.noSuchItem", params));
				}
				if(SystemActions.PRINT==actionID.intValue()){
					if (!AccessBeans.isAllowedToRead(workItemBean, personID)) {
						List<String> params=new LinkedList<String>();
						params.add(ItemBL.getItemNo(workItemBean));
						return encodeError(getText("report.reportError.error.noReadRight", params));
					}
				}else{
					if (!AccessBeans.isAllowedToChange(workItemBean, personID)) {
						List<String> params=new LinkedList<String>();
						params.add(ItemBL.getItemNo(workItemBean));
						return encodeError(getText("report.reportError.error.noEditRight", params));
					}
				}

			}
			boolean readOnlyMode=false;
			if (actionID==SystemActions.PRINT){
				readOnlyMode=true;
			}

			TScreenBean screenBean=ItemBL.loadFullRuntimeScreenBean(workItemContext.getScreenID());
			sb.append(encodeCtx(itemPluginDescriptor, workItemContext,screenBean,actionID, readOnlyMode,lite));
		}
		sb.append("}");
		encodeResponse(sb);
		return null;
	}
	public String reloadIssueTypes(){
		ItemLocationForm itemLocationForm=ItemBL.getItemLocation(locale, tPerson, projectID, issueTypeID, workItemID);
		String s=ItemActionJSON.encodeJSON_IssueLocation(itemLocationForm);
		encodeResponse(s);
		return null;
	}
	private void encodeResponse(StringBuilder sb){
		encodeResponse(sb.toString());
	}
	private void encodeResponseLogon(){
		JSONUtility.encodeJSONFailure(ServletActionContext.getResponse(),
				LocalizeUtil.getLocalizedTextFromApplicationResources("common.noLoggedUser", locale),
				JSONUtility.ERROR_CODE_NO_USER_LOGIN);
	}
	private void encodeResponse(String s){
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}

	public String toolbarEdit(){
		ApplicationBean appBean =(ApplicationBean)application.get(Constants.APPLICATION_BEAN);
		StringBuilder sb=new StringBuilder();
		TWorkItemBean workItem=null;
		try{
			workItem=ItemBL.loadWorkItem(workItemID);

		}catch (Exception ex){
			LOGGER.error(ex);
		}
		if(workItem==null){
			List<LabelValueBean> errors=new ArrayList<LabelValueBean>();
			errors.add(new LabelValueBean(getText("report.reportError.error.noSuchItem",new String[]{workItemID+""}),"1"));
			JSONUtility.encodeJSONErrorsExtJS(ServletActionContext.getResponse(),errors);
			return null;
		}
		List<ToolbarItem> toolbarItemsList=ToolbarBL.getEditToolbars(appBean,personID,workItem,forwardAction,actionID);
		sb.append("{");
		JSONUtility.appendBooleanValue(sb,JSONUtility.JSON_FIELDS.SUCCESS,true);
		JSONUtility.appendJSONValue(sb,"data",JSONUtility.encodeJSONToolbarItemsList(toolbarItemsList),true);
		sb.append("}");
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
	 * print item
	 * @return
	 */
	public String printItem(){
		actionID=SystemActions.PRINT;
		if(workItemID==null){
			workItemID=key;
		}
		//load the item context
		WorkItemContext workItemContext=ItemBL.viewWorkItem(workItemID, personID,locale,true);
		TWorkItemBean workItemBean=workItemContext.getWorkItemBean();
		if (workItemBean==null) {
			//item does not exists
			StringBuilder sb=new StringBuilder();
			sb.append("{");
			List<Integer> params=new LinkedList<Integer>();
			params.add(workItemID);
			JSONUtility.appendStringValue(sb,"error",getText("report.reportError.error.noSuchItem", params),true);
			sb.append("}");
			initData=sb.toString();
			return INPUT;
		}
		//check for readPermission
		//ErrorData errorData = ItemBL2.hasReadPermission(personID, workItemBean);
		//Check for access permission
		//if (errorData!=null) {
		if (!AccessBeans.isAllowedToRead(workItemBean, personID)) {
			StringBuilder sb=new StringBuilder();
			sb.append("{");
			List<String> params=new LinkedList<String>();
			params.add(ItemBL.getItemNo(workItemBean));
			JSONUtility.appendStringValue(sb,"error",getText("report.reportError.error.noReadRight", params),true);
			sb.append("}");
			//ErrorHandlerStruts2Adapter.handleErrorData(errorData, this);
			initData=sb.toString();
			return INPUT;
		}
		/*ErrorData editPermissionErrorData = ItemBL2.hasEditPermission(personID, workItemBean);
		if (editPermissionErrorData!=null) {
			//editable = false;
		}*/
		/*ErrorData disabledErrorData = ItemBL2.isEnabled(personID, workItemBean);
		if (disabledErrorData!=null) {
			//enabledToChange = false;
		}*/

		// Check for access permission for toolbar
		projectID=workItemBean.getProjectID();
		issueTypeID=workItemBean.getListTypeID();

		List<Integer> fieldIDs = new ArrayList<Integer>();
		fieldIDs.add(SystemFields.INTEGER_ISSUENO);
		fieldIDs.add(SystemFields.INTEGER_STATE);
		fieldIDs.add(SystemFields.INTEGER_PROJECT);
		fieldIDs.add(SystemFields.INTEGER_RESPONSIBLE);
		fieldIDs.add(SystemFields.INTEGER_SYNOPSIS);
		TScreenBean screenBean=ItemBL.loadFullRuntimeScreenBean(workItemContext.getScreenID());
		ItemActionDescription itemPluginDescriptor=ItemActionUtil.getDescriptor(actionID.toString());
		initData=encodeCtx(itemPluginDescriptor,workItemContext,screenBean,actionID, true,false);

		return INPUT;
	}

	/**
	 * Saves the issue in the wizard page without going through the second page
	 * @return
	 */
	public String saveInFirstStep(){
		ItemActionDescription itemPluginDescriptor=ItemActionUtil.getDescriptor(actionID.toString());
		IPluginItemAction itemActionPlugin=null;
		if(itemPluginDescriptor.getTheClassName()!=null){
			itemActionPlugin=ItemActionUtil.getPlugin(itemPluginDescriptor.getTheClassName());
		}
		if (itemActionPlugin == null) {
			return null;
		}
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		try {
			if(params==null){
				params=new HashMap<String, Object>();
			}
			Integer newWorkItemID=itemActionPlugin.saveInFirsStep(locale, tPerson, workItemID, params);
			JSONUtility.appendBooleanValue(sb, "success", true);
			sb.append("data:{");
			JSONUtility.appendIntegerValue(sb, "newWorkItemID", newWorkItemID);
			JSONUtility.appendIntegerValue(sb, "workItemID", newWorkItemID,true);
			sb.append("}");
		} catch (PluginItemActionException e) {
			LOGGER.error("Error saving item in first step:"+e.getMessage(), e);
			if(LOGGER.isDebugEnabled()){
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
			return encodeItemException(e);
		}
		sb.append("}");
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
	private String encodeError(String e){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb, "message", e);
		JSONUtility.appendBooleanValue(sb, "success", false,true);
		sb.append("}");
		encodeResponse(sb);
		return null;
	}
	private String encodeItemException(PluginItemActionException e){
		StringBuilder sb=new StringBuilder();
		sb.append("{");

		JSONUtility.appendStringValue(sb, "fieldName", e.getFieldName());
		JSONUtility.appendStringValue(sb, "message", e.getMessage());
		JSONUtility.appendStringValue(sb, "localizedErrorKey", e.getLocalizedErrorKey());
		JSONUtility.appendStringValue(sb, "localizedParam", e.getLocalizedParam());
		JSONUtility.appendBooleanValue(sb, "success", false,true);
		sb.append("}");
		encodeResponse(sb);
		return null;
	}


	private String  encodeCtx(ItemActionDescription itemPluginDescriptor, WorkItemContext workItemContext,TScreenBean screenBean,Integer actionID,boolean readOnlyMode,boolean lite){
		StringBuilder sb=new StringBuilder();
		sb.append("{");

		ApplicationBean appBean =(ApplicationBean)application.get(Constants.APPLICATION_BEAN);
		boolean  useProjectSpecificID=false;
		if(appBean.getSiteBean().getProjectSpecificIDsOn()!=null){
			useProjectSpecificID=appBean.getSiteBean().getProjectSpecificIDsOn().booleanValue();
		}
		workItemContext.setUseProjectSpecificID(useProjectSpecificID);
		List<ToolbarItem> toolbarItemsList=null;
		TWorkItemBean workItem=workItemContext.getWorkItemBean();
		if(actionID.intValue()==SystemActions.PRINT){
			toolbarItemsList=ToolbarBL.getPrintItemToolbar(appBean,personID,workItemContext,forwardAction);
		}else{
			toolbarItemsList=ToolbarBL.getEditToolbars(appBean,personID,workItem,forwardAction,actionID);
		}
		boolean isNew = ItemBL.itemIsNew(actionID);
		List<TWorkItemBean> children = new ArrayList<TWorkItemBean>();
		if(!isNew) {
			children=ItemBL.getChildren(workItemID, personID);
		}
		if(lite==false){
			JSONUtility.appendJSONValue(sb,"toolbar",JSONUtility.encodeJSONToolbarItemsList(toolbarItemsList));
		}
		if (actionID==SystemActions.EDIT || actionID==SystemActions.CHANGE_STATUS || actionID==SystemActions.MOVE) {
			ErrorData errorData = ItemLockBL.lockItem(workItemID, tPerson, ServletActionContext.getRequest().getSession().getId());
			if (errorData!=null) {
				String lockingProblem = ErrorHandlerJSONAdapter.createMessage(errorData, locale);
				JSONUtility.appendStringValue(sb, "itemLockedMessage", lockingProblem);
			}
		}
		TProjectBean projectBean = LookupContainer.getProjectBean(workItem.getProjectID());
		boolean allowedToChange = AccessBeans.isAllowedToChange(workItem,personID);
		boolean personInlineEdit=tPerson.isPrintItemEditable();
		boolean inlineEdit=allowedToChange&&personInlineEdit;
		JSONUtility.appendBooleanValue(sb, "inlineEdit", inlineEdit);
		JSONUtility.appendBooleanValue(sb, "readOnlyMode", readOnlyMode);
		JSONUtility.appendIntegerValue(sb, "actionID", actionID);
		JSONUtility.appendIntegerValue(sb, "workItemID", workItem.getObjectID());
		JSONUtility.appendIntegerValue(sb, "projectID", workItem.getProjectID());
		if (projectBean!=null) {
			JSONUtility.appendStringValue(sb, "projectLabel", projectBean.getLabel());
		}
		JSONUtility.appendIntegerValue(sb, "issueTypeID", workItem.getListTypeID());
		JSONUtility.appendStringValue(sb, "issueTypeLabel", ItemBL.getIssueTypeDisplay(workItemContext));
		JSONUtility.appendStringValue(sb, "synopsis", workItem.getSynopsis());

		JSONUtility.appendBooleanValue(sb, "synopsisReadonly", ItemBL.isSynopsysReadonly(workItemContext));
		//JSONUtility.appendStringValue(sb, "issueNoLabel", FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_ISSUENO, locale));
	    String workItemIDDisplay=null;
		if(workItem.getObjectID()!=null){
			if(useProjectSpecificID){
				workItemIDDisplay=ItemBL.getSpecificProjectID(workItemContext);
			}else{
				workItemIDDisplay=workItem.getObjectID().toString();
			}
		}
		JSONUtility.appendStringValue(sb, "workItemIDDisplay",workItemIDDisplay);
		JSONUtility.appendIntegerValue(sb, "statusID",workItem.getStateID());
		JSONUtility.appendStringValue(sb, "statusDisplay", StatusBL.getStatusDisplay(workItem.getStateID(), locale));
		String title=getText(itemPluginDescriptor.getTitle());
		JSONUtility.appendStringValue(sb, "title","<span class=\"itemTitle\">"+title+"</span>" + ItemBL.formatIssueTitle(workItemContext,useProjectSpecificID));
		JSONUtility.appendStringValue(sb, "lastModified",DateTimeUtils.getInstance().formatISODateTime(workItemContext.getWorkItemBean().getLastEdit()));
		JSONUtility.appendJSONValue(sb, "screen",new ItemScreenRuntimeJSON().encodeScreen(screenBean));
		JSONUtility.appendJSONValue(sb, "workItemContext",ItemActionJSON.encodeContext(workItemContext,locale, useProjectSpecificID,tPerson, MobileBL.isMobileApp(session)));
		JSONUtility.appendJSONValue(sb, "children",ItemActionJSON.encodeJSON_Children(children,locale,useProjectSpecificID));
		JSONUtility.appendBooleanValue(sb, "lite", lite);
		//include bottom part
		if(itemPluginDescriptor.isUseBottomTabs()&&lite==false){
			JSONUtility.appendBooleanValue(sb, "includeBottomTabs", true);
			Integer lastSelectedTab=null;
			if(itemPluginDescriptor.getPreselectedTab()!=null){
				lastSelectedTab=itemPluginDescriptor.getPreselectedTab();
			}else {
				lastSelectedTab=(Integer) session.get(LAST_SELECTED_ITEMD_DETAIL_TAB);
			}
			ItemDetailBL.includeItemDetails(appBean,sb,workItemContext,tPerson,locale,lastSelectedTab);
		}else{
			JSONUtility.appendBooleanValue(sb, "includeBottomTabs", false,true);
		}
		sb.append("}");
		return sb.toString();
	}

	public String loadChildren(){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		sb.append("data:{");

		ApplicationBean appBean =(ApplicationBean)application.get(Constants.APPLICATION_BEAN);
		boolean  useProjectSpecificID=false;
		if(appBean.getSiteBean().getProjectSpecificIDsOn()!=null){
			useProjectSpecificID=appBean.getSiteBean().getProjectSpecificIDsOn().booleanValue();
		}
		List<TWorkItemBean> children = ItemBL.getChildren(workItemID, personID);
		JSONUtility.appendJSONValue(sb, "children",ItemActionJSON.encodeJSON_Children(children,locale,useProjectSpecificID));
		sb.append("}}");
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), sb.toString());
		return null;
	}

	public String setParent() {
		TWorkItemBean workItemBean= null;
		try {
			workItemBean = ItemBL.loadWorkItem(workItemID);
		} catch (ItemLoaderException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		if (workItemBean!=null) {
			List<ErrorData> errorList = new LinkedList<ErrorData>();
			workItemID= FieldsManagerRT.saveOneField(personID, workItemBean.getObjectID(), locale, false,
					SystemFields.INTEGER_SUPERIORWORKITEM, parentID, true, null, errorList);
			if (!errorList.isEmpty()) {
				LOGGER.debug("There are errors on setParent: "+errorList.size()+" errors");
				String errorMessage = ErrorHandlerJSONAdapter.handleErrorListAsString(errorList, locale, ", ");
				LOGGER.warn("Error on setParent:"+errorMessage);
				JSONUtility.encodeJSON(ServletActionContext.getResponse(), JSONUtility.encodeJSONFailure(errorMessage));
			}else{
				JSONUtility.encodeJSON(ServletActionContext.getResponse(), JSONUtility.encodeJSONSuccess());
			}
		}
		return null;
	}

	public String reverseAccessFlag(){
		if(workItemID==null){
			JSONUtility.encodeJSONFailure("No item id.");
			return null;
		}
		TWorkItemBean workItemBean = ItemBL.loadWorkItemSystemAttributes(workItemID);
		List<ErrorData> errorList = new LinkedList<ErrorData>();
		Integer intAccesLevel = null;
		boolean newAcceslevel = !workItemBean.isAccessLevelFlag();
		if (newAcceslevel) {
			intAccesLevel = TWorkItemBean.ACCESS_LEVEL_PRIVATE;
		}
		FieldsManagerRT.saveOneField(personID, workItemID, locale, false,
				SystemFields.INTEGER_ACCESSLEVEL, intAccesLevel, false, null, errorList);
		if (!errorList.isEmpty()) {
			String errorMessage = ErrorHandlerJSONAdapter.handleErrorListAsString(errorList, locale, ", ");
			JSONUtility.encodeJSON(ServletActionContext.getResponse(), JSONUtility.encodeJSONFailure(errorMessage));
		}else{
			JSONUtility.encodeJSON(ServletActionContext.getResponse(), JSONUtility.encodeJSONSuccess());
		}
		return null;
	}
	public String reverseArchive(){
		if(workItemID==null){
			JSONUtility.encodeJSONFailure("No item id.");
			return null;
		}
		TWorkItemBean workItemBean = ItemBL.loadWorkItemSystemAttributes(workItemID);
		List<ErrorData> errorList = new LinkedList<ErrorData>();
		Integer intArchiveLevel = null;
		boolean newArchivelevel = !workItemBean.isArchived();
		if (newArchivelevel) {
			intArchiveLevel = TWorkItemBean.ARCHIVE_LEVEL_ARCHIVED;
		}
		FieldsManagerRT.saveOneField(personID, workItemID, locale, false,
				SystemFields.INTEGER_ARCHIVELEVEL, intArchiveLevel, false, null, errorList);
		if (!errorList.isEmpty()) {
			String errorMessage = ErrorHandlerJSONAdapter.handleErrorListAsString(errorList, locale, ", ");
			JSONUtility.encodeJSON(ServletActionContext.getResponse(), JSONUtility.encodeJSONFailure(errorMessage));
		}else{
			JSONUtility.encodeJSON(ServletActionContext.getResponse(), JSONUtility.encodeJSONSuccess());
		}
		return null;
	}
	public String reverseDelete(){
		if(workItemID==null){
			JSONUtility.encodeJSONFailure("No item id.");
			return null;
		}
		TWorkItemBean workItemBean = ItemBL.loadWorkItemSystemAttributes(workItemID);
		List<ErrorData> errorList = new LinkedList<ErrorData>();
		Integer intDeleteLevel = null;
		boolean newDeletelevel = !workItemBean.isDeleted();
		if (newDeletelevel) {
			intDeleteLevel = TWorkItemBean.ARCHIVE_LEVEL_DELETED;
		}
		FieldsManagerRT.saveOneField(personID, workItemID, locale, false,
				SystemFields.INTEGER_ARCHIVELEVEL, intDeleteLevel, false, null, errorList);
		if (!errorList.isEmpty()) {
			String errorMessage = ErrorHandlerJSONAdapter.handleErrorListAsString(errorList, locale, ", ");
			JSONUtility.encodeJSON(ServletActionContext.getResponse(), JSONUtility.encodeJSONFailure(errorMessage));
		}else{
			JSONUtility.encodeJSON(ServletActionContext.getResponse(), JSONUtility.encodeJSONSuccess());
		}
		return null;
	}
	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public Integer getWorkItemID() {
		return workItemID;
	}

	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}

	public boolean isAnonym() {
		return anonym;
	}

	public void setAnonym(boolean anonym) {
		this.anonym = anonym;
	}

	public Integer getParentID() {
		return parentID;
	}

	public void setParentID(Integer parentID) {
		this.parentID = parentID;
	}

	public Integer getProjectID() {
		return projectID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public Integer getIssueTypeID() {
		return issueTypeID;
	}

	public void setIssueTypeID(Integer issueTypeID) {
		this.issueTypeID = issueTypeID;
	}

	public String getForwardAction() {
		return forwardAction;
	}

	public void setForwardAction(String forwardAction) {
		this.forwardAction = forwardAction;
	}

	public Integer getActionID() {
		return actionID;
	}

	public void setActionID(Integer actionID) {
		this.actionID = actionID;
	}

	public boolean isHasInitData() {
		return hasInitData;
	}

	public String getInitData() {
		return initData;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public void setReleaseID(Integer releaseID) {
		this.releaseID = releaseID;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public String getLinkTypeWithDirection() {
		return linkTypeWithDirection;
	}

	public void setLinkTypeWithDirection(String linkTypeWithDirection) {
		this.linkTypeWithDirection = linkTypeWithDirection;
	}

	public Integer getLinkedWorkItemID() {
		return linkedWorkItemID;
	}

	public void setLinkedWorkItemID(Integer linkedWorkItemID) {
		this.linkedWorkItemID = linkedWorkItemID;
	}

	public Map getApplication() {
		return application;
	}

	public void setApplication(Map application) {
		this.application = application;
	}

	public Map<Integer, String> getParametersMap() {
		return parametersMap;
	}

	public void setParametersMap(Map<Integer, String> parametersMap) {
		this.parametersMap = parametersMap;
	}

	public String getLayoutCls() {
		return layoutCls;
	}

	public String getPageTitle() {
		return pageTitle;
	}
}
