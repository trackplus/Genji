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

package com.aurel.track.exchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;

import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;

public class UploadHelper {
	
	static Logger LOGGER = LogManager.getLogger(UploadHelper.class);
	/**
	 * Upload the file in a person and type specific directory
	 * @return
	 */
	public static String upload(File uploadFile, String uploadFileFileName, String targetPath, Locale locale, String successResult) {
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(uploadFile);
		} catch (FileNotFoundException e) {
			LOGGER.error("Getting the input stream for the  uploaded file failed with " + e.getMessage(), e);
			JSONUtility.encodeJSON(ServletActionContext.getResponse(),
					JSONUtility.encodeJSONFailure(LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importTp.err.failed", locale)));
			return null;
		}
		//String importDirectoryForPerson = importDirectory + String.valueOf(personID);
		UploadHelper.ensureDir(targetPath);
		File targetFile = new File(targetPath, uploadFileFileName);
        if (targetFile.exists())  {
        	//if the file exists (as a result of a previous import) then delete it
            targetFile.delete();
        }
		try {
			OutputStream outputStream = new FileOutputStream(targetFile);
			byte[] buf = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0){
				outputStream.write(buf, 0, len);
			}
			inputStream.close();
			outputStream.close();
		} catch (Exception e) {
			LOGGER.error("Saving the file " + uploadFileFileName + " to the temporary directory " + targetPath + " failed with " +e.getMessage(), e);
			JSONUtility.encodeJSON(ServletActionContext.getResponse(),
					JSONUtility.encodeJSONFailure(LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importTp.err.failed", locale)));
			return null;
		}		
		return successResult;
	}
	/**
	 * Ensure that a directory exists
	 * @param dirName
	 * @return
	 */
	public static boolean ensureDir(String dirName){
		File file=new File(dirName);
		if(!file.exists()){
			file.mkdirs();
		}
		return file.exists() && file.isDirectory();
	}	
}
