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

package com.aurel.track.report.datasource;

import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.plugin.DatasourceDescriptor;

/**
 * Common interface for all datasources 
 * @author Tamas Ruff
 *
 */
public interface IPluggableDatasource {
	
	public static interface CONFIG_FILEDS {
		public static String IMPLEMENT_SERIALIZATION = "implementSerialization";
		static String CONFIG_CLASS = "configClass";
		static String CONFIG_DATA = "configData";
	}
	
	/**
	 * Context parameters by executing a report 
	 * @author Tamas Ruff
	 *
	 */
	public static interface CONTEXT_ATTRIBUTE {
		static String TEMPLATE_ID = "templateID";
		
		//for dynamic templates
		static String QUERY_TYPE = "queryType";
		static String QUERY_ID = "queryID";
		
		static String USE_PROJETC_SPECIFIC_ID = "useProjectSpecificID";
		static String FROM_ISSUE_NAVIGATOR = "fromIssueNavigator";
		static String WORKITEMIDS = "workItemIDs";
		
		//the ID of the dashboard the report was executed from
		//the value of the dashboardID
		static String DASHBOARD_ID = "dashboardID";
		//the project or release ID
		static String DASHBOARD_PROJECT_RELEASE_ID = "dashboardProjectReleaseID";
		//for templates get it from description.xml but for dynamically generated reports get from context
		static String EXPORT_FORMAT = "exportFormat";
	}

	/**
	 * Parameters submitted from the user by configuring the datasource 
	 * @author Tamas Ruff
	 *
	 */
	public static interface PARAMETER_NAME {
		public static String MAP_PREFIX = "params.";
		public static String NAME_SUFFIX = "Name";
		public static String OPTION_SUFFIX = "Options";
		
		/**
		 * Datasource type
		 */
		public static String DATASOURCETYPE = "datasourceType";
		public static String DATASOURCETYPE_OPTIONS = DATASOURCETYPE + OPTION_SUFFIX;
		public static String DATASOURCETYPE_NAME_FIELD = DATASOURCETYPE + NAME_SUFFIX;
		public static String DATASOURCETYPE_NAME_VALUE = MAP_PREFIX + "datasourceType";
		
		/**
		 * The selected project/release.
		 */
		public static String PROJECT_OR_RELEASE_ID = "projectOrReleaseID";
		public static String PROJECT_OR_RELEASE_LABEL = "projectOrReleaseLabel";
		public static String PROJECT_OR_RELEASE_NAME_FIELD = PROJECT_OR_RELEASE_ID + NAME_SUFFIX;
		public static String PROJECT_OR_RELEASE_NAME_VALUE = MAP_PREFIX + PROJECT_OR_RELEASE_ID;
		//used when the project/release picker is limited (not all "used" projects are allowed), for. ex. projects having special rights
        public static String PROJECT_RELEASE_TREE = "projectReleaseTree";
		
		/**
		 * The selected filter
		 */
		public static String FILTER_ID = "filterID";
		public static String FILTER_NAME_FIELD = FILTER_ID + NAME_SUFFIX;
		public static String FILTER_NAME_VALUE = MAP_PREFIX + FILTER_ID;
        public static String FILTER_TREE = "filterTree";
	}
	
	/**
	 * Many datasource types can be configured by either selecting one or more
	 * projects and releases, or by specifying a query filter expression or
	 * reference.
	 *
	 */
	public static interface DATASOURCE_TYPE {
		/**
		 * This datasource is configured via a project or release selector.
		 */
		static int PROJECT_RELEASE = 1;
		/**
		 * This datasource is configured via a query filter.
		 */
		static int FILTER = 2;
	}
	
	/**
	 * Gets the data source object (a Document object in this case) retrieved using the parameters settings
	 * @param params
	 * @param datasourceDescriptor
	 * @param contextMap
	 * @param templateDescriptionMap
	 * @param templateID
	 * @param personBean
	 * @param locale
	 * @return
	 */
	Object getDatasource(Map<String, String[]> params, DatasourceDescriptor datasourceDescriptor,
			Map<String, Object> contextMap, Map<String, Object> templateDescriptionMap,
			Integer templateID, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException;

	/**
	 * Gets the extra parameters from the datasource
	 * @param params
	 * @param datasourceDescriptor
	 * @param contextMap
	 * @param templateDescriptionMap
	 * @param templateID
	 * @param personBean
	 * @param locale
	 * @return
	 */
	Map<String, Object> getJasperReportParameters(Map<String, String[]> params, DatasourceDescriptor datasourceDescriptor,
			Map<String, Object> contextMap, Map<String, Object> templateDescriptionMap,
			Integer templateID, TPersonBean personBean, Locale locale);
	
	/**
	 * Serializes the raw data source object into an OutputStream (typically an XML file).
	 * <ul><li> 
	 * For jar templates (freemarker) it might be not required to serialize the
	 * datasource object because it is a proprietary object built specifically 
	 * for that template.</li>
	 * <li>For zip templates (jasper, fop, etc.) it is typically required to take the 
	 * serialized raw datasource as a file and use it as data source by editing 
	 * the template file using a template editing tool.</li>
	 * </ul>
	 * If the serialized raw data is not needed then just return null, but then implementSerialization() should also return false
	 * @param outputStream typically the OutputStream of the HttpResponse object 
	 * @param datasource typically DOM Document object or any other proprietary object
	 * @return typically an XML file
	 */
	void serializeDatasource(OutputStream outputStream, Object datasource) throws TooManyItemsToLoadException;


	/**
	 * Whether the getSerializedDatasource(OutputStream outputStream, Object datasource)
	 * returns null or not
	 * @return
	 */
	boolean implementSerialization();
		
	/**
	 * Prepares a map for rendering the config page
	 * @param templateID
	 * @param datasourceDescriptor
	 * @param contextMap
	 * @param personBean
	 * @param locale 
	 * @return
	 */
	String prepareParameters(Integer templateID,
			DatasourceDescriptor datasourceDescriptor, Map<String, Object> contextMap,
			TPersonBean personBean, Locale locale);

	
	/**
	 * Refreshing of some parameters through ajax: only a part of the parameters should be recalculated 
	 * @param params
	 * @param templateID
	 * @param datasourceDescriptor
	 * @param contextMap
	 * @param personBean
	 * @param locale 
	 * @return
	 */
	String refreshParameters(Map<String, String[]> params, Integer templateID,
			DatasourceDescriptor datasourceDescriptor, Map<String, Object> contextMap,
			TPersonBean personBean, Locale locale);
}
