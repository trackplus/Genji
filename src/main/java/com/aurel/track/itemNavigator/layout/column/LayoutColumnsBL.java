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

package com.aurel.track.itemNavigator.layout.column;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TNavigatorColumnBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.NavigatorColumnDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.itemNavigator.layout.NavigatorLayoutBL;

/**
 * Managing the layout columns
 * @author Tamas
 *
 */
public class LayoutColumnsBL {
	static private Logger LOGGER = LogManager.getLogger(LayoutColumnsBL.class);
	private static NavigatorColumnDAO navigatorColumnsDAO = DAOFactory.getFactory().getNavigatorFieldsDAO();
	
	//the columns in the default layout
	private static final int[] defaultFields = {TReportLayoutBean.PSEUDO_COLUMNS.CHECKBOX_FIELD_ID,
			SystemFields.ISSUENO, SystemFields.PROJECT,
			SystemFields.SYNOPSIS, SystemFields.STATE,
			SystemFields.RESPONSIBLE, SystemFields.LASTMODIFIEDDATE };

	private static final int[] defaultFieldsSize = {21, 130, 100, 220, 100, 130, 120};
	
	/**
	 * Loads the fields for a layout
	 * @param navigatorLayoutID
	 * @return
	 */
	private static TNavigatorColumnBean loadByPrimaryKey(Integer navigatorColumnID) {
		return navigatorColumnsDAO.loadByPrimaryKey(navigatorColumnID);
	}
	
	/**
	 * Loads the fields for a layout
	 * @param navigatorLayoutID
	 * @return
	 */
	public static List<TNavigatorColumnBean> loadByLayout(Integer navigatorLayoutID) {
		return navigatorColumnsDAO.loadByLayout(navigatorLayoutID);
	}
	
	
	/**
	 * Loads the fields for a layout
	 * @param navigatorLayoutID
	 * @param fieldID
	 * @return
	 */
	public static List<TNavigatorColumnBean> loadByLayoutAndField(Integer navigatorLayoutID, Integer fieldID) {
		return navigatorColumnsDAO.loadByLayoutAndField(navigatorLayoutID, fieldID);
	}
	
	/**
	 * Saves a navigator field in layout
	 * @param navigatorLayoutBean
	 * @return
	 */
	private static Integer save(TNavigatorColumnBean navigatorColumnBean) {
		return navigatorColumnsDAO.save(navigatorColumnBean);
	}
	
	/**
	 * Deletes a field from layout
	 * @param  objectID
	 * @return
	 */
	private static void delete(Integer objectID) {
		navigatorColumnsDAO.delete(objectID);
	}
	
	/**
	 * Deletes all fields from a layout
	 * @param layoutID
	 * @return
	 */
	public static void deleteByLayout(Integer layoutID) {
		navigatorColumnsDAO.deleteByLayout(layoutID);
	}
	
	/**
	 * Creates a default layout
	 * @param layoutID
	 * @return
	 */
	public static List<TNavigatorColumnBean> createDefaultFieldsLayout(Integer layoutID) {
		List<TNavigatorColumnBean> layoutColumns = LayoutColumnsBL.loadByLayout(layoutID);
		if (layoutColumns==null || layoutColumns.isEmpty()) {
			layoutColumns = new ArrayList<TNavigatorColumnBean>(defaultFields.length);
			for (int i = 0; i < defaultFields.length; i++) {
				TNavigatorColumnBean navigatorFieldsBean = createNavigatorFieldBean(layoutID, defaultFields[i], i+1, defaultFieldsSize[i]);
				save(navigatorFieldsBean);
				layoutColumns.add(navigatorFieldsBean);
			}
		}
		return layoutColumns;
	}
	
	/**
	 * Creates a new TNavigatorColumnBean
	 * @param layoutID
	 * @param fieldID
	 * @param pos
	 * @param width
	 * @return
	 */
	private static TNavigatorColumnBean createNavigatorFieldBean(Integer layoutID, Integer fieldID, int pos, int width) {
		TNavigatorColumnBean navigatorColumnBean = new TNavigatorColumnBean();
		navigatorColumnBean.setNavigatorLayout(layoutID);
		navigatorColumnBean.setField(fieldID);
		navigatorColumnBean.setFieldPosition(Integer.valueOf(pos));
		navigatorColumnBean.setFieldWidth(Integer.valueOf(width));
		return navigatorColumnBean;
	}
	
