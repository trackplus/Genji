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

//Component Controller


//Component Controller
Ext.define('com.trackplus.screen.ComponentController',{
	extend:'Ext.Base',
	config:{
		screenEditController:null,
		action:'',
		updateAction:''
	},
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		//this.initConfig(config);
	},
	getUpdateLink:function(compID){
		if(CWHF.isNull(this.updateAction)){
			return null;
		}
		var screenID=this.screenEditController.screenModel.id;
		return this.updateAction+".action?componentID="+compID+"&screenID="+screenID;
	},
	getPropertyLink:function(compID){
		if(CWHF.isNull(this.updateAction)){
			return null;
		}
		return this.action+"!properties.action?componentID="+compID;
	},
	reload:function(compID,action,params, idChild){
	},
	click:function(comp){
		/*var me=this;
		me.setSelectedComponent(comp);
		var compControllerCfg=me.getComponentControllerCfg(comp);
		var componentID=comp.model.getId();
		var urlStr=compControllerCfg.getPropertyLink(componentID);
		me.refreshOutline.call(me,urlStr);*/
	}
});

//ScreenController
Ext.define('com.trackplus.screen.ScreenController',{
	extend:'com.trackplus.screen.ComponentController',
	reload:function(screenView,actionMethod,params, idChild){
		var me=this;
		if(CWHF.isNull(actionMethod)){
			actionMethod="reload";
		}
		var componentID=screenView.model.getId();
		var urlStr=me.action+"!"+actionMethod+".action?componentID="+componentID;
		if(me.projectID){
			urlStr=urlStr+"&projectID="+this.projectID+"&entityType="+this.entityType;
		}
		borderLayout.setLoading(true);
		Ext.Ajax.request({
			url: urlStr,
			params:params,
			success: function(resp){
				location.reload(true);
				/*var jsonData=Ext.decode(resp.responseText);
				var screenModel=com.trackplus.screen.createScreenModel(jsonData.data.screen);
				me.screenEditController.refreshScreenModel.call(me.screenEditController,screenView,screenModel,
					idChild?idChild:jsonData.data.selectedTab);
				screenView.setLoading(false);*/
			},
			failure:function(){
				alert("failed!");
				screenView.setLoading(false);
			}
		});
	},
	copy:function(screenID,sourceType,sourceID,succesHandler,scope){
		var me=this;
		borderLayout.setLoading(true);
		var urlStr=me.action+"!copy.action?componentID="+screenID;
		var params={
			sourceType:sourceType,
			sourceID:sourceID
		};
		Ext.Ajax.request({
			url: urlStr,
			params:params,
			success: function(resp){
				var jsonData=Ext.decode(resp.responseText);
				borderLayout.setLoading(false);
				if(succesHandler){
					succesHandler.call(scope);
				}
			},
			failure:function(){
				alert("failed!");
				borderLayout.setLoading(false);
			}
		});
	},
	paste:function(screenID,sourceType,sourceID,targetType,targetID,targetInfo,successHandler,scope){
		var me=this;
		borderLayout.setLoading(true);
		var urlStr=me.action+"!paste.action?componentID="+screenID;
		var params={
			sourceType:sourceType,
			sourceID:sourceID,
			targetType:targetType,
			targetID:targetID,
			targetInfo:targetInfo
		};
		Ext.Ajax.request({
			url: urlStr,
			params:params,
			success: function(resp){
				var jsonData=Ext.decode(resp.responseText);
				borderLayout.setLoading(false);
				if(successHandler){
					successHandler.call(CWHF.isNull(scope)?me:scope);
				}
			},
			failure:function(){
				alert("failed!");
				borderLayout.setLoading(false);
			}
		});
	}
});

