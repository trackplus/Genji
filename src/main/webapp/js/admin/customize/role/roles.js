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

Ext.define("com.trackplus.admin.customize.role.RolesGridConfig", {
	extend : 'com.trackplus.admin.GridConfig',
	urlStore : 'roleView!loadRoles.action',
	autoLoadGrid : false,
	fields : [ {
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
	columnModel : [
	        {
	            header : getText('admin.customize.role.lbl.role'),
	            width : 175,
	            dataIndex : 'name',
	            id : 'name',
	            sortable : true
	        },
	        {
	            xtype : 'templatecolumn',
	            header : getText('admin.customize.role.lbl.accessFlags'),
	            tpl : new Ext.XTemplate(
	                    '<table><tr>',
	                    '<td valign="top">',
	                    '<tpl for="accessFlags.fullAccessFlags">',
	                    '<div><input type="checkbox" disabled="disabled"  style="vertical-align: middle; margin: 0px;" ',
	                    '<tpl if="selected">', 'checked="true"', '</tpl>',
	                    'value="true"/> {label}</div>', '</tpl>', '</td>', '</tr></table>'),
	            width : 175,
	            sortable : true
	        },
	        {
	            xtype : 'templatecolumn',
	            header : getText('admin.customize.role.lbl.inSelections'),
	            tpl : new Ext.XTemplate(
	                    '<table><tr>',
	                    '<td valign="top">',
	                    '<tpl for="accessFlags.raciRoles">',
	                    '<div><input type="checkbox" disabled="disabled"  style="vertical-align: middle; margin: 0px;" ',
	                    '<tpl if="selected">', 'checked="true"', '</tpl>',
	                    'value="true"/> {label}</div>', '</tpl>', '</td>', '</tr>', '</table>'),
	            width : 175,
	            sortable : true
	        },
	        {
	            xtype : 'templatecolumn',
	            header : getText('admin.customize.role.lbl.allowedListTypes'),
	            tpl : new Ext.XTemplate('<table>', '<tpl for="issueTypesAssigned">',
	                    '<tr><td><img src="optionIconStream.action?fieldID=-2&optionID={id}"></td>',
	                    '<td style="padding:2px">{label}</td></tr>', '</tpl>', '</table>'

	            ),
	            width : 175,
	            sortable : false
	        },
	        {
	            xtype : 'templatecolumn',
	            header : getText('admin.customize.role.fieldsRestrictions.lbl.hiddenFields'),
	            tpl : new Ext.XTemplate('<table>', '<tpl for="noReadFields">',
	                    '<td style="padding:2px">{label}</td></tr>', '</tpl>', '</table>'),
	            width : 175,
	            sortable : false
	        },
	        {
	            xtype : 'templatecolumn',
	            header : getText('admin.customize.role.fieldsRestrictions.lbl.readOnlyFields'),
	            tpl : new Ext.XTemplate('<table>', '<tpl for="noEditFields">',
	                    '<td style="padding:2px">{label}</td></tr>', '</tpl>', '</table>'),
	            width : 175,
	            sortable : false
	        }

	],
	confirmDeleteEntity : true,
	baseAction : 'roleView',
	entityID : 'roleID',
	editWidth : 520,
	editHeight : 335,
	useCopy : true,
	actionIssueTypes : null,
	actionFieldsRestictions : null,
	chekListPanel : null,
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
	        this.actionIssueTypes = this.createAction("common.btn.issueType", "issueTypeLock",
	                this.onAssignIssueType, true,
	                "admin.customize.role.issueTypeToRole.lbl.explanation");
	        this.actionFieldsRestictions = this.createAction(
	                "admin.customize.role.lbl.fieldsRestrictions", "fieldsRestrictions",
	                this.onFieldsRestrictions, true,
	                "admin.customize.role.fieldsRestrictions.lbl.explanation");
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
	        if (selections === null || selections.length === 0) {
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

	createEditForm : function(entityJS, type) {
	    this.chekListPanel = Ext.create('Ext.panel.Panel', {
	        border : false,
	        bodyBorder : false,
	        autoScroll : true,
	        style : {
	            border : 'none',
	            background : 'none'
	        },
	        layout : {
	            type : 'table',
	            columns : 2,
	            tableAttrs : {
	                style : {
	                    width : '100%'
	                }
	            },
	            tdAttrs : {
	                width : '50%',
	                style : {
	                    'vertical-align' : 'top'
	                }
	            }
	        },
	        defaults : {
	            frame : false,
	            border : false
	        },
	        anchor : '100%',
	        items : []
	    });
	    return new Ext.form.FormPanel({
	        url : 'roleSave.action',
	        labelWidth : 130,
	        region : 'center',
	        border : false,
	        autoScroll : true,
	        margin : '0 0 0 0',
	        bodyStyle : {
	            padding : '10px'
	        },
	        /*
			 * style:{ borderBottom:'1px solid #D0D0D0' },
			 */
	        defaultType : 'textfield',
	        items : [ CWHF.createTextField("admin.customize.role.lbl.role", "label", {
	            anchor : '100%',
	            allowBlank : false,
	            labelWidth : 80,
	            labelAlign : "left"
	        }), this.chekListPanel ]
	    });
	},
	afterLoadForm : function(data) {
	    var me = this;
	    me.chekListPanel.removeAll();
	    me.chekList = new Array();
	    var chekListFull = new Array();
	    for (var i = 0; i < data.fullAccessFlags.length; i++) {
	        var flag = data.fullAccessFlags[i];
	        chekListFull.push(Ext.create('Ext.form.field.Checkbox', {
	            boxLabel : flag.label,
	            checked : flag.selected,
	            name : 'f' + flag.id,
	            inputValue : 'true'
	        }));
	    }
	    var fieldSetFullAccess = Ext.create('Ext.form.FieldSet', {
	        title : getText('admin.customize.role.lbl.generalAccess'),
	        defaultType : 'checkbox', // each item will be a
										// checkbox
	        layout : 'anchor',
	        items : chekListFull,
	        margin : '5 15 5 0'
	    });
	    var chekListRaciRole = new Array();
	    for (var i = 0; i < data.raciRoles.length; i++) {
	        var flag = data.raciRoles[i];
	        chekListRaciRole.push(Ext.create('Ext.form.field.Checkbox', {
	            boxLabel : flag.label,
	            checked : flag.selected,
	            name : 'f' + flag.id,
	            inputValue : 'true'
	        }));
	    }
	    var fieldSetRaciRoles = Ext.create('Ext.form.FieldSet', {
	        title : getText('admin.customize.role.lbl.raci'),
	        defaultType : 'checkbox', // each item will be a
										// checkbox
	        layout : 'anchor',
	        items : chekListRaciRole,
	        margin : '5 0 5 0'
	    });

	    for (var i = 0; i < chekListFull.length; i++) {
	        me.chekList.push(chekListFull[i]);
	    }
	    for (var i = 0; i < chekListRaciRole.length; i++) {
	        me.chekList.push(chekListRaciRole[i]);
	    }
	    me.chekListPanel.add(fieldSetFullAccess);
	    me.chekListPanel.add(fieldSetRaciRoles);
	    me.chekListPanel.doLayout();
	},

	preSubmitProcess : function(submitUrlParams, panel) {
	    var me = this;
	    var NUMBER_OF_ACCESS_FLAGS = 21;
	    var accessFlags = new Array(21);
	    for (var i = 0; i < accessFlags.length; i++) {
	        accessFlags[i] = 0;
	    }
	    if (me.chekList != null) {

	        for (var i = 0; i < me.chekList.length; i++) {
	            var index = me.chekList[i].getName().substring(1);
	            if (me.chekList[i].getRawValue() == true) {
	                accessFlags[index] = 1;
	                // extendedAccessKey+='1';
	            } else {
	                accessFlags[index] = 0;
	                // extendedAccessKey+='0';
	            }
	        }
	    }
	    var extendedAccessKey = "";
	    for (var i = 0; i < accessFlags.length; i++) {
	        if (accessFlags[i] == 1) {
	            extendedAccessKey += '1';
	        } else {
	            extendedAccessKey += '0';
	        }
	    }
	    submitUrlParams['extendedAccessKey'] = extendedAccessKey;
	    return submitUrlParams;
	},

	createFieldRestrictionsForm : function(roleID) {
	    var explanation = {
	        region : 'north',
	        xtype : 'component',
	        cls : 'infoBox_bottomBorder',
	        border : true,
	        html : getText('admin.customize.role.fieldsRestrictions.lbl.explanation')
	    };
	    // grid fields
	    var roleFields = [ {
	        name : 'id',
	        mapping : 'id',
	        type : 'string'
	    }, {
	        name : 'objectID',
	        mapping : 'objectID',
	        type : 'int'
	    }, {
	        name : 'fieldLabel',
	        mapping : 'fieldLabel',
	        type : 'string'
	    }, {
	        name : 'hidden',
	        mapping : 'hidden',
	        type : 'boolean'
	    }, {
	        name : 'forcedReadOnly',
	        mapping : 'forcedReadOnly',
	        type : 'boolean'
	    }, {
	        name : 'configuredReadOnly',
	        mapping : 'configuredReadOnly',
	        type : 'boolean'
	    } ];
	    // grid columns
	    var roleFieldColumns = [
	            {
	                text : getText('common.lbl.field'),
	                menuDisabled : true,
	                dataIndex : 'fieldLabel',
	                flex : 3
	            },
	            {
	                text : getText('admin.customize.role.fieldsRestrictions.lbl.hide')
	                        + '&nbsp;<input type="checkbox" id="hidden">',
	                menuDisabled : true,
	                dataIndex : 'hidden',
	                flex : 2,
	                xtype : 'checkcolumn'
	            },
	            {
	                text : getText('admin.customize.role.fieldsRestrictions.lbl.readOnly')
	                        + '&nbsp;<input type="checkbox" id="configuredReadOnly">',
	                menuDisabled : true,
	                dataIndex : 'configuredReadOnly',
	                flex : 2,
	                xtype : 'checkcolumn',
	                renderer : function(val, m, rec) {
	                    if (rec.get('forcedReadOnly')) {
	                        return "";
	                    } else {
	                        return (new Ext.ux.CheckColumn()).renderer(val, m, rec);
	                    }
	                }
	            } ];
	    // detail store
	    this.roleFieldGridStore = new Ext.data.Store({
	        proxy : {
	            type : 'ajax',
	            api : {
	                read : 'fieldsRestrictionsToRole!edit.action',
	                update : 'fieldsRestrictionsToRole!save.action'
	            },
	            extraParams : {
	                // extraParams by read
	                roleID : roleID
	            },
	            reader : {
	                type : 'json',
	                root : 'records'
	            },
	            writer : {
	                type : 'json',
	                allowSingle : false,
	                writeAllFields : true,
	                root : 'records',
	                encode : true
	            }
	        },
	        fields : roleFields,
	        remoteSort : true,
	        idProperty : 'id',
	        successProperty : 'success',
	        autoSync : false,
	        listeners : {
	            load : {
	                scope : this,
	                fn : function(store, records) {
	                    var rawData = store.getProxy().getReader().rawData;
	                    var toolbar = this.win.getDockedItems('toolbar[dock="bottom"]');
	                    // the first one is the save button
	                    toolbar[0].getComponent(0).setDisabled(rawData.disabled);
	                    for (i = 1; i <= 2; i++) {
	                        var column = this.getColumnByIndex(i);
	                        var checkBox = document.getElementById(column);// Ext.get(column);
	                        checkBox.checked = rawData[column];
	                    }
	                }
	            },
	            beforesync : function() {
	                // Grid data is changed.
	                // Label might be changed or not, anyway
					// send it to the server
	                this.getProxy().extraParams = {
	                    // extraParams by update
	                    roleID : roleID
	                }
	            },
	            datachanged : {
	                scope : this,
	                fn : function(store, opts) {
	                    // Grid data is changed
	                    // Label might be changed or not: to be
						// sure load the store again
	                    this.reload.call(this);
	                }
	            }
	        }
	    });
	    var roleFieldsGrid = {
	        xtype : 'grid',
	        itemId : 'detailGrid',
	        columns : roleFieldColumns,
	        sortableColumns : false,
	        draggable : false,
	        store : this.roleFieldGridStore,
	        border : false,
	        bodyBorder : false,
	        loadMask : true,
	        stripeRows : true,
	        region : 'center',
	        style : {
	            borderTop : '1px solid #D0D0D0'
	        },
	        viewConfig : {
	            forceFit : true
	        },
	        listeners : {
	            afterrender : {
	                fn : function() {
	                    for (i = 1; i <= 2; i++) {
	                        var column = this.getColumnByIndex(i);
	                        var checkBox = Ext.get(column);
	                        checkBox.addListener('click', this.headerChkHandler, this, {
	                            column : column
	                        });
	                    }
	                },
	                scope : this
	            }

	        },
	        plugins : [ Ext.create('Ext.grid.plugin.CellEditing', {
	            clicksToEdit : 1
	        }) ]
	    };
	    // detailGridStore.load();
	    return new Ext.panel.Panel({
	        border : false,
	        layout : 'border',
	        autoScroll : true,
	        margin : '0 0 0 0',
	        bodyStyle : {
	            padding : '0px'
	        },
	        /*
			 * style:{ borderBottom:'1px solid #D0D0D0' },
			 */
	        items : [ explanation, roleFieldsGrid ]
	    });
	},

	getColumnByIndex : function(index) {
	    switch (index) {
	    case 1:
	        return "hidden";
	    case 2:
	        return "configuredReadOnly";
	    }
	},

	headerChkHandler : function(event, chkBox, options) {
	    var checked = chkBox.checked;
	    var numberOfRecords = this.roleFieldGridStore.getTotalCount();
	    var column = options.column;
	    for (i = 0; i < numberOfRecords; i++) {
	        var record = this.roleFieldGridStore.getAt(i);
	        record.set(column, checked);
	    }
	},

	onFieldsRestrictions : function() {
	    var entityJS = null;
	    var selections = this.grid.getSelectionModel().getSelection();
	    if (selections.length == 1) {
	        entityJS = selections[0].data;
	    }
	    if (entityJS == null) {
	        return true;
	    }
	    var roleID = entityJS.id;
	    var load = {
	        loadHandler : this.loadRoleFieldsHandler
	    };
	    var submit = {
	        submitHandler : this.submitRoleFieldsHandler
	    };
	    var title = getText('admin.customize.role.fieldsRestrictions.lbl.header') + ' "'
	            + entityJS.name + '"';
	    var windowConfig = Ext.create('com.trackplus.util.WindowConfig', {
	        title : title,
	        width : 400,
	        height : 500,
	        load : load,
	        submit : submit,
	        formPanel : this.createFieldRestrictionsForm(roleID)
	    /*
		 * , postDataProcess:me.afterLoadForm,
		 * preSubmitProcess:me.preSubmitProcess
		 */});
	    windowConfig.showWindowByConfig(this);
	},

	/**
	 * Not a form panel load
	 */
	loadRoleFieldsHandler : function() {
	    this.roleFieldGridStore.load();
	},
	/**
	 * Not a form panel submit
	 */
	submitRoleFieldsHandler : function(win, submitUrl, submitUrlParams) {
	    var records = this.roleFieldGridStore.getUpdatedRecords();
	    if (records != null && records.length > 0) {
	        // at least a record was changed
	        // the label might be changed or not
	        this.roleFieldGridStore.sync();
	    }
	    this.win.close();
	},

	onAssignIssueType : function(entityJS) {
	    var me = this;
	    var entityJS = null;
	    var selections = me.grid.getSelectionModel().getSelection();
	    if (selections.length == 1) {
	        entityJS = selections[0].data;
	    }
	    if (entityJS == null) {
	        return true;
	    }
	    var title = getText('admin.customize.role.issueTypeToRole.lbl.header') + ' "' + entityJS.name
	            + '"';
	    var northPanel = {
	        region : 'north',
	        xtype : 'component',
	        cls : 'infoBox_bottomBorder',
	        border : true,
	        html : getText('admin.customize.role.issueTypeToRole.lbl.explanation')
	    };
	    me.panelGrids = me.createPanelIssueTypeAssign(entityJS.id);
	    var issueTypeWin = Ext.create('Ext.window.Window', {
	        layout : 'border',
	        width : 500,
	        height : 350,
	        minWidth : 425,
	        closeAction : 'destroy',
	        title : title,
	        modal : true,
	        items : [ northPanel, me.panelGrids ],
	        cls : 'bottomButtonsDialog',
	        border : false,
	        bodyBorder : false,
	        margin : '0 0 0 0',
	        style : {
	            padding : '0px'
	        },
	        bodyPadding : '0px',
	        buttons : [ {
	            text : getText('common.btn.done'),
	            handler : function() {
	                issueTypeWin.hide();
	                issueTypeWin.destroy();
	                me.grid.store.load();
	            }
	        } ]
	    });
	    issueTypeWin.show();
	    me.reloadAssigned('issueTypeToRole.action?roleID=' + entityJS.id);
	},
	reloadAssigned : function(urlStr, params, loading) {
	    var me = this;
	    var useLoading = true;
	    if (loading === false) {
	        useLoading = false;
	    }
	    me.panelGrids.setLoading(useLoading);
	    Ext.Ajax.request({
	        url : urlStr,
	        params : params,
	        disableCaching : true,
	        success : function(result) {
	            var jsonData = Ext.decode(result.responseText);
	            if (jsonData.success === true) {
	                me.gridNoAccessSelected.store.loadData(jsonData.data["noAccess"], false);
	                me.gridAssignedSelected.store.loadData(jsonData.data["assiged"], false);
	            } else {
	                var errorCode = jsonData.errorCode;
	                var errorMessage = jsonData.errorMessage;
	                alert("failure" + errorMessage);
	            }
	            me.panelGrids.setLoading(false);
	        },
	        failure : function(result) {
	            var jsonData = Ext.decode(result.responseText);
	            var errorCode = jsonData.errorCode;
	            var errorMessage = jsonData.errorMessage;
	            alert("failure" + errorMessage);
	            me.panelGrids.setLoading(false);
	        }
	    // method:
	    // form:addItemConfig.addFormName
	    });
	},
	renderFieldTypeIcon : function(val) {
	    return "";
	},
	renderIssueTypeIcon : function(val) {
	    if (val == null || val == "") {
	        return "";
	    }
	    return '<img src="optionIconStream.action?fieldID=-2&optionID=' + val + '">';
	},
	createPanelIssueTypeAssign : function(roleID) {
	    var me = this;
	    var firstGridStore = Ext.create('Ext.data.Store', {
	        fields : [ {
	            name : 'id',
	            type : 'int'
	        }, {
	            name : 'label',
	            type : 'string'
	        }, {
	            name : 'symbol',
	            type : 'string'
	        } ],
	        data : []
	    });
	    var columns = [ {
	        text : "symbol",
	        width : 30,
	        sortable : true,
	        dataIndex : 'id',
	        renderer : me.renderIssueTypeIcon
	    }, {
	        text : "Name",
	        flex : 1,
	        sortable : true,
	        dataIndex : 'label'
	    } ];
	    me.gridNoAccessSelected = Ext.create('Ext.grid.Panel', {
	        viewConfig : {
	            plugins : {
	                ptype : 'gridviewdragdrop',
	                dragGroup : 'firstGridDDGroup',
	                dropGroup : 'secondGridDDGroup'
	            },
	            listeners : {
	                drop : function(node, data, dropRec, dropPosition) {
	                    if (data != null && data.records != null && data.records.length > 0) {
	                        var id = data.records[0].data.id;
	                        var params = new Object();
	                        params['assignedSelected'] = id;
	                        me.reloadAssigned('issueTypeToRole!unassign.action?roleID=' + roleID,
	                                params, false);
	                    }
	                }
	            }
	        },
	        store : firstGridStore,
	        columns : columns,
	        stripeRows : true,
	        title : getText('admin.customize.role.issueTypeToRole.lbl.unassigned'),
	        hideHeaders : true,
	        border : false,
	        bodyBorder : false,
	        cls : 'gridNoBorder',
	        style : {
	            borderBottom : '1px solid #D0D0D0'
	        }
	    });

	    var secondGridStore = Ext.create('Ext.data.Store', {
	        fields : [ {
	            name : 'id',
	            type : 'int'
	        }, {
	            name : 'label',
	            type : 'string'
	        }, {
	            name : 'symbol',
	            type : 'string'
	        } ],
	        data : []
	    });
	    // create the destination Grid
	    me.gridAssignedSelected = Ext.create('Ext.grid.Panel', {
	        viewConfig : {
	            plugins : {
	                ptype : 'gridviewdragdrop',
	                dragGroup : 'secondGridDDGroup',
	                dropGroup : 'firstGridDDGroup'
	            },
	            listeners : {
	                drop : function(node, data, dropRec, dropPosition) {
	                    if (data != null && data.records != null && data.records.length > 0) {
	                        var id = data.records[0].data.id;
	                        var params = new Object();
	                        params['noAccessSelected'] = id;
	                        me.reloadAssigned('issueTypeToRole!assign.action?roleID=' + roleID, params,
	                                false);
	                    }
	                }
	            }
	        },
	        store : secondGridStore,
	        columns : columns,
	        stripeRows : true,
	        title : getText('admin.customize.role.issueTypeToRole.lbl.assigned'),
	        hideHeaders : true,
	        border : false,
	        bodyBorder : false,
	        cls : 'gridNoBorder',
	        style : {
	            borderLeft : '1px solid #D0D0D0',
	            borderBottom : '1px solid #D0D0D0'
	        }
	    });

	    // Simple 'border layout' panel to house both grids
	    var displayPanel = Ext.create('Ext.Panel', {
	        region : 'center',
	        margin : '0 0 0 0',
	        border : false,
	        bodyBorder : false,
	        layout : {
	            type : 'hbox',
	            align : 'stretch'
	        },
	        defaults : {
	            flex : 1
	        }, // auto stretch
	        items : [ me.gridNoAccessSelected, me.gridAssignedSelected ]
	    });
	    return displayPanel;
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
