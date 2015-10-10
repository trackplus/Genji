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


package com.aurel.track.report.gantt.data;


import java.util.Hashtable;

import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;


/**
 *
 * @author  Christoph Braeuchle
 */
public class TrackTaskSeriesCollection extends TaskSeriesCollection{
        
	    private static final long serialVersionUID = -3539910218537330928L;
		        
        private Hashtable taskSeries = new Hashtable ();
        
        /** Creates a new instance of TrackTaskSeriesCollection */
        public TrackTaskSeriesCollection () {
                super ();
        }
        
        
        @Override
		public void add (TaskSeries tasks){
                
                super.add (tasks);
                
                // since we do not know which TrackTaskSeries is added at which row, 
                // we have to calculate the exact positions of milestones and 
                // supertasks in this method.
                
                int row = super.getSeriesCount () - 1; // row is zero based
                
                taskSeries.put (new Integer (row), tasks);
                
        }
        
        /** This method is called by the TrackGanttRenderer. It returns true if the item at the specified position is a milestone, false otherwise */
        public boolean isMilestone (int column, int row){
            
            TaskSeries ts = ( (TaskSeries) taskSeries.get ( new Integer (row) ) );
            TrackTask task = null;
            if (ts != null) {
                task = (TrackTask) ts.get ( column );
            }
            else {
                return false;
            }
            if (task != null) {
                return (task.getType() == TrackTask.MILESTONE);
            }
            else {
                return false;
            }
        }
        
        /** This method is called by the TrackGanttRenderer. It returns true if the item at the specified position is a supertask, false otherwise */
        public boolean isSupertask (int column, int row) {
            
            TaskSeries ts = ( (TaskSeries) taskSeries.get ( new Integer (row) ) );
            TrackTask task = null;
            if (ts != null) {
                task = (TrackTask) ts.get ( column );
            }
            else {
                return false;
            }
            if (task != null) {
                return (task.getType() == TrackTask.SUPERTASK);
            }
            else {
                return false;
            }
        }

        
        /** This method returns the Item at the specified position.
         * <p>
         * Note that column identifies the actual Item number in the TaskSeries and <b>not</b> the
         * TaskSeries number (TaskSeries 0, 1, etc.) as one could expect.
         * This unexpected behavior is caused by the fact that JFreeChart handles the gantt-chart as
         * a 90 degree rotated bar diagram.
         * Row identifies the TaskSeries number for the same reason as mentioned above.
         * </p>
         */
        public TrackTask getItem ( int column, int row ){
            
            TaskSeries ts = ( (TaskSeries) taskSeries.get ( new Integer (row) ) );
            TrackTask task = null;
            if (ts != null) {
                task = (TrackTask) ts.get ( column );
                return task;
            }
            else {
                return null;
            }
        }
        
        
}
