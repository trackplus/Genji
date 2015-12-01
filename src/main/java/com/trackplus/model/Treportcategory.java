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
 * The persistent class for the TREPORTCATEGORY database table.
 * 
 */
@Entity
@Table(name="TREPORTCATEGORY")
@TableGenerator(name="TREPORTCATEGORY_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_REPORTCATEGORY, allocationSize = 10)
public class Treportcategory extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TREPORTCATEGORY_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(name="`LABEL`", nullable=false, length=255)
	private String label;

	@Column(nullable=false)
	private int repository;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Texporttemplate
	@OneToMany(mappedBy="treportcategory")
	private List<Texporttemplate> texporttemplates;

	//bi-directional many-to-one association to Tproject
	@ManyToOne
	@JoinColumn(name="PROJECT")
	private Tproject tproject;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="CREATEDBY", nullable=false)
	private Tperson tperson;

	//bi-directional many-to-one association to Treportcategory
	@ManyToOne
	@JoinColumn(name="PARENTID")
	private Treportcategory treportcategory;

	//bi-directional many-to-one association to Treportcategory
	@OneToMany(mappedBy="treportcategory")
	private List<Treportcategory> treportcategories;

	public Treportcategory() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getRepository() {
		return this.repository;
	}

	public void setRepository(int repository) {
		this.repository = repository;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public List<Texporttemplate> getTexporttemplates() {
		return this.texporttemplates;
	}

	public void setTexporttemplates(List<Texporttemplate> texporttemplates) {
		this.texporttemplates = texporttemplates;
	}

	public Texporttemplate addTexporttemplate(Texporttemplate texporttemplate) {
		getTexporttemplates().add(texporttemplate);
		texporttemplate.setTreportcategory(this);

		return texporttemplate;
	}

	public Texporttemplate removeTexporttemplate(Texporttemplate texporttemplate) {
		getTexporttemplates().remove(texporttemplate);
		texporttemplate.setTreportcategory(null);

		return texporttemplate;
	}

	public Tproject getTproject() {
		return this.tproject;
	}

	public void setTproject(Tproject tproject) {
		this.tproject = tproject;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

	public Treportcategory getTreportcategory() {
		return this.treportcategory;
	}

	public void setTreportcategory(Treportcategory treportcategory) {
		this.treportcategory = treportcategory;
	}

	public List<Treportcategory> getTreportcategories() {
		return this.treportcategories;
	}

	public void setTreportcategories(List<Treportcategory> treportcategories) {
		this.treportcategories = treportcategories;
	}

	public Treportcategory addTreportcategory(Treportcategory treportcategory) {
		getTreportcategories().add(treportcategory);
		treportcategory.setTreportcategory(this);

		return treportcategory;
	}

	public Treportcategory removeTreportcategory(Treportcategory treportcategory) {
		getTreportcategories().remove(treportcategory);
		treportcategory.setTreportcategory(null);

		return treportcategory;
	}

}
