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
 * The persistent class for the TFIELDCHANGE database table.
 * 
 */
@Entity
@Table(name="TFIELDCHANGE")
@TableGenerator(name="TFIELDCHANGE_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_FIELDCHANGE, allocationSize = 10)
public class Tfieldchange extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TFIELDCHANGE_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(nullable=false)
	private int fieldkey;

	@Column(length=1)
	private String newcharactervalue;

	@Temporal(TemporalType.TIMESTAMP)
	private Date newdatevalue;

	private double newdoublevalue;

	private int newintegervalue;

	@Lob
	private String newlongtextvalue;

	private int newsystemoptionid;

	@Column(length=255)
	private String newtextvalue;

	@Column(length=1)
	private String oldcharactervalue;

	@Temporal(TemporalType.TIMESTAMP)
	private Date olddatevalue;

	private double olddoublevalue;

	private int oldintegervalue;

	@Lob
	private String oldlongtextvalue;

	private int oldsystemoptionid;

	@Column(length=255)
	private String oldtextvalue;

	private int parametercode;

	private int systemoptiontype;

	private int timesedited;

	@Column(length=36)
	private String tpuuid;

	private int validvalue;

	//bi-directional many-to-one association to Thistorytransaction
	@ManyToOne
	@JoinColumn(name="HISTORYTRANSACTION", nullable=false)
	private Thistorytransaction thistorytransaction;

	//bi-directional many-to-one association to Tfieldchange
	@ManyToOne
	@JoinColumn(name="PARENTCOMMENT")
	private Tfieldchange tfieldchange;

	//bi-directional many-to-one association to Tfieldchange
	@OneToMany(mappedBy="tfieldchange")
	private List<Tfieldchange> tfieldchanges;

	//bi-directional many-to-one association to Toption
	@ManyToOne
	@JoinColumn(name="NEWCUSTOMOPTIONID")
	private Toption toption1;

	//bi-directional many-to-one association to Toption
	@ManyToOne
	@JoinColumn(name="OLDCUSTOMOPTIONID")
	private Toption toption2;

	public Tfieldchange() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public int getFieldkey() {
		return this.fieldkey;
	}

	public void setFieldkey(int fieldkey) {
		this.fieldkey = fieldkey;
	}

	public String getNewcharactervalue() {
		return this.newcharactervalue;
	}

	public void setNewcharactervalue(String newcharactervalue) {
		this.newcharactervalue = newcharactervalue;
	}

	public Date getNewdatevalue() {
		return this.newdatevalue;
	}

	public void setNewdatevalue(Date newdatevalue) {
		this.newdatevalue = newdatevalue;
	}

	public double getNewdoublevalue() {
		return this.newdoublevalue;
	}

	public void setNewdoublevalue(double newdoublevalue) {
		this.newdoublevalue = newdoublevalue;
	}

	public int getNewintegervalue() {
		return this.newintegervalue;
	}

	public void setNewintegervalue(int newintegervalue) {
		this.newintegervalue = newintegervalue;
	}

	public String getNewlongtextvalue() {
		return this.newlongtextvalue;
	}

	public void setNewlongtextvalue(String newlongtextvalue) {
		this.newlongtextvalue = newlongtextvalue;
	}

	public int getNewsystemoptionid() {
		return this.newsystemoptionid;
	}

	public void setNewsystemoptionid(int newsystemoptionid) {
		this.newsystemoptionid = newsystemoptionid;
	}

	public String getNewtextvalue() {
		return this.newtextvalue;
	}

	public void setNewtextvalue(String newtextvalue) {
		this.newtextvalue = newtextvalue;
	}

	public String getOldcharactervalue() {
		return this.oldcharactervalue;
	}

	public void setOldcharactervalue(String oldcharactervalue) {
		this.oldcharactervalue = oldcharactervalue;
	}

	public Date getOlddatevalue() {
		return this.olddatevalue;
	}

	public void setOlddatevalue(Date olddatevalue) {
		this.olddatevalue = olddatevalue;
	}

	public double getOlddoublevalue() {
		return this.olddoublevalue;
	}

	public void setOlddoublevalue(double olddoublevalue) {
		this.olddoublevalue = olddoublevalue;
	}

	public int getOldintegervalue() {
		return this.oldintegervalue;
	}

	public void setOldintegervalue(int oldintegervalue) {
		this.oldintegervalue = oldintegervalue;
	}

	public String getOldlongtextvalue() {
		return this.oldlongtextvalue;
	}

	public void setOldlongtextvalue(String oldlongtextvalue) {
		this.oldlongtextvalue = oldlongtextvalue;
	}

	public int getOldsystemoptionid() {
		return this.oldsystemoptionid;
	}

	public void setOldsystemoptionid(int oldsystemoptionid) {
		this.oldsystemoptionid = oldsystemoptionid;
	}

	public String getOldtextvalue() {
		return this.oldtextvalue;
	}

	public void setOldtextvalue(String oldtextvalue) {
		this.oldtextvalue = oldtextvalue;
	}

	public int getParametercode() {
		return this.parametercode;
	}

	public void setParametercode(int parametercode) {
		this.parametercode = parametercode;
	}

	public int getSystemoptiontype() {
		return this.systemoptiontype;
	}

	public void setSystemoptiontype(int systemoptiontype) {
		this.systemoptiontype = systemoptiontype;
	}

	public int getTimesedited() {
		return this.timesedited;
	}

	public void setTimesedited(int timesedited) {
		this.timesedited = timesedited;
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

	public Thistorytransaction getThistorytransaction() {
		return this.thistorytransaction;
	}

	public void setThistorytransaction(Thistorytransaction thistorytransaction) {
		this.thistorytransaction = thistorytransaction;
	}

	public Tfieldchange getTfieldchange() {
		return this.tfieldchange;
	}

	public void setTfieldchange(Tfieldchange tfieldchange) {
		this.tfieldchange = tfieldchange;
	}

	public List<Tfieldchange> getTfieldchanges() {
		return this.tfieldchanges;
	}

	public void setTfieldchanges(List<Tfieldchange> tfieldchanges) {
		this.tfieldchanges = tfieldchanges;
	}

	public Tfieldchange addTfieldchange(Tfieldchange tfieldchange) {
		getTfieldchanges().add(tfieldchange);
		tfieldchange.setTfieldchange(this);

		return tfieldchange;
	}

	public Tfieldchange removeTfieldchange(Tfieldchange tfieldchange) {
		getTfieldchanges().remove(tfieldchange);
		tfieldchange.setTfieldchange(null);

		return tfieldchange;
	}

	public Toption getToption1() {
		return this.toption1;
	}

	public void setToption1(Toption toption1) {
		this.toption1 = toption1;
	}

	public Toption getToption2() {
		return this.toption2;
	}

	public void setToption2(Toption toption2) {
		this.toption2 = toption2;
	}

}
