/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */


package com.aurel.track.util.event;

import java.util.List;



/**
 * This interface describes oberservers that can attach to an
 * AdminEventPublisher. They will get notified of events of
 * administrative actions, like registering of new users etc.
 * @author Joerg Friedrich <joerg.friedrich@trackplus.com>
 * @version $Revision: 6574 $ $Date: 2014-10-09 16:27:46 +0200 (Do, 09 Okt 2014) $
 */

public interface IEventSubscriber {
	
	//user events
	public static final int EVENT_USER_BASE                 = 1000;
	public static final int EVENT_PRE_USER_REGISTERED       = EVENT_USER_BASE + 1;
	public static final int EVENT_POST_USER_REGISTERED      = EVENT_USER_BASE + 2;
	public static final int EVENT_POST_USER_LOGGED_IN       = EVENT_USER_BASE + 3;
	public static final int EVENT_PRE_USER_LOGGED_OUT       = EVENT_USER_BASE + 4;
	public static final int EVENT_POST_USER_LOGGED_OUT      = EVENT_USER_BASE + 5;
	public static final int EVENT_PRE_USER_DELETED          = EVENT_USER_BASE + 6;
	public static final int EVENT_PRE_USER_DEACTIVATED      = EVENT_USER_BASE + 7;
	public static final int EVENT_PRE_USER_ACTIVATED        = EVENT_USER_BASE + 8;
	public static final int EVENT_POST_USER_FORGOTPASSWORD  = EVENT_USER_BASE + 9;
	public static final int EVENT_POST_USER_REMINDER        = EVENT_USER_BASE + 10;
	public static final int EVENT_POST_USER_SELF_REGISTERED = EVENT_USER_BASE + 11;
	public static final int EVENT_POST_USER_CREATED_BY_EMAIL= EVENT_USER_BASE + 12;
	//system events
	public static final int EVENT_SYSTEM_BASE           = 2000;
	public static final int EVENT_POST_SYSTEM_STARTED   = EVENT_SYSTEM_BASE + 1;
	public static final int EVENT_PRE_SYSTEM_STOPPED    = EVENT_SYSTEM_BASE + 2;
	public static final int EVENT_PRE_SYSTEM_LOCKED     = EVENT_SYSTEM_BASE + 3;
	public static final int EVENT_PRE_SYSTEM_UNLOCKED   = EVENT_SYSTEM_BASE + 4;
	public static final int EVENT_POST_SYSTEM_UNLOCKED  = EVENT_SYSTEM_BASE + 5;
	//project events
	public static final int EVENT_PROJECT_BASE               = 3000;
	public static final int EVENT_PRE_PROJECT_ADDED          = EVENT_PROJECT_BASE + 1;
	public static final int EVENT_POST_PROJECT_ADDED         = EVENT_PROJECT_BASE + 2;
	public static final int EVENT_PRE_PROJECT_REMOVED        = EVENT_PROJECT_BASE + 3;
	public static final int EVENT_POST_PROJECT_REMOVED       = EVENT_PROJECT_BASE + 4;	
	public static final int EVENT_PRE_PROJECT_STATE_CHANGED  = EVENT_PROJECT_BASE + 5;
	public static final int EVENT_POST_PROJECT_STATE_CHANGED = EVENT_PROJECT_BASE + 6;
	public static final int EVENT_PRE_PROJECT_CHANGED        = EVENT_PROJECT_BASE + 7;
	public static final int EVENT_POST_PROJECT_CHANGED       = EVENT_PROJECT_BASE + 8;
	//release events
	public static final int EVENT_PRE_RELEASE_ADDED          = EVENT_PROJECT_BASE + 9;
	public static final int EVENT_POST_RELEASE_ADDED         = EVENT_PROJECT_BASE + 10;
	public static final int EVENT_PRE_RELEASE_STATE_CHANGED  = EVENT_PROJECT_BASE + 11;
	public static final int EVENT_POST_RELEASE_STATE_CHANGED = EVENT_PROJECT_BASE + 12;
	public static final int EVENT_PRE_RELEASE_CHANGED        = EVENT_PROJECT_BASE + 13;
	public static final int EVENT_POST_RELEASE_CHANGED       = EVENT_PROJECT_BASE + 14;	
	//item events
	public static final int EVENT_ISSUE_BASE                   = 4000;
	public static final int EVENT_PRE_ISSUE_CREATE             = EVENT_ISSUE_BASE + 1;
	public static final int EVENT_POST_ISSUE_CREATE            = EVENT_ISSUE_BASE + 2;
	public static final int EVENT_PRE_ISSUE_UPDATE             = EVENT_ISSUE_BASE + 3;
	public static final int EVENT_POST_ISSUE_UPDATE            = EVENT_ISSUE_BASE + 4;
	public static final int EVENT_PRE_ISSUE_MOVE               = EVENT_ISSUE_BASE + 5;
	public static final int EVENT_POST_ISSUE_MOVE              = EVENT_ISSUE_BASE + 6;
	public static final int EVENT_PRE_ISSUE_COPY               = EVENT_ISSUE_BASE + 7;
	public static final int EVENT_POST_ISSUE_COPY              = EVENT_ISSUE_BASE + 8;
	public static final int EVENT_PRE_ISSUE_CLOSE              = EVENT_ISSUE_BASE + 9;	
	public static final int EVENT_POST_ISSUE_CLOSE             = EVENT_ISSUE_BASE + 10;	
	public static final int EVENT_PRE_ISSUE_REOPEN             = EVENT_ISSUE_BASE + 11;
	public static final int EVENT_POST_ISSUE_REOPEN            = EVENT_ISSUE_BASE + 12;
	public static final int EVENT_PRE_ISSUE_CREATECHILD        = EVENT_ISSUE_BASE + 13;
	public static final int EVENT_POST_ISSUE_CREATECHILD       = EVENT_ISSUE_BASE + 14;	
	public static final int EVENT_PRE_ISSUE_CHANGESTATUS       = EVENT_ISSUE_BASE + 15;
	public static final int EVENT_POST_ISSUE_CHANGESTATUS      = EVENT_ISSUE_BASE + 16;
	public static final int EVENT_PRE_ISSUE_ADDCOMMENT         = EVENT_ISSUE_BASE + 17;
	public static final int EVENT_POST_ISSUE_ADDCOMMENT        = EVENT_ISSUE_BASE + 18;	
	public static final int EVENT_PRE_ISSUE_ASSIGNRESPONSIBLE  = EVENT_ISSUE_BASE + 19;
	public static final int EVENT_POST_ISSUE_ASSIGNRESPONSIBLE = EVENT_ISSUE_BASE + 20;
	public static final int EVENT_PRE_ISSUE_ASSIGNMANAGER      = EVENT_ISSUE_BASE + 21;
	public static final int EVENT_POST_ISSUE_ASSIGNMANAGER     = EVENT_ISSUE_BASE + 22;
	public static final int EVENT_PRE_ISSUE_CHANGEDATE         = EVENT_ISSUE_BASE + 23;
	public static final int EVENT_POST_ISSUE_CHANGEDATE        = EVENT_ISSUE_BASE + 24;
	public static final int EVENT_PRE_ISSUE_DELETE             = EVENT_ISSUE_BASE + 25;	
	public static final int EVENT_POST_ISSUE_DELETE            = EVENT_ISSUE_BASE + 26;
	//comment
	public static final int EVENT_PRE_ISSUE_EDITCOMMENT        = EVENT_ISSUE_BASE + 27;
	public static final int EVENT_POST_ISSUE_EDITCOMMENT       = EVENT_ISSUE_BASE + 28;
	public static final int EVENT_PRE_ISSUE_DELETECOMMENT      = EVENT_ISSUE_BASE + 29;
	public static final int EVENT_POST_ISSUE_DELETECOMMENT     = EVENT_ISSUE_BASE + 30;
	//attachment
	public static final int EVENT_PRE_ISSUE_ADDATTACHMENT      = EVENT_ISSUE_BASE + 31;
	public static final int EVENT_POST_ISSUE_ADDATTACHMENT     = EVENT_ISSUE_BASE + 32;	
	public static final int EVENT_PRE_ISSUE_REMOVEATTACHMENT   = EVENT_ISSUE_BASE + 33;
	public static final int EVENT_POST_ISSUE_REMOVEATTACHMENT  = EVENT_ISSUE_BASE + 34;	
	public static final int EVENT_PRE_ISSUE_OPENATTACHMENT     = EVENT_ISSUE_BASE + 35;
	public static final int EVENT_POST_ISSUE_OPENATTACHMENT    = EVENT_ISSUE_BASE + 36;	
	public static final int EVENT_PRE_ISSUE_MODIFYATTACHMENT   = EVENT_ISSUE_BASE + 37;
	public static final int EVENT_POST_ISSUE_MODIFYATTACHMENT  = EVENT_ISSUE_BASE + 38;	
	//watcher
	//public static final int EVENT_PRE_ISSUE_UPDATEWATCHER      = EVENT_ISSUE_BASE + 39;
	//public static final int EVENT_POST_ISSUE_UPDATEWATCHER     = EVENT_ISSUE_BASE + 40;
	//budget/plan/expense
	public static final int EVENT_PRE_ISSUE_UPDATEPLANNEDVALUE	= EVENT_ISSUE_BASE + 41;
	public static final int EVENT_POST_ISSUE_UPDATEPLANNEDVALUE	= EVENT_ISSUE_BASE + 42;
	public static final int EVENT_PRE_ISSUE_UPDATEREMAININGPLAN = EVENT_ISSUE_BASE + 43;
	public static final int EVENT_POST_ISSUE_UPDATEREMAININGPLAN= EVENT_ISSUE_BASE + 44;	
	public static final int EVENT_PRE_ISSUE_ADDEXPENSE        	= EVENT_ISSUE_BASE + 45;
	public static final int EVENT_POST_ISSUE_ADDEXPENSE       	= EVENT_ISSUE_BASE + 46;	
	public static final int EVENT_PRE_ISSUE_UPDATEEXPENSE      	= EVENT_ISSUE_BASE + 47;
	public static final int EVENT_POST_ISSUE_UPDATEEXPENSE     	= EVENT_ISSUE_BASE + 48;	
	public static final int EVENT_PRE_ISSUE_DELETEEXPENSE      	= EVENT_ISSUE_BASE + 49;
	public static final int EVENT_POST_ISSUE_DELETEEXPENSE     	= EVENT_ISSUE_BASE + 50;
	public static final int EVENT_PRE_ISSUE_UPDATEBUDGET       	= EVENT_ISSUE_BASE + 51;
	public static final int EVENT_POST_ISSUE_UPDATEBUDGET      	= EVENT_ISSUE_BASE + 52;
	//watcher
	public static final int EVENT_PRE_ISSUE_ADD_INFORMED      	= EVENT_ISSUE_BASE + 60;
	public static final int EVENT_POST_ISSUE_ADD_INFORMED		= EVENT_ISSUE_BASE + 61;
	public static final int EVENT_PRE_ISSUE_ADD_CONSULTED      	= EVENT_ISSUE_BASE + 62;
	public static final int EVENT_POST_ISSUE_ADD_CONSULTED		= EVENT_ISSUE_BASE + 63;
	public static final int EVENT_PRE_ISSUE_DELETE_INFORMED   	= EVENT_ISSUE_BASE + 64;
	public static final int EVENT_POST_ISSUE_DELETE_INFORMED  	= EVENT_ISSUE_BASE + 65;
	public static final int EVENT_PRE_ISSUE_DELETE_CONSULTED   	= EVENT_ISSUE_BASE + 66;
	public static final int EVENT_POST_ISSUE_DELETE_CONSULTED  	= EVENT_ISSUE_BASE + 67;
	
	//email submission
	public static final int EVENT_EMAIL_SUBMISSION_BASE     	= 5000;
	public static final int EVENT_PRE_ISSUE_CREATE_BY_EMAIL		= EVENT_EMAIL_SUBMISSION_BASE + 1;
	public static final int EVENT_POST_ISSUE_CREATE_BY_EMAIL	= EVENT_EMAIL_SUBMISSION_BASE + 2;
	public static final int EVENT_PRE_ISSUE_UPDATE_BY_EMAIL		= EVENT_EMAIL_SUBMISSION_BASE + 3;
	public static final int EVENT_POST_ISSUE_UPDATE_BY_EMAIL	= EVENT_EMAIL_SUBMISSION_BASE + 4;
	//field changed on the client side
	public static final int FIELD_CHANGED     = 6000;	
	
	
	// This is all that is known for right now. If additional
	// are required they have to be implemented in  the server
	// code, not just in the observer classes!
	
	/**
	 * React on the event(s)
	 * @param events
	 * @param eventContextObject
	 */
	public boolean update(List<Integer> events, Object eventContextObject);
	
	/**
	 * Gets the list of interested events for this subscriber 
	 * @return
	 */
	public List<Integer> getInterestedEvents();
}
