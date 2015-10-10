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



Ext.define('com.trackplus.layout.HomeLayout',{
	extend:'com.trackplus.layout.BaseLayout',
	useToolbar:true,
	screenModel:null,
	screenFacade:null,
	selectedGroup:'cockpit',
	constructor : function(config) {
		var me = this;
		me.callParent(arguments);
		me.screenModel=com.trackplus.screen.createScreenModel(me.initData.screen);
		me.screenFacade= Ext.create('com.trackplus.screen.BaseScreenFacade',{
			screenModel:me.screenModel,
			showOneTab:true,
			lastSelectedTab:me.initData.lastSelectedTab,
			refreshTabUrl:'dashboardTabRuntime!execute.action',
			storeTabUrl:'dashboard!storeLastSelectedTab.action',
            tabViewCls:'com.trackplus.screen.DashboardTabView',
			panelViewCls:'com.trackplus.screen.DashboardPanelView',
			fieldErrorCls:'com.trackplus.screen.DashboardFieldErrorView'
		});
		me.borderLayoutController.setHelpContext("dashboard");
		me.onReady(function(){
			if (com.trackplus.TrackplusConfig.user.configureCockpit) {
				var dashToolbar=[me.createEditDashButton()];
				if (com.trackplus.TrackplusConfig.appType != APPTYPE_BUGS) {
					dashToolbar.push(me.createResetDashButton());
					dashToolbar.push(me.createSaveAsTemplateDashButton());
				}
				me.borderLayoutController.setActiveToolbarList.call(me.borderLayoutController,dashToolbar);
			}
		});
	},
	createCenterPanel:function(){
		var me=this;
		return me.screenFacade.createViewComponent.call(me.screenFacade);
	},
	createEditDashButton:function(){
		var me=this;
		return Ext.create('Ext.button.Button',{
			xtype:'button',
			overflowText:getText('cockpit.config'),
			tooltip:getText('cockpit.config.tt'),
			text: getText('cockpit.config'),
			iconCls: 'cockpitEdit',
			disabled:false,
			handler:function(){
				me.editDashboard();
			}
		});
	},
	createResetDashButton:function(){
		var me=this;
		return Ext.create('Ext.button.Button',{
			xtype:'button',
			overflowText:getText('cockpit.reset'),
			tooltip:getText('cockpit.reset'),
			text: getText('cockpit.reset'),
			iconCls: 'cockpitReset',
			disabled:false,
			handler:function(){
				me.resetDashboard();
			}
		});
	},
	createSaveAsTemplateDashButton:function(){
		var me=this;
		return Ext.create('Ext.button.Button',{
			xtype:'button',
			overflowText:getText('cockpit.saveAsTemplate'),
			tooltip:getText('cockpit.saveAsTemplate.tooltip'),
			text: getText('cockpit.saveAsTemplate'),
			iconCls: 'cockpitSave',
			disabled:false,
			handler:function(){
				me.saveAsDefault();
			}
		});
	},
	editDashboard:function(){
		window.location.href='dashboardEdit.action?backAction=cockpit.action';
	},
	resetDashboard:function(){
		var me=this;
		com.trackplus.dashboard.resetDashboard('dashboardTemplate!resetDashboard.action',{}, me.resetDashboardSuccess,me);
	},
	resetDashboardSuccess:function(){
		window.location.href='cockpit.action';
	},
	saveAsDefault:function(){
		var me=this;
		com.trackplus.dashboard.saveAsDefault();
	}
});

