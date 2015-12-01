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


package com.aurel.track.beans.screen;

import java.util.List;

import com.aurel.track.screen.FieldWrapper;

/**
 */	
public interface IPanel{
	
	Integer getObjectID();

	void setObjectID(Integer objectID);

	String getName();

	void setName(String name);

	String getLabel();

	void setLabel(String label);

	String getDescription();

	void setDescription(String description);

	Integer getIndex();

	void setIndex(Integer index);

	Integer getParent();

	void setParent(Integer parent);

	Integer getRowsNo();
	void setRowsNo(Integer rows);
	Integer getColsNo();
	void setColsNo(Integer cols);
	
	void setNew(boolean b);
	boolean isNew();
	
	IField getScreenField(int row,int col);
	void setFields(List<IField> fields);
	List<IField> getFields();
	FieldWrapper[][] getFieldWrappers();
	void setFieldWrappers(FieldWrapper[][] fieldWrappers);
	public void setUuid(String v);
	public IPanel cloneMe();
}
