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
 * The persistent class for the TFIELD database table.
 * 
 */
@Entity
@Table(name="TFIELD")
@TableGenerator(name="TFIELD_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_FIELD, allocationSize = 10)
public class Tfield extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TFIELD_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=1)
	private String deprecated;

	@Lob
	private String description;

	@Column(nullable=false, length=255)
	private String fieldtype;

	@Column(length=1)
	private String filterfield;

	@Column(nullable=false, length=1)
	private String iscustom;

	@Column(nullable=false, length=255)
	private String name;

	@Column(nullable=false, length=1)
	private String required;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tattributevalue
	@OneToMany(mappedBy="tfield")
	private List<Tattributevalue> tattributevalues;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="OWNER")
	private Tperson tperson;

	//bi-directional many-to-one association to Tfieldconfig
	@OneToMany(mappedBy="tfield")
	private List<Tfieldconfig> tfieldconfigs;

	//bi-directional many-to-one association to Tscreenfield
	@OneToMany(mappedBy="tfield")
	private List<Tscreenfield> tscreenfields;

	public Tfield() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getDeprecated() {
		return this.deprecated;
	}

	public void setDeprecated(String deprecated) {
		this.deprecated = deprecated;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFieldtype() {
		return this.fieldtype;
	}

	public void setFieldtype(String fieldtype) {
		this.fieldtype = fieldtype;
	}

	public String getFilterfield() {
		return this.filterfield;
	}

	public void setFilterfield(String filterfield) {
		this.filterfield = filterfield;
	}

	public String getIscustom() {
		return this.iscustom;
	}

	public void setIscustom(String iscustom) {
		this.iscustom = iscustom;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRequired() {
		return this.required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public List<Tattributevalue> getTattributevalues() {
		return this.tattributevalues;
	}

	public void setTattributevalues(List<Tattributevalue> tattributevalues) {
		this.tattributevalues = tattributevalues;
	}

	public Tattributevalue addTattributevalue(Tattributevalue tattributevalue) {
		getTattributevalues().add(tattributevalue);
		tattributevalue.setTfield(this);

		return tattributevalue;
	}

	public Tattributevalue removeTattributevalue(Tattributevalue tattributevalue) {
		getTattributevalues().remove(tattributevalue);
		tattributevalue.setTfield(null);

		return tattributevalue;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

	public List<Tfieldconfig> getTfieldconfigs() {
		return this.tfieldconfigs;
	}

	public void setTfieldconfigs(List<Tfieldconfig> tfieldconfigs) {
		this.tfieldconfigs = tfieldconfigs;
	}

	public Tfieldconfig addTfieldconfig(Tfieldconfig tfieldconfig) {
		getTfieldconfigs().add(tfieldconfig);
		tfieldconfig.setTfield(this);

		return tfieldconfig;
	}

	public Tfieldconfig removeTfieldconfig(Tfieldconfig tfieldconfig) {
		getTfieldconfigs().remove(tfieldconfig);
		tfieldconfig.setTfield(null);

		return tfieldconfig;
	}

	public List<Tscreenfield> getTscreenfields() {
		return this.tscreenfields;
	}

	public void setTscreenfields(List<Tscreenfield> tscreenfields) {
		this.tscreenfields = tscreenfields;
	}

	public Tscreenfield addTscreenfield(Tscreenfield tscreenfield) {
		getTscreenfields().add(tscreenfield);
		tscreenfield.setTfield(this);

		return tscreenfield;
	}

	public Tscreenfield removeTscreenfield(Tscreenfield tscreenfield) {
		getTscreenfields().remove(tscreenfield);
		tscreenfield.setTfield(null);

		return tscreenfield;
	}

}
