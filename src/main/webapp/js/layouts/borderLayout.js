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




Ext.define('com.trackplus.layout.BaseLayout',{
	extend:'Ext.Base',
	config: {
		initData:{},
		useToolbar:false,
		toolbarCls:null,
		useSelfToolbarSeparators:false,
		borderLayoutController:null,
		selectedGroup:null
	},
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		this.initConfig(config);
	},
	createWestPanel:function(){
		return null;
	},
	createCenterPanel:function(){
		return Ext.create('Ext.panel.Panel',{
			layout:'fit',
			cls: 'centerPanel',
			border:false,
			bodyBorder:false,
			loader:{}
		});
	},
	onReady:function(fn, scope) {
		this.borderLayoutController.onReady.call(this.borderLayoutController,fn,scope);
	},
	historyChange:function(token){
	}

});



Ext.define('com.trackplus.layout.BorderLayout', {
	extend: 'Ext.Base',
	config: {
		layoutCls: 'com.trackplus.layout.BaseLayout',
		initData: {}
	},
	borderLayoutController: null,
	view: null,
	constructor: function (config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		this.initConfig(config);
		me.borderLayoutController = Ext.create('com.trackplus.layout.BorderLayoutController', {
			layoutCls: me.layoutCls,
			initData: me.initData
		});
		Ext.History.on('change', me.historyChange, me);
	},
	historyChange: function (token) {
		var me = this;
		me.borderLayoutController.historyChange(token);
	},
	setHelpContext: function (helpContext) {
		this.borderLayoutController.setHelpContext.call(this.borderLayoutController, helpContext);
	},
	addHistoryToken: function (token) {
		Ext.History.add(token);
	},
	showHelp: function () {
		this.borderLayoutController.showHelp.call(this.borderLayoutController);
	},

	createView: function () {
		var me = this;
		me.view = me.borderLayoutController.createView.call(me.borderLayoutController);
		return me.view;
	},
	notifyReady: function () {
		var me = this;
		this.borderLayoutController.notifyReady.call(this.borderLayoutController);
		var bigToolbar = me.view.header.toolbar;
		var baseLayout = this.borderLayoutController.baseLayout;
		if (baseLayout !== -null) {
			var selectedBtn = baseLayout.selectedGroup;
			if (selectedBtn ) {
				var btn = bigToolbar.getComponent(selectedBtn);
				if (btn ) {
					btn.toggle(true);
				}
			}
		}
	},
	setLoading: function (arg) {
		if(this.view){
			this.view.setLoading(arg);
		}
	},
	setCenterContent: function (component) {
		this.borderLayoutController.setCenterContent.call(this.borderLayoutController, component);
	},
	getActiveToolbarList: function () {
		return this.borderLayoutController.getActiveToolbarList.call(this.borderLayoutController);
	},
	createItemToolbarSeparator: function () {
		return this.borderLayoutController.createItemToolbarSeparator.call(this.borderLayoutController);
	},
	setActiveToolbarList: function (items) {
		this.borderLayoutController.setActiveToolbarList.call(this.borderLayoutController, items);
	},
	setActiveToolbarActionList: function (items) {
		this.borderLayoutController.setActiveToolbarActionList.call(this.borderLayoutController, items);
	},
	setVisibleToolbar: function (visible) {
		this.borderLayoutController.setVisibleToolbar.call(this.borderLayoutController, visible);
	},
	getWidth:function(){
		var me=this;
		if(me.view){
			return me.view.getEl().getWidth();
		}
		return Ext.getBody().getWidth();
	},
	getHeight:function(){
		var me=this;
		if(me.view){
			return me.view.getEl().getHeight();
		}
		return Ext.getBody().getHeight();
	},
	ensureSize:function(width,height){
		var MARGIN=10;
		var appWidth=borderLayout.getWidth();
		var appHeight=borderLayout.getHeight();
		if(appWidth&&width>appWidth-2*MARGIN){
			width=appWidth-2*MARGIN;
		}
		if(appHeight&&height>appHeight-2*MARGIN){
			height=appHeight-2*MARGIN;
		}
		return{
			width:width,
			height:height
		}
	},
	ensureDialogSizeFromState:function(dialog,state,position){
		var me=this;
		var w=state.width;
		var h=state.height;
		var x=null;
		var y=null;
		if(state.pos){
			x=state.pos[0];
			y=state.pos[1];
		}
		if(position){
			dialog.x=position[0];
			dialog.y=position[1];
		}
		var size=me.ensureSize(state.width,state.height);
		if(dialog.x<0){
			dialog.x=10;
		}
		if(dialog.y<0){
			dialog.y=10;
		}
		if(w!==size.width){
			dialog.width=size.width;
			dialog.x=10;
		}
		if(h!==size.height){
			dialog.height=size.height;
			dialog.y=10;
		}
	},
	onPageMouseDown: function(event) {
	}
});

