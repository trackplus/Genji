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

com.trackplus.admin.server={};
com.trackplus.admin.adminEntityWin=null;
com.trackplus.admin.sections=new Object();
com.trackplus.admin.lastSelectedSection=null;
com.trackplus.admin.lastSelectedNode=null;
com.trackplus.admin.createAdminWestPanel=function(initData){
	var admin_westPanel= new Ext.create('Ext.panel.Panel', {
		region:'west',
		layout: 'accordion',
		width:200,
		border: false,
		split:true,
		//collapsible: true,
		collapseMode : 'mini',
		//baseCls:'x-plain',
		//margins: '0 0 0 0',
		//margins:'0 -5 0 0',
		defaults:{
			border:false
		},
		cls:'west-accordion',
	});
	var items=new Array();
	//myPreferencesSection always present even for external user
	items.push(com.trackplus.admin.getMyPreferencesSection());

	if (com.trackplus.TrackplusConfig.user.projectAdmin ||
			com.trackplus.TrackplusConfig.user.privateWorkspace) {
		//projects section allowed project administrators and end users with private workspace
		items.push(com.trackplus.admin.getProjectsSection(initData, false));

	}
	if (com.trackplus.TrackplusConfig.user.sys) {
		//users section visible only for system administrators
		items.push(com.trackplus.admin.getUsersSection());
	}
	//
	var customizationSection = com.trackplus.admin.getCustomizationSection();
	if (customizationSection) {
		//customization section is available for each non-external user
		//but not all customization items are available to all users
		items.push(customizationSection);
	}
	if (com.trackplus.TrackplusConfig.user.sys ||
			com.trackplus.TrackplusConfig.user.actions) {
		//action section
		items.push(com.trackplus.admin.getActionSection());
	}
	if (com.trackplus.TrackplusConfig.user.sysAdmin) {
		//server section available only for system administrators
		items.push(com.trackplus.admin.getServerSection());
	}

	admin_westPanel.add(items);
	return admin_westPanel;
};
com.trackplus.admin.sectionSelectHandler=function(p,treeComponent){
	var sectionSelected=p.id;
	var lastSelectedSection=com.trackplus.admin.lastSelectedSection;
	var lastSelectedNode=com.trackplus.admin.lastSelectedNode;
	var treeCmp=null;
	if(treeComponent){
		treeCmp=treeComponent;
	}else{
		treeCmp=Ext.getCmp('tree-'+sectionSelected);
	}
	var jsonData={};
	var selectedNodeID=null;
	var selectedNodeID=null;
	var selections = treeCmp.getSelectionModel().getSelection();
	if(selections&&selections.length>0){
		selectedNodeID=selections[0].data.id;
		jsonData=selections[0].data;
	}
	if(CWHF.isNull(selectedNodeID)){
		var rootNode=treeCmp.getStore().getRootNode();
		if(rootNode&&rootNode.hasChildNodes()){
			var node=rootNode.getChildAt(0);
			treeCmp.getSelectionModel().select(node);
			jsonData=node.data;
			selectedNodeID=jsonData.id;
		}
	}
	if(lastSelectedSection===sectionSelected&&lastSelectedNode===selectedNodeID){
		//same node
		return;
	}
	//borderLayout.borderLayoutController.abortOldRequests.call(borderLayout.borderLayoutController);
	com.trackplus.admin.replaceCenterContent(sectionSelected,selectedNodeID,jsonData);
};

com.trackplus.admin.myClick=function(view,record,item,index,e){
	var jsonData=record.data;
	var treeCmp=view.ownerCt;
	var sectionSelected=treeCmp.id.substring(5);//'tree-XXXX';
	var selectedNodeID=record.data.id;
	com.trackplus.admin.replaceCenterContent(sectionSelected,selectedNodeID,jsonData);
};
com.trackplus.admin.replaceCenterContent=function(sectionSelected,selectedNodeID,jsonData){
	com.trackplus.admin.storeLastSelectedNode(sectionSelected,selectedNodeID);
	borderLayout.borderLayoutController.replaceCenterContent.call(borderLayout.borderLayoutController,jsonData);
	var helpCtx="admin."+sectionSelected;
	if(sectionSelected!=='projectTreePanel' &&
			sectionSelected!=='projectTemplateTreePanel'&&selectedNodeID){
		helpCtx+="."+selectedNodeID;
	}
	var historyToken=sectionSelected+":"+selectedNodeID;
	borderLayout.addHistoryToken(historyToken);
	borderLayout.setHelpContext(helpCtx);
};


com.trackplus.admin.storeLastSelectedNode=function(sectionSelected,selectedNodeID){
	com.trackplus.admin.lastSelectedSection=sectionSelected;
	com.trackplus.admin.lastSelectedNode=selectedNodeID;
	Ext.Ajax.request({
		fromCenterPanel:true,
		url: "admin!storeLastSelectedNode.action",
		params:{
			sectionSelected:sectionSelected,
			selectedNodeID:selectedNodeID
		}
	});
};

com.trackplus.admin.createSectionTree=function(sectionID,sectionTreeData,title,tt,iconCls){
	this.sections[sectionID]=sectionTreeData;
	var store = Ext.create('Ext.data.TreeStore', {
		fields: ['id','text','tt','menuPath','url', 'useAJAX','script', 'iconCls'],
		root: {
			expanded: true,
			text:"",
			user:"",
			status:"",
			children:sectionTreeData
		}
	});
	var tree=Ext.create('Ext.tree.Panel', {
		id:'tree-'+sectionID,
		useArrows: true,
		autoScroll: true,
		store: store,
		rootVisible: false,
		border: false,
		margins: '0 0 0 0',
		//baseCls:'x-plain',
		//bodyStyle:{border:'none'},
		cls:'westTreeNavigator'
	});
	tree.on('itemclick',com.trackplus.admin.myClick);
	return new Ext.create('Ext.panel.Panel', {
		id:sectionID,
		title:title,
		layout:'fit',
		margins: '0 0 0 0',
		iconCls:iconCls,
		//bodyStyle:{border:'none'},
		cls:'accordionSection',
		listeners:{
			expand:{
				fn: function(p) {
					p.addCls('accordionSection-active');
					com.trackplus.admin.sectionSelectHandler(p);
				}
			},
			collapse:{
				fn: function(p) {
					p.removeCls('accordionSection-active');
				}
			},
			afterrender : function(panel) {
			/*var header = panel.header;
				header.setHeight(27);*/
			}
		},
		items:[tree]
	});
};

