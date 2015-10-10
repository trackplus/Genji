/* Copyright (C) 2012 Trackplus
 * $Id$
 */

Ext.define('Ext.ux.LinkComponent', {
	extend: 'Ext.Component',
	alias: ['widget.linkcomponent'],
	config:{
		handler:null,
		scope:null,
		clsLink:'synopsis_blue',
		label:'',
		prefix:null,
		suffix:null
	},
	initComponent: function(){
		var me=this;
		me.initHTML();
		me.callParent();
		me.addListener('afterrender',function(comp){
			comp.getEl().addListener('click',me.elementClick,me);
		},me);
	},
	initHTML:function(){
		var me=this;
		if(me.label!=null){
			me.html='<a class="'+me.clsLink+'" href="javascript:void(0)">'+me.label+'</a>'
			if(me.prefix!=null){
				me.html=me.prefix+me.html;
			}
			if(me.suffix!=null){
				me.html=me.html+me.suffix;
			}
		}else{
			me.html='';
		}
	},
	setLabel:function(label){
		var me=this;
		me.label=label;
		me.initHTML();
		me.update(me.html);
	},
	setClsLink:function(clsLink){
		var me=this;
		me.clsLink=clsLink;
		me.initHTML();
		me.update(me.html);
	},

	elementClick:function(e){
		var me=this;
		var match = e.getTarget().className.indexOf(me.clsLink)!=-1;
		if(match){
			if(me.handler!=null){
				me.handler.call(me.scope||me);
			}
			e.stopEvent();
			return false;
		}
	},
	getMyLabel:function(){
		var me=this;
		return me.label;
	}
});
