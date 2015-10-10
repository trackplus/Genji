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

dojo.addOnLoad(function() {
	screenConfigInit();
});

var treeController;
var treeSelector;
var detailPane;
var treeDndController;
var treeWidget;
var oldPickerDefault=null;
var oldPickerMinValue=null;
var oldPickerMaxValue=null;

String.prototype.trim=function(){
	return this.replace(/^\s*|\s*$/g,'');
};
String.prototype.ltrim=function(){
	return this.replace(/^\s*/g,'');
};

String.prototype.rtrim=function(){
	return this.replace(/\s*$/g,'');
};
/*
some initialization on page load
*/
function screenConfigInit(){
	treeController=dojo.widget.byId("treeController");
	treeWidget=dojo.widget.byId("treeCfg");
	var menu = dojo.widget.byId("treeContextMenu");
	menu.listenTree(treeWidget);
	treeWidget.menu=menu;
	treeWidget.controller=treeController;
	//tree controller register to events from tree
	treeController.listenTree.call(treeController,treeWidget);

	treeSelector=treeWidget.selector;
	treeDndController=treeController.DNDController;
	dojo.event.connect("after",treeSelector, "select", onTreeCfgSelect);
	//replace the register function used to create dnd objects
	treeDndController.registerDNDNode=myRegisterDNDNode;
	//must override with our functionality
	treeController.onTreeClick = function(message){
		 var node = message.source;
		 if (node.isExpanded){
		   		this.expand(node);
		 } else {
		   this.collapse(node);
		 }
		 if(treeSelector.selectedNode==null){
			treeSelector.doSelect(node);
			onTreeCfgSelect();
		}
	};
 	detailPane=document.getElementById("details");
	var handler = function(node) {
		this.node = node;
		this.process = function() {
			var child = node.children[0];
			treeController.expandToLevel(child,1);
		}
	};
	var h=new handler(treeWidget.children[0]);
	treeController.expand(treeWidget.children[0], false, h, h.process);
	var detailPaneWidget = dojo.widget.byId("details");
	dojo.event.connect("after",detailPaneWidget, "setContent", verifyErrorsOnTreeConfig);
}
function verifyErrorsOnTreeConfig(){
	 if(haveErrorsOnTreeConfig()){
		 showErrorIcon();
	 }else{
		 hideErrorIcon();
	 }
}
function haveErrorsOnTreeConfig(){
	var errorListEl=document.getElementById("errorList");
	if(errorListEl!=null){
		var liElemets=errorListEl.getElementsByTagName("li");
		if(liElemets!=null&&liElemets.length>0){
			return true;
		}
	}
	return false;
}
/*
This method is call when a node from tree is selected
Go asynchronus to the server and obtain propert detail for selected node.
*/
function onTreeCfgSelect(){
	resetCalendars();
	var objectId=treeSelector.selectedNode.objectId;
	var widgetId=treeSelector.selectedNode.widgetId;
	var configType=getConfigType(widgetId);
	var type=getType(widgetId);
	var urlStr=getContextPath()+"/";
	if(type=="configItem"){// leaf
		urlStr=urlStr+configType+"ConfigItemDetail";
	}else{
		urlStr=urlStr+"treeNodeConfigDetail";
	}
	var myDate=new Date();
	urlStr=urlStr+".action?nodeId="+widgetId+"&time="+myDate;
	blockDetail();
	Ext.Ajax.request({
 		url: urlStr,
 		disableCaching:true,
		success: function(result){
			document.getElementById("details").innerHTML=result.responseText;
			//var detailPaneWidget = dojo.widget.byId("details");
			//detailPaneWidget.setContent(data);
			initEditor(result.responseText);
		},
		failure: function(error){
			detailPane.innerHTML=error.statusText;
		},
		method:'POST'
	});
}

