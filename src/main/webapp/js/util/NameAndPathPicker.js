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

Ext.define("com.trackplus.util.NameAndPathPicker",{
	extend:'Ext.Base',
	config: {
		title:"",
		loadUrl: null,
		loadParams: null,
		pathTree: null,
		handler: null,
		//validateHandler: null,
		scope: null,
		width: 400,
		height: 170
	},
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		this.initConfig(config);
	},
	showDialog:function(){
		var me=this;
		var items = [];
		me.filterName = CWHF.createTextField("common.lbl.name", "label", {labelWidth:80, width:350, allowBlank:false, msgTarget: "qtip"});
		me.pathPicker = CWHF.createSingleTreePicker("common.lbl.path", "path", me.pathTree, null, {labelWidth:80,width:350, allowBlank:false});
		me.includeInMenu = CWHF.createCheckbox("admin.customize.queryFilter.lbl.menu", "includeInMenu", {labelWidth:80, width: 100});
		items.push(me.filterName, me.pathPicker, me.includeInMenu);
		var windowParameters = {title:me.title,
				width:me.width,
				height:me.height,
				load: {loadUrl: this.loadUrl, loadUrlParams:this.loadParams},
				postDataProcess: this.loadPath,
				submit: {submitHandler: me.submitSaveAs, validateHandler: me.validateHandler},
				items: items};
			var windowConfig = Ext.create("com.trackplus.util.WindowConfig", windowParameters);
			windowConfig.showWindowByConfig(this);
	},

	submitSaveAs: function(window, submitUrl, submitUrlParams) {
		var me=this;
		me.handler.call(me.scope, window, me.filterName.getValue(), me.pathPicker.getValue(), me.includeInMenu.getValue());
	},

	loadPath: function(data, panel, extraConfig)  {
		this.pathPicker.updateMyOptions(data);
	},

	validateHandler: function(submit, win) {
		var me=this;
		return me.formEdit.isValid();
	}

});
