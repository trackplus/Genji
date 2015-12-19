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

Ext.define('com.trackplus.admin.project.ProjectCopyController',{
	extend:'Ext.Base',
	config: {
		projectID: null,
		isTemplate: false,
		localCopy: true
	},
	view:null,
	copy:false,

	constructor : function(config) {
		var config = config || {};
		this.initConfig(config);
	},
	
	getTitle: function() {
		if (this.getIsTemplate()) {
	    	if (this.getLocalCopy()) {
	    		 return getText("admin.project.copy.titleCopyTpl");
	    	} else {
	    		return getText("common.btn.copyTo");
	    	}
	    } else {
	    	if (this.getLocalCopy()) {
	    		return getText("admin.project.copy.title");
	    	} else {
	    		return getText("admin.project.copy.titleCreateTplFromWp");
	    	}
	    }
	},

	getSaveLabel: function() {
		if (this.getLocalCopy()) {
			return getText("common.btn.copy");
		} else {
			if (this.getIsTemplate()) {
				return getText("admin.project.lbl.createTemplate");
			} else {
				return getText("admin.project.lbl.createSpace");
			}
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
		var width = 500;
		var height = 600;
		var loadParams = {projectID:this.getProjectID(), isTemplate:this.getIsTemplate(), localCopy:this.getLocalCopy()};
		var load = {loadUrl:"projectCopy.action", loadUrlParams:loadParams};
		var submitParams = {projectID:this.getProjectID(), isTemplate:this.getIsTemplate(), localCopy:this.getLocalCopy()};
		
		var submit = {submitUrl:"projectCopy!copy.action",
					submitUrlParams:submitParams,
					submitButtonText:this.getSaveLabel(),
					refreshParametersAfterSubmit: [{parameterName:"projectNodeToSelect", fieldNameFromResult:"projectNodeToSelect"}],
					refreshAfterSubmitHandler:this.reload};
		var postDataProcess = this.postDataProcess;

		var helpText = this.getWindowTopHelpText();
		var windowParameters = {title:this.getTitle(),
			width:width,
			height:height,
			load:load, submit:submit,
			panelConfig:{
				bodyStyle:{
					padding:'5 5 5 0'
				}
			},
			items:[{
					xtype: 'component',
					cls:"infoBox_bottomBorder",
					border:true,
					html: helpText
				},
				CWHF.createTextField("admin.project.copy.lbl.projectName", "projectName",{itemId:'projectName', allowBlank:false, maxLength:255, padding: '5 5 5 5'})],
			postDataProcess:postDataProcess};
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
		windowConfig.showWindowByConfig(this);
	},

	postDataProcess: function(data, panel) {
		this.populateProjectCopyControls(this, panel, data);
	},

	/**
	 * This method handles reloading the proper tree item
	 * (project or template) based on actionTarget parameter:
	 * For ex: when creating a template from workspace: the
	 * workspace node must collapse, the template node must
	 * expand with selecting the newly created node.
	 */
	reload : function(reloadParameters) {
	    var templateTree = Ext.getCmp("tree-projectTemplateTreePanel");
	    var projectTree = Ext.getCmp("tree-projectTreePanel");
	    var reloadProjectTree = false;
	    if (this.getIsTemplate()) {
	    	if (this.getLocalCopy()) {
	    		reloadProjectTree = false;
	    	} else {
	    		projectTree.expand();
	    		templateTree.getSelectionModel().deselectAll();
	    		reloadProjectTree = true;
	    	}
	    } else {
	    	if (this.getLocalCopy()) {
	    		reloadProjectTree = true;
	    	} else {
	    		templateTree.expand();
	    		projectTree.getSelectionModel().deselectAll();
	    		reloadProjectTree = false;
	    	}
	    }
	    reloadParameters["isTemplate"] = !reloadProjectTree;
	    com.trackplus.admin.refrehProjectTree(reloadParameters)
	},
	
	populateProjectCopyControls: function(scope, projectCopyMainPanel, data) {
		var customLists = data["customLists"];
		var projectCopyControls = [];
		if (projectCopyMainPanel) {
			var showAsSibling = data['showAsSibling'];
			if (showAsSibling) {
				projectCopyControls.push(CWHF.createCheckbox("admin.project.copy.lbl.copyAsSiblingProject",
						"copyAsSibling"));
			}
			var showCopySubprojects = data['showCopySubprojects'];
			if (showCopySubprojects) {
				projectCopyControls.push(CWHF.createCheckbox("admin.project.copy.lbl.copySubprojects",
						"copySubprojects"));
			}
			var showCopyItems = data["showCopyItems"];
			if (showCopyItems) {
				projectCopyControls.push({xtype:"fieldset",
					itemId:"fsItems",
					title: getText("admin.project.copy.lbl.copyItems"),
					//collapsible: false,
					//defaults: {anchor: '100%'},
					//layout: 'anchor',
					items:[CWHF.createCheckbox("admin.project.copy.lbl.copyOpenItems", "copyOpenItems", null,
							{change: {fn: this.changeCopyItems, scope:this,
								panel:projectCopyMainPanel, customLists:customLists}}),
							CWHF.createCheckbox("admin.project.copy.lbl.copyAttachments", "copyAttachments", {itemId:"copyAttachments",disabled:true})
					       ]});
				}
		}
		var showCopyReleases = data["showCopyReleases"];
		if (showCopyReleases || (customLists && customLists.length>0)) {
			var items = [];
			if (showCopyReleases) {
				items.push(CWHF.createCheckbox("admin.project.copy.lbl.releases", "copyReleases"),{itemId:"copyReleases"});
			}
			if (customLists && customLists.length>0) {
				Ext.Array.forEach(customLists, function(expression) {
					var expressionPanel = CWHF.createCheckbox(expression["entityLabel"],
							expression["entityName"], {labelIsLocalized:true});
					items.push(expressionPanel);
				}, scope);
			}
			projectCopyControls.push({xtype:"fieldset",
				itemId:"fsLists",
				title: getText("admin.project.copy.lbl.customLists"),
				items:items});
		}

		var associatedEntities = data["associatedEntities"];
		if (associatedEntities) {
			var items = [];
			Ext.Array.forEach(associatedEntities, function(expression) {
				var expressionPanel = CWHF.createCheckbox(expression["entityLabel"],
						expression["entityName"], {labelIsLocalized:true});
				items.push(expressionPanel);
			}, scope);
			projectCopyControls.push({xtype:"fieldset",
				itemId:"fsAssociated",
				title: getText("admin.project.copy.lbl.copyAssignments"),
				//collapsible: false,
				//bodyPadding: '10 10 10 10',
				//defaults: {anchor: '100%'},
				//layout: 'anchor',
				items:items});

		}
		//add all panels at once
		projectCopyMainPanel.add(projectCopyControls);
	},

	changeCopyItems: function(checkboxField, newValue, oldValue, options) {
		var mainPanel = options.panel;
		var copyAttachmentsCheckBox =  CWHF.getControl.apply(mainPanel, ["fsItems", "copyAttachments"]);
		if (copyAttachmentsCheckBox) {
			copyAttachmentsCheckBox.setDisabled(!newValue);
			if (!newValue) {
				copyAttachmentsCheckBox.setValue(false);
			}
		}
		var copyReleasesCheckbox = CWHF.getControl.apply(mainPanel, ["fsLists", "copyReleases"]);
		if (copyReleasesCheckbox) {
			copyReleasesCheckbox.setDisabled(newValue);
			if (newValue) {
				copyReleasesCheckbox.setValue(true);
			}
		}
		var customLists = options.customLists;
		if (customLists && customLists.length>0) {
			Ext.Array.forEach(customLists, function(expression) {
				var customListCheckBox = CWHF.getControl.apply(mainPanel, ["fsLists", expression["entityName"]]);
				if (customListCheckBox) {
					customListCheckBox.setDisabled(newValue);
					if (newValue) {
						customListCheckBox.setValue(newValue);
					}
				}
			}, this);
		}
		var projectFieldConfigsCheckbox = CWHF.getControl.apply(mainPanel, ["fsAssociated", "associatedEntitiyMap[5]"]);
		if (projectFieldConfigsCheckbox) {
			projectFieldConfigsCheckbox.setDisabled(newValue);
			if (newValue) {
				projectFieldConfigsCheckbox.setValue(true);
			}
		}
	},

	getWindowTopHelpText: function() {
		if (this.getIsTemplate()) {
	    	if (this.getLocalCopy()) {
	    		return getText("admin.projectTemplate.copy.lbl.messageCopyTemplate");
	    	} else {
	    		return getText("admin.projectTemplate.copy.lbl.messageCreateWpFromTpl");
	    	}
	    } else {
	    	if (this.getLocalCopy()) {
	    		return getText("admin.project.copy.lbl.message");
	    	} else {
	    		return getText("admin.projectTemplate.copy.lbl.messageCreateTplFromWp");
	    	}
	    }
	}
});
