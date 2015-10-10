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

Ext.define('js.ext.com.track.dashboard.MyFilterConfig',{
	extend: 'js.ext.com.track.dashboard.BaseConfig',
	cmbQuery:null,
	createChildren:function(){
		var me=this;
		var items=me.callParent();
		me.cmbQuery=com.trackplus.dashboard.createFilterPickerCfg("myFilterView.filter",
			'params.selFilter',me.jsonData.selFilter,{width:400});
		items.push(me.cmbQuery);
		me.txtMaxIssues=Ext.create('Ext.form.field.Number',{
			fieldLabel:getText('myFilterView.maxIssuesToShow'),
			name:'params.maxIssuesToShow',
			width:250,
			value:me.jsonData.maxIssuesToShow
		});
		items.push(me.txtMaxIssues);

		return items;
	}
});
