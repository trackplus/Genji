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

package com.aurel.track.admin.customize.category.filter.execute;


import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.category.filter.FilterFacade;
import com.aurel.track.admin.customize.category.filter.TreeFilterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanExpandContext;
import com.aurel.track.report.execute.ReportBeans;

/**
 * Facade for executing a filter (Tree, TQLPlus or TQL)
 * @author Tamas Ruff
 */
public abstract class FilterExecuterFacade {
	
	/**
	 * Gets the filter expression by filterID
	 * @param filterID
	 * @return
	 */
	public static TQueryRepositoryBean getQueryRepositoryBean(Integer filterID) {
		FilterFacade filterFacade = TreeFilterFacade.getInstance();
		return (TQueryRepositoryBean)filterFacade.getByKey(filterID);
	}
	
	/**
	 * Gets the filter expression by filterID
	 * @param queryRepositoryBean
	 * @return
	 */
	public static Integer getFilterType(TQueryRepositoryBean queryRepositoryBean) {
		if (queryRepositoryBean!=null) {
			return queryRepositoryBean.getQueryType();
		}
		return null;
	}
	
	/**
	* Gets the list of workItemBeans for a filterExpression
	* @param filterExpression
	* @param filterID TODO remove the filterID parameter as soon as filter's tree part is compiled into criteria
	* @param locale
	* @param personBean
	* @param errors
	* @param withCustomAttributes
	* @param projectID
	* @param entityFlag
	* @return
	* @throws TooManyItemsToLoadException
	*/
	public abstract List<TWorkItemBean> getInstantFilterWorkItemBeans(String filterExpression, Integer filterID, Locale locale, 
			TPersonBean personBean, List<ErrorData> errors, boolean withCustomAttributes,
			Integer projectID, Integer entityFlag, Date startDate, Date endDate) throws TooManyItemsToLoadException; 

	/**
	* Gets the list of workItemBeans for a saved filter
	* @param filterID
	* @param locale
	* @param personBean
	* @param errors
	* @param withCustomAttributes
	* @return
	* @throws TooManyItemsToLoadException 
	*/
	public static List<TWorkItemBean> getSavedFilterWorkItemBeans(Integer filterID, Locale locale, TPersonBean personBean, 
			List<ErrorData> errors, boolean withCustomAttributes) throws TooManyItemsToLoadException {
		return getSavedFilterWorkItemBeans(filterID,locale,personBean,errors,withCustomAttributes,null,null, null, null);
	}
	
	/**
	 * Gets the workitemBeans for a filter
	 * @param filterID
	 * @param locale
	 * @param personBean
	 * @param errors
	 * @param withCustomAttributes
	 * @param projectID
	 * @param entityFlag
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	public static List<TWorkItemBean> getSavedFilterWorkItemBeans(Integer filterID, Locale locale,
		  TPersonBean personBean, List<ErrorData> errors, boolean withCustomAttributes,
		  Integer projectID, Integer entityFlag, Date startDate, Date endDate) throws TooManyItemsToLoadException {
		TQueryRepositoryBean queryRepositoryBean = getQueryRepositoryBean(filterID); 
		String filterExpression = FilterBL.getFilterExpression(queryRepositoryBean);
		if (filterExpression!=null) {
			Integer filterType = getFilterType(queryRepositoryBean);
			FilterExecuterFacade filterExecuterFacade = FilterExecuterFacadeFactory.getInstance().getFilterExecuterFacade(filterType);
			if (filterExecuterFacade!=null) {
				return filterExecuterFacade.getInstantFilterWorkItemBeans(filterExpression, filterID,
					locale, personBean, errors, withCustomAttributes, projectID, entityFlag, startDate, endDate);
			}
		}
		return new LinkedList<TWorkItemBean>();
	}
	   
	/**
	* Gets the list of ReportBeans for a filterExpression
	* @param filterExpression
	* @param filterID TODO remove the filterID parameter as soon as filter's tree part is compiled into criteria
	* @param locale
	* @param personBean
	* @param errors
	* @param projectID
	* @param entityFlag
	* @param withParents whether the parents should be loaded to set the summary flag (only for XML based reports)
	* @return
	* @throws TooManyItemsToLoadException
	*/
	public abstract List<ReportBean> getInstantFilterReportBeanList(String filterExpression, Integer filterID,
			Locale locale, TPersonBean personBean, List<ErrorData> errors, Integer projectID, Integer entityFlag, boolean withParents) throws TooManyItemsToLoadException;
	
