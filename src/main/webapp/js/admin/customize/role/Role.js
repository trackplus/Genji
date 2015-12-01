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

Ext.define("com.trackplus.admin.customize.role.Role", {
	extend : "com.trackplus.admin.GridBase",
	xtype: "role",
    controller: "role",
	columns : [
	        {
	            text : getText('admin.customize.role.lbl.role'),
	            width : 175,
	            dataIndex : 'name',
	            id : 'name',
	            sortable : true,
	            filter: {
		            type: "string"
		        }
	        },
	        {
	            xtype : 'templatecolumn',
	            text : getText('admin.customize.role.lbl.accessFlags'),
	            tpl : new Ext.XTemplate(
	                    '<table><tr>',
	                    '<td valign="top">',
	                    '<tpl for="accessFlags.fullAccessFlags">',
	                    '<div><input type="checkbox" disabled="disabled"  style="vertical-align: middle; margin: 0px;" ',
	                    '<tpl if="selected">', 'checked="true"', '</tpl>',
	                    'value="true"/> {label}</div>', '</tpl>', '</td>', '</tr></table>'),
	            width : 175,
	            sortable : false
	        },
	        {
	            xtype : 'templatecolumn',
	            text : getText('admin.customize.role.lbl.inSelections'),
	            tpl : new Ext.XTemplate(
	                    '<table><tr>',
	                    '<td valign="top">',
	                    '<tpl for="accessFlags.raciRoles">',
	                    '<div><input type="checkbox" disabled="disabled"  style="vertical-align: middle; margin: 0px;" ',
	                    '<tpl if="selected">', 'checked="true"', '</tpl>',
	                    'value="true"/> {label}</div>', '</tpl>', '</td>', '</tr>', '</table>'),
	            width : 175,
	            sortable : false
	        },
	        {
	            xtype : 'templatecolumn',
	            text : getText('admin.customize.role.lbl.allowedListTypes'),
	            tpl : new Ext.XTemplate('<table>', '<tpl for="issueTypesAssigned">',
	                    '<tr><td><img src="optionIconStream.action?fieldID=-2&optionID={id}"></td>',
	                    '<td style="padding:2px">{label}</td></tr>', '</tpl>', '</table>'
	            ),
	            width : 175,
	            sortable : false
	        },
	        {
	            xtype : 'templatecolumn',
	            text : getText('admin.customize.role.fieldsRestrictions.lbl.hiddenFields'),
	            tpl : new Ext.XTemplate('<table>', '<tpl for="noReadFields">',
	                    '<td style="padding:2px">{label}</td></tr>', '</tpl>', '</table>'),
	            width : 175,
	            sortable : false
	        },
	        {
	            xtype : 'templatecolumn',
	            text : getText('admin.customize.role.fieldsRestrictions.lbl.readOnlyFields'),
	            tpl : new Ext.XTemplate('<table>', '<tpl for="noEditFields">',
	                    '<td style="padding:2px">{label}</td></tr>', '</tpl>', '</table>'),
	            width : 175,
	            sortable : false
	        }

	],
	
	initComponent : function() {
		this.fields = [{
				    name : 'id',
				    type : 'int'
					}, {
				    name : 'name',
				    type : 'string'
					}, {
				    name : 'accessFlags'
					}, {
				    name : 'issueTypesAssigned'
					}, {
				    name : 'noReadFields'
					}, {
				    name : 'noEditFields'
					}],
		this.storeUrl = "roleView!loadRoles.action";
		this.callParent();
	},
	
	actionIssueTypes : null,
	actionFieldsRestictions : null,
	//chekListPanel : null,
	checkList : null,
	panelGrids : null,
	gridNoAccessSelected : null,
	gridAssignedSelected : null,
	
	getEntityLabel : function() {
	    return getText("admin.customize.role.lbl");
	},
	
	initActions : function() {
	    this.callParent();
	    var sys = com.trackplus.TrackplusConfig.user.sys;
	    if (sys) {
	        this.actionIssueTypes = CWHF.createAction("common.btn.issueType", "issueTypeLock",
	                "onAssignIssueType", {tooltip:getText("admin.customize.role.issueTypeToRole.lbl.explanation"), disabled:true});
	        this.actionFieldsRestictions = CWHF.createAction(
	                "admin.customize.role.lbl.fieldsRestrictions", "fieldsRestrictions",
	                "onFieldsRestrictions", {tooltip:getText("admin.customize.role.fieldsRestrictions.lbl.explanation"), disabled:true});
	        this.actions = [ this.actionAdd, this.actionEdit, this.actionCopy, this.actionIssueTypes,
	                this.actionFieldsRestictions, this.actionDelete ];
	    } else {
	        // the project admins have read only right on roles
	        this.actions = [];
	    }
	},

	/**
	 * Handler for double click
	 */
	onItemDblClick : function(view, record) {
	    var sys = com.trackplus.TrackplusConfig.user.sys;
	    if (sys) {
	        return this.callParent(arguments);
	    } else {
	        return [];
	    }
	},

	onGridSelectionChange : function(view, selections) {
	    var sys = com.trackplus.TrackplusConfig.user.sys;
	    if (sys) {
	        if (CWHF.isNull(selections) || selections.length === 0) {
	            // no selection
	            this.actionDelete.setDisabled(true);
	            this.actionEdit.setDisabled(true);
	            this.actionCopy.setDisabled(true);
	            this.actionIssueTypes.setDisabled(true);
	            this.actionFieldsRestictions.setDisabled(true);
	        } else {
	            this.actionDelete.setDisabled(false);
	            this.actionEdit.setDisabled(false);
	            this.actionCopy.setDisabled(false);
	            this.actionIssueTypes.setDisabled(false);
	            this.actionFieldsRestictions.setDisabled(false);
	        }
	    }
	},

	/**
	 * Get the actions available in context menu depending on
	 * the currently selected row
	 */
	getGridContextMenuActions : function(selectedRecords, selectionIsSimple) {
	    var sys = com.trackplus.TrackplusConfig.user.sys;
	    var actions = [];
	    if (sys) {
	        if (selectionIsSimple) {
	            actions.push(this.actionEdit);
	            actions.push(this.actionCopy);
	            actions.push(this.actionIssueTypes);
	            actions.push(this.actionFieldsRestictions);
	        }
	        actions.push(this.actionDelete);
	    }
	    return actions;
	},

	/**
	 * The iconCls for the add button, overwrites base class
	 * icon
	 */
	/* protected */getAddIconCls : function() {
	    return 'rolesAdd';
	},
	/**
	 * The iconCls for the edit button, overwrites base class
	 * icon
	 */
	/* protected */getEditIconCls : function() {
	    return 'rolesEdit';
	}
});
