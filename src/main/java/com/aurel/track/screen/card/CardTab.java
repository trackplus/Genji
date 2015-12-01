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

package com.aurel.track.screen.card;

import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.beans.screen.ITab;

import java.util.HashSet;
import java.util.List;

/**
 */
public class CardTab implements ITab{
	private List<IPanel> panels;
	private Integer objectID;
	private String name;
	private String label;
	private String description;
	private Integer index;
	private Integer parent;
	private HashSet<String> fieldTypes;

	@Override
	public List<IPanel> getPanels() {
		return panels;
	}

	@Override
	public void setPanels(List<IPanel> panels) {
		this.panels = panels;
	}

	@Override
	public Integer getObjectID() {
		return objectID;
	}

	@Override
	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Integer getIndex() {
		return index;
	}

	@Override
	public void setIndex(Integer index) {
		this.index = index;
	}

	@Override
	public Integer getParent() {
		return parent;
	}

	@Override
	public void setParent(Integer parent) {
		this.parent = parent;
	}

	@Override
	public HashSet<String> getFieldTypes() {
		return fieldTypes;
	}

	@Override
	public void setFieldTypes(HashSet<String> fieldTypes) {
		this.fieldTypes = fieldTypes;
	}

	@Override
	public void setNew(boolean b) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void setUuid(String v) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public boolean isNew() {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public ITab cloneMe() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
