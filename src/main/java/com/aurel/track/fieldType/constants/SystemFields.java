/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */


package com.aurel.track.fieldType.constants;

/**
 * Defines constants for all system fields
 * to uniquely identify them.
 * @author Tamas Ruff
 *
 */
public class SystemFields {
    //required system dropdowns
    /**
     * PROJECT
     */
    public static final int PROJECT = 1;
    /**
     * ISSUETYPE
     */
    public static final int ISSUETYPE = 2;
    /**
     * SUBPROJECT also called subsystem or component in some places
     */
    public static final int SUBPROJECT = 3;
    /**
     * STATE is the issue status
     */
    public static final int STATE = 4;
    /**
     * MANAGER
     */
    public static final int MANAGER = 5;
    /**
     * RESPONSIBLE
     */
    public static final int RESPONSIBLE = 6;

    //not required system dropdowns
    /**
     * CLASS
     */
    public static final int CLASS = 7;
    /**
     * RELEASENOTICED
     */
    public static final int RELEASENOTICED = 8;
    /**
     * RELEASESCHEDULED
     */
    public static final int RELEASESCHEDULED = 9;
    /**
     * PRIORITY
     */
    public static final int PRIORITY = 10;
    /**
     * SEVERITY
     */
    public static final int SEVERITY = 11;

    //system label fields
    /**
     * ISSUENO
     */
    public static final int ISSUENO = 12;
    /**
     * ORIGINATOR
     */
    public static final int ORIGINATOR = 13;
    /**
     * CREATEDATE
     */
    public static final int CREATEDATE = 14;
    /**
     * LASTMODIFYDATE
     */
    public static final int LASTMODIFIEDDATE = 15;
    /**
     * SUPERIORWORKITEM also called parent issue
     */
    public static final int SUPERIORWORKITEM = 16;
    //TODO change the number
    /**
     * CHANGEDBY
     */
    public static final int CHANGEDBY = 24;

    //required system textbox
    /**
     * SYNOPSIS also called issue title
     */
    public static final int SYNOPSIS = 17;

    //not required system textbox
    /**
     * BUILD
     */
    public static final int BUILD = 18;
    /**
     * STARTDATE
     */
    public static final int STARTDATE = 19;
    /**
     * ENDDATE
     */
    public static final int ENDDATE = 20;
    /**
     * DESCRIPTION s the issue description
     */
    public static final int DESCRIPTION = 21;

    //not required system checkbox
    /**
     * ACCESSLEVEL
     */
    public static final int ACCESSLEVEL = 22;


    /**
     * COMMENT this a "pseudo" system field: may appear on screens and will be saved in history
     * but doesn't appear in other places where "normal" fields are dealt with.
     */
    public static final int COMMENT = 23;

    /**
     * ARCHIVELEVEL not required system integer: whether the workItem is archived or (logically) deleted
     */
    public static final int ARCHIVELEVEL = 25;

    /**
     * SUBMITTEREMAIL not required system field: if the workItem was created from an e-mail it will store the sender address
     */
    public static final int SUBMITTEREMAIL = 26;

    /**
     * IDNUMBER: building block for a WBS
     */
    public static final int WBS = 27;

    /**
     * PROJECT_SPECIFIC_ISSUENO system field: a unique issueID inside the project
     */
    public static final int PROJECT_SPECIFIC_ISSUENO = 28;

    /**
     * The target start date
     */
    public static final int TOP_DOWN_START_DATE = 29;

    /**
     * The target end date
     */
    public static final int TOP_DOWN_END_DATE = 30;

    /**
     * Whether a task typed item is milestone
     * It is 32 not 31 because the participants custom field was added into populate.sql
     */
    public static final int TASK_IS_MILESTONE = 32;
    
    /**
     * The duration of  a task
     */
    public static final int DURATION = 33;
    
    /**
     * The top/down planned duration of a task
     */
    public static final int TOP_DOWN_DURATION = 34;

    /**
     * the history field for attachment add/modify/delete
     */
    public static final int ATTACHMENT_ADD_HISTORY_FIELD =-1;
    public static final int ATTACHMENT_MODIFY_HISTORY_FIELD =-2;
    public static final int ATTACHMENT_DELETE_HISTORY_FIELD =-3;

