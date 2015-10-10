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


//Dashboard Config

//dashboard types
Ext.define('js.ext.com.track.dashboard.BaseConfig',{
	extend: 'Ext.form.Panel',
	border: false,
	margin: '0 0 0 0',
	autoScroll:true,
	bodyStyle:{
		padding:'10px'
	},
	/*style:{
		borderBottom:'1px solid #D0D0D0'
	},*/
	frame: false,
	collapsible:false,
	titleCmp:null,
	layout:'anchor',
	config: {
		projectID:null,
		entityType:null,
		jsonData:{}
	},
	defaults:{
		labelWidth:150,
		labelAlign: 'right',
		labelSeparator :':',
		labelStyle:{overflow:'hidden'}
	},
	initComponent: function(){
		var me=this;
		me.items=me.createChildren();
		for(var i=0;i<me.items.length;i++){
			Ext.apply(me.items[i],me.defaults);
		}
		me.callParent();
	},
	createChildren:function(){
		var me=this;
		var items=[];
		me.titleCmp=Ext.create('Ext.form.field.Text',{
			fieldLabel:getText('title'),
			labelAlign:'right',
			anchor : '100%',
			name:'params.title',
			value:me.jsonData.title
		});
		items.push(me.titleCmp);
		return items;
	},
	isValidParams:function(){
		var me=this;
		return true;
	}
});

//dashboard types
Ext.define('js.ext.com.track.dashboard.DashboardRenderer',{
	extend: 'Ext.panel.Panel',
	border: false,
	margin: '0 0 5 5',
	frame: true,
	collapsible:true,
	bodyPadding: 0,
	padding:0,
	originalTitle:'',
	cls:'dashboardField',
	config: {
		projectID:null,
		useRefresh:true,
		refreshTime:0,
		useConfig:false,
		jsonData:{}
	},

	initComponent: function(){
		var me=this;
		me.useConfig=me.jsonData.useConfig;
		me.useRefresh=true;
		me.refreshTime=me.jsonData.refresh;
		if(me.jsonData.useRefresh===false){
			me.useRefresh=false;
		}
		if(haveLocalization(me.jsonData.title)){
			me.title=getText(me.jsonData.title);
		}else{
			me.title=me.jsonData.title;
		}

		me.originalTitle=me.title;
		var tools=[];
		if(me.useRefresh==true){
			tools.push({
				type:'refresh',
				tooltip: getText("common.btn.reload"),
				handler: function(event, toolEl, panel){
					me.refreshDashboard.call(me);
				}
			});
		}
		if(me.useConfig==true){
			tools.push({
				type:'gear',
                tooltip: getText("common.btn.config"),
				handler:function(){
					me.configDashboard.call(me);
				}
			});
		}
		Ext.apply(this, {
			tools: tools
		});
		me.id="dashboard_"+me.jsonData.dashboardID;
		me.callParent();
		if(me.refreshTime>0){
			me.title=me.originalTitle+" ("+(me.refreshTime/1000)+" s)";
			setTimeout(function(thisObj) {thisObj.refreshDashboard();}, me.refreshTime, me);
		}
	},
	createErrorCmp:function(errorMessage){
		return Ext.create('Ext.Component',{
			cls:'infoBox1',
			html:errorMessage
		});
	},
	changeToError:function(errorMessage){
		var me=this;
		me.removeAll();
		me.add(me.createErrorCmp(errorMessage));
	},
	refreshErrorHandler:function(){
		var me=this;
		me.changeToError(getText('cockpit.err.tooManyItems'));
	},
	refreshDashboard:function(params){
		var me=this;
		var dashboardID=me.jsonData.dashboardID;
		var projectID=me.jsonData.projectID;
		var entityType=me.jsonData.entityType;
		var releaseID=me.jsonData.releaseID;
		var urlStr=com.trackplus.TrackplusConfig.contextPath+'/dashboardAJAX.action?dashboardID='+dashboardID+"&projectID="+projectID+"&releaseID="+releaseID;
		me.setLoading(true);
		Ext.Ajax.request({
			url: urlStr,
			disableCaching :true,
			success: function(result){
				var jsonData = Ext.decode(result.responseText);
				me.setLoading(false);
				if(jsonData.data.tooManyItems==true){
					me.refreshErrorHandler();
				}else{
					me.doRefresh.call(me,jsonData.data);
					me.refreshTime=me.jsonData.refresh;
					if(me.refreshTime>0){
						setTimeout(function(thisObj) {thisObj.refreshDashboard();}, me.refreshTime, me);
					}
					me.setTitle(me.updateTitle.call(me));
				}
			},
			failure: function(error){
				me.setLoading(false);
				//alert("error"+error.message);
			},
			method:"POST",
			params:params
		});
	},
	updateTitle:function(){
		var me=this;
		var title=me.originalTitle;
		if(me.refreshTime>0){
			title=me.originalTitle+" ("+(me.refreshTime/1000)+" s)";
		}
		return title;
	},
	createParamsMap:function() {
		return {};
	},
	doRefresh:function(jsonData){
	},
	configDashboard:function(){
		var me=this;
		var configURL="";
		var projectID=me.jsonData.projectID;
		var entityType=me.jsonData.entityType;
		var dashboardID=me.jsonData.dashboardID;
		if(projectID!=null){
			configURL="dashboardParamsConfig.action?projectID="+projectID+"&entityType="+entityType+"&dashboardID="+dashboardID;
		}else{
			configURL="dashboardParamsConfig.action?dashboardID="+dashboardID;
		}
		com.trackplus.dashboard.openConfigDialog(dashboardID,configURL,projectID,entityType,function(){
			me.refreshDashboard.call(me);
		});
	},
	addAll:function(destination,source){
		if(source!=null&&source.length>0){
			for(var i=0;i<source.length;i++){
				destination.push(source[i]);
			}
		}
		return destination;
	}
});

