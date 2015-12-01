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

/*ScreenController.instance=new ScreenController("updateDashboardScreenProperty","dashboardEdit");
TabController.instance=new TabController("","dashboardTab");
PanelController.instance=new PanelController("updateDashboardPanelProperty","dashboardPanel");
FieldScreenController.instance=new FieldScreenController("updateDashboardFieldProperty","dashboardField");*/





Ext.define('js.ext.com.track.dashboard.DashboardFieldEditWrapper',{
	extend: 'com.trackplus.screen.FieldEditWrapperView',
	emptyText:getText('cockpit.screenEdit.lbl.emptyField'),
	getFieldCls:function(){
		return "dashboardDesignField";
	},
	getFieldWrapperConfig:function(){
		var me=this;
		if(me.model.empty){
			return me.callParent();
		}
		return {
			border: true,
			margin: '0 0 5 5',
			frame: true,
			collapsible:false,
			bodyPadding: 2,
			padding:0,
			title:me.model.name
		}
	}
});

Ext.define('com.trackplus.screen.DashboardEditPanelView',{
	extend:'com.trackplus.screen.PanelView',
	getPanelConfig:function(panelModel,index,length){
		var me=this;
		var cls="screenDesignPanel";
		var margin='0 0 5 0';
		me.myComponentCls=cls;
		return {
			cls:cls,
			style: {
				overflow:'hidden'
			},
			border:true,
			bodyPadding:2,
			margin	 : margin,
			layout: {
				type: 'table',
				columns: panelModel.colsNo,
				tableAttrs: {
					style: {
						width: '100%'
					}
				},
				tdAttrs:{
					width:(100/panelModel.colsNo)+'%',
					style:{
						'vertical-align':'top'
					}
				}
			},
			defaults: {
				frame:false,
				border: false
			}
		};
	}
});


Ext.define('com.trackplus.screen.DashboardScreenEditController',{
	extend:'com.trackplus.screen.ScreenEditController',
	isConfigurableItem:function(){
		var me=this;
		var me=this;
		var model=me.selectedComponent.model;
		var compType=model.type;
		return (compType==="Field");
	},
	configItem:function(){
		var me=this;
		var model=me.selectedComponent.model;
		var compType=model.type;
		if(compType==="Field"){
			var  dashboardID=model.getId();
			var url=me.configURL+dashboardID;
			com.trackplus.dashboard.openConfigDialog(dashboardID,url);
		}
	}

});


Ext.define('com.trackplus.layout.DashboardEditLayout',{
	extend:'com.trackplus.layout.BaseLayout',
	useToolbar:true,
	screenEditFacade:null,
	screenModel:null,
	constructor : function(config) {
		var me = this;
		me.callParent(arguments);
		me.borderLayoutController.setHelpContext("dashboardEdit");
		me.screenModel=com.trackplus.screen.createScreenModel(me.initData.screen);
		me.screenEditFacade = Ext.create('com.trackplus.screen.ScreenEditFacade',{
			screenModel:me.screenModel,
			refreshTabUrl:'dashboardTab!reload.action',
			controllerCls:'com.trackplus.screen.DashboardScreenEditController',
			fieldWrapperCls:'js.ext.com.track.dashboard.DashboardFieldEditWrapper',
			panelViewCls:'com.trackplus.screen.DashboardEditPanelView',
			urlFieldList:'dashboardPluginList!list.action',
			titleFieldList:getText("cockpit.screenEdit.lbl.views"),
			useConfig:true,
			configURL:me.initData.configURL,

			screenAction:'dashboardEdit',
			screenUpdateAction:'updateDashboardScreenProperty',
			tabAction:'dashboardTab',
			tabUpdateAction:'updateDashboardTabProperty',
			panelAction:'dashboardPanel',
			panelUpdateAction:'updateDashboardPanelProperty',
			fieldAction:'dashboardField',
			fieldUpdateAction:'updateDashboardFieldProperty',

			messageDeletePanel:getText('cockpit.screenEdit.question.deletePanel'),
			messageDeleteTab:getText('cockpit.screenEdit.question.deleteTab'),
			messageDeleteField:getText('cockpit.screenEdit.question.deleteField'),
			messageCantDeleteScreen:getText('cockpit.screenEdit.message.cantDeleteScreen'),
			messageCantDeleteLastTab:getText('cockpit.screenEdit.message.cantDeleteLastTab'),
			backAction:me.initData.backAction
		});
		me.onReady(function(){
			var dashToolbar=me.screenEditFacade.getToolbar.call(me.screenEditFacade);
			me.borderLayoutController.setActiveToolbarActionList(dashToolbar);
		});
	},
	createCenterPanel:function(){
		var me=this;
		return me.screenEditFacade.createScreenEditViewComponent.call(me.screenEditFacade);
	}
});
