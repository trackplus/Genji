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

//MODEL
Ext.define('com.trackplus.screen.Screen',{
	extend:'Ext.Base',
	config: {
		id:null,
		label:null,
		name:null,
		description:null,
		personID:null,
		tabs:[]
	},
	type:'Screen',
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	}
});
Ext.define('com.trackplus.screen.Tab',{
	extend:'Ext.Base',
	config: {
		id:null,
		name:null,
		label:null,
		description:null,
		panels:[]
	},
	type:'Tab',
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	}
});
Ext.define('com.trackplus.screen.Panel',{
	extend:'Ext.Base',
	config: {
		id:null,
		label:null,
		description:null,
		colsNo:1,
		rowsNo:1,
		fields:[]
	},
	type:'Panel',
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config)
	}
});
//FIXME extends fields
Ext.define('com.trackplus.screen.Field',{
	extend:'Ext.Base',
	config: {
		id:null,
		title:null,
		extClassName:null,
		extReadOnlyClassName:null,
		label:null,
		value:null,
		displayValue:null,
		name:null,
		nameMapping:null,
		jsonData:{},
		empty:false,
		colSpan:0,
		rowSpan:0,
		row:0,
		col:0,
		labelHAlign:null,
		labelVAlign:null,
		valueHAlign:null,
		valueVAlign:null,
		hideLabel:false,
		iconRendering:null,
		fieldID:null
	},
	type:'Field',
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config)
	}
});

//Helpers
com.trackplus.screen.createScreenModel=function(jsonData){
	var screenModel=Ext.create('com.trackplus.screen.Screen',{
		id:jsonData.id,
		name:jsonData.name,
		label:jsonData.label,
		description:jsonData.description
	});
	var tabsJson=jsonData.tabs;
	if(tabsJson!=null){
		var tabs=[];
		for(var i=0;i<tabsJson.length;i++){
			tabs.push(com.trackplus.screen.createTabModel(tabsJson[i]));
		}
		screenModel.setTabs(tabs);
	}
	return screenModel;
};
com.trackplus.screen.createTabModel=function(jsonData){
	var tabModel=Ext.create('com.trackplus.screen.Tab',{
		id:jsonData.id,
		name:jsonData.name,
		label:jsonData.label,
		description:jsonData.description
	});
	var panelsJson=jsonData.panels;
	if(panelsJson!=null){
		var panels=[];
		for(var i=0;i<panelsJson.length;i++){
			panels.push(com.trackplus.screen.createPanelModel(panelsJson[i]));
		}
		tabModel.setPanels(panels);
	}
	return tabModel;
};
com.trackplus.screen.createPanelModel=function(jsonData){
	var panelModel=Ext.create('com.trackplus.screen.Panel',{
		id:jsonData.id,
		name:jsonData.name,
		label:jsonData.label,
		description:jsonData.description,
		colsNo:jsonData.colsNo,
		rowsNo:jsonData.rowsNo
	});
	var fieldsJson=jsonData.fields;
	if(fieldsJson!=null){
		var fields=[];
		for(var i=0;i<fieldsJson.length;i++){
			fields.push(com.trackplus.screen.createFieldModel(fieldsJson[i]));
		}
		panelModel.setFields(fields);
	}
	return panelModel;
};
com.trackplus.screen.createFieldModel=function(fieldData){
	var fieldModel=Ext.create('com.trackplus.screen.Field',{
		id:fieldData.id,
		name:fieldData.name,
		nameMapping:fieldData.nameMapping,
		label:fieldData.label,
		description:fieldData.description,
		empty:fieldData.empty,
		row:fieldData.row,
		col:fieldData.col,
		rowSpan:fieldData.rowSpan,
		colSpan:fieldData.colSpan,
		jsonData:fieldData.jsonData,
		extClassName:fieldData.extClassName,
		extReadOnlyClassName:fieldData.extReadOnlyClassName,
		value:fieldData.value,
		displayValue:fieldData.displayValue,
		labelHAlign:fieldData.labelHAlign,
		labelVAlign:fieldData.labelVAlign,
		valueHAlign:fieldData.valueHAlign,
		valueVAlign:fieldData.valueVAlign,
		hideLabel:fieldData.hideLabel,
		iconRendering:fieldData.iconRendering,
		fieldID:fieldData.fieldID
	});
	return fieldModel;
};


