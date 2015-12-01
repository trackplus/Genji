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

package com.aurel.track.item;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.item.history.FlatHistoryBean;
import com.aurel.track.item.history.HistoryBean;
import com.aurel.track.item.history.HistoryComparator;
import com.aurel.track.item.history.HistoryEntry;
import com.aurel.track.item.history.HistoryLoaderBL;
import com.aurel.track.item.history.HistoryLoaderBL.LONG_TEXT_TYPE;
import com.aurel.track.item.history.HistoryValues;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;

public class PrintItemDetailsBL {
	private static final Logger LOGGER = LogManager.getLogger(PrintItemDetailsBL.class);
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
	
	/**
	 * Gets all history, cost, budget data for the current issue and all his descendants  
	 * @param workItemID
	 * @param personID
	 * @param locale
	 * @return
	 */
	public static List<FlatHistoryBean> getAllFlatWithChildren(Integer workItemID, Integer personID, Locale locale) {
		Set<Integer> descendentsSet = ItemBL.getChildHierarchy(new int[]{workItemID});
		int[] descendentsArr = GeneralUtils.createIntArrFromSet(descendentsSet);
		List<TWorkItemBean> descendentWorkItemsList = LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(
				descendentsArr, personID, false, false, false);
		List<FlatHistoryBean> flatHistoryList = getCurrentValuesForDescendents(descendentWorkItemsList);
		//add the current item (the parent) to the list 
		descendentsSet.add(workItemID);
		descendentsArr = GeneralUtils.createIntArrFromSet(descendentsSet);
		try {
			descendentWorkItemsList.add(workItemDAO.loadByPrimaryKey(workItemID));
		} catch (ItemLoaderException e) {
			LOGGER.error("WorkItem " + workItemID + " not found " + e.getMessage());
		}
		Map<Integer, TWorkItemBean> workItemBeansMap = GeneralUtils.createMapFromList(descendentWorkItemsList);
		//history entries
		flatHistoryList.addAll(getFlatHistoryForIssueAndDescendants(descendentsArr,  workItemBeansMap, personID, locale));
		//attachment entries
		flatHistoryList.addAll(getFlatAttachmentsWithChildren(descendentsArr, workItemBeansMap, locale));
		Collections.sort(flatHistoryList);
		return flatHistoryList;
	}
	
	/**
	 * Get the detail FlatHistoryBean for descendants
	 * @param workItemBeansList
	 * @return
	 */
	private static List<FlatHistoryBean> getCurrentValuesForDescendents(
			List<TWorkItemBean> workItemBeansList) {
		List<FlatHistoryBean> flatHistoryBeanList = new ArrayList<FlatHistoryBean>();
		for (TWorkItemBean workItemBean : workItemBeansList) {
			FlatHistoryBean flatHistoryBean = new FlatHistoryBean();
			TPersonBean personBean = LookupContainer.getPersonBean(workItemBean.getOriginatorID());
			if (personBean!=null) {
				//changed by name is in this case the originator's name
				flatHistoryBean.setChangedByName(personBean.getLabel());
				flatHistoryBean.setPersonID(personBean.getObjectID());
			}
			//last edit in this case is the date of creation
			flatHistoryBean.setLastEdit(workItemBean.getCreated());
			flatHistoryBean.setWorkItemID(workItemBean.getObjectID());
			flatHistoryBean.setTitle(workItemBean.getSynopsis());
			flatHistoryBean.setRenderType(FlatHistoryBean.RENDER_TYPE.CHILD_DETAIL);
			flatHistoryBean.setIconName("add.png");
			flatHistoryBeanList.add(flatHistoryBean);
		}
		return flatHistoryBeanList;
	}
	
	/**
	 * Gets the flat history for the issue and all his descendants
	 * @param allDescArr
	 * @param workItemBeansMap
	 * @return
	 */
	private static List<FlatHistoryBean> getFlatHistoryForIssueAndDescendants(int[] allDescArr, 
			Map<Integer, TWorkItemBean> workItemBeansMap, Integer personID, Locale locale) {
		Map<Integer, SortedMap<Integer, Map<Integer, HistoryValues>>> allHistoryMap = HistoryLoaderBL.getWorkItemsHistory(
				allDescArr, null, false, null, null, null, locale, false, LONG_TEXT_TYPE.ISFULLHTML, true, personID);
		return HistoryLoaderBL.getFlatHistoryValuesListForWorkItems(allHistoryMap, workItemBeansMap, locale);
	}
	