com.trackplus.admin.createProjectTree=function(initData, isTemplate) {
	var treeStore = Ext.create('Ext.data.TreeStore', {
		root: {
			expanded: true,
			id: "_"
		},
		fields: [{name : 'id', mapping : 'id', type: 'string'},
			{name : 'text', mapping : 'text', type: 'string'},
			{name : 'tt', mapping : 'tt', type: 'string'},
			{name : 'menuPath', mapping : 'menuPath', type: 'string'},
			{name : 'url', mapping : 'url', type: 'string'},
			{name : 'useAJAX', mapping : 'useAJAX', type: 'boolean'},
			{name : 'script', mapping : 'script', type: 'boolean'},
			{name : 'iconCls', mapping : 'iconCls', type: 'string'}
		],
		autoLoad:false,
		proxy: {
			type: 'ajax',
			url: 'project!projects.action',
			extraParams: {
				isTemplate: isTemplate
			}
		}
	});
	if(initData.sectionSelected==='projectTreePanel' ||
		initData.sectionSelected==='projectTemplateTreePanel'){
		treeStore.on('load', function(treeStore, records, successful, operation, node, eOpts) {
			if (node.isRoot()) {
				var selectedNode=null;
				if(initData.selectedNodeID){
					selectedNode=treeStore.getNodeById(initData.selectedNodeID);
				}
				if(CWHF.isNull(selectedNode)){
					selectedNode = node.firstChild;
				}
				if (CWHF.isNull(selectedNode)) {
					//no project exists at all
					/*borderLayout.setCenterContent(null);
					var treeWithGridConfig=Ext.create('com.trackplus.admin.project.ProjectConfig',{});
					borderLayout.setActiveToolbarActionList(
							treeWithGridConfig.getToolbarActions.call(treeWithGridConfig, true, true));*/
				} else {
					var tmpTreeID = "tree-projectTreePanel";
					var tree=Ext.getCmp(tmpTreeID);//selectedNode.getOwnerTree();
					tree.getSelectionModel().select(selectedNode);
					var jsonData=selectedNode.data;
					com.trackplus.admin.replaceCenterContent(initData.sectionSelected,selectedNode,jsonData);
				}
			}
		},treeStore);
	}

	var treeItemID = 'projectTree';
	var treeID = 'tree-projectTreePanel';
	var treeConfig = {
		xtype: 'treepanel',
		itemId: treeItemID,
		id:treeID,
		useArrows: true,
		autoScroll: true,
		store: treeStore,
		rootVisible: false,
		border:false,
		margins:'0 0 0 0',
		baseCls:'x-plain',
		bodyStyle:{border:'none'},
		cls:'westTreeNavigator',
		listeners:{
			itemcontextmenu:{
				fn:function(tree, record, item, index, evtObj) {
					evtObj.stopEvent();
					var treeNodeCtxMenu = com.trackplus.admin.createCtxMenuProject(record, isTemplate);
					treeNodeCtxMenu.showAt(evtObj.getXY());
					return false;
				}
			}
		},
		viewConfig:{
			plugins:  {
				ptype: 'treeviewdragdrop',
				dragGroup: 'projectAdminDDGroup',
				dropGroup: 'projectAdminDDGroup',
				appendOnly: true,
				enableDrag: true,
				enableDrop: true
			},
			listeners: {
				beforedrop: {
					fn: function(node, data, overModel, dropPosition) {
						var nodeToDrag = data.records[0];
						if (nodeToDrag.data.leaf===true) {
							return false;
						}
						if(overModel.data.leaf===true) {
							return false;
						}
						return true;
					}
				},
				drop: {
					fn: function(node, data, overModel, dropPosition) {
						var copy = data.copy;
						if (CWHF.isNull(copy)) {
							//move by default
							copy=false;
						}
						com.trackplus.admin.dropAdminProject(data.records[0], overModel, treeStore);
					}
				}
			}
		}
	};
	var tree = Ext.create('Ext.tree.Panel', treeConfig);
	var sectionKey = 'projects';
	this.sections[sectionKey]=tree;

	/*var treeWithGridConfig=Ext.create('com.trackplus.admin.project.ProjectConfig',
			{projectTree:this.sections[sectionKey],
			sys:com.trackplus.TrackplusConfig.user.sys,isTemplate:isTemplate});*/
	//not itemclick like for other sections because
	//the selection is possible also after add/delete without item click
	//select to automatically actualize the details after add/delete without item click
	tree.on('itemclick',com.trackplus.admin.myClick);

	var itemID = 'projectTreePanel';
	var title = getText('menu.admin.project');
	return new Ext.create('Ext.panel.Panel', {
		id:itemID,
		title:title,
		layout:'fit',
		margins: '0 0 0 0',
		iconCls:'projects-ticon',
		cls:'accordionSection',
		listeners:{
			expand:{
				fn: function(p) {
					p.addCls('accordionSection-active');
					var tmpTreeID = "projectTree";
					var projectTree = p.getComponent(tmpTreeID);
					if (projectTree) {
						rootNode = projectTree.getRootNode();
						if (rootNode && !rootNode.hasChildNodes()) {
							//if there is no project at all for this user: set the center content to null and the toolbar with addProject (if system admin)
							borderLayout.borderLayoutController.setCenterContent(null);
							/*borderLayout.setActiveToolbarActionList(
								this.getToolbarActions.call(this, false));*/
						}

						com.trackplus.admin.sectionSelectHandler(p,projectTree);
					}

				}/*,
				scope: treeWithGridConfig*/
			},
			collapse:{
				fn: function(p) {
					p.removeCls('accordionSection-active');
				}
			},
			afterrender : function(panel) {
				/*var header = panel.header;
				header.setHeight(27);*/
			}
		},
		items:[tree]
	});
};

com.trackplus.admin.dropAdminProject=function(nodeFrom, nodeTo, treeStore) {
	var projectID=nodeFrom.data.id;
	var parent=nodeTo.data.id;
	Ext.Ajax.request({
		url: 'project!updateParent.action',
		params: {
			projectID:projectID,
			parentID:parent
		},
		disableCaching:true,
		success: function(response){
		}
	});
};
com.trackplus.admin.clearProjectParent=function(projectID, isTemplate){
	Ext.Ajax.request({
		url: 'project!clearParent.action',
		params: {
			projectID:projectID
		},
		disableCaching:true,
		success: function(response) {
			com.trackplus.admin.refrehProjectTree({projectNodeToSelect:projectID, isTemplate:isTemplate});
			/*treeStore.load({
				callback:function(){
					var tmpTreeID = "tree-projectTreePanel";
					if(isTemplate) {
						tmpTreeID = "tree-projectTemplateTreePanel";
					}
					var tree=Ext.getCmp(tmpTreeID);
					var selectedNode=treeStore.getNodeById(projectID);
					tree.getSelectionModel().select(selectedNode);
					com.trackplus.admin.myClick(tree.getView,selectedNode);
				}
			});*/
		}
	});
};

