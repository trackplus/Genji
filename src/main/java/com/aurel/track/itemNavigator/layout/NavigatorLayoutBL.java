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

package com.aurel.track.itemNavigator.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.itemNavigator.cardView.CardViewBL;
import com.aurel.track.resources.LocalizeUtil;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.CategoryBL.REPOSITORY_TYPE;
import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TNavigatorColumnBean;
import com.aurel.track.beans.TNavigatorGroupingSortingBean;
import com.aurel.track.beans.TNavigatorLayoutBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.NavigatorLayoutDAO;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.itemNavigator.ItemNavigatorBL.QUERY_TYPE;
import com.aurel.track.itemNavigator.layout.column.ColumnFieldTO;
import com.aurel.track.itemNavigator.layout.column.ColumnFieldsBL;
import com.aurel.track.itemNavigator.layout.column.LayoutColumnsBL;
import com.aurel.track.itemNavigator.layout.group.GroupFieldTO;
import com.aurel.track.itemNavigator.layout.group.LayoutGroupsBL;
import com.aurel.track.itemNavigator.layout.group.SortFieldTO;

/**
 * Logic for item navigator layout
 * @author Tamas
 *
 */
public class NavigatorLayoutBL {
	static private Logger LOGGER = LogManager.getLogger(NavigatorLayoutBL.class);
	
	private static NavigatorLayoutDAO navigatorLayoutDAO = DAOFactory.getFactory().getNavigatorLayoutDAO();
	
	/**
	 * Load the default navigator layout
	 * @return
	 */
	private static TNavigatorLayoutBean loadDefaultLayout() {
		List<TNavigatorLayoutBean> defaultLayoutBeans = navigatorLayoutDAO.loadDefault();
		if (defaultLayoutBeans==null || defaultLayoutBeans.isEmpty()) {
			return null;
		}
		return defaultLayoutBeans.get(0);
	}
	
	/**
	 * Load navigator layout by user
	 * @param personID
	 * @return
	 */
	private static TNavigatorLayoutBean loadPersonLayout(Integer personID) {
		List<TNavigatorLayoutBean> personLayoutBeans = navigatorLayoutDAO.loadByPerson(personID);
		if (personLayoutBeans==null || personLayoutBeans.isEmpty()) {
			return null;
		}
		return personLayoutBeans.get(0);
	}
	
	/**
	 * Loads the layout beans for a filter
	 * @param personID
	 * @param filterID
	 * @param filterType
	 * @return
	 */
	public static TNavigatorLayoutBean loadByPersonAndFilter(Integer personID, Integer filterID, Integer filterType) {
		List<TNavigatorLayoutBean> filterLayoutBeans = navigatorLayoutDAO.loadByPersonAndFilter(personID, filterID, filterType);
		if (filterLayoutBeans==null || filterLayoutBeans.isEmpty()) {
			return null;
		}
		return filterLayoutBeans.get(0);
	}
	
	/**
	 * Load navigator layout bean
	 * @param filterID
	 * @param filterType
	 * @return
	 */
	public static TNavigatorLayoutBean loadByFilterWithView(Integer filterID, Integer filterType) {
		List<TNavigatorLayoutBean> filterWithViewLayoutBeans = navigatorLayoutDAO.loadByFilterWithView(filterID, filterType);
		if (filterWithViewLayoutBeans==null || filterWithViewLayoutBeans.isEmpty()) {
			return null;
		}
		return filterWithViewLayoutBeans.get(0);
	}
	
	/**
	 * Saves a navigator layout bean
	 * @param navigatorLayoutBean
	 * @return
	 */
	public static Integer saveLayout(TNavigatorLayoutBean navigatorLayoutBean) {
		return navigatorLayoutDAO.save(navigatorLayoutBean);
	}
	
	/**
	 * Deletes a navigator layout with dependencies
	 * @param layoutID
	 * @return
	 */
	public static void delete(Integer layoutID) {
		navigatorLayoutDAO.delete(layoutID);
	}
	
	/**
	 * Loads or the default layoutID.
	 * If it does not exist then create it
	 * @return
	 */
	private static Integer loadOrCreateDefaultLayoutID() {
		Integer defaultLayoutID = null;
		TNavigatorLayoutBean defaultLayoutBean = loadDefaultLayout();
		boolean add = false;
		if (defaultLayoutBean==null) {
			//first use ever: save the default layout
			TNavigatorLayoutBean navigatorLayoutBean = new TNavigatorLayoutBean();
			defaultLayoutID = saveLayout(navigatorLayoutBean);
			add = true;
		} else {
			//default exist
			defaultLayoutID = defaultLayoutBean.getObjectID();
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(getDeafultLayoutString(defaultLayoutID, add));
		}
		return defaultLayoutID;
	}
	
