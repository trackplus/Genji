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

Ext.define("js.ext.com.track.matcher.itemMatcher", {
	extend: "Ext.panel.Panel",
	layout: {
		type: 'hbox'
	},
	border:false,
	config: {
		jsonData: {}
	},
	initComponent: function() {
		this.name = this.jsonData.name;
		this.callParent();
		this.add(CWHF.createHiddenField(this.name,
				{disabled: this.jsonData.disabled,
				value: this.jsonData.value}));
        this.add(CWHF.createTextField(null, "txtIssueNo",
				{itemId:'txtIssueNo',
        		 disabled: this.jsonData.disabled,
                readOnly: true,
				width: 100,
				value: this.jsonData.txtIssueNo}));
        this.add(CWHF.createTextField(null, "txtIssueTitle",
                {itemId:'txtIssueTitle',
        		 disabled: this.jsonData.disabled,
                 readOnly: true,
                 margin: '0 5 0 5',
                 width: 300,
                 value: this.jsonData.txtIssueTitle}));
		this.add({
            xtype: 'button',
            text: getText('common.btn.search'),
            disabled: this.jsonData.disabled,
            scope: this,
            handler:function(){
                this.chooseItem.call(this);
            }
        })
	},

	chooseItem: function() {
		var issuePicker=Ext.create('com.trackplus.util.IssuePicker',{
			title:getText("common.btn.chooseParent"),
			handler:this.setItem,
			scope:this
		});
		issuePicker.showDialog();
	},

	setItem:function(item){
        var hiddenIssueObjectID = this.getComponent(this.name);
        if (hiddenIssueObjectID) {
            hiddenIssueObjectID.setValue(item["objectID"]);
        }
        var txtIssueNo = this.getComponent("txtIssueNo");
        if (txtIssueNo) {
            txtIssueNo.setValue(item["id"]);
        }
        var txtIssueTitle=this.getComponent("txtIssueTitle");
        if (txtIssueTitle) {
            txtIssueTitle.setValue(item["title"]);
        }
	},

	getStringValue: function() {
		var issueIDField = this.getComponent(this.name);
		return issueIDField.getValue();
	}

});
