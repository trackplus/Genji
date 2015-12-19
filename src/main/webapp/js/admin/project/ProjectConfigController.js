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


Ext.define("com.trackplus.admin.project.ProjectConfigController", {
	extend: "Ext.app.ViewController",
	alias: "controller.projectConfig",
	mixins: {
		baseController: "com.trackplus.admin.TreeDetailController"
	},
	
	//templateIsActive: false,
	baseServerAction: "project",
	tabPanel: null,
	addAsSubproject : false,
	addAsPrivateProject : false,
	
	GENERAL : 1,
	ASSIGN_ROLES : 2,
	PHASES : 3,
	LISTS : 4,
	FILTERS : 5,
	REPORT_TEMPLTATES : 6,
	SCREEN_ASSIGNMENT : 7,
	FIELD_CONFIGURATION : 8,
	ASSIGN_ACCOUNTS : 9,
	AUTOMAIL : 10,
	COCKPIT : 11,
	VERSION_CONTROL : 12,
	WORKFLOW : 13,
	EXPORT_TO_MSPROJECT : 14,
	IMPORT_FROM_MSPROJECT : 15,

	
	/**
	 * Handler for selecting a node in the tree
	 */
	onTreeNodeSelect : function(rowModel, node, index, opts) {
		if (node) {
	    	this.getView().selectedNode = node;
			this.getView().selectedNodeID = node.get("id");
			this.storeLastSelectedSection(this.getView().selectedNodeID);
			this.getView().lastSelections.lastSelectedSection = this.getView().selectedNodeID;
		}
		var selectedNodeClass = this.getSelectedNodeClass(node);
		if (this.getView().centerPanel) {
			this.getView().mainPanel.remove(this.getView().centerPanel, true);
		}
		if (this.getSelectedNodeIsWizardBased(node)) {
			this.getView().centerPanel = selectedNodeClass.getWizardPanel();
		} else {
			if (this.getSelectedNodeIsDetailPanelBased(node)) {
				this.getView().centerPanel = selectedNodeClass.getDetailPanel();
			} else {
				this.getView().centerPanel = selectedNodeClass;
			}
		}
		this.getView().mainPanel.add(this.getView().centerPanel);
		this.actualizeToolbarOnTreeSelect(node);
	    
	},

	/**
	 * Which actions to enable/disable depending on tree selection
	 */
	actualizeToolbarOnTreeSelect: function(selectedNode) {
		var selected = false;
		var generalIsSelected = false;
		if (selectedNode) {
			selected = true;
			var id = selectedNode.get("id");
			if (id==this.GENERAL) {
				generalIsSelected = true;
			}
		}
		if (this.getView().actionDelete) {
			this.getView().actionDelete.setDisabled(!generalIsSelected);
		}
	},
	
	/**
	 * Whether the node specific class is a wizard
	 * implementation
	 */
	getSelectedNodeIsWizardBased : function(node) {
	    var projectConfigType = node.get("id");
	    return projectConfigType === this.IMPORT_FROM_MSPROJECT;
	},

	/**
	 * Whether the node specific class is a wizard
	 * implementation
	 */
	getSelectedNodeIsDetailPanelBased : function(node) {
	    var projectConfigType = node.get("id");
	    return projectConfigType === this.VERSION_CONTROL;
	},

	

	/**
	 * Implement this method only if the tree contains
	 * heterogeneous data (like project/project type
	 * assignments) The specific methods on tree node selection
	 * should be called in the scope of the specific
	 * implementation Return the scope of the node specific
	 * implementation Additionally the locale tree and
	 * centerPanel objects are borrowed by the specific
	 * implementations (we do not initialize own tree for each
	 * specific implementation. An initialized (not null) tree
	 * is needed for the selection methods in tree in the
	 * context of a specific implementation and the centerPanel
	 * for rendering the data corresponding to the selected node
	 */
	getSelectedNodeClass: function(node) {
	    var projectConfigType = node.get("id");
	    var branchRoot = node.get("branchRoot");
	    switch (projectConfigType) {
	    case this.GENERAL: 
	    	 return Ext.create("com.trackplus.admin.project.ProjectEdit",{
	    		 	callerScope:this,
	    		 	refreshParametersBeforeSubmit: {isTemplate:this.getView().getIsTemplate()},
	    		 	refreshParametersAfterSubmit: [{parameterName:"reloadProjectTree", fieldNameFromResult:"reloadProjectTree"},
        	                               {parameterName:"projectNodeToSelect", fieldNameFromResult:"projectNodeToSelect"},
        	                               {parameterName:"projectNodeToReload", fieldNameFromResult:"projectNodeToReload"},
        	                               {parameterName:"reloadProjectConfigTree", fieldNameFromResult:"reloadProjectConfigTree"},
        	                               {parameterName:"projectConfigTypeNodeToSelect", fieldNameFromResult:"projectConfigTypeNodeToSelect"}],
        	        refreshAfterSubmitHandler: this.reload,
					entityContext: {
						isTemplate: this.getView().getIsTemplate(),
		    			addProject: false,
		    			addAsSubproject: false,
		     	        addAsPrivateProject: false,
		    			projectID: this.getView().getRootID(),
		    			inDialog: false,
		    			lastSelectedTab: this.getView().getLastSelections().lastSelectedTab
		    		}}); 
	    case this.PHASES:
	    	return Ext.create("com.trackplus.admin.project.Release", {
	    				rootID: branchRoot,
	    				//projectID: branchRoot,
		        		mainRelease: this.getView().getMainRelease(),
		        		childRelease: this.getView().getChildRelease(),
		        		showClosedReleases: this.getView().getShowClosedReleases()
		    		});
	    case this.ASSIGN_ROLES:
	        return Ext.create("com.trackplus.admin.project.RoleAssignment", {
	                	rootID : branchRoot
	            	});
	    case this.ASSIGN_ACCOUNTS:
	        return Ext.create("com.trackplus.admin.SimpleAssignment", {
	                	objectID: this.getView().getRootID(),
	                	objectIDParamName: "projectID", baseServerAction: "accountAssignments"
	            	});
	    case this.LISTS:
	    	return Ext.create("com.trackplus.admin.customize.list.ListConfig", {
	    				rootID : branchRoot,
	    				fromProjectConfig : true
		    		});
	    case this.FILTERS:
	        return Ext.create("com.trackplus.admin.customize.filter.FilterConfig", {
	                	rootID : branchRoot,
	                	projectID : this.getView().getRootID()
	            	});
	    case this.REPORT_TEMPLTATES:
	        return Ext.create("com.trackplus.admin.customize.report.ReportConfig", {
	                	rootID : branchRoot,
	                	projectID : this.getView().getRootID()
	        		});
	    case this.FIELD_CONFIGURATION:
	        return Ext.create('com.trackplus.admin.customize.treeConfig.FieldConfig', {
	                	rootID : branchRoot,
	                	fromProjectConfig : true
	            	});
	    case this.SCREEN_ASSIGNMENT:
	    	return Ext.create("com.trackplus.admin.customize.treeConfig.ScreenConfig", {
	                  	rootID : branchRoot,
	                  	fromProjectConfig : true
	               });
	       
	    case this.AUTOMAIL:
	        return Ext.create("com.trackplus.admin.NotifyConfig", {
	        			defaultSettings : true,
	        			exclusiveProjectID : this.getView().getRootID()
	            	});
	    case this.COCKPIT:
	       return Ext.create("com.trackplus.admin.project.ProjectCockpit", {
	                projectID : this.getView().getRootID()
	            });
	    case this.VERSION_CONTROL:
	        return Ext.create("com.trackplus.vc.VersionControlFacade", {
	            	versionControlFacadeModel: {
	                    projectID : this.getView().getRootID()
	                }
	            });
	    case this.WORKFLOW:
	        return Ext.create("com.trackplus.admin.customize.treeConfig.WorkflowConfig", {
                        rootID : branchRoot,
                        fromProjectConfig : true,
                        projectOrProjectTypeID : this.getView().getRootID()
                    });
	    case this.EXPORT_TO_MSPROJECT:
	       return Ext.create('com.trackplus.admin.action.ExportMsProject', {
	                projectID : this.getView().getRootID()
	            });
	    case this.IMPORT_FROM_MSPROJECT:
	    	return Ext.create("com.trackplus.admin.action.ImportMsProject", {
	                projectID : this.getView().getRootID()
	            });
	    }
	},

	/**
	 * ****************Methods strictly for project CRUD********************
	 */

	/**
	 * Prepare the form for adding a new project
	 */
	onAddProject : function() {
	    this.onAdd(false, false);
	},

	/**
	 * Prepare the form for adding a new project
	 */
	onAddSubproject : function() {
	    this.onAdd(true, false);
	},

	onAddPrivateProject : function() {
	    this.onAdd(false, true);
	},

	onAdd: function(addAsSubproject, addAsPrivateProject) {
		if (this.getView().centerPanel) {
			this.getView().mainPanel.remove(this.getView().centerPanel, true);
		}
		var projectAdd = Ext.create("com.trackplus.admin.project.ProjectEdit",{
		 	callerScope:this,
		 	refreshParametersBeforeSubmit: {isTemplate:this.getView().getIsTemplate()},
		 	refreshParametersAfterSubmit: [{parameterName:"reloadProjectTree", fieldNameFromResult:"reloadProjectTree"},
	                               {parameterName:"projectNodeToSelect", fieldNameFromResult:"projectNodeToSelect"},
	                               {parameterName:"projectNodeToReload", fieldNameFromResult:"projectNodeToReload"},
	                               {parameterName:"reloadProjectConfigTree", fieldNameFromResult:"reloadProjectConfigTree"},
	                               {parameterName:"projectConfigTypeNodeToSelect", fieldNameFromResult:"projectConfigTypeNodeToSelect"}],
	        refreshAfterSubmitHandler: com.trackplus.admin.refrehProjectTree,
			entityContext: {
				projectID: this.getView().getRootID(),
				isTemplate: this.getView().getIsTemplate(),
				templateIsActive: this.getView().getTemplateIsActive(),
    			addProject: true,
    			addAsSubproject : addAsSubproject,
    	        addAsPrivateProject : addAsPrivateProject,
    	        hasPrivateProject: this.getView().getHasPrivateProject(),
    	        inDialog: false,
    	        lastSelections: this.getView().getLastSelections()
    		}});
		borderLayout.borderLayoutController.setCenterContent(projectAdd);
	},

	onCopy: function() {
	    var projectCopyController = Ext.create('com.trackplus.admin.project.ProjectCopyController', {
	    	projectID: this.getView().getRootID(),
	        isTemplate: this.getView().getIsTemplate(),
	        localCopy: true
	    });
	    projectCopyController.showDialog();
	},

	onCreateFromCopy: function() {
	        var projectCopyController = Ext.create('com.trackplus.admin.project.ProjectCopyController', {
	    	projectID: this.getView().getRootID(),
	        isTemplate: this.getView().getIsTemplate(),
	        localCopy: false
	    });
	    projectCopyController.showDialog();
	},

	onLockUnlock: function() {
	    Ext.Ajax.request({
	        url : "project!changeTemplateState.action",
	        params : {
	            projectID : this.getView().getRootID()
	        },
	        scope : this,
	        success : function(response) {
	            var result = Ext.decode(response.responseText);
	            var data = result.data;
	            var success = result.success;
	            if (success) {
	                this.getView().setTemplateIsActive(data.templateIsActive);
	                this.getView().actionLockUnlock.setText(this.getView().getTemplateLockUnlockLabel());
	                this.getView().actionLockUnlock.setIconCls(this.getView().getTemplateLockUnlockIcon());
	                com.trackplus.admin.refrehProjectTree({isTemplate: true, projectNodeToSelect:this.getView().getRootID()});
	                
	            } else {
	                Ext.MessageBox.alert(this.failureTitle,
	                        getText('admin.project.lbl.changeTemplateStateError'));
	            }
	        },
	        failure : function(response) {
	            Ext.MessageBox.alert(this.failureTitle, response.responseText);
	        }
	    });
	},

	onDelete: function() {
	    var selectedRecord = this.getView().getRootID();
	    if (selectedRecord) {
	        var extraConfig = {
	            fromTree : true,
	            isLeaf : false
	        };
	        this.deleteHandler(this.getView().getEntityLabel(), selectedRecord, extraConfig);
	    }
	},

	/**
	 * Parameters for reloading after a delete operation
	 * By delete the reload and select parameters are known before
	 */
	getReloadParamsAfterDelete: function(selectedRecords, extraConfig, responseJson) {
		return {reloadProjectTree: true, isTemplate:this.getView().getIsTemplate()};
	},
	
	/**
	 * Reload after save or delete
	 */
	reload: function(refreshParamsObject) {
		var reloadProjectTree = refreshParamsObject["reloadProjectTree"];
	    var projectNodeToSelect = refreshParamsObject["projectNodeToSelect"];
	    var projectNodeToReload = refreshParamsObject["projectNodeToReload"];
	    var reloadProjectConfigTree = refreshParamsObject["reloadProjectConfigTree"];
	    var projectConfigTypeNodeToSelect = refreshParamsObject["projectConfigTypeNodeToSelect"];
	    var deletedProjectID = refreshParamsObject["deletedProjectID"];
	    if (!reloadProjectTree && reloadProjectConfigTree && this.getView().tree) {
	        // this.tree is null by add 
	        // only after save
	        var options = new Object();
	        if (projectConfigTypeNodeToSelect) {
	            // callback after load
	            options.callback = this.selectNode;
	            // options.node = projectNodeToSelect;
	            // scope for callback
	            options.scope = {
	                tree : this.getView().tree,
	                nodeIdToSelect : projectConfigTypeNodeToSelect
	            };
	        }
	        this.getView().tree.getStore().load(options);
	    }
	    if (reloadProjectTree) {
	    	com.trackplus.admin.refrehProjectTree(refreshParamsObject);
	    }
	},

	selectNode: function() {
	    if (this.nodeIdToSelect) {
	        var nodeToSelect = this.tree.getStore().getNodeById(this.nodeIdToSelect);
	        if (nodeToSelect) {
	            var selectionModel = this.tree.getSelectionModel();
	            selectionModel.select(nodeToSelect);
	        }
	    }
	},

	/**
	 * Parameters for deleting entity recordData: the selected
	 * entity data Even if there is more than one entity
	 * selected for delete this method is called for each
	 * selected entity separately extraConfig: for simple grid
	 * nothing, for tree with grid {fromTree:fromTree,
	 * isLeaf:isLeaf}
	 */
	getDeleteParams : function(selectedRecords, extraConfig) {
	    var deleteParams = new Object();// this.callParent(arguments);
	    deleteParams["projectID"] = this.getView().getRootID();
	    deleteParams["deleteConfirmed"] = this.deleteConfirmed;
	    return deleteParams;
	},
	
	storeLastSelectedSection : function(sectionID) {
	    var storeTabUrl = "project!storeLastSelectedSection.action";
	    Ext.Ajax.request({
	        fromCenterPanel : true,
	        url : storeTabUrl,
	        disableCaching : true,
	        method : 'POST',
	        params : {
	            "sectionID" : sectionID
	        }
	    });
	}

});
