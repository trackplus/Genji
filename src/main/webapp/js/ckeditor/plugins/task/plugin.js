var taskCmd ={
	canUndo : false,    // The undo snapshot will be handled by 'insertElement'.
	exec : function( editor ){
		var workItemID=editor.config.workItemID;
		var projectID=editor.config.projectID;
		var useInlineWidget=editor.config.useInlineWidget;
		if(workItemID==null){
			return true;
		}
		var selectedPlainText =null;
		var selection =editor.getSelection();
		if(editor.getSelection().getType() == CKEDITOR.SELECTION_TEXT){
			selectedPlainText =selection.getSelectedText();// getNative();
		}
		var ranges = selection.getRanges();
		var el = new CKEDITOR.dom.element("div");
		for (var i = 0, len = ranges.length; i < len; ++i) {
			el.append(ranges[i].cloneContents());
		}
		var selectedHTML= el.getHtml();
		if(selectedPlainText==null||selectedPlainText==''){
			CWHF.showDialogMsgWarning(getText('menu.newItem'),getText('item.msg.emptySelectionForInlineItem'));
			return true;
		}


		//<br> is transform by ckeditor via "selection.getSelectedText();" in \r and <p> in stransform in \n
		var idx_br=selectedPlainText.indexOf("\r");
		var idx_p=selectedPlainText.indexOf("\n");
		//if there is a <br> (\r) or <p>(\n) we should cut until to first
		var idxCut=-1;// index of cutting (if necessary)
		if(idx_br>0){
			idxCut=idx_br;
		}
		if(idx_p>0&&idx_p<idx_br){
			//if there is a <p> before the <br>
			idxCut=idx_p;
		}
		if(idxCut>0){
			selectedPlainText=selectedPlainText.substring(0,idxCut);
		}
		var MAX_SYNOPSIS_LENGTH=100;
		if(selectedPlainText.length>MAX_SYNOPSIS_LENGTH){
			//cut to the last space before MAX_SYNOPSIS_LENGTH
			var idxSpace=selectedPlainText.lastIndexOf(" ",MAX_SYNOPSIS_LENGTH);
			if(idxSpace==-1){
				idxSpace=MAX_SYNOPSIS_LENGTH;
			}
			selectedPlainText=selectedPlainText.substring(0,idxSpace);
		}
		var synopsis=selectedPlainText;

		var description=selectedHTML;
		borderLayout.borderLayoutController.createNewIssue.call(borderLayout.borderLayoutController,null,projectID,null,null,null,function(data){
			CWHF.showMsgInfo(getText('item.msg.newItemCreated', data.workItemIDDisplay, data.title));
			var key=data.workItemID;
			var id=data.workItemIDDisplay;
			var title=data.title;
			if(key!=id+""){
				title=id+":"+title;
			}
			var html="";
			if(useInlineWidget===true) {
				html = '<div class="inlineItem" workitemid="' + key + '"></div>';
			}else {
				html = "[issue " + key + " (<span class=\"disabled\">" + title + "</span>) /]";
			}
			editor.insertHtml(html);
		},null,synopsis,description);
	}
};

CKEDITOR.plugins.add('task',{
	init:function(a) {
		var b="task";
		var c=a.addCommand(b,taskCmd);
		a.ui.addButton("task",{label:getText('menu.newItem'),command:b,icon:this.path+"images/item_add.png"});
	}
});
