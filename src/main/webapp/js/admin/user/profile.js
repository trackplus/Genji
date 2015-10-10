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

Ext.define('com.trackplus.admin.user.Profile',{
	extend:'Ext.Base',
	config: {
		context: 1,
		isUser: true //this flag is set in case of adding new user from person.js. If true this is a real user otherwise this is a client user.
	},
	personId: null,
	constructor: function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	},

	CONTEXT:{
			SELFREGISTRATION: 0,
			PROFILEEDIT: 1,
			USERADMINADD: 2,
			USERADMINEDIT: 3
	},

	mainForm:null,
	btnSave:null,
	//originalUserName:null,
	labelWidth:250,
	textFieldWidth:250+300,
	textFieldWidthShort:250+70,
	alignR:"right",
	FieldSetWidth:250+300+150,
	context:1, // this.CONTEXT.PROFILEEDIT,
	//tabPanel:null,
	reqTpl:'<span class="required">&nbsp;</span>',

	/**
	 * Create a Save button
	 * @return {Ext.button.Button} The save button
	 */
	createSaveButton:function(){
		if(this.btnSave==null){
			this.btnSave=new Ext.button.Button({
				overflowText:getText('common.btn.save'),
				tooltip:getText('common.btn.save'),
				text: getText('common.btn.save'),
				iconCls: 'save',
				disabled: true,
				scope:this,
				handler:function(){
					this.save.call(this);
				}
			});
		}
		return this.btnSave;
	},

	getTabPanel: function() {
		return this.mainForm.getComponent(0);
	},

	/**
	 * Gets a control by the path according to the "arguments" starting form the main tab panel
	 */
	getControl: function() {
		return CWHF.getControl.apply(this.getTabPanel(), arguments);
	},

	getHelpWrapper: function() {
		return CWHF.getHelpWrapper.apply(this.getTabPanel(), arguments);
	},

	getWrappedControl: function() {
		return CWHF.getWrappedControl.apply(this.getTabPanel(), arguments);
	},

	changeLdap:function() {
		var ldapCmp = this.getWrappedControl("tab.main", "fslogin", "main.ldapUser");
		var ldapChecked = ldapCmp.getValue();
		this.getControl("tab.main", "fslogin", "main.passwd").setDisabled(ldapChecked);
		this.getControl("tab.main", "fslogin", "main.passwd2").setDisabled(ldapChecked);
	},
	changeLdapSelReg:function(check, newValue, oldValue){
	  	var me=this;
		var ldapChecked = check.getValue();
		me.selfRegForm.getComponent("main.passwd").setDisabled(ldapChecked);
		me.selfRegForm.getComponent("main.passwd2").setDisabled(ldapChecked);
	},

	/**
	 * Creates the main tab for the user profile.
	 * @return {Ext.form.Panel} A panel for this tab
	 */
	createTabMain:function() {
		var panel=new Ext.form.Panel({
			itemId:'tab.main',
			title:getText('admin.user.profile.lbl.tabMain'),
			layout: {
				type: 'anchor'
			},
			padding:'0 0 0 0',
			bodyPadding:'5 0 0 0',
			items: [{
				xtype: 'label',
				html: '<div style="width:' + this.FieldSetWidth+'px;"><span class=requiredHint><span class="requiredHintBar">&nbsp;</span> = ' + getText("common.lbl.requiredInfo") +'</span></div>',
				margin:'8 0 0 0',
				width: this.FieldSetWidth},
				{
				xtype: 'fieldset',
				itemId: 'fslogin',
				width: this.FieldSetWidth,
				title: getText('admin.user.profile.fieldset.basic'),
				collapsible: false,
				//defaults: {anchor: '100%'},
				layout: 'anchor',
				items: [
				        CWHF.createTextFieldWithHelp('admin.user.profile.lbl.userName','main.userName',
								{enableKeyEvents:true,allowBlank:false,maxLength:60,afterLabelTextTpl:this.reqTpl},
								//listeners:
								{
									//keyup: {
									blur: {
											element: 'el',
											scope: this,
											fn: function() {
												this.validateLoginName.call(this);
											}
										}
									}
								//}
						),
						CWHF.createCheckboxWithHelp('admin.user.profile.lbl.ldapUser', 'main.ldapUser', {},
								{change:{
										fn: this.changeLdap,
										scope: this
									}
								}
						),
						CWHF.createTextField('admin.user.profile.lbl.passwd','main.passwd',
										{inputType:'password',minLength:5, id:'main.passwd',afterLabelTextTpl:this.reqTpl}),
						CWHF.createTextField('admin.user.profile.lbl.passwd2','main.passwd2',
										{inputType:'password',vtype:'password',initialPassField:'main.passwd',afterLabelTextTpl:this.reqTpl})
					]
			}, {
				xtype: 'fieldset',
				itemId: 'fsname',
				width: this.FieldSetWidth,
				title: getText('admin.user.profile.fieldset.nameLang'),
				collapsible: false,
				//defaults: {anchor: '100%'},
				layout: 'anchor',
				items: [CWHF.createTextField('admin.user.profile.lbl.lastName','main.lastName', {allowBlank:false, maxLength:25,afterLabelTextTpl:this.reqTpl},
						//{listeners:
						{
							blur: {
								element: 'el',
								fn: function() {
									this.validateUser.call(this);
								},
								scope: this
							}
						}
					),
					CWHF.createTextField('admin.user.profile.lbl.firstName','main.firstName',  {allowBlank:false, maxLength:25,afterLabelTextTpl:this.reqTpl},
						//{listeners:
							{
								blur: {
									element: 'el',
									fn: function() {
										this.validateUser.call(this);
									},
									scope: this
								}
						 //}
						}),
					CWHF.createComboWithHelp('admin.user.profile.lbl.locale',
							'main.locale',{idType:'string'}),
					CWHF.createComboWithHelp('admin.user.profile.lbl.usertz',
							'main.userTz',{idType:'string'})]
			}]
		});
		return panel;
	},

	/**
	 * Creates the reminder e-mail configuration tab for the user profile
	 * @param {Object} jsonData The data required for this and other tabs
	 * @return {Ext.form.Panel} A panel for this tab
	 */
	changeNoEmail:function() {
		var noEmailCmp =this.getWrappedControl("tab.remail", "fsremail1", "remail.noEmail");
		var noEmailChecked = noEmailCmp.getValue();
		// Get all components to disable or enable
		this.getControl("tab.remail", "fsremail1", "remail.prefEmailType").setDisabled(noEmailChecked);
		this.getHelpWrapper("tab.remail", "fsremail2", "remail.remindMeAsOriginator").setDisabled(noEmailChecked);
		this.getHelpWrapper("tab.remail", "fsremail2", "remail.remindMeAsManager").setDisabled(noEmailChecked);
		this.getHelpWrapper("tab.remail", "fsremail2", "remail.remindMeAsResponsible").setDisabled(noEmailChecked);
		this.getHelpWrapper("tab.remail", "fsremail3", "remail.remindPriorityLevel").setDisabled(noEmailChecked);
		this.getHelpWrapper("tab.remail", "fsremail3", "remail.remindSeverityLevel").setDisabled(noEmailChecked);
		//this.getHelpWrapper("tab.remail", "fsremail3", "remail.emailLead").setDisabled(noEmailChecked);
		this.getHelpWrapper("tab.remail", "fsremail3", "remail.remindMeOnDays").setDisabled(noEmailChecked);
	},
	/**
	 * Creates the e-mail tab of the user profile.
	 * @return {Ext.form.Panel} A panel for this tab.
	 */
	createTabEmail:function(){
		var panel=new Ext.Panel({
			id:'tab.remail',
			title:getText('admin.user.profile.lbl.tabEmail'),
			layout: {
				type: 'anchor'
			},
			padding:'0 0 0 0',
			bodyPadding:'5 0 0 0',
			items: [{
				xtype: 'fieldset',
				itemId: 'fsremail1',
				width: this.FieldSetWidth,
				title: getText('admin.user.profile.fieldset.generalEmail'),
				collapsible: false,
				//defaults: {anchor: '100%'},
				layout: 'anchor',
				items: [CWHF.createCheckboxWithHelp('admin.user.profile.lbl.noEmail','remail.noEmail', {},
							{change: {fn: this.changeNoEmail, scope:this}}
						),
						CWHF.getRadioGroup('remail.prefEmailType',
								'admin.user.profile.lbl.prefEmailType',
								400,
								[{boxLabel:"HTML", name:"remail.prefEmailType", inputValue:"HTML", checked:true},
								 {boxLabel:"Plain", name:"remail.prefEmailType", inputValue:"Plain"}])
					]
			}, {
				xtype: 'fieldset',
				itemId:'fsremail2',
				width: this.FieldSetWidth,
				title: getText('admin.user.profile.fieldset.rrole'),
				collapsible: false,
				//defaults: {anchor: '100%'},
				layout: 'anchor',
				items: [CWHF.createCheckboxWithHelp('admin.user.profile.lbl.remindMeAsOriginator','remail.remindMeAsOriginator'),
						CWHF.createCheckboxWithHelp('admin.user.profile.lbl.remindMeAsManager', 'remail.remindMeAsManager'),
						CWHF.createCheckboxWithHelp('admin.user.profile.lbl.remindMeAsResponsible','remail.remindMeAsResponsible')]
			}, {
				xtype: 'fieldset',
				itemId:'fsremail3',
				width: this.FieldSetWidth,
				title: getText('admin.user.profile.fieldset.rlevelLead'),
				collapsible: false,
				//defaults: {anchor: '100%'},
				layout: 'anchor',
				items: [CWHF.createComboWithHelp('admin.user.profile.lbl.remindPriorityLevel','remail.remindPriorityLevel',
												null,null,'remail.remindPriorityLevel'),
						CWHF.createComboWithHelp('admin.user.profile.lbl.remindSeverityLevel','remail.remindSeverityLevel',
												null,null,'remail.remindSeverityLevel'),
						CWHF.createNumberFieldWithHelp('admin.user.profile.lbl.emailLead', 'remail.emailLead',
												0, -100, 100,{width:this.labelWidth+30,hideTrigger:true}),
						CWHF.createComboWithHelp('admin.user.profile.lbl.remindMeOnDays','remail.remindMeOnDays',
												{multiSelect:true},null,'remail.remindMeOnDays')]
			}]
		});

		return panel;
	},


	/**
	 * Creates the remaining configuration options tab for the user profile.
	 * @return {Ext.form.Panel} A panel for this tab.
	 */
	createTabOther:function(){
		var options=[
				{'id':'UTF-8','label':'Unicode UTF-8'},
				{'id':'ISO-8859-1','label':'Western (ISO-8859-1)'},
				{'id':'ISO-8859-2','label':'Central Europe (ISO-8859-2)'},
				{'id':'ISO-8859-3','label':'Southern Europe (ISO-8859-3)'},
				{'id':'ISO-8859-4','label':'Northern Europe (ISO-8859-4)'},
				{'id':'ISO-8859-6','label':'Arabic (ISO-8859-6)'},
				{'id':'ISO-8859-7','label':'Greek (ISO-8859-7)'},
				{'id':'ISO-8859-8','label':'Hebrew (ISO-8859-8)'},
				{'id':'ISO-8859-9','label':'Turkish (ISO-8859-9)'},
				{'id':'ISO-8859-10','label':'Nordic (ISO-8859-10)'},
				{'id':'ISO-8859-11','label':'Thai (ISO-8859-11)'},
				{'id':'ISO-8859-13','label':'Baltic (ISO-8859-13)'},
				{'id':'ISO-8859-14','label':'Celtic (ISO-8859-14)'},
				{'id':'ISO-8859-16','label':'S.E. Europe (ISO-8859-16)'},
				{'id':'KOI8-R','label':'Russian (KOI8-R)'},
				{'id':'KOI8-U','label':'Ukraine (KOI8-U)'},
				{'id':'Shift-JIS','label':'Japanese (Shift-JIS)'},
				{'id':'ISO-2022-JP','label':'Japanese (ISO-2022-JP)'},
				{'id':'GB18030','label':'Chinese (simplified)'},
				{'id':'Big5','label':'Chinese (traditional)'},
				{'id':'UTF-16','label':'Unicode UTF-16'}];
	    var icon = Ext.create('Ext.Img', {
	        src: Ext.BLANK_IMAGE_URL,
	        width:100,
	        height:100,
	        itemId: "avatarIcon"
	    });
	    var modifyBtn={xtype:'button',
	        style:{ marginBottom: '5px', marginLeft: '5px'},
	        enableToggle:false,
	        //margin:  '0 0 0 20',
	        itemId:'modifyBtn',
	        text:getText('common.btn.modify'),
	        scope:this,
	        handler:function(targetEl){
	            this.modifyIcon(targetEl);
	        }
	    };
	    iconWrapperItems =  [icon,  modifyBtn];
	    if (this.context==2) {
	        //add user: although the icon file is saved in the db, can't be saved directly into person because it does not exist yet
	        //after saving the person the iconKey and iconName should be set on the new personBean
	        iconWrapperItems.push(CWHF.createHiddenField("iconKey"));
	        iconWrapperItems.push(CWHF.createHiddenField("iconName"));
	    }
		var iconWrapper=Ext.create('Ext.form.FieldContainer',{
			combineErrors: true,
			itemId: 'iconPanel',
			fieldLabel:getText('admin.user.profile.lbl.avatar'),
			labelWidth:this.labelWidth,
			labelAlign:this.alignR,
			labelStyle:{overflow:'hidden'},
			layout: 'hbox',
			items :iconWrapperItems
		});

		//If new user is normal user then the home page is itemNavigator,
		//If new user is client  user then the home page is cockpit,
		var userHomePageCockpit = false;
		var userHomePageItemNavigator = true;
		if(this.isUser != null) {
			if(!this.isUser) {
				var userHomePageCockpit = true;
				var userHomePageItemNavigator = false;
			}
		}
		var panel=new Ext.Panel({
			id:'tab.other',
			title:getText('admin.user.profile.lbl.tabOther'),
			layout: {
				type: 'anchor'
			},
			padding:'0 0 0 0',
			bodyPadding:'5 0 0 0',
			items: [{
				xtype: 'fieldset',
				itemId: 'fsother1',
				width: this.FieldSetWidth,
				title: getText('admin.user.profile.fieldset.organization'),
				collapsible: false,
				defaultType: 'textfield',
				//defaults: {anchor: '100%'},
				layout: 'anchor',
				items: [CWHF.createLabelComponent('admin.user.profile.lbl.lastLogin','other.lastLogin'),
	                CWHF.createSingleTreePickerWithHelp("admin.user.profile.lbl.department",
	                    "other.department", [], null,
	                    {//allowBlank:false,
	                     labelWidth: this.labelWidth,
	                     width: 550
	                        //margin:'0 0 5 0',
	                    }),
						CWHF.createTextField('admin.user.profile.lbl.phone','other.phone'),
						CWHF.createTextField('admin.user.profile.lbl.employeeId', 'other.employeeId'),
	                    iconWrapper,
						CWHF.createNumberField('admin.user.profile.lbl.workingHours','other.workingHours',2,0,24,
								{labelWidth:this.labelWidth, width:this.labelWidth+80}),
						CWHF.createNumberField('admin.user.profile.lbl.hourlyWage','other.hourlyWage',2,0,null,
								{labelWidth:this.labelWidth, width:this.labelWidth+80}),
						CWHF.createNumberField('admin.user.profile.lbl.extraHourWage','other.extraHourWage',2,0,null,
								{labelWidth:this.labelWidth, width:this.labelWidth+80})]
			}, {
				xtype: 'fieldset',
				itemId: 'fsother2',
				width: this.FieldSetWidth,
				title: getText('admin.user.profile.fieldset.behaviour'),
				collapsible: false,
				defaultType: 'textfield',
				//defaults: {anchor: '100%'},
				layout: 'anchor',
				items: [CWHF.createComboWithHelp('admin.user.profile.lbl.csvEncoding','other.csvEncoding',{data:options, idType:'string'}),
						CWHF.createTextFieldWithHelp('admin.user.profile.lbl.csvSeparator','other.csvSeparator',
								{width:this.labelWidth+20,enforceMaxLength:true,maxLength:1}),
						CWHF.createCheckboxWithHelp('admin.user.profile.lbl.saveAttachments','other.saveAttachments'),
						CWHF.createComboWithHelp('admin.user.profile.lbl.designPath',
								'other.designPath',{idType:'string'}),
						CWHF.createCheckboxWithHelp('admin.user.profile.lbl.activateInlineEdit', 'other.activateInlineEdit'),
						CWHF.createCheckboxWithHelp('admin.user.profile.lbl.activateLayout', 'other.activateLayout'),
						CWHF.getRadioGroupWithHelp('other.homePage','admin.user.profile.lbl.homePage', this.textFieldWidth,
							[{boxLabel: getText('menu.cockpit'), name: 'other.homePage', inputValue: 'cockpit', checked:userHomePageCockpit},
							 {boxLabel: getText('menu.findItems'), name: 'other.homePage', inputValue: 'itemNavigator', checked:userHomePageItemNavigator}]),
						CWHF.createComboWithHelp('admin.user.manage.lbl.userLevel','other.userLevel')
					]
			}, {
				xtype: 'fieldset',
				itemId: 'fsother3',
				width: this.FieldSetWidth,
				title: getText('admin.user.profile.fieldset.timer'),
				collapsible: false,
				defaultType: 'textfield',
				//defaults: {anchor: '100%'},
				layout: 'anchor',
				items: [CWHF.createNumberFieldWithHelp('admin.user.profile.lbl.sessionTimeout',
							'other.sessionTimeout', 0, 1, 99999, {width:this.labelWidth+40,hideTrigger:true,disabled:true})/*,
						CWHF.createNumberFieldWithHelp('admin.user.profile.lbl.autoLoadTime',
							'other.autoLoadTime', 0, 10, 300, {width:this.labelWidth+40, hideTrigger:true}),
						CWHF.createCheckboxWithHelp('admin.user.profile.lbl.dashboardTimer',
							'other.showDashboardTimer', {width:this.labelWidth+40})*/]
			}, {
	                xtype: 'fieldset',
	                itemId: 'fsother4',
	                width: this.FieldSetWidth,
	                title: getText('admin.user.profile.fieldset.substitute'),
	                collapsible: false,
	                defaultType: 'textfield',
	                //defaults: {anchor: '100%'},
	                layout: 'anchor',
	                items: [CWHF.createComboWithHelp('admin.user.profile.lbl.substitutePerson','other.substituteID')]
	            }
	        ]
		});
		return panel;
	},

	modifyIcon: function(targetEl) {
	    var width = 550;
	    var height = 250;
	    var loadUrl = 'avatar.action';
	    var loadParams = {personID:this.personId, context:this.context};
	    if (this.context==2) {
	        var hiddenIconKey = targetEl.ownerCt.getComponent("iconKey");
	        if (hiddenIconKey!=null) {
	            loadParams["iconKey"] = hiddenIconKey.getValue();
	        }
	        var hiddenIconName = targetEl.ownerCt.getComponent("iconName");
	        if (hiddenIconName!=null) {
	            loadParams["iconName"] = hiddenIconName.getValue();
	        }
	    }
	    var load = {loadUrl:loadUrl, loadUrlParams:loadParams};
	    var submitParams = {personID:this.personId, context:this.context};
	    var submit = [{
	        submitUrl:'avatar!upload.action',
	        submitUrlParams:submitParams,
	        submitButtonText:getText('common.btn.upload'),
	        submitHandler:this.uploadFileHandler
	    },
	    {
	        submitUrl:'avatar!delete.action',
	        submitUrlParams:submitParams,
	        submitButtonText:getText('common.btn.delete'),
	        submitHandler:this.deleteUploadedFileHandler
	    }];
	    var title = getText('common.lbl.upload', getText('admin.customize.list.lbl.icon'));
	    var windowParameters = {title:title,
	        width:width,
	        height:height,
	        load:load, submit:submit,
	        formPanel: this.getFormPanel(),
	        postDataProcess:this.renderUploadPostDataProcess,
	        cancelButtonText: getText('common.btn.done'),
	        refreshAfterCancel:true,
	        extraConfig: {avatarIcon: targetEl.ownerCt.getComponent("avatarIcon")}
	    };
	    var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
	    windowConfig.showWindowByConfig(this);
	},

	getFormPanel: function(fromTree, selectedEntryID) {
	    var icon = Ext.create('Ext.Img', {
	        src: Ext.BLANK_IMAGE_URL,
	        width: 100,
	        height:100,
	        itemId: "avatarIcon"
	    });
	    var iconWrapper=Ext.create('Ext.form.FieldContainer',{
		    combineErrors: true,
		    itemId: 'iconPanel',
		    fieldLabel:getText('admin.user.profile.lbl.avatarActual'),
		    labelWidth:this.labelWidth,
		    labelAlign:this.alignR,
		    labelStyle:{overflow:'hidden'},
		    layout: 'hbox',
		    items :[icon]
	    });

	    var picFiles = [
	        iconWrapper,
	        CWHF.createLabelComponent('admin.user.profile.lbl.avatarName',
	            'avatarName', {labelWidth:this.labelWidth}),
	        CWHF.createFileField('admin.user.profile.lbl.avatarNew', 'iconFile',
	            {allowBlank:false,
	                labelWidth:this.labelWidth})
	    ];
	    return Ext.create('Ext.form.Panel', {
	            bodyStyle: 'padding:5px',
	            url: 'avatar!upload.action',
	            defaults: {
	                labelStyle:'overflow: hidden;',
	                margin:"5 5 0 0",
	                msgTarget:	'under',
	                anchor:	'-20'
	            },
	            method: 'POST',
	            fileUpload: true,
	            items: picFiles
	        }
	    );
	},

	uploadFileHandler: function(window, submitUrl, submitUrlParams, extraConfig) {
	    var theForm = this.formEdit.getForm();
	    if (!theForm.isValid()) {
	        Ext.MessageBox.alert(getText("admin.customize.list.title.upload", getText("admin.user.profile.lbl.avatar")),
	            getText("admin.customize.list.lbl.iconFileNotSpecified"));
	        return;
	    }
	    var iconFile = this.formEdit.getComponent('iconFile');
	    if (!this.validateFileExtension(iconFile.getRawValue())) {
	        Ext.MessageBox.alert(getText("admin.customize.list.title.upload", "icon"),
	            getText("admin.customize.list.lbl.iconFileWrongType"));
	        return;
	    }
	    theForm.submit({
	        scope: this,
	        params: submitUrlParams,
	        success: function(form, action) {
	            this.renderUploadPostDataProcess(action.result.data, this.formEdit, extraConfig);
	        },
	        failure: function(form, action) {
	            com.trackplus.util.submitFailureHandler(form, action);
	        }
	    })
	},

	validateFileExtension: function(fileName) {
	    var exp = /^.*\.(jpg|JPG|png|PNG|gif|GIF)$/;
	    return exp.test(fileName);
	},

	deleteUploadedFileHandler : function(window, submitUrl, submitUrlParams, extraConfig) {
	    var pictUploadForm = this.formEdit;
	    Ext.Ajax.request({
	        url : submitUrl,
	        scope: this,
	        params: submitUrlParams,
	        success : function(response) {
	            var result = Ext.decode(response.responseText);
	            this.renderUploadPostDataProcess(result.data, pictUploadForm, extraConfig);
	        }
	    })
	},

	renderUploadPostDataProcess: function(data, formPanel, extraConfig) {
	    //actualize the local avatar icon
	    var avatarIcon = formPanel.getComponent("iconPanel").getComponent("avatarIcon");
	    avatarIcon.setSrc(data.icon);
	    //actualize the local avatar name
	    var avatarName =  formPanel.getComponent("avatarName");
	    avatarName.setValue(data.iconName);
	    //empty the file upload component (after either upload or delete)
	    formPanel.getComponent('iconFile').setValue('');
	    var toolbars = this.win.getDockedItems('toolbar[dock="bottom"]');
	    if (toolbars!=null) {
	        //disable delete button if no icon is specified
	        toolbars[0].getComponent(1).setDisabled(data.iconName==null || data.iconName=="");
	    }
	    if (extraConfig!=null) {
	        //actualize the avatar icon on the user profile
	        var avatarIcon = extraConfig.avatarIcon;
	        if (avatarIcon!=null) {
	            avatarIcon.setSrc(data.icon);
	            if (this.context==2) {
	                var iconKey = avatarIcon.ownerCt.getComponent("iconKey");
	                if (iconKey!=null) {
	                    iconKey.setValue(data.iconKey);
	                }
	                var hiddenIconName = avatarIcon.ownerCt.getComponent("iconName");
	                if (hiddenIconName!=null) {
	                    hiddenIconName.setValue(data.iconName);
	                }
	            }
	        }
	    }
	},
	/**
	 * Creates the full text search configuration tab
	 * @param {Object} jsonData The data required for this and other tabs
	 * @return {Ext.form.Panel} A panel for this tab
	 */
	/*createTabWatchlist:function(){
		var panel=new Ext.Panel({
			title:getText('admin.user.profile.lbl.tabWatchlist'),
			id: 'tab.watchlist',
			layout: {
				type: 'anchor',
			},
			items: [{
				xtype: 'fieldset',
				width: this.FieldSetWidth,
				title: '',
				collapsible: false,
				defaultType: 'textfield',
				defaults: {anchor: '100%'},
				layout: 'anchor',
				items: []
			}]
		});
		return panel;
	},*/

	getEditUrl: function(context) {
		switch (context) {
		case this.CONTEXT.SELFREGISTRATION:
			return "userProfile!loadSelfRegistration.action";
		case this.CONTEXT.PROFILEEDIT:
			return "userProfile!loadEditMyProfile.action";
		case this.CONTEXT.USERADMINADD:
			return "userProfile!loadAddUser.action";
		case this.CONTEXT.USERADMINEDIT:
			return "userProfile!loadEditUser.action";
		}
	},

	loadProfile: function() {
		var me=this;
		this.createMainForm();
		var theUrl = this.getEditUrl(this.context);
		borderLayout.setLoading(true);
		this.mainForm.getForm().load({url: theUrl,
			params:{context:this.context},
			scope: this,
			clientValidation:false,
			success: function(form, action) {
				try{
					if(this.btnSave==null){
						this.createSaveButton();
					}
					this.btnSave.setDisabled(false);
					this.jsonData=action.result.data;
					this.postDataLoadCombos(this.jsonData, this.mainForm);
				}catch(ex){}
				borderLayout.setLoading(false);
			},
			failure:function(form, action){
				borderLayout.setLoading(false);
				com.trackplus.util.submitFailureHandler(form, action);
			}
		});
		return this.mainForm;
	},

	createMainForm: function(extraCfg) {
		var tabPanel = this.createTabPanel();
		var formCfg={
			url:'userProfile!save.action',
			border: false,
			margin: '3 0 0 0',
			baseCls:'x-plain',
			layout:'fit',
			items:[tabPanel]
		};
		if (extraCfg!=null) {
			//add extra panel configuration
			for (propertyName in extraCfg) {
				formCfg[propertyName] = extraCfg[propertyName];
			}
		}
		this.mainForm = Ext.create('Ext.form.Panel',formCfg);
		return this.mainForm;
	},

	/**
	 * Create the main component here.
	 * @param {Object} jsonData The data required for this component
	 * @return {Ext.form.Panel} The user profile configuration form
	 */
	createTabPanel:function(){
		this.applyPasswordVType();
		var tabPanel = new Ext.TabPanel({
			itemId: 'tabPanel',
			plain:true,
			border:false,
			bodyBorder:false,
			defaults:{
				border:false,
				autoScroll:true,
				bodyStyle:{
					border:'none'
					//padding:'10px'
				}
			}
		});
		tabPanel.add(this.createTabMain());
		if (this.context != this.CONTEXT.SELFREGISTRATION) {
			tabPanel.add(this.createTabEmail());
			tabPanel.add(this.createTabOther());
			//tabPanel.add(this.createTabWatchlist());
		}
		return tabPanel;
	},

	/**
	 * The save function to submit the modified user profile
	 * to the server.
	 */
	save:function(){
		borderLayout.setLoading(true);
		var tabBar=this.getTabPanel().getTabBar();
		for(var i=0;i<tabBar.items.length;i++){
			var headerCm=tabBar.getComponent(i);
			headerCm.removeCls("errorTab");
		}
		if(!this.mainForm.getForm().isValid()){
			var invalidFields = this.mainForm.getForm().getFields().filterBy(function(field) {
				return !field.validate();
			});
			var tabErrors=new Array();
			for(var i=0;i<invalidFields.items.length;i++){
				var field=invalidFields.items[i];
				var id=field.getItemId();
				var prefix=id.substring(0,id.indexOf('.'));
				if(!Ext.Array.contains(tabErrors,prefix)){
					tabErrors.push(prefix);
				}
			}
			this.markErrorTabs(tabErrors);
			borderLayout.setLoading(false);
			CWHF.showMsgError(getText('admin.user.profile.err.errorSave'));
			return false;
		}
		borderLayout.setLoading(true);
		this.mainForm.getForm().submit({
			url:'userProfile!save.action',
			params: {context: this.context},
			scope: this,
			success: function(form, action) {
				borderLayout.setLoading(false);
				CWHF.showMsgInfo(getText('admin.user.profile.lbl.successSave'));
				var result = action.result;
				if (result.localeChange) {
					window.location.href = com.trackplus.TrackplusConfig.contextPath + "/admin.action";
				}
			},
			failure: function(form, action) {
				borderLayout.setLoading(false);
				if (action.failureType!='client') {
					this.handleErrors(action.result.errors);
				}
				CWHF.showMsgError(getText('admin.user.profile.err.errorSave'));
			}
		});
	},

	validateLoginName:function(selfRegForm){
		this.validateUser(selfRegForm,'userProfile!validateLoginName.action');
	},
	validateUser:function(selfRegForm,url){
		var urlStr='userProfile!validateUser.action';
		if(url!=null){
			urlStr=url;
		}
		var form=this.mainForm;
		if(selfRegForm==true){
			form=this.selfRegForm;
		}else{
			var tabBar=this.getTabPanel().getTabBar();
			for(var i=0;i<tabBar.items.length;i++){
				var headerCm=tabBar.getComponent(i);
				headerCm.removeCls("errorTab");
			}
		}
		form.getForm().submit({
			url:urlStr ,
			params: {context: this.context, personId:this.personId},
			clientValidation: false,
			scope: this,
			success: function(form, action) {
			},
			failure: function(form, action) {
				if (action.failureType!='client') {
					if(selfRegForm==true){
						this.handleErrorsSelfReg(action.result.errors);
					}else{
						this.handleErrors(action.result.errors);
					}
				}
			}
		});
	},

	/**
	 * Load the combos and some other data after the result has arrived.
	 *
	 */
	postDataLoadCombos: function(lData, form) {

		tabPanel = form.getComponent(0);
		var mainTab = tabPanel.getComponent('tab.main');
		var emailTab = tabPanel.getComponent('tab.remail');
		var otherTab = tabPanel.getComponent('tab.other');

		if (lData['main.adminOrGuest']==true) {
			this.getWrappedControl("tab.main", "fslogin", "main.userName").setReadOnly(true);
		}
		var locale = this.getWrappedControl("tab.main", "fsname", "main.locale");
		locale.store.loadData(lData['main.locales']);
		locale.setValue(lData['main.locale']);

		var timezones = this.getWrappedControl("tab.main", "fsname", "main.userTz");
		timezones.store.loadData(lData['main.timeZones']);
		timezones.setValue(lData['main.userTz']);

		this.getHelpWrapper("tab.main", "fslogin", "main.ldapUser").setDisabled(!lData['main.ldapOn'] || lData['main.forceLdap']);

		if (lData['context'] == this.CONTEXT.SELFREGISTRATION) {
			if (emailTab != null) {
				emailTab.setDisabled(true);
			}
			if (otherTab != null) {
				otherTab.setDisabled(true);
			}
		}
		var prios = this.getWrappedControl("tab.remail", "fsremail3", "remail.remindPriorityLevel");
		prios.store.loadData(lData['remail.remindPriorityLevels']);
		prios.setValue(lData['remail.remindPriorityLevel']);

		var sevs = this.getWrappedControl("tab.remail", "fsremail3", "remail.remindSeverityLevel");
		sevs.store.loadData(lData['remail.remindSeverityLevels']);
		sevs.setValue(lData['remail.remindSeverityLevel']);

		var days = this.getWrappedControl("tab.remail", "fsremail3", "remail.remindMeOnDays");
		days.store.loadData(lData['remail.remindMeOnDaysList']);
		days.setValue(lData['remail.remindMeOnDays']);

		/*var deps = this.getWrappedControl("tab.other", "fsother1", "other.department");
		deps.store.loadData(lData['other.departments']);
		deps.setValue(lData['other.department']);*/

		var deps = this.getWrappedControl("tab.other", "fsother1", "other.department");
	    deps.updateData(lData["departmentTree"]);
	    deps.setValue(lData["department"]);
		//deps.setValue(lData['departmentLabel']);
		//deps.departmentID = lData['department'];

		var des = this.getWrappedControl("tab.other", "fsother2", "other.designPath");
		des.store.loadData(lData['other.designPaths']);
		des.setValue(lData['other.designPath']);
		if (lData['context'] == this.CONTEXT.USERADMINEDIT || lData['context'] == this.CONTEXT.USERADMINADD ) {
			this.getHelpWrapper("tab.other", "fsother3", "other.sessionTimeout").setDisabled(false);
		} else {
			this.getHelpWrapper("tab.other", "fsother3", "other.sessionTimeout").setDisabled(true);
		}
		tabPanel.setActiveTab(mainTab);

		//this.applyMyDomainVType(lData['main.domainPat']);
		//add userEmail only now bacause of the vtype
		this.getControl("tab.main", "fslogin").add(CWHF.createTextFieldWithHelp('admin.user.profile.lbl.userEmail','main.userEmail',
				{vtype:'email',vtypeText:getText('admin.user.profile.err.emailAddress.format'), allowBlank:false, maxLength:60,afterLabelTextTpl:this.reqTpl,value:lData["main.userEmail"],
					listeners: {
						blur: {
							element: 'el',
							scope: this,
							fn: function() {
								this.validateUser.call(this);
							}
						}
					}
				}
			)
		);
		//this.validateUser();
	    var avatarIcon = this.getControl("tab.other", "fsother1", "iconPanel", "avatarIcon")
	    var iconUrl = lData["other.iconUrl"];
	    if (iconUrl!=null)  {
	        avatarIcon.setSrc(iconUrl);
	    }
		var userLevel = this.getWrappedControl("tab.other", "fsother2", "other.userLevel");
	    if (userLevel!=null) {
	        userLevel.store.loadData(lData['userLevelList']);
	        userLevel.setValue(lData['other.userLevel']);
			userLevel.setDisabled(lData['readOnly']);
	    }
	    var substitutePerson = this.getWrappedControl("tab.other", "fsother4", "other.substituteID");
	    if (substitutePerson!=null) {
	        substitutePerson.store.loadData(lData['other.substituteList']);
	        substitutePerson.setValue(lData['other.substituteID']);
	    }
	},

	applyMyDomainVType: function(domainPattern) {
		// Add the additional 'advanced' VTypes
		Ext.apply(Ext.form.field.VTypes, {
			myDomain: function(val) {
				var domainExp1=/\b[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}\b/;
				var tst=domainExp1.test(val.toLowerCase());
				if (!tst) {
					Ext.form.field.VTypes["myDomainText"] = getText('admin.user.profile.err.emailAddress.format');
					return tst;
				}
				var domainExp2=new RegExp(domainPattern);
				tst=domainExp2.test(val.toLowerCase());
				if (!tst) {
					Ext.form.field.VTypes["myDomainText"]=getText('admin.user.profile.err.emailAddress.domain');
					return tst;
				}
				return tst;
			},
			myDomainText:'' // will be substituted, see above
		});
	},

	applyPasswordVType: function() {
		// Add the additional 'advanced' VTypes
		Ext.apply(Ext.form.field.VTypes, {
			password: function(val, field) {
				if (field.initialPassField) {
					// var pwd = field.up('form').down('#' + field.initialPassField);
					var pwd = Ext.getCmp(field.initialPassField);
					return (val == pwd.getValue());
				}
				return true;
			},
			passwordText: getText('admin.user.profile.err.password.match')
		});
	},

	handleErrorsSelfReg:function(errors){
		var me=this;
		var errStr='';
		if (errors!=null && errors.length>0) {
			for(var i=0;i<errors.length;i++){
				var error=errors[i];
				var id=error.id;
				var inputComp=null;
				if(id!=null){
					inputComp=CWHF.getWrappedControl.apply(me.selfRegForm, [id]);
				}
				if(inputComp!=null){
					inputComp.markInvalid(error.label);
				}else{
					errStr+=error.label+"</br>";
				}
			}
			if(errStr!=''){
				CWHF.showMsgError(error.label);
			}
		}
	},

	handleErrors:function(errors){
		var errStr='';
		var tabErrors=new Array();
		if (errors!=null && errors.length>0) {
			for(var i=0;i<errors.length;i++){
				var error=errors[i];
				var id=error.id;
				var inputComp=null;
				if(id!=null){
					inputComp=Ext.getCmp(id);
					var prefix=id.substring(0,id.indexOf('.'));
					if(!Ext.Array.contains(tabErrors,prefix)){
						tabErrors.push(prefix);
					}
				}
				if(inputComp!=null){
					inputComp.markInvalid(error.label);
				}else{
					errStr+=error.label+"</br>";
				}
			}
			if(errStr!=''){
				CWHF.showMsgError(error.label);
			}
			this.markErrorTabs(tabErrors);
		}
	},
	markErrorTabs:function(tabErrors){
		var tabErrorsCmp=new Array();
		if (tabErrors.length>0) {
			for(var i=0;i<tabErrors.length;i++){
				var tabID='tab.'+tabErrors[i];
				var tabComp=this.getControl(tabID);
				if(tabComp!=null){
					tabErrorsCmp.push(tabComp);
				}
			}
			if (tabErrorsCmp.length>0) {
				var selectedTab=this.getTabPanel().getActiveTab();
				if(!Ext.Array.contains(tabErrorsCmp,selectedTab)){
					this.getTabPanel().setActiveTab(tabErrorsCmp[0])
				}
				var tabBar=this.getTabPanel().getTabBar();
				for(var i=0;i<tabErrorsCmp.length;i++){
					var index=this.getTabPanel().items.findIndex('id', tabErrorsCmp[i].id);
					var headerCm=tabBar.getComponent(index);
					headerCm.addCls("errorTab");
				}
			}
		}
	},

	createSelfRegFormPanel:function(){
		var me=this;
		me.applyPasswordVType();
		var form=Ext.create('Ext.form.Panel', {
			layout:'anchor',
			border	: false,
			autoScroll:true,
			margin: '0 0 0 0',
			bodyStyle:{
				padding:'10px'
			},
			itemId: 'panel.selfreg',
			/*style:{
				borderBottom:'1px solid #D0D0D0'
			}, */
			items:[
			CWHF.createTextFieldWithHelp('admin.user.profile.lbl.userName','main.userName',
						{enableKeyEvents:true,allowBlank:false,
						listeners:
						{
							//keyup: {
							blur: {
//									element: 'el',
									scope: this,
									fn: function() {
										this.validateLoginName.call(this,true);
									}
								}
							}
						}
				),
				CWHF.createCheckboxWithHelp('admin.user.profile.lbl.ldapUser', 'main.ldapUser', {},
						{change:{
								fn: this.changeLdapSelReg,
								scope: this
							}
						}
				),
				CWHF.createTextField('admin.user.profile.lbl.passwd','main.passwd',
								{inputType:'password',minLength:5, id:'main.passwd'}),
				CWHF.createTextField('admin.user.profile.lbl.passwd2','main.passwd2',
								{inputType:'password',vtype:'password',initialPassField:'main.passwd'}),
				CWHF.createTextFieldWithHelp('admin.user.profile.lbl.userEmail','main.userEmail',{vtype:'email',allowBlank:false}),
				CWHF.createTextField('admin.user.profile.lbl.lastName','main.lastName',  {allowBlank:false},
					//{listeners:
					{
						blur: {
							element: 'el',
							fn: function() {
								this.validateUser.call(this,true);
							},
							scope: this
						}
					 }
				),
				CWHF.createTextField('admin.user.profile.lbl.firstName','main.firstName',  {allowBlank:false},
					//{listeners:
						{
							blur: {
								element: 'el',
								fn: function() {
									this.validateUser.call(this,true);
								},
								scope: this
							}
					 //}
					}),
				CWHF.createComboWithHelp('admin.user.profile.lbl.locale','main.locale',{idType:'string'}),
				CWHF.createComboWithHelp('admin.user.profile.lbl.usertz','main.userTz',{idType:'string'})
			]
		});
		me.selfRegForm=form;
		return form;
	}
});



