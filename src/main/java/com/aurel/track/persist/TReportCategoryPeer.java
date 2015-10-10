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
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.admin.customize.category.CategoryBL;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TReportCategoryBean;
import com.aurel.track.dao.ReportCategoryDAO;
import com.aurel.track.util.GeneralUtils;

/**
 * Hierarchical categorization of the reports
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TReportCategoryPeer
	extends com.aurel.track.persist.BaseTReportCategoryPeer implements ReportCategoryDAO {
	private static final Logger LOGGER = LogManager.getLogger(TFilterCategoryPeer.class);	
	
	public static final long serialVersionUID = 400L;
	
	private static Class[] deletePeerClasses = {
		TExportTemplatePeer.class,
		TReportCategoryPeer.class,
		BaseTReportCategoryPeer.class
	};
	
	private static String[] deleteFields = {
		TExportTemplatePeer.CATEGORYKEY,
		TReportCategoryPeer.PARENTID,
		BaseTReportCategoryPeer.OBJECTID,
	};
	
	private static Class[] dependentDataClasses = {
		TExportTemplatePeer.class,
		TReportCategoryPeer.class,
	};
	
	private static String[] dependentDataFields = {
		TExportTemplatePeer.CATEGORYKEY,
		TReportCategoryPeer.PARENTID
	};
	
	/**
	 * Loads a filterCategoryBean by primary key
	 * @param objectID
	 * @return
	 */
	public TReportCategoryBean loadByPrimaryKey(Integer objectID){
		TReportCategory reportCategory = null;
		try {
			reportCategory = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.warn("Loading of a reportCategory by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (reportCategory!=null) {
			return reportCategory.getBean();
		}
		return null;
	}
	
	/**
	 * Load the reportCategoryBean by label
	 * @param repository
	 * @param parentID
	 * @param projectID
	 * @param personID filter by only if set
	 * @param label
	 * @return
	 */
	public List<TReportCategoryBean> loadByLabel(Integer repository,
			Integer parentID, Integer projectID, Integer personID, String label) {
		List<TReportCategory> categories = null;
		Criteria criteria = new Criteria();
		criteria.add(REPOSITORY, repository);
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
			LOGGER.error("Getting report categories by repository " + repository
					+ " parent " + parentID + " project " + projectID +  " person " + personID + " label " + label + " failed with " + e.getMessage(), e);
		}
		return convertTorqueListToBeanList(categories);
	}
	
	/**
	 * Get the root categories  
	 * @param repository
	 * @param projectID
	 * @param personID filter by only if set
	 * @return
	 */
	public List<TReportCategoryBean> loadRootCategories(Integer repository, Integer projectID, Integer personID) {
		List<TReportCategory> categories = null;
		Criteria criteria = new Criteria();
		criteria.add(REPOSITORY, repository);
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
			LOGGER.error("Getting root report categories by repository " + repository +
					" project " + projectID +  " person " + personID + " failed with " + e.getMessage(), e);
		}
		return convertTorqueListToBeanList(categories);
	}
	
	/**
	 * Get the root categories for projects
	 * @param projectIDs
	 * @return
	 */
	public List<TReportCategoryBean> loadProjectRootCategories(List<Integer> projectIDs) {
		List<TReportCategory> categories = new ArrayList<TReportCategory>();
		if (projectIDs==null || projectIDs.isEmpty()) {
			return new ArrayList<TReportCategoryBean>();
		}
		List<int[]> projectIDChunksList = GeneralUtils.getListOfChunks(projectIDs);
		if (projectIDChunksList!=null && !projectIDChunksList.isEmpty()) {
			Iterator<int[]> iterator = projectIDChunksList.iterator();
			while (iterator.hasNext()) {
				int[] projectIDsChunk = iterator.next();
				Criteria criteria = new Criteria();
				criteria.add(REPOSITORY, CategoryBL.REPOSITORY_TYPE.PROJECT);
				criteria.add(PARENTID, (Object)null, Criteria.ISNULL);
				criteria.addIn(PROJECT, projectIDsChunk);
				criteria.addAscendingOrderByColumn(LABEL);
				try {
					categories.addAll(doSelect(criteria));
				} catch(Exception e) {
					LOGGER.error("Getting the report categories by projects " + projectIDsChunk.length + 
							" failed with " + e.getMessage(), e);
				}
			}
		}
		return convertTorqueListToBeanList(categories);
	}
	
	/**
	 * Loads filterCategoryBeans by repository
	 * @param repository
	 * @param personID
	 * @return
	 */
	public List<TReportCategoryBean> loadByRepositoryPersonProjects(
			Integer repository, Integer personID, List<Integer> projects){
		List<TReportCategory> categories = new ArrayList<TReportCategory>();
		Criteria criteria = new Criteria();
		criteria.add(REPOSITORY, repository);
		if (personID!=null) {
			criteria.add(CREATEDBY, personID);
		}
		if (projects!=null && !projects.isEmpty()) {
			criteria.addIn(PROJECT, projects);
		}
		criteria.addAscendingOrderByColumn(LABEL);
			try {
				categories.addAll(doSelect(criteria));
			} catch(Exception e) {
				LOGGER.error("Getting filter categories by repository " + repository + " personID " + personID +  
					 " projects " + projects + " failed with " + e.getMessage(), e);
			}
		return convertTorqueListToBeanList(categories);
	}
	
	/**
	 * Loads reportCategoryBean by parent
	 * @param parentID
	 * @return
	 */
	public List<TReportCategoryBean> loadByParent(Integer parentID) {
		List<TReportCategory> categories = new ArrayList<TReportCategory>();
		Criteria criteria = new Criteria();
		criteria.add(PARENTID, parentID);
		criteria.addAscendingOrderByColumn(LABEL);
		try {
			categories = doSelect(criteria);
		} catch(Exception e) {
			LOGGER.error("Getting the report categories by parent " + parentID + 
					" failed with " + e.getMessage(), e);
		}
		return convertTorqueListToBeanList(categories);
	}
	
	/**
	 * Loads a filterCategoryBeans by parentIDs
	 * @param parentIDs
	 * @return
	 */
	public List<TReportCategoryBean> loadByParents(List<Integer> parentIDs) {
		List<TReportCategory> categories = new ArrayList<TReportCategory>();
		if (parentIDs==null || parentIDs.isEmpty()) {
			return new ArrayList<TReportCategoryBean>();
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
					categories.addAll(doSelect(criteria));
				} catch(Exception e) {
					LOGGER.error("Getting the report categories by parents " + parentIDsChunk.length + 
							" failed with " + e.getMessage(), e);
				}
			}
		}
		return convertTorqueListToBeanList(categories);
	}
	
	/**
	 * Saves a filterCategoryBean in the database
	 * @param filterCategoryBean 
	 * @return
	 */
	public Integer save(TReportCategoryBean reportCategoryBean) {
		TReportCategory filterCategory;
		try {
			filterCategory = BaseTReportCategory.createTReportCategory(reportCategoryBean);
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
	 * Deletes a reportCategoryBean from the database
	 * @param objectID 
	 * @return
	 */
	public void delete(Integer objectID) {
		ReflectionHelper.delete(deletePeerClasses, deleteFields, objectID);
	}
	
	/**
	 * Deletes the reportCategoryBean satisfying a certain criteria 
	 * together with the dependent database entries 
	 * @param crit
	 * @throws TorqueException 
	 */
	public static void doDelete(Criteria crit) throws TorqueException  {
		List list = null;
		try {
			list = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Getting the list of TReportCategoryBean to be deleted failed with " + e.getMessage(), e);
			throw e;
		}
		if (list!=null && !list.isEmpty()) {
			Iterator<TReportCategory> iter = list.iterator();
			TReportCategory filterCategory = null;
			while(iter.hasNext()) {
				filterCategory = iter.next();
				ReflectionHelper.delete(deletePeerClasses, deleteFields, filterCategory.getObjectID());			
			}
		}
	}
	
	/**
	 * Delete all private report categories of a person
	 * @param personID
	 */
	public void deletePrivateReportCategories(Integer personID) {
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
	 * Converts a list of TReportCategory torque objects to a list of TReportCategoryBean objects 
	 * @param torqueList
	 * @return
	 */
	private static List<TReportCategoryBean> convertTorqueListToBeanList(List<TReportCategory> torqueList) {		
		List<TReportCategoryBean> beanList = new ArrayList<TReportCategoryBean>();
		TReportCategory reportCategory;
		if (torqueList!=null) {
			Iterator<TReportCategory> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()) {
				reportCategory = itrTorqueList.next();
				beanList.add(reportCategory.getBean());
			}
		}
		return beanList;
	}
}
