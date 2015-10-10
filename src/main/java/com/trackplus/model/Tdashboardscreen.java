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
 * The persistent class for the TDASHBOARDSCREEN database table.
 * 
 */
@Entity
@Table(name="TDASHBOARDSCREEN")
@TableGenerator(name="TDASHBOARDSCREEN_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_DASHBOARDSCREEN, allocationSize = 10)

public class Tdashboardscreen extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TDASHBOARDSCREEN_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Lob
	private String description;

	private int entitytype;

	@Column(name="`LABEL`", length=255)
	private String label;

	@Column(nullable=false, length=255)
	private String name;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="PERSONPKEY")
	private Tperson tperson;

	//bi-directional many-to-one association to Tproject
	@ManyToOne
	@JoinColumn(name="PROJECT")
	private Tproject tproject;

	//bi-directional many-to-one association to Tdashboardtab
	@OneToMany(mappedBy="tdashboardscreen")
	private List<Tdashboardtab> tdashboardtabs;

	public Tdashboardscreen() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getEntitytype() {
		return this.entitytype;
	}

	public void setEntitytype(int entitytype) {
		this.entitytype = entitytype;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

	public Tproject getTproject() {
		return this.tproject;
	}

	public void setTproject(Tproject tproject) {
		this.tproject = tproject;
	}

	public List<Tdashboardtab> getTdashboardtabs() {
		return this.tdashboardtabs;
	}

	public void setTdashboardtabs(List<Tdashboardtab> tdashboardtabs) {
		this.tdashboardtabs = tdashboardtabs;
	}

	public Tdashboardtab addTdashboardtab(Tdashboardtab tdashboardtab) {
		getTdashboardtabs().add(tdashboardtab);
		tdashboardtab.setTdashboardscreen(this);

		return tdashboardtab;
	}

	public Tdashboardtab removeTdashboardtab(Tdashboardtab tdashboardtab) {
		getTdashboardtabs().remove(tdashboardtab);
		tdashboardtab.setTdashboardscreen(null);

		return tdashboardtab;
	}

}
