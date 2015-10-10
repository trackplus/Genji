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


Ext.define('com.aurel.trackplus.field.FieldConfig',{
	extend:'Ext.Base',
	config: {
		fieldID:null,
		parameterCode:null,
		label:null,
		tooltip:null,
		required:false,
		readonly:false,
		invisible:false,
		jsonData:{}
	},
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	}
});
Ext.define('com.aurel.trackplus.field.TypeRenderer',{
	extend:'Ext.Base',
	mixins: {
		observable: 'Ext.util.Observable'
	},
	config:{
		workItemID:null,
		projectID:null,
		fieldConfig:null,
		value:null,
		displayValue:null,
		name:null,
		labelHAlign:null,
		labelVAlign:null,
		valueHAlign:null,
		valueVAlign:null,
		hideLabel :false
	},
	view:null,
	tip:null,
	requiredHtmlTxt:'', //'<span style="color:#FF0000;">*</span>',
	requiredMarkup:'<span class="required">&nbsp;</span>',
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		this.mixins.observable.constructor.call(this, config);
		this.addEvents('valueChange');
		this.addEvents('dblclick');
		this.addEvents('layoutChange');
		this.addEvents('afterRender');
	},
	getOriginalValue:function(){
		return this.value;
	},
	beforeSubmit:function(){
	},
	getValue:function(){
		var me=this;
		var value=null;
		if(me.view!=null){
			if(me.view.getSubmitValue!=null&&Ext.isFunction(me.view.getSubmitValue)){
				value=me.view.getSubmitValue.call(me.view);
			}
		}
		return value;
	},
	getView:function(){
		var me=this;
		if(me.view==null){
			me.view=me.createView();
			me.view.addListener('render',function(c){
				c.getEl().on('dblclick', function(){
					me.itemDblClick.call(me,c);
				});
			});
		}
		return me.view;
	},
	itemDblClick:function(){
		var me=this;
		me.fireEvent('dblclick',me.fieldID);
	},
	createView:function(){
		return Ext.create('Ext.form.Label',{});
	},
	update:function(config){
		var me=this;
		Ext.apply(me, config);
		me.refreshView();
	},
	refreshView:function(){
	},
	focus:function(){
		var me=this;
		if(me.view!=null){
			if(me.view.focus!=null&&Ext.isFunction(me.view.focus)){
				me.view.focus.call(me.view);
			}
		}
	},
	markInvalid:function(str){
		var me=this;
		if(me.view!=null){
			if(me.view.markInvalid!=null&&Ext.isFunction(me.view.markInvalid)){
				me.view.markInvalid.call(me.view,str);
			}
		}
	},
	markConflict:function(newValue){
		var me=this;
		if(newValue==null){
			newValue='';
		}
		var str=getText('common.err.concurrentSave.conflict',newValue);
		if(me.view!=null){
			me.view.addCls('fieldConflict');
			me.setToolTip(str);
		}
	},
	markModifiedByMe:function(){
		var me=this;
		var str=getText('common.err.concurrentSave.modifiedByMe');
		if(me.view!=null){
			me.view.addCls('modifiedByMe');
			me.setToolTip(str);
		}
	},
	markModifiedByOther:function(newValue){
		var me=this;
		var str=getText('common.err.concurrentSave.modifiedByOther');
		if(me.view!=null){
			me.view.addCls('modifiedByOther');
			me.view.setValue(newValue);
			me.setToolTip(str);
		}
	},
	setToolTip:function(str){
		var me=this;
		if(str==null){
			if(me.tip==null){
				me.tip.destroy();
				delete me.tip;
			}
			return;
		}
		if(me.view!=null){
			var domEl=me.view.getEl();
			if(domEl!=null){
			if(me.tip==null){
					var tip = Ext.create('Ext.tip.ToolTip', {
						target: domEl,
						html: str
					});
				}else{
					me.tip.update(str);
				}
			}
		}
	}

});

Ext.define('com.aurel.trackplus.field.LabelTypeRenderer',{
	extend:'com.aurel.trackplus.field.TypeRenderer',
	createView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		return  Ext.create('Ext.form.field.Display',{
			fieldLabel: label,
			labelAlign:me.labelHAlign,
			hideLabel :me.hideLabel,
			cls :'labelVerticalAlign-'+me.labelVAlign+
				' valueVerticalAlign-'+me.valueVAlign+' valueAlign-'+me.valueHAlign,
			labelStyle:{overflow:'hidden',width:'300px'},
			fieldStyle : {
				height:'auto',
				margin: '0 0 0 0'
			},
			value:me.displayValue,
			hidden:hidden
		});
	},
	refreshView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		me.view.setFieldLabel(label);
		me.view.setValue(me.displayValue);
		me.view.hidden=hidden;
	}
});

Ext.define('com.aurel.trackplus.field.TextFieldTypeRenderer',{
	extend:'com.aurel.trackplus.field.TypeRenderer',
	createView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var name=me.fieldConfig.name;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			// label=label+me.requiredHtmlTxt;
			requiredMarkup='';
		}
		return Ext.create('Ext.form.field.Text',{
			fieldLabel: label,
			afterLabelTextTpl:requiredMarkup,
			labelStyle:{overflow:'hidden'},
			labelAlign:me.labelHAlign,
			hideLabel :me.hideLabel,
			cls :'labelVerticalAlign-'+me.labelVAlign+
				' valueVerticalAlign-'+me.valueVAlign+' valueAlign-'+me.valueHAlign,
			name: name,
			allowBlank:!required,
			value:me.value,
			hidden:hidden
		});
	},
	refreshView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			requiredMarkup='';
		}
		me.view.setFieldLabel(label);
		me.view.afterLabelTextTpl=requiredMarkup;
		me.view.setValue(me.value);
		me.view.allowBlank=!required;
		me.view.hidden=hidden;
	}
});

