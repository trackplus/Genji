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

Ext.define("com.trackplus.admin.WindowBaseController", {

	/**
	 * Message info for submits which may take longer
	 */
	submitMessage: null,
	/**
	 * Whether a refresh of the caller view is needed after a successful submit
	 */
	refreshAfterSubmit: true,

	/**
	 * Whether to close the window after a successful submit
	 */
	closeAfterSubmit: true,

	/**
	 * Handler for canceling the window
	 */
	cancelHandler:function(button, refreshAfterCancel) {
		if (refreshAfterCancel) {
			this.onSuccess(this.getView().getCallerScope(), null);
		}
		this.getView().close();
	},

	/**
	 * process the submit parameters before submit
	 */
	preSubmitProcess: null,

	/**
	 * The submit handler function
	 * submitSuccessBehavior: config for success behavior:
	 *  refreshAfterSubmit: whether the refreshing the original view should be called after a successful submit
	 *  closeAfterSubmit: whether the window should be closed after a successful submit
	 * otherFormSettings: config applied to base form: like standardSubmit, timeOut etc.
	 * successHandler: explicit success handler. Otherwise the view's refreshAfterSubmitHandler method is called
	 *
	 */
	submitHandler: function(button, submitUrl, extraSubmitParams, submitSuccessBehavior, otherFormSettings, successHandler, successHandlerScope) {
		var callerScope = this.getView().getCallerScope();
		var submitUrlParams = this.getView().getSubmitUrlParams();
		if (extraSubmitParams) {
			for (submitParamName in extraSubmitParams) {
				submitUrlParams[submitParamName] = extraSubmitParams[submitParamName];
			}
		}
		var loading = true;
		var refreshAfterSubmit = true;
		var closeAfterSubmit = true;
		if (submitSuccessBehavior!=null) {
			if (submitSuccessBehavior.loading === false) {
				loading = false;
			}
			if (submitSuccessBehavior.refreshAfterSubmit===false) {
				refreshAfterSubmit = false;
			}
			if (submitSuccessBehavior.closeAfterSubmit===false) {
				closeAfterSubmit = false;
			}
		}
		/**
		 * The submit success handler
		 *  not specified as class level function to have access to button, callerScope etc
		 */
		var submitSuccessHandler = function(form, action) {
			if (loading) {
				this.getView().formPanel.setLoading(false);
			}
			button.setDisabled(false);
			var result = action.result;
			if (result) {
				if (result.success) {
					//refresh after successful submit
					if (refreshAfterSubmit) {
						this.onSuccess(callerScope, result, successHandler, successHandlerScope);
					}
					if (closeAfterSubmit) {
						this.getView().close();
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
				this.getView().formPanel.setLoading(false);
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
									this.submitHandler(button, submitUrl, extraSubmitParams, submitSuccessBehavior, otherFormSettings, successHandler, successHandlerScope);
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
			/*if(submit.specialSubmitFailHandler ) {
				submit.specialSubmitFailHandler.call(callerScope, result);
			}*/
		};
		/*if (this.validateHandler) {
			var valid = this.validateHandler(this.validateHandlerParams);
			if (!valid) {
				return;
			}
		}*/
		if (this.preSubmitProcess) {
			//add extra (dynamic client side) request parameters, right before submit
			submitUrlParams = this.preSubmitProcess(submitUrlParams, this.getView().formPanel/*, submit.submitAction*/);
		}
		var baseForm = this.getView().formPanel.getForm();
		for (propertyName in otherFormSettings) {
			baseForm[propertyName] = otherFormSettings[propertyName];
		}
		var standardSubmit = false;
		if (otherFormSettings && otherFormSettings.standardSubmit!=null) {
			standardSubmit = otherFormSettings.standardSubmit;
		}

		//if (CWHF.isNull(manualSubmitHandler)) {
			if (!standardSubmit && loading) {
				this.getView().formPanel.setLoading(true);
			}
			//this.disableSubmitButton(scope.win, true, submitButtonIndex);
			button.setDisabled(true);
			this.getView().formPanel.getForm().submit({
				url: submitUrl,
				params: submitUrlParams,
				method: "POST",
				scope: this,
				success: submitSuccessHandler,
				failure: submitFailureHandler
			});
		/*} else {
			//alternative submit: probably loads a store
			//or simply makes a classic submit with specific success handler
			//this handler might close the window
			manualSubmitHandler.call(scope, this.win, submitUrl, submitUrlParams, extraConfig);
		}*/

		if (standardSubmit && (/*this.getView().formPanel.getForm().monitor &&*/ this.getView().formPanel.getForm().isValid())) {
			//by standardSubmit the handlers onSuccess or onFailure are not called consequently the popup window would not be closed
			//1. if after a standardSubmit the result is another page, closing the popup does not hurt
			//because anyway the entire page is newly loaded (close the instant query popup after executing it)
			//2. if after a form submit the result should be a file download (for example by generating the report).
			//In this case a standardSubmit should be used because even if AJAX submit would be used the result is a stream so
			//the resulting data is not JSON containing a "success" field i.e. the submit handlers would not be called.
			//A download dialog will appear but the actual page remains consequently the popup should be closed here:
			if(this.submitMessage){
				CWHF.showMsgInfo(this.submitMessage);
			}
			Ext.defer(function(){
				//this.disableSubmitButton(scope.win, false, submitButtonIndex);
				button.setDisabled(false);
			}, 1000);
		}
	},


	onSuccess: function(callerScope, result, onSuccessHandler, successHandlerScope) {
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
