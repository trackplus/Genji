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


package com.aurel.track.linkType;

/**
 * Generic link type for storing the inline links from documents
 * 
 * @author Tamas Ruff
 *
 */
public class InlineLinkType extends AbstractSpecificLinkType {
	
	private static InlineLinkType instance;
	
	/**
	 * Return a FieldConfigItemFacade instance which implements the ConfigItemFacade 
	 * @return
	 */
	public static InlineLinkType getInstance(){
		if(instance==null){
			instance=new InlineLinkType();
		}
		return instance;
	}
	
	/**
	 * Whether this link type is inline 
	 * (never created explicitly, only implicitly in the background by including an item inline in a document)
	 * @return
	 */
	@Override
	public boolean isInline() {
		return true;
	}
	
	@Override
	public int getPossibleDirection() {
		return LINK_DIRECTION.RIGHT_TO_LEFT;
	}
	
	@Override
	public boolean selectableForQueryResultSuperset() {
		return true;
	}

	@Override
	public boolean isHierarchical() {
		return false;
	}
	
}
