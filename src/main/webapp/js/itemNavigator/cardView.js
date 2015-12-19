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

Ext.define('com.trackplus.itemNavigator.CardViewPlugin',{
	extend: 'com.trackplus.itemNavigator.IssueListViewPlugin',
	fieldGroup:null,
	northPanel:null,
	selectedGroups:null,
	fieldGroupLabel:null,
	queryFieldCSS:null,
	mySelectedIdx:0,
	selectedCardDataView:null,
	refreshData: function(data){
		alert("refresh data");
	},
	dataChangeSuccess:function(opts){
	},

	createSettingsPanel:function(){
		var me=this;
		me.initNorthPanel();
		me.multipleSelect.setValue(me.selectedGroups);
		return me.northPanel;
	},
	getSettingsHeight:function(){
		return 32;
	},
	/**
	 * @cfg {Function} createView
	 *
	 */
	createView:function(){
		var me=this;
		if(CWHF.isNull(me.view)){

			me.centerPanel=Ext.create('Ext.panel.Panel',{
				region:'center',
				layout: {
					type: 'hbox',
					padding:'2',
					align:'stretch'
				},
				border:false,
				defaults:{
					margins:'0 2 0 0'
				},
				bodyBorder:false,
				autoScroll:true
			});
			me.view=me.centerPanel;
			/*me.view=Ext.create('Ext.panel.Panel',{
				layout:'border',
				border:false,
				bodyBorder:false,
				items:[me.northPanel,me.centerPanel]
			});*/

			me.refreshCards();
		}
		return me.view;
	},
	initNorthPanel:function(){
		var me=this;
		var items=new Array();
		var options=me.model.listViewData.groupBy.options;
		me.cmbGroupBy=Ext.create('Ext.form.field.ComboBox',{
			fieldLabel:getText('itemov.cardView.column'),
			name:'groupBy',
			store: Ext.create('Ext.data.Store', {
				data	:(CWHF.isNull(options)?[]:options),
				fields	: [{name:'id', type:'int'}, {name:'label', type:'string'}],
				autoLoad: false
			}),
			border:false,
			bodyBorder:false,
			displayField: 'label',
			valueField: 'id',
			queryMode: 'local',
			value:me.model.listViewData.groupBy.id,
			width:250,
			labelAlign:'right',
			margin: '5 5 5 5'
		});
		me.cmbGroupBy.addListener('change',me.changeGroupBy,me);
		items.push(me.cmbGroupBy);


		items.push(me.createAddGroupPanel());
		var optionsSortBy=me.model.listViewData.sortBy.options;
		me.cmbSortBy=Ext.create('Ext.form.ComboBox',{
			name:'sortBy',
			store: Ext.create('Ext.data.Store', {
				data	:(CWHF.isNull(optionsSortBy)?[]:optionsSortBy),
				fields	: [{name:'id', type:'int'}, {name:'label', type:'string'},{name:'selected',type:'boolean'}],
				autoLoad: false
			}),
			border:false,
			bodyBorder:false,
			displayField: 'label',
			valueField: 'id',
			queryMode: 'local',
			value:me.model.listViewData.sortBy.sortField,
			width:195
		});
		me.cmbSortBy.addListener('change',me.changeSortBy,me);
		var sortOrder=me.model.listViewData.sortBy.sortOrder;
		me.radioAsc=Ext.create('Ext.form.field.Radio',{
			name:'sortOrder',
			inputValue: true,
			boxLabel:getText('itemov.lbl.groupBy.ascending'),
			checked:!sortOrder,
			margin: '0 0 0 5'
		});
		me.radioAsc.addListener('change',me.changeSortBy,me);
		me.radioDsc=Ext.create('Ext.form.field.Radio',{
			name:'sortOrder',
			inputValue: true,
			boxLabel:getText('itemov.lbl.groupBy.descending'),
			checked:sortOrder,
			margin: '0 0 0 5'
		});
		items.push({
			xtype: 'fieldcontainer',
			margin: '5 5 5 5',
			fieldLabel:getText('itemov.lbl.sortBy'),
			labelAlign:'right',
			labelWidth: 100,
			layout: 'hbox',
			items:[me.cmbSortBy,me.radioAsc,me.radioDsc]
		});

		if(me.model.isFilterView===true&&me.model.maySaveFilterLayout){
			var btnSaveAsFilterLayout=Ext.create("Ext.button.Button",{
				text:getText('itemov.btn.saveAsFilterLayout'),
				overflowText:getText('itemov.btn.saveAsFilterLayout'),
				tooltip:getText('itemov.btn.saveAsFilterLayout'),
				iconCls: me.descriptor.iconCls,
				margin:'5 5 5 10',
				handler:function(){
					me.saveAsCardFilterLayout.call(me);
				}
			});
			items.push(btnSaveAsFilterLayout);
		}



		me.northPanel=Ext.create('Ext.panel.Panel',{
			region:'north',
			layout: {
				type: 'column'
			},
			border:false,
			bodyBorder:false,
			//hidden:!me.settingsVisible,
			items:items
		});
	},
	saveAsCardFilterLayout:function(){
		var me=this;
		var urlStr="itemNavigator!saveAsFilterLayout.action";
		borderLayout.setLoading(true);
		Ext.Ajax.request({
			url: urlStr,
			params:{
				queryType:me.model.queryContext.queryType,
				queryID:me.model.queryContext.queryID
			},
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
	navigate:function(workItemID,workItemIndex,dir,keepPosition){
		var me=this;
		if(CWHF.isNull(me.selectedCardDataView)){
			return null;
		}
		var store =me.selectedCardDataView.getStore();
		if(dir==='next'){
			if(me.mySelectedIdx===store.getCount()-1){
				return null;
			}
			me.mySelectedIdx++;
		}else{
			if(me.mySelectedIdx===0){
				return null;
			}
			me.mySelectedIdx--;
		}
		var record=store.getAt(me.mySelectedIdx);
		if(keepPosition){
			if(dir==='next'){
				me.mySelectedIdx--;
			}else{
				me.mySelectedIdx++;
			}
		}else{
			var selModel=me.selectedCardDataView.getSelectionModel();
			selModel.deselectAll();
			selModel.select(me.mySelectedIdx);
		}
		return{
			workItemID:record.data['workItemID'],
			workItemIndex:me.mySelectedIdx
		};
	},
	changeGroupBy:function(){
		var me=this;
		var fieldGroup=me.cmbGroupBy.getValue();
		me.view.setLoading(true);
		var urlStr="cardView!changeGroupBy.action";
		Ext.Ajax.request({
			params:{
				fieldGroup:fieldGroup,
				queryType:me.model.queryContext.queryType,
				queryID:me.model.queryContext.queryID
			},
			url: urlStr,
			disableCaching:true,
			success: function(data){
				me.view.setLoading(false);
				me.fireEvent.call(me,'datachange');
			},
			failure: function(type, error){
			}
		});
	},
	changeSortBy:function(){
		var me=this;
		var fieldGroup=me.cmbGroupBy.getValue();
		var sortField=me.cmbSortBy.getValue();
		var sortOrder=me.radioAsc.getSubmitValue()!==true;
		me.model.layout.sortField=sortField;
		me.model.layout.sortOrder=sortOrder;
		if(sortField){
			me.model.layout.sortWithSO=me.cmbSortBy.getStore().findRecord('id', sortField).data['selected'];
		}
		me.view.setLoading(true);
		var urlStr="cardView!changeSortBy.action";
		Ext.Ajax.request({
			disableCaching:true,
			url:urlStr,
			params:{
				sortField:sortField,
				sortOrder:sortOrder,
				fieldGroup:fieldGroup,
				queryType:me.model.queryContext.queryType,
				queryID:me.model.queryContext.queryID
			},
			success: function(data){
				me.view.setLoading(false);
				me.fireEvent.call(me,'datachange');
			},
			failure: function(type, error){
			}
		});
	},
	findIssuesByGroup:function(groupID){
		var me=this;
		var issues=me.model.issues;
		if(issues&&issues.length>0){
			for(var i=0;i<issues.length;i++){
				var group=issues[i];
				var idx=group.id.lastIndexOf("_");
				var id=group.id.substring(idx+1);
				if(id===groupID+""){
					return me.collectIssues(group.children);
				}
			}
		}
		return null;
	},
	collectIssues:function(issues){
		var me=this;
		if(CWHF.isNull(issues)||issues.length===0){
			return null;
		}
		var allIssues=new Array();
		for(var i=0;i<issues.length;i++){
			var anIssue=issues[i];
			allIssues.push(anIssue);
			var children=me.collectIssues(anIssue.children);
			if(children){
				allIssues=Ext.Array.merge(allIssues,children);
			}
		}
		return allIssues;
	},

	addAllGroups:function(){
		var me=this;
		var allGroups=me.model.listViewData.groups;
		me.selectedGroups=new Array();
		for(var i=0;i<allGroups.length;i++){
			var g=allGroups[i];
			me.selectedGroups.push(g.id);
		}
	},
	refreshCards:function(){
		var me=this;
		me.centerPanel.removeAll(true);
		var groups=new Array();
		me.queryFieldCSS=me.model.queryFieldCSS;
		var allGroups=me.model.listViewData.groups;
		me.fieldGroup=me.model.listViewData.groupBy.id;
		me.selectedGroups=me.model.listViewData.selectedGroups;
		me.fieldGroupLabel=me.model.listViewData.groupBy.labe;

		var addAllGroup=false;
		if(CWHF.isNull(me.selectedGroups)||me.selectedGroups.length===0){
			addAllGroup=true;
			me.selectedGroups=new Array();
		}
		for(var i=0;i<allGroups.length;i++){
			var g=allGroups[i];
			var number=0;
			var issuesInGroup=me.findIssuesByGroup(g.id);
			if(issuesInGroup){
				number=issuesInGroup.length;
			}
			groups.push({id:g.id,label: g.label,number:number,issues:issuesInGroup,width: g.width});
			if(addAllGroup){
				me.selectedGroups.push(g.id);
			}
		}
		me.allGroups=groups;
		var myItems=new Array();
		if(me.selectedGroups){
			for(var i=0;i<me.selectedGroups.length;i++){
				var g=me.findGroup(me.selectedGroups[i]);
				if(g){
					myItems.push(me.createGroupView(g));
				}
			}
		}
		me.centerPanel.add(myItems);
		me.centerPanel.updateLayout();
	},

	createSortFunction:function(){
		var me=this;
		var sorters=null;
		if(me.model.layout.sortField){
			var sortField;
			var sortDirection=1;
			if(me.model.layout.sortOrder===true){
				sortDirection=-1;
			}else{
				sortDirection=1;
			}

			if(me.model.layout.sortWithSO===true){
				sortField="f_so"+me.model.layout.sortField;
			}else{
				sortField="f"+me.model.layout.sortField;
			}
			return function(o1,o2){
				var me = this;
				var v1 = o1[sortField];
				var v2 = o2[sortField];
				var r= v1 > v2 ? 1 : (v1 < v2 ? -1 : 0);
				return r*sortDirection;
			};
		}
		return null;
	},

	findLabelField:function(fieldID){
		var me=this;
		var shortFields=me.model.layout.shortFields;
		if(CWHF.isNull(shortFields)){
			return null;
		}
		for(var i=0;i<shortFields.length;i++){
			if(shortFields[i].reportField===fieldID){
				return shortFields[i].label;
			}
		}
		return null;
	},

	renderIconHtml:function(fieldID, value){
		var htmlStr;
		switch (fieldID){
			case 5://manager
			case 6://responsible
			case 13:{//originator
				var icon='personAvatar.action?personID='+value;
				htmlStr='<img src="'+icon+'" height="50" width="50"/>';
				break;
			}
			default:{
				var icon='optionIconStream.action?fieldID=-'+fieldID+'&optionID='+value;
				htmlStr='<img src="'+icon+'" height="16"/>';
			}
		}
		return htmlStr;
	},

	createGroupView:function(group){
		var me=this;
		var cardElements=new Array();
		var issues=group.issues;
		var fields=me.createFields();
		var sortFn=me.createSortFunction();
		if(sortFn&&issues){
			issues=Ext.Array.sort(issues,sortFn);
		}
		var store=Ext.create('Ext.data.Store', {
			fields:fields,
			data:CWHF.isNull(issues)?[]:issues
		});
		if(CWHF.isNull(me.cardTpl)){
			me.initCardTpl();
		}
		var cardDataView=Ext.create('Ext.view.View', {
			store: store,
			tpl: me.cardTpl,
			multiSelect: true,
			trackOver: true,
			overItemCls: 'x-item-over',
			itemSelector: 'div.cardElementBase',
			emptyText: getText("itemov.cardView.emptyScrumCard"),
			prepareData: function(data) {
				var issue=data[i];
				var itemID=data['f12'];
				var workItemID=data['workItemID'];

				var responsible=data['f6'];
				var synopsis=data['f17'];
				var description=data['f21'];
				var editable=data['editable'];
				var cardCls='cardElementBase cardElement';
				var widthAttr="";
				var clsIndicator='cardElementIndicator';
				if(me.queryFieldCSS&&data['queryFieldCSS']){
					clsIndicator+=' queryFieldCSS rowCls_'+me.queryFieldCSS+"_"+ data['queryFieldCSS'];
				}
				if(editable===false){
					cardCls='cardElementBase cardElement-readOnly';
				}
				var screenFields=me.model.listViewData.panel.fields;
				var screenFieldData;
				var displayValue;

				var screenFieldsHTML=new Array();
				for(var i=0;i<screenFields.length;i++){
					screenFieldsHTML[i]=new Array();
					for(var j=0;j<screenFields[i].length;j++){
						widthAttr="";
						screenFieldData=screenFields[i][j];
						if(screenFieldData){
							screenFieldsHTML[i][j]=new Object();
							screenFieldsHTML[i][j]['empty']=screenFieldData.empty;
						}
						if(screenFieldData&&screenFieldData.empty===false){
							var iconRendering=screenFieldData.iconRendering?screenFieldData.iconRendering:0;
							switch (iconRendering){
								case 0:{
									//label
									displayValue=data['f'+screenFieldData.fieldID];
									break;
								}
								case 1:{
									//icon
									displayValue=me.renderIconHtml(screenFieldData.fieldID,data['f-'+screenFieldData.fieldID]);
									switch (screenFieldData.fieldID){
										case 5://manager
										case 6://responsible
										case 13:{//originator
											widthAttr='style="width:60px;"';
											break;
										}
										default:{
											widthAttr='style="width:30px;"';
										}
									}
									break;
								}
								case 2:{
									//both
									displayValue=me.renderIconHtml(screenFieldData.fieldID,data['f-'+screenFieldData.fieldID])+ '&nbsp;'+data['f'+screenFieldData.fieldID];
									break;
								}
							}
							if(screenFieldData.hideLabel===false){
								var label=me.findLabelField(screenFieldData.fieldID);
								displayValue=label+": "+displayValue;
							}
							screenFieldsHTML[i][j]['displayValue']=displayValue;
							screenFieldsHTML[i][j]['rowSpan']=screenFieldData.rowSpan;
							screenFieldsHTML[i][j]['colSpan']=screenFieldData.colSpan;
							screenFieldsHTML[i][j]['valueHAlign']=screenFieldData.valueHAlign;
							screenFieldsHTML[i][j]['valueVAlign']=screenFieldData.valueVAlign;
							screenFieldsHTML[i][j]['widthAttr']=widthAttr;
						}
					}
				}

				Ext.apply(data,{
					description:description,
					cardCls:cardCls,
					clsIndicator:clsIndicator,
					fields:screenFieldsHTML,
					colsNo:me.model.listViewData.panel.colsNo,
					rowsNo:me.model.listViewData.panel.rowsNo,
					parentGroup:group.id,workItemID:workItemID,itemID:itemID,responsible:responsible,synopsis:synopsis
				});
				return data;
			}
		});
		cardDataView.addListener('itemcontextmenu',me.onGridItemContextMenu,me);
		cardDataView.addListener('itemdblclick',me.onGridItemDblClick,me);
		cardDataView.addListener('itemclick',me.onItemClick,me);
		cardDataView.getSelectionModel().addListener('selectionchange',me.onGridSelectionChange,me);

		cardElements.push(cardDataView);
		var panelGroup=Ext.create('com.trackplus.itemNavigator.GroupView',{
			group:group,
			width:group.width,
			items:cardElements
		});
		panelGroup.addListener('closeGroup',me.removeGroup,me);
		panelGroup.addListener('afterrender',function(c){

			Ext.create('Ext.resizer.Resizer', {
				target: panelGroup,
				handles: 'e',
				minWidth: 200,
				maxWidth: 800,
				listeners:{
					'resize':function(cmp, width, height, e, eOpts ){
						me.groupResizeHandler(panelGroup,me.model.listViewData.fieldGroup,group.id,width);
					}
				}
			});

			var dragZone=new Ext.view.DragZone({
				view: cardDataView,
				ddGroup: 'itemToCategory',
				dragText: '{0} selected row{1}',
				onStartDrag:function(x,y){
					me.validDropTargetIds=null;
					me.partialDropTargetIds=null;
					var records=this.dragData.records;
					var workItems=me.getSelectedItemIds(records);
					var fieldGroup=me.cmbGroupBy.getValue();
					var selectedFieldIDs=new Array();
					selectedFieldIDs.push(fieldGroup);
					me.fireEvent('startDragItems',workItems,selectedFieldIDs,group);
					return true;
				},
				afterDragDrop:function(){
					me.removeAllValidMarkers();
					me.fireEvent('afterDropItems');
				},
				afterInvalidDrop:function(){
					me.removeAllValidMarkers();
					me.fireEvent('afterDropItems');
				}
			});
			var dragSource = new Ext.dd.DragSource(panelGroup.getHeader().getEl(), {
				dragData:{'group': group},
				ddGroup:"itemToCategory",
				onStartDrag:function(){
					var clone = "<div>"+group.label+"</div>";
					clone.id = Ext.id(); // prevent duplicate ids
					this.proxy.update(clone);
					panelGroup.addCls('cardGropDragSource');
				},
				afterDragDrop:function( target, e, id ){
					panelGroup.removeCls('cardGropDragSource');
				},
				afterInvalidDrop:function(){
					panelGroup.removeCls('cardGropDragSource');
				}
			});



			var dropTarget = new Ext.dd.DropTarget(c.getEl(), {
				ddGroup	: 'itemToCategory',
				notifyOver : function(ddSource, e, data) {
					if(data.group){
						if(data.group.id!==group.id){
							return this.dropAllowed;
						}else{
							return false;
						}
					}
					if(!me.checkValidDropTarget(me,data,group)){
						return false;
					}
					return this.dropAllowed
				},
				notifyEnter : function(ddSource, e, data) {
					if(data.group){
						if(data.group.id===group.id){
							return false;
						}
					}else{
						if(!me.checkValidDropTarget(me,data,group)){
							return false;
						}
					}
					if(data.group){
						var idxSource=me.getGroupViewIndex(data.group);
						var idxTarget=me.getGroupViewIndex(group);
						if(idxSource>idxTarget){
							panelGroup.addCls("cardGroupPanel-dragOver-left");
						}else{
							panelGroup.addCls("cardGroupPanel-dragOver-right");
						}
					}else{
						panelGroup.addCls("cardGroupPanel-dragOver");
					}

					return this.dropAllowed
				},
				notifyOut: function(ddSource, e, data) {
					panelGroup.removeCls("cardGroupPanel-dragOver");
					panelGroup.removeCls("cardGroupPanel-dragOver-left");
					panelGroup.removeCls("cardGroupPanel-dragOver-right");
				},
				notifyDrop  : function(ddSource, e, data){
					panelGroup.removeCls("cardGroupPanel-dragOver");
					panelGroup.removeCls("cardGroupPanel-dragOver-left");
					panelGroup.removeCls("cardGroupPanel-dragOver-right");
					if(data.group){
						if(group.id===data.group.id){
							return false;
						}
						var before=false;
						me.changeGroupOrder(data.group,group,before);
					}else{
						if(!me.checkValidDropTarget(me,data,group)){
							return false;
						}
						Ext.defer(me.changeWorkItems, 100, me,[group,data.records]);
					}
					return true;
				}
			});
		});
		return panelGroup
	},
	checkValidDropTarget:function(me,data,group){
		var records=data.records;
		if(records&&records.length>0){
			var r=records[0];
			if(r.data['parentGroup']===group.id){
				return false;
			}
			var valid=CWHF.isNull(me.validDropTargetIds)||Ext.Array.indexOf(me.validDropTargetIds,group.id)!==-1;
			var partialValid=me.partialDropTargetIds&&Ext.Array.indexOf(me.partialDropTargetIds,group.id)!==-1;
			if(valid||partialValid){
				return true;
			}else{
				return false;
			}
		}
		return true;
	},
	updatePossibleFieldOptions:function(jsonData,group){
		var me=this;
		var fieldGroup=me.cmbGroupBy.getValue();
		var validDropTargetIds=jsonData['field'+fieldGroup];
		var partialDropTargetIds=jsonData['partialField'+fieldGroup];
		me.validDropTargetIds=validDropTargetIds;
		me.partialDropTargetIds=partialDropTargetIds;
		for(var i=0;i<me.centerPanel.items.getCount();i++){
			var groupView=me.centerPanel.items.getAt(i);
			var id=groupView.getGroup().id;
			if(id!==group.id){
				if(CWHF.isNull(validDropTargetIds)||Ext.Array.indexOf(validDropTargetIds,id)!==-1){
					groupView.addCls("cardGroupPanel-dropTargetOk");
				}
				if(partialDropTargetIds&&Ext.Array.indexOf(partialDropTargetIds,id)!==-1){
					groupView.addCls("cardGroupPanel-dropTargetPartial");
				}
			}
		}
	},
	removeAllValidMarkers:function(){
		var me=this;
		me.validDropTargetIds=null;
		me.partialDropTargetIds=null;
		for(var i=0;i<me.centerPanel.items.getCount();i++){
			var groupView=me.centerPanel.items.getAt(i);
			groupView.removeCls("cardGroupPanel-dropTargetOk");
			groupView.removeCls("cardGroupPanel-dropTargetPartial");
		}
	},
	myTip:null,
	isDblClick:false,

	onItemClick:function(view, record, item, index, e, eOpts){
		var me=this;
		me.isDblClick=false;
		if(CWHF.isNull(me.myTip)){
			me.myTip = Ext.create('Ext.tip.ToolTip', {
				autoHide : true,
				dismissDelay:0,
				anchor: 'top'
			});
		}
		var description=record.data['f21'];
		if(description&&description!==''){
			me.myTip.update('<div class="ulist">'+description+'</div>');
			Ext.defer(function(){
				if(me.isDblClick){
					return true;
				}
				me.myTip.showAt(e.getXY());
			}, 300);

		}
	},
	onGridItemContextMenu:function(gridView,record,item,index, event,opts){
		var me=this;
		event.stopEvent();
		me.fireEvent('itemcontextmenu',record.data,event,me.view,index,record);
		return false;
	},
	onGridItemDblClick:function(view, record, item, index, e, eOpts){
		var me=this;
		me.isDblClick=true;
		e.stopEvent();
		me.mySelectedIdx=index;
		me.selectedCardDataView=view;
		me.fireEvent.call(me,'itemdblclick',record.data,item);
		return false;
	},
	onGridSelectionChange:function(sm, sel){
		var me=this;
		var allSelections=new Array();
		for(var i=0;i<me.centerPanel.items.getCount();i++){
			var groupView=me.centerPanel.items.getAt(i);
			var selections=groupView.items.get(0).getSelectionModel().getSelection();
			if(selections&&selections.length>0){
				allSelections=Ext.Array.merge(allSelections,selections);
			}
		}
		me.fireEvent.call(me,'selectionchange',allSelections);
		return false;
	},
	createAddGroupPanel:function(){
		var me=this;
		me.fieldGroupLabel=me.model.listViewData.groupBy.label;
		var allGroups=me.model.listViewData.groups;
		var data=new Array();
		for(var i=0;i<allGroups.length;i++){
			var g=allGroups[i];
			data.push({
				id: g.id,
				label: g.label
			});
		}
		var fieldID=me.model.listViewData.groupBy.id;
		var iconUrlPrefix=null;
		if(fieldID===5||fieldID===6){
			iconUrlPrefix=null;
		}else{
			iconUrlPrefix='optionIconStream.action?fieldID=-'+fieldID+'&optionID=';
		}

		me.multipleSelect= Ext.create('com.trackplus.util.MultipleSelectPicker',{
			options:data,
			width:300,
			margin:'5 5 5 5',
			name:'selectedGroups',
			localizedLabel:me.fieldGroupLabel,
			maxSelectionCount:me.model.listViewData.maxSelectionCount,
			iconUrlPrefix:iconUrlPrefix
		});
		me.multipleSelect.addListener('change',me.multipleSelectChange,me);
		return me.multipleSelect;
	},
	multipleSelectChange:function(comp, newValue,oldValue){
		var me=this;
		if(CWHF.isNull(newValue)||newValue.length===0){
			me.centerPanel.removeAll(true);
			me.selectedGroups=null;
			return true;
		}
		me.selectedGroups=new Array();
		//remove groups
		var groupsAlreadyPressent=new Array();
		var groupViewToRemove=new Array();
		for(var i=0;i<me.centerPanel.items.getCount();i++){
			var groupView=me.centerPanel.items.getAt(i);
			var id=groupView.getGroup().id;
			if(Ext.Array.indexOf(newValue,id)!==-1){
				groupsAlreadyPressent.push(id);
				me.selectedGroups.push(id);
			}else{
				groupViewToRemove.push(groupView);
			}
		}
		for(var i=0;i<groupViewToRemove.length;i++){
			me.centerPanel.remove(groupViewToRemove[i]);
		}
		var groupToAdd=new Array();
		for(var i=0;i<newValue.length;i++){
			var id=newValue[i];
			if(Ext.Array.indexOf(groupsAlreadyPressent,id)!==-1){
				continue;
			}
			me.selectedGroups.push(id);
			groupToAdd.push(me.findGroup(id));
		}
		for(var i=0;i<groupToAdd.length;i++){
			me.centerPanel.add(me.createGroupView(groupToAdd[i]));
		}
		me.centerPanel.updateLayout();
		me.changeSelectedGroups();
		return true;
	},
	changeSelectedGroups:function(){
		var me=this;
		var cardViewGroupFilter="";
		if(me.selectedGroups){
			cardViewGroupFilter=me.selectedGroups.join();
		}
		var fieldGroup=me.cmbGroupBy.getValue();
		var urlStr="cardView!cardFieldOptionsChange.action";
		Ext.Ajax.request({
			disableCaching:true,
			url:urlStr,
			params:{
				cardViewGroupFilter:cardViewGroupFilter,
				fieldGroup:fieldGroup,
				queryType:me.model.queryContext.queryType,
				queryID:me.model.queryContext.queryID
			},
			success: function(data){
			},
			failure: function(type, error){
			}
		});
	},
	findGroup:function(id){
		var me=this;
		var allGroups=me.allGroups;
		for(var i=0;i<allGroups.length;i++){
			if(allGroups[i].id===id){
				return allGroups[i];
			}
		}
	},
	getGroupViewIndex:function(group){
		var me=this;
		for(var i=0;i<me.centerPanel.items.getCount();i++){
			var groupView=me.centerPanel.items.getAt(i);
			var id=groupView.getGroup().id;
			if(group.id===id){
				return i;
			}
		}
		return -1;
	},

	removeGroup:function(groupView){
		var me=this;
		var group=groupView.getGroup();
		me.centerPanel.remove(groupView,true);
		if(CWHF.isNull(me.selectedGroups)){
			me.addAllGroups();
		}
		me.selectedGroups=Ext.Array.remove(me.selectedGroups,group.id);
		me.multipleSelect.setValue(me.selectedGroups,true);
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
	changeWorkItems:function(group,records){
		var me=this;
		var workItems=me.getSelectedItemIds(records);
		me.view.setLoading(true);
		var params={
			workItems:workItems,
			fieldID:me.fieldGroup,
			value:group.id
		};
		me.ajaxRequest(params);

	},
	changeGroupOrder:function(sourceGroup,targetGroup,before){
		var me=this;
		var groupViewSource=null;
		var idxSource;
		var idxTarget;
		for(var i=0;i<me.centerPanel.items.getCount();i++){
			var groupView=me.centerPanel.items.getAt(i);
			var id=groupView.getGroup().id;
			if(id===sourceGroup.id){
				groupViewSource=groupView;
				idxSource=i;
			}
			if(id===targetGroup.id){
				idxTarget=i;
			}
		}
		me.centerPanel.remove(idxSource,false);
		me.centerPanel.insert(idxTarget,groupViewSource);
		me.selectedGroups=new Array();
		for(var i=0;i<me.centerPanel.items.getCount();i++){
			var groupView=me.centerPanel.items.getAt(i);
			var id=groupView.getGroup().id;
			me.selectedGroups.push(id);
		}
		me.multipleSelect.setValue(me.selectedGroups,true);
		me.changeSelectedGroups();
	},
	ajaxRequest:function(params){
		var me=this;
		var urlStr="itemNavigator!changeOneField.action";
		Ext.Ajax.request({
			url: urlStr,
			success: function(result){
				me.view.setLoading(false);
				var jsonData=Ext.decode(result.responseText);
				if(jsonData.success===true){
					me.fireEvent.call(me,'datachange');
				}else{
					me.handleError(jsonData,params);
				}
			},
			params:params
		});
	},
	onCardItemDblClick:function(e, t, eOpts){
		var me=this;
		me.fireEvent.call(me,'itemdblclick',eOpts.item,t);
	},
	onCardItemContextMenu:function(e, t, eOpts){
		var me=this;
		e.stopEvent();
		me.fireEvent('itemcontextmenu',eOpts.item,e,me.view);
		return false;
	},

	initCardTpl:function(){
		var me=this;
		var panel=me.model.listViewData.panel;
		var tmpl=new Array();
		tmpl.push('<tpl for=".">');
		tmpl.push('<div class="{cardCls}">');
		tmpl.push('<table style="width:100%;height:100%;" border="0" cellspacing="0" cellpadding="0">');

		tmpl.push('<tr><td rowspan="{rowsNo+1}" width="7" class="{clsIndicator}">&nbsp;</td><td colspan="{colsNo}"></td></tr>');

		tmpl.push('<tpl for="fields">');
		tmpl.push('<TR>');
		tmpl.push('<tpl for=".">');

		tmpl.push('<tpl if=".">');

		tmpl.push('<tpl if="!empty">');
		tmpl.push('<td {widthAttr} class="cardField valueVerticalAlign-{valueVAlign} valueAlign-{valueHAlign}" colspan="{colSpan}" rowspan="{rowSpan}" >');
		tmpl.push('{displayValue}');
		tmpl.push('</td>');
		tmpl.push('</tpl>');//if field not empty

		tmpl.push('<tpl if="empty">');
		tmpl.push('<td>&nbsp;</td>');
		tmpl.push('</tpl>');//if field is empty

		tmpl.push('</tpl>');//if fieldWrapper not null

		tmpl.push('</tpl>');//for
		tmpl.push('</tr>');
		tmpl.push('</tpl>');
		tmpl.push('</table>')
		tmpl.push('</div>')
		tmpl.push('</tpl>');

		me.cardTpl = new Ext.XTemplate(tmpl);
		me.cardTpl.compile()
	},
	destroyView:function(){
	},

	/**
	 * @cfg {Function} getSelectedIssues
	 *
	 */
	getSelectedIssues:function(){
		var me=this;
		var selections=new Array();
		for(var i=0;i<me.centerPanel.items.getCount();i++){
			var groupView=me.centerPanel.items.getAt(i);
			var sel=groupView.items.get(0).getSelectionModel().getSelection();
			if(sel&&sel.length>0){
				selections=Ext.Array.merge(selections,sel);
			}
		}
		var issueIds=[];
		for(var i=0;i<selections.length;i++){
			issueIds.push(selections[i].data.workItemID);
		}
		return issueIds;
	},
	selectItemByIndex:function(workItemIndex){
	},
	selectItem:function(workItemID,isArray){
	},
	deselectItem:function(workItemID,isArray){
	},
	TYPE_COMMON:0,
	TYPE_MASS_OPERATION:1,
	handleError:function(jsonData,params){
		var me=this;
		var type=jsonData.type;
		switch (type){
			case me.TYPE_COMMON:{
				var title=getText("common.err.failure");
				if(jsonData.title){
					title=jsonData.title;
				}
				Ext.MessageBox.show({
					title: title,
					msg:jsonData.errorMessage,
					buttons: Ext.MessageBox.OK,
					icon: Ext.MessageBox.WARNING
				});
				break;
			}
			case me.TYPE_MASS_OPERATION:{
				var massEx=jsonData.massOperation;
				var errorCode=massEx.errorCode;
				if(errorCode&&errorCode===4){
					var title="Error";
					if(massEx.title){
						title=massEx.title;
					}
					Ext.MessageBox.confirm(title,
						massEx.errorMessage,
						function(btn){
							if (btn==="no") {
								return false;
							} else {
								me.confirmationSubmitHandler.call(me,params);
							}
						});
				}else{
					var title="Error";
					if(massEx.title){
						title=massEx.title;
					}
					Ext.MessageBox.show({
						title: title,
						msg:massEx.errorMessage,
						buttons: Ext.MessageBox.OK,
						icon: Ext.MessageBox.WARNING
					});
				}
				break;
			}
		}
	},
	confirmationSubmitHandler:function(params){
		var me=this;
		if(CWHF.isNull(params)){
			params={};
		}
		params['params.confirmSave']='true';
		me.ajaxRequest.call(me,params);
	},
	groupResizeHandler:function (panelGroup,fieldGroup,optionID,width){
		var me=this;
		var fieldGroup=me.cmbGroupBy.getValue();
		var urlStr="cardView!changeOptionWidth.action";
		Ext.Ajax.request({
			disableCaching:true,
			url:urlStr,
			params:{
				optionID:optionID,
				width:width,
				fieldGroup:fieldGroup,
				queryType:me.model.queryContext.queryType,
				queryID:me.model.queryContext.queryID
			},
			success: function(data){

			},
			failure: function(type, error){
			}
		});
	},

	getPopupMenuItems:function(rowData,grid,index,record){
		var me=this;
		var items=[];
		items.push('-');
		items.push({
			text: getText('itemov.cardView.configCardContent'),
			iconCls:'btnConfig16',
			handler:function(){
				me.configCardContent.call(me);
			}
		});
		return items;
	},
	configCardContent:function(){
		var me=this;
		var urlStr='cardScreenEdit.action?backAction=itemNavigator.action';
		window.location.href=urlStr;
	}

});


Ext.define('com.trackplus.itemNavigator.GroupView',{
	extend: 'Ext.panel.Panel',
	config:{
		group:{}
	},
	cls:'cardGroupPanel',
	frame:true,
	autoScroll:true,
	width:250,
	margin:'0 2 0 0',
	constructor : function(config) {
		var me = this;
		me.callParent(arguments);
		//me.addEvents('closeGroup');
	},
	initComponent: function(){
		var me=this;
		me.title= me.group.label+' ('+me.group.number+')';
		var tools=new Array();
		tools.push({
			type:'close',
			tooltip: 'Close',
			handler: function(event, toolEl, panel){
				me.fireEvent.call(me,'closeGroup',me);
			}
		});
		Ext.apply(this, {
			tools: tools
		});
		me.callParent();
	}
});
