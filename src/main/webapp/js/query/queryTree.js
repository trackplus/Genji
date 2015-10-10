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

//init query tree
var treeController;
var treeSelector;
var detailPane;
var treeDndController;
var treeWidget;
var menu;
var mitUp,mitDown,mitRemove;
var oldPicker=null;


/* setup menu actrions */
dojo.addOnLoad(function() {
    dojo.event.topic.subscribe('treeContextMenuRemove/engage',
            function (menuItem) {
                remove();
            }
    );
    dojo.event.topic.subscribe('treeContextMenuUp/engage',
            function (menuItem) {
                move('up');
            }
    );
    dojo.event.topic.subscribe('treeContextMenuDown/engage',
            function (menuItem) {
                move('down');
            }
    );
    dojo.event.topic.subscribe('treeContextMenuNegate/engage',
            function (menuItem) {
                negate();
            }
    );

});
function queryTreeInt(){
    treeController=dojo.widget.byId("treeController");
    treeWidget=dojo.widget.byId("aTree");
    menu = dojo.widget.byId("treeContextMenu");
    mitUp= dojo.widget.byId("treeContextMenuUp");
    mitDown= dojo.widget.byId("treeContextMenuDown");
    mitRemove= dojo.widget.byId("treeContextMenuRemove");
    menu.listenTree(treeWidget);
    treeWidget.menu=menu;
    treeWidget.controller=treeController;
	//tree controller register to events from tree
	treeController.listenTree.call(treeController,treeWidget);
    treeSelector=treeWidget.selector;
    treeDndController=treeController.DNDController;
    dojo.event.connect("after",treeSelector, "select", onQueryTreeSelect);
    dojo.event.connect("after",treeDndController, "onMoveTo", onMoveTo);

    if(treeWidget.children!=null&&treeWidget.children.length>0){
        //expand first child
        treeController.expand(treeWidget.children[0]);
        //select first node
        treeSelector.doSelect(treeWidget.children[0]);
        onQueryTreeSelect();
    }

    menu.aboutToShow=menuAboutToShow;
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
			onQueryTreeSelect();
		}
    };
}
dojo.addOnLoad(queryTreeInt);

function getContextPath(){
    var y=dojo.hostenv.getBaseScriptUri();
    if(y.search("/")==0){
        y=y.substring(1);
    }
    var ctxPath=y.substring(0,y.search("/"));
    return "/"+ctxPath;
}
//-----------------------
//end init part
//-------------

//---------------
//actions

/* process up or down operation */
function move(direction) {
    var selectedNode=treeSelector.selectedNode;
    if (!selectedNode) {
       return false;
    }
    if(selectedNode==treeWidget.children[0]){
        //root node;
        return;
    }
    var nodeId=selectedNode.widgetId;
    var urlStr="queryTreeAction!moveUp.action?nodeID="+nodeId;
    if(direction=="down"){
        urlStr="queryTreeAction!moveDown.action?nodeID="+nodeId;
    }
    dojo.io.bind({
        url: addTimeToAjaxUrl(urlStr),
		encoding: "utf-8",
        load: function(type, data, evt){
            refreshNode(selectedNode.parent);
        },
        error: function(type, error){
         },
        mimetype: "text/plain"
    });
}

/* process create operation */
function create(type) {
    var selectedNode=treeSelector.selectedNode;
    var nodeToRefresh=selectedNode;
    if (!selectedNode) {
        return false;
    }
    if(selectedNode.objectId==2){//expression
        //alert("Can't add node to an expression node!")
        return false
    }
    if(selectedNode.isFolder==false){
        selectedNode.isFolder=true;
        nodeToRefresh=selectedNode.parent;
        selectedNode.isExpanded=true;
    }
    var parentId=selectedNode.widgetId;
    var urlStr="queryTreeAction!addNode.action?nodeID="+parentId+"&type="+type;
    //to force expand of the node
    nodeToRefresh.isExpanded=true;

    dojo.io.bind({
        url: addTimeToAjaxUrl(urlStr),
		encoding: "utf-8",
        load: function(type, data, evt){
             refreshNode(nodeToRefresh);
        },
        error: function(type, error){
             alert("error:"+error.data);
         },
        mimetype: "text/plain"
    });
}

