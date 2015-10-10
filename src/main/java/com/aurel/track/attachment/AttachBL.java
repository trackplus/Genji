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

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.ImageIcon;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.cluster.ClusterBL.CHANGE_TYPE;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.dao.AttachmentDAO;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dbase.HandleHome;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemPersisterException;
import com.aurel.track.item.history.HistorySaverBL;
import com.aurel.track.lucene.index.associatedFields.AttachmentIndexer;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.ImageUtils;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.emailHandling.EmailAttachment;

/**
 * Business logic for attachment
 *
 * @author Adrian Bojani
 *
 */
/**
 * @author friedj
 *
 */
public class AttachBL {
	private static final Logger LOGGER = LogManager.getLogger(AttachBL.class);

	private static AttachmentDAO attachmentDAO = DAOFactory.getFactory().getAttachmentDAO();

	private static MimetypesFileTypeMap mimeTypeMap = null;

	private static int attachTempIndex = 1;
	// folder name for the attachments
	public static final String AttachmentsName = "attachments";
	// The delimiter for the creating temporary attachment's folder
	private static final String tempDelimiter = "_";
	//parameter for key of the issue for attachment download
	private static final String AttachDbFname = "tattach";
	private static final String AttachDbFnameType = ".dat";
	private static final String AttachDbFnameTypeNew =".new";

	public static final String tmpAttachments ="tmpAttachments";
	public static final String excelImportDir ="excelImport";
	public static final String docxImportDir ="docxImport";
	//public static final String docxExportDir ="docxExport";
	public static final String msProjectImportDir="msProjectImport";

	private static final Set<String> imgExt=new TreeSet<String>();
	static{
		imgExt.add("JPG");
		imgExt.add("GIF");
		imgExt.add("PNG");
	}

	/**
	 * Gets all attachments
	 * @return
	 */
	public static List<TAttachmentBean> loadAll() {
		List<TAttachmentBean> allAttachmnents = attachmentDAO.loadAll();
		for (TAttachmentBean attachmentBean : allAttachmnents) {
			Integer attachmentID = attachmentBean.getObjectID();
			Integer workItemID = attachmentBean.getWorkItem();
			attachmentBean.setFullFileNameOnDisk(getFullFileName(null, attachmentID, workItemID));
			attachmentBean.setFileNameOnDisk(getFileNameAttachment(attachmentID, workItemID));
		}
		return allAttachmnents;
	}

	public static List<TAttachmentBean> getExistingAttachments(List<TAttachmentBean> attachmentBeansList) {
		List<TAttachmentBean> existingAttachments=new ArrayList<TAttachmentBean>();
		if (attachmentBeansList!=null) {
			Iterator<TAttachmentBean> iterator = attachmentBeansList.iterator();
			while (iterator.hasNext()) {
				TAttachmentBean attachmentBean = iterator.next();
				String diskFileName=getFullFileName(null,attachmentBean.getObjectID(), attachmentBean.getWorkItem());
				LOGGER.debug("diskFileName="+diskFileName);
				File file=new File(diskFileName);
				if(file.exists()){
					existingAttachments.add(attachmentBean);
				}
			}
		}
		return existingAttachments;
	}

	/**
	 * Gets the attachments filtered by filterSelectsTO and raciBean
	 * @param filterUpperTO
	 * @param raciBean
	 * @param personID
	 * @return
	 */
	public static List<TAttachmentBean> loadTreeFilterAttachments(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID) {
		return attachmentDAO.loadTreeFilterAttachments(filterUpperTO, raciBean, personID);
	}

	/**
	 * Get the attachments for a TQL expression
	 * @param tqlExpression
	 * @param personBean
	 * @param locale
	 * @param errors
	 * @return
	 */
	public static List<TAttachmentBean> loadTQLFilterAttachments(String tqlExpression, TPersonBean personBean, Locale locale, List<ErrorData> errors) {
		return attachmentDAO.loadTQLFilterAttachments(tqlExpression, personBean, locale, errors);
	}

	/**
	 * Obtain the attachments for given attachment ids
	 * @param attachmentIDs
	 * @return
	 */
	public static List<TAttachmentBean> loadByAttachmentIDs(List<Integer> attachmentIDs) {
		List<TAttachmentBean> attachmentBeans = attachmentDAO.loadByAttachmentIDs(attachmentIDs);
		return filterNotOnDisk(attachmentBeans);
	}
	
	/**
	 * Obtain the attachment for given attachment id
	 * @param attachmentID the object id of the attachment
	 * @return
	 */
	public static TAttachmentBean loadByAttachmentID(Integer attachmentID) {
		TAttachmentBean attachmentBean = attachmentDAO.loadByID(attachmentID);
		return attachmentBean;
	}


	public static List<TAttachmentBean> loadByWorkItems(int[] workItemIDs) {
		List<TAttachmentBean> attachmentBeans = attachmentDAO.loadByWorkItemKeys(workItemIDs);
		List<TPersonBean> changedByPersons = PersonBL.getAttachmentPersons(workItemIDs);
		addChangedByName(attachmentBeans, changedByPersons);
		return filterNotOnDisk(attachmentBeans);
	}

	private static List<TAttachmentBean> loadByWorkItem(Integer workItemID) {
		List<TAttachmentBean> attachmentBeans= attachmentDAO.loadByWorkItemKey(workItemID);
		List<TPersonBean> changedByPersons = PersonBL.getAttachmentPersons(new int[] {workItemID});
		return addChangedByName(attachmentBeans, changedByPersons);
	}
	public static int countByWorkItemID(Integer workItemID){
		List<TAttachmentBean> attachList=AttachBL.getAttachments(workItemID);
		return attachList.size();
		//return  attachmentDAO.countByWorkItemID(workItemID);
	}

