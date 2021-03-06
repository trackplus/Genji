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


Ext.define('com.trackplus.itemNavigator.SimpleTreeGridViewPlugin',{
	extend: 'com.trackplus.itemNavigator.IssueListViewPlugin',
	mixins:{
		navigable:'com.trackplus.itemNavigator.TreeNavigableItem'
	},
	view:null,
	includeLongFields:false,
	useArrows:false,
	lines:true,
	disableSort:false,
	treeColumnID:null,
	useSelectionModel:false,
	useTree:true,

	constructor: function(config){
		this.callParent(arguments);
		this.mixins.navigable.constructor.call(this);
	},
	refreshData:function(data){
		var me=this;
		if(me.view){
			var rootNode=me.view.getStore().getRootNode();
			rootNode.removeAll();
			if(data&&data.length>0){
				rootNode.appendChild(data);
				me.view.getStore().sort();
			}
		}
	},
	createView:function(){
		var me=this;
		me.queryFieldCSS=me.model.queryFieldCSS;
		me.cellInlineCtr = Ext.create('com.trackplus.itemNavigator.CellInlineEditController',{
			navigator: me,
	    	isPrintItemEditable: me.model.isPrintItemEditable
		});

		me.view=Ext.create('Ext.tree.Panel',{
			margins: '0 0 0 0',
			region:'center',
			border:false,
			bodyBorder:false,
			columnLines :true,
			rowLines:true,
			lines :me.lines,
			rootVisible: false,
			useArrows: me.useArrows,
			cls:'simpleGridView gridNoBorder',
			plugins: [{
		        ptype: 'cellediting',
		        clicksToEdit: 1
		    }],
			viewConfig: {
				stripeRows: true,
				plugins:me.getTreeViewPlugins(),
				getRowClass: function (record, rowIndex, rp, ds) {
            	    return me.getRowClass.call(me,record, rowIndex, rp, ds);
            	}
			},
			store:me.createStore(),
			columns:me.createColumnModel(),
			selModel:me.createSelModel(),
			features:me.createFeatures.call(me)
		});
		me.initMyListeners();

		me.cellInlineCtr.setGrid(me.view);
        me.cellInlineCtr.initListeners();

        return me.view;
	},
	destroyView:function(){
		var me=this;
		me.view=null;
		delete me.view;
	},

	getTreeViewPlugins:function(){
		return [{
			ddGroup: 'itemToCategory',
			ptype: 'gridviewdragdrop',
			enableDrop: false
		}];
	},
	getSelectedIssues:function(){
		var me=this;
		var issueIds=[];
		if(me.model.layout.bulkEdit){
			var selections=me.view.getSelectionModel().getSelection();
			if(selections&&selections.length>0){
				for(var i=0;i<selections.length;i++){
					issueIds.push(selections[i].data.workItemID);
				}
			}
		}
		return issueIds;
	},
	getTreeGrid:function(){
		return this.view;
	},
	selectItemByIndex:function(workItemIndex){
		var me=this;
		return me.selectItemByIndexInTree(me.view,workItemIndex);
	},
	selectItem:function(workItemID){
		var me=this;
		return me.selectItemInTree(me.view,workItemID);
	},
	deselectItem:function(workItemID){
		var me=this;
		return me.deselectItemInTree(me.view,workItemID);
	},
	initMyListeners:function(){
		var me=this;
		//fix ExtJS BUG: click on row in grid with scroll will focus first row
		me.view.on('boxready', function (thisGrid) {
			thisGrid.view.focus = Ext.emptyFn;
		});
		me.view.addListener('itemcontextmenu',me.onGridItemContextMenu,me);
		me.view.addListener('celldblclick',me.onGridItemDblClick,me);
		me.view.addListener('afterrender',me.onGridAfterRenderer,me);
		me.view.addListener('columnresize',me.columnResizeHandler,me);
		me.view.addListener('columnmove',me.columnMoveHandler,me);
		me.view.addListener('sortchange',me.sortChangeHandler,me);
		me.view.addListener('afteritemexpand',me.afterItemExpandHandler,me);
		me.view.addListener('afteritemcollapse',me.afterItemCollapseHandler,me);
		//me.addListener('columnhide',me.columnHideHandler,me);
		//me.addListener('columnshow',me.columnShowHandler,me);
		me.view.getSelectionModel().addListener('selectionchange',me.onGridSelectionChange,me);
	},

	onGridSelectionChange: function(sm, selections) {
		var me = this;
		if(me.model.layout.bulkEdit){
			me.fireEvent.call(me,'selectionchange',selections);
		}
		if(me.cellInlineCtr ) {
			me.cellInlineCtr.onGridSelectionChnage(sm, selections);
		}
	},

	onGridAfterRenderer:function(){
		var me=this;
		var rowExpandAllTrgEl=Ext.get('rowExpandAllTrg');
		if(rowExpandAllTrgEl){
			rowExpandAllTrgEl.addListener('click',me.rowExpandAll,me);
		}
		var rowCollapseAllTrgEl=Ext.get('rowCollapseAllTrg');
		if(rowCollapseAllTrgEl){
			rowCollapseAllTrgEl.addListener('click',me.rowCollapseAll,me);
		}
		var dragZone=this.view.view.plugins[0].dragZone;
		if(dragZone){
			dragZone.onStartDrag=function(x,y){
				var records=this.dragData.records;
				var workItems=me.getSelectedItemIds(records);
				me.fireEvent('startDragItems',workItems);
				return true;
			};
			dragZone.afterDragDrop=function(){
				me.fireEvent('afterDropItems');
			};
			dragZone.afterInvalidDrop=function(){
				me.fireEvent('afterDropItems');
			};
		}
	},
	getSelectedItemIds:function(records){
		var workItems="";
		var workItemID=-1;
		for(var i=0;i<records.length;i++){
			var workItemIDStr=records[i].data['workItemID'];
			try {
				workItemID=parseInt(workItemIDStr);
			}catch(e){
				workItemID=-1;
			}
			if(!isNaN(workItemID)&&workItemID!==-1){
				workItems+=workItemID+",";
			}
		}
		return workItems;
	},
	onGridItemContextMenu:function(gridView,record,item,index, event,opts){
		var me=this;
		event.stopEvent();
		me.fireEvent('itemcontextmenu',record.data,event,me.view,index,record);
		return false;
	},
	onGridItemDblClick:function(view, td,cellIndex,record, tr, rowIndex,e){
		var me=this;

		e.stopEvent();
		if(me.model.layout.bulkEdit===true&&cellIndex===0){
			return false;
		}
		me.fireEvent.call(me,'itemdblclick',record.data,td);
		return false;
	},
	invalidExpandCollapse:false,
	expandCollapseItem:function(node,expanded){
		var me=this;
		var id=node.data.id;
		if((id+'').indexOf('g')!==-1){
			//group
			me.expandGroup(id.substring(1),expanded);
		}else{
			//item
			me.expandItem(id,expanded);
		}
	},
	expandGroup:function(groupID,expanded){
		Ext.Ajax.request({
			url: 'reportExpand!expandGroup.action',
			params:{
				expanded:expanded,
				groupID:groupID
			}
		});
	},
	expandItem:function(workItemID,expanded){
		Ext.Ajax.request({
			url: 'reportExpand!expandReportBean.action',
			params:{
				expanded:expanded,
				workItemID:workItemID
			}
		});
	},
	afterItemExpandHandler:function(node, index, item){
		var me=this;
		if(me.invalidExpandCollapse===true){
			return;
		}
		me.expandCollapseItem(node,true);
	},
	afterItemCollapseHandler:function(node, index, item, eOpts){
		var me=this;
		if(me.invalidExpandCollapse===true){
			return;
		}
		me.expandCollapseItem(node,false);
	},
	validateExpandCollapse:function(node){
		var me=this;
		Ext.Function.defer(function(){
			me.invalidExpandCollapse=false;
		}, 3000, me);
	},
	rowExpandAll:function(e){
		var me=this;
		e.stopEvent();
		me.invalidExpandCollapse=true;
		//me.view.removeListener('beforeitemexpand',me.beforeItemExpandHandler,me);
		//me.view.removeListener('beforeitemcollapse',me.beforeItemCollapseHandler,me);
		me.view.expandAll(me.validateExpandCollapse,me);
		Ext.Ajax.request({
			url: "reportExpand!expandAll.action"
		});
		return false;
	},
	rowCollapseAll:function(e){
		var me=this;
		e.stopEvent();
		me.invalidExpandCollapse=true;
		me.view.collapseAll(me.validateExpandCollapse,me);
		Ext.Ajax.request({
			url: "reportExpand!collapseAll.action"
		});
		return false;
	},
	columnResizeHandler:function(ct,column,width){
		var me=this;
		var urlStr="layoutColumns!resizeColumn.action";
		var layoutID=column.itemId.substring(2);
		Ext.Ajax.request({
			url: urlStr,
			disableCaching:true,
			success: function(result){
				me.fireEvent.call(me,'layoutchange');
			},
			failure: function(){
			},
			method:'POST',
			params:{
				layoutID:layoutID,
				fieldID: column.fieldID,
				width:width,
				filterType:me.model.queryContext.queryType,
				filterID:me.model.queryContext.queryID
			}
		});
	},
	sortChangeHandler:function(ct,column,direction){
		var me=this;
		var sortField = column.dataIndex.substring(1);
		//false = ascending, true = descending
		var sortOrder = (direction==='DESC');
		me.model.layout.sortField=sortField;
		me.model.layout.sortOrder=sortOrder;
		me.model.layout.sortWithSO=column.sortWithSO;
		if (me.model.queryContext) {
			filterType = me.model.queryContext.queryType;
			filterID = me.model.queryContext.queryID;
		}
		me.fireEvent.call(me,'sortchange',{
			sortField:sortField,
			sortOrder:sortOrder,
			sortWithSO:column.sortWithSO
		});
		Ext.Ajax.request({
			url: 'layoutSorting.action',
			disableCaching:true,
			params:{
				sortField:sortField,
				sortOrder:sortOrder,
				filterType: filterType,
				filterID: filterID
			}
		});
	},

	createFeatures:function(){
		var me=this;
		var features= [];
		/*features.push(Ext.create('com.trackplus.itemNavigator.GroupFeature',{
			treeColumnID:me.treeColumnID,
			useSelectionModel:me.useSelectionModel
		}));*/
		return features;
	},

	getSorters:function(){
		var me=this;
		var sorters=null;
		if(me.model.layout.sortField){
			var sortField="f"+me.model.layout.sortField;
			var sortDirection;
			if(me.model.layout.sortOrder===true){
				sortDirection='DESC';
			}else{
				sortDirection='ASC';
			}
			sorters=[];
			if(me.model.layout.sortWithSO===true){
				sorters.push({
					property:sortField,
					direction :sortDirection,
					sorterFn:function(o1,o2){
						var me = this;
						var v1 = me.getRoot(o1)['f_so'+me.property.substring(1)];
						var v2 = me.getRoot(o2)['f_so'+me.property.substring(1)];
						var r= v1 > v2 ? 1 : (v1 < v2 ? -1 : 0);
					}
				});
			}else{
				sorters.push({
					property:sortField,
					direction :sortDirection
				});
			}

		}
		return sorters;
	},
	createStore:function(){
		var me=this;
		var fields=me.createFields();
		var store=Ext.create('Ext.data.TreeStore', {
			fields:fields,
			autoLoad: false,
			//sorters:me.getSorters(),
			root: {
				expanded: true,
				text:"",
				children:me.model.issues
			}
		});
		store.addListener("refresh",me.onStoreRefresh,me);
		return store;
	},

	findNodeByWorkItemIndex:function(workItemIndex){
		var me=this;
		var selectedNode=null;
		var maxIndex=0;
		var maxNode=0;
		var root=me.view.getStore().getRootNode();
		root.cascadeBy(function (node) {
			var idx=node.data['workItemIndex'];
			if(idx){
				if(idx>maxIndex){
					maxIndex=idx;
					maxNode=node;
				}
				if(idx===workItemIndex){
					selectedNode=node;
				}
			}
		});
		if(selectedNode){
			return selectedNode;
		}
		return maxNode;
	},
	getLayoutData:function(columnID){
	},
	createColumnModel:function(){
		var me=this;
		var shortFields=me.model.layout.shortFields;
		var columnModel=new Array(0);
		var layoutData;
		if(me.model.layout.indexNumber===true){
			var count=me.model.totalCount;
			var size=0;
			if(CWHF.isNull(count)||count===0){
				size=21;
			}else{
				size=(count+"").length*5+17;
			}
			columnModel.push({
				draggable :false,
				menuDisabled:true,
				width:size,
				dataIndex:'workItemIndex',
				sortable:false,
				align:'right',
				tdCls:'simpleTreeGridCell',
				resizable:false
			});
		}
		for(var i=0;i<shortFields.length;i++){
			layoutData=shortFields[i];
			var sortable= !me.disableSort;//&&layoutData.sortable;
			var col=me.createColumn(i,layoutData,sortable,me.useTree);
			if(i>0){
				me.cellInlineCtr.setColumnEditor(shortFields[i], col);
				var uniformizedFieldType = me.cellInlineCtr.getUniformizedFieldType(layoutData.extJsRendererClass);
				if (uniformizedFieldType ) {
					var renderer = me.cellInlineCtr.getRendererForSpecificColumns(uniformizedFieldType);
					if (renderer) {
						col.renderer = renderer;
					}
				}
			}
			columnModel.push(col);
		}
		return columnModel;
	},

	dataChangeSuccess:function(opts){
		var me = this;
		me.cellInlineCtr.dataChangeSuccess();
	}

});

function openAttachments(workItemID,attachmentIds){
	var me=this;
	var selections=attachmentIds.split(";");
	for(var i=0;i<selections.length;i++){
		var attachID=selections[i];
		var attachmentURI='downloadAttachment.action?workItemID='+workItemID+'&attachKey='+attachID;
		if(i===0){
			window.open(attachmentURI,'attachmentWindow');
		}else{
			window.open(attachmentURI);
		}
	}
}
