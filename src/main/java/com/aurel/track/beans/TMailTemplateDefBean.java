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
import java.util.HashMap;
import java.util.Map;

import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.util.EqualUtils;

/**
 * Locale and e-mail type dependent template definitions for notification emails
 *
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TMailTemplateDefBean
    extends com.aurel.track.beans.base.BaseTMailTemplateDefBean
    implements Serializable,ISerializableLabelBean{
	
	/**
	 * Serialize the Bean 
	 * @return
	 */   
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("mailTemplateID", getMailTemplate().toString());
		attributesMap.put("plainMail", Boolean.toString(isPlainEmail()));
		String mailSubject = getMailSubject();
		if (mailSubject!=null && !"".equals(mailSubject)) {
		attributesMap.put("mailSubject", mailSubject);
		}
		String mailBody = getMailBody();
		if (mailBody!=null && !"".equals(mailBody)) {
			attributesMap.put("mailBody", mailBody);	
		}
		String theLocale = getTheLocale();
		if (theLocale!=null && !"".equals(theLocale)) {
			attributesMap.put("theLocale", theLocale);
		}
		String templateChanged = getTemplateChanged();
		if (templateChanged!=null && !"".equals(templateChanged)) {
			attributesMap.put("templateChanged", templateChanged);
		}
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}
	
	//public TMailTemplateDefBean deserializeBean(Map<String, String> attributes) {
	
	/**
	 * Deserialize the Bean 
	 * @param attributes
	 * @return
	 */
	public TMailTemplateDefBean deserializeBean(Map<String, String> attributes) {
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			this.setObjectID(new Integer(strObjectID));
		}
		this.setMailTemplate(Integer.parseInt(attributes.get("mailTemplateID")));
		this.setIsPlainEmailBool(Boolean.parseBoolean(attributes.get("plainMail")));
		this.setMailSubject(attributes.get("mailSubject"));
		this.setMailBody(attributes.get("mailBody"));
		this.setTheLocale(attributes.get("theLocale"));
		this.setTemplateChanged(attributes.get("templateChanged"));
		this.setUuid(attributes.get("uuid"));
		return this;
	}

	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
						  Map<String, Map<Integer, Integer>> matchesMap){
		TMailTemplateDefBean  internalMailTemplateDefBean=(TMailTemplateDefBean)serializableLabelBean;
		boolean result=false;
		if (internalMailTemplateDefBean != null) {
			String externalUuid = getUuid();
			String internalUuid = internalMailTemplateDefBean.getUuid();
			if(EqualUtils.equalStrict(externalUuid,internalUuid)){
				result=true;
			}else{
				String externalPlainEmail=getPlainEmail();
				String externalLocale=getTheLocale();
				String internalPlainEmail=internalMailTemplateDefBean.getPlainEmail();
				String internalLocale=internalMailTemplateDefBean.getTheLocale();
				Integer externalMailTemplateID=getMailTemplate();
				Integer internalMailTemplateID=internalMailTemplateDefBean.getMailTemplate();
				result=EqualUtils.equal(externalPlainEmail,internalPlainEmail)&&
						EqualUtils.equal(externalLocale,internalLocale)&&
						EqualUtils.equal(externalMailTemplateID,internalMailTemplateID);
			}
		}
		return result;
	}

	public Integer saveBean(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		return null;
	}

	public String getLabel(){
		return getMailSubject();
	}

	public boolean isPlainEmail() {
		return BooleanFields.TRUE_VALUE.equals(getPlainEmail());
	}
	public void setIsPlainEmailBool(boolean isPlainEmail) {
		setPlainEmail(BooleanFields.fromBooleanToString(isPlainEmail));
	}
}
