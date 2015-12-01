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


/**
 * The persistent class for the TMSPROJECTEXCHANGE database table.
 * 
 */
@Entity
@Table(name="TMSPROJECTEXCHANGE")
@TableGenerator(name="TMSPROJECTEXCHANGE_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_MSPROJECTEXCHANGE, allocationSize = 10)
public class Tmsprojectexchange extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TMSPROJECTEXCHANGE_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(nullable=false)
	private int entityid;

	@Column(nullable=false)
	private int entitytype;

	@Column(nullable=false)
	private int exchangedirection;

	@Lob
	private String filecontent;

	@Column(length=255)
	private String filename;

	@Column(nullable=false)
	private Timestamp lastedit;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="CHANGEDBY")
	private Tperson tperson;

	public Tmsprojectexchange() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public int getEntityid() {
		return this.entityid;
	}

	public void setEntityid(int entityid) {
		this.entityid = entityid;
	}

	public int getEntitytype() {
		return this.entitytype;
	}

	public void setEntitytype(int entitytype) {
		this.entitytype = entitytype;
	}

	public int getExchangedirection() {
		return this.exchangedirection;
	}

	public void setExchangedirection(int exchangedirection) {
		this.exchangedirection = exchangedirection;
	}

	public String getFilecontent() {
		return this.filecontent;
	}

	public void setFilecontent(String filecontent) {
		this.filecontent = filecontent;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Timestamp getLastedit() {
		return this.lastedit;
	}

	public void setLastedit(Timestamp lastedit) {
		this.lastedit = lastedit;
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

}
