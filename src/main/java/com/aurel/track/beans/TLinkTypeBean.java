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


package com.aurel.track.beans;

import java.io.Serializable;

import com.aurel.track.linkType.ILinkType.FILTER_EXPRESSION_HIERARCHICAL;
import com.aurel.track.linkType.ILinkType.LINK_DIRECTION;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TLinkTypeBean
    extends com.aurel.track.beans.base.BaseTLinkTypeBean
    implements ILabelBean, Serializable
{	
	private static final long serialVersionUID = 1L;

	public String getFilterLinkExpression(Integer linkFilterSupersetExpression) {
		switch (linkFilterSupersetExpression.intValue()) {		
		case FILTER_EXPRESSION_HIERARCHICAL.LEFT_TO_RIGHT_FIRST_LEVEL:
			return getLeftToRightFirst(); 
		case FILTER_EXPRESSION_HIERARCHICAL.LEFT_TO_RIGHT_LEVEL_X:
			return getLeftToRightLevel();	
		case FILTER_EXPRESSION_HIERARCHICAL.LEFT_TO_RIGHT_ALL:
			return getLeftToRightAll();	
		case FILTER_EXPRESSION_HIERARCHICAL.RIGHT_TO_LEFT_FIRST_LEVEL:
			return getRightToLeftFirst();
		case FILTER_EXPRESSION_HIERARCHICAL.RIGHT_TO_LEFT_LEVEL_X:
			return getRightToLeftLevel();
		case FILTER_EXPRESSION_HIERARCHICAL.RIGHT_TO_LEFT_ALL:
			return getRightToLeftAll();		
		}
		return "";
	}
	
	public String getLinkName(Integer linkDirection) {
		switch (linkDirection.intValue()) {		
		case LINK_DIRECTION.LEFT_TO_RIGHT:
			return getName(); 
		case LINK_DIRECTION.RIGHT_TO_LEFT:
			return getReverseName();					
		}
		return "";
	}

	@Override
	public String getLabel() {
		return getName();
	}
}
