/*******************************************************************************
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG.
 * All rights reserved. 
 *
 * This program and the accompanying materials are made 
 * available under the terms of the Genji License v2.0
 * which is available at http://www.trackplus.com/tpl-v20.html
 *
 * $Id: package-info.java 1661 2015-10-25 20:06:43Z friedric $
 *******************************************************************************/
/**
 *   The persist package contains the O/R mapping layer based on Torque, and some business logic.
 *   You should not use these classes directly, but rather work with the DAO interface, or better yet,
 *   with the business logic classes (xxxxBL.java).
 *   
 *   A mapping is required between the Java object model and the relational model used in the database. 
 *   Furthermore, some abstraction is required between the database and the application layer 
 *   to support different relational database management systems.
 *   
 *   For each database table there are a number of classes:
 *   <ul>
 *      <li> A mapping class which creates a relation between the 
 *           columns of the table and the attributes of the JavaBean class related to 
 *           this table (<code>src/com/aurel/track/persist/map</code>)
 *      <li> Two abstract classes which should not be modified  by the user. The 
 *           *Peer class represents an entire table, while the other class represents a single row in the table. 
 *           These classes start with "Base" , e.g. "BaseRelease". Each column of a row has 
 *           its own setter and getter methods, and the classes provide convenient methods to get related 
 *           objects which in the relational model are connected via foreign key constraints. 
 *           These generated classes are in <code>com/aurel/track/persist</code>
 *      <li> To extend and modify the behaviour defined by these classes the Torque generator creates two 
 *           additional classes which are derived from the "Base*" 
 *           classes. These classes all start with the letter "T" , e.g. "TRelease". 
 *      <li> For each table in the database, there is a bean generated having a property field for 
 *           each table column. These beans are pure 
 *           Java beans, having no association with the database. They can conveniently be used in the 
 *           application layer and view layer of the application. 
 *           The beans are separated in a base class, and a derived class which 
 *           can be extended and modified by the developer (<code>src/com/aurel/track/beans</code>).
 *   </ul>
 *   
 */
package com.aurel.track.persist;
