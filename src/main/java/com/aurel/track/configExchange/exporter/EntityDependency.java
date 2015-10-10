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



package com.aurel.track.configExchange.exporter;

public class EntityDependency {
	private boolean entityAdded;
	private String attributeName;
	private EntityContext entityContext;
	private Integer dependencyID;
	private String dependencyType;
	public EntityDependency(String dependencyType,Integer dependencyID, String attributeName) {
		this.dependencyType=dependencyType;
		this.dependencyID=dependencyID;
		this.attributeName= attributeName;
	}
	public String getAttributeName() {
		return attributeName;
	}

	public boolean isEntityAdded() {
		return entityAdded;
	}

	public void setEntityAdded(boolean entityAdded) {
		this.entityAdded = entityAdded;
	}

	public EntityContext getEntityContext() {
		return entityContext;
	}

	public void setEntityContext(EntityContext entityContext) {
		this.entityContext = entityContext;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public Integer getDependencyID() {
		return dependencyID;
	}

	public void setDependencyID(Integer dependencyID) {
		this.dependencyID = dependencyID;
	}

	public String getDependencyType() {
		return dependencyType;
	}

	public void setDependencyType(String dependencyType) {
		this.dependencyType = dependencyType;
	}
}