Ext.define('com.aurel.trackplus.field.TextAreaTypeRenderer',{
	extend:'com.aurel.trackplus.field.TypeRenderer',
	createView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var name=me.fieldConfig.name;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			requiredMarkup='';
		}
		var txtValue='';
		if(me.value!=null){
			txtValue=me.value;
		}
		me.txtArea=Ext.create('Ext.form.field.TextArea',{
			//fieldLabel: label,
			//labelAlign:me.labelHAlign,
			//labelStyle:{overflow:'hidden'},
			hideLabel:true,
			name: name,
			allowBlank:!required,
			value:txtValue,
			hidden:hidden,
			style:{
				width: '100%'
			}

		});
		/*return Ext.create('Ext.form.field.Text',{
			fieldLabel: label,
			labelStyle:{overflow:'hidden'},
			labelAlign:me.labelHAlign,
			name: name,
			allowBlank:!required,
			value:me.value,
			hidden:hidden
		});*/

		//return me.txtArea;
		return Ext.create('Ext.form.FieldContainer',{
			combineErrors: true,
			fieldLabel: label,
			afterLabelTextTpl: requiredMarkup,
			labelAlign:me.labelHAlign,
			cls :'labelVerticalAlign-'+me.labelVAlign+
				' valueVerticalAlign-'+me.valueVAlign+' valueAlign-'+me.valueHAlign,
			labelStyle:{overflow:'hidden'},
			layout:'anchor',
			defaults: {
				hideLabel: true
			},
			style:{
				width: '100%'
			},
			items :[me.txtArea],
			hidden:hidden
		});

	},
	refreshView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			requiredMarkup='';
		}
		me.view.setFieldLabel(label);
		me.view.afterLabelTextTpl=requiredMarkup;
		var txtValue='';
		if(me.value!=null){
			txtValue=me.value;
		}
		me.txtArea.setValue(txtValue);
		me.txtArea.allowBlank=!required;
		me.view.hidden=hidden;
	}
});

Ext.define('com.aurel.trackplus.field.SynopsisTypeRenderer',{
	extend:'com.aurel.trackplus.field.TypeRenderer',
	createView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var name=me.fieldConfig.name;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		return Ext.create('Ext.form.field.Text',{
			fieldLabel: label,
			afterLabelTextTpl: requiredMarkup,
			labelAlign:me.labelHAlign,
			hideLabel :me.hideLabel,
			cls :'labelVerticalAlign-'+me.labelVAlign+
				' valueVerticalAlign-'+me.valueVAlign+' valueAlign-'+me.valueHAlign,
			name: name,
			allowBlank:!required,
			value:me.value,
			width:500,
			hidden:hidden
		});
	},
	refreshView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			requiredMarkup='';
		}
		me.view.setFieldLabel(label);
		me.afterLabelTextTpl = requiredMarkup;
		me.view.setValue(me.value);
		me.view.allowBlank=!required;
		me.view.hidden=hidden;
	}
});

Ext.define('com.aurel.trackplus.field.DateTypeRenderer',{
	extend:'com.aurel.trackplus.field.TypeRenderer',
	createView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var name=me.fieldConfig.name;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			requiredMarkup='';
		}
		var data=me.fieldConfig.jsonData;

		var dateField = CWHF.createDateField(label, name,{
			labelIsLocalized:true,
			allowBlank:!required,
			value:me.value,
			labelAlign:me.labelHAlign,
			afterLabelTextTpl: requiredMarkup,
			hideLabel :me.hideLabel,
			cls :'labelVerticalAlign-'+me.labelVAlign+
				' valueVerticalAlign-'+me.valueVAlign+' valueAlign-'+me.valueHAlign,
			hidden:hidden, labelWidth:100, width:210,
			//same as format because the submit is parsed as a gui date string (with 4 digits year)
			//(not interpreted by struts2 (two digits year) to convert directly into a Date type object)
			submitFormat: com.trackplus.TrackplusConfig.DateFormat
		});
		dateField.addListener('change',me.valueChange,me);
		//dateField.addListener('afterRender',me.afterRender,me);
		return dateField;
	},
	refreshView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			requiredMarkup='';
		}
		me.view.setFieldLabel(label);
		me.view.afterLabeltextTpl=requiredMarkup;
		me.view.setValue(me.value);
		me.view.allowBlank=!required;
		me.view.hidden=hidden;
	},
	valueChange:function(fieldCmp, newValue, oldValue){
		var me=this;
		var fieldValues={};
		fieldValues["f"+me.fieldID]=newValue;
		var oldFieldValues = {};
		oldFieldValues["f"+me.fieldID]=oldValue;
		me.fireEvent("valueChange",me.fieldID,fieldValues,oldFieldValues, me);
	}/*,
	afterRender:function(fieldCmp){
		var me=this;
		if (me.fieldID==20 || me.fieldID==30) {
			var fieldValues={};
			fieldValues["f"+me.fieldID]=fieldCmp.getValue();
			me.fireEvent("valueChange", me.fieldID, fieldValues, null, me);
		}
	}*/
});

Ext.define("com.aurel.trackplus.field.EndDateRenderer",{
	extend:"com.aurel.trackplus.field.DateTypeRenderer",
	config: {
		durationFieldID: 33,
		startDateFieldID: 19
	},
	constructor : function(config) {
		var me=this;
		this.initConfig(config);
		me.callParent(arguments);
	},
	refreshDependentFields:function(fieldID, fieldValues, oldFieldValues, renderer, modelFieldValues, isFromGantt) {
		var me=this;
		var durationFieldID = renderer.durationFieldID;
		var startDateFieldID = renderer.startDateFieldID;
		var newEndDate = fieldValues["f"+fieldID];
		if(!isFromGantt) {
			modelFieldValues["f"+fieldID] = Ext.Date.format(newEndDate, com.trackplus.TrackplusConfig.DateFormat);;
		}

		var oldEndDate = null;
		if (oldFieldValues!=null) {
			oldEndDate = me.getDateFromModel(oldFieldValues, "f"+fieldID, isFromGantt);//oldFieldValues["f"+fieldID];
		}
		var startDate = me.getDateFromModel(modelFieldValues, "f"+startDateFieldID, isFromGantt);//modelFieldValues["f"+startDateFieldID];
		if (newEndDate==null || newEndDate.length==0 || startDate==null || startDate.length==0) {
			me.updateField(durationFieldID, null, "", modelFieldValues);
		} else {
			if (newEndDate !== oldEndDate) {
				var startDate = me.getDateFromModel(modelFieldValues, "f"+startDateFieldID, isFromGantt);//modelFieldValues["f"+startDateFieldID];
				var duration = me.getDurationBetweenDates(startDate, newEndDate);
				me.updateField(durationFieldID, duration, duration, modelFieldValues);
			}
		}
	}
});

