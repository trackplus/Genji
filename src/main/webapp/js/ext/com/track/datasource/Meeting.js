/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */

Ext.define("js.ext.com.track.datasource.Meeting",{
	extend: "Ext.form.Panel",
	border: false,
	items: [],
	autoScroll: true,
	config: {
		templateID: null,
		meeting: null,
		meetingName: null,
		meetingOptions: null,
		onlyActiveMeetings: true,
		onlyActiveMeetingsName: null,
		onlyNotClosedChildren: true,
		onlyNotClosedChildrenName: null,
		linkType: null,
		linkTypeName: null,
		linkTypeOptions: null,
		projectOrReleaseID: null,
		projectOrReleaseLabel: null,
		projectOrReleaseIDName: null,
		filterID: null,
		filterLabel: null,
		filterIDName: null
	},
	bodyStyle:{
		padding:'10px'
	},
	width: 400,
	labelWidth: 150,
	labelAlign: 'right',
	labelStyle: {overflow:'hidden'},
	meetingsCombo: null,
	createReportBtn:null,
	xmlDataSourceBtn:null,
	initComponent: function() {
		var me = this;
		this.callParent();
		if (this.templateID!=null) {
			this.add(CWHF.createHiddenField("templateID", {value:this.templateID}));
		}
		this.meetingsCombo = CWHF.createCombo("meeting.prompt.meeting", this.meetingName, {value: this.meeting,
			data: this.meetingOptions,
			labelWidth: this.labelWidth,
			labelAlign: this.labelAlign,
			labelStyle: this.labelStyle,
			width: this.width});
		this.add(this.meetingsCombo);
		this.add(CWHF.createCheckbox("meeting.prompt.onlyActiveMeeting", this.onlyActiveMeetingsName,
				{value:this.onlyActiveMeetings,
			labelWidth: this.labelWidth,
			labelAlign: this.labelAlign,
			labelStyle: this.labelStyle,
			width: this.width},
			{change: {fn: this.reloadMeetings,
				scope:this
			}}));
		this.add(CWHF.createCheckbox("meeting.prompt.onlyActiveChildren", this.onlyNotClosedChildrenName,
				{value:this.onlyNotClosedChildren,
			labelWidth: this.labelWidth,
			labelAlign: this.labelAlign,
			labelStyle: this.labelStyle,
			width: this.width
				}));
		this.linkTypeCombo = CWHF.createCombo("meeting.prompt.linkType", this.linkTypeName,
				{idType:"string",
			value: this.linkType,
			data: this.linkTypeOptions,
			labelWidth: this.labelWidth,
			labelAlign: this.labelAlign,
			labelStyle: this.labelStyle,
			width: this.width});
		this.add(this.linkTypeCombo);
		this.listeners = {
			afterrender: function(formPanel, eOpts) {
				var windowPanel = formPanel.up('window');
				var bottomToolbar = windowPanel.getDockedItems('toolbar[dock="bottom"]')[0];
				me.createReportBtn = bottomToolbar.items.items[0];
				me.xmlDataSourceBtn = bottomToolbar.items.items[1];
				if(me.meetingOptions.length == 0) {
					me.createReportBtn.setDisabled(true);
					me.xmlDataSourceBtn.setDisabled(true);
				}
			}
		};
	},

	reloadMeetings: function(checkboxField, newValue, oldValue, options) {
		var me = this;
		var params = {templateID: this.templateID};
		params[this.meetingName] = this.meetingsCombo.getValue();
		params[this.onlyActiveMeetingsName] = newValue;
		Ext.Ajax.request({
			url: 'reportDatasource!refreshParameters.action',
			params: params,
			scope: this,
			disableCaching:true,
			success: function(response) {
				var responseJson = Ext.decode(response.responseText);
				this.meetingsCombo.getStore().removeAll();
				if(responseJson.meetingOptions != null) {
					this.meetingsCombo.getStore().loadData(responseJson["meetingOptions"]);
				}
				if(responseJson[this.meetingName] != null) {
					this.meetingsCombo.setValue(responseJson[this.meetingName]);
				}else {
					this.meetingsCombo.setValue('');
				}
				var btnDisabled = false;
				if(responseJson.meetingOptions == null) {
					btnDisabled = true;
				}
				me.createReportBtn.setDisabled(btnDisabled);
				me.xmlDataSourceBtn.setDisabled(btnDisabled);
			},
			failure: function(result) {
				Ext.MessageBox.alert(getText('common.err.failure'), result.responseText);
			}
		})
	}
});
