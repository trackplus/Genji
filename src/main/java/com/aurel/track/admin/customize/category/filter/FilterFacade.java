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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.LeafFacade;
import com.aurel.track.admin.customize.category.filter.tree.design.FieldExpressionInTreeTO;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.localize.LocalizeBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ILocalizedLabelBean;
import com.aurel.track.beans.TCLOBBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.QueryRepositoryDAO;
import com.aurel.track.resources.LocalizeUtil;

/**
 * An implementation of LeafFacade for filters
 * @author Tamas Ruff
 *
 */
public abstract class FilterFacade extends LeafFacade {
	
	private static final Logger LOGGER = LogManager.getLogger(FilterFacade.class);
	protected static final QueryRepositoryDAO queryRepositoryDAO = DAOFactory.getFactory().getQueryRepositoryDAO();
	
	/**
	 * Get the human readable format of the field expression
	 * @param objectID
	 * @param locale
	 */
	@Override
	public String getFilterExpressionString(Integer objectID, Locale locale) {
		TQueryRepositoryBean queryRepositoryBean = (TQueryRepositoryBean)getByKey(objectID);
		if (queryRepositoryBean!=null) {
			return getFilterExpressionString(loadFilterExpression(queryRepositoryBean.getQueryKey()), locale);
		}
		return "";
	}
	
	/**
	 * Get the human readable format of the field expression
	 * @param filterExpression
	 * @param locale
	 */	
	public abstract String getFilterExpressionString(String filterExpression, Locale locale);
	
