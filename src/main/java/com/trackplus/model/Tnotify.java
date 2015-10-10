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
 * The persistent class for the TNOTIFY database table.
 * 
 */
@Entity
@Table(name="TNOTIFY")
@TableGenerator(name="TNOTIFY_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_NOTIFY, allocationSize = 10)
public class Tnotify extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TNOTIFY_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int pkey;

	@Column(length=1)
	private String racirole;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tprojcat
	@ManyToOne
	@JoinColumn(name="PROJCATKEY")
	private Tprojcat tprojcat;

	//bi-directional many-to-one association to Tstate
	@ManyToOne
	@JoinColumn(name="STATEKEY")
	private Tstate tstate;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="PERSONKEY", nullable=false)
	private Tperson tperson;

	//bi-directional many-to-one association to Tworkitem
	@ManyToOne
	@JoinColumn(name="WORKITEM")
	private Tworkitem tworkitem;

	public Tnotify() {
	}

	public int getObjectid() {
		return this.pkey;
	}
	
	public void setObjectid(int key) {
		this.pkey = key;
	}

	public int getPkey() {
		return this.pkey;
	}

	public void setPkey(int pkey) {
		this.pkey = pkey;
	}

	public String getRacirole() {
		return this.racirole;
	}

	public void setRacirole(String racirole) {
		this.racirole = racirole;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tprojcat getTprojcat() {
		return this.tprojcat;
	}

	public void setTprojcat(Tprojcat tprojcat) {
		this.tprojcat = tprojcat;
	}

	public Tstate getTstate() {
		return this.tstate;
	}

	public void setTstate(Tstate tstate) {
		this.tstate = tstate;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

	public Tworkitem getTworkitem() {
		return this.tworkitem;
	}

	public void setTworkitem(Tworkitem tworkitem) {
		this.tworkitem = tworkitem;
	}

}
