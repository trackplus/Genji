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

package com.aurel.track.itemNavigator.cardView;

import java.util.*;

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
import com.aurel.track.beans.*;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.massOperation.MassOperationContext;
import com.aurel.track.itemNavigator.ItemNavigatorBL;
import com.aurel.track.itemNavigator.layout.column.ColumnFieldsBL;
import com.aurel.track.report.ReportItemJSON;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeansBL;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.dao.CardFieldDAO;
import com.aurel.track.dao.CardFieldOptionDAO;
import com.aurel.track.dao.CardGroupingFieldDAO;
import com.aurel.track.dao.CardPanelDAO;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.itemNavigator.layout.NavigatorLayoutBL;
import com.aurel.track.screen.card.bl.design.CardPanelDesignBL;
import com.aurel.track.screen.card.bl.runtime.CardPanelRuntimeBL;

/**
 *
 */
public class CardViewBL {
	static private Logger LOGGER = LogManager.getLogger(CardViewBL.class);

	public static  final int MAX_COLUMNS=10;

	public static final String VIEW_CARD="com.trackplus.itemNavigator.CardViewPlugin";
	public static final Integer DEFAULT_GROUPING_FIELD= SystemFields.INTEGER_STATE;
	public static final Integer DEFAULT_SORT_FIELD= SystemFields.INTEGER_PRIORITY;
	public static final Integer DEFAULT_OPTION_WIDTH= 250;

	private static CardGroupingFieldDAO cardGroupingFieldDAO = DAOFactory.getFactory().getCardGroupingFieldDAO();
	private static CardFieldOptionDAO cardFieldOptionDAO = DAOFactory.getFactory().getCardFieldOptionsDAO();
	private static CardPanelDAO cardPanelDAO = DAOFactory.getFactory().getCardPanelDAO();
	private static CardFieldDAO cardFieldDAO = DAOFactory.getFactory().getCardFieldDAO();


	/**
	 * Saves a card grouping field
	 * @param cardGroupingFieldBean
	 * @return
	 */
	public static Integer saveCardGroupingField(TCardGroupingFieldBean cardGroupingFieldBean) {
		return cardGroupingFieldDAO.save(cardGroupingFieldBean);
	}

	/**
	 * Saves a card field option
	 * @param cardFieldOptionBean
	 * @return
	 */
	public static Integer saveCardFieldOption(TCardFieldOptionBean cardFieldOptionBean) {
		return cardFieldOptionDAO.save(cardFieldOptionBean);
	}
	
	/**
	 * Gets the active grouping field
	 * @param personBean
	 * @param filterID
	 * @param filterType
	 * @param forcePersonLayout
	 * @return
	 */
	public static TCardGroupingFieldBean getActiveGroupingField(TPersonBean personBean, Integer filterID, Integer filterType, boolean forcePersonLayout){
		Integer navigatorLayoutID = NavigatorLayoutBL.getLayoutID(personBean, filterType, filterID, forcePersonLayout);
		TCardGroupingFieldBean cardGroupingFieldBean=null;
		if (navigatorLayoutID!=null) {
			List<TCardGroupingFieldBean> cardGroupingFieldBeanList=cardGroupingFieldDAO.loadActiveByNavigatorLayout(navigatorLayoutID);
			if (cardGroupingFieldBeanList!=null) {
				if(!cardGroupingFieldBeanList.isEmpty()){
					cardGroupingFieldBean=cardGroupingFieldBeanList.get(0);
				}
				if (cardGroupingFieldBeanList.size()>1) {
					//should never happen
					for (int i = 1; i < cardGroupingFieldBeanList.size(); i++) {
						TCardGroupingFieldBean cardGroupingFieldBeanTmp = cardGroupingFieldBeanList.get(i);
						cardGroupingFieldBeanTmp.setIsActiv(BooleanFields.FALSE_VALUE);
						saveCardGroupingField(cardGroupingFieldBeanTmp);
					}
				}
			}
			
		}
		if(cardGroupingFieldBean==null){
			//no active card grouping field. We create a default one
			LOGGER.debug("No active card grouping field found for person:"+personBean.getLabel()+"  filterID:"+filterID+" filterType="+filterType);
			TCardGroupingFieldBean cardGroupingFieldBeanTmp=new TCardGroupingFieldBean();
			cardGroupingFieldBeanTmp.setNavigatorLayout(navigatorLayoutID);
			cardGroupingFieldBeanTmp.setCardField(DEFAULT_GROUPING_FIELD);
			cardGroupingFieldBeanTmp.setSortField(DEFAULT_SORT_FIELD);
			cardGroupingFieldBeanTmp.setIsDescending(BooleanFields.FALSE_VALUE);
			cardGroupingFieldBeanTmp.setIsActiv(BooleanFields.TRUE_VALUE);
			Integer cardGroupingFieldID=saveCardGroupingField(cardGroupingFieldBeanTmp);
			cardGroupingFieldBean=cardGroupingFieldDAO.loadByID(cardGroupingFieldID);
		}
		return cardGroupingFieldBean;
	}
	
