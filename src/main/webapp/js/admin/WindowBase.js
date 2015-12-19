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

Ext.define("com.trackplus.admin.WindowBase",{
	extend:"Ext.window.Window",
	mixins:{
		actionsBase: "com.trackplus.admin.ActionBase"
	},
	
	/**
	 * must be specified if no loadHandler or getLoadUrl is specified is specified (for normal form load)
	 */
	loadUrl: null,
	
	/**
	 * implement this method if the load Url should be calculated dynamically (for ex. delete)
	 */
	getLoadUrl: null,
	
	/**
	 * specify only if special loading is needed
	 */
	loadHandler: null,
	
	config: {
		//should be specified
		callerScope: null,
		windowTitle: null,
		//the entity label of the caller window
		entityLabel: null,
		entityContext: null,
		loadUrlParams: null,
		submitUrlParams: null,
		/**
		 * for refreshing the original view which created this window (after form submit)
		 */
		refreshParametersBeforeSubmit: null,
		refreshParametersAfterSubmit: null,
		refreshAfterSubmitHandler: null,
		/**
		 * the configuration object for the main panel:
		 * this should contain configuration properties for the main panel. Used together with items
		 */
		panelConfig: null,
		
		/**
		 * Process the load data returned from the server to actualize the view (load combos etc.) 
		 */
		postDataProcess:null
		/**
		 * extra configuration for the context of the operation (like operation started from tree or from grid whether the the node is leaf)
		 * used mainly for identifying the correct entity labels
		 */
		//extraConfig:null
	},

	actions: null,
	
	getFormFields: function() {
		return null;
	},
	
	/**
	 * Implement to set the window title by code (title can be specified also directly if no method call is needed)
	 */
	initTitle: null,
	
	/**
	 * Implement to set the window width by code (width can be specified also directly if no method call is needed)
	 */
	initWidth: null,
	
	/**
	 * Implement to set the window height by code (height can be specified also directly if no method call is needed)
	 */
	initHeight: null,
	
	/**
	 * Initialization method
	 */
	/*protected*/initBase : function() {
	    this.initActions();
	},
	
	initActions: function() {
		
	},
	
	initComponent: function() {
		//borrow the entityLabel function from the caller view
		if (CWHF.isNull(this.getEntityLabel()) && this.getCallerScope() && this.getCallerScope().getView && this.getCallerScope().getView()) {
			//this.setEntityLabel(this.getCallerScope().getView().getEntityLabel());
			this.getEntityLabel = this.getCallerScope().getView().getEntityLabel;
		}
		if (this.initTitle) {
			this.title = this.initTitle();
		} else {
			if (this.getWindowTitle()) {
				this.title = this.getWindowTitle();
			}
		}
		this.initBase();
		//this.setEntityLabel(this.getCallerScope().getView().getEntityLabel());
		if (this.initWidth) {
			this.width = this.initWidth();
		}
		if (this.initWidth) {
			this.height = this.initHeight();
		}
		this.ensureSize();
		var formFields = this.getFormFields();
		if (formFields) {
			this.formPanel = this.getPanel(formFields);
		}
		this.layout = 'fit';
		this.plain = true;
		this.modal = true;
		//this.items = [this.formPanel];
		this.buttons = this.getToolbarActionButtons(this.actions);
		cls='windowConfig bottomButtonsDialog tpspecial';
		bodyBorder=false;
		margin='0 0 0 0';
		style={
			padding:' 5px 0px 0px 0px'
		};
		bodyPadding='0px';
		this.callParent();
		this.add(this.formPanel);
	},
	
	/**
	 * Create form panel from items
	 */
	getPanel: function(items) {
		var panelConfig = {
				border: false,
				autoScroll:true,
				margin: '0 0 0 0',
				bodyStyle:{
					padding:'10px'
				},
				defaultType:'textfield',
				defaults: {
					labelStyle:'overflow: hidden;',
					labelWidth:150,
					msgTarget:	'under',
					anchor:'100%'
				},
				method: "POST",
				items: items
			};
		if (this.getPanelConfig()) {
			//add extra panel configuration
			for (propertyName in this.panelConfig) {
				panelConfig[propertyName] = this.panelConfig[propertyName];
			}
		}
		return Ext.create("Ext.form.Panel", panelConfig);
	},
	
	/**
	 * label for the cancel button
	 */
	getCancelButtonText: function() {
		if (this.cancelButtonText) {
			return this.cancelButtonText;
		} else {
			return getText(this.getCancelButtonKey());
		}
	},

	ensureSize:function(){
		var size=borderLayout.ensureSize(this.width,this.height);
		this.width=size.width;
		this.height=size.height;
	},
	/**
	 * Shows the window according to the windowConfig
	 */
	showWindowByConfig: function() {
		//set the win in the scope to be available in the overridden handlers (createEditForm(), submitHandler()
		this.getCallerScope().win = this;
		this.show();
		this.loadFormPanel();
	},

	loadFormPanel: function() {
		//var loadHandler = this.getLoad().loadHandler;
		if (CWHF.isNull(this.loadHandler)) {
			//load data for form panel
			this.formPanel.setLoading(true);
			var loadUrl = null;
			if (this.getLoadUrl) {
				loadUrl = this.getLoadUrl();
			} else {
				loadUrl = this.loadUrl;
			}
			this.formPanel.getForm().load({
				url : loadUrl,
				params: this.getLoadUrlParams(),
				scope: this,
				success: function(form, action) {
					this.formPanel.setLoading(false);
					//call postDataProcess only after window is rendered because
					//some fields (like labelEl) are available only after the window is rendered
					if (this.postDataProcess) {
						this.postDataProcess(action.result.data, this.formPanel/*, this.extraConfig*/);
					}
				},
				failure: function(form, action) {
					this.formPanel.setLoading(false);
					Ext.MessageBox.show({
						title: this.failureTitle,
						msg: action.response.responseText,
						buttons: Ext.Msg.OK,
						icon: Ext.MessageBox.ERROR
					})
				}
			});
		} else {
			//alternative loading: probably loads a store
			this.loadHandler/*.call(this.getCallerScope(),*/(this.loadUrl, this.getLoadUrlParams());
		}
	}
	
})
