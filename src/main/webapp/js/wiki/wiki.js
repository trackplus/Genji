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

Ext.define('com.trackplus.wiki.WikiController',{
	extend:'Ext.Base',
	config:{
		data:{}
	},
	workItemID:null,
	lastNodeType:null,
	lastNodeObjectID:null,
	wikiNavigatorController:null,
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	},
	createWestPanel:function(){
		var me=this;
		if(CWHF.isNull(me.treeWest)){
			me.treeWest=Ext.create('com.trackplus.wiki.WestPanelView', {
				data:me.data,
				treeBaseAction:'wiki'
			});
			me.treeWest.on('itemclick',me.treeNodeClick,me);
			me.treeWest.on('wbsChange',me.refresh,me);

		}
		return me.treeWest;
	},
	treeNodeClick:function(view,node){
		var me=this;
		me.lastNodeType=node.data.nodeType;
		me.lastNodeObjectID=node.data.objectID;
		me.refresh();
	},
	refresh:function(){
		var me=this;
		var nodeObjectID= me.lastNodeObjectID;
		var nodeType=me.lastNodeType;
		if(CWHF.isNull(me.lastNodeObjectID)){
			if(me.data.workItemID){
				me.itemDetail(me.data.workItemID);
			}
		}else{
			if(nodeType==='1'||nodeType==='2'||nodeType==='5'){
				me.executeQuery(nodeType,nodeObjectID);
			}else{
				me.itemDetail(nodeObjectID);
			}
		}
	},
	executeQuery:function(nodeType,nodeObjectID){
		var me=this;
		me.projectID=nodeObjectID;
		borderLayout.setLoading(true);
		var  urlStr='wikiNavigator!filterByNode.action';
		var params={
			nodeType:nodeType,
			nodeObjectID:nodeObjectID
		};
		Ext.Ajax.request({
			url: urlStr,
			disableCaching:true,
			success: function(result){
				me.refreshSuccessHandler.call(me,result,nodeType,nodeObjectID);
			},
			failure: function(){
				borderLayout.setLoading(false);
				CWHF.showMsgError('Failure');
			},
			method:'POST',
			params:params
		});
	},
	refreshSuccessHandler:function(result,nodeType,nodeObjectID){
		var  createView=true;
		var me=this;
		var jsonData=Ext.decode(result.responseText);
		if(jsonData.success===false){
			borderLayout.setLoading(false);
			CWHF.showMsgError(jsonData.errorMessage);
			return false;
		}
		borderLayout.setLoading(false);
		if(CWHF.isNull(me.wikiNavigatorController)){
			var model=com.trackplus.itemNavigator.ItemNavigatorFacade.createInitialModel(jsonData.data);
			me.wikiNavigatorController=Ext.create('com.trackplus.itemNavigator.WikiNavigatorController',{
				model:model,
				baseAction:'wikiNavigator',
				useLastQuery:false
			});
		}else{
			me.wikiNavigatorController.updateModel(jsonData.data);
		}
		me.wikiNavigatorController.nodeType=nodeType;
		me.wikiNavigatorController.nodeObjectID=nodeObjectID;
		borderLayout.setActiveToolbarList(me.wikiNavigatorController.createToolbar.call(me.wikiNavigatorController));
		borderLayout.setVisibleToolbar(true);
		me.centerPanel.removeAll(true);
		var panel=me.wikiNavigatorController.createView();
		me.centerPanel.add(panel);

	},
	itemDetail:function(nodeObjectID){
		var me=this;
		me.workItemID=nodeObjectID;
		wikiEditItems=new Object();
		var urlStr="wiki!item.action";
		borderLayout.setLoading(true);
		Ext.Ajax.request({
			url : urlStr,
			params : {
				'nodeObjectID' : nodeObjectID
			},
			success : function(response) {
				var html = response.responseText;
				me.centerPanel.removeAll(true);
				me.centerPanel.add({
					xtype:'panel',
					border:false,
					bodyBorder:false,
					style:{
				 		borderLeft:'1px solid #D0D0D0'
				 	},
					html:html,
					autoScroll:true
				});

				borderLayout.setActiveToolbarList([]);
				borderLayout.setVisibleToolbar(false);
				borderLayout.setLoading(false);
			},
			failure : function() {
				borderLayout.setLoading(false);
				alert("failed!");
			}
		});
	},
	editDocument:function(nodeObjectID){
		var me=this;
		var urlStr="wiki!item.action";
		borderLayout.setLoading(true);
		Ext.Ajax.request({
			url : urlStr,
			params : {
				'nodeObjectID' : nodeObjectID
			},
			success : function(response) {
				var jsonData = Ext.decode(response.responseText);
				me.refreshItemPanel(jsonData);
				borderLayout.setActiveToolbarList([]);
				borderLayout.setVisibleToolbar(false);
				borderLayout.setLoading(false);
			},
			failure : function() {
				borderLayout.setLoading(false);
				alert("failed!");
			}
		});
	},
	createCenterPanel:function(){
		var me=this;
		if(CWHF.isNull(me.centerPanel)){
			me.centerPanel=Ext.create('Ext.panel.Panel',{
				border:false,
				bodyBorder:false,
				autoScroll: true,
				header:false,
				/*style:{
					 borderLeft:'1px solid #D0D0D0'
				},*/
				bodyStyle:{
					padding:'0px 0px 0px 0px'
				},
				layout:'fit'
			});
		}
		if(me.data.workItemID){
			me.refreshItemPanel(me.data);
		}else{
			var emptyPanel=Ext.create('Ext.panel.Panel',{
				border:false,
				bodyBorder:false,
				bodyPadding:'0 0 0 0',
				padding:'0 0 0 0',
				layout:'anchor',
				defaults:{
					anchor:'100%'
				},
				style:{
					borderLeft:'1px solid #D0D0D0'
				},
				items:[{
					xtype:'component',
					cls:'infoBox_bottomBorder',
					html:getText('wiki.lbl.infoGeneral')
				}]
			});
			me.centerPanel.add(emptyPanel);
		}
		return me.centerPanel;
	},
	refreshItemPanel:function(itemData){
		var me=this;
		me.centerPanel.removeAll(true);
		me.itemPanelView=Ext.create('com.trackplus.wiki.DocumentView',{
			autoScroll: true,
			editable:itemData.wikiEditableMode,
			model:itemData,
			style:{
				borderLeft:'1px solid #D0D0D0'
			}
		});
		me.itemData=itemData;
		//me.itemPanelView.addListener('editMode',me.changeToEditMode,me);
		me.itemPanelView.addListener('itemChanged',me.itemChanged,me);
		me.itemPanelView.addListener('editableChange',me.editableChange,me);
		me.centerPanel.add(me.itemPanelView);
	},
	itemChanged:function(){
		this.refresh();
	},
	editableChange:function(editable){
		var me=this;
		me.data.wikiEditableMode=editable;
		Ext.Ajax.request({
			url: "userPreferences.action?property=wikiEditableMode&value="+editable,
			disableCaching:true,
			success: function(data){
				me.refresh();
			},
			failure: function(type, error){
			}
		});
	},
	exportWiki:function(){
		var me=this;
		window.location.href="docxExport.action?workItemID="+me.workItemID;
	}
});

