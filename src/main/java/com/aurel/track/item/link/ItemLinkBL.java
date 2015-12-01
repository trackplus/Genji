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


package com.aurel.track.item.link;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TLinkTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.cluster.ClusterBL.CHANGE_TYPE;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.dao.WorkItemLinkDAO;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.exchange.msProject.importer.LinkLagBL;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.linkType.ILinkType;
import com.aurel.track.linkType.ILinkType.LINK_DIRECTION;
import com.aurel.track.linkType.LinkTypeBL;
import com.aurel.track.lucene.index.associatedFields.LinkIndexer;
import com.aurel.track.plugin.PluginManager;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.LabelValueBean;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Business logic class for workItem links
 * @author Tamas
 *
 */
public class ItemLinkBL {
	private static final Logger LOGGER = LogManager.getLogger(ItemLinkBL.class);
	private static WorkItemLinkDAO workItemLinkDAO=DAOFactory.getFactory().getWorkItemLinkDAO();
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();

	/**
	 * Gets the TWorkItemLinkBean by key
	 * @param linkID
	 * @return
	 */
	public static TWorkItemLinkBean loadByPrimaryKey(Integer linkID) {
		return workItemLinkDAO.loadByPrimaryKey(linkID);
	}

	/**
	 * Gets the links to successors from itemID as predecessor
	 * @param itemID
	 * @return
	 */
	static List<TWorkItemLinkBean> getSuccessorsForMeAsPredecessor(Integer itemID) {
		return workItemLinkDAO.loadByWorkItemPred(itemID);
	}

	/**
	 * Gets the links from predecessors to itemID as successor
	 * @param itemID
	 * @return
	 */
	static List<TWorkItemLinkBean> getPredecessorsForMeAsSuccessor(Integer itemID) {
		return workItemLinkDAO.loadByWorkItemSucc(itemID);
	}

	/**
	 * Load all successors for an workItem for a link type
	 * @param itemID
	 * @param linkType
	 * @return
	 */
	static List<TWorkItemLinkBean> loadByPredAndLinkType(Integer itemID, Integer linkType, Integer direction) {
		return workItemLinkDAO.loadByPredAndLinkType(itemID, linkType, direction);
	}

	/**
	 * Load all predecessors for an workItem  for a link type
	 * @param itemID
	 * @param linkType
	 * @return
	 */
	static List<TWorkItemLinkBean> loadBySuccAndLinkType(Integer itemID, Integer linkType, Integer direction) {
		return workItemLinkDAO.loadBySuccAndLinkType(itemID, linkType, direction);
	}

	/**
	 * Whether any link of linkTypeIDs exists already between two workItems
	 * @param worItemLinkID
	 * @param linkPred
	 * @param linkSucc
	 * @param linkTypeIDs
	 * @return
	 */
	public static List<TWorkItemLinkBean> loadLinksOfWorkItems(Integer worItemLinkID, Integer linkPred, Integer linkSucc, List<Integer> linkTypeIDs) {
		return workItemLinkDAO.loadLinksOfWorkItems(worItemLinkID, linkPred, linkSucc, linkTypeIDs);
	}

	/**
	 * Loads all workItemLinkBeans by link types and direction
	 * @param linkTypeIDs
	 * @param linkDirection
	 * @return
	 */
	public static List<TWorkItemLinkBean> loadByLinkTypeAndDirection(List<Integer> linkTypeIDs, Integer linkDirection) {
		return workItemLinkDAO.loadByLinkTypeAndDirection(linkTypeIDs, linkDirection);
	}

	/**
	 * Loads all indexable workItemLinkBeans
	 * @return
	 */
	public static List<TWorkItemLinkBean> loadAllIndexable() {
		return workItemLinkDAO.loadAllIndexable();
	}

	/**
	 * Loads a workItemLinkBean list by ids
	 * @param linkIDs
	 * @return
	 */
	public static List<TWorkItemLinkBean> loadByIDs(List<Integer> linkIDs) {
		return workItemLinkDAO.loadByIDs(linkIDs);
	}

	/**
	 * Gets the number of links of this item
	 * @param itemID
	 * @return
	 */
	public static int countLinks(Integer itemID){
		return workItemLinkDAO.countByWorkItem(itemID);
	}

	/**
	 * Gets the links filtered by filterSelectsTO and raciBean
	 * @param filterUpperTO
	 * @param raciBean
	 * @param personID
	 * @return
	 */
	public static List<TWorkItemLinkBean> loadTreeFilterLinks(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID) {
		return workItemLinkDAO.loadTreeFilterLinks(filterUpperTO, raciBean, personID);
	}

	/**
	 * Get the links for a TQL expression
	 * @param tqlExpression
	 * @param personBean
	 * @param locale
	 * @param errors
	 * @return
	 */
	public static List<TWorkItemLinkBean> loadTQLFilterLinks(String tqlExpression, TPersonBean personBean, Locale locale, List<ErrorData> errors) {
		return workItemLinkDAO.loadTQLFilterLinks(tqlExpression, personBean, locale, errors);
	}

	/**
	 * Load all linked workItems for a list of workItemIDs
	 * @param workItemIDs base set of workItemIDs
	 * @return
	 */
	public static List<TWorkItemLinkBean> loadByWorkItems(int[] workItemIDs) {
		return workItemLinkDAO.loadByWorkItems(workItemIDs);
	}



	/**
	 * Load the directly linked workItems
	 * @param predChunk
	 * @param succChunk
	 * @param linkType
	 * @param direction
	 * @return
	 */
	private static List<TWorkItemLinkBean> getLinkedItems(int[] predChunk, int[] succChunk, Integer linkType, Integer direction) {
		return workItemLinkDAO.getLinkedItems(predChunk, succChunk, linkType, direction);
	}

	/**
	 * Get linked items
	 * @param rowChunks
	 * @param columnChunks
	 * @param linkType
	 * @param direction
	 * @return
	 */
	public static List<TWorkItemLinkBean> getLinkedChunks(List<int[]> rowChunks, List<int[]> columnChunks, Integer linkType, Integer direction) {
		List<TWorkItemLinkBean> workItemLinks = new ArrayList<TWorkItemLinkBean>();
		if (rowChunks!=null && !rowChunks.isEmpty() && columnChunks!=null && !columnChunks.isEmpty()) {
			for (int[] rowChunk : rowChunks) {
				for (int[] columnChunk : columnChunks) {
					List<TWorkItemLinkBean> workItemLinkChunk = getLinkedItems(rowChunk, columnChunk, linkType, direction);
					if (workItemLinkChunk!=null) {
						workItemLinks.addAll(workItemLinkChunk);
					}
				}
			}
		}
		return workItemLinks;
	}

