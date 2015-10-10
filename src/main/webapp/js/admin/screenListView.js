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


/* Copyright (C) 2011 Trackplus
 * $Id: screenListView.js 3978 2012-09-27 16:09:05Z friedj $
 */

Ext.define('com.trackplus.admin.ScreenListGridConfig',{
	extend:'com.trackplus.admin.GridConfig',
	urlStore:'indexScreens.action',
	fields:[
		{name: 'id',type:'int'},
		{name: 'name',type:'string'},
		{name: 'tagLabel',type:'string'},
		{name: 'description', type: 'string'},
		{name: 'owner', type: 'string'}
	],
	columnModel:[
		{header: getText('common.lbl.name'),
			width:220,dataIndex: 'name',id:'name',sortable:true,
			filterable: true},
		{header: getText('common.lbl.tagLabel'),
			width:220,dataIndex: 'tagLabel',id:'tagLabel',sortable:true,
			filter: {}},
		{header: getText('common.lbl.description'),flex:1,
			dataIndex: 'description',id:'description',sortable:true,
			filterable: false},
		{header: getText('admin.customize.form.config.owner'),
			width:220,dataIndex: 'owner',id:'owner',sortable:true,
			filterable: false}
	],
	features: [{
		ftype: 'filters',
		encode: false, // json encode the filter query
		local: true,   // defaults to false (remote filtering)
		filters: [{
				type: 'string',
				dataIndex: 'tagLabel',
				disabled: false
			},{
				type: 'string',
				dataIndex: 'name',
				disabled: false
			}
		]
	}],
	confirmDeleteEntity:true,
	baseAction:'indexScreens',
	entityID:'screenID',
	editWidth:500,
	editHeight:300,
	useCopy:true,
	getEntityLabel:function(){
		//getText
		return 'Form';
	},
	/*errorHandlerDelete:function(entityJS,errorCode,errorMessage){
		var me=this;
		if(errorCode==1){
			//ERROR_NEED_REPLACE
			me.replaceDeletedLinkType(entityJS);
		}else{
			alert("error delete: code:"+errorCode+" message="+errorMessage);
		}
	},*/
	createEditForm:function(entityJS,type){
		var me=this;
		var panelForm= new Ext.form.FormPanel({
			url:'indexScreens!save.action',
			fieldDefaults: {
				labelWidth: 150
			},
			region:'center',
			layout:'anchor',
			border:false,
			bodyBorder:false,
			autoScroll: true,
			bodyStyle: 'padding:10px',
			defaultType: 'textfield',
			items: [{
					fieldLabel: getText('common.lbl.name'),
					name: 'screen.name',
					anchor:'100%',
					allowBlank:false
				},{
					fieldLabel: getText('common.lbl.tagLabel'),
					name: 'screen.tagLabel',
					anchor:'100%',
					allowBlank:true
				},{
					xtype:'textareafield',
					fieldLabel:getText('common.lbl.description'),
					name: 'screen.description',
					anchor:'100%-75',//anchor width by percentage
					allowBlank:false
				}
			]
		});
		return panelForm;
	},
	initActions:function(){
		var me=this;
		me.callParent();
		var designAction=me.createAction(getText('common.btn.config'), 'btnConfig', me.onConfig, false);
		me.actions.push(designAction);
	},
	onConfig:function(){
		var me=this;
		var recordData=me.getSingleSelectedRecordData();
		if(recordData==null){
			return false;
		}
		var id=recordData.id;
		window.location.href='screenEdit.action?componentID='+id+'&backAction=cockpit.action';
	}

});

