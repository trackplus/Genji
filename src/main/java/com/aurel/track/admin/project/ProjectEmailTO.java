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

package com.aurel.track.admin.project;


import com.aurel.track.util.emailHandling.IncomingMailSettings;

/**
 * Transfer object used for project specific incoming email (POP3, IMAP) configuration.
 * @author Tamas Ruff
 */
public class ProjectEmailTO extends IncomingMailSettings {
	
	private boolean enabled;
	private String projectFromEmail;
	private String projectFromEmailName;
	private boolean sendFromProjectEmail;
	private boolean sendFromProjectAsReplayTo;

	public String getProjectFromEmail() {
		return projectFromEmail;
	}

	public void setProjectFromEmail(String projectFromEmail) {
		this.projectFromEmail = projectFromEmail;
	}

	public String getProjectFromEmailName() {
		return projectFromEmailName;
	}

	public void setProjectFromEmailName(String projectFromEmailName) {
		this.projectFromEmailName = projectFromEmailName;
	}

	public boolean isSendFromProjectEmail() {
		return sendFromProjectEmail;
	}

	public void setSendFromProjectEmail(boolean sendFromProjectEmail) {
		this.sendFromProjectEmail = sendFromProjectEmail;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isSendFromProjectAsReplayTo() {
		return sendFromProjectAsReplayTo;
	}

	public void setSendFromProjectAsReplayTo(boolean sendFromProjectAsReplayTo) {
		this.sendFromProjectAsReplayTo = sendFromProjectAsReplayTo;
	}
}
