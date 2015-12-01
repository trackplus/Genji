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
 * The persistent class for the TSCREEN database table.
 * 
 */
@Entity
@Table(name="TSCREEN")
@TableGenerator(name="TSCREEN_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_SCREEN, allocationSize = 10)
public class Tscreen extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TSCREEN_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Lob
	private String description;

	@Column(name="`LABEL`", length=255)
	private String label;

	@Column(nullable=false, length=255)
	private String name;

	@Column(length=50)
	private String taglabel;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="OWNER")
	private Tperson tperson;

	//bi-directional many-to-one association to Tscreenconfig
	@OneToMany(mappedBy="tscreen")
	private List<Tscreenconfig> tscreenconfigs;

	//bi-directional many-to-one association to Tscreentab
	@OneToMany(mappedBy="tscreen")
	private List<Tscreentab> tscreentabs;

	public Tscreen() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaglabel() {
		return this.taglabel;
	}

	public void setTaglabel(String taglabel) {
		this.taglabel = taglabel;
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

	public List<Tscreenconfig> getTscreenconfigs() {
		return this.tscreenconfigs;
	}

	public void setTscreenconfigs(List<Tscreenconfig> tscreenconfigs) {
		this.tscreenconfigs = tscreenconfigs;
	}

	public Tscreenconfig addTscreenconfig(Tscreenconfig tscreenconfig) {
		getTscreenconfigs().add(tscreenconfig);
		tscreenconfig.setTscreen(this);

		return tscreenconfig;
	}

	public Tscreenconfig removeTscreenconfig(Tscreenconfig tscreenconfig) {
		getTscreenconfigs().remove(tscreenconfig);
		tscreenconfig.setTscreen(null);

		return tscreenconfig;
	}

	public List<Tscreentab> getTscreentabs() {
		return this.tscreentabs;
	}

	public void setTscreentabs(List<Tscreentab> tscreentabs) {
		this.tscreentabs = tscreentabs;
	}

	public Tscreentab addTscreentab(Tscreentab tscreentab) {
		getTscreentabs().add(tscreentab);
		tscreentab.setTscreen(this);

		return tscreentab;
	}

	public Tscreentab removeTscreentab(Tscreentab tscreentab) {
		getTscreentabs().remove(tscreentab);
		tscreentab.setTscreen(null);

		return tscreentab;
	}

}
