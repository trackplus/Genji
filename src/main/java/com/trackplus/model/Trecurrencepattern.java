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
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TRECURRENCEPATTERN database table.
 * 
 */
@Entity
@Table(name="TRECURRENCEPATTERN")
@TableGenerator(name="TRECURRENCEPATTERN_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_RECURRENCEPATTERN, allocationSize = 10)
public class Trecurrencepattern extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TRECURRENCEPATTERN_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=1)
	private String dateisabsolute;

	@Column(length=255)
	private String days;

	@Temporal(TemporalType.TIMESTAMP)
	private Date enddate;

	private int noofoccurences;

	private int occurencetype;

	private int param1;

	private int param2;

	private int param3;

	@Column(nullable=false)
	private int recurrenceperiod;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startdate;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Treportpersonsetting
	@OneToMany(mappedBy="trecurrencepattern")
	private List<Treportpersonsetting> treportpersonsettings;

	public Trecurrencepattern() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getDateisabsolute() {
		return this.dateisabsolute;
	}

	public void setDateisabsolute(String dateisabsolute) {
		this.dateisabsolute = dateisabsolute;
	}

	public String getDays() {
		return this.days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public Date getEnddate() {
		return this.enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public int getNoofoccurences() {
		return this.noofoccurences;
	}

	public void setNoofoccurences(int noofoccurences) {
		this.noofoccurences = noofoccurences;
	}

	public int getOccurencetype() {
		return this.occurencetype;
	}

	public void setOccurencetype(int occurencetype) {
		this.occurencetype = occurencetype;
	}

	public int getParam1() {
		return this.param1;
	}

	public void setParam1(int param1) {
		this.param1 = param1;
	}

	public int getParam2() {
		return this.param2;
	}

	public void setParam2(int param2) {
		this.param2 = param2;
	}

	public int getParam3() {
		return this.param3;
	}

	public void setParam3(int param3) {
		this.param3 = param3;
	}

	public int getRecurrenceperiod() {
		return this.recurrenceperiod;
	}

	public void setRecurrenceperiod(int recurrenceperiod) {
		this.recurrenceperiod = recurrenceperiod;
	}

	public Date getStartdate() {
		return this.startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public List<Treportpersonsetting> getTreportpersonsettings() {
		return this.treportpersonsettings;
	}

	public void setTreportpersonsettings(List<Treportpersonsetting> treportpersonsettings) {
		this.treportpersonsettings = treportpersonsettings;
	}

	public Treportpersonsetting addTreportpersonsetting(Treportpersonsetting treportpersonsetting) {
		getTreportpersonsettings().add(treportpersonsetting);
		treportpersonsetting.setTrecurrencepattern(this);

		return treportpersonsetting;
	}

	public Treportpersonsetting removeTreportpersonsetting(Treportpersonsetting treportpersonsetting) {
		getTreportpersonsettings().remove(treportpersonsetting);
		treportpersonsetting.setTrecurrencepattern(null);

		return treportpersonsetting;
	}

}
