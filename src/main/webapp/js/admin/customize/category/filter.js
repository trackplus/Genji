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

com.trackplus.admin.Filter = function() {
};

com.trackplus.admin.Filter.tqlFilterWindowWidth = 500;
com.trackplus.admin.Filter.tqlFilterWindowHeight = 400;

com.trackplus.admin.Filter.issueFilterWindowWidth = 910;
com.trackplus.admin.Filter.issueFilterWindowHeight = 500;

com.trackplus.admin.Filter.notifyFilterWindowWidth = 900;
com.trackplus.admin.Filter.notifyFilterWindowHeight = 500;

com.trackplus.admin.Filter.nameWidth = 300;
com.trackplus.admin.Filter.nameLabelWidth = 80;

com.trackplus.admin.Filter.menuLabelWidth = 100;
com.trackplus.admin.Filter.styleFieldLabelWidth = 100;
com.trackplus.admin.Filter.styleFieldWidth = 200;

com.trackplus.admin.Filter.viewLabelWidth = 100;
com.trackplus.admin.Filter.viewFieldWidth = 250;

//the number of  upper selects columns
com.trackplus.admin.Filter.upperSelectColumns = 4;


//the width and height of the other multiple selects
com.trackplus.admin.Filter.multiSelectWidth = 210;
//com.trackplus.admin.Filter.multiSelectHeight = 120;
com.trackplus.admin.Filter.multipleTreePickerWidth = 500;

com.trackplus.admin.Filter.moreControlWidth = 500;
com.trackplus.admin.Filter.moreControlLabelWidth = 150;

com.trackplus.admin.Filter.matcherComboWidth = 150;
com.trackplus.admin.Filter.fieldNameComboWidth = 150;
com.trackplus.admin.Filter.operationComboWidth = 80;
com.trackplus.admin.Filter.parenthesisComboWidth = 50;
com.trackplus.admin.Filter.fieldMomentComboWidth = 70;

/**
 * Executes the last executed filter by refreshing th entire page
 */
com.trackplus.admin.Filter.executeInstantFilter = function(refreshParameters, result) {
	window.location.href = com.trackplus.TrackplusConfig.contextPath + "/itemNavigator.action";
};

/**
* Prepare adding/editing a tql filter
*/
com.trackplus.admin.Filter.getTQLFilterItems = function(modifiable) {
	return [CWHF.createHiddenField("modifiable", {value:modifiable}),
			CWHF.createTextField("common.lbl.name", "label",
				{disabled:!modifiable,
				allowBlank:false,
				width:com.trackplus.admin.Filter.nameWidth,
				labelWidth:com.trackplus.admin.Filter.nameLabelWidth,
				msgTarget: "qtip"}),
			CWHF.createCombo("admin.customize.queryFilter.lbl.filterType",
				"filterSubType", {disabled:!modifiable, labelWidth:com.trackplus.admin.Filter.styleFieldLabelWidth, itemId:"filterSubType"}),
			CWHF.createCheckbox("admin.customize.queryFilter.lbl.menu",
						"includeInMenu", {itemId:'includeInMenu', labelWidth:com.trackplus.admin.Filter.menuLabelWidth}),
			CWHF.createCombo("admin.customize.queryFilter.lbl.styleField",
				"styleField", {labelWidth: com.trackplus.admin.Filter.styleFieldLabelWidth, itemId:"styleField"}),
			CWHF.createCombo(
					"admin.customize.queryFilter.lbl.view",
					"viewID",
					{disabled:!modifiable,
					labelWidth:com.trackplus.admin.Filter.viewLabelWidth,
					width: com.trackplus.admin.Filter.viewFieldWidth,
					data:data["viewList"],
					value: data["viewID"]}),
			CWHF.createTextAreaField("admin.customize.queryFilter.lbl.filterExpression", "tqlExpression",
				{disabled:!modifiable,
				allowBlank:false,
				labelWidth:com.trackplus.admin.Filter.styleFieldLabelWidth,
				height:200})];
};

/**
* Prepare adding/editing a tree filter
*/
com.trackplus.admin.Filter.getTreeFilterItems = function(modifiable, instant) {
	var mainComponents = [CWHF.createHiddenField("filterSubType", {value:1}),
				CWHF.createHiddenField("modifiable", {value:modifiable})];
	var generalItems = [];
	if (!instant) {
		generalItems = [CWHF.createTextField("common.lbl.name", "label",
						{disabled:!modifiable,
						allowBlank:false,
						labelWidth:com.trackplus.admin.Filter.nameLabelWidth,
						width:com.trackplus.admin.Filter.nameWidth,
						msgTarget: "qtip"}),
					CWHF.createCheckbox("admin.customize.queryFilter.lbl.menu",
							"includeInMenu", {itemId:'includeInMenu', labelWidth:com.trackplus.admin.Filter.menuLabelWidth, width: 100})];
		mainComponents.push(Ext.create("Ext.panel.Panel", {
			itemId: "generalFilterFields",
			defaults:	{
				labelStyle:"overflow: hidden;",
				margin:"0 0 5 0"
			},
			layout: {
				type:"table",
				columns:4
			},
			border:false,
			items: generalItems}));
	}
	return mainComponents;
};

/**
* Prepare adding/editing tree filter parameters
*/
com.trackplus.admin.Filter.getTreeFilterParameterItems = function() {
	return [];
};

/**
 * Prepare adding/editing a notification filter
 */
com.trackplus.admin.Filter.getNotifyFilterItems = function(modifiable) {
	return [CWHF.createTextField("common.lbl.name", "label",
			{disabled:!modifiable,
		allowBlank:false,
		labelWidth:com.trackplus.admin.Filter.nameLabelWidth,
		width:com.trackplus.admin.Filter.nameWidth,
		msgTarget: "qtip"}),
	{	xtype:'hidden',
		name: 'filterSubType',
		value: 2
	},
	Ext.create('Ext.panel.Panel', {
		itemId: 'fieldExpressionsInTree',
		border: false
	})];
};



/**
 * Add map representing the order of the "in tree" field expressions to the submitUrlParams object
 */
com.trackplus.admin.Filter.preSubmitProcess = function(submitUrlParams, fieldExpressionsInTreePanel) {
	if (fieldExpressionsInTreePanel && fieldExpressionsInTreePanel.items.getCount()>1) {
		if (CWHF.isNull(submitUrlParams)) {
			submitUrlParams = new Object();
		}
	    fieldExpressionsInTreePanel.items.each(function(fieldExpressionInTreePanel, index, length) {
	         if (index>0) {
	             submitUrlParams['fieldExpressionOrderMap[' + index + ']']=fieldExpressionInTreePanel.index;
	         }
	    });
	}
	return submitUrlParams;
};

/**
 * Add map representing the order of the "in tree" field expressions to the submitUrlParams object
 */
com.trackplus.admin.Filter.preSubmitProcessNotifyFilter = function(submitUrlParams, panel) {
	var fieldExpressionsInTreePanel = panel.getComponent("fieldExpressionsInTree");
	return com.trackplus.admin.Filter.preSubmitProcess(submitUrlParams, fieldExpressionsInTreePanel);
};

/**
 * Add map representing the order of the "in tree" field expressions to the submitUrlParams object
 */
com.trackplus.admin.Filter.preSubmitProcessIssueFilter = function(submitUrlParams, panel) {
	var moreFieldSet = panel.getComponent("moreFieldSet");
	if (moreFieldSet) {
	    var fieldExpressionsInTreePanel = moreFieldSet.getComponent("fieldExpressionsInTree");
	    return com.trackplus.admin.Filter.preSubmitProcess(submitUrlParams, fieldExpressionsInTreePanel);
	}
	return submitUrlParams;
};

/**
 * The handler for the add "in tree" field expression button
 * fieldExpressionsInTreePanel panel for all "in tree" filterExpressions
 * expressionPanel the add was clicked from
 */
com.trackplus.admin.Filter.addFieldExpression = function(scope, fieldExpressionsInTreePanel, expressionPanel, projectTree, itemTypeList) {
	return Ext.create('Ext.Button', {
		text: getText('common.btn.add'),
		scope: scope,
		handler: function(button) {
			button.setDisabled(true);
			var requestParams = {index: scope.indexMax++,
								issueFilter: scope.issueFilter,
								instant: scope.instant};
			if (expressionPanel) {
				//add the field as request parameter to initialize new new field expression with the same field
				var fieldControl = expressionPanel.getComponent("fieldMap" + expressionPanel.index);
				if (fieldControl) {
					requestParams.fieldID = fieldControl.getValue();
				}
			}
			Ext.Ajax.request({
				url: 'fieldExpression!addField.action',
				params: requestParams,
				scope: scope,
				disableCaching:true,
				success: function(response) {
					var insertPosition = 1;
					var first = false;
					var defaultOperation = null;
					if (expressionPanel) {
						//add from an existing filter expression
						for (var i=1;i<fieldExpressionsInTreePanel.items.getCount();i++) {
							var currentExpressionPanel =  fieldExpressionsInTreePanel.items.getAt(i);
							if (currentExpressionPanel===expressionPanel) {
								insertPosition = i+1;
								//submit some default values for the filter expression to be added
								var operationControl = expressionPanel.getComponent("operationMap" + expressionPanel.index);
								if (operationControl) {
									defaultOperation = operationControl.getValue();
								}
							}
						}
					} else {
						//added from first position (no filter expression)
						first = true;
						nextExpressionPanel = fieldExpressionsInTreePanel.items.getAt(1);
						if (nextExpressionPanel) {
							//at least one filter expression exists already?
							operationControl = nextExpressionPanel.getComponent("operationMap" + nextExpressionPanel.index);
							if (operationControl) {
								//set the previous first expression's operation control as visible
								operationControl.setVisible(true);
								if (fieldExpressionsInTreePanel.items.getCount()>2) {
									nextNextExpressionPanel = fieldExpressionsInTreePanel.items.getAt(2);
									if (nextNextExpressionPanel) {
										var nextNextOperationControl = nextNextExpressionPanel.getComponent(
												"operationMap" + nextNextExpressionPanel.index);
										if (nextNextOperationControl) {
											//initialize the operation with the operation of the next filter expression if exists
											operationControl.setValue(nextNextOperationControl.getValue());
										}
									}
								}
							}
						}
					}
					var fieldExpression = Ext.decode(response.responseText);
					if (defaultOperation) {
						fieldExpression.operation = defaultOperation;
					}
					var addedExpressionPanel = com.trackplus.admin.Filter.createFieldExpressionInTreePanel(scope,
							fieldExpressionsInTreePanel, fieldExpression, true, first, projectTree, itemTypeList);
					fieldExpressionsInTreePanel.insert(insertPosition, addedExpressionPanel);
					button.setDisabled(false);
				},
				failure: function(result){
					button.setDisabled(false);
					Ext.MessageBox.alert(scope.failureTitle, result.responseText);
				},
				method:"POST"
			});
		}
	});
};

