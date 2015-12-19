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


Ext.define('com.trackplus.itemNavigator.FlatGridViewPlugin',{
	extend: 'com.trackplus.itemNavigator.IssueListViewPlugin',
	mixins:{
		navigable:'com.trackplus.itemNavigator.GridNavigableItem'
	},
	plainGrid:true,
	view:null,
	includeLongFields:true,
	disableSort:false,
	treeColumnID:null,
	useSelectionModel:false,
	useTree:true,
	paginate:false,
	pageSize:10,
	cellInlineCtr:null,

	constructor: function(config){
		var me=this;
		this.callParent(arguments);
		this.mixins.navigable.constructor.call(this);
		me.paginate=me.model.listViewData.paginate;
		me.pageSize=me.model.listViewData.pageSize;
	},
	refreshData:function(data){
		var me=this;
		if(me.view){
			me.view.getStore().loadData(data);
		}
	},
	createFields:function(){
		var me=this;
		var fields=me.callParent();
		var longFields=me.model.layout.longFields;
		if(longFields){
			for(var i=0;i<longFields.length;i++){
				fields.push({name:'f'+longFields[i].reportField});
			}
		}
		fields.push({
			name : 'leaf'
		});
		return fields;
	},
	getSettingsToolbarItems:function(btnGroup,btnChooseColumns){
		var me=this;
		var items =me.callParent(arguments);
		var paginateLbl=getText('itemov.lbl.paginate');
		me.checkPaginate=Ext.create('Ext.form.field.Checkbox',{
			boxLabel:paginateLbl ,
			overflowText :paginateLbl,
			tooltip :getText('itemov.lbl.paginate.tt'),
			inputValue : true,
			checked:me.paginate

		});
		me.checkPaginate.addListener('change',me.changePaginate,me);
		items.push(me.checkPaginate);
		return items;
	},
	changePaginate:function(){
		var me=this;
		me.view.setLoading(true);
		me.paginate=me.checkPaginate.getValue();
		Ext.Ajax.request({
			url: "userPreferences.action?property=itemNavigatorPaginate&value="+me.paginate,
			disableCaching:true,
			success: function(data){
				me.view.setLoading(false);
				me.fireEvent.call(me,'datachange');
			},
			failure: function(type, error){
			}
		});
	},
	changePageSize:function(field, e){
		if (e.getKey() === e.ENTER) {
			var me = this;
			me.view.setLoading(true);
			me.pageSize = me.txtPageSize.getValue();
			Ext.Ajax.request({
				url: "userPreferences.action?property=itemNavigatorPageSize&value=" + me.pageSize,
				disableCaching: true,
				success: function (data) {
					me.view.setLoading(false);
					me.fireEvent.call(me, 'datachange');
				},
				failure: function (type, error) {
				}
			});
		}
	},

	createView:function(){
		var me=this;
		me.cellInlineCtr = Ext.create('com.trackplus.itemNavigator.CellInlineEditController',{
			navigator: me,
	    	isPrintItemEditable: me.model.isPrintItemEditable
		});
		me.queryFieldCSS=me.model.queryFieldCSS;
		var store=me.createStore();
		var pluginExpanded = true;
		var cfg={
			margins: '0 0 0 0',
			region:'center',
			border:false,
			bodyBorder:false,
			columnLines :true,
			rowLines:true,
			cls:'simpleGridView gridNoBorder',
			plugins: [{
		        ptype: 'cellediting',
		        clicksToEdit: 1
		    }],
			viewConfig: {
				stripeRows: true,
				selectionModel: {
					type: 'rowmodel'
				},
				plugins:me.getTreeViewPlugins(),
				getRowClass: function (record, rowIndex, rp, ds) {
					return me.getRowClass.call(me,record, rowIndex, rp, ds);
				}
			},
			store:store,
			columns:me.createColumnModel(),
			selModel:me.createSelModel(),
			features:me.createFeatures.call(me)
		};
		if(me.paginate===true){
			me.txtPageSize=Ext.create('Ext.form.field.Number',{
				name: 'pageSize',
				fieldLabel: getText('itemov.lbl.pageSize'),
				labelAlign:'right',
				value: me.pageSize,
				maxValue: 10000,
				minValue: 10,
				allowDecimals:false,
				width:175
			});
			me.txtPageSize.addListener('specialkey',me.changePageSize,me);
			cfg.tbar=Ext.create('Ext.PagingToolbar', {
				itemId:'paginateToolbar',
				store: store,
				displayInfo: true,
				afterPageText:getText('itemov.lbl.paginate.afterPageText'),
				beforePageText:getText('itemov.lbl.paginate.beforePageText'),
				displayMsg: getText('itemov.lbl.paginate.displayMsg'),
				emptyMsg: getText('itemov.lbl.paginate.emptyMsg'),
				items:['-', me.txtPageSize]
			});
		}
		me.view=Ext.create('Ext.grid.Panel',cfg);
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
	getGrid:function(){
		return this.view;
	},
	getFieldNameItemID:function(){
		return 'workItemID';
	},
	selectItemByIndex:function(workItemIndex){
		var me=this;
		return me.selectItemGridByIndex(me.view,workItemIndex);
	},
	selectItem:function(workItemID){
		var me=this;
		var grid=me.getGrid();
		if(CWHF.isNull(grid)||CWHF.isNull(workItemID)){
			return null;
		}
		return me.selectItemGrid(grid,workItemID);
	},
	deselectItem:function(workItemID){
		var me=this;
		var grid=me.getGrid();
		if(CWHF.isNull(grid)||CWHF.isNull(workItemID)){
			return null;
		}
		me.deselectItemGrid(grid,workItemID);
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
		//me.view.addListener('afteritemexpand',me.afterItemExpandHandler,me);
		//me.view.addListener('afteritemcollapse',me.afterItemCollapseHandler,me);
		//me.addListener('columnhide',me.columnHideHandler,me);
		//me.addListener('columnshow',me.columnShowHandler,me);
		me.view.getSelectionModel().addListener('selectionchange',me.onGridSelectionChange,me);
	},

	onGridSelectionChange: function(sm, selections) {
		var me = this;
		if(me.model.layout.bulkEdit){
//			me.callParent(sm, selections);
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
		var filterType = null;
		var filterID = null;
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
			},
			success:function(){
				if(me.paginate===true) {
					me.view.getDockedComponent('paginateToolbar').moveFirst();
				}
			}
		});
	},

	createFeatures:function(){
		var me=this;
		var features=new Array();
		if(me.model.layout.longFields&&me.model.layout.longFields.length>0){
			features.push(me.createRowBodyFeature());
		}
		//features.push({ftype: 'rowwrap'});
		return features;
	},
	createRowBodyFeature:function(){
		var me=this;
		return Ext.create('com.trackplus.itemNavigator.RowBody',{
			model:me.model
		});
	},

	getSorters:function(){
		var me=this;
		var sorters=null;
		return null;
		/* TODO
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

		} */
		// return sorters;
	},
	createStore:function(){
		var me=this;
		var fields=me.createFields();
		var cfg={
			fields:fields,
			pageSize: me.pageSize,
			autoLoad: true/*,
			sorters:me.getSorters()*/
		};
		if(me.paginate===true){
			cfg.proxy={
				type: 'ajax',
				url: 'itemNavigator!navigate.action',
				reader:Ext.create('com.trackplus.itemNavigator.FlatGridReader',{
					rootProperty: 'issues',
					totalProperty: 'totalCount',
					flatGridViewPlugin:me
				}),
				simpleSortMode: true
			};
		}else{
			cfg.data=me.model.issues;
		}
		var store=Ext.create('Ext.data.Store',cfg);
		if(me.paginate===true){
			store.addListener('beforeload',me.beforeLoadPage,me);
		}
		//store.addListener("refresh",me.onStoreRefresh,me);
		return store;
	},
	beforeLoadPage:function(){
		var me=this;
		me.view.store.getProxy().extraParams = {
			queryContextID:me.model.queryContext.id,
			nodeType:me.model.nodeType,
			nodeObjectID:me.model.nodeObjectID
		};
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
			var col=me.createColumn(i,layoutData,sortable,false);
			me.cellInlineCtr.setColumnEditor(shortFields[i], col);
			var uniformizedFieldType = me.cellInlineCtr.getUniformizedFieldType(layoutData.extJsRendererClass);
			if(uniformizedFieldType ) {
				var renderer = me.cellInlineCtr.getRendererForSpecificColumns(uniformizedFieldType);
				if(renderer ) {
					col.renderer = renderer;
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

Ext.define('com.trackplus.itemNavigator.FlatGridReader',{
	extend: 'Ext.data.reader.Json',
	config:{
		flatGridViewPlugin:null
	},
	readRecords: function(data) {
		var me = this;
		var totalCount=data.totalCount;
		var count=data.count;
		me.getFlatGridViewPlugin().fireEvent('totalChange',totalCount,count);
		return me.callParent(arguments);
	}
});

Ext.define('com.trackplus.itemNavigator.RowBody',{
	extend: 'Ext.grid.feature.RowBody',
	config:{
		model:{}
	},
	init:function(grid){
		var me = this;
		me.extraRowTpl=[
			'{%',
			'values.view.rowBodyFeature.setupRowData(values.record, values.recordIndex, values);',
			'this.nextTpl.applyOut(values, out, parent);',
			'%}',
				'<tr class="' + Ext.baseCSSPrefix + 'grid-rowbody-tr {rowBodyCls}">',
			me.model.layout.bulkEdit?'<tpl if="emptyCellClass === \'\'" ><td class="x-grid-cell-special"></td><tpl else><td class="x-grid-cell-special" style="border-top-width:1px;"></td></tpl> ':'',
			me.model.layout.indexNumber?'<td class="simpleTreeGridRowBodyEmptyCell {emptyCellClass}"></td>':'',
			'<td class="simpleTreeGridRowBodyEmptyCell {emptyCellClass}"></td>',
				'<td class="' + Ext.baseCSSPrefix + 'grid-cell-rowbody' + '" colspan="{rowBodyColspan}">',
				'<div class="ulist ' + Ext.baseCSSPrefix + 'grid-rowbody' + ' {rowBodyDivCls}">{rowBody}</div>',
			'</td>',
			'</tr>', {
				priority: 100,

				syncRowHeights: function(firstRow, secondRow) {
					var owner = this.owner,
						firstRowBody = Ext.fly(firstRow).down(owner.eventSelector, true),
						secondRowBody,
						firstHeight, secondHeight;

					// Sync the heights of row body elements in each row if they need it.
					if (firstRowBody && (secondRowBody = Ext.fly(secondRow).down(owner.eventSelector, true))) {
						if ((firstHeight = firstRowBody.offsetHeight) > (secondHeight = secondRowBody.offsetHeight)) {
							Ext.fly(secondRowBody).setHeight(firstHeight);
						}
						else if (secondHeight > firstHeight) {
							Ext.fly(firstRowBody).setHeight(secondHeight);
						}
					}
				},

				syncContent: function(destRow, sourceRow) {
					var owner = this.owner,
						destRowBody = Ext.fly(destRow).down(owner.eventSelector, true),
						sourceRowBody;

					// Sync the heights of row body elements in each row if they need it.
					if (destRowBody && (sourceRowBody = Ext.fly(sourceRow).down(owner.eventSelector, true))) {
						Ext.fly(destRowBody).syncContent(sourceRowBody);
					}
				}
			}
		];

		me.callParent(arguments);
	},
	getAdditionalData: function(data, idx, record, orig) {
		var me=this;
		var headerCt = this.view.headerCt;
		var colspan = headerCt.getColumnCount()-1;
		if(me.model.layout.bulkEdit){
			colspan=colspan-1;
		}
		if(me.model.layout.indexNumber){
			colspan=colspan-1;
		}
		if(data['group']===true){//grouping
			return null;
		}
		var rowBody='';
		var rowBodyContent='';
		var emptyCellClass='';
		if(me.model.layout.longFields&&me.model.layout.longFields.length>0){
			rowBody='<div class="simpleTreeGridRowBody">';
			var longField;
			for(var i=0;i<me.model.layout.longFields.length;i++){
				longField=me.model.layout.longFields[i];
				var longFieldID=longField.reportField;
				var longFieldLabel=longField.label;
				var longFieldValue=data["f"+longFieldID];
				switch(longFieldID){
					case -1005:{
						//history
						var history=data['f'+longFieldID];
						if(history&&history.length>0){
							rowBodyContent+=me.createHistoryTemplate.call(me,data, idx, record,orig,longFieldLabel,history);
						}
						break;
					}
					case 23:{
						//comment
						var comments=data['f'+longFieldID];
						if(comments&&comments.length>0){
							rowBodyContent+=me.createCommentsTemplate.call(me,data, idx, record,orig,longFieldLabel,comments);
						}
						break;
					}
					case -1008:{
						//cost
						var costs=data['f'+longFieldID];
						if(costs&&costs.length>0){
							rowBodyContent+=me.createCostsTemplate.call(me,data, idx, record,orig,longFieldLabel,costs);
						}
						break;
					}
					case -1007://budgetHistory
					case -1011:{
						//PLAN_HISTORY_LIST = -1011
						var budgetHistory=data['f'+longFieldID];
						if(budgetHistory&&budgetHistory.length>0){
							rowBodyContent+=me.createBudgetHistoryTemplate.call(me,data, idx, record,orig,longFieldLabel,budgetHistory);
							//longFieldValue="someething here";
							//rowBodyContent+=me.createLongFieldTemplate.call(me,data, idx, record, orig,longFieldID,longFieldLabel,longFieldValue);
						}
						break;
					}
					default:{
						if(longFieldValue&&longFieldValue!==''){
							rowBodyContent+=me.createLongFieldTemplate.call(me,data, idx, record, orig,longFieldID,longFieldLabel,longFieldValue);
						}
					}
				}
			}
			hasContent=rowBodyContent!=='';
			if(hasContent){
				emptyCellClass='simpleTreeGridRowBodyEmptyCell-bodyContent';
			}
			rowBody+=rowBodyContent;
			rowBody+='</div>';
		}
		return {
			rowBody: rowBody,
			rowBodyCls: this.rowBodyCls,
			rowBodyColspan: colspan,
			emptyCellClass:emptyCellClass
		};
	},
	createLongFieldTemplate:function(data, idx, record, orig,longFieldID,lonFieldLabel,longFieldValue){
		var me=this;
		if(CWHF.isNull(me.longFieldTpl)){
			me.initLongFieldTpl();
		}
		return me.longFieldTpl.applyTemplate({lonFieldLabel: lonFieldLabel, longFieldValue:longFieldValue});
	},
	initLongFieldTpl:function(){
		var me=this;
		var str='<table class="reportHistory" border="0" cellPadding="0" cellSpacing="0">';
		str+='<tr class="reportHistoryRow"><td class="reportHistoryCellLabel">{lonFieldLabel}</td></tr>';
		str+='<tr class="reportHistoryRowLast"><td class="reportHistoryCellLongField">{longFieldValue}</td></tr>';
		str+='</table>';
		me.longFieldTpl = new Ext.Template(str);
		me.longFieldTpl.compile()
	},
	createCommentsTemplate:function(data, idx, record, orig,longFieldLabel,comments){
		var me=this;
		if(CWHF.isNull(me.commentsTpl)){
			me.initCommentsTpl();
		}
		return me.commentsTpl.applyTemplate({longFieldLabel:longFieldLabel,comments: comments});
	},
	initCommentsTpl:function(){
		var me=this;
		var str=[
			'<table class="reportHistory" border="0" cellPadding="0" cellSpacing="0">',
			'<tr style="height: 0px">',
			'<th width="130px"></th>',
			'<th width="200px"></th>',
			'<th width="100%"></th>',
			'</tr>',
			'<tr class="reportHistoryRow"><td class="reportHistoryCellLabel" colspan="3">{longFieldLabel}</td></tr>',
			'<tr class="reportHistoryRow">',
				'<td class="reportHistoryCellHeader">'+getText('common.history.lbl.lastEdit')+'</td>',
				'<td class="reportHistoryCellHeader">'+getText('common.history.lbl.changedBy')+'</td>',
				'<td class="reportHistoryCellHeader">'+getText('common.history.lbl.comment')+'</td>',
			'</tr>',
			'<tpl for="comments">',
			'<tr class="reportHistoryRow">',
			'<td class="reportHistoryCell"><div style="width:auto;overflow:hidden;">{date}</div></td>',
			'<td class="reportHistoryCell"><div style="width:auto;overflow:hidden;">{author}</div></td>',
			'<td class="reportHistoryCellLast longFieldCell"><div style="width:auto;overflow:hidden;">{comment}</div></td>',
			'</tr>',
			'</tpl>',
			'</table>'
		];
		me.commentsTpl = new Ext.XTemplate(str);
		me.commentsTpl.compile()
	},
	createHistoryTemplate:function(data, idx, record, orig,longFieldLabel,history){
		var me=this;
		if(CWHF.isNull(me.historyTpl)){
			me.initHistoryTpl();
		}
		return me.historyTpl.applyTemplate({longFieldLabel:longFieldLabel,history: history});
	},
	initHistoryTpl:function(){
		var me=this;
		var str=[
			'<table class="reportHistory" border="0" cellPadding="0" cellSpacing="0">',
			'<tr class="reportHistoryRow">',
			'<td class="reportHistoryCellLabel" colspan="4">{longFieldLabel}</td>',
			'</tr>',
			'<tr class="reportHistoryRow">',
				'<td class="reportHistoryCellHeader" width="100px">'+getText('common.history.lbl.lastEdit')+'</td>',
				'<td class="reportHistoryCellHeader" width="20%">'+getText('common.history.lbl.changedBy')+'</td>',
				'<td class="reportHistoryCellHeader" width="40%">'+getText('common.history.lbl.newValue')+'</td>',
				'<td class="reportHistoryCellHeader" width="40%">'+getText('common.history.lbl.oldValue')+'</td>',
			'</tr>',
			'<tpl for="history">',
			'<tr class="reportHistoryRow">',
			'<td class="reportHistoryCell"><div style="width:auto;overflow:hidden;">{date}</div></td>',
			'<td class="reportHistoryCell"><div style="width:auto;overflow:hidden;">{author}</div></td>',
			'<td class="reportHistoryCell"><div style="width:auto;overflow:hidden;">{newValues}</div></td>',
			'<td class="reportHistoryCellLast"><div style="width:auto;overflow:hidden;">{oldValues}</div></td>',
			'</tr>',
			'</tpl>',
			'</table>'
		];
		me.historyTpl = new Ext.XTemplate(str);
		me.historyTpl.compile()
	},
	createCostsTemplate:function(data, idx, record, orig,longFieldLabel,costs){
		var me=this;
		if(CWHF.isNull(me.costsTpl)){
			me.initCostsTpl();
		}
		return me.costsTpl.applyTemplate({longFieldLabel:longFieldLabel,costs: costs});
	},
	initCostsTpl:function(){
		var me=this;
		var str=[
			'<table class="reportHistory" border="0" cellPadding="0" cellSpacing="0">',
			'<tr class="reportHistoryRow">',
			'<td class="reportHistoryCellLabel" colspan="5">{longFieldLabel}</td>',
			'</tr>',
			'<tr class="reportHistoryRow">',
				'<td class="reportHistoryCellHeader" width="100px">'+getText('common.history.lbl.lastEdit')+'</td>',
				'<td class="reportHistoryCellHeader" width="20%">'+getText('common.history.lbl.changedBy')+'</td>',
				'<td class="reportHistoryCellHeader" width="50%">'+getText('common.history.lbl.subject')+'</td>',
				'<td class="reportHistoryCellHeader" width="10%">'+getText('item.tabs.expense.editExpense.lbl.effort')+'</td>',
				'<td class="reportHistoryCellHeader" width="10%">'+getText('common.lbl.cost')+'</td>',
			'</tr>',
			'<tpl for="costs">',
			'<tr class="reportHistoryRow">',
			'<td class="reportHistoryCell"><div style="width:auto;overflow:hidden;">{date}</div></td>',
			'<td class="reportHistoryCell"><div style="width:auto;overflow:hidden;">{author}</div></td>',
			'<td class="reportHistoryCellLast"><div style="width:auto;overflow:hidden;">{subject}</div></td>',
			'<td class="reportHistoryCellLast"><div style="width:auto;overflow:hidden;">{work}</div></td>',
			'<td class="reportHistoryCellLast"><div style="width:auto;overflow:hidden;">{cost}</div></td>',
			'</tr>',
			'</tpl>',
			'</table>'
		];
		me.costsTpl = new Ext.XTemplate(str);
		me.costsTpl.compile();
	},
	createBudgetHistoryTemplate:function(data, idx, record, orig,longFieldLabel,costs){
		var me=this;
		if(CWHF.isNull(me.budgetHistoryTpl)){
			me.initBudgetHistoryTpl();
		}
		return me.budgetHistoryTpl.applyTemplate({longFieldLabel:longFieldLabel,costs: costs});
	},
	initBudgetHistoryTpl:function(){
		var me=this;
		var str=[
			'<table class="reportHistory" border="0" cellPadding="0" cellSpacing="0">',
			'<tr class="reportHistoryRow">',
			'<td class="reportHistoryCellLabel" colspan="4">{longFieldLabel}</td>',
			'</tr>',
			'<tr class="reportHistoryRow">',
				'<td class="reportHistoryCellHeader" width="100px">'+getText('common.history.lbl.lastEdit')+'</td>',
				'<td class="reportHistoryCellHeader" width="20%">'+getText('common.history.lbl.changedBy')+'</td>',
				'<td class="reportHistoryCellHeader" width="40%">'+getText('item.tabs.expense.editExpense.lbl.effort')+'</td>',
				'<td class="reportHistoryCellHeader" width="40%">'+getText('common.lbl.cost')+'</td>',
			'</tr>',
			'<tpl for="costs">',
			'<tr class="reportHistoryRow">',
			'<td class="reportHistoryCell"><div style="width:auto;overflow:hidden;">{date}</div></td>',
			'<td class="reportHistoryCell"><div style="width:auto;overflow:hidden;">{author}</div></td>',
			'<td class="reportHistoryCellLast"><div style="width:auto;overflow:hidden;">{work}</div></td>',
			'<td class="reportHistoryCellLast"><div style="width:auto;overflow:hidden;">{cost}</div></td>',
			'</tr>',
			'</tpl>',
			'</table>'
		];
		me.budgetHistoryTpl = new Ext.XTemplate(str);
		me.budgetHistoryTpl.compile();
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
