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

com.trackplus.admin.Report = function() {
};


/**
 * Whether the filter contains extra parameter(s) beyond basic datasource
 */
com.trackplus.admin.Report.executeReport = function(scope, templateID, reportConfigNeeded, fromIssueNavigator, workItemIDs, projectID, dashboardID) {
	if (!reportConfigNeeded) {
		var url = 'reportDatasource.action?templateID='+templateID;
		if (fromIssueNavigator) {
			url = url+"&fromIssueNavigator="+fromIssueNavigator;
		}
		if (workItemIDs) {
			url = url+ "&workItemIDs="+workItemIDs;
		}
		if (projectID) {
			url = url+ "&projectID="+projectID;
		}
		if (dashboardID) {
			url = url+ "&dashboardID="+dashboardID;
		}
		window.open(url);
	} else {
		params = {templateID: templateID};
		if (fromIssueNavigator) {
			params["fromIssueNavigator"]=fromIssueNavigator;
		}
		if (workItemIDs) {
			params["workItemIDs"]=workItemIDs;
		}
		if (projectID) {
			params["projectID"]=projectID;
		}
		if (dashboardID) {
			params["dashboardID"]=dashboardID;
		}
		Ext.Ajax.request({
			url: 'reportDatasource!configPage.action',
			params: params,
			scope: scope,
			disableCaching:true,
			success: function(response){
				var data = Ext.decode(response.responseText);
				var configClass = data["configClass"];
				//show the xml datasource button only from report configuration (not when coming from issue navigator)
				var implementSerialization = !fromIssueNavigator && data["implementSerialization"];
				var configData = data["configData"];
				var configForm = Ext.create(configClass, configData);
				com.trackplus.admin.Report.showConfigPage(scope, templateID, configForm, implementSerialization);
			},
			failure: function(result){
				Ext.MessageBox.alert(scope.failureTitle, result.responseText);
			},
			method:"POST"
		});
	}
};

/**
 * Render the report datasource parameters
 */
com.trackplus.admin.Report.showConfigPage = function(scope, templateID, configForm, implementSerialization) {
	//var loadUrl = 'reportDatasource!getConfigPage.action';
	//var loadUrlParams = {templateID: reportID};
	//do not use getForm().load() because the form class to instantiate is specified with the form data in the same JSON result
	//getForm().load() could be used with two AJAX requests: first get the form name and instantiate then getForm().load()
	//this does not worth because then postDataProcess() should be called (like loading the combos)
	//which in this case can be made directly because data is available in the correspondingly initialized JSON result
	var load = {loadHandler:com.trackplus.admin.Report.loadConfigPage};
	var submitUrl = 'reportDatasource.action';
	var submitUrlParams = {
		templateID:templateID
	};
	var submitMessage=getText("admin.customize.reportTemplate.msg.waitForReportCreate");
	var submitExecute = {standardSubmit:true, submitUrl:submitUrl, submitUrlParams:submitUrlParams, submitButtonText:getText('common.btn.executeReport'),submitMessage:submitMessage};
	if (implementSerialization) {
		var submitDatasource = {standardSubmit:true, submitUrl:"reportDatasource!serializeDatasource.action",
				submitUrlParams:submitUrlParams, submitButtonText:getText('common.btn.XML'),
				submitMessage:submitMessage};
		submit = [submitExecute, submitDatasource];
	} else {
		submit = submitExecute;
	}

	var windowParameters = {title:getText("admin.customize.reportTemplate.lbl.configure"),
			width:450,
			height:450,
			load: load,
			submit: submit,
			cancelButtonText: getText('common.btn.done'),
			formPanel: configForm};
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
		windowConfig.showWindowByConfig(scope);
};

/**
 * Empty (but not null) function to avoid the calling of getForm().load() in windowConfig
 */
com.trackplus.admin.Report.loadConfigPage = function() {
};

/**
 * Executes a report from issue navigator: first choose report then render configuration (if needed)
 */
com.trackplus.admin.Report.executeReportFromIssueNavigator=function(workItemIDs) {
	var reportPicker = CWHF.createSingleTreePicker("admin.customize.reportTemplate.lblAlone",
	    "templateID", [], null,
	    {allowBlank:false,
	     labelWidth:100,
	     itemId: 'templateID'
	    });
	var load = {loadUrl:"reportDatasource!chooseReport.action"};
	var submitUrlParams = {
			fromIssueNavigator:true,
			workItemIDs: workItemIDs
		};
	var submit = {standardSubmit:true, submitUrlParams:submitUrlParams,
			submitButtonText:getText('common.btn.executeReport'),
			submitHandler:com.trackplus.admin.Report.submitReport};
	var windowParameters = {title:getText("admin.customize.reportTemplate.lbl.choose"),
			width:500,
			height:120,
	        load: load,
			submit: submit,
	        postDataProcess:com.trackplus.admin.Report.postDataProcess,
			items: [reportPicker]};
	var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
	windowConfig.showWindowByConfig(this);
};

com.trackplus.admin.Report.postDataProcess = function(data, formPanel) {
	var reportPicker = formPanel.getComponent("templateID");
	if (reportPicker) {
	    reportPicker.updateMyOptions(data["categoryTree"]);
	    reportPicker.setValue(data["templateID"]);
	}
};

com.trackplus.admin.Report.submitReport = function(window, submitUrl, submitUrlParams) {
	var theForm = this.formEdit;
	var templateID = theForm.getComponent("templateID").getSubmitValue();
	if (templateID) {
		Ext.Ajax.request({
			url: 'reportDatasource!hasDatasourcePlugin.action',
			params: {templateID:templateID},
			scope: this,
			disableCaching:true,
			success: function(response){
				var data = Ext.decode(response.responseText);
				var reportConfigNeeded = data["value"];
				com.trackplus.admin.Report.executeReport(this, templateID, reportConfigNeeded, true, submitUrlParams.workItemIDs);
			},
			failure: function(result){
				Ext.MessageBox.alert(scope.failureTitle, result.responseText);
			},
			method:"POST"
		});
		window.close();
	}
};
