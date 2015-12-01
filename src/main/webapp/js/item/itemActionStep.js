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

Ext.define('com.trackplus.item.action.StepRenderer',{
	extend:'Ext.Base',
	config: {
		issueNoLabel:'',
		workItemID:'',
		statusDisplay:'',
		synopsis:''
	},
	labelWidth:150,
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		this.initConfig(config);
	},
	createFormPanel:function(data){
		return Ext.create('Ext.form.Panel',{
			padding: '5 5 5 5',
			bodyPadding:0
		});
	},
	showErrorMessage:function(data,form){
		CWHF.showMsgError(getText(data.localizedErrorKey));
	},
	createItemTitleHtml:function(issueNoLabel, workItemID,statusDisplay, synopsis) {
		return issueNoLabel + '<strong><span class="emphasize">&nbsp;'
			+ workItemID + '</span> : ' + '<span class="dataEmphasize">'
			+ statusDisplay + '</span>' + ' :&nbsp;' + synopsis + '</strong>';
	},
	createItemTitleSection:function(issueNoLabel, workItemID,statusDisplay, synopsis, region) {
		var htmlIssueNr = this.createItemTitleHtml(issueNoLabel,
			workItemID, statusDisplay, synopsis, region);
		return {
			xtype : 'box',
			// baseCls:'x-panel-body-default',
			style : {
				border : 'none',
				paddingTop : '2px'
			},
			region : region,
			margin : '0 0 0 0',
			border : false,
			height : 22,
			html : htmlIssueNr
		};
	}
});

Ext.define('com.trackplus.item.action.CopyItem1StepRenderer',{
	extend:'com.trackplus.item.action.StepRenderer',
	createFormPanel:function(data){
		var titleArea=this.createItemTitleSection(data.issueNoLabel,data.workItemID,data.statusDisplay,data.synopsis);
		return Ext.create('Ext.form.Panel', {
			region: 'center',
			layout:'anchor',
			padding: '5 5 5 5',
			bodyPadding:0,
			border:false,
			url: 'item!saveInFirstStep.action',
			items:[titleArea,
				{
					xtype:'checkboxgroup',
					columns: 1,
					labelWidth: 1,
					items: [
						{boxLabel:getText('item.action.copy.deepCopy'),inputValue : true, name: 'params.deepCopy',checked:data.deepCopy},
						{boxLabel:getText('item.action.copy.copyAttachments'),inputValue : true, name: 'params.copyAttachments', checked:data.copyAttachments},
						{boxLabel:getText('item.action.copy.copyChildren'),inputValue : true, name: 'params.copyChildren',checked:data.copyChildren,disabled:!data.hasChildren}
					]
				}
			]
		});
	}
});