	/**
	 * Clone a list of navigator field beans
	 * @param sourceColumnBeans
	 * @param targetLayoutID
	 * @return
	 */
	public static List<TNavigatorColumnBean> cloneNavigatorColumnBeanList(List<TNavigatorColumnBean> sourceColumnBeans, Integer targetLayoutID) {
		List<TNavigatorColumnBean> copiedFieldBeans = new ArrayList<TNavigatorColumnBean>();
		if (sourceColumnBeans!=null) {
			for (TNavigatorColumnBean navigatorColumnBean : sourceColumnBeans) {
				Integer fieldID = navigatorColumnBean.getField();
				if (NavigatorLayoutBL.fieldIsValid(fieldID)) {
					TNavigatorColumnBean navigatorFieldsBeanCopy = cloneNavigatorFieldBean(navigatorColumnBean, targetLayoutID);
					navigatorFieldsBeanCopy.setNavigatorLayout(targetLayoutID);
					save(navigatorFieldsBeanCopy);
					copiedFieldBeans.add(navigatorFieldsBeanCopy);
				}
			}
		}
		return copiedFieldBeans;
	}
	
	/**
	 * Clones a navigatorFieldBean and sets a new layout
	 * @param navigatorColumnBean
	 * @param targetLayoutID
	 * @return
	 */
	private static TNavigatorColumnBean cloneNavigatorFieldBean(TNavigatorColumnBean navigatorColumnBean, Integer targetLayoutID) {
		TNavigatorColumnBean navigatorFieldsBeanCopy = new TNavigatorColumnBean();
		navigatorFieldsBeanCopy.setNavigatorLayout(targetLayoutID);
		navigatorFieldsBeanCopy.setField(navigatorColumnBean.getField());
		navigatorFieldsBeanCopy.setFieldPosition(navigatorColumnBean.getFieldPosition());
		navigatorFieldsBeanCopy.setFieldWidth(navigatorColumnBean.getFieldWidth());
		return navigatorFieldsBeanCopy;
	}
	
	
	
	
	/**
	 * Gets all columns for choose columns
	 * @param personBean
	 * @param locale
	 * @param queryTypeSession
	 * @param queryIDSession
	 * @param includeLongFields
	 * @return
	 */
	public static List<ChooseColumnTO> getAllColumns(TPersonBean personBean,
			Locale locale, Integer filterType, Integer filterID, boolean includeLongFields) {
		List<ChooseColumnTO> chooseColumnTOs = new LinkedList<ChooseColumnTO>();
		Set<Integer> selectedLayoutColumns = loadSelectedColumnFields(personBean, locale, filterType, filterID);
		List<ColumnFieldTO> allFields = ColumnFieldsBL.loadAvailableColumnFields(personBean.getObjectID(), locale, false);
		for (ColumnFieldTO columnField : allFields) {
			Integer fieldID = columnField.getFieldID();
			if (includeLongFields || !columnField.isRenderAsLong() ) {
				ChooseColumnTO chooseColumnTO = new ChooseColumnTO(fieldID, columnField.getLabel());
				if (selectedLayoutColumns.contains(fieldID)) {
					chooseColumnTO.setUsed(true);
				}
				chooseColumnTOs.add(chooseColumnTO);
			}
		}
		return chooseColumnTOs;
	}
	
	/**
	 * Return the column fields used in a person or filter specific layout
	 * @param personID
	 * @param locale
	 * @param filterType
	 * @param filterID
	 * @return
	 */
	private static Set<Integer> loadSelectedColumnFields(TPersonBean personBean,
			Locale locale, Integer filterType, Integer filterID) {
		Set<Integer> columnFieldIDs = new HashSet<Integer>();
		Integer layoutID = NavigatorLayoutBL.getLayoutID(personBean, filterType, filterID, true);
		if (layoutID!=null) {
			List<TNavigatorColumnBean> columnFieldBeans = loadByLayout(layoutID);
			if (columnFieldBeans!=null) {
				for (TNavigatorColumnBean navigatorColumnBean : columnFieldBeans) {
					columnFieldIDs.add(navigatorColumnBean.getField());
				}
			}
		}
		return columnFieldIDs;
	}
	
