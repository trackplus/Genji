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
	},
	view:null,
	controller:null,
	createCenterPanel:function(){
		var me=this;
		me.controller=Ext.create('com.trackplus.logon.LogonPageController',{
			model:me.jsonData
		});
		me.view=me.controller.createView.call(me.controller);

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
					maxWidth:me.controller.WIDTH_COLLAPSED
				},
				items:[me.view]
			});
			return Ext.create('Ext.panel.Panel',{
				region: 'center',
				border: false,
				baseCls:'x-plain',
				unstyled: true,
				layout:{
					type:'border',
					maxWidth:me.controller.WIDTH_COLLAPSED
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
					maxWidth:me.controller.WIDTH_COLLAPSED
				},
				items:[me.view]
			});
		}
	},
	setFocus:function(){
		this.controller.setFocus.call(this.controller);
	}

});

Ext.define('com.trackplus.logon.LogonPageView',{
	extend: 'Ext.form.Panel',
	config:{
		model:{},
		controller:null
	},
	id: 'logonpageview', //custom
	url:'logon!login.action',
	standardSubmit : false,
	title:getText('common.btn.login'),
	//iconCls: 'custom_lock', //'login16',

	layout:'border',

	txtHiddenPassword:null,
	txtUsername:null,
	txtPassword:null,
	linkForgotPassword:null,
	linkSelfRegistration:null,
	panelMotd:null,
	linkExpandMOTD:null,
	panelMessage:null,

	toolExpanCollapse:null,



	initComponent: function(){
		var me=this;
		me.items=me.createChildren();
		me.height=me.controller.HEIGHT_COLLAPSED;
		me.width='100%';
		//me.width=me.controller.WIDTH_COLLAPSED;
		me.callParent();
		me.addListener('afterrender',function(panel) {
			var header = panel.header;
			// header.add(me.linkExpandMOTD);
		});
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

			value:Ext.util.Cookies.get('passwd'),
			blankText:com.trackplus.TrackplusConfig.getText('logon.err.password.required')
			//addChildEls:me.linkForgotPassword
		});
		me.btnLogin=Ext.create('Ext.button.Button',{
			text: getText('common.btn.login'),
			tabIndex:3,
			formBind: true, //only enabled once the form is valid
			handler: me.controller.login,
			scope:me.controller,
			minWidth:75
		});


		if(com.trackplus.TrackplusConfig.isSelfRegAllowed){
			me.linkSelfRegistration=Ext.create('Ext.ux.LinkComponent',{
				handler:me.controller.selfRegistration,
				scope:me.controller,
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
			handler:me.controller.forgetPassword,
			scope:me.controller,
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
			checked : Ext.util.Cookies.get('rememberMe')=='true'
		});


		me.lbl=Ext.create('Ext.form.Label',{
			 text: 'My Awesome Field'
		});

		me.linkExpandMOTD=Ext.create('Ext.ux.LinkComponent',{
			handler:me.controller.expandMOTD,
			scope:me.controller,
			label: getText("logon.lbl.expandMOTD"),
			clsLink:'expandMOTD',
			tabIndex:6,
			style:{
				textAlign:'left',
				whiteSpace:'nowrap',
				padding:'0px 5px 0px 10px',
				margin:'2px 0 2px 0'
			}
		});

		var panelUsername1=Ext.create('Ext.panel.Panel',{
			layout: {
				type:'vbox',
				padding:'0',
				align:'left'
			},
			id:'usernamePanel',
			height:42,
			width:310,
			border:false,
			bodyBorder:false,
			items:[me.txtUsername, me.checkRemeberMe]
		});

		var pwdItems=[];
		pwdItems.push(me.txtPassword);
		pwdItems.push(me.linkForgotPassword);
		if(me.linkSelfRegistration!=null){
//			pwdItems.push({
//				xtype: 'tbspacer',
//				cls: 'toolbarSeperator',
//				margin:0,
//				padding:0,
//				border: 'none',
//				overCls:null
//			});
			pwdItems.push(me.linkSelfRegistration);
		}

		var panelPassword1=Ext.create('Ext.panel.Panel',{
			layout: {
				type:'vbox',
				padding:'0',
				align:'left'
			},
			id:'passwordPanel',
			height:58,
			width:310,
			border:false,
			bodyBorder:false,
			items:pwdItems
		});



		var items=[];
		items.push(me.txtHiddenPassword);
		items.push(panelUsername1); //me.txtUsername);
		items.push(panelPassword1);
		//items.push(me.txtPassword);
		//items.push(me.btnLogin);


		var inputPanel={
			xtype:'panel',
			region:'center',
			layout:'hbox',
			border:false,
			bodyBorder:false,
			bodyPadding:'5 0 5 5',
			margin:'15 0 0 0',
			cls: 'loginPanel',
			items:items
		};

		var linkAreaItems=[];
//		if(me.linkSelfRegistration!=null){
			//linkAreaItems.push(me.linkSelfRegistration);
//		}
		//linkAreaItems.push(me.linkForgotPassword);

		me.linkArea=Ext.create('Ext.panel.Panel', {
			border:false,
			bodyBorder:false,
			id:"linkArea",
			autoScroll:false,
			region:'center',
			layout:'hbox',
			bodyPadding:5,
			items:linkAreaItems
		});



		me.panelMessage=Ext.create('Ext.panel.Panel', {
			border:false,
			bodyBorder:false,
			id:"panelMessage",
			html: me.model.teaserText,
			autoScroll:false,
			region:'center',
			//baseCls:'x-plain',
			//cls:'motd',
			flex:1//with will be flexed horizontally according to each item's relative flex value compared to the sum of all items with a flex value specified.
		});

		me.panelMotd=Ext.create('Ext.panel.Panel', {
			region:'center',
			cls: "motd",
			border:false,
			bodyBorder:false,
			autoScroll:true,
			html:''
		});

		var panelEast=Ext.create('Ext.panel.Panel',{
			layout:{
				type:'vbox',
				align:'left',
				padding:'19 5 5 0'
			},
			minWidth:100,
			border:false,
			bodyBorder:false,
			region:'east',
			cls:'tpspecial',
			items:[me.btnLogin]
		});
		var southItems=[];
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
		southItems.push(me.teaserText);
		southItems.push(me.linkExpandMOTD);

		var panelSouth=Ext.create('Ext.panel.Panel',{
			layout:'hbox',
			border:false,
			bodyBorder:false,
			region:'south',
			cls:'panelTeaser',
			height:42,
			bodyPadding: '10 0 0 20',
			items:southItems
		});
		var northPanel=Ext.create('Ext.panel.Panel',{
			layout:'border',
			border:false,
			bodyBorder:false,
			region:'north',
			cls: 'loginNorthPanel',
			padding: '15 0 0 0',
			items:[inputPanel,panelEast,panelSouth],
			height:me.controller.HEIGHT_COLLAPSED-35
		});
		return [northPanel,me.panelMotd];
	}
});