//TabController
Ext.define('com.trackplus.screen.TabController',{
	extend:'com.trackplus.screen.ComponentController',

	unregister:function(tabCmp){
		tabCmp.unregister.call(tabCmp);
	},
	reload:function(tabCmp,actionMethod,params, idChild){
		var me=this;
		if(CWHF.isNull(me.action)){
			me.screenEditController.reload.call(me.screenEditController);
			return;
		}
		var compID=tabCmp.model.getId();
		tabCmp.setLoading(getText("common.lbl.loading"));

		//unregister
		me.unregister(tabCmp);

		if(CWHF.isNull(actionMethod)){
			actionMethod="reload";
		}
		var urlStr=me.action+"!"+actionMethod+".action?componentID="+compID;
		var screenID=me.screenEditController.screenModel.id;
		urlStr=urlStr+"&screenID="+screenID;
		if(me.projectID){
			urlStr=urlStr+"&projectID="+this.projectID+"&entityType="+this.entityType;
		}
		Ext.Ajax.request({
			url: urlStr,
			params:params,
			success: function(response){
				var jsonData=Ext.decode(response.responseText);
				var tabModel=com.trackplus.screen.createTabModel(jsonData.data);
				var panelToSelect=me.screenEditController.refreshTabModel(tabCmp,tabModel,idChild);
				tabCmp.setLoading(false);
				if(panelToSelect){
					me.screenEditController.setSelectedComponent(panelToSelect);
				}
			},
			failure:function(){
				alert("failed!");
				tabCmp.setLoading(false);
			}
		});
	}
});

//PanelController
Ext.define('com.trackplus.screen.PanelController',{
	extend:'com.trackplus.screen.ComponentController',

	unregister:function(panel){
		panel.unregister.call(panel);
	},
	reload:function(panel,action,params, idToSelect,handler){
		var me=this;
		me.unregister(panel);
		//set loading
		panel.setLoading(getText("common.lbl.loading"));
		if(CWHF.isNull(action)){
			action="reload";
		}
		var panelID=panel.model.getId();
		var urlStr=me.action+"!"+action+".action";
		urlStr=urlStr+"?componentID="+panelID;
		var screenID=me.screenEditController.screenModel.id;
		urlStr=urlStr+"&screenID="+screenID;
		if(me.projectID){
			urlStr=urlStr+"&projectID="+me.projectID+"&entityType="+me.entityType;
		}
		Ext.Ajax.request({
			url: urlStr,
			disableCaching:true,
			success: function(result){
				var jsonData=Ext.decode(result.responseText);
				var panelModel=com.trackplus.screen.createPanelModel(jsonData);
				me.screenEditController.refreshPanelModel(panel,panelModel,true);
				/*document.getElementById("div-p"+panelID).innerHTML=result.responseText;
				var domToSelect=null;
				if(idToSelect){
					domToSelect=document.getElementById(idToSelect);
				}
				if(domToSelect){
				ScreenEdit.selectedItem=null;
					ComponentController.instance.clickHandler(domToSelect);
			 	}*/
				if(handler){
					handler.call();
				}
				panel.setLoading(false);
			},
			failure: function(){
				alert("failure");
				panel.setLoading(false);
			},
			method:'POST',
			params:params
		});
	},
	registerDragDrop:function(panel){
		var me=this;
		var screenCtrl=me.screenEditController;
		var comp=panel;
		var dragSource = new Ext.dd.DragSource(panel.getEl(), {
			dragData:{'panel': panel},
			ddGroup:"panels"
		});
		var dropTarget = new Ext.dd.DropTarget(panel.getEl(), {
			ddGroup	: 'panels',
			notifyEnter : function(ddSource, e, data) {
				screenCtrl.setDragOver(comp);
			},
			notifyOut: function(ddSource, e, data) {
				screenCtrl.clearDragOn();
			},
			notifyDrop  : function(ddSource, e, data){
				screenCtrl.clearDragOn();
				var panelSource=data.panel;
				var panelTarget=panel;
				Ext.defer(me.onDropPanel, 100, me,[panelSource,panelTarget]);
				return true;
			}
		});
		panel.dragDropObjects.push(dragSource);
		panel.dragDropObjects.push(dropTarget);
	},
	onDropPanel:function(panelSource,panelTarget){
		var me=this;
		var sourceID=panelSource.model.getId();
		var targetID=panelTarget.model.getId();
		if(sourceID===targetID){
			return false;
		}
		var newIndex=panelTarget.myIndex;
		var action="moveChild";
		var params={
			"panelID":sourceID,
			"newIndex":newIndex
		};
		var tabCmp=panelSource.parentView;
		me.screenEditController.tabController.reload.call(me.screenEditController.tabController,tabCmp,action,params,sourceID);
		return true;
	}
});

