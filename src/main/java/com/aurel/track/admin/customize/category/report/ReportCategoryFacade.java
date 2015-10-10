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

package com.aurel.track.admin.customize.category.report;

import java.util.List;
import java.util.Locale;

import com.aurel.track.admin.customize.category.CategoryFacade;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TReportCategoryBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ReportCategoryDAO;

/**
 * An implementation of CategoryFacade for report categories
 * @author Tamas
 *
 */
public class ReportCategoryFacade extends CategoryFacade {
	
	static interface ICONS {
		static String PDF = "pdf.gif";
		static String EXCEL = "xls.gif";
	}

	static interface EXPORT_FORMATS {
		static String PDF = "pdf";
		static String EXCEL = "xls";
	}
	
	private static ReportCategoryDAO reportCategoryDAO = DAOFactory.getFactory().getReportCategoryDAO();	
	private static ReportCategoryFacade instance;
	
	/**
	 * Get a singleton instance
	 * @return
	 */
	public static ReportCategoryFacade getInstance(){
		if(instance==null){
			instance=new ReportCategoryFacade();
		}
		return instance;
	}
	
	/**
	 * Gets the category by key
	 * @param categoryID
	 * @return
	 */
	@Override
	public ILabelBean getByKey(Integer categoryID) {
		return reportCategoryDAO.loadByPrimaryKey(categoryID);
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
	 * Gets the root categories/leafs
	 * @param repository
	 * @param projectID
	 * @param personID
	 * @param locale
	 * @return
	 */
	@Override
	public List<ILabelBean> getRootObjects(Integer repository, Integer projectID, Integer personID, Locale locale) {
		return (List)reportCategoryDAO.loadRootCategories(repository, projectID, personID);
	}
	
	/**
	 * Gets the root categories/leafs by projects
	 * @param projectIDs
	 * @param locale
	 * @return
	 */
	@Override
	public List<ILabelBean> getRootObjectsByProjects(List<Integer> projectIDs, Locale locale) {
		return (List)reportCategoryDAO.loadProjectRootCategories(projectIDs);
	}
	
	/**
	 * Gets the categories by parent
	 * @param parentCategoryID
	 * @param locale
	 * @return
	 */
	@Override
	public List<ILabelBean> getByParent(Integer parentCategoryID, Locale locale) {
		return (List)reportCategoryDAO.loadByParent(parentCategoryID);
	}
	
	/**
	 * Gets the categories by parent
	 * @param parentCategoryIDs
	 * @return
	 */
	@Override
	public List<ILabelBean> getByParents(List<Integer> parentCategoryIDs) {
		return (List)reportCategoryDAO.loadByParents(parentCategoryIDs);
	}
	
	/**
	 * Creates a new category bean
	 */
	@Override
	public ILabelBean getNewBean() {
		return new TReportCategoryBean();
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
		return (List)reportCategoryDAO.loadByLabel(repository, parentID, projectID, personID, label);
	}
	
	/**
	 * Gets the project
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getProjectID(ILabelBean labelBean) {
		TReportCategoryBean reportCategoryBean = (TReportCategoryBean)labelBean;
		if (reportCategoryBean!=null) {
			return reportCategoryBean.getProject();
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
		TReportCategoryBean reportCategoryBean = (TReportCategoryBean)labelBean;
		if (reportCategoryBean!=null) {
			reportCategoryBean.setProject(projectID);
		}
	}
	
	/**
	 * Gets the parent category for a category
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getParentID(ILabelBean labelBean) {
		TReportCategoryBean reportCategoryBean = (TReportCategoryBean)labelBean;
		if (reportCategoryBean!=null) {
			return reportCategoryBean.getParentID();
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
		TReportCategoryBean reportCategoryBean = (TReportCategoryBean)labelBean;
		if (reportCategoryBean!=null) {
			reportCategoryBean.setParentID(parentID);
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
		TReportCategoryBean reportCategoryBean = (TReportCategoryBean)labelBean;
		if (reportCategoryBean!=null) {
			reportCategoryBean.setRepository(repository);
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
		TReportCategoryBean reportCategoryBean = (TReportCategoryBean)labelBean;
		if (reportCategoryBean!=null) {
			reportCategoryBean.setLabel(label);
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
		TReportCategoryBean reportCategoryBean = (TReportCategoryBean)labelBean;
		if (reportCategoryBean!=null) {
			reportCategoryBean.setCreatedBy(person);
		}
	}
	
	/**
	 * Gets the person
	 * @param labelBean
	 * @param person
	 * @return
	 */
	@Override
	public Integer getCreatedBy(ILabelBean labelBean) {
		TReportCategoryBean reportCategoryBean = (TReportCategoryBean)labelBean;
		if (reportCategoryBean!=null) {
			return reportCategoryBean.getCreatedBy();
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
		TReportCategoryBean reportCategoryBean = (TReportCategoryBean)labelBean;
		if (reportCategoryBean!=null) {
			return reportCategoryDAO.save(reportCategoryBean);
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
		return reportCategoryDAO.hasDependenData(objectID);
	}
	
	/**
	 * Does the node should be replaced in case of delete
	 * @param objectID
	 * @return
	 */
	@Override
	public boolean replaceNeeded(Integer objectID) {
		return false;
	}
	
	/**
	 * Replace the node (leaf) or leafs below the node (category)
	 * by deleting the node
	 * @param objectID: can be a categoryID or a leafID
	 * @param replacementID: is always a leafID
	 * @return
	 */
	@Override
	public void replace(Integer objectID, Integer replacementID) {
	}
	
	/**
	 * Deletes the category itself after all descendants are deleted
	 * @param categoryID
	 * @return
	 */
	@Override
	public void deleteCategory(Integer categoryID)  {
		reportCategoryDAO.delete(categoryID);
	}	
}
