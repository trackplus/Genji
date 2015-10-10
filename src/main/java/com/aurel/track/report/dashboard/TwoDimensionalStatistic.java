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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.LabelValueBean;

/**
 * Can select filter, can select field for x-axis, field for y-axis,
 * sort-by, sort direction, show sums (yes, no), number of rows to display.
 * @author Adi
 *
 */
public class TwoDimensionalStatistic extends BasePluginDashboardView implements IssueProvider{
	public static final String SELECTED_FILTER = "selFilter";
	public static final String FIELDS_H = "fieldsH";
	public static final String FIELDS_V = "fieldsV";
	public static final String SELECTED_FIELD_H = "selFieldH";
	public static final String SELECTED_FIELD_V = "selFieldV";
	
	private static final String ALPHANUMERIC="alphanumeric";
	private static final String SORT_ORDER="sortOrder";
	private static final String ASC="asc";
	private static final String DESC="desc";
	
	protected static interface LINK_PARAMETERS {
		static String FIELD_VALUE_H= "fieldValueH";
		static String FIELD_VALUE_V= "fieldValueV";
		static String PROJECT = "project";
		static String RELEASE = "release";
	}

	protected boolean isUseConfig(){
		return true;
	}

	@Override
	protected String encodeJSONExtraData(Integer dashboardID,Map<String, Object> session, Map<String, String> parameters,
										 Integer projectID, Integer releaseID,Map<String,String> ajaxParams) throws TooManyItemsToLoadException{
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		StringBuilder sb=new StringBuilder();
		String filterId = parameters.get(SELECTED_FILTER);
		String filterTitle = "";
		JSONUtility.appendStringValue(sb,"filterId",filterId);
		if (filterId == null || "".equals(filterId)) {
			JSONUtility.appendStringValue(sb,"filterTitle",filterTitle);
		}else{
			Locale locale = user.getLocale();
			Integer fieldH=SystemFields.INTEGER_ISSUETYPE;
			Integer fieldV=SystemFields.INTEGER_STATE;
			String fieldHStr=parameters.get(SELECTED_FIELD_H);
			if(fieldHStr!=null){
				try{
					fieldH=Integer.valueOf(fieldHStr);
				}catch (Exception e) {
				}
			}
			String fieldVStr=parameters.get(SELECTED_FIELD_V);
			if(fieldVStr!=null){
				try{
					fieldV=Integer.valueOf(fieldVStr);
				}catch (Exception e) {
				}
			}

			String selectedSortByH=parameters.get("selectedSortByH");
			String selectedSortTypeH=parameters.get("selectedSortTypeH");
			boolean useSortOrderH=SORT_ORDER.equals(selectedSortByH);
			boolean sortDescH=DESC.equals(selectedSortTypeH);

			String selectedSortByV=parameters.get("selectedSortByV");
			String selectedSortTypeV=parameters.get("selectedSortTypeV");
			boolean useSortOrderV=SORT_ORDER.equals(selectedSortByV);
			boolean sortDescV=DESC.equals(selectedSortTypeV);


			Statistic2D statistic2d=TwoDimensionalStatisticBL.calculateStatistic(filterId,fieldH,sortDescH,useSortOrderH,fieldV,sortDescV,useSortOrderV, user, locale, projectID, releaseID);
			JSONUtility.appendJSONValue(sb,"statistic",TwoDimensionalStatisticBL.encodeJSON(statistic2d));
			filterTitle=BasePluginDashboardBL.getFilterTitle(filterId, (Locale)session.get(Constants.LOCALE_KEY));
			JSONUtility.appendStringValue(sb,"filterTitle", filterTitle);
		}

		return sb.toString();
	}