com.trackplus.admin.refrehProjectTree = function(refreshParamsObject) {
	var projectNodeToSelect = refreshParamsObject["projectNodeToSelect"];
    var projectNodeToReload = refreshParamsObject["projectNodeToReload"];
    var deletedProjectID = refreshParamsObject["deletedProjectID"];
    var isTemplate =  refreshParamsObject["isTemplate"];
    var tmpTreeID = "tree-projectTreePanel";
    var projectTree=Ext.getCmp(tmpTreeID);
    if (projectTree!=null) {
		var options = new Object();
	    if (projectNodeToReload) {
	        var nodeToReload = projectTree.getStore().getNodeById(projectNodeToReload);
	        // if null reload the branch i.e. the main projects
	        options.node = nodeToReload;
	    }
	    if (CWHF.isNull(projectNodeToSelect)) {
	        // after deleting a main project
	        var rootNode = projectTree.getRootNode();
	        if (rootNode  && rootNode.childNodes  && rootNode.childNodes.length > 0) {
	            var firstChild = rootNode.getChildAt(0);
	            if (deletedProjectID  && firstChild.get("id") === deletedProjectID) {
	                if (rootNode.childNodes.length > 1) {
	                    var secondChild = rootNode.getChildAt(1);
	                    projectNodeToSelect = secondChild.get("id");
	                }
	            } else {
	                projectNodeToSelect = firstChild.get("id");
	            }
	        }
	    }
	    // callback after load
	    options.callback = com.trackplus.admin.selectProjectTreeNode;
	    // scope for callback
	    options.scope = {
	        tree: projectTree,
	        nodeIdToReload: projectNodeToReload,
	        nodeIdToSelect: projectNodeToSelect
	        //scope: this
	        //expandWorkspaceNode: false
	    };
	    projectTree.getStore().load(options);
	}
},

com.trackplus.admin.selectProjectTreeNode = function() {
    if (this.nodeIdToReload) {
        nodeReloaded = this.tree.getStore().getNodeById(this.nodeIdToReload);
        if (nodeReloaded && !nodeReloaded.isExpanded()) {
            nodeReloaded.expand();
        }
    }
    if (this.nodeIdToSelect ) {
        var nodeToSelect = this.tree.getStore().getNodeById(this.nodeIdToSelect);
        if (nodeToSelect ) {
            var selectionModel = this.tree.getSelectionModel();
            selectionModel.select(nodeToSelect);
            com.trackplus.admin.myClick(this.tree.getView(), nodeToSelect);
        }
    } else {
        // no node to select -> no project
        borderLayout.controller.setCenterContent(null);

    }
},

com.trackplus.admin.deleteProject=function(projectID,projectName, isTemplate, deleteConfirmed){
	var title=getText('common.lbl.delete',getText('admin.project.lbl.projectForOp'));
	var msg=getText('common.lbl.removeWarning',getText('admin.project.lbl.projectForOp'));
	Ext.MessageBox.confirm(title,msg,
		function(btn){
			if (btn==="no") {
				return false;
			} else {
				Ext.Ajax.request({
					url: 'project!delete.action',
					params: {
						projectID:projectID,
						deleteConfirmed:deleteConfirmed
					},
					disableCaching:true,
					success: function(response){
						var responseJson = Ext.decode(response.responseText);
						if (responseJson.success===true) {
							com.trackplus.admin.refrehProjectTree({deletedProjectID:projectID, isTemplate:isTemplate});
							//window.location.href='admin.action';
						}else{
							var errorMessage = responseJson.errorMessage;
							Ext.MessageBox.confirm(title,
								errorMessage,
								function(btn){
									if (btn==="no") {
										return false;
									} else {
										com.trackplus.admin.deleteProject(projectID, projectName, isTemplate, true);
									}
								}
							);
						}
					}
				});
			}
		}
	);
};
com.trackplus.admin.createCtxMenuProject=function(record, isTemplate){
	var projectID=record.data.id;
	var projectName=record.data.text;
	var items = [{
		text:getText('common.btn.delete'),
		iconCls:'delete16',
		handler:function(){
			com.trackplus.admin.deleteProject(projectID, projectName, isTemplate, false);
		}
	}];
	if (record && record.parentNode && !record.parentNode.isRoot()) {
		items.push({
			text:getText('menu.admin.project.removeFromParent'),
			iconCls:'clear16',
			handler:function(){
				com.trackplus.admin.clearProjectParent(projectID, isTemplate);
			}
		});
	}
	var contextMenu = new Ext.menu.Menu({
		items: items
	});
	return contextMenu;
};

com.trackplus.admin.getMyPreferencesSection=function(){
	var menuPathPrefix=getText('menu.admin')+">"+getText('menu.admin.myProfile')+">";
	var myPreferenceData=[{
			id:'myProfile',script:true,
			cls:'treeItem-level-1',
			text:getText('menu.admin.myProfile.myProfile'),
			tooltip:getText('menu.admin.myProfile.myProfile.tt'),
			menuPath:menuPathPrefix+getText('menu.admin.myProfile.myProfile'),
			url:'com.trackplus.admin.myProfile()',
			useAJAX:true,leaf:true,iconCls:'userprefs-ticon'
		},{
			id:'myAutomail',script:true,
			cls:'treeItem-level-1',
			text:getText('menu.admin.custom.automail'),
			tooltip:getText('menu.admin.custom.automail.tt'),
			menuPath:menuPathPrefix+getText('menu.admin.custom.automail'),
			url:'com.trackplus.admin.notify(false)',
			useAJAX:true,leaf:true,iconCls:'automailc-ticon'
		},{
			id:'iCalendarURL',script:true,
			cls:'treeItem-level-1',
			text:getText('menu.admin.myProfile.iCalendar'),
			tooltip:getText('menu.admin.myProfile.iCalendar.tt'),
			menuPath:menuPathPrefix+getText('menu.admin.myProfile.iCalendar'),
			url:'com.trackplus.admin.iCalendarURL()',
			useAJAX:true,leaf:true,iconCls:'calendar-ticon'
		}
	];
	return com.trackplus.admin.createSectionTree('myPreferenceSection',myPreferenceData,getText('menu.admin.myProfile'),getText('menu.admin.myProfile.tt'),'userprefs-ticon');
};

