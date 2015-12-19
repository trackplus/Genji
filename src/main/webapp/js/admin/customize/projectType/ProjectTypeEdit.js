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


Ext.define("com.trackplus.admin.customize.projectType.ProjectTypeEdit",{
	extend: "com.trackplus.admin.FormBase",
	xtype: "projectTypeEdit",
    controller: "projectTypeEdit",
    
	baseServerAction: "projectType",
	
	node: null,
	addProjectType: false,
	
	labelWidth:250,
	textFieldWidth:250+300,
	textFieldWidthShort:250+70,
	alignR:"right",
	FieldSetWidth:250+300+150,
	
	
	actionSave: null,
	actionExport: null,
	
	/**
	 * Initialization method
	 */
	/*protected*/initBase : function() {
		var entityContext = this.getEntityContext();
		this.node = entityContext.node;
		this.addProjectType = entityContext.add;
		this.initActions();
	},
	
	loadUrl: "projectType!edit.action",
	
	getLoadParams: function() {
		var params = new Object();
		if (this.node) {
			params["node"]=this.node.get("id");
		}
		params["add"]=this.addProjectType;
		return params;
	},
	
	getSubmitParams: function() {
		var params = new Object();
		if (this.node) {
			params["node"]=this.node.get("id");
		}
		params["add"]=this.addProjectType;
		return params;
	},
	
		
	initActions: function() {
		this.actionSave = CWHF.createAction(this.getSaveButtonKey(), this.getSaveIconCls(),
				"onSave", {tooltip:this.getActionTooltip(this.getSaveTitleKey())});
		this.actions = [this.actionSave];
	},
	
	getFormFields: function() {
		var projectTypeID = null;
		if (this.node) {
			projectTypeID = this.node.get("projectTypeID");
		}
		var hideDetail = !this.addProjectType && projectTypeID && projectTypeID<0;
		var items = [CWHF.createTextField('common.lbl.name', 'projectTypeTO.label',
			{allowBlank:false, labelWidth:this.labelWidth, width:this.textFieldWidth})];
		if (!hideDetail) {
			items.push(CWHF.createComboWithHelp('admin.customize.projectType.lbl.projectTypeFlag',
					'projectTypeTO.projectTypeFlag', {itemId: 'projectTypeFlag', labelWidth:this.labelWidth}));
			items.push(CWHF.createCheckboxWithHelp('admin.customize.projectType.lbl.enableRelease', 'projectTypeTO.useReleases'));
			items.push(CWHF.createCheckboxWithHelp('admin.customize.projectType.lbl.enableVersionControl','projectTypeTO.useVersionControl'));
		}
		if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
			items.push(CWHF.createCheckboxWithHelp('admin.customize.projectType.lbl.enableAccounting', 'projectTypeTO.useAccounts',
					null, {change: "onAccountingChange"}));
			items.push(CWHF.createNumberFieldWithHelp('common.lbl.hoursPerWorkday',
					'projectTypeTO.hoursPerWorkday', 2, 0, 24,
					{labelWidth:this.labelWidth, width:this.textFieldWidthShort, itemId:'projectTypeTOHoursPerWorkday'}));
			items.push(CWHF.createComboWithHelp('admin.project.lbl.defaultWorkUnit',
					'projectTypeTO.defaultWorkUnit', {itemId: 'defaultWorkUnit',labelWidth:this.labelWidth}));
			items.push(CWHF.createTextFieldWithHelp("admin.project.lbl.currencyName", "projectTypeTO.currencyName",
					{labelWidth:this.labelWidth, width:this.textFieldWidthShort,itemId:'projectTypeTOCurrencyName'}));
			items.push(CWHF.createTextFieldWithHelp("admin.project.lbl.currencySymbol", "projectTypeTO.currencySymbol",
					{labelWidth:this.labelWidth, width:this.textFieldWidthShort,itemId:'projectTypeTOCurrencySymbol'}));
			if (com.trackplus.TrackplusConfig.appType !== APPTYPE_DESK) {
				items.push(CWHF.createCheckboxWithHelp("admin.customize.projectType.lbl.enableMsProjectExportImport",
						"projectTypeTO.useMsProjectExportImport", {labelWidth:this.labelWidth, width:this.textFieldWidthShort}));
			}
		}
		return [{xtype: 'fieldset',
				itemId: 'projectTypeFS',
				width: this.FieldSetWidth,
				style:{marginTop: '6px'},
				title: getText('admin.customize.projectType.fieldset'),
				items:items
				}];
	},
	
	postDataProcess: function(data, panel) {
		var projectTypeFlag = this.getWrappedControl("projectTypeFS", "projectTypeFlag");
		if (projectTypeFlag) {
			projectTypeFlag.store.loadData(data['projectTypeFlagList']);
			projectTypeFlag.setValue(data['projectTypeTO.projectTypeFlag']);
		}
		var projectTypeFlag = this.getWrappedControl("projectTypeFS", "defaultWorkUnit");
		if (projectTypeFlag) {
			projectTypeFlag.store.loadData(data['workUnitList']);
			projectTypeFlag.setValue(data['projectTypeTO.defaultWorkUnit']);
		}
		//if (CWHF.isNull(data["forPrivateProjects"]) || data["forPrivateProjects"]===false) {
			this.enableAccountingFields(data["projectTypeTO.useAccounts"]);
		//}
	},

	enableAccountingFields: function(enableAccounting) {
		this.getHelpWrapper("projectTypeFS", "projectTypeTOHoursPerWorkday").setDisabled(!enableAccounting);
		this.getHelpWrapper("projectTypeFS", "defaultWorkUnit").setDisabled(!enableAccounting);
		this.getHelpWrapper("projectTypeFS", "projectTypeTOCurrencyName").setDisabled(!enableAccounting);
		this.getHelpWrapper("projectTypeFS", "projectTypeTOCurrencySymbol").setDisabled(!enableAccounting);
	},

	
	getWrappedControl: function() {
		return CWHF.getWrappedControl.apply(this, arguments);
	},

	getHelpWrapper: function() {
		return CWHF.getHelpWrapper.apply(this, arguments);
	}

});
