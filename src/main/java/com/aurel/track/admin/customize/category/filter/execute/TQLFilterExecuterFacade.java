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

package com.aurel.track.admin.customize.category.filter.execute;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.util.Criteria;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTQLFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanExpandContext;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.tql.TqlBL;

/**
 * Facade implementation for for executing a TQL filter
 * @author Tamas Ruff
 *
 */
public class TQLFilterExecuterFacade extends FilterExecuterFacade {
	private static final Logger LOGGER = LogManager.getLogger(TQLFilterExecuterFacade.class);
	private static TQLFilterExecuterFacade instance;
	
	/**
	 * Return a FieldConfigItemFacade instance which implements the ConfigItemFacade 
	 * @return
	 */
	public static TQLFilterExecuterFacade getInstance(){
		if(instance==null){
			instance=new TQLFilterExecuterFacade();
		}
		return instance;
	}
	
	/**
	 *  Gets the list of workItemBeans for a filterExpression
	 */
	@Override
	public  List<TWorkItemBean> getInstantFilterWorkItemBeans(String filterExpression, Integer filterID,  Locale locale,
		TPersonBean personBean, List<ErrorData> errors, boolean withCustomAttributes,
		Integer projectID, Integer entityFlag, Date startDate, Date endDate) throws TooManyItemsToLoadException {
		if (filterExpression==null) {
			return new ArrayList<TWorkItemBean>();
		}		
		Criteria crit=TqlBL.createCriteria(filterExpression, personBean, locale, errors);
		if (crit==null && !errors.isEmpty()) {
			return  new ArrayList<TWorkItemBean>();
		}
		if (crit != null) {
			LOGGER.debug("The calculated criteria is: " + crit.toString());
		} else {
			LOGGER.debug("The calculated criteria is null");
		}
		return LoadTQLFilterItems.getTQLFilterWorkItemBeans(filterExpression, personBean, errors, locale, withCustomAttributes,startDate,endDate);
	}
	
	/**
	 * Gets the list of ReportBeans for a filterExpression
	 * @param filterExpression
	 * @param filterID
	 * @param locale
	 * @param personBean
	 * @param errors
	 * @param projectID
	 * @param entityFlag
	 * @param withParents
	 * @return
	 */
	@Override
	public List<ReportBean> getInstantFilterReportBeanList(String filterExpression, Integer filterID, Locale locale, 
			TPersonBean personBean, List<ErrorData> errors, Integer projectID, Integer entityFlag, boolean withParents) {
		return LoadTQLFilterItems.getTQLFilterReportBeans(filterExpression, 
				personBean, errors, locale, projectID, entityFlag); 
	}
	
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
	*/
	@Override
	public List<ReportBean> getInstantFilterReportBeanList(String filterExpression, Integer filterID,
			Locale locale, TPersonBean personBean, boolean editFlagNeeded, List<ErrorData> errors, Integer projectID, Integer entityFlag,
			boolean withCustomAttributes, boolean withWatchers,
			boolean withMyExpenses, boolean withTotalExpenses,
			boolean withBudgetPlan, boolean withRemainingPlan,
			boolean withAttachment, boolean withLinks, boolean withParents) {
		return LoadTQLFilterItems.getTQLFilterReportBeans(
				filterExpression, personBean, errors, locale, editFlagNeeded, projectID, entityFlag,
				withCustomAttributes, withWatchers,
				withMyExpenses, withTotalExpenses,
				withBudgetPlan, withRemainingPlan,
				withAttachment, withLinks, withParents);
		
	}
	
	/**
	 * Gets the ReportBeans object for a filterExpression
	 * @param filterExpression
	 * @param locale
	 * @param personBean
	 * @param errors
	 * @param reportBeanExpandContext
	 * @param projectID
	 * @param entityFlag
	 * @param withParents
	 * @return
	 */	   
	public ReportBeans getInstantFilterReportBeans(String filterExpression, Integer filterID, Locale locale, 
			TPersonBean personBean, List<ErrorData> errors,
			ReportBeanExpandContext reportBeanExpandContext,
			Integer projectID, Integer entityFlag, boolean withParents) {
		LOGGER.debug("getTQLReportBeans() projectID="+projectID+", entityFlag="+entityFlag);
		ReportBeans reportBeans=null;
		Criteria crit=TqlBL.createCriteria(filterExpression, personBean, locale, errors);
		if (crit==null && !errors.isEmpty()) {
			return reportBeans;
		}
		if (crit != null) {
			LOGGER.debug("The calculated criteria is: " + crit.toString());
		} else {
			LOGGER.debug("The calculated criteria is null");
		}
		reportBeans=query(filterExpression, locale, personBean, errors,
				reportBeanExpandContext, projectID, entityFlag);
		return reportBeans;
	}
	
	private static ReportBeans query(String filterExpression, Locale locale, 
			TPersonBean personBean, List<ErrorData> errors,
			ReportBeanExpandContext reportBeanExpandContext, Integer projectID,Integer entityFlag){		
		//get the items from the database according to the tqlCriteria
		List<ReportBean> reportBeansList = LoadTQLFilterItems.getTQLFilterReportBeans(filterExpression, personBean, errors, locale, projectID, entityFlag);						 
		 //create the ReportBeans from the item list
		ReportBeans reportBeans = new ReportBeans(reportBeansList, locale, reportBeanExpandContext, false);
		//no sorting needed for the TQL result because 
		//it might be sorted at the database level
		reportBeans.setSortNeeded(false);
		return reportBeans;
	}
	
	
}
