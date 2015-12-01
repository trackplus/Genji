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

com.trackplus.browseProjects.urlEditScreen="dashboardEdit.action";
com.trackplus.browseProjects.urlResetScreen="browseProjects!resetDashboard.action";

com.trackplus.browseProjects.btnProjectDashEdit= new Ext.Button({
	id:'projectDashEditButton',
	itemId:'projectDashEditButtonItemID',
	overflowText:getText('cockpit.config'),
	tooltip:getText('cockpit.config.tt'),
	text: getText('cockpit.config'),
	iconCls: 'cockpitEdit',
	disabled:true,
	handler:function(){
		window.location.href=com.trackplus.browseProjects.urlEditScreen;
	}
});

com.trackplus.browseProjects.btnProjectDashReset= new Ext.Button({
	xtype:'button',
	id:'projectDashResetButton',
	overflowText:getText('cockpit.reset'),
	tooltip:getText('cockpit.reset'),
	text: getText('cockpit.reset'),
	iconCls: 'cockpitReset',
	disabled:true,
	handler:function(){
		com.trackplus.browseProjects.resetDashboard();
	}
});
com.trackplus.browseProjects.btnSaveAsTemplatetDash= new Ext.Button({
	xtype:'button',
	id:'projectDashSaveAsTemplatetButton',
	overflowText:getText('cockpit.saveAsTemplate'),
	tooltip:getText('cockpit.saveAsTemplate'),
	text: getText('cockpit.saveAsTemplate.tooltip'),
	iconCls: 'cockpitSave',
	disabled:true,
	handler:function(){
		com.trackplus.browseProjects.saveAsDefault();
	}
});

com.trackplus.browseProjects.westPanel=null;
com.trackplus.browseProjects.browseProjectCenterPanel=null;
com.trackplus.browseProjects.treeProjects=null;

com.trackplus.browseProjects.resetDashboard=function(){
	var me=this;
	com.trackplus.dashboard.resetDashboard(com.trackplus.dashboard.urlResetScreen,{}, me.resetDashboardSuccess,me);
};
com.trackplus.browseProjects.resetDashboardSuccess=function(){
	com.trackplus.browseProjects.showProject(com.trackplus.browseProjects.projectID,com.trackplus.browseProjects.entityType,com.trackplus.browseProjects.releaseID);
};
com.trackplus.browseProjects.saveAsDefault=function(){
	var me=this;
	com.trackplus.dashboard.saveAsDefault(com.trackplus.browseProjects.projectID,com.trackplus.browseProjects.entityType);
};

com.trackplus.browseProjects.createWestPanel=function(){
	var store = Ext.create('Ext.data.Store', {
	fields: [
		{name: 'label'},
		{name: 'value'},
		{name: 'cssClass'},
		{name: 'img'},
		{name: 'entityID'},
		{name:'optionID'}
		],
		data: []
	});

	// create the Grid
	var grid = Ext.create('Ext.grid.Panel', {
		//region:'center',
		id:'gridDetail',
		store: store,
		border:false,
		disableSelection: true,
		columns: [
			{dataIndex: 'label',flex:1},
			{dataIndex: 'value',flex:1,renderer : com.trackplus.browseProjects.gridDetailRenderer}
		],
		hideHeaders:true,
		columnLines :false,
		viewConfig: {
			stripeRows: true,
			trackOver  :false
		},
		autoHeight:true
	});
	var panelWest = Ext.create('Ext.panel.Panel',{
		layout: 'border',
		id: 'layout-browser',
		region:'west',
		border: false,
		split:true,
		width: 275,
		listeners:{
			collapse:function(e){
				com.trackplus.browseProjects.storeSplitCollapsed();
			},
			expand:function(e){
				com.trackplus.browseProjects.storeSplitCollapsed();
			}
		},
		items:[com.trackplus.browseProjects.treeProjects,
			Ext.create('Ext.panel.Panel',{
				id:'projectDetailPanel',
				region:'south',
				border:false,
				bodyBorder:false,
				height:325,
				split:true,
				margins:'0 0 0 0',
				collapsible:true,
				/*layout: {
					type: 'vbox',
					align : 'stretch',
					pack  : 'start'
				},*/
				collapseMode:'mini',
				title:'Detail',
				items:[
					{
						border:false,
						bodyBorder:false,
						margin:'0 0 0 0',
						layout:{
							type: 'hbox'
						},
						items:[
							{xtype: 'image',id:'imageDetail',src:com.trackplus.TrackplusConfig.iconsPath+'project.png',width:20, margin:'5 5 0 5'},
							{xtype: 'displayfield',id:'titleDetail',value:'<b>...</b>',flex:1,margin:'5 5 0 0'}
						]
					},
					{xtype: 'displayfield',id:'descriptionDetail',value:'...'},
					grid
				]
			})
		]
	});
	return panelWest;
};
com.trackplus.browseProjects.replaceDetail=function(data){
	var imgDetail=Ext.getCmp("imageDetail");
	var titleDetail=Ext.getCmp("titleDetail");
	var descriptionDetail=Ext.getCmp("descriptionDetail");
	var gridDetail=Ext.getCmp("gridDetail");
	imgDetail.setSrc(com.trackplus.TrackplusConfig.iconsPath+data.image);
	titleDetail.setValue(data.title);
	descriptionDetail.setValue(data.description);
	gridDetail.store.loadData(data.gridData);
};
com.trackplus.browseProjects.gridDetailRenderer=function(value,metaData,record,colIndex,store,view){
	var result="";
	if(record.data.img&&record.data.img!==''){
		result+='<img src="'+com.trackplus.TrackplusConfig.iconsPath+record.data.img+'"\>';
	}
	if(record.data.optionID&&record.data.optionID!==''){
		var srcImg="optionIconStream.action?fieldID="+record.data.entityID+"&optionID="+record.data.optionID;
		result+='<img src="'+srcImg+'" style="margin-right:3px"\>';
	}
	if(record.data.cssClass){
		result+='<span class="'+record.data.cssClass+'">'+value+'</span>';
	}else{
		result+=value;
	}
	return result;
};