	private static List<TAttachmentBean> filterNotOnDisk(List<TAttachmentBean> attachmentBeanList) {
		if(attachmentBeanList==null){
			return new ArrayList<TAttachmentBean>();
		}
		//the attachment must have a file on disk
		//List<TAttachmentBean> existingAttachments=new ArrayList<TAttachmentBean>();
		for (Iterator<TAttachmentBean> iterator = attachmentBeanList.iterator(); iterator.hasNext();) {
			TAttachmentBean attachmentBean = iterator.next();
			if(!BooleanFields.fromStringToBoolean(attachmentBean.getIsUrl())) {
				boolean fileOnDisk=ensureFileOnDisk(attachmentBean, attachmentBean.getWorkItem());
				if(fileOnDisk){
					ensureMimeType(attachmentBean);
				} else {
					iterator.remove();
				}
			}
		}
		return attachmentBeanList;
	}

	private static List<TAttachmentBean> addChangedByName(List<TAttachmentBean> attachmentBeans, List<TPersonBean> changedByPersons) {
		if (attachmentBeans!=null) {
			Map<Integer, TPersonBean> personBeansMap = GeneralUtils.createMapFromList(changedByPersons);
			for (TAttachmentBean attachmentBean : attachmentBeans) {
				TPersonBean personBean = personBeansMap.get(attachmentBean.getChangedBy());
				if (personBean!=null) {
					attachmentBean.setChangedByName(personBean.getLabel());
				}
			}
		}
		return attachmentBeans;
	}

	/**
	 * Obtain the attachment list from db for an item
	 * and the attachment must have a file on disk.
	 * @param itemID the id of item
	 * @return list of TAttachmentBean
	 */
	public static List<TAttachmentBean> getAttachments(Integer itemID){
		List<TAttachmentBean> attachmentBeansDB= loadByWorkItem(itemID);
		if(attachmentBeansDB==null||attachmentBeansDB.isEmpty()){
			return new ArrayList<TAttachmentBean>();
		}
		//the attachment must have a file on disk
		List<TAttachmentBean> realAttachments=new ArrayList<TAttachmentBean>();

		for (int i = 0; i < attachmentBeansDB.size(); i++) {
			//verify every attachment if exist file on disk system
			TAttachmentBean attach=attachmentBeansDB.get(i);
			if(BooleanFields.fromStringToBoolean(attach.getIsUrl())) {
				realAttachments.add(attach);
			}else {
				boolean fileOnDisk=ensureFileOnDisk(attach, itemID);
				if(fileOnDisk){
					ensureMimeType(attach);
					realAttachments.add(attach);
				}
			}
		}
		return realAttachments;
	}
	public static List<TAttachmentBean> getAttachmentsImage(Integer itemID) {
		List<TAttachmentBean> allAttachments= getAttachments(itemID);
		//the attachment must have a file on disk
		List<TAttachmentBean> realAttachments=new ArrayList<TAttachmentBean>();

		for (int i = 0; i < allAttachments.size(); i++) {
			//verify every attachment if exist file on disk system
			TAttachmentBean attach=allAttachments.get(i);
			boolean image = AttachBL.isImage(attach);
			if(image){
				realAttachments.add(attach);
			}
		}
		return realAttachments;

	}

	public static Map<Integer, List<TAttachmentBean>> getGroupedAttachemnts(List<TAttachmentBean> attachList){
		Map<Integer, List<TAttachmentBean>> itemAttachmentsMap = new HashMap<Integer, List<TAttachmentBean>>();
		if (attachList!=null && !attachList.isEmpty()) {
			for (TAttachmentBean attachmentBean : attachList) {
				Integer workItemID=attachmentBean.getWorkItem();
				List<TAttachmentBean> itemAttachList = itemAttachmentsMap.get(workItemID);
				if (itemAttachList==null){
					itemAttachList = new LinkedList<TAttachmentBean>();
					itemAttachmentsMap.put(workItemID,itemAttachList);
				}
				itemAttachList.add(attachmentBean);
			}
		}
		return itemAttachmentsMap;
	}

	public static List<TAttachmentBean> getAttachments(int[] itemIDs){
		return getAttachments(itemIDs,true);
	}
	public static List<TAttachmentBean> getAttachments(int[] itemIDs,boolean verifyFileOnDisk){
		List<TAttachmentBean> attachmentBeansDB= attachmentDAO.loadByWorkItemKeys(itemIDs);
		if (verifyFileOnDisk) {
			return filterNotOnDisk(attachmentBeansDB);
		} else {
			return attachmentBeansDB;
		}
		/*if(attachmentBeansDB==null||attachmentBeansDB.isEmpty()){
			return new ArrayList();
		}
		if(verifyFileOnDisk){
			//the attachment must have a file on disk
			List<TAttachmentBean> realAttachments=new ArrayList();

			for (int i = 0; i < attachmentBeansDB.size(); i++) {
				//verify every attachment if exist file on disk system
				TAttachmentBean attach=(TAttachmentBean) attachmentBeansDB.get(i);
				boolean fileOnDisk=ensureFileOnDisk(attach, attach.getWorkItem());
				if(fileOnDisk){
					ensureMimeType(attach);
					realAttachments.add(attach);
				}
			}
			return realAttachments;
		}else{
			return attachmentBeansDB;
		}*/
	}

	/**
	 * Load the attachment with given id
	 * @param attachmentID
	 * @return
	 */
	public static TAttachmentBean loadByID(Integer attachmentID) {
		return attachmentDAO.loadByID(attachmentID);
	}

	/**
	 * Delete the attachment form DB
	 * @param attachmentID
	 */
	public static void delete(Integer attachmentID) {
		attachmentDAO.delete(attachmentID);
	}

