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
 * The persistent class for the TSLA database table.
 * 
 */
@Entity
@Table(name="TSLA")
@TableGenerator(name="TSLA_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_SLA, allocationSize = 10)
public class Tsla extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TSLA_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(nullable=false, length=255)
	private String name;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tescalationentry
	@OneToMany(mappedBy="tsla")
	private List<Tescalationentry> tescalationentries;

	//bi-directional many-to-one association to Torgprojectsla
	@OneToMany(mappedBy="tsla")
	private List<Torgprojectsla> torgprojectslas;

	//bi-directional many-to-one association to Tworkflowactivity
	@OneToMany(mappedBy="tsla")
	private List<Tworkflowactivity> tworkflowactivities;

	public Tsla() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
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

	public List<Tescalationentry> getTescalationentries() {
		return this.tescalationentries;
	}

	public void setTescalationentries(List<Tescalationentry> tescalationentries) {
		this.tescalationentries = tescalationentries;
	}

	public Tescalationentry addTescalationentry(Tescalationentry tescalationentry) {
		getTescalationentries().add(tescalationentry);
		tescalationentry.setTsla(this);

		return tescalationentry;
	}

	public Tescalationentry removeTescalationentry(Tescalationentry tescalationentry) {
		getTescalationentries().remove(tescalationentry);
		tescalationentry.setTsla(null);

		return tescalationentry;
	}

	public List<Torgprojectsla> getTorgprojectslas() {
		return this.torgprojectslas;
	}

	public void setTorgprojectslas(List<Torgprojectsla> torgprojectslas) {
		this.torgprojectslas = torgprojectslas;
	}

	public Torgprojectsla addTorgprojectsla(Torgprojectsla torgprojectsla) {
		getTorgprojectslas().add(torgprojectsla);
		torgprojectsla.setTsla(this);

		return torgprojectsla;
	}

	public Torgprojectsla removeTorgprojectsla(Torgprojectsla torgprojectsla) {
		getTorgprojectslas().remove(torgprojectsla);
		torgprojectsla.setTsla(null);

		return torgprojectsla;
	}

	public List<Tworkflowactivity> getTworkflowactivities() {
		return this.tworkflowactivities;
	}

	public void setTworkflowactivities(List<Tworkflowactivity> tworkflowactivities) {
		this.tworkflowactivities = tworkflowactivities;
	}

	public Tworkflowactivity addTworkflowactivity(Tworkflowactivity tworkflowactivity) {
		getTworkflowactivities().add(tworkflowactivity);
		tworkflowactivity.setTsla(this);

		return tworkflowactivity;
	}

	public Tworkflowactivity removeTworkflowactivity(Tworkflowactivity tworkflowactivity) {
		getTworkflowactivities().remove(tworkflowactivity);
		tworkflowactivity.setTsla(null);

		return tworkflowactivity;
	}

}
