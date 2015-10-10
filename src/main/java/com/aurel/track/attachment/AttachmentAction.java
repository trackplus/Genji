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


package com.aurel.track.attachment;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.cluster.ClusterBL.CHANGE_TYPE;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemDetailBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.item.history.HistorySaverBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.lucene.index.associatedFields.AttachmentIndexer;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.DetectBrowser;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.PropertiesHelper;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class AttachmentAction extends ActionSupport implements Preparable,SessionAware,ServletRequestAware,ApplicationAware{

	private static final long serialVersionUID = 340L;
	private static final Logger LOGGER = LogManager.getLogger(AttachmentAction.class);

	private HttpServletRequest servletRequest;
	private Map<String, Object> session;
	private String description;
	//protected FormFile theFile = null;
	private String theFile;
	private Integer workItemID;
	private Integer attachKey;
	private String openerRefreshURL;
	private List<TAttachmentBean> attachments;
	private Integer personID;
	private Map application;
	private Locale locale;
	private String deletedItems;
	private String lastModified;

	private String theUrl;
	public String getTheUrl() {
		return theUrl;
	}

	public void setTheUrl(String theUrl) {
		this.theUrl = theUrl;
	}

	Integer attachmentID;


    public void prepare() throws Exception {
    	locale = (Locale) session.get(Constants.LOCALE_KEY);
		/*if(workItemID==null){
			workItemID=new Integer(-1);
		}*/
		TPersonBean person=((TPersonBean) session.get(Constants.USER_KEY));
		if(person!=null){
			personID=person.getObjectID();
		}
	}

	/**
	 * Edits an attachment's description
	 * @return
	 */
	public String edit(){
		TAttachmentBean attachmentBean;
		Date originalLastModifiedDate=null;
		Date lastModifiedDate= DateTimeUtils.getInstance().parseISODateTime(lastModified);
		if (workItemID==null) {
			WorkItemContext workItemContext=((WorkItemContext)session.get("workItemContext"));
			List<TAttachmentBean> localAttachments=workItemContext.getAttachmentsList();
			if(localAttachments==null){
				localAttachments=new ArrayList<TAttachmentBean>();
			}
			attachmentBean=AttachBL.loadLocalAttachment(attachmentID, localAttachments);
			if (attachmentBean!=null) {
				attachmentBean.setDescription(description);
			}
		} else {
			attachmentBean=AttachBL.loadAttachment(attachmentID, workItemID,false);
			if(attachmentBean!=null){
				TWorkItemBean workItemBean=ItemBL.loadWorkItemSystemAttributes(workItemID);
				originalLastModifiedDate=workItemBean.getLastEdit();
				String newFileNameDescription = HistorySaverBL.getAttachmentHistoryText(attachmentBean.getFileName(), description, null, locale, BooleanFields.fromStringToBoolean(attachmentBean.getIsUrl()));
				String oldFileNameDescription = HistorySaverBL.getAttachmentHistoryText(attachmentBean.getFileName(), attachmentBean.getDescription(), null, locale, BooleanFields.fromStringToBoolean(attachmentBean.getIsUrl()));
				HistorySaverBL.editAttachment(workItemID, personID, locale, newFileNameDescription, oldFileNameDescription);
				attachmentBean.setDescription(description);
				attachmentBean.setLastEdit(new Date());
				AttachBL.save(attachmentBean);
				attachmentBean.setFullFileNameOnDisk(AttachBL.getFullFileName(null,attachmentID, workItemID));
				attachmentBean.setFileNameOnDisk(AttachBL.getFileNameAttachment(attachmentID, workItemID));
				AttachmentIndexer.getInstance().addToIndex(attachmentBean, false);
				//possible lucene update in other cluster nodes
				ClusterMarkChangesBL.markDirtyAttachmentInCluster(attachmentID, CHANGE_TYPE.UPDATE_INDEX);
			}
		}
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
		if(originalLastModifiedDate!=null&&lastModifiedDate!=null){
			if(!lastModifiedDate.before(originalLastModifiedDate)){
				TWorkItemBean workItemBean=ItemBL.loadWorkItemSystemAttributes(workItemID);
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

	public String save(){
		MultiPartRequestWrapper wrapper=(MultiPartRequestWrapper)servletRequest;
		HttpServletResponse response=ServletActionContext.getResponse();
		//response.setHeader("Content-Type", "text/html");
		//response.setContentType("text/html");
		if (wrapper.hasErrors()) {
			StringBuffer sb=new StringBuffer();
			Collection errors = wrapper.getErrors();
			Iterator i = errors.iterator();
			while (i.hasNext()) {
				sb.append((String) i.next()).append("</br>");
			}
			if(!errors.isEmpty()){
				JSONUtility.encodeJSONFailure(response, sb.toString(),null,false);
				return null;
			}
		}
		if(wrapper.getFiles("theFile")==null){
			List<LabelValueBean> errors=new ArrayList<LabelValueBean>();
			errors.add(new LabelValueBean(getText("common.err.required",new String[]{getText("report.export.manager.upload.file")}),"theFile"));
			JSONUtility.encodeJSONErrorsExtJS(response, errors,false);
			return null;
		}
		File[] files=wrapper.getFiles("theFile");
		String[] fileNames=wrapper.getFileNames("theFile");
		//theFile=fileName;
		if(files == null || files.length==0||fileNames==null || fileNames.length==0){
			String err=getText("common.err.required",new String[]{getText("report.export.manager.upload.file")});
			addFieldError("theFile",err );
			List<LabelValueBean> errors=new ArrayList<LabelValueBean>();
			errors.add(new LabelValueBean(err,"theFile"));
			JSONUtility.encodeJSONErrorsExtJS(response, errors,false);
			return null;
		}

		ApplicationBean applicationBean=(ApplicationBean)application.get(Constants.APPLICATION_BEAN);
		Double maxAttachmentSizeInMb=AttachBL.getMaxAttachmentSizeInMb(applicationBean);
		int MAXFILESIZE = AttachBL.getMaxFileSize(applicationBean);
		int maxDescriptionSize=ApplicationBean.getInstance().getDescriptionMaxLength();
		if(description!=null&&description.length()>maxDescriptionSize){
			List<LabelValueBean> errors=new ArrayList<LabelValueBean>();
			errors.add(new LabelValueBean(getText("item.err.tooLong",new String[]{getText("common.lbl.description"),Integer.toString(maxDescriptionSize)}),"description"));
			JSONUtility.encodeJSONErrorsExtJS(response, errors,false);
			return null;
		}

		Date originalLastModifiedDate=null;
		Date lastModifiedDate= DateTimeUtils.getInstance().parseISODateTime(lastModified);
		TWorkItemBean workItemBean=ItemBL.loadWorkItemSystemAttributes(workItemID);
		if(workItemBean!=null){
			originalLastModifiedDate=workItemBean.getLastEdit();
		}
		List<LabelValueBean> errors=new ArrayList<LabelValueBean>();
		for(int i=0;i<files.length;i++){
			if (AttachBL.storeFile(workItemID, personID, description, locale, session, response, files[i], fileNames[i], maxAttachmentSizeInMb, MAXFILESIZE,errors,true)==null){
				break;
			}
		}
		if(!errors.isEmpty()){
			JSONUtility.encodeJSONErrorsExtJS(response, errors, false);
			return null;
		}

		description=null;


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
			//Client side ie9 and ie10 does not support application/json when the request is submitted as: 	form.submit({... (The callback is not called!),
			//This is why needed to return response as text, despite this is a JSON. For other browsers does not matter!
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse(), false);
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(sb);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}



	/**
	 * This method save an attachment as a link.
	 * @return
	 */
	public String saveLink() {
		HttpServletResponse response=ServletActionContext.getResponse();
		try {

			Date originalLastModifiedDate=null;
			Date lastModifiedDate= DateTimeUtils.getInstance().parseISODateTime(lastModified);
			TWorkItemBean workItemBean=ItemBL.loadWorkItemSystemAttributes(workItemID);
			if(workItemBean!=null){
				originalLastModifiedDate=workItemBean.getLastEdit();
			}

			AttachBL.saveAttachmentAsLink(workItemID, description, theUrl, personID);
			Long urlSize = 0L;
			HistorySaverBL.addAttachment(workItemID, personID, locale, theUrl, description, urlSize, true);


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


		}catch(AttachBLException ex) {
			LOGGER.error("Can't save attachemnt as a link!!", ex);
			List<LabelValueBean> errors=new ArrayList<LabelValueBean>();
			String error = ex.getMessage();
			errors.add(new LabelValueBean(error,"theFile"));
			JSONUtility.encodeJSONErrorsExtJS(response, errors);
		}

		return null;
	}


	/**
	 * Deletes the selected attachments
	 * @return
	 */
	public String deleted(){
		Integer[] attachmentsToDelete = ItemDetailBL.getIntegerTokens(deletedItems);
		Date originalLastModifiedDate=null;
		Date lastModifiedDate= DateTimeUtils.getInstance().parseISODateTime(lastModified);

		for (int i = 0; i < attachmentsToDelete.length; i++) {
			if(workItemID==null){
				HttpServletRequest request = ServletActionContext.getRequest();
				HttpSession httpSession = request.getSession();
				WorkItemContext workItemContext=((WorkItemContext)session.get("workItemContext"));
				if(workItemContext==null){
					String err="No context on session!";
					LOGGER.error("No context on session");
					JSONUtility.encodeJSONFailure(err);
					return null;
				}
				List<TAttachmentBean> attachments=workItemContext.getAttachmentsList();
				if(attachments==null){
					attachments=new ArrayList<TAttachmentBean>();
				}
				String sessionID=httpSession.getId();
				attachments=AttachBL.deleteLocalAttachment(attachments, attachmentsToDelete[i], sessionID);
				workItemContext.setAttachmentsList(attachments);
			} else {
				TWorkItemBean workItemBean=null;
				try {
					workItemBean = ItemBL.loadWorkItem(workItemID);
				} catch (ItemLoaderException e) {
					LOGGER.error("Loading the workItem failed with " + e.getMessage(), e);
				}
				if (workItemBean!=null && AccessBeans.isAllowedToChange(workItemBean, personID)) {
					if(workItemBean!=null){
						originalLastModifiedDate=workItemBean.getLastEdit();
					}
					TAttachmentBean attachmentBean = AttachBL.loadAttachment(attachmentsToDelete[i], workItemID, false);
					AttachBL.deleteAttachment(attachmentsToDelete[i], workItemID);
					//register the remove in history
					if (attachmentBean!=null) {
						HistorySaverBL.removeAttachment(workItemID, personID, locale,
								HistorySaverBL.getAttachmentHistoryText(attachmentBean.getFileName(), attachmentBean.getDescription(), null, locale, BooleanFields.fromStringToBoolean(attachmentBean.getIsUrl())));
					}
				}
			}
		}
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
		if(originalLastModifiedDate!=null&&lastModifiedDate!=null){
			if(!lastModifiedDate.before(originalLastModifiedDate)){
				TWorkItemBean workItemBean=ItemBL.loadWorkItemSystemAttributes(workItemID);
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

	public String download(){
		if(personID==null){
			String forwardUrl="downloadAttachment.action?workItemID="+workItemID+"&attachKey="+attachKey;
			session.put(Constants.POSTLOGINFORWARD, forwardUrl);
			// forward to login
			return "logon";
		}
		TPersonBean person=((TPersonBean) session.get(Constants.USER_KEY));
		String allwaysSaveAttachmentStr=PropertiesHelper.getProperty(person.getMoreProperties(), TPersonBean.ALWAYS_SAVE_ATTACHMENT);
		boolean allwaysSaveAttachment=false;
    	if(allwaysSaveAttachmentStr!=null&&allwaysSaveAttachmentStr.equalsIgnoreCase("true")){
    		allwaysSaveAttachment=true;
		}else{
			allwaysSaveAttachment=false;
		}
    	String disposition="inline";
    	if(allwaysSaveAttachment){
   			disposition="attachment";
    	}
		HttpServletRequest request = ServletActionContext.getRequest();
		DetectBrowser dtb = new DetectBrowser();
		dtb.setRequest(request);
		LOGGER.debug("Download attachment: " + attachKey + " for item: " + workItemID);
		//TODO check if access is allowed for download attachment
		boolean isAccessAllowed=true;
		if( !isAccessAllowed){
			addActionError("item.err.noAccess");
			LOGGER.debug("Access to attachment denied.");
			return null;
		}else{
			TAttachmentBean attach= extractAttachmentBean();
			if(attach==null){
				LOGGER.error("Error loading attachment with id "+attachKey+" for item "+workItemID);
				return null;
			}
			HttpServletResponse response=ServletActionContext.getResponse();
			// setup the http header
			response.setHeader("Pragma", "");
			// Workaround for a IE bug
			if (dtb.getIsIE()) {
				response.setHeader("Cache-Control", "private");
				response.setHeader("Expires", "0");
				//!!!! For IE and .pdf files set disposition as attachment
				if(attach.getFileName().endsWith(".pdf")){
					response.setHeader("Content-Disposition",
							"attachment; filename=\""
							+ attach.getFileName() + "\"");
				}else{
					response.setHeader("Content-Disposition",
							disposition+"; filename=\""
							+ attach.getFileName() + "\"");
				}
			}
			else {
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("Content-Disposition",
						disposition+"; filename=\""
						+ attach.getFileName() + "\"");
			}
			response.setHeader("Content-length", Long.toString(attach.getSize()));
			String contentType = attach.getMimeType();
			response.setHeader("Content-Type", contentType);

			// only for debugging now
			LOGGER.debug("set Content-Type: " + contentType);

			// copy to output
			LOGGER.debug("Delivering file...");
			OutputStream outstream=null;
			try {
				outstream = response.getOutputStream();
			} catch (IOException e) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
				LOGGER.error("Error obtaining output stream from request.",e);
				return null;
			}
			long startTime = System.currentTimeMillis();

			try {
				AttachBL.download(attach,outstream);
				LOGGER.debug("Download attachment " + attach.getFileName() + " by user " + person.getFullName());
			} catch (AttachBLException e) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
				LOGGER.error("Error download attachment",e);
				return null;
			}
			finally{
				if (outstream != null) {
					try {
						outstream.flush();
						outstream.close();
					}
					catch (Exception t) {
						// just ignore
					}
				}
			}
			LOGGER.debug("Download finished in "
					+ (System.currentTimeMillis() - startTime)
					+ "ms.");
		}
		return null; // Stay on current page
	}

	public String thumbnail() {
		//TODO check if access is allowed for thumbnail attachment
		boolean  isAccessAllowed=true;
		if( !isAccessAllowed){
			addActionError("item.err.noAccess");
			LOGGER.debug("Access to attachment denied.");
			return null;
		}
		String sessionID=null;
		TAttachmentBean attach= extractAttachmentBean();

		if(attach==null){
			LOGGER.error("Error loading attachment with id "+attachKey+" for item "+workItemID);
			return null;
		}
		if(workItemID==null) {
			HttpServletRequest request = org.apache.struts2.ServletActionContext.getRequest();
			HttpSession httpSession = request.getSession();
			sessionID = httpSession.getId();
		}
		if(!AttachBL.isImage(attach)){
			LOGGER.error("Attachment with id"+attachKey+" for item "+workItemID+" is not an image!");
			return null;
		}
		HttpServletResponse  response=org.apache.struts2.ServletActionContext.getResponse();
		OutputStream outstream=null;
		try {
			outstream = response.getOutputStream();
		} catch (IOException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			LOGGER.error("Error obtaining output stream from request." + e.getMessage());
			return null;
		}
		if(AttachBL.hasTumbnail(sessionID,attach)){
			//Attach have thumb
		}else{
			boolean okThumb=AttachBL.createTumbnail(sessionID,attach);
			if(!okThumb){
				LOGGER.error("Error get attachment thumbnail with id"+attachKey+" for item "+workItemID);
				return null;
			}
		}
		try {
			AttachBL.downloadThumb(sessionID,attach,outstream);
		} catch (AttachBLException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			LOGGER.error("Error download attachemnt" + e.getMessage());
			return null;
		}
		finally{
			if (outstream != null) {
				try {
					outstream.flush();
					outstream.close();
				}
				catch (Exception t) {
					// just ignore
				}
			}
		}
		return null;
	}

	private TAttachmentBean extractAttachmentBean() {
		TAttachmentBean attach;
		if(workItemID==null/*||workItemID.intValue()==-1*/){
			//HttpSession httpSession=request.getSession();
			//String sessionID=httpSession.getId();
			WorkItemContext ctx=(WorkItemContext)session.get("workItemContext");
			if(ctx==null){
				LOGGER.error("No context on session");
				return null;
			}
			List<TAttachmentBean> localAttachments=ctx.getAttachmentsList();
			if(localAttachments==null){
				localAttachments=new ArrayList<TAttachmentBean>();
			}
			attach=AttachBL.loadLocalAttachment(attachKey, localAttachments);
		}else{
			attach=AttachBL.loadAttachment(attachKey,workItemID,true);
		}
		return attach;
	}

	/**
	 * @return the session
	 */
	public Map<String, Object> getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	/**
	 * @return the servletRequest
	 */
	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}
	/**
	 * @param servletRequest the servletRequest to set
	 */
	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the theFile
	 */
	public String getTheFile() {
		return theFile;
	}
	/**
	 * @param theFile the theFile to set
	 */
	public void setTheFile(String theFile) {
		this.theFile = theFile;
	}
	/**
	 * @return the workItemID
	 */
	public Integer getWorkItemID() {
		return workItemID;
	}
	/**
	 * @param workItemID the workItemID to set
	 */
	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}
	/**
	 * @return the attachKey
	 */
	public Integer getAttachKey() {
		return attachKey;
	}
	/**
	 * @param attachKey the attachKey to set
	 */
	public void setAttachKey(Integer attachKey) {
		this.attachKey = attachKey;
	}

    public String getOpenerRefreshURL() {
        return openerRefreshURL;
    }

    public void setOpenerRefreshURL(String openerRefreshURL) {
        this.openerRefreshURL = openerRefreshURL;
    }

 	public List<TAttachmentBean> getAttachments() {
		if(workItemID==null/*||workItemID.intValue()==-1*/){
			WorkItemContext ctx=(WorkItemContext)session.get("workItemContext");
			if(ctx==null){
				 LOGGER.error("No context on session");
				 return new ArrayList<TAttachmentBean>();
			}
			attachments=ctx.getAttachmentsList();
		}else{
			attachments=AttachBL.getAttachments(workItemID);
		}
		return attachments;
	}

	/**
	 * To remove the thousend separator
	 * @return
	 */
	/*public String getFormattedWorkItemID() {
		if (workItemID!=null) {
			return IntegerNumberFormatUtil.getInstance().formatGUI(workItemID, null);
		}
		return "";
	}*/
	public Map getApplication() {
		return application;
	}
	public void setApplication(Map application) {
		this.application = application;
	}

	public void setDeletedItems(String deletedItems) {
		this.deletedItems = deletedItems;
	}

	public void setAttachmentID(Integer attachmentID) {
		this.attachmentID = attachmentID;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
}
