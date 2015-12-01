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
 * The persistent class for the TTEMPLATEPERSON database table.
 * 
 */
@Entity
@Table(name="TTEMPLATEPERSON")
@TableGenerator(name="TTEMPLATEPERSON_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_TEMPLATEPERSON, allocationSize = 10)
public class Ttemplateperson extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TTEMPLATEPERSON_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	private int rightflag;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="PERSON", nullable=false)
	private Tperson tperson;

	//bi-directional many-to-one association to Texporttemplate
	@ManyToOne
	@JoinColumn(name="REPORTTEMPLATE", nullable=false)
	private Texporttemplate texporttemplate;

	public Ttemplateperson() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public int getRightflag() {
		return this.rightflag;
	}

	public void setRightflag(int rightflag) {
		this.rightflag = rightflag;
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

	public Texporttemplate getTexporttemplate() {
		return this.texporttemplate;
	}

	public void setTexporttemplate(Texporttemplate texporttemplate) {
		this.texporttemplate = texporttemplate;
	}

}
