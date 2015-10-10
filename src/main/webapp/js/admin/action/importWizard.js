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

Ext.define('com.trackplus.admin.action.ImportWizard',{
	extend:'Ext.Base',
	config: {
	},
	wizardPanel: null,
	fileName: null,
	uploadDone: false,

	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		//this.init();
	},

	getToolbarActions: function() {
		//return this.actions;
		return [];
	},

	/**
	 * Initialize the wizard panel. The initialization will be actualized
	 * once the card's data is loaded or further edited by the user
	 */
	getWizardPanel: function() {
		var toolbarItems = [];
		var numberOfCards = this.getNumberOfCards();
		if (numberOfCards>1) {
			toolbarItems.push({xtype: 'button',
				itemId: 'previous',
				text:'&laquo; '+ getText('common.lbl.wizard.previous'),
				scope: this,
				handler: function(btn) {
					this.onNavigate(btn, btn.up("panel"));
				},
				//at the beginning disabled
				disabled: true
			});
			toolbarItems.push('-');
			toolbarItems.push({xtype: 'button',
				itemId: 'next',
				text: getText('common.lbl.wizard.next')+' &raquo;',
				scope: this,
				handler: function(btn) {
					this.onNavigate(btn, btn.up("panel"));
				}
			});
			toolbarItems.push('-');
			toolbarItems.push({xtype: 'button',
				itemId: 'finish',
				iconCls: 'import16',
				text: getText('common.btn.import'),
				scope: this,
				handler: function(btn) {
					this.onNavigate(btn, btn.up("panel"));
				},
				disabled: true
				});
			toolbarItems.push('->');
			toolbarItems.push({xtype  : 'box',
				itemId : 'indicator',
				style  : 'margin-right: 5px',
				width: 100,
				autoEl : {
					tag	: 'div',
					html: ''
				}
			 });
		} else {
			//for direct import no navigation is needed
			toolbarItems.push({	itemId: 'finish',
				text: getText('common.btn.import'),
				scope: this,
				handler: function(btn) {
					this.loadDataForCard(1, true);
				},
				disabled: false
			});
		}
		this.wizardPanel = Ext.create('Ext.panel.Panel', {
			title: '',
			layout: 'card',
			region: 'center',
			cls: 'importWizard',
			//bodyStyle: 'padding:15px; border-left:none',
			//bodyStyle: 'border-left:none',
			defaults: {
				// applied to each contained panel
				border: false
			},
			// the panels (or "cards") within the layout
			items: [this.createEmptyCard("card1", true)],
			dockedItems: [{
				xtype: 'toolbar',
				cls: 'importWizard',
				dock: 'top',
				items: toolbarItems
			}]
		});
		this.initCards(2);
		this.loadDataForCard(1);
		return this.wizardPanel;
	},

	/**
	 * Depending on the actual card and data initialize the expected cards depending on the data received
	 */
	/*private*/initCards: function(cardNo) {
		//first clean the eventual next cards
		var layoutItems = this.wizardPanel.getLayout().getLayoutItems();
		if (layoutItems.length>1) {
			for ( var i = cardNo-1; i < layoutItems.length; i++) {
				this.wizardPanel.remove(layoutItems[i], true);
			}
		}
		for (var i = cardNo; i <= this.getNumberOfCards(); i++) {
			this.wizardPanel.add(this.createEmptyCard("card"+ i, false));
		}
		//actualize the toolbar according to the number of wizard panels
		this.actualizeToolbar(this.wizardPanel);
	},

	removeCard: function(cardNo) {
		var layoutItems = this.wizardPanel.getLayout().getLayoutItems();
		if (layoutItems.length>=cardNo) {
			this.wizardPanel.remove(cardNo-1, true);
		}
		//actualize the toolbar according to the number of wizard panels
		this.actualizeToolbar(this.wizardPanel);
	},

	getNumberOfCards: function() {
		return 1;
	},

	/**
	 * Creates a new empty form panel for a card
	 */
	createEmptyCard: function(itemId, fileUpload) {
		var panelConfig = {
				itemId: itemId,
				componentCls: 'importWizard',
				fileUpload:	fileUpload,
				//bodyStyle:	'padding: 5px; border-left:none',
				border: false,
				autoScroll:true,
				defaults: {
					labelStyle:'overflow: hidden;',
					//margin:"5 5 0 0",
					msgTarget:	'side'/*,
					anchor:	'-20'*/
				},
				method: "POST",
				//autoScroll:	true,
				items: []
			};
		return Ext.create('Ext.form.Panel', panelConfig);
	},

	onNavigate: function(btn, panel) {
		var layout = panel.getLayout();
		var activeItem = layout.getActiveItem();
		var index = panel.items.indexOf(activeItem);
		var numItems = panel.items.getCount() - 1;
		if (btn!=null) {
	        var params = null;
			if (btn.itemId == 'next' || btn.itemId == 'finish' && index <= numItems) {
				//+ 2 because index is 0 based and the data for the next card is loaded
				var reload = false;
				if (index==numItems) {
					//reload the error handling card
					index = index-1;
					reload = true;
	                //two forms should be submitted by conflict handling: the second one is "serialized" in params
	                params = this.getDataBeforeCardDelete(index+2, reload);
				}
				//TODO: redundantly initializes the next cards.
				//After a form is submitted and we go back to the previous card
				//the field values of the already submitted data is somehow stored/cached in the Ext.form.Basic
				//(even if the Ext.form.Panel form with all his fields is removed). Consequently when the form will be sumbitted
				//next time all form field values are duplicated in submit resulting in server side errors.
				//To avoid this initialize all next cards after each next.
				this.initCards(index+2);
				var dataIsValid = this.loadDataForCard(index+2, reload, params);
				if (dataIsValid!=false) {
					//if data is valid then change to the next card
					panel.layout.setActiveItem(index + 1);
				}
			} else {
				if (btn.itemId == 'previous' && index > 0) {
					var activeItem = layout.getActiveItem();
					//remove all data from the current card before moving back to the previous card: next always starts from an empty card
					activeItem.removeAll();
					panel.layout.setActiveItem(index - 1);
				}
			}
		}
		this.actualizeToolbar(panel);
	},

	/**
	 * Go back to the previous card if after submitting a card an error is received from the server
	 * (if data validation fails on the client side before submit then the card is not incremented anyway)
	 */
	goPrevious: function() {
		var layout = this.wizardPanel.getLayout();
		var activeItem = layout.getActiveItem();
		var index = this.wizardPanel.items.indexOf(activeItem);
		//var numItems = this.wizardPanel.items.getCount() - 1;
		if (index > 0) {
			this.wizardPanel.layout.setActiveItem(index - 1);
		}
		this.actualizeToolbar(this.wizardPanel);
	},

	goNext: function() {
		var layout = this.wizardPanel.getLayout();
		var activeItem = layout.getActiveItem();
		var index = this.wizardPanel.items.indexOf(activeItem);
		//var numItems = this.wizardPanel.items.getCount() - 1;
		if (index > 0) {
			this.wizardPanel.layout.setActiveItem(index + 1);
		}
		this.actualizeToolbar(this.wizardPanel);
	},

	/**
	 * Actualize the toolbar settings according to the current index
	 */
	actualizeToolbar: function(panel) {
		var layout = panel.getLayout();
		var activeItem = layout.getActiveItem();
		var index = panel.items.indexOf(activeItem);
		var numItems = panel.items.getCount();
		var toolbar = panel.getDockedItems('toolbar[dock="top"]');
		var previous = toolbar[0].getComponent('previous');
		if (previous!=null) {
			previous.setDisabled(!layout.getPrev());
		}
		var next = toolbar[0].getComponent('next');
		if (next!=null) {
			next.setDisabled((index+1)>=(numItems-1));
		}
		var finish = toolbar[0].getComponent('finish');
		if (finish!=null) {
			finish.setDisabled((index+1)!=(numItems-1));
		}
		var indicator = toolbar[0].getComponent('indicator');
		if (indicator!=null) {
			var indicatorText = getText("common.lbl.wizard.step", (index + 1), numItems);
			panel.setTitle(this.getTitle(index + 1));
			indicator.update(indicatorText);
		}
	},

	/**
	 * Gets the card title depending on card number
	 * should be overridden in the derived classes
	 */
	/*protected abstract*/getTitle: function(cardNo) {
		return '';
	},

	validateFileExtension: function(fileName) {
		var exp = this.getFilePattern();
		return exp.test(fileName);
	},

	/*protected abstract*/getFilePattern: function() {
		return /^.*$/
	},

	/*protected abstract*/getFileTypeLabel: function() {
		return "";
	},

	/*protected abstract*/getFileEmptyText: function() {
	    return "";
	},

	/**
	 * Load/submit the data for on the cardNo
	 * cardNo: the card number to go next, or the actual card number if reload
	 * reload: whether to reload the current card or submit the current values and go to the next card
	 * return false and optionally show an alert box if there is a validation problem which prevents navigating to the next card
	 */
	/*protected abstract*/loadDataForCard: function(cardNo, reload, params) {
	},

	/*protected abstract*/getDataBeforeCardDelete: function(cardNo, reload) {

	},

	submitFromCardToCard: function(cardFrom, cardTo, params, reload, timeout) {
		var panelFrom = this.wizardPanel.getComponent("card" + cardFrom);
		var panelTo = this.wizardPanel.getComponent("card" + cardTo);
		if (!panelFrom.getForm().isValid()) {
			return false;
		}
		panelTo.setLoading(getText("common.lbl.loading"));
	    if (timeout!=null) {
	        //for huge files the import can take longer as the standard 30 sec timeout
	        //if timeout expires before the file is completely processed the failure callback executed
	        panelFrom.getForm().timeout = timeout;
	    }
		panelFrom.getForm().submit({
			url: this.getImportWizardUrl(cardTo, reload),
			params: params,
			scope: this,
			method: "POST",
			success: function(form, action) {
	            panelTo.setLoading(false);
				panelTo.removeAll(true);
				panelTo.add(this.getImportWizardItemsForCard(cardTo));
				this.postDataProcess(panelTo, action.result, cardTo, cardFrom);
			},
			failure: function(form, action) {
	            panelTo.setLoading(false);
				panelTo.removeAll();
				panelTo.add(this.getImportWizardItemsForCard(cardTo));
	            var data = action.result;
	            if (data==null) {
	                //not controlled failure: for ex. connection time out
	                data =  action.response;
	            }
				this.postDataProcess(panelTo, data, cardTo, cardFrom);
			}
		})
	},

	submitFromCardToCardMessageOnFailure: function(cardFrom, cardTo, params, reload, back) {
		var panelFrom = this.wizardPanel.getComponent("card" + cardFrom);
		var panelTo = this.wizardPanel.getComponent("card" + cardTo);
		panelTo.setLoading(getText("common.lbl.loading"));
		panelFrom.getForm().submit({
			url: this.getImportWizardUrl(cardTo, reload),
			params: params,
			scope: this,
			method: "POST",
			success: function(form, action) {
	            panelTo.setLoading(false);
				//do not recreate the form (items for card) if there is no card change
				//because the previous fields in the formBase are not cleaned
				//consequently it would duplicate the fields to submit
				//instead upgrade the existing fields with refreshed data
				if (cardFrom!=cardTo) {
					panelTo.removeAll(true);
					var panel = this.getImportWizardItemsForCard(cardTo);
					panelTo.add(panel);
				}
				this.postDataProcess(panelTo, action.result, cardTo, cardFrom);
			},
			failure: function(form, action) {
				panelTo.setLoading(false);
				if (back) {
					this.goPrevious();
				}
				com.trackplus.util.submitFailureHandler(form, action);
			}
		})
	},

	/*protected abstract*/getImportWizardUrl: function(card, reload) {
		return "";
	},

	/**
	 * Get the initial items for a card
	 * Additional items can be added once the data is back from the server
	 */
	/*protected abstract*/getImportWizardItemsForCard: function(card) {
		return this.getImportWizardCard1Items();
	},

	/*protected abstract*/postDataProcess: function(panel, data, cardTo, cardFrom) {
		this.postProcessCard1(panel, data);
	},

	/**************************Import source**************************************/

	/*protected*/getImportWizardCard1Items: function() {
		var card1Items = [CWHF.createFileField(
							getText("common.lbl.file", this.getFileTypeLabel()), 'uploadFile',
							{allowBlank:false, labelWidth:250, width:700, labelIsLocalized: true, emptyText:this.getFileEmptyText()},
							{change:{fn:function(){
								this.uploadDone=false;},
								scope:this}})];
		card1Items.push({xtype: 'label',
			itemId: 'importSourceError'});
		return card1Items;
	},

	/**
	 * Post process first card data
	 * Called after first loading the first card data and after each import source change
	 */
	postProcessCard1: function(formPanel, data) {
		var uploadFile = formPanel.getComponent('uploadFile');
		uploadFile.setRawValue('');
	}
});
