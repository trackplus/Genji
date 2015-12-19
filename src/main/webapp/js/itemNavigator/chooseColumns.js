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

Ext.define('com.trackplus.itemNavigator.ChooseColumns',{
	extend:'Ext.Base',
	config: {
		itemNavigatorController:null,
		filterType:null,
		filterID:null,
		includeLongFields:false
	},
	ccController:null,
	constructor : function(cfg) {
		var me = this;
		var config = cfg || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		me.ccController=Ext.create('com.trackplus.itemNavigator.ChooseColumnsController',{
			itemNavigatorController:me.itemNavigatorController,
			filterType:me.filterType,
			filterID:me.filterID,
			includeLongFields:me.includeLongFields
		});
	},
	setIncludeLongFields:function(includeLongFields){
		this.includeLongFields=includeLongFields;
		this.ccController.includeLongFields=includeLongFields;
	},
	showDialog:function(){
		var me=this;
		me.ccController.showDialog.call(me.ccController);
	},
	destroyMe:function(){
	   var me=this;
		if(me.ccController){
			me.ccController.destroyMe.call(me.ccController);
			me.ccController=null;
		}
	}
});

Ext.define('com.trackplus.itemNavigator.ChooseColumnsController',{
	extend:'Ext.Base',
	config: {
		itemNavigatorController:null,
		filterType:null,
		filterID:null,
		includeLongFields:false
	},
	constructor : function(cfg) {
		var me = this;
		var config = cfg || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	},
	destroyMe:function(){
		var me=this;
		if(me.win){
			me.win.destroy();
		}
	},

	showDialog: function() {
		var width = 300;
		var height = 400;
		var loadParams = {filterType:this.filterType, filterID:this.filterID, includeLongFields:this.includeLongFields};
		var load = {loadUrl:"layoutColumns.action", loadUrlParams:loadParams};
		var submitParams = {filterType:this.filterType, filterID:this.filterID};
		var submit = {submitUrl:"layoutColumns!save.action",
					submitUrlParams:submitParams,
					refreshAfterSubmitHandler:this.reload, loading:true};
		var windowParameters = {title:getText("itemov.lbl.chooseColumns"),
			width:width,
			height:height,
			load:load,
			submit:submit,
			items:[],
			postDataProcess:this.createColumnItems,
			panelConfig:{
				bodyStyle:{
					padding:'0 0 0 10'
				}
			}
		};
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
		windowConfig.showWindowByConfig(this);
	},

	/**
	 * Refreshes a grid's store and selects a row by rowToSelect parameter if it is specified
	 * Called from selectAfterReload() (grid and tree refresh) and from simple grid CRUD
	 */
	reload: function() {
		this.itemNavigatorController.changeLayout.call(this.itemNavigatorController);
	},

	createColumnItems: function(data, panel){
		var me=this;
		var columnFieldList=data;
		var items=[];
		for(var i=0;i<columnFieldList.length;i++) {
			items.push(me.createColumnItem(columnFieldList[i]));
		}
		panel.add(items);
	},

	createColumnItem: function(columnField){
		if(columnField.fieldID<0){
			name='selectedColumnsMap.f_'+(columnField.fieldID*-1);
		}else{
			name='selectedColumnsMap.f'+columnField.fieldID;
		}
		return CWHF.createCheckbox(null, name,
				{value:columnField.used, boxLabel:columnField.label, margin: 5});
	}

	/*showDialog:function(){
		var me=this;
		borderLayout.setLoading(true);
		me.formPanel=Ext.create('Ext.form.Panel',{
			url:'itemNavigatorLayout!saveLayout.action',
			id:'chooseColumnsFormPanel',
			layout:'anchor',
			standardSubmit:false,
			bodyBorder:false,
			border    : false,
			autoScroll:true,
			margin: '0 0 0 0',
			bodyStyle:{
				padding:'10px 10px 5px 10px'
			}
		});
		if(me.win){
			me.win.destroy();
		}
		me.win = Ext.create('Ext.window.Window',{
			layout      : 'fit',
			width       : 300,
			height      : 400,
			closeAction :'destroy',
			plain       : true,
			title		 : getText('itemov.lbl.chooseColumns'),
			modal       : true,
			cls:'bottomButtonsDialog',
			bodyBorder:true,
			margin:'0 0 0 0',
			style:{
				padding:'5px 0px 0px 0px'
			},
			bodyPadding:'0px',
			autoScroll  : false,
			items       : [me.formPanel],
			buttons     : [
				{text:getText('common.btn.save') ,handler  :me.saveColumnsLayout,scope:me},
				{text:getText('common.btn.close'),handler  :function(){me.win.destroy();},scope:me}
			]
		});
		var urlStr='itemNavigatorLayout!loadAllColumns.action';
		Ext.Ajax.request({
			url: urlStr,
			params:{
				filterType:me.filterType,
				filterID:me.filterID,
				includeLongFields:me.includeLongFields
			},
			disableCaching:true,
			success: function(response){
				var jsonData=Ext.decode(response.responseText);
				var allColumns=jsonData.data;
				me.refresh.call(me,allColumns);
				borderLayout.setLoading(false);
				me.win.show();
			},
			failure: function(){
				borderLayout.setLoading(false);
				//alert("failure");
			}
		});
	},
	refresh:function(allColumns){
		var me=this;
		me.formPanel.removeAll();
		var items=[];
		var col;
		var name="";
		var idx=0;
		for(var i=0;i<allColumns.length;i++){
			col=allColumns[i];
			var longField=col.reportLayout.renderAsLong;

			if(longField===false&&(col.reportLayout.reportField===-1005||
				col.reportLayout.reportField===-1007||
				col.reportLayout.reportField===-1008||
				col.reportLayout.reportField===23)){
				longField=true;
			}
			if(col.reportLayout.reportField<0){
				name='selectedColumnsMap.f_'+(col.reportLayout.reportField*-1);
			}else{
				name='selectedColumnsMap.f'+col.reportLayout.reportField;
			}
			var hidden=false;
			if(me.includeLongFields===false){
				hidden=(longField===true);
			}
			if(!hidden){
				idx++;
			}
			var cls="";
			if(idx%2===1){
				cls='checkColumnOdd';
			}
			items.push({
				xtype:"checkboxfield",
				name:name,
				cls:cls,
				boxLabel:col.reportLayout.label,
				checked:col.used,
				inputValue:'true',
				value:col.used,
				hidden:hidden,
				anchor:'100%'
			});
		}
		me.formPanel.add(items);
		me.formPanel.updateLayout();
	},*/
	/*saveColumnsLayout:function() {
		var me=this;
		borderLayout.setLoading(true);
		me.formPanel.getForm().submit({
			params:{
				filterType:me.filterType,
				filterID:me.filterID
			},
			success: function(form, action) {
				me.win.destroy();
				borderLayout.setLoading(false);
				me.itemNavigatorController.changeLayout.call(me.itemNavigatorController);
			},
			failure: function(form, action) {
				borderLayout.setLoading(false);
				alert("failure");
			}
		});
	}*/
});
