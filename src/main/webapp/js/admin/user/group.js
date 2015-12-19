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

/**
 * Class for role and account assignments for project
 */
Ext.define("com.trackplus.admin.user.Group", {
	extend:"com.trackplus.admin.TreeDetailAssignment",
	xtype: "group",
    controller: "group",
	config: {
		rootID: '_'
	},

	getBaseServerAction: function() {
		return "group";
	},
	
	allowMultipleNodeSelections: true,
	/**
	 * Whether the tree has double click listener
	 */
	treeHasItemdblclick: true,

	/**
	 * actions
	 */
	actionAddGroup: null,
	actionEditGroup: null,
	actionShowAssignments: null,
	actionDeleteGroup: null,
	actionRemovePerson: null,

		
	getEntityLabel: function(extraConfig) {
		return getText("admin.user.group.lbl.group");
	},

	getAddIconCls: function() {
		return "groupAdd";
	},

	getEditIconCls: function() {
		return "groupEdit";
	},

	getRoleAssignCls: function() {
		return "roleAssign";
	},

	/**
	 * Initialize all possible actions
	 */
	initActions: function() {
		this.actionAddGroup = CWHF.createAction(this.getActionTooltip(this.getAddTitleKey()), this.getAddIconCls(),
				"onAddGroup", {labelIsLocalized:true});
		this.actionEditGroup = CWHF.createAction(this.getActionTooltip(this.getEditTitleKey()), this.getEditIconCls(),
				"onEditGroup", {labelIsLocalized:true, disabled:true});
		this.actionShowAssignments = CWHF.createAction("admin.user.group.lbl.roleAssignments", this.getRoleAssignCls(),
				"onShowAssignments", {disabled:true});
		this.actionDeleteGroup = CWHF.createAction(this.getActionTooltip(this.getDeleteTitleKey()), this.getDeleteIconCls(),
				"onDeleteGroup", {labelIsLocalized:true, disabled:true});
		this.actionRemovePerson = CWHF.createAction(getText(this.getRemoveTitleKey(), getText("admin.user.lbl.user")),
				this.getDeleteIconCls(), "onRemovePerson");
		this.actions=[this.actionAddGroup, this.actionEditGroup, this.actionDeleteGroup, this.actionShowAssignments];
	},

	/**
	 * Context menu for tree
	 */
	getTreeContextMenuActions: function(selectedRecord, selectionIsSimple) {
		if (selectionIsSimple) {
			if (selectedRecord.isLeaf()) {
				return [this.actionRemovePerson];
			} else {
				return [this.actionEditGroup, this.actionShowAssignments, this.actionDeleteGroup];
			}
		} else {
			return [this.actionDeleteGroup];
		}
	},

	/**
	 * Create the edit
	 */
	/*onTreeNodeDblClick: function(view, record) {
		var leaf = record.isLeaf();
		if (!leaf) {
			this.onEditGroup();
		}
	},*/

	/**
	 * Which actions to enable/disable depending on tree selection
	 * Implement only if replaceToolbarOnTreeNodeSelect is false
	 */
	getToolbarActionChangesForTreeNodeSelectionChange: function(selectedNodes) {
		var noSelection = false;
		var multipleSelection = false;
		if (CWHF.isNull(selectedNodes)) {
			noSelection = true;
		} else {
			multipleSelection = selectedNodes.length>1;
		}
		this.actionEditGroup.setDisabled(noSelection || multipleSelection);
		this.actionShowAssignments.setDisabled(noSelection || multipleSelection);
		this.actionDeleteGroup.setDisabled(noSelection);
	},

	/**
	 * The message to appear first time after selecting this menu entry
	 * Is should be shown by selecting the root but the root is typically not visible
	 */
	getRootMessage: function() {
		return getText("menu.admin.users.groups.tt");

	},

	/**
	 * Handler for adding a new screen
	 */
	/*onAddGroup: function() {
		var title = this.getTitle(this.getAddTitleKey(), {isLeaf:false});
		return this.onAddEdit(title, {add:true}, {add:true});
	},*/

	/**
	 * Handler for editing a screen
	 */
	/*onEditGroup: function() {
		var title = this.getTitle(this.getEditTitleKey(), {isLeaf:false});
		var loadParams = this.getEditParams();
		var submitParams = this.getEditParams();
		return this.onAddEdit(title, loadParams, submitParams);
	},*/

	/*onShowAssignments: function() {
		var title = getText("admin.user.group.lbl.roleAssignments");
		var submitParams = this.getEditParams();
		var load = {loadHandler: this.showAssignmentsLoadHandler};
		var personID = null;
		var recordData = this.getSingleSelectedRecordData(true);
		if (recordData) {
			personID = recordData['id'];
		}
		var windowParameters = {title:title,
			width:500,
			height:500,
			load:load,
			//submit:submit, no submit
			cancelButtonText: getText("common.btn.done"),
			formPanel: this.getAssignmentGrid(personID)};
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
		windowConfig.showWindowByConfig(this);
	},*/

	/**
	 * Empty (but not null) function to avoid the calling of getForm().load() in windowConfig
	 * loadUrl and loadParams is not specified because the data will be loaded through the grid's store
	 */
	/*showAssignmentsLoadHandler: function(loadUrl, loadParams) {

	},*/

	/*getAssignmentGrid: function(personID) {
		var gridComponent = Ext.create("com.trackplus.admin.user.UserRolesInProject",
				{personID:personID, group:true});
		//var grid = gridComponent.getGrid();
		//grid.store.load();
		return [gridComponent];
	},*/

	/**
	 * Parameters for editing an existing entity
	 * recordData: the selected entity data
	 */
	/*getEditParams: function() {
		var recordData = this.getSingleSelectedRecordData(true);
		if (recordData) {
			var nodeID = recordData['id'];
			if (nodeID) {
				return {node:nodeID};
			}
		}
	},*/

	/**
	 * Handler for add/edit a node/row
	 * title: 'add'/'edit'/'copy'
	 * recordData: the selected record (tree node data or grid row data)
	 * isLeaf: whether to add a leaf or a folder
	 * add: whether it is add or edit
	 * fromTree: operations started from tree or from grid
	 * loadParams
	 * submitParams
	 * refreshParams
	 * refreshParamsFromResult
	 */
	/*onAddEdit: function(title, loadParams, submitParams) {
		var loadUrl = this.getBaseAction() + '!edit.action';
		var load = {loadUrl:loadUrl, loadUrlParams:loadParams};
		var submitUrl = this.getBaseAction() + '!save.action';
		var submit = {submitUrl:submitUrl,
					submitUrlParams:submitParams,
					submitButtonText:getText('common.btn.save'),
					refreshAfterSubmitHandler:com.trackplus.util.RefreshAfterSubmit.refreshTreeAfterSubmit,
					refreshParametersBeforeSubmit:{nodeIDToReload: this.getRootID()},
					refreshParametersAfterSubmit:[{parameterName:'nodeIDToSelect', fieldNameFromResult:'node'},
												{parameterName:'reloadTree', fieldNameFromResult:'reloadTree'}]};
		var items = this.getPanelItems();
		var windowParameters = {title:title,
			width:this.editWidth,
			height:this.editHeight,
			load:load,
			submit:submit,
			items:items};
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
		windowConfig.showWindowByConfig(this);
	},*/


	/*getPanelItems: function() {
		return [CWHF.createTextField('common.lbl.name','name',
				{anchor:'95%', allowBlank:false, labelWidth:this.labelWidth, maxLength:60, enforceMaxLength:true}),
				{xtype: 'fieldset',
				title: getText('admin.user.group.lbl.groupFlags'),
					collapsible: false,
					layout: 'anchor',
					anchor:'95%',
					height: 145,
					items: [CWHF.createCheckbox("admin.user.group.lbl.groupFlagOriginator", "originator", {labelWidth:this.labelWidth}),
							CWHF.createCheckbox("admin.user.group.lbl.groupFlagManager", "manager", {labelWidth:this.labelWidth}),
							CWHF.createCheckbox("admin.user.group.lbl.groupFlagResponsible", "responsible", {labelWidth:this.labelWidth}),
							CWHF.createCheckbox("admin.user.group.lbl.groupFlagJoinNewUserToThisGroup", "joinNewUserToThisGroup", {labelWidth:this.labelWidth})]}];
	},*/

	/*onRemovePerson: function() {
		this.reloadAssigned(this.getBaseAction()+"!unassign.action", {})
	},

	reload: com.trackplus.util.RefreshAfterSubmit.refreshTreeAfterSubmit,

	getReloadParamsAfterDelete: function(selectedRecords, extraConfig, responseJson) {
	    var reloadParams = {reloadTree:true};
	    if (selectedRecords && selectedRecords.length>0) {
	        //we suppose that only one selection is allowed in tree
	    	var selNode = selectedRecords[0];
	    	reloadParams["nodeIDToReload"] = selNode.parentNode.data.id;
	    	if (selectedRecords.length===1) {
	    		//one group was deleted: select the previous or next if exist
	            if (selNode) {
	                var previousSibling = selNode.previousSibling;
	                if (previousSibling) {
	                    reloadParams["nodeIDToSelect"] = previousSibling.data.id;
	                } else {
	                    var nextSibling = selNode.nextSibling;
	                    if (nextSibling) {
	                        reloadParams["nodeIDToSelect"] = nextSibling.data.id;
	                    } else {
	                        reloadParams["resetDetail"] = true;
	                    }
	                }
	            } else {

	            }
	    	} else {
	    		//more than one group was selected to delete
	    		reloadParams["resetDetail"] = true;
	    	}
	    }
	    return reloadParams;
	},*/

	/**
	 * Get the detail part after selecting a tree node
	 */
	/*protected*//*loadDetailPanel: function(node, leaf, opts) {
		if (this.selectionIsSimple(true)) {
			this.loadSimpleDetailPanel(node, false);
		} else {
			this.resetDetailPanel();
		}
	},*/

	/*onDeleteGroup: function() {
		var selectedRecords = this.getSelection(true);
		if (selectedRecords) {
			this.deleteHandler(selectedRecords, {fromTree:true});
		}
	},*/

	/**
	 * Url for deleting an entity
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	/*getDeleteUrl: function(extraConfig){
		return this.getBaseAction() + '!delete.action';
	},*/

	/*getDeleteParams: function(selectedRecords, extraConfig) {
		return this.getEditParams();
	},*/

	/*getDeleteParamName: function() {
	    return "selectedGroupIDs";
	},*/

	getGridFields: function(record) {
		return [{name:'id', type:'int'},
				{name:'userName', type:'string'},
				{name:'name', type:'string'},
				{name:'active', type:'boolean'},
				{name:'activeLabel', type:'string'},
				{name:'employeeId', type:'string'},
				{name:'department', type:'string'}];
	},

	getColumnModel: function() {
		return [{text:getText('admin.user.profile.lbl.userName'),
			flex:2, dataIndex:'userName', sortable:true,
			renderer:this.renderer,
			filter: {
	            type: "string"
	        }
		}, {
			text:getText('common.lbl.name'),
			flex:2, dataIndex:'name', sortable:true,
			renderer:this.renderer,
			filter: {
	            type: "string"
	        }
		}, {
			text:getText('admin.user.profile.lbl.department'),
			flex:2, dataIndex:'department', sortable:true,
			renderer:this.renderer,
			filter: {
	            type: "string"
	        }
		}, {
			text:getText('admin.user.profile.lbl.employeeId'),
			flex:1, dataIndex:'employeeId', sortable:true,
			renderer:this.renderer, hidden: true,
			filter: {
	            type: "string"
	        }
		}, {
			text:getText('admin.user.manage.lbl.activ'),
			flex:1, dataIndex:'activeLabel', sortable:true,
			renderer:this.renderer, hidden: true,
			filter: {
				type: "list",
				options: [getText("common.boolean.Y"),
							getText("common.boolean.N")]
			}
		}];
	},

	/**
	 * Render the inherited rows as grey
	 */
	renderer: function(value, metadata, record) {
		if (!record.data["active"]) {
			metadata.style = 'color:#909090';
		}
		return value;
	},

	hideGridHeaders: function() {
		return false;
	},

	enableColumnHide: function() {
		return true;
	}

	/*getDetailWidth: function() {
		return 800;
	},

	getDetailHeight: function() {
		return 600;
	},

	getMinWidth: function() {
		return 425;
	}*/
});
