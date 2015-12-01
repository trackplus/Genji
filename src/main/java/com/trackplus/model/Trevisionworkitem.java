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
 * The persistent class for the TREVISIONWORKITEMS database table.
 * 
 */
@Entity
@Table(name="TREVISIONWORKITEMS")
@TableGenerator(name="TREVISIONWORKITEMS_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_REVISIONWORKITEMS, allocationSize = 10)
public class Trevisionworkitem extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TREVISIONWORKITEMS_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=36)
	private String tpuuid;

	@Column(nullable=false)
	private int workitemkey;

	//bi-directional many-to-one association to Trevision
	@ManyToOne
	@JoinColumn(name="REVISIONKEY", nullable=false)
	private Trevision trevision;

	public Trevisionworkitem() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public int getWorkitemkey() {
		return this.workitemkey;
	}

	public void setWorkitemkey(int workitemkey) {
		this.workitemkey = workitemkey;
	}

	public Trevision getTrevision() {
		return this.trevision;
	}

	public void setTrevision(Trevision trevision) {
		this.trevision = trevision;
	}

}
