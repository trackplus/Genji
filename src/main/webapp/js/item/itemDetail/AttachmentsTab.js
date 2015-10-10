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


Ext.define('com.aurel.trackplus.itemDetail.AttachmentsTab',{
	extend:'com.aurel.trackplus.itemDetail.TabGrid',
	urlDeleteItems:'deletedAttachments.action',
	readOnly:false,
	dropFileSupported:false,
	fieldsToNotifyOnDelete:[-1001, 15],
	initComponent : function(){
		var me = this;
		var attachmentNumber=0;
		if(me.jsonData.attachmentNumber!=null){
			attachmentNumber=me.jsonData.attachmentNumber;
		}
		me.title=getText('item.printItem.lbl.tab.attachments')+" ("+attachmentNumber+")";
		if(typeof window.FileReader === 'undefined'){
			//drop file unsupported
			me.dropFileSupported=false;
		}else{
			me.dropFileSupported=true;
		}
		// me.iconCls='attachment16';
		me.gridConfig=me.createAttachmentsConfig();
		me.callParent();
		me.addListener('render',me.onReadyHandler,me);
	},
	onReadyHandler:function(){
		var me=this;
		if(me.dropFileSupported==false){
			return false;
		}
		var domEl=me.getEl().dom;
		domEl.addEventListener("dragenter", function(evt){
			evt.stopPropagation();
			evt.preventDefault();
			var mouseX=parseInt(evt.clientX);
			var mouseY=parseInt(evt.clientY);
			var winX=mouseX-150;
			if(winX<10){
				winX=10;
			}
			me.addCls("attachmentsTabBody");
			var btnDragDrop=me.gridConfig.grid.down("#dragDropAttachemnt");
			btnDragDrop.setIconCls('dragOver');
			btnDragDrop.setText(getText('item.tabs.attachment.drop'))
			btnDragDrop.setDisabled(false);
		}, false);
		domEl.addEventListener("dragexit",  function(evt){
			evt.stopPropagation();
			evt.preventDefault();
			me.removeCls("attachmentsTabBody");
			var btnDragDrop=me.gridConfig.grid.down("#dragDropAttachemnt");
			btnDragDrop.setIconCls('drag');
			btnDragDrop.setText(getText('item.tabs.attachment.dragDrop'))
			btnDragDrop.setDisabled(true);
		}, false);
		domEl.addEventListener("dragover",  function(evt){
			evt.stopPropagation();
			evt.preventDefault();
		}, false);
		domEl.addEventListener("drop",  function(evt){
			evt.stopPropagation();
			evt.preventDefault();
			me.removeCls("attachmentsTabBody");
			var btnDragDrop=me.gridConfig.grid.down("#dragDropAttachemnt");
			btnDragDrop.setIconCls('drag');
			btnDragDrop.setDisabled(true);
			btnDragDrop.setText(getText('item.tabs.attachment.dragDrop'))
			var files = evt.dataTransfer.files;
			if(files!=null&&files.length>0){
				me.sendingFile(files);
			}
		}, false);
	},
	sendingFile:function(files){
		var me=this;
		var formData = new FormData();
		for (var i = 0; i < files.length; i++) {
			formData.append('theFile', files[i]);
		}

		// now post a new XHR request
		var xhr = new XMLHttpRequest();
		var urlStr='saveAttachment.action';
		urlStr+='?workItemID='+me.workItemID+'&lastModified='+me.lastModified;
		urlStr+='&projectID='+me.projectID;
		urlStr+='&issueTypeID='+me.issueTypeID;
		xhr.open('POST', urlStr);
		xhr.onload = function () {
			if (xhr.status === 200) {
				me.setLoading('Uploading: 100%');
				me.setLoading(false);
				me.refresh();
			} else {
				alert('Something went terribly wrong...');
				me.setLoading(false);
			}
		};
		xhr.onerror=function(e){
			me.setLoading(false);
			alert("error:"+e);
		};
		xhr.upload.onprogress = function (event) {
			if (event.lengthComputable) {
				var complete = (event.loaded / event.total * 100 | 0);
				me.setLoading('Uploading: '+complete+'%');
			}
		};
		xhr.onerror=function(e){
			me.setLoading(false);
			alert("error:"+e);
		}
		me.setLoading('Uploading...');
		xhr.send(formData);

	},


	createAttachmentsConfig:function(){
		var me=this;
		if(me.jsonData!=null){
			me.readOnly=me.jsonData.readOnly;
		}
		var gridConfig=new com.trackplus.itemDetail.GridConfig();
		gridConfig.tabID="attachmentsTab";
		gridConfig.id=3;
		gridConfig.urlStore='itemAttachments.action?workItemID='+me.workItemID+"&projectID="+me.projectID+"&issueTypeID="+me.issueTypeID;
		gridConfig.fields=[
			{name:'id', type:'int'},
			{name:'fileName', type:'string'},
			{name:'fileSize', type:'int'},
			{name:'description', type:'string'},
			{name:'date', type:'date', dateFormat: com.trackplus.TrackplusConfig.ISODateTimeFormat},
			{name:'author', type:'string'},
			{name:'authorID', type:'string'},
			{name: 'editable', type: 'boolean'},
			{name: 'imgHeight', type: 'int'},
			{name: 'thumbnail', type: 'boolean'},
			{name: 'isURL', type: 'boolean'}
		];
		gridConfig.selectionModel = Ext.create('Ext.selection.CheckboxModel', {
			listeners: {
				selectionchange: function(sm, selections) {
					var btnDelete=me.gridConfig.grid.down("#deleteAttachmentBtn");
					var btnEdit=me.gridConfig.grid.down("#editAttachmentBtn");
					var btnDownload=me.gridConfig.grid.down("#downloadAttachmentBtn");
					if(selections==null||selections.length==0){
						btnDelete.setDisabled(true);
						btnEdit.setDisabled(true);
						btnDownload.setDisabled(true);
					}else{
						btnDownload.setDisabled(false);
						if(selections.length==1){
							var rowData=selections[0].data;
							btnEdit.setDisabled(me.readOnly||rowData.editable==false);
							btnDelete.setDisabled(me.readOnly||rowData.editable==false);
							if(selections[0].data.isURL) {
								btnDownload.setDisabled(true);
							}
						}else{
							btnEdit.setDisabled(true);
							var enableDelete=false;
							var disableDownload = false;
							for(var i=0;i<selections.length;i++){
								var rowData=selections[i].data;
								if(!me.readOnly&&rowData.editable==true){
									enableDelete=true;
								}
								if(rowData.isURL) {
									disableDownload = true;
								}
							}
							btnDelete.setDisabled(!enableDelete);
							btnDownload.setDisabled(disableDownload);
						}
					}
				}
			}
		});
		//toolbar
		var tbarArray=new Array();
		tbarArray.push({
			text:getText('common.btn.add'),
			itemId:"addAttachmentBtn",
			disabled:me.readOnly,
			iconCls:'add16',
			handler:function(){
				me.addAttachment.call(me);
			}
		});
		tbarArray.push({
			text:getText('item.tabs.attachment.addScreenshot'),
			itemId:"addScreenshotBtn",
			disabled:me.readOnly,
			iconCls:'add16',
			handler:function(){
				me.addScreenshot.call(me);
			}
		});

		tbarArray.push({
			text: getText('item.tabs.attachment.addLink'),
			itemId:"addLinkBtn",
			disabled: me.readOnly,
			iconCls:'add16',
			handler:function(){
				me.addLink.call(me);
			}
		});

		tbarArray.push({
			text:getText('common.btn.delete'),
			itemId:"deleteAttachmentBtn",
			disabled:true,
			iconCls:'delete16',
			handler:function(){
				me.deleteItems.call(me);
			}
		});
		tbarArray.push({
			text:getText('common.btn.edit'),
			itemId:"editAttachmentBtn",
			disabled:true,
			iconCls:'edit16',
			handler:function(){
				me.editAttachment.call(me);
			}
		});
		tbarArray.push('-');
		tbarArray.push({
			text:getText('common.btn.download'),
			itemId:"downloadAttachmentBtn",
			disabled:true,
			iconCls:'download16',
			handler:function(){
				me.downloadAttachment.call(me);
			}
		});
		if(me.dropFileSupported==true){
			tbarArray.push({
				text:getText('item.tabs.attachment.dragDrop'),
				itemId:'dragDropAttachemnt',
				iconCls:'drag',
				disabled:true
			});
		}
		gridConfig.tbar=tbarArray;
		gridConfig.dblClickHandler=me.editAttachment;
		gridConfig.updateCol=function(col,layout,index){
			if(layout.dataIndex=='description'){
				col.flex=1;
			}
			if(layout.dataIndex=='fileName'){
				col.renderer =  function(value, metaData, record, row, col, store, gridView){
					if(record.data.isURL) {
						var synopsisClass='synopsis_blue newWindow';
						return '<a href="'+ record.data.fileName + '" class="'+synopsisClass + '" target = "_blank">'+record.data.fileName +'</a>';
					}else {
						return record.data.fileName;
					}
				 }
			}
		};
		/*if(newItem){
		 refreshGridSize(attachmentsGridConfig);
		 }*/
		/*

		 */
		return gridConfig;
	},
	refreshCallback:function(r,options,success){
		var me=this;
		var size=r.length;
		me.setTitle(me.replaceTitleNumber(me.title,size));
	},
	addAttachment:function() {
		var me=this;
		var dialogCfg= new com.trackplus.itemDetail.DialogConfig(me.workItemID,me.projectID,me.issueTypeID);
		dialogCfg.title=getText("item.tabs.attachment.lbl.add");
		//url:addAttachment
		dialogCfg.formPanel=Ext.create('Ext.form.Panel', {
			itemId :'editAttachment',
			//autoScroll:true,
			layout:'anchor',
			border	: false,
			url: 'saveAttachment.action?lastModified='+me.lastModified,
			margin: '0 0 0 0',
			padding:0,
			bodyPadding:0,
			/*style:{
			 padding:'0px 0px 0px 0px'
			 },
			 bodyStyle:{
			 padding:'0px 0px 0px 0px'
			 },
			 /*style:{
			 borderBottom:'1px solid #D0D0D0'
			 },*/
			fileUpload: true,
			items:[
				{
					xtype: 'component',
					html: getText('item.tabs.attachment.explanation',me.jsonData.maxSize),
					anchor: '100%',
					border:true,
					cls: 'infoBox_bottomBorder'
				},{
					xtype: 'filefield',
					name: 'theFile',
					fieldLabel: getText('item.tabs.attachment.lbl.file'),
					labelWidth: 75,
					labelAlign:me.labelHAlign,
					msgTarget: 'side',
					allowBlank: false,
					anchor: '100%',
					buttonText: getText('common.btn.choose'),
					style:{
						marginTop:'5px',
						marginLeft:'10px',
						marginRight:'10px'
					}
				},{
					xtype:'textarea',
					fieldLabel: getText('common.lbl.description'),
					name:'description',
					labelWidth: 75,
					labelAlign:me.labelHAlign,
					height:75,
					anchor: '100% -90',
					style:{
						marginLeft:'10px',
						marginRight:'10px'
					}
				}
			]
		});
		dialogCfg.layout="fit";
		dialogCfg.w=500;
		dialogCfg.h=275;
		dialogCfg.minw=450;
		dialogCfg.minh=200;
		dialogCfg.autoScroll=false;
		dialogCfg.iconCls='attachment16';
		dialogCfg.successHandler=function(result){
			//ATTACHMENT_SYMBOL = -1001;
			var lastModified=result.data.lastModified;
			me.fireItemChange([-1001, 15],lastModified);
			me.refresh.call(me);
		};

		com.trackplus.itemDetail.openDialog(dialogCfg);
	},
	editAttachment:function(){
		var me=this;
		var selections=me.gridConfig.grid.getSelectionModel().getSelection();
		if(selections==null){
			return;
		}
		var rowData=selections[0].data;
		if(me.readOnly||rowData.editable==false){
			return;
		}
		var originalDescription=rowData.description;
		var attachmentID=rowData.id;

		var dialogCfg= new com.trackplus.itemDetail.DialogConfig(me.workItemID,me.projectID,me.issueTypeID);
		dialogCfg.title=com.trackplus.TrackplusConfig.getText("item.tabs.attachment.lbl.edit");
		dialogCfg.formPanel=Ext.create('Ext.form.Panel', {
			itemId :'editAttachment',
			region:'center',
			layout:'anchor',
			url: 'editAttachment.action?attachmentID='+attachmentID+'&lastModified='+me.lastModified,
			autoScroll:false,
			fileUpload: true,
			margin: '0 0 0 0',
			border:false,
			bodyStyle:{
				padding:'10px'
			},
			/*style:{
			 borderBottom:'1px solid #D0D0D0'
			 },*/
			items:[
				{
					xtype:'textarea',
					fieldLabel:getText('common.lbl.description'),
					name:'description',
					labelWidth: 75,
					labelAlign:'right',
					value:originalDescription,
					height:75,
					anchor: '100%'
				}
			]
		});
		dialogCfg.w=500;
		dialogCfg.h=175;
		dialogCfg.autoScroll=false;
		dialogCfg.iconCls='attachment16';
		dialogCfg.successHandler=function(result){
			//ATTACHMENT_SYMBOL = -1001;
			var lastModified=result.data.lastModified;
			me.fireItemChange([-1001, 15],lastModified);
			me.refresh.call(me);
		};

		com.trackplus.itemDetail.openDialog(dialogCfg);
	},
	downloadAttachment:function(){
		var me=this;
		var selections=me.gridConfig.grid.getSelectionModel().getSelection();
		//var row=attachmentsGridConfig.grid.getSelectionModel().getSelected();
		if(selections==null||selections.length==0){
			return;
		}
		var row;
		var i;

		for(i=0;i<selections.length;i++){
			row=selections[i];
			var attachID=row.data.id;
			var attachmentURI;
			if(me.workItemID==null){
				attachmentURI='downloadAttachment.action?attachKey='+attachID;
			}else{
				attachmentURI='downloadAttachment.action?workItemID='+me.workItemID+'&attachKey='+attachID;
			}
			if(i==0){
				window.open(attachmentURI,'attachmentWindow');
			}else{
				window.open(attachmentURI);
			}
		}
	},
	addScreenshot:function(){
		var me=this;
		/*var javaEnabled=navigator.javaEnabled();
		 if(javaEnabled==false){
		 alert(msgAppletNotSupported);
		 return false;
		 }*/

		var dialogCfg= new com.trackplus.itemDetail.DialogConfig(me.workItemID,me.projectID,me.issueTypeID);
		dialogCfg.title=getText("item.tabs.attachment.addScreenshot");
		dialogCfg.formPanel=Ext.create('Ext.form.Panel', {
			//region:'center',
			url: 'saveScreenshot.action',
			border	: false,
			autoScroll:true,
			margin: '0 0 0 0',
			bodyPadding:0,
			bodyStyle:{
				padding:'0px'
			},
			/*style:{
			 borderBottom:'1px solid #D0D0D0'
			 }, */
			defaults:{
				minWidth:550
			},
			items:[
				{
					xtype:'hidden',
					name:'bytes',
					id:'input_bytes'
				},{
					xtype: 'component',
					cls: 'infoBox_bottomBorder',
					border:true,
					html: getText('item.tabs.attachment.addScreenshot.explanation')
				},{
					xtype:'panel',
					layout:{
						type:'hbox'
					},
					border:false,
					bodyBorder:false,
					anchor:'100%',
					margin:'0 0 0 0',
					padding:0,
					items:[
						{
							xtype:'panel',
							border:false,
							bodyBorder:false,
							layout:'anchor',
							width:320,
							items:[
								Ext.create('Ext.form.FieldSet',{
									margin: '5 10 0 10',
									padding:3,
									width:310,
									title:getText('item.tabs.attachment.lbl.preview'),
									html:'<applet id="AddScreenshotApplet" code="com.trackplus.screenshot.AddScreenshotApplet"'+
										'archive="Screenshot.jar,commons-codec-1.5.jar" codebase="applet"	width="300" height="200" style="background-color:white">'+
										'<param name="prefWidth" value="300">'+
										'<param name="prefHeight" value="200">'+
										'<param name="permissions" value="all-permissions" />'+
										getText('item.tabs.attachment.addScreenshot.appletNotSupported')+
										'</applet>'
								}),
								Ext.create('Ext.panel.Panel',{
									layout:{
										type:'hbox'
									},
									border:false,
									bodyBorder:false,
									margin: '0 10 0 10',
									items:[
										Ext.create('Ext.Button', {
											text:getText('common.btn.paste'),
											iconCls:'copy16',
											handler:me.paste,
											margin:'5 5 5 0',
											scope:me
										}),
										Ext.create('Ext.Button', {
											text:getText('common.btn.clear'),
											iconCls:'delete16',
											handler:me.clearApplet,
											margin:'5 5 5 5',
											scope:me
										})
									]
								})
							]
						},{
							xtype:'panel',
							border:false,
							bodyBorder:false,
							flex:1,
							layout:'anchor',
							height:235,
							items:[
								{
									xtype:'textfield',
									fieldLabel: getText('item.tabs.attachment.lbl.file'),
									labelWidth: 75,
									labelAlign:'right',
									margin:'15 10 5 10',
									anchor: '100%',
									value:'screenshot',
									name:'file'
								},{
									xtype:'textarea',
									fieldLabel: getText('common.lbl.description'),
									name:'description',
									labelWidth: 75,
									margin:'5 10 10 10',
									labelAlign:'right',
									anchor: '100% -35'
								}
							]
						}
					]
				}
			]
		});
		dialogCfg.w=750;
		dialogCfg.h=400;
		dialogCfg.autoScroll=false;
		dialogCfg.layout='fit';
		dialogCfg.iconCls='attachment16';
		dialogCfg.validate=me.updateBytes;
		dialogCfg.successHandler=function(result){
			//ATTACHMENT_SYMBOL = -1001;
			var lastModified=result.data.lastModified;
			me.fireItemChange([-1001, 15],lastModified);
			me.refresh.call(me);
		};
		com.trackplus.itemDetail.openDialog(dialogCfg);
	},
	paste:function() {
		var CLIPBOARD_STATUS_OK=0;
		var CLIPBOARD_STATUS_EMPTY=1;
		var CLIPBOARD_STATUS_NOT_SUPPORTED=2;
		var CLIPBOARD_STATUS_UNKNOW=3;
		var javaEnabled=navigator.javaEnabled();
		var msgAppletNotSupported=getText('item.tabs.attachment.addScreenshot.appletNotSupported');
		var msgEmptyClipboard=getText('item.tabs.attachment.addScreenshot.emptyClipbaord');
		var msgClipboardNotSupported=getText('item.tabs.attachment.addScreenshot.clipboardNotSupported');
		var msgUnknownError=getText('item.tabs.attachment.addScreenshot.unknownError');
		if(javaEnabled==false){
			CWHF.showMsgError(msgAppletNotSupported);
			return false;
		}
		try {
			var applet = document.getElementById("AddScreenshotApplet");
			var err = applet.pasteFromClipboard();
			switch( err ) {
				case CLIPBOARD_STATUS_OK:
					break;
				case CLIPBOARD_STATUS_EMPTY:
					CWHF.showMsgError(msgEmptyClipboard);
					break;
				case CLIPBOARD_STATUS_NOT_SUPPORTED:
					CWHF.showMsgError(msgClipboardNotSupported);
					break;
				case CLIPBOARD_STATUS_UNKNOW:
					CWHF.showMsgError(msgUnknownError);
					break;
				default:
					CWHF.showMsgError( "Unknown error code: "+err );
			}
		}catch( e ) {
			CWHF.showMsgError(e);
			throw e;
		}
		return false;
	},
	clearApplet:function(){
		var me=this;
		var javaEnabled=navigator.javaEnabled();
		if(javaEnabled==false){
			CWHF.showMsgError(msgAppletNotSupported);
			return false;
		}
		document.getElementById('AddScreenshotApplet').clear();
		return false;
	},
	updateBytes:function() {
		var me=this;
		var msgNoData=getText('item.tabs.attachment.addScreenshot.noData');
		var cmpBytes=Ext.getCmp("input_bytes");
		cmpBytes.setValue(null);
		var applet = document.getElementById( "AddScreenshotApplet" );
		var bytes = applet.getEncodedString();
		if(bytes==null||bytes==""){
			CWHF.showMsgError(msgNoData);
			return false;
		}
		cmpBytes.setValue(bytes);
		return true;
	},

	addLink: function() {
		var me=this;
		var dialogCfg= new com.trackplus.itemDetail.DialogConfig(me.workItemID,me.projectID,me.issueTypeID);
		dialogCfg.title=getText("item.tabs.attachment.lbl.add");
		//url:addAttachment
		dialogCfg.formPanel=Ext.create('Ext.form.Panel', {
			itemId :'editAttachment',
			//autoScroll:true,
			layout:'anchor',
			border	: false,
			url: 'saveAttachment!saveLink.action?lastModified='+me.lastModified,
			margin: '0 0 0 0',
			padding:0,
			bodyPadding:0,
			fileUpload: true,
			items:[
			   {
					xtype:'textfield',
					fieldLabel: 'Url',
					name:'theUrl',
					labelWidth: 75,
					labelAlign:me.labelHAlign,
					anchor: '100%',
					allowBlank: false,
					margin: '18 15 0 0',
					vtype:'url',
					style:{
						marginLeft:'10px',
						marginRight:'10px'
					}
				},{
					xtype:'textarea',
					fieldLabel: getText('common.lbl.description'),
					name:'description',
					labelWidth: 75,
					labelAlign:me.labelHAlign,
					height:75,
					anchor: '100% -90',
					margin: '10 15 0 0',
					style:{
						marginLeft:'10px',
						marginRight:'10px'
					}
				}
			]
		});
		dialogCfg.layout="fit";
		dialogCfg.w=500;
		dialogCfg.h=275;
		dialogCfg.minw=450;
		dialogCfg.minh=200;
		dialogCfg.autoScroll=false;
		dialogCfg.iconCls='attachment16';
		dialogCfg.successHandler=function(result){
			//ATTACHMENT_SYMBOL = -1001;
			var lastModified=result.data.lastModified;
			me.fireItemChange([-1001, 15],lastModified);
			me.refresh.call(me);

		};

		com.trackplus.itemDetail.openDialog(dialogCfg);
	}
});
