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

package com.aurel.track.admin.customize.lists;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TBLOBBean;
import com.aurel.track.dao.BlobDAO;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.DownloadUtil;

/**
 * BLOB utilities for storing and retrieving icons for lists
 * 
 * @author Tamas
 * 
 */
public class BlobBL {
	private static BlobDAO blobDAO = DAOFactory.getFactory().getBlobDAO();
	private static final Logger LOGGER = LogManager.getLogger(BlobBL.class);
	private static int BUFFER_SIZE = 8192;

	/**
	 * Gets a BLOBBean by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	public static TBLOBBean loadByPrimaryKey(Integer objectID) {
		return blobDAO.loadByPrimaryKey(objectID);
	}

	/**
	 * Saves a BLOBBean
	 * 
	 * @param blobBean
	 * @return
	 */
	public static Integer save(TBLOBBean blobBean) {
		return blobDAO.save(blobBean);
	}
	public static List<TBLOBBean> loadAll() {
		return blobDAO.loadAll();
	}


	/**
	 * Deletes a BLOBBean by primary key
	 * 
	 * @param objectID
	 */
	public static void delete(Integer objectID) {
		blobDAO.delete(objectID);
	}

	/**
	 * Saves the uploaded file on the disk and in the database as blob
	 * 
	 * @param blobID
	 * @param inputStream
	 * @return
	 */
	public static Integer saveFileInDB(Integer blobID, InputStream inputStream) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			TBLOBBean blobBean = null;
			if (blobID != null) {
				blobBean = loadByPrimaryKey(blobID);
			}
			if (blobBean == null) {
				blobBean = new TBLOBBean();
			}
			// retrieve the file data
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = 0;
			while ((bytesRead = inputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
				byteArrayOutputStream.write(buffer, 0, bytesRead);
			}
			blobBean.setBLOBValue(byteArrayOutputStream.toByteArray());
			blobID = save(blobBean);
		} catch (FileNotFoundException fnfe) {
			LOGGER.error("Storing the iconFile on disk failed with FileNotFoundException", fnfe);
		} catch (IOException ioe) {
			LOGGER.error("Storing the attachment on disk failed: IOException", ioe);
		} finally {
			// flush and close the streams
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
			if (byteArrayOutputStream != null) {
				try {
					byteArrayOutputStream.flush();
					byteArrayOutputStream.close();
				} catch (Exception e) {
				}
			}
		}
		return blobID;
	}

	public static Integer saveThumbImageFileInDB(Integer iconKey, File iconFile,int width, int height, int cornerRadius) {
		String thumbFileName=iconFile.getAbsolutePath()+"_tummb.png";
		boolean thumbOk= AttachBL.createThumbFile(iconFile.getAbsolutePath(), thumbFileName, width, height, cornerRadius);
		if(thumbOk){
			try{
				InputStream inputStream = new BufferedInputStream(new FileInputStream(thumbFileName));
				iconKey = saveFileInDB(iconKey, inputStream);
			}catch (Exception ex){
				LOGGER.error("Error save file:"+thumbFileName);
			}
		}else{
			try{
				InputStream inputStream = new BufferedInputStream(new FileInputStream(iconFile));
				iconKey = saveFileInDB(iconKey, inputStream);
			}catch (Exception ex){
				LOGGER.error("Error save file:"+iconFile);
			}
		}
		return iconKey;
	}

	/**
	 * Download the icon if exists
	 * @param iconKey
	 * @param iconName
	 * @param request
	 * @param reponse
	 * @param inline
	 */
	public static void downloadIfExist(Integer iconKey, String iconName,
			HttpServletRequest request, HttpServletResponse reponse,
			boolean inline) {
		byte[] imageContent = null;
		if (iconKey!=null) {
			TBLOBBean blobBean = loadByPrimaryKey(iconKey);
			if (blobBean != null) {
				imageContent = blobBean.getBLOBValue();
			}
		}
		if (imageContent != null) {
			download(imageContent, request, reponse, iconName, inline);
		}
	}
	
	/**
	 * This method returns the image content in byte array for requested iconKey 
	 * @param iconKey
	 * @param iconName
	 * @return
	 */
	public static byte[] getIfExist(Integer iconKey, String iconName) {
		byte[] imageContent = null;
		if (iconKey!=null) {
			TBLOBBean blobBean = loadByPrimaryKey(iconKey);
			if (blobBean != null) {
				imageContent = blobBean.getBLOBValue();
			}
		}
		if (imageContent != null) {
			return imageContent;
		}		
		return null;
	}

	/**
	 * Download the icon if exists. If there is no icon set then the default
	 * blank icon will be rendered
	 * 
	 * @param iconKey
	 * @param iconName
	 * @param request
	 * @param reponse
	 * @param inline
	 */
	public static void download(Integer iconKey, String iconName,
			HttpServletRequest request, HttpServletResponse reponse,
			boolean inline, String defaultIconPath) {
		byte[] imageContent = null;
		if (iconKey != null) {
			TBLOBBean blobBean = loadByPrimaryKey(iconKey);
			if (blobBean != null) {
				imageContent = blobBean.getBLOBValue();
			}
		}
		if (defaultIconPath!=null) {
			if (imageContent==null||imageContent.length==0) {
				try {
					URL defaultIconURL = ApplicationBean.getApplicationBean().getServletContext().getResource(defaultIconPath);
					imageContent = IOUtils.toByteArray(defaultIconURL.openStream());
				} catch (IOException e) {
					LOGGER.info("Getting the icon content for " + defaultIconPath + " from URL failed with " + e.getMessage(), e);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
				}
			}
			if (imageContent==null||imageContent.length==0) {
				InputStream is = ApplicationBean.getApplicationBean().getServletContext().getResourceAsStream(defaultIconPath);
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				try {
					byte[] buf = new byte[1024];
					int numBytesRead;
					while ((numBytesRead = is.read(buf)) != -1) {
						output.write(buf, 0, numBytesRead);
					}
					imageContent = output.toByteArray();
					output.close();
					is.close();
				} catch (IOException e) {
					LOGGER.info("Getting the icon content for " + defaultIconPath + " as resource stream failed with " + e.getMessage(), e);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
				}
			}
		}
		if (imageContent != null) {
			download(imageContent, request, reponse, iconName, inline);
		}
	}
	
	/**
	 * This method returns the iconKey content in byte array.In case of not founds returns the default image content. 
	 * @param iconKey
	 * @param iconName
	 * @param inline
	 * @param defaultIconPath
	 * @return
	 */
	public static byte[] getAvatarInByteArray(Integer iconKey, String iconName,			
			boolean inline, String defaultIconPath) {
		byte[] imageContent = null;
		if (iconKey != null) {
			TBLOBBean blobBean = loadByPrimaryKey(iconKey);
			if (blobBean != null) {
				imageContent = blobBean.getBLOBValue();
			}
		}
		if (defaultIconPath!=null) {
			if (imageContent==null||imageContent.length==0) {
				try {
					URL defaultIconURL = ApplicationBean.getApplicationBean().getServletContext().getResource(defaultIconPath);
					imageContent = IOUtils.toByteArray(defaultIconURL.openStream());
				} catch (IOException e) {
					LOGGER.info("Getting the icon content for " + defaultIconPath + " from URL failed with " + e.getMessage(), e);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
				}
			}
			if (imageContent==null||imageContent.length==0) {
				InputStream is = ApplicationBean.getApplicationBean().getServletContext().getResourceAsStream(defaultIconPath);
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				try {
					byte[] buf = new byte[1024];
					int numBytesRead;
					while ((numBytesRead = is.read(buf)) != -1) {
						output.write(buf, 0, numBytesRead);
					}
					imageContent = output.toByteArray();
					output.close();
					is.close();
				} catch (IOException e) {
					LOGGER.info("Getting the icon content for " + defaultIconPath + " as resource stream failed with " + e.getMessage(), e);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
				}
			}
		}		
		return imageContent;		
	}
	

	/**
	 * Download the icon content
	 * 
	 * @param iconBytes
	 * @param request
	 * @param reponse
	 * @param fileName
	 * @param inline
	 */
	public static void download(byte[] iconBytes, HttpServletRequest request,
			HttpServletResponse reponse, String fileName, boolean inline) {
		prepareResponse(request, reponse, fileName,
				Long.toString(iconBytes.length), inline);
		// open the file
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			// retrieve the file data
			inputStream = new BufferedInputStream(new ByteArrayInputStream(
					iconBytes));
			outputStream = reponse.getOutputStream();
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = 0;
			while ((bytesRead = inputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		} catch (FileNotFoundException fnfe) {
			LOGGER.error("FileNotFoundException thrown " + fnfe.getMessage(), fnfe);
			LOGGER.debug(ExceptionUtils.getStackTrace(fnfe));
			return;
		} catch (Exception ioe) {
			LOGGER.error("Creating the input stream failed with  "
					+ ioe.getMessage(), ioe);
			LOGGER.debug(ExceptionUtils.getStackTrace(ioe));
			return;
		} finally {
			// flush and close the streams
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
			if (outputStream != null) {
				try {
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * Prepares the response
	 * 
	 * @param request
	 * @param response
	 * @param fileName
	 * @param fileLength
	 * @param inline
	 */
	private static void prepareResponse(HttpServletRequest request,
			HttpServletResponse response, String fileName, String fileLength,
			boolean inline) {
		String contentType = null;
		if (fileName != null) {
			int mid = fileName.lastIndexOf(".");
			if (mid != -1) {
				String ext = fileName.substring(mid + 1, fileName.length());
				if (ext != null) {
					contentType = "image/" + ext.toLowerCase();
					response.setHeader("Content-Type", contentType);
				}
			}
		}
		DownloadUtil.prepareResponse(request, response, fileName, contentType,
				fileLength, inline);
	}
}
