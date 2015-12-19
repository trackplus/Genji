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

Ext.define("com.trackplus.admin.DeleteWindow",{
	extend: "com.trackplus.admin.WindowBase",
	xtype: "deleteWindow",
    controller: "deleteWindow",

    width: 500,
    height: 200,

    getLoadUrl:function() {
    	return this.getEntityContext().deleteUrlBase + "!renderReplace.action"
    },

    initActions: function() {
    	this.actionDelete = CWHF.createAction(this.getDeleteButtonKey(), this.getDeleteIconCls(), "onDelete", {tooltip:getText(this.getDeleteTitleKey(), this.getEntityContext().entityLabel)});
    	this.actionCancel = CWHF.createAction(this.getCancelButtonKey(), this.getCancelIconCls(), "onCancel");
    	this.actions = [this.actionDelete, this.actionCancel];
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
    	var replacementFormFields = [{xtype : "label", itemId: "replacementWarning"}];
    	replacementFormFields.push(this.getReplacementComponent());
		return replacementFormFields;
	},

	/**
	 * Gets the replacement control
	 */
    getReplacementComponent: function () {
    	var replacementIsTree = this.getEntityContext().replacementIsTree;
    	if (replacementIsTree) {
    		return CWHF.createSingleTreePicker("Replacement", "replacementID", [], null, {
    	        allowBlank : false,
    	        blankText : getText('common.err.replacementRequired', this.getEntityContext().entityLabel),
    	        labelWidth : 200,
    	        margin : '5 0 0 0',
    	        itemId:'replacementID'
    	    });
    	} else {
    		return CWHF.createCombo("Replacement", "replacementID",
					{itemId:"replacementID",
					labelWidth:200,
					allowBlank:false,
					blankText: getText("common.err.replacementRequired", this.getEntityContext().entityLabel)});
    	}
    },

    /**
	 * Load the data in the replacement panel when it arrives from server
	 * the complete replacementWarning could be composed on the server
	 * If not, it will be composed on the client, but at least
	 * the label of the entity to be deleted should be specified
	 */
	postDataProcess: function(data, panel, extraConfig) {
		var replacementWarning = panel.getComponent("replacementWarning");
		var replacementWarningText = data["replacementWarning"];
		if (CWHF.isNull(replacementWarningText)) {
			var label = data["label"];
			replacementWarningText = getText("common.lbl.replacementWarning", this.getEntityContext().entityLabel, label);
			replacementWarningText = replacementWarningText + getText("common.lbl.cancelDeleteAlert");
		}
		replacementWarning.setText(replacementWarningText, false);
		var replacementComponent = panel.getComponent("replacementID");
		this.loadReplacementComponent(data, replacementComponent);
		var replacementListLabel = data["replacementListLabel"];
		if (CWHF.isNull(replacementListLabel)) {
			replacementListLabel = getText(this.getReplacementTitleKey(), this.getEntityContext().entityLabel);
		}
		replacementComponent.labelEl.dom.innerHTML = replacementListLabel;
	},

	/**
	 * Load the combo or the tree
	 */
	loadReplacementComponent: function (data, replacementComponent) {
		var replacementIsTree = this.getEntityContext().replacementIsTree;
    	if (replacementIsTree) {
    		//is a tree
    		replacementComponent.updateMyOptions(data["replacementTree"]);
    	} else {
    		//is a combo
    		replacementComponent.store.loadData(data["replacementList"]);
    		replacementComponent.setValue(null);
    	}
	}
});
