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

package com.aurel.track.item.massOperation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.bulkSetters.CompositeSelectBulkSetter;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.bulkSetters.WatcherSelectBulkSetter;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.GeneralUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Configuring a single bulk operation field
 * Each change is instantly stored in 
 * the massOperationExpressions list from the session 
 * (after submitting the 'small' form corresponding to the field).
 * After each operation (relation change, value change) 
 * the corresponding massOperationExpression should be actualized 
 * because after a project change the entire page will be refreshed,
 * consequently the massOperationExpressions should be always actual. 
 */
public class MassOperationFieldSetterAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale;
	private TPersonBean personBean;
	private Integer fieldID;
	private int relationID;
	private Map<String, String> displayValueMap = new HashMap<String, String>();
	private String selectedIssueIDs;
	private int[] issueIDsArr;
	private Integer projectID;
	private Integer issueTypeID;
	/**
	 * By submitting a project specific or custom list
	 */
	private Integer listID;
	private boolean required;

	public void prepare() throws Exception {
		locale = (Locale)session.get(Constants.LOCALE_KEY);
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		if (selectedIssueIDs!=null) {
			issueIDsArr = GeneralUtils.createIntArrFromIntegerCollection(GeneralUtils.createIntegerListFromStringArr(selectedIssueIDs.split(",")));
		}
	}

	/**
	 * After changing the relationID value for a field
	 * (the datasource should be refreshed and rendered again) 
	 */
	@Override
	public String execute() {
		IFieldTypeRT fieldTypeRT = null;
		Set<Integer> pseudoFields = MassOperationBL.getPseudoFields();
		IBulkSetter bulkSetterDT = null;
		if (pseudoFields.contains(fieldID)) {
			//watchers field (consulted or informed)
			bulkSetterDT = new WatcherSelectBulkSetter(fieldID);
		} else {
			fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			if (fieldTypeRT!=null) {
				//system or custom field
				bulkSetterDT = fieldTypeRT.getBulkSetterDT(fieldID);
			}
		}
		Object value = null; 
		if (bulkSetterDT!=null) {
			bulkSetterDT.setRelation(relationID);
			//value from submit
			value = bulkSetterDT.fromDisplayString(displayValueMap, locale);
		}
		Integer[] involvedProjectsIDs;
		if (projectID!=null) {
			involvedProjectsIDs = new Integer[] {projectID};
		} else {
			List<TWorkItemBean> selectedWorkItemBeans =
					LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(issueIDsArr, personBean.getObjectID(), true, true, false);
			involvedProjectsIDs = GeneralUtils.createIntegerArrFromCollection(MassOperationBL.getInvolvedProjectIDs(selectedWorkItemBeans));
		}
		String bulkValueTemplate = null;
		List<TWorkItemBean> selectedWorkItemBeans = LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(
				issueIDsArr, personBean.getObjectID(), true, true, false);
		MassOperationContext massOperationContext = MassOperationBL.initMassOperationContext(
				selectedWorkItemBeans, projectID, issueTypeID);
		MassOperationValue massOperationValue = new MassOperationValue(fieldID);
		massOperationValue.setValue(value);
		if (fieldTypeRT==null) {
			//pseudo field: watcher relation changed 
			List<Integer> selectedWorkItemIDsList = GeneralUtils.createListFromIntArr(issueIDsArr); //MassOperationBL.getSelectedWorkItems(selectedIssuesMap);						
			Map<Integer, Set<Integer>> projectIssueTypeContexts = massOperationContext.getProjectIssueTypeContexts();
			List<TPersonBean> possibleValues = MassOperationWatchersBL.getPossibleWatchers(
					fieldID, relationID, projectIssueTypeContexts, personBean,
					involvedProjectsIDs, selectedWorkItemIDsList); 
			massOperationValue.setPossibleValues(possibleValues);
		} else {
			Set<Integer> presentFieldsSet = new HashSet<Integer>();
			presentFieldsSet.add(fieldID);
			List<Integer> customListFieldIDs = new LinkedList<Integer>();
			if (fieldTypeRT.getValueType()==ValueType.CUSTOMOPTION || fieldTypeRT.isComposite()) {
				customListFieldIDs.add(fieldID);
			}
			MassOperationFieldsBL.configureContext(massOperationContext, projectID,
					issueTypeID, presentFieldsSet, customListFieldIDs, personBean, locale);
			//refresh the possible values
			fieldTypeRT.loadBulkOperationDataSource(massOperationContext, massOperationValue, null, personBean, locale);
			//set possibleValues and matcherValueJSP for rendering now
		}
		if (bulkSetterDT!=null) {
			bulkValueTemplate = bulkSetterDT.getSetterValueControlClass();
			String jsonConfig = bulkSetterDT.getSetterValueJsonConfig(MassOperationBL.VALUE_BASE_NAME,
					massOperationValue.getValue(), massOperationValue.getPossibleValues(),
					massOperationValue.getValueLabelMap(), false, personBean, locale);
			//store it to be available after eventual project refresh		
			JSONUtility.encodeJSON(servletResponse, MassOperationJSON.getBulkExpressionValueJSON(
					MassOperationBL.getControlName(MassOperationBL.VALUE_BASE_NAME, fieldID), required,
					bulkValueTemplate, jsonConfig));
		}
		return null;
	}
	
	public String compositePartChange() {
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
		CompositeSelectBulkSetter compositeSelectBulkSetter = null;
		if (fieldTypeRT!=null) {
			//system or custom field
			compositeSelectBulkSetter = (CompositeSelectBulkSetter)fieldTypeRT.getBulkSetterDT(fieldID);
		}
		Object value = null; 
		if (compositeSelectBulkSetter!=null) {
			compositeSelectBulkSetter.setRelation(relationID);
			//value from submit
			value = compositeSelectBulkSetter.fromDisplayString(displayValueMap, locale);
		}
		List<TWorkItemBean> selectedWorkItemBeans = LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(
				issueIDsArr, personBean.getObjectID(), true, true, false);
		MassOperationContext massOperationContext = MassOperationBL.initMassOperationContext(
				selectedWorkItemBeans, projectID, issueTypeID);
		Set<Integer> presentFieldsSet = new HashSet<Integer>();
		presentFieldsSet.add(fieldID);
		List<Integer> customListFieldIDs = new LinkedList<Integer>();
		customListFieldIDs.add(fieldID);
		MassOperationFieldsBL.configureContext(massOperationContext, projectID,
				issueTypeID, presentFieldsSet, customListFieldIDs, personBean, locale);
		MassOperationValue massOperationValue = new MassOperationValue(fieldID);
		massOperationValue.setValue(value);
		if (fieldTypeRT != null) {
			fieldTypeRT.loadBulkOperationDataSource(massOperationContext, massOperationValue, null, personBean, locale);
		}
		//set possibleValues and matcherValueJSP for rendering now
		if (compositeSelectBulkSetter!=null) {
			String jsonValues = compositeSelectBulkSetter.getJsonValuesForList(MassOperationBL.VALUE_BASE_NAME,
					massOperationValue.getValue(), massOperationValue.getPossibleValues(), listID);
			//store it to be available after eventual project refresh
			JSONUtility.encodeJSON(servletResponse, jsonValues);
		}
		return null;
	}
	
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public Map<String, String> getDisplayValueMap() {
		return displayValueMap;
	}

	public void setDisplayValueMap(Map<String, String> displayValueMap) {
		this.displayValueMap = displayValueMap;
	}


	public void setRelationID(int relationID) {
		this.relationID = relationID;
	}


	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}

	public void setSelectedIssueIDs(String selectedIssueIDs) {
		this.selectedIssueIDs = selectedIssueIDs;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public void setListID(Integer listID) {
		this.listID = listID;
	}
		
	public void setIssueTypeID(Integer issueTypeID) {
		this.issueTypeID = issueTypeID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}
	
}