com.trackplus.browseProjects.showProject=function(projectID,entityType,releaseID){
	Ext.Ajax.request({
		url: 'dashboard.action',
		disableCaching :true,
		method:'POST',
		params:{"projectID":projectID,"entityType":entityType,'releaseID':releaseID},
		success: function(response){
			var jsonData=Ext.decode(response.responseText);
			var data=jsonData.data;
			var screenModel=com.trackplus.screen.createScreenModel(data.screen);
			var refreshTabUrl='dashboardTabRuntime!execute.action?projectID='+projectID;
			var storeTabUrl='dashboard!storeLastSelectedTab.action?projectID='+projectID+"&entityType="+entityType;
			if(releaseID){
				refreshTabUrl+="&releaseID="+releaseID;
			}
			var screenFacade= Ext.create('com.trackplus.screen.BaseScreenFacade',{
				screenModel:screenModel,
				lastSelectedTab:data.lastSelectedTab,
				projectID:data.projectID,
				releaseID:data.releaseID,
				refreshTabUrl:refreshTabUrl,
				storeTabUrl:storeTabUrl,
				tabViewCls:'com.trackplus.screen.DashboardTabView',
				panelViewCls:'com.trackplus.screen.DashboardPanelView',
				fieldErrorCls:'com.trackplus.screen.DashboardFieldErrorView'
			});

			var tabPanel= screenFacade.createViewComponent();
			com.trackplus.browseProjects.browseProjectCenterPanel.removeAll(true);
			com.trackplus.browseProjects.browseProjectCenterPanel.add(tabPanel);
			com.trackplus.browseProjects.browseProjectCenterPanel.updateLayout();
		}
	});

	/*com.trackplus.browseProjects.browseProjectCenterPanel.getLoader().load({
		url: 'browseProject.action',
		params: {"projectID":project,"releaseID":release,"entityType":entityType},
		scripts:true
	});*/

};

