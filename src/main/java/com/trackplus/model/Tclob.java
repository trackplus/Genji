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
 * The persistent class for the TCLOB database table.
 * 
 */
@Entity
@Table(name="TCLOB")
@TableGenerator(name="TCLOB_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
 pkColumnValue=BaseEntity.TABLE_CLOB, allocationSize = 10)


public class Tclob extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TCLOB_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Lob
	private String clobvalue;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tevent
	@OneToMany(mappedBy="tclob")
	private List<Tevent> tevents;

	//bi-directional many-to-one association to Tlastexecutedquery
	@OneToMany(mappedBy="tclob")
	private List<Tlastexecutedquery> tlastexecutedqueries;

	//bi-directional many-to-one association to Tqueryrepository
	@OneToMany(mappedBy="tclob")
	private List<Tqueryrepository> tqueryrepositories;

	public Tclob() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getClobvalue() {
		return this.clobvalue;
	}

	public void setClobvalue(String clobvalue) {
		this.clobvalue = clobvalue;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public List<Tevent> getTevents() {
		return this.tevents;
	}

	public void setTevents(List<Tevent> tevents) {
		this.tevents = tevents;
	}

	public Tevent addTevent(Tevent tevent) {
		getTevents().add(tevent);
		tevent.setTclob(this);

		return tevent;
	}

	public Tevent removeTevent(Tevent tevent) {
		getTevents().remove(tevent);
		tevent.setTclob(null);

		return tevent;
	}

	public List<Tlastexecutedquery> getTlastexecutedqueries() {
		return this.tlastexecutedqueries;
	}

	public void setTlastexecutedqueries(List<Tlastexecutedquery> tlastexecutedqueries) {
		this.tlastexecutedqueries = tlastexecutedqueries;
	}

	public Tlastexecutedquery addTlastexecutedquery(Tlastexecutedquery tlastexecutedquery) {
		getTlastexecutedqueries().add(tlastexecutedquery);
		tlastexecutedquery.setTclob(this);

		return tlastexecutedquery;
	}

	public Tlastexecutedquery removeTlastexecutedquery(Tlastexecutedquery tlastexecutedquery) {
		getTlastexecutedqueries().remove(tlastexecutedquery);
		tlastexecutedquery.setTclob(null);

		return tlastexecutedquery;
	}

	public List<Tqueryrepository> getTqueryrepositories() {
		return this.tqueryrepositories;
	}

	public void setTqueryrepositories(List<Tqueryrepository> tqueryrepositories) {
		this.tqueryrepositories = tqueryrepositories;
	}

	public Tqueryrepository addTqueryrepository(Tqueryrepository tqueryrepository) {
		getTqueryrepositories().add(tqueryrepository);
		tqueryrepository.setTclob(this);

		return tqueryrepository;
	}

	public Tqueryrepository removeTqueryrepository(Tqueryrepository tqueryrepository) {
		getTqueryrepositories().remove(tqueryrepository);
		tqueryrepository.setTclob(null);

		return tqueryrepository;
	}

}