Ext.define('com.trackplus.layout.HeaderView',{
	extend:'Ext.container.Container',
	config:{
		borderLayoutController:null,
		externalAction:false,
		anonymous:false
	},
	margins: '0 0 0 0',
	cls:'x-panel-header-default headerMaster',
	layout: {
		type: 'hbox',
		padding:'0',
		align:'stretch'
	},

	initComponent: function(){
		var me=this;
		me.items=me.createChildren();
		me.callParent();
	},

	createToolbar:function(){
		var me=this;
		var items=[];
		if(com.trackplus.TrackplusConfig.userName){
			if(me.externalAction===false) {
				var btnCockpitCfg={
					xtype:'button',
					//enableToggle:true,
					//toggleGroup:'bigToolbar',
					itemId:'cockpit',
					text: getText('menu.cockpit'),
					overflowText:getText('menu.cockpit'),
					tooltip:getText('menu.cockpit.tt'),
					style: {
						margin: '0 0 0 10px'
					},
					iconCls: 'cockpit',
					handler:function(){
						window.location.href="cockpit.action";
					}
				};

				var spacerElement = {
					xtype: 'tbspacer',
					cls: 'navigationSeperator'
				};

				if (com.trackplus.TrackplusConfig.user.hasCockpit) {
					items.push(btnCockpitCfg);
					items.push(spacerElement);
				}

				if (com.trackplus.TrackplusConfig.user.hasProjectCockpit) {
					var btnBrowseProjectsCfg={
						xtype:'button',
						text: getText('menu.browseProjects'),
						overflowText:getText('menu.browseProjects'),
						tooltip:getText('menu.browseProjects.tt'),
						itemId:'browseProjects',
						iconCls: 'browseProjects',
						style: {
							margin:'0 0px 0 0px'
						},
						handler:function(){
							window.location.href="browseProjects.action";
						}
					};
					items.push(btnBrowseProjectsCfg);
					items.push(spacerElement);
				}

				if (com.trackplus.TrackplusConfig.user.itemNavigator) {
				    items.push(me.createBtnFindItemsCfg());
				    items.push(spacerElement);
				}

				if (com.trackplus.TrackplusConfig.user.reports) {
					var btnReportsCfg = {
						xtype: 'button',
						text: getText('menu.reports'),
						overflowText: getText('menu.reports'),
						tooltip: getText('menu.reports.tt'),
						itemId: 'reports',
						iconCls: 'reports',
						handler: function () {
							window.location.href = 'reportConfig.action';
						}
					};
					items.push(btnReportsCfg);
					items.push(spacerElement);
				}

				if(com.trackplus.TrackplusConfig.user.administration) {
					items.push(me.createAdministrationCfg());
					items.push({
						xtype: 'tbspacer',
						width: 25
					});
				}else {
					items.push("");
				}
				items.push(me.createNewItemMenuButton());

				if (com.trackplus.TrackplusConfig.toolbarPlugins !== undefined && com.trackplus.TrackplusConfig.toolbarPlugins.length > 0) {
					items.push({
							xtype: 'tbspacer',
							width: 25}
					);
					items.push(spacerElement);
					for (var i = 0; i < com.trackplus.TrackplusConfig.toolbarPlugins.length; i++) {
						items.push(me.createToolbarPlugin(com.trackplus.TrackplusConfig.toolbarPlugins[i]));
						items.push(spacerElement);
					}
				}
			}//externalAction
		}//user



		return Ext.create('Ext.toolbar.Toolbar', {
			layout: {
				overflowHandler: 'Menu'
			},
			cls: 'navigation transparentToolbar',
			enableOverflow: true,
			border:false,
			margin:'0 0 0 0',
			style: {
				padding:'0'
			},
			defaults: {
				iconAlign: 'top',
				scale: 'large'
			},
			flex:1,
			items:items
		});
	},

	createToolbarPlugin:function(tplugin){
		return {
			text: tplugin.name,
			overflowText:tplugin.name,
			tooltip:tplugin.description,
			itemId:tplugin.id,
			iconCls: tplugin.cls,
			//iconAlign: 'top',
			//scale: 'large',
			handler:function(){
				if(tplugin.fullScreen===true){
					window.location.href=tplugin.url;
				}else{
					window.location.href='customFrame.action?pluginID='+tplugin.id+'&url='+tplugin.url+'&title='+tplugin.name;
				}
			}
		};
	},

	createBtnFindItemsCfg:function(){
		var me=this;
		var btnFindItemsCfg={
			xtype:'splitbutton',
			text: getText('menu.findItems'),
			overflowText:getText('menu.findItems'),
			tooltip:getText('menu.findItems.tt'),
			//enableToggle:true,
			//toggleGroup:'bigToolbar',
			itemId:'itemNavigator',
			iconCls: 'query',
			handler:function(){
				window.location.href="itemNavigator.action";
			}
		};
		var menu=[];
		menu.push(me.createMyFiltersMenu());
		menu.push(me.createLastExecutedFiltersMenu());
		menu.push('-');
		menu.push({
			text: getText('menu.findItems.newInstantFilter'),
			tooltip:getText('menu.findItems.newInstantFilter.tt'),
			handler:function() {
				 var windowParameters = {
			            	callerScope:this,
			            	windowTitle:getText("menu.findItems.instantFilter"),
			            	loadUrlParams: {instant:true/*, add:true*/},
			            	submitUrlParams: {instant:true/*, add:true*/, ajax: true},
			            	entityContext: {
			                	operation: "instant",
			                	issueFilter: true
			                	//the operation is add
			                    //add: true,
			                   // isLeaf: true,
			            	}
			            };
			    		var windowConfig = Ext.create("com.trackplus.admin.customize.filter.FilterEdit", windowParameters);
			    		windowConfig.showWindowByConfig(this);



				/*var scope=Ext.create("com.trackplus.admin.customize.filter.FilterConfig",{
						rootID:"issueFilter"
					});
				scope.instant = true;
				com.trackplus.admin.CategoryConfig.showInstant(scope,'filterConfig!edit.action');*/
			}
		});
		if (com.trackplus.TrackplusConfig.user.manageFilters) {
			menu.push({
				text: getText('menu.admin.custom.queryFilters'),
				tooltip:getText('menu.admin.custom.queryFilters.tt'),
				handler:function(){
					var sectionSelected='customizationSection';
					var selectedNodeID='queryFilters';
					window.location.href='admin.action?selectedNodeID='+selectedNodeID+"&sectionSelected="+sectionSelected;
				}
			});
		}
		if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS
				&& com.trackplus.TrackplusConfig.appType !== APPTYPE_DESK && com.trackplus.TrackplusConfig.user.alm===true) {
			menu.push('-');
			menu.push({
				text: getText('menu.linking'),
				tooltip:getText('menu.linking.tt'),
				iconCls: 'links16',
				handler:function(){
					window.location.href = 'linking.action';
				}
			});
		}
		btnFindItemsCfg.menu=menu;
		return btnFindItemsCfg;
	},

	createMyFiltersMenu:function(){
		var me=this;
		return {
			text: getText('menu.findItems.myFilters'),
			tooltip:getText('menu.findItems.myFilters.tt'),
			menu:{items: me.createMyFiltersChildrenMenu(com.trackplus.TrackplusConfig.myMenuFilters)}
		};
	},
	createMyFiltersChildrenMenu:function(myMenuFilters){
		var mnuMyFilters=[];
		var filter;
		if(myMenuFilters){
			for(var i=0;i<myMenuFilters.length;i++){
				filter=myMenuFilters[i];
				var item={
					text:filter.label,
                    tooltip:filter.tooltip,
                    iconCls:filter.iconCls//"treeFilter-ticon"
				};
				/*if(filter.children&&filter.children.length>0){
					var childrenMenu=this.createMyFiltersChildrenMenu(filter.children);
					item.menu={items: childrenMenu}
				}else{*/
					item.handler=this.executeMyMenuFilter;
					item.scope=filter;
				//}
				mnuMyFilters.push(item);
			}
		}
		return mnuMyFilters;
	},
	executeMyMenuFilter:function(){
		var me=this;
		var filter=this;
		com.trackplus.filter.executeFilter(me, filter.objectID, false);
		//window.location.href="itemNavigator.action?queryType=1&queryID="+filter.objectID;
	},
	createLastExecutedFiltersMenu:function(){
		var me=this;
		me.menuLastExecutedFilters=Ext.create('Ext.menu.Item', {
			text: getText('menu.findItems.lastExecutedFilters'),
			tooltip:getText('menu.findItems.lastExecutedFilters.tt'),
			iconCls:'queryHistory',
			menu:{
				items:me.createLastQueriesItems(com.trackplus.TrackplusConfig.lastExecutedFilters)
			}
		});
		return me.menuLastExecutedFilters;
	},
	createLastQueriesItems:function(lastQueries){
		var me=this;
		var menu=[];
		if(lastQueries){
			for(var i=0;i<lastQueries.length;i++){
				menu.push(me.createQueryMenu(lastQueries[i]/*.id,lastQueries[i].label*/));
			}
		}
		return menu;
	},
	createQueryMenu:function(lastQuery/*, queryContext,queryName*/){
		var me=this;
        //var queryType = lastQuery["type"];
        var queryContextID = lastQuery["queryContextID"];
        //var queryID = lastQuery["objectID"];
        var queryName =  lastQuery["label"];
        var tooltip =  lastQuery["tooltip"];
        var icon=lastQuery["icon"];
        var iconCls=lastQuery["iconCls"];
		/*var iconCls='treeFilter-ticon';
		if(queryType===2){//dashboard
			iconCls='dashboard-ticon'
		}
		if(queryType===4){//basket
			iconCls='basket-ticon';
		}
		if(queryType===5){//PROJECT_RELEASE= 5;
			var entityID=0;
			try {
				entityID=parseInt(queryID);
			}catch(e){
				entityID=0;
			}
			if(entityID<0){
				iconCls='projects-ticon';
			}else{
				iconCls='release-ticon';
			}
		}
		if(queryType===6){//SCHEDULED= 6;
			iconCls="schedule-ticon";
		}
        if (queryType===7) {
            iconCls="tqlPlusFilter-ticon";
        }*/
		return {
			text:queryName,
            tooltip: tooltip,
            icon: icon,
			iconCls:iconCls,
			handler:function(){
				me.executePreviousQuery.call(me,queryContextID);
			}
		}
	},
	executePreviousQuery:function(queryContextID){
		var me=this;
		window.location.href="itemNavigator.action?queryContextID="+queryContextID;
	},

	updateLastExecutedQueries:function(lastQueries){
		var me=this;
		if(me.menuLastExecutedFilters ) {
			me.menuLastExecutedFilters.menu=Ext.create('Ext.menu.Menu',{
				items:me.createLastQueriesItems(lastQueries)
			});
		}

	},
	createAdministrationCfg:function(){
		var btnAdminCfg={
			xtype:'splitbutton',
			text:getText('menu.admin'),
			overflowText:getText('menu.admin'),
			tooltip:getText('menu.admin.tt'),
			//enableToggle:true,
			//toggleGroup:'bigToolbar',
			itemId:'admin',
			iconCls: 'admin',
			handler:function(){
				window.location.href='admin.action';
			}
		};
		var menu=[];
		menu.push({
			text: getText('menu.admin.myProfile'),
			iconCls:'userprefs-ticon',

			menu:{
				items:[
					{
						id:"menu_myPreferenceSection_myProfile",
						text:getText('menu.admin.myProfile.myProfile'),
						tooltip:getText('menu.admin.myProfile.myProfile.tt'),
						iconCls:'userprefs-ticon',
						handler:this.adminMenuHandler
					},{
						id:'menu_myPreferenceSection_myAutomail',
						text:getText('menu.admin.custom.automail'),
						tooltip:getText('menu.admin.custom.automail.tt'),
						iconCls:'automailc-ticon',
						handler:this.adminMenuHandler
					},{
						id:'menu_myPreferenceSection_iCalendarURL',
						text:getText('menu.admin.myProfile.iCalendar'),
						tooltip:getText('menu.admin.myProfile.iCalendar.tt'),
						iconCls:'calendar-ticon',
						handler:this.adminMenuHandler
					}
				]
			}
		});
		if (com.trackplus.TrackplusConfig.user.projectAdmin ||
				com.trackplus.TrackplusConfig.user.privateWorkspace) {
			//projects section allowed project administrators and end users with private workspace
			menu.push({
				text:getText('menu.admin.project'),
				id:'menu_projectTreePanel',
				iconCls:'projects-ticon',
				handler:this.adminMenuHandler
			});
			menu.push({
				text:getText('menu.admin.projectTemplate'),
				id:'menu_projectTemplateTreePanel',
				iconCls:'projects-ticon',
				handler:this.adminMenuHandler
			});
		}
		if (com.trackplus.TrackplusConfig.user.sys) {
		var userData=[{
			id:'menu_usersSection_users',
			text:getText('menu.admin.users.users'),
			tooltip:getText('menu.admin.users.users.tt'),
			iconCls:'user-ticon',
			handler:this.adminMenuHandler
		}];
		if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
			userData.push({
				id:'menu_usersSection_clients',
				text:getText('menu.admin.users.clients'),
				tooltip:getText('menu.admin.users.clients.tt'),
				iconCls:'user-ticon',
				handler:this.adminMenuHandler
			});
		}
		userData.push({
			id:'menu_usersSection_groups',
			text:getText('menu.admin.users.groups'),
			tooltip:getText('menu.admin.users.groups.tt'),
			iconCls:'group-ticon',
			handler:this.adminMenuHandler
		});
		userData.push({
			id:'menu_usersSection_departments',
			text:getText('menu.admin.users.departments'),
			tooltip:getText('menu.admin.users.departments.tt'),
			iconCls:'department-ticon',
			handler:this.adminMenuHandler
		});
		menu.push({
			text:getText('menu.admin.user'),
			iconCls:'users-ticon',
			menu:{
				items:userData
			}
		});
		}
		var customizationData=[];
		if (com.trackplus.TrackplusConfig.user.manageFilters) {
			customizationData.push({
				id:'menu_customizationSection_queryFilters',
				text:getText('menu.admin.custom.queryFilters'),
				tooltip:getText('menu.admin.custom.queryFilters.tt'),
				iconCls:'filter-ticon',
				handler:this.adminMenuHandler
			});
		}
		if (com.trackplus.TrackplusConfig.user.reportTemplates) {
			customizationData.push({
				id:'menu_customizationSection_reportTemplates',
				text:getText('menu.admin.custom.reportTemplates'),
				tooltip:getText('menu.admin.custom.reportTemplates.tt'),
				iconCls:'report-ticon',
				handler:this.adminMenuHandler
			});
		}
		if (com.trackplus.TrackplusConfig.user.sys ||
				(com.trackplus.TrackplusConfig.user.projectAdmin &&
						com.trackplus.TrackplusConfig.user.userRoles)) {
			customizationData.push({
				id:'menu_customizationSection_roles',
				text:getText('menu.admin.custom.roles'),
				tooltip:getText('menu.admin.custom.roles.tt'),
				iconCls:'roles-ticon',
				handler:this.adminMenuHandler
			});
		}
		if (com.trackplus.TrackplusConfig.user.sys ||
				(com.trackplus.TrackplusConfig.user.projectAdmin &&
						com.trackplus.TrackplusConfig.user.userLevels)) {
			customizationData.push({
				id:'menu_customizationSection_userLevels',
				text:getText('menu.admin.custom.userLevels'),
				tooltip:getText('menu.admin.custom.userLevels.tt'),
				iconCls:'personSystemAdmin',
				handler:this.adminMenuHandler
			});
		}
		if (com.trackplus.TrackplusConfig.user.sys) {
			if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
				customizationData.push({
					id:'menu_customizationSection_accounts',
					text:getText('menu.admin.custom.account'),
					tooltip:getText('menu.admin.custom.account.tt'),
					iconCls:'account-ticon',
					handler:this.adminMenuHandler
				});
			}
			customizationData.push({
				id:'menu_customizationSection_defaultAutomail',
				text:getText('menu.admin.custom.automail'),
				tooltip:getText('menu.admin.custom.automail'),
				iconCls:'automailc-ticon',
				handler:this.adminMenuHandler
			});
			if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
				customizationData.push({
					id:'menu_customizationSection_linkTypes',
					text:getText('menu.admin.custom.linkType'),
					tooltip:getText('menu.admin.custom.linkType.tt'),
					iconCls:'links-ticon',
					handler:this.adminMenuHandler
				});
			}
		}
		if (com.trackplus.TrackplusConfig.user.sys ||
			(com.trackplus.TrackplusConfig.user.projectAdmin  &&
				com.trackplus.TrackplusConfig.user.forms)) {
			customizationData.push({
				id:'menu_customizationSection_customForms',
				text:getText('menu.admin.custom.customForms'),
				tooltip:getText('menu.admin.custom.customForms.tt'),
				iconCls:'forms-ticon',
				handler:this.adminMenuHandler
			});
		}
		if (com.trackplus.TrackplusConfig.user.sys ||
				(com.trackplus.TrackplusConfig.user.projectAdmin  &&
					com.trackplus.TrackplusConfig.user.fields)) {
			customizationData.push({
				id:'menu_customizationSection_customFields',
				text:getText('menu.admin.custom.customField'),
				tooltip:getText('menu.admin.custom.customField.tt'),
				iconCls:'fields-ticon',
				handler:this.adminMenuHandler
			});
		}
		if (com.trackplus.TrackplusConfig.user.sys ||
				(com.trackplus.TrackplusConfig.user.projectAdmin  &&
					com.trackplus.TrackplusConfig.user.lists)) {
			customizationData.push({
				id:'menu_customizationSection_pickLists',
				text:getText('menu.admin.custom.list'),
				tooltip:getText('menu.admin.custom.list.tt'),
				iconCls:'picklists-ticon',
				handler:this.adminMenuHandler
			});
		}
		if (com.trackplus.TrackplusConfig.user.sys) {
			if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS
					&& com.trackplus.TrackplusConfig.appType !== APPTYPE_DESK) {
				customizationData.push({
					id:'menu_customizationSection_objectStatus', script:true,
					text:getText('menu.admin.custom.objectStatus'),
					tooltip:getText('menu.admin.custom.objectStatus.tt'),
					iconCls:'objectStatus-ticon',
					handler:this.adminMenuHandler
				});
			}
			customizationData.push({
				id:'menu_customizationSection_projectTypes',
				text:getText('menu.admin.custom.projectType'),
				tooltip:getText('menu.admin.custom.projectType.tt'),
				iconCls:'projecttypes-ticon',
				handler:this.adminMenuHandler
			});
		}
		if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
			if (com.trackplus.TrackplusConfig.user.sys ||
				(com.trackplus.TrackplusConfig.user.projectAdmin &&
					com.trackplus.TrackplusConfig.user.workflows)) {
				customizationData.push({
					id:'menu_customizationSection_workflows',
					text:getText('menu.admin.custom.workflow'),
					tooltip:getText('menu.admin.custom.workflow.tt'),
					iconCls:'workflow-ticon',
					handler:this.adminMenuHandler
				});
			}
		}
		if (com.trackplus.TrackplusConfig.user.sys) {
			if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
				customizationData.push({
					id:'menu_customizationSection_scripts',
					text:getText('menu.admin.custom.script'),
					tooltip:getText('menu.admin.custom.script.tt'),
					iconCls:'scripts-ticon',
					handler:this.adminMenuHandler
				});
				customizationData.push({
					id:'menu_customizationSection_localization',
					text:getText('menu.admin.custom.localeEditor'),
					tooltip:getText('menu.admin.custom.localeEditor.tt'),
					iconCls:'localize-ticon',
					handler:this.adminMenuHandler
				});
			}
			if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
				customizationData.push({
					id:'menu_customizationSection_mailTemplates',
					text:getText('menu.admin.custom.mailTemplate'),
					tooltip:getText('menu.admin.custom.mailTemplate.tt'),
					iconCls:'mailTemplate-ticon',
					handler:this.adminMenuHandler
				});
			}
			customizationData.push({
				id:'menu_customizationSection_dashboardAssign',
				text:getText('menu.admin.users.cockpitDefault'),
				tooltip:getText('menu.admin.users.cockpitDefault.tt'),
				iconCls:'cockpit-ticon',
				handler:this.adminMenuHandler
			});
		}

		if (customizationData.length>0) {
		menu.push({
			text:getText('menu.admin.custom'),
			iconCls:'customize-ticon',
			menu:{
				items:customizationData
			}
		});
		}
		if (com.trackplus.TrackplusConfig.user.sys ||
			com.trackplus.TrackplusConfig.user.actions) {
		menu.push({
			text:getText('menu.admin.action'),
			iconCls:'action-ticon',
			menu:{
				items:[
					{
						id:'menu_actionSection_importTrackplus',
						text:getText('menu.admin.action.importTrackplus'),
						tooltip:getText('menu.admin.action.importTrackplus.tt'),
						iconCls:'trackPlus-ticon',
						handler:this.adminMenuHandler
					},{
						id:'menu_serverSection_broadcastEmail',
						text:getText('menu.admin.manServer.sendEmail'),
						tooltip:getText('menu.admin.manServer.sendEmail.tt'),
						iconCls:'broadcast-ticon',
						handler:this.adminMenuHandler
					}
				]
			}
		});
		}
		if (com.trackplus.TrackplusConfig.user.sysAdmin) {
		menu.push({
			text:getText('menu.admin.manServer'),
			tooltip:getText('menu.admin.manServer.tt'),
			iconCls:'server-ticon',
			menu:{
				items:[
					{
						id:'menu_serverSection_serverConfiguration',
						text:getText('menu.admin.manServer.serverConfig'),
						tooltip:getText('menu.admin.manServer.serverConfig.tt'),
						iconCls:'server-ticon',
						handler:this.adminMenuHandler
					},{
						id:'menu_serverSection_logonPageText',
						text:getText('menu.admin.manServer.logonText'),
						tooltip:getText('menu.admin.manServer.logonText.tt'),
						iconCls:'motd-ticon',
						handler:this.adminMenuHandler
					},{
						id:'menu_serverSection_serverStatus',
						text:getText('menu.admin.manServer.serverStatus'),
						tooltip:getText('menu.admin.manServer.serverStatus.tt'),
						iconCls:'serverStatus-ticon',
						handler:this.adminMenuHandler
					},{
						id:'menu_serverSection_loggingConfiguration',
						text:getText('menu.admin.manServer.loggingConfig'),
						tooltip:getText('menu.admin.manServer.loggingConfig.tt'),
						iconCls:'logging-ticon',
						handler:this.adminMenuHandler
					},{
						id:'menu_serverSection_dataBackup',
						text:getText('menu.admin.manServer.databaseBackup'),
						tooltip:getText('menu.admin.manServer.databaseBackup.tt'),
						iconCls:'backup-ticon',
						handler:this.adminMenuHandler
					},{
						id:'menu_serverSection_dataRestore',
						text:getText('menu.admin.manServer.databaseRestore'),
						tooltip:getText('menu.admin.manServer.databaseRestore.tt'),
						iconCls:'restore-ticon',
						handler:this.adminMenuHandler
					}
				]
			}
		});
		}
		btnAdminCfg.menu=menu;
		return btnAdminCfg;
	},

	adminMenuHandler:function(){
		var id=this.id;

		id=id.substring("menu_".length);
		var idx=id.indexOf("_");
		var sectionSelected=null;
		var selectedNodeID=null;
		if(idx!==-1){
			sectionSelected=id.substring(0,idx);
			selectedNodeID=id.substring(idx+1);
		}else{
			sectionSelected=id;
		}
		var dummyForm = Ext.create('Ext.form.Panel', {
			items:[],
			url:'admin.action',
			standardSubmit:true,
			method: 'POST'
		});
		dummyForm.getForm().submit({
			params:{
				sectionSelected:sectionSelected,
				selectedNodeID:selectedNodeID
			}
		});
	},

	createNewItemMenuButton:function(){
		var me=this;
		var mnuIssueType=[];
		var issueTypes=com.trackplus.TrackplusConfig.issueTypes;
		var issueType;
		for(var i=0;i<issueTypes.length;i++){
			issueType=issueTypes[i];
			mnuIssueType.push({
				text:issueType.label,
				icon:'optionIconStream.action?fieldID=-2&optionID='+issueType.id,
				handler:me.createNewIssue,
				scope:{
					id:issueType.id,
					borderLayoutController:me.borderLayoutController
				}
			});
		}
		return {
			xtype:'splitbutton',
			text: getText('menu.newItem'),
			overflowText:getText('menu.newItem'),
			tooltip:getText('menu.newItem.tt'),
			iconCls: 'addItem',
			handler:me.createNewIssue,
			scope:{
				id:null,
				borderLayoutController:me.borderLayoutController
			},
			menu: mnuIssueType
		};
	},
	createNewIssue:function(button){
		var me=this;
		var issueTypeID=me.id;
		me.borderLayoutController.createNewIssue.call(me.borderLayoutController,issueTypeID,null,null,button);
	},

	showHelp:function(){
		this.borderLayoutController.showHelp.call(this.borderLayoutController);
	},

	createUserNameToolbar:function(){
		var me=this;
		var userName=com.trackplus.TrackplusConfig.userName;
		var logOff=getText('menu.logoff');
		var help=getText('menu.help');
		//var warningIcon=new Ext.Toolbar.TextItem({text: '',ctCls :'warningIcon'});
		var items=[];
		items.push('->');
		if(userName&&userName!==''){
			items.push('<a class="loginName" href="admin.action?sectionSelected=myPreferenceSection&selectedNodeID=myProfile">'+userName+'</a>');
			items.push({xtype: 'tbspacer', width: 5});
			items.push('-');
		}
		items.push({
			xtype:'button',text:help,
			handler:function(){
				me.showHelp();
			}
		});
		if(userName&&userName!==''){
			items.push('-');
			items.push({
				xtype:'button',text:logOff,
				handler:function(){
					window.location.href="logoff.action?logOff=true";
				}
			});
		}else{
			if(me.anonymous===true) {
				items.push('-');
				items.push({
					xtype:'button',text:getText('common.btn.login'),
					handler:function(){
						window.location.href="anonymousToLogon.action?forwardURL="+window.location.href;
					}
				});
			}
		}
		return Ext.create('Ext.toolbar.Toolbar',{
			cls: 'metaNavi transparentToolbar',
			border:false,
			items:items
		});
	},
	menuAppClickHandler:function(){
		var me=this;
		if(CWHF.isNull(me.appPopupMenu)){
			me.appPopupMenu = Ext.create('Ext.menu.Menu',{
				floating:true,
				items:me.getAppPopupMenuItems.call(me),
				closeAction:'destroy'
			});
		}
		me.appPopupMenu.showBy(me.btnMenuApp);
	},
	openCustomApp:function(){
		var m=this;
		var url;
		if(m.useHeader===true){
			url='externalAction.action?moduleID='+ m.id;
		}else{
			url=m.url;
		}
		var target=null;
		target= m.target;
		if(target==null) {
			window.location.href = url;
		}else {
			var win = window.open(url, target);
			win.focus();
		}
	},
	getAppPopupMenuItems:function(){
		var me=this;
		var items=new Array();
		var modules=com.trackplus.TrackplusConfig.usedModulesJSON;

		var m;
		for(var i=0;i<modules.length;i++){
			m=modules[i];
			items.push({
				text:m.name,
				iconCls:m.iconCls,
				//icon:'optionIconStream.action?fieldID=-2&optionID='+issueType.id,
				handler:me.openCustomApp,
				scope:m
			});
		}
		return items;
	},
	createChildren:function(){
		var me=this;
		var items=[];

		var loggedInPersonUserLevel = com.trackplus.TrackplusConfig.loggedInPersonUserLevel;
		if(CWHF.isNull(loggedInPersonUserLevel)) {
			loggedInPersonUserLevel = 1;
		}
		var clientUserLevelID = com.trackplus.TrackplusConfig.clientUserLevelID;
		if(CWHF.isNull(clientUserLevelID)) {
			clientUserLevelID = 0;
		}
		if(com.trackplus.TrackplusConfig.userName){
			me.btnMenuApp=Ext.create('Ext.button.Button',{
				iconCls: 'menuApp24',
				scale:'medium',
				handler:me.menuAppClickHandler,
				scope:me,
				tooltip:getText('menu.modulSelector.tt'),
				disabled: loggedInPersonUserLevel === clientUserLevelID
			});
			items.push(
				Ext.create('Ext.toolbar.Toolbar',{
					cls: 'menuAppWrapper',
					items:[me.btnMenuApp]
				})
			);
		}

		me.toolbar=me.createToolbar();
		items.push(me.toolbar);


		var estPartItems=[];
		estPartItems.push(me.createUserNameToolbar());
		var userName=com.trackplus.TrackplusConfig.userName;
		var bottomItems=new Array();
		/*bottomItems.push({
			xtype: 'component',
			flex:1
		});*/
		if(userName&&userName!==''){
			var iniData=me.borderLayoutController.initData;
			var value=null;
			if(iniData){
				value=iniData.search;
			}
			bottomItems.push(
				Ext.create('com.trackplus.SearchField',{
					cls: 'searchfield',
					paramName: 'q',
					emptyText:getText('common.btn.search'),
					value:value,
					width:213,
					//flex:1,
					externalAction:me.externalAction
				})
			);
		}
		bottomItems.push({
			xtype: 'component',
			cls:'logoHeader'
		});
		estPartItems.push({
			xtype: 'container',
			border: false,
			cls: 'searchBar',
			layout: {
				type: 'hbox',
				padding:'0',
				pack:'end',
				align:'middle'
			},
			style: {
				border:'none'
			},
			items:bottomItems
		});
		var estPartCfg={
			xtype: 'container',
			margins: '0 0 0 0',
			border: false,
			layout:{
				type:'vbox',
				align:'stretch'
			},
			//layout:'anchor',
			//defaults:{
			//	anchor:'100%'
			//},
			items:estPartItems
		};
		items.push(estPartCfg);
		return items;
	},
	logoClickHandler:function(){
		//window.location.href="masterHome.action";
	}
});