/**
 * The handler for the remove expression button
 */
com.trackplus.admin.Filter.deleteFieldExpression = function(scope, fieldExpressionsInTreePanel, expressionPanel) {
	return Ext.create('Ext.Button', {
		text: getText('common.btn.delete'),
		scope: scope,
		handler: function(button) {
			if (expressionPanel) {
				if (expressionPanel===fieldExpressionsInTreePanel.items.getAt(1) && fieldExpressionsInTreePanel.items.getCount()>2) {
					//set the next first expression's operation control as hidden
					nextExpressionPanel = fieldExpressionsInTreePanel.items.getAt(2);
					if (nextExpressionPanel) {
						operationControl = nextExpressionPanel.getComponent("operationMap["+ nextExpressionPanel.index +"]");
						if (operationControl) {
							operationControl.setVisible(false);
						}
					}
				}
				fieldExpressionsInTreePanel.remove(expressionPanel,true);
			}
		}
	});
};

/**
 * Load the combos after the result has arrived containing also the combo data sources
 */
com.trackplus.admin.Filter.postLoadProcessTQLFilter = function(data, panel) {
	var filterTypesCombo = panel.getComponent('filterSubType');
	filterTypesCombo.store.loadData(data['filterTypesList']);
	filterTypesCombo.setValue(data['filterSubType']);

	var styleFieldsCombo = panel.getComponent('styleField');
	styleFieldsCombo.store.loadData(data['styleFieldsList']);
	styleFieldsCombo.setValue(data['styleField']);
};

com.trackplus.admin.Filter.refreshReleaseData = function(checkboxField, newValue, oldValue, options) {
	var selectedProjectIDs = options.projectIDs;
	var hiddenShowClosed = options.hiddenShowClosed;
	hiddenShowClosed.setValue(newValue);
	var releaseTree = options.releaseTree;
	var selectedReleaseIDs = null;
	if (releaseTree) {
		selectedReleaseIDs = releaseTree.getValue();
	}
	var params = {
			withParameter:!this.instant,
			projectIDs: selectedProjectIDs,
			closedFlag: newValue,
			useChecked: true,
	        projectIsSelectable: false
		};
	Ext.Ajax.request({
		url: 'releasePicker.action',
		params: params,
		scope: this,
		disableCaching:true,
		success: function(response){
			var data = Ext.decode(response.responseText);
	        releaseTree.updateMyOptions(data);
			releaseTree.setValue(selectedReleaseIDs);
		},
		failure: function(result){
			Ext.MessageBox.alert(this.failureTitle, result.responseText);
		},
		method:"POST"
	});
};

/**
 * Refresh some upper selects after project change
 */
com.trackplus.admin.Filter.projectOrItemTypeChange = function(treePickerOrList, selectedProjects, oldValue, options){
	var upperSelects = options.panel;
	var withParameter = options.withParameter;
	var paramSetting = options.paramSetting;
	var projectChange = options.projectChange;
	var upperSelectFields = options.upperSelectFields.join();

	var projectIDs = null;
	var projectTree = upperSelects.getComponent('selectId1');
	if (projectTree) {
		var selectedProjects = projectTree.getValue();
		if (selectedProjects) {
			projectIDs = selectedProjects;
		}
	}
	var itemTypeIDs = null;
	var itemTypeList = upperSelects.getComponent('selectId2');
	if (itemTypeList) {
		var selectedItemTypes = itemTypeList.getValue();
		if (selectedItemTypes) {
			itemTypeIDs = selectedItemTypes;
		}
	}
	var releasePanel = upperSelects.getComponent('panel9');
	var showClosedReleases = false;
	if (releasePanel) {
		//refresh the release tree
		var showClosedReleasesCheckBox = releasePanel.getComponent('showClosedReleases');
		if (showClosedReleasesCheckBox) {
			showClosedReleases = showClosedReleasesCheckBox.getValue();
		}
	}
	//refresh the other selects
	var params = {
			upperSelectFields: upperSelectFields,
			instant: !withParameter,
			showClosedReleases: showClosedReleases,
			projectIDs: projectIDs,
			itemTypeIDs: itemTypeIDs,
			projectChange: projectChange
			};
	Ext.Ajax.request({
		url: 'filterUpperRefresh.action',
		params: params,
		scope: this,
		disableCaching:true,
		success: function(response){
			var data = Ext.decode(response.responseText);
			var upperSelects = options.panel;
	        com.trackplus.admin.Filter.reloadUpperSelects(upperSelects, data, paramSetting, false, projectChange);
		},
		failure: function(result){
			Ext.MessageBox.alert(this.failureTitle, result.responseText);
		},
		method:"POST"
	});
};

com.trackplus.admin.Filter.reloadUpperSelects = function(upperSelects, data, paramSetting, resetValue, projectChange) {
	if (data.selectFields) {
	    Ext.Array.forEach(data.selectFields, function(selectFieldData) {
	        var selectControl = null;
	        var fieldID = selectFieldData["fieldID"];
	        //do not set the value for itemType if the itemType change triggered this call
	        //because that would trigger a change event resulting in reloading the dependent fields second time
	        var noItemTypeChange = CWHF.isNull(projectChange) || projectChange || fieldID!==2;
	        //if the reload is caused by itemType change then the item type list control should not be actualized,
	        //because that would trigger a change event resulting in a second reload
	        if (!paramSetting && (fieldID===9 || fieldID===-1001)) {
	            //if no parameter settings an extra panel exists for project/release/watchers
	            var panelOfSelect = upperSelects.getComponent('panel'+fieldID);
	            if (panelOfSelect) {
	                selectControl = panelOfSelect.getComponent("selectId" + fieldID);
	            }
	        } else {
	            selectControl = upperSelects.getComponent("selectId" + fieldID);
	        }
	        if (selectControl) {
	            var selectedValue = selectControl.getValue();
	            var isTree =  selectFieldData["isTree"];
	            if (isTree) {
	                //tree
	                selectControl.updateMyOptions(selectFieldData["dataSource" + fieldID]);
	            } else {
	                //list
	            	if (noItemTypeChange) {
	            		selectControl.store.loadData(selectFieldData["dataSource" + fieldID]);
	            	}
	            }
	            if (resetValue) {
	                selectControl.clearSelection();
	            } else {
	            	if (noItemTypeChange) {
	            		selectControl.setValue(selectedValue);
	            	}
	            }
	        }
	    });
	}
	if (data.missingSelectFields) {
	    Ext.Array.forEach(data.missingSelectFields, function(selectFieldData) {
	        var selectControl = null;
	        var fieldID = selectFieldData["id"];
	        if (!paramSetting && (fieldID===9 || fieldID===-1001)) {
	            //if no parameter settings an extra panel exists for project/release/watchers
	            var panelOfSelect = upperSelects.getComponent('panel'+fieldID);
	            if (panelOfSelect) {
	                selectControl = panelOfSelect.getComponent("selectId" + fieldID);
	            }
	        } else {
	            selectControl = upperSelects.getComponent("selectId" + fieldID);
	        }
	        if (selectControl) {
	            selectControl.removeHandler();
	        }
	    });
	}
	upperSelects.updateLayout();
};


/**
 * Clear the filter
 */
com.trackplus.admin.Filter.clearFilter = function(refreshParameters, result) {
	 var filterSubTypeHiddenField =  refreshParameters.items[0];
	  var panel = filterSubTypeHiddenField.up("panel");
	  com.trackplus.admin.Filter.clearTreeFilter(panel, result.data);
};

/**
 * Clear the filter
 */
