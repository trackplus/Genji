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


package com.aurel.track.fieldType.bulkSetters;

import java.util.ArrayList;
import java.util.List;

/**
 * Bulk setter for string fields
 * @author Tamas
 *
 */
public class CommentBulkSetter extends RichTextBulkSetter {
	public CommentBulkSetter(Integer fieldID) {		
		super(fieldID);
		//setRichText(true);
	}
	
	@Override
	public List <Integer> getPossibleRelations(boolean required) {
		List <Integer> relations = new ArrayList<Integer>();				
		relations.add(Integer.valueOf(BulkRelations.ADD_COMMENT));		
		return relations;
	}		
}
