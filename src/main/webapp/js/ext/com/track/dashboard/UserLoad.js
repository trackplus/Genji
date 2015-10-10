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

Ext.define('js.ext.com.track.dashboard.UserLoad',{
	extend:'js.ext.com.track.dashboard.DashboardRenderer',
	initComponent : function(){
		var me=this;
		me.items=me.createChildren();
		me.callParent();
	},
	createChildren:function(){
		var me=this;
		if(me.jsonData.tooManyItems==true){
			return [me.createErrorCmp(getText('cockpit.err.tooManyItems'))];
		}
		var items=me.jsonData.items;
		var myGridData=[];
		myGridData.push({
			label:getText('userLoad.user'),
			category:true
		});
		myGridData=me.addAll(myGridData,com.trackplus.dashboard.updateElementWrapperList(items,{
			groupByFieldType:6//RESPONSIBLE = 6
		}));
		return com.trackplus.dashboard.createPercentGroup(null,myGridData,me);
	},
	doRefresh:function(jsonData){
		var me=this;
		me.jsonData=jsonData;
		me.removeAll();
		me.add(me.createChildren());
	}
});
