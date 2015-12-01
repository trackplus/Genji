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
 * The persistent class for the TWORKFLOWGUARD database table.
 * 
 */
@Entity
@Table(name="TWORKFLOWGUARD")
@TableGenerator(name="TWORKFLOWGUARD_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_WORKFLOWGUARD, allocationSize = 10)
public class Tworkflowguard extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TWORKFLOWGUARD_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Lob
	private String guardparams;

	@Column(nullable=false)
	private int guardtype;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tworkflowtransition
	@ManyToOne
	@JoinColumn(name="WORKFLOWTRANSITION", nullable=false)
	private Tworkflowtransition tworkflowtransition;

	//bi-directional many-to-one association to Trole
	@ManyToOne
	@JoinColumn(name="ROLEKEY")
	private Trole trole;

	//bi-directional many-to-one association to Tscript
	@ManyToOne
	@JoinColumn(name="GROOVYSCRIPT")
	private Tscript tscript;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="PERSON")
	private Tperson tperson;

	public Tworkflowguard() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getGuardparams() {
		return this.guardparams;
	}

	public void setGuardparams(String guardparams) {
		this.guardparams = guardparams;
	}

	public int getGuardtype() {
		return this.guardtype;
	}

	public void setGuardtype(int guardtype) {
		this.guardtype = guardtype;
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

	public Trole getTrole() {
		return this.trole;
	}

	public void setTrole(Trole trole) {
		this.trole = trole;
	}

	public Tscript getTscript() {
		return this.tscript;
	}

	public void setTscript(Tscript tscript) {
		this.tscript = tscript;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

}
