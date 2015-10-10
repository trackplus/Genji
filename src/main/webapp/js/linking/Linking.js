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



Ext.define('com.trackplus.linking.LinkingController', {
	extend: 'Ext.Base',
	config: {
		model: {}
	},
	view: null,
	constructor: function (config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	},
	createView: function () {
		var me = this;
		if (me.view == null) {
			me.view=Ext.create('com.trackplus.linking.LinkingView',{
				model:me.model
			});
		}
		return me.view;
	}
});

Ext.define('com.trackplus.linking.LinkingView', {
	extend: 'Ext.panel.Panel',
	config: {
		model: {}
	},
	border: false,
	layout: 'border',
	cls:'linking',
	northPanel:null,
	centerPanel:null,
	readOnly:false,
	initComponent: function () {
		var me = this;
		me.items = me.createChildren();
		me.callParent();
	},

	createChildren: function () {
		var me = this;
		me.createNorthPanel();
		me.centerPanel=Ext.create('Ext.panel.Panel',{
			region:'center',
			layout:'fit',
			html:'....'
		});
		return [me.northPanel,me.centerPanel];
	},
	createNorthPanel:function(){
		var me=this;
		var columnsQueryID=me.model['columnsQueryID'];
		var rowsQueryID=me.model['rowsQueryID'];
		var linkTypeWithDirection=me.model['linkTypeWithDirection'];

		me.cmbColumnsQuery=me.createFilterPickerCfg("linking.lbl.verticalQuery",'columnsQueryID',columnsQueryID);
		me.cmbRowsQuery=me.createFilterPickerCfg("linking.lbl.horizontalQuery",'rowsQueryID',rowsQueryID);
		me.cmbLinkType = CWHF.createCombo("linking.lbl.linkType", "linkTypeWithDirection",
			{labelWidth:150, width:500, idType:"string"});
		me.cmbLinkType.store.loadData(me.model["linkTypesList"],false);

		me.cmbLinkType.setValue(linkTypeWithDirection);

		me.btnApply=Ext.create('Ext.button.Button', {
			overflowText: getText('linking.lbl.search'),
			text:getText('linking.lbl.search'),
			margin:'5 5 5 155',
			//iconCls: 'itemAction_downward16',
			//cls:'toolbarItemAction-noText',
			handler: me.applyHandler,
			scope: me
		});
		me.northPanel=Ext.create('Ext.panel.Panel',{
			layout:'anchor',
			region:'north',
			border:false,
			margin:'10 0 5 0',
			bodyBorder:false,
			items:[me.cmbRowsQuery,me.cmbColumnsQuery,me.cmbLinkType,me.btnApply]
		});
	},
	applyHandler:function(){
		var me=this;
		var columnsQueryID=me.cmbColumnsQuery.getValue();
		var rowsQueryID=me.cmbRowsQuery.getValue();
		var linkTypeWithDirection=me.cmbLinkType.getValue();
		if(columnsQueryID==null||rowsQueryID==null){
			return false;
		}
		var urlStr="linking!search.action";
		borderLayout.setLoading(true);
		me.linkTypeWithDirection=linkTypeWithDirection;
		Ext.Ajax.request({
			url: urlStr,
			params:{
				columnsQueryID:columnsQueryID,
				rowsQueryID:rowsQueryID,
				linkTypeWithDirection:linkTypeWithDirection
			},
			disableCaching:true,
			success: function(response){
				var dataAJAX=Ext.decode(response.responseText);
				me.emptyCols=null;
				if(dataAJAX.success==true){
					var itemsRows=dataAJAX.data.itemsRows
					var itemsColumns=dataAJAX.data.itemsColumns;
					var links=dataAJAX.data.links;
					var linksDescription=dataAJAX.data.linksDescription;
					var data=new Array();
					var emptyRows=new Array();
					var emptyCols=new Array();
					me.readOnly=dataAJAX.data.readOnly;
					for(var i=0;i<itemsRows.length;i++){
						var row=new Object();
						row['f']=itemsRows[i].title;
						row['id']=itemsRows[i].id;
						row['workItemID']=itemsRows[i].objectID;
						var hasLinks=false;
						if(links!=null) {
							for (var j = 0; j < itemsColumns.length; j++) {
								var rowData = '';
								var key = itemsRows[i].objectID + '_' + itemsColumns[j].objectID;
								if (links[key] != null) {
									rowData = links[key];
									hasLinks=true;
								}
								row['f' + itemsColumns[j].objectID] = rowData;
								var found=false;
								for(var x in links){
									if(x.indexOf('_'+itemsColumns[j].objectID)!=-1){
										found=true;
									}
								}
								if(found==false){
									emptyCols.push(j);
								}
							}
						}
						if(hasLinks==false){
							emptyRows.push(itemsRows[i].objectID);
						}
						data.push(row);
					}
					var model={
						itemsColumns:itemsColumns,
						itemsRows:itemsRows,
						linksDescription:linksDescription,
						data:data
					};
					me.emptyRows=emptyRows;
					me.emptyCols=emptyCols;
					me.createGrid(model);
					me.centerPanel.removeAll(true);
					me.centerPanel.add(me.gridView);
					borderLayout.setLoading(false);
				}else{
					me.centerPanel.removeAll(true);
					var  message=dataAJAX.errorMessage;
					if(message=='tooManyItems'){
						message=getText('itemov.err.tooManyItems');
					}
					var pan=Ext.create('Ext.Component',{
						html:message,
						border:false,
						cls:'infoBox1'
					});
					me.centerPanel.add(pan);
					borderLayout.setLoading(false);


				}
			},
			failure: function(){
				borderLayout.setLoading(false);
				CWHF.showMsgError('Failure');
			},
			method:'POST'
		});
	},
	createGrid:function(model){
		var me=this;
		var itemsColumns=model.itemsColumns;
		me.itemsColumns=itemsColumns;
		var fields=me.createFields(itemsColumns);
		var store=Ext.create('Ext.data.Store',{
			fields:fields,
			data:model.data
		});
		me.gridView=Ext.create('Ext.grid.Panel',{
			margins: '0 0 0 0',
			border:false,
			bodyBorder:false,
			columnLines :true,
			cls:'simpleGridView gridNoBorder',
			viewConfig: {
				stripeRows: true
			},
			store:store,
			columns:me.createColumnModel(model),
			plugins:[Ext.create('GeneralLearning.ux.VerticalHeader',{})]
		});
		me.gridView.addListener('cellcontextmenu',me.onCellContextMenu,me);
		me.gridView.addListener('celldblclick',me.onItemDblClick,me);
		return me.gridView;
	},
	createFields:function(itemsColumns){
		var me=this;
		var fields=new Array();
		fields.push({name:'f'});
		fields.push({name:'id'});
		fields.push({name:'workItemID',type:'int',useNull:true});
		if(itemsColumns!=null){
			for(var i=0;i<itemsColumns.length;i++){
				fields.push({name:'f'+itemsColumns[i].objectID});
			}
		}
		return fields;
	},
	createColumnModel:function(model){
		var me=this;
		var itemsColumns=model.itemsColumns;
		var columnModel=new Array();
		columnModel.push(Ext.create('Ext.grid.column.Column',{
			header:'',
			draggable :false,
			menuDisabled:true,
			sortable:false,
			width:200,
			dataIndex:'f',
			locked:true,
			resizable:true,
			renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
				var tooltip=record.data.id+"<b> : </b>"+record.data.f;
				metaData.tdAttr = 'data-qtip="' + tooltip+ '"';
				return value;
			}

		}));
		for(var i=0;i<itemsColumns.length;i++){
			var item=itemsColumns[i];
			var header=item.title;
			if(header.length>17){
				header=header.substring(0,17)+"...";
			}
			var col=Ext.create('Ext.grid.column.Column',{
				header:header,
				tooltip :item.id+"<B> : </B>"+ item.title,
				draggable :false,
				menuDisabled:true,
				sortable:false,
				width:25,
				dataIndex:'f'+item.objectID,
				resizable:false,
				renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
					if(colIndex==0){
						if(me.emptyRows!=null){
							var workItemID=record.data.workItemID;
							if(Ext.Array.contains(me.emptyRows, workItemID)){
								metaData.tdCls = 'linkingEmptyRow';
							}
						}
					}
					if(rowIndex==0){
						if(me.emptyCols!=null){
							if(Ext.Array.contains(me.emptyCols, colIndex)){
								metaData.tdCls =metaData.tdCls+ ' linkingEmptyCol';
							}
						}
					}
					if(value!=null&&value!='') {
						var imgName = "arrow_bent.png";
						var tooltip=model.linksDescription[value];
						if(tooltip!=null){
							metaData.tdAttr = 'data-qtip="' + tooltip+ '"';
						}
						return '<img src="' + com.trackplus.TrackplusConfig.icon16Path + imgName + '">';
					}
					return value;
				}
			});
			columnModel.push(col);
		}
		return columnModel;
	},
	onCellContextMenu:function(gridView,td, cellIndex, record, tr, rowIndex, e, eOpts){
		var me=this;
		var workItemID=record.data.workItemID;
		if(workItemID!=null&&workItemID!=-1){
			me.lastTDOriginaClass=td.className;
			td.className = td.className + " linkCellFocus";
			me.lastTD=td;
			var popupMenu=me.createPopupCell(record.data,gridView,cellIndex);
			if(popupMenu!=null){
				popupMenu.addListener('hide',function(){
					me.lastTD.className=me.lastTDOriginaClass;
				})
				popupMenu.showAt(e.getXY());
			}
		}
	},
	onItemDblClick:function(view, td,cellIndex,record, tr, rowIndex,e){
		var me=this;
		var workItemID=record.data.workItemID;
		if(workItemID!=null&&workItemID!=-1){
			var me=this;
			var actionID=-2;//PRINT
			var itemAction=Ext.create('com.trackplus.item.ItemActionDialog',{
				workItemID:workItemID,
				actionID:actionID,
				parentID:null,
				//successHandler:me.itemActionSuccessHandler,
				//scope:me,
				modal:false/*,
				navigatorHandler:me.navigatorHandler,
				navigatorScope:me*/
			});
			itemAction.execute.call(itemAction);
		}
	},
	createPopupCell:function(rowData,grid,cellIndex){
		var me=this;
		if(me.popupMenu!=null){
			me.popupMenu.destroy();
			me.popupMenu=null;
			delete me.popupMenu;
		}
		me.popupMenu = Ext.create('Ext.menu.Menu',{
			floating:true,
			items:me.getPopupMenuItems.call(me,rowData,grid,cellIndex),
			closeAction:'destroy'
		});
		return me.popupMenu;
	},
	getPopupMenuItems:function(rowData,grid,cellIndex){
		var me=this;
		var workItemID=rowData.workItemID;
		var targetWorkItemID=me.itemsColumns[cellIndex].objectID;
		var linkData=rowData['f'+targetWorkItemID];
		var items=[];
		var editable=rowData["editable"];
		var linkable=rowData["linkable"];
		if(!me.readOnly){
			if(linkData==null||linkData=='') {
				items.push({
					text: getText('common.btn.addLink'),
					iconCls: 'links16',
					handler: function () {
						borderLayout.setLoading(true);
						var urlStr = 'itemLink!saveItemLink.action';
						var params = {
							linkTypeWithDirection: me.linkTypeWithDirection,
							linkedWorkItemID: targetWorkItemID,
							workItemID: workItemID
						}
						Ext.Ajax.request({
							url: urlStr,
							params: params,
							success: function (response) {
								borderLayout.setLoading(false);
								var data=Ext.decode(response.responseText);
								if(data.success==true){
									me.applyHandler();
								} else {
									var errors = data.errors;
									if (errors!=null) {
										linkedWorkItemTitle = errors.linkedWorkItemTitle;
										if (linkedWorkItemTitle!=null) {
											Ext.MessageBox.show({
												title: getText('common.err.failure'),
												msg: linkedWorkItemTitle,
												buttons: Ext.Msg.OK,
												icon: Ext.MessageBox.ERROR
											});
										}
									}
								}
							},
							failure: function (response) {
								borderLayout.setLoading(false);
							}
						});

					}
				});
			}else{
				items.push({
					text: getText('common.btn.delete'),
					iconCls: 'delete16',
					handler: function () {
						borderLayout.setLoading(true);
						var urlStr = 'itemLink!deleteLinks.action';
						var params = {
							deletedItems:linkData,
							workItemID: workItemID
						}
						Ext.Ajax.request({
							url: urlStr,
							params: params,
							success: function (response) {
								borderLayout.setLoading(false);
								me.applyHandler();
							},
							failure: function (response) {
								borderLayout.setLoading(false);
							}
						});

					}
				});
			}
			items.push('-');
		}

		items.push({
			text: getText('item.action.viewInNewTab'),
			iconCls:'itemAction_viewAll16',
			handler: function(){
				var urlStr='printItem.action?key='+workItemID;
				window.open(urlStr,'printItem'+workItemID);
			}
		});
		return items;
	},

	createFilterPickerCfg:function(label,name,queryID,extraCfg){
		var cfg={
			allowBlank:false,
			labelWidth:150,
			labelAlign:'right',
			//width:300
			width:500,
			margin: '0 0 5 0'
		};
		if(extraCfg!=null){
			for (var propertyName in extraCfg) {
				cfg[propertyName] = extraCfg[propertyName];
			}
		}
		var picker = CWHF.createSingleTreePicker(label,name, [], queryID, cfg);
		Ext.Ajax.request({
			url: "categoryPicker.action",
			params: {node:"issueFilter"},
			success: function(response){
				var filterTree = Ext.decode(response.responseText);
				picker.updateData(filterTree);
				picker.setValue(queryID);
			},
			failure: function(response){
				com.trackplus.util.requestFailureHandler(response);
			}
		});
		return picker;
	}
});


Ext.define('com.trackplus.layout.LinkingLayout',{
	extend:'com.trackplus.layout.BaseLayout',
	useToolbar:false,
	selectedGroup:'linking',
	constructor : function(config) {
		var me = this;
		me.callParent(arguments);
		me.borderLayoutController.setHelpContext("linking");
	},
	createCenterPanel:function(){
		var me=this;
		var  linkingController=Ext.create('com.trackplus.linking.LinkingController',{
			model:me.initData
		});
		return linkingController.createView();
	}
});
