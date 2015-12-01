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

Ext.define('com.trackplus.admin.ActionBase',{
	extend:'Ext.Base',
	
	/**
	 * The title for a failure message
	 */
	failureTitle:getText('common.err.failure'),

	/**
	 * The iconCls for the add button
	 */
	/*protected*/getAddIconCls: function() {
		return 'add';
	},
	/**
	 * The key for "add" button text
	 */
	/*protected*/getAddButtonKey: function() {
		return 'common.btn.add';
	},
	/**
	 * The title for "add" popup and "add" action tooltip
	 */
	/*protected*/getAddTitleKey: function() {
		return 'common.lbl.add';
	},

	/**
	 * The iconCls for the edit button
	 */
	/*protected*/getEditIconCls: function() {
		return 'edit';
	},
	/**
	 * The key for "edit" button text
	 */
	/*protected*/getEditButtonKey: function() {
		return 'common.btn.edit';
	},
	/**
	 * The title for "edit" popup and "edit" action tooltip
	 */
	/*protected*/getEditTitleKey: function() {
		return 'common.lbl.edit';
	},

	/**
	 * The iconCls for the copy button
	 */
	/*protected*/getCopyIconCls: function() {
		return 'copy';
	},
	/**
	 * The key for "copy" button text
	 */
	/*protected*/getCopyButtonKey: function() {
		return 'common.btn.copy';
	},
	/**
	 * The title for "copy" popup and "copy" action tooltip
	 */
	/*protected*/getCopyTitleKey: function() {
		return 'common.lbl.copy';
	},

	/**
	 * The iconCls for the delete button
	 */
	/*protected*/getDeleteIconCls: function() {
		return 'delete';
	},
	/**
	 * The key for "delete" button text
	 */
	/*protected*/getDeleteButtonKey: function() {
		return 'common.btn.delete';
	},
	/**
	 * The title for "delete" popup and "delete" action tooltip
	 */
	/*protected*/getDeleteTitleKey: function() {
		return 'common.lbl.delete';
	},
	/**
	 * The delete confirmation text (if it will be asked for confirmation)
	 */
	/*protected*/getRemoveWarningKey: function() {
		return 'common.lbl.removeWarning';
	}
});