/*remove a node*/
function remove() {
    var selectedNode=treeSelector.selectedNode;
    var parent=selectedNode.parent;
    if(parent==treeWidget){
        //alert("Can't remove root!");
        return;
    }
    if (!selectedNode) {
        //alert('No node selected');
        return;
    }
    var nodeId=selectedNode.widgetId;
    var i=0;
    var nodes=parent.children;
    var nodeToSelect;
    for(i=0;i<nodes.length;i++){
       if(nodes[i].widgetId==nodeId){
           if(i==nodes.length-1){
               if(i==0){
                   nodeToSelect=parent.widgetId;
               }else{
                   nodeToSelect=nodes[i-1].widgetId;
               }
           }else{
               nodeToSelect=nodes[i+1].widgetId;
           }
           break;
       }
    }
    var urlStr="queryTreeAction!removeNode.action?nodeID="+nodeId;
    dojo.io.bind({
        url: addTimeToAjaxUrl(urlStr),
		encoding: "utf-8",
        load: function(type, data, evt){
            refreshNode(selectedNode.parent,nodeToSelect);
        },
        error: function(type, error){

         },
        mimetype: "text/plain"
    });
}
/*negate a node*/
function negate() {
    var selectedNode=treeSelector.selectedNode;
    if (!selectedNode) {
        //alert('No node selected');
        return false;
    }
    var nodeId=selectedNode.widgetId;
    if(nodeId==treeWidget.children[0].widgetId){
        updateRootNode("queryTreeAction!updateRootNode.action?negateRootNode=true");
        return;
    }
    var urlStr="queryTreeAction!negateNode.action?nodeID="+nodeId;
    dojo.io.bind({
        url: addTimeToAjaxUrl(urlStr),
		encoding: "utf-8",
        load: function(type, data, evt){
            if(selectedNode.parent==treeWidget){
                refreshNode(selectedNode);
            }else{
                refreshNode(selectedNode.parent);
            }
        },
        error: function(type, error){

         },
        mimetype: "text/plain"
    });
}
function updateRootNode(urlStr){
    dojo.io.bind({
        url: addTimeToAjaxUrl(urlStr),
		encoding: "utf-8",
        load: function(type, data, evt){
           treeWidget.children[0].titleNode.innerHTML=data.title;
           treeWidget.children[0].objectId=data.objectId;
           onQueryTreeSelect();
        },
        error: function(type, error){

         },
        mimetype: "application/json"
    });
}
function menuAboutToShow(){
    var source = menu.getTopOpenEvent().target;
    while (!source.getAttribute('treeNode') && source.tagName != 'body') {
        source = source.parentNode;
    }
    if (source.tagName == 'body') {
        dojo.raise("treeNode not detected");
    }
    var treeNode = dojo.widget.manager.getWidgetById(source.getAttribute('treeNode'));
    if(treeSelector.selectedNode){
        treeSelector.deselect();
    }
    treeSelector.doSelect(treeNode);
    var nodeType=treeNode.objectId;
    //OR=0;AND=1;EXP=2;
    removeMenuItem("treeContextMenuChangeToADD");
    removeMenuItem("treeContextMenuChangeToOR");
    removeMenuItem("treeContextMenuAddEXP");
    if(nodeType==0){//OR
       mnitAND = dojo.widget.createWidget("TreeMenuItem",
       {	caption:menuItemChangeToAND,
            widgetId:"treeContextMenuChangeToADD"
       });
       mnitAND.onClick=mitAndClick;
       menu.addChild(mnitAND);
    }
    if(nodeType==1){//AND
       mnitOR = dojo.widget.createWidget("TreeMenuItem",
       {	caption:menuItemChangeToOR,
            widgetId:"treeContextMenuChangeToOR"
       });
       mnitOR.onClick=mitOrClick;
       menu.addChild(mnitOR);
    }
    if(nodeType!=2){
       mnitAddEXP = dojo.widget.createWidget("TreeMenuItem",
       {	caption:menuItemAddExp,
            widgetId:"treeContextMenuAddEXP"
       });
       mnitAddEXP.onClick=mitAddEXPClick;
       menu.addChild(mnitAddEXP);
    }
    if(treeNode==treeWidget.children[0]){
        mitUp.setDisabled.call(mitUp,true);
        mitDown.setDisabled.call(mitDown,true);
        mitRemove.setDisabled.call(mitRemove,true);
    }else{
        mitRemove.setDisabled.call(mitRemove,false);
        mitUp.setDisabled.call(mitUp,treeNode.parent.children[0]==treeNode);
        mitDown.setDisabled.call(mitDown,treeNode.parent.children[treeNode.parent.children.length]==treeNode);
    }
}
function removeMenuItem(id){
    var menuChildren=menu.children;
    var i=0;
    for(i=0;i<menuChildren.length;i++){
        if(menuChildren[i].widgetId==id){
            menu.removeChild(menuChildren[i]);
            break;
        }
    }
}
function mitOrClick(){
    var nodeId=treeSelector.selectedNode.widgetId;
    if(nodeId==treeWidget.children[0].widgetId){
        updateRootNode("queryTreeAction!updateRootNode.action");
        return;
    }
    var newType=0;
    var urlStr="queryTreeAction!changeNodeType.action?nodeID="+nodeId+"&type="+newType;
    changeNode(urlStr);
}
function mitAndClick(){
    var nodeId=treeSelector.selectedNode.widgetId;
    if(nodeId==treeWidget.children[0].widgetId){
        updateRootNode("queryTreeAction!updateRootNode.action");
        return;
    }
    var newType=1;
    var urlStr="queryTreeAction!changeNodeType.action?nodeID="+nodeId+"&type="+newType;
    changeNode(urlStr);
}
function mitAddEXPClick(){
    create('EXP');
}
function onMoveTo(evt){
    var nodeChildId=evt.child.widgetId;
    var oldParent=evt.oldParent;
    var oldParentId=oldParent.widgetId;
    var newParent=evt.newParent;
    var newParentId=newParent.widgetId;
    var i=0;
    var index=0;
    for(i=0;i<newParent.children.length;i++){
        if(newParent.children[i].widgetId==nodeChildId){
           index=i;
           break;
        }
    }
    var urlStr="queryTreeAction!moveNode.action?nodeID="+nodeChildId+"&from="+oldParentId+"&to="+newParentId+"&index="+index;
    dojo.io.bind({
        url: addTimeToAjaxUrl(urlStr),
		encoding: "utf-8",
        load: function(type, data, evt){
            refreshNode(treeWidget.children[0]);
            //refreshNode(oldParent);
        },
        error: function(type, error){
             alert("error:"+error);
         },
        mimetype: "text/plain"
    });
}
function changeNodeType(nodeId){
    if(nodeId==null){
        nodeId=document.getElementById("nodeID").value;
    }
    if(nodeId==treeWidget.children[0].widgetId){
        updateRootNode("queryTreeAction!updateRootNode.action");
        return;
    }
    var newType=document.getElementById("types").value;
    var urlStr="queryTreeAction!changeNodeType.action?nodeID="+nodeId+"&type="+newType;
    changeNode(urlStr);
}
function changeNode(urlStr){
    //blockDetail();
    dojo.io.bind({
        url: addTimeToAjaxUrl(urlStr),
		encoding: "utf-8",
        load: function(type, data, evt){
            if(treeSelector.selectedNode.parent==treeWidget){
                refreshNode(treeSelector.selectedNode);
            }else{
                refreshNode(treeSelector.selectedNode.parent);
            }
        },
        error: function(type, error){
         /*do something w/ the error*/
         },
        mimetype: "text/plain"
     });
}
function updateNodeExpression(index){
    var formName="nodeExpressionAction";
    var urlStr="queryNodeExpressionAction!update.action"
    //blockDetail();
    dojo.io.bind({
        url: addTimeToAjaxUrl(urlStr),
        encoding: "utf-8",
        load: function(type, data, evt){
            refreshNode(treeSelector.selectedNode.parent);
        },
        error: function(type, error){
         /*do something w/ the error*/
         },
        method: "POST",
        formNode: dojo.byId(formName),
        mimetype: "text/plain"
     });
}

