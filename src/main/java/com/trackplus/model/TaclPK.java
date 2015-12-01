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
 * The primary key class for the TACL database table.
 * 
 */
@Embeddable
public class TaclPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(unique=true, nullable=false)
	private int personkey;

	@Column(unique=true, nullable=false)
	private int rolekey;

	@Column(unique=true, nullable=false)
	private int projkey;

	public TaclPK() {
	}
	public int getPersonkey() {
		return this.personkey;
	}
	public void setPersonkey(int personkey) {
		this.personkey = personkey;
	}
	public int getRolekey() {
		return this.rolekey;
	}
	public void setRolekey(int rolekey) {
		this.rolekey = rolekey;
	}
	public int getProjkey() {
		return this.projkey;
	}
	public void setProjkey(int projkey) {
		this.projkey = projkey;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TaclPK)) {
			return false;
		}
		TaclPK castOther = (TaclPK)other;
		return 
			(this.personkey == castOther.personkey)
			&& (this.rolekey == castOther.rolekey)
			&& (this.projkey == castOther.projkey);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.personkey;
		hash = hash * prime + this.rolekey;
		hash = hash * prime + this.projkey;
		
		return hash;
	}
}
