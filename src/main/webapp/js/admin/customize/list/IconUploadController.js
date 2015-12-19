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

Ext.define("com.trackplus.admin.customize.list.IconUploadController", {
	extend: "Ext.app.ViewController",
	alias: "controller.iconUpload",
	mixins: {
		baseController: "com.trackplus.admin.WindowBaseController"
	},
	
	newIconFileChanged:function(){
		var toolbars = this.getView().getDockedItems("toolbar[dock='bottom']");
		if (toolbars) {
			// disable delete button if no icon is specified
			toolbars[0].getComponent(0).setDisabled(false);
		}
	},
	
	validateFileExtension : function(fileName) {
        var exp = /^.*\.(jpg|JPG|png|PNG|gif|GIF)$/;
        return exp.test(fileName);
    },
	
	onUpload: function(button, event) {
		var theForm = this.getView().formPanel;
		//var theForm = this.formEdit.getForm();
        if (!theForm.isValid()) {
            Ext.MessageBox.alert(this.getView().getTitle(), getText("admin.customize.list.lbl.iconFileNotSpecified"));
            return;
        }
        var iconFile = theForm.getComponent("iconFile");
        if (!this.validateFileExtension(iconFile.getRawValue())) {
            Ext.MessageBox.alert(this.getView().getTitle(), getText("admin.customize.list.lbl.iconFileWrongType"));
            return;
        }
        theForm.submit({
            scope : this,
            url: "listOptionIcon!upload.action",
            params: this.getView().getSubmitUrlParams(),
            success: function(form, action) {
                this.renderUploadPostDataProcess(action.result.data, theForm);
                var callerScope = this.getView().getCallerScope();
                callerScope.reload.call(callerScope, this.getView().getRefreshParametersBeforeSubmit());
                //this.refreshHandler(this.getView().getCallerScope());
            },
            failure: function(form, action) {
                com.trackplus.util.submitFailureHandler(form, action);
            }
        });
	},

    renderUploadPostDataProcess : function(data, formPanel) {
        var actualIcon = formPanel.getComponent("iconPanel").getComponent("actualIcon");
        if (actualIcon ) {
            actualIcon.setSrc(data.icon);
            // actualIcon.doComponentLayout();
        }
        actualIconName = formPanel.getComponent("actualIconName");
        if (actualIconName ) {
            actualIconName.setValue(data["iconName"]);
        }
        // Ext.getDom("pic").src = data.icon;
        /*
		 * if (data["iconName"]) {
		 * formPanel.getComponent("actualIconName").setValue(data["iconName"]);
		 * formPanel.getComponent("actualIconName").setVisible(true); }
		 */
        formPanel.getComponent("iconFile").setValue("");
        var toolbars = this.getView().getDockedItems("toolbar[dock='bottom']");
        if (toolbars ) {
			toolbars[0].getComponent(0).setDisabled(true);
            // disable delete button if no icon is specified
            toolbars[0].getComponent(1).setDisabled(CWHF.isNull(data.iconName) || data.iconName === "");
        }
    },
    
    onDelete: function() {
    	var pictUploadForm = this.getView().formPanel;
		Ext.Ajax.request({
        	url: "listOptionIcon!delete.action",
            params: this.getView().getSubmitUrlParams(),
            scope : this,
            success : function(response) {
                var result = Ext.decode(response.responseText);
                /*
				 * Ext.getDom('pic').src = "";
				 * pictUploadForm.getComponent('actualIconName').setValue("");
				 * pictUploadForm.getComponent('iconFile').setRawValue('');
				 */
                this.renderUploadPostDataProcess(result.data, pictUploadForm);
                var callerScope = this.getView().getCallerScope();
                callerScope.reload.call(callerScope, this.getView().getRefreshParametersBeforeSubmit());
            },
            failure: function(form, action) {
                com.trackplus.util.submitFailureHandler(form, action);
            }
        });    
    },
    
    onDone: function(button, event) {
    	 this.cancelHandler(button);
    }
    
});
