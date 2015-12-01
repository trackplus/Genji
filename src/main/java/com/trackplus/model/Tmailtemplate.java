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
import java.util.List;


/**
 * The persistent class for the TMAILTEMPLATE database table.
 * 
 */
@Entity
@Table(name="TMAILTEMPLATE")
@TableGenerator(name="TMAILTEMPLATE_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_MAILTEMPLATE, allocationSize = 10)
public class Tmailtemplate extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TMAILTEMPLATE_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=255)
	private String description;

	@Column(length=255)
	private String name;

	@Column(length=255)
	private String taglabel;

	private int templatetype;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tmailtemplateconfig
	@OneToMany(mappedBy="tmailtemplate")
	private List<Tmailtemplateconfig> tmailtemplateconfigs;

	//bi-directional many-to-one association to Tmailtemplatedef
	@OneToMany(mappedBy="tmailtemplate")
	private List<Tmailtemplatedef> tmailtemplatedefs;

	public Tmailtemplate() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaglabel() {
		return this.taglabel;
	}

	public void setTaglabel(String taglabel) {
		this.taglabel = taglabel;
	}

	public int getTemplatetype() {
		return this.templatetype;
	}

	public void setTemplatetype(int templatetype) {
		this.templatetype = templatetype;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public List<Tmailtemplateconfig> getTmailtemplateconfigs() {
		return this.tmailtemplateconfigs;
	}

	public void setTmailtemplateconfigs(List<Tmailtemplateconfig> tmailtemplateconfigs) {
		this.tmailtemplateconfigs = tmailtemplateconfigs;
	}

	public Tmailtemplateconfig addTmailtemplateconfig(Tmailtemplateconfig tmailtemplateconfig) {
		getTmailtemplateconfigs().add(tmailtemplateconfig);
		tmailtemplateconfig.setTmailtemplate(this);

		return tmailtemplateconfig;
	}

	public Tmailtemplateconfig removeTmailtemplateconfig(Tmailtemplateconfig tmailtemplateconfig) {
		getTmailtemplateconfigs().remove(tmailtemplateconfig);
		tmailtemplateconfig.setTmailtemplate(null);

		return tmailtemplateconfig;
	}

	public List<Tmailtemplatedef> getTmailtemplatedefs() {
		return this.tmailtemplatedefs;
	}

	public void setTmailtemplatedefs(List<Tmailtemplatedef> tmailtemplatedefs) {
		this.tmailtemplatedefs = tmailtemplatedefs;
	}

	public Tmailtemplatedef addTmailtemplatedef(Tmailtemplatedef tmailtemplatedef) {
		getTmailtemplatedefs().add(tmailtemplatedef);
		tmailtemplatedef.setTmailtemplate(this);

		return tmailtemplatedef;
	}

	public Tmailtemplatedef removeTmailtemplatedef(Tmailtemplatedef tmailtemplatedef) {
		getTmailtemplatedefs().remove(tmailtemplatedef);
		tmailtemplatedef.setTmailtemplate(null);

		return tmailtemplatedef;
	}

}