	/**
	 * Overwrites the default layout
	 * @param sourceLayoutID
	 * @param destinationLayoutID
	 */
	public static void overwriteLayout(Integer sourceLayoutID, Integer destinationLayoutID) {
		List<TCardGroupingFieldBean> cardGroupingFieldBeanList=cardGroupingFieldDAO.loadByNavigatorLayout(sourceLayoutID);
		for(TCardGroupingFieldBean cardGroupingFieldBean:cardGroupingFieldBeanList){
			TCardGroupingFieldBean clone=cloneCardGrouping(cardGroupingFieldBean);
			clone.setNavigatorLayout(destinationLayoutID);
			Integer groupID=saveCardGroupingField(clone);
			List<TCardFieldOptionBean> optionsDB=CardViewBL.loadByCardGroupingField(cardGroupingFieldBean.getObjectID());
			for(TCardFieldOptionBean cardFieldOptionBean:optionsDB){
				TCardFieldOptionBean fieldOptionClone=cloneCardField(cardFieldOptionBean);
				fieldOptionClone.setGroupingField(groupID);
				saveCardFieldOption(fieldOptionClone);
			}
		}
	}
	
	/**
	 * Clones a card grouping
	 * @param cardGroupingFieldBean
	 * @return
	 */
	private static TCardGroupingFieldBean cloneCardGrouping(TCardGroupingFieldBean cardGroupingFieldBean){
		TCardGroupingFieldBean clone=new TCardGroupingFieldBean();
		clone.setCardField(cardGroupingFieldBean.getCardField());
		clone.setIsDescending(cardGroupingFieldBean.getIsDescending());
		clone.setSortField(cardGroupingFieldBean.getSortField());
		clone.setIsActiv(cardGroupingFieldBean.getIsActiv());
		return clone;
	}
	
	/**
	 * Clones a field option
	 * @param cardFieldOptionBean
	 * @return
	 */
	private static TCardFieldOptionBean cloneCardField(TCardFieldOptionBean cardFieldOptionBean){
		TCardFieldOptionBean clone=new TCardFieldOptionBean();
		clone.setMaxNumber(cardFieldOptionBean.getMaxNumber());
		clone.setOptionID(cardFieldOptionBean.getOptionID());
		clone.setOptionPosition(cardFieldOptionBean.getOptionPosition());
		clone.setOptionWidth(cardFieldOptionBean.getOptionWidth());
		return clone;
	}
	
	/**
	 * Deletes the grouping fields for a layout
	 * @param destinationLayoutID
	 */
	public static  void deleteByLayout(Integer destinationLayoutID){
		cardGroupingFieldDAO.deleteByLayout(destinationLayoutID);
	}

	/**
	 * Changes the default grouping field
	 * @param personBean
	 * @param filterID
	 * @param filterType
	 * @param fieldGroup
	 */
	public static void changeGroupBy(TPersonBean personBean, Integer filterID,Integer filterType,Integer fieldGroup){
		TCardGroupingFieldBean activeGroup=getActiveGroupingField(personBean,filterID,filterType, true);
		activeGroup.setIsActiv(BooleanFields.FALSE_VALUE);
		saveCardGroupingField(activeGroup);
		Integer navigatorLayoutID=activeGroup.getNavigatorLayout();
		TCardGroupingFieldBean cardGroupingFieldBean=cardGroupingFieldDAO.loadByGroupField(navigatorLayoutID,fieldGroup);
		if(cardGroupingFieldBean==null){
			cardGroupingFieldBean=new TCardGroupingFieldBean();
			cardGroupingFieldBean.setNavigatorLayout(navigatorLayoutID);
			cardGroupingFieldBean.setCardField(fieldGroup);
			cardGroupingFieldBean.setSortField(DEFAULT_SORT_FIELD);
			cardGroupingFieldBean.setIsDescending(BooleanFields.FALSE_VALUE);
		}
		cardGroupingFieldBean.setIsActiv(BooleanFields.TRUE_VALUE);
		saveCardGroupingField(cardGroupingFieldBean);
	}
	
