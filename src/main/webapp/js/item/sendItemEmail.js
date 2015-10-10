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


Ext.define('com.trackplus.item.SendEmail',{
	extend:'Ext.Base',
	config: {
		workItemID:null
	},
	constructor: function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	},
	view:null,
	controller:null,
	show:function(){
		var me=this;
		if(me.controller==null){
			me.controller=Ext.create('com.trackplus.item.SendEmailController',{
				workItemID:me.workItemID
			});
		}
		me.controller.show();
	}
});


Ext.define('com.trackplus.item.SendEmailView',{
	extend: 'Ext.form.Panel',
	config:{
		model:{},
		controller:null
	},
	layout:'border',
	border:false,
	padding:'0 0 0 0',

	cmbFromAddress:null,
	txtTo:null,
	txtCC:null,
	txtBCC:null,
	visibleCC:false,
	visibleBCC:false,
	visibleAttachments:false,
	initComponent: function(){
		var me=this;
		me.items=me.createChildren();
		me.callParent();
	},
	createToolbarBtn:function(label,handler,cssClass,enableToggle){
		var me=this;
		return Ext.create('Ext.button.Button',{
			xtype:'button',
			overflowText:label,
			tooltip:label,
			iconCls:cssClass,
			cls:'toolbarItemAction-noText',
			text:label,
			handler:handler,
			enableToggle:enableToggle!=null?enableToggle==true:false,
			scope:me
		});
	},
	createChildren:function(){
		var me=this;
		var labelWidth=70;
		me.btnCC=me.createToolbarBtn(getText('item.action.sendItemEmail.lbl.cc'),me.showHideCC,'user16',true);
		me.btnBCC=me.createToolbarBtn(getText('item.action.sendItemEmail.lbl.bcc'),me.showHideBCC,'user16',true);
		me.btnAttachemnt=me.createToolbarBtn(getText('item.action.sendItemEmail.lbl.attachments'),me.showHideAtachments,'attachment16',true);
		me.btnSend=me.createToolbarBtn(getText('item.action.sendItemEmail.lbl.send'),me.sendEmail,'buttonEmailSend');
		me.btnCancel=me.createToolbarBtn(getText('common.btn.cancel'),me.cancelHandler,'itemAction_cancel');
		var toolbarItems=new Array();
		toolbarItems.push(me.btnCC);
		toolbarItems.push(me.btnBCC);
		if(me.model.attachmentsList!=null&&me.model.attachmentsList.length>0){
			toolbarItems.push(me.btnAttachemnt);
		}
		toolbarItems.push(me.btnSend);
		toolbarItems.push(me.btnCancel);
		me.toolbar=Ext.create('Ext.toolbar.Toolbar', {
			layout: {
				overflowHandler: 'Menu'
			},
			enableOverflow: true,
			anchor:'100%',
			cls:'toolbarActions',
			border: '1 0 1 0',
			defaults: {
				cls:'toolbarItemAction',
				overCls:'toolbarItemAction-over',
				scale:'small',
				iconAlign: 'left',
				enableToggle:false
			},
			items:toolbarItems
		});
		var submitterEmail=me.model['submitterEmail'];
		me.dsFromAddress=Ext.create('Ext.data.Store', {
			fields	: [{name:'id', type:'string'}, {name:'label', type:'string'}],
			data:me.model["fromAddressList"]
		});
		me.cmbFromAddress=Ext.create('Ext.form.ComboBox',{
			fieldLabel:getText('item.action.sendItemEmail.lbl.from'),
			labelStyle:"overflow:hidden;",
			labelAlign:'right',
			margin:'5 5 5 5',
			labelWidth: labelWidth,
			store:me.dsFromAddress,
			displayField : 'label',
			valueField: 'id',
			typeAhead:true,
			queryMode: 'local',
			triggerAction: 'all',
			name: 'from',
			value:me.model['from'],
			width:400
		});

		me.linkTo=Ext.create('Ext.ux.LinkComponent',{
			clsLink:'link_blue',
			style:{
				textAlign:'right',
				paddingTop:'4px'
			},
			suffix:':',
			label:getText('item.action.sendItemEmail.lbl.to'),
			width:labelWidth,
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
		}

		me.linkCC=Ext.create('Ext.ux.LinkComponent',{
			clsLink:'link_blue',
			style:{
				textAlign:'right',
				paddingTop:'4px'
			},
			suffix:':',
			label:getText('item.action.sendItemEmail.lbl.cc'),
			width:labelWidth,
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
			width:labelWidth,
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
		me.containerAttachments= Ext.create('com.trackplus.util.MultipleSelectPicker',{
			data:me.model.attachmentsList,
			hidden:true,
			//width:400,
			anchor: '100%',
			margin:'10 5 5 5',
			name:'selectedAttachments',
			fieldLabel:getText('item.action.sendItemEmail.lbl.attachments'),
			labelStyle:"overflow:hidden;",
			labelAlign:'right',
			labelWidth: labelWidth,
			localizedLabel:getText('item.action.sendItemEmail.lbl.attachments'),
			useNull:true
			//iconUrlPrefix:'optionIconStream.action?fieldID=-'+fieldID+'&optionID='
		});

		me.chkSubmitter=Ext.create('Ext.form.field.Checkbox',{
			margin:'0 0 0 80',
			boxLabel:getText('item.action.sendItemEmail.lbl.submitterEmail')+'&nbsp;&lt;'+submitterEmail+'&gt;',
			name: 'includeSubmitterEmail',
			hidden:submitterEmail==null,
			inputValue : true
		});
		me.txtSubjectPrefix=Ext.create('Ext.form.field.Text',{
			margin:'0 5 0 0',
			readOnly:true,
			name:'subjectReadonly',
			value:me.model['subjectReadolnyPart'],
			columnWidth:0.25
		});
		me.txtSubject=Ext.create('Ext.form.field.Text',{
			name:'subject',
			value:me.model['subject'],
			columnWidth:0.75
		})
		var subjectCmp={
			xtype: 'fieldcontainer',
			margin:'15 5 5 5',
			fieldLabel:getText('item.action.sendItemEmail.lbl.subject'),
			labelStyle:"overflow:hidden;",
			labelAlign:'right',
			labelWidth: labelWidth,
			layout:'column',
			anchor:'100%',
			defaults: {
				hideLabel: true
			},
			items:[me.txtSubjectPrefix,me.txtSubject]
		};
		var northPanel=Ext.create('Ext.panel.Panel',{
			layout:'anchor',
			border:false,
			bodyBorder:false,
			region:'north',
			padding: '0 0 0 0',
			items:[me.toolbar, me.cmbFromAddress,containerTo,me.containerCC,me.containerBCC, me.chkSubmitter,me.containerAttachments,subjectCmp]
		});


		var bodyCfg={
			region:'center',
			margin:'5 5 5 5',
			border:false
			/*cls:'ckeField100Percent'*/
		};
		var ckeditorCfg={
			workItemID:me.controller.workItemID
		};
		me.txtBody=CWHF.createRichTextEditorField('mailBody',bodyCfg,false,true,ckeditorCfg);
		me.chkIncludeItemInformation=Ext.create('Ext.form.field.Checkbox',{
			boxLabel:getText('item.action.sendItemEmail.lbl.includeItemInformation'),
			inputValue : true,
			name: 'includeItemInformation'
		});
		me.chkIncludeLink=Ext.create('Ext.form.field.Checkbox',{
			boxLabel:getText('item.action.sendItemEmail.lbl.includeItemLink'),
			inputValue : true,
			checked : true,
			name: 'includeItemLink'
		});
		var containerSouth=Ext.create('Ext.container.Container',{
			margin:'0 5 5 5',
			items:[me.chkIncludeItemInformation,me.chkIncludeLink],
			region:'south'
		});
		return [northPanel,me.txtBody,containerSouth];
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
	showHideAtachments:function(){
		var me=this;
		me.visibleAttachments=!me.visibleAttachments;
		me.containerAttachments.setVisible(me.visibleAttachments);
	},

	openLinkTo:function(){
		var me=this;
		var title=getText('item.action.sendItemEmail.choosePerson.title',getText('item.action.sendItemEmail.lbl.to'));
		me.controller.openPersonDialog.call(me.controller,me.txtTo,title);
	},
	openLinkCC:function(){
		var me=this;
		var title=getText('item.action.sendItemEmail.choosePerson.title',getText('item.action.sendItemEmail.lbl.cc'));
		me.controller.openPersonDialog.call(me.controller,me.txtCC,title);
	},
	openLinkBCC:function(){
		var me=this;
		var title=getText('item.action.sendItemEmail.choosePerson.title',getText('item.action.sendItemEmail.lbl.bcc'));
		me.controller.openPersonDialog.call(me.controller,me.txtBCC,title);
	},
	sendEmail:function(){
		var me=this;
		me.controller.sendEmail.call(me.controller);
	},
	cancelHandler:function(){
		var me=this;
		me.controller.cancelHandler.call(me.controller);
	}
});


Ext.define('com.trackplus.item.SendEmailController',{
	extend:'Ext.Base',
	config: {
		workItemID:null
	},
	view:null,
	ERROR_NEED_FROM:1,
	ERROR_INVALID_EMAIL:2,
	ERROR_NEED_PERSON:3,
	ERROR_EMAIL_NOT_SEND:4,
	ERROR_NEED_MORE_TIME:5,

	constructor: function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	},
	createView:function(data){
		var me=this;
		me.view=Ext.create('com.trackplus.item.SendEmailView',{
			model:data,
			controller:me
		});
		me.addListeners();
		return me.view;
	},
	addListeners:function(){
	},
	openPersonDialog:function(txtTo,title){
		var me=this;
		me.currentTxtTo=txtTo;
		var personPikerDialog=Ext.create('com.trackplus.util.PersonPickerDialog',{
			title:title,
			data:null,
			includeEmail:true,
			includeGroups:true,
			handler:me.addPersonHandler,
			scope:me
		});
		personPikerDialog.showDialog();
	},
	addPersonHandler:function(value,displayValue){
		var me=this;
		if(displayValue==null){
			return ;
		}
		var oldValue=me.currentTxtTo.getValue();
		var newValue="";
		if(oldValue!=null&&oldValue!=''){
			newValue=oldValue+";"
		}
		newValue+=displayValue.join('; ');
		me.currentTxtTo.setValue(newValue);
	},
	show:function(){
		var me=this;
		var urlStr='sendItemEmail.action';
		borderLayout.setLoading(true);
		Ext.Ajax.request({
			url: urlStr,
			scope:me,
			params:{'workItemID':me.workItemID},
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				borderLayout.setLoading(false);
				if (responseJson.success==true) {
					me.successHandler.call(me,responseJson.data);
				}else{
					var errorCode=responseJson.errorCode;
					var errorMessage=responseJson.errorMessage;
					if(errorCode==me.ERROR_EMAIL_NOT_SEND){
						CWHF.showMsgError(getText('item.action.sendItemEmail.err.noSMTP'));
					}else{
						CWHF.showMsgError(errorMessage);
					}
				}
			},
			failure:function(){
				borderLayout.setLoading(false);
				alert("failure");
			}
		});
	},
	successHandler:function(data){
		var me=this;
		if(me.view!=null){
			me.view.destroy();
		};
		me.view=me.createView(data);
		if(me.win!=null){
			me.win.destroy();
			me.win=null;
		}
		me.win = Ext.create('Ext.window.Window',{
			layout      : 'fit',
			maximizable :true,
			border:false,
			bodyBorder:true,
			margin:'0 0 0 0',
			style:{
				padding:'5px 0px 0px 0px'
			},
			bodyStyle:{
				padding:'0 0 0 0',
				margin:'0 0 0 0'
			},
			iconCls:'buttonEmail16',
			width       : 800,
			height      : 600,
			closeAction :'destroy',
			plain       : true,
			title		:getText('item.action.sendItemEmail.lbl.title'),
			modal       :true,
			items       :[me.view],
			autoScroll  :true
		});
		var width=800;
		var height=600;
		var size=borderLayout.ensureSize(width,height);
		width=size.width;
		height=size.height;
		me.win.setWidth(width);
		me.win.setHeight(height);
		if(width<800||height<600){
			me.win.setPosition(10,10);
		}
		me.win.show();
	},
	sendEmail:function(){
		var me=this;
		CWHF.submitRTEditor(me.view.txtBody);
		var formPanel=me.view;
		formPanel.setLoading(com.trackplus.TrackplusConfig.getText("common.lbl.loading"));
		formPanel.getForm().submit({
			url:'sendItemEmail!sendEmail.action',
			params:{
				'workItemID':me.workItemID
			},
			success: function(form, action) {
				formPanel.setLoading(false);
				CWHF.showMsgInfo(getText('item.action.sendItemEmail.lbl.sentSuccessful'));
				me.win.hide();
				me.win.destroy();
			},
			failure: function(form, action) {
				formPanel.setLoading(false);
				var errorCode=action.result.errorCode;
				var errorMessage=action.result.errorMessage;
				CWHF.showMsgError(errorMessage);
				switch(errorCode){
					case me.ERROR_NEED_FROM:{
						me.view.cmbFromAddress.focus();
						break;
					}
					case me.ERROR_INVALID_EMAIL:{
						me.view.txtTo.focus();
						break;
					}
					case me.ERROR_NEED_PERSON:{
						me.view.txtTo.focus();
						break;
					}
				}
			}
		});
	},
	cancelHandler:function(){
		var me=this;
		me.win.hide();
		me.win.destroy();
		me.win=null;
	}
});
