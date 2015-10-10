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

package com.aurel.track.itemNavigator.lastExecuted;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.filter.ClobBL;
import com.aurel.track.beans.TCLOBBean;
import com.aurel.track.beans.TLastExecutedQueryBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.LastExecutedQueryDAO;
import com.aurel.track.itemNavigator.ItemNavigatorBL.QUERY_TYPE;
import com.aurel.track.itemNavigator.QueryContext;

/**
 * Logic for last executed queries
 * @author Tamas
 *
 */
public class LastExecutedBL {
	private static final Logger LOGGER = LogManager.getLogger(LastExecutedBL.class);
	
	private static LastExecutedQueryDAO lastExecutedQueryDAO = DAOFactory.getFactory().getLastExecutedQueryDAO();
	private static final int LAST_EXECUTED_QUERY_COUNT=20;
	/**
	 * Loads the last executed query by primary key
	 * @param objectID
	 * @return
	 */
	public static TLastExecutedQueryBean loadByPrimaryKey(Integer objectID) {
		return lastExecutedQueryDAO.loadByPrimaryKey(objectID);
	}
	
	/**
	 * Load last executed filters by person
	 * @param personID
	 * @return
	 */
	public static List<TLastExecutedQueryBean> loadByPerson(Integer personID) {
		return lastExecutedQueryDAO.loadByPerson(personID);
	}
	
	/**
	 * @param personID
	 * @return
	 */
	public static TLastExecutedQueryBean loadLastByPerson(Integer personID) {
		return lastExecutedQueryDAO.loadLastByPerson(personID);
	}
	
	/**
	 * Save  screenField in the TScreenField table
	 * @param lastExecutedQueryBean
	 * @return
	 */
	public static Integer save(TLastExecutedQueryBean lastExecutedQueryBean) {
		return lastExecutedQueryDAO.save(lastExecutedQueryBean);
	}


	/**
	 * Deletes a screenField from the TScreenField table
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	public static void delete(Integer objectID) {
		lastExecutedQueryDAO.delete(objectID);
	}
	
	/**
	 * Deletes the last executed quieries by filterID and filterType
	 */
	public static void deleteByFilterIDAndFilterType(Integer filterID, Integer filterType) {
		lastExecutedQueryDAO.deleteByFilterIDAndFilterType(filterID, filterType);
	}
	
	/**
	 * Deletes the last executed quieries by filterID and filterType
	 */
	public static void deleteByFilterIDAndFilterType(Integer filterID, Integer filterType, Connection con) {
		lastExecutedQueryDAO.deleteByFilterIDAndFilterType(filterID, filterType, con);
	}
	
	public static Integer storeLastExecutedQuery(Integer personID,QueryContext queryContext){
		Integer queryType=queryContext.getQueryType();
		Integer queryID=queryContext.getQueryID();
		Integer queryContextID=queryContext.getId();
		String filterExpression = queryContext.getFilterExpression(); 
		LOGGER.debug("Store last query; personID="+personID+", queryType="+queryType+", queryID="+queryID + ", filterExpression="+filterExpression);
		List<TLastExecutedQueryBean> lastExecutedQueryBeanList=LastExecutedBL.loadByPerson(personID);
		TLastExecutedQueryBean lastExecutedQueryBean=null;
		if(queryType==QUERY_TYPE.INSTANT){
			if(queryContextID!=null){
				lastExecutedQueryBean=findLastQuery(lastExecutedQueryBeanList,queryContextID);
			}
		}else{
			lastExecutedQueryBean=lastQueryAlreadyExecuted(lastExecutedQueryBeanList,queryContext);
		}
		//old clobs to remove
		Integer oldClobID =null;
		if(lastExecutedQueryBean==null) {
			//only one instant query is allowed in the last executed query history
			if(lastExecutedQueryBeanList!=null&&lastExecutedQueryBeanList.size()>=LAST_EXECUTED_QUERY_COUNT){
				TLastExecutedQueryBean queryBeanToRemove=lastExecutedQueryBeanList.get(lastExecutedQueryBeanList.size()-1);
				oldClobID = queryBeanToRemove.getQueryClob();
				LOGGER.debug("The number of queries for person "+personID+" is >="+LAST_EXECUTED_QUERY_COUNT+". LastExecutedQuery to remove "+queryBeanToRemove.getObjectID()+":"+queryBeanToRemove.getQueryType()+":"+queryBeanToRemove.getQueryKey());
				LastExecutedBL.delete(queryBeanToRemove.getObjectID());
			}
			lastExecutedQueryBean=queryContextToQueryBean(queryContext, personID);
		}else{
			if(queryType==QUERY_TYPE.DASHBOARD){
				Map<String,String> dashParams=queryContext.getDashboardParams();
				oldClobID=lastExecutedQueryBean.getQueryClob();
				TCLOBBean clobBean = new TCLOBBean();
				if (dashParams!=null && !dashParams.isEmpty()){
					clobBean.setCLOBValue(encodeDashboardParams(dashParams));
				}
				Integer newQueryClobID=ClobBL.save(clobBean);
				lastExecutedQueryBean.setQueryClob(newQueryClobID);
			}
		}
		if(filterExpression!=null){
			Integer clobID=lastExecutedQueryBean.getQueryClob();
			clobID=saveFilterExpression(clobID,filterExpression);
			lastExecutedQueryBean.setQueryClob(clobID);
		}
		queryContextID=LastExecutedBL.save(lastExecutedQueryBean);
		//remove the old clob only now, as dependency to this entry do not exist any more
		if (oldClobID!=null){
			ClobBL.delete(oldClobID);
		}
		queryContext.setId(queryContextID);
		return queryContextID;
	}
	
