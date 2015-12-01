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


Ext.define('com.aurel.trackplus.itemDetail.LinksTab',{
	extend:'com.aurel.trackplus.itemDetail.TabGrid',
	urlDeleteItems:'itemLink!deleteLinks.action',
	fieldsToNotifyOnDelete:[19,20,-1009],
	readOnly:false,
	cmbLinkType:null,
	formPanel:null,
	//the visible item number: either global or project specific
	txtIssueNo:null,
	//the global item number
	hiddenIssueNo:null,
	txtSynopsis:null,
	keepSelection: false,
	btnAddLink:null,btnDeleteLink:null,btnEditLink:null,

	initComponent : function(){
		var me = this;
		var linkNumber=0;
		if(me.jsonData.linkNumber){
			linkNumber=me.jsonData.linkNumber;
		}
		me.title=getText('item.printItem.lbl.tab.links')+" ("+linkNumber+")";
		// me.iconCls='links16';
		me.gridConfig=me.createLinkConfig();
		me.callParent();
	},
	refreshCallback:function(r,options,success){
		var me=this;
		var size=r.length;
		me.setTitle(me.replaceTitleNumber(me.title,size));
		me.onSelectionChange(me.gridConfig.grid, me.gridConfig.grid.getSelectionModel().getSelection());
	},
	createLinkConfig:function(){
		var me=this;
		if(me.jsonData){
			me.readOnly=me.jsonData.readOnly;
		}
		var gridConfig=new com.trackplus.itemDetail.GridConfig();
		gridConfig.tabID="linksTab";
		gridConfig.id=7;
		gridConfig.urlStore='itemLink.action?workItemID='+me.workItemID+"&projectID="+me.projectID+"&issueTypeID="+me.issueTypeID;
		gridConfig.fields=[
			{name: 'id',type:'int'},
			{name: 'linkTypeID',type:'int'},
			{name: 'linkType',type:'string'},
			{name: 'workItemID',type:'int'},
			{name: 'itemID',type:'string'},
			{name: 'itemTitle',type: 'string'},
			{name: 'itemStatus',type: 'string'},
			{name: 'itemResponsible',type: 'string'},
			{name: 'parameters',type:'string'},
			{name: 'comment',type:'string'},
			{name: 'editable', type: 'boolean'}
		];
		me.btnAddLink=Ext.create('Ext.button.Button',{
			text:getText('common.btn.add'),
			disabled:me.readOnly,
			iconCls:'add16',
			tooltip:getText('item.tabs.itemLink.lbl.add'),
			handler:function(){
				me.addLink.call(me);
			}
		});
		me.btnDeleteLink=Ext.create('Ext.button.Button',{
			text:getText('common.btn.delete'),
			itemId:"deleteLinkBtn",
			disabled:true,
			iconCls:'delete16',
			handler:function(){
				me.deleteItems.call(me);
			}
		});
		me.btnEditLink=Ext.create('Ext.button.Button',{
			text:getText('common.btn.edit'),
			itemId:"editLinkBtn",
			disabled:true,
			iconCls:'edit16',
			handler:function(){
				me.editLink.call(me);
			}
		});
		me.btnUpLink=Ext.create('Ext.button.Button',{
			text:getText("common.btn.up"),
			tooltip: getText("item.tabs.itemLink.lbl.moveUp"),
			itemId:"moveUpBtn",
			disabled:true,
			iconCls:"moveUp",
			handler:function(){
				me.onMoveUpGridRow.call(me);
			}
		});
		me.btnDownLink=Ext.create('Ext.button.Button',{
			text:getText("common.btn.down"),
			tooltip: getText("item.tabs.itemLink.lbl.moveDown"),
			itemId:"moveDownBtn",
			disabled:true,
			iconCls:"moveDown",
			handler:function(){
				me.onMoveDownGridRow.call(me);
			}
		});
		gridConfig.tbar=[me.btnAddLink,me.btnDeleteLink,me.btnEditLink,me.btnUpLink,me.btnDownLink];
		gridConfig.dblClickHandler=me.editLink;
		gridConfig.selectionModel = Ext.create('Ext.selection.CheckboxModel', {
			listeners: {
				selectionchange: { fn : this.onSelectionChange,
						scope : me}
			}
		});
		gridConfig.updateColCfg=function(colCfg,layout,index){
			if(layout.dataIndex==='itemID'||layout.dataIndex==='itemTitle'){
				colCfg.xtype='linkcolumn';
				colCfg.handler=me.clickOnLinkItem;
				colCfg.scope=me;
				return 'Ext.ux.LinkColumn';
			}
			return 'Ext.grid.column.Column';
		};
		return gridConfig;
	},

	getViewConfig: function() {
		return {
				stripeRows: true,
				plugins: {
					ptype: "gridviewdragdrop",
					dragGroup: "itemLink" + "gridDDGroup",
					dropGroup: "itemLink" + "gridDDGroup",
					enableDrag: true,
					enableDrop: true
				},
				listeners: {
					drop: {scope:this, fn: function(node, data, dropRec, dropPosition) {
						this.onGridDrop(node, data, dropRec, dropPosition);
						}
					}
				}
		};
	},

	/**
	 * Method called on selection change
	 */
	onSelectionChange: function(sm, selections) {
		var me=this;
		if(CWHF.isNull(selections)||selections.length===0){
			me.btnDeleteLink.setDisabled(true);
			me.btnEditLink.setDisabled(true);
			me.btnUpLink.setDisabled(true);
			me.btnDownLink.setDisabled(true);
		}else{
			if(selections.length===1) {
				var rowData=selections[0].data;
				var editable = rowData.editable;
				me.btnEditLink.setDisabled(!editable);
				me.btnDeleteLink.setDisabled(!editable);
				var grid = me.gridConfig.grid;
				var store = grid.getStore();
				if (store) {
					//the records are not equal after reloading the store, so it is tested for id equality
					me.btnDownLink.setDisabled(rowData.id===store.last().data.id || !editable);
					me.btnUpLink.setDisabled(rowData.id===store.first().data.id || !editable);
				}
			}else{
				me.btnEditLink.setDisabled(true);
				me.btnUpLink.setDisabled(true);
				me.btnDownLink.setDisabled(true);
				var enableDelete=false;
				var enableMoveUp = true;
				var enableMoveDown = true;
				var grid = me.gridConfig.grid;
				var store = grid.getStore();
				for(var i=0;i<selections.length;i++){
					var record = selections[i];
					var rowData=record.data;
					if (rowData.editable===true) {
						enableDelete=true;
						if (rowData.id===store.first().data.id) {
							enableMoveUp = false;
						}
						if (rowData.id===store.last().data.id) {
							enableMoveDown = false;
						}
						//break;
					} else {
						enableMoveUp = false;
						enableMoveDown = false;
						break;
					}
				}
				me.btnDeleteLink.setDisabled(!enableDelete);
				me.btnUpLink.setDisabled(!enableMoveUp);
				me.btnDownLink.setDisabled(!enableMoveDown);
			}
		}
	},

	getSelectedIDs:function(){
		var me=this;
		var ids=null;
		var grid = me.gridConfig.grid;
		var selections=grid.getSelectionModel().getSelection();
		if (selections && selections.length>0) {
			ids=[];
			for(var i=0;i<selections.length;i++){
				ids.push(selections[i].data["id"]);
			}
		}
		return ids;
	},

	clickOnLinkItem:function(record,cellIndex){
		var me=this;
		var workItemID=record.data['workItemID'];
		me.fireEvent("clickOnLink",workItemID);
	},
	createFormPanel:function(linkID){
		var me=this;
		me.cmbLinkType = CWHF.createCombo("item.tabs.itemLink.lbl.thisIssue", "linkTypeWithDirection",
			{labelWidth:150, anchor:'100%', idType:"string"}, {select:{fn:me.selectLinkType, scope:me, linkID:linkID, workItemID:me.workItemID}});
		me.txtIssueNo = CWHF.createTextField(null, "linkedNumber", {itemId:'linkedNumber', width: 100,margin:'0 5 0 0', readOnly:true/*, allowBlank:false*/});
		me.hiddenIssueNo = CWHF.createHiddenField("linkedWorkItemID",{itemId:"linkedWorkItemID"});
		me.txtSynopsis = CWHF.createTextField(null, "linkedWorkItemTitle", {itemId:'linkedWorkItemTitle', margin:'0 10 0 0', readOnly:true, columnWidth:1/*, allowBlank:false*/});
		me.txtDescription = CWHF.createTextAreaField("item.tabs.itemLink.lbl.comment", "description", {height:125, anchor:'100%', labelWidth:150});
		var btnSearch={
			xtype: 'button',text:getText('common.btn.search'),
			handler:function(){
				me.chooseItem.call(me);
			}
		};
		var urlStr= 'itemLink!saveItemLink.action';
		if(linkID){
			urlStr=urlStr+'?linkID='+linkID;
		}
		me.formPanel=Ext.create('Ext.form.Panel', {
			itemId :'editLink',
			region:'center',
			layout:'anchor',
			url: urlStr,
			autoScroll:true,
			border : false,
			margins: '0 0 0 0',
			bodyStyle:{
				padding:'10px'
			},
			items:[
				me.cmbLinkType,
				{
					xtype: 'fieldcontainer',
					itemId: "linkedItem",
					fieldLabel: getText('item.tabs.itemLink.lbl.issue'),
					labelWidth:150,
					labelStyle:"overflow:hidden;",
					labelAlign:'right',
					layout:'column',
					anchor:'100%',
					defaults: {
						hideLabel: true
					},
					items: [me.hiddenIssueNo, me.txtIssueNo, me.txtSynopsis, btnSearch]
				},me.txtDescription
			]
		});
		return me.formPanel;
	},
	chooseItem:function(){
		var me=this;
		var workItemID=me.workItemID;
		var projectID=me.projectID;
		var issuePicker=Ext.create('com.trackplus.util.IssuePicker',{
			workItemID:workItemID,
			projectID:projectID*-1,
			title:getText("common.btn.chooseItem"),
			handler:me.setIssue,
			scope:me
		});
		issuePicker.showDialog();
	},

	selectLinkType: function(combo, records, options) {
		Ext.Ajax.request({
			url:"itemLink!getSpecificPart.action",
			params:{"linkTypeWithDirection":combo.getValue(),"linkID":options["linkID"], "workItemID":options["workItemID"]},
			disableCaching:true,
			scope: this,
			success: function(response, opts){
				var responseJson = Ext.decode(response.responseText);
				this.replaceSpecificPart(this.formPanel, responseJson["linkTypeJSClass"], responseJson["specificData"]);
			},
			failure: function(response, opts) {
			},
			method:'POST'
		});
	},

	setIssue:function(item){
		var me=this;
		var key=item['id'];
		var synopsis=item['title'];
		me.txtIssueNo.setValue(key);
		me.hiddenIssueNo.setValue(item["objectID"]);
		me.txtSynopsis.setValue(synopsis);
	},

	addLink:function() {
		var me=this;
		var dialogCfg= new com.trackplus.itemDetail.DialogConfig(me.workItemID,me.projectID,me.issueTypeID);
		dialogCfg.title=getText("item.tabs.itemLink.lbl.add");
		var formPanel=me.createFormPanel();
		dialogCfg.formPanel=formPanel;
		dialogCfg.w=700;
		dialogCfg.h=330;
		dialogCfg.autoScroll=false;
		dialogCfg.iconCls='links16';
		formPanel.getForm().load({
			url : 'itemLink!editItemLink.action',
			params:{workItemID:me.workItemID},
			scope: me,
			success : function(form, action) {
				var data=action.result.data;
				me.cmbLinkType.store.loadData(data["linkTypesList"],false);
				me.cmbLinkType.setValue(data["linkTypeWithDirection"]);
				this.replaceSpecificPart(formPanel, data["linkTypeJSClass"], data["specificData"]);
			},
			failure:function(){
				alert("failure");
			}
		});
		dialogCfg.successHandler=function(result){
			//STARTDATE = 19;
			//ENDDATE = 20;
			//LINKED_ITEMS = -1009
			//me.fireItemChange([19,20,-1009]);
			me.refresh.call(me);
		};
		com.trackplus.itemDetail.openDialog(dialogCfg);
	},

	/**
	 * Add the link type specific configuration
	 */
	replaceSpecificPart: function(panel, specificLinkTypeClass, specificData) {
		var specificPart = panel.getComponent("specificPart");
		if (specificPart) {
			specificPart.setDisabled(true);
			panel.remove(specificPart, true);
		}
		if (specificLinkTypeClass) {
			var specificPart = Ext.create(specificLinkTypeClass,{
				margin:'0 5 5 0'
			});
			//specificItem.setTitle(getText('admin.customize.field.config.detail.lbl.specificConfig'));
			if (specificPart) {
				//panel.add(specificItem);
				panel.insert(2, specificPart);
				//specificData["labelWidth"] = 100;
				specificPart.onDataReady(specificData);
				panel.updateLayout();
			}
		}
	},

	editLink:function(){
		var me=this;
		var selections=me.gridConfig.grid.getSelectionModel().getSelection();
		if(CWHF.isNull(selections)){
			return;
		}
		var rowData=selections[0].data;
		var linkID=null;
		if(rowData){
			linkID=rowData.id;
		}
		if(rowData.editable===false){
			return;
		}
		var dialogCfg= new com.trackplus.itemDetail.DialogConfig(me.workItemID,me.projectID,me.issueTypeID);
		dialogCfg.title=getText("item.tabs.itemLink.lbl.edit");
		var formPanel=me.createFormPanel(linkID);
		dialogCfg.formPanel=formPanel;
		dialogCfg.w=700;
		dialogCfg.h=330;
		dialogCfg.autoScroll=false;
		dialogCfg.iconCls='links16';
		formPanel.getForm().load({
			url : 'itemLink!editItemLink.action',
			params:{workItemID:me.workItemID,linkID:linkID},
			scope: me,
			success : function(form, action) {
				var data=action.result.data;
				me.cmbLinkType.store.loadData(data["linkTypesList"],false);
				me.cmbLinkType.setValue(data["linkTypeWithDirection"]);
				me.txtIssueNo.setValue(data["linkedWorkItemID"]);
				me.hiddenIssueNo.setValue(data["linkedWorkItemObjectID"]);
				me.txtSynopsis.setValue(data["linkedWorkItemTitle"]);
				me.txtDescription.setValue(data["description"]);
				this.replaceSpecificPart(formPanel, data["linkTypeJSClass"], data["specificData"]);
			},
			failure:function(){
				alert("failure");
			}
		});
		dialogCfg.successHandler=function(result){
			//STARTDATE = 19;
			//ENDDATE = 20;
			//LINKED_ITEMS = -1009
			//me.fireItemChange([19,20,-1009]);
			me.refresh.call(me);
		};
		dialogCfg.failureHandler=function(form, action){
			var violationError = action.result.errors['itemov.ganttView.dependency.violation'];
			if(violationError  || violationError !== undefined) {
				var title = getText("itemov.ganttView.dependency.violationTitle");
				Ext.Msg.show({
					title: title,
					msg: action.result.errors['itemov.ganttView.dependency.violation'],
					icon: Ext.Msg.ERROR,
					buttonText: {ok: getText("common.btn.ok")}
				});
			}
			var msg=action.result.errorMessage;
			if(CWHF.isNull(msg)||msg===''){
				msg=getText('common.err.failure.validate');
			}
			CWHF.showMsgError(msg);
		};
		com.trackplus.itemDetail.openDialog(dialogCfg);
	},

	onGridDrop: function(node, data, dropRec, dropPosition) {
		var before = false;
		if (dropPosition==="before") {
			before = true;
		}
		var draggedLinkIDs = this.getSelectedIDs();
		if (draggedLinkIDs) {
			var params = {workItemID: this.workItemID, draggedLinkIDs:draggedLinkIDs.join(), droppedToLinkID:dropRec.get("id"), before:before};
			this.onOrderChange("itemLink!droppedNear.action", params);
		}
	},

	/**
	 * Move the selected grid row up
	 */
	onMoveUpGridRow: function() {
		var draggedLinkIDs = this.getSelectedIDs();
		if (draggedLinkIDs) {
			this.onOrderChange("itemLink!moveUp.action", {workItemID: this.workItemID, draggedLinkIDs:draggedLinkIDs.join()});
		}
	},

	/**
	 * Move the selected grid row down
	 */
	onMoveDownGridRow: function() {
		var draggedLinkIDs = this.getSelectedIDs();
		if (draggedLinkIDs) {
			this.onOrderChange("itemLink!moveDown.action", {workItemID: this.workItemID, draggedLinkIDs:draggedLinkIDs.join()});
		}
	},

	/**
	 * Private function foe changing the order by drag and drop or move up/down
	 */
	onOrderChange: function(url, params) {
		Ext.Ajax.request({
			url: url,
			disableCaching: true,
			scope: this,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				if (responseJson.success !== true) {
					Ext.MessageBox.alert(this.failureTitle, responseJson.errorMessage);
				}
				if (this.workItemID) {
					this.keepSelection = true;
				}
				this.refresh.call(this);
				if (this.workItemID) {
					this.keepSelection = false;
				}
			},
			failure: function(reponse){
				Ext.MessageBox.alert(this.failureTitle, reponse.responseText);
			},
			isUpload: false,
			method:'POST',
			params: params
		});
	}

});
