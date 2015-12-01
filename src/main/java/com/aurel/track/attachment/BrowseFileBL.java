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

package com.aurel.track.attachment;

import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.item.history.HistorySaverBL;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Locale;

public class BrowseFileBL {
	private static final Logger LOGGER = LogManager.getLogger(BrowseFileBL.class);
	public static Integer storeImage(Integer workItemID,TPersonBean person,Locale locale, String imageData,boolean addToHistory){
		String description="";
		String fileName="image.jpg";
		Integer attachKey=null;
		if(imageData!=null) {
			byte[] bytearray = new Base64().decode(imageData);
			InputStream is = new ByteArrayInputStream(bytearray);
			try {
				TAttachmentBean attachmentBean=AttachBL.save(workItemID, description, fileName,is,person.getObjectID());
				attachKey=attachmentBean.getObjectID();
				if(addToHistory){
					//add to history
					HistorySaverBL.addAttachment(workItemID, person.getObjectID(), locale, fileName, description, Long.valueOf(bytearray.length), false);
				}
			} catch (AttachBLException e) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return attachKey;
	}
}