com.trackplus.admin.Filter.clearTreeFilter = function(panel, data) {
	var upperSelectsPanel = panel.getComponent("upperSelectsFieldSet");
	com.trackplus.admin.Filter.reloadUpperSelects(upperSelectsPanel, data, false, true);
	var moreFieldSet = panel.getComponent("moreFieldSet");
	if (moreFieldSet) {
	    var keywordTextField = moreFieldSet.getComponent("filterUpperTO.keyword");
	    if (keywordTextField) {
	        keywordTextField.setValue(null);
	    }
	    var linkTypeFilterSupersetCombo = moreFieldSet.getComponent("linkTypeFilterSuperset");
	    if (linkTypeFilterSupersetCombo) {
	        linkTypeFilterSupersetCombo.setValue(null);
	    }
	    var archivedCombo = moreFieldSet.getComponent("archived");
	    if (archivedCombo) {
	        archivedCombo.setValue(0);
	    }
	    var deletedCombo = moreFieldSet.getComponent("deleted");
	    if (deletedCombo) {
	        deletedCombo.setValue(0);
	    }
	    var fieldExpressionsSimple = moreFieldSet.getComponent("fieldExpressionsSimple");
	    if (fieldExpressionsSimple) {
	        fieldExpressionsSimple.items.each(function(fieldExpressionSimple, index, length) {
	            var combo = fieldExpressionSimple.getComponent(0);
	            if (combo) {
	                combo.setValue(-1);
	            }
	            var valueElement =  fieldExpressionSimple.getComponent(1);
	            if (valueElement) {
	                fieldExpressionSimple.remove(valueElement);
	            }
	        });
	    }
	    var fieldExpressionsInTree = moreFieldSet.getComponent("fieldExpressionsInTree");
	    if (fieldExpressionsInTree)  {
	        fieldExpressionsInTree.items.each(function(fieldExpressionInTree, index, length) {
	            if (index>0) {
	                //the first Add button should remain
	                fieldExpressionsInTree.remove(fieldExpressionInTree);
	            }
	        });
	    }
	}
};


/**
 * Add a new select field
 */
com.trackplus.admin.Filter.addNewSelection = function(button, options) {
	var upperSelectsPanel = this.upperSelectsPanel;
	var upperSelectFields = this.upperSelectFields;
	var projectTree = this.projectTree;
	var itemTypeList = this.itemTypeList;
	var instant = this.instant;
	var addSelectionFieldSplitter = this.addSelectionFieldSplitter;
	var fieldID = parseInt(button.getItemId().substr(1)); //remove "f"
	//add as used field to be removed from selectable list fields
	this.upperSelectFields.push(fieldID);
	var selectedProjectIDs = null;
	if (projectTree) {
		selectedProjectIDs = projectTree.getValue();
	}
	var selectedItemTypes = null;
	if (itemTypeList) {
		selectedItemTypes = itemTypeList.getValue();
	}
	Ext.Ajax.request({
		url: 'filterUpperRefresh!addSelectionField.action',
		params: {fieldID:fieldID,
				projectIDs:selectedProjectIDs,
				itemTypeIDs:selectedItemTypes,
				upperSelectFields: this.upperSelectFields.join(),
				instant: this.instant},
		disableCaching:true,
		scope: this,
		success: function(response) {
			var data = Ext.decode(response.responseText);
			var selectFieldData = data["selectField"];
			var missingSelectFields = data["missingSelectFields"];
			if (fieldID===1) {
				selectControl = com.trackplus.admin.Filter.createProjectTree(upperSelectFields,
						upperSelectsPanel, instant, 1, selectFieldData, true, addSelectionFieldSplitter);
				selectControl.addListener('change', com.trackplus.admin.Filter.projectOrItemTypeChange, this,
						{upperSelectFields:upperSelectFields, panel:upperSelectsPanel, withParameter:!this.instant, paramSetting:false, projectChange:true});
			} else {
				if (fieldID===9) {
					//releaseTree
					selectControl = com.trackplus.admin.Filter.createReleaseTree(upperSelectFields,
							upperSelectsPanel, projectTree, instant, selectFieldData, 9, selectedProjectIDs, true, data['showClosedReleasesName'],
							data['showClosedReleases'], data["releaseTypeSelectorIsIncluded"],
							data['releaseTypeSelectorName'], data['releaseTypeSelector'], data['releaseTypeSelectorList'], addSelectionFieldSplitter);
				} else {
					if (fieldID===-1001) {
						//watchers
						selectControl = com.trackplus.admin.Filter.createWatcherList(upperSelectFields,
								upperSelectsPanel, projectTree, instant, selectFieldData, -1001, true,
								data['watcherSelectorName'], data['watcherSelector'], data['watcherSelectorList'], addSelectionFieldSplitter);
					} else {
						//other multiple selects
	                    var isTree =  selectFieldData["isTree"];
	                    if (isTree) {
	                        selectControl = CWHF.createMultipleTreePicker(selectFieldData["label"+fieldID],
	                            selectFieldData["name" + fieldID],
	                            selectFieldData["dataSource" + fieldID], selectFieldData["value" + fieldID],
	                            {itemId:"selectId" + fieldID, labelIsLocalized:true,
	                                //matchFieldWidth:false,
	                                pickerWidth:com.trackplus.admin.Filter.multipleTreePickerWidth,
	                                removeHandler: function() {
	                                    this.setDisabled(true);
	                                    this.ownerCt.remove(this);
	                                    com.trackplus.admin.Filter.updateAfterRemove(upperSelectFields,
	                                        upperSelectsPanel, projectTree, itemTypeList, instant, fieldID, addSelectionFieldSplitter);
	                                }
	                            });
	                    } else {
	                        selectControl = CWHF.createMultipleSelectPicker(selectFieldData["label"+fieldID], selectFieldData["name" + fieldID],
								selectFieldData["dataSource" + fieldID], selectFieldData["value" + fieldID],
								{itemId:"selectId" + fieldID,
								labelIsLocalized:true,
								iconUrlPrefix:selectFieldData["iconUrlPrefix"],
								useIconCls:selectFieldData["useIconCls"],
								removeHandler: function() {
									this.setDisabled(true);
									this.ownerCt.remove(this);
									com.trackplus.admin.Filter.updateAfterRemove(
											upperSelectFields, upperSelectsPanel, projectTree, itemTypeList, instant, fieldID, addSelectionFieldSplitter);
								}
								});
								if (fieldID===2) {
									selectControl.addListener('change', com.trackplus.admin.Filter.projectOrItemTypeChange, this,
										{upperSelectFields:upperSelectFields, panel:upperSelectsPanel, withParameter:!this.instant, paramSetting:false, projectChange:false});
								}
	                    }
				    }
			    }
			}
			//insert at the last but one position
			upperSelectsPanel.insert(upperSelectsPanel.items.getCount()-1, selectControl);
			var menuSelectionFields=com.trackplus.admin.Filter.getMissingSelectionFieldMenues(addSelectionFieldSplitter,
					missingSelectFields, upperSelectsPanel, upperSelectFields, projectTree, itemTypeList, instant);
			addSelectionFieldSplitter.menu.removeAll(true);
	        if (CWHF.isNull(menuSelectionFields) || menuSelectionFields.length===0) {
	            addSelectionFieldSplitter.setVisible(false);
	        } else {
	           addSelectionFieldSplitter.menu.add(menuSelectionFields);
	        }
		},
		failure: function(response) {
			com.trackplus.util.requestFailureHandler(response);
		}
	});
};

com.trackplus.admin.Filter.updateAfterRemove = function(upperSelectFields,
		upperSelectsPanel, projectTree, itemTypeList, instant, fieldID, addSelectionFieldSplitter) {
	var index = upperSelectFields.indexOf(fieldID);
	if (index!==-1) {
		//remove from this.upperSelectFields
		upperSelectFields.splice(index, 1);
	}
	//refresh the combo with list fields available to select after a remove
	Ext.Ajax.request({
		url: 'filterUpperRefresh!removeSelectionField.action',
		params: {upperSelectFields: upperSelectFields},
		disableCaching:true,
		scope: this,
		success: function(response) {
			var data = Ext.decode(response.responseText);
	        if (!addSelectionFieldSplitter.isVisible()) {
	            //after remove the addField should be visible
	            addSelectionFieldSplitter.setVisible(true);
	            upperSelectsPanel.updateLayout();
	        }
			var missingSelectFields = data["missingSelectFields"];
			var menuSelectionFields=com.trackplus.admin.Filter.getMissingSelectionFieldMenues(addSelectionFieldSplitter,
					missingSelectFields, upperSelectsPanel, upperSelectFields, projectTree, itemTypeList, instant);
			addSelectionFieldSplitter.menu.removeAll(true);
			addSelectionFieldSplitter.menu.add(menuSelectionFields);

			if (fieldID===1 || fieldID===2) {
				//project or itemType control was removed: reload the dependences
				var options = {panel:upperSelectsPanel, withParameter:!instant, paramSetting:false, upperSelectFields:upperSelectFields};
				options.projectChange = (fieldID===1);
				com.trackplus.admin.Filter.projectOrItemTypeChange(null, null, null, options);
			}
		},
		failure: function(response) {
			com.trackplus.util.requestFailureHandler(response);
		}
	});
};

com.trackplus.admin.Filter.createProjectTree = function(upperSelectFields, upperSelectsPanel, instant, fieldID, selectFieldData, modifiable, addSelectionFieldSplitter) {
	return CWHF.createMultipleTreePicker(selectFieldData["label"+fieldID],
			selectFieldData["name" + fieldID],
			selectFieldData["dataSource" + fieldID], selectFieldData["value" + fieldID],
			{itemId:"selectId" + fieldID, labelIsLocalized:true,
			disabled:!modifiable,
			//matchFieldWidth:false,
			pickerWidth:com.trackplus.admin.Filter.multipleTreePickerWidth,
			removeHandler: function() {
				//remove from container
				this.setDisabled(true);
				this.ownerCt.remove(this);
				com.trackplus.admin.Filter.updateAfterRemove(upperSelectFields,
						upperSelectsPanel, this, null, instant, fieldID, addSelectionFieldSplitter);
			}
		});
};

