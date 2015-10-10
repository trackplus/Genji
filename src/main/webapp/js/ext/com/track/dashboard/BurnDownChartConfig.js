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

Ext.define('js.ext.com.track.dashboard.BurnDownChartConfig',{
	extend: 'js.ext.com.track.dashboard.BaseConfig',
	layout:'hbox',
	cmbProjects:null,
	cmbQueryType:null,
	cmbRepositoryType:null,
	cmbQueries:null,
	labelWidth: 175,
	componentWidth: 350,
	labelAlign: 'right',
	labelSeparator :':',
	labelStyle: {overflow:'hidden'},

	createChildren:function(){
		var me=this;
		var items=me.callParent();
		var projectID=null;
		if(typeof(me.projectID) !== 'undefined' && me.projectID != null) {
			projectID=me.projectID;
		}

		if(items.length > 0) {
			var titleComp = items[0];
			me.chnageTitleCompSettings(titleComp);
		}

		if(projectID==null){
			var dataSourceType=com.trackplus.dashboard.createRadioGroupConfig(getText('common.datasource'),
				'params.selectedDatasourceType',me.jsonData.datasourceTypes,me.jsonData.selectedDatasourceType,me.datasourceTypeChanged,me);

			me.cmbProjects=com.trackplus.dashboard.createReleasePickerCfg("common.datasource.projectRelease",
				'params.selectedProjectOrRelease',me.jsonData.selectedProjectOrRelease);
			me.cmbProjects.setDisabled(me.jsonData.selectedDatasourceType!=1);
			me.cmbProjects.anchor = null;

			me.cmbQueries=com.trackplus.dashboard.createFilterPickerCfg("common.datasource.filter",'params.selectedQueryID',me.jsonData.selectedQueryID);
			me.cmbQueries.setDisabled(me.jsonData.selectedDatasourceType==1);
			me.cmbQueries.anchor = null;

			var dsFieldSetItems = [dataSourceType, me.cmbProjects, me.cmbQueries];
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


		me.compHeight = Ext.create('Ext.form.field.Number',{
			fieldLabel:getText('statusOverTime.prompt.yAxe'),
			labelWidth:me.labelWidth,
			labelAlign:me.labelAlign,
			labelStyle:me.labelStyle,
			labelSeparator:me.labelSeparator,
			name:'params.yAxe',
			width:200,
			value:me.jsonData.yAxe
		});
		var optionalSettingsFieldSetItems = [me.compHeight];
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

	chnageComponentsLabelWidth: function(items) {
		var me = this;
		for(var i in items){
			if(items[i] != null) {
				items[i].labelWidth = me.labelWidth;
			}
		}
	},

	datasourceTypeChanged:function(radioGroup, newValue, oldValue, options){
		var me=this;
		var checkedArr = radioGroup.getChecked();
		var checkedRadio;
		if (checkedArr.length==1) {
			checkedRadio = checkedArr[0];
			var value=checkedRadio.getSubmitValue();
			//project=1,query=2
			me.cmbProjects.setDisabled(value!=1);
			me.cmbQueries.setDisabled(value==1);
		}
	},

	periodTypeChanged:function(radioGroup, newValue, oldValue, options){
		var me=this;
		var checkedArr = radioGroup.getChecked();
		var checkedRadio;
		if (checkedArr.length==1) {
			checkedRadio = checkedArr[0];
			var value=checkedRadio.getSubmitValue();
			//fromTo=1,dateFrom=2
			me.dateFrom.setDisabled(value!=1);
			me.dateTo.setDisabled(value!=1);
			me.txtDaysBefore.setDisabled(value==1);
		}
	},

	initComponents:function(panelW) {
	},

	effortTypeChanged: function(combo, newValue, oldValue, eOpts) {
		var me = this;
		if(newValue == me.jsonData.storyPointEffortTypeID) {
			me.customFieldForStoryPoint.setDisabled(false);
		}else {
			me.customFieldForStoryPoint.setDisabled(true);
		}
	},

	getGenericFieldSet: function(items, label) {
		var genericFieldSet = {
			xtype: 'fieldset',
			title: label,
			collapsible: false,
			defaultType: 'textfield',
			layout: 'anchor',
			items: items,
			margin: '0 8 0 0'
		};
		return genericFieldSet;
	},

	chnageTitleCompSettings: function(titleComp) {
		var me = this;
		if(titleComp != null) {
			titleComp.anchor = null;
			titleComp.labelWidt = me.labelWidth;
			titleComp.labelAlign = me.labelAlign;
			titleComp.labelStyle = me.labelStyle;
			titleComp.labelSeparator = me.labelSeparator;
			titleComp.width = 400;
		}
	},

	chnageComponentsWidth: function(items) {
		var me = this;
		for(var i in items){
			if(items[i] != null) {
				items[i].labelWidth = me.labelWidth;
				items[i].width= me.componentWidth;
			}
		}
	},

	getTimeSettingsFieldSetItems: function() {
		var me = this;
		var items = [];
		var periodType=com.trackplus.dashboard.createRadioGroupConfig(getText('statusOverTime.prompt.periodType'),
				'params.selectedPeriodType',me.jsonData.periodTypes,me.jsonData.selectedPeriodType,me.periodTypeChanged,me);
			items.push(periodType);

		me.dateFrom=Ext.create('Ext.form.field.Date',{
			fieldLabel: getText('statusOverTime.prompt.dateFrom'),
			labelWidth:me.labelWidth,
			labelAlign:me.labelAlign,
			labelStyle:me.labelStyle,
			labelSeparator:me.labelSeparator,
			name: 'params.dateFrom',
			format:com.trackplus.TrackplusConfig.DateFormat,
			value:me.jsonData.dateFrom,
			allowBlank:true,
			width: me.componentWidth
		});
		items.push(me.dateFrom);

		me.dateTo=Ext.create('Ext.form.field.Date',{
			fieldLabel: getText('statusOverTime.prompt.dateTo'),
			labelWidth:me.labelWidth,
			labelAlign:me.labelAlign,
			labelStyle:me.labelStyle,
			labelSeparator:me.labelSeparator,
			name: 'params.dateTo',
			value:me.jsonData.dateTo,
			format:com.trackplus.TrackplusConfig.DateFormat,
			allowBlank:true,
			width: me.componentWidth
		});
		items.push(me.dateTo);
		me.txtDaysBefore=Ext.create('Ext.form.field.Number',{
			fieldLabel:getText('statusOverTime.prompt.daysBefore'),
			labelWidth:me.labelWidth,
			labelAlign:me.labelAlign,
			labelStyle:me.labelStyle,
			labelSeparator:me.labelSeparator,
			name:'params.daysBefore',
			value:me.jsonData.daysBefore,
			width: me.componentWidth
		});
		items.push(me.txtDaysBefore);
		//fromTo=1,dateFrom=2
		me.dateFrom.setDisabled(me.jsonData.selectedPeriodType!=1);
		me.dateTo.setDisabled(me.jsonData.selectedPeriodType!=1);
		me.txtDaysBefore.setDisabled(me.jsonData.selectedPeriodType==1);

		me.cmbselRepInt = com.trackplus.dashboard.createSelectConfig(getText('burnDownChart.tooltip.reportingIntervalLabel'),
				'params.reportingInterval', me.jsonData.reportingInterval, me.jsonData.selectedReportingInterval);
		me.cmbselRepInt.width= me.componentWidth;
		items.push(me.cmbselRepInt);
		return items;
	},
	getAdditionalSettingsItems: function() {
		var me = this;
		var items = [];
		var statusSelectedFirstExtraConfig = [];
		statusSelectedFirstExtraConfig['margin'] = '5, 0, 5, 0';
		me.cmbStatuses=com.trackplus.dashboard.createMultiSelectConfig(getText('burnDownChart.prompt.finalstatus'),
			'params.selectedStatus',me.jsonData.statuses,me.jsonData.selectedStatus, statusSelectedFirstExtraConfig);
		me.cmbStatuses.width = 350;

		var statusSelecExtraConfig = [];
		statusSelecExtraConfig['width'] = 200;
		statusSelecExtraConfig['margin'] = '5, 0, 5, 20';

		items.push(me.cmbStatuses);

		me.effortType=com.trackplus.dashboard.createSelectConfig(getText('burnDownChart.tooltip.effortTypeCbLabel'),
				'params.effortType', me.jsonData.effortType, me.jsonData.selectedEffortType, me.effortTypeChanged, me);
		me.effortType.width = me.componentWidth;
		items.push(me.effortType);
		var customFieldForStoryPointSelectedValue = '';
		if(me.jsonData.selectedCustomFieldForStoryPoint == -1) {
			if (me.jsonData.customFieldForStoryPoint.length > 0) {
				customFieldForStoryPointSelectedValue = me.jsonData.customFieldForStoryPoint[0].id;
			}
		}else {
			customFieldForStoryPointSelectedValue = me.jsonData.selectedCustomFieldForStoryPoint;
		}

		me.customFieldForStoryPoint = com.trackplus.dashboard.createSelectConfig(getText('burnDownChart.tooltip.story.point.field.name'),
				'params.customFieldForStoryPoint', me.jsonData.customFieldForStoryPoint, customFieldForStoryPointSelectedValue);
		me.customFieldForStoryPoint.width = me.componentWidth;

		if(me.jsonData.storyPointEffortTypeID != me.jsonData.selectedEffortType) {
			me.customFieldForStoryPoint.setDisabled(true);
		}
		items.push(me.customFieldForStoryPoint);
		me.chnageComponentsLabelWidth(items);


		return items;

	}

});
