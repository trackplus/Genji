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


//Item Action class
Ext.define('com.trackplus.item.ItemAction',{
	extend:'Ext.util.Observable',
	config: {
		workItemID:null,
		actionID:null,
		screenID:null,
		parentID:null,
		projectID:null,
		releaseID:null,
		issueTypeID:null,
		synopsis:null,
		description:null,
		successHandler:null,
		successExtra:null,
		readOnlyMode:false,
		scope:null,
		title:null,
		disabledNext:true,
		disabledPrev:true
	},
	lastModified:null,
	parentContainer:null,
	centerPanelCls:'itemscreen',
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		me.events=[];
		this.addEvents('activate','deactivate','close','itemChange','navigateToItem','clickOnChild','clickOnLink','clickOnParent');
		this.listeners = config.listeners;
		this.callParent(arguments);
	},
	execute:function(){
		var me=this;
		var urlStr='item!executeAJAX.action';
		me.setLoading(true);
		Ext.Ajax.request({
			url: urlStr,
			scope:me,
			params:{
				workItemID:me.workItemID,
				parentID:me.parentID,
				synopsis:me.synopsis,
				description:me.description,
				readOnlyMode:me.readOnlyMode,
				issueTypeID:me.issueTypeID,
				projectID:me.projectID,
				releaseID:me.releaseID,
				actionID:me.actionID
			},
			success: function(response){
				var dataAJAX=Ext.decode(response.responseText);
				me.setLoading(false);
				if(dataAJAX.success==true){
					me.loadSuccess.call(me,dataAJAX.data);
				}else{
					me.loadFailure.call(me,dataAJAX);
				}
			},
			failure:function(result){
				me.setLoading(false);
				Ext.MessageBox.show({
					title: getText('common.err.sorry'),
					msg: result.responseText,
					buttons: Ext.Msg.OK,
					icon: Ext.MessageBox.ERROR
				});
			}
		});
	},
	reExecute:function(){
		this.execute();
	},
	setLoading:function(b){
		borderLayout.setLoading(b);
	},
	createChildren:function(){
		var me=this;
		me.initToolbar('0 0 0 0');
		me.parentPanel= Ext.create('Ext.panel.Panel', {
			margin: '0 0 0 0',
			cls:me.centerPanelCls,
			bodyPadding:0,
			border: false,
			layout:'fit',
			region:'center'
		});
		var northPanel=Ext.create('Ext.panel.Panel',{
			border:false,
			bodyBorder:false,
			margin:'0 0 0 0',
			region:'north',
			layout: {
				type: 'hbox',
				padding:'0',
				align:'top'
			},
			items:[me.toolbar,me.extraToolbar]
		});
		return [northPanel,me.parentPanel];
	},


	initToolbar:function(){
		this.toolbar= Ext.create('Ext.toolbar.Toolbar', {
			layout: {
				overflowHandler: 'Menu'
			},
			enableOverflow: true,
			flex:1,
			cls:'toolbarActions',
			border: '1 0 1 0',
			defaults: {
				cls:'toolbarItemAction',
				overCls:'toolbarItemAction-over',
				scale:'small',
				iconAlign: 'left',
				enableToggle:false
			}
		});
		this.extraToolbar=Ext.create('Ext.toolbar.Toolbar', {
			enableOverflow: false,
			//region:'north',
			cls:'toolbarActions',
			border: '1 0 1 0',
			defaults: {
				cls:'toolbarItemAction',
				overCls:'toolbarItemAction-over',
				scale:'small',
				iconAlign: 'left',
				enableToggle:false
			}
		});
	},
	loadSuccess:function(data){
		var me=this;
		Ext.suspendLayouts();
		me.lastModified=data.lastModified;
		var useWizard=data.useWizard;
		if(me.toolbar!=null){
			me.toolbar.removeAll();
			me.toolbar.doLayout();
		}
		if(useWizard){
			me.title1=data.title1;
			me.title=data.title;
			me.openWizardItem.call(me,data);
		}else{
			me.title=data.title;
			me.openSimpleItem.call(me,data);
		}
		me.updateTitle.call(me,data);
		var toolbarData=data.toolbar;
		me.addToolbarButtons.call(me,toolbarData,useWizard);
		Ext.resumeLayouts(true);
	},
	loadFailure:function(response){
		if(response.localizedErrorKey!=null){
			CWHF.showMsgError(getText(response.localizedErrorKey));
		}
		if(response.message!=null){
			CWHF.showMsgError(response.message);
		}
		if(response.errorMessage!=null){
			CWHF.showMsgError(response.errorMessage);
		}
	},
	updateTitle:function(data){
	},
	openSimpleItem:function(data){
		var me=this;
		/*var panelItem=Ext.create('Ext.panel.Panel',{
		 layout:'fit',
		 border:false
		 });
		 me.parentPanel.removeAll(true);
		 me.parentPanel.add(panelItem);
		 me.parentPanel.doLayout();*/
		me.editItemScreen.call(me,data,me.parentPanel,true);
	},
	createParentContainer:function(){
		var me=this;
		return Ext.create('Ext.panel.Panel',{
			layout:'border',
			margin:'0 0 0 0',
			border:false,
			bodyBorder  :false,
			cls:'printItem',
			items:[{region:'center'}]
		});
	},


	addToolbarButtons:function(toolbarData, disableConfig){
		var me=this;
		if(me.toolbar!=null){
			if(toolbarData!=null){
				var items=[];
                var moreItems = [];
				for(var i=0;i<toolbarData.length;i++){
					if(me.isVisibleAction(toolbarData[i])){
                        var toolbarButtonConfig =  toolbarData[i];
                        var isMore = toolbarButtonConfig["isMore"];
                        var toolbarButton = me.createToolbarAction(toolbarButtonConfig);
                        if (isMore) {
                            moreItems.push(toolbarButton);
                        }  else {
                            items.push(toolbarButton);
                        }
					}
				}
				me.toolbar.add(items);
                if (moreItems.length>0) {
                    var moreItemActions=Ext.create('Ext.button.Split',{
                        text:getText('common.btn.moreActions'),
                        overflowText:getText('common.btn.moreActions'),
                        tooltip:getText('common.btn.moreActions.tt'),
                        iconCls: 'extraActions',
                        disabled:false,
                        menu:moreItems
                    });
                    me.toolbar.add(moreItemActions);
                }
			}
		}
		if(me.extraToolbar!=null){
			me.extraToolbar.removeAll();
			var items=[];
			if(me.actionID==-2){//printItem
				items.push("-");
				items.push(Ext.create('Ext.button.Button',{
					itemId:'prevItem',
					overflowText:getText('common.btn.backward'),
					tooltip:getText('common.btn.backward.tt'),
					iconCls: 'itemAction_upward16',
					cls:'toolbarItemAction-noText',
					disabled:me.disabledPrev,
					handler:function(){
						me.executeToolbarAction.call(me,{id:com.trackplus.item.ToolbarItem.NAVIGATION_PREV});
					},
					scope:me
				}));
				items.push("-");
				items.push(Ext.create('Ext.button.Button',{
					itemId:'nextItem',
					overflowText:getText('common.btn.forward'),
					tooltip:getText('common.btn.forward.tt'),
					iconCls: 'itemAction_downward16',
					cls:'toolbarItemAction-noText',
					disabled:me.disabledNext,
					handler:function(){
						me.executeToolbarAction.call(me,{id:com.trackplus.item.ToolbarItem.NAVIGATION_NEXT});
					},
					scope:me
				}));
			}

			if(com.trackplus.TrackplusConfig.user.sys==true){
				//or projectAdmin ?
				if(disableConfig==null||disableConfig==false){
					items.push("-");
					items.push(Ext.create('Ext.button.Button',{
						overflowText:getText('common.btn.config'),
						tooltip:getText('common.btn.config'),
						iconCls: 'btnConfig',
						cls:'toolbarItemAction-noText',
						handler:me.onScreenConfig,
						scope:me
					}));
				}
			}
			me.extraToolbar.add(items);
		}
	},
	setDisabledNextButton:function(disabledNext){
		var me=this;
		me.disabledNext=disabledNext;
		if(me.extraToolbar!=null){
			var btnNext=me.extraToolbar.getComponent('nextItem');
			if(btnNext!=null){
				btnNext.setDisabled(disabledNext);
			}
		}
	},
	setDisabledPrevButton:function(disabledPrev){
		var me=this;
		me.disabledPrev=disabledPrev;
		if(me.extraToolbar!=null){
			var btnPrev=me.extraToolbar.getComponent('prevItem');
			if(btnPrev!=null){
				btnPrev.setDisabled(disabledPrev);
			}
		}
	},
	onScreenConfig:function(){
		var me=this;
		var id=me.screenID;
		var urlStr='screenEdit.action?componentID='+id+'&backAction=itemNavigator.action%3FactionID%3D'+me.actionID;
		if(me.workItemID!=null){
			urlStr=urlStr+'%26workItemID%3D'+me.workItemID;
		}
		window.location.href=urlStr;
	},
	isVisibleAction:function(toolbarItem){
		var id=toolbarItem.id;
		if(id==com.trackplus.item.ToolbarItem.BACK){
			return false;
		}
		return true;
	},
	createToolbarAction:function(toolbarItem){
		var me=this;
		/*return Ext.create('Ext.button.Button',{
			xtype:'button',
			overflowText:getText(toolbarItem.labelKey),
			tooltip:getText(toolbarItem.tooltipKey),
			iconCls: toolbarItem.cssClass+'20',
			cls:'toolbarItemAction-noText',
			disabled:toolbarItem.condition==false,
			text:getText(toolbarItem.labelKey),
			handler:function(btn){
				me.executeToolbarAction.call(me,toolbarItem);
			}
		}); */
        return  {
            overflowText:getText(toolbarItem.labelKey),
            tooltip:getText(toolbarItem.tooltipKey),
            iconCls: toolbarItem.cssClass+'20',
            cls:'toolbarItemAction-noText',
            disabled:toolbarItem.condition==false,
            text:getText(toolbarItem.labelKey),
            handler:function(btn){
                me.executeToolbarAction.call(me,toolbarItem);
            }
        }
	},

	openWizardItem:function(data){
		var me=this;
		me.canFinish=data.canFinish;
		me.step1Renderer=Ext.create(data.extClassName);
		me.form1=me.step1Renderer.createFormPanel(data.jsonData);
		var panelStep2=Ext.create('Ext.form.Panel', {
			region: 'center',
			url: 'item!save.action',
			//standardSubmit : true,
			margins: '0 0 0 0',
			bodyPadding:0,
			border: false,
			baseCls:'x-plain',
			layout:'fit'
		});
		me.form2=panelStep2;
		me.btnPrev=Ext.create('Ext.Button',{
			text: /*'&laquo; '+*/getText('common.btn.backward'),
			iconCls:'itemAction_navigate_left',
			cls:'toolbarItemAction-noImage',
			disabled:true,
			scope:me,
			handler:function(btn){
				me.prev.call(me,data);
			}
		});
		me.btnNext=Ext.create('Ext.Button',{
			text: getText('common.btn.forward'),//+' &raquo;',
			iconCls:'itemAction_navigate_right',
			disabled:false,
			scope:me,
			formBind: true,
			iconAlign:'right',
			cls:'toolbarItemAction-noImage',
			handler:function(btn){
				me.next.call(me,data);
			}
		});
		me.btnFinish=Ext.create('Ext.Button',{
			text: data.finishLabel,
			iconCls:data.cssClass==null?'save':data.cssClass+'20',
			hidden :!data.canFinish,
			scope:me,
			handler:function(btn){
				me.finish.call(me,data);
			}
		});
		me.btnReset=Ext.create('Ext.Button',{
			text: 'Reset',
			scope:me,
			handler:function(btn){
				//
			}
		});
		me.btnCancel=Ext.create('Ext.Button',{
			text: 'Cancel',
			scope:me,
			handler:function(btn){
				if(me.dialogItemAction!=null){
					me.dialogItemAction.close();
					me.dialogItemAction.destroy();
				}
			}
		});
		me.centerPanelWizard =Ext.create('Ext.panel.Panel',{
			layout      : 'card',
			//plain       : true,
			border:false,
			bodyBorder:false,
			autoScroll  :true,
			//baseCls:'x-plain',
			defaults: {
				//baseCls:'x-plain',
				margins: '0 0 0 0',
				bodyStyle:{padding:'5px'}
			},
			items:[me.form1,panelStep2]
		});
		//me.itemToolbar.add(Ext.create('Ext.toolbar.Fill',{}));
		me.toolbar.add(me.btnPrev);
		me.toolbar.add(borderLayout.createItemToolbarSeparator());
		me.toolbar.add(me.btnNext);
		me.toolbar.add(borderLayout.createItemToolbarSeparator());
		me.toolbar.add(me.btnFinish);
		me.toolbar.doLayout();
		me.parentPanel.removeAll();
		me.parentPanel.add(me.centerPanelWizard);
		me.parentPanel.doLayout();
		//In case of Add linked item(from item navigator context menu), the form must contain link initialization part
		//otherwise normal new item create form
		if(me.addLinkFromContextMenu!=null) {
			me.addLinkDetailsToForm();
		}
	},

	/**
	 * Loading link form configuration data, when Add linked item (from item navigator context menu).
	 */
	addLinkDetailsToForm: function() {
		var me = this;
		var link=Ext.create('com.aurel.trackplus.itemDetail.LinksTab',{
			workItemID:me.workItemID,
			projectID:me.addLinkFromContextMenu.projectID,
			issueTypeID:me.addLinkFromContextMenu.issueTypeID
		});
		me.linkFormPanel = link.createFormPanel();
		me.linkFormPanel.getForm().load({
			url : 'itemLink!editItemLink.action',
			params:{workItemID:me.workItemID},
			scope: me,
			success : function(form, action) {
				var data=action.result.data;
				link.cmbLinkType.store.loadData(data["linkTypesList"],false);
				link.cmbLinkType.setValue(data["linkTypeWithDirection"]);
				link.txtIssueNo.setValue(me.workItemID);
				link.hiddenIssueNo.setValue(me.workItemID);
				link.txtSynopsis.setValue(me.addLinkFromContextMenu.workItemName);
				link.txtDescription.setValue("");
				link.replaceSpecificPart(me.linkFormPanel, data["linkTypeJSClass"], data["specificData"]);
			},
			failure:function(){
				alert("failure");
			}
		});
		//align to existing labels
		me.linkFormPanel.margin = '0 0 0 -12';
		me.form1.items.add(me.linkFormPanel);
	},

	prev:function(data){
		var me=this;
		me.navigate.call(me,'prev',data);
		var items=me.toolbar.items;
		while(items.getCount()>5){
			me.toolbar.remove(items.getAt(5));
		}
		if(me.extraToolbar!=null){
			me.extraToolbar.removeAll();
		}
		me.btnFinish.setVisible(me.canFinish);
		me.toolbar.doLayout();
	},

	next:function(data){
		var me=this;
		if(!me.form1.getForm().isValid()){
			return false;
		}
		var params = {};
		//In case of Add linked item  we need to append link config into submit parameters
		if(me.addLinkFromContextMenu!=null) {
			params = me.linkFormPanel.getForm().getValues();
		}
		params.workItemID=me.workItemID;
		params.parentID=me.parentID;
		params.synopsis=me.synopsis;
		params.description=me.description;
		params.actionID=me.actionID;
		me.setLoading(true);
		me.form1.getForm().submit({
			url:'item!next.action',
			params:params,
			success: function(form, action) {
				me.navigate.call(me,'next',action.result.data);
				me.btnFinish.setVisible(false);
				me.editItemScreen(action.result.data,me.form2,true);
				me.addToolbarButtons.call(me,action.result.data.toolbar);
				me.setLoading(false);
			},
			failure: function(form, action) {
				me.setLoading(false);
				me.step1Renderer.showErrorMessage(action.result,me.form1);
			}
		});
	},
	navigate:function(direction,data){
		var me=this;
		var layout = me.centerPanelWizard.getLayout();
		layout[direction]();
		me.btnPrev.setDisabled(!layout.getPrev());
		me.btnNext.setDisabled(!layout.getNext());
		if(!data.canFinish){
			me.btnFinish.setDisabled(layout.getNext());
		}
	},
	finish:function(){
		var me=this;
		var firstStep=me.centerPanelWizard.getLayout().getNext();
		if(firstStep==false){
			me.save.call(me);
		}else{
			me.saveInFirstStep.call(me);
		}
	},
	saveInFirstStep:function(){
		var me=this;
		var successExtra={};
		if(me.successExtra!=null){
			for(var x in me.successExtra){
				successExtra[x]=me.successExtra[x];
			}
			successExtra['actionID']=me.actionID;
			successExtra['workItemID']=me.workItemID;
		}
		me.form1.getForm().submit({
			url:'item!saveInFirstStep.action',
			params:{
				'workItemID':me.workItemID,
				'projectID':me.projectID,
				'issueTypeID':me.issueTypeID,
				'actionID':me.actionID//+'&forwardAction='+forwardAction,
			},
			success:function(form,action){
				me.saveSuccess.call(me,form,action);
			},
			scope:me,
			failure:me.saveFailure
		});
	},
	saveSuccess:function(form, action){
		var me=this;
		var data=null;
		if(action!=null){
			data=action.result.data;
		}
		if(me.successHandler!=null){
			me.successHandler.call(me.scope!=null?me.scope:me,data,me.successExtra);
		}
	},
	saveFailure:function(form, action){
		alert("failure");
	},
	itemChangeHandler:function(fields){
		var me=this;
		me.fireEvent('itemChange',fields);
	},
	clickOnChild:function(workItemID){
		var me=this;
		me.fireEvent('clickOnChild',workItemID);
	},
	clickOnLink:function(workItemID){
		var me=this;
		me.fireEvent('clickOnLink',workItemID);
	},
	clickOnParent:function(parentID){
		var me=this;
		me.fireEvent('clickOnParent',parentID);
	},

	changeToEditMode:function(){
		var me=this;
		var successExtra={};
		if(me.successExtra==null){
			me.successExtra={};
		}
		me.successExtra['actionID']=me.actionID;
		me.successExtra['workItemID']=me.workItemID;


		Ext.Ajax.request({
			url : "item!toolbarEdit.action",
			params : {
				'workItemID' : me.workItemID
			},
			success : function(response) {
				var jsonData = Ext.decode(response.responseText);
				var toolbarData=jsonData.data;
				var el=me.toolbar.getEl();
				el.fadeOut({
					opacity: 0.1, //can be any value between 0 and 1 (e.g. .5)
					easing: 'easeOut',
					duration: 150,
					remove: false,
					useDisplay: false,
					callback:function(){
						me.toolbar.removeAll();
						me.addToolbarButtons.call(me,toolbarData);
						el.fadeIn({
							opacity: 1, //can be any value between 0 and 1 (e.g. .5)
							easing: 'easeOut',
							duration: 300
						});
					}
				});
				me.actionID=2;//EDIT
				me.readOnlyMode=false;
			},
			failure : function() {
				alert("failed!");
			}
		});
	},
	changeLastModified:function(lastModified){
		var me=this;
		me.lastModified=lastModified;
	},
	editItemScreen:function(data,panel,tabsIncluded){
		var me=this;
		me.projectID=data.projectID;
		me.issueTypeID=data.issueTypeID;
		me.screenID=data.screen.id;
		me.itemComponent=Ext.create('com.trackplus.item.ItemComponent',{
			workItemID:data.workItemID,
			workItemIDDisplay:data.workItemIDDisplay,
			projectID:data.projectID,
			projectLabel:data.projectLabel,
			issueTypeID:data.issueTypeID,
			issueTypeLabel:data.issueTypeLabel,
			readOnlyMode:data.readOnlyMode,
			actionID:data.actionID,
			issueNoLabel:data.issueNoLabel,
			statusDisplay:data.statusDisplay,
			statusID:data.statusID,
			synopsis:data.synopsis,
			includeBottomTabs:data.includeBottomTabs,
			itemDetailData:data.itemDetail,
			includeItemTitle:false,
			synopsisReadonly:data.synopsisReadonly,
			inlineEdit:data.inlineEdit,
			lastModified:me.lastModified,
			itemLockedMessage:data.itemLockedMessage
		});
		me.itemComponent.addListener("editMode",me.changeToEditMode,me);
		me.itemComponent.addListener("lastModified",me.changeLastModified,me);
		me.itemComponent.addListener("itemChange",me.itemChangeHandler,me);
		me.itemComponent.addListener("clickOnChild",me.clickOnChild,me);
		me.itemComponent.addListener("clickOnLink",me.clickOnLink,me);
		me.itemComponent.addListener("clickOnParent",me.clickOnParent,me);

		me.readOnlyMode=data.readOnlyMode;
		var container=me.itemComponent.createItemPanel();
		panel.removeAll();
		panel.add(container);
		if(tabsIncluded){
			me.itemComponent.replaceScreenItem(data.screen,data.children,data.readOnlyMode,data.workItemContext);
		}else{
			me.itemComponent.refreshItemPanel();
		}
	},
	refreshChildren:function(){
		var me=this;
		if(me.itemComponent!=null){
			me.itemComponent.refreshChildren.call(me.itemComponent);
		}
	},
	executeToolbarAction:function(toolbarItem){
		var me=this;
		var id=toolbarItem.id;
		var url=toolbarItem.url;
		var jsonData=toolbarItem.jsonData;
		switch(id){
			case com.trackplus.item.ToolbarItem.ITEM_ACTION:
			case com.trackplus.item.ToolbarItem.SIBLING:
				me.executeItemAction.call(me,jsonData);
				break;
			case com.trackplus.item.ToolbarItem.CHOOSE_PARENT:
				me.chooseParent();
				break;
			case com.trackplus.item.ToolbarItem.PRINT:
				var url="printItem2.action?workItemID="+me.workItemID;
				window.open(url,'PrintItem','scrollbars=yes,resizable=yes,menubar=yes,location=yes,status=yes');
				break;
			case com.trackplus.item.ToolbarItem.PRINT_WITH_CHILDREN:
				var url="printItemWithChildren.action?workItemID="+me.workItemID;
				window.open(url,'PrintItem','scrollbars=yes,resizable=yes,menubar=yes,location=yes,status=yes');
				break;
				break;
			case com.trackplus.item.ToolbarItem.ACCESS_LEVEL:
				me.reverseAccessFlag();
				break;
			case com.trackplus.item.ToolbarItem.ARCHIVE:
				me.reverseArchive();
				break;
			case com.trackplus.item.ToolbarItem.DELETE:
				me.reverseDelete();
				break;
			case com.trackplus.item.ToolbarItem.MAIL:
				me.sendEmail();
				break;
			case com.trackplus.item.ToolbarItem.BACK:
				me.back();
				break;
			case com.trackplus.item.ToolbarItem.NAVIGATION_NEXT:
				me.navigateToItem.call(me,'next');
				break;
			case com.trackplus.item.ToolbarItem.NAVIGATION_PREV:
				me.navigateToItem.call(me,'prev');
				break;
			case com.trackplus.item.ToolbarItem.SAVE:
				me.save.call(me);
				break;
			case com.trackplus.item.ToolbarItem.RESET:
				me.reset();
				break;
			case com.trackplus.item.ToolbarItem.CANCEL:
				/*if(me.successHandler!=null){
					me.successHandler.call(me.scope!=null?me.scope:me,null,me.successExtra);
				}*/
				break;
			case com.trackplus.item.ToolbarItem.PRINT_ITEM:
				window.open("printItem.action?workItemID="+me.workItemID,'printItem'+me.workItemID);
				break;
			case com.trackplus.item.ToolbarItem.EXPORT:
				break;
		}
	},
	executeItemAction:function(jsonData){
		var me=this;
		var actionID=jsonData.actionID;
		var parentID=jsonData.parentID;
		if(me.successExtra==null){
			me.successExtra={};
		}
		me.successExtra['actionID']=me.actionID;
		me.successExtra['workItemID']=me.workItemID;

		me.actionID=actionID;
		me.parentID=parentID;
		me.reExecute();
		me.fireEvent.call(me,'activate',me);

		/*var itemAction=Ext.create('com.trackplus.item.ItemActionDialog',{
			workItemID:me.workItemID,
			actionID:actionID,
			parentID:parentID,
			successHandler:me.successHandler,
			successExtra:successExtra,
			scope:me.scope,
			modal:false
		});
		itemAction.execute.call(itemAction);
		itemAction.addListener('activate',function(){
			me.fireEvent.call(me,'activate',me);
		},me);
		itemAction.addListener('deactivate',function(){
			me.fireEvent.call(me,'deactivate',me);
		},me);
		itemAction.addListener('close',function(){
			me.fireEvent.call(me,'close',me);
		},me);*/

	},
	chooseParent:function(){
		var me=this;
		var workItemID=me.workItemID;
		var projectID=me.projectID;
		//var projectName="";
		var ajaxContext=null;
		var handler=null;
		var scope=null;
		if(me.readOnlyMode==true){
			if(me.successExtra==null){
				me.successExtra={};
			}
			me.successExtra['actionID']=me.actionID;
			me.successExtra['workItemID']=me.workItemID;
			ajaxContext={
				url:'item!setParent.action',
				params:{
					'workItemID':workItemID
				},
				pickItemName:'parentID',
				successHandler:me.saveSuccess,
				successHandlerScope:me
			};
		}else{
			handler=me.setParent;
			scope=me;
		}

		var issuePicker=Ext.create('com.trackplus.util.IssuePicker',{
			workItemID:workItemID,
			parent:true,
			projectID:projectID*-1,
			//projectName:projectName,
			title:getText('common.btn.chooseParent'),
			ajaxContext:ajaxContext,
			handler:handler,
			scope:scope
		});
		issuePicker.showDialog();
	},
	reverseAccessFlag:function(){
		this.reverseFlag('item!reverseAccessFlag.action?workItemID='+this.workItemID);
	},
	reverseArchive:function(){
		this.reverseFlag('item!reverseArchive.action?workItemID='+this.workItemID);
	},
	reverseDelete:function(){
		this.reverseFlag('item!reverseDelete.action?workItemID='+this.workItemID);
	},
	reverseFlag:function(urlStr){
		var me=this;
		Ext.Ajax.request({
			url: urlStr,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				if (responseJson.success==true) {
					me.reExecute();
				}else{
					if (responseJson.errorMessage!=null) {
						//parent change for an issue
						Ext.MessageBox.show({
							title: getText('common.warning'),
							msg: responseJson.errorMessage,
						    buttons: Ext.Msg.OK,
						    icon: Ext.MessageBox.ERROR
						});
					}
				}
			},
			failure:function(){
				var jsonData=Ext.decode(action.response.responseText);
				me.handleErrors.call(me,jsonData.data);
			}
		});
	},
	sendEmail:function(){
		var me=this;
		var sendItemEmail=Ext.create('com.trackplus.item.SendEmail',{
			workItemID:me.workItemID
		});
		sendItemEmail.show();
	},
	back:function(){
	},

	getMyPosition:function(){
		return null;
	},
	navigateToItem:function(direction){
		var me=this;
		me.fireEvent('navigateToItem',me,direction);
	},
	reset:function(){
		var me=this;
		var itemFormPanel=me.itemComponent.itemPanel;
		itemFormPanel.getForm().reset();
	},
	setLoading:function(b){
		var me=this;
		var itemFormPanel=me.itemComponent.itemPanel;
		itemFormPanel.setLoading(b);
	},
	save:function(confirm){
		var me=this;
		var synopsis=me.itemComponent.txtTitleHeader.getValue();
		var itemFormPanel=me.itemComponent.itemPanel;
		var fieldTypeRenderersMap=me.itemComponent.screenFacade.controller.fieldTypeRenderersMap;
		var parentID=me.itemComponent.screenFacade.controller.dataModel.fieldValues['f16'];
		var parentOnscreen=false;
		var fieldTypeRenderer;
		for(var x in fieldTypeRenderersMap){
			if(x=='f16'){
				parentOnscreen=true;
			}
			fieldTypeRenderer=fieldTypeRenderersMap[x];
			fieldTypeRenderer.beforeSubmit.call(fieldTypeRenderer);
		}
		if(!itemFormPanel.getForm().isValid()){
			CWHF.showMsgError(getText('item.err.invalidForm'));
			var firstInvalid = itemFormPanel.getForm().getFields().findBy(function(f){return !f.validate();});
			if (firstInvalid) {
				firstInvalid.focus();
			}
			return false;
		}
		var lastModified=me.lastModified;
		var parentID=me.itemComponent.screenFacade.controller.dataModel.fieldValues['f16'];
		var params={
			confirm: confirm,
			'workItemID':me.workItemID,
			'projectID':me.projectID,
			'issueTypeID':me.issueTypeID,
			'lastModified':lastModified,
			'fieldValues.f17':synopsis,
			'actionID':me.actionID//+'&forwardAction='+forwardAction,
		};
		//include parent to submit
		if(parentID!=null&&parentOnscreen==false){
			params['fieldValues.f16']=parentID;
		}
		me.setLoading(true);
		itemFormPanel.getForm().submit({
			url:'itemSave.action',
			params:params,
			success: function(form, action) {
				me.setLoading(false);
				me.saveSuccess.call(me,form, action);
			},
			failure: function(form, action) {
                me.setLoading(false);
                var jsonData = null;
                var errorCode = null;
                var errors = null;
                if (action.response!=null) {
                   var responseText = action.response.responseText;
                   if (responseText!=null) {
                       jsonData = Ext.decode(responseText);
                       if (jsonData!=null && jsonData.data!=null) {
                           errorCode = jsonData.data.errorCode;
                           errors = jsonData.data.errors;
                       }
                   }
                }
                if (errorCode==3 && errors!=null) {
                    //conformation needed
                    var errorMessage = "";
                    Ext.Array.forEach(errors, function (error) {
                        errorMessage += error.label;
                    }, me);
                    Ext.MessageBox.confirm(getText("common.confirm"),
                        errorMessage,
                        function(btn){
                            if (btn=="no") {
                                var jsonData=Ext.decode(action.response.responseText);
                                me.handleErrors.call(me,jsonData.data);
                            } else {
                                me.save.call(me, true);
                            }
                        }, this);
                }  else {
                    me.handleErrors.call(me,jsonData.data);
                }
			}
		});
	},
	setParent:function(item){
		var me=this;
		var key=item['objectID'];
		var id=item['id'];
		me.itemComponent.setParent.call(me.itemComponent,key,item['title'],id);
	},
	reload:function(item){
		var me=this;
		me.itemComponent.refreshItemPanel.call(me.itemComponent);
		if(me.successHandler!=null){
			me.successHandler.call(me.scope!=null?me.scope:me);
		}
	},
	handleErrors:function(data){
		var me=this;
		com.trackplus.item.ItemErrorHandler.handleErrors(me,data);
	}
});
