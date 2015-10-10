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

Ext.define("js.ext.com.track.datasource.BasicDatasource",{
	extend: "Ext.form.Panel",
	border: false,
	items: [],
	autoScroll: true,
	config: {
		templateID: null,
		fromIssueNavigator: null,
		workItemID: null,
		dashboardID: null,
		dashboardProjectReleaseID: null,
		datasourceType: null,
		datasourceTypeName: null,
		datasourceTypeOptions: null,
		projectOrReleaseID: null,
        projectReleaseTree: null,
		projectOrReleaseIDName: null,
		filterID: null,
        filterTree: null,
		filterIDName: null
	},
	bodyStyle:{
		padding:'10px'
	},
	width: 350,
	labelWidth: 150,
	labelAlign: 'right',
	labelStyle: {overflow:'hidden'},
	datasourceRadioGroup: null,
	releasePicker: null,
	queryPicker: null,
	initComponent: function() {
		this.callParent();
		var datasourceDisabled = false;
		if (this.templateID!=null) {
			this.add(CWHF.createHiddenField("templateID", {value:this.templateID}));
		}
		if (this.fromIssueNavigator) {
			datasourceDisabled = true;
			this.add(CWHF.createHiddenField("fromIssueNavigator", {value:this.fromIssueNavigator}));
		}
		if (this.workItemID!=null) {
			datasourceDisabled = true;
			this.add(CWHF.createHiddenField("workItemID", {value:this.workItemID}));
		}
		//hidden fields for reports called from project/release specific dashboard
		if (this.dashboardID!=null) {
			this.add(CWHF.createHiddenField("dashboardID", {value:this.dashboardID}));
		}
		if (this.dashboardProjectReleaseID!=null) {
			this.add(CWHF.createHiddenField("projectID", {value:this.dashboardProjectReleaseID}));
			datasourceDisabled = true;
		}
		//datasource type
		var datasourceList = CWHF.getRadioButtonItems(this.datasourceTypeOptions,
				this.datasourceTypeName, 'id', 'label', this.datasourceType, false, false);
		this.datasourceRadioGroup = CWHF.getRadioGroup("datasourceType",
					"common.datasource", this.width, datasourceList,
					{disabled: datasourceDisabled,
					labelWidth: this.labelWidth,
					labelAlign: this.labelAlign,
					labelStyle: this.labelStyle},
					{change: {fn: this.datasourceTypeChanged, scope:this}});
		//this.add(this.datasourceRadioGroup);
		//project/release picker
        this.releasePicker = CWHF.createSingleTreePicker("common.datasource.projectRelease",
            this.projectOrReleaseIDName, this.projectReleaseTree, this.projectOrReleaseID,
            {allowBlank: false,
             disabled: datasourceDisabled || this.datasourceType!=1,
             labelWidth: this.labelWidth,
             labelAlign: this.labelAlign,
             labelStyle: this.labelStyle,
             anchor:'100%',
             margin:'0 0 5 0'
            });
		//this.add(this.releasePicker);
		//query picker
        this.queryPicker = CWHF.createSingleTreePicker("common.datasource.filter",
            this.filterIDName, this.filterTree, this.filterID,
            {allowBlank: false,
                disabled: datasourceDisabled || this.datasourceType==1,
                labelWidth: this.labelWidth,
                labelAlign: this.labelAlign,
                labelStyle: this.labelStyle,
                anchor:'100%',
                margin:'0 0 5 0'
            });
		//this.add(this.queryPicker);
        this.add( {
            xtype: 'fieldset',
            itemId: 'fsDatasource',
            title: getText('common.datasource'),
            collapsible: false,
            defaultType: 'textfield',
            layout: 'anchor',
            items: [this.datasourceRadioGroup, this.releasePicker, this.queryPicker]});

	},

	datasourceTypeChanged: function(radioGroup, newValue, oldValue, options){
		var checkedArr = radioGroup.getChecked();
		if (checkedArr.length==1) {
			var checkedRadio = checkedArr[0];
			var value=checkedRadio.getSubmitValue();
			//project=1,query=2
			this.releasePicker.setDisabled(value!=1);
			this.queryPicker.setDisabled(value==1);
		}
	}
});
