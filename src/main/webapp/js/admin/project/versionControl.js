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


com.trackplus.vc={};
Ext.define("com.trackplus.vc.VersionControlFacade",{
	extend:'Ext.Base',
	config: {
		versionControlFacadeModel:null
	},
	view:null,
	vcController:null,
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		this.initConfig(config);
		me.vcController=Ext.create('com.trackplus.vc.VersionControlController',{
			versionControlControllerModel:me.versionControlFacadeModel
		});
	},
	getDetailPanel:function(){
		var me=this;
		return me.vcController.createView.call(me.vcController);
	},
	initActions:function(){
		this.vcController.initToolbar.call(this.vcController);
	},
	getToolbarActions:function(){
		var me=this;
		return me.vcController.getToolbar.call(me.vcController);
	}
});

Ext.define("com.trackplus.vc.VersionControlController",{
	extend:'Ext.Base',
	config: {
		versionControlControllerModel:null
	},
	vcView:null,
	lastFormData:null,
	vcPlugin:null,
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		this.initConfig(config);
	},
	createView:function(){
		var me=this;
		me.vcView=Ext.create('com.trackplus.vc.VersionControlView',{
			versionControlViewModel:me.versionControlControllerModel,
			vcController:me
		});
		me.addMyListeners();
		me.loadData.call(me);
		return me.vcView;
	},
	getVcView:function(){
		var me=this;
		if(CWHF.isNull(me.vcView)){
			me.createView();
		}
		return me.vcView;
	},
	addMyListeners:function(){
		var me=this;
		me.vcView.addListener('changeUseVC',me.changeUseVC,me);
		me.vcView.addListener('changeVC',me.changeVC,me);
	},
	getToolbar:function(){
		var me=this;
		if(CWHF.isNull(me.actionSave)){
			me.initToolbar();
		}
		return [me.actionSave];
	},
	initToolbar:function(){
		var me=this;
		me.actionSave=Ext.create('Ext.Action',{
			overflowText:getText('common.btn.save'),
			tooltip:getText('common.btn.save'),
			text: getText('common.btn.save'),
			iconCls: 'save',
			disabled:false,
			handler:function(){
				me.save.call(me);
			}
		});
	},
	loadData:function(){
		var me=this;
		var urlStr="versionControlConfig.action";
		Ext.Ajax.request({
			url: urlStr,
			params:{projectID:+me.versionControlControllerModel.projectID},
			disableCaching:true,
			scope: me,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				var data=responseJson.data;
				me.versionControlControllerModel=data;
				me.lastFormData=me.versionControlControllerModel;
				me.vcView.cmbVCPlugins.store.loadData(me.versionControlControllerModel['vcPluginList']);
				me.vcView.cmbVCPlugins.setValue(me.versionControlControllerModel['vc.versionControlType']);
				me.vcView.checkUseVC.setValue(''+me.versionControlControllerModel['vc.useVersionControl']);
			}
		});
	},
	save:function(){
		var me=this;
		var urlStr="versionControlConfig!save.action?projectID="+me.versionControlControllerModel.projectID;
		borderLayout.setLoading(true);
		me.vcView.formPanel.getForm().submit({
			url:urlStr,
			params:{
				'vc.useVersionControl':me.vcView.checkUseVC.getRawValue(),
				'vc.versionControlType':me.vcView.cmbVCPlugins.getValue()
			},
			success: function(form, action) {
				borderLayout.setLoading(false);
				var success=action.result.success;
				if(success===false){
					me.handleErrors(action.result.errors);
				}else{
					CWHF.showMsgInfo(getText('admin.project.versionControl.successSave'));
				}
			},
			failure: function(form, action) {
				borderLayout.setLoading(false);
				me.handleErrors(action.result.errors);
			}
		});
	},
	testVC:function(){
		var me=this;
		if(!me.vcView.formPanel.getForm().isValid()){
			CWHF.showMsgError(getText('admin.project.versionControl.errorSave'));
			return false;
		}
		borderLayout.setLoading(true);
		var urlStr='versionControlConfig!test.action?projectID='+me.versionControlControllerModel.projectID;
		me.vcView.formPanel.getForm().submit({
			url:urlStr,
			params:{
				'vc.useVersionControl':me.vcView.checkUseVC.getRawValue(),
				'vc.versionControlType':me.vcView.cmbVCPlugins.getValue()
			},
			success: function(form, action) {
				borderLayout.setLoading(false);
				CWHF.showMsgInfo('The settings are valid');
			},
			failure: function(form, action) {
				borderLayout.setLoading(false);
				if(action.result&&action.result.errors){
					me.handleErrors(action.result.errors);
				}else{
					CWHF.showMsgError('Invalid settings!');
				}
			}
		});
	},
	handleErrors:function(errors){
		var me=this;
		var errMsg=getText('admin.project.versionControl.errorSave');
		for(var i=0;i<errors.length;i++){
			var inputComp=me.vcPlugin.getControl.call(me.vcPlugin,errors[i].id);
			if(inputComp){
				inputComp.markInvalid(errors[i].label);
			}else{
				errMsg=errMsg+errors[i].label+' ';
			}
		}

		CWHF.showMsgError(errMsg);
	},
	changeVC:function(pluginID){
		var me=this;
		me.lastFormData=me.vcView.formPanel.getForm().getValues();
		me.doChangeVC(pluginID);
	},
	doChangeVC:function(pluginID){
		var me=this;
		me.vcView.clearFormPanel();
		me.vcPlugin=null;
		var pluginCls=null;
		var pluginDescriptor=null;
		var vcPluginList=me.versionControlControllerModel['vcPluginList'];
		for(var i=0;vcPluginList&&i<vcPluginList.length;i++){
			if(vcPluginList[i].id===pluginID){
				pluginDescriptor=vcPluginList[i];
				pluginCls=pluginDescriptor['jsConfigClass'];
				break;
			}
		}
		if(pluginCls){
			try{
				me.vcPlugin=Ext.create(pluginCls,{
					versionControlPluginModel:me.versionControlControllerModel,
					pluginDescriptor:pluginDescriptor
				});
			}catch(e){
				alert("ex:"+e);
			}
		}
		if(me.vcPlugin&&me.vcView.checkUseVC.getRawValue()){
			me.vcView.replaceFormPanel(me.vcPlugin);
			me.vcView.setLoading(true);
			var urlStr="versionControlConfig!loadVCPlugin.action";
			me.vcView.formPanel.getForm().load({
				url:urlStr,
				params:{
					projectID:me.versionControlControllerModel.projectID,
					pluginID:pluginID
				},
				success:function(form,action){
					var data=action.result.data;
					try{
						me.vcPlugin.postProcessDataLoad.call(me.vcPlugin,data);
					}catch(e){}
					me.vcView.setLoading(false);
				}
			});
		}
	},
	changeUseVC:function(useVC){
		var me=this;
		me.vcView.cmbVCPlugins.setDisabled(!useVC);
		me.vcView.testBtn.setDisabled(!useVC);
		if(CWHF.isNull(useVC&&me.vcView.cmbVCPlugins.getValue())){
			if(me.versionControlControllerModel['vcPluginList']&&me.versionControlControllerModel['vcPluginList'].length>0){
				me.vcView.cmbVCPlugins.setValue(me.versionControlControllerModel['vcPluginList'][0].id);
			}
		}
		me.doChangeVC(me.vcView.cmbVCPlugins.getValue());
	}

});

