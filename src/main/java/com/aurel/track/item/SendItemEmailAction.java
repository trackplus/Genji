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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.server.siteConfig.OutgoingEmailBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.PropertiesHelper;
import com.aurel.track.util.StringArrayParameterUtils;
import com.aurel.track.util.emailHandling.Html2Text;
import com.aurel.track.util.emailHandling.JavaMailBean;
import com.aurel.track.util.emailHandling.SMTPMailSettings;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * This class supports sending an information e-mail manually from an item to
 * a number of recipients. A link to the item can be included, and
 * the e-mail text is added to the comment trail.
 *
 */
public class SendItemEmailAction extends ActionSupport implements Preparable, SessionAware,ApplicationAware/*, RequestAware */{
	private static final long serialVersionUID = 1009768020080913361L;

	private static final Logger LOGGER = LogManager.getLogger(SendItemEmailAction.class);

	//session map
	private Map<String,Object> session;

	private Map application;

	private Integer workItemID;
	private Locale locale;
	private TPersonBean personBean;
	private Integer personID;
	private String subject,mailBody;
	private String from;
	private String toCustom;
	private String ccCustom;
	private String bccCustom;
	private List<LabelValueBean> fromAddressList;
	private WorkItemContext workItemContext;
	private String submitterEmail;
	private Boolean includeSubmitterEmail=Boolean.FALSE;
	private Boolean plainText=Boolean.FALSE;
	private Boolean includePDFItem=Boolean.FALSE;
	private Boolean includeItemLink=Boolean.FALSE;
	private Boolean includeItemInformation=Boolean.FALSE;
	private TProjectBean projectBean;
	private List<IntegerStringBean> attachmentsList;
	private String selectedAttachments;
	private String subjectReadolnyPart;
	public void prepare() throws Exception {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		if (personBean==null) {
			return;
		}
		personID = personBean.getObjectID();
		workItemContext=ItemBL.editWorkItem(workItemID, personID,locale,true);
		projectBean=SendItemEmailBL.getProject(workItemContext.getWorkItemBean().getProjectID());
		fromAddressList=new ArrayList<LabelValueBean>();

		String userAddr = personBean.getEmail();
		LabelValueBean addres=new LabelValueBean();
		addres.setLabel(personBean.getFullName()+"<"+userAddr+">");
		addres.setValue(userAddr);
		fromAddressList.add(addres);

		String defaultFrom=userAddr;

		String projectTrackSystemEmail=PropertiesHelper.getProperty(projectBean.getMoreProperties(), TProjectBean.MOREPPROPS.TRACK_EMAIL_PROPERTY);
		//String projectEmailPersonName=PropertiesHelper.getProperty(projectBean.getMoreProps(), TProjectBean.MOREPPROPS.EMAIL_PERSONAL_NAME);
		boolean useTrackFromAddressDisplay="true".equalsIgnoreCase(PropertiesHelper.getProperty(projectBean.getMoreProperties(), TProjectBean.MOREPPROPS.USE_TRACK_FROM_ADDRESS_DISPLAY));
		String projectLocalized= projectBean.getLabel();// FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.PROJECT, locale);
		if(projectTrackSystemEmail!=null&&projectTrackSystemEmail.length()>0){
			//verify address
			try {
				InternetAddress tmp = new InternetAddress(projectTrackSystemEmail);
				LOGGER.debug(tmp.getAddress() + " is valid!");
				addres=new LabelValueBean();
				addres.setLabel(projectLocalized+"<"+projectTrackSystemEmail+">");
				addres.setValue(projectTrackSystemEmail);
				fromAddressList.add(addres);
				if(useTrackFromAddressDisplay){
					defaultFrom=projectTrackSystemEmail;
				}
			} catch (AddressException e) {
				LOGGER.error("The email:'"+projectTrackSystemEmail+"' set on this project is invalid!");
			}
		}
		String emailAddress=ApplicationBean.getInstance().getSiteBean().getSendFrom().getEmail();
		if(emailAddress!=null){
			addres=new LabelValueBean();
			addres.setLabel(ApplicationBean.getInstance().getSiteBean().getSendFrom().getFullName()+"<"+emailAddress+">");
			addres.setValue(emailAddress);
			fromAddressList.add(addres);

			if(!useTrackFromAddressDisplay && ApplicationBean.getInstance().getSiteBean().getUseTrackFromAddressDisplay().equals(TSiteBean.SEND_FROM_MODE.SYSTEM)){
				defaultFrom=emailAddress;
			}
		}
		if(from==null){
			from=defaultFrom;
		}
		submitterEmail=workItemContext.getWorkItemBean().getSubmitterEmail();
		attachmentsList=new ArrayList<IntegerStringBean>();
		List attachListDB=null;
		if(workItemID==null){
			attachListDB=workItemContext.getAttachmentsList();
		}else{
			attachListDB=AttachBL.getAttachments(workItemID);
		}
		if(attachListDB!=null){
			for (int i = 0; i < attachListDB.size(); i++) {
				TAttachmentBean attachmentBean=(TAttachmentBean) attachListDB.get(i);
				attachmentsList.add(new IntegerStringBean(attachmentBean.getFileName(),attachmentBean.getObjectID()));
			}
		}
		String part0 = SendItemEmailBL.getMarker(workItemContext.getWorkItemBean(), locale);
		/*String part0 = LocalizeUtil.getParametrizedString("item.mail.subjectStructure.part0",
			new Object[] {workItemID}, locale);*/
		subjectReadolnyPart=part0+"["+projectBean.getLabel()+"]";
	}
	public void setSession(Map<String, Object> sess) {
		this.session=sess;
	}