	/**
	* Gets the list of ReportBeans for a filterExpression loading only the necessary associated entities
	* @param filterExpression
	* @param filterID TODO remove the filterID parameter as soon as filter's tree part is compiled into criteria
	* @param locale
	* @param personBean
	* @param errors
	* @param projectID
	* @param entityFlag
	* @return
	* @throws TooManyItemsToLoadException
	*/
	public abstract List<ReportBean> getInstantFilterReportBeanList(String filterExpression, Integer filterID,
			Locale locale, TPersonBean personBean, boolean editFlagNeeded, List<ErrorData> errors, Integer projectID, Integer entityFlag,
			boolean withCustomAttributes, boolean withWatchers,
			boolean withMyExpenses, boolean withTotalExpenses,
			boolean withBudgetPlan, boolean withRemainingPlan,
			boolean withAttachment, boolean withLinks, boolean withParents) throws TooManyItemsToLoadException;
	
	/**
	* Gets the list of ReportBeans for a saved filter
	* @param filterID
	* @param locale
	* @param personBean
	* @param errors
	* @param projectID
	* @param entityFlag
	* @return
	* @throws TooManyItemsToLoadException 
	*/
	public static List<ReportBean> getSavedFilterReportBeanList(Integer filterID, Locale locale, 
			TPersonBean personBean, List<ErrorData> errors, Integer projectID, Integer entityFlag) throws TooManyItemsToLoadException {
		return getSavedFilterReportBeanList(filterID, locale, personBean, errors, projectID, entityFlag, false);
	}
	
	/**
	 * Gets the list of ReportBeans for a saved filter
	 * @param filterID
	 * @param locale
	 * @param personBean
	 * @param errors
	 * @param projectID
	 * @param entityFlag
	 * @param withParents
	 * @return
	 * @throws TooManyItemsToLoadException 
	 */
	public static List<ReportBean> getSavedFilterReportBeanList(Integer filterID, Locale locale, 
			TPersonBean personBean, List<ErrorData> errors, Integer projectID, Integer entityFlag, boolean withParents) throws TooManyItemsToLoadException {
		TQueryRepositoryBean queryRepositoryBean = getQueryRepositoryBean(filterID); 
		String filterExpression = FilterBL.getFilterExpression(queryRepositoryBean);
		if (filterExpression!=null) {
			Integer filterType = getFilterType(queryRepositoryBean);
			FilterExecuterFacade filterExecuterFacade = FilterExecuterFacadeFactory.getInstance().getFilterExecuterFacade(filterType);
			if (filterExecuterFacade!=null) {
				return filterExecuterFacade.getInstantFilterReportBeanList(filterExpression, filterID, locale, personBean, errors, projectID, entityFlag, withParents);
			}
		}
		return new LinkedList<ReportBean>();
	}
	
