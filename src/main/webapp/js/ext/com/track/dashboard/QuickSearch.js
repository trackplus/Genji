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

Ext.define('js.ext.com.track.dashboard.QuickSearch',{
	extend:'js.ext.com.track.dashboard.DashboardRenderer',
	bodyPadding:'5 5 5 5',
	initComponent : function(){
		var me=this;
		me.items=me.createChildren();
		me.callParent();
	},
	createChildren:function(){
		var me=this;
		var projectID=null;
		if(typeof(me.jsonData.projectID) !== 'undefined' && me.jsonData.projectID != null) {
			projectID=me.jsonData.projectID;
		}
		var items=[];
		if(projectID==null){
			/*var dummyData=[
				{id:'1',text:'aa',leaf:true,selectable:true,checked:false},
				{id:'2',text:'bb',leaf:false,selectable:true,checked:false,
					children:[
						{id:'10',text:'bb-000',leaf:true,selectable:true,checked:false},
						{id:'11',text:'bb-111',leaf:true,selectable:true,checked:false},
						{id:'12',text:'bb-222',leaf:true}
					]
				},
				{id:'3',text:'cc',leaf:true},
				{id:'4',text:'dd',leaf:true,selectable:true,checked:false},
				{id:'101',text:'sfswf',leaf:true,selectable:true,checked:false},
				{id:'102',text:'asdfsda',leaf:true,selectable:true,checked:false},
				{id:'103',text:'asdfa',leaf:true,selectable:true,checked:false},
				{id:'104',text:'sfsdfaa',leaf:true,selectable:true,checked:false},
				{id:'105',text:'asdfsdfa',leaf:true,selectable:true,checked:false},
				{id:'106',text:'asdfsda',leaf:true,selectable:true,checked:false},
				{id:'107',text:'asdfa',leaf:true,selectable:true,checked:false},
				{id:'108',text:'asdfsdfa',leaf:true,selectable:true,checked:false}
			]; */
			me.projectPicker=Ext.create('com.trackplus.util.MultipleTreePicker',{
				data:me.jsonData.projects,
				margin:'0 5 0 0',
				width:250,
				matchFieldWidth:false,
				pickerWidth:500,
				name:'projectID',
				localizedLabel:me.jsonData.projectLabel
			});
			me.projectPicker.addListener('change',me.projectChange,me);
			items.push(me.projectPicker);
		}

		me.issueTypeSelect= Ext.create('com.trackplus.util.MultipleSelectPicker',{
			data:me.jsonData.issueTypes,
			width:200,
			margin:'0 5 0 0',
			name:'issueType',
			localizedLabel:me.jsonData.issueTypeLabel,
			iconUrlPrefix:'optionIconStream.action?fieldID=-2&optionID=',
			value:me.jsonData.selectedIssueTypes
		});
		items.push(me.issueTypeSelect);

		me.statusSelect= Ext.create('com.trackplus.util.MultipleSelectPicker',{
			data:me.jsonData.states,
			margin:'0 5 0 0',
			width:200,
			name:'status',
			localizedLabel:me.jsonData.statusLabel,
			iconUrlPrefix:'optionIconStream.action?fieldID=-4&optionID=',
			value:me.jsonData.selectedStates
		});

		items.push(me.statusSelect);
		var btnExecute=Ext.create('Ext.Button', {
			text: getText('common.btn.execute'),
			scope: me,
			scale: 'small',
			iconCls:'filter-ticon',
			handler: function(button) {
				me.executeFilter.call(me);
			}
		});
		items.push(btnExecute);
		var panel=Ext.create('Ext.panel.Panel', {
			border:	false,
			margin:"0 0 0 0",
			region:'center',
			layout:'column',
			items:items
		});
		return [panel];
	},
	executeFilter:function(){
		var me=this;
		var urlStr='itemNavigator.action';
		var selectedProjects=me.selectedProjects;
		var selectedIssueTypes=me.issueTypeSelect.getSubmitValue();
		var selectedStates=me.statusSelect.getSubmitValue();
		var params={
			'queryType':2,//dashboard
			'queryID':me.jsonData.dashboardID,
			'dashboardParams.dashboardID':me.jsonData.dashboardID,
			'dashboardParams.selectedProjects':selectedProjects,
			'dashboardParams.selectedIssueTypes':selectedIssueTypes,
			'dashboardParams.selectedStates':selectedStates
		};
		//overwrite projectID and entity type if dashboard is in browse projects page
		var projectID=null;
		var entityFlag=null;
		if(me.jsonData.projectID!=null){
			projectID=me.jsonData.projectID;
			entityFlag=me.jsonData.entityType;
		}
		if(me.jsonData!=null&&me.jsonData.releaseID!=null){
			projectID=me.jsonData.releaseID;
			entityFlag=me.jsonData.entityType;
		}
		if(projectID!=null){
			params['dashboardParams.projectID']=projectID;
			params['dashboardParams.entityFlag']=entityFlag;
		}
		var dummyForm = Ext.create('Ext.form.Panel', {
			items:[],
			url:urlStr,
			standardSubmit:true
		});
		dummyForm.getForm().submit({
			params:params
		});
	},
	doRefresh:function(jsonData){
		var me=this;
		me.jsonData=jsonData;
		if(jsonData['refreshProject']==true){
			me.issueTypeSelect.store.loadData(me.jsonData.issueTypes);
			me.issueTypeSelect.setValue(me.jsonData.selectedIssueTypes);

			me.statusSelect.store.loadData(me.jsonData.states);
			me.statusSelect.setValue(me.jsonData.selectedStates);
		}else{
			me.removeAll();
			me.add(me.createChildren());
		}
	},
	projectChange:function(comp, selectedProjects, oldValue, eOpts){
		var me=this;
		me.selectedProjects=selectedProjects;
		var selectedIssueTypes=me.issueTypeSelect.getSubmitValue();
		var selectedStates=me.statusSelect.getSubmitValue();
		var params={
			'params.selectedProjects':selectedProjects,
			'params.selectedIssueTypes':selectedIssueTypes,
			'params.selectedStates':selectedStates,
			'params.refreshProject':true
		};
		me.refreshDashboard(params);
	}
});