Ext.define("com.trackplus.vc.VersionControlView",{
	extend:'Ext.panel.Panel',
	config: {
		versionControlViewModel:null,
		vcController:null
	},
	region:'center',
	layout:'border',
	border:false,
	bodyBorder:false,
	initComponent: function(){
		var me=this;
		me.items=me.createChildren();
		//TODO remove comment and check the right way to call adding events
//		this.addEvents('changeVC','changeUseVC');
		me.callParent();
	},
	createChildren:function(){
		var me=this;
		var items=new Array();

		me.checkUseVC=Ext.create('Ext.form.field.Checkbox',{
			fieldLabel:getText('admin.project.versionControl.useVersionControl'),
			name      : 'vc.useVersionControl',
			labelAlign :'right',
			labelWidth:250,
			style:{marginLeft:'15px'},
			inputValue: 'true',
			listeners:{
				change: {fn:me.changeUseVC, scope:me}
			}
		});
		me.cmbVCPlugins= Ext.create('Ext.form.field.ComboBox', {
			hideLabel:true,
			style:{marginLeft:'5px'},
			displayField: 'label',
			valueField	 : 'id',
			store: new Ext.data.Store({
				data : [],
				fields	 : [{name:'id', type:'string'}, {name:'label', type:'string'}],
				autoLoad: false
			}),
			queryMode: 'local',
			typeAhead: true,
			triggerAction: 'all',
			name:"vc.versionControlType",
			listeners:{
				select: {fn:me.changeVC, scope:me}
			}
		});
		var infoBox=Ext.create('Ext.Component',{
			html: getText('admin.project.versionControl.help'),
			border:true,
			region:'north',
			cls:'infoBox_bottomBorder'
		});
		items.push(infoBox);
		me.northPanel=Ext.create('Ext.panel.Panel',{
			layout:{
				type: 'hbox',
				padding: 5
			},
			border:false,
			bodyBorder:false,
			items:[me.checkUseVC,me.cmbVCPlugins]
		});
		me.formPanel=Ext.create('Ext.form.Panel',{
			autoScroll:true,
			border:false,
			bodyBorder:false
		});

		me.testBtn=Ext.create('Ext.button.Button',{
			style:{marginTop:'10px', marginBottom: '5px', marginLeft:'275px'},
			disabled :true,
			iconCls: 'check16',
			text: getText('admin.project.versionControl.lbl.test'),
			handler:function(){
				me.vcController.testVC.call(me.vcController);
			},
			scope:me
		});
		me.centerPanel=Ext.create('Ext.panel.Panel',{
			region:'center',
			layout:'anchor',
			autoScroll:true,
			border:false,
			items:[me.northPanel,me.formPanel,me.testBtn]
		});
		items.push(me.centerPanel);
		return items;
	},
	changeUseVC:function(){
		var me=this;
		me.fireEvent.call(me,'changeUseVC',me.checkUseVC.getRawValue());
	},
	changeVC:function(){
		var me=this;
		me.fireEvent.call(me,'changeVC',me.cmbVCPlugins.getValue());
	},
	replaceFormPanel:function(pan){
		var me=this;
		me.formPanel.removeAll(true);
		me.formPanel.add(pan);
		me.formPanel.updateLayout();
	},
	clearFormPanel:function(){
		var me=this;
		me.centerPanel.remove(me.formPanel,true);
		me.formPanel=Ext.create('Ext.form.Panel',{
			autoScroll:true,
			border:false,
			bodyBorder:false
		});
		me.centerPanel.insert(1,me.formPanel);
		me.updateLayout();
		me.formPanel.removeAll(true);
		me.formPanel.updateLayout();
	}
});