com.trackplus.browseProjects.findParentProject=function(node){
	var parentNode=node.parentNode;
	if(parentNode.data.id<0){//project
		return (-1)*node.parentNode.data.id;
	}else{
		return com.trackplus.browseProjects.findParentProject(parentNode);
	}
};
com.trackplus.browseProjects.treeNodeClick=function(view,node){
	var entityType=1;//PROJECT
	var projectID;
	var releaseID=null;
	var id=node.data.id;
	var entityID=parseInt(id);
	var urlStr;
	var projectDetailPanel=Ext.get("projectDetailPanel");
	var urlDetail;
	//com.trackplus.browseProjects.btnProjectDashEdit.setDisabled(true);
	if(entityID<0){
		//project
		entityType=1;//PROJECT
		projectID=-1*entityID;
		com.trackplus.browseProjects.urlEditScreen="dashboardEdit.action?backAction=browseProjects.action&projectID="+projectID+"&entityType=1";
		com.trackplus.dashboard.urlResetScreen="project!cokpitAssignment.action?projectID="+projectID+"&entityType=1"
	}else{
		//release
		releaseID=entityID;
		entityType=9;//RELEASESCHEDULED
		var parentID=com.trackplus.browseProjects.findParentProject(node);
		projectID=parentID;
		com.trackplus.browseProjects.urlEditScreen="dashboardEdit.action?backAction=browseProjects.action&projectID="+projectID+"&entityType=9";
		com.trackplus.dashboard.urlResetScreen="project!cokpitAssignment.action?projectID="+projectID+"&entityType=9"
	}
	var detailPanel=Ext.getCmp('projectDetailPanel');
	detailPanel.setLoading(com.trackplus.TrackplusConfig.getText("common.lbl.loading"));
	Ext.Ajax.request({
		url: 'browseProjects!entityDetail.action',
		params:{"entityID":entityID,"entityType":entityType},
		success: function(response){
			var jsonData=Ext.decode(response.responseText);
			var newPanel;
			var data={};
			var canEdit=jsonData.data.canEdit;
			if(canEdit===true){
				com.trackplus.browseProjects.btnProjectDashEdit.setDisabled(false);
				com.trackplus.browseProjects.btnProjectDashReset.setDisabled(false);
				com.trackplus.browseProjects.btnSaveAsTemplatetDash.setDisabled(false);
			}else{
				com.trackplus.browseProjects.btnProjectDashEdit.setDisabled(true);
				com.trackplus.browseProjects.btnProjectDashReset.setDisabled(true);
				com.trackplus.browseProjects.btnSaveAsTemplatetDash.setDisabled(true);
			}
			if(entityType===1){//project
				data.title=jsonData.data.projectLabel;
				data.description=jsonData.data.projectDescription;
				data.image='project.png';
				var projectLinkingImg="bulkCheck.gif";
				if(jsonData.data.projectLinking){
					projectLinkingImg='bulkCheck-select.gif';
				}
				var projectWorkCostImg="bulkCheck.gif";
				if(jsonData.data.projectWorkCost){
					projectWorkCostImg='bulkCheck-select.gif';
				}
				data.gridData=[
					{label:getText('common.lbl.projectType'),value:jsonData.data.projectType},
					{label:getText('admin.customize.localeEditor.type.projectStatus'),value:jsonData.data.projectState},
					{label:getText('admin.project.lbl.defaultManager'),value:jsonData.data.defaultManager},
					{label:getText('admin.project.lbl.defaultResponsible'),value:jsonData.data.defaultResponsible},
					{label:getText('admin.project.lbl.defaultInitialState'),value:jsonData.data.defaultItemState,
						cssClass:'dataEmphasize',optionID:jsonData.data.defaultItemStateID,entityID:-4},
					{label:getText('admin.project.lbl.defaultIssueType'),value:jsonData.data.defaultIssueType,
						optionID:jsonData.data.defaultIssueTypeID,entityID:-2},
					{label:getText('admin.project.lbl.defaultPriority'),value:jsonData.data.defaultPriority,
						optionID:jsonData.data.defaultPriorityID,entityID:-10},
					{label:getText('admin.project.lbl.defaultSeverity'),value:jsonData.data.defaultSeverity,
						optionID:jsonData.data.defaultSeverityID,entityID:-11},
					{label:getText('admin.project.lbl.linking'),value:'',img:projectLinkingImg},
					{label:getText('admin.project.lbl.accounting'),value:'',img:projectWorkCostImg}
				];
			}else{//release
				data.title=jsonData.data.label;
				data.description=jsonData.data.description;
				data.image='releaseActive.png';
				var imgNoticed="bulkCheck.gif";
				if(jsonData.data.noticedDefault){
					imgNoticed='bulkCheck-select.gif';
				}
				var imgSchedule="bulkCheck.gif";
				if(jsonData.data.scheduleDefault){
					imgSchedule='bulkCheck-select.gif';
				}
				data.gridData=[
					{label:getText('admin.customize.localeEditor.type.releaseStatus'),value:jsonData.data.state},
					{label:getText('admin.project.release.lbl.dueDate'),value:jsonData.data.dueDate},
					{label:getText('admin.project.release.lbl.defaultReleaseNoticed'),value:'',img:imgNoticed},
					{label:getText('admin.project.release.lbl.defaultReleaseScheduled'),value:'',img:imgSchedule}

				];
				//newPanel=com.trackplus.browseProjects.createReleaseDetailPanel(jsonData.data);
			}
			com.trackplus.browseProjects.replaceDetail(data);
			detailPanel.updateLayout();
			detailPanel.setLoading(false);
			/*detailPanel.removeAll();
			detailPanel.add(newPanel);
			detailPanel.updateLayout();*/
			//var detailDiv = document.getElementById("detailDiv");
			//detailDiv.innerHTML=result.responseText;
		},
		failure: function(error){
			detailPanel.setLoading(false);
			//var detailDiv = document.getElementById("detailDiv");
			//detailDiv.innerHTML=error.message;
		},
		method:"POST"
	});
	com.trackplus.browseProjects.projectID=projectID;
	com.trackplus.browseProjects.entityType=entityType;
	com.trackplus.browseProjects.releaseID=releaseID;

	com.trackplus.browseProjects.showProject(projectID,entityType,releaseID);
};