	/**
	 * Changes the sorting field and order for a grouping field
	 * @return
	 */
	public static void changeSortBy(TPersonBean personBean, Integer filterID, Integer filterType, Integer fieldGroup, Integer sortField, boolean sortOrder) {
		Integer navigatorLayoutID = NavigatorLayoutBL.getLayoutID(personBean, filterType, filterID, true);
		TCardGroupingFieldBean cardGroupingFieldBean = cardGroupingFieldDAO.loadByGroupField(navigatorLayoutID,fieldGroup);
		if(cardGroupingFieldBean==null){
			cardGroupingFieldBean=new TCardGroupingFieldBean();
			cardGroupingFieldBean.setNavigatorLayout(navigatorLayoutID);
			cardGroupingFieldBean.setCardField(fieldGroup);
		}
		cardGroupingFieldBean.setSortField(sortField);
		cardGroupingFieldBean.setIsDescending(BooleanFields.fromBooleanToString(sortOrder));
		cardGroupingFieldDAO.save(cardGroupingFieldBean);
	}
	
	
	/**
	 * Loads the options by grouping field
	 * @param cardGroupingFieldID
	 * @return
	 */
	public static  List<TCardFieldOptionBean> loadByCardGroupingField(Integer cardGroupingFieldID){
		return cardFieldOptionDAO.loadByCardGroupingField(cardGroupingFieldID);
	}
	
	/**
	 * Stores the changes in grouping options
	 * @param personBean
	 * @param filterID
	 * @param filterType
	 * @param fieldGroup
	 * @param cardViewGroupFilter
	 */
	public static  void cardFieldOptionsChange(TPersonBean personBean, Integer filterID, Integer filterType, Integer fieldGroup, String cardViewGroupFilter){
		List<Integer> fieldIDList= StringArrayParameterUtils.splitSelectionAsInteger(cardViewGroupFilter, ",");
		Integer navigatorLayoutID = NavigatorLayoutBL.getLayoutID(personBean, filterType, filterID, true);
		TCardGroupingFieldBean cardGroupingFieldBean = cardGroupingFieldDAO.loadByGroupField(navigatorLayoutID,fieldGroup);
		if(cardGroupingFieldBean==null){
			cardGroupingFieldBean=new TCardGroupingFieldBean();
			cardGroupingFieldBean.setNavigatorLayout(navigatorLayoutID);
			cardGroupingFieldBean.setCardField(fieldGroup);
			cardGroupingFieldBean.setSortField(DEFAULT_SORT_FIELD);
			cardGroupingFieldBean.setIsDescending(BooleanFields.FALSE_VALUE);
			saveCardGroupingField(cardGroupingFieldBean);
		}
		Integer cardGroupingFieldID = cardGroupingFieldBean.getObjectID();
		List<TCardFieldOptionBean> optionsDB=cardFieldOptionDAO.loadByCardGroupingField(cardGroupingFieldID);
		List<TCardFieldOptionBean> optionsToSave=new ArrayList<TCardFieldOptionBean>();
		TCardFieldOptionBean optionsBean=null;
		Integer fieldID=null;
		if(fieldIDList!=null){
			for (int i=0;i<fieldIDList.size();i++){
				fieldID=fieldIDList.get(i);
				optionsBean= findOptionBean(optionsDB, fieldID);
				if(optionsBean!=null){
					optionsDB.remove(optionsBean);
				}else{
					optionsBean=new TCardFieldOptionBean();
					optionsBean.setGroupingField(cardGroupingFieldID);
					optionsBean.setOptionID(fieldID);
					optionsBean.setOptionWidth(DEFAULT_OPTION_WIDTH);
				}
				optionsToSave.add(optionsBean);
			}
		}
		//all remaining options in optionsDB need to be removed
		for(TCardFieldOptionBean o:optionsDB){
			cardFieldOptionDAO.delete(o.getObjectID());
		}
		for(int i=0;i<optionsToSave.size();i++){
			optionsBean=optionsToSave.get(i);
			optionsBean.setOptionPosition(i);
			saveCardFieldOption(optionsBean);
		}
	}
	
	public static Integer[] getOptionsIDList(List<TCardFieldOptionBean> optionsDB){
		Integer[] filterGroup=new Integer[optionsDB.size()];
		for(int i=0;i<optionsDB.size();i++){
			filterGroup[i]=optionsDB.get(i).getOptionID();
		}
		return filterGroup;
	}
	
