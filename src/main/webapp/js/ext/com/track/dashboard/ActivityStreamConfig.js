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

Ext.define('js.ext.com.track.dashboard.ActivityStreamConfig',{
	extend: 'js.ext.com.track.dashboard.BaseConfig',
	//txtRefresh:null,
	createChildren:function(){
		var me=this;
		var items=me.callParent();
        var projectID=null;
        var width = 350;
        var labelAlign = 'right';
        var labelWidth = 150;
        var dateDaysWidth = 250;
        var numberWidth = 225;
        if(typeof(me.projectID) !== 'undefined' && me.projectID ) {
            projectID=me.projectID;
        }
        if(CWHF.isNull(projectID)){
            me.dataSourceType=com.trackplus.dashboard.createRadioGroupConfig(getText('common.datasource'),
                'params.selectedDatasourceType',me.jsonData.datasourceTypes,me.jsonData.selectedDatasourceType,me.datasourceTypeChanged,me);
            me.projectReleasePicker=com.trackplus.dashboard.createReleasePickerCfg("common.datasource.projectRelease",
                'params.selectedProjectOrRelease',me.jsonData.selectedProjectOrRelease);
            me.projectReleasePicker.setDisabled(me.jsonData.selectedDatasourceType!==1);
            me.filterPicker=com.trackplus.dashboard.createFilterPickerCfg("common.datasource.filter",'params.selectedQueryID',me.jsonData.selectedQueryID);
            me.filterPicker.setDisabled(me.jsonData.selectedDatasourceType===1);
            items.push({
                xtype: 'fieldset',
                itemId: 'fsDatasource',
                title: getText('common.datasource'),
                collapsible: false,
                defaultType: 'textfield',
                layout: 'anchor',
                items: [this.dataSourceType, this.projectReleasePicker, this.filterPicker]});
        }
        me.periodTypeCombo=com.trackplus.dashboard.createRadioGroupConfig(getText('common.timePeriod'),
            'params.selectedPeriodType',me.jsonData.periodTypes,me.jsonData.selectedPeriodType,me.periodTypeChanged,me);
        me.dateFrom = CWHF.createDateField("common.timePeriod.dateFrom", "params.dateFrom",
            {value:me.jsonData.dateFrom,
                labelWidth: labelWidth,
                submitFormat: com.trackplus.TrackplusConfig.DateFormat,
                width: dateDaysWidth});
        me.dateTo = CWHF.createDateField("common.timePeriod.dateTo", "params.dateTo",
            {value:me.jsonData.dateTo,
                labelWidth: labelWidth,
                submitFormat: com.trackplus.TrackplusConfig.DateFormat,
                width: dateDaysWidth});
        me.txtDaysBefore = CWHF.createNumberField("common.timePeriod.daysBefore", "params.daysBefore", 0, null, null,
            {value:me.jsonData.daysBefore,
               labelWidth: labelWidth,
               width: numberWidth,
               itemId:"paramsDaysBefore"});
        //fromTo=1,dateFrom=2
        me.dateFrom.setDisabled(me.jsonData.selectedPeriodType!==1);
        me.dateTo.setDisabled(me.jsonData.selectedPeriodType!==1);
        me.txtDaysBefore.setDisabled(me.jsonData.selectedPeriodType===1);
        items.push({
            xtype: 'fieldset',
            itemId: 'fsTimePeriod',
            title: getText('common.timePeriod'),
            collapsible: false,
            defaultType: 'textfield',
            layout: 'anchor',
            items: [me.periodTypeCombo, me.dateFrom, me.dateTo, me.txtDaysBefore]});
        me.changeType=CWHF.createCombo('activityStream.changeType','params.selectedChangeType',
            {multiSelect:true, labelWidth:labelWidth, width:width, data:me.jsonData.changeTypes, value:me.jsonData.selectedChangeType});
        me.changedByPerson = CWHF.createCombo('activityStream.changedByPerson','params.selectedChangedByPerson',
            {multiSelect:true, labelWidth:labelWidth,  width:width, data:me.jsonData.changedByPersons, value:me.jsonData.selectedChangedByPerson});
        me.gridView = Ext.create('Ext.form.Checkbox',{
            fieldLabel:getText('activityStream.gridView'),
            inputValue : true,
            labelWidth: labelWidth,
            labelAlign: labelAlign,
            //labelStyle:me.labelStyle,
            //labelSeparator:me.labelSeparator,
            name:'params.gridView',
            checked: me.jsonData.gridView
        });
        me.txtRefresh=CWHF.createNumberField("activityStream.refreshTime", "params.refresh", 0, null, null,
            {value:me.jsonData.refresh,
             minValue: 60,
             labelWidth: labelWidth,
             width: numberWidth,
             itemId:'paramsRefresh'});

		me.txtMaxIssues=CWHF.createNumberField('myFilterView.maxIssuesToShow','params.maxIssuesToShow',0,1,1000,
			{value:me.jsonData.maxIssuesToShow,
				labelWidth: labelWidth,
				width: numberWidth,
				itemId:'paramsMaxIssuesToShow'});
		items.push({
            xtype: 'fieldset',
            itemId: 'fsOtherSettings',
            title: getText('common.otherSettings'),
            collapsible: false,
            defaultType: 'textfield',
            layout: 'anchor',
            items: [me.changeType, me.changedByPerson, me.gridView, me.txtRefresh,me.txtMaxIssues]});
		return items;
	},

    datasourceTypeChanged:function(radioGroup, newValue, oldValue, options){
        var me=this;
        var checkedArr = radioGroup.getChecked();
        var checkedRadio;
        if (checkedArr.length===1) {
            checkedRadio = checkedArr[0];
            var value=checkedRadio.getSubmitValue();
            //project=1,query=2
            me.projectReleasePicker.setDisabled(value!==1);
            me.filterPicker.setDisabled(value===1);
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

	isValidParams:function(){
		var me=this;
		var valid=me.callParent();
		var refresh=me.txtRefresh.getValue();
		if(refresh&&parseInt(refresh)<60){
			valid=false;
			me.txtRefresh.markInvalid(me.txtRefresh.invalidText);
		}
		return valid;
	}
});
