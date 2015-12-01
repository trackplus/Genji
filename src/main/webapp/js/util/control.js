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


Ext.define('com.trackplus.util.HelpWrapperComponent',{
	extend:'Ext.container.Container',
	config:{
		inputComp:null,
		helpStr:null,
		helpExpanded:false,
		flexInputComp:false
	},
	layout:'anchor',
	defaults:{
		anchor:'100%'
	},

	border:false,
	bodyBorder:false,
	btnHelp:null,helpArea:null,panWrapper:null,
	initComponent: function(){
		var me=this;
		if(CWHF.isNull(me.helpExpanded)){
			me.helpExpanded=false;
		}
		if(CWHF.isNull(me.flexInputComp)){
			me.flexInputComp=false;
		}
		me.callParent();
		var items=me.createChildren(me.disabled || me.inputComp.disabled);
		me.add(items);
		me.setDisabled(me.disabled || me.inputComp.disabled);
	},
	createChildren:function(disabled){
		var me=this;
		var items=new Array();
		me.btnHelp=Ext.create('Ext.button.Button',{
			style:{ marginBottom: '5px', marginLeft: '5px',border:'none',background:'none'},
			iconCls: 'help16',
			enableToggle:false,
			scale: 'small',
			handler:me.showHideHelpArea,
			disabled:disabled,
			scope:me
		});
		me.btnHelp.focusable =false;
		if(me.flexInputComp===true){
			me.inputComp.flex=1;
		}
		me.panWrapper=Ext.create('Ext.container.Container', {
			border:false,
			itemId: 'panWrapper',
			layout: 'hbox',
			items: [me.inputComp, me.btnHelp]
		});
		me.helpArea=Ext.create('Ext.Component',{
			html: me.helpStr,
			margin:'0 5 5 5',
			border:true,
			disabled:disabled,
			cls:'infoBox2',
			hidden:(me.helpExpanded===false)
		});
		items.push(me.panWrapper);
		items.push(me.helpArea);
		return items;
	},
	showHideHelpArea:function(){
		var me=this;
		me.helpArea.setVisible(!me.helpArea.isVisible());
	},
	setDisabled:function(disabled){
		var me=this;
		me.callParent(false);
		if(me.inputComp.setDisabled&&Ext.isFunction(me.inputComp.setDisabled)){
			me.inputComp.setDisabled(disabled);
		}
		me.btnHelp.setDisabled(disabled);
		me.helpArea.setDisabled(disabled);
	},
	getInputComponent:function(){
		return this.inputComp;
	},
	getHelpButton:function(){
		return this.btnHelp;
	}
});

Ext.define('com.trackplus.SpellCheckTextField',{
	extend:'Ext.form.field.Text',
	initComponent: function(){
		var me=this;
		me.addListener('afterrender',me.initSpellCheck,me);
		me.callParent();
	},
	initSpellCheck:function(){
		var me=this;
		me.inputEl.dom.setAttribute('spellcheck','true');
		me.inputEl.on('contextmenu', function(e){
			e.stopPropagation();
			return false;
		}, me);
	}
});


