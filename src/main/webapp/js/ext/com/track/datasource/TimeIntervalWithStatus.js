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

Ext.define("js.ext.com.track.datasource.TimeIntervalWithStatus",{
	extend: "js.ext.com.track.datasource.TimePeriod",
	config: {
		timeIntervalOptions: null,
		timeInterval: null,
		timeIntervalName:null,

		statusOptions:null,
		statuses: null,
		statusesName: null,
		statusLabelKey:null,

		statusSecondOptions:null,
		statusesSecond: null,
		statusesSecondName: null,
		statusSecondLabelKey:null,

		statusThirdOptions:null,
		statusesThird: null,
		statusesThirdName: null,
		statusThirdLabelKey:null,

		statusFourthOptions:null,
		statusesFourth: null,
		statusesFourthName: null,
		statusFourthLabelKey:null,

		statusFifthOptions:null,
		statusesFifth: null,
		statusesFifthName: null,
		statusFifthLabelKey:null,

		isStatusOverTime:null,

		groupingName:null,
		grouping: null,

		groupingFirstLabelName:null,
		groupingFirstLabel:null,

		groupingSecondLabelName:null,
		groupingSecondLabel:null,

		groupingThirdLabelName:null,
		groupingThirdLabel:null,

		groupingFourthLabelName:null,
		groupingFourthLabel:null,

		groupingFifthLabelName:null,
		groupingFifthLabel:null
	},
	timeIntervalComp:null,
	groupingComp: null,
	statusSelect: null,
	groupingFirstLabelComp: null,
	statusSelectSecond:null,
	groupingSecondLabelComp: null,
	statusSelectThird: null,
	groupingThirdLabelComp: null,
	statusSelectFourth: null,
	groupingFourthLabelComp: null,
	statusSelectFifth: null,
	groupingFifthLabelComp: null,

	NEW: 1,

	initComponent: function() {
		this.callParent();
		var me = this;
		var sectionItems= [];
		Ext.Array.forEach(this.timeIntervalOptions, function(timeIntervalOption, index) {
			timeIntervalOption.label = getText(timeIntervalOption.label);
		}, this);

		this.timeIntervalComp = CWHF.createCombo("common.timeInterval", this.timeIntervalName,{
			value:this.timeInterval,
			data:this.timeIntervalOptions,
			labelWidth: this.labelWidth,
			labelAlign: this.labelAlign,
			labelStyle: this.labelStyle,
			width: this.width
		});

		this.statusSelect = CWHF.createMultiSelect(null, this.statusesName, {
			data: this.statusOptions,
			value: this.statuses,
			labelWidth: this.labelWidth,
			labelAlign: this.labelAlign,
			labelStyle: this.labelStyle,
			width: this.width,
			height: 100,
			fieldLabel:this.statusLabelKey,
			disabled: this.calculationMode == this.NEW
		});

		//If selected chart type is status over time
		if(this.isStatusOverTime) {
			this.addExtraComponentsForStatusOverTime(sectionItems);
		}else {
			sectionItems.push(this.timeIntervalComp);
			sectionItems.push(this.statusSelect);
		}

		this.add( {
			xtype: 'fieldset',
			itemId: 'fsIntervalWithStatus',
			title: getText('common.intervalAndStatus'),
			collapsible: false,
			defaultType: 'textfield',
			layout: 'anchor',
			items: sectionItems});
	},

	addExtraComponentsForStatusOverTime: function(sectionItems) {
		var me = this;
		this.groupingComp = CWHF.createCheckbox('statusOverTime.prompt.groupingCheckBox' ,this.groupingName,{
			labelAlign:this.labelAlign,
			labelStyle:this.labelStyle,
			labelSeparator:this.labelSeparator,
			labelWidth: this.labelWidth,
			checked: this.grouping,
			disabled: this.calculationMode == this.NEW,
			listeners: {
				'change':{
					fn: function (newValue, oldValue, eOpts ){
						if(newValue.checked) {

							me.groupingFirstLabelComp.show();
							me.statusSelectSecond.show();
							me.groupingSecondLabelComp.show();
							me.statusSelectThird.show();
							me.groupingThirdLabelComp.show();
							me.statusSelectFourth.show();
							me.groupingFourthLabelComp.show();
							me.statusSelectFifth.show();
							me.groupingFifthLabelComp.show();
							me.grouping = true;
						}else {
							me.groupingFirstLabelComp.hide();
							me.statusSelectSecond.hide();
							me.groupingSecondLabelComp.hide();
							me.statusSelectThird.hide();
							me.groupingThirdLabelComp.hide();
							me.statusSelectFourth.hide();
							me.groupingFourthLabelComp.hide();
							me.statusSelectFifth.hide();
							me.groupingFifthLabelComp.hide();
							me.grouping = false;
						}
					}
				}
			}
		});

		this.groupingFirstLabelComp = CWHF.createTextField('statusOverTime.prompt.groupName',this.groupingFirstLabelName,{
			labelWidth:this.labelWidth,
			labelAlign:this.labelAlign,
			labelStyle:this.labelStyle,
			labelSeparator:this.labelSeparator,
			width: this.width,
			value: this.groupingFirstLabel,
			hidden: !this.grouping,
			disabled: this.calculationMode == this.NEW

		});


		this.statusSelectSecond = CWHF.createMultiSelect(null, this.statusesSecondName,{
			data: this.statusSecondOptions,
			value: this.statusesSecond,
			labelWidth: this.labelWidth,
			labelAlign: this.labelAlign,
			labelStyle: this.labelStyle,
			width: this.width,
			height: 100,
			fieldLabel:this.statusLabelKey,
			hidden: !this.grouping,
			disabled: this.calculationMode == this.NEW,
			margin: '5 0 0 0'
		});


		this.groupingSecondLabelComp = CWHF.createTextField('statusOverTime.prompt.groupName',this.groupingSecondLabelName,{
			labelWidth:this.labelWidth,
			labelAlign:this.labelAlign,
			labelStyle:this.labelStyle,
			labelSeparator:this.labelSeparator,
			width: this.width,
			value: this.groupingSecondLabel,
			hidden: !this.grouping,
			disabled: this.calculationMode == this.NEW,
			margin: '12 0 0 0'
		});


		this.statusSelectThird = CWHF.createMultiSelect(null, this.statusesThirdName,{
			data: this.statusThirdOptions,
			value: this.statusesThird,
			labelWidth: this.labelWidth,
			labelAlign: this.labelAlign,
			labelStyle: this.labelStyle,
			width: this.width,
			height: 100,
			fieldLabel:this.statusLabelKey,
			hidden: !this.grouping,
			disabled: this.calculationMode == this.NEW,
			margin: '5 0 0 0'
		});

		this.groupingThirdLabelComp = CWHF.createTextField('statusOverTime.prompt.groupName',this.groupingThirdLabelName,{
			labelWidth:this.labelWidth,
			labelAlign:this.labelAlign,
			labelStyle:this.labelStyle,
			labelSeparator:this.labelSeparator,
			width: this.width,
			value: this.groupingThirdLabel,
			hidden: !this.grouping,
			disabled: this.calculationMode == this.NEW,
			margin: '12 0 0 0'
		});

		this.statusSelectFourth = CWHF.createMultiSelect(null, this.statusesFourthName,{
			data: this.statusFourthOptions,
			value: this.statusesFourth,
			labelWidth: this.labelWidth,
			labelAlign: this.labelAlign,
			labelStyle: this.labelStyle,
			width: this.width,
			height: 100,
			fieldLabel:this.statusLabelKey,
			hidden: !this.grouping,
			disabled: this.calculationMode == this.NEW,
			margin: '5 0 0 0'
		});
		this.groupingFourthLabelComp = CWHF.createTextField('statusOverTime.prompt.groupName',this.groupingFourthLabelName,{
			labelWidth:this.labelWidth,
			labelAlign:this.labelAlign,
			labelStyle:this.labelStyle,
			labelSeparator:this.labelSeparator,
			width: this.width,
			value: this.groupingFourthLabel,
			hidden: !this.grouping,
			disabled: this.calculationMode == this.NEW,
			margin: '12 0 0 0'
		});

		this.statusSelectFifth = CWHF.createMultiSelect(null, this.statusesFifthName,{
			data: this.statusFifthOptions,
			value: this.statusesFifth,
			labelWidth: this.labelWidth,
			labelAlign: this.labelAlign,
			labelStyle: this.labelStyle,
			width: this.width,
			height: 100,
			fieldLabel:this.statusLabelKey,
			hidden: !this.grouping,
			disabled: this.calculationMode == this.NEW,
			margin: '5 0 0 0'
		});
		this.groupingFifthLabelComp = CWHF.createTextField('statusOverTime.prompt.groupName',this.groupingFifthLabelName,{
			labelWidth:this.labelWidth,
			labelAlign:this.labelAlign,
			labelStyle:this.labelStyle,
			labelSeparator:this.labelSeparator,
			width: this.width,
			value: this.groupingFifthLabel,
			hidden: !this.grouping,
			disabled: this.calculationMode == this.NEW,
			margin: '12 0 0 0'
		});

		sectionItems.push(this.timeIntervalComp);
		sectionItems.push(this.groupingComp);

		sectionItems.push(this.groupingFirstLabelComp);
		sectionItems.push(this.statusSelect);

		sectionItems.push(this.groupingSecondLabelComp);
		sectionItems.push(this.statusSelectSecond);

		sectionItems.push(this.groupingThirdLabelComp);
		sectionItems.push(this.statusSelectThird);

		sectionItems.push(this.groupingFourthLabelComp);
		sectionItems.push(this.statusSelectFourth);

		sectionItems.push(this.groupingFifthLabelComp);
		sectionItems.push(this.statusSelectFifth);
	}
});
