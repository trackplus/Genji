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

package com.aurel.track.itemNavigator.layout.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TNavigatorGroupingSortingBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.NavigatorGroupingSortingDAO;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.itemNavigator.layout.NavigatorLayoutBL;
import com.aurel.track.itemNavigator.layout.column.ColumnFieldsBL;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.BooleanStringBean;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;

/**
 * Utility for group fields
 * @author Tamas
 *
 */
public class LayoutGroupsBL {
	static private Logger LOGGER = LogManager.getLogger(LayoutGroupsBL.class);
	
	private static NavigatorGroupingSortingDAO navigatorGroupingSortingDAO = DAOFactory.getFactory().getNavigatorGroupingSortingDAO();
	
	/**
	 * Loads the fields for a layout
	 * @param navigatorLayoutID
	 * @return
	 */
	public static List<TNavigatorGroupingSortingBean> loadByLayout(Integer navigatorLayoutID) {
		return navigatorGroupingSortingDAO.loadByLayout(navigatorLayoutID);
	}
	
	/**
	 * Loads the fields for a layout
	 * @param navigatorLayoutID
	 * @return
	 */
	public static List<TNavigatorGroupingSortingBean> loadSortingByLayout(Integer navigatorLayoutID) {
		return navigatorGroupingSortingDAO.loadSortingByLayout(navigatorLayoutID);
	}

	/**
	 * Saves a grouping/sorting bean in layout
	 * @param navigatorGroupingSortingBean
	 * @return
	 */
	public static Integer save(TNavigatorGroupingSortingBean navigatorGroupingSortingBean) {
		return navigatorGroupingSortingDAO.save(navigatorGroupingSortingBean);
	}
	
	/**
	 * Removes a grouping/sorting from layout
	 * @param  objectID
	 * @return
	 */
	public static void delete(Integer objectID) {
		navigatorGroupingSortingDAO.delete(objectID);
	}
	
	/**
	 * Deletes all fields from a layout
	 * @param layoutID
	 * @return
	 */
	public static void deleteByLayout(Integer layoutID) {
		navigatorGroupingSortingDAO.deleteByLayout(layoutID);
	}
	
	
	/**
	 * Saves a copy of sourceGroupingSorting with targetLayoutID into the database
	 * @param sourceGroupingSorting
	 * @param targetLayoutID
	 * @return
	 */
	public static List<TNavigatorGroupingSortingBean> cloneNavigatorGroupingSorting(List<TNavigatorGroupingSortingBean> sourceGroupingSorting, Integer targetLayoutID) {
		List<TNavigatorGroupingSortingBean> copiedGroupingSortingBeans = new ArrayList<TNavigatorGroupingSortingBean>();
		if (sourceGroupingSorting!=null) {
			for (TNavigatorGroupingSortingBean navigatorFieldsBean : sourceGroupingSorting) {
				Integer fieldID = navigatorFieldsBean.getField();
				if (NavigatorLayoutBL.fieldIsValid(fieldID)) {
					TNavigatorGroupingSortingBean navigatorGroupingSortingBeanCopy = cloneNavigatorGroupingSortingBean(navigatorFieldsBean, targetLayoutID);
					save(navigatorGroupingSortingBeanCopy);
					copiedGroupingSortingBeans.add(navigatorGroupingSortingBeanCopy);
				}
			}
		}
		return copiedGroupingSortingBeans;
	}
	
	/**
	 * Clones a groupingSorting bean and sets a new layout
	 * @param groupingSortingBean
	 * @param targetLayoutID
	 * @return
	 */
	private static TNavigatorGroupingSortingBean cloneNavigatorGroupingSortingBean(TNavigatorGroupingSortingBean groupingSortingBean,  Integer targetLayoutID) {
		TNavigatorGroupingSortingBean groupingSortingBeanCopy = new TNavigatorGroupingSortingBean();
		groupingSortingBeanCopy.setNavigatorLayout(targetLayoutID);
		groupingSortingBeanCopy.setField(groupingSortingBean.getField());
		groupingSortingBeanCopy.setSortPosition(groupingSortingBean.getSortPosition());
		groupingSortingBeanCopy.setIsGrouping(groupingSortingBean.getIsGrouping());
		groupingSortingBeanCopy.setIsDescending(groupingSortingBean.getIsDescending());
		groupingSortingBeanCopy.setIsCollapsed(groupingSortingBean.getIsCollapsed());
		return groupingSortingBeanCopy;
	}
	
