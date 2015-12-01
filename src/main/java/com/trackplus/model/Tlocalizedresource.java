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
 * The persistent class for the TLOCALIZEDRESOURCES database table.
 * 
 */
@Entity
@Table(name="TLOCALIZEDRESOURCES")
@TableGenerator(name="TLOCALIZEDRESOURCES_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_LOCALIZEDRESOURCES, allocationSize = 10)
public class Tlocalizedresource extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TLOCALIZEDRESOURCES_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(nullable=false, length=255)
	private String fieldname;

	@Column(length=20)
	private String locale;

	@Lob
	private String localizedtext;

	private int primarykeyvalue;

	@Column(length=255)
	private String tablename;

	@Column(length=1)
	private String textchanged;

	@Column(length=36)
	private String tpuuid;

	public Tlocalizedresource() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getFieldname() {
		return this.fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public String getLocale() {
		return this.locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getLocalizedtext() {
		return this.localizedtext;
	}

	public void setLocalizedtext(String localizedtext) {
		this.localizedtext = localizedtext;
	}

	public int getPrimarykeyvalue() {
		return this.primarykeyvalue;
	}

	public void setPrimarykeyvalue(int primarykeyvalue) {
		this.primarykeyvalue = primarykeyvalue;
	}

	public String getTablename() {
		return this.tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getTextchanged() {
		return this.textchanged;
	}

	public void setTextchanged(String textchanged) {
		this.textchanged = textchanged;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

}
