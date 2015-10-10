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
 * The persistent class for the TESCALATIONENTRY database table.
 * 
 */
@Entity
@Table(name="TESCALATIONENTRY")
@TableGenerator(name="TESCALATIONENTRY_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_ESCALATIONENTRY, allocationSize = 10)

public class Tescalationentry extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TESCALATIONENTRY_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	private int nlevel;

	@Lob
	private String sparameters;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tsla
	@ManyToOne
	@JoinColumn(name="SLA", nullable=false)
	private Tsla tsla;

	//bi-directional many-to-one association to Tpriority
	@ManyToOne
	@JoinColumn(name="PRIORITY")
	private Tpriority tpriority;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="ESCALATETO")
	private Tperson tperson;

	//bi-directional many-to-one association to Tescalationstate
	@OneToMany(mappedBy="tescalationentry")
	private List<Tescalationstate> tescalationstates;

	public Tescalationentry() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public int getNlevel() {
		return this.nlevel;
	}

	public void setNlevel(int nlevel) {
		this.nlevel = nlevel;
	}

	public String getSparameters() {
		return this.sparameters;
	}

	public void setSparameters(String sparameters) {
		this.sparameters = sparameters;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tsla getTsla() {
		return this.tsla;
	}

	public void setTsla(Tsla tsla) {
		this.tsla = tsla;
	}

	public Tpriority getTpriority() {
		return this.tpriority;
	}

	public void setTpriority(Tpriority tpriority) {
		this.tpriority = tpriority;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

	public List<Tescalationstate> getTescalationstates() {
		return this.tescalationstates;
	}

	public void setTescalationstates(List<Tescalationstate> tescalationstates) {
		this.tescalationstates = tescalationstates;
	}

	public Tescalationstate addTescalationstate(Tescalationstate tescalationstate) {
		getTescalationstates().add(tescalationstate);
		tescalationstate.setTescalationentry(this);

		return tescalationstate;
	}

	public Tescalationstate removeTescalationstate(Tescalationstate tescalationstate) {
		getTescalationstates().remove(tescalationstate);
		tescalationstate.setTescalationentry(null);

		return tescalationstate;
	}

}
