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
 * The persistent class for the TDASHBOARDPARAMETER database table.
 * 
 */
@Entity
@Table(name="TDASHBOARDPARAMETER")
@TableGenerator(name="TDASHBOARDPARAMETER_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_DASHBOARDPARAMETER, allocationSize = 10)

public class Tdashboardparameter extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TDASHBOARDPARAMETER_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(nullable=false, length=255)
	private String name;

	@Lob
	private String paramvalue;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tdashboardfield
	@ManyToOne
	@JoinColumn(name="DASHBOARDFIELD", nullable=false)
	private Tdashboardfield tdashboardfield;

	public Tdashboardparameter() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParamvalue() {
		return this.paramvalue;
	}

	public void setParamvalue(String paramvalue) {
		this.paramvalue = paramvalue;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tdashboardfield getTdashboardfield() {
		return this.tdashboardfield;
	}

	public void setTdashboardfield(Tdashboardfield tdashboardfield) {
		this.tdashboardfield = tdashboardfield;
	}

}
