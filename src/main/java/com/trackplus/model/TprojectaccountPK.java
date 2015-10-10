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

/**
 * The primary key class for the TPROJECTACCOUNT database table.
 * 
 */
@Embeddable
public class TprojectaccountPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(unique=true, nullable=false)
	private int account;

	@Column(unique=true, nullable=false)
	private int project;

	public TprojectaccountPK() {
	}
	public int getAccount() {
		return this.account;
	}
	public void setAccount(int account) {
		this.account = account;
	}
	public int getProject() {
		return this.project;
	}
	public void setProject(int project) {
		this.project = project;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TprojectaccountPK)) {
			return false;
		}
		TprojectaccountPK castOther = (TprojectaccountPK)other;
		return 
			(this.account == castOther.account)
			&& (this.project == castOther.project);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.account;
		hash = hash * prime + this.project;
		
		return hash;
	}
}
