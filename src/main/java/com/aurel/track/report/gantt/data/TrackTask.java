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


package com.aurel.track.report.gantt.data;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;

/** This class represents a TrackPlus Item to be displayed in a Gantt chart.
 * Milestones and top level tasks are identified by the int attribute type.
 *
 * Subtasks are automatically sorted in chronical order where first the start 
 * date is compared and afterwards the end date.
 *
 * @author  C.Braeuchle
 * @author  J. Friedrich
 */
// To do: implements comparable
public class TrackTask extends Task implements Comparable {
        
		private static final long serialVersionUID = 340L;
		public static final int TASK = 10;
        public static final int SUPERTASK = 20;
        public static final int MILESTONE = 30;
        
        private static boolean displaySynopsis = false;
        
        /** The string to identify a task - subtask relationship in the gantt 
         *  chart's label section.
         */
        private final String RELSHIP_IDENTIFIER = "    ";
        
        private int level = 0;
        
        private int id = 0;
        
        private int type = TASK;
        
        private LinkedList children = new LinkedList ();
        
              
        /** Creates a new Item that represents a Milestone */
        public TrackTask (int id, String description, Date milestoneDate) {
                
                super (description, milestoneDate, milestoneDate);
                
                this.id = id;
                this.type = MILESTONE;
                
        }
        
        /** Creates a new Item to be rendered as normal Task */
        public TrackTask (int id, String description, Date start, Date stop) {
                
                super (description, start, stop);
                
                this.id = id;
                this.type = TASK;
                
        }
        
        
        /** Returns the type of the item. This can be a Task, Supertask or a 
         *  milestone. Use the final constants TASK, SUPERTASK and MILESTONE
         *  to validate the type.
         */
        public int getType (){
                return type;
        }
        
        public void setType(int ptype) {
            if (this.type != MILESTONE) {
                this.type = ptype;
            }
        }
        
        /** Inserts the item as subitem to the item with the specified ID.
         */
        public boolean insertTask (int parentId, TrackTask task){
                TrackTask parent = get (parentId);
                if(parent == null){
                        return false;
                }
                parent.addSubtask (task);
                return true;
        }
        
        public void addSubtask(TrackTask task) {
            if (task != null) {
                type = SUPERTASK;
                task.setLevel(level+1);
                super.addSubtask(task);
            }
        }
        
        /** adds a task as subtask/milestone to the task.
         *  If the current task is automatically set to the type Supertask
         */
        public void add (TrackTask task){
                
                // time order
                Iterator children = this.children.iterator ();
                int index = 0;
                while(children.hasNext ()){
                        switch ( ((TrackTask) children.next ()).compareTo (task) ){
                                
                                case 1:
                                        this.children.add (index, task);
                                        // set the task's level to one level higher than my level
                                        task.setLevel (level + 1);
                                        
                                        // now I'm a superman!
                                        type = SUPERTASK;
                                        return; // work is done.
                                        
                                case 0:
                                        return; // task is already in the tree - so we do not add.
                                        
                        }
                        
                        index++;
                }
                
                // task's start and end date is not before any of the task's start and end date. So we add the task at the last position.
                this.children.add (task);
                
                // set the task's level to one level higher than my level
                task.setLevel (level + 1);
                
                // now I'm a supertask
                type = SUPERTASK;               
        }
        
        /** Retrieves the task with the specified id or null if the id is 
         * neither the current tasks id nor the id of a subtask of this task.
         */
        public TrackTask get (int id){
                
                if( getId () == id ){
                        return this;
                }
                
                Iterator children = this.children.iterator ();
                TrackTask kid = null;
                while(children.hasNext ()){
                        
                        kid = (TrackTask) children.next ();
                        
                        if(kid.getId () == id){
                                return kid;
                        }
                        
                        // recurse
                        kid = kid.get (id);
                        if( kid != null){
                                
                                return kid;
                        }
                }
                
                return null;
        }
        
        /** Returns the tasks ID.
         */
        public int getId (){
                return id;
        }
        
        /** Adds the current task and all Subtasks to the specified 
         *  TaskSeries Object.
         */
        public void addToTaskSeries (TaskSeries tasks){
                
                tasks.add (this);
                
                
                Iterator children = this.children.iterator ();
                while(children.hasNext ()){
                        
                        // recurse in next level
                        ((TrackTask) children.next ()).addToTaskSeries (tasks);
                        
                }
        }
        
        /** Sets the new level for this task and calls setDescription to set 
         *  the relationship identifier String appropriately
         */
        public void setLevel (int level){
                this.level = level; // first set the new level, then the description
                setDescription (getDescription ());
        }
        
        /** This method sets a new description and makes sure that the 
         *  levelString is set correctly in the label.
         *
         */
        @Override
		public void setDescription (String description) {
                
                String newDescription = description;
                
                // if a relship identifier was set before, it is removed first.
                int start = description.indexOf (RELSHIP_IDENTIFIER);
                
                if(start != -1){
                        newDescription = description.substring (start + RELSHIP_IDENTIFIER.length () - 1);
                }
                
                // finally set the new description string with the relation ship identifier
                if ( displaySynopsis){
                        super.setDescription ( newDescription );
                }
                else{
                        super.setDescription ( getLevelString ().append ( newDescription ).toString () );
                }
                
                
        }
        
        
        /** Returns the appropriate Level String as StringBuffer for further 
         *  concatenation. This is the appropriate indentation followed by the 
         *  parent- subtask relationship identifier (e.g. "|____").
         *  If the tasks level is 0 or negative, an empty StringBuffer is 
         *  returned.
         */
        private StringBuffer getLevelString (){
                
                if(level >= 1){
                        
                        StringBuffer ret = new StringBuffer ();
                        for(int i = 0; i < (level -1); i++){
                                ret.append ("    ");
                        }
                        return ret.append (RELSHIP_IDENTIFIER);
                }
                
                else{
                        
                        return new StringBuffer ();
                }
        }
        
        public Date getStart (){
                return getDuration ().getStart ();
        }
        
        public Date getStop (){
                return getDuration ().getEnd ();
        }
        
        @Override
        public boolean equals (Object task) {
                
                return (this.getDescription ().equals (((TrackTask)task).getDescription ())
                && getStart ().equals (((TrackTask)task).getStart ())
                && getStop ().equals (((TrackTask)task).getStop ()));
        }
        
        @Override
        public int hashCode() {
        	return super.hashCode(); // tough luck  
        }
        
                
        
        
        /**
         *@ throw ClassCastException if the Object to compare is not an 
         * instance of the class task
         */
        @Override
        public int compareTo (Object o) {
                
                if ( equals ( (TrackTask) o )){
                        return 0;
                }
                
                // compare start date
                int c = super.getDuration ().getStart ().compareTo ( ( (TrackTask) o).getStart () );
                
                // my start date is before the compare date's start date
                if ( c < 0 )  {
                        return -1;
                }
                // my start date is the same as the compare date's start date
                // thus we have to compare the end dates
                else if ( c == 0 ) {
                        c = super.getDuration ().getEnd ().compareTo ( ( (TrackTask) o).getStop () );
                        if ( c < 0 )  {
                                return -1;
                        }
                        else if ( c == 0 ){
                                return this.id < ((TrackTask) o ).getId () ? -1 : 1;
                        }
                        else{
                                return 1;
                        }
                }
                // my start date is after the compare date's start date
                else{
                        return 1;
                }
        }
        
        public static void setDisplaySynopsis (boolean _displaySynopsis){
                displaySynopsis = _displaySynopsis;
        }
        
}
