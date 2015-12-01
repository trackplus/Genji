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

package com.aurel.track.admin.customize.lists;

import java.io.File;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class OptionIconConfigAction extends ActionSupport 
	implements Preparable, SessionAware, ServletResponseAware, ServletRequestAware  {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private HttpServletRequest servletRequest;
	private Locale locale;
	private TPersonBean personBean;	
	private File iconFile;
	private String iconFileFileName;
	private Integer listID;
	private Integer optionID;
	private String node;
	private boolean emptyIfMissing;

	@Override
	public void prepare() throws Exception {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		if(node!=null&&node.length()>0){
			ListOptionIDTokens listOptionIDTokens = ListBL.decodeNode(node);
			listID = listOptionIDTokens.getChildListID();
			optionID = listOptionIDTokens.getOptionID();
		}
	}

	/**
	 * Renders the upload form
	 */
	@Override
	public String execute() throws Exception {
		String iconName = ListOptionIconBL.getIconFileName(listID, optionID, locale);		
		String iconPathForURL = "listOptionIcon!serveIconStream.action?node="+node+"&emptyIfMissing=true&time="+new Date().getTime();
		JSONUtility.encodeJSON(servletResponse, 
				BlobJSON.createFileUploadJSON(true, iconName, iconPathForURL));
		return null;
	}

	/**
	 * Saves the uploaded file
	 * @return
	 */
	public String upload() {
		//get the existing icon key (if any)
		Integer iconKey = ListOptionIconBL.getIconKey(listID, optionID, locale);
		//replace or add the icon content in the db and save it also on the disk
		
		iconKey = ListOptionIconBL.saveScaledImageFileInDB(iconKey, iconFile, 16, 16);
		
		ListOptionIconBL.saveIconKey(listID, optionID, iconKey, iconFileFileName, personBean.getObjectID(), locale);
		String iconPathForURL = "listOptionIcon!serveIconStream.action?node="+node+"&emptyIfMissing=true&time="+new Date().getTime();
		JSONUtility.encodeJSON(servletResponse, 
				BlobJSON.createFileUploadJSON(true, 
						iconFileFileName, iconPathForURL), false);
		return null;
	}

	/**
	 * Deletes the uploaded file if any
	 * @return
	 */
	public String delete() {
		ListOptionIconBL.deleteFileFromDB(listID, optionID, personBean.getObjectID(), locale);
		JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONSuccess());
		return null;
	}

	/**
	 * Downloads the file (inline or not)
	 * @return
	 */
	public String download() {
		boolean inline = !personBean.isAlwaysSaveAttachment();
		ListOptionIconBL.download(servletRequest, servletResponse, listID, optionID, inline, true);
		return null;
	}

	/**
	 * Serves the file inline for tree icon
	 * @return
	 */
	public String serveIconStream() {
		ListOptionIconBL.download(servletRequest, servletResponse, listID, optionID, true, emptyIfMissing);
		return null;
	}

	public void setIconFile(File iconFile) {
		this.iconFile = iconFile;
	}

	public void setIconFileFileName(String iconFileFileName) {
		this.iconFileFileName = iconFileFileName;
	}

	public void setNode(String node) {
		this.node = node;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	@Override
	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}

	public void setListID(Integer listID) {
		this.listID = listID;
	}

	public Integer getOptionID() {
		return optionID;
	}

	public void setOptionID(Integer optionID) {
		this.optionID = optionID;
	}

	public void setEmptyIfMissing(boolean emptyIfMissing) {
		this.emptyIfMissing = emptyIfMissing;
	}
	
	
}
