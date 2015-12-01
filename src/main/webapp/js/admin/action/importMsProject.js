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

Ext.define('com.trackplus.admin.action.ImportMsProject',{
	extend:'com.trackplus.admin.action.ImportWizard',
	config: {
		projectID: null
	},
	conflictParameterNames:null,
	dialogHeight:470,
	dialogWidth:820,
	isFromDragAndDrop:false,

	getNumberOfCards: function() {
		return 3;
	},

	getTitle: function(cardNo) {
		var titleKey=null;
		switch (cardNo) {
		case 1:
			titleKey = "admin.actions.importMSProject.title.upload";
			break;
		case 2:
			titleKey = "admin.actions.importMSProject.title.resourceMappings";
			break;
		case 3:
			titleKey = "admin.actions.importMSProject.title.importResult";
			break;
		}
		if (titleKey) {
			return getText(titleKey);
		} else {
			return '';
		}

	},

	getFilePattern: function(importSource, fileName) {
		return /^.*\.(xml|XML|MPP|mpp|MPT|mpt)$/;
	},

	getFileTypeLabel: function() {
		return getText("admin.actions.importMSProject.lbl.uploadFile");
	},

	getImportWizardUrl: function(card, reload) {
		if (card===1) {
			return 'msProjectUploadRender.action';
		} else {
			if (card===2) {
				return 'msProjectUpload.action';
			} else {
				if (card===3) {
					return "msProjectImport!submitResourceMapping.action";
				} /*else {
	                if (card===4) {
	                    return "msProjectImport!doImport.action";
	                }
	            } */
			}
		}
	},

	/**
	 * Load/submit the data for on the cardNo
	 * cardNo: the card number to go next, or the actual card number if reload
	 * reload: whether to reload the current card or submit to the current values and go to the next card
	 * return false and optionally show an alert box if there is a validation problem which prevents navigating to the next card
	 */
	loadDataForCard: function(cardNo, reload, parameters) {
		var me = this;
		if (cardNo===1) {
			//for the very first card nothing to submit only render (load)
			var panel1 = this.wizardPanel.getComponent('card1');
			panel1.add(this.getImportWizardItemsForCard(cardNo));
			var params = null;
			if (this.projectID) {
				params = {projectOrReleaseID:-this.projectID};
			}

			panel1.getForm().load({
				url: this.getImportWizardUrl(cardNo, reload),
				scope: this,
				params: params,
				success : function(form, action) {
					try{
						this.postDataProcess(panel1, action.result.data, 1);
						//if data is valid we can go to the next card
					}catch(ex){}
				},
				failure: function(form, action) {
					Ext.MessageBox.alert(this.failureTitle, action.response.responseText);
				}
			});
			return true;
		} else {
			//from card to each card starts with a submit
			if (cardNo===2) {
				var panel1 = this.wizardPanel.getComponent('card1');
				if (!panel1.getForm().isValid()) {
					Ext.MessageBox.alert(getText('admin.actions.importTp.lbl.uploadFileNotSpecified'),
							getText('admin.actions.importTp.lbl.uploadFileNotSpecified'));
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
				var params = {fileName:this.fileName};
				//this.submitFromCardToCardMessageOnFailure(1, 2, params, reload, true);
	            this.submitFromCardToCard(1, 2, params, reload, true);
			} else {
				if (cardNo===3) {
					//var params = {fileName:this.fileName};
	                if (CWHF.isNull(parameters)) {
	                    parameters = new Object();
	                }
	                parameters["fileName"] = this.fileName;
					return this.submitFromCardToCard(2, 3, parameters, reload, 240);
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
			return this.getImportWizardCard1Items();
		} else {
			if (card===2) {
				return this.getMsProjectResourceMappingItems();
			} else {
				if (card===3) {
					return this.getMsProjectConflictHandlingItems();
				}
			}
		}
	},

	postDataProcess: function(panel, data, card) {
		if (card===1) {
			this.postProcessCard1(panel, data);
		} else {
			if (card===2) {
				if (data.data) {
					this.postProcessMsProjectResourceMapping(panel, data);
					this.actualizeToolbar(this.wizardPanel);
				} else {
					this.goNext();
					var panel3 = this.wizardPanel.getComponent("card" + 3);
					panel3.removeAll();
					panel3.add(this.getImportWizardItemsForCard(3));
					this.postProcessMsProjectConflictHandling(panel3, data);
					//this.actualizeToolbar(this.wizardPanel);

					//this.removeCard(2);
				}
			} else {
				if (card===3) {
					this.postProcessMsProjectConflictHandling(panel, data);
				}
			}
		}
	},

	/**************************Import source**************************************/

	getImportWizardCard1Items: function() {
		var me = this;
		var card1Items = [];
		if(!me.isFromDragAndDrop) {
			var card1Items = [CWHF.createFileField(
					getText("common.lbl.file", this.getFileTypeLabel()), "uploadFile",
					{margin: '5 0 0 0', allowBlank:false, labelWidth:250, width:700, labelIsLocalized: true, itemId:"uploadFile"},
					{change:{fn:function(){
						this.uploadDone=false;},
						scope:this}})];
		}
		card1Items.push({xtype: 'label',
			itemId: 'importSourceError'});
		card1Items.push(CWHF.createSingleTreePicker("admin.actions.importTp.lbl.projectRelease",
	        "projectOrReleaseID", [], null,
	        {allowBlank:false,
	            labelWidth:250,
	            width:500,
	            margin: '10 0 10 0',
	            itemId: 'projectOrReleaseID'
	        }));
		return card1Items;
	},

	/**
	 * Post process first card data
	 * Called after first loading the first card data and after each import source change
	 */
	postProcessCard1: function(formPanel, data) {
	    var projectReleasesPicker = formPanel.getComponent("projectOrReleaseID");
	    projectReleasesPicker.updateMyOptions(data["projectReleaseTree"]);
	    projectReleasesPicker.setValue(data["selectedProjectReleaseID"]);

		var uploadFile = formPanel.getComponent("uploadFile");
		uploadFile.setRawValue('');
		this.initCards(2);
	},

	/**************************MsProject resource - Genji user mappings**************************************/

	/**
	 * Gets the items for excel field mapping, before the data has arrived
	 */
	getMsProjectResourceMappingItems: function() {
		var resourcePanelConfig = {
				itemId: 'resourceMappings',
				layout: {
					type: 'table',
					columns: 4,
					style: {
						width: '100%'
					}
				},
				bodyStyle: 'padding: 5px',
				border: false,
				method: "POST",
				autoScroll:	false,
				items: []
			};
		var newUserErrorsPanelConfig = {
				itemId: 'newUserErrors',
				layout: {
					type: 'table',
					columns: 1,
					style: {
						width: '100%'
					}
				},
				bodyStyle:	'padding: 5px',
				border: false,
				method: "POST",
				autoScroll:	false,
				items: []
			};
		return [Ext.create('Ext.form.Panel', resourcePanelConfig),
				Ext.create('Ext.form.Panel', newUserErrorsPanelConfig)];
	},

	/**
	 * Add field mappings data for rendering after the result has arrived
	 */
	postProcessMsProjectResourceMapping: function(panel, jsonData) {
		var data = jsonData.data;
		this.fileName = data['fileName'];
		var resourceMappings = data['resourceMappings'];
		var trackplusPersons = data['trackplusPersons'];
		items = [];
		items.push({xtype: 'label',
			html: getText('admin.actions.importMSProject.lbl.resourceName')
			});
		items.push({xtype: 'label',
			html: getText('admin.actions.importMSProject.lbl.personName'),
			colspan: 3
			});
		if (panel) {
			if (resourceMappings) {
				Ext.Array.forEach(resourceMappings, function(resourceMapping) {
					var resourceName = resourceMapping['resourceName'];
					var resourceUID = resourceMapping['resourceUID'];
					var personID = resourceMapping['personID'];
					items.push({xtype: 'label',
						 html: resourceName,
						 width: 200
						});
					items.push(CWHF.createCombo(null,
							'resourceUIDToPersonIDMap[' + resourceUID + ']',
							{width:150,
							data:trackplusPersons,
							value:personID,
							itemId: 'resourceUIDToPersonID' + resourceUID},
							{select: {fn: this.showHide, scope:this,
								panel: panel,
								resourceUID: resourceUID}},
								'resourceUIDToPersonID'+resourceUID));
					var textFieldUsername = CWHF.createTextField('admin.actions.importMSProject.lbl.userName',
							'resourceUIDToUsernameMap['+resourceUID +']',
							{itemId:'resourceUIDToUsername'+resourceUID,
							allowBlank:false,
							width:250,
							labelWidth: 100});
					textFieldUsername.setDisabled(personID!==0);
					items.push(textFieldUsername);
					var textFieldEmail = CWHF.createTextField('admin.actions.importMSProject.lbl.email',
							'resourceUIDToEmailMap['+resourceUID +']',
							{itemId:'resourceUIDToEmail'+resourceUID,
							allowBlank:false,
							width: 250,
							labelWidth: 75});
					textFieldEmail.setDisabled(personID!==0);
					items.push(textFieldEmail);
				}, this);
			}
		}
		var mappingsPanel = panel.getComponent('resourceMappings');
		mappingsPanel.removeAll(true);
		//add all panels at once
		mappingsPanel.add(items);
		var newUserErrorsPanel = panel.getComponent('newUserErrors');
		newUserErrorsPanel.removeAll(true);
		//add all panels at once
		var errors = data['errorMessage'];
		if (errors) {
			newUserErrorsPanel.add(errors);
		}
	},

	/**
	 * Set/reset disable automatically the identifier checkbox by selecting a field
	 */
	showHide: function(combo, records, options) {
		var panel = options.panel;
		var resourceUID = options.resourceUID;
		var mappingPanel = panel.getComponent('resourceMappings');
		if (mappingPanel) {
			var resourceUIDToPersonID = mappingPanel.getComponent("resourceUIDToPersonID" + resourceUID);
			var resourceUIDToUsername = mappingPanel.getComponent("resourceUIDToUsername" + resourceUID);
			var resourceUIDToEmail = mappingPanel.getComponent("resourceUIDToEmail" + resourceUID);
			var selectedValue = resourceUIDToPersonID.getValue();
			var showUsernameAndEmail = selectedValue!==0;
			resourceUIDToUsername.setDisabled(showUsernameAndEmail);
			resourceUIDToEmail.setDisabled(showUsernameAndEmail);
		}
	},

	/******************************************Ms project conflicts*****************************************************/
	/**
	 * Gets the items for ms project import, before the data has arrived
	 */
	getMsProjectConflictHandlingItems: function() {
		var panelConfig = {
				itemId: 'errorAndConflict',
				bodyStyle:	'padding: 5px',
				border: false,
				method: "POST",
				autoScroll:	false,
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
							columns:6
						},
						border:false})
					]};
		return [Ext.create('Ext.form.Panel', panelConfig)];
	},

	/**
	 * Add error or conflict handling data for rendering after the result has arrived
	 */
	postProcessMsProjectConflictHandling: function(panel, data) {
		var me = this;
		var errorCode = data['errorCode'];
		var disableFinal = data['disableFinal'];
		var toolbar =  me.win.getDockedItems('toolbar[dock="bottom"]');

		var finish = toolbar[0].getComponent('finish');
		finish.setDisabled(disableFinal);
		var errorAndConflictPanel = panel.getComponent('errorAndConflict');
		var errorCode = data['errorCode'];
		if (CWHF.isNull(errorCode)) {
			var message = data['message'];
			if (message) {
				var errorPanel = errorAndConflictPanel.getComponent('errorPanel');
				errorPanel.add({xtype: 'label',
					 html: message
					});
				finish.setText(getText('common.lbl.wizard.finish'));
				finish.setDisabled(false);
				finish.setIconCls('');
				finish.handler = function() {me.win.close();};
				var toolbar = me.win.getDockedItems('toolbar[dock="bottom"]');
				var previous = toolbar[0].getComponent('previous');
				if (previous) {
					previous.setDisabled(true);
				}
			} else {
	            var statusText = data["statusText"];
	            if (statusText) {
	                //from submitFromCardToCard -> failure branch: for ex. connection timeout: import took too long
	                var errorPanel = errorAndConflictPanel.getComponent('errorPanel');
	                errorPanel.add({xtype: 'label',
	                    html: statusText
	                });
	            }
	        }
			return;
		}
		var conflictPanel = panel.getComponent('conflictPanel');
		switch(errorCode) {
		case 2:
			//error messages
			var errorPanel = errorAndConflictPanel.getComponent('errorPanel');
			var errorMessages = data['errorMessage'];
			for ( var i = 0; i < errorMessages.length; i++) {
				errorPanel.add({xtype: 'label',
					html: errorMessages[i]
					});
			}
			break;
		case 4:
	        this.conflictParameterNames = [];
			var errorMessage = data['errorMessage'];
			//conflicts
			var conflictPanel = errorAndConflictPanel.getComponent('conflictPanel');
			var leaveOverwriteList = data['leaveOverwriteList'];
	        var leaveDeleteList = data['leaveDeleteList'];
	        var leaveUndeleteList = data['leaveUndeleteList'];
	        conflictPanel.add({xtype: 'label',
	            html: errorMessage,
	            colspan: 6
	        });
			var conflicts = data['conflicts'];
			if (conflicts) {
				Ext.Array.forEach(conflicts, function(conflict) {
					var conflictMessage =  conflict['conflictMessage'];
	                var conflictType =  conflict['conflictType'];
	                var conflictHandlingList = null;
	                var titleColspan = 1;
	                var withValues = true;
	                switch(conflictType) {
	                    case 1:  //BUDGET;
	                    case 2: //STARTDATE
	                    case 3: //ENDDATE
	                        conflictHandlingList = data['leaveOverwriteList'];
	                        break;
	                    case 4: //DELETED
	                        conflictHandlingList = data['leaveDeleteList'];
	                        titleColspan = 3;
	                        withValues = false;
	                        break;
	                    case 5: //UNDELETED
	                        conflictHandlingList = data['leaveUndeleteList'];
	                        withValues = false;
	                        titleColspan = 3;
	                        break;
	                }
					var conflictMapName =  conflict['conflictMapName'];
	                conflictPanel.add({xtype: 'label',
	                    html: conflictMessage,
	                    colspan: 6
	                });

	                var items = [];
	                items.push({xtype: 'label',
	                    html: '<b>'+"UID"+'</b>'
	                });
	                items.push({xtype: 'label',
	                    html: '<b>'+data['issueNoLabel']+'</b>'
	                });
	                items.push({xtype: 'label',
	                    html: '<b>'+getText('common.lbl.name')+'</b>',
	                    colspan: titleColspan
	                });
	                if (withValues) {
	                    items.push({xtype: 'label',
	                        html: '<b>'+getText('admin.actions.importMSProject.conflict.lbl.msProjectValue')+'</b>'
	                    });
	                    items.push({xtype: 'label',
	                        html: '<b>'+getText('admin.actions.importMSProject.conflict.lbl.trackplusValue')+'</b>'
	                    });
	                }
	                var conflictResolutionRadioButtons = CWHF.getRadioButtonItems(conflictHandlingList,
	                    conflictMapName, 'id', 'label', false, false, true);
	                var conflictResolutionRadioGroup = CWHF.getRadioGroup('', 300, conflictResolutionRadioButtons,  {itemId:conflictMapName+'RadioGroup'},
	                    {change: {fn: this.onMsProjectConflictHandlingForAll, scope:this,
	                        panel: conflictPanel, conflictValues: conflict['conflictValues'], conflictMapName:conflictMapName}});
	                items.push(conflictResolutionRadioGroup);
	                conflictPanel.add(items);
					var conflictValues = conflict['conflictValues'];
					Ext.Array.forEach(conflictValues, function(conflictValue, index) {
						var workItemID = conflictValue['workItemID'];
	                    var items = [];
						items.push({xtype: 'label',
							html: conflictValue['uid']
							});
						items.push({xtype: 'label',
							html: conflictValue['itemID']
							});
						items.push({xtype: 'label',
							html: conflictValue['title'],
	                        colspan: titleColspan
							});
	                    if (withValues) {
	                        items.push({xtype: 'label',
	                            html: conflictValue['msProjectValue']
	                            });
	                        items.push({xtype: 'label',
	                            html: conflictValue['trackplusValue']
	                            });
	                    }
	                    //the name of the conflict parameter to be submitted
	                    //the radio group will have the same itemId, to store only one array (this.conflictParameterNames) for submitting the conflict paramaters. See getDataBeforeCardDelete()
	                    var conflictParamName =  conflictMapName + "[" + workItemID + "]";
	                    var conflictParamItemId =  conflictMapName + workItemID;
						var conflictResolutionRadioButtons = CWHF.getRadioButtonItems(conflictHandlingList,
	                        conflictParamName, 'id', 'label', false, false, true);
						var conflictResolutionRadioGroup = CWHF.getRadioGroup('', 300, conflictResolutionRadioButtons,{itemId:conflictParamItemId});
	                    this.conflictParameterNames.push(conflictParamName);
						items.push(conflictResolutionRadioGroup);
	                    conflictPanel.add(items);
					}, this);
				}, this);
			}
			break;
		}
	},

	onMsProjectConflictHandlingForAll: function(field, newValue, oldValue, options) {
	    var panel = options["panel"];
	    var conflictMapName =  options["conflictMapName"];
	    var conflictValues = options["conflictValues"];
	    var conflictResolutionValue = newValue[conflictMapName];
	    var conflictParameterItemId =  conflictMapName.split('[').join('');
	    conflictParameterItemId =  conflictParameterItemId.split(']').join('');
	    Ext.Array.forEach(conflictValues, function(conflictValue) {
	        var workItemID = conflictValue['workItemID'];
	        var radioGroup = panel.getComponent(conflictParameterItemId + workItemID);
	        //TODO setValue() does not work (ext js bug?).
	        //radioGroup.setValue(conflictResolutionValue);
	        //Once setValue fixed remove this cycle
	        radioGroup.items.each(function(item){
	            item.setValue(item.inputValue === conflictResolutionValue);
	        });
	    }, this)
	},

	getDataBeforeCardDelete: function(cardNo, reload) {
	    var params = null;
	    if (cardNo===3) {
	        params = new Object();
	        if (reload && this.conflictParameterNames) {
	            var overwriteMap = new Object();
	            var card3 = this.wizardPanel.getComponent("card3");
	            var errorAndConflictPanel = card3.getComponent('errorAndConflict');
	            var conflictPanel = errorAndConflictPanel.getComponent('conflictPanel');
	            Ext.Array.forEach(this.conflictParameterNames, function(conflictParameterName) {
	            	var conflictParameterItemId =  conflictParameterName.split('[').join('');
	            	conflictParameterItemId =  conflictParameterItemId.split(']').join('');
	                var radioButtons = conflictPanel.getComponent(conflictParameterItemId);
	                var paramValue = CWHF.getSelectedRadioButtonValue(radioButtons);
	                params[conflictParameterName]= paramValue;
	            }, this);
	        }
	    }
	    return params;
	},

	/**
	 * This function initialize necessary components for showing pop
	 * up dialog
	 */
	createPopUpDialog: function(files) {
		var me = this;
		if(CWHF.isNull(files)) {
			me.isFromDragAndDrop = false;
			var windowParameters = {
				title: "",
				width: me.dialogWidth,
				height: me.dialogHeight,
				overrideButtons: me.getDialogButtons(),
				items: me.getWizardPanel()
			};
			me.window = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
			me.window.showWindowByConfig(me);
			me.actualizeToolbar(me.wizardPanel);
		}else {
			me.file = null;
			me.isFromDragAndDrop = true;
			if(files  && files.length > 0){
				me.file = files[0];
			}

			var continueToUpload = true;
			if (!me.validateFileExtension(me.file.name)) {
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
				formData.append('uploadFile', me.file);
				formData.append('uploadFileFileName', "MyName!");

				var windowParameters = {
					title: "",
					width: me.dialogWidth,
					height: me.dialogHeight,
					overrideButtons: me.getDialogButtons(),
					items: me.getWizardPanel()
				};

				me.window = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
			    var card1 = me.wizardPanel.getComponent("card1");
			    var fileUploader = card1.getComponent("uploadFile");
			    me.window.showWindowByConfig(me);
				me.actualizeToolbar(me.wizardPanel);
			}
		}
	},

	/**
	 * The following method uploads dropped excel file, and
	 * opens excel import wizard pop up dialog
	 */
	uploadMsProjFile: function() {
		var me = this;
		var formData = new FormData();
		formData.append('uploadFile', me.file);
		formData.append('uploadFileFileName', me.file.name);
	    var card1 = me.wizardPanel.getComponent("card1");
		var projectReleasesPicker = card1.getComponent("projectOrReleaseID");
		me.projectID = projectReleasesPicker.getValue();
		formData.append('projectOrReleaseID', me.projectID);
		var xhr = new XMLHttpRequest();
		var urlStr='msProjectUpload.action';
		xhr.open('POST', urlStr);
		xhr.onload = function () {
			if (xhr.status === 200) {
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
	},

	onNavigate: function(btn, panel) {
		var me = this;
		var layout = panel.getLayout();
		var activeItem = layout.getActiveItem();
		var index = panel.items.indexOf(activeItem);
		var numItems = panel.items.getCount() - 1;
		if (btn) {
	        var params = null;
			if (btn.itemId === 'next' || btn.itemId === 'finish' && index <= numItems) {
				if(index === 0 && me.isFromDragAndDrop) {
						me.uploadMsProjFile();
				}else {
					//+ 2 because index is 0 based and the data for the next card is loaded
					var reload = false;
					if (index===numItems) {
						//reload the error handling card
						index = index-1;
						reload = true;
	                    //two forms should be submitted by conflict handling: the second one is "serialized" in params
	                    params = this.getDataBeforeCardDelete(index+2, reload);
					}
					//TODO: redundantly initializes the next cards.
					//After a form is submitted and we go back to the previous card
					//the field values of the already submitted data is somehow stored/cached in the Ext.form.Basic
					//(even if the Ext.form.Panel form with all his fields is removed). Consequently when the form will be sumbitted
					//next time all form field values are duplicated in submit resulting in server side errors.
					//To avoid this initialize all next cards after each next.
					this.initCards(index+2);
					var dataIsValid = this.loadDataForCard(index+2, reload, params);
					if (dataIsValid!==false) {
						//if data is valid then change to the next card
						panel.layout.setActiveItem(index + 1);
					}
				}
			} else {
				if (btn.itemId === 'previous' && index > 0) {
					var activeItem = layout.getActiveItem();
					//remove all data from the current card before moving back to the previous card: next always starts from an empty card
					activeItem.removeAll();
					panel.layout.setActiveItem(index - 1);
				}
			}
		}
		this.actualizeToolbar(panel);
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
//				autoScroll: false,
//				scroll:false
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
			toolbarItems.push({
				itemId: 'finish',
				iconCls: 'import16',
				text: getText('common.btn.import'),
				scope: this,
				handler: function(btn) {
					this.onNavigate(btn, me.wizardPanel);
				},
				disabled: true
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
		}

		if(me.win) {
			me.win.setTitle(this.getTitle(index + 1));
		}
	},

	closeWindow: function() {
	},

	getWindowComponent: function() {
		var me = this;
		return me.window;
	}

});
