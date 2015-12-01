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

import com.aurel.track.beans.screen.IScreen;
import com.aurel.track.beans.screen.ITab;

import java.util.List;

/**
 *
 */
public class CardScreen implements IScreen {
	private List<ITab> tabs;
	private Integer objectID;
	private String name;
	private String description;
	private String label;
	private Integer personID;
	private String tagLabel;

	@Override
	public List<ITab> getTabs() {
		return tabs;
	}

	@Override
	public void setTabs(List<ITab> tabs) {
		this.tabs = tabs;
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
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
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
	public Integer getPersonID() {
		return personID;
	}

	@Override
	public void setPersonID(Integer personID) {
		this.personID = personID;
	}

	@Override
	public String getTagLabel() {
		return tagLabel;
	}

	@Override
	public void setTagLabel(String tagLabel) {
		this.tagLabel = tagLabel;
	}
}
