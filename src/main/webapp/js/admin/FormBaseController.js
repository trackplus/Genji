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

Ext.define("com.trackplus.admin.FormBaseController", {

	/**
	 * Message info for submits which may take longer
	 */
	//submitMessage: null,
	/**
	 * Whether a refresh of the caller view is needed after a successful submit
	 */
	refreshAfterSubmit: true,

	/**
	 * The submit handler function
	 * submitSuccessBehavior: config for success behavior:
	 *  refreshAfterSubmit: whether the refreshing the original view should be called after a successful submit
	 * successHandler: explicit success handler. Otherwise the view's refreshAfterSubmitHandler method is called
	 *
	 */
	submitHandler: function(button, submitUrl, extraSubmitParams, submitSuccessBehavior, successHandler, successHandlerScope) {
		var callerScope = this.getView().getCallerScope();
		var submitUrlParams = this.getView().getSubmitParams();
		if (extraSubmitParams) {
			for (submitParamName in extraSubmitParams) {
				submitUrlParams[submitParamName] = extraSubmitParams[submitParamName];
			}
		}
		var loading = true;
		var refreshAfterSubmit = true;
		if (submitSuccessBehavior!=null) {
			if (submitSuccessBehavior.loading === false) {
				loading = false;
			}
			if (submitSuccessBehavior.refreshAfterSubmit===false) {
				refreshAfterSubmit = false;
			}
		}
		/**
		 * The submit success handler
		 *  not specified as class level function to have access to button, callerScope etc
		 */
		var submitSuccessHandler = function(form, action) {
			if (loading) {
				this.getView().setLoading(false);
			}
			button.setDisabled(false);
			var result = action.result;
			if (result) {
				if (result.success) {
					//refresh after successful submit
					if (refreshAfterSubmit) {
						this.onSuccess(callerScope, result, successHandler, successHandlerScope);
					}

				} else {
					com.trackplus.util.showError(result);
				}
			}
		};
		/**
		 * The submit failure handler
		 *  not specified as class level function to have access to button, callerScope etc
		 */
		var submitFailureHandler = function(form, action) {
			if (loading) {
				this.getView().setLoading(false);
			}
			button.setDisabled(false);
			result = action.result;
			if (result) {
				var errorCode = result.errorCode;
				var title = result.title;
				if (errorCode && (errorCode===4 || errorCode===5)) {
					//4===NEED_CONFIRMATION
					if (errorCode===4) {
						var errorMessage = result.errorMessage;
						Ext.MessageBox.confirm(title,
							errorMessage,
							function(btn){
								if (btn==="no") {
									return false;
								} else {
									submitUrlParams["confirmSave"]=true;
									this.submitHandler(button, submitUrl, submitSuccessBehavior);
								}
							}, this);
					} else {
						//5===RECOMMENDED_REPLACE
						// if (errorCode===5) {
							//callerScope.recommendedReplace.call(callerScope, me, submit, result);
						// }
					}
				} else {
					errors = result.errors;
					if (errors) {
						//form control errors (with control itemIds)
						form.markInvalid(errors);
					} else {
						var errorMessage = result.errorMessage;
						if (errorMessage) {
							//only error message, no errorCode
							com.trackplus.util.showError(result);
						} else {
							com.trackplus.util.submitFailureHandler(form, action);
						}
					}
				}
			}
		};
		this.getView().setLoading(true);
		button.setDisabled(true);
		this.getView().getForm().submit({
			url: submitUrl,
			params: submitUrlParams,
			method: "POST",
			scope: this,
			success: submitSuccessHandler,
			failure: submitFailureHandler
		});

	},

	onSuccess: function(callerScope, result, onSuccessHandler, successHandlerScope) {
		//gather the view specific refresh parameters before and after submit
		var refreshParameters = this.getView().refreshParametersBeforeSubmit;
		if (CWHF.isNull(refreshParameters)) {
			refreshParameters = new Object();
		}
		var refreshParametersAfterSubmit = this.getView().getRefreshParametersAfterSubmit();
		if (refreshParametersAfterSubmit && result) {
			Ext.each(refreshParametersAfterSubmit, function(refreshAfterExecuteParmeter) {
				var parameterName = refreshAfterExecuteParmeter.parameterName;
				var fieldNameFromResult = refreshAfterExecuteParmeter.fieldNameFromResult;
				refreshParameters[parameterName]=result[fieldNameFromResult];
			})
		}
		if (CWHF.isNull(onSuccessHandler)) {
			//by default refresh the "parent" view which opened this dialog
			onSuccessHandler = this.getView().refreshAfterSubmitHandler;
			if (onSuccessHandler) {
				/*var refreshParameters = this.getView().refreshParametersBeforeSubmit;
				if (CWHF.isNull(refreshParameters)) {
					refreshParameters = new Object();
				}
				var refreshParametersAfterSubmit = this.getView().getRefreshParametersAfterSubmit();
				if (refreshParametersAfterSubmit && result) {
					Ext.each(refreshParametersAfterSubmit, function(refreshAfterExecuteParmeter) {
						var parameterName = refreshAfterExecuteParmeter.parameterName;
						var fieldNameFromResult = refreshAfterExecuteParmeter.fieldNameFromResult;
						refreshParameters[parameterName]=result[fieldNameFromResult];
					})
				}*/
				onSuccessHandler.call(callerScope, refreshParameters, result);
			}
		} else {
			if (successHandlerScope) {
				onSuccessHandler.call(successHandlerScope, refreshParameters, result);
			} else {
				onSuccessHandler.call(callerScope, refreshParameters, result);
			}
		}
	}
})
