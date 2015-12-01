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


package com.aurel.track.fieldType.runtime.base;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.item.budgetCost.AccountingForm;
import com.aurel.track.item.consInf.ConsInfShow;
import com.aurel.track.util.emailHandling.EmailAttachment;

/**
 * Transfer object between the controler (action) and the business logic
 * @author Tamas Ruff
 *
 */
public class WorkItemContext {
	
	/**
	 * The currently logged in person
	 */
	private Integer person;
	
	/**
	 * The selected locale 
	 */
	private Locale locale;
	
	/**
	 * The actual workItem to edit/create 
	 */
	private TWorkItemBean workItemBean;
	
	/**
	 * The original workItem used by edit 
	 */
	private TWorkItemBean workItemBeanOriginal;

	/**
	 * The action :new,edit,move
	 */
	private Integer actionID;
	
	/**
	 * The fieldIDs which are present in the screen 
	 */
	private Set<Integer> presentFieldIDs;

	/**
	 * The field beans map for present fields
	 */
	private Map<Integer, TFieldBean> presentFieldBeans;

	/**
	 * datasources for selects
	 */
	private DropDownContainer dropDownContainer;
	
	/**
	 * Map with restrictions on fields
	 * - key: fieldID
	 * - value: the restriction (not readable or read only) 
	 */
	private Map<Integer, Integer> fieldRestrictions;
	
	/**
	 * Map with the best matching fieldConfigs
	 * - key: fieldID
	 * - value TFieldConfigBean 
	 */
	private Map<Integer, TFieldConfigBean> fieldConfigs;
	
	/**
	 * Map with settings 
	 * - key: fieldID and parameterCode combination
	 * - value: TOptionSettingsBean, TTextBoxSettingsBean or TGeneralSettingsBean 
	 */
	private Map<String, Object> fieldSettings;
	
	/**
	 * The screen corresponding to present the workItem 
	 */
	private Integer screenID;
	
	/**
	 * consultants/informants by create item
	 */
	private ConsInfShow consInfShow = new ConsInfShow(); 
	
	/**
	 * costs budgets by create item
	 */
	private AccountingForm accountingForm = new AccountingForm();
	
	/**
	 * The linked items map for a new item
	 */
	private SortedMap<Integer, TWorkItemLinkBean> workItemsLinksMap;
	/**
	 * The list of AttachFileDb objects on create
	 */
	private List<TAttachmentBean> attachmentsList;
	
	/**
	 * The list of File objects attached to the submitted email and the description/comment if it is too long
	 */
	private List<EmailAttachment> emailAttachmentList;
	
	private String sessionID;
	
	/**
	 * The objectID of the fieldChange from the history which will be edited/deleted
	 * when this field is set then most probably only comment editing/deleting takes place 
	 */
	private Integer fieldChangeID;

	private boolean updateLastEdit=true;
	/**
	 * This flag is set when the workItem is created/modified due to a 
	 * import from another data sources (for example another track+ instance)
	 */
	private boolean exchangeImport;
	
	/**
	 * Extra values need for custom actions on edit item. This values will not be persistent in item
	 */
	private Map<String, Object> extraFormValues;


	private boolean useProjectSpecificID;
	
	/**
	 * Context for Groovy activities
	 */
	private Map<String, Object> inputBinding;
	
	private Map<String, List<TWorkItemLinkBean>> successorWorkItemLinksByLinkTypeMap;
	private Map<String, List<TWorkItemLinkBean>> predecessorWorkItemLinksByLinkTypeMap;

	private List<Integer> inlineItems;
	private Integer rootItemID;

	public Integer getFieldChangeID() {
		return fieldChangeID;
	}
	public void setFieldChangeID(Integer fieldChangeID) {
		this.fieldChangeID = fieldChangeID;
	}
	
	public DropDownContainer getDropDownContainer() {
		return dropDownContainer;
	}
	public void setDropDownContainer(DropDownContainer dropDownContainer) {
		this.dropDownContainer = dropDownContainer;
	}
	/*public List<ILabelBean> getPossibleValues(Integer fieldID,Integer parameterCode){
		return getDropDownContainer().getDataSourceList(MergeUtil.mergeKey(fieldID, parameterCode));
	}*/
	public ILabelBean getILabelBean(Integer value,Integer fieldID){
		return getILabelBean(value,fieldID,null);
	}
	public ILabelBean getILabelBean(Integer value,Integer fieldID,Integer parameterCode){
		ILabelBean result=null;
		if(value!=null){
			Map<Integer, ILabelBean> map=getDropDownContainer().getDataSourceMap(MergeUtil.mergeKey(fieldID, parameterCode));
			if(map!=null){
				result=map.get(value);
			}
		}
		return result;
	}
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public Integer getPerson() {
		return person;
	}
	public void setPerson(Integer person) {
		this.person = person;
	}
	public TWorkItemBean getWorkItemBean() {
		return workItemBean;
	}
	public void setWorkItemBean(TWorkItemBean workItemBean) {
		this.workItemBean = workItemBean;
	}
	public Map<Integer, TFieldConfigBean> getFieldConfigs() {
		return fieldConfigs;
	}
	public void setFieldConfigs(Map<Integer, TFieldConfigBean> fieldConfigs) {
		this.fieldConfigs = fieldConfigs;
	}
	public Map<Integer, Integer> getFieldRestrictions() {
		return fieldRestrictions;
	}
	public void setFieldRestrictions(Map<Integer, Integer> fieldRestrictions) {
		this.fieldRestrictions = fieldRestrictions;
	}
	public Map<String, Object> getFieldSettings() {
		return fieldSettings;
	}
	public void setFieldSettings(Map<String, Object> fieldSettings) {
		this.fieldSettings = fieldSettings;
	}
	public Set<Integer> getPresentFieldIDs() {
		return presentFieldIDs;
	}
	public void setPresentFieldIDs(Set<Integer> presentFieldIDs) {
		this.presentFieldIDs = presentFieldIDs;
	}
	public TWorkItemBean getWorkItemBeanOriginal() {
		return workItemBeanOriginal;
	}
	public void setWorkItemBeanOriginal(TWorkItemBean workItemBeanOriginal) {
		this.workItemBeanOriginal = workItemBeanOriginal;
	}

