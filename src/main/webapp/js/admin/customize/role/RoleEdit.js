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


Ext.define("com.trackplus.admin.customize.role.RoleEdit",{
	extend:"com.trackplus.admin.WindowBase",
	xtype: "roleEdit",
    controller: "roleEdit",
	
    width: 520,
    height: 335,
    
    loadUrl: "roleView!edit.action",
    
    addRole: false,
    copy: false,
    record: null,
    operation: null,
    
    initBase: function() {
    	var entityContext = this.getEntityContext();
		this.record = entityContext.record;
    	this.operation = entityContext.operation;
    	this.addRole = (this.operation==="add");
    	this.copy= (this.operation==="copy");
    	this.initActions();
    },
    
    initActions: function() {
    	this.actionSave = CWHF.createAction(this.getSaveButtonKey(), this.getSaveIconCls(), "onSave", {tooltip:this.getActionTooltip(this.getSaveTitleKey())});
    	this.actionCancel = CWHF.createAction(this.getCancelButtonKey(), this.getCancelIconCls(), "onCancel");
    	this.actions = [this.actionSave, this.actionCancel];
    },
    
    
	getLoadUrlParams: function() {
		var params = {};
		if (this.record) {
			params["roleID"] = this.record.get("id");
		}
		if (this.copy) {
			params["copy"] = this.copy;
		}
		return params;
	},
	
	getSubmitUrlParams: function() {
		var params = {};
		if (this.record) {
			params["roleID"] = this.record.get("id");
		}
		if (this.copy) {
			params["copy"] = this.copy;
		}
		return params;
	},
	
    /**
     * Get the panel items
     * Empty at the beginning: add the fields dynamically in postDataProcess 
     */
    getFormFields: function() {
    	return [ CWHF.createTextField("admin.customize.role.lbl.role", "label", {
		            anchor : '100%',
		            allowBlank : false,
		            labelWidth : 80,
		            labelAlign : "left"
		        }), 
		        Ext.create('Ext.panel.Panel', {
		        	itemId: "checkListPanel",
			        border: false,
			        bodyBorder: false,
			        autoScroll: true,
			        style: {
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
			    })
    	];
    },
    
    postDataProcess: function(data, panel) {
    	var checkListPanel = panel.getComponent("checkListPanel");
    	//var me = this;
	    checkListPanel.removeAll();
	    this.checkList = new Array();
	    var checkListFull = new Array();
	    for (var i = 0; i < data.fullAccessFlags.length; i++) {
	        var flag = data.fullAccessFlags[i];
	        checkListFull.push(Ext.create('Ext.form.field.Checkbox', {
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
	        items : checkListFull,
	        margin : '5 15 5 0'
	    });
	    var checkListRaciRole = new Array();
	    for (var i = 0; i < data.raciRoles.length; i++) {
	        var flag = data.raciRoles[i];
	        checkListRaciRole.push(Ext.create('Ext.form.field.Checkbox', {
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
	        items : checkListRaciRole,
	        margin : '5 0 5 0'
	    });

	    for (var i = 0; i < checkListFull.length; i++) {
	        this.checkList.push(checkListFull[i]);
	    }
	    for (var i = 0; i < checkListRaciRole.length; i++) {
	        this.checkList.push(checkListRaciRole[i]);
	    }
	    checkListPanel.add(fieldSetFullAccess);
	    checkListPanel.add(fieldSetRaciRoles);
	    checkListPanel.updateLayout();
	}
  
});
