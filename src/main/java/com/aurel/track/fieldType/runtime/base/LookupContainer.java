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


package com.aurel.track.fieldType.runtime.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.objectStatus.SystemStatusBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TPriorityBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TSeverityBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ILookup;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;

/**
 * Used by rendering the lists (IFieldTypeRT.processLoadDataSource()), 
 * and as lookup maps for select keys IFieldTypeRT.getShowValue(), 
 * IFieldTypeRT.getShowISOValue(), IFieldTypeRT.getSortOrderValue()
 * @author Tamas
 *
 */
public class LookupContainer {
	private static final Logger LOGGER = LogManager.getLogger(LookupContainer.class);
	
	//public static Integer CUSTOM_OPTIONS_KEY = Integer.valueOf(-1);
	
	/**
	 * The "field" for all custom options
	 * All custom options are loaded in a map using this value as fieldID (listID)
	 */
	//public static String CUSTOM_OPTIONS = MergeUtil.mergeKey(CUSTOM_OPTIONS_KEY, null);
	
	/**
	 * The custom lists with their custom options
	 */
	//public static Map<Integer, List<Integer>> customListToOptionsMap = new HashMap<Integer, List<Integer>>();
	
	//TODO add the localized lists also in the not localized maps (used in getSortOrderValue()) 
	
	/**
	 * Map containing the not localized datasources as maps
	 * Used by retrieving beans by key
	 * for ex. getting the show value or the sort order value 
	 * 	-	key: fieldID
	 *	-	value: map: key: objectID
	 * 				value: the bean
	 */
	protected static Map<Integer, Map<Integer, ILabelBean>> notLocalizedDataSourceMaps =
			new HashMap<Integer, Map<Integer,ILabelBean>>();
	
	/**
	 * Map containing the datasources as maps
	 * Used by retrieving beans by key 
	 * for ex. getting the show value or the sort order value 
	 * 	-	key: fieldID
	 *	-	value: map with key: locale
	 *						value map:	key: objectID
	 * 									value: the bean
	 */
	protected static Map<Integer, Map<Locale, Map<Integer, ILabelBean>>> localizedDataSourceMaps =
			new HashMap<Integer, Map<Locale, Map<Integer,ILabelBean>>>();

	/**
	 * Map containing the system state beans for project, release and account
	 */
	protected static Map<Integer, List<TSystemStateBean>> systemStateMaps = new HashMap<Integer, List<TSystemStateBean>>();
	
	/**
	 * Resets all lists
	 * @return
	 */
	public static void resetAll() {
		notLocalizedDataSourceMaps.clear();
		localizedDataSourceMaps.clear();
		systemStateMaps.clear();
		LOGGER.debug("Resetting all lookups");
	}

	
	/**
	 * Gets the lookups for a list: both localized and not localized lists should be added in the notLocalizedDataSourceMaps
	 * because use the for getting the sortOrder value for lists we do not need the localizations 
	 * @param fieldID
	 * @return
	 */
	private static Map<Integer, ILabelBean> getNotLocalizedLookupMap(Integer fieldID) {
		Map<Integer, ILabelBean> listOptionsMap = notLocalizedDataSourceMaps.get(fieldID);
		if (listOptionsMap==null) {
			//Integer[] parts = MergeUtil.getParts(listKey);
			//Integer fieldID = parts[0];
			LOGGER.debug("Loading the not localized map for " + fieldID);
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			if (fieldTypeRT!=null && fieldTypeRT.isLookup())	{
				//for ex. itemPicker: the SystemFields.ISSUENO (see getSystemOptionType() for ItemPickerRT) 
				//getSortOrderValue() in CustomPickerRT() looks for LookupContainer.getNotLocalizedLookupMap(getDropDownMapFieldKey(fieldID)) 
				//but SystemIssueNoRT is not ILookup
				ILookup lookup = (ILookup)fieldTypeRT;
				if (lookup!=null) {
					List<ILabelBean> listOptionsList = lookup.getDataSource(fieldID);
					listOptionsMap = GeneralUtils.createMapFromList(listOptionsList);
					notLocalizedDataSourceMaps.put(fieldID, listOptionsMap);
				}
			}
		}
		return listOptionsMap;
	}
	