com.trackplus.dashboard.resetWin=null;
com.trackplus.dashboard.resetDashboard=function(url,params,handlerCallback,scopeCallBack){
	var urlStr='dashboardTemplate!loadNotAssigned.action';
	borderLayout.setLoading(true);
	Ext.Ajax.request({
		url: urlStr,
		disableCaching:true,
		success: function(response){
			var jsonData=Ext.decode(response.responseText);
			var options=jsonData.data;
			borderLayout.setLoading(false);
			com.trackplus.dashboard.openResetDialog(options,url,params,handlerCallback,scopeCallBack);
		},
		failure: function(){
			borderLayout.setLoading(false);
			//alert("failure");
		}
	});
};
com.trackplus.dashboard.openResetDialog=function(options,urlStr,params,handlerCallback,scopeCallBack){
	var store = Ext.create('Ext.data.Store', {
		fields	: [{name:'id', type:'int'}, {name:'label', type:'string'},{name:'selected',type:'bool'}],
		data :options
	});
	var cmb=Ext.create('Ext.form.ComboBox', {
		store: store,
		queryMode: 'local',
		displayField: 'label',
		valueField: 'id',
		editable: false,
		name:'resetID',
		fieldLabel : getText('cockpit.reset.lbl.defaultCockpit'),
		labelStyle:{overflow:'hidden'},
		labelAlign:'right',
		labelWidth:150,
		anchor:'100%'
	});

	var btnDelete=Ext.create('Ext.button.Button',{
		text:getText('common.btn.delete'),
		disabled:true,
		handler:function(){
			borderLayout.setLoading(true);
			var id= cmb.getValue();
			var urlDeleteItems="dashboardAssign!delete.action";
			var messageConfirmDelete=getText("common.lbl.messageBox.removeSelected.confirm");
			var titleDelete=getText("common.btn.delete");
			var okLabel=getText("common.btn.ok");
			Ext.MessageBox.show({
				title:titleDelete,
				msg: messageConfirmDelete,
				//buttons:{yes : yesLabel, no : noLabel},
				buttons: Ext.MessageBox.YESNO,
				fn: function(btn){
					if(btn=="yes"){
						var deletedItems=id;
						Ext.Ajax.request({
							url: urlDeleteItems,
							disableCaching:true,
							success: function(){
								var urlStr='dashboardTemplate!loadNotAssigned.action';
								Ext.Ajax.request({
									url: urlStr,
									disableCaching:true,
									success: function(response){
										var jsonData=Ext.decode(response.responseText);
										var options=jsonData.data;
										btnDelete.setDisabled(true);
										cmb.setValue(null);
										cmb.getStore().loadData(options);
										borderLayout.setLoading(false);
									},
									failure: function(){
										borderLayout.setLoading(false);
										//alert("failure");
									}
								});
							},
							failure: function(){
								borderLayout.setLoading(false);
							},
							method:'POST',
							params:{"deletedItems":deletedItems}
						});
					}
				},
				//animEl: 'mb4',
				icon: Ext.MessageBox.QUESTION
			});


		}
	});
	cmb.addListener('select',function( combo, records, eOpts){
		if(records!=null&&records.length>0){
			var selected=records[0].data['selected'];
			var id=records[0].data['id'];
			btnDelete.setDisabled(!selected);
		}else{
			btnDelete.setDisabled(true);
		}
	});

	var formPanel=Ext.create('Ext.form.Panel',{
		layout:'anchor',
		standardSubmit:false,
		bodyBorder:false,
		border    : false,
		autoScroll:false,
		margin: '0 0 0 0',
		bodyStyle:{
			padding:'10px 10px 5px 10px'
		},
		/*style:{
		 borderBottom:'1px solid #D0D0D0'
		 },*/
		items:[cmb]
	});
	if(com.trackplus.dashboard.resetWin!=null){
		com.trackplus.dashboard.resetWin.destroy();
	}
	com.trackplus.dashboard.resetWin = Ext.create('Ext.window.Window',{
		layout      : 'fit',
		width       : 400,
		height      : 115,
		minWidth:360,
		minHeight:115,
		closeAction :'destroy',
		plain       : true,
		title		 : getText('cockpit.reset'),
		modal       : true,
		cls:'bottomButtonsDialog',
		border:false,
		bodyBorder:true,
		margin:'0 0 0 0',
		style:{
			padding:'5px 0px 0px 0px'
		},
		bodyPadding:'0px',
		autoScroll  : false,
		items       : [formPanel],
		buttons     : [
			{text:getText('common.btn.ok') ,handler  :function(){
				formPanel.getForm().submit({
					url:urlStr,
					params:params,
					success: function(form, action) {
						com.trackplus.dashboard.resetWin.destroy();
						if(handlerCallback!=null){
							handlerCallback.call(scopeCallBack==null?this:scopeCallBack);
						}
					},
					failure: function(form, action) {
						alert("failure");
					}
				});
			}},btnDelete,
			{text:getText('common.btn.cancel'),handler  :function(){com.trackplus.dashboard.resetWin.destroy();}}
		]
	});
	com.trackplus.dashboard.resetWin.show();
};

