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

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TScriptsBean;
import com.aurel.track.dao.ScriptsDAO;

/**
 * This class handles management of Groovy scripts.
 * @since 3.7.0
 *
 */
public class TScriptsPeer
extends com.aurel.track.persist.BaseTScriptsPeer implements ScriptsDAO
{
	private static final long serialVersionUID = 372L;
	private static final Logger LOGGER = LogManager.getLogger(TScriptsPeer.class);
	
	private static Class[] replacePeerClasses = {
		TWorkflowActivityPeer.class,
		TWorkflowGuardPeer.class,
		TFieldConfigPeer.class
	};
	
	private static String[] replaceFields = {
		TWorkflowActivityPeer.GROOVYSCRIPT,
		TWorkflowGuardPeer.GROOVYSCRIPT,
		TFieldConfigPeer.GROOVYSCRIPT
	};
	
	private static Class[] deletePeerClasses = {
		TWorkflowActivityPeer.class,
		TWorkflowGuardPeer.class,
		TFieldConfigPeer.class,
		TScriptsPeer.class
	};
	
	private static String[] deleteFields = {
		TWorkflowActivityPeer.GROOVYSCRIPT,
		TWorkflowGuardPeer.GROOVYSCRIPT,
		TFieldConfigPeer.GROOVYSCRIPT,
		TScriptsPeer.OBJECTID
	};
	
	/**
	 * Loads the script by primary key
	 * @param objectID
	 * @return
	 */
	@Override
	public TScriptsBean loadByPrimaryKey(Integer objectID) {
		TScripts tScript = null;
		try {
			tScript = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.warn("Loading of a script by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
		if (tScript!=null) {
			return tScript.getBean();
		}
		return null;
	}
	
	/**
	 * Loads the script by class name
	 * @param className
	 * @return
	 */
	@Override
	public TScriptsBean loadByClassName(String className) {
		Criteria crit = new Criteria();
		crit.add(CLAZZNAME, className);
		List<TScripts> scriptList = null;
		try {
			scriptList = doSelect(crit);
		} catch (Exception ex) {
			LOGGER.error("Problem fetching Groovy class " + className + ": " + ex.getMessage());
		}
		if (scriptList!=null) {
			LOGGER.debug("Found " + scriptList.size() + " scripts with name " + className);
			if (!scriptList.isEmpty()) {
				return scriptList.get(0).getBean();
			}
		}
		return null;
	}
	
	/**
	 * Get the TScriptsBean by class name
	 * @param className
	 * @param objectID
	 * @return
	 */
	@Override
	public boolean classNameExists(String className, Integer objectID) {
		Criteria crit = new Criteria();
		crit.add(CLAZZNAME, className);
		if (objectID!=null) {
			crit.add(OBJECTID, objectID, Criteria.NOT_EQUAL);
		}
		List<TScripts> codes = null;
		try {
			codes = doSelect(crit);
		} catch (Exception ex) {
			LOGGER.error("Problem fetching Groovy class " + className + ": " + ex.getMessage());
		}
		return codes!=null && !codes.isEmpty();
	}
	
	
	/**
	 * Returns a typed List with all scripts. 
	 * @return List with TScriptsBean beans.
	 */
	@Override
	public List<TScriptsBean> getAllScripts() {
		List<TScripts> scripts = null;
		Criteria crit = new Criteria();
		crit.addAscendingOrderByColumn(CLAZZNAME);
		try {
			scripts = doSelect(crit);
		}
		catch (Exception ex) {
			LOGGER.error("Problem fetching all scripts: " + ex.getMessage());
		}
		return convertTorqueListToBeanList(scripts);
	}
	
	/**
	 * Returns a typed List with all TScriptsBean beans of scriptType 
	 * @return List with TScriptsBean beans.
	 */
	@Override
	public List<TScriptsBean> getScriptsByType(Integer scriptType) {
		Criteria crit = new Criteria();
		crit.add(SCRIPTTYPE, scriptType);
		crit.addAscendingOrderByColumn(CLAZZNAME);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		}
		catch (Exception ex) {
			LOGGER.error("Problem fetching all Groovy scripts: " + ex.getMessage());
			return null;
		}
	}
	
	/**
	 * Saves the scripts to the database. 
	 */
	@Override
	public Integer saveScript(TScriptsBean script) {
		try {
			TScripts tScript = BaseTScripts.createTScripts(script);
			tScript.save();
			return tScript.getObjectID();
		}
		catch (Exception ex) {
			LOGGER.error("Was unable to save script " + script.getClazzName() + ": " + ex.getMessage());
		}
		return null;
	}

	/**
	 * Returns whether the script has dependent data
	 * @param objectID 
	 * @return
	 */
	@Override
	public boolean hasDependentData(Integer objectID) {
		return ReflectionHelper.hasDependentData(replacePeerClasses, replaceFields, objectID);
	}
	
	/**
	 * Deletes the script from the database.
	 * @param scriptID
	 */	
	@Override
	public void deleteScript(Integer scriptID) {
		ReflectionHelper.delete(deletePeerClasses, deleteFields, scriptID);
	}
	
	private List<TScriptsBean> convertTorqueListToBeanList(List<TScripts> torqueList) {
		List<TScriptsBean> beanList = new LinkedList<TScriptsBean>();
		if (torqueList!=null){
			for (TScripts script : torqueList) {
				beanList.add(script.getBean());
			}
		}
		return beanList;
	}
	
}
