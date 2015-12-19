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

Ext.define("com.trackplus.admin.customize.objectStatus.ObjectStatusEdit", {
	extend: "com.trackplus.admin.WindowBase",
	xtype: "objectStatusEdit",
    controller: "objectStatusEdit",
    
    width: 500,
    height: 150,
    
    loadUrl: "objectStatus!edit.action",
    
    initActions: function() {
    	this.actionSave = CWHF.createAction(this.getSaveButtonKey(), this.getSaveIconCls(), "onSave", {tooltip:this.title});
    	this.actionCancel = CWHF.createAction(this.getCancelButtonKey(), this.getCancelIconCls(), "onCancel");
    	this.actions = [this.actionSave, this.actionCancel];
    },
    
    /**
     * Get the panel items
     * recordData: the record data (for the record to be edited or added to)
     * isLeaf: whether add a leaf or a folder
     * add: whether it is add or edit
     * fromTree: operations started from tree or from grid
     * operation:  the name of the operation
     */
    getFormFields : function() {
    	var entityContext = this.getEntityContext();
    	var record = entityContext.record;
    	var operation = entityContext.operation;
    	var nodeID = recordData["id"];
		var canEdit = false;
		if (operation==="add" || operation==="copy") {
			canEdit = true;
		} else {
			if (operation==="edit") {
				canEdit=record.get("modifiable");
			}
		}
		return this.getListOptionWindowItems(nodeID, canEdit);
    },
    
    /**
	* Prepare adding/editing a system or custom list entry
	*/
	getListOptionWindowItems: function(node, canEdit) {
		//dialog for adding/editing a list option
		var windowItems = [CWHF.createTextField(
				'common.lbl.name', "label", {disabled:!canEdit, allowBlank:false, labelWidth:150})];
		//type flag combo
		windowItems.push(CWHF.createCombo('admin.customize.list.lbl.typeflag',
				"typeflag", {itemId:"typeflagsList", disabled:!canEdit, labelWidth:150}));
		return windowItems;
	},
	
	/**
	 * Load the combos after the result has arrived containing also the combo data sources
	 */
	postDataProcess: function(data, panel) {
		 var typeFlagsList = panel.getComponent("typeflagsList");
	        if (typeFlagsList ) {
	            typeFlagsList.store.loadData(data["typeflagsList"]);
	            typeFlagsList.setValue(data["typeflag"]);
	        }
	        
		/*if (panel.items.items) {
			Ext.Array.forEach(panel.items.items,function(item) {
				//for each combo-type control set the value also:
				//for a combos itemId <xxx>sList the json result should contain
				//both <xxx>sList for the combo datasource and <xxx> for the combo's actual value
				//the json field for value is
				if (item.xtype === "combo") {
					var comboSource = data[item.itemId];
					item.store.loadData(comboSource);
					//-'Lists'
					item.setValue(data[item.itemId.substring(0,item.itemId.length-5)]);
				}
			});
		}*/
	}
});
