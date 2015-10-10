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

import java.sql.Timestamp;
import java.util.Date;


/**
 * The persistent class for the TCOST database table.
 * 
 */
@Entity
@Table(name="TCOST")
@TableGenerator(name="TCOST_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_COST, allocationSize = 10)

public class Tcost extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TCOST_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	private double cost;

	@Column(length=255)
	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	private Date effortdate;

	private double effortvalue;

	private double hours;

	@Temporal(TemporalType.TIMESTAMP)
	private Date invoicedate;

	@Column(length=255)
	private String invoicenumber;

	@Column(length=255)
	private String invoicepath;

	@Column(nullable=false)
	private Timestamp lastedit;

	@Lob
	private String moreprops;

	@Column(length=255)
	private String subject;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Taccount
	@ManyToOne
	@JoinColumn(name="ACCOUNT")
	private Taccount taccount;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="PERSON")
	private Tperson tperson;

	//bi-directional many-to-one association to Tworkitem
	@ManyToOne
	@JoinColumn(name="WORKITEM")
	private Tworkitem tworkitem;


	public Tcost() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public double getCost() {
		return this.cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEffortdate() {
		return this.effortdate;
	}

	public void setEffortdate(Date effortdate) {
		this.effortdate = effortdate;
	}

	public double getEffortvalue() {
		return this.effortvalue;
	}

	public void setEffortvalue(double effortvalue) {
		this.effortvalue = effortvalue;
	}

	public double getHours() {
		return this.hours;
	}

	public void setHours(double hours) {
		this.hours = hours;
	}

	public Date getInvoicedate() {
		return this.invoicedate;
	}

	public void setInvoicedate(Date invoicedate) {
		this.invoicedate = invoicedate;
	}

	public String getInvoicenumber() {
		return this.invoicenumber;
	}

	public void setInvoicenumber(String invoicenumber) {
		this.invoicenumber = invoicenumber;
	}

	public String getInvoicepath() {
		return this.invoicepath;
	}

	public void setInvoicepath(String invoicepath) {
		this.invoicepath = invoicepath;
	}

	public Timestamp getLastedit() {
		return this.lastedit;
	}

	public void setLastedit(Timestamp lastedit) {
		this.lastedit = lastedit;
	}

	public String getMoreprops() {
		return this.moreprops;
	}

	public void setMoreprops(String moreprops) {
		this.moreprops = moreprops;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Taccount getTaccount() {
		return this.taccount;
	}

	public void setTaccount(Taccount taccount) {
		this.taccount = taccount;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

	public Tworkitem getTworkitem() {
		return this.tworkitem;
	}

	public void setTworkitem(Tworkitem tworkitem) {
		this.tworkitem = tworkitem;
	}


}
