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

Ext.define('com.trackplus.itemNavigator.CellInlineEditController',{
	extend:'Ext.Base',
	 mixins: {
		 field: 'Ext.form.field.Field'
	 },
	config: {
		navigator: null,
		grid: null,
		gantt: null,
	    //if Enable inline edit is enabled (My Settings => Other
	    //settings) then the cells are editable inline, otherwise NO.
		isPrintItemEditable: null
	},

	isNew: false,
	editOrCreateInProgress: false,
	row: null, //edited row or in case of creating new item the above row
	actualEditedOrCreatedRow: null,
	actualEditedOrCreatedRecord: null,
	warningDisplayed: false,
	timeStampWhenDataArrived: null,
	warnedWorkItemsForParallelEditing: {},

	SYSTEM_FIELDS: {
		SYNOPSIS: 17,
		RESPONSIBLE: 6,
		STATE_FIELD_ID: 4,

		STARTDATE: 19,
		ENDDATE: 20,
		DURATION: 33,

		TOP_DOWN_STARTDATE: 29,
		TOP_DOWN_ENDDATE: 30,
		TOP_DOWN_DURATION: 34
	},

	UNIFORMIZED_FIELD_TYPES: {
		singleSelect: 'SINGLE_SELECT',
		datePicker: 'DATE_PICKER',
		simpleTextField: 'SIMPLE_TEXT_FIELD',
		checkBox: "CHECK_BOX",
		numberEditor: "NUMBER_EDITOR"
	},

	constructor : function(cfg) {
		var me = this;
		var config = cfg || {};
		me.initConfig(config);
		me.timeStampWhenDataArrived = new Date();
		me.warnedWorkItemsForParallelEditing = {};
	},

	initListeners: function() {
		var me = this;
		if(me.getIsPrintItemEditable()) {
			me.getGrid().addListener('edit', me.onGridEdit, me);
			me.getGrid().addListener('beforeedit', me.onGridBeforeEdit, me);
			me.getGrid().addListener('render', me.onReadyHandler, me);
			me.getGrid().addListener('validateedit', me.validateedit, me);
		}
	},

	validateedit: function(editor, e) {
		var me = this;
		if(CWHF.isNull(e.value)) {
			e.cancel = true;
		}
		if(me.getUniformizedFieldType(e.column.extJsRendererClass) === me.UNIFORMIZED_FIELD_TYPES.checkBox) {
			var convertedOriginalValue = me.convertCheckboxValueToBoolean(e.originalValue);
			var convertedValue = me.convertCheckboxValueToBoolean(e.value);
			if(convertedOriginalValue === convertedValue) {
				e.cancel = true;
			}
		}
	},

	convertCheckboxValueToBoolean: function(val) {
		if(typeof(val) === "boolean"){
			return val;
		}
		if(val === "true") {
			return true;
		}
		if(val === "false") {
			return false;
		}
		if(val === getText('common.boolean.N')) {
			return false;
		}
		if(val === getText('common.boolean.Y')) {
			return true;
		}
		return null;
	},

	initGanttSpecificListener: function() {
		var me = this;
		if(me.getIsPrintItemEditable()) {
			me.getGantt().addListener('selectionchange', me.onGridSelectionChnage, me);
		}
	},

	onReadyHandler: function() {
		var me = this;
		if(me.getIsPrintItemEditable()) {
			Ext.getBody().addKeyMap({
			    eventName: "keydown",
			    binding: [{
			    	key: [Ext.event.Event.NUM_PLUS, 171, 61],
			        ctrl: true,
			        fn:  me.addNewRow,
			        scope: me
			    },{
			    	key: [83],
			        ctrl: true,
			        fn:  me.saveShortcutHandler,
			        scope: me
			    },{
			    	key: [90],
			        ctrl: true,
			        fn:  me.undoEditOrCreate,
			        scope: me
			    },{
			    	key: [Ext.event.Event.NUM_MINUS],
			        ctrl: true,
			        fn:  me.removeRowFromGrid,
			        scope: me
			    }]
			});

			borderLayout.onPageMouseDown = function(event) {
				if(!me.clickIsOutOfGrid(event)) {
					return true;
				}
				var modifiedRecords = me.getGrid().store.getModifiedRecords();
				if(!me.warningDisplayed && me.editOrCreateInProgress && modifiedRecords.length > 0) {//me.isRowEditedt()) {
					event.stopEvent();
					Ext.MessageBox.show({
						title:getText('common.warning'),
						msg: getText('item.action.leavingInlineEdit'),
						buttons: Ext.MessageBox.YESNO,
						fn: function(btn){
							if(btn==="yes"){
								me.prepareDataAdnSave();
							}
							if(btn==="no"){
								me.getNavigator().fireEvent.call(me.getNavigator(),'datachange');
								me.reInitializeDataAfterEditOrCreate();

							}

						},

						icon: Ext.MessageBox.QUESTION
					});
					me.warningDisplayed = false;
					me.getGrid().getSelectionModel().select(me.actualEditedOrCreatedRecord);
				}else {
					return true;
				}
			};
		}
	},

	clickIsOutOfGrid: function(event) {
		var me = this;
		var isOut = true;
		clickX = event.getX();
		clickY = event.getY();
		var gridX = me.getGrid().getX();
//		var gridY = me.getGrid().getY();
		var toolbarY = borderLayout.getActiveToolbarList().getY();
		if(clickX > gridX && clickY > toolbarY) {
			isOut = false;
		}
		return isOut;
	},

	onGridSelectionChnage: function(view, selected, opts) {
		var me = this;
		me.prepareDataAdnSave();
		me.warnedWorkItemsForParallelEditing = {};
	},

	saveShortcutHandler: function(keyCode, event) {
		event.stopEvent();
		var me = this;
		me.prepareDataAdnSave();
	},

	prepareDataAdnSave: function() {
		var me = this;
		//if not from Gantt we save the changes otherwise the Gantt will handle saving.
		if(CWHF.isNull(me.getGantt())) {
			var params = new Object();
			if(me.row  && me.isRowEditedt()) {
				var workItemIDAbove = null;
				if(me.isSelectedRecordNew()) {
					workItemIDAbove = me.workItemIDAbove;
				}
				lastModified = Ext.util.Format.date(me.timeStampWhenDataArrived, 'Y-m-d H:i:s.m');
				me.saveData(params, workItemIDAbove, lastModified);
			}
		}
	},

	onGridEdit: function(editor, ctx, e) {
		var me = this;
		if(CWHF.isNull(me.row)) {
			me.row = me.getGridSelectedRow();
		}
		if(CWHF.isNull(me.actualEditedOrCreatedRow)) {
			me.actualEditedOrCreatedRow = me.getGridSelectedRow();
		}
	},

	getGridSelectedRow: function() {
		var me = this;
		var selectedData = me.getGrid().getSelectionModel().getSelection();
		var row = selectedData[0].getData();
		return row;
	},

	isSelectedRecordNew: function() {
		var me = this;
		var selectedRow = me.getGridSelectedRow();
		if(me.getGantt()) {
			if(selectedRow.Id < 0) {
				return true;
			}else {
				return false;
			}
		}else {
			return me.isNew;
		}
	},

	onGridBeforeEdit: function(editor, ctx, e) {
		var me = this;
		if(!me.isEditable(ctx)) {
			return false;
		}
		me.actualEditedOrCreatedRecord = ctx.record;
		me.editOrCreateInProgress = true;
		if(!me.isSelectedRecordNew()){
			var editableValue = ctx.record.data.editable;
			if(!editableValue) {
				return false;
			}
		}
		me.warnIfitemLocked(ctx.record.data.workItemID);
		var editorComponent = ctx.column.field;
		var uniformizedFieldType = me.getUniformizedFieldType(ctx.column.extJsRendererClass);
		switch(uniformizedFieldType) {
		    case me.UNIFORMIZED_FIELD_TYPES.singleSelect:
		    	me.loadFieldValue(editorComponent, ctx.record.data.workItemID, ctx.record.data.projectID, ctx.record.data.issueTypeID, ctx.column.fieldID, me.UNIFORMIZED_FIELD_TYPES.singleSelect, 'itemNavigator!loadComboDataByFieldID.action');
		    	break;
		    case me.UNIFORMIZED_FIELD_TYPES.checkBox:
		    	break;
		    case me.UNIFORMIZED_FIELD_TYPES.simpleTextField:
		    	if(me.isSelectedRecordNew()) {
		    		editorComponent.margin = '0 0 0 0';
		    	}
		    	break;
		    case me.UNIFORMIZED_FIELD_TYPES.numberEditor:
		    	break;
		    default:
		    	break;
		}
	},

	warnIfitemLocked: function(workItemID) {
		var me = this;
		if(!me.isSelectedRecordNew() && CWHF.isNull(me.warnedWorkItemsForParallelEditing[workItemID])) {
			me.warnedWorkItemsForParallelEditing[workItemID] = true;
			Ext.Ajax.request({
				url: "itemNavigator!checkIfLockedBeforeInlineEdit.action",
				success: function(response){
					var responseJSON = Ext.decode(response.responseText);
					if(responseJSON.locked) {
						CWHF.showMsgError(responseJSON.itemLockedMessage);
					}
				},
				failure: function(){
				},
				method:'POST',
				params: {'workItemID': workItemID}
			});
		}
	},

	isEditable: function(ctx) {
		var me = this;
		if(ctx.record.data.leaf) {
			return true;
		}else {
			if(ctx.field === 'f' + me.SYSTEM_FIELDS.STARTDATE || ctx.field === 'f' + me.SYSTEM_FIELDS.ENDDATE ||
					ctx.field === 'f' + me.SYSTEM_FIELDS.DURATION ||
				ctx.field === 'f' + me.SYSTEM_FIELDS.TOP_DOWN_STARTDATE || ctx.field === 'f' + me.SYSTEM_FIELDS.TOP_DOWN_ENDDATE ||
					ctx.field === 'f' + me.SYSTEM_FIELDS.TOP_DOWN_DURATION) {
				return false;
			}else {
				return true;
			}
		}
	},


	getShortField: function(fieldID) {
	},

	setColumnEditor: function(shortField, column) {
		var me = this;
		if(me.getIsPrintItemEditable()) {
			column.extJsRendererClass = shortField.extJsRendererClass;
			me.setCellRenderer(shortField. extJsRendererClass, column, shortField);
		}
	},

	createContext: function() {
		var me = this;
		var params = new Object();
		params.actionID = 1;
		params.fromAjax	= true;
		params['params.projectID'] = me.row.projectID;
		params['params.issueTypeID'] = me.row.issueTypeID;
		params.parentID = '';
		params.synopsis = '';
		params.workItemID = '';
		Ext.Ajax.request({
			url: "item!next.action",
			disableCaching:true,
			success: function(response){
			},
			failure: function(){
			},
			method:'POST',
			params: params
		});
	},

	saveData: function(params, workItemIDAbove, lastModified) {
		var me = this;

		var modifiedRecords = me.getGrid().store.getModifiedRecords();
		if(modifiedRecords.length > 0) {
			var firstRecord = modifiedRecords[0];
			var changedFields = firstRecord.getChanges();
			for(var ind in changedFields) {
				var fieldValue = changedFields[ind];
				if (ind && ind.charAt(0) === 'f') {
					params['fieldValues.' + ind] = fieldValue;
				}
			}
			params.projectID = me.row.projectID;
			params.issueTypeID = me.row.issueTypeID;
			if(workItemIDAbove ) {
				params.workItemIDAbove = workItemIDAbove;
			}
			if(me.isSelectedRecordNew()) {
				params.actionID = "1";
				actionIDValue = 1;
				params.workItemID = "";
			}else {
				params.actionID = "2";
				params.workItemID = me.row.workItemID;
			}
			params.inlineEditInNavigator = true;
			params.fromAjax	= true;
			params.lastModified = lastModified;
			Ext.Ajax.request({
				url: "itemSave.action",
				disableCaching:true,
				success: function(response){
					var responseJSON = Ext.decode(response.responseText);
					if(responseJSON.success) {
						borderLayout.workItemIDToSelectAfterReload = responseJSON.data.workItemID;
						me.reInitializeDataAfterEditOrCreate();
						if(me.getGantt() ) {
							var changedRow = me.getGrid().store.getById(responseJSON.data.workItemID);
							changedRow.commit();
						}else {
							me.getNavigator().fireEvent.call(me.getNavigator(),'datachange');
						}
					}else {
						var data = responseJSON.data;
						me.handleErrorsAfterSaveFromSimpleGrids(params, data, workItemIDAbove);

					}
				},
				failure: function(){
					me.reInitializeDataAfterEditOrCreate();
				},
				method:'POST',
				params: params
			});
		}
	},

	handleErrorsAfterSaveFromSimpleGrids: function(submittedParams, responseData, workItemIDAbove) {
		var me = this;
		var errorCode = responseData.errorCode;
		if(errorCode == 2) {
			me.outOfDateHandlerForSimpleGrid(submittedParams, responseData, workItemIDAbove);
		}else {
			var errors = responseData.errors;
			var errorMessage = '';
			Ext.Array.forEach(errors, function (error) {
		    	errorMessage += error.label;
            }, me);
			CWHF.showMsgError(errorMessage);
		}
	},

	isRowEditedt: function() {
		var me = this;
		return me.editOrCreateInProgress;
	},

	outOfDateHandlerForSimpleGrid: function(submittedParams, responseData, workItemIDAbove) {
		var me = this;
		Ext.MessageBox.show({
			title:getText('item.err.wasModified'),
			msg: getText('item.err.modified'),
			buttons: Ext.MessageBox.YESNO,
			buttonText: {yes: getText('item.err.btn.ignore'), no: getText('item.err.btn.overwrite')},
			fn: function(btn){
				if(btn==="yes"){
					//take mine
					me.saveData(submittedParams, workItemIDAbove, responseData.lastModified);
					return;
				}
				if(btn==="no"){
					//take theirs
					me.getNavigator().fireEvent.call(me.getNavigator(),'datachange');
					return;
				}
			},
			icon: Ext.MessageBox.QUESTION
		});
	},

	dateColumnRenderer: function(value) {
		if(value.length > 0) {
			var date = Ext.Date.parse(value, com.trackplus.TrackplusConfig.DateFormat);
			if(!Ext.isDefined(date)) {
				date = new Date(value);
			}
			var formatted = Ext.Date.format(date, com.trackplus.TrackplusConfig.DateFormat);
			return formatted;
		}
		return value;
	},

	setCellRenderer: function(extJsRendererClass, column, shortField) {
		var me = this;
		var uniformizedFieldType = me.getUniformizedFieldType(extJsRendererClass);
		switch(uniformizedFieldType) {
		    case me.UNIFORMIZED_FIELD_TYPES.simpleTextField:
		        me.createTextFieldEditor(column);
		        break;
		    case me.UNIFORMIZED_FIELD_TYPES.singleSelect:
		    	me.createComboEditor(column);
		    	break;
		    case me.UNIFORMIZED_FIELD_TYPES.datePicker:
		    	me.createDateEditor(column, extJsRendererClass);
		    	break;
		    case me.UNIFORMIZED_FIELD_TYPES.checkBox:
		    	me.createCheckBox(column, shortField);
		    	break;
		    case me.UNIFORMIZED_FIELD_TYPES.numberEditor:
		    	me.createNumberEditor(column, shortField, extJsRendererClass);
		    	break;
		    default:
		        editor = null;
		}
	},

	getUniformizedFieldType: function(extJsRendererClass) {
		var me = this;
		switch(extJsRendererClass) {
		    case "com.aurel.trackplus.field.SynopsisTypeRenderer":
		        return me.UNIFORMIZED_FIELD_TYPES.simpleTextField;
		        break;
		    case "com.aurel.trackplus.field.PersonPickerRenderer":
		    case "com.aurel.trackplus.field.StateRenderer":
		    case "com.aurel.trackplus.field.PriorityRenderer":
		    case "com.aurel.trackplus.field.SelectTypeRenderer":
		    	return me.UNIFORMIZED_FIELD_TYPES.singleSelect;
		    	break;
		    case "com.aurel.trackplus.field.DateTypeRenderer":
		    case "com.aurel.trackplus.field.StartDateRenderer":
		    case "com.aurel.trackplus.field.EndDateRenderer":
		    case "com.aurel.trackplus.field.StartDateTargetRenderer":
		    case "com.aurel.trackplus.field.EndDateTargetRenderer":
		    	return me.UNIFORMIZED_FIELD_TYPES.datePicker;
		    	break;
		    case "com.aurel.trackplus.field.CheckBoxTypRenderer":
		    	return me.UNIFORMIZED_FIELD_TYPES.checkBox;
		    case "com.aurel.trackplus.field.DurationRenderer":
		    case "com.aurel.trackplus.field.TargetDurationRenderer":
		    	return me.UNIFORMIZED_FIELD_TYPES.numberEditor;
		    default:
		        return null;
		}
	},

	createTextFieldEditor: function(column) {
		var me = this;
		var editor = Ext.create('Ext.form.field.Text', {
		    listeners: {
		    	change: function(component, newValue, oldValue, eOpts) {
		    	}
		    }
		});
		column.editor = editor;
	},

	createComboEditor: function(column) {
		var me = this;
		var fields = [];
		fields.push({name : 'label',type : 'string'});
		fields.push({name : 'id', type : 'number'});
		var combo = Ext.create('Ext.form.ComboBox', {
			fields: fields,
		    displayField: 'label',
		    valueField: 'id',
		    listeners: {
		    	select: function(combo, records) {
		    	}
		    }
	    });
		column.editor = combo;
	},

	createDateEditor: function(column, originalFieldType) {
		var me = this;
		var editor = Ext.create('Ext.form.field.Date', {
			format: com.trackplus.TrackplusConfig.DateFormat,
			listeners: {
				change: function(component, newValue, oldValue, eOpts) {
					var rowItem = me.getGrid().getSelectionModel().getSelection()[0];
					rowItem.set('f' + component.column.fieldID, newValue);
		    		if(CWHF.isNull(me.getGantt())) {
		    			me.refreshDependentFields(component, rowItem.data, oldValue, newValue);
		    		}else {
    					var task = me.getGrid().store.getById(rowItem.data.Id);
    					if(!me.getNavigator().isShowBaseline()) {
			    			if(this.originalFieldType === 'com.aurel.trackplus.field.StartDateRenderer') {
			    				task.setStartDate(newValue);
			    			}
			    			if(this.originalFieldType === 'com.aurel.trackplus.field.EndDateRenderer') {
			    				task.setEndDate(newValue);

			    			}
			    			if(this.originalFieldType === 'com.aurel.trackplus.field.StartDateTargetRenderer' ||
			    					this.originalFieldType === 'com.aurel.trackplus.field.EndDateTargetRenderer') {
			    				me.refreshDependentFields(component, rowItem.data, oldValue, newValue);
			    			}
    					}else {
    						if(this.originalFieldType === 'com.aurel.trackplus.field.StartDateTargetRenderer') {
    							task.setStartDate(newValue);
    						}
    						if(this.originalFieldType === 'com.aurel.trackplus.field.EndDateTargetRenderer') {
    							task.setEndDate(newValue);
    						}
    						if(this.originalFieldType === 'com.aurel.trackplus.field.StartDateRenderer' ||
    								this.originalFieldType === 'com.aurel.trackplus.field.EndDateRenderer') {
			    				me.refreshDependentFields(component, rowItem.data, oldValue, newValue);
			    			}
    					}
		    		}
		    		me.clearDirtyMarkerFromGroupValues();
				}
			},
			 getValue: function(){
				 if(me.getGantt() ) {
					 if(me.fieldIsTargetOrNormalStartEnd(this.originalFieldType)) {
						 return this.value;
					 }else {
						 return Ext.util.Format.date(this.value, com.trackplus.TrackplusConfig.DateFormat);
					 }

				 }else {
					 var date = this.value;
					 if(date  && date !== "") {
						return Ext.util.Format.date(date, com.trackplus.TrackplusConfig.DateFormat);
					 }else {
						 return this.value;
					 }
				 }
			 }
		});
		editor.originalFieldType = originalFieldType;
		column.format = com.trackplus.TrackplusConfig.DateFormat;
		column.renderer = me.dateColumnRenderer;
		column.editor = editor;
	},

	/*
	 * This method removes all red markers from group items
	 */
	clearDirtyMarkerFromGroupValues: function() {
		var me = this;
		if(me.getGantt()) {
			var store = me.getGantt().getTaskStore();
			var rootNode = store.getRootNode();
			me.parseGridItemsAndClearDirtyMarkerFromGroupItems(rootNode);
		}
	},

	/*
	 * This method parses tasks if a group item is dirty then removes red marker.
	 */
	parseGridItemsAndClearDirtyMarkerFromGroupItems: function(rootItem) {
		var me = this;
		if(rootItem.data.group ) {
			rootItem.commit();
		}
		if(rootItem.childNodes.length !== 0) {
			for(var ind in rootItem.childNodes) {
				var child = rootItem.childNodes[ind];
				if(child.length !== 0) {
					me.parseGridItemsAndClearDirtyMarkerFromGroupItems(child);
				}
			}
		}
	},

	fieldIsTargetOrNormalStartEnd: function(originalFieldType) {
		var me = this;
		if(originalFieldType === 'com.aurel.trackplus.field.StartDateRenderer' || originalFieldType === 'com.aurel.trackplus.field.EndDateRenderer' ||
				originalFieldType === 'com.aurel.trackplus.field.StartDateTargetRenderer' || originalFieldType === 'com.aurel.trackplus.field.EndDateTargetRenderer') {
			return true;
		}else {
			return false;
		}
	},

	createCheckBox: function(column, shortField) {
		var me = this;
		var editor = Ext.create('Ext.form.field.Checkbox', {
			boxLabel: shortField.label,
//		    listeners: {
//		    	change: function(component, newValue, oldValue, eOpts) {
//		    		if(component.column ) {
//		    		}
//		    	}
//			},
			setValue: function(val) {
				if(typeof(val) === "boolean"){
					this.setRawValue(val);
				}else {
					if(val === getText('common.boolean.Y')) {
						this.setRawValue(true);
					}else {
						this.setRawValue(false);
					}
				}
			},
			getValue: function() {
				return this.getRawValue();
			}
		});
		column.editor = editor;
	},

	createNumberEditor: function(column, shortField, originalFieldType) {
		var me = this;
		var editor = Ext.create('Ext.form.field.Number', {
			boxLabel: shortField.label,
			value: 0,
		    listeners: {
		    	change: function(component, newValue, oldValue, eOpts) {
		    		if(component.column ) {
    					var rowItem = me.getGrid().getSelectionModel().getSelection()[0];
		    			if(me.getGantt()  ) {
		    				if(!me.getNavigator().isShowBaseline()) {
		    					if(component.originalFieldType === 'com.aurel.trackplus.field.DurationRenderer') {
		    						var task = me.getGrid().store.getById(rowItem.data.Id);
		    						task.setDuration(newValue);
		    					}
		    					if(component.originalFieldType === 'com.aurel.trackplus.field.TargetDurationRenderer') {
			    					me.refreshDependentFields(component, rowItem.data, oldValue, newValue);
		    					}
		    				}else {
		    					if(component.originalFieldType === 'com.aurel.trackplus.field.TargetDurationRenderer') {
		    						var task = me.getGrid().store.getById(rowItem.data.Id);
		    						task.setDuration(newValue);
		    					}
		    					if(component.originalFieldType === 'com.aurel.trackplus.field.DurationRenderer') {
		    						me.refreshDependentFields(component, rowItem.data, oldValue, newValue);
		    					}
		    				}
		    			}else {
		    				if(component.originalFieldType === 'com.aurel.trackplus.field.DurationRenderer' ||
		    						component.originalFieldType === 'com.aurel.trackplus.field.TargetDurationRenderer') {
		    					me.refreshDependentFields(component, rowItem.data, oldValue, newValue);
		    				}
		    			}
		    		}
		    	}
			},
			getValue : function() {
				var num = parseInt(this.value) || '';
				return num;
			}

		});
		editor.originalFieldType = originalFieldType;
		column.editor = editor;
	},

	loadFieldValue: function(component, workItemID, projectID, issueTypeID, fieldID, fieldType, url) {
		var me = this;
		params = new Object();
		params.workItemID = workItemID;
		params.fieldID = fieldID;
		params.projectID = projectID;
		params.issueTypeID = issueTypeID;
		Ext.Ajax.request({
			url: url,
			disableCaching:true,
			success: function(response){
				responseJSON = Ext.decode(response.responseText);
				if(me.UNIFORMIZED_FIELD_TYPES.singleSelect === fieldType) {
					var storeData = responseJSON.storeData;
					var myStore = Ext.create('Ext.data.Store', {
						fields: ['id', 'label'],
						data: storeData
					});
					component.bindStore(myStore);
				}
			},
			failure: function(){
			},
			method:'POST',
			params: params
		});
	},

	checkUserCreateRight: function() {
		var me = this;
		var selectedData = me.getGrid().getSelectionModel().getSelection();
		if(selectedData.length > 0) {
			var selection = selectedData[0];
			me.row = selection.getData();
			var projectID = me.row.projectID;
			var issueTypeID = me.row.issueTypeID;
			var params = new Object();
			params.projectID = projectID;
			params.issueTypeID = issueTypeID;
			Ext.Ajax.request({
				url: "item!checkUserRightForCreatingNewItem.action",
				disableCaching:true,
				success: function(response){
					var responseJSON = Ext.decode(response.responseText);
					var userCanCreateNewItem = responseJSON.userCanCreateNewItem;
					if(userCanCreateNewItem) {
						me.handleCreatingNewItem();
					}else {
						CWHF.showMsgError(getText('item.err.createNotAllowed'));
						me.reInitializeDataAfterEditOrCreate();
					}
				},
				failure: function(){
				},
				method:'POST',
				params: params
			});
		}
	},

	reInitializeDataAfterEditOrCreate: function() {
		var me = this;
		me.isNew = false;
		me.row = null;
		me.editOrCreateInProgress = false;
		me.warnedWorkItemsForParallelEditing = {};
	},

	handleCreatingNewItem: function() {
		var me = this;
		var selectedData = me.getGrid().getSelectionModel().getSelection();
		if(selectedData.length > 0) {
			var selection = selectedData[0];
			me.row = selection.getData();
			me.isNew = true;
			var taskTypeName = me.row.f2;
			var workspaceName = me.row.f1;
			var newTask = new Object();
			newTask.leaf = true;
			newTask.cls = 'item-calendarStartDateMissing';
			newTask.f2 = taskTypeName;
			newTask.f1 = workspaceName;
			newTask.f17 = "";
			var tmpID = Math.floor((Math.random() * 10) + 1000) * -1;
			newTask.Id = tmpID;
			newTask.workItemID = null;
			newTask.projectID = me.row.projectID;
			newTask.issueTypeID = me.row.issueTypeID;
			newTask.linkable = me.row.linkable;
			newTask.editable = me.row.editable;
			newTask.workItemIDAbove = me.row.workItemID;
			me.workItemIDAbove =  me.row.workItemID;
			switch(me.getNavigator().$className) {
			    case 'com.trackplus.itemNavigator.SimpleTreeGridViewPlugin':
			    case 'com.trackplus.itemNavigator.WBSViewPlugin':
			    case 'com.trackplus.itemNavigator.GanttViewPlugin':
			    	me.selectNewlyAddedRowTree(selection, newTask);
			    	break;
			    case 'com.trackplus.itemNavigator.FlatGridViewPlugin':
			    	me.selectNewlyAddedRowFlat(selection, newTask);
			    	break;
			    default:
			    	break;
			}
			if(CWHF.isNull(me.getGantt())) {
				me.createContext();
			}
			me.editOrCreateInProgress = true;
			me.recalculateGridItemIndex();

			selectedData = me.getGrid().getSelectionModel().getSelection();
			if(selectedData.length > 0) {
				var selection = selectedData[0];
				selection.set('f17', getText('itemov.ganttView.newlyCreatedItemDummyTitle'));
			}
		}
	},

	addNewRow: function(keyCode, event) {
		var me = this;
		event.stopEvent();
		me.reInitializeDataAfterEditOrCreate();
		me.checkUserCreateRight();
	},

	selectNewlyAddedRowTree: function(selection, newTask) {
		var me = this;
		var parentNode = selection.parentNode;
		var rowIndex = parentNode.indexOf(selection);
		rowIndex++;
		me.actualEditedOrCreatedRecord = parentNode.insertChild(rowIndex, newTask);
		me.getGrid().getSelectionModel().select(me.actualEditedOrCreatedRecord);
	},

	selectNewlyAddedRowFlat: function(selection, newTask) {
		var me = this;
		var rowIndex = me.getGrid().store.indexOf(selection);
		rowIndex++;
		me.actualEditedOrCreatedRecord = me.getGrid().store.insert(rowIndex, newTask);
		me.getGrid().getSelectionModel().select(me.actualEditedOrCreatedRecord);
	},

	dataChangeSuccess: function() {
		var me = this;
		var id = borderLayout.workItemIDToSelectAfterReload;
		var recordToSelect = me.getGrid().store.getById(id);
		me.getGrid().getSelectionModel().select(recordToSelect);
	},

	undoEditOrCreate: function() {
		var me = this;
		me.getNavigator().fireEvent.call(me.getNavigator(),'datachange');
	},

	removeRowFromGrid: function(keyCode, event) {
		var me = this;
		event.stopEvent();
		if(CWHF.isNull(me.getGantt())) {
			if(!me.isSelectedRecordNew()) {
				var selectedData = me.getGrid().getSelectionModel().getSelection();
				if(selectedData.length > 0) {
					var selection = selectedData[0];
					var workItemID = selection.getData().workItemID;
					me.closeItemAjax(workItemID);
				}
			}
		}else {
			var selectedData = me.getGrid().getSelectionModel().getSelection();
			if(selectedData.length > 0) {
				var selection = selectedData[0];
				if(me.isSelectedRecordNew()) {
					me.getGantt().getTaskStore().remove(selection);
					me.recalculateGridItemIndex();
				}else {
					var workItemID = selection.getData().workItemID;
					me.closeItemAjax(workItemID);
				}
			}
		}
	},

	handleRemoveFromGantt: function() {
		var me = this;
		var selectedData = me.getGrid().getSelectionModel().getSelection();
		if(selectedData.length > 0) {
			var selection = selectedData[0];
			if(me.isSelectedRecordNew()) {
				me.getGantt().getTaskStore().remove(selection);
				me.recalculateGridItemIndex();
			}else {
				var workItemID = selection.getData().workItemID;
				me.closeItemAjax(workItemID);
			}
		}
	},

	closeItemAjax: function(workItemID) {
		var me = this;
		var params = {};
		params.workItemID = workItemID;
		Ext.Ajax.request({
			url: "itemNavigator!closeWorkItem.action",
			disableCaching:true,
			success: function(response){
				var responseJSON = Ext.decode(response.responseText);
				if(responseJSON.success) {
					CWHF.showMsgInfo(getText('item.action.stateChangeToClosed', workItemID));
					if(CWHF.isNull(me.getGantt())) {
						me.getNavigator().fireEvent.call(me.getNavigator(),'datachange');
					}else {
						var task = me.getGrid().store.getById(workItemID);
						task.data['f' + me.SYSTEM_FIELDS.STATE_FIELD_ID] = responseJSON.closedStateName;
						me.getGrid().getView().refresh();
					}
				}else {
					var data = responseJSON.data;
					var errors = data.errors;
					var errorMessage = '';
					Ext.Array.forEach(errors, function (error) {
				    	errorMessage += error.label;
	                }, me);
					CWHF.showMsgError(errorMessage);
				}
			},
			failure: function(){
			},
			method:'POST',
			params: params
		});

	},

	recalculateGridItemIndex: function() {
		var me = this;
		if(me.getGantt() ) {
			var store = me.getGantt().getTaskStore();
			var rootNode = store.getRootNode();
			me.workItemIndex = 0;
			me.parseGridItemsAndResetIndex(rootNode);
			me.getGrid().getView().refresh();
		}
	},

	parseGridItemsAndResetIndex: function(rootItem) {
		var me = this;
		var isProject = false;
		if(rootItem.data.group ) {
			isProject = rootItem.data.group;
		}
		if(!isProject && rootItem.data.Id !== 'root') {
			rootItem.data.workItemIndex = me.workItemIndex;
			me.workItemIndex++;
		}
		if(rootItem.childNodes.length !== 0) {
			for(var ind in rootItem.childNodes) {
				var child = rootItem.childNodes[ind];
				if(child.length !== 0) {
					me.parseGridItemsAndResetIndex(child);
				}
			}
		}
	},

    getRendererForSpecificColumns: function(uniformizedFieldType, columnNr) {
		var me = this;
		var renderer = null;
	    if(uniformizedFieldType === me.UNIFORMIZED_FIELD_TYPES.datePicker) {
	    	renderer = function (value, metaData, record, row, col, store, gridView) {
	    		var isProject = false;
    			if(record.data.group ) {
    				isProject = record.data.group;
    			}
    			if(!isProject) {
		    		var dateObj = null;
		    		if(value instanceof Date) {
		    			dateObj = new Date(value.getTime());
		    		}else {
		    			dateObj = Ext.Date.parse(value, com.trackplus.TrackplusConfig.DateFormat);
		    		}
		    		if(dateObj ) {
		    			if(!isProject) {
		    				return Ext.util.Format.date(dateObj, com.trackplus.TrackplusConfig.DateFormat);
		    			}
		    		}
    			}else {
	    			if(columnNr === 0 ) {
	    				return record.data['Name'];
	    			}else {
	    				return "";
	    			}
	    		}
	    	};
	    }
	    if(uniformizedFieldType === me.UNIFORMIZED_FIELD_TYPES.singleSelect) {
	    	renderer = function (val, metaData, record, row, col, store, gridView) {
    			var combo = metaData.column.getEditor();
    			if(val && combo && combo.store && combo.displayField){
    				var valueFieldInt = parseInt(val);
    				var index = combo.store.findExact(combo.valueField, valueFieldInt);
    				if(index >= 0){
    					return combo.store.getAt(index).get(combo.displayField);
    				}
    			}
    			return val;
	    	};
	    }
	    if(uniformizedFieldType === me.UNIFORMIZED_FIELD_TYPES.checkBox) {
	    	renderer= function (val, metaData, record, row, col, store, gridView) {
	    		var isProject = false;
    			if(record.data.group ) {
    				isProject = record.data.group;
    			}
    			if(!isProject) {
    				if(typeof(val) === "boolean"){
    					if(val) {
    						return getText('common.boolean.Y');
    					}else {
    						return getText('common.boolean.N');
    					}
    				}else {
    					if(val === "true") {
    						return getText('common.boolean.Y');
    					}
    					if(val === "false") {
    						return getText('common.boolean.N');
    					}
    					if(val === "") {
    						return getText('common.boolean.N');
    					}
    					return val;
    				}
    			}else {
    				return '';
    			}
	    	};
	    }

	    if(uniformizedFieldType === me.UNIFORMIZED_FIELD_TYPES.numberEditor) {
	    	renderer = function (value, metaData, record, row, col, store, gridView) {
	    		var isProject = false;
	    		if(record.data.group ) {
	    			isProject = record.data.group;
	    		}
	    		if(!isProject) {
	    			return value;
	    		}
	    		return '';
	    	};
	    }
	    return renderer;
	},

	/************** HELPRE methods for updating dependents fields **************/

	refreshDependentFields: function(renderer, model, oldValue, newValue) {
		var me = this;
		var fieldID = renderer.column.fieldID;
		var fieldFullID = 'f' + fieldID;
		var originalFieldType = renderer.originalFieldType;
		var rendererClassInstance = null;

		var convertedOldValue = oldValue;
		var convertedNewValue = newValue;
		switch(originalFieldType) {
		    case 'com.aurel.trackplus.field.StartDateRenderer':
		    	rendererClassInstance =  Ext.create('com.aurel.trackplus.field.StartDateRenderer', {});
		    	renderer.durationFieldID = me.SYSTEM_FIELDS.DURATION;
		    	renderer.startDateFieldID = me.SYSTEM_FIELDS.STARTDATE;
		    	renderer.endDateFieldID = me.SYSTEM_FIELDS.ENDDATE;
		    	if(CWHF.isNull(me.getGantt())) {
		    		convertedOldValue = Ext.Date.parse(oldValue, com.trackplus.TrackplusConfig.DateFormat);
		    		convertedNewValue = Ext.Date.parse(newValue, com.trackplus.TrackplusConfig.DateFormat);
		    	}
		    	break;
		    case 'com.aurel.trackplus.field.EndDateRenderer':
		    	rendererClassInstance =  Ext.create('com.aurel.trackplus.field.EndDateRenderer', {});
		    	renderer.durationFieldID = me.SYSTEM_FIELDS.DURATION;
		    	renderer.startDateFieldID = me.SYSTEM_FIELDS.STARTDATE;
		    	renderer.endDateFieldID = me.SYSTEM_FIELDS.ENDDATE;
		    	if(CWHF.isNull(me.getGantt())) {
		    		convertedOldValue = Ext.Date.parse(oldValue, com.trackplus.TrackplusConfig.DateFormat);
		    		convertedNewValue = Ext.Date.parse(newValue, com.trackplus.TrackplusConfig.DateFormat);
		    	}
		    	break;
		    case 'com.aurel.trackplus.field.StartDateTargetRenderer':
		    	rendererClassInstance =  Ext.create('com.aurel.trackplus.field.StartDateTargetRenderer', {});
		    	renderer.startDateFieldID = me.SYSTEM_FIELDS.TOP_DOWN_STARTDATE;
		    	renderer.endDateFieldID = me.SYSTEM_FIELDS.TOP_DOWN_ENDDATE;
		    	renderer.durationFieldID = me.SYSTEM_FIELDS.TOP_DOWN_DURATION;
		    	if(CWHF.isNull(me.getGantt())) {
		    		convertedOldValue = Ext.Date.parse(oldValue, com.trackplus.TrackplusConfig.DateFormat);
		    		convertedNewValue = Ext.Date.parse(newValue, com.trackplus.TrackplusConfig.DateFormat);
		    	}
		    	break;
		    case 'com.aurel.trackplus.field.EndDateTargetRenderer':
		    	rendererClassInstance =  Ext.create('com.aurel.trackplus.field.EndDateTargetRenderer', {});
		    	renderer.startDateFieldID = me.SYSTEM_FIELDS.TOP_DOWN_STARTDATE;
		    	renderer.endDateFieldID = me.SYSTEM_FIELDS.TOP_DOWN_ENDDATE;
		    	renderer.durationFieldID = me.SYSTEM_FIELDS.TOP_DOWN_DURATION;
		    	if(CWHF.isNull(me.getGantt())) {
		    		convertedOldValue = Ext.Date.parse(oldValue, com.trackplus.TrackplusConfig.DateFormat);
		    		convertedNewValue = Ext.Date.parse(newValue, com.trackplus.TrackplusConfig.DateFormat);
		    	}
		    	break;
		    case 'com.aurel.trackplus.field.DurationRenderer':
		    	rendererClassInstance =  Ext.create('com.aurel.trackplus.field.DurationRenderer', {});
		    	renderer.durationFieldID = me.SYSTEM_FIELDS.DURATION;
		    	renderer.startDateFieldID = me.SYSTEM_FIELDS.STARTDATE;
		    	renderer.endDateFieldID = me.SYSTEM_FIELDS.ENDDATE;
		    	break;

		    case 'com.aurel.trackplus.field.TargetDurationRenderer':
		    	rendererClassInstance =  Ext.create('com.aurel.trackplus.field.TargetDurationRenderer', {});
		    	renderer.durationFieldID = me.SYSTEM_FIELDS.TOP_DOWN_DURATION;
		    	renderer.startDateFieldID = me.SYSTEM_FIELDS.TOP_DOWN_STARTDATE;
		    	renderer.endDateFieldID = me.SYSTEM_FIELDS.TOP_DOWN_ENDDATE;
		    	break;

		    default:
		    	return;
		}
		var fieldValues = {};
		fieldValues[fieldFullID] = convertedNewValue;
		var oldFieldValues = {};
		oldFieldValues[fieldFullID] = convertedOldValue;
		var isFromGantt = false;
		if(me.getGantt() ) {
			isFromGantt = true;
		}
		rendererClassInstance.refreshDependentFields.call(me, fieldID, fieldValues, oldFieldValues, renderer, model, isFromGantt);
	},

	updateField: function(fieldID, fieldValue, fieldDisplayValue, fieldValues) {
		var me=this;
		if(fieldValue ) {
			var fieldName = "f" + fieldID;
			var rowItem = me.getGrid().getSelectionModel().getSelection()[0];
			var convertedFieldValue = fieldValue;
			if(me.getGantt() ) {
				if(fieldID === me.SYSTEM_FIELDS.STARTDATE || fieldID === me.SYSTEM_FIELDS.ENDDATE ||
						fieldID === me.SYSTEM_FIELDS.TOP_DOWN_STARTDATE || fieldID === me.SYSTEM_FIELDS.TOP_DOWN_ENDDATE) {
					convertedFieldValue = Ext.Date.parse(fieldValue, com.trackplus.TrackplusConfig.DateFormat);
				}
			}
			rowItem.set(fieldName, convertedFieldValue);
			me.getGrid().getView().refresh();
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
		if (CWHF.isNull(dateStr) || dateStr.length===0) {
			if (nowIfNull) {
				return new Date();
			} else {
				return null;
			}
		} else {
			var retValue = Ext.Date.parse(dateStr, com.trackplus.TrackplusConfig.DateFormat);
			return retValue;
		}
	},

	getDurationBetweenDates: function(startDateStr, endDate) {
		var me=this;
		var startDate = me.parseDate(startDateStr, false);
		if (CWHF.isNull(startDate) || CWHF.isNull(endDate)) {
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
	        		if (day===6) {
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
	}
	/************** ENDS OF HELPRE methods for updating dependents fields **************/
});
