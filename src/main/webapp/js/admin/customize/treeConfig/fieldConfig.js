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


Ext.define("com.trackplus.admin.customize.treeConfig.FieldConfig",{
	extend:"com.trackplus.admin.customize.treeConfig.TreeConfig",
	xtype: "fieldConfig",
    controller: "fieldConfig",
    treeWidth: 250,
	config: {
		rootID:"field"
	},
	baseServerAction: "fieldConfigItemDetail",
	
		
	//fieldSetWidth: 500,
	//textFieldWidth: 350,
	//labelWidth: 120,
	//extra actions for field configuration

	/**
	 * The message to appear first time after selecting this menu entry
	 * Is should be shown by selecting the root but the root is typically not visible
	 */
	getRootMessage: function(rootID) {
		return getText("admin.customize.field.config.lbl.description");
	},

	/**
	 * The localized leaf name
	 */
	getEntityLabel: function() {
		return getText("admin.customize.field.lbl.fieldConfig");
	},

	/**
	 * Add "Field" not "Field Config" (getEntityLabel() does not fit here)
	 */
	getAddLabel: function() {
		return getText(this.getAddTitleKey(), getText("admin.customize.field.lbl.field"));
	},

	/**
	 * Delete "Field" not "Field Config" (getEntityLabel() does not fit here)
	 */
	getDeleteLabel: function() {
		return getText(this.getDeleteTitleKey(), getText("admin.customize.field.lbl.field"));
	},

	initActions: function() {
		this.actionSave = CWHF.createAction(this.getSaveButtonKey(), this.getSaveIconCls(),
	    		"onSave", {tooltip:this.getActionTooltip(this.getSaveTitleKey()), disabled:true});
		this.callParent();
		this.actions = [this.actionSave, this.actionOverwrite, this.actionReset];
	},

	/**
	 * Initialize all actions and return the toolbar actions
	 */
	/*getToolbarActions: function() {
		var actions = [this.actionSave, this.actionOverwrite, this.actionReset];
		
		actions = [this.actionAddLeaf, this.actionSave, this.actionOverwrite, this.actionReset, this.actionDelete];
		return actions;
	},*/

	/**
	 * Which actions to enable/disable depending on tree selection
	 */
	getToolbarActionChangesForTreeNodeSelect: function(selectedNode) {
		if (selectedNode) {
			var leaf = selectedNode.isLeaf();
			var inheritedConfig=selectedNode.data['inheritedConfig'];
			var defaultConfig=selectedNode.data['defaultConfig'];
			this.actionSave.setDisabled(!leaf || inheritedConfig);
			this.actionOverwrite.setDisabled(!leaf || !inheritedConfig);
			this.actionReset.setDisabled(inheritedConfig || defaultConfig);
		} else {
			//add field
			this.actionSave.setDisabled(false);
			this.actionOverwrite.setDisabled(true);
			this.actionReset.setDisabled(true);
		}
	},

	/**
	 * Get the actions available in context menu depending on the currently selected row
	 *
	 */
	getTreeContextMenuActions: function(selectedRecord, selectionIsSimple) {
		var actions = [];
		if (selectionIsSimple) {
			var inheritedConfig = selectedRecord.get("inheritedConfig");
			var defaultConfig = selectedRecord.get("defaultConfig");
			var isLeaf = selectedRecord.isLeaf();
			if (inheritedConfig && !defaultConfig && isLeaf) {
				actions.push(this.actionOverwrite);
			}
			if (!inheritedConfig && !defaultConfig) {
				actions.push(this.actionReset);
			}
			if (!isLeaf) {
				actions.push(this.actionReload);
			}
		}
		return actions;
	},

	/**
	 * Gets the folder details
	 */
	/*getFolderDetailItems: function(node, add, responseJson) {
		return this.getInfoBoxItems(responseJson.info);
	},*/

	/**
	 * Gets the leaf details
	 */
	/*getLeafDetailItems: function(node, add) {
		return [{
				xtype:'hidden',
				name: 'add',
				value: add
			},{
				xtype: 'fieldset',
				title: getText('admin.customize.field.config.detail.lbl.fieldConfig'),
				itemId:"fieldFieldset",
				defaultType : 'textfield',
				style:{
					marginTop: '6px'
				},
				defaults : {
					labelStyle:'overflow: hidden;',
					msgTarget : 'side',
					anchor : '-20',
					width: this.textFieldWidth
				},
				width: this.fieldSetWidth,
				items: this.getFieldItems()
			}, {
				xtype: 'fieldset',
				title: getText('admin.customize.field.config.detail.lbl.generalConfig'),
				itemId:"fieldConfigFieldset",
				defaultType : 'textfield',
				// margin:'0 0 0 5',
				defaults : {
					labelStyle:'overflow: hidden;',
					msgTarget : 'side',
					anchor : '-20',
					width: this.textFieldWidth
				},
				width: this.fieldSetWidth,
				items: this.getFieldConfigItems()
			}
		];
	},*/

	/**
	 * Get field details
	 */
	/*getFieldItems: function() {
		return [CWHF.createTextField("common.lbl.name", "name",
					{itemId: "name", labelWidth:this.labelWidth,
					width:this.textFieldWidth, allowBlank : false,
					stripCharsRe : /\s|:|\<|\>|\/|\.|\(|\)|\'|\"|\,/}),
				CWHF.createCombo("admin.customize.field.lbl.fieldType", "fieldType",
					{itemId: "fieldType", labelWidth:this.labelWidth, hidden: true, idType: "string"},
					{select: {fn:this.selectFieldType, scope:this}}, "fieldType"),
				CWHF.createCheckbox("admin.customize.field.lbl.filterField", "filterField",
					{itemId: "filterField", labelWidth:this.labelWidth, hidden: true}),
				CWHF.createCheckbox("admin.customize.field.lbl.deprecated", "deprecated",
					{itemId: "deprecated", labelWidth:this.labelWidth, hidden: true}),
				CWHF.createTextAreaField("common.lbl.description", "description",
					{itemId: "description", labelWidth:this.labelWidth, width:450})];
	},*/

	/**
	 * Get field config details
	 */
	/*getFieldConfigItems: function() {
		return [CWHF.createTextField("common.lbl.label", "label",
				{itemId: "label", labelWidth:this.labelWidth,
					width:this.textFieldWidth, allowBlank : false}),
				CWHF.createTextField("common.lbl.tooltip", "tooltip",
					{itemId: "tooltip", labelWidth:this.labelWidth,
					width:this.textFieldWidth}),
				CWHF.createCheckbox("admin.customize.field.config.detail.lbl.required", "required",
					{itemId: "required", labelWidth:this.labelWidth}),
				CWHF.createCheckbox("admin.customize.field.config.detail.lbl.history", "history",
					{itemId: "history", labelWidth:this.labelWidth})];
	},*/

	/**
	 * Post process for leaf load
	 */
	/*getEditLeafPostDataProcess: function(recordData, add) {
		return this.fieldPostDataProcess;
	},*/

	/**
	 * Post process for folder load (only for add)
	 */
	/*getEditFolderPostDataProcess: function(recordData, add) {
		if (add) {
			//add from a non leaf node: same as add to leaf node or add when no node is selected
			return this.fieldPostDataProcess;
		} else {
			//no post process needed for folder selections
			return null;
		}
	},*/

	/**
	 * Method to process the data to be loaded arrived from the server by editing the leaf
	 */
	/*fieldPostDataProcess: function(data, panel) {
		var fieldFieldset = panel.getComponent("fieldFieldset");
		var fieldConfigFieldset = panel.getComponent("fieldConfigFieldset");
		var fieldAttributesDisabled = data.fieldAttributesDisabled;
		fieldFieldset.getComponent('name').setDisabled(fieldAttributesDisabled);
		var inheritedConfig = data.inheritedConfig;
		var customField = data.customField;
		var fieldTypeCombo = fieldFieldset.getComponent('fieldType');
		if (customField) {
			fieldTypeCombo.setVisible(true);
			fieldTypeCombo.store.loadData(data.fieldTypesList);
			fieldTypeCombo.setValue(data.fieldType);
			fieldTypeCombo.setDisabled(data.fieldTypeDisabled || fieldAttributesDisabled);
		}
		//if (data.renderDeprecated) {
			fieldFieldset.getComponent('deprecated').setVisible(data.renderDeprecated);
			fieldFieldset.getComponent('deprecated').setDisabled(fieldAttributesDisabled);
		//}
		//if (data.renderFilterField) {
			fieldFieldset.getComponent('filterField').setVisible(data.renderFilterField);
			fieldFieldset.getComponent('filterField').setDisabled(fieldAttributesDisabled);
		//}
		fieldFieldset.getComponent('description').setDisabled(fieldAttributesDisabled);

		fieldConfigFieldset.getComponent('label').setDisabled(inheritedConfig);
		fieldConfigFieldset.getComponent('tooltip').setDisabled(inheritedConfig);

		//if (data.renderRequired) {
			//fieldConfigFieldset.getComponent('required').setVisible(data.renderRequired);
			fieldConfigFieldset.getComponent('required').setDisabled(inheritedConfig || !data.renderRequired);
		//}
		//if (data.renderHistory) {
			//fieldConfigFieldset.getComponent('history').setVisible(data.renderHistory);
			fieldConfigFieldset.getComponent('history').setDisabled(inheritedConfig || !data.renderHistory);
		//}
		var specificItem = panel.getComponent("specificItem");
		if (specificItem) {
			//the value will not be removed from Ext.form.Basic only from the UI
			//workaround: set the fields to disabled to not to be submitted then remove the component
			//it seems to be enough to disable the panel not necessarily the components one by one
			specificItem.setDisabled(true);
			panel.remove(specificItem);
		}
		var specificFieldConfigClass = data['specificFieldConfigClass'];
		if (specificFieldConfigClass && specificFieldConfigClass!=="") {
			this.addSpecificConfig(panel, specificFieldConfigClass, this.fieldSetWidth);
			var specificItem = panel.getComponent("specificItem");
			if (specificItem) {
				//specificItem.setWidth(this.fieldSetWidth);
				data['node'] = this.selectedNodeID;
				data['labelWidth'] = this.labelWidth;
				specificItem.onDataReady(data);
			}
		}
		if (customField && data['canDelete']) {
			this.actionDelete.setDisabled(false);
		}
	},*/

	/**
	 * Add the field type specific configuration
	 */
	/*addSpecificConfig: function(panel, specificFieldConfigClass, width) {
		if (specificFieldConfigClass) {
			var specificItem = Ext.create(specificFieldConfigClass,{
				// margin:'0 0 0 5',
				width: width
			});
			specificItem.setTitle(getText('admin.customize.field.config.detail.lbl.specificConfig'));
			if (specificItem) {
				panel.add(specificItem);
				panel.updateLayout();
			}
		}
	},*/
	
	/**
	 * Prepare the form for adding a new field
	 */
	/*onAddLeaf: function() {
		var node = this.getSingleSelectedRecord(true);
		this.loadDetailPanelWithFormLoad(node, true);
		this.actionSave.setDisabled(false);
	},*/

	/**
	 * Save the detail part
	 * The 'add' parameter is added as hidden field to the corresponding form detail items
	 * Alternatively this should be included as extra parameter here if 'add' were declared
	 * as class level variable set in onAdd action and reset on treeNode select
	 */
	/*onSave: function(extraConfig) {
		this.prepareValidate(this.centerPanel, true);
		this.centerPanel.getForm().submit({
			url: this.getBaseAction() + '!save.action',
			params: this.getNodeParam(extraConfig),
			scope: this,
			success: function(form, action) {
				var result = action.result;
				if (result.success) {
					this.refreshAfterSaveSuccess(result);
				} else {
					com.trackplus.util.showError(result);
				}
			},
			failure: function(form, action) {
				com.trackplus.util.submitFailureHandler(form, action);
			}
		})
	},*/

	/**
	* By selecting a new field type the validation should be disabled
	* because a submit should be possible to the server even with incomplete data.
	* (even the incomplete data should not be lost)
	* By saving a field config the validation should be enabled
	**/
	/*prepareValidate: function(panel, validate) {
		var fieldFieldset = panel.getComponent("fieldFieldset");
		fieldFieldset.getComponent('name').allowBlank = !validate;
		var fieldConfigFieldset = panel.getComponent("fieldConfigFieldset");
		fieldConfigFieldset.getComponent('label').allowBlank = !validate;
	},*/

	/**
	 * By selecting another field type update the proxy urls
	 * for the grid store and reload the data into the store
	 */
	/*selectFieldType: function(comboBox, record, index ) {
		var onSuccessOrFail = function(form, action) {
			var result = action.result;
			if (result.success) {
				this.postProcessFieldConfig(this.centerPanel, action.result.data);
			} else {
				Ext.MessageBox.alert(this.failureTitle, action.response.responseText);
			}
		};
		//var panel = this.centerPanel.getComponent(0);
		//deactivate validation during the field type change
		this.prepareValidate(this.centerPanel, false);
		this.centerPanel.getForm().submit({
			fromCenterPanel:true,
			url : this.getBaseAction() + '!load.action',
			scope: this,
			params: this.getNodeParam(),
			success : function(form, action) {
				var result = action.result;
				if (result.success) {
					this.fieldPostDataProcess(action.result.data, this.centerPanel);
				} else {
					com.trackplus.util.showError(result);
				}
			},
			failure : function(form, action) {
					com.trackplus.util.submitFailureHandler(form, action);
				}
			});
	},*/

	/**
	 * Parameters for adding a new leaf
	 * The 'add' parameter is added as hidden fields to the corresponding form detail items
	 * (Alternatively this should be set here if 'add' is declared as class level variable,
	 * set in onAdd action and reset on treeNode select)
	 */
	/*getNodeParam: function(extraConfig) {
		var saveParams = this.callParent(arguments);
		var renameConfirmed = false;
		if (extraConfig) {
			renameConfirmed = extraConfig.renameConfirmed;
			if (renameConfirmed) {
				saveParams['renameConfirmed'] = renameConfirmed;
			}
		}
		return saveParams;
	},*/

	/**
	 * Rename a field
	 */
	/*renameField: function(btn) {
		if (btn === 'yes') {
			this.onSave.call(this, {renameConfirmed:true});
		}
	},*/

	/**
	 * Refresh after a successful save
	 */
	/*refreshAfterSaveSuccess: function(result) {
		errorMessage = result.errorMessage;
		if (errorMessage) {
			Ext.MessageBox.show({title:getText('admin.customize.field.config.detail.lbl.rename'),
					msg: errorMessage,
					buttons: Ext.Msg.YESNO,
					icon: Ext.Msg.WARNING,
					fn: this.renameField,
					scope:this});
		} else {
			var refreshTree = result.refreshTree;
			if (refreshTree) {
				//refresh the entire tree after adding a new field or changing the name of an existing field
				//if it was not added from a "Custom fields" node then it can be that the parents are not loaded
				//but after adding the new field should be selected. Consequently the parents should be loaded
				//because the refreshTreeOpenBranches() works only for already loaded branches
				this.expandAscendentPath.call(this, result.pathToExpand, result.node);
			} else {
				//save an existing field configuration: no tree refresh needed
				var nodeToReload=this.tree.getStore().getNodeById(result.node);
				if (nodeToReload) {
					this.onTreeNodeSelect(null, nodeToReload);
				}
			}
		}
	},*/

	/**
	 * Load and expand a path (ascendent hierarchy from ancestors to leaf) in the tree
	 * Once the path is expanded the refreshTreeOpenBranches is called
	 */
	/*expandAscendentPath: function(pathToExpand, node, actualParent) {
		if (actualParent) {
			actualParent.expand(false);
		}
		if (pathToExpand && pathToExpand.length>0) {
			var parentNodeID = pathToExpand.shift();
			parentNode=this.tree.getStore().getNodeById(parentNodeID);
						this.tree.getStore().load({node:parentNode, callback:function() {
								this.expandAscendentPath(pathToExpand, node, parentNode)
							},
							scope:this});
					
		} else {
			//refresh the entire tree
			this.refreshTreeOpenBranches(this.tree.getRootNode(), false, false, actualParent, node);
		}
	},*/

	/**
	 * The localized entity name to be deleted
	 * Should be implemented only if differs from entity name
	 * (for example in field configuration: entity label is "field configuration" but delete label is "field")
	 */
	/*getDeleteEntityLabel:function() {
		//because a field will be deleted, not a field config
		return getText('admin.customize.field.lbl.field');
	},*/

	/**
	 * Delete handler
	 */
	/*onDelete: function() {
		var selectedRecords = this.getSelectedRecords(true);
		if (selectedRecords) {
			var isLeaf = this.selectedIsLeaf(true);
			if (isLeaf) {
				var extraConfig = {fromTree:true, isLeaf:isLeaf};
				this.deleteHandler(selectedRecords, extraConfig);
			}
		}
	},*/

	/**
	 * Reload after delete
	 */
	/*reload: function(refreshParamsObject) {
		this.refreshTreeOpenBranches(refreshParamsObject.root, false, false, refreshParamsObject.parentNode, refreshParamsObject.nodeIdToSelect);
	},*/

	/**
	 * Gets reload parameters after delete
	 */
	/*getReloadParamsAfterDelete: function(selectedRecords, extraConfig, responseJson) {
		//select the parent node of the deleted node
		var nodeToSelect = selectedRecords.parentNode;
		var parentNode = null;
		//parent node of the node to be selected
		if (nodeToSelect) {
			parentNode = nodeToSelect.parentNode;
		}
		return {root:this.tree.getRootNode(), parentNode:nodeToSelect, nodeIdToSelect:nodeToSelect.data['id']}
	}*/
});
