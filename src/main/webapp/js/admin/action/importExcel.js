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

Ext.define('com.trackplus.admin.action.ImportExcel',{
	extend:'com.trackplus.admin.action.ImportWizard',
	config: {
	},
	itemNavigatorController: null,
	selectedSheet: null,
	workItemIDFieldIDs:null,
	window: null,
	dialogHeight:470,
	dialogWidth:850,

	getNumberOfCards: function() {
		return 4;
	},

	getTitle: function(cardNo) {
		var titleKey=null;
		switch (cardNo) {
		case 1:
			titleKey = "admin.actions.importExcel.title.upload";
			break;
		case 2:
			titleKey = "admin.actions.importExcel.title.excelFieldMatch";
			break;
		case 3:
			titleKey = "admin.actions.importExcel.invalidValueHadling.title";
			break;
		case 4:
			titleKey = "admin.actions.importExcel.title.errorAndConflict";
			break;
		}
		if (titleKey) {
			return getText(titleKey);
		} else {
			return '';
		}
	},

	getFilePattern: function(fileName) {
		return /^.*\.(xls|XLS|xlsx|XLSX)$/;
	},

	getFileTypeLabel: function() {
		return getText('admin.actions.importExcel.lbl.uploadFile');
	},

	getFileEmptyText: function() {
	    //return getText("admin.actions.importExcel.lbl.fileEmptyText");
	    //TODO replace back as soon as the drag and drop works in browser
	    return "";
	},

	getImportWizardUrl: function(card, reload) {
		if (card===1) {
			return 'excelUploadRender.action';
		} else {
			if (card===2) {
				return 'excelUpload.action';
			} else {
				if (card===3) {
					if (reload) {
						return "excelInvalidValueHandling.action";
					} else {
						return "excelFieldMatch!save.action";
					}
				} else {
					if (card===4) {
						return "excelImport.action";
					}
				}
			}
		}
	},

	getDataBeforeCardDelete: function(cardNo, reload) {
	    var params = null;
	    if (cardNo===4) {
	        params = {fileName:this.fileName, selectedSheet: this.selectedSheet};
	        if (reload && this.workItemIDFieldIDs) {
	            var overwriteMap = new Object();
	            var card4 = this.wizardPanel.getComponent("card4");
	            var errorAndConflictPanel = card4.getComponent('errorAndConflict');
	            var conflictPanel = errorAndConflictPanel.getComponent('conflictPanel');
	            Ext.Array.forEach(this.workItemIDFieldIDs, function(workItemIDFieldID) {
	                var radioButtons = conflictPanel.getComponent('conflictResoultion'+workItemIDFieldID);
	                var paramName = "overwriteMap['" + workItemIDFieldID + "']";
	                var paramValue = CWHF.getSelectedRadioButtonValue(radioButtons);
	                params[paramName]= paramValue;
	            }, this);
	        }

	    }
	    return params;
	},

	/**
	 * Load/submit the data for on the cardNo
	 * cardNo: the card number to go next, or the actual card number if reload
	 * reload: whether to reload the current card or submit to the current values and go to the next card
	 * return false and optionally show an alert box if there is a validation problem which prevents navigating to the next card
	 */
	loadDataForCard: function(cardNo, reload, params) {
		if (cardNo===1) {
			//for the very first card nothing to submit only render (load)
			var panel1 = this.wizardPanel.getComponent('card1');
			panel1.add(this.getImportWizardItemsForCard(cardNo));
			panel1.getForm().load({
				url: this.getImportWizardUrl(cardNo, reload),
				scope: this,
				success : function(form, action) {
					try{
						this.postDataProcess(panel1, action.result.data, 1);
						//if data is valid we can go to the next card
					}catch(ex){}
				},
				failure: function(form, action) {
					Ext.MessageBox.alert(this.failureTitle, action.response.responseText)
				}
			});
		} else {
			//from card to each card starts with a submit
			if (cardNo===2) {
				var panel1 = this.wizardPanel.getComponent('card1');
				if (!panel1.getForm().isValid()) {
					// Ext.MessageBox.alert('',
					Ext.MessageBox.show({
							title: '',
							msg: getText('admin.actions.importTp.lbl.uploadFileNotSpecified'),
							buttons: Ext.Msg.OK,
							icon: Ext.MessageBox.ERROR
					});
					return false;
				}
				var importFile = panel1.getComponent('uploadFile');
				if (!this.validateFileExtension(importFile.getRawValue())) {
					Ext.MessageBox.alert(getText('admin.actions.importTp.lbl.uploadFileWrongType'),
						getText('common.err.fileExpectedType', this.getFileTypeLabel()));
					return false;
				}
				if (this.uploadDone) {
					Ext.MessageBox.alert(getText('admin.actions.importTp.err.uploadAgain'),
							getText('admin.actions.importTp.err.uploadAgain', this.getFileTypeLabel()));
						return false;
				}
				this.uploadDone = true;
				this.submitFromCardToCardMessageOnFailure(1, 2, null, reload, true);
			} else {
				if (cardNo===3) {
					var cardToSubmit = 2;
					if (reload) {
						cardToSubmit = 3;
					}
					var params = {fileName:this.fileName, selectedSheet: this.selectedSheet, reload: reload};
					this.submitFromCardToCardMessageOnFailure(cardToSubmit, 3, params, reload, true);
				} else {
					if (cardNo===4) {
	                    if (CWHF.isNull(params)) {
	                        params = {fileName:this.fileName, selectedSheet: this.selectedSheet};
	                    }
						this.submitFromCardToCard(3, 4, params, false, 300000);//5 minutes timeout
					}
				}
			}
		}
	},


	/**
	 * Get the initial items for a card
	 * Additional items can be added once the data is back from the server
	 */
	getImportWizardItemsForCard: function(card) {
		if (card===1) {
			var items = this.getImportWizardCard1Items();
			if(items && items.length > 0) {
				items[0].setMargin('2 0 0 0');
			}
			return items;
		} else {
			if (card===2) {
				return this.getExcelMappingItems();
			} else {
				if (card===3) {
						return this.getExcelInvalidHandlingItems();
				} else {
					if (card===4) {
						return  this.getExcelErrorAndConflictHandlingItems();
					}
				}
			}
		}
	},

	postDataProcess: function(panel, jsonResult, cardTo, cardFrom) {
		if (cardTo===1) {
			this.postProcessCard1(panel, jsonResult);
		} else {
			if (cardTo===2) {
				this.postProcessExcelMapping(panel, jsonResult);
				this.actualizeToolbar(this.wizardPanel);
			} else {
				if (cardTo===3) {
					this.postProcessInvalidHandling(panel, jsonResult, cardTo, cardFrom);
				} else {
					if (cardTo===4) {
						this.postProcessExcelErrorAndConflictHandling(panel, jsonResult);
					}
				}
			}
		}
	},

	/**************************Excel column - Genji field mappings**************************************/

	/**
	 * Gets the items for excel field mapping, before the data has arrived
	 */
	getExcelMappingItems: function() {
		var panelConfig = {
				itemId: 'mappings',
				layout: {
					type: 'table',
					columns: 4,
					tdAttrs: { style: {
							padding: '5px 15px 0 0'
						}
					}
				},
				bodyStyle:	'margin: 0 0 0 35px;',
				border: false,
				method: "POST",
				autoScroll:	true,
				items: []
			};
		return [{xtype: 'component',
	            cls:"infoBox_bottomBorder",
	            border:true,
	            anchor:'100%',
	            margin:"0 0 0 0",
	            html: getText("admin.actions.importExcel.fieldMatch.message")},
	        CWHF.createCombo('admin.actions.importExcel.fieldMatch.lbl.sheet', 'selectedSheet',
				{labelWidth:250, width:470, margin: '5 0 0 70', labelAlign:"right", itemId:"selectedSheet"},
				{select: {fn: this.reloadExcelFieldMapping, scope:this}}),
				Ext.create('Ext.form.Panel', panelConfig)];
	},

	/**
	 * Add field mappings data for rendering after the result has arrived
	 */
	postProcessExcelMapping: function(panel, jsonResult) {
		var data = jsonResult.data;
		this.fileName = data['fileName'];
		var excelFieldMapperExpressions = data['mappingExpressions'];
		var sheetNames = data['sheetNames'];
		var sheetsCombo = panel.getComponent('selectedSheet');
		sheetsCombo.store.loadData(data['sheetNames']);
		this.selectedSheet = data['selectedSheet'];
		sheetsCombo.setValue(data['selectedSheet']);
		var fieldList = data['fieldList'];
		items = [];
		items.push({xtype: 'label',
			html: '<b>'+getText('admin.actions.importExcel.fieldMatch.lbl.columnNumber')+'</b>'
			});
		items.push({xtype: 'label',
			html: '<b>'+getText('admin.actions.importExcel.fieldMatch.lbl.columnLabel')+'</b>'
			});
		items.push({xtype: 'label',
			html: '<b>'+getText('admin.actions.importExcel.fieldMatch.lbl.field')+'</b>'
			});
		items.push({xtype: 'label',
			html: '<b>'+getText('admin.actions.importExcel.fieldMatch.lbl.isIdentifier')+'</b>'
			});
		if (panel) {
			if (excelFieldMapperExpressions) {
				//var mappingExpressionPanels = [];
				Ext.Array.forEach(excelFieldMapperExpressions, function(mappingExpression) {
					var columnIndex = mappingExpression.columnIndex;
					items.push({xtype: 'label',
						html: mappingExpression.columnLetter,
						width: 200
						});
					items.push({xtype: 'label',
						html: mappingExpression.columnName,
						width: 200
					});
					items.push(CWHF.createCombo(null,
							'columnIndexToFieldIDMap[' + columnIndex + ']',
							{itemId:'columnMapping' + columnIndex,
							width:200,
							data:fieldList,
							value:mappingExpression.fieldID},
							{select: {fn: this.setIsIdentifier, scope:this,
								panel: panel,
								columnIndex: columnIndex,
								possibleIdentifiers: data['possibleIdentifiers'],
								mandatoryIdentifiers: data['mandatoryIdentifiers']}},
								'columnMapping'+columnIndex));

					items.push(CWHF.createCheckbox('','columnIndexIsIdentifierMap[' + columnIndex + ']',
							{itemId:'columnIsIdentifier'+columnIndex, width: 100, padding: '0 0 0 20',
						disabled:mappingExpression.identifierDisabled, value:mappingExpression.isIdentifier}));
				}, this);
			}
		}
		var mappingsPanel = panel.getComponent('mappings');
		mappingsPanel.removeAll(true);
		//add all panels at once
		mappingsPanel.add(items);
	},

	/**
	 * Reload the field mapping after changing the excel sheet
	 */
	reloadExcelFieldMapping: function(combo, records, eOpts) {
		var panel = this.wizardPanel.getComponent('card2');
		panel.setLoading(getText("common.lbl.loading"));
		this.selectedSheet = combo.getValue();
		Ext.Ajax.request({
			url: 'excelFieldMatch.action',
			params: {fileName:this.fileName, selectedSheet: this.selectedSheet},
			disableCaching:true,
			scope: this,
			success: function(response) {
				var responseJson = Ext.decode(response.responseText);
				this.postProcessExcelMapping(panel, responseJson);
				panel.setLoading(false);
			},
			failure: function(response){
				com.trackplus.util.requestFailureHandler(response);
				panel.setLoading(false);
			}
		})
	},

	/**
	 * Set/reset disable automatically the identifier checkbox by selecting a field
	 */
	setIsIdentifier: function(combo, records, options) {
		var panel = options.panel;
		var columnIndex = options.columnIndex;
		var possibleIdentifiers = options.possibleIdentifiers;
		var mandatoryIdentifiers = options.mandatoryIdentifiers;
		var mappingPanel = panel.getComponent('mappings');
		if (mappingPanel) {
			var fieldMapper = mappingPanel.getComponent("columnMapping" + columnIndex);
			var columnIsIdentifier = mappingPanel.getComponent("columnIsIdentifier" + columnIndex);
			//var selectedIndex = fieldMapper.selectedIndex;
			//var selectedValue = fieldMapper.options[selectedIndex].value;
			var selectedValue = fieldMapper.getValue();
			if (CWHF.isNull(selectedValue)|| ""===selectedValue) {
				//the empty entry -> field is not mapped
				columnIsIdentifier.setValue(false);
				columnIsIdentifier.setDisabled(true);
			} else {
				var isPossibleIdentifier = Ext.Array.contains(possibleIdentifiers, selectedValue);
				var isMandatoryIdentifier = Ext.Array.contains(mandatoryIdentifiers, selectedValue);
				if (isMandatoryIdentifier) {
					columnIsIdentifier.setValue(true);
					columnIsIdentifier.setDisabled(true);
				} else {
					if (isPossibleIdentifier) {
						columnIsIdentifier.setDisabled(false);
					} else {
						columnIsIdentifier.setValue(false);
						columnIsIdentifier.setDisabled(true);
					}
				}
			}
		}
	},

	/**************************Excel invalid value handling**************************************/

	/**
	 * Gets the items for excel invalid value handling, before the data has arrived
	 */
	getExcelInvalidHandlingItems: function() {
		var panelConfig = {
				itemId: 'invalidValueHandlingPanel',
				layout: {
					type: 'table',
					columns: 3,
					tdAttrs: { style: {
						padding: '5px 15px 0 0'
					}
				}
				},
				bodyStyle:	'padding: 5px',
				border: false,
				method: "POST",
				autoScroll:	true,
				items: []
			};
		return [{xtype: 'component',
	            cls:"infoBox_bottomBorder",
	            border:true,
	            anchor:'100%',
	            margin:"0 0 0 0",
	            html: getText("admin.actions.importExcel.invalidValueHadling.message")},
	        Ext.create('Ext.form.Panel', panelConfig)];
	},

	postProcessInvalidHandling: function(panel, jsonResult, cardTo, cardFrom) {
		if (cardFrom===2) {
			this.postProcessInvalidHandlingFirstTime(panel, jsonResult)
		} else {
			this.postProcessInvalidHandlingUpdate(panel, jsonResult);
		}
	},

	/**
	 * Add invalid value handling data for rendering after the result has arrived
	 */
	postProcessInvalidHandlingFirstTime: function(panel, jsonResult, cardTo, cardFrom) {
		var data = jsonResult.data;
		var fieldList = data['fieldList'];
		var possibleValues = data['possibleValues'];
		var invalidValueHandlingList = data['invalidValueHandlingList'];
		if (panel) {
			var invalidValueHandlingPanel = panel.getComponent("invalidValueHandlingPanel");
			items = [];
			items.push({xtype: 'label',
					html: '<b>'+getText('admin.actions.importExcel.invalidValueHadling.lbl.fieldName')+'</b>',
					width: 150
				});
			items.push({xtype: 'label',
					html: '<b>'+getText('admin.actions.importExcel.invalidValueHadling.lbl.defaultfieldValue')+'</b>',
					colspan: 2
				});
			if (fieldList) {
				Ext.Array.forEach(fieldList, function(fieldBean) {
					var fieldId = fieldBean.id;
					var possibleFieldValues = possibleValues['fieldID'+fieldId];
					var invalidValueHandlingValue = possibleFieldValues['invalidValueHandlingValue'];
					var possibleValueList = possibleFieldValues['possibleFieldValues'];
					var defaultFieldValue = possibleFieldValues['defaultFieldValue'];
					items.push({xtype: 'label',
						html: fieldBean.label,
						width: 150,
						padding: '10 0 0 0'
						});
					var invalidValueHandlingRadioButtons = CWHF.getRadioButtonItems(invalidValueHandlingList,
							'invalidValueHandlingMap[' + fieldId + ']', 'id', 'label', invalidValueHandlingValue, false, true);
					var invalidValueHandlingRadioGroup =
						CWHF.getRadioGroup('', 250, invalidValueHandlingRadioButtons, {itemId:'invalidValueHandling'+fieldId},
							{change: {fn: this.onInvalidHandlingChange, scope:this,
								panel: invalidValueHandlingPanel,
								fieldId: fieldId}});
					items.push(invalidValueHandlingRadioGroup);
					var control;
					if (fieldId===1) {
	                    control = CWHF.createSingleTreePicker(null,
	                        "defaultValuesMap[" + fieldId + "]", possibleValueList, defaultFieldValue,
	                        {allowBlank:false,
	                         disabled:invalidValueHandlingValue===2,
	                         itemId:'defaultValues'+fieldId
	                        }, {select:{fn: this.onProjectSelect, scope:this,
	                            fieldId: fieldId}});
					} else {
						control = CWHF.createCombo(null,
								'defaultValuesMap[' + fieldId + ']',
								{disabled:invalidValueHandlingValue===2,
									itemId: 'defaultValues' + fieldId,
									width:200,
									maxWidth: 250,
									data:possibleValueList,
									value:defaultFieldValue},
								{select: {fn: this.onSelectDefaultValue, scope:this,
									fieldId: fieldId}},
								'defaultValues'+fieldId);
					}
					items.push(control);
				}, this);
			}
			invalidValueHandlingPanel.removeAll(true);
			invalidValueHandlingPanel.add(items);
		}
	},

	/**
	 * Change event, not
	 * @param projectPicker
	 * @param selectedProject
	 * @param options
	 */
	onProjectSelect: function(projectPicker, selectedProject, options) {
	    this.onRefreshInvalidHandling(options['fieldId']);
	},

	/**
	 * Handler for changing a default value
	 */
	onSelectDefaultValue: function(combo, records, options) {
	    this.onRefreshInvalidHandling(options['fieldId']);
	},
	/**
	 * Add invalid value handling data for rendering after the result has arrived
	 */
	postProcessInvalidHandlingUpdate: function(panel, jsonResult) {
		var data = jsonResult.data;
		var fieldList = data['fieldList'];
		var possibleValues = data['possibleValues'];
		if (panel) {
			var invalidValueHandlingPanel = panel.getComponent("invalidValueHandlingPanel");
			if (fieldList) {
				Ext.Array.forEach(fieldList, function(fieldId) {
					var possibleFieldValues = possibleValues['fieldID'+fieldId];
					//var invalidValueHandlingValue = possibleFieldValues['invalidValueHandlingValue'];
					var possibleValueList = possibleFieldValues['possibleFieldValues'];
					var defaultFieldValue = possibleFieldValues['defaultFieldValue'];
					if (fieldId!==1) {
						control = invalidValueHandlingPanel.getComponent('defaultValues' + fieldId);
						if (control) {
							control.getStore().loadData(possibleValueList);
							control.setValue(defaultFieldValue);
						}
					}
				}, this);
			}
		}
	},
	/**
	 * Handler for changing an invalid value handling
	 */
	onInvalidHandlingChange: function(field, newValue, oldValue, options) {
		var fieldId = options['fieldId'];
		var panel = options['panel'];
		var invalidValueHandlingRadioGroup = panel.getComponent('invalidValueHandling' + fieldId);
		var fieldSelect = panel.getComponent('defaultValues' + fieldId);
		var checkedArr = invalidValueHandlingRadioGroup.getChecked();
		var checkedRadioValue = null;
		if (checkedArr.length>0) {
			checkedRadio = checkedArr[0];
			if (checkedRadio) {
				checkedRadioValue = checkedRadio.getSubmitValue();
			}
		}
		fieldSelect.setDisabled(checkedRadioValue===2);
		this.onRefreshInvalidHandling(fieldId);
	},



	onRefreshInvalidHandling: function(fieldId) {
		if (fieldId===1 || fieldId===2) {
			//reload is needed only for project or issueType change
			//most of the other lists depend on project and issueType
			this.loadDataForCard(3, true);
		}
	},

	/******************************************Excel conflicts*****************************************************/

	/**
	 * Gets the items for excel import, before the data has arrived
	 */
	getExcelErrorAndConflictHandlingItems: function() {
		var panelConfig = {
				itemId: 'errorAndConflict',
				bodyStyle:	'padding: 5px',
				border: false,
				method: "POST",
				//autoScroll:	true,
				items: [
					Ext.create('Ext.panel.Panel', {
						itemId: 'errorPanel',
						border:false,
						defaults:	{
							labelStyle:'overflow: hidden;',
							margin:"0 5 0 0"
						},
						layout: {
							type:'table',
							columns:1
						}
					}),
					Ext.create('Ext.form.Panel', {
						itemId: 'conflictPanel',
						defaults:	{
							labelStyle:'overflow: hidden;',
							margin:"0 5 0 0"
						},
						layout: {
							type:'table',
							columns:7,
							tdAttrs: { style: {
								padding: '5px 15px 0 0',
								'vertical-align':'top'
							}
						}
						//autoScroll:true
						},
						border:false})
					]};
		return [Ext.create('Ext.form.Panel', panelConfig)];
	},

	/**
	 * Add error or conflict handling data for rendering after the result has arrived
	 */
	postProcessExcelErrorAndConflictHandling: function(panel, data) {
		var me = this;
		var disableFinal = data['disableFinal'];
		var toolbar =  me.win.getDockedItems('toolbar[dock="bottom"]');
		var finish = toolbar[0].getComponent('finish');
		var previous = toolbar[0].getComponent('previous');
		finish.setDisabled(disableFinal);
		//previous.setDisabled(disableFinal);
		var errorAndConflictPanel = panel.getComponent('errorAndConflict');
		var errorCode = data['errorCode'];
		if (CWHF.isNull(errorCode)) {
			var message = data['message'];
			if (message) {
				var errorPanel = errorAndConflictPanel.getComponent('errorPanel');
				errorPanel.add({xtype: 'label',
					html: '<b>' + message + '</b>'
					});
			}
			return;
		}

		switch(errorCode) {
		case 1:
			//simple message
			var errorPanel = errorAndConflictPanel.getComponent('errorPanel');
			errorPanel.add({xtype: 'label',
				html: '<b>' + data['errorMessage'] + '</b>'
				});
			break;
		case 2:
			//more messages
			var errorPanel = errorAndConflictPanel.getComponent('errorPanel');
			var errorMessages = data['errorMessage'];
			for ( var i = 0; i < errorMessages.length; i++) {
				errorPanel.add({xtype: 'label',
					html: errorMessages[i]
					});
			}
			break;
		case 3:
			//me.confirmDialogForImportNotFoundItemsAsNew();
			//grid and row errors
			var errorPanel = errorAndConflictPanel.getComponent('errorPanel');
			var gridErrors = data['gridErrors'];
			if (gridErrors) {
				for ( var i = 0; i < gridErrors.length; i++) {
					var gridError = gridErrors[i];
					var errorMessage = gridError['errorMessage'];
					var locationList = gridError['locationList'];
					if (locationList) {
						errorPanel.add({xtype: 'label',
							html: '<b>' + errorMessage + '</b>'
							});
						for (var j = 0; j < locationList.length; j++) {
							errorPanel.add({xtype: 'label',
								html: locationList[j]
								});
						}
					}
					var solutionMessage = gridError['solutionMessage'];
					if (solutionMessage) {
						errorPanel.add({xtype: 'label',
							html: '<br><b>' + solutionMessage + '</b>'
							});
					}
				}
			}
			var rowErrors = data['rowErrors'];
			if (rowErrors) {
				for (var i = 0; i < rowErrors.length; i++) {
					var rowError = rowErrors[i];
					var errorMessage = rowError['errorMessage'];
					var locationList = rowError['locationList'];
					errorPanel.add({xtype: 'label',
						html: '<b>' + errorMessage + '</b>'
						});
					errorPanel.add({xtype: 'label',
						html: locationList
						});
				}
			}
			break;
		case 4:
			//field conflicts
			var errorMessage = data['errorMessage'];
			var conflicts = data['conflicts'];
			if (conflicts) {
				var conflictPanel = errorAndConflictPanel.getComponent('conflictPanel');
				var conflictResolutionList = data['conflictResolutionList'];
				items = [];
					if (errorMessage) {
					items.push({xtype: 'label',
						html: errorMessage,
						colspan: 7
					});
				}
				items.push({xtype: 'label',
					html: '<b>'+getText('admin.actions.importExcel.conflict.lbl.row')+'</b>'
					});
				items.push({xtype: 'label',
					html: '<b>'+data['issueNoLabel']+'</b>'
					});
				items.push({xtype: 'label',
					html: '<b>'+getText('admin.actions.importExcel.conflict.lbl.column')+'</b>'
					});
				items.push({xtype: 'label',
					html: '<b>'+getText('admin.actions.importExcel.conflict.lbl.field')+'</b>'
					});
				items.push({xtype: 'label',
					html: '<b>'+getText('admin.actions.importExcel.conflict.lbl.excelValue')+'</b>'
					});
				items.push({xtype: 'label',
					html: '<b>'+getText('admin.actions.importExcel.conflict.lbl.trackplusValue')+'</b>'
					});
				var conflictResolutionListBold = [];
				Ext.Array.forEach(conflictResolutionList, function(conflictResolution) {
					conflictResolutionListBold.push({id:conflictResolution["id"], label:"<b>"+conflictResolution["label"]+"</b>"});
				}, this);
				var conflictResolutionRadioButtons = CWHF.getRadioButtonItems(conflictResolutionListBold,
						'conflictResoultionEntry', 'id', 'label', false, false, true);
				var conflictResolutionRadioGroup = CWHF.getRadioGroup('', 300, conflictResolutionRadioButtons, {itemId:'conflictResoultionRadioGroup'},
						{change: {fn: this.onExcelConflictHandlingForAll, scope:this,
							panel: conflictPanel, /*conflictResolutionList:conflictResolutionList,*/ conflicts:conflicts}});
				items.push(conflictResolutionRadioGroup);
				this.workItemIDFieldIDs = [];
				Ext.Array.forEach(conflicts, function(conflict) {
					var row = conflict['row'];
					var fields = conflict['fields'];
					Ext.Array.forEach(fields, function(field, index) {
						if (index===0) {
							items.push({xtype: 'label',
								html: row,
								rowspan: fields.length
								});
							items.push({xtype: 'label',
								html: field['workItemID'],
								rowspan: fields.length
								});
						}
						items.push({xtype: 'label',
							html: field['columnLetter']
							});
						items.push({xtype: 'label',
							html: field['fieldName']
							});
						items.push({xtype: 'label',
							html: field['excelValue']
							});
						items.push({xtype: 'label',
							html: field['trackplusValue']
							});
						var workItemIDFieldID = field['workItemIDFieldID'];
						this.workItemIDFieldIDs.push(workItemIDFieldID);
						var conflictResolutionRadioButtons = CWHF.getRadioButtonItems(conflictResolutionList,
								'overwriteMap['+workItemIDFieldID + "]", 'id', 'label', false, false, true);
						var conflictResolutionRadioGroup = CWHF.getRadioGroup('', 300, conflictResolutionRadioButtons,{itemId:'conflictResoultion'+workItemIDFieldID});
						items.push(conflictResolutionRadioGroup);
					}, this);
				}, this);
				conflictPanel.add(items);
			}
			break;
		}
	},

	/*confirmDialogForImportNotFoundItemsAsNew: function() {
		var me = this;
		Ext.MessageBox.show({
	        title : getText('common.warning'),
	        msg : getText('admin.actions.importExcel.err.importNotFoundWorkItems'),
	        buttons : Ext.MessageBox.YESNO,
	        fn : function(btn) {
	            if (btn === "yes") {
					Ext.Ajax.request({
				        url : "excelImport!removeIssueNoFromMapping.action",
				        disableCaching : true,
				        success : function() {
				        	me.onNavigate(me.importButton, me.wizardPanel);
				        },
				        failure : function() {
				        },
				        method : 'POST',
				        params : {fileName:me.fileName}
				    });

	            }
	            if (btn === "no") {
					 me.win.close();
				 }
	        },
	        icon : Ext.MessageBox.QUESTION
	    });
	},*/

	onExcelConflictHandlingForAll: function(field, newValue, oldValue, options) {
		var panel = options["panel"];
		var conflictResoultionRadioGroup = panel.getComponent("conflictResoultionRadioGroup");
		var conflicts = options["conflicts"];
		var conflictResolutionValue = CWHF.getSelectedRadioButtonValue(conflictResoultionRadioGroup);
		Ext.Array.forEach(conflicts, function(conflict) {
			var row = conflict['row'];
			var fields = conflict['fields'];
			Ext.Array.forEach(fields, function(field, index) {
				var workItemIDFieldID = field['workItemIDFieldID'];
				var radioGroup = panel.getComponent('conflictResoultion'+workItemIDFieldID);
				//TODO setValue() does not work (ext js bug?).
				//radioGroup.setValue(conflictResolutionValue);
				//Once setValue fixed remove this cycle
				Ext.Array.forEach(radioGroup.items.items, function(item, index) {
					//radioGroup.items.items.each(function(item){
						//item.setValue(item.inputValue === conflictResolutionValue);
					item.setRawValue(item.inputValue === conflictResolutionValue);
				});
			}, this)
		}, this)
	},

	/**
	 * Actualize the toolbar settings according to the current index
	 */
	actualizeToolbar: function(panel) {
		var me = this;
		if(me.win) {
		var layout = panel.getLayout();
		var activeItem = layout.getActiveItem();
		var index = panel.items.indexOf(activeItem);
		var numItems = panel.items.getCount();

		var toolbar = me.win.getDockedItems('toolbar[dock="bottom"]');
		var previous = toolbar[0].getComponent('previous');
		if (previous) {
			previous.setDisabled(!layout.getPrev());
		}
		var next = toolbar[0].getComponent('next');
		if (next) {
			next.setDisabled((index+1)>=(numItems-1));
		}
		var finish = toolbar[0].getComponent('finish');
		if (finish) {
			finish.setDisabled((index+1)!==(numItems-1));
		}
//		var indicator = toolbar[0].getComponent('indicator');
//		if (indicator) {
//			var indicatorText = getText("common.lbl.wizard.step", (index + 1), numItems);
//			panel.setTitle(this.getTitle(index + 1));
//			indicator.update(indicatorText);
//		}

		if(me.win) {
			me.win.setTitle(this.getTitle(index + 1));
		}
		}
	},


	getWizardPanel: function() {
		var me = this;
		var toolbarItems = [];
		this.wizardPanel = Ext.create('Ext.panel.Panel', {
			title: '',
			layout: 'card',
			region: 'left',
			//margin: '2 0 0 -10',
			cls: 'importWizard',
//			bodyStyle: 'padding:15px; border-left:none',
			//bodyStyle: 'border-left:none',
			defaults: {
				// applied to each contained panel
				border: false
			},
			// the panels (or "cards") within the layout
			items: [this.createEmptyCard("card1", true)]

		});
		this.initCards(2);
		this.loadDataForCard(1);
		return this.wizardPanel;
	},

	getDialogButtons: function() {
		var me = this;
		var toolbarItems = [];
		var numberOfCards = this.getNumberOfCards();
		if (numberOfCards>1) {
			toolbarItems.push({
				itemId: 'previous',
				text:'&laquo; '+ getText('common.lbl.wizard.previous'),
				scope: this,
				handler: function(btn) {
					this.onNavigate(btn, me.wizardPanel);
				},
				//at the beginning disabled
				disabled: true
			});
			toolbarItems.push('-');
			toolbarItems.push({
				itemId: 'next',
				text: getText('common.lbl.wizard.next')+' &raquo;',
				scope: this,
				handler: function(btn) {
					this.onNavigate(btn, me.wizardPanel);
				}
			});
			toolbarItems.push('-');
			me.importButton = {
					itemId: 'finish',
					iconCls: 'import16',
					text: getText('common.btn.import'),
					scope: this,
					handler: function(btn) {
						this.onNavigate(btn, me.wizardPanel);
					},
					disabled: true};
			toolbarItems.push(me.importButton);

			toolbarItems.push({
				text: getText('common.btn.done'),
				scope: this,
				handler: function(){
					me.doneHandler(me);
				}
			});
		} else {
			//for direct import no navigation is needed
			toolbarItems.push({	itemId: 'finish',
				text: getText('common.btn.import'),
				scope: this,
				handler: function(btn) {
					this.loadDataForCard(1, true);
				},
				disabled: false
			});
		}
		return toolbarItems;

	},

	/**
	 * Handler for closing the window
	 */
	doneHandler:function(scope) {
		if (this.itemNavigatorController) {
			this.itemNavigatorController.refresh.call(this.itemNavigatorController);
		}
		this.win.close();
	},

	/**
	 * This function initialize necessary components for showing pop
	 * up dialog
	 */
	createPopUpDialog: function(files, itemNavigatorController) {
		var me = this;
		this.itemNavigatorController = itemNavigatorController;
		if(CWHF.isNull(files)) {
			var windowParameters = {
				title: "",
				width: me.dialogWidth,
				height: me.dialogHeight,
				overrideButtons: me.getDialogButtons(),
				items: me.getWizardPanel()
			};
			var window = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
			window.showWindowByConfig(me);
		}else {
			var file = null;
			if(files  && files.length > 0){
				file = files[0];
			}

			var continueToUpload = true;
			if (!me.validateFileExtension(file.name)) {
				Ext.MessageBox.alert(getText('admin.actions.importTp.lbl.uploadFileWrongType'),
					getText('common.err.fileExpectedType', me.importExcelWizard.getFileTypeLabel()));
				continueToUpload = false;
			}

			if (me.uploadDone) {
				Ext.MessageBox.alert(getText('admin.actions.importTp.err.uploadAgain'),
						getText('admin.actions.importTp.err.uploadAgain', this.getFileTypeLabel()));
				continueToUpload = false;
			}
			if(continueToUpload) {
				me.uploadDone = true;
				var formData = new FormData();
				formData.append('uploadFile', file);
				formData.append('uploadFileFileName', file.name);
				borderLayout.setLoading(true);
				me.uploadExcelFile(formData);
			}
		}
	},

	/**
	 * The following method uploads dropped excel file, and
	 * opens excel import wizard pop up dialog
	 */
	uploadExcelFile: function(formData) {
		var me = this;
		var xhr = new XMLHttpRequest();
		var urlStr='excelUpload.action';
		xhr.open('POST', urlStr);
		xhr.onload = function () {
			if (xhr.status === 200) {
				borderLayout.setLoading(false);
				var windowParameters = {
					title: "",
					width: me.dialogWidth,
					height: me.dialogHeight,
					overrideButtons: me.getDialogButtons(),
					items: me.getWizardPanel()
				};
				var window = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
				var panelFrom = me.wizardPanel.getComponent("card" + 1);
				var panelTo = me.wizardPanel.getComponent("card" + 2);
				panelTo.removeAll(true);
				var panel = me.getImportWizardItemsForCard(2);
				panelTo.add(panel);
				var decodedString = Ext.decode(xhr.response);
				var jsonResult = {};
				jsonResult.data = decodedString.data;
				me.postDataProcess(panelTo, jsonResult, 2, 1);
				me.uploadDone = true;
				me.wizardPanel.layout.setActiveItem(1);

				window.showWindowByConfig(me);
				me.actualizeToolbar(me.wizardPanel);
			} else {
				alert('Something went terribly wrong...');
			}
		};
		xhr.onerror=function(e){
			borderLayout.setLoading(false);
			alert("Error uploading!" + e);
		};
		xhr.upload.onprogress = function (event) {
		};
		xhr.onerror=function(e){
			borderLayout.setLoading(false);
			alert("Error: " + e);
		}
		xhr.send(formData);
	}

});
