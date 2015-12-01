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

package com.aurel.track.configExchange.exporter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to serialize relations between entities
 * @author Adrian Bojani
 * @since 4.0.0
 */
public class EntityRelation {
	private String parentAttributeName;
	private List<EntityContext> entities = new ArrayList<EntityContext>();

	public EntityRelation(){
	}
	public EntityRelation(String parentAttributeName){
		this(parentAttributeName,new ArrayList<EntityContext>());
	}
	public EntityRelation(String parentAttributeName, List<EntityContext> entities) {
		this.parentAttributeName = parentAttributeName;
		this.entities = entities;
	}

	public String getParentAttributeName() {
		return parentAttributeName;
	}

	public void setParentAttributeName(String parentAttributeName) {
		this.parentAttributeName = parentAttributeName;
	}

	public List<EntityContext> getEntities() {
		return entities;
	}

	public void setEntities(List<EntityContext> entities) {
		this.entities = entities;
	}
}
