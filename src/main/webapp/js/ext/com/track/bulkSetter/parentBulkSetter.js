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

Ext.define("js.ext.com.track.bulkSetter.parentBulkSetter", {
	extend: "Ext.panel.Panel",
	layout: {
		type: 'hbox'
	},
	border:false,
	config: {
		jsonData: {}
	},
	initComponent: function() {
		this.callParent();
		this.add(CWHF.createHiddenField(this.jsonData.name, {itemId:"objectID", disabled:this.jsonData.disabled}));
        this.add(CWHF.createTextField(null, "txtIssueNo",
				{itemId:'txtIssueNo',
        		 disabled:this.jsonData.disabled,
                 readOnly :true,
				width:100}));
        this.add(CWHF.createTextField(null, "txtIssueTitle",
                {itemId:'txtIssueTitle',
        		 disabled: this.jsonData.disabled,
                 readOnly: true,
                 margin: '0 5 0 5',
                 width:300}));
		this.add({
            xtype: 'button',
            text: getText('common.btn.search'),
            disabled: this.jsonData.disabled,
            scope: this,
            handler:function(){
                this.chooseParent.call(this);
            }
        })
	},

	chooseParent: function() {
		var issuePicker=Ext.create('com.trackplus.util.IssuePicker',{
			title:getText("common.btn.chooseParent"),
			handler:this.setParent,
			scope:this
		});
		issuePicker.showDialog();
	},

	setParent:function(item){
		var hiddenIssueObjectID = this.getComponent("objectID");
        if (hiddenIssueObjectID) {
            hiddenIssueObjectID.setValue(item["objectID"]);
        }
        var txtIssueNo = this.getComponent("txtIssueNo");
        if (txtIssueNo) {
            txtIssueNo.setValue(item["id"]);
        }
        var txtIssueTitle=this.getComponent("txtIssueTitle");
        if (txtIssueTitle) {
            txtIssueTitle.setValue(item["title"])
        }
	},

	/**
	 * Get the field value as a json
	 */
	getFieldValueJson: function() {
		var valueJson = new Object();
		var issueIDField = this.getComponent("objectID");
		if (issueIDField) {
			valueJson[this.name] = issueIDField.getValue();
		}
		return valueJson;
	},

	setDisabled: function(disabled) {
		for (var i=0;i<this.items.getCount();i++) {
			var item = this.items.getAt(i);
			item.setDisabled(disabled);
		}
	}
});