	/**
	 * Gets the flat history for the current issue
	 * @return
	 */
	public static List<FlatHistoryBean> getFlatHistory(TWorkItemBean workItemBean, Integer personID, Locale locale){
		List<FlatHistoryBean> flatHistoryList;
		Integer workItemID = workItemBean.getObjectID();
		if (workItemID==null) {
			//no history for new workItem
			flatHistoryList = new ArrayList<FlatHistoryBean>();
		} else {
			SortedMap<Integer, Map<Integer, HistoryValues>> allHistoryMap=HistoryLoaderBL.getRestrictedWorkItemHistory(personID,
					workItemBean, locale, false, LONG_TEXT_TYPE.ISFULLHTML);
			flatHistoryList= HistoryLoaderBL.getFlatHistoryValuesList(allHistoryMap, null, null, locale, true, false);
			//add the budget history
			if (flatHistoryList.size() > 0) {
				// sort all history beans chronologically
				Collections.sort(flatHistoryList, new HistoryComparator());
			}
	}
		return flatHistoryList;
	}

	
	
	
	
	
	
	public static List<FlatHistoryBean> getFlatAttachmentsWithChildren(int[] workItemIDs, 
			Map<Integer, TWorkItemBean> workItemBeansMap, Locale locale) {
		List<TAttachmentBean> attachmentBeans = AttachBL.loadByWorkItems(workItemIDs);
		return loadAttachmentFlatHistoryBeans(attachmentBeans, workItemBeansMap, locale, true);
	}
	
	public static List<FlatHistoryBean> getFlatAttachments(Integer workItemID, Locale locale) {
		List<TAttachmentBean> attachmentBeans = AttachBL.getAttachments(workItemID);
		return loadAttachmentFlatHistoryBeans(attachmentBeans, null, locale, false);
	}		

