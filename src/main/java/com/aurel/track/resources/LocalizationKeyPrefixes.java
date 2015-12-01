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


package com.aurel.track.resources;

public interface LocalizationKeyPrefixes {
	
	public static String FIELD = "field.";
	public static String FIELD_LABEL_KEY_PREFIX = FIELD + "label";
	public static String FIELD_TOOLTIP_KEY_PREFIX = FIELD + "tooltip";
	public static String FIELD_SYSTEMSELECT_KEY_PREFIX = FIELD + "systemSelect.";
	public static String FIELD_CUSTOMSELECT_KEY_PREFIX = FIELD + "customSelect.";

	
    public static String ACTION_LABEL_KEY_PREFIX = "action.label.";
    
    //the prefix for time units by efforts
	public static String TIME_UNIT_KEY_PREFIX = "item.tabs.worklogSummary.timeUnit.";
		
	//prefix for roles
	public static String ROLE_KEYPREFIX = "role.";
	
	//prefix for baskets
	public static String BASKET_KEYPREFIX = "basket.label.";
	
	public static String SYSTEM_STATUS_KEY_PREFIX = "systemStatus.";
	
	//prefix for linkType filter result superset expressions 
	public static String LINKTYPE_SUPERSET = "linkType.filterSuperset.";
	//prefix for linkType name
	public static String LINKTYPE_NAME = "linkType.name.";
	public static String FILTER_LABEL_PREFIX = "filter.label.";
	public static String FILTER_TOOLTIP_PREFIX = "filter.tooltip.";
	public static String REPORT_LABEL_PREFIX = "report.label.";
	public static String REPORT_TOOLTIP_PREFIX = "report.tooltip.";
	public static String FILTER_CATEGORY_LABEL_PREFIX = "filterCategory.label.";
	public static String REPORT_CATEGORY_LABEL_PREFIX = "reportCategory.label.";
	public static String COCKPIT_TEMPLATE_NAME_PREFIX = "cockpitTemplate.name.";
	public static String COCKPIT_TEMPLATE_DESCRIPTION_PREFIX = "cockpitTemplate.description.";
	
	public static String USER_LEVEL = "userLevel.";
	
}
