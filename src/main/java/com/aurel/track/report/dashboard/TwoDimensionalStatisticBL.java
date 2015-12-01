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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.execute.FilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigBL;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.report.dashboard.TwoDimensionalStatistic.LINK_PARAMETERS;
import com.aurel.track.report.dashboard.TwoDimensionalStatistic.Statistic2D;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.screen.item.bl.design.ScreenFieldDesignBL;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.SortedOptionBean;
import com.aurel.track.util.SortedOptionBeanComparator;

public class TwoDimensionalStatisticBL {
	private static final Logger LOGGER = LogManager.getLogger(TwoDimensionalStatistic.class);

	public static List<IntegerStringBean> getOptionFileds(Locale locale) {
		List<IntegerStringBean> fieldsSelect=new ArrayList<IntegerStringBean>();
		List fieldsConfigs= ScreenFieldDesignBL.getInstance().getFieldsConfigs(locale);
		for (Iterator iter = fieldsConfigs.iterator(); iter.hasNext();) {
			TFieldConfigBean fieldConfig = (TFieldConfigBean) iter.next();
			int valueType=FieldTypeManager.getFieldTypeRT(fieldConfig.getField()).getValueType();	
			if(valueType==ValueType.CUSTOMOPTION||valueType==ValueType.SYSTEMOPTION){
				if(fieldConfig.getField().intValue()!=SystemFields.ISSUENO&&fieldConfig.getField().intValue()!=SystemFields.SUPERIORWORKITEM){
					IntegerStringBean lvb=new IntegerStringBean();
					lvb.setLabel(fieldConfig.getLabel());
					lvb.setValue(fieldConfig.getField());
					fieldsSelect.add(lvb);
				}
			}
				
		}
		Collections.sort(fieldsSelect);
		return fieldsSelect;
	}

