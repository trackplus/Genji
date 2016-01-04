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

Ext.define('com.trackplus.admin.action.sendEmail.SendEmailController',{
	extend: "Ext.app.ViewController",
	alias: "controller.sendEmail",

//	static
	ERROR_NEED_PERSON:3,
	ERROR_NEED_SUBJECT:6,
	ERROR_NEED_BODY:7,
	ERROR_EMAIL_NOT_SEND:4,
	ERROR_NEED_MORE_TIME:5,

//	end static

	visibleCC:false,
	visibleBCC:false,

	currentTxtTo:null,

	showHideCC:function(){
		this.visibleCC=!this.visibleCC;
		var containerCC=this.lookupReference('container_cc');
		containerCC.setVisible(this.visibleCC);
	},
	showHideBCC:function(){
		this.visibleBCC=!this.visibleBCC;
		var containerBCC=this.lookupReference('container_bcc');
		containerBCC.setVisible(this.visibleBCC);
	},

	sendEmail:function(){
		var me=this;
		var txtBody=this.lookupReference('txtBody');
		var txtTo=this.lookupReference('txt_to');
		var txtSubject=this.lookupReference('txtSubject');
		var formPanel=this.getView();

		CWHF.submitRTEditor(txtBody);
		formPanel.setLoading(com.trackplus.TrackplusConfig.getText("common.lbl.loading"));
		formPanel.getForm().submit({
			url:'sendEmail!sendEmail.action',
			success: function(form, action) {
				formPanel.setLoading(false);
				CWHF.showMsgInfo(getText('admin.server.sendEmail.lbl.sentSuccessful'));
			},
			failure: function(form, action) {
				formPanel.setLoading(false);
				var errorCode=action.result.errorCode;
				var errorMessage=action.result.errorMessage;
				switch(errorCode){
					case me.ERROR_NEED_PERSON:{
						txtTo.focus();
						break;
					}
					case me.ERROR_NEED_SUBJECT:{
						txtSubject.focus();
						break;
					}
				}
				if(errorMessage){
					CWHF.showMsgError(errorMessage);
				}
			}
		});
	},

	openLink_to:function(){
		this.openPersonDialog('to');
	},
	openLink_cc:function(){
		this.openPersonDialog('cc');
	},
	openLink_bcc:function(){
		this.openPersonDialog('bcc');
	},

	openPersonDialog:function(type,title){
		var me=this;
		var title=getText('item.action.sendItemEmail.choosePerson.title',getText('item.action.sendItemEmail.lbl.'+type));
		me.currentTxtTo='txt_'+type;
		var personPikerDialog=Ext.create('com.trackplus.util.PersonPickerDialog',{
			title:title,
			options:null,
			includeEmail:true,
			includeGroups:true,
			handler:me.addPersonHandler,
			scope:me
		});
		personPikerDialog.showDialog();
	},
	addPersonHandler:function(value,displayValue){
		var me=this;
		var currentTxtToCmp=this.lookupReference(me.currentTxtTo);
		if(CWHF.isNull(displayValue)){
			return ;
		}
		var oldValue=currentTxtToCmp.getValue();
		var newValue="";
		if(oldValue&&oldValue!==''){
			newValue=oldValue+";"
		}
		newValue+=displayValue.join('; ');
		currentTxtToCmp.setValue(newValue);
	}
});
