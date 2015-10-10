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

Ext.define('js.ext.com.track.dashboard.TwoDimensionalStatisticConfig',{
	extend: 'js.ext.com.track.dashboard.BaseConfig',
	cmbQuery:null,
	cmbFieldsH:null,
	cmbFieldsV:null,
	createChildren:function(){
		var me=this;
		var items=me.callParent();
		me.cmbQuery=com.trackplus.dashboard.createFilterPickerCfg("myFilterView.filter",'params.selFilter',
			me.jsonData.selFilter,{width:400});
		items.push(me.cmbQuery);
		me.cmbFieldsH=com.trackplus.dashboard.createSelectConfig(null,
			'params.selFieldH',me.jsonData.fieldsH,me.jsonData.selFieldH);
		items.push(me.createFieldGroup(getText('twoDimensionalStatistic.hfield'),
			me.cmbFieldsH,me.jsonData.sortBys,me.jsonData.selectedSortByH,
			me.jsonData.sortTypes,me.jsonData.selectedSortTypeH,"H"
		));

		me.cmbFieldsV=com.trackplus.dashboard.createSelectConfig(null,
			'params.selFieldV',me.jsonData.fieldsV,me.jsonData.selFieldV);
		items.push(me.createFieldGroup(getText('twoDimensionalStatistic.vfield'),
			me.cmbFieldsV,me.jsonData.sortBys,me.jsonData.selectedSortByV,
			me.jsonData.sortTypes,me.jsonData.selectedSortTypeV,"V"
		));

		return items;
	},
	createFieldGroup:function(label,cmbField,sortBys,sortByValue,sortTypes,sortTypeValue,fieldOrientation){
		var items=[];
		var nameSortBy='params.selectedSortBy'+fieldOrientation;
		var nameSortType='params.selectedSortType'+fieldOrientation;
		var container={
			xtype: 'fieldcontainer',
			fieldLabel:label ,
			labelWidth:100,
			anchor:'100%',
			layout: 'hbox',
			defaults: {
				//flex: 1,
				//hideLabel: true
			}
		};
		items.push(cmbField);
		items.push({
			xtype:'displayfield',
			fieldLabel:getText('twoDimensionalStatistic.sort'),
			labelStyle:{overflow:'hidden'},
			labelWidth:50,
			labelAlign:'right',
			value:'',
			width:55
		});
		for(var i=0;i<sortBys.length;i++){
			items.push({
				xtype:'radio',
				name:nameSortBy,
				inputValue: sortBys[i].id,
				boxLabel:sortBys[i].label,
				checked:sortBys[i].id==sortByValue,
				margin: '0 0 0 5'
			});
		}
		items.push({
			xtype:'displayfield',
			fieldLabel:'',
			hideLabel:true,
			value:'',
			width:10
		});
		for(var i=0;i<sortTypes.length;i++){
			items.push({
				xtype:'radio',
				name:nameSortType,
				inputValue: sortTypes[i].id,
				boxLabel:sortTypes[i].label,
				checked:sortTypes[i].id==sortTypeValue,
				margin: '0 0 0 5'
			});
		}
		container.items=items;
		return container;
	}

});
