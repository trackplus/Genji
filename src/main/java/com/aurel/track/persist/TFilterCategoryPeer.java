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

import com.aurel.track.admin.customize.category.CategoryBL;
import com.aurel.track.beans.TFilterCategoryBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.dao.FilterCategoryDAO;
import com.aurel.track.util.GeneralUtils;

/**
 * The persons who can or can not run a report template
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TFilterCategoryPeer
    extends com.aurel.track.persist.BaseTFilterCategoryPeer 
    implements FilterCategoryDAO
{
	private static final Logger LOGGER = LogManager.getLogger(TFilterCategoryPeer.class);	
	
	public static final long serialVersionUID = 400L;
	
	private static Class[] deletePeerClasses = {
    	TQueryRepositoryPeer.class,
    	TFilterCategoryPeer.class,
    	BaseTFilterCategoryPeer.class
    };
    
    private static String[] deleteFields = {
    	TQueryRepositoryPeer.CATEGORYKEY,
    	TFilterCategoryPeer.PARENTID,
    	BaseTFilterCategoryPeer.OBJECTID,
    };
    
    private static Class[] dependentDataClasses = {
    	TQueryRepositoryPeer.class,
    	TFilterCategoryPeer.class,    	
    };
    
    private static String[] dependentDataFields = {
    	TQueryRepositoryPeer.CATEGORYKEY,
    	TFilterCategoryPeer.PARENTID,    	
    };
    
	/**
	 * Loads a filterCategoryBean by primary key
	 * @param objectID
	 * @return
	 */
	public TFilterCategoryBean loadByPrimaryKey(Integer objectID){
		TFilterCategory filterCategory = null;
    	try {
    		filterCategory = retrieveByPK(objectID);    		
    	} catch(Exception e) {
    		LOGGER.warn("Loading of a filterCategory by primary key " + objectID + " failed with " + e.getMessage());
    		LOGGER.debug(ExceptionUtils.getStackTrace(e));
    	} 
    	if (filterCategory!=null) {
			return filterCategory.getBean();
		}
		return null;
	}
	
	/**
	 * Loads the filterCategoryBeans by keys
	 * @param categoryIDs
	 * @return
	 */
	public List<TFilterCategoryBean> loadByKeys(List<Integer> categoryIDs) {
		if (categoryIDs==null || categoryIDs.isEmpty()) {
			return new LinkedList<TFilterCategoryBean>();			
		}
		List<int[]> categoryIDChunksList = GeneralUtils.getListOfChunks(categoryIDs);
		if (categoryIDChunksList==null) {
			return new LinkedList<TFilterCategoryBean>();
		}		
		Iterator<int[]> iterator = categoryIDChunksList.iterator();
		List<TFilterCategory> torqueList = new LinkedList<TFilterCategory>();
		while (iterator.hasNext()) {
			int[] categoryIDChunks = iterator.next();
			Criteria criteria = new Criteria();
			criteria.addIn(OBJECTID, categoryIDChunks);
			try {
				torqueList.addAll(doSelect(criteria));
			} catch(Exception e) {
				LOGGER.error("Loading the filter categories for " + categoryIDChunks.length +  " failed with " + e.getMessage(), e);}			
		}
		return convertTorqueListToBeanList(torqueList);
	}
	
	/**
	 * Load the filterCategoryBeans by label
	 * @param repository
	 * @param filterType
	 * @param parentID
	 * @param projectID
	 * @param personID filter by only if set
	 * @param label
	 * @return
	 */
	public List<TFilterCategoryBean> loadByLabel(Integer repository, Integer filterType,
			Integer parentID, Integer projectID, Integer personID, String label) {
		List<TFilterCategory> categories = null;		
		Criteria criteria = new Criteria();
		criteria.add(REPOSITORY, repository);
		criteria.add(FILTERTYPE, filterType);
		if (parentID==null) {
			criteria.add(PARENTID, (Object)null, Criteria.ISNULL);
		} else {
			criteria.add(PARENTID, parentID);
		}
		if (projectID==null) {
			criteria.add(PROJECT, (Object)null, Criteria.ISNULL);
		} else {
			criteria.add(PROJECT, projectID);
		}
		if (personID!=null) {			
			criteria.add(CREATEDBY, personID);
		}
		criteria.add(LABEL, label);
		try {
			categories = doSelect(criteria);
		} catch(Exception e) {
			LOGGER.error("Getting filter categories by repository " + repository
					+ " parent " + parentID + " project " + projectID +  " person " + personID + " label " + label + " failed with " + e.getMessage(), e);
        }						
		return convertTorqueListToBeanList(categories);
	}
	
	/**
	 * Get the root categories  
	 * @param repository
	 * @param filterType
	 * @param projectID
	 * @param personID
	 * @return
	 */
	public List<TFilterCategoryBean> loadRootCategories(Integer repository, 
			Integer filterType, Integer projectID, Integer personID) {
		List<TFilterCategory> categories = null;
		Criteria criteria = new Criteria();
		criteria.add(REPOSITORY, repository);
		criteria.add(FILTERTYPE, filterType);
		criteria.add(PARENTID, (Object)null, Criteria.ISNULL);
		if (projectID==null) {
			criteria.add(PROJECT, (Object)null, Criteria.ISNULL);
		} else {
			criteria.add(PROJECT, projectID);
		}
		if (personID!=null) {			
			criteria.add(CREATEDBY, personID);
		}
		criteria.addAscendingOrderByColumn(LABEL);
		try {
			categories = doSelect(criteria);
		} catch(Exception e) {
			LOGGER.error("Getting root filter categories by repository " + repository +
					" project " + projectID +  " person " + personID + " failed with " + e.getMessage(), e);
        }
		return convertTorqueListToBeanList(categories);
	}
		
	/**
	 * Get the root categories for projects 
	 * @param projectIDs
	 * @return
	 */
	public List<TFilterCategoryBean> loadProjectRootCategories(List<Integer> projectIDs, Integer filterType) {
		List<TFilterCategoryBean> categories = new LinkedList<TFilterCategoryBean>();				
		if (projectIDs==null || projectIDs.isEmpty()) {
			return categories;
		}
		List<int[]> projectIDChunksList = GeneralUtils.getListOfChunks(projectIDs);   
    	if (projectIDChunksList!=null && !projectIDChunksList.isEmpty()) {
			Iterator<int[]> iterator = projectIDChunksList.iterator();
			while (iterator.hasNext()) {
				int[] projectIDsChunk = iterator.next();
				Criteria criteria = new Criteria();
				criteria.add(REPOSITORY, CategoryBL.REPOSITORY_TYPE.PROJECT);
				criteria.add(FILTERTYPE, filterType);
				criteria.add(PARENTID, (Object)null, Criteria.ISNULL);
				criteria.addIn(PROJECT, projectIDsChunk);
				criteria.addAscendingOrderByColumn(LABEL);
				try {
					categories.addAll(convertTorqueListToBeanList(doSelect(criteria)));
				} catch(Exception e) {
					LOGGER.error("Getting the filter categories by projects " + projectIDsChunk.length + 
							" and filterType " + filterType + " failed with " + e.getMessage(), e);
		        }		
			}
		}
		return categories;
	}
	
	/**
	 * Loads filterCategoryBeans by parent
	 * @param parentID
	 * @return
	 */
	public List<TFilterCategoryBean> loadByParent(Integer parentID) {
		List<TFilterCategory> categories = new ArrayList<TFilterCategory>();		
		Criteria criteria = new Criteria();
		criteria.add(PARENTID, parentID);	
		criteria.addAscendingOrderByColumn(LABEL);				
		try {
			categories = doSelect(criteria);
		} catch(Exception e) {
			LOGGER.error("Getting the filter categories by parent " + parentID + 
					" failed with " + e.getMessage(), e);
        }
		return convertTorqueListToBeanList(categories);
	}
	
	/**
	 * Loads a filterCategoryBeans by parentIDs
	 * @param parentIDs
	 * @return
	 */
	public List<TFilterCategoryBean> loadByParents(List<Integer> parentIDs) {
		List<TFilterCategoryBean> categories = new LinkedList<TFilterCategoryBean>();		
		
		if (parentIDs==null || parentIDs.isEmpty()) {
			return categories;
		}
		List<int[]> parentIDChunksList = GeneralUtils.getListOfChunks(parentIDs);   
    	if (parentIDChunksList!=null && !parentIDChunksList.isEmpty()) {
			Iterator<int[]> iterator = parentIDChunksList.iterator();
			while (iterator.hasNext()) {
				int[] parentIDsChunk = iterator.next();
				Criteria criteria = new Criteria();
				criteria.addIn(PARENTID, parentIDsChunk);	
				criteria.addAscendingOrderByColumn(LABEL);				
				try {
					categories.addAll(convertTorqueListToBeanList(doSelect(criteria)));
				} catch(Exception e) {
					LOGGER.error("Getting the filter categories by parents " + parentIDs.size() + 
							" failed with " + e.getMessage(), e);
		        }		
			}
		}
		return categories;
	}	
	
	/**
	 * Saves a filterCategoryBean in the database
	 * @param filterCategoryBean 
	 * @return
	 */
	public Integer save(TFilterCategoryBean filterCategoryBean) {
		TFilterCategory filterCategory;		
		try {
			filterCategory = BaseTFilterCategory.createTFilterCategory(filterCategoryBean);
			filterCategory.save();
			return filterCategory.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a filterCategory failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
     * This method verifies wheteher <code>ProjectType</code> 
     * is used in other tables in the database.
     * @param oldOID object identifier of list type to be replaced
     */
    public boolean hasDependenData(Integer oldOID) {
        return ReflectionHelper.hasDependentData(dependentDataClasses, dependentDataFields, oldOID);                       
    }
	
	/**
	 * Deletes a filterCategoryBean from the database
	 * @param objectID 
	 * @return
	 */
	public void delete(Integer objectID) {
		ReflectionHelper.delete(deletePeerClasses, deleteFields, objectID);				
	}
	
	/**
	 * Deletes the filterCategoryBean satisfying a certain criteria 
	 * together with the dependent database entries 
	 * @param crit
	 * @throws TorqueException 
	 */
	public static void doDelete(Criteria crit) throws TorqueException  {
		List list = null;
		try {
			list = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Getting the list of TFilterCategoryBean to be deleted failed with " + e.getMessage(), e);
			throw e;
		}			                        
		if (list!=null && !list.isEmpty()) {
			Iterator<TFilterCategory> iter = list.iterator();
			TFilterCategory filterCategory = null;
			while(iter.hasNext()) {
				filterCategory = iter.next();			
				ReflectionHelper.delete(deletePeerClasses, deleteFields, filterCategory.getObjectID());			
			}
		}				
	}
	
	/**
	 * Delete all private tree queries of a person
	 * @param personID
	 */
	public void deletePrivateFilterCategories(Integer personID) {
		Criteria criteria = new Criteria();        
        criteria.add(CREATEDBY, personID);
        criteria.add(REPOSITORY, TQueryRepositoryBean.REPOSITORY_TYPE.PRIVATE);        
        try {
			doDelete(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the private filter categories failed with " +e.getMessage(), e);
		}                      
	}
	
	/**
	 * Converts a list of TFilterCategory torque objects to a list of TFilterCategoryBean objects 
	 * @param torqueList
	 * @return
	 */
	private static List<TFilterCategoryBean> convertTorqueListToBeanList(List<TFilterCategory> torqueList) {		
		List<TFilterCategoryBean> beanList = new ArrayList<TFilterCategoryBean>();
		TFilterCategory filterCategory;
		if (torqueList!=null)
		{
			Iterator<TFilterCategory> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext())
			{
				filterCategory = itrTorqueList.next();
				beanList.add(filterCategory.getBean());
			}
		}
		return beanList;
	}
}
