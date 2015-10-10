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

import java.util.Date;


/**
 * The persistent class for the TEMAILPROCESSED database table.
 * 
 */
@Entity
@Table(name="TEMAILPROCESSED")
@TableGenerator(name="TEMAILPROCESSED_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_EMAILPROCESSED, allocationSize = 10)

public class Temailprocessed extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TEMAILPROCESSED_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(nullable=false, length=255)
	private String messageuid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date processeddate;

	@Column(nullable=false, length=255)
	private String receivedat;

	@Column(length=36)
	private String tpuuid;

	public Temailprocessed() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getMessageuid() {
		return this.messageuid;
	}

	public void setMessageuid(String messageuid) {
		this.messageuid = messageuid;
	}

	public Date getProcesseddate() {
		return this.processeddate;
	}

	public void setProcesseddate(Date processeddate) {
		this.processeddate = processeddate;
	}

	public String getReceivedat() {
		return this.receivedat;
	}

	public void setReceivedat(String receivedat) {
		this.receivedat = receivedat;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

}
