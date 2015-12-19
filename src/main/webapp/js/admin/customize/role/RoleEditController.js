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


Ext.define("com.trackplus.admin.customize.role.RoleEditController",{
	extend: "Ext.app.ViewController",
	alias: "controller.roleEdit",
	mixins: {
		baseController: "com.trackplus.admin.WindowBaseController"
	},
	
	onSave: function(button, event) {
		this.submitHandler(button, "roleView!save.action");
	},
	
	onCancel: function(button, event) {
		this.cancelHandler(button);
	},
	
	preSubmitProcess : function(submitUrlParams, panel) {
	    var NUMBER_OF_ACCESS_FLAGS = 21;
	    var accessFlags = new Array(21);
	    for (var i = 0; i < accessFlags.length; i++) {
	        accessFlags[i] = 0;
	    }
	    var checkList = this.getView().checkList;
	    if (checkList) {

	        for (var i = 0; i < checkList.length; i++) {
	            var index = checkList[i].getName().substring(1);
	            if (checkList[i].getRawValue() === true) {
	                accessFlags[index] = 1;
	                // extendedAccessKey+='1';
	            } else {
	                accessFlags[index] = 0;
	                // extendedAccessKey+='0';
	            }
	        }
	    }
	    var extendedAccessKey = "";
	    for (var i = 0; i < accessFlags.length; i++) {
	        if (accessFlags[i] === 1) {
	            extendedAccessKey += '1';
	        } else {
	            extendedAccessKey += '0';
	        }
	    }
	    submitUrlParams['extendedAccessKey'] = extendedAccessKey;
	    return submitUrlParams;
	},
	
});
