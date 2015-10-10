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

Ext.define('com.trackplus.screen.ItemTabView',{
	extend: 'com.trackplus.screen.TabView',
	firstTimeOverflow:true,
	layout: 'auto',
	border:false,
	style:{
		overflowX:'auto'
	},
	initComponent: function(){
		var me=this;
		me.callParent();
		if(me.oneTab==true){
			me.margin= '0 0 17 0';
			me.region='center';
		}else{
			me.margin='5 0 0 0';
			me.title=me.model.getLabel();
		}
	}
});

Ext.define('com.trackplus.screen.ItemScreenController', {
	extend : 'com.trackplus.screen.BaseScreenController',
	config : {
		children : null,
		readOnlyMode : false,
		dataModel : null
	},
	tabViewCls:'com.trackplus.screen.ItemTabView',
	showOneTab : true,
	fieldTypeRenderersMap : new Object(),
	fieldTypeWrappersMap : new Object(),
	fieldDataMap : new Object(),
	constructor : function(config) {
		this.callParent(arguments);
		this.addEvents('editMode','lastModified','clickOnParent');
	},
	refreshScreenModel : function(screenView, screenModel, selectedTab) {
		var me = this;
		me.fieldTypeRenderersMap = new Object();
		me.fieldTypeWrappersMap = new Object();
		me.fieldDataMap = new Object();
		me.callParent(arguments);
	},
	createField : function(parentView, aFieldData) {
		var me = this;
		var aField;
		var editable=false;
		var fieldConfig;
		if (aFieldData.empty) {
			aField = Ext.create('Ext.form.Label', {});
		} else {
			var fieldID = aFieldData.fieldID;
			me.fieldDataMap['f' + fieldID]=aFieldData;
			fieldConfig = me.dataModel.fieldConfigs['f' + fieldID];
			fieldConfig['name'] = "fieldValues.f" + fieldID;
			var extClassName = aFieldData.extClassName;
			if (me.readOnlyMode||fieldConfig['readonly']==true) {
				extClassName = aFieldData.extReadOnlyClassName;
				editable=me.dataModel.inlineEdit&&!fieldConfig.readonly;
			}
			var fieldValue = me.dataModel.fieldValues['f' + fieldID];
			var displayValue = me.dataModel.fieldDisplayValues['f' + fieldID];
			var fieldTypeRenderer = Ext.create(extClassName, {
				workItemID:me.dataModel.workItemID,
				projectID:me.dataModel.projectID,
				fieldID:fieldID,
				fieldConfig : fieldConfig,
				value : fieldValue,
				displayValue : displayValue,
				labelHAlign : aFieldData.labelHAlign,
				labelVAlign : aFieldData.labelVAlign,
				valueHAlign : aFieldData.valueHAlign,
				valueVAlign : aFieldData.valueVAlign,
				hideLabel   : aFieldData.hideLabel
			});
			if (me.fieldTypeRenderersMap['f' + fieldID] != null||fieldID==17) {
				aField = Ext.create('Ext.form.Label', {
					text : 'Field "'+fieldConfig.label+'" already on screen!'
				});
			} else {
				if (fieldConfig.hasDependences){
					fieldTypeRenderer.addListener('valueChange',me.fieldValueChange,me);
				}
				if (editable){
					fieldTypeRenderer.addListener('dblclick',me.fieldDblclick,me);
				}
				fieldTypeRenderer.addListener('afterRender',me.fieldAfterRender,me,{parentView:parentView});
				aField = fieldTypeRenderer.getView.call(fieldTypeRenderer);
				me.fieldTypeRenderersMap['f' + fieldID] = fieldTypeRenderer;
			}
			aField.colspan = aFieldData.colSpan;
			aField.rowspan = aFieldData.rowSpan;
			aField.addCls("screenField");
			if(fieldID==16){//parent
				fieldTypeRenderer.addListener('clickOnParent',me.clickOnParentHandler,me);
			}

		}
		if(editable&&!aFieldData.empty){
			var fieldID=aFieldData.fieldID;
			var fieldName="fieldValues.f" + fieldID;
			var fieldValue=me.dataModel.fieldValues['f' + fieldID];
			var extClassNameEditable=aFieldData.extClassName;
			var wrapperItems=[];
			wrapperItems.push(aField);
			if(extClassNameEditable=='com.aurel.trackplus.field.CompositeTypeRenderer'){
				if(fieldValue!=null){
					for(var i=0;i<fieldConfig.jsonData.parts.length;i++){
						var hiddenField=CWHF.createHiddenField(fieldName+"_"+(i+1),{value:fieldValue[''+(i+1)]});
						wrapperItems.push(hiddenField);
					}
				}
			}else{
				var hiddenField=CWHF.createHiddenField(fieldName,{value:fieldValue});
				wrapperItems.push(hiddenField);
			}
			var wrapper=Ext.create('Ext.container.Container',{
				border:false,
				cls:'editableFieldWrapper',
				overCls:'editableFieldWrapper-over',
				bodyBorder:false,
				margin:'0 0 0 0',
				bodyPadding:0,
				colspan:aFieldData.colSpan,
				rowspan:aFieldData.rowSpan,
				items:wrapperItems,
				listeners: {
					afterrender:{
						fn:function(cmp){
							var html="";
							if(fieldConfig.tooltip!=null&&fieldConfig.tooltip!=''){
								html=fieldConfig.tooltip+"<br/>"+getText('item.view.dblClickToEditField');
							}else{
								html=getText('item.view.dblClickToEditField');
							}
							Ext.create('Ext.tip.ToolTip', {
								target: cmp.el,
								trackMouse: true,
								dismissDelay: 0,
								html: html
							});
						}
					}
				}
			});
			me.fieldTypeWrappersMap['f'+fieldID]=wrapper;


			return wrapper;
		}else{
			if(me.dataModel.inlineEdit&&!aFieldData.empty){
				aField.addCls('readOnlyField');
			}
			if(fieldConfig!=null&&fieldConfig.tooltip!=null&&fieldConfig.tooltip!=''){
				aField.addListener('afterrender',function(cmp){
					Ext.create('Ext.tip.ToolTip', {
						target: cmp.el,
						trackMouse: true,
						dismissDelay: 0,
						html: fieldConfig.tooltip
					});
				});
			}
			return aField;
		}
	},
	editMode:false,
	fieldDblclick:function(fieldID){
		var me=this;
		var wrapper=me.fieldTypeWrappersMap['f'+fieldID];
		var aFieldData=me.fieldDataMap['f' + fieldID];
		var extClassName=aFieldData.extClassName;
		var fieldConfig = me.dataModel.fieldConfigs['f' + fieldID];
		fieldConfig['name'] = "fieldValues.f" + fieldID;
		var fieldValue = me.dataModel.fieldValues['f' + fieldID];
		var displayValue = me.dataModel.fieldDisplayValues['f' + fieldID];
		var fieldTypeRenderer = Ext.create(extClassName, {
			fieldID:fieldID,
			fieldConfig : fieldConfig,
			value : fieldValue,
			displayValue : displayValue,
			labelHAlign : aFieldData.labelHAlign,
			labelVAlign : aFieldData.labelVAlign,
			valueHAlign : aFieldData.valueHAlign,
			valueVAlign : aFieldData.valueVAlign,
			hideLabel : aFieldData.hideLabel
		});
		if(fieldConfig.hasDependences){
			fieldTypeRenderer.addListener('valueChange',me.fieldValueChange,me);
		}
		var aField = fieldTypeRenderer.getView.call(fieldTypeRenderer);
		aField.addCls('screenField');
		me.fieldTypeRenderersMap['f' + fieldID] = fieldTypeRenderer;
		var el=wrapper.getEl();
		el.fadeOut({
			opacity: 0.1, //can be any value between 0 and 1 (e.g. .5)
			easing: 'easeOut',
			duration: 150,
			remove: false,
			useDisplay: false,
			callback:function(){
				wrapper.removeAll(true);
				wrapper.add(aField);
				wrapper.removeCls("editableFieldWrapper");
				wrapper.ownerCt.doLayout();
				el.fadeIn({
					opacity: 1, //can be any value between 0 and 1 (e.g. .5)
					easing: 'easeOut',
					duration: 500,
					callback:function(){
						fieldTypeRenderer.focus();
					}
				});
				if(me.editMode==false){
					me.fireEvent('editMode');
					me.editMode=true;
				}
			}
		});


	},
	fieldAfterRender:function(fieldID,opts){
		var me=this;
		var parentView=opts.parentView;
		parentView.ownerCt.doLayout();
	},
	clickOnParentHandler:function(parentID){
		var me=this;
		me.fireEvent('clickOnParent',parentID);
	},
	fieldValueChange:function(fieldID, fieldValues, oldFieldValues, fieldTypeRenderer){
		var me=this;
		var modelFieldValues = me.dataModel.fieldValues;
		var fieldConfigs = me.dataModel.fieldConfigs;
		var fieldConfig = fieldConfigs["f"+fieldID];
		if (fieldConfig!=null && fieldConfig.clientSideRefresh) {
			//client side refresh
			if (fieldTypeRenderer.refreshDependentFields!=null) {
				fieldTypeRenderer.refreshDependentFields.call(me, fieldID, fieldValues, oldFieldValues, fieldTypeRenderer, modelFieldValues, false);
			}
		} else {
			//server side refresh
			var params={
					workItemID:me.dataModel.workItemID,
					projectID:me.dataModel.projectID,
					issueTypeID:me.dataModel.issueTypeID,
					actionID:me.dataModel.actionID,
					fieldID:fieldID
				};
			for(var x in fieldValues){
				params[x]=fieldValues[x];
			}
			me.screenView.setLoading(true);
			var urlStr="itemFieldRefresh.action";
			Ext.Ajax.request({
				url: urlStr,
				disableCaching:true,
				encoding: "utf-8",
				sync: true,
				success: function(result){
					var jsonData=Ext.decode(result.responseText);
					if(jsonData.success===true) {
						var data=jsonData.data;
						me.refreshDependentFields.call(me,data);
						me.screenView.setLoading(false);
					}else{
						me.screenView.setLoading(false);
					}

				},
				failure: function(type, error){
					me.screenView.setLoading(false);
					//alert("failure");
				},
				params :params,
				method:"POST"
			});
		}
	},

	updateField: function(fieldID, fieldValue, fieldDisplayValue, fieldValues) {
		var me=this;
		var data = {};
		var fieldName = "f"+fieldID;
		var fieldConfig = me.dataModel.fieldConfigs[fieldName];
		if (fieldConfig!=null) {
			//field to update is present on form
			data.fieldConfigs = {};
			data.fieldConfigs[fieldName] = fieldConfig;
			data.fieldValues = {};
			data.fieldValues[fieldName] = fieldValue;
			//actualize the model (for later use)
			fieldValues[fieldName] = fieldValue;
			data.fieldDisplayValues = {};
			data.fieldDisplayValues[fieldName] = fieldDisplayValue;
			//actualize the controls
			me.refreshDependentFields.call(me,data);
		}
	},

	addWeekdays: function(date, weekdays, add) {
		var me=this;
		var i = 0;
	    var oneDay = 1;
	    if (!add) {
	    	oneDay = -1;
	    }
	    while (i < weekdays) {
	    	date.setDate(date.getDate() + oneDay);
	        var day = date.getDay();
	        if (day > 0 && day < 6) {
	            i++;
	        }
	    }
	    return Ext.Date.format(date, com.trackplus.TrackplusConfig.DateFormat);
	},

	parseDate: function(dateStr, nowIfNull) {
		if (dateStr==null || dateStr.length==0) {
			if (nowIfNull) {
				return new Date();
			} else {
				return null;
			}
		} else {
			return Ext.Date.parse(dateStr, com.trackplus.TrackplusConfig.DateFormat);
		}
	},

	/**
	 * Returns number of free days from given interval
	 * @param startDateStr
	 * @param endDateStr
	 * @return
	 */
	getDurationBetweenDates: function(startDateStr, endDate) {
		var me=this;
		var startDate = me.parseDate(startDateStr, false);
		//var endDate = me.parseDate(endDateStr, false);
		if (startDate==null || endDate==null) {
			return null;
		}
		var i=0;
		while (startDate<endDate) {
			startDate.setDate(startDate.getDate() + 1);
			var day = startDate.getDay();
	        if (day > 0 && day < 6) {
	            i++;
	        } else {
	        	//end date explicitly set on Saturday or Sunday: take this week end day(s) as working day
	        	if (startDate>=endDate) {
	        		if (day==6) {
	        			//add one day for task ending on Saturday
	        			i++;
	        		} else {
	        			//add two days for task ending on Sunday
	        			i = i+2;
	        		}
	        	}
	        }
		}
		return i;
	},
	
	getDateFromModel: function(model, fieldID, isFromGantt) {
		var me = this;
		var date = null;
		date = model[fieldID];
		if(isFromGantt) {
			date = Ext.util.Format.date(date, com.trackplus.TrackplusConfig.DateFormat);
		}
		return date;
	},

	getPresentFields:function(){
		var me=this;
		var fields=new Array();
		for (var f in me.fieldTypeRenderersMap){
			var fieldID=parseInt(f.substring(1));
			fields.push(fieldID);
		}
		return fields;
	},
	refreshByFields:function(fieldIds,lastModified){
		var me=this;
		var fieldsToReload=new Array();
		var presentFields=me.getPresentFields();
		for(var i=0;i<fieldIds.length;i++){
			if(Ext.Array.indexOf(presentFields,fieldIds[i])>0){
				fieldsToReload.push(fieldIds[i]);

			}
		}

		if(lastModified!=null){
			me.fireEvent('lastModified',lastModified);
		}

		if(fieldsToReload.length==0){
			return false;
		}
		me.screenView.setLoading(true);
		var params={
			workItemID:me.dataModel.workItemID,
			projectID:me.dataModel.projectID,
			issueTypeID:me.dataModel.issueTypeID,
			actionID:me.dataModel.actionID,
			fields:fieldsToReload.join(",")
		};
		var urlStr="itemFieldRefresh!refreshFields.action";
		Ext.Ajax.request({
			url: urlStr,
			disableCaching:true,
			encoding: "utf-8",
			sync: true,
			success: function(result){
				var jsonData=Ext.decode(result.responseText);
				if(jsonData.success===true) {
					var data=jsonData.data;
					me.refreshDependentFields.call(me,data);
					me.screenView.setLoading(false);
					me.fireEvent('lastModified',data.lastModified);
				}else{
					me.screenView.setLoading(false);
					alert("failure");
				}

			},
			failure: function(type, error){
				me.screenView.setLoading(false);
				alert("failure");
			},
			params :params,
			method:"POST"
		});
	},

	refreshDependentFields:function(data){
		var me=this;
		var fieldConfigs=data.fieldConfigs;
		var fieldValues=data.fieldValues;
		var fieldDisplayValues=data.fieldDisplayValues;
		for(var f in fieldConfigs){
			var fieldCfg=fieldConfigs[f];
			me.dataModel.fieldValues[f]=fieldValues[f];
			me.dataModel.fieldDisplayValues[f]=fieldDisplayValues[f];
			fieldCfg['name'] = "fieldValues.f" + fieldConfigs[f].fieldID;
			var fieldTypeRenderer=me.fieldTypeRenderersMap[f];
			if(fieldTypeRenderer!=null) {
				//suspend events becasue setting the value would trigger triggering the change event (possibly infinit cycle)
				fieldTypeRenderer.suspendEvents(false);
				fieldTypeRenderer.update.call(fieldTypeRenderer,{
					fieldConfig:fieldCfg,
					value:fieldValues[f],
					displayValue:fieldDisplayValues[f]
				});
				//resume the event triggering
				fieldTypeRenderer.resumeEvents();
			}
			var wrapper=me.fieldTypeWrappersMap[f];
			if(wrapper!=null){
				var hiddenField=null;
				if(wrapper.items.getCount()>1){
					hiddenField=wrapper.items.getAt(1);
				}
				if(hiddenField!=null){
					hiddenField.setValue(fieldValues[f])
				}
			}
		}
	}
});
