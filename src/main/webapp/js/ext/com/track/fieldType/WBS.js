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

Ext.define('js.ext.com.track.fieldType.WBS', {
	extend: 'Ext.form.FieldSet',
	itemId: 'specificItem',
	hidden: true,
	items: [],
	onDataReady : function(data) {
		var projectSpecific = data['projectSpecific'];
		if (projectSpecific) {
			var button = Ext.create("Ext.button.Button", {
				text: getText("common.btn.renumber"),
				margins:'5 5 5 5',
				handler: function(button) {
					Ext.Ajax.request({
						url: 'fieldConfigItemDetail!executeCommand.action',
						params: {node:data['node']},
						success : function(response) {
							var result = Ext.decode(response.responseText);
							button.enable(true);
						},
						failure: function(response) {
							Ext.MessageBox.alert(getText('common.err.failure'), response.responseText);
							button.enable(true);
						}
					});
					button.disable(true);
				}
			});
			this.setVisible(projectSpecific);
			this.add(button);
		}
	}
});
