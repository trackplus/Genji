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


Ext.define('com.aurel.trackplus.itemDetail.VersionControlTab',{
	extend:'com.aurel.trackplus.itemDetail.TabGrid',
	readOnly:false,
	showFlat:false,
	flatPanel:null,
	flatData:null,
	initComponent : function(){
		var me = this;
		me.title=getText('item.tabs.versionControl.lbl');
		// me.iconCls='versionControl16';
		me.gridConfig=me.createVersionConfig();
		var toolbarItems=new Array();
		//var btnFlatHistory=me.createViewButton('flatHistory','flatHistory',getText('common.history.lbl.flatHistory'),
		//	getText('common.history.lbl.flatHistory.tlt'));
		var btnGridView=me.createViewButton('gridHistory','gridHistory',getText('common.history.lbl.gridHistory'),
			getText('common.history.lbl.gridHistory.tlt'));
		if(me.showFlat===true){
			btnGridView.pressed=false;
		}else{
			btnGridView.pressed=true;
		}
		toolbarItems.push(btnGridView);
		me.dockedItems=[{
			xtype: 'toolbar',
			dock: 'top',//'bottom'
			items:toolbarItems,
			border:false
		}];

		me.callParent();
	},
	createChildren:function(){
		var me=this;
		var items=[];
		if(me.showFlat===true){
			me.initFlatPanel();
			items.push(me.flatPanel);
		}else{
			if(me.gridConfig&&me.jsonData&&me.jsonData.gridLayoutData){
				var someGrid=me.createGrid.call(me,me.gridConfig,me.jsonData.gridLayoutData);
				me.gridConfig.grid=someGrid;
				items.push(someGrid);
			}
		}
		return items;
	},
	createVersionConfig:function(){
		var me=this;
		var versionControlRevNoType='string';
		if(me.jsonData){
			me.readOnly=me.jsonData.readOnly;
			versionControlRevNoType=me.jsonData.versionControlRevNoType;
			me.showFlat=this.jsonData.showFlat;
		}
		var gridConfig=new com.trackplus.itemDetail.GridConfig();
		gridConfig.tabID="versionControlTab";
		gridConfig.id=6;
		gridConfig.urlStore='itemVersionControl.action?workItemID='+me.workItemID+"&projectID="+me.projectID+"&issueTypeID="+me.issueTypeID;
		gridConfig.fields=[
			{name: 'revision',type:versionControlRevNoType},
			{name:'revisionURL',type:'string'},
			{name: 'repository',type:'string'},
			{name: 'date',type: 'date', dateFormat: com.trackplus.TrackplusConfig.ISODateTimeFormat},
			{name: 'user',type:'string'},
			{name: 'message',type:'string'}
		];
		return gridConfig;
	},
	createViewButton:function(id,iconCls,txt,tooltip){
		var me=this;
		return Ext.create('Ext.button.Button',{
			//itemId:id,
			iconCls:iconCls,
			enableToggle:true,
			allowDepress:true,
			toggleGroup:'itemDetailHistoryViewGroup',
			text:txt,
			overflowText :txt,
			tooltip :tooltip,
			handler:function(){
				me.changeViewMode();
			},
			scope:me
		});
	},
	changeViewMode:function(id){
		var me=this;

		if(me.showFlat===false){
			me.showFlat=true;
			if(me.gridConfig.grid){
				me.remove(me.gridConfig.grid,true);
				me.gridConfig.grid=null;
			}
			me.initFlatPanel();
			me.add(me.flatPanel);
			me.refreshFlat();
		}else{
			me.showFlat=false;
			if(me.flatPanel){
				me.remove(me.flatPanel);
				me.flatPanel=null;
			}
			me.refresh();
			me.add(me.gridConfig.grid);
		}
		me.showHideFlat();
	},
	showHideFlat:function(){
		var me=this;
		Ext.Ajax.request({
			url: "userPreferences.action?property=showFlatVersionControl&value="+me.showFlat,
			disableCaching:true,
			success: function(data){
			},
			failure: function(type, error){
			}
		});
	},
	refresh:function(){
		var me=this;
		if(me.showFlat===true){
			me.refreshFlat();
		}else{
			me.callParent();
		}
	},
	refreshFlat:function(){
		var me=this;
		if(me.flatPanel){
			me.flatPanel.setLoading(true);
		}
		//var urlStr='itemHistory.action?workItemID='+me.workItemID+"&projectID="+me.projectID+"&issueTypeID"+me.issueTypeID;
		var urlStr='itemDetailVersionControl.action?workItemID='+me.workItemID;
		Ext.Ajax.request({
			url: urlStr,
			disableCaching:true,
			success: function(response){
				//var responseJson = Ext.decode(response.responseText);
				//var jsonData=responseJson;
				me.flatData=response.responseText;
				if(me.flatPanel){
					me.flatPanel.setLoading(false);
				}
				me.initFlatPanel();
			},

			failure: function(){
			},
			method:'POST',
			params:{includeLayout:false,tabID:me.gridConfig.id}
		});
	},
	initFlatPanel:function(){
		var me=this;
		var html="";
		if(me.flatData/*&&me.flatData.length>0*/){
			html=me.flatData;
		}
		if(CWHF.isNull(me.flatPanel)){
			me.flatPanel=Ext.create('Ext.panel.Panel',{
				style:{
					borderTop:'1px solid #D0D0D0'
				},
				bodyBorder:false,
				cls:'flatHistoryPanel',
				html:html
			});
		}else{
			me.flatPanel.update(html);
		}
	}
});
