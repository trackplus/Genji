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

Ext.define("com.trackplus.itemNavigator.Grouping",{
	extend:"Ext.Base",
	config: {
		filterType: null,
		filterID: null,
		itemNavigatorController: null
	},
	constructor : function(cfg) {
		var config = cfg || {};
		this.initialConfig = config;
		Ext.apply(this, config);
	},
	showDialog: function() {
		var width = 350;
		var height = 460;
		var loadParams = {filterType:this.filterType, filterID:this.filterID};
		var load = {loadUrl:"layoutGrouping.action", loadUrlParams:loadParams};
		var submitParams = {filterType:this.filterType, filterID:this.filterID};
		var submit = {submitUrl:"layoutGrouping!save.action",
					submitUrlParams:submitParams,
					refreshAfterSubmitHandler:this.reload, loading:true};
		var windowParameters = {title:getText("itemov.lbl.groupBy"),
			width:width,
			height:height,
			load:load, submit:submit,
			items:[],
			postDataProcess:this.createGroupingItems,
			panelConfig:{
				bodyStyle:{
					padding:'0 0 0 10'
				}
			}
		};
		var windowConfig = Ext.create("com.trackplus.util.WindowConfig", windowParameters);
		windowConfig.showWindowByConfig(this);
	},

	/**
	 * Refreshes a grid's store and selects a row by rowToSelect parameter if it is specified
	 * Called from selectAfterReload() (grid and tree refresh) and from simple grid CRUD
	 */
	reload: function() {
		this.itemNavigatorController.changeLayout.call(this.itemNavigatorController);
	},

	createGroupingItems: function(data, panel){
		var me=this;
		var groupFields=data.selectedGroupFields;
		var groupFieldList=data.groupFieldList;
		var ascendingDescendingList=data.ascendingDescendingList;
		var expandCollapseList=data.expandCollapseList;
		var maxGroupingLevels=groupFields.length;
		var items=[];
		var activeSet = false;
		var previousActiveSet = true;
		for(var i=0;i<groupFields.length;i++) {
			activeSet = groupFields[i].fieldID!=null;
			items.push(me.createGroupingItem(i, maxGroupingLevels, panel, groupFields[i], groupFieldList, ascendingDescendingList, expandCollapseList, activeSet, previousActiveSet));
			previousActiveSet = activeSet;
		}
		panel.add(items);
	},

	createGroupingItem: function(i, count, mainPanel, groupField, groupFieldList, ascendingDescendingList, expandCollapseList, activeSet, previousActiveSet){
		var me=this;
		var itemIDPrefix =  "groupFields["+i+"].";
		var checkBox = CWHF.createCheckbox(null, "activated"+i,
				{value:activeSet, disabled:!previousActiveSet, boxLabel:getText("itemov.lbl.groupBy.active")},
				{change: {fn: me.activate,
					scope:me, index:i, count:count, mainPanel: mainPanel}});
		var fieldCombo = CWHF.createCombo(null, itemIDPrefix + "fieldID", {value: groupField.fieldID, data:groupFieldList, disabled:!activeSet, anchor:'100%'});
		var orderRadioButtonItems = CWHF.getRadioButtonItems(ascendingDescendingList,
				itemIDPrefix + "descending", "id", "label", groupField.descending, false, true);
		var orderRadioGroup = CWHF.getRadioGroup(itemIDPrefix + "descending", null, null, orderRadioButtonItems, {anchor:'100%', disabled:!activeSet});
		var expandingRadioButtonItems = CWHF.getRadioButtonItems(expandCollapseList,
				itemIDPrefix + "collapsed", "id", "label", groupField.collapsed, false, true);
		var expandingRadioGroup = CWHF.getRadioGroup(itemIDPrefix + "collapsed", null, null, expandingRadioButtonItems, {anchor:'100%', disabled:!activeSet});
		return Ext.create("Ext.panel.Panel",{
			margin:'5 0 5 0',
			border:true,
			itemId: "panel" + i,
			bodyBorder:false,
			bodyStyle:{
				padding:'5px 5px 2px 10px'
			},
			anchor:'100%',
			layout:'column',
			items:[
				{
					xtype: 'fieldcontainer',
					border:false,
					fieldLabel: '<B>'+(i+1)+'</B>',
					labelSeparator:'.',
					labelWidth:15,
					width:90,
					items:[checkBox]
				},{
					columnWidth: 1,
					border:false,
					bodyBorder:false,
					layout:'anchor',
					items:[fieldCombo,
					       orderRadioGroup,
					       expandingRadioGroup]
				}
			]
		});
	},

	activate: function(checkboxField, newValue, oldValue, options) {
		var index = options.index;
		var count = options.count;
		var mainPanel = options.mainPanel;
		var indexPanel = mainPanel.getComponent("panel" + index);
		var itemIDPrefix =  "groupFields["+index+"].";
		var checkBoxActivated = indexPanel.getComponent(0).getComponent("activated"+index);
		var activated = checkBoxActivated.getRawValue();
		var selectField = indexPanel.getComponent(1).getComponent(itemIDPrefix+"fieldID");
		selectField.setDisabled(!activated);
		var descending = indexPanel.getComponent(1).getComponent(itemIDPrefix+"descending");
		descending.setDisabled(!activated);
		var collapsed = indexPanel.getComponent(1).getComponent(itemIDPrefix+"collapsed");
		collapsed.setDisabled(!activated);
		if (index<count-1) {
			if (activated) {
				//activate the next grouping level's checkbox
				var nextIndex = index+1;
				var nextPanel = mainPanel.getComponent("panel" + nextIndex);
				var nextCheckBox = nextPanel.getComponent(0).getComponent("activated"+nextIndex);
				nextCheckBox.setDisabled(false);
			} else {
				for (var i=index+1;i<count;i++) {
					var nextPanel = mainPanel.getComponent("panel" + i);
					var nextIndexPrefix =  "groupFields["+i+"].";
					var selectField = nextPanel.getComponent(1).getComponent(nextIndexPrefix+"fieldID");
					selectField.setDisabled(true);
					var descending = nextPanel.getComponent(1).getComponent(nextIndexPrefix+"descending");
					descending.setDisabled(true);
					var collapsed = nextPanel.getComponent(1).getComponent(nextIndexPrefix+"collapsed");
					collapsed.setDisabled(true);
					var nextCheckBoxActivated = nextPanel.getComponent(0).getComponent("activated"+i);
					nextCheckBoxActivated.setDisabled(true);
					nextCheckBoxActivated.setRawValue(false);
				}
			}
		}
	}
});