com.trackplus.admin.Filter.createReleaseTree = function(upperSelectFields, upperSelectsPanel, projectTree, instant, selectFieldData, fieldID,
		selectedProjectIDs, modifiable, showClosedReleasesName, showClosedReleases,
		releaseTypeSelectorIsIncluded, releaseTypeSelectorName, releaseTypeSelector, releaseTypeSelectorList, addSelectionFieldSplitter) {
	var hiddenShowClosed=CWHF.createHiddenField(showClosedReleasesName, {value:showClosedReleases});
	var checkShowClosed=CWHF.createCheckbox(
			'admin.customize.queryFilter.lbl.showClosedReleases',
			showClosedReleasesName,
			{
				itemId:'showClosedReleases',
				disabled:!modifiable,
				value:showClosedReleases,
				labelAlign:'left',
				labelWidth:com.trackplus.admin.Filter.multiSelectWidth-50,
				width:com.trackplus.admin.Filter.multiSelectWidth,
				border:true,
				cls:'extensionSelect1'
			}
	);
	var extraCmp=null;
	var hiddenReleaseTypeSelectorList = null;
	if (releaseTypeSelectorIsIncluded) {
		hiddenReleaseTypeSelectorList=CWHF.createHiddenField(releaseTypeSelectorName, {value:releaseTypeSelector});
		var releaseTypeRadioButtons = CWHF.getRadioButtonItems(releaseTypeSelectorList,
				releaseTypeSelectorName, 'id', 'label', releaseTypeSelector, !modifiable, true);
		var releaseTypeSelectorList = CWHF.getRadioGroup(null,com.trackplus.admin.Filter.multiSelectWidth, releaseTypeRadioButtons, {itemId:'releaseTypeSelectorList',disabled:!modifiable,border:true,cls:'extensionSelect'},
			{change: {fn: function(cmp){
				var newValue=CWHF.getSelectedRadioButtonValue(cmp);
				this.setValue(newValue);
			}, scope:hiddenReleaseTypeSelectorList}}
		);
		extraCmp=Ext.create('Ext.panel.Panel',{
			items:[checkShowClosed,releaseTypeSelectorList]
		});
	} else {
		extraCmp=checkShowClosed;
	}
	var releaseTree = CWHF.createMultipleTreePicker(selectFieldData["label"+fieldID],
		selectFieldData["name" + fieldID],
		selectFieldData["dataSource" + fieldID], selectFieldData["value" + fieldID],
		{itemId:"selectId" + fieldID, labelIsLocalized:true, disabled:!modifiable,
		extraComponent:extraCmp,
		pickerWidth:com.trackplus.admin.Filter.multipleTreePickerWidth,
		removeHandler: function() {
			this.setDisabled(true);
			this.ownerCt.ownerCt.remove(this.ownerCt);
			com.trackplus.admin.Filter.updateAfterRemove(upperSelectFields,
					upperSelectsPanel, projectTree, null, instant, fieldID, addSelectionFieldSplitter);
			}
		});
	checkShowClosed.addListener('change',com.trackplus.admin.Filter.refreshReleaseData,this,
			{projectIDs: selectedProjectIDs,releaseTree:releaseTree, hiddenShowClosed:hiddenShowClosed});
	return Ext.create('Ext.panel.Panel',{
		border:false,
		height: 22,
		itemId: "panel" + fieldID,
		items:[releaseTree,hiddenShowClosed, hiddenReleaseTypeSelectorList]
	});
};

com.trackplus.admin.Filter.createWatcherList = function(upperSelectFields, upperSelectsPanel, projectTree, instant,
		selectFieldData, fieldID, modifiable, watcherSelectorName, watcherSelector, watcherSelectorList, addSelectionFieldSplitter) {
	var hiddenField=CWHF.createHiddenField(watcherSelectorName, {value:watcherSelector});
	var watcherTypeRadioButtons = CWHF.getRadioButtonItems(watcherSelectorList,
			watcherSelectorName, 'id', 'label', watcherSelector, !modifiable, true);
	var watcherSelectorList = CWHF.getRadioGroup(null,com.trackplus.admin.Filter.multiSelectWidth, watcherTypeRadioButtons,
			{itemId:'watcherSelectorList1',disabled:!modifiable,border:true,cls:'extensionSelect'},
			{change: {fn: function(cmp){
				var newValue=CWHF.getSelectedRadioButtonValue(cmp);
				this.setValue(newValue);
			}, scope:hiddenField}}
	);
	var watchersControl = CWHF.createMultipleSelectPicker(selectFieldData["label"+fieldID], selectFieldData["name" + fieldID],
		selectFieldData["dataSource" + fieldID], selectFieldData["value" + fieldID],
		{itemId:"selectId" + fieldID,
		labelIsLocalized:true,
		useIconCls:selectFieldData["useIconCls"],
		disabled:!modifiable,
		extraComponent:watcherSelectorList,
		removeHandler: function() {
			this.setDisabled(true);
			this.ownerCt.ownerCt.remove(this.ownerCt);
			com.trackplus.admin.Filter.updateAfterRemove(upperSelectFields,
					upperSelectsPanel, projectTree, null, instant, fieldID, addSelectionFieldSplitter);
			}
		});
	return Ext.create('Ext.panel.Panel',{
		border:false,
		height: 22,
		itemId: "panel" + fieldID,
		items:[watchersControl,hiddenField]
	});
};

/**
 * Gets the menu entries for the missing selection fields
 */
com.trackplus.admin.Filter.getMissingSelectionFieldMenues = function(addSelectionFieldSplitter,
		missingSelectFields, upperSelectsPanel, upperSelectFields, projectTree, itemTypeList, instant) {
	var menuSelectionFields=[];
	if (missingSelectFields) {
		for(var i=0;i<missingSelectFields.length;i++){
			var missingSelectField=missingSelectFields[i];
			menuSelectionFields.push({
				text: missingSelectField["text"],
				itemId: "f"+missingSelectField["id"],//itemId should be string
				iconCls:missingSelectField["iconCls"],
				handler: com.trackplus.admin.Filter.addNewSelection,
				scope: {addSelectionFieldSplitter:addSelectionFieldSplitter,
						upperSelectsPanel:upperSelectsPanel,
						upperSelectFields:upperSelectFields,
						projectTree:projectTree,
						itemTypeList: itemTypeList,
						instant:instant}
			});
		}
	}
	return menuSelectionFields;
};

/**
 * Post process the data after the result has arrived containing the combo data sources and field expression configs.
 */
