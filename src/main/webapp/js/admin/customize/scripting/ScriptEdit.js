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


Ext.define("com.trackplus.admin.customize.script.ScriptEdit",{
	extend:"com.trackplus.admin.WindowBase",
	xtype: "scriptEdit",
    controller: "scriptEdit",
	
    width: 800,
    height: 600,
    labelWidth: 120,	
    
    addScript: false,
    copy: false,
    record: null,
    operation: null,
    
    loadUrl: "script!edit.action",
    
    initBase: function() {
    	var entityContext = this.getEntityContext();
		this.record = entityContext.record;
    	this.operation = entityContext.operation;
    	this.addScript = (this.operation==="add");
    	this.copy= (this.operation==="copy");
    	this.initActions();
    },
    
    initActions: function() {
    	var compileDisabled = false;
	    if (this.addScript) {
	    	compileDisabled = false;
	    } else {
	    	if (this.record) {
		    	var scriptType = this.record.get("scriptType");
		    	compileDisabled = this.compileIsDisabled(scriptType);
		    }
	    }
	    this.actionCompile = CWHF.createAction("common.btn.compile", null, "onCompile", {disabled: compileDisabled});
		this.actionSave = CWHF.createAction(this.getSaveButtonKey(), this.getSaveIconCls(), "onSave", {tooltip:this.getActionTooltip(this.getSaveTitleKey())});
    	this.actionCancel = CWHF.createAction(this.getCancelButtonKey(), this.getCancelIconCls(), "onCancel");
    	this.actions = [this.actionCompile, this.actionSave, this.actionCancel];
    },
    
    compileIsDisabled: function(scriptType) {
	    if (scriptType && scriptType===3) {
	        return true;
	    }
	    return false;
	},
	
	getLoadUrlParams: function() {
		var params = {};
		if (this.record) {
			params["scriptID"] = this.record.get("id");
		}
		if (this.copy) {
			params["copy"] = this.copy;
		}
		return params;
	},
	
	getSubmitUrlParams: function() {
		var params = {};
		if (this.record) {
			params["scriptID"] = this.record.get("id");
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
    	return [CWHF.createTextField("admin.customize.script.lbl.className", "clazzName",
						{allowBlank:false, labelWidth:this.labelWidth, anchor:'100%',itemId:'clazzName'}),
				CWHF.createCombo("admin.customize.script.lbl.scriptType", "scriptType",
					{itemId:"scriptType", width:300, allowBlank:false, labelWidth:this.labelWidth},
	                {select: "onScriptTypeChange"}),
				CWHF.createTextAreaField('admin.customize.script.lbl.sourceCode', "sourceCode",
						{labelWidth:this.labelWidth,anchor: '100% -60'})
			];
    },
    
    postDataProcess: function(data, panel) {
    	var scriptTypeCombo = panel.getComponent("scriptType");
		scriptTypeCombo.store.loadData(data["scriptTypeList"]);
		scriptTypeCombo.setValue(data["scriptType"]);
	}
  
});
