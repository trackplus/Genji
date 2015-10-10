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
 * The persistent class for the TFILTERCATEGORY database table.
 * 
 */
@Entity
@Table(name="TFILTERCATEGORY")
@TableGenerator(name="TFILTERCATEGORY_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_FILTERCATEGORY, allocationSize = 10)
public class Tfiltercategory extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TFILTERCATEGORY_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(nullable=false)
	private int filtertype;

	@Column(name="`LABEL`", nullable=false, length=255)
	private String label;

	@Column(nullable=false)
	private int repository;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tproject
	@ManyToOne
	@JoinColumn(name="PROJECT")
	private Tproject tproject;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="CREATEDBY", nullable=false)
	private Tperson tperson;

	//bi-directional many-to-one association to Tfiltercategory
	@ManyToOne
	@JoinColumn(name="PARENTID")
	private Tfiltercategory tfiltercategory;

	//bi-directional many-to-one association to Tfiltercategory
	@OneToMany(mappedBy="tfiltercategory")
	private List<Tfiltercategory> tfiltercategories;

	//bi-directional many-to-one association to Tqueryrepository
	@OneToMany(mappedBy="tfiltercategory")
	private List<Tqueryrepository> tqueryrepositories;

	public Tfiltercategory() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public int getFiltertype() {
		return this.filtertype;
	}

	public void setFiltertype(int filtertype) {
		this.filtertype = filtertype;
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

	public Tfiltercategory getTfiltercategory() {
		return this.tfiltercategory;
	}

	public void setTfiltercategory(Tfiltercategory tfiltercategory) {
		this.tfiltercategory = tfiltercategory;
	}

	public List<Tfiltercategory> getTfiltercategories() {
		return this.tfiltercategories;
	}

	public void setTfiltercategories(List<Tfiltercategory> tfiltercategories) {
		this.tfiltercategories = tfiltercategories;
	}

	public Tfiltercategory addTfiltercategory(Tfiltercategory tfiltercategory) {
		getTfiltercategories().add(tfiltercategory);
		tfiltercategory.setTfiltercategory(this);

		return tfiltercategory;
	}

	public Tfiltercategory removeTfiltercategory(Tfiltercategory tfiltercategory) {
		getTfiltercategories().remove(tfiltercategory);
		tfiltercategory.setTfiltercategory(null);

		return tfiltercategory;
	}

	public List<Tqueryrepository> getTqueryrepositories() {
		return this.tqueryrepositories;
	}

	public void setTqueryrepositories(List<Tqueryrepository> tqueryrepositories) {
		this.tqueryrepositories = tqueryrepositories;
	}

	public Tqueryrepository addTqueryrepository(Tqueryrepository tqueryrepository) {
		getTqueryrepositories().add(tqueryrepository);
		tqueryrepository.setTfiltercategory(this);

		return tqueryrepository;
	}

	public Tqueryrepository removeTqueryrepository(Tqueryrepository tqueryrepository) {
		getTqueryrepositories().remove(tqueryrepository);
		tqueryrepository.setTfiltercategory(null);

		return tqueryrepository;
	}

}
