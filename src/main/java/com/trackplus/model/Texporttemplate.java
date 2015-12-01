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
 * The persistent class for the TEXPORTTEMPLATE database table.
 * 
 */
@Entity
@Table(name="TEXPORTTEMPLATE")
@TableGenerator(name="TEXPORTTEMPLATE_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_EXPORTTEMPLATE, allocationSize = 10)

public class Texporttemplate extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TEXPORTTEMPLATE_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=1)
	private String deleted;

	@Lob
	private String description;

	@Column(nullable=false, length=255)
	private String exportformat;

	@Column(nullable=false, length=255)
	private String name;

	@Column(nullable=false, length=255)
	private String reporttype;

	@Column(nullable=false)
	private int repositorytype;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="PERSON", nullable=false)
	private Tperson tperson;

	//bi-directional many-to-one association to Tproject
	@ManyToOne
	@JoinColumn(name="PROJECT")
	private Tproject tproject;

	//bi-directional many-to-one association to Treportcategory
	@ManyToOne
	@JoinColumn(name="CATEGORYKEY")
	private Treportcategory treportcategory;

	//bi-directional many-to-one association to Treportpersonsetting
	@OneToMany(mappedBy="texporttemplate")
	private List<Treportpersonsetting> treportpersonsettings;

	//bi-directional many-to-one association to Ttemplateperson
	@OneToMany(mappedBy="texporttemplate")
	private List<Ttemplateperson> ttemplatepersons;

	public Texporttemplate() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getDeleted() {
		return this.deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExportformat() {
		return this.exportformat;
	}

	public void setExportformat(String exportformat) {
		this.exportformat = exportformat;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReporttype() {
		return this.reporttype;
	}

	public void setReporttype(String reporttype) {
		this.reporttype = reporttype;
	}

	public int getRepositorytype() {
		return this.repositorytype;
	}

	public void setRepositorytype(int repositorytype) {
		this.repositorytype = repositorytype;
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

	public Tproject getTproject() {
		return this.tproject;
	}

	public void setTproject(Tproject tproject) {
		this.tproject = tproject;
	}

	public Treportcategory getTreportcategory() {
		return this.treportcategory;
	}

	public void setTreportcategory(Treportcategory treportcategory) {
		this.treportcategory = treportcategory;
	}

	public List<Treportpersonsetting> getTreportpersonsettings() {
		return this.treportpersonsettings;
	}

	public void setTreportpersonsettings(List<Treportpersonsetting> treportpersonsettings) {
		this.treportpersonsettings = treportpersonsettings;
	}

	public Treportpersonsetting addTreportpersonsetting(Treportpersonsetting treportpersonsetting) {
		getTreportpersonsettings().add(treportpersonsetting);
		treportpersonsetting.setTexporttemplate(this);

		return treportpersonsetting;
	}

	public Treportpersonsetting removeTreportpersonsetting(Treportpersonsetting treportpersonsetting) {
		getTreportpersonsettings().remove(treportpersonsetting);
		treportpersonsetting.setTexporttemplate(null);

		return treportpersonsetting;
	}

	public List<Ttemplateperson> getTtemplatepersons() {
		return this.ttemplatepersons;
	}

	public void setTtemplatepersons(List<Ttemplateperson> ttemplatepersons) {
		this.ttemplatepersons = ttemplatepersons;
	}

	public Ttemplateperson addTtemplateperson(Ttemplateperson ttemplateperson) {
		getTtemplatepersons().add(ttemplateperson);
		ttemplateperson.setTexporttemplate(this);

		return ttemplateperson;
	}

	public Ttemplateperson removeTtemplateperson(Ttemplateperson ttemplateperson) {
		getTtemplatepersons().remove(ttemplateperson);
		ttemplateperson.setTexporttemplate(null);

		return ttemplateperson;
	}

}