	public static List<GroupTO> createGroupTOList(List<ILabelBean> groups, List<TCardFieldOptionBean> optionsDB){
		List<GroupTO> result=new ArrayList<GroupTO>(groups.size());
		int width;
		GroupTO groupTO;
		ILabelBean group;
		for(int i=0;i<groups.size();i++){
			group=groups.get(i);
			groupTO=new GroupTO(group.getObjectID(),group.getLabel());
			TCardFieldOptionBean optionsBean= findOptionBean(optionsDB, group.getObjectID());
			if(optionsBean!=null){
				width=optionsBean.getOptionWidth();
			}else{
				width=DEFAULT_OPTION_WIDTH;
			}
			groupTO.setWidth(width);
			result.add(groupTO);
		}
		return result;
	}
	
	private static TCardFieldOptionBean findOptionBean(List<TCardFieldOptionBean> optionsDB, Integer fieldID){
		for(TCardFieldOptionBean option: optionsDB){
			if(option.getOptionID().equals(fieldID)){
				return option;
			}
		}
		return null;
	}
	
	/**
	 * Changes the width for an option
	 * @param personBean
	 * @param filterID
	 * @param filterType
	 * @param fieldGroup
	 * @param optionID
	 * @param width
	 */
	public static void changeOptionWidth(TPersonBean personBean, Integer filterID, Integer filterType, Integer fieldGroup, Integer optionID,Integer width) {
		Integer navigatorLayoutID = NavigatorLayoutBL.getLayoutID(personBean, filterType, filterID, true);
		TCardGroupingFieldBean cardGroupingFieldBean = cardGroupingFieldDAO.loadByGroupField(navigatorLayoutID,fieldGroup);
		if(cardGroupingFieldBean==null){
			cardGroupingFieldBean=new TCardGroupingFieldBean();
			cardGroupingFieldBean.setNavigatorLayout(navigatorLayoutID);
			cardGroupingFieldBean.setCardField(fieldGroup);
			cardGroupingFieldBean.setSortField(DEFAULT_SORT_FIELD);
			cardGroupingFieldBean.setIsDescending(BooleanFields.FALSE_VALUE);
			saveCardGroupingField(cardGroupingFieldBean);
		}
		Integer cardGroupingFieldID = cardGroupingFieldBean.getObjectID();
		TCardFieldOptionBean optionsBean=cardFieldOptionDAO.loadByCardGroupingFieldAndOption(cardGroupingFieldID, optionID);
		optionsBean.setOptionWidth(width);
		saveCardFieldOption(optionsBean);
	}

	public static TCardPanelBean loadCardTemplate(TPersonBean personBean){
		Integer cardPanelID = getCardPanelBeanID(personBean);
		CardPanelRuntimeBL cardPanelRuntimeBL=CardPanelRuntimeBL.getInstance();
		TCardPanelBean cardPanelBean=(TCardPanelBean)cardPanelRuntimeBL.loadPanelWrapped(cardPanelID);
		return  cardPanelBean;
	}

	public static Integer getCardPanelBeanID(TPersonBean personBean) {
		Integer cardPanelID=null;
		TCardPanelBean cardPanelBean=cardPanelDAO.loadByPerson(personBean.getObjectID());

		if(cardPanelBean!=null){
			cardPanelID=cardPanelBean.getObjectID();
		}else{
			//create default card template
			cardPanelID=createDefaultCardPanel(personBean.getObjectID());
		}
		return cardPanelID;
	}
	public static List<TCardFieldBean> loadFiledsByPanel(Integer panelID){
		return cardFieldDAO.loadByPanel(panelID);
	}

	public static TCardPanelBean loadDesignCardTemplate(TPersonBean personBean){
		CardPanelDesignBL cardPanelDesignBL=CardPanelDesignBL.getInstance();
		Integer cardPanelID = getCardPanelBeanID(personBean);
		cardPanelDesignBL.setLocale(personBean.getLocale());
		TCardPanelBean cardPanelBean=(TCardPanelBean)cardPanelDesignBL.loadPanelWrapped(cardPanelID);
		return  cardPanelBean;
	}
	public static Integer createDefaultCardPanel(Integer personID){
		LOGGER.debug("Create default card panel for:"+personID);
		TCardPanelBean cardPanelBean=new TCardPanelBean();
		cardPanelBean.setPerson(personID);
		cardPanelBean.setRowsNo(3);
		cardPanelBean.setColsNo(3);
		Integer cardPanelID=cardPanelDAO.save(cardPanelBean);

		createCardFieldBean(cardPanelID,SystemFields.INTEGER_ISSUETYPE,0,0,"IssueType",1,true,1,1);
		createCardFieldBean(cardPanelID,SystemFields.INTEGER_ISSUENO,0,1,"IssueNo");
		createCardFieldBean(cardPanelID,SystemFields.INTEGER_RESPONSIBLE,0,2,"Responsible");
		createCardFieldBean(cardPanelID,SystemFields.INTEGER_PRIORITY,1,0,"Priority",1,true,1,1);
		createCardFieldBean(cardPanelID,SystemFields.INTEGER_SYNOPSIS,1,1,"Synopsis",0,true,2,2);
		createCardFieldBean(cardPanelID,SystemFields.INTEGER_SEVERITY,2,0,"Severity",1,true,1,1);
		return cardPanelID;
	}

