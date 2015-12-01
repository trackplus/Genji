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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.item.ItemActionJSON;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.item.ItemLocationForm;

public class NewItemChildActionPlugin extends NewItemActionPlugin{
	private static final Logger LOGGER = LogManager.getLogger(NewItemChildActionPlugin.class);
	
	@Override
	public String encodeJsonDataStep1(Locale locale, TPersonBean user, Integer workItemID,
									  Integer parentID, Integer projectID, Integer issueTypeID, String synopsis, String description) throws PluginItemActionException{
		if(parentID==null){
			parentID=workItemID;
		}
		if(projectID==null){
			TWorkItemBean wibeanParent = null;
			try {
				wibeanParent = ItemBL.loadWorkItem(parentID);
			} catch (ItemLoaderException e) {
				LOGGER.error("getting the workItem failed with " + e.getMessage());
			}
			projectID=wibeanParent.getProjectID();
            if (issueTypeID==null) {
                issueTypeID =  wibeanParent.getListTypeID();
            }
		}
		ItemLocationForm form=ItemBL.getItemLocation(locale, user, projectID, issueTypeID, parentID);
		form.setParentID(parentID);
		return ItemActionJSON.encodeJSON_IssueLocation(form);
	}
	@Override
	protected ItemLocationForm extractItemLocation(Locale locale,TPersonBean user,Map<String, Object> params,Integer workItemID,Integer parentID) {
		ItemLocationForm form=super.extractItemLocation(locale,user,params, workItemID,parentID);
		form.setParentID(parentID!=null?parentID:workItemID);
		try {
			HashMap<String, Object>newlyCreatedLinkSettings = (HashMap<String, Object>)params.get("newlyCreatedLinkSettings");
			if(newlyCreatedLinkSettings != null){
				form.setNewlyCreatedLinkSettings(newlyCreatedLinkSettings);
			}
		}catch(Exception ex) {
			LOGGER.error(ExceptionUtils.getStackTrace(ex));
		}
		return form;
	}
	@Override
	protected WorkItemContext createCtx(ItemLocationForm form,Integer personID,Locale locale){
		Boolean accessLevelFlag=form.isAccessLevelFlag();
		Integer issueTypeID=form.getIssueTypeID();
		Integer projectID=form.getProjectID();
		Integer parentID=form.getParentID();
		
		TWorkItemBean wibeanParent = null;
		try {
			wibeanParent = ItemBL.loadWorkItem(parentID);
		} catch (ItemLoaderException e) {
			LOGGER.error("getting the workItem failed with " + e.getMessage());
		}
		
		WorkItemContext ctx=ItemBL.createNewItemChild(projectID, issueTypeID,  accessLevelFlag,personID, locale);
		ctx.getWorkItemBean().setSuperiorworkitem(parentID);
		if (wibeanParent!=null) {
			ctx.getWorkItemBean().setStartDate(wibeanParent.getStartDate());
			ctx.getWorkItemBean().setEndDate(wibeanParent.getEndDate());
		}
        Set<Integer> presentFields = ctx.getPresentFieldIDs();
		presentFields.add(SystemFields.INTEGER_STARTDATE);
		presentFields.add(SystemFields.INTEGER_ENDDATE);
		presentFields.add(SystemFields.INTEGER_SUPERIORWORKITEM);
		return  ctx;
	}
	
	@Override
	public Map<String, Object> editItem(Map<String, Object> session,
			Integer workItemID, Map<String, Object> params,
			Integer projectID, Integer issueTypeID){
		//boolean accessLevelFlag,
		//accessLevelFlag,
		Map map=super.editItem(session, workItemID, params, projectID, issueTypeID);
		Integer parentID=parseInt(params.get("parentID"));
		TWorkItemBean wibeanParent = null;
		try {
			wibeanParent = ItemBL.loadWorkItem(parentID);
		} catch (ItemLoaderException e) {
			LOGGER.error("getting the workItem failed with " + e.getMessage());
		}
		WorkItemContext ctx= (WorkItemContext) session.get("workItemContext");
		ctx.getWorkItemBean().setSuperiorworkitem(parentID);
		if (wibeanParent!=null) {
			ctx.getWorkItemBean().setStartDate(wibeanParent.getStartDate());
			ctx.getWorkItemBean().setEndDate(wibeanParent.getEndDate());
		}
        Set<Integer> presentFields = ctx.getPresentFieldIDs();
		presentFields.add(SystemFields.INTEGER_STARTDATE);
		presentFields.add(SystemFields.INTEGER_ENDDATE);
		presentFields.add(SystemFields.INTEGER_SUPERIORWORKITEM);
		return map;
	}
	@Override
	public boolean isEnabled(Integer personID, TWorkItemBean workItemBean,
			boolean allowedToChange, boolean allowedToCreate, int appEdition) {								
		if (workItemBean==null || workItemBean.getObjectID()==null) {			
			return false;
		}												
		TStateBean stateBean = LookupContainer.getStatusBean(workItemBean.getStateID());
		if (stateBean!=null && stateBean.getStateflag()!=null && 
				stateBean.getStateflag().intValue()==TStateBean.STATEFLAGS.CLOSED) {
			return false;
		}
		return appEdition>=2 && allowedToCreate;
	}
	
}
