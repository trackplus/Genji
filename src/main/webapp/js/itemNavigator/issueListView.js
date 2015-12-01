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


Ext.define('com.trackplus.itemNavigator.IssueListViewDescriptor',{
	extend:'Ext.Base',
	config: {
		id:'',
		enabledColumnChoose:false,
		enabledGrouping:false,
		useLongFields:false,
		name:'',
		description:'',
		jsClass:null,
		iconCls:null,
		plainData:false
	},
	constructor : function(cfg) {
		var me = this;
		var config = cfg || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	}
});

Ext.define('com.trackplus.itemNavigator.IssueListViewPlugin',{
	extend:'Ext.util.Observable',
	config: {
		id:'',
		descriptor:null,
		model:null,
		pluginCls:null,
		settingsVisible:false,
		plainGrid:false
	},
	view:null,
	/**
	 * @cfg {Function} refreshData
	 * @param {Object} data
	 */
	refreshData: function(data){
	},
	dataChangeSuccess:function(opts){
	},
	containsFields:function(fields){
		var me=this;
		if(fields){
            //TOTAL_EXPENSE_COST = -1100
            //TOTAL_EXPENSE_TIME = -1101
            // TOTAL_PLANNED_COST = -1300;
            //TOTAL_PLANNED_TIME = -1301;
            //REMAINING_PLANNED_COST = -1400;
            //REMAINING_PLANNED_TIME = -1401;
			var hardCodedRefreshFields=[-1100,-1101,-1300,-1301,-1400,-1401];
			for(var i=0;i<fields.length;i++){
				if(Ext.Array.indexOf(hardCodedRefreshFields,fields[i])!==-1){
					return true;
				}
				if(me.containsOneField(fields[i])){
					return true;
				}
			}
		}
		return false;
	},
	containsOneField:function(fieldID){
	    var me=this;
		var shortFields=me.model.layout.shortFields;
		for(var i=0;i<shortFields.length;i++){
			if(shortFields[i].reportField===fieldID){
				return true;
			}
		}
		if(me.descriptor.useLongFields){
			var longFields=me.model.layout.longFields;
			for(var i=0;i<longFields.length;i++){
				if(longFields[i].reportField===fieldID){
					return true;
				}
			}
		}
		return false;
	},

	createSettingsPanel:function(btnGroup, btnChooseColumns, btnSaveAsStandardLayout, btnUseStandardLayout, btnSaveAsFilterLayout){
		var me=this;
		var items=me.getSettingsToolbarItems(btnGroup, btnChooseColumns, btnSaveAsStandardLayout, btnUseStandardLayout, btnSaveAsFilterLayout);
		return Ext.create('Ext.toolbar.Toolbar', {
			region:'north',
			layout: {
				overflowHandler: 'Menu'
			},
			enableOverflow: true,
			cls:'toolbarActions',
			border: '0 0 1 0',
			//hidden:!me.settingsVisible,
			defaults: {
				cls:'toolbarItemAction',
				scale:'small',
				iconAlign: 'left',
				enableToggle:false
			},
			items:items
		});
	},
	getSettingsToolbarItems:function(btnGroup, btnChooseColumns, btnSaveAsStandardLayout, btnUseStandardLayout, btnSaveAsFilterLayout){
		var me=this;
		var items=new Array();
		items.push(btnGroup);
		items.push(btnChooseColumns);
		//items.push('->');
		if (btnSaveAsStandardLayout) {
			items.push(btnSaveAsStandardLayout);
		}
		if (btnUseStandardLayout) {
			items.push(btnUseStandardLayout);
		}
		if (btnSaveAsFilterLayout) {
			items.push(btnSaveAsFilterLayout);
		}
		return items;
	},

	getSettingsHeight:function(){
		return 27;
	},


	/**
	 * @cfg {Function} createView
	 *
	 */
	createView:function(){
		return Ext.create('Ext.panel.Panel',{
			html:'Implement me!'
		});
	},
	destroyView:function(){
	},

	/**
	 * @cfg {Function} getSelectedIssues
	 *
	 */
	getSelectedIssues:function(){
		return null;
	},
	selectItemByIndex:function(workItemIndex){
	},
	selectItem:function(workItemID,isArray){
	},
	deselectItem:function(workItemID,isArray){
	},


	constructor : function(cfg) {
		var me = this;
		var config = cfg || {};
		me.initialConfig = config;
		//me.events=[];
		this.listeners = config.listeners;
		com.trackplus.itemNavigator.IssueListViewPlugin.superclass.constructor.call(this, config);
	},
	getPopupMenuItems:function(rowData,grid,index,record){
		return null;
	},
	destroy:function(){
		var me=this;
		me.id=null;
		me.descriptor=null;
		me.model=null;
		me.pluginCls=null;
		me.clearListeners();
		if(me.view){
			me.view.clearListeners.call(me.view);
			me.view.removeAll.call(me.view,true);
			me.view.destroy.call(me.view);
			me.view=null;
		}

	},
	createFields:function(){
		var me=this;
		var fields=[];
		var shortFields=me.model.layout.shortFields;
		var sfield;
		for(var i=0;i<shortFields.length;i++){
			sfield=shortFields[i];
			fields.push({name:'f'+sfield.reportField,type:sfield.extJsType,allowNull:true});
			if(sfield.so===true){
				var type='int';
				if(sfield.soType){
					type=sfield.soType;
				}
				fields.push({name:'f_so'+sfield.reportField,type:type,allowNull:true});
			}
		}
		fields.push({name:'group',type:'boolean'});
		fields.push({name:'accessLevelFlag',type:'boolean'});
		fields.push({name:'editable',type:'boolean'});
		fields.push({name:'linkable',type:'boolean'});
		fields.push({name:'notEditableFields'});
		fields.push({name:'leaf',type:'boolean'});
		fields.push({name:'workItemID',type:'int',allowNull:true});
		fields.push({name:'attachmentIds'});
		fields.push({name:'workItemIndex',type:'int',allowNull:true});
		fields.push({name:'projectID',type:'int'});
		fields.push({name:'issueTypeID',type:'int'});
		fields.push({name:'parentID',type:'int',allowNull:true});
		fields.push({name:'originatorID',type:'int'});
		fields.push({name:'projectName'});
		fields.push({name:'longFields'});
		fields.push({name:'iconCls'});
		fields.push({name:'cssColorClass'});
		fields.push({name:'cssColorClassGroup'});
		fields.push({name:'queryFieldCSS'});
		fields.push({name:'Name'});
		return fields;
	},
	createColumn:function(i,layoutData,sortable,useTree){
		var me=this;
		var colType='Ext.grid.column.Column';
		var colID='ID_'+layoutData.id;
		if(i===0&&useTree){
			colType='Ext.tree.Column';
		}
		var colHeader='&nbsp;';
		var colCls=null;
		var colResizable=true;
		var align='left';
		if(i===0&&useTree){
			colHeader='&nbsp;<span class="expandAll" id="rowExpandAllTrg">&nbsp;&nbsp;&nbsp;&nbsp;</span><span class="collapseAll" id="rowCollapseAllTrg">&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;&nbsp;'+layoutData.label;
		}else{
			if(layoutData.renderContentAsImg===true||layoutData.reportField===-1001){
				colCls='headerIcon headerIcon'+layoutData.reportField;
				if(layoutData.reportField!==-10001){
					colResizable=false;
				}
				if (layoutData.fieldIsCustomIcon) {
					colCls='headerIcon headerIcon-customOption';
					//true beacause multiple icons are possible
					colResizable=true;
				}
				align='center';
			}else{
				colHeader=layoutData.label;
			}
		}
		var colCfg={
			text:colHeader,
			draggable :true,//(i!==0),
			hideable:(i!==0),
			menuDisabled:true,
			width:layoutData.fieldWidth,
			dataIndex:'f'+layoutData.reportField,
			fieldID: layoutData.reportField,
			fieldIsCustomIcon: layoutData.fieldIsCustomIcon,
			itemId:'R_'+layoutData.id,
			sortable:sortable,
			sortWithSO:(layoutData.so===true),
			hidden:false,//layoutData.hidden,
			tdCls:'simpleTreeGridCell',
			id:colID,
			cls:colCls,
			align:align,
			resizable:colResizable
		};
		var col=Ext.create(colType,colCfg);
		if(layoutData.so===true){
			col.getSortParam=function(){
				return 'f_so'+this.dataIndex.substring(1);
			};
		}
		if(i===0&&me.plainGrid===true){
			col.renderer=function(value,metaData,record,rowIndex,colIndex,store,view){
				var iconCls=record.data.iconCls;
				return '<img class="x-tree-icon '+iconCls+'" role="presentation" src="data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==">'+
					'<span class="x-tree-node-text">'+value+'</span>';
			};
		}
		/*if(com.trackplus.TrackplusConfig.mobile===true&&
			layoutData.reportField===17&&i!==0){
			//synopsis
			col.renderer=function(value,metaData,record,rowIndex,colIndex,store,view){
				var urlStr='printItem.action?key='+record.data.workItemID;
				var synopsisClass='synopsis_blue newWindow';
				return '<a href="'+urlStr+'" class="'+synopsisClass+'" target="printItem'+record.data.workItemID+'">'+value+'</a>';
			};
		}*/
		if(layoutData.reportField===-1001){//attachments
			if(i===0){
				col.renderer = function (value, metaData, record, rowIndex, colIndex, store, view) {
					if(record.data['group']===true) {
						return record.data['Name'];
					}else{
						if (CWHF.isNull(value)) {
							return "";
						}
						return '<a href="javaScript:openAttachments(' + record.data.workItemID + ',&quot;' + record.data['attachmentIds'] + '&quot;)" class="downloadAttachments">' + value + '</a>';
					}
				};
			}else {
				col.renderer = function (value, metaData, record, rowIndex, colIndex, store, view) {
					if (CWHF.isNull(value)) {
						return "";
					}
					return '<a href="javaScript:openAttachments(' + record.data.workItemID + ',&quot;' + record.data['attachmentIds'] + '&quot;)" class="downloadAttachments">' + value + '</a>';
				};
			}
		}
		if(layoutData.renderContentAsImg===true&&i!==0){
			var fieldWidth = layoutData["fieldWidth"];
			if (CWHF.isNull(fieldWidth)) {
				col.width=25;
			} else {
				col.width=fieldWidth;
			}
			col.tdCls='iconTreeGridCell';
			col.renderer=function(value,metaData,record,rowIndex,colIndex,store,view){
				var myIndex=colIndex;
				if(me.model.layout.bulkEdit){
					myIndex=colIndex-1;
				}
				var reportField=this.columns[myIndex].dataIndex.substring(1);
				var fieldIsCustomIcon = this.columns[myIndex].fieldIsCustomIcon;
				if (reportField===-1006 || reportField===-1010){
					//private symbol
					if (reportField===-1006) {
						if(value&&value!==''){
							return '<img src="'+com.trackplus.TrackplusConfig.icon16Path+value+'">';
						}else{
							return '';
						}
					} else {
						//overflow icons
						if (reportField===-1010) {
							if (value && value.length>0) {
								var selectedValues = value.split(",");
								//valuesArr = value.split()
								var outOfBoundIcons = "";
								Ext.Array.forEach(selectedValues, function(option) {
									if (option) {
										var srcImg='<img src="'+com.trackplus.TrackplusConfig.icon16Path+option+'">';
										outOfBoundIcons = outOfBoundIcons + srcImg;
									}
								}, this);
								return outOfBoundIcons;
							} else {
								return '';
							}
						}
					}
				}else{
					if(value&&value!==''){
						if (fieldIsCustomIcon) {
							var selectedValues = value.split(",");
							var images = "";
							Ext.Array.forEach(selectedValues, function(option) {
								if (option) {
									var srcImg="optionIconStream.action?&optionID="+option;
									images = images + '<img src="'+srcImg+'">';
								}
							} , this);
							return images;
						} else {
							var srcImg="optionIconStream.action?fieldID="+reportField+"&optionID="+value;
							return '<img src="'+srcImg+'" ">';
						}
					}else{
						return "";
					}
				}
			};
		}
		return col;
	},
	createSelModel:function(){
		var me=this;
		me.useSelectionModel=me.model.layout.bulkEdit;
		var selModel=null;
		if(me.model.layout.bulkEdit===true){
			selModel=Ext.create('com.trackplus.itemNavigator.CheckboxModel',{
				allowDeselect:true,
				checkOnly:false,
				mode:'MULTI'
			});
		}
		return selModel;
	},
	columnMoveHandler:function(ct,column,fromIdx,toIdx){
		var me=this;
		var urlStr="layoutColumns!moveColumn.action";
		var layoutID=column.itemId.substring(2);
		Ext.Ajax.request({
			url: urlStr,
			disableCaching:true,
			success: function(result){
				var jsonData=Ext.decode(result.responseText);
				if(jsonData.success===false){
					var msg=getText(jsonData.errorMessage);
					CWHF.showMsgError(msg);
				}
				me.fireEvent.call(me,'layoutchange');
			},
			failure: function(response){
				com.trackplus.util.requestFailureHandler(result);
			},
			method:'POST',
			//index in layout start from 1.
			params:{
				layoutID:layoutID,
				fromIdx:fromIdx,
				toIdx:toIdx,
				filterType:me.model.queryContext.queryType,
				filterID:me.model.queryContext.queryID
			}
		});
	},
	onGridSelectionChange:function(sm, selections){
		var me=this;
		me.fireEvent.call(me,'selectionchange',selections);
		return false;
	},

	getRowClass: function(record, rowIndex, rp, ds){
     	var me=this;
		var cls="";
		if(record.data['group']===true){//grouping
			cls+=" reportsTableGrouping";
			if(record.data['cssColorClassGroup']){
				cls+=" "+record.data['cssColorClassGroup'];
			}
		}
		if(record.data['cssColorClass']){
			cls+=" "+record.data['cssColorClass'];
		}
		if(me.queryFieldCSS&&record.data['queryFieldCSS']){
			cls+=' queryFieldCSS rowCls_'+me.queryFieldCSS+"_"+ record.data['queryFieldCSS'];
		}
		return cls;
     },
	updatePossibleFieldOptions:function(jsonData,extraParams){
	}
});


