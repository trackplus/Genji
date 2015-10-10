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

package com.aurel.track.util.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.scripting.GroovyScriptExecuter;
import com.aurel.track.admin.customize.scripting.ScriptAdminBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldDesignBL;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TScriptsBean;
import com.aurel.track.util.GeneralUtils;

/**
 * Handler for field changes on client side
 * Builds up based on field change type scripts
 * @author Tamas
 *
 */
public class FieldChangeScriptHandler implements Serializable, IEventSubscriber {

	private static final long serialVersionUID = 1L;

	public static Logger LOGGER = LogManager.getLogger(FieldChangeScriptHandler.class);
	
	protected static FieldChangeScriptHandler fieldChangeScriptHandler = null;
	
	public static final String TRIGGERING_FIELD_METHOD_NAME = "getTriggeringFieldName";
	
	public static final String TRIGGERING_ON_ITEM_CREATE = "triggerOnItemCreate";
	
	public static final String TRIGGERING_ON_ITEM_CHANGE = "triggerOnItemChange";
	
	
	/**
	 * The name of binding attribute for storing the dependent fields 
	 */
	public static final String BINDING_DEPENDENT_FIELDS = "dependentFields";
	
	private static Map<Integer, List<String>> fieldToToHandlerNames = new HashMap<Integer, List<String>>();
	
	
	private FieldChangeScriptHandler() {
	}
	
	
	
	
	public static FieldChangeScriptHandler getInstance() {
		if (fieldChangeScriptHandler == null) {
			fieldChangeScriptHandler = new FieldChangeScriptHandler();
		}
		return fieldChangeScriptHandler;
	}
	
	public static List<String> getHandlersForField(Integer fieldID) {
		return fieldToToHandlerNames.get(fieldID);
	}
	
	/**
	 * Get the events this class is interested in
	 * At the same time can be more events in the list 
	 * (IEventSubscriber.getInterestedEvents())
	 */
	public List<Integer> getInterestedEvents() {
		Set<Integer> fieldChangeEvents = new HashSet<Integer>();
		List<TScriptsBean> fieldChangeScripts = ScriptAdminBL.getScriptsByType(TScriptsBean.SCRIPT_TYPE.FIELD_CHANGE);
		if (fieldChangeScripts!=null) {
			for (TScriptsBean scriptsBean : fieldChangeScripts) {
				String handlerName = scriptsBean.getClazzName();
				String triggeringFieldName = (String)GroovyScriptExecuter.executeGroovyMethod(handlerName, TRIGGERING_FIELD_METHOD_NAME, null);
					if (triggeringFieldName!=null) {
					List<TFieldBean> fieldBeans = FieldDesignBL.loadByName(triggeringFieldName);
					if (fieldBeans!=null && !fieldBeans.isEmpty()) {
						TFieldBean fieldBean = fieldBeans.get(0);
						if (fieldBean!=null) {
							Integer fieldID = fieldBean.getObjectID();
							Integer event = Integer.valueOf(IEventSubscriber.FIELD_CHANGED+fieldID);
							fieldChangeEvents.add(event);
							LOGGER.debug("Field change handler " +  handlerName + " found for field " + triggeringFieldName +  "(" + fieldID  +  "): event " + event + " added.");
							List<String> handlerNames = fieldToToHandlerNames.get(fieldID);
							if (handlerNames==null) {
								handlerNames = new LinkedList<String>();
								fieldToToHandlerNames.put(fieldID, handlerNames);
							}
							LOGGER.debug("Script " + handlerName + " handles event  " +  event);
							if (!handlerNames.contains(handlerName)) {
								//FIXME load fieldToToHandlerNames only by attach not by detach (it duplicates each handler)
								handlerNames.add(handlerName);
							}
						}
					}
				}
			}	
		}		
		return GeneralUtils.createIntegerListFromCollection(fieldChangeEvents);
	}

		
	/**
	 * Execute the script as handling the event
	 * IEventSubscriber.update()
	 */
	public boolean update(List<Integer> events, Object eventContextObject) {
		Map<String, Object> eventContextMap = (Map<String, Object>)eventContextObject;
		if (events!=null && !events.isEmpty()) {
			//we suppose that script handlers are triggered always by one single event
			Integer event = events.get(0);
			if (event.intValue()>IEventSubscriber.FIELD_CHANGED) {
				//new FieldChangeScriptHandlerExample().handleEvent((Map)eventContextObject);
				Integer fieldID = Integer.valueOf(event.intValue()-IEventSubscriber.FIELD_CHANGED);
				List<String> scriptHandlers = fieldToToHandlerNames.get(fieldID);
				if (scriptHandlers!=null) {
					for (String scriptHandler : scriptHandlers) {
						GroovyScriptExecuter.executeGroovyHandler(scriptHandler, eventContextMap);
					}
				}
			}
		}
		return true;
	}

	/**
	 * Gets the dependent fieldIDs
	 * @param fieldID
	 * @param workItemContext
	 * @param personBean
	 * @param locale
	 * @return
	 */
	public static Set<Integer> getDependentFields(Map<String, Object> inputBinding) {
		Set<Integer> dependecesSet = new HashSet<Integer>();
		List<String> dependentFieldNames = (List<String>)inputBinding.get(BINDING_DEPENDENT_FIELDS);
		if (dependentFieldNames!=null && !dependentFieldNames.isEmpty()) {
			List<TFieldBean> dependentFieldBeans = FieldBL.loadByNames(dependentFieldNames);
			dependecesSet.addAll(GeneralUtils.createIntegerSetFromBeanList(dependentFieldBeans));
		}
		return dependecesSet;
	}
}