/*
This method is called when a new TreeNode is complete registred
Change the controller for drag-drop: dragOver,dragOut and drop
*/
function myRegisterDNDNode(node){
	if (!node.actionIsDisabled(node.actions.MOVE)) {
		var source = new dojo.dnd.TreeDragSource(node.labelNode, this, node.tree.widgetId, node);
		this.dragSources[node.widgetId] = source;
	}
	var target = new dojo.dnd.TreeDropTarget(node.labelNode, treeController, "*", node);

	this.dropTargets[node.widgetId] = target;
	//chage the trag-drop functions
	target.onDragOver=myDragOver;
	target.onDragOut=myDragOut;
	target.onDrop=myDrop;
}
function myDrop(evt){
	this.treeNode.labelNode.style.borderBottom = "";
	var source=evt.dragObject.treeNode;
	var target=this.treeNode;
	var sourceId=source.widgetId;
	var targetId=target.widgetId;
	var configType=getConfigType(sourceId);
	var urlStr=getContextPath()+"/"+configType+"Config!copy.action?sourceId="+
		sourceId+"&targetId="+targetId;
	changeConfig(urlStr,configType);
}
function myDragOut(evt){
	this.treeNode.labelNode.style.borderBottom = "";
}
function myDragOver(evt){
	var source=evt.dragObjects[0].dragSource.treeNode;
	var target=this.treeNode;
	if(accept(source,target)){
		this.treeNode.labelNode.style.borderBottom = this.indicatorStyle;
		return true;
	}
	return false;
}
/*
Used to validate if a node can drop to another node
*/
function accept(/*TreeNode*/ source,/*TreeNode*/ target){
	var targetWidgetId=target.widgetId;
	var index=targetWidgetId.search("_");
	if(index==-1){
	 return false;
	}
	var targetType=getType(targetWidgetId);
	if(targetType=="configItem"){
		return false;
	}
	var index2=source.widgetId.search("_");
	var sourceType=getType(source.widgetId);
	if(targetType!=sourceType){
		return false;
	}
	return true;
}
/*
Change a ConfgItem. Go asyncronust to the given urlStr and then
refres the proper nodes
*/
function changeConfig(urlStr,configType){
	blockDetail();
	var recursive=false;
	recursive=isRecursiveType(configType);

	Ext.Ajax.request({
 		url: urlStr,
 		disableCaching:true,
		success: function(result){
			if(recursive==true){
				refreshAllTree();
			}else{
				refreshTree();
			}
		},
		failure: function(error){
		},
		method:'POST'
	});
}
/*
Refresh the parent of selected node
*/
function refreshTree(){
	var selectedNode=treeSelector.selectedNode;
	if (selectedNode==null)
	{
		refreshNode(treeWidget);
		return;
	}
	var node=treeSelector.selectedNode.parent;
	if(node==treeWidget){
		//is a root treeNode
		refreshNode(selectedNode);
	}else{
		refreshNode(node);
	}
}
/*
Refreh only the selected node
remove all childrens and go asyncronus to the server
and obtain again the childrens
*/

function refreshSelectedNode(){
	var node=treeSelector.selectedNode;
	refreshNode(node);
}

/*
Refresh all nodes from tree and reexpand all expanded nodes
*/
function refreshAllTree(selectExactMatch, newWidgetId){
	try{
	var selectedNode=treeSelector.selectedNode;
	var selectedSchema="";
	if (newWidgetId!=null){
		//new field created: no such node existed in the tree previousely
		selectedSchema=getSchema(newWidgetId, selectExactMatch);
	}else{
		if (selectedNode==null){
			//no need to reselect any node
			selectedSchema="";
		}else{
			selectedSchema = getSchema(selectedNode.widgetId, selectExactMatch);
		}
	}
	//create a "copy" of treeNodes expanded  from tree
	var tns=createTreeSchema(selectExactMatch);

	for(var i=0; i<treeWidget.children.length; i++) {
		var node = treeWidget.children[i];
		var wasExpanded=node.isExpanded;
		while(node.children.length>0){
			var child = node.children[0];
			child.tree.removeNode(child);
		}
		//treeController.collapse(node);
		node.state= node.loadStates.UNCHECKED;
		if(wasExpanded==true){
			expandRecursive(node,tns.children[i],selectedSchema,selectExactMatch);
		}
	}
	}catch(ex){
		var str="";
		for (var x in ex)
		{
			str=str+"\n"+ex[x];
		}
		alert("ex="+str);
	}
}


