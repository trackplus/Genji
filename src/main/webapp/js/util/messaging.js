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

com.trackplus.util.errorWin=null;

/**
 * Show an error messgae
 * params:
 * title:String
 * errorMessage:String
 */
com.trackplus.util.showErrorMessage=function(title,errorMessage){
	if(com.trackplus.util.errorWin){
		com.trackplus.util.errorWin.destroy();
	}
	var panel=new Ext.Panel({
		collapsible:false,
		region:'center',
		bodyBorder:true,
		border:false,
		layout:'fit',
		html:errorMessage
	});
	com.trackplus.util.errorWin = new Ext.Window({
		layout      : 'border',
		width       : 425,
		height      : 215,
		minWidth    : 425,
		closeAction :'hide',
		plain       : true,
		title       :title,
		modal       :true,
		items       :panel,
		autoScroll  :true,
		buttons: [
			{text : closeLabel,
				handler  : function(){
					com.trackplus.util.errorWin.hide();
				}
			}
		]
	});
	com.trackplus.util.errorWin.show();
};
com.trackplus.util.win=null;
com.trackplus.util.showHtmlDetail=function(title,htmlContent){
	if(com.trackplus.util.win){
		com.trackplus.util.win.destroy();
	}
	htmlDinamicContent=htmlContent;
	var htmlFrameWrapper='<div class="containerRow1" style="width:100%;height:100%;border:medium none;"><iframe allowtransparency="true" frameborder="0" width="100%" style="height:250px;background-color: transparent;"  '+
	'src="richTextPreview.action"></iframe></div>';
	var panel=new Ext.Panel({
		collapsible:false,
		autoWidth:true,
		autoHeight:true,
		bodyBorder:false,
		border:false,
		id:"panelContent",
		html:htmlFrameWrapper
	})
	com.trackplus.util.win = new Ext.Window({
		layout      : 'fit',
		width       : 550,
		height      : 350,
		closeAction :'hide',
		plain       : true,
		title       :title,
		modal       :true,
		items       :panel,
		autoScroll  :true,
		buttons: [{text : getText("common.btn.close"),
			handler  : function(){
				com.trackplus.util.win.hide();
			}
		}]
	});
	com.trackplus.util.win.show();
};
