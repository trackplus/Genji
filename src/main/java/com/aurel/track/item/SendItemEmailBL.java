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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.scripting.GroovyScriptExecuter;
import com.aurel.track.admin.user.department.DepartmentBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TDepartmentBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.history.HistorySaverBL;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.IntegerStringBean;

import freemarker.template.Template;

public class SendItemEmailBL {
	private static final Logger LOGGER = LogManager.getLogger(SendItemEmailBL.class);
	public static final Integer ERROR_NEED_FROM=Integer.valueOf(1);
	public static final Integer ERROR_INVALID_EMAIL=Integer.valueOf(2);
	public static final Integer ERROR_NEED_PERSON=Integer.valueOf(3);
	public static final Integer ERROR_EMAIL_NOT_SEND=Integer.valueOf(4);
	public static final Integer ERROR_NEED_MORE_TIME=Integer.valueOf(5);
	public static final Integer ERROR_NEED_SUBJECT=Integer.valueOf(6);
	public static final Integer ERROR_NEED_BODY=Integer.valueOf(7);

	/**
	 * Gets the first part of the e-mail subject related to an existing item
	 * @param workItemBean
	 * @param locale
	 * @return
	 */
	public static String getMarker(TWorkItemBean workItemBean, Locale locale) {
		Object[] msgArguments = null;
		String messageKey = null;
		if (ApplicationBean.getInstance().getSiteBean().getProjectSpecificIDsOn()) {
			String projectSpecificID = ItemBL.getItemNo(workItemBean);
			msgArguments = new Object[] {projectSpecificID};
			messageKey = "item.mail.subjectStructure.part0ProjectSpecific";
		} else {
			msgArguments = new Object[] {workItemBean.getObjectID().toString()};
			messageKey = "item.mail.subjectStructure.part0";
		}
		return LocalizeUtil.getParametrizedString(messageKey, msgArguments, locale);
	}