com.trackplus.admin.Filter.postLoadProcessTreeFilter = function(data, panel) {
	var modifiable = data['modifiable'];
	var selectColumnCount = data.selectColumnCount;
	if (CWHF.isNull(selectColumnCount)) {
		selectColumnCount = com.trackplus.admin.Filter.upperSelectColumns;
	}
	var instant = this.instant;
	if (CWHF.isNull(instant)) {
		instant = false;
	}
	var renderPath = this.renderPath;
	if (CWHF.isNull(renderPath)) {
		renderPath = false;
	}
	var generalFilterFields = panel.getComponent("generalFilterFields");
	if (generalFilterFields) {
		generalFilterFields.add(CWHF.createCombo(
			"admin.customize.queryFilter.lbl.styleField", "styleField",
			{labelWidth:com.trackplus.admin.Filter.styleFieldLabelWidth,
			width: com.trackplus.admin.Filter.styleFieldWidth,
			data:data["styleFieldsList"],
			value: data["styleField"]}));
		generalFilterFields.add(CWHF.createCombo(
				"admin.customize.queryFilter.lbl.view",
				"viewID",
				{
				labelWidth:com.trackplus.admin.Filter.viewLabelWidth,
				width: com.trackplus.admin.Filter.viewFieldWidth,
				 disabled:!modifiable,
				idType: "string",
				data:data["viewList"],
				value: data["viewID"]}));
		if (renderPath) {
			generalFilterFields.add(CWHF.createSingleTreePicker("common.lbl.path", "node", data["pathNodes"], null,
					{labelWidth:com.trackplus.admin.Filter.nameLabelWidth,
					width: com.trackplus.admin.Filter.nameWidth,
					allowBlank:false}));
		}
	}
	//upper selects
	this.upperSelectFields = []; //gather select fields to be used by project change
	var upperSelectsPanel = Ext.create("Ext.form.Panel", {
		//xtype: "fieldset",
		border: false,
		itemId: "upperSelectsFieldSet",
		padding:'5 5 10 10',
		layout: {
			type:'table',
			columns: selectColumnCount,
			tdAttrs:{
				style:{
					'vertical-align':'top',
					paddingBottom:'5px'
				}
			}
		},
		items:[]
	});
	panel.add(upperSelectsPanel);
	//add new select field combo
	var missingSelectFields = data["missingSelectFields"];
	var addSelectionFieldSplitter = Ext.create("Ext.button.Split", {
		text: getText('admin.customize.queryFilter.opt.addSelectField'),
		overflowText:getText('admin.customize.queryFilter.opt.addSelectField'),
		tooltip:getText('admin.customize.queryFilter.opt.addSelectField.tt'),
		iconCls: 'addCriteria',
		menu: {items:[]}
		});
	var projectTree = null;
	var itemTypeList = null;
	if (upperSelectsPanel) {
		var upperSelectControls = [];
		var selectedProjectIDs = null;
		Ext.Array.forEach(data.selectFields, function(selectFieldData, index) {
			var fieldID = selectFieldData["fieldID"];
			var selectControl;
			this.upperSelectFields.push(fieldID);
			if (fieldID===1) {
				selectedProjectIDs = selectFieldData["value" + fieldID];
				projectTree = com.trackplus.admin.Filter.createProjectTree(this.upperSelectFields,
						upperSelectsPanel, instant, fieldID, selectFieldData, modifiable, addSelectionFieldSplitter);
				projectTree.addListener('change', com.trackplus.admin.Filter.projectOrItemTypeChange, this,
						{upperSelectFields:this.upperSelectFields, panel:upperSelectsPanel, withParameter:!instant, paramSetting:false, projectChange:true});
				selectControl = projectTree;
			} else {
				//gather the selects reloaded by project change
				if (fieldID===9) {
					//releaseTree
					selectControl = com.trackplus.admin.Filter.createReleaseTree(this.upperSelectFields,
							upperSelectsPanel, projectTree, instant,
							selectFieldData, fieldID, selectedProjectIDs, modifiable, data['showClosedReleasesName'],
							data['showClosedReleases'], data["releaseTypeSelectorIsIncluded"],
							data['releaseTypeSelectorName'], data['releaseTypeSelector'], data['releaseTypeSelectorList'], addSelectionFieldSplitter);
				} else {
					if (fieldID===-1001) {
						//watchers
						selectControl = com.trackplus.admin.Filter.createWatcherList(this.upperSelectFields,
								upperSelectsPanel, projectTree, instant, selectFieldData, fieldID, modifiable,
								data['watcherSelectorName'], data['watcherSelector'], data['watcherSelectorList'], addSelectionFieldSplitter);
					} else {
						//other selects
						//with this can't be used in removeHandler because the scope (this) is the picker
						var upperSelectFields = this.upperSelectFields;
	                    var isTree = selectFieldData["isTree"];
	                    if (isTree) {
	                        selectControl = CWHF.createMultipleTreePicker(selectFieldData["label"+fieldID],
	                            selectFieldData["name" + fieldID],
	                            selectFieldData["dataSource" + fieldID], selectFieldData["value" + fieldID],
	                            {itemId:"selectId" + fieldID,
	                            disabled:!modifiable,
	                            labelIsLocalized:true,
	                            //matchFieldWidth:false,
	                            pickerWidth:com.trackplus.admin.Filter.multipleTreePickerWidth,
	                            removeHandler: function() {
	                                this.setDisabled(true);
	                                this.ownerCt.remove(this);
	                                com.trackplus.admin.Filter.updateAfterRemove(upperSelectFields,
	                                    upperSelectsPanel, projectTree, itemTypeList, instant, fieldID, addSelectionFieldSplitter);
	                            }
	                            });
	                    } else {
	                        selectControl = CWHF.createMultipleSelectPicker(selectFieldData["label"+fieldID], selectFieldData["name" + fieldID],
	                            selectFieldData["dataSource" + fieldID], selectFieldData["value" + fieldID],
	                            {itemId:"selectId" + fieldID,
	                            disabled:!modifiable,
	                            labelIsLocalized:true,
	                            useIconCls:selectFieldData["useIconCls"],
								iconUrlPrefix:selectFieldData["iconUrlPrefix"],
	                            removeHandler: function() {
	                                this.setDisabled(true);
	                                this.ownerCt.remove(this);
	                                com.trackplus.admin.Filter.updateAfterRemove(upperSelectFields, upperSelectsPanel,
	                                        projectTree, itemTypeList, instant, fieldID, addSelectionFieldSplitter);
	                                }
	                            });
	                        if (fieldID===2) {
	                        	itemTypeList = selectControl;
	                        	itemTypeList.addListener('change', com.trackplus.admin.Filter.projectOrItemTypeChange, this,
	            						{upperSelectFields:this.upperSelectFields, panel:upperSelectsPanel, withParameter:!instant, paramSetting:false, projectChange:false});
	                        }
	                    }
					}
				}
			}
			upperSelectControls.push(selectControl);
		}, this);
		if (modifiable) {
			if (CWHF.isNull(missingSelectFields)) {
	            addSelectionFieldSplitter.setVisible(false);
			}
	        upperSelectControls.push(addSelectionFieldSplitter);
	    	var menuSelectionFields=com.trackplus.admin.Filter.getMissingSelectionFieldMenues(addSelectionFieldSplitter,
					missingSelectFields, upperSelectsPanel, this.upperSelectFields, projectTree, itemTypeList, instant);
			addSelectionFieldSplitter.menu.add(menuSelectionFields);
		}

		//add all selects at the same time
		upperSelectsPanel.add(upperSelectControls);
		upperSelectsPanel.updateLayout();
	}
	//add simple field expressions other than simple lists set in field configuration as appear in filter
	var treeExpressionsExist = (data["fieldsExpressionsInTree"]);
	var moreCriteriaExpanded = (data["keyword"] && data["keyword"]!=="") ||  (data['linkTypeFilterSuperset'] && data['linkTypeFilterSuperset']!=="") ||
	    (data["archived"] && data["archived"]!==0) ||
	    (data["deleted"] && data["deleted"]!==0) || data["hasSimpleFieldExpressions"] || treeExpressionsExist;
	var moreFieldSet = Ext.create("Ext.form.FieldSet", {xtype: 'fieldset',
	    itemId: "moreFieldSet",
	    border: "1 0 0 0",
	    title: getText('admin.customize.queryFilter.fieldset.moreFields'),
	    collapsible: true,
	    collapsed: !moreCriteriaExpanded,
	    layout: 'anchor',
	    items: []});
	panel.add(moreFieldSet);
	moreFieldSet.add(Ext.create('Ext.panel.Panel', {
	    itemId: "fieldExpressionsSimple",
	    padding: "0 0 5 0",
	    border:false
	}));
	var fieldsExpressionsSimple = data['fieldsExpressionsSimple'];
	if (fieldsExpressionsSimple) {
		com.trackplus.admin.Filter.populateFieldExpressionsSimplePanel(this, moreFieldSet.getComponent("fieldExpressionsSimple"),
				fieldsExpressionsSimple, modifiable, true, {projectTree:projectTree, itemTypeList:itemTypeList});
	}
	var upperComboControls = [];
	if (data["keywordIsIncluded"]) {
	    upperComboControls.push(CWHF.createTextField(
	        'admin.customize.queryFilter.lbl.keyword', data['keywordName'],
	        {disabled:!modifiable,
	            labelWidth:com.trackplus.admin.Filter.moreControlLabelWidth,
	            width:com.trackplus.admin.Filter.moreControlWidth,
	            value:data['keyword']}
	    ));
	}
	upperComboControls.push(CWHF.createCombo(
	    'admin.customize.queryFilter.lbl.linkTypeFilterSuperset',
	    data['linkTypeFilterSupersetName'],
	    {	itemId: "linkTypeFilterSuperset",
	    	disabled:!modifiable,
	        labelWidth:com.trackplus.admin.Filter.moreControlLabelWidth,
	        width:com.trackplus.admin.Filter.moreControlWidth,
	        data:data["linkTypeFilterSupersetList"],
	        value:data["linkTypeFilterSuperset"],
	        idType:'string'}));
	if (data["archivedDeletedIsIncluded"]) {
	    upperComboControls.push(
	        CWHF.createCombo('admin.customize.queryFilter.lbl.archived',
	            data['archivedName'],
	            {	itemId: "archived",
	        		disabled:!modifiable,
	                labelWidth:com.trackplus.admin.Filter.moreControlLabelWidth,
	                width:com.trackplus.admin.Filter.moreControlWidth,
	                data:data["archivedList"],
	                value:data["archived"]}));
	    upperComboControls.push(CWHF.createCombo('admin.customize.queryFilter.lbl.deleted',
	        data['deletedName'],
	        {	itemId: "deleted",
	    		disabled:!modifiable,
	            labelWidth:com.trackplus.admin.Filter.moreControlLabelWidth,
	            width:com.trackplus.admin.Filter.moreControlWidth,
	            data:data["deletedList"],
	            value:data["deleted"]}));
	}
	moreFieldSet.add(upperComboControls);
	if (modifiable || treeExpressionsExist) {
	    //add this fieldset only if has tree expressions or is modifiable
	   	var fieldExpressionsInTree = Ext.create("Ext.panel.Panel", {
	           itemId: "fieldExpressionsInTree",
	           border:false/*,
	           layout: {
	               type: 'table',
	               // The total column count must be specified here
	               columns: 8
	           } */
	        });
	        moreFieldSet.add(fieldExpressionsInTree);
	    com.trackplus.admin.Filter.populateFieldExpressionsInTreePanel(this, fieldExpressionsInTree,
	            data["fieldsExpressionsInTree"], modifiable, projectTree, itemTypeList);
	}
};



/**
 * Post process the data after filter parameter data has arrived containing the combo data sources and field expression configs .
 */
