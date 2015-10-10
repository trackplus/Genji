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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.beans.TScreenBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.errors.ErrorHandlerStruts2Adapter;
import com.aurel.track.errors.ErrorParameter;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.budgetCost.ExpenseBL;
import com.aurel.track.item.history.FlatHistoryBean;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.HTMLSanitiser;
import com.aurel.track.util.TagReplacer;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * 
 * @author Tamas
 *
 */
public class PrintItemAction extends ActionSupport implements Preparable, SessionAware,ApplicationAware{
	private static final long serialVersionUID = 1L;
	private Map<String,Object> session;
	private Locale locale;
	private TPersonBean person;
	private Integer personID;
	private Integer workItemID;
	private String workItemDisplayID;
	private TWorkItemBean workItemBean;
	private WorkItemContext workItemContext;
	private TScreenBean screenBean;

	private String initData=null;

	private List<TWorkItemBean> children;
	private Map<Integer, TStateBean> statusMap;
	private Map<Integer, TPersonBean> personMap;

	private List<FlatHistoryBean> flatHistoryList;
	private String flatValueLabel;
	private Map application;

	private String iconsPath;
	private boolean hideHeader=false;
	private boolean hideChildren=false;
	private boolean hideAttachments=false;
	private boolean hideExpense=false;
	private boolean hideHistory=false;


	/**
	 * prepare the item
	 */
	public void prepare() throws Exception {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		person = (TPersonBean) session.get(Constants.USER_KEY);
		if (person!=null) {
			personID = person.getObjectID();
		}
	}
	
	/**
	 * Render the first screen: wizard or item depending on action
	 * @return
	 */
	@Override
	public String execute(){
		workItemContext=ItemBL.viewWorkItem(workItemID, personID,locale,true);
		if (workItemContext!=null) {
			workItemBean = workItemContext.getWorkItemBean();
		}
		if (workItemBean==null) {
			List<ErrorParameter> errorParameters = new ArrayList<ErrorParameter>();
			ErrorParameter errorParameter = new ErrorParameter(workItemID);
			errorParameters.add(errorParameter);
			ErrorData errorData = new ErrorData("report.reportError.error.noSuchItem", errorParameters);
			ErrorHandlerStruts2Adapter.handleErrorData(errorData, this);
			return ERROR;
		}
		//ErrorData errorData = ItemBL2.hasReadPermission(personID, workItemBean);
		//Check for access permission
		//if (errorData!=null) {
		if (!AccessBeans.isAllowedToRead(workItemBean, personID)) {
			List<ErrorParameter> errorParameters = new ArrayList<ErrorParameter>();
			ErrorParameter errorParameter = new ErrorParameter(ItemBL.getItemNo(workItemBean));
			errorParameters.add(errorParameter);
			ErrorData errorData = new ErrorData("report.reportError.error.noReadRight", errorParameters);
			ErrorHandlerStruts2Adapter.handleErrorData(errorData, this);
			return ERROR;
		}
		workItemDisplayID=obtainWorkItemDisplayID(workItemBean);
		screenBean=ItemBL.loadFullRuntimeScreenBean(workItemContext.getScreenID());
		//initData=prepareInitData(workItemContext);
		return INPUT;
	}

	public String obtainWorkItemDisplayID(TWorkItemBean itemBean) {
		String result=null;
		boolean  useProjectSpecificID=false;
		ApplicationBean appBean =(ApplicationBean)application.get(Constants.APPLICATION_BEAN);
		if(appBean.getSiteBean().getProjectSpecificIDsOn()!=null){
			useProjectSpecificID=appBean.getSiteBean().getProjectSpecificIDsOn().booleanValue();
		}
		if(useProjectSpecificID){
			result= ItemBL.getSpecificProjectID(itemBean);
		}else{
			result=workItemID.toString();
		}
		return result;
	}


	public String getInitData() {
		return initData;
	}

	public String children() {
		children = ItemBL.getChildren(workItemID, personID);
		statusMap = LookupContainer.getStatusMap(locale);
		personMap=LookupContainer.getPersonsMap();
		return INPUT;
	}
	
	public String printItemWithChildren() {
		workItemContext=ItemBL.viewWorkItem(workItemID, personID,locale,true);
		if (workItemContext!=null) {
			workItemBean = workItemContext.getWorkItemBean();
		}
		if (workItemBean==null) {
			List<ErrorParameter> errorParameters = new ArrayList<ErrorParameter>();
			ErrorParameter errorParameter = new ErrorParameter(workItemID);
			errorParameters.add(errorParameter);
			ErrorData errorData = new ErrorData("report.reportError.error.noSuchItem", errorParameters);
			ErrorHandlerStruts2Adapter.handleErrorData(errorData, this);
			return ERROR;
		}
		workItemDisplayID=obtainWorkItemDisplayID(workItemBean);
		flatHistoryList = PrintItemDetailsBL.getAllFlatWithChildren(workItemID, person.getObjectID(), locale);
		screenBean=ItemBL.loadFullRuntimeScreenBean(workItemContext.getScreenID());
		return INPUT;
	}
	
