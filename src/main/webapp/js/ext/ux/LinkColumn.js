/* Copyright (C) 2012 Trackplus
 * $Id$
 */

Ext.define('Ext.ux.LinkColumn', {
	extend: 'Ext.grid.column.Column',
	alias: ['widget.linkcolumn'],
	config:{
		handler:null,
		scope:null,
		isLink:null,
		suffix:null,
		postProcessRenderer:null,
		clsLink:'synopsis_blue'
	},
	//clsLink:'synopsis_blue',
	constructor: function(config) {
		var me = this;
		var cfg = Ext.apply({}, config);
		me.callParent([cfg]);
		me.renderer = function(value,metaData,record,rowIndex,colIndex,store,view) {
			var renderAsLink=true
			if(me.isLink!=null&&Ext.isFunction(me.isLink)){
				renderAsLink=me.isLink.call(me.scope||me,value,metaData,record,rowIndex,colIndex,store,view);
			}
			var htmlStr="";
			if(renderAsLink){
				htmlStr='<a class="'+me.clsLink+'" href="javascript:void(0)">'+value+'</a>'
			}else{
				htmlStr=value;
			}
			if(me.suffix!=null){
				htmlStr+=me.suffix;
			}
			//v = Ext.isFunction(cfg.renderer) ? cfg.renderer.apply(this, arguments)||'' : '';
			if(me.postProcessRenderer!=null){
				htmlStr=me.postProcessRenderer.call(me.scope||me,htmlStr,metaData,record,rowIndex,colIndex,store,view);
			}
			return htmlStr;
		};
	},
	processEvent : function(type, view, cell, recordIndex, cellIndex, e){
		var me = this;
		var match = e.getTarget().className.indexOf(me.clsLink)!=-1;
		if (match) {
			if (type == 'click') {
				if(me.handler!=null){
					var record = view.panel.store.getAt(recordIndex);
					me.handler.call(me.scope||me,record,cellIndex);
				}
				e.stopEvent();
				return false;
			}else if (type == 'mousedown') {
				return false;
			}
		}
		return me.callParent(arguments);
	}
});
