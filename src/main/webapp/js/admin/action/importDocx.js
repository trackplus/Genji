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

Ext.define('com.trackplus.admin.action.ImportDocx', {
	extend : 'com.trackplus.admin.action.ImportWizard',
	config : {},
	// selectedSheet: null,
	// workItemIDFieldIDs:null,
	fileName : null,
	addTo : null,
	workItemID : null,
	parentID : null,
	projectOrReleaseID : null,

	getNumberOfCards : function() {
	    return 4;
	},

	getTitle : function(cardNo) {
	    var titleKey = null;
	    switch (cardNo) {
	    case 1:
		    titleKey = "admin.actions.importDocx.title.upload";
		    break;
	    case 2:
		    titleKey = "admin.actions.importDocx.title.addTo";
		    break;
	    case 3:
		    titleKey = "admin.actions.importDocx.title.preview";
		    break;
	    case 4:
		    titleKey = "admin.actions.importDocx.title.result";
		    break;
		default:
			titleKey = '';
	    }

	    if (titleKey != null) {
		    return getText(titleKey);
	    } else {
		    return '';
	    }
	},

	getFilePattern : function(fileName) {
	    return /^.*\.(docx|DOCX)$/;
	},

	getFileTypeLabel : function() {
	    return getText('admin.actions.importDocx.lbl.uploadFile');
	},

	getFileEmptyText : function() {
	    return getText("admin.actions.importDocx.lbl.fileEmptyText");
	},

	getImportWizardUrl : function(card, reload) {
	    if (card == 1) {
		    return 'docxlUploadRender.action';
	    } else {
		    if (card == 2) {
			    return "docxUpload.action";
		    } else {
			    if (card == 3) {
				    // return 'docxAddTo.action';
				    return "docxPreview.action";
			    } else {
				    if (card == 4) {
					    return "docxImport.action";
				    }
			    }
		    }
	    }
	},

	/**
	 * Load/submit the data for on the cardNo cardNo: the card number to go
	 * next, or the actual card number if reload reload: whether to reload the
	 * current card or submit to the current values and go to the next card
	 * return false and optionally show an alert box if there is a validation
	 * problem which prevents navigating to the next card
	 */
	loadDataForCard : function(cardNo, reload, params) {
	    if (cardNo == 1) {
		    // for the very first card nothing to submit only render (load)
		    var panel1 = this.wizardPanel.getComponent('card1');
		    panel1.add(this.getImportWizardItemsForCard(cardNo));
		    panel1.getForm().load({
		        url : this.getImportWizardUrl(cardNo, reload),
		        scope : this,
		        success : function(form, action) {
			        try {
				        this.postDataProcess(panel1, action.result.data, 1);
				        // if data is valid we can go to the next card
			        } catch (ex) {
			        }
		        },
		        failure : function(form, action) {
			        Ext.MessageBox.alert(this.failureTitle, action.response.responseText)
		        }
		    });
	    } else {
		    // from card to each card starts with a submit
		    if (cardNo == 2) {
			    var panel1 = this.wizardPanel.getComponent('card1');
			    if (!panel1.getForm().isValid()) {
				    Ext.MessageBox.show({
				        title : '',
				        msg : getText('admin.actions.importTp.lbl.uploadFileNotSpecified'),
				        buttons : Ext.Msg.OK,
				        icon : Ext.MessageBox.ERROR
				    });
				    return false;
			    }
			    var importFile = panel1.getComponent('uploadFile');
			    if (!this.validateFileExtension(importFile.getRawValue())) {
				    Ext.MessageBox.alert(getText('admin.actions.importDocx.err.uploadFileWrongType'), getText(
				            'common.err.fileExpectedType', this.getFileTypeLabel()));
				    return false;
			    }
			    if (this.uploadDone) {
				    Ext.MessageBox.alert(getText('admin.actions.importTp.err.uploadAgain'), getText(
				            'admin.actions.importTp.err.uploadAgain', this.getFileTypeLabel()));
				    return false;
			    }
			    this.uploadDone = true;
			    var params = {
				    fileName : this.fileName
			    };
			    this.submitFromCardToCard(1, 2, params);
			    // this.submitFromCardToCardMessageOnFailure(1, 2, null, reload,
				// true);
		    } else {
			    if (cardNo == 3) {
				    var params = {
				        fileName : this.fileName,
				        workItemID : this.workItemID,
				        parentID : this.parentID,
				        projectOrReleaseID : this.projectOrReleaseID
				    };
				    this.submitFromCardToCard(2, 3, params, false, 300);
			    } else {
				    if (cardNo == 4) {
					    if (params == null) {
						    params = {
							    fileName : this.fileName
						    };
					    }
					    this.submitFromCardToCard(3, 4, params, false, 300);
				    }
			    }
		    }
	    }
	},

	/**
	 * Get the initial items for a card Additional items can be added once the
	 * data is back from the server
	 */
	getImportWizardItemsForCard : function(card) {
	    if (card == 1) {
		    return this.getImportWizardUploadItems();
	    } else {
		    if (card == 2) {
			    return this.getImportWizardAddToItems();
		    } else {
			    if (card == 3) {
				    return this.createPreviewPanel();
				    // return this.getExcelInvalidHandlingItems();
			    } else {
				    if (card == 4) {
					    return this.getExcelErrorAndConflictHandlingItems();
				    }
			    }
		    }
	    }
	},

	postDataProcess : function(panel, jsonResult, cardTo, cardFrom) {
	    if (cardTo == 1) {
		    this.postProcessUpload(panel, jsonResult);
	    } else {
		    if (cardTo == 2) {
			    this.postProcessAddTo(panel, jsonResult);
		    } else {
			    if (cardTo == 3) {
				    this.postProcessPreview(panel, jsonResult);
			    } else {
				    if (cardTo == 4) {
					    this.postProcessInvalidHandling(panel, jsonResult, cardTo, cardFrom);
					    // this.postProcessExcelErrorAndConflictHandling(panel,
						// jsonResult);
				    }
			    }
		    }
	    }
	},

	/**
	 * ************************Import
	 * source*************************************
	 */

	getImportWizardUploadItems : function() {
	    var card1Items = [ CWHF.createFileField(getText("common.lbl.file", this.getFileTypeLabel()), "uploadFile", {
	        allowBlank : false,
	        labelWidth : 150,
	        width : 500,
	        labelIsLocalized : true
	    }, {
		    change : {
		        fn : function() {
			        this.uploadDone = false;
		        },
		        scope : this
		    }
	    }) ];
	    card1Items.push({
	        xtype : 'label',
	        itemId : 'importSourceError'
	    });
	    return card1Items;
	},

	postProcessUpload : function(formPanel, data) {
	    var uploadFile = formPanel.getComponent("uploadFile");
	    uploadFile.setRawValue('');
	    this.initCards(2);
	},

	getImportWizardAddToItems : function() {
	    if (this.workItemID == null) {
		    var options = [ {
		        id : 1,
		        label : "admin.actions.importDocx.lbl.projectRelease"
		    }, {
		        id : 2,
		        label : "admin.actions.importDocx.lbl.parent"
		    } ];
		    var optionList = CWHF.getRadioButtonItems(options, "addTo", 'id', 'label', 1, false, false);
		    var addToRadioGroup = CWHF.getRadioGroup("addToType", "admin.actions.importDocx.lbl.addTo", 350,
		            optionList, null,
		            /*
					 * { labelWidth: this.labelWidth, labelAlign:
					 * this.labelAlign, labelStyle: this.labelStyle},
					 */
		            {
			            change : {
			                fn : this.addToTypeChanged,
			                scope : this
			            }
		            });
		    // project/release picker
		    var releasePickerConfig = {
		        activeFlag : true,
		        inactiveFlag : true,
		        notPlannedFlag : true,
		        closedFlag : false,
		        projectIsSelectable : true,
		        labelWidth : 150,
		        width : 400
		    };
		    var releasePicker = CWHF.createSingleTreePicker("admin.actions.importDocx.lbl.projectRelease",
		            "projectOrReleaseID", null, null, releasePickerConfig);
		    var parentHidden = CWHF.createHiddenField("parentID");
		    var parentIssueNo = CWHF.createTextField(null, "txtIssueNo", {
		        disabled : true,
		        readOnly : true,
		        width : 50
		    });
		    var parentIssueTitle = CWHF.createTextField(null, "txtIssueTitle", {
		        disabled : true,
		        readOnly : true,
		        margin : '0 5 0 5',
		        width : 300
		    });
		    var selectParent = {
		        xtype : 'button',
		        text : getText('common.btn.search'),
		        itemId : "searchParent",
		        disabled : true,
		        scope : this,
		        handler : function() {
			        this.chooseParent.call(this);
		        }
		    };

		    var parentPanel = Ext.create('Ext.form.FieldContainer', {
		        combineErrors : true,
		        itemId : 'parentPanel',
		        fieldLabel : getText('admin.actions.importDocx.lbl.parent'),
		        labelWidth : 150,
		        labelAlign : "right",
		        labelStyle : {
			        overflow : 'hidden'
		        },
		        layout : 'hbox',
		        items : [ parentHidden, parentIssueNo, parentIssueTitle, selectParent ]
		    });
		    return [ addToRadioGroup, releasePicker, parentPanel ];
	    } else {
		    // var workItemHidden = CWHF.createHiddenField("workItemID");
		    var issueNo = CWHF.createTextField(null, "txtIssueNo", {// disabled:this.jsonData.disabled,
		        readOnly : true,
		        width : 50
		    });
		    var issueTitle = CWHF.createTextField(null, "txtIssueTitle", {// disabled:
																			// this.jsonData.disabled,
		        readOnly : true,
		        margin : '0 5 0 5',
		        width : 300
		    });
		    return [ issueNo, issueTitle ];
	    }

	},

	chooseParent : function() {
	    var issuePicker = Ext.create('com.trackplus.util.IssuePicker', {
	        title : getText("common.btn.chooseParent"),
	        handler : this.setParent,
	        scope : this
	    });
	    issuePicker.showDialog();
	},

	setParent : function(item) {
	    var formPanel = this.wizardPanel.getComponent("card2");
	    var parentPanel = formPanel.getComponent("parentPanel");
	    this.parentID = item["objectID"];
	    var txtIssueNo = parentPanel.getComponent("txtIssueNo");
	    if (txtIssueNo != null) {
		    txtIssueNo.setValue(item["id"]);
	    }
	    var txtIssueTitle = parentPanel.getComponent("txtIssueTitle");
	    if (txtIssueTitle != null) {
		    txtIssueTitle.setValue(item["title"])
	    }
	},

	addToTypeChanged : function(radioGroup, newValue, oldValue, options) {
	    var formPanel = radioGroup.ownerCt;
	    var checkedArr = radioGroup.getChecked();
	    if (checkedArr.length == 1) {
		    var checkedRadio = checkedArr[0];
		    var value = checkedRadio.getSubmitValue();
		    // project=1, parent=2
		    var projectReleasesPicker = formPanel.getComponent("projectOrReleaseID");
		    projectReleasesPicker.setDisabled(value != 1);
		    var parentPanel = formPanel.getComponent("parentPanel");
		    var searchParent = parentPanel.getComponent("searchParent");
		    searchParent.setDisabled(value == 1);
	    }
	},

	/**
	 * Post process add to
	 */
	postProcessAddTo : function(formPanel, data) {
	    this.workItemID = data["workItemID"];
	    this.fileName = data["fileName"];
	    if (this.workItemID == null) {
		    var projectReleasesPicker = formPanel.getComponent("projectOrReleaseID");
		    projectReleasesPicker.updateData(data["projectReleaseTree"]);
		    // projectReleasesPicker.setValue(data["selectedProjectReleaseID"]);
	    } else {
		    var issueNo = formPanel.getComponent("txtIssueNo");
		    issueNo.setValue(data["itemID"]);
		    var issueTitle = formPanel.getComponent("txtIssueTitle");
		    issueTitle.setValue(data["itemTitle"]);
	    }
	    this.initCards(3);
	},

	/**
	 * ************************Excel column - Genji field
	 * mappings*************************************
	 */

	createPreviewPanel : function() {
	    var store = Ext.create('Ext.data.TreeStore', {
	        /*
			 * proxy:{ type: 'ajax', url: 'browseProjects!expandNode.action' },
			 */
	        fields : [ 'id', 'text', 'type', 'canEdit', 'icon', 'leaf' ],
	        root : {
	            expanded : true,
	            children : []
	        }
	    });
	    return Ext.create('Ext.tree.Panel', {
	        region : 'center',
	        itemId : 'itemTree',
	        useArrows : true,
	        autoScroll : true,
	        store : store,
	        rootVisible : false,
	        border : false,
	        margins : '0 0 0 0',
	        baseCls : 'x-plain',
	        bodyStyle : {
		        border : 'none'
	        },
	        cls : 'westTreeNavigator'
	    });
	},

	/**
	 * Add field mappings data for rendering after the result has arrived
	 */
	postProcessPreview : function(panel, jsonResult) {
	    var itemTree = panel.getComponent("itemTree");
	    var rootNode = itemTree.store.getRootNode();
	    rootNode.removeAll(true);
	    var data = jsonResult.data;
	    if (data != null) {
		    rootNode.appendChild(data.itemTree);
	    }
	},

	/**
	 * ************************Excel invalid value
	 * handling*************************************
	 */

	/**
	 * Gets the items for excel invalid value handling, before the data has
	 * arrived
	 */
	getExcelInvalidHandlingItems : function() {
	    var panelConfig = {
	        itemId : 'invalidValueHandlingPanel',
	        layout : {
	            type : 'table',
	            columns : 3,
	            tdAttrs : {
		            style : {
			            padding : '5px 15px 0 0'
		            }
	            }
	        },
	        bodyStyle : 'padding: 5px',
	        border : false,
	        method : "POST",
	        autoScroll : true,
	        items : []
	    };
	    return [ {
	        xtype : 'component',
	        cls : "infoBox_bottomBorder",
	        border : true,
	        anchor : '100%',
	        margin : "0 0 0 0",
	        html : getText("admin.actions.importExcel.invalidValueHadling.message")
	    }, Ext.create('Ext.form.Panel', panelConfig) ];
	},

	postProcessInvalidHandling : function(panel, jsonResult, cardTo, cardFrom) {
	    if (cardFrom == 2) {
		    this.postProcessInvalidHandlingFirstTime(panel, jsonResult)
	    } else {
		    this.postProcessInvalidHandlingUpdate(panel, jsonResult);
	    }
	},

	/**
	 * Add invalid value handling data for rendering after the result has
	 * arrived
	 */
	postProcessInvalidHandlingFirstTime : function(panel, jsonResult, cardTo, cardFrom) {
	    var data = jsonResult.data;
	    var fieldList = data['fieldList'];
	    var possibleValues = data['possibleValues'];
	    var invalidValueHandlingList = data['invalidValueHandlingList'];
	    if (panel != null) {
		    var invalidValueHandlingPanel = panel.getComponent("invalidValueHandlingPanel");
		    items = [];
		    items.push({
		        xtype : 'label',
		        html : '<b>' + getText('admin.actions.importExcel.invalidValueHadling.lbl.fieldName') + '</b>',
		        width : 150
		    });
		    items.push({
		        xtype : 'label',
		        html : '<b>' + getText('admin.actions.importExcel.invalidValueHadling.lbl.defaultfieldValue') + '</b>',
		        colspan : 2
		    });
		    if (fieldList != null) {
			    Ext.Array.forEach(fieldList, function(fieldBean) {
				    var fieldId = fieldBean.id;
				    var possibleFieldValues = possibleValues['fieldID' + fieldId];
				    var invalidValueHandlingValue = possibleFieldValues['invalidValueHandlingValue'];
				    var possibleValueList = possibleFieldValues['possibleFieldValues'];
				    var defaultFieldValue = possibleFieldValues['defaultFieldValue'];
				    items.push({
				        xtype : 'label',
				        html : fieldBean.label,
				        width : 150,
				        padding : '10 0 0 0'
				    });
				    var invalidValueHandlingRadioButtons = CWHF.getRadioButtonItems(invalidValueHandlingList,
				            'invalidValueHandlingMap[' + fieldId + ']', 'id', 'label', invalidValueHandlingValue,
				            false, true);
				    var invalidValueHandlingRadioGroup = CWHF.getRadioGroup('invalidValueHandling' + fieldId, '', 250,
				            invalidValueHandlingRadioButtons, null, {
					            change : {
					                fn : this.onInvalidHandlingChange,
					                scope : this,
					                panel : invalidValueHandlingPanel,
					                fieldId : fieldId
					            }
				            });
				    items.push(invalidValueHandlingRadioGroup);
				    var control;
				    if (fieldId == 1) {
					    control = CWHF.createSingleTreePicker(null, "defaultValuesMap[" + fieldId + "]",
					            possibleValueList, defaultFieldValue, {
					                allowBlank : false,
					                disabled : invalidValueHandlingValue == 2,
					                itemId : 'defaultValues' + fieldId
					            }, {
						            select : {
						                fn : this.onProjectSelect,
						                scope : this,
						                fieldId : fieldId
						            }
					            });
				    } else {
					    control = CWHF.createCombo(null, 'defaultValuesMap[' + fieldId + ']', {
					        disabled : invalidValueHandlingValue == 2,
					        width : 200,
					        maxWidth : 250,
					        data : possibleValueList,
					        value : defaultFieldValue
					    }, {
						    select : {
						        fn : this.onSelectDefaultValue,
						        scope : this,
						        fieldId : fieldId
						    }
					    }, 'defaultValues' + fieldId);
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
	 *
	 * @param projectPicker
	 * @param selectedProject
	 * @param options
	 */
	onProjectSelect : function(projectPicker, selectedProject, options) {
	    this.onRefreshInvalidHandling(options['fieldId']);
	},

	/**
	 * Handler for changing a default value
	 */
	onSelectDefaultValue : function(combo, records, options) {
	    this.onRefreshInvalidHandling(options['fieldId']);
	},
	/**
	 * Add invalid value handling data for rendering after the result has
	 * arrived
	 */
	postProcessInvalidHandlingUpdate : function(panel, jsonResult) {
	    var data = jsonResult.data;
	    var fieldList = data['fieldList'];
	    var possibleValues = data['possibleValues'];
	    if (panel != null) {
		    var invalidValueHandlingPanel = panel.getComponent("invalidValueHandlingPanel");
		    if (fieldList != null) {
			    Ext.Array.forEach(fieldList, function(fieldId) {
				    var possibleFieldValues = possibleValues['fieldID' + fieldId];
				    // var invalidValueHandlingValue =
					// possibleFieldValues['invalidValueHandlingValue'];
				    var possibleValueList = possibleFieldValues['possibleFieldValues'];
				    var defaultFieldValue = possibleFieldValues['defaultFieldValue'];
				    if (fieldId != 1) {
					    control = invalidValueHandlingPanel.getComponent('defaultValues' + fieldId);
					    if (control != null) {
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
	onInvalidHandlingChange : function(field, newValue, oldValue, options) {
	    var fieldId = options['fieldId'];
	    var panel = options['panel'];
	    var invalidValueHandlingRadioGroup = panel.getComponent('invalidValueHandling' + fieldId);
	    var fieldSelect = panel.getComponent('defaultValues' + fieldId);
	    var checkedArr = invalidValueHandlingRadioGroup.getChecked();
	    var checkedRadioValue = null;
	    if (checkedArr.length > 0) {
		    checkedRadio = checkedArr[0];
		    if (checkedRadio != null) {
			    checkedRadioValue = checkedRadio.getSubmitValue();
		    }
	    }
	    fieldSelect.setDisabled(checkedRadioValue == 2);
	    this.onRefreshInvalidHandling(fieldId);
	},

	onRefreshInvalidHandling : function(fieldId) {
	    if (fieldId == 1 || fieldId == 2) {
		    // reload is needed only for project or issueType change
		    // most of the other lists depend on project and issueType
		    this.loadDataForCard(3, true);
	    }
	},

	/**
	 * ****************************************Excel
	 * conflicts****************************************************
	 */

	/**
	 * Gets the items for excel import, before the data has arrived
	 */
	getExcelErrorAndConflictHandlingItems : function() {
	    var panelConfig = {
	        itemId : 'errorAndConflict',
	        bodyStyle : 'padding: 5px',
	        border : false,
	        method : "POST",
	        // autoScroll: true,
	        items : [ Ext.create('Ext.panel.Panel', {
	            itemId : 'errorPanel',
	            border : false,
	            defaults : {
	                labelStyle : 'overflow: hidden;',
	                margin : "0 5 0 0"
	            },
	            layout : {
	                type : 'table',
	                columns : 1
	            }
	        }), Ext.create('Ext.form.Panel', {
	            itemId : 'conflictPanel',
	            defaults : {
	                labelStyle : 'overflow: hidden;',
	                margin : "0 5 0 0"
	            },
	            layout : {
	                type : 'table',
	                columns : 7,
	                tdAttrs : {
		                style : {
		                    padding : '5px 15px 0 0',
		                    'vertical-align' : 'top'
		                }
	                }
	            // autoScroll:true
	            },
	            border : false
	        }) ]
	    };
	    return [ Ext.create('Ext.form.Panel', panelConfig) ];
	},

	/**
	 * Add error or conflict handling data for rendering after the result has
	 * arrived
	 */
	postProcessExcelErrorAndConflictHandling : function(panel, data) {
	    var disableFinal = data['disableFinal'];
	    var toolbar = this.wizardPanel.getDockedItems('toolbar[dock="top"]');
	    var finish = toolbar[0].getComponent('finish');
	    var previous = toolbar[0].getComponent('previous');
	    finish.setDisabled(disableFinal);
	    // previous.setDisabled(disableFinal);
	    var errorAndConflictPanel = panel.getComponent('errorAndConflict');
	    var errorCode = data['errorCode'];
	    if (errorCode == null) {
		    var message = data['message'];
		    if (message != null) {
			    var errorPanel = errorAndConflictPanel.getComponent('errorPanel');
			    errorPanel.add({
			        xtype : 'label',
			        html : '<b>' + message + '</b>'
			    });
		    }
		    return;
	    }
	    switch (errorCode) {
	    case 1:
		    // simple message
		    var errorPanel = errorAndConflictPanel.getComponent('errorPanel');
		    errorPanel.add({
		        xtype : 'label',
		        html : '<b>' + data['errorMessage'] + '</b>'
		    });
		    break;
	    case 2:
		    // more messages
		    var errorPanel = errorAndConflictPanel.getComponent('errorPanel');
		    var errorMessages = data['errorMessage'];
		    for (var i = 0; i < errorMessages.length; i++) {
			    errorPanel.add({
			        xtype : 'label',
			        html : errorMessages[i]
			    });
		    }
		    break;
	    case 3:
		    // grid and row errors
		    var errorPanel = errorAndConflictPanel.getComponent('errorPanel');
		    var gridErrors = data['gridErrors'];
		    if (gridErrors != null) {
			    for (var i = 0; i < gridErrors.length; i++) {
				    var gridError = gridErrors[i];
				    var errorMessage = gridError['errorMessage'];
				    var locationList = gridError['locationList'];
				    if (locationList != null) {
					    errorPanel.add({
					        xtype : 'label',
					        html : '<b>' + errorMessage + '</b>'
					    });
					    for (var j = 0; j < locationList.length; j++) {
						    errorPanel.add({
						        xtype : 'label',
						        html : locationList[j]
						    });
					    }
				    }
			    }
		    }
		    var rowErrors = data['rowErrors'];
		    if (rowErrors != null) {
			    for (var i = 0; i < rowErrors.length; i++) {
				    var rowError = rowErrors[i];
				    var errorMessage = rowError['errorMessage'];
				    var locationList = rowError['locationList'];
				    errorPanel.add({
				        xtype : 'label',
				        html : '<b>' + errorMessage + '</b>'
				    });
				    errorPanel.add({
				        xtype : 'label',
				        html : locationList
				    });
			    }
		    }
		    break;
	    case 4:
		    // field conflicts
		    var errorMessage = data['errorMessage'];
		    var conflicts = data['conflicts'];
		    if (conflicts != null) {
			    var conflictPanel = errorAndConflictPanel.getComponent('conflictPanel');
			    var conflictResolutionList = data['conflictResolutionList'];
			    items = [];
			    if (errorMessage != null) {
				    items.push({
				        xtype : 'label',
				        html : errorMessage,
				        colspan : 7
				    });
			    }
			    items.push({
			        xtype : 'label',
			        html : '<b>' + getText('admin.actions.importExcel.conflict.lbl.row') + '</b>'
			    });
			    items.push({
			        xtype : 'label',
			        html : '<b>' + data['issueNoLabel'] + '</b>'
			    });
			    items.push({
			        xtype : 'label',
			        html : '<b>' + getText('admin.actions.importExcel.conflict.lbl.column') + '</b>'
			    });
			    items.push({
			        xtype : 'label',
			        html : '<b>' + getText('admin.actions.importExcel.conflict.lbl.field') + '</b>'
			    });
			    items.push({
			        xtype : 'label',
			        html : '<b>' + getText('admin.actions.importExcel.conflict.lbl.excelValue') + '</b>'
			    });
			    items.push({
			        xtype : 'label',
			        html : '<b>' + getText('admin.actions.importExcel.conflict.lbl.trackplusValue') + '</b>'
			    });
			    var conflictResolutionListBold = [];
			    Ext.Array.forEach(conflictResolutionList, function(conflictResolution) {
				    conflictResolutionListBold.push({
				        id : conflictResolution["id"],
				        label : "<b>" + conflictResolution["label"] + "</b>"
				    });
			    }, this);
			    var conflictResolutionRadioButtons = CWHF.getRadioButtonItems(conflictResolutionListBold,
			            'conflictResoultionEntry', 'id', 'label', false, false, true);
			    var conflictResolutionRadioGroup = CWHF.getRadioGroup('conflictResoultionRadioGroup', '', 300,
			            conflictResolutionRadioButtons, null, {
				            change : {
				                fn : this.onExcelConflictHandlingForAll,
				                scope : this,
				                panel : conflictPanel, /* conflictResolutionList:conflictResolutionList, */
				                conflicts : conflicts
				            }
			            });
			    items.push(conflictResolutionRadioGroup);
			    this.workItemIDFieldIDs = [];
			    Ext.Array.forEach(conflicts, function(conflict) {
				    var row = conflict['row'];
				    var fields = conflict['fields'];
				    Ext.Array.forEach(fields, function(field, index) {
					    if (index == 0) {
						    items.push({
						        xtype : 'label',
						        html : row,
						        rowspan : fields.length
						    });
						    items.push({
						        xtype : 'label',
						        html : field['workItemID'],
						        rowspan : fields.length
						    });
					    }
					    items.push({
					        xtype : 'label',
					        html : field['columnLetter']
					    });
					    items.push({
					        xtype : 'label',
					        html : field['fieldName']
					    });
					    items.push({
					        xtype : 'label',
					        html : field['excelValue']
					    });
					    items.push({
					        xtype : 'label',
					        html : field['trackplusValue']
					    });
					    var workItemIDFieldID = field['workItemIDFieldID'];
					    this.workItemIDFieldIDs.push(workItemIDFieldID);
					    var conflictResolutionRadioButtons = CWHF.getRadioButtonItems(conflictResolutionList,
					            'overwriteMap[' + workItemIDFieldID + "]", 'id', 'label', false, false, true);
					    var conflictResolutionRadioGroup = CWHF.getRadioGroup('conflictResoultion' + workItemIDFieldID,
					            '', 300, conflictResolutionRadioButtons);
					    items.push(conflictResolutionRadioGroup);
				    }, this);
			    }, this);
			    conflictPanel.add(items);
		    }
		    break;
	    }
	},

	onExcelConflictHandlingForAll : function(field, newValue, oldValue, options) {
	    var panel = options["panel"];
	    var conflictResoultionRadioGroup = panel.getComponent("conflictResoultionRadioGroup");
	    var conflicts = options["conflicts"];
	    var conflictResolutionValue = CWHF.getSelectedRadioButtonValue(conflictResoultionRadioGroup);
	    Ext.Array.forEach(conflicts, function(conflict) {
		    var row = conflict['row'];
		    var fields = conflict['fields'];
		    Ext.Array.forEach(fields, function(field, index) {
			    var workItemIDFieldID = field['workItemIDFieldID'];
			    var radioGroup = panel.getComponent('conflictResoultion' + workItemIDFieldID);
			    // TODO setValue() does not work (ext js bug?).
			    // radioGroup.setValue(conflictResolutionValue);
			    // Once setValue fixed remove this cycle
			    radioGroup.items.each(function(item) {
				    item.setValue(item.inputValue == conflictResolutionValue);
			    });
		    }, this)
	    }, this)
	}
});