Ext.define('com.trackplus.layout.WikiLayout',{
	extend:'com.trackplus.layout.BaseLayout',
	useToolbar:true,
	selectedGroup:'wiki',
	constructor : function(config) {
		var me = this;
		me.callParent(arguments);
		me.borderLayoutController.setHelpContext("wiki");
		me.wikiController=Ext.create('com.trackplus.wiki.WikiController',{
			data:me.initData
		});
		me.onReady(function() {
			borderLayout.setActiveToolbarList([]);
			borderLayout.setVisibleToolbar(false);
		});
	},
	createCenterPanel:function(){
		var me=this;
		return me.wikiController.createCenterPanel();
	},
	createWestPanel:function(){
		var me=this;
		return me.wikiController.createWestPanel();
	},
	refresh:function(){
		var me=this;
		me.wikiController.refresh();
	}
});

Ext.define('com.trackplus.itemNavigator.WikiNavigatorController', {
	extend: 'com.trackplus.itemNavigator.ItemNavigatorController',
	changeLayout: function () {
		var me = this;
		me.refresh();
	}
});



//general functions
var wikiEditItems=new Object();
function changeWikiEditMode(editable){
	var wikiLayout=borderLayout.controller.baseLayout;
	wikiLayout.wikiController.editableChange(editable);
}
function wikiTitleChangeToEdit(workItemID,synopsis){
	var divWrapper=document.getElementById("sectionTitleWrapper_"+workItemID);
	wikiEditItems[workItemID+'_synopsis']=divWrapper.innerHTML;
	var cmp='<input type="text" id="'+workItemID+'_synopsis" autocomplete="off" class="someClass" maxlength="255" value="'+synopsis+'" name="fieldValues.f17" spellcheck="true" style="width: 100%; height: 28px;">';
	divWrapper.innerHTML = cmp;
	wikiUpdateButton(workItemID,true);
}
function wikiUpdateButton(workItemID,edit){
	//SAVE=101;
	//CANCEL=102;
	var btnSave=document.getElementById("wikiBtn_"+workItemID+"_101");
	var btnCancel=document.getElementById("wikiBtn_"+workItemID+"_102");
	if(edit===true){
		btnSave.className='wikiBtn';
		btnCancel.className='wikiBtn';
	}else{
		btnSave.className='wikiBtn-hidden';
		btnCancel.className='wikiBtn-hidden';
	}
}
function wikiDescriptionChangeToEdit(workItemID){
	borderLayout.setLoading(true);
	var urlStr="wiki!getWorkItemDescription.action";
	Ext.Ajax.request({
		url : urlStr,
		params : {
			workItemID:workItemID
		},
		success : function(response) {
			borderLayout.setLoading(false);
			var jsonData = Ext.decode(response.responseText);
			var description=jsonData.data.description;
			var divWrapper=document.getElementById("sectionDescriptionWrapper_"+workItemID);
			wikiEditItems[workItemID+'_description']=divWrapper.innerHTML;
			divWrapper.innerHTML = '<textarea id="wikiDescription_'+workItemID+'-inputEl">'+description+'</textarea>';
			CWHF.initRTEditor(null,{
				id:"wikiDescription_"+workItemID,
				focus:true,
				resize:false,
				ckeditorCfg:{
					workItemID:workItemID,
					resize_enabled:true
				}
			});
			wikiUpdateButton(workItemID,true);
		},
		failure : function() {
			borderLayout.setLoading(false);
			alert("failed!");
		}
	});
}

