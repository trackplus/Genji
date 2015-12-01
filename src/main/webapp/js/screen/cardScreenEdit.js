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

Ext.define('com.trackplus.layout.CardScreenEditLayout',{
	extend:'com.trackplus.layout.BaseLayout',
	useToolbar:true,
	screenModel:null,
	screenEditFacade:null,
	constructor : function(config) {
		var me = this;
		me.callParent(arguments);
		var data=me.initData;
		var backAction;
		if(data.backAction){
			backAction=data.backAction;
		}else{
			backAction='admin.action?selectedNodeID=customForms&sectionSelected=customizationSection';
		}
		me.screenModel=com.trackplus.screen.createScreenModel(me.initData.screen);
		me.screenEditFacade= Ext.create('com.trackplus.screen.ScreenEditFacade',{
			screenModel:me.screenModel,
			showOneTab:true,
			refreshTabUrl:'tab!reload.action',
			controllerCls:'com.trackplus.screen.ScreenEditController',
			urlFieldList:'screenTree!listExtraFields.action',
			fieldsAsTree:true,
			titleFieldList:getText("admin.customize.form.edit.lbl.fields"),
			useConfig:false,
			panelViewCls:'com.trackplus.screen.ItemEditPanelView',

			screenAction:null,
			screenUpdateAction:null,
			tabAction:null,
			tabUpdateAction:null,
			panelAction:'cardPanel',
			panelUpdateAction:'updateCardPanelProperty',
			fieldAction:'cardField',
			fieldUpdateAction:'updateCardFieldProperty',

			messageDeletePanel:getText('admin.customize.form.edit.question.deletePanel'),
			messageDeleteTab:getText('admin.customize.form.edit.question.deleteTab'),
			messageDeleteField:getText('admin.customize.form.edit.question.deleteField'),
			messageCantDeleteScreen:getText('admin.customize.form.edit.message.cantDeleteScreen'),
			messageCantDeleteLastTab:getText('admin.customize.form.edit.message.cantDeleteLastTab'),
			backAction:backAction
		});
		me.onReady(function(){
			var toolbar=me.screenEditFacade.getToolbar.call(me.screenEditFacade);
			me.borderLayoutController.setActiveToolbarActionList(toolbar);
		});
	},
	createCenterPanel:function(){
		var me=this;
		return me.screenEditFacade.createScreenEditViewComponent.call(me.screenEditFacade);
	}
});
