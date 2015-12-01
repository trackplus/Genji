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

Ext.define('com.trackplus.issue.Link',{
	extend:'Ext.Base',
	config: {
        itemNavigatorController: null,
		workItemIDs: null,
		fromGantt: false,
		ganttController: null,
		dependencyType: null,
		crossProject: null
	},
	constructor: function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		me.initConfig(config);
	},

	/**
	 * Executes a report from issue navigator: first choose report then render configuration (if needed)
	 */
	addLinkFromIssueNavigator:function() {
		var me = this;
		var load = {loadUrl:"itemLink!editItemLink.action", loadUrlParams: {fromGantt:this.fromGantt}};
		var submitUrlParams = {workItemIDs: this.workItemIDs, fromGantt: this.fromGantt, crossProject: this.crossProject};
		var submit = null;
		if(me.ganttController ) {
			submit = {submitHandler: me.ganttSpecialSubmitHandler, refreshAfterSubmitHandler: this.refreshAfterSubmitOrCancelHandler, submitButtonText:getText('common.btn.save'), submitUrlParams:submitUrlParams};
		}else {
			submit = {specialSubmitFailHandler:this.submitFailHandler, refreshAfterSubmitHandler:this.refresh, submitUrl:"itemLink!saveLinkFromIssueNavigator.action", submitUrlParams:submitUrlParams,
					submitButtonText:getText('common.btn.save')};
		}
		var windowParameters = {title:getText("itemov.link.title"),
				width:600,
				height:300,
				load:load,
				submit: submit,
				items: this.getLinkPanelItems(),
//				isFormPanel:false,
				refreshAfterCancel: true,
				postDataProcess:this.postDataLoadCombos};
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
		this.windowConfig = windowConfig;
		windowConfig.showWindowByConfig(this);

	},
	refreshAfterSubmitOrCancelHandler:function() {
		var me = this;
		if(me.fromGantt === true) {
			me.ganttController.removeLastAddedDependency();
		}
	},
	refresh:function(scope, result, submit) {
		var me = this;
		if (this.itemNavigatorController) {
			this.itemNavigatorController.refresh.call(this.itemNavigatorController);
		}
	},
	loadFailHandler:function() {
		var me = this;
		if(me.fromGantt === true) {
			me.ganttController.removeLastAddedDependency();
		}
	},

	ganttSpecialSubmitHandler: function() {
		var me = this;
		var formValues = this.windowConfig.formPanel.getValues();
		var newLinkProperties = {};
		newLinkProperties.description = formValues.description;
		newLinkProperties.linkTypeWithDirection = formValues.linkTypeWithDirection;
		newLinkProperties.linkType = formValues["parametersMap['DependencyType']"];
		newLinkProperties.lag = formValues["parametersMap['Lag']"];
		newLinkProperties.lagUnit = formValues["parametersMap['Lagformat']"];
		me.ganttController.adjustNewlyCreatedLink(newLinkProperties);
		this.windowConfig.win.close();
	},

	submitFailHandler:function(result) {
		var me = this;
		if(me.fromGantt === true) {
			me.ganttController.removeLastAddedDependency();
		}
	},

	postDataLoadCombos: function(data, panel) {
		var linkTypeWithDirection = panel.getComponent("linkTypeWithDirection");
		if (linkTypeWithDirection) {
			linkTypeWithDirection.store.loadData(data["linkTypesList"]);
			linkTypeWithDirection.setValue(data["linkTypeWithDirection"]);
		}
        this.replaceSpecificPart(panel, data["linkTypeJSClass"], data["specificData"]);
        if(this.dependencyType ) {
        	var dependencyCombo = panel.getComponent("specificPart");
        	dependencyCombo.items.items[0].setValue(this.getDependencyComboValueFromGanttValue(this.dependencyType));
        	dependencyCombo.items.items[0].setReadOnly(true);
        }
        if(data.linkTypesList.length === 0) {
        	this.loadFailHandler();
        }
	},

	getDependencyComboValueFromGanttValue: function(type) {
		switch(type) {
			case 0:
				return 3;
				break;
			case 1:
				return 2;
				break;
			case 2:
				return 1;
				break;
			case 3:
				return 0;
				break;
			default:
				return 0;
		}
	},

	getLinkPanelItems:function(){
		var linkTypeCombo = CWHF.createCombo("itemov.link.type", "linkTypeWithDirection",
				{labelWidth:150, anchor:'100%', idType:"string", allowBlank:false}, {select:{fn:this.selectLinkType, scope:this}});
		var descriptionText = CWHF.createTextAreaField("item.tabs.itemLink.lbl.comment", "description",
				{height:125, anchor:'100%', labelWidth:150});
		return [linkTypeCombo, descriptionText];
	},

	selectLinkType: function(combo, records, options) {
		Ext.Ajax.request({
			url:"itemLink!getSpecificPart.action",
			params:{"linkTypeWithDirection":combo.getValue()},
			disableCaching:true,
			scope: this,
			success: function(response, opts){
				var responseJson = Ext.decode(response.responseText);
				this.replaceSpecificPart(combo.ownerCt, responseJson["linkTypeJSClass"], responseJson["specificData"]);
			},
			failure: function(response, opts) {
			},
			method:'POST'
		});
	},

	/**
	 * Add the field type specific configuration
	 */
	replaceSpecificPart: function(panel, specificLinkTypeClass, specificData) {
		var specificPart = panel.getComponent("specificPart");
		if (specificPart) {
            specificPart.setDisabled(true);
			panel.remove(specificPart, true);
		}
		if (specificLinkTypeClass) {
			var specificPart = Ext.create(specificLinkTypeClass,{
				margin:'0 5 5 0'
			});
			if (specificPart) {
				panel.insert(1, specificPart);
				specificData["labelWidth"] = 100;
				specificPart.onDataReady(specificData);
				panel.updateLayout();
			}
		}
	}
});