Ext.define("com.aurel.trackplus.field.EndDateTargetRenderer",{
	extend:"com.aurel.trackplus.field.EndDateRenderer",
	config: {
		durationFieldID: 34,
		startDateFieldID: 29
	},
	constructor : function(config) {
		var me=this;
		this.initConfig(config);
		me.callParent(arguments);
	}
});

Ext.define("com.aurel.trackplus.field.StartDateRenderer", {
	extend:"com.aurel.trackplus.field.DateTypeRenderer",
	config: {
		durationFieldID: 33,
		endDateFieldID: 20
		},
	constructor : function(config) {
		var me=this;
		this.initConfig(config);
		me.callParent(arguments);
	},
	refreshDependentFields:function(fieldID, fieldValues, oldFieldValues, renderer, modelFieldValues, isFromGantt) {
		var me=this;
		var durationFieldID = renderer.durationFieldID;
		var endDateFieldID = renderer.endDateFieldID;
		var newStartDate = fieldValues["f"+fieldID];
		if(!isFromGantt) {
			modelFieldValues["f"+fieldID] = Ext.Date.format(newStartDate, com.trackplus.TrackplusConfig.DateFormat);
		}

		var oldStartDate = null;
		if (oldFieldValues!=null) {
			oldStartDate = me.getDateFromModel(oldFieldValues, "f"+fieldID, isFromGantt);//oldFieldValues["f"+fieldID];
		}
		var duration = modelFieldValues["f"+durationFieldID];
		if (duration!=null) {
			var endDate = me.getDateFromModel(modelFieldValues, "f"+endDateFieldID, isFromGantt);//modelFieldValues["f"+endDateFieldID];
			if (newStartDate==null || newStartDate.length==0 || endDate==null || endDate.length==0) {
				me.updateField(durationFieldID, null, "", modelFieldValues);
			} else {
				if (newStartDate !== oldStartDate) {
					endDate = this.addWeekdays(newStartDate, duration, true);
					me.updateField(endDateFieldID, endDate, endDate, modelFieldValues);
				}
			}
		}
	}
});

Ext.define("com.aurel.trackplus.field.StartDateTargetRenderer", {
	extend:"com.aurel.trackplus.field.StartDateRenderer",
	config: {
		durationFieldID: 34,
		endDateFieldID: 30
	},
	constructor : function(config) {
		var me=this;
		this.initConfig(config);
		me.callParent(arguments);
	}
});

Ext.define('com.aurel.trackplus.field.HtmlReadOnlyTypeRenderer',{
	extend:'com.aurel.trackplus.field.TypeRenderer',
	createView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		return  Ext.create('Ext.form.field.Display',{
			fieldLabel: label,
			labelAlign:me.labelHAlign,
			hideLabel :me.hideLabel,
			cls :'olist-ulist htmlReadOnlyField labelVerticalAlign-'+me.labelVAlign+
				' valueVerticalAlign-'+me.valueVAlign+' valueAlign-'+me.valueHAlign,
			labelStyle:{overflow:'hidden'},
			fieldStyle : {
				height:'auto'
			},
			value:me.displayValue,
			hidden:hidden
		});
	},
	refreshView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		me.view.setFieldLabel(label);
		me.view.setValue(me.displayValue);
		me.view.hidden=hidden;
	}
});
Ext.define('com.aurel.trackplus.field.HtmlTypeRenderer',{
	extend:'com.aurel.trackplus.field.TypeRenderer',
	createView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var name=me.fieldConfig.name;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			requiredMarkup='';
		}
		var txtValue='';
		if(me.value!=null){
			txtValue=me.value;
		}
		var otherCfg={
			fieldLabel: label,
			afterLabelTextTpl: requiredMarkup,
			labelAlign:me.labelHAlign,
			hideLabel :me.hideLabel,
			cls :'labelVerticalAlign-'+me.labelVAlign+
				' valueVerticalAlign-'+me.valueVAlign+' valueAlign-'+me.valueHAlign,
			labelStyle:{overflow:'hidden'},
			value:txtValue,
			style:{
				width: '100%'
			},
			inputWidth:'300',
			height:175
		};
		var ckeditorCfg={
			//resize_enabled:true
			workItemID:me.workItemID,
			projectID:me.projectID
		}
		me.txtArea=CWHF.createRichTextEditorField(name,otherCfg,false,false,ckeditorCfg);
		return me.txtArea;
	},
	beforeSubmit:function(){
		CWHF.submitRTEditor(this.view);
	},
	refreshView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			requiredMarkup='';
		}
		me.view.setFieldLabel(label);
		me.view.afterLabelTextTpl=requiredMarkup;
		CWHF.refreshRTEditorValue(me.view,me.value);
		me.view.allowBlank=!required;
		me.view.hidden=hidden;
	},
	initEditor:function(c){
		var me=this;
		me.replaceTextArea(me.idDescription+"-inputEl");
	},
	replaceTextArea:function(idDescription,sLang){
		var me=this;
		var toolbarFCKStartExpanded=true;
		if ( sLang == null ){
			sLang="en";
		}
		var o=CKEDITOR.instances[idDescription];
		if (o){
			CKEDITOR.remove(o);
		}
		me.ckEditor=CKEDITOR.replace(idDescription,{
			customConfig:sBasePath+'cktrackplusconfig.js',
			contentsCss:com.trackplus.TrackplusConfig.htmlEditorCSS,
			language:sLang/*,
			toolbarStartupExpanded:toolbarFCKStartExpanded*/
		});
		me.ckEditor.on('blur', function(e) {
			me.ckEditor.updateElement();
		});
		me.ckEditor.on('instanceReady', function(e) {
			me.fireEvent('afterRender',me.fieldID);
		});
	},
	getValue:function(){
		var me=this;
		var value=null;
		if(me.ckEditor!=null){
			value=me.ckEditor.getData();
			me.ckEditor.updateElement();
		}
		return value;
	}
});


