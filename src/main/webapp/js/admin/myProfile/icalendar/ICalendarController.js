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

Ext.define('com.trackplus.admin.myProfile.icalendar.ICalendarController',{
	extend: "Ext.app.ViewController",
	alias: "controller.icalendar",
	generateURL:function(){
		var tree=this.lookupReference('tree');
		var records = tree.getView().getChecked();
		if(CWHF.isNull(records)||records.length===0){
			Ext.MessageBox.show({
				title:getText('admin.myprefs.iCalendar.title'),
				msg: getText('common.err.required',getText('admin.project.lbl.projectForOp')),
				buttons: Ext.MessageBox.OK,
				icon: Ext.MessageBox.ERROR
			});
			return false;
		}
		var selectedProjects="";
		for(var i=0;i<records.length;i++){
			selectedProjects+=records[i].data.id;
			if(i<records.length-1){
				selectedProjects+='-';
			}
		}

		var view=this.getView();
		var boxURL=this.lookupReference('boxURL');
		Ext.Ajax.request({
			url: "iCalendar!generateURL.action",
			params:{
				selectedProjects:selectedProjects,
				allProjects:false
			},
			success: function(response){
				var jsonData=Ext.decode(response.responseText);
				boxURL.update(jsonData.data);
				boxURL.setVisible(true);
				view.setLoading(false);
			},
			failure:function(response){
				alert("failure");
				view.setLoading(false);
			}
		});
	}
});