	private static TLastExecutedQueryBean findLastQuery(List<TLastExecutedQueryBean> lastExecutedQueryBeanList,Integer objectID){
		if(lastExecutedQueryBeanList==null||lastExecutedQueryBeanList.isEmpty()){
			return null;
		}
		for(int i=0;i<lastExecutedQueryBeanList.size();i++){
			TLastExecutedQueryBean lastExecutedQueryBean=lastExecutedQueryBeanList.get(i);
			if(lastExecutedQueryBean.getObjectID().equals(objectID)){
					return lastExecutedQueryBean;
			}
		}
		return null;
	}
	
	private static TLastExecutedQueryBean lastQueryAlreadyExecuted(List<TLastExecutedQueryBean> lastExecutedQueryBeanList,QueryContext queryContext){
		if(lastExecutedQueryBeanList==null||lastExecutedQueryBeanList.isEmpty()){
			return null;
		}
		Integer queryType=queryContext.getQueryType();
		Integer queryID=queryContext.getQueryID();
		for(int i=0;i<lastExecutedQueryBeanList.size();i++){
			TLastExecutedQueryBean lastExecutedQueryBean=lastExecutedQueryBeanList.get(i);
			Integer dbQueryType= lastExecutedQueryBean.getQueryType();
			Integer dbQueryID=lastExecutedQueryBean.getQueryKey();
			if(dbQueryType.equals(queryType)){
				if(dbQueryID==null&&queryID==null||
						(dbQueryID!=null&&dbQueryID.equals(queryID))){
					return lastExecutedQueryBean;
				}
			}
		}
		return null;
	}
	
	private static TLastExecutedQueryBean queryContextToQueryBean(QueryContext queryContext,Integer personID){
		TLastExecutedQueryBean lastExecutedQueryBean=new TLastExecutedQueryBean();
		lastExecutedQueryBean.setQueryType(queryContext.getQueryType());
		lastExecutedQueryBean.setQueryKey(queryContext.getQueryID());
		lastExecutedQueryBean.setPerson(personID);
		Integer queryClobID=null;
		switch (queryContext.getQueryType()){
			case QUERY_TYPE.INSTANT:
				String filterExpression=queryContext.getFilterExpression();
				if (filterExpression!=null) {
					TCLOBBean clobBean = new TCLOBBean();
					clobBean.setCLOBValue(filterExpression);
					queryClobID=ClobBL.save(clobBean);
					lastExecutedQueryBean.setQueryClob(queryClobID);
				}
				break;
			/*case QUERY_TYPE.SAVED:
				Integer queryID=queryContext.getQueryID();
				if (queryID!=null) {
					lastExecutedQueryBean.setQueryKey(queryID);					
				}*/
			case QUERY_TYPE.DASHBOARD:
				Map<String,String> dashParams=queryContext.getDashboardParams();
				if(dashParams!=null&&!dashParams.isEmpty()){
					TCLOBBean clobBean = new TCLOBBean();
					clobBean.setCLOBValue(encodeDashboardParams(dashParams));
					queryClobID=ClobBL.save(clobBean);
					lastExecutedQueryBean.setQueryClob(queryClobID);
				}
				break;
		}		
		return lastExecutedQueryBean;
	}
	
	private static String encodeDashboardParams(Map<String,String> dashParams){
		Properties properties=new Properties();
		properties.putAll(dashParams);
		StringWriter writer = new StringWriter();
		properties.list(new PrintWriter(writer));
		return writer.getBuffer().toString();
	}
	
	private static Integer saveFilterExpression(Integer clobID, String filterExpression) {
		TCLOBBean clobBean = null;
		if (clobID!=null) {
			clobBean = ClobBL.loadByPrimaryKey(clobID);
		}
		if (clobBean==null) {
			clobBean = new TCLOBBean();
		}
		clobBean.setCLOBValue(filterExpression);
		return ClobBL.save(clobBean);
	}

}