Ext.define('com.aurel.trackplus.field.SelectTypeRenderer',{
	extend:'com.aurel.trackplus.field.TypeRenderer',
	createView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var name=me.fieldConfig.name;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			// label=label+me.requiredHtmlTxt;
			requiredMarkup='';
		}
		var data=me.fieldConfig.jsonData;
		var options=data.list;
		if(required==false&&options!=null&&options.length>0){
			options.unshift({id:null,label:''});
		}
		var forceSelection=false;
		if(me.fieldConfig.jsonData.forceSelection!=null){
			forceSelection=me.fieldConfig.jsonData.forceSelection;
		}
		var cmbCfg={
			forceSelection:forceSelection,
			fieldLabel: label,
			afterLabelTextTpl:requiredMarkup,
			labelAlign:me.labelHAlign,
			hideLabel :me.hideLabel,
			cls :'labelVerticalAlign-'+me.labelVAlign+
				' valueVerticalAlign-'+me.valueVAlign+' valueAlign-'+me.valueHAlign,
			labelStyle:{overflow:'hidden'},
			store:Ext.create('Ext.data.Store', {
				fields:[
					{type: 'int', name: 'id',useNull:true},
					{type: 'string', name: 'label'}
				],
				data:options
			}),

			valueField:'id',
			displayField: 'label',
			queryMode: 'local',
			allowBlank:!required,
			name: name,
			editable:true,
			typeAhead: true,
			anyMatch:true,
			grow:true
		}
		if(required==false){
			cmbCfg.tpl= new Ext.XTemplate('<tpl for=".">' + '<li style="min-height:22px;" class="x-boundlist-item" role="option">' + '{label}' + '</li></tpl>');
		}
		var cmb=Ext.create('Ext.form.ComboBox',cmbCfg);
		cmb.setValue(me.value);
		cmb.addListener('change',me.valueChange,me);
		return cmb;
	},
	refreshView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			//label=label+me.requiredHtmlTxt;
			requiredMarkup='';
		}
		var data=me.fieldConfig.jsonData;
		var options=data.list;
		if(required==false&&options!=null&&options.length>0){
			options.unshift({id:null,label:''});
		}
		me.view.setFieldLabel(label);
		me.view.afterLabelTextTpl=requiredMarkup;
		me.view.store.loadData(options);
		me.view.setValue(me.value);
		me.view.allowBlank=!required;
		me.view.hidden=hidden;
	},
	valueChange:function(fieldCmp,newValue){
		var me=this;
		var fieldValues={};
		fieldValues['fieldValues.f'+me.fieldID]=newValue;
		me.fireEvent('valueChange',me.fieldID,fieldValues);
	}
});

Ext.define('com.aurel.trackplus.field.CrmContactRenderer',{
	extend:'com.aurel.trackplus.field.TypeRenderer',
	createView:function(){
		var me=this;
		var fieldID=me.fieldConfig.fieldID;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var name=me.fieldConfig.name;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			requiredMarkup='';
		}
		var data=me.fieldConfig.jsonData;
		var cmb=Ext.create('Ext.form.ComboBox',{
			fieldLabel: label,
			afterLabelTextTpl:requiredMarkup,
			labelAlign:me.labelHAlign,
			hideLabel :me.hideLabel,
			cls :'labelVerticalAlign-'+me.labelVAlign+
				' valueVerticalAlign-'+me.valueVAlign+' valueAlign-'+me.valueHAlign,
			labelStyle:{overflow:'hidden'},
			valueField:'contactPartnerID',
			displayField: 'displayValue',
			allowBlank:!required,
			name: name,
			forceSelection:true,
			width: 500,
			hideTrigger: true,
			minChars: 2,
			triggerAction: 'query',
			typeAhead: true,
			store: {
				fields: [	{name: 'contactPartnerID', mapping: 'contactPartnerID'},
							{name: 'customerID', mapping: 'customerID'},
							{name: 'displayValue', mapping: 'displayValue'},
							{name: 'customerName', mapping: 'customerName'},
							{name: 'postalCode', mapping: 'postalCode'},
							{name: 'city', mapping: 'city'},
							{name: 'street', mapping: 'street'},
							{name: 'country', mapping: 'country'},
							{name: 'contactPartnerFirstName', mapping: 'contactPartnerFirstName'},
							{name: 'contactPartnerLastName', mapping: 'contactPartnerLastName'}],
				proxy: {
					type: 'ajax',
					url: 'remoteFiltering.action',
					extraParams: {fieldID:fieldID}
				},
				data:data.list
			},
			tpl:new Ext.XTemplate('<tpl for=".">' + '<li style="min-height:22px;" class="x-boundlist-item{[xindex % 2 === 0 ? "":" x-boundlist-item-odd"]}" role="option">' +
				'{customerID} {country} <br/> {customerName} <br/> {postalCode} {city}, {street} <br/> <b>{contactPartnerFirstName} {contactPartnerLastName}</b>' +
				'</li></tpl>')
			/*listConfig: {
				//loadingText: 'Searching...',
				//emptyText: 'No matching customer found.',
				getInnerTpl: function() {
					return '{customerID} {country} <br/> {customerName} <br/> {postalCode} {city}, {street} <br/> <b>{contactPartnerFirstName} {contactPartnerLastName}</b>';
				}
			}*/
		});
		cmb.setValue(me.value);
		//cmb.addListener('change',me.valueChange,me);
		return cmb;
	},
	refreshView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			requiredMarkup='';
		}
		var data=me.fieldConfig.jsonData;
		me.view.setFieldLabel(label);
		me.view.afterLabelTextTpl=requiredMarkup;
		me.view.store.loadData(data.list);
		me.view.setValue(me.value);
		me.view.allowBlank=!required;
		me.view.hidden=hidden;
	}
	/*valueChange:function(fieldCmp,newValue){
		var me=this;
		var data=me.fieldConfig.jsonData;
		var fieldValues=[];
		fieldValues['fieldValues.f'+me.fieldID]=newValue;
		me.fireEvent('valueChange',me.fieldID,fieldValues);
	}*/
});

