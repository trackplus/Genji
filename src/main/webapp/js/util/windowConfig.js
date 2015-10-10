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

Ext.define('com.trackplus.util.WindowConfig',{
	extend:'Ext.Base',
	config: {
		/**
		 * The title of the popup window
		 */
		title:"",
		/**
		 * The width of the popup window
		 */
		width:400,
		/**
		 * The height of the popup window
		 */
		height:300,
		/**
		 * the configuration object for window:
		 * this should contain extra configuration properties for the opened window
		 * (except width, height, and title: they can be specified directly)
		 */
		windowConfig: null,
		/**
		 * the configuration object for the main panel:
		 * this should contain configuration properties for the main panel. Used together with items
		 */
		panelConfig: null,
		/**
		 * the form panel is configured with this items array
		 */
		items:null,
		/**
		 * the formPanel loaded/submitted: alternative to (items:...,panelConfig...): a ready made panel
		 */
		formPanel:null,
		//the load object contains:
		//1. loadUrl: url the load is made from
		//2. loadUrlParams: load parametersc
		//3. isFormPanel: whether the edit form is form panel (load/submit)
		//4. loadHandler: alternative load for edit form (if isFormPanel is false)
		load:null,
		//the submit object contains:
		//1. submitUrl: url the submit is sent to
		//2. submitUrlParams: extra parameters for submit
		//3. submitButtonText: the label for submit button (defaults to the label keyed by 'common.btn.save')
		//4. refreshAfterSubmit: true if not specified, if false the refresh is not called in the submit handler
		//5. closeAfterSubmit: true if not specified, if false the window is not closed in the submit handler
		//6. submitHandler: an alternative submit handler used when isFormPanel is false (probably manual submit of a grid)
		//	refreshAfterSubmit and closeAfterSubmit has no relevance if submitHandler is specified
		//7. submitAction: the action for submit ("add", "edit", "copy", "overwrite"). Not required
		//if there are more actions which need a submit the submit object can be an array of such submit objects
		submit:null,
		//the submit method
		method:"POST",
		//the refresh object contains:
		//1. refreshAfterSubmitHandler: the function to call after the submit (save or delete) operation was made
		//2. refreshParametersBeforeSubmit: refresh parameters known before submit (save or delete): (for example 'nodeIDToReload')
		//3. refreshParametersAfterSubmit: json object array like
		//			[{parameterName:'<refreshParameterName>', fieldNameFromResult:'<fieldNameFromResult>'}, ...]
		//			refresh parameters known only after submit (save or delete). The values are get after save or delete,
		//			from the result json, see submitHandler().
		//			<refreshParameterName> is typically either: 'nodeIDToSelect' (the tree node to select after branch reload) or
		//									'rowToSelect': the grid row to select after grid store reload
		//refresh:null,
		//function to call after the json data to be loaded in the window has arrived from the server
		//(like loading combo data source, show/hide controls depending on data)
		postDataProcess:null,
		//function to call before form submit like adding extra dynamic submit parameters depending on the prepared data
		//(add to submitUrlParams)
		preSubmitProcess:null,
		cancelButtonText: null, //the text for the cancel button
		submitButtonText: null,
		refreshAfterCancel: false, //whether a refresh should be made by closing the window (typically a refresh is made immediately after submits, not here)
		/**
		 * Whether it is a file upload form
		 */
		fileUpload:false,
		/**
		 * extra configuration for the context of the operation (like operation started from tree or from grid whether the the node is leaf)
		 * used mainly for identifying the correct entity labels
		 */
		extraConfig:null,

		overrideButtons: null
	},

	constructor: function(config) {
		var me = this
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	},
	/**
	 * Text for failure
	 */
	failureTitle:getText('common.err.failure'),
	/**
	 * The window to be opened
	 */
	win: null,
	/**
	 * label for the cancel button
	 */
	getCancelButtonText: function() {
		if (this.cancelButtonText!=null) {
			return this.cancelButtonText;
		} else {
			return getText('common.btn.cancel');
		}
	},

	/**
	 * label for save button
	 */
	getSubmitButtonText: function(submit) {
		if (this.submitButtonText!=null) {
			submit.submitButtonText = this.submitButtonText;
		}
		if (submit.submitButtonText!=null) {
		    return submit.submitButtonText;
		} else {
			return getText('common.btn.save');
		}
	},

    getButtonDisabled: function(submit) {
        if (submit!=null && submit.disabled!=null)   {
           return submit.disabled;
        }
        return false;
    },

	/**
	 * Handler for canceling the window
	 */
	cancelHandler:function(scope, submit) {
		if(submit!=null && submit.specialSubmitFailHandler != null) {
			submit.specialSubmitFailHandler.call(scope);
		}
		if (this.refreshAfterCancel) {
			this.refreshHandler(scope, null, submit);
		}
		this.win.close();
	},

	/**
	 * Shows the popup window
	 */
	showWindow: function(scope, title, width, height,
			load, submit, /*refresh,*/ windowItems) {
		this.title=title;
		this.width=width;
		this.height=height;
		this.ensureSize();
		this.items = windowItems;
		this.load = load;
		this.submit = submit;
		//this.refresh = refresh;
		this.showWindowByConfig(scope);
	},
	setLoading:function(b){
		var me=this;
		if(me.win!=null){
			me.win.setLoading(b);
		}
	},
	ensureSize:function(){
		var me=this;
		var size=borderLayout.ensureSize(me.width,me.height);
		me.width=size.width;
		me.height=size.height;
	},
	/**
	 * Shows the window according to the windowConfig
	 */
	showWindowByConfig: function(scope) {
		var me = this;
		if(me.win!=null){
			me.win.destroy();
		}
		me.ensureSize();
		if (me.items!=null || me.formPanel!=null) {
			if (me.items!=null) {
				//the formPanel is made here according to the items
				var panelConfig = {
						fileUpload:	me.fileUpload,
						//bodyStyle:	'padding: 5px',
						border: false,
						autoScroll:true,
						margin: '0 0 0 0',
						bodyStyle:{
							padding:'10px'
						},
						/*style:{
							borderBottom:'1px solid #D0D0D0'
						},*/
						defaultType:'textfield',
						defaults: {
							labelStyle:'overflow: hidden;',
							labelWidth:150,
							msgTarget:	'under',
							anchor:'100%'
						},
						method: "POST",
						items : me.items
					};
				if (me.panelConfig!=null) {
					//add extra panel configuration
					for (propertyName in me.panelConfig) {
						panelConfig[propertyName] = me.panelConfig[propertyName];
					}
				}
                if (me.submit!=null && me.submit.timeout!=null)  {
                    panelConfig.timeout = me.submit.timeout;
                }
				me.formPanel = Ext.create('Ext.form.Panel', panelConfig);
			}
			scope.formEdit = me.formPanel;
			var buttons = [];
			var submit = me.submit;
			if(me.overrideButtons==null) {
				if (submit!=null) {
					var submitByEnter = null;
					//var submitHandler_submitButtonIndex;
					if (submit instanceof Array) {
						//more than one submit: for example a save and an execute
						//for (i=0;i<submit.length;i++) {
						if (submit[0]!=null) {
							submitByEnter = submit[0];
							buttons.push({text: me.getSubmitButtonText(submit[0]),
	                            disabled: me.getButtonDisabled(submit[0]),
								scope: scope,
								handler : function(){
									me.submitHandler(submit[0], scope, 0, me.extraConfig);
									}
								})

						}
						if (submit[1]!=null) {
							buttons.push({text: me.getSubmitButtonText(submit[1]),
	                            disabled: me.getButtonDisabled(submit[1]),
								scope: scope,
								handler  : function(){
									me.submitHandler(submit[1], scope, 1, me.extraConfig);
									}
								})
	                    }
	                    if (submit[2]!=null) {
	                            buttons.push({text: me.getSubmitButtonText(submit[2]),
	                                disabled: me.getButtonDisabled(submit[2]),
	                                scope: scope,
	                                handler  : function(){
	                                    me.submitHandler(submit[2], scope, 2, me.extraConfig);
	                                }
	                            })
						}
	                    if (submit[3]!=null) {
	                        buttons.push({text: me.getSubmitButtonText(submit[3]),
	                            disabled: me.getButtonDisabled(submit[3]),
	                            scope: scope,
	                            handler  : function(){
	                                me.submitHandler(submit[3], scope, 2, me.extraConfig);
	                            }
	                        })
					}
					} else {
						submitByEnter=submit;
						//submitHandler_submitButtonIndex=0;
						buttons = [{text: me.getSubmitButtonText(submit),
	                                disabled: me.getButtonDisabled(submit),
									scope: scope,
									handler: function(){
										me.submitHandler(submit, scope, 0, me.extraConfig);
									}
								}]
					}
					if (me.formPanel!=null) {
						me.formPanel.addListener('afterRender',function(thisForm, options){
							this.keyNav = Ext.create('Ext.util.KeyNav',{
								target:this.el,
								scope:this,
								enter: {
									fn:function(e){
										var target = e.getTarget();
										if(target['type']=='textarea'){
											return true;
										}
										me.submitHandler(submitByEnter, scope, 0, me.extraConfig);
									},
									defaultEventAction:false
								}
							});
						});
					}

				}
				buttons.push({text: me.getCancelButtonText(),
					scope: scope,
					handler: function(){
						me.cancelHandler(scope, submit);
					}
				});
			}else {
				buttons = me.overrideButtons;
			}
			//show window
			if (me.formPanel!=null) {
				var windowConfig = {
					title	: me.title,
					width	: me.width,
					height	: me.height,
					layout	: 'fit',
					plain	: true,
					modal	: true,
					items	: me.formPanel,
//					autoScroll: true,
					buttons	: buttons

				};
				if (me.windowConfig!=null) {
					//add extra window configuration
					for (propertyName in me.windowConfig) {
						windowConfig[propertyName] = me.windowConfig[propertyName];
					}
				};
				windowConfig.cls='windowConfig bottomButtonsDialog tpspecial';
				// windowConfig.border=false;
				windowConfig.bodyBorder=false;
				windowConfig.margin='0 0 0 0';
				windowConfig.style={
					padding:' 5px 0px 0px 0px'
				};
				windowConfig.bodyPadding='0px';
				me.win = Ext.create('Ext.window.Window', windowConfig);
				//set the win in the scope to be available in the overridden handlers (createEditForm(), submitHandler()
				scope.win = me.win;
				me.win.show();
			};
			if (me.load!=null) {
				var loadHandler = me.load.loadHandler;
				if (loadHandler==null) {
					//load data for form panel
					me.formPanel.setLoading(true);
					me.formPanel.getForm().load({
						url : me.load.loadUrl,
						params: me.load.loadUrlParams,
						scope: scope,
						success: function(form, action) {
							//call postDataProcess only after window is rendered because
							//some fields (like labelEl) are available only after the window is rendered
							if (me.postDataProcess!=null) {
								me.postDataProcess.call(scope, action.result.data, me.formPanel, me.extraConfig);
							}
							me.formPanel.setLoading(false);
						},
						failure: function(form, action) {
							me.formPanel.setLoading(false);
							Ext.MessageBox.show({
								title: this.failureTitle,
								msg: action.response.responseText,
								buttons: Ext.Msg.OK,
								icon: Ext.MessageBox.ERROR
							})
							me.formPanel.setLoading(false);
						}
					});
				} else {
					//alternative loading: probably loads a store
					loadHandler.call(scope, me.load.loadUrl, me.load.loadUrlParams);
				}
			}
		}
	},

	/**
	 * The submit handler function
	 */
	submitHandler: function(submit, scope, submitButtonIndex, extraConfig) {
		var submitUrl = submit.submitUrl;
		var submitUrlParams = submit.submitUrlParams;
		var manualSubmitHandler = submit.submitHandler;
		var me = this;
		var onSuccess = function(form, action) {
			if (submit.loading) {
				me.formPanel.setLoading(false);
			}
			me.disableSubmitButton(scope.win, false, submitButtonIndex);
			var result = action.result;
			if (result!=null) {
				if (result.success) {
					//refresh after successful submit
					if (submit.refreshAfterSubmit==null || submit.refreshAfterSubmit==true) {
						me.refreshHandler(scope, result, submit);
					}
					if (submit.closeAfterSubmit==null || submit.closeAfterSubmit==true) {
						me.win.close();
					}
				} else {
					com.trackplus.util.showError(result);
				}
			}
		};
		var onFailure = function(form, action) {
			if (submit.loading) {
				me.formPanel.setLoading(false);
			}
			me.disableSubmitButton(scope.win, false, submitButtonIndex);
			result = action.result;
			if (result!=null) {
				var errorCode = result.errorCode;
				var title = result.title;
				if (errorCode!=null && (errorCode==4 || errorCode==5)) {
					//4==NEED_CONFIRMATION
					if (errorCode==4) {
						var errorMessage = result.errorMessage;
						Ext.MessageBox.confirm(title,
							errorMessage,
							function(btn){
								if (btn=="no") {
									return false;
								} else {
									submitUrlParams["confirmSave"]=true;
									me.submitHandler(submit, scope, submitButtonIndex);
								}
							}, this);
					} else {
						//5==RECOMMENDED_REPLACE
						if (errorCode==5) {
							scope.recommendedReplace.call(scope, me, submit, result);
						}
					}
				} else {
					errors = result.errors;
					if (errors!=null) {
						//form control errors (with control itemIds)
						form.markInvalid(errors);
					} else {
						var errorMessage = result.errorMessage;
						if (errorMessage!=null) {
							//only error message, no errorCode
							com.trackplus.util.showError(result);
						} else {
							com.trackplus.util.submitFailureHandler(form, action);
						}
					}
				}
			}

			if(submit.specialSubmitFailHandler != null) {
				submit.specialSubmitFailHandler.call(scope, result);
			}
		};
		if (submit.validateHandler!=null) {
			var valid = submit.validateHandler.call(scope, submit);
			if (valid!=null && !valid) {
				return;
			}
		}
		if (me.preSubmitProcess!=null) {
			//add extra (dynamic client side) request parameters, right before submit
			submitUrlParams = me.preSubmitProcess.call(scope, submitUrlParams, me.formPanel, submit.submitAction);
		}
		var standardSubmit = submit.standardSubmit;
		if (standardSubmit!=null) {
			me.formPanel.getForm().standardSubmit = standardSubmit;
		}
		var timeOut = submit.timeout;
		if (timeOut!=null) {
			me.formPanel.getForm().timeOut = timeOut;
		}
		if (manualSubmitHandler==null) {
			if (submit.loading) {
				me.formPanel.setLoading(true);
			}
			this.disableSubmitButton(scope.win, true, submitButtonIndex);
			me.formPanel.getForm().submit({
				url: submitUrl,
				params: submitUrlParams,
				method: "POST",
				scope: scope,
				success: onSuccess,
				failure: onFailure
			});
		} else {
			//alternative submit: probably loads a store
			//or simply makes a classic submit with specific success handler
			//this handler might close the window
			manualSubmitHandler.call(scope, me.win, submitUrl, submitUrlParams, extraConfig);
		}
		if (standardSubmit && me.formPanel.getForm().isValid()) {
			//by standardSubmit the handlers onSuccess or onFailure are not called consequently the popup window would not be closed
			//1. if after a standardSubmit the result is another page, closing the popup does not hurt
			//because anyway the entire page is newly loaded (close the instant query popup after executing it)
			//2. if after a form submit the result should be a file download (for example by generating the report).
			//In this case a standardSubmit should be used because even if AJAX submit would be used the result is a stream so
			//the resulting data is not JSON containing a "success" field i.e. the submit handlers would not be called.
			//A download dialog will appear but the actual page remains consequently the popup should be closed here:
			if(submit.submitMessage!=null){
				CWHF.showMsgInfo(submit.submitMessage);
			}
			Ext.defer(function(){
				me.disableSubmitButton(scope.win, false, submitButtonIndex);
			}, 1000);

		}
	},

	/**
	 * Disable the submit button at a specified index
	 */
	disableSubmitButton: function(window, disable, submitButtonIndex) {
		var toolbars = window.getDockedItems('toolbar[dock="bottom"]');
		if (toolbars!=null && toolbars.length>0) {
			//disable submit button
			if (submitButtonIndex==null) {
				submitButtonIndex=0;
			}
			toolbars[0].getComponent(submitButtonIndex).setDisabled(disable);
		}
	},

	refreshHandler: function(scope, result, submit) {
		if (submit!=null && submit.refreshAfterSubmitHandler!=null) {
			var refreshParameters = submit.refreshParametersBeforeSubmit;
			if (refreshParameters==null) {
				refreshParameters = new Object();
			}
			if (submit.refreshParametersAfterSubmit!=null && result!=null) {
				Ext.each(submit.refreshParametersAfterSubmit, function(refreshAfterExecuteParmeter) {
					var parameterName = refreshAfterExecuteParmeter.parameterName;
					var fieldNameFromResult = refreshAfterExecuteParmeter.fieldNameFromResult;
					refreshParameters[parameterName]=result[fieldNameFromResult];
				})
			}
			submit.refreshAfterSubmitHandler.call(scope, refreshParameters, result);
		}
	}
})
