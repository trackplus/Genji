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
import java.util.List;


/**
 * The persistent class for the TWORKFLOWDEF database table.
 * 
 */
@Entity
@Table(name="TWORKFLOWDEF")
@TableGenerator(name="TWORKFLOWDEF_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_WORKFLOWDEF, allocationSize = 10)
public class Tworkflowdef extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TWORKFLOWDEF_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=255)
	private String description;

	@Column(nullable=false, length=255)
	private String name;

	@Column(length=255)
	private String taglabel;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tworkflowconnect
	@OneToMany(mappedBy="tworkflowdef")
	private List<Tworkflowconnect> tworkflowconnects;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="OWNER")
	private Tperson tperson;

	//bi-directional many-to-one association to Tworkflowstation
	@OneToMany(mappedBy="tworkflowdef")
	private List<Tworkflowstation> tworkflowstations;

	//bi-directional many-to-one association to Tworkflowtransition
	@OneToMany(mappedBy="tworkflowdef")
	private List<Tworkflowtransition> tworkflowtransitions;

	public Tworkflowdef() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaglabel() {
		return this.taglabel;
	}

	public void setTaglabel(String taglabel) {
		this.taglabel = taglabel;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public List<Tworkflowconnect> getTworkflowconnects() {
		return this.tworkflowconnects;
	}

	public void setTworkflowconnects(List<Tworkflowconnect> tworkflowconnects) {
		this.tworkflowconnects = tworkflowconnects;
	}

	public Tworkflowconnect addTworkflowconnect(Tworkflowconnect tworkflowconnect) {
		getTworkflowconnects().add(tworkflowconnect);
		tworkflowconnect.setTworkflowdef(this);

		return tworkflowconnect;
	}

	public Tworkflowconnect removeTworkflowconnect(Tworkflowconnect tworkflowconnect) {
		getTworkflowconnects().remove(tworkflowconnect);
		tworkflowconnect.setTworkflowdef(null);

		return tworkflowconnect;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

	public List<Tworkflowstation> getTworkflowstations() {
		return this.tworkflowstations;
	}

	public void setTworkflowstations(List<Tworkflowstation> tworkflowstations) {
		this.tworkflowstations = tworkflowstations;
	}

	public Tworkflowstation addTworkflowstation(Tworkflowstation tworkflowstation) {
		getTworkflowstations().add(tworkflowstation);
		tworkflowstation.setTworkflowdef(this);

		return tworkflowstation;
	}

	public Tworkflowstation removeTworkflowstation(Tworkflowstation tworkflowstation) {
		getTworkflowstations().remove(tworkflowstation);
		tworkflowstation.setTworkflowdef(null);

		return tworkflowstation;
	}

	public List<Tworkflowtransition> getTworkflowtransitions() {
		return this.tworkflowtransitions;
	}

	public void setTworkflowtransitions(List<Tworkflowtransition> tworkflowtransitions) {
		this.tworkflowtransitions = tworkflowtransitions;
	}

	public Tworkflowtransition addTworkflowtransition(Tworkflowtransition tworkflowtransition) {
		getTworkflowtransitions().add(tworkflowtransition);
		tworkflowtransition.setTworkflowdef(this);

		return tworkflowtransition;
	}

	public Tworkflowtransition removeTworkflowtransition(Tworkflowtransition tworkflowtransition) {
		getTworkflowtransitions().remove(tworkflowtransition);
		tworkflowtransition.setTworkflowdef(null);

		return tworkflowtransition;
	}

}
