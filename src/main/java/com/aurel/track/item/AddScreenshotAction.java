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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.attachment.AttachBLException;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.item.history.HistorySaverBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.LabelValueBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class AddScreenshotAction extends ActionSupport implements Preparable,SessionAware,ApplicationAware {
	private static final long serialVersionUID = 1001171102927110704L;

	private static final Logger LOGGER = LogManager.getLogger(AddScreenshotAction.class);

	private Integer workItemID;
	private String bytes;
	private String file;
	private String description;

	private Locale locale;
	private Integer personID;

	private Map application;
	private Map<String, Object> session;

	@Override
	public void setSession(Map<String, Object> session) {
		this.session=session;
	}
	@Override
	public void setApplication(Map application) {
		this.application = application;
	}
	@Override
	public void prepare() throws Exception {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		TPersonBean person=((TPersonBean) session.get(Constants.USER_KEY));
		if(person!=null){
			personID=person.getObjectID();
		}
	}
	@Override
	public String execute(){
		return SUCCESS;
	}
	public Integer getWorkItemID() {
		return workItemID;
	}
	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}
	public String saveScreenshot(){
		LOGGER.debug("Save screenshot workItemID="+workItemID);
		List<LabelValueBean> errors=new ArrayList<LabelValueBean>();
		if(file == null|| file.length()==0){
			String err=getText("common.err.required",new String[]{getText("report.export.manager.upload.file")});
			errors.add(new LabelValueBean(err,"file"));
			JSONUtility.encodeJSONErrors(ServletActionContext.getResponse(), errors);
			return null;
		}
		if(!file.endsWith(".png")){
			file=file+".png";
		}
		ApplicationBean applicationBean=(ApplicationBean)application.get(Constants.APPLICATION_BEAN);
		Double maxAttachmentSizeInMb=AttachBL.getMaxAttachmentSizeInMb(applicationBean);
		int MAXFILESIZE = AttachBL.getMaxFileSize(applicationBean);
		if (maxAttachmentSizeInMb != null && Double.compare(maxAttachmentSizeInMb.doubleValue(), 0.0) != 0){
			MAXFILESIZE = (int)(maxAttachmentSizeInMb.doubleValue() * 1024 * 1024);
		}
		else{
			MAXFILESIZE = 4 * 1024 * 1024;
		}
		byte[] bytearray=null;
		try {
			bytearray = new Base64().decode(bytes);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			LOGGER.error(ExceptionUtils.getStackTrace(e1));
			errors.add(new LabelValueBean(e1.getMessage(),"file"));
			JSONUtility.encodeJSONErrors(ServletActionContext.getResponse(), errors);
			return null;
		}

		if( bytearray.length> MAXFILESIZE){
			errors.add(new LabelValueBean(getText("attachment.maxLengthExceeded",new String[]{maxAttachmentSizeInMb+""}),"file"));
			JSONUtility.encodeJSONErrors(ServletActionContext.getResponse(), errors);
			return null;
		}
		int maxDescriptionSize=ApplicationBean.getInstance().getDescriptionMaxLength();
		if(description.length()>maxDescriptionSize){
			errors.add(new LabelValueBean(getText("item.err.tooLong",new String[]{getText("common.lbl.description"),Integer.toString(maxDescriptionSize)}),"description"));
			JSONUtility.encodeJSONErrors(ServletActionContext.getResponse(), errors);
			return null;

		}
		InputStream is = new ByteArrayInputStream(bytearray);
		ApplicationBean appBean = ApplicationBean.getInstance();
		if(appBean.isBackupInProgress()){
			errors.add(new LabelValueBean(getText("item.tabs.attachment.err.backupInProgress"),"file"));
			JSONUtility.encodeJSONErrors(ServletActionContext.getResponse(), errors);
			return null;
		}
		if(workItemID==null/*||workItemID.intValue()==-1*/){
			HttpServletRequest request = org.apache.struts2.ServletActionContext.getRequest();
			HttpSession httpSession = request.getSession();
			WorkItemContext ctx=(WorkItemContext)session.get("workItemContext");
			if(ctx==null){
				LOGGER.error("No context on session");
				errors.add(new LabelValueBean("No context on session","file"));
				JSONUtility.encodeJSONErrors(ServletActionContext.getResponse(), errors);
				return null;
			}
			List<TAttachmentBean> attachments=ctx.getAttachmentsList();
			if(attachments==null){
				attachments=new ArrayList<TAttachmentBean>();
			}
			String sessionID=httpSession.getId();
			try {
				AttachBL.saveLocal(workItemID, description, file, is, attachments, sessionID,personID);
			} catch (AttachBLException e) {
				String err="";
				if(e.getLocalizedKey()!=null){
					err= getText(e.getLocalizedKey(),e.getLocalizedParameteres());
				}else{
					err=e.getMessage();
				}
				errors.add(new LabelValueBean(err,"file"));
				JSONUtility.encodeJSONErrors(ServletActionContext.getResponse(), errors);
				return null;

			}
			ctx.setAttachmentsList(attachments);
		}else{
			try {
				AttachBL.save(workItemID, description, file,is,personID);
				//add to history
				HistorySaverBL.addAttachment(workItemID, personID, locale, file, description, Long.valueOf(bytearray.length), false);
			} catch (AttachBLException e) {
				LOGGER.error("Can't save attachemnt",e);
				String err="";
				if(e.getLocalizedKey()!=null){
					err= getText(e.getLocalizedKey(),e.getLocalizedParameteres());
				}else{
					err=e.getMessage();
				}
				errors.add(new LabelValueBean(err,"file"));
				JSONUtility.encodeJSONErrors(ServletActionContext.getResponse(), errors);
				return null;
			}
		}
		description=null;
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}

	public String getBytes() {
		return bytes;
	}
	public void setBytes(String bytes) {
		this.bytes = bytes;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public static void writeImage(String base64String) throws IOException{
		if(base64String!=null){
			byte[] bytearray =new Base64().decode(base64String);
			BufferedImage imag=ImageIO.read(new ByteArrayInputStream(bytearray));
			ImageIO.write(imag, "jpg", new File(".","snap.jpg"));
		}
	}


}
