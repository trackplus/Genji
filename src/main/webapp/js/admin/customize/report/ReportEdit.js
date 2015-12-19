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

Ext.define("com.trackplus.admin.customize.report.ReportEdit", {
	extend: "com.trackplus.admin.WindowBase",
	xtype: "reportEdit",
    controller: "reportEdit",
    //operation name which popped up this window
    operation: null,
    isAddOperation: true,
    //for instant filter is not set but should be true
    isLeaf: true,
    parentViewRecord: null,
    
    labelWidth: 170,
    
    /**
	 * Initialization method
	 */
	/*protected*/initBase : function() {
		var entityContext = this.getEntityContext();
		this.operation = entityContext.operation;
		if (entityContext.isLeaf===false) {
			this.isLeaf = false;
		}
		if (entityContext.add===false) {
			this.isAddOperation = false;
		}
		if (this.isAddOperation) {
    		this.minWidth = 450;
    		this.minHeight = 150;
    	}
		this.parentViewRecord = entityContext.record;
		this.initActions();
	},
	
    initWidth : function() {
    	if (this.isLeaf) {
		    return 500;
	    } else {
		    return 400;
	    }
    },

    initHeight : function() {
    	if (this.isLeaf) {
		    return 150;
	    } else {
		    return 115;
	    }
    },
    
    getLoadUrl:function() {
    	if (this.isLeaf) {
    		return "reportConfig!edit.action";
    	} else {
    		return "categoryConfig!edit.action";
    	}
    },
    
    /*initBase: function() {
    	if (this.isAddOperation) {
    		this.minWidth = 450;
    		this.minHeight = 150;
    	}
    	this.initActions();
    },*/
    
    
    initActions: function() {
		this.actionSave = CWHF.createAction(this.getSaveButtonKey(), this.getSaveIconCls(), "onSave", {tooltip:this.getActionTooltip(this.getSaveTitleKey(), {isLeaf: false})});
		this.actionCancel = CWHF.createAction(this.getCancelButtonKey(), this.getCancelIconCls(), "onCancel");
		this.actions = [this.actionSave, this.actionCancel];
    },
    
    /**
     * Get the panel items
     */
    getFormFields : function() {
    	if (this.isLeaf) {
    		return this.getEditReportItems();
    	} else {
    		return this.getEditFolderItems();
    	}
    },
    
	/**
	 * Prepare adding/editing a report
	 */
	getEditReportItems : function() {
	    var modifiable = false;
	    if (this.isAddOperation) {
		    modifiable = true;
	    } else {
		    modifiable = (CWHF.isNull(this.parentViewRecord) || this.parentViewRecord.get("modifiable"));
	    }
	    var windowItems = [ CWHF.createTextField("common.lbl.name", "label", {
	        disabled : !modifiable,
	        allowBlank : false,
	        labelWidth : this.labelWidth
	    }) ];
	    if (this.isAddOperation) {
		    windowItems.push(CWHF.createFileField("admin.customize.reportTemplate.lbl.reportFile", "reportFile", {
		        disabled: !modifiable,
		        allowBlank: false,
		        labelWidth: this.labelWidth,
		        itemId: "reportFile"
		    }));
	    }
	    return windowItems;
	},
	
	/**
	 * Prepare adding/editing a report or filter category
	 */
	getEditFolderItems : function() {
	    var modifiable = false;
	    if (this.isAddOperation) {
		    modifiable = true;
	    } else {
		    modifiable = this.parentViewRecord.get("modifiable");
	    }
	    return [CWHF.createTextField("common.lbl.name", "label", {
	        disabled : !modifiable,
	        allowBlank : false,
	        labelWidth : this.labelWidth
	    })];
	}
});
