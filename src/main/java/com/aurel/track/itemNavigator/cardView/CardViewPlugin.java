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

package com.aurel.track.itemNavigator.cardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.beans.*;
import com.aurel.track.itemNavigator.BaseIssueListViewPlugin;
import com.aurel.track.itemNavigator.ItemNavigatorBL;
import com.aurel.track.itemNavigator.QueryContext;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.lists.customOption.OptionBL;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.admin.customize.treeConfig.field.CustomListsConfigBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.massOperation.MassOperationBL;
import com.aurel.track.item.massOperation.MassOperationContext;
import com.aurel.track.item.massOperation.MassOperationFieldsBL;
import com.aurel.track.itemNavigator.layout.column.ColumnFieldsBL;
import com.aurel.track.itemNavigator.layout.group.GroupFieldTO;
import com.aurel.track.itemNavigator.layout.group.SortFieldTO;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.report.ReportItemJSON;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanExpandContext;
import com.aurel.track.report.execute.ReportBeansBL;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.screen.card.bl.runtime.CardScreenRuntimeJSON;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.IntegerStringBooleanBean;
import com.aurel.track.util.PropertiesHelper;

/**
 */
public class CardViewPlugin extends BaseIssueListViewPlugin {
	private static final Logger LOGGER = LogManager.getLogger(CardViewPlugin.class);
	public CardViewPlugin(){
	}
	@Override
	public ReportBeanExpandContext updateReportBeanExpandContext(TPersonBean personBean, List<ReportBean> reportBeanList,
			ReportBeanExpandContext reportBeanExpandContext, Map session, QueryContext queryContext, boolean forcePersonLayout) {
		TCardGroupingFieldBean cardGroupingFieldBean= CardViewBL.getActiveGroupingField(personBean, queryContext.getQueryID(), queryContext.getQueryType(), forcePersonLayout);
		Integer groupField=cardGroupingFieldBean.getCardField();
		if(groupField==null){
			groupField=SystemFields.INTEGER_STATE;
		}
		List<TReleaseBean> releases=CardViewBL.getReleases(reportBeanList);
		boolean useRelease=false;
		if(releases!=null && !releases.isEmpty()){
			useRelease=true;
		}else{
			useRelease=false;
		}
		if(groupField.intValue()==SystemFields.RELEASESCHEDULED && useRelease==false){
			groupField=SystemFields.INTEGER_STATE;
		}
		List<GroupFieldTO> groupByList=new ArrayList<GroupFieldTO>();
		groupByList.add(new GroupFieldTO(groupField, false, false));
		ReportBeanExpandContext result=new ReportBeanExpandContext();
		result.setGroupBy(groupByList);
		SortFieldTO sortFieldTO = new SortFieldTO(cardGroupingFieldBean.getSortField());
		sortFieldTO.setDescending(cardGroupingFieldBean.isDescending());
		result.setSortFieldTO(sortFieldTO);
		return result;
	}
	@Override
	public String getExtraJSON(Map<String, Object> session, List<ReportBean> reportBeanList, QueryContext queryContext, boolean forcePersonLayout) {
		Locale locale = (Locale) session.get(Constants.LOCALE_KEY);
		TPersonBean personBean = (TPersonBean) session.get(Constants.USER_KEY);

		TCardGroupingFieldBean cardGroupingFieldBean=CardViewBL.getActiveGroupingField(personBean,queryContext.getQueryID(),queryContext.getQueryType(), forcePersonLayout);


		MassOperationContext massOperationContext = MassOperationBL.initMassOperationContextByReportBeans(reportBeanList, null, null);
		Set<Integer> presentFieldsOnFormsSet = MassOperationFieldsBL.getPresentFieldsOnFormsSet(massOperationContext, personBean);
		List<Integer> customListFieldIDs = MassOperationFieldsBL.getCustomListFieldIDs(presentFieldsOnFormsSet, false, false);
		Map<Integer, Integer> fieldIDToListID= CardViewBL.getMostSpecificLists(customListFieldIDs,massOperationContext);

		boolean useRelease=false;
		List<TReleaseBean> releases = null;
		if (presentFieldsOnFormsSet.contains(SystemFields.INTEGER_RELEASESCHEDULED)) {
			releases=CardViewBL.getReleases(reportBeanList);
			useRelease=releases!=null&&!releases.isEmpty();
		}

		SortByTO sortByTO=CardViewBL.getSortByTO(personBean,locale,cardGroupingFieldBean);
		GroupByTO groupByTO=CardViewBL.getGroupByTO(reportBeanList, cardGroupingFieldBean, presentFieldsOnFormsSet,useRelease, customListFieldIDs, locale);
		List<GroupTO> groups=CardViewBL.getAllGroups(session,cardGroupingFieldBean,reportBeanList,releases, fieldIDToListID);
		Integer[] selectedGroups=CardViewBL.getSelectedGroups(cardGroupingFieldBean,groups);

		Integer maxSelectionCount=null;
		if(groups!=null&&groups.size()>CardViewBL.MAX_COLUMNS){
			maxSelectionCount=CardViewBL.MAX_COLUMNS;
		}

		TCardPanelBean cardPanelBean=CardViewBL.loadCardTemplate(personBean);

		CardViewTO cardViewTO=new CardViewTO();
		cardViewTO.setCardGroupingFieldID(cardGroupingFieldBean.getObjectID());
		cardViewTO.setCardPanelBean(cardPanelBean);
		cardViewTO.setGroupByTO(groupByTO);
		cardViewTO.setSortByTO(sortByTO);
		cardViewTO.setGroups(groups);
		cardViewTO.setSelectedGroups(selectedGroups);
		cardViewTO.setMaxSelectionCount(maxSelectionCount);

		String s=CardViewJSON.encodeCardView(cardViewTO);
		return s;
	}

