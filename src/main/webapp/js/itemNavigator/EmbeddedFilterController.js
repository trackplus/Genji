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

Ext.define("com.trackplus.itemNavigator.EmbeddedFilterController",{
	extend: "Ext.app.ViewController",
	alias: "controller.embeddedFilter",
	
	/**
	 * Applies the filter
	 */
	onApply: function() {
		submitUrlParams = {instant:true,
				ajax:true,
				filterID:this.getView().getFilterID(),
				filterType:this.getView().getFilterType()};
		com.trackplus.admin.Filter.preSubmitProcessIssueFilter(submitUrlParams, this.getView());
		this.getView().setLoading(true);
		this.getView().getForm().submit({
			url: "savedFilterExecute!applyInstant.action",
			params: submitUrlParams,
			method: "POST",
			scope: this,
			success: function(form, action) {
				this.getView().setLoading(false);
				var result = action.result;
				if (result) {
					if (result.success) {
						this.getView().fireEvent.call(this.getView(),"applyFilter",{
							filterType: this.getView().getFilterType(),
							filterID: this.getView().getFilterID(),
							queryContextID: this.getView().getQueryContextID()
						});
					} else {
						com.trackplus.util.showError(result);
					}
				}
			},
			failure: function(form, action) {
				this.getView().setLoading(false);
				result = action.result;
				if (result) {
					var errorMessage = result.errorMessage;
					if (errorMessage) {
						//only error message, no errorCode
						com.trackplus.util.showError(result);
					} else {
						com.trackplus.util.submitFailureHandler(form, action);
					}
				}
			}
		});
	},

	/**
	 * Cleans the filter conditions
	 */
	onClean: function() {
		var params = {clearFilter:true};
		if (CWHF.isNull(this.getView().getFilterID()) || CWHF.isNull(this.getView().getFilterType())) {
			params.instant = true;
		} else {
			params.filterID = this.getView().getFilterID();
			params.filterType = this.getView().getFilterType();
			params.queryContextID = this.getView().getQueryContextID();
		}
		this.getView().setLoading(true);
		this.getView().getForm().load({
			url : "filterConfig!edit.action",
			params: params,
			scope: this,
			success: function(form, action) {
				this.getView().setLoading(false);
				var data = action.result.data;
				data.selectColumnCount = 7;
				//force to be modifiable
				data["modifiable"] = true;
				com.trackplus.admin.Filter.clearTreeFilter.call(this.getView(), this.getView(), data);
				this.getView().fireEvent.call(this.getView(),"clearFilter",{
					filterType: this.getView().getFilterType(),
					filterID: this.getView().getFilterID(),
					queryContextID: this.getView().getQueryContextID()
				});
			},
			failure: function(form, action) {
				this.getView().setLoading(false);
			}
		});

	},

	onSave: function() {
		var params = {};
		if (CWHF.isNull(this.getView().getFilterID()) || CWHF.isNull(this.getView().getFilterType())) {
			//params.instant = true;
			params.add = true;
		} else {
			params.filterID = this.getView().getFilterID();
			params.filterType = this.getView().getFilterType();
			params.queryContextID = this.getView().getQueryContextID();
		}
		com.trackplus.admin.Filter.preSubmitProcessIssueFilter(params, this);
		this.getView().setLoading(true);
		this.getView().getForm().submit({
			url: "filterConfig!save.action",
			params: params,
			method: "POST",
			scope: this,
			success: function(form, action) {
				this.getView().setLoading(false);
				var result = action.result;
				if (result) {
					if (result.success) {
						CWHF.showMsgInfo(getText("admin.customize.queryFilter.successSave"));
						//TODO reload content
					} else {
						CWHF.showMsgError(getText("admin.customize.queryFilter.errorSave"));
					}
				}
			},
			failure: function(form, action) {
				this.getView().setLoading(false);
				result = action.result;
				if (result) {
					var errorMessage = result.errorMessage;
					if (errorMessage) {
						//only error message, no errorCode
						com.trackplus.util.showError(result);
					} else {
						com.trackplus.util.submitFailureHandler(form, action);
					}
				}
			}
		});
	},

	onSaveAs: function() {
		var nameAndPathPicker=Ext.create("com.trackplus.util.NameAndPathPicker",{
			title:getText("common.btn.saveAs"),
			loadUrl: "categoryPathPicker.action",
			loadParams:  {categoryType:"issueFilter", add: true},
			//pathTree: data,
			handler:this.saveWithNameAndPath,
			//validateHandler: this.validateHandler,
			scope:this
		});
		nameAndPathPicker.showDialog();
	},

	saveWithNameAndPath:function(window, name, parentID, includeInMenu) {
		var submitParams = {node:parentID, label:name, includeInMenu:includeInMenu, add:true, viewID: this.getView().getViewID()};
		this.getView().getForm().submit({
			url: "filterConfig!save.action",
			params: submitParams,
			method: "POST",
			scope: this,
			success: function(form, action) {
				this.getView().setLoading(false);
				var result = action.result;
				if (result) {
					if (result.success) {
						CWHF.showMsgInfo(getText("admin.customize.queryFilter.successSave"));
						window.close();
						//TODO reload content
					} else {
						CWHF.showMsgError(getText("admin.customize.queryFilter.errorSave"));
					}
				}
			},
			failure: function(form, action) {
				this.getView().setLoading(false);
				result = action.result;
				if (result) {
					var errorMessage = result.errorMessage;
					if (errorMessage) {
						//only error message, no errorCode
						com.trackplus.util.showError(result);
					} else {
						com.trackplus.util.submitFailureHandler(form, action);
					}
				}
			}
		});
	}
});
