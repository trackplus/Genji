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

Ext.define('com.trackplus.ACField', {
    extend: 'Ext.form.field.Text',

    initComponent: function() {
        Ext.each(this.fieldSubTpl, function(oneTpl, idx, allItems) {
            if (Ext.isString(oneTpl)) {
                allItems[idx] = oneTpl.replace('autocomplete="off"', 'autocomplete="on"');
            }
        });
        this.callParent(arguments);
    }
});

Ext.define('com.trackplus.logon.LogonPage',{
	extend:'Ext.Base',
	config: {
		context: 1,
		jsonData: null
	},
	constructor: function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		this.initConfig(config);
	},
	logonView:null,
	logonController:null,
	createCenterPanel:function(){
		var me=this;
		me.logonController=Ext.create('com.trackplus.logon.LogonPageController',{
			model:me.jsonData
		});
		me.logonView=me.logonController.createView.call(me.logonController);
		if(isDemo){
			me.infoMessage=Ext.create('Ext.Component',{
				html: initData.teaserText,
				border:true,
				cls:'infoBoxDemoSite',
				region:'north'
			});

			var cmpLogin=Ext.create('Ext.panel.Panel',{
				region: 'center',
				cls:'logonPageWrapper',
				border: false,
				baseCls:'x-plain',
				unstyled: true,
				layout:{
					type:'ux.center',
					maxWidth:me.logonController.WIDTH_COLLAPSED
				},
				items:[me.logonView]
			});
			return Ext.create('Ext.panel.Panel',{
				region: 'center',
				border: false,
				baseCls:'x-plain',
				unstyled: true,
				layout:{
					type:'border',
					maxWidth:me.logonController.WIDTH_COLLAPSED
				},
				items:[me.infoMessage,cmpLogin]
			});
		}else{
			return Ext.create('Ext.panel.Panel',{
				region: 'center',
				cls:'logonPageWrapper',
				border: false,
				baseCls:'x-plain',
				unstyled: true,
				layout:{
					type:'ux.center',
					maxWidth:me.logonController.WIDTH_COLLAPSED
				},
				items:[me.logonView]
			});
		}
	},
	setFocus:function(){
		this.logonController.setFocus.call(this.logonController);
	}

});