	/**
	 * Gets the flat history and budget history for the current issue
	 * @return
	 */
	public String flatHistory(){
		workItemBean=ItemBL.loadWorkItemSystemAttributes(workItemID);
		flatHistoryList = PrintItemDetailsBL.getFlatHistory(workItemBean, personID, locale);
		flatValueLabel = getText("item.printItem.lbl.tab.commonHistory");
		return SUCCESS;
	}
	public String itemDetailHistory(){
		workItemBean=ItemBL.loadWorkItemSystemAttributes(workItemID);
		HttpServletRequest request = org.apache.struts2.ServletActionContext.getRequest();
		String personPath=person.getDesignPath();
		//if(personPath==null){
		personPath="silver";
		//}
		iconsPath= request.getContextPath()+"/design/"+personPath;
		flatHistoryList = PrintItemDetailsBL.getFlatHistory(workItemBean, personID, locale);
		flatValueLabel = "Flat history values";
		session.put(ItemAction.LAST_SELECTED_ITEMD_DETAIL_TAB, ItemDetailBL.TAB_HISTORY);
		return SUCCESS;
	}
	
	/**
	 * Gets the flat expenses for the current issue
	 * @return
	 */
	public String flatExpense() {  
		workItemBean=ItemBL.loadWorkItemSystemAttributes(workItemID);
		flatHistoryList = ExpenseBL.getFlatExpense(workItemBean, person, locale);
		flatValueLabel = getText(TReportLayoutBean.PSEUDO_COLUMN_LABELS.COST_LIST);
		return SUCCESS;
	}
	
	public String flatAttachment() {
		flatHistoryList = PrintItemDetailsBL.getFlatAttachments(workItemID, locale);
		flatValueLabel = getText("item.printItem.lbl.tab.attachments");
		return SUCCESS;
	}
	 
	public List<FlatHistoryBean> getFlatHistoryList() {
		return flatHistoryList;
	}

	public String getFlatValueLabel() {
		return flatValueLabel;
	}
	
	public String formatDateTime(Date date){
		return DateTimeUtils.getInstance().formatGUIDateTime(date, locale);
	}
	/**
	 * @param info
	 * @return
	 */
	public String formatDescription(String info){
		TagReplacer replacer = new TagReplacer(locale);
		HttpServletRequest request = org.apache.struts2.ServletActionContext.getRequest();
		replacer.setContextPath(request.getContextPath());
		String htmlContent=HTMLSanitiser.stripHTML(info);
		htmlContent = replacer.formatDescription(htmlContent);
		return htmlContent;
	}
	
	/**
	 * Part of header text for existing issues
	 * @return
	 */
	public String getStatusDisplay() {
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(SystemFields.INTEGER_STATE);
		if(workItemBean!=null){
			Object state = workItemBean.getAttribute(SystemFields.INTEGER_STATE);
			return fieldTypeRT.getShowValue(state, locale);
		}
		return "";
	}
	
	/**
	 * Part of header text for existing issues
	 * @return
	 */
	public String getSynopsis() {
		if (workItemBean!=null) {		
			return workItemBean.getSynopsis();
		}
		return "";
	}
	public String getFieldLabel(Integer fieldID){
		TFieldConfigBean cfg= workItemContext.getFieldConfigs().get(fieldID);
		return cfg.getLabel();
	}
	public String getFieldDisplayValue(Integer fieldID){
		Object value=workItemContext.getWorkItemBean().getAttribute(fieldID);
		String displayValue = null;
		boolean  useProjectSpecificID=false;
		ApplicationBean appBean =(ApplicationBean)application.get(Constants.APPLICATION_BEAN);
		if(appBean.getSiteBean().getProjectSpecificIDsOn()!=null){
			useProjectSpecificID=appBean.getSiteBean().getProjectSpecificIDsOn().booleanValue();
		}
		if(useProjectSpecificID&&fieldID.intValue()==SystemFields.ISSUENO){
			displayValue=ItemBL.getSpecificProjectID(workItemContext);
		}else{
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			if (fieldTypeRT!=null) {
				displayValue=fieldTypeRT.getShowValue(value,workItemContext,fieldID);
			}
		}
		if(fieldID==SystemFields.DESCRIPTION){
			displayValue=ItemDetailBL.formatDescription(displayValue,workItemContext.getLocale());
		}
		return displayValue;
	}

	
	public void setSession(Map session) {
		this.session = session;
	}
	
	public Integer getWorkItemID() {
		return workItemID;
	}
	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}
		
	public List<TWorkItemBean> getChildren() {
		return children;
	}

	public Map<Integer, TStateBean> getStatusMap() {
		return statusMap;
	}	

	public String getIssueNoLabel() {
		return FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_ISSUENO, locale);
	}

	public Map getApplication() {
		return application;
	}

	public void setApplication(Map application) {
		this.application = application;
	}

	public String getIconsPath() {
		return iconsPath;
	}

	public void setIconsPath(String iconsPath) {
		this.iconsPath = iconsPath;
	}

	public WorkItemContext getWorkItemContext() {
		return workItemContext;
	}

	public TScreenBean getScreenBean() {
		return screenBean;
	}

	public boolean isHideChildren() {
		return hideChildren;
	}

	public void setHideChildren(boolean hideChildren) {
		this.hideChildren = hideChildren;
	}

	public boolean isHideAttachments() {
		return hideAttachments;
	}

	public void setHideAttachments(boolean hideAttachments) {
		this.hideAttachments = hideAttachments;
	}

	public boolean isHideExpense() {
		return hideExpense;
	}

	public void setHideExpense(boolean hideExpense) {
		this.hideExpense = hideExpense;
	}

	public boolean isHideHistory() {
		return hideHistory;
	}

	public void setHideHistory(boolean hideHistory) {
		this.hideHistory = hideHistory;
	}

	public boolean isHideHeader() {
		return hideHeader;
	}

	public void setHideHeader(boolean hideHeader) {
		this.hideHeader = hideHeader;
	}

	public Map<Integer, TPersonBean> getPersonMap() {
		return personMap;
	}

	public String getWorkItemDisplayID() {
		return workItemDisplayID;
	}
}