com.trackplus.admin.Filter.postLoadProcessTreeFilterParameters = function(data, panel) {
	this.upperSelectFields = []; //gather select fields to be used by project change
	var projectTree = null;
	var itemTypeList = null;
	var originalProjects = data["originalProjects"];
	var originalItemTypes = data["originalItemTypes"];
	//upper selects
	if (data.selectFields) {
		var upperSelectsPanel = Ext.create('Ext.panel.Panel', {
			itemId: "upperSelectsPanel",
			layout: {
				type:'table',
				columns:4,
				tdAttrs:{
					style:{
						'vertical-align':'top',
						paddingBottom:'5px'
					}
				}
			},
			border:false,
			items:[]
		});
		panel.add(upperSelectsPanel);
		var upperSelectControls = [];
		Ext.Array.forEach(data.selectFields, function(selectFieldData) {
			var fieldID = selectFieldData["fieldID"];
			var title = selectFieldData["label"+fieldID];
			if (fieldID===1) {
				selectControl = CWHF.createMultipleTreePicker(title,
						selectFieldData["name" + fieldID],
						selectFieldData["dataSource" + fieldID], originalProjects,
						{itemId:"selectId" + fieldID, labelIsLocalized:true, useRemoveBtn:false});
				projectTree = selectControl;
				selectControl.addListener('change', com.trackplus.admin.Filter.projectOrItemTypeChange, this,
						{upperSelectFields:this.upperSelectFields, panel:upperSelectsPanel, withParameter:false, paramSetting:true, projectChange:true});
			} else {
				//gather the selects reloaded by project change
				this.upperSelectFields.push(fieldID);
				if (fieldID===9) {
					//release
					selectControl = CWHF.createMultipleTreePicker(title,
							selectFieldData["name" + fieldID],
							selectFieldData["dataSource" + fieldID], selectFieldData["value" + fieldID],
							{itemId:"selectId" + fieldID, labelIsLocalized:true, useRemoveBtn:false});
				} else {
	                var isTree = selectFieldData["isTree"];
	                if (isTree) {
	                    selectControl = CWHF.createMultipleTreePicker(title,
	                        selectFieldData["name" + fieldID],
	                        selectFieldData["dataSource" + fieldID], selectFieldData["value" + fieldID],
	                        {itemId:"selectId" + fieldID,
	                         labelIsLocalized:true,
	                         //matchFieldWidth:false,
	                         pickerWidth:com.trackplus.admin.Filter.multipleTreePickerWidth,
	                         useRemoveBtn:false
	                        });
	                }  else {
	                    selectControl = CWHF.createMultipleSelectPicker(title, selectFieldData["name" + fieldID],
	                            selectFieldData["dataSource" + fieldID], selectFieldData["value" + fieldID],
	                            {itemId:"selectId" + fieldID,
	                    		labelIsLocalized:true,
	                    		useIconCls:selectFieldData["useIconCls"],
	                    		iconUrlPrefix:selectFieldData["iconUrlPrefix"],
	                    		useRemoveBtn:false});
	                    if (fieldID===2) {
	                    	itemTypeList = selectControl;
	                    	selectControl.addListener('change', com.trackplus.admin.Filter.projectOrItemTypeChange, this,
	    						{upperSelectFields:this.upperSelectFields, panel:upperSelectsPanel, withParameter:false, paramSetting:true, projectChange:false});
	                    }
	                }
				}
			}
			upperSelectControls.push(selectControl);
		}, this);
		if (upperSelectControls.length>0) {
			upperSelectsPanel.add(upperSelectControls);
		}
	}
	//simple field expressions
	var selectedProjects = null;
	if (CWHF.isNull(projectTree)) {
		//project is not parameterized, the selected projects will not change in the replace parameters form
		selectedProjects = originalProjects;
	} else {
		//if the project is parameterized it might change during in the replace parameters form
		//and the changed value should be available in the simple filter expressions' listener
		//that's why the projectTree is sent as parameter
	}
	var selectedItemTypes = null;
	if (CWHF.isNull(itemTypeList)) {
		//itemType is not parameterized
		selectedItemTypes = originalItemTypes;
	}
	var fieldsExpressionsSimple = data["fieldsExpressionsSimple"];
	if (fieldsExpressionsSimple) {
		var moreFieldSet = Ext.create("Ext.panel.Panel", {
			itemId: "fieldExpressionsSimple",
			border:false,
			items: []});
		panel.add(moreFieldSet);
		com.trackplus.admin.Filter.populateFieldExpressionsSimplePanel(this, moreFieldSet,
			fieldsExpressionsSimple, true, true,
			{projectTree:projectTree, itemTypeList:itemTypeList, selectedProjects:selectedProjects, selectedItemTypes:selectedItemTypes});
	}
	//"in tree" field expressions
	var fieldExpressionsInTree = data["fieldsExpressionsInTree"];
	var fieldExpressionsInTreePanel = null;
	if (fieldExpressionsInTree) {
		fieldExpressionsInTreePanel = Ext.create('Ext.panel.Panel', {
			itemId: "fieldExpressionsInTree",
			border:false,
			items:[]
		});
		panel.add(fieldExpressionsInTreePanel);
	}
	com.trackplus.admin.Filter.populateFieldExpressionsSimplePanel(this, fieldExpressionsInTreePanel, fieldExpressionsInTree, true, false,
			{projectTree:projectTree, itemTypeList:itemTypeList, selectedProjects:selectedProjects, selectedItemTypes:selectedItemTypes});
};

/**
 * Load the notify filter expressions
 */
com.trackplus.admin.Filter.postLoadProcessNotifyFilter = function(data, panel) {
	com.trackplus.admin.Filter.populateFieldExpressionsInTreePanel(this, panel.getComponent("fieldExpressionsInTree"), data['fieldsExpressionsInTree'], data['modifiable']);
};

/**
 * Add the simple field expressions after the result has arrived
 */
com.trackplus.admin.Filter.populateFieldExpressionsSimplePanel =
	function (scope, fieldExpressionsSimplePanel, fieldsExpressionsSimple, modifiable, indexIsField, contextFields) {
		if (fieldExpressionsSimplePanel ) {
			if (fieldsExpressionsSimple ) {
				var fieldExpressionSimplePanels = [];
				Ext.Array.forEach(fieldsExpressionsSimple, function (fieldExpression) {
					var expressionPanel = com.trackplus.admin.Filter.createFieldExpressionSimplePanel(scope,
						fieldExpression, modifiable, indexIsField, contextFields);
					fieldExpressionSimplePanels.push(expressionPanel);
				}, scope);
				//add all panels at once
				fieldExpressionsSimplePanel.add(fieldExpressionSimplePanels);
			}
		}
	};

/**
 * Create a simple filter expression
 */
com.trackplus.admin.Filter.createFieldExpressionSimplePanel =
	function (scope, fieldExpression, modifiable, indexIsField, contextFields) {
		var expressionPanel = Ext.create("Ext.panel.Panel", {
			border:false,
			defaults:{margin:"4 4 0 0"},
			layout:{
				type:'hbox'
			}
		});
		var matcherCombo = CWHF.createCombo(fieldExpression.fieldLabel,
			fieldExpression.matcherName,
			{	itemId: fieldExpression.matcherItemId,
				disabled:!modifiable,
				width:com.trackplus.admin.Filter.matcherComboWidth + 200,
				labelWidth:com.trackplus.admin.Filter.moreControlLabelWidth,
				data:fieldExpression.matcherList,
				value:fieldExpression.matcher,
				labelIsLocalized:true},
			{select:{fn:com.trackplus.admin.Filter.selectMatcher, scope:scope,
				panel:expressionPanel, fieldExpression:fieldExpression,
				contextFields:contextFields}});
		expressionPanel.add(matcherCombo);
		com.trackplus.admin.Filter.addValuePart(scope, fieldExpression.valueRenderer,
				fieldExpression.jsonConfig, fieldExpression, expressionPanel, matcherCombo,
				null, contextFields);
	    if (!indexIsField) {
	        expressionPanel.add(CWHF.createHiddenField("fieldMap"+ fieldExpression.index, {value:fieldExpression.field}));
	    }
		return expressionPanel;
	};

/**
 * Add the "in tree" filter expressions after the result has arrived
 */
com.trackplus.admin.Filter.populateFieldExpressionsInTreePanel = function(scope, fieldExpressionsInTreePanel, fieldsExpressionsInTree, modifiable, projectTree, itemTypeList) {
	if (fieldExpressionsInTreePanel) {
		if (modifiable) {
			fieldExpressionsInTreePanel.add(com.trackplus.admin.Filter.addFieldExpression(scope, fieldExpressionsInTreePanel, null, projectTree, itemTypeList));
		}
		if (fieldsExpressionsInTree) {
			var fieldExpressionInTreePanels = [];
			scope.indexMax = 0;
			Ext.Array.forEach(fieldsExpressionsInTree, function(fieldExpression) {
				var expressionPanel = com.trackplus.admin.Filter.createFieldExpressionInTreePanel(scope,
						fieldExpressionsInTreePanel, fieldExpression, modifiable, scope.indexMax===0, projectTree, itemTypeList);
				fieldExpressionInTreePanels.push(expressionPanel);
				scope.indexMax++;
			}, scope);
			//add all panels at once
			fieldExpressionsInTreePanel.add(fieldExpressionInTreePanels);
		}
	}
};

/**
 * Create an "in tree" filter expression
 */
