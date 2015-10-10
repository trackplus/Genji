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

package com.aurel.track.report.dashboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.errors.ErrorHandlerJSONAdapter;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.itemNavigator.ItemNavigatorBL;
import com.aurel.track.itemNavigator.layout.LayoutTO;
import com.aurel.track.itemNavigator.layout.column.ColumnFieldTO;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.ReportItemJSON;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.LabelValueBean;

public abstract class AbstractIssueFilterDashboard extends BasePluginDashboardView {
	protected static int MAX_NUMBER_OF_ISSUES_TO_SHOW = 100;
	
	protected abstract List<ReportBean> getReportBeanList(FilterInfo filter,TPersonBean user,Integer projectID,Integer releaseID, List<ErrorData> errors) throws TooManyItemsToLoadException;
	
	protected abstract FilterInfo getFilterInfo(Map session, Map parameters, TPersonBean user,Integer projectID,Integer releaseID);


	@Override
	protected String encodeJSONExtraData(Integer dashboardID, Map<String, Object> session, Map<String, String> parameters,
										 Integer projectID, Integer releaseID, Map<String,String> ajaxParams) throws TooManyItemsToLoadException{
		TPersonBean user = (TPersonBean)session.get(Constants.USER_KEY);
		StringBuilder sb=new StringBuilder();
		Locale locale = user.getLocale();
		FilterInfo filterInfo=getFilterInfo(session, parameters, user, projectID, releaseID);
		String filterTitle=null;
		String filterDescription=null;
		Integer filterID=null;
		if(filterInfo==null){
			List<LabelValueBean> localizedErrors=new ArrayList<LabelValueBean>();
			String filterLabel=BasePluginDashboardBL.getText("myFilterView.filter",  locale);
			String errorTxt= LocalizeUtil.getParametrizedString("common.err.required",new String[]{filterLabel}, locale);
			localizedErrors.add(new LabelValueBean(errorTxt,"filterId"));
			JSONUtility.appendLabelValueBeanList(sb,"errors",localizedErrors);
		}
		if(filterInfo!=null){
			filterTitle=filterInfo.getName();
			filterID=filterInfo.getId();
			filterDescription=filterInfo.getDescription();
			JSONUtility.appendIntegerValue(sb,"filterId",filterID);
		}
		if(filterTitle!=null){
			JSONUtility.appendBooleanValue(sb,"haveFilterTitle",true);
			JSONUtility.appendStringValue(sb,"filterTitle", filterTitle);
		}else{
			JSONUtility.appendBooleanValue(sb,"haveFilterTitle",false);
		}
		if(filterDescription!=null){
			JSONUtility.appendBooleanValue(sb,"haveFilterDescription",false);
			JSONUtility.appendStringValue(sb,"filterDescription", filterDescription);
		}else{
			JSONUtility.appendBooleanValue(sb,"haveFilterDescription",false);

		}
		if(filterInfo!=null){
			List<ReportBean> reportBeanList;
			List<ErrorData> errors=new ArrayList<ErrorData>();
			reportBeanList = getReportBeanList(filterInfo,user, projectID, releaseID,errors);
			if(!errors.isEmpty()){
				List<LabelValueBean> localizedErrors=new ArrayList<LabelValueBean>();
				for (int i = 0; i < errors.size(); i++) {
					ErrorData errorData=(ErrorData) errors.get(i);
					//String resourceKey = errorData.getResourceKey();
					String label= ErrorHandlerJSONAdapter.createMessage(errorData, locale);
					localizedErrors.add(new LabelValueBean(label,errorData.getFieldName()));
				}
				JSONUtility.appendLabelValueBeanList(sb,"errors",localizedErrors);
			}else{
				int originalSize=reportBeanList.size();
				int maxIssuesToShow=MAX_NUMBER_OF_ISSUES_TO_SHOW;
				String maxIssuesToShowStr=parameters.get("maxIssuesToShow");
				if(maxIssuesToShowStr!=null&&maxIssuesToShowStr.length()>0){
					try{
						maxIssuesToShow=Integer.parseInt(maxIssuesToShowStr);
						if(maxIssuesToShow<=0){
							maxIssuesToShow=MAX_NUMBER_OF_ISSUES_TO_SHOW;
						}
					}catch (Exception e) {
						maxIssuesToShow=MAX_NUMBER_OF_ISSUES_TO_SHOW;
					}
				}
				boolean chunked=false;
				if(reportBeanList.size()>maxIssuesToShow){
					reportBeanList= ItemNavigatorBL.cutItems(reportBeanList, maxIssuesToShow);
					chunked=true;
				}
				JSONUtility.appendBooleanValue(sb,"chunked",chunked);
				JSONUtility.appendStringValue(sb,"chunkedText",maxIssuesToShow+" from "+originalSize);
				ReportBeans reportBeans=new ReportBeans(reportBeanList, locale, null, true, false);
				//layout
				List<ColumnFieldTO> shortFields=getShortFields(session, parameters, user, projectID, releaseID);
				int sum=0;
				for (Iterator<ColumnFieldTO> iter = shortFields.iterator(); iter.hasNext();) {
					ColumnFieldTO rlb = iter.next();
					sum=sum+rlb.getFieldWidth().intValue();
				}
				Integer widthTableHeader=new Integer(sum);
				JSONUtility.appendIntegerValue(sb,"widthTableHeader",widthTableHeader);
				JSONUtility.appendIntegerValue(sb,"itemNoFieldID",SystemFields.ISSUENO);
				JSONUtility.appendIntegerValue(sb,"synopsisFieldID",SystemFields.SYNOPSIS);
				JSONUtility.appendIntegerValue(sb,"linksFieldID",TReportLayoutBean.PSEUDO_COLUMNS.LINKED_ITEMS);
				JSONUtility.appendIntegerValue(sb,"attachmentFieldID",TReportLayoutBean.PSEUDO_COLUMNS.ATTACHMENT_SYMBOL);
				JSONUtility.appendIntegerValue(sb,"noOfFields",shortFields.size());
				JSONUtility.appendIntegerValue(sb,"personKey", user.getObjectID());
				//ReportItemLayout reportItemLayout=new ReportItemLayout();
				LayoutTO layoutTO = new LayoutTO();
				layoutTO.setShortFields(shortFields);
				boolean  useProjectSpecificID=false;
				ApplicationBean appBean =ApplicationBean.getApplicationBean();
				if(appBean.getSiteBean().getProjectSpecificIDsOn()!=null){
					useProjectSpecificID=appBean.getSiteBean().getProjectSpecificIDsOn().booleanValue();
				}
				JSONUtility.appendJSONValue(sb, "shortFields", ReportItemJSON.encodeColumnFields(shortFields, useProjectSpecificID));
				JSONUtility.appendJSONValue(sb, "data",ReportItemJSON.encodeReportBeans(reportBeans, layoutTO, false,locale,useProjectSpecificID,user,null,false,null,false,false));
			}
		}
		return sb.toString();
	}

