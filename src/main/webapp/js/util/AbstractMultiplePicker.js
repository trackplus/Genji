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


Ext.define('com.trackplus.util.AbstractMultiplePicker', {
    extend : 'Ext.form.field.Picker',
    config : {
        localizedLabel : '',
        data : null,
        value : null,
        store : null,
        displayField : 'label',
        valueField : 'id',
        includeClear : true,
        useNull : false,
        useTooltip : true,
        extraComponent : null,
        useRemoveBtn : false,
        removeHandler : null,
        pickerWidth : null,
        maxSelectionCount : null,
        includeSearch : true
    },
    displayValue : null,
    editable : false,
    tip : null,
    allText : getText('common.btn.all'),
    lblClearSelection : getText('common.lbl.clearSelectedItems'),
    lblSelectAll : getText('common.lbl.selectAll'),
    allItemSelected : false,
    initComponent : function() {
	    var me = this;
	    if (me.useNull == true) {
		    me.emptyText = null;
	    } else {
		    me.emptyText = me.localizedLabel + ":" + me.allText;
	    }
	    me.tooltip = me.emptyText;
	    if (me.useRemoveBtn == true) {
		    me.trigger2Cls = Ext.baseCSSPrefix + 'form-clear-trigger';
	    }
	    me.callParent(arguments);
	    if (me.store == null) {
		    me.store = me.createStore();
	    }
	    if (me.useTooltip) {
		    me.on('afterrender', me.updateTooltip, me);
	    }
    },
    onTrigger2Click : function() {
	    var me = this;
	    if (me.removeHandler != null && Ext.isFunction(me.removeHandler)) {
		    me.removeHandler.call(me);
	    } else {
		    me.ownerCt.remove(me);
	    }
    },

    initEvents : function() {
	    var me = this;
	    me.callParent();
	    // if (me.includeSearch) {
	    me.mon(me.inputEl, 'keyup', me.onKeyUpHandler, me);
	    // }
    },
    onKeyUpHandler : function(e, t) {
	    var me = this;
	    var key = e.getKey();
	    if (!e.isSpecialKey()) {
		    if (!me.isExpanded) {
			    this.onTriggerClick();
			    if (me.includeSearch) {
				    var charCode = e.getCharCode();
				    var cc = String.fromCharCode(e.keyCode);
				    me.searchField.setValue(cc);
				    me.searchField.focus();
			    }
		    }
	    }
    },

    /* abstract */
    createStore : function() {
	    return null;
    },
    /* abstract */
    createBoundList : function() {
	    return null;
    },
    /* abstract */
    addBoundListListeners : function() {
    },
    /* abstract */
    syncSelection : function() {
    },
    clearOrSelectAll : function() {
	    var me = this;
	    if ((me.maxSelectionCount != null && me.maxSelectionCount > 0) || me.allItemSelected == true) {
		    me.clearSelection();
	    } else {
		    me.selectAll();
	    }
    },

    selectAll : function() {
    },
    /* abstract */
    updateData : function(data) {
    },

    updateTooltip : function() {
	    var me = this;
	    if (me.useTooltip == true) {
		    if (me.tooltip != null) {
			    me.setToolTip(me.tooltip);
		    }
	    }
    },
    createSearchField : function() {
	    var me = this;
	    var searchField = Ext.create('Ext.form.field.Text', {
	        emptyText : getText('common.btn.search') + ' ' + me.localizedLabel + "...",
	        cls : 'searchfield',
	        paramName : me.displayField,
	        width : me.pickerWidth == null ? me.inputEl.getWidth() - 5 : me.pickerWidth - 15,
	        margin : '5 15 5 5'
	    });
	    searchField.on('change', function(cmp, value) {
		    me.filter.call(me, value);
	    });
	    return searchField;
    },

    filter : function(value) {
	    var me = this;
	    if (value.length > 0) {
		    me.store.filter({
		        id : me.displayField,
		        property : me.displayField,
		        value : value
		    });
	    } else {
		    me.clearFilter();
	    }
    },
    clearFilter : function() {
	    var me = this;
	    me.store.clearFilter();
    },
    getClearSelectionLabel : function() {
	    var me = this;
	    if (me.maxSelectionCount != null && me.maxSelectionCount > 0) {
		    var count = me.getSelectedItemsCount();
		    return me.lblClearSelection + " (" + count + ' from ' + me.maxSelectionCount + ')';
	    }
	    return me.allItemSelected == true ? me.lblClearSelection : me.lblSelectAll
    },
    createPicker : function() {
	    var me = this;
	    me.boundList = me.createBoundList();
	    var label;
	    me.linkClearSelection = Ext.create('Ext.ux.LinkComponent', {
	        handler : me.clearOrSelectAll,
	        clsLink : 'link_blue',
	        margin : '2 5 7 5',
	        scope : me,
	        label : me.getClearSelectionLabel()
	    });
	    var items = new Array();
	    if (me.extraComponent != null) {
		    items.push(me.extraComponent);
	    }
	    if (me.includeSearch == true) {
		    me.searchField = me.createSearchField();
		    items.push(me.searchField);
	    }
	    if (me.includeClear == true) {
		    items.push(me.linkClearSelection);
	    }
	    items.push(me.boundList);

	    me.picker = Ext.create('Ext.panel.Panel', {
	        hidden : true,
	        floating : true,
	        width : me.pickerWidth,
	        items : items
	    });
	    me.addBoundListListeners();
	    return me.picker;
    },

    onListRefresh : function() {
	    var me = this;
	    me.setValue(me.getValue(), true);
    },
    selectionChange : function(selectedRecords) {
	    var me = this;
	    var hasRecords = selectedRecords.length > 0;
	    if (me.isExpanded) {
		    me.setValue(selectedRecords, false);
	    }
    },
    getDisplayValue : function() {
	    return this.displayValue;
    },
    getSelectableItemsCount : function() {
	    var me = this;
	    if (me.store == null) {
		    me.store = me.createStore();
	    }
	    var items = me.store.getRange();
	    return items.length;
    },
    getSelectedItemsCount : function() {
	    return 0;
    },
    findRecord : function(field, value) {
	    var me = this;
	    if (me.store == null) {
		    me.store = me.createStore();
	    }
	    var ds = this.store, idx = ds.findExact(field, value);
	    return idx !== -1 ? ds.getAt(idx) : false;
    },
    findRecordByValue : function(value) {
	    return this.findRecord(this.valueField, value);
    },

    getRecordDisplayValue : function(record) {
	    var me = this;
	    return record.get(me.displayField)
    },
    getRecordValue : function(record) {
	    var me = this;
	    return record.get(me.valueField)
    },

    setValue : function(value, doSelect) {
	    var me = this;
	    var processedValue = new Array();
	    var displayData = new Array();
	    me.valueModels = new Array();
	    if (value == null || value == '') {
		    me.value = null;
		    me.displayValue = null;
	    } else {
		    value = Ext.Array.from(value);
		    var record;
		    for (i = 0, len = value.length; i < len; i++) {
			    record = value[i];
			    if (!record || !record.isModel) {
				    record = me.findRecordByValue(record);
			    }
			    // record found, select it.
			    if (record) {
				    me.valueModels.push(record);
				    displayData.push(me.getRecordDisplayValue(record));
				    processedValue.push(me.getRecordValue(record));
			    }
		    }
		    if (processedValue.length > 0) {
			    me.value = processedValue;
			    me.displayValue = displayData.join(', ');
		    } else {
			    me.value = null;
			    me.displayValue = null;
		    }
	    }
	    if (doSelect !== false) {
		    me.syncSelection();
	    }
	    var tooltipBegin = "";
	    if (me.localizedLabel != null && me.localizedLabel != "") {
		    tooltipBegin = me.localizedLabel + ":";
	    }
	    if (me.value == null) {
		    me.tooltip = tooltipBegin + me.allText;
		    me.allItemSelected = false;
	    } else {
		    me.tooltip = tooltipBegin + me.displayValue;
		    var count = me.getSelectableItemsCount();
		    if (count == me.value.length) {
			    me.allItemSelected = true;
		    } else {
			    me.allItemSelected = false;
		    }
	    }
	    if (me.linkClearSelection != null) {
		    var oldLabel = me.linkClearSelection.getMyLabel();
		    var newLabel = me.getClearSelectionLabel();// me.allItemSelected==true?me.lblClearSelection:me.lblSelectAll;
		    if (oldLabel != newLabel) {
			    me.linkClearSelection.suspendLayout = true;
			    me.linkClearSelection.setLabel(newLabel);
			    me.linkClearSelection.suspendLayout = false;
		    }
	    }
	    me.updateTooltip();

	    me.setRawValue(me.getDisplayValue());
	    me.checkChange();
	    me.applyEmptyText();
	    return me;
    },

    clearSelection : function() {
	    var me = this;
	    if (me.boundList != null) {
		    me.boundList.getSelectionModel().deselectAll(true);
	    }
	    me.setValue(null);
    },
    getSubmitValue : function() {
	    var value = this.getValue();
	    if (value != null) {
		    value = value.join();
	    }
	    return value;

    },
    getValue : function() {
	    return this.value;
    },
    setToolTip : function(str) {
	    var me = this;
	    if (str == null) {
		    if (me.tip == null) {
			    me.tip.destroy();
			    delete me.tip;
		    }
		    return;
	    }
	    var domEl = me.getEl();
	    if (domEl != null) {
		    if (me.tip == null) {
			    me.tip = Ext.create('Ext.tip.ToolTip', {
			        target : domEl,
			        html : str
			    });
		    } else {
			    me.tip.update(str);
		    }
	    }
    }
});
