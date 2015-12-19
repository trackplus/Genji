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

package com.aurel.track.admin.customize.objectStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.localize.LocalizeBL.RESOURCE_TYPE_KEYS;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.beans.TSystemStateBean.ENTITYFLAGS;
import com.aurel.track.beans.TSystemStateBean.STATEFLAGS;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.tree.TreeNodeBaseTO;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.SortOrderUtil;

/**
 * Helper class for system and custom fields
 * @author Tamas
 *
 */
public class ObjectStatusBL {
	private static String LINK_CHAR = "_";
		
	public static interface ICONS_CLS {
		static String PROJECT_STATUS_ICON = "config-project-ticon";
		static String RELEASE_STATUS_ICON = "releaseActive-ticon";
		static String ACCOUNT_STATUS_ICON = "expense-ticon";
	}

	/**
	 * Encode a node
	 * @param objectStatusTokens
	 * @return
	 */
	private static String encodeNode(ObjectStatusTokens objectStatusTokens){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(objectStatusTokens.getListID());
		stringBuffer.append(LINK_CHAR);
		stringBuffer.append(objectStatusTokens.getOptionID());
		return stringBuffer.toString();
	}
	
	/**
	 * Decode a node
	 * @param id
	 * @return
	 */
	private static ObjectStatusTokens decodeNode(String id){
		ObjectStatusTokens objectStatusTokens = new ObjectStatusTokens();
		if (id!=null && !"".equals(id)) {
			String[] tokens = id.split(LINK_CHAR);
			if (tokens!=null && tokens.length>0) {	
				objectStatusTokens.setListID(Integer.valueOf(tokens[0]));
				if (tokens.length>1) {
					if (tokens[1]!=null && !"".equals(tokens[1])) {
						objectStatusTokens.setOptionID(Integer.valueOf(tokens[1]));
					}
				}
			}
		}
		return objectStatusTokens;
	}
	
	/**
	 * Gets the hardcoded system state state nodes
	 * Can add new entries to lists, but can't edit or delete status lists
	 * @param locale
	 * @return
	 */
	static List<TreeNodeBaseTO> getChildren(Locale locale) {
		List<TreeNodeBaseTO> listOptionTreeNodeTOs = new ArrayList<TreeNodeBaseTO>();
		TreeNodeBaseTO projectStatusNode = new TreeNodeBaseTO(String.valueOf(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE),
				LocalizeUtil.getLocalizedTextFromApplicationResources(RESOURCE_TYPE_KEYS.PROJECT_STATUS_KEY, locale),
				ICONS_CLS.PROJECT_STATUS_ICON, true);
		listOptionTreeNodeTOs.add(projectStatusNode);
		TreeNodeBaseTO releaseStatusNode = new TreeNodeBaseTO(String.valueOf(TSystemStateBean.ENTITYFLAGS.RELEASESTATE),
				LocalizeUtil.getLocalizedTextFromApplicationResources(RESOURCE_TYPE_KEYS.RELEASE_STATUS_KEY, locale),
				ICONS_CLS.RELEASE_STATUS_ICON, true);
		listOptionTreeNodeTOs.add(releaseStatusNode);
		if (!ApplicationBean.getInstance().isGenji()) {
			TreeNodeBaseTO accountStatusNode = new TreeNodeBaseTO(String.valueOf(TSystemStateBean.ENTITYFLAGS.ACCOUNTSTATE),
					LocalizeUtil.getLocalizedTextFromApplicationResources(RESOURCE_TYPE_KEYS.ACCOUNT_STATUS_KEY, locale),
					ICONS_CLS.ACCOUNT_STATUS_ICON, true);
			listOptionTreeNodeTOs.add(accountStatusNode);
		}
		return listOptionTreeNodeTOs;	
	}
	
