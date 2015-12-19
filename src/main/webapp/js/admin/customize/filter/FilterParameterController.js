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

Ext.define("com.trackplus.admin.customize.filter.FilterParameterController", {
	extend: "Ext.app.ViewController",
	alias: "controller.filterParameter",
	mixins: {
		baseController: "com.trackplus.admin.WindowBaseController"
	},
	
	/**
	 * And instant filter can be executed directly
	 */
	onApply: function(button, event) {
		this.submitHandler(button, "savedFilterExecute!replaceSubmittedParameters.action", null, null, {standardSubmit:true});
	},
	
	onCancel: function(button, event) {
		this.cancelHandler(button);
   }
	
});