Ext.define('js.ext.com.track.dashboard.Error',{
	extend:'js.ext.com.track.dashboard.DashboardRenderer',
	initComponent : function(){
		var me=this;
		me.html=me.createHtmlString();
		me.callParent();
	},
	createHtmlString:function(){
		var me=this;
		var htmlStr='<div style="height:100%;width:100%;padding:5px;">';
		htmlStr+="<i>No js class  found!</i>";
		htmlStr+='</div>';
		return htmlStr;
	},
	doRefresh:function(jsonData){
		var me=this;
		me.jsonData=jsonData;
		me.update(me.createHtmlString());
	}
});

com.trackplus.dashboard.createPercentGroup=function(title,list,dashboard){
	var gridData=list;
	if(title!=null){
		gridData.unshift({
			label:title,
			h2:true,
			category:true
		});
	}
	var store = Ext.create('Ext.data.Store', {
		fields: [
			{name: 'label'},
			{name: 'number',type:'int'},
			{name: 'width',type:'int'},
			{name: 'percent',type: 'int'},

			{name: 'groupByFieldType',type: 'int'},
			{name: 'projectID',type: 'int',useNull:true},
			{name: 'releaseID',type: 'int',useNull:true},
			{name: 'openOnly',type: 'boolean'},
			{name: 'groupByField',type: 'int'},
			{name: 'icon'},

			{name: 'percentLate',type: 'int'},
			{name: 'category',type: 'boolean'},
			{name: 'h2',type: 'boolean'},
			{name: 'widthLate',type: 'int'}
		],
		data: gridData
	});
	var grid = Ext.create('Ext.grid.Panel', {
		store: store,
		columns: [
			{
				xtype:'linkcolumn',
				text	 :'label',
				width:175,
				sortable:false,
				menuDisabled:true,
				dataIndex: 'label',
				align:'right',
				handler:com.trackplus.dashboard.clickOnLabel,
				scope:dashboard,
				isLink:com.trackplus.dashboard.isLabelLink,
				postProcessRenderer:com.trackplus.dashboard.postProcessRendererLabel
			},{
				text	 :'number',
				width	: 70,
				sortable:false,
				menuDisabled:true,
				renderer : com.trackplus.dashboard.formatNumber,
				align:'right',
				dataIndex: 'number'
			},{
				text	 :'',
				width	: 75,
				sortable:false,
				menuDisabled:true,
				renderer : com.trackplus.dashboard.formatBars,
				dataIndex: 'percent'
			},{
				text	 :'',
				width	: 50,
				sortable:false,
				menuDisabled:true,
				renderer : com.trackplus.dashboard.formatPercent,
				dataIndex: 'percent'
			},{
				text	 :'',
				width:10,
				flex:1,
				sortable:false,
				menuDisabled:true
			}
		],
		border:false,
		bodyBorder:false,
		hideHeaders:true,
		columnLines :false,
		disableSelection: true,
		viewConfig: {
			stripeRows: true,
			trackOver  :false,
			style:{
				overflow:'hidden'
			},
			getRowClass: function(record, rowIndex, rp, ds){
				var cls="";
				if(record.data['category']==true){//grouping
					cls+="dashboardCategoryPercent";
				}
				return cls;
			}
		},
		autoHeight:true
	});
	return grid;
};
com.trackplus.dashboard.updateElementWrapperList=function(list,projectWrapper){
	if(list!=null){
		var elmWrapper;
		for(var i=0;i<list.length;i++){
			elmWrapper=list[i];
			elmWrapper['groupByFieldType']=projectWrapper.groupByFieldType;
			elmWrapper['projectID']=projectWrapper.projectID;
			elmWrapper['releaseID']=projectWrapper.releaseID;
			elmWrapper['openOnly']=projectWrapper.openOnly;
		}
	}
	return list;
};

