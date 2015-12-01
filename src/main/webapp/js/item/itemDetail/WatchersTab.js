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


Ext.define('com.aurel.trackplus.itemDetail.WatchersTab',{
	extend:'com.aurel.trackplus.itemDetail.TabGrid',
	urlDeleteItems:'deleteWatchers.action',
	//readOnly:false,
	myID:0,
	meAsC:false,
	meAsI:false,
	watcherAddDeleteDisabled:false,
	watcherMeConsultedDisabled:false,
	watcherMeInformedDisabled:false,
	fieldsToNotifyOnDelete:[-1003,-1004],
	initComponent : function(){
		var me = this;
		var watchersNumber=0;
		if(me.jsonData.watchersNumber){
			watchersNumber=me.jsonData.watchersNumber;
		}
		me.title=getText('item.printItem.lbl.watchers')+" ("+watchersNumber+")";
		// me.iconCls='watcher16';
		me.gridConfig=me.createWatchersConfig();
		me.callParent();
	},
	createWatchersConfig:function(){
		var me=this;
		if(me.jsonData){
			//me.readOnly=me.jsonData.readOnly;
			me.myID=me.jsonData.myID;
			me.meAsC=me.jsonData.meAsC;
			me.meAsI=me.jsonData.meAsI;
			me.watcherAddDeleteDisabled=me.jsonData.watcherAddDeleteDisabled;
			me.watcherMeConsultedDisabled=me.jsonData.watcherMeConsultedDisabled;
			me.watcherMeInformedDisabled=me.jsonData.watcherMeInformedDisabled;
		}
		var gridConfig=new com.trackplus.itemDetail.GridConfig();
		gridConfig.tabID="watchersTab";
		gridConfig.id=5;
		gridConfig.urlStore='itemWatcher.action?workItemID='+me.workItemID+"&projectID="+me.projectID+"&issueTypeID="+me.issueTypeID;
		gridConfig.fields=[
			{name: 'objectID',type:'int'},
			{name: 'type',type:'string'},
			{name: 'raciRole',type:'string'},
			{name: 'person',type:'string'},
			{name: 'isGroup',type:'string'},
			{name: 'editable',type: 'boolean'}
		];
		//toolbar
		if(!me.watcherAddDeleteDisabled){
			gridConfig.selectionModel = Ext.create('Ext.selection.CheckboxModel', {
				listeners: {
					selectionchange: function(sm, selections) {
						var selections=me.gridConfig.selectionModel.getSelection();
						var btnDelete=me.gridConfig.grid.down("#deleteWatcherBtn");
						if(CWHF.isNull(selections)||selections.length===0){
							btnDelete.setDisabled(true);
						}else{
							btnDelete.setDisabled(false);
						}
					}
				}
			});
		}
		gridConfig.tbar=[
			{
				text:getText('common.btn.addConsultant'),
				disabled:me.watcherAddDeleteDisabled,
				iconCls:'add16',
				//disabled:me.readOnly,
				handler : function(){
					me.addRole.call(me,'c',getText('item.tabs.watchers.lbl.header.consultants'),400,300);
				}
			},{
				text:getText('common.btn.addInformant'),
				disabled:me.watcherAddDeleteDisabled,
				iconCls:'add16',
				handler : function(){
					me.addRole.call(me,'i',getText('item.tabs.watchers.lbl.header.informants'),400,300);
				}
			},{
				text:com.trackplus.TrackplusConfig.getText('common.btn.delete'),
				itemId:"deleteWatcherBtn",
				disabled:true,
				iconCls:'delete16',
				handler:function(){
					me.deleteItems.call(me);
				}
			},{
				text:com.trackplus.TrackplusConfig.getText('common.btn.addMeConsultant'),
				itemId:"addMeAsC",
				disabled:me.watcherMeConsultedDisabled,
				iconCls:'add16',
				handler:function(){
					me.addRemoveMeAsConsultant.call(me);
				}
			},'-',{
				text:com.trackplus.TrackplusConfig.getText('common.btn.addMeInformant'),
				itemId:"addMeAsI",
				disabled:me.watcherMeInformedDisabled,
				iconCls:'add16',
				handler:function(){
					me.addRemoveMeAsInformant.call(me);
				}
			}
		];
		return gridConfig;
	},
	addRole:function(role, name, w, h){
		var me=this;
		var params={projectID:me.projectID,issueTypeID:me.issueTypeID};
		if(me.workItemID){
			params.workItemID=me.workItemID;
		}
		Ext.Ajax.request({
			url: 'addRaciRole!load.action?raciRole=' + role,
			params:params,
			encoding: "utf-8",
			sync: true,
			success: function(response){
				var text = response.responseText;
				var data = Ext.decode(text);
				var personItems=[];
				var persons=data.persons;
				if(persons){
					for(var i=0;i<persons.length;i++){
						personItems.push(persons[i]);
					}
				}
				var groups=data.groups;
				if(groups){
					for(var i=0;i<groups.length;i++){
						personItems.push(groups[i]);
					}
				}

				var personPikerDialog=Ext.create('com.trackplus.util.PersonPickerDialog',{
					title:name,
					options:personItems,
					width:300,
					height:250,
					includeEmail:false,
					handler:function(value,displayValue){
						me.addPersonHandler.call(me,value,role);
					},
					scope:me
				});
				personPikerDialog.showDialog();
			}
		});
	},
	addPersonHandler:function(value,role){
		var me=this;
		if(CWHF.isNull(value)&&value.length===0){
			return ;
		}
		var urlStr='addRaciRole!save.action';
		var selectedPersons='';
		for(var i=0;i<value.length;i++){
			var id=value[i].data.id;
			if(i===0){
				selectedPersons=id;
			}else{
				selectedPersons+=','+id;
			}
		}
		var params={
			'raciRole':role,
			'consInfEdit.selectedPersonsStr':selectedPersons,
			issueTypeID:me.issueTypeID,
			projectID:me.projectID,
			workItemID:me.workItemID
		};
		Ext.Ajax.request({
			url: urlStr,
			disableCaching:true,
			success: function(){
				// CONSULTANT_LIST = -1003;
				//INFORMANT_LIST = -1004;
				me.fireItemChange([-1003,-1004]);
				me.refresh.call(me);
			},
			failure: function(){
			},
			method:'POST',
			params:params
		});
		//consInfEdit.selectedPersons
		//consInfEdit.selectedGroups
		/*
		 url: 'addRaciRole!save.action?raciRole='+role,
		 items:[
		 {xtype: 'displayfield',value: getText('item.tabs.watchers.lbl.persons')},
		 checkPersons,
		 {xtype: 'displayfield',value: getText('item.tabs.watchers.lbl.groups')},
		 checkGroups
		 ]
		 */

	},
	handlerDeleteItem:function(){
		var me=this;
		var selections=me.gridConfig.selectionModel.getSelection();
		var deletedConsultants="";
		var deletedInformants="";
		var deletedConsultantGroups="";
		var deletedInformantGroups="";
		var row;
		var i;
		for(i=0;i<selections.length;i++){
			row=selections[i].data;
			if(row.raciRole==="i"){
				if(row.isGroup==="true"){
					deletedInformantGroups=deletedInformantGroups+row.objectID+";";
				}else{
					deletedInformants=deletedInformants+row.objectID+";";
				}
			}else{
				if(row.isGroup==="true"){
					deletedConsultantGroups=deletedConsultantGroups+row.objectID+";";
				}else{
					deletedConsultants=deletedConsultants+row.objectID+";";
				}
			}
		}
		var urlStr='deleteWatchers.action?projectID='+me.projectID+"&issueTypeID="+me.issueTypeID;
		if(me.workItemID){
			urlStr=urlStr+'&workItemID='+me.workItemID;
		}
		Ext.Ajax.request({
			url:urlStr,
			disableCaching:true,
			success: function(){
				// CONSULTANT_LIST = -1003;
				//INFORMANT_LIST = -1004;
				me.fireItemChange([-1003,-1004]);
				me.refresh.call(me);
			},
			failure: function(){
			},
			method:'POST',
			params:{"deletedConsultants":deletedConsultants,
				"deletedInformants":deletedInformants,
				"deletedInformantGroups":deletedInformantGroups,
				"deletedConsultantGroups":deletedConsultantGroups
			}
		});
	},
	addRemoveMeAsConsultant:function(){
		var me=this;
		var operation="";
		var raciRole="c";
		if(me.meAsC){
			operation="r";
			me.meAsC=false;
		}else{
			operation="a";
			me.meAsC=true;
		}
		me.doMe(operation,raciRole);
	},
	doMe:function(operation,raciRole){
		var me=this;
		var urlStr='editConsultants.action?projectID='+me.projectID+"&issueTypeID="+me.issueTypeID;
		if(me.workItemID){
			urlStr=urlStr+'&workItemID='+me.workItemID;
		}
		Ext.Ajax.request({
			url:urlStr,
			disableCaching:true,
			success: function(){
				// CONSULTANT_LIST = -1003;
				//INFORMANT_LIST = -1004;
				me.fireItemChange([-1003,-1004]);
				me.refresh.call(me);
			},
			failure: function(){
			},
			method:'POST',
			params:{"raciRole":raciRole,"operation":operation}
		});
	},
	addRemoveMeAsInformant:function(){
		var me=this;
		var operation="";
		var raciRole="i";
		if(me.meAsI){
			operation="r";
			me.meAsI=false;
		}else{
			operation="a";
			me.meAsI=true;
		}
		me.doMe(operation,raciRole);
	},
	refreshCallback:function(r,options,success){
		var me=this;
		if(success===false){
			return;
		}
		this.meAsC=false;
		this.meAsI=false;
		var i=0;
		for(i=0;i<r.length;i++){
			if(r[i].data.objectID===this.myID){
				if(r[i].data.raciRole==='i'){
					this.meAsI=true;
				}else{
					this.meAsC=true;
				}
			}
		}
		var btnAddMeAsC=this.gridConfig.grid.down("#addMeAsC");
		var btnAddMeAsI=this.gridConfig.grid.down("#addMeAsI");

		if(this.meAsC){
			btnAddMeAsC.setText(getText('common.btn.deleteMeConsultant'));
			btnAddMeAsC.setIconCls('delete16');
		}else{
			btnAddMeAsC.setText(getText('common.btn.addMeConsultant'));
			btnAddMeAsC.setIconCls('add16');
		}
		if(this.meAsI){
			btnAddMeAsI.setText(getText('common.btn.deleteMeInformant'));
			btnAddMeAsI.setIconCls('delete16');
		}else{
			btnAddMeAsI.setText(getText('common.btn.addMeInformant'));
			btnAddMeAsI.setIconCls('add16');
		}
		var size=r.length;
		me.setTitle(me.replaceTitleNumber(me.title,size));
	}
});