Ext.define('com.trackplus.layout.FooterView',{
	extend:'Ext.toolbar.Toolbar',
	id:'statusBarMaster',
	cls:'x-panel-header-default',
	height:29,
	initComponent: function(){
		var me=this;
		me.items=me.createChildren();
		me.callParent();
	},
	createChildren:function(){
		var me=this;
		var items=[];
		items.push({xtype: 'tbfill'});
		items.push('<div class="footerWarning">'+com.trackplus.TrackplusConfig.statusUpdateVersion+'</div>');
		items.push({xtype: 'tbfill'});
		items.push('<div class="footerWarning">'+com.trackplus.TrackplusConfig.statusLicense+'</div>');
		items.push({xtype: 'tbfill'});
		items.push(com.trackplus.TrackplusConfig.statusVersion);
		return items;
	}
});


Ext.define('com.trackplus.layout.BorderLayoutView',{
	extend:'Ext.container.Viewport',
	config:{
		borderLayoutController:null,
		useToolbar:false,
		toolbarCls:null,
		externalAction:false
	},
	layout: 'border',
	toolbar:null,
	panelCenter:null,
	header:null,
	footer:null,
	initComponent: function(){
		var me=this;
		me.items=me.createChildren();
		me.callParent();
		me.on('render', function (comp) {
			comp.getEl().on('mousedown', me.onPageMouseDown, me);
			if(!com.trackplus.TrackplusConfig.isDebugEnabled) {
				comp.getEl().on('contextmenu', Ext.emptyFn, null, {preventDefault: true});
			}
			var shortcuts=com.trackplus.TrackplusConfig.shortcutsJSON;
			if(shortcuts!==undefined&&shortcuts.length>0) {
				comp.getEl().on('keydown', me.keyDownHandler, me);
			}
		});
	},

	onPageMouseDown: function(event) {
		borderLayout.onPageMouseDown(event);
	},

	showShortcutsHelp:function(){
		var me=this;
		var shortcuts=com.trackplus.TrackplusConfig.shortcutsJSON;
		if(shortcuts&&shortcuts.length>0) {
			var html='<table><tr><td width="200"></td><td width="100%"></td></tr>';
			for(var i=0;i<shortcuts.length;i++) {
				html=html+'<tr>'
				var s = shortcuts[i];
				var h = "";
				if (s.ctrl === true) {
					h = getText('common.shortcut.ctrl');
				}
				if (s.alt === true) {
					if (h !== '') {
						h =h +'+';
					}
					h = h+getText('common.shortcut.alt');
				}
				if (s.shift === true) {
					if (h !== '') {
						h =h+ '+';
					}
					h =h+ getText('common.shortcut.shift');
				}
				h=h+"&nbsp;<B>"+ s.keyName+"</B>";
				html=html+'<td class="shortuctCell">'+h+'</td>';
				html=html+'<td class="shortuctCell">'+me.getLocalizedText(s.menuItemID)+'</td>';
				html=html+'</tr>'
			}
			var w=300;
			var h=200;
			var win = Ext.create('Ext.window.Window',{
				layout	  : 'fit',
				width	   : w,
				height	  : h,
				//iconCls:'btnConfig16',
				cls:'bottomButtonsDialog',
				border:false,
				bodyBorder:true,
				margin:'0 0 0 0',
				bodyCls: 'shortcutBody',
				style:{
					padding:'5px 0px 0px 0px'
				},
				closeAction :'destroy',
				plain	   : true,
				title		 :getText('menu.help'),
				modal	   :true,
				items	   :[
					Ext.create('Ext.Component',{
						html:html,
						autoScroll:true
					})
				],
				autoScroll  :false,
				buttons: [
					{
						text :getText('common.btn.close'),
						handler  : function(){
							win.hide();
						}
					}
				]
			});
			win.show();
		}
	},
	getLocalizedText:function(menuItem){
		var me=this;
		var key="";
		switch (menuItem){
			case me.MENU_ITEM.DASHBOARD:{
				key= "menu.cockpit";
				break;
			}
			case me.MENU_ITEM.BROWSE_PROJECT:{
				key="menu.browseProjects";
				break;
			}
			case me.MENU_ITEM.ITEM_NAVIGATOR:{
				key="menu.findItems";
				break;
			}
			case me.MENU_ITEM.REPORTS:{
				key="menu.reports";
				break;
			}
			case me.MENU_ITEM.ADMIN:{
				key="menu.admin";
				break;
			}
			case me.MENU_ITEM.NEW_ITEM:{
				key='menu.newItem';
				break;
			}
		}
		return getText(key);
	},
	keyDownHandler:function(e, t, eOpts){
		var me=this;
		var key= e.getKey();
		var alt=e.altKey;
		var ctrl=e.ctrlKey;
		var shift=e.shiftKey;
		if(key===e.F1){
			e.stopEvent();
			if (ctrl === true) {
				me.showShortcutsHelp();
			} else {
				borderLayout.showHelp();
			}
		}
		var shortcuts=com.trackplus.TrackplusConfig.shortcutsJSON;
		for(var i=0;i<shortcuts.length;i++){
			var s=shortcuts[i];
			if(s.ctrl===ctrl&&s.alt===alt&& s.shift===shift&& s.key===key){
				var menuItemID=s.menuItemID;
				e.stopEvent();
				me.executeShortcut(menuItemID);
				return false;
			}
		}
	},
	MENU_ITEM:{
		DASHBOARD:0,
		BROWSE_PROJECT:1,
		ITEM_NAVIGATOR:3,
		REPORTS:4,
		ADMIN:5,
		ADMIN_MY_SETTINGS:5001,
		ADMIN_AUTOMAIL:5002,
		ADMIN_ICALENDAR:5003,
		ADMIN_PROJECTS:5004,
		ADMIN_MANAGE_FILTERS:5005,
		ADMIN_REPORT_TEMPLATES:5006,
		NEW_ITEM:6
	},
	executeShortcut:function(menuItem){
		var me=this;
		switch (menuItem){
			case me.MENU_ITEM.DASHBOARD:{
				window.location.href="cockpit.action";
				break;
			}
			case me.MENU_ITEM.BROWSE_PROJECT:{
				window.location.href="browseProjects.action";
				break;
			}
			case me.MENU_ITEM.ITEM_NAVIGATOR:{
				window.location.href="itemNavigator.action";
				break;
			}
			case me.MENU_ITEM.REPORTS:{
				window.location.href="reportConfig.action";
				break;
			}
			case me.MENU_ITEM.ADMIN:{
				window.location.href="admin.action";
				break;
			}
			case me.MENU_ITEM.NEW_ITEM:{
				me.borderLayoutController.createNewIssue();
				break;
			}
		}
	},
	createChildren:function(){
		var me=this;
		var items=[];
		me.header=Ext.create('com.trackplus.layout.HeaderView',{
			borderLayoutController:me.borderLayoutController,
			region:'north',
			externalAction:me.externalAction,
			anonymous:me.anonymous
		});
		items.push(me.header);
		me.footer=Ext.create('com.trackplus.layout.FooterView',{
			region:'south'
		});
		me.panelCenter=me.borderLayoutController.createCenterPanel.call(me.borderLayoutController);
		me.panelCenter.region='center';
		if(me.useToolbar){
			var cls='toolbarActions';
			if(me.toolbarCls){
				cls+=' '+me.toolbarCls;
			}
			me.toolbar= Ext.create('Ext.toolbar.Toolbar', {
				layout: {
					overflowHandler: 'Menu'
				},
				enableOverflow: true,
				region:'north',
				cls:cls,
				defaults: {
					cls:'toolbarItemAction',
					//overCls:'toolbarItemAction-over',
					scale:'small',
					iconAlign: 'left',
					enableToggle:false
				}
			});
			var panelCenterWrapper=Ext.create('Ext.panel.Panel',{
				layout:'border',
				region:'center',
				border:false,
				bodyBorder:false,
				cls: 'panelCenterWrapper',
				items:[me.toolbar,me.panelCenter]
			});
			items.push(panelCenterWrapper);
		}else{
			items.push(me.panelCenter);
		}

		var westPanel=me.borderLayoutController.createWestPanel.call(me.borderLayoutController);
		if(westPanel){
			westPanel.setRegion('west');
			westPanel.setMargin(Ext.isIE?'0 -4 0 0':'0 -5 0 0');
			westPanel.addCls('borderLayout-westPanel');
			items.push(westPanel);
		}

		items.push(me.footer);
		return items;
	},
	updateLastExecutedQueries:function(lastQueries){
		var me=this;
		me.header.updateLastExecutedQueries.call(me.header,lastQueries)
	}
});