com.trackplus.dashboard.isLabelLink=function(value,metaData,record,rowIndex,colIndex,store,view){
	return !(record.data['category']==true);
};
com.trackplus.dashboard.postProcessRendererLabel=function(value,metaData,record,rowIndex,colIndex,store,view){
	var htmlStr='';
	if(record.data['category']==true){
		htmlStr='<div style="text-align:left;overflow: visible;">';
		if(record.data['h2']==true){
			htmlStr+='<h2>'+value+'</h2>';
		}else{
			htmlStr+='<B>'+value+'</B>';
		}
		htmlStr+='</div>';
	}else{
		htmlStr=value;
	}
	return htmlStr;
};

com.trackplus.dashboard.clickOnLabel=function(record,cellIndex){
	var me=this;//scope is current dashboard
	var groupByFieldType=record.data['groupByFieldType'];
	var projectID=record.data['projectID'];
	var releaseID=record.data['releaseID'];
	var openOnly=record.data['openOnly'];
	var groupByField=record.data['groupByField'];
	var urlStr='itemNavigator.action';
	var params={
		'queryType':2,//dashboard
		'queryID':me.jsonData.dashboardID,
		'dashboardParams.dashboardID':me.jsonData.dashboardID
	};
	var entityFlag=null;
	if(projectID==null && releaseID==null){
		projectID=me.jsonData.projectID;
        releaseID=me.jsonData.releaseID;
		entityFlag=me.jsonData.entityType;
        if (entityFlag!=null) {
             if (entityFlag==9 && releaseID!=null) {
                 projectID = releaseID;
             }
        }
	}else{
		if(releaseID!=null){
			entityFlag=9;
            projectID=releaseID;
		}else{
			entityFlag=1;
		}
	}
	if(openOnly==null){
		openOnly=false;
	}
	params['dashboardParams.groupByFieldType']=groupByFieldType;
	params['dashboardParams.groupByField']=groupByField;
	params['dashboardParams.projectID']=projectID;
	params['dashboardParams.entityFlag']=entityFlag;
	params['dashboardParams.openOnly']=openOnly;

	var dummyForm = Ext.create('Ext.form.Panel', {
		items:[],
		url:urlStr,
		standardSubmit:true
	});
	dummyForm.getForm().submit({
		params:params
	});
};

