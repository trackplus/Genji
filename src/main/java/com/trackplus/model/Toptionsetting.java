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
 * The persistent class for the TOPTIONSETTINGS database table.
 * 
 */
@Entity
@Table(name="TOPTIONSETTINGS")
@TableGenerator(name="TOPTIONSETTINGS_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_OPTIONSETTINGS, allocationSize = 10)
public class Toptionsetting extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TOPTIONSETTINGS_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=1)
	private String multiple;

	private int parametercode;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tlist
	@ManyToOne
	@JoinColumn(name="LIST", nullable=false)
	private Tlist tlist;

	//bi-directional many-to-one association to Tfieldconfig
	@ManyToOne
	@JoinColumn(name="CONFIG", nullable=false)
	private Tfieldconfig tfieldconfig;

	public Toptionsetting() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getMultiple() {
		return this.multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

	public int getParametercode() {
		return this.parametercode;
	}

	public void setParametercode(int parametercode) {
		this.parametercode = parametercode;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tlist getTlist() {
		return this.tlist;
	}

	public void setTlist(Tlist tlist) {
		this.tlist = tlist;
	}

	public Tfieldconfig getTfieldconfig() {
		return this.tfieldconfig;
	}

	public void setTfieldconfig(Tfieldconfig tfieldconfig) {
		this.tfieldconfig = tfieldconfig;
	}

}