	/**
	 * Load the attachment from DB
	 * and also verify the file on disk
	 * @param attachmentID
	 * @param itemID
	 * @return
	 */
	public static TAttachmentBean loadAttachment(Integer attachmentID, Integer itemID, boolean setMimeType){
		TAttachmentBean attach=loadByID(attachmentID);
		if(attach==null){
			LOGGER.warn("Attachment with id:"+attachmentID+" for item:"+itemID+" not found in database!");
			return null;
		}
		//verify the file on disk
		if(BooleanFields.fromStringToBoolean(attach.getIsUrl())) {
			return attach;
		}else {
			if(ensureFileOnDisk(attach, itemID)){
				if (setMimeType) {
					ensureMimeType(attach);
				}
				return attach;
			}
		}
		return null;
	}
	public static TAttachmentBean loadLocalAttachment(Integer attachmentID,List<TAttachmentBean> attachments){
		return findLocalAttachment(attachments, attachmentID);
	}
	/**
	 * Download the attachment
	 * @param attach
	 * @param outStream
	 */
	public static void download(TAttachmentBean attach,OutputStream outStream) throws AttachBLException {
		download(attach.getFullFileNameOnDisk(), outStream);
	}
	public static void downloadThumb(String sessionID, TAttachmentBean attach,OutputStream outStream) throws AttachBLException {
		download(getFullThumbFileName(sessionID,attach), outStream);
	}

	public static void download(String fileName, OutputStream outStream) /*throws AttachBLException*/ {
		// open the file
		BufferedInputStream instream = null;
		try {
			//retrieve the file data
			File file = new File(fileName);
			instream = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException fnfe) {
			LOGGER.debug(ExceptionUtils.getStackTrace(fnfe));
			return;
		} catch (Exception ioe) {
			LOGGER.debug(ExceptionUtils.getStackTrace(ioe));
			return;
		}
		// copy the file to output stream
		int BUF_SIZE = 	Constants.MAXFILESIZE;
		byte[] buffer = new byte[BUF_SIZE];
		try {
			long totalByteCount = 0;
			int size = -1;
			while (-1 != (size = instream.read(buffer, 0, BUF_SIZE))){
				outStream.write(buffer, 0, size);
				totalByteCount += size;
				LOGGER.debug("Sent " + (size >> 10) + " KB | Total: "
							 + (totalByteCount >> 10) +"KB");
			}
		} catch (Exception t) {
			LOGGER.info("Download aborted: " + t.getMessage());
		} finally {
			try {
				instream.close();
			}
			catch (Exception t) {
				// just ignore
			}
		}
	}
	public static List<TAttachmentBean> saveLocal(Integer itemID,String description, String fileName, InputStream is,List<TAttachmentBean> attachments,String sessionID,Integer userId) throws AttachBLException{
		if(fileName==null||fileName.length()==0){
			throw new IllegalArgumentException("The file name must be not null!");
		}
		TAttachmentBean oldAttach= findLocalAttachment(attachments,fileName);
		if(oldAttach!=null){
			//delete the existing attachment
			LOGGER.debug("Found attach with the same name.Delete attachment " + oldAttach.getFileName()+ " for item " + itemID);
			attachments=deleteLocalAttachment(attachments,oldAttach.getObjectID(),sessionID);
		}
		//store the new attachment
		LOGGER.debug("Save attachment " + fileName + " for item "+ itemID);
		//save in the DB
		TAttachmentBean attach=new TAttachmentBean();
		attach.setWorkItem(itemID);
		attach.setFileName(fileName);
		attach.setDescription(description);
		attach.setChangedBy(userId);
		attach.setLastEdit(new Date());
		Integer attachID=new Integer(getAttachTempIndex());
		attach.setObjectID(attachID);
		//ensure the disk file
		boolean parentDirOk=ensureDir(getFullDirName(sessionID,itemID));
		if(!parentDirOk){
			LOGGER.error("Error accessing/create directory for attachments");
			return attachments;
		}
		//set the fileNameOnDisk
		attach.setFullFileNameOnDisk(getFullFileName(sessionID,attachID, itemID));
		attach.setFileNameOnDisk(getFileNameAttachment(attachID, itemID));

		//store on disk the attach
		storeOnDisk(attach, is);

		//add attach
		attachments.add(attach);
		//no add to lucene the local attachments
		return attachments;
	}
	private static TAttachmentBean findLocalAttachment(List<TAttachmentBean> attachments,String fileName){
		if(attachments==null||attachments.isEmpty()){
			return null;
		}
		for (int i = 0; i < attachments.size(); i++) {
			TAttachmentBean attach=(TAttachmentBean) attachments.get(i);
			if(attach.getFileName().equals(fileName)){
				return attach;
			}
		}
		return null;
	}
	private static TAttachmentBean findLocalAttachment(List<TAttachmentBean> attachments,Integer attachID){
		if(attachments==null||attachments.isEmpty()){
			return null;
		}
		for (int i = 0; i < attachments.size(); i++) {
			TAttachmentBean attach=(TAttachmentBean) attachments.get(i);
			if(attach.getObjectID().equals(attachID)){
				ensureMimeType(attach);
				return attach;
			}
		}
		return null;
	}

	/**
	 * Save an attachment
	 * @param itemID
	 * @param description
	 * @param fileName
	 * @param is
	 */
	public static TAttachmentBean save(Integer itemID, String description,
			String fileName, InputStream is,Integer userId) throws AttachBLException{
		if(fileName==null||fileName.length()==0){
			throw new AttachBLException("The fileName must be not null!");
		}
		LOGGER.debug("Save attachment " + fileName + " for item "+ itemID);

		//save in the DB
		TAttachmentBean attach=new TAttachmentBean();
		attach.setWorkItem(itemID);
		attach.setFileName(fileName);
		attach.setDescription(description);
		attach.setChangedBy(userId);
		attach.setLastEdit(new Date());
		Integer attachID=save(attach);
		if(attachID==null){
			throw new AttachBLException("Can't save attachemnt in DB");
		}
		attach.setObjectID(attachID);

		//ensure the disk file
		boolean parentDirOk=ensureDir(getFullDirName(null,itemID));
		if(!parentDirOk){
			LOGGER.error("Error accessing/create directory for attachments");
			throw new AttachBLException("Error accessing/create directory for attachments");
		}
		//set the fileNameOnDisk
		attach.setFullFileNameOnDisk(getFullFileName(null,attachID, itemID));
		attach.setFileNameOnDisk(getFileNameAttachment(attachID, itemID));

		TAttachmentBean attachDB=null;
		//store on disk the attach
		try{
			storeOnDisk(attach, is);
			attachDB=loadByID(attachID);
			attachDB.setFileSize(attach.getFileSize());
			attachDB.setFullFileNameOnDisk(attach.getFullFileNameOnDisk());
			attachDB.setFileNameOnDisk(attach.getFileNameOnDisk());
			//do save again to persist the size
			save(attachDB);
			//add to lucene
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Add attachment " + attachDB.getObjectID() + " to the existing workItem " + attachDB.getWorkItem());
			}
			AttachmentIndexer.getInstance().addToIndex(attachDB, true);
			//possible lucene update in other cluster nodes
			ClusterMarkChangesBL.markDirtyAttachmentInCluster(attachID, CHANGE_TYPE.ADD_TO_INDEX);
		}catch (AttachBLException e) {
			//can't store on  disk. delete the attachment from db ant throw the ex
			delete(attachID);
			throw e;
		}