com.trackplus.admin.getProjectsSection=function(initData, isTemplate) {
	return com.trackplus.admin.createProjectTree(initData, isTemplate);
};

com.trackplus.admin.getUsersSection=function(){
	var menuPathPrefix=getText('menu.admin')+">"+getText('menu.admin.user')+">";
	var str = "tmp";
	var userNavigator=[{
			id:'users',
			script:true,
			cls:'treeItem-level-1',
			text:getText('menu.admin.users.users'),
			tooltip:getText('menu.admin.users.users.tt'),
			menuPath:menuPathPrefix+getText('menu.admin.users.users'),
			url:'com.trackplus.admin.user.person(true)',
			useAJAX:true,leaf:true,iconCls:'user-ticon'
		}];
	if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
		userNavigator.push({
			id:'clients',
			script:true,
			cls:'treeItem-level-1',
			text: getText('menu.admin.users.clients'),
			tooltip:getText('menu.admin.users.clients.tt'),
			menuPath: menuPathPrefix+getText('menu.admin.users.clients'),
			url:'com.trackplus.admin.user.person(false)',
			useAJAX:true,leaf:true,iconCls:'user-ticon'
		});
	}
	userNavigator.push({
		id:'groups',script:true,
		cls:'treeItem-level-1',
		text:getText('menu.admin.users.groups'),
		tooltip:getText('menu.admin.users.groups.tt'),
		menuPath:menuPathPrefix+getText('menu.admin.users.groups'),
		url:'com.trackplus.admin.user.group()',
		useAJAX:true,leaf:true,iconCls:'group-ticon'
	});
	userNavigator.push({
		id:'departments',script:true,
		cls:'treeItem-level-1',
		text:getText('menu.admin.users.departments'),
		tooltip:getText('menu.admin.users.departments.tt'),
		menuPath:menuPathPrefix+getText('menu.admin.users.departments'),
		url:'com.trackplus.admin.user.department()',
		useAJAX:true,leaf:true,iconCls:'department-ticon'
	});
	return com.trackplus.admin.createSectionTree('usersSection',userNavigator,getText('menu.admin.user'),getText('menu.admin.user.tt'),'users-ticon');
};

com.trackplus.admin.getActionSection=function(){
	var menuPathPrefix=getText('menu.admin')+">"+getText('menu.admin.action')+">";
	var actionNavigator=[
		{
			id:'importTrackplus',script:true,
			text:getText('menu.admin.action.importTrackplus'),
			tooltip:getText('menu.admin.action.importTrackplus.tt'),
			menuPath:menuPathPrefix+getText('menu.admin.action.importTrackplus'),
			url:'com.trackplus.admin.action.importTrackplus()',
			useAJAX:true,leaf:true,iconCls:'trackPlus-ticon'
		},{
			id:'broadcastEmail',script:true,
			cls:'treeItem-level-1',
			text:getText('menu.admin.manServer.sendEmail'),
			tooltip:getText('menu.admin.manServer.sendEmail.tt'),
			menuPath:menuPathPrefix+getText('menu.admin.manServer.sendEmail'),
			url:'com.trackplus.admin.sendEmail()',
			useAJAX:false,leaf:true,iconCls:'broadcast-ticon'
		}
	];
	return com.trackplus.admin.createSectionTree('actionSection',actionNavigator,getText('menu.admin.action'),getText('menu.admin.action.tt'),'action-ticon');
};

