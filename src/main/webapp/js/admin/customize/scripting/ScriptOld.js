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


Ext.define('com.trackplus.admin.customize.script.Script',{
	extend:'com.trackplus.admin.GridBase',
	autoLoadGrid:false,
	fields:[{name: 'id',type:'int'},
	        {name: 'name',type:'string'},
	        {name: 'scriptType', type: 'int'},
	        {name: 'typeLabel', type: 'string'}],
	columnModel:[
		{text:getText('admin.customize.script.lbl.className'),
			width:400,dataIndex: 'name', sortable:true,
			filter: {
	            type: "string"
	        }},
		{text:getText('admin.customize.script.lbl.scriptType'),
			width:150,dataIndex: 'typeLabel', sortable:true}
	],
	getLoadStoreUrl:function() {
		return this.baseAction + '!loadScripts.action';
	},
	confirmDeleteEntity:true,
	confirmDeleteNotEmpty:true,
	baseAction:'script',
	entityID:'scriptID',
	editWidth:800,
	editHeight:600,
	labelWidth: 120,
	useCopy:true,

	getEntityLabel:function(){
		return getText("admin.customize.script.lbl");
	},

	createEditForm:function(entityJS,type){
		var me=this;
		return new Ext.form.FormPanel({
			url:'script!save.action',
			border:false,
			autoScroll: true,
			margin: '0 0 0 0',
			bodyStyle:{
				padding:'10px'
			},
			/*style:{
				borderBottom:'1px solid #D0D0D0'
			},*/
			items: [CWHF.createTextField("admin.customize.script.lbl.className", "clazzName",
						{allowBlank:false, labelWidth:this.labelWidth, anchor:'100%',itemId:'clazzName'}),
				CWHF.createCombo("admin.customize.script.lbl.scriptType", "scriptType",
					{itemId:"scriptType", width:300, allowBlank:false, labelWidth:this.labelWidth},
	                {select: {fn: this.scriptTypeChange, scope:this}}),
				CWHF.createTextAreaField('admin.customize.script.lbl.sourceCode', "sourceCode",
						{labelWidth:this.labelWidth,anchor: '100% -60'})
			]
		});
	},
	afterLoadForm:function(data, panel) {
		var scriptTypeCombo = panel.getComponent("scriptType");
		scriptTypeCombo.store.loadData(data["scriptTypeList"]);
		scriptTypeCombo.setValue(data["scriptType"]);
	},

	/**
	 * Handler for changing a default value
	 */
	scriptTypeChange: function(combo, records, options) {
	    var scriptType = combo.getValue();
	    var toolbars = this.win.getDockedItems('toolbar[dock="bottom"]');
	    if (toolbars) {
	        toolbars[0].getComponent(0).setDisabled(this.compileIsDisabled(scriptType));
	    }
	},

	compileIsDisabled: function(scriptType) {
	    if (scriptType && scriptType===3) {
	        return true;
	    }
	    return false;
	},

	/**
	 * Additional execute compile method for script
	 */
	getAdditionalActions: function(recordData, submitParams, operation) {
	    var disabled = false;
	    if (recordData) {
	        disabled = this.compileIsDisabled(recordData["scriptType"]);
	    }
		//only for "edit", not for "add" or "instant"
		return [{submitUrl:this.getBaseAction() + '!compile.action',
				submitUrlParams:submitParams,
				submitButtonText:getText('common.btn.compile'),
	            disabled: disabled,
				closeAfterSubmit: false,
				refreshAfterSubmitHandler:this.showCompileResult}];
	},

	/**
	 * Whether the filter contains parameter(s)
	 */
	showCompileResult: function(refreshParameters, result) {
		Ext.MessageBox.show({
			title: result.title,
			msg: result.message,
			buttons: Ext.Msg.OK,
			icon: Ext.MessageBox.INFO
		});
	},

	/**
	 * The iconCls for the add button, overwrites base class icon
	 */
	getAddIconCls: function() {
		return 'scriptAdd';
	},
	/**
	 * The iconCls for the edit button, overwrites base class icon
	 */
	getEditIconCls: function() {
		return 'scriptEdit';
	}
});