Ext.define('com.aurel.trackplus.field.ExtensibleSelectTypeRenderer',{
	extend:'com.aurel.trackplus.field.SelectTypeRenderer',
	createView:function(){
		var me=this;
		var cmb=me.callParent(arguments);
		cmb.editable=true;
		cmb.getSubmitValue=function(){
			var v=this.getValue();
			var record=this.findRecordByValue(v);
			if(record==false){
				return v==null?"":"'"+v+"'";
			}else{
				return v;
			}
		};
		return cmb;
	}
});

Ext.define('com.aurel.trackplus.field.MultipleSelectPickerRenderer',{
	extend:'com.aurel.trackplus.field.TypeRenderer',
	config:{
		useIconCls:false,
		iconFieldID:null,
		pikerWidth:300
	},
	createView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var name=me.fieldConfig.name;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			requiredMarkup='';
		}
		var data=me.fieldConfig.jsonData;
		var options=data.list;
		/*if(required==false&&options!=null&&options.length>0){
			options.unshift({id:null,label:''});
		}*/
		var optionLength=0;
		if(options!=null){
			optionLength=options.length;
		}
		var picker =Ext.create('com.trackplus.util.MultipleSelectPicker',{
			data:options,
			includeSearch:optionLength>=7,
			labelStyle:{overflow:'hidden'},
			labelWidth:100,
			width:me.pikerWidth,
			name:name,
			value:me.value,
			fieldLabel:label,
			labelAlign:me.labelHAlign,
			cls :'labelVerticalAlign-'+me.labelVAlign+
				' valueVerticalAlign-'+me.valueVAlign+' valueAlign-'+me.valueHAlign,
			useNull:true,
			afterLabelTextTpl:requiredMarkup,
			useIconCls:me.useIconCls,
			iconUrlPrefix:me.iconFieldID==null?null:'optionIconStream.action?fieldID='+me.iconFieldID+"&optionID="
		});
		picker.addListener('change',me.valueChange,me);
		return picker;
	},
	valueChange:function(fieldCmp,newValue){
		var me=this;
		var name=me.fieldConfig.name;
		var fieldValues={};
		fieldValues['fieldValues.f'+me.fieldID]=newValue;
		me.fireEvent('valueChange',me.fieldID,fieldValues);
	},
	refreshView:function(){
		var me=this;
		var name=me.fieldConfig.name;
		var value=me.value;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			//label=label+me.requiredHtmlTxt;
			requiredMarkup='';
		}
		var data=me.fieldConfig.jsonData;
		me.view.setFieldLabel(label);
		me.view.afterLabelTextTpl=requiredMarkup;
		var options=data.list;
		if(required==false&&options!=null&&options.length>0){
			options.unshift({id:null,label:''});
		}
		me.view.updateData(options);
		me.view.setValue(value,true);
		me.view.allowBlank=!required;
		me.view.hidden=hidden;
	}
});

Ext.define('com.aurel.trackplus.field.MultipleSelectTypeRenderer',{
	extend:'com.aurel.trackplus.field.TypeRenderer',
	createView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var name=me.fieldConfig.name;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			requiredMarkup='';
		}
		var data=me.fieldConfig.jsonData;

		var cmb=Ext.create('Ext.ux.form.MultiSelect',{
			fieldLabel: label,
			afterLabelTextTpl:requiredMarkup,
			labelAlign:me.labelHAlign,
			hideLabel :me.hideLabel,
			cls :'labelVerticalAlign-'+me.labelVAlign+
				' valueVerticalAlign-'+me.valueVAlign+' valueAlign-'+me.valueHAlign,
			labelStyle:{overflow:'hidden'},
			store:Ext.create('Ext.data.Store', {
				fields:[
					{type: 'int', name: 'id'},
					{type: 'string', name: 'label'}
				],
				data:data.list
			}),
			valueField:'id',
			displayField: 'label',
			queryMode: 'local',
			allowBlank:!required,
			name: name,
			value:me.value,
			grow:true,
			//width:300,
			height:125
		});

		cmb.setValue(me.value);
		return cmb;
	},
	refreshView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			requiredMarkup='';
		}
		me.view.setFieldLabel(label);
		me.view.afterLabelTextTpl=requiredMarkup;
		me.view.setValue(me.value);
		me.view.allowBlank=!required;
		me.view.hidden=hidden;
	}
});


Ext.define('com.aurel.trackplus.field.ReleasePickerRenderer',{
	extend:'com.aurel.trackplus.field.TypeRenderer',
	createView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var name=me.fieldConfig.name;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		var includeClear=!required;
		if(required==false){
			requiredMarkup='';
		}
		var data=me.fieldConfig.jsonData;
		var releaseTree = data["releaseTree"];
		var picker = CWHF.createSingleTreePicker(label,	name, releaseTree, me.value,
			{
				includeClear:includeClear,
				labelIsLocalized: true,
				afterLabelTextTpl: requiredMarkup,
				labelAlign:me.labelHAlign,allowBlank:!required,
				cls :'labelVerticalAlign-'+me.labelVAlign+
					' valueVerticalAlign-'+me.valueVAlign+' valueAlign-'+me.valueHAlign,
				labelStyle:{overflow:'hidden'},
				labelWidth:100,
				width:310
			}
		);
		return picker;
	},
	refreshView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			requiredMarkup='';
		}
		me.view.setFieldLabel(label);
		me.view.afterLabelTextTpl=requiredMarkup;
		var value=null;
		try{
			 value=parseInt(me.value);
			 if(isNaN(value)){
				value=null;
			}
		}catch(e){}
		var data=me.fieldConfig.jsonData;
		var releaseLabel=data.releaseLabel;
		me.view.setValue(releaseLabel);
		me.view.releaseID=value;
		me.view.allowBlank=!required;
		me.view.hidden=hidden;
	}
});