	protected List<ColumnFieldTO> getShortFields(Map session,Map parameters, TPersonBean user,Integer projectID,Integer releaseID){
		List<ColumnFieldTO> shortFields=new ArrayList<ColumnFieldTO>();
		Locale locale = (Locale) session.get(Constants.LOCALE_KEY);
		int[] field=new int[]{SystemFields.ISSUENO,TReportLayoutBean.PSEUDO_COLUMNS.ISSUETYPE_SYMBOL, SystemFields.SYNOPSIS,SystemFields.STATE, SystemFields.LASTMODIFIEDDATE};
		Map<Integer, TFieldConfigBean> defaultConfigsMap = FieldRuntimeBL.getLocalizedDefaultFieldConfigsMap(locale);//getLocalizeAllDefaultConfigs(locale);
		int[] fieldSize=new int[]{100,30,350,100, 120};
		boolean[] renderAsImg=new boolean[]{false,true,false,false, false};
		TFieldConfigBean fieldConfigBean;
		for (int i = 0; i < field.length; i++) {
			Integer fieldID=field[i];
			ColumnFieldTO columnFieldTO = new ColumnFieldTO();
			fieldConfigBean = (TFieldConfigBean)defaultConfigsMap.get(fieldID);
			if (fieldConfigBean!=null)  {
				columnFieldTO.setLabel(fieldConfigBean.getLabel());
			}
			columnFieldTO.setFieldID(field[i]);
			columnFieldTO.setFieldWidth(fieldSize[i]);
			columnFieldTO.setRenderContentAsImg(renderAsImg[i]);
			shortFields.add(columnFieldTO);
		}
		return shortFields;
	}
	
	protected static class FilterInfo{
		private Integer id;
		private Integer type;
		private String name;
		private String description;
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public Integer getType() {
			return type;
		}
		public void setType(Integer type) {
			this.type = type;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
	}
}