Ext.define("com.trackplus.vc.VersionControlPlugin",{
	extend:'Ext.panel.Panel',
	config: {
		versionControlPluginModel:null,
		pluginDescriptor:null
	},
	layout:'anchor',
	labelWidth:250,
	textFieldWidth:250+300,
	alignR:'right',
	border:false,
	bodyBorder:false,
	bodyPadding:0,
	getControl:function(id){
		return null;
	},
	initComponent: function(){
		var me=this;
		me.items=me.createChildren();
		me.callParent();
	},
	createChildren:function(){
		var me=this;
		var items=new Array();
		items.push(me.createBrowserFieldSet());
		return items;
	},
	postProcessDataLoad:function(data){
		var me=this;
		me.cmbBrowser.store.loadData(data.browsers);
		me.browserList=data.browsers;
		var browserID=data['vc.browserID'];
		me.cmbBrowser.setValue(browserID);
		var linksDisabled=(CWHF.isNull(browserID)||browserID==="-1");
		me.setDisabledLinks(linksDisabled);
		if(browserID === 'integratedwebsvn') {
			me.setRepoBrowserTxtFieldDisabled(true);
		}
	},
	createBrowserFieldSet:function(){
		var me=this;
		var options=[];
		me.cmbBrowser=CWHF.createCombo('admin.project.lbl.vcBrowser', 'vc.browserID',{data:options,idType:'string'},
			{select: {fn: me.changeBrowser, scope:me}});
		me.cmbBrowser.setValue(me.browser);
		me.cmbBrowserWrapper=Ext.create('com.trackplus.util.HelpWrapperComponent',{
			inputComp:me.cmbBrowser,
			helpStr:getText('admin.project.lbl.vcBrowser.help')
		});

		me.txtChangedLink=CWHF.createTextField('admin.project.lbl.vc.changesetLink','vc.changesetLink',{anchor:'100%'});
		me.txtAddedLink=CWHF.createTextField('admin.project.lbl.vc.addedLink','vc.addedLink',{anchor:'100%'});
		me.txtModifiedLink=CWHF.createTextField('admin.project.lbl.vc.modifiedLink','vc.modifiedLink',{anchor:'100%'});
		me.txtReplacedLink=CWHF.createTextField('admin.project.lbl.vc.replacedLink','vc.replacedLink',{anchor:'100%'});
		me.txtDeletedLink=CWHF.createTextField('admin.project.lbl.vc.deletedLink','vc.deletedLink',{anchor:'100%'});
		var fieldSetBrowser={
			xtype: 'fieldset',
			title: getText('admin.project.lbl.vcBrowser'),
			collapsible: false,
			defaultType: 'textfield',
			defaults: {anchor: '100%'},
			layout: 'anchor',
			items:[me.cmbBrowserWrapper,me.txtChangedLink,me.txtAddedLink,me.txtModifiedLink,
			me.txtReplacedLink,me.txtDeletedLink]
		};
		return fieldSetBrowser;
	},
	createRadioGroup:function(fieldLabelKey,name,value,options,handler, handlerScope,disabled){
		var me=this;
		var width=me.textFieldWidth;
		var columns=1;
		var items=new Array();
		for(var i=0;i<options.length;i++){
			items.push({
				name:name,
				inputValue: options[i].id,
				//id:name+"_"+options[i].id,
				boxLabel:options[i].label,
				checked:options[i].id===value
			});
		}
		var inputComp=Ext.create('Ext.form.RadioGroup',{
			fieldLabel:CWHF.isNull(fieldLabelKey)?"":getText(fieldLabelKey),
			hideLabel:(CWHF.isNull(fieldLabelKey)),
			labelStyle:{overflow:'hidden'},
			labelWidth:me.labelWidth,
			labelAlign:me.alignR,
			width:width,
			anchor:'100%',
			layout: 'hbox',
			columns: columns,
			vertical: true,
			defaults:{margin:'0 5 0 0'},
			listeners: {
				change: function(radioGroup, newValue, oldValue, options) {
					if(handler){
						handler.call(handlerScope,radioGroup, newValue, oldValue, options);
					}
				}
			},
			items:items
		});
		return inputComp;
	},
	setDisabledLinks:function(linksDisabled){
		var me=this;
		me.txtChangedLink.setDisabled(linksDisabled);
		me.txtAddedLink.setDisabled(linksDisabled);
		me.txtModifiedLink.setDisabled(linksDisabled);
		me.txtReplacedLink.setDisabled(linksDisabled);
		me.txtDeletedLink.setDisabled(linksDisabled);
	},

	setRepoBrowserTxtFieldDisabled: function(isDisabled) {
		var me = this;
		me.txtChangedLink.setReadOnly(isDisabled);
		me.txtAddedLink.setReadOnly(isDisabled);
		me.txtModifiedLink.setReadOnly(isDisabled);
		me.txtReplacedLink.setReadOnly(isDisabled);
		me.txtDeletedLink.setReadOnly(isDisabled);
	},

	changeBrowser:function(){
		var me=this;
		var browserValue=me.cmbBrowser.getValue();
		var linksDisabled=(CWHF.isNull(browserValue)||browserValue==="-1");
		me.setDisabledLinks(linksDisabled);
		var changesetLink="";
		var addedLink="";
		var modifiedLink="";
		var replacedLink="";
		var deletedLink="";
		if(me.browserList&&browserValue&&browserValue!=="-1"){
			for(var i=0;i<me.browserList.length;i++){
				var selected=false;
				if (me.browserList[i].id===browserValue) {
					changesetLink=me.browserList[i].baseURL+me.browserList[i].changesetLink;
					addedLink=me.browserList[i].baseURL+me.browserList[i].addedLink;
					modifiedLink=me.browserList[i].baseURL+me.browserList[i].modifiedLink;
					replacedLink=me.browserList[i].baseURL+me.browserList[i].replacedLink;
					deletedLink=me.browserList[i].baseURL+me.browserList[i].deletedLink;
					break;
				}
			}
		}
		me.txtChangedLink.setValue(changesetLink);
		me.txtAddedLink.setValue(addedLink);
		me.txtModifiedLink.setValue(modifiedLink);
		me.txtReplacedLink.setValue(replacedLink);
		me.txtDeletedLink.setValue(deletedLink);
		if(browserValue === 'integratedwebsvn') {
			me.setRepoBrowserTxtFieldDisabled(true);
		} else {
			me.setRepoBrowserTxtFieldDisabled(false);
		}
	}
});