//VIEW
Ext.define('com.trackplus.screen.ScreenView',{
	extend: 'Ext.container.Container',
	border: false,
	margin: '0 0 0 0',
	frame: false,
	collapsible:false,
	padding:0,
	plain:true,
	originalTitle:'',
	layout:'fit',
	region:'center',
	config: {
		model:null,
		parentView:null,
		oneTab:false
	},
	initComponent: function(){
		var me=this;

		if(me.oneTab){
			me.margin= '0 0 0 0';
		}else{
			me.margin= '0 0 0 0';
		}

		Ext.apply(this);
		me.callParent();
	},
	getSelectedTab:function(){
		var me=this;
		if(me.oneTab){
			return  me.items.get(0);
		}else{
			return  me.items.get(0).getActiveTab();
		}
	}
});

Ext.define('com.trackplus.screen.TabView',{
	extend: 'Ext.container.Container',
	config: {
		model:null,
		parentView:null,
		oneTab:false
	},
	oneTab:false,
	border:false,
	style:{
		overflow:'auto'
	},
	initComponent: function(){
		var me=this;
		if(me.oneTab==true){
			me.margin= '0 0 0 0';
			me.region='center';
		}else{
			me.margin='0 0 0 0';
			me.title=me.model.getLabel();
		}
		me.callParent();
	},
	unregister:function(){
		var me=this;
		var panels=me.items;
		var panelCmp;
		for(var i=0;i<panels.getCount();i++){
			panelCmp=panels.getAt(i);
			panelCmp.unregister.call(panelCmp);
		}
	}
});


Ext.define('com.trackplus.screen.PanelView',{
	extend: 'Ext.container.Container',
	config: {
		model:null,
		parentView:null,
		myIndex:0,
		myLength:1
	},
	dragDropObjects:[],
	margin:'0 0 0 0',
	border:false,
	myComponentCls:null,
	unregister:function(){
		var me=this;
		var ddObjects= me.dragDropObjects;
		for(var i=0;i<ddObjects.length;i++){
			var o=ddObjects[i];
			o.destroy();
			o=null;
		}
		me.dragDropObjects=[];
	},

	/*initComponent: function(){
		var me=this;
		var cfg=me.getPanelConfig.call(me,me.model,me.myIndex,me.myLength);
		Ext.apply(this,cfg);
		me.callParent();
	},*/
	getPanelConfig:function(model,index,length){
		var me=this;
		var cls="screenPanel";
		var margin='0 0 5 0';
		if(index==0){
			cls="screenPanel-first";
			margin='0 0 5 0';
		}
		if(index%2==1){
			cls="screenPanel-odd";
		}
		me.myComponentCls=cls;
		return {
			cls:cls,
			margin:margin,
			border:true,
			layout: {
				type: 'table',
				columns: model.colsNo,
				tableAttrs: {
					style: {
						minWidth: '100%'
					}
				},
				trAttrs:{
					'class':'screenField-tr',
					style:{
						'vertical-align':'top'
					}
				}
			},
			defaults: {frame:false, border: false}
		};
	}
});

Ext.define('com.trackplus.screen.FieldErrorView',{
	extend:'Ext.panel.Panel',
	config:{
		fieldModel:null,
		parentView:null,
		error:null
	},
	layout:'anchor',
	border:true,
	bodyBorder:false,
	bodyCls:'screeFieldError',
	initComponent : function(){
		var me=this;
		Ext.apply(this);
		var msgCmp=Ext.create('Ext.form.field.Display',{
			fieldLabel:'Class',
			anchor : '100%',
			value:me.fieldModel.extClassName
		});
		var errorCmp=Ext.create('Ext.form.field.Display',{
			fieldLabel:'Error',
			anchor : '100%',
			value:me.error.message
		});
		me.items=[msgCmp,errorCmp];
		me.callParent();
	}
});