	private static TCardFieldBean createCardFieldBean(Integer cardPanelID,Integer fieldID,int rowIndex,int colIndex,String name) {
		return createCardFieldBean(cardPanelID, fieldID, rowIndex, colIndex,name,0,true,1,1);
	}
	private static TCardFieldBean createCardFieldBean(Integer cardPanelID,Integer fieldID,int rowIndex,int colIndex,String name,Integer iconRendering, boolean hideLabel,int rowSpan,int colSpan) {
		TCardFieldBean cardFieldBean=new TCardFieldBean();
		cardFieldBean.setCardPanel(cardPanelID);
		cardFieldBean.setRowIndex(rowIndex);
		cardFieldBean.setColIndex(colIndex);
		cardFieldBean.setField(fieldID);
		cardFieldBean.setLabelHAlign(0);
		cardFieldBean.setLabelVAlign(0);
		cardFieldBean.setValueHAlign(0);
		cardFieldBean.setValueVAlign(0);
		cardFieldBean.setRowSpan(rowSpan);
		cardFieldBean.setColSpan(colSpan);
		cardFieldBean.setHideLabelBoolean(hideLabel);
		cardFieldBean.setIconRendering(iconRendering);
		cardFieldBean.setName(name);
		cardFieldDAO.save(cardFieldBean);
		return cardFieldBean;
	}

	public static SortByTO getSortByTO(TPersonBean personBean,Locale locale,TCardGroupingFieldBean cardGroupingFieldBean){
		List<IntegerStringBean> sortFields = getSortFields(personBean, locale);
		List<IntegerStringBooleanBean> fields = new ArrayList<IntegerStringBooleanBean>();
		for(IntegerStringBean field:sortFields){
			IntegerStringBooleanBean integerStringBooleanBean=new IntegerStringBooleanBean();
			integerStringBooleanBean.setLabel(field.getLabel());
			integerStringBooleanBean.setValue(field.getValue());
			integerStringBooleanBean.setSelected(ReportItemJSON.hasExtraSortOrder(field.getValue()));
			fields.add(integerStringBooleanBean);
		}

		Integer sortField=cardGroupingFieldBean.getSortField();
		boolean sortOrder= BooleanFields.fromStringToBoolean(cardGroupingFieldBean.getIsDescending());

		SortByTO sortByTO=new SortByTO();
		sortByTO.setOptions(fields);
		sortByTO.setSortField(sortField);
		sortByTO.setSortOrder(sortOrder);

		return sortByTO;
	}

	/**
	 * Gets the sort fields
	 * @param personBean
	 * @param locale
	 * @return
	 */
	private static List<IntegerStringBean> getSortFields(TPersonBean personBean,Locale locale) {
		List<IntegerStringBean> result=new ArrayList<IntegerStringBean>();
		Set<Integer> fieldsIDs= ColumnFieldsBL.getAvailableRealFieldIDs(personBean.getObjectID());
		if(fieldsIDs.contains(SystemFields.SUPERIORWORKITEM)){
			fieldsIDs.remove(SystemFields.INTEGER_SUPERIORWORKITEM);
		}
		if(fieldsIDs.contains(SystemFields.COMMENT)){
			fieldsIDs.remove(SystemFields.INTEGER_COMMENT);
		}
		if(fieldsIDs.contains(SystemFields.INTEGER_SUBMITTEREMAIL)){
			fieldsIDs.remove(SystemFields.INTEGER_SUBMITTEREMAIL);
		}
		if(fieldsIDs.contains(SystemFields.INTEGER_WBS)){
			fieldsIDs.remove(SystemFields.INTEGER_WBS);
		}
		List<TFieldBean> fieldBeans = FieldBL.loadByFieldIDs(fieldsIDs.toArray());
		Map<Integer, TFieldConfigBean> defaultConfigsMap = FieldRuntimeBL.getLocalizedDefaultFieldConfigsMap(locale);
		Integer fieldID;
		String label = null;
		for(TFieldBean fieldBean:fieldBeans){
			fieldID=fieldBean.getObjectID();
			TFieldConfigBean fieldConfigBean = defaultConfigsMap.get(fieldID);
			if (fieldConfigBean==null || fieldConfigBean.getLabel()==null) {
				label = fieldBean.getName();
			} else {
				label = fieldConfigBean.getLabel();
			}
			result.add(new IntegerStringBean(label,fieldID));
		}
		return result;
	}