	/**
	 * Get the grid rows children for a status list
	 * @param node
	 * @param personBean
	 * @param locale
	 * @return
	 */
	static String getLoadList(String node, TPersonBean personBean, 
			Locale locale) {
		ObjectStatusTokens objectStatusTokens = decodeNode(node);
		Integer entityFlag = objectStatusTokens.getListID();
		List<ObjectStatusGridRowTO> listOptionGridRowTOs = new LinkedList<ObjectStatusGridRowTO>();
		List<TSystemStateBean> systemStatusBeans = null;
		if (entityFlag!=null) {
			systemStatusBeans = SystemStatusBL.getStatusOptions(entityFlag, locale);
		}
		boolean isSys = personBean.isSys();
		if (systemStatusBeans!=null) {
			for (TSystemStateBean systemStateBean: systemStatusBeans) {
				listOptionGridRowTOs.add(createGridRow(systemStateBean, isSys,
						entityFlag,  getTypeflagLabel(entityFlag, systemStateBean, locale), locale));
			}
		 }
		 return ObjectStatusJSON.createListOptionGridRowsJSON(listOptionGridRowTOs);
	}
	
	/**
	 * Gets the bean by optionID or creates a new bean
	 * @param optionID
	 * @param listID
	 * @param locale
	 * @return
	 */
	private static TSystemStateBean getSystemOptionBean(Integer optionID, Integer listID, Locale locale) {
		TSystemStateBean systemStateBean = null;
		if (optionID!=null) {
			systemStateBean = SystemStatusBL.getExistingSystemOptionBeanByOptionID(optionID);
			if (systemStateBean!=null) {
				systemStateBean.setLabel(LocalizeUtil.localizeSystemStateEntry(systemStateBean, listID, locale));
			}
			//existing bean
			return systemStateBean;
		}
		//new bean
		return getNewSystemOptionBean(listID);
	}
	
	/**
	 * Gets the specific new label bean
	 * @param listID
	 * @return
	 */
	private static TSystemStateBean getNewSystemOptionBean(Integer listID) {
		TSystemStateBean systemStateBean = new TSystemStateBean();
		systemStateBean.setEntityflag(listID);
		return systemStateBean;
	}
	
	/**
	 * Gets the typeflag label if hasTypeFlag
	 * @param listID
	 * @param systemStateBean
	 * @param locale
	 * @return
	 */
	private static String getTypeflagLabel(Integer listID, TSystemStateBean systemStateBean, Locale locale) {
		return LocalizeUtil.getLocalizedTextFromApplicationResources(
				getTypeflagPrefix(listID) + systemStateBean.getStateflag(), locale);
	}
	
	/**
	 * Gets the possible typeflag prefix for a system field
	 * @param entityFlag
	 * @return
	 */
	private static String getTypeflagPrefix(int entityFlag) {
		switch (entityFlag) { 	
		case TSystemStateBean.ENTITYFLAGS.PROJECTSTATE:
			return "admin.customize.objectStatus.opt.project.";
		case TSystemStateBean.ENTITYFLAGS.RELEASESTATE:
			return "admin.customize.objectStatus.opt.release.";
		case TSystemStateBean.ENTITYFLAGS.ACCOUNTSTATE:
			return "admin.customize.objectStatus.opt.account.";
		default: 
			return null;
		}
	}
	
	
	
	/**
	 * Gets the option detail for a list entry  
	 * @param node
	 * @param add
	 * @param locale
	 * @return
	 */
	static String load(String node, boolean add, Locale locale) {
		ObjectStatusTokens objectStatusTokens = decodeNode(node);	
		Integer listID = objectStatusTokens.getListID();
		Integer optionID = objectStatusTokens.getOptionID();
		TSystemStateBean systemStateBean = getSystemOptionBean(optionID, listID, locale);
		List<IntegerStringBean> typeflagsList = new ArrayList<IntegerStringBean>();
		int[] possibleTypeflags = getPossibleTypeFlags(listID);
		if (possibleTypeflags!=null) {
			for (int i = 0; i < possibleTypeflags.length; i++) {
				int possibleValue = possibleTypeflags[i];
				typeflagsList.add(new IntegerStringBean(
					LocalizeUtil.getLocalizedTextFromApplicationResources(getTypeflagPrefix(listID) + possibleValue, locale), 
					Integer.valueOf(possibleValue)));
			}
		}				
		Integer typeFlag = systemStateBean.getStateflag();
		if (typeFlag==null) {
			//new option
			typeFlag = typeflagsList.get(0).getValue();
		}		
		String label = systemStateBean.getLabel();
		return ObjectStatusJSON.createObjectStatusDetailJSON(label, 
				typeFlag, typeflagsList);
	}
	
