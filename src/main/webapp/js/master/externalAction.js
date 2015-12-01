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

Ext.define('com.trackplus.layout.ExternalActionLayout',{
	extend:'com.trackplus.layout.BaseLayout',
	useToolbar:false,
	//selectedGroup:'wiki',
	constructor : function(config) {
		var me = this;
		me.callParent(arguments);
		me.onReady(function() {
		});
	},

	createCenterPanel: function(){
		var me=this;
		var module = me.initData.module;
		return me.getPanel(module.url);
	},

	createJenkinsSessionCookie: function(sessionID) {

	},

	getPanel: function(url) {
		return  Ext.create('Ext.panel.Panel',{
			region: 'center',
			border: false,
			baseCls:'x-plain',
			layout:'fit',
			items:[{
				xtype: 'box',
				autoEl: {
					tag: 'iframe',
					style:'width: 100%;height: 100%;border: medium none;',
					src: url
				}
			}]
		});
	}
});
