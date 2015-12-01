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


/**
 * Low level handler for request failure
 * @param form
 * @param action
 */
com.trackplus.util.requestFailureHandler = function(response, options) {
	var failureTitle = getText('common.err.failure');
	var message = "status: " + response.status + " statusText: " + response.statusText + " responseText: " + response.responseText;
	if(response.statusText&&response.statusText!==''){
		Ext.MessageBox.show({
			title: getText('common.err.failure'),
			msg: message,
			buttons: Ext.Msg.OK,
			icon: Ext.MessageBox.ERROR
		});
	}
}

/**
 * Handle the submit failure
 * @param form
 * @param action
 */
com.trackplus.util.submitFailureHandler = function(form, action) {
	var failureTitle = getText('common.err.failure');
	switch (action.failureType) {
	case Ext.form.action.Action.CLIENT_INVALID:
		Ext.Msg.alert(failureTitle, getText('common.err.failure.validate'));
		break;
	case Ext.form.action.Action.CONNECT_FAILURE:
		Ext.Msg.alert(failureTitle, getText('common.err.failure.ajax'));
		break;
	case Ext.form.action.Action.SERVER_INVALID:
		decodedResult = action.result;
		if (decodedResult) {
			com.trackplus.util.showError(decodedResult);
		} else {
			com.trackplus.util.requestFailureHandler(action.response);
		}
	}
}

/**
 * Show the errors after an ajax request() or submit()
 * result the decoded JSON result
 */
com.trackplus.util.showError = function(result) {
	if (result) {
		var message = "";
		var title = result['title'];
		var errorCode = result['errorCode'];
		var applicationErrorInfoFound = false;
		if (errorCode) {
			applicationErrorInfoFound = true;
			message += getText('common.err.failure.errorCode') + ": " + errorCode;
		}
		var errorMessage = result.errorMessage;
		if (errorMessage) {
			applicationErrorInfoFound = true;
			message += errorMessage;
		}
		var sucessMessage = result.message;
		if (sucessMessage) {
			applicationErrorInfoFound = true;
			message += sucessMessage;
		}

		if (CWHF.isNull(title)) {
			if (errorMessage || errorCode) {
				title = getText('common.err.failure');
			} else {
				title = getText('common.lbl.message');
			}
		}
		if (!applicationErrorInfoFound) {
			//no standard error fields found, show the entire result
			for (propertyName in result) {
				message += propertyName + " " + result[propertyName] + " ";
			}
		}
		Ext.MessageBox.show({
			title: title,
			msg: message,
			buttons: Ext.Msg.OK,
			icon: Ext.MessageBox.ERROR
		});
	}
}