	/**
	 * The big difference relative to other FlatHistoryBean of RENDER_TYPE=ACTUAL_VALUES is that 
	 * some of HistoryEntries of FlatHistoryBean have oldValue set which means special handling in jsp:
	 * either rendering a image or create a link for attachment
	 * @param attachmentBeans
	 * @param locale
	 * @return
	 */
	private static List<FlatHistoryBean> loadAttachmentFlatHistoryBeans(
			List<TAttachmentBean> attachmentBeans, 
			Map<Integer, TWorkItemBean> workItemBeansMap, Locale locale, boolean withChildren) {
		List<FlatHistoryBean> flatValueList = new ArrayList<FlatHistoryBean>();
		Integer stretchPercent = null;
		Integer maxWidth = null;
		Integer maxHeight = null;
		Properties properties = loadConfigFromFile();
		stretchPercent = getConfigByName(properties, "stretchPercent", 100);
		maxWidth = getConfigByName(properties, "maxWidth", 500);
		maxHeight = getConfigByName(properties, "maxHeight", 500);
		if (attachmentBeans!=null) {
			for (TAttachmentBean attachmentBean : attachmentBeans) {
				FlatHistoryBean flatHistoryBean = new FlatHistoryBean();
				List<HistoryEntry> historyEntries = new ArrayList<HistoryEntry>();
				flatHistoryBean.setHistoryEntries(historyEntries);
				flatHistoryBean.setChangedByName(attachmentBean.getChangedByName());
				flatHistoryBean.setPersonID(attachmentBean.getChangedBy());
				flatHistoryBean.setLastEdit(attachmentBean.getLastEdit());
				flatHistoryBean.setType(HistoryBean.HISTORY_TYPE.ATTACHMENT);
				flatHistoryBean.setIconName("attachment.png");
				HistoryLoaderBL.addWorkItemToFlatHistoryBean(flatHistoryBean, workItemBeansMap, 
						attachmentBean.getWorkItem(), FlatHistoryBean.RENDER_TYPE.ACTUAL_VALUES);
				flatHistoryBean.setWorkItemID(attachmentBean.getWorkItem());
				
				HistoryEntry historyEntry = new HistoryEntry();
				/*render the attachmnet download: 
				historyEntry.fieldLabel -> Name
				historyEntry.newValue -> the fileName
				historyEntry.oldValue -> attachmentID: the value is used in the URL						 
				*/
				historyEntry.setFieldLabel(LocalizeUtil.getLocalizedTextFromApplicationResources(
	 					"common.lbl.name", locale));
				historyEntry.setNewValue(attachmentBean.getFileName());
				historyEntry.setOldValue(attachmentBean.getObjectID().toString());
				
				historyEntries.add(historyEntry);
				if (!withChildren) {						
					historyEntry = new HistoryEntry();
					historyEntry.setFieldLabel(LocalizeUtil.getLocalizedTextFromApplicationResources(
		 					"common.lbl.size", locale));
					historyEntry.setNewValue(TAttachmentBean.getFileSizeString(attachmentBean.getSize()));
					historyEntries.add(historyEntry);
								
					String description = attachmentBean.getDescription();
					if (description!=null && !"".equals(description)) {
						historyEntry = new HistoryEntry();
						historyEntry.setFieldLabel(LocalizeUtil.getLocalizedTextFromApplicationResources(
			 					"common.lbl.description", locale));
						historyEntry.setNewValue(description);
						historyEntries.add(historyEntry);
					}
				}
				boolean isImage = AttachBL.isImage(attachmentBean);
				if (isImage) {
					/*render the image: 
						historyEntry.fieldLabel -> null
						historyEntry.newValue -> image width (in pixels)
						historyEntry.oldValue -> attachmentID: used in the URL					 
					*/
					TAttachmentBean attachmentBeanPopulated = AttachBL.loadAttachment(attachmentBean.getObjectID(),attachmentBean.getWorkItem(),true);
					String fileNameOnDisk = attachmentBeanPopulated.getFullFileNameOnDisk();
					File f = new File(fileNameOnDisk);
					BufferedImage image;
					Integer originalWidth = null;
					Integer originalHeight = null;
					try {
						image = ImageIO.read(f);
						originalWidth = image.getWidth();
						originalHeight = image.getHeight();
					} catch (IOException e) {
						LOGGER.warn("Reading the image failed with " + e.getMessage());
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}				   
					
					if (fileNameOnDisk!=null && !"".equals(fileNameOnDisk)) {
						historyEntry = new HistoryEntry();
						if (originalWidth!=null && originalHeight!=null) {
							Integer calculatedWidth = Integer.valueOf(originalWidth.intValue()*stretchPercent/100);
							Integer calculatedHeight = Integer.valueOf(originalHeight.intValue()*stretchPercent/100);
							if (calculatedWidth.intValue()>maxWidth.intValue() &&
									calculatedHeight.intValue()>maxHeight.intValue()) {
								double widthScale = calculatedWidth.doubleValue()/maxWidth.doubleValue();
								double heightScale = calculatedHeight.doubleValue()/maxHeight.doubleValue();
								if (widthScale>heightScale) {
									calculatedWidth = maxWidth;
								} else {
									calculatedWidth = Integer.valueOf((int)(calculatedWidth.doubleValue()*maxHeight.doubleValue()/calculatedHeight.doubleValue()));
								}
							} else {
								if (calculatedWidth.intValue()>maxWidth.intValue()) {
									calculatedWidth = maxWidth;
								} else {
									if (calculatedHeight.intValue()>maxHeight.intValue()) {
										calculatedWidth = Integer.valueOf((int)(calculatedWidth.doubleValue()*maxHeight.doubleValue()/calculatedHeight.doubleValue()));
									}
								}
							}
							historyEntry.setNewValue(calculatedWidth.toString());
						}
						historyEntry.setOldValue(attachmentBean.getObjectID().toString());
						historyEntries.add(historyEntry);
					}
				}
				flatValueList.add(flatHistoryBean);
			}
		}
		return flatValueList;
	} 
	
	/**
	 * Loads the sizes from a property file
	 * @return
	 */
	private static Properties loadConfigFromFile() {
		Properties properties = new Properties();
		InputStream inputStream = PrintItemDetailsBL.class.getResourceAsStream("PrintItem.properties");
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			LOGGER.warn("Loading the PrintItem.properties from classpath failed with " + e.getMessage());
		}
		return properties;
	}
	
	private static Integer getConfigByName(Properties properties, String configString, int defaultValue) {
		Integer configValue = null;
		try {
			configValue = Integer.valueOf(properties.getProperty(configString).trim());
		} catch (Exception e) {
			LOGGER.warn("Loading the " + configString + " failed with " + e.getMessage());
		}
		if (configValue==null || configValue.intValue()<=0) {
			return Integer.valueOf(defaultValue);
		} else {
			return configValue;
		}
	}
	
	
}
