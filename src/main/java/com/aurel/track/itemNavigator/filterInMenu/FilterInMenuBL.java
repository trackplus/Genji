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

package com.aurel.track.itemNavigator.filterInMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.filter.ClobBL;
import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.beans.TBasketBean;
import com.aurel.track.beans.TCLOBBean;
import com.aurel.track.beans.TDashboardFieldBean;
import com.aurel.track.beans.TLastExecutedQueryBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.item.operation.BasketBL;
import com.aurel.track.itemNavigator.ItemNavigatorBL.QUERY_TYPE;
import com.aurel.track.itemNavigator.lastExecuted.LastExecutedBL;
import com.aurel.track.util.GeneralUtils;

/**
 * Logic for actualizing the my filters and my lst executed filters in item navigator menu
 * @author Tamas
 *
 */
public class FilterInMenuBL {
	private static final Logger LOGGER = LogManager.getLogger(FilterInMenuBL.class);
	
	
	/**
	 * Gets the last executed queries for a person
	 * @param personID
	 * @param locale
	 * @return
	 */
	public static List<FilterInMenuTO> getLastExecutedQueries(TPersonBean personBean, Locale locale){
		Integer personID = personBean.getObjectID();
		List<TLastExecutedQueryBean> lastQueries=LastExecutedBL.loadByPerson(personID);
		List<FilterInMenuTO> result=new ArrayList<FilterInMenuTO>();
		if (lastQueries!=null) {
			List<Integer> clobIDs = new LinkedList<Integer>();
			Map<Integer, Integer> instantLastExecutedObjectIDToClobIDMap = new HashMap<Integer, Integer>();
			Map<Integer, Integer> savedLastExecutedObjectIDToFilterIDMap = new HashMap<Integer, Integer>();
			Map<Integer, String> savedQueryIDToViewIDMap = new HashMap<Integer, String>();
			Set<Integer> savedQueryIDs = new HashSet<Integer>();
			Set<Integer> dashboardIDs = new HashSet<Integer>();
			Set<Integer> basketIDs = new HashSet<Integer>();
			Set<Integer> projectReleaseIDs = new HashSet<Integer>();
			Map<Integer, Integer> luceneLastExecutedObjectIDToClobIDMap = new HashMap<Integer, Integer>();
			Set<Integer> statusIDs = new HashSet<Integer>();
			for (TLastExecutedQueryBean lastExecutedQueryBean : lastQueries) {
				Integer lastExecutedID = lastExecutedQueryBean.getObjectID();
				Integer queryID = lastExecutedQueryBean.getQueryKey();
				Integer clobID = lastExecutedQueryBean.getQueryClob();
				Integer queryType = lastExecutedQueryBean.getQueryType();
				if (queryType!=null) {
					switch(queryType.intValue()) {
					case QUERY_TYPE.INSTANT:
						if (clobID!=null) {
							instantLastExecutedObjectIDToClobIDMap.put(lastExecutedID, clobID);
							clobIDs.add(clobID);
						}
						break;
					case QUERY_TYPE.SAVED:
						savedLastExecutedObjectIDToFilterIDMap.put(lastExecutedID, queryID);
						savedQueryIDs.add(queryID);
						break;
					case QUERY_TYPE.DASHBOARD:
						dashboardIDs.add(queryID);
						break;
					case QUERY_TYPE.BASKET:
						basketIDs.add(queryID);
						break;
					case QUERY_TYPE.PROJECT_RELEASE:
						projectReleaseIDs.add(queryID);
						break;
					case QUERY_TYPE.LUCENE_SEARCH:
						if (clobID!=null) {
							luceneLastExecutedObjectIDToClobIDMap.put(lastExecutedID, clobID);
							clobIDs.add(clobID);
						}
						break;
					case QUERY_TYPE.STATUS: 
						statusIDs.add(queryID);
						break;
					}
				}
			}
			Map<Integer, TDashboardFieldBean> dashboardMap = new HashMap<Integer, TDashboardFieldBean>();
			if (!dashboardIDs.isEmpty()) {
				List<TDashboardFieldBean> dasboardFields = DAOFactory.getFactory().getDashboardFieldDAO().loadByKeys(dashboardIDs);
				if (dasboardFields!=null) {
					for (TDashboardFieldBean dashboardFieldBean : dasboardFields) {
						dashboardMap.put(dashboardFieldBean.getObjectID(), dashboardFieldBean);
					}
				}
			}
			Map<Integer, TBasketBean> basketMap = new HashMap<Integer, TBasketBean>();
			if (!basketIDs.isEmpty()) {
				List<TBasketBean> basketBeans = BasketBL.loadByPrimaryKeys(basketIDs);
				if (basketBeans!=null) {
					for (TBasketBean basketBean : basketBeans) {
						basketMap.put(basketBean.getObjectID(), basketBean);
					}
				}
			}
			Map<Integer, TQueryRepositoryBean> savedFilterMap = new HashMap<Integer, TQueryRepositoryBean>();
			Map<Integer, Integer>  savedLastExecutedObjectIDToClobIDMap = new HashMap<Integer, Integer>();
			if (!savedQueryIDs.isEmpty()) {
				List<TQueryRepositoryBean> savedFilterBeans = FilterBL.loadByPrimaryKeys(GeneralUtils.createIntegerListFromCollection(savedQueryIDs));
				Map<Integer, Integer> savedQueryIDToClobIDMap = new HashMap<Integer, Integer>();
				if (savedFilterBeans!=null) {
					for (TQueryRepositoryBean queryRepositoryBean : savedFilterBeans) {
						Integer filterID = queryRepositoryBean.getObjectID();
						Integer clobKey = queryRepositoryBean.getQueryKey();
						clobIDs.add(clobKey);
						savedQueryIDToViewIDMap.put(filterID, queryRepositoryBean.getViewID());
						savedQueryIDToClobIDMap.put(filterID, clobKey);
						savedFilterMap.put(filterID, queryRepositoryBean);
					}
				}
				for (Map.Entry<Integer, Integer> lastExecutedToFilterIDEntry : savedLastExecutedObjectIDToFilterIDMap.entrySet()) {
					Integer lastExecutedObjectID = lastExecutedToFilterIDEntry.getKey();
					Integer filterID = lastExecutedToFilterIDEntry.getValue();
					if (savedQueryIDToClobIDMap.containsKey(filterID)) {
						savedLastExecutedObjectIDToClobIDMap.put(lastExecutedObjectID, savedQueryIDToClobIDMap.get(filterID));
					}
				}
			}
			List<TCLOBBean> clobBeans = ClobBL.loadByPrimaryKeys(clobIDs);
			Map<Integer, String> clobIDToClobValueMap = new HashMap<Integer, String>();
			for (TCLOBBean clobBean : clobBeans) {
				clobIDToClobValueMap.put(clobBean.getObjectID(), clobBean.getCLOBValue());
			}
			Map<Integer, String> lastExecutedObjectIDToClobValueMap = new HashMap<Integer, String>();
			appendLastExecutedIDToClobValue(instantLastExecutedObjectIDToClobIDMap, clobIDToClobValueMap, lastExecutedObjectIDToClobValueMap);
			appendLastExecutedIDToClobValue(savedLastExecutedObjectIDToClobIDMap, clobIDToClobValueMap, lastExecutedObjectIDToClobValueMap);
			appendLastExecutedIDToClobValue(luceneLastExecutedObjectIDToClobIDMap, clobIDToClobValueMap, lastExecutedObjectIDToClobValueMap);
			for (TLastExecutedQueryBean lastExecutedQuery : lastQueries) {
				Integer lastExecutedQueryID = lastExecutedQuery.getObjectID();
				Integer queryID=lastExecutedQuery.getQueryKey();
				Integer queryType=lastExecutedQuery.getQueryType();
				Object entityBean = null;
				if (queryType!=null) {
					switch(queryType.intValue()) {
					case QUERY_TYPE.SAVED:
						entityBean = savedFilterMap.get(queryID);
						break;
					case QUERY_TYPE.DASHBOARD:
						entityBean = dashboardMap.get(queryID);
						break;
					case QUERY_TYPE.BASKET:
						entityBean = basketMap.get(queryID);
						break;
					}
				}
				String filterExpression = lastExecutedObjectIDToClobValueMap.get(lastExecutedQueryID);
				try {
					IFilterInMenu filterInMenu = FilterInMenuFactory.getInstance().getFilterInMenuFacade(queryType, queryID);
					String label = filterInMenu.getLabel(entityBean, filterExpression, locale);
					String tooltip = filterInMenu.getTooltip(entityBean, filterExpression, locale);
					if (tooltip==null) {
						tooltip = label;
					}
					String icon = filterInMenu.getIcon();
					String iconCls = null;
					if (icon==null) {
						iconCls = filterInMenu.getIconCls(entityBean);
					}
					FilterInMenuTO filterInMenuTO = new FilterInMenuTO();
					filterInMenuTO.setObjectID(queryID);
					filterInMenuTO.setLabel(label);
					filterInMenuTO.setTooltip(tooltip);
					filterInMenuTO.setType(queryType);
					filterInMenuTO.setIcon(icon);
					filterInMenuTO.setIconCls(iconCls);
					filterInMenuTO.setLastExecutedQueryID(lastExecutedQueryID);
					String viewID = savedQueryIDToViewIDMap.get(queryID);
					if (viewID==null) {
						filterInMenuTO.setMaySaveFilterLayout(false);
					} else {
						filterInMenuTO.setViewID(viewID);
						filterInMenuTO.setMaySaveFilterLayout(filterInMenu.maySaveFilterWithViewLayout(entityBean, personBean));
					}
					result.add(filterInMenuTO);
				} catch (Exception ex) {
					LOGGER.info("Loading the last executed query " + queryID + " of type " + queryType + " failed with " + ex.getMessage());
					if (LOGGER.isDebugEnabled()) {
						LOGGER.error(ExceptionUtils.getStackTrace(ex));
					}
				}
			}
		}
		return result;
	}

	/**
	 * Appends the lastExecutedID to clob value based on two intermediary maps
	 * @param lastExecutedObjectIDToClobIDMap
	 * @param clobIDToClobValueMap
	 * @param lastExecutedObjectIDToClobValueMap
	 */
	private static void appendLastExecutedIDToClobValue(Map<Integer, Integer> lastExecutedObjectIDToClobIDMap,
			Map<Integer, String> clobIDToClobValueMap,
			Map<Integer, String> lastExecutedObjectIDToClobValueMap) {
		for (Map.Entry<Integer, Integer> lastExecutedObjectIDToClobID : lastExecutedObjectIDToClobIDMap.entrySet()) {
			Integer lastExecutedObjectID = lastExecutedObjectIDToClobID.getKey();
			Integer clobID = lastExecutedObjectIDToClobID.getValue();
			if (clobIDToClobValueMap.containsKey(clobID)) {
				lastExecutedObjectIDToClobValueMap.put(lastExecutedObjectID, clobIDToClobValueMap.get(clobID));
			}
		}
	}
}
