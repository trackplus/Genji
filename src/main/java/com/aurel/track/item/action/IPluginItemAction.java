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

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;

public interface IPluginItemAction {
	public String encodeJsonDataStep1(Locale locale, TPersonBean user, Integer workItemID,
									  Integer parentID, Integer projectID, Integer issueTypeID, String synopsis, String description) throws PluginItemActionException;
	
	public WorkItemContext next(Locale locale, TPersonBean user, Integer workItemID, Integer parentID, Map<String, Object> params, String synopsis, String description)
			throws PluginItemActionException;
	
	public boolean canFinishInFirstStep();
	
	public Integer saveInFirsStep(Locale locale,TPersonBean user,Integer workItemID,Map<String, Object> params) throws PluginItemActionException;
	public boolean isEnabled(Integer personID, TWorkItemBean workItemBean, boolean allowedToChange, boolean allowedToCreate, int appEdition);
	
}
