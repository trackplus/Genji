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

import com.aurel.track.admin.customize.category.CategoryBL;
import com.aurel.track.beans.TExportTemplateBean;
import com.aurel.track.dao.ExportTemplateDAO;
import com.aurel.track.dao.torque.SimpleCriteria;
import com.aurel.track.util.GeneralUtils;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TExportTemplatePeer
	extends com.aurel.track.persist.BaseTExportTemplatePeer implements ExportTemplateDAO {

	private static final long serialVersionUID = -5657825832504203511L;
	
	private static final Logger LOGGER = LogManager.getLogger(TExportTemplatePeer.class);

	private static Class[] deletePeerClasses = {
		TTemplatePersonPeer.class,
		TReportPersonSettingsPeer.class,
		TReportSubscribePeer.class,
		TExportTemplatePeer.class
	};
	
	private static String[] deleteFields = {
		TTemplatePersonPeer.REPORTTEMPLATE,
		TReportPersonSettingsPeer.REPORTTEMPLATE,
		TReportSubscribePeer.REPORTTEMPLATE,
		TExportTemplatePeer.OBJECTID
	};
	
	/* Gets a exportTemplateBean from the TExportTemplate table
	 * @param objectID
	 * @return
	 */
	@Override
	public TExportTemplateBean loadByPrimaryKey(Integer objectID) {
		TExportTemplateBean templateBean = null;
		TExportTemplate tobject = null;
		try {
			tobject = retrieveByPK(objectID);
		} catch (TorqueException e) {
			LOGGER.warn("Loading of a ExportTemplate by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (tobject!=null) {
			templateBean=tobject.getBean();
		}
		return templateBean;

	}

	/**
	 * Load the exportTemplateBeans by label
	 * @param repository
	 * @param categoryID
	 * @param projectID
	 * @param personID filter by only if set
	 * @param label
	 * @return
	 */
	@Override
	public List<TExportTemplateBean> loadByLabel(Integer repository, Integer categoryID, Integer projectID, Integer personID, String label) {
		List<TExportTemplate> categories = null;
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
		criteria.add(NAME, label);
		try {
			categories = doSelect(criteria);
		} catch(Exception e) {
			LOGGER.error("Getting reports by repository " + repository
					+ " parent " + categoryID + " project " + projectID + " label " + label + " failed with " + e.getMessage(), e);
		}
		return convertTorqueListToBeanList(categories);
	}
	
	/**
	 * Get the root reports  
	 * @param repository
	 * @param projectID
	 * @param personID filter by only if set
	 * @return
	 */
	@Override
	public List<TExportTemplateBean> loadRootReports(Integer repository, Integer projectID, Integer personID) {
		List<TExportTemplate> categories = null;
		Criteria criteria = new Criteria();
		criteria.add(REPOSITORYTYPE, repository);
		criteria.add(CATEGORYKEY, (Object)null, Criteria.ISNULL);
		if (projectID==null) {
			criteria.add(PROJECT, (Object)null, Criteria.ISNULL);
		} else {
			criteria.add(PROJECT, projectID);
		}
		if (personID!=null) {
			criteria.add(PERSON, personID);
		}
		criteria.addAscendingOrderByColumn(NAME);
		try {
			categories = doSelect(criteria);
		} catch(Exception e) {
			LOGGER.error("Getting filters by repository " + repository + " project " + projectID +
					" person " + personID + " failed with " + e.getMessage(), e);
		}
		return convertTorqueListToBeanList(categories);
	}
	
	/**
	 * Get the root reports  
	 * @param projectIDs
	 * @return
	 */
	@Override
	public List<TExportTemplateBean> loadProjectRootCategories(List<Integer> projectIDs) {
		List<TExportTemplateBean> reports = new LinkedList<TExportTemplateBean>();
		if (projectIDs==null || projectIDs.isEmpty()) {
			return reports;
		}
		List<int[]> projectIDChunksList = GeneralUtils.getListOfChunks(projectIDs);
		if (projectIDChunksList!=null && !projectIDChunksList.isEmpty()) {
			Iterator<int[]> iterator = projectIDChunksList.iterator();
			while (iterator.hasNext()) {
				int[] projectIDsChunk = iterator.next();
				Criteria criteria = new Criteria();
				criteria.add(REPOSITORYTYPE, CategoryBL.REPOSITORY_TYPE.PROJECT);
				criteria.add(CATEGORYKEY, (Object)null, Criteria.ISNULL);
				criteria.addIn(PROJECT, projectIDsChunk);
				criteria.addAscendingOrderByColumn(NAME);
				try {
					reports.addAll(convertTorqueListToBeanList(doSelect(criteria)));
				} catch(Exception e) {
					LOGGER.error("Getting the reports by categoryies " + projectIDsChunk.length + 
							" failed with " + e.getMessage(), e);
				}
			}
		}
		return reports;

	}
	
	/**
	 * Loads the reports without category by repository
	 * @param repository typically public 
	 * @return
	 */
	public List<TExportTemplateBean> loadLeafByRepository(Integer repository) {
		List<TExportTemplate> queries = null;
		Criteria criteria = getLeafCriteria(repository);
		try {
			queries = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the reports by repository " + repository +
					" failed with " + e.getMessage(), e);
		}
		return convertTorqueListToBeanList(queries);
	}
	
	/**
	 * Loads the reports without category by repository and project
	 * @param repository typically project
	 * @param projectID
	 * @return
	 */
	public List<TExportTemplateBean>  loadLeafByRepositoryAndProject(
			Integer repository, Integer projectID) {
		List<TExportTemplate> queries = null;
		Criteria criteria = getLeafCriteria(repository);
		criteria.add(PROJECT, projectID);
		try {
			queries = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the reports by repository " + repository +
					" and project " + projectID + " failed with " + e.getMessage(), e);
		}
		return convertTorqueListToBeanList(queries);
	}
	
	/**
	 * Loads the reports without category by repository and person
	 * @param repository typically private
	 * @param personID
	 * @return
	 */
	public List<TExportTemplateBean>  loadLeafByRepositoryAndPerson(
			Integer repository, Integer personID) {
		List<TExportTemplate> queries = null;
		Criteria criteria = getLeafCriteria(repository);
		criteria.add(PERSON, personID);
		try {
			queries = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the reports by repository " + repository +
					" and person " + personID + " failed with " + e.getMessage(), e);
		}
		return convertTorqueListToBeanList(queries);
	}

	private Criteria getLeafCriteria(Integer repository) {
		Criteria criteria = new Criteria();
		criteria.add(REPOSITORYTYPE, repository);
		criteria.add(CATEGORYKEY, (Object)null, Criteria.ISNULL);
		criteria.addAscendingOrderByColumn(NAME);
		return criteria;
	}
	
	/**
	 * Loads the queries for a category
	 * @param categoryID
	 * @return
	 */
	@Override
	public List<TExportTemplateBean> loadByCategory(Integer categoryID) {
		List<TExportTemplate> queries = null;
		Criteria criteria = new Criteria();
		criteria.add(CATEGORYKEY, categoryID);
		criteria.addAscendingOrderByColumn(NAME);
		try {
			queries = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the reports by category " + categoryID +
					" failed with " + e.getMessage(), e);
		}
		return convertTorqueListToBeanList(queries);
	}
	
	/**
	 * Loads the queries for parent categories
	 * @param categoryIDs
	 * @return
	 */
	@Override
	public List<TExportTemplateBean> loadByCategories(List<Integer> categoryIDs) {
		List<TExportTemplateBean> reports = new LinkedList<TExportTemplateBean>();
		if (categoryIDs==null || categoryIDs.isEmpty()) {
			return reports;
		}
		List<int[]> parentIDChunksList = GeneralUtils.getListOfChunks(categoryIDs);
		if (parentIDChunksList!=null && !parentIDChunksList.isEmpty()) {
			Iterator<int[]> iterator = parentIDChunksList.iterator();
			while (iterator.hasNext()) {
				int[] parentIDsChunk = iterator.next();
				Criteria criteria = new Criteria();
				criteria.addIn(CATEGORYKEY, parentIDsChunk);
				criteria.addAscendingOrderByColumn(NAME);
				try {
					reports.addAll(convertTorqueListToBeanList(doSelect(criteria)));
				} catch(Exception e) {
					LOGGER.error("Getting the reports by categoryies " + parentIDsChunk.length + 
							" failed with " + e.getMessage(), e);
				}
			}
		}
		return reports;
	}
	
	/**
	 * Loads exportTemplateBeans by IDs
	 * @param reportIds
	 * @return
	 */
	@Override
	public List<TExportTemplateBean> loadByIDs(List<Integer> reportIds) {
		if (reportIds!=null && !reportIds.isEmpty()) {
			Criteria crit = new Criteria();
			crit.addIn(OBJECTID, reportIds);
			crit.addAscendingOrderByColumn(NAME);
			crit.addAscendingOrderByColumn(REPOSITORYTYPE);
			try {
				return convertTorqueListToBeanList(doSelect(crit));
			} catch (TorqueException e) {
				LOGGER.error("Loading all exportTemplates by IDs " + 
						reportIds.size() + " failed with " + e.getMessage(), e);
			}
		}
		return new LinkedList<TExportTemplateBean>();
	}

	/**
	 * Loads the templates derived from a parent template
	 * @param parentTemplateID
	 * @return
	 */
	@Override
	public List<TExportTemplateBean> loadDerived(Integer parentTemplateID) {
		Criteria crit = new Criteria();		
		crit.add(PARENT, parentTemplateID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading of all derived templates of the template " + parentTemplateID +  " failed with " + e.getMessage());
			return null;
		}
	}
	
	@Override
	public List<TExportTemplateBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all exportTemplate failed with " + e.getMessage());
			return null;
		}
		
	}

	/**
	 * Loads all templates between two indexes
	 * @return
	 */
	@Override
	public List<TExportTemplateBean> loadFromTo(Integer from, Integer to) {
		SimpleCriteria crit = new SimpleCriteria();
		crit.addIsBetween(OBJECTID, from, to);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all exportTemplates between " + 
					from + " and " + to + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	@Override
	public Integer save(TExportTemplateBean exportTemplate) {
		try {
			TExportTemplate tobject = BaseTExportTemplate.createTExportTemplate(exportTemplate);
			tobject.save();
			return tobject.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a exportTemplate failed with " + e.getMessage());
			return null;
		}
	}
	
	@Override
	public void delete(Integer objectID) {
		ReflectionHelper.delete(deletePeerClasses, deleteFields, objectID);
	}
	
	@Override
	public boolean isDeletable(Integer objectID) {
		return true;
	}

	/**
	 * Delete all private export templates of a person
	 */
	
	@Override
	public List<TExportTemplateBean> loadPrivate(Integer personID) {
		Criteria crit = new Criteria();
		crit.add(REPOSITORYTYPE,new Integer(TExportTemplateBean.REPOSITORY_TYPE.PRIVATE));
		crit.add(PERSON,personID);
		try	{
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(TorqueException e){
			LOGGER.error("Loading private templates by person failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Loads all project specific report templates 
	 * @param projectID
	 * @return
	 */
	@Override
	public List<TExportTemplateBean> loadProject(Integer projectID) {
		Criteria crit = new Criteria();
		crit.add(REPOSITORYTYPE,new Integer(TExportTemplateBean.REPOSITORY_TYPE.PROJECT));
		crit.add(PROJECT, projectID);
		try	{
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(TorqueException e){
			LOGGER.error("Loading private templates by person failed with " + e.getMessage());
			return null;
		}
	}

	private List<TExportTemplateBean> convertTorqueListToBeanList(List<TExportTemplate> torqueList) {
		List<TExportTemplateBean> beanList = new ArrayList<TExportTemplateBean>();
		if (torqueList!=null){
			for (TExportTemplate exportTemplate : torqueList) {
				beanList.add(exportTemplate.getBean());
			}
		}
		return beanList;
	}
}
