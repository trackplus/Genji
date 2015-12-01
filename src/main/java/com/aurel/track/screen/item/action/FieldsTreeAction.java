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

package com.aurel.track.screen.item.action;

import java.util.*;

import com.aurel.track.admin.customize.treeConfig.field.FieldDesignBL;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.fieldType.types.FieldType;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.resources.LocalizeUtil;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.screen.item.TreeNodeField;
import com.aurel.track.screen.item.bl.design.ScreenFieldDesignBL;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * A action used to list the fields used in screen designer
 * @author Adrian Bojani
 *
 */
public class FieldsTreeAction extends ActionSupport implements Preparable, SessionAware {

	private static final long serialVersionUID = 340L;
	//private List systemFields;
	//private List customFields; 
	private Map<String, Object> session;	
	@Override
	public void prepare() throws Exception {
	}
	/**
	 * Obtain the fields: system and custom fields
	 * @return
	 */
	public String list(){
		Locale locale = (Locale)session.get(Constants.LOCALE_KEY);
		List<TreeNodeField> systemFields=new ArrayList<TreeNodeField>();
		List<TreeNodeField> customFields=new ArrayList<TreeNodeField>();

		ScreenFieldDesignBL.getInstance().loadFields(systemFields, customFields, locale);

		JSONUtility.encodeJSON(ServletActionContext.getResponse(),
				FileldsTreeJSON.encodeFieldList(systemFields, customFields, locale));
		return null;
	}
	public String listExtraFields(){
		Locale locale = (Locale)session.get(Constants.LOCALE_KEY);
		List<TreeNodeField> systemFields=new ArrayList<TreeNodeField>();
		List<TreeNodeField> customFields=new ArrayList<TreeNodeField>();

		ScreenFieldDesignBL.getInstance().loadFields(systemFields, customFields, locale);

		List<TreeNodeField> extraFields=new ArrayList<TreeNodeField>();
		Integer[] pseudoFields=new Integer[]{
			TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_COST,
			TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME,

			TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_COST,
			TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_TIME,

			TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_COST,
			TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_TIME,

			TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_COST,
			TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_TIME,

			TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_COST,
			TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_TIME,

		};
		for(int i=0;i<pseudoFields.length;i++) {
			TreeNodeField treeNodeField = new TreeNodeField();
			treeNodeField.setObjectID(pseudoFields[i]);
			treeNodeField.setName(TReportLayoutBean.getPseudoColumnName(pseudoFields[i]));
			String key=TReportLayoutBean.getPseudoColumnLabel(pseudoFields[i]);
			treeNodeField.setLabel(LocalizeUtil.getLocalizedTextFromApplicationResources(key,locale));
			//treeNodeField.setDescription(fieldBean.getDescription());
			treeNodeField.setImg(FieldDesignBL.ICON_CLS.LABEL_ICONCLS);

			extraFields.add(treeNodeField);
		}
		Collections.sort(extraFields);

		JSONUtility.encodeJSON(ServletActionContext.getResponse(),
				FileldsTreeJSON.encodeFieldList(systemFields, customFields,extraFields, locale));
		return null;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
