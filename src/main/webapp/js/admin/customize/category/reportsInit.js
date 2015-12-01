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

Ext.define('com.trackplus.layout.ReportConfigLayout',{
	extend:'com.trackplus.layout.BaseLayout',
	useToolbar:true,
	reportsConfig:null,
	selectedGroup:'reports',
	constructor : function(config) {
		var me = this;
		me.callParent(arguments);
		var fromIssueNavigator=me.initData.fromIssueNavigator;
		me.borderLayoutController.setHelpContext("reportConfig");

		me.reportsConfig=Ext.create('com.trackplus.admin.customize.ReportConfig',{
			rootID: 'report',
			fromIssueNavigator: fromIssueNavigator,
			repCfgLayout: me
		});

		me.onReady(function(){
			var data=me.initData;
			me.borderLayoutController.setActiveToolbarList(me.reportsConfig.getToolbarActions());
		});
	},
	createCenterPanel:function(){
		var me=this;
		if(CWHF.isNull(me.reportsConfig.centerPanel)){
			me.reportsConfig.createCenterPanel();
		}
		return me.reportsConfig.centerPanel;
	},
	createWestPanel:function(){
		var me=this;
		if(CWHF.isNull(me.reportsConfig.tree)){
			me.reportsConfig.createCenterPanel();
		}
		me.reportsConfig.tree.addCls('westTreeNavigator');
		Ext.apply(me.reportsConfig.tree,{'split':true,collapseMode : 'mini'});
		return me.reportsConfig.tree;
	}
});
