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


Ext.define('com.trackplus.itemNavigator.GanttViewPlugin', {
    extend : 'com.trackplus.itemNavigator.IssueListViewPlugin',
    mixins : {
	    navigable : 'com.trackplus.itemNavigator.TreeNavigableItem'
    },
    view: null,
    includeLongFields: false,
    useArrows: false,
    disableSort: false,
    treeColumnID: null,
    useSelectionModel: false,
    useTree: true,
    readOnly: false,
    dateArrayMinMax: [],
    eventDrag: false,
    workItemDates: [],
    dependencyStoreData: [],
    granularity: "",
    invalidExpandCollapse: false,
    presentedProjectIDs: {},
    dependenciesToRemove: [],

    SYSTEM_FIELDS: {
		START_DATE: 19,
		END_DATE: 20,
		DURATION: 33,
		START_DATE_FIELD: 'f19',
		END_DATE_FIELD: 'f20',
		DURATION_FIELD: 'f33',

		TOP_DOWN_START_DATE: 29,
		TOP_DOWN_END_DATE: 30,
		TOP_DOWN_DURATION: 34,
		TOP_DOWN_START_DATE_FIELD: 'f29',
		TOP_DOWN_END_DATE_FIELD: 'f30',
		TOP_DOWN_DURATION_FIELD: 'f34'
	},

	UNIFORMIZED_FIELD_TYPES: {
		singleSelect: 'SINGLE_SELECT',
		datePicker: 'DATE_PICKER',
		simpleTextField: 'SIMPLE_TEXT_FIELD',
		checkBox: "CHECK_BOX",
		numberEditor: "NUMBER_EDITOR"
	},

    constructor : function(config) {
	    this.callParent(arguments);
	    this.mixins.navigable.constructor.call(this);
	    var me = this;
	    Ext.require([ 'Gnt.plugin.Printable', 'Sch.plugin.TreeCellEditing', 'Gnt.column.StartDate',
	                  'Gnt.field.Calendar',   'Gnt.plugin.Export']);

	    Ext.define('com.trackplus.ganttView.DependencyStore', {
	        extend : 'Gnt.data.DependencyStore',
	        Id : null,
	        projectId : null,
	        issueTypeID : null
	    });

	    Ext.define('com.trackplus.dependency.locale', {
	        override : 'Gnt.feature.DependencyDragDrop',
	        fromText : getText('itemov.ganttView.from') + ': ',
	        toText : getText('itemov.ganttView.to') + ': ',
	        startText : getText('itemov.ganttView.start'),
	        endText : getText('itemov.ganttView.end')
	    });

	    var startDateFieldID = me.SYSTEM_FIELDS.START_DATE_FIELD;
	    var endDateFieldID = me.SYSTEM_FIELDS.END_DATE_FIELD;
	    var durationField = me.SYSTEM_FIELDS.DURATION_FIELD;

	    if(me.model.showBaseline) {
	    	startDateFieldID = me.SYSTEM_FIELDS.TOP_DOWN_START_DATE_FIELD;
		    endDateFieldID = me.SYSTEM_FIELDS.TOP_DOWN_END_DATE_FIELD;
		    durationField = me.SYSTEM_FIELDS.TOP_DOWN_DURATION_FIELD;
	    }
	    Ext.define('com.trackplus.itemNavigator.GanttChartModel', {
	    	extend: 'Gnt.model.Task',
	    	startDateField: startDateFieldID,
	    	endDateField: endDateFieldID,
	    	durationField: durationField,
	    	baselineStartDateField: me.SYSTEM_FIELDS.TOP_DOWN_START_DATE_FIELD,
	    	baselineEndDateField: me.SYSTEM_FIELDS.TOP_DOWN_END_DATE_FIELD,
	    	fields : me.createFields()
	    });
	    me.localizeExportPDFDialog();
    },

    refreshData : function() {
    },

    localizeExportPDFDialog: function() {
    	Ext.define('com.trackplus.exportPDFDialog.locale', {
    		override : 'Sch.widget.ExportDialog',
    		generalError: getText('itemov.ganttView.exportPDF.generalError'),
	        title: getText('itemov.ganttView.exportPDF.title'),
	        formatFieldLabel: getText('itemov.ganttView.exportPDF.formatFieldLabel'),
	        orientationFieldLabel: getText('itemov.ganttView.exportPDF.orientationFieldLabel'),
	        rangeFieldLabel: getText('itemov.ganttView.exportPDF.rangeFieldLabel'),
	        showHeaderLabel: getText('itemov.ganttView.exportPDF.showHeaderLabel'),
	        orientationPortraitText: getText('itemov.ganttView.exportPDF.orientationPortraitText'),
	        orientationLandscapeText: getText('itemov.ganttView.exportPDF.orientationLandscapeText'),
	        completeViewText: getText('itemov.ganttView.exportPDF.completeViewText'),
	        currentViewText: getText('itemov.ganttView.exportPDF.currentViewText'),
	        dateRangeText: getText('itemov.ganttView.exportPDF.dateRangeText'),
	        dateRangeFromText: getText('itemov.ganttView.exportPDF.dateRangeFromText'),
	        pickerText: getText('itemov.ganttView.exportPDF.pickerText'),
	        dateRangeToText: getText('itemov.ganttView.exportPDF.dateRangeToText'),
	        exportButtonText: getText('common.btn.export'),
	        cancelButtonText: getText('common.btn.cancel'),
	        progressBarText: getText('itemov.ganttView.exportPDF.progressBarText'),
	        exportersFieldLabel: getText('itemov.ganttView.exportPDF.exportersFieldLabel'),
	        adjustCols: getText('itemov.ganttView.exportPDF.adjustCols'),
	        adjustColsAndRows: getText('itemov.ganttView.exportPDF.adjustColsAndRows'),
	        specifyDateRange: getText('itemov.ganttView.exportPDF.specifyDateRange')
	    });

    	Ext.define('com.trackplus.exportPDFDialog.locale.SinglePage', {
    		override : 'Sch.plugin.exporter.SinglePage',
    		constructor : function(config) {
    			this.callParent(config);
    			this.name = getText('itemov.ganttView.exportPDF.singlePage');
	        }
       });

    	Ext.define('com.trackplus.exportPDFDialog.locale.MultiPageVertical', {
    		override : 'Sch.plugin.exporter.MultiPageVertical',
    		constructor : function(config) {
    			this.callParent(config);
    			this.name = getText('itemov.ganttView.exportPDF.multiPageVertical');
	        }
       });

    	Ext.define('com.trackplus.exportPDFDialog.locale.MultiPage', {
    		override : 'Sch.plugin.exporter.MultiPage',
    		constructor : function(config) {
    			this.callParent(config);
    			this.name = getText('itemov.ganttView.exportPDF.multiPage');
	        }
       });
    },

    getTreeGrid : function() {
	    return this.view.getView().lockedGrid;
    },

    selectItemByIndex : function(workItemIndex) {
	    var me = this;
	    return me.selectItemByIndexInTree(me.view.getView().lockedGrid, workItemIndex);
    },

    selectItem : function(workItemID) {
	    var me = this;
	    return me.selectItemInTree(me.view.getView().lockedGrid, workItemID);
    },

    deselectItem : function(workItemID) {
	    var me = this;
	    return me.deselectItemInTree(me.view.getView().lockedGrid, workItemID);
    },

    createSettingsPanel: function(btnGroup, btnChooseColumns, btnSaveAsStandardLayout, btnUseStandardLayout, btnSaveAsFilterLayout) {
    	var me = this;
		var items = me.getToolbarLayoutConfigButtons(btnGroup, btnChooseColumns, btnSaveAsStandardLayout, btnUseStandardLayout, btnSaveAsFilterLayout);
		var btnQuarters = me.getToolbarViewPresetsButtons();
		var ganttConfigs = me.getToolbarGanttConfigs();

		var btnGrLayouts = Ext.create('Ext.container.ButtonGroup', {
			items: items,
			frame: false,
			border: '0 0 0 0',
			bodyBorder: false,
			shadow: false,
			baseCls: 'ganttToolbarButtonGroup'
		});

		var btnGrViewPr = Ext.create('Ext.container.ButtonGroup', {
			items: btnQuarters,
			frame: false,
			width: 245,
            baseCls: 'ganttToolbarButtonGroup',
            layout: {
                type: "hbox",
                pack: 'start'
            }
		});

		var btnGrGanttConfig = Ext.create('Ext.container.ButtonGroup', {
			items: ganttConfigs,
			frame: false,
			columns: 4,
			align : 'left',
			margin: '5 0 0 0',
			baseCls: 'ganttToolbarButtonGroup'
		});
		return Ext.create('Ext.toolbar.Toolbar', {
			region:'north',
			height: 75,
			layout: {
				type: 'table',
				columns: 2,
				tableAttrs: {
					style: {
						width: '100%'
				    }
				}
			},
			enableOverflow: true,
			cls: 'toolbarActions',
			border: '0 0 1 0',
			defaults: {
				cls:'toolbarItemAction',
				overCls:'toolbarItemAction-over',
				scale:'small',
				enableToggle:false
			},
			items: [btnGrLayouts, btnGrViewPr, btnGrGanttConfig]
		});
    },

    getToolbarLayoutConfigButtons: function(btnGroup, btnChooseColumns, btnSaveAsStandardLayout, btnUseStandardLayout, btnSaveAsFilterLayout) {
    	var layoutConfigItems = [btnChooseColumns];
	    if (btnSaveAsStandardLayout!=null) {
	    	layoutConfigItems.push(btnSaveAsStandardLayout);
	    }
	    if (btnUseStandardLayout!=null) {
	    	layoutConfigItems.push(btnUseStandardLayout);
	    }
	    if (btnSaveAsFilterLayout!=null) {
	    	layoutConfigItems.push(btnSaveAsFilterLayout);
	    }
	    return layoutConfigItems;
    },

    getToolbarViewPresetsButtons: function() {
    	var me = this;
    	var btns = [];
    	var quart = Ext.create('Ext.button.Button', {
    		text : getText('itemov.ganttView.granularity.quarterly'),
    		id : 'quarterly',
			enableToggle : true,
			toggleGroup : 'granularityGroup',
			pressed : me.view.viewPreset == 'year',
			margin : '0 2 0 0',
			handler : function(btn) {
				me.granularity = "year";
				me.view.switchViewPreset('year', me.calendarStartDate, me.calendarEndDate);
				me.storeGranularityAndZoom();
			}
	    });
    	var month = Ext.create('Ext.button.Button', {
    		text : getText('itemov.ganttView.granularity.monthly'),
			id : 'monthly',
			enableToggle : true,
			toggleGroup : 'granularityGroup',
			pressed : me.view.viewPreset == 'monthAndYear',
			margin : '0 2 0 0',
			handler : function() {
				me.granularity = "monthAndYear";
				me.view.switchViewPreset('monthAndYear', me.calendarStartDate, me.calendarEndDate);
				me.storeGranularityAndZoom();
			}
    	});

	    var week = Ext.create('Ext.button.Button', {
	    	text: getText('itemov.ganttView.granularity.weekly'),
	    	id: 'weekly',
	    	enableToggle : true,
	    	toggleGroup : 'granularityGroup',
			pressed: me.view.viewPreset == 'weekAndMonth',
			margin: '0 2 0 0',
			handler : function(btn) {
				me.granularity = "weekAndMonth";
				me.view.switchViewPreset('weekAndMonth', me.calendarStartDate, me.calendarEndDate);
				me.storeGranularityAndZoom();
			}
	    });

	    var day = Ext.create('Ext.button.Button', {
	    	text : getText('itemov.ganttView.granularity.daily'),
			id : 'daily',
			enableToggle : true,
			toggleGroup : 'granularityGroup',
			pressed : me.view.viewPreset == 'weekAndDayLetter',
			handler : function() {
				me.granularity = "weekAndDayLetter";
				me.view.switchViewPreset('weekAndDayLetter', me.calendarStartDate, me.calendarEndDate);
				me.storeGranularityAndZoom();
			}
	    });

	    var zoomIn = Ext.create('Ext.button.Button', {
	    	iconCls : 'zoomIn',
			margin : '0 0 0 0',
			handler : function() {
				me.view.zoomIn();
			}
        });

        var zoomOut = Ext.create('Ext.button.Button', {
        	iconCls : 'zoomOut',
			margin : '0 0 0 0',
			handler : function(btn) {
				me.view.zoomOut();
			}
        });
	    btns.push(quart);
	    btns.push(month);
	    btns.push(week);
	    btns.push(day);
	    btns.push(zoomIn);
	    btns.push(zoomOut);
	    return btns;
    },

    getToolbarGanttConfigs: function() {
    	var me = this;
    	var components = [];
    	var isActiveTopDownDate = me.model.isActiveTopDownDate;
	    var showBaseline = me.model.showBaseline;
	    var showBoth = me.model.showBoth;

	    var isCheckedShowBaseline = false;
	    var isCheckedShowBoth = false;

	    var isDisabledShowBaseline = false;
	    var isDisabledShowBoth = false;

	    if (isActiveTopDownDate) {
		    if (showBaseline) {
			    isCheckedShowBaseline = true;
			    isDisabledShowBoth = true;
			    isDisabledShowBaseline = false;
		    }
		    if (showBoth) {
			    isCheckedShowBoth = true;
			    isDisabledShowBaseline = true;
			    isDisabledShowBoth = false;
		    }

	    } else {
		    if (showBaseline) {
			    isCheckedShowBaseline = true;
		    }
		    if (showBoth) {
			    isCheckedShowBoth = true;
		    }
		    isDisabledShowBaseline = true;
		    isDisabledShowBoth = true;
	    }

	    var validateRelationshipsFlag = me.model.validateRelationships;
	    var highlightCriticalPathFlag = me.model.highlightCriticalPath;

	    var highlightCp = Ext.create('Ext.form.field.Checkbox', {
            boxLabel : getText('itemov.ganttView.dependency.highlightCp'),
            margin : '0 12 0 0',
            checked : highlightCriticalPathFlag,
            handler : function(checkBox) {
	            var v = me.view.getSchedulingView();
	            if (checkBox.checked) {
		            v.highlightCriticalPaths(true);
	            } else {
		            v.unhighlightCriticalPaths(true);
	            }
	            var params = new Object();
	            params.ganttConfigValue = checkBox.checked;
	            params.ganttConfigNameToSave = 'highlightCriticalPath';
	            me.saveGanttSpecifigConfig(params, false);
            }
        });

	    me.showBaseline = Ext.create('Ext.form.field.Checkbox', {
            boxLabel : getText('itemov.ganttView.showBaseline'),
            id : 'showBaselineCh',
            itemID : 'showBaselineCh',
            name : 'showBaselineCh',
            margin : '0 12 0 0',
            checked : isCheckedShowBaseline,
            disabled : isDisabledShowBaseline,
            enabled : false,
            handler : function(checkBox) {
            	if(me.isChartContainsUnsavedItems()) {
            		me.showSaveDialogWarningIfChartContainsUnsavedItems(me, me.showBaselineCheckBoxHandler, me.showBaselineCheckBoxHandler, null);
            	}else{
            		me.showBaselineCheckBoxHandler();
            	}

            }
        });

	    me.showBoth = Ext.create('Ext.form.field.Checkbox', {
            boxLabel : getText('itemov.ganttView.showBoth'),
            id : 'showBothCh',
            margin : '0 12 0 0',
            checked : isCheckedShowBoth,
            disabled : isDisabledShowBoth,
            handler : function(checkBox) {
	            if (checkBox.checked) {
		            me.view.el.toggleCls('sch-ganttpanel-showbaseline');
		            me.showBaseline.setDisabled(true);
	            } else {
		            me.view.el.toggleCls('sch-ganttpanel-showbaseline');
		            me.showBaseline.setDisabled(false);
	            }
	            var params = new Object();
	            params.ganttConfigValue = checkBox.checked;
	            params.ganttConfigNameToSave = 'showBoth';
	            me.saveGanttSpecifigConfig(params, false);
            }
        });

	    var validate = Ext.create('Ext.form.field.Checkbox', {
            boxLabel : getText('itemov.ganttView.validate'),
            margin : '0 12 0 0',
            checked : validateRelationshipsFlag,
            handler : function(checkBox) {
	            if (checkBox.checked) {
		            me.validateDependencies();
		            me.model.validateRelationships = true;
	            } else {
		            me.removeClsFromValidatedTasks();
		            me.model.validateRelationships = false;
	            }
	            var params = new Object();
	            params.ganttConfigValue = checkBox.checked;
	            params.ganttConfigNameToSave = 'validateRelationships';
	            me.saveGanttSpecifigConfig(params, false);
            }
        });

	    var search = Ext.create('Ext.form.field.Text', {
            emptyText : getText('itemov.ganttView.searchForTask'),
            width : 175,
            enableKeyEvents : true,
            colspan: 2,
            listeners : {
                keyup : {
                    fn : function(field, e) {
	                    var value = field.getValue();
	                    var regexp = new RegExp(Ext.String.escapeRegex(value), 'i');
	                    if (value) {
//		                    me.view.taskStore.filterTreeBy(function(task) {
//			                    return regexp.test(task.get('Name'));
//		                    });
	                    	me.view.taskStore.filterTreeBy({
	                    	    filter: function (task) { return regexp.test(task.get('f17'));},
	                    	    checkParents: true
	                    	});

	                    } else {
		                    me.view.taskStore.clearTreeFilter();
	                    }
                    },
                    buffer : 300,
                    scope : this
                },
                specialkey : {
	                fn : function(field, e) {
		                if (e.getKey() === e.ESC) {
			                field.reset();
			                me.view.taskStore.clearTreeFilter();
		                }
	                }
                }
            }
        });

	    me.normalizeTasks = Ext.create('Ext.button.Button', {
	    	margin : '-4 0 0 0',
			text: getText('itemov.ganttView.normalizeTasks'),
			iconCls : 'normalizeTasks',
			handler : function(btn) {
				me.view.getTaskStore().renormalizeTasks();
				if (me.model.validateRelationships) {
				    me.validateDependencies();
			    }
			}
	     });

	    components.push(highlightCp);
	    components.push(me.showBaseline);
	    components.push(search);
	    components.push(validate);
	    components.push(me.showBoth);
	    components.push(me.normalizeTasks);

	    return components;
    },

    showBaselineCheckBoxHandler: function() {
    	var me = this;
        if (me.showBaseline) {
        	me.showBoth.setDisabled(true);
        } else {
        	me.showBoth.setDisabled(false);
        }
        var params = new Object();
        params.ganttConfigValue = me.showBaseline.checked;
        params.ganttConfigNameToSave = 'showBaseline';
        me.saveGanttSpecifigConfig(params, true);
    },

    isShowBaseline: function() {
    	var me = this;
    	return me.model.showBaseline;
    },

    saveGanttSpecifigConfig : function(params, refreshAfterSuccess) {
	    var me = this;
	    Ext.Ajax.request({
	        url : "itemNavigator!saveGanttSpecificConfigs.action",
	        disableCaching : true,
	        success : function() {
		        me.view.setLoading(false);
		        if(refreshAfterSuccess) {
		        	me.fireEvent.call(me, 'datachange');
		        }
	        },
	        failure : function() {
		        me.view.setLoading(false);
		        alert("failure");
	        },
	        method : 'POST',
	        params : params
	    });
    },

    getSettingsHeight : function() {
	    return 67;
    },

    initNorthPanel : function() {
	    var me = this;
	    var items = new Array();
	    items.push({
	        xtype : 'fieldcontainer',
	        margin : '5 5 5 5',
	        fieldLabel : getText('itemov.lbl.sortBy'),
	        labelAlign : 'right',
	        labelWidth : 100,
	        layout : 'hbox',
	        items : [ me.cmbSortBy, me.radioAsc, me.radioDsc ]
	    });
	    me.northPanel = Ext.create('Ext.panel.Panel', {
	        region : 'north',
	        layout : {
		        type : 'column'
	        },
	        border : false,
	        bodyBorder : false,
	        // hidden:!me.settingsVisible,
	        items : items
	    });
    },

    getParentFromID : function(parentArray, id) {
	    for (aParent in parentArray) {
		    if (parentArray[aParent].Id == id) {
			    return aParent;
		    }
	    }
	    return -1;
    },

    getSelectedIssues : function() {
	    var me = this;
	    var issueIds = [];
	    if (me.model.layout.bulkEdit) {
		    var selections = me.view.getSelectionModel().getSelection();
		    if (selections != null && selections.length > 0) {
			    for (var i = 0; i < selections.length; i++) {
				    issueIds.push(selections[i].data.workItemID);
			    }
		    }
	    }
	    return issueIds;
    },

    myComparator : function(a, b) {
	    return parseFloat(a.f27) > parseFloat(b.f27);
    },

    appendNewDependency : function(oneIssue) {
	    var me = this;
	    if (oneIssue.linksTo != "") {
		    var linksStoArray = oneIssue.linksTo.split(",");
		    for (index in linksStoArray) {
			    var workItemToId_dependencyId = linksStoArray[index].split("/");
			    var oneDependency = new Object();
			    oneDependency.From = oneIssue.workItemID;
			    if (workItemToId_dependencyId[0] != null) {
				    oneDependency.To = workItemToId_dependencyId[0];
			    }
			    if (workItemToId_dependencyId[1] != null) {
				    oneDependency.Id = workItemToId_dependencyId[1];
			    }
			    if (workItemToId_dependencyId[2] != null) {
				    oneDependency.Type = workItemToId_dependencyId[2];
			    }

			    if (workItemToId_dependencyId[3] != null && workItemToId_dependencyId[4] != null ) {
				    var bryntumLagFormat = me.convertTrackLagUnitToBryntum(parseInt(workItemToId_dependencyId[4]));
				    if (bryntumLagFormat != -1) {
					    oneDependency.Lag = workItemToId_dependencyId[3];
					    oneDependency.LagUnit = bryntumLagFormat;
				    }
			    }
			    oneDependency.projectId = oneIssue.projectID;
			    oneDependency.issueTypeID = oneIssue.issueTypeID;
			    me.dependencyStoreData.push(oneDependency);
		    }
	    }
    },

    convertTrackLagUnitToBryntum : function(trackLag) {
	    switch (trackLag) {
	    case 7:
	    case 8:
		    return 'd';
	    case 9:
	    case 10:
		    return 'w';
	    case 11:
	    case 12:
		    return 'mo';
	    default:
		    return -1;
	    }
    },

    parseJSON: function(issuesTree) {
	    var me = this;
	    if (issuesTree == null || issuesTree.length == 0) {
		    return;
	    }
	    var issue = null;
	    for (var i = 0; i < issuesTree.length; i++) {
		    issue = issuesTree[i];
		    if (issue.group == true) {
			    this.parseJSON(issue.children);
		    } else {

		    	if (issue.f19) {
		    		var dtTmp = Ext.Date.parse(issue.f19, com.trackplus.TrackplusConfig.DateFormat);
		    		me.dateArrayMinMax.push(new Date(Ext.util.Format.date(dtTmp, "Y-m-d")));
		    	}
		    	if (issue.f20) {
		    		var dtTmp = Ext.Date.parse(issue.f20, com.trackplus.TrackplusConfig.DateFormat);
		    		me.dateArrayMinMax.push(new Date(Ext.util.Format.date(dtTmp, "Y-m-d")));
		    	}

		    	if (issue.f29) {
		    		var dtTmp = Ext.Date.parse(issue.f29, com.trackplus.TrackplusConfig.DateFormat);
		    		me.dateArrayMinMax.push(new Date(Ext.util.Format.date(dtTmp, "Y-m-d")));
		    	}
		    	if (issue.f30) {
		    		var dtTmp = Ext.Date.parse(issue.f30, com.trackplus.TrackplusConfig.DateFormat);
		    		me.dateArrayMinMax.push(new Date(Ext.util.Format.date(dtTmp, "Y-m-d")));
		    	}
			    me.appendNewDependency(issue);
			    if (issue.calendarStartDateMissing == "true") {
				    issue.cls = 'item-calendarStartDateMissing';
			    }
			    if (issue.children != null) {
				    this.parseJSON(issue.children);
			    }
		    }
	    }
    },

    initMyListeners : function() {
	    var me = this;
		me.view.on('boxready', function(thisGrid) {
			thisGrid.view.focus = Ext.emptyFn;
		});
		me.view.getView().lockedView.addListener('beforedrop', me.onGridBeforeDrop, me);
		me.view.addListener('itemcontextmenu', me.onGridItemContextMenu, me);
		me.view.addListener('celldblclick', me.onGridItemDblClick, me);
		if (me.model.layout.bulkEdit) {
			me.view.getSelectionModel().addListener('selectionchange', me.onGridSelectionChange, me);
		}
	    me.view.getView().lockedGrid.addListener('afterrender', me.onGridAfterRenderer, me);
	    me.view.getView().lockedGrid.addListener('columnresize', me.columnResizeHandler, me);
	    me.view.getView().lockedGrid.addListener('columnmove', me.columnMoveHandler, me);
	    me.view.getView().lockedGrid.addListener('afteritemexpand', me.afterItemExpandHandler, me);
	    me.view.getView().lockedGrid.addListener('afteritemcollapse', me.afterItemCollapseHandler, me);
	    if (me.model.layout.bulkEdit) {
		    me.view.getSelectionModel().addListener('selectionchange', me.onGridSelectionChange, me);
	    }

    },

    onGridBeforeDrop : function(node, data, overModel, dropPosition) {
	    var me = this;
	    var targetProjectID = overModel.data.projectID;
	    var validWorkItems = [];
	    var otherProjectItems = [];
	    for (var i = 0; i < data.records.length; i++) {
		    var record = data.records[i];
		    if (record.data['group'] == true) {
			    continue;
		    }
		    var sourceProjectID = record.data['projectID'];
		    if (sourceProjectID != targetProjectID) {
			    otherProjectItems.push(record);
		    } else {
			    validWorkItems.push(record);
		    }
	    }
	    if (validWorkItems.length == 0) {
		    var msg = getText('itemov.wbsView.err.notTheSameProject');
		    Ext.MessageBox.show({
		        title : '',
		        msg : msg,
		        width : 200,
		        buttons : Ext.MessageBox.OK,
		        icon : Ext.MessageBox.ERROR
		    });
		    return false;
	    }
	    while (data.records.length > 0) {
		    data.records.pop();
	    }
	    me.view.setLoading(true);
	    var workItems = '';
	    for (i = 0; i < validWorkItems.length; i++) {
		    data.records.unshift(validWorkItems[i]);
		    workItems += validWorkItems[i].data['workItemID'];
		    if (i < validWorkItems.length - 1) {
			    workItems += ",";
		    }
	    }
	    var before = (dropPosition == "before");
	    if (!me.WBS_SORT_ASCENDING) {
		    before = !before;
	    }
	    var params = {
	        workItems : workItems,
	        targetWorkItemID : overModel.data['workItemID'],
	        before : before
	    };
	    Ext.Ajax.request({
	        url : "itemNavigator!changeWBS.action",
	        disableCaching : true,
	        success : function() {
		        me.view.setLoading(false);
		        me.fireEvent.call(me, 'datachange');
	        },
	        failure : function() {
		        me.view.setLoading(false);
		        alert("failure");
	        },
	        method : 'POST',
	        params : params
	    });
	    return true;
    },

    onGridSelectionChange : function(sm, selections) {
	    var me = this;
	    me.fireEvent.call(me, 'selectionchange', selections);
	    return false;
    },

    onGridAfterRenderer : function() {
	    var me = this;
	    var rowExpandAllTrgEl = Ext.get('rowExpandAllTrg');
	    if (rowExpandAllTrgEl != null) {
		    rowExpandAllTrgEl.addListener('click', me.rowExpandAll, me);
	    }
	    var rowCollapseAllTrgEl = Ext.get('rowCollapseAllTrg');
	    if (rowCollapseAllTrgEl != null) {
		    rowCollapseAllTrgEl.addListener('click', me.rowCollapseAll, me);
	    }
	    me.onReadyHandler();
    },

    onReadyHandler: function() {
		var me = this;
		borderLayout.onPageMouseDown = function(event) {
			if(!me.clickIsOutOfGrid(event)) {
				return true;
			}
			if(me.isChartContainsUnsavedItems()) {
				event.stopEvent();
				me.showSaveDialogWarningIfChartContainsUnsavedItems(null, null, null, null);
			}else {
				return true;
			}
		};
	},

	showSaveDialogWarningIfChartContainsUnsavedItems: function(scope, methodToExecuteAfterYes, methodToExecuteAfterNo, methodToExecuteAfterYesParams) {
		var me = this;
		if(me.isChartContainsUnsavedItems()) {
			Ext.MessageBox.show({
				title:getText('common.warning'),
				msg: getText('item.action.leavingInlineEdit'),
				buttons: Ext.MessageBox.YESNO,
				modal: true,
				fn: function(btn){
					if(btn=="yes"){
						me.saveChanges2(scope, methodToExecuteAfterYes, methodToExecuteAfterYesParams, false);
					}
					if(btn=="no"){
						me.fireEvent.call(me, 'datachange');
						if(methodToExecuteAfterNo != null) {
							methodToExecuteAfterNo.call(me);
						}
					}
				},
				icon: Ext.MessageBox.QUESTION
			});
		}
	},

	showSaveErrorsConfirmDialog: function(msg) {
		var me = this;
		var problemDef = getText('itemov.ganttView.saveTasksErrors');
		var question = getText('itemov.ganttView.saveTasksErrorsQuestion');
		var msgToShow = problemDef + '<br>' + msg + question;
		Ext.MessageBox.show({
			title:getText('common.warning'),
			msg: msgToShow,
			buttons: Ext.MessageBox.OKCANCEL,
			modal: true,
			fn: function(btn) {
				if(btn=="ok") {
					me.saveChanges2(null, null, null, false);
				}
//				if(btn=="cancel") {
//				}
			},

			icon: Ext.MessageBox.QUESTION
		});
	},

	isChartContainsUnsavedItems: function() {
		var me = this;
		var isNewlyCreatedLink = false;
    	var store = me.view.getDependencyStore();
    	var items = store.data.items;
    	for(var ind in items) {
    		var depItem = items[ind];
    		if(depItem != null) {
	    		if(depItem.dirty) {
	    			isNewlyCreatedLink = true;
	    		}
    		}
    	}
    	var modifiedRecords = me.view.getTaskStore().getUpdatedRecords();
    	if(modifiedRecords.length > 0 || me.dependenciesToRemove.length > 0 || isNewlyCreatedLink) {
    		return true;
    	}
    	return false;
	},

	isNewSelectedTask: function() {
		var me = this;
		var selectedData = me.getTreeGrid().getSelectionModel().getSelection();
		if(selectedData.length > 0) {
			var selection = selectedData[0];
			if(selection.data.Id < 0) {
				return true;
			}
		}
		return false;
	},

	clickIsOutOfGrid: function(event) {
		var me = this;
		var isOut = true;
		clickX = event.getX();
		clickY = event.getY();
		var gridX = me.view.getX();
//		var gridY = me.grid.getY();
		var toolbarY = borderLayout.getActiveToolbarList().getY();
		if(clickX > gridX && clickY > toolbarY) {
			isOut = false;
		}
		return isOut;
	},

    addSaveButton: function(toolbar) {
    	var me = this;
	    var save = Ext.create('Ext.button.Button', {
	    	itemId: 'ganttSaveBtn',
	    	id: 'ganttSaveBtn',
	    	margin : '0 0 0 0',
			text: getText('common.btn.save'),
			iconCls : 'save',
			handler : function(btn) {
				me.saveChanges2(null, null, null, true);
			}
	    });
	    if(toolbar != null) {
	    	toolbar.add(save);
	    }else {
	    	borderLayout.getActiveToolbarList().add(save);
	    }
    },

    onGridItemContextMenu : function(gridView, record, item, index, event, opts) {
	    var me = this;
	    event.stopEvent();
	    me.fireEvent('itemcontextmenu', record.data, event, me.view, index, record);
	    return false;
    },

    getPopupMenuItems : function(rowData, grid, index, record) {
	    var me = this;
	    var items = [];
	    if (rowData.editable) {
		    items.push({
		        text : getText('common.btn.indent'),
		        iconCls : 'itemAction_indent16',
		        handler : function() {
			        me.indent.call(me, rowData, grid, index, record);
		        }
		    });
		    items.push({
		        text : getText('common.btn.outdent'),
		        iconCls : 'itemAction_outdent16',
		        handler : function() {
			        me.outdent.call(me, rowData, grid, index, record);
		        }
		    });
	    }
	    return items;
    },

    indent : function(rowData, grid, index, record) {
	    var me = this;
	    var parentNode = record.parentNode;
	    var recordIndex = parentNode.indexOf(record);
	    if (recordIndex === 0) {
		    alert(getText('itemov.wbsView.err.noIndentPossible'));
		    return false;
	    }
	    var supNode = parentNode.getChildAt(recordIndex - 1);
	    grid.setLoading(true);
	    var params = {
	        nodeType : me.itemNavigatorController.nodeType,
	        nodeObjectID : me.itemNavigatorController.nodeObjectID,
	        descriptorID : me.itemNavigatorController.selectedIssueViewDescriptor.id,
	        queryType : me.itemNavigatorController.model.queryType,
	        queryID : me.itemNavigatorController.model.queryID,
	        workItemID : record.data['workItemID'],
	        targetWorkItemID : supNode.data['workItemID']
	    };
	    Ext.Ajax.request({
	        url : "itemNavigator!indent.action",
	        disableCaching : true,
	        success : function() {
		        parentNode.removeChild(record);
		        supNode.data['leaf'] = false;
		        supNode.appendChild(record);
		        supNode.expand();
		        grid.setLoading(false);
		        me.fireEvent.call(me, 'datachange');
		        grid.setLoading(false);

	        },
	        failure : function() {
		        grid.setLoading(false);
		        alert("failure");
	        },
	        method : 'POST',
	        params : params
	    });
    },

    outdent : function(rowData, grid, index, record) {
	    var me = this;
	    var parentNode = record.parentNode;
	    var rootNode = grid.store.getRootNode();
	    if (parentNode == rootNode) {
		    CWHF.showMsgError(getText("itemov.wbsView.err.noOutdentPossible"));
		    return false;
	    }
	    var parentIsGroup = parentNode.data['group'];
	    if (parentIsGroup == true) {
		    CWHF.showMsgError(getText("itemov.wbsView.err.noOutdentPossible"));
		    return false;
	    }
	    grid.setLoading(true);
	    var params = {
	        nodeType : me.itemNavigatorController.nodeType,
	        nodeObjectID : me.itemNavigatorController.nodeObjectID,
	        descriptorID : me.itemNavigatorController.selectedIssueViewDescriptor.id,
	        queryContextID : me.itemNavigatorController.model.queryContext.id,
	        workItemID : record.data['workItemID']
	    };
	    Ext.Ajax.request({
	        url : "itemNavigator!outdent.action",
	        disableCaching : true,
	        success : function() {
		        var grandParent = parentNode.parentNode;
		        var recordIndex = grandParent.indexOf(parentNode);
		        grandParent.insertChild(recordIndex + 1, record);
		        if (!parentNode.hasChildNodes()) {
			        parentNode.data['leaf'] = false;
		        }
		        grid.setLoading(false);
		        me.fireEvent.call(me, 'datachange');
	        },
	        failure : function() {
		        grid.setLoading(false);
		        alert("failure");
	        },
	        method : 'POST',
	        params : params
	    });
    },

    onGridItemDblClick : function(view, td, cellIndex, record, tr, rowIndex, e) {
	    var me = this;
	    e.stopEvent();
	    if (me.model.layout.bulkEdit == true && cellIndex == 0) {
		    return false;
	    }
	    me.fireEvent.call(me, 'itemdblclick', record.data, td);
	    return false;
    },

    expandCollapseItem : function(node, expanded) {
	    var me = this;
	    var id = node.data.Id;
	    if ((id + '').indexOf('g') !== -1) {
		    // group
		    me.expandGroup(id.substring(1), expanded);
	    } else {
		    // item
		    me.expandItem(id, expanded);
	    }

    },

    expandGroup : function(groupID, expanded) {
	    Ext.Ajax.request({
	        url : 'reportExpand!expandGroup.action',
	        params : {
	            expanded : expanded,
	            groupID : groupID
	        }
	    });
    },

    expandItem : function(workItemID, expanded) {
	    Ext.Ajax.request({
	        url : 'reportExpand!expandReportBean.action',
	        params : {
	            expanded : expanded,
	            workItemID : workItemID
	        }
	    });
    },

    afterItemExpandHandler : function(node, index, item) {
	    var me = this;
	    if (me.invalidExpandCollapse) {
		    return;
	    }
	    me.expandCollapseItem(node, true);
    },

    afterItemCollapseHandler : function(node, index, item) {
	    var me = this;
	    if (me.invalidExpandCollapse) {
		    return;
	    }
	    me.expandCollapseItem(node, false);
    },

    validateExpandCollapse : function(node) {
	    var me = this;
	    Ext.Function.defer(function() {
		    me.invalidExpandCollapse = false;
	    }, 3000, me);
    },

    rowExpandAll : function(e) {
	    var me = this;
	    e.stopEvent();
	    me.invalidExpandCollapse = true;
	    me.view.expandAll();
	    Ext.Ajax.request({
		    url : "reportExpand!expandAll.action"
	    });
	    return false;
    },

    rowCollapseAll : function(e) {
	    var me = this;
	    e.stopEvent();
	    me.invalidExpandCollapse = true;
	    me.view.collapseAll();
	    Ext.Ajax.request({
		    url : "reportExpand!collapseAll.action"
	    });
	    return false;
    },

    columnResizeHandler : function(ct, column, width) {
	    var me = this;
	    var urlStr = "layoutColumns!resizeColumn.action";
	    var layoutID = column.getItemId().substring(2);
	    Ext.Ajax.request({
	        url : urlStr,
	        disableCaching : true,
	        success : function(result) {
		        me.fireEvent.call(me, 'layoutchange');
	        },
	        failure : function() {
	        },
	        method : 'POST',
	        params : {
	            layoutID : layoutID,
	            fieldID : column.fieldID,
	            width : width,
	            filterType : me.model.queryContext.queryType,
	            filterID : me.model.queryContext.queryID
	        }
	    });
    },

    createFields: function() {
	    var me = this;
	    var fields = me.callParent();
	    fields.push({
	        name : 'startDateEditFlag',
	        type : 'string'
	    });
	    fields.push({
	        name : 'endDateEditFlag',
	        type : 'string'
	    });
	    fields.push({
	        name : 'linkable',
	        type : 'boolean'
	    });
	    fields.push({
	        name : 'status',
	        type : 'string'
	    });
	    fields.push({
	        name : 'wbs',
	        type : 'string'
	    });
	    fields.push({
	        name : 'responsibleValue',
	        type : 'string'
	    });
	    fields.push({
	        name : 'PercentDoneToolTip',
	        type : 'string'
	    });
	    fields.push({
		    name : 'iconCls'
	    });
	    fields.push({
		    name : 'cssColorClass'
	    });
	    fields.push({
		    name : 'cssColorClassGroup'
	    });
	    fields.push({
		    name : 'queryFieldCSS'
	    });
	    fields.push({
		    name : 'extendedItem'
	    });

	    fields.push({
		    name : 'workItemIDAbove'
	    });
	    fields.push({
		    name : 'group',
		    type: 'boolean'
	    });

	    var presentedFieldsFromObligatoryFields = {};
	    for(var ind in fields) {
	    	if(fields[ind].name == me.SYSTEM_FIELDS.START_DATE_FIELD || fields[ind].name == me.SYSTEM_FIELDS.END_DATE_FIELD ||
	    			fields[ind].name == me.SYSTEM_FIELDS.TOP_DOWN_START_DATE_FIELD || fields[ind].name == me.SYSTEM_FIELDS.TOP_DOWN_END_DATE_FIELD) {
	    		fields[ind].type = 'date';
	    		fields[ind].dateFormat  = com.trackplus.TrackplusConfig.DateFormat;
	    		var name = fields[ind].name;
	    		presentedFieldsFromObligatoryFields[name] = true;
	    	}
	    	if(fields[ind].name == me.SYSTEM_FIELDS.DURATION_FIELD ||
	    			fields[ind].name ==  me.SYSTEM_FIELDS.TOP_DOWN_DURATION_FIELD) {
	    		fields[ind].type = 'number';
	    	}
	    }
	    if(presentedFieldsFromObligatoryFields[me.SYSTEM_FIELDS.START_DATE_FIELD] == undefined) {
	    	fields.push(me.createNewField(me.SYSTEM_FIELDS.START_DATE_FIELD));
	    }
	    if(presentedFieldsFromObligatoryFields[me.SYSTEM_FIELDS.END_DATE_FIELD] == undefined) {
	    	fields.push(me.createNewField(me.SYSTEM_FIELDS.END_DATE_FIELD));
	    }

	    if(presentedFieldsFromObligatoryFields[me.SYSTEM_FIELDS.TOP_DOWN_START_DATE_FIELD] == undefined) {
	    	fields.push(me.createNewField(me.SYSTEM_FIELDS.TOP_DOWN_START_DATE_FIELD));
	    }

	    if(presentedFieldsFromObligatoryFields[me.SYSTEM_FIELDS.TOP_DOWN_END_DATE_FIELD] == undefined) {
	    	fields.push(me.createNewField( me.SYSTEM_FIELDS.TOP_DOWN_END_DATE_FIELD));
	    }
	    return fields;
    },

    createNewField: function(fieldName) {
    	var newField = {};
    	newField.name = fieldName;
    	newField.type = 'date';
    	newField.useNull = 'true';
    	newField.dateFormat = com.trackplus.TrackplusConfig.DateFormat;
    	return newField;
    },

    dateSortAsc: function (date1, date2) {
    	if (date1 > date2) return 1;
    	if (date1 < date2) return -1;
    	return 0;
    },

    setCalendarStartEndDate : function() {
	    var me = this;
	    if (me.dateArrayMinMax.length == 0) {
		    me.calendarStartDate = new Date();
		    me.calendarStartDate.setDate(me.calendarStartDate.getDate() - 7);
		    me.calendarEndDate = new Date();
		    me.calendarEndDate.setDate(me.calendarEndDate.getDate() + 14);
	    } else if (me.dateArrayMinMax.length == 1) {
		    me.calendarStartDate = me.dateArrayMinMax[0];
		    me.calendarStartDate.setDate(me.calendarStartDate.getDate() - 7);
		    me.calendarEndDate = me.dateArrayMinMax[0];
		    me.calendarEndDate.setDate(me.calendarEndDate.getDate() + 14);
	    } else {
	    	me.dateArrayMinMax.sort(me.dateSortAsc);
	    	me.calendarStartDate = me.dateArrayMinMax[0];
		    me.calendarEndDate = me.dateArrayMinMax[me.dateArrayMinMax.length - 1];
		    me.calendarStartDate.setDate(me.calendarStartDate.getDate() - 7);
		    me.calendarEndDate.setDate(me.calendarEndDate.getDate() + 14);
	    }
    },

    createColumns: function(empty) {
	    var me = this;
	    var shortFields = me.model.layout.shortFields;
	    me.columns = new Array(0);
	    var layoutData;
	    if (me.model.layout.indexNumber == true) {
		    var count = me.model.totalCount;
		    var size = 0;
		    if (count == null || count == 0) {
			    size = 21;
		    } else {
			    size = (count + "").length * 5 + 17;
		    }
		    me.columns.push({
		        draggable : false,
		        menuDisabled : true,
		        width : size,
		        dataIndex : 'workItemIndex',
		        sortable : false,
		        align : 'right',
		        tdCls : 'simpleTreeGridCell',
		        resizable : false
		    });
	    }
	    for (var i = 0; i < shortFields.length; i++) {
		    layoutData = shortFields[i];
		    var sortable = !me.disableSort;// &&layoutData.sortable;
		    var col = me.createColumn(i, layoutData, sortable, me.useTree);
		    if(i > 0) {
			    me.cellInlineCtr.setColumnEditor(shortFields[i], col);
			    var uniformizedFieldType = me.getUniformizedFieldType(layoutData.extJsRendererClass);
			    if(uniformizedFieldType != null) {
			    	var renderer = me.getRendererForSpecificColumns(uniformizedFieldType, i);
			    	if(renderer != null) {
			    		col.renderer = renderer;
			    	}
			    }
		    }else {
		    	   var uniformizedFieldType = me.getUniformizedFieldType(layoutData.extJsRendererClass);
		    	   if(uniformizedFieldType != null) {
				    	if(uniformizedFieldType == me.UNIFORMIZED_FIELD_TYPES.datePicker) {
				    		var renderer = me.getRendererForSpecificColumns(uniformizedFieldType, i);
					    	if(renderer != null) {
					    		col.renderer = renderer;
					    	}
				    	}
				    }
		    }
		    me.columns.push(col);
	    }
	    me.columns = me.columns.reverse();
    },

	getRendererForSpecificColumns: function(uniformizedFieldType, columnNr) {
		var me = this;
		var renderer = null;

	    if(uniformizedFieldType == me.UNIFORMIZED_FIELD_TYPES.datePicker) {
	    	renderer = function (value, metaData, record, row, col, store, gridView) {
	    		var isProject = false;
    			if(record.data.group != null) {
    				isProject = record.data.group;
    			}
    			if(!isProject) {
		    		var dateObj = null;
		    		if(value instanceof Date) {
		    			dateObj = new Date(value.getTime());
		    		}else {
		    			dateObj = Ext.Date.parse(value, com.trackplus.TrackplusConfig.DateFormat);
		    		}
		    		if(dateObj != null) {
		    			if(!isProject) {
		    				return Ext.util.Format.date(dateObj, com.trackplus.TrackplusConfig.DateFormat);
		    			}
		    		}
    			}else {
	    			if(columnNr == 0 ) {
	    				return record.data['Name'];
	    			}else {
	    				return "";
	    			}
	    		}
	    	};
	    }
	    if(uniformizedFieldType == me.UNIFORMIZED_FIELD_TYPES.singleSelect) {
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
	    if(uniformizedFieldType == me.UNIFORMIZED_FIELD_TYPES.checkBox) {
	    	renderer = function (val, metaData) {
	    		var checkBox = metaData.column.getEditor();
	    		if (val == 'true' || val == 'false') {
	    			if(checkBox.checked) {
	    				return getText('common.boolean.Y');
	    			}else {
	    				return getText('common.boolean.N');
	    			}
	    		}else {
	    			return val;
	    		}
	    	};
	    }

	    if(uniformizedFieldType == me.UNIFORMIZED_FIELD_TYPES.numberEditor) {
	    	renderer = function (value, metaData, record, row, col, store, gridView) {
	    		var isProject = false;
	    		if(record.data.group != null) {
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

    beforeTaskDropEvent: function(taskStore) {
	    var me = this;
	    var title = getText("itemov.ganttView.task.permissionErrorTitle");
	    var msg = getText("itemov.ganttView.task.editPermissionErrorMsg");
	    if (!me.eventDrag) {
		    if (taskStore.data.startDateEditFlag && taskStore.data.endDateEditFlag) {
			    return true;
		    } else {
			    me.showSimpleErrorMessage(msg, title);
			    return false;
		    }
	    } else {
		    return false;
	    }
    },

    afterTaskDropEvent : function(taskStore) {
    	var me = this;
    	Ext.defer(me.doInitAfterEverythingLoadedAndRendered, 150, me);
    	if (me.model.validateRelationships) {
		    me.validateDependencies();
	    }
    },

    showErrorMessage : function(msg, title, taskStore, workItemID, startDate, endDate, wrongDependencies) {
	    var me = this;
    },

    showSimpleErrorMessage: function(msg, title) {
	    var me = this;
	    Ext.Msg.show({
	        title : title,
	        msg : msg,
	        icon : Ext.Msg.ERROR,
	        buttons : Ext.Msg.OK
	    });
    },

    beforeTaskResize : function(taskStore, title) {
	    var me = this;
	    me.eventDrag = true;
	    return true;
    },

    afterTaskResize: function(taskStore) {
    	var me = this;
    	Ext.defer(me.doInitAfterEverythingLoadedAndRendered, 150, me);
    	if (me.model.validateRelationships) {
		    me.validateDependencies();
	    }

    },

    defineStartEndListenerScheduleView : function() {
    },

    adjustNewlyCreatedLink: function(params) {
    	var me = this;
    	var store = me.view.getDependencyStore();
    	var newItem = store.last();
    	var sourceID = newItem.getSourceId();
    	var targetID = newItem.getTargetId();
    	newItem.setLag(params.lag);
    	newItem.setLagUnit(me.convertTrackLagUnitToBryntum(params.lagUnit));
    	newItem.extraParamsToSubmit = params;
    	newItem.extraParamsToSubmit.sourceID = sourceID;
    	newItem.extraParamsToSubmit.targetID = targetID;
    	newItem.data.Id = Math.floor((Math.random() * 10) + 1000) * -1;
    },

    removeLastAddedDependency: function() {
    	var me = this;
    	var depStore = me.view.getDependencyStore();
    	depStore.removeAt(depStore.data.items.length - 1);
    },

    beforeDependencyDragDrop : function(dependencyStore, taskStore) {
	    var me = this;
    },

    afterDependencyDragDrop: function(dependencyStore, taskStore) {
	    var me = this;
	    if(dependencyStore.data.items.length > me.depStoreSize) {
		    var from = dependencyStore.last().data.From;
		    var to = dependencyStore.last().data.To;
		    var dependencyType = dependencyStore.last().data.Type;
		    var fromTask = me.view.taskStore.getById(from);
		    var toTask = me.view.taskStore.getById(to);
		    if(fromTask != null && toTask != null) {
		    	var fromIsEditable = fromTask.data.editable;
		    	var toTaskEditable = toTask.data.editable;
		    	if(fromIsEditable != null && toTaskEditable != null) {
		    		if(!fromIsEditable || !toTaskEditable) {
		    			var title = getText("itemov.ganttView.task.permissionErrorTitle");
		    			var msg = getText("itemov.ganttView.task.editPermissionErrorMsg");
		    			me.showSimpleErrorMessage(msg, title);
		    			dependencyStore.removeAt(dependencyStore.data.items.length - 1);
		    			return false;
		    		}
		    	}
		    	if(fromTask.data.group || toTask.data.group) {
		    		var title = getText("itemov.ganttView.task.permissionErrorTitle");
		    		var msg = getText("itemov.ganttView.dependency.projectCannotBeLinked");
		    		me.showSimpleErrorMessage(msg, title);
		    		me.removeLastAddedDependency();
		    		return false;
		    	}
		    	if(!fromTask.data.linkable || !toTask.data.linkable) {
		    		var title = getText("itemov.ganttView.task.permissionErrorTitle");
		    		var msg = getText("itemov.ganttView.dependency.createDependencyPermError");
		    		me.showSimpleErrorMessage(msg, title);
		    		me.removeLastAddedDependency();
		    		return false;
		    	}
		    }
		    var link = Ext.create('com.trackplus.issue.Link', {
		    	fromGantt: true,
		    	gantt: this,
		    	dependencyType: dependencyType
		    });
		    link.addLinkFromIssueNavigator();
	    }
    },

    validateNewLinkAndShowErrorMessage: function(fromTask, toTask, linkType) {
	    return true;
    },

    setCreatedLinkID: function(createdLinkID, createdLinkLag) {
    	var me = this;
    	var store = me.view.getDependencyStore();
    	var items = store.data.items;
    	var lastAddedLink = items[items.length - 1];
    	if(lastAddedLink != null) {
    		lastAddedLink.data.Id = createdLinkID;
    		lastAddedLink.setLag(createdLinkLag);

    	}
    },

    storeGranularityAndZoom : function() {
	    var me = this;
	    Ext.Ajax.request({
	        url : "itemNavigator!storeGranularityAndZoom.action",
	        disableCaching : true,
	        success : function() {
	        },
	        failure : function() {
		        me.view.setLoading(false);
		        alert("failure");
	        },
	        method : 'POST',
	        params : {
		        "ganttGranularity" : me.granularity
	        }
	    });
    },

    createContextMenuForDependency : function(event, precord) {
	    var me = this;
	    var items = [];
	    var record = precord;
	    items.push({
	        text : getText('itemov.ganttView.deleteLink'),
	        iconCls : 'dependencyContextDelete',
	        handler : function() {
		        var messageConfirmDelete = com.trackplus.TrackplusConfig
		                .getText("common.lbl.messageBox.removeSelected.confirm");
		        var titleDelete = com.trackplus.TrackplusConfig.getText("common.btn.delete");
		        var idToDelete = record.data.Id;
		        Ext.MessageBox.show({
		            title : titleDelete,
		            msg : messageConfirmDelete,
		            buttons : Ext.MessageBox.YESNO,
		            fn : function(btn) {
			            if (btn == "yes") {
			            	var items = me.view.getDependencyStore().data.items;
		    		        for(var ind in items) {
		                		if(items[ind].data.Id == idToDelete) {
		                			me.view.getDependencyStore().remove(items[ind]);
		                		}
		                	}
		    		        if(idToDelete > 0) {
		    		        	me.dependenciesToRemove.push(idToDelete);
		    		        }
			            }
		            },
		            icon : Ext.MessageBox.QUESTION
		        });
	        }
	    });
	    var popupMenu = Ext.create('Ext.menu.Menu', {
	        floating : true,
	        items : items,
	        closeAction : 'destroy'
	    });
	    popupMenu.showAt(event.getXY());
    },

    validateDependencies: function() {
	    var me = this;
	    var dependencies = me.dependencyStore.data.items;
	    var conflicingTasks = new Array();
	    for (index in dependencies) {
		    var fromTaskId = dependencies[index].data.From;
		    var toTaskId = dependencies[index].data.To;
		    var linkType = dependencies[index].data.Type;
		    var lag = dependencies[index].data.Lag;
		    var depID = dependencies[index].data.Id;
		    var lagUnit = dependencies[index].data.LagUnit;
		    if(lagUnit == "mo") {
		    	lag = lag * 30;
		    }
		    if(lagUnit == "w") {
		    	lag = lag * 7;
		    }
		    if (me.validateOneRelation(fromTaskId, toTaskId, linkType, lag) == false) {
			    var fromTask = me.view.taskStore.getById(fromTaskId);
			    var toTask = me.view.taskStore.getById(toTaskId);
			    if (conflicingTasks[parseInt(fromTaskId)] == undefined) {
				    var el = me.view.getSchedulingView().getElementFromEventRecord(fromTask);
				    el && el.frame("#ff0000", 1, {
					    duration : 1000
				    });
				    conflicingTasks[parseInt(fromTaskId)] = true;
			    }
			    if (conflicingTasks[parseInt(toTaskId)] == undefined) {
				    var el2 = me.view.getSchedulingView().getElementFromEventRecord(toTask);
				    el2 && el2.frame("#ff0000", 1, {
					    duration : 1000
				    });
				    conflicingTasks[parseInt(toTaskId)] = true;
			    }
			    me.view.getSchedulingView().highlightDependency(dependencies[index]);
		    }else {
		    	me.removeClsFromOneValidatedTask(depID);
		    }
	    }
    },

    removeClsFromValidatedTasks : function() {
	    var me = this;
	    var dependencies = me.dependencyStore.data.items;
	    for (index in dependencies) {
		    me.view.getSchedulingView().unhighlightDependency(dependencies[index]);
	    }
    },

    removeClsFromOneValidatedTask : function(depID) {
	    var me = this;
	    var dependencies = me.dependencyStore.data.items;
	    for (index in dependencies) {
	    	if(depID == dependencies[index].data.Id) {
	    		me.view.getSchedulingView().unhighlightDependency(dependencies[index]);
	    	}
	    }
    },

    validateOneRelation: function(fromTaskId, toTaskId, linkType, lag) {
	    var me = this;
	    var fromTask = me.view.taskStore.getById(fromTaskId);
	    var fromStartDate = fromTask.data.f19;
	    var fromEndDate = fromTask.data.f20;

	    var toTask = me.view.taskStore.getById(toTaskId);
	    var toStartDate = toTask.data.f19;
	    var toEndDate = toTask.data.f20;

	    switch (linkType) {
	    case 0:
	    	return me.validateSourceEndRelation(fromStartDate, toStartDate, lag);
		    break;
	    case 1:
	    	return me.validateSourceEndRelation(fromStartDate, toEndDate, lag);
		    break;
	    case 2:
	    	return me.validateSourceEndRelation(fromEndDate, toStartDate, lag);
		    break;
	    case 3:
	    	return me.validateSourceEndRelation(fromEndDate, toEndDate, lag);
		    break;
	    default:
		    return true;
	    }
    },

    validateSourceEndRelation: function(sourceDate, targetDate, lag) {
    	var me = this;
    	var isValid = true;
    	if(sourceDate != null && targetDate != null) {
	    	var sourceTmp = new Date(sourceDate.getTime());
			var targetTmp = new Date(targetDate.getTime());
	    	if(sourceDate.getTime() == targetDate.getTime()) {
	    		if(lag > 0) {
	    			isValid = false;
	    		}
	    	}else if(sourceDate.getTime() < targetDate.getTime()) {
	    		var workingDaysBetween = me.getWorkingDays(sourceTmp, targetTmp);
	    		workingDaysBetween -= 1;
	    		if(workingDaysBetween < lag) {
	    			isValid = false;
	    		}

	    	}else {
	    		var workingDaysBetween = me.getWorkingDays(targetTmp, sourceTmp);
	    		workingDaysBetween -= 1;
	    		if(workingDaysBetween * (-1) < lag) {
	    			isValid = false;
	    		}
	    	}
    	}
    	return isValid;
    },

    getWorkingDays: function(startDate, endDate) {
        var result = 0;

       var currentDate = new Date(startDate);
       while (currentDate <= endDate)  {
    	   var weekDay = currentDate.getDay();
           if(weekDay != 0 && weekDay != 6) {
        	   result++;
           }
           currentDate.setDate(currentDate.getDate()+1);
       }
       return result;
    },

    /**
     * This method highlights today.
     * If IsWorkingDay = true then automatically the highlight disappear!
     * For highlighting actual day with enabling moving item to this day needs the following trick:
     * 		IsWorkingDay = false (for highlight), Availability = ['00:00-24:00'] (enable moving item to this date).
     *
     */
    configureActualDay : function(markedDays) {
	    var actualDay = {};
	    actualDay.Date = new Date();//year + "-" + month + "-" + day;
	    actualDay.Cls = "gnt-actual-day-highlight";
	    actualDay.Name = "Today";
	    actualDay.IsWorkingDay = false;
	    actualDay.Availability = ['00:00-24:00'];
	    markedDays.push(actualDay);
    },

    createGanttChart: function(empty) {
	    var me = this;
	    var issuesTree = null;
	    var calendar = null;

	    if (empty == false) {
		    issuesTree = me.model.issues;
		    // marked day= free days, actual day = highlighted columnin Gantt
		    var markedDays = me.model.holidays;
		    me.configureActualDay(markedDays);
		    if (markedDays == undefined) {
			    markedDays = null;
			    calendar = null;
		    } else {
			    calendar = new Gnt.data.Calendar({
				    data : markedDays
			    });
		    }
		    me.parseJSON(issuesTree);
		    me.setCalendarStartEndDate();
	    } else {
		    me.calendarStartDate = new Date();
		    me.calendarStartDate.setDate(me.calendarStartDate.getDate() - 7);
		    me.calendarEndDate = new Date();
		    me.calendarEndDate.setDate(me.calendarEndDate.getDate() + 14);
	    }
	    me.exportPlugin = me.createExportPlugin();
	    me.exportPlugin.exportDialogConfig.cls = 'windowConfig bottomButtonsDialog tpspecial exportGanttPDFDialog';
	    var taskStore = Ext.create("Gnt.data.TaskStore", {
	        calendar : calendar,
	        model : 'com.trackplus.itemNavigator.GanttChartModel',
	        sorters : me.getSorters()
	    });

	    if (!empty) {
		    taskStore.loadData(issuesTree);
		    me.dependencyStore = Ext.create("com.trackplus.ganttView.DependencyStore", {
		        data : me.dependencyStoreData,
		        strictDependencyValidation : false
		    });
		    me.createColumns(false);
	    } else {
		    var taskData = [];
		    taskData[0] = new Object();
		    taskStore.loadData(taskData);
		    taskStore.remove();
		    me.createColumns(true);
	    }
	    taskStore.sort();
	    me.columns.reverse();

	    var baselineVisible = false;
	    if (me.model.isActiveTopDownDate) {
		    baselineVisible = me.model.showBoth;
	    }

	    var ganttChart = Ext.create('Gnt.panel.Gantt', {
	        region: 'center',
	        highlightWeekends: true,
	        cascadeChanges: true,
	        border: false,
	        loadMask: true,
	        rowHeight: 24,
	        enableBaseline: true,
	        baselineVisible: baselineVisible,
	        resizeConfig: {
		        showDuration: false
	        },
	        lockedGridConfig: {
	            stateful: true,
	            stateId: 'itemNavigator_ganttView',
	            width: '250px',
	            plugins: Ext.create('Sch.plugin.TreeCellEditing', {
		            clicksToEdit : 1
	            }),
	            listeners : {
	                // deleting column order from cookie
	                'beforestaterestore': {
		                fn : function(grid, state, eOpts) {
			                delete state.columns;
		                }
	                },
	                'staterestore' : {
		                fn : function(grid, state, eOpts) {
			                var w = state.width;
			                var appWidth = borderLayout.getWidth();
			                if (w > appWidth) {
				                w = appWidth - 450;
				                if (w < 0) {
					                w = 50;
				                }
				                grid.width = w;
			                }
		                }
	                }
	            }
	        },
	        lockedViewConfig : {
	            plugins : [ {
	                ptype : 'gridviewdragdrop',
	                enableDrop : true,
	                dragText : getText('itemov.wbsView.lbl.dragText'),
	                dragGroup : 'itemToCategory',
	                dropGroup : 'itemToCategory'
	            } ],
	            getRowClass : function(record, rowIndex, rp, ds) {
	            	var cls = me.getRowClass.call(me, record, rowIndex, rp, ds);
	            	if(record.data.extendedItem) {
	            		cls += ' ganttExtraRowsWhichAreNotIncludedInFilter';
	            	}
		            return cls;
	            }
	        },
	        viewConfig : {
	            focusedItemCls : 'row-focused',
	            selectedItemCls : 'row-selected',
	            trackOver : false
	        },
	        enableDependencyDragDrop: true,
	        enableTaskDragDrop: true,
//	        enableDragCreation : true,
	        startDate : me.calendarStartDate,
	        enableProgressBarResize : false,
	        endDate : me.calendarEndDate,
	        viewPreset : "weekAndDayLetter",
	        tooltipTpl: me.createTaskTooltipTemplate(),
	        columns: me.columns,
	        selModel : me.createSelModel(),
	        taskStore : taskStore,
	        dependencyStore: me.dependencyStore,
	        plugins: [me.exportPlugin],
	        listeners : {
	        	beforetaskdrag : function beforetaskdrag(ganttChart, taskStore, e, eOpts) {
	            	var isNew = false;
	    			var workItemID = taskStore.data.Id;
	    			if(workItemID < 0) {
	    				isNew = true;
	    			}
	    			if(isNew) {
	    				return true;
	    			}else {

		            	if(!taskStore.data['editable']) {
		            		var title = getText("itemov.ganttView.task.permissionErrorTitle");
			        	    var msg = getText("itemov.ganttView.task.editPermissionErrorMsg");
			        	    me.showSimpleErrorMessage(msg, title);
		            		return false;
		            	}
	    			}
	            	return true;
	            },
	            aftertaskdrop : function aftertaskdrop(ganttChart, taskStore, eOpts) {
	            	me.afterTaskDropEvent(taskStore);
	            },
	            beforetaskresize : function beforetaskresize(ganttChart, taskStore, e, eOpts) {
	            	var isNew = false;
	    			var workItemID = taskStore.data.Id;
	    			if(workItemID < 0) {
	    				isNew = true;
	    			}
	    			if(isNew) {
	    				return true;
	    			}else {
		            	if (!taskStore.data['leaf'] || !taskStore.data['editable']) {
		            		if(!taskStore.data['leaf'] || !taskStore.data['editable']) {
		            		  	var title = getText("itemov.ganttView.task.permissionErrorTitle");
				        	    var msg = getText("itemov.ganttView.task.editPermissionErrorMsg");
				        	    me.showSimpleErrorMessage(msg, title);
		            		}
				            return false;
			            } else {
				            return me.beforeTaskResize(taskStore);
			            }
	    			}
	            },
	            aftertaskresize: function aftertaskresize(ganttChart, taskStore, eOpts) {
	            	me.afterTaskResize(taskStore);
	            },
	            beforedependencydrag: function(ganttChart, taskStore, eOpts) {
	            	me.depStoreSize = me.dependencyStore.data.items.length;
		            me.beforeDependencyDragDrop(me.dependencyStore, taskStore);
	            },
	            afterdependencydragdrop: function(ganttChart, taskStore, eOpts) {
	            	me.afterDependencyDragDrop(me.dependencyStore, taskStore);
	            },
	            dependencycontextmenu : function(g, record, event, target, eOpts) {
	            	me.createContextMenuForDependency(event, record);
	            }
	        }
	    });
	    return ganttChart;
    },

    createExportPlugin: function() {
    	var me = this;
    	var exportPlugin = new Gnt.plugin.Export({
    		printServer: 'itemNavigator!exportGanttChart.action',
	        defaultConfig: {format: "A3", orientation: "landscape", range: "complete"},
	        fileFormat: 'pdf',
	        pageSizes: {Legal: {width: 8.5, height: 14}, Letter: {width: 8.5, height: 11}, A5: {width: 5.8, height: 8.3}, A4: {width: 8.3, height: 11.7}, A3: {width: 11.7, height: 16.5}},
	        DPI: 110,
	        openAfterExport: false,
	        listeners: {
	        	hidedialogwindow: function(response, eOpts) {
	        		if(response.success) {
	        			var url = response.url;
	        			var tmpWorkingDirName = response.tmpWorkingDirName;
	        			Ext.Ajax.request({
	        		        url : 'itemNavigator!checkIfExportedGanttPDFExistsBeforeDownload.action',
	        		        disableCaching : true,
	        		        success : function(response) {
	        		        	var jsonData = Ext.decode(response.responseText);
	        		        	if(jsonData.success) {
	        		        		window.open(url);
	        		        	}else {
	        		        		me.showSimpleErrorMessage(jsonData.msg, "");
	        		        	}
	        		        },
	        		        failure : function() {
	        		        },
	        		        method : 'POST',
	        		        params: {gntPDFLocation: tmpWorkingDirName}
	        		    });
	        		}
	        		return true;
	        	}
	        }
	    });
    	return exportPlugin;
    },

    createTaskTooltipTemplate: function() {
    	var me = this;
    	var myTpl = new Ext.XTemplate('<ul class="taskTip">', '<li><strong>'
    			+ me.model.localizedToolTipLabels.name + ': </strong>{Name}</li>', '<li><strong>'
    			+ me.model.localizedToolTipLabels.issue + ': </strong>{f12}</li>', '<li><strong>'
    			+ me.model.localizedToolTipLabels.start + ': </strong>{f19:this.formatTplDate}</li>', '<li><strong>'
    			+ me.model.localizedToolTipLabels.end + ': </strong>{f20:this.formatTplDate}</li>', '<li><strong>'
    			+ me.model.localizedToolTipLabels.state + ': </strong>{status}</li>', '<li><strong>'
    			+ me.model.localizedToolTipLabels.responsible + ': </strong>{responsibleValue}</li>',
    			'<li><strong>' + getText('admin.customize.list.lbl.percentComplete')
    			+ ': </strong>{PercentDoneToolTip}%</li>', '</ul>').compile();

  	    myTpl.formatTplDate = function(date) {
  	    	return Ext.util.Format.date(date, com.trackplus.TrackplusConfig.DateFormat);
  	    };
  	    return myTpl;
    },

    getISOFormattedDate: function(date) {
    	var str = "";
    	str += date.getFullYear() + "-";
    	var monthNr = parseInt(date.getMonth());
    	monthNr += 1;
    	var monthStr = monthNr;
    	if(monthNr < 10) {
    		monthStr = '0' + monthNr;
    	}
    	str += monthStr + "-";
    	var dayNr = parseInt(date.getDate());
    	var dayStr = dayNr;
    	if(dayNr < 10) {
    		dayStr = "0" + dayNr;
    	}
    	str += dayStr;
    	return str;
    },

    getSorters : function() {
	    var me = this;
	    var sortField = "wbs";
	    // var sortField = "f_so27";
	    var sortDirection;
	    me.WBS_SORT_ASCENDING = true;
	    if (me.WBS_SORT_ASCENDING == true) {
		    sortDirection = 'ASC';
	    } else {
		    sortDirection = 'DESC';
	    }
	    return [ {
	        property : sortField,
	        direction : sortDirection
	    }];
    },

    dataChangeSuccess : function(opts) {
	    var me = this;
	    me.cellInlineCtr.dataChangeSuccess();
	    var me = this;
	    if (opts != null) {
		    var position = opts['position'];
		    me.view.getSchedulingView().scrollEventIntoView(position.task, true);
		    me.view.getSchedulingView().scrollHorizontallyTo(me.view.getSchedulingView().getHorizontalScroll());
		    me.view.getSchedulingView().scrollVerticallyTo(me.view.getSchedulingView().getVerticalScroll());
	    }
    },

    doInitAfterEverythingLoadedAndRendered : function() {
	    var me = this;
	    if (me.model.validateRelationships) {
		    me.validateDependencies();
	    }
	    if (me.model.highlightCriticalPath) {
		    var v = me.view.getSchedulingView();
		    v.highlightCriticalPaths(true);
	    }
    },

    normalizeTask: function(fieldID, date, taskID) {
		var me = this;
		var node = me.view.getTaskStore().getById(taskID);
		if(fieldID == 'f19') {
			node.setStartDate(Ext.Date.parse(date, com.trackplus.TrackplusConfig.DateFormat));
		}
		if(fieldID == 'f20') {
			node.setEndDate(Ext.Date.parse(date, com.trackplus.TrackplusConfig.DateFormat));
		}
    },

    getEditedOrNewItemJSON: function(oneRecord) {
    	var me = this;
    	var modifiedFieldsObj = oneRecord.getChanges();
		var oneRecordJSON = null;
		if(modifiedFieldsObj != null) {
			oneRecordJSON = {};
			oneRecordJSON.modifiedFields = {};
			for(var fieldID in modifiedFieldsObj) {
				if (fieldID && fieldID.charAt(0) == 'f') {
					var fieldValue = modifiedFieldsObj[fieldID];
					if(fieldValue instanceof Date) {
						fieldValue = Ext.util.Format.date(fieldValue, com.trackplus.TrackplusConfig.DateFormat);
					}
					oneRecordJSON.modifiedFields[fieldID] = fieldValue;
				}
			}
			var workItemID = oneRecord.data.Id;

			var isProjectRecord = oneRecord.data.group;
			if(isProjectRecord == undefined) {
				isProjectRecord = false;
			}
			var isNew = false;
			if(workItemID < 0) {
				isNew = true;
			}
			oneRecordJSON["workItemID"] = workItemID;
			oneRecordJSON["workItemIDAbove"] = null;
			if(isNew) {
				oneRecordJSON["workItemIDAbove"] = oneRecord.data.workItemIDAbove;
			}
			oneRecordJSON["projectID"] = oneRecord.data.projectID;
			oneRecordJSON["issueTypeID"] = oneRecord.data.issueTypeID;
			oneRecordJSON["isNew"] = isNew;
			oneRecordJSON["rowIndex"] = oneRecord.data.workItemIndex;
		}
		return oneRecordJSON;
    },

    getdependenciesJSON: function() {
    	var me = this;
    	var depArr = [];
    	var store = me.view.getDependencyStore();
    	var items = store.data.items;
    	for(var ind in items) {
    		var depItem = items[ind];
    		if(depItem != null) {
	    		if(depItem.dirty) {
	    			if(depItem.extraParamsToSubmit != null) {
	    				depArr.push(depItem.extraParamsToSubmit);
	    			}
	    		}
    		}
    	}
    	return depArr;
    },

    saveChanges2: function(scope, methodToExecuteAfterSuccess,  methodToExecuteAfterSuccessParams, validateBeforeSave) {
    	var me = this;
    	var taskStore = me.view.getTaskStore();
    	var modifiedRecords = me.view.getTaskStore().getUpdatedRecords();
    	if(me.isChartContainsUnsavedItems()) {
    		me.view.setLoading(true);
    		me.taskStoreJSONForSave = [];
    		editedOrNewTasksJSON = [];
    		for(var ind in modifiedRecords) {
    			var oneRecord = modifiedRecords[ind];
    			var isProjectRecord = oneRecord.data.group;
    			if(isProjectRecord == undefined) {
    				isProjectRecord = false;
    			}
    			if(!isProjectRecord) {
	    			var editedOrNewItemJSON = me.getEditedOrNewItemJSON(oneRecord);
	    			if(editedOrNewItemJSON != null) {
	    				editedOrNewTasksJSON.push(editedOrNewItemJSON);
	    			}
    			}
    		}

    		var ganttDependencyStore = me.getdependenciesJSON();
	    	Ext.Ajax.request({
	    		url : "itemNavigator!planItem3.action",
	    		disableCaching : true,
	    		success : function(result) {
	    			var jsonData = Ext.decode(result.responseText);
	    			me.view.setLoading(false);
	    			if(jsonData.success) {
	    				taskStore.sync();
	    				if(methodToExecuteAfterSuccess != null) {
	    					methodToExecuteAfterSuccess.apply(scope, methodToExecuteAfterSuccessParams);
	    				}
	    				me.fireEvent.call(me, 'datachange');
	    			}else {
	    				var errorArray = Ext.decode(jsonData.errorMessage);
	    				me.showSaveErrorsConfirmDialog(me.getErrorStringAfterValidation(errorArray));
	    			}
	    		},
	    		failure : function() {
	    			me.view.setLoading(false);
	    			alert("failure");
	    		},
	    		method : 'POST',
	    		params : {
	    			ganttTaskStore : Ext.encode(editedOrNewTasksJSON),
	    			validateBeforeSave: validateBeforeSave,
	    			ganttDependencyStore: Ext.encode(ganttDependencyStore),
	    			ganttDependenciesToRemove: Ext.encode(me.dependenciesToRemove )
	    		}
	    	});
    	}
    },

    getErrorStringAfterValidation: function(errorArray) {
    	var me = this;
    	var errorStrToShow = "";
		for(var ind in errorArray) {
			var oneErrorItem = errorArray[ind];
			var rowIndex = oneErrorItem.rowIndex;
			errorStrToShow += '<b>' + getText('item.pseudoColumn.indexNumber') + ": " +  rowIndex + '</b> <br>';
			var errors = oneErrorItem.errors;
			for(var ind2 in errors) {
				errorStrToShow += '  ' + errors[ind2] + '<br>';
			}
		}
		return errorStrToShow;
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


    createView: function(borderLayoutController) {
	    var me = this;
	    me.cellInlineCtr = Ext.create('com.trackplus.itemNavigator.CellInlineEditController', {
	    	navigator : me,
	    	isPrintItemEditable: me.model.isPrintItemEditable
	    });
	    me.dependenciesToRemove = [];
	    me.queryFieldCSS = me.model.queryFieldCSS;
	    if (me.model.issues.length > 0) {
		    me.dateArrayMinMax = [];
		    me.dependencyStoreData = [];
		    me.view = me.createGanttChart(false);
		    if (me.model.listViewData != undefined) {
			    actualGranularity = me.model.listViewData.ganttGranularity;
			    me.view.switchViewPreset(actualGranularity, me.calendarStartDate, me.calendarEndDate);
		    }
		    me.initMyListeners();
			me.defineStartEndListenerScheduleView();
			me.cellInlineCtr.grid = me.getTreeGrid();
			me.cellInlineCtr.gantt = me.view;
			me.cellInlineCtr.initListeners();
			me.cellInlineCtr.initGanttSpecificListener();
	    } else {
		    me.view = me.createGanttChart(true);
	    }
	    Ext.defer(me.doInitAfterEverythingLoadedAndRendered, 150, me);
	    return me.view;
    }

});
