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

Ext.define('com.trackplus.layout.MasterHomeLayout',{
	extend:'com.trackplus.layout.BaseLayout',
	useToolbar:false,
	//selectedGroup:'wiki',
	constructor : function(config) {
		var me = this;
		me.callParent(arguments);
		me.borderLayoutController.setHelpContext("masterHome");
		//me.wikiController=Ext.create('com.trackplus.wiki.WikiController',{
		//	data:me.initData
		//});
		me.onReady(function() {
			//borderLayout.setActiveToolbarList([]);
			//borderLayout.setVisibleToolbar(false);
		});
	},
	createCenterPanel:function(){
		var me=this;
		var data=me.initData.modules;
		var items=new Array();
		for(var i=0;i<data.length;i++){
			items.push(Ext.create('com.trackplus.master.AppBoxView',{
				width:100,
				height:100,
				appID:data[i].id,
				appUrl:data[i].url,
				appTitle:data[i].name,
				appIcon:"appJenkins" //data[i].iconCls
			}));
		}

		me.view=Ext.create('Ext.panel.Panel',{
			border:false,
			bodyBorder:false,
			bodyCls:'appPanel',
			height:200,
			layout:'column',
			items:items
		});
		var maxWidth=125*data.length;
		if(maxWidth>1000){
			maxWidth=1000;
		}
		return  Ext.create('Ext.panel.Panel',{
			region: 'center',
			border: false,
			baseCls:'x-plain',
			unstyled: true,
			layout:{
				type:'ux.center',
				maxWidth:maxWidth
			},
			items:[me.view]
		});
	}
});


Ext.define('com.trackplus.master.AppBoxView',{
	extend:'Ext.panel.Panel',
	layout:'border',
	border:false,
	bodyBorder:false,
	cls:'appBox',
	overCls:'appBoxOver',
	config:{
		appID:null,
		appUrl:null,
		appTitle:null,
		appIcon:null
	},
	initComponent: function(){
		var me=this;
		me.items=me.createChildren();
		me.addListener('afterrender',function(cmp){
			cmp.getEl().addListener('click',me.clickHandler,me);
		});
		me.callParent();
	},
	clickHandler:function(){
		var me=this;		
		window.location.href=me.appUrl;
	},
	createChildren:function(){
		var me=this;
		var items=new Array();
		var centerPanel=Ext.create('Ext.panel.Panel',{
			region:'center',
			border:false,
			bodyBorder:false,
			layout:'fit',
			id:'XXX',
			cls:'appBoxImage',
			items:[{xtype:'component',cls:me.appIcon}]
		});
		items.push(centerPanel);
		items.push({
			xtype:'component',
			cls:'appBoxTitle',
			html:me.appTitle,
			iconCls:me.appIcon,
			region:'south'
		});
		return items;
	}

});