	/**
	 * Debug string for person with filter
	 * @param personFilterLayoutID
	 * @param filterID
	 * @param filterType
	 * @param viewID
	 * @param add
	 * @return
	 */
	private static String getDeafultLayoutString(Integer defaultLayoutID, boolean add) {
		String addString = null;
		if (add) {
			addString = "added: ";
		} else {
			addString = "loaded: ";
		}
		return "Default layout " + addString + defaultLayoutID;
	}
	
	/**
	 * Load the person specific layoutID (filter layout not activated for person)
	 * If it does not exist then create it
	 * @param personID
	 * @return
	 */
	private static Integer loadOrCreatePersonLayoutID(Integer personID) {
		Integer personLayoutID = null;
		TNavigatorLayoutBean personLayoutBean = loadPersonLayout(personID);
		boolean add = false;
		if (personLayoutBean==null) {
			//no layout for person, create a new one
			personLayoutBean = new TNavigatorLayoutBean();
			personLayoutBean.setPerson(personID);
			personLayoutID = saveLayout(personLayoutBean);
			add = true;
		} else {
			personLayoutID = personLayoutBean.getObjectID();
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(getPersonLayoutString(personLayoutID, personID, add));
		}
		return personLayoutID;
	}
	
	/**
	 * Debug string for person with filter
	 * @param personFilterLayoutID
	 * @param filterID
	 * @param filterType
	 * @param viewID
	 * @param add
	 * @return
	 */
	private static String getPersonLayoutString(Integer personFilterLayoutID, Integer personID, boolean add) {
		String addString = null;
		if (add) {
			addString = "added: ";
		} else {
			addString = "loaded: ";
		}
		return "Person with filter (" + personID +") layout " + addString + personFilterLayoutID;
	}
	
	/**
	 * Loads the person and filter specific layoutID (filter layout activated for person).
	 * If it does not exist then create it
	 * @param personID
	 * @param filterID
	 * @param filterType
	 * @return
	 */
	private static Integer loadOrCreatePersonAndFilterLayoutID(Integer personID, Integer filterID, Integer filterType) {
		TNavigatorLayoutBean personAndFilterLayoutBean = loadByPersonAndFilter(personID, filterID, filterType);
		Integer personFilterLayoutID = null;
		boolean add = false;
		if (personAndFilterLayoutBean==null) {
			//no layout for person, create a new one
			personAndFilterLayoutBean = new TNavigatorLayoutBean();
			personAndFilterLayoutBean.setPerson(personID);
			personAndFilterLayoutBean.setFilterID(filterID);
			personAndFilterLayoutBean.setFilterType(filterType);
			personFilterLayoutID = saveLayout(personAndFilterLayoutBean);
			add = true;
		} else {
			personFilterLayoutID = personAndFilterLayoutBean.getObjectID();
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(getPersonWithFilterLayoutString(personFilterLayoutID, personID, filterID, filterType, add));
		}
		return personFilterLayoutID;
	}
	
	/**
	 * Debug string for person with filter
	 * @param personFilterLayoutID
	 * @param filterID
	 * @param filterType
	 * @param viewID
	 * @param add
	 * @return
	 */
	private static String getPersonWithFilterLayoutString(Integer personFilterLayoutID, Integer personID, Integer filterID, Integer filterType, boolean add) {
		String addString = null;
		if (add) {
			addString = "added: ";
		} else {
			addString = "loaded: ";
		}
		return "Person with filter (" + personID + ", "  + filterType + ", " + filterID + ") layout " + addString + personFilterLayoutID;
	}
	
