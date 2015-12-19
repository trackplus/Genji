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


package com.aurel.track.item;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import com.aurel.track.beans.*;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.history.HistoryBean;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;

import com.aurel.track.Constants;
import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.role.FieldsRestrictionsToRoleBL;
import com.aurel.track.admin.project.ProjectAccountingTO;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.user.person.PersonPickerJSON;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.item.consInf.ConsInfBL;
import com.aurel.track.item.consInf.ConsInfEdit;
import com.aurel.track.item.consInf.ConsInfShow;
import com.aurel.track.item.consInf.RaciRole;
import com.aurel.track.item.history.FieldChangeBL;
import com.aurel.track.item.history.HistoryValues;
import com.aurel.track.item.link.ItemLinkBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.json.JSONUtility.JSON_FIELDS;
import com.aurel.track.linkType.LinkTypeBL;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.screen.SystemActions;
import com.aurel.track.util.HTMLDiff;
import com.aurel.track.util.HTMLSanitiser;
import com.aurel.track.util.PropertiesHelper;
import com.aurel.track.util.TagReplacer;
import com.aurel.track.util.emailHandling.Html2Text;
import com.aurel.track.vc.Revision;
import com.aurel.track.vc.VersionControlMap;

public class ItemDetailBL {
	public static final int TAB_HISTORY=1;
	public static final int TAB_COMMENTS=2;
	public static final int TAB_ATTACHMENTS=3;
	public static final int TAB_WORKLOG=4;
	public static final int TAB_WATCHERS=5;
	public static final int TAB_VERSION_CONTROL=6;
	public static final int TAB_LINKS=7;
	public static final int TAB_MIGRATIONS=8;
	
	private static final Logger LOGGER = LogManager.getLogger(ItemDetailBL.class);

	private ItemDetailBL(){
	}

