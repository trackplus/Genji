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


/**
 * The persistent class for the TWORKFLOWACTIVITY database table.
 * 
 */
@Entity
@Table(name="TWORKFLOWACTIVITY")
@TableGenerator(name="TWORKFLOWACTIVITY_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_WORKFLOWACTIVITY, allocationSize = 10)
public class Tworkflowactivity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TWORKFLOWACTIVITY_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Lob
	private String activityparams;

	@Column(nullable=false)
	private int activitytype;

	private int screen;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tworkflowtransition
	@ManyToOne
	@JoinColumn(name="TRANSITIONACTIVITY")
	private Tworkflowtransition tworkflowtransition;

	//bi-directional many-to-one association to Tworkflowstation
	@ManyToOne
	@JoinColumn(name="STATIONENTRYACTIVITY")
	private Tworkflowstation tworkflowstation1;

	//bi-directional many-to-one association to Tworkflowstation
	@ManyToOne
	@JoinColumn(name="STATIONEXITACTIVITY")
	private Tworkflowstation tworkflowstation2;

	//bi-directional many-to-one association to Tworkflowstation
	@ManyToOne
	@JoinColumn(name="STATIONDOACTIVITY")
	private Tworkflowstation tworkflowstation3;

	//bi-directional many-to-one association to Tscript
	@ManyToOne
	@JoinColumn(name="GROOVYSCRIPT")
	private Tscript tscript;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="NEWMAN")
	private Tperson tperson1;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="NEWRESP")
	private Tperson tperson2;

	//bi-directional many-to-one association to Tsla
	@ManyToOne
	@JoinColumn(name="SLA")
	private Tsla tsla;

	public Tworkflowactivity() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getActivityparams() {
		return this.activityparams;
	}

	public void setActivityparams(String activityparams) {
		this.activityparams = activityparams;
	}

	public int getActivitytype() {
		return this.activitytype;
	}

	public void setActivitytype(int activitytype) {
		this.activitytype = activitytype;
	}

	public int getScreen() {
		return this.screen;
	}

	public void setScreen(int screen) {
		this.screen = screen;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tworkflowtransition getTworkflowtransition() {
		return this.tworkflowtransition;
	}

	public void setTworkflowtransition(Tworkflowtransition tworkflowtransition) {
		this.tworkflowtransition = tworkflowtransition;
	}

	public Tworkflowstation getTworkflowstation1() {
		return this.tworkflowstation1;
	}

	public void setTworkflowstation1(Tworkflowstation tworkflowstation1) {
		this.tworkflowstation1 = tworkflowstation1;
	}

	public Tworkflowstation getTworkflowstation2() {
		return this.tworkflowstation2;
	}

	public void setTworkflowstation2(Tworkflowstation tworkflowstation2) {
		this.tworkflowstation2 = tworkflowstation2;
	}

	public Tworkflowstation getTworkflowstation3() {
		return this.tworkflowstation3;
	}

	public void setTworkflowstation3(Tworkflowstation tworkflowstation3) {
		this.tworkflowstation3 = tworkflowstation3;
	}

	public Tscript getTscript() {
		return this.tscript;
	}

	public void setTscript(Tscript tscript) {
		this.tscript = tscript;
	}

	public Tperson getTperson1() {
		return this.tperson1;
	}

	public void setTperson1(Tperson tperson1) {
		this.tperson1 = tperson1;
	}

	public Tperson getTperson2() {
		return this.tperson2;
	}

	public void setTperson2(Tperson tperson2) {
		this.tperson2 = tperson2;
	}

	public Tsla getTsla() {
		return this.tsla;
	}

	public void setTsla(Tsla tsla) {
		this.tsla = tsla;
	}

}
