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

Ext.define('com.trackplus.screen.ItemEditPanelView',{
	extend:'com.trackplus.screen.PanelView',
	getPanelConfig:function(model,index,length){
		var me=this;
		var cls="screenDesignPanel";
		var margin='0 0 5 0';
		me.myComponentCls=cls;
		return {
			cls:cls,
			margin:margin,
			border:true,
			layout: {
				type: 'table',
				columns: model.colsNo,
				tableAttrs: {
					style: {
						minWidth: '100%'
					}
				},
				trAttrs:{
					'class':'screenField-tr',
					style:{
						'vertical-align':'top'
					}
				}
			},
			defaults: {frame:false, border: false}
		};
	}
});
Ext.define('com.trackplus.layout.ItemScreenEditLayout',{
	extend:'com.trackplus.layout.BaseLayout',
	useToolbar:true,
	screenModel:null,
	screenEditFacade:null,
	constructor : function(config) {
		var me = this;
		me.callParent(arguments);
		var data=me.initData;
		var backAction;
		if(data.backAction){
			backAction=data.backAction;
		}else{
			backAction='admin.action?selectedNodeID=customForms&sectionSelected=customizationSection';
		}
		me.screenModel=com.trackplus.screen.createScreenModel(me.initData.screen);
		me.screenEditFacade= Ext.create('com.trackplus.screen.ScreenEditFacade',{
			screenModel:me.screenModel,
			refreshTabUrl:'tab!reload.action',
			controllerCls:'com.trackplus.screen.ScreenEditController',
			urlFieldList:'screenTree!list.action',
			fieldsAsTree:true,
			titleFieldList:getText("admin.customize.form.edit.lbl.fields"),
			useConfig:false,
			panelViewCls:'com.trackplus.screen.ItemEditPanelView',

			screenAction:'screenEdit',
			screenUpdateAction:'updateScreenProperty',
			tabAction:'tab',
			tabUpdateAction:'updateTabProperty',
			panelAction:'panel',
			panelUpdateAction:'updatePanelProperty',
			fieldAction:'field',
			fieldUpdateAction:'updateFieldProperty',

			messageDeletePanel:getText('admin.customize.form.edit.question.deletePanel'),
			messageDeleteTab:getText('admin.customize.form.edit.question.deleteTab'),
			messageDeleteField:getText('admin.customize.form.edit.question.deleteField'),
			messageCantDeleteScreen:getText('admin.customize.form.edit.message.cantDeleteScreen'),
			messageCantDeleteLastTab:getText('admin.customize.form.edit.message.cantDeleteLastTab'),
			backAction:backAction
		});
		me.onReady(function(){
			var toolbar=me.screenEditFacade.getToolbar.call(me.screenEditFacade);
			me.borderLayoutController.setActiveToolbarActionList(toolbar);
		});
	},
	createCenterPanel:function(){
		var me=this;
		return me.screenEditFacade.createScreenEditViewComponent.call(me.screenEditFacade);
	}
});


