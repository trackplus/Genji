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
 * The persistent class for the TOUTLINETEMPLATEDEF database table.
 * 
 */
@Entity
@Table(name="TOUTLINETEMPLATEDEF")
@TableGenerator(name="TOUTLINETEMPLATEDEF_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_OUTLINETEMPLATEDEF, allocationSize = 10)
public class Toutlinetemplatedef extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TOUTLINETEMPLATEDEF_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(nullable=false)
	private int levelno;

	private int listid;

	@Column(nullable=false, length=10)
	private String separatorchar;

	@Column(nullable=false)
	private int sequencelength;

	@Column(nullable=false)
	private int sequencetype;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Toutlinetemplate
	@ManyToOne
	@JoinColumn(name="OUTLINETEMPLATE", nullable=false)
	private Toutlinetemplate toutlinetemplate;

	public Toutlinetemplatedef() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public int getLevelno() {
		return this.levelno;
	}

	public void setLevelno(int levelno) {
		this.levelno = levelno;
	}

	public int getListid() {
		return this.listid;
	}

	public void setListid(int listid) {
		this.listid = listid;
	}

	public String getSeparatorchar() {
		return this.separatorchar;
	}

	public void setSeparatorchar(String separatorchar) {
		this.separatorchar = separatorchar;
	}

	public int getSequencelength() {
		return this.sequencelength;
	}

	public void setSequencelength(int sequencelength) {
		this.sequencelength = sequencelength;
	}

	public int getSequencetype() {
		return this.sequencetype;
	}

	public void setSequencetype(int sequencetype) {
		this.sequencetype = sequencetype;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Toutlinetemplate getToutlinetemplate() {
		return this.toutlinetemplate;
	}

	public void setToutlinetemplate(Toutlinetemplate toutlinetemplate) {
		this.toutlinetemplate = toutlinetemplate;
	}

}
