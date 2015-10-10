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

package com.aurel.track.admin.customize.category.filter.execute.loadItems;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TAttributeValueBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.bl.AttributeValueBL;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.types.FieldTypeManager;

/**
 * Utility class fro loading the items from the database
 * @author Tamas
 *
 */
public class LoadItemsUtil {
    private static final Logger LOGGER = LogManager.getLogger(LoadItemsUtil.class);

    private LoadItemsUtil() {
    }

    /**
     * Get only workItems from project/release if specified
     * @param workItemBeanListDB
     * @param projectID
     * @param entityFlag
     * @return
     */
    public static List<TWorkItemBean> filterByProjectAndEntityFlag(
            List<TWorkItemBean> workItemBeanListDB, Integer projectID, Integer entityFlag) {
        List<TWorkItemBean> workItemBeanList;
        if(projectID!=null) {
            workItemBeanList = new LinkedList<TWorkItemBean>();
            for (TWorkItemBean itemBean : workItemBeanListDB) {
                //projectID or releaseID
                if(SystemFields.INTEGER_PROJECT.equals(entityFlag)){
                    if(itemBean.getProjectID().intValue()==projectID.intValue()){
                        workItemBeanList.add(itemBean);
                    }
                }else{
                    if (itemBean.getReleaseScheduledID()!=null && itemBean.getReleaseScheduledID().intValue()==projectID.intValue()){
                        workItemBeanList.add(itemBean);
                    }
                }
            }
        } else {
            workItemBeanList = workItemBeanListDB;
        }
        return workItemBeanList;
    }

    /**
     * Populates a list of workItemBeans with custom attributes
     * @param workItemBeanList  list of workItemBeans
     * @param attributeValueBeanList list of attributeValueBean
     * @return
     */
    public static List<TWorkItemBean> loadCustomFields(List<TWorkItemBean> workItemBeanList, List<TAttributeValueBean> attributeValueBeanList) {
        Date start = null;
        if (LOGGER.isDebugEnabled()) {
            start = new Date();
        }
        Map<Integer, Map<String, Object>> workItemsAttributesMap = AttributeValueBL.prepareAttributeValueMapForWorkItems(attributeValueBeanList, null, null);
        for (TWorkItemBean workItemBean : workItemBeanList) {
            Integer workItemID = workItemBean.getObjectID();
            //load all custom fields because they might be needed by filtering
            //the resulted workItemList by further matchers found in a custom query
            Map<String, Object> attributeValueBeanMap = workItemsAttributesMap.get(workItemID);
            if (attributeValueBeanMap!=null) {
                for (String mergeKey : attributeValueBeanMap.keySet()) {
                    Integer[] keyComponents = MergeUtil.getParts(mergeKey);
                    Integer fieldID = keyComponents[0];
                    Integer parameterCode = keyComponents[1];
                    if (parameterCode==null || Integer.valueOf(1).equals(parameterCode)) {
                        //load the not composites and the composite if it is the first part
                        //(in order to avoid loading the composite again and again for each of his part)
                        IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
                        if (fieldTypeRT!=null) {
                            fieldTypeRT.processLoad(fieldID, null, workItemBean, attributeValueBeanMap);
                        }
                    }
                }
            }
        }
        if (LOGGER.isDebugEnabled() && start!=null) {
            Date end = new Date();
            LOGGER.debug("Populating custom fields lasted " + Long.toString(new Long(end.getTime()-start.getTime())) + " ms");
        }
        return workItemBeanList;
    }

    /**
     * Removes the archived or deleted items
     * This is useful in cases where the archived/deleted items couldn't be removed at database query level
     * @param workItemBeanList
     */
    public static void removeArchivedOrDeleted(List<TWorkItemBean> workItemBeanList) {
        if (workItemBeanList!=null) {
            for (Iterator<TWorkItemBean> iterator = workItemBeanList.iterator(); iterator.hasNext();) {
                TWorkItemBean workItemBean = (TWorkItemBean) iterator.next();
                if (workItemBean.isArchivedOrDeleted()) {
                    iterator.remove();
                }
            }
        }
    }
}
