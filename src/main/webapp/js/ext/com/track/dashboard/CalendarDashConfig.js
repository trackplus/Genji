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

Ext.define('js.ext.com.track.dashboard.CalendarDashConfig',{
	extend: 'js.ext.com.track.dashboard.BaseConfig',
	layout:'anchor',
	cmbProjects:null,
	cmbQueryType:null,
	cmbRepositoryType:null,
	cmbQueries:null,
	labelWidth:100,
	labelAlign: 'right',
	labelSeparator :':',
	labelStyle:{overflow:'hidden'},
	createChildren:function(){
		var me=this;
		var items=me.callParent();
		var projectID=null;
		if(typeof(me.projectID) !== 'undefined' && me.projectID ) {
			projectID=me.projectID;
		}
		if(CWHF.isNull(projectID)){
			var dataSourceType=com.trackplus.dashboard.createRadioGroupConfig(getText('common.datasource'),
				'params.selectedDatasourceType',me.jsonData.datasourceTypes,me.jsonData.selectedDatasourceType,me.datasourceTypeChanged,me);
			items.push(dataSourceType);

			me.cmbProjects=com.trackplus.dashboard.createReleasePickerCfg("common.datasource.projectRelease",
				'params.selectedProjectOrRelease',me.jsonData.selectedProjectOrRelease,{anchor:'100%'});
			me.cmbProjects.setDisabled(me.jsonData.selectedDatasourceType!==1);
			items.push(me.cmbProjects);

			me.cmbQueries=com.trackplus.dashboard.createFilterPickerCfg("common.datasource.filter",'params.selectedQueryID',
				me.jsonData.selectedQueryID,{anchor:'100%'});
			me.cmbQueries.setDisabled(me.jsonData.selectedDatasourceType===1);
			items.push(me.cmbQueries);
		}
		var cmbViewMode=Ext.create('Ext.form.ComboBox',{
			fieldLabel:null,
			name:"params.view",
			store: Ext.create('Ext.data.Store', {
				data	:[
					{id:'week',label:getText("CalendarDash.weekBtn")},
					{id:'month',label:getText("CalendarDash.monthBtn")}

				],
				fields	: [{name:'id', type:'string'}, {name:'label', type:'string'}],
				autoLoad: false
			}),
			border:false,
			bodyBorder:false,
			displayField: 'label',
			valueField: 'id',
			queryMode: 'local',
			value:me.jsonData.view,
			margin:'15 0 0 155',
			width:200,
			labelAlign:'right'
		});
		cmbViewMode.setValue(me.jsonData.view);
		items.push(cmbViewMode);
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
			me.cmbProjects.setDisabled(value!==1);
			me.cmbQueries.setDisabled(value===1);
		}
	}
});
