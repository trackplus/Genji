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

Ext.define('com.trackplus.layout.ReportConfigLayout',{
	extend:'com.trackplus.layout.BaseLayout',
	useToolbar:true,
	reportsConfig:null,
	selectedGroup:'reports',
	constructor : function(config) {
		var me = this;
		me.callParent(arguments);
		var fromIssueNavigator=me.initData.fromIssueNavigator;
		//var workItemID = me.initData.workItemID;
		me.borderLayoutController.setHelpContext("reportConfig");
//		me.reportsConfig=Ext.create('com.trackplus.admin.customize.category.CategoryConfig',{
		me.reportsConfig=Ext.create('com.trackplus.admin.customize.ReportConfig',{
			rootID:'report',
			fromIssueNavigator:fromIssueNavigator,
			//btnExecute: 'common.btn.executeReport',
			//workItemID:workItemID,
			replaceCenterPanel:function(centerPanel){
				me.borderLayoutController.setCenterContent.call(me.borderLayoutController,centerPanel);
			}
		});

		me.onReady(function(){
			var data=me.initData;
			me.borderLayoutController.setActiveToolbarList(me.reportsConfig.getToolbarActions());
		});
	},
	createCenterPanel:function(){
		var me=this;
		if(me.reportsConfig.centerPanel==null){
			me.reportsConfig.createCenterPanel();
		}
		return me.reportsConfig.centerPanel;
	},
	createWestPanel:function(){
		var me=this;
		if(me.reportsConfig.tree==null){
			me.reportsConfig.createCenterPanel();
		}
		me.reportsConfig.tree.addCls('westTreeNavigator');
		return me.reportsConfig.tree;
	}
});