Ext.define('util.ControlWithHelpFactory',{
	extend:'Ext.Base',
	singleton:true,
	constructor: function(config) {
	},

	labelWidth:250,
	textFieldWidth:250+300,
	textFieldWidthShort:250+70,
	alignR:"right",
	FieldSetWidth:250+300+150,

	/**
	 * Creates the help button that controls the help area that appears underneath an input element.
	 *
	 * @param {String} idee The itemId or id of the associated input component. The id of the help button
	 * is constructed from the idee plus '.hlp' appended.
	 * @param {String} helpStr The localized help string
	 * @param {String} intId An auto-generated internal id for connecting the help area to this button.
	 * @return {Ext.button.Button} The help button
	 */
	/*createHelpCmp:function(idee,helpStr,intId){
		var me=this;
		var helpComp={
				xtype:'button',
				style:{ marginBottom: '5px', marginLeft: '5px',border:'none',background:'none'},
				iconCls: 'help16',
				enableToggle:false,
				scale: 'small',
				itemId: idee+'.hlp',
				handler:function(){
					Ext.getCmp(intId).setVisible(!Ext.getCmp(intId).isVisible());
				}
		};
		return helpComp;
	},*/

	/**
	 * Creates a wrapper panel that encloses an in input component, a help button, and a help
	 * area that can appear underneath the input element. The id of the complete
	 * wrapper component is created from the id in <code>otherSettings</code>, if any is given,
	 * plus '.wrp' appended. Same is true for itemId.
	 *
	 * @param {xtype} inputComp The input component that is being wrapped together with a help
	 * button and help area.
	 * @param {String} name The form field name of the associated input component.
	 * @param {helpStr} The localized help string.
	 * @return {Ext.panel.Panel} The wrapper panel
	 */
	/*createWrapperHelp:function(inputComp,name,helpStr,otherSettings){
		var me=this;
		var idee = name;
		var intId = Ext.id();
		if (otherSettings) {
			for (propertyName in otherSettings) {
				if (propertyName==='id') idee = otherSettings[propertyName];
				if (propertyName==='itemId') idee = otherSettings[propertyName];
			}
		}
		var helpComp=me.createHelpCmp(idee,helpStr,intId);
		var panWrapper={
				xtype: 'panel',
				border:false,
				bodyBorder:false,
				layout: 'hbox',
				itemId: idee+'.wrp',
				items: [inputComp,helpComp]
		};
		var helpArea={
				xtype: 'panel',
				id:intId,
				html: helpStr,
				margin:'0 5 5 5',
				border:false,
				bodyBorder:true,
				width:me.textFieldWidth-5,
				bodyCls:'infoBox2',
				hidden:true
		};
		return [panWrapper,helpArea];
	},*/



	/**
	 * Gets a control by the path specified in the "arguments", starting form the scope
	 * The scope should be the main container the components are searched from
	 */
	getControl: function() {
		var container = this;
		Ext.Array.each(arguments, function(argument, index, allArguments) {
			container = container.getComponent(argument);
			if (CWHF.isNull(container)) {
				return false;
			}
		}, this);
		return container;
	},

	/**
	 * Similar like getControl but gets the help wrapper container, not the included control
	 * although the path in the "arguments" should be the control path
	 */
	getHelpWrapper: function() {
		var container = this;
		Ext.Array.each(arguments, function(argument, index, allArguments) {
			if (index===allArguments.length-1) {
				container = container.getComponent(CWHF.getWrapperItemId(argument));
			} else {
				container = container.getComponent(argument);
			}
			if (CWHF.isNull(container)) {
				return false;
			}
		}, this);
		return container;
	},

	/**
	 * Gets the itemId of the help wrapper container based on the itemId of the included control
	 */
	getWrapperItemId: function(inputCompItemId) {
		return inputCompItemId +'_wrp';
	},

	/**
	 * Similar like getControl but gets the help-wrapped control,
	 * the path in the "arguments" should be the control path without help contained itemId
	 */
	getWrappedControl: function() {
		var container = this;
		Ext.Array.each(arguments, function(argument, index, allArguments ) {
			if (index===allArguments.length-1) {
				container = container.getComponent(CWHF.getWrapperItemId(argument));
			} else {
				container = container.getComponent(argument);
			}
			if (CWHF.isNull(container)) {
				return false;
			}
		}, this);
		if (container) {
			return container.getInputComponent();
		}
		return null;
	},

	getLabel: function(labelKey, otherSettings) {
		var label = '';
		if (labelKey && labelKey!=='') {
			if (otherSettings && otherSettings.labelIsLocalized) {
				label = labelKey;
			} else {
				label = getText(labelKey);
			}
		}
		return label;
	},

	addOtherSettings: function(fieldConfig, otherSettings, label) {
		if (otherSettings) {
			if (label) {
				this.addBlankText(fieldConfig, otherSettings, label);
			}
			for (propertyName in otherSettings) {
				fieldConfig[propertyName] = otherSettings[propertyName];
			}
		}
	},

	addBlankText: function(fieldConfig, otherSettings, label) {
		if (otherSettings && /*otherSettings.allowBlank &&*/ otherSettings.allowBlank===false && CWHF.isNull(otherSettings.blankText)) {
			fieldConfig.blankText = getText("common.err.required", label);
		}
	},

	
	/**
	 * Create a new Ext.Action action with localized text and tooltip
	 * label the localized label of the operation
	 * iconCls the icon class for the action
	 * handler the action handler
	 * tooltip makes sense only for toolbar buttons (not for context menu items)
	 * 		this is the localized tooltip not the tooltip key, because the tooltip for localized actions should not change
	 * 		depending on the context
	 * disabled whether by creation it should be disabled
	 */
	/*public*/createAction: function(labelKey, iconCls, handlerName, otherSettings) {
		var me = this;
		var label = this.getLabel(labelKey, otherSettings);
		var actionConfig = {
				text: label,
				overflowText: label,
				tooltip: label,
				iconCls: iconCls,
				handler: handlerName
				//handlerScope: handlerScope
		};
		this.addOtherSettings(actionConfig, otherSettings, label);
		return Ext.create("Ext.Action", actionConfig);
	},

	/**
	 * Create a text field component with proper sizing. The label will be localized.
	 * @param {String} labelKey The resource key for the label.
	 * @param {String} name The field name of the component.
	 * @param {String} otherSettings Any other configuration parameters, e.g., <code>{width:'200px', id:'myId'}</code>.
	 * @return {Ext.form.TextField} A text field
	 */
	createHiddenField: function(name, otherSettings, listenerConfig) {
		var hiddenFieldConfig = {
				xtype: 'hiddenfield',
				name: name
				};

		if (otherSettings) {
			for (propertyName in otherSettings) {
				hiddenFieldConfig[propertyName] = otherSettings[propertyName];
			}
		}
		if (listenerConfig) {
			hiddenFieldConfig.listeners = listenerConfig;
		}
		return Ext.create('Ext.form.field.Hidden', hiddenFieldConfig);
	},

	/**
	 * Create a text field component with proper sizing. The label will be localized.
	 * @param {String} labelKey The resource key for the label.
	 * @param {String} name The field name of the component.
	 * @param {String} otherSettings Any other configuration parameters, e.g., <code>{width:'200px', id:'myId'}</code>.
	 * @return {Ext.form.TextField} A text field
	 */
	createTextField: function(labelKey, name, otherSettings, listenerConfig) {
		var label = this.getLabel(labelKey, otherSettings);
		var textFieldConfig = {
				xtype: 'textfield',
				fieldLabel: label,
				name: name,
				msgTarget: 'under',
				labelStyle:{overflow:'hidden'},
				labelWidth:this.labelWidth,
				labelAlign:this.alignR,
				width:this.textFieldWidth
				};
		if (listenerConfig) {
			textFieldConfig.listeners = listenerConfig;
		}
		this.addOtherSettings(textFieldConfig, otherSettings, label);
		return Ext.create('Ext.form.field.Text', textFieldConfig);
	},



	/**
	 * Create a text field component together with a help button and help area.The label will be localized.
	 * The help string will be looked up under resource string <code>'labelKey.help'</code>.
	 * If an <code>id</code> and/or <code>itemId</code> is given in <code>otherSettings</code>,
	 * it will be for used the field component. The associated help button then has this id,
	 * with the string <code>'.hlp'</code> appended. The enclosing panel has this id,
	 * with the string <code>'.wrp'</code> appended.
	 *
	 * @param {String} labelKey The resource key for the label.
	 * @param {String} name The field name of the inner component.
	 * @param {JSONString} otherSettings Any other configuration parameters, e.g., {width:'200px', id:'myId'}
	 * @return {Ext.panel.Panel} A panel enclosing a text field, a help button, and a help area.
	 */
	createTextFieldWithHelp: function(labelKey, name, otherSettings, listenerConfig, wrapperItemId) {
		var inputComp=this.createTextField(labelKey, name, otherSettings, listenerConfig);
		var helpStr = getText(labelKey+'.help');
		if(CWHF.isNull(helpStr)){
			return inputComp;
		}

		//return this.createWrapperHelp(inputComp,name,helpStr,otherSettings);
		if (CWHF.isNull(wrapperItemId)) {
			wrapperItemId = this.getWrapperItemId(inputComp.getItemId());
		}
		return Ext.create("com.trackplus.util.HelpWrapperComponent",
				{inputComp:inputComp, helpStr:helpStr, itemId: wrapperItemId});
	},

	/*createFilterPicker: function(labelKey, name, otherSettings) {
		var label = this.getLabel(labelKey, otherSettings);
		var queryPickerConfig = {
			xtype: 'textfield',
			fieldLabel : this.getLabel(labelKey, otherSettings),
			name: name,
			itemId: name,
			labelStyle:{overflow:'hidden'},
			labelWidth:this.labelWidth,
			labelAlign:this.alignR,
			width:this.textFieldWidth
		};
		this.addOtherSettings(queryPickerConfig, otherSettings, label);
		return Ext.create('com.trackplus.util.FilterPicker', queryPickerConfig);
	},*/

	/**
	 * Creates a report tree for multiple selection
	 */
	/*createReportTree: function(reportIDs, extraProxyParameters,
			hiddenFieldName, treeConfigOptions) {
		var treeConfig = {
				rootVisible: false,
				containerScroll: true,
				autoScroll: true,
				cls:"simpleTree"
			}
		this.addOtherSettings(treeConfig, treeConfigOptions, null);
		//extraProxyParameters for getting the tree nodes
		if (CWHF.isNull(extraProxyParameters)) {
			extraProxyParameters = new Object();
		}
		//report tree is with check boxes (report picker isn't but they use the same struts action)
		extraProxyParameters["useChecked"] = true;
		//to set the checked field on the server side and send back in json nodes
		extraProxyParameters["selectedIDs"] = reportIDs;
		return Ext.create("com.trackplus.util.ReportTree",
				{selectedIDs: reportIDs,//initialize the hidden field
				extraProxyParameters: extraProxyParameters,
				hiddenFieldName: hiddenFieldName,
				treeConfig: treeConfig});
	},*/


	/**
	 * Creates a multiple select project tree
	 */
	/*createProjectTree: function(projectIDs, extraProxyParameters,
			hiddenFieldName, treeConfigOptions, itemId, selectionChangeOptions) {
		var treeConfig = {
				//minHeight: 50,
				rootVisible: false,
				containerScroll: true,
				autoScroll: true,
				cls:"simpleTree"
			}
		this.addOtherSettings(treeConfig, treeConfigOptions, null);
		//extraProxyParameters for getting the tree nodes
		if (CWHF.isNull(extraProxyParameters)) {
			extraProxyParameters = new Object();
		}
		//project tree is with check boxes (project picker isn't but they use the same struts action)
		extraProxyParameters["useChecked"] = true;
		//to set the checked field on the server side and send back in json nodes
		extraProxyParameters["projectIDs"] = projectIDs;
		return Ext.create("com.trackplus.util.ProjectTree",
				{selectedIDs: projectIDs,//initialize the hidden field
				itemId: itemId,
				extraProxyParameters: extraProxyParameters,
				hiddenFieldName: hiddenFieldName,
				treeConfig: treeConfig,
				selectionChangeOptions:selectionChangeOptions});
	},*/

	/**
	 * Creates a project picker
	 */
	/*createProjectPicker: function(labelKey, name, otherSettings, listenerConfig) {
		var label = this.getLabel(labelKey, otherSettings);
		var projectPickerConfig = {
			//xtype: 'textfield',
			fieldLabel : label,
			name: name,
			itemId: name,
			labelStyle:{overflow:'hidden'},
			labelWidth:this.labelWidth,
			labelAlign:this.alignR,
			width:this.textFieldWidth
		};
		if (listenerConfig) {
			projectPickerConfig.listeners = listenerConfig;
		}
		this.addOtherSettings(projectPickerConfig, otherSettings, label);
		return Ext.create("com.trackplus.util.ProjectPicker", projectPickerConfig);
	}, */

	/*createProjectPickerWithHelp: function(labelKey, name, otherSettings) {
		var inputComp=this.createProjectPicker(labelKey, name, otherSettings)
		var helpStr = getText(labelKey+'.help');
		if(CWHF.isNull(helpStr)){
			return inputComp;
		}
		wrapperItemId = this.getWrapperItemId(inputComp.getItemId());
		return Ext.create("com.trackplus.util.HelpWrapperComponent",
				{inputComp:inputComp, helpStr:helpStr, itemId: wrapperItemId});
	},*/

	/**
	 * Creates a multiple select release tree
	 */
	/*createReleaseTree: function(releaseIDs, extraProxyParameters,
			hiddenFieldName, treeConfigOptions, itemId) {
		var treeConfig = {
				rootVisible: false,
				containerScroll: true,
				autoScroll: true,
				cls:"simpleTree"
			}
		this.addOtherSettings(treeConfig, treeConfigOptions, null);
		//extraProxyParameters for getting the tree nodes
		if (CWHF.isNull(extraProxyParameters)) {
			extraProxyParameters = new Object();
		}
		//project tree is with check boxes (project picker isn't but they use the same struts action)
		extraProxyParameters["useChecked"] = true;
		//to set the checked field on the server side and send back in json nodes
		extraProxyParameters["releaseIDs"] = releaseIDs;
		return Ext.create("com.trackplus.util.ReleaseTree",
				{releaseIDs: releaseIDs,
				itemId: itemId,
				extraProxyParameters: extraProxyParameters,
				hiddenFieldName: hiddenFieldName,
				treeConfig: treeConfig});
	},*/

	/*createReleasePicker: function(labelKey, name, otherSettings, listenerConfig) {
		var label = this.getLabel(labelKey, otherSettings);
		var releasePickerConfig = {
			fieldLabel : label,
			name: name,
			itemId: name,
			labelStyle:{overflow:'hidden'},
			labelWidth:this.labelWidth,
			labelAlign:this.alignR,
			width:this.textFieldWidth
		};
		if (listenerConfig) {
			releasePickerConfig.listeners = listenerConfig;
		}
		this.addOtherSettings(releasePickerConfig, otherSettings, label);
		return Ext.create('com.trackplus.util.ReleasePicker', releasePickerConfig);
	},*/

	/**
	 * Create a department picker
	 */
	/*createDepartmentPicker: function(labelKey, name, otherSettings) {
		var label = this.getLabel(labelKey, otherSettings);
		var departmentPickerConfig = {
			xtype: 'textfield',
			fieldLabel : label,
			name: name,
			itemId: name,
			labelStyle:{overflow:'hidden'},
			labelWidth:this.labelWidth,
			labelAlign:this.alignR,
			width:this.textFieldWidth
		};
		this.addOtherSettings(departmentPickerConfig, otherSettings, label);
		return Ext.create('com.trackplus.util.DepartmentPicker', departmentPickerConfig);
	},*/

	/*createDepartmentPickerWithHelp: function(labelKey, name, otherSettings) {
		var inputComp=this.createDepartmentPicker(labelKey, name, otherSettings)
		var helpStr = getText(labelKey+'.help');
		if(CWHF.isNull(helpStr)){
			return inputComp;
		}
		wrapperItemId = this.getWrapperItemId(inputComp.getItemId());
		return Ext.create("com.trackplus.util.HelpWrapperComponent",
				{inputComp:inputComp, helpStr:helpStr, itemId: wrapperItemId});
	},*/

	/**
	 * Creates a multiple select department tree
	 */
	/*createDepartmentTree: function(departmentIDs, extraProxyParameters,
			hiddenFieldName, treeConfigOptions, itemId, selectionChangeOptions) {
		var treeConfig = {
				rootVisible: false,
				containerScroll: true,
				autoScroll: true,
				cls:"simpleTree"
			}
		this.addOtherSettings(treeConfig, treeConfigOptions, null);
		//extraProxyParameters for getting the tree nodes
		if (CWHF.isNull(extraProxyParameters)) {
			extraProxyParameters = new Object();
		}
		//project tree is with check boxes (project picker isn't but they use the same struts action)
		extraProxyParameters["useChecked"] = true;
		//to set the checked field on the server side and send back in json nodes
		extraProxyParameters["departmentIDs"] = departmentIDs;
		return Ext.create("com.trackplus.util.DepartmentTree",
				{selectedIDs: departmentIDs,//initialize the hidden field
				extraProxyParameters: extraProxyParameters,
				hiddenFieldName: hiddenFieldName,
				treeConfig: treeConfig,
				itemId: itemId,
				selectionChangeOptions:selectionChangeOptions});
	},*/

	getTreeWithLabel: function(label, tree, treeWithLabelID, labelWidth, labelAlign) {
		if (CWHF.isNull(labelWidth)) {
			labelWidth=150;
		}
		if (CWHF.isNull(labelAlign)) {
			labelAlign='right';
		}
		if (CWHF.isNull(label)){
			return tree;
		}
		tree.flex=1;
		var labelCmp = this.getLabelCmp(label, labelAlign, labelWidth);
		return this.wrapLabelAndTree(labelCmp,tree,treeWithLabelID);
	},

	getLabelCmp: function(label, labelAlign, labelWidth) {
		return Ext.create('Ext.Component',{
			cls:labelAlign==='right'?'x-form-item-label-right x-form-item-label':'x-form-item-label',
			style:{
				marginRight:'5px'
			},
			html:label+":",
			width:labelWidth
		});
	},

	wrapLabelAndTree: function(labelCmp, treeObject,treeWithLabelID) {
		return Ext.create('Ext.container.Container', {
			itemId: treeWithLabelID,
			border:false,
			layout: {
				type: 'hbox',
				pack: 'start',
				align: 'stretch'
			},
			items: [labelCmp, treeObject]
		});
	},

	/**
	 * Create a department picker
	 */
	createColorPicker: function(labelKey, name, otherSettings) {
		var label = this.getLabel(labelKey, otherSettings);
		var colorPickerConfig = {
			xtype: 'textfield',
			fieldLabel : label,
			name: name,
			labelStyle:{overflow:'hidden'},
			labelWidth:this.labelWidth,
			labelAlign:this.alignR,
			width:this.textFieldWidth
		};
		this.addOtherSettings(colorPickerConfig, otherSettings, label);
		return Ext.create('com.trackplus.util.ColorField', colorPickerConfig);
	},

	/**
	 * Create a number field component with proper sizing. The label will be localized.
	 * @param {String} labelKey The resource key for the label.
	 * @param {String} name The field name of the component.
	 * @param {Integer} decimalPrecision Number of digits after the decimal character
	 * @param {Number} minValue Minimum value
	 * @param {Number} maxValue Maximum value
	 * @param {String} otherSettings Any other configuration parameters, e.g., <code>{width:'200px', id:'myId'}</code>.
	 * @return {Ext.form.field.Number} A number field
	 */
	createNumberField: function(labelKey, name, decimalPrecision, minValue, maxValue, otherSettings) {
		var label = this.getLabel(labelKey, otherSettings);
		var numberFieldConfig = {
				xtype: 'numberfield',
				fieldLabel : label,
				hideLabel:(CWHF.isNull(labelKey)),
				msgTarget: 'under',
				name: name,
				labelStyle:{overflow:'hidden'},
				labelWidth:this.labelWidth,
				labelAlign:this.alignR,
				width:this.textFieldWidth,
				decimalSeparator:com.trackplus.TrackplusConfig.DecimalSeparator
		};
		if (decimalPrecision) {
			numberFieldConfig.decimalPrecision = decimalPrecision;
		}
		if (minValue) {
			numberFieldConfig.minValue = minValue;
		}
		if (maxValue) {
			numberFieldConfig.maxValue = maxValue;
		}
		this.addOtherSettings(numberFieldConfig, otherSettings, label);
		return Ext.create('Ext.form.field.Number', numberFieldConfig);
	},

	/**
	 * Create a number field component together with a help button and help area.The label will be localized.
	 * The help string will be looked up under resource string <code>'labelKey.help'</code>.
	 * If an <code>id</code> and/or <code>itemId</code> is given in <code>otherSettings</code>,
	 * it will be for used the field component. The associated help button then has this id,
	 * with the string <code>'.hlp'</code> appended. The enclosing panel has this id,
	 * with the string <code>'.wrp'</code> appended.
	 *
	 * @param {String} labelKey The resource key for the label.
	 * @param {String} name The field name of the inner component.
	 * @param {Integer} decimalPrecision Number of digits after the decimal character
	 * @param {Number} minValue Minimum value
	 * @param {Number} maxValue Maximum value
	 * @param {JSONString} otherSettings Any other configuration parameters, e.g., {width:'200px', id:'myId'}
	 * @return {Ext.panel.Panel} A panel enclosing a number field, a help button, and a help area.
	 */
	createNumberFieldWithHelp: function(labelKey, name, decimalPrecision, minValue, maxValue, otherSettings, wrapperItemId) {
		var inputComp=this.createNumberField(labelKey, name, decimalPrecision, minValue, maxValue, otherSettings)
		var helpStr = getText(labelKey+'.help');
		if(CWHF.isNull(helpStr)){
			return inputComp;
		}
		if (CWHF.isNull(wrapperItemId)) {
			wrapperItemId = this.getWrapperItemId(inputComp.getItemId());
		}
		return Ext.create("com.trackplus.util.HelpWrapperComponent",
				{inputComp:inputComp, helpStr:helpStr, itemId: wrapperItemId});
	},

	/**
	 * Create a time field component with proper sizing. The label will be localized.
	 * @param {String} labelKey The resource key for the label.
	 * @param {String} name The field name of the component.
	 * @param {String} format Format of the time, e.g., 'H:i'
	 * @param {String} altFormats Alternative formats
	 * @param {String} otherSettings Any other configuration parameters, e.g., <code>{width:'200px', id:'myId'}</code>.
	 * @return {Ext.form.field.TimeField} A time field
	 */
	createDateField: function(labelKey, name, otherSettings) {
		var label = this.getLabel(labelKey, otherSettings);
		var dateFieldConfig = {
				xtype: 'datefield',
				fieldLabel : label,
				name: name,
				labelStyle:{overflow:'hidden'},
				labelWidth:this.labelWidth,
				labelAlign:this.alignR,
				width:this.textFieldWidth,
				format: com.trackplus.TrackplusConfig.DateFormat,
				//the short format interpretable by struts2 (two year digit)
				submitFormat:com.trackplus.TrackplusConfig.DateSubmitFormat
				};
		this.addOtherSettings(dateFieldConfig, otherSettings, label);
		return Ext.create('Ext.form.field.Date', dateFieldConfig);
	},

	/**
	 * Create a time field component with proper sizing. The label will be localized.
	 * @param {String} labelKey The resource key for the label.
	 * @param {String} name The field name of the component.
	 * @param {String} format Format of the time, e.g., 'H:i'
	 * @param {String} altFormats Alternative formats
	 * @param {String} otherSettings Any other configuration parameters, e.g., <code>{width:'200px', id:'myId'}</code>.
	 * @return {Ext.form.field.TimeField} A time field
	 */
	createTimeField: function(labelKey, name, otherSettings) {
		var label = this.getLabel(labelKey, otherSettings)
		var timeFieldConfig = {
				xtype: 'timefield',
				fieldLabel : label,
				name: name,
				labelStyle:{overflow:'hidden'},
				labelWidth:this.labelWidth,
				labelAlign:this.alignR,
				width:this.textFieldWidth,
				//format: com.trackplus.TrackplusConfig.TimeFormat
				format: com.trackplus.TrackplusConfig.TimeFormatNoSeconds
				};
		this.addOtherSettings(timeFieldConfig, otherSettings, label);
		return Ext.create('Ext.form.field.Time', timeFieldConfig);
	},

	createTimeFieldWithHelp: function(labelKey, name, otherSettings, wrapperItemId) {
		var inputComp=this.createTimeField(labelKey, name, otherSettings)
		var helpStr = getText(labelKey+'.help');
		if(CWHF.isNull(helpStr)){
			return inputComp;
		}
		if (CWHF.isNull(wrapperItemId)) {
			wrapperItemId = this.getWrapperItemId(inputComp.getItemId());
		}
		return Ext.create("com.trackplus.util.HelpWrapperComponent",
				{inputComp:inputComp, helpStr:helpStr, itemId: wrapperItemId});
	},

	/**
	 * Creates a field container for dateTime picker with a date and a time part
	 */
	createDateTimeField: function(labelKey, dateName, timeName, otherDateSettings, otherTimeSettings, fieldContainerSettings) {
		var label = this.getLabel(labelKey, otherDateSettings)
		var txtDate=CWHF.createDateField(null, dateName, otherDateSettings);
		var txtTime=CWHF.createTimeField(null, timeName, otherTimeSettings);
		var fieldContainerConfig = {
				xtype: 'fieldcontainer',
				fieldLabel : label,
				layout: 'hbox',
				labelWidth:this.labelWidth,
				labelAlign:this.alignR,
				labelStyle:{overflow:'hidden'},
				items:[txtDate,txtTime]
		}
		this.addOtherSettings(fieldContainerConfig, fieldContainerSettings, label);
		return Ext.create('Ext.form.FieldContainer', fieldContainerConfig);
	},

	/**
	 * Create a text area field component with proper sizing. The label will be localized.
	 * @param {String} labelKey The resource key for the label.
	 * @param {String} name The field name of the inner component.
	 * @param {JSONString} otherSettings Any other configuration parameters, e.g., {width:'200px', id:'myId'}
	 * @return {Ext.form.TextArea} A text area field.
	 */
	createTextAreaField: function(labelKey, name, otherSettings) {
		var label = this.getLabel(labelKey, otherSettings);
		var textAreaFieldConfig = {
				xtype: 'textareafield',
				fieldLabel : this.getLabel(labelKey, otherSettings),
				name: name,
				labelStyle:{overflow:'hidden'},
				labelWidth:this.labelWidth,
				labelAlign:this.alignR,
				width:this.textFieldWidth
				};
		this.addOtherSettings(textAreaFieldConfig, otherSettings, label);
		return Ext.create('Ext.form.field.TextArea', textAreaFieldConfig);
	},

	/**
	 * Create a text area field component together with a help button and help area.The label will be localized.
	 * The help string will be looked up under resource string <code>'labelKey.help'</code>.
	 * If an <code>id</code> and/or <code>itemId</code> is given in <code>otherSettings</code>,
	 * it will be for used the field component. The associated help button then has this id,
	 * with the string <code>'.hlp'</code> appended. The enclosing panel has this id,
	 * with the string <code>'.wrp'</code> appended.
	 *
	 * @param {String} labelKey The resource key for the label.
	 * @param {String} name The field name of the inner component.
	 * @param {Integer} decimalPrecision Number of digits after the decimal character
	 * @param {Number} minValue Minimum value
	 * @param {Number} maxValue Maximum value
	 * @param {JSONString} otherSettings Any other configuration parameters, e.g., {width:'200px', id:'myId'}
	 * @return {Ext.panel.Panel} A panel enclosing a number field, a help button, and a help area.
	 */
	createTextAreaFieldWithHelp: function(labelKey, name, otherSettings, wrapperItemId) {
		var inputComp=this.createTextAreaField(labelKey, name, otherSettings)
		var helpStr = getText(labelKey+'.help');
		if(CWHF.isNull(helpStr)){
			return inputComp;
		}
		//return this.createWrapperHelp(inputComp,name,helpStr,otherSettings);
		if (CWHF.isNull(wrapperItemId)) {
			wrapperItemId = this.getWrapperItemId(inputComp.getItemId());
		}
		return Ext.create("com.trackplus.util.HelpWrapperComponent",
				{inputComp:inputComp, helpStr:helpStr, itemId: wrapperItemId});
	},

	/**
	 * Create a text area field component with proper sizing. The label will be localized.
	 * @param {String} labelKey The resource key for the label.
	 * @param {String} name The field name of the inner component.
	 * @param {JSONString} otherSettings Any other configuration parameters, e.g., {width:'200px', id:'myId'}
	 * @return {Ext.form.TextArea} A text area field.
	 */
	createHtmlEditorField: function(labelKey, name, otherSettings) {
		var label = this.getLabel(labelKey, otherSettings);
		var htmlEditorFieldConfig = {
				fieldLabel: label,
				name: name,
				labelStyle:{overflow:'hidden'},
				labelWidth:this.labelWidth,
				labelAlign:this.alignR,
				defaultValue:""
				//padding: '20 0 0 0',
				//anchor:'100% -27'
				};
		this.addOtherSettings(htmlEditorFieldConfig, otherSettings, label);
		return Ext.create('Ext.form.field.HtmlEditor', htmlEditorFieldConfig);
	},
	txtRTEIndex:0,
	RTEInstanceReady:new Object(),
	createRichTextEditorField:function(name,otherSettings,focus,resize,ckeditorCfg){
		var me=this;
		var idRTE;
		me.txtRTEIndex++;
		idRTE="rte"+me.txtRTEIndex;
		var rteCfg={
			name:name,
			id:idRTE
		};
		this.addOtherSettings(rteCfg, otherSettings);
		var txtArea=Ext.create('Ext.form.field.TextArea',rteCfg);
		txtArea.addListener('afterrender',me.initRTEditor,me,{id:idRTE,focus:focus,resize:resize,ckeditorCfg:ckeditorCfg});
		return txtArea;
	},
	resizeRTEditor:function(cmp,width,height,oldWidth,oldHeight,opts){
		var me=this;
		var difW=width-oldWidth;
		var difH=height-oldHeight;
		var idDescription=opts.id+'-inputEl';
		var o=CKEDITOR.instances[idDescription];
		var isReady=me.RTEInstanceReady[opts.id]===true;
		var w=width;
		if(cmp.fieldLabel){
			w=w-cmp.labelWidth;
		}
		if (o&&isReady){
			o.resize( w, height);
		}
	},
	destroyRTEditor:function(comp,opts){
		var me=this;
		//var me=this;
		//alert(opts.id);
		var idDescription=opts.id+'-inputEl';
		//var toolbarFCKStartExpanded=true;
		//var resize=opts.resize;
		/*var sLang=null;
		if (CWHF.isNull(sLang)){
			sLang="en";
		}*/
		me.RTEInstanceReady[opts.id]=false;
		var o=CKEDITOR.instances[idDescription];
		if (o){
			CKEDITOR.remove(o);
			o.destroy();
		}
	},
	initRTEditor:function(comp, opts){
		var me=this;
		var idDescription=opts.id+'-inputEl';
		var toolbarFCKStartExpanded=true;
		var resize=opts.resize;
		var sLang=getLocale();
		if (CWHF.isNull(sLang)){
			sLang="en";
		}
		var o=CKEDITOR.instances[idDescription];
		if (o){
			me.RTEInstanceReady[opts.id]=false;
			CKEDITOR.remove(o);
		}
		if(comp&&comp.isDisabled()){
			return false;
		}
		var filebrowserImageBrowseUrl='';
		var filebrowserUploadUrl ='';// uploadUrl + '?Type=File';
		var filebrowserImageUploadUrl ='';// uploadUrl + '?Type=Image';
		var otherCkeditorCfg=opts.ckeditorCfg;
		var workItemID=null;
		var useInlineTask=false;
		var useBrowseImage=false;
		if(otherCkeditorCfg){
			useBrowseImage=otherCkeditorCfg.useBrowseImage;
			if(otherCkeditorCfg.workItemID) {
				workItemID = otherCkeditorCfg.workItemID;
			}
			if(useBrowseImage===true){
				filebrowserImageBrowseUrl = 'browseFile.action?type=Images';
				filebrowserUploadUrl = 'browseFile!uploadFile.action?Type=File';
				filebrowserImageUploadUrl = 'browseFile!uploadFile.action?Type=Image';
				if(workItemID!=null){
					filebrowserImageBrowseUrl+= '&workItemID=' + workItemID;
					filebrowserUploadUrl+= '&workItemID=' + workItemID;
					filebrowserImageUploadUrl+= '&workItemID=' + workItemID;
				}
			}
			useInlineTask=otherCkeditorCfg.useInlineTask;
		}
		var ckeditorCfg={
			customConfig:sBasePath+'cktrackplusconfig.js',
			contentsCss:com.trackplus.TrackplusConfig.htmlEditorCSS,
			filebrowserUploadUrl:filebrowserUploadUrl,
			filebrowserImageBrowseUrl:filebrowserImageBrowseUrl,
			filebrowserImageUploadUrl:filebrowserImageUploadUrl,
			simpleuploads_maxFileSize:com.trackplus.TrackplusConfig.MAXFILESIZE,
			language:sLang/*,
			 toolbarStartupExpanded:toolbarFCKStartExpanded*/
		};

		if(otherCkeditorCfg) {
			for (propertyName in otherCkeditorCfg) {
				ckeditorCfg[propertyName] = otherCkeditorCfg[propertyName];
			}
		}

		if(workItemID&&useInlineTask){
			var extraPlugins=ckeditorCfg['extraPlugins'];
			if(CWHF.isNull(extraPlugins)||extraPlugins===''){
				extraPlugins='issue,code,simpleuploads,image2,task';
			}else{
				extraPlugins=extraPlugins+",task";
			}
			ckeditorCfg['extraPlugins']=extraPlugins;
		}

		var ckEditor=CKEDITOR.replace(idDescription,ckeditorCfg);
		var focus=opts.focus;
		if(focus===true){
			ckEditor.on('instanceReady',function(evt) {
				ckEditor.focus();
			});
		}
		ckEditor.on('blur', function(e) {
			ckEditor.updateElement();
		});
		if(resize===true&&comp){
			comp.addListener('resize',me.resizeRTEditor,me,opts);
			ckEditor.on('instanceReady',function(evt) {
				me.RTEInstanceReady[opts.id]=true;
				var w=comp.getWidth();
				if(comp.fieldLabel){
					w=w-comp.labelWidth;
				}
				var h=comp.getHeight();
				ckEditor.resize(w,h);
			});
		}


		CKEDITOR.on('instanceReady', function(e) {
			var editor = e.editor;

			// When an upload starts
			editor.on('simpleuploads.startUpload', function (e) {
				//alert("Uploading " + e.data.name + ", please wait...");
			});

			// When the upload ends
			editor.on('simpleuploads.endUpload', function (e) {
				if (e.data.ok){
					//alert("File " + e.data.name + " uploaded correctly.");
				}
				else{
					alert("Upload of " + e.data.name + " has failed.");
				}
			})

			// When the upload has finished (the plugin has finished and the element is ready on the page
			editor.on( 'simpleuploads.finishedUpload' , function(ev) {
				var element = ev.data.element;
				var finishedUploadHandler=editor.config.finishedUploadHandler;
				if(finishedUploadHandler){
					finishedUploadHandler.call(editor,ev);
				}
				//element.addClass("picture");
			});
			editor.on('fileUploadResponse',function(ev){
				var finishedUploadHandler=editor.config.finishedUploadHandler;
				if(finishedUploadHandler){
					finishedUploadHandler.call(editor,ev);
				}
			});

		});
	},
	submitRTEditor:function(txtArea){
		var idDescription=txtArea.id+'-inputEl';
		var ckEditor=CKEDITOR.instances[idDescription];
		if (ckEditor){
			ckEditor.updateElement();
		}
	},
	refreshRTEditorValue:function(txtArea,value){
		txtArea.setValue(value);
		var idDescription=txtArea.id+'-inputEl';
		var o=CKEDITOR.instances[idDescription];
		if (o){
			o.setData( value, function() {
				this.checkDirty(); // true
			});
		}
	},


	/**
	 * Create a check box component. The label will be localized.
	 * @param {String} labelKey The resource key for the label.
	 * @param {String} name The field name of the inner component.
	 * @param {JSONString} otherSettings Any other configuration parameters, e.g., {width:'200px', id:'myId'}
	 * @param {JSONString} listenerConfig The associated listeners for this component
	 * @return {Ext.form.field.Checkbox} A checkbox component.
	 */
	createCheckbox: function(labelKey, name, otherSettings, listenerConfig) {
		var checkBoxConfig = {
				xtype: 'checkbox',
				inputValue : true,
				fieldLabel : this.getLabel(labelKey, otherSettings),
				//boxLabel : this.getLabel(labelKey, otherSettings),
				name: name,
				labelStyle:{overflow:'hidden'},
				labelWidth:this.labelWidth,
				labelAlign:this.alignR,
				width:this.textFieldWidth
			};

		if (otherSettings && otherSettings.value) {
			checkBoxConfig.checked = otherSettings.value;
			delete otherSettings.value;
		}
		this.addOtherSettings(checkBoxConfig, otherSettings);
		if (listenerConfig) {
			checkBoxConfig.listeners = listenerConfig;
		}
		return Ext.create('Ext.form.field.Checkbox', checkBoxConfig);
	},

	/**
	 * Create a checkbox field component together with a help button and help area.The label will be localized.
	 * The help string will be looked up under resource string <code>'labelKey.help'</code>.
	 * If an <code>id</code> and/or <code>itemId</code> is given in <code>otherSettings</code>,
	 * it will be for used the field component. The associated help button then has this id,
	 * with the string <code>'.hlp'</code> appended. The enclosing panel has this id,
	 * with the string <code>'.wrp'</code> appended.
	 *
	 * @param {String} labelKey The resource key for the label.
	 * @param {String} name The field name of the inner component.
	 * @param {JSONString} otherSettings Any other configuration parameters, e.g., {width:'200px', id:'myId'}
	 * @return {Ext.panel.Panel} A panel enclosing a checkbox, a help button, and a help area.
	 */
	createCheckboxWithHelp: function(labelKey, name, otherSettings, listenerConfig, wrapperItemId) {
		var inputComp=this.createCheckbox(labelKey, name, otherSettings, listenerConfig)
		var helpStr = getText(labelKey+'.help');
		if(CWHF.isNull(helpStr)){
			return inputComp;
		}
		//return this.createWrapperHelp(inputComp,name,helpStr,otherSettings);
		if (CWHF.isNull(wrapperItemId)) {
			wrapperItemId = this.getWrapperItemId(inputComp.getItemId());
		}
		return Ext.create("com.trackplus.util.HelpWrapperComponent",
				{inputComp:inputComp, helpStr:helpStr, itemId: wrapperItemId});
	},

	/**
	 * Create a combobox component. The label will be localized.
	 * @param {String} labelKey The resource key for the label.
	 * @param {String} name The field name of the inner component.
	 * @param {JSONString} otherSettings Any other configuration parameters, e.g., {width:'200px', id:'myId'}
	 * @param {JSONString} listenerConfig The associated listeners for this component
	 * @param {String} itemId The itemId for the inner component.
	 * @return {Ext.form.field.Checkbox} A combobox component.
	*/
	createCombo: function(labelKey, name, otherSettings, listenerConfig/*, itemId*/) {
		var data = [];
		var includeEmpty=false;
		if (otherSettings && otherSettings.data) {
			data = otherSettings.data;
			delete otherSettings.data;
		}
		if (otherSettings && otherSettings.includeEmpty) {
			includeEmpty = otherSettings.includeEmpty;
			delete otherSettings.includeEmpty;
		}
		var idType = 'int';
		if (otherSettings && otherSettings.idType) {
			idType = otherSettings.idType;
			delete otherSettings.idType;
		}
		var displayField='label';
		if (otherSettings && otherSettings.displayField) {
			displayField = otherSettings.displayField;
		}
		var label = this.getLabel(labelKey, otherSettings);
		var comboConfig = {
			xtype	: 'combo',
			store	: Ext.create('Ext.data.Store', {
				data	: data,
				fields	: [{name:'id', type:idType, useNull:true}, {name:'label', type:'string'}],
				autoLoad: false
				}),
			displayField: 'label',
			valueField	: 'id',
			queryMode	: 'local',
			triggerAction: 'all',
			name:	name,
			editable: false,
			fieldLabel : label,
			labelStyle:{overflow:'hidden'},
			labelWidth:this.labelWidth,
			labelAlign:this.alignR,
			width:this.textFieldWidth
		};
		if(includeEmpty===true){
			comboConfig.tpl= new Ext.XTemplate('<tpl for=".">' + '<li style="min-height:22px;" class="x-boundlist-item" role="option">' + '{'+displayField+'}' + '</li></tpl>');
		}
		if (listenerConfig) {
			comboConfig.listeners = listenerConfig;
		}
		this.addOtherSettings(comboConfig, otherSettings, label);
		return Ext.create('Ext.form.field.ComboBox', comboConfig);
	},

	/**
	 * Create a combobox field component together with a help button and help area.The label will be localized.
	 * The help string will be looked up under resource string <code>'labelKey.help'</code>.
	 * If an <code>id</code> and/or <code>itemId</code> is given in <code>otherSettings</code>,
	 * it will be for used the field component. The associated help button then has this id,
	 * with the string <code>'.hlp'</code> appended. The enclosing panel has this id,
	 * with the string <code>'.wrp'</code> appended.
	 *
	 * @param {String} labelKey The resource key for the label.
	 * @param {String} name The field name of the inner component.
	 * @param {JSONString} otherSettings Any other configuration parameters, e.g., {width:'200px', id:'myId'}
	 * @param {JSONString} listenerConfig The associated listeners for this component
	 * @param {String} itemId The itemId for the inner component.
	 * @return {Ext.panel.Panel} A panel enclosing a combobox, a help button, and a help area.
	 */
	createComboWithHelp: function(labelKey, name, otherSettings, listenerConfig, wrapperItemId) {
		var inputComp=this.createCombo(labelKey, name, otherSettings, listenerConfig);
		var helpStr = getText(labelKey+'.help');
		if(CWHF.isNull(helpStr)){
			return inputComp;
		}
		//return this.createWrapperHelp(inputComp,itemId,helpStr,otherSettings);
		if (CWHF.isNull(wrapperItemId)) {
			wrapperItemId = this.getWrapperItemId(inputComp.getItemId());
		}
		return Ext.create("com.trackplus.util.HelpWrapperComponent",
				{inputComp:inputComp, helpStr:helpStr, itemId: wrapperItemId});
	},

	/**
	 * Create a multiselect component. There is no label.
	 * @param {String} itemId The itemId of this component
	 * @param {String} name The field name of the inner component.
	 * @param {Boolean} modifiable If the selection list can be modified
	 * @param {JSONString} data The data for this component.
	 * @param {String} value The current value.
	 * @param {String} width The width of the box.
	 * @param {String} height The height of the box.
	 * @param {JSONString} listenerConfig The associated listeners for this component
	 * @return {Ext.ux.form.MultiSelect} A multiselect component.
	*/
	createMultiSelect: function(labelKey, name, otherSettings, listenerConfig) {
		var data = [];
		if (otherSettings) {
			if (otherSettings.data) {
				data = otherSettings.data;
				delete otherSettings.data;
			}
			var idType = 'int';
			if (otherSettings.idType) {
				idType = otherSettings.idType;
				delete otherSettings.idType;
			}
			var fieldLabel = '';
			if (otherSettings.fieldLabel) {
				fieldLabel = this.getLabel(otherSettings.fieldLabel, otherSettings);
				delete otherSettings.fieldLabel;
			}
		}
		var multiSelectConfig = {
				xtype: 'multiselect',
				listTitle: this.getLabel(labelKey, otherSettings),
				fieldLabel: fieldLabel,
				store: Ext.create('Ext.data.Store', {
					data	: data,
					fields	: [{name:'id', type:idType}, {name:'label', type:'string'}],
					autoLoad: false
					}),
				displayField: 'label',
				valueField: 'id',
				labelAlign:this.alignR,
				name: name
		};
		if (listenerConfig) {
			multiSelectConfig.listeners = listenerConfig;
		}
//		if (itemId) {
//			multiSelectConfig.itemId=itemId;
//		}
		if (otherSettings) {
			for (property in otherSettings) {
				multiSelectConfig[property] = otherSettings[property];
			}
		}
		return Ext.create('Ext.ux.form.MultiSelect', multiSelectConfig);
	},




	/**
	 * Create a multiselect component with help. There is no label.
	 * @param {String} itemId The itemId of this component
	 * @param {String} name The field name of the inner component.
	 * @param {Boolean} modifiable If the selection list can be modified
	 * @param {JSONString} data The data for this component.
	 * @param {String} value The current value.
	 * @param {String} width The width of the box.
	 * @param {String} height The height of the box.
	 * @param {JSONString} listenerConfig The associated listeners for this component
	 * @return {Ext.ux.form.MultiSelect} A multiselect component.
	 */
	/*createMultiSelectWithHelp: function(itemId, name, modifiable, data, value, width, height, listenerConfig, wrapperItemId) {
		var inputComp=this.createMultiSelect(itemId, name, modifiable, data, value, width, height, listenerConfig);
		var helpStr = getText(itemId+'.help');
		if(CWHF.isNull(helpStr)){
			return inputComp;
		}
		//return this.createWrapperHelp(inputComp,name,helpStr,otherSettings);
		if (CWHF.isNull(wrapperItemId)) {
			wrapperItemId = this.getWrapperItemId(inputComp.getItemId());
		}
		return Ext.create("com.trackplus.util.HelpWrapperComponent",
				{inputComp:inputComp, helpStr:helpStr, itemId: wrapperItemId});
	},*/


	/**
	 * Create a radiogroup component. The label will be localized.
	 * @param {String} labelKey The resource key for the label.
	 * @param {String} itemdId The itemId of the inner component.
	 * @param {Number} width The width of the group
	 * @param {Array} items The items of the radio group
	 * @param {JSONString} otherSettings Any other configuration parameters, e.g., {width:'200px', id:'myId'}
	 * @param {JSONString} listenerConfig The associated listeners for this component
	 * @param {String} itemId The itemId for the inner component.
	 * @return {Ext.form.RadioGroup} A radio group component.
	*/
	getRadioGroup:function(labelKey, width, items, otherSettings, listenerConfig) {
		var radioGroupConfig = {
				xtype	: 'radiogroup',
				layout: 'hbox',
				defaults : {margin:"0 5 0 0"},
				labelStyle:{overflow:'hidden'},
				//labelWidth:this.labelWidth,
				labelAlign:this.alignR,
				width: width
				//disabled: !modifiable
			}
		if (CWHF.isNull(items)) {
			items = [];
		}
		radioGroupConfig.items = items;
		if (listenerConfig) {
			radioGroupConfig.listeners = listenerConfig;
		}
		if (CWHF.isNull(labelKey)) {
			radioGroupConfig.fieldLabel = '';
			radioGroupConfig.labelSeparator = '';
			radioGroupConfig.labelWidth = 0;
		} else {
			radioGroupConfig.fieldLabel = this.getLabel(labelKey, otherSettings);
			radioGroupConfig.labelWidth = this.labelWidth;
		}
		this.addOtherSettings(radioGroupConfig, otherSettings);
		return Ext.create('Ext.form.RadioGroup', radioGroupConfig);
	},

	/**
	 * Create a radiogroup component with help. The label will be localized.
	 * @param {String} labelKey The resource key for the label.
	 * @param {String} itemdId The itemId of the inner component.
	 * @param {Number} width The width of the group
	 * @param {Array} items The items of the radio group
	 * @param {JSONString} otherSettings Any other configuration parameters, e.g., {width:'200px', id:'myId'}
	 * @param {JSONString} listenerConfig The associated listeners for this component
	 * @param {String} itemId The itemId for the inner component.
	 * @return {Ext.form.RadioGroup} A radio group component.
	*/
	getRadioGroupWithHelp:function(labelKey, width, items, otherSettings, listenerConfig, wrapperItemId) {
		var inputComp=this.getRadioGroup(labelKey, width, items, otherSettings, listenerConfig);
		var helpStr = getText(labelKey+'.help');
		if(CWHF.isNull(helpStr)){
			return inputComp;
		}
		//return this.createWrapperHelp(inputComp,itemId,helpStr,otherSettings);
		if (CWHF.isNull(wrapperItemId)) {
			wrapperItemId = this.getWrapperItemId(inputComp.getItemId());
		}
		return Ext.create("com.trackplus.util.HelpWrapperComponent",
				{inputComp:inputComp, helpStr:helpStr, itemId: wrapperItemId});
	},

	/**
	 * Add radio buttons to radio group
	 */
	getRadioButtonItems: function(list,
			commonName, id, label, checkedValue, disabled, labelIsLocalized) {
		var radioButtons = [];
		Ext.Array.each(list, function(listEntry) {
			radioButtons.push({
				name:commonName,
				inputValue: listEntry[id],
				boxLabel: this.getLabel(listEntry[label], {labelIsLocalized:labelIsLocalized}),
				checked: listEntry[id]===checkedValue,
				disabled: disabled
			});
		}, this);
		return radioButtons;
	},

	/**
	 * Gets the value of the selecetd radio button
	 */
	getSelectedRadioButtonValue:function(radioButtonGroup) {
		var checkedArr = radioButtonGroup.getChecked();
		var checkedRadioValue = null;
		if (checkedArr.length>0) {
			checkedRadio = checkedArr[0];
			if (checkedRadio) {
				checkedRadioValue = checkedRadio.getSubmitValue();
			}
		}
		return checkedRadioValue;
	},

	/**
	 * Create a localized label component.
	 * The resource help key is derived from the standard label key plus ".help".
	 * @param {String} fieldLabelKey The label key for this field, used for localization
	 * @param {Object} value the currently selected value
	 * @return {Object} The localized label component
	 */
	createLabelComponent:function(fieldLabelKey, name, otherSettings){
		var me=this;
		var label = this.getLabel(fieldLabelKey, otherSettings);
		var labelComp={
				xtype:'displayfield',
				fieldLabel:label,
				name: name,
				labelStyle:{overflow:'hidden'},
				labelWidth:me.labelWidth,
				labelAlign:me.alignR,
				fieldStyle : {
					height:'auto'
				}
		};
		this.addOtherSettings(labelComp, otherSettings);
		return labelComp;
	},


	/**
	 * Create a file upload component with proper sizing. The label will be localized.
	 * @param {String} labelKey The resource key for the label.
	 * @param {String} name The field name of the component.
	 * @param {String} otherSettings Any other configuration parameters, e.g., <code>{width:'200px', id:'myId'}</code>.
	 * @return {Ext.form.field.File} A file upload field
	 */
	createFileField: function(labelKey, name, otherSettings, listenerConfig) {
		var label = this.getLabel(labelKey, otherSettings);
		var fileFieldConfig = {
				xtype: 'filefield',
				fieldLabel : label,
				name: name,
				buttonText: getText("common.btn.browse"),
				labelWidth:this.labelWidth,
				labelAlign:this.alignR};
		if (listenerConfig) {
			fileFieldConfig.listeners = listenerConfig;
		}
		this.addOtherSettings(fileFieldConfig, otherSettings, label);
		return Ext.create('Ext.form.field.File', fileFieldConfig);
	},

	/**
	 * Create a multiple select combobox like component. The label will be localized.
	 * @param {String} labelKey The resource key for the label.
	 * @param {String} name The field name
	 * @param {array} data The data source
	 * @param {array} valus The selected values
	 * @param {JSONString} iconSettings settings for getting the icons dynamically
	 * @param {JSONString} otherSettings Any other configuration parameters, e.g., {width:'200px', id:'myId'}
	 * @param {JSONString} listenerConfig The associated listeners for this component
	*/
	createMultipleSelectPicker: function(labelKey, name, data, value, /*iconSettings,*/ otherSettings) {
		var label = this.getLabel(labelKey, otherSettings);
		var multipleSelectPickerConfig = {
				localizedLabel:label,
				name:name,
				options:data,
				value: value,
				width:200,
				margin:'0 5 0 0',
				useRemoveBtn:true
				}
		/*if (iconSettings) {
			var iconUrl = iconSettings.iconUrl;
			if (iconUrl) {
				//exclude from properties
				delete iconSettings.iconUrl;
				var valueName = iconSettings.valueName;
				if (valueName) {
					//exclude from for cycle
					delete iconSettings.valueName;
				}
				var parameterSeparator = "?";
				for (propertyName in iconSettings) {
					var propertyValue = iconSettings[propertyName];
					if (propertyValue) {
						iconUrl = iconUrl + parameterSeparator + propertyName + "=" + propertyValue;
						parameterSeparator = "&";
					}
				}
				if (valueName) {
					iconUrl = iconUrl + parameterSeparator + valueName + "="
				}
				multipleSelectPickerConfig.iconUrlPrefix = iconUrl;
			}
		}*/
		this.addOtherSettings(multipleSelectPickerConfig, otherSettings, label);
		/*if (listenerConfig) {
			multipleSelectPickerConfig.listeners = listenerConfig;
		}*/
		return Ext.create('com.trackplus.util.MultipleSelectPicker', multipleSelectPickerConfig);
	},

    /**
     * Create a single selectable tree like component. The label will be localized.
     * @param {String} labelKey The resource key for the label.
     * @param {String} name The field name
     * @param {array} data The data source
     * @param  value The selected value
     * @param {JSONString} otherSettings Any other configuration parameters, e.g., {width:'200px', id:'myId'}
     * @param {JSONString} listenerConfig The associated listeners for this component
     */
    createSingleTreePicker: function(labelKey, name, data, value, otherSettings, listenerConfig) {
        var label = this.getLabel(labelKey, otherSettings);
        var singleSelectPickerConfig = {
            fieldLabel:label,
            name:name,
            options:data,
            value: value,
            width:200,
            margin:'0 5 0 0',
            labelAlign:this.alignR,
            useRemoveBtn:false
        }
        this.addOtherSettings(singleSelectPickerConfig, otherSettings, label);
        if (listenerConfig) {
            singleSelectPickerConfig.listeners = listenerConfig;
        }
        return Ext.create('com.trackplus.util.SingleTreePicker', singleSelectPickerConfig);
    },

    /**
     * Create a single selectable tree like component. The label will be localized.
     * @param {String} labelKey The resource key for the label.
     * @param {String} name The field name
     * @param {array} data The data source
     * @param  value The selected value
     * @param {JSONString} otherSettings Any other configuration parameters, e.g., {width:'200px', id:'myId'}
     * @param {JSONString} listenerConfig The associated listeners for this component
     */
    createSingleTreePickerWithHelp: function(labelKey, name, data, value, otherSettings, listenerConfig) {
        var inputComp=this.createSingleTreePicker(labelKey, name, data, value, otherSettings, listenerConfig)
        var helpStr = getText(labelKey+'.help');
        if(CWHF.isNull(helpStr)){
            return inputComp;
        }
        wrapperItemId = this.getWrapperItemId(inputComp.getItemId());
        return Ext.create("com.trackplus.util.HelpWrapperComponent",
            {inputComp:inputComp, helpStr:helpStr, itemId: wrapperItemId/*, flexInputComp:true*/});
    },

	/**
	 * Create a multiple selectable tree like component. The label will be localized.
	 * @param {String} labelKey The resource key for the label.
	 * @param {String} name The field name
	 * @param {array} data The data source
	 * @param {array} valus The selected values
	 * @param {JSONString} iconSettings settings for getting the icons dynamically
	 * @param {JSONString} otherSettings Any other configuration parameters, e.g., {width:'200px', id:'myId'}
	 * @param {JSONString} listenerConfig The associated listeners for this component
	*/
	createMultipleTreePicker: function(labelKey, name, data, value, otherSettings, listenerConfig) {
		var label = this.getLabel(labelKey, otherSettings);
		var multipleSelectPickerConfig = {
                localizedLabel:label,
				name:name,
				options:data,
				value: value,
				width:200,
				margin:'0 5 0 0',
				useRemoveBtn:true
		};

        if (otherSettings && otherSettings.useNull) {
            multipleSelectPickerConfig.fieldLabel = label;
        }
		this.addOtherSettings(multipleSelectPickerConfig, otherSettings, label);
		if (listenerConfig) {
			multipleSelectPickerConfig.listeners = listenerConfig;
		}
		return Ext.create('com.trackplus.util.MultipleTreePicker', multipleSelectPickerConfig);
	},

	createMultipleTreePickerWithHelp: function(labelKey, name, data, value, otherSettings, listenerConfig, wrapperItemId) {
		var inputComp=this.createMultipleTreePicker(labelKey, name, data, value, otherSettings, listenerConfig);
		var helpStr = getText(labelKey+'.help');
		if(CWHF.isNull(helpStr)){
			return inputComp;
		}
		//return this.createWrapperHelp(inputComp,itemId,helpStr,otherSettings);
		if (CWHF.isNull(wrapperItemId)) {
			wrapperItemId = this.getWrapperItemId(inputComp.getItemId());
		}
		return Ext.create("com.trackplus.util.HelpWrapperComponent",
				{inputComp:inputComp, helpStr:helpStr, itemId: wrapperItemId});
	},

	isNull: function(variable) {
		if( typeof variable === 'undefined' || variable === null ){
			return true;
		}
		return false;
	},

	/**
	 * Creates a selectControl and possibly
	 */
	/*createSelectPanel: function(scope, selectPanelItems, selectControl, panelID ,title, controlIsTree,
			changeHandler, changeHandlerScope) {
		return Ext.create("Ext.panel.Panel", {
			itemId: panelID,
			border:	true,
			bodyBorder:false,
			bodyStyle:{
				border:'medium none'
			},
			margin:"0 5 0 0",
			//title: title,
			cls:'selectPanelWrp',
			//height: selectControl.height,
			//width: selectControl.width,
			tools: [{type: 'plus',
					scope: scope,
					tooltip: getText('common.btn.all'),
					handler: function(event, toolElement, owner, tool) {
						if (selectControl) {
							if (CWHF.isNull(controlIsTree) || !controlIsTree) {
								var store = selectControl.store;
								var values = [];
								store.each(function(record) {
									values.push(record.get("id"));
								});
								selectControl.setValue(values);
							} else {
								var rootNode = selectControl.getRootNode();
								rootNode.cascadeBy(function(currentNode) {
									if (currentNode.get('checked')) {
										currentNode.set('checked', true);
									}
								}, scope);
								if (changeHandler) {
									changeHandler.call(changeHandlerScope);
								}
							}
						}
					}
					},{
					type: 'minus',
					scope: scope,
					tooltip: getText('common.btn.reset'),
					handler: function(event, toolElement, owner, tool) {
						if (selectControl) {
							//although clearing the value triggers the selectProject method it will not go to the server because newValue is empty...
							if (CWHF.isNull(controlIsTree) || !controlIsTree) {
								if (selectControl.getValue() && selectControl.getValue().length!==0) {
									selectControl.clearValue();
								}
							} else {
								var rootNode = selectControl.getRootNode();
								rootNode.cascadeBy(function(currentNode) {
									if (currentNode.get('checked')) {
										currentNode.set('checked',  false);
									}
								}, scope);
								if (changeHandler) {
									changeHandler.call(changeHandlerScope);
								}
							}
						}
					}
				}],
			items: selectPanelItems
		})
	},*/
	msgCt:null,
	showMsgInfo: function(msg, isTopDownDirection){
		this.showMsg(msg,'info', isTopDownDirection);
	},
	showMsgError: function(msg){
		this.showMsg(msg,'error');
	},
	showMsg: function(msg,cls,isTopDownDirection){
		var divID = "msg-div";
		if(isTopDownDirection && isTopDownDirection!==undefined) {
			if(isTopDownDirection) {
				divID = "msg-div";
			}else {
				divID = "msg-div-bottom";
			}

		}

		if (!this.msgCt) {
			this.msgCt = Ext.DomHelper.insertFirst(document.body, {id: divID}, true);

		}else{
			if(this.msgCt.id !== divID) {
				this.msgCt.remove();
				this.msgCt = Ext.DomHelper.insertFirst(document.body, {id: divID}, true);
			}
		}

		var zIndex=null;
		var active=Ext.WindowManager.getActive();
		if(active){
			zIndex = parseInt(active.el.getStyle('zIndex'), 10);
		}
		if(zIndex){
			this.msgCt.setStyle('z-index',''+(zIndex+1));
		}
		var boxHtml='<div class="msg msg-'+cls+'"<p>' + msg + '</p></div>';
		var m = Ext.DomHelper.append(this.msgCt,boxHtml, true);
		m.hide();
		var direction = "t";
		if(isTopDownDirection && isTopDownDirection!==undefined) {
			if(!isTopDownDirection) {
				direction = "b";
			}
		}
		m.slideIn("t",{easing: 'ease', duration: 400}).ghost(direction, {delay: 3000, duration: 600, easing: 'ease', remove: true});
	},
	log1:function(text) {
		if(com.trackplus.TrackplusConfig.isDebugEnabled) {
			console.log(text);
		}
	},
	showDialogMsgWarning:function(title, message){
		Ext.Msg.show({
			title: title,
			message: message,
			buttonText:{'cancel':getText('common.btn.close')},
			buttons:Ext.Msg.CANCEL,
			icon: Ext.Msg.WARNING
		});
	}
},

/**
 * Global shortcut accessor for ControlWithHelpFactory
 */
function() {
	CWHF = this;

})




