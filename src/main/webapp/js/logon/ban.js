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

Ext.define('com.trackplus.layout.BannedLayout',{
	extend:'com.trackplus.layout.BaseLayout',
	useToolbar:false,
	constructor : function(config) {
		var me = this;
		me.callParent(arguments);
		me.borderLayoutController.setHelpContext("logon");
	},
	createCenterPanel:function(){
		return Ext.create('Ext.panel.Panel',{
			region: 'center',
			cls:'messagePageWrapper',
			border: false,
			baseCls:'x-plain',
			unstyled: true,
			html:'<div class="boxMessage boxMessage-error">'+
				'<div class="messageIcon"><img src="design/silver/32x32/cancel.png"/></div>'+
				'<div class="messageBody">'+getText("logon.banned.body")+'</div>'+
				'</div>'
		});
	}
});
