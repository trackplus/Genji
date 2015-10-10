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

package com.trackplus.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TPROJECTREPORTREPOSITORY database table.
 * 
 */
@Entity
@Table(name="TPROJECTREPORTREPOSITORY")
@TableGenerator(name="TPROJECTREPORTREPOSITORY_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_PROJECTREPORTREPOSITORY, allocationSize = 10)
public class Tprojectreportrepository extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TPROJECTREPORTREPOSITORY_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int pkey;

	@Column(nullable=false, length=100)
	private String name;

	@Lob
	@Column(nullable=false)
	private String query;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tproject
	@ManyToOne
	@JoinColumn(name="PROJECT", nullable=false)
	private Tproject tproject;

	public Tprojectreportrepository() {
	}

	public int getObjectid() {
		return this.pkey;
	}
	
	public void setObjectid(int key) {
		this.pkey = key;
	}

	public int getPkey() {
		return this.pkey;
	}

	public void setPkey(int pkey) {
		this.pkey = pkey;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuery() {
		return this.query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tproject getTproject() {
		return this.tproject;
	}

	public void setTproject(Tproject tproject) {
		this.tproject = tproject;
	}

}