	/**
	 * Loads the filter specific layoutID. (saved filter has a view specified)
	 * @param filterID
	 * @param filterType
	 * @param viewID
	 * @return
	 */
	private static Integer loadOrCreateFilterWithViewLayout(Integer filterID, Integer filterType) {
		TNavigatorLayoutBean filterWithViewLayoutBean = loadByFilterWithView(filterID, filterType);
		Integer filterWithViewLayoutID = null;
		boolean add = false;
		if (filterWithViewLayoutBean==null) {
			//no layout for person, create a new one
			filterWithViewLayoutBean = new TNavigatorLayoutBean();
			filterWithViewLayoutBean.setFilterID(filterID);
			filterWithViewLayoutBean.setFilterType(filterType);
			filterWithViewLayoutID = saveLayout(filterWithViewLayoutBean);
			add = true;
		} else {
			filterWithViewLayoutID = filterWithViewLayoutBean.getObjectID();
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(getFilterWithViewLayoutString(filterWithViewLayoutID, filterID, filterType, add));
		}
		return filterWithViewLayoutID;
	}
	
	/**
	 * Debug string for filter with view
	 * @param filterWithViewLayoutID
	 * @param filterID
	 * @param filterType
	 * @param viewID
	 * @param add
	 * @return
	 */
	private static String getFilterWithViewLayoutString(Integer filterWithViewLayoutID, Integer filterID, Integer filterType, /*String viewID,*/ boolean add) {
		String addString = null;
		if (add) {
			addString = "added: ";
		} else {
			addString = "loaded: ";
		}
		return "Filter with view (" + filterType + ", " + filterID + /*", " + viewID +*/ ") layout " + addString + filterWithViewLayoutID;
	}
	
	/**
	 * Verify again before save that: the private repository is extra-verified
	 * @param repository
	 * @param objectID
	 * @param projectID
	 * @param personBean
	 * @return
	 */
	public static boolean isModifiable(TQueryRepositoryBean filterBean, TPersonBean personBean) {
		Integer repository = filterBean.getRepositoryType();
		Integer projectID = filterBean.getProject();
		switch (repository.intValue()) {
		case REPOSITORY_TYPE.PUBLIC:
			return personBean.isSys();
		case REPOSITORY_TYPE.PROJECT:
			boolean modifiable = personBean.isSys();
			if (!modifiable) {
				if (projectID!=null) {
					modifiable = PersonBL.isProjectAdmin(personBean.getObjectID(), projectID);
					if (!modifiable) {
						TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
						while (projectBean!=null) {
							Integer parentProjectID = projectBean.getParent();
							if (parentProjectID!=null) {
								modifiable = PersonBL.isProjectAdmin(personBean.getObjectID(), parentProjectID);
								if (modifiable) {
									break;
								}
								projectBean = LookupContainer.getProjectBean(parentProjectID);
							} else {
								projectBean = null;
							}
						}
					}
				}
			}
			return modifiable;
		case REPOSITORY_TYPE.PRIVATE:
			return personBean.getObjectID().equals(filterBean.getPerson());
		}
		return false;
	}
	/**
	 * Gets the layoutID for a person and filter
	 * @param personID
	 * @param filterType
	 * @param filterID
	 * @param forcePersonLayout
	 * @return
	 */
	public static Integer getLayoutID(TPersonBean personBean, Integer filterType, Integer filterID, boolean forcePersonLayout) {
		Integer personID = personBean.getObjectID();
		String viewID = getSavedItemFilterView(filterID, filterType);
		if (viewID==null || "".equals(viewID)) {
			boolean enableQueryLayout = personBean.isEnableQueryLayout();
			if (enableQueryLayout) {
				//"person filter" layout
				return loadOrCreatePersonAndFilterLayoutID(personID, filterID, filterType);
			} else {
				//person layout
				return loadOrCreatePersonLayoutID(personID);
			}
		} else {
			//for the possibility to change the "filter with view" layout: first modify as "filter with view" + person specific
			//which later can be explicitly copied to be "filter with view" specific
			//even if enableQueryLayout=false still look for "filter with view" + person specific layout
			//because the temporary changes are "filter with view" + person specific 
			//not only person specific (nothing to do with the "person only" specific layout)
			TNavigatorLayoutBean personAndFilterLayoutBean = loadByPersonAndFilter(personID, filterID, filterType);
			if (forcePersonLayout) {
				//person specific layout changes on "filter with view" layout: temporary
				//(till the next complete page reload) or to be saved later for "filter with view"
				if (personAndFilterLayoutBean==null) {
					Integer filterWithViewLayoutID = loadOrCreateFilterWithViewLayout(filterID, filterType);
					loadOrCloneFromDefaultNaviagatorLayout(filterWithViewLayoutID);
					Integer personFilterLayoutID = loadOrCreatePersonAndFilterLayoutID(personID, filterID, filterType);
					LOGGER.debug("Clone person specific layout " + personFilterLayoutID + " from the filter with view based layout " + filterWithViewLayoutID +  " for temporary use");
					overwriteLayout(filterWithViewLayoutID, personFilterLayoutID);
					return personFilterLayoutID;
				} else {
					//person has modified his own view specific layout but not saved back to view specific layout
					LOGGER.debug("Get person specific layout " + personAndFilterLayoutBean.getObjectID() + " for temporary use");
					return personAndFilterLayoutBean.getObjectID();
				}
			} else {
				//force filter specific layout independently of person (complete page reload)
				if (personAndFilterLayoutBean!=null) {
					Integer personLayoutID = personAndFilterLayoutBean.getObjectID();
					LOGGER.debug("Delete person specific layout " + personLayoutID);
					delete(personLayoutID);
				}
				//filter with view layout
				return loadOrCreateFilterWithViewLayout(filterID, filterType);
			}
		}
	}
	