//FieldController
Ext.define('com.trackplus.screen.FieldController',{
	extend:'com.trackplus.screen.ComponentController',
	config:{
		//screenView:null
	},
	registerDrag:function(field){
		var me=this;
		var panelCmp=field.parentView;
		var fieldID=field.model.getId();
		var dragSource = new Ext.dd.DragSource(field.getEl(), {
			dragData:{
				'panelCmp': panelCmp,
				'cell':field.model.row+";"+field.model.col,
				'fieldID':fieldID
			},
			ddGroup:"fields"
		});
		return dragSource;
	},
	registerDrop:function(field){
		var me=this;
		var comp=field;
		var screenCtrl=me.screenEditController;
		var dropTarget = new Ext.dd.DropTarget(field.getEl(), {
			ddGroup	: 'fields',
			notifyEnter : function(ddSource, e, data) {
				screenCtrl.setDragOver(comp);
			},
			notifyOut: function(ddSource, e, data) {
				screenCtrl.clearDragOn();
			},
			notifyOver : function(dd, e, data){
				if(CWHF.isNull(me.lastDropComponent)||me.lastDropComponent.model.getId()!==comp.id){
					screenCtrl.setDragOver(comp);
				}
				return this.dropAllowed;
			},
			notifyDrop  : function(ddSource, e, data){
				screenCtrl.clearDragOn();
				var fieldID=null;
				var fieldType=null;
				var sourcePanel=null;
				var sourceCell=null;
				var fieldModel=comp.model;
				var targetPanel=field.parentView;
				var targetCell=fieldModel.row+";"+fieldModel.col;
				if(data.records){
					//drop a new field
					fieldType=data.records[0].data.id;
				}else{
					fieldID=data.fieldID;
					sourcePanel=data.panelCmp;
					sourceCell=data.cell;
				}
				if(CWHF.isNull(fieldID)&&CWHF.isNull(fieldType)){
					return false;
				}
				Ext.defer(me.onDropField,100,me,[fieldType,fieldID,sourcePanel,sourceCell,targetPanel,targetCell]);
				return true;
			}
		});
		return dropTarget;
	},
	onDropField:function(fieldType,fieldID,sourcePanel,sourceCell,targetPanel,targetCell){
		var me=this;
		var sourcePanelID=null;
		if(sourcePanel){
			sourcePanelID=sourcePanel.model.getId();
		}
		var targetPanelID=targetPanel.model.getId();
		var screenCtrl=me.screenEditController;
		screenCtrl.clearSelectedComponent();
		var action;
		if(CWHF.isNull(sourceCell)){
			action="addField"
		}else{
			action="moveField";
		}
		var params={"fieldID":fieldID,
				"fieldType":fieldType,
				"source":sourceCell,
				"sourcePanelID":sourcePanelID,
				"target":targetCell,
				"targetPanelID":targetPanelID};
		var handler=null;
		if(sourcePanelID&&sourcePanelID!==targetPanelID){
			//drop in other panel
			action="moveFieldFromOther";
			handler=function(){
				screenCtrl.panelController.reload.call(screenCtrl.panelController,sourcePanel,"reload");
			}
		}

		if(sourcePanelID&&sourcePanelID===targetPanelID&&sourceCell===targetCell){
			return;
		}
		var idToSelect=targetPanelID;
		screenCtrl.panelController.reload.call(screenCtrl.panelController,targetPanel,action,params,idToSelect,handler);
		return true;
	}

});

