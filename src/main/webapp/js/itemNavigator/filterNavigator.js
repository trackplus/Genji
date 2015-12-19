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


//View
Ext.define('com.trackplus.itemNavigator.FilterView',{
	extend:'Ext.tree.Panel',
	useArrows: true,
	autoScroll: true,
	rootVisible: false,
	border: false,
	cls:'westTreeNavigator',
	bodyBorder:false,
	//margins:Ext.isIE?'0 -4 0 0':'0 -5 0 0',
	width:200,
	frame: false,
	//collapsible:false,
	//title:'my title here',
	//iconCls:'queryView',
	//collapseMode:'mini',
	//collapsedCls:'westTreeNavigatorCollapsed',
	//split:true,
	listeners:{
		'staterestore':{
			fn:function(panel,state){
				var w=state.width;
				var appWidth=borderLayout.getWidth();
				if(w>appWidth+100){
					w=appWidth-100;
					if(w<0){
						w=50
					}
					panel.width=w;
				}
			}
		}
	},
	config: {
		model:null,
		filterController:null,
		queryContextID:null
	},
	initComponent: function(){
		var me=this;
		//me.title=" ";
		me.store = Ext.create('Ext.data.TreeStore', {
			fields: ['id','text','objectID','canDrop','verifyAllowDropAJAX','nodeType',
				'useFilter','filterViewID', 'maySaveFilterLayout','iconCls','icon','dropHandlerCls','cls'],
			root: {
				expanded: true,
				text:"",
				user:"",
				status:"",
				children:me.model.children
			},
			proxy:{
				type: 'ajax',
				url: 'itemNavigator!expandNode.action'
			}
		});
		me.callParent();
		me.initMyListeners();
	},
	initMyListeners:function(){
		var me=this;
		//me.addListener('beforecollapse',me.beforeCollapseWestNavigator,me);
		//me.addListener('beforeexpand',me.beforeExpandWestNavigator,me);
		me.view.addListener('afterrender',me.onTreeViewAfterRenderer,me);
		me.view.addListener('itemcontextmenu',me.onTreeNodeCtxMenu,me);
	},
	beforeCollapseWestNavigator:function(){
		var me=this;
		me.lastIconCls=me.iconCls;
		me.setIconCls(null);
	},
	beforeExpandWestNavigator:function(){
		var me=this;
		me.setIconCls(me.lastIconCls);
	},
	onTreeViewAfterRenderer:function(){
		var me=this;
		me.store.getRootNode().expandChildren();
		me.dropZone = Ext.create('com.trackplus.util.TreeDropZone', {
			view: me.view,
			ddGroup: "itemToCategory"
		});
	},
	/**
	 * Show the context menu
	 */
	onTreeNodeCtxMenu: function(tree, record, item, index, evtObj) {
		var me=this;
		evtObj.stopEvent();
		me.createCtxMenu(tree, record,evtObj);
		return false;
	},

	createCtxMenu: function(tree, record,evtObj) {
		var me=this;
		if (record) {
			var nodeType = record.data["nodeType"];
			if (nodeType) {
				switch (nodeType){
					case "2_8": {
						//project or release
						me.createProjectReleaseCtxMenu(tree, record,evtObj);
						break;
					}
					case "2_5":{
						//subscribed filter
						me.createFilterCtxMenu(tree, record, evtObj);
						/*var items = [];
						items.push({
							text:getText('common.btn.remove'),
							iconCls:'delete16',
							scope: this,
							handler:function(){
								this.removeFilter(record, tree);
							}
						});
						var contextMenu = new Ext.menu.Menu({
							items: items
						});
						contextMenu.showAt(evtObj.getXY());*/
						break;
					}
					case "2_7":{
						var entityID=record.data["objectID"];
						if(entityID===6){
							//trash
							var items = [];
							items.push({
								text:getText('common.btn.clear'),
								iconCls:'clear16',
								scope: this,
								handler:function(){
									this.clearTrashBasket(record, tree);
								}
							});
							var contextMenu = new Ext.menu.Menu({
								items: items
							});
							contextMenu.showAt(evtObj.getXY());
						}
						break;
					}
				}
			}
		}
		return contextMenu;
	},
	clearTrashBasket:function(){
		var me=this;
		var urlStr="itemNavigator!emptyTrashBasket.action";
		borderLayout.setLoading(true);
		Ext.Ajax.request({
			url: urlStr,
			disableCaching:true,
			success: function(response){
				borderLayout.setLoading(false);
				me.filterController.itemNavController.refresh.call(me.filterController.itemNavController,null,null,false);
			},
			failure:function(){
				borderLayout.setLoading(false);
			}
		});
	},
	createProjectReleaseCtxMenu:function(tree, record,evtObj) {
		var me=this;
		var entityID=record.data["objectID"];
		tree.setLoading(true);
		var urlStr="project!getProjectReleaseCtxMenu.action";
		Ext.Ajax.request({
			url: urlStr,
			params: {
				entityID:entityID
			},
			disableCaching:true,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				var issueTypes=responseJson.data.issueTypes;
				var projectID=responseJson.data.projectID;
				var isProjectAdmin = responseJson.data.isProjectAdmin;
				var mightAddSubproject = responseJson.data.mightAddSubproject;
				var mightHaveRelease = responseJson.data.mightHaveRelease;
				var items = [];
				if (isProjectAdmin) {
					if (entityID<0) {
						//is a project
						if (mightAddSubproject) {
							var addSubprojectTitle = getText("common.lbl.add",getText("admin.project.lbl.subproject"));
							items.push({
								text: addSubprojectTitle,
								iconCls: "projectAdd",
								scope: this,
								handler: function(){
									me.addSubproject(projectID, addSubprojectTitle);
								}
							});
						}
						var editProjectTitle = getText("common.lbl.edit",getText("admin.project.lbl.project"));
						items.push({
							text: editProjectTitle,
							iconCls: "projects-ticon",
							scope: this,
							handler: function(){
								me.editProject(projectID, editProjectTitle);
							}
						});
						var assignRolesTitle = getText("common.lbl.assign",getText("admin.customize.role.lbl.roles"));
						items.push({
							text: assignRolesTitle,
							iconCls: "roles-ticon",
							scope: this,
							handler: function(){
								me.assignRoles(projectID, assignRolesTitle);
							}
						});
					}
					var releaseID=null;
					if (mightHaveRelease) {
						var releaseLbl=getText('admin.project.release.lbl.main');
						items.push({
							text:getText('common.lbl.add',releaseLbl),
							iconCls:'releaseAdd',
							scope: this,
							handler:function(){
								me.addRelease(projectID,releaseID);
							}
						});
					}
					if (entityID>0) {
						//is release
						releaseID=entityID;
						items.push({
							text:getText('common.lbl.add',getText('admin.project.release.childPhase.general')),
							iconCls:'releaseAdd',
							scope: this,
							handler:function(){
								me.addChildRelease(projectID, releaseID);
							}
						});
						items.push({
							text:getText('common.lbl.edit',releaseLbl),
							iconCls:'releaseEdit',
							scope: this,
							handler:function(){
								me.editRelease(projectID, releaseID);
							}
						});
						items.push({
							text:getText('common.lbl.delete',releaseLbl),
							iconCls:'delete',
							scope: this,
							handler:function(){
								me.deleteRelease(projectID, releaseID);
							}
						});
					}
					items.push('-');
				}
				if (mightHaveRelease) {
					items.push({
						text:getText('common.btn.releaseNotes'),
						iconCls:'releaseNotes16',
						scope: this,
						handler:function(){
							me.releaseNotes(record, tree);
						}
					});
				}
				if(issueTypes&&issueTypes.length>0){
					items.push('-');
					for(var i=0;i<issueTypes.length;i++){
						var issueType=issueTypes[i];
						items.push({
							text:getText('common.lbl.add',issueType.label),
							icon:'optionIconStream.action?fieldID=-2&optionID='+issueType.id,
							scope:{
								issueTypeID:issueType.id,
								projectID:projectID,
								releaseID:entityID>0?entityID:null
							},
							handler:function(btn){
								borderLayout.borderLayoutController.createNewIssue.call(borderLayout.borderLayoutController,this.issueTypeID,this.projectID,this.releaseID);
							}
						});
					}
				}
				if (items.length>0) {
					var contextMenu = new Ext.menu.Menu({
						items: items
					});
					contextMenu.showAt(evtObj.getXY());
				}
				tree.setLoading(false);
			},
			failure:function(){
				tree.setLoading(false);
			}
		});
	},

	addSubproject:function(projectID, title) {
		var projectAdd = Ext.create("com.trackplus.admin.project.ProjectEdit", 
				{callerScope: this,
				entityLabel: title,
				refreshAfterSubmitHandler: this.reloadAll,
				entityContext: {isTemplate:false, addProject:true, addAsSubproject: true, projectID: projectID, inDialog:true}});
		var windowConfig = Ext.create("Ext.window.Window", {
			title: title,
			width: 800,
			height: 650,
			items: [projectAdd],
			layout: 'fit',
			plain: true,
			modal: true,
			cls:'windowConfig bottomButtonsDialog tpspecial',
			bodyBorder:false,
			margin:'0 0 0 0',
			style:{
				padding:' 5px 0px 0px 0px'
			},
			bodyPadding:'0px'
		});
		windowConfig.show();
		/*var me=this;
		var loadParams={
			add:true,
			addAsSubproject:true,
			projectID:projectID
		};
		var submitParams=loadParams;
		var projectConfig=Ext.create("com.trackplus.admin.project.ProjectConfig",{});
		projectConfig.addOrEditProject(loadParams,submitParams, title, me, me.reloadAll);*/
	},
	
	editProject:function(projectID, title) {
		var projectEdit = Ext.create("com.trackplus.admin.project.ProjectEdit", 
				{callerScope: this,
				entityLabel: title,
				refreshAfterSubmitHandler: this.reloadAll,
				entityContext: {isTemplate:false, addProject:false, addAsSubproject: true, projectID: projectID, inDialog:true}});
		var windowConfig = Ext.create("Ext.window.Window", {
			title: title,
			width: 800,
			height: 650,
			items: [projectEdit],
			layout: 'fit',
			plain: true,
			modal: true,
			cls:'windowConfig bottomButtonsDialog tpspecial',
			bodyBorder:false,
			margin:'0 0 0 0',
			style:{
				padding:' 5px 0px 0px 0px'
			},
			bodyPadding:'0px'
		});
		windowConfig.show();
		/*var me=this;
		var loadParams={
			add:false,
			projectID:projectID
		};
		var submitParams=loadParams;
		var projectConfig=Ext.create("com.trackplus.admin.project.ProjectConfig",{rootID:projectID});
		projectConfig.addOrEditProject(loadParams,submitParams, title, me, me.reloadAll);*/
	},

	assignRoles:function(projectID, title) {
		var roleAssignment = Ext.create("com.trackplus.admin.project.RoleAssignment", {
            rootID : projectID
        });
		var width = 1200;
		var height = 800;
		var windowParameters = {title:title,
			width:width,
			height:height,
			formPanel:roleAssignment.createCenterPanel(),
			cancelButtonText : getText("common.btn.done")
		};
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
		windowConfig.showWindowByConfig(this);

	},

	addRelease:function(projectID, releaseID){
		var me=this;
		var node=""+projectID;
		if(releaseID){
			node=node+"_"+releaseID;
		}
		var loadParams={
			add:true,
			addAsChild:false,
			node:node
		};
		var submitParams=loadParams;
		var releaseLbl=getText('admin.project.release.lbl.main');
		me.addOrEditRelease(loadParams,submitParams,getText("common.lbl.add",releaseLbl));
	},
	addChildRelease:function(projectID, releaseID){
		var me=this;
		var node=""+projectID;
		if(releaseID){
			node=node+"_"+releaseID;
		}
		var loadParams={
			add:true,
			addAsChild:true,
			node:node
		};
		var submitParams=loadParams;
		var releaseLbl=getText('admin.project.release.lbl.main');
		me.addOrEditRelease(loadParams,submitParams,getText("common.lbl.add",releaseLbl));
	},
	editRelease:function(projectID, releaseID){
		var me=this;
		var node=""+projectID;
		if(releaseID){
			node=node+"_"+releaseID;
		}
		var loadParams={
			add:false,
			addAsChild:false,
			node:node
		};
		var submitParams=loadParams;
		var releaseLbl=getText('admin.project.release.lbl.main');
		me.addOrEditRelease(loadParams,submitParams,getText("common.lbl.edit",releaseLbl));
	},
	addOrEditRelease:function(loadParams,submitParams,title) {
		var windowParameters = {
        	callerScope:this,
        	windowTitle:title,
        	loadUrlParams: loadParams,
        	submitUrlParams: submitParams,
        	refreshAfterSubmitHandler: this.reloadAll,
 
        };
		var windowConfig = Ext.create("com.trackplus.admin.project.ReleaseEdit", windowParameters);
		windowConfig.showWindowByConfig(this);
	},
	
	deleteRelease:function(projectID, releaseID){
		var me=this;
		var releaseLbl=getText('admin.project.release.lbl.main');
		Ext.MessageBox.confirm(getText("common.lbl.delete",releaseLbl),
			getText("common.lbl.removeWarning",releaseLbl),
			function(btn){
				if (btn==="no") {
					return false;
				} else {
					me.deleteSelected.call(me, projectID, releaseID);
				}
			}
		);
	},
	deleteSelected:function(projectID, releaseID){
		var me=this;
		var node=projectID+"_"+releaseID;
		var releaseLbl=getText('admin.project.release.lbl.main');
		Ext.Ajax.request({
			url: "release!delete.action",
			params: {
				node:node
			},
			disableCaching:true,
			scope: me,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				if (responseJson.success===true) {
					//delete done (no replacement were needed)
					me.reloadAll();
				} else {
					var errorCode = responseJson.errorCode;
					if (errorCode) {
						if (errorCode===1){//ERROR_CODE_NEED_REPLACE
							//render dialog for selecting the replacement
							var windowItems = [{xtype : 'label',itemId: 'replacementWarning'},
								CWHF.createSingleTreePicker("Replacement",
									"replacementID", [], null,
									{itemId:"replacementID",
										allowBlank:false,
										blankText: getText('common.err.replacementRequired',releaseLbl),
										labelWidth:150,
										margin:'5 0 0 0'
									})
							];
							var title = getText("common.lbl.delete",releaseLbl);
							var load = {
								loadUrl: 'release!renderReplace.action',
								loadUrlParams:{node: node}
							};
							var submit = {
								submitUrl:"release!replaceAndDelete.action",
								submitUrlParams:{node:node},
								submitButtonText:getText('common.btn.delete'),
								//deleting more users can be a lenghty operation
								timeout:300,
								refreshAfterSubmitHandler:me.reloadAll
								//refreshAfterSubmitHandler:me.refreshAfterDeleteAndRepleceRelease
								//refreshParametersBeforeSubmit:me.getReloadParamsAfterDelete(selectedRecords, extraConfig, responseJson)
							};
							var extraConfig={};
							var windowConfig = Ext.create('com.trackplus.util.WindowConfig',
								{postDataProcess:me.replaceOptionPostDataProcess, extraConfig:extraConfig});
							windowConfig.showWindow(me, title, 500, 200, load, submit, /*refresh,*/ windowItems);
						}
					} else {
						//no right to delete (for ex. with fake URL-Params)
						//var errorMessage=responseJson.errorMessage;
						//me.errorHandlerDelete(selectedRecords, errorCode, errorMessage);
						me.errorHandlerDelete(responseJson);
					}
				}
			},
			failure: function(response){
				com.trackplus.util.requestFailureHandler(response);
			},
			method:"POST"
		});
	},
	replaceOptionPostDataProcess: function(data, panel, extraConfig) {
		var replacementWarning = panel.getComponent('replacementWarning');
		var replacementWarningText = data['replacementWarning'];
		if (CWHF.isNull(replacementWarningText)) {
			var label = data['label'];
			replacementWarningText = getText("common.lbl.replacementWarning", this.getEntityLabel(extraConfig), label);
			replacementWarningText = replacementWarningText + getText("common.lbl.cancelDeleteAlert");
		}
		replacementWarning.setText(replacementWarningText, false);
		var replacementList = panel.getComponent('replacementID');
		replacementList.updateMyOptions(data["replacementTree"]);
		var replacementListLabel = data.replacementListLabel;
		if (CWHF.isNull(replacementListLabel)) {
			replacementListLabel =  this.getTitle('common.lbl.replacement', extraConfig);
		}
		replacementList.labelEl.dom.innerHTML = replacementListLabel;
	},
	reloadAll:function(){
		borderLayout.setLoading(true);
		window.location.href="itemNavigator.action";
	},
	releaseNotes: function(record, tree){
		var url='releaseNotes.action?project='+record.data["objectID"];
		window.open(url,"_blank");
	},


	createFilterCtxMenu:function(tree, record, evtObj) {
		var me=this;
		var filterID=record.data["objectID"];
		tree.setLoading(true);
		var urlStr="filterConfig!filterIsModifiable.action";
		Ext.Ajax.request({
			url: urlStr,
			params: {
				filterID:filterID
			},
			disableCaching:true,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				var modifiable=responseJson.value;
				var items = [];
				var issueFilterLabel=getText("admin.customize.queryFilter.lbl.issueFilter");
				items.push({
					text:getText('common.lbl.add', issueFilterLabel),
					iconCls:"filterAdd",
					scope: this,
					handler:function(){
						me.addFilter(filterID);
					}
				});
				if (modifiable) {
					items.push({
						text:getText("common.lbl.edit", issueFilterLabel),
						iconCls:"filterEdit",
						scope: this,
						handler:function(){
							me.editFilter(filterID);
						}
					});
				}
				items.push({
					text:getText("common.lbl.remove", issueFilterLabel),
					iconCls:"delete",
					scope: this,
					handler:function(){
						me.removeFilter(tree, record, filterID);
					}
				});
				var contextMenu = new Ext.menu.Menu({
					items: items
				});
				contextMenu.showAt(evtObj.getXY());
				tree.setLoading(false);
			},
			failure:function(){
				tree.setLoading(false);
			}
		});
	},

	removeFilter: function(tree, record, filterID){
		Ext.Ajax.request({
			url: 'filtersInMenu!delete.action',
			params: {
				unassign:filterID
			},
			disableCaching:true,
			success: function(response){
				record.remove(true);
			}
		});
	},

	addFilter:function(filterID){
		var me=this;
		var issueFilterLabel=getText("admin.customize.queryFilter.lbl.issueFilter");
		me.addOrEditFilter(true, null, 1, getText("common.lbl.add",issueFilterLabel));
	},

	editFilter:function(filterID){
		var me=this;
		var issueFilterLabel=getText("admin.customize.queryFilter.lbl.issueFilter");
		me.addOrEditFilter(false, filterID, 1, getText("common.lbl.edit",issueFilterLabel));
	},

	addOrEditFilter:function(add, filterID, filterType, title) {
		/*var scope=Ext.create("com.trackplus.admin.customize.filter.FilterConfig",{
			rootID:"issueFilter"
		});
		//scope.indexMax = 0;
		//scope.instant = false;
		scope.renderPath = add;
		com.trackplus.admin.CategoryConfig.showAddEditFilter(scope, this, this.reloadAll, add, filterID, filterType, title);*/
		var operation = "edit";
		if (add) {
			operation = "add";
		}
		var windowParameters = {
            	callerScope:this,
            	windowTitle:title,
            	loadUrlParams: {
    	            add: add,
    	            filterID: filterID,
    	            filterType: filterType,
    	            fromNavigatorContextMenu: true
    	        },
            	submitUrlParams: {
    	            add : add,
    	            filterID : filterID,
    	            filterType : filterType,
    	            fromNavigatorContextMenu : true
    	        },
    	        //reload the navigator after save
    	        refreshAfterSubmitHandler: this.reloadAll,
            	entityContext: {
                	operation: operation,
                    add: add,
                    renderPath: add
            	}
            };
    		var windowConfig = Ext.create("com.trackplus.admin.customize.filter.FilterEdit", windowParameters);
    		windowConfig.showWindowByConfig(this);
	}
});




