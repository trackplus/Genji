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


Ext.define("com.trackplus.admin.customize.project.ProjectTypeController",{
	extend: "Ext.app.ViewController",
	alias: "controller.projectEdit",
	mixins: {
		baseController: "com.trackplus.admin.FormBaseController"
	},
	
	/**
	 * Save the detail part
	 */
	/*onSave: function(button, event) {
		this.submitHandler(button, "project!save.action");	
	},*/

	
	onCancel: function() {
		if (this.getView().inDialog) {
			//close the window
			this.getView().ownerCt.close();
		} else {
			var lastSelectedProjectID = this.getView().projectID;
		    if (lastSelectedProjectID) {
				var projectConfig=Ext.create("com.trackplus.admin.project.ProjectConfig",{
					rootID: lastSelectedProjectID,
					isTemplate: this.getView().isTemplate,
					lastSelections: this.getView().lastSelections,
					hasPrivateProject: this.getView().hasPrivateProject,
					templateIsActive: this.getView().templateIsActive
				});
				borderLayout.borderLayoutController.setCenterContent(projectConfig);
		    } else {
		    	borderLayout.controller.setCenterContent(null);
		    }
		}
	    
	},

	onSave : function(button, event) {
	    this.saveProject(false);
	},

	/**
	 * Save the detail part
	 */
	saveProject : function(confirmSave) {
	    borderLayout.setLoading(true);
	    this.clearErrorTabs();
	    var params = this.getView().getSubmitParams();
	    if (confirmSave) {
	        params["confirmSave"] = confirmSave;
	    }
	    this.getView().getForm().submit({
	        url: "project!save.action",
	        params: params,
	        scope: this,
	        success: function(form, action) {
	            borderLayout.setLoading(false);
	            var result = action.result;
	            if (result.success) {
	            	if (this.getView().isDialog) {
	            		this.getView().ownerCt.close();
	            	} else {
	            		CWHF.showMsgInfo(getText('admin.project.successSave'));
	            	}
	            	this.onSuccess(this.getView().getCallerScope(), result, this.getView().refreshAfterSubmitHandler, this);
	            	/*var projectNodeToSelect = result['projectNodeToSelect'];
	                this.setRootID(projectNodeToSelect);*/
	            } else {
	                errorHandlerSave(result);
	            }
	        },
	        failure : function(form, action) {
	            borderLayout.setLoading(false);
	            result = action.result;
	            if (result ) {
	                if (action.result  && action.result.errors ) {
	                    this.handleErrors(action.result.errors);
	                } else {
	                    var errorCode = result.errorCode;
	                    var title = result.title;
	                    if (errorCode  && errorCode === 4) {
	                        // 4===NEED_CONFIRMATION
	                        var errorMessage = result.errorMessage;
	                        Ext.MessageBox.confirm(title, errorMessage, function(btn) {
		                        if (btn === "no") {
			                        return false;
		                        } else {
			                        this.saveProject(true);
		                        }
	                        }, this);
	                    } else {
	                        com.trackplus.util.submitFailureHandler(form, action);
	                    }
	                }
	            } else {
	                CWHF.showMsgError(getText('admin.project.err.errorSave'));
	            }
	        }
	    });
	},
	
	onTabChange: function(tabPanel, newCard, oldCard, eOpts) {
		var tabIndex = tabPanel.items.indexOf(newCard);
	    //this.getView().lastSelections.lastSelectedTab = tabIndex;
	    var storeTabUrl = "project!storeLastSelectedTab.action";
	    Ext.Ajax.request({
	        fromCenterPanel : true,
	        url : storeTabUrl,
	        disableCaching : true,
	        method : 'POST',
	        params : {
	            "tabIndex" : tabIndex
	        }
	    });
	},

	
	/**
	 * Handler for project type or issue type change
	 */
	onProjectTypeOrIssueTypeChange : function(combo, newValue, oldValue, eOpts) {
	    this.getView().tabPanel.ownerCt.getForm().submit({
	        //allow submit even without the required name this time
	        clientValidation : false,
	        url : "project!projectTypeChange.action",
	        params : {
	            projectID : this.getView().projectID,
	            addAsSubproject : this.getView().addAsSubproject
	        },
	        scope : this,
	        success : function(form, action) {
	            var result = action.result;
	            if (result.success) {
	                this.refreshSystemFieldCombos(result.data, this.getView().tabPanel.ownerCt);
	            } else {
	                errorHandlerSave(result);
	            }
	        },
	        failure : function(form, action) {
	            com.trackplus.util.submitFailureHandler(form, action);
	        }
	    });
	},
	
	/**
	 * Load the combos after the result has arrived containing also the combo data sources
	 */
	refreshSystemFieldCombos : function(data, panel) {
	    var mainTab = this.getControl("mainTab");
	    var accountingFieldSet = this.getControl("mainTab", "fsacc");
	    var oldProjectTypeHasAccounting = accountingFieldSet !== null;
	    var newProjectTypeHasAccounting = data["hasAccounting"];
	    if (oldProjectTypeHasAccounting !== newProjectTypeHasAccounting) {
	        if (oldProjectTypeHasAccounting && !newProjectTypeHasAccounting) {
	            mainTab.remove(accountingFieldSet);
	        } else {
	            this.getView().addAccountingFieldSet(data, mainTab);
	        }
	    } else {
	        if (newProjectTypeHasAccounting) {
	            this.actualizeAccountingByProjectType(data);
	        }
	    }
	    var releaseFieldSet = this.getControl("mainTab", "fsrel");
	    var oldProjectTypeHasRelease = releaseFieldSet !== null;
	    var newProjectTypeHasRelease = data["hasRelease"];
	    if (oldProjectTypeHasRelease !== newProjectTypeHasRelease) {
	        if (oldProjectTypeHasRelease && !newProjectTypeHasRelease) {
	            mainTab.remove(releaseFieldSet);
	        } else {
	            this.getView().addReleaseFieldSet(data, mainTab);
	        }
	    }
	    var initialStatus = this.getWrappedControl("defaultTab", "fsInitSt",
	            "projectDefaultsTOInitialStatusID");
	    initialStatus.store.loadData(data['statusList']);
	    initialStatus.setValue(data['projectDefaultsTO.initialStatusID']);

	    var defaultIssueType = this.getWrappedControl("defaultTab", "fsdef",
	            "projectDefaultsTODefaultIssueTypeID");
	    defaultIssueType.store.loadData(data['issueTypeList']);
	    defaultIssueType.setValue(data['projectDefaultsTO.defaultIssueTypeID']);

	    var defaultPriority = this.getWrappedControl("defaultTab", "fsdef",
	            "projectDefaultsTODefaultPriorityID");
	    defaultPriority.store.loadData(data['priorityList']);
	    defaultPriority.setValue(data['projectDefaultsTO.defaultPriorityID']);

	    var defaultSeverity = this.getWrappedControl("defaultTab", "fsdef",
	            "projectDefaultsTODefaultSeverityID");
	    defaultSeverity.store.loadData(data['severityList']);
	    defaultSeverity.setValue(data['projectDefaultsTO.defaultSeverityID']);

	    //after project type or issue type change the system list are refreshed from the server with valid values
	    //call validate for each system field to remove the eventual invalid marked fields at postDataLoadCombos()
	    initialStatus.validate();
	    defaultIssueType.validate();
	    defaultPriority.validate();
	    defaultSeverity.validate();
	    //var tabBar=tabPanel.getTabBar();
	    //var headerCm=tabBar.getComponent(1);
	    //headerCm.removeCls("errorTab");
	},
	
	actualizeAccountingByProjectType : function(data) {
	    var accountingControls = [ "projectAccountingTODefaultWorkUnit",
	            "projectAccountingTOHoursPerWorkday", "projectAccountingTOCurrencyName",
	            "projectAccountingTOCurrencySymbol" ];
	    Ext.Array.forEach(accountingControls, function(itemId) {
	        var control = this.getWrappedControl("mainTab", "fsacc", itemId.split(".").join("_"));
	        if (control ) {
	            control.setValue(data[itemId]);
	        }
	    }, this);
	},
	
	
	testEmailIncoming : function() {
	    borderLayout.setLoading(true);
	    var urlStr = 'project!testIncomingEmail.action';
	    this.clearErrorTabs();
	    var form = this.getView().tabPanel.ownerCt.getForm();
	    if (!form.isValid()) {
	        CWHF.showMsgError(getText('admin.project.err.invalidEmail'));
	        return false;
	    }
	    borderLayout.setLoading(true);
	    form.submit({
	        url : urlStr,
	        params : {
	            projectID : this.getView().projectID
	        },
	        scope: this,
	        success : function(form, action) {
	            borderLayout.setLoading(false);
	            CWHF.showMsgInfo(getText('admin.project.msg.validEmail'));
	        },
	        failure : function(form, action) {
	            borderLayout.setLoading(false);
	            if (action.result  && action.result.errors ) {
	                this.handleErrors(action.result.errors, getText('admin.project.err.invalidEmail'));
	            } else {
	                CWHF.showMsgError(getText('admin.project.err.invalidEmail'));
	            }
	        }
	    });
	},
	
	changePort: function(protocol, securityConnection, port) {
	    var protocol = this.getWrappedControl("emailTab", "fseserv", "projectEmailTOProtocol");
	    var securityConnection = this.getWrappedControl("emailTab", "fsauth", "securityConnection");
	    var port = this.getWrappedControl("emailTab", "fseserv", "projectEmailTOPort");
	    var protocolValue = CWHF.getSelectedRadioButtonValue(protocol);
	    var securityConnectionValue = CWHF.getSelectedRadioButtonValue(securityConnection);
	    var portValue;
	    if (protocolValue === this.getView().EMAIL_PROTOCOL.POP3) {
	        if (securityConnectionValue === this.getView().SECURITY_CONNECTIONS_MODES.SSL) {
	            portValue = this.getView().DEFAULT_EMAIL_PORTS.POP3_SSL;
	        } else {
	            portValue = this.getView().DEFAULT_EMAIL_PORTS.POP3;
	        }
	    } else {
	        if (securityConnectionValue === this.getView().SECURITY_CONNECTIONS_MODES.SSL) {
	            portValue = this.getView().DEFAULT_EMAIL_PORTS.IMAP_SSL;
	        } else {
	            portValue = this.getView().DEFAULT_EMAIL_PORTS.IMAP;
	        }
	    }
	    port.setValue(portValue);
	},

	
	clearErrorTabs : function() {
	    var tabBar = this.getView().tabPanel.getTabBar();
	    for (var i = 0; i < tabBar.items.length; i++) {
	        var headerCm = tabBar.getComponent(i);
	        headerCm.removeCls("errorTab");
	    }
	},
	
	handleErrors : function(errors, errorMsg) {
	    var errStr = '';
	    var tabErrors = new Array();
	    if (CWHF.isNull(errorMsg)) {
	        errorMsg = getText('admin.project.err.errorSave');
	    }
	    if (errors  && errors.length > 0) {
	        for (var i = 0; i < errors.length; i++) {
	            var error = errors[i];
	            var controlPath = error.controlPath;
	            var inputComp = null;
	            if (controlPath  && controlPath.length > 0) {
	                var tabId = controlPath[0];
	                if (!Ext.Array.contains(tabErrors, tabId)) {
	                    tabErrors.push(tabId);
	                }
	                inputComp = this.getView().getControl.apply(this.getView(), controlPath);
	                if (CWHF.isNull(inputComp)) {
	                    inputComp = this.getView().getWrappedControl.apply(this.getView(), controlPath);
	                }
	            }
	            if (inputComp ) {
	                inputComp.markInvalid(error.errorMessage);
	            } else {
	                errStr += error.errorMessage + "</br>";
	            }
	        }
	        this.markErrorTabs(tabErrors);
	    }
	    if (errStr !== '') {
	        errorMsg = errStr;
	    }
	    CWHF.showMsgError(errorMsg);
	},
	
	markErrorTabs : function(tabErrors) {
	    var tabErrorsCmp = new Array();
	    if (tabErrors.length > 0) {
	        for (var i = 0; i < tabErrors.length; i++) {
	            var tabComp = this.getControl(tabErrors[i]);
	            if (tabComp ) {
	                tabErrorsCmp.push(tabComp);
	            }
	        }
	        if (tabErrorsCmp.length > 0) {
	            var selectedTab = this.getView().tabPanel.getActiveTab();
	            if (!Ext.Array.contains(tabErrorsCmp, selectedTab)) {
	                this.getView().tabPanel.setActiveTab(tabErrorsCmp[0]);
	            }
	            var tabBar = this.getView().tabPanel.getTabBar();
	            for (var i = 0; i < tabErrorsCmp.length; i++) {
	                var index = this.getView().tabPanel.items.findIndex('itemId', tabErrorsCmp[i].getItemId());
	                var headerCm = tabBar.getComponent(index);
	                headerCm.addCls("errorTab");
	            }
	        }
	    }
	},
	
	/**
	 * Gets a control by the path according to the "arguments"
	 * starting form the main tab panel
	 */
	getControl : function() {
	    return CWHF.getControl.apply(this.getView().tabPanel, arguments);
	},

	getHelpWrapper : function() {
	    return CWHF.getHelpWrapper.apply(this.getView().tabPanel, arguments);
	},

	getWrappedControl : function() {
	    return CWHF.getWrappedControl.apply(this.getView().tabPanel, arguments);
	},
	
	
});
