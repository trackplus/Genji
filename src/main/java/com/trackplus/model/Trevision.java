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
 * The persistent class for the TREVISION database table.
 * 
 */
@Entity
@Table(name="TREVISION")
@TableGenerator(name="TREVISION_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_REVISION, allocationSize = 10)
public class Trevision extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TREVISION_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(nullable=false, length=255)
	private String authorname;

	@Lob
	private String changedescription;

	@Column(nullable=false, length=255)
	private String filename;

	@Column(nullable=false)
	private Timestamp revisiondate;

	@Column(nullable=false, length=255)
	private String revisionnumbr;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Trepository
	@ManyToOne
	@JoinColumn(name="REPOSITORYKEY", nullable=false)
	private Trepository trepository;

	//bi-directional many-to-one association to Trevisionworkitem
	@OneToMany(mappedBy="trevision")
	private List<Trevisionworkitem> trevisionworkitems;

	public Trevision() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getAuthorname() {
		return this.authorname;
	}

	public void setAuthorname(String authorname) {
		this.authorname = authorname;
	}

	public String getChangedescription() {
		return this.changedescription;
	}

	public void setChangedescription(String changedescription) {
		this.changedescription = changedescription;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Timestamp getRevisiondate() {
		return this.revisiondate;
	}

	public void setRevisiondate(Timestamp revisiondate) {
		this.revisiondate = revisiondate;
	}

	public String getRevisionnumbr() {
		return this.revisionnumbr;
	}

	public void setRevisionnumbr(String revisionnumbr) {
		this.revisionnumbr = revisionnumbr;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Trepository getTrepository() {
		return this.trepository;
	}

	public void setTrepository(Trepository trepository) {
		this.trepository = trepository;
	}

	public List<Trevisionworkitem> getTrevisionworkitems() {
		return this.trevisionworkitems;
	}

	public void setTrevisionworkitems(List<Trevisionworkitem> trevisionworkitems) {
		this.trevisionworkitems = trevisionworkitems;
	}

	public Trevisionworkitem addTrevisionworkitem(Trevisionworkitem trevisionworkitem) {
		getTrevisionworkitems().add(trevisionworkitem);
		trevisionworkitem.setTrevision(this);

		return trevisionworkitem;
	}

	public Trevisionworkitem removeTrevisionworkitem(Trevisionworkitem trevisionworkitem) {
		getTrevisionworkitems().remove(trevisionworkitem);
		trevisionworkitem.setTrevision(null);

		return trevisionworkitem;
	}

}
