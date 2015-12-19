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

Ext.define("com.trackplus.admin.customize.list.ListEdit", {
	extend: "com.trackplus.admin.WindowBase",
	xtype: "listEdit",
    controller: "listEdit",
    
    operation: null,
    isLeaf: true,
    isAddOperation: true,
    parentViewRecord: null,
    parentViewConfig: null,
    selectedTreeNode: null,
    labelWidth: 120,
    
    /**
	 * Initialization method
	 */
	/*protected*/initBase : function() {
		var entityContext = this.getEntityContext();
		this.operation = entityContext.operation;
		this.isLeaf = entityContext.isLeaf;
		this.isAddOperation = entityContext.add;
		this.parentViewRecord = entityContext.record;
		this.parentViewConfig = entityContext.config;
		this.selectedTreeNode = entityContext.selectedTreeNode;
		this.initActions();
	},
    
    
    initWidth : function() {
    	if (this.isListOperation()) {
            return 500;
        } else {
            return 450;
        }
    },

    initHeight : function() {
    	if (this.isListOperation()) {
            return 200;
        } else {
            return 350;
        }
    },
    
    loadUrl: "listOptions!edit.action",
    
    initActions: function() {
    	this.actionSave = CWHF.createAction(this.getSaveButtonKey(), this.getSaveIconCls(), "onSave", {tooltip:this.title});
    	this.actionCancel = CWHF.createAction(this.getCancelButtonKey(), this.getCancelIconCls(), "onCancel");
    	this.actions = [this.actionSave, this.actionCancel];
    },
    
    /**
	 * Whether the operation refers to a list or a list option
	 */
    isListOperation : function() {
    	if (this.isProjectSpecificRoot()) {
    		return true;	
    	}
        // in the tree either the global lists node or a project
		// node is selected
        var listParentNode = this.selectedTreeNode.get("nodeChildrenListForLabel") === -20
                || this.selectedTreeNode.get("nodeChildrenListForLabel") === -21;
        if ((listParentNode && (this.operation === "add" || ((this.operation === "edit" || this.operation === "copy") && !this.fromTree)))
                || // add from tree or edit or copy from grid
                ((this.operation === "edit" || this.operation === "copy") && this.fromTree && (CWHF.isNull(this.parentViewRecord.get("optionID")) || this.parentViewRecord.get("optionID") === 0))) {
            return true;
        } else {
            return false;
        }
    },
    
    isProjectSpecificRoot: function() {
    	if (this.parentViewConfig && this.parentViewRecord) {
    		//project specific list from project configuration
    		return this.parentViewConfig["fromProjectConfig"] && this.parentViewRecord.get("id") === this.parentViewConfig["rootID"];
    	}
    	return false;
    },
    
    /**
     * Get the panel items
     */
    getFormFields : function() {
    	var canEdit = false;
        if (this.operation === "add" || this.operation === "copy") {
            canEdit = true;
        } else {
            if (this.operation === "edit") {
                canEdit = this.parentViewRecord.get("canEdit");
            }
        }
        if (this.isListOperation()) {
            return this.getCustomListWindowItems(this.isAddOperation, canEdit);
        } else {
            var hasTypeflag;
            var disableTypeflag;
            var hasCssStyle;
            var hasPercentComplete;
            var hasDefaultOption;
            if (this.isAddOperation) {
                hasTypeflag = this.parentViewRecord.get("childrenHaveTypeflag");
                disableTypeflag = this.parentViewRecord.get("disableTypeflag");
                hasCssStyle = this.parentViewRecord.get("childrenHaveCssStyle");
                hasPercentComplete = this.parentViewRecord.get("childrenHavePercentComplete");
                hasDefaultOption = this.parentViewRecord.get("childrenHaveDefaultOption");
            } else {
                hasTypeflag = this.parentViewRecord.get("hasTypeflag");
                disableTypeflag = this.parentViewRecord.get("disableTypeflag");
                hasCssStyle = this.parentViewRecord.get("hasCssStyle");
                hasPercentComplete = this.parentViewRecord.get("hasPercentComplete");
                hasDefaultOption = this.parentViewRecord.get("hasDefaultOption");
            }
            return this.getListOptionWindowItems(hasTypeflag, disableTypeflag, hasCssStyle,
                    hasPercentComplete, hasDefaultOption, canEdit);
        }
    },
    
    /**
     * Prepare adding/editing a custom list
     */
    getCustomListWindowItems : function(add, canEdit) {
        var windowItems = [ CWHF.createTextField("common.lbl.name", "label", {
            disabled : !canEdit,
            allowBlank : false,
            labelWidth : this.labelWidth
        }) ];
        windowItems.push(CWHF.createTextAreaField("common.lbl.description", "description", {
            disabled : !(add || canEdit),
            labelWidth : this.labelWidth
        }));
        return windowItems;
    },

    /**
     * Prepare adding/editing a system or custom list entry
     */
    getListOptionWindowItems : function(hasTypeflag, disableTypeflag, hasCssStyle,
            hasPercentComplete, hasDefaultOption, canEdit) {
        //dialog for adding/editing a list option
        var windowItems = [ CWHF.createTextField("common.lbl.name", "label", {
            disabled : !canEdit,
            allowBlank : false,
            labelWidth : this.labelWidth
        }) ];
        if (hasTypeflag) {
            //type flag combo
            windowItems.push(CWHF.createCombo("admin.customize.list.lbl.typeflag", "typeflag", {
            	itemId: "typeflag",
                disabled : disableTypeflag || !canEdit,
                labelWidth : this.labelWidth
            }));
        }
        if (hasPercentComplete) {
            windowItems.push(CWHF.createNumberField("admin.customize.list.lbl.percentComplete",
                    "percentComplete", 0, 0, 100, {
                        disabled : !canEdit,
                        labelWidth : this.labelWidth
                    }));
        }
        if (hasDefaultOption) {
            windowItems.push(CWHF.createCheckbox("admin.customize.list.lbl.defaultOption",
                    "defaultOption", {
                        disabled : !canEdit,
                        labelWidth : this.labelWidth
                    }));
        }
        if (hasCssStyle) {
            windowItems.push({
                xtype : "fieldset",
                itemId : "fsStyle",
                //width: this.FieldSetWidth,
                title : getText("admin.customize.list.lbl.style"),
                collapsible : false,
                defaults : {
                    anchor : "100%"
                },
                layout : "anchor",
                items : [
                        CWHF.createColorPicker("admin.customize.list.lbl.style.bgrColor",
                                "cssStyleBean.bgrColor", {
                        			itemId : "cssStyleBeanBgrColor",
                                    disabled : !canEdit,
                                    labelWidth : this.labelWidth
                                }),
                        CWHF.createColorPicker("admin.customize.list.lbl.style.color",
                                "cssStyleBean.color", {
                        			itemId : "cssStyleBeanColor",
                                    disabled : !canEdit,
                                    labelWidth : this.labelWidth
                                }),
                        CWHF.createCombo("admin.customize.list.lbl.style.fontWeight",
                                "cssStyleBean.fontWeight", {
                        			itemId: "fontWeight",
                                    disabled : !canEdit,
                                    idType : "string",
                                    labelWidth : this.labelWidth
                                }),
                        CWHF.createCombo("admin.customize.list.lbl.style.fontStyle",
                                "cssStyleBean.fontStyle", {
                        			itemId: "fontStyle",
                                    disabled : !canEdit,
                                    idType : "string",
                                    labelWidth : this.labelWidth
                                }),
                        CWHF.createCombo("admin.customize.list.lbl.style.text-decoration",
                                "cssStyleBean.textDecoration", {
                        			itemId: "textDecoration",
                                    disabled : !canEdit,
                                    idType : "string",
                                    labelWidth : this.labelWidth
                                }) ]
            });
        }
        return windowItems;
    },

    
    postDataProcess: function(data, formPanel) {
    	if (!this.isListOperation()) {
            this.postDataLoadCombos(data, formPanel);
        }
    },
    
    /**
     * Load the combos after the result has arrived containing also the combo data sources
     */
    postDataLoadCombos: function(data, panel) {
        var typeFlagsList = panel.getComponent("typeflag");
        if (typeFlagsList ) {
            typeFlagsList.store.loadData(data["typeflagsList"]);
            typeFlagsList.setValue(data["typeflag"]);
        }
        var fsStyle = panel.getComponent("fsStyle");
        if (fsStyle ) {
            var fontWeight = fsStyle.getComponent("fontWeight");
            if (fontWeight ) {
                fontWeight.store.loadData(data["fontWeightsList"]);
            }
            var fontStyle = fsStyle.getComponent("fontStyle");
            if (fontStyle ) {
                fontStyle.store.loadData(data["fontStylesList"]);
            }
            var textDecoration = panel.getComponent("fsStyle").getComponent("textDecoration");
            if (textDecoration ) {
                textDecoration.store.loadData(data["textDecorationsList"]);
            }
            var bgrColor = fsStyle.getComponent("cssStyleBeanBgrColor");
            if (bgrColor  && data["cssStyleBean.bgrColor"] ) {
                bgrColor.setFieldStyle('background-color:#' + data["cssStyleBean.bgrColor"]
                        + '; background-image: none;');
            }
            var color = fsStyle.getComponent("cssStyleBeanColor");
            if (color  && data["cssStyleBean.color"] ) {
                color.setFieldStyle('background-color:#' + data["cssStyleBean.color"]
                        + '; background-image: none;');
            }
        }
    }
});