	public static Map<Integer, Integer>  getMostSpecificLists(List<Integer> customListFieldIDs,MassOperationContext massOperationContext){
		Map<Integer, Set<Integer>> projectIssueTypeContexts = massOperationContext.getProjectIssueTypeContexts();
		Map<Integer, Integer> fieldIDToListID = new HashMap<Integer, Integer>();
		Map<Integer, Map<Integer, List<TFieldConfigBean>>> fieldIDToListIDToFieldConfigs = CustomListsConfigBL.getMostSpecificLists(
				customListFieldIDs, projectIssueTypeContexts);
		for (Iterator<Integer> iterator = customListFieldIDs.iterator(); iterator.hasNext();) {
			Integer fieldID = iterator.next();
			Map<Integer, List<TFieldConfigBean>> mapForField = fieldIDToListIDToFieldConfigs.get(fieldID);
			if (mapForField==null || mapForField.isEmpty()) {
				//no configuration found for fieldID
				iterator.remove();
			} else {
				if (mapForField.size()>1) {
					//more than one list is involved in field configuration
					iterator.remove();
				} else {
					//get the only listID
					fieldIDToListID.put(fieldID, mapForField.keySet().iterator().next());
				}
			}
		}
		return fieldIDToListID;
	}

	public static GroupByTO getGroupByTO(List<ReportBean> reportBeanList,TCardGroupingFieldBean cardGroupingFieldBean, Set<Integer> presentFieldsOnFormsSet,boolean useRelease, List<Integer> customListFieldIDs,Locale locale){
		List<Integer> listIDs = new ArrayList<Integer>();
		if(reportBeanList==null|| reportBeanList.isEmpty()){
			listIDs.add(SystemFields.INTEGER_ISSUETYPE);
			listIDs.add(SystemFields.INTEGER_STATE);
			listIDs.add(SystemFields.INTEGER_PRIORITY);
			listIDs.add(SystemFields.INTEGER_SEVERITY);
		}else{
			if (presentFieldsOnFormsSet.contains(SystemFields.INTEGER_ISSUETYPE)) {
				listIDs.add(SystemFields.INTEGER_ISSUETYPE);
			}
			if (presentFieldsOnFormsSet.contains(SystemFields.INTEGER_STATE)) {
				listIDs.add(SystemFields.INTEGER_STATE);
			}
			if (presentFieldsOnFormsSet.contains(SystemFields.INTEGER_MANAGER)) {
				listIDs.add(SystemFields.INTEGER_MANAGER);
			}
			if (presentFieldsOnFormsSet.contains(SystemFields.INTEGER_RESPONSIBLE)) {
				listIDs.add(SystemFields.INTEGER_RESPONSIBLE);
			}
			if (presentFieldsOnFormsSet.contains(SystemFields.INTEGER_PRIORITY)) {
				listIDs.add(SystemFields.INTEGER_PRIORITY);
			}
			if (presentFieldsOnFormsSet.contains(SystemFields.INTEGER_SEVERITY)) {
				listIDs.add(SystemFields.INTEGER_SEVERITY);
			}
			if (customListFieldIDs!=null) {
				listIDs.addAll(customListFieldIDs);
			}
		}

		if (useRelease){
			listIDs.add(SystemFields.INTEGER_RELEASESCHEDULED);
		}
		List<IntegerStringBean> fieldsSelect = getOptionFields(locale, listIDs);


		Integer groupField=cardGroupingFieldBean.getCardField();
		if(groupField.intValue()==SystemFields.RELEASESCHEDULED &&useRelease==false){
			groupField=SystemFields.INTEGER_STATE;
		}
		TFieldConfigBean fieldConfigBean= LocalizeUtil.localizeFieldConfig(FieldConfigBL.loadDefault(groupField), locale);
		String groupFieldLabel=fieldConfigBean.getLabel();

		GroupByTO groupByTO=new GroupByTO();
		groupByTO.setOptions(fieldsSelect);
		groupByTO.setId(groupField);
		groupByTO.setLabel(groupFieldLabel);

		return groupByTO;
	}

