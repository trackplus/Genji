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

import java.util.List;


/**
 * The persistent class for the TDEPARTMENT database table.
 * 
 */
@Entity
@Table(name="TDEPARTMENT")
@TableGenerator(name="TDEPARTMENT_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_DEPARTMENT, allocationSize = 10)

public class Tdepartment extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TDEPARTMENT_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int pkey;

	@Column(name="`LABEL`", nullable=false, length=18)
	private String label;

	private int parent;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tcostcenter
	@ManyToOne
	@JoinColumn(name="COSTCENTER")
	private Tcostcenter tcostcenter;

	//bi-directional many-to-one association to Torgprojectsla
	@OneToMany(mappedBy="tdepartment")
	private List<Torgprojectsla> torgprojectslas;

	//bi-directional many-to-one association to Tperson
	@OneToMany(mappedBy="tdepartment")
	private List<Tperson> tpersons;

	public Tdepartment() {
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

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getParent() {
		return this.parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tcostcenter getTcostcenter() {
		return this.tcostcenter;
	}

	public void setTcostcenter(Tcostcenter tcostcenter) {
		this.tcostcenter = tcostcenter;
	}

	public List<Torgprojectsla> getTorgprojectslas() {
		return this.torgprojectslas;
	}

	public void setTorgprojectslas(List<Torgprojectsla> torgprojectslas) {
		this.torgprojectslas = torgprojectslas;
	}

	public Torgprojectsla addTorgprojectsla(Torgprojectsla torgprojectsla) {
		getTorgprojectslas().add(torgprojectsla);
		torgprojectsla.setTdepartment(this);

		return torgprojectsla;
	}

	public Torgprojectsla removeTorgprojectsla(Torgprojectsla torgprojectsla) {
		getTorgprojectslas().remove(torgprojectsla);
		torgprojectsla.setTdepartment(null);

		return torgprojectsla;
	}

	public List<Tperson> getTpersons() {
		return this.tpersons;
	}

	public void setTpersons(List<Tperson> tpersons) {
		this.tpersons = tpersons;
	}

	public Tperson addTperson(Tperson tperson) {
		getTpersons().add(tperson);
		tperson.setTdepartment(this);

		return tperson;
	}

	public Tperson removeTperson(Tperson tperson) {
		getTpersons().remove(tperson);
		tperson.setTdepartment(null);

		return tperson;
	}

}
