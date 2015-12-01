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

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.util.Criteria;

import com.aurel.track.util.GeneralUtils;

public class ReflectionHelper {

	private static final Logger LOGGER = LogManager.getLogger(ReflectionHelper.class); 
	
	/**
	 * This method replaces all occurrences of 
	 * oldOID with newOID going through all related
	 * tables in the database.
	 * @param oldOID object identifier to be replaced
	 * @param newOID object identifier of replacement
	 */
	public static void replace(Class[] peerClasses, String[] fields, Integer oldOID, Integer newOID) {
		Criteria selectCriteria = new Criteria();
		Criteria updateCriteria = new Criteria();
		for (int i = 0; i < peerClasses.length; ++i) {
			Class peerClass = peerClasses[i];
			String field = fields[i];
			selectCriteria.clear();
			updateCriteria.clear();
			selectCriteria.add(field, oldOID, Criteria.EQUAL);
			updateCriteria.add(field, newOID);
			try {
				Class partypes[] = new Class[2];
				partypes[0] = Criteria.class;
				partypes[1] = Criteria.class;
				Method meth = peerClass.getMethod("doUpdate", partypes);
				
				Object arglist[] = new Object[2];
				arglist[0] = selectCriteria;
				arglist[1] = updateCriteria;
				meth.invoke(peerClass, arglist);
			}
			catch (Exception e) {
				LOGGER.error(
						"Exception when trying to replace " +
						"oldOID " + oldOID + " with " +
						"newOID " + newOID + " for class " + peerClass.toString() + " and field " + field +  ": " + e.getMessage(), e);
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
	}
	
	/**
	 * This method sets to null all occurrences of oldOID
	 * going through all related tables in the database.
	 * @param oldOID object identifier to be replaced 
	 */
	public static void setToNull(Class[] peerClasses, String[] fields, Integer oldOID) {
		Criteria selectCriteria = new Criteria();
		Criteria updateCriteria = new Criteria();
		for (int i = 0; i < peerClasses.length; ++i) {
			Class peerClass = peerClasses[i];
			String field = fields[i];
			selectCriteria.clear();
			updateCriteria.clear();
			selectCriteria.add(field, oldOID, Criteria.EQUAL);
			updateCriteria.add(field, (Object)null, Criteria.ISNULL);
			try {
				Class partypes[] = new Class[2];
				partypes[0] = Criteria.class;
				partypes[1] = Criteria.class;
				Method meth = peerClass.getMethod("doUpdate", partypes);
				
				Object arglist[] = new Object[2];
				arglist[0] = selectCriteria;
				arglist[1] = updateCriteria;
				meth.invoke(peerClass, arglist);
			}
			catch (Exception e) {
				LOGGER.error(
						"Exception when trying to set " +
						"oldOID " + oldOID + " to null " +
						" for class " + peerClass.toString() + " and field " + field +  ": " + e.getMessage(), e);
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
	}
	
	/**
	 * This method replaces all occurrences of <code>ProjectType</code> 
	 * value oldOID with project type value newOID going through all related
	 * tables in the database.
	 * @param oldOID object identifier of list type to be replaced
	 * @param newOID object identifier of replacement list type
	 */
	public static boolean hasDependentData(Class[] peerClasses, String[] fields, Integer oldOID) {
		// Do this using reflection.
		Criteria selectCriteria = new Criteria();
		for (int i = 0; i < peerClasses.length; ++i) {
			Class peerClass = peerClasses[i];
			String field = fields[i];
			selectCriteria.clear();
			selectCriteria.add(field, oldOID, Criteria.EQUAL);
			try {
				Class partypes[] = new Class[1];
				partypes[0] = Criteria.class;
				Method meth = peerClass.getMethod("doSelect", partypes);
				
				Object arglist[] = new Object[1];
				arglist[0] = selectCriteria;
				List results =  (List)meth.invoke(peerClass, arglist);
				if (results!=null && !results.isEmpty())
				{
					return true;
				}
			}
			catch (Exception e) {
				LOGGER.error(
						"Exception when trying to find dependent data for " +
						"oldOID " + oldOID +
						" for class " + peerClass.toString() + " and field " + field +  ": " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return false;
	}
	
	/**
	 * This method replaces all occurrences of <code>ProjectType</code> 
	 * value oldOID with project type value newOID going through all related
	 * tables in the database.
	 * @param oldOID object identifier of list type to be replaced
	 * @param newOID object identifier of replacement list type
	 */
	public static boolean hasDependentData(Class[] peerClasses, String[] fields, List<Integer> oldOIDs) {
		// Do this using reflection.
		if (oldOIDs==null || oldOIDs.isEmpty()) {
			return false;
		}
		Criteria selectCriteria;
		for (int i = 0; i < peerClasses.length; ++i) {
			Class peerClass = peerClasses[i];
			String field = fields[i];
			List<int[]> chunkList = GeneralUtils.getListOfChunks(oldOIDs);
			Iterator<int[]> iterator = chunkList.iterator();
			while (iterator.hasNext()) {
				int[] oIDsChunk = iterator.next();
				selectCriteria = new Criteria();
				selectCriteria.addIn(field, oIDsChunk);
				try {
					Class partypes[] = new Class[1];
					partypes[0] = Criteria.class;
					Method meth = peerClass.getMethod("doSelect", partypes);
					Object arglist[] = new Object[1];
					arglist[0] = selectCriteria;
					List results =  (List)meth.invoke(peerClass, arglist);
					if (results!=null && !results.isEmpty()) {
						return true;
					}
				} catch (Exception e) {
					LOGGER.error(
							"Exception when trying to find dependent data for " +
							"oldOIDs " + oldOIDs.size() +
							" for class " + peerClass.toString() + " and field " + field +  ": " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
		return false;
	}
	
	/**
	 * This method replaces all occurrences of <code>ProjectType</code> 
	 * value oldOID with project type value newOID going through all related
	 * tables in the database.
	 * @param oldOID object identifier of list type to be replaced
	 * @param newOID object identifier of replacement list type
	 */
	public static void delete(Class[] peerClasses, String[] fields, Integer oldOID) {
		// Do this using reflection.		
		Criteria selectCriteria = new Criteria();
		for (int i = 0; i < peerClasses.length; ++i) {
			Class peerClass = peerClasses[i];
			String field = fields[i];
			selectCriteria.clear();
			selectCriteria.add(field, oldOID, Criteria.EQUAL);
			try {
				Class partypes[] = new Class[1];
				partypes[0] = Criteria.class;
				Method meth = peerClass.getMethod("doDelete", partypes);
				Object arglist[] = new Object[1];
				arglist[0] = selectCriteria;
				meth.invoke(peerClass, arglist);
			}
			catch (Exception e) {
				LOGGER.error("Exception when trying to delete dependent data for oldOID " + oldOID +
						" class " + peerClass.toString() + " and field " + field +  ": " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
	}
	
}
