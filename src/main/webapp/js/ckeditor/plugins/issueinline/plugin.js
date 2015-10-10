CKEDITOR.plugins.add( 'issueinline', {
	requires: 'widget',
	icons: 'issueinline',
	init: function( editor ) {
		editor.widgets.add( 'issueinline', {
			button: getText("common.btn.chooseItem")+' (Inline)',
			template:'<div class="inlineItem">...</div>',
			allowedContent:'div(!inlineItem,workitemid)',
			requiredContent: 'div(inlineItem)',
			insert:function(){
				var me=this;
				var projectID=null;
				var issuePicker=Ext.create('com.trackplus.util.IssuePicker',{
					title:getText('common.btn.chooseItem'),
					projectID:projectID==null?null:projectID*-1,
					handler:function(item){
						var key=item['objectID'];
						var id=item['id'];
						var title=item['title'];
						var element = editor.document.createElement( 'div' );
						element.setAttribute("class",'inlineItem');
						element.setAttribute("workitemid",key);
						var html=''
						element.setHtml(html);
						editor.insertElement( element );
						var widget = editor.widgets.initOn( element, 'issueinline' );
					}
				});
				issuePicker.showDialog();
			},
			edit:function(){
				var projectID=null;
				var me=this;
				var issuePicker=Ext.create('com.trackplus.util.IssuePicker',{
					title:getText('common.btn.chooseItem'),
					projectID:projectID==null?null:projectID*-1,
					workItemID:this.data.workItemID,
					handler:function(item){
						var key=item['objectID'];
						var id=item['id'];
						var title=item['title'];
						me.setData('workItemID',key);
					}
				});
				issuePicker.showDialog();
				return true;
			},

			upcast: function( element ) {
				return element.name == 'div' && element.hasClass( 'inlineItem' );
			},
			downcast:function(el){
				el.setHtml("");
			},

			init: function() {
				var width = this.element.getStyle( 'width' );
				var workItemID=this.element.getAttribute('workitemid');
				if(workItemID!=null){
					this.setData( 'workItemID', workItemID );
				}
			},

			data: function(evt) {
				var workItemIDAttribute=this.element.getAttribute('workitemid');
				if((this.data.workItemID!=null&&workItemIDAttribute!=this.data.workItemID)||this.element.getHtml().length<20){
					this.element.setAttribute('workitemid',this.data.workItemID);
					this.updateElementHtml(this.element,this.data.workItemID);
				}
			},
			updateElementHtml:function(element,workItemID){
				Ext.Ajax.request({
					url : "macroIssueAction.action",
					params : {
						'workItemID' : workItemID
					},
					success : function(response) {
						var dirty=editor.checkDirty();
						var jsonData = Ext.decode(response.responseText);
						var data=jsonData.data;
						var html=data.html;
						element.setHtml(html);
						if(!dirty){
							editor.resetDirty();
						}
					}
				});
			}
		});
	}
});