com.trackplus.dashboard.formatNumber=function(value,metaData,record,rowIndex,colIndex,store,view){
	if(record.data.category){
		return "";
	}else{
		return value;
	}
};
com.trackplus.dashboard.formatBars=function(value,metaData,record,rowIndex,colIndex,store,view){
	var htmlStr='';
	if(record.data.category){
		htmlStr='';
	}else{
		htmlStr='<div class="barWrapper"> <img src="'+com.trackplus.TrackplusConfig.iconsPath+'BlueBar.gif" '+
			'width="'+record.data.width+'" height="10" alt="BlueBar"><img src="'+
			com.trackplus.TrackplusConfig.iconsPath+'RedBar.gif" '+
			'width="'+record.data.widthLate+'" height="10" alt="RedBar"></div>';
	}
	return htmlStr;
};
com.trackplus.dashboard.formatPercent=function(value,metaData,record,rowIndex,colIndex,store,view){
	var htmlStr='';
	if(record.data.category){
		htmlStr='';
	}else{
		htmlStr=value+"%";
	}
	return htmlStr;
};



com.trackplus.dashboard.reload=function(id){
	var dashComp=Ext.getCmp("dashboard_"+id);
	var extraParams=[];
	if(arguments.length>1){
		for(var i=1;i<arguments.length;i++){
			extraParams.push(arguments[i]);
		}
	}
	var params=dashComp.createParamsMap.apply(dashComp,extraParams);
	dashComp.refreshDashboard.call(dashComp,params);
};

com.trackplus.dashboard.createReportTreeCfg=function(label,name,values,projectID,extraCfg){
	var cfg = {
        anchor:'100%',
        margin: '0 0 5 0'
	};
	var labelWidth=150;
	var labelAlign='right';
	if(extraCfg!=null){
		for (var propertyName in extraCfg) {
			if(propertyName=='labelAlign'){
				labelAlign=extraCfg[propertyName];
				continue;
			}
			if(propertyName=='labelWidth'){
				labelWidth=extraCfg[propertyName];
				continue;
			}
			cfg[propertyName] = extraCfg[propertyName];
		}
	}
    cfg["useRemoveBtn"] = false;
    cfg["useNull"] = true;
    cfg["useTooltip"] = false;
    var reportTree = CWHF.createMultipleTreePicker(label,
        name, [], values, cfg)
    Ext.Ajax.request({
        url: "categoryPicker.action",
        params:{node:"report", useChecked: true},
        //scope:me,
        success: function(response){
            var children = Ext.decode(response.responseText);
            reportTree.updateData(children);
            reportTree.setValue(values);
        },
        failure: function(response){
            com.trackplus.util.requestFailureHandler(response);
        }
    });
    return reportTree;
	/*var reportTreeObject = CWHF.createReportTree(values,{projectID:projectID},name,cfg);//,null,label);
	if(label==null){
		return reportTreeObject;
	}
	reportTreeObject.flex=1;
	var labelCmp = com.trackplus.dashboard.getLabelCmp(label, labelAlign, labelWidth);
	return com.trackplus.dashboard.wrapLabelAndTree(labelCmp,reportTreeObject);*/

};


com.trackplus.dashboard.createReleaseTreeCfg=function(label,name,values,extraCfg){
	var cfg={
		//height: 200
	}
	var labelWidth=150;
	var labelAlign='right';
	if(extraCfg!=null){
		for (var propertyName in extraCfg) {
			if(propertyName=='labelAlign'){
				labelAlign=extraCfg[propertyName];
				continue;
			}
			if(propertyName=='labelWidth'){
				labelWidth=extraCfg[propertyName];
				continue;
			}
			cfg[propertyName] = extraCfg[propertyName];
		}
	}
    cfg["useRemoveBtn"] = false;
    cfg["useNull"] = true;
    cfg["useTooltip"] = false;
    var releaseProjectTree = CWHF.createMultipleTreePicker(label,
        name, [], values, cfg)
    Ext.Ajax.request({
        url: "releasePicker.action",
        params:{useChecked: true},
        //scope:me,
        success: function(response){
            var children = Ext.decode(response.responseText);
            releaseProjectTree.updateData(children);
            releaseProjectTree.setValue(values);
        },
        failure: function(response){
            com.trackplus.util.requestFailureHandler(response);
        }
    });
    return releaseProjectTree;
	/*var releaseTreeObject = CWHF.createReleaseTree(values,
		{	withParameter:false,
			activeFlag: true,
			inactiveFlag: true,
			notPlannedFlag: true,
			closedFlag: false,
			useChecked: true,
			projectIsSelectable: true
		},
		name,cfg);//,null,label);
	if(label==null){
		return releaseTreeObject;
	}
	releaseTreeObject.flex=1;
	var labelCmp = com.trackplus.dashboard.getLabelCmp(label, labelAlign, labelWidth);
	return com.trackplus.dashboard.wrapLabelAndTree(labelCmp,releaseTreeObject);*/

};