	public Integer getScreenID() {
		return screenID;
	}

	public void setScreenID(Integer screenID) {
		this.screenID = screenID;
	}

	public AccountingForm getAccountingForm() {
		return accountingForm;
	}
	public void setAccountingForm(AccountingForm accountingForm) {
		this.accountingForm = accountingForm;
	}
	public ConsInfShow getConsInfShow() {
		return consInfShow;
	}
	public void setConsInfShow(ConsInfShow consInfShow) {
		this.consInfShow = consInfShow;
	}
	/**
	 * @return the action
	 */
	public Integer getActionID() {
		return actionID;
	}
	/**
	 * @param action the action to set
	 */
	public void setActionID(Integer action) {
		this.actionID = action;
	}
	/**
	 * @return the attachemntsList
	 */
	public List<TAttachmentBean> getAttachmentsList() {
		return attachmentsList;
	}
	/**
	 * @param attachemntsList the attachemntsList to set
	 */
	public void setAttachmentsList(List<TAttachmentBean> attachemntsList) {
		this.attachmentsList = attachemntsList;
	}
	public SortedMap<Integer, TWorkItemLinkBean> getWorkItemsLinksMap() {
		return workItemsLinksMap;
	}
	public void setWorkItemsLinksMap(
			SortedMap<Integer, TWorkItemLinkBean> workItemsLinksMap) {
		this.workItemsLinksMap = workItemsLinksMap;
	}
	/**
	 * @return the sessionID
	 */
	public String getSessionID() {
		return sessionID;
	}
	/**
	 * @param sessionID the sessionID to set
	 */
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}


	public Map<Integer,TFieldBean> getPresentFieldBeans() {
		return presentFieldBeans;
	}

	public void setPresentFieldBeans(Map<Integer,TFieldBean> presentFieldBeans) {
		this.presentFieldBeans = presentFieldBeans;
	}
	/**
	 * @return the emailAttachmentList
	 */
	public List<EmailAttachment> getEmailAttachmentList() {
		return emailAttachmentList;
	}
	/**
	 * @param emailAttachmentList the emailAttachmentList to set
	 */
	public void setEmailAttachmentList(List<EmailAttachment> emailAttachmentList) {
		this.emailAttachmentList = emailAttachmentList;
	}
	public Map<String, Object> getExtraFormValues() {
		return extraFormValues;
	}
	public void setExtraFormValues(Map<String, Object> extraFormValues) {
		this.extraFormValues = extraFormValues;
	}
	public boolean isExchangeImport() {
		return exchangeImport;
	}
	public void setExchangeImport(boolean exchangeImport) {
		this.exchangeImport = exchangeImport;
	}

	public boolean isUseProjectSpecificID() {
		return useProjectSpecificID;
	}

	public void setUseProjectSpecificID(boolean useProjectSpecificID) {
		this.useProjectSpecificID = useProjectSpecificID;
	}

	public boolean isUpdateLastEdit() {
		return updateLastEdit;
	}

	public void setUpdateLastEdit(boolean updateLastEdit) {
		this.updateLastEdit = updateLastEdit;
	}
	public Map<String, Object> getInputBinding() {
		return inputBinding;
	}
	public void setInputBinding(Map<String, Object> inputBinding) {
		this.inputBinding = inputBinding;
	}
	public Map<String, List<TWorkItemLinkBean>> getSuccessorWorkItemLinksByLinkTypeMap() {
		return successorWorkItemLinksByLinkTypeMap;
	}
	public void setSuccessorWorkItemLinksByLinkTypeMap(
			Map<String, List<TWorkItemLinkBean>> successorWorkItemLinksByLinkTypeMap) {
		this.successorWorkItemLinksByLinkTypeMap = successorWorkItemLinksByLinkTypeMap;
	}
	public Map<String, List<TWorkItemLinkBean>> getPredecessorWorkItemLinksByLinkTypeMap() {
		return predecessorWorkItemLinksByLinkTypeMap;
	}
	public void setPredecessorWorkItemLinksByLinkTypeMap(
			Map<String, List<TWorkItemLinkBean>> predecessorWorkItemLinksByLinkTypeMap) {
		this.predecessorWorkItemLinksByLinkTypeMap = predecessorWorkItemLinksByLinkTypeMap;
	}

	public List<Integer> getInlineItems() {
		return inlineItems;
	}

	public void setInlineItems(List<Integer> inlineItems) {
		this.inlineItems = inlineItems;
	}

	public Integer getRootItemID() {
		return rootItemID;
	}

	public void setRootItemID(Integer rootItemID) {
		this.rootItemID = rootItemID;
	}
}
