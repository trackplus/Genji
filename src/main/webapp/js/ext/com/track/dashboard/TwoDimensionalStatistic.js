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

Ext.define('js.ext.com.track.dashboard.TwoDimensionalStatistic',{
	extend:'js.ext.com.track.dashboard.DashboardRenderer',

	initComponent : function(){
		var me=this;
		me.items=me.createChildren();
		me.callParent();
		if(me.jsonData.filterTitle){
			me.title=me.title+"&nbsp;-&nbsp;<i>"+me.jsonData.filterTitle+"</i>";
		}
	},
	createChildren:function(){
		var me=this;
		if(me.jsonData.tooManyItems===true){
			return [me.createErrorCmp(getText('cockpit.err.tooManyItems'))];
		}
		var statistic=me.jsonData.statistic;
		if(statistic){
			return [me.createGrid()];
		}else{
			return [];
		}
	},
	doRefresh:function(jsonData){
		var me=this;
		me.jsonData=jsonData;
		me.removeAll();
		me.add(me.createChildren());
		me.updateLayout();
	},
	updateTitle:function(){
		var me=this;
		var title=me.callParent();
		if(me.jsonData.filterTitle){
			title=title+"&nbsp;-&nbsp;<i>"+me.jsonData.filterTitle+"</i>";
		}
		return title;
	},
	createGrid:function(){
		var me=this;
		var statistic=me.jsonData.statistic;
		var fields=[];
		fields.push({name:'labelV'});
		fields.push({name:'linkV'});
		if(statistic.optionsH){
			for(var i=0;i<statistic.optionsH.length;i++){
				fields.push({name:'label'+i});
				fields.push({name:'link'+i});
			}
		}
		fields.push({name:'total'});

		var columns=[];
		columns.push({
			xtype:'linkcolumn',
			dataIndex: 'labelV',
			handler:me.clickOnLinkV,
			scope:me,
			isLink:me.isLinkV
		});
		if(statistic.optionsH){
			for(var i=0;i<statistic.optionsH.length;i++){
				columns.push({
					xtype:'linkcolumn',
					dataIndex:'label'+i,
					align:'right',
					handler:me.clickOnLink,
					scope:me,
					isLink:me.isLink,
					postProcessRenderer:me.postProcessRendererLink
				});
			}
		}
		columns.push({
			dataIndex: 'total',
			width:50,
			align:'right',
			renderer:me.formatTotal
		});
		columns.push({
			dataIndex: 'dummy',
			flex:1
		});

		var data=[];
		var firstRow={};
		firstRow.labelV="&nbsp;&nbsp;&nbsp;"+statistic.labelH+"</BR>"+statistic.labelV;
		if(statistic.optionsH){
			for(var i=0;i<statistic.optionsH.length;i++){
				firstRow['label'+i]=statistic.optionsH[i].label;
				firstRow['link'+i]=statistic.optionsH[i].id;
			}
		}
		firstRow['total']=getText('twoDimensionalStatistic.total');
		data.push(firstRow);
		if(statistic.optionsV){
			for(var i=0;i<statistic.optionsV.length;i++){
				var row={};
				var option=statistic.optionsV[i];
				row['labelV']=option.label;
				row['linkV']=option.id;
				for(var j=0;j<statistic.optionsH.length;j++){
					var optionValue=statistic.values[i][j];
					row['label'+j]=optionValue.label;
					row['link'+j]=optionValue.id;
				}
				row['total']=statistic.totalV[i];
				data.push(row);
			}
		}
		var lastRow={};
		lastRow.labelV=getText('twoDimensionalStatistic.total');
		if(statistic.totalH){
			for(var i=0;i<statistic.totalH.length;i++){
				lastRow['label'+i]=statistic.totalH[i];
			}
			lastRow['total']=statistic.totalV[statistic.totalV.length-1];
		}
		data.push(lastRow);
		var store = Ext.create('Ext.data.Store', {
			fields: fields,
			data: data
		});

		return Ext.create('Ext.grid.Panel', {
			cls:'dashboardGrid-noBottomBorder',
			store: store,
			columns:columns,
			hideHeaders:true,
			columnLines :false,
			disableSelection: true,
			border:false,
			bodyBorder:false,
			margin:'0 0 0 0',
			viewConfig: {
				stripeRows: true,
				trackOver  :false
			},
			autoHeight:true
		});
	},
	submit:function(fieldValueH,fieldValueV){
		var me=this;
		var urlStr='itemNavigator.action';
		var params={
			'queryType':2,//dashboard
			'queryID':me.jsonData.dashboardID,
			'dashboardParams.dashboardID':me.jsonData.dashboardID
		};
		
		//overwrite projectID and entity type if dashboard is in browse projects page
		if(me.jsonData&&me.jsonData.projectID){
			params['dashboardParams.project']=me.jsonData.projectID;
		}
		if(me.jsonData&&me.jsonData.releaseID){
			params['dashboardParams.release']=me.jsonData.releaseID;
		}
		if(fieldValueH){
			params['dashboardParams.fieldValueH']=fieldValueH;
		}
		if(fieldValueV){
			params['dashboardParams.fieldValueV']=fieldValueV;
		}
		var dummyForm = Ext.create('Ext.form.Panel', {
			items:[],
			url:urlStr,
			standardSubmit:true
		});
		dummyForm.getForm().submit({
			params:params
		});
	},
	clickOnLinkV:function(record,cellIndex){
		var me=this;
		var link=record.data['linkV'];
		me.submit(null,link);
	},
	isLinkV:function(value,metaData,record,rowIndex,colIndex,store,view){
		var link=record.data['linkV'];
		return (link&&link!=="");
	},
	clickOnLink:function(record,cellIndex){
		var me=this;
		var link=record.data['link'+(cellIndex-1)];
		var idx=link.indexOf(";");
		var fieldValueH=null;
		var fieldValueV=null;
		if(idx===-1){
			fieldValueH=link;
		}else{
			fieldValueH=link.substring(0,idx);
			fieldValueV=link.substring(idx+1);
		}
		me.submit(fieldValueH,fieldValueV);
	},
	isLink:function(value,metaData,record,rowIndex,colIndex,store,view){
		var link=record.data['link'+(colIndex-1)];
		if(value==="0"){
			return false;
		}
		return (link&&link!=="");
	},
	postProcessRendererLink:function(value,metaData,record,rowIndex,colIndex,store,view){
		var htmlStr=value;
		var link=record.data['link'+(colIndex-1)];
		if(link&&link!==""){
			if(value==="0"){
				htmlStr='<span style="color:#CACACA;">0</span>';
			}
		}else{
			htmlStr='<B>'+value+'</B>'
		}
		return htmlStr;
	},
	formatTotal:function(value,metaData,record,rowIndex,colIndex,store,view){
		var htmlStr="";
		if(rowIndex>0&&value){
			htmlStr='<B>'+value+'</B>'
		}else{
			htmlStr=value;
		}
		return htmlStr;
	}
});
