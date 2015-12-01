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
 * The persistent class for the TOUTLINETEMPLATE database table.
 * 
 */
@Entity
@Table(name="TOUTLINETEMPLATE")
@TableGenerator(name="TOUTLINETEMPLATE_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_OUTLINETEMPLATE, allocationSize = 10)
public class Toutlinetemplate extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TOUTLINETEMPLATE_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(nullable=false)
	private int entitytype;

	@Column(name="`LABEL`", nullable=false, length=255)
	private String label;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Toutlinecode
	@OneToMany(mappedBy="toutlinetemplate")
	private List<Toutlinecode> toutlinecodes;

	//bi-directional many-to-one association to Toutlinetemplatedef
	@OneToMany(mappedBy="toutlinetemplate")
	private List<Toutlinetemplatedef> toutlinetemplatedefs;

	public Toutlinetemplate() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public int getEntitytype() {
		return this.entitytype;
	}

	public void setEntitytype(int entitytype) {
		this.entitytype = entitytype;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public List<Toutlinecode> getToutlinecodes() {
		return this.toutlinecodes;
	}

	public void setToutlinecodes(List<Toutlinecode> toutlinecodes) {
		this.toutlinecodes = toutlinecodes;
	}

	public Toutlinecode addToutlinecode(Toutlinecode toutlinecode) {
		getToutlinecodes().add(toutlinecode);
		toutlinecode.setToutlinetemplate(this);

		return toutlinecode;
	}

	public Toutlinecode removeToutlinecode(Toutlinecode toutlinecode) {
		getToutlinecodes().remove(toutlinecode);
		toutlinecode.setToutlinetemplate(null);

		return toutlinecode;
	}

	public List<Toutlinetemplatedef> getToutlinetemplatedefs() {
		return this.toutlinetemplatedefs;
	}

	public void setToutlinetemplatedefs(List<Toutlinetemplatedef> toutlinetemplatedefs) {
		this.toutlinetemplatedefs = toutlinetemplatedefs;
	}

	public Toutlinetemplatedef addToutlinetemplatedef(Toutlinetemplatedef toutlinetemplatedef) {
		getToutlinetemplatedefs().add(toutlinetemplatedef);
		toutlinetemplatedef.setToutlinetemplate(this);

		return toutlinetemplatedef;
	}

	public Toutlinetemplatedef removeToutlinetemplatedef(Toutlinetemplatedef toutlinetemplatedef) {
		getToutlinetemplatedefs().remove(toutlinetemplatedef);
		toutlinetemplatedef.setToutlinetemplate(null);

		return toutlinetemplatedef;
	}

}