	static Statistic2D calculateStatistic( String filterId,Integer fieldH,boolean sortDescH,boolean useSortOrderH,Integer fieldV,boolean sortDescV,boolean useSortOrderV, TPersonBean user, Locale locale,Integer projectID,Integer releaseID) throws TooManyItemsToLoadException {
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
		Collection<ReportBean> items = getIssues(new Integer(filterId), user, locale,objectID,entityFlag);

		Statistic2D statistic2d=new Statistic2D();
		TFieldConfigBean fieldConfigH= FieldConfigBL.loadDefault(fieldH);
		TFieldBean fieldBeanH= FieldBL.loadByPrimaryKey(fieldH);
		boolean customH= BooleanFields.TRUE_VALUE.equals(fieldBeanH.getIsCustom());
		LocalizeUtil.localizeFieldConfig(fieldConfigH, locale);

		TFieldConfigBean fieldConfigV= FieldConfigBL.loadDefault(fieldV);
		TFieldBean fieldBeanV= FieldBL.loadByPrimaryKey(fieldV);
		boolean customV= BooleanFields.TRUE_VALUE.equals(fieldBeanV.getIsCustom());
		LocalizeUtil.localizeFieldConfig(fieldConfigV, locale);
		
		statistic2d.setLabelH(fieldConfigH.getLabel());
		statistic2d.setLabelV(fieldConfigV.getLabel());
		
		
	    String theParams = "";
	    
		if(items!=null){
			Map<Object,List<ReportBean>> map1=new HashMap<Object, List<ReportBean>>();
			Object value1;
			List<ReportBean> list1;
			for (Iterator<ReportBean> iterator = items.iterator(); iterator.hasNext();) {
				ReportBean reportBean = iterator.next();
				value1=getAttributeValue(reportBean.getWorkItemBean(),fieldH,customH);
				if(value1!=null){
					list1=map1.get(value1);
					if(list1==null){
						list1=new ArrayList<ReportBean>();
					}
					list1.add(reportBean);
					map1.put(value1, list1);
				}
			}
			Iterator<Map.Entry<Object, List<ReportBean>>> it=map1.entrySet().iterator();
			List<SortedOptionBean> field1Options=new ArrayList<SortedOptionBean>();
			Map<String,Map<Object,IntegerStringBean>> map2List=new HashMap<String,Map<Object,IntegerStringBean>>();
			Map<Object,String> field2OptionsMap=new HashMap<Object, String>();
			SortedOptionBean sob;
			Map.Entry<Object, List<ReportBean>> entry;
			while (it.hasNext()) {
				entry=it.next();
				value1=entry.getKey();
				list1=entry.getValue();
				sob=new SortedOptionBean();
				sob.setValue(value1.toString());
				ReportBean rb=list1.get(0);
				sob.setLabel(rb.getShowValue(fieldH));
				sob.setSortOrder(rb.getSortOrder(fieldH));
				sob.setValue(value1.toString());
				Map<Object,IntegerStringBean> map2=groupItems(list1,fieldV,customV);
				Iterator<Map.Entry<Object,IntegerStringBean>> it2=map2.entrySet().iterator();
				Map.Entry<Object,IntegerStringBean> entry2;
				while(it2.hasNext()){
					entry2=it2.next();
					Object key=entry2.getKey();
					if(!field2OptionsMap.containsKey(key)){
						field2OptionsMap.put(key, entry2.getValue().getLabel());
					}
				}
				map2List.put(value1.toString(),map2);
				field1Options.add(sob);
			}
			Collections.sort(field1Options, new SortedOptionBeanComparator(sortDescH,useSortOrderH));
			
			List<SortedOptionBean> field2Options=new ArrayList<SortedOptionBean>();
			Iterator<Map.Entry<Object,String>>  it2=field2OptionsMap.entrySet().iterator();
			Map.Entry<Object, String> entry2;
			while(it2.hasNext()){
				entry2=it2.next();
				Object key=entry2.getKey();
				sob=new SortedOptionBean();
				sob.setLabel(entry2.getValue());
				sob.setValue(key);
				for (Iterator<ReportBean> iterator = items.iterator(); iterator.hasNext();) {
					ReportBean reportBean = iterator.next();
					Object o= getAttributeValue(reportBean.getWorkItemBean(),fieldV,customV);
					if(o==key){
						sob.setSortOrder(reportBean.getSortOrder(fieldV));
						break;
					}
				}
				field2Options.add(sob);
			}
			
			Collections.sort(field2Options, new SortedOptionBeanComparator(sortDescV,useSortOrderV));
			
			Integer[][] values=new Integer[field2Options.size()][field1Options.size()];
			for (int i = 0; i < field2Options.size(); i++) {
				Object f2Value=field2Options.get(i).getValue();
				for (int j = 0; j < field1Options.size(); j++) {
					sob=field1Options.get(j);
					Map<Object,IntegerStringBean> m2=map2List.get(sob.getValue());
					Integer value=Integer.valueOf(0);
					if(m2!=null){
						IntegerStringBean isb= m2.get(f2Value);
						if(isb!=null){
							value=isb.getValue();
						}
					}
					values[i][j]=value;
				}
			}
			//total vertical
			List<Integer> totalH=new ArrayList<Integer>();
			int sumV;
			for (int i = 0; i < field1Options.size(); i++) {
				sumV=0;
				for (int j = 0; j < field2Options.size(); j++) {
					sumV=sumV+values[j][i].intValue();
				}
				totalH.add(Integer.valueOf(sumV));
			}
			
			//total horizontal
			List<Integer> totalV=new ArrayList<Integer>();
			int sumH=0;
			for (int i = 0; i < field2Options.size(); i++) {
				sumH=0;
				for (int j = 0; j < field1Options.size(); j++) {
					sumH=sumH+values[i][j].intValue();
				}
				totalV.add(Integer.valueOf(sumH));
			}
			int sumT=0;
			for (int i = 0; i < totalH.size(); i++) {
				Integer v=totalH.get(i);
				sumT=sumT+v.intValue();
			}
			totalV.add(Integer.valueOf(sumT));
			
			//options horizontal
			List<LabelValueBean> optionsH=new ArrayList<LabelValueBean>();
			for (int i = 0; i < field1Options.size(); i++) {
				sob=field1Options.get(i);
				//String link = createEncodedLink(filterId, projectID, releaseID,theProvider,
				
				LabelValueBean o=new LabelValueBean();
				o.setLabel(sob.getLabel());
				o.setValue(sob.getValue().toString());
				optionsH.add(o);
			}
			
			//options vertical
			List<LabelValueBean> optionsV=new ArrayList<LabelValueBean>();
			for (int i = 0; i < field2Options.size(); i++) {
				sob=field2Options.get(i);
				//String link = createEncodedLink(filterId, projectID, releaseID,theProvider,
				
				LabelValueBean o=new LabelValueBean();
				o.setLabel(sob.getLabel());
				o.setValue(sob.getValue().toString());
				optionsV.add(o);
			}
			
			LabelValueBean[][] valuesArr=new LabelValueBean[field2Options.size()][field1Options.size()];
			for (int i = 0; i < field2Options.size(); i++) {
				SortedOptionBean lvbV=field2Options.get(i);
				for (int j = 0; j < field1Options.size(); j++) {
					SortedOptionBean lvbH=field1Options.get(j);
					Integer value=values[i][j];
					LabelValueBean lvb=new LabelValueBean();
					lvb.setLabel(value.toString());
					//String link = createEncodedLink(filterId, projectID, releaseID,theProvider,
					lvb.setValue(lvbH.getValue().toString()+";"+lvbV.getValue().toString());
					valuesArr[i][j]=lvb;
				}
			}
			
			statistic2d.setOptionsH(optionsH);
			statistic2d.setOptionsV(optionsV);
			statistic2d.setTotalH(totalH);
			statistic2d.setTotalV(totalV);
			statistic2d.setValues(valuesArr);
			
		}
		if(items!=null){
			items.clear();
			items=null;
		}
		return statistic2d;
	}
	
