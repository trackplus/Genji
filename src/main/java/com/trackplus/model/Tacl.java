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
 * The persistent class for the TACL database table.
 * 
 */
@Entity
@Table(name="TACL")
public class Tacl extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TaclPK id;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="PERSONKEY", nullable=false, insertable=false, updatable=false)
	private Tperson tperson;

	//bi-directional many-to-one association to Trole
	@ManyToOne
	@JoinColumn(name="ROLEKEY", nullable=false, insertable=false, updatable=false)
	private Trole trole;

	//bi-directional many-to-one association to Tproject
	@ManyToOne
	@JoinColumn(name="PROJKEY", nullable=false, insertable=false, updatable=false)
	private Tproject tproject;

	public Tacl() {
	}

	/*
	 * Just a dummy to satisfy the interface
	 * @see com.trackplus.model.BaseObject#getObjectid()
	 */
	public int getObjectid() {
		return 0;
	}
	
	public void setObjectid(int key) {
		// just dummy to satisfy the interface
	}

	public TaclPK getId() {
		return this.id;
	}

	public void setId(TaclPK id) {
		this.id = id;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

	public Trole getTrole() {
		return this.trole;
	}

	public void setTrole(Trole trole) {
		this.trole = trole;
	}

	public Tproject getTproject() {
		return this.tproject;
	}

	public void setTproject(Tproject tproject) {
		this.tproject = tproject;
	}

}
