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
 * The persistent class for the TTEXTBOXSETTINGS database table.
 * 
 */
@Entity
@Table(name="TTEXTBOXSETTINGS")
@TableGenerator(name="TTEXTBOXSETTINGS_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_TEXTBOXSETTINGS, allocationSize = 10)
public class Ttextboxsetting extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TTEXTBOXSETTINGS_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=1)
	private String dateiswithtime;

	@Column(length=1)
	private String defaultchar;

	@Temporal(TemporalType.TIMESTAMP)
	private Date defaultdate;

	private double defaultdouble;

	private int defaultinteger;

	private int defaultoption;

	@Column(length=255)
	private String defaulttext;

	@Temporal(TemporalType.TIMESTAMP)
	private Date maxdate;

	private int maxdecimaldigit;

	private double maxdouble;

	private int maxinteger;

	private int maxoption;

	private int maxtextlength;

	@Temporal(TemporalType.TIMESTAMP)
	private Date mindate;

	private double mindouble;

	private int mininteger;

	private int minoption;

	private int mintextlength;

	private int parametercode;

	@Column(nullable=false, length=1)
	private String required;

	@Column(length=36)
	private String tpuuid;

	private int validvalue;

	//bi-directional many-to-one association to Tfieldconfig
	@ManyToOne
	@JoinColumn(name="CONFIG", nullable=false)
	private Tfieldconfig tfieldconfig;

	public Ttextboxsetting() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getDateiswithtime() {
		return this.dateiswithtime;
	}

	public void setDateiswithtime(String dateiswithtime) {
		this.dateiswithtime = dateiswithtime;
	}

	public String getDefaultchar() {
		return this.defaultchar;
	}

	public void setDefaultchar(String defaultchar) {
		this.defaultchar = defaultchar;
	}

	public Date getDefaultdate() {
		return this.defaultdate;
	}

	public void setDefaultdate(Date defaultdate) {
		this.defaultdate = defaultdate;
	}

	public double getDefaultdouble() {
		return this.defaultdouble;
	}

	public void setDefaultdouble(double defaultdouble) {
		this.defaultdouble = defaultdouble;
	}

	public int getDefaultinteger() {
		return this.defaultinteger;
	}

	public void setDefaultinteger(int defaultinteger) {
		this.defaultinteger = defaultinteger;
	}

	public int getDefaultoption() {
		return this.defaultoption;
	}

	public void setDefaultoption(int defaultoption) {
		this.defaultoption = defaultoption;
	}

	public String getDefaulttext() {
		return this.defaulttext;
	}

	public void setDefaulttext(String defaulttext) {
		this.defaulttext = defaulttext;
	}

	public Date getMaxdate() {
		return this.maxdate;
	}

	public void setMaxdate(Date maxdate) {
		this.maxdate = maxdate;
	}

	public int getMaxdecimaldigit() {
		return this.maxdecimaldigit;
	}

	public void setMaxdecimaldigit(int maxdecimaldigit) {
		this.maxdecimaldigit = maxdecimaldigit;
	}

	public double getMaxdouble() {
		return this.maxdouble;
	}

	public void setMaxdouble(double maxdouble) {
		this.maxdouble = maxdouble;
	}

	public int getMaxinteger() {
		return this.maxinteger;
	}

	public void setMaxinteger(int maxinteger) {
		this.maxinteger = maxinteger;
	}

	public int getMaxoption() {
		return this.maxoption;
	}

	public void setMaxoption(int maxoption) {
		this.maxoption = maxoption;
	}

	public int getMaxtextlength() {
		return this.maxtextlength;
	}

	public void setMaxtextlength(int maxtextlength) {
		this.maxtextlength = maxtextlength;
	}

	public Date getMindate() {
		return this.mindate;
	}

	public void setMindate(Date mindate) {
		this.mindate = mindate;
	}

	public double getMindouble() {
		return this.mindouble;
	}

	public void setMindouble(double mindouble) {
		this.mindouble = mindouble;
	}

	public int getMininteger() {
		return this.mininteger;
	}

	public void setMininteger(int mininteger) {
		this.mininteger = mininteger;
	}

	public int getMinoption() {
		return this.minoption;
	}

	public void setMinoption(int minoption) {
		this.minoption = minoption;
	}

	public int getMintextlength() {
		return this.mintextlength;
	}

	public void setMintextlength(int mintextlength) {
		this.mintextlength = mintextlength;
	}

	public int getParametercode() {
		return this.parametercode;
	}

	public void setParametercode(int parametercode) {
		this.parametercode = parametercode;
	}

	public String getRequired() {
		return this.required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public int getValidvalue() {
		return this.validvalue;
	}

	public void setValidvalue(int validvalue) {
		this.validvalue = validvalue;
	}

	public Tfieldconfig getTfieldconfig() {
		return this.tfieldconfig;
	}

	public void setTfieldconfig(Tfieldconfig tfieldconfig) {
		this.tfieldconfig = tfieldconfig;
	}

}