Ext.define('com.trackplus.logon.LogonPageController',{
	extend:'Ext.Base',
	config: {
		context: 1,
		model: {}
	},
	view:null,
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
	},
	createView:function(){
		var me=this;
		me.view=Ext.create('com.trackplus.logon.LogonPageView',{
			model:me.model,
			controller:me
		});
		me.addListeners();
		return me.view;
	},
	addListeners:function(){
		var me=this;
		me.view.txtUsername.addListener('specialkey',me.onInputKeyPressed,me);
		me.view.txtPassword.addListener('specialkey',me.onInputKeyPressed,me);
	},

	setFocus:function(){
		var me = this;
		var uf = me.view.txtUsername;
		uf.focus(true);
	},

	expandMOTD:function(){
		var me=this;
		me.expandedMOTD=!me.expandedMOTD;
		var y=me.view.ownerCt.getEl().getY();
		var height=me.view.ownerCt.body.getSize().height;
		if(me.expandedMOTD){
			if(me.view.linkSelfRegistration!=null){
				me.view.linkSelfRegistration.getEl().fadeIn({
					opacity: 1, //can be any value between 0 and 1 (e.g. .5)
					easing: 'easeOut',
					duration: 600
				});
			}
			me.view.linkForgotPassword.getEl().fadeIn({
				opacity: 1, //can be any value between 0 and 1 (e.g. .5)
				easing: 'easeOut',
				duration: 600
			});
			me.view.checkRemeberMe.getEl().fadeIn({
				opacity: 1, //can be any value between 0 and 1 (e.g. .5)
				easing: 'easeOut',
				duration: 600
			});
			/*me.view.teaserText.getEl().fadeIn({
				opacity: 1, //can be any value between 0 and 1 (e.g. .5)
				easing: 'easeOut',
				duration: 600
			});*/

			me.view.getEl().animate({
				y:y+20,
				duration:600
			});
			me.view.body.animate({
				height:height-40,
				duration:600,
				listeners:{
					'afteranimate':{
						fn:function(){
							me.view.height='100%';
							me.view.width='100%';
							me.view.panelMotd.getEl().fadeOut({
								opacity: 0.1, //can be any value between 0 and 1 (e.g. .5)
								easing: 'easeOut',
								duration: 15,
								remove: false,
								useDisplay: false,
								callback:function(){
											me.view.panelMotd.update(me.model.motd);
											me.view.panelMotd.getEl().fadeIn({
												opacity: 1, //can be any value between 0 and 1 (e.g. .5)
												easing: 'easeOut',
												duration: 300
											});
								}
							});


							me.view.linkExpandMOTD.setClsLink("collapseMOTD");
							me.view.linkExpandMOTD.setLabel(getText("logon.lbl.collapseMOTD"));
							me.view.ownerCt.doLayout();
							me.view.doLayout();
						}
					}
				}
			});
		}else{
			if(me.view.linkSelfRegistration!=null){
				me.view.linkSelfRegistration.getEl().fadeOut({
					opacity: 0,
					easing: 'easeOut',
					duration: 600,
					remove: false,
					useDisplay: false
				});
			}
			me.view.linkForgotPassword.getEl().fadeOut({
				opacity: 0,
				easing: 'easeOut',
				duration: 600,
				remove: false,
				useDisplay: false
			});
			me.view.checkRemeberMe.getEl().fadeOut({
				opacity: 0,
				easing: 'easeOut',
				duration: 600,
				remove: false,
				useDisplay: false
			});
			/*me.view.teaserText.getEl().fadeOut({
				opacity: 0,
				easing: 'easeOut',
				duration: 600,
				remove: false,
				useDisplay: false
			});*/


			var height=me.view.ownerCt.body.getSize().height;
			var y=me.view.ownerCt.getEl().getY();
			var pos=Math.round(height/2)-Math.round(me.HEIGHT_COLLAPSED/2);
			me.view.getEl().animate({
				y:y+20+pos,
				duration:600
			});
			me.view.body.animate({
				height:me.HEIGHT_COLLAPSED-40,
				duration:625,
				listeners:{
					'afteranimate':{
						fn:function(){
							me.view.height=me.HEIGHT_COLLAPSED;
							me.view.width='100%';
							//me.view.width=me.WIDTH_COLLAPSED;
							me.view.panelMotd.update('');
							me.view.linkExpandMOTD.setClsLink("expandMOTD");
							me.view.linkExpandMOTD.setLabel(getText("logon.lbl.expandMOTD"));
							me.view.ownerCt.doLayout();
							me.view.doLayout();
						}
					}
				}
			});
		}
	},
	onTxtEmailTestInputKeyPressed:function(field, e){
		var me=this;
		if (e.getKey() == e.ENTER&&me.txtEmail.isValid()) {
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
					if(action.result.errors!=null&&action.result.errors.length>0){
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
		if(me.logon_forgotWin!=null){
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
			border:false,
			autoScroll:true,
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
			layout	  : 'fit',
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
		if (e.getKey() == e.ENTER) {
			me.login.call(me);
		}
	},
	login:function(){
		var me=this;
		var form = me.view.getForm();
		if (!form.isValid()) {
			return true;
		}
		var pw = me.view.txtHiddenPassword;
		var kpw = me.view.txtPassword.getValue();

		var usernameValue=me.view.txtUsername.getValue();
		var expiry = new Date(new Date().getTime() + 30 * 24 * 60 * 60 * 1000);
		var rememberMe=me.view.checkRemeberMe.getValue();
		if(rememberMe==true){
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
								document.location=jsonURL;
				},
				failure: function(form, action) {
					if(me.model.nonce!=action.result.data.nonce){
						if(action.result.data.nonce==null) {
							location.reload(true);
							return true;
						}else {
							me.model.nonce = action.result.data.nonce;
							me.login.call(me);
							return true;
						}
					}
					borderLayout.setLoading(false);
					if (action.failureType!='client') {
						me.handleErrors(action.result.data);
					}
				}
			}
		);
	},
	decimalToHex:function(d, padding) {
		var hex = Number(d).toString(16);
		padding = typeof (padding) === "undefined" || padding === null ? padding = 2 : padding;
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
		if(errors!=null&&errors.length>0){
			for(var i=0;i<errors.length;i++){
				if(errors[i].label=='errCredentials') {
					var un = me.view.txtUsername;
					var pn = me.view.txtPassword;
					un.markInvalid(errors[i].id);
					pn.markInvalid(errors[i].id);
					data.continyou=false;
				}
				if(errors[i].label=='errGeneralError') {
					strErr+=errors[i].id+"\n";
					data.continyou=false;
				}
				if(errors[i].label=='errLicenseError') {
					strErr+=errors[i].id+"\n";
				}
			}
			if (strErr != null && strErr.length > 0) {
				Ext.MessageBox.show({
					title: '',
					msg: strErr,
					modal: true,
					fn: function(btn){
						if (data.continyou==true) {
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
		var x=me.view.getEl().getX();
		var dif=15;
		me.view.getEl().animate({
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
