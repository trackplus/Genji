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


package com.aurel.track.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.attachment.AttachBL;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.EqualUtils;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TAttachmentBean
    extends com.aurel.track.beans.base.BaseTAttachmentBean
    implements Serializable, ISerializableLabelBean {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(TAttachmentBean.class);
	private String fullFileNameOnDisk;
	private String fileNameOnDisk;
	private String changedByName;
			
	public String getLabel() {		
		return getFileName();
	}

	public static String getFileSizeString(Long size){
		if (size!=null) {
			if(size.longValue() < 1024) {
	            return size + " bytes";
	        } else if(size < 1048576) {
	            return (Math.round(((double)(size.longValue()*10) / 1024)))/10.0 + " KB";
	        } else {
	            return (Math.round(((double)(size.longValue()*10) / 1048576)))/10.0 + " MB";
	        }		
		}
		return "";
    };
	
	/**
	 * get size of attachment
	 * @return
	 */
	public String getSizeKb() {
		return Long.toString(getSize()>>10);
	}

	/**
	 * @return
	 */
	public String getSizeStr() {
		String attSize = getSizeKb();
		if (attSize.equals("0")) {
			attSize = getSize() + " Bytes";
		} else {
			attSize = attSize + " KB";
		}
		return attSize;
	}
	
	//the size of the disk file
	public long getSize() {
		long size=0;
		try{
			size=Long.parseLong(getFileSize());
		}catch(Exception ex){}
		return size;
	}
	
	public String getFullFileNameOnDisk() {
		return fullFileNameOnDisk;
	}

	public void setFullFileNameOnDisk(String fullFileNameOnDisk) {
		this.fullFileNameOnDisk = fullFileNameOnDisk;
	}

	public String getFileNameOnDisk() {
		return fileNameOnDisk;
	}

	public void setFileNameOnDisk(String fileNameOnDisk) {
		this.fileNameOnDisk = fileNameOnDisk;
	}

	public String getChangedByName() {
		return changedByName;
	}

	public void setChangedByName(String changedByName) {
		this.changedByName = changedByName;
	}
	
	public boolean hasChanged(TAttachmentBean attachmentBean) {
		if (attachmentBean == null) {
			return true;
		}
		if (EqualUtils.notEqual(this.getDescription(), attachmentBean.getDescription())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Serialize a label bean to a dom element
	 * @param labelBean
	 * @return
	 */
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		//not needed because it is "embedded" in the workItem element anyway 
		/*Integer workItemID = getWorkItemID();
		if (workItemID!=null) {
			attributesMap.put("workItemID", workItemID.toString());
		}*/
		Integer personID = getChangedBy();
		if (personID!=null) {
			attributesMap.put("changedBy", personID.toString());
		}
		Integer documentState = getDocumentState();
		if (documentState!=null) {
			attributesMap.put("documentState", documentState.toString());
		}
		String fileName = getFileName();
		if (fileName!=null && !"".equals(fileName)) {
			attributesMap.put("fileName", fileName);
		}
		String fileSize = getFileSize();
		if (fileSize!=null && !"".equals(fileSize)) {
			attributesMap.put("fileSize", fileSize);
		}
		String mimeType = getMimeType();
		if (mimeType!=null && !"".equals(mimeType)) {
			attributesMap.put("mimeType", mimeType);
		}
		Date lastEdit = getLastEdit(); 
		if (lastEdit!=null) {
			attributesMap.put("lastEdit", DateTimeUtils.getInstance().formatISODateTime(lastEdit));
		}
		String version = getVersion();
		if (version!=null && !"".equals(version)) {
			attributesMap.put("version", version);
		}
		String description = getDescription();
		if (description!=null && !"".equals(description)) {
			attributesMap.put("description", description);
		}
		String cryptKey = getCryptKey();
		if (cryptKey!=null && !"".equals(cryptKey)) {
			attributesMap.put("cryptKey", cryptKey);
		}
		String isEncrypted = getIsEncrypted();
		if (isEncrypted!=null && !"".equals(isEncrypted)) {
			attributesMap.put("isEncrypted", isEncrypted);
		}
		String isDeleted = getIsDeleted();
		if (isDeleted!=null && !"".equals(isDeleted)) {
			attributesMap.put("isDeleted", isDeleted);
		}
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}
	
	/**
	 * Deserialze the labelBean 
	 * @param attributes
	 * @return
	 */
	public TAttachmentBean deserializeBean(Map<String, String> attributes) {
		TAttachmentBean attachmentBean = new TAttachmentBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			attachmentBean.setObjectID(new Integer(strObjectID));
		}
		//not needed because it is "embedded" in the workItem element anyway 
		/*String strWorkItemID = attributes.get("workItemID");
		if (strWorkItemID!=null) {
			costBean.setWorkitem(new Integer(strWorkItemID));
		}*/		
		String strPerson = attributes.get("changedBy");
		if (strPerson!=null) {
			attachmentBean.setChangedBy(new Integer(strPerson));
		}
		String strDocumentState = attributes.get("documentState");
		if (strDocumentState!=null) {
			attachmentBean.setDocumentState(new Integer(strDocumentState));
		}
		attachmentBean.setFileName(attributes.get("fileName"));
		attachmentBean.setFileSize(attributes.get("fileSize"));
		attachmentBean.setMimeType(attributes.get("mimeType"));
		String strLastEdit = attributes.get("lastEdit");
		if (strLastEdit!=null) {
			attachmentBean.setLastEdit(DateTimeUtils.getInstance().parseISODateTime(strLastEdit));
		}
		attachmentBean.setVersion(attributes.get("version"));
		attachmentBean.setDescription(attributes.get("description"));
		attachmentBean.setCryptKey(attributes.get("cryptKey"));
		attachmentBean.setIsEncrypted(attributes.get("isEncrypted"));
		attachmentBean.setIsDeleted(attributes.get("isDeleted"));
		attachmentBean.setUuid(attributes.get("uuid"));
		return attachmentBean;
	}
	
	
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		TAttachmentBean attachmentBean = (TAttachmentBean)serializableLabelBean;
		Integer externalWorkItem = getWorkItem();
		Integer internalWorkItem = attachmentBean.getWorkItem();
		String externalFileName = getFileName();
		String internalFileName = attachmentBean.getFileName();
		String extarnalFieldSize = getFileSize();
		String internalFieldSize = attachmentBean.getFileSize();
		if (externalWorkItem!=null && internalWorkItem!=null && externalFileName!=null && internalFileName!=null) {
			Map<Integer, Integer> workItemMatches = matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_ISSUENO, null)); 
			return internalWorkItem.equals(workItemMatches.get(externalWorkItem)) && 
					externalFileName.equals(internalFileName) && !EqualUtils.notEqual(extarnalFieldSize, internalFieldSize);
		}
		return false;
	}

	public Integer saveBean(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		TAttachmentBean attachmentBean = (TAttachmentBean)serializableLabelBean;
		Map<Integer, Integer> workItemIDsMap = matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_ISSUENO, null));		
		if (workItemIDsMap!=null && workItemIDsMap.get(getWorkItem())!=null) {
			attachmentBean.setWorkItem(workItemIDsMap.get(getWorkItem()));
		} else {
			LOGGER.warn("No internal workItemID found for external workItemID " + getWorkItem() + " in attachmentBean ");
		}		
		Integer changedBy = getChangedBy();
		if (changedBy!=null) {
			Map<Integer, Integer> personMap = matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PERSON, null));
			if (personMap!=null && personMap.get(changedBy)!=null) {
				attachmentBean.setChangedBy(personMap.get(changedBy));
			} else {
				LOGGER.warn("No internal person found for external person " + changedBy + " in attachmentBean ");
			}
		}
		return AttachBL.save(attachmentBean);
	}
	
	private Integer imageWidth = 0;
	/**
	 * In case this is an image, get the width in pixels
	 * @return width in pixels
	 */
	public Integer getWidth() {
	  return imageHeight;
	}
	
	/**
	 * In case this is an image, set the width in pixels
	 */
	public void setWidth(Integer width) {
	  imageWidth = width;
	}
	
	private Integer imageHeight = 0;
	/**
	 * In case this is an image, get the height in pixels
	 * @return width in pixels
	 */
	public Integer getHeight() {
		return imageWidth;
	}
	
	/**
	 * In case this is an image, set the height in pixels
	 */
	public void setHeight(Integer height) {
	  imageHeight = height;
	}
}