Ext.define('com.trackplus.screen.BaseScreenController',{
	extend:'Ext.Base',
	mixins: {
		observable: 'Ext.util.Observable'
	},
	config: {
		screenModel:null,
		showOneTab:false,
		lastSelectedTab:null,
		refreshTabUrl:null,
		storeTabUrl:null,
		screenViewCls:'com.trackplus.screen.ScreenView',
		tabViewCls:'com.trackplus.screen.TabView',
		panelViewCls:'com.trackplus.screen.PanelView',
		fieldWrapperCls:null,
		fieldErrorCls:null,
		dataModel:null
	},
	screenViewCls:'com.trackplus.screen.ScreenView',
	tabViewCls:'com.trackplus.screen.TabView',
	panelViewCls:'com.trackplus.screen.PanelView',
	fieldErrorCls:'com.trackplus.screen.FieldErrorView',

	screenView:null,
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		this.mixins.observable.constructor.call(this, config);
	},
	createScreenView:function(screenModel){
		var me=this;
		var tabsData=me.screenModel.getTabs();
		me.screenView=Ext.create(me.screenViewCls,{
			model:screenModel,
			oneTab:me.showOneTab&&tabsData.length==1
		});
		me.refreshScreenModel(me.screenView,screenModel,me.lastSelectedTab);
		me.afterScreenCreated.call(me,me.screenView);
		return me.screenView;
	},
	afterScreenCreated:function(screenCmp){
	},
	refreshScreenModel:function(screenView,screenModel,selectedTab){
		var me=this;
		me.screenModel=screenModel;
		me.lastSelectedTab=selectedTab;
		screenView.model=screenModel;
		screenView.removeAll(true);
		var tabsData=me.screenModel.getTabs();
		var cmp;
		if(me.showOneTab==true&&tabsData.length==1){
			cmp=me.createTab(screenView,tabsData[0],true);
			cmp.region='center';
		}else{
			cmp=me.createTabPanel(screenView,tabsData);
		}
		screenView.add(cmp);
		screenView.doLayout();
	},
	createTabPanel:function(parentView,tabsData){
		var me=this;
		var tabsItems=[];
		var activeTab=0;
		for(var i=0;i<tabsData.length;i++){
			tabsItems.push(me.createTab(parentView,tabsData[i]));
			if(me.lastSelectedTab!=null&&tabsData[i].getId()==me.lastSelectedTab){
				activeTab=i;
			}
		}
		var tabsPanel=Ext.create('Ext.tab.Panel',{
			plain:true,
			border:false,
			region:'center',
			bodyBorder:false,
			defaults:{autoScrollX: true},
			items:tabsItems,
			activeTab:activeTab,
			cls:'screenTabPanel'
		});
		me.afterTabPanelCreated.call(me,tabsPanel);
		return tabsPanel;
	},
	afterTabPanelCreated:function(tabsPanel){
	},

	createTab:function(parentView,tabModel,oneTab){
		var me=this;
	    var tabCmp= Ext.create(me.tabViewCls,{
			parentView:parentView,
			model:tabModel,
			refreshTabUrl:me.refreshTabUrl,
			storeTabUrl:me.storeTabUrl,
			getPanelConfig:me.getPanelConfig,
			oneTab:oneTab
		});
		me.refreshTabModel.call(me,tabCmp,tabModel);
		tabCmp.addListener('activate',me.tabActivate,me);
		me.afterTabCreated.call(me,tabCmp);
		return tabCmp;
	},
	afterTabCreated:function(tabCmp){
	},
	tabActivate:function(tab){
		var me=this;
		var tabModel=tab.model;

		if(me.storeTabUrl!=null){
			Ext.Ajax.request({
				url: me.storeTabUrl,
				disableCaching :true,
				method:'POST',
				params:{"tabID":tabModel.getId()}
			})
		}
		if(me.refreshTabUrl!=null){
			me.refreshTab.call(me,tab);
		}
	},
	refreshTabModel:function(tabCmp,tabModel,idChild){
		var me=this;
		var panelsData=tabModel.panels;
		tabCmp.unregister();
		tabCmp.removeAll(true);
		tabCmp.tabModel=tabModel;
		var panels=new Array(0);
		var panelToSelect=null;
		if(panelsData!=null&&panelsData.length>0){
			for(var i=0;i<panelsData.length;i++){
				var panCmp=me.createPanel(tabCmp,panelsData[i],i,panelsData.length);
				panels.push(panCmp);
				if(idChild!=null&&idChild==panelsData[i].id){
					panelToSelect=panCmp;
				}
			}
		}
		tabCmp.add(panels);
		return panelToSelect;
	},
	refreshTab:function(tabCmp,url){
		var me=this;
		var tabModel=tabCmp.model;
		var urlStr=me.refreshTabUrl;
		if(url!=null&&url.length>0){
			urlStr=url;
		}
		if(urlStr==null){
			return -1;
		}
		tabCmp.setLoading(getText("common.lbl.loading"));
		Ext.Ajax.request({
			url:urlStr ,
			disableCaching :true,
			encoding: "utf-8",
			params:{"tabID":tabModel.getId(),'componentID':tabModel.getId()},
			success: function(response){
				var jsonData=Ext.decode(response.responseText);
				var tabModelNew=com.trackplus.screen.createTabModel(jsonData.data);
				me.refreshTabModel.call(me,tabCmp,tabModelNew);
				tabCmp.setLoading(false);
			},
			failure: function(response){
				tabCmp.setLoading(false);
				com.trackplus.util.requestFailureHandler(response);
			}
		});
	},

	createPanel:function(parentView,model,index,length){
		var me=this;
		var panel=Ext.create(me.panelViewCls,{
			parentView:parentView,
			model:model,
			myIndex:index,
			myLength:length
		});
		me.refreshPanelModel(panel,model);
		me.afterPanelCreated.call(me,panel);
		return panel;
	},
	afterPanelCreated:function(panelCmp){
	},
	refreshPanelModel:function(panelCmp,panelModel){
		var me=this;
		var fieldsData=panelModel.fields;
		var fields=new Array(0);
		panelCmp.unregister.call(panelCmp);
		panelCmp.removeAll.call(panelCmp,true);
		var cfg=panelCmp.getPanelConfig.call(panelCmp,panelModel,panelCmp.myIndex,panelCmp.myLength);
		Ext.apply(panelCmp,cfg);
		if(fieldsData!=null){
			for(var i=0;i<fieldsData.length;i++){
				fields.push(me.createField(panelCmp, fieldsData[i]));
			}
		}
		panelCmp.add(fields);
	},

	createField:function(parentView,aFieldData){
		var me=this;
		var aField;
		if(aFieldData.empty){
			aField=Ext.create('Ext.form.Label');
		}else{
			try{
				aField=Ext.create(aFieldData.extClassName,{jsonData:aFieldData.jsonData,parentView:parentView});
			}catch(e){
				aField=Ext.create(me.fieldErrorCls, {
					parentView:parentView,
					fieldModel:aFieldData,
					error:e
				});
			}
			aField.colspan=aFieldData.colSpan;
			aField.rowspan=aFieldData.rowSpan;
		}
		return aField;
	}
});