	/**
	 * Split the workItemLinksMap in predecessor and successor maps
	 * @param workItemLinksMap
	 * @param successorsForMeAsPredecessor
	 * @param predecessorsForMeAsSuccessor
	 */
	static void getSuccessorsForMeAsPredecessor(Map<Integer, TWorkItemLinkBean> workItemLinksMap,
			SortedMap<Integer, TWorkItemLinkBean> successorsForMeAsPredecessor, SortedMap<Integer, TWorkItemLinkBean> predecessorsForMeAsSuccessor) {
		if (workItemLinksMap!=null) {
			for (Map.Entry<Integer, TWorkItemLinkBean>  entry : workItemLinksMap.entrySet()) {
				Integer sortorder = entry.getKey();
				TWorkItemLinkBean workItemLinkBean = entry.getValue();
				workItemLinkBean.setSortorder(sortorder);
				Integer linkType = workItemLinkBean.getLinkType();
				//Integer linkDirection = workItemLinkBean.getLinkDirection();
				String newLinkTypePluginString = LinkTypeBL.getLinkTypePluginString(linkType);
				ILinkType newLinkTypePlugin = (ILinkType)PluginManager.getInstance().getPluginClass(PluginManager.LINKTYPE_ELEMENT, newLinkTypePluginString);
				int linkTypeDirection = newLinkTypePlugin.getPossibleDirection();
				//boolean isBidirectional = ILinkType.LINK_DIRECTION.BIDIRECTIONAL==newLinkTypePlugin.getPossibleDirection();
				if (linkTypeDirection==LINK_DIRECTION.RIGHT_TO_LEFT) {
					predecessorsForMeAsSuccessor.put(sortorder, workItemLinkBean);
				} else {
					//isBidirectional or left to right
					successorsForMeAsPredecessor.put(sortorder, workItemLinkBean);
				}
			}
		}
	}

	/**
	 * Gets the links to successors from itemID as predecessor
	 * @param itemID
	 * @return
	 */
	static SortedMap<Integer, TWorkItemLinkBean> getSuccessorsForMeAsPredecessorMap(Integer itemID) {
		SortedMap<Integer, TWorkItemLinkBean> itemLinkMap = new TreeMap<Integer, TWorkItemLinkBean>();
		List<TWorkItemLinkBean> linksToSuccessors = workItemLinkDAO.loadByWorkItemPred(itemID);
		if (linksToSuccessors!=null) {
			for (TWorkItemLinkBean workItemLinkBean : linksToSuccessors) {
				itemLinkMap.put(workItemLinkBean.getObjectID(), workItemLinkBean);
			}
		}
		return itemLinkMap;
	}

	/**
	 * Gets the links from predecessors to itemID as successor
	 * @param itemID
	 * @return
	 */
	static SortedMap<Integer, TWorkItemLinkBean> getPredecessorsForMeAsSuccessorMap(Integer itemID) {
		SortedMap<Integer, TWorkItemLinkBean> itemLinkMap = new TreeMap<Integer, TWorkItemLinkBean>();
		List<TWorkItemLinkBean> linksFromPredecessors = workItemLinkDAO.loadByWorkItemSucc(itemID);
		if (linksFromPredecessors!=null) {
			for (TWorkItemLinkBean workItemLinkBean : linksFromPredecessors) {
				itemLinkMap.put(workItemLinkBean.getObjectID(), workItemLinkBean);
			}
		}
		return itemLinkMap;
	}

	public static List<ItemLinkListEntry> getLinks(Integer workItemID, boolean editable, Locale locale){
		SortedMap<Integer, TWorkItemLinkBean> successorsForMeAsPredecessorMap = ItemLinkBL.getSuccessorsForMeAsPredecessorMap(workItemID);
		SortedMap<Integer, TWorkItemLinkBean> predecessorsForMeAsSuccessorMap = ItemLinkBL.getPredecessorsForMeAsSuccessorMap(workItemID);
		return ItemLinkBL.getLinks(successorsForMeAsPredecessorMap, predecessorsForMeAsSuccessorMap, editable, locale, false);
	}

	/**
	 * Merge the predecessor and successor links for a workItem
	 * @return
	 */
	static List<TWorkItemLinkBean> getLinks(Collection<TWorkItemLinkBean> successorsForMeAsPredecessor,
			Collection<TWorkItemLinkBean> predecessorsForMeAsSuccessor){
		Map<Integer, TLinkTypeBean> linkTypeMap = GeneralUtils.createMapFromList(LinkTypeBL.loadAll());
		List<TWorkItemLinkBean> workItemLinkList = new LinkedList<TWorkItemLinkBean>();
		//links from me as predecessor to successors
		if (successorsForMeAsPredecessor!=null && !successorsForMeAsPredecessor.isEmpty()) {
			for (TWorkItemLinkBean workItemLinkBean : successorsForMeAsPredecessor) {
				Integer linkType = workItemLinkBean.getLinkType();
				TLinkTypeBean linkTypeBean = linkTypeMap.get(linkType);
				Integer linkTypeDirection = linkTypeBean.getLinkDirection();
				if (linkTypeBean==null || linkTypeDirection==null ||
						linkTypeDirection.intValue()==LINK_DIRECTION.RIGHT_TO_LEFT) {
					//remove the links of type "right to left". Bidirectional and "left to right" (pred to succ) relations remain
					//for right to left link types the links are visible only from successor item
					continue;
				}
				workItemLinkList.add(workItemLinkBean);
			}
		}
		//links from me as successor to predecessors
		if (predecessorsForMeAsSuccessor!=null && !predecessorsForMeAsSuccessor.isEmpty()) {
			for (TWorkItemLinkBean workItemLinkBean : predecessorsForMeAsSuccessor) {
				Integer linkType = workItemLinkBean.getLinkType();
				TLinkTypeBean linkTypeBean = linkTypeMap.get(linkType);
				Integer linkTypeDirection = linkTypeBean.getLinkDirection();
				if (linkTypeBean==null || linkTypeDirection==null ||
						linkTypeDirection.intValue()==LINK_DIRECTION.LEFT_TO_RIGHT) {
					//remove the links of type "left to right". Bidirectional and "right to left" (pred to succ) relations remain
					//for left to right link types the links are visible only from predecessor item
					continue;
				}
				workItemLinkList.add(workItemLinkBean);
			}
		}
		Collections.sort(workItemLinkList);
		return workItemLinkList;
	}