	private static List<IntegerStringBean> getOptionFields(Locale locale, List<Integer> listFieldIDs) {
		List<IntegerStringBean> fieldsSelect=new ArrayList<IntegerStringBean>();
		Map<Integer, String> fieldIDsToLabels = FieldRuntimeBL.getLocalizedDefaultFieldLabels(listFieldIDs, locale);
		if (fieldIDsToLabels!=null) {
			for (Map.Entry<Integer, String> mapEntry : fieldIDsToLabels.entrySet()) {
				fieldsSelect.add(new IntegerStringBean(mapEntry.getValue(), mapEntry.getKey()));
			}
		}
		Collections.sort(fieldsSelect);
		return fieldsSelect;
	}

	public static List<TReleaseBean> getReleases(List<ReportBean> reportBeanList){
		List<TReleaseBean> releases=null;
		Set<Integer> projectIDs= ReportBeansBL.getProjectIDs(reportBeanList);
		if(projectIDs.size()==1){
			Integer projectID=projectIDs.iterator().next();
			TProjectBean projectBean= LookupContainer.getProjectBean(projectID);
			if (projectBean!=null&& PropertiesHelper.getBooleanProperty(projectBean.getMoreProps(), TProjectBean.MOREPPROPS.USE_RELEASE)){
				releases= ReleaseBL.loadActiveByProject(projectID);
			}
		}
		return  releases;
	}

	public static List<GroupTO> getAllGroups(Map<String, Object> session,TCardGroupingFieldBean cardGroupingFieldBean, List<ReportBean> reportBeanList,
											 List releases, Map<Integer, Integer> fieldIDToListID){
		Locale locale = (Locale) session.get(Constants.LOCALE_KEY);
		TPersonBean personBean = (TPersonBean) session.get(Constants.USER_KEY);

		List<ILabelBean> groupsDB=null;
		Set<Integer> involvedProjectsSet=new HashSet<Integer>();
		Set<Integer> involvedItemTypesSet=new HashSet<Integer>();

		if (reportBeanList!=null) {
			for (ReportBean reportBean : reportBeanList) {
				TWorkItemBean workItemBean = reportBean.getWorkItemBean();
				involvedProjectsSet.add(workItemBean.getProjectID());
				involvedItemTypesSet.add(workItemBean.getListTypeID());
			}
		}

		Integer[] involvedProjectIDs = GeneralUtils.createIntegerArrFromCollection(involvedProjectsSet);
		Integer[] involvedItemTypeIDs = GeneralUtils.createIntegerArrFromCollection(involvedItemTypesSet);


		Integer groupField=cardGroupingFieldBean.getCardField();
		if(groupField.intValue()==SystemFields.RELEASESCHEDULED &&(releases==null||releases.isEmpty())){
			groupField=SystemFields.INTEGER_STATE;
		}

		List<TCardFieldOptionBean> optionsDB=CardViewBL.loadByCardGroupingField(cardGroupingFieldBean.getObjectID());

		int valueType= FieldTypeManager.getFieldTypeRT(groupField).getValueType();
		if(valueType== ValueType.CUSTOMOPTION){
			groupsDB=new ArrayList<ILabelBean>();
			Integer listID = fieldIDToListID.get(groupField);
			if (listID!=null) {
				groupsDB = (List) OptionBL.loadByListID(listID);
			}
		}else{
			switch (groupField){
				case SystemFields.MANAGER:{
					int[] arrRights = new int[] { AccessBeans.AccessFlagIndexes.MANAGER, AccessBeans.AccessFlagIndexes.PROJECTADMIN };
					Set<Integer> managersByRoleSet = AccessBeans.getPersonSetByProjectsRights(involvedProjectIDs, arrRights);
					groupsDB = (List) PersonBL.getDirectAndIndirectPersons(GeneralUtils.createIntegerListFromCollection(managersByRoleSet), true, true, null);
					break;
				}
				case SystemFields.RESPONSIBLE:{
					int[] arrRights = new int[] { AccessBeans.AccessFlagIndexes.RESPONSIBLE, AccessBeans.AccessFlagIndexes.PROJECTADMIN };
					Set<Integer> responsiblesByRoleSet = AccessBeans.getPersonSetByProjectsRights(involvedProjectIDs, arrRights);
					groupsDB = (List)PersonBL.getDirectAndIndirectPersonsAndGroups(GeneralUtils.createIntegerListFromCollection(responsiblesByRoleSet), true, true, null);
					break;
				}
				case SystemFields.RELEASESCHEDULED:{
					groupsDB=releases;
					break;
				}
				case SystemFields.STATE:{
					groupsDB = getStatusOptions(session, locale, personBean, involvedProjectsSet, involvedItemTypeIDs);
					break;
				}
				case SystemFields.ISSUETYPE:
					groupsDB = (List) IssueTypeBL.getByProjectsAndPerson(involvedProjectIDs, personBean.getObjectID(), locale, false);
					break;
				default:{
					groupsDB= LookupContainer.getLocalizedLookup(locale, groupField);
				}
			}
		}
		List<GroupTO> result=createGroupTOList(groupsDB,optionsDB);
		return result;
	}