Ext.define('com.trackplus.layout.BorderLayoutController',{
	extend:'Ext.Base',
	config: {
		layoutCls:null,
		initData:{}
	},
	view:null,
	baseLayout:null,
	readyListeners:new Array(0),
	helpContext:null,
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		this.initConfig(config);
		me.baseLayout=Ext.create(me.layoutCls,{
			initData:me.initData,
			borderLayoutController:me
		});
	},
	initAjaxRequest:function(){
		var me = this;
		Ext.Ajax.on('beforerequest',me.onBeforeRequest,me);
	},
	onBeforeRequest:function(conn, options, eOpts ){
		var me = this;
	},
	historyChange:function(token){
		var me=this;
		me.baseLayout.historyChange(token);
	},
	setHelpContext:function(helpContext){
		this.helpContext=helpContext;
	},
	showHelp:function(){
		var me=this;
		var ctx=me.helpContext;
		if(CWHF.isNull(ctx)){
			ctx='general';
		}
		var path=com.trackplus.TrackplusConfig.helpPaths[ctx];
		if(CWHF.isNull(path)||path===""){
			alert("No help config for context:"+ctx);
			return false;
		}
		var url=com.trackplus.TrackplusConfig.helpPagePrefixURL+path;
		var strHelpOptions = "location=no,toolbar=no,menubar=yes,status=yes,scrollbars=yes";
		strHelpOptions += ",resizable=yes,top=0,left=0,width=900,height=700";
		window.open(url, "Help", strHelpOptions);
	},
	createView:function(){
		var me=this;
		var externalAction=false;
		var anonymous=false;
		if(me.initData.externalAction===true){
			externalAction=true;
		}
		if(me.initData.anonymous===true){
			anonymous=true;
		}
		me.view = Ext.create('com.trackplus.layout.BorderLayoutView', {
			borderLayoutController: this,
			renderTo: Ext.getBody(),
			externalAction:externalAction,
			anonymous:anonymous,
			useToolbar: me.baseLayout.getUseToolbar(),
			toolbarCls: me.baseLayout.getToolbarCls()
		});

		return me.view;
	},
	updateLastExecutedQueries:function(lastQueries){
		var me=this;
		me.view.updateLastExecutedQueries.call(me.view,lastQueries)
	},
	createWestPanel:function(){
		return this.baseLayout.createWestPanel.call(this.baseLayout);
	},
	createCenterPanel:function(){
		return this.baseLayout.createCenterPanel.call(this.baseLayout);
	},
	notifyReady:function() {
		var readyListeners = this.readyListeners;
		var listener;
		for(var i=0;i<readyListeners.length;i++) {
			listener = readyListeners[i];
			listener.fn.call(listener.scope);
		}
	},
	onReady:function(fn, scope) {
		this.readyListeners.push({fn: fn,scope: scope});
	},
	clearItemToolbar:function(){
		var me=this;
		var toolbar=me.view.toolbar;
		if(toolbar){
			toolbar.removeAll.call(toolbar,true);
			toolbar.updateLayout();
		}
	},
	createItemToolbarSeparator:function(){
		return '-';
	},
	setActiveToolbarList:function(toolbarList){
		var me=this;
		me.clearItemToolbar();
		var toolbar=me.view.toolbar;
		var toolbarItems=[];
		if(toolbarList){
			for(var i=0;i<toolbarList.length;i++){
				toolbarItems.push(toolbarList[i]);
				if(me.baseLayout.useSelfToolbarSeparators===false&&i<toolbarList.length-1){
					// toolbarItems.push(me.createItemToolbarSeparator());
				}
			}
		}
		if(toolbar){
			toolbar.add(toolbarItems);
			toolbar.updateLayout ();
		}
	},
	setVisibleToolbar:function(visible){
		var me=this;
		var toolbar=me.view.toolbar;
		toolbar.setVisible(visible);
	},
	getActiveToolbarList:function(toolbarList){
		var me=this;
		return me.view.toolbar;
	},
	setActiveToolbarActionList:function(actionList){
		var me=this;
		var toolbarList=[];
		if(actionList){
			for(var i=0;i<actionList.length;i++){
				toolbarList.push(me.createActionToolbarItem(actionList[i]));
			}
		}
		me.setActiveToolbarList(toolbarList);
	},
	createActionToolbarItem:function(action){
		return new Ext.button.Button(action);
	},

	abortOldRequests:function(){
		var me = this;
		//abort ajax request from old center content
		var activeRequests = Ext.Ajax.requests;
//		alert('center content'+activeRequests);
		var s='';
		var dif = '';
//		Ext.Ajax.abortAll();
		for(var x in activeRequests){
			var request = activeRequests[x];
			var options = request.options;
			var isFromCenter = options['fromCenterPanel'];
			//alert('isFromCenter = '+isFromCenter);
			if(isFromCenter===true){
				//alert(options.url);
				s+=options.url + ' ;';
//				Ext.Ajax.abort(request);
			}else{
				dif+=options.url;
			}
		}
		if(s!==''){
//			alert('urlToRemove = '+s);
		}
//		alert('dif = '+dif);
	},

	replaceCenterContent:function(jsonData){
		var me=this;
		var url=jsonData.url;
		if(url&&url!==''){
			var useAJAX=jsonData.useAJAX;
			var script=jsonData.script;
			if(script){
				eval(url);
				//this.replaceMenuPath(jsonData.menuPath);
				return true;
			}

			if(useAJAX){
				//this.replaceMenuPath(jsonData.menuPath);
			}
			me.loadUrl(url, useAJAX);
		}
	},
	loadUrl:function(url,useAJAX){
		var me=this;
		if(url&&url!==''){
			if(useAJAX&&useAJAX===true){
				me.clearItemToolbar();
				me.view.panelCenter.removeAll();
				me.view.panelCenter.getLoader().load({
					url: url,
					scripts:true/*,
					 success :callbackAJAX*/
				});
			}else{
				window.location.href=url;
			}
		}
	},
	setCenterContent:function(component){
		var me=this;
		me.view.panelCenter.removeAll(true);
		me.view.panelCenter.add(component);
		me.view.panelCenter.updateLayout ();
	},
	createNewIssue:function(issueTypeID,projectID,releaseID,button,parentID,handler,scope,synopsis,description){
		var me=this;
		var actionID=1;//new
		var workItemID=null;

		var successHandler;
		var myScope=me;
		if(handler){
			successHandler=handler;
			if(scope){
				myScope=scope;
			}
		}else {
			successHandler = function (data) {
				//window.location.href="printItem.action?workItemID="+data.workItemID;
				CWHF.showMsgInfo(getText('item.msg.newItemCreated', data.workItemIDDisplay, data.title));
				if (me.layoutCls === 'com.trackplus.layout.ItemNavigatorLayout' || me.layoutCls === 'com.trackplus.layout.WikiLayout') {
					me.baseLayout.refresh.call(me.baseLayout);
				} else {
					//window.location.href="itemNavigator.action?queryType=3&queryID=101";//MyItems
				}
			};
		}
		var itemAction=Ext.create('com.trackplus.item.ItemActionDialog',{
			workItemID:workItemID,
			actionID:actionID,
			successHandler:successHandler,
			scope:myScope,
			issueTypeID:issueTypeID,
			projectID:projectID,
			releaseID:releaseID,
			parentID:parentID,
			synopsis:synopsis,
			description:description,
			createDialogBeforeLoaded:false,
			animateTarget:button,
			modal:true
		});
		itemAction.execute.call(itemAction);
	}
});


