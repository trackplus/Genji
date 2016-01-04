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

Ext.define('com.trackplus.admin.server.status.ServerStatusController',{
	extend: "Ext.app.ViewController",
	alias: "controller.serverStatus",

	onSave:function(button, event){
		var me=this;
		var formPanel=this.lookupReference('formPanel');
		formPanel.setLoading(true);
		formPanel.getForm().submit({
			success: function(form, action) {
				formPanel.setLoading(false);
			},
			failure: function(form, action) {
				formPanel.setLoading(false);
				var errorCode=action.result.errorCode;
				var errorMessage=action.result.errorMessage;
				alert(errorMessage);
			}
		});
	},
	onAfterRender:function(cmp,opt){
		var btnSave=this.lookupReference('btnSave');
		btnSave.setDisabled(false);
		this.reload();
	},
	onReload:function(button, event){
		this.reload();
	},
	reload:function(){
		var me=this;
		var logMessagesTab=this.lookupReference('logMessagesTab');
		var formPanel=this.lookupReference('formPanel');
		var view=this.getView();
		borderLayout.setLoading(true);
		Ext.Ajax.request({
			url: 'editAdminSiteStatus!load.action',
			success: function(response){
				var jsonData=Ext.decode(response.responseText);
				borderLayout.setLoading(false);
				if(jsonData.success===true){
					formPanel.removeAll(true);
					formPanel.add(view.createFormItems(jsonData.data));

					var logMessages=me.formatLogMessage(jsonData.data);
					logMessagesTab.update(logMessages);
					me.scrollToBottomLogs(logMessagesTab);
				}else{
					alert("failed:"+jsonData.errorMessage);
				}
			},
			failure:function(response){
				borderLayout.setLoading(false);
			}
		});
	},
	scrollToBottomLogs:function(logMessagesTab){
		if(logMessagesTab.body!=null&&logMessagesTab.body.dom!=null){
			var d = logMessagesTab.body.dom;
			d.scrollTop = d.scrollHeight - d.offsetHeight;
		}
	},
	formatLogMessage:function(jsonData){
		var logArray=jsonData['logMessages'];
		var logMessages = '';
		if (logArray  && logArray.length > 0) {
			for (i=0; i < logArray.length; ++i) {

				pre = '<div class="logMessage">';
				post = '</div>';
				if (logArray[i].indexOf(' ERROR ') !== -1) {
					pre = '<div class="logMessageError">'
				}
				if (logArray[i].indexOf(' WARN ') !== -1) {
					pre = '<div class="logMessageWarn">'
				}

				logMessages = logMessages + pre + logArray[i] + post;
			}
		}
		return logMessages;
	}
});
