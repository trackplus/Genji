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

package com.aurel.track.admin.customize.notify;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.tree.TreeNodeBaseTO;

public class NotifyBL {
	
	interface AUTOMAIL_PARTS {
		static final int AUTOMAIL_ASSIGNMENT = 1;
		static final int AUTOMAIL_TRIGGER = 2;
		static final int AUTOMAIL_FILTER = 3;
		
	}
	
	interface AUTOMAIL_ICONS {
		static final String AUTOMAIL_ASSIGNMENT = "automailc-ticon";
		static final String AUTOMAIL_TRIGGER = "automailt-ticon";
		static final String AUTOMAIL_FILTER = "automailf-ticon";
	}
	
	interface AUTOMAIL_LABELS {
		static final String AUTOMAIL_ASSIGNMENT = "menu.admin.myProfile.automailAssignments";
		static final String AUTOMAIL_TRIGGER = "menu.admin.myProfile.automailTrigger";
		static final String AUTOMAIL_FILTER = "menu.admin.myProfile.automailConditions";
	}
	
	/**
	 * Get the children of node having the given id
	 * @param locale
	 * @return
	 */
	public static List<TreeNodeBaseTO> getChildren(Locale locale){
		List<TreeNodeBaseTO> result = new LinkedList<TreeNodeBaseTO>();
		result.add(new TreeNodeBaseTO(String.valueOf(AUTOMAIL_PARTS.AUTOMAIL_ASSIGNMENT),
				LocalizeUtil.getLocalizedTextFromApplicationResources(AUTOMAIL_LABELS.AUTOMAIL_ASSIGNMENT, locale), AUTOMAIL_ICONS.AUTOMAIL_ASSIGNMENT, true));
		result.add(new TreeNodeBaseTO(String.valueOf(AUTOMAIL_PARTS.AUTOMAIL_TRIGGER),
				LocalizeUtil.getLocalizedTextFromApplicationResources(AUTOMAIL_LABELS.AUTOMAIL_TRIGGER, locale), AUTOMAIL_ICONS.AUTOMAIL_TRIGGER, true));
		result.add(new TreeNodeBaseTO(String.valueOf(AUTOMAIL_PARTS.AUTOMAIL_FILTER),
				LocalizeUtil.getLocalizedTextFromApplicationResources(AUTOMAIL_LABELS.AUTOMAIL_FILTER, locale), AUTOMAIL_ICONS.AUTOMAIL_FILTER, true));		
		return result;
	}
}
