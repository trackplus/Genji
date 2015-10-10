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


Ext.define('com.trackplus.util.TreeDropZone',{
	extend: 'Ext.dd.DropZone',
	config:{
		view:null,
		isValidNode:null,
		isValidNodeScope:null
	},
	expandDelay : 600,
	constructor: function(config) {
		var me = this;
		Ext.apply(me, config);
		me.callParent([me.view.el]);
	},
	getTargetFromEvent: function(e) {
		var node = e.getTarget(this.view.getItemSelector()),
		mouseY, nodeList, testNode, i, len, box;
		if (!node) {
			mouseY = e.getPageY();
			for (i = 0, nodeList = this.view.getNodes(), len = nodeList.length; i < len; i++) {
				testNode = nodeList[i];
				box = Ext.fly(testNode).getBox();
				if (mouseY <= box.bottom) {
					return testNode;
				}
			}
		}
		return node;
	},
	expandNode : function(node) {
		var view = this.view;
		if (!node.isLeaf() && !node.isExpanded()) {
			view.expand(node);
			this.expandProcId = false;
		}
	},
	queueExpand : function(node) {
		this.expandProcId = Ext.Function.defer(this.expandNode, this.expandDelay, this, [node]);
	},
	fireViewEvent: function() {
		var me = this,
			result;

		me.lock();
		result = me.view.fireEvent.apply(me.view, arguments);
		me.unlock();
		return result;
	},
	invalidateDrop: function() {
		if (this.valid) {
			this.valid = false;
		}
		if(this.overRecord!=null){
			Ext.fly(this.overRecord).removeCls('my-row-highlight-class');
		}
	},

	onNodeEnter : function(target, dd, e, data){
		if(this.isValidDropPoint(target,  dd, e, data)) {
			Ext.fly(target).addCls('my-row-highlight-class');
		}
	},

	onNodeOut : function(target, dd, e, data){
		Ext.fly(target).removeCls('my-row-highlight-class');
	},

	onNodeOver : function(node, dragZone, e, data) {
		var me=this;
		var returnCls = this.dropNotAllowed;
		var targetNode = this.view.getRecord(node);

		this.cancelExpand();
		if (!this.expandProcId&&!targetNode.isLeaf() && !targetNode.isExpanded()) {
			this.queueExpand(targetNode);
		}

	if(this.isValidDropPoint(node,  dragZone, e, data)) {
			this.valid = true;
			this.overRecord = targetNode;
			returnCls = Ext.baseCSSPrefix +'dd-drop-ok';
		} else {
			this.valid = false;
		}

		this.currentCls = returnCls;
		return returnCls;
	},
	cancelExpand : function() {
		if (this.expandProcId) {
			clearTimeout(this.expandProcId);
			this.expandProcId = false;
		}
	},

	isValidDropPoint : function(node,  dragZone, e, data) {
		var me=this;
		if (!node || !data.item) {
			return false;
		}
		var view = this.view;
		var targetNode = view.getRecord(node);
		if(me.isValidNode!=null&&(typeof me.isValidNode == 'function')){
			if (this.isValidNodeScope==null) {
				return me.isValidNode.call(me,targetNode);
			} else {
				return me.isValidNode.call(this.isValidNodeScope,targetNode);
			}
		}
		var canDrop=targetNode.data['canDrop'];
		if(canDrop!=null){
			return canDrop===true;
		};
		return true;
	},

	onNodeDrop: function(node, dragZone, e, data) {
		var me = this,
			dropped = false,
			processDrop = function () {
				me.invalidateDrop();
				dropped = true;
				me.fireViewEvent('drop', node, data, me.overRecord);
			},
			performOperation = false;
		if (me.valid) {
			performOperation = me.fireViewEvent('beforedrop', node, data, me.overRecord, processDrop);
			if (performOperation !== false) {
				// If the processDrop function was called in the event handler, do not do it again.
				if (!dropped) {
					processDrop();
				}
			}
		}
		return performOperation;
	}
});
