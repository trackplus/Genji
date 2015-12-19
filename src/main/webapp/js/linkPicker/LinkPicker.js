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


Ext.define('com.trackplus.linkPicker.LinkPicker',{
	extend:'Ext.Base',
	config: {
		workItemID:null,
		useDocument:false,
		parent:false,
		projectID:null,
		//projectName:null,
		title:"",
		handler:null,
		scope:null,
		ajaxContext:null,
		width:800,
		height:500,
		stateId:null,
		linkType:0,
		linkText:'',
		linkURL:'',
		linkObjectID:null,
		linkTarget:null
	},
	linkPikerController:null,
	constructor : function(config) {
		var me = this
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		this.initConfig(config);
		me.linkPikerController=Ext.create('com.trackplus.linkPicker.LinkPickerController',{
			workItemID:me.getWorkItemID(),
			projectID:me.getProjectID(),
			useDocument:me.getUseDocument(),
			parent:me.getParent(),
			selectedProjectOrRelease:me.getProjectID(),
			//selectedProjectOrReleaseName:me.projectName,
			title:me.getTitle(),
			handler:me.getHandler(),
			scope:me.getScope(),
			ajaxContext:me.getAjaxContext(),
			width:me.getWidth(),
			height:me.getHeight(),
			stateId:me.getStateId(),
			linkType:me.getLinkType(),
			linkText:me.getLinkText(),
			linkURL:me.getLinkURL(),
			linkObjectID:me.getLinkObjectID(),
			linkTarget:me.getLinkTarget()
		});
	},
	showDialog:function(){
		var me=this;
		me.linkPikerController.showDialog.call(me.linkPikerController)
	}
});

