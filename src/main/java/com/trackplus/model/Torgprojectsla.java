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
 * The persistent class for the TORGPROJECTSLA database table.
 * 
 */
@Entity
@Table(name="TORGPROJECTSLA")
@TableGenerator(name="TORGPROJECTSLA_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_ORGPROJECTSLA, allocationSize = 10)
public class Torgprojectsla extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TORGPROJECTSLA_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tdepartment
	@ManyToOne
	@JoinColumn(name="DEPARTMENT", nullable=false)
	private Tdepartment tdepartment;

	//bi-directional many-to-one association to Tproject
	@ManyToOne
	@JoinColumn(name="PROJECT", nullable=false)
	private Tproject tproject;

	//bi-directional many-to-one association to Tsla
	@ManyToOne
	@JoinColumn(name="SLA", nullable=false)
	private Tsla tsla;

	public Torgprojectsla() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tdepartment getTdepartment() {
		return this.tdepartment;
	}

	public void setTdepartment(Tdepartment tdepartment) {
		this.tdepartment = tdepartment;
	}

	public Tproject getTproject() {
		return this.tproject;
	}

	public void setTproject(Tproject tproject) {
		this.tproject = tproject;
	}

	public Tsla getTsla() {
		return this.tsla;
	}

	public void setTsla(Tsla tsla) {
		this.tsla = tsla;
	}

}
