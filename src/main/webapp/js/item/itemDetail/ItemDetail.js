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


/**
 * Configuration for creating a grid in item detail
 */
com.trackplus.itemDetail.GridConfig=function(){
};
com.trackplus.itemDetail.GridConfig.prototype.id=null;
com.trackplus.itemDetail.GridConfig.prototype.tabId=null;
com.trackplus.itemDetail.GridConfig.prototype.urlStore=null;
com.trackplus.itemDetail.GridConfig.prototype.fields=null;
com.trackplus.itemDetail.GridConfig.prototype.tbar=null;
com.trackplus.itemDetail.GridConfig.prototype.grid=null;
com.trackplus.itemDetail.GridConfig.prototype.dblClickHandler=null;
com.trackplus.itemDetail.GridConfig.prototype.cellClickHandler=null;


com.trackplus.itemDetail.GridConfig.prototype.oldGroupField=null;
com.trackplus.itemDetail.GridConfig.prototype.updateCol=function(col,layout,index){
};
com.trackplus.itemDetail.GridConfig.prototype.updateColCfg=function(colCfg,layout,index){
	return 'Ext.grid.column.Column';
};




//Item detial component
com.trackplus.itemDetail.instanceID=0;
/**
 * Create the item detail tab panel
 * workItemID:int the ID of the item
 * tabsEnabled:Object contains the tabs enabled:
 * 		history,comments,attachments, worklog, watchers, links, migrations
 *
 */
com.trackplus.itemDetail.ItemDetailComponent=function(workItemID,projectID,issueTypeID,tabs,activeTab,fireEventFunction,fireEventScope,lastModified){
	this.workItemID=workItemID;
	this.projectID=projectID;
	this.issueTypeID=issueTypeID;
	com.trackplus.itemDetail.instanceID++;
	this.instanceID=com.trackplus.itemDetail.instanceID;
	this.tabs=tabs;
	this.activeTab=activeTab;
	this.fireEventFunction=fireEventFunction;
	this.fireEventScope=fireEventScope;
	this.lastModified=lastModified;
};
com.trackplus.itemDetail.ItemDetailComponent.prototype.instanceID=0;
com.trackplus.itemDetail.ItemDetailComponent.prototype.workItemID=null;
com.trackplus.itemDetail.ItemDetailComponent.prototype.projectID=null;
com.trackplus.itemDetail.ItemDetailComponent.prototype.issueTypeID=null;
com.trackplus.itemDetail.ItemDetailComponent.prototype.fireEventFunction=null;
com.trackplus.itemDetail.ItemDetailComponent.prototype.fireEventScope=null;
com.trackplus.itemDetail.ItemDetailComponent.prototype.lastModified=null;

com.trackplus.itemDetail.ItemDetailComponent.prototype.tabs=null;
com.trackplus.itemDetail.ItemDetailComponent.prototype.activeTab=1;
com.trackplus.itemDetail.ItemDetailComponent.prototype.tabPanel=null;
com.trackplus.itemDetail.ItemDetailComponent.prototype.getTabID=function(tabName){
	return "tabItemDetail."+this.instanceID+tabName;
};
com.trackplus.itemDetail.ItemDetailComponent.prototype.createComponent=function(){
	var me=this;
	var tabItems=new Array(0);
	var aTab;
	for(var i=0;i<this.tabs.length;i++){
		aTab=this.tabs[i];
		var tab=Ext.create(aTab.extClassName,{
			id:this.getTabID(aTab.id),
			workItemID:this.workItemID,
			tabNo:aTab.id,
			projectID:this.projectID,
			issueTypeID:this.issueTypeID,
			lastModified:me.lastModified,
			jsonData:aTab.jsonData
		});
		if(me.fireEventFunction!=null){
			tab.addListener('itemChange',function(){
				me.fireEventFunction.call(me.fireEventScope,'itemChange',arguments);
			},me);
			tab.addListener('clickOnLink',function(){
				me.fireEventFunction.call(me.fireEventScope,'clickOnLink',arguments);
			},me);
		}
		tabItems.push(tab);
		tab.addListener('activate',this.activateTab,this);
	};
	this.tabPanel=Ext.create('Ext.tab.Panel',{
		cls:'itemDetailTabPanel',
		margin: '0 0 0 0',
		autoHeight:true,
		plain:true,
		border:true,
		bodyBorder:false,
		deferredRender:true,
		defaults:{autoScroll: false,border:false,bodyBorder:false},
		//split: true,
		//collapsible: true,
		//title:'Item detail: History',
		tabPosition: 'top',
		items:tabItems,
		activeTab:this.activeTab
	});
	return this.tabPanel;
};
com.trackplus.itemDetail.ItemDetailComponent.prototype.activateTab=function(tab){
	var me=this;
	var tabNo=tab.tabNo;
	var idx = me.tabPanel.items.indexOf(tab);
	/*if(me.activeTab==idx){
		return;
	}*/
	me.activeTab=idx;
	tab.refresh.call(tab);
};



/**
 * Dialog configuration use to open a  modal dialog
 */
com.trackplus.itemDetail.DialogConfig=function(workItemID,projectID,issueTypeID){
	this.workItemID=workItemID;
	this.projectID=projectID;
	this.issueTypeID=issueTypeID;
}
com.trackplus.itemDetail.DialogConfig.prototype.workItemID=null;
com.trackplus.itemDetail.DialogConfig.prototype.projectID=null;
com.trackplus.itemDetail.DialogConfig.prototype.issueTypeID=null;