/*
*	Return the part of a widgetId relevant for (re)selecting the node
*	For ex. after reset the last part of a widgetId changes but the node
*		should remain selected so only partial match is needed
*	After field delete the parent of the deleted node will be selected
*		consequently an exact match is needed because the Custom Field node and System Field node
*		have the same config schema field_field_null_null_null_null (only the end differ 0 and 1)
*/
function getSchema(widgetId, exactMatch)
{
	if (widgetId.indexOf("_")==widgetId.lastIndexOf("_"))
	{
		//node directly under the root, return widgetId independently of exactMatch flag
		return widgetId;
	}
	if (exactMatch==null || exactMatch == false)
	{
		//child node, no exact match needed the id is removed
		return widgetId.substring(0,widgetId.lastIndexOf("_"));
	}
	if (exactMatch==true)
	{
		//child node, exact match needed
		return widgetId;
	}
}
function TreeNodeSchema(/*String*/ id){
	this.id=id;
	this.children=[];
}
TreeNodeSchema.prototype.add=function(/*TreeNodeSchema*/ child){
	this.children[this.children.length]=child;
};
/*
Create a snap-shot of the treeNodes expanded from tree
*/
function createTreeSchema(selectExactMatch){
	var  tns=new TreeNodeSchema("root");
	for(var i=0; i<treeWidget.children.length; i++) {
		var child = treeWidget.children[i];
		var childTns=createTreeNodeSchema(child, selectExactMatch);
		tns.add(childTns);
	}
	return tns;
}

/*
Create a snap-shot of a treeNode expanded child recursive
*/
function createTreeNodeSchema(node, selectExactMatch){
	if(node.isFolder==false){
		return null;
	}
	if(node.isExpanded==false){
		return null;
	}
	var tns;
	tns=new TreeNodeSchema(getSchema(node.widgetId, selectExactMatch));
	for(var i=0; i<node.children.length; i++) {
		var child = node.children[i];
		var childTns=createTreeNodeSchema(child, selectExactMatch);
		if(childTns!=null){
			tns.add(childTns);
		}
	}
	return tns;
}
/*
Expand a node recursive
	node - the node to be expanded
	tns - the snap-shot of expanded tree
	selectedSchema- the widgetId or only the firstPart of id for next selected node
				depends on selectExactMatch
	selectExactMatch - if is true than the selectedSchema is the whole widgetId
*/
function expandRecursive(node,tns,selectedSchema,selectExactMatch){
	treeController.expand(node,true);
	for(var i=0; i<node.children.length; i++) {
		var child = node.children[i];
		var childId = node.children[i].widgetId;
		var childScheme=getSchema(childId, selectExactMatch);
		if(childScheme==selectedSchema){
			treeSelector.doSelect(child);
			onTreeCfgSelect();
		}
		for(var j=0; j<tns.children.length; j++) {
			var tnsChild=tns.children[j];
			if(childScheme==tnsChild.id){
				expandRecursive(child,tnsChild,selectedSchema,selectExactMatch);
				break;
			}
		}
	}
}
/*
Refresh a ndoe
	remove all child nodes
	and go asyncrhron to server to obtain the childrens.
	If there are a selctedNode keep the selection
*/

