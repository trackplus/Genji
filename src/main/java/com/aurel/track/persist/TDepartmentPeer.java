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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TDepartmentBean;
import com.aurel.track.dao.DepartmentDAO;
import com.aurel.track.util.GeneralUtils;

/** 
 */
public class TDepartmentPeer 
	extends com.aurel.track.persist.BaseTDepartmentPeer implements DepartmentDAO
{

	private static final long serialVersionUID = 467577511464925349L;

	private static final Logger LOGGER = LogManager.getLogger(TDepartmentPeer.class);

	static private int DEFAULT_DEPARTMENT_KEY = 10;
	
	private static Class[] replacePeerClasses = {
		TDepartmentPeer.class,
		TPersonPeer.class
	};
	
	private static String[] replaceFields = {
		TDepartmentPeer.PARENT,
		TPersonPeer.DEPKEY
	};
	
	private static Class[] deletePeerClasses = {
		TOrgProjectSLAPeer.class,
		//delete itself
		TDepartmentPeer.class
	};

	private static String[] deleteFields = {
		TOrgProjectSLAPeer.DEPARTMENT,
		//delete itself
		TDepartmentPeer.PKEY
	};
	
	/************************************************************************
	 *************************Bean methods***********************************
	 ************************************************************************/
	
	/**
	 * Loads all DepartmentBeans
	 * @return 
	 */
	@Override
	public List<TDepartmentBean> loadAll() {
		Criteria crit = new Criteria();
		crit.add(PKEY, new Integer(0), Criteria.GREATER_THAN);
		crit.addAscendingOrderByColumn(LABEL);
		try{
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading all departments failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Loads a TDepartmentBean by primary key
	 * @param departmentID
	 * @return
	 */
	@Override
	public TDepartmentBean loadByPrimaryKey(Integer departmentID) {
		TDepartment tDepartment = null;
		try {
			tDepartment = retrieveByPK(departmentID);
		} catch(Exception e) {
			LOGGER.warn("Loading of a department by primary key " + departmentID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
		if (tDepartment!=null) {
			return tDepartment.getBean();
		}
		return null;
	}
	
	/**
	 * Loads a TDepartmentBean by primary keys
	 * @param departmentIDs
	 * @return
	 */
	@Override
	public List<TDepartmentBean> loadByKeys(List<Integer> departmentIDs) {
		List<TDepartmentBean> departmentList = new LinkedList<TDepartmentBean>();
		if (departmentIDs==null || departmentIDs.isEmpty()) {
			return departmentList;
		}
		Criteria crit = null;
		List<int[]> departmentChunkList = GeneralUtils.getListOfChunks(departmentIDs);
		Iterator<int[]> iterator = departmentChunkList.iterator();
		while (iterator.hasNext()) {
			int[] departmentIDChunk = iterator.next();
			crit = new Criteria();
			crit.addIn(PKEY, departmentIDChunk);
			crit.addAscendingOrderByColumn(LABEL);
		}
		try {
			departmentList.addAll(convertTorqueListToBeanList(doSelect(crit)));
		} catch(Exception e) {
			LOGGER.error("Loading the departments by keys " + departmentIDs + " failed with " + e);
		}
		return departmentList;
	}
	
	/**
	 * Loads a TDepartmentBean by name
	 * @param name
	 * @param parentDepartment
	 * @return
	 */
	@Override
	public List<TDepartmentBean> loadByName(String name, Integer parentDepartment) {
		Criteria crit = new Criteria();
		crit.add(LABEL, name);
		if (parentDepartment==null) {
			crit.add(PARENT, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(PARENT, parentDepartment);
		}
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading all departments failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Loads all main departments
	 * @param
	 * @return
	 */
	@Override
	public List<TDepartmentBean> loadMainDepartments() {
		Criteria crit = new Criteria();		
		crit.addAscendingOrderByColumn(LABEL);
		crit.add(PKEY, 0, Criteria.GREATER_THAN);
		crit.add(PARENT, (Object)null, Criteria.ISNULL);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		}
		catch (Exception e) {
			LOGGER.error("Loading of all main departments failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Loads all child departments for a department
	 * @param parentDepartmentID
	 * @return
	 */
	@Override
	public List<TDepartmentBean> loadSubdepartments(Integer parentDepartmentID) {
		Criteria crit = new Criteria();
		crit.addAscendingOrderByColumn(LABEL);
		crit.add(PARENT, parentDepartmentID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading of all subdepartments of the department " + parentDepartmentID +  " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Loads all child departments for a list of parent department
	 * @param departmentIDs
	 * @return
	 */
	@Override
	public List<TDepartmentBean> loadSubdepartments(List<Integer> departmentIDs) {
		List<TDepartmentBean> departmentList = new LinkedList<TDepartmentBean>();
		if (departmentIDs==null || departmentIDs.isEmpty()) {
			return departmentList;
		}
		List<int[]> releaseIDChunksList = GeneralUtils.getListOfChunks(departmentIDs);
		if (releaseIDChunksList==null) {
			return departmentList;
		}
		Iterator<int[]> iterator = releaseIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] departmentIDChunk = iterator.next();
			Criteria criteria = new Criteria();
			criteria.addIn(PARENT, departmentIDChunk);
			criteria.addAscendingOrderByColumn(LABEL);
			try {
				departmentList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
				LOGGER.error("Loading the child departments by main departments " + departmentIDs.size() +
						"  failed with " + e.getMessage(), e);
			}
		}
		return departmentList;
	}
	
	/**
	 * Gets the default department
	 */
	@Override
	public Integer getDefaultDepartment() {
		List<TDepartment> departments = null;
		Criteria crit = new Criteria();
		crit.add(PKEY, DEFAULT_DEPARTMENT_KEY);
		try {
			departments = doSelect(crit);
			if (departments!=null && !departments.isEmpty()) {
				return departments.get(0).getObjectID();
			}
		} catch(Exception e) {
			LOGGER.error("Loading all departments failed with " + e.getMessage());
		}
		crit = new Criteria();
		crit.addDescendingOrderByColumn(PKEY);
		try {
			departments = doSelect(crit);
			if (departments!=null && !departments.isEmpty()) {
				return departments.get(0).getObjectID();
			}
		} catch(Exception e) {
			LOGGER.error("Loading all departments failed with " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Saves a department to the TDepartment table.
	 * @param departmentBean
	 * @return
	 */
	@Override
	public Integer save(TDepartmentBean departmentBean) {
		TDepartment tDepartment;
		try {
			tDepartment = BaseTDepartment.createTDepartment(departmentBean);
			tDepartment.save();
			return tDepartment.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of department failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Whether the department has dependent data
	 * @param departmentID
	 * @return
	 */
	@Override
	public boolean hasDependentData(Integer departmentID) {
		return ReflectionHelper.hasDependentData(replacePeerClasses, replaceFields, departmentID);	  		
	}
	
	/**
	 * Replaces the oldDepartmentID with newDepartmentID departmentID
	 * @param oldDepartmentID
	 * @param newDepartmentID
	 * @return
	 */
	@Override
	public void replace(Integer oldDepartmentID, Integer newDepartmentID) {
		ReflectionHelper.replace(replacePeerClasses, replaceFields, oldDepartmentID, newDepartmentID); 
	}
	
	/**
	 * Deletes a departmentID
	 * @param departmentID
	 * @return
	 */
	@Override
	public void delete(Integer departmentID) {
		ReflectionHelper.delete(deletePeerClasses, deleteFields, departmentID);
	}
	
	private static List<TDepartmentBean> convertTorqueListToBeanList(List<TDepartment> torqueList) {		
		List<TDepartmentBean> beanList = new LinkedList<TDepartmentBean>();
		if (torqueList!=null) {
			for (TDepartment tDepartment : torqueList) {
				beanList.add(tDepartment.getBean());
			}
		}
		return beanList;
	}
}
