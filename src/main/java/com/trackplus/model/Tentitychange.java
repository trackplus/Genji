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

package com.trackplus.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TENTITYCHANGES database table.
 * 
 */
@Entity
@Table(name="TENTITYCHANGES")
@TableGenerator(name="TENTITYCHANGES_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_ENTITYCHANGES, allocationSize = 10)

public class Tentitychange extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TENTITYCHANGES_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	private int clusternode;

	@Column(nullable=false)
	private int entitykey;

	@Column(nullable=false)
	private int entitytype;

	public Tentitychange() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public int getClusternode() {
		return this.clusternode;
	}

	public void setClusternode(int clusternode) {
		this.clusternode = clusternode;
	}

	public int getEntitykey() {
		return this.entitykey;
	}

	public void setEntitykey(int entitykey) {
		this.entitykey = entitykey;
	}

	public int getEntitytype() {
		return this.entitytype;
	}

	public void setEntitytype(int entitytype) {
		this.entitytype = entitytype;
	}

}
