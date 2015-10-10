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
import java.sql.Timestamp;


/**
 * The persistent class for the TTRAIL database table.
 * 
 */
@Entity
@Table(name="TTRAIL")
@TableGenerator(name="TTRAIL_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_TRAIL, allocationSize = 10)
public class Ttrail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TTRAIL_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int trailkey;

	@Lob
	private String changedescription;

	@Column(nullable=false)
	private Timestamp lastedit;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tworkitem
	@ManyToOne
	@JoinColumn(name="WORKITEMKEY", nullable=false)
	private Tworkitem tworkitem;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="CHANGEDBY", nullable=false)
	private Tperson tperson;

	public Ttrail() {
	}


	public int getObjectid() {
		return this.trailkey;
	}
	
	public void setObjectid(int key) {
		this.trailkey = key;
	}
	
	public int getTrailkey() {
		return this.trailkey;
	}

	public void setTrailkey(int trailkey) {
		this.trailkey = trailkey;
	}

	public String getChangedescription() {
		return this.changedescription;
	}

	public void setChangedescription(String changedescription) {
		this.changedescription = changedescription;
	}

	public Timestamp getLastedit() {
		return this.lastedit;
	}

	public void setLastedit(Timestamp lastedit) {
		this.lastedit = lastedit;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tworkitem getTworkitem() {
		return this.tworkitem;
	}

	public void setTworkitem(Tworkitem tworkitem) {
		this.tworkitem = tworkitem;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

}
