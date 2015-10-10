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

import java.util.List;

import com.aurel.track.GeneralSettings;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemDAO;

/**
 * Loads the responsible items for "in basket" and calendar items
 * @author Tamas
 *
 */
public class LoadResponsibleItems {
    private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();

    private LoadResponsibleItems(){
    }

    /**
     * Count the number of items in InBasket
     * @param personID
     * @return
     */
    public static Integer countResponsibleWorkItems(Integer personID) {
        return workItemDAO.countResponsibleWorkItems(personID);
    }

    /**
     * Gets a list of responsible items.
     * @param personBean
     * @throws TooManyItemsToLoadException
     */
    public static List<TWorkItemBean> loadResponsibleWorkItems(TPersonBean personBean) throws TooManyItemsToLoadException {
        Boolean raciRoleItemsAboveLimit = personBean.getRaciRoleItemsAboveLimit();
        if (raciRoleItemsAboveLimit==null) {
            raciRoleItemsAboveLimit = Boolean.TRUE;
        }
        if (raciRoleItemsAboveLimit.booleanValue()) {
            Integer count = countResponsibleWorkItems(personBean.getObjectID());
            if (count!=null && count.intValue()>GeneralSettings.getMaxItems()) {
                throw new TooManyItemsToLoadException("Too many items to load " + count, count);
            }
        }
        return workItemDAO.loadResponsibleWorkItems(personBean.getObjectID());
    }
}
