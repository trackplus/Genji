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

Ext.define("com.trackplus.admin.customize.list.IconUpload", {
	extend: "com.trackplus.admin.WindowBase",
	xtype: "iconUpload",
    controller: "iconUpload",
    
    width: 550,
    height: 180,
    fieldLabelWidth: 180,
    
    loadUrl: "listOptionIcon.action",
	
    initTitle: function() {
    	return getText(this.getUploadTitleKey(), getText("admin.customize.list.lbl.icon"));
    },
    
    initActions: function() {
    	this.actionUpload = CWHF.createAction(this.getUploadButtonKey(), this.getUploadIconCls(),
			"onUpload", {tooltip:getText(this.getUploadTitleKey(), getText("admin.customize.list.lbl.iconOp")), disabled:true});
    	this.actionDelete = CWHF.createAction(this.getDeleteButtonKey(), this.getDeleteIconCls(),
			"onDelete", {tooltip:getText(this.getDeleteTitleKey(), getText("admin.customize.list.lbl.iconOp")), disabled:true});
    	this.actionDone = CWHF.createAction(this.getDoneButtonKey(), this.getCancelIconCls(), "onDone");
    	this.actions = [this.actionUpload, this.actionDelete, this.actionDone];
    },
    
    getFormFields: function() {
    	var icon = Ext.create("Ext.Img", {
            src : Ext.BLANK_IMAGE_URL,
            width : 16,
            height : 16,
            itemId : "actualIcon"
        });
        var iconWrapper = Ext.create("Ext.form.FieldContainer", {
            combineErrors : true,
            itemId : "iconPanel",
            fieldLabel : getText("admin.customize.list.lbl.icon"),
            labelWidth : this.fieldLabelWidth,
            labelAlign : "right",
            labelStyle : {
                overflow : "hidden"
            },
            layout : "hbox",
            items : [icon]
        });
        return [iconWrapper,
                CWHF.createLabelComponent("admin.customize.list.lbl.iconName", "actualIconName", {
                    labelWidth : this.fieldLabelWidth, itemId:"actualIconName"}),
                CWHF.createFileField("admin.customize.list.lbl.newIcon", "iconFile", {
                    allowBlank : false,
                    labelWidth : this.fieldLabelWidth,
                    itemId: "iconFile"}, {change:"newIconFileChanged"})];
    },
    
    postDataProcess: function(data, formPanel, fromTree) {
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
        var toolbars = this.getDockedItems("toolbar[dock='bottom']");
        if (toolbars ) {
			toolbars[0].getComponent(0).setDisabled(true);
            // disable delete button if no icon is specified
            toolbars[0].getComponent(1).setDisabled(CWHF.isNull(data.iconName) || data.iconName === "");
        }
    }
    
});