function updateDropDown(index){
    var formName="nodeExpressionAction";
    var urlStr="queryNodeExpressionAction!refresh.action"
    //blockDetail();
    dojo.io.bind({
        url: addTimeToAjaxUrl(urlStr),
		encoding: "utf-8",
        load: function(type, data, evt){
             //var detailPaneWidget = dojo.widget.byId("details");
            //detailPaneWidget.setContent(data);
            refreshNode(treeSelector.selectedNode.parent);
        },
        error: function(type, error){
         /*do something w/ the error*/
         },
        method: "POST",
        formNode: dojo.byId(formName),
        mimetype: "text/plain"
     });
}


//------------------------------------------------------------------------------
//End part actions
//------------------------------------------------------------------------------


//---------------
//Refresh tree

function refreshNode(node,nodeToSelect){
    refreshQueryString();
    var selectedNode=treeSelector.selectedNode;
    var nodeId=node.widgetId;
    var	selectedNodeId = "";
    var wasExpanded = false;
    if (selectedNode!=null){
        selectedNodeId=selectedNode.widgetId;
    }
    if(nodeToSelect!=null){
        selectedNodeId=nodeToSelect;
    }
    wasExpanded=node.isExpanded;
    //create a "copy" of treeNodes expanded  from tree
    var tns=createTreeSchema(node);
    //remove all children
    while(node.children.length>0){
        var child = node.children[0];
        child.tree.removeNode(child);
    }
    //colapse node
    //treeController.collapse(node);
    node.isExpanded = false;
	node.updateExpandIcon();
    node.state= node.loadStates.UNCHECKED;
    if(wasExpanded==true){
       expandRecursive(node,tns,selectedNodeId);
    }
}