	/**
	 * Gets the links for a workItem
	 * @param itemID
	 * @return
	 */
	public static List<Integer> getLinkedItemIDs(Integer itemID, Integer linkType, Integer linkDirection) {
		boolean bidirectional=LinkTypeBL.isBidirectional(linkType);
		List<TWorkItemLinkBean> linkList = null;
		List<TWorkItemLinkBean> reverseLinkList = null;
		List<TWorkItemLinkBean> linkAndReverseLinkList = new LinkedList<TWorkItemLinkBean>();
		if (bidirectional) {
			List<Integer> linkedItemIDs = new LinkedList<Integer>();
			if (linkDirection.intValue()==ILinkType.LINK_DIRECTION.UNIDIRECTIONAL_OUTWARD) {
				linkList = loadByPredAndLinkType(itemID, linkType, linkDirection);
				linkAndReverseLinkList.addAll(linkList);
				//linkedItemIDs.addAll(getLinkedItemIDs(linkList, true));
				int reverseDirection = LinkTypeBL.getReverseDirection(linkDirection);
				reverseLinkList = loadBySuccAndLinkType(itemID, linkType, reverseDirection);
				linkAndReverseLinkList.addAll(reverseLinkList);
				//linkedItemIDs.addAll(getLinkedItemIDs(reverseLinkList, false));
			} else {
				if (linkDirection.intValue()==ILinkType.LINK_DIRECTION.UNIDIRECTIONAL_INWARD) {
					reverseLinkList = loadByPredAndLinkType(itemID,  linkType, linkDirection);
					linkAndReverseLinkList.addAll(reverseLinkList);
					//linkedItemIDs.addAll(getLinkedItemIDs(reverseLinkList, true));
					int reverseDirection = LinkTypeBL.getReverseDirection(linkDirection);
					linkList = loadBySuccAndLinkType(itemID, linkType, reverseDirection);
					linkAndReverseLinkList.addAll(linkList);
					//linkedItemIDs.addAll(getLinkedItemIDs(linkList, false));
				}
			}
			//merge the the links from two directions together by sort order
			Collections.sort(linkAndReverseLinkList);
			for (TWorkItemLinkBean workItemLinkBean : linkAndReverseLinkList) {
				Integer succ = workItemLinkBean.getLinkSucc();
				Integer pred = workItemLinkBean.getLinkPred();
				if (succ!=null && pred!=null) {
					if (!succ.equals(itemID)) {
						linkedItemIDs.add(succ);
					} else {
						if (!pred.equals(itemID)) {
							linkedItemIDs.add(pred);
						}
					}
				}
			}
			return linkedItemIDs;
		} else {
			if (linkDirection.intValue()==ILinkType.LINK_DIRECTION.UNIDIRECTIONAL_OUTWARD) {
				linkList = loadByPredAndLinkType(itemID, linkType, linkDirection);
				return getLinkedItemIDs(linkList, true);
			} else {
				if (linkDirection.intValue()==ILinkType.LINK_DIRECTION.UNIDIRECTIONAL_INWARD) {
					linkList = loadBySuccAndLinkType(itemID, linkType, linkDirection);
					return getLinkedItemIDs(linkList, false);
				}
			}
		}
		return null;
	}

	/**
	 * Gather the set of linked itemIDs
	 * @param linkList
	 * @param getSuccessor
	 * @return
	 */
	private static List<Integer> getLinkedItemIDs(List<TWorkItemLinkBean> linkList, boolean getSuccessor) {
		List<Integer> linkedItemSet = new LinkedList<Integer>();
		if (linkList!=null && !linkList.isEmpty()){
			for	(TWorkItemLinkBean workItemLinkBean:linkList) {
				Integer linkedItemID = null;
				if (getSuccessor) {
					linkedItemID = workItemLinkBean.getLinkSucc();
				} else {
					linkedItemID = workItemLinkBean.getLinkPred();
				}
				if (linkedItemID!=null) {
					linkedItemSet.add(linkedItemID);
				}
			}
		}
		return linkedItemSet;
	}

	/**
	 * Gets the links for a workItem
	 * @param itemID
	 * @return
	 */
	static Set<Integer> getLinkedItemIDs(Integer itemID) {
		List<TWorkItemLinkBean> successorsForMeAsPredecessorMap = getSuccessorsForMeAsPredecessor(itemID);
		List<TWorkItemLinkBean> predecessorsForMeAsSuccessorMap = getPredecessorsForMeAsSuccessor(itemID);
		Map<Integer, TLinkTypeBean> linkTypeMap = GeneralUtils.createMapFromList(LinkTypeBL.loadAll());
		Set<Integer> linkedItemIDs = new HashSet<Integer>();
		//links from me as predecessor to successors
		if (successorsForMeAsPredecessorMap!=null && !successorsForMeAsPredecessorMap.isEmpty()) {
			for (TWorkItemLinkBean workItemLinkBean : successorsForMeAsPredecessorMap) {
				Integer linkType = workItemLinkBean.getLinkType();
				TLinkTypeBean linkTypeBean = linkTypeMap.get(linkType);
				Integer linkTypeDirection = linkTypeBean.getLinkDirection();
				if (linkTypeBean==null || linkTypeDirection==null ||
						linkTypeDirection.intValue()==LINK_DIRECTION.RIGHT_TO_LEFT) {
					//remove the links of type "right to left". Bidirectional and "left to right" (pred to succ) relations remain
					//for right to left link types the links are visible only from successor item
					continue;
				}
				Integer succesorItemID = workItemLinkBean.getLinkSucc();
				linkedItemIDs.add(succesorItemID);
			}
		}
		//links from me as successor to predecessors
		if (predecessorsForMeAsSuccessorMap!=null && !predecessorsForMeAsSuccessorMap.isEmpty()) {
			for (TWorkItemLinkBean workItemLinkBean : predecessorsForMeAsSuccessorMap) {
				Integer linkType = workItemLinkBean.getLinkType();
				TLinkTypeBean linkTypeBean = linkTypeMap.get(linkType);
				Integer linkTypeDirection = linkTypeBean.getLinkDirection();
				if (linkTypeBean==null || linkTypeDirection==null ||
						linkTypeDirection.intValue()==LINK_DIRECTION.LEFT_TO_RIGHT) {
					//remove the links of type "left to right". Bidirectional and "right to left" (pred to succ) relations remain
					//for left to right link types the links are visible only from predecessor item
					continue;
				}
				Integer predecessorItemID = workItemLinkBean.getLinkPred();
				linkedItemIDs.add(predecessorItemID);
			}
		}
		return linkedItemIDs;
	}

