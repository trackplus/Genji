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

package com.aurel.track.admin.customize.lists.importList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.customize.lists.ListOptionIDTokens;
import com.aurel.track.admin.customize.lists.customOption.OptionBL;
import com.aurel.track.admin.customize.treeConfig.screen.importScreen.IExchangeFieldNames;
import com.aurel.track.beans.TListBean;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.configExchange.importer.EntityImporter;
import com.aurel.track.configExchange.importer.EntityImporterException;
import com.aurel.track.configExchange.importer.ImportContext;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.json.JSONUtility;

public class ImportListBL {
	
	private static OptionParser optionParser = new OptionParser();
	private static Map<String, Map<Integer, Integer>> lookUpMap = new HashMap<String, Map<Integer, Integer>>();
	
	private static final Logger LOGGER = LogManager.getLogger(ImportListBL.class);
	
	/**
	 * Imports a list from an xml file
	 * @param uploadFile
	 * @return
	 */
	public static void importList(File uploadFile, String node, boolean overwriteExisting, boolean clearChildren) {
		ListOptionIDTokens listOptionIDTokens = ListBL.decodeNode(node);
		Integer repository = listOptionIDTokens.getRepository();
		Integer projectID = null;
		if (ListBL.REPOSITORY_TYPE.PROJECT==repository) {
			projectID = listOptionIDTokens.getProjectID();
		}
		boolean contained = false;
		Map<Integer, Integer> listObjectIdMap = new HashMap<Integer, Integer>();
		try {
			EntityImporter entityImporter = new EntityImporter();
			Map<String,String> extraAttributes=new HashMap<String, String>();
			if(projectID!=null){
				extraAttributes.put("project",projectID.toString());
				extraAttributes.put("repositoryType",TListBean.REPOSITORY_TYPE.PROJECT+"");
			}else{
				extraAttributes.put("project",null);
				extraAttributes.put("repositoryType",TListBean.REPOSITORY_TYPE.PUBLIC+"");
			}
			ImportContext importContext=new ImportContext();
			importContext.setEntityType("TListBean");
			importContext.setOverrideExisting(overwriteExisting);
			importContext.setClearChildren(clearChildren);
			importContext.setAttributeMap(extraAttributes);
			entityImporter.importFile(uploadFile, importContext);
		} catch (EntityImporterException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			JSONUtility.encodeJSONFailure(e.getMessage());
		} catch (Exception e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			JSONUtility.encodeJSONFailure(e.getMessage());
		}
	}
	
	/**
	 * Import options related to the lists being imported 
	 * @param contained
	 * @param uploadFile
	 * @param addNew
	 * @param deleteMissing
	 */
	private static void importOption(List<TListBean> usedLists, boolean contained, File uploadFile, boolean addNew, boolean deleteMissing) {
		Map<Integer, Integer> OptionObjectIdMap = new HashMap<Integer, Integer>();
		List<TOptionBean> removableElements =null;
		List<TOptionBean> xmlOptionBeans = optionParser.parse(uploadFile);
		List<Integer> listIDs = new ArrayList<Integer>();
		List<TOptionBean> dbOptionBeans = null;
		if (contained != false){
			for (TListBean l : usedLists){
				listIDs.add(l.getObjectID());
			}
			dbOptionBeans = OptionBL.loadForListIDs(listIDs);
			if (deleteMissing == true)
				removableElements = new ArrayList<TOptionBean>(dbOptionBeans);
		}
		for (TOptionBean l : xmlOptionBeans){
			System.out.println("Name = " + l.getLabel());
		}			
		Iterator<TOptionBean> xmlItr = xmlOptionBeans.iterator();
		while (xmlItr.hasNext()){
			boolean isUpdated = false;
			TOptionBean tmpXmlOptionBean = xmlItr.next();
			if (dbOptionBeans!=null) {
				Iterator<TOptionBean> dbItr = dbOptionBeans.iterator();
				while(dbItr.hasNext()){
					TOptionBean tmpDbOptionBean = dbItr.next();
					if (tmpXmlOptionBean.considerAsSame(tmpDbOptionBean, lookUpMap)){
						isUpdated = true;
						OptionObjectIdMap.put(tmpXmlOptionBean.getObjectID(), tmpDbOptionBean.getObjectID());
						if (deleteMissing == true){
							removeOptionElement(removableElements, tmpDbOptionBean);}
						if ((contained && addNew)){
							tmpXmlOptionBean = getOptionValues(tmpDbOptionBean, tmpXmlOptionBean);
							tmpDbOptionBean.saveBean(tmpXmlOptionBean,lookUpMap);
						}
					}
				}
			}
			if (isUpdated == false)
				if (!contained || (contained && addNew)){
					System.out.println("Uj");
					tmpXmlOptionBean.setNew(true);
						Integer updatedObjectID = tmpXmlOptionBean.saveBean(tmpXmlOptionBean, lookUpMap);	
						OptionObjectIdMap.put(tmpXmlOptionBean.getObjectID(), 
											updatedObjectID);
				}
			lookUpMap.put(IExchangeFieldNames.TOPTION, OptionObjectIdMap);	
		}	
		if (contained && deleteMissing == true  && (removableElements!=null && !removableElements.isEmpty())) {
			for (TOptionBean l : removableElements)
				OptionBL.delete(l.getObjectID());
		}
	}
	