Ext.define('com.trackplus.logon.LogonPageView',{
	extend: 'Ext.form.Panel',
	config:{
		model:{},
		logonController:null
	},
	id: 'logonpageview', //custom
	url:'logon!login.action',
	standardSubmit : false,
	title:getText('common.btn.login'),
	/*header:{
		height:38
	},*/
	//iconCls: 'custom_lock', //'login16',
	layout:'border',
	txtHiddenPassword:null,
	txtUsername:null,
	txtPassword:null,
	linkForgotPassword:null,
	linkSelfRegistration:null,
	cmpMotd:null,
	linkExpandMOTD:null,
	toolExpanCollapse:null,

	initComponent: function(){
		var me=this;
		me.items=me.createChildren();
		me.height=me.logonController.HEIGHT_COLLAPSED;
		me.width='100%';
		//me.width=me.logonController.WIDTH_COLLAPSED;
		me.callParent();
	},
	createChildren:function(){
		var me=this;
		me.txtHiddenPassword=Ext.create('com.trackplus.ACField',{
			hidden:true,
			name:'j_password',
			inputId: 'password'
		});
		me.txtUsername=Ext.create('com.trackplus.ACField',{
			cls:'loginInputField',
			fieldLabel:getText('logon.lbl.username'),
			labelAlign :'right',
			itemId :'j_username',
			name:'j_username',
			inputId: 'username',
			allowBlank :false,
			validateOnBlur :false,
			validateOnChange:true,
			tabIndex:1,
			width:310,
			value:Ext.util.Cookies.get('username'),//  me.model.j_username,
			blankText:getText('logon.err.username.required')
		});


		me.txtPassword=Ext.create('Ext.form.field.Text',{
			cls:'loginInputField',
			fieldLabel: getText('logon.lbl.password'),
			labelAlign :'right',
			inputType: 'password',
			name:'k_password',
			itemId:'k_password',
			allowBlank :false,
			validateOnBlur :false,
			validateOnChange:true,
			submitValue: false,
			tabIndex:2,
			width:310,
			value:Ext.util.Cookies.get('passwd'),
			blankText:com.trackplus.TrackplusConfig.getText('logon.err.password.required')
			//addChildEls:me.linkForgotPassword
		});
		me.btnLogin=Ext.create('Ext.button.Button',{
			text: getText('common.btn.login'),
			tabIndex:3,
			formBind: true, //only enabled once the form is valid
			handler: me.logonController.login,
			scope:me.logonController,
			minWidth:75
		});


		if(com.trackplus.TrackplusConfig.isSelfRegAllowed){
			me.linkSelfRegistration=Ext.create('Ext.ux.LinkComponent',{
				handler:me.logonController.selfRegistration,
				scope:me.logonController,
				id: 'registerLink',
				clsLink:'registerLink',
				tabIndex:10,
				//hidden:true,
				hideMode:'offsets',
				style:{
					opacity:0
				},
				width: 310,
				label:getText("logon.lbl.register")
			});
		}

		me.linkForgotPassword=Ext.create('Ext.ux.LinkComponent',{
			handler:me.logonController.forgetPassword,
			scope:me.logonController,
			id: 'pw_forgot',
			clsLink:'forgotPwd',
			tabIndex:4,
			style:{
				opacity:0
			},
			hideMode:'offsets',
			width: 310,
			label:getText("logon.lbl.passwordForgotten")
		});

		me.checkRemeberMe=Ext.create('Ext.form.field.Checkbox',{
			boxLabel  : getText('logon.lbl.remember'),
			fieldLabel: '&nbsp;',
			labelSeparator:'',
			labelAlign :'right',
			cls:'rememberMe',
			name: 'rememberME',
			style:{
				opacity:0
			},
			//hidden:true,
			hideMode:'offsets',
			width: 310,
			tabIndex:5,
			inputValue : true,
			checked : Ext.util.Cookies.get('rememberMe')==='true'
		});

		var panelUsername=Ext.create('Ext.container.Container',{
			layout: {
				type:'vbox',
				padding:'0',
				align:'left'
			},
			id:'usernamePanel',
			//height:42,
			width:310,
			border:false,
			items:[me.txtUsername, me.checkRemeberMe]
		});

		var pwdItems=[];
		pwdItems.push(me.txtPassword);
		pwdItems.push(me.linkForgotPassword);
		if(me.linkSelfRegistration){
			pwdItems.push(me.linkSelfRegistration);
		}

		var panelPassword=Ext.create('Ext.container.Container',{
			layout: {
				type:'vbox',
				padding:'0',
				align:'left'
			},
			id:'passwordPanel',
			//height:58,
			width:310,
			border:false,
			bodyBorder:false,
			items:pwdItems
		});



		var items=[];
		items.push(panelUsername);
		items.push(panelPassword);
		items.push(me.txtHiddenPassword);

		var inputPanel=Ext.create('Ext.container.Container',{
			region:'center',
			layout:'hbox',
			border:false,
			padding:'6 0 5 5',
			margin:'15 0 0 0',
			cls: 'loginPanel',
			items:items
		});

		var panelEast=Ext.create('Ext.container.Container',{
			layout:{
				type:'vbox',
				align:'left',
				padding:'19 25 5 0'
			},
			border:false,
			region:'east',
			cls:'tpspecial',
			items:[me.btnLogin]
		});


		var panelTeaser=me.createTeaserPanel();

		var northPanel=Ext.create('Ext.container.Container',{
			layout:'border',
			border:false,
			region:'north',
			cls: 'loginNorthPanel',
			//padding: '15 0 0 0',
			items:[inputPanel,panelEast,panelTeaser],
			height:me.logonController.HEIGHT_COLLAPSED-35
		});

		me.cmpMotd=Ext.create('Ext.Component', {
			region:'center',
			cls: "motd",
			border:false,
			bodyBorder:false,
			autoScroll:true,
			html:''
		});

		return [northPanel,me.cmpMotd];
	},
	createTeaserPanel:function(){
		var me=this;
		var items=[];
		me.linkExpandMOTD=Ext.create('Ext.ux.LinkComponent',{
			handler:me.logonController.expandMOTD,
			scope:me.logonController,
			label: getText("logon.lbl.expandMOTD"),
			clsLink:'expandMOTD',
			tabIndex:6,
			style:{
				textAlign:'left',
				whiteSpace:'nowrap',
				padding:'0px 5px 0px 10px',
				margin:'2px 5px 2px 0'
			}
		});
		me.teaserText=Ext.create('Ext.Component',{
			html:me.model.teaserText,
			cls: 'versionInfoLabel',
			margin:'0 25 0 0',
			style:{
				//opacity:0,
				overflow:'hidden',
				whiteSpace:'nowrap'
			},
			flex:1
		});
		items.push(me.teaserText);
		items.push(me.linkExpandMOTD);

		var panelTeaser=Ext.create('Ext.container.Container',{
			layout:'hbox',
			region:'south',
			cls:'panelTeaser',
			height:42,
			padding: '10 0 0 20',
			items:items
		});
		return panelTeaser;
	}
});