function executeWikiAction(workItemID,projectID,releaseID,issueTypeID,actionID){
	var wikiLayout=borderLayout.controller.baseLayout;
	/*
	 private static final int  NEW_SECTION=1;
	 private static final int  EDIT=2;
	 private static final int  DELETE=3;
	 private static final int  LINKS=4;
	 private static final int  ATTACHMENTS=5;
	 private static final int  COMMENTS=6;
	 private static final int  HISTORY=7;
	 private static final int  SAVE=101;
	 private static final int  CANCEL=102;
	 */
	switch(actionID){
		case 1:{
			var issueTypeID=-5;
			var parentID=workItemID;
			borderLayout.controller.createNewIssue.call(borderLayout.controller,issueTypeID,projectID,releaseID,null,parentID);
			break;
		}
		case 2:{
			var actionID=2;//EDIT
			var itemAction=Ext.create('com.trackplus.item.ItemActionDialog',{
				workItemID:workItemID,
				actionID:actionID,
				parentID:null,
				successHandler:function(){
					var wikiLayout=borderLayout.controller.baseLayout;
					wikiLayout.wikiController.refresh();
				},
				scope:this,
				modal:true
			});
			itemAction.execute.call(itemAction);
			break;
		}
		case 3:{
			wikiDeleteSection(workItemID);
			break;
		}
		case 4:{
			var issuePicker=Ext.create('com.trackplus.util.IssuePicker',{
				workItemID:workItemID,
				parent:true,
				projectID:projectID*-1,
				//projectName:projectName,
				title:getText('common.btn.addLinkedItem'),
				ajaxContext:null,
				handler:wikiAddLinkHandler,
				scope:{
					projectID:projectID,
					workItemID:workItemID,
					issueTypeID:issueTypeID
				}
			});
			issuePicker.showDialog();
			break;
		}
		case 5:{
			wikiAddAttachment(workItemID,projectID,issueTypeID);
			break;
		}
		case 6:{
			wikiOpenEditComment(workItemID,projectID,issueTypeID);
			break;
		}
		case 7:{
			//history
			showHideHistory(workItemID);
			break;
		}
		case 101:{
			//save
			saveWikiSection(workItemID);
			break;
		}
		case 102:{
			//cancel
			wikiUpdateButton(workItemID,false);
			var originalDescription=wikiEditItems[workItemID+'_description'];
			if(originalDescription){
				wikiEditItems[workItemID+'_description']=null;
				var divWrapper=document.getElementById("sectionDescriptionWrapper_"+workItemID);
				divWrapper.innerHTML = originalDescription;
			}

			var originalSynopsis=wikiEditItems[workItemID+'_synopsis'];
			if(originalSynopsis){
				wikiEditItems[workItemID+'_synopsis']=null;
				var divWrapper=document.getElementById("sectionTitleWrapper_"+workItemID);
				divWrapper.innerHTML = originalSynopsis;
			}
			break;
		}
	}
}
function wikiOpenEditComment(workItemID,projectID,issueTypeID,originalValue,commentID){
	var me=this;
	var commentCfg={
		anchor:'100%',
		allowBlank:false,
		cls:'ckeField100Percent'
	};
	if(originalValue){
		commentCfg.value=originalValue;
	}
	me.txtArea=CWHF.createRichTextEditorField('comment',commentCfg,true,true);

	var dialogCfg= new com.trackplus.itemDetail.DialogConfig(workItemID,projectID,issueTypeID);
	dialogCfg.title=com.trackplus.TrackplusConfig.getText("item.tabs.comment.lbl.edit");
	var urlSave='saveComment.action';
	if(commentID){
		urlSave+='?commentID='+commentID;
	}
	dialogCfg.formPanel=Ext.create('Ext.form.Panel', {
		cls:'ckeditor100Percent-noBorder',
		itemId :'editCommentForm',
		border	: false,
		bodyBorder: false,
		layout:'fit',
		margin: '0 0 0 0',
		bodyStyle:{
			padding:'0px'
		},
		/*style:{
		 borderBottom:'1px solid #D0D0D0'
		 },*/
		url: urlSave,
		items:[me.txtArea]
	});
	dialogCfg.w=750;
	dialogCfg.h=300;
	dialogCfg.layout="fit";
	dialogCfg.autoScrol=false;
	dialogCfg.iconCls='comment16';
	dialogCfg.successHandler=function(result){
		var wikiLayout=borderLayout.controller.baseLayout;
		wikiLayout.wikiController.refresh();
	};
	dialogCfg.validate=function(){
		var idDescription=me.txtArea.id+'-inputEl';
		var o=CKEDITOR.instances[idDescription];
		if(o){
			o.updateElement();
		}
		var form=this.formPanel.getForm();

		return (form.isValid());
	};
	com.trackplus.itemDetail.openDialog(dialogCfg);
}

