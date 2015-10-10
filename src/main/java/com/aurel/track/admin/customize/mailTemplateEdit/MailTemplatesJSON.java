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

package com.aurel.track.admin.customize.mailTemplateEdit;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aurel.track.admin.user.profile.ProfileBL.EMAIL_TYPE;
import com.aurel.track.beans.TMailTemplateBean;
import com.aurel.track.beans.TMailTemplateDefBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.LabelValueBean;

public class MailTemplatesJSON {
	
	static interface JSON_FIELDS {
		static final String SUBJECT = "mailSubject";
		static final String BODY = "mailBody";
		static final String EMAIL_TYPE = "emailType";
		static final String PLAIN_MAIL = "plainMail";
		static final String TEMPLATE_TYPE = "templateType";
		static final String TEMPLATE_TYPE_LABEL = "templateTypeLabel";
		static final String TEMPLATE_TYPE_LIST = "templateTypeList";
		static final String LOCALE = "theLocale";
		static final String LOCALE_LABEL = "localeLabel";
		static final String LOCALE_LIST = "localeList";		
	}

	//restrict constructor of this utility class
	private MailTemplatesJSON(){
	}

	public  static String encodeJSONMailTemplateList(List<TMailTemplateBean> mailTemplates){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		for (Iterator<TMailTemplateBean> iterator = mailTemplates.iterator(); iterator.hasNext();) {
			TMailTemplateBean mailTemplateBean = iterator.next();
			sb.append("{");
			JSONUtility.appendStringValue(sb,"name",mailTemplateBean.getName());
			JSONUtility.appendStringValue(sb,"tagLabel",mailTemplateBean.getTagLabel());
			JSONUtility.appendStringValue(sb,"description",mailTemplateBean.getDescription());
			JSONUtility.appendIntegerValue(sb, "id", mailTemplateBean.getObjectID(),true);
			sb.append("}");
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public static String encodeMailTemplateJSON(TMailTemplateBean mailTemplateBean){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb,"name",mailTemplateBean.getName());
		JSONUtility.appendStringValue(sb,"tagLabel",mailTemplateBean.getTagLabel());
		JSONUtility.appendStringValue(sb,"description",mailTemplateBean.getDescription());
		JSONUtility.appendIntegerValue(sb,"id",mailTemplateBean.getObjectID(),true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}


	/**
	 * Creates the JSON string list for locale grid rows lists
	 * @return
	 */
	public static String encodeMailTemplateDefListJSON(List<TMailTemplateDefBean> mailTemplateDefs,
											 Map<String, String> localeMap) {

		StringBuilder sb=new StringBuilder();

		sb.append("[");
		for (Iterator<TMailTemplateDefBean> iterator = mailTemplateDefs.iterator(); iterator.hasNext();) {
			TMailTemplateDefBean mailTemplateDefBean = iterator.next();
			sb.append("{");
			JSONUtility.appendStringValue(sb, JSON_FIELDS.SUBJECT, mailTemplateDefBean.getMailSubject());
			boolean isPlain = mailTemplateDefBean.isPlainEmail();
			String emailType = null;
			if (isPlain) {
				emailType = EMAIL_TYPE.PLAIN;
			} else {
				emailType = EMAIL_TYPE.HTML;
			}
			JSONUtility.appendStringValue(sb,JSON_FIELDS.EMAIL_TYPE,emailType);
			JSONUtility.appendStringValue(sb, JSON_FIELDS.LOCALE, mailTemplateDefBean.getTheLocale());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.LOCALE_LABEL, localeMap.get(mailTemplateDefBean.getTheLocale()));
			JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, mailTemplateDefBean.getObjectID(), true);
			sb.append("}");
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Encodes the MOTD into a JSON string,
	 * @return the JSON encoded MOTD
	 */
	public static String encodeMailTemplateDefJSON(TMailTemplateDefBean mailTemplateDefBean,
			List<LabelValueBean> localeList){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
		if (mailTemplateDefBean!=null) {
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.PLAIN_MAIL, mailTemplateDefBean.isPlainEmail());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.LOCALE, mailTemplateDefBean.getTheLocale());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.SUBJECT, mailTemplateDefBean.getMailSubject());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.BODY, mailTemplateDefBean.getMailBody());
			JSONUtility.appendIntegerValue(sb, "id", mailTemplateDefBean.getObjectID());
		}
		JSONUtility.appendLabelValueBeanList(sb, JSON_FIELDS.LOCALE_LIST , localeList,true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
}