Ext.define('com.trackplus.itemNavigator.FilterController',{
	extend:'Ext.Base',
	config: {
		model:null,
		queryContext:null,
		skipEmptyNodeType:false,
		itemNavController:null,
		subFilterVisible:false
	},
	constructor : function(cfg) {
		var me = this;
		var config = cfg || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	},
	createView:function(){
		var me=this;
		if(CWHF.isNull(me.model)){
			me.view=null;
			return null;
		}
		var id=me.queryContext.queryType+"_"+me.queryContext.queryID;
		me.filterView=me.createTreeView(me.model['queryView'],me.subFilterVisible===true,id);
		var myTitle=getText('common.lbl.queries');
		if(me.subFilterVisible===true){
			me.subFilterView=me.createTreeView(me.model['subFilterView']);
			myTitle=myTitle+" > "+getText('common.lbl.subfilter');
		}else{
			me.subFilterView=null;
		}
		var filterItems=new Array();
		filterItems.push(me.filterView);
		if(me.subFilterView){
			filterItems.push(me.subFilterView);
		}
		me.toolPlus=Ext.create('Ext.panel.Tool',{
			type:'plus',
			hidden:(me.subFilterVisible===true),
			tooltip:getText('common.lbl.subfilter'),
			handler:function(){
				me.toolPlus.setVisible(false);
				me.toolMinus.setVisible(true);
				me.showHideSubFilter();
			}
		});
		me.toolMinus=Ext.create('Ext.panel.Tool',{
			type:'minus',
			tooltip:getText('common.lbl.subfilter.clear'),
			hidden:!(me.subFilterVisible===true),
			handler:function(){
				me.toolPlus.setVisible(true);
				me.toolMinus.setVisible(false);
				me.showHideSubFilter();
			}
		});
		var width=225;
		if(me.subFilterView){
			width=450;
		}

		me.view=Ext.create('Ext.panel.Panel',{
			region:'west',
			bodyBorder:false,
			border:false,
			//margin:Ext.isIE?'0 -4 0 0':'0 -6 0 0',
			width:width,
			minWidth:width,
			collapsible:true,
			title:myTitle,
			//iconCls:'queryView',
			//collapseMode:'mini',
			cls:'westTreeNavigator',
			collapsedCls:'westTreeNavigatorCollapsed',
			split:true,
			tools:[me.toolPlus,me.toolMinus],
			layout:{
				type: 'hbox',
				pack: 'start',
				align: 'stretch'
			},
			defaults:{
				flex:1
			},
			items:filterItems
		});
		return me.view;
	},

	createTreeView:function(model,border,nodeID){
		var me=this;
		var myCls='westTreeNavigator';
		if(border===true){
			myCls=myCls+' westTreeNavigator-borderRight'
		}
		var treeView=Ext.create('com.trackplus.itemNavigator.FilterView',{
			flex:1,
			model:model,
			cls:myCls,
			filterController:this
		});
		if(nodeID){
			treeView.addListener('afterrender',function(){
				var record = treeView.getStore().getNodeById(nodeID);
				if(record){
					treeView.getSelectionModel().select(record);
					treeView.expandPath(record.getPath());
				}

				treeView.addListener('select',function(rowModel,record,index, eOpts ){
					me.filterByNode.call(me,rowModel,record,index, eOpts);
					return false;
				});

			});
		}else{
			treeView.addListener('select',function(rowModel,record,index, eOpts ){
				me.filterByNode.call(me,rowModel,record,index, eOpts);
				return false;
			});
		}

		treeView.getView().addListener('beforedrop',function(node,data, overModel){
			var canDrop=overModel.data.canDrop;
			if(!canDrop){
				return false;
			}
			/*var verifyAllowDropAJAX=overModel.data.verifyAllowDropAJAX;
			 if(verifyAllowDropAJAX===true){
			 me.dropOnNodeAjaxRequest.call(me,node,data, overModel, dropPosition,dropFunction,eOpts);
			 }else{
			 me.dropOnNode.call(me,node,data, overModel, dropPosition,dropFunction,eOpts);
			 }
			 return false;*/
			return true;
		});
		treeView.getView().addListener('drop',function(node,data, overModel, dropFunction,eOpts){
			me.dropOnNode.call(me,node,data, overModel,dropFunction,eOpts);
			return true;
		});
		treeView.store.on('beforeload',function( store, operation, eOpts){
			if(operation.node){
				var extraParams = me.getTreeExpandExtraParams.call(me,operation.node);
				if (extraParams) {
					treeView.store.proxy.extraParams = extraParams;
				}
			}
		});
		return treeView;
	},

	getTreeExpandExtraParams:function(node){
		var nodeObjectID=node.data.objectID;
		var nodeType=node.data.nodeType;
		return {
			nodeType:nodeType,
			nodeObjectID:nodeObjectID
		};
	},

	showHideSubFilter:function(){
		var me=this;
		me.subFilterVisible=!me.subFilterVisible;
		var myTitle=getText('common.lbl.queries');
		if(me.subFilterVisible===true){
			var cloneChildren=Ext.clone(me.model['subFilterView']);
			me.subFilterView=me.createTreeView(cloneChildren);

			me.view.getEl().fadeOut({
				opacity: 0.1, //can be any value between 0 and 1 (e.g. .5)
				easing: 'easeOut',
				duration: 250,
				remove: false,
				useDisplay: false,
				callback:function(){
					if(me.view.width<450){
						me.view.setWidth(450);
					}
					me.filterView.addCls('westTreeNavigator-borderRight');
					me.view.add(me.subFilterView);
					myTitle=myTitle+" > "+getText('common.lbl.subfilter');
					me.view.setTitle(myTitle);
					me.view.getEl().fadeIn({
						opacity: 1, //can be any value between 0 and 1 (e.g. .5)
						easing: 'easeOut',
						duration: 500,
						callback:function(){
							me.view.updateLayout();
						}
					});
				}
			});

		}else{
			var needToClear=me.isNeedToClear();

			me.view.getEl().fadeOut({
					opacity: 0.1, //can be any value between 0 and 1 (e.g. .5)
					easing: 'easeOut',
					duration: 250,
					remove: false,
					useDisplay: false,
					callback:function(){
						if(me.view.width>225){
							me.view.setWidth(225);
						}
						me.filterView.removeCls('westTreeNavigator-borderRight');
						me.view.remove(me.subFilterView,true);
						me.subFilterView=null;
						if(needToClear){
							me.itemNavController.clearSubfilter.call(me.itemNavController);
						}
						me.view.setTitle(myTitle);
						me.view.getEl().fadeIn({
							opacity: 1, //can be any value between 0 and 1 (e.g. .5)
							easing: 'easeOut',
							duration: 500,
							callback:function(){
								me.view.updateLayout();
							}
						});
					}
			});
		}
		//me.btnClearSubfilter.setVisible(me.subFilterVisible);
	/*Ext.Ajax.request({
			url: "itemNavigator!storeLastSelectedNavigator.action",
			params:{
				objectID:10
			}
		});*/
	},
	isNeedToClear:function(){
		var me=this;
		var needToClear=false;
		var selections=me.subFilterView.getSelectionModel().getSelection();
		if(selections&&selections.length>0){
			var nodeData=selections[0].data;
			var nodeObjectID=nodeData.objectID;
			var nodeType=nodeData.nodeType;
			if(nodeType&&nodeType!==''){
				needToClear=true;
			}
		}
		return needToClear;
	},
	clearSubFilter:function(){
		var me=this;
		if(me.isNeedToClear()){
			me.subFilterView.getSelectionModel().deselectAll();
			me.itemNavController.clearSubfilter.call(me.itemNavController);
		}
	},

	updatePossibleFieldOptions:function(jsonData){
		var me=this;
		//Status query
		var fieldID=4;
		var validDropTargetIds=jsonData['field'+fieldID];
		var partialDropTargetIds=jsonData['partialField'+fieldID];
		var statusNodeID="section_status";
		var node=me.filterView.getStore().getNodeById(statusNodeID);
		me.markValidDropNodes(node,validDropTargetIds,partialDropTargetIds);

		if(me.subFilterVisible===true){
			var fieldIDs=new Array();
			fieldIDs.push(4);//STATE
			//fieldIDs.push(2);//ISSUETYPE
			fieldIDs.push(10);//PRIORITY
			for(var i=0;i<fieldIDs.length;i++){
				fieldID=fieldIDs[i];
				validDropTargetIds=jsonData['field'+fieldID];
				partialDropTargetIds=jsonData['partialField'+fieldID];
				node=me.subFilterView.getStore().getNodeById("section_operation_ItemOperationSelect_"+fieldID);
				me.markValidDropNodes(node,validDropTargetIds,partialDropTargetIds);
			}
		}
	},
	markValidDropNodes:function(node,validDropTargetIds,partialDropTargetIds){
		var me=this;
		var children = node.childNodes;
		if (children ) {
			for (var i = 0; i < children.length; i++) {
				var child = children[i];
				var nodeID = child.data['id'];
				var id = parseInt(nodeID.substring(nodeID.lastIndexOf("_") + 1));
				var cls = child.get('cls');
				var valid = CWHF.isNull(validDropTargetIds) || Ext.Array.indexOf(validDropTargetIds, id) !== -1;
				var partialValid = partialDropTargetIds  && Ext.Array.indexOf(partialDropTargetIds, id) !== -1;
				if (valid) {
					child.set("cls", cls + " treeItem-dropOk");
					child.set('canDrop',true);
				}
				if (partialValid) {
					child.set("cls", cls + " treeItem-dropPartial");
					child.set('canDrop',true);
				}
				if (!valid && !partialValid) {
					child.set("cls", cls + " treeItem-dropDisabled");
					child.set('canDrop',false);
				}
			}
		}
	},
	clearMarkedNodes:function(node){
		var children=node.childNodes;
		if(children){
			for(var i=0;i<children.length;i++){
				var child=children[i];
				var cls=child.get('cls');
				if(cls){
					cls=cls.replace(" treeItem-dropOk","");
					cls=cls.replace(" treeItem-dropPartial","");
					cls=cls.replace(" treeItem-dropDisabled","");
					child.set("cls",cls);
				}
			}
		}
	},


	onAfterDropItems:function(){
		var me=this;
		//Status query
		var statusNodeID="section_status";
		var node=me.filterView.getStore().getNodeById(statusNodeID);
		me.clearMarkedNodes(node);
		if(me.subFilterVisible===true){
			var fieldIDs=new Array();
			fieldIDs.push(4);//STATE
			//fieldIDs.push(2);//ISSUETYPE
			fieldIDs.push(10);//PRIORITY
			for(var i=0;i<fieldIDs.length;i++){
				var fieldID=fieldIDs[i];
				node=me.subFilterView.getStore().getNodeById("section_operation_ItemOperationSelect_"+fieldID);
				me.clearMarkedNodes(node);
			}
		}
	},

	filterByNode:function(rowModel,record){
		var me=this;
		var nodeType=record.data.nodeType;
		if(me.skipEmptyNodeType===true&&(CWHF.isNull(nodeType)||nodeType==='')){
			return false;
		}
		me.itemNavController.filterByNodeType.call(me.itemNavController,record.data);
	},
	getSelectedItemIds:function(data){
		var workItems="";
		var workItemID=-1;
		for(var i=0;i<data.records.length;i++){
			var workItemIDStr=data.records[i].data['workItemID'];
			try {
				workItemID=parseInt(workItemIDStr);
			}catch(e){
				workItemID=-1;
			}
			if(!isNaN(workItemID)&&workItemID!==-1){
				workItems+=workItemID+",";
			}
		}
		return workItems;
	},
	dropOnNodeAjaxRequest:function(node,data, overModel, dropPosition,dropFunction,eOpts){
		var me=this;
		var workItems =me.getSelectedItemIds(data);
		if(workItems===""){
			return false;
		}
		var nodeObjectID=overModel.data.objectID;
		var nodeType=overModel.data.nodeType;
		var urlStr="itemNavigator!canDropOnNode.action";
		me.view.setLoading(true);
		Ext.Ajax.request({
			url: urlStr,
			disableCaching:true,
			success: function(result){
				var jsonData=Ext.decode(result.responseText);
				var canDrop=jsonData.data.canDrop;
				me.view.setLoading(false);
				if(canDrop){
					me.dropOnNode.call(me,node,data, overModel, dropPosition,dropFunction,eOpts);
				}
			},
			failure: function(){
				me.view.setLoading(false);
			},
			method:'POST',
			params:{
				workItems:workItems,
				nodeType:nodeType,
				nodeObjectID:nodeObjectID
			}
		});
	},
	dropOnNode:function(node,data, overModel){
		var me=this;
		var  workItems =me.getSelectedItemIds(data);
		if(workItems===""){
			return false;
		}
		var nodeObjectID=overModel.data.objectID;
		var nodeType=overModel.data.nodeType;
		var dropHandlerCls=overModel.data.dropHandlerCls;
		if(CWHF.isNull(dropHandlerCls)||dropHandlerCls===""){
			dropHandlerCls='com.trackplus.itemNavigator.DropHandler';
		}
		var workItemID=workItems[0];
		var workItemIndex=data.records[0].data['workItemIndex'];
		var dropHandler=Ext.create(dropHandlerCls,{
			workItemID:workItemID,
			workItemIndex:workItemIndex,
			workItems:workItems,
			nodeType:nodeType,
			nodeObjectID:nodeObjectID,
			itemNavController:me.itemNavController
		});
		dropHandler.execute.call(dropHandler);
	}
});

