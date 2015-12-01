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

package com.aurel.track.report.datasource.releaseNotes;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.report.execute.ReportBeansToXML;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.persist.TListType;
import com.aurel.track.plugin.DatasourceDescriptor;
import com.aurel.track.report.dashboard.ReleaseNoteWrapper;
import com.aurel.track.report.datasource.BasicDatasource;
import com.aurel.track.util.StringArrayParameterUtils;

public class ReleaseNotesDatasource extends BasicDatasource {
	
	private static final Logger LOGGER = LogManager.getLogger(ReleaseNotesDatasource.class);
	
	protected static interface CONFIGURATION_PARAMETERS {
		static String STATUSES = "statuses";
		static String SELECTED_STATUS = "selectedStatus";
		static String ISSUETYPES = "issueTypes";
		static String SELECTED_IT = "selectedIt";
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
	 * @throws TooManyItemsToLoadException 
	 */
	@Override
	public Object getDatasource(Map<String, String[]> parameters, DatasourceDescriptor datasourceDescriptor,
			Map<String, Object> contextMap, Map<String, Object> templateDescriptionMap,
			Integer templateID, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException {
		List<TListType> selectedIssueTypes = new ArrayList<TListType>();
		Integer selectedProjectReleaseID = StringArrayParameterUtils.parseIntegerValue(parameters, 
				PARAMETER_NAME.PROJECT_OR_RELEASE_ID);
		TReleaseBean release = LookupContainer.getReleaseBean(selectedProjectReleaseID);
		FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(selectedProjectReleaseID, true, false, true);
		List<TWorkItemBean> workItemBeans = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, personBean, locale, false);
		if (workItemBeans==null || workItemBeans.isEmpty()) {
			return null;
		}
		// Group by issue type...
		List<ReleaseNoteWrapper> rw = getReleaseNotesForSingleRelease(workItemBeans, locale, selectedIssueTypes);
		return ReleaseNotesBL.convertToDOM(release, rw, locale, personBean.getFullName());
	}
	
	/**
	 * Serializes the datasource in an XML file
	 */
	@Override
	public void serializeDatasource(OutputStream outputStream,
			Object datasource) {
		ReportBeansToXML.convertToXml(outputStream, (Document)datasource);
	}
	
	
	/**
	 * Prepares a map for rendering the config page
	 * @param params
	 * @param templateID
	 * @param datasourceDescriptor
	 * @param contextMap
	 * @param personBean
	 * @param locale 
	 * @return
	 */
	@Override
	public String prepareParameters(Integer templateID,
			DatasourceDescriptor datasourceDescriptor, Map<String, Object> contextMap,
			TPersonBean personBean, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder();
		
		//parameters.put(DatasourceCommonBL.PARAMETER_NAME.locale, locale);
		// parameters.put(DatasourceCommonBL.PARAMETER_NAME.bundleName, bundleName);		
		
		//parameters.put(DatasourceCommonBL.PARAMETER_NAME.PROJECTS, DatasourceCommonBL.getProjectsAndReleases(personBean.getObjectID()));
		/*parameters.put(PARAMETER_NAME.PROJECT_OR_RELEASE_ID, StringArrayParameterUtils.parseIntegerValue(parameters, 
				PARAMETER_NAME.PROJECT_OR_RELEASE_ID));
		
		parameters.put(ReleaseNotesDatasource.CONFIGURATION_PARAMETERS.STATUSES, 
				LocalizeUtil.localizeDropDownList(StatusBL.loadClosedStates(), locale));*/

		//IssueTypeDAO itdao = DAOFactory.getFactory().getIssueTypeDAO();
		//List<TProjectBean> projectList = ProjectBL.loadProjectsWithReadIssueRight(personBean.getObjectID()); //TProjectPeer.loadActiveByUser(personBean.getObjectID());
		//Integer[] projects = getObjectIds(projectList); 
		//List<TListTypeBean> usedIssueTypes = itdao.loadUsedByProject(projects);
		//parameters.put(ReleaseNotesDatasource.CONFIGURATION_PARAMETERS.ISSUETYPES, LocalizeUtil.localizeDropDownList(usedIssueTypes, locale));
		return stringBuilder.toString();
	
	}
	
	
	
	/**
	 * Function to group the workitems by issue types.
	 * @param items
	 * @return List with ReleaseNoteWrapper elements, which themselves contain a list with
	 * issues for a specific issue type.
	 */
	public static List<ReleaseNoteWrapper> getReleaseNotesForSingleRelease(List<TWorkItemBean> items, Locale locale, List selectedIssueTypes) {
		HashMap<Integer, ReleaseNoteWrapper> issueTypeMap = new HashMap<Integer, ReleaseNoteWrapper>();
		if (items!=null) {
			Iterator<TWorkItemBean> it = items.iterator();
			//int i = -1;
			while (it.hasNext()) {
				TWorkItemBean itemBean = it.next();
				Integer listTypeID=itemBean.getListTypeID();
				if (selectedIssueTypes.isEmpty()||selectedIssueTypes.contains(listTypeID.toString())) {
					//Integer sortorder = issueTypeDAO.loadByPrimaryKey(itemBean.getListTypeID()).getSortorder();
					/*if (sortorder == null || sortorder.equals(0)) {
						sortorder = --i; // force  being unique
					}*/
					if (issueTypeMap.containsKey(listTypeID)) {
						issueTypeMap.get(listTypeID).getWorkItems().add(itemBean);
					}
					else {
						ReleaseNoteWrapper relwrap = new ReleaseNoteWrapper();
						TListTypeBean listTypeBean = LookupContainer.getItemTypeBean(listTypeID, locale);
						if (listTypeBean!=null) {
							listTypeBean.setLabel(listTypeBean.getLabel());
						}
						relwrap.setIssueType(listTypeBean);
						relwrap.getWorkItems().add(itemBean);
						issueTypeMap.put(listTypeID, relwrap);
					}
				}
			}
		}
		Map<Integer, ReleaseNoteWrapper> sortedMap = new TreeMap<Integer, ReleaseNoteWrapper>(issueTypeMap);
		return new ArrayList<ReleaseNoteWrapper>(sortedMap.values());
	}
	
}
