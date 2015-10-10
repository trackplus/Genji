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

//Grid tab class
Ext.define('com.aurel.trackplus.itemDetail.TabGrid',{
	extend:'com.aurel.trackplus.itemDetail.Tab',
	//layout:'border',
	config: {
		urlDeleteItems:null,
		handlerDeleteItem:null
	},
	gridConfig:null,
	gridLayoutData:null,
	grid:null,
	fieldsToNotifyOnDelete:null,
	COLUMN_TYPE:{
		STRING:0,
		DATETIME:1,
		INTEGER:2,
		DOUBLE:3,
		BYTES:4,
		IMAGE:5,
		DATE:6
	},
	createGrid:function(gridConfig,gridLayoutData,items){
		var me=this;

		var sortField=gridLayoutData.sortField;
		var sortDirection=gridLayoutData.sortDirection;
		var columnModel=new Array(0);
		var layoutData;
		var groupField=null;
		for(var i=0;i<gridLayoutData.layout.length;i++){
			layoutData=gridLayoutData.layout[i];
			colCfg={
				header:layoutData.header,
				dataIndex:layoutData.dataIndex,
				itemId:gridConfig.id+'_'+layoutData.id,
				sortable:layoutData.sortable,
				hidden:layoutData.hidden
			};
			//if(layoutData.flex!=null){
			//if(i==gridLayoutData.layout.length-1){
			//colCfg['flex']=1;
			//}else{
			colCfg['width']=layoutData.width;
			//}
			var colClassName= 'Ext.grid.column.Column';

			colClassName=me.gridConfig.updateColCfg.call(me.gridConfig,colCfg,layoutData,i);
			var col=Ext.create(colClassName,colCfg);
			if(layoutData.type==me.COLUMN_TYPE.DATETIME){
				col.renderer=Ext.util.Format.dateRenderer(com.trackplus.TrackplusConfig.DateTimeFormat);
			}
			if(layoutData.type==me.COLUMN_TYPE.DATE){
				col.renderer=Ext.util.Format.dateRenderer(com.trackplus.TrackplusConfig.DateFormat);
			}
			if(layoutData.type==me.COLUMN_TYPE.BYTES){
				col.renderer=me.formatBytes;
			}
			if(layoutData.type==me.COLUMN_TYPE.IMAGE){
				col.renderer=me.thumbnailRenderer(me.workItemID);
			}
			if(layoutData.dataIndex=='newValues'||layoutData.dataIndex=='oldValues'||
				layoutData.dataIndex=='comment'||layoutData.dataIndex=='diff'){
				col.renderer=function(value,metaData, record, rowIndex, colIndex, store) {
					if(this.isHidden()==true){
						metaData.tdCls = '';
						return '';
					}else{
						metaData.tdCls = 'wordWrapTd';
					}
					return value;
				};
				col.scope=col;
			}
			me.gridConfig.updateCol.call(me.gridConfig,col,layoutData,i);
			columnModel.push(col);
			if(layoutData.grouping){
				groupField=layoutData.dataIndex;
			}
		}
		var store = Ext.create('Ext.data.Store', {
			groupField:groupField,
			sortInfo:{field:gridConfig.sortField, direction:gridConfig.sortDirection},
			fields:gridConfig.fields,
			proxy: {
				type: 'ajax',
				url: gridConfig.urlStore,
				reader: {
					type: 'json'
				}
			}
		});
		if(items!=null){
			store.loadData(items);
		}

		var groupingFeature = Ext.create('Ext.grid.feature.Grouping',{
			groupHeaderTpl: '{name} ({rows.length} Item{[values.rows.length > 1 ? "s" : ""]})'
		});
		var features=me.gridConfig.features;
		if(features==null){
			features=[];
		}
		features.push(groupingFeature);
		// create the grid
		me.grid=  Ext.create('Ext.grid.Panel', {
			cls:'itemDetailGrid',
			store	  : store,
			selModel   : gridConfig.selectionModel,
			columns	: columnModel,
			border	: false,
			bodyBorder:false,
			columnLines :true,
			features: features,
			viewConfig: me.getViewConfig(),
			dockedItems: [{
				xtype: 'toolbar',
				dock: 'top',//'bottom'
				items:gridConfig.tbar
			}]
		});
		if(groupField==null){
			me.grid.addListener('afterrender', function(){
				groupingFeature.disable();
			});
		}
		me.grid.addListener('itemdblclick', me.gridOnClickHandler,me);
		me.grid.addListener('cellclick', me.gridOnCellClickHandler,me);

		me.grid.addListener('columnmove',me.handleColChange,me);
		me.grid.addListener('columnresize', me.handleColChange,me);
		me.grid.addListener('sortchange', me.handleColChange,me);
		me.grid.addListener('columnhide',me.handleColChange,me);
		me.grid.addListener('columnshow',me.handleColChange,me);
		//grid.view.addListener('refresh',gridConfig.groupingChangeHandler,gridConfig);
		return me.grid;
	},
	formatBytes:function(value){
		return Ext.util.Format.fileSize(value);
	},

	thumbnailRenderer:function(workItemID) {
		var me=this;
		return function(value,metaData, record, rowIndex, colIndex, store) {
			return me.formatThumbnail(value,metaData, record, rowIndex, colIndex, store,workItemID);
		};
	},
	formatThumbnail:function(value,metaData, record, rowIndex, colIndex, store,workItemID){
		var result="";
		if(value){
			result= '<div style="text-align: center;';
			if(record.data['imgHeight']!=null){
				result+=' height:'+record.data['imgHeight']+'px;'
			}
			result+='"><img src="thumbnailAttachment.action?attachKey='+record.data['id'];
			if(workItemID!=null){
				result=result+'&workItemID='+workItemID;
			}
			result=result+'"></div>';
		}else{
			result="...";
		}
		return result;
	},

	getViewConfig: function() {
		return {
			stripeRows: true
		};
	},

	handleColChange:function(ct,column){
		var me=this;
		var sortField=null;
		var sortDir=null;
		var sorters=me.grid.getStore().sorters;
		if(sorters!=null&&sorters.getCount()>0){
			var s=sorters.getAt(0);
			sortField=s.property;
			sortDir=s.direction;
		}
		var groupField=null;//me.grid.view.ds.groupField;
		var columns="";
		var gridColumns=ct.getGridColumns();
		var nr=gridColumns.length;
		var i=0;
		for(i=0;i<nr;i++){
			var col=gridColumns[i];
			var colId=null;
			var itemID=col.getItemId();
			if(itemID!=null){
				var idx=itemID.indexOf("_");
				if(idx>0){
					colId=itemID.substring(idx+1);
				}
			}
			if(colId==null){
				continue;
			}
			var colWidth=col.getWidth();
			var colDataIndex=col.dataIndex;
			var colHidden=col.isHidden();
			var direction="-";
			if(sortField!=null&&colDataIndex==sortField){
				if(sortDir=='ASC'){
					direction='Y'
				}else{
					direction='N';
				}
			}
			var grouping=false;
			if(groupField!=null&&colDataIndex==groupField){
				grouping=true;
			}
			columns=columns+colId+":"+colHidden+":"+colWidth+":"+direction+":"+grouping+";";
		}
		Ext.Ajax.request({
			url: "saveGridConfig.action",
			disableCaching:true,
			success: function(result){
			},
			failure: function(){
			},
			method:'POST',
			params:{"columns":columns,"gridID":me.gridConfig.id}
		});
	},


	initComponent : function(){
		var me=this;
		me.items=me.createChildren();
		me.callParent();
	},
	createChildren:function(){
		var me=this;
		var items=[];
		if(me.gridConfig!=null&&me.jsonData!=null&&me.jsonData.gridLayoutData!=null){
			var someGrid=me.createGrid.call(me,me.gridConfig,me.jsonData.gridLayoutData,me.jsonData.items);
			me.gridConfig.grid=someGrid;
			items.push(someGrid);
		}
		return items;
	},
	refresh:function(){
		var me=this;
		Ext.suspendLayouts();
		if(me.gridConfig.grid==null){
			Ext.Ajax.request({
				url: me.gridConfig.urlStore,
				disableCaching:true,
				success: function(response){
					var responseJson = Ext.decode(response.responseText);
					var jsonData=responseJson.data;
					var someGrid=me.createGrid.call(me,me.gridConfig,jsonData.gridLayoutData,jsonData.items);
					me.gridConfig.grid=someGrid;
					me.add(someGrid);
					var records=new Array();
					var data=someGrid.store.data;
					if(data!=null){
						for(var i=0;i<data.getCount();i++){
							records.push(data.getAt(i));
						}
					}
					me.refreshCallback.call(me,records,{},true);
					Ext.resumeLayouts(true);
				},
				failure: function(){
					Ext.resumeLayouts(true);
				},
				method:'POST',
				params:{includeLayout:true,tabID:me.gridConfig.id}
			});

		}else{
			if (!me.keepSelection) {
				me.gridConfig.grid.getSelectionModel().deselectAll();
			}
			me.gridConfig.grid.store.load({
				params:{tabID:me.gridConfig.id},
				callback : function(r,options,success){
					me.refreshCallback.call(me,r,options,success);
					Ext.resumeLayouts(true);
				}
			});
		}
	},
	replaceTitleNumber:function(title,nr){
		var p=" ";
		var idx=title.search(p);
		return title.substr(0,idx)+" ("+nr+")";
	},

	refreshCallback:function(r,options,success){
	},
	gridOnClickHandler:function(){
		var me=this;
		if(me.gridConfig.dblClickHandler!=null){
			me.gridConfig.dblClickHandler.call(me);
		}
	},
	gridOnCellClickHandler:function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts){
		var me=this;
		if(me.gridConfig.cellClickHandler!=null){
			me.gridConfig.cellClickHandler.call(me, grid, td, cellIndex, record, tr, rowIndex, e, eOpts);
		}
	},
	deleteItems:function(){
		var me=this;
		var selections=me.gridConfig.selectionModel.getSelection();
		var titleNotSelected=com.trackplus.TrackplusConfig.getText("common.lbl.messageBox.title.notSelected");
		var messageConfirmDelete=com.trackplus.TrackplusConfig.getText("common.lbl.messageBox.removeSelected.confirm");
		var messageNoSelection=com.trackplus.TrackplusConfig.getText("common.lbl.messageBox.removeSelected.notSelected");
		var titleDelete=com.trackplus.TrackplusConfig.getText("common.btn.delete");
		var okLabel=com.trackplus.TrackplusConfig.getText("common.btn.ok");
		if(selections==null||selections.length==0){
			Ext.MessageBox.show({
				title:titleNotSelected,
				msg:messageNoSelection ,
				buttons:{ok : okLabel},
				icon: Ext.MessageBox.WARNING
			});
			return;
		}
		/*var rowData=selections[0].data;
		 if(rowData.editable==false){
		 return false;
		 }*/
		Ext.MessageBox.show({
			title:titleDelete,
			msg: messageConfirmDelete,
			//buttons:{yes : yesLabel, no : noLabel},
			buttons: Ext.MessageBox.YESNO,
			fn: function(btn){
				if(btn=="yes"){
					if(me.handlerDeleteItem!=null){
						me.handlerDeleteItem();
					}else{
						var selections=me.gridConfig.selectionModel.getSelection();
						var deletedItems="";
						var row;
						var i;
						for(i=0;i<selections.length;i++){
							//row=selections[i];
							row=selections[i].data;
							if(row.editable==true) {
								deletedItems = deletedItems + row.id + ";";
							}
						}
						Ext.Ajax.request({
							url: me.urlDeleteItems,
							disableCaching:true,
							success: function(response){
								if(me.fieldsToNotifyOnDelete!=null){
									var result = Ext.decode(response.responseText);
									var lastModified=result.data.lastModified;
									me.fireItemChange(me.fieldsToNotifyOnDelete,lastModified);
								}
								me.refresh.call(me);
								/*if (reloadFlatHistory) {
								 refreshFlatHistory();
								 }*/
							},
							failure: function(){
							},
							method:'POST',
							params:{"deletedItems":deletedItems,"workItemID":me.workItemID,"projectID":me.projectID,"issueTypeID":me.issueTypeID,"lastModified":me.lastModified}
						});
					}
				}
			},
			//animEl: 'mb4',
			icon: Ext.MessageBox.QUESTION
		});
	}
});
