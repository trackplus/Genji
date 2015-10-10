/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */

//controller
Ext.define('com.trackplus.itemNavigator.MassOperationController',{
	extend:'Ext.Base',
	config: {
		model:null,
		itemNavigatorController: null
	},
	copy:false,
	selectedIssueIDs:[],
	constructor : function(cfg) {
		var config = cfg || {};
		this.initialConfig = config;
		Ext.apply(this, config);
		this.copy=this.model.copy;
		this.selectedIssueIDs=this.model.selectedIssueIDs;
	},
	getTitle: function() {
		if (this.copy){
			return getText('common.btn.bulkCopy');
		}else{
			return getText('common.btn.bulkEdit');
		}
	},

	getSaveLabel: function() {
		if (this.copy) {
			return getText('common.btn.copy');
		}else{
			return getText('common.btn.save');
		}
	},

	/**
	 * Handler for add/edit a node/row
	 * title: 'add'/'edit'/'copy'
	 * recordData: the selected record (tree node data or grid row data)
	 * add: whether it is add or edit
	 * fromTree: operations started from tree or from grid
	 * loadParams
	 * submitParams
	 * refreshParams
	 * refreshParamsFromResult
	 */
	showDialog: function() {
		var width = 900;
		var height = 600;
		var loadParams = {selectedIssueIDs:this.selectedIssueIDs, bulkCopy:this.copy};
		var load = {loadUrl:"massOperationEdit.action", loadUrlParams:loadParams};
		var submitParams = {selectedIssueIDs:this.selectedIssueIDs, bulkCopy:this.copy};
		var submit = {
					submitUrl:"massOperationEdit!save.action",
					submitUrlParams:submitParams,
					submitButtonText:this.getSaveLabel(),
					refreshAfterSubmitHandler:this.reload, loading:true};
		var postDataProcess = this.postDataProcess;
		var windowParameters = {title:this.getTitle(),
			width:width,
			height:height,
			load:load, submit:submit,
			items:[],
			postDataProcess:postDataProcess,
            preSubmitProcess: this.preSubmitProcess,
			panelConfig:{
				bodyStyle:{
					padding:'0 0 0 10'
				}
			}
		};
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
		windowConfig.showWindowByConfig(this);
	},

    preSubmitProcess: function(submitUrlParams, panel) {
        var massOperationExpressionPanels = panel.items;
        var startAt = 1;
        if (this.copy) {
            startAt=2;
        }
        for ( var i=startAt;i<massOperationExpressionPanels.getCount();i++ ) {
            var expressionPanel = massOperationExpressionPanels.getAt(i);
            var numberOfItems = expressionPanel.items.getCount();
            var valueControl = expressionPanel.items.getAt(numberOfItems-1);
            if (valueControl!=null && valueControl.beforeSubmit!=null && !valueControl.isDisabled()) {
                valueControl.beforeSubmit.call(this);
            }
        }
        return submitUrlParams;
    },

	postDataProcess: function(data, panel) {
		this.populateFieldExpressions(this, panel, data);
	},

	/**
	 * Refreshes a grid's store and selects a row by rowToSelect parameter if it is specified
	 * Called from selectAfterReload() (grid and tree refresh) and from simple grid CRUD
	 */
	reload: function() {
		this.itemNavigatorController.refresh.call(this.itemNavigatorController);
	},

	populateFieldExpressions: function(scope, massOperationMainPanel, data) {
		if (massOperationMainPanel!=null) {
			var massOperationPanels = [{
				xtype: 'component',
				cls:"infoBox_bottomBorder",
				border:true,
				html: getText("itemov.massOperation.lbl.numberOfSelectedIssues") + '&nbsp;'+data['numberOfSelectedIssues']
			}];
			if (this.copy) {
				var labelWidth=175;
				var width=labelWidth+25;
				var enableCopyAttachments = true;//data['enableCopyAttachments'];
				var enableCopyChildren = true;//data['enableCopyChildren'];
				massOperationPanels.push({
					xtype:'panel',
					border:false,
					bodyBorde:false,
					anchor:'100%',
					margin:'5 5 5 5',
					//height:30,
					layout:'hbox',
					items:[
						CWHF.createCheckbox("itemov.massOperation.lbl.copyHistory", "deepCopy",{labelWidth:labelWidth,width:width}),
						CWHF.createCheckbox("itemov.massOperation.lbl.copyAttachments",
							"copyAttachments", {disabled:!enableCopyAttachments,labelWidth:labelWidth,width:width}),
						CWHF.createCheckbox("itemov.massOperation.lbl.copyChildren",
							"copyChildren", {disabled:!enableCopyChildren,labelWidth:labelWidth,width:width}),
						CWHF.createCheckbox("itemov.massOperation.lbl.copyWatchers",
							"copyWatchers", {labelWidth:labelWidth,width:width}, {change: {fn: this.changeCopyWatchers,
								scope:this}})
					]
				});
			}
			var massOperationExpressions = data['expressions'];
			if (massOperationExpressions!=null) {
				//massOperationPanels.push(this.createMassOperationHeader());
				for(var i=0;i<massOperationExpressions.length;i++){
					var expression=massOperationExpressions[i];
					var cls='massOperationPanel';
					if(i%2==1){
						cls="massOperationOddPanel";
					}
					var expressionPanel = this.createMassOperationExpression(scope,
							expression, massOperationMainPanel,cls);
					massOperationPanels.push(expressionPanel);
				}
				//add all panels at once
				massOperationMainPanel.add(massOperationPanels);
			}
		}
	},

	changeCopyWatchers: function(checkboxField, newValue, oldValue, options) {
		var consultedPanel = this.formEdit.getComponent("expressionPanel-1");
		if (consultedPanel!=null) {
			var consultedCheckBox = consultedPanel.getComponent("selectedFieldMap.pseudoField1");
			if (consultedCheckBox!=null) {
				consultedCheckBox.setDisabled(newValue);
				if (newValue==true) {
					this.changeFieldSelection(consultedCheckBox, false, true, {panel:consultedPanel, field:"-1"})
				} else {
					this.changeFieldSelection(consultedCheckBox, consultedCheckBox.getValue(), false, {panel:consultedPanel, field:"-1"})
				}
			}
		}
		var informedPanel = this.formEdit.getComponent("expressionPanel-2");
		if (informedPanel!=null) {
			var informedCheckBox = informedPanel.getComponent("selectedFieldMap.pseudoField2");
			if (informedCheckBox!=null) {
				informedCheckBox.setDisabled(newValue);
				if (newValue==true) {
					this.changeFieldSelection(informedCheckBox, false, true, {panel:informedPanel, field:"-2"})
				} else {
					this.changeFieldSelection(informedCheckBox, informedCheckBox.getValue(), false, {panel:informedPanel, field:"-2"})
				}
			}
		}
	},

	createMassOperationHeader: function() {
		return Ext.create("Ext.panel.Panel", {
			border:false,
			defaults: {margin:"4 4 0 0"},
			layout: {
				type:'hbox'
				},
			items: [{
				xtype: 'label',
				html: getText("itemov.massOperation.lbl.active"),
				width: 80
			}, {
				xtype: 'displayfield',
				fieldLabel: getText("itemov.massOperation.lbl.fieldName"),
				width:300,
				labelWidth:90,
				labelAlign:'right',
				value: getText("itemov.massOperation.lbl.action")
			}, {
				xtype: 'label',
				html: getText("itemov.massOperation.lbl.fieldValue"),
				width: 200
			}]
		});
	},

	/**
	 * Create a simple filter expression
	 */
	createMassOperationExpression: function(scope, bulkExpression, massOperationMainPanel,cls) {
		var expressionPanel = Ext.create("Ext.panel.Panel", {
			itemId: 'expressionPanel' + bulkExpression['field'],
			border:false,
			bodyBorder:false,
			cls:cls,
			layout: {
				type:'hbox',
				pack: 'start',
				align: 'top'//'stretch'
			}
		});
		var fieldSelection = CWHF.createCheckbox(null, bulkExpression['fieldName'], {margin:'0 0 0 5',width:24,border:true,cls:'massOperationCheck'},
				{change: {fn: this.changeFieldSelection,
					scope:this,
					panel:expressionPanel, field:bulkExpression['field'], massOperationMainPanel:massOperationMainPanel}});
		expressionPanel.add(fieldSelection);
		var disabled = true;
		var jsonConfig = bulkExpression['jsonConfig'];
		if (jsonConfig!=null && jsonConfig.disabled!=null) {
			disabled = jsonConfig.disabled;
		}
		var setterCombo = CWHF.createCombo(bulkExpression['fieldLabel'],
				bulkExpression['relationName'],
				{disabled:disabled,
					width:350,
					data:bulkExpression['setterRelations'],
					value:bulkExpression['selectedRelation'],
					labelWidth:150,
					margin:'2 5 2 5',
					labelIsLocalized:true
				},
				{select: {fn: this.changeSetter, scope:scope,
					panel:expressionPanel, bulkExpression:bulkExpression}});
		expressionPanel.add(setterCombo);
		this.addValuePart(scope, bulkExpression.valueRenderer, bulkExpression.jsonConfig, bulkExpression, expressionPanel, setterCombo);
		return expressionPanel;
	},


	getComponentItemID: function(baseName, field) {
		if (field>0) {
			return baseName + ".field" + field;
		} else {
			return baseName + ".pseudoField" + Math.abs(field);
		}
	},

	changeFieldSelection: function(checkboxField, newValue, oldValue, options) {
		var projectIDs = null;
		var expressionPanel = options.panel;
		var field = options.field;
		var setterCombo = expressionPanel.getComponent(this.getComponentItemID("setterRelationMap",field));
		if (setterCombo!=null) {
			setterCombo.setDisabled(!newValue);
		}
		var valuePart = expressionPanel.getComponent(this.getComponentItemID("displayValueMap",field));
		if (valuePart!=null) {
			valuePart.setDisabled(!newValue);
		}
		if (field==1 || field==2) {
			var params = new Object();
			if (field==1) {
				params['projectRefresh'] = true;
				if (newValue) {
					//get the submit value not the visible label value
					params['projectID'] = valuePart.getSubmitValue();
				}
				this.refreshContext(expressionPanel, params, field/*, newValue*/);
			} else {
				if (field==2) {
					params['issueTypeRefresh'] = true;
					if (newValue) {
						params['issueTypeID'] = valuePart.getValue();
					}
					this.refreshContext(expressionPanel, params, field/*, false*/);
				}
			}
		}
		if (field==19 || field==20 || field==29 || field==30) {
			//disable opposite date
			var massOperationMainPanel = options.massOperationMainPanel;
			if (massOperationMainPanel!=null) {
				var adjustValue = false;
				var datePanel = massOperationMainPanel.getComponent('expressionPanel' + field);
				if (datePanel!=null) {
					var valuePart = expressionPanel.getComponent(this.getComponentItemID("displayValueMap", field));
					if (valuePart!=null) {
						adjustValue = valuePart.getAdjustCheckBoxValue();
						if (adjustValue) {
							var oppositeDateField = null;
							if (field==19) {
								oppositeDateField = 20;
							} else {
								if (field==20) {
									oppositeDateField = 19;
								} else {
									if (field==29) {
										oppositeDateField = 30;
									} else {
										if (field==30) {
											oppositeDateField = 29;
										}
									}
								}
							}
							var oppositeDatePanel = massOperationMainPanel.getComponent('expressionPanel' + oppositeDateField);
							if (oppositeDatePanel!=null) {
								var oppositeFieldCheckBox = oppositeDatePanel.getComponent(this.getComponentItemID("selectedFieldMap", oppositeDateField));
								if (oppositeFieldCheckBox!=null) {
									var oppositeFieldCheckBoxDisabled = false;
									if (!newValue) {
										oppositeFieldCheckBoxDisabled = false;
									} else {
										oppositeFieldCheckBoxDisabled = adjustValue;
									}
									oppositeFieldCheckBox.setDisabled(oppositeFieldCheckBoxDisabled);
									var setterCombo = oppositeDatePanel.getComponent(this.getComponentItemID("setterRelationMap", oppositeDateField));
									if (setterCombo!=null) {
										setterCombo.setDisabled(oppositeFieldCheckBoxDisabled || !oppositeFieldCheckBox.getValue());
									}
									var valuePart = oppositeDatePanel.getComponent(this.getComponentItemID("displayValueMap", oppositeDateField));
									if (valuePart!=null) {
										valuePart.setDisabled(oppositeFieldCheckBoxDisabled || !oppositeFieldCheckBox.getValue());
									}
								}
							}
						}
					}
				}
			}
		}
	},

	refreshContext: function(expressionPanel, params, field/*, projectFieldWasSelected*/) {
		var mainPanel = expressionPanel.ownerCt;
		if (field==1) {
			params['issueTypeID'] = this.getContextField(mainPanel, 2);
		} else {
			if (field==2) {
				params['projectID'] = this.getContextField(mainPanel, 1)
			}
		}
		params['selectedIssueIDs'] = this.selectedIssueIDs;
		mainPanel.setLoading(true);
		Ext.Ajax.request({
			url: 'massOperationEdit.action',
			params: params,
			scope: this,
			disableCaching:true,
			success: function(response) {
				var responseJson = Ext.decode(response.responseText);
				massOperationExpressions = responseJson.data['expressions'];
				if (mainPanel!=null) {
					if (massOperationExpressions!=null) {
						var projectPanel = mainPanel.getComponent('expressionPanel1');
						var projectFieldWasSelected = false;
						if (projectPanel!=null) {
                             var fieldNameCheckbox = projectPanel.getComponent("selectedFieldMap.field1");
                             if (fieldNameCheckbox!=null) {
                            	 projectFieldWasSelected = fieldNameCheckbox.getValue();
                             }
						}
						var releaseNoticedFound = false;
						var releaseScheduledFound = false;
						Ext.Array.forEach(massOperationExpressions, function(expression) {
							var field = expression.field;
							if (field==8) {
								releaseNoticedFound = true;
							} else {
								if (field==9) {
									releaseScheduledFound = true;
								}
							}
							var disabled = expression.jsonConfig['disabled'];
							var expressionPanel = mainPanel.getComponent('expressionPanel' + field);
                            if (expressionPanel!=null) {
                                var selectedFieldName = expression['fieldName'];
                                var fieldNameCheckbox = expressionPanel.getComponent(selectedFieldName);
                                fieldNameCheckbox.setDisabled(projectFieldWasSelected);
                                fieldNameCheckbox.setValue(!disabled);
                                var setterRelationName = expression['relationName'];
                                var setterRelationCombo = expressionPanel.getComponent(setterRelationName);
                                setterRelationCombo.setDisabled(disabled);
                                var valueComponentName = expression.valueName;
                                var valueComponent = expressionPanel.getComponent(valueComponentName);
                                if (valueComponent!=null) {
                                    //the value will not be removed from Ext.form.Basic only from the UI
                                    //workaround: set the fields to disabled to not to be submitted then remove the component
                                    valueComponent.setDisabled(true);
                                    expressionPanel.remove(valueComponent);
                                }
                                if (expression.valueRenderer!=null) {
                                    this.addValuePart(this, expression.valueRenderer, expression.jsonConfig,
                                            expression, expressionPanel, setterRelationCombo);
                                }
                            } else {
                                var issueType = mainPanel.getComponent('expressionPanel2');
                                if (issueType!=null) {
                                    var index = mainPanel.items.indexOf(issueType);
                                    if (index!=null) {
                                    	if (field!=8 && field!=9) {
                                    		//releases before item type, other dependent lists after item type
                                    		index = index+1;
                                    	}
                                        mainPanel.insert(index, this.createMassOperationExpression(this, expression, mainPanel, null));
                                    } else {
                                        mainPanel.add(this.createMassOperationExpression(this, expression, mainPanel, null));
                                    }
                                } else {
                                    mainPanel.add(this.createMassOperationExpression(this, expression, mainPanel, null));
                                }
                                var expressionPanel = mainPanel.getComponent('expressionPanel' + field);
                                if (expressionPanel!=null) {
                                    var selectedFieldName = expression['fieldName'];
                                    var fieldNameCheckbox = expressionPanel.getComponent(selectedFieldName);
                                    fieldNameCheckbox.setDisabled(projectFieldWasSelected);
                                    fieldNameCheckbox.setValue(!disabled);
                                }
                            }
						}, this);
						//remove the release fields if they are not defined in the new project
						if (!releaseNoticedFound) {
							var expressionPanel = mainPanel.getComponent('expressionPanel8');
							if (expressionPanel!=null) {
								mainPanel.remove(expressionPanel);
							}
						}
						if (!releaseScheduledFound) {
							var expressionPanel = mainPanel.getComponent('expressionPanel9');
							if (expressionPanel!=null) {
								mainPanel.remove(expressionPanel);
							}
						}
					}
				}
				mainPanel.setLoading(false);
			},
			failure: function(result){
				mainPanel.setLoading(false);
				Ext.MessageBox.show({
					title: this.failureTitle,
					msg: result.responseText,
					buttons: Ext.Msg.OK,
					icon: Ext.MessageBox.WARNING
				})
			},
			method:"POST"
		});
	},

	getContextField: function(mainPanel, fieldID) {
		var fieldComponent = this.getFieldComponent(mainPanel, fieldID);
		if (fieldComponent!=null && !fieldComponent.isDisabled()) {
			return fieldComponent.getValue();
		}
		return null;
	},

	getFieldComponent: function(mainPanel, fieldID) {
		var contextPanel = mainPanel.getComponent('expressionPanel' + fieldID);
		if (contextPanel!=null) {
			return contextPanel.getComponent(this.getComponentItemID("displayValueMap", fieldID));
		}
		return null;
	},

	/**
	 * Reload the value part of a bulk expression after changing the setter
	 * or after changing any or the composite select part
	 */
	changeSetter: function(combo, records, options) {
		var fieldID = options.bulkExpression.field;
		var valueComponentName = options.bulkExpression.valueName;
		var valueComponent = options.panel.getComponent(valueComponentName);
		var value = null;
		if (valueComponent!=null) {
			value = valueComponent.getFieldValueJson();
		}

		var relationID = combo.getValue();
		var params = value;
		if (value==null) {
			params = new Object();
		}
		params['fieldID'] = fieldID;
		params['relationID'] = relationID;
		params['issueTypeID'] = this.getContextField(combo.ownerCt.ownerCt, 2);
		params['projectID'] = this.getContextField(combo.ownerCt.ownerCt, 1);
		params['selectedIssueIDs'] = this.selectedIssueIDs;
		Ext.Ajax.request({
			url: 'massOperationFieldSetterAction.action',
			params: params,
			scope: this,
			disableCaching:true,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				if (valueComponent!=null) {
					//the value will not be removed from Ext.form.Basic only from the UI
					//workaround: set the fields to disabled to not to be submitted then remove the component
					valueComponent.setDisabled(true);
					options.panel.remove(valueComponent, true);
				}
				if (responseJson.valueRenderer!=null && responseJson.valueRenderer!="") {
					options.bulkExpression.valueRenderer = responseJson.valueRenderer;
					this.addValuePart(this, responseJson.valueRenderer, responseJson.jsonConfig,
						options.bulkExpression, options.panel, combo);
				}
			},
			failure: function(result){
				Ext.MessageBox.show({
					title: this.failureTitle,
					msg: result.responseText,
					buttons: Ext.Msg.OK,
					icon: Ext.MessageBox.WARNING
				})
			},
			method:"POST"
		});
	},

	/**
	 * Configures and adds the value part (the dynamic part) to the filter expressions by first rendering of
	 * simple or "in tree" field expressions and after changing the matcher or the field
	 */
	addValuePart: function(scope, valueRenderer, jsonConfig, bulkExpression, expressionPanel, setterRelationCombo) {
		if (bulkExpression.valueRenderer!=null && bulkExpression.valueRenderer!="") {
			if (jsonConfig!=null) {
				//the jsonConfig coming from the server for each bulkExpression or after a matcher or field change
				//is set with further parameters used by cascadingSelectMatcher
				jsonConfig.scope=scope;
				jsonConfig.selectHandler=this.changeCompositePart;
				jsonConfig.expressionPanel=expressionPanel;
				jsonConfig.bulkExpression=bulkExpression;
				jsonConfig.setterRelationCombo = setterRelationCombo;
				jsonConfig.selectedIssueIDs = this.selectedIssueIDs;
				var valueControl = Ext.create(valueRenderer, {
					jsonData:jsonConfig,
					margin:'2 0 2 0'
				});
				if (jsonConfig["fieldID"]==1) {
					//project picker
					valueControl.postProcessSelectScope = this;
					valueControl.postProcessSelect = function() {
						var params = new Object();
						params['projectRefresh'] = true;
						//get the submit value not the visible label value
						var projectID = valueControl.getSubmitValue();
						params['projectID'] = projectID;
						this.refreshContext(expressionPanel, params, 1/*, true*/);
					};
				}
				/*var oldValueControlName = jsonConfig['name'];
				var oldValueControl = expressionPanel.getComponent(oldValueControlName);
				if (oldValueControl!=null) {
					expressionPanel.remove(oldValueControl);
				}*/
				expressionPanel.add(valueControl);
			}
		}
	}
});

