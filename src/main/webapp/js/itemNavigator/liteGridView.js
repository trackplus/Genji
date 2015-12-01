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

Ext.define('com.trackplus.itemNavigator.LiteGridViewPlugin',{
	extend: 'com.trackplus.itemNavigator.SimpleTreeGridViewPlugin',
	initMyListeners:function(){
		var me=this;
		//fix ExtJS BUG: click on row in grid with scroll will focus first row
		me.view.on('boxready', function (thisGrid) {
			thisGrid.view.focus = Ext.emptyFn;
		});
		me.view.addListener('celldblclick',me.onLiteGridItemDblClick,me);
	},
	onLiteGridItemDblClick:function(view, td,cellIndex,record, tr, rowIndex,e){
		var me=this;
		e.stopEvent();
		if(me.model.layout.bulkEdit===true&&cellIndex===0){
			return false;
		}
		var workItemID=record.data['workItemID'];
		me.openItem(workItemID);
		return false;
	},
	openItem:function(workItemID){
		var me=this;
		var actionID=-2;//PRINT
		var itemAction=Ext.create('com.trackplus.item.ItemActionDialog',{
			workItemID:workItemID,
			actionID:actionID,
			parentID:null,
			successHandler:me.itemActionSuccessHandler,
			scope:me,
			modal:false,
			navigatorHandler:me.navigatorHandler,
			navigatorScope:me
		});
		itemAction.execute.call(itemAction);
		itemAction.addListener('navigateToItem',me.navigateToItemHandler,me);
	},
	navigateToItemHandler:function(action,nextWorkItemID,position){
		var me=this;
		me.openItem(nextWorkItemID);
	},
	itemActionSuccessHandler:function(){
	},
	navigatorHandler:function(workItemID,dir,keepPosition){
		var me=this;
		return me.navigateToItem(workItemID,dir,keepPosition);
	},
	getTreeViewPlugins:function(){
		return null
	}
});