	/**
	 * Gets the links for a workItem
	 * @return
	 */
	static List<ItemLinkListEntry> getLinks(SortedMap<Integer, TWorkItemLinkBean> successorsForMeAsPredecessorMap,
			SortedMap<Integer, TWorkItemLinkBean> predecessorsForMeAsSuccessorMap, boolean editable, Locale locale, boolean newItem) {
		Map<Integer, TLinkTypeBean> linkTypeMap = GeneralUtils.createMapFromList(LinkTypeBL.loadAll());
		List<ItemLinkListEntry> itemLinkList = new ArrayList<ItemLinkListEntry>();
		//links from me as predecessor to successors
		if (successorsForMeAsPredecessorMap!=null && !successorsForMeAsPredecessorMap.isEmpty()) {
			Set<Integer> successorItemIDs = new HashSet<Integer>();
			for (TWorkItemLinkBean workItemLinkBean : successorsForMeAsPredecessorMap.values()) {
				successorItemIDs.add(workItemLinkBean.getLinkSucc());
			}
			List<TWorkItemBean> successorItems=ItemBL.loadByWorkItemKeys(GeneralUtils.createIntArrFromSet(successorItemIDs));
			Map<Integer, TWorkItemBean> workItemsMap = GeneralUtils.createMapFromList(successorItems);
			for (Map.Entry<Integer, TWorkItemLinkBean> entry : successorsForMeAsPredecessorMap.entrySet()) {
				TWorkItemLinkBean workItemLinkBean = entry.getValue();
				Integer linkID = null;
				if (newItem) {
					//the sort order from the map
					linkID = entry.getKey();
				} else {
					//the saved linkID
					linkID = workItemLinkBean.getObjectID();
				}
				Integer linkType = workItemLinkBean.getLinkType();
				TLinkTypeBean linkTypeBean = linkTypeMap.get(linkType);
				Integer linkTypeDirection = linkTypeBean.getLinkDirection();
				if (linkTypeBean==null || linkTypeDirection==null ||
						linkTypeDirection.intValue()==LINK_DIRECTION.RIGHT_TO_LEFT) {
					//remove the links of type "right to left". Bidirectional and "left to right" (pred to succ) relations remain
					//for right to left link types the links are visible only from successor item
					continue;
				}
				Integer succesorItemID = workItemLinkBean.getLinkSucc();
				TWorkItemBean workItemBean = workItemsMap.get(succesorItemID);
				ItemLinkListEntry itemLinkListEntry = new ItemLinkListEntry();
				itemLinkListEntry.setSortOrder(workItemLinkBean.getSortorder());
				itemLinkListEntry.setLinkType(linkType);
				itemLinkListEntry.setLinkDirection(linkTypeDirection);
				itemLinkListEntry.setLinkedWorkItemID(succesorItemID);
				if (workItemLinkBean!=null) {
					itemLinkListEntry.setLinkedWorkItemTitle(workItemBean.getSynopsis());
				}
				itemLinkListEntry.setDescription(workItemLinkBean.getDescription());
				itemLinkListEntry.setLinkID(linkID);
				itemLinkListEntry.setLinkTypeName(LinkTypeBL.getLinkTypeName(linkTypeBean, workItemLinkBean.getLinkDirection(), locale));
				TStateBean stateBean = LookupContainer.getStatusBean(workItemBean.getStateID(), locale);
				if (stateBean!=null) {
					itemLinkListEntry.setStateLabel(stateBean.getLabel());
				}
				ILabelBean responsiblePerson = LookupContainer.getPersonBean(workItemBean.getResponsibleID());
				if (responsiblePerson!=null) {
					itemLinkListEntry.setResponsibleLabel(responsiblePerson.getLabel());
				}
				itemLinkListEntry.setLastEdit(workItemLinkBean.getLastEdit());
				boolean isInline = false;
				ILinkType linkTypeInstance = LinkTypeBL.getLinkTypePluginInstanceByLinkTypeKey(linkType);
				if (linkTypeInstance!=null) {
					itemLinkListEntry.setParameters(linkTypeInstance.prepareParametersOnLinkTab(workItemLinkBean, linkTypeDirection, locale));
					itemLinkListEntry.setParameterMap(linkTypeInstance.prepareParametersMap(workItemLinkBean));
					isInline = linkTypeInstance.isInline();
				}
				itemLinkListEntry.setEditable(editable && !isInline);
				itemLinkList.add(itemLinkListEntry);
			}
		}
		//links from me as successor to predecessors
		if (predecessorsForMeAsSuccessorMap!=null && !predecessorsForMeAsSuccessorMap.isEmpty()) {
			Set<Integer> predecessorItemIDs = new HashSet<Integer>();
			for (TWorkItemLinkBean workItemLinkBean : predecessorsForMeAsSuccessorMap.values()) {
				predecessorItemIDs.add(workItemLinkBean.getLinkPred());
			}
			List<TWorkItemBean> predecessorItems=ItemBL.loadByWorkItemKeys(GeneralUtils.createIntArrFromSet(predecessorItemIDs));
			Map<Integer, TWorkItemBean> workItemsMap = GeneralUtils.createMapFromList(predecessorItems);
			for (Map.Entry<Integer, TWorkItemLinkBean> entry : predecessorsForMeAsSuccessorMap.entrySet()) {
				TWorkItemLinkBean workItemLinkBean = entry.getValue();
				Integer linkID = null;
				if (newItem) {
					//the sort order from the map
					linkID = entry.getKey();
				} else {
					//the saved linkID
					linkID = workItemLinkBean.getObjectID();
				}
				Integer linkType = workItemLinkBean.getLinkType();
				TLinkTypeBean linkTypeBean = linkTypeMap.get(linkType);
				Integer linkTypeDirection = linkTypeBean.getLinkDirection();
				if (linkTypeBean==null || linkTypeDirection==null ||
						linkTypeDirection.intValue()==LINK_DIRECTION.LEFT_TO_RIGHT) {
					//remove the links of type "left to right". Bidirectional and "right to left" (pred to succ) relations remain
					//for left to right link types the links are visible only from predecessor item
					continue;
				}
				if (linkTypeDirection.intValue()==LINK_DIRECTION.BIDIRECTIONAL) {
					linkTypeDirection = LinkTypeBL.getReverseDirection(workItemLinkBean.getLinkDirection());
				}
				Integer predecessorItemID = workItemLinkBean.getLinkPred();
				TWorkItemBean workItemBean = workItemsMap.get(predecessorItemID);
				ItemLinkListEntry itemLinkListEntry = new ItemLinkListEntry();
				itemLinkListEntry.setSortOrder(workItemLinkBean.getSortorder());
				itemLinkListEntry.setLinkType(linkType);
				itemLinkListEntry.setLinkDirection(linkTypeDirection);
				itemLinkListEntry.setLinkedWorkItemID(predecessorItemID);
				if (workItemLinkBean!=null) {
					itemLinkListEntry.setLinkedWorkItemTitle(workItemBean.getSynopsis());
				}
				itemLinkListEntry.setDescription(workItemLinkBean.getDescription());
				itemLinkListEntry.setLinkID(linkID);
				itemLinkListEntry.setLinkTypeName(LinkTypeBL.getLinkTypeName(linkTypeBean,  linkTypeDirection, locale));
				TStateBean stateBean = LookupContainer.getStatusBean(workItemBean.getStateID(), locale);
				if (stateBean!=null) {
					itemLinkListEntry.setStateLabel(stateBean.getLabel());
				}
				ILabelBean responsiblePerson = LookupContainer.getPersonBean(workItemBean.getResponsibleID());
				if (responsiblePerson!=null) {
					itemLinkListEntry.setResponsibleLabel(responsiblePerson.getLabel());
				}
				itemLinkListEntry.setLastEdit(workItemLinkBean.getLastEdit());
				ILinkType linkTypeInstance = LinkTypeBL.getLinkTypePluginInstanceByLinkTypeKey(linkType);
				boolean isInline = false;
				if (linkTypeInstance!=null) {
					itemLinkListEntry.setParameters(linkTypeInstance.prepareParametersOnLinkTab(workItemLinkBean, linkTypeDirection, locale));
					itemLinkListEntry.setParameterMap(linkTypeInstance.prepareParametersMap(workItemLinkBean));
					isInline = linkTypeInstance.isInline();
				}
				itemLinkListEntry.setEditable(editable && !isInline);
				itemLinkList.add(itemLinkListEntry);
			}
		}
		Collections.sort(itemLinkList);
		return itemLinkList;
	}

