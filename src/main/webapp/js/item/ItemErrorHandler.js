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


com.trackplus.item.ItemErrorHandler={
	handleErrors:function(itemAction,data){
		var errorCode=data.errorCode;
		var fieldTypeRenderersMap=itemAction.itemComponent.screenFacade.controller.fieldTypeRenderersMap;
		var synopsisTxt=itemAction.itemComponent.txtTitleHeader;
		switch (errorCode){
			case 0://GENERAL
			case 1:{//CONVERSION
				var errorTxt="";
				var errors=data.errors;
				for(var i=0;i<errors.length;i++){
					var fieldID=errors[i].id;
					if(fieldID){
						var fieldTypeRenderer=fieldTypeRenderersMap["f"+fieldID];
						if(fieldTypeRenderer){
							fieldTypeRenderer.markInvalid(errors[i].label);
						}else{
							if(fieldID===17&&synopsisTxt){
								//synopsis
								synopsisTxt.markInvalid(errors[i].label);
							}
						}
					}
					errorTxt+=errors[i].label+"</BR>";
				}
				var msgError=getText(getText("item.err.saveFailed",itemAction.itemComponent.synopsis));
				if(errorTxt.length>0){
					msgError=errorTxt;
				}
				CWHF.showMsgError(msgError);
				break;
			}
			case 2:{//OUT_OF_DATE
				com.trackplus.item.ItemErrorHandler.outOfDateHandler(itemAction,data);
				break;
			}
		}
	},
	outOfDateHandler:function(itemAction,data){
		Ext.MessageBox.show({
			title:getText('item.err.wasModified'),
			msg: getText('item.err.modified'),
			buttons: Ext.MessageBox.YESNOCANCEL,
			buttonText: {yes: getText('item.err.btn.ignore'), no: getText('item.err.btn.overwrite'), cancel: getText('item.err.btn.merge')},
			fn: function(btn){
				if(btn==="yes"){
					com.trackplus.item.ItemErrorHandler.outOfDateHandler_ignore(itemAction,data);
					return;
				}
				if(btn==="no"){
					com.trackplus.item.ItemErrorHandler.outOfDateHandler_overwrite(itemAction,data);
					return;
				}
				if(btn==="cancel"){
					com.trackplus.item.ItemErrorHandler.outOfDateHandler_merge(itemAction,data);
					return;
				}
			},
			icon: Ext.MessageBox.QUESTION
		});
	},

	outOfDateHandler_ignore:function(itemAction,data){
		itemAction.lastModified=data.lastModified;
	},

	outOfDateHandler_overwrite:function(itemAction,data){
		var fieldTypeRenderersMap=itemAction.itemComponent.screenFacade.controller.fieldTypeRenderersMap;
		var workItemContext=data.workItemContext;
		var fieldConfigs=workItemContext.fieldConfigs;
		var fieldValues=workItemContext.fieldValues;
		var fieldDisplayValues=workItemContext.fieldDisplayValues;

		for(var f in fieldConfigs){
			var fieldCfg=fieldConfigs[f];
			var fieldTypeRenderer=fieldTypeRenderersMap[f];
			if(fieldTypeRenderer){
				fieldTypeRenderer.update.call(fieldTypeRenderer,{
					fieldConfig:fieldCfg,
					value:fieldValues[f],
					displayValue:fieldDisplayValues[f]
				});
			}
		}
		itemAction.lastModified=data.lastModified;
	},

	outOfDateHandler_merge:function(itemAction,data){
		var fieldTypeRenderersMap=itemAction.itemComponent.screenFacade.controller.fieldTypeRenderersMap;
		var workItemContext=data.workItemContext;
		var fieldConfigs=workItemContext.fieldConfigs;
		var fieldValues=workItemContext.fieldValues;
		var fieldDisplayValues=workItemContext.fieldDisplayValues;

		for(var f in fieldConfigs){
			var fieldCfg=fieldConfigs[f];
			var fieldTypeRenderer=fieldTypeRenderersMap[f];
			if(fieldTypeRenderer){
				var newValue=fieldValues[f];
				var myValue=fieldTypeRenderer.getValue();
				var originalValue=fieldTypeRenderer.getOriginalValue();
				if(newValue!==originalValue){
					//somebody else modify the field value
					if(myValue!==originalValue){
						//also modify by me ->conflict
						fieldTypeRenderer.markConflict(newValue);
					}else{
						fieldTypeRenderer.markModifiedByOther(newValue);
					}
				}else{
					//other people do not change the field value
					if(myValue!==originalValue){
						//I change the value
						fieldTypeRenderer.markModifiedByMe();
					}
				}
			}
		}
		itemAction.lastModified=data.lastModified;
	}
};