com.trackplus.SearchField=Ext.define('com.trackplus.SearchField', {
	extend: 'Ext.form.field.Trigger',

	alias: 'widget.searchfield',

	trigger1Cls: Ext.baseCSSPrefix + 'form-clear-trigger',

	trigger2Cls: Ext.baseCSSPrefix + 'form-search-trigger',

	hasSearch : false,
	paramName : 'query',
	externalAction: null,
	initComponent: function() {
		this.callParent(arguments);
		this.on('specialkey', function(f, e){
			if(e.getKey() === e.ENTER){
				this.onTrigger2Click();
			}
		}, this);
	},

	afterRender: function(){
		this.callParent();
		this.triggerCell.item(0).setDisplayed(false); //close icon
		this.triggerCell.item(1).setDisplayed(false);

	},

	onTrigger1Click : function(){
		var me = this;
		if (me.hasSearch) {
			me.setValue('');
			me.hasSearch = false;
			me.triggerEl.item(0).setDisplayed('none');
			me.doComponentLayout();
		}
	},

	onTrigger2Click : function(){
		var me=this;
		var v = me.getRawValue();
		if (v.length < 1) {
			me.onTrigger1Click();
			return;
		}
		com.trackplus.search(v, me.externalAction);
		me.hasSearch = true;
		me.triggerEl.item(0).setDisplayed('block');
		me.doComponentLayout();
	}
});