	/**
	 * Saves a workItemLink in db
	 * @param workItemLinkBean
	 * @return
	 */
	public static Integer saveLink(TWorkItemLinkBean workItemLinkBean){
		workItemLinkBean.setLastEdit(new Date());
		boolean isNew = workItemLinkBean.getObjectID()==null;
		Integer linkID = workItemLinkDAO.save(workItemLinkBean);
		LinkIndexer.getInstance().addToIndex(workItemLinkBean, isNew);
		//possible lucene update in other cluster nodes
		ClusterMarkChangesBL.markDirtyLinkInCluster(linkID, ClusterMarkChangesBL.getChangeTypeByAddOrUpdateIndex(isNew));
		return linkID;
	}

	/**
	 * Saves the link in workItemContext
	 * @param workItemContext
	 * @param workItemLinkBean
	 */
	static void saveLinkInContext(WorkItemContext workItemContext, TWorkItemLinkBean workItemLinkBean, Integer linkID) {
		if (workItemContext!=null) {
			SortedMap<Integer, TWorkItemLinkBean> workItemsLinksMap = workItemContext.getWorkItemsLinksMap();
			Integer localLinkID = null;
			if (workItemsLinksMap==null) {
				//first link to the new item
				workItemsLinksMap = new TreeMap<Integer, TWorkItemLinkBean>();
				workItemContext.setWorkItemsLinksMap(workItemsLinksMap);
				localLinkID = Integer.valueOf(1);
			} else {
				if (linkID==null) {
				//get the highest generated linkID plus one
					for (Integer usedLinkID : workItemsLinksMap.keySet()) {
						if (localLinkID == null) {
							localLinkID = usedLinkID;
						} else {
							if (localLinkID < usedLinkID) {
								localLinkID = usedLinkID;
							}
						}
					}
					if(localLinkID == null) {
						localLinkID = Integer.valueOf(1);
					}else {
						localLinkID = Integer.valueOf(localLinkID.intValue()+1);
					}
				} else {
					localLinkID = linkID;
				}
			}
			workItemsLinksMap.put(localLinkID, workItemLinkBean);
		}
	}

	/**
	 * Saves the links from workItemContext into the database
	 * @param workItemsLinksMap
	 */
	public static void saveAllFromSessionToDb(Integer workItemID, Map<Integer, TWorkItemLinkBean> workItemsLinksMap) {
		if (workItemsLinksMap!=null) {
			int i = 0;
			for (TWorkItemLinkBean workItemLinkBean : workItemsLinksMap.values()) {
				Integer predecessor = workItemLinkBean.getLinkPred();
				Integer successor = workItemLinkBean.getLinkSucc();
				if (predecessor==null ^ successor==null) {
					//only one can be non null
					if (predecessor==null) {
						workItemLinkBean.setLinkPred(workItemID);
					} else {
						workItemLinkBean.setLinkSucc(workItemID);
					}
					i++;
					workItemLinkBean.setSortorder(i);
					saveLink(workItemLinkBean);
				}
			}
		}
	}