    //dummy fields for sending watcher modification notification e-mails
    public static final int ADD_INFORMED = -50;
    public static final int DELETE_INFORMED = -51;
    public static final int ADD_CONSULTED = -52;
    public static final int DELETE_CONSULTED = -53;

    /**
     * Symbolic numbers used in history filter to allow adding this two fields
     * to the list of system and custom fields list to be selectable
     * (instead of of rendering them separately)
     */
    public static final int COST_HISTORY = -10;
    public static final int PLAN_HISTORY = -11;
    public static final int BUDGET_HISTORY = -12;


    /**
     * !!!Exclusive for Revenue Solutions!!!
     */
    public static final int MIGRATION_ADD_HISTORY_FIELD =-20;
    public static final int MIGRATION_MODIFY_HISTORY_FIELD =-21;
    public static final int MIGRATION_DELETE_HISTORY_FIELD =-22;
    /**
     * !!!Exclusive for Revenue Solutions!!!
     */

    public static final int COMMENT_MODIFY_HISTORY_FIELD =-30;
    public static final int COMMENT_DELETE_HISTORY_FIELD =-31;


    public static final int VERSION_CONTROL = -40;

    public static final Integer INTEGER_PROJECT =Integer.valueOf(PROJECT);
    public static final Integer INTEGER_ISSUETYPE = Integer.valueOf(ISSUETYPE);
    public static final Integer INTEGER_SUBPROJECT = Integer.valueOf(SUBPROJECT);
    public static final Integer INTEGER_STATE = Integer.valueOf(STATE);
    public static final Integer INTEGER_MANAGER = Integer.valueOf(MANAGER);
    public static final Integer INTEGER_RESPONSIBLE = Integer.valueOf(RESPONSIBLE);

    //not required system dropdowns
    public static final Integer INTEGER_CLASS = Integer.valueOf(CLASS);
    public static final Integer INTEGER_RELEASENOTICED = Integer.valueOf(RELEASENOTICED);
    public static final Integer INTEGER_RELEASESCHEDULED = Integer.valueOf(RELEASESCHEDULED);
    public static final Integer INTEGER_PRIORITY = Integer.valueOf(PRIORITY);
    public static final Integer INTEGER_SEVERITY = Integer.valueOf(SEVERITY);

    //system label fields
    public static final Integer INTEGER_ISSUENO = Integer.valueOf(ISSUENO);
    public static final Integer INTEGER_ORIGINATOR = Integer.valueOf(ORIGINATOR);
    public static final Integer INTEGER_CREATEDATE = Integer.valueOf(CREATEDATE);
    public static final Integer INTEGER_LASTMODIFIEDDATE = Integer.valueOf(LASTMODIFIEDDATE);
    public static final Integer INTEGER_SUPERIORWORKITEM = Integer.valueOf(SUPERIORWORKITEM);
    //TODO change the number
    public static final Integer INTEGER_CHANGEDBY = Integer.valueOf(CHANGEDBY);

    //required system textbox
    public static final Integer INTEGER_SYNOPSIS = Integer.valueOf(SYNOPSIS);

    //not required system textbox
    public static final Integer INTEGER_BUILD = Integer.valueOf(BUILD);
    public static final Integer INTEGER_STARTDATE = Integer.valueOf(STARTDATE);
    public static final Integer INTEGER_ENDDATE = Integer.valueOf(ENDDATE);
    public static final Integer INTEGER_DESCRIPTION = Integer.valueOf(DESCRIPTION);

    //not required system checkbox
    public static final Integer INTEGER_ACCESSLEVEL = Integer.valueOf(ACCESSLEVEL);

    //this a "pseudo" system field: may appear on screens and will be saved in history
    //but doesn't appear in other places where "normal" fields are dealt with
    public static final Integer INTEGER_COMMENT = Integer.valueOf(COMMENT);

    //not required system integer
    public static final Integer INTEGER_ARCHIVELEVEL = Integer.valueOf(ARCHIVELEVEL);

    public static final Integer INTEGER_SUBMITTEREMAIL = Integer.valueOf(SUBMITTEREMAIL);