	static List<ReportBean> getIssues(Map<String, String> configParams, Map<String, String> filterParams,TPersonBean user,Locale locale) throws TooManyItemsToLoadException {

		Integer fieldIdH=BasePluginDashboardBL.parseInteger(configParams,TwoDimensionalStatistic.SELECTED_FIELD_H);
		String fieldValueH=filterParams.get(LINK_PARAMETERS.FIELD_VALUE_H);
		TFieldBean fieldBeanH= FieldBL.loadByPrimaryKey(fieldIdH);
		boolean customH= BooleanFields.TRUE_VALUE.equals(fieldBeanH.getIsCustom());

		Integer fieldIdV=BasePluginDashboardBL.parseInteger(configParams,TwoDimensionalStatistic.SELECTED_FIELD_V);
		String fieldValueV=filterParams.get(LINK_PARAMETERS.FIELD_VALUE_V);
		TFieldBean fieldBeanV= FieldBL.loadByPrimaryKey(fieldIdV);
		boolean customV= BooleanFields.TRUE_VALUE.equals(fieldBeanV.getIsCustom());

		Integer projectID=BasePluginDashboardBL.parseInteger(filterParams,LINK_PARAMETERS.PROJECT);
		Integer releaseID=BasePluginDashboardBL.parseInteger(filterParams,LINK_PARAMETERS.RELEASE);

		String filterId=configParams.get(TwoDimensionalStatistic.SELECTED_FILTER);

		LOGGER.debug(" getIssues() providerParams: projectID " + projectID +  "  releaseID " + releaseID +
				" fieldValueH " + fieldValueH + " fieldValueV " + fieldValueV);

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
		List<ReportBean> items = getIssues(new Integer(filterId), user, locale,objectID,entityFlag);
		List<ReportBean> workItemList = new ArrayList<ReportBean>();
		for (Iterator<ReportBean> iterator = items.iterator(); iterator.hasNext();) {
			ReportBean reportBean = iterator.next();
			boolean ok=true;
			
			if(fieldValueH!=null){
				Object v=getAttributeValue(reportBean.getWorkItemBean(),fieldIdH,customH);
				if(v==null){
					ok=false;
				}else{
					if(!v.toString().equals(fieldValueH)){
						ok=false;
					}
				}
			}
			if(fieldValueV!=null){
				Object v=getAttributeValue(reportBean.getWorkItemBean(),fieldIdV,customV);
				if(v==null){
					ok=false;
				}else{
					if(!v.toString().equals(fieldValueV)){
						ok=false;
					}
				}
			}
			if(ok){
				workItemList.add(reportBean);
			}
		}
		return workItemList;
	}
	
	public static String encodeJSON(Statistic2D statistic2D){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb,"labelH",statistic2D.getLabelH());
		JSONUtility.appendStringValue(sb,"labelV",statistic2D.getLabelV());
		JSONUtility.appendLabelValueBeanList(sb,"optionsH",statistic2D.getOptionsH());
		JSONUtility.appendLabelValueBeanList(sb,"optionsV",statistic2D.getOptionsV());
		JSONUtility.appendIntegerListAsArray(sb,"totalH",statistic2D.getTotalH());
		JSONUtility.appendIntegerListAsArray(sb,"totalV",statistic2D.getTotalV());
		JSONUtility.appendJSONLabelValueBeanMatrix(sb,"values",statistic2D.getValues(),true);
		sb.append("}");
		return sb.toString();
	}
	
	
	
	
	private static List<ReportBean> getIssues(Integer filterId, TPersonBean user, Locale locale,Integer objectID,Integer entityFlag) throws TooManyItemsToLoadException {
		List<ReportBean> reportBeansList= new ArrayList<ReportBean>();
		if (filterId != null) {
			ReportBeans reportBeans = FilterExecuterFacade.getSavedFilterReportBeans(
				filterId, locale, user, new LinkedList<ErrorData>(), null, objectID, entityFlag);
			if (reportBeans != null) {
				reportBeansList.addAll(reportBeans.getReportBeansMap().values());
			}
		}
		return reportBeansList;
	}
	private static Map<Object,IntegerStringBean> groupItems(List<ReportBean> lst,Integer fieldID,boolean custom){
		Map<Object,IntegerStringBean> map=new HashMap<Object, IntegerStringBean>();
		Object value;
		IntegerStringBean isb;
		for (Iterator<ReportBean> iterator = lst.iterator(); iterator.hasNext();) {
			ReportBean reportBean = iterator.next();
			value=getAttributeValue(reportBean.getWorkItemBean(),fieldID,custom);
			if(value!=null){
				isb=map.get(value);
				if(isb==null){
					isb=new IntegerStringBean(reportBean.getShowValue(fieldID),0);
				}
				isb.setValue(Integer.valueOf(isb.getValue().intValue()+1));
				map.put(value, isb);
			}
		}
		return map;
	}
	private static Object getAttributeValue(TWorkItemBean workItemBean,Integer fieldID,boolean custom){
		Object value=workItemBean.getAttribute(fieldID);
		if(value!=null&&custom){
			return ((Object[])value)[0];
		}
		return value;
	}
}