/*field types*/
Ext.define('com.aurel.trackplus.field.design.TypeRenderer',{
	ALIGN_TOP: 0,
	ALIGN_BOTTOM:1,
	ALIGN_MIDDLE: 2,
	ALIGN_LEFT:3,
	ALIGN_RIGHT:4,
	ALIGN_CENTER:5,
	ALIGN_TOP_STR: 'top',
	ALIGN_BOTTOM_STR:'bottom',
	ALIGN_MIDDLE_STR: 'middle',
	ALIGN_LEFT_STR:'left',
	ALIGN_RIGHT_STR:'right',
	ALIGN_CENTER_STR:'center',
	render:function(model){
		return Ext.create('Ext.form.Label',{});
	},
	getAlignStr:function(align){
		var me=this;
		var align=null;
		switch (align) {
			case me.ALIGN_TOP:
				align=me.ALIGN_TOP_STR;
				break;
			case me.ALIGN_BOTTOM:
				align=me.ALIGN_BOTTOM_STR;
				break;
			case me.ALIGN_MIDDLE:
				align=me.ALIGN_MIDDLE_STR;
				break;
			case me.ALIGN_LEFT:
				align=me.ALIGN_LEFT_STR;
				break;
			case me.ALIGN_RIGHT:
				align=me.ALIGN_RIGHT_STR;
				break;
			case me.ALIGN_CENTER:
				align=me.ALIGN_CENTER_STR;
				break;
		}
		return align;
	}
});
Ext.define('com.aurel.trackplus.field.design.LabelTypeRenderer',{
	extend:'com.aurel.trackplus.field.design.TypeRenderer',
	render:function(model){
		var label=model.label;
		var value='abc';
		if(model.hideLabel===true){
			value="abc("+model.label+")";
		}
		return  Ext.create('Ext.form.field.Display',{
			fieldLabel: label,
			hideLabel :model.hideLabel,
			labelAlign:model.labelHAlign,
			value:value,
			cls :'labelVerticalAlign-'+model.labelVAlign+
				' valueVerticalAlign-'+model.valueVAlign+' valueAlign-'+model.valueHAlign
		});
	}
});
Ext.define('com.aurel.trackplus.field.design.TextFieldTypeRenderer',{
	extend:'com.aurel.trackplus.field.design.TypeRenderer',
	render:function(model){
		var label=model.label;
		return Ext.create('Ext.form.field.Text',{
			fieldLabel: label,
			hideLabel :model.hideLabel,
			labelAlign:model.labelHAlign,
			cls :'labelVerticalAlign-'+model.labelVAlign+
				' valueVerticalAlign-'+model.valueVAlign+' valueAlign-'+model.valueHAlign,
			editable:false
		});
	}
});
Ext.define('com.aurel.trackplus.field.design.TextAreaTypeRenderer',{
	extend:'com.aurel.trackplus.field.design.TypeRenderer',
	render:function(model){
		var label=model.label;
		return Ext.create('Ext.form.field.Text',{
			fieldLabel: label,
			hideLabel :model.hideLabel,
			labelAlign:model.labelHAlign,
			cls :'labelVerticalAlign-'+model.labelVAlign+
				' valueVerticalAlign-'+model.valueVAlign+' valueAlign-'+model.valueHAlign,
			editable:false
		});
	}
});

Ext.define('com.aurel.trackplus.field.design.SynopsisTypeRenderer',{
	extend:'com.aurel.trackplus.field.design.TypeRenderer',
	render:function(model){
		var label=model.label;
		return Ext.create('Ext.form.field.Text',{
			fieldLabel: label,
			hideLabel :model.hideLabel,
			labelAlign:model.labelHAlign,
			cls :'labelVerticalAlign-'+model.labelVAlign+
				' valueVerticalAlign-'+model.valueVAlign+' valueAlign-'+model.valueHAlign,
			editable:false,
			width:500
		});
	}
});
Ext.define('com.aurel.trackplus.field.design.DateTypeRenderer',{
	extend:'com.aurel.trackplus.field.design.TypeRenderer',
	render:function(model){
		var label=model.label;
		return Ext.create('Ext.form.field.Picker',{
			fieldLabel: label,
			hideLabel :model.hideLabel,
			labelAlign:model.labelHAlign,
			cls :'labelVerticalAlign-'+model.labelVAlign+
				' valueVerticalAlign-'+model.valueVAlign+' valueAlign-'+model.valueHAlign,
			triggerCls : Ext.baseCSSPrefix + 'form-date-trigger',
			editable:false,
			initTrigger:function(){
			}
		});
	}
});
Ext.define('com.aurel.trackplus.field.design.CheckboxTypeRenderer',{
	extend:'com.aurel.trackplus.field.design.TypeRenderer',
	render:function(model){
		var label=model.label;
		return Ext.create('Ext.form.field.Checkbox',{
			fieldLabel: label,
			hideLabel :model.hideLabel,
			labelAlign:model.labelHAlign,
			cls :'labelVerticalAlign-'+model.labelVAlign+
				' valueVerticalAlign-'+model.valueVAlign+' valueAlign-'+model.valueHAlign,
			readOnly:true
		});
	}
});