com.trackplus.browseProjects.storeSplitCollapsed=function(){
	Ext.Ajax.request({
		url: "browseProjects!storeSpliterCollapsed.action",
		disableCaching:true,
		success: function(){
		},
		failure: function(){
		},
		method:'POST',
		params:{"editSpliterCollapses":com.trackplus.browseProjects.westPanel.collapsed}
	});
};

com.trackplus.browseProjects.createCustomCenterPanel=function(data){
	var store = Ext.create('Ext.data.TreeStore', {
		proxy:{
			type: 'ajax',
			url: 'browseProjects!expandNode.action'
		},
		fields: ['id','text','type', 'canEdit','icon','leaf'],
		root: {
			expanded: true,
			text:"",
			user:"",
			status:"",
			children:CWHF.isNull(data.treeDataProjects)?[]:data.treeDataProjects
		}

	});
	com.trackplus.browseProjects.treeProjects=Ext.create('Ext.tree.Panel', {
		region:'center',
		useArrows: true,
		autoScroll: true,
		store: store,
		rootVisible: false,
		border: false,
		margins: '0 0 0 0',
		baseCls:'x-plain',
		bodyStyle:{border:'none'},
		cls:'westTreeNavigator'
	});
	//com.trackplus.browseProjects.treeProjects.store.on('beforeexpand', function(node) {
	com.trackplus.browseProjects.treeProjects.store.on('beforeload', function(store, operation) {
		var node=operation.node;
		if(node){
			var id=node.data.id;
			id=id.substring(1,id.length);
			var entityID=parseInt(id);
			var entityType=node.data.type;
			com.trackplus.browseProjects.treeProjects.store.proxy.extraParams = {
				entityID:entityID,
				entityType:entityType
			}
		}
	});
	//com.trackplus.browseProjects.treeProjects.expandAll();
	//treeProjects = new ApiPanel();
	com.trackplus.browseProjects.treeProjects.on('itemclick',com.trackplus.browseProjects.treeNodeClick);
	//com.trackplus.browseProjects.westPanel=com.trackplus.browseProjects.createWestPanel();
	com.trackplus.browseProjects.browseProjectCenterPanel=new Ext.Panel({
		region:'center',
		border:false,
		autoScroll: true,
		header:false,
		style:{
			// borderLeft:'1px solid #D0D0D0'
		},
		bodyStyle:{
			padding:'2px 0px 0px 0px'
		},
		layout:'fit'
	});

	/*var mainPanel = new Ext.Panel({
		region: 'center',
		margins: '0 0 0 0',
		border: false,
		baseCls:'x-plain',
		layout:'border',
	 	items: [com.trackplus.browseProjects.westPanel,com.trackplus.browseProjects.browseProjectCenterPanel]
	});*/
	return com.trackplus.browseProjects.browseProjectCenterPanel;
};


Ext.define('com.trackplus.layout.BrowseProjectsLayout',{
	extend:'com.trackplus.layout.BaseLayout',
	useToolbar:true,
	selectedGroup:'browseProjects',
	constructor : function(config) {
		var me = this;
		me.callParent(arguments);
		me.borderLayoutController.setHelpContext("browseProject");
		me.onReady(function(){
			var data=me.initData;
			var dashToolbar=[com.trackplus.browseProjects.btnProjectDashEdit];
			if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
				dashToolbar.push(com.trackplus.browseProjects.btnProjectDashReset);
				dashToolbar.push(com.trackplus.browseProjects.btnSaveAsTemplatetDash);
			}
			me.borderLayoutController.setActiveToolbarList(dashToolbar);
			if(CWHF.isNull(data.treeDataProjects)){
				CWHF.showMsgError(getText("browseProject.err.noProjectToBrowse"));
				return;
			}
			var node=com.trackplus.browseProjects.treeProjects.getStore().getNodeById(data.selectedNode);
			if(node){
				com.trackplus.browseProjects.treeProjects.getSelectionModel().select(node);
				com.trackplus.browseProjects.treeProjects.expandPath(node.getPath());
				com.trackplus.browseProjects.treeNodeClick(null,node);
			}
		});
	},
	createCenterPanel:function(){
		var me=this;
		return com.trackplus.browseProjects.createCustomCenterPanel(me.initData);
	},
	createWestPanel:function(){
		com.trackplus.browseProjects.westPanel=com.trackplus.browseProjects.createWestPanel();
		return com.trackplus.browseProjects.westPanel;
	}
});
