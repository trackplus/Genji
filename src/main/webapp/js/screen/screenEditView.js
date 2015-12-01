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


Ext.define('com.trackplus.screen.FieldEditWrapperView', {
	extend : 'Ext.panel.Panel',
	config : {
		model : null,
		parentView : null,
		emptyText : getText('admin.customize.form.edit.emptyField')
	},
	bodyStyle : {
		overflow : 'hidden',
		background : 'transparent'
	},
	myComponentCls : null,
	getFieldCls : function() {
		return "designField";
	},
	initComponent : function() {
		var me = this;
		var cfg = me.getFieldWrapperConfig.call(me);
		var iconRendering = me.model.iconRendering
		if(iconRendering===2){
			//both
			this.layout="hbox";
		}
		Ext.apply(this, cfg);
		if (me.model.empty) {
			me.bodyStyle = {
				paddingTop : '3px',
				paddingBottom : '5px'
			};
			me.html = me.emptyText;
			me.addCls("emptyField");
			me.myComponentCls = "emptyField";
		} else {
			me.myComponentCls = me.getFieldCls();
			me.addCls(me.myComponentCls);
			var htmlStr = null;
			var missingPlugin = me.model.jsonData.missingPlugin;
			if (missingPlugin) {
				htmlStr = '<span>' + me.model.jsonData.htmlString + '</span>';
			} else {
				var srcImage = me.model.jsonData.srcImage;
				if (srcImage ) {
					var imgCmp = Ext.create('Ext.Img', {
						src : 'loadImage.action?imageName=' + srcImage,
						listeners : {
							render : function(c) {
								c.getEl().on('load', function() {
									me.ownerCt.ownerCt.updateLayout();
								});
							}
						}
					});
					me.items = [ imgCmp ];
				} else {
					var iconRendering = me.model.iconRendering  ? me.model.iconRendering : 0;
					switch (iconRendering) {
					case 0: {
						// label
						var extClassName = me.model.extClassName;
						if (extClassName ) {
							var fieldCmp = Ext.create(extClassName, {
								flex : 1
							}).render(me.model);
							me.items = [ fieldCmp ];
						}
						break;
					}
					case 1: {
						// icon
						var iconCmp = me.createIconCmp();
						me.items = [ iconCmp ];
						break;
					}
					case 2: {
						// both
						var iconCmp = me.createIconCmp();
						var extClassName = me.model.extClassName;
						if (extClassName ) {
							var fieldCmp = Ext.create(extClassName, {
								flex : 1
							}).render(me.model);
							me.items = [ iconCmp, fieldCmp ];
						} else {
							me.items = [ iconCmp ];
						}

						break;
					}
					}
				}
			}
			if (htmlStr ) {
				me.html = htmlStr;
			}
		}
		me.callParent();
	},
	createIconCmp : function() {
		var me = this;
		var icon = "data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==";
		var iconCls = 'customFields-ticon';
		switch (me.model.fieldID) {
		case 2: {
			// ISSUETYPE
			iconCls = "issueType-ticon";
			break;
		}
		case 4: {
			// STATE
			iconCls = "status-ticon";
			break;
		}
		case 10: {
			// PRIORITY
			iconCls = "priority-ticon";
			break;
		}
		case 11: {
			// SEVERITY
			iconCls = "severity-ticon";
			break;
		}
		}
		return Ext.create('Ext.Component', {
			width : 22,
			html : '<img class="' + iconCls + '" src="' + icon + '" height="16"/>'
		});
	},
	getFieldWrapperConfig : function() {
		var me = this;
		var cfg = {
			margin : '2 2 2 2',
			border : false
		};
		if (me.model.iconRendering === 1) {
			cfg['width'] = 25;
		}
		return cfg;
	}
});

