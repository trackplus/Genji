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
 * The persistent class for the TCLASS database table.
 * 
 */
@Entity
@Table(name="TCLASS")
@TableGenerator(name="TCLASS_GEN", table="ID_TABLE",
pkColumnName="ID_TABLE_ID",
pkColumnValue=BaseEntity.TABLE_CLASS, allocationSize = 10)

public class Tclass extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TCLASS_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int pkey;

	@Column(name="`LABEL`", nullable=false, length=25)
	private String label;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tproject
	@ManyToOne
	@JoinColumn(name="PROJKEY")
	private Tproject tproject;

	//bi-directional many-to-one association to Tworkitem
	@OneToMany(mappedBy="tclass")
	private List<Tworkitem> tworkitems;

	public Tclass() {
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

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tproject getTproject() {
		return this.tproject;
	}

	public void setTproject(Tproject tproject) {
		this.tproject = tproject;
	}

	public List<Tworkitem> getTworkitems() {
		return this.tworkitems;
	}

	public void setTworkitems(List<Tworkitem> tworkitems) {
		this.tworkitems = tworkitems;
	}

	public Tworkitem addTworkitem(Tworkitem tworkitem) {
		getTworkitems().add(tworkitem);
		tworkitem.setTclass(this);

		return tworkitem;
	}

	public Tworkitem removeTworkitem(Tworkitem tworkitem) {
		getTworkitems().remove(tworkitem);
		tworkitem.setTclass(null);

		return tworkitem;
	}

}