Ext.define('com.aurel.trackplus.field.ProjectPickerRenderer',{
    extend:'com.aurel.trackplus.field.TypeRenderer',
    createView:function(){
        var me=this;
        var label=me.fieldConfig.label;
        var hidden=me.fieldConfig.invisible;
        var name=me.fieldConfig.name;
        var required=me.fieldConfig.required;
        var requiredMarkup=me.requiredMarkup;
        if(required==false){
            requiredMarkup='';
        }
        var data=me.fieldConfig.jsonData;
        var projectTree = data["projectTree"];
        var picker = CWHF.createMultipleTreePicker(label,
            name, projectTree, me.value,
            {   useRemoveBtn:false,
                useNull:true,
                //useTooltip:false,
                labelIsLocalized: true,
                afterLabelTextTpl: requiredMarkup,
                labelAlign:me.labelHAlign,
                allowBlank:!required,
                cls :'labelVerticalAlign-'+me.labelVAlign+
                    ' valueVerticalAlign-'+me.valueVAlign+' valueAlign-'+me.valueHAlign,
                labelStyle:{overflow:'hidden'},
                labelWidth:100,
                width:310
            });
        return picker;
    },
    refreshView:function(){
        var me=this;
        var label=me.fieldConfig.label;
        var hidden=me.fieldConfig.invisible;
        var required=me.fieldConfig.required;
        var requiredMarkup=me.requiredMarkup;
        if(required==false){
            requiredMarkup='';
        }
        me.view.setFieldLabel(label);
        me.view.afterLabelTextTpl=requiredMarkup;
        var data=me.fieldConfig.jsonData;
        me.view.allowBlank=!required;
        me.view.hidden=hidden;
    }
});

Ext.define('com.aurel.trackplus.field.ItemPickerRenderer',{
    extend:'com.aurel.trackplus.field.TypeRenderer',
    createView:function(){
        var me=this;
        var label=me.fieldConfig.label;
        var hidden=me.fieldConfig.invisible;
        var name=me.fieldConfig.name;
        var required=me.fieldConfig.required;
        var requiredMarkup=me.requiredMarkup;
        if(required==false){
            requiredMarkup='';
        }
        var data=me.fieldConfig.jsonData;
        var picker = CWHF.createMultipleTreePicker(label,
            name, data["itemTree"], me.value,
            {   useRemoveBtn:false,
                useNull:true,
                //useTooltip:false,
                labelIsLocalized: true,
                afterLabelTextTpl: requiredMarkup,
                labelAlign:me.labelHAlign,
                allowBlank:!required,
                cls :'labelVerticalAlign-'+me.labelVAlign+
                    ' valueVerticalAlign-'+me.valueVAlign+' valueAlign-'+me.valueHAlign,
                labelStyle:{overflow:'hidden'},
                labelWidth:100,
                width:310
            });
        return picker;
    },
    refreshView:function(){
        var me=this;
        var label=me.fieldConfig.label;
        var hidden=me.fieldConfig.invisible;
        var required=me.fieldConfig.required;
        var requiredMarkup=me.requiredMarkup;
        if(required==false){
            requiredMarkup='';
        }
        me.view.setFieldLabel(label);
        me.view.afterLabelTextTpl=requiredMarkup;
        var data=me.fieldConfig.jsonData;
        me.view.allowBlank=!required;
        me.view.hidden=hidden;
    }
});

Ext.define('com.aurel.trackplus.field.ParentTypeRenderer',{
	extend:'com.aurel.trackplus.field.TypeRenderer',
	config:{
		readOnly:false
	},
	idField:null,
	labelField:null,
	constructor : function(config) {
		var me=this;
		me.callParent(arguments);
		this.addEvents('clickOnParent');
	},
	createView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var name=me.fieldConfig.name;
		var data=me.fieldConfig.jsonData;
		var itemID=me.value;
		if(data.projectSpecificID!=null&&data.projectSpecificID!=''){
			itemID=data.projectSpecificID;
		}
		var displayValue="";
		if(itemID!=null){
			displayValue=itemID+': '+data.title;
		}
		me.idField= Ext.create('Ext.form.field.Hidden',{
			name: name,
			value:me.value,
			readOnly:true,
			margin:'0 0 0 0',
			cls:'insideFiledCointainer'
		});
		me.labelField=Ext.create('Ext.ux.LinkComponent',{
			handler:me.clickOnParent,
			scope:me,
			margin:'4 0 0 0',
			clsLink:'parentLink',
			style:{
				overflow:'hidden'
			},
			cls:'insideFiledCointainer',
			flex:1,
			label:displayValue
		});
		me.btnClear=Ext.create('Ext.button.Button',{
			margin:'0 5 0 0',
			iconCls:'delete16',
			cls:'insideFiledCointainer',
			handler:me.clearParent,
			hidden:(me.value==null||me.value==''),
			scope:me
		});
		var items=[];
		if(me.readOnly==false){
			items.push(me.idField);
			items.push(me.btnClear);
		}
		items.push(me.labelField);
		return Ext.create('Ext.form.FieldContainer',{
			combineErrors: true,
			fieldLabel: label,
			labelAlign:me.labelHAlign,
			hideLabel :me.hideLabel,
			cls :'labelVerticalAlign-'+me.labelVAlign+
				' valueVerticalAlign-'+me.valueVAlign+' valueAlign-'+me.valueHAlign,
			labelStyle:{overflow:'hidden'},
			layout: 'hbox',
			defaults: {
				hideLabel: true
			},
			width:450,
			items :items,
			hidden:hidden
		});

	},
	refreshView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var requiredMarkup=me.fieldConfig.required;
		var itemID=me.value;
		var data=me.fieldConfig.jsonData;
		var itemID=me.value;
		if(data.projectSpecificID!=null&&data.projectSpecificID!=''){
			itemID=data.projectSpecificID;
		}
		var displayValue="";
		if(itemID!=null){
			displayValue=itemID+': '+data.title;
		}
		me.idField.setValue(me.value);
		me.labelField.setLabel(displayValue);
		me.view.setFieldLabel(label);
		me.view.afterLabelTextTpl=requiredMarkup;
		me.btnClear.setVisible(me.value!=null&&me.value!="");
		me.view.hidden=hidden;
	},
	clearParent:function(){
		var me=this;
		me.value=null;
		me.idField.setValue(null);
		me.labelField.setLabel(null);
		me.btnClear.setVisible(false);
		me.view.doLayout();
		me.view.ownerCt.doLayout();
	},
	clickOnParent:function(){
		var me=this;
		var parentID=me.value;
		me.fireEvent("clickOnParent",parentID);
		//var url="printItem.action?workItemID="+me.value;
		//window.open(url,'parentItem');
	}
});
Ext.define('com.aurel.trackplus.field.ParentTypeRendererReadOnly',{
	extend:'com.aurel.trackplus.field.ParentTypeRenderer',
	readOnly:true
});


