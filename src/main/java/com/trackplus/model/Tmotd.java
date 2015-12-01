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
 * The persistent class for the TMOTD database table.
 * 
 */
@Entity
@Table(name="TMOTD")
@TableGenerator(name="TMOTD_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_MOTD, allocationSize = 10)
public class Tmotd extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TMOTD_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=255)
	private String teasertext;

	@Column(length=2)
	private String thelocale;

	@Lob
	private String themessage;

	@Column(length=36)
	private String tpuuid;

	public Tmotd() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getTeasertext() {
		return this.teasertext;
	}

	public void setTeasertext(String teasertext) {
		this.teasertext = teasertext;
	}

	public String getThelocale() {
		return this.thelocale;
	}

	public void setThelocale(String thelocale) {
		this.thelocale = thelocale;
	}

	public String getThemessage() {
		return this.themessage;
	}

	public void setThemessage(String themessage) {
		this.themessage = themessage;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

}