	/**
	 * Restore the person and or filter specific layout to default layout 
	 * @param personID
	 * @param filterType
	 * @param filterID
	 */
	public static synchronized void restoreToDefault(TPersonBean personBean, Integer filterType, Integer filterID) {
		LOGGER.debug("Restore to default layout for person " + personBean.getLabel() + " filterType " + filterType + " filterID "+filterID);
		Integer destinationLayoutID = NavigatorLayoutBL.getLayoutID(personBean, filterType, filterID, false);
		LayoutColumnsBL.deleteByLayout(destinationLayoutID);
		LayoutGroupsBL.deleteByLayout(destinationLayoutID);
		loadOrCloneFromDefaultNaviagatorLayout(destinationLayoutID);
	}
	
	/**
	 * Saves the layout assigned to person or person and filter as filter layout
	 * @param personID
	 * @param locale
	 * @param filterType
	 * @param filterID
	 */
	public static synchronized void saveAsFilterLayout(TPersonBean personBean, Locale locale, Integer filterType, Integer filterID) {
		Integer destinationLayoutID = loadOrCreateFilterWithViewLayout(filterID, filterType);
		Integer sourceLayoutID = getLayoutID(personBean, filterType, filterID, true);
		LOGGER.debug("Copy filter layout from " + personBean.getLabel()  + " for filterType " + filterType + " filterID "
				+ filterID + " from personal layout " + sourceLayoutID + " to filter layout " + destinationLayoutID);
		overwriteLayout(sourceLayoutID, destinationLayoutID);
		LOGGER.debug("Delete person layout " + sourceLayoutID + " for person " + personBean.getLabel());
		delete(sourceLayoutID);
	}
	
	/**
	 * Saves the layout assigned to person or person and filter as filter layout
	 * @param personID
	 * @param locale
	 * @param filterType
	 * @param filterID
	 */
	public static void saveAsStandardLayout(TPersonBean personBean, Locale locale, Integer filterType, Integer filterID) {
		LOGGER.debug("Save by " + personBean.getLabel()  + " as default layout from filterType " + filterType+ " filterID " + filterID);
		Integer destinationLayoutID = loadOrCreateDefaultLayoutID();
		Integer sourceLayoutID = getLayoutID(personBean, filterType, filterID, true);
		overwriteLayout(sourceLayoutID, destinationLayoutID);
	}
	
