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
 * The persistent class for the TCONFIGOPTIONSROLE database table.
 * 
 */
@Entity
@Table(name="TCONFIGOPTIONSROLE")
@TableGenerator(name="TCONFIGOPTIONSROLE_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_CONFIGOPTIONSROLE, allocationSize = 10)


public class Tconfigoptionsrole extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TCONFIGOPTIONSROLE_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tfieldconfig
	@ManyToOne
	@JoinColumn(name="CONFIGKEY", nullable=false)
	private Tfieldconfig tfieldconfig;

	//bi-directional many-to-one association to Trole
	@ManyToOne
	@JoinColumn(name="ROLEKEY", nullable=false)
	private Trole trole;

	//bi-directional many-to-one association to Toption
	@ManyToOne
	@JoinColumn(name="OPTIONKEY", nullable=false)
	private Toption toption;

	public Tconfigoptionsrole() {
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

	public Tfieldconfig getTfieldconfig() {
		return this.tfieldconfig;
	}

	public void setTfieldconfig(Tfieldconfig tfieldconfig) {
		this.tfieldconfig = tfieldconfig;
	}

	public Trole getTrole() {
		return this.trole;
	}

	public void setTrole(Trole trole) {
		this.trole = trole;
	}

	public Toption getToption() {
		return this.toption;
	}

	public void setToption(Toption toption) {
		this.toption = toption;
	}

}
