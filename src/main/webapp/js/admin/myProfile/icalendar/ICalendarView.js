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

Ext.define('com.trackplus.admin.myProfile.icalendar.ICalendarView',{
	extend:'Ext.panel.Panel',
	controller:'icalendar',

	layout:'anchor',
	margin:'0 0 0 0',
	border:false,
	bodyBorder:false,

	initComponent : function() {
		var store = Ext.create('Ext.data.TreeStore', {
			proxy: {
				type: 'ajax',
				url: 'projectPicker.action',
				extraParams: {
					useChecked: true
				}
			}
		});
		var tree = Ext.create('Ext.tree.Panel', {
			store: store,
			reference:'tree',
			rootVisible: false,
			useArrows: true,
			margin:'10 10 10 10',
			frame: true,
			title: getText('admin.project.lbl.project'),
			width: 300,
			height: 200
		});
		var infoBox={
			xtype: 'component',
			cls: 'infoBox_bottomBorder',
			border:true,
			html: getText('admin.myprefs.iCalendar.defaultUrlMessage')
		};
		var boxURL=Ext.create('Ext.Component',{
			border:true,
			reference:'boxURL',
			html:'...',
			margin:'10 0 0 10',
			anchor:'100%',
			hidden:true
		});

		this.items=[infoBox,tree,boxURL];
		this.dockedItems = [{
			xtype: "toolbar",
			dock: 'top',
			items: [
				Ext.create('Ext.button.Button',{
					overflowText:getText('common.btn.generateUrl'),
					tooltip:getText('common.btn.generateUrl'),
					text: getText('common.btn.generateUrl'),
					iconCls: 'save',
					disabled:false,
					handler:'generateURL'
				})
			]
		}];
		this.callParent();
	}
});
