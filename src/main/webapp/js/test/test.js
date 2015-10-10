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


Ext.define('com.trackplus.test.Test',{
	extend:'Ext.Base',
	config: {
		name:null
	},
	baseLayout:null,
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	},
	setUp:function(){
		var me=this;
		me.baseLayout=borderLayout.controller.baseLayout;
	},
	tearDown:function(){
	},
	isElementPresent:function(id){
		var me=this;
		me.setUp();
		var result=me.doIsElementPresent(id);
		me.tearDown();
		return result;
	},
	doIsElementPresent:function(id){
		return false;
	},
	hasLabel:function(id,label){
		var me=this;
		me.setUp();
		var result=me.doHasLabel(id,label);
		me.tearDown();
		return result;
	},
	doHasLabel:function(id,label){
		return false;
	},
	clickOnElement:function(id){
		var me=this;
		me.setUp();
		var result=me.doClickOnElement(id);
		me.tearDown();
		return result;
	},
	doClickOnElement:function(id){
		return false;
	}
});
