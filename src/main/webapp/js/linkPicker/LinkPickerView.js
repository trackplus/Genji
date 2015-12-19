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

Ext.define('com.trackplus.linkPicker.LinkPickerView',{
	extend: 'Ext.panel.Panel',
	config:{
		linkPikerController:null,
		linkText:'',
		linkURL:'',
		useDocument:false,
		linkTarget:null
	},
	margin: '0 0 0 0',
	border: false,
	bodyBorder:false,
	baseCls:'x-plain',
	layout:'border',
	bodyStyle:{
		padding:'0px 0px 0px 0px'
	},
	txtURL:null,
	txtLinkText:null,
	txtTarget:null,
	initComponent: function(){
		var me=this;
		if(CWHF.isNull(me.selectedDatasourceType)){
			me.selectedDatasourceType=1;
		}
		me.items=me.createChildren();
		me.addMyListeners();
		me.callParent();
	},
	createChildren:function(){
		var me=this;
		var items=new Array();
		var panelNorth=Ext.create('Ext.panel.Panel',{
			border:false,
			bodyBorder:false,
			region:'north',
			html:'North info'
		});


		me.panDetails=Ext.create('Ext.panel.Panel',{
			region:'center',
			border:false,
			bodyBorder:false,
			layout:'fit'
		});
		var labelWidth=100;
		var textWidth=300;
		me.txtURL=CWHF.createTextField("linkPicker.lbl.url", "url",
			{labelWidth:labelWidth, anchor:'100%',value:me.getLinkURL()});
		me.txtLinkText=CWHF.createTextField("linkPicker.lbl.linkText", "linkText",
			{labelWidth:labelWidth, anchor:'100%',value:me.getLinkText()});
		me.txtTarget=CWHF.createTextField("linkPicker.lbl.target", "target",
			{labelWidth:labelWidth,width:250,value:me.getLinkTarget()});

		me.panLinkText=Ext.create('Ext.panel.Panel',{
			region:'south',
			border:false,
			bodyBorder:false,
			style:{
				borderTop:'1px solid #d0d0d0',
				padding:'10px'
			},
			layout:'anchor',
			/*defaults:{
				anchor:'100%'
			},*/
			items:[me.txtURL,me.txtLinkText,me.txtTarget]
		});

		me.centerPanel=Ext.create('Ext.panel.Panel',{
			region:'center',
			layout:'border',
			border:false,
			bodyBorder:false,
			items:[me.panDetails,me.panLinkText]
		});

		var linkTypes=[
			{id:'0',text:getText('linkPicker.lbl.linkType.external'),leaf:true},
			{id:'1',text:getText('linkPicker.lbl.linkType.attachment'),leaf:true},
			{id:'2',text:getText('linkPicker.lbl.linkType.workItem'),leaf:true}
		];
		if(me.getUseDocument()==true){
			linkTypes.push({id:'3',text:getText('linkPicker.lbl.linkType.docuemnt'),leaf:true});
		}

		var store = Ext.create('Ext.data.TreeStore', {
			root: {
				text: '',
				expanded: true,
				children:linkTypes
			}
		});
		me.westPanel=Ext.create('Ext.tree.Panel',{
			store: store,
			border:false,
			bodyBorder:false,
			cls:'westTreeNavigator',
			style:{
				borderRight:'1px solid #d0d0d0'
			},
			useArrows: false,
			rootVisible: false,
			region:'west',
			html:'westPanel',
			width:175
		});
		//items.push(panelNorth);
		items.push(me.westPanel);
		items.push(me.centerPanel);
		return items;
	},
	addMyListeners:function(){
		var me=this;
		me.westPanel.on('select',me.treeNodeSelect,me);
	},
	treeNodeSelect:function(view,node){
		var me=this;
		var nodeID=node.data.id;
		var linkType=parseInt(nodeID);
		me.fireEvent("linkTypeChange",linkType);
	},
	setSelectedLinkTypeNode:function(linkType){
		var me=this;
		var nodeToSelect=me.westPanel.getStore().getNodeById(linkType+"");
		var treeSelectionModel = me.westPanel.getSelectionModel();
		if (nodeToSelect != null) {
			treeSelectionModel.select(nodeToSelect);
		}
	},
	setActiveDetailPanel:function(panel){
		var me=this;
		me.panDetails.removeAll(true);
		me.panDetails.add(panel);
	}
});