    public static final Integer INTEGER_WBS = Integer.valueOf(WBS);

    public static final Integer INTEGER_PROJECT_SPECIFIC_ISSUENO = Integer.valueOf(PROJECT_SPECIFIC_ISSUENO);

    /**
     * The target start date
     */
    public static final Integer INTEGER_TOP_DOWN_START_DATE = Integer.valueOf(TOP_DOWN_START_DATE);
    /**
     * The target end date
     */
    public static final Integer INTEGER_TOP_DOWN_END_DATE = Integer.valueOf(TOP_DOWN_END_DATE);

    /**
     * Whether a task typed item is milestone
     */
    public static final Integer INTEGER_TASK_IS_MILESTONE = Integer.valueOf(TASK_IS_MILESTONE);
    
    /**
     * The duration of  a task
     */
    public static final Integer INTEGER_DURATION = Integer.valueOf(DURATION);
    
    /**
     * The top/down planned duration of a task
     */
    public static final Integer INTEGER_TOP_DOWN_DURATION = Integer.valueOf(TOP_DOWN_DURATION);

    public static final Integer INTEGER_ATTACHMENT_ADD_HISTORY_FIELD = Integer.valueOf(ATTACHMENT_ADD_HISTORY_FIELD);

    public static final Integer INTEGER_ATTACHMENT_MODIFY_HISTORY_FIELD = Integer.valueOf(ATTACHMENT_MODIFY_HISTORY_FIELD);

    public static final Integer INTEGER_ATTACHMENT_DELETE_HISTORY_FIELD = Integer.valueOf(ATTACHMENT_DELETE_HISTORY_FIELD);

    public static final Integer INTEGER_COMMENT_MODIFY_HISTORY_FIELD = Integer.valueOf(COMMENT_MODIFY_HISTORY_FIELD);

    public static final Integer INTEGER_COMMENT_DELETE_HISTORY_FIELD = Integer.valueOf(COMMENT_DELETE_HISTORY_FIELD);

    public static final Integer INTEGER_COST_HISTORY = Integer.valueOf(COST_HISTORY);
    public static final Integer INTEGER_PLAN_HISTORY = Integer.valueOf(PLAN_HISTORY);
    public static final Integer INTEGER_BUDGET_HISTORY = Integer.valueOf(BUDGET_HISTORY);
    public static final Integer INTEGER_VERSION_CONTROL = Integer.valueOf(VERSION_CONTROL);

    private static final int[] SYSTEM_FIELDS_ARRAY =
        new int[] {
        PROJECT,
        ISSUETYPE,
        STATE,
        MANAGER,
        RESPONSIBLE,
        RELEASENOTICED,
        RELEASESCHEDULED,
        PRIORITY,
        SEVERITY,
        ISSUENO,
        ORIGINATOR,
        CREATEDATE,
        LASTMODIFIEDDATE,
        SUPERIORWORKITEM,
        CHANGEDBY,
        SYNOPSIS,
        BUILD,
        STARTDATE,
        ENDDATE,
        DESCRIPTION,
        ACCESSLEVEL,
        ARCHIVELEVEL,
        COMMENT,
        SUBMITTEREMAIL,
        PROJECT_SPECIFIC_ISSUENO,
        TOP_DOWN_START_DATE,
        TOP_DOWN_END_DATE,
        TASK_IS_MILESTONE,
        DURATION,
        TOP_DOWN_DURATION
    };
    
    public static int[] getSystemFieldsArray() {
        return SYSTEM_FIELDS_ARRAY;
    }

    //used in dropdowns to do not have separate lookup map for
    //Manager, Responsible, Originator, User Picker
    //but one single common map for Persons
    public static final int PERSON = MANAGER;

    public static final Integer INTEGER_PERSON = Integer.valueOf(PERSON);
    //used in dropdowns to do not have separate lookup map for
    //ReleaseNoticed and ReleaseScheduled (evtl. Release Picker)
    //but one single common map for Releases
    public static final int RELEASE = RELEASESCHEDULED;

    public static final Integer INTEGER_RELEASE = Integer.valueOf(RELEASE);
}
