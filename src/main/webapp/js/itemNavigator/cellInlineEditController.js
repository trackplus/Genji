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
	selectedIdOrValue: null,
	row: null, //edited row or in case of creating new item the above row
	actualEditedOrCreatedRow: null,
	actualEditedOrCreatedRecord: null,
	warningDisplayed: false,

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
		Ext.apply(me, config);
	},

	initListeners: function() {
		var me = this;
		if(me.isPrintItemEditable) {
			me.grid.addListener('edit', me.onGridEdit, me);
			me.grid.addListener('beforeedit', me.onGridBeforeEdit, me);
			me.grid.addListener('render', me.onReadyHandler, me);
		}
	},

	initGanttSpecificListener: function() {
		var me = this;
		if(me.isPrintItemEditable) {
			me.gantt.addListener('selectionchange', me.onGridSelectionChnage, me);
		}
	},

	onReadyHandler: function() {
		var me = this;
		if(me.isPrintItemEditable) {
			Ext.getBody().addKeyMap({
			    eventName: "keydown",
			    binding: [{
			    	key: [Ext.EventObject.NUM_PLUS, 171, 61],
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
			    	key: [Ext.EventObject.NUM_MINUS],
			        ctrl: true,
			        fn:  me.removeRowFromGrid,
			        scope: me
			    }]
			});

			borderLayout.onPageMouseDown = function(event) {
				if(!me.clickIsOutOfGrid(event)) {
					return true;
				}
				var modifiedRecords = me.grid.store.getModifiedRecords();
				if(!me.warningDisplayed && me.editOrCreateInProgress && modifiedRecords.length > 0) {//me.isRowEditedt()) {
					event.stopEvent();
					Ext.MessageBox.show({
						title:getText('common.warning'),
						msg: getText('item.action.leavingInlineEdit'),
						buttons: Ext.MessageBox.YESNO,
						fn: function(btn){
							if(btn=="yes"){
								me.prepareDataAdnSave();
							}
							if(btn=="no"){
								me.navigator.fireEvent.call(me.navigator,'datachange');
								me.reInitializeDataAfterEditOrCreate();

							}

						},

						icon: Ext.MessageBox.QUESTION
					});
					me.warningDisplayed = false;
					me.grid.getSelectionModel().select(me.actualEditedOrCreatedRecord);
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
		var gridX = me.grid.getX();
//		var gridY = me.grid.getY();
		var toolbarY = borderLayout.getActiveToolbarList().getY();
		if(clickX > gridX && clickY > toolbarY) {
			isOut = false;
		}
		return isOut;
	},

	onGridSelectionChnage: function(view, selected, opts) {
		var me = this;
		me.prepareDataAdnSave();
	},

	saveShortcutHandler: function(keyCode, event) {
		event.stopEvent();
		var me = this;
		me.prepareDataAdnSave();
	},

	prepareDataAdnSave: function() {
		var me = this;
		//if not from Gantt we save the changes otherwise the Gantt will handle saving.
		if(me.gantt == null) {
			var params = new Object();
			if(me.row != null && me.isRowEditedt()) {
				var workItemIDAbove = null;
				if(me.isNew) {
					workItemIDAbove = me.workItemIDAbove;
				}
				me.saveData(params, workItemIDAbove);
			}
		}
	},

	onGridEdit: function(editor, ctx, e) {
		var me = this;
		if(me.selectedIdOrValue != null) {
			if(me.row == null) {
				me.row = me.getGridSelectedRow();
			}
			if(me.actualEditedOrCreatedRow == null) {
				me.actualEditedOrCreatedRow = me.getGridSelectedRow();
			}
			/*if(me.gantt != null && !me.isNew) {
				if(ctx.field == 'f19' || ctx.field == 'f20' ||
						ctx.field == 'f29' || ctx.field == 'f30') {
//					me.navigator.normalizeTask(ctx.field, me.selectedIdOrValue, me.row.workItemID);
				}
			}*/
		}
	},

	getGridSelectedRow: function() {
		var me = this;
		var selectedData = me.grid.getSelectionModel().getSelection();
		var row = selectedData[0].getData();
		return row;
	},

	onGridBeforeEdit: function(editor, ctx, e) {
		var me = this;
		if(!me.isEditable(ctx)) {
			return false;
		}
		me.selectedIdOrValue = null;
		me.actualEditedOrCreatedRecord = ctx.record;
		me.editOrCreateInProgress = true;
		if(!me.isNew){
			var editableValue = ctx.record.data.editable;
			if(!editableValue) {
				return false;
			}
		}
		var editorComponent = ctx.column.getEditor();
		var uniformizedFieldType = me.getUniformizedFieldType(ctx.column.extJsRendererClass);
		switch(uniformizedFieldType) {
		    case me.UNIFORMIZED_FIELD_TYPES.singleSelect:
		    	me.loadFieldValue(editorComponent, ctx.record.data.workItemID, ctx.record.data.projectID, ctx.record.data.issueTypeID, ctx.column.fieldID, me.UNIFORMIZED_FIELD_TYPES.singleSelect, 'itemNavigator!loadComboDataByFieldID.action');
		    	break;
		    case me.UNIFORMIZED_FIELD_TYPES.checkBox:
		    	me.loadFieldValue(editorComponent, ctx.record.data.workItemID, ctx.record.data.projectID, ctx.record.data.issueTypeID, ctx.column.fieldID, me.UNIFORMIZED_FIELD_TYPES.checkBox, 'itemNavigator!loadFieldValue.action');
		    	break;
		    case me.UNIFORMIZED_FIELD_TYPES.simpleTextField:
		    	if(me.isNew) {
		    		editorComponent.margin = '0 0 0 0';
		    	}
		    	break;
		    case me.UNIFORMIZED_FIELD_TYPES.numberEditor:
		    	break;
		    default:
		    	break;
		}
	},

	isEditable: function(ctx) {
		var me = this;
		Ext.defer(me.doInitAfterEverythingLoadedAndRendered, 150, me);
		if(ctx.record.data.leaf) {
			return true;
		}else {
			if(ctx.field == 'f' + me.SYSTEM_FIELDS.STARTDATE || ctx.field == 'f' + me.SYSTEM_FIELDS.ENDDATE ||
					ctx.field == 'f' + me.SYSTEM_FIELDS.DURATION ||
				ctx.field == 'f' + me.SYSTEM_FIELDS.TOP_DOWN_STARTDATE || ctx.field == 'f' + me.SYSTEM_FIELDS.TOP_DOWN_ENDDATE ||
					ctx.field == 'f' + me.SYSTEM_FIELDS.TOP_DOWN_DURATION) {
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
		if(me.isPrintItemEditable) {
			var editor = new Object();
			editor.allowBlank = false;
			editor.selectOnFocus = true;
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

	saveData: function(params, workItemIDAbove) {
		var me = this;

		var modifiedRecords = me.grid.store.getModifiedRecords();
		if(modifiedRecords.length > 0) {
			var firstRecord = modifiedRecords[0];
			var changedFields = firstRecord.getChanges();
			for(var ind in changedFields) {
				var fieldValue = changedFields[ind];
				if (ind && ind.charAt(0) == 'f') {
					params['fieldValues.' + ind] = fieldValue;
				}
			}
			params.projectID = me.row.projectID;
			params.issueTypeID = me.row.issueTypeID;
			if(workItemIDAbove != null) {
				params.workItemIDAbove = workItemIDAbove;
			}
			if(me.isNew) {
				params.actionID = "1";
				actionIDValue = 1;
				params.workItemID = "";
			}else {
				params.actionID = "2";
				params.workItemID = me.row.workItemID;
			}
			params.inlineEditInNavigator = true;
			params.fromAjax	= true;
			Ext.Ajax.request({
				url: "itemSave.action",
				disableCaching:true,
				success: function(response){
					var responseJSON = Ext.decode(response.responseText);
					if(responseJSON.success) {
						borderLayout.workItemIDToSelectAfterReload = responseJSON.data.workItemID;
						me.reInitializeDataAfterEditOrCreate();
						if(me.gantt != null) {
							var changedRow = me.grid.store.getById(responseJSON.data.workItemID);
							changedRow.commit();
						}else {
							me.navigator.fireEvent.call(me.navigator,'datachange');
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
					me.reInitializeDataAfterEditOrCreate();
				},
				method:'POST',
				params: params
			});
		}
	},

	isRowEditedt: function() {
		var me = this;
		/*var modified = false;

		var rowItem = me.grid.getSelectionModel().getSelection()[0];
		console.log(rowItem)
		var modifiedRecords = me.grid.store.getUpdatedRecords();
		console.log(modifiedRecords);
		if(modifiedRecords.length > 0) {
			modified = true;
		}*/
		return me.editOrCreateInProgress;
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
		    		me.selectedIdOrValue = newValue;
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
		    		me.selectedIdOrValue = records[0].data.id;
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
					me.selectedIdOrValue = Ext.Date.format(newValue, com.trackplus.TrackplusConfig.DateFormat);
					var rowItem = me.grid.getSelectionModel().getSelection()[0];
					rowItem.set('f' + component.column.fieldID, newValue);
		    		if(me.gantt == null) {
		    			me.refreshDependentFields(component, rowItem.data, oldValue, newValue);
		    		}else {
    					var task = me.grid.store.getById(rowItem.data.Id);
    					if(!me.navigator.isShowBaseline()) {
			    			if(this.originalFieldType == 'com.aurel.trackplus.field.StartDateRenderer') {
			    				task.setStartDate(newValue);
			    			}
			    			if(this.originalFieldType == 'com.aurel.trackplus.field.EndDateRenderer') {
			    				task.setEndDate(newValue);

			    			}
			    			if(this.originalFieldType == 'com.aurel.trackplus.field.StartDateTargetRenderer' ||
			    					this.originalFieldType == 'com.aurel.trackplus.field.EndDateTargetRenderer') {
			    				me.refreshDependentFields(component, rowItem.data, oldValue, newValue);
			    			}
    					}else {
    						if(this.originalFieldType == 'com.aurel.trackplus.field.StartDateTargetRenderer') {
    							task.setStartDate(newValue);
    						}
    						if(this.originalFieldType == 'com.aurel.trackplus.field.EndDateTargetRenderer') {
    							task.setEndDate(newValue);
    						}
    						if(this.originalFieldType == 'com.aurel.trackplus.field.StartDateRenderer' ||
    								this.originalFieldType == 'com.aurel.trackplus.field.EndDateRenderer') {
			    				me.refreshDependentFields(component, rowItem.data, oldValue, newValue);
			    			}
    					}
		    		}
				}
			},
			 getValue : function(){
				 if(me.gantt != null) {
					 if(me.fieldIsTargetOrNormalStartEnd(this.originalFieldType)) {
						 return this.value;
					 }else {
						 return Ext.util.Format.date(this.value, com.trackplus.TrackplusConfig.DateFormat);
					 }

				 }else {
					 var date = this.value;
					 if(date != null && date != "") {
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

	fieldIsTargetOrNormalStartEnd: function(originalFieldType) {
		var me = this;
		if(originalFieldType == 'com.aurel.trackplus.field.StartDateRenderer' || originalFieldType == 'com.aurel.trackplus.field.EndDateRenderer' ||
				originalFieldType == 'com.aurel.trackplus.field.StartDateTargetRenderer' || originalFieldType == 'com.aurel.trackplus.field.EndDateTargetRenderer') {
			return true;
		}else {
			return false;
		}
	},

	createCheckBox: function(column, shortField) {
		var me = this;
		var editor = Ext.create('Ext.form.field.Checkbox', {
			boxLabel: shortField.label,
			/*getValue: function() {
				if(this.checked) {
					return true;
//		    		me.selectedIdOrValue = true;
//		    		return getText('common.boolean.Y');
		    	}else {
		    		return false;
//		    		me.selectedIdOrValue = false;
//		    		return getText('common.boolean.N');
		    	}
		    },*/
		    listeners: {
		    	change: function(component, newValue, oldValue, eOpts) {
		    		if(component.column != null) {
			    		me.selectedIdOrValue = Ext.Date.format(newValue, com.trackplus.TrackplusConfig.DateFormat);
		    		}
		    	}
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
		    		if(component.column != null) {
			    		me.selectedIdOrValue = newValue;
    					var rowItem = me.grid.getSelectionModel().getSelection()[0];
		    			if(me.gantt != null ) {
		    				if(!me.navigator.isShowBaseline()) {
		    					if(component.originalFieldType == 'com.aurel.trackplus.field.DurationRenderer') {
		    						var task = me.grid.store.getById(rowItem.data.Id);
		    						task.setDuration(newValue);
		    					}
		    					if(component.originalFieldType == 'com.aurel.trackplus.field.TargetDurationRenderer') {
			    					me.refreshDependentFields(component, rowItem.data, oldValue, newValue);
		    					}
		    				}else {
		    					if(component.originalFieldType == 'com.aurel.trackplus.field.TargetDurationRenderer') {
		    						var task = me.grid.store.getById(rowItem.data.Id);
		    						task.setDuration(newValue);
		    					}
		    					if(component.originalFieldType == 'com.aurel.trackplus.field.DurationRenderer') {
		    						me.refreshDependentFields(component, rowItem.data, oldValue, newValue);
		    					}
		    				}
		    			}else {
		    				if(component.originalFieldType == 'com.aurel.trackplus.field.DurationRenderer' ||
		    						component.originalFieldType == 'com.aurel.trackplus.field.TargetDurationRenderer') {
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
				if(me.UNIFORMIZED_FIELD_TYPES.singleSelect == fieldType) {
					var storeData = responseJSON.storeData;
					var myStore = Ext.create('Ext.data.Store', {
						fields: ['id', 'label'],
						data: storeData
					});
					component.bindStore(myStore);
				}
				if(me.UNIFORMIZED_FIELD_TYPES.checkBox == fieldType) {
					var value = responseJSON.value;
					component.setValue(value);
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
		var selectedData = me.grid.getSelectionModel().getSelection();
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
	},

	handleCreatingNewItem: function() {
		var me = this;
		var selectedData = me.grid.getSelectionModel().getSelection();
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
			switch(me.navigator.$className) {
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
			if(me.gantt == null) {
				me.createContext();
			}
			me.editOrCreateInProgress = true;
			me.recalculateGridItemIndex();

			selectedData = me.grid.getSelectionModel().getSelection();
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
		me.grid.getSelectionModel().select(me.actualEditedOrCreatedRecord);
	},

	selectNewlyAddedRowFlat: function(selection, newTask) {
		var me = this;
		var rowIndex = me.grid.store.indexOf(selection);
		rowIndex++;
		me.actualEditedOrCreatedRecord = me.grid.store.insert(rowIndex, newTask);
		me.grid.getSelectionModel().select(me.actualEditedOrCreatedRecord);
	},

	dataChangeSuccess: function() {
		var me = this;
		var id = borderLayout.workItemIDToSelectAfterReload;
		var recordToSelect = me.grid.store.getById(id);
		me.grid.getSelectionModel().select(recordToSelect);
	},

	undoEditOrCreate: function() {
		var me = this;
		me.navigator.fireEvent.call(me.navigator,'datachange');
	},

	removeRowFromGrid: function(keyCode, event) {
		var me = this;
		event.stopEvent();
		if(me.gantt == null) {
			if(!me.isNew) {
				var selectedData = me.grid.getSelectionModel().getSelection();
				if(selectedData.length > 0) {
					var selection = selectedData[0];
					var workItemID = selection.getData().workItemID;
					me.closeItemAjax(workItemID);
				}
			}
		}else {
			var selectedData = me.grid.getSelectionModel().getSelection();
			if(selectedData.length > 0) {
				var selection = selectedData[0];
				var isNew = false;
//				if(selection.data.Id.indexOf("newTaskID") > -1) {
				if(selection.data.Id < 0) {
					isNew = true;
				}
				if(isNew) {
					me.gantt.getTaskStore().remove(selection);
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
		var selectedData = me.grid.getSelectionModel().getSelection();
		if(selectedData.length > 0) {
			var selection = selectedData[0];
			var isNew = false;
//			if(selection.data.Id.indexOf("newTaskID") > -1) {
			if(selection.data.Id < 0) {
				isNew = true;
			}
			if(isNew) {
				me.gantt.getTaskStore().remove(selection);
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
					if(me.gantt == null) {
						me.navigator.fireEvent.call(me.navigator,'datachange');
					}else {
						var task = me.grid.store.getById(workItemID);
						task.data['f' + me.SYSTEM_FIELDS.STATE_FIELD_ID] = responseJSON.closedStateName;
						me.grid.getView().refresh();
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
		if(me.gantt != null) {
			var store = me.gantt.getTaskStore();
//			var store = me.view.getTaskStore();
			var rootNode = store.treeStore.getRootNode();
			me.workItemIndex = 0;
			me.parseGridItemsAndResetIndex(rootNode);
			me.grid.getView().refresh();
		}
	},

	parseGridItemsAndResetIndex: function(rootItem) {
		var me = this;
		var isProject = false;
		if(rootItem.data.group != null) {
			isProject = rootItem.data.group;
		}
		if(!isProject && rootItem.data.Id != 'root') {
			rootItem.data.workItemIndex = me.workItemIndex;
			me.workItemIndex++;
		}
		if(rootItem.childNodes.length != 0) {
			for(var ind in rootItem.childNodes) {
				var child = rootItem.childNodes[ind];
				if(child.length != 0) {
					me.parseGridItemsAndResetIndex(child);
				}
			}
		}
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
		    	if(me.gantt == null) {
		    		convertedOldValue = Ext.Date.parse(oldValue, com.trackplus.TrackplusConfig.DateFormat);
		    		convertedNewValue = Ext.Date.parse(newValue, com.trackplus.TrackplusConfig.DateFormat);
		    	}
		    	break;
		    case 'com.aurel.trackplus.field.EndDateRenderer':
		    	rendererClassInstance =  Ext.create('com.aurel.trackplus.field.EndDateRenderer', {});
		    	renderer.durationFieldID = me.SYSTEM_FIELDS.DURATION;
		    	renderer.startDateFieldID = me.SYSTEM_FIELDS.STARTDATE;
		    	renderer.endDateFieldID = me.SYSTEM_FIELDS.ENDDATE;
		    	if(me.gantt == null) {
		    		convertedOldValue = Ext.Date.parse(oldValue, com.trackplus.TrackplusConfig.DateFormat);
		    		convertedNewValue = Ext.Date.parse(newValue, com.trackplus.TrackplusConfig.DateFormat);
		    	}
		    	break;
		    case 'com.aurel.trackplus.field.StartDateTargetRenderer':
		    	rendererClassInstance =  Ext.create('com.aurel.trackplus.field.StartDateTargetRenderer', {});
		    	renderer.startDateFieldID = me.SYSTEM_FIELDS.TOP_DOWN_STARTDATE;
		    	renderer.endDateFieldID = me.SYSTEM_FIELDS.TOP_DOWN_ENDDATE;
		    	renderer.durationFieldID = me.SYSTEM_FIELDS.TOP_DOWN_DURATION;
		    	if(me.gantt == null) {
		    		convertedOldValue = Ext.Date.parse(oldValue, com.trackplus.TrackplusConfig.DateFormat);
		    		convertedNewValue = Ext.Date.parse(newValue, com.trackplus.TrackplusConfig.DateFormat);
		    	}
		    	break;
		    case 'com.aurel.trackplus.field.EndDateTargetRenderer':
		    	rendererClassInstance =  Ext.create('com.aurel.trackplus.field.EndDateTargetRenderer', {});
		    	renderer.startDateFieldID = me.SYSTEM_FIELDS.TOP_DOWN_STARTDATE;
		    	renderer.endDateFieldID = me.SYSTEM_FIELDS.TOP_DOWN_ENDDATE;
		    	renderer.durationFieldID = me.SYSTEM_FIELDS.TOP_DOWN_DURATION;
		    	if(me.gantt == null) {
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
		if(me.gantt != null) {
			isFromGantt = true;
		}
		rendererClassInstance.refreshDependentFields.call(me, fieldID, fieldValues, oldFieldValues, renderer, model, isFromGantt);
	},

	updateField: function(fieldID, fieldValue, fieldDisplayValue, fieldValues) {
		var me=this;
		if(fieldValue != null) {
			var fieldName = "f" + fieldID;
			var rowItem = me.grid.getSelectionModel().getSelection()[0];
			var convertedFieldValue = fieldValue;
			if(me.gantt != null) {
				if(fieldID == me.SYSTEM_FIELDS.STARTDATE || fieldID == me.SYSTEM_FIELDS.ENDDATE ||
						fieldID == me.SYSTEM_FIELDS.TOP_DOWN_STARTDATE || fieldID == me.SYSTEM_FIELDS.TOP_DOWN_ENDDATE) {
					convertedFieldValue = Ext.Date.parse(fieldValue, com.trackplus.TrackplusConfig.DateFormat);
				}
			}
			rowItem.set(fieldName, convertedFieldValue);
			me.grid.getView().refresh();
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
			var retValue = Ext.Date.parse(dateStr, com.trackplus.TrackplusConfig.DateFormat);
//			if(retValue == null) {
//				retValue = new Date(dateStr);
//			}
			return retValue;
		}
	},

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
	}

	/************** ENDS OF HELPRE methods for updating dependents fields **************/
});