com.trackplus.admin.user.selfRegistrationAfterSubmit=function(params, result){
	var title=getText("logon.lbl.register");
	var emailSent=result.emailSent;
	var email=result.email;
	var message="";
	if(emailSent==true){
		message=getText("logon.register.msg.registeredWithEmailSent",email);
	}else{
		message=getText("logon.register.msg.registeredNoEmailSent");
	}
	Ext.MessageBox.show({
		title: title,
		msg: message,
		width: 400,
		buttons: Ext.Msg.OK,
		icon: Ext.MessageBox.INFO
	});
}
com.trackplus.admin.user.createSelfRegistrationDialog=function(){
	var context={
		selfRegistration:0
	};
	var profile=Ext.create('com.trackplus.admin.user.Profile',{
		context:context
	});
	var submit = {
		submitUrl:'userProfile!save.action?context=0',
		refreshAfterSubmitHandler: com.trackplus.admin.user.selfRegistrationAfterSubmit
	};
	var formPanel=profile.createSelfRegFormPanel();
	var windowParameters = {
		title:getText('admin.user.profile.title.create'),
		width:640,
		height:350,
		submit:submit,
		submitButtonText:getText('logon.lbl.register'),
		formPanel:formPanel
	};
	formPanel.getForm().load({
		url: 'userProfile!loadSelfRegistration.action',
		params:{
			context:context
		},
		scope: this,
		clientValidation:false,
		success: function(form, action) {
			try{
				profile.jsonData=action.result.data;
				var lData=action.result.data;

				var locale =CWHF.getWrappedControl.call(formPanel,"main.locale");
				locale.store.loadData(lData['main.locales']);
				locale.setValue(lData['main.locale']);

				var timezones = CWHF.getWrappedControl.call(formPanel, "main.userTz");
				if(timezones!=null){
					timezones.store.loadData(lData['main.timeZones']);
					timezones.setValue(lData['main.userTz']);
				}
				CWHF.getWrappedControl.call(formPanel, "main.ldapUser").setDisabled(!lData['main.ldapOn'] || lData['main.forceLdap']);
			}catch(ex){}
		},
		failure:function(form, action){
			com.trackplus.util.submitFailureHandler(form, action);
		}
	});
	var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
	windowConfig.showWindowByConfig(profile);
	// windowConfig.disableSubmitButton(profile.win,true,0);
	return {
		profile:profile,
		windowConfig:windowConfig
	};
};



