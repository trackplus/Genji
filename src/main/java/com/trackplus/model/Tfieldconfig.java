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
 * The persistent class for the TFIELDCONFIG database table.
 * 
 */
@Entity
@Table(name="TFIELDCONFIG")
@TableGenerator(name="TFIELDCONFIG_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_FIELDCONFIG, allocationSize = 10)

public class Tfieldconfig extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TFIELDCONFIG_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Lob
	private String description;

	@Column(nullable=false, length=1)
	private String history;

	@Column(name="`LABEL`", nullable=false, length=255)
	private String label;

	@Column(nullable=false, length=1)
	private String required;

	@Column(length=255)
	private String tooltip;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tconfigoptionsrole
	@OneToMany(mappedBy="tfieldconfig")
	private List<Tconfigoptionsrole> tconfigoptionsroles;

	//bi-directional many-to-one association to Tfield
	@ManyToOne
	@JoinColumn(name="FIELDKEY", nullable=false)
	private Tfield tfield;

	//bi-directional many-to-one association to Tcategory
	@ManyToOne
	@JoinColumn(name="ISSUETYPE")
	private Tcategory tcategory;

	//bi-directional many-to-one association to Tprojecttype
	@ManyToOne
	@JoinColumn(name="PROJECTTYPE")
	private Tprojecttype tprojecttype;

	//bi-directional many-to-one association to Tproject
	@ManyToOne
	@JoinColumn(name="PROJECT")
	private Tproject tproject;

	//bi-directional many-to-one association to Tscript
	@ManyToOne
	@JoinColumn(name="GROOVYSCRIPT")
	private Tscript tscript;

	//bi-directional many-to-one association to Tgeneralsetting
	@OneToMany(mappedBy="tfieldconfig")
	private List<Tgeneralsetting> tgeneralsettings;

	//bi-directional many-to-one association to Toptionsetting
	@OneToMany(mappedBy="tfieldconfig")
	private List<Toptionsetting> toptionsettings;

	//bi-directional many-to-one association to Ttextboxsetting
	@OneToMany(mappedBy="tfieldconfig")
	private List<Ttextboxsetting> ttextboxsettings;

	public Tfieldconfig() {
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

	public String getHistory() {
		return this.history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getRequired() {
		return this.required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getTooltip() {
		return this.tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public List<Tconfigoptionsrole> getTconfigoptionsroles() {
		return this.tconfigoptionsroles;
	}

	public void setTconfigoptionsroles(List<Tconfigoptionsrole> tconfigoptionsroles) {
		this.tconfigoptionsroles = tconfigoptionsroles;
	}

	public Tconfigoptionsrole addTconfigoptionsrole(Tconfigoptionsrole tconfigoptionsrole) {
		getTconfigoptionsroles().add(tconfigoptionsrole);
		tconfigoptionsrole.setTfieldconfig(this);

		return tconfigoptionsrole;
	}

	public Tconfigoptionsrole removeTconfigoptionsrole(Tconfigoptionsrole tconfigoptionsrole) {
		getTconfigoptionsroles().remove(tconfigoptionsrole);
		tconfigoptionsrole.setTfieldconfig(null);

		return tconfigoptionsrole;
	}

	public Tfield getTfield() {
		return this.tfield;
	}

	public void setTfield(Tfield tfield) {
		this.tfield = tfield;
	}

	public Tcategory getTcategory() {
		return this.tcategory;
	}

	public void setTcategory(Tcategory tcategory) {
		this.tcategory = tcategory;
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

	public Tscript getTscript() {
		return this.tscript;
	}

	public void setTscript(Tscript tscript) {
		this.tscript = tscript;
	}

	public List<Tgeneralsetting> getTgeneralsettings() {
		return this.tgeneralsettings;
	}

	public void setTgeneralsettings(List<Tgeneralsetting> tgeneralsettings) {
		this.tgeneralsettings = tgeneralsettings;
	}

	public Tgeneralsetting addTgeneralsetting(Tgeneralsetting tgeneralsetting) {
		getTgeneralsettings().add(tgeneralsetting);
		tgeneralsetting.setTfieldconfig(this);

		return tgeneralsetting;
	}

	public Tgeneralsetting removeTgeneralsetting(Tgeneralsetting tgeneralsetting) {
		getTgeneralsettings().remove(tgeneralsetting);
		tgeneralsetting.setTfieldconfig(null);

		return tgeneralsetting;
	}

	public List<Toptionsetting> getToptionsettings() {
		return this.toptionsettings;
	}

	public void setToptionsettings(List<Toptionsetting> toptionsettings) {
		this.toptionsettings = toptionsettings;
	}

	public Toptionsetting addToptionsetting(Toptionsetting toptionsetting) {
		getToptionsettings().add(toptionsetting);
		toptionsetting.setTfieldconfig(this);

		return toptionsetting;
	}

	public Toptionsetting removeToptionsetting(Toptionsetting toptionsetting) {
		getToptionsettings().remove(toptionsetting);
		toptionsetting.setTfieldconfig(null);

		return toptionsetting;
	}

	public List<Ttextboxsetting> getTtextboxsettings() {
		return this.ttextboxsettings;
	}

	public void setTtextboxsettings(List<Ttextboxsetting> ttextboxsettings) {
		this.ttextboxsettings = ttextboxsettings;
	}

	public Ttextboxsetting addTtextboxsetting(Ttextboxsetting ttextboxsetting) {
		getTtextboxsettings().add(ttextboxsetting);
		ttextboxsetting.setTfieldconfig(this);

		return ttextboxsetting;
	}

	public Ttextboxsetting removeTtextboxsetting(Ttextboxsetting ttextboxsetting) {
		getTtextboxsettings().remove(ttextboxsetting);
		ttextboxsetting.setTfieldconfig(null);

		return ttextboxsetting;
	}

}