com.trackplus.admin.Filter.createFieldExpressionInTreePanel = function(scope, fieldExpressionsInTreePanel, fieldExpression, modifiable, first, projectTree, itemTypeList) {
	var expressionPanel = Ext.create("Ext.panel.Panel", {
		border:false,
		itemId: "filterExpressionInTreePanel"+fieldExpression.index,
		defaults: {margin:"4 4 0 0"},
		layout: {
			type:'hbox'
		}
	});
	expressionPanel.index = fieldExpression.index;
	expressionPanel.add(CWHF.createCombo(null,
			fieldExpression.operationsName,
			{itemId: fieldExpression.operationItemId,
			disabled:!modifiable,
			width:com.trackplus.admin.Filter.operationComboWidth,
			data:fieldExpression.operationsList,
			value:fieldExpression.operation,
			hidden:first}));
	expressionPanel.add(CWHF.createCombo(null,
			fieldExpression.parenthesisOpenName,
			{disabled:!modifiable, includeEmpty:true,
				width:com.trackplus.admin.Filter.parenthesisComboWidth,
				data:fieldExpression.parenthesisOpenList,
				value:fieldExpression.parenthesisOpen
			}));
	if (fieldExpression.withFieldMoment) {
		expressionPanel.add(CWHF.createCombo(null,
			fieldExpression.fieldMomentName,
			{disabled:!modifiable,
				width:com.trackplus.admin.Filter.fieldMomentComboWidth,
				data:fieldExpression.fieldMomentList,
				value:fieldExpression.fieldMoment
			}));
	}
	var contextFields = {projectTree:projectTree, itemTypeList:itemTypeList};
	expressionPanel.add(CWHF.createCombo(null,
			fieldExpression.fieldName,
			{	itemId: fieldExpression.fieldItemId,
				disabled:!modifiable,
				width:com.trackplus.admin.Filter.fieldNameComboWidth,
				data:fieldExpression.fieldList,
				value:fieldExpression.field
			},
			{select: {fn: com.trackplus.admin.Filter.selectField, scope:scope,
				panel:expressionPanel, fieldExpression:fieldExpression, contextFields:contextFields}}
			));
	var matcherCombo = CWHF.createCombo(null,
			fieldExpression.matcherName,
			{	itemId: fieldExpression.matcherItemId,
				disabled:!modifiable,
				width:com.trackplus.admin.Filter.matcherComboWidth,
				data:fieldExpression.matcherList,
				value:fieldExpression.matcher
			},
			{select: {fn: com.trackplus.admin.Filter.selectMatcher, scope:scope, panel:expressionPanel,
				fieldExpression:fieldExpression, contextFields:contextFields}});
	expressionPanel.add(matcherCombo);
	com.trackplus.admin.Filter.addValuePart(scope, fieldExpression.valueRenderer, fieldExpression.jsonConfig,
			fieldExpression, expressionPanel, matcherCombo, null, contextFields);
	expressionPanel.add(CWHF.createCombo(null,
		fieldExpression.parenthesisClosedName,
		{disabled:!modifiable, includeEmpty:true,
			width:com.trackplus.admin.Filter.parenthesisComboWidth,
			data:fieldExpression.parenthesisClosedList,
			value:fieldExpression.parenthesisClosed
		}));
	if (modifiable) {
		expressionPanel.add(com.trackplus.admin.Filter.addFieldExpression(scope, fieldExpressionsInTreePanel, expressionPanel, projectTree, itemTypeList));
		expressionPanel.add(com.trackplus.admin.Filter.deleteFieldExpression(scope, fieldExpressionsInTreePanel, expressionPanel));
	}
	return expressionPanel;
};

/**
 * Reload the value part of a simple or "in tree" filter expression after changing the matcher
 * or after changing any or the composite select part: in this latter case combo is not the matcher combo
 * but a composite select combo and the matcher combo is in options.matcherCombo
 */
com.trackplus.admin.Filter.selectMatcher = function(combo, records, options) {
	var fieldID = null;
	var fieldComponentItemId = options.fieldExpression.fieldItemId;
	if (fieldComponentItemId) {
		//"in tree" field expression: fieldID changed after field select
		var fieldComponent = options.panel.getComponent(fieldComponentItemId);
		if (fieldComponent) {
			fieldID = fieldComponent.getValue();
		}
	} else {
		//"simple" field expression: fieldID remains the same (no field select)
		fieldID = options.fieldExpression.field;
	}
	var valueComponentItemId = options.fieldExpression.valueItemId;
	var valueComponent = options.panel.getComponent(valueComponentItemId);
	var value = null;
	if (valueComponent) {
		value = valueComponent.getStringValue();
	}
	var matcherCombo = options.matcherCombo;
	var matcherID;
	var comboToInsertAfter;
	if (matcherCombo) {
		//combo is a composite select, the matcherCombo is got from options
		matcherID = matcherCombo.getValue();
		comboToInsertAfter = matcherCombo;
	} else {
		//combo is the matcherCombo
		matcherID = combo.getValue();
		comboToInsertAfter = combo;
	}
	var index = options.fieldExpression.index;
	var params = {
			index:index,//index is null for "simple" field expressions
			fieldID: fieldID,
			matcherID: matcherID,
			stringValue: value,//to maintain the value if possible
			issueFilter: this.issueFilter,
			instant:  this.instant
		};
	if (index) {
		params.inTree=true;
	}
	var contextFields = options.contextFields;
	var projectTree = null;
	var selectedProjects = null;
	var itemTypeList = null;
	var selectedItemTypes = null;
	if (contextFields) {
		projectTree = contextFields.projectTree;
		selectedProjects = contextFields.selectedProjects;
		itemTypeList = contextFields.itemTypeList;
		selectedItemTypes = contextFields.selectedItemTypes;
	}
	if (projectTree) {
		params.projectIDs = projectTree.getValue();
	} else {
		if (selectedProjects) {
			params.projectIDs = selectedProjects;
		}
	}
	if (itemTypeList) {
		params.itemTypeIDs = itemTypeList.getValue();
	} else {
		if (selectedItemTypes) {
			params.itemTypeIDs = selectedItemTypes;
		}
	}
	Ext.Ajax.request({
		url: 'fieldExpression!selectMatcher.action',
		params: params,
		scope: this,
		disableCaching:true,
		success: function(response){
			var responseJson = Ext.decode(response.responseText);
			if (valueComponent) {
				options.panel.remove(valueComponent);
			}
			if (responseJson.needMatcherValue && responseJson.valueRenderer) {
				var valueInsertPosition = 5;//a good default
				for (var i=0;i<options.panel.items.getCount();i++) {
					var fieldExpressionComponent = options.panel.items.getAt(i);
					if (comboToInsertAfter===fieldExpressionComponent) {
						valueInsertPosition = i+1;//insert seems to be 1 based
					}
				}
				options.fieldExpression.needMatcherValue = responseJson.needMatcherValue;
				options.fieldExpression.valueRenderer = responseJson.valueRenderer;
				com.trackplus.admin.Filter.addValuePart(this, responseJson.valueRenderer, responseJson.jsonConfig, options.fieldExpression,
						options.panel, comboToInsertAfter, valueInsertPosition, contextFields);
			}
		},
		failure: function(result){
			Ext.MessageBox.alert(this.failureTitle, result.responseText);
		},
		method:"POST"
	});
};

/**
 * Reload the matcher and value part of an "in tree" filter expression after changing the field
 */
com.trackplus.admin.Filter.selectField = function(combo, records, options) {
	var matcherComponentName = options.fieldExpression.matcherItemId;
	var matcherID = null;
	var matcherComponent = null;
	if (matcherComponentName) {
		matcherComponent = options.panel.getComponent(matcherComponentName);
		if (matcherComponent) {
			matcherID = matcherComponent.getValue();
		}
	}
	var valueComponentItemId = options.fieldExpression.valueItemId;
	var valueComponent = null;
	if (valueComponentItemId) {
		valueComponent = options.panel.getComponent(valueComponentItemId);
	}
	var index = options.fieldExpression.index;
	params = {
		index: index,
		fieldID: combo.getValue(),
		matcherID: matcherID,
		//stringValue: value,//do not maintain the value because a select controls option value may be transformed to integer control value
		issueFilter: this.issueFilter,
		instant:  this.instant
	};
	var contextFields = options.contextFields;
	if (contextFields) {
		var projectTree = contextFields.projectTree;
		if (projectTree) {
			params.projectIDs = projectTree.getValue();
		}
		var itemTypeList = contextFields.itemTypeList;
		if (itemTypeList) {
			params.itemTypeIDs = itemTypeList.getValue();
		}
	}
	Ext.Ajax.request({
		url: 'fieldExpression!selectField.action',
		params: params,
		scope: this,
		disableCaching:true,
		success: function(response){
			var responseJson = Ext.decode(response.responseText);
			if (matcherComponent) {
				matcherComponent.store.loadData(responseJson.matcherList);
				//actualize also the fieldExpression's matcherList used by ADD button
				//options.fieldExpression.matcherList = responseJson.matcherList;
				matcherComponent.setValue(responseJson.matcher);
			}
			if (valueComponent) {
				options.panel.remove(valueComponent);
			}
			if (responseJson.needMatcherValue && responseJson.valueRenderer) {
				var valueInsertPosition = 5;
				for (var i=0;i<options.panel.items.getCount();i++) {
					var fieldExpressionComponent = options.panel.items.getAt(i);
					if (matcherComponent===fieldExpressionComponent) {
						valueInsertPosition = i+1;//insert seems to be 1 based
					}
				}
				options.fieldExpression.needMatcherValue = responseJson.needMatcherValue;
				options.fieldExpression.valueRenderer = responseJson.valueRenderer;
				com.trackplus.admin.Filter.addValuePart(this, responseJson.valueRenderer, responseJson.jsonConfig,
						options.fieldExpression, options.panel, matcherComponent, valueInsertPosition, contextFields);
			}
		},
		failure: function(result){
			Ext.MessageBox.alert(this.failureTitle, result.responseText);
		},
		method:"POST"
	});
};

/**
 * Configures and adds the value part (the dynamic part) to the filter expressions by first rendering of
 * simple or "in tree" field expressions and after changing the matcher or the field
 */
com.trackplus.admin.Filter.addValuePart = function(scope, valueRenderer, jsonConfig, fieldExpression,
		expressionPanel, matcherCombo, insertPosition, contextFields) {
	if (fieldExpression.needMatcherValue && fieldExpression.valueRenderer && fieldExpression.valueRenderer!=="") {
		if (jsonConfig) {
			//the jsonConfig coming from the server for each fieldExpression or after a matcher or field change
			var valueComponent=Ext.create(valueRenderer, {jsonData:jsonConfig, itemId:fieldExpression.valueItemId});
			if (valueComponent) {
				if (valueComponent.addListeners) {
					valueComponent.scope = scope;
					valueComponent.selectHandler=com.trackplus.admin.Filter.selectMatcher;
					valueComponent.fieldExpression = fieldExpression;
					valueComponent.expressionPanel = expressionPanel;
					valueComponent.matcherCombo = matcherCombo;
					valueComponent.contextFields = contextFields;
					valueComponent.addListeners();
				}
				if (CWHF.isNull(insertPosition)) {
					expressionPanel.add(valueComponent);
				} else {
					expressionPanel.insert(insertPosition, valueComponent);
				}
			}
		}
	}
};

/**
 * Set the values on replace window when the data is available
 */
