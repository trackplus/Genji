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


package com.aurel.track.admin.customize.scripting;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.TScriptsBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ScriptsDAO;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.IntegerStringBean;

/**
 * Business layer for script administration
 *
 */
public class ScriptAdminBL {
	
	private static ScriptsDAO scriptsDAO = DAOFactory.getFactory().getScriptsDAO();
	
	/**
	 * gets the localized list of possible script types
	 * @param locale
	 * @return
	 */
	static List<IntegerStringBean> getScriptTypesList(Locale locale) {
		List<IntegerStringBean> scriptTypes = new LinkedList<IntegerStringBean>();
		scriptTypes.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.script.opt.workflowActivityScript", locale), TScriptsBean.SCRIPT_TYPE.WORKFLOW_ACTIVITY));
		scriptTypes.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.script.opt.workflowGuardScript", locale), TScriptsBean.SCRIPT_TYPE.WORKFLOW_GUARD));
		scriptTypes.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.script.opt.fieldChangeScript", locale), TScriptsBean.SCRIPT_TYPE.FIELD_CHANGE));
		scriptTypes.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.script.opt.generalScript", locale), TScriptsBean.SCRIPT_TYPE.GENEARL_SCRIPT));
		scriptTypes.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.script.opt.parameterData", locale), TScriptsBean.SCRIPT_TYPE.PARAMETER_SCRIPT));
		return scriptTypes;
		
	}

	/**
	 * gets the localized map of possible script types
	 * @param locale
	 * @return
	 */
	static Map<Integer, String> getScriptTypeLabels(Locale locale) {
		Map<Integer, String> scriptTypesLabel = new HashMap<Integer, String>();
		scriptTypesLabel.put(TScriptsBean.SCRIPT_TYPE.WORKFLOW_ACTIVITY, 
				LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.script.opt.workflowActivityScript", locale));
		scriptTypesLabel.put(TScriptsBean.SCRIPT_TYPE.WORKFLOW_GUARD, 
				LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.script.opt.workflowGuardScript", locale));
		scriptTypesLabel.put(TScriptsBean.SCRIPT_TYPE.FIELD_CHANGE, 
				LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.script.opt.fieldChangeScript", locale));
		scriptTypesLabel.put(TScriptsBean.SCRIPT_TYPE.GENEARL_SCRIPT, 
				LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.script.opt.generalScript", locale));
		scriptTypesLabel.put(TScriptsBean.SCRIPT_TYPE.PARAMETER_SCRIPT, 
				LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.script.opt.parameterData", locale));
		return scriptTypesLabel;
	}
	
	public static TScriptsBean copyScript(Integer objectID,Locale locale){
		TScriptsBean scriptBean=loadByPrimaryKey(objectID);
		scriptBean.setObjectID(null);
		String[] args={scriptBean.getClazzName()};
		String name = LocalizeUtil.getParametrizedString("common.copy", args, locale);
		scriptBean.setClazzName(name);
		return scriptBean;
	}
	
	public static TScriptsBean loadByPrimaryKey(Integer objectID) {
		return scriptsDAO.loadByPrimaryKey(objectID);
	}
	
	/**
	 * Loads the script by class name
	 * @param className
	 * @return
	 */
	public static TScriptsBean loadByClassName(String className) {
		return scriptsDAO.loadByClassName(className);
	}
	
	public static boolean classNameExists(String className, Integer objectID) {
		return scriptsDAO.classNameExists(className, objectID);
	}
	
	public static List<TScriptsBean> getAllScripts() {
		return scriptsDAO.getAllScripts();
	}
	
	public static List<TScriptsBean> getScriptsByType(Integer scriptType) {
		return scriptsDAO.getScriptsByType(scriptType);
	}
	public static Map<Integer,TScriptsBean> getScriptsByTypeAsMap(Integer scriptType) {
		List<TScriptsBean> scriptsBeanList=getScriptsByType(scriptType);
		Map<Integer,TScriptsBean> map=new HashMap<Integer, TScriptsBean>();
		if(scriptsBeanList!=null && !scriptsBeanList.isEmpty()){
			for(TScriptsBean scriptsBean:scriptsBeanList){
				map.put(scriptsBean.getObjectID(),scriptsBean);
			}
		}
		return map;
	}
	
	public static Integer saveScript(TScriptsBean script) {
		return scriptsDAO.saveScript(script);
	}
	
	/**
	 * Whether the script has dependent data
	 * @param objectID
	 * @return
	 */
	public static boolean hasDependentData(Integer objectID) {
		return scriptsDAO.hasDependentData(objectID);
	}
	
	
	public static void deleteScript(Integer scriptID) {
		scriptsDAO.deleteScript(scriptID);
	}
}
