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

Ext.define('com.trackplus.admin.server.SendEmail',{
	extend:'Ext.Base',
	constructor: function(config) {
	},

//	static
	ERROR_NEED_PERSON:3,
	ERROR_NEED_SUBJECT:6,
	ERROR_NEED_BODY:7,
	ERROR_EMAIL_NOT_SEND:4,
	ERROR_NEED_MORE_TIME:5,
	instance:null,
//	end static

	btnCC:null,
	btnBCC:null,
	btnSend:null,
	win:null,
	formPanel:null,

	txtSubject:null,
	txtBody:null,

	txtTo:null,
	txtCC:null,
	txtBCC:null,

	visibleCC:false,
	visibleBCC:false,
	containerCC:null,
	containerBCC:null,

	getToolbar:function(){
		var me=this;
		if(CWHF.isNull(me.btnSend)){
			me.initButtons();
		}
		return [me.btnCC,me.btnBCC,me.btnSend];
	},
	initButtons:function(){
		var me=this;
		me.btnCC=me.createToolbarBtn(getText('item.action.sendItemEmail.lbl.cc'),me.showHideCC,'user16',true);
		me.btnBCC=me.createToolbarBtn(getText('item.action.sendItemEmail.lbl.bcc'),me.showHideBCC,'user16',true);
		me.btnSend=new Ext.Button({
			text:getText('item.action.sendItemEmail.lbl.send'),
			tooltip:getText('item.action.sendItemEmail.lbl.send'),
			iconCls: 'buttonEmailSend',
			disabled:false,
			handler:function(){
				me.sendEmail.call(me);
			}
		});
	},
	createToolbarBtn:function(label,handler,cssClass,enableToggle){
		var me=this;
		return new Ext.Button({
			overflowText:label,
			tooltip:label,
			iconCls:cssClass,
			text:label,
			handler:handler,
			enableToggle:enableToggle?enableToggle===true:false,
			scope:me
		});
	},
	showHideCC:function(){
		var me=this;
		me.visibleCC=!me.visibleCC;
		me.containerCC.setVisible(me.visibleCC);
	},
	showHideBCC:function(){
		var me=this;
		me.visibleBCC=!me.visibleBCC;
		me.containerBCC.setVisible(me.visibleBCC);
	},

	createMainComponent:function(){
		var me=this;
		me.initView();
		return me.formPanel;
	},


	initView:function(){
		var me=this;

		me.linkTo=Ext.create('Ext.ux.LinkComponent',{
			clsLink:'link_blue',
			style:{
				textAlign:'right',
				paddingTop:'4px'
			},
			suffix:':',
			label:getText('item.action.sendItemEmail.lbl.to'),
			width:55,
			handler:me.openLinkTo,
			scope:me
		});
		me.txtTo=Ext.create('Ext.form.field.Text',{
			name:'toCustom',
			margin:'0 0 0 5',
			flex:1
		});
		me.btnChooseTo=Ext.create('Ext.button.Button',{
			tooltip:getText('item.action.sendItemEmail.choosePerson.title',getText('item.action.sendItemEmail.lbl.to')),
			iconCls:'addressBook',
			handler:me.openLinkTo,
			margin:'0 0 0 5',
			scope:me
		})
		var containerTo={
			xtype:'container',
			layout: {
				type: 'hbox',
				pack: 'start',
				align: 'stretch'
			},
			anchor: '100%',
			margin:'5 5 5 5',
			items:[me.linkTo,me.txtTo,me.btnChooseTo]
		};

		me.linkCC=Ext.create('Ext.ux.LinkComponent',{
			clsLink:'link_blue',
			style:{
				textAlign:'right',
				paddingTop:'4px'
			},
			suffix:':',
			label:getText('item.action.sendItemEmail.lbl.cc'),
			width:55,
			handler:me.openLinkCC,
			scope:me
		});
		me.txtCC=Ext.create('Ext.form.field.Text',{
			name:'ccCustom',
			margin:'0 0 0 5',
			flex:1
		});
		me.btnChooseCC=Ext.create('Ext.button.Button',{
			tooltip:getText('item.action.sendItemEmail.choosePerson.title',getText('item.action.sendItemEmail.lbl.cc')),
			iconCls:'addressBook',
			handler:me.openLinkCC,
			margin:'0 0 0 5',
			scope:me
		})
		me.containerCC=Ext.create('Ext.container.Container',{
			xtype:'container',
			layout: {
				type: 'hbox',
				pack: 'start',
				align: 'stretch'
			},
			anchor: '100%',
			margin:'5 5 5 5',
			hidden:true,
			items:[me.linkCC,me.txtCC,me.btnChooseCC]
		});

		me.linkBCC=Ext.create('Ext.ux.LinkComponent',{
			clsLink:'link_blue',
			style:{
				textAlign:'right',
				paddingTop:'4px'
			},
			suffix:':',
			label:getText('item.action.sendItemEmail.lbl.bcc'),
			width:55,
			handler:me.openLinkBCC,
			scope:me
		});
		me.txtBCC=Ext.create('Ext.form.field.Text',{
			margin:'0 0 0 5',
			flex:1,
			name:'bccCustom',
			anchor: '100%'
		});
		me.btnChooseBCC=Ext.create('Ext.button.Button',{
			tooltip:getText('item.action.sendItemEmail.choosePerson.title',getText('item.action.sendItemEmail.lbl.bcc')),
			iconCls:'addressBook',
			handler:me.openLinkBCC,
			margin:'0 0 0 5',
			scope:me
		})
		me.containerBCC=Ext.create('Ext.container.Container',{
			xtype:'container',
			layout: {
				type: 'hbox',
				pack: 'start',
				align: 'stretch'
			},
			anchor: '100%',
			margin:'5 5 5 5',
			hidden:true,
			items:[me.linkBCC,me.txtBCC,me.btnChooseBCC]
		});


		me.txtSubject=Ext.create('Ext.form.field.Text',{
			name:'subject',
			fieldLabel:getText('admin.server.sendEmail.lbl.subject'),
			labelWidth: 60,
			labelStyle:"overflow:hidden;",
			labelAlign:'right',
			anchor:'100%'
		});

		var northPanel=Ext.create('Ext.panel.Panel',{
			layout:'anchor',
			border:false,
			bodyBorder:false,
			region:'north',
			padding: '0 0 0 0',
			items:[containerTo,me.containerCC,me.containerBCC,me.txtSubject]
		});


		var bodyCfg={
			region:'center',
			margin:'0 0 0 0',
			cls:'rteField',
			border:false
			/*cls:'ckeField100Percent'*/
		};
		me.txtBody=CWHF.createRichTextEditorField('mailBody',bodyCfg,false,true);

		me.formPanel=Ext.create('Ext.form.Panel', {
			layout:'border',
			border    : false,
			margins: '0 0 0 0',
			bodyStyle: "padding: 5px;",
			items:[northPanel,me.txtBody]
		});
	},


	init:function(){
		var me=this;
		me.initView();
	},

	clear:function(){
		var me=this;
		me.txtBody.setValue('');
		me.txtSubject.setValue(null);
		me.txtTo.setValue(null);
		me.txtCC.setValue(null);
		me.txtBCC.setValue(null);
	},

	validate:function(){
		var me=this;
		return true;
	},

	sendEmail:function(){
		var me=this;
		CWHF.submitRTEditor(me.txtBody);
		if(!me.validate){
			return false;
		}
		me.formPanel.setLoading(com.trackplus.TrackplusConfig.getText("common.lbl.loading"));
		me.formPanel.getForm().submit({
			url:'sendEmail!sendEmail.action',
			success: function(form, action) {
				me.formPanel.setLoading(false);
				CWHF.showMsgInfo(getText('admin.server.sendEmail.lbl.sentSuccessful'));
			},
			failure: function(form, action) {
				me.formPanel.setLoading(false);
				var errorCode=action.result.errorCode;
				var errorMessage=action.result.errorMessage;
				switch(errorCode){
					case me.ERROR_NEED_PERSON:{
						me.txtTo.focus();
						break;
					}
					case me.ERROR_NEED_SUBJECT:{
						me.txtSubject.focus();
						break;
					}
				}
				if(errorMessage){
					CWHF.showMsgError(errorMessage);
				}
			}
		});
	},

	openLinkTo:function(){
		var me=this;
		var title=getText('item.action.sendItemEmail.choosePerson.title',getText('item.action.sendItemEmail.lbl.to'));
		me.openPersonDialog(me.txtTo,title);
	},
	openLinkCC:function(){
		var me=this;
		var title=getText('item.action.sendItemEmail.choosePerson.title',getText('item.action.sendItemEmail.lbl.cc'));
		me.openPersonDialog(me.txtCC,title);
	},
	openLinkBCC:function(){
		var me=this;
		var title=getText('item.action.sendItemEmail.choosePerson.title',getText('item.action.sendItemEmail.lbl.bcc'));
		me.openPersonDialog(me.txtBCC,title);
	},

	openPersonDialog:function(txtTo,title){
		var me=this;
		me.currentTxtTo=txtTo;
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
		if(CWHF.isNull(displayValue)){
			return ;
		}
		var oldValue=me.currentTxtTo.getValue();
		var newValue="";
		if(oldValue&&oldValue!==''){
			newValue=oldValue+";"
		}
		newValue+=displayValue.join('; ');
		me.currentTxtTo.setValue(newValue);
	}
});
