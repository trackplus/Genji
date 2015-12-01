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

Ext.define('com.trackplus.itemNavigator.WBSViewPlugin',{
	extend: 'com.trackplus.itemNavigator.SimpleTreeGridViewPlugin',
	disableSort:true,
	WBS_SORT_ASCENDING:true,
	getTreeViewPlugins:function(){
		return [{
			ptype: 'gridviewdragdrop',
			dragText: getText('itemov.wbsView.lbl.dragText'),
			dragGroup: 'itemToCategory',
			dropGroup: 'itemToCategory',
			enableDrop: true
		}];
	},
	initMyListeners:function(){
		var me=this;
		me.callParent();
		me.view.getView().addListener('beforedrop',me.onGridBeforeDrop,me);
	},
	createFields:function(){
		var me=this;
		var fields=me.callParent();
		fields.push({name:'wbs'});
		return fields;
	},

	getSorters:function(){
		var me=this;
		var sortField="wbs";
		//var sortField = "f_so27";
		var sortDirection;
		if(me.WBS_SORT_ASCENDING===true){
			sortDirection='ASC'
		}else{
			sortDirection='DESC'
		}
		return [{
			property:sortField,
			direction :sortDirection
		}];
	},
	createStore:function(){
		var me=this;
		var store=me.callParent(arguments);
		store.sort();
		return store;
	},
	expandGroup:function(groupID,expanded){
		Ext.Ajax.request({
			url: 'reportExpand!expandGroupWBS.action',
			params:{
				expanded:expanded,
				groupID:groupID
			}
		});
	},

	onGridBeforeDrop:function(node,data, overModel, dropPosition){
		var me=this;
		var targetProjectID=overModel.data.projectID;
		var validWorkItems=[];
		var otherProjectItems=[];
		for(var i=0;i<data.records.length;i++){
			var record=data.records[i];
			if(record.data['group']===true){
				continue;
			}
			var sourceProjectID=record.data['projectID'];
			if(sourceProjectID!==targetProjectID){
				otherProjectItems.push(record);
			}else{
				validWorkItems.push(record);
			}
		}
		if(validWorkItems.length===0){
			var msg=getText('itemov.wbsView.err.notTheSameProject');
			Ext.MessageBox.show({
				title: '',
				msg: msg,
				width: 200,
				buttons: Ext.MessageBox.OK,
				icon:Ext.MessageBox.ERROR
			});
			return false;
		}
		while(data.records.length>0){
			data.records.pop();
		}

		me.view.setLoading(true);
		var workItems='';
		for(i=0;i<validWorkItems.length;i++){
			data.records.unshift(validWorkItems[i]);
			workItems+=validWorkItems[i].data['workItemID'];
			if(i<validWorkItems.length-1){
				workItems+=",";
			}
		}
		var before=(dropPosition==="before");
		if(me.WBS_SORT_ASCENDING===false){
			before=!before;
		}
		var params={
			workItems:workItems,
			targetWorkItemID:overModel.data['workItemID'],
			before:before
		};
		Ext.Ajax.request({
			url: "itemNavigator!changeWBS.action",
			disableCaching:true,
			success: function(){
				me.view.setLoading(false);
				me.fireEvent.call(me,'datachange');
			},
			failure: function(){
				me.view.setLoading(false);
				alert("failure");
			},
			method:'POST',
			params:params
		});
		return true;
	},
	getPopupMenuItems:function(rowData,grid,index,record){
		var me=this;
		var items=[];
		if (rowData.editable) {
			items.push({
					text: getText('common.btn.indent'),
					iconCls:'itemAction_indent16',
					handler:function(){
						me.indent.call(me,rowData,grid,index,record);
					}
			});
			items.push({
				text: getText('common.btn.outdent'),
				iconCls:'itemAction_outdent16',
				handler:function(){
					me.outdent.call(me,rowData,grid,index,record);
				}
			});
		}
		return items;
	},

	indent:function(rowData,grid,index,record){
		var me=this;
		var parentNode=record.parentNode;
		var recordIndex=parentNode.indexOf(record);
		if(recordIndex===0){
			alert(getText('itemov.wbsView.err.noIndentPossible'));
			return false;
		}
		grid.setLoading(true);
		var supNode=parentNode.getChildAt(recordIndex-1);
		var params={
			nodeType:me.itemNavigatorController.nodeType,
			nodeObjectID:me.itemNavigatorController.nodeObjectID,
			descriptorID:me.itemNavigatorController.selectedIssueViewDescriptor.id,
			queryType:me.itemNavigatorController.model.queryType,
			queryID:me.itemNavigatorController.model.queryID,
			workItemID:record.data['workItemID'],
			targetWorkItemID:supNode.data['workItemID']
		};
		Ext.Ajax.request({
			url: "itemNavigator!indent.action",
			disableCaching:true,
			success: function(response){
				grid.setLoading(false);
				var resp = Ext.decode(response.responseText);
	        	if (resp.success==false) {
	        		com.trackplus.util.showError(resp);
	        	} else {
					parentNode.removeChild(record);
					supNode.data['leaf']=false;
					supNode.appendChild(record);
					supNode.expand();
					me.fireEvent.call(me,'datachange');
	        	}
			},
			failure: function(response){
				grid.setLoading(false);
				com.trackplus.util.showError(response);
			},
			method:'POST',
			params:params
		});
	},
	outdent:function(rowData,grid,index,record){
		var me=this;
		var parentNode=record.parentNode;
		var rootNode=grid.store.getRootNode();
		if(parentNode===rootNode){
			CWHF.showMsgError(getText("itemov.wbsView.err.noOutdentPossible"));
			return false;
		}
		var parentIsGroup=parentNode.data['group'];
		if(parentIsGroup===true){
			CWHF.showMsgError(getText("itemov.wbsView.err.noOutdentPossible"));
			return false;
		}
		grid.setLoading(true);
		var params={
			nodeType:me.itemNavigatorController.nodeType,
			nodeObjectID:me.itemNavigatorController.nodeObjectID,
			descriptorID:me.itemNavigatorController.selectedIssueViewDescriptor.id,
			queryContextID:me.itemNavigatorController.model.queryContext.id,
			workItemID:record.data['workItemID']
		};
		Ext.Ajax.request({
			url: "itemNavigator!outdent.action",
			disableCaching:true,
			success: function(){
				var grandParent=parentNode.parentNode;
				var recordIndex=grandParent.indexOf(parentNode);
				grandParent.insertChild(recordIndex+1,record);
				if(!parentNode.hasChildNodes()){
					parentNode.data['leaf']=false;
				}
				grid.setLoading(false);
				me.fireEvent.call(me,'datachange');
			},
			failure: function(){
				grid.setLoading(false);
				alert("failure");
			},
			method:'POST',
			params:params
		});
	}
});

