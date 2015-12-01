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

package com.aurel.track.item.operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.itemNavigator.ItemNavigatorFilterBL;
import com.aurel.track.itemNavigator.navigator.MenuItem;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.TreeNode;

/**
 *
 */
public class ItemOperationSelectProject extends ItemOperationSelect {
	private static final Logger LOGGER = LogManager.getLogger(ItemOperationSelectProject.class);
	public ItemOperationSelectProject(Locale locale){
		super(SystemFields.PROJECT,true,true,true,locale);
		this.iconName= ProjectBL.PROJECT_ICON_CLASS;
		this.name = LocalizeUtil.getLocalizedTextFromApplicationResources("menu.admin.project", locale);
	}


	@Override
	public List<MenuItem> getChildren(TPersonBean personBean, Locale locale, Integer nodeID){
		List<TreeNode> projects=ReleaseBL.getReleaseNodesEager(personBean, null,false,true,true,true,null,false,true,false,null,locale);
		List<MenuItem> menuItems=null;
		if(projects!=null && !projects.isEmpty()){
			String type=ItemNavigatorFilterBL.NODE_TYPE_PREFIX.ITEM_OPERATION+ItemNavigatorFilterBL.NODE_TYPE_SEPARATOR+pluginID;
			menuItems=ItemNavigatorFilterBL.createMenuItemsFromProjectNodes(projects,type);
		}
		return menuItems;
	}

	@Override
	protected List<ILabelBean> getOptionsInternal(TPersonBean personBean,Locale locale,Integer projectID) {
		return null;
	}

	@Override
	protected String getSymbol(ILabelBean labelBean) {
		return null;
	}

	@Override
	protected boolean getAllowDrop(ILabelBean labelBean){
		return  this.allowDrop&&labelBean.getObjectID().intValue()>0;
	}

	@Override
	public String getIconCls(ILabelBean labelBean) {
		Integer beanID=labelBean.getObjectID();
		String result=null;
		if(beanID.intValue()>0){
			//release
			result="release-ticon";
		}else{
			//project
			result=ProjectBL.PROJECT_ICON_CLASS;
		}
		return result;
	}
	@Override
	public List<ReportBean> filter(List<ReportBean> reportBeans, Integer nodeObjectID){
		List<ReportBean> result=new ArrayList<ReportBean>();
		if(nodeObjectID!=null&&nodeObjectID.intValue()>0){
			//release
			Integer releaseFieldID=SystemFields.INTEGER_RELEASE;
			Integer releaseID=nodeObjectID;
			ReportBean reportBean;
			TWorkItemBean workItemBean;
			Set<Integer> releaseDescendants=ReleaseBL.getDescendantReleasesAsSet(releaseID);
			//include release itself
			releaseDescendants.add(releaseID);
			for(int i=0;i<reportBeans.size();i++){
				reportBean=reportBeans.get(i);
				workItemBean=reportBean.getWorkItemBean();
				Object fieldValue= workItemBean.getAttribute(releaseFieldID);
				if(releaseDescendants.contains(fieldValue)){
					result.add(reportBean);
				}
			}
			return result;
		}else{
			Integer projectID=null;
			if(nodeObjectID!=null){
				projectID=Integer.valueOf(nodeObjectID.intValue()*-1);
			}
			ReportBean reportBean;
			TWorkItemBean workItemBean;
			List<Integer> projectDescendants=ProjectBL.getDescendantProjectIDsAsList(projectID);
			//include project itself
			projectDescendants.add(projectID);
			for(int i=0;i<reportBeans.size();i++){
				reportBean=reportBeans.get(i);
				workItemBean=reportBean.getWorkItemBean();
				Object fieldValue= workItemBean.getProjectID();
				if(projectDescendants.contains(fieldValue)){
					result.add(reportBean);
				}
			}
			return result;
		}
	}

	@Override
	public void execute(int[] workItemIds, Integer nodeObjectID, Map<String, String> params, Integer personID, Locale locale) throws ItemOperationException {
		Integer targetFieldID;
		if(nodeObjectID!=null&&nodeObjectID.intValue()>0){
			//release
			targetFieldID=SystemFields.INTEGER_RELEASE;
			Integer projectID=null;
			TReleaseBean releaseBean=LookupContainer.getReleaseBean(nodeObjectID);
			if (releaseBean!=null) {
				projectID=releaseBean.getProjectID();
				HashMap targetValue=new HashMap<Integer,Integer>();
				targetValue.put(projectID,nodeObjectID);
				executeInternal(targetFieldID,workItemIds,targetValue,params,personID,locale);
			}
		}else{
			//project
		}
	}
}
