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

Ext.define('com.trackplus.itemNavigator.NavigableItem',{
	extend:'Ext.Base',
	itemDialogManager:null,
	usePosition:false,
	constructor : function(cfg) {
		var me = this;
		var config = cfg || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		this.initConfig(config);
		me.itemDialogManager=Ext.create('com.trackplus.itemNavigator.ItemDialogManager',{
			navigable:me,
			usePosition:me.usePosition
		});
	},

	/*abstract function*/
	navigate:function(workItemID,workItemIndex,dir){
	},
	selectItem:function(workItemID){
	},
	deselectItem:function(workItemID){
	},
	selectItemByIndex:function(workItemIndex){
	},
	deselectItemByIndex:function(workItemIndex){
	},
	itemChangeHandler:function(fields){
	},

	openItem:function(workItemID,actionID,workItemIndex,extraCfg){
		var me=this;
		var nextItem=me.navigate(workItemID,workItemIndex,'next',true);
		var prevItem=me.navigate(workItemID,workItemIndex,'prev',true);
		var navigateCtx={
			workItemID:workItemID,
			workItemIndex:workItemIndex,
			nextItem:nextItem,
			prevItem:prevItem
		};
		var itemActionCfg={
			workItemID:workItemID,
			actionID:CWHF.isNull(actionID)?-2:actionID,
			successHandler:me.successHandler,
			scope:me,
			modal:false,
			disabledNext:CWHF.isNull(nextItem),
			disabledPrev:CWHF.isNull(prevItem)
		};
		if(extraCfg){
			for(var x in extraCfg){
				itemActionCfg[x]=extraCfg[x];
			}
		}
		var itemAction=Ext.create('com.trackplus.item.ItemActionDialog',itemActionCfg);
		me.itemDialogManager.openDialog(itemAction,navigateCtx);
	},

	containsFields:function(myFields,changedFields){
		var me=this;
		if(changedFields){
			for(var i=0;i<changedFields.length;i++){
				if(me.containsOneField(myFields,changedFields[i])){
					return true;
				}
			}
		}
		return false;
	},
	containsOneField:function(myFields,fieldID){
		var me=this;
		for(var i=0;i<myFields.length;i++){
			if(myFields[i].reportField===fieldID){
				return true;
			}
		}
		return false;
	}
});