function wikiDeleteSection(workItemID){
	var messageConfirmDelete=com.trackplus.TrackplusConfig.getText("common.lbl.messageBox.removeSelected.confirm");
	var titleDelete=com.trackplus.TrackplusConfig.getText("common.btn.delete")
	Ext.MessageBox.show({
		title:titleDelete,
		msg: messageConfirmDelete,
		buttons: Ext.MessageBox.YESNO,
		fn: function(btn){
			if(btn==="yes"){
				wikiDoDeleteItem(workItemID);
			}
		},
		icon: Ext.MessageBox.QUESTION
	});
}

function wikiDoDeleteItem(workItemID){
	borderLayout.setLoading(true);
	var urlStr='item!reverseDelete.action?workItemID='+workItemID;
	Ext.Ajax.request({
		url: urlStr,
		success: function(response){
			borderLayout.setLoading(false);
			var responseJson = Ext.decode(response.responseText);
			if (responseJson.success===true) {
				var wikiLayout=borderLayout.controller.baseLayout;
				wikiLayout.wikiController.refresh();
			}else{
				if (responseJson.errorMessage) {
					//parent change for an issue
					Ext.MessageBox.show({
						title: getText('common.warning'),
						msg: responseJson.errorMessage,
						buttons: Ext.Msg.OK,
						icon: Ext.MessageBox.ERROR
					});
				}
			}
		},
		failure:function(){
			borderLayout.setLoading(false);
			var jsonData=Ext.decode(action.response.responseText);
			//com.trackplus.item.ItemErrorHandler.handleErrors(me,jsonData.data);
			//me.handleErrors.call(me,jsonData.data);
		}
	});
}

function wikiAddLinkHandler(item){
	var me=this;
	var key=item['objectID'];
	var id=item['id'];
	borderLayout.setLoading(true);
	var urlStr="itemLink!saveItemLink.action";
	var linkTypeWithDirection="1_1";
	var params={
		issueTypeID:me.issueTypeID,
		linkTypeWithDirection:linkTypeWithDirection,
		linkedNumber:id,
		linkedWorkItemID:key,
		projectID:me.projectID,
		workItemID:me.workItemID
	};
	Ext.Ajax.request({
		url : urlStr,
		params :params,
		success : function(response) {
			borderLayout.setLoading(false);
			var wikiLayout=borderLayout.controller.baseLayout;
			wikiLayout.wikiController.refresh();
		},
		failure : function() {
			borderLayout.setLoading(false);
			alert("failed!");
		}
	});
}


