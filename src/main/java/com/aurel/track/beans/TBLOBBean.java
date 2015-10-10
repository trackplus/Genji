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

import com.aurel.track.admin.customize.lists.BlobBL;
import org.apache.commons.codec.binary.Base64;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TBLOBBean
    extends com.aurel.track.beans.base.BaseTBLOBBean
	implements Serializable, ISerializableLabelBean {
	public static final long serialVersionUID = 400L;

	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		byte[] imageContent = getBLOBValue();
		if(imageContent!=null){
			String imageData = new Base64().encodeAsString(imageContent);
			attributesMap.put("imageData", imageData);
		}
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}

	@Override
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TBLOBBean blobBean = new TBLOBBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			blobBean.setObjectID(new Integer(strObjectID));
		}
		String imageData = attributes.get("imageData");
		if (imageData!=null) {
			byte[] byteArray = new Base64().decode(imageData);
			blobBean.setBLOBValue(byteArray);
		}
		blobBean.setUuid(attributes.get("uuid"));
		return blobBean;
	}

	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		if (serializableLabelBean==null) {
			return false;
		}
		TBLOBBean blobBean = (TBLOBBean)serializableLabelBean;
		String externalUID=blobBean.getUuid();
		String internalUID = getUuid();
		if (externalUID!=null && internalUID!=null){
			return externalUID.equals(internalUID);
		}
		return false;
	}

	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		return BlobBL.save((TBLOBBean) serializableLabelBean);
	}

	@Override
	public String getLabel() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

}
