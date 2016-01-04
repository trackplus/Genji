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

Ext.define('com.trackplus.admin.server.status.ServerStatusView',{
	extend:'Ext.tab.Panel',
	controller:'serverStatus',

	labelWidth:290,
	alignR:"right",

	plain:true,
	border:false,
	bodyBorder:false,
	padding: '3 0 0 0',
	defaults:{
		border:false,
		autoScroll:true,
		bodyStyle:{
			border:'none',
			padding:'0px'
		}
	},

	listeners : {
		scope: 'controller',
		afterrender:'onAfterRender'
	},

	initComponent : function() {
		var formPanel=this.createFormPanel();
		var logMessagesTab=Ext.create('Ext.panel.Panel',{
			title: getText('admin.server.status.logMessages'),
			reference:'logMessagesTab',
			layout: {
				type: 'anchor'
			},
			html: '...',
			bodyPadding:10
		});

		this.items=[formPanel, logMessagesTab];
		this.dockedItems = [{
			xtype: "toolbar",
			dock: 'top',
			items: [
				Ext.create('Ext.button.Button',{
					text:getText('common.btn.save'),
					tooltip:getText('common.btn.save'),
					iconCls: 'save',
					disabled:true,
					reference:'btnSave',
					handler:'onSave'
				}),
				Ext.create('Ext.button.Button',{
					text:getText('common.btn.reload'),
					tooltip:getText('common.btn.reload'),
					iconCls: 'reload16',
					handler:'onReload'
				})
			]
		}];
		this.callParent();
	},

	createFormPanel:function(){
		var formPanel=Ext.create('Ext.form.Panel', {
			title:getText('admin.server.status.status'),
			url: 'editAdminSiteStatus!save.action',
			reference:'formPanel',
			border	: false,
			bodyBorder:false,
			autoScroll:true,
			margins: '0 0 0 0',
			width: '100%',
			layout: {
				type: 'table',
				columns: 2,
				tableAttrs: {
					style: {
						width: '100%'
					}
				},
				tdAttrs:{
					width:'50%',
					style:{
						'vertical-align':'top'
					}
				}
			},
			defaults: {
				frame:false,
				border: false
			}
		});
		return formPanel;
	},

	createCountActiveInactiveLabel: function(userCount) {
		var me=this;
		var label = userCount["label"];
		var total = userCount["total"];
		var active = userCount["active"];
		var inactive = userCount["inactive"];
		return CWHF.createLabelComponent(label, null,
			{labelIsLocalized:true,
				labelWidth:me.labelWidth,
				value:total + " (" + active + "/" + inactive + ")"});
	},

	createLabelComponent:function(fieldLabelKey,value){
		var me=this;
		var fieldLabel='';
		if(fieldLabelKey&&fieldLabelKey!==''){
			fieldLabel=getText(fieldLabelKey);
		}
		var labelComp={
			xtype:'displayfield',
			fieldLabel:fieldLabel,
			labelStyle:{overflow:'hidden'},
			labelWidth:me.labelWidth,
			labelAlign:me.alignR,
			value: value,
			htmlEncode :false,
			fieldStyle : {
				height:'auto'
			},
			anchor:'100%'
		};
		return labelComp;
	},

	createLabelPercentComponent:function(fieldLabelKey,value,percent){
		var me=this;
		var barWidth = 80;
		var fieldLabel='';
		if(fieldLabelKey&&fieldLabelKey!==''){
			fieldLabel=getText(fieldLabelKey);
		}

		if (CWHF.isNull(percent)) {
			percent = 0;
		}

		var rwidth = barWidth * percent / 100;
		var gwidth = barWidth - rwidth;

		var labelComp={
			xtype:'displayfield',
			fieldLabel:fieldLabel,
			labelStyle:{overflow:'hidden'},
			labelWidth:me.labelWidth,
			labelAlign:me.alignR,
			value: value,
			width: me.labelWidth+100,
			htmlEncode :false
			//anchor:'50%'
		};

		var divComp={
			xtype: 'container',
			layout: {
				type: 'hbox'
			},
			id: 'MYIMAGE',
			items: [
				labelComp,
				{
					xtype:'component',
					cls:'barWrapper',
					style: {
						right: '40px',
						marginLeft: '10px',
						marginTop: '7px'
					},
					html:'<img width="'+rwidth+'" height="10" alt="RedBar" src="'+
						com.trackplus.TrackplusConfig.iconsPath+'RedBar.gif"/>'+
						'<img width="'+gwidth+'" height="10" alt="GreenBar" src="'+
						com.trackplus.TrackplusConfig.iconsPath+'GreenBar.gif"/>'
				}
			]
		};
		return divComp;
	},

	createFormItems:function(jsonData){
		var me=this;
		me.jsonData=jsonData;
		var ipString=jsonData.serverIPAddress;
		var index = ipString.indexOf(",");
		while(index !== -1){
			ipString = ipString.replace(",","<br>");
			index = ipString.indexOf(",");
		}

		var generalInformation=Ext.create('Ext.form.FieldSet',{
			// frame: true,
			title: getText('admin.server.status.generalInfo'),
			layout: {
				type: 'anchor'
			},
			items:[
				me.createLabelComponent("common.version",jsonData.version),
				me.createLabelComponent("admin.server.status.ipNumber",ipString)
			],
			margin:'0 0 5 0'
		});

		var dsPerson= Ext.create('Ext.data.Store', {
			data	:jsonData.usersLoggedIn,
			fields	: [{name:'value', type:'string'}, {name:'label', type:'string'}],
			autoLoad: true
		});

		var cmbPerson = Ext.create('Ext.ux.form.MultiSelect',{
			fieldLabel	: ' ',
			labelSeparator:'',
			hideLabel :false,
			labelWidth:me.labelWidth,
			labelAlign:me.alignR,
			width: (me.labelWidth+230),
			height:140,
			margin:'0 5 5 0',
			store	:dsPerson,
			displayField: 'label',
			valueField	: 'value',
			value: ''
		});

		var dsClusterNodes= Ext.create('Ext.data.Store', {
			data	:jsonData.clusterNodes,
			fields	: [{name:'value', type:'string'}, {name:'label', type:'string'}],
			autoLoad: true
		});

		var cmbClusterNodes = Ext.create('Ext.ux.form.MultiSelect',{
			fieldLabel	: getText('admin.server.status.nodes'),
			labelWidth:me.labelWidth,
			labelAlign:me.alignR,
			width: (me.labelWidth+230),
			height:50,
			margin:'0 5 5 0',
			store	:dsClusterNodes,
			displayField: 'label',
			valueField	: 'value',
			value: ''
		});
		var statisticItems = [me.createLabelComponent("admin.server.status.numberOfProjects",jsonData.numberOfProjects),
			me.createLabelComponent("admin.server.status.numberOfIssues",jsonData.numberOfIssues)];
		var userCountList = jsonData.userCountList;
		if (userCountList) {
			Ext.Array.forEach(userCountList, function(userCount) {
				statisticItems.push(me.createCountActiveInactiveLabel(userCount));
			}, this);
		}
		statisticItems.push(cmbPerson);
		statisticItems.push(cmbClusterNodes);
		var statistics=Ext.create('Ext.form.FieldSet',{
			// frame: true,
			title: getText('admin.server.status.statistics'),
			// height:485, // that does not work
			layout: {
				type: 'anchor'
			},
			items: statisticItems,
			margin:'0 0 5 0'
		});


		var javaVMMemory=Ext.create('Ext.form.FieldSet',{
			// frame: true,
			title: getText('admin.server.status.jvmmem'),
			layout: {
				type: 'anchor'
			},
			anchor: '50%',
			items:[
				me.createLabelComponent("admin.server.status.maxMemory",jsonData.javaVMmaxMemory+" Mbyte"),
				me.createLabelComponent("admin.server.status.totalMemory",jsonData.javaVMtotalMemory+" Mbyte"),
				me.createLabelPercentComponent("admin.server.status.usedMemory",jsonData.javaVMusedMemory+" Mbyte", (100*jsonData.javaVMusedMemory)/jsonData.javaVMtotalMemory),
				me.createLabelComponent("admin.server.status.freeMemory",jsonData.javaVMfreeMemory+" Mbyte")
			],
			margin:'0 0 5 0'
		});
		var javaInformation=Ext.create('Ext.form.FieldSet',{
			frame: true,
			title: getText('admin.server.status.javaInfo'),
			layout: {
				type: 'anchor'
			},
			items:[
				me.createLabelComponent("admin.server.status.javaVersion",jsonData.javaVersion),
				me.createLabelComponent("admin.server.status.javaVendor",jsonData.javaVendor),
				me.createLabelComponent("admin.server.status.javaHome",jsonData.javaHome),
				me.createLabelComponent("admin.server.status.javaVmVersion",jsonData.javaVMVersion),
				me.createLabelComponent("admin.server.status.javaVmVendor",jsonData.javaVMVendor),
				me.createLabelComponent("admin.server.status.javaVmName",jsonData.javaVMName)
			],
			margin:'0 0 5 0'
		});
		var systemInformation=Ext.create('Ext.form.FieldSet',{
			frame: true,
			title: getText('admin.server.status.sysinfo'),
			layout: {
				type: 'anchor'
			},
			items:[
				me.createLabelComponent("admin.server.status.osName",jsonData.operatingSystem),
				me.createLabelComponent("admin.server.status.userName",jsonData.userName),
				me.createLabelComponent("admin.server.status.userDir",jsonData.currentUserDir),
				me.createLabelComponent("admin.server.status.systemLocale",jsonData.systemLocale),
				me.createLabelComponent("admin.server.status.userTz",jsonData.userTimezone),
				me.createLabelComponent("admin.server.status.jasperVersion",jsonData.jasperVersion)
			],
			margin:'0 0 5 0'
		});
		var radioGroupOpMode={
			xtype: 'radiogroup',
			fieldLabel:getText('admin.server.status.opstate'),
			labelStyle:{overflow:'hidden'},
			labelWidth:me.labelWidth,
			labelAlign:me.alignR,
			anchor:'100%',
			layout: 'hbox',
			defaults:{margin:'0 5 0 0'},
			items:[
				{
					name:'opState',
					inputValue: 'Running',
					boxLabel:getText("admin.server.status.opstate.running"),
					checked:(CWHF.isNull(jsonData.operationalStatus)||jsonData.operationalStatus==="Running")
				},{
					name:'opState',
					inputValue: 'Maintenance',
					boxLabel:getText("admin.server.status.opstate.maintenance"),
					checked:(jsonData.operationalStatus==="Maintenance")
				}
			]
		};
		var checkUserMsg={
			xtype: 'checkboxfield',
			fieldLabel	: getText('admin.server.status.userMsg'),
			labelWidth:me.labelWidth,
			labelAlign:me.alignR,
			inputValue:'true',
			name:'hasUserMsg',
			checked:(jsonData.isUserInfo===true)
		};
		var txtUserMsg={
			xtype:'textarea',
			fieldLabel	: ' ',
			labelSeparator:'',
			labelWidth:me.labelWidth,
			labelAlign:me.alignR,
			name:'userMsg',
			width: (me.labelWidth+200),
			value:jsonData.userInfo,
			height:120
		};
		var operation=Ext.create('Ext.form.FieldSet',{
			// frame: true,
			title: getText('admin.server.status.opstateInfo'),
			layout: {
				type: 'anchor'
			},
			items:[
				radioGroupOpMode,
				checkUserMsg,txtUserMsg
			],
			margin:'0 0 5 0'
		});

		var databaseInformation=Ext.create('Ext.form.FieldSet',{
			// frame: true,
			title: getText('admin.server.status.dbInfo'),
			layout: {
				type: 'anchor'
			},
			items:[
				me.createLabelComponent("admin.server.status.dbInfo",jsonData.database),
				me.createLabelComponent("admin.server.status.jdbcDriverInfo",jsonData.jdbcDriver),
				me.createLabelComponent("admin.server.status.jdbcUrl",jsonData.jdbcUrl),
				me.createLabelComponent("admin.server.status.pingTime",jsonData.pingTime)
			],
			margin:'0 0 5 0'
		});

		return [
			{
				margin:'5 5 5 5',
				items:[generalInformation, statistics, databaseInformation]
			},{
				margin:'5 5 5 5',
				items:[javaVMMemory,javaInformation,systemInformation,operation]
			}
		];
	}
});
