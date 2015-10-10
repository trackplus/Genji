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


package com.aurel.track.item.action;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.ItemActionJSON;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLocationForm;
import com.aurel.track.item.workflow.execute.StatusWorkflow;
import com.aurel.track.json.JSONUtility;


public class MoveItemActionPlugin extends AbstractPluginItemAction {

	private static final Logger LOGGER = LogManager.getLogger(MoveItemActionPlugin.class);

	@Override
	public String encodeJsonDataStep1(Locale locale, TPersonBean user, Integer workItemID,
									  Integer parentID, Integer projectID, Integer issueTypeID, String synopsis, String description) throws PluginItemActionException{
		
		TWorkItemBean workItem = ItemBL.loadWorkItemSystemAttributes(workItemID);
		ItemLocationForm form=ItemBL.getItemLocation(locale, user, workItem.getProjectID(), workItem.getListTypeID(), workItem.getSuperiorworkitem());
		String issueLocation=ItemActionJSON.encodeJSON_IssueLocation(form);
		IFieldTypeRT projectFieldType = FieldTypeManager.getFieldTypeRT(SystemFields.INTEGER_PROJECT);
		IFieldTypeRT issueTypeFieldType = FieldTypeManager.getFieldTypeRT(SystemFields.INTEGER_ISSUETYPE);
		String oldProject = projectFieldType.getShowValue(workItem.getProjectID(), locale);
		String oldIssueType = issueTypeFieldType.getShowValue(workItem.getListTypeID(), locale);
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, "hasChildren", ItemBL.hasChildren(workItemID));
		JSONUtility.appendStringValue(sb, "projectLabel", FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_PROJECT, locale));
		JSONUtility.appendStringValue(sb, "issueTypeLabel", FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_ISSUETYPE, locale));
		JSONUtility.appendStringValue(sb, "oldProject",oldProject);
		JSONUtility.appendStringValue(sb, "oldIssueType",oldIssueType);
		JSONUtility.appendIntegerValue(sb, "workItemID", workItem.getObjectID());
		JSONUtility.appendStringValue(sb, "issueNoLabel", FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_ISSUENO, locale));
		JSONUtility.appendStringValue(sb, "statusDisplay", StatusBL.getStatusDisplay(workItem.getStateID(), locale));
        JSONUtility.appendStringValue(sb,"synopsis", workItem.getSynopsis());
		JSONUtility.appendBooleanValue(sb, "statusNeeded", Boolean.valueOf(false));
		JSONUtility.appendBooleanValue(sb, "moveChildren", false);
		JSONUtility.appendJSONValue(sb, "issueLocation",issueLocation, true);
		sb.append("}");
		return sb.toString();
	}
	
	@Override
	public boolean canFinishInFirstStep(){
		return false;
	}
	@Override
	public WorkItemContext next(Locale locale, TPersonBean user, Integer workItemID, Integer parentID, Map<String, Object> params, String synopsis, String description) throws PluginItemActionException {
		ItemLocationForm form = extractItemLocation(locale,user,params,workItemID,parentID);
		Integer projectID=form.getProjectID();
		Integer issueTypeID=form.getIssueTypeID();
		if(projectID==null){
			LOGGER.warn("Can't execute next().Project is required!");
			PluginItemActionException ex=new PluginItemActionException("Project is required!");
			ex.setFieldName("projectID");
			ex.setLocalizedErrorKey("common.err.required");
			throw  ex;
		}
		if(issueTypeID==null){
			LOGGER.warn("Can't execute next().Issue type is required!");
			PluginItemActionException ex=new PluginItemActionException("IssueType is required!");
			ex.setFieldName("issuTypeID");
			ex.setLocalizedErrorKey("common.err.required");
			throw  ex;
		}
		TWorkItemBean workItem = ItemBL.loadWorkItemSystemAttributes(workItemID);
		if (workItem.getProjectID().equals(projectID)
				&& workItem.getListTypeID().equals(issueTypeID)) {
			PluginItemActionException ex=new PluginItemActionException();
			ex.setFieldName("projectID");
			ex.setLocalizedErrorKey("item.action.move.err.same");
			throw ex;
		}
		Integer stateID=parseInt(params.get("statusID"));
		if (stateID==null) {
			boolean stateValid = StatusWorkflow.statusIsValid(projectID, issueTypeID, workItem.getStateID());
			if (!stateValid) {
				params.put("statusNeeded", Boolean.valueOf(true));
				PluginItemActionException ex=new PluginItemActionException();
				ex.setFieldName("projectID");
				ex.setLocalizedErrorKey("item.action.move.err.invalidState");
				throw ex;
			} else {
				params.put("statusNeeded", Boolean.valueOf(false));
			}
		}
		WorkItemContext ctx=ItemBL.moveItem(workItemID,user.getObjectID(),locale,projectID,issueTypeID, stateID);
		Boolean moveChildren=parseBoolean(params.get("moveChildren"));
		ctx.getWorkItemBean().setMoveChildren(moveChildren.booleanValue());
		Set<Integer> presentFields = ctx.getPresentFieldIDs();
		presentFields.add(SystemFields.INTEGER_PROJECT);
		presentFields.add(SystemFields.INTEGER_ISSUETYPE);
		return ctx;
	}


	/*private Map<String, Object> prepareStep1(Map<String, Object> session,
			Map<String, Object> params, Integer workItemID, TWorkItemBean workItem,
			Integer projectID, Integer issueTypeID) {
		String oldProject;
		String oldIssueType;
		Integer personID=((TPersonBean) session.get(Constants.USER_KEY)).getObjectID();
		
		// needeed for INPUT result: 1. first execute(), 2. project change
		// execute() and 3. same project-issueType error in next()
		List<TProjectBean> projectList = ProjectBL.loadProjectsWithCreateIssueRight(personID);
		List<TListTypeBean> issueTypeList;
		Locale locale=(Locale) session.get(Constants.LOCALE_KEY);
		if (projectID == null) {
			// no submit: 1. first rendering
			issueTypeList = ItemBL2.getIssueTypes(workItem.getProjectID(),
					personID, locale, workItem.getListTypeID(), workItem.getSuperiorworkitem());
		} else {
			// as a result of a submit: 2. project change or 3. next step with
			// error
			issueTypeList = ItemBL2.getIssueTypes(projectID, personID, locale, workItem.getListTypeID(), workItem.getSuperiorworkitem());
		}
		IFieldTypeRT projectType = FieldTypeManager.getFieldTypeRT(SystemFields.INTEGER_PROJECT);
		IFieldTypeRT issueType = FieldTypeManager.getFieldTypeRT(SystemFields.INTEGER_ISSUETYPE);
		oldProject = projectType.getShowValue(workItem.getProjectID(), locale);
		oldIssueType = issueType.getShowValue(workItem.getListTypeID(), locale);
		if (params==null) {
			params=new HashMap<String, Object>();
		}
		params.put("workItem", workItem);
		params.put("workItemID", workItemID);
		params.put("oldProject", oldProject);
		params.put("oldIssueType", oldIssueType);
		params.put("projectList", projectList);
		if(projectID==null){
			params.put("projectID", workItem.getProjectID());
		}else{
			params.put("projectID", projectID);
		}
		if (issueTypeID == null) {
			params.put("issueTypeID", workItem.getListTypeID());
		} else {
			params.put("issueTypeID", issueTypeID);
		}
		params.put("issueTypeList", issueTypeList);
		params.put("synopsis", workItem.getSynopsis());
		Boolean statusNeeded = (Boolean)params.get("statusNeeded");
		if (statusNeeded==null) {
			statusNeeded = Boolean.valueOf(false);
			params.put("statusNeeded", statusNeeded);
		}
		if (statusNeeded.booleanValue()) {
			params.put("statusList", StatusWorkflow.loadStatesTo(projectID, issueTypeID, null, personID, workItem, null));
		}
		params.put("projectLabel", FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_PROJECT, locale));
		params.put("issueTypeLabel", FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_ISSUETYPE, locale));
		params.put("statusLabel", FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_STATE, locale));
		return params;
	}*/
	@Override
	public Map<String, Object> editItem(Map<String, Object> session,
			Integer workItemID, Map<String, Object> params,
			Integer projectID, Integer issueTypeID) {
		WorkItemContext workItemContext= (WorkItemContext) session.get("workItemContext");	
		Locale locale=(Locale) session.get(Constants.LOCALE_KEY);
		TWorkItemBean workItemOriginal=workItemContext.getWorkItemBeanOriginal();
		//the project and issueType fields are not present in the second screen as fields 
		//(just in the first, and in the second just as labels)
		//but the isChanged flag (which determines whether is the save needed) is set according to 
		//the changing of any present field from the actual from (screen). 
		//So force the adding of these fields to presentfields  
		Set<Integer> presentFields = workItemContext.getPresentFieldIDs();
		presentFields.add(SystemFields.INTEGER_PROJECT);
		presentFields.add(SystemFields.INTEGER_ISSUETYPE);
		IFieldTypeRT projectType = FieldTypeManager.getFieldTypeRT(SystemFields.INTEGER_PROJECT);
		IFieldTypeRT issueType = FieldTypeManager.getFieldTypeRT(SystemFields.INTEGER_ISSUETYPE);
		//IFieldTypeRT stateType = FieldTypeManager.getFieldTypeRT(new Integer(SystemFields.STATE));
		try{
			String oldProject=projectType.getShowValue(workItemOriginal.getProjectID(), locale);
			String oldIssueType=issueType.getShowValue(workItemOriginal.getListTypeID(), locale);
			//Integer projectID=parseInt(params.get("projectID"));
			//Integer issueTypeID=parseInt(params.get("issueTypeID"));
			String newProject=projectType.getShowValue(projectID, locale);
			String newIssueType = issueType.getShowValue(issueTypeID, locale);
			params.put("oldProject", oldProject);
			params.put("oldIssueType", oldIssueType);
			params.put("newProject", newProject);
			params.put("newIssueType", newIssueType);			
			
		}catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return params;
	}
	
	@Override
	public boolean isEnabled(Integer personID, TWorkItemBean workItem, 
			boolean allowedToChange, boolean allowedToCreate, int appEdition) {					
		if (workItem==null || workItem.getObjectID()==null) {
			//no status change for new issues
			return false;
		}												
		return allowedToChange;					
	}
}
