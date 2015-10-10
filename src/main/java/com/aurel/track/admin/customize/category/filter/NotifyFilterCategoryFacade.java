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

package com.aurel.track.admin.customize.category.filter;


import java.util.List;
import java.util.Locale;

import com.aurel.track.admin.customize.category.filter.FilterBL.FILTER_TYPE;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TFilterCategoryBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.FilterCategoryDAO;

/**
 * An implementation of CategoryFacade for notify filter categories
 * @author Tamas Ruff
 *
 */
public class NotifyFilterCategoryFacade extends FilterCategoryFacade {
	
	private static FilterCategoryDAO filterCategoryDAO = DAOFactory.getFactory().getFilterCategoryDAO();	
	private static NotifyFilterCategoryFacade instance;

	/**
	 * Return a FieldConfigItemFacade instance which implements the ConfigItemFacade 
	 * @return
	 */
	public static NotifyFilterCategoryFacade getInstance(){
		if(instance==null){
			instance=new NotifyFilterCategoryFacade();
		}
		return instance;
	}
	
	/**
	 * Creates a new category bean
	 */
	@Override
	public ILabelBean getNewBean() {
		TFilterCategoryBean filterCategoryBean = new TFilterCategoryBean();
		filterCategoryBean.setFilterType(FILTER_TYPE.NOTIFY_FILTER);
		return filterCategoryBean;
	}
	
	/**
	 * Gets the root categories/leafs
	 * @param repository
	 * @param projectID
	 * @param personID  filter by only if set
	 * @param locale
	 * @return
	 */
	@Override
	public List<ILabelBean> getRootObjects(Integer repository, Integer projectID, Integer personID, Locale locale) {
		return (List)filterCategoryDAO.loadRootCategories(
				repository, FILTER_TYPE.NOTIFY_FILTER, projectID, personID);
	}
	
	/**
	 * Gets the root categories/leafs by projects
	 * @param projectIDs
	 * @param locale
	 * @return
	 */
	@Override
	public List<ILabelBean> getRootObjectsByProjects(List<Integer> projectIDs, Locale locale) {
		return (List)filterCategoryDAO.loadProjectRootCategories(
				projectIDs, FILTER_TYPE.NOTIFY_FILTER);
	}
	
	/**
	 * Gets the categories/leafs by label
	 * @param repository
	 * @param parentID
	 * @param projectID
	 * @param personID
	 * @param subtype
	 * @param label
	 * @return
	 */
	@Override
	public List<ILabelBean> getListByLabel(Integer repository, Integer parentID,
			Integer projectID, Integer personID, Integer subtype, String label) {
		return (List)filterCategoryDAO.loadByLabel(
				repository, FILTER_TYPE.NOTIFY_FILTER, parentID, projectID, personID, label);
	}	
	
	@Override
	public boolean replaceNeeded(Integer objectID) {
		NotifyFilterFacade notifyFilterFacade = NotifyFilterFacade.getInstance();
		List<ILabelBean> filterBeans = notifyFilterFacade.getByParent(objectID, null);
		if (filterBeans!=null) {
			for (ILabelBean labelBean : filterBeans) {
				boolean replaceNeeded = notifyFilterFacade.replaceNeeded(labelBean.getObjectID());
				if (replaceNeeded) {
					return true;
				}
			}
		}
		NotifyFilterCategoryFacade notifyFilterCategoryFacade = NotifyFilterCategoryFacade.getInstance();
		List<ILabelBean> filterCategoryList = notifyFilterCategoryFacade.getByParent(objectID, null);
		if (filterCategoryList!=null) {
			for (ILabelBean labelBean : filterCategoryList) {
				boolean replaceNeeded = replaceNeeded(labelBean.getObjectID());
				if (replaceNeeded) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public void replace(Integer objectID, Integer replaceQueryID) {
		NotifyFilterFacade notifyFilterFacade = NotifyFilterFacade.getInstance();
		List<ILabelBean> filterBeans = notifyFilterFacade.getByParent(objectID, null);
		if (filterBeans!=null) {
			for (ILabelBean labelBean : filterBeans) {
				notifyFilterFacade.replace(labelBean.getObjectID(), replaceQueryID);
			}
		}
		NotifyFilterCategoryFacade notifyFilterCategoryFacade = NotifyFilterCategoryFacade.getInstance();
		List<ILabelBean> filterCategoryList = notifyFilterCategoryFacade.getByParent(objectID, null);
		if (filterCategoryList!=null) {
			for (ILabelBean labelBean : filterCategoryList) {
				replace(labelBean.getObjectID(), replaceQueryID);
			}
		}
	}
}