	/**
	 * Gets the projectsMap
	 * @return
	 */
	/*public static Map<Integer, TProjectBean> getProjectsMap() { xx
		return (Map)getNotLocalizedLookupMap(SystemFields.INTEGER_PROJECT);
	}*/
	
	/**
	 * Gets the projectsMap
	 * @return
	 */
	/*public static Map<Integer, TReleaseBean> getReleasesMap() {
		return (Map)getNotLocalizedLookupMap(SystemFields.INTEGER_RELEASE);
	}*/
	
	/**
	 * Gets the projectsMap
	 * @return
	 */
	public static Map<Integer, TPersonBean> getPersonsMap() {
		return (Map)getNotLocalizedLookupMap(SystemFields.INTEGER_PERSON);
	}
	
	
	/*private static void removeLabelBean(Integer fieldID, Integer objectID) {
		ILabelBean labelBean = null;
		if (objectID!=null) {
			Map<Integer, ILabelBean> notLocalizedLookupMap = getNotLocalizedLookupMap(fieldID);
			if (notLocalizedLookupMap!=null) {
				notLocalizedLookupMap.remove(fieldID);
			}	
		}
	}*/
	
	/**
	 * Gets the not localized label bean
	 * @param fieldID
	 * @param objectID
	 * @return
	 */
	public static ILabelBean getNotLocalizedLabelBean(Integer fieldID, Integer objectID) {
		ILabelBean labelBean = null;
		if (objectID!=null) {
			Map<Integer, ILabelBean> notLocalizedLookupMap = getNotLocalizedLookupMap(fieldID);
			if (notLocalizedLookupMap!=null) {
				labelBean = notLocalizedLookupMap.get(objectID);
				if (labelBean==null) {
					IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
					if (fieldTypeRT!=null && fieldTypeRT.isLookup()) {
						ILookup lookup = (ILookup)fieldTypeRT;
						labelBean = lookup.getLabelBean(objectID, null);
						if (labelBean!=null) {
							LOGGER.debug("Adding the not localized label bean for field " + fieldID  + " and objectID " + objectID + " into lookup map");
							notLocalizedLookupMap.put(objectID, labelBean);
						}
					}
				}
			}
		}
		return labelBean;
	}
	
	/**
	 * Gets the label for a not localized label bean
	 * @param fieldID
	 * @param objectID
	 * @return
	 */
	public static String getNotLocalizedLabelBeanLabel(Integer fieldID, Integer objectID) {
		ILabelBean labelBean = getNotLocalizedLabelBean(fieldID, objectID);
		if (labelBean!=null) {
			return labelBean.getLabel();
		}
		return null;
	}
	
