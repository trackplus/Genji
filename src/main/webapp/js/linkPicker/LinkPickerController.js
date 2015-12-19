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

com.trackplus.linkPicker.LinkPickerController=Ext.define('com.trackplus.linkPicker.LinkPickerController',{
	extend:'Ext.Base',
	config: {
		workItemID:null,
		projectID:null,
		useDocument:false,
		parent:false,
		title:"",
		handler:null,
		scope:null,
		ajaxContext:null,
		selectedProjectOrReleaseName:null,
		selectedProjectOrRelease:null,
		selectedQueryName:null,
		selectedQueryID:null,
		selectedDatasourceType:1,
		width:800,
		height:500,
		stateId:null,
		linkType:0,
		linkText:'',
		linkURL:'',
		linkObjectID:null,
		linkTarget:null
	},
	statics:{
		LINKTYPE:{
			EXTERNAL:0,
			ATTACHMENT:1,
			WORKITEM:2,
			DOCUMENT:3
		}
	},
	view:null,
	win:null,
	constructor : function(config) {
		var me = this
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		this.initConfig(config);
	},
	showDialog:function(){
		var me=this;
		me.linkPikerView=Ext.create('com.trackplus.linkPicker.LinkPickerView',{
			linkPikerController:me,
			linkText:me.getLinkText(),
			linkURL:me.getLinkURL(),
			linkTarget:me.getLinkTarget(),
			useDocument:me.getUseDocument(),
			selectedProjectOrReleaseName:me.selectedProjectOrReleaseName,
			selectedProjectOrRelease:me.selectedProjectOrRelease,
			selectedQueryName:me.selectedQueryName,
			selectedQueryID:me.selectedQueryID,
			selectedDatasourceType:me.selectedDatasourceType
		});
		var  linkType=me.getLinkType();
		me.setActiveLinkType(linkType);
		me.linkPikerView.on('linkTypeChange',me.linkTypeChanged,me);
		me.linkPikerView.setSelectedLinkTypeNode(linkType);


		if(me.win){
			me.win.destroy();
		}

		me.win = Ext.create('Ext.window.Window',{
			layout      : 'fit',
			//iconCls:'buttonParent',
			width       : me.getWidth(),
			minWidth  : 600,
			minHeight  : 400,
			height      : me.getHeight(),
			closeAction :'destroy',
			plain       : true,
			title		:me.getTitle(),
			modal       :true,
			items       :[me.linkPikerView],
			border:false,
			bodyBorder:true,
			margin:'0 0 0 0',
			style:{
				padding:'5px 0px 0px 0px'
			},
			autoScroll  :false,
			stateId:me.getStateId(),
			stateful:me.getStateId(),
			listeners:{
				'staterestore':{
					fn:function(dialog,state){
						var w=state.width;
						var h=state.height;
						var x=null;
						var y=null;
						if(state.pos){
							x=state.pos[0];
							y=state.pos[1];
						}
						var size=borderLayout.ensureSize(state.width,state.height);
						if(dialog.x<0){
							dialog.x=10;
						}
						if(dialog.y<0){
							dialog.y=10;
						}
						if(w!==size.width){
							dialog.width=size.width;
							dialog.x=10;
						}
						if(h!==size.height){
							dialog.height=size.height;
							dialog.y=10;
						}
					}
				}
			},
			buttons: [
				{
					text: "OK",
					handler  :me.okHandler,
					scope:me
				},{
					text : "Cancel",
					handler:  function(){
						me.win.destroy();
					}
				}
			]
		});
		me.win.show();
	},
	okHandler:function(){
		var me=this;
		me.win.destroy();
		if(me.handler){
			var type=me.getTypeName();
			var link={
				href:me.linkPikerView.txtURL.getValue(),
				html:me.linkPikerView.txtLinkText.getValue(),
				target:me.linkPikerView.txtTarget.getValue(),
				type:type
			};
			me.handler.call(me.scope,link);
		}
	},

	linkTypeChanged:function(linkType){
		var me=this;
		var originalLinkType=me.getLinkType();
		if(originalLinkType!=linkType){
			me.setLinkType(linkType);
			me.setActiveLinkType(linkType);
		}
	},
	activateAttachmentPanel:function(){
		var me=this;
		/*var pan=Ext.create('Ext.panel.Panel',{
			border:false,
			bodyBorder:false,
			padding:'10 10 10 10',
			html:'attachments'
		});*/


		var store = Ext.create('Ext.data.Store', {
			fields:[
				{name:'id', type:'int'},
				{name:'fileName', type:'string'},
				{name:'fileSize', type:'int'},
				{name:'description', type:'string'},
				{name:'date', type:'date', dateFormat: com.trackplus.TrackplusConfig.ISODateTimeFormat},
				{name:'author', type:'string'},
				{name:'authorID', type:'string'},
				{name: 'editable', type: 'boolean'},
				{name: 'thumbnail', type: 'boolean'},
				{name: 'isURL', type: 'boolean'}
			]
		});




		me.gridAttachemnt=  Ext.create('Ext.grid.Panel', {
			cls:'itemDetailGrid',
			store	  : store,
			columns: [
				{
					text     : getText('common.lbl.attachment.file'),
					width:150,
					hideable:false,
					dataIndex: 'fileName'
				},{
					text     : getText('common.lbl.size'),
					width:100,
					hideable:false,
					dataIndex: 'fileSize',
					renderer:function(value){
						return Ext.util.Format.fileSize(value);
					}
				},{
					text     : getText('common.lbl.attachment.description'),
					flex:1,
					hideable:false,
					dataIndex: 'description'
				}
			],
			border	: false,
			bodyBorder:false,
			columnLines :true,
			viewConfig: {
				stripeRows: true
			}
		});
		me.gridAttachemnt.addListener("select",me.gridAttachmentSelect,me);
		me.linkPikerView.setActiveDetailPanel(me.gridAttachemnt);
		me.linkPikerView.setLoading(true);

		var urlStr='itemAttachments.action?workItemID='+me.workItemID+"&projectID="+me.projectID;
		Ext.Ajax.request({
			url: urlStr,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				//alert("responseJson="+responseJson);
				store.loadData(responseJson);
				me.linkPikerView.setLoading(false);
			},
			failure: function(response){
				me.linkPikerView.setLoading(false);
			}
		});
	},
	gridAttachmentSelect:function( grid, record, index, eOpts ){
		var me=this;
		var attachID=record.data['id'];
		var url='downloadAttachment.action?workItemID='+me.workItemID+'&attachKey='+attachID;
		me.linkPikerView.txtURL.setValue(url);
		me.linkPikerView.txtLinkText.setValue(record.data['fileName']);
	},
	activateWorkItemPanel:function(){
		var me=this;
		var issuePicker=Ext.create('com.trackplus.util.IssuePicker',{
			title:getText('common.btn.chooseItem'),
			projectID:me.projectID==null?null:me.projectID*-1,
			handler:me.insertIssue,
			scope:me
		});
		var pan=issuePicker.createPickerView();

		me.linkPikerView.setActiveDetailPanel(pan);
		issuePicker.search();

	},
	insertIssue:function(item){
		var me=this;
		var key=item['objectID'];
		var id=item['id'];
		var title=item['title'];

		var url='printItem.action?workItemID='+key;
		me.linkPikerView.txtURL.setValue(url);
		me.linkPikerView.txtLinkText.setValue(title);
	},
	insertDocument:function(item){
		var me=this;
		var key=item['objectID'];
		var id=item['id'];
		var text=item['text'];

		var url='wiki.action?workItemID='+key;
		me.linkPikerView.txtURL.setValue(url);
		me.linkPikerView.txtLinkText.setValue(text);
	},

	activateDocumentPanel:function(){
		var me=this;
		me.linkPikerView.setLoading(true);
		if(me.getUseDocument()==true){
			var urlStr="wiki!getDocumentProjects.action";
			Ext.Ajax.request({
				url: urlStr,
				scope:me,
				params:{
					projectID:me.getProjectID()
				},
				success: function(response){
					var responseJson = Ext.decode(response.responseText);
					var docuemntPicker=Ext.create('com.trackplus.wiki.DocumentPicker',{
						title:'Choose docuemnt',
						projectID:me.getProjectID(),
						projects:responseJson,
						handler:me.insertDocument,
						scope:me
					});
					var docuemntPicker=docuemntPicker.createPickerView();
					Ext.apply(docuemntPicker,{region:'center'});

					var txtSearch=Ext.create('Ext.form.field.Text',{
						cls: 'searchfield',
						width:250,
						emptyText:getText('common.btn.search')
					});
					var northPanel=Ext.create('Ext.panel.Panel',{
						border:false,
						bodyBorder:false,
						region:'north',
						padding:'10 10 10 10',
						style:{
							borderBottom:'1px solid #c0c0c0'
						},
						items:[txtSearch]
					});

					var pan=Ext.create('Ext.panel.Panel',{
						border:false,
						bodyBorder:false,
						layout:'border',
						items:[northPanel,docuemntPicker]
					});

					me.linkPikerView.setActiveDetailPanel(pan);
					me.linkPikerView.setLoading(false);
				},
				failure:function(){
					me.linkPikerView.setLoading(false);
				}
			});
		}else{
			var pan=Ext.create('Ext.panel.Panel',{
				border:false,
				bodyBorder:false,
				html:'Link picker is config to not show docuemnt picker!'
			});
			me.linkPikerView.setActiveDetailPanel(pan);
		}

	},
	activateExternalLinkPanel:function(){
		var me=this;
		var pan=Ext.create('Ext.panel.Panel',{
			border:false,
			bodyBorder:false,
			padding:'10 10 10 10',
			html:getText('linkPicker.externalLink.msg')
		});
		me.linkPikerView.setActiveDetailPanel(pan);
	},
	setActiveLinkType:function(linkType){
		var me=this;
		var readOnlyURL=false;
		switch(linkType){
			case com.trackplus.linkPicker.LinkPickerController.LINKTYPE.ATTACHMENT:{
				readOnlyURL=true;
				me.activateAttachmentPanel();
				break;
			}
			case com.trackplus.linkPicker.LinkPickerController.LINKTYPE.WORKITEM:{
				readOnlyURL=true;
				me.activateWorkItemPanel();
				break;
			}
			case com.trackplus.linkPicker.LinkPickerController.LINKTYPE.DOCUMENT:{
				readOnlyURL=true;
				me.activateDocumentPanel();
				break;
			}
			default:{
				readOnlyURL=false;
				me.activateExternalLinkPanel();
			}
		}
		me.linkPikerView.txtURL.setReadOnly(readOnlyURL);
	},

	getTypeName:function(){
		var me=this;
		var linkType=me.getLinkType();
		var name;

		switch(linkType){
			case com.trackplus.linkPicker.LinkPickerController.LINKTYPE.ATTACHMENT:{
				name="attachment";
				break;
			}
			case com.trackplus.linkPicker.LinkPickerController.LINKTYPE.WORKITEM:{
				name="workItem";
				break;
			}
			case com.trackplus.linkPicker.LinkPickerController.LINKTYPE.DOCUMENT:{
				name="docuemnt";
				break;
			}
			default:{
				name=null;
			}
		}
		return name;
	}
});
