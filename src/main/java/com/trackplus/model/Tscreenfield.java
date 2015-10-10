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
 * The persistent class for the TSCREENFIELD database table.
 * 
 */
@Entity
@Table(name="TSCREENFIELD")
@TableGenerator(name="TSCREENFIELD_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_SCREENFIELD, allocationSize = 10)
public class Tscreenfield extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TSCREENFIELD_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	private int colindex;

	private int colspan;

	@Lob
	private String description;

	@Column(nullable=false, length=1)
	private String isempty;

	@Column(nullable=false)
	private int labelhalign;

	@Column(nullable=false)
	private int labelvalign;

	@Column(nullable=false, length=255)
	private String name;

	private int rowindex;

	private int rowspan;

	private int sortorder;

	@Column(length=36)
	private String tpuuid;

	@Column(nullable=false)
	private int valuehalign;

	@Column(nullable=false)
	private int valuevalign;

	//bi-directional many-to-one association to Tscreenpanel
	@ManyToOne
	@JoinColumn(name="PARENT", nullable=false)
	private Tscreenpanel tscreenpanel;

	//bi-directional many-to-one association to Tfield
	@ManyToOne
	@JoinColumn(name="FIELDKEY")
	private Tfield tfield;

	public Tscreenfield() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public int getColindex() {
		return this.colindex;
	}

	public void setColindex(int colindex) {
		this.colindex = colindex;
	}

	public int getColspan() {
		return this.colspan;
	}

	public void setColspan(int colspan) {
		this.colspan = colspan;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsempty() {
		return this.isempty;
	}

	public void setIsempty(String isempty) {
		this.isempty = isempty;
	}

	public int getLabelhalign() {
		return this.labelhalign;
	}

	public void setLabelhalign(int labelhalign) {
		this.labelhalign = labelhalign;
	}

	public int getLabelvalign() {
		return this.labelvalign;
	}

	public void setLabelvalign(int labelvalign) {
		this.labelvalign = labelvalign;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRowindex() {
		return this.rowindex;
	}

	public void setRowindex(int rowindex) {
		this.rowindex = rowindex;
	}

	public int getRowspan() {
		return this.rowspan;
	}

	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}

	public int getSortorder() {
		return this.sortorder;
	}

	public void setSortorder(int sortorder) {
		this.sortorder = sortorder;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public int getValuehalign() {
		return this.valuehalign;
	}

	public void setValuehalign(int valuehalign) {
		this.valuehalign = valuehalign;
	}

	public int getValuevalign() {
		return this.valuevalign;
	}

	public void setValuevalign(int valuevalign) {
		this.valuevalign = valuevalign;
	}

	public Tscreenpanel getTscreenpanel() {
		return this.tscreenpanel;
	}

	public void setTscreenpanel(Tscreenpanel tscreenpanel) {
		this.tscreenpanel = tscreenpanel;
	}

	public Tfield getTfield() {
		return this.tfield;
	}

	public void setTfield(Tfield tfield) {
		this.tfield = tfield;
	}

}