	/**
	 * Whether the label is valid
	 * @param node
	 * @param label
	 * @param add
	 * @param locale
	 * @return
	 */
	static String isValidLabel(String node, String label,
			boolean add, Locale locale) {
		ObjectStatusTokens objectStatusTokens = decodeNode(node);
		Integer listID = objectStatusTokens.getListID();
		Integer optionID = objectStatusTokens.getOptionID();
		Integer resourceType = null;
		String errorKey = isValidLabel(listID, optionID, label);
		if (errorKey!=null) {
			resourceType = listID;
		}
		if (errorKey!=null) {
			return LocalizeUtil.getParametrizedString(errorKey, 
					new Object[] {getLocalizedLabel(resourceType, locale)} , locale);
		}
		return null;
	}	
	
	/**
	 * Whether the new label is valid (not empty not duplicate)
	 * @param optionID
	 * @param label
	 * @return
	 */
	private static String isValidLabel(Integer listID, Integer optionID, String label)  {
		if (label==null || "".equals(label)) {
			return "common.err.required";
		}
		List<TSystemStateBean> labelBeans = SystemStatusBL.getExistingLabelBeansByLabel(listID, label);
		ILabelBean labelBean = null;
		if (labelBeans!=null && !labelBeans.isEmpty()) {
			if (labelBeans.size()>1) {
				return "common.err.unique";
			} else {
				labelBean = labelBeans.get(0);
				if (optionID==null || !labelBean.getObjectID().equals(optionID)) {
					return "common.err.unique";
				}
			}
		}
		return null;
	}
	
	/**
	 * Saves a new or existing custom list or system or custom list entry
	 * @param node
	 * @param label
	 * @param typeFlag
	 * @param add
	 * @param personBean
	 * @param locale
	 */
	static String save(String node, String label, Integer typeFlag,
			boolean add, TPersonBean personBean, Locale locale) {
		ObjectStatusTokens objectStatusTokens = ObjectStatusBL.decodeNode(node);
		Integer listID = objectStatusTokens.getListID();
		Integer optionID = objectStatusTokens.getOptionID();
		TSystemStateBean systemStateBean = prepareSystemStatusBean(optionID, listID, label,typeFlag);
		if (systemStateBean!=null) {
			Integer objectID = SystemStatusBL.save(systemStateBean, locale);
			ObjectStatusTokens objectStatusTokensReturn = new ObjectStatusTokens();
			objectStatusTokensReturn.setListID(listID);
			objectStatusTokensReturn.setOptionID(objectID);
			return ObjectStatusJSON.createSaveResultJSON(true, encodeNode(objectStatusTokensReturn), objectID);
		}
		return "";
	}	
		
	/**
	 * Prepares the system list option for save after add/edit
	 * @param optionID
	 * @param listID
	 * @param label
	 * @param typeflag
	 * @return
	 */
	private static TSystemStateBean prepareSystemStatusBean(Integer optionID, Integer listID, 
			String label, Integer typeflag) {
		TSystemStateBean systemStateBean;
		if (optionID==null) {
			systemStateBean = getNewSystemOptionBean(listID);
		} else {
			systemStateBean = SystemStatusBL.getExistingSystemOptionBeanByOptionID(optionID);
		}
		if (systemStateBean!=null) {
			systemStateBean.setLabel(label);
			if (typeflag!=null) {
				systemStateBean.setStateflag(typeflag);
			} else {
				if (systemStateBean.getStateflag()==null) {
					//not sent as request parameter because directly added from tree
					int[] typeflags = getPossibleTypeFlags(listID);
					if (typeflags.length>0) {
						systemStateBean.setStateflag(typeflags[0]);
					}
				}
			}			
		}
		return systemStateBean;
	}
	