Ext.define('com.trackplus.itemNavigator.IssueListViewFacade',{
	extend:'Ext.Base',
	config: {
		descriptor:null,
		model:null,
		itemNavigatorController:null,
		queryContext:null
	},
	listViewPlugin:null,
	settingsPanel:null,
	filterPanel:null,
	settingsHeight:null,

	view:null,
	constructor : function(cfg) {
		var me = this;
		var config = cfg || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	},
	destroy:function(){
		var me=this;
		if(me.listViewPlugin){
			me.listViewPlugin.destroyView.call(me.listViewPlugin);
		}
		me.listViewPlugin=null;
		me.descriptor=null;
		me.model=null;
		delete me.listViewPlugin;
		delete me.descriptor;
		delete me.model;

	},
	createView:function(model,settingsVisible){
		var me=this;
		me.model=model;

		var pluginCls=me.descriptor.jsClass;
		if(me.listViewPlugin){
			me.listViewPlugin.destroyView.call(me.listViewPlugin);
		}
		if(CWHF.isNull(me.listViewPlugin)){
			me.listViewPlugin=Ext.create(pluginCls,{
				descriptor:me.descriptor,
				model:me.model,
				settingsVisible:settingsVisible,
				itemNavigatorController:me.itemNavigatorController
			});
			me.addViewPluginListeners();
		}
		me.listView=me.listViewPlugin.createView.call(me.listViewPlugin);
		var btnGroup=Ext.create('Ext.button.Button',{
			//margin:'3 5 3 3',
			//id:'btnGroupBy',
			iconCls:'group',
			//itemId:'btnGroupByItemID',
			hidden:(me.descriptor.enabledGrouping===false),
			text:getText('itemov.lbl.groupBy'),
			tooltip:getText('itemov.lbl.groupBy'),
			handler:function(){
				me.itemNavigatorController.showGroupByPopup.call(me.itemNavigatorController);
			}
		 });
		 var btnChooseColumns=Ext.create('Ext.button.Button',{
			//margin:'3 5 3 3',
			//id:'btnChooseColumns',
			iconCls:'column',
			//itemId:'btnChooseColumnsItemID',
			hidden:(me.descriptor.enabledColumnChoose===false),
			text:getText('itemov.lbl.chooseColumns'),
			tooltip:getText('itemov.lbl.chooseColumns'),
			handler:function(){
				 me.itemNavigatorController.showColumnsPopup.call(me.itemNavigatorController);
			}
		 });

		 var btnSaveAsStandardLayout = null;
		 if (com.trackplus.TrackplusConfig.user.sys||com.trackplus.TrackplusConfig.user.projectAdmin) {
			 btnSaveAsStandardLayout=Ext.create("Ext.button.Button",{
					text:getText('itemov.btn.saveAsStandardLayout'),
					overflowText:getText('itemov.btn.saveAsStandardLayout'),
					tooltip:getText('itemov.btn.saveAsStandardLayout'),
					iconCls: me.descriptor.iconCls,
					handler:function(){
						me.saveAsStandardLayout.call(me);
					}
				});
			 }
		 var btnUseStandardLayout=Ext.create("Ext.button.Button",{
				text:getText('itemov.btn.useStandardLayout'),
				overflowText:getText('itemov.btn.useStandardLayout'),
				tooltip:getText('itemov.btn.useStandardLayout'),
				iconCls: me.descriptor.iconCls,
				handler:function(){
					me.useStandardLayout.call(me);
				}
			});
		 var btnSaveAsFilterLayout=Ext.create("Ext.button.Button",{
				text:getText('itemov.btn.saveAsFilterLayout'),
				overflowText:getText('itemov.btn.saveAsFilterLayout'),
				tooltip:getText('itemov.btn.saveAsFilterLayout'),
				iconCls: me.descriptor.iconCls,
				handler:function(){
					me.saveAsFilterLayout.call(me);
				}
			});

		 	if (me.model.isFilterView===true) {
		 		if (btnSaveAsStandardLayout) {
		 			btnSaveAsStandardLayout.setVisible(false);
		 		}
		 		btnUseStandardLayout.setVisible(false);
		 		var maySaveFilterLayout=me.model.maySaveFilterLayout;
				btnSaveAsFilterLayout.setVisible(maySaveFilterLayout/*true*/);
				/*if(maySaveFilterLayout===true){
					//only save as filter
					btnSaveAsFilterLayout.setDisabled(false);
				} else {
					//nothing
					btnSaveAsFilterLayout.setDisabled(true);
				}*/
			} else {
				btnSaveAsFilterLayout.setVisible(false);
				if (btnSaveAsStandardLayout) {
		 			btnSaveAsStandardLayout.setVisible(true);
		 		}
				btnUseStandardLayout.setVisible(true);
			}
		me.settingsPanel=me.listViewPlugin.createSettingsPanel(btnGroup, btnChooseColumns, btnSaveAsStandardLayout, btnUseStandardLayout, btnSaveAsFilterLayout);
		Ext.apply(me.settingsPanel, {
			region:'north',
			hidden:!settingsVisible
		});
		me.settingsHeight= me.listViewPlugin.getSettingsHeight();
		me.view=Ext.create('Ext.panel.Panel',{
			layout:'border',
			border:false,
			bodyBorder:false,
			items:[me.settingsPanel,me.listView]
		});
		return me.view;
	},

	useStandardLayout:function(){
		var me=this;
		var urlStr="itemNavigator!useStandardLayout.action";
		me.itemNavigatorController.refresh.call(me.itemNavigatorController,urlStr,null,true);
		//me.refresh(urlStr,null,true);
	},

	saveAsStandardLayout:function(){
		var urlStr="itemNavigator!saveAsStandardLayout.action";
		borderLayout.setLoading(true);
		Ext.Ajax.request({
			url: urlStr,
			disableCaching:true,
			success: function(result){
				borderLayout.setLoading(false);
				CWHF.showMsgInfo(getText('itemov.msg.saveAsStandardLayoutSuccess'));
			},
			failure: function(){
				borderLayout.setLoading(false);
				CWHF.showMsgError('Failure');
			},
			method:'POST'
		});
	},

	saveAsFilterLayout:function(){
		var urlStr="itemNavigator!saveAsFilterLayout.action";
		borderLayout.setLoading(true);
		Ext.Ajax.request({
			url: urlStr,
			disableCaching:true,
			success: function(result){
				borderLayout.setLoading(false);
				CWHF.showMsgInfo(getText('itemov.msg.saveAsFilterLayoutSuccess'));
			},
			failure: function(){
				borderLayout.setLoading(false);
				CWHF.showMsgError('Failure');
			},
			method:'POST'
		});
	},

	viewContainsFields:function(fields){
		var me=this;
		if(me.listViewPlugin){
			return me.listViewPlugin.containsFields(fields);
		}
		return false;
	},
	getView:function(model){
		var me=this;
		if(model){
			me.model=model;
		}
		if(CWHF.isNull(me.view)){
			me.createView(me.model);
		}
		return me.view;
	},
	addViewPluginListeners:function(){
		var me=this;
		me.listViewPlugin.addListener('datachange',me.onDataChange,me);
		me.listViewPlugin.addListener('layoutchange',me.onLayoutChange,me);
		me.listViewPlugin.addListener('sortchange',me.onSortChange,me);
		me.listViewPlugin.addListener('itemcontextmenu',me.onItemContextMenu,me);
		me.listViewPlugin.addListener('itemdblclick',me.onItemDblClick,me);
		me.listViewPlugin.addListener('selectionchange',me.onSelectionChange,me);
		me.listViewPlugin.addListener('totalChange',me.onTotalChange,me);
		me.listViewPlugin.addListener('startDragItems',me.onStartDragItems,me);
		me.listViewPlugin.addListener('afterDropItems',me.onAfterDropItems,me);
	},
	onDataChange:function(opts){
		var me=this;
		//urlStr,queryContextID,createView,
		me.itemNavigatorController.refresh.call(me.itemNavigatorController,null,null,false,function(){
			//here the scope is ItemNavigatorController
			var listViewPlugin=this.issueListFacade.listViewPlugin;
			listViewPlugin.dataChangeSuccess.call(listViewPlugin,opts);
		});
	},
	onLayoutChange:function(){
		var me=this;
		me.itemNavigatorController.changeLayout.call(me.itemNavigatorController);
	},
	onSortChange:function(sort){
		var me=this;
		me.itemNavigatorController.sortChange.call(me.itemNavigatorController,sort)
	},

	onSelectionChange:function(selections){
		var me=this;
		me.itemNavigatorController.issueSelectionChange.call(me.itemNavigatorController,selections);
	},
	onItemContextMenu:function(data,event,grid,index,record){
		var me=this;
		var workItemID=data.workItemID;
		if(workItemID&&workItemID!==-1){
			var popupMenu=me.createPopup.call(me,data,grid,index,record);
			if(popupMenu){
				popupMenu.showAt(event.getXY());
				me.listViewPlugin.selectItem(workItemID);
			}
		}
	},
	onItemDblClick:function(data,animateTarget){
		var me=this;

		if(me.listViewPlugin.$className !== 'com.trackplus.itemNavigator.GanttViewPlugin') {
			me.openEditDlg(data, animateTarget);
		}else {
			if(!me.listViewPlugin.isNewSelectedTask()) {
				if(me.listViewPlugin.ganttController.isChartContainsUnsavedItems()) {
					me.listViewPlugin.ganttController.showSaveDialogWarningIfChartContainsUnsavedItems(me, me.openEditDlg, null, [data, animateTarget]);
				}else {
					me.openEditDlg(data, animateTarget);
				}
			}
		}
	},

	openEditDlg:function(data,animateTarget){
		var me = this;
		var workItemID=data.workItemID;
		if(workItemID&&workItemID!==-1){
			var workItemIndex=data['workItemIndex'];
			var actionID=-2;//PRINT
			me.listViewPlugin.selectItem(workItemID);
			me.itemNavigatorController.executeItemAction.call(me.itemNavigatorController,workItemID,actionID,null,workItemIndex/*,animateTarget*/);
		}
	},

	onTotalChange:function(totalCount,count){
		var me=this;
		me.itemNavigatorController.model.totalCount=totalCount;
		me.itemNavigatorController.model.count=count;
		me.itemNavigatorController.updateTotalCount();
	},
	onStartDragItems:function(workItems,selectedFieldIDs,extraParams){
		var me=this;
		var urlStr="possibleFieldOptions.action";
		if(CWHF.isNull(selectedFieldIDs)){
			selectedFieldIDs=new Array();
		}
		if(Ext.Array.indexOf(selectedFieldIDs,4)===-1){
			selectedFieldIDs.push(4);//STATE
		}
		if(me.itemNavigatorController.filterController.subFilterVisible===true){
			/*if(Ext.Array.indexOf(selectedFieldIDs,2)===-1){
				selectedFieldIDs.push(2);//ISSUETYPE
			}*/
			if(Ext.Array.indexOf(selectedFieldIDs,10)===-1){
				selectedFieldIDs.push(10);//PRIORITY
			}
		}
		var params={
			selectedItemIDs:workItems,
			selectedFieldIDs:selectedFieldIDs.join(",")
		};
		Ext.Ajax.request({
			disableCaching:true,
			url:urlStr,
			params:params,
			success: function(result){
				var jsonData=Ext.decode(result.responseText);
				me.listViewPlugin.updatePossibleFieldOptions(jsonData,extraParams);
				me.itemNavigatorController.filterController.updatePossibleFieldOptions(jsonData);
			},
			failure: function(type, error){
			}
		});
	},
	onAfterDropItems:function(){
		var me=this;
		me.itemNavigatorController.filterController.onAfterDropItems();

	},
	refreshData:function(issues){
		var me=this;
		if(CWHF.isNull(me.listViewPlugin)){
			return null;
		}
		me.listViewPlugin.refreshData.call(me.listViewPlugin,issues);
	},
	setVisibleTools:function(b){
		var me=this;
		var toolPanel=me.settingsPanel;
		if(toolPanel){
			var el=toolPanel.getEl();
			var settingsHeight=me.settingsHeight;
			var gridEl=me.listView.getEl();
			var gridY=gridEl.getY();
			var gridHeight=gridEl.getHeight();
			if(b){
				gridEl.animate({
					top:settingsHeight,
					height:gridHeight-settingsHeight,
					duration:300,
					listeners:{
						'afteranimate':function(){
							toolPanel.setVisible(true);
							el.slideIn('t', {
								easing: 'easeOut',
								duration: 300,
								//remove: false,
								//useDisplay: false,
								listeners:{
									'afteranimate':function(){
										me.view.ownerCt.updateLayout();
									}
								}
							});
						}
					}
				});
			}else{
				el.slideOut('t', {
					easing: 'easeOut',
					duration: 300,
					listeners:{
						'afteranimate':function(){
							gridEl.animate({
								top:0,
								height:gridHeight+settingsHeight,
								duration:300,
								listeners:{
									'afteranimate':function(){
										toolPanel.setVisible(false);
										me.view.ownerCt.updateLayout();
									}
								}
							});
						}
					}
				});
			}
		}
	},
	//For Gantt
	addSaveButton: function(toolbarItemsArr) {
		var me = this;
		if(me.listViewPlugin){
			if(me.listViewPlugin.$className === 'com.trackplus.itemNavigator.GanttViewPlugin') {
				me.listViewPlugin.ganttController.addSaveButton();

			}
		}
    },
	//For Gantt
    addOrRemoveSaveButton: function(toolbarItems) {
    	var me = this;
    	if(me.listViewPlugin ){
			if(me.listViewPlugin.$className === 'com.trackplus.itemNavigator.GanttViewPlugin') {
				var items = borderLayout.getActiveToolbarList().items.items;
				var toolbar = borderLayout.getActiveToolbarList();
				for(var ind in items) {
					if(items[ind].itemId ) {
						if(items[ind].itemId === 'ganttSaveBtn') {
							toolbar.remove(items[ind]);
						}
					}
				}
				me.addSaveButton(toolbarItems);
			}else{
				var toolbar = borderLayout.getActiveToolbarList();
				var items = borderLayout.getActiveToolbarList().items.items;
				for(var ind in items) {
					if(items[ind].itemId ) {
						if(items[ind].itemId === 'ganttSaveBtn') {
							toolbar.remove(items[ind]);
						}
					}
				}
			}
		}
    },

	getSelectedIssues:function(){
		var me=this;
		return me.listViewPlugin.getSelectedIssues();
	},
	navigate:function(workItemID,workItemIndex,dir){
		var me=this;
		return me.listViewPlugin.navigate.call(me.listViewPlugin,workItemID,workItemIndex,dir);
	},
	selectItemByIndex:function(workItemIndex){
		var me=this;
		return me.listViewPlugin.selectItemByIndex.call(me.listViewPlugin,workItemIndex);
	},
	selectItem:function(workItemID){
		var me=this;
		if(me.listViewPlugin){
			return me.listViewPlugin.selectItem.call(me.listViewPlugin,workItemID);
		}
		return null;
	},
	deselectItem:function(workItemID){
		var me=this;
		return me.listViewPlugin.deselectItem.call(me.listViewPlugin,workItemID);
	},

	createPopup:function(rowData,grid,index,record){
		var me=this;
		if(me.popupMenu){
			me.popupMenu.destroy();
			me.popupMenu=null;
			delete me.popupMenu;
		}
		me.popupMenu = Ext.create('Ext.menu.Menu',{
			floating:true,
			items:me.getPopupMenuItems.call(me,rowData,grid,index,record),
			closeAction:'destroy'
		});
		return me.popupMenu;
	},
	getPopupMenuItems:function(rowData,grid,index,record){
		var me=this;
		var workItemID=rowData.workItemID;
		var workItemIndex=rowData.workItemIndex;
		var projectID=rowData.projectID;
		var issueTypeID=rowData.issueTypeID;
		var originatorID=rowData.originatorID;
		var parentID=rowData.parentID;
		var accessLevelFlag=rowData.accessLevelFlag;
		var items=[];
		var editable=rowData["editable"];
		var linkable=rowData["linkable"];
		var notEditableFields=rowData["notEditableFields"];
		var leaf = rowData["leaf"];
		var summaryItemsBehavior = true;
		if (me.model) {
			summaryItemsBehavior = me.model.summaryItemsBehavior;
		}
		var addExpense=(leaf || !summaryItemsBehavior)  && editable;
		if(addExpense && notEditableFields && notEditableFields.length>0){
			//public static final int MY_EXPENSE_COST = -1200;
			//public static final int MY_EXPENSE_TIME = -1201;
			var idx_MY_EXPENSE_COST=Ext.Array.indexOf(notEditableFields,-1200);
			var idx_MY_EXPENSE_TIME=Ext.Array.indexOf(notEditableFields,-1201);
			addExpense=(idx_MY_EXPENSE_COST===-1&&idx_MY_EXPENSE_TIME===-1);
		}
		if(editable){
			items.push({
				text: getText('common.btn.edit'),
				iconCls:'itemAction_edit16',
				handler: function(){
					me.taskEditHandler(workItemID, workItemIndex);
				}
			});
		}
		items.push({
			text: getText('item.action.viewInNewTab'),
			iconCls:'itemAction_viewAll16',
			handler: function(){
				var urlStr='printItem.action?key='+workItemID;
				window.open(urlStr,'printItem'+workItemID);
			}
		});
		items.push({
			text: getText('common.btn.copy'),
			iconCls:'itemAction_copy16',
			handler: function(){
				me.itemNavigatorController.executeItemAction.call(me.itemNavigatorController,workItemID,-1,null,workItemIndex);
			}
		});
		if(editable){
			items.push({
				text: getText('common.btn.move'),
				iconCls:'itemAction_move16',
				handler:function(){
					me.itemNavigatorController.executeItemAction.call(me.itemNavigatorController,workItemID,3,null,workItemIndex);
				}
			});
			items.push({
				text: getText('common.btn.changeStatus'),
				iconCls:'itemAction_changeStatus16',
				handler:function(){
					me.itemNavigatorController.executeItemAction.call(me.itemNavigatorController,workItemID,5,null,workItemIndex);
				}
			});
		}
		items.push({
			text: getText('common.btn.addChild'),
			iconCls:'itemAction_addChild16',
			handler:function(){
				me.itemNavigatorController.executeItemAction.call(me.itemNavigatorController,workItemID,4,null,workItemIndex,null,null,true);
			}
		});
		if(editable){
			items.push({
				text: getText('common.btn.chooseParent'),
				iconCls:'itemAction_parent16',
				handler:function(){
					me.itemNavigatorController.chooseParent.call(me.itemNavigatorController,rowData);
				}
			});
			if(parentID){
				items.push({
					text: getText('common.btn.removeParent'),
					iconCls:'itemAction_removeParent16',
					handler:function(){
						me.itemNavigatorController.executeAJAXItemAction.call(me.itemNavigatorController,'itemNavigator!removeParent.action?workItemID='+workItemID);
					}
				});
			}
		}
		if (editable && linkable) {
			items.push({
				text: getText('common.btn.addLinkedItem'),
				iconCls:'links16',
				handler:function(){
					/*addLinkFromContextMenu = {};
					addLinkFromContextMenu.projectID=rowData.projectID;
					addLinkFromContextMenu.issueTypeID=rowData.issueTypeID;*/
					addLinkFromContextMenu = true;
					me.itemNavigatorController.executeItemAction.call(me.itemNavigatorController,workItemID,6,null,workItemIndex,null,null,true,addLinkFromContextMenu);
				}
			});
		}
		if(originatorID===com.trackplus.TrackplusConfig.userID){
			items.push({
				text: accessLevelFlag?getText('common.btn.unlock'):getText('common.btn.lock'),
				iconCls:'itemAction_lock16',
				handler:function(){
					me.executeAJAX('item!reverseAccessFlag.action?workItemID='+workItemID);
				}
			});
		}
		if(addExpense){
			items.push({
				text: getText('common.btn.addExpense'),
				iconCls:'cost16',
				handler:function(){
					me.addWorklog(workItemID,projectID,issueTypeID);
				}
			});
		}
		items.push({
			text: getText('common.btn.report'),
			iconCls:'itemAction_report16',
			handler:function(){
				//me.executeURL('reportConfig.action?fromIssueNavigator=true&workItemID='+workItemID);
				com.trackplus.admin.Report.executeReportFromIssueNavigator(workItemID);
			}
		});
		var pluginMenuItems=me.listViewPlugin.getPopupMenuItems.call(me.listViewPlugin,rowData,grid,index,record);
		if(pluginMenuItems){
			for(var i=0;i<pluginMenuItems.length;i++){
				items.push(pluginMenuItems[i]);
			}
		}
		return items;
	},
	taskEditHandler: function(workItemID, workItemIndex) {
		var me = this;
		if(me.listViewPlugin.$className !== 'com.trackplus.itemNavigator.GanttViewPlugin') {
			me.itemNavigatorController.executeItemAction.call(me.itemNavigatorController,workItemID,2,null,workItemIndex);
		}else{
			if(!me.listViewPlugin.isNewSelectedTask()) {
				if(me.listViewPlugin.ganttController.isChartContainsUnsavedItems()) {
					me.listViewPlugin.ganttController.showSaveDialogWarningIfChartContainsUnsavedItems(me, me.taskEditHandler, null, [workItemID, workItemIndex]);
				}else {
					me.itemNavigatorController.executeItemAction.call(me.itemNavigatorController,workItemID,2,null,workItemIndex);
				}
			}
		}
	},

	addWorklog:function(workItemID,projectID,issueTypeID){
		var me=this;
		var worklogController=Ext.create('com.aurel.trackplus.itemDetail.WorklogController',{
			workItemID:workItemID,
			projectID:projectID,
			issueTypeID:issueTypeID,
			jsonData:{},
			refresh: me.itemNavigatorController.refresh,
			refreshScope:me.itemNavigatorController
		});
		worklogController.openWorklog.call(worklogController,getText("item.action.edit.title")+"&nbsp;"+workItemID+"&nbsp;"+getText("item.tabs.expense.editExpense.title.add"),null);
		/*worklogController.addModifyExpense.call(worklogController, me.itemNavigatorController,
				getText("item.action.edit.title")+"&nbsp;"+workItemID+"&nbsp;"+getText("item.tabs.expense.editExpense.title.add"),null);*/
	},
	executeAJAX:function(urlStr){
		var me=this;
		Ext.Ajax.request({
			url: urlStr,
			success: function(response){
				me.itemNavigatorController.refresh.call(me.itemNavigatorController);
			},
			failure:function(){
				alert("failure");
			}
		});
	},
	executeURL:function(urlStr){
		window.location.href=urlStr;
	}
});


