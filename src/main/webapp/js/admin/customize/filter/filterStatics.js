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

com.trackplus.filter = new Object();
com.trackplus.filter.executeFilter = function(scope, filterID, ajax) {
	Ext.Ajax.request({
		url: "filterParameters!containsParameter.action",
		params: {
			filterID: filterID
		},
		scope: scope,
		disableCaching:true,
		success: function(response){
			var data = Ext.decode(response.responseText);
			var containsParameter = data.success;
			if (containsParameter) {
				com.trackplus.filter.renderFilterParameter(filterID, ajax);
			} else {
				com.trackplus.filter.executeParameterlessFilter(filterID,ajax);
			}
		},
		failure: function(result){
			Ext.MessageBox.alert(scope.failureTitle, result.responseText);
		},
		method:"POST"
	});
};

com.trackplus.filter.renderFilterParameter= function(filterID, ajax) {
	var windowParameters = {
    	callerScope:this,
    	loadUrlParams: {filterID: filterID},
    	submitUrlParams: {filterID: filterID, ajax:ajax},
    	//refreshParametersBeforeSubmit: refreshParams,
    	//refreshParametersAfterSubmit: refreshParamsFromResult,
    	//TODO refreshAfterSubmitHandler if ajax
    	//refreshAfterSubmitHandler: refreshAfterSubmitHandler,
    	/*entityContext: {
    		//the last selected tree node
    		selectedTreeNode: this.getView().getLastSelectedTreeNode(),
    		//the config object passed to view
    		config: this.getView().getConfig(),
    		//the record data of the actually selected node/row
    		record: record,
    		//the selection is leaf
    		isLeaf: isLeaf,
    		//the operation is add
        	add: add,
        	//whether the operation refres to tree node or grid row
        	fromTree: fromTree,
        	//the name of the operation
        	operation: operation
    	}*/
    };
	var windowConfig = Ext.create("com.trackplus.admin.customize.filter.FilterParameter", windowParameters);
	windowConfig.showWindowByConfig(this);
};

com.trackplus.filter.executeParameterlessFilter = function(filterID,ajax) {
	var dummyForm = Ext.create("Ext.form.Panel", {
		items:[],
		url:"itemNavigator.action",
		baseParams: {
			queryType:1,//SAVED
			queryID: filterID,
			previousQuery:false,
			ajax:ajax
		},
		standardSubmit:CWHF.isNull(ajax)||ajax===false});
		dummyForm.getForm().submit();
};