	/**
	 * Gets the possible typeflags
	 * @param listID
	 * @return
	 */
	private static int[] getPossibleTypeFlags(Integer listID) {
		int entityFlag = listID;
		int[] possibleSpecials = new int[0];
		switch (entityFlag) {
		case ENTITYFLAGS.PROJECTSTATE:
			possibleSpecials = new int[] {STATEFLAGS.ACTIVE, 
					STATEFLAGS.INACTIVE, STATEFLAGS.CLOSED};
			break;
		case ENTITYFLAGS.RELEASESTATE:
			possibleSpecials = new int[] {STATEFLAGS.ACTIVE, 
				STATEFLAGS.INACTIVE, STATEFLAGS.NOT_PLANNED, STATEFLAGS.CLOSED};
			break;		
		case ENTITYFLAGS.ACCOUNTSTATE:
			possibleSpecials = new int[] {STATEFLAGS.ACTIVE, STATEFLAGS.CLOSED};
			break;
		}		
		return possibleSpecials;
	}
	
	
	/**
	 * Delete a list or a list option
	 * @param node
	 * @return
	 */
	static String delete(String node) {
		ObjectStatusTokens objectStatusTokens = ObjectStatusBL.decodeNode(node);
		Integer optionID = objectStatusTokens.getOptionID();
		if (SystemStatusBL.hasDependentData(optionID)) {
			return JSONUtility.encodeJSONFailure(JSONUtility.DELETE_ERROR_CODES.NEED_REPLACE);
		} else {
			SystemStatusBL.delete(optionID);
			return ObjectStatusJSON.createDeleteResultJSON(true, node, null);
		}
	}
	
	/**
	 * Render the replacement option 
	 * @param node
	 * @param errorMessage
	 * @param locale
	 * @return
	 */
	static String renderReplace(String node, String errorMessage, Locale locale) {
		ObjectStatusTokens objectStatusTokens = ObjectStatusBL.decodeNode(node);
		Integer listID = objectStatusTokens.getListID();
		Integer optionID = objectStatusTokens.getOptionID();
		TSystemStateBean systemStateBean = getSystemOptionBean(optionID, listID, locale);
		String label = null;
		if (systemStateBean!=null) {
			label = systemStateBean.getLabel();
		}
		List<TSystemStateBean> replacementsList = getReplacementList(listID, optionID, locale);
		String localizedLabel=ObjectStatusBL.getLocalizedLabel(listID, locale);		
		return JSONUtility.createReplacementListJSON(true, label, localizedLabel, localizedLabel, (List)replacementsList, errorMessage, locale);	
	}
	
	/**
	 * Replace and delete
	 * @param replacementID
	 * @param node
	 * @param locale
	 * @return
	 */
	static String replaceAndDelete(Integer replacementID, String node, Locale locale) {
		if (replacementID==null) {
			ObjectStatusTokens objectStatusTokens = ObjectStatusBL.decodeNode(node);
			Integer listID = objectStatusTokens.getListID();
			String errorMessage =  LocalizeUtil.getParametrizedString("common.err.replacementRequired", 
					new Object[] {ObjectStatusBL.getLocalizedLabel(listID, locale)}, locale);
			return renderReplace(node, errorMessage, locale);
		} else {
			replaceAndDeleteListEntry(node, replacementID);
			return ObjectStatusJSON.createNodeResultJSON(true, node, null);
		}
	}	
	
	private static void replaceAndDeleteListEntry(String node, Integer replacementID) {
		ObjectStatusTokens objectStatusTokens = ObjectStatusBL.decodeNode(node);
		Integer optionID = objectStatusTokens.getOptionID();
		SystemStatusBL.replaceAndDelete(optionID, replacementID);
	}
	
	
	private static List<TSystemStateBean> getReplacementList(Integer entityFlag, Integer optionID, Locale locale) {
		List<TSystemStateBean> replacementOptions = null;
		if (entityFlag!=null) {
			replacementOptions = SystemStatusBL.getStatusOptions(entityFlag, locale);
			for (Iterator<TSystemStateBean> iterator = replacementOptions.iterator(); iterator.hasNext();) {
				ILabelBean labelBean = iterator.next();
				if (labelBean.getObjectID().equals(optionID)) {
					iterator.remove();
					break;
				}
			}
		}
		return replacementOptions;
	}
			