	/**
	 * Set the Type of the list regarding of its type (project specific or global)
	 * @param xmlListBean
	 * @param projectID
	 */
	private static void setGlobalOrProjectSpecific(TListBean xmlListBean, Integer projectID){
		Map<Integer,Integer> PersonObjectIDMap = new HashMap<Integer, Integer>();
		if (projectID == null && xmlListBean.getParentList() == null) {
				xmlListBean.setProject(null);
		
			if (xmlListBean.getOwner()!=null) {
				xmlListBean.setOwner(1);
				PersonObjectIDMap.put(1, 1);
				lookUpMap.put(MergeUtil.mergeKey(SystemFields.INTEGER_PERSON, null), PersonObjectIDMap);	
			}	
			xmlListBean.setRepositoryType(2);
		}
		else if (projectID != null && xmlListBean.getParentList() == null){
			Map<Integer,Integer> ProjectObjectIDMap = new HashMap<Integer, Integer>();	
			xmlListBean.setOwner(1);
			xmlListBean.setProject(projectID);
			ProjectObjectIDMap.put(projectID, projectID);
			PersonObjectIDMap.put(1, 1);	
			lookUpMap.put(MergeUtil.mergeKey(SystemFields.INTEGER_PROJECT, null), ProjectObjectIDMap);
			lookUpMap.put(MergeUtil.mergeKey(SystemFields.INTEGER_PERSON, null), PersonObjectIDMap);	
			xmlListBean.setRepositoryType(3);
		}
	}
	
	/**
	 * Perform the necessary changes on the TListBean extracted from the xml
	 * Updates the values which remain the same in the database
	 * @param dbListBean
	 * @param xmlListBean
	 * @return
	 */
	private static TListBean getListValues(TListBean dbListBean, TListBean xmlListBean, Integer projectID)
	{	
		String dbUuid = dbListBean.getUuid();
		xmlListBean.setUuid(dbUuid);
		xmlListBean.setObjectID(dbListBean.getObjectID());
		xmlListBean.setNew(false);
		xmlListBean.setModified(true);
		return xmlListBean;
	}
	
		
	/**
	 * Perform the necessary changes on the TOptionBean extracted from the xml
	 * Updates the values which remain the same in the database
	 * @return
	 */
	private static TOptionBean getOptionValues(TOptionBean dbOptionBean, TOptionBean xmlOptionBean)
	{	
		String dbUuid = dbOptionBean.getUuid();
		xmlOptionBean.setUuid(dbUuid);
		xmlOptionBean.setObjectID(dbOptionBean.getObjectID());
		
		xmlOptionBean.setNew(false);
		xmlOptionBean.setModified(true);
		return xmlOptionBean;
	}

	/**
	 * Load the list hierarchy from the database which will be updated
	 * @param globalList
	 * @return
	 */
	public static List<TListBean> loadHierarchy(TListBean globalList) {
		List<TListBean> list = new ArrayList<TListBean>();
		List<TListBean> queue = new ArrayList<TListBean>();
		if (ListBL.loadByPrimaryKey(globalList.getObjectID()) != null) {
			list.add(globalList);
			queue.add(globalList);
			do
			{
				List<TListBean> childList = ListBL.getChildLists(queue.get(0).getObjectID()); 
				queue.remove(0);
				
				Iterator itr = childList.iterator();
				while(itr.hasNext()) {
					TListBean tmpListBean = (TListBean)itr.next();
					list.add(tmpListBean);
					queue.add(tmpListBean);
				}
			}while(queue.size() != 0);
		}
		return list;
	}

	/**
	 * Remove the lists that won't be deleted
	 * @param removableElements
	 * @param listBean
	 */
	private static void removeListElement(List<TListBean>removableElements, TListBean listBean){
		for (int i = 0; i < removableElements.size(); i++)
			if (removableElements.get(i).getObjectID().equals(listBean.getObjectID()))
				removableElements.remove(i);
	}
	
	/**
	 * Remove the options that won't be deleted
	 * @param removableElements
	 * @param optionBean
	 */
	private static void removeOptionElement(List<TOptionBean>removableElements, TOptionBean optionBean){
		for (int i = 0; i < removableElements.size(); i++) {
			if (removableElements.get(i).getObjectID().equals(optionBean.getObjectID()))
				removableElements.remove(i);
		}
	}	
	
}
