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

Ext.define('com.trackplus.admin.NotifySettingsList', {
	extend:	'com.trackplus.admin.GridConfig',
	config: {defaultSettings:false,
		//the exclusiveProject set only for automail in project settings
		exclusiveProjectID: null},
	urlStore: 'notifySettings!loadList.action',
	autoLoadGrid:false,
	fields:	[{name: 'id',	type: 'int'},
				{name: 'project',	type: 'int'},
				{name: 'projectLabel',	type: 'string'},
				{name: 'triggerLabel',	type: 'string'},
				{name: 'filterLabel',	type: 'String'},
				{name: 'inherited',	type: 'boolean'}],
	getEntityLabel: function() {
		return getText('admin.customize.automail.assignments.lbl.assignment');
	},
	confirmDeleteEntity:true,
	baseAction:'notifySettings',
	entityID:'notifySettingsID',
	editWidth:500,
	editHeight:210,
	labelWidth: 200,
	actionOverwrite:null,

	/**
	 * The iconCls for the overwrite button
	 */
	getOverwriteIconCls: function() {
		return 'copy';
	},

	/**
	 * The key for overwrite button text
	 */
	getOverwriteButtonKey: function() {
		return 'common.btn.overwrite';
	},

	/**
	 * The title for "add" popup and "add" action tooltip
	 */
	getOverwriteTitleKey: function() {
		return 'common.lbl.overwrite';
	},

	/**
	 * Specify the columnModel implementing the getColumnModel() instead of columnModel config parameter
	 * because "this" is not available in config
	 */
	getColumnModel:function() {
		return [{
			text: getText('common.lbl.name'),
			flex:1, dataIndex: 'projectLabel', sortable:true, renderer:this.renderer
		}, {
			text: getText('admin.customize.automail.trigger.lblAlone'),
			flex:1, dataIndex: 'triggerLabel', sortable:true, renderer:this.renderer
		}, {
			text: getText('admin.customize.automail.filter.lblAlone'),
			flex:1, dataIndex: 'filterLabel', sortable:true, renderer:this.renderer
		}];
	},

	/**
	 * Get extra parameters for grid load
	 */
	getLoadGridParams:function() {
		return {defaultSettings: this.defaultSettings, exclusiveProjectID:this.exclusiveProjectID};
	},

	/**
	 * Parameters for adding a new entity
	 */
	getAddParams:function() {
		return {defaultSettings: this.defaultSettings, exclusiveProjectID: this.exclusiveProjectID};
	},

	/**
	 * Parameters for editing an existing entity
	 */
	getEditParams:function(recordData) {
		var params = {defaultSettings: this.defaultSettings, exclusiveProjectID: this.exclusiveProjectID};
		params[''+this.entityID]=recordData.id;
		return params;
	},

	/**
	 * Parameters for deleting an existing entity
	 */
	getDeleteParams:function(selectedRecord) {
		var params = new Object();
		params[''+this.entityID]=selectedRecord.data.id;
		params['defaultSettings']=this.defaultSettings;
		return params;
	},

	//actions
	initActions:function(){
		this.actionAdd = this.createAction(this.getAddButtonKey(), this.getAddIconCls(), this.onAdd, false, this.getAddTitleKey());
		this.actionEdit = this.createAction(this.getEditButtonKey(), this.getEditIconCls(), this.onEdit, true, this.getEditTitleKey());
		this.actionDelete = this.createAction(this.getDeleteButtonKey(), this.getDeleteIconCls(), this.onDelete, true, this.getDeleteTitleKey());
		if (this.defaultSettings==false) {
			this.actionOverwrite = this.createAction(this.getOverwriteButtonKey(), this.getOverwriteIconCls(), this.onOverwrite, true, this.getOverwriteTitleKey());
			this.actions=[this.actionAdd,this.actionEdit, this.actionOverwrite, this.actionDelete];
		} else {
			this.actions=[this.actionAdd,this.actionEdit, this.actionDelete];
		}
	},

	onGridStoreLoad: function(store, records) {
		if (this.exclusiveProjectID!=null && records!=null && records.length>0) {
			this.actionAdd.setDisabled(true);
		} else {
			this.actionAdd.setDisabled(false);
		}
	},

	/**
	 * Handler for overwrite
	 */
	onOverwrite: function(){
		var recordData=this.getSingleSelectedRecordData();
		if (recordData!=null) {
			this.openEditEntity(this.getOverwriteTitleKey(), recordData, "overwrite");
		}
	},

	/**
	 * Parameters for saving an entity (extra parameters additionally to submit parameters)
	 */
	getSaveParams: function(recordData, type){
		var result=null;
		if (type=="add"){
			result = this.getAddParams();
		} else {
			if (type=="edit") {
				result = this.getEditParams(recordData);
			} else {
				if (type=="overwrite") {
					result = new Object();
					//the default (original) notifySettingsID is not needed
					//but the project should be explicitly set because it is disabled (not automatically submitted)
					result['project']=recordData.project;
				}
			}
		}
		if (result==null) {
			//should not be null because dynamic parameters might be added right before submit
			//(in either submitHandler()  or preSubmitProcess() implementations)
			result = new Object();
		}
		return result;
	},

	/**
	 * Function to be called before submit to add dynamic parameters
	 * to existing submitUrlParams based on the panel's content
	 */
	preSubmitProcess: function(submitUrlParams, panel, submitAction) {
	 	//add parameters to submitUrlParams based on panel
		if (submitAction=="overwrite") {
			//if overwrite, the project combo is disabled and consequently not submitted
			//manually add project parameter
			var projectsList = panel.getComponent('projectsList');
			if (projectsList!=null) {
				submitUrlParams['project']=projectsList.getValue();
			}
		}
		return submitUrlParams;
	},

	/**
	 * Render the inherited rows as grey
	 */
	renderer: function(value, metadata, record) {
		if (record.data.inherited) {
			metadata.style = 'color:#909090';
		}
		return value;
	},

	/**
	 * Enable change only if not inherited
	 */
	onGridSelectionChange: function(view, arrSelections) {
		if (arrSelections==null || arrSelections.length==0){
			this.actionDelete.setDisabled(true);
			this.actionEdit.setDisabled(true);
			if (this.actionOverwrite!=null) {
				this.actionOverwrite.setDisabled(true);
			}
		} else {
			var selectedRecord = arrSelections[0];
			var inherited = selectedRecord.data['inherited'];
			this.actionDelete.setDisabled(inherited);
			this.actionEdit.setDisabled(inherited);
			if (this.actionOverwrite!=null) {
				this.actionOverwrite.setDisabled(!inherited);
			}
		}
	},

	/**
	 * Get the actions available in context menu depending on the currently selected row
	 */
	getGridContextMenuActions: function(selectedRecords, selectionIsSimple) {
		var actions = [];
		if (selectionIsSimple) {
			var inherited = selectedRecords.data['inherited'];
			if (inherited) {
				actions.push(this.actionOverwrite);
			} else {
				actions.push(this.actionEdit);
				actions.push(this.actionDelete);
			}
		}
		return actions;
	},

	/**
	 *  Handler for double click
	 */
	onItemDblClick:function(view, record) {
		var inherited = record.data['inherited'];
		if (inherited) {
			this.onOverwrite.call(this);
		} else {
			this.onEdit.call(this);
		}
	},

	/**
	 * If not own allow only copy
	 */
	contextMenuActions: function(selectedRow) {
		var inherited=selectedRow.data['inherited'];
		if (inherited) {
			return [this.actionOverwrite];
		} else {
			return [this.actionEdit,this.actionDelete];
		}
	},

	/**
	 * Populate the combos once data is loaded
	 */
	afterLoadForm: function(data, panel) {
	    //first set the filter, because by onProjectSelect the filterPicker's value will be sent
	    var filterPicker = panel.getComponent('filter');
	    filterPicker.updateData(data["filterTree"]);
	    filterPicker.setValue(data["filter"]);
	    var projectPicker = panel.getComponent('project');
	    projectPicker.updateData(data["projectTree"]);
	    projectPicker.setValue(data["project"]);
		var triggersList = panel.getComponent('trigger');
		triggersList.store.loadData(data['triggersList']);
		triggersList.setValue(data['trigger']);
	},

	/**
	 * Create the form to edit
	 */
	createEditForm: function(recordData, type) {
		return new Ext.form.Panel({
			border	: false,
			autoScroll:true,
			margin: '0 0 0 0',
			bodyStyle:{
				padding:'10px'
			},
			defaults : {
				labelStyle:'overflow: hidden;',
				msgTarget : 'side',
				anchor : '100%'
			},
			items : this.getFormItems(recordData, type)
		});
	},

	/**
	 * Gets the form items
	 */
	getFormItems: function(recordData, type) {
		var inherited = (type=="overwrite");
		var notifySettingsID = null;
		if (recordData!=null) {
			notifySettingsID = recordData['id'];
		}
	    var filterPicker = CWHF.createSingleTreePicker("admin.customize.automail.filter.lblAlone",
	        "filter", [], null,
	        {allowBlank:false,
	         labelWidth:this.labelWidth,
	         margin:'0 0 5 0'
	        })
	    var projectPicker = CWHF.createSingleTreePicker("admin.project.lbl.project",
	        "project", [], null,
	        {allowBlank:false,
	            labelWidth: this.labelWidth,
	            margin:'0 0 5 0'
	        }, {select:{fn: this.onProjectSelect, scope:this,
	            filterPicker: filterPicker}});
		return [projectPicker,
				CWHF.createCombo("admin.customize.automail.trigger.lblAlone",
						"trigger", {labelWidth:this.labelWidth}),
				filterPicker];
	},

	/**
	 * Change event handler
	 * @param projectPicker
	 * @param selectedProjects
	 * @param options
	 */
	onProjectSelect: function(projectPicker, selectedProject, options) {
	    var filterPicker = options["filterPicker"];
	    Ext.Ajax.request({
	        url: "notifySettings!projectChange.action",
	        params:{defaultSettings: this.defaultSettings, project: projectPicker.getValue(), filter: filterPicker.getValue()},
	        scope:this,
	        success: function(response){
	            var responseJSON = Ext.decode(response.responseText);
	            filterPicker.updateData(responseJSON["filterTree"]);
	            filterPicker.setValue(responseJSON["filter"]);
	        },
	        failure: function(response){
	            com.trackplus.util.requestFailureHandler(response);
	        }
	    })
	},

	/**
	 * The iconCls for the add button, overwrites base class icon
	 */
	getAddIconCls: function() {
		return 'automailAdd';
	},
	/**
	 * The iconCls for the edit button, overwrites base class icon
	 */
	getEditIconCls: function() {
		return 'automailEdit';
	}
});
