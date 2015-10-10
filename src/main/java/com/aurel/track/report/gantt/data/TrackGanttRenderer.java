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


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleEdge;

/**
 * A renderer for Track Plus Gantt charts.
 * This renderer is designed to draw TrackTaskSeriesCollections.
 * It draws milestones and supertasks with their appropriate shapes instead 
 * of using simple bars.
 *
 * @author  C.Braeuchle
 */
public class TrackGanttRenderer extends GanttRenderer {
        
        
	    private static final long serialVersionUID = -7981872201205051326L;

		private static final Logger log = LogManager.getLogger (TrackGanttRenderer.class);
        
        /** Rendering information for milestones */
        private ItemRenderingInfo msInfo = null;
        
        /** Rendering information for supertasks */
        private ItemRenderingInfo stInfo = null;
        
        /** Rendering information for normal tasks */
        private ItemRenderingInfo tsInfo = null;
        
        private int width = 960;
        
        
        // X-Positions where Bars resp. Labels start resp. end
        
        private int startBars = 0;
        private int stopBars = 0;
        
        private int startLabels = 0;
        private int stopLabels = 0;
        
        Locale locale = Locale.getDefault ();
        
        public int getStartBars (){
                return startBars;
        }
        
        public int getStartLabels (){
                return startLabels;
        }
        
        public int getStopBars (){
                return stopBars;
        }
        
        public int getStopLabels (){
                return stopLabels;
        }
        
        
        /** Creates a new instance of TrackGanttRenderer */
        public TrackGanttRenderer ( ItemRenderingInfo milestoneInfo, 
                                    ItemRenderingInfo supertaskInfo, 
                                    ItemRenderingInfo taskInfo, 
                                    Locale locale ) {
                
                super ();
                
                msInfo = milestoneInfo;
                stInfo = supertaskInfo;
                tsInfo = taskInfo;
                
                this.locale = locale;
                
        }
        
        
        
        /**
         * Draws the bar for a single (series, category) data item.
         *
         * @param g2  the graphics device.
         * @param state  the renderer state.
         * @param dataArea  the data area.
         * @param plot  the plot.
         * @param domainAxis  the domain axis.
         * @param rangeAxis  the range axis.
         * @param dataset  the dataset.
         * @param row  the row index (zero-based).
         * @param column  the column index (zero-based).
         */
        @Override
		public void drawItem (Graphics2D g2,
        CategoryItemRendererState state,
        Rectangle2D dataArea,
        CategoryPlot plot,
        CategoryAxis domainAxis,
        ValueAxis rangeAxis,
        CategoryDataset dataset,
        int row,
        int column,
        int pass) {
                
                if (dataset instanceof TrackTaskSeriesCollection) {
                        
                        log.debug ("Drawing Item in TrackTaskSeriesCollection.");
                        TrackTaskSeriesCollection ttsc = (TrackTaskSeriesCollection) dataset;
                        drawTasks (g2, state, dataArea, plot, domainAxis, rangeAxis, ttsc, row, column, pass);
                }
                else {  // let the superclass handle it...
                        
                        log.debug ("Drawing item in CategoryDataset.");
                        super.drawItem (g2, state, dataArea, plot, domainAxis, 
                                        rangeAxis, dataset, row, column, 0);
                }
                
        }
        
        
        /**
         * Draws the tasks/subtasks for one item.
         *
         * @param g2  the graphics device.
         * @param state  the renderer state.
         * @param dataArea  the data plot area.
         * @param plot  the plot.
         * @param domainAxis  the domain axis.
         * @param rangeAxis  the range axis.
         * @param dataset  the data.
         * @param row  the row index (zero-based).
         * @param column  the column index (zero-based).
         */
        protected void drawTasks (Graphics2D g2,
        CategoryItemRendererState state,
        Rectangle2D dataArea,
        CategoryPlot plot,
        CategoryAxis domainAxis,
        ValueAxis rangeAxis,
        TrackTaskSeriesCollection dataset,
        int row,
        int column,
        int pass) {
                
                int count = dataset.getSubIntervalCount (row, column);
                if (count == 0) {
                        drawTask (g2, state, dataArea, plot, domainAxis, 
                                  rangeAxis, dataset, row, column, pass);
                }
                // rendering for subtasks is actually obsolete since we have a tree structure and 
                // do not use subtasks in the class Task (JFreechart Task)
                else{ // just in case... we let the superclass handle it...
                        super.drawTasks (g2, state, dataArea, plot, domainAxis, 
                                         rangeAxis, dataset, row, column);
                }
        }
        
