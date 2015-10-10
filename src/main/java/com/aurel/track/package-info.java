/**
 * The application at the top level comprises the following packages:
 * <ul>
 *   <li><a href="{@docRoot}/com/aurel/track/admin/package-summary.html#package_description">com.aurel.track.admin</a>: Administration, customization, user management, and server management
 *   <li><a href="{@docRoot}/com/aurel/track/persist/package-summary.html#package_description">com.aurel.track.persist</a>: The O/R mapping layer based on Torque, and some business logic.
 *   You should not use these classes directly but rather use the DAO interface or better yet an existing 
 *   suitable business logic.
 *   <li><a href="{@docRoot}/com/aurel/track/beans/package-summary.html#package_description">com.aurel.track.beans</a>: The beans representing entries in the database.
 *   <li><a href="{@docRoot}/com/aurel/track/dao/package-summary.html#package_description">com.aurel.track.dao</a>: An abstraction for the Torque persistence layer. If possible
 *   use an existing business layer class rather than the DAO classes.
 *   <li><a href="{@docRoot}/com/aurel/track/exchange/package-summary.html#package_description">com.aurel.track.exchange</a>: This package together with 
 *   <a href="{@docRoot}/com/aurel/track/report/package-summary.html#package_description">com.aurel.track.report </a> and
 *   <a href="{@docRoot}/com/aurel/track/customize/category/report/package-summary.html#package_description">com.aurel.track.customize.category.report</a> implements the applications reporting engine. If you
 *   want to add your own data sources, study the classes in these packages.
 *   <li><a href="{@docRoot}/com/aurel/track/report/package-summary.html#package_description">com.aurel.track.report</a>: Here are the classes that implement the
 *   logic for cockpit tiles and report data sources.
 *   <li><a href="{@docRoot}/com/aurel/track/fieldType/package-summary.html#package_description">com.aurel.track.fieldType</a>: Here are the classes that implement the
 *   logic for field types (item property types).
 *   <li><a href="{@docRoot}/com/aurel/track/vcs/package-summary.html#package_description">com.aurel.track.vc</a>: Support for the version control systems like Git, SVN, and CVS.
 * </ul>
 */
package com.aurel.track;
