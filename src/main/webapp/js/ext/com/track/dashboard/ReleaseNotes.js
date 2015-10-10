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

Ext.define('js.ext.com.track.dashboard.ReleaseNotes',{
	extend:'js.ext.com.track.dashboard.DashboardRenderer',
	layout:'anchor',
	defaults:{
		anchor:'100%'
	},
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
		var projects=me.jsonData.result;
		var prjItems=[];
		if(projects!=null){
			for(var i=0;i<projects.length;i++){
				prjItems.push(me.createReleaseHeaderGroup.call(me,projects[i]));
				prjItems.push(me.createReleaseGroup.call(me,projects[i],i<projects.length-1));
			}
		}else{
			var panel=Ext.create('Ext.panel.Panel', {
				html:"...",
				border:false,
				unstyled: true,
				bodyPadding: 0
			});
			prjItems.push(panel);
		}
		return prjItems;
	},
	createReleaseHeaderGroup:function(project){
		var me=this;
		var items=new Array(0);

		var htmSymbol='<img src="'+com.trackplus.TrackplusConfig.iconsPath+project.symbol+'" >';

		var baseURL='itemNavigator.action?queryType=2&queryID='+me.jsonData.dashboardID+
			'&dashboardParams.dashboardID='+me.jsonData.dashboardID;
		var projectID=project.projectID;
		var releaseID=project.releaseID;
		if(projectID!=null){
			baseURL+="&dashboardParams.projectID="+projectID;
		}
		if(releaseID!=null){
			baseURL+="&dashboardParams.releaseID="+releaseID;
		}
		var urlNrA=baseURL+"&dashboardParams.showAll=true";
		var urlNrO=baseURL+"&dashboardParams.showAll=false&dashboardParams.openOnly=true";
		var urlNrR=baseURL+"&dashboardParams.showAll=false&dashboardParams.openOnly=false";
		var htmNr='<a href="'+urlNrA+'" class="synopsis_blue">'+project.number+'</a>'+
		'&nbsp;(<a href="'+urlNrR+'" class="synopsis_blue">'+project.numberResolved+'&nbsp;'+getText('releaseNotes.resolved')+'</a>,&nbsp;'+
		'<a href="'+urlNrO+'" class="synopsis_blue">'+project.numberOpen+'&nbsp;'+getText('releaseNotes.unresolved')+'</a>)';

		var htmNrRes='<a href="'+urlNrR+'" class="synopsis_blue">'+project.numberResolved+'</a>'+
		'&nbsp;(<a href="'+urlNrA+'" class="synopsis_blue">'+project.number+'&nbsp;'+getText('releaseNotes.all')+'</a>,&nbsp;'+
		'<a href="'+urlNrO+'" class="synopsis_blue">'+project.numberOpen+'&nbsp;'+getText('releaseNotes.unresolved')+'</a>)';

		var htmNrUnRes='<a href="'+urlNrO+'" class="synopsis_blue">'+project.numberOpen+'</a>'+
		'&nbsp;(<a href="'+urlNrA+'" class="synopsis_blue">'+project.number+'&nbsp;'+getText('releaseNotes.all')+'</a>,&nbsp;'+
		'<a href="'+urlNrR+'" class="synopsis_blue">'+project.numberResolved+'&nbsp;'+getText('releaseNotes.resolved')+'</a>)';

		htmNr = htmNrUnRes;

		if(project.areResolved != null && project.areResolved==true) {
			htmNr=htmNrRes;
		}

		var htmlImgs='<div class="barWrapper"><img src="'+com.trackplus.TrackplusConfig.iconsPath+'GreenBar.gif" '+
			'width="'+project.widthResolved+'" height="10" alt="GreenBar">'+
			'<img src="'+com.trackplus.TrackplusConfig.iconsPath+'LightBlueBar.gif" '+
			'width="'+project.widthOpen+'" height="10" alt="LightBlueBar"></div>';

		items.push({
			xtype:'component',
			width:35,
			style:{
				paddingLeft:'5px'
			},
			html:htmSymbol
		});
		items.push({
			xtype:'component',
			style:{
				paddingTop:'5px',
				whiteSpace:'nowrap',
				overflow:'hidden'
			},
			flex:1,
			html:'<b>'+project.label+'</b>'
		});
		items.push({
			xtype:'component',
			style:{
				paddingTop:'5px',
				paddingRight:'5px'
			},
			html:htmNr
		});
		items.push({
			xtype:'component',
			style:{
				paddingLeft:'5px',
				paddingRight:'5px',
				paddingTop:'7px'
			},
			html:htmlImgs
		});
		return Ext.create('Ext.panel.Panel',{
			border:false,
			bodyBorder:false,
			bodyPadding: 0,
			layout: {
				type: 'hbox',
				pack: 'start',
				align: 'stretch'
			},
			height:24,
			items:items
		});
	},
	createReleaseGroup:function(project,hasNext){
		var me=this;
		var data=[];
		var list=project.list;
		if(list!=null){
			for(var i=0;i<list.length;i++){
				var releaseNote=list[i];
				var issueType=releaseNote.issueType;
				var workItems=releaseNote.workItems;
				for(var j=0;j<workItems.length;j++){
					var workItem=workItems[j];
					var issueTypeSymbol='';
					issueTypeSymbol='<img alt="." src="optionIconStream.action?fieldID=-2&optionID='+issueType.id+'">';
					data.push({
						'objectID':workItem.objectID,
						'synopsis':workItem.synopsis,
						'issueTypeID':issueType.id,
						'issueTypeSymbol':issueTypeSymbol,
						'issueTypeLabel':issueType.label
					});
				}
			}
		}
		var store = Ext.create('Ext.data.Store', {
			fields: [
				{name: 'objectID',type:'int'},
				{name: 'synopsis'},
				{name: 'issueTypeID',type:'int'},
				{name: 'issueTypeSymbol'},
				{name: 'issueTypeLabel'}
			],
			groupField: 'issueTypeLabel',
			data: data
		});
		var groupingFeature = Ext.create('Ext.grid.feature.Grouping',{
			groupHeaderTpl: '{name} ({rows.length} Item{[values.rows.length > 1 ? "s" : ""]})'
		});
		var margin= '0 0 5 0';
		if(hasNext){
			margin='0 0 5 0';
		}
		return Ext.create('Ext.grid.Panel', {
			store: store,
			cls:'gridWithIcons',
			//height:120,
			features: [groupingFeature],
			columns: [
				{
					text	 : 'icon',
					width	 : 32,
					sortable : true,
					dataIndex: 'issueTypeSymbol'
				},{
					text	 : 'objectID',
					width	 : 75,
					sortable : true,
					dataIndex: 'objectID'
				},{
					text	 :'synopsis',
					flex	: 1,
					sortable : true,
					dataIndex: 'synopsis'
				}
			],
			hideHeaders:true,
			bodyBorder:false,
			border:false,
			columnLines :false,
			margin:margin,
			lines :false,
			viewConfig: {
				stripeRows: true
			}
		});
	},
	doRefresh:function(jsonData){
		var me=this;
		me.jsonData=jsonData;
		me.removeAll();
		me.add(me.createChildren());
	}
});
