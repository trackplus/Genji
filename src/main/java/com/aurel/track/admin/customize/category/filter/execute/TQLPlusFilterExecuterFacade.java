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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanExpandContext;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.tql.TqlBL;

/**
 * Facade implementation for for executing a TQLPlus (lucene) filter
 * @author Tamas Ruff
 *
 */
public class TQLPlusFilterExecuterFacade extends FilterExecuterFacade {
	
	private static final Logger LOGGER = LogManager.getLogger(TQLPlusFilterExecuterFacade.class);
	private static TQLPlusFilterExecuterFacade instance;
	
	/**
	 * Return a FieldConfigItemFacade instance which implements the ConfigItemFacade 
	 * @return
	 */
	public static TQLPlusFilterExecuterFacade getInstance(){
		if(instance==null){
			instance=new TQLPlusFilterExecuterFacade();
		}
		return instance;
	}
	
	/**
	 * Gets the list of workItemBeans for a filterExpression
	 * @param filterExpression
	 * @param locale
	 * @param personBean
	 * @param errors 
	 * @param withCustomAttributes 
	 * @return
	 */
	/*@Override
	public List<TWorkItemBean> getInstantFilterWorkItemBeans(String filterExpression, Locale locale, 
			TPersonBean personBean, List<ErrorData> errors, boolean withCustomAttributes, Integer projectID, Integer entityFlag) {
		return  getInstantFilterWorkItemBeans(filterExpression, locale, personBean, errors, withCustomAttributes, projectID, entityFlag, null, null);
	}*/
	
	@Override
	public List<TWorkItemBean> getInstantFilterWorkItemBeans(String filterExpression, Integer filterID, Locale locale,
			TPersonBean personBean, List<ErrorData> errors, boolean withCustomAttributes,
			Integer projectID, Integer entityFlag, Date startDate, Date endDate) throws TooManyItemsToLoadException {
		if (filterExpression==null) {
			return new LinkedList<TWorkItemBean>();
		}
		Integer personID = personBean.getObjectID();
		int[] workItemIDs = TqlBL.luceneQuery(filterExpression, false, locale, null, errors);
		return LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(workItemIDs,personID, true, withCustomAttributes, false);
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
		Integer personID = personBean.getObjectID();
		int[] workItemIDs = TqlBL.luceneQuery(filterExpression, false, locale, null, errors);
		return LoadItemIDListItems.getReportBeansByWorkItemIDs(workItemIDs, true,
			personID, locale, projectID, entityFlag);
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
		Integer personID = personBean.getObjectID();
		int[] workItemIDs = TqlBL.luceneQuery(filterExpression, false, locale, null, errors);
		return LoadItemIDListItems.getReportBeansByWorkItemIDs(workItemIDs, editFlagNeeded, personID, locale, projectID, entityFlag,
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
	@Override
	public ReportBeans getInstantFilterReportBeans(String filterExpression, Integer filterID, Locale locale,
			TPersonBean personBean, List<ErrorData> errors,
			ReportBeanExpandContext reportBeanExpandContext, Integer projectID, Integer entityFlag, boolean withParents){
		LOGGER.debug("getTQLPlusReportBeans() projectID="+projectID+", entityFlag="+entityFlag);
		Integer personID = personBean.getObjectID();
		int[] workItemIDs = TqlBL.luceneQuery(filterExpression, false, locale, null, errors);
		List<ReportBean> reportBeanList = LoadItemIDListItems.getReportBeansByWorkItemIDs(workItemIDs, true,
				personID, locale, projectID, entityFlag);
		return new ReportBeans(reportBeanList, locale, reportBeanExpandContext, true);
		
	}
}