	@Override
	public Set<Integer> getExclusiveShortFields(TPersonBean personBean, QueryContext queryContext){
		Set<Integer> result=new HashSet<Integer>();
		Integer cardPanelID = CardViewBL.getCardPanelBeanID(personBean);
		List<TCardFieldBean>  fieldsOnCard=CardViewBL.loadFiledsByPanel(cardPanelID);
		Integer fieldID;
		for(TCardFieldBean fieldBean:fieldsOnCard){
			fieldID=fieldBean.getField();
			result.add(fieldID);
			switch (fieldID.intValue()){
				case SystemFields.PRIORITY:{
					result.add(TReportLayoutBean.PSEUDO_COLUMNS.PRIORITY_SYMBOL);
					break;
				}
				case SystemFields.SEVERITY:{
					result.add(TReportLayoutBean.PSEUDO_COLUMNS.SEVERITY_SYMBOL);
					break;
				}
				case SystemFields.STATE:{
					result.add(TReportLayoutBean.PSEUDO_COLUMNS.STATUS_SYMBOL);
					break;
				}
				case SystemFields.ISSUETYPE:{
					result.add(TReportLayoutBean.PSEUDO_COLUMNS.ISSUETYPE_SYMBOL);
					break;
				}
				case SystemFields.ORIGINATOR:{
					result.add(TReportLayoutBean.PSEUDO_COLUMNS.ORIGINATOR_SYMBOL);
					break;
				}
				case SystemFields.MANAGER:{
					result.add(TReportLayoutBean.PSEUDO_COLUMNS.MANAGER_SYMBOL);
					break;
				}
				case SystemFields.RESPONSIBLE:{
					result.add(TReportLayoutBean.PSEUDO_COLUMNS.RESPONSIBLE_SYMBOL);
					break;
				}


			}
		}
		//ensure description
		result.add(SystemFields.INTEGER_DESCRIPTION);
		return result;
	}
}