com.trackplus.admin.getCustomizationSection=function(){
	var menuPathPrefix=getText('menu.admin')+">"+getText('menu.admin.custom')+">";
	var customizationData=[];
	if (com.trackplus.TrackplusConfig.user.manageFilters) {
		customizationData.push({
			id:'queryFilters',script:true,
			cls:'treeItem-level-1',
			text:getText('menu.admin.custom.queryFilters'),
			menuPath:menuPathPrefix+getText('menu.admin.custom.queryFilters'),
			url:"com.trackplus.admin.filterConfig()",
			useAJAX:true,leaf:true,iconCls:'filter-ticon'
		});
	}
	if (com.trackplus.TrackplusConfig.user.reportTemplates) {
		customizationData.push({
			id:'reportTemplates',script:true,
			cls:'treeItem-level-1',
			text:getText('menu.admin.custom.reportTemplates'),
			menuPath:menuPathPrefix+getText('menu.admin.custom.reportTemplates'),
			url:'com.trackplus.admin.reportConfig()',
			useAJAX:true,leaf:true,iconCls:'report-ticon'
		});
	}
	if (com.trackplus.TrackplusConfig.user.sys ||
		(com.trackplus.TrackplusConfig.user.projectAdmin &&
			com.trackplus.TrackplusConfig.user.userRoles)) {
		customizationData.push({
			id:'roles',script:true,
			cls:'treeItem-level-1',
			text:getText('menu.admin.custom.roles'),
			tooltip:getText('menu.admin.custom.roles.tt'),
			menuPath:menuPathPrefix+getText('menu.admin.custom.roles'),
			url:'com.trackplus.admin.refreshRoles()',
			useAJAX:true,leaf:true,iconCls:'roles-ticon'
		});
	}
	if (com.trackplus.TrackplusConfig.user.sys ||
			(com.trackplus.TrackplusConfig.user.projectAdmin &&
				com.trackplus.TrackplusConfig.user.userLevels)) {
		customizationData.push({
			id:'userLevels',script:true,
			cls:'treeItem-level-1',
			text:getText('menu.admin.custom.userLevels'),
			tooltip:getText('menu.admin.custom.userLevels.tt'),
			menuPath:menuPathPrefix+getText('menu.admin.custom.userLevels'),
			url:'com.trackplus.admin.userLevelConfig()',
			useAJAX:true,leaf:true,iconCls:'personSystemAdmin'
		});
	}
	if (com.trackplus.TrackplusConfig.user.sys) {
		if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
			customizationData.push({
				id:'accounts',script:true,
				cls:'treeItem-level-1',
				text:getText('menu.admin.custom.account'),
				tooltip:getText('menu.item.account.tt'),
				menuPath:menuPathPrefix+getText('menu.admin.custom.account'),
				url:'com.trackplus.admin.accountConfig()',
				useAJAX:true,leaf:true,iconCls:'account-ticon'
			});
		}
		customizationData.push({
			id:'defaultAutomail',script:true,
			cls:'treeItem-level-1',
			text:getText('menu.admin.custom.automail'),
			tooltip:getText('menu.admin.custom.automail.tt'),
			menuPath:menuPathPrefix+getText('menu.admin.custom.automail'),
			url:'com.trackplus.admin.notify(true)',
			useAJAX:true,leaf:true,iconCls:'automailc-ticon'
		});
	}
	if (com.trackplus.TrackplusConfig.user.sys) {
		if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
			customizationData.push({
				id:'linkTypes',script:true,
				cls:'treeItem-level-1',
				text:getText('menu.admin.custom.linkType'),
				tooltip:getText('menu.admin.custom.linkType'),
				menuPath:menuPathPrefix+getText('menu.admin.custom.linkType'),
				url:'com.trackplus.admin.refreshLinkTypes()',
				useAJAX:true,leaf:true,iconCls:'links-ticon'
			});
		}
	}
	if (com.trackplus.TrackplusConfig.user.sys ||
		(com.trackplus.TrackplusConfig.user.projectAdmin &&
			com.trackplus.TrackplusConfig.user.forms)) {
		customizationData.push({
			id:'customForms',script:true,
			cls:'treeItem-level-1',
			text:getText('menu.admin.custom.customForms'),
			tooltip:getText('menu.admin.custom.customForms'),
			menuPath:menuPathPrefix+getText('menu.admin.custom.customForms'),
			url:'com.trackplus.admin.screenConfig()',
			useAJAX:true,leaf:true,iconCls:'forms-ticon'
		});
	}
	if (com.trackplus.TrackplusConfig.user.sys ||
			(com.trackplus.TrackplusConfig.user.projectAdmin &&
				com.trackplus.TrackplusConfig.user.fields)) {
		customizationData.push({
			id:'customFields',script:true,
			cls:'treeItem-level-1',
			text:getText('menu.admin.custom.customField'),
			tooltip:getText('menu.admin.custom.customField.tt'),
			menuPath:menuPathPrefix+getText('menu.admin.custom.customField'),
			url:'com.trackplus.admin.fieldConfig()',
			useAJAX:true,leaf:true,iconCls:'fields-ticon'
		});
	}
	if (com.trackplus.TrackplusConfig.user.sys ||
			(com.trackplus.TrackplusConfig.user.projectAdmin &&
				com.trackplus.TrackplusConfig.user.lists)) {
		customizationData.push({
			id:'pickLists',script:true,
			cls:'treeItem-level-1',
			text:getText('menu.admin.custom.list'),
			tooltip:getText('menu.admin.custom.list.tt'),
			menuPath:menuPathPrefix+getText('menu.admin.custom.list'),
			url:'com.trackplus.admin.listConfig()',
			useAJAX:true,leaf:true,iconCls:'picklists-ticon'
		});
	}
	if (com.trackplus.TrackplusConfig.user.sys) {
		customizationData.push({
				id:'objectStatus', script:true,
				cls:'treeItem-level-1',
				text:getText('menu.admin.custom.objectStatus'),
				tooltip:getText('menu.admin.custom.objectStatus.tt'),
				menuPath:menuPathPrefix+getText('menu.admin.custom.objectStatus'),
				url:'com.trackplus.admin.objectStatusConfig()',
				useAJAX:true,leaf:true,iconCls:'objectStatus-ticon'
			});
		customizationData.push({
			id:'projectTypes',script:true,
			cls:'treeItem-level-1',
			text:getText('menu.admin.custom.projectType'),
			tooltip:getText('menu.admin.custom.projectType.tt'),
			menuPath:menuPathPrefix+getText('menu.admin.custom.projectType'),
			url:'com.trackplus.admin.projectTypes()',
			useAJAX:true,leaf:true,iconCls:'projecttypes-ticon'
		});
	}
	if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
		if (com.trackplus.TrackplusConfig.user.sys ||
				(com.trackplus.TrackplusConfig.user.projectAdmin &&
				com.trackplus.TrackplusConfig.user.workflows)) {
			customizationData.push({
				id:'workflows',script:true,
				text:getText('menu.admin.custom.workflow'),
				tooltip:getText('menu.admin.custom.workflow.tt'),
				menuPath:menuPathPrefix+getText('menu.admin.custom.workflow'),
				url:'com.trackplus.admin.workflowConfig()',
				useAJAX:false,leaf:true,iconCls:'workflow-ticon'
			});
		}
	}
	if (com.trackplus.TrackplusConfig.user.sys) {
		if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
			customizationData.push({
				id:'scripts',script:true,
				cls:'treeItem-level-1',
				text:getText('menu.admin.custom.script'),
				tooltip:getText('menu.admin.custom.script.tt'),
				menuPath:menuPathPrefix+getText('menu.admin.custom.script'),
				url:'com.trackplus.admin.refreshScript()',
				useAJAX:true,leaf:true,iconCls:'scripts-ticon'
			});
			customizationData.push({
				id:'localization',script:true,
				cls:'treeItem-level-1',
				text:getText('menu.admin.custom.localeEditor'),
				tooltip:getText('menu.admin.custom.localeEditor.tt'),
				menuPath:menuPathPrefix+getText('menu.admin.custom.localeEditor'),
				url:'com.trackplus.admin.localeEditor()',
				useAJAX:true,leaf:true,iconCls:'localize-ticon'
			});
		}

		if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
			customizationData.push({
				id:'mailTemplates',script:true,
				cls:'treeItem-level-1',
				text:getText('menu.admin.custom.mailTemplate'),
				tooltip:getText('menu.admin.custom.mailTemplate.tt'),
				menuPath:menuPathPrefix+getText('menu.admin.custom.mailTemplate'),
				url:'com.trackplus.admin.mailTemplateConfig()',
				useAJAX:true,leaf:true,iconCls:'mailTemplate-ticon'
			});

			customizationData.push({
				id:'dashboardAssign',script:true,
				cls:'treeItem-level-1',
				text:getText('menu.admin.users.cockpitDefault'),
				tooltip:getText('menu.admin.users.cockpitDefault.tt'),
				menuPath:menuPathPrefix+getText('menu.admin.users.cockpitDefault'),
				url:'com.trackplus.admin.user.dashboardAssign()',
				useAJAX:true,leaf:true,iconCls:'cockpit-ticon'
			});
		}
	}
	if (customizationData.length>0) {
		return com.trackplus.admin.createSectionTree('customizationSection', customizationData, getText('menu.admin.custom'), getText('menu.admin.custom.tt'),'customize-ticon');
	} else {
		return null;
	}
};