Ext.define('com.trackplus.itemNavigator.GridNavigableItem',{
	extend:'com.trackplus.itemNavigator.NavigableItem',
	/*abstract function*/
	getGrid:function(){
		return this.grid;
	},
	getFieldNameItemID:function(){
		return 'objectID';
	},
	navigate:function(workItemID,workItemIndex,dir){
		var me=this;
		var grid=me.getGrid();
		var fieldNameItemID=me.getFieldNameItemID();
		if(CWHF.isNull(grid)){
			return null;
		}
		var store =grid.getStore();
		var idx=-1;
		if(workItemIndex>=0&&workItemIndex<store.getCount()){
			var r=store.getAt(workItemIndex);
			if(r.data[fieldNameItemID]===workItemID){
				idx=workItemIndex;
			}
		}
		if(idx===-1){
			idx=me.findIndex(fieldNameItemID,workItemID);
		}
		if(idx===-1){
			//the item is not present anymore use workItemIndex;
			idx=workItemIndex;
			if(dir==='next'){
				//keep this item as next
				idx--;
			}
		}
		if(dir==='next'){
			if(idx===store.getCount()-1){
				return null;
			}
			idx++;
		}else{
			if(idx===0){
				return null;
			}
			idx--;
		}
		var record=store.getAt(idx);
		return{
			workItemID:record.data[fieldNameItemID],
			workItemIndex:idx
		};
	},
	selectItem:function(workItemID){
		var me=this;
		var grid=me.getGrid();
		if(CWHF.isNull(grid)||CWHF.isNull(workItemID)){
			return null;
		}
		return me.selectItemGrid(grid,workItemID);
	},
	selectItemGrid:function(grid,workItemID){
		var me=this;
		var store =grid.getStore();
		var workItemIDs = Ext.Array.from(workItemID);
		var records=new Array();
		var fieldNameItemID=me.getFieldNameItemID();
		for(var i=0;i<workItemIDs.length;i++){
			var idx=me.findIndex(fieldNameItemID,workItemIDs[i]);
			if (idx) {
				grid.getSelectionModel().select(idx);
				records.push(store.getAt(idx));
			}
		}
		return records;
	},
	deselectItem:function(workItemID){
		var me=this;
		var grid=me.getGrid();
		if(CWHF.isNull(grid)||CWHF.isNull(workItemID)){
			return null;
		}
		me.deselectItemGrid(grid,workItemID);
	},
	deselectItemGrid:function(grid,workItemID){
		var me=this;
		var store =grid.getStore();
		var workItemIDs = Ext.Array.from(workItemID);
		var fieldNameItemID=me.getFieldNameItemID();
		for(var i=0;i<workItemIDs.length;i++){
			var idx=me.findIndex(fieldNameItemID,workItemIDs[i]);
			if (idx) {
				grid.getSelectionModel().deselect(idx);
			}
		}
	},
	selectItemByIndex:function(workItemIndex){
		var me=this;
		var grid=me.getGrid();
		if(CWHF.isNull(grid)||CWHF.isNull(workItemIndex)){
			return null;
		}
		return me.selectItemGridByIndex(grid,workItemIndex);
	},
	selectItemGridByIndex:function(grid,workItemIndex){
		var store =grid.getStore();
		var workItemIndexes = Ext.Array.from(workItemIndex);
		var records=new Array();
		for(var i=0;i<workItemIndexes.length;i++){
			var idx=workItemIndexes[i];
			if (idx>=0&&idx<store.getCount()) {
				grid.getSelectionModel().select(idx);
				records.push(store.getAt(idx));
			}
		}
		return records;
	},

	/*private functions*/
	findIndex:function(fieldNameItemID, workItemID){
		var me=this;
		var grid=me.getGrid();
		var store =grid.getStore();
		var r;
		for(var i=0;i<store.getCount();i++){
			r=store.getAt(i);
			if(r.data[fieldNameItemID]===workItemID){
				return i;
			}
		}
		return -1;
	}

});