	/**
	 * Get the possible filter types for the facade	for getting only the filters of specific types
	 * @return
	 */
	public abstract List<Integer> getReportQueryTypes();
		
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
		return FilterBL.localizeAndSort((List)queryRepositoryDAO.loadRootFilters(repository, projectID, personID, getReportQueryTypes()), locale);
	}
	
	/**
	 * Gets the root categories/leafs by projects
	 * @param projectIDs
	 * @param locale
	 * @return
	 */
	@Override
	public List<ILabelBean> getRootObjectsByProjects(List<Integer> projectIDs, Locale locale) {
		return FilterBL.localizeAndSort((List)queryRepositoryDAO.loadProjectRootFilters(projectIDs, getReportQueryTypes()), locale);
	}
	
	/**
	 * Gets the leafs by parent
	 * @param parentCategoryID
	 * @param locale
	 * @return
	 */
	@Override
	public List<ILabelBean> getByParent(Integer parentCategoryID, Locale locale) {
		return FilterBL.localizeAndSort((List)queryRepositoryDAO.loadByCategory(parentCategoryID), locale);
	}
	
	
	/**
	 * Gets the leafs by parents
	 * @param parentCategoryIDs
	 * @return
	 */
	@Override
	public List<ILabelBean> getByParents(List<Integer> parentCategoryIDs) {
		return (List)queryRepositoryDAO.loadByCategories(parentCategoryIDs);
	}
	
	/**
	 * Creates a new leaf bean
	 */
	@Override
	public ILabelBean getNewBean() {
		return new TQueryRepositoryBean();
	}
	
	/**
	 * Gets the leaf (filter or report) by key
	 * @param objectID
	 * @return
	 */
	@Override
	public ILabelBean getByKey(Integer objectID) {
		return queryRepositoryDAO.loadByPrimaryKey(objectID);
	}
	
	/**
	 * Loads a leaf by key and locale
	 * @param objectID
	 * @param locale
	 * @return
	 */
	@Override
	public ILabelBean getByKey(Integer objectID, Locale locale) {
		ILocalizedLabelBean localizedLabelBean = queryRepositoryDAO.loadByPrimaryKey(objectID);
		if(localizedLabelBean!=null){
			localizedLabelBean.setLabel(LocalizeUtil.localizeDropDownEntry(localizedLabelBean, locale));
		}
		return localizedLabelBean;
	}
		
	/**
	 * Gets the categories/leafs by label
	 * @param repository
	 * @param parentID
	 * @param projectID
	 * @param personID filter by only if set
	 * @param subtype
	 * @param label
	 * @return
	 */
	@Override
	public List<ILabelBean> getListByLabel(Integer repository, Integer parentID,
			Integer projectID, Integer personID, Integer subtype, String label) {		
		return (List)queryRepositoryDAO.loadByLabel(repository, parentID, projectID, personID, subtype, label);
	}
	
	/**
	 * Gets the project
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getProjectID(ILabelBean labelBean) {
		TQueryRepositoryBean queryRepositoryBean = (TQueryRepositoryBean)labelBean;
		if (queryRepositoryBean!=null) {
			return queryRepositoryBean.getProject();
		} else {
			return null;
		}
	}
	
	/**
	 * Sets the project
	 * @param labelBean
	 * @param projectID
	 * @return
	 */
	@Override
	public void setProjectID(ILabelBean labelBean, Integer projectID) {
		TQueryRepositoryBean queryRepositoryBean = (TQueryRepositoryBean)labelBean;
		if (queryRepositoryBean!=null) {
			queryRepositoryBean.setProject(projectID);
		}
	}	
	
	/**
	 * Gets the category for a leaf
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getParentID(ILabelBean labelBean) {
		TQueryRepositoryBean queryRepositoryBean = (TQueryRepositoryBean)labelBean;
		if (queryRepositoryBean!=null) {
			return queryRepositoryBean.getCategoryKey();
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
		TQueryRepositoryBean queryRepositoryBean = (TQueryRepositoryBean)labelBean;
		if (queryRepositoryBean!=null) {
			queryRepositoryBean.setCategoryKey(parentID);
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
		TQueryRepositoryBean queryRepositoryBean = (TQueryRepositoryBean)labelBean;
		if (queryRepositoryBean!=null) {
			queryRepositoryBean.setRepositoryType(repository);
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
		TQueryRepositoryBean queryRepositoryBean = (TQueryRepositoryBean)labelBean;
		if (queryRepositoryBean!=null) {
			queryRepositoryBean.setLabel(label);
		}
	}
	
	/**
	 * Gets the person
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getCreatedBy(ILabelBean labelBean) {
		TQueryRepositoryBean queryRepositoryBean = (TQueryRepositoryBean)labelBean;
		if (queryRepositoryBean!=null) {
			return queryRepositoryBean.getPerson();
		}
		return null;
	}
	
	/**
	 * Sets the person
	 * @param labelBean
	 * @param person
	 * @return
	 */
	@Override
	public void setCreatedBy(ILabelBean labelBean, Integer person) {
		TQueryRepositoryBean queryRepositoryBean = (TQueryRepositoryBean)labelBean;
		if (queryRepositoryBean!=null) {
			queryRepositoryBean.setPerson(person);
		}
	}

	/**
	 * Saves the filter after submit, before save
	 * @param repository
	 * @param objectID
	 * @param number
	 * @param personID
	 * @param add
	 */
	
	/**
	 * Saves a clob
	 * @param clobID
	 * @param filterExpression
	 * @return
	 */
	/*private static Integer saveFilterExpression(Integer clobID, String filterExpression) {
		TCLOBBean clobBean = null;
		if (clobID!=null) {
			clobBean = clobDAO.loadByPrimaryKey(clobID);
		}
		if (clobBean==null) {
			clobBean = new TCLOBBean();
		}
		clobBean.setCLOBValue(filterExpression);
		return clobDAO.save(clobBean);
	}*/	

	/**
	 * Saves a modified leaf 
	 * @param labelBean	
	 * @return
	 */
	@Override
	public Integer save(ILabelBean labelBean) {
		TQueryRepositoryBean queryRepositoryBean = (TQueryRepositoryBean)labelBean;	
		if (queryRepositoryBean!=null) {
			return queryRepositoryDAO.save(queryRepositoryBean);
		}		
		return null;
	}
		
		
	/**
	 * Copy specific data if copy (not cut)
	 * @param labelBeanFrom
	 * @param labelBeanTo
	 */
	@Override
	public void copySpecific(ILabelBean labelBeanFrom, ILabelBean labelBeanTo) {
		TQueryRepositoryBean queryRepositoryBeanFrom = (TQueryRepositoryBean)labelBeanFrom;
		TQueryRepositoryBean queryRepositoryBeanTo = (TQueryRepositoryBean)labelBeanTo;
		if (queryRepositoryBeanFrom!=null) {
			Integer clobIDFrom = queryRepositoryBeanFrom.getQueryKey();
			if (clobIDFrom!=null) {
				TCLOBBean clobBeanFrom = ClobBL.loadByPrimaryKey(clobIDFrom);
				if (clobBeanFrom!=null) {
					TCLOBBean clobBeanTo = new TCLOBBean();
					clobBeanTo.setCLOBValue(clobBeanFrom.getCLOBValue());
					Integer clobIDTo = ClobBL.save(clobBeanTo);
					queryRepositoryBeanTo.setQueryKey(clobIDTo);
				}
			}
			queryRepositoryBeanTo.setQueryType(queryRepositoryBeanFrom.getQueryType());
		}
	}
	
	/**
	 * Get the subtype the next unique name is looked by
	 * @param labelBeanFrom
	 * @return
	 */
	@Override
	public Integer getSubtype(ILabelBean labelBeanFrom) {
		TQueryRepositoryBean queryRepositoryBeanFrom = (TQueryRepositoryBean)labelBeanFrom;
		if (queryRepositoryBeanFrom!=null) {
			return queryRepositoryBeanFrom.getQueryType();
		}
		return null;
	}	
	
	/**
	 * Deletes an object by key (and also the eventual descendants if not leaf)
	 * @param objectID
	 * @param categoryType
	 * @return
	 */
	@Override
	public void delete(Integer objectID, String categoryType) {
		queryRepositoryDAO.delete(objectID);
		LocalizeBL.removeLocalizedResources(new TQueryRepositoryBean().getKeyPrefix(), objectID);
	}
	
	/**
	 * Gets the detail json for a leaf
	 * @param includeHeader whether to include the header part (name and other settings)
	 * @param includePathPicker whether to include the patch picker
	 * @param objectID
	 * @param filterType ItemNavigatorBL.QUERY_TYPE
	 * @param queryContextID
	 * @param add
	 * @param modifiable
	 * @param instant
	 * @param clearFilter
	 * @param applyInstant instantly applied from item navigator upper part
	 * @param personBean
	 * @param projectID
	 * @param locale
	 * @return
	 */
	public abstract String getDetailJSON(boolean includeHeader, boolean includePathPicker, Integer objectID, Integer filterType, Integer queryContextID, boolean add,
			boolean modifiable, boolean instant, boolean clearFilter, boolean applyInstant,
			TPersonBean personBean, Integer projectID, Locale locale);
	
	/**
	 * Loads the filter expression by clobID
	 * @param clobID	
	 * @return
	 */
	public String loadFilterExpression(Integer clobID) {
		if (clobID!=null) {
			TCLOBBean clobBean = ClobBL.loadByPrimaryKey(clobID);
			if (clobBean!=null) {
				String filterExpression = clobBean.getCLOBValue();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("The raw filter extression for clobID " + clobID + " is " + filterExpression);
				}
				return filterExpression;
			}
		}
		return null;
	}
	
	
	/**
	 * Gets the specific expression
	 * It is preferable to be declared as abstract to force the implementation in subclasses
	 * but then the class should be also abstract and then it can't be instantiated 
	 * @param tqlExpression
	 * @param filterSelectsTO
	 * @param fieldExpressionsInTree
	 * @return
	 */
	public abstract String getFilterExpression(String tqlExpression, FilterUpperTO filterSelectsTO,
			List<FieldExpressionInTreeTO> fieldExpressionsInTree) throws Exception;
		
}