        /**
         * Draws a single task.
         *
         * @param g2  the graphics device.
         * @param state  the renderer state.
         * @param dataArea  the data plot area.
         * @param plot  the plot.
         * @param domainAxis  the domain axis.
         * @param rangeAxis  the range axis.
         * @param dataset  the data.
         * @param row  the row index (zero-based).
         * @param column  the column index (zero-based).
         */
        protected void drawTask (Graphics2D g2,
        CategoryItemRendererState state,
        Rectangle2D dataArea,
        CategoryPlot plot,
        CategoryAxis domainAxis,
        ValueAxis rangeAxis,
        TrackTaskSeriesCollection dataset,
        int row,
        int column,
        int pass) {
                
                log.debug ("Working on item at position - column: " 
                            + column + " row: " + row);
                
                int seriesCount = getRowCount ();
                int categoryCount = getColumnCount ();
                
                
                RectangleEdge domainAxisLocation = plot.getDomainAxisEdge ();
                RectangleEdge rangeAxisLocation = plot.getRangeAxisEdge ();
                
                // Y0
                Number value0 = dataset.getEndValue (row, column);
                if (value0 == null) {
                        return;
                }
                double java2dValue0 = rangeAxis.valueToJava2D (
                value0.doubleValue (), dataArea, rangeAxisLocation
                );
                
                // Y1
                Number value1 = dataset.getStartValue (row, column);
                if (value1 == null) {
                        return;
                }
                double java2dValue1 = rangeAxis.valueToJava2D (
                value1.doubleValue (), dataArea, rangeAxisLocation
                );
                
                if (java2dValue1 < java2dValue0) {
                        double temp = java2dValue1;
                        java2dValue1 = java2dValue0;
                        java2dValue0 = temp;
                        Number tempNum = value1;
                        value1 = value0;
                        value0 = tempNum;
                }
                
                double rectStart = domainAxis.getCategoryStart (column, 
                                                       getColumnCount (), 
                                                       dataArea,
                                                       domainAxisLocation);
                
                // BREADTH
                double rectBreadth = state.getBarWidth ();
                
                
                // BAR HEIGHT
                double rectLength = Math.abs (java2dValue1 - java2dValue0);
                
                Rectangle2D bar = null;
                
                if (seriesCount > 1) {
                        double seriesGap = dataArea.getHeight () * getItemMargin ()
                        / (categoryCount * (seriesCount - 1));
                        rectStart = rectStart + row * (state.getBarWidth () 
                                                       + seriesGap);
                }
                else {
                        rectStart = rectStart + row * state.getBarWidth ();
                }
                
                // correct the start/stop Bars point
                if(startBars > (int) ( java2dValue1 - rectLength)){
                        this.startBars = (int) (java2dValue1 - rectLength);
                }
                
                if( stopBars < (int)  java2dValue1){
                        stopBars = (int) java2dValue1;
                }
                
                
                // The coordinates for the bar labels
                int startLabelX = 0;
                int startLabelY = 0;
                
                int endLabelX = 0;
                int endLabelY = 0;
                
                int barLabelX = 0;
                int barLabelY = 0;
                
                Polygon outer = null;
                Polygon inner = null;
                
                boolean isMilestone = dataset.isMilestone (column, row);
                boolean isSupertask = dataset.isSupertask (column, row);
                
                
                if(isMilestone){
                        
                        // The start point for the end date label
                        endLabelX = (int) (java2dValue1 + rectBreadth/2 );
                        endLabelY = (int) (rectStart + (3 * rectBreadth/4) );
                        
                        barLabelX = (int) (java2dValue1 - rectBreadth/2 );
                        barLabelY = (int) (rectStart + (3 * rectBreadth/4) );
                        
                        int breadth = (int) rectBreadth;
                        
                        int centerX = (int) java2dValue1;
                        
                        int centerY = (int) (rectStart + rectBreadth / 2);
                        
                        int[] xPoints = {centerX, centerX - breadth / 2, 
                                         centerX, centerX + breadth / 2};
                        int[] yPoints = {centerY - breadth / 2, centerY, 
                                         centerY + breadth / 2, centerY};
                        
                        outer = new Polygon (xPoints, yPoints , 4);
                        
                        //calculate the difference from the ratio
                        int diff = (int) ( rectBreadth / (2 * msInfo.getRatio () ) );
                        
                        int[] xPointsI = {centerX, centerX - breadth / 2 
                                          +  diff , 
                                          centerX, centerX + breadth / 2 
                                          - diff};
                        int[] yPointsI = {centerY - breadth / 2 + diff, 
                                          centerY, centerY + breadth / 2 
                                          - diff, centerY};
                        
                        inner = new Polygon (xPointsI, yPointsI , 4);
                        
                }
                
                else if ( isSupertask ){
                        
                        
                        // The start point for the end date label
                        startLabelX = (int) (java2dValue1 - rectLength - rectBreadth / 2);
                        startLabelY = (int) ( rectStart + rectBreadth * 3 / 4 );
                        
                        endLabelX = (int) ( java2dValue1 + rectBreadth / 2 );
                        endLabelY = (int) ( rectStart + rectBreadth * 3 / 4 );
                        
                        barLabelX = (int) (java2dValue1 - rectLength + rectBreadth / 3 );
                        barLabelY = (int) ( rectStart + rectBreadth);
                        
                        int breadth = (int) rectBreadth;
                        int length = (int) rectLength;
                        
                        int startX = (int) (java2dValue1 - rectLength);
                        int startY = (int) rectStart;
                        
                        int[] xPoints = {
                                startX - breadth/2,
                                startX + length + breadth/2,
                                startX + length + breadth/2,
                                startX + length,
                                startX + length - breadth/2,
                                startX + breadth/2,
                                startX,   startX  - breadth/2
                        };
                        int[] yPoints = {
                                startY,
                                startY,
                                startY + breadth/2,
                                startY + breadth,
                                startY + breadth/2,
                                startY + breadth/2,
                                startY + breadth,
                                startY + breadth/2
                        };
                        
                        outer = new Polygon (xPoints, yPoints , 8);
                        
                        //calculate the difference from the ratio
                        int diff = (int) ( rectBreadth / (2 * stInfo.getRatio ()));
                        
                        int[] xPointsI = {
                                startX - breadth/2 + diff ,
                                startX + length + breadth/2 - diff ,
                                startX + length + breadth/2 - diff ,
                                startX + length,
                                startX + length - breadth/2 + diff ,
                                startX + length - breadth/2 + diff ,
                                startX + breadth/2 - diff,
                                startX + breadth/2 - diff,
                                startX,
                                startX - breadth/2 + diff
                        };
                        int[] yPointsI = {
                                startY + diff,
                                startY + diff,
                                startY + breadth/2,
                                startY + breadth/2 + diff,
                                startY + breadth/2,
                                startY + breadth/2  - diff,
                                startY + breadth/2 - diff,
                                startY + breadth/2,
                                startY + breadth/2 + diff,
                                startY + breadth/2
                        };
                        
                        inner = new Polygon (xPointsI, yPointsI , 10);
                }
                else{
                        
                    	rectBreadth = rectBreadth/2.0;
                        // The start point for the end date label
                        startLabelX = (int) (java2dValue1 - rectLength);
                        startLabelY = (int) ( rectStart + rectBreadth * 3 / 4 );
                        
                        endLabelX = (int) java2dValue1;
                        endLabelY = (int) ( rectStart + rectBreadth * 3 / 4 );
                        
                        barLabelX = (int) (java2dValue1 - rectLength );
                        barLabelY = (int) ( rectStart ); //+ rectBreadth * 4 / 3 );
                        
                        
                        bar = new Rectangle2D.Double (java2dValue0, rectStart, 
                                                      rectLength, rectBreadth);
                        // percent complete could be obtained in the 
                        // ReportBeans, set in the ReportBean and set in the Item by the GanttGenerator
                        // currently this is not relevant.
                        Rectangle2D completeBar = null;
                        Rectangle2D incompleteBar = null;
                        Number percent = dataset.getPercentComplete (row, column);
                        double start = getStartPercent ();
                        double end = getEndPercent ();
                        if (percent != null) {
                                double p = percent.doubleValue ();
                                completeBar = new Rectangle2D.Double (
                                java2dValue0,
                                rectStart + start * rectBreadth,
                                rectLength * p,
                                rectBreadth * (end - start)
                                );
                                incompleteBar = new Rectangle2D.Double (
                                java2dValue0 + rectLength * p,
                                rectStart + start * rectBreadth,
                                rectLength * (1 - p),
                                rectBreadth * (end - start)
                                );
                        }
                        
                        /** This is for displaying e.g. (german Soll und Ist Termine). This implementation is designed to display only one Series.
                         * If you need Soll und Ist, you can generate two gantt charts.
                         * --->> Paint seriesPaint = getItemPaint (row, column);
                         */
                        
                        Paint seriesPaint = tsInfo.getInnerColor ();
                        
                        g2.setPaint (seriesPaint);
                        g2.fill (bar);
                        
                        if(tsInfo.isDisplayIDLabel () && (! tsInfo.isDisplaySynopsis ()) ){
                                // draw the tasks ID
                                drawLabel ( new Integer ( dataset.getItem (column, row).getId () ).toString () , g2, barLabelX, barLabelY, tsInfo.getLabelFontID (), false, (int) rectLength);
                        }
                        
                        else if( tsInfo.isDisplaySynopsis ()) {
                                if(tsInfo.isDisplayDateLabel () ){
                                        
                                        // format start date to the requested format using the current locale
                                        String label = ( new SimpleDateFormat ( tsInfo.getDateLabelFormat (), locale) )
                                        .format ( dataset.getItem (column, row).getStart () );
                                        
                                        label = dataset.getItem (column, row).getDescription () + " - " + label;
                                        drawLabel ( label, g2, barLabelX, barLabelY, tsInfo.getLabelFont (), true );
                                        
                                        
                                        // format end date to the requested format using the current locale
                                        label = ( new SimpleDateFormat ( tsInfo.getDateLabelFormat (), locale) )
                                        .format ( dataset.getItem (column, row).getStop () );
                                        
                                        // draw the supertasks end date
                                        drawLabel ( label, g2, endLabelX, endLabelY, tsInfo.getLabelFont (), false);
                                        
                                }
                                else{
                                        drawLabel ( dataset.getItem (column, row).getDescription (), g2, startLabelX, startLabelY, tsInfo.getLabelFont (), true );
                                }
                        }
                        
                        if(tsInfo.isDisplayDateLabel () && (!tsInfo.isDisplaySynopsis ())){
                                
                                // format start date to the requested format using the current locale
                                String label = ( new SimpleDateFormat ( tsInfo.getDateLabelFormat (), locale) )
                                .format ( dataset.getItem (column, row).getStart () );
                                
                                // draw the tasks start date
                                drawLabel ( label, g2, startLabelX, startLabelY, tsInfo.getLabelFont (), true);
                                
                                // format end date to the requested format using the current locale
                                label = ( new SimpleDateFormat ( tsInfo.getDateLabelFormat (), locale) )
                                .format ( dataset.getItem (column, row).getStop () );
                                // draw the tasks end date
                                drawLabel ( label, g2, endLabelX, endLabelY, tsInfo.getLabelFont (), false);
                        }
                        
                        if (completeBar != null) {
                                g2.setPaint (getCompletePaint ());
                                g2.fill (completeBar);
                        }
                        if (incompleteBar != null) {
                                g2.setPaint (getIncompletePaint ());
                                g2.fill (incompleteBar);
                        }
                        
                        // draw the outline...
                        if (state.getBarWidth () > BAR_OUTLINE_WIDTH_THRESHOLD) {
                                
                                
                                Stroke stroke = getItemOutlineStroke (row, column);
                                Paint paint = getItemOutlinePaint (row, column);
                                if (stroke != null && paint != null) {
                                        g2.setStroke (stroke);
                                        g2.setPaint (paint);
                                        
                                        g2.draw (bar);
                                }
                        }
                }
                
                // Draw milestone or Supertask
                if ( isMilestone ) {
                        
                        
                        g2.setPaint (msInfo.getOuterColor ());
                        g2.fill (outer);
                        g2.draw (outer);
                        
                        g2.setPaint (msInfo.getInnerColor ());
                        g2.fill (inner);
                        g2.draw (inner);
                        
                        Stroke stroke = getItemOutlineStroke (row, column);
                        Paint paint = getItemOutlinePaint (row, column);
                        if (stroke != null && paint != null) {
                                g2.setStroke (stroke);
                                g2.setPaint (paint);
                                g2.draw (outer);
                        }
                        
                        // draw the milestones due date
                        if(msInfo.isDisplayDateLabel ()){
                                
                                // format milestone date to the requested format using the current locale
                                String label = ( new SimpleDateFormat ( msInfo.getDateLabelFormat (), locale) )
                                .format ( dataset.getItem (column, row).getStart () );
                                
                                drawLabel ( label, g2, endLabelX, endLabelY, msInfo.getLabelFont (), false);
                        }
                        
                        if(msInfo.isDisplayIDLabel () && (! msInfo.isDisplaySynopsis ())){
                                // for milestones the ID is aligned at the left side.
                                drawLabel ( new Integer ( dataset.getItem (column, row).getId () ).toString () , g2, barLabelX, barLabelY, msInfo.getLabelFontID (), true, (int) rectLength );
                        }
                        else if ( msInfo.isDisplaySynopsis ()){
                                drawLabel ( dataset.getItem (column, row).getDescription () , g2, barLabelX, barLabelY, msInfo.getLabelFont (), true, (int) rectLength );
                        }
                        
                }
                else if ( isSupertask ) {
                        
                        
                        g2.setPaint (stInfo.getOuterColor ());
                        g2.fill (outer);
                        g2.draw (outer);
                        
                        g2.setPaint (stInfo.getInnerColor ());
                        g2.fill (inner);
                        g2.draw (inner);
                        
                        Stroke stroke = getItemOutlineStroke (row, column);
                        Paint paint = getItemOutlinePaint (row, column);
                        if (stroke != null && paint != null) {
                                g2.setStroke (stroke);
                                g2.setPaint (paint);
                                g2.draw (outer);
                        }
                        
                        // draw the supertasks ID
                        if(stInfo.isDisplayIDLabel ()&& (! stInfo.isDisplaySynopsis ())){
                                drawLabel ( new Integer ( dataset.getItem (column, row).getId () ).toString () , g2, barLabelX, barLabelY, stInfo.getLabelFontID (), false, (int) rectLength);
                        }
                        else if( stInfo.isDisplaySynopsis ()){
                                if(stInfo.isDisplayDateLabel () ){
                                        
                                        // format start date to the requested format using the current locale
                                        String label = ( new SimpleDateFormat ( stInfo.getDateLabelFormat (), locale) )
                                        .format ( dataset.getItem (column, row).getStart () );
                                        
                                        label = dataset.getItem (column, row).getDescription () + " - " + label;
                                        drawLabel ( label, g2, startLabelX, startLabelY, stInfo.getLabelFont (), true );
                                        
                                        
                                        // format end date to the requested format using the current locale
                                        label = ( new SimpleDateFormat ( stInfo.getDateLabelFormat (), locale) )
                                        .format ( dataset.getItem (column, row).getStop () );
                                        
                                        // draw the supertasks end date
                                        drawLabel ( label, g2, endLabelX, endLabelY, stInfo.getLabelFont (), false);
                                        
                                }
                                else{
                                        drawLabel ( dataset.getItem (column, row).getDescription (), g2, startLabelX, startLabelY, stInfo.getLabelFont (), true );
                                }
                        }
                        if(stInfo.isDisplayDateLabel () && (!tsInfo.isDisplaySynopsis ())){
                                
                                // format start date to the requested format using the current locale
                                String label = ( new SimpleDateFormat ( stInfo.getDateLabelFormat (), locale) )
                                .format ( dataset.getItem (column, row).getStart () );
                                
                                // draw the supertasks start date
                                drawLabel ( label, g2, startLabelX, startLabelY, stInfo.getLabelFont (), true);
                                
                                // format end date to the requested format using the current locale
                                label = ( new SimpleDateFormat ( stInfo.getDateLabelFormat (), locale) )
                                .format ( dataset.getItem (column, row).getStop () );
                                
                                // draw the supertasks end date
                                drawLabel ( label, g2, endLabelX, endLabelY, stInfo.getLabelFont (), false);
                        }
                }
        }
        
        
        /** Prints the specified labelstring.
         * The x coordinate specifies the startposition.
         * The y coordinate specifies the lower startposition of the label.
         * If alignRight is true, this method subtracts the length of the 
         * labelstring and 3 pixels from the x coordinate.
         * Otherwise it adds 3 pixels to the x coordinate.
         */
        private void drawLabel ( String s, Graphics2D g2, int _x, int _y, Font f, boolean alignRight) {
                
                g2.setPaint (Color.black);
                g2.setFont (f);
                
                int x = 0;
                
                if (alignRight ){
                        // subtract the length neede to print the label from the x coordinate
                        x = _x - g2.getFontMetrics ().stringWidth (s) - 3;
                }
                else{
                        x = _x + 3;
                }
                
                
                // correct the very first and very last point in the diagram.
                if( startLabels > x ){
                        startLabels = x;
                }
                
                if(stopLabels < x + g2.getFontMetrics ().stringWidth (s)){
                        stopLabels = x + g2.getFontMetrics ().stringWidth (s) ;
                }
                
                g2.drawString (s, x, _y);
                
        }
        
        
        
        /** Prints the specified labelstring. If neccessary it reduces the 
         * font size that the label fits in the specified space.
         * The x coordinate specifies the startposition.
         * The y coordinate specifies the lower startposition of the label.
         * If alignRight is true, this method subtracts the length of the 
         * labelstring and 3 pixels from the x coordinate.
         * Otherwise it adds 3 pixels to the x coordinate.
         */
        private void drawLabel ( String s, Graphics2D g2, int _x, int _y, Font f, boolean alignRight, int space ) {
                
                g2.setPaint (Color.black);
                g2.setFont (f);
                
                int x = 0;
                
                if (alignRight ){
                        // subtract the length neede to print the label from the x coordinate
                        x = _x - g2.getFontMetrics ().stringWidth (s) - 3;
                }
                else{
                        
                        while ( g2.getFontMetrics ().stringWidth (s) > space - 4 ){
                                
                                log.debug ("Reducing Font size for label " + s);
                                f = new Font (f.getName (), f.getStyle (), f.getSize () - 1);
                                g2.setFont (f);
                                
                        }
                        
                        x = _x + 2;
                }
                
                g2.drawString (s, x, _y);
                
        }
        
}