	/**
	 * Gets the view for a saved item filter
	 * @param filterID
	 * @param filterType
	 * @return
	 */
	public static String getSavedItemFilterView(Integer filterID, Integer filterType) {
		if (filterType.intValue()==QUERY_TYPE.SAVED) {
			TQueryRepositoryBean filterBean = FilterBL.loadByPrimaryKey(filterID);
			if (filterBean!=null) {
				return filterBean.getViewID();
			}
		}
		return null;
	}
	public static TQueryRepositoryBean getSavedQuery(Integer filterID, Integer filterType) {
		if (filterType.intValue()==QUERY_TYPE.SAVED) {
			TQueryRepositoryBean filterBean = FilterBL.loadByPrimaryKey(filterID);
			return filterBean;
		}
		return null;
	}
	/**
	 * Saves the layout assigned to person or person and filter as default layout
	 * @param sourceLayoutID
	 * @param destinationLayoutID
	 */
	private static void overwriteLayout(Integer sourceLayoutID, Integer destinationLayoutID) {
		if (sourceLayoutID.equals(destinationLayoutID)) {
			//should never happen
			return;
		}
		LayoutColumnsBL.deleteByLayout(destinationLayoutID);
		LayoutGroupsBL.deleteByLayout(destinationLayoutID);
		CardViewBL.deleteByLayout(destinationLayoutID);
		CardViewBL.overwriteLayout(sourceLayoutID,destinationLayoutID);
		PersistedLayout persistedLayout = loadOrCloneFromDefaultNaviagatorLayout(sourceLayoutID);
		List<TNavigatorColumnBean> columnFieldBeans = persistedLayout.getColumnFieldBeans();
		if (columnFieldBeans==null || columnFieldBeans.isEmpty()) {
			LOGGER.info("Cannot save a layout without fields as default");
		} else {
			LayoutGroupsBL.cloneNavigatorGroupingSorting(persistedLayout.getGroupingSortingFieldBeans(), destinationLayoutID);
			LayoutColumnsBL.cloneNavigatorColumnBeanList(persistedLayout.getColumnFieldBeans(), destinationLayoutID);
		}
	}
	

	/**
	 * Load the complete layout for person or/and filter
	 * @param personID
	 * @param locale
	 * @param filterType
	 * @param filterID
	 * @return
	 */
	public static LayoutTO loadLayout(TPersonBean personBean,
			Locale locale, Integer filterType, Integer filterID, boolean forcePersonLayout) {
		LOGGER.debug("Load layout for person " + personBean.getLabel() + " filterType " + filterType + " filterID " + filterID);
		PersistedLayout persistedLayout = null;
		Integer layoutID = getLayoutID(personBean, filterType, filterID, forcePersonLayout);
		if (layoutID!=null) {
			persistedLayout = loadOrCloneFromDefaultNaviagatorLayout(layoutID);
		}
		LayoutTO layoutTO = new LayoutTO();
		if (persistedLayout!=null) {
			List<TNavigatorColumnBean> navigatorColumnBeans = persistedLayout.getColumnFieldBeans();
			List<Integer> columnFieldIDs = new ArrayList<Integer>();
			for (TNavigatorColumnBean navigatorColumnBean : navigatorColumnBeans) {
				columnFieldIDs.add(navigatorColumnBean.getField());
			}
			List<ColumnFieldTO> columnFields = loadColumnFieldsByFieldIDs(navigatorColumnBeans, locale);
			layoutTO.setShortFields(getColumnFields(columnFields, false));
			layoutTO.setLongFields(getColumnFields(columnFields, true));
			layoutTO.setBulkEdit(LayoutColumnsBL.isInLayout(columnFieldIDs, TReportLayoutBean.PSEUDO_COLUMNS.CHECKBOX_FIELD_ID));
			layoutTO.setIndexNumber(LayoutColumnsBL.isInLayout(columnFieldIDs, TReportLayoutBean.PSEUDO_COLUMNS.INDEX_NUMBER));
			List<TNavigatorGroupingSortingBean> navigatorGroupingSortingBeans = persistedLayout.getGroupingSortingFieldBeans();
			List<GroupFieldTO> groupFields = new LinkedList<GroupFieldTO>();
			SortFieldTO sortField = LayoutGroupsBL.getSortLoadGroupFields(navigatorGroupingSortingBeans, locale, true, groupFields);
			layoutTO.setGroupFields(groupFields);
			layoutTO.setSortField(sortField);
		}
		return layoutTO;
	}
	
	public static LayoutTO loadExclusiveFields(Set<Integer> exclusiveShortFields,Locale locale){
		LayoutTO layoutTO = new LayoutTO();
		List<ColumnFieldTO> shortFields = new ArrayList<ColumnFieldTO>();
		layoutTO.setShortFields(shortFields);
		Map<Integer, TFieldConfigBean> defaultConfigsMap = FieldRuntimeBL.getLocalizedDefaultFieldConfigsMap(locale);
		for (Integer fieldID : exclusiveShortFields) {
			String label = null;
			if(fieldID<0){
				//pseudo-field
				String key=TReportLayoutBean.getPseudoColumnLabel(fieldID);
				label= LocalizeUtil.getLocalizedTextFromApplicationResources(key,locale);

			}else {
				TFieldConfigBean fieldConfigBean = defaultConfigsMap.get(fieldID);
				if (fieldConfigBean != null) {
					label = fieldConfigBean.getLabel();
				}
			}
			ColumnFieldTO columnFieldTO = new ColumnFieldTO(fieldID, label);
			shortFields.add(columnFieldTO);
		}
		layoutTO.setBulkEdit(false);
		layoutTO.setIndexNumber(false);
		return layoutTO;
	}
	