function refreshNode(node){
	var nodeId=node.widgetId;
	var	selectedNodeId = "";
	var isExpanded = false;
	if (treeSelector.selectedNode!=null)
	{
		selectedNodeId=treeSelector.selectedNode.widgetId;
		isExpanded=treeSelector.selectedNode.isExpanded;
	}
	while(node.children.length>0){
		var child = node.children[0];
		child.tree.removeNode(child);
	}

	treeController.collapse(node);
	node.state= node.loadStates.UNCHECKED;

	var handler = function(node, selectedNodeId,isExpanded) {
		this.node = node;
		this.selectedNodeId = selectedNodeId;
		this.isExpanded=isExpanded;
		// recursively expand opened node
		this.process = function() {
			var nodeToSelect=node;
			if(node.widgetId==selectedNodeId){
				nodeToSelect=node;
			}else{
				var scheme= selectedNodeId.substring(0,selectedNodeId.lastIndexOf("_"));
				for(var i=0; i<node.children.length; i++) {
					var childId = node.children[i].widgetId;
					var childScheme=childId.substring(0,childId.lastIndexOf("_"));
					if(scheme==childScheme){
						nodeToSelect=node.children[i];
						break;
					}
				}
			}
			/*if (treeSelector.selectedNode) {
				treeSelector.deselect();
			}*/
			if (nodeToSelect!=null){
				treeSelector.doSelect(nodeToSelect);
				onTreeCfgSelect();
			}
			if(isExpanded){
				treeController.expand(nodeToSelect,false);
			}
		};
	};
	var h = new handler(node,selectedNodeId,isExpanded);
	treeController.expand(node, true, h, h.process);
}
/*
Reset the selected node:
Go asyncronus to server for delete all configs that corespond to selected node
*/

function  resetSelectedNode(){
	var node=treeSelector.selectedNode;
	var widgetId=node.widgetId;
	var configType=getConfigType(widgetId);
	var type=getType(widgetId);
	if(type!="configItem"){
		//if is not a simple configItem
		//request a confirmation
		var r=confirm("Do you really want to reset "+node.title+"?");
	  	if (r==false){
	  		return;
		}

	}
	var urlStr=getContextPath()+"/"+configType+"Config!reset.action?nodeId="+widgetId;
	changeConfig(urlStr,configType);
}

//some utils functions:
function blockDetail(){
	var div1=document.createElement("DIV");
	document.createElement("DIV");
	div1.innerHTML="<img style='position: absolute; top: 0px;left: 60%;' src='"+getContextPath()+"/design/classic/img/loading.gif'/>";
	detailPane.appendChild(div1);
	detailPane.style.display="block";
}
function getConfigType(/*String */widgetId){
	return widgetId.substring(0,widgetId.search("_"));
}
function getType(/*String */widgetId){
	var index=widgetId.search("_");
	widgetId=widgetId.substring(index+1);
	var type=widgetId;
	index=widgetId.search("_");
	if(index>0){
		type=widgetId.substring(0,index);
	}
	return type;
}
function getContextPath(){
	var y=dojo.hostenv.getBaseScriptUri();
	if(y.search("/")==0){
		y=y.substring(1);
	}
	var ctxPath=y.substring(0,y.search("/"));
	return "/"+ctxPath;
}
function isRecursiveType(configType){
	if(configType=="screen"){
		return true;
	}
	return false;
}


//ScreenConfig functions
function changeScreen(nodeId){
	var screenValue=document.getElementById("screenID").value;
	var urlStr=getContextPath()+"/screenConfigItemDetail!update.action?screenID="+screenValue+"&nodeId="+nodeId;
	var configType=getConfigType(nodeId);
	changeConfig(urlStr,configType);
}
//end screenConfig function

