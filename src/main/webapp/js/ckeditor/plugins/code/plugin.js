var codeCmd =
{
	canUndo : false,    // The undo snapshot will be handled by 'insertElement'.
	exec : function( editor )
	{
		var selectedtxt ="";
		var selection =editor.getSelection();
		if(editor.getSelection().getType() == CKEDITOR.SELECTION_TEXT){
			selectedtxt =selection.getNative();
		}
		//var srcDivHtml='<div style="border: 1px solid rgb(140, 140, 140); padding: 5px; background-color: rgb(250, 250, 210);">'+selectedtxt+'</div>';
		//var srcDiv = new CKEDITOR.dom.element('div' );
		//setupSrcDiv(srcDiv,selectedtxt);
		//editor.insertHtml(srcDivHtml);
		containers = createDiv( editor, true );

		// Update elements attributes
		//for( var i = 0 ; i < containers.length ; i++ )
		//this.commitContent( containers[ i ] );
	}
};

CKEDITOR.plugins.add('code',{
	init:function(a) {
		var b="code";
		var c=a.addCommand(b,codeCmd);
		a.ui.addButton("code",{label:'code',command:b,icon:this.path+"code_c.png"});
	}
});

function setupSrcDiv(srcDiv){
	//srcDiv.setHtml(selectedtxt);
	srcDiv.setStyle( 'background-color', '#fafad2' );
	srcDiv.setStyle( 'border', '1px solid rgb(140,140,140)' );
	srcDiv.setStyle( 'padding', '5px' );
	srcDiv.setStyle( 'padding', '5px' );
	srcDiv.addClass("code-text");
}

/**
 * Divide a set of nodes to different groups by their path's blocklimit element.
 * Note: the specified nodes should be in source order naturally, which mean they are supposed to producea by following class:
 *  * CKEDITOR.dom.range.Iterator
 *  * CKEDITOR.dom.domWalker
 *  @return {Array []} the grouped nodes
 */
function groupByDivLimit( nodes )
{
	var groups = [],
		lastDivLimit = null,
		path, block;
	for ( var i = 0 ; i < nodes.length ; i++ )
	{
		block = nodes[i];
		var limit = getDivLimitElement( block );
		if ( !limit.equals( lastDivLimit ) )
		{
			lastDivLimit = limit ;
			groups.push( [] ) ;
		}
		groups[ groups.length - 1 ].push( block ) ;
	}
	return groups;
}


// Definition of elements at which div operation should stopped.
var divLimitDefinition = ( function(){

	// Customzie from specialize blockLimit elements
	var definition = CKEDITOR.tools.extend( {}, CKEDITOR.dtd.$blockLimit );

	// Exclude 'div' itself.
	delete definition.div;

	// Exclude 'td' and 'th' when 'wrapping table'
	/*if( editor.config.div_wrapTable )
	 {
	 delete definition.td;
	 delete definition.th;
	 }*/
	return definition;
})();

// DTD of 'div' element
var dtd = CKEDITOR.dtd.div;

/**
 * Get the first div limit element on the element's path.
 * @param {Object} element
 */
function getDivLimitElement( element )
{
	var pathElements = new CKEDITOR.dom.elementPath( element ).elements;
	var divLimit;
	for ( var i = 0; i < pathElements.length ; i++ )
	{
		if ( pathElements[ i ].getName() in divLimitDefinition )
		{
			divLimit = pathElements[ i ];
			break;
		}
	}
	return divLimit;
}

/**
 * Add to collection with DUP examination.
 * @param {Object} collection
 * @param {Object} element
 * @param {Object} database
 */
function addSafely( collection, element, database )
{
	// 1. IE doesn't support customData on text nodes;
	// 2. Text nodes never get chance to appear twice;
	if ( !element.is || !element.getCustomData( 'block_processed' ) )
	{
		element.is && CKEDITOR.dom.element.setMarker( database, element, 'block_processed', true );
		collection.push( element );
	}
}

function getNonEmptyChildren( element )
{
	var retval = [];
	var children = element.getChildren();
	for( var i = 0 ; i < children.count() ; i++ )
	{
		var child = children.getItem( i );
		if( ! ( child.type === CKEDITOR.NODE_TEXT
			&& ( /^[ \t\n\r]+$/ ).test( child.getText() ) ) )
			retval.push( child );
	}
	return retval;
}

/**
 * Wrapping 'div' element around appropriate blocks among the selected ranges.
 * @param {Object} editor
 */
function createDiv( editor )
{
	// new adding containers OR detected pre-existed containers.
	var containers = [];
	// node markers store.
	var database = {};
	// All block level elements which contained by the ranges.
	var containedBlocks = [], block;

	// Get all ranges from the selection.
	var selection = editor.document.getSelection();
	var ranges = selection.getRanges();
	var bookmarks = selection.createBookmarks();
	var i, iterator;

	// Calcualte a default block tag if we need to create blocks.
	var blockTag = editor.config.enterMode == CKEDITOR.ENTER_DIV ? 'div' : 'p';

	// collect all included elements from dom-iterator
	for( i = 0 ; i < ranges.length ; i++ )
	{
		iterator = ranges[ i ].createIterator();
		while( ( block = iterator.getNextParagraph() ) )
		{
			// include contents of blockLimit elements.
			if( block.getName() in divLimitDefinition )
			{
				var j, childNodes = block.getChildren();
				for ( j = 0 ; j < childNodes.count() ; j++ )
					addSafely( containedBlocks, childNodes.getItem( j ) , database );
			}
			else
			{
				// Bypass dtd disallowed elements.
				while( !dtd[ block.getName() ] && block.getName() != 'body' )
					block = block.getParent();
				addSafely( containedBlocks, block, database );
			}
		}
	}

	CKEDITOR.dom.element.clearAllMarkers( database );

	var blockGroups = groupByDivLimit( containedBlocks );
	var ancestor, blockEl, divElement;

	for( i = 0 ; i < blockGroups.length ; i++ )
	{
		var currentNode = blockGroups[ i ][ 0 ];

		// Calculate the common parent node of all contained elements.
		ancestor = currentNode.getParent();
		for ( j = 1 ; j < blockGroups[ i ].length; j++ )
			ancestor = ancestor.getCommonAncestor( blockGroups[ i ][ j ] );

		divElement = new CKEDITOR.dom.element( 'div', editor.document );
		setupSrcDiv(divElement);
		// Normalize the blocks in each group to a common parent.
		for( j = 0; j < blockGroups[ i ].length ; j++ )
		{
			currentNode = blockGroups[ i ][ j ];

			while( !currentNode.getParent().equals( ancestor ) )
				currentNode = currentNode.getParent();

			// This could introduce some duplicated elements in array.
			blockGroups[ i ][ j ] = currentNode;
		}

		// Wrapped blocks counting
		var fixedBlock = null;
		for ( j = 0 ; j < blockGroups[ i ].length ; j++ )
		{
			currentNode = blockGroups[ i ][ j ];

			// Avoid DUP elements introduced by grouping.
			if ( !( currentNode.getCustomData && currentNode.getCustomData( 'block_processed' ) ) )
			{
				currentNode.is && CKEDITOR.dom.element.setMarker( database, currentNode, 'block_processed', true );

				// Establish new container, wrapping all elements in this group.
				if ( !j )
					divElement.insertBefore( currentNode );

				divElement.append( currentNode );
			}
		}

		CKEDITOR.dom.element.clearAllMarkers( database );
		containers.push( divElement );
	}

	selection.selectBookmarks( bookmarks );
	return containers;
}
