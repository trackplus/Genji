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


Ext.define('com.trackplus.admin.user.FiltersInUserMenus',{
	extend:'com.trackplus.admin.GridConfig',
	config: {
			urlStore:null,
			//person or group ID
			personIDs: null
			//whether it is group or person
			//group: null
			},
	urlStore:null,
	allowMultipleSelections:true,
	personIDs: null,
	//group: null,
	fields:[{name: 'filterID',type:'int'},
			{name: 'filterLabel',type:'string'},
			{name: 'personID',type:'int', useNull:true},
			{name: 'personName', type: 'string'},
			{name: 'first', type: 'boolean'}
	],
	confirmDeleteEntity:true,
	baseAction: "filtersInMenu",
	labelWidth: 100,
	editWidth: 400,
	editHeight: 125,

	constructor: function(config) {
		var config = config || {};
		this.initialConfig = config;
		config["features"] = this.getGridFeatures();
		config['region']='center';
		config['style']={
			borderTop:'1px solid #D0D0D0',
			borderBottom:'1px solid #D0D0D0'
		};
		Ext.apply(this, config);
		this.init();
	},

	getEntityLabel:function(){
		return getText("admin.user.manage.lbl.filterAssignments");
	},

	getAddIconCls: function() {
		return "add16";
	},

	getDeleteIconCls: function() {
		return "delete16";
	},

	/**
	 * Gets the column model array for the grid.
	 * Alternative to the columnModel config property (if "this" keyword is needed in column model)
	 */
	getColumnModel:function() {
		return [{header: getText('common.lbl.filter'), flex:1,
					dataIndex: 'filterLabel', sortable:true, renderer:this.filterRenderer},
				{header:getText('admin.user.manage.lbl.filterAssignments.user'),
					flex:1, dataIndex:'personName', sortable:false}];
	},

	/**
	 * Render the inherited rows as grey
	 */
	filterRenderer: function(value, metadata, record) {
	    if (!record.data["first"]) {
	        return "";
	    }
	    return value;
	},

	getGridSelectionModel: function() {
		return Ext.create('Ext.selection.CheckboxModel', {mode:"MULTI"});
	},

	/**
	 * Defined typically if there is no "classic" toolbar available (like grid is in a popup)
	 */
	getDockedItems: function() {
		return [{
			xtype: 'toolbar',
			dock: 'top',
			margin:'4 0 0 0',
			items: [this.actionAdd, this.actionDelete]
		}];
	},

	/**
	 * Get extra parameters for grid load
	 */
	getLoadGridParams:function() {
		return {personIDs:this.personIDs};
	},

	/**
	 * Parameters for adding a new entity
	 * Specify extra parameters if needed
	 * (like "defaultSettings" for "my" and "default" automail settings)
	 */
	getAddParams: function() {
		return {personIDs:this.personIDs};
	},

	getGridFeatures: function() {
		return [{
			ftype: 'filters',
			encode: false, // json encode the filter query
			local: true,   // defaults to false (remote filtering)
			filters: [{
					type: 'string',
					dataIndex: 'filterLabel'
				}/*,{
					type: 'string',
					dataIndex: 'roleLabel'
				}*/
			]
		}];
	},

	/**
	 * Get the ID based from the recordData
	 *  No objectID primary key in tacl table
	 */
	getRecordID: function(recordData) {
		if (recordData!=null) {
	        var filterID = recordData["filterID"];
	        var personID = recordData["personID"];
	        if (personID==null) {
	            return filterID;
	        } else {
	            return filterID + "|" + personID;
	        }
		}
		return null;
	},

	/**
	 * Parameters for deleting entity
	 * recordData: the selected entity data
	 * Even if there is more than one entity selected for delete
	 * this method is called for each selected entity separately
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	getDeleteParams: function(selectedRecords, extraConfig) {
		var params=new Object();
		var selectionParam = this.getSelectionParam(selectedRecords, extraConfig);
		params["unassign"] = selectionParam;
	    params["personIDs"] = this.personIDs;
		return params;
	},

	createEditForm:function(entityJS,operation){
		var disabled = false;
		if (operation!="add" && !entityJS["direct"]) {
			disabled = true;
		}
		return Ext.create('Ext.form.FormPanel',{
			url:this.baseAction + '!save.action',
			autoScroll: true,
			border: false,
			margin: '0 0 0 0',
			bodyStyle:{
				padding:'10px'
			},
			style:{
				borderBottom:'1px solid #D0D0D0'
			},
			defaults : {
				labelStyle:'overflow: hidden;',
				msgTarget : 'side',
				anchor : '100%'
			},
			items: [CWHF.createMultipleTreePicker(null,
					"filterIDs", [], null,
					{allowBlank:false,
					disabled:disabled,
	                margin:'0 0 5 0',
	                useRemoveBtn:false,
	                useNull:true,
	                useTooltip:false,
	                fieldLabel:getText("common.lbl.filter")
				})]
		});
	},

	afterLoadForm:function(data, panel) {
		var filterControl = panel.getComponent("filterIDs");
	    filterControl.updateData(data["filterTree"]);
	}
});