	/**
	 * Gets the short or the long fields
	 * @param navigatorColumns
	 * @param isLong
	 * @return
	 */
	private static List<ColumnFieldTO> getColumnFields(
			List<ColumnFieldTO> navigatorColumns, boolean isLong) {
		List<ColumnFieldTO> filteredColumns = new ArrayList<ColumnFieldTO>();
		if (navigatorColumns==null) {
			return filteredColumns;
		}
		for (ColumnFieldTO navigatorFieldTO : navigatorColumns) {
			Integer fieldID = navigatorFieldTO.getFieldID();
			if (fieldID.intValue()!=TReportLayoutBean.PSEUDO_COLUMNS.CHECKBOX_FIELD_ID &&
					fieldID.intValue()!=TReportLayoutBean.PSEUDO_COLUMNS.INDEX_NUMBER &&
						((isLong && navigatorFieldTO.isRenderAsLong()) ||
								(!isLong && !navigatorFieldTO.isRenderAsLong()))) {
					filteredColumns.add(navigatorFieldTO);
				}
		}
		return filteredColumns;
	}
	
	
	/**
	 * Load the layout details. Copies fist the default if not exists
	 * @param layoutID
	 * @return
	 */
	public static PersistedLayout loadOrCloneFromDefaultNaviagatorLayout(Integer layoutID) {
		List<TNavigatorColumnBean> columnFieldBeans = LayoutColumnsBL.loadByLayout(layoutID);
		List<TNavigatorGroupingSortingBean> groupFieldBeans = LayoutGroupsBL.loadByLayout(layoutID);
		if (columnFieldBeans==null || columnFieldBeans.isEmpty()) {
			Integer defaultLayoutID = loadOrCreateDefaultLayoutID();
			List<TNavigatorColumnBean> defaultLayoutColumnBeans = LayoutColumnsBL.loadByLayout(defaultLayoutID);
			if (defaultLayoutColumnBeans==null || defaultLayoutColumnBeans.isEmpty()) {
				defaultLayoutColumnBeans = LayoutColumnsBL.createDefaultFieldsLayout(defaultLayoutID);
			}
			List<TNavigatorGroupingSortingBean> defaultLayoutGroupingSortingBeans = LayoutGroupsBL.loadByLayout(defaultLayoutID);
			groupFieldBeans = LayoutGroupsBL.cloneNavigatorGroupingSorting(defaultLayoutGroupingSortingBeans, layoutID);
			columnFieldBeans = LayoutColumnsBL.cloneNavigatorColumnBeanList(defaultLayoutColumnBeans, layoutID);
		}
		PersistedLayout persistedLayout = new PersistedLayout();
		persistedLayout.setColumnFieldBeans(columnFieldBeans);
		persistedLayout.setGroupingSortingFieldBeans(groupFieldBeans);
		return persistedLayout;
	}
	
	
	
	
	