com.trackplus.dashboard.wrapLabelAndTree=function(labelCmp, treeObject) {
	return Ext.create('Ext.container.Container', {
		border:false,
		layout: {
			type: 'hbox',
			pack: 'start',
			align: 'stretch'
		},
		items: [labelCmp, treeObject]
	});
};

com.trackplus.dashboard.getLabelCmp=function(label, labelAlign, labelWidth) {
	return Ext.create('Ext.Component',{
		cls:labelAlign=='right'?'x-form-item-label-right x-form-item-label':'x-form-item-label',
		style:{
			marginRight:'5px'
		},
		html:label+":",
		width:labelWidth
	});
};

com.trackplus.dashboard.createReleasePickerCfg=function(label,name,releaseID,extraCfg){
	var cfg={
		labelAlign:'right',
		labelWidth:100,
        //width:300,
        anchor:'100%',
        margin: '0 0 5 0'
	};
	if(extraCfg!=null){
		for (var propertyName in extraCfg) {
			cfg[propertyName] = extraCfg[propertyName];
		}
	}
    var picker = CWHF.createSingleTreePicker(label,
        name, [], releaseID, cfg);
    Ext.Ajax.request({
        url: "releasePicker.action",
        //scope:me,
        success: function(response){
            var projectReleaseTree = Ext.decode(response.responseText);
            picker.updateData(projectReleaseTree);
            picker.setValue(releaseID);
        },
        failure: function(response){
            com.trackplus.util.requestFailureHandler(response);
        }
    });
	//var picker=CWHF.createReleasePicker(label,name,cfg);
	//picker.setValue(releaseLabel);
	return picker;
};
com.trackplus.dashboard.createMultipleReleasePickerCfg=function(label,name,releaseID,extraCfg){
	var cfg={
		labelAlign:'right',
		labelWidth:100,
        //width:300,
        anchor:'100%',
        margin: '0 0 5 0'
	};
	if(extraCfg!=null){
		for (var propertyName in extraCfg) {
			cfg[propertyName] = extraCfg[propertyName];
		}
	}
    var picker = CWHF.createMultipleTreePicker(label,
        name, [], releaseID, cfg);
    Ext.Ajax.request({
        url: "releasePicker.action",
        //scope:me,
        success: function(response){
            var projectReleaseTree = Ext.decode(response.responseText);
            picker.updateData(projectReleaseTree);
            picker.setValue(releaseID);
        },
        failure: function(response){
            com.trackplus.util.requestFailureHandler(response);
        },
        params: {useChecked:true}
    });
	//var picker=CWHF.createReleasePicker(label,name,cfg);
	//picker.setValue(releaseLabel);
	return picker;
};
com.trackplus.dashboard.createFilterPickerCfg=function(label,name,queryID,extraCfg){
	var cfg={
		allowBlank:false,
		labelWidth:100,
		labelAlign:'right',
		//width:300
		anchor:'100%',
        margin: '0 0 5 0'
	};
	if(extraCfg!=null){
		for (var propertyName in extraCfg) {
			cfg[propertyName] = extraCfg[propertyName];
		}
	}
    var picker = CWHF.createSingleTreePicker(label,
        name, [], queryID, cfg);
        Ext.Ajax.request({
            url: "categoryPicker.action",
            params: {node:"issueFilter"},
            success: function(response){
                var filterTree = Ext.decode(response.responseText);
                picker.updateData(filterTree);
                picker.setValue(queryID);
            },
            failure: function(response){
                com.trackplus.util.requestFailureHandler(response);
            }
    });
	return picker;
};

