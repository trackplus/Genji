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

package com.aurel.track.admin.customize.category.filter.execute.loadItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aurel.track.beans.TLinkTypeBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.runtime.system.text.SystemProjectSpecificIssueNoRT;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.link.ItemLinkBL;
import com.aurel.track.linkType.ILinkType;
import com.aurel.track.linkType.ILinkType.LINK_DIRECTION;
import com.aurel.track.linkType.ILinkType.PARENT_CHILD_EXPRESSION;
import com.aurel.track.linkType.LinkTypeBL;
import com.aurel.track.linkType.MsProjectLinkType;
import com.aurel.track.plugin.PluginManager;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanLink;
import com.aurel.track.util.GeneralUtils;

public class LoadItemLinksUtil {
	private static final Logger LOGGER = LogManager.getLogger(LoadItemLinksUtil.class);
    private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
   
    private LoadItemLinksUtil(){
    }

    /**
     * Add the linked items to reportbeans
     * set also the show value for linked items
     * @param reportBeansList
     * @param personID
     * @param locale
     * @return
     */
    public static List<ReportBean> addAllLinkedWorkItemsToReportBeans(List<ReportBean> reportBeansList, List<TWorkItemLinkBean> itemLinkBeans, Integer personID, Locale locale) {
        if (reportBeansList!=null) {
            boolean  useProjectSpecificID=false;
            ApplicationBean appBean = ApplicationBean.getInstance();
            if(appBean.getSiteBean().getProjectSpecificIDsOn()!=null){
                useProjectSpecificID=appBean.getSiteBean().getProjectSpecificIDsOn().booleanValue();
            }
            List<TWorkItemBean> itemBeanList = new LinkedList<TWorkItemBean>();
            if (reportBeansList!=null) {
                for (ReportBean reportBean : reportBeansList) {
                    TWorkItemBean workItemBean = reportBean.getWorkItemBean();
                    itemBeanList.add(workItemBean);
                }
            }
            Map<Integer, SortedSet<ReportBeanLink>> ganntMSProjectReportBeanLinksMap = new HashMap<Integer, SortedSet<ReportBeanLink>>();
            Map<Integer, SortedSet<ReportBeanLink>> allLinkedWorkItemsMap = getAllLinkedWorkItems(itemBeanList, itemLinkBeans, personID, locale, ganntMSProjectReportBeanLinksMap);
            for (ReportBean reportBean : reportBeansList) {
                TWorkItemBean workItemBean = reportBean.getWorkItemBean();
                Integer workItemID = workItemBean.getObjectID();
                SortedSet<ReportBeanLink> reportBeanLinksSet = allLinkedWorkItemsMap.get(workItemID);
                if (reportBeanLinksSet!=null) {
                    reportBean.setReportBeanLinksSet(reportBeanLinksSet);
                    //set also the show value
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Iterator<ReportBeanLink> iterator = reportBeanLinksSet.iterator(); iterator.hasNext();) {
                        ReportBeanLink reportBeanLink = iterator.next();
                        String showLink = null;
                        if (useProjectSpecificID) {
                            showLink = reportBeanLink.getProjectSpecificIssueNo();
                        } else {
                            showLink = reportBeanLink.getWorkItemID().toString();
                        }
                        stringBuilder.append(showLink);
                        if (iterator.hasNext()) {
                            stringBuilder.append(" | ");
                        }
                    }
                    Map<Integer, String> showValuesMap = reportBean.getShowValuesMap();
                    if (showValuesMap!=null) {
                        showValuesMap.put(TReportLayoutBean.PSEUDO_COLUMNS.LINKED_ITEMS, stringBuilder.toString());
                    }
                }
                SortedSet<ReportBeanLink> ganntMSProjectReportBeanLinksSet = ganntMSProjectReportBeanLinksMap.get(workItemID);
                if (ganntMSProjectReportBeanLinksSet!=null) {
                    reportBean.setGanntMSProjectReportBeanLinksSet(ganntMSProjectReportBeanLinksSet);
                }
            }
        }
        return reportBeansList;
    }

    /**
     * Gets all linked workItems
     * @param itemBeanList
     * @param itemLinkBeans
     * @param personID
     * @param locale
     * @param ganntMSProjectReportBeanLinksMap out parameter for "reversed" (gantt compatible MS Project links)
     * @return
     */
    public static Map<Integer, SortedSet<ReportBeanLink>> getAllLinkedWorkItems(
            List<TWorkItemBean> itemBeanList, List<TWorkItemLinkBean> itemLinkBeans,
            Integer personID, Locale locale, Map<Integer, SortedSet<ReportBeanLink>> ganntMSProjectReportBeanLinksMap) {
        Map<Integer, SortedSet<ReportBeanLink>> allLinkedWorkItems = new HashMap<Integer, SortedSet<ReportBeanLink>>();
        Set<Integer> baseWorkItemIDsSet = new HashSet<Integer>();
        Map<Integer, TWorkItemBean> itemBeanMap = new HashMap<Integer, TWorkItemBean>();
        if (itemBeanList!=null) {
            for (TWorkItemBean workItemBean : itemBeanList) {
                Integer workItemID = workItemBean.getObjectID();
                baseWorkItemIDsSet.add(workItemID);
                itemBeanMap.put(workItemID, workItemBean);
            }
        }
        if (itemLinkBeans==null) {
            return allLinkedWorkItems;
        }
        Map<Integer, ILinkType> linkTypeIDToLinkTypeMap = LinkTypeBL.getLinkTypeIDToLinkTypeMap();
        //if project specificID is active gather the linked workItems not included in the reportBeanList
        Map<Integer, TWorkItemBean> linkedWorkItemsMap = new HashMap<Integer, TWorkItemBean>();
        Set<Integer> links = new HashSet<Integer>();
        boolean projectSpecificIDsActive = ApplicationBean.getInstance().getSiteBean().getProjectSpecificIDsOn();
        for (TWorkItemLinkBean workItemLinkBean : itemLinkBeans) {
            Integer linkPred = workItemLinkBean.getLinkPred();
            Integer linkSucc = workItemLinkBean.getLinkSucc();
            if (linkPred!=null && !baseWorkItemIDsSet.contains(linkPred)) {
                links.add(linkPred);
            }
            if (linkSucc!=null && !baseWorkItemIDsSet.contains(linkSucc)) {
                links.add(linkSucc);
            }
        }
        if (!links.isEmpty()) {
            List<TWorkItemBean> linkedWorkItemBeans = LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(
                    GeneralUtils.createIntArrFromIntegerCollection(links), personID, false, false, false);
            for (TWorkItemBean workItemBean : linkedWorkItemBeans) {
                linkedWorkItemsMap.put(workItemBean.getObjectID(), workItemBean);
            }
        }
        Map<String, String> linkTypeNames = LinkTypeBL.getLinkTypeNamesMap(locale);
        Map<Integer, TLinkTypeBean> allLinkTypesMap = GeneralUtils.createMapFromList(LinkTypeBL.loadAll());
        List<Integer> msProjectLinkTypes = null;
        //gather the MSProject link types if Gantt MSProject links are needed
        if (ganntMSProjectReportBeanLinksMap!=null) {
            MsProjectLinkType msProjectLinkType = MsProjectLinkType.getInstance();
            msProjectLinkTypes = LinkTypeBL.getLinkTypesByPluginClass(msProjectLinkType);
        }
        for (TWorkItemLinkBean workItemLinkBean : itemLinkBeans) {
            Integer linkID = workItemLinkBean.getObjectID();
            Integer linkTypeID = workItemLinkBean.getLinkType();
            Integer linkDirection = workItemLinkBean.getLinkDirection();
            Integer linkPred = workItemLinkBean.getLinkPred();
            Integer linkSucc = workItemLinkBean.getLinkSucc();
            if (linkSucc==null) {
                //link to a non-workItem
                continue;
            }
            TLinkTypeBean linkTypeBean = allLinkTypesMap.get(linkTypeID);
            if (linkTypeBean!=null) {
                SortedSet<ReportBeanLink> allLinksForWorkItem;
                Integer linkTypeDirection = linkTypeBean.getLinkDirection();
                ILinkType linkType = null;
                switch (linkTypeDirection) {
                case LINK_DIRECTION.UNIDIRECTIONAL_OUTWARD:
                    allLinksForWorkItem = allLinkedWorkItems.get(linkPred);
                    if (allLinksForWorkItem==null) {
                        allLinksForWorkItem = new TreeSet<ReportBeanLink>();
                        allLinkedWorkItems.put(linkPred, allLinksForWorkItem);
                    }
                    TWorkItemBean linkedItemBeanOutward = getLinkedReportBean(itemBeanMap, linkSucc);
                    String outwardLinkTitle = getLinkedItemTitle(linkedItemBeanOutward, linkedWorkItemsMap, linkSucc);
                    ReportBeanLink reportBeanLinkOutward = new ReportBeanLink(linkID, linkSucc, outwardLinkTitle, linkedItemBeanOutward!=null, linkTypeID, linkDirection,
                            linkTypeNames.get(MergeUtil.mergeKey(linkTypeID, linkDirection)));
                    if (projectSpecificIDsActive && linkSucc!=null) {
                        reportBeanLinkOutward.setProjectSpecificIssueNo(getProjectSpecificIssueNo(linkedItemBeanOutward, linkedWorkItemsMap, linkSucc));
                    }
                    linkType = linkTypeIDToLinkTypeMap.get(linkTypeID);
					if (linkType!=null) {
						reportBeanLinkOutward.setLinkSpecificData(linkType.getItemLinkSpecificData(workItemLinkBean));
					}
                    allLinksForWorkItem.add(reportBeanLinkOutward);
                    break;
                case LINK_DIRECTION.UNIDIRECTIONAL_INWARD:
                    allLinksForWorkItem = allLinkedWorkItems.get(linkSucc);
                    if (allLinksForWorkItem==null) {
                        allLinksForWorkItem =  new TreeSet<ReportBeanLink>();
                        allLinkedWorkItems.put(linkSucc, allLinksForWorkItem);
                    }
                    TWorkItemBean linkedItemBeanInward = getLinkedReportBean(itemBeanMap, linkPred);
                    String inwardLinkTitle = getLinkedItemTitle(linkedItemBeanInward, linkedWorkItemsMap, linkPred);
                    ReportBeanLink reportBeanLinkInward = new ReportBeanLink(linkID, linkPred, inwardLinkTitle, linkedItemBeanInward!=null, linkTypeID, linkDirection,
                            linkTypeNames.get(MergeUtil.mergeKey(linkTypeID, linkDirection)));
                    if (projectSpecificIDsActive && linkPred!=null) {
                        reportBeanLinkInward.setProjectSpecificIssueNo(getProjectSpecificIssueNo(linkedItemBeanInward, linkedWorkItemsMap, linkPred));
                    }
                    linkType = linkTypeIDToLinkTypeMap.get(linkTypeID);
					if (linkType!=null) {
						reportBeanLinkInward.setLinkSpecificData(linkType.getItemLinkSpecificData(workItemLinkBean));
					}
                    allLinksForWorkItem.add(reportBeanLinkInward);
                    if (ganntMSProjectReportBeanLinksMap!=null && msProjectLinkTypes!=null && msProjectLinkTypes.contains(linkTypeID)) {
                        //for Gantt chart the predecessors should contain the successors: same as it would be UNIDIRECTIONAL_OUTWARD, but only for msProject link types
                        SortedSet<ReportBeanLink> ganntMSProjectReportBeanLinkSet = ganntMSProjectReportBeanLinksMap.get(linkPred);
                        if (ganntMSProjectReportBeanLinkSet==null) {
                            ganntMSProjectReportBeanLinkSet = new TreeSet<ReportBeanLink>();
                            ganntMSProjectReportBeanLinksMap.put(linkPred, ganntMSProjectReportBeanLinkSet);
                        }
                        reportBeanLinkInward = new ReportBeanLink(linkID, linkSucc, inwardLinkTitle, linkedItemBeanInward!=null, linkTypeID, linkDirection,
                                linkTypeNames.get(MergeUtil.mergeKey(linkTypeID, linkDirection)));
                        //now only the MsProject links for the gantt chart need this specific data
                        reportBeanLinkInward.setLinkSpecificData(MsProjectLinkType.getInstance().getItemLinkSpecificData(workItemLinkBean));
                        if (projectSpecificIDsActive && linkPred!=null) {
                            reportBeanLinkInward.setProjectSpecificIssueNo(getProjectSpecificIssueNo(linkedItemBeanInward, linkedWorkItemsMap, linkSucc));
                        }
                        ganntMSProjectReportBeanLinkSet.add(reportBeanLinkInward);
                    }
                    break;
                case LINK_DIRECTION.BIDIRECTIONAL:
                    allLinksForWorkItem = allLinkedWorkItems.get(linkPred);
                    if (allLinksForWorkItem==null) {
                        allLinksForWorkItem =  new TreeSet<ReportBeanLink>();
                        allLinkedWorkItems.put(linkPred, allLinksForWorkItem);
                    }
                    TWorkItemBean succLinkedItemBean = getLinkedReportBean(itemBeanMap, linkSucc);
                    String succLinkTitle = getLinkedItemTitle(succLinkedItemBean, linkedWorkItemsMap, linkSucc);
                    ReportBeanLink reportBeanLinkSucc = new ReportBeanLink(linkID, linkSucc, succLinkTitle, succLinkedItemBean!=null, linkTypeID, linkDirection,
                            linkTypeNames.get(MergeUtil.mergeKey(linkTypeID, linkDirection)));
                    if (projectSpecificIDsActive && linkSucc!=null) {
                        reportBeanLinkSucc.setProjectSpecificIssueNo(getProjectSpecificIssueNo(succLinkedItemBean, linkedWorkItemsMap, linkSucc));
                    }
                    linkType = linkTypeIDToLinkTypeMap.get(linkTypeID);
					if (linkType!=null) {
						reportBeanLinkSucc.setLinkSpecificData(linkType.getItemLinkSpecificData(workItemLinkBean));
					}
                    allLinksForWorkItem.add(reportBeanLinkSucc);
                    if (linkDirection!=null) {
                        int reverseDirection = LinkTypeBL.getReverseDirection(linkDirection.intValue());
                        allLinksForWorkItem = allLinkedWorkItems.get(linkSucc);
                        if (allLinksForWorkItem==null) {
                            allLinksForWorkItem =  new TreeSet<ReportBeanLink>();
                            allLinkedWorkItems.put(linkSucc, allLinksForWorkItem);
                        }
                        TWorkItemBean predLinkedItemBean = getLinkedReportBean(itemBeanMap, linkPred);
                        String predLinkTitle = getLinkedItemTitle(predLinkedItemBean, linkedWorkItemsMap, linkPred);
                        ReportBeanLink reportBeanLinkPred = new ReportBeanLink(linkID, linkPred, predLinkTitle, predLinkedItemBean!=null, linkTypeID, Integer.valueOf(reverseDirection),
                                linkTypeNames.get(MergeUtil.mergeKey(linkTypeID, Integer.valueOf(reverseDirection))));
                        if (projectSpecificIDsActive && linkPred!=null) {
                            reportBeanLinkPred.setProjectSpecificIssueNo(getProjectSpecificIssueNo(predLinkedItemBean, linkedWorkItemsMap, linkPred));
                        }
                        if (linkType!=null) {
                        	reportBeanLinkPred.setLinkSpecificData(linkType.getItemLinkSpecificData(workItemLinkBean));
    					}
                        allLinksForWorkItem.add(reportBeanLinkPred);
                    }
                    break;
                default:
                    break;
                }
            }
        }
        return allLinkedWorkItems;
    }

    /**
     * Gets the report bean corresponding to the linked item
     * If the linked item is not in the result then it returns null;
     * @param itemBeanMap
     * @param linkedWorkItemID
     * @return
     */
    private static TWorkItemBean getLinkedReportBean(Map<Integer, TWorkItemBean> itemBeanMap, Integer linkedWorkItemID) {
        if (itemBeanMap!=null) {
            return itemBeanMap.get(linkedWorkItemID);
        }
        return null;
    }

    /**
     * Gets the linked item's title
     * @param baseItemBean might be null
     * @param linkedWorkItemsMap
     * @param linkedWorkItemID
     * @return
     */
    private static String getLinkedItemTitle(TWorkItemBean baseItemBean,
            Map<Integer, TWorkItemBean> linkedWorkItemsMap, Integer linkedWorkItemID) {
        if (baseItemBean!=null) {
            return baseItemBean.getSynopsis();
        } else {
            if (linkedWorkItemsMap!=null) {
                TWorkItemBean workItemBean = linkedWorkItemsMap.get(linkedWorkItemID);
                if (workItemBean!=null) {
                    return workItemBean.getSynopsis();
                }
            }
        }
        return "";
    }

    /**
     * Gets the project specific item number
     * @param reportBeanMap
     * @param linkedWorkItemsMap
     * @param linkedWorkItemID
     * @return
     */
    private static String getProjectSpecificIssueNo(TWorkItemBean baseItemBean,
            Map<Integer, TWorkItemBean> linkedWorkItemsMap, Integer linkedWorkItemID) {
        if (baseItemBean!=null) {
            return SystemProjectSpecificIssueNoRT.getShowValue(baseItemBean.getIDNumber(), baseItemBean);
        } else {
            if (linkedWorkItemsMap!=null) {
                TWorkItemBean workItemBean = linkedWorkItemsMap.get(linkedWorkItemID);
                if (workItemBean!=null) {
                    return SystemProjectSpecificIssueNoRT.getShowValue(workItemBean.getIDNumber(), workItemBean);
                }
            }
        }
        return "";
    }

    /**
     * Get the linked items: either linked though parent or children hierarchy or through plugin links
     * It is very important to remove those linked/parent/child issues which are
     * already contained in the result set to avoid adding them once again
     * @param linkTypeFilterSuperset
     * @param archived
     * @param deleted
     * @param itemTypesList
     * @param baseWorkItemBeanList
     */
    public static int[] getLinkedWorkItemIDs(String linkTypeFilterSuperset, Integer archived, Integer deleted,
    		List<Integer> itemTypesList, List<TWorkItemBean> baseWorkItemBeanList) {
        if (linkTypeFilterSuperset==null || "".equals(linkTypeFilterSuperset) ||
                baseWorkItemBeanList==null || baseWorkItemBeanList.isEmpty()) {
            return null;
        } else {
            List<Integer> baseWorkItemIDsList = GeneralUtils.createIntegerListFromBeanList(baseWorkItemBeanList);
            int[] baseWorkItemIDsArr = GeneralUtils.createIntArrFromIntegerList(baseWorkItemIDsList);
            Integer[] parts = MergeUtil.getParts(linkTypeFilterSuperset);
            Integer linkType = parts[0];
            Integer direction = parts[1];
            Set<Integer> linkedWorkItemIDsSet = null;
            if (ILinkType.PARENT_CHILD==linkType.intValue()) {
                if (PARENT_CHILD_EXPRESSION.ALL_PARENTS==direction) {
                    linkedWorkItemIDsSet = getParentHierarchy(baseWorkItemBeanList,
                            archived, deleted);
                } else {
                    linkedWorkItemIDsSet = ItemBL.getChildHierarchy(baseWorkItemIDsArr,
                            direction, archived, deleted, itemTypesList);
                }
            } else {
                Map<Integer, SortedSet<Integer>> linkedWorkItemIDsMap = getLinkedWorkItemIDsMap(
                        baseWorkItemIDsList, linkType, direction, archived, deleted);
                linkedWorkItemIDsSet = getFlatItems(linkedWorkItemIDsMap);
            }
            //remove those linked/parent/child issues which are
            //already contained in the result set to avoid adding them once again
            if (linkedWorkItemIDsSet!=null) {
                linkedWorkItemIDsSet.removeAll(GeneralUtils.createSetFromIntArr(baseWorkItemIDsArr));
            }
            return GeneralUtils.createIntArrFromSet(linkedWorkItemIDsSet);
        }
    }

    /**
     * Loads all dependent items in allItemIDSet
     * @param baseWorkItemBeans
     * @param archived
     * @param deleted
     * @param allItemIDSet
     */
    public static void loadAllDependentItems(List<TWorkItemBean> baseWorkItemBeans, Integer archived, Integer deleted, Set<Integer> allItemIDSet) {
    	Set<Integer> linkedItemIDsSet = loadAncestorDescendantAndDirectLinkedItems(baseWorkItemBeans, archived, deleted, allItemIDSet);
    	int i=0;
    	LOGGER.debug("Number of linked items at level " + i);
    	while (linkedItemIDsSet!=null && linkedItemIDsSet.size()>0) {
    		List<TWorkItemBean> linkedItemBeans = ItemBL.loadByWorkItemKeys(GeneralUtils.createIntArrFromSet(linkedItemIDsSet));
    		i++;
    		linkedItemIDsSet = loadAncestorDescendantAndDirectLinkedItems(linkedItemBeans, archived, deleted, allItemIDSet);
    		LOGGER.debug("Number of linked items at level " + i);
    	}
    }
    
    /**
     * 
     * @param baseWorkItemBeanList
     * @param archived
     * @param deleted
     * @param allItemIDSet
     * @return
     */
    private static Set<Integer> loadAncestorDescendantAndDirectLinkedItems(List<TWorkItemBean> baseWorkItemBeanList, Integer archived, Integer deleted, Set<Integer> allItemIDSet) {
    	Set<Integer> originalItemIDSet = GeneralUtils.createIntegerSetFromBeanList(baseWorkItemBeanList);
    	LOGGER.debug("Number of items in filter " + originalItemIDSet.size());
    	Set<Integer> ancestorWorkItemIDsSet = getParentHierarchy(baseWorkItemBeanList, archived, deleted);
    	LOGGER.debug("Number of ascendent items " + ancestorWorkItemIDsSet.size());
    	allItemIDSet.addAll(originalItemIDSet);
    	allItemIDSet.addAll(ancestorWorkItemIDsSet);
    	Set<Integer> descendantItemIDSet = ItemBL.getChildHierarchy(GeneralUtils.createIntArrFromIntegerCollection(allItemIDSet),
   			 PARENT_CHILD_EXPRESSION.ALL_CHILDREN, archived, deleted, null);
    	LOGGER.debug("Total number of descendent items " + descendantItemIDSet.size());
    	allItemIDSet.addAll(descendantItemIDSet);
    	//gather the MSProject link types
        MsProjectLinkType msProjectLinkType = MsProjectLinkType.getInstance();
        List<Integer> msProjectLinkTypes = LinkTypeBL.getLinkTypesByPluginClass(msProjectLinkType);
        //although Msproject link is unidirectional, we have to load also the predecessors in order to avoid moving back the successor items
        Map<Integer, SortedSet<Integer>> linkDependencyMap = ItemLinkBL.loadByWorkItemsAndLinkType(GeneralUtils.createIntegerListFromCollection(allItemIDSet), msProjectLinkTypes, msProjectLinkType.getPossibleDirection(), true, archived, deleted);
        Set<Integer> linkedItemIDsSet = getFlatItems(linkDependencyMap);
        LOGGER.debug("Number of linked items from hierarchy " + linkedItemIDsSet.size());
        linkedItemIDsSet.removeAll(allItemIDSet);
        LOGGER.debug("Number of extended linked items " + linkedItemIDsSet.size());
        allItemIDSet.addAll(linkedItemIDsSet);
        return linkedItemIDsSet;
    }
     
    
   
    public static int[] getMsProjectLinkedWorkItemIDs(Integer archived, Integer deleted, List<TWorkItemBean> baseWorkItemBeanList) {
        List<TLinkTypeBean> linkTypeBeans = LinkTypeBL.loadAll();
        Integer linkTypeID = null;
        for (Iterator<TLinkTypeBean> iterator = linkTypeBeans.iterator(); iterator.hasNext();) {
            TLinkTypeBean linkTypeBean = iterator.next();
            linkTypeID = linkTypeBean.getObjectID();
            String linkTypePluginClass = LinkTypeBL.getLinkTypePluginString(linkTypeID);
            ILinkType linkType = (ILinkType)PluginManager.getInstance().getPluginClass(PluginManager.LINKTYPE_ELEMENT, linkTypePluginClass);
            if(linkType.isGanttSpecific()) {
                break;
            }
        }
        String ganttLinkType = "";
        if(linkTypeID != null) {
            ganttLinkType = linkTypeID.toString() + "_" + "2";
        }
        if(!"".equals(ganttLinkType)) {
            return getLinkedWorkItemIDs(ganttLinkType, archived, deleted, null, baseWorkItemBeanList);
        }
        return ArrayUtils.EMPTY_INT_ARRAY;
    }

    /**
     * Get all ascendents which are not included in the original query result
     * @param baseWorkItemBeans
     * @return
     */
    private static Set<Integer> getParentHierarchy(List<TWorkItemBean> baseWorkItemBeans, Integer archived, Integer deleted) {
        Set<Integer> allAscendentIDsSet = new HashSet<Integer>();
        if (baseWorkItemBeans!=null) {
            //to avoid infinite cycles by inconsistent data
            Set<Integer> toRemoveSet = GeneralUtils.createIntegerSetFromBeanList(baseWorkItemBeans);
            List<TWorkItemBean> workItemBeans = baseWorkItemBeans;
            Set<Integer> notIncludedParentIDs;
            int i=0;
            do {
                notIncludedParentIDs = new HashSet<Integer>();
                if (workItemBeans!=null) {
	                for (TWorkItemBean workItemBean : workItemBeans) {
	                    Integer parentID = workItemBean.getSuperiorworkitem();
	                    if (parentID!=null && !toRemoveSet.contains(parentID)) {
	                        notIncludedParentIDs.add(parentID);
	                    }
	                }
	            }
                i++;
                LOGGER.debug(notIncludedParentIDs.size() + " parents found at " + i + ". level");
                //gather the not yet present parents to the remove set to avoid infinite cycles by inconsistent data
                workItemBeans = null;
                if (!notIncludedParentIDs.isEmpty()) {
	                toRemoveSet.addAll(notIncludedParentIDs);
	                //get the next level of parent workItems
	                workItemBeans = workItemDAO.loadByWorkItemKeys(
	                        GeneralUtils.createIntArrFromSet(notIncludedParentIDs),
	                        archived, deleted);
	                //retain only the workItems with corresponding archive/delete
	                notIncludedParentIDs.retainAll(GeneralUtils.createIntegerListFromBeanList(workItemBeans));
	                //add the not yet present parents to the list
	                allAscendentIDsSet.addAll(notIncludedParentIDs);
                }
            } while (!notIncludedParentIDs.isEmpty());
        }
        return allAscendentIDsSet;
    }

    /**
     * Gets the linked issues to add
     * @param baseWorkItemIDsList
     * @param linkType
     * @param direction
     * @param archived
     * @param deleted
     * @return
     */
    private static Map<Integer, SortedSet<Integer>> getLinkedWorkItemIDsMap(
            List<Integer> baseWorkItemIDsList, Integer linkType, Integer direction,
            Integer archived, Integer deleted) {
        if (linkType==null || LinkTypeBL.loadByPrimaryKey(linkType)==null) {
            //the link type is null or is a link type which was deleted previously return an empty array
            //(it can happen because there is no foreign key relationship for the expressions in CLOB values)
            return new HashMap<Integer, SortedSet<Integer>>();
        }
        String linkTypePluginClass = LinkTypeBL.getLinkTypePluginString(linkType);
        ILinkType linkTypeBase = (ILinkType)PluginManager.getInstance().getPluginClass(PluginManager.LINKTYPE_ELEMENT, linkTypePluginClass);
        boolean isBidirectional = LinkTypeBL.isBidirectional(linkType);
        //If link type == Gantt /Ms. project/ we need to obtains predecessors and successors
        if(linkTypeBase.isGanttSpecific()) {
            isBidirectional = true;
        }

        List<Integer> linkTypes = new ArrayList<Integer>();
        if (linkType!=null) {
            linkTypes.add(linkType);
        }
        return ItemLinkBL.loadByWorkItemsAndLinkType(baseWorkItemIDsList, linkTypes, direction, isBidirectional, archived, deleted);
    }

    /**
     * Get the linked workItemIDs
     * @param linkedWorkItemIDsMap
     * @return
     */
    private static SortedSet<Integer> getFlatItems(
            Map<Integer, SortedSet<Integer>> linkedWorkItemIDsMap) {
        //get the flat linked items
        SortedSet<Integer> linkedWorkItemIDsSet = new TreeSet<Integer>();
        if (linkedWorkItemIDsMap!=null) {
            Iterator<SortedSet<Integer>> iterator = linkedWorkItemIDsMap.values().iterator();
            while (iterator.hasNext()) {
                linkedWorkItemIDsSet.addAll(iterator.next());
            }
        }
        return linkedWorkItemIDsSet;
    }
}
