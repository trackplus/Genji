var taskCmd =
{
	canUndo : false,    // The undo snapshot will be handled by 'insertElement'.
	exec : function( editor )
	{
		var workItemID=editor.config.workItemID;
		if(workItemID==null){
			return true;
		}
		var selectedText =null;
		var selection =editor.getSelection();
		if(editor.getSelection().getType() == CKEDITOR.SELECTION_TEXT){
			selectedText =selection.getSelectedText();// getNative();
		}
		var ranges = selection.getRanges();
		var el = new CKEDITOR.dom.element("div");
		for (var i = 0, len = ranges.length; i < len; ++i) {
			el.append(ranges[i].cloneContents());
		}
		var selectedHTML= el.getHtml();

		if(selectedText==null||selectedText==''){
			return true;
		}
		var synopsis=selectedText;
		var description=selectedHTML;
		var projectID=null;
		borderLayout.controller.createNewIssue.call(borderLayout.controller,null,projectID,null,null,null,function(data){
			CWHF.showMsgInfo(getText('item.msg.newItemCreated', data.workItemIDDisplay, data.title));
			var key=data.workItemID;
			var id=data.workItemIDDisplay;
			var title=data.title;
			if(key!=id+""){
				title=id+":"+title;
			}
			editor.insertHtml("[issue "+key+" (<span class=\"disabled\">"+title+"</span>) /]");
		},this,synopsis,description);

		/*var urlStr="wiki!createRequirement.action";
		Ext.Ajax.request({
			url : urlStr,
			params : {
				workItemID:workItemID,
				title:title,
				description:selectedHTML
			},
			success : function(response) {
				var jsonData = Ext.decode(response.responseText);
				var newWorkItemID=jsonData.data.workItemID;
				var macro='[issue '+newWorkItemID+'/]';
				editor.insertHtml(macro);
				borderLayout.setLoading(false);
				//me.fireEvent('itemChanged');
			},
			failure : function() {
				borderLayout.setLoading(false);
				alert("failed!");
			}
		});*/
	}
};

CKEDITOR.plugins.add('task',{
	init:function(a) {
		var b="task";
		var c=a.addCommand(b,taskCmd);
		a.ui.addButton("task",{label:getText('menu.newItem'),command:b,icon:this.path+"images/item_add.png"});
	}
});