	/**
	 * Gets the label for a not localized label bean list
	 * @param fieldID
	 * @param objectIDs
	 * @return
	 */
	public static String getNotLocalizedLabelBeanListLabels(Integer fieldID, Set<Integer> objectIDs) {
		StringBuilder stringBuilder = new StringBuilder();
		if (objectIDs!=null) {
			for (Iterator<Integer> iterator = objectIDs.iterator(); iterator.hasNext();) {
				Integer objectID = iterator.next();
				if (objectID!=null) {
					String label = getNotLocalizedLabelBeanLabel(fieldID, objectID);
					if (label!=null) {
						stringBuilder.append(label);
					}
					if (iterator.hasNext()) {
						stringBuilder.append(", ");
					}
				}
			}
		}
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the projectBean by id
	 * @param objectID
	 * @return
	 */
	public static TProjectBean getProjectBean(Integer objectID) {
		return (TProjectBean)getNotLocalizedLabelBean(SystemFields.INTEGER_PROJECT, objectID);
	}
	
	/**
	 * Gets the releaseBean by id
	 * @param objectID
	 * @return
	 */
	public static TReleaseBean getReleaseBean(Integer objectID) {
		return (TReleaseBean)getNotLocalizedLabelBean(SystemFields.INTEGER_RELEASE, objectID);
	}
	
	/**
	 * Gets the itemTypeBean by id
	 * @param objectID
	 * @return
	 */
	public static TListTypeBean getItemTypeBean(Integer objectID) {
		return (TListTypeBean)getNotLocalizedLabelBean(SystemFields.INTEGER_ISSUETYPE, objectID);
	}
	
	/**
	 * Gets the statusBean by id
	 * @param objectID
	 * @return
	 */
	public static TStateBean getStatusBean(Integer objectID) {
		return (TStateBean)getNotLocalizedLabelBean(SystemFields.INTEGER_STATE, objectID);
	}
	
	/**
	 * Gets the statusBean by id
	 * @param objectID
	 * @return
	 */
	public static TPriorityBean getPriorityBean(Integer objectID) {
		return (TPriorityBean)getNotLocalizedLabelBean(SystemFields.INTEGER_PRIORITY, objectID);
	}
	
	/**
	 * Gets the statusBean by id
	 * @param objectID
	 * @return
	 */
	public static TSeverityBean getSeverityBean(Integer objectID) {
		return (TSeverityBean)getNotLocalizedLabelBean(SystemFields.INTEGER_SEVERITY, objectID);
	}
	
	/**
	 * Gets the personBean by id
	 * @param objectID
	 * @return
	 */
	public static TPersonBean getPersonBean(Integer objectID) {
		return (TPersonBean)getNotLocalizedLabelBean(SystemFields.INTEGER_PERSON, objectID);
	}
	
	/**
	 * Gets the comma separated labels for an array of options
	 * @param fieldID
	 * @param optionIDs
	 * @return
	 */
	public static String getNotLocalizedLookupCommaSepatatedString(Integer fieldID, Integer[] optionIDs, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder();
		Map<Integer, ILabelBean> lookupMap = getNotLocalizedLookupMap(fieldID);
		if (optionIDs!=null && optionIDs.length>0) {
			for (int i = 0; i < optionIDs.length; i++) {
				Integer objectID = optionIDs[i];
				if (objectID!=null) {
					String label = null;
					if (MatcherContext.PARAMETER.equals(objectID)) {
						label= MatcherContext.getLocalizedParameter(locale);
					} else {
						if (MatcherContext.LOGGED_USER_SYMBOLIC.equals(objectID)) {
							label = MatcherContext.getLocalizedLoggedInUser(locale);
						} else {
							ILabelBean labelBean = null;
							if (lookupMap!=null) {
								labelBean = lookupMap.get(objectID);
							}
							if (labelBean==null) {
								labelBean = getNotLocalizedLabelBean(fieldID, objectID);
							}
							if (labelBean!=null) {
								label = labelBean.getLabel();
							}
						}
					}
					if (label!=null) {
						if (stringBuilder.length()>0) {
							stringBuilder.append(",");
						}
						stringBuilder.append(label);
					}
				}
			}
		}
		return stringBuilder.toString();
	}
	
	/**
	 * Resets a lookup map for a field
	 * @param fieldID
	 */
	public static void resetLookupMap(Integer fieldID) {
		resetNotLocalizedLookupMap(fieldID);
		switch (fieldID.intValue()) {
		case SystemFields.STATE:
		case SystemFields.ISSUETYPE:
		case SystemFields.PRIORITY:
		case SystemFields.SEVERITY:
			localizedDataSourceMaps.remove(fieldID);
			//TODO notify the other clusters about reset
			LOGGER.debug("Resetting the localized maps for field " + fieldID);
		}
	}
	
	/**
	 * Resets the not localized list
	 * @param fieldID
	 * @return
	 */
	private static void resetNotLocalizedLookupMap(Integer fieldID) {
		notLocalizedDataSourceMaps.remove(fieldID);
		//TODO notify the other clusters about reset 
		LOGGER.debug("Resetting the not localized map for " + fieldID);
	}
	
	/**
	 * Resets the projects list
	 */
	public static void resetProjectMap() {
		resetNotLocalizedLookupMap(SystemFields.INTEGER_PROJECT);
	}
	
	/**
	 * Resets the projects list
	 */
	public static void resetReleaseMap() {
		resetNotLocalizedLookupMap(SystemFields.INTEGER_RELEASE);
	}
	
	/**
	 * Resets the projects list
	 */
	public static void resetPersonMap() {
		resetNotLocalizedLookupMap(SystemFields.INTEGER_PERSON);
	}
	
	/**
	 * Resets the not localized list
	 * @param fieldID
	 * @return
	 */
	public static void reloadNotLocalizedLabelBean(Integer fieldID, Integer objectID) {
		if (objectID!=null) {
			LOGGER.debug("Reloading the not localized label bean for field " + fieldID  + " and objectID " + objectID);
			Map<Integer, ILabelBean> notLocalizedLookupMap = getNotLocalizedLookupMap(fieldID);
			ILabelBean labelBean = notLocalizedLookupMap.get(objectID);
			if (labelBean!=null) {
				notLocalizedLookupMap.remove(objectID);
			}
			getNotLocalizedLabelBean(fieldID, objectID);
		}
	}
	
	/**
	 * Reloads a project bean
	 * @param objectID
	 */
	public static void reloadProject(Integer objectID) {
		reloadNotLocalizedLabelBean(SystemFields.INTEGER_PROJECT, objectID);
	}
	
	/**
	 * Reloads a release bean
	 * @param objectID
	 */
	public static void reloadRelease(Integer objectID) {
		reloadNotLocalizedLabelBean(SystemFields.INTEGER_RELEASE, objectID);
	}
	
	/**
	 * Reloads a person bean
	 * @param objectID
	 */
	public static void reloadPerson(Integer objectID) {
		reloadNotLocalizedLabelBean(SystemFields.INTEGER_PERSON, objectID);
	}
	
	/**
	 * Gets the localized lookups for a list
	 * @param locale
	 * @param fieldID
	 * @return
	 */
	private static Map<Integer, ILabelBean> getLocalizedLookupMap(Locale locale, Integer fieldID) {
		Map<Locale, Map<Integer, ILabelBean>> dataSourcesForLocale = localizedDataSourceMaps.get(fieldID);
		if (dataSourcesForLocale==null) {
			dataSourcesForLocale = new HashMap<Locale, Map<Integer,ILabelBean>>();
			localizedDataSourceMaps.put(fieldID, dataSourcesForLocale);
		}
		
		IFieldTypeRT fieldTypeRT = null;
		//if (fieldID>0) {
			fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
		/*} else {
			if (LookupContainer.CUSTOM_OPTIONS_KEY.equals(fieldID)) {
				fieldTypeRT = new CustomSelectSimpleRT();
			}
		}*/
		if (fieldTypeRT!=null) {
			ILookup lookup = (ILookup)fieldTypeRT;
			Map<Integer, ILabelBean> listOptions = dataSourcesForLocale.get(locale);
			if (listOptions==null) {
				List<ILabelBean> listOptionsList = LocalizeUtil.localizeDropDownList(lookup.getDataSource(fieldID), locale);
				LOGGER.debug("Loading the localized map for " + fieldID + " and locale " + locale);
				listOptions = GeneralUtils.createMapFromList(listOptionsList);
				dataSourcesForLocale.put(locale, listOptions);
			}
			return listOptions;
		}
		return new HashMap<Integer, ILabelBean>();
	}
	
	/**
	 * Gets the localized label bean
	 * @param fieldID
	 * @param objectID
	 * @param locale
	 * @return
	 */
	public static ILabelBean getLocalizedLabelBean(Integer fieldID, Integer objectID, Locale locale) {
		Map<Integer, ILabelBean> localizedLookupMap = getLocalizedLookupMap(locale, fieldID);
		ILabelBean labelBean = localizedLookupMap.get(objectID);
		if (labelBean==null) {
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			if (fieldTypeRT!=null && fieldTypeRT.isLookup()) {
				ILookup lookup = (ILookup)fieldTypeRT;
				labelBean = lookup.getLabelBean(objectID, locale);
				if (labelBean!=null) {
					LOGGER.debug("Adding the localized label bean for field " + fieldID  + " and objectID " + objectID + " into lookup map for locale " + locale);
					localizedLookupMap.put(objectID, labelBean);
				}
			}
		}
		return labelBean;
	}
	
	/**
	 * Gets the label for a localized label bean
	 * @param fieldID
	 * @param objectID
	 * @param locale
	 * @return
	 */
	public static String getLocalizedLabelBeanLabel(Integer fieldID, Integer objectID, Locale locale) {
		ILabelBean labelBean = getLocalizedLabelBean(fieldID, objectID, locale);
		if (labelBean!=null) {
			return labelBean.getLabel();
		}
		return null;
	}
	
	/**
	 * Gets the label for a localized label bean list
	 * @param fieldID
	 * @param objectIDs
	 * @param locale
	 * @return
	 */
	public static String getLocalizedLabelBeanListLabels(Integer fieldID, Set<Integer> objectIDs, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder();
		if (objectIDs!=null) {
			for (Iterator<Integer> iterator = objectIDs.iterator(); iterator.hasNext();) {
				Integer objectID = iterator.next();
				if (objectID!=null) {
					String label = getLocalizedLabelBeanLabel(fieldID, objectID, locale);
					if (label!=null) {
						stringBuilder.append(label);
					}
					if (iterator.hasNext()) {
						stringBuilder.append(", ");
					}
				}
			}
		}
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the localized status map
	 * @param locale
	 * @return
	 */
	public static Map<Integer, TStateBean> getStatusMap(Locale locale) {
		return (Map)getLocalizedLookupMap(locale, SystemFields.INTEGER_STATE);
	}
	
	/**
	 * Gets the localized item type bean by id
	 * @param objectID
	 * @return
	 */
	public static TListTypeBean getItemTypeBean(Integer objectID, Locale locale) {
		return (TListTypeBean)getLocalizedLabelBean(SystemFields.INTEGER_ISSUETYPE, objectID, locale);
	}
	
	/**
	 * Gets the localized status bean by id
	 * @param objectID
	 * @return
	 */
	public static TStateBean getStatusBean(Integer objectID, Locale locale) {
		return (TStateBean)getLocalizedLabelBean(SystemFields.INTEGER_STATE, objectID, locale);
	}
	
	/**
	 * Gets the localized priority bean by id
	 * @param objectID
	 * @return
	 */
	public static TPriorityBean getPriorityBean(Integer objectID, Locale locale) {
		return (TPriorityBean)getLocalizedLabelBean(SystemFields.INTEGER_PRIORITY, objectID, locale);
	}

	/**
	 * Gets the localized severity bean by id
	 * @param objectID
	 * @return
	 */
	public static TSeverityBean getSeverityBean(Integer objectID, Locale locale) {
		return (TSeverityBean)getLocalizedLabelBean(SystemFields.INTEGER_SEVERITY, objectID, locale);
	}
	
	/**
	 * Gets the localized lookup list
	 * @param locale
	 * @param fieldID
	 * @return
	 */
	public static List<ILabelBean> getLocalizedLookup(Locale locale, Integer fieldID) {
		Map<Integer, ILabelBean> map=getLocalizedLookupMap(locale,fieldID);
		List<ILabelBean> result=new ArrayList<ILabelBean>();
		Iterator<Integer> it=map.keySet().iterator();
		while(it.hasNext()){
			result.add( map.get(it.next()));
		}
		return result;
	}
	
	/**
	 * Gets the comma separated labels for an array of options
	 * @param fieldID
	 * @param optionIDs
	 * @return
	 */
	public static String getLocalizedLookupCommaSepatatedString(Integer fieldID, Integer[] optionIDs, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder();
		Map<Integer, ILabelBean> lookupMap = getLocalizedLookupMap(locale, fieldID);
		if (optionIDs!=null && optionIDs.length>0) {
			for (int i = 0; i < optionIDs.length; i++) {
				Integer objectID = optionIDs[i];
				if (objectID!=null) {
					String label = null;
					if (MatcherContext.PARAMETER.equals(objectID)) {
						label= MatcherContext.getLocalizedParameter(locale);
					} else {
						ILabelBean labelBean = lookupMap.get(objectID);
						if (labelBean==null) {
							labelBean = getLocalizedLabelBean(fieldID, objectID, locale);
						}
						if (labelBean!=null) {
							label = labelBean.getLabel();
						}
					}
					
					if (label!=null) {
						if (stringBuilder.length()>0) {
							stringBuilder.append(",");
						}
						stringBuilder.append(label);
					}
				}
			}
		}
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the lookups for a field
	 * @param fieldID
	 * @return
	 */
	/*public static void resetLocalizedLookupMaps(Integer fieldID) {
		resetNotLocalizedLookupMap(fieldID);
		localizedDataSourceMaps.remove(fieldID);
		//TODO notify the other clusters about reset
		LOGGER.debug("Resetting the localized maps for field " + fieldID);
	}*/
	
	/**
	 * Resets the localized for a field and locale
	 * @param fieldID
	 * @param locale
	 * @return
	 */
	public static void resetLocalizedLookupMap(Integer fieldID, Locale locale) {
		Map<Locale, Map<Integer, ILabelBean>> listLookupMap = localizedDataSourceMaps.get(fieldID);
		if (listLookupMap!=null) {
			listLookupMap.remove(locale);
			//TODO notify the other clusters about reset
			LOGGER.debug("Resetting the localized map for field " + fieldID + " and locale " + locale);
		}
	}
	
	/**
	 * Gets the system options for an entity
	 * @param entityFlag
	 * @return
	 */
	public static List<TSystemStateBean> getSystemStateList(Integer entityFlag) {
		List<TSystemStateBean> systemStateList = systemStateMaps.get(entityFlag);
		if (systemStateList==null) {
			LOGGER.debug("Loading the system state list for " + entityFlag);
			systemStateList = SystemStatusBL.getStatusOptions(entityFlag);
			systemStateMaps.put(entityFlag, systemStateList);
		}
		return systemStateList;
	}
	
	/**
	 * Resets the system states map for an entity
	 * @param entityFlag
	 * @return
	 */
	public static void resetSystemStateMap(Integer entityFlag) {
		systemStateMaps.remove(entityFlag);
		//TODO notify the other clusters about reset 
		LOGGER.debug("Resetting the system states for " + entityFlag);
	}
	
	/**
	 * Resets the not localized list
	 * @param fieldID
	 * @return
	 */
	/*public static void resetLocalizedLabelBean(Integer fieldID, Integer objectID, Locale locale) {
		Map<Locale, Map<Integer, ILabelBean>> listOptionsMap = localizedDataSourceMaps.get(fieldID);
		if (listOptionsMap!=null) {
			Map<Integer, ILabelBean> localeOptionsMap = listOptionsMap.get(locale);
			if (localeOptionsMap!=null) {
				//TODO notify the other clusters about reset 
				LOGGER.debug("Resetting the not localized label bean for field " + fieldID  + " and objectID " + objectID + " and locale " + locale);
				localeOptionsMap.remove(objectID);
			}
		}
	}*/
}
