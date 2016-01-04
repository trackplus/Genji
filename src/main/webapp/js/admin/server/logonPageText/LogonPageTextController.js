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

Ext.define('com.trackplus.admin.server.logonPageText.LogonPageTextController',{
	extend: "Ext.app.ViewController",
	alias: "controller.logonPageText",

	onSave:function(button, event){
		var panelForm=this.getView();
		panelForm.setLoading(true);
		panelForm.getForm().submit({
			method :'POST',
			success: function(form, action) {
				panelForm.setLoading(false);
			},
			failure: function(form, action) {
				panelForm.setLoading(false);
				Ext.Msg.alert('Failure',action.result.errorMessage);
				var errorCode=action.result.errorCode;
				var errorMessage=action.result.errorMessage;
			}
		});
	},
	onLocaleSelect:function(field,record) {
		var theLocale=field.getValue();
		this.reload(theLocale);
	},
	onAfterRender:function(cmp,opt){
		this.reload();
	},
	reload:function(theLocale){
		var url="logonPageText.action";
		var panelForm=this.getView();
		panelForm.setLoading(true);
		panelForm.getForm().load({
			url : url,
			params:{
				'theLocale':theLocale
			},
			success : this.afterLoadForm,
			scope:this
		});
	},
	afterLoadForm:function(form, action){
		var panelForm=this.getView();
		panelForm.setLoading(false);
		var data=action.result.data;
		var cmbLocale=this.lookupReference('cmbLocale');
		cmbLocale.store.loadData(data["localeList"],false);
		cmbLocale.setValue(data["theLocale"]);
	}
});