Ext.define('com.trackplus.item.action.ItemLocationStepRenderer',{
	extend:'com.trackplus.item.action.StepRenderer',
	cmbIssueType:null,
	createCmbIssueType:function(jsonData){
		var me=this;
		var dsIssueType = Ext.create('Ext.data.Store', {
			fields	: [{name:'id', type:'int'}, {name:'label', type:'string'}],
			data:jsonData.issueTypeList
		});
		var cmbIssueType=Ext.create('Ext.form.ComboBox',{
			id: 'IssueTypeList',
			fieldLabel: jsonData.issueTypeLabel,
			store:dsIssueType,
			displayField : 'label',
			valueField: 'id',
			typeAhead:false,
			queryMode: 'local',
			triggerAction: 'all',
			name: 'params.issueTypeID',
			allowBlank :false,
			editable: false,
			disabled :(jsonData.fixedIssueType===true),
			//anchor:'100%',//anchor width by percentage
			hiddenId:'issueTypeID',
			labelWidth:me.labelWidth,
			labelAlign:'right',
			width:400
		});
		cmbIssueType.setValue(jsonData.issueTypeID);
		return cmbIssueType;
	},
	createFormPanel:function(jsonData){
		var me=this;
		me.jsonData = jsonData;
		var urlStr='item!next.action';
		me.form=Ext.create('Ext.form.Panel', {
			border:false,
			bodyBorder:false,
			region: 'center',
			url:urlStr,
			padding: '5 5 5 5',
			bodyPadding:0,
			layout:'anchor',
			items:me.createFormItems(jsonData)
		});
		return me.form;
	},
	createFormItems:function(jsonData){
		var me=this;
		if(CWHF.isNull(jsonData)){
			return[{
				xtype:'component',
				html:'no right to create items'
			}];
		}
		var listenerConfig=null;
		if(jsonData.fixedIssueType===true){
			listenerConfig=null;
		}else{
			listenerConfig=	{select:{fn: this.projectChange, scope:this}};
		}
		me.cmbProject = CWHF.createSingleTreePicker(jsonData.projectLabel,
			"params.projectID", jsonData["projectTree"], jsonData["projectID"],
			{
				allowBlank:true,
				labelIsLocalized:true,
				labelWidth: this.labelWidth,
				width:400,
				margin:'0 0 5 0'
			},listenerConfig );
		var items=new Array();
		me.cmbIssueType=me.createCmbIssueType(jsonData);
		var items=new Array();
		items.push(me.cmbProject);
		items.push(me.cmbIssueType);
		if(jsonData.fixedIssueType===true){
			items.push({
				xtype: 'hiddenfield',
				name: 'params.issueTypeID',
				value:jsonData.issueTypeID
			})
		}

		return items;
	},
	projectChange:function(){
		var me=this;
		var urlStr='item!reloadIssueTypes.action';
		var projectID=me.cmbProject.getSubmitValue();
		var issueTypeID=me.cmbIssueType.getValue();
		me.form.setLoading(true);
		Ext.Ajax.request({
			url: urlStr,
			disableCaching :true,
			params:{
				projectID:projectID,
				issueTypeID:issueTypeID,
				workItemID: me.jsonData.parentID
			},
			success: function(result){
				var jsonData=Ext.decode(result.responseText);
				me.cmbIssueType.store.loadData(jsonData.issueTypeList);
				me.cmbIssueType.setValue(jsonData.issueTypeID);
				me.cmbIssueType.setFieldLabel(jsonData.issueTypeLabel);
				me.form.setLoading(false);
			},
			failure: function(error){
				me.form.setLoading(false);
				alert("error refresh issue type: "+error.message);
			},
			method:"POST"
		});
	}
});

Ext.define('com.trackplus.item.action.MoveItemStepRenderer',{
	extend:'com.trackplus.item.action.ItemLocationStepRenderer',

	createOldLocationPanel:function(jsonData){
		var htmlStr=jsonData.projectLabel+': <b>'+jsonData.oldProject+'</b> &nbsp;';
		htmlStr+=jsonData.issueTypeLabel+': <b>'+jsonData.oldIssueType+'</b>';
		return {
			xtype: 'component',
			html: htmlStr,
			anchor: '100%',
			cls:'infoBox',
			margin:'0 0 10 0',
			border:true
		};
	},
	createCmbStatus:function(jsonData){
		var dsStatus = Ext.create('Ext.data.Store', {
			model: 'Status',
			fields : [{name:'id', type:'int'}, {name:'label', type:'string'}],
			data:jsonData.statusList
		});
		var cmbStatus=Ext.create('Ext.form.ComboBox',{
			id: 'StatusList',
			fieldLabel: jsonData.statusLabel,
			store:dsStatus,
			displayField : 'label',
			valueField: 'id',
			typeAhead:false,
			queryMode: 'local',
			triggerAction: 'all',
			name: 'params.statusID',
			allowBlank :false,
			//anchor:'100%',//anchor width by percentage
			hiddenId:'statusID'
		});
		cmbStatus.setValue(jsonData.statusID);

		return cmbStatus;
	},
	createFormItems:function(jsonData){
		var me=this;
		var items=me.superclass.createFormItems.call(me,jsonData.issueLocation);
		items.unshift(me.createOldLocationPanel(jsonData));
		var titleArea=me.createItemTitleSection(jsonData.issueNoLabel,jsonData.workItemID,jsonData.statusDisplay,jsonData.synopsis);
		items.unshift(titleArea);
		if (jsonData.statusNeeded){
			items.push(me.createCmbStatus(jsonData));
		}
		me.moveChildren = CWHF.createCheckbox("item.action.move.moveChildren", "params.moveChildren", {value:jsonData.moveChildren, labelWidth:150, disabled:!jsonData.hasChildren});
		items.push(me.moveChildren);
		return items;
	}
});
