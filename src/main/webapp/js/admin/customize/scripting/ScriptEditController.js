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


Ext.define("com.trackplus.admin.customize.script.ScriptEditController",{
	extend: "Ext.app.ViewController",
	alias: "controller.scriptEdit",
	mixins: {
		baseController: "com.trackplus.admin.WindowBaseController"
	},
	
	onCompile: function(button, event) {
		this.submitHandler(button, "script!compile.action", null, {closeAfterSubmit:false}, null, this.showCompileResult);
	},
	
	onSave: function(button, event) {
		this.submitHandler(button, "script!save.action");
	},
	
	onCancel: function(button, event) {
		this.cancelHandler(button);
	},
	
	/**
	 * Whether the filter contains parameter(s)
	 */
	showCompileResult: function(refreshParameters, result) {
		Ext.MessageBox.show({
			title: result.title,
			msg: result.message,
			buttons: Ext.Msg.OK,
			icon: Ext.MessageBox.INFO
		});
	},
		
	/**
	 * Handler for changing a default value
	 */
	onScriptTypeChange: function(combo, records, options) {
	    var scriptType = combo.getValue();
	    var toolbars = this.getView().getDockedItems('toolbar[dock="bottom"]');
	    if (toolbars) {
	        toolbars[0].getComponent(0).setDisabled(this.getView().compileIsDisabled(scriptType));
	    }
	}
	
});