/*
Expand a node recursive
	node - the node to be expanded
	tns - the snap-shot of expanded tree
	selectedNodeId- the widgetId of selected node
*/
function expandRecursive(node,tns,selectedNodeId){
	treeController.expand(node,true);
	for(var i=0; i<node.children.length; i++) {
		var child = node.children[i];
		var childId = node.children[i].widgetId;
		if(childId==selectedNodeId){
			treeSelector.doSelect(child);
			onQueryTreeSelect();
		}
        if(tns==null||tns.children==null||tns.children.length==0){
            continue;
        }
        for(var j=0; j<tns.children.length; j++) {
			var tnsChild=tns.children[j];
			if(childId==tnsChild.id){
                expandRecursive(child,tnsChild,selectedNodeId);
				break;
			}
		}
	}
}

function TreeNodeSchema(/*String*/ id){
    this.id=id;
	this.children=new Array();
}
TreeNodeSchema.prototype.add=function(/*TreeNodeSchema*/ child){
	this.children[this.children.length]=child;
}
/*
Create a snap-shot of the treeNodes expanded from tree
*/
function createTreeSchema(node){
	var  tns=new TreeNodeSchema("root");
	for(var i=0; i<node.children.length; i++) {
		var child = node.children[i];
		var childTns=createTreeNodeSchema(child);
        if(childTns!=null){
            tns.add(childTns);
        }
    }
	return tns;
}

/*
Create a snap-shot of a treeNode expanded child recursive
*/
function createTreeNodeSchema(node){
	if(node.isFolder==false){
		return null;
	}
	if(node.isExpanded==false){
		return null;
	}
	var tns;
	tns=new TreeNodeSchema(node.widgetId);
	for(var i=0; i<node.children.length; i++) {
		var child = node.children[i];
		var childTns=createTreeNodeSchema(child);
		if(childTns!=null){
			tns.add(childTns);
		}
	}
	return tns;
}
//-------------------------------------------------
//selection
//------------