	/**
	 * Return the group fields in a person or filter specific layout
	 * @param personID
	 * @param locale
	 * @param filterType
	 * @param filterID
	 * @return
	 */
	public static SortFieldTO loadGroupFields(TPersonBean personBean,
			Locale locale, Integer filterType, Integer filterID, List<GroupFieldTO> groupFields) {
		Integer layoutID = NavigatorLayoutBL.getLayoutID(personBean, filterType, filterID, true);
		List<TNavigatorGroupingSortingBean> groupingSortingFieldBeans = loadByLayout(layoutID);
		LOGGER.debug("Load grouping/sorting for person " + personBean.getLabel() + " filterType " + filterType + " filterID " + filterID);
		return getSortLoadGroupFields(groupingSortingFieldBeans, locale, false, groupFields);
	}
	
	/**
	 * Completes the grouping levels to maxGroupingLevel
	 * @param groupFieldList
	 * @param maxGroupingLevel
	 * @return
	 */
	static List<GroupFieldTO> completeGroupLevels(List<GroupFieldTO> groupFieldList, int maxGroupingLevel) {
		if (groupFieldList==null) {
			groupFieldList =  new ArrayList<GroupFieldTO>(maxGroupingLevel);
		}
		for (int i = groupFieldList.size(); i < maxGroupingLevel; i++) {
			GroupFieldTO groupField = new GroupFieldTO(null);
			groupField.setDescending(false);
			groupField.setCollapsed(false);
			groupFieldList.add(groupField);
		}
		return groupFieldList;
	}
	
