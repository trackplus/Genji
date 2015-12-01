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


package com.aurel.track.item.action;

import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.item.ItemActionJSON;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLocationForm;

public class NewItemActionPlugin extends AbstractPluginItemAction{
	private static final Logger LOGGER = LogManager.getLogger(NewItemActionPlugin.class);
	
	@Override
	public String encodeJsonDataStep1(Locale locale, TPersonBean user, Integer workItemID,
									  Integer parentID, Integer projectID, Integer issueTypeID, String synopsis, String description) throws PluginItemActionException{
		ItemLocationForm form=null;
		if(issueTypeID!=null){
			form=ItemBL.getItemLocationFixedIssueType(locale, user, projectID, issueTypeID);
		}else{
			form=ItemBL.getItemLocation(locale, user, projectID, issueTypeID, null);
		}
		if(form==null){
			//You don't have right to create item in any project!
			LOGGER.warn("No right to create item in any project!");
			PluginItemActionException ex=new PluginItemActionException("You don't have right to create item in any project!");
			ex.setFieldName("projectID");
			ex.setLocalizedErrorKey("item.err.noProjectWithCreateRight");
			throw  ex;
		}
		form.setSynopsis(synopsis);
		form.setDescription(description);
		return ItemActionJSON.encodeJSON_IssueLocation(form);
	}
	@Override
	public boolean canFinishInFirstStep(){
		return false;
	}
	@Override
	public WorkItemContext next(Locale locale, TPersonBean user, Integer workItemID, Integer parentID, Map<String, Object> params, String synopsis, String description) throws PluginItemActionException {
		ItemLocationForm form = extractItemLocation(locale,user,params,workItemID,parentID);
		if(form.getProjectID()==null){
			LOGGER.warn("Can't execute next().Project is required!");
			PluginItemActionException ex=new PluginItemActionException("Project is required!");
			ex.setFieldName("projectID");
			ex.setLocalizedErrorKey("common.err.required");
			ex.setLocalizedParam(form.getProjectLabel());
			throw  ex;
		}
		if(form.getIssueTypeID()==null){
			LOGGER.warn("Can't execute next().Issue type is required!");
			PluginItemActionException ex=new PluginItemActionException("IssueType is required!");
			ex.setFieldName("issuTypeID");
			ex.setLocalizedErrorKey("common.err.required");
			ex.setLocalizedParam(form.getIssueTypeLabel());
			throw  ex;
		}

		//FIXME verify issue creating rights
		WorkItemContext workItemContext=createCtx(form, user.getObjectID(), locale);
		if(synopsis!=null){
			workItemContext.getWorkItemBean().setSynopsis(synopsis);
		}
		if(description!=null){
			workItemContext.getWorkItemBean().setDescription(description);
		}
		return workItemContext;
	}


	protected WorkItemContext createCtx(ItemLocationForm form,Integer personID,Locale locale){
		Boolean accessLevelFlag=form.isAccessLevelFlag();
		Integer issueTypeID=form.getIssueTypeID();
		Integer projectID=form.getProjectID();
		WorkItemContext workItemContext=ItemBL.createNewItem(projectID, issueTypeID, personID, accessLevelFlag, locale);
		return  workItemContext;
	}
}
