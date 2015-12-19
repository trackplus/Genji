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

Ext.define("com.trackplus.admin.FormBase",{
	extend:"Ext.form.Panel",
	mixins:{
		actionsBase: "com.trackplus.admin.ActionBase"
	},
	
	scrollable: true,
	
	toolbarDock: "top",
	
	/**
	 * implement this method if the load Url should be calculated dynamically (for ex. delete)
	 */
	getLoadUrl: null,
	
	getLoadParams: null,
	
	getSubmitParams: null,
	
	config: {
		//should be specified
		callerScope: null,
		//the entity label of the caller window
		entityLabel: null,
		entityContext: null,
		/**
		 * for refreshing the original view which created this window (after form submit)
		 */
		refreshParametersBeforeSubmit: null,
		refreshParametersAfterSubmit: null,
		refreshAfterSubmitHandler: null,
		
		/**
		 * Process the load data returned from the server to actualize the view (load combos etc.) 
		 */
		postDataProcess:null
	},

	/**
	 * Initialization method
	 */
	/*protected*/initBase : function() {
	    this.initActions();
	},
	
	initActions: function() {
		//this.actions=[];	
	},
	
	initComponent: function() {
		if (CWHF.isNull(this.getEntityLabel()) && this.getCallerScope() && this.getCallerScope().getView()) {
			this.setEntityLabel(this.getCallerScope().getView().getEntityLabel());
		}
		this.initBase();
		//this.region = "center";
		this.border = false;
		this.bodyBorder = false;
		
		this.items = this.getFormFields();
		this.dockedItems = this.getToolbar(this.toolbarDock);
		//this.buttons = this.getToolbarActionButtons(this.actions);
		//cls='windowConfig bottomButtonsDialog tpspecial';
		//bodyBorder=false;
		/*margin='0 0 0 0';
		style={
			padding:' 5px 0px 0px 0px'
		};
		bodyPadding='0px';*/
		this.callParent();
		//this.add(this.getFormFields());
		this.loadFormPanel();
		//this.add(this.formPanel);
	},
	
	loadFormPanel: function() {
		this.setLoading(true);
		var loadUrl = null;
		if (this.getLoadUrl) {
			loadUrl = this.getLoadUrl();
		} else {
			loadUrl = this.loadUrl;
		}
		this.getForm().load({
			url : loadUrl,
			params: this.getLoadParams(),
			scope: this,
			success: function(form, action) {
				this.setLoading(false);
				//call postDataProcess only after window is rendered because
				//some fields (like labelEl) are available only after the window is rendered
				if (this.postDataProcess) {
					this.postDataProcess(action.result.data, this);
				}
			},
			failure: function(form, action) {
				this.setLoading(false);
				Ext.MessageBox.show({
					title: this.failureTitle,
					msg: action.response.responseText,
					buttons: Ext.Msg.OK,
					icon: Ext.MessageBox.ERROR
				})
			}
		});
	},
	
	postDataProcess: function() {
		
	}
	
	
})
