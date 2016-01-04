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

Ext.define('com.trackplus.admin.server.logging.LoggingConfigController',{
	extend: "Ext.app.ViewController",
	alias: "controller.loggingConfig",

	filter:'',//'aurel',

	onAfterRender:function(cmp,opt){
		this.reload();
	},


	reload:function(){
		var me=this;
		var grid=this.lookupReference('grid');
		Ext.Ajax.request({
			url: "editAdminLoggingConfig!load.action",
			params: {
				filter: me.filter
			},
			disableCaching:true,
			scope: me,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				if (responseJson.success === true) {
					grid.store.loadData(responseJson.data.loggers);
				}else {
					if (responseJson.errorMessage) {
						var errorCode=responseJson.errorCode;
						var errorMessage=responseJson.errorMessage;
						alert("Failure:"+errorMessage);
					}
				}
			},
			failure: function(response){
				Ext.MessageBox.alert(getText('common.err.failure'), response.responseText);
			},
			method:"POST"
		});
	},

	onFilterBoxKeyUp:function(cmp, e, eOpts){
		var callme = false;
		if (cmp.getRawValue().length > 3){
			callme=true;
		}
		if (cmp.getRawValue().length < this.filter.length){
			callme = true;
		}
		if (cmp.getRawValue().length <= 3) {
			this.filter="";
		} else {
			this.filter = cmp.getRawValue();
		}
		if (callme) {
			this.reload();
		}
	},
	clearFilter:function(){
		var filterBox=this.lookupReference('filterBox');
		filterBox.setRawValue("");
		this.filter=filterBox.getRawValue();
		this.reload();
	},

	onGridEdit:function(editor, e) {
		e.record.commit();
		var className=e.record.data['className'];
		var value=e.value;
		var oldValue=e.originalValue;
		if(value!==oldValue){
			this.changeLevel(className,value);
		}
	},

	changeLevel:function(className,value){
		var me=this;
		var grid=this.lookupReference('grid');
		grid.setLoading(true);
		Ext.Ajax.request({
			url: "editAdminLoggingConfig!save.action",
			params:{
				className:className,
				level:value
			},
			disableCaching:true,
			scope: me,
			success: function(response){
				grid.setLoading(false);
				var responseJson = Ext.decode(response.responseText);
				if (responseJson.success === false) {
					Ext.MessageBox.alert(getText('common.err.failure'), response.responseText);
				}
			},
			failure: function(response){
				grid.setLoading(false);
				Ext.MessageBox.alert(getText('common.err.failure'), response.responseText);
			},
			method:"POST"
		});
	}
});

