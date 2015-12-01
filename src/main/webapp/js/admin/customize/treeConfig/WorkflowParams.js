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

//controller
Ext.define("com.trackplus.admin.customize.treeConfig.WorkflowParams",{
	extend:"Ext.Base",
	config: {
		workflowID:null,
		node:null,
		workflowConfigContoller:null
	},
	paramExpressionsRender: null,
	constructor : function(cfg) {
		var config = cfg || {};
		this.initConfig(config);
	},

	getTitle: function() {
		return getText("admin.customize.workflow.config.param.title");
	},

	getSaveLabel: function() {
		return getText('common.btn.save');
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
		var loadParams = {workflowID:this.getWorkflowID(), node:this.getNode()};
		var load = {loadUrl:"workflowParams.action", loadUrlParams:loadParams};
		var submitParams = {workflowID:this.getWorkflowID(), node:this.getNode()};
		var submit = {
					/*submitUrl:"workflowParams!save.action",
					submitUrlParams:submitParams,*/
					submitButtonText:this.getSaveLabel(),
					submitHandler: this.submitHandler,
					refreshParametersBeforeSubmit: submitParams,
					refreshAfterSubmitHandler:this.getWorkflowConfigContoller().saveWorkflowAssignment, loading:true};
		var postDataProcess = this.postDataProcess;
		var windowParameters = {title:this.getTitle(),
			width:width,
			height:height,
			load:load, submit:submit,
			items:[],
			//windowConfig: {closeAction:"destroy"},
			postDataProcess:postDataProcess,
			panelConfig:{
				bodyStyle:{
					padding:'0 0 0 10'
				}
			}
		};
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
		windowConfig.showWindowByConfig(this);
	},

	submitHandler: function(window, submitUrl, submitUrlParams) {
		var panel = this.formEdit;
		var triggerPanels = panel.items;
	    var paramExpressionsSubmit = [];
	    for ( var i=1;i<triggerPanels.getCount();i++ ) {
	    	var triggerPanel = triggerPanels.getAt(i);
	    	var triggerExpression = this.paramExpressionsRender[i-1];
	    	var activitiesRender = triggerExpression["activities"];
	    	Ext.Array.forEach(activitiesRender, function(activityExpression, index, allItems) {
	    		var activityPanel = triggerPanel.getComponent("activityPanel" + activityExpression["workflowActivityID"]);
	    		var paramExpressionSubmit = {};
	        	paramExpressionSubmit["workflowActivityID"] = activityExpression["workflowActivityID"];
	            var setterControl = activityPanel.getComponent("setter" + activityExpression["workflowActivityID"]);// items.getAt(0);
	            if (setterControl && setterControl.getValue()) {
	            	paramExpressionSubmit["selectedSetter"] = setterControl.getValue();
	            }
	            var valueControl = activityPanel.getComponent("value" + activityExpression["workflowActivityID"]);//.items.getAt(1);
	            if (valueControl) {
	                paramExpressionSubmit["value"] = valueControl.getSetterValue();
	            }
	        	paramExpressionsSubmit.push(paramExpressionSubmit);
	    	});
	    }
	    var paramsJSON = Ext.encode(paramExpressionsSubmit);
		borderLayout.setLoading(true);
		Ext.Ajax.request({
			url: "workflowParams!save.action",
			disableCaching:true,
			scope: this,
			params: {
				node: this.getNode(),
				paramsJSON:paramsJSON
			},
			success: function(response) {
				borderLayout.setLoading(false);
				this.getWorkflowConfigContoller().saveWorkflowAssignment(this.getWorkflowID(), this.getNode());
			},
			failure: function(response){
				borderLayout.setLoading(false);
				alert("failed to save params");
			}
		});
		window.close();
	},

	postDataProcess: function(data, panel) {
		this.paramExpressionsRender = data["triggers"];
		this.populateParamExpressions(this, panel, data);
	},

	/**
	 * Refreshes a grid's store and selects a row by rowToSelect parameter if it is specified
	 * Called from selectAfterReload() (grid and tree refresh) and from simple grid CRUD
	 */
	reload: function() {
		this.itemNavigatorController.refresh.call(this.itemNavigatorController);
	},

	populateParamExpressions: function(scope, paramsMainPanel, data) {
		if (paramsMainPanel) {
			var workflowName = data["workflowName"];
			var context = data["context"];
			var paramPanels = [{
				xtype: 'component',
				cls:"infoBox_bottomBorder",
				border:true,
				html: getText("admin.customize.workflow.config.param.infoBox", workflowName, context)
			}];
			paramsMainPanel.add(paramPanels);
			var triggerExpressions = data["triggers"];
			if (triggerExpressions) {
				for(var i=0;i<triggerExpressions.length;i++){
					var triggerExpression=triggerExpressions[i];
					var triggerPanel = Ext.create("Ext.panel.Panel", {
						border:false,
						bodyBorder:false,
						//title: triggerExpression["activityTriggerLabel"],
						/*layout: {
							type:'vbox',
							pack: 'start',
							align: 'top'//'stretch'
						},*/
						items:[]
					});
					triggerPanel.add(this.createParamExpression(scope,
							triggerExpression));
					paramsMainPanel.add(triggerPanel);
				}
			}
			//add all panels at once
			//paramsMainPanel.add(triggerPanel);
		}
	},

	/**
	 * Create a simple filter expression
	 */
	createParamExpression: function(scope, paramExpression) {
		var paramPanels = [{
			xtype: 'component',
			cls:"infoBox",
			border:true,
			html: paramExpression["activityTriggerLabel"]
		}];
		var activities = paramExpression["activities"];
		Ext.Array.forEach(activities, function(activity, index, allItems) {
			var activityPanel = Ext.create("Ext.panel.Panel", {
				itemId: "activityPanel" + activity["workflowActivityID"],
				border:false,
				bodyBorder:false,
				layout: {
					type:'hbox'/*,
					pack: 'start',
					align: 'top'*/
				}
			});
			var hasSetter = activity["hasSetter"];
			var activityTypeLabel = activity["activityTypeLabel"];
			var setterCombo = null;
			if (hasSetter) {
				var setterName = "setter" + activity["workflowActivityID"];
				setterCombo = CWHF.createCombo(activityTypeLabel, setterName,
					{	itemId: setterName,
						width:270,
						data:activity["possibleSetters"],
						value:activity["selectedSetter"],
						labelWidth:120,
						margin:'2 5 2 5',
						labelIsLocalized:true
					},
					{select: {fn: this.changeSetter, scope:scope,
						panel:activityPanel, activityExpression:activity}});
				activityPanel.add(setterCombo);
			}
			var jsonConfig = activity["jsonConfig"];
			jsonConfig["name"] = "value" + activity["workflowActivityID"];
			jsonConfig["activityTypeLabel"] = activityTypeLabel;
			jsonConfig["assignment"] = true;
			jsonConfig["labelWidth"] = 120;
			this.addValuePart(scope, activity.valueRenderer, jsonConfig, activity, activityPanel, setterCombo);
			paramPanels.push(activityPanel);
		}, this);
		return paramPanels;
	},


	/**
	 * Reload the value part of a bulk expression after changing the setter
	 * or after changing any or the composite select part
	 */
	changeSetter: function(combo, records, options) {
		var activityExpression = options.activityExpression;
		var workflowActivityID = activityExpression.workflowActivityID;
		var activityType = activityExpression.activityType;
		var valueComponentName = "value" + workflowActivityID;
		var valueComponent = null;
		var value = null;
		if (activityExpression.jsonConfig) {
			//valueComponentName = activityExpression.jsonConfig.name;
			if (valueComponentName) {
				valueComponent = options.panel.getComponent(valueComponentName);
				if (valueComponent) {
					value = valueComponent.getSetterValue();
				}
			}
		}
		var setterID = combo.getValue();
		params = new Object();
		params["activityParams"] = value;
		params["activityTypeID"] = activityType;
		params["setterID"] = setterID;
		params["node"] = this.getNode();
		Ext.Ajax.request({
			url: "workflowActivityConfig!changeSetter.action",
			params: params,
			scope: this,
			disableCaching:true,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				if (valueComponent) {
					//the value will not be removed from Ext.form.Basic only from the UI
					//workaround: set the fields to disabled to not to be submitted then remove the component
					valueComponent.setDisabled(true);
					options.panel.remove(valueComponent, true);
				}
				responseJson.jsonConfig.name = valueComponentName;
				if (responseJson.valueRenderer && responseJson.valueRenderer!=="") {
					activityExpression.valueRenderer = responseJson.valueRenderer;
					this.addValuePart(this, responseJson.valueRenderer, responseJson.jsonConfig,
							activityExpression, options.panel, combo);
				}
			},
			failure: function(result){
				Ext.MessageBox.show({
					title: this.failureTitle,
					msg: result.responseText,
					buttons: Ext.Msg.OK,
					icon: Ext.MessageBox.WARNING
				});
			},
			method:"POST"
		});
	},

	/**
	 * Configures and adds the value part (the dynamic part) to the expressions by first rendering and after changing the setter or a cascade select part
	 */
	addValuePart: function(scope, valueRenderer, jsonConfig, activityExpression, activityPanel, setterRelationCombo) {
		if (activityExpression.valueRenderer && activityExpression.valueRenderer!=="") {
			if (jsonConfig) {
				//the jsonConfig coming from the server for each activityExpression or after a setter or cascade select change
				jsonConfig.scope = scope;
				jsonConfig.selectHandler = this.changeCompositePart;
				jsonConfig.paramExpression = activityExpression;
				jsonConfig.setterRelationCombo = setterRelationCombo;
				var valueControl = Ext.create(valueRenderer, {
					jsonData:jsonConfig,
					margin:'2 0 2 0'
				});
				activityPanel.add(valueControl);
			}
		}
	}
});

