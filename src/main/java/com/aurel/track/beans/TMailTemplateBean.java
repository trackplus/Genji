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


package com.aurel.track.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.MailTemplateDAO;
import com.aurel.track.util.EqualUtils;

/**
 * Templates for notification emails
 *
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TMailTemplateBean
    extends com.aurel.track.beans.base.BaseTMailTemplateBean
    implements Serializable,ILabelBean,ISerializableLabelBean {

	MailTemplateDAO mailTemplateDAO = DAOFactory.getFactory().getMailTemplateDAO();
	
	/**
	 * Serialize the Bean 
	 * @return
	 */   
	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		String mailSubject = getName();
		if (mailSubject!=null && !"".equals(mailSubject)) {
		attributesMap.put("name", mailSubject);	
		}
		String description = getDescription();
		if (description!=null && !"".equals(description)) {
			attributesMap.put("description", description);	
		}
		String tagLabel = getTagLabel();
		if (tagLabel!=null && !"".equals(tagLabel)) {
			attributesMap.put("tagLabel", tagLabel);
		}
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}
	
	/**
	 * Deserialize the Bean 
	 * @param attributes
	 * @return
	 */

	@Override
	public TMailTemplateBean deserializeBean(Map<String, String> attributes) {
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			this.setObjectID(new Integer(strObjectID));
		}
		this.setName(attributes.get("name"));
		this.setDescription(attributes.get("description"));
		this.setTagLabel(attributes.get("tagLabel"));
		this.setUuid(attributes.get("uuid"));
		return this;
	}


	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
						   Map<String, Map<Integer, Integer>> matchesMap){
		TMailTemplateBean internalMailTemplateBean=(TMailTemplateBean)serializableLabelBean;
		if (internalMailTemplateBean == null) {
			return false;
		}
		
		String externalUuid = getUuid();
		String internalUuid = internalMailTemplateBean.getUuid();
			
		return EqualUtils.equalStrict(externalUuid,internalUuid);
	}

	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean,
							Map<String, Map<Integer, Integer>> matchesMap){
		TMailTemplateBean internalMailTemplateBean=(TMailTemplateBean)serializableLabelBean;
		return mailTemplateDAO.save(internalMailTemplateBean);
	}

	@Override
	public String getLabel() {
		return getName();
	}
}