	static Map<Integer, String> getLocalizedLabels(Locale locale) {
		Map<Integer, String> localizedLists = new HashMap<Integer, String>();
		localizedLists.put(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE, 
				LocalizeUtil.getLocalizedTextFromApplicationResources(RESOURCE_TYPE_KEYS.PROJECT_STATUS_KEY, locale));
		localizedLists.put(TSystemStateBean.ENTITYFLAGS.RELEASESTATE, 
				LocalizeUtil.getLocalizedTextFromApplicationResources(RESOURCE_TYPE_KEYS.RELEASE_STATUS_KEY, locale));
		localizedLists.put(TSystemStateBean.ENTITYFLAGS.ACCOUNTSTATE, 
				LocalizeUtil.getLocalizedTextFromApplicationResources(RESOURCE_TYPE_KEYS.ACCOUNT_STATUS_KEY, locale));
		return localizedLists;
	}
	
	private static String getLocalizedLabel(Integer listID, Locale locale) {
		if (listID!=null) {
			switch (listID.intValue()) {
			case TSystemStateBean.ENTITYFLAGS.PROJECTSTATE:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(
						RESOURCE_TYPE_KEYS.PROJECT_STATUS_KEY, locale);
			case TSystemStateBean.ENTITYFLAGS.RELEASESTATE:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(
						RESOURCE_TYPE_KEYS.RELEASE_STATUS_KEY, locale);
			case TSystemStateBean.ENTITYFLAGS.ACCOUNTSTATE:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(
						RESOURCE_TYPE_KEYS.ACCOUNT_STATUS_KEY, locale);
			}
		}
		return "";
	}
		
	
	/**
	 * Creates a new grid row
	 * @param systemStateBean
	 * @param modifiable
	 * @param listForLabel
	 * @param typeflagLabel
	 * @param locale
	 * @return
	 */
	private static ObjectStatusGridRowTO createGridRow(TSystemStateBean systemStateBean, boolean modifiable, 
			Integer listForLabel, String typeflagLabel, Locale locale) {
		ObjectStatusGridRowTO objectStatusGridRowTO = new ObjectStatusGridRowTO();
		ObjectStatusTokens objectStatusTokens = new ObjectStatusTokens();
		objectStatusTokens.setListID(systemStateBean.getEntityflag());
		objectStatusTokens.setOptionID(systemStateBean.getObjectID());
		objectStatusGridRowTO.setId(systemStateBean.getObjectID());
		objectStatusGridRowTO.setNode(ObjectStatusBL.encodeNode(objectStatusTokens));
		objectStatusGridRowTO.setLabel(systemStateBean.getLabel());		
		objectStatusGridRowTO.setListForLabel(listForLabel);
		objectStatusGridRowTO.setModifiable(modifiable);
		objectStatusGridRowTO.setTypeflagLabel(typeflagLabel);
		return objectStatusGridRowTO;
	}
	
	/**
	 * Drop after drag
	 * @param draggedNodeID
	 * @param droppedToNodeID
	 * @param before
	 * @param locale
	 */
	static Integer droppedNear(String draggedNodeID, String droppedToNodeID, boolean before, Locale locale) {
		ObjectStatusTokens draggedTokens = decodeNode(draggedNodeID);
		Integer listID = draggedTokens.getListID();
		Integer draggedOptionID = draggedTokens.getOptionID();
		ObjectStatusTokens droppedToTokens = decodeNode(droppedToNodeID);
		Integer droppedToOptionID = droppedToTokens.getOptionID();
		
		//hopefully not needed, but just to be sure
		normalizeSortOrder(draggedNodeID, locale);
		dropNear(draggedOptionID, droppedToOptionID, before, listID, locale);
		return draggedOptionID;
	}
	
