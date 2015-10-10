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


/**
 * The persistent class for the TWORKITEMLOCK database table.
 * 
 */
@Entity
@Table(name="TWORKITEMLOCK")
@TableGenerator(name="TWORKITEMLOCK_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_WORKITEMLOCK, allocationSize = 10)
public class Tworkitemlock extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TWORKITEMLOCK_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int workitem;

	@Column(nullable=false, length=255)
	private String httpsession;

	@Column(length=36)
	private String tpuuid;

	//bi-directional one-to-one association to Tworkitem
	@OneToOne
	@JoinColumn(name="WORKITEM", nullable=false, insertable=false, updatable=false)
	private Tworkitem tworkitem;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="PERSON", nullable=false)
	private Tperson tperson;

	public Tworkitemlock() {
	}


	public int getObjectid() {
		return 0;
	}
	
	public void setObjectid(int key) {

	}
	
	public int getWorkitem() {
		return this.workitem;
	}

	public void setWorkitem(int workitem) {
		this.workitem = workitem;
	}

	public String getHttpsession() {
		return this.httpsession;
	}

	public void setHttpsession(String httpsession) {
		this.httpsession = httpsession;
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