com.trackplus.admin.getServerSection=function(){
	var menuPathPrefix=getText('menu.admin')+">"+getText('menu.admin.manServer')+">";
	var serverData=[{
			id:'serverConfiguration',script:true,
			cls:'treeItem-level-1',
			text:getText('menu.admin.manServer.serverConfig'),
			tooltip:getText('menu.admin.manServer.serverConfig.tt'),
			menuPath:menuPathPrefix+getText('menu.admin.manServer.serverConfig'),
			url:'com.trackplus.admin.serverConfiguration()',
			useAJAX:true,leaf:true,iconCls:'server-ticon'
		},{
			id:'logonPageText',script:true,
			cls:'treeItem-level-1',
			text:getText('menu.admin.manServer.logonText'),
			tooltip:getText('menu.admin.manServer.logonText.tt'),
			menuPath:menuPathPrefix+getText('menu.admin.manServer.logonText'),
			url:'com.trackplus.admin.logonPageText()',
			useAJAX:false,leaf:true,iconCls:'motd-ticon'
		},{
			id:'serverStatus',script:true,
			cls:'treeItem-level-1',
			text:getText('menu.admin.manServer.serverStatus'),
			tooltip:getText('menu.admin.manServer.serverStatus.tt'),
			menuPath:menuPathPrefix+getText('menu.admin.manServer.serverStatus'),
			url:'com.trackplus.admin.serverStatus()',
			useAJAX:false,leaf:true,iconCls:'serverStatus-ticon'
		},{
			id:'loggingConfiguration',script:true,
			cls:'treeItem-level-1',
			text:getText('menu.admin.manServer.loggingConfig'),
			tooltip:getText('menu.admin.manServer.loggingConfig.tt'),
			menuPath:menuPathPrefix+getText('menu.admin.manServer.loggingConfig'),
			url:'com.trackplus.admin.loggingConfiguration()',
			useAJAX:false,leaf:true,iconCls:'logging-ticon'
		},{
			id:'dataBackup',script:true,
			cls:'treeItem-level-1',
			text:getText('menu.admin.manServer.databaseBackup'),
			tooltip:getText('menu.admin.manServer.databaseBackup.tt'),
			menuPath:menuPathPrefix+getText('menu.admin.manServer.databaseBackup'),
			url:'com.trackplus.admin.databaseBackup()',
			useAJAX:false,leaf:true,iconCls:'backup-ticon'
		},{
			id:'dataRestore',script:true,
			cls:'treeItem-level-1',
			text:getText('menu.admin.manServer.databaseRestore'),
			tooltip:getText('menu.admin.manServer.databaseRestore.tt'),
			menuPath:menuPathPrefix+getText('menu.admin.manServer.databaseRestore'),
			url:'com.trackplus.admin.databaseRestore()',
			useAJAX:false,leaf:true,iconCls:'restore-ticon'
		}
	];
	return com.trackplus.admin.createSectionTree('serverSection',serverData,getText('menu.admin.manServer'),getText('menu.admin.manServer.tt'),'server-ticon');
};

com.trackplus.admin.getNodeJsonData=function(sectionID,nodeID){
	var sectionNodes=this.sections[sectionID];
	if(sectionNodes){
		for(var i=0;i<sectionNodes.length;i++){
			if(sectionNodes[i].id===nodeID){
				return sectionNodes[i];
			}
		}
	}
	return null;
};

com.trackplus.admin.myProfile=function(){
	var myProfile=Ext.create('com.trackplus.admin.user.Profile',{context:1});
	borderLayout.setActiveToolbarList([myProfile.createSaveButton()]);
	var component=myProfile.loadProfile();
	borderLayout.setCenterContent(component);
};

/**
 * Called not from here, but from dynamic projects json
 */
com.trackplus.admin.projectConfig=function(projectID, isTemplate) {
	borderLayout.setLoading(true);
	/*var sectionKey = 'projects';
	if(isTemplate) {
		sectionKey = 'projectsTemplate';
	}*/
	Ext.Ajax.request({
		fromCenterPanel:true,
		url: 'project!loadConfig.action',
		params: {projectID:projectID},
		disableCaching:true,
		scope: this,
		success: function(response) {
			borderLayout.setLoading(false);
			var responseJson = Ext.decode(response.responseText);
			var projectConfig=Ext.create('com.trackplus.admin.project.ProjectConfig',{
				rootID:projectID,
				lastSelections:responseJson.lastSelections,
				hasPrivateProject: responseJson.hasPrivateProject,
				mainRelease: responseJson.mainRelease,
				childRelease: responseJson.childRelease,
				showClosedReleases: responseJson.showClosedReleases,
				isTemplate:isTemplate,
				templateIsActive: responseJson.templateIsActive
			});
			borderLayout.borderLayoutController.setCenterContent(projectConfig);
		},
		failure: function(response){
			borderLayout.setLoading(false);
			var projectConfig=Ext.create('com.trackplus.admin.project.ProjectConfig',{
				rootID:projectID,
				lastSelections:{},
				isTemplate:isTemplate,
				templateIsActive: responseJson.templateIsActive
			});
			borderLayout.borderLayoutController.setCenterContent(projectConfig);
		}
	});
};

com.trackplus.admin.user.person=function(isUser){
	//var gridConfig=Ext.create("com.trackplus.admin.user.Person",{isUser: isUser});
	//com.trackplus.admin.replaceGridConfig(gridConfig);
	Ext.Ajax.request({
		url: "person!getUserConfigs.action",
		scope: this,
		success: function(response) {
			var result = Ext.decode(response.responseText);
			var userLevels = result.userLevelList;
			var featureList = result.featureList;
			var gridConfig=Ext.create("com.trackplus.admin.user.Person", {isUser: isUser, userLevels:userLevels, featureList:featureList});
			com.trackplus.admin.replaceGridConfig(gridConfig);
		},
		failure: function(response) {
			Ext.MessageBox.alert(this.failureTitle, response.responseText);
		}
	});

};

com.trackplus.admin.user.group=function(){
	var treeWithGridConfig=Ext.create('com.trackplus.admin.user.Group');
	com.trackplus.admin.replaceTreeWithGridConfig(treeWithGridConfig);
};

