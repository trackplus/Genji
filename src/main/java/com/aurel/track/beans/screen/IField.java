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

/**
 *
 */
public interface IField {
    Integer getObjectID();

    void setObjectID(Integer objectID);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    Integer getIndex();

    void setIndex(Integer index);

    Integer getParent();

    void setParent(Integer parent);

    Integer getColSpan();
    void setColSpan(Integer colSpan);

    Integer getRowSpan();
    void setRowSpan(Integer rowSpan);
    
    Integer getRowIndex();
    Integer getColIndex();
    void setRowIndex(Integer i);
    void setColIndex(Integer i);

	void setIconRendering(Integer iconRendering);
	Integer getIconRendering();

    void setNew(boolean b);
    boolean isNew();
	public void setUuid(String v);
	public IField cloneMe();

}
