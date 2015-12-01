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
 * The persistent class for the TMENUITEMQUERY database table.
 * 
 */
@Entity
@Table(name="TMENUITEMQUERY")
@TableGenerator(name="TMENUITEMQUERY_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_MENUITEMQUERY, allocationSize = 10)
public class Tmenuitemquery extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TMENUITEMQUERY_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	private int cssstylefield;

	@Column(length=1)
	private String includeinmenu;

	@Column(nullable=false)
	private int person;

	@Column(nullable=false)
	private int querykey;

	@Column(length=36)
	private String tpuuid;

	public Tmenuitemquery() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public int getCssstylefield() {
		return this.cssstylefield;
	}

	public void setCssstylefield(int cssstylefield) {
		this.cssstylefield = cssstylefield;
	}

	public String getIncludeinmenu() {
		return this.includeinmenu;
	}

	public void setIncludeinmenu(String includeinmenu) {
		this.includeinmenu = includeinmenu;
	}

	public int getPerson() {
		return this.person;
	}

	public void setPerson(int person) {
		this.person = person;
	}

	public int getQuerykey() {
		return this.querykey;
	}

	public void setQuerykey(int querykey) {
		this.querykey = querykey;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

}