	private static List<ILabelBean> getStatusOptions(Map<String, Object> session, Locale locale, TPersonBean personBean, Set<Integer> involvedProjectsSet, Integer[] involvedItemTypeIDs) {
		List<ILabelBean> groupsDB;
		List<ILabelBean> usedStatusList=(List<ILabelBean>)session.get(ItemNavigatorBL.USED_STATUS_LIST);
		if(usedStatusList==null){
			List<TProjectBean> projectList = ProjectBL.loadUsedProjectsFlat(personBean.getObjectID());
			Integer[] projectTypeIDs = ProjectTypesBL.getProjectTypeIDsForProjects(projectList);
			usedStatusList= StatusBL.loadAllowedByProjectTypesAndIssueTypes(projectTypeIDs, null, locale);
			session.put(ItemNavigatorBL.USED_STATUS_LIST,usedStatusList);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("User status list " + usedStatusList.size());
		}
		List<TProjectBean> projectList = ProjectBL.loadByProjectIDs(GeneralUtils.createIntegerListFromCollection(involvedProjectsSet));
		Integer[] projectTypeIDs = ProjectTypesBL.getProjectTypeIDsForProjects(projectList);
		List<TStateBean> statusesByProjectTypesAndIssueTypes = StatusBL.loadAllowedByProjectTypesAndIssueTypes(projectTypeIDs, involvedItemTypeIDs);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Project type/item type specific status list " + usedStatusList.size());
		}
		Set<Integer> statusIDsByProjectTypesAndIssueTypes = GeneralUtils.createIntegerSetFromBeanList(statusesByProjectTypesAndIssueTypes);
		groupsDB = new ArrayList<ILabelBean>();
		for (ILabelBean statusBean : usedStatusList) {
			if (statusIDsByProjectTypesAndIssueTypes.contains(statusBean.getObjectID())) {
				groupsDB.add(statusBean);
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Status subset " + groupsDB.size());
		}
		return groupsDB;
	}


	public static Integer[] getSelectedGroups(TCardGroupingFieldBean cardGroupingFieldBean,List<GroupTO> allGroups){
		Integer[] selectedGroups=null;

		List<TCardFieldOptionBean> optionsDB=loadByCardGroupingField(cardGroupingFieldBean.getObjectID());
		Integer[] selectedGroupDB=CardViewBL.getOptionsIDList(optionsDB);
		Integer groupField=cardGroupingFieldBean.getCardField();
		boolean useRelease=false;
		if(groupField.intValue()==SystemFields.RELEASESCHEDULED &&useRelease==false){
			selectedGroupDB=null;
		}

		if(selectedGroupDB==null||selectedGroupDB.length==0){
			if(allGroups!=null){
				int size=0;
				if(allGroups.size()>MAX_COLUMNS){
					size=MAX_COLUMNS;
				}else{
					size=allGroups.size();
				}
				LOGGER.debug("Creating default selected options. Size="+size);
				selectedGroups=new Integer[size];
				for(int i=0;i<size;i++){
					selectedGroups[i]=allGroups.get(i).getObjectID();
				}
			}
		}else{
			if(selectedGroupDB.length<=MAX_COLUMNS){
				selectedGroups=selectedGroupDB;
			}else{
				LOGGER.warn("Options size bigger maximum. Option size="+selectedGroupDB.length+" . Max size="+MAX_COLUMNS);
				selectedGroups=new Integer[MAX_COLUMNS];
				for(int i=0;i<MAX_COLUMNS;i++){
					selectedGroups[i]=selectedGroupDB[i];
				}
			}
		}
		return selectedGroups;
	}
}
