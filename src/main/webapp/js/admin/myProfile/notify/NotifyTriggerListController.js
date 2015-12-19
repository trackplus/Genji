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

Ext.define("com.trackplus.admin.NotifyTriggerListController", {
	extend: "Ext.app.ViewController",
	alias: "controller.notifyTriggerList",
	mixins: {
		baseController: "com.trackplus.admin.GridBaseController"
	},
	
	entityDialog: "com.trackplus.admin.NotifyTriggerEdit",
	/*baseAction : "notifyTrigger",
	entityID : "notifyTriggerID",
	editWidth : 1000,
	editHeight : 600,
	peviousSelectedRows : [],*/
	
	/**
	 * Parameters for adding a new entity
	 */
	/*getAddParams : function() {
	    return {
		    defaultSettings : this.defaultSettings
	    };
	},*/
	/**
	 * Parameters for editing an existing entity
	 */
	/*getEditParams : function(recordData) {
	    var params = new Object();
	    params['' + this.entityID] = recordData.id;
	    params["defaultSettings"] = this.defaultSettings;
	    return params;
	},*/

	getDeleteUrl: function() {
		return "notifyTrigger!delete.action";
	},
	
	/**
	 * Parameters for deleting an existing entity
	 */
	getDeleteParams : function(selectedRecord) {
	    var params = new Object();
	    params['' + this.entityID] = selectedRecord.data.id;
	    params['defaultSettings'] = this.defaultSettings;
	    return params;
	},

	/*headerChkHandler : function(event, chkBox, options) {
	    var checked = chkBox.checked;
	    var numberOfRecords = this.formStore.getTotalCount();
	    var column = options.column;
	    for (i = 0; i < numberOfRecords; i++) {
		    var record = this.formStore.getAt(i);
		    // record.data[column] = checked;
		    record.set(column, checked);
	    }
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
	},*/

	/*createEditForm : function(recordData, type) {
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
	    var triggerFieldColumns = [
	            {
	                text : getText('admin.customize.automail.trigger.lbl.actionType'),
	                menuDisabled : true,
	                dataIndex : 'actionTypeLabel',
	                flex : 1
	            },
	            {
	                text : getText('admin.customize.automail.trigger.lbl.fieldType'),
	                menuDisabled : true,
	                dataIndex : 'fieldTypeLabel',
	                flex : 1
	            },
	            {
	                text : getText('common.lbl.field'),
	                menuDisabled : true,
	                dataIndex : 'fieldLabel',
	                flex : 1
	            },
	            {
	                text : getText('admin.customize.automail.trigger.lbl.originator')
	                        + '&nbsp;<input type="checkbox" id="originator">',
	                menuDisabled : true,
	                dataIndex : 'originator',
	                flex : 1,
	                xtype : 'checkcolumn'
	            },
	            {
	                text : getText('admin.customize.automail.trigger.lbl.manager')
	                        + '&nbsp;<input type="checkbox" id="manager">',
	                menuDisabled : true,
	                dataIndex : 'manager',
	                flex : 1,
	                xtype : 'checkcolumn'
	            },
	            {
	                text : getText('admin.customize.automail.trigger.lbl.responsible')
	                        + '&nbsp;<input type="checkbox" id="responsible">',
	                menuDisabled : true,
	                dataIndex : 'responsible',
	                flex : 1,
	                xtype : 'checkcolumn'
	            },
	            {
	                text : getText('admin.customize.automail.trigger.lbl.consultant')
	                        + '&nbsp;<input type="checkbox" id="consulted">',
	                menuDisabled : true,
	                dataIndex : 'consulted',
	                flex : 1,
	                xtype : 'checkcolumn'
	            },
	            {
	                text : getText('admin.customize.automail.trigger.lbl.informant')
	                        + '&nbsp;<input type="checkbox" id="informed">',
	                menuDisabled : true,
	                dataIndex : 'informed',
	                flex : 1,
	                xtype : 'checkcolumn'
	            },
	            {
	                text : getText('admin.customize.automail.trigger.lbl.observer')
	                        + '&nbsp;<input type="checkbox" id="observer">',
	                menuDisabled : true,
	                dataIndex : 'observer',
	                flex : 1,
	                xtype : 'checkcolumn'
	            } ];
	    var isCopy = type === "copy";
	    var triggerID = recordData.id;
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
	                copy : isCopy
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
		                var toolbar = this.win.getDockedItems('toolbar[dock="bottom"]');
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
		                    copy : isCopy,
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
		                this.reload.call(this);
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
			                checkBox.addListener('click', this.headerChkHandler, this, {
				                column : column
			                });
		                }
	                },
	                scope : this
	            },
	            
	            // This method handles row selecting, deselecting
	            selectionchange : {
	                fn : function(model, selected, eOpts) {
		                this.changeEntireRowSelection(this.peviousSelectedRows, false);
		                this.changeEntireRowSelection(selected, true);
		                this.peviousSelectedRows = selected;

	                },
	                scope : this
	            }
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
	},*/

	/**
	 * This method set each row each checkbox checked /true or false/ In case of
	 * setting false: if header checkbox checked then actual checkbox remains
	 * checked otherwise bill be unchecked
	 */
	/*changeEntireRowSelection : function(rows, checked) {
	    for (i = 0; i < rows.length; i++) {
		    var record = rows[i];
		    for (var j = 1; j < 7; j++) {
			    if (!checked) {
				    var column = this.getColumnByIndex(j);
				    var checkBox = document.getElementById(column);
				    if (!checkBox.checked) {
					    record.set(this.getColumnByIndex(j), checked);
				    }
			    } else {
				    record.set(this.getColumnByIndex(j), checked);
			    }

		    }
	    }
	},*/

	/**
	 * Not a form panel load
	 */
	/*loadHandler : function() {
	    this.formStore.load();
	},*/
	/**
	 * Not a form panel submit
	 */
	/*submitHandler : function(window, submitUrl, submitUrlParams) {
	    var labelTextField = this.formEdit.getComponent('label');
	    if (!labelTextField.validate()) {
		    return;
	    }
	    var records = this.formStore.getUpdatedRecords();
	    if (records  && records.length > 0) {
		    // at least a record was changed
		    // the label might be changed or not
		    this.formStore.sync();
	    } else {
		    if (labelTextField.isDirty()) {
			    // var params = this.getSaveParams(recordData, type);
			    // add the current label to submitUrlParams
			    submitUrlParams["label"] = labelTextField.getValue();
			    submitUrlParams["defaultSettings"] = this.defaultSettings;
			    // no record but the label was changed
			    Ext.Ajax.request({
			        url : 'notifyTrigger!save.action',
			        params : submitUrlParams,
			        scope : this,
			        success : function(response) {
				        var responseJson = Ext.decode(response.responseText);
				        this.reload.call(this, {
					        rowToSelect : responseJson.id
				        });
			        },
			        failure : function(result) {
				        Ext.MessageBox.alert(this.failureTitle, result.responseText);
			        }
			    });
		    }
	    }
	    this.win.close();
	},*/

	/**
	 * This method creates row selection model for grid.
	 */
	/*createSelModel : function() {
	    selModel = Ext.create('com.trackplus.itemNavigator.CheckboxModel', {
	        allowDeselect : true,
	        checkOnly : false,
	        mode : 'MULTI'
	    });
	    return selModel;
	},*/

	/**
	 * Enable change only if own trigger
	 */
	onSelectionChange : function(view, arrSelections) {
	    if (CWHF.isNull(arrSelections) || arrSelections.length === 0) {
		    this.getView().actionDelete.setDisabled(true);
		    this.getView().actionEdit.setDisabled(true);
		    this.getView().actionCopy.setDisabled(true);
	    } else {
		    var selectedRecord = arrSelections[0];
		    var own = selectedRecord.data['own'];
		    this.getView().actionDelete.setDisabled(!own);
		    this.getView().actionEdit.setDisabled(!own);
		    this.getView().actionCopy.setDisabled(!own);
	    }
	}
	
});
