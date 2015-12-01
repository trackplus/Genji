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

import com.aurel.track.beans.ISerializableLabelBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to serialize entity beans. Is a wrapper over ISerializableLabelBean
 * @author  Adrian Bojani
 * @since 4.0.0
 * */
public class EntityContext {
	/**
	 * Name of the entity. This name identify the entity
	 */
	protected String name;
	/**
	 * The serializable entity
	 */
	protected ISerializableLabelBean serializableLabelBeans;
	/**
	 * relations of the entities
	 */
	protected List<EntityRelation> relations = new ArrayList<EntityRelation>();
	/**
	 * relations of the entities
	 */
	protected List<EntityDependency> dependencies = new ArrayList<EntityDependency>();
	
	public  EntityContext(){
	}
	public EntityContext(String name, ISerializableLabelBean serializableLabelBeans) {
		this(name, serializableLabelBeans,new ArrayList<EntityRelation>(), new ArrayList<EntityDependency>());
	}
	public EntityContext(String name,ISerializableLabelBean serializableLabelBeans, List<EntityRelation> relations, ArrayList<EntityDependency> dependencies ) {
		this.name = name;
		this.serializableLabelBeans = serializableLabelBeans;
		this.relations = relations;
		this.dependencies = dependencies;
	}
	
	public void addRelation(EntityRelation relation){
		if(relations==null){
			relations=new ArrayList<EntityRelation>();
		}
		relations.add(relation);
	}
	
	public void addDependecy(EntityDependency dependecy){
		if(dependencies==null){
			dependencies=new ArrayList<EntityDependency>();
		}
		dependencies.add(dependecy);
	}	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ISerializableLabelBean getSerializableLabelBeans() {
		return serializableLabelBeans;
	}

	public void setSerializableLabelBeans(ISerializableLabelBean serializableLabelBeans) {
		this.serializableLabelBeans = serializableLabelBeans;
	}

	public List<EntityRelation> getRelations() {
		return relations;
	}

	public void setRelations(List<EntityRelation> relations) {
		if(relations==null){
			relations=new ArrayList<EntityRelation>();
		}
		this.relations = relations;
	}

	public List<EntityDependency> getDependencies() {
		return dependencies;
	}
	
	public void setDependencies(List<EntityDependency> dependencies) {
		if(dependencies==null){
			dependencies=new ArrayList<EntityDependency>();
		}
		this.dependencies = dependencies;
	}
	
}
