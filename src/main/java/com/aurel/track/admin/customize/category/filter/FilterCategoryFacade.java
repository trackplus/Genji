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

import com.aurel.track.admin.customize.category.CategoryFacade;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TFilterCategoryBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.FilterCategoryDAO;

/**
 * An implementation of CategoryFacade for filter categories:
 * issue filters (tree/TQLPlus/TQL ) and notification filters
 * @author Tamas Ruff
 *
 */
public abstract class FilterCategoryFacade extends CategoryFacade {
	
	private static FilterCategoryDAO filterCategoryDAO = DAOFactory.getFactory().getFilterCategoryDAO();
	
	/**
	 * Gets the category by key
	 * @param categoryID
	 * @return
	 */
	@Override
	public ILabelBean getByKey(Integer categoryID) {
		return filterCategoryDAO.loadByPrimaryKey(categoryID);
	}
	
	/**
	 * Loads a leaf by key and locale
	 * @param objectID
	 * @param locale
	 * @return
	 */
	@Override
	public ILabelBean getByKey(Integer objectID, Locale locale) {
		//localization not implemented
		return getByKey(objectID);
	}
	
	/**
	 * Gets the categories by parent
	 * @param parentCategoryID
	 * @param locale
	 * @return
	 */
	@Override
	public List<ILabelBean> getByParent(Integer parentCategoryID, Locale locale) {
		return (List)filterCategoryDAO.loadByParent(parentCategoryID);
	}
	
	/**
	 * Gets the categories by parent
	 * @param parentCategoryIDs
	 * @return
	 */
	@Override
	public List<ILabelBean> getByParents(List<Integer> parentCategoryIDs) {
		return (List)filterCategoryDAO.loadByParents(parentCategoryIDs);
	}
	
	/**
	 * Gets the project
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getProjectID(ILabelBean labelBean) {
		TFilterCategoryBean filterCategoryBean = (TFilterCategoryBean)labelBean;
		if (filterCategoryBean!=null) {
			return filterCategoryBean.getProject();
		}
		return null;
	}
	
	/**
	 * Sets the project
	 * @param labelBean
	 * @param projectID
	 * @return
	 */
	@Override
	public void setProjectID(ILabelBean labelBean, Integer projectID) {
		TFilterCategoryBean filterCategoryBean = (TFilterCategoryBean)labelBean;
		if (filterCategoryBean!=null) {
			filterCategoryBean.setProject(projectID);
		}
	}

	/**
	 * Gets the parent category for a category
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getParentID(ILabelBean labelBean) {
		TFilterCategoryBean filterCategoryBean = (TFilterCategoryBean)labelBean;
		if (filterCategoryBean!=null) {
			return filterCategoryBean.getParentID();
		}
		return null;
	}
	
	/**
	 * Sets the parent for a category
	 * @param labelBean
	 * @param parentID
	 * @return
	 */
	@Override
	public void setParentID(ILabelBean labelBean, Integer parentID) {
		TFilterCategoryBean filterCategoryBean = (TFilterCategoryBean)labelBean;
		if (filterCategoryBean!=null) {
			filterCategoryBean.setParentID(parentID);
		}
	}

	/**
	 * Sets the repository
	 * @param labelBean
	 * @param repository
	 * @return
	 */
	@Override
	public void setRepository(ILabelBean labelBean, Integer repository) {
		TFilterCategoryBean filterCategoryBean = (TFilterCategoryBean)labelBean;
		if (filterCategoryBean!=null) {
			filterCategoryBean.setRepository(repository);
		}
	}
	
	/**
	 * Sets the label for a category
	 * @param labelBean
	 * @param label
	 * @return
	 */
	@Override
	public void setLabel(ILabelBean labelBean, String label) {
		TFilterCategoryBean filterCategoryBean = (TFilterCategoryBean)labelBean;
		if (filterCategoryBean!=null) {
			filterCategoryBean.setLabel(label);
		}
	}
	
	/**
	 * Sets the person
	 * @param labelBean
	 * @param person
	 * @return
	 */
	@Override
	public void setCreatedBy(ILabelBean labelBean, Integer person) {
		TFilterCategoryBean filterCategoryBean = (TFilterCategoryBean)labelBean;
		if (filterCategoryBean!=null) {
			filterCategoryBean.setCreatedBy(person);
		}
	}
	
	/**
	 * Gets the person
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getCreatedBy(ILabelBean labelBean) {
		TFilterCategoryBean filterCategoryBean = (TFilterCategoryBean)labelBean;
		if (filterCategoryBean!=null) {
			return filterCategoryBean.getCreatedBy();
		}
		return null;
	}
			
	
	/**
	 * Saves a new/modified category 
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer save(ILabelBean labelBean) {
		TFilterCategoryBean filterCategoryBean = (TFilterCategoryBean)labelBean;
		if (filterCategoryBean!=null) {
			return filterCategoryDAO.save(filterCategoryBean);
		}
		return null;
	}

	/**
	 * Does the category has content (subcategories or leafs)
	 * @param objectID
	 * @return
	 */	
	@Override
	public boolean hasDependentData(Integer objectID) {		
		return filterCategoryDAO.hasDependenData(objectID);
	}
		
	/**
	 * Deletes the category itself after all descendants are deleted
	 * @param categoryID
	 * @return
	 */
	@Override
	public void deleteCategory(Integer categoryID)  {
		filterCategoryDAO.delete(categoryID);
	}
	
}
