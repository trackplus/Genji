/*
 * This file is part of the Track+ application, a
 * tool for project and change management.
 *
 * Copyright (C) 2007 Trackplus
 *
 * Use and distribution of this software is governed by the
 * Track+ license conditions.
 * 
 * $Id: initOnExit.groovy 3930 2012-09-12 23:19:20Z friedj $
 * 
 */
 
 // This script is called at the end of system
 // initialization. Everything should be alive now. Full access
 // to the servlet, the application, and the applications
 // configuration from web.xml is available here, as well as to
 // the site configuration (type TSite, variable "site").
 // 
 // This script can be overwritten by an external script
 // with the same name and path.
 //
 // Bound variables:
 // - site (type TSite)
 // - config (type ServletConfig)
 // - application (type ServletContext)
 
 // We just give a small demonstration of what can be done here.
 // The Java startup routine will print the "output" variable to System.err.
 
 package plugins.groovy.system;
 
 // println();
 // println("Track+: System initialization completed.");
 output = "${site.attachmentRootDir}."
 // println();
 