	public static void deleteLink(Integer linkID) {
		workItemLinkDAO.delete(linkID);
		LinkIndexer.getInstance().deleteByKey(linkID);
		//possible lucene update in other cluster nodes
		ClusterMarkChangesBL.markDirtyLinkInCluster(linkID, CHANGE_TYPE.DELETE_FROM_INDEX);
	}

	/**
	 * Gets the link data for a link
	 * @param workItemID
	 * @param workItemLinkBean
	 * @return
	 */
	static ItemLinkTO getLinkData(Integer workItemID, TWorkItemLinkBean workItemLinkBean) {
		ItemLinkTO itemLinkTO = new ItemLinkTO();
		if (workItemLinkBean!=null) {
			Integer linkPred = workItemLinkBean.getLinkPred();
			Integer linkSucc = workItemLinkBean.getLinkSucc();
			Integer linkDirection = workItemLinkBean.getLinkDirection();
			//Integer direction = linkDirection;
			//1. current item is linkPred: the link is visible from linkPred as current item if link is bidirectional or or UNIDIRECTIONAL_OUTWARD
			if (linkSucc!=null && (workItemID==null || //for new item
					workItemID.equals(linkPred))) { //for existing item
				//the edited item  (new or existing) is predecessor: load the the successor linked item details
				itemLinkTO.setLinkedWorkItemID(linkSucc);
				try {
					TWorkItemBean linkedWorkItemBean = workItemDAO.loadByPrimaryKey(linkSucc);
					if (linkedWorkItemBean!=null) {
						itemLinkTO.setLinkedWorkItemTitle(linkedWorkItemBean.getSynopsis());
					}
				} catch (ItemLoaderException e) {
					LOGGER.error("Loading the linkSucc failed with " + e.getMessage());
				}

			} else {
				//2. current item is linkSucc: the link is visible from linkSucc as current item if  link is bidirectional (in this case the reverse direction)
				//or UNIDIRECTIONAL_INWARD: the successor sees the predecessor but not vice versa
				if (linkPred!=null && (workItemID==null || workItemID.equals(linkSucc))) {
					//the edited item is successor: load the predecessor item details
					boolean isBidirectional = false;
					String linkTypePluginClass = LinkTypeBL.getLinkTypePluginString(workItemLinkBean.getLinkType());
					if (linkTypePluginClass!=null) {
						ILinkType linkType = (ILinkType)PluginManager.getInstance().getPluginClass(PluginManager.LINKTYPE_ELEMENT, linkTypePluginClass);
						if (linkType!=null) {
							isBidirectional = ILinkType.LINK_DIRECTION.BIDIRECTIONAL==linkType.getPossibleDirection();
						}
					}
					if (isBidirectional) {
						//by bidirectional links if the edited item is the successor the saved direction (which was saved when the edited item was considered predecessor) should be inverted.
						//by save it will not  be inverted, only for correct rendering
						linkDirection = LinkTypeBL.getReverseDirection(linkDirection);
					}
					itemLinkTO.setLinkedWorkItemID(linkPred);
					try {
						TWorkItemBean linkedWorkItemBean = workItemDAO.loadByPrimaryKey(linkPred);
						if (linkedWorkItemBean!=null) {
							itemLinkTO.setLinkedWorkItemTitle(linkedWorkItemBean.getSynopsis());
						}
					} catch (ItemLoaderException e) {
						LOGGER.error("Loading the linkPred failed with " + e.getMessage());
					}

				}
			}
			//itemLinkTO.setLinkTypeWithDirection(MergeUtil.mergeKey(workItemLinkBean.getLinkType(), direction));
			itemLinkTO.setLinkType(workItemLinkBean.getLinkType());
			itemLinkTO.setLinkDirection(linkDirection);
			itemLinkTO.setDescription(workItemLinkBean.getDescription());

			ILinkType linkTypeInstance = LinkTypeBL.getLinkTypePluginInstanceByLinkTypeKey(workItemLinkBean.getLinkType());
			if (linkTypeInstance!=null) {
				itemLinkTO.setParameterMap(linkTypeInstance.prepareParametersMap(workItemLinkBean));
			}
		}
		return itemLinkTO;
	}