Ext.define('com.trackplus.screen.BaseScreenFacade',{
	extend:'Ext.Base',
	config: {
		screenModel:null,
		readOnlyMode:false,
		showOneTab:false,
		lastSelectedTab:null,
		refreshTabUrl:null,
		storeTabUrl:null,
		controllerCls:null,
		screenViewCls:'com.trackplus.screen.ScreenView',
		tabViewCls:'com.trackplus.screen.TabView',
		panelViewCls:'com.trackplus.screen.PanelView',
		fieldWrapperCls:null,
		fieldErrorCls:'com.trackplus.screen.FieldErrorView',
		dataModel:null
	},
	screenViewCls:'com.trackplus.screen.ScreenView',
	panelViewCls:'com.trackplus.screen.PanelView',
	fieldErrorCls:'com.trackplus.screen.FieldErrorView',
	controllerCls:'com.trackplus.screen.BaseScreenController',
	controller:null,
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
   	    Ext.apply(me, config);
		me.controller=Ext.create(me.controllerCls,{
			screenModel:me.screenModel,
			readOnlyMode:me.readOnlyMode,
			showOneTab:me.showOneTab,
			lastSelectedTab:me.lastSelectedTab,
			refreshTabUrl:me.refreshTabUrl,
			storeTabUrl:me.storeTabUrl,
            tabViewCls:me.tabViewCls,
			panelViewCls:me.panelViewCls,
			fieldErrorCls:me.fieldErrorCls,
			dataModel:me.dataModel
		});
	},
	screenView:null,
	createViewComponent:function(){
		var me=this;
		me.screenView=me.controller.createScreenView.call(me.controller,me.screenModel);
		return me.screenView;
	}
});
