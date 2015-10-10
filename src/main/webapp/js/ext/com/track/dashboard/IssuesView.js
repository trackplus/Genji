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

Ext.define('js.ext.com.track.dashboard.IssuesView',{
	extend:'js.ext.com.track.dashboard.DashboardRenderer',
	mixins:{
		navigable:'com.trackplus.itemNavigator.TreeNavigableItem'
	},
	haveErrors:false,
	constructor: function(config){
		this.callParent(arguments);
		this.mixins.navigable.constructor.call(this);
	},

	initComponent : function(){
		var me=this;
		var errors=me.jsonData.errors;
		if(errors!=null&&errors.length>0){
			me.haveErrors=true;
			me.addCls("dashboardError");
			me.items=[{xtpe:'component',border:false,html:me.getErrorsHtml(errors)}];
			me.bodyPadding=5;
		}
		if(me.jsonData.tooManyItems==true){
			me.items=[me.createErrorCmp(getText('cockpit.err.tooManyItems'))];
			me.haveErrors=true;
		}
		if(!me.haveErrors){
			me.createTreeGrid();
			me.items=[me.treeGrid];
		}
		me.callParent();
		if(me.jsonData.filterTitle!=null){
			if (me.title != null && !""==me.title) {
			   me.title=me.title+"&nbsp;-&nbsp;"
			}
			me.title=me.title+"<i>"+me.jsonData.filterTitle+"</i>";
		}
	},
	getErrorsHtml:function(errors){
		var htmlStr="";
		for(var i=0;i<errors.length;i++){
			htmlStr+=errors[i].label+"</br>";
		}
		return htmlStr;
	},

	getRowClass: function(record, rowIndex, rp, ds){
     	var me=this;
		var cls="";
		if(record.data['group']==true){//grouping
			cls+=" reportsTableGrouping";
			if(record.data['cssColorClassGroup']!=null){
				cls+=" "+record.data['cssColorClassGroup'];
			}
		}
		if(record.data['cssColorClass']!=null){
			cls+=" "+record.data['cssColorClass'];
		}
		if(me.queryFieldCSS!=null&&record.data['queryFieldCSS']!=null){
			cls+=' queryFieldCSS rowCls_'+me.queryFieldCSS+"_"+ record.data['queryFieldCSS'];
		}
		return cls;
     },

	createTreeGrid:function(){
		var me=this;
		var shortFields=me.jsonData.shortFields;
		var fields=[];
		var sfield;
		for(var i=0;i<shortFields.length;i++){
			sfield=shortFields[i];
			fields.push({name:'f'+sfield.reportField,type:sfield.extJsType,useNull:true});
			if(sfield.so===true){
				fields.push({name:'f_so'+sfield.reportField,type:'int',useNull:true});
			}
		}
		fields.push({name:'workItemID',type:'int'});
		fields.push({name:'attachmentIds'});
		fields.push({name:'projectID',type:'int'});
		fields.push({name:'workItemIndex',type:'int'});
		fields.push({name:'projectName'});
		fields.push({name:'longFields'});
		fields.push({name:'iconCls'});
		fields.push({name:'cssColorClass'});

		var treeData=me.jsonData.data;
		var sortField="f"+shortFields[0].reportField;
		var sortDirection='ASC';
		var sorters=[];
		var sortField="f_so12";
		var sortDirection='ASC';
		sorters.push({
			property:sortField,
			direction :sortDirection,
			sorterFn:function(o1,o2){
				var me = this;
				var v1 = me.getRoot(o1)['f_so12'];
				var v2 = me.getRoot(o2)['f_so12'];
				return v1 > v2 ? 1 : (v1 < v2 ? -1 : 0);
			}
		});
		var store = Ext.create('Ext.data.TreeStore', {
			fields: fields,
			root: {
				xpanded: true,
				text:"",
				children:treeData
			},
			sorters:sorters
		});
		var columnModel1=new Array(0);
		var layoutData;
		var colHeader;
		for(var i=0;i<shortFields.length;i++){
			layoutData=shortFields[i];
			var colType='Ext.grid.column.Column';
			colHeader=layoutData.label;
			var colCls=null;
			var colResizable=true;
			if(i==0){
				colType='Ext.tree.Column';
			}else{
				if(layoutData.renderContentAsImg==true){
					colCls='headerIcon'+layoutData.reportField;
					colHeader='&nbsp;';
					colResizable=false;
				}
			}
			var col=Ext.create(colType,{
				header:colHeader,
				width:layoutData.fieldWidth,
				dataIndex:'f'+layoutData.reportField,
				tdCls:'simpleTreeGridCell',
				cls:colCls,
				resizable:colResizable,
				menuDisabled:true,
				sortable:true,
				hideable:false,
				draggable:false,
				hidden:false
			});
			if(layoutData.so==true){
				col.getSortParam=function(){
					return 'f_so'+this.dataIndex.substring(1);
				};
			}
			/*if(layoutData.reportField==17&&i!=0){
				//synopsis
				col.renderer=function(value,metaData,record,rowIndex,colIndex,store,view){
					var urlStr='printItem.action?key='+record.data.workItemID;
					var synopsisClass='synopsis_blue newWindow';
					return '<a href="'+urlStr+'" class="'+synopsisClass+'" target="printItem'+record.data.workItemID+'">'+value+'</a>';
				};
			}*/
			if(layoutData.renderContentAsImg==true){
				col.width=25;
				col.tdCls='iconTreeGridCell';
				col.renderer=function(value,metaData,record,rowIndex,colIndex,store,view){
					var reportField=this.columns[colIndex].dataIndex.substring(1);
					if(reportField==-1006){
						//private symbol
						return '<img src="'+com.trackplus.TrackplusConfig.icon16Path+value+'" style="padding-top:2px;">';
					}else{
						if(value!=null&&value!=''){
							var srcImg="optionIconStream.action?fieldID="+reportField+"&optionID="+value;//com.trackplus.TrackplusConfig.listIconsPath+reportField+"/"+value;
							return '<img src="'+srcImg+'" style="padding-top:2px;">';
						}else{
							return "";
						}
					}
				};
			}
			columnModel1.push(col);
		}
		// create the grid
		me.treeGrid =  Ext.create('Ext.tree.Panel', {
			store	  : store,
			columns	: columnModel1,
			rootVisible: false,
			border:false,
			bodyBorder:false,
			columnLines :true,
			lines :true,
			viewConfig: {
				stripeRows: true,
				getRowClass: function (record, rowIndex, rp, ds) {
            	    return me.getRowClass.call(me,record, rowIndex, rp, ds);
            	}
			}
		});
		me.treeGrid.addListener('celldblclick',me.onGridItemDblClick,me);
		store.addListener("refresh",me.onStoreRefresh,me);
	},
	onGridItemDblClick:function(view, td,cellIndex,record, tr, rowIndex,e){
		var me=this;
		var data=record.data;
		var workItemID=data['workItemID'];
		var workItemIndex=data['workItemIndex'];
		me.openItem.call(me,workItemID,-2,workItemIndex);
	},
	successHandler:function(data,successExtra){
		this.refreshDashboard(data,successExtra);
	},
	getTreeGrid:function(){
		return this.treeGrid;
	},
	itemChangeHandler:function(fields){
		var me=this;
		if(me.containsFields(fields)){
			me.refreshDashboard();
		}
	},
	refreshErrorHandler:function(){
		var me=this;
		me.haveErrors=true;
		me.removeCls("dashboardError");
		me.callParent(arguments);
		me.setTitle(me.originalTitle);
	},
	doRefresh:function(jsonData){
		var me=this;
		me.jsonData=jsonData;
		var errors=me.jsonData.errors;
		if(errors!=null&&errors.length>0){
			me.haveErrors=true;
			me.removeAll(true);
			me.addCls("dashboardError");
			me.add({xtype:'component',border:false,html:me.getErrorsHtml(errors)});
		}else{
			if(me.haveErrors){
				me.haveErrors=false;
				me.removeAll(true);
				me.removeCls("dashboardError");
				me.createTreeGrid();
				me.add(me.treeGrid);
			}
			else{

				/*
				TODO do not create treeGrid again.
				In next version of extjs maybe will be fixed the treeStore reload data
				 */
				//me.treeGrid.getStore().getRootNode().removeAll();
				//me.treeGrid.getStore().getRootNode().appendChild(jsonData.data);
				me.removeAll(true);
				me.createTreeGrid();
				me.add(me.treeGrid);


				if(me.jsonData.filterTitle!=null){
					me.setTitle(me.originalTitle+"&nbsp;-&nbsp;<i>"+me.jsonData.filterTitle+"</i>");
				}else{
					me.setTitle(me.originalTitle);
				}
			}

		}
		me.doLayout();
	},
	updateTitle:function(){
		var me=this;
		var title=me.callParent();
		if(me.jsonData.filterTitle!=null){
			title=title+"&nbsp;-&nbsp;<i>"+me.jsonData.filterTitle+"</i>";
		}
		return title;
	}
});
