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
 * The persistent class for the TREPORTPERSONSETTINGS database table.
 * 
 */
@Entity
@Table(name="TREPORTPERSONSETTINGS")
@TableGenerator(name="TREPORTPERSONSETTINGS_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_REPORTPERSONSETTINGS, allocationSize = 10)
public class Treportpersonsetting extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TREPORTPERSONSETTINGS_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Lob
	private String paramsettings;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Treportparameter
	@OneToMany(mappedBy="treportpersonsetting")
	private List<Treportparameter> treportparameters;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="PERSON", nullable=false)
	private Tperson tperson;

	//bi-directional many-to-one association to Trecurrencepattern
	@ManyToOne
	@JoinColumn(name="RECURRENCEPATTERN")
	private Trecurrencepattern trecurrencepattern;

	//bi-directional many-to-one association to Texporttemplate
	@ManyToOne
	@JoinColumn(name="REPORTTEMPLATE", nullable=false)
	private Texporttemplate texporttemplate;

	public Treportpersonsetting() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getParamsettings() {
		return this.paramsettings;
	}

	public void setParamsettings(String paramsettings) {
		this.paramsettings = paramsettings;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public List<Treportparameter> getTreportparameters() {
		return this.treportparameters;
	}

	public void setTreportparameters(List<Treportparameter> treportparameters) {
		this.treportparameters = treportparameters;
	}

	public Treportparameter addTreportparameter(Treportparameter treportparameter) {
		getTreportparameters().add(treportparameter);
		treportparameter.setTreportpersonsetting(this);

		return treportparameter;
	}

	public Treportparameter removeTreportparameter(Treportparameter treportparameter) {
		getTreportparameters().remove(treportparameter);
		treportparameter.setTreportpersonsetting(null);

		return treportparameter;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

	public Trecurrencepattern getTrecurrencepattern() {
		return this.trecurrencepattern;
	}

	public void setTrecurrencepattern(Trecurrencepattern trecurrencepattern) {
		this.trecurrencepattern = trecurrencepattern;
	}

	public Texporttemplate getTexporttemplate() {
		return this.texporttemplate;
	}

	public void setTexporttemplate(Texporttemplate texporttemplate) {
		this.texporttemplate = texporttemplate;
	}

}
