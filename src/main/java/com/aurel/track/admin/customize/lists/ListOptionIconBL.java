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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.lists.ListBL.RESOURCE_TYPES;
import com.aurel.track.admin.customize.lists.customOption.CustomOptionSimpleSelectBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
/**
 * Helper class for uploading icons
 * @author Tamas
 *
 */
public class ListOptionIconBL {

	private static String BLANK_ICON_PATH = "/"+ Constants.DESIGN_DIRECTORY + "/" + Constants.DEFAULTDESIGNPATH + "/" + "icons" + "/" + "blank.gif";
	
	/**
	 * Saves the uploaded file on the disk and in the database as blob
	 * @param blobID
	 * @param file
	 * @return
	 */
	public static Integer saveFileInDB(Integer blobID, File file) {
		InputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
			return BlobBL.saveFileInDB(blobID, inputStream);
		} catch (Exception e) {
			
		}
		return 0;
	}
	
	/**
	 * Saves the uploaded file on the disk and in the database as blob
	 * @param blobID
	 * @param file
	 * @param width
	 * @param height
	 * @return
	 */
	public static Integer saveScaledImageFileInDB(Integer blobID, File file, int width, int height) {
		blobID = BlobBL.saveThumbImageFileInDB(blobID, file, width, height,0);
		return blobID;
	}
	
	
	/**
	 * Deletes an icon file from the database
	 * @param listID
	 * @param optionID
	 * @param personID
	 * @param locale
	 */
	static void deleteFileFromDB(Integer listID, Integer optionID, 
			Integer personID, Locale locale) {
		Integer iconKey = getIconKey(listID, optionID, locale);
		removeIconKey(listID, optionID, personID, locale);
		if (iconKey!=null) {
			BlobBL.delete(iconKey);
		}
	}
	
	/**
	 * Resets the icon key and symbol in the corresponding bean
	 * @param listID
	 * @param optionID
	 * @param personID
	 * @param locale
	 */
	private static void removeIconKey(Integer listID, Integer optionID, 
			Integer personID, Locale locale) {
		OptionBaseBL optionBaseBL = ListBL.getOptionBaseInstance(listID, optionID);
		ILabelBean labelBean = optionBaseBL.getLocalizedLabelBean(optionID, listID, locale);
		optionBaseBL.setSymbol(labelBean, null);
		optionBaseBL.setIconKey(labelBean, null);
		optionBaseBL.saveSimple(labelBean);
	}
	
	/**
	 * Gets the existing icon key
	 * @param listID
     * @param optionID
	 * @param locale
	 * @return
	 */
	static String getIconFileName(Integer listID, Integer optionID, Locale locale) {
		OptionBaseBL optionBaseBL = ListBL.getOptionBaseInstance(listID, optionID);
		ILabelBean labelBean = optionBaseBL.getLocalizedLabelBean(optionID, listID, locale);
		if (labelBean!=null) {
			return optionBaseBL.getSymbol(labelBean);
		}
		return null;
	}
		
	/**
	 * Gets the existing icon key
	 * @param listID
     * @param optionID
	 * @param locale
	 * @return
	 */
	static Integer getIconKey(Integer listID, Integer optionID, Locale locale) {
		OptionBaseBL optionBaseBL = ListBL.getOptionBaseInstance(listID, optionID);
		ILabelBean labelBean = optionBaseBL.getLocalizedLabelBean(optionID, listID, locale);
		if (labelBean!=null) {
			return optionBaseBL.getIconKey(labelBean);
		}
		return null;
	}
	
	/**
	 * Saves the icon key and symbol in the corresponding bean
	 * @param listID
	 * @param optionID
	 * @param iconKey
	 * @param iconFileFileName
	 * @param personID
	 * @param locale
	 */
	static void saveIconKey(Integer listID, Integer optionID, 
			Integer iconKey, String iconFileFileName, Integer personID, Locale locale) {
		OptionBaseBL optionBaseBL = ListBL.getOptionBaseInstance(listID, optionID);
		ILabelBean labelBean = optionBaseBL.getLocalizedLabelBean(optionID, listID, locale);
		optionBaseBL.setSymbol(labelBean, iconFileFileName);
		optionBaseBL.setIconKey(labelBean, iconKey);
		optionBaseBL.setIconChanged(labelBean, true);
		optionBaseBL.saveSimple(labelBean);
	}
	
	
	/**
	 * Downloads the file based on listId and optionID
	 * @param servletRequest
	 * @param servletResponse
	 * @param listID
	 * @param optionID
	 * @param inline
	 */
	public static void download(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
			Integer listID, Integer optionID, boolean inline, boolean emptyIfMissing) {
		if (listID!=null || optionID!=null) {
			OptionBaseBL optionBaseBL = ListBL.getOptionBaseInstance(listID, optionID);
			ILabelBean labelBean = getLabelBean(listID, optionID);
			String iconName = null;
			Integer iconKey = null;
			if (labelBean!=null) {
				iconName = optionBaseBL.getSymbol(labelBean);
				iconKey = optionBaseBL.getIconKey(labelBean);
			}
			if (emptyIfMissing) {
				BlobBL.downloadIfExist(iconKey, iconName, servletRequest, servletResponse, inline);
			} else {
				BlobBL.download(iconKey, iconName, servletRequest, servletResponse, inline, BLANK_ICON_PATH);
			}
		}
	}
	
	/**
	 * Downloads the file based on fieldID and optionID
	 * @param servletRequest
	 * @param servletResponse
	 * @param fieldID
	 * @param optionID
	 */
	public static void downloadForField(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
			Integer fieldID, Integer optionID) {
		if (optionID!=null) {
			ILabelBean labelBean = getLabelBean(fieldID, optionID);
			OptionBaseBL optionBaseBL = ListBL.getOptionBaseInstance(fieldID, optionID);
			String iconName = null;
			Integer iconKey = null;
			if (labelBean!=null) {
				iconName = optionBaseBL.getSymbol(labelBean);
				iconKey = optionBaseBL.getIconKey(labelBean);
			}
			BlobBL.download(iconKey, iconName, servletRequest, servletResponse, true, BLANK_ICON_PATH);
		}else{
			BlobBL.download(null, null, servletRequest, servletResponse, true, BLANK_ICON_PATH);
		}
	}
	
	/**
	 * Gets the label bean for a fieldID an optionID
	 * @param fieldID
	 * @param optionID
	 * @return
	 */
	private static ILabelBean getLabelBean(Integer fieldID, Integer optionID) {
		ILabelBean labelBean = null;
		Integer systemList = null;
		if (fieldID!=null) {
			switch (fieldID.intValue()) {
			case RESOURCE_TYPES.STATUS:
				systemList = SystemFields.INTEGER_STATE;
				break;
			case RESOURCE_TYPES.PRIORITY:
				systemList = SystemFields.INTEGER_PRIORITY;
				break;
			case RESOURCE_TYPES.SEVERITY:
				systemList = SystemFields.INTEGER_SEVERITY;
				break;
			case RESOURCE_TYPES.ISSUETYPE:
				systemList = SystemFields.INTEGER_ISSUETYPE;
				break;
			}
		}
		if (!MatcherContext.PARAMETER.equals(optionID)) {
			if (systemList!=null) {
				//system option
				labelBean = LookupContainer.getNotLocalizedLabelBean(systemList, optionID);
			} else {
				//custom option
				labelBean = CustomOptionSimpleSelectBL.getInstance().getLabelBean(optionID, fieldID);
			}
		}
		return labelBean;
	}
}
