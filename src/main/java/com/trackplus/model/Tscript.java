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
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the TSCRIPTS database table.
 * 
 */
@Entity
@Table(name="TSCRIPTS")
@TableGenerator(name="TSCRIPTS_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_SCRIPTS, allocationSize = 10)
public class Tscript extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TSCRIPTS_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=253)
	private String clazzname;

	@Column(nullable=false)
	private Timestamp lastedit;

	private int originalversion;

	private int scriptrole;

	private int scripttype;

	private int scriptversion;

	@Lob
	private String sourcecode;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tfieldconfig
	@OneToMany(mappedBy="tscript")
	private List<Tfieldconfig> tfieldconfigs;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="CHANGEDBY")
	private Tperson tperson;

	//bi-directional many-to-one association to Tprojecttype
	@ManyToOne
	@JoinColumn(name="PROJECTTYPE")
	private Tprojecttype tprojecttype;

	//bi-directional many-to-one association to Tproject
	@ManyToOne
	@JoinColumn(name="PROJECT")
	private Tproject tproject;

	//bi-directional many-to-one association to Tworkflowactivity
	@OneToMany(mappedBy="tscript")
	private List<Tworkflowactivity> tworkflowactivities;

	//bi-directional many-to-one association to Tworkflowguard
	@OneToMany(mappedBy="tscript")
	private List<Tworkflowguard> tworkflowguards;

	public Tscript() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getClazzName() {
		return this.clazzname;
	}

	public void setClazzName(String clazzname) {
		this.clazzname = clazzname;
	}

	public Timestamp getLastEdit() {
		return this.lastedit;
	}

	public void setLastEdit(Timestamp lastedit) {
		this.lastedit = lastedit;
	}

	public int getOriginalVersion() {
		return this.originalversion;
	}

	public void setOriginalVersion(int originalversion) {
		this.originalversion = originalversion;
	}

	public int getScriptRole() {
		return this.scriptrole;
	}

	public void setScriptRole(int scriptrole) {
		this.scriptrole = scriptrole;
	}

	public int getScriptType() {
		return this.scripttype;
	}

	public void setScriptType(int scripttype) {
		this.scripttype = scripttype;
	}

	public int getScriptVersion() {
		return this.scriptversion;
	}

	public void setScriptVersion(int scriptversion) {
		this.scriptversion = scriptversion;
	}

	public String getSourceCode() {
		return this.sourcecode;
	}

	public void setSourceCode(String sourcecode) {
		this.sourcecode = sourcecode;
	}

	public String getUuid() {
		return this.tpuuid;
	}

	public void setUuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public List<Tfieldconfig> getTfieldconfigs() {
		return this.tfieldconfigs;
	}

	public void setTfieldconfigs(List<Tfieldconfig> tfieldconfigs) {
		this.tfieldconfigs = tfieldconfigs;
	}

	public Tfieldconfig addTfieldconfig(Tfieldconfig tfieldconfig) {
		getTfieldconfigs().add(tfieldconfig);
		tfieldconfig.setTscript(this);

		return tfieldconfig;
	}

	public Tfieldconfig removeTfieldconfig(Tfieldconfig tfieldconfig) {
		getTfieldconfigs().remove(tfieldconfig);
		tfieldconfig.setTscript(null);

		return tfieldconfig;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

	public Tprojecttype getTprojecttype() {
		return this.tprojecttype;
	}

	public void setTprojecttype(Tprojecttype tprojecttype) {
		this.tprojecttype = tprojecttype;
	}

	public Tproject getTproject() {
		return this.tproject;
	}

	public void setTproject(Tproject tproject) {
		this.tproject = tproject;
	}

	public List<Tworkflowactivity> getTworkflowactivities() {
		return this.tworkflowactivities;
	}

	public void setTworkflowactivities(List<Tworkflowactivity> tworkflowactivities) {
		this.tworkflowactivities = tworkflowactivities;
	}

	public Tworkflowactivity addTworkflowactivity(Tworkflowactivity tworkflowactivity) {
		getTworkflowactivities().add(tworkflowactivity);
		tworkflowactivity.setTscript(this);

		return tworkflowactivity;
	}

	public Tworkflowactivity removeTworkflowactivity(Tworkflowactivity tworkflowactivity) {
		getTworkflowactivities().remove(tworkflowactivity);
		tworkflowactivity.setTscript(null);

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
		tworkflowguard.setTscript(this);

		return tworkflowguard;
	}

	public Tworkflowguard removeTworkflowguard(Tworkflowguard tworkflowguard) {
		getTworkflowguards().remove(tworkflowguard);
		tworkflowguard.setTscript(null);

		return tworkflowguard;
	}

}
