/*
 * This file is part of the Track+ application, a
 * tool for project and change management.
 *
 * Copyright (C) 2009 Trackplus
 *
 * Use and distribution of this software is governed by the
 * Track+ license conditions.
 * 
 * $Id: initOnEntry.groovy 3930 2012-09-12 23:19:20Z friedj $
 * 
 */
 
 // This script is called at the very beginning of system
 // initialization. Not much is alive yet, but full access
 // to the servlet, the application, and the applications
 // configuration from web.xml is available here.
 // 
 // This script cannot be overwritten by an external script
 // with the same name, since at this point in time the external
 // scripts are not yet available.
 //
 // Bound variables: 
 // - config (type ServletConfig)
 // - application (type ServletContext)

package plugins.groovy.system; 

import com.aurel.track.persist.TSitePeer;
//println(); 
//println("---------------------------------------------------------------------");
//Date now = new Date();
//print(now.toString());
//println(": Track+ system starts initialization...\r\n\r\n");
//println();