com.trackplus.admin.Filter.replacePostDataProcess =	function(data, panel) {
	var replaceLabel =  panel.getComponent('replacementWarning');
	replaceLabel.setText(data.replacementWarning, false);
	var replacementID = panel.getComponent('replacementID');
	//tree picker instead of plain list
	//replacementList.store.loadData(data.replacementList);
	//replacementList.setValue(null);
	replacementID.labelEl.dom.innerHTML = data.replacementListLabel;
};

/**
 * Whether the filter contains parameter(s)
 */
com.trackplus.admin.Filter.executeFilter = function(scope, filterID, ajax) {
	Ext.Ajax.request({
		url: 'filterParameters!containsParameter.action',
		params: {
			filterID: filterID
		},
		scope: scope,
		disableCaching:true,
		success: function(response){
			var data = Ext.decode(response.responseText);
			var containsParameter = data.success;
			if (containsParameter) {
				com.trackplus.admin.Filter.renderFilterParameter(scope, filterID,ajax);
			} else {
				com.trackplus.admin.Filter.executeParameterlessFilter(filterID,ajax);
			}
		},
		failure: function(result){
			Ext.MessageBox.alert(scope.failureTitle, result.responseText);
		},
		method:"POST"
	});
};

/**
 * Render the filter parameters
 */
com.trackplus.admin.Filter.renderFilterParameter = function(scope, filterID,ajax) {
	var title = getText('admin.customize.queryFilter.lbl.parameters');
	var width = 1000;
	var height = 800;
	var windowItems = com.trackplus.admin.Filter.getTreeFilterParameterItems();
	var loadUrl = 'filterParameters!renderParameters.action';
	var loadUrlParams = {filterID:filterID};
	var submitUrl = 'savedFilterExecute!replaceSubmittedParameters.action';
	var submitUrlParams = {
		filterID:filterID,
		ajax:ajax
	};
	var windowConfig = Ext.create('com.trackplus.util.WindowConfig',
			{postDataProcess:com.trackplus.admin.Filter.postLoadProcessTreeFilterParameters});
	windowConfig.showWindow(scope, title, width, height,
			{loadUrl:loadUrl, loadUrlParams:loadUrlParams},
			{submitUrl:submitUrl,
			submitUrlParams:submitUrlParams,
			submitButtonText:getText('common.btn.applyFilter'),
			standardSubmit:CWHF.isNull(ajax)||ajax===false},
			windowItems);
};

/**
 * Execute the parameterless filter
 */
com.trackplus.admin.Filter.executeParameterlessFilter = function(filterID,ajax) {
	var dummyForm = Ext.create('Ext.form.Panel', {
		items:[],
		url:'itemNavigator.action',
		baseParams: {
			queryType:1,//SAVED
			queryID: filterID,
			previousQuery:false,
			ajax:ajax
		},
		standardSubmit:CWHF.isNull(ajax)||ajax===false});
		dummyForm.getForm().submit();
};

/**
 * Render the filter parameters
 */
com.trackplus.admin.Filter.generateFilterLink = function(scope, node) {
	var title = getText('admin.customize.queryFilter.lbl.filterURL.report.encodedUrl');
	var width = 500;
	var height = 300;
	var windowItems = com.trackplus.admin.Filter.getFilterLinkItems();
	var loadUrl = 'filterLink.action';
	var loadUrlParams = {node:node};
	var windowConfig = Ext.create('com.trackplus.util.WindowConfig',{
		postDataProcess:com.trackplus.admin.Filter.postLoadProcessFilterLink,
		cancelButtonText:getText('common.btn.done')
	});
	windowConfig.showWindow(scope, title, width, height,
			{loadUrl:loadUrl, loadUrlParams:loadUrlParams},
			null, /*null,*/ windowItems);
};

/**
* Prepare adding/editing tree filter parameters
*/
com.trackplus.admin.Filter.getFilterLinkItems = function() {
	return [
		CWHF.getRadioGroup(null, 400,[{ boxLabel: getText("admin.customize.queryFilter.lbl.filterURL.reportOverviewLink"),
					name: 'urlType', inputValue: '1', checked: true },
				{ boxLabel: getText("admin.customize.queryFilter.lbl.filterURL.changesMavenPluginLink"),
					name: 'urlType', inputValue: '2', checked: false}],{itemId:'urlTypeRadio'}),
		CWHF.createCheckbox(
			"admin.customize.queryFilter.lbl.filterURL.encodeUserPassword",
			"encodePassword",
			{itemId:'encodePassword',
				disabled:false,
				checked:true,
				labelWidth:200}),
		CWHF.createCheckbox(
				"admin.customize.queryFilter.lbl.filterURL.keepMeLogged",
				"keepMeLogged",
				{itemId:'keepMeLogged',
				disabled:false,
				labelWidth:200}),
		CWHF.createTextAreaField(null, "filterLinkTextArea",{
			itemId: "filterLinkTextArea",
			anchor:'100% -45',
			listeners:{
				'render':{
					fn:function (comp) {
						comp.getEl().on('contextmenu', function(e){e.stopPropagation();}, null, {preventDefault: false});
					}
				}
			}
		})
	];
};

/**
 * Post process the data after filter parameter data has arrived containing the combo data sources and field expression configs .
 */
com.trackplus.admin.Filter.postLoadProcessFilterLink = function(data, panel) {
	var filterLinkTextArea = panel.getComponent("filterLinkTextArea");
	if (data["filterParams"]) {
		filterLinkTextArea.anchor='100% -70';
		panel.insert(1, CWHF.createCheckbox(
					"admin.customize.queryFilter.lbl.filterURL.showParameters",
					"showParameters",
					{itemId:'showParameters',
					labelWidth:200},
					{change: {fn:com.trackplus.admin.Filter.changeLinkType, scope:this, panel:panel, data:data}}));

	}
	var urlType = panel.getComponent("urlTypeRadio");
	if (urlType) {
		urlType.on("change", com.trackplus.admin.Filter.changeLinkType, this, {panel:panel, data:data});
	}
	var keepMeLogged = panel.getComponent("keepMeLogged");
	if (keepMeLogged) {
		keepMeLogged.on("change", com.trackplus.admin.Filter.changeLinkType, this, {panel:panel, data:data});
	}
	var encodePassword= panel.getComponent("encodePassword");
	if (encodePassword) {
		encodePassword.on("change", com.trackplus.admin.Filter.changeEncodePassword, this, {panel:panel, data:data});
	}

	filterLinkTextArea.setValue(data["filterUrlIssueNavigator"]);
};

/**
 * Set the URL after the link type is changed
 */
com.trackplus.admin.Filter.changeLinkType = function(radioGroupOrCheckbox, newValue, oldValue, options) {
	var panel = options.panel;
	var data = options.data;
	if (panel) {
		var filterLinkValue = null;
		var keepMeLogged = panel.getComponent("keepMeLogged");
		var encodePassword = panel.getComponent("encodePassword");
		var urlType = panel.getComponent("urlTypeRadio");
		var checkedArr = urlType.getChecked();
		var filterUrlChecked = false;
		var checkedRadio;
		if (checkedArr.length>0) {
			checkedRadio = checkedArr[0];
			if (checkedRadio.getSubmitValue()===1) {
				filterUrlChecked = true;
				keepMeLogged.setDisabled(false);
				encodePassword.setDisabled(false);
				if(encodePassword.getValue()===false){
					filterLinkValue = data["filterUrlIssueNavigatorNoUser"];
				}else{
					if (keepMeLogged.getValue()) {
						filterLinkValue = data["filterUrlIssueNavigatorKeep"];
					} else{
						filterLinkValue = data["filterUrlIssueNavigator"];
					}
				}
			} else {
				filterLinkValue = data["filterUrlMavenPlugin"];
				keepMeLogged.setDisabled(true);
				encodePassword.setDisabled(true);
			}
		}
		var showParameters = panel.getComponent("showParameters");
		if (showParameters) {
			if (showParameters.getValue()) {
				filterLinkValue = filterLinkValue+"&"+data["filterParams"];
			}
		}
		var filterLinkTextArea = panel.getComponent("filterLinkTextArea");
		if (filterLinkTextArea) {
			filterLinkTextArea.setValue(filterLinkValue);
		}
		if (keepMeLogged.getValue() && filterUrlChecked) {
			filterLinkTextArea.inputEl.setStyle("color", "red");
		} else {
			filterLinkTextArea.inputEl.setStyle("color", "black");
		}
	}
};

com.trackplus.admin.Filter.changeEncodePassword=function(radioGroupOrCheckbox, newValue, oldValue, options) {
	var panel = options.panel;
	var data = options.data;
	var filterLinkValue = null;
	if (panel) {
		var keepMeLogged = panel.getComponent("keepMeLogged");
		var encodePassword = panel.getComponent("encodePassword");
		if(encodePassword.getValue()){
			keepMeLogged.setDisabled(false);
			if (keepMeLogged.getValue()) {
				filterLinkValue = data["filterUrlIssueNavigatorKeep"];
			} else{
				filterLinkValue = data["filterUrlIssueNavigator"];
			}
		}else{
			filterLinkValue = data["filterUrlIssueNavigatorNoUser"];
			keepMeLogged.setValue(false);
			keepMeLogged.setDisabled(true);
		}
		var filterLinkTextArea = panel.getComponent("filterLinkTextArea");
		if (filterLinkTextArea) {
			filterLinkTextArea.setValue(filterLinkValue);
		}
		if (keepMeLogged.getValue() && filterUrlChecked) {
			filterLinkTextArea.inputEl.setStyle("color", "red");
		} else {
			filterLinkTextArea.inputEl.setStyle("color", "black");
		}
	}
};
