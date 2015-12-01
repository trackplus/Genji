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


/* Toolbar items*/
com.trackplus.item.ToolbarItem = {
	SIBLING : 1,
	CHOOSE_PARENT : 2,
	PRINT : 3,
	PRINT_WITH_CHILDREN : 4,
	ACCESS_LEVEL : 5,
	ARCHIVE : 6,
	DELETE : 7,
	MAIL : 8,
	BACK : 9,
	NAVIGATION_NEXT : 10,
	NAVIGATION_PREV : 11,
	ITEM_ACTION : 12,
	SAVE : 13,
	RESET : 14,
	CANCEL : 15,
	PRINT_ITEM : 16,
	EXPORT : 17
};

Ext.define('com.trackplus.screen.ItemView',{
	extend : 'Ext.container.Container',
	config : {
		model : null,
		itemDetailComponent:null
	},
	// region: 'center',
	margin : '0 0 0 0',
	padding : '0 0 0 0',
	border : false,
	layout : {
		type : 'border'
	},
	itemPanel : null,
	initComponent : function() {
		var me = this;
		me.items = me.createChildren();
		me.callParent();
		me.addListener('afterrender',me.setFocusComponent,me);
	},
	setFocusComponent:function(){
		var me=this;
		if(me.txtTitleHeader&&!me.txtTitleHeader.isHidden()){
			me.txtTitleHeader.focus(false);
		}
	},
	createChildren : function() {
		var me = this;
		var children = [];
		/*if (me.model.includeItemTitle) {
			children.push(com.trackplus.item
					.createItemTitleSection(
							me.model.issueNoLabel,
							me.model.workItemID,
							me.model.statusDisplay,
							me.model.synopsis, 'north'));
		}*/
		var cgfItemPanel={
			border : false
		}
		if (me.model.includeBottomTabs) {
			cgfItemPanel.margin = '5 0 5 0';
			cgfItemPanel.anchor = '100%';
			cgfItemPanel.layout='fit';
		}else{
			cgfItemPanel.margin = '0 0 0 0';
			cgfItemPanel.padding = '0 5 5 5';
			cgfItemPanel.region='center';
			cgfItemPanel.autoScroll =true;
		}
		me.itemPanel = Ext.create('Ext.form.Panel', cgfItemPanel);
		children.push(me.itemPanel);

		// detail part
		// me.model.includeBottomTabs=false;

		if (me.model.includeBottomTabs) {

			var cmp = me.itemDetailComponent.createComponent();
			children.push(cmp);
		}
		var project=me.model.projectLabel;
		var issueNumber=me.model.workItemIDDisplay;
		var statusDisplay=me.model.statusDisplay;
		var itemLockedMessage=me.model.itemLockedMessage;
		var htmlProjectHeader='<img style="margin-bottom: -2px;" src="optionIconStream.action?fieldID=-2&optionID='+me.model.issueTypeID+'" title="'+me.model.issueTypeLabel+'">' +
			' <strong>'+project+'</strong>';
		if(issueNumber){
			htmlProjectHeader+='&nbsp;/&nbsp;<span class="emphasize"><strong>'+ issueNumber + '</strong></span>';
		}
		if(statusDisplay){
			htmlProjectHeader+="&nbsp;:&nbsp;"+ '<span class="dataEmphasize">'+ statusDisplay + '</span>';
		}
		if (!me.model.readOnlyMode) {
			htmlProjectHeader+='<span class=requiredHint><span class="requiredHintBar">&nbsp;</span> = ' + getText("common.lbl.requiredInfo") +'</span>';
		}
		if (itemLockedMessage) {
			htmlProjectHeader+='<span class="itemLock">' + itemLockedMessage +'</span>';
		}
		me.projectHeader=Ext.create('Ext.form.Label',{
			html: htmlProjectHeader,
			margin:'0 0 5 0'
		});
		me.lblWrapper=null;
		me.lblTitleHeader=Ext.create('Ext.form.Label',{
			text: me.model.synopsis,
			cls: 'titleHeader',
			margin:'0 0 5 0',
			hidden:!me.model.readOnlyMode
		});
		var editable=(me.model.readOnlyMode===true&&me.model.inlineEdit===true&&me.model.synopsisReadonly===false);
		if(editable){
			me.lblWrapper=Ext.create('Ext.container.Container',{
				border:false,
				cls:'editableFieldWrapper',
				overCls:'editableFieldWrapper-over',
				bodyBorder:false,
				margin:'0 0 2px 0',
				bodyPadding:0,
				items:[me.lblTitleHeader],
				listeners: {
					afterrender:{
						fn:function(cmp){
							Ext.create('Ext.tip.ToolTip', {
								target: cmp.el,
								trackMouse: true,
								dismissDelay: 0,
								html: getText('item.view.dblClickToEditField')
							});
						}
					}
				}
			});
		}else{
			me.lblWrapper=me.lblTitleHeader;
		}
		me.txtTitleHeader=Ext.create('com.trackplus.SpellCheckTextField',{
			beforeSubTpl:'<span class="required" style="height:27px">&nbsp;</span>',
			allowBlank:false,
			value: me.model.synopsis,
			cls: 'titleHeader',
			height:28,
			enforceMaxLength:true,
			maxLength:255,
			name:'fieldValues.f17',
			hideLabel:true,
			style:{
				opacity:me.model.readOnlyMode===true?0:1
			},
			hidden:me.model.readOnlyMode
		});

		me.headerPanel=Ext.create('Ext.container.Container', {
			padding:'10 5 0 5',
			margin:'0 0 0 0',
			border:false,
			cls:'itemHeader',
			region:'north',
			layout:{
				type:'vbox',
				align:'stretch'
			},
			items:[me.projectHeader,me.lblWrapper,me.txtTitleHeader]
		});

		me.panelWrapper=null;
		if(me.model.includeBottomTabs){
			//TODO removw this fix for IE
			if(Ext.isIE9){
				me.panelWrapper=Ext.create('Ext.panel.Panel',{
					layout:'anchor',
					defaults : {
						anchor : '100%'
					},
					margin : '0 0 0 0',
					padding : '0 5 5 5',
					border: false,
					bodyBorder:false,
					region:'center',
					autoScroll : true,
					items:children
				});
			}else{
				me.panelWrapper=Ext.create('Ext.container.Container',{
					layout:'anchor',
					defaults : {
						anchor : '100%'
					},
					margin : '0 0 0 0',
					padding : '0 5 5 5',
					border: false,
					region:'center',
					autoScroll : true,
					items:children
				});
			}
		}else{
			me.panelWrapper=me.itemPanel;
		}
		return [me.headerPanel,me.panelWrapper];
	}
});



