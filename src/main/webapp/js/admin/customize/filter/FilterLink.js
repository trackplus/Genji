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

Ext.define("com.trackplus.admin.customize.filter.FilterLink", {
	extend: "com.trackplus.admin.WindowBase",
	xtype: "filterLink",
    controller: "filterLink",
    //scope for the static filter operations 
   
	width: 500,
    height: 270,
    
    initTitle: function() {
    	return getText("admin.customize.queryFilter.lbl.parameters");
    },
    
    loadUrl: "filterLink.action",
    
    initTitle: function() {
    	return getText("admin.customize.queryFilter.lbl.filterURL.report.encodedUrl");
    },
    
    initActions: function() {
		this.actionDone = CWHF.createAction(this.getDoneButtonKey(), this.getCancelIconCls(), "onDone");
    	this.actions = [this.actionDone];
    },
    
    /**
     * Get the panel items
     * Empty at the beginning: add the fields dynamically in postDataProcess 
     */
    getFormFields: function() {
    	return [
			CWHF.getRadioGroup(null, 400,[{ boxLabel: getText("admin.customize.queryFilter.lbl.filterURL.reportOverviewLink"),
						name: 'urlType', inputValue: '1', checked: true },
					{ boxLabel: getText("admin.customize.queryFilter.lbl.filterURL.changesMavenPluginLink"),
						name: 'urlType', inputValue: '2', checked: false}],{itemId:'urlTypeRadio'}),
			CWHF.createCheckbox(
				"admin.customize.queryFilter.lbl.filterURL.encodeUserPassword",
				"encodePassword",
				{itemId:'encodePassword',
					disabled:false,
					checked:true,
					labelWidth:200}),
			CWHF.createCheckbox(
					"admin.customize.queryFilter.lbl.filterURL.keepMeLogged",
					"keepMeLogged",
					{itemId:'keepMeLogged',
					disabled:false,
					labelWidth:200}),
			CWHF.createTextAreaField(null, "filterLinkTextArea",{
				itemId: "filterLinkTextArea",
				anchor:'100% -80',
				listeners:{
					'render':{
						fn:function (comp) {
							comp.getEl().on('contextmenu', function(e){e.stopPropagation();}, null, {preventDefault: false});
						}
					}
				}
			})
		];
    },
    
    postDataProcess: function(data, panel) {
    	this.resultData = data;
		var filterLinkTextArea = panel.getComponent("filterLinkTextArea");
		if (data["filterParams"]) {
			filterLinkTextArea.anchor='100% -70';
			panel.insert(1, CWHF.createCheckbox(
						"admin.customize.queryFilter.lbl.filterURL.showParameters",
						"showParameters",
						{itemId:'showParameters',
						labelWidth:200},
						{change: "changeLinkType"}));

		}
		var urlType = panel.getComponent("urlTypeRadio");
		if (urlType) {
			urlType.on("change", "changeLinkType");
		}
		var keepMeLogged = panel.getComponent("keepMeLogged");
		if (keepMeLogged) {
			keepMeLogged.on("change", "changeLinkType");
		}
		var encodePassword= panel.getComponent("encodePassword");
		if (encodePassword) {
			encodePassword.on("change", "changeEncodePassword");
		}
		filterLinkTextArea.setValue(data["filterUrlIssueNavigator"]);
    }
});
