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


package com.aurel.track.report.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.filter.execute.TQLPlusFilterExecuterFacade;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.errors.ErrorHandlerJSONAdapter;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.tql.TqlBL;
import com.aurel.track.util.LabelValueBean;

public class MyTqlView extends AbstractIssueFilterDashboard {
	static Logger LOGGER = LogManager.getLogger(MyTqlView.class);

	@Override
	public Map<String,String> convertMapFromPageConfig(Map<String,String> properties, TPersonBean user,Locale locale,Integer entityId, Integer entityType,List<LabelValueBean> errors) {
		Map<String,String> map=super.convertMapFromPageConfig(properties, user, locale,entityId,entityType, errors);
		String tql=properties.get("tql");
		if(tql==null||tql.length()==0){
			String localizedErr= LocalizeUtil.getParametrizedString("common.err.required", new Object[]{BasePluginDashboardBL.getText("myTqlView.tql", locale)}, locale);
			errors.add(new LabelValueBean(localizedErr ,"params.tql"));
		}else{
			List<ErrorData> criteriaErrors=new ArrayList<ErrorData>();
			TqlBL.luceneQuery(tql, false, locale, null, criteriaErrors);
			if(!criteriaErrors.isEmpty()){
				StringBuilder sb=new StringBuilder();
				for(int i=0;i<criteriaErrors.size();i++){
					sb.append(ErrorHandlerJSONAdapter.createMessage(criteriaErrors.get(i), locale)+"</BR>");
				}
				errors.add(new LabelValueBean(sb.toString(),"params.tql"));
			}
		}
		return map;
	}

	@Override
	protected boolean isUseConfig(){
		return true;
	}

	@Override
	public String encodeJSONExtraDataConfig(Map<String,String> parameters, TPersonBean user, Integer entityId, Integer entityType){
		StringBuilder sb=new StringBuilder();
		JSONUtility.appendStringValue(sb,"tql",parameters.get("tql"));
		return sb.toString();
	}

	@Override
	protected List<ReportBean> getReportBeanList(FilterInfo filter,TPersonBean user, Integer projectID, Integer releaseID,List<ErrorData> errors) {
		String userQuery=filter.getDescription();
		Locale locale=user.getLocale();
		Integer objectID=null;
		Integer entityFlag=null;
		if(projectID!=null){
			if(releaseID!=null){
				entityFlag=SystemFields.RELEASESCHEDULED;
				objectID=releaseID;
			}else{
				entityFlag=SystemFields.PROJECT;
				objectID=projectID;
			}
		}
		List<ReportBean> result=new ArrayList<ReportBean>();
		if(userQuery!=null&&userQuery.length()>0){
			ReportBeans reportBeans = TQLPlusFilterExecuterFacade.getInstance().getInstantFilterReportBeans(
					userQuery, null, locale, user, errors, null, objectID, entityFlag, false);
			if(reportBeans!=null){
				result=reportBeans.getItems();
			}
		}
		if(!errors.isEmpty()){
			for (int i = 0; i < errors.size(); i++) {
				ErrorData error=(ErrorData) errors.get(i);
				LOGGER.error(error.getResourceKey());
			}
		}
		return result;
	}

	@Override
	protected FilterInfo getFilterInfo(Map session,Map parameters, TPersonBean user, Integer projectID,Integer releaseID) {
		String tql=(String) parameters.get("tql");
		FilterInfo filter=new FilterInfo();
		filter.setDescription(tql);
		return filter;
	}
}