com.trackplus.dashboard.createMultiSelectConfig=function(label,name,data,value,extraCfg){
	var cfg={
		fieldLabel:label,
		labelAlign:'right',
		name:name,
		store: Ext.create('Ext.data.Store', {
			data	: data,
			fields	: [{name:'id', type:'int'}, {name:'label', type:'string'}],
			autoLoad: false
		}),
		border:true,
		bodyBorder:false,
		displayField: 'label',
		valueField: 'id',
		value:value,
		width:300,
		height:125
	};
	if(extraCfg!=null){
		for (var propertyName in extraCfg) {
			cfg[propertyName] = extraCfg[propertyName];
		}
	}
	return Ext.create('Ext.ux.form.MultiSelect',cfg);
};


com.trackplus.dashboard.createSelectConfig=function(label,name,data,value,handler,scope){
	return Ext.create('Ext.form.ComboBox',{
		fieldLabel:label,
		hideLabel:(label==null),
		name:name,
		store: Ext.create('Ext.data.Store', {
			data	:(data==null?[]:data),
			fields	: [{name:'id', type:'int'}, {name:'label', type:'string'}],
			autoLoad: false
		}),
		border:false,
		bodyBorder:false,
		displayField: 'label',
		valueField: 'id',
		queryMode: 'local',
		value:value,
		width:(label==null? 150:300),
		labelAlign:'right',
		listeners: {
			change: function(cmb, newValue, oldValue, options) {
				if(handler!=null){
					handler.call(scope,cmb, newValue, oldValue, options);
				}
			}
		}
	});
};

com.trackplus.dashboard.createMultiSelectComboConfig=function(label,name,data,value,handler,scope){
	return Ext.create('Ext.form.ComboBox',{
		fieldLabel:label,
		hideLabel:(label==null),
		name:name,
		store: Ext.create('Ext.data.Store', {
			data	:(data==null?[]:data),
			fields	: [{name:'id', type:'int'}, {name:'label', type:'string'}],
			autoLoad: false
		}),
		border:false,
		bodyBorder:false,
		displayField: 'label',
		valueField: 'id',
		queryMode: 'local',
		value:value,
		width:(label==null? 150:300),
		labelAlign:'right',
		multiSelect: true,
		listeners: {
			change: function(cmb, newValue, oldValue, options) {
				if(handler!=null){
					handler.call(scope,cmb, newValue, oldValue, options);
				}
			}
		}
	});
};
com.trackplus.dashboard.createRadioGroupConfig=function(label,name,options,value,handler,scope){
	var items=[];
	if(options!=null){
		for(var i=0;i<options.length;i++){
			items.push({
				name:name,
				inputValue: options[i].id,
				//id:id+"_"+options[i].id,
				boxLabel:options[i].label,
				checked:options[i].id==value
			});
		}
	}
	var inputComp=Ext.create('Ext.form.RadioGroup',{
		fieldLabel:label,
		labelStyle:{overflow:'hidden'},
		labelWidth:100,
		labelAlign:'right',
		anchor:'100%',
		layout: 'hbox',
		defaults:{margin:'0 5 0 0'},
		listeners: {
			change: function(radioGroup, newValue, oldValue, options) {
				if(handler!=null){
					handler.call(scope,radioGroup, newValue, oldValue, options);
				}
			}
		},
		items:items
	});
	return inputComp;
};


var dashboardCfgWin;

