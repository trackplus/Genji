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

Ext.define("com.trackplus.util.MultipleTreePicker",{
	extend:"com.trackplus.util.AbstractMultiplePicker",
	displayField:'text',
	valueField:'id',
	createStore:function(){
		var me=this;
		var options=me.getOptions();
		return  Ext.create("Ext.data.TreeStore", {
			fields: [
				{name : "id", mapping : "id", type: "string"},
				{name : "text", mapping : "text", type: "string"},
				{name : "leaf", mapping : "leaf", type: "boolean"},
				{name : "iconCls", mapping : "iconCls", type: "string"},
				{name: "selectable", mapping: "selectable", type: "boolean", allowNull: true}
			],
			root: {
				expanded: true,
				children:(CWHF.isNull(options)?[]:options)
			},
			originalData:CWHF.isNull(options)?[]:options
		});
	},
	updateMyOptions:function(options){
		var me=this;
		var newOptions=Ext.clone(options);
		me.setOptions(newOptions);
		if(me.store){
			var rootNode=me.store.getRootNode();
			rootNode.removeAll(false);
			if(newOptions&&newOptions.length>0){
				rootNode.appendChild(newOptions);
			}
		}
	},
	createBoundList:function(){
		var treeConfig = {
			maxHeight: 200,
			height:200,
			store:this.store,
			rootVisible: false,
			containerScroll: true,
			autoScroll: true,
			cls:"simpleTree",
			border:false,
			//selModel : Ext.create('Ext.selection.CheckboxModel', {mode:"MULTI"}),
			plugins: [{
				ptype: 'treefilter',
				allowParentFolders: true
			}]
		};
		var tree= Ext.create("Ext.tree.Panel", treeConfig);
		return tree;
	},
	addBoundListListeners:function(){
		var me=this;
		me.boundList.addListener('checkchange',me.treeCheckChange,me);
		me.boundList.on('afterrender',me.onListRefresh,me);
		me.boundList.on('beforeselect',me.onBeforeselect,me);
	},
	findRecord: function(field, value) {
		var me=this;
		var store =me.getStore();
		if(CWHF.isNull(store)){
			store=me.createStore();
			me.setStore(store);
		}
		var nodeToSelect=store.getNodeById(value);
		if(nodeToSelect){
			return nodeToSelect;
		}else{
			return false;
		}
	},
	getRecordDisplayValue:function(record){
		return record.data.text;
	},
	getRecordValue:function(record){
		return record.data.id;
	},
	syncSelection: function() {
		var me = this;
		var values=new Array();
		if(me.valueModels){
			for(var i=0;i<me.valueModels.length;i++){
				values.push(me.getRecordValue(me.valueModels[i]));
			}
		}
		if(this.boundList){
			this.boundList.getRootNode().cascadeBy(function(){
				var checked=this.get('checked');
				if(!CWHF.isNull(checked)){
					this.set( 'checked', Ext.Array.contains(values, this.data.id));
				}
			});
		}
	},
	onBeforeselect:function(rowModel, record, index, eOpts){
		var me=this;
		var selectable=record.data['selectable'];
		if(selectable===false){
			return false;
		}
		return true;
	},
	treeCheckChange: function(node, checked, options) {
		var me=this;
		var records = this.boundList.getView().getChecked();
		me.selectionChange(records);
	},
	selectAll:function(){
		var me=this;
		var value=new Array();
		if (me.boundList) {
			this.boundList.getRootNode().cascadeBy(function(){
				if(this.get('checked')!==undefined){
					this.set( 'checked', true);
					value.push(this);
				}
			});
			this.boundList.expandAll();
		}
		me.setValue(value);
	},
	getSelectableItemsCount:function(){
		var me=this;
		var count=0;
		if (me.boundList) {
			this.boundList.getRootNode().cascadeBy(function(){
				if(this.get('checked')!==undefined){
					count++;
				}
			});
		}
		return count;
	},
	clearSelection:function(){
		var me=this;
        if (me.boundList) {
            this.boundList.getRootNode().cascadeBy(function(){
                if(this.get('checked')!==undefined){
                    this.set( 'checked', false );
                }
            });
        }
		me.setValue(null);
        if (me.searchField) {
		    me.searchField.setValue(null);
        }
	},
	filter : function(value){
		var me = this;
		if (value.length > 0) {
			me.boundList.filter(value);
		}else{
			me.clearFilter();
		}
	},
	clearFilter:function(){
		var me=this;
		me.boundList.clearFilter();
	}
});