Ext.define('com.trackplus.itemNavigator.TreeNavigableItem',{
	extend:'com.trackplus.itemNavigator.NavigableItem',
	/*abstract function*/
	getTreeGrid:function(){
		return null;
	},
	navigate:function(workItemID,workItemIndex,dir){
		var me=this;
		var treeGrid=me.getTreeGrid();
		if(treeGrid){
			var node=treeGrid.getStore().getNodeById(workItemID);
			var nodeToSelect=null;
			if (node) {
				if(dir==='next'){
					nodeToSelect=me.findNext(node,true);
				}else{
					nodeToSelect=me.findPrev(node,true);
				}
			}else{
				//item is not present anymore on lis, try to get node by index
				node = me.findNodeByWorkItemIndex(workItemIndex);
				if(node){
					if(dir==='next'){
						nodeToSelect=node;
					}else{
						nodeToSelect=me.findPrev(node,true);
					}
				}
			}
			if(nodeToSelect){
				return{
					workItemID:nodeToSelect.data['workItemID'],
					workItemIndex:nodeToSelect.data['workItemIndex']
				}
			}
			return null;
		}
	},
	selectItem:function(workItemID){
		var me=this;
		var treeGrid=me.getTreeGrid();
		if(CWHF.isNull(treeGrid)||CWHF.isNull(workItemID)){
			return null;
		}
		return me.selectItemInTree(treeGrid,workItemID);
	},
	selectItemInTree:function(treeGrid,workItemID){
		var me=this;
		var workItemIDs = Ext.Array.from(workItemID);
		var nodeArrays=new Array();
		for(var i=0;i<workItemIDs.length;i++){
			var node = treeGrid.getStore().getNodeById(workItemIDs[i]);
			if (node) {
				treeGrid.selectPath(node.getPath());
				nodeArrays.push(node.data);
			}
		}
		return nodeArrays;
	},
	deselectItem:function(workItemID){
		var me=this;
		var treeGrid=me.getTreeGrid();
		if(CWHF.isNull(treeGrid)||CWHF.isNull(workItemID)){
			return null
		}
		me.deselectItemInTree(treeGrid,workItemID);
	},
	deselectItemInTree:function(treeGrid,workItemID){
		var me=this;
		var workItemIDs = Ext.Array.from(workItemID);
		for(var i=0;i<workItemIDs.length;i++){
			var node = treeGrid.getStore().getNodeById(workItemIDs[i]);
			if (node) {
				treeGrid.getSelectionModel().deselect(node);
			}
		}
	},

	selectItemByIndex:function(workItemIndex){
		var me=this;
		var treeGrid=me.getTreeGrid();
		if(CWHF.isNull(treeGrid)||CWHF.isNull(workItemIndex)){
			return null;
		}
		return me.selectItemByIndexInTree(treeGrid,workItemIndex);

	},
	selectItemByIndexInTree:function(treeGrid,workItemIndex){
		var me=this;
		var workItemIndexes = Ext.Array.from(workItemIndex);
		var nodeArrays=new Array();
		for(var i=0;i<workItemIndexes.length;i++){
			var node = me.findNodeByWorkItemIndex(workItemIndexes[i]);
			if (node) {
				treeGrid.selectPath(node.getPath());
				nodeArrays.push(node.data);
			}
		}
		return nodeArrays;
	},

	/*protected functions*/
	onStoreRefresh:function(store){
		var me=this;
		var root=store.getRootNode();
		me.updateNodeChildrenIndex(root,0);
	},
	updateNodeChildrenIndex:function(node,idx){
		var me=this;
		var children=node.childNodes;
		if(children){
			for(var i=0;i<children.length;i++){
				var child=children[i];
				if(!child.data['group']===true){
					child.data['workItemIndex']=idx++;
				}
				idx=me.updateNodeChildrenIndex(child,idx);
			}
		}
		return idx;
	},

	/*private functions*/
	findPrev:function(node){
		var me=this;
		var nodeToSelect=null;
		var parentNode=node.parentNode;
		if(node.isFirst()){
			if(parentNode.isRoot()){
				nodeToSelect=null;
			}else{
				if(parentNode.data['workItemID']){
					nodeToSelect=parentNode;
				}else{
					nodeToSelect=me.findPrev(parentNode);
				}
			}
		}else{
			var myIndex=parentNode.indexOf(node);
			var prevSibling=parentNode.getChildAt(myIndex-1);
			if(prevSibling.data['workItemID']){
				nodeToSelect=prevSibling;
			}else{
				nodeToSelect=me.findPrev(prevSibling);
			}
		}
		return nodeToSelect;
	},
	findNext:function(node,includeChildren){
		var me=this;
		var nodeToSelect=null;
		if(node.hasChildNodes()&&includeChildren===true){
			nodeToSelect=node.getChildAt(0);
		}else{
			var parentNode=node.parentNode;
			if(node.isLast()){
				if(parentNode.isRoot()){
					nodeToSelect=null;
				}else{
					nodeToSelect=me.findNext(parentNode,false);
				}
			}else{
				var myIndex=parentNode.indexOf(node);
				var nextSibling=parentNode.getChildAt(myIndex+1);
				if(nextSibling.data['workItemID']){
					nodeToSelect=nextSibling;
				}else{
					nodeToSelect=me.findNext(nextSibling,true);
				}
			}
		}
		return nodeToSelect;
	},
	findNodeByWorkItemIndex:function(workItemIndex){
		var me=this;
		var selectedNode=null;
		var maxIndex=0;
		var maxNode=0;
		var treeGrid=me.getTreeGrid();
		if(treeGrid){
			var root=treeGrid.getStore().getRootNode();
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
		}
		return null;
	}
});
