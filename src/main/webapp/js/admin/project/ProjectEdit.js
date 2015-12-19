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


Ext.define("com.trackplus.admin.project.ProjectEdit", {
	extend : "com.trackplus.admin.FormBase",
	xtype: "projectEdit",
    controller: "projectEdit",
	
	//the projectID: needed both by add (onCancel) and edit (onSave) 
	projectID: null,
	//whether a template or a project is edited
	isTemplate : false,
	templateIsActive: false,
	//used by load/save
	addProject: false,
	addAsSubproject: false,
	addAsPrivateProject: false,
	
	inDialog: false,
	refreshAfterSubmitHandler: null,
	
	hasPrivateProject: false,
	//used in cancel
	lastSelections: null,
	//the last selected project tab
	lastSelectedTab: null,
	
	tabPanel: null,
	
	// actions
	actionSave : null,
	actionCancel : null,
	
	labelWidth : 200,
	textFieldWidth : 200 + 300,
	textFieldWidthShort : 200 + 150,
	textFieldWidthCheck : 200 + 80,
	FieldSetWidth : 250 + 300 + 150,
	alignR : "right",
	prefillByWidth : 200 + 350,
	protocolWidth : 200 + 350,
	securityConnectionWidth : 200 + 350,

	EMAIL_PROTOCOL : {
	    POP3 : 'pop3',
	    IMAP : 'imap'
	},

	SECURITY_CONNECTIONS_MODES : {
	    NEVER : 0,
	    TLS_IF_AVAILABLE : 1,
	    TLS : 2,
	    SSL : 3
	},

	DEFAULT_EMAIL_PORTS : {
	    POP3 : 110,
	    POP3_SSL : 995,
	    IMAP : 143,
	    IMAP_SSL : 993
	},

	PREFILL : {
	    LASTWORKITEM : 1,
	    PROJECTDEFAULT : 2
	},

	/**
	 * Initialization method
	 */
	initBase : function() {
		var entityContext = this.getEntityContext();
		this.projectID = entityContext.projectID;
		this.isTemplate = entityContext.isTemplate;
		this.templateIsActive = entityContext.templateIsActive;
		this.addProject = entityContext.addProject;
		this.addAsSubproject = entityContext.addAsSubproject;
		this.addAsPrivateProject = entityContext.addAsPrivateProject;
		this.hasPrivateProject = entityContext.hasPrivateProject;
		this.inDialog = entityContext.inDialog;
		if (this.inDialog) {
			this.toolbarDock = "bottom";
		}
		this.lastSelections = entityContext.lastSelections;
		if (this.lastSelections) {
			this.lastSelectedTab = this.lastSelections.lastSelectedTab;
		}
		//this.lastSelectedTab = entityContext.lastSelectedTab;
		this.initActions();
	},
	
	/**
	 * ****************Methods strictly for project CRUD********************
	 */

	/**
	 * The iconCls for the edit button, overwrites base class
	 * icon
	 */
	getSaveIconCls : function() {
	    return 'projecttSave';
	},
	
	
	initActions : function() {
		this.actionSave = CWHF.createAction(this.getSaveButtonKey(), this.getSaveIconCls(),
	    		"onSave", {tooltip:this.getActionTooltip(this.getSaveTitleKey())});
		this.actions = [this.actionSave];
		if (this.addProject || this.inDialog) {
			this.actionCancel = CWHF.createAction(this.getCancelButtonKey(), this.getCancelIconCls(),
	    		"onCancel");
			this.actions.push(this.actionCancel);
		}
	},

	loadUrl: "project!load.action",
	
	getLoadParams: function() {
		var params = new Object();
		if (this.projectID) {
			params["projectID"]=this.projectID;
		}
		params["add"]=this.addProject;
		params["addAsSubproject"]=this.addAsSubproject;
		params["addAsPrivateProject"]=this.addAsPrivateProject;
		params["isTemplate"] = this.isTemplate;
		return params;
	},
	
	getSubmitParams: function() {
		var params = new Object();
		if (this.projectID) {
			params["projectID"]=this.projectID;
		}
		params["add"]=this.addProject;
		params["addAsSubproject"]=this.addAsSubproject;
		params["addAsPrivateProject"]=this.addAsPrivateProject;
		params["isTemplate"] = this.isTemplate;
		return params;
	},

	getFormFields: function() {
		var activeTab = this.lastSelectedTab;
	    /*if (this.getLastSelections()) {
	    	activeTab = this.getView().getLastSelections().lastSelectedTab;
		}*/
	    if (CWHF.isNull(activeTab)) {
	        activeTab = 0;
	    }
	    var tabs = [this.createTabMain(), this.createTabDefault()];
	    if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
	    	tabs.push(this.createTabEmail());
	    }
	    this.tabPanel = Ext.create("Ext.tab.Panel", {
	        itemId: "tabPanel",
	        plain: true,
	        border: false,
	        bodyBorder: false,
	        cls: "projectConfig",
	        defaults: {
	            border: false,
	            autoScroll: true,
	            bodyStyle: {
	                border: 'none',
	                padding: '0px'
	            }
	        },
	        items: tabs,
	        activeTab: activeTab,
	        listeners: {"tabChange":"onTabChange"}
	    });
	    return [this.tabPanel];
	},
	
	/*tabActivate: function(tab) {
	    var tabIndex = this.tabPanel.items.indexOf(tab);
	   // this.getView().getLastSelections().lastSelectedTab = tabIndex;
	    var storeTabUrl = "project!storeLastSelectedTab.action";
	    Ext.Ajax.request({
	        fromCenterPanel : true,
	        url : storeTabUrl,
	        disableCaching : true,
	        method : 'POST',
	        params : {
	            "tabIndex" : tabIndex
	        }
	    });
	},*/
	
	/**
	 * Creates the main project tab
	 * @return {Ext.form.Panel} A panel for this tab
	 */
	createTabMain: function() {
	    var items = [];
	    items.push(CWHF.createTextField('common.lbl.name', 'projectBaseTO.label', {
	        allowBlank : false,
	        maxLength : 255
	    }));
	    items.push(CWHF.createTextAreaField('common.lbl.description', 'projectBaseTO.description', {
	        height : 150,
	        maxLength : 255
	    }));
	    items.push(CWHF.createComboWithHelp('common.lbl.projectType', 'projectBaseTO.projectTypeID',
	            {itemId:'projectType'}, { "change": "onProjectTypeOrIssueTypeChange"}));
	    if (!this.isTemplate) {
	        items.push(CWHF.createComboWithHelp('admin.customize.localeEditor.type.projectStatus',
	                'projectBaseTO.projectStatusID', {itemId:'projectStatus'}, null));
	    }
	    var panel = Ext.create('Ext.panel.Panel', {
	        title : getText('admin.project.lbl.mainTab'),
	        itemId : 'mainTab',
	        items : [{
	                    xtype : 'fieldset',
	                    itemId : 'fsbasic',
	                    width : this.FieldSetWidth,
	                    title : getText('admin.project.lbl.fsbasic'),
	                    collapsible : false,
	                    defaultType : 'textfield',
	                    defaults : {
	                        anchor : '100%'
	                    },
	                    layout : 'anchor',
	                    items : items
	                }, {
	                    xtype : 'fieldset',
	                    itemId : 'fslnk',
	                    width : this.FieldSetWidth,
	                    title : getText('admin.project.lbl.linking'),
	                    collapsible : false,
	                    defaults : {
	                        anchor : '100%'
	                    },
	                    layout : 'anchor',
	                    items : [ CWHF.createCheckboxWithHelp('admin.project.lbl.linking',
	                            'projectBaseTO.linking', {
	                                width : this.textFieldWidthCheck,
	                                itemId : 'projectBaseTOLinking'
	                            }) ]
	                }/*, {
	                    xtype : 'hidden',
	                    name : 'add',
	                    value : add
	                }*/]
	                
	    });
	    //panel.addListener('activate', this.tabActivate, this);
	    return panel;
	},
	
	
	/**
	 * Creates the project defaults tab
	 * @return {Ext.form.Panel} A panel for this tab
	 */
	createTabDefault : function() {
	    var me = this;
	    var panel = Ext.create("Ext.panel.Panel", {
	                    title : getText('admin.project.lbl.defaultTab'),
	                    itemId : 'defaultTab',
	                    items : [{xtype : 'fieldset',
	                              itemId : 'fsdef',
	                              width : this.FieldSetWidth,
	                              title : getText('admin.project.lbl.defaultFields'),
	                              collapsible : false,
	                              defaultType : 'textfield',
	                              defaults : {
	                            	  anchor : '100%'
	                              },
	                              layout : 'anchor',
	                              items : [CWHF.createComboWithHelp(
	                                                'admin.project.lbl.defaultManager',
	                                                'projectDefaultsTO.defaultManagerID',{itemId:'projectDefaultsTODefaultManagerID'}),
	                                        CWHF.createComboWithHelp(
	                                                'admin.project.lbl.defaultResponsible',
	                                                'projectDefaultsTO.defaultResponsibleID',{itemId:'projectDefaultsTODefaultResponsibleID'}),
	                                        CWHF.createComboWithHelp(
	                                                'admin.project.lbl.defaultIssueType',
	                                                'projectDefaultsTO.defaultIssueTypeID',{itemId:"projectDefaultsTODefaultIssueTypeID"}, {"change":"onProjectTypeOrIssueTypeChange"}),
	                                        CWHF.createComboWithHelp(
	                                                'admin.project.lbl.defaultPriority',
	                                                'projectDefaultsTO.defaultPriorityID',{itemId:'projectDefaultsTODefaultPriorityID'}),
	                                        CWHF.createComboWithHelp(
	                                                'admin.project.lbl.defaultSeverity',
	                                                'projectDefaultsTO.defaultSeverityID',{itemId:'projectDefaultsTODefaultSeverityID'}),
	                                        CWHF.getRadioGroupWithHelp(
	                                                        'admin.project.lbl.prefill',
	                                                        this.prefillByWidth,
	                                                        [{boxLabel : getText('admin.project.lbl.prefill.lastItem'),
	                                                          name : 'projectDefaultsTO.prefillBy',
	                                                          inputValue : this.PREFILL.LASTWORKITEM,
	                                                          checked : true
	                                                          },
	                                                          {boxLabel : getText('admin.project.lbl.prefill.projectDefault'),
	                                                           name : 'projectDefaultsTO.prefillBy',
	                                                           inputValue : this.PREFILL.PROJECTDEFAULT
	                                                           }],
	                                                           {
	                                                        	itemId : 'prefillBy',
	                                                            layout : 'checkboxgroup',
	                                                            columns : 2
	                                                        })]
	                            },
	                            {
	                                xtype : 'fieldset',
	                                itemId : 'fsInitSt',
	                                width : this.FieldSetWidth,
	                                //padding: '8 0 6 0',
	                                title : getText('admin.project.lbl.initialState'),
	                                collapsible : false,
	                                defaults : {
	                                    anchor : '100%'
	                                },
	                                layout : 'anchor',
	                                items : [
	                                        CWHF.createComboWithHelp(
	                                                'admin.project.lbl.defaultInitialState',
	                                                'projectDefaultsTO.initialStatusID',{itemId:'projectDefaultsTOInitialStatusID'}),
	                                        Ext.create('Ext.Component', {
	                                                            html : getText('admin.project.lbl.defaultIssueTypeInitialState'),
	                                                            margin : '0 5 5 5',
	                                                            border : true,
	                                                            cls : 'infoBox3'
	                                                        })]
	                            }]
	                    });
	    //panel.addListener('activate', me.tabActivate, me);
	    return panel;
	},
	
	/**
	 * Creates the project defaults tab
	 * @return {Ext.form.Panel} A panel for this tab
	 */
	createTabEmail : function() {
	    var me = this;
	    me.testBtn = Ext.create('Ext.button.Button', {
	        xtype : 'button',
	        style : {
	            marginTop : '10px',
	            marginBottom : '5px',
	            marginLeft : (me.labelWidth + 20) + 'px'
	        },
	        itemId : 'emailTab_testConnection',
	        enableToggle : false,
	        iconCls : 'check16',
	        text : getText('admin.server.config.testConnection'),
	        handler : "testEmailIncoming" /*function() {
	            me.testEmailIncoming.call(me);
	        }*/
	    });
	    var panel = Ext.create(
	    		'Ext.panel.Panel', {
	    			title : getText('admin.project.lbl.emailTab'),
	    			itemId : 'emailTab',
	    			layout : {
	    				type : 'anchor'
	    			},
	    			items : [{
	    				xtype : 'fieldset',
	    				itemId : 'fseserv',
	    				width : this.FieldSetWidth,
                        title : getText('admin.server.config.incomingServer'),
                        collapsible : false,
                        defaultType : 'textfield',
	                    defaults : {
	                    	anchor : '100%'
	                    },
	                    layout : 'anchor',
	                    items : [
	                    CWHF.createCheckboxWithHelp(
	                    		'admin.server.config.emailSubmission',
	                            'projectEmailTO.enabled', {itemId:"projectEmailTOEnabled"}, {
	                            change : {
	                            	fn: this.onChangeEmailEnabled,
	                                	scope: this
	                            }}),
	                             CWHF.getRadioGroupWithHelp('admin.server.config.mailReceivingProtocol',
	                            	this.protocolWidth, [{
	                            			 boxLabel : 'POP3',
	                            			 name : 'projectEmailTO.protocol',
	                            			 inputValue : 'pop3',
	                            			 checked : true
	                            		 }, {
	                            			 boxLabel : 'IMAP',
	                            			 name : 'projectEmailTO.protocol',
	                            			 inputValue : 'imap'
	                            		 }], {itemId:'projectEmailTOProtocol'}, {
	                            			 change: "changePort"/*{
	                            				 fn : this.changePort,
	                            				 scope : this
	                            			 }*/
	                            		 }),
	                            		 CWHF.createTextFieldWithHelp(
	                            				 'admin.server.config.mailReceivingServerName',
	                            				 'projectEmailTO.serverName',{itemId:'projectEmailTOServerName'}),
	                            		CWHF.createTextFieldWithHelp(
	                            				'admin.server.config.mailReceivingPort',
	                            				'projectEmailTO.port', {
	                            				width : this.textFieldWidthShort,
	                            				itemId : 'projectEmailTOPort'
	                            				})
	                            ]
	                    },{
	    				xtype : 'fieldset',
	    				itemId : 'fsauth',
	                                    width : this.FieldSetWidth,
	                                    title : getText('admin.server.config.incomingAuth'),
	                                    collapsible : false,
	                                    defaultType : 'textfield',
	                                    defaults : {
	                                        anchor : '100%'
	                                    },
	                                    layout : 'anchor',
	                                    items : [
	                                            CWHF.createTextFieldWithHelp(
	                                                    'admin.server.config.mailReceivingUser',
	                                                    'projectEmailTO.user', {itemId:'projectEmailTOUser'}),
	                                            CWHF.createTextFieldWithHelp(
	                                                    'admin.server.config.mailReceivingPassWord',
	                                                    'projectEmailTO.password', {
	                                                        inputType : 'password',
	                                                        itemId : 'projectEmailTOPassword'
	                                                    }),
	                                            CWHF.getRadioGroupWithHelp('admin.server.config.mailReceivingSecurityConnection',
	                                            		this.securityConnectionWidth,[
	                                                                    {
	                                                                        boxLabel : getText('admin.server.config.securityConnections.never'),
	                                                                        name : 'projectEmailTO.securityConnection',
	                                                                        inputValue : this.SECURITY_CONNECTIONS_MODES.NEVER
	                                                                    },
	                                                                    {
	                                                                        boxLabel : getText('admin.server.config.securityConnections.tlsIsAvailable'),
	                                                                        name : 'projectEmailTO.securityConnection',
	                                                                        inputValue : this.SECURITY_CONNECTIONS_MODES.TLS_IF_AVAILABLE
	                                                                    },
	                                                                    {
	                                                                        boxLabel : getText('admin.server.config.securityConnections.tls'),
	                                                                        name : 'projectEmailTO.securityConnection',
	                                                                        inputValue : this.SECURITY_CONNECTIONS_MODES.TLS
	                                                                    },
	                                                                    {
	                                                                        boxLabel : getText('admin.server.config.securityConnections.ssl'),
	                                                                        name : 'projectEmailTO.securityConnection',
	                                                                        inputValue : this.SECURITY_CONNECTIONS_MODES.SSL
	                                                                    } ], {itemId:'securityConnection'}, {
	                                                                change : "changePort"/*{
	                                                                    fn : this.changePort,
	                                                                    scope : this
	                                                                }*/
	                                                            }) ]
	                                },
	                                {
	                                    xtype : 'fieldset',
	                                    itemId : 'fsout',
	                                    width : this.FieldSetWidth,
	                                    title : getText('admin.project.lbl.fsoutgoing'),
	                                    collapsible : false,
	                                    defaultType : 'textfield',
	                                    defaults : {
	                                        anchor : '100%'
	                                    },
	                                    layout : 'anchor',
	                                    items : [
	                                            CWHF.createCheckboxWithHelp(
	                                                    'admin.project.lbl.useProjectFromAddress',
	                                                    'projectEmailTO.sendFromProjectEmail', {
	                                                        width : this.textFieldWidthCheck,
	                                                        itemId: 'projectEmailTOSendFromProjectEmail'
	                                                    }, {
	                                                        change : {
	                                                            fn : this.onEmailOutChange,
	                                                            scope : this
	                                                        }
	                                                    }),
	                                            CWHF.createTextFieldWithHelp(
	                                                    'admin.project.lbl.projectEmail',
	                                                    'projectEmailTO.projectFromEmail',
	                                                    {itemId: 'projectEmailTOProjectFromEmail'}),
	                                            CWHF.createTextFieldWithHelp(
	                                                    'admin.project.lbl.projectEmailPersonalName',
	                                                    'projectEmailTO.projectFromEmailName', {itemId:'projectEmailTOProjectFromEmailName'}),
	                                            CWHF.createCheckboxWithHelp(
	                                                    'admin.project.lbl.useProjectFromAsReplyTo',
	                                                    'projectEmailTO.sendFromProjectAsReplayTo', {
	                                                        width : this.textFieldWidthCheck,
	                                                        itemId : 'projectEmailTOSendFromProjectAsReplayTo'
	                                                    }) ]
	                                },
	                                {
	                                    xtype : 'fieldset',
	                                    itemId : 'fsother',
	                                    width : this.FieldSetWidth,
	                                    title : getText('admin.server.config.incomingOther'),
	                                    collapsible : false,
	                                    defaultType : 'textfield',
	                                    defaults : {
	                                        anchor : '100%'
	                                    },
	                                    layout : 'anchor',
	                                    items : [ CWHF.createCheckboxWithHelp(
	                                            'admin.server.config.keepMessagesOnServer',
	                                            'projectEmailTO.keepMessagesOnServer', {
	                                                itemId : 'keep',
	                                                width : this.textFieldWidthCheck
	                                            }) ]
	                                }, me.testBtn ]
	                    });
	    //panel.addListener('activate', me.tabActivate, me);
	    return panel;
	},
	
	
	/**
	 * Handler for changing an invalid value handling
	 */
	onEmailOutChange : function(field, newValue, oldValue, options) {
	    var eoutValue = field.getValue();
	    this.enableEmailOutFields(eoutValue);
	},

	enableEmailOutFields : function(eout) {
	    this.getHelpWrapper("emailTab", "fsout", "projectEmailTOProjectFromEmail").setDisabled(!eout);
	    this.getHelpWrapper("emailTab", "fsout", "projectEmailTOProjectFromEmailName").setDisabled(!eout);
	    this.getHelpWrapper("emailTab", "fsout", "projectEmailTOSendFromProjectAsReplayTo").setDisabled(!eout);
	},

	onChangeEmailEnabled : function(checkBox, newValue, oldValue, eOpts) {
	    disabled = !newValue;
	    this.getHelpWrapper("emailTab", "fseserv", "projectEmailTOProtocol").setDisabled(disabled);
	    this.getHelpWrapper("emailTab", "fseserv", "projectEmailTOServerName").setDisabled(disabled);
	    this.getHelpWrapper("emailTab", "fseserv", "projectEmailTOPort").setDisabled(disabled);

	    this.getHelpWrapper("emailTab", "fsauth", "projectEmailTOUser").setDisabled(disabled);
	    this.getHelpWrapper("emailTab", "fsauth", "projectEmailTOPassword").setDisabled(disabled);
	    this.getHelpWrapper("emailTab", "fsauth", "securityConnection").setDisabled(disabled);

	    this.getHelpWrapper("emailTab", "fsother", "keep").setDisabled(disabled);
	    this.testBtn.setDisabled(disabled);
	},
		
	postDataProcess: function(data, panel) {
		// project main
		var me = this;
	    var mainTab = this.getControl("mainTab");
	    var projectType = this.getWrappedControl("mainTab", "fsbasic", "projectType");
	    projectType.store.loadData(data['projectTypeList']);
	    projectType.setValue(data['projectBaseTO.projectTypeID']);
	    // add the change listener only here: if it would be
		// added by form creation by setting
	    // the listenerConfig parameter the setValue() above
		// would trigger
	    // onProjectTypeOrIssueTypeChange which is not desirable
	    //projectType.on('change', this.onProjectTypeOrIssueTypeChange, this);

	    if (!this.isTemplate) {
	        var projectStatus = this.getWrappedControl("mainTab", "fsbasic", "projectStatus");
	        projectStatus.store.loadData(data['projectStatusList']);
	        projectStatus.setValue(data['projectBaseTO.projectStatusID']);
	    }
	    var hasPrefix = data['hasPrefix'];
	    if (hasPrefix) {
	    	var workspacePrefixTxtBoxChangeObj = {};
	    	workspacePrefixTxtBoxChangeObj.change = function(txtBox, newValue, oldValue, eOpts) {
        		me.checkNewWorkspacePrefix(txtBox, newValue, me.getRootID());
	    	};

	    	mainTab.add({
	            xtype : 'fieldset',
	            itemId : 'fsprefix',
	            width : this.FieldSetWidth,
	            title : getText('admin.project.lbl.prefix'),
	            collapsible : false,
	            defaults : {
	                anchor : '100%'
	            },
	            layout : 'anchor',
	            items : [ CWHF.createTextFieldWithHelp('admin.project.lbl.prefix',
	                    'projectBaseTO.prefix', {
	                        width : this.textFieldWidthShort,
	                        value : data['projectBaseTO.prefix'],
	                        allowBlank : false,
	                        readOnly: data['projectBaseTO.projectTypeID'] < 0
	                    }, workspacePrefixTxtBoxChangeObj)]
	        });
	    }
	    var hasAccounting = data['hasAccounting'];
	    if (hasAccounting) {
	        this.addAccountingFieldSet(data, mainTab);
	    }

	    var hasRelease = data['hasRelease'];
	    if (hasRelease) {
	        this.addReleaseFieldSet(data, mainTab);
	    }

	    // default tab
	    var defaultManager = this.getWrappedControl("defaultTab", "fsdef",
	            "projectDefaultsTODefaultManagerID");
	    defaultManager.store.loadData(data['managerList']);
	    defaultManager.setValue(data['projectDefaultsTO.defaultManagerID']);

	    var defaultResponsible = this.getWrappedControl("defaultTab", "fsdef",
	            "projectDefaultsTODefaultResponsibleID");
	    defaultResponsible.store.loadData(data['responsibleList']);
	    defaultResponsible.setValue(data['projectDefaultsTO.defaultResponsibleID']);

	    var defaultIssueType = this.getWrappedControl("defaultTab", "fsdef",
	            "projectDefaultsTODefaultIssueTypeID");
	    defaultIssueType.store.loadData(data['issueTypeList']);
	    defaultIssueType.setValue(data['projectDefaultsTO.defaultIssueTypeID']);
	    // add the change listener only here: if it would be
		// added by form creation by setting
	    // the listenerConfig parameter the setValue() above
		// would trigger
	    // onProjectTypeOrIssueTypeChange which is not desirable
	    //defaultIssueType.on('change', this.onProjectTypeOrIssueTypeChange, this);

	    var defaultPriority = this.getWrappedControl("defaultTab", "fsdef",
	            "projectDefaultsTODefaultPriorityID");
	    defaultPriority.store.loadData(data['priorityList']);
	    defaultPriority.setValue(data['projectDefaultsTO.defaultPriorityID']);

	    var defaultSeverity = this.getWrappedControl("defaultTab", "fsdef",
	            "projectDefaultsTODefaultSeverityID");
	    defaultSeverity.store.loadData(data['severityList']);
	    defaultSeverity.setValue(data['projectDefaultsTO.defaultSeverityID']);

	    // add the initial statuses for issueTypes only when the
		// data has arrived
	    var fsInitSt = this.getControl("defaultTab", "fsInitSt");

	    var initialStatus = this.getWrappedControl("defaultTab", "fsInitSt",
	            "projectDefaultsTOInitialStatusID");
	    initialStatus.store.loadData(data['statusList']);
	    initialStatus.setValue(data['projectDefaultsTO.initialStatusID']);

	    initStItems = [];
	    var initStatusData = data['initStatuses'];
	    if (initStatusData ) {
	        Ext.Array.forEach(initStatusData, function(item) {
	            var issueType = item['issueType'];
	            var status = item['status'];
	            var active = item['active'];
	            var statusList = item['statusList'];
	            var comboName = 'projectDefaultsTO.issueTypeToInitStatusMap[' + issueType + ']';
	            if (issueType < 0) {
	                comboName = 'projectDefaultsTO.issueTypeNegativeToInitStatusMap[' + (-issueType)
	                        + ']';
	            }
	            var comboControl = CWHF.createCombo(item['label'], comboName, {
	                value : status,
	                labelWidth : this.labelWidth - 40,
	                width : this.textFieldWidth - 60,
	                data : statusList,
	                disabled : !active,
	                labelIsLocalized : true
	            });
	            var issueTypePanel = new Ext.Panel({
	                border : false,
	                defaults : {
	                    margin : "4 4 0 0"
	                },
	                margin : "0 10 3 80",
	                layout : {
	                    type : 'hbox'
	                },
	                itemId : 'issueTypePanel' + issueType,
	                items : [ CWHF.createCheckbox('', '', {
	                    value : active,
	                    width : 20
	                }, {
	                    change : {
	                        fn : this.onStatusForIssueTypeChange,
	                        scope : this,
	                        comboControl : comboControl
	                    }
	                }), comboControl ]
	            });
	            initStItems.push(issueTypePanel);
	        }, this);
	        fsInitSt.add(initStItems);
	    }

	    var errors = data.errors;
	    this.markInvalidByErrors(errors, panel);
	    // email tab
	    if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
	        var eoutValue = this.getWrappedControl("emailTab", "fsout",
	                "projectEmailTOSendFromProjectEmail").getValue();
	        this.enableEmailOutFields(eoutValue);
	        var emailEnabled = this.getWrappedControl("emailTab", "fseserv", "projectEmailTOEnabled");
	        this.onChangeEmailEnabled(emailEnabled, emailEnabled.getValue());
	    }
	},
	
	checkNewWorkspacePrefix: function(txtBox, newValue, projectID) {
		Ext.Ajax.request({
	        url : "	project!validateProjectPrefix.action",
	        disableCaching : true,
	        success : function(result) {
	        	var jsonData = Ext.decode(result.responseText);
	        	if(jsonData.projectPrefixExists) {
	        		txtBox.markInvalid(getText('common.err.duplicatePrefix'));
	        	}
	        },
	        failure : function() {
	        },
	        method : 'POST',
	        params : {'projectBaseTO.prefix': newValue, 'projectID': projectID}
	    });
	},

	markInvalidByErrors : function(errors, panel) {
	    var accountErrorFound = false;
	    var systemListErrorFound = false;
	    if (errors ) {
	        var accountErrorMessage = errors['projectBaseTO.defaultAccount'];
	        if (accountErrorMessage ) {
	            var defaultAccount = this.getWrappedControl("mainTab", "fsacc",
	                    "projectBaseTO.defaultAccount");
	            defaultAccount.markInvalid(accountErrorMessage);
	            accountErrorFound = true;
	        }

	        var defaultSystemListIDs = [ "projectDefaultsTODefaultManagerID",
	                "projectDefaultsTODefaultResponsiblID", "projectDefaultsTOInitialStatusID",
	                "projectDefaultsTODefaultIssueTypeID", "projectDefaultsTODefaultPriorityID",
	                "projectDefaultsTODefaultSeverityID" ];
	        Ext.Array.forEach(defaultSystemListIDs, function(item) {
	            var errorMessage = errors[item];
	            if (errorMessage ) {
	                var control = this.getWrappedControl("defaultTab", "fsdef", item);
	                if (control ) {
	                    if (!systemListErrorFound) {
		                    systemListErrorFound = true;
	                    }
	                    control.markInvalid(errorMessage);
	                }
	            }
	        }, this);
	        if (!accountErrorFound && systemListErrorFound) {
	            var tabPanel = panel.getComponent('tabPanel');
	            tabPanel.setActiveTab('defaultTab');
	        }
	        /*
			 * var tabBar=tabPanel.getTabBar(); var
			 * headerCm=tabBar.getComponent(1);
			 * headerCm.addCls("errorTab");
			 */
	    }
	},
	
	addAccountingFieldSet : function(data, mainTab) {
	    var subproject = data["subproject"];
	    var accountingInherited = data["projectAccountingTO.accountingInherited"];
	    var workTracking = data["projectAccountingTO.workTracking"];
	    var costTracking = data["projectAccountingTO.costTracking"];
	    var items = [];
	    if (this.addAsSubproject || subproject) {
	        items.push(CWHF.createCheckboxWithHelp("admin.project.lbl.accountingInherited",
	                "projectAccountingTO.accountingInherited", {
	                    width : this.textFieldWidthCheck,
	                    value : accountingInherited
	                }, {
	                    change : {
	                        fn : this.onAccountingInheritedChange,
	                        scope : this
	                    }
	                }));
	    }
	    items.push(CWHF.createCheckboxWithHelp("admin.project.lbl.work",
	            "projectAccountingTO.workTracking", {
	                width : this.textFieldWidthCheck,
	                value : workTracking,
	                itemId: 'projectAccountingTOWorkTracking'
	            }, {
	                change : {
	                    fn : this.onWorkTrackingChange,
	                    scope : this
	                }
	            }));
	    items.push(CWHF.createNumberFieldWithHelp("common.lbl.hoursPerWorkday",
	            "projectAccountingTO.hoursPerWorkday", 2, 0, 24, {
	                width : this.textFieldWidthShort,
	                value : data['projectAccountingTO.hoursPerWorkday'],
	                itemId: 'projectAccountingTOHoursPerWorkday'
	            }));
	    items.push(CWHF.createComboWithHelp("admin.project.lbl.defaultWorkUnit",
	            "projectAccountingTO.defaultWorkUnit", {
	                itemId: "projectAccountingTODefaultWorkUnit",
	    			data : data['workUnitList'],
	                value : data["projectAccountingTO.defaultWorkUnit"]
	            }));
	    items.push(CWHF.createCheckboxWithHelp("admin.project.lbl.cost",
	            "projectAccountingTO.costTracking", {
	                width : this.textFieldWidthCheck,
	                value : costTracking,
	                itemId: 'projectAccountingTOCostTracking'
	            }, {
	                change : {
	                    fn : this.onCostTrackingChange,
	                    scope : this
	                }
	            }));
	    items.push(CWHF.createTextFieldWithHelp("admin.project.lbl.currencyName",
	            "projectAccountingTO.currencyName", {
	                width : this.textFieldWidthShort,
	                value : data['projectAccountingTO.currencyName'],
	                itemId: 'projectAccountingTOCurrencyName'
	            }));
	    items.push(CWHF.createTextFieldWithHelp("admin.project.lbl.currencySymbol",
	            "projectAccountingTO.currencySymbol", {
	                width : this.textFieldWidthShort,
	                value : data['projectAccountingTO.currencySymbol'],
	                itemId : 'projectAccountingTOCurrencySymbol'
	            }));
	    items.push(CWHF.createComboWithHelp('admin.project.lbl.defaultAccount',
	            "projectAccountingTO.defaultAccount", {
	    			itemId: "projectAccountingTODefaultAccount",
	                data : data["accountList"],
	                value : data['projectAccountingTO.defaultAccount']
	            }));
	    var accountingFieldSet = {
	        xtype : 'fieldset',
	        itemId : 'fsacc',
	        width : this.FieldSetWidth,
	        title : getText('admin.project.lbl.fsaccount'),
	        collapsible : false,
	        defaultType : 'textfield',
	        defaults : {
	            anchor : '100%'
	        },
	        layout : 'anchor',
	        items : items
	    };
	    mainTab.add(accountingFieldSet);
	    var disabled = (this.addAsSubproject || subproject) && accountingInherited;
	    this.enableAccountingFields(disabled, workTracking, costTracking);
	},

	addReleaseFieldSet : function(data, mainTab) {
	    mainTab.add({
	        xtype : 'fieldset',
	        itemId : 'fsrel',
	        width : this.FieldSetWidth,
	        title : getText('admin.project.lbl.release'),
	        collapsible : false,
	        defaults : {
	            anchor : '100%'
	        },
	        layout : 'anchor',
	        items : [ CWHF.createCheckboxWithHelp('admin.project.lbl.useRelease',
	                'projectBaseTO.useRelease', {
	                    width : this.textFieldWidthCheck,
	                    value : data['projectBaseTO.useRelease'],
	                    itemId: 'projectBaseTOUseRelease'
	                }) ]
	    });
	},

	onStatusForIssueTypeChange : function(field, newValue, oldValue, options) {
	    options.comboControl.setDisabled(!newValue);
	},
	
	/**
	 * Enable or disable the fields related to accounting (work and cost tracking)
	 */
	enableAccountingFields : function(accountingInherited, workTracking, costTracking) {
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTOWorkTracking").setDisabled(
	            accountingInherited);
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTOHoursPerWorkday").setDisabled(
	            accountingInherited || !workTracking);
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTODefaultWorkUnit").setDisabled(
	            accountingInherited || !workTracking);
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTOCostTracking").setDisabled(
	            accountingInherited);
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTOCurrencyName").setDisabled(
	            accountingInherited || !costTracking);
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTOCurrencySymbol").setDisabled(
	            accountingInherited || !costTracking);
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTODefaultAccount").setDisabled(
	            accountingInherited || !workTracking && !costTracking);
	},
	
	onAccountingInheritedChange : function(field, newValue, oldValue, options) {
	    var accountingInherited = field.getValue();
	    var workTracking = this.getWrappedControl("mainTab", "fsacc",
	            "projectAccountingTOWorkTracking").getValue();
	    var costTracking = this.getWrappedControl("mainTab", "fsacc",
	            "projectAccountingTOCostTracking").getValue();
	    this.enableAccountingFields(accountingInherited, workTracking, costTracking);
	},

	onWorkTrackingChange : function(field, newValue, oldValue, options) {
	    var workTracking = field.getValue();
	    this.enableWorkTrackingFields(workTracking);
	},
	
	/**
	 * Enable or disable the fields related to work tracking
	 */
	enableWorkTrackingFields : function(workTracking) {
	    var costTracking = this.getWrappedControl("mainTab", "fsacc",
	            "projectAccountingTOCostTracking").getValue();
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTOHoursPerWorkday").setDisabled(
	            !workTracking);
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTODefaultWorkUnit").setDisabled(
	            !workTracking);
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTODefaultAccount").setDisabled(
	            !workTracking && !costTracking);
	},

	onCostTrackingChange : function(field, newValue, oldValue, options) {
	    var costTracking = field.getValue();
	    this.enableCostTrackingFields(costTracking);
	},
	
	/**
	 * Enable or disable the fields related to cost tracking
	 */
	enableCostTrackingFields : function(costTracking) {
	    var workTracking = this.getWrappedControl("mainTab", "fsacc",
	            "projectAccountingTOWorkTracking").getValue();
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTOCurrencyName").setDisabled(
	            !costTracking);
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTOCurrencySymbol").setDisabled(
	            !costTracking);
	    this.getHelpWrapper("mainTab", "fsacc", "projectAccountingTODefaultAccount").setDisabled(
	            !costTracking && !workTracking);
	},
	
	/**
	 * Gets a control by the path according to the "arguments"
	 * starting form the main tab panel
	 */
	getControl : function() {
	    return CWHF.getControl.apply(this.tabPanel, arguments);
	},

	getHelpWrapper : function() {
	    return CWHF.getHelpWrapper.apply(this.tabPanel, arguments);
	},

	getWrappedControl : function() {
	    return CWHF.getWrappedControl.apply(this.tabPanel, arguments);
	}
});
