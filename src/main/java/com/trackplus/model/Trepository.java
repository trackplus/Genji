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
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the TREPOSITORY database table.
 * 
 */
@Entity
@Table(name="TREPOSITORY")
@TableGenerator(name="TREPOSITORY_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_REPOSITORY, allocationSize = 10)
public class Trepository extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TREPOSITORY_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(nullable=false)
	private Timestamp enddate;

	@Column(nullable=false, length=255)
	private String repositorytype;

	@Column(nullable=false, length=255)
	private String repositoryurl;

	@Column(nullable=false)
	private Timestamp startdate;

	@Column(nullable=false)
	private int statuskey;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Trevision
	@OneToMany(mappedBy="trepository")
	private List<Trevision> trevisions;

	public Trepository() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public Timestamp getEnddate() {
		return this.enddate;
	}

	public void setEnddate(Timestamp enddate) {
		this.enddate = enddate;
	}

	public String getRepositorytype() {
		return this.repositorytype;
	}

	public void setRepositorytype(String repositorytype) {
		this.repositorytype = repositorytype;
	}

	public String getRepositoryurl() {
		return this.repositoryurl;
	}

	public void setRepositoryurl(String repositoryurl) {
		this.repositoryurl = repositoryurl;
	}

	public Timestamp getStartdate() {
		return this.startdate;
	}

	public void setStartdate(Timestamp startdate) {
		this.startdate = startdate;
	}

	public int getStatuskey() {
		return this.statuskey;
	}

	public void setStatuskey(int statuskey) {
		this.statuskey = statuskey;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public List<Trevision> getTrevisions() {
		return this.trevisions;
	}

	public void setTrevisions(List<Trevision> trevisions) {
		this.trevisions = trevisions;
	}

	public Trevision addTrevision(Trevision trevision) {
		getTrevisions().add(trevision);
		trevision.setTrepository(this);

		return trevision;
	}

	public Trevision removeTrevision(Trevision trevision) {
		getTrevisions().remove(trevision);
		trevision.setTrepository(null);

		return trevision;
	}

}
