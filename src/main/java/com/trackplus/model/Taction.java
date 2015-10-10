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

import static javax.persistence.GenerationType.TABLE;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the TACTION database table.
 * 
 */
@Entity
@Table(name="TACTION")
@TableGenerator(name="TACTION_GEN", table="ID_TABLE",
pkColumnName="ID_TABLE_ID",
pkColumnValue=BaseEntity.TABLE_ACTION, allocationSize = 1)

public class Taction extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@Id
    @GeneratedValue(generator="TACTION_GEN", strategy = TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	private int actiontype;

	@Lob
	private String description;

	@Column(name="`LABEL`", length=255)
	private String label;

	@Column(nullable=false, length=255)
	private String name;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tblob
	@ManyToOne
	@JoinColumn(name="ICONKEY")
	private Tblob tblob;

	//bi-directional many-to-one association to Tscreenconfig
	@OneToMany(mappedBy="taction")
	private List<Tscreenconfig> tscreenconfigs;

	//bi-directional many-to-one association to Tworkflowtransition
	@OneToMany(mappedBy="taction")
	private List<Tworkflowtransition> tworkflowtransitions;

	public Taction() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public int getActiontype() {
		return this.actiontype;
	}

	public void setActiontype(int actiontype) {
		this.actiontype = actiontype;
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

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tblob getTblob() {
		return this.tblob;
	}

	public void setTblob(Tblob tblob) {
		this.tblob = tblob;
	}

	public List<Tscreenconfig> getTscreenconfigs() {
		return this.tscreenconfigs;
	}

	public void setTscreenconfigs(List<Tscreenconfig> tscreenconfigs) {
		this.tscreenconfigs = tscreenconfigs;
	}

	public Tscreenconfig addTscreenconfig(Tscreenconfig tscreenconfig) {
		getTscreenconfigs().add(tscreenconfig);
		tscreenconfig.setTaction(this);

		return tscreenconfig;
	}

	public Tscreenconfig removeTscreenconfig(Tscreenconfig tscreenconfig) {
		getTscreenconfigs().remove(tscreenconfig);
		tscreenconfig.setTaction(null);

		return tscreenconfig;
	}

	public List<Tworkflowtransition> getTworkflowtransitions() {
		return this.tworkflowtransitions;
	}

	public void setTworkflowtransitions(List<Tworkflowtransition> tworkflowtransitions) {
		this.tworkflowtransitions = tworkflowtransitions;
	}

	public Tworkflowtransition addTworkflowtransition(Tworkflowtransition tworkflowtransition) {
		getTworkflowtransitions().add(tworkflowtransition);
		tworkflowtransition.setTaction(this);

		return tworkflowtransition;
	}

	public Tworkflowtransition removeTworkflowtransition(Tworkflowtransition tworkflowtransition) {
		getTworkflowtransitions().remove(tworkflowtransition);
		tworkflowtransition.setTaction(null);

		return tworkflowtransition;
	}

}