	public static void appendGridLayoutData(StringBuilder sb,Integer tabID, Integer personID, Locale locale,boolean last) {
		sb.append("gridLayoutData:{");
		List<ItemGridLayout> layout=ItemGridLayoutBL.loadLayout(tabID,personID, null, locale);
		//String[] sortInfo=ItemGridLayoutBL.getSortInfo(tabID,layout);
		sb.append("layout:").append(ItemDetailBL.encodeJSON_Layout(layout,locale));
		sb.append("}");
		if(!last){
			sb.append(",");
		}
	}
	public static void appendGridLayoutData(StringBuilder sb,Integer tabID, Integer personID, Locale locale) {
		appendGridLayoutData(sb, tabID, personID, locale, false);
	}
	public static String encodeJSON_ItemDetailTabList(List<ItemDetailTab> list){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			ItemDetailTab itemDetailTab;
			for (int i = 0; i < list.size(); i++) {
				itemDetailTab=list.get(i);
				if(i>0){
					sb.append(",");
				}
				sb.append("{");
				JSONUtility.appendStringValue(sb, "extClassName",itemDetailTab.getExtClassName());
				JSONUtility.appendJSONValue(sb, "jsonData", itemDetailTab.getJsonData());
				JSONUtility.appendIntegerValue(sb, "id",itemDetailTab.getId(),true);
				sb.append("}");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	public static String encodeJSON_Layout(List<ItemGridLayout> list,Locale locale){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			ItemGridLayout layout;
			for (int i = 0; i < list.size(); i++) {
				layout=list.get(i);
				if(i>0){
					sb.append(",");
				}
				sb.append("{");
				JSONUtility.appendStringValue(sb, "header",LocalizeUtil.getLocalizedTextFromApplicationResources(layout.getHeader(),locale));
				JSONUtility.appendIntegerValue(sb, "width",layout.getWidth());
				JSONUtility.appendStringValue(sb, "dataIndex",layout.getDataIndex());
				JSONUtility.appendIntegerValue(sb, "id",layout.getId());
				JSONUtility.appendIntegerValue(sb, "type",layout.getType());
				JSONUtility.appendBooleanValue(sb, "sortable",layout.isSortable());
				JSONUtility.appendBooleanValue(sb, "hidden",layout.isHidden(),true);
				sb.append("}");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	public static String encodeJSON_HistoryList(List<HistoryValues> list,boolean historyFull,Locale locale){
		return encodeJSON_HistoryList(list,historyFull,locale,false);
	}
	public static String encodeJSON_HistoryList(List<HistoryValues> list,boolean historyFull,Locale locale,boolean formatted){

		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			HistoryValues history;
			for (int i = 0; i < list.size(); i++) {
				history=list.get(i);
				if(i>0){
					sb.append(",");
				}
				sb.append("{");

				
				String newShowValueFull =history.getNewShowValue();
				String newValueStr;
				if(historyFull){
					newShowValueFull=formatDescription(newShowValueFull,locale);
					newValueStr=newShowValueFull;
				}else{
					newValueStr=extractShortDescription(newShowValueFull);
					newShowValueFull=formatDescription(newShowValueFull,locale);
					newValueStr=formatDescription(newValueStr,locale);
				}
				JSONUtility.appendStringValue(sb, "newValues", newValueStr);
				JSONUtility.appendStringValue(sb, "newValuesFull", newShowValueFull);
				
				String oldShowValueFull = history.getOldShowValue();
				String oldValueStr;
				if(historyFull){
					oldShowValueFull=formatDescription(oldShowValueFull,locale);
					oldValueStr=oldShowValueFull;
				}else{
					oldValueStr=extractShortDescription(oldShowValueFull);
					oldShowValueFull=formatDescription(oldShowValueFull,locale);
					oldValueStr=formatDescription(oldValueStr,locale);
				}

				String commentFull =history.getTransactionComment();
				String commentStr;
				if(historyFull){
					commentFull=formatDescription(commentFull,locale);
					commentStr=commentFull;
				}else{
					commentStr=extractShortDescription(commentFull);
					commentFull=formatDescription(commentFull,locale);
					commentStr=formatDescription(commentStr,locale);
				}

				boolean isText=isTextHistory(history.getFieldID());
				String diffFull = null;
				if(isText){
					diffFull=makeHtmlDif(newShowValueFull,oldShowValueFull,locale);
				}else{
					diffFull=makeSimpleHtmlDif(newShowValueFull,oldShowValueFull);
				}
				String diffStr=null;
				//diffFull=formatDescription(diffFull,locale);
				if(historyFull){
					diffStr=diffFull;
				}else{
					if(isText) {
						diffStr =makeHtmlDif(newValueStr,oldValueStr,locale);
					}else{
						diffStr =makeSimpleHtmlDif(newValueStr,oldValueStr);
					}
				}

				JSONUtility.appendDateTimeValue(sb,"date",history.getLastEdit());
				JSONUtility.appendStringValue(sb, "author", history.getChangedByName());
				JSONUtility.appendStringValue(sb, "authorID", history.getChangedByID()+"");
				JSONUtility.appendStringValue(sb, "typeOfChange", history.getFieldName()+"");

				JSONUtility.appendStringValue(sb, "oldValues", oldValueStr);
				JSONUtility.appendStringValue(sb, "oldValuesFull", oldShowValueFull);

				JSONUtility.appendStringValue(sb, "comment", commentStr);
				JSONUtility.appendStringValue(sb, "commentFull", commentFull);

				JSONUtility.appendStringValue(sb, "diff", diffStr);
				JSONUtility.appendStringValue(sb, "diffFull", diffFull);
				JSONUtility.appendIntegerValue(sb, "id", i, true);
				sb.append("}");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	private static boolean isTextHistory(Integer fieldID){
		boolean result=false;
		if(fieldID==null){
			return false;
		}
		if(fieldID.intValue()== TFieldChangeBean.COMPOUND_HISTORY_FIELD){
			result=true;
		}else {
			if(fieldID>0) {
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldTypeRT != null && (fieldTypeRT.getValueType() == ValueType.TEXT || fieldTypeRT.getValueType() == ValueType.TEXT)) {
					result = true;
				}
			}
		}
		return result;
	}
	private static String makeSimpleHtmlDif(String newValue,String oldValue){
		StringBuilder sb=new StringBuilder();
		if(oldValue!=null&&oldValue.length()>0){
			sb.append("<span class=\"diff-html-removed\">").append(oldValue).append("</span>");
		}
		if(newValue!=null&&newValue.length()>0){
			sb.append(" ").append("<span class=\"diff-html-added\">").append(newValue).append("</span>");
		}
		return sb.toString();
	}
	private static String makeHtmlDif(String newValue,String oldValue,Locale locale){
		String diffStr=null;
		try{
			diffStr=HTMLDiff.makeDiff(newValue,oldValue,locale);
		}catch (Exception ex) {
			LOGGER.info("Making html diff failed with " + ex.getMessage());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("New value " + newValue);
				LOGGER.debug("Old value " + oldValue);
				LOGGER.error(ExceptionUtils.getStackTrace(ex));
			}
		}
		return diffStr;
	}

	public static String encodeJSON_Comments(List<HistoryValues> list, Locale locale) {
		return encodeJSON_Comments(list,locale,null,false,false);
	}
	public static String encodeJSON_Comments(List<HistoryValues> list,
			Locale locale, Integer personID, boolean isProjectActive,
			boolean isProjectAdmin) {
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			HistoryValues comment;
			for (int i = 0; i < list.size(); i++) {
				comment=list.get(i);
				if(i>0){
					sb.append(",");
				}
				sb.append("{");
				JSONUtility.appendDateTimeValue(sb,"date",comment.getLastEdit());
				JSONUtility.appendStringValue(sb, "author", comment.getChangedByName());
				
				JSONUtility.appendStringValue(sb, "authorID", comment.getChangedByID()+"");
				JSONUtility.appendStringValue(sb, "comment", formatDescription(comment.getNewShowValue(),locale));
				if(personID!=null){
					sb.append("\"editable\":").append(editableComment(comment, personID, isProjectActive, isProjectAdmin)).append(",");
				}

				sb.append("\"id\":").append(comment.getObjectID());
				
				sb.append("}");
			}
		}
		sb.append("]");
		return sb.toString();
		
	}
	public static String encodeJSON_Attachments(String sessionID,List<TAttachmentBean> list, Locale locale, Integer personID,boolean isProjectActive,boolean editable) {
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			TAttachmentBean attachBean;
			for (int i = 0; i < list.size(); i++) {
				attachBean=list.get(i);
				if(i>0){
					sb.append(",");
				}
				sb.append("{");
				sb.append("id:").append(attachBean.getObjectID()).append(",");
				JSONUtility.appendStringValue(sb, "fileName", attachBean.getFileName());
				JSONUtility.appendBooleanValue(sb, "isURL", BooleanFields.fromStringToBoolean(attachBean.getIsUrl()));
				JSONUtility.appendStringValue(sb, "fileSize", Long.toString(attachBean.getSize()));
				JSONUtility.appendDateTimeValue(sb,"date",attachBean.getLastEdit());
				JSONUtility.appendStringValue(sb, "author", attachBean.getChangedByName());
				JSONUtility.appendStringValue(sb, "description", attachBean.getDescription());
				
				sb.append("editable:").append(editableAttachemnt(attachBean, personID,isProjectActive,editable)).append(",");
				boolean thumbnail=AttachBL.isImage(attachBean);
				sb.append("thumbnail:").append(thumbnail);
				sb.append("}");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	public static String encodeJSON_AddRole(ConsInfEdit consInfEdit){
		StringBuilder sb=new StringBuilder();
		sb.append("{success:true,");
		sb.append("persons:");
		if(consInfEdit.getPotentialPersons()!=null){
			List<TPersonBean> potentialPersons=consInfEdit.getPotentialPersons();
			sb.append(PersonPickerJSON.encodePersonList(potentialPersons, false));
		}
		sb.append(",");
		sb.append("groups:");
		if(consInfEdit.getPotentialGroups()!=null){
			List<TPersonBean> potentialGroups=consInfEdit.getPotentialGroups();
			sb.append(PersonPickerJSON.encodePersonList(potentialGroups, false));
		}
		sb.append("}");
		return sb.toString();
	}
	public static String encodeJSON_Watchers(ConsInfShow consInfShow,Integer personID,String consPrompt,String infPrompt) {
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		List realConsultantGroups=consInfShow.getRealConsultantGroups();
		List realConsultantPersons=consInfShow.getRealConsultantPersons();
		List realInformantGroups=consInfShow.getRealInformantGroups();
		List realInformantPersons=consInfShow.getRealInformantPersons();

		int realConsultantGroupsSize=realConsultantGroups.size();
		int realConsultantPersonsSize=realConsultantPersons.size();
		int realInformantGroupsSize=realInformantGroups.size();
		int realInformantPersonsSize=realInformantPersons.size();

		boolean hasNext=false;
		if(realConsultantGroupsSize>0){
			sb.append(encodeJSON_ConsInfItem(realConsultantGroups, true, consPrompt,"c",personID));
			hasNext=(realConsultantPersonsSize+realInformantGroupsSize+realInformantPersonsSize>0);
		}
		if(hasNext){
			sb.append(",");
		}

		hasNext=false;
		if(realConsultantPersonsSize>0){
			sb.append(encodeJSON_ConsInfItem(realConsultantPersons, false, consPrompt,"c",personID));
			hasNext=(realInformantGroupsSize+realInformantPersonsSize>0);
		}
		if(hasNext){
			sb.append(",");
		}

		hasNext=false;
		if(realInformantGroupsSize>0){
			sb.append(encodeJSON_ConsInfItem(realInformantGroups, true, infPrompt,"i",personID));
			hasNext=(realInformantPersonsSize>0);
		}
		if(hasNext){
			sb.append(",");
		}

		if(realInformantPersonsSize>0){
			sb.append(encodeJSON_ConsInfItem(realInformantPersons, false, infPrompt,"i",personID));
		}
		sb.append("]");
		return sb.toString();
	}
	private static String encodeJSON_ConsInfItem(List consInfList, boolean isGroup, String type,String raciRole,Integer personID) {
		StringBuilder sb=new StringBuilder();
		for (int i = 0; i < consInfList.size(); i++) {
			TPersonBean person=(TPersonBean) consInfList.get(i);
			
			if(i>0){
				sb.append(",");
			}
			sb.append("{");
			JSONUtility.appendIntegerValue(sb, "objectID", person.getObjectID());
			JSONUtility.appendStringValue(sb, "type", type);
			JSONUtility.appendStringValue(sb, "raciRole", raciRole);
			JSONUtility.appendStringValue(sb, "person",person.getFullName());
			JSONUtility.appendBooleanValue(sb, "isGroup", isGroup);
			JSONUtility.appendBooleanValue(sb, "editable", editableWatcher(person, personID), true);
			sb.append("}");
		}
		return sb.toString();
	}



	
	/**
	 * Get the integer array from the semicolon sparated string  
	 * @param semicolonSeparatedValues
	 * @return
	 */
	public static Integer[] getIntegerTokens(String semicolonSeparatedValues) {
		Integer[] selectedItems=new Integer[0];
		if(semicolonSeparatedValues!=null&&semicolonSeparatedValues.length()>0){
			StringTokenizer st=new StringTokenizer(semicolonSeparatedValues,";");
			List<Integer> intList=new ArrayList<Integer>();
			while(st.hasMoreTokens()){
				String token=st.nextToken();
				try{
					Integer personID=new Integer(token);
					intList.add(personID);
				}catch (Exception e) {
					LOGGER.warn("Converting the semicolon separated value  " + token + " to integer failed with " + e.getMessage());
				}
			}
			selectedItems=new Integer[intList.size()];
			selectedItems=intList.toArray(selectedItems);
		}
		return selectedItems;
	}
	


	private static String extractShortDescription(String descriptionOriginal){
		String description;
		try {
			description = Html2Text.getNewInstance().convert(descriptionOriginal);
		} catch (Exception e) {
			description=descriptionOriginal;
		}
		int maxSizeDescription=120;
		if(description==null||description.length()<=maxSizeDescription){
			return description;
		}
		return description.substring(0,maxSizeDescription)+"...";
	}
	
	private static boolean editableComment(HistoryValues comment, Integer personID, boolean isProjectActive, boolean isProjectAdmin){		
		return isProjectActive && (isProjectAdmin || comment.getChangedByID().intValue()==personID.intValue());
	}
	
	
	private static boolean editableAttachemnt(TAttachmentBean attachBean,
			Integer personID, boolean isProjectActive,boolean editable) {
		return (isProjectActive && editable) || (attachBean.getChangedBy()!=null && attachBean.getChangedBy().equals(personID));
	}
	private static boolean editableExpense(TCostBean costBean, Integer personID, boolean isProjectActive, boolean personIsProjectAdmin) { 
		return isProjectActive && (personIsProjectAdmin || personID.equals(costBean.getChangedByID()));
	}
	
	private static boolean editableWatcher(TPersonBean person, Integer personID) {
		return true;
	}

	public static String replacePattern(String pattern,String path,String revision){
		String s=pattern;//.replaceAll("\\$\\{repo-name\\}", repoName);
		if(path!=null){
			String encodedPath=path.replaceAll("/", "%2F");
			s=s.replaceAll("\\$\\{path\\}", path);
			s=s.replaceAll("\\$\\{encodedPath\\}", encodedPath);
		}
		s=s.replaceAll("\\$\\{rev\\}", revision);
		//s=s.replaceAll("\\$\\{rev-1\\}", revision-1);
		return s;
	}
	public static String formatDescription(String s,Locale locale){
		if (s==null) {
			return null;
		}
		TagReplacer replacer = new TagReplacer(locale);
		replacer.setContextPath(Constants.BaseURL);
		String html=HTMLSanitiser.stripHTML(s);
		return replacer.formatDescription(html);
	}



	/**
	 * Whether the comments tabs content is read only
	 * @return
	 */
	public static boolean isCommentsReadOnly(boolean isProjectActive,boolean  editable) {
		return !isProjectActive || !editable;
	}

	/**
	 * Whether the attachments tabs content is read only
	 * @return
	 */
	public static boolean isAttachmentsReadOnly(boolean isProjectActive,boolean  editable) {
		return !isProjectActive || !editable;
	}
	 /* Enable watcher's add/delete buttons
	 * @return
	 */
	private static boolean isWatcherAddDeleteDisabled(boolean projectIsActive, boolean editable, Map<Integer, Integer> fieldRestrictions) {
		if (!projectIsActive || !editable) {
			return true;
		}
		if (fieldRestrictions!=null && fieldRestrictions.containsKey(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.WATCHERS)) {
			return true;
		}
		return false;
	}

	/**
	 * Whether the me (add/remove) consulted button is disabled
	 * @return
	 */
	public static boolean isWatcherMeConsultedDisabled(TWorkItemBean workItemBean,Integer personID,boolean projectIsActive, boolean editable) {
		if (workItemBean.getObjectID()==null) {
			//new workItem add/remove me as consulted is enabled
			return false;
		}
		//existing workItem
		if (!projectIsActive) {
			return true;
		}
		if (editable) {
			return false;
		}
		if (ConsInfBL.hasDirectRaciRole(workItemBean.getObjectID(), personID, RaciRole.CONSULTANT)) {
			//if he is set as consulted he can remove himself
			return false;
		} else {
			//he is not set as consulted
			return !ConsInfBL.isEnableAddMeConsulted(personID, workItemBean.getProjectID(), workItemBean.getListTypeID());
		}
	}

	/**
	 * Whether the me (add/remove) informed button is disabled
	 * @return
	 */
	public static boolean isWatcherMeInformedDisabled(TWorkItemBean workItemBean,Integer personID,boolean projectIsActive, boolean editable) {
		if (workItemBean.getObjectID()==null) {
			//new workItem add/remove me as informed is enabled
			return false;
		}
		//existing workItem
		if (!projectIsActive) {
			return true;
		}
		if (editable) {
			return false;
		}
		if (ConsInfBL.hasDirectRaciRole(workItemBean.getObjectID(), personID, RaciRole.INFORMANT)) {
			//if he is set as informed he can remove himself
			return false;
		} else {
			return !ConsInfBL.isEnableAddMeInformed(personID, workItemBean.getProjectID(), workItemBean.getListTypeID());
		}
	}

	public static boolean isExpenseReadOnly(boolean projectIsActive,boolean editable) {
		if (!projectIsActive || !editable) {
			return true;
		}
		return false;
	}

	/*public boolean isExpenseAddDisabled(TWorkItemBean workItemBean,Integer personID,boolean projectIsActive, boolean editable) {
		if (!projectIsActive || !editable) {
			return true;
		}
		if (workItemBean!=null) {
			return !AccountingBL.isEnableExpenseAdd(personID, workItemBean.getProjectID(),
					workItemBean.getListTypeID());
		}
		return true;
	}
	public boolean isExpenseEditDeleteDisabled(boolean projectIsActive) {
		if (!projectIsActive) {
			return true;
		}
		return false;
	}*/






	public static void includeItemDetails(ApplicationBean applicationBean, StringBuilder sb,WorkItemContext ctx,TPersonBean personBean,Locale locale, Integer lastSelectedTab) {
		Integer personID=personBean.getObjectID();
		TWorkItemBean workItemBean=ctx.getWorkItemBean();
		if (workItemBean == null) {
			return;  // we cannot do much here anymore
		}
		
		//TProjectBean project=ctx.getProject();
		Integer projectID=workItemBean.getProjectID();
		Integer issueTypeID=workItemBean.getListTypeID();
		Integer activeTab=0;
		sb.append("\"itemDetail\":{");
		Integer workItemID=workItemBean.getObjectID();

		boolean isProjectActive = ItemBL.projectIsActive(projectID);
		boolean editable=true;
		ErrorData editPermissionErrorData = ItemBL.hasEditPermission(personID, workItemBean);
		if (editPermissionErrorData!=null) {
			editable = false;
		}

		Integer versionControlSize=getVersionControlSize(workItemID);
		//tabsEnabled:
		Map<Integer, Integer> fieldRestrictions=ctx.getFieldRestrictions();
		boolean enabledHistory=(workItemID!=null);
		boolean enabledComments=(workItemID!=null&&
				(fieldRestrictions==null || !fieldRestrictions.containsKey(SystemFields.INTEGER_COMMENT)));


		Integer accessFlagAttachment=null;
		if(fieldRestrictions!=null){
			accessFlagAttachment=fieldRestrictions.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ATTACHMENTS);
		}
		boolean readonlyAttachment=false;
		boolean invisibleAttachment=false;
		if(accessFlagAttachment!=null){
			if(accessFlagAttachment.intValue()== TRoleFieldBean.ACCESSFLAG.NOACCESS){
				readonlyAttachment=true;
				invisibleAttachment=true;
			}else{
				invisibleAttachment=false;
				readonlyAttachment = (accessFlagAttachment.intValue() == TRoleFieldBean.ACCESSFLAG.READ_ONLY);
			}
		}
		boolean enabledAttachments=!invisibleAttachment;

		boolean enabledWorklog = false;
		ProjectAccountingTO projectAccountingTO = null;
		if (!ApplicationBean.getInstance().isGenji()) {
			projectAccountingTO = ProjectBL.getProjectAccountingTO(workItemBean.getProjectID());
			if (projectAccountingTO.isWorkTracking() || projectAccountingTO.isCostTracking()) {
				enabledWorklog= AccessBeans.showAccountingTab(personID, projectID, issueTypeID);
			}
		}

		boolean enabledWatchersTab=AccessBeans.watchersTabAllowed(personID, workItemBean.getProjectID(), workItemBean.getListTypeID());
		boolean enabledVersionControl=(workItemID!=null&&versionControlSize>0);
		boolean enabledLinks=isEnableLinks(workItemBean);

		//TODO implement migrations
		boolean enabledMigrations=false;//isEnableMigrations(actionID);
		
		List<ItemDetailTab> itemDetailTabs=new ArrayList<ItemDetailTab>();
		if(enabledHistory){
			ItemDetailTab historyTab = createTabHistory(personBean);
			itemDetailTabs.add(historyTab);
		}
		if(enabledComments){
			ItemDetailTab commentsTab = createTabComments(workItemBean, workItemID, isProjectActive, editable);
			itemDetailTabs.add(commentsTab);
		}
		if(enabledAttachments){
			ItemDetailTab attachmentsTab = createTabAttachment(applicationBean, workItemID, isProjectActive, editable, readonlyAttachment);
			itemDetailTabs.add(attachmentsTab);
		}
		if(enabledWorklog){
			ItemDetailTab worklogTab = createTabWorklog(workItemID, isProjectActive, editable, fieldRestrictions, projectAccountingTO);
			itemDetailTabs.add(worklogTab);
		}
		if(enabledWatchersTab){
			ItemDetailTab watchersTab = createTabWatcher(personID, workItemBean, workItemID, isProjectActive, editable, fieldRestrictions);
			itemDetailTabs.add(watchersTab);
		}
		if(enabledVersionControl){
			ItemDetailTab versionControlTab = createTabVersionControl(personBean);
			itemDetailTabs.add(versionControlTab);
		}
		if(enabledLinks){
			ItemDetailTab linksTab = createTabLinks(workItemID, isProjectActive, editable);
			itemDetailTabs.add(linksTab);
		}
		if(lastSelectedTab!=null){
			for(int i=0;i<itemDetailTabs.size();i++){
				ItemDetailTab tab=itemDetailTabs.get(i);
				if(tab.getId()==lastSelectedTab.intValue()){
					activeTab=i;
					break;
				}
			}
		}
		JSONUtility.appendIntegerValue(sb, "activeTab", activeTab);
		JSONUtility.appendJSONValue(sb, "tabs", ItemDetailBL.encodeJSON_ItemDetailTabList(itemDetailTabs),true);
		sb.append("}");
	}

	private static ItemDetailTab createTabLinks(Integer workItemID, boolean projectActive, boolean editable) {
		ItemDetailTab linksTab=new ItemDetailTab();
		linksTab.setId(ItemDetailBL.TAB_LINKS);
		linksTab.setExtClassName("com.aurel.trackplus.itemDetail.LinksTab");
		StringBuilder jsonData=new StringBuilder();
		jsonData.append("{");

		int linkNumber=0;

		if(workItemID!=null){
			linkNumber= ItemLinkBL.countLinks(workItemID);
		}
		JSONUtility.appendBooleanValue(jsonData, "readOnly", !projectActive || !editable);
		JSONUtility.appendIntegerValue(jsonData, "linkNumber", linkNumber,true);
		jsonData.append("}");
		linksTab.setJsonData(jsonData.toString());
		return linksTab;
	}

	private static ItemDetailTab createTabVersionControl(TPersonBean personBean) {
		ItemDetailTab versionControlTab=new ItemDetailTab();
		versionControlTab.setId(ItemDetailBL.TAB_VERSION_CONTROL);
		versionControlTab.setExtClassName("com.aurel.trackplus.itemDetail.VersionControlTab");
		boolean showFlat;
		String showFlatStr = PropertiesHelper.getProperty(personBean.getMoreProperties(), TPersonBean.SHOW_FLAT_VERSION_CONTROL);
		if(showFlatStr!=null&&showFlatStr.equalsIgnoreCase("true")){
			showFlat=true;
		}else{
			showFlat=false;
		}

		StringBuilder jsonData=new StringBuilder();
		jsonData.append("{");
		JSONUtility.appendBooleanValue(jsonData, "showFlat", showFlat, true);
		jsonData.append("}");
		versionControlTab.setJsonData(jsonData.toString());
		return versionControlTab;
	}

	private static ItemDetailTab createTabWatcher(Integer personID, TWorkItemBean workItemBean, Integer workItemID, boolean projectActive, boolean editable, Map<Integer, Integer> fieldRestrictions) {
		ItemDetailTab watchersTab=new ItemDetailTab();
		watchersTab.setId(ItemDetailBL.TAB_WATCHERS);
		watchersTab.setExtClassName("com.aurel.trackplus.itemDetail.WatchersTab");
		int watchersNumber=0;
		if(workItemID!=null){
			watchersNumber= ConsInfBL.countDirectWatcherPersons(workItemID);
		}
		StringBuilder jsonData=new StringBuilder();
		jsonData.append("{");
		//ItemDetailBL.appendGridLayoutData(jsonData,ItemDetailBL.TAB_WATCHERS,personID,locale);
		JSONUtility.appendBooleanValue(jsonData, "watcherAddDeleteDisabled", isWatcherAddDeleteDisabled(projectActive, editable, fieldRestrictions));
		JSONUtility.appendBooleanValue(jsonData, "watcherMeConsultedDisabled", isWatcherMeConsultedDisabled(workItemBean,personID, projectActive,editable));
		JSONUtility.appendBooleanValue(jsonData, "watcherMeInformedDisabled", isWatcherMeInformedDisabled(workItemBean,personID, projectActive,editable));
		JSONUtility.appendIntegerValue(jsonData, "watchersNumber", watchersNumber);
		JSONUtility.appendIntegerValue(jsonData, "myID", personID, true);

		jsonData.append("}");
		watchersTab.setJsonData(jsonData.toString());
		return watchersTab;
	}

	private static ItemDetailTab createTabComments(TWorkItemBean workItemBean, Integer workItemID, boolean projectActive, boolean editable) {
		ItemDetailTab commentsTab=new ItemDetailTab();
		commentsTab.setId(ItemDetailBL.TAB_COMMENTS);
		commentsTab.setExtClassName("com.aurel.trackplus.itemDetail.CommentsTab");
		int commentNumber=0;

		StringBuilder jsonData=new StringBuilder();
		jsonData.append("{");

		if(workItemID!=null){
			commentNumber= FieldChangeBL.countCommentsByWorkItemID(workItemBean.getObjectID());
		}
		JSONUtility.appendBooleanValue(jsonData, "readOnly", isCommentsReadOnly(projectActive, editable));
		JSONUtility.appendIntegerValue(jsonData, "commentNumber", commentNumber,true);
		jsonData.append("}");
		commentsTab.setJsonData(jsonData.toString());
		return commentsTab;
	}

	private static ItemDetailTab createTabHistory(TPersonBean personBean) {
		ItemDetailTab historyTab=new ItemDetailTab();
		historyTab.setId(ItemDetailBL.TAB_HISTORY);
		historyTab.setExtClassName("com.aurel.trackplus.itemDetail.HistoryTab");

		boolean historyFull;
		String historyFullStr= PropertiesHelper.getProperty(personBean.getMoreProperties(), TPersonBean.HISTORY_FULL);
		if(historyFullStr!=null&&historyFullStr.equalsIgnoreCase("false")){
			historyFull=false;
		}else{
			historyFull=true;
		}
		boolean showCommentsHistory;
		String showCommentsHistoryStr=PropertiesHelper.getProperty(personBean.getMoreProperties(), TPersonBean.SHOW_COMMENTS_HISTORY);
		if(showCommentsHistoryStr!=null&&showCommentsHistoryStr.equalsIgnoreCase("true")){
			showCommentsHistory=true;
		}else{
			showCommentsHistory=false;
		}
		boolean showFlatHistory;
		String showFlatHistoryStr = PropertiesHelper.getProperty(personBean.getMoreProperties(), TPersonBean.SHOW_FLAT_HISTORY);
		if(showFlatHistoryStr!=null&&showFlatHistoryStr.equalsIgnoreCase("true")){
			showFlatHistory=true;
		}else{
			showFlatHistory=false;
		}

		StringBuilder jsonData=new StringBuilder();
		jsonData.append("{");
		JSONUtility.appendBooleanValue(jsonData, "historyFull", historyFull);
		JSONUtility.appendBooleanValue(jsonData, "showCommentsHistory", showCommentsHistory);
		JSONUtility.appendBooleanValue(jsonData, "showFlatHistory", showFlatHistory,true);
		jsonData.append("}");
		historyTab.setJsonData(jsonData.toString());
		return historyTab;
	}

	private static ItemDetailTab createTabWorklog(Integer workItemID, boolean projectActive, boolean editable, Map<Integer, Integer> fieldRestrictions, ProjectAccountingTO projectAccountingTO) {
		ItemDetailTab worklogTab=new ItemDetailTab();
		worklogTab.setId(ItemDetailBL.TAB_WORKLOG);
		worklogTab.setExtClassName("com.aurel.trackplus.itemDetail.WorklogTab");

		boolean readOnly=isExpenseReadOnly(projectActive,editable);
		boolean expenseAddDisabled = false;
		boolean expenseEditDeleteDisabled = false;
		if (readOnly){
			expenseAddDisabled = true;
			expenseEditDeleteDisabled = true;
		} else {
			if (fieldRestrictions!=null && fieldRestrictions.containsKey(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.MY_EXPENSES)) {
				expenseAddDisabled = true;
				expenseEditDeleteDisabled = true;
			} else {
				if (ApplicationBean.getInstance().getSiteBean().getSummaryItemsBehavior()) {
					if (workItemID != null && ItemBL.hasChildren(workItemID)) {
						expenseAddDisabled = true;
						expenseEditDeleteDisabled = true;
					}
				}
			}
		}
		StringBuilder jsonData=new StringBuilder();
		jsonData.append("{");
		JSONUtility.appendBooleanValue(jsonData, "readOnly", readOnly);
		JSONUtility.appendBooleanValue(jsonData, "expenseAddDisabled", expenseAddDisabled);
		JSONUtility.appendBooleanValue(jsonData, "expenseEditDeleteDisabled", expenseEditDeleteDisabled);
		JSONUtility.appendBooleanValue(jsonData, "workTracking", projectAccountingTO.isWorkTracking());
		JSONUtility.appendBooleanValue(jsonData, "costTracking", projectAccountingTO.isCostTracking(), true);
		jsonData.append("}");
		worklogTab.setJsonData(jsonData.toString());
		return worklogTab;
	}

	private static ItemDetailTab createTabAttachment(ApplicationBean applicationBean, Integer workItemID, boolean projectActive, boolean editable, boolean readonlyAttachment) {
		ItemDetailTab attachmentsTab=new ItemDetailTab();
		attachmentsTab.setId(ItemDetailBL.TAB_ATTACHMENTS);
		attachmentsTab.setExtClassName("com.aurel.trackplus.itemDetail.AttachmentsTab");
		StringBuilder jsonData=new StringBuilder();
		jsonData.append("{");
		JSONUtility.appendBooleanValue(jsonData, "readOnly", isAttachmentsReadOnly(projectActive, editable) || readonlyAttachment);
		int attachmentNumber=0;

		if(workItemID!=null){
			attachmentNumber= AttachBL.countByWorkItemID(workItemID);
		}

		JSONUtility.appendIntegerValue(jsonData, "attachmentNumber", attachmentNumber);
		JSONUtility.appendStringValue(jsonData, "maxSize", AttachBL.getMaxAttachmentSizeInMb(applicationBean).toString(), true);
		jsonData.append("}");
		attachmentsTab.setJsonData(jsonData.toString());
		return attachmentsTab;
	}

	private static Integer getVersionControlSize(Integer workItemID){
		Integer versionControlSize;
		if(workItemID==null){
			versionControlSize=Integer.valueOf(0);
		}else{
			Map<Integer,List<Revision>>  revisionsMap= VersionControlMap.getMap();
			List<Revision> revisions;
			if(revisionsMap==null){
				revisions=new ArrayList<Revision>();
			}else{
				revisions= revisionsMap.get(workItemID);
			}
			if(revisions==null||revisions.isEmpty()){
				versionControlSize=Integer.valueOf(0);
			}else{
				versionControlSize=revisions.size();
			}
		}
		return versionControlSize;
	}
	
	/**
	 * Whether the link tab is enabled
	 * @param workItemBean
	 * @return
	 */
	public  static boolean isEnableLinks(TWorkItemBean workItemBean){
		if(workItemBean!=null) {
			Integer projectID = workItemBean.getProjectID();
			if (projectID!=null) {
				TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
				if (projectBean!=null) {
					return LinkTypeBL.isEnableTab(projectBean);
				}
			}
		}
		return false;
	}
	

	public static void encodeTabJSON(String itemsJSON,boolean  includeLayout,Integer tabID,Integer personID,Locale locale) {
		String result;
		if(includeLayout){
			StringBuilder sb=new StringBuilder();
			sb.append("{");
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.SUCCESS, true);
			JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
			List<ItemGridLayout> layout= ItemGridLayoutBL.loadLayout(tabID, personID, null, locale);
			JSONUtility.appendFieldName(sb, "gridLayoutData").append(":{");
			JSONUtility.appendJSONValue(sb, "layout", ItemDetailBL.encodeJSON_Layout(layout, locale), true);
			sb.append("},");
			JSONUtility.appendJSONValue(sb, "items", itemsJSON, true);
			sb.append("}}");
			result=sb.toString();
		}else{
			result=itemsJSON;
		}
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(result);
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}
}
