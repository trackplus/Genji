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
 * The persistent class for the TWORKFLOWTRANSITION database table.
 * 
 */
@Entity
@Table(name="TWORKFLOWTRANSITION")
@TableGenerator(name="TWORKFLOWTRANSITION_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_WORKFLOWTRANSITION, allocationSize = 10)
public class Tworkflowtransition extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TWORKFLOWTRANSITION_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=255)
	private String controlpoints;

	private int textpositionx;

	private int textpositiony;

	@Column(length=36)
	private String tpuuid;

	private int transitiontype;

	//bi-directional many-to-one association to Tworkflowactivity
	@OneToMany(mappedBy="tworkflowtransition")
	private List<Tworkflowactivity> tworkflowactivities;

	//bi-directional many-to-one association to Tworkflowguard
	@OneToMany(mappedBy="tworkflowtransition")
	private List<Tworkflowguard> tworkflowguards;

	//bi-directional many-to-one association to Tworkflowstation
	@ManyToOne
	@JoinColumn(name="STATIONFROM")
	private Tworkflowstation tworkflowstation1;

	//bi-directional many-to-one association to Tworkflowstation
	@ManyToOne
	@JoinColumn(name="STATIONTO")
	private Tworkflowstation tworkflowstation2;

	//bi-directional many-to-one association to Taction
	@ManyToOne
	@JoinColumn(name="ACTIONKEY")
	private Taction taction;

	//bi-directional many-to-one association to Tworkflowdef
	@ManyToOne
	@JoinColumn(name="WORKFLOW", nullable=false)
	private Tworkflowdef tworkflowdef;

	public Tworkflowtransition() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getControlpoints() {
		return this.controlpoints;
	}

	public void setControlpoints(String controlpoints) {
		this.controlpoints = controlpoints;
	}

	public int getTextpositionx() {
		return this.textpositionx;
	}

	public void setTextpositionx(int textpositionx) {
		this.textpositionx = textpositionx;
	}

	public int getTextpositiony() {
		return this.textpositiony;
	}

	public void setTextpositiony(int textpositiony) {
		this.textpositiony = textpositiony;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public int getTransitiontype() {
		return this.transitiontype;
	}

	public void setTransitiontype(int transitiontype) {
		this.transitiontype = transitiontype;
	}

	public List<Tworkflowactivity> getTworkflowactivities() {
		return this.tworkflowactivities;
	}

	public void setTworkflowactivities(List<Tworkflowactivity> tworkflowactivities) {
		this.tworkflowactivities = tworkflowactivities;
	}

	public Tworkflowactivity addTworkflowactivity(Tworkflowactivity tworkflowactivity) {
		getTworkflowactivities().add(tworkflowactivity);
		tworkflowactivity.setTworkflowtransition(this);

		return tworkflowactivity;
	}

	public Tworkflowactivity removeTworkflowactivity(Tworkflowactivity tworkflowactivity) {
		getTworkflowactivities().remove(tworkflowactivity);
		tworkflowactivity.setTworkflowtransition(null);

		return tworkflowactivity;
	}

	public List<Tworkflowguard> getTworkflowguards() {
		return this.tworkflowguards;
	}

	public void setTworkflowguards(List<Tworkflowguard> tworkflowguards) {
		this.tworkflowguards = tworkflowguards;
	}

	public Tworkflowguard addTworkflowguard(Tworkflowguard tworkflowguard) {
		getTworkflowguards().add(tworkflowguard);
		tworkflowguard.setTworkflowtransition(this);

		return tworkflowguard;
	}

	public Tworkflowguard removeTworkflowguard(Tworkflowguard tworkflowguard) {
		getTworkflowguards().remove(tworkflowguard);
		tworkflowguard.setTworkflowtransition(null);

		return tworkflowguard;
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

	public Taction getTaction() {
		return this.taction;
	}

	public void setTaction(Taction taction) {
		this.taction = taction;
	}

	public Tworkflowdef getTworkflowdef() {
		return this.tworkflowdef;
	}

	public void setTworkflowdef(Tworkflowdef tworkflowdef) {
		this.tworkflowdef = tworkflowdef;
	}

}
