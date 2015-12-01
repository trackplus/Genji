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

Ext.define('com.trackplus.test.logon.LogonTest',{
	extend:'com.trackplus.test.Test',
	name:'Logon',
	logonPage:null,
	view:null,
	controller:null,
	setUp:function(){
		var me=this;
		me.callParent(arguments);
		me.logonPage=me.baseLayout.logonPage;
		me.view=me.logonPage.view;
		me.controller=me.logonPage.controller;
	},
	tearDown:function(){
	},
	doIsElementPresent:function(id){
		return false;
	},
	doHasLabel:function(id,label){
		return false;
	},
	doClickOnElement:function(id){
	}
});

var logonTest=Ext.create('com.trackplus.test.logon.LogonTest',{});
