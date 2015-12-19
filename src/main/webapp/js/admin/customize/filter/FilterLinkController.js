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

Ext.define("com.trackplus.admin.customize.filter.FilterLinkController", {
	extend: "Ext.app.ViewController",
	alias: "controller.filterLink",
	mixins: {
		baseController: "com.trackplus.admin.WindowBaseController"
	},
	
	onDone: function(button, event) {
		this.cancelHandler(button);
   },
   
   changeLinkType: function(radioGroupOrCheckbox, newValue, oldValue, options) {
		var panel = this.getView().formPanel;
		var data = this.getView().resultData;
		if (panel) {
			var filterLinkValue = null;
			var keepMeLogged = panel.getComponent("keepMeLogged");
			var encodePassword = panel.getComponent("encodePassword");
			var urlType = panel.getComponent("urlTypeRadio");
			var checkedArr = urlType.getChecked();
			var filterUrlChecked = false;
			var checkedRadio;
			if (checkedArr.length>0) {
				checkedRadio = checkedArr[0];
				if (checkedRadio.getSubmitValue()===1) {
					filterUrlChecked = true;
					keepMeLogged.setDisabled(false);
					encodePassword.setDisabled(false);
					if(encodePassword.getValue()===false){
						filterLinkValue = data["filterUrlIssueNavigatorNoUser"];
					}else{
						if (keepMeLogged.getValue()) {
							filterLinkValue = data["filterUrlIssueNavigatorKeep"];
						} else{
							filterLinkValue = data["filterUrlIssueNavigator"];
						}
					}
				} else {
					filterLinkValue = data["filterUrlMavenPlugin"];
					keepMeLogged.setDisabled(true);
					encodePassword.setDisabled(true);
				}
			}
			var showParameters = panel.getComponent("showParameters");
			if (showParameters) {
				if (showParameters.getValue()) {
					filterLinkValue = filterLinkValue+"&"+data["filterParams"];
				}
			}
			var filterLinkTextArea = panel.getComponent("filterLinkTextArea");
			if (filterLinkTextArea) {
				filterLinkTextArea.setValue(filterLinkValue);
			}
			if (keepMeLogged.getValue() && filterUrlChecked) {
				filterLinkTextArea.inputEl.setStyle("color", "red");
			} else {
				filterLinkTextArea.inputEl.setStyle("color", "black");
			}
		}
	},
	
	changeEncodePassword: function(radioGroupOrCheckbox, newValue, oldValue, options) {
		var panel = this.getView().formPanel;
		var data = this.getView().resultData;
		var filterLinkValue = null;
		if (panel) {
			var keepMeLogged = panel.getComponent("keepMeLogged");
			var encodePassword = panel.getComponent("encodePassword");
			if(encodePassword.getValue()){
				keepMeLogged.setDisabled(false);
				if (keepMeLogged.getValue()) {
					filterLinkValue = data["filterUrlIssueNavigatorKeep"];
				} else{
					filterLinkValue = data["filterUrlIssueNavigator"];
				}
			}else{
				filterLinkValue = data["filterUrlIssueNavigatorNoUser"];
				keepMeLogged.setValue(false);
				keepMeLogged.setDisabled(true);
			}
			var filterLinkTextArea = panel.getComponent("filterLinkTextArea");
			if (filterLinkTextArea) {
				filterLinkTextArea.setValue(filterLinkValue);
			}
			if (keepMeLogged.getValue() && filterUrlChecked) {
				filterLinkTextArea.inputEl.setStyle("color", "red");
			} else {
				filterLinkTextArea.inputEl.setStyle("color", "black");
			}
		}
	}
});