Ext.define('com.aurel.trackplus.field.IntegerTypeRenderer',{
	extend:'com.aurel.trackplus.field.TypeRenderer',
	config:{
		allowDecimals:false,
		decimalSeparator:'.'
	},
	createView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var name=me.fieldConfig.name;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			requiredMarkup='';
		}
		var data=me.fieldConfig.jsonData;
		var numberFieldConfig = {
				fieldLabel: label,
				afterLabelTextTpl:requiredMarkup,
				allowDecimals:me.allowDecimals,
				decimalSeparator:me.decimalSeparator,
				labelAlign:me.labelHAlign,
				hideLabel :me.hideLabel,
				cls :'labelVerticalAlign-'+me.labelVAlign+
					' valueVerticalAlign-'+me.valueVAlign+' valueAlign-'+me.valueHAlign,
				labelStyle:{overflow:'hidden'},
				name: name,
				allowBlank:!required,
				value:me.value,
				hidden:hidden
			};
		if (data!=null) {
			if (data.minValue!=null) {
				numberFieldConfig.minValue = data.minValue;
			}
		}
		var numberField = Ext.create('Ext.form.field.Number', numberFieldConfig);
		numberField.addListener('change', me.valueChange, me, me);
		return numberField;
	},
	refreshView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			requiredMarkup='';
		}
		me.view.setFieldLabel(label);
		me.view.afterLabelTextTpl=requiredMarkup;
		me.view.setValue(me.value);
		me.view.allowBlank=!required;
		me.view.hidden=hidden;
	},
	valueChange:function(fieldCmp, newValue, oldValue){
		var me=this;
		var fieldValues={};
		fieldValues["f"+me.fieldID]=newValue;
		var oldFieldValues = {};
		oldFieldValues["f"+me.fieldID]=oldValue;
		me.fireEvent("valueChange",me.fieldID,fieldValues,oldFieldValues, me);
	}
});

Ext.define('com.aurel.trackplus.field.DoubleTypeRenderer',{
	extend:'com.aurel.trackplus.field.IntegerTypeRenderer',
	allowDecimals:true,
	decimalSeparator:com.trackplus.TrackplusConfig.DecimalSeparator
});

Ext.define('com.aurel.trackplus.field.DurationRenderer',{
	extend:'com.aurel.trackplus.field.DoubleTypeRenderer',
	config: {
		startDateFieldID: 19,
		endDateFieldID: 20
	},
	constructor : function(config) {
		var me=this;
		this.initConfig(config);
		me.callParent(arguments);
	},
	refreshDependentFields:function(fieldID, fieldValues, oldFieldValues, renderer, modelFieldValues, isFromGantt) {
		var me=this;
		var startDateFieldID = renderer.startDateFieldID;
		var endDateFieldID = renderer.endDateFieldID;
		var newDuration = fieldValues["f"+fieldID];
		modelFieldValues["f"+fieldID] = newDuration;
		var oldDuration = oldFieldValues["f"+fieldID];
		if (newDuration !== oldDuration) {
			var startDate = modelFieldValues["f"+startDateFieldID];
			var endDate = modelFieldValues["f"+endDateFieldID];
			if(isFromGantt) {
				startDate = Ext.util.Format.date(startDate, com.trackplus.TrackplusConfig.DateFormat);
				endDate = Ext.util.Format.date(endDate, com.trackplus.TrackplusConfig.DateFormat);
			}

			if (newDuration!=null) {
				if (startDate!=null && startDate.length>0) {
					endDate = me.addWeekdays(me.parseDate(startDate, true), newDuration, true);
					me.updateField(endDateFieldID, endDate, endDate, modelFieldValues);
				} else {
					if (endDate!=null && endDate.length>0) {
						startDate = me.addWeekdays(me.parseDate(endDate, true), newDuration, false);
						me.updateField(startDateFieldID, startDate, startDate, modelFieldValues);
					} else {
						startDate = Ext.Date.format(new Date(), com.trackplus.TrackplusConfig.DateFormat);
						me.updateField(startDateFieldID, startDate, startDate, modelFieldValues);
						endDate = me.addWeekdays(me.parseDate(endDate, true), newDuration, true);
						me.updateField(endDateFieldID, endDate, endDate, modelFieldValues);
					}
				}
			}
		}
	}
});

Ext.define('com.aurel.trackplus.field.TargetDurationRenderer',{
	extend:'com.aurel.trackplus.field.DurationRenderer',
	config: {
		startDateFieldID: 29,
		endDateFieldID: 30
	},
	constructor : function(config) {
		var me=this;
		this.initConfig(config);
		me.callParent(arguments);
	}
});

Ext.define('com.aurel.trackplus.field.CheckBoxTypRenderer',{
	extend:'com.aurel.trackplus.field.TypeRenderer',
	config:{
		allowDecimals:false
	},
	createView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var name=me.fieldConfig.name;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			requiredMarkup='';
		}
		return Ext.create('Ext.form.field.Checkbox',{
			fieldLabel: label,
			afterLabelTextTpl:requiredMarkup,
			allowDecimals:me.allowDecimals,
			decimalSeparator:me.decimalSeparator,
			labelAlign:me.labelHAlign,
			hideLabel :me.hideLabel,
			cls :'labelVerticalAlign-'+me.labelVAlign+
				' valueVerticalAlign-'+me.valueVAlign+' valueAlign-'+me.valueHAlign,
			labelStyle:{overflow:'hidden'},
			name: name,
			margin: '3 0 0 0',
			allowBlank:!required,
			inputValue:'true',
			uncheckedValue:'false',
			value:me.value,
			checked:me.value==true,
			hidden:hidden
		});
	},
	refreshView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			requiredMarkup='';
		}
		me.view.setFieldLabel(label);
		me.view.afterLabelTextTpl=requiredMarkup;
		me.view.setValue(me.value);
		me.view.allowBlank=!required;
		me.view.hidden=hidden;
	}
});

