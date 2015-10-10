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

/** This class stores the rendering information for Items such as font, 
 *  color and if labels should be displayed.
 *
 * @author  C.Braeuchle
 */
public class ItemRenderingInfo {
        
        /** the item type for which the rendering info is for - default: TASK*/
        int itemType = TrackTask.TASK;
        
        /** The font for labels - default: default, plain, 12 */
        private Font labelFontDate = new Font (null, Font.PLAIN, 12);
        
        /** The font for labels - default: default, plain, 12 */
        private Font labelFontID = new Font (null, Font.ITALIC | Font.BOLD , 12);
        
        /** display start/end date labels? default: true */
        private boolean displayDateLabel = true;
        
        /** display labels with the item's ID? default: true */
        private boolean displayIDLabel = true;
        
        /** display Synopsis as Label in the diagram? defaulst: false */
        private boolean displaySynopsis = false;
        
        /** The formatting pattern for start/end date labels on the bars - 
         * default: MM/dd
         */
        private String dateLabelFormat = "MM/dd";
                
        /**The Item's inner Polygon color  - default: blue*/
        private Color innerColor = Color.blue;
        
        /**The Item's outer Polygon color  - default: black*/
        private Color outerColor = Color.black;
        
        /** The ratio between the outer and inner Polygons  - default: 2*/
        private double ratio = 2;
        
        
        /** Creates a new instance of ItemRenderingInfo */
        public ItemRenderingInfo ( int itemType ) {
                this.itemType = itemType;
        }
        
        /**
         * Getter for property itemType.
         * @return Value of property itemType.
         */
        public int getItemType () {
                return itemType;
        }        
        
        /**
         * Getter for property labelFontDate.
         * @return Value of property labelFont.
         */
        public java.awt.Font getLabelFont () {
                return labelFontDate;
        }        
        
        /**
         * Setter for property labelFontDate.
         * @param labelFont New value of property labelFont.
         */
        public void setLabelFont (java.awt.Font labelFont) {
                this.labelFontDate = labelFont;
        }
        
        /**
         * Getter for property labelFontID.
         * @return Value of property labelFont.
         */
        public java.awt.Font getLabelFontID () {
                return labelFontID;
        }        
        
        /**
         * Setter for property labelFontID.
         * @param labelFont New value of property labelFont.
         */
        public void setLabelFontID (java.awt.Font labelFont) {
                this.labelFontID = labelFont;
        }
        
        /**
         * Getter for property displayDateLabel.
         * @return Value of property displayDateLabel.
         */
        public boolean isDisplayDateLabel () {
                return displayDateLabel;
        }
        
        /**
         * Setter for property displayDateLabel.
         * @param displayDateLabel New value of property displayDateLabel.
         */
        public void setDisplayDateLabel (boolean displayDateLabel) {
                this.displayDateLabel = displayDateLabel;
        }
        
        /**
         * Getter for property dateLabelFormat.
         * @return Value of property dateLabelFormat.
         */
        public java.lang.String getDateLabelFormat () {
                return dateLabelFormat;
        }
        
        /**
         * Setter for property dateLabelFormat.
         * @param dateLabelFormat New value of property dateLabelFormat.
         */
        public void setDateLabelFormat (java.lang.String dateLabelFormat) {
                this.dateLabelFormat = dateLabelFormat;
        }
        
        
        /**
         * Getter for property innerColor.
         * @return Value of property innerColor.
         */
        public java.awt.Color getInnerColor () {
                return innerColor;
        }
        
        /**
         * Setter for property innerColor.
         * @param innerColor New value of property innerColor.
         */
        public void setInnerColor (java.awt.Color innerColor) {
                this.innerColor = innerColor;
        }
        
        /**
         * Getter for property outerColor.
         * @return Value of property outerColor.
         */
        public java.awt.Color getOuterColor () {
                return outerColor;
        }
        
        /**
         * Setter for property outerColor.
         * @param outerColor New value of property outerColor.
         */
        public void setOuterColor (java.awt.Color outerColor) {
                this.outerColor = outerColor;
        }
        
        /**
         * Getter for property ratio.
         * @return Value of property ratio.
         */
        public double getRatio () {
                return ratio;
        }
        
        /**
         * Setter for property ratio.
         * @param ratio New value of property ratio.
         */
        public void setRatio (double ratio) {
                this.ratio = ratio;
        }
        
        /**
         * Getter for property displayIDLabel.
         * @return Value of property displayIDLabel.
         */
        public boolean isDisplayIDLabel () {
                return displayIDLabel;
        }
        
        /**
         * Setter for property displayIDLabel.
         * @param displayIDLabel New value of property displayIDLabel.
         */
        public void setDisplayIDLabel (boolean displayIDLabel) {
                this.displayIDLabel = displayIDLabel;
        }
        
        /**
         * Getter for property displaySynopsis.
         * @return Value of property displaySynopsis.
         */
        public boolean isDisplaySynopsis () {
                return displaySynopsis;
        }
        
        /**
         * Setter for property displaySynopsis.
         * @param displaySynopsis New value of property displaySynopsis.
         */
        public void setDisplaySynopsis (boolean displaySynopsis) {
                this.displaySynopsis = displaySynopsis;
        }
        
}
