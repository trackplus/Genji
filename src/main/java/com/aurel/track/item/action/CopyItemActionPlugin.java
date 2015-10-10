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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;

public class CopyItemActionPlugin extends AbstractPluginItemAction {
	
	private static final Logger LOGGER = LogManager.getLogger(CopyItemActionPlugin.class);
	
	@Override
	public String encodeJsonDataStep1(Locale locale, TPersonBean user, Integer workItemID,
									  Integer parentID, Integer projectID, Integer issueTypeID, String synopsis, String description)throws PluginItemActionException{
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		TWorkItemBean workItem=null;
		try {
			workItem=ItemBL.loadWorkItem(workItemID);
			JSONUtility.appendBooleanValue(sb, "hasChildren", ItemBL.hasChildren(workItemID));
			JSONUtility.appendBooleanValue(sb, "deepCopy", false);
			JSONUtility.appendBooleanValue(sb, "copyAttachments", false);
			JSONUtility.appendBooleanValue(sb, "copyChildren", false);
			JSONUtility.appendIntegerValue(sb, "projectID", workItem.getProjectID());
			JSONUtility.appendIntegerValue(sb, "issueTypeID", workItem.getListTypeID());
			JSONUtility.appendIntegerValue(sb, "workItemID", workItem.getObjectID());
			JSONUtility.appendStringValue(sb, "issueNoLabel", FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_ISSUENO, locale));
			JSONUtility.appendStringValue(sb, "statusDisplay", StatusBL.getStatusDisplay(workItem.getStateID(), locale));
			JSONUtility.appendStringValue(sb,"synopsis", workItem.getSynopsis(),true);

		} catch (ItemLoaderException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			sb.append("success:false,errorMessage:").append(e.getMessage());
		}
		sb.append("}");
		return sb.toString();
	}
	@Override
	public boolean canFinishInFirstStep(){
		return true;
	}
	@Override
	public WorkItemContext next(Locale locale, TPersonBean user, Integer workItemID, Integer parentID, Map<String, Object> params, String synopsis, String description) throws PluginItemActionException {

		WorkItemContext ctx=ItemBL.editCopyWorkItem(workItemID, user.getObjectID(), locale);
		
		TWorkItemBean workItemBean=ctx.getWorkItemBean();
		Boolean deepCopy=parseBoolean(params.get("deepCopy"));
		Boolean copyAttachments=parseBoolean(params.get("copyAttachments"));
		Boolean copyChildren=parseBoolean(params.get("copyChildren"));
		workItemBean.setDeepCopy(deepCopy.booleanValue()); 
		workItemBean.setCopyAttachments(copyAttachments.booleanValue());
		workItemBean.setCopyChildren(copyChildren.booleanValue());
		
		String[] msgArguments = new String[] {workItemBean.getSynopsis()};
		String newSysnopsys =LocalizeUtil.getParametrizedString("common.copy",msgArguments, locale);
		workItemBean.setSynopsis(newSysnopsys);
		return ctx;
	}
	
	@Override
	public Integer saveInFirsStep(Locale locale, TPersonBean user,
			Integer workItemID, Map<String, Object> params)
			throws PluginItemActionException {
		
		WorkItemContext workItemContext=ItemBL.viewWorkItem(workItemID, user.getObjectID(),locale,false);
		TWorkItemBean  workItemBean=workItemContext.getWorkItemBean();
		
		boolean deepCopy=parseBoolean(params.get("deepCopy")).booleanValue();
		boolean copyAttachments=parseBoolean(params.get("copyAttachments")).booleanValue();
		boolean copyChildren=parseBoolean(params.get("copyChildren")).booleanValue();
		workItemBean.setDeepCopy(deepCopy);
		workItemBean.setCopyAttachments(copyAttachments);
		workItemBean.setCopyChildren(copyChildren);
		
		String[] msgArguments = new String[] {workItemBean.getSynopsis()};
		String newSysnopsys =LocalizeUtil.getParametrizedString("common.copy", msgArguments ,locale);            	
		workItemBean.setSynopsis(newSysnopsys);
		
		List<ErrorData> errorList=new ArrayList<ErrorData>();
		Integer newWorkItemID=ItemBL.copyWorkItem(workItemContext, errorList, true);
		if(!errorList.isEmpty()){
			StringBuilder message=new StringBuilder();
			for (Iterator<ErrorData> iterator = errorList.iterator(); iterator.hasNext();) {
				ErrorData errorData = iterator.next();
				message.append(errorData.getFieldName()+":"+errorData.getResourceKey()+";\n");
			}
			throw new PluginItemActionException(message.toString());
		}
		
		return newWorkItemID;
	}
	
	@Override
	public boolean isEnabled(Integer personID, TWorkItemBean workItem,
			boolean allowedToChange, boolean allowedToCreate, int appEdition) {							
		if (workItem==null || workItem.getObjectID()==null) {
			//no copy from new issues
			return false;
		}
		return allowedToCreate;
	}
}
