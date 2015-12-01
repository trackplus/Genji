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

Ext.define("js.ext.com.track.setter.emailSender", {
	extend: "Ext.panel.Panel",
	layout:'anchor',
	border:false,
	config: {
		jsonData: {}
	},
	initComponent: function() {
		var me=this;
		me.itemId = me.jsonData.name;
		me.name = me.jsonData.name;
		me.assignment = me.jsonData.assignment;
		me.activityTypeLabel = me.jsonData.activityTypeLabel;
		this.callParent();
		if (this.jsonData.includeFrom) {
			var fromLabel = getText("item.action.sendItemEmail.lbl.from");
			if (me.assignment && me.assignment && me.activityTypeLabel) {
				fromLabel = me.activityTypeLabel + " " + fromLabel;
			}
			me.cmbFrom = CWHF.createCombo(fromLabel, null, {
				anchor:'100%',
				allowBlank:false,
				labelAlign:'right',
				labelWidth:this.jsonData["labelWidth"],
				data:this.jsonData.fromList,
				value:this.jsonData.selectedFrom,
				labelIsLocalized:true
			});
			me.add(me.cmbFrom);
		}
		if (this.jsonData.includeTo) {
			var toLabel = getText("item.action.sendItemEmail.lbl.to");
			if (me.assignment && me.assignment && me.activityTypeLabel) {
				toLabel = me.activityTypeLabel + " " + toLabel;
			}
			me.cmbTo = CWHF.createMultipleSelectPicker(null, null, this.jsonData.toList, this.jsonData.selectedTos, {
				anchor:'100%',
				allowBlank:false,
				margin:'0 0 5 0',
				minWidth:300,
				pickerWidth: 400,
				labelAlign:'right',
				fieldLabel:toLabel,
				labelWidth:this.jsonData["labelWidth"],
				useNull:true,
				useIconCls:true,
				useRemoveBtn:false/*,
				labelIsLocalized:true*/
			});
			me.add(me.cmbTo);
		}
		if (this.jsonData.includeTemplate) {
			var templateLabel = getText("admin.customize.mailTemplate.config.lbl.mailTemplate");
			if (me.assignment && me.assignment && me.activityTypeLabel) {
				templateLabel = me.activityTypeLabel + " " + templateLabel;
			}
			me.cmbTemplate = CWHF.createCombo(templateLabel, null, {
				anchor:'100%',
				allowBlank:false,
				labelAlign:'right',
				labelWidth:this.jsonData["labelWidth"],
				data:this.jsonData.mailTemplates,
				value:this.jsonData.selectedMailTemplate,
				labelIsLocalized:true
			});
			me.add(me.cmbTemplate);
		}
		//me.add(me.cmbFrom, me.cmbTo,me.cmbTemplate);
	},

	getSetterValue:function(){
		var me=this;
		var from = "";
		if (me.cmbFrom) {
			from =me.cmbFrom.getValue();
		}
		var to = "";
		if (me.cmbTo) {
			to=me.cmbTo.getValue();
		}
		var template = "";
		if (me.cmbTemplate) {
			template=me.cmbTemplate.getValue();
		}
		return from+"|"+to+"|"+template;
	},

	getSetterDisplayValue:function() {
		var me=this;
		//var from=me.cmbFrom.getValue();
		var to = "";
		if (me.cmbTo) {
			to=me.cmbTo.getDisplayValue();
		}
		var templateStr=null;
		if (me.cmbTemplate) {
			var template=me.cmbTemplate.getValue();
			if (template){
				var r = me.cmbTemplate.getStore().find('id',template);
				templateStr= me.cmbTemplate.getStore().getAt(r).get('label');
			}
		}
		var s=getText('item.action.sendItemEmail.lbl.to')+" "+to;
		if (templateStr) {
			s=s+" "+getText('admin.customize.mailTemplate.config.lbl.mailTemplate')+":"+templateStr;
		}
		return s;
	}
});
