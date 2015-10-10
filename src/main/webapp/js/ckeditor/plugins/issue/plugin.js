var currentEditor;
//var titleChooseIssue="Choose issue";
var issueCmd =
{
	canUndo : false,    // The undo snapshot will be handled by 'insertElement'.
	exec : function( editor )
	{
		showChooseItem(editor);
		currentEditor=editor;
	}
};

CKEDITOR.plugins.add('issue',   
  {    
    requires: ['dialog'],
	lang : ['de','en','es','fr','it'], 
    init:function(a) { 
		var b="issue";
		var c=a.addCommand(b,issueCmd);
		c.modes={wysiwyg:1,source:0};
		c.canUndo=false;
		//titleChooseIssue=a.lang.issue.title;
		a.ui.addButton("issue",{
						label: getText("common.btn.chooseItem"),
						command:b,
						icon:this.path+"images/task-link.png"
		});
	}
});
function showChooseItem(editor){
	var projectID=editor.config.projectID;
	var issuePicker=Ext.create('com.trackplus.util.IssuePicker',{
		title:getText('common.btn.chooseItem'),
		projectID:projectID==null?null:projectID*-1,
		handler:insertIssue
	});
	issuePicker.showDialog();
}

function insertIssue(item){
	var key=item['objectID'];
	var id=item['id'];
	var title=item['title'];
	if(key!=id+""){
		title=id+":"+title;
	}
	currentEditor.insertHtml("[issue "+key+" (<span class=\"disabled\">"+title+"</span>) /]");
}