	/**
	 * Saves the columns layout
	 * @param personBean
	 * @param selectedColumnsMap
	 * @param locale
	 * @param filterType
	 * @param filterID
	 */
	static void saveLayout(TPersonBean personBean,
			Map<String, Boolean> selectedColumnsMap, Locale locale, Integer filterType, Integer filterID){
		Integer layoutID = NavigatorLayoutBL.getLayoutID(personBean, filterType, filterID, true);
		List<TNavigatorColumnBean> originalColumnBeans = loadByLayout(layoutID);
		LOGGER.debug("Save columns for person " + personBean.getLabel() + ", filterType " + filterType + ", filterID " + filterID);
		List<TNavigatorColumnBean> newColumnBeans = new LinkedList<TNavigatorColumnBean>();
		List<Integer> newColumnIDs=new ArrayList<Integer>();
		for (Iterator<TNavigatorColumnBean> iterator = originalColumnBeans.iterator(); iterator.hasNext();) {
			TNavigatorColumnBean navigatorColumnBean = iterator.next();
			Integer fieldID = navigatorColumnBean.getField();
			String key;
			if (fieldID.intValue()<0) {
				key="f_"+(fieldID.intValue()*-1);
			}else{
				key="f"+fieldID.intValue();
			}
			Boolean select = selectedColumnsMap.get(key);
			if (select!=null && select.booleanValue()) {
				LOGGER.debug("Field " + navigatorColumnBean.getField() + " maintained ");
				newColumnIDs.add(fieldID);
			} else {
				LOGGER.debug("Field " + navigatorColumnBean.getField() + " removed ");
				delete(navigatorColumnBean.getObjectID());
				iterator.remove();
			}
		}
		//LOGGER.debug("Columns from original layout="+newColumnIDs);
		newColumnBeans.addAll(originalColumnBeans);
		//add the rest of selected columns 
		List<ColumnFieldTO> allFields = ColumnFieldsBL.loadAvailableColumnFields(personBean.getObjectID(), locale, false);
		Map<Integer, ColumnFieldTO> columnFieldMap = new HashMap<Integer, ColumnFieldTO>();
		for (ColumnFieldTO columnFieldTO : allFields) {
			columnFieldMap.put(columnFieldTO.getFieldID(), columnFieldTO);
		}
		for (ColumnFieldTO columnFieldTO : allFields) {
			Integer fieldID=columnFieldTO.getFieldID();
			String key;
			if(fieldID<0){
				key="f_"+(fieldID.intValue()*-1);
			}else{
				key="f"+fieldID.intValue();
			}
			Boolean select = selectedColumnsMap.get(key);
			if(select!=null && select.booleanValue()){
				if (!isInLayout(newColumnIDs, fieldID)){
					//add the new reportLayout
					TNavigatorColumnBean navigatorColumnBean = createNavigatorColumn(layoutID, columnFieldTO, newColumnIDs.size(),columnFieldTO.getFieldWidth());
					LOGGER.debug("Add field "+navigatorColumnBean.getField());
					switch (fieldID){
						case TReportLayoutBean.PSEUDO_COLUMNS.CHECKBOX_FIELD_ID:{
							newColumnBeans.add(0,navigatorColumnBean);
							break;
						}
						case TReportLayoutBean.PSEUDO_COLUMNS.INDEX_NUMBER:{
							int pos=0;
							if (isInLayout(newColumnIDs, TReportLayoutBean.PSEUDO_COLUMNS.CHECKBOX_FIELD_ID)){
								pos=1;
							}
							newColumnBeans.add(pos,navigatorColumnBean);
							break;
						}
						default:{
							newColumnBeans.add(navigatorColumnBean);
						}
					}
				}
			}
		}
		newColumnBeans = ensureLongFieldsAtEnd(newColumnBeans, columnFieldMap);
		//ensure position and person
		for (int i = 0; i < newColumnBeans.size(); i++) {
			TNavigatorColumnBean columnBean = newColumnBeans.get(i);
			columnBean.setFieldPosition(i+1);
			save(columnBean);
		}
	}
	
	/**
	 * Creates a TNavigatorColumnBean from a columnFieldTO
	 * @param layoutID
	 * @param columnFieldTO
	 * @param pos
	 * @param width
	 * @return
	 */
	private static TNavigatorColumnBean createNavigatorColumn(Integer layoutID, ColumnFieldTO columnFieldTO, int pos, Integer width) {
		TNavigatorColumnBean navigatorColumnBean = new TNavigatorColumnBean();
		navigatorColumnBean.setNavigatorLayout(layoutID);		
		navigatorColumnBean.setField(columnFieldTO.getFieldID());
		navigatorColumnBean.setFieldPosition(Integer.valueOf(pos));
		navigatorColumnBean.setFieldWidth(width);
		return navigatorColumnBean;
	}
	