	public static List<TPersonBean> validateEmails(String emails,List<TPersonBean> groups){
		if(emails==null||emails.length()==0){
			return new ArrayList<TPersonBean>();
		}
		List<TPersonBean> result=new ArrayList<TPersonBean>();
		StringTokenizer st=new StringTokenizer(emails,";");
		Set<String> emailSet=new HashSet<String>();
		String emailFull;
		String email;
		while (st.hasMoreElements()) {
			emailFull = st.nextToken();
			email=extractEmail(emailFull);
			if(!verifyEmail(email)){
				Integer groupID=isGroup(email,groups);
				if(groupID!=null){
					LOGGER.debug("Include group");
					List<TPersonBean> personsInGroup=PersonBL.loadPersonsForGroup(groupID);
					if(personsInGroup!=null && !personsInGroup.isEmpty()){
						for(TPersonBean p:personsInGroup){
							if(!emailSet.contains(p.getEmail())){
								result.add(p);
								emailSet.add(p.getEmail());
							}
						}
					}
				}else {
					LOGGER.debug("emailFull:" + emailFull + ", email:" + email + " not Valid!");
					return null;
				}
			}else{
				LOGGER.debug("emailFull:"+emailFull+", email:"+email+" is Valid!" );
				if(emailSet.contains(email)){
					LOGGER.debug("Email already present");
				}else{
					TPersonBean person=new TPersonBean("","",email);
					result.add(person);
					emailSet.add(email);
				}
			}
		}
		if(LOGGER.isDebugEnabled()){
			StringBuilder sb=new StringBuilder();
			sb.append("Valid emails:[");
			for(TPersonBean p:result){
				sb.append(p.getEmail()+";");
			}
			sb.append("]");
			LOGGER.debug(sb);
		}
		return result;
	}
	private static String extractEmail(String emailFull){
		int idxStart=emailFull.indexOf("<");
		int idxEnd=emailFull.indexOf(">");
		if(idxStart!=-1&&idxEnd!=-1&&idxEnd>idxStart){
			return  emailFull.substring(idxStart+1,idxEnd);
		}
		return emailFull;
	}
	private static boolean verifyEmail(String email){
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher m = p.matcher(email);
		return m.matches();
	}
	private static Integer isGroup(String groupName,List<TPersonBean> groups){
		if(groups!=null && !groups.isEmpty()){
			for(TPersonBean group:groups){
				if(group.getFullName().equals(groupName.trim())){
					return group.getObjectID();
				}
			}
		}
		return null;
	}
	public static List<IntegerStringBean> getDepartmens(){
		List<IntegerStringBean> result=new ArrayList<IntegerStringBean>();
		List<TDepartmentBean> departmentBeans=DepartmentBL.getAllDepartments();
		if(departmentBeans!=null){
			TDepartmentBean depBean;
			for (int i=0;i<departmentBeans.size();i++){
				depBean=departmentBeans.get(i);
				result.add(new IntegerStringBean(depBean.getLabel(),depBean.getObjectID()));
			}
		}
		return result;
	}
	public static TProjectBean getProject(Integer projectID){
		return LookupContainer.getProjectBean(projectID);
	}
	public static List<IntegerStringBean> getFieldList(WorkItemContext workItemContext,Locale locale){
		List<IntegerStringBean> fieldList=new ArrayList<IntegerStringBean>();
		Map<Integer,TFieldBean> presentFields=workItemContext.getPresentFieldBeans();
		TFieldBean fieldBean;
		for(Map.Entry<Integer,TFieldBean> entry:presentFields.entrySet()) {
			fieldBean = entry.getValue();
			fieldList.add(new IntegerStringBean(fieldBean.getLabel(),fieldBean.getObjectID()));
		}
		Collections.sort(fieldList);
		return fieldList;
	}
	public static void successEmail(Integer itemID,Integer personID, Locale locale,List<TPersonBean> recipients,String subject,String messageBody){
		LOGGER.debug("Send email:\""+subject+"\" succesfully!");
		StringBuffer comment=new StringBuffer();
		//ResourceBundle rb=ResourceBundleManager.getInstance().getResourceBundle(ResourceBundleManager.APPLICATION_RESOURCES_STRUTS2, locale);
		String commentTitle;
		try{
			commentTitle=LocalizeUtil.getLocalizedTextFromApplicationResources("item.action.sendItemEmail.lbl.comment.sendEmailTo", locale);
		}catch (Exception e) {
			commentTitle="An email was send to:";
		}
		comment.append(commentTitle);
		comment.append("<br>");
		comment.append("<ul>");
		String fullName;
		for (int i = 0; i < recipients.size(); i++) {
			TPersonBean person=recipients.get(i);
			if(person.getFirstName()!=null&&person.getFirstName().length()>0){
				fullName=person.getFullName();
			}else{
				fullName=person.getEmail();
			}
			comment.append("<li>").append(fullName).append("</li>");
		}
		comment.append("</ul>");
		comment.append("<br>");
		String subjectLocalized;
		try{
			subjectLocalized=LocalizeUtil.getLocalizedTextFromApplicationResources("item.action.sendItemEmail.lbl.subject", locale);
		}catch (Exception e) {
			subjectLocalized="Subject";
		}
		String messageLocalized;
		try{
			messageLocalized=LocalizeUtil.getLocalizedTextFromApplicationResources("item.action.sendItemEmail.lbl.body", locale);
		}catch (Exception e) {
			messageLocalized="Text";
		}
		comment.append(subjectLocalized).append(": ").append(subject).append("<br>");
		comment.append(messageLocalized).append(":<br>");
		comment.append(messageBody);
		LOGGER.debug("Adding comment to issue "+itemID+":\n"+comment.toString());
		List errorList=new ArrayList();
		HistorySaverBL.addComment(itemID,personID, locale, comment.toString(), true,errorList);
		if(errorList.isEmpty()){
			LOGGER.debug("Comment to issue"+itemID+ "added succesfuly!");
		}else{
			LOGGER.error("Error on adding comment to issue"+itemID);
			for (int i = 0; i < errorList.size(); i++) {
				ErrorData errorData=(ErrorData) errorList.get(i);
				LOGGER.error(errorData.getFieldID()+":"+errorData.getFieldName()+":"+errorData.getResourceKey());
			}
		}
	}
	
