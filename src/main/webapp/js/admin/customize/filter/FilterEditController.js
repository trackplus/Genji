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

Ext.define("com.trackplus.admin.customize.filter.FilterEditController", {
	extend: "Ext.app.ViewController",
	alias: "controller.filterEdit",
	mixins: {
		baseController: "com.trackplus.admin.WindowBaseController"
	},
	
	/**
	 * And instant filter can be executed directly
	 */
	onApplyInstant: function(button, event) {
		this.submitHandler(button, "savedFilterExecute!executeUnwrapped.action", null, null, null, this.executeInstantFilter);
	},
	
	/**
	 * Executes the last executed filter by refreshing th entire page
	 */
	executeInstantFilter: function(refreshParameters, result) {
		window.location.href = com.trackplus.TrackplusConfig.contextPath + "/itemNavigator.action";
	},
	
	/**
	 * A saved filter should be verified for parameters. If there are parameters in the query the parameter dialog should be popped up first
	 */
	onApplySaved: function(button, event) {
		this.submitHandler(button, "savedFilterExecute!unwrappedContainsParameter.action", null, null, null, this.executeUnwrappedFilter);
	},
	
	onReset: function(button, event) {
		this.submitHandler(button, "filterConfig!edit.action", {clearFilter: true}, {closeAfterSubmit:false}, null, this.clearFilter, this);
	},
	
	onSave:function(button, event) {
		var submitUrl = null;
		if (this.getView().isLeaf) {
			submitUrl =  "filterConfig!save.action";
		} else {
			submitUrl =  "categoryConfig!save.action";
		}
		this.submitHandler(button, submitUrl);
	},
	
	onCancel: function(button, event) {
		this.cancelHandler(button);
   },
	
	/**
	 * Method to add extra request parameters be sent to the sever before
	 * submitting the leaf data
	 */
   preSubmitProcess: function(submitUrlParams, panel) {
	    // is not TQL filter
	    if (this.getView().isIssueFilter() && (CWHF.isNull(this.getView().recordData) || this.getView().recordData.get("treeFilter") || add)) {
		    return com.trackplus.admin.Filter.preSubmitProcessIssueFilter(submitUrlParams, panel);
	    } else {
		    if (this.getView().isNotifyFilter()) {
			    return com.trackplus.admin.Filter.preSubmitProcessNotifyFilter(submitUrlParams, panel);
		    }
	    }
	},
	
	/**
	 * Whether the filter contains parameter(s)
	 */
	executeUnwrappedFilter: function(refreshParameters, result) {
	    containsParameter = result.value;
	    if (containsParameter) {
	    	var parentViewRecordID = this.getView().parentViewRecord.get("id");
	    	if (parentViewRecordID) {
		    	var lastIndex = parentViewRecordID.lastIndexOf("_");
			    var objectID = parentViewRecordID.substring(lastIndex + 1);
			    //this.renderFilterParameter(objectID);
			    com.trackplus.filter.renderFilterParameter(objectID, false);
	    	}
	    } else {
		    window.location.href = com.trackplus.TrackplusConfig.contextPath + "/itemNavigator.action";
	    }
	},

	/**
	 * Render the filter parameters
	 */
	/*renderFilterParameter: function(filterID) {
		var windowParameters = {
	    	callerScope:this,
	    	loadUrlParams: {filterID: filterID},
	    	submitUrlParams: {filterID: filterID},
	    	//refreshParametersBeforeSubmit: refreshParams,
	    	//refreshParametersAfterSubmit: refreshParamsFromResult,
	    	//refreshAfterSubmitHandler: refreshAfterSubmitHandler,
        };
		var windowConfig = Ext.create("com.trackplus.admin.customize.filter.FilterParameter", windowParameters);
		windowConfig.showWindowByConfig(this);
	},
	*/
	
	clearFilter: function(result) {
		com.trackplus.admin.Filter.clearTreeFilter(this.getView().formPanel, result.data);
	}
});
