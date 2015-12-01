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

package com.aurel.track.item;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.admin.customize.scripting.BINDING_PARAMS;
import com.aurel.track.admin.customize.scripting.GroovyScriptExecuter;
import com.aurel.track.admin.customize.scripting.GroovyScriptLoader;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.renderer.CompositeTypeRendererRT;
import com.aurel.track.fieldType.types.FieldType;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.util.event.EventPublisher;
import com.aurel.track.util.event.FieldChangeScriptHandler;
import com.aurel.track.util.event.IEventSubscriber;

/**
 *
 */
public class ItemFieldRefreshBL {
	
	/**
	 * Whether the field has dependences (other fields depending on this field)
	 * @param fieldBean
	 * @param ctx
	 * @return
	 */
	public static boolean hasDependences(TFieldBean fieldBean, WorkItemContext ctx){
		if (fieldBean!=null) {
			Integer fieldID = fieldBean.getObjectID();
			FieldType fieldType= FieldTypeManager.getInstance().getType(fieldID);
			if (fieldType!=null && fieldType.getFieldTypeRT().isComposite()){
				fieldType.setFieldID(fieldID);
				if (((CompositeTypeRendererRT)fieldType.getRendererRT()).isCascading()) {
					return true;
				}
			} else {
				if (SystemFields.INTEGER_DURATION.equals(fieldID) || SystemFields.INTEGER_TOP_DOWN_DURATION.equals(fieldID) ||
						SystemFields.INTEGER_STARTDATE.equals(fieldID) || SystemFields.INTEGER_ENDDATE.equals(fieldID) ||
						SystemFields.INTEGER_TOP_DOWN_START_DATE.equals(fieldID) || SystemFields.INTEGER_TOP_DOWN_END_DATE.equals(fieldID)) {
					return true;
				}
				TWorkItemBean workItemBean = ctx.getWorkItemBean();
				if (workItemBean!=null) {
					boolean newItem = workItemBean.getObjectID()==null;
					List<String> scriptNames = FieldChangeScriptHandler.getHandlersForField(fieldID);
					if (scriptNames!=null && !scriptNames.isEmpty()) {
						for (Iterator<String> iterator = scriptNames.iterator(); iterator.hasNext();) {
							String scriptName = iterator.next();
							if (newItem) {
								Boolean triggerOnItemCreate = (Boolean)GroovyScriptExecuter.executeGroovyMethod(scriptName, FieldChangeScriptHandler.TRIGGERING_ON_ITEM_CREATE, null);
								if (triggerOnItemCreate!=null && !triggerOnItemCreate.booleanValue()) {
									iterator.remove();
								}
							} else {
								Boolean triggerOnItemChange = (Boolean)GroovyScriptExecuter.executeGroovyMethod(scriptName, FieldChangeScriptHandler.TRIGGERING_ON_ITEM_CHANGE, null);
								if (triggerOnItemChange!=null && !triggerOnItemChange.booleanValue()) {
									iterator.remove();
								}
							}
						}
						return !scriptNames.isEmpty();
					} else {
						return false;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Whether the dependency should be solved on the client side or an ajax request is needed on the server side
	 * @param fieldID
	 * @return
	 */
	public static boolean isClientSideRefresh(Integer fieldID){
		if (fieldID!=null) {
			if (SystemFields.INTEGER_DURATION.equals(fieldID) || SystemFields.INTEGER_TOP_DOWN_DURATION.equals(fieldID) ||
					SystemFields.INTEGER_STARTDATE.equals(fieldID) || SystemFields.INTEGER_ENDDATE.equals(fieldID) ||
					SystemFields.INTEGER_TOP_DOWN_START_DATE.equals(fieldID) || SystemFields.INTEGER_TOP_DOWN_END_DATE.equals(fieldID)) {
				return true;
			}	
		}
		return false;
	}
	
	/**
	 * Gets the dependent fields and executes the field change script if exist
	 * @param fieldID
	 * @param workItemContext
	 * @param personBean
	 * @param locale
	 * @return
	 */
	public static Set<Integer> getDependences(Integer fieldID, WorkItemContext workItemContext, TPersonBean personBean, Locale locale){
		Set<Integer> result=new HashSet<Integer>();
		FieldType fieldType= FieldTypeManager.getInstance().getType(fieldID);
		if(fieldType!=null && fieldType.getFieldTypeRT().isComposite()) {
			//hardcoded field change
			result.add(fieldID);
		} else {
			if (SystemFields.INTEGER_DURATION.equals(fieldID)) {
				result.add(SystemFields.INTEGER_DURATION);
				//result.add(SystemFields.INTEGER_ENDDATE);
			} else {
				if (SystemFields.INTEGER_TOP_DOWN_DURATION.equals(fieldID)) {
					result.add(SystemFields.INTEGER_TOP_DOWN_DURATION);
					//result.add(SystemFields.INTEGER_TOP_DOWN_END_DATE);
				} else {
					List<String> scriptNames = FieldChangeScriptHandler.getHandlersForField(fieldID);
					if (scriptNames!=null) {
						for (String scriptName : scriptNames) {
							if (GroovyScriptLoader.getInstance().doesGroovyClassExist(scriptName)) {
								Map<String, Object> inputBinding = new HashMap<String, Object>();
								inputBinding.put(BINDING_PARAMS.WORKITEM_CONTEXT, workItemContext);
								inputBinding.put(BINDING_PARAMS.ISSUE, workItemContext.getWorkItemBean());
								inputBinding.put(BINDING_PARAMS.ISSUE_ORIGINAL, workItemContext.getWorkItemBeanOriginal());
								inputBinding.put(BINDING_PARAMS.USER, personBean);
								inputBinding.put(BINDING_PARAMS.LOCALE, locale);
								EventPublisher eventPublisher = EventPublisher.getInstance();
								if (eventPublisher!=null) {
									List<Integer> events = new LinkedList<Integer>();
									events.add(Integer.valueOf(IEventSubscriber.FIELD_CHANGED+fieldID));
									eventPublisher.notify(events, inputBinding);
									Set<Integer> scriptDependentFields = FieldChangeScriptHandler.getDependentFields(inputBinding);
									if (scriptDependentFields!=null) {
										result.addAll(scriptDependentFields);		
									}
								}
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Gets the field modified by a field change. This modified fields does not set their own values but the changed field is responsible to set the modified field values
	 * @param fieldID
	 * @param workItemContext
	 * @param personBean
	 * @param locale
	 * @return
	 */
	public static Set<Integer> getModifiedFields(Integer fieldID, WorkItemContext workItemContext, TPersonBean personBean, Locale locale) {
		Set<Integer> result=null;
		if (SystemFields.INTEGER_DURATION.equals(fieldID)) {
			result=new HashSet<Integer>();
			result.add(SystemFields.INTEGER_STARTDATE);
			result.add(SystemFields.INTEGER_ENDDATE);
		} else {
			if (SystemFields.INTEGER_TOP_DOWN_DURATION.equals(fieldID)) {
				result=new HashSet<Integer>();
				result.add(SystemFields.INTEGER_TOP_DOWN_START_DATE);
				result.add(SystemFields.INTEGER_TOP_DOWN_END_DATE);
			}
		}
		return result;
	}
}
