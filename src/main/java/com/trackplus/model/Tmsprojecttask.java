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
 * The persistent class for the TMSPROJECTTASK database table.
 * 
 */
@Entity
@Table(name="TMSPROJECTTASK")
@TableGenerator(name="TMSPROJECTTASK_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_MSPROJECTTASK, allocationSize = 10)
public class Tmsprojecttask extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TMSPROJECTTASK_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=100)
	private String actualduration;

	@Temporal(TemporalType.TIMESTAMP)
	private Date constraintdate;

	private int constrainttype;

	@Column(length=100)
	private String contact;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deadline;

	@Column(length=100)
	private String duration;

	private int durationformat;

	@Column(length=1)
	private String estimated;

	@Column(length=1)
	private String milestone;

	@Column(length=100)
	private String outlinenumber;

	@Column(length=100)
	private String remainingduration;

	@Column(length=1)
	private String summary;

	@Column(nullable=false)
	private int tasktype;

	@Column(length=36)
	private String tpuuid;

	@Column(nullable=false)
	private int uniqueid;

	@Column(length=100)
	private String wbs;

	//bi-directional many-to-one association to Tworkitem
	@ManyToOne
	@JoinColumn(name="WORKITEM", nullable=false)
	private Tworkitem tworkitem;

	public Tmsprojecttask() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getActualduration() {
		return this.actualduration;
	}

	public void setActualduration(String actualduration) {
		this.actualduration = actualduration;
	}

	public Date getConstraintdate() {
		return this.constraintdate;
	}

	public void setConstraintdate(Date constraintdate) {
		this.constraintdate = constraintdate;
	}

	public int getConstrainttype() {
		return this.constrainttype;
	}

	public void setConstrainttype(int constrainttype) {
		this.constrainttype = constrainttype;
	}

	public String getContact() {
		return this.contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Date getDeadline() {
		return this.deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public String getDuration() {
		return this.duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public int getDurationformat() {
		return this.durationformat;
	}

	public void setDurationformat(int durationformat) {
		this.durationformat = durationformat;
	}

	public String getEstimated() {
		return this.estimated;
	}

	public void setEstimated(String estimated) {
		this.estimated = estimated;
	}

	public String getMilestone() {
		return this.milestone;
	}

	public void setMilestone(String milestone) {
		this.milestone = milestone;
	}

	public String getOutlinenumber() {
		return this.outlinenumber;
	}

	public void setOutlinenumber(String outlinenumber) {
		this.outlinenumber = outlinenumber;
	}

	public String getRemainingduration() {
		return this.remainingduration;
	}

	public void setRemainingduration(String remainingduration) {
		this.remainingduration = remainingduration;
	}

	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getTasktype() {
		return this.tasktype;
	}

	public void setTasktype(int tasktype) {
		this.tasktype = tasktype;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public int getUniqueid() {
		return this.uniqueid;
	}

	public void setUniqueid(int uniqueid) {
		this.uniqueid = uniqueid;
	}

	public String getWbs() {
		return this.wbs;
	}

	public void setWbs(String wbs) {
		this.wbs = wbs;
	}

	public Tworkitem getTworkitem() {
		return this.tworkitem;
	}

	public void setTworkitem(Tworkitem tworkitem) {
		this.tworkitem = tworkitem;
	}

}