	/**
	 * Gets the list of ReportBeans for a saved filter loading only the necessary associated entities
	 * @param filterID
	 * @param locale
	 * @param personBean
	 * @param errors
	 * @param projectID
	 * @param entityFlag
	 * @param withCustomAttributes
	 * @param withWatchers
	 * @param withMyExpenses
	 * @param withTotalExpenses
	 * @param withBudgetPlan
	 * @param withRemainingPlan
	 * @param withAttachment
	 * @param withLinks
	 * @param withParents
	 * @return
	 * @throws TooManyItemsToLoadException 
	 */
	public static List<ReportBean> getSavedFilterReportBeanList(Integer filterID, Locale locale, 
			TPersonBean personBean, boolean editFlagNeeded, List<ErrorData> errors, Integer projectID, Integer entityFlag,
			boolean withCustomAttributes, boolean withWatchers,
			boolean withMyExpenses, boolean withTotalExpenses,
			boolean withBudgetPlan, boolean withRemainingPlan,
			boolean withAttachment, boolean withLinks, boolean withParents) throws TooManyItemsToLoadException {
		TQueryRepositoryBean queryRepositoryBean = getQueryRepositoryBean(filterID); 
		String filterExpression = FilterBL.getFilterExpression(queryRepositoryBean);
		if (filterExpression!=null) {
			Integer filterType = getFilterType(queryRepositoryBean);
			FilterExecuterFacade filterExecuterFacade = FilterExecuterFacadeFactory.getInstance().getFilterExecuterFacade(filterType);
			if (filterExecuterFacade!=null) {
				return filterExecuterFacade.getInstantFilterReportBeanList(
						filterExpression, filterID, locale, personBean, editFlagNeeded, errors, projectID, entityFlag,
						withCustomAttributes, withWatchers,
						withMyExpenses, withTotalExpenses,
						withBudgetPlan, withRemainingPlan,
						withAttachment, withLinks, withParents);
			}
		}
		return new LinkedList<ReportBean>();
	}
	
	/**
	* Gets the ReportBeans object for a filterExpression
	* @param filterExpression
	* @param filterID TODO remove the filterID parameter as soon as filter's tree part is compiled into criteria
	* @param locale
	* @param personBean
	* @param errors
	* @param reportBeanExpandContext
	* @param projectID
	* @param entityFlag
	* @param withParents whether the parents should be loaded to set the summary flag (only for XML based reports)
	* @return
	* @throws TooManyItemsToLoadException
	*/   
	public abstract ReportBeans getInstantFilterReportBeans(String filterExpression, Integer filterID, Locale locale, TPersonBean personBean, 
			List<ErrorData> errors, ReportBeanExpandContext reportBeanExpandContext, Integer projectID, Integer entityFlag, boolean withParents) throws TooManyItemsToLoadException;
	
	/**
	* Gets the ReportBeans object for a saved filter
	* @param filterID
	* @param locale
	* @param personBean
	* @param errors
	* @param reportBeanExpandContext
	* @param projectID
	* @param entityFlag
	* @return
	* @throws TooManyItemsToLoadException 
	*/   
	public static ReportBeans getSavedFilterReportBeans(Integer filterID, Locale locale, TPersonBean personBean, 
			List<ErrorData> errors, ReportBeanExpandContext reportBeanExpandContext, Integer projectID, Integer entityFlag) throws TooManyItemsToLoadException {
		return getSavedFilterReportBeans(filterID, locale, personBean, errors, reportBeanExpandContext, projectID, entityFlag, false);
	}
	
	/**
	* Gets the ReportBeans object for a saved filter
	* @param filterID
	* @param locale
	* @param personBean
	* @param errors
	* @param reportBeanExpandContext
	* @param projectID
	* @param entityFlag
	* @return
	* @throws TooManyItemsToLoadException 
	*/   
	public static ReportBeans getSavedFilterReportBeans(Integer filterID, Locale locale, TPersonBean personBean, 
			List<ErrorData> errors, ReportBeanExpandContext reportBeanExpandContext, Integer projectID, Integer entityFlag, boolean withParents) throws TooManyItemsToLoadException {
		TQueryRepositoryBean queryRepositoryBean = getQueryRepositoryBean(filterID); 
		String filterExpression = FilterBL.getFilterExpression(queryRepositoryBean);
		if (filterExpression!=null) {
			Integer filterType = getFilterType(queryRepositoryBean);
			FilterExecuterFacade filterExecuterFacade = FilterExecuterFacadeFactory.getInstance().getFilterExecuterFacade(filterType);
			if (filterExecuterFacade!=null) {
				return filterExecuterFacade.getInstantFilterReportBeans(filterExpression, filterID, locale,
						personBean, errors, reportBeanExpandContext, projectID, entityFlag, withParents);
			}
		}
		return new ReportBeans(new LinkedList<ReportBean>(), locale, reportBeanExpandContext, true);
	}
}
