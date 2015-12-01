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
 * The persistent class for the TAPPLICATIONCONTEXT database table.
 * 
 */
@Entity
@Table(name="TAPPLICATIONCONTEXT")
@TableGenerator(name="TAPPLICATIONCONTEXT_GEN", table="ID_TABLE",
pkColumnName="ID_TABLE_ID",
pkColumnValue=BaseEntity.TABLE_APPLICATIONCONTEXT, allocationSize = 10)

public class Tapplicationcontext extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TAPPLICATIONCONTEXT_GEN", strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(nullable=false)
	private int firsttime;

	private int loggedfullusers;

	private int loggedlimitedusers;

	@Lob
	private String moreprops;

	@Column(nullable=false)
	private int refreshconfiguration;

	public Tapplicationcontext() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public int getFirsttime() {
		return this.firsttime;
	}

	public void setFirsttime(int firsttime) {
		this.firsttime = firsttime;
	}

	public int getLoggedfullusers() {
		return this.loggedfullusers;
	}

	public void setLoggedfullusers(int loggedfullusers) {
		this.loggedfullusers = loggedfullusers;
	}

	public int getLoggedlimitedusers() {
		return this.loggedlimitedusers;
	}

	public void setLoggedlimitedusers(int loggedlimitedusers) {
		this.loggedlimitedusers = loggedlimitedusers;
	}

	public String getMoreprops() {
		return this.moreprops;
	}

	public void setMoreprops(String moreprops) {
		this.moreprops = moreprops;
	}

	public int getRefreshconfiguration() {
		return this.refreshconfiguration;
	}

	public void setRefreshconfiguration(int refreshconfiguration) {
		this.refreshconfiguration = refreshconfiguration;
	}

}
