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

Ext.define("com.trackplus.admin.customize.filter.FilterParameter", {
	extend: "com.trackplus.admin.WindowBase",
	xtype: "filterParameter",
    controller: "filterParameter",
    //scope for the static filter operations 
   
	width: 1000,
    height: 800,
    
    
    loadUrl: "filterParameters!renderParameters.action",
    
    initTitle: function() {
    	return getText("admin.customize.queryFilter.lbl.parameters");
    },
    
    initActions: function() {
		this.actionApply = CWHF.createAction("common.btn.apply", "filterInst", "onApply", {tooltip:getText("common.btn.applyFilter")});
    	this.actionCancel = CWHF.createAction(this.getCancelButtonKey(), this.getCancelIconCls(), "onCancel");
    	this.actions = [this.actionApply, this.actionCancel];
    },
    
    /**
     * Get the panel items
     * Empty at the beginning: add the fields dynamically in postDataProcess 
     */
    getFormFields: function() {
    	return [];
    },
    
    postDataProcess: function(data, formPanel) {
    	com.trackplus.admin.Filter.postLoadProcessTreeFilterParameters(data, formPanel);
    }
});