		return attachDB;
	}

	/**
	 * Save an attachment as a link.
	 * @param itemID
	 * @param description
	 * @param url
	 * @param userId
	 * @return
	 * @throws AttachBLException
	 */
	public static Integer saveAttachmentAsLink(Integer itemID, String description,
			String url, Integer userId) throws AttachBLException{
		//save in the DB
		TAttachmentBean attach=new TAttachmentBean();
		attach.setWorkItem(itemID);
		attach.setFileName(url);
		attach.setDescription(description);
		attach.setChangedBy(userId);
		attach.setLastEdit(new Date());
		attach.setIsUrl("Y");
		Integer attachID=AttachBL.save(attach);
		return attachID;
	}
	/**
	 * Delete an attachment
	 * @param attachID
	 * @param itemID
	 */
	public static void deleteAttachment(Integer attachID,Integer itemID) {
		delete(attachID);
		//String fileName=getFileNameAttachment(attachID, itemID);
		String fullFileName = getFullFileName(null,attachID,itemID);
		File file = null;
		try {
			file = new File(fullFileName);
		} catch (NullPointerException e) {
			return;
		}
		LOGGER.debug("Trying to delete " + fullFileName);
		try {
			if (file.exists()) {
				file.delete();
			}
			String thumbName = getFullThumbFileName(fullFileName);
			File fileThumb=new File(thumbName);
			if (fileThumb.exists()) {
				fileThumb.delete();
			}

			// check id directory needs to be removed
			String fullDirName=getAttachDirBase()+File.separator+itemID;
			List<TAttachmentBean> fileList = getAttachments(itemID);
			if (fileList.isEmpty()) {
				deleteDir(fullDirName);
			}
		} catch (SecurityException e) {
			LOGGER.error("Security Exception when trying to delete "
					+ "file "+ fullFileName);
		}
		//delete from lucene index
		AttachmentIndexer.getInstance().deleteByKey(attachID);
		//possible lucene update in other cluster nodes
		ClusterMarkChangesBL.markDirtyAttachmentInCluster(attachID, CHANGE_TYPE.DELETE_FROM_INDEX);
	}

	public static List<TAttachmentBean> deleteLocalAttachment(List<TAttachmentBean> attachments,Integer attachID,String sessionID) {
		TAttachmentBean attach=findLocalAttachment(attachments,attachID);
		if(attach==null){
			LOGGER.error("Attachment not found locally:"+attachID);
			return attachments;
		}
		String fileName=attach.getFullFileNameOnDisk();
		File file = null;
		try {
			file = new File(fileName);
		} catch (NullPointerException e) {
			return attachments;
		}
		LOGGER.debug("Trying to delete " + fileName);
		try {
			if (file.exists()) {
				file.delete();
			}
		} catch (SecurityException e) {
			LOGGER.error("Security Exception when trying to delete "
					+ "file "+ fileName);
		}
		attachments.remove(attach);
		return attachments;
	}

	private static boolean ensureFileOnDisk(TAttachmentBean attach, Integer itemID){
		String diskFileName=getFullFileName(null,attach.getObjectID(), itemID);
		LOGGER.debug("diskFileName="+diskFileName);
		File file=new File(diskFileName);
		if(file.exists()){
			//ensure length
			attach.setFileSize(file.length()+"");
			attach.setFullFileNameOnDisk(file.getAbsolutePath());
			attach.setFileNameOnDisk(getFileNameAttachment(attach.getObjectID(), itemID));
			return true;
		}else{
			LOGGER.info("The attachment for issue no. " + itemID + " with attachment id "+attach.getObjectID()+" and name "+attach.getFileName()+" does not exist on disk!");
			return false;
		}
	}

	public static String getFullFileName(String sessionId,Integer attachID,Integer itemID){
		return getFullDirName(sessionId, itemID)+File.separator+getFileNameAttachment(attachID, itemID);
	}
	public static String getFullDirName(String sessionId,Integer itemID){
		if(sessionId!=null){
			//localy attach
			if(itemID==null){
				itemID=new Integer(-1);
			}
			return getAttachDirBase()+File.separator+sessionId+tempDelimiter+itemID;
		}
		return getAttachDirBase()+File.separator+itemID;
	}
	/**
	 * @return name of the attachment on disk
	 */
	public static String getFileNameAttachment(Integer attachID, Integer itemID) {
		NumberFormat nf = new DecimalFormat("00000");
		String dbId = nf.format(attachID);
		// choose different suffix for attachments of new WorkItems
		String fileSuffix;
		if (itemID == null ||itemID.intValue()==-1) {
			fileSuffix = AttachDbFnameTypeNew;
		} else {
			fileSuffix = AttachDbFnameType;
		}
		return AttachDbFname + dbId + fileSuffix;
	}
	private static boolean ensureDir(String dirName){
		File f=new File(dirName);
		if(!f.exists()){
			f.mkdirs();
		}
		return f.exists()&&f.isDirectory();
	}
	private static  void deleteDir(String fullDirName) {
		// now go ahead and delete empty directory
		File dir = null;
		try {
			dir = new File(fullDirName);
		} catch (NullPointerException e) {
			return;
		}
		try {
			if (dir.exists() && dir.isDirectory()) {
				if (!dir.delete()) {
					LOGGER.error(
						"Couldn't delete directory " + fullDirName);
				}
			}
			dir = null;
		} catch (SecurityException e) {
			LOGGER.error(
				"Security Exception when trying to delete "
					+ "directory "
					+ fullDirName);
		}
	}

	static public boolean deleteDirectory(File path) {
		if (path.exists() && path.isDirectory()) {
		  File[] files = path.listFiles();
		  for(int i=0; i<files.length; i++) {
			 if(files[i].isDirectory()) {
			   deleteDirectory(files[i]);
			 }
			 else {
			   files[i].delete();
			 }
		  }
		}
		return path.delete();
	  }


	private static  void storeOnDisk(TAttachmentBean attach,InputStream inStream) throws AttachBLException{
		String fullFileName=attach.getFullFileNameOnDisk();
		LOGGER.debug("Full Filename for attachment: " + fullFileName);
		try {
			//retrieve the file data
			FileOutputStream faos = new FileOutputStream(fullFileName);
			byte[] buffer = new byte[8192];
			int bytesRead = 0;
			while ((bytesRead = inStream.read(buffer, 0, 8192)) != -1) {
				faos.write(buffer, 0, bytesRead);
			}
			//close the stream
			faos.flush();
			inStream.close();
			faos.close();
		} catch (FileNotFoundException fnfe) {
			LOGGER.error("Storing the attachment on disk failed: File not found(if the file exists but is a directory rather than a regular file, does not exist but cannot  be created, or cannot be opened for any other reason. FullFileName="+fullFileName,fnfe);
			throw new AttachBLException("File:'" + fullFileName + "' not found on server. "+fnfe.getMessage(), fnfe);
		} catch (IOException ioe) {
				LOGGER.debug(ExceptionUtils.getStackTrace(ioe));
			LOGGER.error("Storing the attachment on disk failed: IOException fullFileName="+fullFileName,ioe);
			throw new AttachBLException("Can't save attachment:"+ioe.getMessage(), ioe);
		}
		attach.setFileSize(Long.toString(new File(fullFileName).length()));
		//TODO save mimeType in DB after change the DB to allow storing mimeType with more characters
		//ensureMimeType(attach);
	}
	private static synchronized int getAttachTempIndex() {
		int index = attachTempIndex;
		attachTempIndex += 1;
		return index;
	}

	/**
	 * Delete all the files and the directory, where the temporary attchments
	 * for a new item were stored
	 * @param sessionID
	 */
	public static void deleteTempFiles(String sessionID) {
		LOGGER.debug("Cleanup attachments for session:"+sessionID);
		File attachDir =new File(getFullDirName(sessionID, new Integer(-1)));
		LOGGER.debug("attachDir="+attachDir.getAbsolutePath());
		if(attachDir.exists()) {
			// remove all files first
			File[] files = attachDir.listFiles();
			for(int i=0; i<files.length; ++i) {
				if(files[i].isFile()) {
					files[i].delete();
				} else {
					LOGGER.error("Unexpected directory found!");
				}
			}
			// remove the directory
			if(!attachDir.delete()) {
				LOGGER.error("Deleting the directory failed!");
			}
		}
	}

	/**
	 * Save the attachments for new item
	 * @param attachList List<TAttachmentBean>
	 * @param sessionID
	 * @param itemID
	 */
	public static void approve(List<TAttachmentBean> attachList,String sessionID,Integer itemID){
		if (attachList.isEmpty()) {
			LOGGER.debug("No attachments were stored for the new item");
			return;
		}
		LOGGER.debug("Approve " + attachList.size() + " attachments for item#" + itemID);
		// create the directory where the attachments will be copied(renamed) to
		File newDir =new File(getFullDirName(null, itemID));
		boolean ok = newDir.mkdir();
		if(!ok) {
			LOGGER.error("Creating new attachment dir " + newDir.getAbsolutePath() + " failed!");
		}

		// work through all the attachments
		Iterator<TAttachmentBean> attachIter = attachList.iterator();
		while(attachIter.hasNext()) {
			TAttachmentBean attach = attachIter.next();
			// get the old filename on disk
			String fileNameOnDiskOld = attach.getFullFileNameOnDisk();
			attach.setWorkItem(itemID);
			//save the attach in DB
			Integer attachID=save(attach);

			attach.setObjectID(attachID);
			//ensure the disk file
			boolean parentDirOk=ensureDir(getFullDirName(null,itemID));
			if(!parentDirOk){
				LOGGER.error("Error accessing or creating directory for attachments");
				return;
			}
			//set the fileNameOnDisk
			attach.setFullFileNameOnDisk(getFullFileName(null,attachID, itemID));
			attach.setFileNameOnDisk(getFileNameAttachment(attachID, itemID));
			String fileNameOnDiskNew=attach.getFullFileNameOnDisk();

			LOGGER.debug("Rename " + fileNameOnDiskOld + " to " + fileNameOnDiskNew);
			// rename all the attachments
			File fileOld = new File(fileNameOnDiskOld);
			File fileNew = new File(fileNameOnDiskNew);
			ok = fileOld.renameTo(fileNew);
			if(!ok) {
				LOGGER.error("Renaming " + fileOld.getAbsolutePath() +
					" to " + fileNew.getAbsolutePath() + " failed!");
			}
		}
		// remove the old temp-directory
		deleteTempFiles(sessionID);
	}

	/**
	 * get the basename of the attachments direcory
	 * @return
	 */
	public static String getAttachDirBase() {
		// will set path to save from the runtime env.
		StringBuffer sb=new StringBuffer();
		sb.append(trackDataDirBase());
		/*sb.append(Constants.RealPathAttachment);
		if(Constants.RealPathAttachment!=null&&Constants.RealPathAttachment.charAt(Constants.RealPathAttachment.length()-1)!=File.separatorChar){
			sb.append(File.separator);
		}
		sb.append(DataName);
		sb.append(File.separator);*/
		sb.append(AttachmentsName);
		return sb.toString();
	}

	/**
	 * Gets the basename of the excel imports directory
	 * @return
	 */
	public static String getExcelImportDirBase() {
		// will set path to save from the runtime env.
		StringBuffer sb=new StringBuffer();
		sb.append(trackDataDirBase());
		sb.append(excelImportDir);
		sb.append(File.separator);
		return sb.toString();
	}

	/**
	 * Gets the basename of the excel imports directory
	 * @return
	 */
	public static String getDocxImportDirBase() {
		// will set path to save from the runtime env.
		StringBuffer sb=new StringBuffer();
		sb.append(trackDataDirBase());
		sb.append(docxImportDir);
		sb.append(File.separator);
		return sb.toString();
	}

	/**
	 * Gets the basename of the excel imports directory
	 * @return
	 */
	/*public static String getDocxExportDirBase() {
		// will set path to save from the runtime env.
		StringBuffer sb=new StringBuffer();
		sb.append(trackDataDirBase());
		sb.append(docxExportDir);
		sb.append(File.separator);
		return sb.toString();
	}*/

	/**
	 * Gets the basename of the msProject imports directory
	 * @return
	 */
	public static String getMsProjectImportDirBase() {
		StringBuffer sb=new StringBuffer();
		sb.append(trackDataDirBase());
		sb.append(msProjectImportDir);
		sb.append(File.separator);
		return sb.toString();
	}

	public static String trackDataDirBase() {
		StringBuffer sb=new StringBuffer();
		String trackplusHome=HandleHome.getTrackplus_Home();
		sb.append(trackplusHome);
		if(trackplusHome!=null&&trackplusHome.length()>0&&trackplusHome.charAt(trackplusHome.length()-1)!=File.separatorChar){
			sb.append(File.separator);
		}
		sb.append(HandleHome.DATA_DIR);
		sb.append(File.separator);
		return sb.toString();
	}

	/**
	 * Add attachments for an Item
	 * @param attachList List<File>
	 * @param itemID
	 */
	public static List<Integer> storeEmailAttachments(List<EmailAttachment> attachList, Integer itemID){
		if(attachList==null||attachList.isEmpty()){
			return null;
		}
		List<Integer> result=new ArrayList<Integer>(attachList.size());
		Iterator<EmailAttachment> attachIter = attachList.iterator();
		File newDir = new File(getFullDirName(null, itemID));
		boolean ok = newDir.mkdirs();
		while(attachIter.hasNext()) {
			EmailAttachment emailAttachment=attachIter.next();
			File attachFile = emailAttachment.getFile();
			// get the old filename
			String fileName = attachFile.getName();
			// set the new workItemKey -> attachment is added to the DB
			TAttachmentBean attachBean = new TAttachmentBean();
			// set the properties
			attachBean.setFileName(fileName);
			attachBean.setWorkItem(itemID);
			//attachDbItem.setDescription(description);
			attachBean.setLastEdit(new Date());
			Integer attachID=save(attachBean);
			attachBean.setObjectID(attachID);
			result.add(attachID);
			//ensure the disk file
			boolean parentDirOk=ensureDir(getFullDirName(null,itemID));
			if(!parentDirOk){
				LOGGER.error("Error accessing or creating directory for attachments");
				return null;
			}
			//set the fileNameOnDisk
			attachBean.setFullFileNameOnDisk(getFullFileName(null,attachID, itemID));
			attachBean.setFileNameOnDisk(getFileNameAttachment(attachID, itemID));

			// rename the attachments
			File fileNew = new File(attachBean.getFullFileNameOnDisk());
			ok = attachFile.renameTo(fileNew);
			if(!ok) {
				LOGGER.error("Renaming " + attachFile.getAbsolutePath() +
							 " to " + fileNew.getAbsolutePath() + " failed!");
			}
		}
		return result;
	}

	public static void replaceInlineImagesDescription(Integer workItemID,List<EmailAttachment> attachList, List<Integer> emailAttachmentIDList){
		TWorkItemBean workItemBean=null;
		try{
			workItemBean= ItemBL.loadWorkItem(workItemID);
		}catch (Exception ex){
			return;
		}
		if(workItemBean==null){
			return;
		}
		String originalDescription=workItemBean.getDescription();
		if(originalDescription==null||originalDescription.length()==0){
			return;
		}
		String description=originalDescription;
		description = replaceInlineImagesText(attachList, emailAttachmentIDList, workItemID, description);
		if(!originalDescription.equals(description)){
			workItemBean.setDescription(description);
			try {
				DAOFactory.getFactory().getWorkItemDAO().saveSimple(workItemBean);
			} catch (ItemPersisterException e) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
	}

	public static String replaceInlineImagesText(List<EmailAttachment> attachList, List<Integer> emailAttachmentIDList, Integer workItemID, String description) {
		for(int i=0;i<attachList.size();i++){
			EmailAttachment attachment=attachList.get(i);
			String cid=attachment.getCid();
			//
			String srcTxt="src=\"cid:"+cid+"\"";
			Integer attachKey=emailAttachmentIDList.get(i);
			String srcReplace="src=\"downloadAttachment.action?workItemID="+workItemID+"&attachKey="+ attachKey+"\"";
			if(description.indexOf(srcTxt)!=-1){
				description=description.replaceAll(srcTxt,srcReplace);
			}
		}
		return description;
	}

	public static Integer save(TAttachmentBean tAttachmentBean) {
		return attachmentDAO.save(tAttachmentBean);
	}

	public static boolean isImage(TAttachmentBean attachBean){
		String fileName=attachBean.getFileName();
		String ext=null;
		int idx=fileName.lastIndexOf(".");
		if(idx>0&&idx<fileName.length()-1){
			ext=fileName.substring(idx+1);
		}
		return (ext!=null&&imgExt.contains(ext.toUpperCase()));
	}
	public static Integer getThumbnailHeight(String sessionID, TAttachmentBean attachBean){
		Integer result=null;
		if(isImage(attachBean)){
			boolean b=createTumbnail(sessionID,attachBean);
			if(b){
				String thumbFileName = getFullThumbFileName(sessionID,attachBean);
				Image image = Toolkit.getDefaultToolkit().getImage(thumbFileName);
				return image.getHeight(null);
			}
		}

		return result;
	}
	public static boolean hasTumbnail(String sessionID,TAttachmentBean attach){
		String thumbName = getFullThumbFileName(sessionID,attach);
		File f=new File(thumbName);
		return f.exists();
	}

	public static boolean createTumbnail(String sessionID,TAttachmentBean attach){
		if(hasTumbnail(sessionID,attach)){
			return true;
		}
		String fileName=attach.getFullFileNameOnDisk();
		String thumbFileName = getFullThumbFileName(sessionID,attach);
		return createThumbFile(fileName, thumbFileName,100,100,0);
	}


	/**
	 * @param fileName the file name of the original image
	 * @param thumbFileName the file name of the thumb created from the original
	 * @param thumbWidth
	 * @param thumbHeight
	 * @return
	 */
	public static boolean createThumbFile(String fileName, String thumbFileName,int thumbWidth,int thumbHeight, int cornerRadius) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(fileName));
			// Toolkit.getDefaultToolkit().getImage(fileName);
	    } catch (Exception e) {
	    	// too bad...
	    	return false;
	    }

		BufferedImage newImg = ImageUtils.scaleImage(image,thumbWidth,thumbHeight, cornerRadius);

		File outfile=new File(thumbFileName);

		try {
			ImageIO.write(newImg, "png", outfile);
			return true;
		} catch (IOException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}


	private static boolean hasAlpha(Image image) {
		// If buffered image, the color model is readily available
		if (image instanceof BufferedImage) {
			BufferedImage bimage = (BufferedImage)image;
			return bimage.getColorModel().hasAlpha();
		}

		// Use a pixel grabber to retrieve the image's color model;
		// grabbing a single pixel is usually sufficient
		PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
		}

		// Get the image's color model
		ColorModel cm = pg.getColorModel();
		return cm.hasAlpha();
	}

	private static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage)image;
		}

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Determine if the image has transparent pixels; for this method's
		// implementation, see Determining If an Image Has Transparent Pixels
		boolean hasAlpha = hasAlpha(image);

		// Create a buffered image with a format that's compatible with the screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;
			if (hasAlpha) {
				transparency = Transparency.BITMASK;
			}

			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(
					image.getWidth(null), image.getHeight(null), transparency);
		} catch (HeadlessException e) {
			// The system does not have a screen
		}

		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			if (hasAlpha) {
				type = BufferedImage.TYPE_INT_ARGB;
			}
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		}

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}

	private static String getFullThumbFileName(String sessionID,TAttachmentBean attach) {
		String fullFileName = getFullFileName(sessionID,attach.getObjectID(),attach.getWorkItem());
		return  getFullThumbFileName(fullFileName);
	}

	private static String getFullThumbFileName(String fullFileName) {
		int idxDot=fullFileName.lastIndexOf(".");
		String thumbName=fullFileName.substring(0,idxDot)+"_thumb.png";
		return thumbName;
	}


	//-------------------Mime types
	private static void ensureMimeType(TAttachmentBean attach){
		String mimeType = null;
		if(mimeTypeMap==null){
			createMimeTypeMap(
					ApplicationBean.getApplicationBean().getServletContext()
					.getResourceAsStream("/WEB-INF/mime.types"));
		}
		if(mimeTypeMap != null) {
			mimeType = mimeTypeMap.getContentType(attach.getFileName().toLowerCase());
		}
		if(mimeType == null) {
			// set default mimeType
			mimeType ="text/html";
		}
		LOGGER.debug("MimeType for: "+attach.getFileName()+" is:"+mimeType);
		attach.setMimeType(mimeType);
	}

	/**
	 * create the mime type-map
	 * @param is, stream with fiel-based mime-definitions
	 */
	private static void createMimeTypeMap(InputStream is) {
		if(is != null) {
			mimeTypeMap = new MimetypesFileTypeMap(is);
		} else {
			mimeTypeMap = new MimetypesFileTypeMap();
		}
	}

	/**
	 * Copy the Attachments from the original workItemBean to the copied.
	 * It generates the db and the file information
	 * @param workItemBeanOriginalId
	 * @param workItemBeanCopyId
	 * @param userOrSessionId if it is known the copied id, the userId must be passed
	 * 							else the sessionId
	 * @return list of attachments if the copied id is null or -1, else null
	 * @throws AttachBLException
	 */
	public static List<TAttachmentBean> copyAttachments(Integer workItemBeanOriginalId, Integer workItemBeanCopyId, String userOrSessionId,Integer userID) throws AttachBLException{

		//First all attachments of the original workItem are loaded
		List<TAttachmentBean> attachmentsList = loadByWorkItem(workItemBeanOriginalId);

		List<TAttachmentBean> attachmentsReturn= new ArrayList<TAttachmentBean>();

		Iterator<TAttachmentBean> it = attachmentsList.iterator();

		while(it.hasNext()){
			TAttachmentBean attachOriginal = it.next();

			//verify the file on disk
			if(ensureFileOnDisk(attachOriginal, workItemBeanOriginalId)){
				//ensureMimeType(attachOriginal);
			}else{
				//The file not exist on the disk. Somebody delete it!
				continue;
			}
			try {
				if(workItemBeanCopyId == null || workItemBeanCopyId.intValue()==-1){
					//If it is editCopy, we do not know the copied workItem id, so the attachments must be save in local
					List<TAttachmentBean> attachment = saveLocal(workItemBeanCopyId, attachOriginal.getDescription(), attachOriginal.getFileName(),
								new FileInputStream(new File(attachOriginal.getFullFileNameOnDisk())),
								new ArrayList<TAttachmentBean>(), userOrSessionId.toString(),userID);
					//and the return is prepared because it will be needed by the context
					attachmentsReturn.addAll(attachment);

				} else {
					//else, we know the copied workItem id, so we save it correctly
					save(workItemBeanCopyId, attachOriginal.getDescription(), attachOriginal.getFileName(),
								new FileInputStream(new File(attachOriginal.getFullFileNameOnDisk())),
								Integer.parseInt(userOrSessionId));
				}
			}catch (FileNotFoundException fnfe) {
				LOGGER.debug(ExceptionUtils.getStackTrace(fnfe));
				return null;
			}
		}
		return attachmentsReturn;
	}
	public static Double getMaxAttachmentSizeInMb(ApplicationBean applicationBean){
		TSiteBean siteBean=applicationBean.getSiteBean();
		Double maxAttachmentSizeInMb = siteBean.getMaxAttachmentSize();
		if(maxAttachmentSizeInMb==null){
			maxAttachmentSizeInMb=4d;
		}
		return maxAttachmentSizeInMb;
	}
	public static int getMaxFileSize(ApplicationBean applicationBean){
		TSiteBean siteBean=applicationBean.getSiteBean();
		return getMaxFileSize(siteBean);
	}
	public static int getMaxFileSize(TSiteBean siteBean){
		Double maxAttachmentSizeInMb = siteBean.getMaxAttachmentSize();
		int max = 0;
		if (maxAttachmentSizeInMb==null||maxAttachmentSizeInMb.doubleValue()<=0){
			//default value 4MB
			max = new Double(4.0 * 1024 * 1024).intValue();
		}else{
			max = new Double(maxAttachmentSizeInMb.doubleValue() * 1024 * 1024).intValue();
		}
		return  max;
	}


	public static Integer storeFile(Integer workItemID,Integer personID,String description,Locale locale, Map<String, Object> session, HttpServletResponse response, File file, String fileName, Double maxAttachmentSizeInMb, int MAXFILESIZE,List<LabelValueBean> errors,boolean addToHistory) {
		File f= file;
		LOGGER.debug("upload path for file'"+fileName+"':"+f.getAbsolutePath());
		if( f.length()> MAXFILESIZE){
			String err=getText("item.tabs.attachment.maxLengthExceeded",new String[]{maxAttachmentSizeInMb+""},locale);
			errors.add(new LabelValueBean(err,"theFile"));
			return null;
		}

		FileInputStream is;
		try {
			is = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			LOGGER.error("Error upload file"+f.getAbsolutePath()+" file not found!");
			String err="File not found";
			errors.add(new LabelValueBean(err,"theFile"));
			return null;
		}
		ApplicationBean appBean = ApplicationBean.getApplicationBean();
		if(appBean.isBackupInProgress()){
			String err=getText("item.tabs.attachment.err.backupInProgress",locale);
			try {
				is.close();
			} catch (IOException ioe) {
				LOGGER.error("Unable to close input stream.");
			}
			errors.add(new LabelValueBean(err,"theFile"));
			return null;
		}
		if(workItemID==null/*||workItemID.intValue()==-1*/){
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession httpSession = request.getSession();
			WorkItemContext ctx=(WorkItemContext)session.get("workItemContext");
			if(ctx==null){
				LOGGER.error("No context on session");
				try {
					is.close();
				} catch (IOException ioe) {
					LOGGER.error("Unable to close input stream.");
				}
				String err="No context on session!";
				errors.add(new LabelValueBean(err,"theFile"));
				return null;
			}
			List<TAttachmentBean> attachments=ctx.getAttachmentsList();
			if(attachments==null){
				attachments=new ArrayList<TAttachmentBean>();
			}
			String sessionID=httpSession.getId();
			try {
				attachments= AttachBL.saveLocal(workItemID, description, fileName, is, attachments, sessionID, personID);
			} catch (AttachBLException e) {
				String err="";
				if(e.getLocalizedKey()!=null){
					err= getText(e.getLocalizedKey(),e.getLocalizedParameteres(),locale);
				}else{
					err=e.getMessage();
				}
				errors.add(new LabelValueBean(err,"theFile"));
				return null;
			}
			ctx.setAttachmentsList(attachments);
			f.delete();
			return -1;
		}else{
			//Integer userID=((TPerson) session.get(Constants.USER_KEY)).getObjectID();
			try {
				TAttachmentBean attachmentBean=AttachBL.save(workItemID, description, fileName,is,personID);
				TPersonBean person = LookupContainer.getPersonBean(personID);
				LOGGER.debug("Saving attachment " + fileName + " by user " + person.getFullName());
				//add to history
				if(addToHistory){
					HistorySaverBL.addAttachment(workItemID, personID, locale, fileName, description, Long.valueOf(f.length()), false);
							/*HistorySaverBL.getAttachmentHistoryText(fileName, description, Long.valueOf(f.length()), locale);*/
				}
				f.delete();
				return  attachmentBean.getObjectID();
			} catch (AttachBLException e) {
				LOGGER.error("Can't save attachemnt",e);
				String err="";
				if(e.getLocalizedKey()!=null){
					err=getText(e.getLocalizedKey(),e.getLocalizedParameteres(),locale);
				}else{
					err=e.getMessage();
				}
				errors.add(new LabelValueBean(err,"theFile"));
				return null;
			}
		}
	}

	private static String getText(String s, Locale locale){
		return LocalizeUtil.getLocalizedTextFromApplicationResources(s, locale);
	}
	private static String getText(String key, String[] params, Locale locale) {
		return LocalizeUtil.getParametrizedString(key, params, locale);
	}


}
