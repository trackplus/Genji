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


Ext.define('com.trackplus.util.IssuePicker',{
	extend:'Ext.Base',
	config: {
		workItemID:null,
		parent:false,
		projectID:null,
		//projectName:null,
		title:"",
		handler:null,
		scope:null,
		ajaxContext:null,
		width:600,
		height:400,
		stateId:null
	},
	controller:null,
	constructor : function(config) {
		var me = this
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		me.controller=Ext.create('com.trackplus.util.IssuePickerController',{
			workItemID:me.workItemID,
			parent:me.parent,
			selectedProjectOrRelease:me.projectID,
			//selectedProjectOrReleaseName:me.projectName,
			title:me.title,
			handler:me.handler,
			scope:me.scope,
			ajaxContext:me.ajaxContext,
			width:me.width,
			height:me.height,
			stateId:me.stateId
		});
	},
	showDialog:function(){
		var me=this;
		me.controller.showDialog.call(me.controller)
	}
});

Ext.define('com.trackplus.util.IssuePickerView',{
	extend: 'Ext.panel.Panel',
	config:{
		controller:null,
		selectedProjectOrReleaseName:null,
		selectedProjectOrRelease:null,
		selectedQueryName:null,
		selectedQueryID:null,
		selectedDatasourceType:1
	},
	margin: '0 0 0 0',
	border: false,
	bodyBorder:false,
	baseCls:'x-plain',
	layout:'border',
	bodyStyle:{
		padding:'0px 0px 0px 0px'
	},
	initComponent: function(){
		var me=this;
		if(me.selectedDatasourceType==null){
			me.selectedDatasourceType=1;
		}
		me.items=me.createChildren();
		me.callParent();
	},
	createChildren:function(){
		var me=this;
		var itemsNorth=new Array();
		me.dataSourceType=CWHF.getRadioGroup(null, null, 500,
				[{boxLabel: getText('common.lbl.projectRelease'), name: 'dataSourceType',
						inputValue: '1', checked: true},
                 {boxLabel:getText('common.lbl.filter'), name: 'dataSourceType', inputValue: '2'}],
				{anchor:'100%',style:{ marginLeft: '155px',marginTop:'5px'},labelWidth:150,hideLabel:true},
				{change: {fn: me.datasourceTypeChanged, scope:me}});
		itemsNorth.push(me.dataSourceType);
        me.cmbProjects = CWHF.createSingleTreePicker("common.lbl.projectRelease",
            "projectOrRelease", [], me.selectedProjectOrRelease,
            {   labelStyle:{overflow:'hidden'},
                labelWidth:150,
                labelAlign:'right',
                style:{ marginRight: '5px'},
                anchor:'100%',
                margin: '0 5 5 0'
            })
        Ext.Ajax.request({
            url: "releasePicker.action",
            scope:me,
            success: function(response){
                var projectReleaseTree = Ext.decode(response.responseText);
                me.cmbProjects.updateData(projectReleaseTree);
                me.cmbProjects.setValue(me.selectedProjectOrRelease);
            },
            failure: function(response){
                com.trackplus.util.requestFailureHandler(response);
            }
        });
		itemsNorth.push(me.cmbProjects);
        me.cmbQueries=CWHF.createSingleTreePicker("common.lbl.filter",
            "query", [], me.selectedQueryID,
            {   labelStyle:{overflow:'hidden'},
                labelWidth:150,
                labelAlign:'right',
                style:{ marginRight: '5px'},
                anchor:'100%',
                margin: '0 5 5 0'
            });
		//me.cmbQueries=CWHF.createFilterPicker(getText('common.lbl.filter'), 'query',cfg);
		//me.cmbQueries.setValue(me.selectedQueryName);
        Ext.Ajax.request({
            url: "categoryPicker.action",
            params: {node:"issueFilter"},
            success: function(response){
                var filterTree = Ext.decode(response.responseText);
                me.cmbQueries.updateData(filterTree);
                me.cmbQueries.setValue(me.selectedQueryID);
            },
            failure: function(response){
                com.trackplus.util.requestFailureHandler(response);
            }})

		itemsNorth.push(me.cmbQueries);

		//project=1,query=2
		me.cmbProjects.setDisabled(me.selectedDatasourceType!=1);
		me.cmbQueries.setDisabled(me.selectedDatasourceType==1);


		me.chkIncludeClosed= Ext.create('Ext.form.field.Checkbox',{
			name:'includeClosed',
			fieldLabel:getText('item.lbl.includeClosed'),
			labelStyle:{overflow:'hidden'},
			labelWidth:150,
			labelAlign:'right'
		});
			//boxLabel:'Project/Release',
			//CWHF.createCheckbox('includeClosed','includeClosed',{anchor:'100%'});
		itemsNorth.push(me.chkIncludeClosed);

		me.txtIssueNumber= Ext.create('Ext.form.field.Text',{
			name:'searchIssueKey',
			fieldLabel:getText('item.lbl.itemNumber'),
			labelStyle:{overflow:'hidden'},
			labelWidth:150,
			width:250,
			labelAlign:'right'
		});
			//CWHF.createTextField('item.lbl.itemNumber','searchIssueKey',{width:CWHF.labelWidth+65});
		itemsNorth.push(me.txtIssueNumber);

		me.btnSearch=Ext.create('Ext.button.Button',{
			text: getText('common.btn.search'),
			margin:'0 5 10 155',
			formBind: true, //only enabled once the form is valid
			handler: function() {
				me.controller.search.call(me.controller);
			}
		});
		itemsNorth.push(me.btnSearch);

		me.storeIssues = Ext.create('Ext.data.Store', {
			fields:['id', 'title','objectID'],
			data:[],
			proxy: {
				type: 'ajax',
				url:'issuePicker!search.action',
				reader: {
					type: 'json'
				}
			}
		});
		me.gridIssues =Ext.create('Ext.grid.Panel',{
			store      : me.storeIssues,
			columns    : [
				{
					xtype:'linkcolumn',
					header: getText('item.lbl.itemNumber'),
					dataIndex: 'id',
					width: 75,
					align :'right',
					handler:me.controller.clickOnItem,
					scope:me.controller
				},{
					xtype:'linkcolumn',
					header: getText('item.prompt.synopsys'),
					dataIndex: 'title',
					width: 420,
					handler:me.controller.clickOnItem,
					scope:me.controller
				}
			],
			minHeight:75,
			border: true,
			margin:'0 0 0 0',
			bodyBorder:false,
			cls:'gridNoBorder',
			bodyStyle:{
				border:'none'
			},
			//columnLines :true,
			region:'center',
			forceFit: true,
			viewConfig: {
				stripeRows: true,
				forceFit: true
			}
		});
		var items=new Array();
		var panelNorth=Ext.create('Ext.panel.Panel',{
			border:false,
			bodyBorder:false,
			region:'north',
			layout:'anchor',
			items:itemsNorth
		})
		items.push(panelNorth);
		items.push(me.gridIssues);
		return items;
	},

	datasourceTypeChanged:function(radioGroup, newValue, oldValue, options){
		var me=this;
		var checkedArr = radioGroup.getChecked();
		var checkedRadio;
		if (checkedArr.length==1) {
			checkedRadio = checkedArr[0];
			var value=checkedRadio.getSubmitValue();
			//project=1,query=2
			me.cmbProjects.setDisabled(value!=1);
			me.cmbQueries.setDisabled(value==1);
		}
	}
});