com.trackplus.admin.user.department=function(){
	var treeWithGridConfig=Ext.create('com.trackplus.admin.user.Department'/*, {rootID:'_', reloadGrids:true}*/);
	com.trackplus.admin.replaceTreeWithGridConfig(treeWithGridConfig);
};
com.trackplus.admin.user.dashboardAssign=function(){
	var treeWithGridConfig=Ext.create('com.trackplus.admin.user.DashboardAssignment', {rootID:'_'});
	com.trackplus.admin.replaceTreeWithGridConfig(treeWithGridConfig);
};

/*com.trackplus.admin.action.exportForm=function() {
	var exportForms=Ext.create('com.trackplus.admin.action.ExportForm',{});
	borderLayout.borderLayoutController.setActiveToolbarList([exportForms.createExportButton()]);
	//var component=exportForms.createMainComponent();
	borderLayout.borderLayoutController.setCenterContent(null);
};*/

com.trackplus.admin.action.importExcel=function() {
	var importExcel=Ext.create('com.trackplus.admin.action.ImportExcel');
	com.trackplus.admin.replaceWizardConfig(importExcel);
};

com.trackplus.admin.action.importDocx=function() {
	var importDocx=Ext.create('com.trackplus.admin.action.ImportDocx');
	com.trackplus.admin.replaceWizardConfig(importDocx);
};


com.trackplus.admin.action.importMsProject=function() {
	var importMsProject=Ext.create('com.trackplus.admin.action.ImportMsProject');
	com.trackplus.admin.replaceWizardConfig(importMsProject);
};

com.trackplus.admin.action.importTrackplus=function() {
	var importTrackplus=Ext.create('com.trackplus.admin.action.ImportTrackplus');
	//com.trackplus.admin.replaceWizardConfig(importTrackplus);
	borderLayout.borderLayoutController.setActiveToolbarList(importTrackplus.createToolbar());
	var component=importTrackplus.createMainComponent();
	borderLayout.borderLayoutController.setCenterContent(component);
	//importTrackplus.renderImport();

};

/*com.trackplus.admin.action.importForm=function() {
	var importForms=Ext.create('com.trackplus.admin.action.ImportForm',{});
	borderLayout.borderLayoutController.setActiveToolbarList([importForms.createImportButton()]);
	var component=importForms.createMainComponent();
	borderLayout.borderLayoutController.setCenterContent(component);
};*/

com.trackplus.admin.serverConfiguration=function(){
	var siteConfig=Ext.create('com.trackplus.admin.server.SiteConfig',{});
	borderLayout.borderLayoutController.setActiveToolbarList([siteConfig.createSaveButton()]);
	var component=siteConfig.createMainComponent();
	borderLayout.borderLayoutController.setCenterContent(component);
	siteConfig.loadMyForm.call(siteConfig);
};

com.trackplus.admin.logonPageText=function(){
	var logonPage=Ext.create('com.trackplus.admin.server.LogonPageText',{});
	borderLayout.borderLayoutController.setActiveToolbarList([logonPage.createSaveButton()]);
	var component=logonPage.createMainComponent();
	logonPage.reload();
	borderLayout.borderLayoutController.setCenterContent(component);
};
com.trackplus.admin.sendEmail=function(){
	var sendEmail=Ext.create('com.trackplus.admin.server.SendEmail',{});
	borderLayout.borderLayoutController.setActiveToolbarList(sendEmail.getToolbar());
	var component=sendEmail.createMainComponent();
	borderLayout.borderLayoutController.setCenterContent(component);
};

com.trackplus.admin.serverStatus=function(){
	var serverStatus=Ext.create('com.trackplus.admin.server.ServerStatus',{});
	borderLayout.borderLayoutController.setActiveToolbarList(serverStatus.createToolbar());
	serverStatus.reloadAndReplaceComp();
};

com.trackplus.admin.loggingConfiguration=function(){
	var loggingConfig=Ext.create('com.trackplus.admin.server.LoggingConfig',{});
	borderLayout.borderLayoutController.setActiveToolbarList([]);
	var component=loggingConfig.createMainComponent();
	loggingConfig.reload();
	borderLayout.borderLayoutController.setCenterContent(component);
};
com.trackplus.admin.databaseBackup=function(){
	var databaseBackup=Ext.create('com.trackplus.admin.server.DatabaseBackup',{});
	borderLayout.borderLayoutController.setActiveToolbarList(databaseBackup.createToolbar());
	var component=databaseBackup.createMainComponent();
	borderLayout.borderLayoutController.setCenterContent(component);
};

com.trackplus.admin.databaseRestore=function(){
	var databaseRestore=Ext.create('com.trackplus.admin.server.DatabaseRestore',{});
	borderLayout.borderLayoutController.setActiveToolbarList(databaseRestore.createToolbar());
	var component=databaseRestore.createMainComponent();
	databaseRestore.reload();
	borderLayout.borderLayoutController.setCenterContent(component);
};

com.trackplus.admin.refreshLinkTypes=function(){
	var gridConfig=Ext.create('com.trackplus.admin.customize.linkType.LinkType',{});
	com.trackplus.admin.replaceGridConfig(gridConfig);
};

com.trackplus.admin.projectTypes=function(){
	var treeWithGridConfig=Ext.create('com.trackplus.admin.customize.projectType.ProjectType');
	com.trackplus.admin.replaceTreeWithGridConfig(treeWithGridConfig);
};

com.trackplus.admin.refreshScript=function(){
	var gridConfig=Ext.create('com.trackplus.admin.customize.script.Script',{});
	com.trackplus.admin.replaceGridConfig(gridConfig);
};

com.trackplus.admin.workflowConfig=function(){
	var workflowConfig=Ext.create("com.trackplus.admin.customize.treeConfig.WorkflowConfig");
	com.trackplus.admin.replaceTreeWithGridConfig(workflowConfig);
};

com.trackplus.admin.refreshRoles=function(){
	var gridConfig=Ext.create('com.trackplus.admin.customize.role.Role',{});
	com.trackplus.admin.replaceGridConfig(gridConfig);
};
com.trackplus.admin.accountConfig=function() {
	var treeWithGridConfig=Ext.create('com.trackplus.admin.customize.account.AccountConfig',{rootID:"_false"});
	com.trackplus.admin.replaceTreeWithGridConfig(treeWithGridConfig);
};

com.trackplus.admin.replaceTreeWithGridConfig=function(treeWithGridConfig){
	borderLayout.borderLayoutController.setCenterContent(treeWithGridConfig/*.createCenterPanel()*/);
	//borderLayout.borderLayoutController.setActiveToolbarActionList(treeWithGridConfig.getToolbarActions());
};