	/**
	 * 
	 * @param columnFields
	 * @param fieldID
	 * @return
	 */
	public static boolean isInLayout(Integer fieldID, List<ColumnFieldTO> columnFields) {
		if (columnFields==null || columnFields.isEmpty()) {
			return false;
		}
		for (ColumnFieldTO columnField : columnFields) {
			if (columnField.getFieldID().equals(fieldID)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param columnFields
	 * @param fieldID
	 * @return
	 */
	public static boolean isInLayout(List<Integer> columnFields, Integer fieldID) {
		if (columnFields==null || columnFields.isEmpty()) {
			return false;
		}
		for (Integer columnField : columnFields) {
			if (columnField.equals(fieldID)) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Put the long fields at the end
	 * @param columnBeans
	 * @return
	 */
	private static List<TNavigatorColumnBean> ensureLongFieldsAtEnd(List<TNavigatorColumnBean> columnBeans, Map<Integer, ColumnFieldTO> columnFieldMap){
		List<TNavigatorColumnBean> result=new ArrayList<TNavigatorColumnBean>();
		List<TNavigatorColumnBean> simpleFieldList=new ArrayList<TNavigatorColumnBean>();
		List<TNavigatorColumnBean> longFieldList=new ArrayList<TNavigatorColumnBean>();
		for (TNavigatorColumnBean columnBean : columnBeans) {
			ColumnFieldTO columnFieldTO = columnFieldMap.get(columnBean.getField());
			if (columnFieldTO.isRenderAsLong()) {
				longFieldList.add(columnBean);
				//field width is required
				columnBean.setFieldWidth(Integer.valueOf(0));
			} else {
				simpleFieldList.add(columnBean);
			}
		}
		result.addAll(simpleFieldList);
		result.addAll(longFieldList);
		return result;
	}
	
	/**
	 * Resize the column
	 * @param personBean
	 * @param layoutID
	 * @param width
	 */
	static void resizeColumn(TPersonBean personBean, /*Integer layoutID,*/Integer filterType, Integer filterID, Integer fieldID, Integer width) {
		if (fieldID==null) {
			LOGGER.info("No field specified");
			return;
		}
		Integer layoutID = NavigatorLayoutBL.getLayoutID(personBean, filterType, filterID, true);
		List<TNavigatorColumnBean> columns = loadByLayoutAndField(layoutID, fieldID);
		if (columns==null || columns.isEmpty()) {
			LOGGER.info("No column found for field " + fieldID);
			return;
		}
		LOGGER.debug("Resize column by " + personBean.getLabel() + " layoutID " + layoutID + " width " + width);
		TNavigatorColumnBean navigatorFieldsBean = columns.get(0);// loadByPrimaryKey(layoutID);
		if (navigatorFieldsBean!=null){
			navigatorFieldsBean.setFieldWidth(width);
			save(navigatorFieldsBean);
		}
	}
	
	/**
	 * Whether the column is included in the list
	 * @param navigatorColumns
	 * @param id
	 * @return
	 */
	private static boolean columnIsIncluded(List<TNavigatorColumnBean> navigatorColumns, int id){
		for (TNavigatorColumnBean navigatorColumnBean : navigatorColumns) {
			if (navigatorColumnBean.getField().intValue()==id) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Move a column
	 * @param personID
	 * @param filterType
	 * @param filterID
	 * @param from
	 * @param to
	 * @return
	 */
	static String moveColumn(TPersonBean personBean, Integer filterType, Integer filterID, int from, int to) {
		Integer layoutID = NavigatorLayoutBL.getLayoutID(personBean, filterType, filterID, true);
		List<TNavigatorColumnBean> columnBeans = loadByLayout(layoutID);
		boolean bulkEdit= columnIsIncluded(columnBeans, TReportLayoutBean.PSEUDO_COLUMNS.CHECKBOX_FIELD_ID);
		boolean indexNumber= columnIsIncluded(columnBeans, TReportLayoutBean.PSEUDO_COLUMNS.INDEX_NUMBER);
		int firstPosition=0;
		if (bulkEdit){
			firstPosition++;
		}
		if (indexNumber) {
			firstPosition++;
		}
		if (to<firstPosition) {
			LOGGER.info("Can't move before mass operation or index column");
			return null;
		}
		
		if (from==to || from + 1==to) {
			LOGGER.debug("No move. From " + from + " to " + to);
			return null;
		}
		LOGGER.debug("Move column from index " + from + " to index " + to + " for person " + personBean.getLabel() + " fiterType " + filterType + " filterID " + filterID);
		
		if (columnBeans==null) {
			LOGGER.warn("No layout found");
			return null;
		} else {
			LOGGER.debug("Number of layout columns " + columnBeans.size());
			if (columnBeans.size()<from+1) {
				LOGGER.warn("Outdated layout: column size " + columnBeans.size() + " from " + from);
				return null;
			} else {
				if (columnBeans.size()<to) {
					LOGGER.warn("Outdated layout: column size " + columnBeans.size() + " to " + to);
					return null;
				} 
			}
		}
		TNavigatorColumnBean columnBeanFrom = columnBeans.get(from);
		LOGGER.debug("Move the field " + columnBeanFrom.getField());
		if (to==firstPosition && isRenderContentAsImg(columnBeanFrom.getField())) {
			LOGGER.warn("Can't move a image column as first column!");
			return "itemov.err.iconColumnFirst";
		}
		if (from==firstPosition && columnBeans.size()>from+1) {
			TNavigatorColumnBean nextFirstColumn= columnBeans.get(from+1);
			if (isRenderContentAsImg(nextFirstColumn.getField())){
				LOGGER.warn("Can't leave a image column as first column!");
				return "itemov.err.iconColumnFirst";
			}
		}
		columnBeans.remove(from);
		//from and to are 0 based.
		//fieldPosition is 1 based
		if (from < to) {
			for (int i = from; i < to - 1; i++) {// i<to-1 because we remove the from element
				TNavigatorColumnBean columnBean = columnBeans.get(i);
				//the actual filePosition is (i+2): +1 because from is 0 based fieldPosition is 1 based
				//+1 because "from" was removed so the next item (with previous index = i+1) is taken from the list
				LOGGER.debug("Shift field " + columnBean.getField() + " to left from " + (i+2) + " to " + (i+1));
				//columnBean.setFieldPosition(Integer.valueOf(columnBean.getFieldPosition().intValue() - 1));
				columnBean.setFieldPosition(i+1);
				save(columnBean);
			}
			//add on to position
			columnBeanFrom.setFieldPosition(Integer.valueOf(to));
			LOGGER.debug("Save field " + columnBeanFrom.getField() + " to position " + to);
			save(columnBeanFrom);
		} else {
			for (int i = to; i < from; i++) {//i<from-1 because we remove the from element
				TNavigatorColumnBean columnBean = columnBeans.get(i);
				//the actual filePosition is (i+1): +1 because from is 0 based fieldPosition is 1 based
				//although "from" was removed but it does not influence the items before "from"
				LOGGER.debug("Shift field " + columnBean.getField() + " to right from " + (i+1) + " to " + (i+2));
				//columnBean.setFieldPosition(Integer.valueOf(columnBean.getFieldPosition().intValue() + 1));
				columnBean.setFieldPosition(i+2);
				save(columnBean);
			}
			columnBeanFrom.setFieldPosition(Integer.valueOf(to + 1));
			LOGGER.debug("Save field " + columnBeanFrom.getField() + " to position " + (to+1));
			save(columnBeanFrom);
		}
		return null;
	}
	
	/**
	 * Whether the content is rendered as image
	 * @param fieldID
	 * @return
	 */
	private static boolean isRenderContentAsImg(Integer fieldID) {
		if (fieldID==null) {
			return false;
		}
		switch (fieldID.intValue()) {
		case TReportLayoutBean.PSEUDO_COLUMNS.PRIVATE_SYMBOL:
		case TReportLayoutBean.PSEUDO_COLUMNS.ISSUETYPE_SYMBOL:
		case TReportLayoutBean.PSEUDO_COLUMNS.OVERFLOW_ICONS:
		case TReportLayoutBean.PSEUDO_COLUMNS.PRIORITY_SYMBOL:
		case TReportLayoutBean.PSEUDO_COLUMNS.SEVERITY_SYMBOL:
		case TReportLayoutBean.PSEUDO_COLUMNS.STATUS_SYMBOL:
				return true;
		default:
			if (fieldID.intValue()<0) {
				Integer realFieldID = ColumnFieldsBL.getCustomListFieldFromIconField(fieldID);
				if (realFieldID>0) {
					IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(realFieldID);
					if (fieldTypeRT.getValueType()==ValueType.CUSTOMOPTION || fieldTypeRT.isComposite()) {
						return true;
					}
				}
			}
			return false;
		}
	}
}