	public static String getFieldsBody(WorkItemContext workItemContext, Integer[] fields,Locale locale,boolean isPlain){
		if(fields==null||fields.length==0){
			return "";
		}
		StringBuffer result=new StringBuffer();
		
		Map presentFields=workItemContext.getPresentFieldBeans();
		for (int i = 0; i < fields.length; i++) {
			Integer fieldID=fields[i];
			if(fieldID!=null){
				TFieldBean fieldBean = (TFieldBean) presentFields.get(fieldID);
				if(fieldBean!=null){
					IFieldTypeRT fieldType = FieldTypeManager.getFieldTypeRT(fieldID);
					Object value= workItemContext.getWorkItemBean().getAttribute(fieldID);
					String showValue=fieldType.getShowValue(value, locale);
					result.append(fieldBean.getLabel());
					result.append("=").append(showValue);
					if(isPlain){
						result.append("\n");
					}else{
						result.append("<br>");
					}
				}
			}
		}
		return result.toString();
	}
	
	/**
	 * Execute the hardcoded email guard and activity scripts when no workflow is used
	 * @param workItemContext
	 * @param personBean
	 */
	static void executeHardcodedEmailSendScript(WorkItemContext workItemContext, TPersonBean personBean) {
		LOGGER.debug("Try hardcoded email send script: " + GroovyScriptExecuter.EMAIL_SEND_SCRIPT);
		GroovyScriptExecuter.executeActivityScript(GroovyScriptExecuter.EMAIL_SEND_SCRIPT, workItemContext, personBean);
	}


	public static String getItemInfo(WorkItemContext workItemContext,boolean  useProjectSpecificID,Locale locale) {
		Integer workItemID=workItemContext.getWorkItemBean().getObjectID();
		String infoHtml="";
		TWorkItemBean workItemBean=workItemContext.getWorkItemBean();
		Map<String, Object> root=new HashMap<String, Object>();
		root.put("issueNoLabel", FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_ISSUENO, locale));

		String  statusDisplay="";
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(SystemFields.INTEGER_STATE);
		if(workItemBean!=null){
			Object state = workItemBean.getAttribute(SystemFields.INTEGER_STATE);
			statusDisplay= fieldTypeRT.getShowValue(state, locale);
		}
		root.put("statusDisplay",statusDisplay);
		root.put("synopsis",workItemBean.getSynopsis());
		root.put("workItemID",workItemID);
		String id="";
		if(useProjectSpecificID){
			id= ItemBL.getSpecificProjectID(workItemContext);
		}else{
			id=workItemBean.getObjectID().toString();
		}
		root.put("itemID",id);
		root.put("screenBean",ItemBL.loadFullRuntimeScreenBean(workItemContext.getScreenID()));

		Map<String,String> fieldLabels=new HashMap<String, String>();
		Map<String,String> fieldDisplayValues=new HashMap<String, String>();
		Map<Integer, TFieldConfigBean> mapFieldsConfig=workItemContext.getFieldConfigs();
		Iterator<Integer> it= mapFieldsConfig.keySet().iterator();
		while (it.hasNext()){
			Integer fieldID=it.next();
			TFieldConfigBean cfg=mapFieldsConfig.get(fieldID);
			fieldLabels.put("f_"+fieldID,cfg.getLabel());
			String displayValue= SendItemEmailBL.getFieldDisplayValue(fieldID, workItemContext, useProjectSpecificID);
			fieldDisplayValues.put("f_"+fieldID,displayValue);
		}
		root.put("fieldLabels",fieldLabels);
		root.put("fieldDisplayValues",fieldDisplayValues);
		try{
			URL propURL = ApplicationBean.getApplicationBean().getServletContext().getResource("/WEB-INF/classes/template/printItem.ftl");
			InputStream in = propURL.openStream();
			Template fmtemplate = new Template("name", new InputStreamReader(in));
			StringWriter w = new StringWriter();
			try {
				fmtemplate.process(root, w);
			} catch (Exception e) {
				LOGGER.error("Processing reminder template " + fmtemplate.getName() + " failed with " + e.getMessage(), e);
			}
			w.flush();
			infoHtml= w.toString();
		}catch (Exception ex){
			LOGGER.error(ExceptionUtils.getStackTrace(ex));
		}
		return infoHtml;
	}

	public  static String getFieldDisplayValue(Integer fieldID,WorkItemContext workItemContext,boolean  useProjectSpecificID){
		Object value=workItemContext.getWorkItemBean().getAttribute(fieldID);
		String displayValue = null;
		if(useProjectSpecificID&&fieldID.intValue()== SystemFields.ISSUENO){
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
}