/**
* Ajax request with submitting a form
* urlStr 			the url the ajax call refers to
* formName 			the name of the form to submit
* refreshNodeOnly	true: 	only refresh the selected node
*					false: 	refresh the entire tree
* add				true: 	save a new field or updating a default field config (for ex. if the name of the field changed)
*					false: 	delete an existing field
*/
function ajaxRequestWithSubmit(urlStr, formName, refreshNodeOnly, add){
	blockDetail();
	resetCalendars();
	updateRichTextEditor();
	var myDate=new Date();
	urlStr+="?time="+myDate;
	Ext.Ajax.request({
 		url: urlStr,
 		disableCaching:true,
		success: function(result){
			var data=result.responseText;
			try{
				if (data!=null && data.trim().length<50){
					//return a jsp containing a widgetId string after a database operation
					//the detailPane will not be directly actualized, instead a tree refresh
					//and a node select will be triggered in order to actualize the detailPane section
					if (refreshNodeOnly==true){
						//sucessfull save of an existing field config (which is not the default config)
						//or copy the inherited field config
						refreshTree();
					}else{
						if (add==true){
							//sucessfull save of a new field
							/*
							force the Field -> Custom Field branch to be expanded in order to
							select the newly added node after recursive refresh of the tree
							(only the expanded nodes will be refreshed and in this expanded nodes
							will be searched for the node to select which matches the  selected schema)
							*/
							var nodeField = treeWidget.children[0];
							if (nodeField.isExpanded==false)
							{
								treeController.expand(nodeField,true);
							}
							var nodeCustom = nodeField.children[1];
							if (nodeCustom!=null && nodeCustom.isExpanded==false)
							{
								treeController.expand(nodeCustom,true);
							}
							refreshAllTree(true, data.trim());
						}else{
							treeSelector.doSelect(treeSelector.selectedNode.parent);
							refreshAllTree(true);
						}
					}
				}else{
					//no tree refresh is needed, for example validation error, only detailPane refresh
					//detailPane.innerHTML=data;
					var detailPaneWidget = dojo.widget.byId("details");
					detailPaneWidget.setContent(data);
					initEditor(data);
				}
			}catch(ex){
				alert("ex="+ex);
			}
		},
		failure: function(error){
			detailPane.innerHTML="ERROR: " + error.statusText;
		},
		method:'POST',
		form: dojo.byId(formName)
	});
}
/**
* Ajax request without submitting a form.
* Typically used by selecting the Add button to return an empty form
* urlStr	the url the ajax call refers to
*/
function ajaxRequestNoSubmit(urlStr){
	/* deselect the selected node by add */
	//no node should be selected by adding of a new field
	if (treeSelector.selectedNode){
		treeSelector.deselect();
	}
	blockDetail();
	Ext.Ajax.request({
 		url: urlStr,
 		disableCaching:true,
		success: function(result){
			document.getElementById("details").innerHTML=result.responseText;
			initEditor(result.responseText);
		},
		failure: function(error){
			detailPane.innerHTML=error.statusText;
		},
		method:'POST'
	});
}

/**
 * JavaScript warning by changing the field name:
 */
function renameWarning(url, mayModifyField, confirmText) {
		//avoid the warning for new custom fields (document.fieldDetailForm.oldFieldName.value=="")
		//and for same name values
		if (document.fieldDetailForm.add!=null && document.fieldDetailForm.add.value=='false' &&
				document.fieldDetailForm.oldFieldName.value!=null &&
				document.fieldDetailForm.oldFieldName.value!="" &&
				document.fieldDetailForm.elements['fieldBean.name'].value!=document.fieldDetailForm.oldFieldName.value) {
			//field name modification: show the warning, and ask for confirmation
			answer = confirm(confirmText);
		 	if (answer==true) {
		 		if (mayModifyField) {
				 	ajaxRequestWithSubmit(url, 'fieldDetailForm', false, true);
			 	} else {
			 		ajaxRequestWithSubmit(url, 'fieldDetailForm', true);
		 		}
		 	}
	 	} else {
	 		//no field name modification
	 		if (mayModifyField) {
				 	ajaxRequestWithSubmit(url, 'fieldDetailForm', false, true);
			 	} else {
			 		ajaxRequestWithSubmit(url, 'fieldDetailForm', true);
		 		}
	 	}
}

