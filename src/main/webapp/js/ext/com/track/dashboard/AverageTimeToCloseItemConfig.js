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

Ext.define('js.ext.com.track.dashboard.AverageTimeToCloseItemConfig',{
	extend: 'js.ext.com.track.dashboard.BaseConfig',
	layout:'hbox',
	cmbProjects:null,
	cmbQueryType:null,
	cmbRepositoryType:null,
	cmbQueries:null,
	labelWidth: 175,
	componentWidth: 385,
	labelAlign: 'right',
	labelSeparator :':',
	labelStyle: {overflow:'hidden'},

	createChildren:function(){
		var me=this;
		var items=me.callParent();
		var projectID=null;
		if(typeof(me.projectID) !== 'undefined' && me.projectID ) {
			projectID=me.projectID;
		}

		if(items.length > 0) {
			var titleComp = items[0];
			me.chnageTitleCompSettings(titleComp);
		}

		if(CWHF.isNull(projectID)){
			var dataSourceType=com.trackplus.dashboard.createRadioGroupConfig(getText('common.datasource'),
				'params.selectedDatasourceType',me.jsonData.datasourceTypes,me.jsonData.selectedDatasourceType,me.datasourceTypeChanged,me);

			me.cmbProjects=com.trackplus.dashboard.createReleasePickerCfg("common.datasource.projectRelease",
				'params.selectedProjectOrRelease',me.jsonData.selectedProjectOrRelease);
			me.cmbProjects.setDisabled(me.jsonData.selectedDatasourceType!==1);

			me.cmbQueries=com.trackplus.dashboard.createFilterPickerCfg("common.datasource.filter",'params.selectedQueryID',me.jsonData.selectedQueryID);
			me.cmbQueries.setDisabled(me.jsonData.selectedDatasourceType===1);

			dsFieldSetItems = [dataSourceType, me.cmbProjects, me.cmbQueries];
			me.chnageComponentsWidth(dsFieldSetItems);
			var dsFieldSet = me.getGenericFieldSet(dsFieldSetItems, getText('common.datasource'));
			items.push(dsFieldSet);


		}

		timeSettingsFieldSetItems = me.getTimeSettingsFieldSetItems();
		me.chnageComponentsWidth(timeSettingsFieldSetItems);
		var timeSettingsFieldSet = me.getGenericFieldSet(timeSettingsFieldSetItems, getText('common.time.settings'));
		items.push(timeSettingsFieldSet);

		var additionalSettingsItems = me.getAdditionalSettingsItems();
		me.chnageComponentsWidth(additionalSettingsItems);
		var additionalSettingsFieldSet = me.getGenericFieldSet(additionalSettingsItems, getText('common.additional'));
		items.push(additionalSettingsFieldSet);


		me.txtYAxe=Ext.create('Ext.form.field.Number',{
			fieldLabel:getText('statusOverTime.prompt.yAxe'),
			labelAlign:me.labelAlign,
			labelStyle:me.labelStyle,
			labelSeparator:me.labelSeparator,
			name:'params.yAxe',
			value:me.jsonData.yAxe
		});

		var optionalSettingsFieldSetItems = [me.txtYAxe];
		me.chnageComponentsWidth(optionalSettingsFieldSetItems);
		var optionalSettingsFieldSet = me.getGenericFieldSet(optionalSettingsFieldSetItems, getText('common.optional'));
		items.push(optionalSettingsFieldSet);

		var panelW=Ext.create('Ext.panel.Panel',{
			flex:2,
			border: false,
			margin: '0 0 0 0',
			frame: false,
			collapsible:false,
			bodyPadding: 5,
			padding:0,
			layout:'anchor',
			items:items,
			id: 'panelW'
		});
		me.initComponents(panelW);
		return [panelW];
	},

	chnageComponentsWidth: function(items) {
		var me = this;
		for(var i in items){
			if(items[i] ) {
				items[i].labelWidth = me.labelWidth;
				items[i].width= me.componentWidth;
			}
		}
	},

	datasourceTypeChanged:function(radioGroup, newValue, oldValue, options){
		var me=this;
		var checkedArr = radioGroup.getChecked();
		var checkedRadio;
		if (checkedArr.length===1) {
			checkedRadio = checkedArr[0];
			var value=checkedRadio.getSubmitValue();
			//project=1,query=2
			me.cmbProjects.setDisabled(value!==1);
			me.cmbQueries.setDisabled(value===1);
		}
	},

	periodTypeChanged:function(radioGroup, newValue, oldValue, options){
		var me=this;
		var checkedArr = radioGroup.getChecked();
		var checkedRadio;
		if (checkedArr.length===1) {
			checkedRadio = checkedArr[0];
			var value=checkedRadio.getSubmitValue();
			//fromTo=1,dateFrom=2
			me.dateFrom.setDisabled(value!==1);
			me.dateTo.setDisabled(value!==1);
			me.txtDaysBefore.setDisabled(value===1);
		}
	},

	initComponents:function(panelW) {
	},

	getGenericFieldSet: function(items, label) {
		var genericFieldSet = {
			xtype: 'fieldset',
			title: label,
			collapsible: false,
			defaultType: 'textfield',
			layout: 'anchor',
			anchor: '100%',
			items: items,
			margin: '0 8 0 0'
		};
		return genericFieldSet;
	},

	chnageTitleCompSettings: function(titleComp) {
		var me = this;
		if(titleComp ) {
			titleComp.anchor = null;
			titleComp.labelWidt = me.labelWidth;
			titleComp.labelAlign = me.labelAlign;
			titleComp.labelStyle = me.labelStyle;
			titleComp.labelSeparator = me.labelSeparator;
			titleComp.width = 400;
		};
	},

	getTimeSettingsFieldSetItems: function() {
		var me = this;
		var items = [];
		var periodType=com.trackplus.dashboard.createRadioGroupConfig(getText('statusOverTime.prompt.periodType'),
				'params.selectedPeriodType',me.jsonData.periodTypes,me.jsonData.selectedPeriodType,me.periodTypeChanged,me);
		items.push(periodType);

		me.dateFrom=Ext.create('Ext.form.field.Date',{
			fieldLabel: getText('statusOverTime.prompt.dateFrom'),
			labelAlign:me.labelAlign,
			labelStyle:me.labelStyle,
			labelSeparator:me.labelSeparator,
			name: 'params.dateFrom',
			format:com.trackplus.TrackplusConfig.DateFormat,
			value:me.jsonData.dateFrom,
			allowBlank:true
		});
		items.push(me.dateFrom);

		me.dateTo=Ext.create('Ext.form.field.Date',{
			fieldLabel: getText('statusOverTime.prompt.dateTo'),
			labelAlign:me.labelAlign,
			labelStyle:me.labelStyle,
			labelSeparator:me.labelSeparator,
			name: 'params.dateTo',
			value:me.jsonData.dateTo,
			format:com.trackplus.TrackplusConfig.DateFormat,
			allowBlank:true
		});
		items.push(me.dateTo);

		me.txtDaysBefore=Ext.create('Ext.form.field.Number',{
			fieldLabel:getText('statusOverTime.prompt.daysBefore'),
			labelAlign:me.labelAlign,
			labelStyle:me.labelStyle,
			labelSeparator:me.labelSeparator,
			name:'params.daysBefore',
			value:me.jsonData.daysBefore
		});
		items.push(me.txtDaysBefore);

		me.cmbselRepInt = com.trackplus.dashboard.createSelectConfig(getText('burnDownChart.tooltip.reportingIntervalLabel'),
				'params.reportingInterval', me.jsonData.reportingInterval, me.jsonData.selectedReportingInterval);
		items.push(me.cmbselRepInt);
		//fromTo=1,dateFrom=2
		me.dateFrom.setDisabled(me.jsonData.selectedPeriodType!==1);
		me.dateTo.setDisabled(me.jsonData.selectedPeriodType!==1);
		me.txtDaysBefore.setDisabled(me.jsonData.selectedPeriodType===1);
		return items;
	},

	getAdditionalSettingsItems: function() {
		var me = this;
		items  = [];
		var statusSelectedFirstExtraConfig = [];
		statusSelectedFirstExtraConfig['margin'] = '5, 0, 5, 0';
		me.cmbStatuses=com.trackplus.dashboard.createMultiSelectConfig(getText('averageTimeToCloseItem.prompt.finalstatus'),
			'params.selectedStatus',me.jsonData.statuses,me.jsonData.selectedStatus, statusSelectedFirstExtraConfig);

		var statusSelecExtraConfig = [];
		statusSelecExtraConfig['width'] = 200;
		statusSelecExtraConfig['margin'] = '5, 0, 5, 20';


//		me.cmbselTimeFormat = com.trackplus.dashboard.createSelectConfig(getText('averageTimeToCloseItem.timeFormatLabel'),
//				'params.timeFormat', me.jsonData.timeFormat, me.jsonData.selectedTimeFormat, me.timeFormatChanged, me);
		me.timeFormatCheckBox = me.getTimeUnitSelector();

		me.responseTimeLimit = Ext.create('Ext.form.field.Number',{
			fieldLabel: getText('averageTimeToCloseItem.fieldLabel.respomse.time.limit'),
			labelAlign: me.labelAlign,
			labelStyle: me.labelStyle,
			labelSeparator: me.labelSeparator,
			name: 'params.responseTimeLimitValue',
			value: me.jsonData.responseTimeLimitValue  ? me.jsonData.responseTimeLimitValue : 10
		});

		items.push(me.timeFormatCheckBox);
		items.push(me.responseTimeLimit);
		items.push(me.cmbStatuses);
		return items;
	},

	getTimeUnitSelector: function() {
		var me = this;
		timeUnitSelector = {
			xtype: 'fieldcontainer',
	        fieldLabel: getText('averageTimeToCloseItem.timeFormatLabel'),
	        defaultType: 'radiofield',
	        labelAlign: me.labelAlign,
			labelStyle: me.labelStyle,
			labelSeparator: me.labelSeparator,
	        defaults: {
	        	flex: 1
	        },
	        layout: 'hbox',
	        items: [{
	        	boxLabel: getText('averageTimeToCloseItem.tooltip.time.format.working.day'),
	        	name: 'params.timeFormat',
	        	inputValue: 0,
	            id: 'radio1',
	            checked: me.jsonData.selectedTimeFormat === 0
	          },{
	        	  boxLabel: getText('averageTimeToCloseItem.tooltip.time.format.working.hours'),
	        	  name: 'params.timeFormat',
	        	  inputValue: 1,
	        	  id: 'radio2',
	        	  checked: me.jsonData.selectedTimeFormat === 1

	          }]
		};
		return timeUnitSelector;
	}

});