function wikiAddAttachment(workItemID,projectID,issueTypeID){
	var dialogCfg= new com.trackplus.itemDetail.DialogConfig(workItemID,projectID,issueTypeID);
	dialogCfg.title=getText("item.tabs.attachment.lbl.add");
	//url:addAttachment
	dialogCfg.formPanel=Ext.create('Ext.form.Panel', {
		itemId :'editAttachment',
		//autoScroll:true,
		layout:'anchor',
		border	: false,
		url: 'saveAttachment.action',
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
				html: getText('item.tabs.attachment.explanation','4M'),
				anchor: '100%',
				border:true,
				cls: 'infoBox_bottomBorder'
			},{
				xtype: 'filefield',
				name: 'theFile',
				fieldLabel: getText('item.tabs.attachment.lbl.file'),
				labelWidth: 75,
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
	dialogCfg.successHandler=function(){
		var wikiLayout=borderLayout.controller.baseLayout;
		wikiLayout.wikiController.refresh();
	};
	com.trackplus.itemDetail.openDialog(dialogCfg);
}

function saveWikiSection(workItemID){
	var synopsis=null;
	var txtTitle=document.getElementById(workItemID+'_synopsis');
	if(txtTitle){
		synopsis=txtTitle.value;
		if(CWHF.isNull(synopsis)||synopsis===''){

			return;
		}
	}
	var description=null;
	var idDescription='wikiDescription_'+workItemID+'-inputEl';
	var txtDescription=document.getElementById(idDescription);
	if(txtDescription){
		var ckEditor=CKEDITOR.instances[idDescription];
		if (ckEditor){
			ckEditor.updateElement();
		}
		description=txtDescription.value;
	}
	borderLayout.setLoading(true);
	var urlStr="wiki!save.action"
	var params={
		workItemID : workItemID
	}
	if(synopsis){
		params['synopsis']=synopsis;
	}
	if(description){
		params['description']=description;
	}
	Ext.Ajax.request({
		url : urlStr,
		params :params,
		success : function(response) {
			var jsonData = Ext.decode(response.responseText);
			if (jsonData.success===true) {
				borderLayout.setLoading(false);
				var wikiLayout=borderLayout.controller.baseLayout;
				wikiLayout.wikiController.refresh();
			}else{
				borderLayout.setLoading(false);
				if (jsonData.errorMessage) {
					//parent change for an issue
					Ext.MessageBox.show({
						title: getText('common.warning'),
						msg: jsonData.errorMessage,
						buttons: Ext.Msg.OK,
						icon: Ext.MessageBox.ERROR
					});
				}
			}
		},
		failure : function() {
			borderLayout.setLoading(false);
			alert("failed!");
		}
	});
}

function wikiExpanCollapseFieldSet(workItemID,id){
	var fieldSetCmp=document.getElementById(workItemID+"_"+id);

	//var bodyFieldSet=document.getElementById(workItemID+"_"+id+"_body");
	if(fieldSetCmp.className==='wikiFieldSet-collapsed'){
		fieldSetCmp.className='wikiFieldSet';
	}else{
		fieldSetCmp.className='wikiFieldSet-collapsed';
	}
}
function removeWikiLink(workItemID,linkID){
	var me=this;
	var messageConfirmDelete=com.trackplus.TrackplusConfig.getText("wiki.msg.removeLink");
	var titleDelete=com.trackplus.TrackplusConfig.getText("common.btn.delete")
	Ext.MessageBox.show({
		title:titleDelete,
		msg: messageConfirmDelete,
		buttons: Ext.MessageBox.YESNO,
		fn: function(btn){
			if(btn==="yes"){
				wikiDoDeleteLink(workItemID,linkID);
			}
		},
		icon: Ext.MessageBox.QUESTION
	});
}

function wikiDoDeleteLink(workItemID,linkID){
	borderLayout.setLoading(true);
	var urlStr='itemLink!deleteLinks.action?workItemID='+workItemID;
	Ext.Ajax.request({
		url: urlStr,
		params:{
			deletedItems:linkID
		},
		success: function(response){
			borderLayout.setLoading(false);
			var responseJson = Ext.decode(response.responseText);
			if (responseJson.success===true) {
				var wikiLayout=borderLayout.controller.baseLayout;
				wikiLayout.wikiController.refresh();
			}else{
				if (responseJson.errorMessage) {
					//parent change for an issue
					Ext.MessageBox.show({
						title: getText('common.warning'),
						msg: responseJson.errorMessage,
						buttons: Ext.Msg.OK,
						icon: Ext.MessageBox.ERROR
					});
				}
			}
		},
		failure:function(){
			borderLayout.setLoading(false);
			var jsonData=Ext.decode(action.response.responseText);
			//com.trackplus.item.ItemErrorHandler.handleErrors(me,jsonData.data);
			//me.handleErrors.call(me,jsonData.data);
		}
	});
}

function removeWikAttachment(workItemID,attachmentID){
	var messageConfirmDelete=com.trackplus.TrackplusConfig.getText("item.tabs.attachment.lbl.delete")+"?";
	var titleDelete=com.trackplus.TrackplusConfig.getText("common.btn.delete")
	Ext.MessageBox.show({
		title:titleDelete,
		msg: messageConfirmDelete,
		buttons: Ext.MessageBox.YESNO,
		fn: function(btn){
			if(btn==="yes"){
				wikiDoDeleteAttachment(workItemID,attachmentID);
			}
		},
		icon: Ext.MessageBox.QUESTION
	});
}
function wikiDoDeleteAttachment(workItemID,attachmentID){
	borderLayout.setLoading(true);
	var urlStr='deletedAttachments.action?workItemID='+workItemID;
	Ext.Ajax.request({
		url: urlStr,
		params:{
			deletedItems:attachmentID
		},
		success: function(response){
			borderLayout.setLoading(false);
			var responseJson = Ext.decode(response.responseText);
			if (responseJson.success===true) {
				var wikiLayout=borderLayout.controller.baseLayout;
				wikiLayout.wikiController.refresh();
			}else{
				if (responseJson.errorMessage) {
					//parent change for an issue
					Ext.MessageBox.show({
						title: getText('common.warning'),
						msg: responseJson.errorMessage,
						buttons: Ext.Msg.OK,
						icon: Ext.MessageBox.ERROR
					});
				}
			}
		},
		failure:function(){
			borderLayout.setLoading(false);
			var jsonData=Ext.decode(action.response.responseText);
			//com.trackplus.item.ItemErrorHandler.handleErrors(me,jsonData.data);
			//me.handleErrors.call(me,jsonData.data);
		}
	});
}

function showHideHistory(workItemID){
	var historyID=workItemID+"_history";
	var sectionDom=document.getElementById('wikiSection_'+workItemID);
	var historyDom=document.getElementById(historyID);
	if(CWHF.isNull(historyDom)){
		var historyDom = document.createElement('fieldset');
		historyDom.setAttribute('id',historyID);
		historyDom.setAttribute('class','wikiFieldSet');
		var historyLegend=document.createElement('legend');
		historyLegend.innerHTML='<a href="javascript:wikiExpanCollapseFieldSet('+workItemID+",'history')"+'">'+
			'<table style="table-layout:fixed\">'+
			"<tr><td>"+
			'<div class="x-tool-img">&nbsp;</div>'+
			'</td><td><div class="wikiLinksLabel">'+getText('item.printItem.lbl.tab.commonHistory')+"</div>"+
			"</td></tr></table></a>";
		historyDom.appendChild(historyLegend);
		var historyBody=document.createElement('div');
		historyBody.setAttribute('id',workItemID+"_history_body");
		historyBody.setAttribute('class','fieldset_body');
		historyDom.appendChild(historyBody);
		sectionDom.appendChild(historyDom);
		borderLayout.setLoading(true);
		var urlStr='itemDetailHistory.action?workItemID='+workItemID;
		Ext.Ajax.request({
			url: urlStr,
			disableCaching:true,
			success: function(response){
				//var responseJson = Ext.decode(response.responseText);
				//var jsonData=responseJson;
				var htmlData=response.responseText;
				//var html='<div class="wikiHistoryLabel">'+getText('item.printItem.lbl.tab.commonHistory')+'</div>'+me.flatData;
				historyBody.innerHTML=htmlData;
				borderLayout.setLoading(false);
			},
			failure: function(){
				borderLayout.setLoading(false);
			},
			method:'POST'
		});
	}else{
		sectionDom.removeChild(historyDom);
	}
}