	/**
	 * Get the sorting direction list
	 * @param locale
	 * @return
	 */
	static List<BooleanStringBean> getOrderList(Locale locale) {
		List<BooleanStringBean> ascendingDescendingList = new LinkedList<BooleanStringBean>();
		ascendingDescendingList.add(new BooleanStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.lbl.groupBy.ascending", locale), Boolean.FALSE));
		ascendingDescendingList.add(new BooleanStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.lbl.groupBy.descending", locale), Boolean.TRUE));
		return ascendingDescendingList;
	}
	
	/**
	 * Get the sorting direction list
	 * @param locale
	 * @return
	 */
	static List<BooleanStringBean> getExpandCollapseList(Locale locale) {
		List<BooleanStringBean> expandCollapseList = new ArrayList<BooleanStringBean>();
		expandCollapseList.add(new BooleanStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.lbl.groupBy.expand", locale), Boolean.FALSE));
		expandCollapseList.add(new BooleanStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.lbl.groupBy.collapse", locale), Boolean.TRUE));
		return expandCollapseList;
	}

	/**
	 * Get the localized default field configs for all, 
	 * not deprecated fields which are groupabel 
	 * @param locale
	 * @return
	 */
	public static List<IntegerStringBean> getGroupableFields(Locale locale) {
		List<IntegerStringBean> fieldConfigList = new LinkedList<IntegerStringBean>();
		List<TFieldConfigBean> defaultFieldConfigs = FieldRuntimeBL.getLocalizedDefaultFieldConfigs(locale);
		Map<Integer, TFieldBean> fieldsMap = GeneralUtils.createMapFromList(FieldBL.loadActive());	
		for (TFieldConfigBean fieldConfigBean : defaultFieldConfigs) {
			Integer fieldID = fieldConfigBean.getField();
			TFieldBean fieldBean = fieldsMap.get(fieldID);
			if (fieldBean!=null && FieldTypeManager.isGroupable(fieldID)) {
				fieldConfigList.add(new IntegerStringBean(fieldConfigBean.getLabel(), fieldID));
			}
		}
		Collections.sort(fieldConfigList);
		return fieldConfigList;
	}
	
	/**
	 * Saves the grouping
	 * @param personID
	 * @param filterType
	 * @param filterID
	 * @param groupFields
	 */
	static void saveGrouping(TPersonBean personBean, Integer filterType, Integer filterID, List<GroupFieldTO> groupFields) {
		Integer layoutID = NavigatorLayoutBL.getLayoutID(personBean, filterType, filterID, true);
		List<TNavigatorGroupingSortingBean> groupingSortingFieldBeans = loadByLayout(layoutID);
		if (groupingSortingFieldBeans!=null) {
			for (TNavigatorGroupingSortingBean navigatorGroupingSortingBean : groupingSortingFieldBeans) {
				if (navigatorGroupingSortingBean.isGrouping()) {
					delete(navigatorGroupingSortingBean.getObjectID());
				}
			}
		}
		int i = 1;
		LOGGER.debug("Save grouping for person " + personBean.getLabel() + " filterType " + filterType + " filterID " + filterID);
		for (GroupFieldTO groupField : groupFields) {
			TNavigatorGroupingSortingBean navigatorGroupingSortingBean = new TNavigatorGroupingSortingBean();
			navigatorGroupingSortingBean.setNavigatorLayout(layoutID);
			navigatorGroupingSortingBean.setField(groupField.getFieldID());
			navigatorGroupingSortingBean.setSortPosition(i++);
			navigatorGroupingSortingBean.setGrouping(true);
			navigatorGroupingSortingBean.setDescending(groupField.isDescending());
			navigatorGroupingSortingBean.setCollapsed(groupField.isCollapsed());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(groupField.toString());
			}
			save(navigatorGroupingSortingBean);
		}
	}

	/**
	 * Return the sort field and loads the group fields in the list
	 * @param navigatorGroupingSortingBeans
	 * @param locale
	 * @param withLabel
	 * @param groupFields output parameter
	 * @return
	 */
	public static SortFieldTO getSortLoadGroupFields(
			List<TNavigatorGroupingSortingBean> navigatorGroupingSortingBeans,
			Locale locale, boolean withLabel, List<GroupFieldTO> groupFields) {
		List<Integer> realFieldIDs = new LinkedList<Integer>();
		List<Integer> pseudoFieldIDs = new LinkedList<Integer>();
		SortFieldTO sortField = null;
		if (navigatorGroupingSortingBeans!=null) {
			for (TNavigatorGroupingSortingBean navigatorGroupingSortingBean : navigatorGroupingSortingBeans) {
				Integer fieldID = navigatorGroupingSortingBean.getField();
				if (fieldID!=null) {
					if (fieldID.intValue()>0) {
						realFieldIDs.add(fieldID);
					} else {
						pseudoFieldIDs.add(fieldID);
					}
				}
			}
			List<TFieldBean> fieldBeans = FieldBL.loadByFieldIDs(realFieldIDs.toArray());
			Map<Integer, ILabelBean> fieldsMap = GeneralUtils.createMapFromList(fieldBeans);
			Map<Integer, String> defaultConfigsMap = null;
			if (withLabel) {
				defaultConfigsMap = FieldRuntimeBL.getLocalizedDefaultFieldLabels(realFieldIDs, locale);
			}
			for (Iterator<TNavigatorGroupingSortingBean> iterator = navigatorGroupingSortingBeans.iterator(); iterator.hasNext();) {
				TNavigatorGroupingSortingBean navigatorGroupingSortingBean = iterator.next();
				Integer fieldID = navigatorGroupingSortingBean.getField();
				if (fieldID!=null) {
					String fieldName = null;
					String label = null;
					if (fieldID>0) {
						//real field
						ILabelBean labelBean = fieldsMap.get(fieldID);
						if (labelBean==null) {
							//field was deleted
							iterator.remove();
							continue;
						}  else {
							fieldName =  labelBean.getLabel();
							if (defaultConfigsMap!=null) {
								label = defaultConfigsMap.get(fieldID);
								if (label==null) {
									label = fieldName;
								}
							}
						}
					} else {
						//pseudo field: typically for sorting (we do not group by pseudo fields)
						fieldName = ColumnFieldsBL.getPseudoColumnName(fieldID);
						label = ColumnFieldsBL.getPseudoColumnLabel(fieldID, locale);
					}
					boolean isGrouping = BooleanFields.fromStringToBoolean(navigatorGroupingSortingBean.getIsGrouping());
					boolean descending = BooleanFields.fromStringToBoolean(navigatorGroupingSortingBean.getIsDescending());
					if (isGrouping) {
						GroupFieldTO groupField = new GroupFieldTO(fieldID, label);
						groupField.setName(fieldName);
						groupField.setSortPosition(navigatorGroupingSortingBean.getSortPosition());
						groupField.setDescending(descending);
						groupField.setCollapsed(BooleanFields.fromStringToBoolean(navigatorGroupingSortingBean.getIsCollapsed()));
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug(groupField.toString());
						}
						groupFields.add(groupField);
					} else {
						sortField = new SortFieldTO(fieldID, label);
						sortField.setName(fieldName);
						sortField.setDescending(descending);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug(sortField.toString());
						}
					}				
				}
			}
		}
		return sortField;
	}

	/**
	 * Returns the first duplicate column by grouping
	 * @param groupColumns
	 * @return
	 */
	public static Integer duplicatedGroupByColumn(List<GroupFieldTO> groupColumns) {
		if (groupColumns!=null) {
			Set<Integer> fields = new HashSet<Integer>();
			for (GroupFieldTO groupFieldTO : groupColumns) {
				if (fields.contains(groupFieldTO.getFieldID())) {
					return groupFieldTO.getFieldID();
				} else {
					fields.add(groupFieldTO.getFieldID());
				}
			}
		}
		return null;
	}
	
	

}