Ext.define('com.trackplus.screen.PropertiesPanel', {
	extend : 'Ext.grid.property.Grid',
	componentModel : null,
	title : getText('admin.customize.form.edit.lbl.layoutProperties'),
	border : false,
	bodyBorder : false,
	height : 300,
	region : 'north',
	ALIGN_TOP : 0,
	ALIGN_BOTTOM : 1,
	ALIGN_MIDDLE : 2,
	ALIGN_LEFT : 3,
	ALIGN_RIGHT : 4,
	ALIGN_CENTER : 5,
	ALIGN_TOP_STR : getText('admin.customize.form.fieldProperties.align.top'),
	ALIGN_BOTTOM_STR : getText('admin.customize.form.fieldProperties.align.bottom'),
	ALIGN_MIDDLE_STR : getText('admin.customize.form.fieldProperties.align.middle'),
	ALIGN_LEFT_STR : getText('admin.customize.form.fieldProperties.align.left'),
	ALIGN_RIGHT_STR : getText('admin.customize.form.fieldProperties.align.right'),
	ALIGN_CENTER_STR : getText('admin.customize.form.fieldProperties.align.center'),
	ICON_RENDERING_LABEL : 0,
	ICON_RENDERING_ICON : 1,
	ICON_RENDERING_BOTH : 2,
	ICON_RENDERING_LABEL_STR : getText('admin.customize.form.fieldProperties.iconRendering.label'),
	ICON_RENDERING_ICON_STR : getText('admin.customize.form.fieldProperties.iconRendering.icon'),
	ICON_RENDERING_BOTH_STR : getText('admin.customize.form.fieldProperties.iconRendering.both'),

	propertyNames : {
		'screen.label' : getText('common.lbl.label'),
		'screen.name' : getText("common.lbl.name"),
		'screen.description' : getText("common.lbl.description"),
		'tab.label' : getText('common.lbl.label'),
		'tab.name' : getText("common.lbl.name"),
		'tab.description' : getText("common.lbl.description"),
		'panel.name' : getText("common.lbl.name"),
		'panel.description' : getText("common.lbl.description"),
		'panel.rowsNo' : getText("admin.customize.form.panelProperties.rows"),
		'panel.colsNo' : getText("admin.customize.form.panelProperties.columns"),
		'fieldType' : getText("admin.customize.form.fieldProperties.type"),
		'field.name' : getText("common.lbl.name"),
		'field.description' : getText("common.lbl.description"),
		'field.rowSpan' : getText("admin.customize.form.fieldProperties.rowSpan"),
		'field.colSpan' : getText("admin.customize.form.fieldProperties.colSpan"),
		'field.iconRendering' : getText('admin.customize.form.fieldProperties.iconRendering'),
		'field.labelHAlign' : getText('admin.customize.form.fieldProperties.labelHAlign'),
		'field.labelVAlign' : getText('admin.customize.form.fieldProperties.labelVAlign'),
		'field.valueHAlign' : getText('admin.customize.form.fieldProperties.valueHAlign'),
		'field.valueVAlign' : getText('admin.customize.form.fieldProperties.valueVAlign'),
		'field.hideLabelBoolean' : getText('admin.customize.form.fieldProperties.hideLabel')
	},
	source : {},
	createCombo : function(options) {
		var store = Ext.create('Ext.data.Store', {
			fields : [ {
				name : 'id',
				type : 'int'
			}, {
				name : 'label',
				type : 'string'
			} ],
			data : options
		});
		var inputComp = Ext.create('Ext.form.ComboBox', {
			store : store,
			queryMode : 'local',
			displayField : 'label',
			valueField : 'id'
		});
		return inputComp;
	},
	initComponent : function() {
		var me = this;
		Ext.apply(this);

		var valignList = [ {
			id : me.ALIGN_TOP,
			label : me.ALIGN_TOP_STR
		}, {
			id : me.ALIGN_BOTTOM,
			label : me.ALIGN_BOTTOM_STR
		}, {
			id : me.ALIGN_MIDDLE,
			label : me.ALIGN_MIDDLE_STR
		} ];
		var halignList = [ {
			id : me.ALIGN_LEFT,
			label : me.ALIGN_LEFT_STR
		}, {
			id : me.ALIGN_RIGHT,
			label : me.ALIGN_RIGHT_STR
		}, {
			id : me.ALIGN_CENTER,
			label : me.ALIGN_CENTER_STR
		} ];
		var iconRenderingList = [ {
			id : me.ICON_RENDERING_LABEL,
			label : me.ICON_RENDERING_LABEL_STR
		}, {
			id : me.ICON_RENDERING_ICON,
			label : me.ICON_RENDERING_ICON_STR
		}, {
			id : me.ICON_RENDERING_BOTH,
			label : me.ICON_RENDERING_BOTH_STR
		}

		];
		var comboIconRendering = me.createCombo(iconRenderingList);
		var comboHAlign = me.createCombo(halignList);
		var comboVAlign = me.createCombo(valignList);
		var comboHAlign1 = me.createCombo(halignList);
		var comboVAlign1 = me.createCombo(valignList);
		me.customEditors = {
			'fieldType' : Ext.create('Ext.form.field.Display', {}),
			'field.rowSpan' : Ext.create('Ext.form.field.Number', {
				minValue : 1,
				maxValue : 20,
				allowBlank : false
			}),
			'field.colSpan' : Ext.create('Ext.form.field.Number', {
				minValue : 1,
				maxValue : 10,
				allowBlank : false
			}),
			'panel.rowsNo' : Ext.create('Ext.form.field.Number', {
				minValue : 1,
				maxValue : 20,
				allowBlank : false
			}),
			'panel.colsNo' : Ext.create('Ext.form.field.Number', {
				minValue : 1,
				maxValue : 10,
				allowBlank : false
			}),
			'field.iconRendering' : comboIconRendering,
			'field.labelHAlign' : comboHAlign,
			'field.labelVAlign' : comboVAlign,
			'field.valueHAlign' : comboHAlign1,
			'field.valueVAlign' : comboVAlign1
		};
		me.customRenderers = {
			'field.iconRendering' : function(val, meta, r) {
				var record = comboIconRendering.findRecord(comboIconRendering.valueField, val);
				return record ? record.get(comboIconRendering.displayField) : comboIconRendering.valueNotFoundText;
			},
			'field.labelHAlign' : function(val, meta, r) {
				var record = comboHAlign.findRecord(comboHAlign.valueField, val);
				return record ? record.get(comboHAlign.displayField) : comboHAlign.valueNotFoundText;
			},
			'field.labelVAlign' : function(val, meta, r) {
				var record = comboVAlign.findRecord(comboVAlign.valueField, val);
				return record ? record.get(comboVAlign.displayField) : comboVAlign.valueNotFoundText;
			},
			'field.valueHAlign' : function(val, meta, r) {
				var record = comboHAlign1.findRecord(comboHAlign1.valueField, val);
				return record ? record.get(comboHAlign1.displayField) : comboHAlign1.valueNotFoundText;
			},
			'field.valueVAlign' : function(val, meta, r) {
				var record = comboVAlign1.findRecord(comboVAlign1.valueField, val);
				return record ? record.get(comboVAlign1.displayField) : comboVAlign1.valueNotFoundText;
			}
		};
		me.callParent();
	}
});

