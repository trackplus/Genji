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


//
//Model
Ext.define('com.trackplus.itemNavigator.QueryContext',{
	extend:'Ext.Base',
	config: {
		id:null,
		queryType:null,
		queryID:null,
		queryName:null,
		dashboardParams:null
	},
	constructor : function(cfg) {
		var me = this;
		var config = cfg || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	}
});

Ext.define('com.trackplus.itemNavigator.ItemNavigatorModel',{
	extend:'Ext.Base',
	config: {
		layout:null,
		issues:null,
		tooManyItems:false,
		listViewData:null,
		milestoneWorkitems:null,
		totalCount:0,
		overflowItems:0,
		count:0,
		lastQueries:null,
		queryContext:null,
		queryFieldCSS:null,
		issueListViewDescriptors:null,
		isFilterView:false,
		maySaveFilterLayout:false,
		summaryItemsBehavior:true,
		lastSelectedViewPerson:null,
		lastSelectedView:null,
		lastSelectedNavigator:null,
		navigator:null,
		holidays:null,
		localizedToolTipLabels:null
	},
	constructor : function(cfg) {
		var me = this;
		var config = cfg || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	}
});


//facade
com.trackplus.itemNavigator.ItemNavigatorFacade=Ext.define('com.trackplus.itemNavigator.ItemNavigatorFacade',{
	extend:'Ext.Base',
	config: {
		model:null,
		useLastQuery:true,
		baseAction:'itemNavigator',
		skipEmptyNodeType:false,
		workItemID:null,
		actionID:null
	},
	statics: {
		createInitialModel: function(initData) {
			var me=this;
			var queryContext=me.createQueryContext(initData.queryContext);
			me.updateCssRules(initData.queryFieldCSS,initData.cssRules);
			return Ext.create('com.trackplus.itemNavigator.ItemNavigatorModel',{
				layout:initData.layout,
				issues:initData.issues,
				tooManyItems:initData.tooManyItems,
				listViewData:initData.listViewData,
				milestoneWorkitems:initData.milestoneWorkitems,
				totalCount:initData.totalCount,
				count:initData.count,
				overflowItems:initData.overflowItems,
				holidays:initData.holidays,
				localizedToolTipLabels:initData.localizedToolTipLabels,
				includeCharts:initData.includeCharts,
				lastQueries:initData.lastQueries,
				queryContext:queryContext,
				queryFieldCSS:initData.queryFieldCSS,
				issueListViewDescriptors:me.createIssueListViewDescriptors(initData.issueListViews),
				isFilterView:initData.isFilterView,
				maySaveFilterLayout:initData.maySaveFilterLayout,
				summaryItemsBehavior:initData.summaryItemsBehavior,
				lastSelectedViewPerson:initData.lastSelectedViewPerson,
				lastSelectedView:initData.lastSelectedView,
				lastSelectedNavigator:initData.lastSelectedNavigator,
				navigator:initData.navigator,
				isActiveTopDownDate:initData.isActiveTopDownDate,
				showBaseline:initData.showBaseline,
				validateRelationships:initData.validateRelationships,
				showDragDropInfoMsg:initData.showDragDropInfoMsg,
				highlightCriticalPath:initData.highlightCriticalPath,
				showBoth:initData.showBoth,
				isPrintItemEditable:initData.isPrintItemEditable
			});
		},
		createIssueListViewDescriptors:function(issueListViews){
			var issueListViewDescriptors=[];
			if(issueListViews!=null){
				for(var i=0;i<issueListViews.length;i++){
					var ilw=issueListViews[i];
					issueListViewDescriptors.push(Ext.create('com.trackplus.itemNavigator.IssueListViewDescriptor',ilw));
				}
			}
			return issueListViewDescriptors;
		},
		createQueryContext:function(queryContextJSON){
			if(queryContextJSON==null){
				queryContextJSON={};
			}
			return Ext.create('com.trackplus.itemNavigator.QueryContext',queryContextJSON);
		},
		updateCssRules:function(fieldListID,cssRules){
			var me=this;
			if(me.oldCss!=null){
				Ext.util.CSS.removeStyleSheet(me.oldCss);
				me.oldCss=null;
			}
			if(cssRules!=null){
				var cssTxt='';
				var clsName;
				for(var i=0;i<cssRules.length;i++){
					var r=cssRules[i];
					clsName='rowCls_'+fieldListID+'_'+ r.id;
					var cls='.'+clsName+',.'+clsName+' .x-grid-td, .'+clsName+' .simpleTreeGridCell .x-grid-cell-inner{';
					cls+= r.rule;
					cls+="}";
					cssTxt+=cls;
				}
				me.oldCss="cssList_"+fieldListID;
				Ext.util.CSS.createStyleSheet(cssTxt,me.oldCss);
			}
		}
	},
	constructor : function(cfg) {
		var me = this;
		var config = cfg || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		me.controller=Ext.create('com.trackplus.itemNavigator.ItemNavigatorController',{
			model:me.model,
			useLastQuery:me.useLastQuery,
			baseAction:me.baseAction,
			skipEmptyNodeType:me.skipEmptyNodeType,
			workItemID:me.workItemID,
			actionID:me.actionID,
			settingsVisible:me.settingsVisible,
			filterEditVisible:me.filterEditVisible
		});
	},
	createView:function(model){
		var me=this;
		return me.controller.createView.call(me.controller);
	},
	createNavigator:function(){
		var me=this;
		return me.controller.createNavigator.call(me.controller);
	},
	createToolbar:function(){
		var me=this;
		return me.controller.createToolbar.call(me.controller);
	}
});


