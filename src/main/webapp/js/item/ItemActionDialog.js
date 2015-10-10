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


Ext.define('com.trackplus.item.ItemActionDialog',{
	extend:'com.trackplus.item.ItemAction',
	config:{
		title:'...',
		modal:true,
		w:1000,
		h:600,
		createDialogBeforLoaded:true,
		animateTarget:null,
		position:null,
		addLinkFromContextMenu:null
	},
	myPosition:null,
	centerPanelCls:'itemDialogBody',
	getMyPosition:function(){
		var me=this;
		return me.myPosition;
	},
	dialog:null,
	savedSuccessfully:false,
	reExecute:function(){
		this.execute(true);
	},
	execute:function(reExecute){
		var me=this;
		if(!(reExecute==true)&&me.createDialogBeforLoaded){
			me.initDialog();
		}
		me.callParent();
	},

	initDialog:function(){
		var me=this;
		me.initToolbar();
		me.createDialog();
		me.dialog.show(me.animateTarget);
	},
	loadSuccess:function(data){
		var me=this;
		if(!me.createDialogBeforLoaded){
			me.initDialog();
		}
		me.callParent(arguments);
	},
	loadFailure:function(response){
		var me=this;
		if(me.dialog!=null){
			try{
				me.dialog.close();
			}catch(ex){}
			me.dialog=null;
		}
		me.callParent(arguments);
	},

	createDialog:function(){
		var me=this;
		me.parentPanel= Ext.create('Ext.container.Container', {
			margin: '0 0 0 0',
			padding:' 0 0 0 0',
			border: false,
			cls:'itemDialogBody',
			layout:'fit',
			region:'center'
		});
		me.toolbar.height=27;
		var size=borderLayout.ensureSize(me.w,me.h);
		me.w=size.width;
		me.h=size.height;
		var northPanel=Ext.create('Ext.panel.Panel',{
			border:false,
			bodyBorder:false,
			margin:'0 0 0 0',
			region:'north',
			layout: {
				type: 'hbox',
				padding:'0',
				align:'top'
			},
			items:[me.toolbar,me.extraToolbar]
		});
		me.dialog= Ext.create('Ext.window.Window',{
			cls:'workItemDialog itemscreen',
			constrainHeader: true,
			maximizable :true,
			layout      : 'border',
			width       : me.w,
			height      : me.h,
			//iconCls     : this.iconCls,
			closeAction :'destroy',
			stateful:true,
			stateId :'winItem',
			bodyBorder  :false,
			bodyStyle: {
				border:'none'
			},
			plain       : true,
			title		:me.title,
			modal       :me.modal,
			items       :[northPanel,me.parentPanel],
			afterSetPosition:me.afterSetPositionItemDialog,
			listeners:{
				'staterestore':{
					fn:function(dialog,state){
						borderLayout.ensureDialogSizeFromState(dialog,state,me.position);
						me.myPosition=[dialog.x,dialog.y];
					}
				},
				'activate':{
					fn:function(){
						me.fireEvent.call(me,'activate',me);
					}
				},
				'deactivate':{
					fn:function(){
						me.fireEvent.call(me,'deactivate',me);
					}
				},
				'close':{
					fn:function(){
						me.fireEvent.call(me,'close',me,{savedSuccessfully:me.savedSuccessfully});
						me.savedSuccessfully=false;
					}
				},
				'maximize':{
					fn:me.onMaximizeItemDialog,
					scope:me
				},
				'beforestatesave':{
					fn:function(){
						me.myPosition=me.dialog.getPosition();
					}
				}
			}
		});
	},
	afterSetPositionItemDialog:function(){
		if(this.maximized==true){
			var maxH=borderLayout.getHeight();
			var h=this.getHeight();
			var y=this.getY();
			var preferredH=maxH-(67+28);
			if(preferredH!=h||y!=67){
				this.setY(67);
				this.setHeight(preferredH);
				this.doLayout();
			}
		}
	},
	onMaximizeItemDialog:function(win, eOpts){
		var me=this;
		var h=me.dialog.getHeight();
		var maxH=borderLayout.getHeight();
		me.dialog.setHeight(maxH-(67+28));
		me.dialog.setY(67);
		me.dialog.doLayout();
	},
	updateTitle:function(data){
		var me=this;
		var useWizard=data.useWizard;
		if(useWizard){
			me.dialog.setTitle(data.title1);
		}else{
			me.dialog.setTitle(data.title);
		}
	},
	navigate:function(direction,data){
		var me=this;
		me.callParent(arguments);
		if(me.dialog!=null){
			if(direction=="prev"){
				me.dialog.setTitle(data.title1);
			}else{
				me.dialog.setTitle(data.title);
			}
		}
	},
	saveSuccess:function(form, action){
		var me=this;
		var data=null;
		var workItemID=null;
		if(action!=null){
			data=action.result.data;
			workItemID=data.workItemID;
		}
		if(me.successExtra!=null&&me.successExtra['actionID']==-2){
			me.actionID=-2;
			if(workItemID==null){
				workItemID=me.successExtra['workItemID']
			}
			me.workItemID=workItemID;
			me.successExtra['actionID']=null;
			me.successExtra['workItemID']=null;
			me.reExecute();
		}else{
			try{
				me.savedSuccessfully=true;
				me.dialog.close();
				me.dialog.destroy();
			}catch(ex){}
			me.dialog=null;
		}
		me.callParent(arguments);
	},
	saveFailure:function(form, action){
		var me=this;
		if(me.dialog!=null){
			try{
				me.dialog.destroy();
			}catch(ex){}
			me.dialog=null;
		}
		me.callParent(arguments);
	},
	executeToolbarAction:function(toolbarItem){
		var me=this;
		var id=toolbarItem.id;
		switch(id){
			case com.trackplus.item.ToolbarItem.ITEM_ACTION:
			case com.trackplus.item.ToolbarItem.SIBLING:
			case com.trackplus.item.ToolbarItem.BACK:
				break;
			case com.trackplus.item.ToolbarItem.NAVIGATION_NEXT:
				break;
			case com.trackplus.item.ToolbarItem.NAVIGATION_PREV:
				break;
			case com.trackplus.item.ToolbarItem.CANCEL:
				if(me.dialog!=null) {

					if (me.successExtra != null && me.successExtra['actionID'] == -2) {
						me.actionID = -2;
						me.workItemID  = me.successExtra['workItemID']
						me.successExtra['actionID'] = null;
						me.successExtra['workItemID'] = null;
						me.reExecute();
					} else {
						try {
							me.dialog.close();
							me.dialog.destroy();
						} catch (ex) {
						}
						me.dialog = null;
					}
				}
				break;
		}
		me.callParent(arguments);
	},
	setLoading:function(b){
		if(this.dialog!=null){
			this.dialog.setLoading(b);
		}else{
			borderLayout.setLoading(b);
		}
	}
});