Ext.define('com.trackplus.util.IssuePickerController',{
	extend:'Ext.Base',
	config: {
		workItemID:null,
		parent:false,
		title:"",
		handler:null,
		scope:null,
		ajaxContext:null,
		selectedProjectOrReleaseName:null,
		selectedProjectOrRelease:null,
		selectedQueryName:null,
		selectedQueryID:null,
		selectedDatasourceType:1,
		width:600,
		height:400,
		stateId:null
	},
	view:null,
	win:null,
	constructor : function(config) {
		var me = this
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	},
	showDialog:function(){
		var me=this;
		me.view=Ext.create('com.trackplus.util.IssuePickerView',{
			controller:me,
			selectedProjectOrReleaseName:me.selectedProjectOrReleaseName,
			selectedProjectOrRelease:me.selectedProjectOrRelease,
			selectedQueryName:me.selectedQueryName,
			selectedQueryID:me.selectedQueryID,
			selectedDatasourceType:me.selectedDatasourceType
		});

		if(me.win!=null){
			me.win.destroy();
		}

		me.win = Ext.create('Ext.window.Window',{
			layout      : 'fit',
			//iconCls:'buttonParent',
			width       : me.width,
			minWidth  : 350,
			minHeight  : 300,
			height      : me.height,
			closeAction :'destroy',
			plain       : true,
			title		:me.title,
			modal       :true,
			items       :[me.view],
			border:false,
			bodyBorder:true,
			margin:'0 0 0 0',
			style:{
				padding:'5px 0px 0px 0px'
			},
			autoScroll  :false,
			stateId:me.stateId,
			stateful:me.stateId!=null,
			listeners:{
				'staterestore':{
					fn:function(dialog,state){
						var w=state.width;
						var h=state.height;
						var x=null;
						var y=null;
						if(state.pos!=null){
							x=state.pos[0];
							y=state.pos[1];
						}
						var size=borderLayout.ensureSize(state.width,state.height);
						if(dialog.x<0){
							dialog.x=10;
						}
						if(dialog.y<0){
							dialog.y=10;
						}
						if(w!=size.width){
							dialog.width=size.width;
							dialog.x=10;
						}
						if(h!=size.height){
							dialog.height=size.height;
							dialog.y=10;
						}
					}
				}
			}
		});
		me.win.show();
		if(me.selectedProjectOrRelease!=null){
			me.search();
		}
	},
	search:function(){
		var me=this;
		var projectID=null;
		var queryID=null;
		var checkedArr = me.view.dataSourceType.getChecked();
		var checkedRadio;
		var projectSelected=true;
		if (checkedArr.length==1) {
			checkedRadio = checkedArr[0];
			var value=checkedRadio.getSubmitValue();
			if(value==2){
				projectSelected=false;
			}

		}
		if(projectSelected){
			projectID=me.view.cmbProjects.getValue();
            if (projectID==null) {
                //not yet selected by user, but preselected by issue's project
                projectID = me.selectedProjectOrRelease;
            }
		}else{
			queryID=me.view.cmbQueries.getSubmitValue();
		}
		var includeClosed=me.view.chkIncludeClosed.value;
		var searchIssueKey=me.view.txtIssueNumber.value;
		me.view.storeIssues.load({
			url:'issuePicker!search.action',
			params:{
				workItemID:me.workItemID,
				parent:me.parent,
				projectID:projectID,
				queryID:queryID,
				includeClosed:includeClosed,
				searchIssueKey:searchIssueKey
			},
			callback: function(records, operation, success) {
			}
		});
	},
	clickOnItem:function(record,cellIndex){
		var me=this;
		var item=record.data;
		if(me.ajaxContext!=null){
			me.ajaxPick(item);
		}else{
			me.win.close();
			me.win.destroy();
			if(me.handler!=null){
				me.handler.call(me.scope,item);
			}
		}
	},
	ajaxPick:function(item){
		var me=this;
		me.view.setLoading(true);
		var urlStr=me.ajaxContext.url;
		var params=me.ajaxContext.params;
		var pickItemName=me.ajaxContext.pickItemName;
		if(pickItemName==null){
			pickItemName='workItemID';
		}
		if(params==null){
			params=new Object();
		}
		params[pickItemName]=item['objectID'];
		Ext.Ajax.request({
			url: urlStr,
			scope:me,
			params:params,
			success: function(response){
				me.view.setLoading(false);
				var responseJson = Ext.decode(response.responseText);
				if (responseJson.success==true) {
					me.win.close();
					me.win.destroy();
					if(me.ajaxContext.successHandler!=null){
						if(me.ajaxContext.successHandlerScope!=null){
							me.ajaxContext.successHandler.call(me.ajaxContext.successHandlerScope,item);
						}else{
							me.ajaxContext.successHandler.call(me,item);
						}
					}
				}else{
					if (responseJson.errorMessage!=null) {
						//parent change for an issue
						Ext.MessageBox.show({
							title: getText('common.warning'),
							msg: responseJson.errorMessage,
						    buttons: Ext.Msg.OK,
						    icon: Ext.MessageBox.WARNING
						});
					} else {
						Ext.MessageBox.show({
							title: getText('common.err.invalid.item'),
							msg: errorMessage,
						    buttons: Ext.Msg.OK,
						    icon: Ext.MessageBox.ERROR
						});
					}
				}
			},
			failure:function(){
				me.view.setLoading(false);
				alert("failure");
			}
		});
	}

});