Ext.define('com.trackplus.screen.ScreenEditController',{
	extend:'com.trackplus.screen.BaseScreenController',
	config: {
		fieldWrapperCls:null,
		screenAction:null,
		screenActionParams:null,
		screenUpdateAction:null,
		tabAction:null,
		tabUpdateAction:null,
		panelAction:null,
		panelUpdateAction:null,
		fieldAction:null,
		fieldUpdateAction:null,
		useConfig:false,
		messageDeletePanel:null,
		messageDeleteTab:null,
		messageDeleteField:null,
		messageCantDeleteScreen:null,
		messageCantDeleteLastTab:null,
		backAction:'cockpit.action'
	},
	showOneTab:false,
	selectedComponent:null,
	clipboard:null,
	lastDropComponent:null,
	clickOnTab:false,clickOnPanel:false,clickOnField:false,

	screenEditView:null,

	screenController:null,
	tabController:null,
	panelController:null,
	fieldController:null,
	actionsController:null,

	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		me.screenController=Ext.create('com.trackplus.screen.ScreenController',{
			screenEditController:me,
			action:me.screenAction,
			updateAction:me.screenUpdateAction
		});
		me.tabController=Ext.create('com.trackplus.screen.TabController',{
			screenEditController:me,
			action:me.tabAction,
			updateAction:me.tabUpdateAction
		});
		me.panelController=Ext.create('com.trackplus.screen.PanelController',{
			screenEditController:me,
			action:me.panelAction,
			updateAction:me.panelUpdateAction
		});
		me.fieldController=Ext.create('com.trackplus.screen.FieldController',{
			screenEditController:me,
			action:me.fieldAction,
			updateAction:me.fieldUpdateAction
		});
		me.actionsController=Ext.create('com.trackplus.screen.ScreenActionsController',{
			screenEditController:me,
			useConfig:me.useConfig
		});
		me.callParent();
	},
	afterScreenCreated:function(screenCmp){
		var me=this;
		screenCmp.addListener('render',function(c){
			c.getEl().on('click', function(){
				me.clickOnScreenHandler.call(me,c);
			});
			c.getEl().on('contextmenu', function(e,domEl){
				var result=me.clickOnScreenHandler.call(me,c);
				if(result===0){
					me.showPopup.call(me,e);
				}
			});
		});
	},
	showPopup:function(e,emptyFieldCmp){
		var me=this;
		me.emptyFieldCmp=emptyFieldCmp;
		var contextMenu=me.actionsController.createPopup.call(me.actionsController);
		if(contextMenu){
			contextMenu.showAt(e.getXY());
		}
		e.stopEvent();
	},
	clickOnScreenHandler:function(c){
		var me=this;
		if(me.clickOnField===true){
			me.clickOnField=false;
			return 1;
		}
		if(me.clickOnPanel===true){
			me.clickOnPanel=false;
			return 2;
		}
		if(me.clickOnTab===true){
			me.clickOnTab=false;
			return 3;
		}
		me.clickOnScreen=true;
		me.clickOnComponent.call(me,c);
		return 0;
	},
	clickOnTabHandler:function(c){
		var me=this;
		if(me.clickOnField===true){
			return 1;
		}
		if(me.clickOnPanel===true){
			return 2;
		}
		me.clickOnTab=true;
		me.clickOnComponent.call(me,c);
		return 0;
	},
	afterTabPanelCreated:function(tabPanel){
		var me=this;
		var items=tabPanel.tabBar.items;
		for(var i=0;i<items.getCount();i++){
			var item=items.getAt(i);
			item.addListener('render',function(c){
				c.getEl().addListener('click',me.clickOnTabBar,me,tabPanel);
			});
		}
		items.addListener('add',function(index,o){
			o.addListener('render',function(c){
				o.getEl().addListener('click',me.clickOnTabBar,me,tabPanel);
			});
		});
	},

	clickOnTabBar:function(e,t,tabPanel){
		var me=this;
		me.clickOnTabHandler.call(me,tabPanel.getActiveTab());
	},
	afterTabCreated:function(tabCmp){
		var me=this;
		tabCmp.addCls("designTab");
		tabCmp.myComponentCls="designTab";
		tabCmp.addListener('render',function(c){
			c.getEl().on('click', function(){
				me.clickOnTabHandler.call(me,c);
			});
			c.getEl().on('contextmenu', function(e,domEl){
				var result=me.clickOnTabHandler.call(me,c);
				if(result===0){
					me.showPopup.call(me,e);
				}
			});
		});
		tabCmp.addListener('activate',me.clickOnTabHandler,me);
	},
	clickOnPanelHandler:function(c){
		var me=this;
		if(me.clickOnField===true){
			return 1;
		}
		me.clickOnPanel=true;
		me.clickOnComponent.call(me,c,me);
		return 0;
	},
	afterPanelCreated:function(panelCmp){
		var me=this;
		panelCmp.addListener('render',function(c){
			c.getEl().on('click', function(){
				me.clickOnPanelHandler.call(me,c);
			});
			c.getEl().on('contextmenu', function(e,domEl){
				var result=me.clickOnPanelHandler.call(me,c);
				if(result===0){
					me.showPopup.call(me,e);
				}
			});
			me.panelController.registerDragDrop.call(me.panelController,c);
		});
	},

	clearDragOn:function(){
		var me=this;
		if(me.lastDropComponent){
			var clsToRemove="dragOver";
			if(me.lastDropComponent.myComponentCls){
				clsToRemove=me.lastDropComponent.myComponentCls+"-dragOver";
				me.lastDropComponent.addCls(me.lastDropComponent.myComponentCls);
			}
			me.lastDropComponent.removeCls(clsToRemove);
			me.lastDropComponent=null;
		}
	},
	setDragOver:function(comp){
		var me=this;
		if(me.lastDropComponent){
			me.clearDragOn();
		}
		var clsToAdd="dragOver";
		if(comp.myComponentCls){
			clsToAdd=comp.myComponentCls+"-dragOver";
			comp.removeCls(comp.myComponentCls);
		}
		comp.addCls(clsToAdd);
		me.lastDropComponent=comp;
	},


	clearSelectedComponent:function(){
		var me=this;
		if(me.selectedComponent&&me.selectedComponent.getEl()){
			//clear selecttion
			var clsToRemove="componentSelected";
			if(me.selectedComponent.myComponentCls){
				clsToRemove=me.selectedComponent.myComponentCls+"-selected";
				me.selectedComponent.addCls(me.selectedComponent.myComponentCls);
			}
			me.selectedComponent.removeCls(clsToRemove);
			me.selectedComponent=null;
		}
	},
	setSelectedComponent:function(comp){
		var me=this;
		if(me.selectedComponent){
			me.clearSelectedComponent();
		}
		var clsToAdd="componentSelected";
		if(comp.myComponentCls){
			clsToAdd=comp.myComponentCls+"-selected";
			comp.removeCls(comp.myComponentCls);
		}
		comp.addCls(clsToAdd);
		me.selectedComponent=comp;
		me.refreshActions();
	},
	refreshActions:function(){
		var me=this;
		var model=me.selectedComponent.model;
		if(CWHF.isNull(model)){
			return null;
		}
		var type=model.type;
		if(type==='Field'){
			me.actionsController.setActionConfigEnabled.call(me.actionsController,true);
		}else{
			me.actionsController.setActionConfigEnabled.call(me.actionsController,false);
		}
		if(type==='Screen'){
			me.actionsController.setActionCopyEnabled.call(me.actionsController,false);
			me.actionsController.setActionDeleteEnabled.call(me.actionsController,false);
		}else{
			var actionEnabled=true;
			if(CWHF.isNull(me.tabAction)){
				if(type==='Panel'||type==='Tab'){
					actionEnabled=false;
				}
			}

			me.actionsController.setActionDeleteEnabled.call(me.actionsController,actionEnabled);
			me.actionsController.setActionCopyEnabled.call(me.actionsController,actionEnabled);
		}
		actionPasteEnabled=me.allowPaste()
		me.actionsController.setActionPasteEnabled.call(me.actionsController,actionPasteEnabled);
	},
	clickOnComponent:function(comp){
		var me=this;
		me.setSelectedComponent(comp);
		var compController=me.getComponentController(comp);
		var componentID=comp.model.getId();
		var urlStr=compController.getPropertyLink(componentID);
		if(CWHF.isNull(urlStr)){
			return true;
		}
		me.refreshOutline.call(me,urlStr);
	},
	refreshOutline:function(urlStr,params,handler){
		var me=this;
		Ext.Ajax.request({
			url: urlStr,
			disableCaching:true,
			params:params,
			success: function(result){
				var jsonData=Ext.decode(result.responseText);
				me.screenEditView.propertiesPanel.setSource(jsonData);
				if(handler){
					handler.call(me);
				}
			},
			failure: function(){
			},
			method:'POST'
		});
	},
	getComponentController:function(comp){
		var me=this;
		var model=comp.model;
		if(CWHF.isNull(model)){
			return null;
		}
		var type=model.type;
		if(type==='Tab'){
			return me.tabController;
		}
		if(type==='Panel'){
			return me.panelController;
		}
		if(type==='Field'){
			return me.fieldController;
		}
		if(type==='Screen'){
			return me.screenController;
		}
		return null;
	},
	changeAttribute:function(property,value,oldValue){
		var me=this;
		var componentID;
		if(me.selectedComponent){
			var model=me.selectedComponent.model;
			var cmpController=me.getComponentController.call(me,me.selectedComponent);
			componentID=model.getId();
			var urlStr=cmpController.getUpdateLink(componentID);
			if(CWHF.isNull(urlStr)){
				return true;
			}
			var params={};
			var source=me.screenEditView.propertiesPanel.getSource();
			for(var x in source){
				params[x]=source[x];
			}
			var type=model.type;
			me.refreshOutline(urlStr,params, function(){
				if(type==='Field'){
					var panel=me.selectedComponent.parentView;
					me.panelController.reload(panel);
				}else if(type==='Panel'){
					var panel=me.selectedComponent;
					me.tabController.reload(panel.parentView,null,null,panel.model.id);
				}else{
					var parentView=me.selectedComponent.parentView;
					if(CWHF.isNull(parentView)){
						parentView=me.selectedComponent;
					}
					me.screenController.reload(parentView,null,null,me.selectedComponent.model.id);
					//mpController.reload(me.selectedComponent);
				}
			});
		}
	},


	createField:function(parentView,aFieldWrapperData){
		var me=this;
		if(CWHF.isNull(me.fieldWrapperCls)){
			me.fieldWrapperCls='com.trackplus.screen.FieldEditWrapperView';
		}
		var fieldWrapperCmp=Ext.create(me.fieldWrapperCls,{
			parentView:parentView,
			model:aFieldWrapperData
		});

		fieldWrapperCmp.addListener('render',function(c){
			if(!aFieldWrapperData.empty){
				c.getEl().on('click', function(e){
					me.clickOnField=true;
					me.clickOnComponent.call(me,c);
					e.stopEvent();
					return false;
				});
				c.getEl().on('contextmenu', function(e,domEl){
					me.clickOnField=true;
					me.clickOnComponent.call(me,c);
					me.showPopup.call(me,e);
				});
				var dragSource=me.fieldController.registerDrag.call(me.fieldController,c);
				parentView.dragDropObjects.push(dragSource);

			}else{
				c.getEl().on('contextmenu', function(e,domEl){
					me.clickOnPanel=true;
					me.clickOnComponent.call(me, c.parentView);
					me.showPopup.call(me,e,c);
				});
			}
			var dropTarget=me.fieldController.registerDrop.call(me.fieldController,c);
			parentView.dragDropObjects.push(dropTarget);

		});
		fieldWrapperCmp.colspan=aFieldWrapperData.colSpan;
		fieldWrapperCmp.rowspan=aFieldWrapperData.rowSpan;

		return fieldWrapperCmp;
	},
	getToolbar:function(){
		var me=this;
		return me.actionsController.getToolbar();
	},
	newTab:function(){
		var me=this;
		me.screenController.reload.call(me.screenController,me.screenView,"addTab",me.screenActionParams);
	},
	newPanel:function(){
		var me=this;
		var selectedTab=me.screenEditView.getSelectedTab();
		me.tabController.reload.call(me.tabController,selectedTab,"addPanel");
	},

	cutItem:function(){
		var me=this;
		me.copyItem(function(){
			me.deleteItem(true);
		},me);
	},
	copyItem:function(succesHandler,scope){
		var me=this;
		if(CWHF.isNull(me.selectedComponent)){
			return false;
		}
		var model=me.selectedComponent.model;
		var sourceType=model.type;
		var sourceID=model.getId();

		var copyModel={
			type:sourceType,
			id:sourceID
		};
		me.clipboard=copyModel;
		me.screenController.copy.call(me.screenController,me.screenModel.getId(),sourceType,sourceID,succesHandler,scope);
	},
	pasteItem:function(){
		var me=this;
		if(CWHF.isNull(me.selectedComponent)){
			return false;
		}
		var model=me.selectedComponent.model;
		var targetType=model.type;
		var targetID=model.getId();
		var targetInfo=null;
		if(me.clipboard){
			if(me.allowPaste()){
				var sourceType=me.clipboard.type;
				var sourceID=me.clipboard.id;
				if(me.emptyFieldCmp){
					var fieldModel=me.emptyFieldCmp.model;
					targetInfo=fieldModel.row+";"+fieldModel.col;
				}
				me.screenController.paste.call(me.screenController,me.screenModel.getId(),sourceType,sourceID,targetType,targetID,targetInfo,function(){
					var view=me.selectedComponent;
					var controller=null;
					if(targetType==="Screen"){
						controller=me.screenController;
					}
					if(targetType==="Tab"){
						controller=me.tabController;
					}
					if(targetType==="Panel"){
						controller=me.panelController;
						if(sourceType==="Panel"){
							controller=me.tabController;
							view=me.selectedComponent.parentView;
						}
					}
					if(targetType==="Field"){
						controller=me.panelController;
						view=me.selectedComponent.parentView;
					}
					controller.reload(view);
				},me);
			}
		}
	},
	allowPaste:function(){
		var me=this;
		if(CWHF.isNull(me.selectedComponent)){
			return false;
		}
		var compType=me.selectedComponent.model.type;
		if(me.clipboard){
			var clipboardType=me.clipboard.type;
			return ((compType==='Screen'&&clipboardType==='Tab')||
				(compType==='Tab'&&clipboardType==='Tab')||
				(compType==='Tab'&&clipboardType==='Panel')||
				(compType==='Panel'&&clipboardType==='Panel')||
				(compType==='Panel'&&clipboardType==='Field')||
				(compType==='Field'&&clipboardType==='Field'));
		}
		return false;
	},
	deleteItem:function(noAsk){
		var me=this;
		if(CWHF.isNull(me.selectedComponent)){
			return false;
		}
		var model=me.selectedComponent.model;
		var compType=model.type;
		var idComp=model.getId();
		var cname="";
		var msg;
		var controller;
		if(compType==="Screen"){
			alert(me.messageCantDeleteScreen);
			return -1;
		}
		var params={};

		if(compType==="Tab"){
			/*if(tabsItem1.items.length===1){
				alert(messageCantLastTab);
				return 0;
			}*/
			cname="tab";
			msg= me.messageDeleteTab;
			controller=me.screenController;
			if(me.screenActionParams){
				for(var x in me.screenActionParams){
					params[x]=me.screenActionParams[x];
				}
			}
		}

		if(compType==="Panel"){
			cname="panel";
			msg= me.messageDeletePanel;
			controller=me.tabController;
			//idParent=me.selectedTab;
		}
		if(compType==="Field"){
			cname="field";
			msg=me.messageDeleteField;
			controller=me.panelController;
		}
		var theName=model.name;
		msg=msg.replace("{0}",theName);
		if(CWHF.isNull(noAsk)||CWHF.isNull(noAsk)){
			var r=confirm(msg);
			if (r===false){
				return 0;
			}
		}
		var action="deleteChild";
		var property=cname+"ID";

		params[property]=idComp;
		var parentView=me.selectedComponent.parentView;
		controller.reload(parentView,action,params);
		me.selectedComponent=null;
	},
	isConfigurableItem:function(){
		return false;
	},
	configItem:function(){
	},
	back:function(){
		window.location.href=this.backAction;
	},
	reload:function(){
		borderLayout.setLoading(true);
		location.reload();
	}
});

