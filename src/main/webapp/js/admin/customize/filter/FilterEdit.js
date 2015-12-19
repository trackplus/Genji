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

Ext.define("com.trackplus.admin.customize.filter.FilterEdit", {
	extend: "com.trackplus.admin.WindowBase",
	xtype: "filterEdit",
    controller: "filterEdit",
    //scope for the static filter operations 
    indexMax: 0,
    //the dialog is an instant filter
    instant: false,
    issueFilter: false,
    //add/edit from item navigator
    renderPath: false,
    //operation name which popped up this window
    operation: null,
    //for instant filter is not set but should be true
    isLeaf: true,
    isAddOperation: true,
    parentViewRecord: null,
    
    upperSelectFields: null,
    
    INSTANT: "instant",
	ADD: "add",
	EDIT: "edit",
	
	labelWidth: 170,
	
	/**
	 * The localized entity name: not borrowed from parent view if we are coming from add/edit filter in itemNavigator
	 */
	getEntityLabel: function(extraConfig) {
	    var entityLabel = null;
	    var isLeaf = true;
	    if (extraConfig ) {
		    isLeaf = extraConfig.isLeaf;
	    }
	    if (isLeaf) {
		    if (this.isIssueFilter()) {
			    return getText("admin.customize.queryFilter.lbl.issueFilter");
		    } else {
			    if (this.isNotifyFilter()) {
				    return getText("admin.customize.automail.filter.lblOperation");
			    }
		    }
	    } else {
		    return getText("admin.customize.queryFilter.lbl.category");
	    }
	},
	
    /**
	 * Initialization method
	 */
	/*protected*/initBase : function() {
		var entityContext = this.getEntityContext();
		this.operation = entityContext.operation;
		if (this.operation===this.INSTANT) {
			this.instant = true;
		}
		this.renderPath = entityContext.renderPath;
		if (entityContext.isLeaf===false) {
			this.isLeaf = false;
		}
		if (entityContext.add===false) {
			this.isAddOperation = false;
		}
		this.parentViewRecord = entityContext.record;
		this.parentViewConfig = entityContext.config;
		//for scope in filter.js
		if (this.instant) {
			this.issueFilter = true;
		} else {
			this.issueFilter = this.isIssueFilter();
		}
	    this.initActions();
	},
	
    isIssueFilter : function() {
    	if (CWHF.isNull(this.parentViewRecord)) {
    		//instant filter or add/edit from item navigator
    		return true;
    	}
    	// this.rootID cold be a project specific branch node
	    return  this.parentViewConfig.rootID.indexOf("issueFilter") === 0;
	},

	isNotifyFilter : function() {
		// this.rootID cold be a project specific branch node
		if (CWHF.isNull(this.parentViewRecord)) {
    		//instant filter
    		return false;
    	}
	    return this.parentViewConfig.rootID.indexOf("notifyFilter") === 0;
	},

    initWidth : function() {
    	if (this.isLeaf) {
		    if (this.isIssueFilter()) {
			    return 910;
		    } else {
			    if (this.isNotifyFilter()) {
				    return 900;
			    }
		    }
	    } else {
		    return 400;
	    }
    },

    initHeight : function() {
    	if (this.isLeaf) {
		    if (this.isIssueFilter()) {
			    return 500;
		    } else {
			    if (this.isNotifyFilter()) {
				    return 500;
			    }
		    }
	    } else {
		    return 115;
	    }
    },
    
    getLoadUrl:function() {
    	if (this.isLeaf) {
    		return "filterConfig!edit.action";
    	} else {
    		return "categoryConfig!edit.action";
    	}
    },
   
    initActions: function() {
    	if (this.isLeaf) {
    		this.actionReset = CWHF.createAction(this.getResetButtonKey(), this.getResetIconCls(), "onReset");
        	this.actionCancel = CWHF.createAction(this.getCancelButtonKey(), this.getCancelIconCls(), "onCancel");
        	if (this.operation===this.INSTANT) {
        		this.actionApply = CWHF.createAction("common.btn.apply", "filterInst", "onApplyInstant", {tooltip:getText("common.btn.applyFilter")});
        		this.actions = [this.actionApply, this.actionReset, this.actionCancel];
        	} else {
        		if (this.isIssueFilter()) {
        			this.actionApply = CWHF.createAction("common.btn.apply", "filterInst", "onApplySaved", {tooltip:getText("common.btn.applyFilter")});
        		}
        		this.actionSave = CWHF.createAction(this.getSaveButtonKey(), this.getSaveIconCls(), "onSave", {tooltip:this.getActionTooltip(this.getSaveTitleKey(), {isLeaf: false})});
        		if (this.isIssueFilter()) {
        			if (this.parentViewRecord) {
        				//issue filter: add/edit from filter config
        				this.actions = [this.actionApply, this.actionSave, this.actionReset, this.actionCancel];
        			} else  {
        				//add/edit from filter navigator
        				this.actions = [this.actionSave, this.actionReset, this.actionCancel];
        			}
        		} else {
        			//navigation filter, 
        			this.actions = [this.actionSave, /*this.actionReset,*/ this.actionCancel];
        		}
        	}
    	} else {
    		this.actionSave = CWHF.createAction(this.getSaveButtonKey(), this.getSaveIconCls(), "onSave", {tooltip:this.getActionTooltip(this.getSaveTitleKey(), {isLeaf: false})});
    		this.actionCancel = CWHF.createAction(this.getCancelButtonKey(), this.getCancelIconCls(), "onCancel");
    		this.actions = [this.actionSave, this.actionCancel];
    	}
    },
    
    /**
     * Get the panel items
     */
    getFormFields : function() {
    	if (this.isLeaf) {
    		return this.getEditFilterItems();
    	} else {
    		return this.getEditFolderItems();
    	}
    },
    
	/**
	 * Prepare adding/editing a report
	 */
	getEditFilterItems : function() {
	    var modifiable = false;
	    if (this.instant || this.isAddOperation) {
		    modifiable = true;
	    } else {
		    modifiable = (CWHF.isNull(this.parentViewRecord) || this.parentViewRecord.get("modifiable"));
	    }
	    if (this.isIssueFilter()) {
		    if (this.instant || this.isAddOperation || CWHF.isNull(this.parentViewRecord) || CWHF.isNull(this.parentViewRecord.get("customFeature")) || this.parentViewRecord.get("customFeature")) {
			    return com.trackplus.admin.Filter.getTreeFilterItems(modifiable, this.instant);
		    } else {
			    return com.trackplus.admin.Filter.getTQLFilterItems(modifiable);
		    }
	    } else {
	    	return com.trackplus.admin.Filter.getNotifyFilterItems(modifiable);
	    }
	},
	
	/**
	 * Prepare adding/editing a report or filter category
	 */
	getEditFolderItems : function() {
	    var modifiable = false;
	    if (this.instant || this.isAddOperation) {
		    modifiable = true;
	    } else {
		    modifiable = this.parentViewRecord.get("modifiable");
	    }
	    return [CWHF.createTextField("common.lbl.name", "label", {
	        disabled : !modifiable,
	        allowBlank : false,
	        labelWidth : this.labelWidth
	    })];
	}, 
	
    postDataProcess: function(data, formPanel) {
    	if (this.isLeaf) { 
	    	if (this.isIssueFilter()) {
	 		    if (this.instant || CWHF.isNull(this.parentViewRecord) || this.isAddOperation || this.parentViewRecord.get("customFeature")) {
	 			    com.trackplus.admin.Filter.postLoadProcessTreeFilter.call(this, data, formPanel);
	 		    } else {
	 			    com.trackplus.admin.Filter.postLoadProcessTQLFilter.call(this, data, formPanel);
	 		    }
	 	    } else {
	 		    if (this.isNotifyFilter()) {
	 			    com.trackplus.admin.Filter.postLoadProcessNotifyFilter.call(this, data, formPanel);
	 		    }
	 	    }
    	}
    }
});
