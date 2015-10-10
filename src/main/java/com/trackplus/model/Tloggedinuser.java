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
import java.sql.Timestamp;


/**
 * The persistent class for the TLOGGEDINUSERS database table.
 * 
 */
@Entity
@Table(name="TLOGGEDINUSERS")
@TableGenerator(name="TLOGGEDINUSERS_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_LOGGEDINUSERS, allocationSize = 10)
public class Tloggedinuser extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TLOGGEDINUSERS_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(nullable=false)
	private Timestamp lastupdate;

	@Lob
	private String moreprops;

	private int nodeaddress;

	@Column(length=255)
	private String sessionid;

	private int userlevel;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="LOGGEDUSER", nullable=false)
	private Tperson tperson;

	public Tloggedinuser() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public Timestamp getLastupdate() {
		return this.lastupdate;
	}

	public void setLastupdate(Timestamp lastupdate) {
		this.lastupdate = lastupdate;
	}

	public String getMoreprops() {
		return this.moreprops;
	}

	public void setMoreprops(String moreprops) {
		this.moreprops = moreprops;
	}

	public int getNodeaddress() {
		return this.nodeaddress;
	}

	public void setNodeaddress(int nodeaddress) {
		this.nodeaddress = nodeaddress;
	}

	public String getSessionid() {
		return this.sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public int getUserlevel() {
		return this.userlevel;
	}

	public void setUserlevel(int userlevel) {
		this.userlevel = userlevel;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

}
