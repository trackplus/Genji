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


Ext.define("com.trackplus.admin.NotifyTriggerEdit", {
	extend:"com.trackplus.admin.WindowBase",
	xtype: "notifyTriggerEdit",
    controller: "notifyTriggerEdit",
	
    width: 1000,
    height: 600,
    
    loadUrl: "notifySettings!edit.action",
    
    addNotifyTrigger: false,
    copy: false,
    record: null,
    triggerID: null,
    operation: null,
    defaultSettings: false,
   
    initBase: function() {
    	var entityContext = this.getEntityContext();
		this.record = entityContext.record;
		if (this.record) {
			this.triggerID = this.record.get("id");
		}
		this.operation = entityContext.operation;
    	this.addNotifyTrigger = (this.operation==="add");
    	this.copy = (this.operation==="copy");
    	var parentViewConfig = entityContext.config;
    	if (parentViewConfig) {
    		this.defaultSettings = parentViewConfig.defaultSettings;
    	}
    	this.initActions();
    	this.formPanel = this.createPanel(this.triggerID, this.copy);
    },
    
    initActions: function() {
    	this.actionSave = CWHF.createAction(this.getSaveButtonKey(), this.getSaveIconCls(), "onSave", {tooltip:this.getActionTooltip(this.getSaveTitleKey())});
    	this.actionCancel = CWHF.createAction(this.getCancelButtonKey(), this.getCancelIconCls(), "onCancel");
    	this.actions = [this.actionSave, this.actionCancel];
    },
    
    
	getLoadUrlParams: function() {
		var params = {defaultSettings: this.defaultSettings};
		if (this.record) {
			params["notifyTriggerID"] = this.record.get("id");
		}
		return params;
	},
	
	getSubmitUrlParams: function() {
		var params = {defaultSettings: this.defaultSettings};
		if (this.record) {
			params["notifyTriggerID"] = this.record.get("id");
		}
		return params;
	},
	
	/**
	 * Not a form panel load
	 */
	loadHandler : function() {
	    this.formStore.load();
	},
	
	getColumnByIndex : function(index) {
	    switch (index) {
	    case 1:
		    return "originator";
	    case 2:
		    return "manager";
	    case 3:
		    return "responsible";
	    case 4:
		    return "consulted";
	    case 5:
		    return "informed";
	    case 6:
		    return "observer";
	    }
	},
	
	createPanel : function(triggerID, copy) {
	    var labelTextField = CWHF.createTextField('common.lbl.name', 'label', {
	        itemId : 'label',
	        allowBlank : false,
	        labelWidth : 100,
	        width : 500,
	        anchor : '100%',
	        margin : '10 10 10 10',
	        region : 'north'
	    });
	    // trigger fields
	    var triggerFields = [ {
	        name : 'id',
	        mapping : 'id',
	        type : 'string'
	    }, {
	        name : 'objectID',
	        mapping : 'objectID',
	        type : 'int'
	    }, {
	        name : 'actionTypeLabel',
	        mapping : 'actionTypeLabel',
	        type : 'string',
	        persist : false
	    }, {
	        name : 'fieldTypeLabel',
	        mapping : 'fieldTypeLabel',
	        type : 'string',
	        persist : false
	    }, {
	        name : 'fieldLabel',
	        mapping : 'fieldLabel',
	        type : 'string',
	        persist : false
	    }, {
	        name : 'originator',
	        mapping : 'originator',
	        type : 'boolean'
	    }, {
	        name : 'manager',
	        mapping : 'manager',
	        type : 'boolean'
	    }, {
	        name : 'responsible',
	        mapping : 'responsible',
	        type : 'boolean'
	    }, {
	        name : 'consulted',
	        mapping : 'consulted',
	        type : 'boolean'
	    }, {
	        name : 'informed',
	        mapping : 'informed',
	        type : 'boolean'
	    }, {
	        name : 'observer',
	        mapping : 'observer',
	        type : 'boolean'
	    } ];
	    // trigger columns
	    var triggerFieldColumns = [{
	                text : getText('admin.customize.automail.trigger.lbl.actionType'),
	                menuDisabled : true,
	                dataIndex : 'actionTypeLabel',
	                flex : 1
	            }, {
	                text : getText('admin.customize.automail.trigger.lbl.fieldType'),
	                menuDisabled : true,
	                dataIndex : 'fieldTypeLabel',
	                flex : 1
	            }, {
	                text : getText('common.lbl.field'),
	                menuDisabled : true,
	                dataIndex : 'fieldLabel',
	                flex : 1
	            }, {
	                text : getText('admin.customize.automail.trigger.lbl.originator')
	                        + '&nbsp;<input type="checkbox" id="originator">',
	                menuDisabled : true,
	                dataIndex : 'originator',
	                flex : 1,
	                xtype : 'checkcolumn'
	            }, {
	                text : getText('admin.customize.automail.trigger.lbl.manager')
	                        + '&nbsp;<input type="checkbox" id="manager">',
	                menuDisabled : true,
	                dataIndex : 'manager',
	                flex : 1,
	                xtype : 'checkcolumn'
	            }, {
	                text : getText('admin.customize.automail.trigger.lbl.responsible')
	                        + '&nbsp;<input type="checkbox" id="responsible">',
	                menuDisabled : true,
	                dataIndex : 'responsible',
	                flex : 1,
	                xtype : 'checkcolumn'
	            }, {
	                text : getText('admin.customize.automail.trigger.lbl.consultant')
	                        + '&nbsp;<input type="checkbox" id="consulted">',
	                menuDisabled : true,
	                dataIndex : 'consulted',
	                flex : 1,
	                xtype : 'checkcolumn'
	            }, {
	                text : getText('admin.customize.automail.trigger.lbl.informant')
	                        + '&nbsp;<input type="checkbox" id="informed">',
	                menuDisabled : true,
	                dataIndex : 'informed',
	                flex : 1,
	                xtype : 'checkcolumn'
	            }, {
	                text : getText('admin.customize.automail.trigger.lbl.observer')
	                        + '&nbsp;<input type="checkbox" id="observer">',
	                menuDisabled : true,
	                dataIndex : 'observer',
	                flex : 1,
	                xtype : 'checkcolumn'
	            }];
	    //var isCopy = type === "copy";
	    //var triggerID = recordData.id;
	    // detail store
	    this.formStore = new Ext.data.Store({
	        proxy : {
	            type : 'ajax',
	            api : {
	                read : 'notifyTrigger!edit.action',
	                update : 'notifyTrigger!save.action'
	            },
	            extraParams : {
	                // extraParams by read
	                notifyTriggerID : triggerID,
	                copy : copy
	            },
	            reader : {
	                type : 'json',
	                rootProperty : 'records',
	                keepRawData: true
	            },
	            writer : {
	                type : 'json',
	                allowSingle : false,
	                writeAllFields : true,
	                rootProperty : 'records',
	                encode : true
	            }
	        },
	        fields : triggerFields,
	        remoteSort : true,
	        idProperty : 'id',
	        successProperty : 'success',
	        autoSync : false,
	        listeners : {
	            load : {
	                scope : this,
	                fn : function(store, records) {
		                var rawData = store.getProxy().getReader().rawData;
		                labelTextField.setValue(rawData.label);
		                labelTextField.setDisabled(rawData.disabled);
		                // this.formEdit.updateLayout();
		                var toolbar = this.getDockedItems('toolbar[dock="bottom"]');
		                // the first one is the save button
		                toolbar[0].getComponent(0).setDisabled(rawData.disabled);
		                for (var i = 1; i <= 6; i++) {
			                var column = this.getColumnByIndex(i);
			                var checkBox = document.getElementById(column);// Ext.get(column);
			                checkBox.checked = rawData[column];
		                }
	                }
	            },
	            beforesync : {
	                scope : this,
	                fn : function() {
		                // Grid data is changed.
		                // Label might be changed or not, anyway send it to the
						// server
		                this.formStore.getProxy().extraParams = {
		                    // extraParams by update
		                    notifyTriggerID : triggerID,
		                    copy : copy,
		                    label : labelTextField.getValue(),
		                    defaultSettings : this.defaultSettings
		                };
	                }
	            },
	            datachanged : {
	                scope : this,
	                fn : function(store, opts) {
		                // Grid data is changed
		                // Label might be changed or not: to be sure load the
						// store again
		                this.getRefreshAfterSubmitHandler({rowToSelect:triggerID});
	                }
	            }
	        }
	    });
	    var triggerFieldsGrid = {
	        xtype : 'grid',
	        itemId : 'detailGrid',
	        columns : triggerFieldColumns,
	        sortableColumns : false,
	        draggable : false,
	        store : this.formStore,
	        border : false,
	        bodyBorder : false,
	        loadMask : true,
	        region : 'center',
	        selModel : this.createSelModel(),
	        style : {
		        borderTop : '1px solid #D0D0D0'
	        },
	        viewConfig : {
		        forceFit : true,
		        stripeRows : true,
	        },
	        listeners : {
	            afterrender : {
	                fn : function() {
		                for (i = 1; i <= 6; i++) {
			                var column = this.getColumnByIndex(i);
			                var checkBox = Ext.get(column);
			                /*checkBox.addListener('click', this.headerChkHandler, this, {
				                column : column
			                });*/
			                checkBox.addListener('click', "headerChkHandler");
		                }
	                },
	                scope : this
	            },
	            // This method handles row selecting, deselecting
	            selectionchange : "onSelectionchange"/*{
	                fn : function(model, selected, eOpts) {
		                this.changeEntireRowSelection(this.peviousSelectedRows, false);
		                this.changeEntireRowSelection(selected, true);
		                this.peviousSelectedRows = selected;

	                },
	                scope : this
	            }*/
	        },
	        plugins : [ Ext.create('Ext.grid.plugin.CellEditing', {
		        clicksToEdit : 1
	        }) ]
	    };
	    return new Ext.panel.Panel({
	        border : false,
	        layout : 'border',
	        autoScroll : true,
	        margin : '0 0 0 0',
	        bodyStyle : {
		        padding : '0px'
	        },
	        items : [ labelTextField, triggerFieldsGrid ]
	    });
	},
	
	/**
	 * This method creates row selection model for grid.
	 */
	createSelModel : function() {
	    selModel = Ext.create('com.trackplus.itemNavigator.CheckboxModel', {
	        allowDeselect : true,
	        checkOnly : false,
	        mode : 'MULTI'
	    });
	    return selModel;
	},
  
});
