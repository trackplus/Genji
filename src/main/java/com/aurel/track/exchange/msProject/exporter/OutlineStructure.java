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


package com.aurel.track.exchange.msProject.exporter;

public class OutlineStructure {
	String outlineNumber = "";
	int counterInLevel = 0;
	int outlineLevel = 1;
	
	public int getAbsoluteLevel() {
		return outlineLevel;
	}	
	
	public OutlineStructure(int currentLevel){
		this.outlineNumber = "";
		this.counterInLevel = currentLevel;
	}
	
	public OutlineStructure(String outlineNumber,int currentLevel,int absoluteLevel){
		this.outlineNumber = outlineNumber;
		this.counterInLevel = currentLevel;
		this.outlineLevel = absoluteLevel;
	}
	/**
	 * returns new instance of TaskLevel with new level string
	 * @return
	 */
	public OutlineStructure newLevel() {
		outlineLevel++;
		if(!"".equals(outlineNumber)) {
			return new OutlineStructure(outlineNumber+counterInLevel+".",0,outlineLevel);
		}
		else { 
			return new OutlineStructure(counterInLevel+".",0,outlineLevel);
		}	
	}
	
	/**
	 * returns new instance of TaskLevel with new level string
	 * @return
	 */
	public OutlineStructure copy() {				
		return new OutlineStructure(outlineNumber,counterInLevel,outlineLevel);		
	}
	
	/**
	 * increase current level, which counts the number of tasks on the same level
	 */
	public void incCurrentLevel(){
		counterInLevel++;
	}
	
	public String getFullOutlineNumber(){
		return outlineNumber+counterInLevel;
	}
	
}