	@Override
	public String encodeJSONExtraDataConfig(Map<String,String> parameters, TPersonBean user, Integer entityId, Integer entityType){
		StringBuilder sb=new StringBuilder();
		String filterStr = parameters.get(SELECTED_FILTER);
		Integer filter=null;
		if(filterStr!=null){
			try{
				filter=Integer.parseInt(filterStr);
			}catch (Exception e){}
		}

		JSONUtility.appendIntegerValue(sb,SELECTED_FILTER, filter);
		String filterTitle=BasePluginDashboardBL.getFilterTitle(filterStr, user.getLocale());
		JSONUtility.appendStringValue(sb,"selFilterName", filterTitle);

		String selFieldHStr = parameters.get(SELECTED_FIELD_H);
		String selFieldVStr = parameters.get(SELECTED_FIELD_V);
		Integer selFieldH=null,selFieldV=null;
		if(selFieldHStr!=null){
			try{
				selFieldH=Integer.parseInt(selFieldHStr);
			}catch (Exception ex){}
		}
		if(selFieldVStr!=null){
			try{
				selFieldV=Integer.parseInt(selFieldVStr);
			}catch (Exception ex){}
		}
		if(selFieldH==null){
			selFieldH=SystemFields.ISSUETYPE;
		}
		if(selFieldV==null){
			selFieldV=SystemFields.STATE;
		}
		JSONUtility.appendIntegerValue(sb,SELECTED_FIELD_H,selFieldH);
		JSONUtility.appendIntegerValue(sb,SELECTED_FIELD_V,selFieldV);
		Locale locale=user.getLocale();

		List<IntegerStringBean> fieldsSelect = TwoDimensionalStatisticBL.getOptionFileds(locale);
		JSONUtility.appendIntegerStringBeanList(sb,FIELDS_H,fieldsSelect);
		JSONUtility.appendIntegerStringBeanList(sb,FIELDS_V,fieldsSelect);

		List<LabelValueBean> sortTypes=new ArrayList<LabelValueBean>();
		sortTypes.add(new LabelValueBean(localizeText("twoDimensionalStatistic.asc",locale),ASC));
		sortTypes.add(new LabelValueBean(localizeText("twoDimensionalStatistic.desc",locale),DESC));
		JSONUtility.appendLabelValueBeanList(sb,"sortTypes",sortTypes);
		String selectedSortTypeV=parameters.get("selectedSortTypeV");
		if(selectedSortTypeV==null||selectedSortTypeV.trim().length()==0){
			selectedSortTypeV=ASC;
		}
		JSONUtility.appendStringValue(sb,"selectedSortTypeV",selectedSortTypeV);
		String selectedSortTypeH=parameters.get("selectedSortTypeH");
		if(selectedSortTypeH==null||selectedSortTypeH.trim().length()==0){
			selectedSortTypeH=ASC;
		}
		JSONUtility.appendStringValue(sb,"selectedSortTypeH",selectedSortTypeH);


		List<LabelValueBean> sortBys=new ArrayList<LabelValueBean>();
		sortBys.add(new LabelValueBean(localizeText("twoDimensionalStatistic.alphanumeric",locale),ALPHANUMERIC));
		sortBys.add(new LabelValueBean(localizeText("twoDimensionalStatistic.sortOrder",locale),SORT_ORDER));
		JSONUtility.appendLabelValueBeanList(sb,"sortBys",sortBys);
		String selectedSortByV=parameters.get("selectedSortByV");
		if(selectedSortByV==null||selectedSortByV.trim().length()==0){
			selectedSortByV=ALPHANUMERIC;
		}
		JSONUtility.appendStringValue(sb,"selectedSortByV",selectedSortByV);
		String selectedSortByH=parameters.get("selectedSortByH");
		if(selectedSortByH==null||selectedSortByH.trim().length()==0){
			selectedSortByH=ALPHANUMERIC;
		}
		JSONUtility.appendStringValue(sb,"selectedSortByH",selectedSortByH);

		int prefWidth=700;
		int prefHeight=400;
		JSONUtility.appendIntegerValue(sb,"prefWidth",prefWidth);
		JSONUtility.appendIntegerValue(sb,"prefHeight",prefHeight);

		return sb.toString();
	}

	@Override
	public List<ReportBean> getIssues(Map<String, String> configParams, Map<String, String> filterParams, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException{
		return TwoDimensionalStatisticBL.getIssues(configParams, filterParams,personBean,locale);
	}
	public int[] getIssueNos(Map providerParams) {
		return null;
	}
	
	public static class Statistic2D{
		private String labelH;
		private String labelV;
		private List<LabelValueBean> optionsH;
		private List<LabelValueBean> optionsV;
		private List<Integer> totalH;
		private List<Integer> totalV;
		private LabelValueBean[][] values;
		public String getLabelH() {
			return labelH;
		}
		public void setLabelH(String labelH) {
			this.labelH = labelH;
		}
		public String getLabelV() {
			return labelV;
		}
		public void setLabelV(String labelV) {
			this.labelV = labelV;
		}
		public List<LabelValueBean> getOptionsH() {
			return optionsH;
		}
		public void setOptionsH(List<LabelValueBean> optionsH) {
			this.optionsH = optionsH;
		}
		public List<LabelValueBean> getOptionsV() {
			return optionsV;
		}
		public void setOptionsV(List<LabelValueBean> optionsV) {
			this.optionsV = optionsV;
		}
		public List<Integer> getTotalH() {
			return totalH;
		}
		public void setTotalH(List<Integer> totalH) {
			this.totalH = totalH;
		}
		public List<Integer> getTotalV() {
			return totalV;
		}
		public void setTotalV(List<Integer> totalV) {
			this.totalV = totalV;
		}
		public LabelValueBean[][] getValues() {
			return values;
		}
		public void setValues(LabelValueBean[][] values) {
			this.values = values;
		}
	}
}