Ext.define('com.trackplus.screen.FieldListPanel', {
	extend : 'Ext.grid.GridPanel',
	config : {
		url : ''
	},
	stripeRows : true,
	border : false,
	bodyBorder : false,
	enableColumnHide : false,
	enableColumnMove : false,
	columnLines : true,
	region : 'center',
	collapsible : false,
	split : false,
	columns : [ {
		text : getText('common.lbl.name'),
		// flex: 1,
		width : 140,
		dataIndex : 'label',
		id : 'label',
		sortable : true
	}, {
		text : getText('common.lbl.description'),
		flex : 1,
		/* width:200, */dataIndex : 'description',
		id : 'description',
		sortable : false
	} ],
	viewConfig : {
		forceFit : true,
		plugins : {
			ddGroup : 'fields',
			ptype : 'gridviewdragdrop',
			enableDrop : false,
			enableDrag : true
		}
	},
	selModel : Ext.create('Ext.selection.RowModel', {
		singleSelect : true
	}),

	initComponent : function() {
		var me = this;
		var store = Ext.create('Ext.data.Store', {
			fields : [ {
				name : 'id'
			}, {
				name : 'name'
			}, {
				name : 'label'
			}, {
				name : 'description'
			} ],
			proxy : {
				type : 'ajax',
				url : me.url,
				reader : {
					type : 'json'
				}
			},
			sorters : [ {
				property : 'label',
				direction : 'ASC'
			} ]
		});
		me.store = store;
		me.callParent();
		store.load();
	}
});
Ext.define('com.trackplus.screen.FieldTreePanel', {
	extend : 'Ext.tree.Panel',
	config : {
		url : ''
	},
	useArrows : false,
	rootVisible : false,
	border : false,
	bodyBorder : false,
	region : 'center',
	height : 300,
	selModel : Ext.create('Ext.selection.RowModel', {
		singleSelect : true
	}),
	viewConfig : {
		forceFit : true,
		plugins : {
			ddGroup : 'fields',
			ptype : 'gridviewdragdrop',
			enableDrop : false,
			enableDrag : true
		}
	},
	columns : [ {
		xtype : 'treecolumn',
		text : getText('common.lbl.name'),
		// flex: 1,
		width : 140,
		dataIndex : 'label',
		id : 'label',
		sortable : true
	}, {
		text : getText('common.lbl.description'),
		flex : 1,
		/* width:200, */dataIndex : 'description',
		id : 'description',
		sortable : false
	} ],
	initComponent : function() {
		var me = this;
		var store = Ext.create('Ext.data.TreeStore', {
			fields : [ {
				name : 'id'
			}, {
				name : 'name'
			}, {
				name : 'label'
			}, {
				name : 'description'
			} ],
			proxy : {
				type : 'ajax',
				url : me.url,
				reader : {
					type : 'json'
				}
			}
		});
		me.store = store;
		me.callParent();
	}
});