	@Override
	public String execute() throws Exception {
		LOGGER.debug("SendItemMailAction.execute() for workItemID="+workItemID);
		subject=workItemContext.getWorkItemBean().getSynopsis();

		TSiteBean siteBean = ApplicationBean.getApplicationBean().getSiteBean();
		SMTPMailSettings smtpMailSettings= OutgoingEmailBL.getSMTPMailSettings(siteBean);
		if (smtpMailSettings.getHost() == null || "".equals(smtpMailSettings.getHost().trim() ) ) {
			//It's acceptable that there is no SMTP server :)
			LOGGER.info("No SMTP host found, e-mail sending is impossible");
			return encodeFailure("No SMTP host found, e-mail sending is impossible",SendItemEmailBL.ERROR_EMAIL_NOT_SEND);
		}

		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendLabelValueBeanList(sb, "fromAddressList", fromAddressList);
		JSONUtility.appendStringValue(sb,"submitterEmail",submitterEmail);
		JSONUtility.appendStringValue(sb,"from",from);
		JSONUtility.appendIntegerStringBeanList(sb,"attachmentsList",attachmentsList);
		JSONUtility.appendStringValue(sb,"subjectReadolnyPart",subjectReadolnyPart);
		JSONUtility.appendStringValue(sb,"subject",subject,true);
		sb.append("}}");
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), sb.toString());
		return null;
	}

	/**
	 * Send a notification e-mail.
	 * @return
	 */
	public String sendEmail(){
		LOGGER.debug("SendItemMailAction.sendMail() for workItemID="+workItemID);
		if(LOGGER.isDebugEnabled()){
			if(selectedAttachments==null){
				LOGGER.debug("No attachments");
			}else{
				LOGGER.debug("Selected Attachments:"+selectedAttachments);
			}
		}
		if(from==null||from.length()==0){
			return encodeFailure(getText("item.action.sendItemEmail.err.needFrom"),SendItemEmailBL.ERROR_NEED_FROM);
		}
		List<TPersonBean> groups= PersonBL.loadGroups();
		List<TPersonBean> recipients=new ArrayList<TPersonBean>();
		if(submitterEmail!=null&&includeSubmitterEmail!=null&&includeSubmitterEmail.booleanValue()){
			TPersonBean person=new TPersonBean("","",submitterEmail);
			recipients.add(person);
		}
		List<TPersonBean> customToPersons=SendItemEmailBL.validateEmails(toCustom,groups);
		if(customToPersons==null){
			return encodeFailure(getText("item.action.sendItemEmail.err.invalidEmail"),SendItemEmailBL.ERROR_INVALID_EMAIL);
		}
		recipients.addAll(customToPersons);

		if(recipients.isEmpty()){
			return encodeFailure(getText("item.action.sendItemEmail.err.needPerson"),SendItemEmailBL.ERROR_NEED_PERSON);
		}
		List<TPersonBean> recipientsCC=SendItemEmailBL.validateEmails(ccCustom,groups);
		List<TPersonBean> recipientsBCC=SendItemEmailBL.validateEmails(bccCustom, groups);

		TPersonBean addressFrom=new TPersonBean();
		//check the from address
		//1 current user
		String userAddr = personBean.getEmail();
		if(userAddr.equals(from)){
			addressFrom.setFirstName(personBean.getFirstName());
			addressFrom.setLastName(personBean.getLastName());
		}else{
			//2. project email
			String projectTrackSystemEmail=PropertiesHelper.getProperty(projectBean.getMoreProperties(), TProjectBean.MOREPPROPS.TRACK_EMAIL_PROPERTY);
			if(from.equals(projectTrackSystemEmail)){
				String projectLocalized=projectBean.getLabel(); //FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.PROJECT, locale);
				addressFrom.setFirstName(projectLocalized);
			}else{
				//3.system email
				String emailAddress=ApplicationBean.getInstance().getSiteBean().getSendFrom().getEmail();
				if(from.equals(emailAddress)) {
					addressFrom.setFirstName(ApplicationBean.getInstance().getSiteBean().getSendFrom().getFullName());
				}
			}
		}
		addressFrom.setEmail(from);

		Integer itemID=workItemContext.getWorkItemBean().getObjectID();
		//String itemTitle=workItemContext.getWorkItemBean().getSynopsis();

		boolean isPlain=false;

		String mailBodyPlain=mailBody;
		try{
			mailBodyPlain=Html2Text.getNewInstance().convert(mailBody);
		}catch (Exception e) {
			mailBodyPlain=mailBody;
		}
		String originalMailBody=mailBody;
		LOGGER.debug("includeItemLink="+includeItemLink+" includeItemInformation="+includeItemInformation);
		if(includeItemInformation!=null&&includeItemInformation.booleanValue()){
			boolean  useProjectSpecificID=false;
			ApplicationBean appBean =(ApplicationBean)application.get(Constants.APPLICATION_BEAN);
			if(appBean.getSiteBean().getProjectSpecificIDsOn()!=null){
				useProjectSpecificID=appBean.getSiteBean().getProjectSpecificIDsOn().booleanValue();
			}
			String infoHtml = SendItemEmailBL.getItemInfo(workItemContext,useProjectSpecificID,locale);
			String infoPlain="";
			try{
				infoPlain=Html2Text.getNewInstance().convert(infoHtml);
			}catch (Exception ex){}
			mailBodyPlain+= "\n\n"+infoPlain;
			mailBody+="<BR/>"+infoHtml;
		}
		if(includeItemLink!=null&&includeItemLink.booleanValue()){
			String moreInfo=Constants.Hyperlink + itemID;
			mailBodyPlain+= "\n\n"+getText("item.action.sendItemEmail.lbl.forFurtherInformation")+""+moreInfo;
			mailBody+="<p>"+getText("item.action.sendItemEmail.lbl.forFurtherInformation")+"<a href=\""+moreInfo+"\">"+moreInfo+"</a></p>";
		}

		boolean emailSent=false;

		String messageTitle=getMessageTitle(workItemContext.getWorkItemBean(),projectBean.getLabel(),locale,subject==null?"":subject);
		List<LabelValueBean> attachments=findAttachments();
		LOGGER.debug("sending email: messageTitle="+messageTitle);
		LOGGER.debug("messageBodies plain:\n"+mailBodyPlain+"\n\n");
		LOGGER.debug("messageBodies html:\n"+mailBody+"\n\n");



		emailSent=JavaMailBean.getInstance().sendMail(recipients,messageTitle,addressFrom,mailBody,isPlain,attachments,recipientsCC, recipientsBCC);
		if(emailSent){
			try{
				SendItemEmailBL.successEmail(itemID,personID,locale,recipients,subject,originalMailBody);
			}catch (Exception e){
				LOGGER.error("Fail to add comment mail send.");
			}
			SendItemEmailBL.executeHardcodedEmailSendScript(workItemContext, personBean);
			return encodeSuccess();
		}else{
			return encodeFailure("Email not sent!",SendItemEmailBL.ERROR_EMAIL_NOT_SEND);
		}
	}

	private String encodeSuccess(){
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(JSONUtility.encodeJSONSuccess());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	private String encodeFailure(String error,Integer errorCode){
		JSONUtility.encodeJSONFailure(ServletActionContext.getResponse(),error,errorCode);
		return null;
	}

	private List<LabelValueBean> findAttachments(){
		List<LabelValueBean> attachments=new ArrayList<LabelValueBean>();
		List attachListDB=null;
		if(workItemID==null){
			attachListDB=workItemContext.getAttachmentsList();
		}else{
			attachListDB=AttachBL.getAttachments(workItemID);
		}
		if(attachListDB!=null && !attachListDB.isEmpty()){
			if(selectedAttachments!=null){
				List<Integer> attachemtIDList= StringArrayParameterUtils.splitSelectionAsInteger(selectedAttachments);
				for (Integer attachID:attachemtIDList){
					LabelValueBean lvb=findAttachment(attachListDB, attachID);
					if(lvb!=null){
						attachments.add(lvb);
					}
				}
			}
		}
		return attachments;
	}

	private LabelValueBean findAttachment(List attachListDB,Integer attachID){
		for (int i = 0; i < attachListDB.size(); i++) {
			TAttachmentBean attachmentBean=(TAttachmentBean) attachListDB.get(i);
			if(attachmentBean.getObjectID().equals(attachID)){
				return new LabelValueBean(attachmentBean.getFileName(),attachmentBean.getFullFileNameOnDisk());
			}
		}
		return null;
	}
	//TODO getEmailTitle
	//put this in a common place
	private String getMessageTitle(TWorkItemBean workItemBean, String projectLabel, Locale locale,String subjectSuffix){
		//this will be parsed at a possible replay
		/*String part0 = LocalizeUtil.getParametrizedString("item.mail.subjectStructure.part0",
				new Object[] {itemID}, locale);*/
		String part0 = SendItemEmailBL.getMarker(workItemBean, locale);
		String part3 = LocalizeUtil.getLocalizedTextFromApplicationResources("item.mail.subjectStructure.part3", locale);
		Object[] subjectStructure;
		if (part3==null || "".equals(part3.trim())) {
			subjectStructure = new Object[] { part0, "["+projectLabel+"] ", subjectSuffix };
		} else {
			subjectStructure = new Object[] { part0, "["+projectLabel+"] ", subjectSuffix, part3 };
		}
		return LocalizeUtil.getParametrizedString("item.mail.subjectStructure", subjectStructure, locale);
	}

	private TPersonBean findPerson(Integer personID,List<TPersonBean> personBeanList){
		if(personID==null){
			return null;
		}
		for (int i = 0; i < personBeanList.size(); i++) {
			if(personBeanList.get(i).getObjectID().intValue()==personID.intValue()){
				return personBeanList.get(i);
			}
		}

		return null;
	}
	public Integer getWorkItemID() {
		return workItemID;
	}
	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMailBody() {
		return mailBody;
	}
	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
	}

	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}

	public String getToCustom() {
		return toCustom;
	}
	public void setToCustom(String toCustom) {
		this.toCustom = toCustom;
	}
	public List<LabelValueBean> getFromAddressList() {
		return fromAddressList;
	}
	public void setFromAddressList(List<LabelValueBean> fromAddressList) {
		this.fromAddressList = fromAddressList;
	}
	public String getSubmitterEmail() {
		return submitterEmail;
	}
	public void setSubmitterEmail(String submitterEmail) {
		this.submitterEmail = submitterEmail;
	}
	public Boolean getIncludeSubmitterEmail() {
		return includeSubmitterEmail;
	}
	public void setIncludeSubmitterEmail(Boolean includeSubmitterEmail) {
		this.includeSubmitterEmail = includeSubmitterEmail;
	}
	public Boolean getPlainText() {
		return plainText;
	}
	public void setPlainText(Boolean plainText) {
		this.plainText = plainText;
	}
	public Boolean getIncludePDFItem() {
		return includePDFItem;
	}
	public void setIncludePDFItem(Boolean includePDFItem) {
		this.includePDFItem = includePDFItem;
	}
	public Boolean getIncludeItemLink() {
		return includeItemLink;
	}
	public void setIncludeItemLink(Boolean includeItemLink) {
		this.includeItemLink = includeItemLink;
	}

	public Boolean getIncludeItemInformation() {
		return includeItemInformation;
	}

	public void setSelectedAttachments(String selectedAttachments) {
		this.selectedAttachments = selectedAttachments;
	}

	public void setBccCustom(String bccCustom) {
		this.bccCustom = bccCustom;
	}

	public void setCcCustom(String ccCustom) {
		this.ccCustom = ccCustom;
	}

	public void setIncludeItemInformation(Boolean includeItemInformation) {
		this.includeItemInformation = includeItemInformation;
	}

	public Map getApplication() {
		return application;
	}

	public void setApplication(Map application) {
		this.application = application;
	}
}