Ext.define('com.trackplus.logon.LogonPageController',{
	extend:'Ext.Base',
	config: {
		context: 1,
		model: {}
	},
	logonView:null,
	expandedMOTD:false,
	HEIGHT_EXPANDED:600,
	HEIGHT_COLLAPSED:175,
	WIDTH_COLLAPSED:750,

	profile:null,
	windowConfigProfile:null,

	constructor: function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		this.initConfig(config);
	},
	createView:function(){
		var me=this;
		me.logonView=Ext.create('com.trackplus.logon.LogonPageView',{
			model:me.model,
			logonController:me
		});
		me.addListeners();
		return me.logonView;
	},
	addListeners:function(){
		var me=this;
		me.logonView.txtUsername.addListener('specialkey',me.onInputKeyPressed,me);
		me.logonView.txtPassword.addListener('specialkey',me.onInputKeyPressed,me);
	},

	setFocus:function(){
		var me = this;
		var uf = me.logonView.txtUsername;
		uf.focus(true);
	},

	expandMOTD:function(){
		var me=this;
		me.expandedMOTD=!me.expandedMOTD;
		var y=me.logonView.ownerCt.getEl().getY();
		var height=me.logonView.ownerCt.body.getSize().height;
		if(me.expandedMOTD){
			if(me.logonView.linkSelfRegistration){
				me.logonView.linkSelfRegistration.getEl().fadeIn({
					opacity: 1, //can be any value between 0 and 1 (e.g. .5)
					easing: 'easeOut',
					duration: 600
				});
			}
			me.logonView.linkForgotPassword.getEl().fadeIn({
				opacity: 1, //can be any value between 0 and 1 (e.g. .5)
				easing: 'easeOut',
				duration: 600
			});
			me.logonView.checkRemeberMe.getEl().fadeIn({
				opacity: 1, //can be any value between 0 and 1 (e.g. .5)
				easing: 'easeOut',
				duration: 600
			});
			/*me.logonView.teaserText.getEl().fadeIn({
				opacity: 1, //can be any value between 0 and 1 (e.g. .5)
				easing: 'easeOut',
				duration: 600
			});*/

			me.logonView.getEl().animate({
				y:y+20,
				duration:600
			});
			me.logonView.body.animate({
				height:height-40,
				duration:600,
				listeners:{
					'afteranimate':{
						fn:function(){
							me.logonView.height='100%';
							me.logonView.width='100%';
							me.logonView.cmpMotd.getEl().fadeOut({
								opacity: 0.1, //can be any value between 0 and 1 (e.g. .5)
								easing: 'easeOut',
								duration: 15,
								remove: false,
								useDisplay: false,
								callback:function(){
											me.logonView.cmpMotd.update(me.model.motd);
											me.logonView.cmpMotd.getEl().fadeIn({
												opacity: 1, //can be any value between 0 and 1 (e.g. .5)
												easing: 'easeOut',
												duration: 300
											});
								}
							});


							me.logonView.linkExpandMOTD.setClsLink("collapseMOTD");
							me.logonView.linkExpandMOTD.setLabel(getText("logon.lbl.collapseMOTD"));
							me.logonView.ownerCt.updateLayout ();
							me.logonView.updateLayout ();
						}
					}
				}
			});
		}else{
			if(me.logonView.linkSelfRegistration){
				me.logonView.linkSelfRegistration.getEl().fadeOut({
					opacity: 0,
					easing: 'easeOut',
					duration: 600,
					remove: false,
					useDisplay: false
				});
			}
			me.logonView.linkForgotPassword.getEl().fadeOut({
				opacity: 0,
				easing: 'easeOut',
				duration: 600,
				remove: false,
				useDisplay: false
			});
			me.logonView.checkRemeberMe.getEl().fadeOut({
				opacity: 0,
				easing: 'easeOut',
				duration: 600,
				remove: false,
				useDisplay: false
			});
			/*me.logonView.teaserText.getEl().fadeOut({
				opacity: 0,
				easing: 'easeOut',
				duration: 600,
				remove: false,
				useDisplay: false
			});*/


			var height=me.logonView.ownerCt.body.getSize().height;
			var y=me.logonView.ownerCt.getEl().getY();
			var pos=Math.round(height/2)-Math.round(me.HEIGHT_COLLAPSED/2);
			me.logonView.getEl().animate({
				y:y+20+pos,
				duration:600
			});
			me.logonView.body.animate({
				height:me.HEIGHT_COLLAPSED-40,
				duration:625,
				listeners:{
					'afteranimate':{
						fn:function(){
							me.logonView.height=me.HEIGHT_COLLAPSED;
							me.logonView.width='100%';
							//me.logonView.width=me.WIDTH_COLLAPSED;
							me.logonView.cmpMotd.update('');
							me.logonView.linkExpandMOTD.setClsLink("expandMOTD");
							me.logonView.linkExpandMOTD.setLabel(getText("logon.lbl.expandMOTD"));
							me.logonView.ownerCt.updateLayout ();
							me.logonView.updateLayout ();
						}
					}
				}
			});
		}
	},
	onTxtEmailTestInputKeyPressed:function(field, e){
		var me=this;
		if (e.getKey() === e.ENTER&&me.txtEmail.isValid()) {
			me.doForgetPassword();
		}
	},
	doForgetPassword:function(){
		var me=this;
		if (me.forgotForm.getForm().isValid()) {
			me.logon_forgotWin.setLoading(true);
			me.forgotForm.getForm().submit({
				success: function(form, action) {
					me.logon_forgotWin.setLoading(false);
					me.logon_forgotWin.hide();
					//var emailSent=action.result.data.emailSent;
					CWHF.showMsgInfo(getText('logon.newpassword.welcome'));
					me.setFocus();
				},
				failure: function(form, action) {
					me.logon_forgotWin.setLoading(false);
					var msg="";
					if(action.result.errors&&action.result.errors.length>0){
						var errs=action.result.errors;
						for(var i=0;i<errs.length;i++){
							msg=msg+errs[i].message;
						}
					}
					CWHF.showMsgError(msg);
				}
			});
		}
	},
	selfRegistration:function(){
		var me=this;
		var result=com.trackplus.admin.user.createSelfRegistrationDialog();
		me.profile=result.profile;
		me.windowConfigProfile=result.windowConfig;
	},
	forgetPassword:function(){
		var me = this;
		if(me.logon_forgotWin){
			me.logon_forgotWin.destroy();
		}
		me.txtEmail=Ext.create('Ext.form.field.Text',{
			anchor:'100%',
			vtype: 'email',
			name:'email',
			allowBlank: false
		});
		me.txtEmail.addListener('specialkey',me.onTxtEmailTestInputKeyPressed,me);
		me.forgotForm=Ext.create('Ext.form.Panel', {
			border: false,
			autoScroll:false,
			margin: '0 0 0 0',
			bodyStyle:{
				padding:'10px'
			},
			layout:'anchor',
			url: 'resetPassword.action',
			items:[
				{
					xtype:'displayfield',
					value:getText('logon.newpassword.prompt.text'),
					itemId: 'forgotLabel',
					height: '32px'
				},me.txtEmail
			]
		});

		me.logon_forgotWin = Ext.create('Ext.window.Window',{
			width	   : 400,
			height	  : 155,
			closeAction :'hide',
			plain	   : true,
			title		:com.trackplus.TrackplusConfig.getText('logon.newpassword.title'),
			modal	   :true,
			items	   :[me.forgotForm],
			autoScroll  :true,
			margin:'0 0 0 0',
			style:{
				padding:' 5px 0px 0px 0px'
			},
			bodyPadding:'0px',
			cls:'forgotPassword bottomButtonsDialog tpspecial',
			buttons: [
				{
					text: com.trackplus.TrackplusConfig.getText('common.btn.ok'),
					itemID:'btnOkForgetPassword',
					handler  :me.doForgetPassword,
					scope:me
				},{
					text : getText('common.btn.cancel'),
					itemID:'btnCancelForgetPassword',
					handler  : function(){
						me.logon_forgotWin.hide();
						me.setFocus();
					}
				}
			],
			defaultFocus:me.txtEmail
		});
		me.logon_forgotWin.show(me.linkForgotPassword);
	},
	onInputKeyPressed:function(field, e){
		var me=this;
		if (e.getKey() === e.ENTER) {
			me.login.call(me);
		}
	},
	login:function(){
		var me=this;
		var form = me.logonView.getForm();
		if (!form.isValid()) {
			return true;
		}
		var pw = me.logonView.txtHiddenPassword;
		var kpw = me.logonView.txtPassword.getValue();

		var usernameValue=me.logonView.txtUsername.getValue();
		var expiry = new Date(new Date().getTime() + 30 * 24 * 60 * 60 * 1000);
		var rememberMe=me.logonView.checkRemeberMe.getValue();
		if(rememberMe===true){
			Ext.util.Cookies.set('username', usernameValue,expiry);
			Ext.util.Cookies.set('passwd', kpw,expiry);
			Ext.util.Cookies.set('rememberMe','true',expiry);
		}else{
			Ext.util.Cookies.clear('username');
			Ext.util.Cookies.clear('passwd');
			Ext.util.Cookies.clear('rememberMe');
		}


		var the_res = '';
		var key = me.model.nonce.charCodeAt(0);
		var maybeMobile = me.model.mayBeMobile;
		// Some very simple, easy to break password encryption. There is no
		// way this can be done any better, though. Protect your passwords
		// using an SSL connection between server and client; anything else
		// just does not work, no matter what you try.
		for(var i=0;i<kpw.length;++i){
			var ccode = kpw.charCodeAt(i);
			ccode = key ^ ccode;
			the_res+=me.decimalToHex(ccode,4);
		}
		pw.setValue(the_res);
		borderLayout.setLoading(true);
		form.submit({
				url:'logon!login.action',
				success: function(form, action) {
					var jsonURL=action.result.data.jsonURL;
					var ftever=action.result.data.ftever;
					var licURL=action.result.data.licURL;
								borderLayout.setLoading(true);
								document.location=jsonURL;
				},
				failure: function(form, action) {
					if(me.model.nonce!==action.result.data.nonce){
						if(CWHF.isNull(action.result.data.nonce)) {
							location.reload(true);
							return true;
						}else {
							me.model.nonce = action.result.data.nonce;
							me.login.call(me);
							return true;
						}
					}
					borderLayout.setLoading(false);
					if (action.failureType!=='client') {
						me.handleErrors(action.result.data);
					}
				}
			}
		);
	},

	decimalToHex:function(d, padding) {
		var hex = Number(d).toString(16);
		padding =  CWHF.isNull(padding) ? padding = 2 : padding;
		while (hex.length < padding) {
			hex = "0" + hex;
		}
		return hex;
	},

	handleErrors:function(data){
		var me = this;
		var errors =  data.errors;
		var strErr="";
		// Check first if banned
		if (data.banned) {
			document.location=data.jsonURL;
		}
		if(errors&&errors.length>0){
			for(var i=0;i<errors.length;i++){
				if(errors[i].label==='errCredentials') {
					var un = me.logonView.txtUsername;
					var pn = me.logonView.txtPassword;
					un.markInvalid(errors[i].id);
					pn.markInvalid(errors[i].id);
					data.continyou=false;
				}
				if(errors[i].label==='errGeneralError') {
					strErr+=errors[i].id+"\n";
					data.continyou=false;
				}
				if(errors[i].label==='errLicenseError') {
					strErr+=errors[i].id+"\n";
				}
			}
			if (strErr  && strErr.length > 0) {
				Ext.MessageBox.show({
					title: '',
					msg: strErr,
					modal: true,
					fn: function(btn){
						if (data.continyou===true) {
							document.location=data.jsonURL;
						}
					},
					buttons: Ext.MessageBox.OK,
					icon: Ext.MessageBox.ERROR
				});
			}else{
				me.shakeWin();
			}
		}
	},
	shakeWin:function(){
		var me=this;
		var x=me.logonView.getEl().getX();
		var dif=15;
		me.logonView.getEl().animate({
			keyframes:{
				'0%': {x:x-dif},
				'25%':{x:x+dif},
				'50%':{x:x-dif},
				'75%':{x:x+dif},
				'100%':{x:x}
			},
			duration:250
		});
	}
});

Ext.define('com.trackplus.layout.LogonLayout',{
	extend:'com.trackplus.layout.BaseLayout',
	useToolbar:false,
	logonPage:null,
	constructor : function(config) {
		var me = this;
		me.callParent(arguments);
		me.logonPage = Ext.create('com.trackplus.logon.LogonPage',{
			jsonData:me.initData
		});
		me.borderLayoutController.setHelpContext("logon");
		me.onReady(function(){
			me.logonPage.setFocus.call(me.logonPage);
			Ext.DomHelper.append('logonpageview', {tag: 'div', cls: '', id: 'logonBoxShadow',html: "&nbsp;"});
			}
		);
	},
	createCenterPanel:function(){
		var me=this;
		return me.logonPage.createCenterPanel.call(me.logonPage);
	}


});
