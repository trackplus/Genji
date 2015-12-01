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

Ext.define('js.ext.com.track.dashboard.VersionControlActivityConfig',{
	extend: 'js.ext.com.track.dashboard.BaseConfig',
	cmbProjects:null,
	txtCommits:null,
	createChildren:function(){
		var me=this;
		var items=me.callParent();
		me.cmbProjects=com.trackplus.dashboard.createReleaseTreeCfg("versionControlActivity.projects",
					"params.selectedProjects",me.jsonData.selectedProjects, {width:400, margin:'0 0 5 0'});

		items.push(me.cmbProjects);

		me.txtCommits=Ext.create('Ext.form.field.Number',{
			fieldLabel:getText('versionControlActivity.noOfCommits'),
			name:'params.commits',
			width:250,
			value:me.jsonData.commits
		});
		items.push(me.txtCommits);
		return items;
	}
});