Ext.define('com.trackplus.screen.ScreenEditView', {
	extend : 'Ext.panel.Panel',
	config : {
		screenView : null,
		urlFieldList : '',
		titleFieldList : 'Fields',
		fieldsAsTree : false
	},
	region : 'center',
	fieldsAsTree : false,
	margins : '0 0 0 0',
	border : false,
	baseCls : 'x-plain',
	layout : 'border',
	propertiesPanel : null,
	fieldListPanel : null,
	cls : 'screenEditViewWrapper',
	initComponent : function() {
		var me = this;
		me.items = [ me.screenView, me.createEastPanel() ];
		me.callParent();
	},
	createEastPanel : function() {
		var me = this;
		me.propertiesPanel = Ext.create('com.trackplus.screen.PropertiesPanel', {
			bodyBorder : false
		});
		if (me.fieldsAsTree) {
			me.fieldListPanel = Ext.create('com.trackplus.screen.FieldTreePanel', {
				url : me.urlFieldList,
				title : me.titleFieldList
			});

		} else {
			me.fieldListPanel = Ext.create('com.trackplus.screen.FieldListPanel', {
				url : me.urlFieldList,
				title : me.titleFieldList
			});
		}
		return Ext.create('Ext.panel.Panel', {
			layout : 'border',
			region : 'east',
			border : false,
			bodyBorder : false,
			bodyPadding : 0,
			width : 275,
			split : true,
			style : {
				borderLeft : '1px solid #D0D0D0'
			},
			margin : '0 0 0 -4',
			items : [ me.propertiesPanel, me.fieldListPanel ]
		});
	},
	getSelectedTab : function() {
		return this.screenView.getSelectedTab();
	}
});

Ext.define('com.trackplus.screen.ScreenEditFacade', {
	extend : 'com.trackplus.screen.BaseScreenFacade',
	config : {
		urlFieldList : null,
		fieldsAsTree : false,
		useConfig : false,
		configURL : null,
		lastSelectedTab : null,
		fieldWrapperCls : null,
		screenAction : null,
		screenActionParams : null,
		screenUpdateAction : null,
		tabAction : null,
		tabUpdateAction : null,
		panelAction : null,
		panelUpdateAction : null,
		fieldAction : null,
		fieldUpdateAction : null,

		messageDeletePanel : null,
		messageDeleteTab : null,
		messageDeleteField : null,
		messageCantDeleteScreen : null,
		messageCantDeleteLastTab : null,
		backAction : null
	},
	fieldsAsTree : false,
	baseScreenView : null,
	mainPanel : null,
	tabsPanel : null,
	propertiesPan : null,

	clickOnField : false,
	clickOnPanel : false,
	clickOnTab : false,

	screenEditView : null,
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		me.screenController = Ext.create(me.controllerCls, {
			screenModel : me.screenModel,
			showOneTab : me.showOneTab,
			lastSelectedTab : me.lastSelectedTab,
			refreshTabUrl : me.refreshTabUrl,
			storeTabUrl : me.storeTabUrl,
			panelViewCls : me.panelViewCls,
			fieldWrapperCls : me.fieldWrapperCls,
			screenAction : me.screenAction,
			screenActionParams : me.screenActionParams,
			screenUpdateAction : me.screenUpdateAction,
			tabAction : me.tabAction,
			tabUpdateAction : me.tabUpdateAction,
			panelAction : me.panelAction,
			panelUpdateAction : me.panelUpdateAction,
			fieldAction : me.fieldAction,
			fieldUpdateAction : me.fieldUpdateAction,
			useConfig : me.useConfig,
			configURL : me.configURL,
			messageDeletePanel : me.messageDeletePanel,
			messageDeleteTab : me.messageDeleteTab,
			messageDeleteField : me.messageDeleteField,
			messageCantDeleteScreen : me.messageCantDeleteScreen,
			messageCantDeleteLastTab : me.messageCantDeleteLastTab,
			backAction : me.backAction
		});

	},
	createScreenEditViewComponent : function() {
		var me = this;
		me.screenView = me.createViewComponent();
		me.screenView.addCls('designScreen');
		me.screenView.myComponentCls = 'designScreen';
		me.screenEditView = Ext.create('com.trackplus.screen.ScreenEditView', {
			screenView : me.screenView,
			urlFieldList : me.urlFieldList,
			fieldsAsTree : me.fieldsAsTree,
			titleFieldList : me.titleFieldList
		});
		me.screenEditView.propertiesPanel.addListener('propertychange', me.screenController.changeAttribute, me.screenController);
		me.screenController.screenEditView = me.screenEditView;
		return me.screenEditView;
	},
	getToolbar : function() {
		var me = this;
		return me.screenController.getToolbar.call(me.screenController);
	}
});