Ext.define('com.trackplus.item.ItemComponent',{
	extend:'Ext.Base',
	mixins: {
		observable: 'Ext.util.Observable'
	},
	config:{
		includeItemTitle:false,
		workItemID:null,
		workItemIDDisplay:null,
		lastModified:null,
		projectID:null,
		projectLabel:null,
		issueTypeID:null,
		issueTypeLabel:null,
		actionID:null,
		readOnlyMode:false,
		includeBottomTabs:false,
		itemDetailData:null,
		issueNoLabel:'IssueNo',
		statusDisplay:null,
		statusID:null,
		synopsis:null,
		itemLockedMessage:null
	},
	itemPanel:null,
	itemScreenPanel:null,
	txtTitleHeader:null,
	itemDetailComponent:null,
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		this.mixins.observable.constructor.call(this, config);
		this.initConfig(config);
	},
	createItemPanel:function() {
		var me = this;
		if(me.itemDetailData){
			var tabs = me.itemDetailData.tabs;
			var activeTab = me.itemDetailData.activeTab;
			me.itemDetailComponent = new com.trackplus.itemDetail.ItemDetailComponent(
				me.workItemID, me.projectID,
				me.issueTypeID, tabs, activeTab,me.eventHandlerTabDetail,me,me.lastModified);
		}
		var itemView = Ext.create('com.trackplus.screen.ItemView', {
			itemDetailComponent:me.itemDetailComponent,
			model : {
				workItemID : me.workItemID,
				projectID : me.projectID,
				projectLabel:me.projectLabel,
				issueTypeID : me.issueTypeID,
				issueTypeLabel:me.issueTypeLabel,
				issueNoLabel : me.issueNoLabel,
				workItemIDDisplay:me.workItemIDDisplay,
				statusDisplay : me.statusDisplay,
				statusID:me.statusID,
				synopsis : me.synopsis,
				includeItemTitle : me.includeItemTitle,
				includeBottomTabs : me.includeBottomTabs,
				itemDetailData : me.itemDetailData,
				readOnlyMode:me.readOnlyMode,
				synopsisReadonly:me.synopsisReadonly,
				inlineEdit:me.inlineEdit,
				itemLockedMessage:me.itemLockedMessage
			},
			region : 'center'
		});
		me.itemPanel = itemView.itemPanel;
		me.txtTitleHeader=itemView.txtTitleHeader;
		me.lblWrapper=itemView.lblWrapper;
		me.lblTitleHeader=itemView.lblTitleHeader;
		var editable=(me.readOnlyMode===true&&me.inlineEdit===true&&me.synopsisReadonly===false);
		if(editable){
			me.lblTitleHeader.addListener('afterrender',function(){
				me.lblTitleHeader.getEl().addListener('dblclick',me.lblTitleHeaderDblClick,me);
			});
		}
		return itemView;
	},
	eventHandlerTabDetail:function(eventName,args){
		var me=this;
		if(eventName==='itemChange'){
			me.screenFacade.screenController.refreshByFields.apply(me.screenFacade.screenController,args);
		}
		me.fireEventArgs(eventName,args);
	},
	lblTitleHeaderDblClick:function(){
		var me=this;
		//me.lblWrapper.setVisible(false);
		//me.txtTitleHeader.setVisible(true);
		//me.txtTitleHeader.focus();


		var el=me.lblWrapper.getEl();
		el.fadeOut({
			opacity: 0, //can be any value between 0 and 1 (e.g. .5)
			easing: 'easeOut',
			duration: 150,
			remove: false,
			useDisplay: false,
			callback:function(){
				me.lblWrapper.setVisible(false);
				me.txtTitleHeader.setVisible(true);
				me.txtTitleHeader.getEl().fadeIn({
					opacity: 1, //can be any value between 0 and 1 (e.g. .5)
					easing: 'easeOut',
					duration: 500,
					callback:function(){
						me.txtTitleHeader.focus();
					}
				});
			}
		});
		me.fireEvent('editMode');
	},
	refreshChildren:function(){
		var me=this;
		if(me.gridChildren){
			me.gridChildren.setLoading(true);
			var urlStr='item!loadChildren.action';
			Ext.Ajax.request({
				url : urlStr,
				params : {
					'workItemID' : me.workItemID
				},
				success : function(response) {
					var jsonData = Ext.decode(response.responseText);
					var children = jsonData.data.children;
					me.gridChildren.store.loadData(children);
					me.gridChildren.setLoading(false);
				},
				failure : function() {
					alert("failed!");
					me.gridChildren.setLoading(false);
				}
			});
		}
	},
	createChildrenPanel:function(panelsLength, children) {
		var me=this;
		var margin = '5 0 5 0';
		var cls = "screenPanel";
		//if(panelsLength%2===0){
			cls="screenPanel-odd";
		//}
		var panel = Ext.create('Ext.panel.Panel', {
			bodyPadding : 0,
			margin : margin,
			border : false,
			bodyBorder : false,
			cls:cls+' gridNoBorder',
			/*(bodyStyle:{
				borderRight:'1px solid #D0D0D0'
			},*/
			defaults : {
				frame : false,
				border : false
			},
			items : [ {
				xtype : 'displayfield',
				cls:'screenField',
				style:{marginLeft:'5px'},
				fieldLabel : getText('item.printItem.lbl.childIssues'),
				value : children.length
			} ]
		});
		var store = Ext.create('Ext.data.Store', {
			fields : [
				{name : 'workItemID',type : 'int'},
				{name : 'id'},
				{name : 'status'},
				{name : 'originator'},
				{name : 'responsible'},
				{name : 'synopsis'}
			],
			data : children
		});
		// create the Grid
		me.gridChildren = Ext.create('Ext.grid.Panel', {
			store : store,
			queryMode : 'local',
			border    : false,
			bodyBorder:false,
			columnLines :false,
			style:{
				 borderTop:'1px solid #D0D0D0'
			},
			stateId : 'stateGrid',
			columns : [ {
				text : getText('field.label12'),
				width : 75,
				sortable : true,
				menuDisabled:true,
				dataIndex : 'id',
				xtype:'linkcolumn',
				handler:me.clickChildItemNumber,
				scope:me
				/*renderer:function(value,metaData, record){
					var urlStr='printItem.action?key='+record.data.workItemID;
					var synopsisClass='synopsis_blue newWindow';
					return '<a href="'+urlStr+'" class="'+synopsisClass+'" target="printItem'+record.data.workItemID+'">'+record.data['id']+'</a>';
				}*/
			}, {
				text : getText('field.label4'),
				width : 125,
				sortable : true,
				menuDisabled:true,
				dataIndex : 'status'
			}, {
				text : getText('field.label13'),
				width : 150,
				sortable : true,
				menuDisabled:true,
				dataIndex : 'originator'
			}, {
				text : getText('field.label6'),
				width : 150,
				sortable : true,
				menuDisabled:true,
				dataIndex : 'responsible'
			}, {
				text : getText('field.label17'),
				flex : 1,
				//width:250,
				sortable : true,
				menuDisabled:true,
				dataIndex : 'synopsis'
			} ],
			viewConfig : {
				stripeRows : true
			}
		});
		me.gridChildren.addListener('celldblclick',me.onGridChildrenItemDblClick,me);
		panel.add(me.gridChildren);
		me.childrenPanel=panel;
		return panel;
	},
	clickChildItemNumber:function(record,cellIndex){
		var me=this;
		var workItemID=record.data['workItemID'];
		me.fireEvent("clickOnChild",workItemID);
	},
	onGridChildrenItemDblClick:function(view, td,cellIndex,record, tr, rowIndex,e){
		var me=this;
		e.stopEvent();
		var workItemID=record.data['workItemID'];
		me.fireEvent("clickOnChild",workItemID);
		return false;
	},

	refreshItemPanel:function() {
		var me = this;
		var urlStr = 'item!executeAJAX.action';
		Ext.Ajax.request({
			url : urlStr,
			params : {
				'workItemID' : me.workItemID,
				'projectID' : me.projectID,
				'issueTypeID' : me.issueTypeID,
				'readOnlyMode' : me.readOnlyMode,
				'actionID' : me.actionID
			// +'&forwardAction='+forwardAction,
			},
			success : function(response) {
				var jsonData = Ext.decode(response.responseText);
				me.lastModified = jsonData.data.lastModified;
				me.replaceScreenItem.call(me, jsonData.data.screen,
						jsonData.data.children, jsonData.data.readOnlyMode,
						jsonData.data.workItemContext);
			},
			failure : function() {
				alert("failed!");
			}
		});
	},
	replaceScreenItem:function(
			screenData, children, readOnlyMode, workItemContext) {
		var me = this;
		var screenModel = com.trackplus.screen.createScreenModel(screenData);
		me.screenFacade = Ext.create('com.trackplus.screen.BaseScreenFacade', {
			screenModel : screenModel,
			readOnlyMode : readOnlyMode,
			showOneTab : true,
			tabViewCls:'com.trackplus.screen.ItemTabView',
			controllerCls : 'com.trackplus.screen.ItemScreenController',
			dataModel : workItemContext
		});
		var screenView = me.screenFacade.createViewComponent();
		if (CWHF.isNull(children)|| children.length === 0) {
			me.itemScreenPanel = screenView;
		} else {
			me.itemScreenPanel = Ext.create('Ext.container.Container', {
				layout:'anchor',
				defaults:{
					anchor:'100%'
				},
				//border : false,
				//autoScroll : true,
				items : [ screenView, me.createChildrenPanel(0, children) ]
			});
		}
		me.screenFacade.screenController.addListener('editMode',function(){
			me.fireEvent('editMode');
		});
		me.screenFacade.screenController.addListener('clickOnParent',function(parentID){
			me.fireEvent('clickOnParent',parentID);
		});
		me.screenFacade.screenController.addListener('lastModified',function(lastModified){
			if(me.itemDetailComponent){
				me.itemDetailComponent.lastModified=lastModified;
			}
			me.lastModified=lastModified;
			me.fireEvent('lastModified',lastModified);
		});
		me.screenFacade.screenController.addListener('finishedUpload',function(){
			if(me.itemDetailComponent){
				me.itemDetailComponent.refreshAttachments();
			}
		});
		me.itemPanel.removeAll(true);
		me.itemPanel.add(me.itemScreenPanel);
	},
	setParent:function(objectID, label,id){
		var me=this;
		var fieldTypeRenderersMap=me.screenFacade.screenController.fieldTypeRenderersMap;
		var fieldTypeRenderer=fieldTypeRenderersMap['f16'];
		if(fieldTypeRenderer){
			var fieldConfig=fieldTypeRenderer.fieldConfig;
			fieldConfig.jsonData={
				projectSpecificID:id,
				title:label
			};
			fieldTypeRenderer.update.call(fieldTypeRenderer,{
				value:objectID,
				fieldConfig:fieldConfig
			});
		}
	}
});
