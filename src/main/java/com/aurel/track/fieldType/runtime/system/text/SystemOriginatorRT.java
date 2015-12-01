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


package com.aurel.track.fieldType.runtime.system.text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.accessControl.AccessBeans.AccessFlagIndexes;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.base.DropDownContainer;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherValue;
import com.aurel.track.fieldType.runtime.matchers.design.MatcherDatasourceContext;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.util.GeneralUtils;

/**
 * Runtime type for Originator
 * @author Tamas Ruff
 *
 */
public class SystemOriginatorRT extends SystemLookupBaseRT {

	
	/**
	 * Actualizes the dropDownContainer with the datasource for a select type field 
	 * by editing an existing issue or creating a new issue.
	 * The result might differ from loadCreateDataSource because it might be needed 
	 * to add the current value from the workItem to the list entries 
	 * (when right already revoked for the current value).
	 * The datasource for originator should contain only the originator person itself, and that just for existing workItem
	 * @param selectContext
	 * @param dropDownContainer 
	 * @return
	 */
	@Override
	public void processLoadDataSource(SelectContext selectContext, DropDownContainer dropDownContainer) {
		List dataSource = new ArrayList();
		TWorkItemBean workItemBean = selectContext.getWorkItemBean();
		Integer originator = workItemBean.getOriginatorID();
		if (originator==null) {
			//new workItem
			return;
		}
		TPersonBean personBean = LookupContainer.getPersonBean(originator);
		if (personBean!=null) {
			dataSource.add(personBean);
		}		
		dropDownContainer.setDataSourceList(MergeUtil.mergeKey(selectContext.getFieldID(), selectContext.getParameterCode()), dataSource);
	}
	
	/**
	 * Loads the datasource for the matcher
	 * used by select fields to get the possible values
	 * It will be called from both field expressions and upper selects 
	 * The value can be a list for simple select or a map of lists for composite selects or a tree
	 * @param matcherValue
	 * @param matcherDatasourceContext the data source may be project dependent. 
	 * @param parameterCode for composite selects
	 * @return the datasource (list or tree)
	 */	
	@Override
	public Object getMatcherDataSource(IMatcherValue matcherValue, MatcherDatasourceContext matcherDatasourceContext, Integer parameterCode) {
		//get the persons who have create role in any selected project 
		List<ILabelBean> originatorsList = new LinkedList<ILabelBean>();
		Integer[] projects = matcherDatasourceContext.getProjectIDs();
		Integer[] ancestorProjects = matcherDatasourceContext.getAncestorProjectIDs();
		if (projects!=null && projects.length>0) {
			int[] arrRights = new int[] { AccessFlagIndexes.CREATETASK, AccessFlagIndexes.PROJECTADMIN };
			Set<Integer> originatorsByRoleSet = AccessBeans.getPersonSetByProjectsRights(ancestorProjects, arrRights);
			List<TPersonBean> originatorsByRole = PersonBL.getDirectAndIndirectPersons(GeneralUtils.createIntegerListFromCollection(originatorsByRoleSet), true, true, null);
			//get the persons who are originators in existing issues: they are not necessary 
			//a subset of the persons with create role because it could happen that somebody
			//is originator of an issue but his create role was revoked for the project 
			List<TPersonBean> originatorInIssues = PersonBL.loadUsedOriginatorsByProjects(matcherDatasourceContext.getPersonBean().getObjectID(), projects);
			Set<TPersonBean> originators = new TreeSet<TPersonBean>();
			originators.addAll(originatorsByRole);
			originators.addAll(originatorInIssues);
			originatorsList.addAll(GeneralUtils.createListFromSet(originators));
		}
		//add the symbolic user as the first entry in the list
		Locale locale = matcherDatasourceContext.getLocale();
		originatorsList.add(0, new SystemOriginatorRT().getLabelBean(MatcherContext.LOGGED_USER_SYMBOLIC, locale));
		if (matcherDatasourceContext.isWithParameter()) {
			originatorsList.add(new SystemOriginatorRT().getLabelBean(MatcherContext.PARAMETER, locale));
		}
		if (matcherDatasourceContext.isInitValueIfNull() && matcherValue!=null) {
			Object value = matcherValue.getValue();
			if (value==null) {
				matcherValue.setValue(new Integer[] {originatorsList.get(0).getObjectID()});
			}
		}
		return originatorsList;
	}
	
	/**
	 * Whether the value of this field can be changed
	 * @return
	 */
	@Override
	public boolean isReadOnly() {
		return true;
	}
	
}
