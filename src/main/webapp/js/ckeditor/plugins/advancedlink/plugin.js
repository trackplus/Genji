

CKEDITOR.plugins.add( 'advancedlink', {
	icons: 'advancedlink',
	onLoad: function() {
	},

	init: function( editor ) {
		var allowed = 'a[!href]';
		var required = 'a[href])';

		// Add the link and unlink buttons.
		editor.addCommand( 'advancedlink',{
			canUndo : false,    // The undo snapshot will be handled by 'insertElement'.
			//allowedContent: allowed,
			//requiredContent: required,
			exec : function( editor ){

				var selectedHTML=null;
				var element = CKEDITOR.plugins.advancedlink.getSelectedLink(editor);
				if(element==null){
					var selection =editor.getSelection();
					var ranges = selection.getRanges();
					var el = new CKEDITOR.dom.element("div");
					for (var i = 0, len = ranges.length; i < len; ++i) {
						el.append(ranges[i].cloneContents());
					}
					selectedHTML= el.getHtml();
				}
				CKEDITOR.plugins.advancedlink.openLinkDialog(editor,element,selectedHTML);
			}
		});
		editor.ui.addButton("Advancedlink",{label:getText('common.btn.link'),command:'advancedlink',icon:this.path+"icons/advancedlink.png"});

		editor.on( 'doubleclick', function( evt ) {
			var element = CKEDITOR.plugins.advancedlink.getSelectedLink( editor ) || evt.data.element;

			if ( !element.isReadOnly() ) {
				if ( element.is( 'a' ) ) {
					evt.stop();
					CKEDITOR.plugins.advancedlink.openLinkDialog(editor,element,null);
					return true;
				}
			}
		}, null, null, 0 );
	},

	afterInit: function( editor ) {
	}
});



CKEDITOR.plugins.advancedlink = {

	getSelectedLink: function( editor ) {
		var selection = editor.getSelection();
		var selectedElement = selection.getSelectedElement();
		if ( selectedElement && selectedElement.is( 'a' ) )
			return selectedElement;

		var range = selection.getRanges()[ 0 ];

		if ( range ) {
			range.shrink( CKEDITOR.SHRINK_TEXT );
			return editor.elementPath( range.getCommonAncestor() ).contains( 'a', 1 );
		}
		return null;
	},
	getLinkType:function(linkElement){
		var type=linkElement.getAttribute('type');
		if(type=='attachment'){
			return 1;
		}
		if(type=='workItem'){
			return 2;
		}
		if(type=='docuemnt'){
			return 3;
		}
		return 0;
	},
	openLinkDialog:function(editor,linkElement,selectedHTML){
		var workItemID=editor.config.workItemID;
		var projectID=editor.config.projectID;
		var useDocument=false;
		if(editor.config.useLinkDocument!=null&&editor.config.useLinkDocument===true){
			useDocument=true;
		}
		var linkType=0;
		var linkObjectID=null;
		var linkURL="";
		var linkText=selectedHTML;
		var linkTarget=null;
		if(linkElement!=null){
			linkURL=linkElement.getAttribute('href');
			linkTarget=linkElement.getAttribute('target');
			linkText=linkElement.getHtml();
			linkType= CKEDITOR.plugins.advancedlink.getLinkType(linkElement);
		}else{
			linkTarget="_blank";
		}
		var linkPicker=Ext.create('com.trackplus.linkPicker.LinkPicker',{
			title:getText('linkPicker.lbl.title'),
			projectID:projectID,
			workItemID:workItemID,
			useDocument:useDocument,
			linkType:linkType,
			linkURL:linkURL,
			linkText:linkText,
			linkTarget:linkTarget,
			handler:function(link){
				var attributes={
					href:link.href,
					'data-cke-saved-href':link.href,
					type:link.type,
					target:link.target
				}
				if(linkElement!=null){
					var html=link.html;
					linkElement.setAttributes( attributes );
					//element.removeAttributes( attributes.removed )
					linkElement.setHtml(html);
				}else{
					var selection = editor.getSelection();
					var range = selection.getRanges()[ 0 ];
					if ( range.collapsed ) {
						var text = new CKEDITOR.dom.text( link.html, editor.document );
						range.insertNode( text );
						range.selectNodeContents( text );
					}
					var style = new CKEDITOR.style( {
						element: 'a',
						attributes: attributes
					} );

					style.type = CKEDITOR.STYLE_INLINE; // need to override... dunno why.
					style.applyToRange( range, editor );
					range.select();
				}
			}
		});
		linkPicker.showDialog();
	}
}

