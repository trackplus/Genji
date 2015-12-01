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

package com.aurel.track.persist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;

import com.aurel.track.admin.customize.category.CategoryBL;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.dao.QueryRepositoryDAO;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.itemNavigator.ItemNavigatorBL.QUERY_TYPE;
import com.aurel.track.itemNavigator.lastExecuted.LastExecutedBL;
import com.aurel.track.util.GeneralUtils;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TQueryRepositoryPeer
	extends com.aurel.track.persist.BaseTQueryRepositoryPeer
	implements QueryRepositoryDAO
{
	public static final long serialVersionUID = 400L;
	
	private static final Logger LOGGER = LogManager.getLogger(TQueryRepositoryPeer.class);
	
	/**
	 * replace the automail filter settings
	 */
	private static Class[] replacePeerClasses = {
		TNotifySettingsPeer.class
	};
	
	private static String[] replaceFields = {
		TNotifySettingsPeer.NOTIFYFILTER
	};
		
	private static Class[] deletePeerClassesWithoutNotifySettings = {
		TMenuitemQueryPeer.class, //probably not needed (automail filter)
		//TLastExecutedQueryPeer.class,
		//use the superclass doDelete() method!!!
		BaseTQueryRepositoryPeer.class
	};
	
	private static String[] deleteFieldsWithoutNotifySettings = {
		TMenuitemQueryPeer.QUERYKEY, //probably not needed (automail filter)
		//TLastExecutedQueryPeer.QUERYKEY,
		BaseTQueryRepositoryPeer.OBJECTID
	};
	
	private static Class[] deletePeerClassesWithNotifySettings = {
		TNotifySettingsPeer.class, //probably not needed (item filter)
		TMenuitemQueryPeer.class,
		//TLastExecutedQueryPeer.class,
		//use the superclass doDelete() method!!!
		BaseTQueryRepositoryPeer.class
	};
	
	private static String[] deleteFieldsWithNotifySettings = {
		TNotifySettingsPeer.NOTIFYFILTER,  //probably not needed (item filter)
		TMenuitemQueryPeer.QUERYKEY,
		//TLastExecutedQueryPeer.QUERYKEY,
		BaseTQueryRepositoryPeer.OBJECTID
	};
	
	/**
	 * Loads a queryRepositoryBean by primary key
	 */
	@Override
	public TQueryRepositoryBean loadByPrimaryKey(Integer objectID) {
		TQueryRepository tQueryRepository = null;
		try {
			tQueryRepository = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.debug("Loading of a queryRepository by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
		if (tQueryRepository!=null) {
			return tQueryRepository.getBean();
		}
		return null;
	}
	
	/**
	 * Loads a queryRepositoryBean by primary keys
	 * @param objectIDs
	 * @return
	 */
	@Override
	public List<TQueryRepositoryBean> loadByPrimaryKeys(List<Integer> objectIDs) {
		List<TQueryRepositoryBean> filterBeansList = new ArrayList<TQueryRepositoryBean>();
		if (objectIDs==null || objectIDs.isEmpty()) {
			return filterBeansList;
		}
		List<int[]> filterIDChunksList = GeneralUtils.getListOfChunks(objectIDs);
		if (filterIDChunksList==null) {
			return filterBeansList;
		}
		Iterator<int[]> iterator = filterIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] filterIDChunk = iterator.next();
			Criteria criteria = new Criteria();
			criteria.addIn(OBJECTID, filterIDChunk);
			try {
				filterBeansList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
				LOGGER.error("Loading the filters for " + filterIDChunk.length +
						" failed with " + e.getMessage(), e);
			}
		}
		return filterBeansList;
	}
	
	/**
	 * Loads the hardcoded filters
	 * @return
	 */
	@Override
	public List<TQueryRepositoryBean> loadHardcodedFilters() {
		Criteria criteria = new Criteria();
		criteria.add(OBJECTID, 0, Criteria.LESS_THAN);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch(Exception e) {
			LOGGER.error("Getting the hardcoded filters failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Load the filterCategoryBeans by label
	 * @param repository
	 * @param categoryID
	 * @param projectID
	 * @param personID filter by only if set
	 * @param queryType
	 * @param label
	 * @return
	 */
	@Override
	public List<TQueryRepositoryBean> loadByLabel(Integer repository,Integer categoryID,
			Integer projectID, Integer personID, Integer queryType, String label) {
		List<TQueryRepository> categories = null;
		Criteria criteria = new Criteria();
		criteria.add(REPOSITORYTYPE, repository);
		if (categoryID==null) {
			criteria.add(CATEGORYKEY, (Object)null, Criteria.ISNULL);
		} else {
			criteria.add(CATEGORYKEY, categoryID);
		}
		if (projectID==null) {
			criteria.add(PROJECT, (Object)null, Criteria.ISNULL);
		} else {
			criteria.add(PROJECT, projectID);
		}
		if (personID!=null) {
			criteria.add(PERSON, personID);
		}
		criteria.add(QUERYTYPE, queryType);
		criteria.add(LABEL, label);
		try {
			categories = doSelect(criteria);
		} catch(Exception e) {
			LOGGER.error("Getting filters by repository " + repository
					+ " parent " + categoryID + " project " + projectID + " person " + personID + " queryType " + queryType + " label " + label + " failed with " + e.getMessage(), e);
		}
		return convertTorqueListToBeanList(categories);
	}
	
	/**
	 * Get the root filters
	 * @param repository
	 * @param projectID
	 * @param personID filter by only if set
	 * @param queryTypes
	 * @return
	 */
	@Override
	public List<TQueryRepositoryBean> loadRootFilters(Integer repository, Integer projectID, Integer personID, List<Integer> queryTypes) {		
		Criteria criteria = new Criteria();		
		criteria.add(REPOSITORYTYPE, repository);
		if (queryTypes!=null && !queryTypes.isEmpty()) {
			criteria.addIn(QUERYTYPE, queryTypes);
		}
		criteria.add(CATEGORYKEY, (Object)null, Criteria.ISNULL);
		if (projectID==null) {
			criteria.add(PROJECT, (Object)null, Criteria.ISNULL);
		} else {
			criteria.add(PROJECT, projectID);
		}
		if (personID!=null) {
			criteria.add(PERSON, personID);
		}
		criteria.addAscendingOrderByColumn(LABEL);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch(Exception e) {
			LOGGER.error("Getting filters by repository " + repository + " project " + projectID + " person " + personID +
					 " queryTypes " + queryTypes.size() + " failed with " + e.getMessage(), e);
			return null;
		}
	}
		
	/**
	 * Get the root filters for the projects
	 * @param projectIDs
	 * @param queryTypes
	 * @return
	 */
	@Override
	public List<TQueryRepositoryBean> loadProjectRootFilters(List<Integer> projectIDs, List<Integer> queryTypes) {
		List<TQueryRepositoryBean> filters = new LinkedList<TQueryRepositoryBean>();	
		if (projectIDs==null || projectIDs.isEmpty()) {
			return filters;
		}
		List<int[]> projectIDChunksList = GeneralUtils.getListOfChunks(projectIDs);
		if (projectIDChunksList!=null && !projectIDChunksList.isEmpty()) {
			Iterator<int[]> iterator = projectIDChunksList.iterator();
			while (iterator.hasNext()) {
				int[] projectIDsChunk = iterator.next();
				Criteria criteria = new Criteria();
				criteria.add(REPOSITORYTYPE, CategoryBL.REPOSITORY_TYPE.PROJECT);
				if (queryTypes!=null && !queryTypes.isEmpty()) {
					criteria.addIn(QUERYTYPE, queryTypes);
				}
				criteria.add(CATEGORYKEY, (Object)null, Criteria.ISNULL);
				criteria.addIn(PROJECT, projectIDsChunk);
				criteria.addAscendingOrderByColumn(LABEL);
				try {
					filters.addAll(convertTorqueListToBeanList(doSelect(criteria)));
				} catch(Exception e) {
					LOGGER.error("Getting root project filters by projects " + projectIDsChunk.length + 
							 " queryTypes " + queryTypes.size() + " failed with " + e.getMessage(), e);
				}
			}
		}
		return filters;
	}
	
	/**
	 * Loads the queries for a category
	 * @param categoryID
	 * @return
	 */
	@Override
	public List<TQueryRepositoryBean> loadByCategory(Integer categoryID) {
		List<TQueryRepository> queries = null;
		Criteria criteria = new Criteria();
		criteria.add(CATEGORYKEY, categoryID);
		try {
			queries = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the queries by category " + categoryID +
					" failed with " + e.getMessage(), e);
		}
		return convertTorqueListToBeanList(queries);
	}
	
	/**
	 * Loads the queries by parent categories
	 * @param categoryIDs
	 * @return
	 */
	@Override
	public List<TQueryRepositoryBean> loadByCategories(List<Integer> categoryIDs) {
		List<TQueryRepositoryBean> filters = new LinkedList<TQueryRepositoryBean>();
		if (categoryIDs==null || categoryIDs.isEmpty()) {
			return filters;
		}
		List<int[]> parentIDChunksList = GeneralUtils.getListOfChunks(categoryIDs);
		if (parentIDChunksList!=null && !parentIDChunksList.isEmpty()) {
			Iterator<int[]> iterator = parentIDChunksList.iterator();
			while (iterator.hasNext()) {
				int[] parentIDsChunk = iterator.next();
				Criteria criteria = new Criteria();
				criteria.addIn(CATEGORYKEY, parentIDsChunk);
				criteria.addAscendingOrderByColumn(LABEL);
				try {
					filters.addAll(convertTorqueListToBeanList(doSelect(criteria)));
				} catch(Exception e) {
					LOGGER.error("Getting the queries by categoryies " + categoryIDs.size() + 
							" failed with " + e.getMessage(), e);
				}
			}
		}
		return filters;
	}
	
	/**
	 * Loads a list of queryRepositoryBean
	 * @param queryType report or notification
	 * @param repositoryType private, public or project
	 * @param entityID a personID or a projectID depending on the repository type
	 * @return
	 */
	@Override
	public List<TQueryRepositoryBean> loadQueries(Integer queryType, Integer repositoryType, Integer entityID) {
		List queries = new ArrayList();
		if (queryType == null || repositoryType == null)
			return queries;
		Criteria criteria = new Criteria();
		criteria.add(QUERYTYPE, queryType);
		criteria.add(REPOSITORYTYPE, repositoryType);
		switch (repositoryType.intValue()) {
		case TQueryRepositoryBean.REPOSITORY_TYPE.PRIVATE:
			criteria.add(PERSON, entityID);
			break;
		case TQueryRepositoryBean.REPOSITORY_TYPE.PROJECT:
			criteria.add(PROJECT, entityID);
			break;
		case TQueryRepositoryBean.REPOSITORY_TYPE.PUBLIC:
			if (entityID != null)
			criteria.add(PERSON, entityID);
			break;
		}
		criteria.addAscendingOrderByColumn(LABEL);
		try {
			queries = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the queries by queryType " + queryType
				 + " repositoryType " + repositoryType + " entity "
				 + entityID + " failed with " + e.getMessage(), e);
		}
		return convertTorqueListToBeanList(queries);
	}
	
	/**
	 * Loads a list of private queryRepositoryBean report tree queries which are included in menus 
	 * @return
	 */
	@Override
	public List<TQueryRepositoryBean> loadPrivateReportQueriesInMenu() {
		List queries = new ArrayList();
		Criteria criteria = new Criteria();
		criteria.add(QUERYTYPE, TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER);
		criteria.add(REPOSITORYTYPE, TQueryRepositoryBean.REPOSITORY_TYPE.PRIVATE);
		criteria.add(MENUITEM, BooleanFields.TRUE_VALUE);
		try {
			queries = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the private report tree queries failed with " + e.getMessage());
		}
		return convertTorqueListToBeanList(queries);
	}
	
	
	@Override
	public List<TQueryRepositoryBean> loadMenuitemQueries(Integer personID, int[] queryTypes, boolean sorted) {
		Criteria criteria = new Criteria();
		criteria.addIn(QUERYTYPE, queryTypes);
		criteria.addJoin(OBJECTID, TMenuitemQueryPeer.QUERYKEY);
		criteria.add(TMenuitemQueryPeer.INCLUDEINMENU, BooleanFields.TRUE_VALUE);
		criteria.add(TMenuitemQueryPeer.PERSON, personID);
		if(sorted){
			criteria.addAscendingOrderByColumn(LABEL);
		}
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the menuitem queries failed with " + e.getMessage());
			return null;
		}		
	}
	
	/**
	 * Loads a list of queryRepositoryBeans set for any project: 
	 * for the personID (own) or those not linked to any person (default)
	 * @param personID
	 * @return
	 */
	@Override
	public List<TQueryRepositoryBean> loadByOwnOrDefaultNotifySettings(Integer personID) {
		Criteria criteria = new Criteria();
		criteria.addJoin(OBJECTID, BaseTNotifySettingsPeer.NOTIFYFILTER);
		Criterion personCrit = criteria.getNewCriterion(BaseTNotifySettingsPeer.PERSON, personID, Criteria.EQUAL);
		Criterion defaultCrit = criteria.getNewCriterion(BaseTNotifySettingsPeer.PERSON, (Object)null, Criteria.ISNULL);
		criteria.add(personCrit.or(defaultCrit));
		criteria.setDistinct();
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading of the queryRepositoryBeans with notify assignments " +
					"either as default or by person " + personID + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads a list of queryRepositoryBeans set for any project 
	 * which is not linked to any person (default)
	 * @return
	 */
	@Override
	public List<TQueryRepositoryBean> loadByDefaultNotifySettings() {
		Criteria criteria = new Criteria();
		criteria.addJoin(OBJECTID, BaseTNotifySettingsPeer.NOTIFYFILTER);
		criteria.add(BaseTNotifySettingsPeer.PERSON, (Object)null, Criteria.ISNULL);
		criteria.setDistinct();
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading of the queryRepositoryBeans " +
					"having default notify assignments failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Saves a queryRepositoryBean in the database
	 * @param queryRepositoryBean 
	 * @return
	 */
	@Override
	public Integer save(TQueryRepositoryBean queryRepositoryBean) {
		TQueryRepository tQueryRepository;
		try {
			tQueryRepository = BaseTQueryRepository.createTQueryRepository(queryRepositoryBean);
			tQueryRepository.save();
			return tQueryRepository.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a query failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Deletes a queryRepositoryBean
	 * This is called by directly deleting a query  
	 * @param objectID 
	 * @return
	 */
	@Override
	public void delete(Integer objectID) {
		Criteria crit = new Criteria();
		crit.add(OBJECTID, objectID);
		doDelete(crit);
	}
	
	/**
	 * Delete all private tree queries of a person
	 * @param personID
	 */
	@Override
	public void deletePrivateTreeQueries(Integer personID) {
		Criteria criteria = new Criteria();
		criteria.add(PERSON, personID);
		criteria.add(REPOSITORYTYPE, TQueryRepositoryBean.REPOSITORY_TYPE.PRIVATE);
		doDelete(criteria);
	}
	
	/**
	 * Deletes the TQueryRepositoryBean satisfying a certain criteria 
	 * together with the dependent database entries 
	 * @param crit
	 */
	public static void doDelete(Criteria crit) {
		List<TQueryRepository> list = null;
		try {
			list = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Getting the list of TQueryRepositorys to be deleted failed with " + e.getMessage());
		}
		if (list == null || list.size() < 1) {
			return;
		}
		for (TQueryRepository queryRepository : list) {
			Integer filterID = queryRepository.getObjectID();
			ReflectionHelper.delete(deletePeerClassesWithNotifySettings, deleteFieldsWithNotifySettings, filterID);
			new TCLOBPeer().delete(filterID);
			LastExecutedBL.deleteByFilterIDAndFilterType(filterID, QUERY_TYPE.SAVED);
			new TReportLayoutPeer().removeByQueryPerson(null, QUERY_TYPE.SAVED, queryRepository.getObjectID());
		}
	}
	
	/**
	 * Returns wheteher a queryRepositoryBean has dependent data
	 * @param objectID 
	 * @return
	 */
	@Override
	public boolean hasDependentData(Integer objectID) {
		return ReflectionHelper.hasDependentData(replacePeerClasses, replaceFields, objectID); 
	}
	
	/**
	 * Replaces all occurrences of the oldID with the newID and then deletes the oldID queryRepositoryBean
	 * @param oldID
	 * @param newID
	 * @return
	 */
	@Override
	public void replaceAndDelete(Integer oldID, Integer  newID) {
		if (newID!=null) {
			ReflectionHelper.replace(replacePeerClasses, replaceFields, oldID, newID);
		}
		ReflectionHelper.delete(deletePeerClassesWithoutNotifySettings, deleteFieldsWithoutNotifySettings, oldID);
	}
	
	/**
	 * Converts a list of TQueryRepository torque objects to a list of TQueryRepositoryBean objects 
	 * @param torqueList
	 * @return
	 */
	private static List<TQueryRepositoryBean> convertTorqueListToBeanList(List<TQueryRepository> torqueList) {
		List<TQueryRepositoryBean> beanList = new ArrayList<TQueryRepositoryBean>();
		if (torqueList!=null) {
			for (TQueryRepository tQueryRepository : torqueList) {
				beanList.add(tQueryRepository.getBean());
			}
		}
		return beanList;
	}
}