Ext.define('com.trackplus.itemNavigator.DropHandler',{
	extend:'Ext.Base',
	config: {
		workItemIndex:null,
		workItemID:null,
		workItems:null,
		nodeType:null,
		nodeObjectID:null,
		itemNavController:null
	},
	TYPE_COMMON:0,
	TYPE_MASS_OPERATION:1,
	constructor : function(cfg) {
		var me = this;
		var config = cfg || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	},
	execute:function(){
		var me=this;
		me.ajaxRequest.call(me,{
			workItems:me.workItems,
			nodeType:me.nodeType,
			nodeObjectID:me.nodeObjectID
		});
	},
	ajaxRequest:function(params){
		var me=this;
		var urlStr="itemNavigator!dropOnNode.action";
		me.itemNavController.view.setLoading(true);
		Ext.Ajax.request({
			url: urlStr,
			disableCaching:true,
			success: function(result){
				var jsonData=Ext.decode(result.responseText);
				me.itemNavController.view.setLoading(false);
				if(jsonData.success===true){
					me.itemNavController.refresh.call(me.itemNavController,null,null,false,function(){
						if(me.workItemIndex){
							me.itemNavController.issueListFacade.selectItemByIndex.call(me.itemNavController.issueListFacade,me.workItemIndex);
						}else{
							if(me.workItemID){
								me.itemNavController.issueListFacade.selectItem.call(me.itemNavController.issueListFacade,me.workItemID);
							}else{
								me.itemNavController.issueListFacade.selectItem.call(me.itemNavController.issueListFacade,me.workItems.split(","));
							}
						}
					});
				}else{
					me.handleError.call(me,jsonData,params);
				}
			},
			failure: function(){
				me.itemNavController.view.setLoading(false);
			},
			method:'POST',
			params:params
		});
	},
	confirmationSubmitHandler:function(params){
		var me=this;
		if(CWHF.isNull(params)){
			params={};
		}
		params['params.confirmSave']='true';
		me.ajaxRequest.call(me,params);
	},
	handleError:function(jsonData,params){
		var me=this;
		var type=jsonData.type;
		switch (type){
			case me.TYPE_COMMON:{
				var title=getText("common.err.failure");
				if(jsonData.title){
					title=jsonData.title;
				}
				Ext.MessageBox.show({
					title: title,
					msg:jsonData.errorMessage,
					buttons: Ext.MessageBox.OK,
					icon: Ext.MessageBox.WARNING
				});
				break;
			}
			case me.TYPE_MASS_OPERATION:{
				var massEx=jsonData.massOperation;
				var errorCode=massEx.errorCode;
				if(errorCode&&errorCode===4){
					var title="Error";
					if(massEx.title){
						title=massEx.title;
					}
					Ext.MessageBox.confirm(title,
						massEx.errorMessage,
						function(btn){
							if (btn==="no") {
								return false;
							} else {
								me.confirmationSubmitHandler.call(me,params);
							}
						});
				}else{
					var title="Error";
					if(massEx.title){
						title=massEx.title;
					}
					Ext.MessageBox.show({
						title: title,
						msg:massEx.errorMessage,
						buttons: Ext.MessageBox.OK,
						icon: Ext.MessageBox.WARNING
					});
				}
				break;
			}
		}
	}
});
Ext.define('com.trackplus.itemNavigator.PopupDropHandler',{
	extend:'com.trackplus.itemNavigator.DropHandler',
	config:{
		width:600,
		height:400,
		title:'',
		layout:'fit',
		iconCls:null,
		autoScroll:false
	},
	execute:function(){
		var me=this;
		me.formPanel=me.createFormPanel();
		if(CWHF.isNull(me.formPanel)){
			me.ajaxRequest.call(me,{
				workItems:me.workItems,
				nodeType:me.nodeType,
				nodeObjectID:me.nodeObjectID
			});
			return;
		}
		me.openDialog();
	},
	openDialog:function(){
		var me=this;
		if(me.modalDialog){
			me.modalDialog.destroy();
		}
		if(CWHF.isNull(me.width)){
			me.width=600;
		}
		if(CWHF.isNull(me.height)){
			me.height=400;
		}
		if(CWHF.isNull(me.autoScroll)){
			me.autoScroll=false;
		}
		me.modalDialog = new Ext.Window({
			layout	  : me.layout,
			width	   :me.width ,
			height	  : me.height,
			iconCls	 : me.iconCls,
			closeAction :'destroy',
			plain	   : true,
			bodyBorder  : false,
			bodyStyle:{border:'none'},
			title		:me.title,
			modal	   :true,
			items	   :[me.formPanel],
			autoScroll  :me.autoScroll,
			buttons: [
				{text: getText('common.btn.ok'),
					handler  : function(){
						me.okHandler.call(me);
					}
				}/*,{text :getText('common.btn.reset'),
					handler  : function(){
						me.formPanel.getForm().reset();
					}
				}*/,{text : getText('common.btn.close'),
					handler  : function(){
						me.modalDialog.destroy();
					}
				}
			]
		});
		me.modalDialog.show();
	},
	okHandler:function(){
		var me=this;
		var urlStr="itemNavigator!dropOnNode.action";
		var params={
			workItems:me.workItems,
			nodeType:me.nodeType,
			nodeObjectID:me.nodeObjectID
		};
		me.formPanel.getForm().submit({
			url:urlStr,
			params:params,
			scope: me,
			success: function(form, action) {
				var result = action.result;
				if (result.success) {
					me.modalDialog.destroy();
					me.itemNavController.refresh.call(me.itemNavController);
					/*me.itemNavController.refresh.call(me.itemNavController,
						me.itemNavController.nodeType,me.itemNavController.nodeObjectID);*/
				}else {
					me.handleError.call(me,action.result,params);
				}
			},
			failure: function(form, action) {
				me.handleError.call(me,action.result,params);
			}
		});
	},
	createFormPanel:function(){
		var me=this;
		return Ext.create('Ext.form.Panel',{
			items:me.createFormItems()
		});
	},
	createFormItems:function(){
		return [];
	}
});
Ext.define('com.trackplus.itemNavigator.BasketDropHandler',{
	extend:'com.trackplus.itemNavigator.PopupDropHandler',
	width:450,
	height:250,
	layout:'fit',
	iconCls:null,
	autoScroll:false,
	title:getText("itemov.basketOperation.delegate.title"),
	createFormPanel:function(){
		var me=this;
		switch(me.nodeObjectID){
			case 4:{
				//CALENDAR
				me.width=380;
				me.height=100;
				me.title=getText("itemov.basketOperation.calendar.title");
				return Ext.create('Ext.form.Panel',{
					layout:{
						type:'hbox'
					},
					bodyStyle:{
						padding:'5px'
					},
					items:me.createFormItemsCalendar()
				});
			}
			case 5:{
				//DELEGATED
				return Ext.create('Ext.form.Panel',{
					bodyStyle:{
						padding:'5px'
					},
					items:me.createFormItems()
				});
			}
		}
		return null;
	},
	createFormItemsCalendar:function() {
		return [CWHF.createDateTimeField("itemov.basketOperation.calendar.date", "params.date", "params.time", {submitFormat:null,width:100}, {width:80, increment:30}, {labelWidth:150})];
	},
	createFormItems:function(){
		var me=this;
		var store = Ext.create('Ext.data.Store', {
			fields: ['id', 'label'],
			proxy: {
				type: 'ajax',
				url:'itemNavigator!getResponsibles.action?workItems='+me.workItems,
				reader: {
					type: 'json'
				}
			}
		});

		var cmbPerson=Ext.create('Ext.form.field.ComboBox',{
			fieldLabel: getText("admin.customize.automail.trigger.lbl.responsible"),
			labelAlign:'right',
			store: store,
			displayField: 'label',
			valueField: 'id',
			labelWidth:100,
			width:250,
			name:'params.responsible'
		});
		var dateTimePicker = CWHF.createDateTimeField("itemov.basketOperation.delegate.date", "params.date", "params.time", {submitFormat:null,width:100}, {width:80, increment:30}, {labelWidth:100});
		var txtComment=Ext.create('Ext.form.field.TextArea',{
			fieldLabel: getText('common.history.lbl.comment'),
			labelAlign:'right',
			name:'params.comment',
			minWidth:25,
			labelWidth:100,
			anchor: '100% -50'
		});
		return [cmbPerson,dateTimePicker,txtComment];
	}
});