Ext.define('com.trackplus.itemNavigator.CheckboxModel',{
	extend: 'Ext.selection.CheckboxModel',
	updateHeaderState: function() {
		var me = this,
			store = me.store,
			storeCount = store.getCount(),
			views = me.views,
			hdSelectStatus = false,
			selectedCount = 0,
			selected, len, i;
		var selectedGroups=0;
		var items = me.store.getRange();
		for(var i=0;i<items.length;i++){
			if(items[i].get('group')===true){
				selectedGroups++;
			}
		}

		if (!store.buffered && storeCount > 0) {
			selected = me.selected;
			hdSelectStatus = true;
			for (i = 0, len = selected.getCount(); i < len; ++i) {
				/*if (!me.storeHasSelected(selected.getAt(i))) {
				 break;
				 }*/
				++selectedCount;
			}
			hdSelectStatus = storeCount === selectedCount+ selectedGroups;
		}

		if (views && views.length) {
			me.toggleUiHeader(hdSelectStatus);
		}
	},
	deselectAll:function(){
		var me=this;
		//this.selectedGroups=0;
		me.callParent(arguments);
	},

	listeners:{
		beforeselect:function(selModel, record, index) {
			return record.get('group') !== true;
		},
		beforedeselect:function(selModel, record, index) {
			return record.get('group') !== true;
		}
	}
});


