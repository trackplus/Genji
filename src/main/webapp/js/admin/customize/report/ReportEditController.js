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

Ext.define("com.trackplus.admin.customize.filter.ReportEditController", {
	extend: "Ext.app.ViewController",
	alias: "controller.reportEdit",
	mixins: {
		baseController: "com.trackplus.admin.WindowBaseController"
	},
		
	onSave:function(button, event) {
		var submitUrl = null;
		if (this.getView().isLeaf) {
			if (this.getView().isAddOperation) {
				this.onUpload(button, event);
			} else {
				this.submitHandler(button,  "reportConfig!save.action");	
			}
			
		} else {
			this.submitHandler(button,  "categoryConfig!save.action");	
		}
		if (this.getView().isAddOperation) {
			//file upload
		} else {
			//rename report
			this.submitHandler(button, submitUrl);
		}
	},
	
	validateFileExtension : function(fileName) {
        var exp = /^.*\.(zip)$/;
        return exp.test(fileName);
    },
	
    onUpload: function(button, event) {
		var theForm = this.getView().formPanel;
	    /*if (!theForm.isValid()) {
            Ext.MessageBox.alert(this.getView().getTitle(), getText("common.err.fileNotSpecified"));
            return;
        }*/
        var reportFile = theForm.getComponent("reportFile");
        if (!this.validateFileExtension(reportFile.getRawValue())) {
            Ext.MessageBox.alert(this.getView().getTitle(), getText("common.err.fileExpectedType", "zip"));
            return;
        }
        theForm.submit({
            scope : this,
            url: "reportConfig!save.action",
            params: this.getView().getSubmitUrlParams(),
            success: function(form, action) {
                var callerScope = this.getView().getCallerScope();
                callerScope.reload.call(callerScope, this.getView().getRefreshParametersBeforeSubmit());
                this.getView().close();
            },
            failure: function(form, action) {
                com.trackplus.util.submitFailureHandler(form, action);
            }
        });
	},
	
	onCancel: function(button, event) {
		this.cancelHandler(button);
   }
	
});