	/**
	 * Move up the option
	 * @param draggedNodeID
	 * @param locale
	 */
	static Integer moveUp(String draggedNodeID, Locale locale) {
		ObjectStatusTokens draggedTokens = decodeNode(draggedNodeID);
		Integer listID = draggedTokens.getListID();
		Integer draggedOptionID = draggedTokens.getOptionID();
		List<TSystemStateBean> sortOrderBeans = normalizeSortOrder(draggedNodeID, locale);
		ILabelBean previousLabelBean = null;
		boolean found = false;
		for (TSystemStateBean sytemStateBean : sortOrderBeans) {
			if (sytemStateBean.getObjectID().equals(draggedOptionID)) {
				found = true;
				break;
			}
			previousLabelBean = sytemStateBean;
		}
		if (found && previousLabelBean!=null) {
			dropNear(draggedOptionID, previousLabelBean.getObjectID(), true, listID, locale);
		}
		return draggedOptionID;
	}
	
	/**
	 * Move down the option
	 * @param draggedNodeID
	 * @param locale
	 */
	static Integer moveDown(String draggedNodeID, Locale locale) {
		ObjectStatusTokens draggedTokens = decodeNode(draggedNodeID);
		Integer listID = draggedTokens.getListID();
		Integer draggedOptionID = draggedTokens.getOptionID();
		List<TSystemStateBean> sortOrderBeans = normalizeSortOrder(draggedNodeID, locale);
		ILabelBean nextSortedBean = null;
		for (Iterator<TSystemStateBean> iterator = sortOrderBeans.iterator(); iterator.hasNext();) {
			TSystemStateBean systemStateBean = iterator.next();
			if (systemStateBean.getObjectID().equals(draggedOptionID)) {
				if (iterator.hasNext()) {
					nextSortedBean = iterator.next();
					break;
				}
			}
		}		
		if (nextSortedBean!=null) {
			dropNear(draggedOptionID, nextSortedBean.getObjectID(), false, listID, locale);
		}
		return draggedOptionID;
	}
	
	/**
	 * Sets the wbs on the affected workItems after a drag and drop operation
	 * @param draggedID
	 * @param droppedToID
	 * @param before
	 */
	private static synchronized void dropNear(Integer draggedID, Integer droppedToID, 
			boolean before, Integer listID, Locale locale) {
		TSystemStateBean draggedBean = getSystemOptionBean(draggedID, listID, locale);
		TSystemStateBean droppedToBean =  getSystemOptionBean(droppedToID, listID, locale);
		Integer draggedSortOrder = draggedBean.getSortorder();
		Integer droppedToSortOrder = droppedToBean.getSortorder();
		String sortOrderColumn = SystemStatusBL.getSortOrderColumn();
		String tabelName = SystemStatusBL.getTableName();
		Integer newSortOrder = SortOrderUtil.dropNear(draggedSortOrder, droppedToSortOrder,
				sortOrderColumn, tabelName, getSpecificCriteria(draggedBean), before);
		draggedBean.setSortorder(newSortOrder);
		SystemStatusBL.saveSimple(draggedBean);
	}
	
	/**
	 * Gets the specific extra constraints
	 * @param systemStateBean
	 * @return
	 */
	private static String getSpecificCriteria(TSystemStateBean systemStateBean) {
		Integer entityFlag = systemStateBean.getEntityflag();
		String entityFlagCriteria = " AND ENTITYFLAG = " + entityFlag;
		return entityFlagCriteria;
	}
	
	/**
	 * Normalize the sortorder before change  
	 * @param node
	 * @param locale
	 * @return
	 */
	private static List<TSystemStateBean> normalizeSortOrder(String node, Locale locale) {
		ObjectStatusTokens listOptionIDTokens = decodeNode(node);
		Integer entityFlag = listOptionIDTokens.getListID();
		List<TSystemStateBean> sortedBeans = null;
		if (entityFlag!=null) {
			sortedBeans = SystemStatusBL.getStatusOptions(entityFlag, locale);
			if (sortedBeans!=null) {
				Iterator<TSystemStateBean> itrSortedObjects = sortedBeans.iterator();
				int i = 0;
				while(itrSortedObjects.hasNext()) {
					i++;
					TSystemStateBean sortedObject = itrSortedObjects.next();
					Integer sortOrder = sortedObject.getSortorder();
					if (sortOrder==null || sortOrder.intValue()!=i) {
						//set and save only if differs					
						sortedObject.setSortorder(Integer.valueOf(i));
						SystemStatusBL.save(sortedObject, locale);
					}
				}
			}
		}
		return sortedBeans;
	}
}