/**
 * see textBoxDate.jsp
 */
function mouseOverTrigBtn(textField, picker, jsCalDateFormat){
		document.getElementById(picker).style.background='#1d5090';
		if(this["wasinit"+picker]==null){
			Calendar.setup({
				inputField : textField, // ID of the input field
				ifFormat : jsCalDateFormat, // the date format
				button : picker // ID of the button
			});
			this["wasinit"+picker]=true;
			this[picker]=picker;
			if (picker=="datePickerDefault") {
				oldPickerDefault = picker;
			}
			if (picker=="datePickerMinValue") {
				oldPickerMinValue = picker;
			}
			if (picker=="datePickerMaxValue") {
				oldPickerMaxValue = picker;
			}
			//oldPicker=picker;
		}
	}
/**
 * Reset the calendars after reloading of the content:
 * after selecting a tree node
 * after ajax submit, when eventually typeconversion/validation error occures and
 * the same page will be reloaded again
 */
function resetCalendars() {
	if(oldPickerDefault!=null){
		this["wasinit"+oldPickerDefault]=null;
	}
	if(oldPickerMinValue!=null){
		this["wasinit"+oldPickerMinValue]=null;
	}
	if(oldPickerMaxValue!=null){
		this["wasinit"+oldPickerMaxValue]=null;
	}
}


function dateSettingChanged(value){
	var dateRadioBox = document.getElementById(value+"2");
	var disableDateField = !dateRadioBox.checked;
	setDisableFromTo(value, disableDateField);

}

function setDisableFromTo(value, disableDateField){
	var dateField = document.getElementById("dateField"+value);
	var datePicker = document.getElementById("datePicker"+value);
	dateField.disabled = disableDateField;
	if (disableDateField) {
		dateField.style.display = 'none';
		datePicker.style.display = 'none';
		dateField.value=null;
	} else {
		dateField.style.display = 'block';
		datePicker.style.display = 'block';
	}
}

function systemDateSettingChanged(value){
	var dateRadioBox = document.getElementById(value+"2");
	var daysAfterCreateRadioBox = document.getElementById(value+"3");
	var disableDateField = !dateRadioBox.checked;
	var disableDaysAfterCreateField = !daysAfterCreateRadioBox.checked;
	setDisableSystemFromTo(value, disableDateField, disableDaysAfterCreateField);
}

function setDisableSystemFromTo(value, disableDateField, disableDaysAfterCreateField){
	var dateField = document.getElementById("dateField"+value);
	var datePicker = document.getElementById("datePicker"+value);
	var daysAfterCreate = document.getElementById("daysAfterCreate"+value);
	//dateField.disabled = disableDateField;
	if (disableDateField) {
		dateField.style.display = 'none';
		datePicker.style.display = 'none';
		dateField.value=null;
	} else {
		datePicker.style.display = 'block';
		dateField.style.display = 'block';
	}
	if (disableDaysAfterCreateField) {
		daysAfterCreate.style.display = 'none';
		daysAfterCreate.value=null;
	} else {
		daysAfterCreate.style.display = 'block';
	}
}


function userPickerSettingChanged(radioBoxBaseName) {
	var userPickerRadioBoxRole = document.getElementById(radioBoxBaseName+"1");
	var userPickerRadioBoxDepartment = document.getElementById(radioBoxBaseName+"2");
	var roles = document.getElementById("roles");
	var departments = document.getElementById("departments");
	if (userPickerRadioBoxRole.checked==true){
		roles.disabled=false;
		departments.disabled=true;
	} else {
			if (userPickerRadioBoxDepartment.checked==true) {
				roles.disabled=true;
				departments.disabled=false;
			} else {
				roles.disabled=true;
				departments.disabled=true;
			}
	}
}