Ext.define('com.trackplus.layout.ItemNavigatorLayout',{
	extend:'com.trackplus.layout.BaseLayout',
	useToolbar:true,
	toolbarCls:'toolbarActions-leftBorder',
	useSelfToolbarSeparators:true,
	itemNavigatorFacade:null,
	selectedGroup:'itemNavigator',
	constructor : function(config) {
		var me = this;
		me.callParent(arguments);
		var model=com.trackplus.itemNavigator.ItemNavigatorFacade.createInitialModel(me.initData);
		me.itemNavigatorFacade=Ext.create('com.trackplus.itemNavigator.ItemNavigatorFacade',{
			model:model,
			workItemID:me.initData.workItemID,
			actionID:me.initData.actionID,
			settingsVisible:me.initData.settingsVisible,
			filterEditVisible:me.initData.filterEditVisible
		});
		me.borderLayoutController.setHelpContext("itemNavigator");
		me.onReady(function(){
			var data=me.initData;
			var toolbar=me.itemNavigatorFacade.createToolbar();
			me.borderLayoutController.setActiveToolbarList(toolbar);
			me.itemNavigatorFacade.controller.issueListFacade.addOrRemoveSaveButton(me.borderLayoutController.getActiveToolbarList());

			if(me.initData.actionID!=null){
				var ctrl=me.itemNavigatorFacade.controller;
				var issueListFacade=ctrl.issueListFacade;
				var nodeDataArray=issueListFacade.listViewPlugin.selectItem(me.initData.workItemID);
				var workItemIndex=null;
				if(nodeDataArray!=null&&nodeDataArray.length>0){
					workItemIndex=nodeDataArray[0]['workItemIndex'];
				}
				ctrl.executeItemAction.call(ctrl,me.initData.workItemID,me.initData.actionID,null,ctrl.navigate,ctrl,workItemIndex);
			}

		});
	},
	createCenterPanel:function(){
		var me=this;
		return me.itemNavigatorFacade.createView();
	},
	createWestPanel:function(){
		return this.itemNavigatorFacade.createNavigator.call(this.itemNavigatorFacade);
	},
	refresh:function(){
		var me=this;
		me.itemNavigatorFacade.controller.refresh();
	}
});