Ext.define('com.trackplus.screen.ScreenActionsController',{
	extend:'Ext.Base',
	config: {
		screenEditController:null,
		useConfig:false
	},
	actionNewTab:null,
	actionNewPanel:null,
	actionDelete:null,
	actionConfig:null,
	actionCut:null,
	actionCopy:null,
	actionPaste:null,
	actions:null,
	actionConfigEnabled:false,
	actionDeleteEnabled:false,
	actionCopyEnabled:true,
	actionPasteEnabled:false,

	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		this.initConfig(config);
	},
	setActionConfigEnabled:function(b){
		var me=this;
		me.actionConfigEnabled=b;
		if(me.actionConfig){
			me.actionConfig.setDisabled(!b);
		}
	},
	setActionDeleteEnabled:function(b){
		var me=this;
		me.actionDeleteEnabled=b;
		if(me.actionDelete){
			me.actionDelete.setDisabled(!b);
		}
	},
	setActionCopyEnabled:function(b){
		var me=this;
		me.actionCopyEnabled=b;
		if(me.actionCut){
			me.actionCut.setDisabled(!b);
		}
		if(me.actionCopy){
			me.actionCopy.setDisabled(!b);
		}
	},
	setActionPasteEnabled:function(b){
		var me=this;
		me.actionPasteEnabled=b;
		if(me.actionPaste){
			me.actionPaste.setDisabled(!b);
		}
	},

	initToolbar:function(){
		var me=this;
		me.actionNewTab=Ext.create('Ext.Action',{
			id:'newTabBtn',
			itemId:'newTabBtnItemID',
			overflowText:getText('admin.customize.form.edit.newTab'),
			tooltip:getText('admin.customize.form.edit.newTab.tooltip'),
			text: getText('admin.customize.form.edit.newTab'),
			iconCls: 'newTab',
			disabled:false,
			handler:function(){
				me.screenEditController.newTab.call(me.screenEditController);
			}
		});
		me.actionNewPanel=Ext.create('Ext.Action',{
			id:'newPanelBtn',
			itemId:'newPanelBtnItemID',
			overflowText:getText('admin.customize.form.edit.newPanel'),
			tooltip:getText('admin.customize.form.edit.newPanel.tooltip'),
			text: getText('admin.customize.form.edit.newPanel'),
			iconCls: 'newPanel',
			disabled:false,
			handler:function(){
				me.screenEditController.newPanel.call(me.screenEditController);
			}
		});
		me.actionCut=Ext.create('Ext.Action',{
			id:'cutBtn',
			itemId:'cutBtnItemID',
			overflowText:getText('common.btn.cut'),
			tooltip:getText('common.btn.cut'),
			text: getText('common.btn.cut'),
			iconCls: 'cut',
			disabled:!me.actionCopyEnabled,
			handler:function(){
				me.screenEditController.cutItem.call(me.screenEditController);
			}
		});
		me.actionCopy=Ext.create('Ext.Action',{
			id:'copyBtn',
			itemId:'copyBtnItemID',
			overflowText:getText('common.btn.copy'),
			tooltip:getText('common.btn.copy'),
			text: getText('common.btn.copy'),
			iconCls: 'copy',
			disabled:!me.actionCopyEnabled,
			handler:function(){
				me.screenEditController.copyItem.call(me.screenEditController);
			}
		});
		me.actionPaste=Ext.create('Ext.Action',{
			id:'pasteBtn',
			itemId:'pasteBtnItemID',
			overflowText:getText('common.btn.paste'),
			tooltip:getText('common.btn.paste'),
			text: getText('common.btn.paste'),
			iconCls: 'paste',
			disabled:!me.actionPasteEnabled,
			handler:function(){
				me.screenEditController.pasteItem.call(me.screenEditController);
			}
		});

		me.actionDelete=Ext.create('Ext.Action',{
			id:'deleteBtn',
			itemId:'deleteBtnItemID',
			overflowText:getText('common.btn.delete'),
			tooltip:getText('common.btn.delete'),
			text: getText('common.btn.delete'),
			iconCls: 'delete',
			disabled:!me.actionDeleteEnabled,
			handler:function(){
				me.screenEditController.deleteItem.call(me.screenEditController);
			}
		});
		me.actionConfig=Ext.create('Ext.Action',{
			id:'configureBtn',
			itemId:'configureBtnItemID',
			overflowText:getText('common.btn.config'),
			tooltip:getText('common.btn.config'),
			text:getText('common.btn.config'),
			iconCls: 'btnConfig',
			disabled:!me.actionConfigEnabled,
			handler:function(){
				me.screenEditController.configItem.call(me.screenEditController);
			}
		});

		me.btnBack=Ext.create('Ext.Action',{
			id:'backBtn',
			itemId:'backBtnItemID',
			overflowText:getText('common.btn.done'),
			tooltip:getText('common.btn.done'),
			text: getText('common.btn.done'),
			iconCls: 'btnBack',
			disabled:false,
			handler:function(){
				me.screenEditController.back.call(me.screenEditController);
			}
		});
		me.actions=[];
		if(me.screenEditController.tabAction){
			me.actions.push(me.actionNewTab);
		}
		if(me.screenEditController.tabAction){
			me.actions.push(me.actionNewPanel);
		}
		me.actions.push(me.actionCut);
		me.actions.push(me.actionCopy);
		me.actions.push(me.actionPaste);
		me.actions.push(me.actionDelete);
		if(me.useConfig){
			me.actions.push(me.actionConfig);
		}
		me.actions.push(me.btnBack);
	},
	getToolbar:function(){
		var me=this;
		if(CWHF.isNull(me.actions)){
			me.initToolbar();
		}
		return me.actions;
	},
	createPopup:function(){
		var me=this;
		var contextMenu = new Ext.menu.Menu({
			items: []
		});
		var actions=[];
		if(me.screenEditController.tabAction){
			me.actions.push(me.actionNewTab);
		}
		if(me.screenEditController.tabAction){
			me.actions.push(me.actionNewPanel);
		}
		if(me.actionCopyEnabled===true){
			actions.push(me.actionCut);
		}
		if(me.actionCopyEnabled===true){
			actions.push(me.actionCopy);
		}
		if(me.actionPasteEnabled===true){
			actions.push(me.actionPaste);
		}
		if(me.actionDeleteEnabled===true){
			actions.push(me.actionDelete);
		}
		if(me.useConfig===true&&me.actionConfigEnabled===true){
			actions.push(me.actionConfig);
		}
		if(actions.length===0){
			return null;
		}
		for(var i=0;i<actions.length;i++){
			var action=actions[i];
			var contextMenuItemCfg = {
				text:action.getText(),
				iconCls:action.getIconCls()+"16",
				scope:action,
				tooltip:action.tooltip,
				handler:action.execute,
				itemId:action.itemId
			};
			contextMenu.add(contextMenuItemCfg);
		}
		return contextMenu;
	}
});
