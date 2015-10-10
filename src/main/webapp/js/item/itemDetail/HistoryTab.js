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


//History tab class
Ext.define('com.aurel.trackplus.itemDetail.HistoryTab',{
	extend:'com.aurel.trackplus.itemDetail.TabGrid',
	cls:'ulist',
	historyFull:true,
	showCommentsHistory:true,
	showFlatHistory:false,
	flatPanel:null,
	initComponent : function(){
		var me = this;
		me.title=getText('item.printItem.lbl.tab.commonHistory');
		// me.iconCls='history16';
		me.gridConfig=me.createHistoryConfig();

		var toolbarItems=new Array();
		//var btnFlatHistory=me.createViewButton('flatHistory','flatHistory',getText('common.history.lbl.flatHistory'),
		//	getText('common.history.lbl.flatHistory.tlt'));
		var btnGridHistory=me.createViewButton('gridHistory','gridHistory',getText('common.history.lbl.gridHistory'),
			getText('common.history.lbl.gridHistory.tlt'));
		if(me.showFlatHistory==true){
			btnGridHistory.pressed=false;
		}else{
			btnGridHistory.pressed=true;
		}
		//toolbarItems.push(btnFlatHistory);
		toolbarItems.push(btnGridHistory);

		if(me.historyFull){
			btnLabel=com.trackplus.TrackplusConfig.getText("item.tabs.history.lbl.collapse");
			iconCls='collapse16';
		}else{
			btnLabel=com.trackplus.TrackplusConfig.getText("item.tabs.history.lbl.expand");
			iconCls='expand16';
		}
		me.btnExpandCollapse=Ext.create('Ext.button.Button',{
			itemId:"expandCollapseHistoryBtn",
			text:btnLabel,
			iconCls:iconCls,
			handler:function(){
				me.expandCollapseHistory.call(me);
			},
			hidden:me.showFlatHistory
		})
		toolbarItems.push(me.btnExpandCollapse);
		if(me.showCommentsHistory){
			btnLabel=com.trackplus.TrackplusConfig.getText("item.tabs.history.lbl.hideComments");
			iconCls='collapse16';
		}else{
			btnLabel=com.trackplus.TrackplusConfig.getText("item.tabs.history.lbl.showComments");
			iconCls='expand16';
		}
		me.btnComments=Ext.create('Ext.button.Button',{
			itemId:"showHideCommentsBtn",
			text:btnLabel,
			iconCls:iconCls,
			handler:function(){
				me.showHideCommentsHistory.call(me);
			},
			hidden:me.showFlatHistory
		});
		toolbarItems.push(me.btnComments);
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
		if(me.showFlatHistory==true){
			me.initFlatPanel();
			items.push(me.flatPanel);
		}else{
			if(me.gridConfig!=null&&me.jsonData!=null&&me.jsonData.gridLayoutData!=null){
				var someGrid=me.createGrid.call(me,me.gridConfig,me.jsonData.gridLayoutData);
				me.gridConfig.grid=someGrid;
				items.push(someGrid);
			}
		}
		return items;
	},
	createHistoryConfig:function(){
		if(this.jsonData!=null){
			this.historyFull=this.jsonData.historyFull;
			this.showCommentsHistory=this.jsonData.showCommentsHistory;
			this.showFlatHistory=this.jsonData.showFlatHistory;
		}
		var me=this;
		var gridConfig=new com.trackplus.itemDetail.GridConfig();
		gridConfig.tabID="historyTab";
		gridConfig.id=1;
		gridConfig.urlStore='itemHistory.action?workItemID='+me.workItemID+"&projectID="+me.projectID+"&issueTypeID"+me.issueTypeID;
		gridConfig.fields=[
			{name: 'id',type:'int'},
			{name: 'date',type: 'date', dateFormat: com.trackplus.TrackplusConfig.ISODateTimeFormat},
			{name: 'author',type:'string'},
			{name: 'authorID',type:'string'},
			{name: 'typeOfChange',type:'string'},
			{name: 'newValues', type: 'string'},
			{name: 'newValuesFull', type: 'string'},
			{name: 'oldValues', type: 'string'},
			{name: 'oldValuesFull', type: 'string'},
			{name: 'comment', type: 'string'},
			{name: 'commentFull', type: 'string'},
			{name: 'diff', type: 'string'},
			{name: 'diffFull', type: 'string'}
		];
		gridConfig.cellClickHandler=me.cellClickHistory;
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

		if(me.showFlatHistory==false){
			me.showFlatHistory=true;
			if(me.gridConfig.grid!=null){
				me.remove(me.gridConfig.grid,true);
				me.gridConfig.grid=null;
			}
			me.initFlatPanel();
			me.add(me.flatPanel);
			me.refreshFlatHistory();
		}else{
			me.showFlatHistory=false;
			if(me.flatPanel!=null){
				me.remove(me.flatPanel);
				me.flatPanel=null;
			}
			me.refresh();
			me.add(me.gridConfig.grid);
		}
		me.btnExpandCollapse.setVisible(!me.showFlatHistory);
		me.btnComments.setVisible(!me.showFlatHistory);

		me.showHideFlatHistory();

	},
	refreshFlatHistory:function(){
		var me=this;
		if(me.flatPanel!=null){
			me.flatPanel.setLoading(true);
		}
		Ext.suspendLayouts();
		//var urlStr='itemHistory.action?workItemID='+me.workItemID+"&projectID="+me.projectID+"&issueTypeID"+me.issueTypeID;
		var urlStr='itemDetailHistory.action?workItemID='+me.workItemID;

		Ext.Ajax.request({
			url: urlStr,
			disableCaching:true,
			success: function(response){
				//var responseJson = Ext.decode(response.responseText);
				//var jsonData=responseJson;
				me.flatData=response.responseText;
				if(me.flatPanel!=null){
					me.flatPanel.setLoading(false);
				}
				me.initFlatPanel();
				Ext.resumeLayouts(true);
			},

			failure: function(){
				Ext.resumeLayouts(true);
			},
			method:'POST',
			params:{includeLayout:false,tabID:me.gridConfig.id}
		});
	},
	cellClickHistory:function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts){
		var me=this;
		var row=grid.getStore().getAt(rowIndex);
		var dataIndex = grid.getHeaderCt().getHeaderAtIndex(cellIndex).dataIndex;
		if (dataIndex=="oldValues"&&row.data.oldValuesFull!=null&&row.data.oldValuesFull.length>0){
			me.showHtmlDetail(getText("common.history.lbl.oldValue"),row.data.oldValuesFull);
		}
		if (dataIndex=="newValues"&&
			row.data['newValuesFull']!=null&&row.data['newValuesFull'].length>0){
			me.showHtmlDetail(getText("common.history.lbl.newValue"),row.data.newValuesFull);
		}
		if (dataIndex=="comment"&&
			row.data.commentFull!=null&&row.data.commentFull.length>0){
			me.showHtmlDetail(getText("common.history.lbl.comment"),row.data.commentFull);
		}
		if (dataIndex=="diff"&&
			row.data.diffFull!=null&&row.data.diffFull.length>0){
			me.showHtmlDetail(getText("common.history.lbl.comment"),row.data.diffFull);
		}
	},
	win:null,
	showHtmlDetail:function(title,htmlContent){
		var me=this;
		if(me.win!=null){
			me.win.destroy();
		}
		htmlDinamicContent=htmlContent;
		var htmlFrameWrapper='<iframe allowtransparency="true" frameborder="0" width="100%" style="height:100%;background-color: transparent;"  '+
			'src="richTextPreview.action"></iframe>';
		var panel=Ext.create('Ext.panel.Panel',{
			collapsible:false,
			autoWidth:true,
			autoHeight:true,
			bodyBorder:false,
			bodyPadding:'5 5 5 5',
			autoScroll:true,
			border:false,
			id:"panelContent",
			html:htmlFrameWrapper
		});
		me.win = new Ext.Window({
			layout      : 'fit',
			iconCls:'history16',
			width       : 550,
			height      : 350,
			closeAction :'destroy',
			plain       : true,
			title       :title,
			modal       :true,
			items       :panel,
			autoScroll  :true,
			buttons:[{
				text : getText('common.btn.close'),
				itemId:'btnClose',
				handler  : function(){
					me.win.hide();
					me.win.destroy();
				}
			}],
			listeners:{
				show:function( win, eOpts ){
					win.down('#btnClose').focus(false,150);
				}
			}
		});
		me.win.show();
	},
	expandCollapseHistory:function(){
		var me=this;
		me.historyFull=!me.historyFull;
		Ext.Ajax.request({
			url: "userPreferences.action?property=historyFull&value="+me.historyFull,
			disableCaching:true,
			success: function(data){
				me.refresh();
			},
			failure: function(type, error){
			}
		});
	},
	showHideCommentsHistory:function(){
		var me=this;
		me.showCommentsHistory=!me.showCommentsHistory;
		Ext.Ajax.request({
			url: "userPreferences.action?property=showCommentsHistory&value="+me.showCommentsHistory,
			disableCaching:true,
			success: function(data){
				me.refresh();
			},
			failure: function(type, error){
			}
		});
	},
	showHideFlatHistory:function(){
		var me=this;
		Ext.Ajax.request({
			url: "userPreferences.action?property=showFlatHistory&value="+me.showFlatHistory,
			disableCaching:true,
			success: function(data){
			},
			failure: function(type, error){
			}
		});
	},
	refresh:function(){
		var me=this;
		if(me.showFlatHistory==true){
			me.refreshFlatHistory();
		}else{
			me.callParent();
		}
	},
	refreshCallback:function(r,options,success){
		var me=this;
		if(success==false){
			return;
		}
		if(me.historyFull){
			me.btnExpandCollapse.setText(com.trackplus.TrackplusConfig.getText("item.tabs.history.lbl.collapse"));
			me.btnExpandCollapse.setIconCls('collapse16');
		}else{
			me.btnExpandCollapse.setText(com.trackplus.TrackplusConfig.getText("item.tabs.history.lbl.expand"));
			me.btnExpandCollapse.setIconCls('expand16');
		}
		var btnShowHideComments=me.gridConfig.grid.down("#showHideCommentsBtn");
		if(me.showCommentsHistory){
			me.btnComments.setText(com.trackplus.TrackplusConfig.getText("item.tabs.history.lbl.hideComments"));
			me.btnComments.setIconCls('collapse16');
		}else{
			me.btnComments.setText(com.trackplus.TrackplusConfig.getText("item.tabs.history.lbl.showComments"));
			me.btnComments.setIconCls('expand16');
		}
		/*var btnShowHideFlatHistory=me.gridConfig.grid.down("#showHideFlatHistoryBtn");
		 if(me.showFlatHistory){
		 btnShowHideFlatHistory.setText(com.trackplus.TrackplusConfig.getText("item.tabs.history.lbl.hideFlatHistory"));
		 btnShowHideFlatHistory.setIconCls('collapse16');
		 }else{
		 btnShowHideFlatHistory.setText(com.trackplus.TrackplusConfig.getText("item.tabs.history.lbl.showFlatHistory"));
		 btnShowHideFlatHistory.setIconCls('expand16');
		 }*/
	},
	initFlatPanel:function(){
		var me=this;
		var html="";
		if(me.flatData!=null/*&&me.flatData.length>0*/){
			html=me.flatData;
		}
		if(me.flatPanel==null){
			me.flatPanel=Ext.create('Ext.Component',{
				style:{
					borderTop:'1px solid #D0D0D0'
				},
				cls:'flatHistoryPanel',
				html:html
			});
		}else{
			me.flatPanel.update(html);
		}
	}
});
