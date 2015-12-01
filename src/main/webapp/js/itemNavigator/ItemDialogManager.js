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

Ext.define('com.trackplus.itemNavigator.ItemDialogManager',{
	extend:'Ext.Base',
	config: {
		navigable:null,
		usePosition:false
	},
	allDialogs:new Array(),
	navigateContexts:new Array(),
	lastActiveDialog:null,

	constructor : function(cfg) {
		var me = this;
		var config = cfg || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	},
	openDialog:function(itemAction,navigateCtx){
		var me=this;
		if(me.allDialogs.length>0){
			var prevDialog;
			if(me.lastActiveDialog){
				prevDialog=me.lastActiveDialog;
			}else{
				prevDialog=me.allDialogs[me.allDialogs.length-1];
			}
			var prevPosition=prevDialog.dialog.getPosition();
			itemAction.position=[prevPosition[0]+25,prevPosition[1]+25];
		}
		itemAction.execute.call(itemAction);
		itemAction.addListener('activate',me.activateItemDialog,me);
		itemAction.addListener('deactivate',me.deactivateItemDialog,me);
		itemAction.addListener('close',me.closeItemDialog,me);
		itemAction.addListener('itemChange',me.itemChangeHandler,me);
		itemAction.addListener('clickOnChild',me.clickOnChildHandler,me);
		itemAction.addListener('clickOnLink',me.clickOnLinkHandler,me);
		itemAction.addListener('clickOnParent',me.clickOnParentdHandler,me);
		itemAction.addListener('navigateToItem',me.navigateToItemHandler,me);
		me.allDialogs.push(itemAction);
		me.navigateContexts.push(navigateCtx);
		me.lastActiveDialog=itemAction;
	},

	navigateToItemHandler:function(itemAction,direction){
		var me=this;
		var workItemID=itemAction.workItemID;
		var dialogIndex= Ext.Array.indexOf(me.allDialogs, itemAction);
		var navCtx=me.navigateContexts[dialogIndex];
		var nextItem=me.navigable.navigate.call(me.navigable,workItemID,navCtx.workItemIndex,direction);

		if(nextItem){
			var nextWorkItemID=nextItem.workItemID;
			var nextWorkItemIndex=nextItem.workItemIndex;
			if(me.usePosition){
				me.navigable.deselectItemByIndex.call(me.navigable,navCtx.workItemIndex);
			}else{
				me.navigable.deselectItem.call(me.navigable,itemAction.workItemID);
			}

			itemAction.workItemID=nextWorkItemID;
			var nextItem1=me.navigable.navigate.call(me.navigable,nextWorkItemID,nextWorkItemIndex,'next');
			var prevItem1=me.navigable.navigate.call(me.navigable,nextWorkItemID,nextWorkItemIndex,'prev');
			navCtx.workItemID=nextWorkItemID;
			navCtx.workItemIndex=nextWorkItemIndex;
			navCtx.nextItem=nextItem1;
			navCtx.prevItem=prevItem1;
			itemAction.disabledNext=(CWHF.isNull(nextItem1));
			itemAction.disabledPrev=(CWHF.isNull(prevItem1));
			itemAction.reExecute();
			if(me.usePosition){
				me.navigable.selectItemByIndex.call(me.navigable,nextWorkItemIndex);
			}else{
				me.navigable.selectItem.call(me.navigable,itemAction.workItemID);
			}
		}else{
			var msg="";
			if(direction==='next'){
				msg=getText('itemov.err.noNextAvailable');
				itemAction.setDisabledNextButton(true);
			}else{
				msg=getText('itemov.err.noPrevAvailable');
				itemAction.setDisabledPrevButton(true);
			}
			CWHF.showMsgError(msg);
		}
	},
	itemChangeHandler:function(fields){
		var me=this;
		if(me.navigable){
			me.navigable.itemChangeHandler.call(me.navigable,fields);
		}
	},
	clickOnChildHandler:function(workItemID){
		var me=this;
		if(me.navigable){
			me.navigable.openItem.call(me.navigable,workItemID);
		}
	},
	clickOnLinkHandler:function(workItemID){
		var me=this;
		if(me.navigable){
			me.navigable.openItem.call(me.navigable,workItemID);
		}
	},
	clickOnParentdHandler:function(parentID){
		var me=this;
		if(me.navigable){
			me.navigable.openItem.call(me.navigable,parentID);
		}
	},
	activateItemDialog:function(itemAction){
		var me=this;
		me.lastActiveDialog=itemAction;
		var workItemID=itemAction.workItemID;
		if(me.navigable){
			me.navigable.selectItem.call(me.navigable,workItemID);
		}
	},
	deactivateItemDialog:function(itemAction){
		var me=this;
		var workItemID=itemAction.workItemID;
		me.lastActiveDialog=null;
		/*if(me.navigable){
			me.navigable.deselectItem.call(me.navigable,workItemID);
		}*/
	},

	releaseItemLock: function(workItemID) {
		Ext.Ajax.request({
			url: "releaseItemLock.action",
			params:{
				workItemID:workItemID
			},
			success: function(response){
			},
			failure:function(result){
			}
		});
	},

	closeItemDialog:function(itemAction,options) {
		var me=this;
		var workItemID = itemAction.workItemID;
		var actionID = itemAction.actionID;
		if (workItemID && actionID) {
			var savedSuccessfully=false;
			if(options){
				savedSuccessfully=options.savedSuccessfully;
			}
			//alert("savedSuccessfully="+savedSuccessfully);
			//2:edit, 3:move, 5:change status
			if (savedSuccessfully===false && (actionID===2 || actionID===3 || actionID===5)) {
				//cancel or X button after opening the item with edit/move/status change form
				me.releaseItemLock(workItemID);
			}
		}
		if(me.lastActiveDialog===itemAction){
			me.lastActiveDialog=null;
		}
		var idx=Ext.Array.indexOf(me.allDialogs,itemAction);
		me.navigateContexts=Ext.Array.erase( me.navigateContexts, idx, 1);
		me.allDialogs=Ext.Array.remove(me.allDialogs,itemAction);
		/*if(me.navigable){
			me.navigable.deselectItem.call(me.navigable,itemAction.workItemID);
		}*/
	},
	findNextItem:function(workItemID,direction,keepPosition){
		var me=this;
		var nextItemID=null;
		if(me.navigable){
			nextItemID=me.navigable.navigate.call(me.navigable,workItemID,CWHF.isNull(direction)?'next':direction,keepPosition);
		}
		return nextItemID;
	}
});
