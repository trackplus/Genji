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


Ext.define("com.trackplus.admin.NotifyTriggerEditController",{
	extend: "Ext.app.ViewController",
	alias: "controller.notifyTriggerEdit",
	mixins: {
		baseController: "com.trackplus.admin.WindowBaseController"
	},

	
	/**
	 * Not a form panel submit
	 */
	onSave : function(button, event) {
	    var labelTextField = this.getView().formPanel.getComponent('label');
	    if (!labelTextField.validate()) {
		    return;
	    }
	    var records = this.getView().formStore.getUpdatedRecords();
	    if (records  && records.length > 0) {
		    // at least a record was changed
		    // the label might be changed or not
		    this.getView().formStore.sync();
	    } else {
		    if (labelTextField.isDirty()) {
			    // var params = this.getSaveParams(recordData, type);
			    // add the current label to submitUrlParams
		    	submitUrlParams = this.getView().getSubmitUrlParams();
			    submitUrlParams["label"] = labelTextField.getValue();
			    //submitUrlParams["defaultSettings"] = this.defaultSettings;
			    // no record but the label was changed
			    Ext.Ajax.request({
			        url : "notifyTrigger!save.action",
			        params : submitUrlParams,
			        scope : this,
			        success : function(response) {
				        var responseJson = Ext.decode(response.responseText);
				        this.getView().getRefreshAfterSubmitHandler().call(this.getView().getCallerScope(),{
					        rowToSelect : responseJson.id
				        });
				        this.getView().close();
			        },
			        failure : function(result) {
				        Ext.MessageBox.alert(this.failureTitle, result.responseText);
			        }
			    });
		    }
	    }
	   
	},

	
	
	onCancel: function(button, event) {
		this.cancelHandler(button);
	},
	
	onSelectionchange: function(model, selected, eOpts) {
        this.changeEntireRowSelection(this.peviousSelectedRows, false);
        this.changeEntireRowSelection(selected, true);
        this.peviousSelectedRows = selected;

    },
	
	/**
	 * This method set each row each checkbox checked /true or false/ In case of
	 * setting false: if header checkbox checked then actual checkbox remains
	 * checked otherwise bill be unchecked
	 */
	changeEntireRowSelection: function(rows, checked) {
		if (rows) {
		    for (i = 0; i < rows.length; i++) {
			    var record = rows[i];
			    for (var j = 1; j < 7; j++) {
				    if (!checked) {
					    var column = this.getView().getColumnByIndex(j);
					    var checkBox = document.getElementById(column);
					    if (!checkBox.checked) {
						    record.set(this.getView().getColumnByIndex(j), checked);
					    }
				    } else {
					    record.set(this.getView().getColumnByIndex(j), checked);
				    }
	
			    }
		    }
		}
	},
	
	headerChkHandler : function(event, chkBox, options) {
	    var checked = chkBox.checked;
	    var numberOfRecords = this.formStore.getTotalCount();
	    //var column = options.column;
	    var column = chBox.getId();
	    for (i = 0; i < numberOfRecords; i++) {
		    var record = this.formStore.getAt(i);
		    record.set(column, checked);
	    }
	}
	
});
