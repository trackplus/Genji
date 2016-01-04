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

Ext.define('com.trackplus.admin.action.sendEmail.SendEmailView',{
	extend:'Ext.form.Panel',
	controller:'sendEmail',
	layout:'border',
	border    : false,
	margins: '0 0 0 0',
	bodyStyle: "padding: 5px;",

	listeners : {
		scope: 'controller',
		'onOpenLink_to':'openLink_to',
		'onOpenLink_cc':'openLink_cc',
		'onOpenLink_bcc':'openLink_bcc'
	},
	initComponent : function() {
		var me=this;


		var container_to=me.createContainerTO("to");
		var container_cc=me.createContainerTO("cc");
		var container_bcc=me.createContainerTO("bcc");

		var txtSubject=Ext.create('Ext.form.field.Text',{
			name:'subject',
			fieldLabel:getText('admin.server.sendEmail.lbl.subject'),
			labelWidth: 60,
			labelStyle:"overflow:hidden;",
			labelAlign:'right',
			reference:'txtSubject',
			anchor:'100%'
		});

		var northPanel=Ext.create('Ext.panel.Panel',{
			layout:'anchor',
			border:false,
			bodyBorder:false,
			region:'north',
			padding: '0 0 0 0',
			items:[container_to,container_cc,container_bcc,txtSubject]
		});


		var bodyCfg={
			region:'center',
			margin:'0 0 0 0',
			cls:'rteField',
			border:false,
			reference:'txtBody'
		};
		var txtBody=CWHF.createRichTextEditorField('mailBody',bodyCfg,false,true);

		this.items=[northPanel,txtBody];


		var btnCC=this.createToolbarBtn(getText('item.action.sendItemEmail.lbl.cc'),'showHideCC','user16',true);
		var btnBCC=this.createToolbarBtn(getText('item.action.sendItemEmail.lbl.bcc'),'showHideBCC','user16',true);
		var btnSend=new Ext.Button({
			text:getText('item.action.sendItemEmail.lbl.send'),
			tooltip:getText('item.action.sendItemEmail.lbl.send'),
			iconCls: 'buttonEmailSend',
			disabled:false,
			handler:'sendEmail'
		});

		this.dockedItems = [{
			xtype: "toolbar",
			dock: 'top',
			items: [btnCC,btnBCC,btnSend]
		}];
		this.callParent();
	},
	createToolbarBtn:function(label,handler,cssClass,enableToggle){
		var me=this;
		return new Ext.Button({
			overflowText:label,
			tooltip:label,
			iconCls:cssClass,
			text:label,
			handler:handler,
			enableToggle:enableToggle?enableToggle===true:false
		});
	},

	createContainerTO:function(type){
		var me=this;
		var linkTo=Ext.create('Ext.ux.LinkComponent',{
			clsLink:'link_blue',
			style:{
				textAlign:'right',
				paddingTop:'4px'
			},
			suffix:':',
			label:getText('item.action.sendItemEmail.lbl.'+type),
			width:55,
			handler:function(){
				me.fireEvent('onOpenLink_'+type);
			}
		});
		var txtTo=Ext.create('Ext.form.field.Text',{
			name:type+'Custom',
			margin:'0 0 0 5',
			reference:'txt_'+type,
			flex:1
		});
		var btnChooseTo=Ext.create('Ext.button.Button',{
			tooltip:getText('item.action.sendItemEmail.choosePerson.title',getText('item.action.sendItemEmail.lbl.'+type)),
			iconCls:'addressBook',
			handler:'openLink_'+type,
			margin:'0 0 0 5'
		});
		return {
			xtype:'container',
			layout: {
				type: 'hbox',
				pack: 'start',
				align: 'stretch'
			},
			anchor: '100%',
			margin:'5 5 5 5',
			hidden:(type!='to'),
			reference:'container_'+type,
			items:[linkTo,txtTo,btnChooseTo]
		};
	}
});
