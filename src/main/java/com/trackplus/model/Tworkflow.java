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
 * The persistent class for the TWORKFLOW database table.
 * 
 */
@Entity
@Table(name="TWORKFLOW")
@TableGenerator(name="TWORKFLOW_GEN", table="ID_TABLE",
pkColumnName="ID_TABLE_ID",
pkColumnValue=BaseEntity.TABLE_WORKFLOW, allocationSize = 10)
public class Tworkflow extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TWORKFLOW_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tstate
	@ManyToOne
	@JoinColumn(name="STATEFROM")
	private Tstate tstate1;

	//bi-directional many-to-one association to Tstate
	@ManyToOne
	@JoinColumn(name="STATETO")
	private Tstate tstate2;

	//bi-directional many-to-one association to Tprojecttype
	@ManyToOne
	@JoinColumn(name="PROJECTTYPE")
	private Tprojecttype tprojecttype;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="RESPONSIBLE")
	private Tperson tperson;

	//bi-directional many-to-one association to Tworkflowcategory
	@OneToMany(mappedBy="tworkflow")
	private List<Tworkflowcategory> tworkflowcategories;

	//bi-directional many-to-one association to Tworkflowrole
	@OneToMany(mappedBy="tworkflow")
	private List<Tworkflowrole> tworkflowroles;

	public Tworkflow() {
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

	public Tstate getTstate1() {
		return this.tstate1;
	}

	public void setTstate1(Tstate tstate1) {
		this.tstate1 = tstate1;
	}

	public Tstate getTstate2() {
		return this.tstate2;
	}

	public void setTstate2(Tstate tstate2) {
		this.tstate2 = tstate2;
	}

	public Tprojecttype getTprojecttype() {
		return this.tprojecttype;
	}

	public void setTprojecttype(Tprojecttype tprojecttype) {
		this.tprojecttype = tprojecttype;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

	public List<Tworkflowcategory> getTworkflowcategories() {
		return this.tworkflowcategories;
	}

	public void setTworkflowcategories(List<Tworkflowcategory> tworkflowcategories) {
		this.tworkflowcategories = tworkflowcategories;
	}

	public Tworkflowcategory addTworkflowcategory(Tworkflowcategory tworkflowcategory) {
		getTworkflowcategories().add(tworkflowcategory);
		tworkflowcategory.setTworkflow(this);

		return tworkflowcategory;
	}

	public Tworkflowcategory removeTworkflowcategory(Tworkflowcategory tworkflowcategory) {
		getTworkflowcategories().remove(tworkflowcategory);
		tworkflowcategory.setTworkflow(null);

		return tworkflowcategory;
	}

	public List<Tworkflowrole> getTworkflowroles() {
		return this.tworkflowroles;
	}

	public void setTworkflowroles(List<Tworkflowrole> tworkflowroles) {
		this.tworkflowroles = tworkflowroles;
	}

	public Tworkflowrole addTworkflowrole(Tworkflowrole tworkflowrole) {
		getTworkflowroles().add(tworkflowrole);
		tworkflowrole.setTworkflow(this);

		return tworkflowrole;
	}

	public Tworkflowrole removeTworkflowrole(Tworkflowrole tworkflowrole) {
		getTworkflowroles().remove(tworkflowrole);
		tworkflowrole.setTworkflow(null);

		return tworkflowrole;
	}

}