Ext.define('com.aurel.trackplus.field.CompositeTypeRenderer',{
	extend:'com.aurel.trackplus.field.TypeRenderer',
	parts:null,
	createView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var name=me.fieldConfig.name;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			requiredMarkup='';
		}
		var data=me.fieldConfig.jsonData;
		var items=[];
		var cascading=data.cascading;
		me.parts=[];
		for(var i=0;i<data.parts.length;i++){
			var hasDependences=(cascading&&i<data.parts.length-1);
			var partRenderer=Ext.create(data.parts[i].extClassName,{
				fieldID:me.fieldID,
				parameterCode:i+1,
				fieldConfig:{
					required:required,
					hasDependences:hasDependences,
					name:me.fieldConfig.name+"_"+(i+1),
					jsonData:data.parts[i].jsonData
				},
				value:me.value==null?null:me.value[''+(i+1)]
			});
			if(hasDependences){
				partRenderer.addListener('valueChange',me.partValueChange,me);
			}
			me.parts.push(partRenderer);
			items.push(partRenderer.getView());
		}
		return Ext.create('Ext.form.FieldContainer',{
			combineErrors: true,
			fieldLabel: label,
			afterLabelTextTpl:requiredMarkup,
			labelAlign:me.labelHAlign,
			hideLabel :me.hideLabel,
			cls :'labelVerticalAlign-'+me.labelVAlign+
				' valueVerticalAlign-'+me.valueVAlign+' valueAlign-'+me.valueHAlign,
			labelStyle:{overflow:'hidden'},
			layout:{
				type:'column'
			},
			defaults: {
				hideLabel: true,
				margin:'0 0 0 5'
			},
			style:{
				width: '100%'
			},
			items :items,
			hidden:hidden
		});
	},
	partValueChange:function(fieldID){
		var me=this;
		//alert("partValueChange:fieldID="+fieldID+" paramaterCode="+parameterCode+", newValue="+newValue+" ,oldValue="+oldValue);
		var fieldValues={};
		for(var i=0;i<me.parts.length;i++){
			var part=me.parts[i];
			fieldValues['fieldValues.f'+me.fieldID+"_"+(i+1)]=part.getValue();
		}
		me.fireEvent('valueChange',me.fieldID,fieldValues);
	},
	refreshView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			requiredMarkup='';
		}
		me.view.setFieldLabel(label);
		me.view.afterLabelTextTpl=requiredMarkup;
		var data=me.fieldConfig.jsonData;
		for(var i=0;i<me.parts.length;i++){
			me.parts[i].value= me.value==null?null:me.value[''+(i+1)];
			me.parts[i].fieldConfig.jsonData=data.parts[i].jsonData;
			me.parts[i].refreshView();
		}
		me.view.allowBlank=!required;
		me.view.hidden=hidden;
	}
});

Ext.define('com.aurel.trackplus.field.SingleSelectPickerRenderer',{
	extend:'com.aurel.trackplus.field.TypeRenderer',
	config:{
		useIconCls:false,
		iconFieldID:null,
		pikerWidth:300
	},
	createView:function(){
		var me=this;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var name=me.fieldConfig.name;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			requiredMarkup='';
		}
		var data=me.fieldConfig.jsonData;
		var options=data.list;
		if(required==false&&options!=null&&options.length>0){
			options.unshift({id:null,label:''});
		}

		var releaseTree = data["releaseTree"];
		var optionLength=0;
		if(options!=null){
			optionLength=options.length;
		}
		var picker =Ext.create('com.trackplus.util.SingleSelectPicker',{
			data:options,
			includeSearch:optionLength>=7,
			cls :'labelVerticalAlign-'+me.labelVAlign+
				' valueVerticalAlign-'+me.valueVAlign+' valueAlign-'+me.valueHAlign,
			labelStyle:{overflow:'hidden'},
			labelWidth:100,
			width:me.pikerWidth,
			name:name,
			value:me.value,
			fieldLabel:label,
			labelAlign:me.labelHAlign,
			afterLabelTextTpl:requiredMarkup,
			useIconCls:me.useIconCls,
			iconUrlPrefix:me.iconFieldID==null?null:'optionIconStream.action?fieldID='+me.iconFieldID+"&optionID="
		});
		picker.addListener('change',me.valueChange,me);
		return picker;
	},
	valueChange:function(fieldCmp,newValue){
		var me=this;
		var name=me.fieldConfig.name;
		var fieldValues={};
		fieldValues['fieldValues.f'+me.fieldID]=newValue;
		me.fireEvent('valueChange',me.fieldID,fieldValues);
	},
	refreshView:function(){
		var me=this;
		var name=me.fieldConfig.name;
		var value=me.value;
		var label=me.fieldConfig.label;
		var hidden=me.fieldConfig.invisible;
		var required=me.fieldConfig.required;
		var requiredMarkup=me.requiredMarkup;
		if(required==false){
			//label=label+me.requiredHtmlTxt;
			requiredMarkup='';
		}
		var data=me.fieldConfig.jsonData;
		me.view.setFieldLabel(label);
		me.view.afterLabelTextTpl=requiredMarkup;
		var options=data.list;
		if(required==false&&options!=null&&options.length>0){
			options.unshift({id:null,label:''});
		}
		me.view.updateData(options);
		me.view.setValue(value,true);
		me.view.allowBlank=!required;
		me.view.hidden=hidden;
	}
});

Ext.define('com.aurel.trackplus.field.StateRenderer',{
	extend:'com.aurel.trackplus.field.SingleSelectPickerRenderer',
	iconFieldID:-4,
	pikerWidth:265
});

Ext.define('com.aurel.trackplus.field.PriorityRenderer',{
	extend:'com.aurel.trackplus.field.SingleSelectPickerRenderer',
	iconFieldID:-10,
	pikerWidth:265
});

Ext.define('com.aurel.trackplus.field.SeverityRenderer',{
	extend:'com.aurel.trackplus.field.SingleSelectPickerRenderer',
	iconFieldID:-11,
	pikerWidth:265
});

Ext.define('com.aurel.trackplus.field.PersonPickerRenderer',{
	extend:'com.aurel.trackplus.field.SingleSelectPickerRenderer',
	useIconCls:true,
	pikerWidth:310
});
