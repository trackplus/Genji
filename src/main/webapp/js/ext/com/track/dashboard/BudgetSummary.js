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

Ext.define('js.ext.com.track.dashboard.BudgetSummary',{
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
		var projects=me.jsonData.result;
		var prjItems=[];
		prjItems.push(me.createProjectCmp(projects));
		prjItems.push({
			xtype:'displayfield',
			fieldLabel:getText('budgetSummary.display.sumOverdueEstimatedBudgets'),
			labelStyle:{overflow:'hidden'},
			labelWidth:400,
			labelAlign:'right',
			value:me.jsonData.sumOverdueEstimatedBudgets
		});
		prjItems.push({
			xtype:'displayfield',
			fieldLabel:getText('budgetSummary.display.sumDueThisWeekEstimatedBudgets'),
			labelStyle:{overflow:'hidden'},
			labelWidth:400,
			labelAlign:'right',
			value:me.jsonData.sumDueThisWeekEstimatedBudgets
		});
		return prjItems;
	},
	doRefresh:function(jsonData){
		var me=this;
		me.jsonData=jsonData;
		me.removeAll();
		me.add(me.createChildren());
	},
	addProjectData:function(projectData,project){
		var groupItem=project.groupItem;
		projectData.push({
			rowType:5,
			label:'<b>'+groupItem.label+'</b>',
			numberOfWorkItems:groupItem.numberOfWorkItems,
			sumBudget:groupItem.sumBudget,
			sumBookedCosts:groupItem.sumBookedCosts,
			estimatedBudget:groupItem.estimatedBudget,
			completion:groupItem.completion,
			consumptionValue:groupItem.consumptionValue,
			consumption:groupItem.consumption
		});
		var categories=project.categories;
		for(var i=0;i<categories.length;i++){
			var category=categories[i];
			var items=category.items;
			for(var j=0;j<items.length;j++){
				var item=items[j];
				projectData.push({
					rowType:(j==0?1:null),
					groupName:(j==0?category.label:null),
					bgColor:(j==0?category.bgColor:null),
					label:item.label,
					groupItemID:project.objectID,
					type:item.type,
					category:category.id,
					numberOfWorkItems:item.numberOfWorkItems,
					sumBudget:item.sumBudget,
					sumBookedCosts:item.sumBookedCosts,
					estimatedBudget:item.estimatedBudget,
					completion:item.completion,
					consumptionValue:item.consumptionValue,
					consumption:item.consumption
				});
			}
		}
	},
	createProjectCmp:function(projects){
		var me=this;
		var projectData=[];
		if(projects!=null&&projects.length>0){
			if(projects.length==1){
				me.addProjectData(projectData,projects[0]);
			}else{
				for(var i=0;i<projects.length;i++){
					me.addProjectData(projectData,projects[i]);
					if(i<projects.length-1){
						//add separator row
						projectData.push({
							rowType:-1
						});
					}
				}
			}
		}

		var store = Ext.create('Ext.data.Store', {
			fields: [
				{name:'rowType',type:'int'},
				{name: 'groupName'},
				{name: 'bgColor'},
				{name: 'label'},
				{name:'groupItemID',type:'int'},
				{name:'category',type:'int'},
				{name: 'type',type:'int'},
				{name: 'numberOfWorkItems',type:'int'},
				{name: 'sumBudget',type: 'string'},
				{name: 'sumBookedCosts',type: 'string'},
				{name: 'estimatedBudget',type: 'string'},
				{name: 'completion',type: 'string'},
				{name: 'consumptionValue',type: 'float'},
				{name: 'consumption',type: 'string'}
			],
			data: projectData
		});

		return Ext.create('Ext.grid.Panel', {
			store: store,
			columns: [
				{
					text	 : '',
					flex:1,
					dataIndex: 'groupName',
					sortable:false,
					menuDisabled:true,
					renderer:me.renderGroupName
				},{
					xtype:'linkcolumn',
					text	 :'',
					flex:3,
					sortable:false,
					menuDisabled:true,
					dataIndex: 'label',
					handler:me.clickOnLabel,
					scope:me,
					isLink:me.isLabelLink,
					postProcessRenderer:me.postProcessRendererLabel
				},{
					text	 :getText('budgetSummary.display.numberOfWorkItems'),
					flex:1,
					sortable:false,
					menuDisabled:true,
					renderer : me.formatNumbers,
					dataIndex: 'numberOfWorkItems'
				},{
					text	 :getText('budgetSummary.display.sumBudget'),
					flex:1,
					sortable:false,
					menuDisabled:true,
					renderer : me.formatNumbers,
					dataIndex: 'sumBudget'
				},{
					text	 :getText('budgetSummary.display.sumBookedCosts'),
					flex:1,
					sortable:false,
					renderer : me.formatNumbers,
					dataIndex: 'sumBookedCosts'
				},{
					text	 :getText('budgetSummary.display.estimatedBudget'),
					flex:1,
					sortable:false,
					menuDisabled:true,
					renderer : me.formatNumbers,
					dataIndex: 'estimatedBudget'
				},{
					text	 :getText('budgetSummary.display.completion'),
					flex:1,
					sortable:false,
					menuDisabled:true,
					renderer : me.formatPercent,
					dataIndex: 'completion'
				},{
					text	 :getText('budgetSummary.display.consumption'),
					flex:1,
					sortable:false,
					menuDisabled:true,
					renderer : me.renderConsumption,
					dataIndex: 'consumption'
				}

			],
			border:false,
			columnLines :false,
			margins: '0 0 0 0',
			lines :false,
			viewConfig: {
				stripeRows: true
			},
			autoHeight:true
		});
	},
	renderGroupName:function(value,metaData,record,rowIndex,colIndex,store,view){
		var rowType=record.data.rowType;
		if(rowType==-1){
			return "";
		}
		if(rowType==0||rowType==5){
			return '<div style="padding:2px">&nbsp;</div>';
		}
		var bgColor=null;
		if(rowType==1) {
			//group
			bgColor=record.data.bgColor;
		}
		return '<div style="border: 1px solid black;padding:2px;color:white;font-weight: bold;background-color:'+bgColor+';">'+value+'</div>';
	},

	isLabelLink:function(value,metaData,record,rowIndex,colIndex,store,view){
		var rowType=-1;
		if(record!=null){
			rowType=record.data.rowType;
		}
		var result=false;
		switch (rowType) {
			case -1://line break
				result=false;
				break;
			case 5://project
				result=false;
				break;
			default://regular line
				result=(record.data.numberOfWorkItems>0);
				break;
		}
		return result;
	},
	postProcessRendererLabel:function(value,metaData,record,rowIndex,colIndex,store,view){
		var rowType=-1;
		if(record!=null){
			rowType=record.data.rowType;
		}
		var htmlStr="";
		switch (rowType) {
			case -1://line break
				htmlStr="";
				break;
			case 5://project
				htmlStr='<div style="text-align: center;font-weight: bold">'+value+'</div>';
				break;
			default://regular line
				htmlStr=value;
				break;
		}
		return htmlStr;
	},

	clickOnLabel:function(record,cellIndex){
		var me=this;
		var urlStr='itemNavigator.action';
		var params={
			'queryType':2,//dashboard
			'queryID':me.jsonData.dashboardID,
			'dashboardParams.dashboardID':me.jsonData.dashboardID
		};
		var projectID=null;
		var entityFlag=null;
		//overwrite projectID and entity type if dashboard is in browse projects page
		if(me.jsonData!=null&&me.jsonData.projectID!=null){
			projectID=me.jsonData.projectID;
			entityFlag=me.jsonData.entityType;
		}
		if(me.jsonData!=null&&me.jsonData.releaseID!=null){
			projectID=me.jsonData.releaseID;
			entityFlag=me.jsonData.entityType;
		}

		var category=record.data['category'];
		var type=record.data['type'];
		var groupItemID=record.data['groupItemID'];
		if(projectID!=null){
			params['dashboardParams.projectID']=projectID;
			params['dashboardParams.entityFlag']=entityFlag;
		}
		params['dashboardParams.category']=category;
		params['dashboardParams.type']=type;
		params['dashboardParams.groupItemID']=groupItemID;

		var dummyForm = Ext.create('Ext.form.Panel', {
			items:[],
			url:urlStr,
			standardSubmit:true
		});
		dummyForm.getForm().submit({
			params:params
		});
	},
	formatNumbers:function(value,metaData,record,rowIndex,colIndex,store,view){
		var rowType=record.data.rowType;
		if(rowType==-1){
			return "";
		}
		if(rowType==5){//project
			return '<div style="text-align: center;font-weight: bold">'+value+'</div>';
		}else{
			return '<div style="text-align: center;">'+value+'</div>';
		}
	},
	formatPercent:function(value,metaData,record,rowIndex,colIndex,store,view){
		var rowType=record.data.rowType;
		if(rowType==-1){
			return "";
		}
		if(rowType==5){//project
			return '<div style="text-align: center;font-weight: bold">'+value+'</div>';
		}else{
			return '<div style="text-align: center;">'+value+'</div>';
		}
	},
	renderConsumption:function(value,metaData,record,rowIndex,colIndex,store,view){
		var rowType=record.data.rowType;
		if(rowType==-1){
			return "";
		}
		var bgColor='green';
		if(record.data.consumptionValue > 1.0){
			bgColor='red';
		}
		if(record.data.consumptionValue == 1.0){
			bgColor='orange';
		}
		return '<div style="border: 1px solid black;padding:2px;color:white;text-align: center;background-color:'+bgColor+';">'+record.data.consumption+'</div>';
	}
});
