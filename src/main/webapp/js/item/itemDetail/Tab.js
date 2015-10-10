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


Ext.define('com.aurel.trackplus.itemDetail.Tab',{
	extend: 'Ext.panel.Panel',
	border:false,
	bodyBorder:false,
	cls:'gridNoBorder',
	config: {
		workItemID:null,
		projectID:null,
		issueTypeID:null,
		tabNo:null,
		jsonData:{},
		labelHAlign:'right',
		decimalSeparator:'.',
		decimalFormat:'0,000.00',
		jsDateFormat:'d/m/Y',
		keepSelection: false,
		lastModified:null
	},
	margin:'0 0 0 0',
	bodyPadding:0,
	TAB_HISTORY:1,
	TAB_COMMENTS:2,
	TAB_ATTACHMENTS:3,
	TAB_WORKLOG:4,
	TAB_WATCHERS:5,
	TAB_VERSION_CONTROL:6,
	TAB_LINKS:7,
	TAB_MIGRATIONS:8,
	initComponent : function(){
		var me = this;
		this.addEvents('itemChange','clickOnLink');
		me.callParent();
	},
	fireItemChange:function(fields,lastEdit,values){
		var me=this;
		if(lastEdit!=null){
			me.lastModified=lastEdit;
		}
		me.fireEvent.call(me,'itemChange',fields,lastEdit,values);
	},
	refresh:function(){
	}
});