	/**
	 * Load the directly linked workItemLinkBeans for a list of workItemIDs:
	 * no matter whether workItemIDs are linkPred or linkSucc but the direction is the same
	 * Used only for unidirectional link types
	 * @param workItemIDs
	 * @param linkTypes
	 * @param direction
	 * @return
	 */
	public static List<TWorkItemLinkBean> loadUnidirectionalLinksByWorkItemsAndLinkTypes(List<Integer> workItemIDs,
			List<Integer> linkTypes, Integer direction) {
		List<TWorkItemLinkBean> workItemLinkBeans = new ArrayList<TWorkItemLinkBean>();
		if (workItemIDs==null || workItemIDs.isEmpty()) {
			return workItemLinkBeans;
		}
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList==null) {
			return workItemLinkBeans;
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = iterator.next();
			workItemLinkBeans.addAll(
					workItemLinkDAO.getWorkItemsOfDirection(
							workItemIDChunk, linkTypes, false, direction, null, null));
		}
		return workItemLinkBeans;
	}

	/**
	 * Load all linked workItems for a list of workItemIDs
	 * @param workItemIDs base set of workItemIDs
	 * @param linkTypes
	 * @param direction the link direction
	 * @param isBidirectional whether linkType is bidirectional
	 * @return
	 */
	public static Map<Integer, SortedSet<Integer>> loadByWorkItemsAndLinkType(List<Integer> workItemIDs,
			List<Integer> linkTypes, Integer direction, boolean isBidirectional, Integer archived, Integer deleted) {
		Map<Integer, SortedSet<Integer>> workItemLinksMap = new HashMap<Integer, SortedSet<Integer>>();
		if (workItemIDs==null || workItemIDs.isEmpty()) {
			return workItemLinksMap;
		}
		Integer reverseDirection = null;
		if (isBidirectional && direction!=null) {
			reverseDirection = LinkTypeBL.getReverseDirection(direction);
			if (direction.equals(reverseDirection)) {
				reverseDirection = null;
			}
		}
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList==null) {
			return workItemLinksMap;
		}
		Set<Integer> checkedWorkItems = new HashSet<Integer>();
		int i = 0;
		int originalSize = workItemIDChunksList.size();
		while (i < originalSize) {
			int[] workItemIDChunk = workItemIDChunksList.get(i);
			List<Integer> toLoad = new ArrayList<Integer>();
			for (Integer workItemID : workItemIDChunk) {
				if (!checkedWorkItems.contains(workItemID)) {
					checkedWorkItems.add(workItemID);
					toLoad.add(workItemID);
				}
			}
			if (toLoad.size() > 0) {
				int[] toLoadArray = GeneralUtils.createIntArrFromIntegerCollection(toLoad);
				if (isBidirectional) {
					Map<Integer, SortedSet<Integer>> succWorkItemLinksMap = getSuccOrPredLinkedWorkItemIDMaps(
							workItemLinkDAO.getWorkItemsOfDirection(
									toLoadArray, linkTypes, true, direction, archived, deleted), true);
					addChuck(workItemLinksMap, succWorkItemLinksMap);
					Set<Integer>newItemsToLoad = new HashSet<Integer>();
					for (Integer key : succWorkItemLinksMap.keySet()) {
						newItemsToLoad.addAll(succWorkItemLinksMap.get(key));
					}
					if (reverseDirection!=null) {
						Map<Integer, SortedSet<Integer>> predWorkItemLinksMap = getSuccOrPredLinkedWorkItemIDMaps(
							workItemLinkDAO.getWorkItemsOfDirection(
									toLoadArray, linkTypes, false, reverseDirection, archived, deleted), false);
						addChuck(workItemLinksMap, predWorkItemLinksMap);
						for (Integer key : predWorkItemLinksMap.keySet()) {
							newItemsToLoad.addAll(predWorkItemLinksMap.get(key));
						}
					}
					if (newItemsToLoad.size() > 0) {
						List<int[]> chunkList = GeneralUtils.getListOfChunks(newItemsToLoad);
						workItemIDChunksList.addAll(chunkList);// GeneralUtils.createIntArrFromIntegerCollection(newItemsToLoad));
						originalSize += chunkList.size();
					}
				} else {
					Map<Integer, SortedSet<Integer>> predWorkItemLinksMap = getSuccOrPredLinkedWorkItemIDMaps(
							workItemLinkDAO.getWorkItemsOfDirection(
									toLoadArray, linkTypes, false, direction, archived, deleted), false);
					addChuck(workItemLinksMap, predWorkItemLinksMap);
					Set<Integer>newItemsToLoad = new HashSet<Integer>();
					for (Integer key : predWorkItemLinksMap.keySet()) {
						newItemsToLoad.addAll(predWorkItemLinksMap.get(key));
					}
					if (newItemsToLoad.size() > 0) {
						List<int[]> chunkList = GeneralUtils.getListOfChunks(newItemsToLoad);
						workItemIDChunksList.addAll(chunkList);//GeneralUtils.createIntArrFromIntegerCollection(newItemsToLoad)
						originalSize += chunkList.size();
					}
				}
			}
			i++;
		}
		return workItemLinksMap;
	}

	private static Map<Integer, SortedSet<Integer>> getSuccOrPredLinkedWorkItemIDMaps(List<TWorkItemLinkBean> workItemLinksList, boolean predToSucc) {
		Map<Integer, SortedSet<Integer>> workItemIDsMap = new HashMap<Integer, SortedSet<Integer>>();
		Iterator<TWorkItemLinkBean> iterator = workItemLinksList.iterator();
		while (iterator.hasNext()) {
			TWorkItemLinkBean workItemLinkBean = iterator.next();
			Integer linkPred = workItemLinkBean.getLinkPred();
			Integer linkSucc = workItemLinkBean.getLinkSucc();
			Integer key = null;
			Integer value = null;
			if (predToSucc) {
				key = linkPred;
				value = linkSucc;
			} else {
				key = linkSucc;
				value = linkPred;
			}
			SortedSet<Integer> linkSet = workItemIDsMap.get(key);
			if (linkSet==null) {
				linkSet = new TreeSet<Integer>();
				workItemIDsMap.put(key, linkSet);
			}
			linkSet.add(value);
		}
		return workItemIDsMap;
	}


	/**
	 * Add processed chunk data to final map
	 * @param finalMap
	 * @param chunkMap
	 */
	private static void addChuck(Map<Integer, SortedSet<Integer>> finalMap, Map<Integer, SortedSet<Integer>> chunkMap) {
		if (chunkMap!=null) {
			for (Map.Entry<Integer, SortedSet<Integer>> chunkEntry : chunkMap.entrySet()) {
				Integer baseWorkItemID = chunkEntry.getKey();
				SortedSet<Integer> linkedValueIDs = chunkEntry.getValue();
				SortedSet<Integer> finalValues = finalMap.get(baseWorkItemID);
				if (finalValues==null) {
					finalValues =  new TreeSet<Integer>();
					finalMap.put(baseWorkItemID, finalValues);
				}
				finalValues.addAll(linkedValueIDs);
			}
		}
	}

	/**
	 * Whether a linkSucc is descendant of linkPred for an unidirectional link
	 * @param linkPred
	 * @param linkSucc
	 * @param direction
	 * @return
	 */
	public static boolean isDescendent(Integer linkPred, Integer linkSucc, Integer direction, ILinkType iLinkType) {
		if (EqualUtils.equal(linkPred, linkSucc)) {
			return true;
		}
		List<Integer> linkTypeIDs = LinkTypeBL.getLinkTypesByPluginClass(iLinkType);
		List<Integer> workItemIDsFromLevel = new ArrayList<Integer>();
		workItemIDsFromLevel.add(linkPred);
		Set<Integer> linkPredSet = new HashSet<Integer>();
		while (!workItemIDsFromLevel.isEmpty()) {
			Map<Integer, SortedSet<Integer>> predToSuccLinkedWorkItemsMap =
				loadByWorkItemsAndLinkType(workItemIDsFromLevel, linkTypeIDs, direction, false, null, null);
			workItemIDsFromLevel = new ArrayList<Integer>();
			Iterator<Integer> itrSuccLinkedWorkItemsSet = predToSuccLinkedWorkItemsMap.keySet().iterator();
			while (itrSuccLinkedWorkItemsSet.hasNext()) {
				Integer crtLinkPred = itrSuccLinkedWorkItemsSet.next();
				//gather the predecessors from all levels
				linkPredSet.add(crtLinkPred);
				SortedSet<Integer> succLinkedWorkItemsSet = predToSuccLinkedWorkItemsMap.get(crtLinkPred);
				if (succLinkedWorkItemsSet.contains(linkSucc)) {
					return true;
				}
				//remove those successors which were already predecessors in a previous level
				//if the data is consistent this will never remove any entry,
				//but if data is not consistent not removing the entry it would lead to infinite cycle
				succLinkedWorkItemsSet.removeAll(linkPredSet);
				workItemIDsFromLevel.addAll(succLinkedWorkItemsSet);
			}
		}
		return false;
	}


	/**
	 * Prepare add a link for save or add the specific error
	 * @param linkPred
	 * @param linkSucc
	 * @param linkTypeID
	 * @param linkDirection
	 * @param description
	 * @param linkType
	 * @param parametersMap
	 * @param personBean
	 * @param locale
	 * @param linksToAdd output parameter
	 * @param errorMap output parameter
	 * @param specificParameterErrors output parameter
	 */
	public static void prepareLink(Integer linkPred, Integer linkSucc, Integer linkTypeID, Integer linkDirection, String description,
			ILinkType linkType, Map<String, String> parametersMap, TPersonBean personBean, Locale locale,
			List<TWorkItemLinkBean> linksToAdd, Map<String, List<LabelValueBean>> errorMap, List<LabelValueBean> specificParameterErrors) {
		if (linkPred!=null && linkSucc!=null) {
			TWorkItemLinkBean workItemLinkBean = new TWorkItemLinkBean();
			workItemLinkBean.setLinkType(linkTypeID);
			workItemLinkBean.setLinkDirection(linkDirection);
			workItemLinkBean.setLinkPred(linkPred);
			workItemLinkBean.setLinkSucc(linkSucc);
			workItemLinkBean.setDescription(description);
			List<ErrorData> validationErrorList = new LinkedList<ErrorData>();
			List<ErrorData> unwrapList = linkType.unwrapParametersBeforeSave(parametersMap, workItemLinkBean, personBean, locale);
			if (unwrapList!=null && !unwrapList.isEmpty()) {
				validationErrorList.addAll(unwrapList);
			}
			TWorkItemBean predWorkItem = null;
			if (linkPred!=null) {
				try {
					predWorkItem = ItemBL.loadWorkItem(linkPred);
				} catch (ItemLoaderException e) {
				}
			}
			TWorkItemBean succWorkItem = null;
			if (linkSucc!=null) {
				try {
					succWorkItem = ItemBL.loadWorkItem(linkSucc);
				} catch (ItemLoaderException e) {
				}
			}
			List<ErrorData> validationList = linkType.validateBeforeSave(workItemLinkBean, null, null, predWorkItem, succWorkItem, personBean, locale);
			if (validationList!=null && !validationList.isEmpty()) {
				validationErrorList.addAll(validationList);
			}
			if (!validationErrorList.isEmpty()) {
				for (ErrorData errorData : validationErrorList) {
					String fieldName = errorData.getFieldName();
					if (fieldName==null) {
						//wrong linked workItem
						String resourceKey = errorData.getResourceKey();
						List<LabelValueBean> itemPairList = errorMap.get(resourceKey);
						if (itemPairList==null) {
							itemPairList = new LinkedList<LabelValueBean>();
							errorMap.put(resourceKey, itemPairList);
						}
						String predItemID = "";
						String succItemID = "";
						if (ApplicationBean.getInstance().getSiteBean().getProjectSpecificIDsOn()) {
							Integer fieldID =SystemFields.INTEGER_PROJECT_SPECIFIC_ISSUENO;
							IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
							Integer predIDNumber = null;
							if (predWorkItem!=null) {
								predIDNumber = predWorkItem.getIDNumber();
							}
							Integer succIDNumber = null;
							if (succWorkItem!=null) {
								succIDNumber = succWorkItem.getIDNumber();
							}
							if (predIDNumber!=null) {
								predItemID = fieldTypeRT.getLuceneValue(predIDNumber, predWorkItem);
							}
							if (succIDNumber!=null) {
								succItemID = fieldTypeRT.getLuceneValue(succIDNumber, succWorkItem);
							}
						} else {
							predItemID = linkPred.toString();
							succItemID = linkSucc.toString();
						}
						itemPairList.add(new LabelValueBean(predItemID, succItemID));
					} else {
						//problem with a specific parameter from parametersMap
						specificParameterErrors.add(new LabelValueBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
								errorData.getResourceKey(), locale), fieldName));
					}
				}
			} else {
				linksToAdd.add(workItemLinkBean);
			}
		}
	}

	public static String getGanttCreatedLinkJSONResponse(TWorkItemLinkBean createdWorkItemLinkBean, List<ReportBean>reportBeans,
			Integer createdLinkID,  Double createdLinkLag) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, "success", true);
		Double convertedLinkLag = null;
		if(createdWorkItemLinkBean != null) {
			TWorkItemBean succWorkItemBean = null;
			for(ReportBean oneReportBean : reportBeans) {
				if(oneReportBean.getWorkItemBean().getObjectID().equals(createdWorkItemLinkBean.getLinkSucc())) {
					succWorkItemBean = oneReportBean.getWorkItemBean();
				}
			}
			if(succWorkItemBean == null) {
				try {
					succWorkItemBean = ItemBL.loadWorkItem(createdWorkItemLinkBean.getLinkSucc());
				}catch(ItemLoaderException ilEx) {
					LOGGER.error("Loading the work item with following ID failed: " + createdWorkItemLinkBean.getLinkSucc());
					LOGGER.error(ExceptionUtils.getFullStackTrace(ilEx));
				}
			}
			Double hoursPerWorkday = ProjectBL.getHoursPerWorkingDay(succWorkItemBean.getProjectID());
			convertedLinkLag = LinkLagBL.getUILinkLagFromMinutes(createdLinkLag, createdWorkItemLinkBean.getLinkLagFormat(), hoursPerWorkday);
		}
		if(convertedLinkLag == null) {
			convertedLinkLag = createdLinkLag / 4800;
		}

		JSONUtility.appendDoubleValue(sb, "createdLinkLag", convertedLinkLag);
		JSONUtility.appendIntegerValue(sb, "createdLinkID", createdLinkID, true);
		sb.append("}");
		return sb.toString();
	}

}
