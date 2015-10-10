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
 * The persistent class for the TWORKFLOWSTATION database table.
 * 
 */
@Entity
@Table(name="TWORKFLOWSTATION")
@TableGenerator(name="TWORKFLOWSTATION_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_WORKFLOWSTATION, allocationSize = 10)
public class Tworkflowstation extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TWORKFLOWSTATION_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=255)
	private String name;

	private int nodex;

	private int nodey;

	private int stationtype;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tworkflowactivity
	@OneToMany(mappedBy="tworkflowstation1")
	private List<Tworkflowactivity> tworkflowactivities1;

	//bi-directional many-to-one association to Tworkflowactivity
	@OneToMany(mappedBy="tworkflowstation2")
	private List<Tworkflowactivity> tworkflowactivities2;

	//bi-directional many-to-one association to Tworkflowactivity
	@OneToMany(mappedBy="tworkflowstation3")
	private List<Tworkflowactivity> tworkflowactivities3;

	//bi-directional many-to-one association to Tstate
	@ManyToOne
	@JoinColumn(name="STATUS")
	private Tstate tstate;

	//bi-directional many-to-one association to Tworkflowdef
	@ManyToOne
	@JoinColumn(name="WORKFLOW")
	private Tworkflowdef tworkflowdef;

	//bi-directional many-to-one association to Tworkflowtransition
	@OneToMany(mappedBy="tworkflowstation1")
	private List<Tworkflowtransition> tworkflowtransitions1;

	//bi-directional many-to-one association to Tworkflowtransition
	@OneToMany(mappedBy="tworkflowstation2")
	private List<Tworkflowtransition> tworkflowtransitions2;

	public Tworkflowstation() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNodex() {
		return this.nodex;
	}

	public void setNodex(int nodex) {
		this.nodex = nodex;
	}

	public int getNodey() {
		return this.nodey;
	}

	public void setNodey(int nodey) {
		this.nodey = nodey;
	}

	public int getStationtype() {
		return this.stationtype;
	}

	public void setStationtype(int stationtype) {
		this.stationtype = stationtype;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public List<Tworkflowactivity> getTworkflowactivities1() {
		return this.tworkflowactivities1;
	}

	public void setTworkflowactivities1(List<Tworkflowactivity> tworkflowactivities1) {
		this.tworkflowactivities1 = tworkflowactivities1;
	}

	public Tworkflowactivity addTworkflowactivities1(Tworkflowactivity tworkflowactivities1) {
		getTworkflowactivities1().add(tworkflowactivities1);
		tworkflowactivities1.setTworkflowstation1(this);

		return tworkflowactivities1;
	}

	public Tworkflowactivity removeTworkflowactivities1(Tworkflowactivity tworkflowactivities1) {
		getTworkflowactivities1().remove(tworkflowactivities1);
		tworkflowactivities1.setTworkflowstation1(null);

		return tworkflowactivities1;
	}

	public List<Tworkflowactivity> getTworkflowactivities2() {
		return this.tworkflowactivities2;
	}

	public void setTworkflowactivities2(List<Tworkflowactivity> tworkflowactivities2) {
		this.tworkflowactivities2 = tworkflowactivities2;
	}

	public Tworkflowactivity addTworkflowactivities2(Tworkflowactivity tworkflowactivities2) {
		getTworkflowactivities2().add(tworkflowactivities2);
		tworkflowactivities2.setTworkflowstation2(this);

		return tworkflowactivities2;
	}

	public Tworkflowactivity removeTworkflowactivities2(Tworkflowactivity tworkflowactivities2) {
		getTworkflowactivities2().remove(tworkflowactivities2);
		tworkflowactivities2.setTworkflowstation2(null);

		return tworkflowactivities2;
	}

	public List<Tworkflowactivity> getTworkflowactivities3() {
		return this.tworkflowactivities3;
	}

	public void setTworkflowactivities3(List<Tworkflowactivity> tworkflowactivities3) {
		this.tworkflowactivities3 = tworkflowactivities3;
	}

	public Tworkflowactivity addTworkflowactivities3(Tworkflowactivity tworkflowactivities3) {
		getTworkflowactivities3().add(tworkflowactivities3);
		tworkflowactivities3.setTworkflowstation3(this);

		return tworkflowactivities3;
	}

	public Tworkflowactivity removeTworkflowactivities3(Tworkflowactivity tworkflowactivities3) {
		getTworkflowactivities3().remove(tworkflowactivities3);
		tworkflowactivities3.setTworkflowstation3(null);

		return tworkflowactivities3;
	}

	public Tstate getTstate() {
		return this.tstate;
	}

	public void setTstate(Tstate tstate) {
		this.tstate = tstate;
	}

	public Tworkflowdef getTworkflowdef() {
		return this.tworkflowdef;
	}

	public void setTworkflowdef(Tworkflowdef tworkflowdef) {
		this.tworkflowdef = tworkflowdef;
	}

	public List<Tworkflowtransition> getTworkflowtransitions1() {
		return this.tworkflowtransitions1;
	}

	public void setTworkflowtransitions1(List<Tworkflowtransition> tworkflowtransitions1) {
		this.tworkflowtransitions1 = tworkflowtransitions1;
	}

	public Tworkflowtransition addTworkflowtransitions1(Tworkflowtransition tworkflowtransitions1) {
		getTworkflowtransitions1().add(tworkflowtransitions1);
		tworkflowtransitions1.setTworkflowstation1(this);

		return tworkflowtransitions1;
	}

	public Tworkflowtransition removeTworkflowtransitions1(Tworkflowtransition tworkflowtransitions1) {
		getTworkflowtransitions1().remove(tworkflowtransitions1);
		tworkflowtransitions1.setTworkflowstation1(null);

		return tworkflowtransitions1;
	}

	public List<Tworkflowtransition> getTworkflowtransitions2() {
		return this.tworkflowtransitions2;
	}

	public void setTworkflowtransitions2(List<Tworkflowtransition> tworkflowtransitions2) {
		this.tworkflowtransitions2 = tworkflowtransitions2;
	}

	public Tworkflowtransition addTworkflowtransitions2(Tworkflowtransition tworkflowtransitions2) {
		getTworkflowtransitions2().add(tworkflowtransitions2);
		tworkflowtransitions2.setTworkflowstation2(this);

		return tworkflowtransitions2;
	}

	public Tworkflowtransition removeTworkflowtransitions2(Tworkflowtransition tworkflowtransitions2) {
		getTworkflowtransitions2().remove(tworkflowtransitions2);
		tworkflowtransitions2.setTworkflowstation2(null);

		return tworkflowtransitions2;
	}

}