com.trackplus.dashboard.saveAsDefault=function(projectID,entityType,handlerCallback,scopeCallBack){
	var urlStr='dashboardTemplate!copyAsTemplateDashboard.action';
	if(projectID!=null){
		urlStr=urlStr+"?projectID="+projectID+"&entityType="+entityType;
	}
	borderLayout.setLoading(true);
	Ext.Ajax.request({
		url: urlStr,
		disableCaching:true,
		success: function(response){
			var jsonData=Ext.decode(response.responseText);
			borderLayout.setLoading(false);
			com.trackplus.dashboard.openSaveAsDefaultDialog(jsonData.data);
		},
		failure: function(){
			borderLayout.setLoading(false);
		}
	});
};

com.trackplus.dashboard.openSaveAsDefaultDialog=function(data,handlerCallback,scopeCallBack){
	var items=[
		CWHF.createHiddenField('dashboardID',{labelWidth:150,value:data.id}),
		CWHF.createTextField('common.lbl.name','name', {labelWidth:150,anchor:'100%', allowBlank:false,value:data.name}),
		CWHF.createTextAreaField('common.lbl.description','description', {labelWidth:150,anchor:'100%',value:data.description})
	];
	var formPanel=Ext.create('Ext.form.Panel',{
		layout:'anchor',
		standardSubmit:false,
		bodyBorder:false,
		border    : false,
		autoScroll:false,
		margin: '0 0 0 0',
		bodyStyle:{
			padding:'10px 10px 5px 10px'
		},
		defaults: {
			labelStyle:'overflow: hidden;',
			labelWidth:150,
			msgTarget:	'under',
			anchor:'100%'
		},
		/*style:{
		 borderBottom:'1px solid #D0D0D0'
		 },*/
		items:items
	});
	var urlStr="dashboardTemplate!saveAsTemplateDashboard.action";
	if(com.trackplus.dashboard.resetWin!=null){
		com.trackplus.dashboard.resetWin.destroy();
	}
	com.trackplus.dashboard.resetWin = Ext.create('Ext.window.Window',{
		layout      : 'fit',
		width       : 500,
		height      : 300,
		minWidth:400,
		minHeight:300,
		closeAction :'destroy',
		plain       : true,
		title		 : getText('cockpit.saveAsTemplate'),
		modal       : true,
		cls:'bottomButtonsDialog',
		border:false,
		bodyBorder:true,
		margin:'0 0 0 0',
		style:{
			padding:'5px 0px 0px 0px'
		},
		bodyPadding:'0px',
		autoScroll  : false,
		items       : [formPanel],
		buttons     : [
			{text:getText('common.btn.ok') ,handler  :function(){
				formPanel.getForm().submit({
					url:urlStr,
					success: function(form, action) {
						com.trackplus.dashboard.resetWin.destroy();
						if(handlerCallback!=null){
							handlerCallback.call(scopeCallBack==null?this:scopeCallBack);
						}else{
							CWHF.showMsgInfo(getText('cockpit.saveAsTemplate.successMessage'));
						}
					},
					failure: function(form, action) {
						alert("failure");
					}
				});
			}},
			{text:getText('common.btn.cancel'),handler  :function(){com.trackplus.dashboard.resetWin.destroy();}}
		]
	});
	com.trackplus.dashboard.resetWin.show();
};



