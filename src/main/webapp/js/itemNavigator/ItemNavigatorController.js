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

Ext.define('com.trackplus.itemNavigator.ItemNavigatorController',{
	extend:'Ext.Base',
	mixins:{
		navigable:'com.trackplus.itemNavigator.TreeNavigableItem'
	},
	config: {
		model:null,
		useLastQuery:true,
		baseAction:'itemNavigator',
		skipEmptyNodeType:false,
		nodeType:null,
		nodeObjectID:null,
		workItemID:null,
		actionID:null,
		settingsVisible:false,
		filterEditVisible:false
	},
	view:null,
	filterController:null,
	selectedIssueViewDescriptor:null,
	selectedIssueViewDescriptorStandard:null,
	issueListFacade:null,
	selectedExtraAction:null,
	subDialogForExcelWizard:null,
	//apply filter from navigator upper area
	fromSession: false,

	constructor : function(cfg) {
		var me = this;
		var config = cfg || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		this.initConfig(config);
		this.mixins.navigable.constructor.call(this,config);
	},
	test:function() {
	},
	createView:function(){
		var me=this;
		me.selectedIssueViewDescriptor=me.findView(me.model.lastSelectedView);
		me.selectedIssueViewDescriptorStandard=me.findView(me.model.lastSelectedViewPerson);

		me.filterEditPanel=Ext.create('Ext.panel.Panel', {
			region:'north',
			margins: '0 0 0 0',
			border: false,
			bodyBorder:false,
			hidden:!me.filterEditVisible,
			split:true,
			stateful:true,
			stateId:'itemNavigator_filterEdit',
			height:200,
			layout:'fit'
		});

		me.issueViewPanel=Ext.create('Ext.panel.Panel',{
			region:'center',
			margins: '0 0 0 0',
			border: false,
			bodyBorder:false,
			layout:'fit'
		});
		me.view=Ext.create('Ext.panel.Panel',{
			region:'center',
			margins: '0 0 0 0',
			border: false,
			bodyBorder:false,
			baseCls:'itemNavigator',
			layout:'border',
			bodyStyle:{
				padding:'0px 0px 0px 0px'
			},
			items:[me.filterEditPanel,me.issueViewPanel]
		});


		//me.initIssueListViewToolbar();
		me.updateSelectedIssueListView(true);
		return me.view;
	},
	updateSelectedIssueListView:function(createView){
		var me=this;
		var queryContextID=me.model.queryContext.id;
		var filterType=me.model.queryContext.queryType;
		var filterID=me.model.queryContext.queryID;
		var filterName=me.model.queryContext.queryName;
		var viewID = me.selectedIssueViewDescriptor.id;
		if(CWHF.isNull(this.fromSession)||this.fromSession===false){
			if(me.useLastQuery) {
				var instantFilter = Ext.create("com.trackplus.itemNavigator.InstantFilter",
						{filterName: filterName,
						filterType: filterType,
						filterID:filterID,
						queryContextID:queryContextID,
						viewID:viewID,
						fromSession:this.fromSession});
				me.filterEditPanel.removeAll(true);
				me.filterEditPanel.add(instantFilter.createPanel());
				instantFilter.addListener("applyFilter",me.onApplyFilter,me);
				instantFilter.addListener("clearFilter",me.onClearFilter,me);
				instantFilter.loadFilter.call(instantFilter);
			}
		}

		if(me.model.tooManyItems){
			me.issueViewPanel.removeAll(true);
			var pan=Ext.create('Ext.Component',{
				html:getText('itemov.err.tooManyItems'),
				border:false,
				cls:'infoBox1'
			});
			me.issueViewPanel.add(pan);
			me.view.updateLayout ();
			return true;
		}

		if(createView===true||CWHF.isNull(me.issueListFacade)){
			me.createIssueListView();
		}else{
			me.issueListFacade.refreshData.call(me.issueListFacade,me.model.issues);
		}
	},

	createIssueListView:function(){
		var me=this;
		if(me.issueListFacade){
			me.issueListFacade.destroy.call(me.issueListFacade);
			me.issueListFacade=null;
			delete me.issueListFacade;
		}
		me.issueListFacade=Ext.create('com.trackplus.itemNavigator.IssueListViewFacade',{
			descriptor:me.selectedIssueViewDescriptor,
			model:me.model,
			queryContext:me.model.queryContext,
			itemNavigatorController:me
		});
		me.issueViewPanel.removeAll(true);
		var pan=me.issueListFacade.createView.call(me.issueListFacade,me.model,me.settingsVisible);
		me.issueViewPanel.add(pan);
		me.view.updateLayout ();
		if(pan.getEl()) {
			me.onReadyHandler();
		}else {
			pan.addListener('render',me.onReadyHandler,me);
		}
	},

	onApplyFilter:function(queryCtx){
		var me=this;
		var filterType=queryCtx.filterType;
		var filterID=queryCtx.filterID;
		this.fromSession=true;
		me.refresh(null,null,true,null);
	},

	onClearFilter:function(queryCtx){
		var me=this;
		var filterType=queryCtx.filterType;
		var filterID=queryCtx.filterID;
		this.fromSession=false;
	},

	/**
	 * This method initialize external drag drop functionality for
	 * all item navigator views.
	 */
	onReadyHandler: function() {
		var me = this;
		if(me.model.showDragDropInfoMsg) {
			CWHF.showMsgInfo(getText('itemov.lbl.dragDropInfoText'), false);
		}
		me.dragCounter = 0;
		var viewDrop=me.issueListFacade.view;
		var domEl=viewDrop.getEl().dom;
		me.highLightPanel = new Ext.Panel({
			width: 500,
			height: 500,
		    bodyCls:'itemNavigatorViewHighlightWhenDrag',
		    hidden: true
		  });
		viewDrop.insert(me.highLightPanel);

		domEl.addEventListener("dragenter", function(evt){
			evt.stopPropagation();
			evt.preventDefault();
			var mouseX=parseInt(evt.clientX);
			var mouseY=parseInt(evt.clientY);
			var winX=mouseX-150;
			if(winX<10){
				winX=10;
			}
			me.dragCounter++;
			if(!me.highLightPanel.isVisible()) {
				me.highLightPanel.setWidth(me.view.lastBox.width);
				me.highLightPanel.setHeight(me.view.lastBox.height);
				me.highLightPanel.setVisible(true);
			}
//			viewDrop.addCls("itemNavigatorViewHighlightWhenDrag");
		}, false);

		domEl.addEventListener("dragleave",  function(evt){
			evt.stopPropagation();
			me.dragCounter--;
			if (Ext.isChrome || Ext.isSafari || Ext.isIE) {
				if(me.dragCounter=== 0) {
					me.highLightPanel.setVisible(false);
				}
			}else {
				me.highLightPanel.setVisible(false);
			}
//			viewDrop.removeCls("itemNavigatorViewHighlightWhenDrag");
		}, false);


		domEl.addEventListener("dragover",  function(evt){
			evt.stopPropagation();
			evt.preventDefault();
//			me.highLightPanel.setVisible(true);
		}, false);

		domEl.addEventListener("drop",  function(evt){
			evt.stopPropagation();
			evt.preventDefault();
//			viewDrop.removeCls("itemNavigatorViewHighlightWhenDrag");
			me.highLightPanel.setVisible(false);
			var files = evt.dataTransfer.files;
			if(files.length === 1) {
				var importExcelWizard = Ext.create('com.trackplus.admin.action.ImportExcel');
				var importMsProjPopUpWiz = 	Ext.create('com.trackplus.admin.action.ImportMsProject', {projectID:null});
				var file = null;
				if(files  && files.length > 0){
					file = files[0];
				}
				if(importExcelWizard.validateFileExtension(file.name)) {
					importExcelWizard.createPopUpDialog(files, me);
				}
				if(importMsProjPopUpWiz.validateFileExtension(file.name)) {
					importMsProjPopUpWiz.createPopUpDialog(files);
				}
			}
		}, false);
	},

	createNavigator:function(){
		var me=this;
		me.filterController=Ext.create('com.trackplus.itemNavigator.FilterController',{
			model:me.model.navigator,
			queryContext:me.model.queryContext,
			skipEmptyNodeType:me.skipEmptyNodeType,
			lastSelectedNavigator:me.model.lastSelectedNavigator,
			itemNavController:me
		});
		return me.filterController.createView.call(me.filterController);
	},

	issueSelectionChange:function(selections){
		var btnMassEdit=Ext.getCmp("massEdit");
		var btnMassCopy=Ext.getCmp("massCopy");
		var btnAddLink=Ext.getCmp("addLink");
		btnMassEdit.setDisabled(CWHF.isNull(selections)||selections.length===0);
		btnMassCopy.setDisabled(CWHF.isNull(selections)||selections.length===0);
		btnAddLink.setDisabled(CWHF.isNull(selections)||selections.length<2);
	},
	showGroupByPopup:function(){
		var me = this;
		var groupingDialog = Ext.create("com.trackplus.itemNavigator.Grouping",{
			filterType:me.model.queryContext.queryType,
			filterID:me.model.queryContext.queryID,
			itemNavigatorController:me
		});
		groupingDialog.showDialog();

		/*var me=this;
		if(me.grouping){
			me.grouping.destroyMe.call(me.grouping);
		}
		me.grouping=new com.trackplus.itemNavigator.Grouping(me);

		me.grouping.show.call(me.grouping);*/
	},
	showColumnsPopup:function(){
		var me=this;
		var includeLongFields=false;
		var selView=me.selectedIssueViewDescriptor;
		if(selView){
			includeLongFields=selView.useLongFields;
		}
		if(me.chooseColumns){
			me.chooseColumns.destroyMe.call(me.chooseColumns);
		}
		me.chooseColumns=Ext.create('com.trackplus.itemNavigator.ChooseColumns',{
			itemNavigatorController:me,
			filterType:me.model.queryContext.queryType,
			filterID:me.model.queryContext.queryID,
			includeLongFields:includeLongFields
		});
		me.chooseColumns.setIncludeLongFields(includeLongFields);
		me.chooseColumns.showDialog.call(me.chooseColumns);
	},
	changeLayout:function(){
		var me=this;
		var selView=me.selectedIssueViewDescriptor;
		me.selectedIssueViewDescriptor=null;
		me.changeIssueListViewMode(selView,null,true);
	},
	sortChange:function(sort){
		var me=this;
		me.model.layout.sortField=sort.sortField;
		me.model.layout.sortOrder=sort.sortOrder;
		me.model.layout.sortWithSOr=sort.sortWithSO;
	},

	/*appendDashboardParams:function(params){
	 var me=this;
	 if(me.model.dashboardParams){
	 for(var x in me.model.dashboardParams){
	 params['dashboardParams.'+x]=me.model.dashboardParams[x];
	 }
	 }
	 },*/

	changeIssueListViewMode:function(issueListViewDescriptor,handler,storeLast){
		var me=this;
		if(me.selectedIssueViewDescriptor){
			if(me.selectedIssueViewDescriptor.id===issueListViewDescriptor.id){
				return false;
			}
		}
		me.selectedIssueViewDescriptor=issueListViewDescriptor;
		if(me.btnListView) {
			me.btnListView.setText(me.selectedIssueViewDescriptor.name);
			me.btnListView.setIconCls(me.selectedIssueViewDescriptor.iconCls);
			me.btnListView.setTooltip(me.selectedIssueViewDescriptor.description);
		}
		var items = [];
		if(issueListViewDescriptor.id==='com.trackplus.itemNavigator.GanttViewPlugin'){
			items.push(me.btnPrintGantt);
		}else {
			items.push(me.btnExportPDF);
		}
		items.push(me.btnExportXLS);
		items.push(me.btnExportCSV);
		items.push(me.btnExportDOCX);
		items.push(me.btnExportXML);
		items.push(me.btnExportTrackplus);
		me.btnExport.menu.removeAll();
		me.btnExport.menu.add(items);

		//var btnGroupByCmp=Ext.getCmp('btnGroupBy');
		//btnGroupByCmp.setVisible(me.selectedIssueViewDescriptor.enabledGrouping);

		//var btnChooseColumnsCmp=Ext.getCmp('btnChooseColumns');
		//btnChooseColumnsCmp.setVisible(me.selectedIssueViewDescriptor.enabledColumnChoose);

		//var separatorGroupColumns=Ext.getCmp('separatorGroupColumns');
		//separatorGroupColumns.setVisible(me.selectedIssueViewDescriptor.enabledGrouping||me.selectedIssueViewDescriptor.enabledColumnChoose);
		me.removeOrAddPrintGanttIntoPDFAfterChange(issueListViewDescriptor);
		if(storeLast){
			me.selectedIssueViewDescriptorStandard=me.selectedIssueViewDescriptor;
			Ext.Ajax.request({
				url: "itemNavigator!storeLastSelectedView.action",
				params:{
					viewID:me.selectedIssueViewDescriptor.id
				},
				success: function(response){
					me.doChangeIssueListViewMode(issueListViewDescriptor,handler,storeLast);
				},
				failure: function(){
					CWHF.showMsgError('Failure');
				}
			});
		}else{
			me.doChangeIssueListViewMode(issueListViewDescriptor,handler,storeLast);
		}

	},

	/**
	 * This method remove or add the Print into pdf menu item depending on selected item navigator view element.
	 */
	removeOrAddPrintGanttIntoPDFAfterChange: function(issueListViewDescriptor) {
		var me = this;
		if(issueListViewDescriptor.id==='com.trackplus.itemNavigator.GanttViewPlugin'){
			var found = false;
			me.btnExtraActions.menu.items.each(function(item){
				if(item.itemId ) {
					if(item.itemId  === 'exportGanttIntoPDFBtn') {
						found = true;
					}
				}
			});
			if(!found) {
				me.btnExtraActions.menu.add(me.btnExportGanttIntoPDF);
			}
		}else {
			me.btnExtraActions.menu.items.each(function(item){
				if(item.itemId ) {
					if(item.itemId  === 'exportGanttIntoPDFBtn') {
						me.btnExtraActions.menu.remove(item);
					}
				}
			});
		}
	},

	doChangeIssueListViewMode:function(issueListViewDescriptor,handler,storeLast){
		var me=this;
		if(handler){
			handler.call(me);
		}else{
			var urlStr="itemNavigator!reload.action";
			me.refresh(urlStr,me.model.queryContext.id,true);
		}
	},

	createLastQueriesMenu:function(lastQueries){
		var me=this;
		var menu=[];
		if(lastQueries){
			for(var i=0;i<lastQueries.length;i++){
				menu.push(me.createQueryMenu(lastQueries[i]/*.id,lastQueries[i].label*/));
			}
		}
		return Ext.create('Ext.menu.Menu',{items:menu});
	},
	updateMyItemNavigatorModel:function(jsonData){
		var me=this;
		if(jsonData.layout){
			me.model.layout=jsonData.layout;
		}else{
		}
		me.model.issues=jsonData.issues;
		me.model.isFilterView=jsonData.isFilterView;
		me.model.maySaveFilterLayout=jsonData.maySaveFilterLayout;
		me.model.summaryItemsBehavior=jsonData.summaryItemsBehavior;
		me.model.tooManyItems=jsonData.tooManyItems;
		me.model.listViewData=jsonData.listViewData;
		me.model.milestoneWorkitems=jsonData.milestoneWorkitems;
		me.model.totalCount=jsonData.totalCount;
		me.model.forceAllItems=jsonData.forceAllItems===true;
		me.model.overflowItems=jsonData.overflowItems;
		me.model.holidays=jsonData.holidays;
		me.model.localizedToolTipLabels=jsonData.localizedToolTipLabels;
		me.model.count=jsonData.count;
		me.model.lastQueries=jsonData.lastQueries;
		me.model.queryContext=com.trackplus.itemNavigator.ItemNavigatorFacade.createQueryContext(jsonData.queryContext);
		me.model.queryFieldCSS=jsonData.queryFieldCSS;
		me.model.isActiveTopDownDate=jsonData.isActiveTopDownDate;
		me.model.showBaseline=jsonData.showBaseline;
		me.model.validateRelationships=jsonData.validateRelationships;
		me.model.showDragDropInfoMsg=jsonData.showDragDropInfoMsg;
		me.model.showBoth=jsonData.showBoth;
		me.model.highlightCriticalPath=jsonData.highlightCriticalPath;
		me.model.nodeType=me.nodeType;
		me.model.nodeObjectID=me.nodeObjectID;
		me.model.isPrintItemEditable=jsonData.isPrintItemEditable;
		com.trackplus.itemNavigator.ItemNavigatorFacade.updateCssRules(jsonData.queryFieldCSS,jsonData.cssRules);
	},
	refresh:function(urlStr,queryContextID,createView,handlerRefresh){
		//TODO FIXME remove this
		createView=true;
		borderLayout.setLoading(true);
		var me=this;
		if(CWHF.isNull(urlStr)){
			urlStr=me.getBaseAction()+'!refresh.action';
		}
		if(CWHF.isNull(queryContextID)){
			queryContextID=me.model.queryContext.id;
		}
		var params={
			queryContextID:queryContextID,
			nodeType:me.nodeType,
			nodeObjectID:me.nodeObjectID,
			forceAllItems:me.model.forceAllItems,
			fromSession:this.fromSession
		};
		//me.appendDashboardParams.call(me,params);
		Ext.Ajax.request({
			url: urlStr,
			disableCaching:true,
			success: function(result){
				borderLayout.setLoading(false);
				me.refreshSuccessHandler.call(me,result,createView,handlerRefresh);
			},
			failure: function(){
				borderLayout.setLoading(false);
				CWHF.showMsgError('Failure');
			},
			method:'POST',
			params:params
		});
	},
	refreshSuccessHandler:function(result,createView,handlerRefresh){
		//TODO FIXME remove this
		createView=true;
		var me=this;
		var jsonData=Ext.decode(result.responseText);
		if(jsonData.success===false){
			CWHF.showMsgError(jsonData.errorMessage);
			return false;
		}
		me.updateMyItemNavigatorModel(jsonData.data);
		me.updateTotalCount();
		me.updateLastExecutedQueries();
		me.updateSelectedIssueListView(createView===true);
		borderLayout.setLoading(false);
		if(handlerRefresh){
			handlerRefresh.call(me);
		}
		me.issueListFacade.addOrRemoveSaveButton(null);

	},
	filterByNodeType:function(nodeData){
		var me=this;
		var nodeObjectID=nodeData.objectID;
		var nodeType=nodeData.nodeType;
		var useFilter=nodeData.useFilter;
		var filterViewID=nodeData.filterViewID;

		if(me.nodeType===nodeType&&me.nodeObjectID===nodeObjectID){
			return false;
		}
		if(me.btnListView){
			me.btnListView.setDisabled(false);
		}
		if(filterViewID&&filterViewID!==''){
			if(me.btnListView){
				me.btnListView.setDisabled(true);
			}
			if(me.selectedIssueViewDescriptor.id!==filterViewID){
				me.nodeType=nodeType;
				me.nodeObjectID=nodeObjectID;
				me.changeIssueListViewMode(me.findView(filterViewID),function(){
					me.doFilterByNode(nodeType,nodeObjectID,useFilter);
				},false);
				return true;
			}
		}else{
			if(me.selectedIssueViewDescriptorStandard&&me.selectedIssueViewDescriptorStandard.id!==me.selectedIssueViewDescriptor.id){
				me.nodeType=nodeType;
				me.nodeObjectID=nodeObjectID;
				me.changeIssueListViewMode(me.selectedIssueViewDescriptorStandard,function(){
					me.doFilterByNode(nodeType,nodeObjectID,useFilter);
				},true);
				return true;
			}
		}

		me.doFilterByNode(nodeType,nodeObjectID,useFilter);

	},
	clearSubfilter:function(){
		var me=this;
		me.doFilterByNode(null,null,null);
	},
	doFilterByNode:function(nodeType,nodeObjectID,useFilter){
		var me=this;
		me.fromSession = false;
		var urlStr=me.getBaseAction()+"!filterByNode.action";
		me.nodeType=nodeType;
		me.nodeObjectID=nodeObjectID;
		var queryContextID=me.model.queryContext.id;
		var params={
			queryContextID:queryContextID,
			nodeType:me.nodeType,
			nodeObjectID:me.nodeObjectID
		};
		borderLayout.setLoading(true);
		Ext.Ajax.request({
			url: urlStr,
			disableCaching:true,
			success: function(result){
				borderLayout.setLoading(false);
				var jsonData=Ext.decode(result.responseText);
				if(jsonData.success===false){
					var msg="";
					if(jsonData.errorMessage){
						msg=jsonData.errorMessage;
					}else{
						msg="Failure!";
					}
					CWHF.showMsgError(msg);
					return;
				}
				if(jsonData.data.includeParameters===true){
					//var node=nodeType+"_"+nodeObjectID;
					com.trackplus.admin.Filter.executeFilter(me, nodeObjectID, false);
				}else{
					var createView=jsonData.data.layout!==null;
					if(useFilter===true){
						createView=true;
					}
					me.refreshSuccessHandler.call(me,result,createView);
				}
			},
			failure: function(){
				borderLayout.setLoading(false);
			},
			method:'POST',
			params:params
		});
	},

	executePreviousQuery:function(lastQuery){
		var me=this;
		me.fromSession = false;
		var queryContextID = lastQuery["queryContextID"];
		var filterViewID=lastQuery['viewID'];
		if(me.btnListView){
			me.btnListView.setDisabled(false);
		}
		if(filterViewID&&filterViewID!==''){
			if(me.btnListView){
				me.btnListView.setDisabled(true);
			}
			if(me.selectedIssueViewDescriptor.id!==filterViewID){
				me.changeIssueListViewMode(me.findView(filterViewID),function(){
					var urlStr="itemNavigator!executePreviousQuery.action";
					me.refresh(urlStr,queryContextID,true);
				},false);
				return true;
			}
		}else{
			if(me.selectedIssueViewDescriptorStandard&&me.selectedIssueViewDescriptorStandard.id!==me.selectedIssueViewDescriptor.id){
				me.changeIssueListViewMode(me.selectedIssueViewDescriptorStandard,function(){
					var urlStr="itemNavigator!executePreviousQuery.action";
					me.refresh(urlStr,queryContextID,true);
				},true);
				return true;
			}
		}
		if(me.btnListView){
			me.btnListView.setDisabled(false);
		}
		var urlStr="itemNavigator!executePreviousQuery.action";
		me.refresh(urlStr,queryContextID,true);
	},
	createQueryMenu:function(lastQuery/*queryContext,queryName*/){
		var me=this;
		//var queryType = lastQuery["type"];
		var queryContextID = lastQuery["queryContextID"];
		//var queryID = lastQuery["objectID"];
		var queryName =  lastQuery["label"];
		var tooltip =  lastQuery["tooltip"];
		var icon=lastQuery["icon"];
		var iconCls=lastQuery["iconCls"];
		/*var iconCls='treeFilter-ticon';
		if(queryType===2){//dashboard
			iconCls='dashboard-ticon'
		}
		if(queryType===4){//basket
			iconCls='basket-ticon';
		}
		if(queryType===5){//PROJECT_RELEASE= 5;
			var entityID=0;
			try {
				entityID=parseInt(queryID);
			}catch(e){
				entityID=0;
			}
			if(entityID<0){
				iconCls='projects-ticon';
			}else{
				iconCls='release-ticon';
			}
		}
		if(queryType===6){//SCHEDULED= 6;
			iconCls="schedule-ticon";
		}
		if (queryType===7) {
			iconCls="tqlPlusFilter-ticon";
		}*/
		return {
			text:queryName,
			tooltip: tooltip,
			icon:icon,
			iconCls:iconCls,
			handler:function(){
				me.executePreviousQuery.call(me,lastQuery);
			}
		}
	},
	updateLastExecutedQueries:function(){
		var me=this;

		if(me.useLastQuery) {
			var btnQueries = Ext.getCmp('reportQueryName');
			//btnQueries.setIconCls(me.getQueryContextIcon(me.model.queryContext));
			btnQueries.setText(me.model.queryContext.queryName);
			btnQueries.setMenu(me.createLastQueriesMenu(me.model.lastQueries));
			borderLayout.borderLayoutController.updateLastExecutedQueries.call(borderLayout.borderLayoutController, me.model.lastQueries);
		}
	},
	updateTotalCount:function(){
		var me=this;
		Ext.getCmp('txtReportCount').setText(me.createHtmlTotalNumber());
		Ext.getCmp('txtReportFilteredCount').setText(me.createHtmlFilteredNumber());
		var overflowItems=me.model.overflowItems;
		me.viewAllLink.setVisible(overflowItems&&overflowItems>0);
	},
	createHtmlTotalNumber:function(){
		var me=this;
		var totalCount=me.model.totalCount;
		var overflowItems=me.model.overflowItems;
		var txtTotalNumber="";
		if(totalCount===-1){
			return getText('itemov.lbl.totalNumber') + ' ? ';
		}
		if(overflowItems&&overflowItems>0){
			txtTotalNumber='<span class="warning">!</span> '+getText('itemov.lbl.totalNumber') +' ' +totalCount
		}else{
			txtTotalNumber = getText('itemov.lbl.totalNumber') + ' ' + totalCount;
		}
		return  txtTotalNumber;
	},
	createHtmlFilteredNumber:function(){
		var me=this;
		var count=me.model.count;
		var overflowItems=me.model.overflowItems;
		var txtFiltered="";
		if(count===-1){
			return '   ' + getText('itemov.lbl.filtered')+': ? ';
		}
		if(overflowItems&&overflowItems>0){
			txtFiltered='   ' + getText('itemov.lbl.filtered')+': '+overflowItems+" ("+getText('itemov.lbl.filteredLimitedTo')+" "+count+")";
		}else{
			txtFiltered='   ' + getText('itemov.lbl.filtered')+': '+count;
		}
		return  txtFiltered;
	},

	createIssueListViewButton:function(issueListViewDescriptor){
		var me=this;
		return {
			//itemId:issueListViewDescriptor.id,
			iconCls:issueListViewDescriptor.iconCls,
			overflowText :issueListViewDescriptor.name,
			text :issueListViewDescriptor.name,
			tooltip :issueListViewDescriptor.description,
			handler:function(){
				me.changeIssueListViewMode(issueListViewDescriptor,null,true);
			}
		};
	},
					storeExtraAction:function(extraActionID){
		var me=this;
		if(me.selectedExtraAction===extraActionID){
			return false;
		}
		me.selectedExtraAction=extraActionID;
		var text,iconCls;
		switch(extraActionID){
			case 1:{//MASS_EDIT
				text=getText('common.btn.bulkEdit');
				iconCls='bulkEdit';
				break;
			}
			case 2:{//MASS_COPY
				text=getText('common.btn.bulkCopy');
				iconCls='bulkCopy';
				break;
			}
			case 3:{//EXPORT_REPORT
				text=getText('common.btn.report');
				iconCls='reports16';
				break;
			}
			case 4:{//EXCHANGE
				text=getText('common.btn.export');
				iconCls='export';
				break;
			}
			case 5:{//EXPORT_PDF
				text=getText('itemov.btn.exportPDF');
				iconCls='printIssue';
				break;
			}
			case 6:{//EXPORT_CSV
				text=getText('itemov.btn.exportCSV');
				iconCls='exportCSV';
				break;
			}
			case 7:{//EXPORT_XML
				text=getText('itemov.btn.exportXML');
				iconCls='exportXML';
				break;
			}
			case 8:{//EXPORT_XLS
				text=getText('itemov.btn.exportExcel');
				iconCls='exportExcel';
				break;
			}
			case 9:{//EXPORT_DOCX
				text=getText('itemov.btn.exportDocx');
				iconCls='exportDocx';
				break;
			}
			case 10:{//ADD_LINK
				text=getText('common.btn.addLink');
				iconCls='links16';
				break;
			}
			case 13:{//EXPORT MS. PROJECT
				text=getText('itemov.btn.exportMSProject');
				iconCls='msProject-ticon';
				break;
			}
			case 14:{//IMPORT FROM EXCEL
				text=getText('itemov.btn.importExcel');
				iconCls='exportDocx';
				break;
			}
			case 15:{//IMPORT FROM MS PROJECT
				text=getText('itemov.btn.importMSProject');
				iconCls='msProject-ticon';
				break;
			}
		}
		//me.btnExtraActions.setText(text);
		//me.btnExtraActions.setIconCls(iconCls);
	},
	MASS_EDIT:1,
	MASS_COPY:2,
	EXPORT_REPORT:3,
	EXCHANGE:4,
	EXPORT_PDF:5,
	EXPORT_CSV:6,
	EXPORT_XML:7,
	EXPORT_XLS:8,
	EXPORT_DOCX:9,
	ADD_LINK:10,
	SAVE_AS_STANDARD_LAYOUT:11,
	USE_STANDARD_LAYOUT:12,
	EXPORT_MS_PROJECT:13,
	IMPORT_EXCEL:14,
	IMPORT_MS_PROJECT:15,
	SAVE_AS_FILTER_LAYOUT:16,

	executeExtraAction:function(){
		var me=this;
		var extraActionID=me.selectedExtraAction;
		if(CWHF.isNull(extraActionID)){
			return false;
		}
		switch(extraActionID){
			case 1:{//MASS_EDIT
				me.massOperation.call(me,false);
				break;
			}
			case 2:{//MASS_COPY
				me.massOperation.call(me,true);
				break;
			}
			case 3:{//EXPORT_REPORT
				me.executeReport.call(me);
				//me.executeUrl('reportConfig.action?fromIssueNavigator=true',"_self");
				break;
			}
			case 4:{//EXCHANGE
				me.executeUrl("exchangeExport.action");
				break;
			}
			case 5:{//EXPORT_PDF
				me.executeUrl("reportDatasource.action?exportFormat=pdf");
				break;
			}
			case 6:{//EXPORT_CSV
				me.executeUrl("reportDatasource.action?exportFormat=csv");
				break;
			}
			case 7:{//EXPORT_XML
				me.executeUrl("reportDatasource!serializeDatasource.action?fromIssueNavigator=true");
				break;
			}
			case 8:{//EXPORT_XLS
				me.executeUrl("reportDatasource.action?exportFormat=xls");
				break;
			}
			case 9:{//EXPORT_DOCX
				//me.executeUrl("docxExport.action");
				me.exportDocx();
				break;
			}
			case 10:{
				me.addLink.call(me);
				break;
			}
		}
	},
	initToolbar:function(){
		var me=this;
		var includeCharts=me.model.includeCharts;
		var extraActions=[];
		var btnMassEdit={
			id:'massEdit',
			//itemId:'massEditItemId',
			text:getText('common.btn.bulkEdit'),
			overflowText:getText('common.btn.bulkEdit'),
			tooltip:getText('common.btn.bulkEdit.tt'),
			iconCls: 'bulkEdit',
			//selectedIssueMap && selectedIssueMap.size()>0
			disabled:true,
			handler:function(){
				me.massOperation.call(me,false);
				me.storeExtraAction.call(me,me.MASS_EDIT);
			}
		};
		var btnMassCopy={
			id:'massCopy',
			//itemId:'massCopyItemId',
			text:getText('common.btn.bulkCopy'),
			overflowText:getText('common.btn.bulkCopy'),
			tooltip:getText('common.btn.bulkCopy.tt'),
			iconCls: 'bulkCopy',
			//selectedIssueMap && selectedIssueMap.size()>0
			disabled:true,
			handler:function(){
				me.massOperation.call(me,true);
				me.storeExtraAction.call(me,me.MASS_COPY);
			}
		};
		var btnAddLink={
			id:'addLink',
			//itemId:'addLinkItemId',
			text:getText('common.btn.addLink'),
			overflowText:getText('common.btn.addLink'),
			tooltip:getText('common.btn.addLink.tt'),
			iconCls: 'links16',
			//selectedIssueMap && selectedIssueMap.size()>0
			disabled:true,
			handler:function(){
				me.addLink.call(me);
				me.storeExtraAction.call(me,me.ADD_LINK);
			}
		};
		var btnReports={
			//id:'exportReport',
			//itemId:'exportReportItemId',
			text:getText('common.btn.report'),
			overflowText:getText('common.btn.report'),
			tooltip:getText('common.btn.report.tt'),
			iconCls: 'reports16',
			disabled:false,
			handler:function(){
				//me.executeUrl('reportConfig.action?fromIssueNavigator=true',"_self");
				me.executeReport.call(me);
				me.storeExtraAction.call(me,me.EXPORT_REPORT);
			}
		};
		me.btnExportTrackplus={
			text:getText('itemov.btn.exportTrackplus'),
			overflowText:getText('itemov.btn.exportTrackplus'),
			tooltip:getText('itemov.btn.exportTrackplus.tt'),
			iconCls: 'export',
			disabled:false,//#session.user.sys
			handler:function(){
				me.executeUrl("exchangeExport.action");
				me.storeExtraAction.call(me,me.EXCHANGE);
			}
		};

		me.btnPrintGantt={
			text: getText('itemov.ganttView.print'),
			overflowText: getText('itemov.ganttView.print'),
			tooltip:getText('report.tooltip.printGantt'),
			iconCls: 'export',
			disabled:false,//#session.user.sys
			handler:function(){
				me.issueListFacade.listViewPlugin.view.zoomToFit();
				me.issueListFacade.listViewPlugin.view.print();
			}
		};

		me.btnExportPDF={
			text:getText('itemov.btn.exportPDF'),
			overflowText:getText('itemov.btn.exportPDF'),
			tooltip:getText('report.tooltip.exportPDF'),
			iconCls: 'printIssue',
			disabled:false,
			handler:function(){
				me.executeUrl("reportDatasource.action?exportFormat=pdf");
				me.storeExtraAction.call(me,me.EXPORT_PDF);
			}
		};

		me.btnExportCSV={
			text:getText('itemov.btn.exportCSV'),
			overflowText:getText('itemov.btn.exportCSV'),
			tooltip:getText('report.tooltip.exportCSV'),
			iconCls: 'exportCSV',
			disabled:false,
			handler:function(){
				me.executeUrl("reportDatasource.action?exportFormat=csv");
				me.storeExtraAction.call(me,me.EXPORT_CSV);
			}
		};
		me.btnExportXML={
			text:getText('itemov.btn.exportXML'),
			overflowText:getText('itemov.btn.exportXML'),
			tooltip:getText('itemov.btn.exportXML'),
			iconCls: 'exportXML',
			disabled:false,
			handler:function(){
				me.executeUrl("reportDatasource!serializeDatasource.action?fromIssueNavigator=true&exportFormat=xml");
				me.storeExtraAction.call(me,me.EXPORT_XML);
			}
		};
		me.btnExportXLS={
			text:getText('itemov.btn.exportExcel'),
			overflowText:getText('itemov.btn.exportExcel'),
			tooltip:getText('report.tooltip.exportExcel'),
			iconCls: 'exportExcel',
			disabled:false,
			handler:function(){
				me.executeUrl("reportDatasource.action?exportFormat=xls");
				me.storeExtraAction.call(me,me.EXPORT_XLS);
			}
		};

		me.btnExportMSProject={
			text:getText('itemov.btn.exportMSProject'),
			overflowText:getText('itemov.btn.exportMSProject'),
			tooltip:getText('report.tooltip.exportMSProject'),
			iconCls: 'msProject-ticon',
			disabled:false,
			handler:function(){
				var exportExcelWizard = Ext.create('com.trackplus.admin.action.ExportMsProject', {projectID:-1});
				exportExcelWizard.createPopUpDialog();
				me.storeExtraAction.call(me,me.EXPORT_MS_PROJECT);
			}
		};

		me.btnExportDOCX={
			text:getText('itemov.btn.exportDocx'),
			overflowText:getText('itemov.btn.exportDocx'),
			tooltip:getText('itemov.btn.exportDocx'),
			iconCls: 'word',
			disabled:false,
			handler:function(){
				//me.executeUrl("docxExport.action");
				me.exportDocx();
				me.storeExtraAction.call(me,me.EXPORT_DOCX);
			}
		};
		var items = [];
		if(me.model.lastSelectedView==='com.trackplus.itemNavigator.GanttViewPlugin'){
			items.push(me.btnPrintGantt);
		}else {
			items.push(me.btnExportPDF);
		}
		items.push(me.btnExportXLS);
		items.push(me.btnExportMSProject);
		items.push(me.btnExportCSV);
		items.push(me.btnExportDOCX);
		items.push(me.btnExportXML);
		items.push(me.btnExportTrackplus);
		me.btnExportGanttIntoPDF= {
			itemId: 'exportGanttIntoPDFBtn',
			text: getText('itemov.btn.exportPDF'),
			overflowText: getText('itemov.btn.exportPDF'),
			tooltip: getText('itemov.btn.exportPDF.tt'),
			iconCls: 'exportPDF',
			disabled:false,
			handler:function(){
				var gantView=me.issueListFacade.getView().items.items[1];
				var exportPlugin = null;
				for(var aPlugin in gantView.plugins) {
					if(gantView.plugins[aPlugin].$className === "Gnt.plugin.Export") {
						exportPlugin = gantView.plugins[aPlugin];
					}
				}
				if(exportPlugin ) {
				    Ext.Ajax.request({
				        url : "itemNavigator!getGanttExportPDFConfig.action",
				        disableCaching : true,
				        success : function(response) {
					        var respJSON = Ext.decode(response.responseText);
					        if(respJSON.success) {
					        	exportPlugin.defaultConfig.format = respJSON.format;
					        	exportPlugin.defaultConfig.orientation = respJSON.orientation;
					        	exportPlugin.defaultConfig.range = respJSON.range;
					        }
					        gantView.showExportDialog();
				        },
				        failure : function() {
				        	gantView.showExportDialog();
				        },
				        method : 'POST'
				    });
				}
			}
		};

		me.btnExport={
			text:getText('common.btn.export'),
			overflowText:getText('common.btn.export'),
			tooltip:getText('common.btn.export.tt'),
			iconCls: 'export',
			disabled:false,//#session.user.sys
			menu:Ext.create('Ext.menu.Menu',{
				items:items
			})
		};
		me.btnImportXLS={
			text:getText('itemov.btn.importExcel'),
			overflowText:getText('itemov.btn.importExcel'),
			tooltip:getText('report.tooltip.importExcel'),
			iconCls: 'exportExcel',
			disabled:false,
			handler:function(){
				var importExcelWizard = Ext.create('com.trackplus.admin.action.ImportExcel');
				importExcelWizard.createPopUpDialog(null);
				me.storeExtraAction.call(me,me.IMPORT_EXCEL);
			}
		};
		me.btnImportMSProject={
			text:getText('itemov.btn.importMSProject'),
			overflowText:getText('itemov.btn.importMSProject'),
			tooltip:getText('report.tooltip.importMSProject'),
			iconCls: 'msProject-ticon',
			disabled:false,
			handler:function(){
				var importMsProjPopUpWiz = 	Ext.create('com.trackplus.admin.action.ImportMsProject', {projectID:null});
				importMsProjPopUpWiz.createPopUpDialog(null);
				me.storeExtraAction.call(me,me.IMPORT_MS_PROJECT);
			}
		};
		var importItems = [];
		importItems.push(me.btnImportXLS);
		importItems.push(me.btnImportMSProject);
		me.btnImport={
			text:getText('common.btn.import'),
			overflowText:getText('common.btn.import'),
			tooltip:getText('common.btn.import.tt'),
			iconCls: 'import',
			disabled:false,//#session.user.sys
			menu:Ext.create('Ext.menu.Menu',{
				items:importItems
			})
		};
		extraActions.push(btnMassEdit);
		extraActions.push(btnMassCopy);
		extraActions.push(btnAddLink);
		if (com.trackplus.TrackplusConfig.user.reports) {
			extraActions.push(btnReports);
		}
		extraActions.push(me.btnImport);
		extraActions.push(me.btnExport);
		if(me.model.lastSelectedView==='com.trackplus.itemNavigator.GanttViewPlugin'){
			extraActions.push(me.btnExportGanttIntoPDF);
		}
		me.btnExtraActions=Ext.create('Ext.button.Split',{
			id:'extraActions',
			itemId:'extraActionsItemId',
			text:getText('itemov.btn.extraActions'),
			overflowText:getText('itemov.btn.extraActions'),
			tooltip:getText('itemov.btn.extraActions.tt'),
			iconCls: 'extraActions',
			disabled:false,
			handler:function(){
				me.executeExtraAction.call(me);
			},
			menu:extraActions
		});
	},
	createToolbar:function(){
		var me=this;
		me.initToolbar();
		var toolbarItems=new Array(0);
		if(me.useLastQuery) {
			toolbarItems.push(Ext.create('Ext.button.Split', {
				iconCls: 'queryHistory',//me.getQueryContextIcon(me.model.queryContext),
				text: me.model.queryContext.queryName,
				id: 'reportQueryName',
				menu: me.createLastQueriesMenu(me.model.lastQueries)
			}));
			toolbarItems.push(borderLayout.createItemToolbarSeparator());
		}
		toolbarItems.push({
			xtype: 'tbtext',
			disabled:true,
			id:'txtReportCount',
			text:me.createHtmlTotalNumber()
		});
		toolbarItems.push({
			xtype: 'tbtext',
			disabled:true,
			id:'txtReportFilteredCount',
			text:me.createHtmlFilteredNumber()
		});
		me.viewAllLink=Ext.create('Ext.ux.LinkComponent',{
			handler:me.forceViewAll,
			hidden:(CWHF.isNull(me.model.overflowItems)||me.model.overflowItems===0),
			scope:me,
			margin:'0 0 0 0',
			style:{
				overflow:'hidden'
			},
			label:getText('itemov.lbl.forceShowAll')
		});
		toolbarItems.push(me.viewAllLink);
		toolbarItems.push('->');
		me.btnFilterEdit=Ext.create('Ext.button.Button',{
			iconCls:'queryView',
			enableToggle:true,
			allowDepress:true,
			cls:'toolbarItemAction-noText',
			text:getText('common.btn.filter'),
			overflowText :getText('common.btn.filter'),
			tooltip :getText('common.btn.filter'),
			handler:function(){
				me.showHideFilterEdit();
			},
			scope:me
		});

		if(me.useLastQuery) {
			toolbarItems.push(me.btnFilterEdit);
		}
		toolbarItems.push(me.btnExtraActions);
		if(me.model.issueListViewDescriptors.length>1){
			var listViewMenu=new Array();
			for(var i=0;i<me.model.issueListViewDescriptors.length;i++){
				listViewMenu.push(me.createIssueListViewButton(me.model.issueListViewDescriptors[i]));
			}
			me.btnListView=Ext.create('Ext.button.Split',{
				id:'listView',
				itemId:'elistViewItemId',
				iconCls:me.selectedIssueViewDescriptor.iconCls,
				text:me.selectedIssueViewDescriptor.name,
				overflowText:me.selectedIssueViewDescriptor.name,
				tooltip:me.selectedIssueViewDescriptor.description,
				disabled:me.model.isFilterView,
				handler:function(){
					//me.changeIssueListViewMode();
				},
				menu:listViewMenu
			});
			toolbarItems.push(me.btnListView);
		}

		me.btnToolbarSettings=Ext.create('Ext.button.Button',{
			iconCls:'btnConfig',
			enableToggle:true,
			allowDepress:true,
			cls:'toolbarItemAction-noText',
			text:getText('itemov.lbl.toolbarSettings'),
			overflowText :getText('itemov.lbl.toolbarSettings'),
			tooltip :getText('itemov.lbl.toolbarSettings.tt'),
			handler:function(){
				me.showHideSettings();
			},
			scope:me
		});
		toolbarItems.push(me.btnToolbarSettings);
//		me.issueListFacade.addOrRemoveSaveButton(toolbarItems);
		return toolbarItems;
	},
	forceViewAll:function(){
		var me=this;
		me.model.forceAllItems=true;
		me.refresh();
	},
	showHideFilterEdit:function(){
		var me=this;
		me.filterEditVisible=!me.filterEditVisible;
		Ext.Ajax.request({
			url: "userPreferences.action?property=itemNavigator_filterEditVisible&value="+me.filterEditVisible,
			disableCaching:true,
			success: function(data){
			},
			failure: function(type, error){
			}
		});
		var b=me.filterEditVisible;
		var filterPanel=me.filterEditPanel;
		var el=filterPanel.getEl();
		var settingsHeight=100;//me.settingsHeight;
		var gridEl=me.issueViewPanel.getEl();
		var gridY=gridEl.getY();
		var gridHeight=gridEl.getHeight();
		if(b){
			gridEl.animate({
				top:settingsHeight,
				height:gridHeight-settingsHeight,
				duration:300,
				listeners:{
					'afteranimate':function(){
						filterPanel.setVisible(true);
						el.slideIn('t', {
							easing: 'easeOut',
							duration: 300,
							//remove: false,
							//useDisplay: false,
							listeners:{
								'afteranimate':function(){
									me.view.updateLayout ();
								}
							}
						});
					}
				}
			});
		}else{
			el.slideOut('t', {
				easing: 'easeOut',
				duration: 300,
				listeners:{
					'afteranimate':function(){
						gridEl.animate({
							top:0,
							height:gridHeight+settingsHeight,
							duration:300,
							listeners:{
								'afteranimate':function(){
									filterPanel.setVisible(false);
									me.view.updateLayout ();
								}
							}
						});
					}
				}
			});
		}

	},
	showHideSettings:function(){
		var me=this;
		me.settingsVisible=!me.settingsVisible;
		me.issueListFacade.setVisibleTools(me.settingsVisible);
		Ext.Ajax.request({
			url: "userPreferences.action?property=itemNavigator_settingsVisible&value="+me.settingsVisible,
			disableCaching:true,
			success: function(data){
			},
			failure: function(type, error){
			}
		});
	},
	getSelectedIssues:function(){
		var me=this;
		if(CWHF.isNull(me.selectedIssueViewDescriptor)){
			return null;
		}
		if(CWHF.isNull(me.issueListFacade)){
			return null;
		}
		return me.issueListFacade.getSelectedIssues();
	},
	formatIntegersAsString:function(integers){
		var str="";
		if(integers&&integers.length>0){
			for(var i=0;i<integers.length;i++){
				str+=integers[i];
				if(i<integers.length-1){
					str+=",";
				}
			}
		}
		return str;
	},
	massOperation:function(copy){
		var me=this;
		var selectedIssues=me.getSelectedIssues();
		var workItemIds=me.formatIntegersAsString(selectedIssues);
		var massOperationController=Ext.create('com.trackplus.itemNavigator.MassOperationController',{
			copy:copy,
			selectedIssueIDs:workItemIds/*,
			itemNavigatorController:me*/
		});
		massOperationController.itemNavigatorController=me;
		massOperationController.showDialog();
	},
	addLink:function() {
		/*var fromGantt = false;
		if(this.issueListFacade.descriptor.id === "com.trackplus.itemNavigator.GanttViewPlugin") {
			fromGantt = true;
		}*/
		var selectedIssues=this.getSelectedIssues();
		var workItemIDs=selectedIssues.join();
		var link=Ext.create('com.trackplus.issue.Link',{
			itemNavigatorController:this,
			workItemIDs:workItemIDs//,
			//fromGantt: fromGantt
		});
		link.addLinkFromIssueNavigator();
	},
	executeReport:function() {
		var selectedIssues=this.getSelectedIssues();
		var workItemIDs = null;
		if (selectedIssues) {
			workItemIDs=selectedIssues.join();
		}
		com.trackplus.admin.Report.executeReportFromIssueNavigator(workItemIDs);
	},

	exportDocx: function() {
		var selectedIssues=this.getSelectedIssues();
		var workItemIDs = null;
		if (selectedIssues) {
			workItemIDs=selectedIssues.join();
		}
		var exportDocx = Ext.create("com.trackplus.admin.action.ExportDocx", {workItemIDs:workItemIDs});
		exportDocx.createExportForm();
	},

	executeUrl:function(url) {
		var selectedIssues=this.getSelectedIssues();
		var workItemIDs = null;
		if (selectedIssues) {
			workItemIDs=selectedIssues.join();
		}
		if (CWHF.isNull(workItemIDs)) {
			window.open(url);
		} else {
			//because there could be a large number of selections the url would be too long, we force a http post
			var dummyForm = Ext.create('Ext.form.Panel', {
			items:[],
			url:url,
			baseParams: {
				workItemIDs:workItemIDs
			},
			standardSubmit:true});
			dummyForm.getForm().submit();
		}
	},

	findView:function(viewID){
		var me=this;
		var lastSelectedView=me.model.issueListViewDescriptors[0];
		if(viewID){
			for(var i=0;i<me.model.issueListViewDescriptors.length;i++){
				if(me.model.issueListViewDescriptors[i].id===viewID){
					lastSelectedView=me.model.issueListViewDescriptors[i];
					break;
				}
			}
		}
		return lastSelectedView;
	},
	successHandler:function(data,successExtra){
		this.itemActionSuccessHandler(data,successExtra);
	},
	executeItemAction:function(workItemID,actionID,parentID,workItemIndex,animateTarget,position, createItem, addLinkFromContextMenu){
		var me=this;
		var extraCfg={
			parentID:parentID,
			addLinkFromContextMenu:addLinkFromContextMenu,
			successExtra:{
				workItemIndex:workItemIndex,
				createItem:createItem
			},
			animateTarget:animateTarget
		};
		me.openItem(workItemID,actionID,workItemIndex,extraCfg);
	},
	navigate:function(workItemID,workItemIndex,dir){
		var me=this;
		if(me.issueListFacade){
			return me.issueListFacade.navigate.call(me.issueListFacade,workItemID,workItemIndex,dir);
		}
		return null;
	},
	itemChangeHandler:function(fields){
		var me=this;
		if(me.issueListFacade){
			if(me.issueListFacade.viewContainsFields(fields)) {
				me.refresh();
			}
		}
	},
	selectItem:function(workItemID){
		var me=this;
		if(me.issueListFacade){
			me.issueListFacade.selectItem.call(me.issueListFacade,workItemID);
		}
	},
	deselectItem:function(workItemID){
		var me=this;
		if(me.issueListFacade){
			me.issueListFacade.deselectItem.call(me.issueListFacade,workItemID);
		}
	},
	closeItemDialog:function(itemAction){
		var me=this;
		var workItemID=itemAction.workItemID;
		//me.issueListFacade.deselectItem.call(me.issueListFacade,workItemID);
	},

	chooseParent:function(rowData){
		var me=this;
		var workItemID=rowData.workItemID;
		var projectID=rowData.projectID;
		//var projectName=rowData.projectName;
		var ajaxContext={
			url:'item!setParent.action',
			params:{
				'workItemID':workItemID
			},
			pickItemName:'parentID',
			successHandler:me.itemActionSuccessHandler,
			successHandlerScope:me
		};
		var issuePicker=Ext.create('com.trackplus.util.IssuePicker',{
			workItemID:workItemID,
			parent:true,
			projectID:projectID*-1,
			//projectName:projectName,
			title:getText('common.btn.chooseParent'),
			ajaxContext:ajaxContext,
			width:700,
			height:500,
			stateId:'chooseParent'
		});
		issuePicker.showDialog();
	},
	itemActionSuccessHandler:function(data,successExtra){
		var me=this;
		var workItemID=null;
		if(data){
			workItemID=data.workItemID;
		}else{
			if(successExtra){
				workItemID=successExtra['workItemID'];
			}
		}
		if(successExtra&&successExtra['createItem']===true) {
			CWHF.showMsgInfo(getText('item.msg.newItemCreated', data.workItemIDDisplay, data.title));
		}
		me.refresh(null,null,false,function(){
			var workItemIndex=null;
			if(successExtra){
				workItemIndex=successExtra['workItemIndex'];
			}
			var selectedItems=null;
			if(workItemID){
				selectedItems=me.issueListFacade.selectItem.call(me.issueListFacade,workItemID);
			}
			if(CWHF.isNull(selectedItems)||selectedItems.length===0){
				if(workItemIndex){
					me.issueListFacade.selectItemByIndex.call(me.issueListFacade,workItemIndex);
				}
			}
			/*if(successExtra&&successExtra['actionID']&&successExtra['actionID']===-2){
			 me.issueListFacade.onItemDblClick.call(me.issueListFacade,{workItemID:workItemID});
			 }*/


		});
	},
	executeAJAXItemAction:function(url){
		var me=this;
		Ext.Ajax.request({
			url: url,
			success: function(response){
				me.itemActionSuccessHandler.call(me);
			},
			failure:function(){
				alert("failure");
			}
		});
	}
});
