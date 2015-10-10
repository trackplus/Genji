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
 * The persistent class for the TPROJCAT database table.
 * 
 */
@Entity
@Table(name="TPROJCAT")
@TableGenerator(name="TPROJCAT_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_PROJCAT, allocationSize = 10)
public class Tprojcat extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TPROJCAT_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int pkey;

	@Column(name="`LABEL`", length=35)
	private String label;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tnotify
	@OneToMany(mappedBy="tprojcat")
	private List<Tnotify> tnotifies;

	//bi-directional many-to-one association to Tproject
	@ManyToOne
	@JoinColumn(name="PROJKEY", nullable=false)
	private Tproject tproject;

	//bi-directional many-to-one association to Tworkitem
	@OneToMany(mappedBy="tprojcat")
	private List<Tworkitem> tworkitems;

	public Tprojcat() {
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

	public List<Tnotify> getTnotifies() {
		return this.tnotifies;
	}

	public void setTnotifies(List<Tnotify> tnotifies) {
		this.tnotifies = tnotifies;
	}

	public Tnotify addTnotify(Tnotify tnotify) {
		getTnotifies().add(tnotify);
		tnotify.setTprojcat(this);

		return tnotify;
	}

	public Tnotify removeTnotify(Tnotify tnotify) {
		getTnotifies().remove(tnotify);
		tnotify.setTprojcat(null);

		return tnotify;
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
		tworkitem.setTprojcat(this);

		return tworkitem;
	}

	public Tworkitem removeTworkitem(Tworkitem tworkitem) {
		getTworkitems().remove(tworkitem);
		tworkitem.setTprojcat(null);

		return tworkitem;
	}

}
