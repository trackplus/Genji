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

Ext.define('js.ext.com.track.dashboard.ManagerView',{
	extend:'js.ext.com.track.dashboard.DashboardRenderer',
	layout:'anchor',
	defaults:{
		anchor:'100%'
	},
	initComponent : function(){
		var me=this;
		me.items=me.createChildren();
		me.callParent();
	},
	createChildren:function(){
		var me=this;
		if(me.jsonData.tooManyItems==true){
			return [me.createErrorCmp(getText('cockpit.err.tooManyItems'))];
		}
		var projects=me.jsonData.projects;
		var myGridData=[];
		if(projects!=null){
			for(var i=0;i<projects.length;i++){
				myGridData.push({
					label:projects[i].label,
					h2:true,
					category:true
				});

				myGridData.push({
					label:getText('managerView.priority'),
					category:true
				});
				myGridData=me.addAll(myGridData,
					com.trackplus.dashboard.updateElementWrapperList(projects[i].list,projects[i]));

				myGridData.push({
					label:getText('managerView.user'),
					category:true
				});
				myGridData=me.addAll(myGridData,com.trackplus.dashboard.updateElementWrapperList(projects[i].secondList,{
					groupByFieldType:projects[i].groupByFieldType2,
					projectID:projects[i].projectID,
					releaseID:projects[i].releaseID
				}));
			}
		}else{
			var project=me.jsonData.result;
			if(typeof(project) !== 'undefined' && project != null) {
				myGridData.push({
					label:getText('managerView.priority'),
					category:true
				});
				myGridData=me.addAll(myGridData,
				com.trackplus.dashboard.updateElementWrapperList(project.list,project));

				myGridData.push({
					label:getText('managerView.user'),
					category:true
				});
				myGridData=me.addAll(myGridData,com.trackplus.dashboard.updateElementWrapperList(project.secondList,{
					groupByFieldType:project.groupByFieldType2,
					projectID:project.projectID,
					releaseID:project.releaseID
				}));
			}
		}
		return [com.trackplus.dashboard.createPercentGroup(null,myGridData,me)];
	},
	doRefresh:function(jsonData){
		var me=this;
		me.jsonData=jsonData;
		me.removeAll();
		me.add(me.createChildren());
	}
});