var borderLayout;
com.trackplus.search=function(value, externalAction){
	borderLayout.setLoading(true);
	var urlStr='searchItem.action';
	Ext.Ajax.request({
		url: urlStr,
		disableCaching:true,
		encoding: "utf-8",
		sync: true,
		success: function(result){
			var jsonData=Ext.decode(result.responseText);
			if(jsonData.success===true) {
				if(externalAction===true){
					borderLayout.setLoading(false);
					var externalLayout=borderLayout.borderLayoutController.baseLayout;
					if(Ext.isFunction(externalLayout.searchSuccessHandler)){
						externalLayout.searchSuccessHandler(jsonData,value);
						return true;
					}
				}
				if (jsonData.workItem ) {
					window.location.href = "printItem.action?key=" + jsonData.workItem;
				} else {
					window.location.href = "itemNavigator.action";
				}
			}else{
				borderLayout.setLoading(false);
				var msg="";
				for(var i=0;i<jsonData.errors.length;i++){
					msg+=jsonData.errors[i]+"</br>";
				}
				CWHF.showMsgError(msg);
			}
		},
		failure: function(type, error){
			borderLayout.setLoading(false);
			alert("failure");
		},
		params :{
			querySearchExpression:value,
			externalAction:externalAction
		},
		method:"POST"
	});
};

function getText(){
	return com.trackplus.TrackplusConfig.getText.apply(com.trackplus.TrackplusConfig, arguments);
}
function haveLocalization(){
	return com.trackplus.TrackplusConfig.haveLocalization.apply(com.trackplus.TrackplusConfig, arguments);
}

