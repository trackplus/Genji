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
		ganttAction: null,
		ganttController: null,
		crossProject: null,
		linkProperties: null
	},

	GANTT_ACTION: {
		NEW: 1,
		EDIT: 2,
		NEW_EDITED: 3
	},

	constructor: function(config) {
		var me = this;
		var cfg = config || {};
		me.initConfig(cfg);
	},

	/**
	 * Executes a report from issue navigator: first choose report then render configuration (if needed)
	 */
	addLinkFromIssueNavigator:function() {
		var me = this;
		var load = {loadUrl:"itemLink!editItemLink.action", loadUrlParams: me.getFormLoadParams()};
		var windowParameters = {title:getText("itemov.link.title"),
				width:600,
				height:300,
				load:load,
				submit: me.getFormSubmitParams(),
				items: this.getLinkPanelItems(),
				refreshAfterCancel: true,
				postDataProcess:this.postDataLoadCombos};
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
		this.windowConfig = windowConfig;
		windowConfig.showWindowByConfig(this);

	},

	getFormLoadParams: function() {
		var me = this;
		var loadUrlParams = {};
		loadUrlParams.fromGantt = this.getFromGantt();
		if(me.getFromGantt() && me.getGanttAction()) {
			if(me.getGanttAction() === me.GANTT_ACTION.EDIT) {
				loadUrlParams.workItemID = this.getWorkItemIDs();
				loadUrlParams.linkID = me.getLinkProperties().Id;
			}
		}
		return loadUrlParams;
	},

	getFormSubmitParams: function() {
		var me = this;
		var submit = null;
		var submitUrlParams = {workItemIDs: this.getWorkItemIDs(), fromGantt: this.getFromGantt(), crossProject: this.getCrossProject()};
		if(me.getGanttAction()) {
			submit = {submitHandler: me.ganttSpecialSubmitHandler, refreshAfterSubmitHandler: this.refreshAfterSubmitOrCancelHandler, submitButtonText:getText('common.btn.save'), submitUrlParams:submitUrlParams};
		}else {
			submit = {specialSubmitFailHandler:this.submitFailHandler, refreshAfterSubmitHandler:this.refresh, submitUrl:"itemLink!saveLinkFromIssueNavigator.action", submitUrlParams:submitUrlParams,
					submitButtonText:getText('common.btn.save')};
		}
		return submit;
	},

	refreshAfterSubmitOrCancelHandler:function() {
		var me = this;
		if(me.getFromGantt() === true && me.getGanttAction() === me.GANTT_ACTION.NEW) {
			me.getGanttController().removeLastAddedDependency();
		}
	},
	refresh:function(scope, result, submit) {
		var me = this;
		if (this.getItemNavigatorController()) {
			this.getItemNavigatorController().refresh.call(this.getItemNavigatorController());
		}
	},
	loadFailHandler:function() {
		var me = this;
		if(me.getFromGantt() === true && me.getGanttAction() === me.GANTT_ACTION.NEW) {
			me.getGanttController().removeLastAddedDependency();
		}
	},

	ganttSpecialSubmitHandler: function() {
		var me = this;
		if(me.getGanttAction()) {
			var formValues = this.windowConfig.formPanel.getValues();
			var linkProperties = {};
			linkProperties.description = formValues.description;
			linkProperties.linkTypeWithDirection = formValues.linkTypeWithDirection;
			linkProperties.linkType = formValues["parametersMap['DependencyType']"];
			var ganttSpecificLinkType = me.getGanttController().convertTrackDepTypeToBryntum(linkProperties.linkType);
			linkProperties.lag = formValues["parametersMap['Lag']"];
			linkProperties.lagUnit = formValues["parametersMap['Lagformat']"];
			if(me.GANTT_ACTION.NEW === me.getGanttAction()) {
				linkProperties.linkID = Math.floor((Math.random() * 10) + 1000) * -1;
				me.getGanttController().adjustNewlyCreatedLink(linkProperties, ganttSpecificLinkType);
				this.windowConfig.win.close();
			}else {
				linkProperties.linkID = me.getLinkProperties().Id;
				me.getGanttController().adjustEditedLink(linkProperties, me.getLinkProperties().Id, ganttSpecificLinkType);
				this.windowConfig.win.close();
			}
		}
	},

	submitFailHandler:function(result) {
		var me = this;
		if(me.getFromGantt() === true && me.getGanttAction() === me.GANTT_ACTION.NEW) {
			me.getGanttController().removeLastAddedDependency();
		}
	},

	postDataLoadCombos: function(data, panel) {
		var me = this;
		var linkTypeWithDirection = panel.getComponent("linkTypeWithDirection");
		if (linkTypeWithDirection!=null) {
			linkTypeWithDirection.store.loadData(data["linkTypesList"]);
			linkTypeWithDirection.setValue(data["linkTypeWithDirection"]);
		}
		this.replaceSpecificPart(panel, data["linkTypeJSClass"], data["specificData"]);
		var specificPart = panel.getComponent("specificPart");
		if(me.getFromGantt()) {
			if(me.getGanttAction() === me.GANTT_ACTION.NEW) {
				specificPart.items.items[0].setValue(this.getGanttController().convertBryntumDepTypeToTrack(me.getLinkProperties().Type));
			}
			if(me.getGanttAction() === me.GANTT_ACTION.EDIT) {
				specificPart.items.items[0].setValue(this.getGanttController().convertBryntumDepTypeToTrack(me.getLinkProperties().Type));
				specificPart.items.items[1].setValue(me.getLinkProperties().Lag);
				specificPart.items.items[2].setValue(me.getGanttController().convertBryntumLagUnitToTrack(me.getLinkProperties().LagUnit));
			}
		}
	},

	getLinkPanelItems:function(){
		var linkTypeCombo = CWHF.createCombo("itemov.link.type", "linkTypeWithDirection",
				{itemId:"linkTypeWithDirection", labelWidth:150, anchor:'100%', idType:"string", allowBlank:false}, {select:{fn:this.selectLinkType, scope:this}});
		var descriptionText = CWHF.createTextAreaField("item.tabs.itemLink.lbl.comment", "description",
				{itemId: 'description', height:125, anchor:'100%', labelWidth:150});
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