Ext.define('TreeFilter', {
	extend: 'Ext.AbstractPlugin'
	, alias: 'plugin.treefilter'

	, collapseOnClear: true                                                 // collapse all nodes when clearing/resetting the filter
	, allowParentFolders: false                                             // allow nodes not designated as 'leaf' (and their child items) to  be matched by the filter

	, init: function (tree) {
		var me = this;
		me.tree = tree;

		tree.filter = Ext.Function.bind(me.filter, me);
		tree.clearFilter = Ext.Function.bind(me.clearFilter, me);
	},
	filter: function (value, property, re) {
		var me = this;
		if (Ext.isEmpty(value)) {                                           // if the search field is empty
			me.clearFilter();
			return;
		}
		var escapeRe      = Ext.String.escapeRegex;
		//value = '^' + escapeRe(value);//start with
		value = escapeRe(value);
		var tree = me.tree
			, matches = []                                                  // array of nodes matching the search criteria
			, root = tree.getRootNode()                                     // root node of the tree
			, property = property || 'text'                                 // property is optional - will be set to the 'text' propert of the  treeStore record by default
			, re = re || new RegExp(value, "ig")                            // the regExp could be modified to allow for case-sensitive, starts  with, etc.
			, visibleNodes = []                                             // array of nodes matching the search criteria + each parent non-leaf  node up to root
			, viewNode;



		tree.expandAll();                                                   // expand all nodes for the the following iterative routines

		// iterate over all nodes in the tree in order to evalute them against the search criteria
		root.cascadeBy(function (node) {
			if (node.get(property).match(re)) {                             // if the node matches the search criteria and is a leaf (could be  modified to searh non-leaf nodes)
				matches.push(node);                                         // add the node to the matches array
			}
		});

		if (me.allowParentFolders === false) {                              // if me.allowParentFolders is false (default) then remove any  non-leaf nodes from the regex match
			Ext.each(matches, function (match) {
				if (!match.isLeaf()) {
					Ext.Array.remove(matches, match);
				}
			});
		}

		Ext.each(matches, function (item, i, arr) {                         // loop through all matching leaf nodes
			root.cascadeBy(function (node) {                                // find each parent node containing the node from the matches array
				if (node.contains(item) === true) {
					visibleNodes.push(node);                                // if it's an ancestor of the evaluated node add it to the visibleNodes  array
				}
			});
			if (me.allowParentFolders === true && !item.isLeaf()) {        // if me.allowParentFolders is true and the item is  a non-leaf item
				item.cascadeBy(function (node) {                            // iterate over its children and set them as visible
					visibleNodes.push(node);
				});
			}
			visibleNodes.push(item);                                        // also add the evaluated node itself to the visibleNodes array
		});

		root.cascadeBy(function (node) {                                    // finally loop to hide/show each node
			viewNode = Ext.fly(tree.getView().getNode(node));               // get the dom element assocaited with each node
			if (viewNode) {                                                 // the first one is undefined ? escape it with a conditional
				viewNode.setVisibilityMode(Ext.Element.DISPLAY);            // set the visibility mode of the dom node to display (vs offsets)
				viewNode.setVisible(Ext.Array.contains(visibleNodes, node));
			}
		});
	}

	, clearFilter: function () {
		var me = this
			, tree = this.tree
			, root = tree.getRootNode();

		if (me.collapseOnClear) {
			tree.collapseAll();                                             // collapse the tree nodes
		}
		root.cascadeBy(function (node) {                                    // final loop to hide/show each node
			viewNode = Ext.fly(tree.getView().getNode(node));               // get the dom element assocaited with each node
			if (viewNode) {                                                 // the first one is undefined ? escape it with a conditional and show  all nodes
				viewNode.show();
			}
		});
	}
});



