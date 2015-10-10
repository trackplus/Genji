/*
 * This file is part of the Track+ application, a
 * tool for project and change management.
 *
 * Copyright (C) 2009 Trackplus
 *
 * Use and distribution of this software is governed by the
 * Track+ license conditions.
 * 
 * $Id: DefaultActionHandler.groovy 3495 2012-03-15 17:23:21Z tamas $
 * 
 */
package plugins.groovy.system; 

/**
 * <p>
 * This serves as the default action action handler written in Groovy.
 * Action handlers are called before an action is executed (preAction), 
 * and after an action is executed (postAction).
 * </p>
 * <p>
 * All actions have at least these bound variables:<br/>
 * <ul>
 *   <li><strong>user</strong>: the current user, <code>com.aurel.track.beans.TPersonBean</code></li>
 *   <li><strong>paramMap</strong>: parameter map, <code>java.util.Map</code>. Map with parameters, depends on action.</li>
 *   <li><strong>resultCode</strong>: result code, <code>java.lang.Integer</code>. 0 = continue, negative numbers: error codes</li>
 *   <li><strong>resultMap</strong>: result map, <code>java.util.Map</code>. Map with results, depends on action.</li>
 * </ul>
 * The DefaultActionHandler is being used whenever there is no specialized custom handler.
 * The specialized custom handlers can be found in the scripts table of the database.
 *  
 */
class DefaultActionHandler {
	
	 /**
	  * <p>This method is being called before an action is executed, like a save to
	  * a database or load of a form. If the result code is 0, the calling function
	  * may continue.
	  * </p> 
	  * <p>If the function code is not zero, the behavior of the calling
	  * function is specific to that function.</p>
	  */
	 public void preAction() {
		 resultCode = new Integer(0);
	 }

	 /**
	  * <p>This method is being called after an action is executed, like a save to
	  * a database. If the result code is 0 or positive, everything is okay.
	  * What is being done then depends on the specific action.
	  * </p>
	  * <p>If the result code is negative, an error occurred. Again, what is being
	  * done then depends on the specific action.
	  * </p>
	  */
	 public void postAction() {
		 resultCode = new Integer(0);
	 }
}
