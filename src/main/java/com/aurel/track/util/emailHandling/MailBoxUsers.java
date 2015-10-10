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

package com.aurel.track.util.emailHandling;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.aurel.track.util.emailHandling.MailBoxUsers;

/**
 * Synchronized set of mailbo	x user e-mails to avoid infinite cycle of e-mailing 
 * @author Tamas
 *
 */
public class MailBoxUsers {

	private static Set<String> mailboxUsersSet = Collections.synchronizedSet(new HashSet<String>());

	public static Set<String> getMailboxUsersSet() {
		return mailboxUsersSet;
	}

	public static void setMailboxUsersSet(Set<String> mailboxUsersSet) {
		MailBoxUsers.mailboxUsersSet = mailboxUsersSet;
	}
	
	
}
