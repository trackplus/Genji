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

package com.aurel.track.admin.projectCopy;

import java.util.Iterator;
import java.util.List;

import com.aurel.track.json.JSONUtility;

public class ProjectCopyJSON {

	/**
	 * JSON field names for bulk expression
	 * 
	 * @author Tamas
	 * 
	 */
	private static interface PROJECT_COPY_JSON_FIELDS {
		String PROJECT_NAME = "projectName";
		String SHOW_AS_SIBLING = "showAsSibling";
		String SHOW_COPY_SUBPROJECTS = "showCopySubprojects";
		String SHOW_COPY_ITEMS = "showCopyItems";
		String SHOW_COPY_RELEASES = "showCopyReleases";
		String ENTITY_NAME = "entityName"; // the name of the select field checkbox
		String ENTITY_LABEL = "entityLabel";
		String ASSOCIATED_ENTITIES = "associatedEntities";
		String CUSTOM_LISTS = "customLists";
	}

	/**
	 * Gets the JSON string for a ProjectCopyExpression list
	 * 
	 * @param associatedEntitiesList
	 * @return
	 */
	public static String getProjectCopyExpressionListJSON(String projectName,
			List<ProjectCopyExpression> associatedEntitiesList, List<ProjectCopyExpression> customListsList, boolean showAsSibling,
			boolean showCopySubprojects, boolean showCopyReleases,
			boolean showCopyItems) {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendBooleanValue(stringBuilder,
				JSONUtility.JSON_FIELDS.SUCCESS, true);
		stringBuilder.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(stringBuilder,
				PROJECT_COPY_JSON_FIELDS.PROJECT_NAME, projectName);
		JSONUtility.appendBooleanValue(stringBuilder,
				PROJECT_COPY_JSON_FIELDS.SHOW_AS_SIBLING, showAsSibling);
		JSONUtility.appendBooleanValue(stringBuilder,
				PROJECT_COPY_JSON_FIELDS.SHOW_COPY_SUBPROJECTS,
				showCopySubprojects);
		JSONUtility.appendBooleanValue(stringBuilder,
				PROJECT_COPY_JSON_FIELDS.SHOW_COPY_ITEMS, showCopyItems);
		JSONUtility.appendBooleanValue(stringBuilder,
				PROJECT_COPY_JSON_FIELDS.SHOW_COPY_RELEASES, showCopyReleases);
		stringBuilder.append(PROJECT_COPY_JSON_FIELDS.ASSOCIATED_ENTITIES)
				.append(":[");
		if ((associatedEntitiesList != null)
				&& (associatedEntitiesList.size() > 0)) {
			for (final Iterator<ProjectCopyExpression> iterator = associatedEntitiesList
					.iterator(); iterator.hasNext();) {
				final ProjectCopyExpression projectCopyExpression = iterator
						.next();
				stringBuilder.append(ProjectCopyJSON
						.getProjectCopyExpressionJSON(projectCopyExpression));
				if (iterator.hasNext()) {
					stringBuilder.append(",");
				}
			}
		}
		stringBuilder.append("]");
		if ((customListsList != null) && (customListsList.size() > 0)) {
			stringBuilder.append(",")
					.append(PROJECT_COPY_JSON_FIELDS.CUSTOM_LISTS).append(":[");
			for (final Iterator<ProjectCopyExpression> iterator = customListsList
					.iterator(); iterator.hasNext();) {
				final ProjectCopyExpression projectCopyExpression = iterator
						.next();
				stringBuilder.append(ProjectCopyJSON
						.getProjectCopyExpressionJSON(projectCopyExpression));
				if (iterator.hasNext()) {
					stringBuilder.append(",");
				}
			}
			stringBuilder.append("]");
		}
		stringBuilder.append("}");
		stringBuilder.append("}");
		return stringBuilder.toString();
	}

	/**
	 * Gets the JSON string for a ProjectCopyExpression
	 * 
	 * @return
	 */
	private static String getProjectCopyExpressionJSON(
			ProjectCopyExpression projectCopyExpression) {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendStringValue(stringBuilder,
				PROJECT_COPY_JSON_FIELDS.ENTITY_LABEL,
				projectCopyExpression.getEntityLabel());
		JSONUtility.appendStringValue(stringBuilder,
				PROJECT_COPY_JSON_FIELDS.ENTITY_NAME,
				projectCopyExpression.getEntityName(), true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
}