com.trackplus.dashboard.showConfigDialog=function(dashboardID,data,projectID,entityType,handlerCallback,scopeCallBack){
	var w=600;
	var h=400;
	var title=getText('cockpit.screenEdit.config.title');

	if(dashboardCfgWin!=null){
		dashboardCfgWin.destroy();
	}
	var cfgClass=data.cfgClass;
	var prefWidth=data.jsonData.prefWidth;
	var prefHeight=data.jsonData.prefHeight;
	if(prefWidth!=null){
		w=prefWidth;
	}
	if(prefHeight!=null){
		h=prefHeight;
	}
	var dashboardConfigPanel=Ext.create(cfgClass,{
		projectID:data.projectID,
		entityType:data.entityType,
		jsonData:data.jsonData,
		bodyStyle:{
			padding:'10px'
		}
	});
	var size=borderLayout.ensureSize(w,h);
	w=size.width;
	h=size.height;
	dashboardCfgWin = Ext.create('Ext.window.Window',{
		layout	  : 'fit',
		width	   : w,
		height	  : h,
		iconCls:'btnConfig16',
		cls:'bottomButtonsDialog',
		border:false,
		bodyBorder:true,
		margin:'0 0 0 0',
		style:{
			padding:'5px 0px 0px 0px'
		},
		closeAction :'hide',
		plain	   : true,
		title		 :title,
		modal	   :true,
		items	   :dashboardConfigPanel,
		autoScroll  :false,
		buttons: [
			{text :getText('common.btn.save'),
				handler  : function(){
					saveDashParams(dashboardID,dashboardConfigPanel,projectID,entityType, handlerCallback,scopeCallBack);
				}
			},{
				text :getText('common.btn.close'),
				handler  : function(){
					dashboardCfgWin.hide();
				}
			}
		]
	 });

	 dashboardCfgWin.show();
};
com.trackplus.dashboard.openConfigDialog=function(dashboardID,urlStr,projectID,entityType, handlerCallback,scopeCallBack){
	borderLayout.setLoading(true);
	Ext.Ajax.request({
		url:urlStr,
		success: function(response){
			var jsonData=Ext.decode(response.responseText);
			com.trackplus.dashboard.showConfigDialog(dashboardID,jsonData.data,projectID,entityType, handlerCallback,scopeCallBack);
			borderLayout.setLoading(false);
		},
		failure:function(response){
			alert("failed to open config dashboard");
			borderLayout.setLoading(false);
		}
	});
};
function saveDashParams(dashboardID,dashboardConfigPanel,projectID,entityType, handler,scope){
	var urlSave="";
	if(projectID!=null){
		urlSave="dashboardParamsConfig!save.action?projectID="+projectID+"&entityType="+entityType+"&dashboardID="+dashboardID;
	}else{
		urlSave="dashboardParamsConfig!save.action?dashboardID="+dashboardID;
	}
	if(!dashboardConfigPanel.isValidParams.call(dashboardConfigPanel)){
		return false;
	}
	dashboardCfgWin.setLoading(true);
	dashboardConfigPanel.getForm().submit({
		url: urlSave,
		success: function(form, action) {
			dashboardCfgWin.setLoading(false);
			dashboardCfgWin.hide();
			if(handler!=null){
				handler.call(scope);
			}
		},
		failure: function(form, action) {
			dashboardCfgWin.setLoading(false);
		}
	});
	//var params=dashboardConfigPanel.getParameters.call(dashboardConfigPanel);
	/*Ext.Ajax.request({
		url: urlSave,
		disableCaching:true,
		success: function(result){

		},
		failure: function(){
			alert("failure");
		},
		method:'POST',

	}); */
}

Ext.define('com.trackplus.screen.DashboardFieldErrorView',{
	extend:'com.trackplus.screen.FieldErrorView',
	margin: '0 0 5 5',
	frame: true,
	style:{
		borderColor:'red'
	},
	collapsible:true,
	padding:0,
	title:'Dashboard field error'
});

Ext.define('com.trackplus.screen.DashboardTabView',{
    extend: 'com.trackplus.screen.TabView',
    layout:{
        type:'anchor'
    },
    defaults:{
        anchor:'100%'
    },
    autoScroll:true,
	bodyStyle:{}
});

Ext.define('com.trackplus.screen.DashboardPanelView',{
	extend:'com.trackplus.screen.PanelView',
	getPanelConfig:function(panelModel,index,length){
		var me=this;
		var margins='0 5 2 0';
		if(index==0){
			margins='5 5 2 0';
		}
		return {
			style: {
				overflow:'hidden'
			},
			border:false,
			margin	 : margins,
			cls:'dashboardPanel',
			layout: {
				type: 'table',
				columns: panelModel.colsNo,
				tableAttrs: {
					style: {
						width: '100%',
						tableLayout:'fixed'
					}
				},
				tdAttrs:{
					style:{
						'vertical-align':'top'
					}
				}
			},
			defaults: {
				frame:false,
				border: false
			}
		};
	}
});