	/**
	 * Whether the field is valid, i.e. still exists
	 * @param fieldID
	 * @return
	 */
	public static boolean fieldIsValid(Integer fieldID) {
		if (fieldID!=null) {
			if (fieldID.intValue()>0) {
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldTypeRT==null) {
					//field does not exist any more
					return false;
				}
			} else {
				if (fieldID<ColumnFieldsBL.CUSTOM_LIST_ICON_FIELD_START) {
					IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(ColumnFieldsBL.getCustomListFieldFromIconField(fieldID));
					if (fieldTypeRT==null) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Loads the selected column fields
	 * @param navigatorColumnBeans
	 * @param locale
	 * @return
	 */
	private static List<ColumnFieldTO> loadColumnFieldsByFieldIDs(List<TNavigatorColumnBean> navigatorColumnBeans, Locale locale) {
		Set<Integer> realFieldIDs = new HashSet<Integer>();
		Set<Integer> customIconFieldIDs = new HashSet<Integer>();
		Set<Integer> pseudoFieldIDs = new HashSet<Integer>();
		Map<Integer, TNavigatorColumnBean> navigatorColumnBeansByFieldMap = new HashMap<Integer, TNavigatorColumnBean>();
		//boolean indexNumber = false;
		List<ColumnFieldTO> columnFieldsList = new ArrayList<ColumnFieldTO>();
		if (navigatorColumnBeans!=null) {
			for (TNavigatorColumnBean navigatorColumnBean : navigatorColumnBeans) {
				Integer fieldID = navigatorColumnBean.getField();
				navigatorColumnBeansByFieldMap.put(fieldID, navigatorColumnBean);
				if (fieldID.intValue()>0) {
					realFieldIDs.add(fieldID);
				} else {
					if (fieldID<ColumnFieldsBL.CUSTOM_LIST_ICON_FIELD_START) {
						realFieldIDs.add(ColumnFieldsBL.getCustomListFieldFromIconField(fieldID));
						customIconFieldIDs.add(fieldID);
					} else {
						if (fieldID.intValue()!=TReportLayoutBean.PSEUDO_COLUMNS.CHECKBOX_FIELD_ID && 
								fieldID.intValue()!=TReportLayoutBean.PSEUDO_COLUMNS.INDEX_NUMBER) {
								pseudoFieldIDs.add(fieldID);
						}
						
					}
				}
			}
			List<ColumnFieldTO> realFieldColumns = ColumnFieldsBL.loadRealFields(realFieldIDs, navigatorColumnBeansByFieldMap, locale);
			List<ColumnFieldTO> customFieldIconColumns = ColumnFieldsBL.getSelectedCustomListIconColumns(customIconFieldIDs, navigatorColumnBeansByFieldMap, locale);
			List<ColumnFieldTO> pseudoFieldColumns = ColumnFieldsBL.loadSelectedPseudoFields(pseudoFieldIDs, navigatorColumnBeansByFieldMap, locale);
			Map<Integer, ColumnFieldTO> realColumnFieldsMap = new HashMap<Integer, ColumnFieldTO>();
			if (realColumnFieldsMap!=null) {
				for (ColumnFieldTO columnFieldTO : realFieldColumns) {
					realColumnFieldsMap.put(columnFieldTO.getFieldID(), columnFieldTO);
				}
			}
			Map<Integer, ColumnFieldTO> customFieldIconColumnsMap = new HashMap<Integer, ColumnFieldTO>();
			if (customFieldIconColumns!=null) {
				for (ColumnFieldTO columnFieldTO : customFieldIconColumns) {
					customFieldIconColumnsMap.put(columnFieldTO.getFieldID(), columnFieldTO);
				}
			}
			Map<Integer, ColumnFieldTO> pseudoColumnFieldsMap = new HashMap<Integer, ColumnFieldTO>();
			if (pseudoFieldColumns!=null) {
				for (ColumnFieldTO columnFieldTO : pseudoFieldColumns) {
					pseudoColumnFieldsMap.put(columnFieldTO.getFieldID(), columnFieldTO);
				}
			}
			for (TNavigatorColumnBean navigatorColumnBean : navigatorColumnBeans) {
				Integer fieldID = navigatorColumnBean.getField();
				if (realColumnFieldsMap.containsKey(fieldID)) {
					columnFieldsList.add(realColumnFieldsMap.get(fieldID));
				} else {
					if (pseudoColumnFieldsMap.containsKey(fieldID)) {
						columnFieldsList.add(pseudoColumnFieldsMap.get(fieldID));
					} else {
						if (customFieldIconColumnsMap.containsKey(fieldID)) {
							columnFieldsList.add(customFieldIconColumnsMap.get(fieldID));
						} 
					}
				}
			}
		}
		return columnFieldsList;
	}
	
	
	
	static class PersistedLayout {
		private List<TNavigatorColumnBean> columnFieldBeans = null;
		private List<TNavigatorGroupingSortingBean> groupingSortingFieldBeans = null;
		public List<TNavigatorColumnBean> getColumnFieldBeans() {
			return columnFieldBeans;
		}
		public void setColumnFieldBeans(List<TNavigatorColumnBean> columnFieldBeans) {
			this.columnFieldBeans = columnFieldBeans;
		}
		public List<TNavigatorGroupingSortingBean> getGroupingSortingFieldBeans() {
			return groupingSortingFieldBeans;
		}
		public void setGroupingSortingFieldBeans(
				List<TNavigatorGroupingSortingBean> groupingSortingFieldBeans) {
			this.groupingSortingFieldBeans = groupingSortingFieldBeans;
		}
	}
	
	
	
	
}


