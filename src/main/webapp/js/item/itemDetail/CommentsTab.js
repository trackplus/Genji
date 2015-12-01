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


Ext.define('com.aurel.trackplus.itemDetail.CommentsTab',{
	extend:'com.aurel.trackplus.itemDetail.TabGrid',
	readOnly:false,
	cls:'ulist',
	urlDeleteItems:'deleteComments.action',
	fieldsToNotifyOnDelete:[23, 15],
	initComponent : function(){
		var me = this;
		var commentNumber=0;
		if(me.jsonData.commentNumber){
			commentNumber=me.jsonData.commentNumber;
		}
		me.title=getText('item.tabs.comment.lbl.title')+" ("+commentNumber+")";
		// me.iconCls='comment16';
		me.gridConfig=me.createCommentsConfig();
		me.callParent();
	},
	refreshCallback:function(r,options,success){
		var me=this;
		var size=r.length;
		me.setTitle(me.replaceTitleNumber(me.title,size));
	},
	createCommentsConfig:function(){
		var me=this;
		if(me.jsonData){
			me.readOnly=me.jsonData.readOnly;
		}
		var gridConfig=new com.trackplus.itemDetail.GridConfig();
		gridConfig.tabID="commentsTab";
		gridConfig.id=2;
		gridConfig.urlStore='itemComments.action?workItemID='+me.workItemID+"&projectID="+me.projectID+"&issueTypeID="+me.issueTypeID;
		gridConfig.fields=[
			{name: 'id',type: 'int'},
			{name: 'date',type: 'date',  dateFormat: com.trackplus.TrackplusConfig.ISODateTimeFormat},
			{name: 'author',type: 'string'},
			{name: 'authorID',type: 'string'},
			{name: 'comment',type: 'string'},
			{name: 'editable',type: 'bool'}
		];
		gridConfig.selectionModel = Ext.create('Ext.selection.CheckboxModel', {
			listeners: {
				selectionchange: function(sm, selections) {
					var btnDelete=me.gridConfig.grid.down("#deleteCommentBtn");
					var btnEdit=me.gridConfig.grid.down("#editCommentBtn");
					if(CWHF.isNull(selections)||selections.length===0){
						btnDelete.setDisabled(true);
						btnEdit.setDisabled(true);
					}else{
						if(selections.length===1){
							var rowData=selections[0].data;
							btnEdit.setDisabled(rowData.editable===false);
							btnDelete.setDisabled(rowData.editable===false);
						}else{
							btnEdit.setDisabled(true);
							var enableDelete=false;
							for(var i=0;i<selections.length;i++){
								var rowData=selections[i].data;
								if(rowData.editable===true){
									enableDelete=true;
									break;
								}
							}
							btnDelete.setDisabled(!enableDelete);
						}
					}
				}
			}
		});
		//toolbar
		gridConfig.tbar=[
			{
				text:com.trackplus.TrackplusConfig.getText('common.btn.add'),
				disabled:me.readOnly,
				iconCls:'add16',
				handler:function(){
					me.addComment.call(me);
				}
			},{
				text:com.trackplus.TrackplusConfig.getText('common.btn.delete'),
				itemId:"deleteCommentBtn",
				disabled:true,
				iconCls:'delete16',
				handler:function(){
					me.deleteItems.call(me);
				}
			},{
				text:com.trackplus.TrackplusConfig.getText('common.btn.edit'),
				itemId:"editCommentBtn",
				disabled:true,
				iconCls:'edit16',
				handler:function(){
					me.editComment.call(me);
				}
			}
		];
		gridConfig.dblClickHandler=me.editComment;

		gridConfig.updateCol=function(col,layout,index){
			if(layout.dataIndex==='comment'){
				col.flex=1;
			}
		};
		return gridConfig;
	},
	addComment:function(){
		this.openEditComment();
	},
	editComment:function(){
		var me=this;
		if(me.gridConfig.grid.getStore().getCount()===1){
			//commentsGridConfig.grid.getSelectionModel().selectFirstRow();
		}
		var selections=me.gridConfig.grid.getSelectionModel().getSelection();
		if(CWHF.isNull(selections)){
			return;
		}
		var rowData=selections[0].data;
		var	localizedComment=com.trackplus.TrackplusConfig.getText('common.history.lbl.comment');
		if(rowData.editable===false){
			com.trackplus.util.showHtmlDetail(localizedComment,rowData.comment);
			return;
		}
		//var originalComment=rowData.comment;
		var commentID=rowData.id;
		borderLayout.setLoading(true);
		Ext.Ajax.request({
			url: "itemDetail!editComment.action",
			params:{commentID:commentID},
			disableCaching:true,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				var jsonData=responseJson.data;
				var originalComment=jsonData.comment;
				borderLayout.setLoading(false);
				me.openEditComment(originalComment,commentID);
			},
			failure: function(){
				borderLayout.setLoading(false);
			},
			method:'POST'

		});
	},
	openEditComment:function(originalValue,commentID){
		var me=this;
		var commentCfg={
			anchor:'100%',
			allowBlank:false,
			cls:'rteField ckeField100Percent'
		};
		if(originalValue){
			commentCfg.value=originalValue;
		}
		var ckeditorCfg={
			workItemID:me.workItemID,
			projectID:me.projectID,
			useInlineTask:true,
			useBrowseImage:true
		}
		me.txtArea=CWHF.createRichTextEditorField('comment',commentCfg,true,true,ckeditorCfg);

		var dialogCfg= new com.trackplus.itemDetail.DialogConfig(me.workItemID,me.projectID,me.issueTypeID);
		dialogCfg.title=com.trackplus.TrackplusConfig.getText("item.tabs.comment.lbl.edit");
		var urlSave='saveComment.action?lastModified='+me.lastModified;
		if(commentID){
			urlSave+='&commentID='+commentID;
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
		dialogCfg.w=850;
		dialogCfg.h=300;
		dialogCfg.layout="fit";
		dialogCfg.autoScrol=false;
		dialogCfg.iconCls='comment16';
		dialogCfg.successHandler=function(result){
			var lastModified=result.data.lastModified;
			me.fireItemChange([23, 15],lastModified);
			me.refresh();
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
});
