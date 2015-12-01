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

package com.aurel.track.linkType;

/**
 * Ms Project specific item link data
 * @author Tamas
 *
 */
public class MsProjectLinkSpecificData implements ItemLinkSpecificData {
	private Integer dependencyType;
	private Double linkLag;
	private Integer linkLagFormat;
	
	public Integer getDependencyType() {
		return dependencyType;
	}
	public void setDependencyType(Integer dependencyType) {
		this.dependencyType = dependencyType;
	}
	public Double getLinkLag() {
		return linkLag;
	}
	public void setLinkLag(Double linkLag) {
		this.linkLag = linkLag;
	}
	public Integer getLinkLagFormat() {
		return linkLagFormat;
	}
	public void setLinkLagFormat(Integer linkLagFormat) {
		this.linkLagFormat = linkLagFormat;
	}
	
	

}