com.trackplus.admin.notify=function(defaultSettings){
	var treeWithGridConfig=Ext.create('com.trackplus.admin.NotifyConfig',{defaultSettings:defaultSettings});
	com.trackplus.admin.replaceTreeWithGridConfig(treeWithGridConfig);
};
com.trackplus.admin.iCalendarURL=function(){
	var iCalendar=Ext.create('com.trackplus.admin.ICalendarURL',{data:{}});
	borderLayout.setActiveToolbarList(iCalendar.getToolbarButtons.call(iCalendar));
	var component=iCalendar.createView.call(iCalendar);
	borderLayout.setCenterContent(component);
};

com.trackplus.admin.fieldConfig=function(){
	var fieldConfig=Ext.create("com.trackplus.admin.customize.treeConfig.FieldConfig");
	com.trackplus.admin.replaceTreeWithGridConfig(fieldConfig);
};

com.trackplus.admin.screenConfig=function(){
	var screenConfig=Ext.create("com.trackplus.admin.customize.treeConfig.ScreenConfig");
	com.trackplus.admin.replaceTreeWithGridConfig(screenConfig);
};

com.trackplus.admin.mailTemplateConfig=function(){
	var mailTemplateConfig=Ext.create("com.trackplus.admin.customize.treeConfig.MailTemplateConfig");
	com.trackplus.admin.replaceTreeWithGridConfig(mailTemplateConfig);
};

com.trackplus.admin.localeEditor=function(){
	var treeWithGridConfig=Ext.create("com.trackplus.admin.customize.locale.LocaleEditor");
//	treeWithGridConfig.initLocaleEditor();
	com.trackplus.admin.replaceTreeWithGridConfig(treeWithGridConfig);
};

com.trackplus.admin.userLevelConfig=function() {
	var treeWithGridConfig=Ext.create("com.trackplus.admin.customize.userLevel.UserLevel");
	com.trackplus.admin.replaceTreeWithGridConfig(treeWithGridConfig);
};

com.trackplus.admin.listConfig= function() {
	var treeWithGridConfig=Ext.create("com.trackplus.admin.customize.list.ListConfig");
	com.trackplus.admin.replaceTreeWithGridConfig(treeWithGridConfig);
};

com.trackplus.admin.objectStatusConfig= function() {
	var treeWithGridConfig=Ext.create('com.trackplus.admin.customize.objectStatus.ObjectStatus');
	com.trackplus.admin.replaceTreeWithGridConfig(treeWithGridConfig);
};

com.trackplus.admin.filterConfig=function() {
	var treeWithGridConfig=Ext.create("com.trackplus.admin.customize.filter.FilterConfig", {rootID:"issueFilter"});
	com.trackplus.admin.replaceTreeWithGridConfig(treeWithGridConfig);
};

com.trackplus.admin.reportConfig=function(rootNode) {
	var treeWithGridConfig=Ext.create("com.trackplus.admin.customize.report.ReportConfig", {rootID:"report"});
	com.trackplus.admin.replaceTreeWithGridConfig(treeWithGridConfig);
};

com.trackplus.admin.replaceGridConfig=function(gridConfig){
	var grid=gridConfig;//.getGrid();
	borderLayout.borderLayoutController.setCenterContent(grid);
	//gridConfig.reload();
	//borderLayout.borderLayoutController.setActiveToolbarActionList(gridConfig.getToolbarActions());
};

com.trackplus.admin.replaceWizardConfig= function(wizardConfig){
	var wizardPanel=wizardConfig.getWizardPanel();
	borderLayout.borderLayoutController.setCenterContent(wizardPanel);
	borderLayout.borderLayoutController.setActiveToolbarActionList(wizardConfig.getToolbarActions());
};

Ext.define('com.trackplus.layout.AdminLayout',{
	extend:'com.trackplus.layout.BaseLayout',
	useToolbar:true,
	sectionSelected:null,
	selectedNodeID:null,
	selectedGroup:'admin',
	constructor : function(config) {
		var me = this;
		me.callParent(arguments);
		/*me.reportsConfig=Ext.create('com.trackplus.admin.customize.ReportConfig',{
			rootID:'report'
		});*/

		var sysAdmin=com.trackplus.TrackplusConfig.user.sysAdmin;
		var data=me.initData;
		if(sysAdmin){
			me.sectionSelected='serverSection';
			me.selectedNodeID="serverConfiguration";
		}else{
			me.sectionSelected='myPreferenceSection';
			me.selectedNodeID='myProfile';
		}
		if(data.sectionSelected){
			me.sectionSelected=data.sectionSelected;
		}

		if(data.selectedNodeID){
			me.selectedNodeID=data.selectedNodeID;
		}
		me.onReady(function(){

			if(me.sectionSelected){
				me.setSelectedContext(me.sectionSelected,me.selectedNodeID);
				/*Ext.getCmp(me.sectionSelected).expand();
				var treeCmp=Ext.getCmp('tree-'+me.sectionSelected);
				if(treeCmp){
					var node=treeCmp.getStore().getNodeById(me.selectedNodeID);
					if(node){
						treeCmp.getSelectionModel().select(node);
						treeCmp.expandPath(node.getPath());
						var jsonData=node.data;
						com.trackplus.admin.replaceCenterContent(me.sectionSelected,me.selectedNodeID,jsonData);
					}
				}*/
			}
		});
	},
	createWestPanel:function(){
		return com.trackplus.admin.createAdminWestPanel(this.initData);
	},
	historyChange:function(token){
		var me=this;
		var parts = token.split(":");
		var section=parts[0];
		var nodeID=parts[1];
		me.setSelectedContext(section,nodeID);
	},
	setSelectedContext:function(sectionSelected,selectedNodeID){
		var me=this;
		if(com.trackplus.admin.lastSelectedSection===sectionSelected&&com.trackplus.admin.lastSelectedNode===selectedNodeID){
			return true;
		}
		me.sectionSelected=sectionSelected;
		me.selectedNodeID=selectedNodeID;
		if(me.sectionSelected){
			Ext.getCmp(me.sectionSelected).expand();
			var treeCmp=Ext.getCmp('tree-'+me.sectionSelected);
			if(treeCmp){
				var node=treeCmp.getStore().getNodeById(me.selectedNodeID);
				if(node){
					treeCmp.getSelectionModel().select(node);
					treeCmp.expandPath(node.getPath());
					var jsonData=node.data;
					com.trackplus.admin.replaceCenterContent(me.sectionSelected,me.selectedNodeID,jsonData);
				}
			}
		}
	}
});