//title:String -title for dialog
com.trackplus.itemDetail.DialogConfig.prototype.title="";
//formPanel:Ext.form.Panel - main panel form
com.trackplus.itemDetail.DialogConfig.prototype.formPanel=null;
//w:int - width
com.trackplus.itemDetail.DialogConfig.prototype.w=400;
//h:int - height
com.trackplus.itemDetail.DialogConfig.prototype.h=300;
//layout:String - layout for dialog: 'fit','border'
com.trackplus.itemDetail.DialogConfig.prototype.layout="fit";
//autoScroll:boolean
com.trackplus.itemDetail.DialogConfig.prototype.autoScroll=false;
//iconCls:String - name of the CSS class for dialog
com.trackplus.itemDetail.DialogConfig.prototype.iconCls=null;

//okLabelt:String - label for ok button
com.trackplus.itemDetail.DialogConfig.prototype.okLabel=com.trackplus.TrackplusConfig.getText("common.btn.ok");
//resetLabelt:String - label for reset button
com.trackplus.itemDetail.DialogConfig.prototype.resetLabel=com.trackplus.TrackplusConfig.getText("common.btn.reset");
//closeLabelt:String - label for close button
com.trackplus.itemDetail.DialogConfig.prototype.closeLabel=com.trackplus.TrackplusConfig.getText("common.btn.close");

//a function to validate the form
//scope(this) must be the instance of DialogConfig class
com.trackplus.itemDetail.DialogConfig.prototype.validate=function(){
	var form=this.formPanel.getForm();
	return (form.isValid());
};
//validate and submit the form
//scope(this) must be the instance of DialogConfig class
//call like dialogCfgInstance.okHandler.call(dialogCfgInstance);
com.trackplus.itemDetail.DialogConfig.prototype.okHandler=function(){
	var me=this;
	if(!this.validate()){
		return false;
	}
	var form=this.formPanel.getForm();
	var myScope=this;
	com.trackplus.itemDetail.modalDialog.setLoading(true);
	form.submit({
		method :'POST',
		params:{workItemID:this.workItemID,projectID:me.projectID,issueTypeID:me.issueTypeID},
		scope:myScope,
		success: function(form, action) {
			com.trackplus.itemDetail.modalDialog.setLoading(false);
			com.trackplus.itemDetail.modalDialog.hide();
			com.trackplus.itemDetail.modalDialog.destroy();
			myScope.successHandler.call(myScope,action.result);
		},
		failure: function(form, action) {
			com.trackplus.itemDetail.modalDialog.setLoading(false);
			myScope.failureHandler.call(myScope,form,action);
		}
	});
};
//invoked after form is valid and submit with success
com.trackplus.itemDetail.DialogConfig.prototype.successHandler=function(){
};
com.trackplus.itemDetail.DialogConfig.prototype.failureHandler=function(form, action){
	var msg=action.result.errorMessage;
	if(msg==null||msg==''){
		msg=getText('common.err.failure.validate');
	}
	CWHF.showMsgError(msg);
};

com.trackplus.itemDetail.enterOnForm=function(e){
	var  dialogCfg=this;
	var target = e.getTarget();
	if(target['type']=='textarea'){
		return;
	}
	dialogCfg.okHandler.call(dialogCfg);
};

/**
 * Open a modal dialog for a DialogConfig
 */
com.trackplus.itemDetail.openDialog=function(dialogCfg){
	if(com.trackplus.itemDetail.modalDialog!=null){
		com.trackplus.itemDetail.modalDialog.destroy();
	}
	var width=dialogCfg.w;
	var height=dialogCfg.h;
	var minHeight=null;
	var minWidth=null;
	if(dialogCfg.minw!=null){
		minWidth=dialogCfg.minw;
	}
	if(dialogCfg.minh!=null){
		minHeight=dialogCfg.minh;
	}
	var size=borderLayout.ensureSize(width,height);
	width=size.width;
	height=size.height;
	dialogCfg.formPanel.addListener('afterRender',function(thisForm, options){
		this.keyNav = Ext.create('Ext.util.KeyNav',{
			target:this.el,
			enter:{
				fn:com.trackplus.itemDetail.enterOnForm,
				defaultEventAction:false
			},
			scope: dialogCfg
		});
	});
	com.trackplus.itemDetail.modalDialog = Ext.create('Ext.window.Window',{
		layout		: dialogCfg.layout,
		width		: width,
		height		: height,
		minHeight	: minHeight,
		minWidth	: minWidth,
		iconCls		:dialogCfg.iconCls,
		closeAction :'destroy',
		cls:'bottomButtonsDialog',
		//border:false,
		bodyBorder:true,
		margin:'0 0 0 0',
		style:{
			padding:'5px 0px 0px 0px'
		},
		bodyPadding:'0px',
		title		:dialogCfg.title,
		modal		:true,
		items		:[dialogCfg.formPanel],
		autoScroll  :dialogCfg.autoScroll,
		buttons: [
			{text: dialogCfg.okLabel,
				handler  : function(){
					dialogCfg.okHandler.call(dialogCfg);
				}
			}/*,{text :dialogCfg.resetLabel,
				handler  : function(){
					dialogCfg.formPanel.getForm().reset();
					var ckeditors=CKEDITOR.instances;
					if(ckeditors!=null){
						for(var x in ckeditors){
							var ckEditor=ckeditors[x];
							var txtArea=document.getElementById(x);
							if(txtArea!=null&& ckEditor.checkDirty()){
								ckEditor.setData(txtArea.value);
							}
						}
					}
				}
			}*/,{text : dialogCfg.closeLabel,
				handler  : function(){
					com.trackplus.itemDetail.modalDialog.destroy();
				}
			}
		]
	});
	com.trackplus.itemDetail.modalDialog.show();
};
