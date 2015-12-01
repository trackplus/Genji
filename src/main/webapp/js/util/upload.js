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


Ext.define('util.Upload',{
	extend:'Ext.Base',
	singleton:true,
	constructor: function(config) {
	},

	validateUpload: function(submit) {
		var theForm = this.formEdit.getForm();
		if (!theForm.isValid()) {
			Ext.MessageBox.show({
				title: getText("common.lbl.import"),
				msg: getText("common.err.fileNotSpecified"),
			    buttons: Ext.Msg.OK,
			    icon: Ext.MessageBox.ERROR
			});
			return false;
		}
		var uploadFile = this.formEdit.getComponent("uploadFile");

		var expectedFileType = submit.expectedFileType;
		if (expectedFileType) {
			if (!expectedFileType.test(uploadFile.getRawValue())) {
				Ext.MessageBox.alert(this.getTitle("common.lbl.import"),
					getText("common.err.fileExpectedType", submit.expectedFileType));
				return false;
			}
		}
	}


},

/**
 * Global shortcut accessor for upload util methods
 */
function() {
   Upload = this;
})
