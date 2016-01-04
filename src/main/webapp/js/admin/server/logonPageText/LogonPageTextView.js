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

Ext.define('com.trackplus.admin.server.logonPageText.LogonPageTextView',{
	extend:'Ext.form.FormPanel',
	controller:'logonPageText',
	url:'logonPageText!save.action',
	region:'center',
	border:false,
	autoScroll: true,
	bodyStyle: 'padding:10px',

	listeners : {
		scope: 'controller',
		afterrender:'onAfterRender'
	},

	initComponent : function() {
		var cmbLocale=CWHF.createCombo("admin.server.motd.locale", "theLocale",
			{idType:"string", labelWidth:120, allowBlank:false,reference:'cmbLocale'},
			{select: 'onLocaleSelect'});
		var txtEditorTeaser =CWHF.createTextField("admin.server.motd.teaserText", "teaserText",
			{anchor:'100%', labelWidth:120});
		var htmlEditor = CWHF.createHtmlEditorField("admin.server.motd.message", "theMessage",
			{itemId:'theMessage',padding: '20 0 0 0', anchor:'100% -50', labelWidth:120});

		this.items=[cmbLocale, txtEditorTeaser, htmlEditor];
		this.dockedItems = [{
			xtype: "toolbar",
			dock: 'top',
			items: [
				Ext.create('Ext.button.Button',{
					text:getText('common.btn.save'),
					tooltip:getText('common.btn.save'),
					iconCls: 'save',
					handler:'onSave'
				})
			]
		}];
		this.callParent();
	}
});
