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

package com.aurel.track.configExchange.importer;

import com.aurel.track.configExchange.exporter.jaxb.Entity;

import java.util.Map;

/**
 * a context wrapper used to save an entity
 * @author  Adrian Bojani
 */
public class EntityImportContext {
	private Entity entity;
	private Map<String, String> attributeMap;
	private boolean overrideExisting;
	private boolean overrideOnlyNotModifiedByUser;
	private boolean clearChildren;

	public EntityImportContext(){
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public Map<String, String> getAttributeMap() {
		return attributeMap;
	}

	public void setAttributeMap(Map<String, String> attributeMap) {
		this.attributeMap = attributeMap;
	}

	public boolean isClearChildren() {
		return clearChildren;
	}

	public void setClearChildren(boolean clearChildren) {
		this.clearChildren = clearChildren;
	}

	public boolean isOverrideExisting() {
		return overrideExisting;
	}

	public void setOverrideExisting(boolean overrideExisting) {
		this.overrideExisting = overrideExisting;
	}

	public boolean isOverrideOnlyNotModifiedByUser() {
		return overrideOnlyNotModifiedByUser;
	}

	public void setOverrideOnlyNotModifiedByUser(boolean overrideOnlyNotModifiedByUser) {
		this.overrideOnlyNotModifiedByUser = overrideOnlyNotModifiedByUser;
	}
}
