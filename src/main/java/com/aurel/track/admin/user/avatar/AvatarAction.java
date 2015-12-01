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

package com.aurel.track.admin.user.avatar;

import java.io.File;
import java.security.MessageDigest;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.lists.BlobBL;
import com.aurel.track.admin.customize.lists.BlobJSON;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.admin.user.profile.ProfileMainTO;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Avatar download/upload for persons
 * @author Tamas
 *
 */
public class AvatarAction extends ActionSupport 
	implements Preparable, SessionAware, ServletResponseAware, ServletRequestAware  {

	private static final Logger LOGGER = LogManager.getLogger(AvatarAction.class);

	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private HttpServletRequest servletRequest;
	private TPersonBean currentUser;	
	private File iconFile;
	private String iconFileFileName;
	
	private Integer personID;
	private String personIDs;

	public String getPersonIDs() {
		return personIDs;
	}

	public void setPersonIDs(String personIDs) {
		this.personIDs = personIDs;
	}

	private Integer context;
	//by upload icon and person profile if personID is null (new person) the iconKey should be rendered (downloaded) based on iconKey
	private Integer iconKey;
	private String iconName;
	private boolean emptyIfMissing;
	
	@Override
	public void prepare() throws Exception {
		currentUser = (TPersonBean) session.get(Constants.USER_KEY);		
	}

	/**
	 * Renders the upload form
	 */
	@Override
	public String execute() throws Exception {
		if (context!=null && context.intValue()==ProfileMainTO.CONTEXT.PROFILEEDIT) {
			//edit own profile: personID is not submitted
			personID = currentUser.getObjectID();
		}
		if (personID!=null) {
			//get from person (otherwise it is submitted)
			iconName = AvatarBL.getIconNameForPerson(personID);
		}
		//if no avatar is present do not show the default one on the user profile
		String iconPathForURL = AvatarBL.getAvatarDownloadURL(personID, iconKey, true);
		JSONUtility.encodeJSON(servletResponse, 
				BlobJSON.createFileUploadJSON(true, iconName, iconPathForURL, iconKey));
		return null;
	}

	/**
	 * Saves the uploaded file
	 * @return
	 */
	public String upload() {
		//get the existing icon key (if any)
		Integer iconKey = null;
		switch (context.intValue()) {
		case ProfileMainTO.CONTEXT.PROFILEEDIT:
			iconKey = currentUser.getIconKey();
			//replace or add the icon content in the db and save it also on the disk
			iconKey = BlobBL.saveThumbImageFileInDB(iconKey, iconFile, 100, 100,20);
			currentUser.setSymbol(iconFileFileName);
			currentUser.setIconKey(iconKey);
			personID=currentUser.getObjectID();
			PersonBL.saveSimple(currentUser);
			break;
		case ProfileMainTO.CONTEXT.USERADMINEDIT:
			//upload icon for existing person with or without icon
			TPersonBean personBean = LookupContainer.getPersonBean(personID);
			iconKey = personBean.getIconKey();
			//replace or add the icon content in the db and save it also on the disk
			iconKey = BlobBL.saveThumbImageFileInDB(iconKey, iconFile,100,100,20);
			personBean.setSymbol(iconFileFileName);
			personBean.setIconKey(iconKey);
			PersonBL.saveSimple(personBean);
			break;
		case ProfileMainTO.CONTEXT.USERADMINADD:
			iconKey = BlobBL.saveThumbImageFileInDB(null, iconFile,100,100,20);
		}
		String iconPathForURL = AvatarBL.getAvatarDownloadURL(personID, iconKey, true);
		JSONUtility.encodeJSON(servletResponse, 
				BlobJSON.createFileUploadJSON(true, 
						iconFileFileName, iconPathForURL, iconKey), false);
		return null;
	}

	/**
	 * Deletes the uploaded file if any
	 * @return
	 */
	public String delete() {
		if (context!=null && context.intValue()==ProfileMainTO.CONTEXT.PROFILEEDIT) {
			//edit own profile: personID is not submitted
			personID = currentUser.getObjectID();
		}
		AvatarBL.deleteFileFromDB(personID);
		JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONSuccess());
		return null;
	}

	/**
	 * Downloads the avatar
	 * @return
	 */
	public String download() {		
		AvatarBL.downloadAvatar(servletRequest, servletResponse, personID, iconKey, emptyIfMissing);
		return null;
	}
	
	/**
	 * This method based on personID returns in JSON format: the avatar image with checksum. 
	 * @return
	 */
	public String downloadAvatarWithCheckSum() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");	
		if(personID != null)  {
					
			byte[] oneAvatar = AvatarBL.getAvatarInByteArray(personID, iconKey);
			String oneAvatarString  = DatatypeConverter.printBase64Binary(oneAvatar);			
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] thedigest = md.digest(oneAvatar);
				String checksum =  DatatypeConverter.printBase64Binary(thedigest);
				JSONUtility.appendBooleanValue(sb, "success", true);
				JSONUtility.appendStringValue(sb, "img", oneAvatarString);
				JSONUtility.appendStringValue(sb, "checkSum", checksum, true);
			}catch(Exception ex) {
				JSONUtility.appendBooleanValue(sb, "success", false, true);
			}
			sb.append("}");
			JSONUtility.encodeJSON(servletResponse, sb.toString());			
		}
		
		return null;
	}
	
	/**
	 * This method returns the requested personID's avatar image into JSON format.
	 * At client side it is necessary to convert each image string to BASE64 array.
	 * @return
	 */
	public String downloadPersonIDsAvatar() {		
		StringBuilder sb = new StringBuilder();
		sb.append("{");

			JSONUtility.appendBooleanValue(sb, "success", true);			
			sb.append("\"data\":");
			sb.append("[");
			if(personIDs != null && personIDs.length() > 0) {
				String[] personIDsArray = personIDs.split(",");
				for(int i = 0; i < personIDsArray.length; i++) {
					int id = Integer.parseInt(personIDsArray[i]);				
					byte[] oneAvatar = AvatarBL.getAvatarInByteArray(id, iconKey);
									
					
					String oneAvatarString  = DatatypeConverter.printBase64Binary(oneAvatar);
					sb.append("{");
					JSONUtility.appendStringValue(sb, Integer.toString(id), oneAvatarString);
					try {
						MessageDigest md = MessageDigest.getInstance("MD5");
						byte[] thedigest = md.digest(oneAvatar);					
						JSONUtility.appendStringValue(sb, "checkSum", DatatypeConverter.printBase64Binary(thedigest), true);
					}catch(Exception ex) {
						JSONUtility.appendStringValue(sb, "checkSum", "", true);
						LOGGER.error(ExceptionUtils.getStackTrace(ex));
					}
					if(i == personIDsArray.length - 1) {
						sb.append("}");
					}else {
						sb.append("},");
					}
				}
			}
			sb.append("]");
			sb.append("}");		
		JSONUtility.encodeJSON(servletResponse, sb.toString());
		return null;
	}

	public void setIconFile(File iconFile) {
		this.iconFile = iconFile;
	}

	public void setIconFileFileName(String iconFileFileName) {
		this.iconFileFileName = iconFileFileName;
	}

	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	@Override
	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
	}

	public void setIconKey(Integer iconKey) {
		this.iconKey = iconKey;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public void setContext(Integer context) {
		this.context = context;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public boolean isEmptyIfMissing() {
		return emptyIfMissing;
	}

	public void setEmptyIfMissing(boolean emptyIfMissing) {
		this.emptyIfMissing = emptyIfMissing;
	}
	
	
}
