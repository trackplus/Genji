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

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLocationForm;
import com.aurel.track.util.StringArrayParameterUtils;

public class AbstractPluginItemAction implements IPluginItemAction{
	public String encodeJsonDataStep1(Locale locale, TPersonBean user, Integer workItemID,
									  Integer parentID, Integer projectID, Integer issueTypeID, String synopsis, String description) throws PluginItemActionException{
		return "{}";
	}
	public WorkItemContext next(Locale locale, TPersonBean user, Integer workItemID, Integer parentID, Map<String, Object> params, String synopsis, String description) throws PluginItemActionException {
		return null;
	}
	public Integer saveInFirsStep(Locale locale, TPersonBean user,Integer workItemID, Map<String, Object> params)
			throws PluginItemActionException {
		return null;
	}
	/**
	 * @deprecated To be removed
	 * @param session
	 * @param workItemID
	 * @param params
	 * @param projectID
	 * @param issueTypeID
	 * @return
	 */
	@Deprecated
	public Map<String, Object> editItem(Map<String, Object> session,
			Integer workItemID, Map<String, Object> params,
			Integer projectID, Integer issueTypeID){
		return params;
	}

	public boolean canFinishInFirstStep(){
		return false;
	}
	
	protected ItemLocationForm extractItemLocation(Locale locale,TPersonBean user, Map<String, Object> params,Integer workItemID,Integer parentID) {
		Boolean accessLevelFlag = Boolean.FALSE;
		Integer projectID=null;
		Integer issueTypeID=null;
		if (params.get("accessLevelFlag")!=null) {
			accessLevelFlag = StringArrayParameterUtils.getBooleanValue(params, "accessLevelFlag");
		}
		projectID=StringArrayParameterUtils.parseIntegerValue(params, "projectID");
		issueTypeID=StringArrayParameterUtils.parseIntegerValue(params, "issueTypeID");
		ItemLocationForm form=new ItemLocationForm();
		form.setAccessLevelFlag(accessLevelFlag);
		form.setIssueTypeID(issueTypeID);
		form.setProjectID(projectID);
		ItemBL.updateItemLocationFormLabels(form,locale, projectID, issueTypeID, user);
		return form;
	}

	protected Boolean parseBoolean(Object s){
		if(s==null){
			return Boolean.FALSE;
		}
		if(s instanceof Boolean){
			return (Boolean)s;
		}
		try{
			return Boolean.valueOf(((Object[])s)[0].toString());
		}catch (Exception e) {
			return  Boolean.FALSE;
		}
	}
	protected Integer parseInt(Object s){
		if(s==null){
			return null;
		}
		if(s instanceof Integer){
			return (Integer)s;
		}
		try{
			return new Integer(((Object[])s)[0].toString());
		}catch (Exception e) {
			return  null;
		}
	}
	public boolean isEnabled(Integer personID,  TWorkItemBean workItemBean,
			boolean allowedToChange, boolean allowedToCreate,
			/*Map<String, Object> session, Integer workItemID,
			Integer projectID, Integer issueTypeID,*/int appEdition) {
		return true;
	}
	
}
