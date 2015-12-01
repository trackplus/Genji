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


package com.aurel.track.struts2.converter;

import java.util.Map;

import ognl.DefaultTypeConverter;

import com.aurel.track.fieldType.constants.BooleanFields;

public class BooleanConverter extends DefaultTypeConverter {
	
	

	

	@Override
	public Object convertValue(Map ctx, Object o, Class toType) {
		if (toType == String.class) {
			//after submit
			//this branch is called only when the checkbox is checked
			//why????
			if (o==null) {
				return null;
			}
			String isDefault = ((String[])o)[0];
			if (isDefault==null || Boolean.FALSE.toString().equals(isDefault)) {
				return BooleanFields.FALSE_VALUE;
			} else {
				return BooleanFields.TRUE_VALUE;
			}		
		} else if (toType == Boolean.class) {
			//by rendering
			String isDefault = (String)o;
			if (BooleanFields.TRUE_VALUE.equals(isDefault)) {
				return new Boolean(true);
			} else {
				return new Boolean(false);
			}
		}
		return null;
	}
	
}
