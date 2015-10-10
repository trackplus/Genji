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

package com.aurel.track.admin.user.avatar;

import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.lists.BlobBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;

public class AvatarBL {
	
	private static String DEFALUT_AVATAR_PATH = "/"+Constants.DESIGN_DIRECTORY + "/" + Constants.DEFAULTDESIGNPATH + "/icons/avatar.png";
	
	/**
	 * Gets the avatar download URL
	 * @param personID
	 * @param iconKey
	 * @param emptyIfMissing
	 * @return
	 */
	public static String getAvatarDownloadURL(Integer personID, Integer iconKey, boolean emptyIfMissing) {
		return "avatar!download.action?personID="+personID+"&iconKey="+iconKey+"&emptyIfMissing="+emptyIfMissing+"&time="+new Date().getTime();
	}

	/**
	 * Download the avatar ogf a person
	 * @param servletRequest
	 * @param servletResponse
	 * @param personID
	 * @param iconKey
	 * @param emptyIfMissing no content if true, default icon if false
	 */
	public static void downloadAvatar(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Integer personID,  Integer iconKey, boolean emptyIfMissing) {
		String iconName = null;
		if (personID!=null) {
			TPersonBean personBean = LookupContainer.getPersonBean(personID);
			if (personBean!=null) {
				iconName = personBean.getSymbol();
				iconKey = personBean.getIconKey();
			}
		}
		if (emptyIfMissing) {
			BlobBL.downloadIfExist(iconKey, iconName, servletRequest, servletResponse, true);
		} else {
			BlobBL.download(iconKey, iconName, servletRequest, servletResponse, true, DEFALUT_AVATAR_PATH);
		}
	}
	
	
	public static byte[] getAvatarInByteArray(Integer personID,  Integer iconKey) {
		String iconName = null;
		if (personID!=null) {
			TPersonBean personBean = LookupContainer.getPersonBean(personID);
			if (personBean!=null) {
				iconName = personBean.getSymbol();
				iconKey = personBean.getIconKey();
			}
		}
		byte[] tmp;
		tmp = BlobBL.getAvatarInByteArray(iconKey, iconName, true, DEFALUT_AVATAR_PATH);
		return tmp;
	}
	
	/**
	 * Gets the icon name for person
	 * @param personID
	 * @return
	 */
	static String getIconNameForPerson(Integer personID) {
		TPersonBean personBean = LookupContainer.getPersonBean(personID);
		if (personBean!=null) {
			return personBean.getSymbol();
		}
		return null;
	}
	
	/**
	 * Gets the icon key for person
	 * @param personID
	 * @return
	 */
	static Integer getIconKeyForPerson(Integer personID) {
		TPersonBean personBean = LookupContainer.getPersonBean(personID);
		if (personBean!=null) {
			return personBean.getIconKey();
		}
		return null;
	}
	
	/**
	 * Deletes an icon from person and the corresponding blob
	 * @param personID
	 */
	static void deleteFileFromDB(
			Integer personID) {
		TPersonBean personBean = LookupContainer.getPersonBean(personID);
		if (personBean!=null) {
			Integer iconKey = personBean.getIconKey();
			String symbol = personBean.getSymbol();
			if (iconKey!=null || symbol!=null) {
				personBean.setSymbol(null);
				personBean.setIconKey(null);
				PersonBL.saveSimple(personBean);
				if (iconKey!=null) {
					BlobBL.delete(iconKey);
				}
			}
		}
	}
	
	public static Map<Integer, String>getAvatarsCheckSum(Set<Integer>personIDs) {
		Map<Integer, String>personIDToCheckSum = new HashMap<Integer, String>();
		for (Integer  id: personIDs) {
			Integer iconKey = Integer.valueOf(-1);
			byte[] oneAvatar = AvatarBL.getAvatarInByteArray(id, iconKey);			
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] thedigest = md.digest(oneAvatar);
				String checksum =  DatatypeConverter.printBase64Binary(thedigest);
				personIDToCheckSum.put(id, checksum);
			}catch(Exception ex){}				
		}
		return personIDToCheckSum;
	}
	
}