Ext.define('com.aurel.trackplus.field.design.NumberTypeRenderer',{
	extend:'com.aurel.trackplus.field.design.TypeRenderer',
	render:function(model){
		var label=model.label;
		return Ext.create('Ext.form.field.Number',{
			fieldLabel: label,
			hideLabel :model.hideLabel,
			labelAlign:model.labelHAlign,
			cls :'labelVerticalAlign-'+model.labelVAlign+
				' valueVerticalAlign-'+model.valueVAlign+' valueAlign-'+model.valueHAlign,
			editable:false,
			initTrigger:function(){
			}
		});
	}
});
//

Ext.define('com.aurel.trackplus.field.design.HtmlTypeRenderer',{
	extend:'com.aurel.trackplus.field.design.TypeRenderer',
	render:function(model){
		var label=model.label;
		return Ext.create('Ext.form.field.HtmlEditor',{
			fieldLabel: label,
			hideLabel :model.hideLabel,
			labelAlign:model.labelHAlign,
			cls :'labelVerticalAlign-'+model.labelVAlign+
				' valueVerticalAlign-'+model.valueVAlign+' valueAlign-'+model.valueHAlign,
			editable:false,
			readOnly:true,
			height:100,
			style:{
				width: '100%',
				marginBottom:'5px'
			}
		});
	}
});

Ext.define('com.aurel.trackplus.field.design.SelectTypeRenderer',{
	extend:'com.aurel.trackplus.field.design.TypeRenderer',
	render:function(model){
		var label=model.label;
		var cmb=Ext.create('Ext.form.field.Picker',{
			fieldLabel: label,
			hideLabel :model.hideLabel,
			editable:false,
			labelAlign:model.labelHAlign,
			cls :'labelVerticalAlign-'+model.labelVAlign+
				' valueVerticalAlign-'+model.valueVAlign+' valueAlign-'+model.valueHAlign,
			triggerCls: Ext.baseCSSPrefix + 'form-arrow-trigger',
			initTrigger:function(){
			}
		});
		return cmb;
	}
});

Ext.define('com.aurel.trackplus.field.design.CompositeRenderer',{
	extend:'com.aurel.trackplus.field.design.TypeRenderer',
	config:{
		parts:[]
	},
	render:function(model){
		var me=this;
		var items=new Array();
		var modelPart=new Object();
		for(var x in model){
			modelPart[x]=model[x];
		}
		modelPart['hideLabel']=true;

		for(var i=0;i<me.parts.length;i++){
			var partRenderer=Ext.create(me.parts[i].extClassName,{});
			items.push(partRenderer.render(modelPart));
		}
		return Ext.create('Ext.form.FieldContainer',{
			combineErrors: true,
			fieldLabel: model.label,
			labelAlign:model.labelHAlign,
			hideLabel :model.hideLabel,
			cls :'labelVerticalAlign-'+model.labelVAlign+
				' valueVerticalAlign-'+model.valueVAlign+' valueAlign-'+model.valueHAlign,
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
			items :items
		});
	}
});

Ext.define('com.aurel.trackplus.field.design.SelectParentChildRenderer',{
	extend:'com.aurel.trackplus.field.design.CompositeRenderer',
	parts:[
		{extClassName:'com.aurel.trackplus.field.design.SelectTypeRenderer'},
		{extClassName:'com.aurel.trackplus.field.design.SelectTypeRenderer'}
	]
});
Ext.define('com.aurel.trackplus.field.design.SelectParentChildrenRenderer',{
	extend:'com.aurel.trackplus.field.design.TypeRenderer',
	parts:[
		{extClassName:'com.aurel.trackplus.field.design.SelectTypeRenderer'},
		{extClassName:'com.aurel.trackplus.field.design.SelectTypeRenderer'},
		{extClassName:'com.aurel.trackplus.field.design.SelectTypeRenderer'}
	]
});

Ext.define('com.aurel.trackplus.field.design.SelectParentChildGrandchildRenderer',{
	extend:'com.aurel.trackplus.field.design.CompositeRenderer',
	parts:[
		{extClassName:'com.aurel.trackplus.field.design.SelectTypeRenderer'},
		{extClassName:'com.aurel.trackplus.field.design.SelectTypeRenderer'},
		{extClassName:'com.aurel.trackplus.field.design.SelectTypeRenderer'}
	]
});
