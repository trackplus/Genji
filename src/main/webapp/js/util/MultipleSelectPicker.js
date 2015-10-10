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


Ext.define('com.trackplus.util.MultipleSelectPicker',{
	extend:'com.trackplus.util.AbstractMultiplePicker',
	config:{
		multiSelect:true,
		iconUrlPrefix:null,
		useIconCls:false
	},
	displayField:'label',
	valueField:'id',
	ignoreSelection:0,
	createStore:function(){
		var me=this;
		return Ext.create('Ext.data.Store', {
				data	:(me.data==null?[]:me.data),
				fields	: [{name:'id', type:'int',useNull:true}, {name:'label', type:'string'},{name:'icon', type:'string'},{name:'iconCls', type:'string'}],
				autoLoad: false
		});
	},
	updateData:function(data){
		var me=this;
		me.data=data;
		if(me.store!=null){
			me.store.loadData.call(me.store,data,false);
		}
	},
	createBoundList:function(){
		var me=this;
		var tpl='';
		if(me.multiSelect==true){
			tpl+='<div class="x-combo-list-item"><img src="' + Ext.BLANK_IMAGE_URL + '" class="chkCombo-default-icon chkCombo" /> ';
		}
		if(me.iconUrlPrefix!=null){
			var urlStr=me.iconUrlPrefix+'{'+me.valueField+'}';
			tpl+='<img  style="width:16px;height:16px;vertical-align: bottom; margin-bottom: 2px;margin-right: 5px;" src="'+urlStr+'"/>';
		}else if(me.useIconCls){
			tpl+='<img  style="width:16px;height:16px;vertical-align: bottom; margin-bottom: 2px;margin-right: 5px;" src="data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==" class="{iconCls}"/>';
		}
		tpl+='{'+ me.displayField+'} </div>';

		var listCfg = {
			xtype: 'boundlist',
			region:'center',
			autoScroll:true,
			border:false,
			selModel: {
				mode: me.multiSelect ? 'SIMPLE' : 'SINGLE'
			},
			store: me.store,
			displayField: me.displayField,
			focusOnToFront: false,
			pageSize: me.pageSize,
			getInnerTpl : function() {
				return tpl;
			},
			//minHeight:75,
			maxHeight: 200
		};

		return Ext.widget(listCfg);
	},
	getSelectedItemsCount:function(){
		var me=this;
		return me.boundList.getSelectionModel().getCount();
	},

	addBoundListListeners:function(){
		var me=this;
		me.boundList.getSelectionModel().on('beforeselect',me.onListBeforeSelect,me);
		me.boundList.getSelectionModel().on('selectionchange',me.onListSelectionChange,me);
		me.boundList.on('refresh',me.onListRefresh,me);
	},
	onListBeforeSelect:function(){
		var me=this;
		if(me.maxSelectionCount!=null&&me.maxSelectionCount>0){
			var selectedIssues=me.getSelectedItemsCount();
			return selectedIssues<me.maxSelectionCount;
		}
		return true;
	},
	onListSelectionChange:function(list,records){
		this.selectionChange(records);
	},
	findRecord: function(field, value) {
		var me=this;
		if(me.store==null){
			me.store=me.createStore();
		}
		var ds = this.store,
			idx = ds.findExact(field, value);
		return idx !== -1 ? ds.getAt(idx) : false;
	},
	syncSelection: function() {
		var me = this,
			boundList = me.boundList,
			selection, selModel,
			values = me.valueModels || [],
			vLen  = values.length, v, value;
	  	if (boundList) {
			// From the value, find the Models that are in the store's current data
			selection = [];
			for (v = 0; v < vLen; v++) {
				value = values[v];

				if (value && value.isModel && me.store.indexOf(value) >= 0) {
					selection.push(value);
				}
			}

			// Update the selection to match
			me.ignoreSelection++;
			selModel = boundList.getSelectionModel();
			selModel.deselectAll();
			if (selection.length) {
				selModel.select(selection, false, true);
			}
			me.ignoreSelection--;
		}
	},
	clearSelection:function(){
		var me=this;
        if (me.boundList!=null) {
		    me.boundList.getSelectionModel().deselectAll(true);
        }
		me.setValue(null);
	},
	selectAll:function(){
		var me=this;
		if (me.boundList!=null) {
			me.boundList.getSelectionModel().selectAll();
		}
		//me.setValue(null);
	},
	filter:function(value){
		var me = this;
		if (Ext.isEmpty(value)) {
			me.clearFilter();
		}else{
			var matches = [];
			var property = me.displayField;
			var escapeRe      = Ext.String.escapeRegex;
			//value = '^' + escapeRe(value);
			value = escapeRe(value);
			var re=new RegExp(value, "ig");

			for(var i=0;i<me.store.getCount();i++){
				var item=me.store.getAt(i);
				var viewNode = Ext.fly(me.boundList.getNode(item));
				viewNode.setVisibilityMode(Ext.Element.DISPLAY);
				if(item.get(property).match(re)){
					viewNode.setVisible(true);
				}else{
					viewNode.setVisible(false);
				}
			}
		}
		me.picker.doLayout();
	},
	clearFilter: function () {
		var me = this;
		for(var i=0;i<me.store.getCount();i++){
			var item=me.store.getAt(i);
			var viewNode = Ext.fly(me.boundList.getNode(item));
			viewNode.setVisibilityMode(Ext.Element.DISPLAY);
			viewNode.setVisible(true);
		}
	}
});