function onQueryTreeSelect(){
    if(oldPicker!=null){
        this["wasinit"+oldPicker]=null;
    }
    var widgetId=treeSelector.selectedNode.widgetId;
    var queryPurpose = document.getElementById("queryPurpose");
    var nodeType=treeSelector.selectedNode.objectId;
    var btnAND=document.getElementById("btnAND");
    var btnOR=document.getElementById("btnOR");
    var btnEXP=document.getElementById("btnEXP");
    if(nodeType==2){//expresion
        btnAND.disabled=true;
        btnOR.disabled=true;
        btnEXP.disabled=true;
    }else{
        btnAND.disabled=false;
        btnOR.disabled=false;
        btnEXP.disabled=false;
    }
    //var myDate=new Date();
    var urlStr="queryTreeAction!detailNode.action?nodeID="+widgetId+"&queryPurpose="+queryPurpose.value;//+"&time="+myDate;
    //blockDetail();
    dojo.io.bind({
        url: addTimeToAjaxUrl(urlStr),
		encoding: "utf-8",
		useCache:false,
		load: function(type, data, evt){
             //detailPane.innerHTML=data;
            var detailPaneWidget = dojo.widget.byId("details");
            detailPaneWidget.setContent(data);
        },
        error: function(type, error){
            detailPane.innerHTML=error.message;
         /*do something w/ the error*/
         },
        mimetype: "text/plain"
    });
}
/*
function onQueryTreeSelect1(){
    var widgetId=treeSelector.selectedNode.widgetId;
    var nodeType=treeSelector.selectedNode.objectId;
    var btnAND=document.getElementById("btnAND");
    var btnOR=document.getElementById("btnOR");
    var btnEXP=document.getElementById("btnEXP");
    if(nodeType==2){//expresion
        btnAND.disabled=true;
        btnOR.disabled=true;
        btnEXP.disabled=true;
    }else{
        btnAND.disabled=false;
        btnOR.disabled=false;
        btnEXP.disabled=false;
    }
    var myDate=new Date();
    var urlStr="queryTreeAction!detailNode.action?nodeID="+widgetId+"&nodeSelect=true"+"&time="+myDate;
    //blockDetail();
    dojo.io.bind({
        url: addTimeToAjaxUrl(urlStr),
		encoding: "utf-8",
        load: function(type, data, evt){
             //detailPane.innerHTML=data;
             var detailPaneWidget = dojo.widget.byId("details");
            detailPaneWidget.setContent(data);
        },
        error: function(type, error){
            detailPane.innerHTML=error.message;
         },
        mimetype: "text/plain"
    });
}
*/
function blockDetail(){
    var div1=document.createElement("DIV");
    document.createElement("DIV");
    div1.innerHTML="<img style='position: absolute; top: 0px;left: 60%;' src='"+getContextPath()+"/design/classic/img/loading.gif'/>"
    var detailPane=document.getElementById("details");
    detailPane.appendChild(div1);
    detailPane.style.display="block";
}

function mouseOverTrigBtn(textField, picker,jsCalDateFormat){
		document.getElementById(picker).style.background='#1d5090';
		if(this["wasinit"+picker]==null){
			Calendar.setup({
				inputField : textField, // ID of the input field
				ifFormat : jsCalDateFormat, // the date format
				button : picker // ID of the button
			});
			this["wasinit"+picker]=true;
            oldPicker=picker;
        }
	}

/*
function showHideProject(selectedIndex) {
	var repositoryType=document.getElementById("repositoryType");
	var repositoryTypeValue = repositoryType.options[selectedIndex].value;
	var show = (repositoryTypeValue==3);
	var project = document.getElementById("project");
	//var projectLabel = document.getElementById("projectLabel");
	if (show) {
		project.style.display='block';
		//projectLabel.style.display='block';
	} else {
		project.style.display='none';
		//projectLabel.style.display='none';
	}
}
*/
function refreshQueryString(){
    var urlStr="queryTreeAction!refreshQueryString.action"
    //blockDetail();
    dojo.io.bind({
        url: addTimeToAjaxUrl(urlStr),
		encoding: "utf-8",
        load: function(type, data, evt){
            document.getElementById("queryString").innerHTML=data;
        },
        error: function(type, error){
         /*do something w/ the error*/
         },
        mimetype: "text/plain"
     });
}

