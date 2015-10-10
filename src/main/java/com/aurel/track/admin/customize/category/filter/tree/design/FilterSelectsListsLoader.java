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


package com.aurel.track.admin.customize.category.filter.tree.design;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.accessControl.AccessControlBL;
import com.aurel.track.accessControl.AccessBeans.AccessFlagIndexes;
import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.role.FieldsRestrictionsToRoleBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherValue;
import com.aurel.track.fieldType.runtime.matchers.design.MatcherDatasourceContext;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.fieldType.runtime.system.select.SystemResponsibleRT;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.consInf.RaciRole;
import com.aurel.track.linkType.LinkTypeBL;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.TreeNode;

/**
 * Business logic class for loading/reloading the dropdowns of filter selects
 * @author Tamas Ruff
 *
 */
public class FilterSelectsListsLoader {
	
	public static List<ILabelBean> getProjects(Integer person) {
		return (List)ProjectBL.loadUsedProjectsFlat(person);
	}
	
	public static boolean isAdmin(TPersonBean personBean) {
		return personBean.isSys() || AccessControlBL.hasPersonRightInAnyProjectWithStatusFlag(
				personBean.getObjectID(), new int[] {AccessFlagIndexes.PROJECTADMIN},
				new int[] {TSystemStateBean.STATEFLAGS.ACTIVE, TSystemStateBean.STATEFLAGS.INACTIVE});
	}
	
	/**
	 * Whether the person has viewWatcherRight in any of the projects 
	 * @param personBean
	 * @return
	 */
	public static boolean hasViewWatcherRightInAnyProject(TPersonBean personBean) {
		if (personBean.isSys()) {
			return true;
		} else {
			List<Integer> fieldIDs = new LinkedList<Integer>();
			fieldIDs.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.WATCHERS);
			Map<Integer, Integer> fieldRestrictions = AccessBeans.getFieldRestrictions(personBean.getObjectID(), null, null, fieldIDs, false);
			return fieldRestrictions == null || fieldRestrictions.isEmpty();
		}
	}
	
	/**
	 * Whether the person has viewWatcherRight in all selected projects
	 * Otherwise it could find out indirectly whether a person is watcher for a certain issue 
	 * (not by editing the issue but by the fact that it is present in the filter's result) 
	 * @param personID
	 * @param projects
	 * @return
	 */
	private static boolean hatViewWatcherRightInAllProjects(Integer personID, Integer[] projects) {
		if (projects==null || projects.length==0) {
			return false;
		}
		for (int i = 0; i < projects.length; i++) {
			Integer projectID = projects[i];
			if (projectID!=null && !projectID.equals(MatcherContext.PARAMETER)) {
				List<Integer> fieldIDs = new LinkedList<Integer>();
				fieldIDs.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.WATCHERS);
				Map<Integer, Integer> fieldRestrictions = AccessBeans.getFieldRestrictions(personID, projectID, null, fieldIDs, false);
				if (fieldRestrictions!=null && !fieldRestrictions.isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Gets the watchers
	 * @param projects
	 * @param person
	 * @param withParameter
	 * @param locale
	 * @return
	 */
	public static List<ILabelBean> getConsultantsInformants(Integer[] projects, Integer[] ancestorProjects, Integer person, boolean withParameter, String raciRole, Locale locale) {
		List<ILabelBean> consultantInformantList = null;
		if (hatViewWatcherRightInAllProjects(person, projects)) {
			int[] arrRights = null;
			//get the persons who have consultants/informants role in any selected project
			if (raciRole==null) {
				//both consulted and informed
				arrRights = new int[] { AccessFlagIndexes.CONSULTANT, 
						AccessFlagIndexes.INFORMANT, AccessFlagIndexes.PROJECTADMIN };
			} else {
				if (RaciRole.CONSULTANT.equals(raciRole)) {
					arrRights = new int[] { AccessFlagIndexes.CONSULTANT, 
							AccessFlagIndexes.PROJECTADMIN };
				} else {
					if (RaciRole.INFORMANT.equals(raciRole)) {
						arrRights = new int[] { AccessFlagIndexes.INFORMANT, 
								AccessFlagIndexes.PROJECTADMIN };
					}
				}
			}
			Set<Integer> consultantInformantByRoleSet = AccessBeans.getPersonSetByProjectsRights(ancestorProjects, arrRights);
			List<TPersonBean> consultantInformantByRole = PersonBL.getDirectAndIndirectPersonsAndGroups(
					GeneralUtils.createIntegerListFromCollection(consultantInformantByRoleSet), true, true, null);
			//get the persons who are consultants/informants in existing issues: they are not necessary 
			//a subset of the persons with consultants/informants role because it could happen that somebody
			//is a consultants/informants on an issue but his consultants/informants role was revoked for the project 
			List<TPersonBean> consultantInformantInIssues = PersonBL.loadUsedConsultantsInformantsByProjects(projects, raciRole);
			Set<TPersonBean> consultantsInformants = new TreeSet<TPersonBean>();
			consultantsInformants.addAll(consultantInformantByRole);
			consultantsInformants.addAll(consultantInformantInIssues);
			consultantInformantList = GeneralUtils.createListFromSet(consultantsInformants);
		} else {
			//the person has no view watchers right in all selected projects
			consultantInformantList = new ArrayList<ILabelBean>();
		}
		if (withParameter) {
			consultantInformantList.add(new SystemResponsibleRT().getLabelBean(MatcherContext.PARAMETER, locale));
		}
		//add the symbolic user as the first entry in the list 
		consultantInformantList.add(0, new SystemResponsibleRT().getLabelBean(MatcherContext.LOGGED_USER_SYMBOLIC, locale));
		return consultantInformantList;
	}
		
	
	/**
	 * Gets the release type selectors
	 * @param locale
	 * @return
	 */
	public static List<IntegerStringBean> getReleaseTypeSelectors(Locale locale) {
		List<IntegerStringBean> consultantInformantSelectorList = new ArrayList<IntegerStringBean>();
		consultantInformantSelectorList.add(
				new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.customize.queryFilter.opt.release.noticed", locale),
						Integer.valueOf(FilterUpperTO.RELEASE_SELECTOR.NOTICED)));
		consultantInformantSelectorList.add(
				new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.customize.queryFilter.opt.release.scheduled", locale),
						Integer.valueOf(FilterUpperTO.RELEASE_SELECTOR.SCHEDULED)));
		consultantInformantSelectorList.add(
				new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.customize.queryFilter.opt.release.noticedOrScheduled", locale),
					Integer.valueOf(FilterUpperTO.RELEASE_SELECTOR.NOTICED_OR_SCHEDULED)));
		return consultantInformantSelectorList;
	}
	
	/**
	 *  Gets the watcher selectors
	 */
	public static List<IntegerStringBean> getConsultantInformantTypeSelectors(Locale locale) {
		List<IntegerStringBean> consultantInformantSelectorList = new ArrayList<IntegerStringBean>();
		consultantInformantSelectorList.add(
				new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.customize.queryFilter.opt.watcher.consultant", locale),
					Integer.valueOf(FilterUpperTO.CONSINF_SELECTOR.CONSULTANT)));
		consultantInformantSelectorList.add(
				new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.customize.queryFilter.opt.watcher.informant", locale),
						Integer.valueOf(FilterUpperTO.CONSINF_SELECTOR.INFORMANT)));
		consultantInformantSelectorList.add(
				new IntegerStringBean(
						LocalizeUtil.getLocalizedTextFromApplicationResources(
								"admin.customize.queryFilter.opt.watcher.consOrInf", locale),
					Integer.valueOf(FilterUpperTO.CONSINF_SELECTOR.CONSULTANT_OR_INFORMANT)));
		return consultantInformantSelectorList;
	}
	
	/**
	 * Gets the archived options
	 * @param locale
	 * @return
	 */
	private static List<IntegerStringBean> getArchivedOptions(Locale locale) {
		List<IntegerStringBean> archivedValues = new ArrayList<IntegerStringBean>();
		archivedValues.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.queryFilter.opt.archived.exclude", locale), 
				Integer.valueOf(FilterUpperTO.ARCHIVED_FILTER.EXCLUDE_ARCHIVED)));
		archivedValues.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.queryFilter.opt.archived.inlude", locale), 
				Integer.valueOf(FilterUpperTO.ARCHIVED_FILTER.INCLUDE_ARCHIVED)));
		archivedValues.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.queryFilter.opt.archived.only", locale), 
				Integer.valueOf(FilterUpperTO.ARCHIVED_FILTER.ONLY_ARCHIVED)));
		return archivedValues;
	}
	
	/**
	 *  Gets the deleted options
	 * @param locale
	 * @return
	 */
	private static List<IntegerStringBean> getDeletedOptions(Locale locale) {
		List<IntegerStringBean> archivedValues = new ArrayList<IntegerStringBean>();
		archivedValues.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.queryFilter.opt.deleted.exclude", locale), 
				Integer.valueOf(FilterUpperTO.DELETED_FILTER.EXCLUDE_DELETED)));
		archivedValues.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.queryFilter.opt.deleted.inlude", locale), 
				Integer.valueOf(FilterUpperTO.DELETED_FILTER.INCLUDE_DELETED)));
		archivedValues.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.queryFilter.opt.deleted.only", locale), 
				Integer.valueOf(FilterUpperTO.DELETED_FILTER.ONLY_DELETED)));
		return archivedValues;
	}
	
	/**
	 * Get the system selects which could be loaded without specific code (exclude project and release)
	 * @return
	 */
	private static List<Integer> getSystemSelects() {
		List<Integer> systemSelects = new LinkedList<Integer>();
		systemSelects.add(SystemFields.INTEGER_ORIGINATOR);
		systemSelects.add(SystemFields.INTEGER_CHANGEDBY);
		systemSelects.add(SystemFields.INTEGER_MANAGER);
		systemSelects.add(SystemFields.INTEGER_RESPONSIBLE);
		systemSelects.add(SystemFields.INTEGER_ISSUETYPE);
		systemSelects.add(SystemFields.INTEGER_STATE);
		systemSelects.add(SystemFields.INTEGER_PRIORITY);
		systemSelects.add(SystemFields.INTEGER_SEVERITY);
		return systemSelects;
	}
	
	
	/**
	 * 
	 * 
	 * Adds the possible values, matchers etc.
	 * Depending on firstLoad preselects some values  
	 * Extra care should be taken for selecting the projects when firstLoad=false because 
	 * the other lists should be populated depending on the selected project
	 * The other selected fields might be invalid after a new loading 
	 * because the project list might change (for ex. by revoking the read right for a project), 
	 * consequently the selected project(s) might change and therefore the content of the lists also.
	 * It means that some selections may have invalid values (values not present in the list)
	 * but that is not a problem because after the first submit we will have valid selections again 
	 * @param filterUpperTO
	 * @param personBean
	 * @param locale
	 * @param firstLoad first load during this session, false: any further loads
	 * @param withParameter
	 * @param exclusiveProjectID set only by creating a project specific filter
	 * @return
	 */
	public static FilterUpperTO loadFilterSelects(FilterUpperTO filterUpperTO, 
			TPersonBean personBean, Locale locale, boolean firstLoad,
			boolean withParameter/*, Integer exclusiveProjectID*/) {
		//may be null when coming from a dashboard configuration, but never null when coming form filter configuration
		//(when it is null everything will be loaded because is not known which select is needed)
		List<Integer> filterFields = filterUpperTO.getUpperFields();
		filterUpperTO.setAdmin(isAdmin(personBean));
		filterUpperTO.setKeywordIncluded(LuceneUtil.isUseLucene());
		//load project datasource and eventually the preselect project(s)
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(SystemFields.INTEGER_PROJECT);
		IMatcherValue matcherValue = new MatcherExpressionBase(SystemFields.INTEGER_PROJECT, filterUpperTO.getSelectedProjects());
		Integer[] selectedProjects=(Integer[])matcherValue.getValue();
		Integer[] ancestorProjects = null;
		filterUpperTO.setSelectedProjects(selectedProjects);
		MatcherDatasourceContext matcherDatasourceContext = new MatcherDatasourceContext(
				selectedProjects, ancestorProjects, filterUpperTO.getSelectedIssueTypes(), personBean, locale, withParameter, false, false, firstLoad);
		filterUpperTO.setProjectTree((List<TreeNode>)fieldTypeRT.getMatcherDataSource(matcherValue, matcherDatasourceContext, null));
		if (selectedProjects==null || selectedProjects.length==0) {
			//none of the projects is selected -> get the other lists datasource as all available projects would be selected
			List<TProjectBean> projectBeans = ProjectBL.loadUsedProjectsFlat(personBean.getObjectID());
			if (projectBeans!=null && !projectBeans.isEmpty()) {
				selectedProjects = GeneralUtils.createIntegerArrFromCollection(GeneralUtils.createIntegerListFromBeanList(projectBeans));
				ancestorProjects = selectedProjects;
				matcherDatasourceContext.setProjectIDs(selectedProjects);
				matcherDatasourceContext.setAncestorProjectIDs(ancestorProjects);
			}
		} else {
			ancestorProjects = ProjectBL.getAncestorProjects(selectedProjects);
			matcherDatasourceContext.setAncestorProjectIDs(ancestorProjects);
		}
		//datasource for release 
		Integer[] selectedReleases = filterUpperTO.getSelectedReleases();
		if (filterFields==null || filterFields.contains(SystemFields.INTEGER_RELEASENOTICED) || 
				filterFields.contains(SystemFields.INTEGER_RELEASESCHEDULED) || (selectedReleases!=null && selectedReleases.length>0)) {
			if (selectedReleases!=null && selectedReleases.length>0) {
				//Implicit release filter from project/release section of item navigator
				if (filterFields!=null && !filterFields.contains(SystemFields.INTEGER_RELEASESCHEDULED)) {
					filterFields.add(SystemFields.INTEGER_RELEASESCHEDULED);
				}
			}
			fieldTypeRT = FieldTypeManager.getFieldTypeRT(SystemFields.INTEGER_RELEASE);
			matcherValue = new MatcherExpressionBase(SystemFields.INTEGER_RELEASE, selectedReleases);
			filterUpperTO.setReleaseTree((List<TreeNode>)fieldTypeRT.getMatcherDataSource(matcherValue, matcherDatasourceContext, null));
			filterUpperTO.setSelectedReleases((Integer[])matcherValue.getValue());
			filterUpperTO.setReleaseTypeSelectors(getReleaseTypeSelectors(locale));
			if (filterUpperTO.getReleaseTypeSelector()==null) {
				filterUpperTO.setReleaseTypeSelector(
						Integer.valueOf(FilterUpperTO.RELEASE_SELECTOR.SCHEDULED));
			}
		}
		//datasources for the other system select's 
		List<Integer> systemSelects = getSystemSelects();
		for (Integer fieldID : systemSelects) {
			Integer[] selectedValues = filterUpperTO.getSelectedValuesForField(fieldID);
			if (filterFields==null || filterFields.contains(fieldID) || selectedValues!=null) {
				//normally all fields with  with non null values are present in filterFields when they are loaded 
				//from database through FilterUpperFromQNodeTransformer.getFilterSelectsFromTree()
				//but in case of applying instant filter from item navigator upper part the filterFields
				//does not contain the instantly newly added fields (fields which does not have the "appear in filter" flag set)
				//so the fields with value should be added to filterFields, otherwise they will not be rendered
				//by reloading the instant filter in the upper part of item navigator  
				if (filterFields!=null && !filterFields.contains(fieldID)) {
					//apply filter from item navigator upper part (the newly added selection fields are added to filter fields)
					filterFields.add(fieldID);
				}
				fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				matcherValue = new MatcherExpressionBase(fieldID, filterUpperTO.getSelectedValuesForField(fieldID));
				filterUpperTO.setListDatasourceForField(fieldID, (List<ILabelBean>)fieldTypeRT.getMatcherDataSource(
						matcherValue, matcherDatasourceContext, null));
				filterUpperTO.setSelectedValuesForField(fieldID, (Integer[]) matcherValue.getValue());
			}
		}
		//datasource for watchers
		Integer[] selectedWatchers = filterUpperTO.getSelectedConsultantsInformants();
		if ((filterFields!=null && filterFields.contains(FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID)) || selectedWatchers!=null) {
			if (filterFields!=null && !filterFields.contains(FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID)) {
				//see comments of previous for cycle
				filterFields.add(FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID);
			}
			filterUpperTO.setConsultantsInformants(getConsultantsInformants(selectedProjects, ancestorProjects, personBean.getObjectID(), withParameter, null, locale));
			filterUpperTO.setWatcherSelectorList(getConsultantInformantTypeSelectors(locale));
			if (filterUpperTO.getWatcherSelector()==null) {
				filterUpperTO.setWatcherSelector(
					Integer.valueOf(FilterUpperTO.CONSINF_SELECTOR.CONSULTANT_OR_INFORMANT));
			}
		}
		//datasource for upper part custom simple selects
		if (filterUpperTO.getCustomSelectSimpleFields()!=null) {
			Set<Integer> customSelectSimpleFields = filterUpperTO.getCustomSelectSimpleFields().keySet();
			if (customSelectSimpleFields!=null) {
				for (Integer fieldID : customSelectSimpleFields) {
					fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
					matcherValue = new MatcherExpressionBase(fieldID, filterUpperTO.getSelectedValuesForField(fieldID));
					List datasource = (List)fieldTypeRT.getMatcherDataSource(
							matcherValue, matcherDatasourceContext, null);
					if (FieldBL.isTree(fieldID)) {
						filterUpperTO.setTreeDatasourceForField(fieldID, (List<TreeNode>)datasource);
					} else {
						filterUpperTO.setListDatasourceForField(fieldID, (List<ILabelBean>)datasource);
					}
				}
			}
		}
		//datasource for archived/delete
		if (filterUpperTO.isAdmin() && (filterFields==null || filterFields.contains(SystemFields.INTEGER_ARCHIVELEVEL) || //"appears in filter" at field config
				filterFields.contains(FilterUpperTO.PSEUDO_FIELDS.ARCHIVED_FIELD_ID) || filterFields.contains(FilterUpperTO.PSEUDO_FIELDS.DELETED_FIELD_ID))) {//a value remained from the time when "appears in filter" was set
			filterUpperTO.setArchivedOptions(getArchivedOptions(locale));
			filterUpperTO.setDeletedOptions(getDeletedOptions(locale));
			if (firstLoad) {
				filterUpperTO.setArchived(Integer.valueOf(FilterUpperTO.ARCHIVED_FILTER.EXCLUDE_ARCHIVED));
				filterUpperTO.setDeleted(Integer.valueOf(FilterUpperTO.DELETED_FILTER.EXCLUDE_DELETED));
			}
		}
		//datsource for link types
		filterUpperTO.setLinkTypeFilterSupersetList(LinkTypeBL.getLinkTypeFilterSupersetExpressions(locale, true));
		return filterUpperTO;
	}
	
	/**
	 * Adds the possible values, matchers etc.
	 * Depending on firstLoad preselects some values  
	 * Extra care should be taken for selecting the projects when firstLoad=false because 
	 * the other lists should be populated depending on the selected project
	 * The other selected fields might be invalid after a new loading 
	 * because the project list might change (for ex. by revoking the read right for a project), 
	 * consequently the selected project(s) might change and therefore the content of the lists also.
	 * It means that some selections may have invalid values (values not present in the list)
	 * but that is not a problem because after the first submit we will have valid selections again
     * @param filterUpperTO
     * @param selectedProjects
     * @param personBean
	 * @param locale
	 * @param withParameter
     * @param showClosedReleases
	 * @return
	 */
	public static FilterUpperTO reloadProjectDependentSelects(FilterUpperTO filterUpperTO, 
			Integer[] selectedProjects, Integer[] selectedItemTypes, TPersonBean personBean, Locale locale, boolean withParameter, boolean showClosedReleases) {
		List<Integer> filterFields = filterUpperTO.getUpperFields();
		if (filterFields==null || filterFields.isEmpty()) {
			//from dashboard?
			//should never happen, always it is good to explicitly specify the fields needed to reload
			//otherwise everything will be reloaded even if probably not needed 
			filterFields = getSystemSelects();
			filterFields.add(FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID);
			filterUpperTO.setUpperFields(filterFields);
		}
		//get all available projects if none of them is selected, for loading the datasource for the other selects
		Integer[] ancestorProjects = null;
		if (selectedProjects==null || selectedProjects.length==0) {
			List<TProjectBean> projectBeans =  ProjectBL.loadUsedProjectsFlat(personBean.getObjectID());
			//none of the projects is selected -> get the other lists datasource as all available projects would be selected
			if (projectBeans!=null && !projectBeans.isEmpty()) {
				selectedProjects = GeneralUtils.createIntegerArrFromCollection(GeneralUtils.createIntegerListFromBeanList(projectBeans));
				ancestorProjects = selectedProjects;
			}
		} else {
			ancestorProjects = ProjectBL.getAncestorProjects(selectedProjects);
		}
		filterUpperTO.setSelectedIssueTypes(selectedItemTypes); 
		//refresh release datasource
		if (filterFields.contains(SystemFields.INTEGER_RELEASENOTICED) || 
				filterFields.contains(SystemFields.INTEGER_RELEASESCHEDULED)) {
			Integer[] selectedReleases = filterUpperTO.getSelectedReleases();
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(SystemFields.INTEGER_RELEASE);
			IMatcherValue matcherValue = new MatcherExpressionBase(SystemFields.INTEGER_RELEASE, selectedReleases);
			MatcherDatasourceContext matcherDatasourceContext = new MatcherDatasourceContext(
					selectedProjects, ancestorProjects, filterUpperTO.getSelectedIssueTypes(), personBean, locale, withParameter, false, showClosedReleases, false);
			filterUpperTO.setReleaseTree((List<TreeNode>)fieldTypeRT.getMatcherDataSource(matcherValue, matcherDatasourceContext, null));
		}
		//refresh watchers
		if (filterFields.contains(FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID)) {
			filterUpperTO.setConsultantsInformants(getConsultantsInformants(selectedProjects, ancestorProjects, personBean.getObjectID(), withParameter, null, locale));			
		}
		//other selects except some could be loaded without specific code 
		List<Integer> specificSystemFields = new LinkedList<Integer>();
		specificSystemFields.add(SystemFields.RELEASENOTICED);
		specificSystemFields.add(SystemFields.RELEASESCHEDULED);
		specificSystemFields.add(FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID);
		//initialize the custom selects map for filterUpperTO.setDatasourceForField
		filterUpperTO.setCustomSelects(new HashMap<Integer, List<ILabelBean>>());
		filterUpperTO.setCustomSelectSimpleFields(getUpperCustomSelectSimpleFieldsMap(filterUpperTO.getUpperFields()));
		for (Integer fieldID : filterFields) {
			if (!specificSystemFields.contains(fieldID)) {
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldTypeRT!=null) {
					IMatcherValue matcherValue = new MatcherExpressionBase(fieldID, null);
					MatcherDatasourceContext matcherDatasourceContext = new MatcherDatasourceContext(
							selectedProjects, ancestorProjects, filterUpperTO.getSelectedIssueTypes(), personBean, locale, withParameter, false, false, false);
					Object datasource = fieldTypeRT.getMatcherDataSource(
							matcherValue, matcherDatasourceContext, null);
					if (FieldBL.isTree(fieldID)) {
						filterUpperTO.setTreeDatasourceForField(fieldID, (List<TreeNode>)datasource);
					} else {
						filterUpperTO.setListDatasourceForField(fieldID, (List<ILabelBean>)datasource);
					}
				}
			}
		}
		return filterUpperTO;
	}
	
	/**
	 * Adds the possible values, matchers etc.
	 * Depending on firstLoad preselects some values  
	 * Extra care should be taken for selecting the projects when firstLoad=false because 
	 * the other lists should be populated depending on the selected project
	 * The other selected fields might be invalid after a new loading 
	 * because the project list might change (for ex. by revoking the read right for a project), 
	 * consequently the selected project(s) might change and therefore the content of the lists also.
	 * It means that some selections may have invalid values (values not present in the list)
	 * but that is not a problem because after the first submit we will have valid selections again
     * @param fieldID
     * @param selectedProjects
	 * @param personBean
	 * @param locale
	 * @param withParameter
     * @param showClosedReleases
	 * @return
	 */
	public static Object getSelectFieldDataSource(Integer fieldID,
			Integer[] selectedProjects, Integer[] itemTypeIDs, TPersonBean personBean, Locale locale, boolean withParameter, boolean showClosedReleases) {
		//get all available projects if none of them is selected, for loading the datasource for the other selects
		Integer[] ancestorProjects = null;
		if (selectedProjects==null || selectedProjects.length==0) {
			//none of the projects is selected -> get the other lists datasource as all available projects would be selected
			List<TProjectBean> projectBeans = ProjectBL.loadUsedProjectsFlat(personBean.getObjectID());
			if (projectBeans!=null && !projectBeans.isEmpty()) {
				selectedProjects = GeneralUtils.createIntegerArrFromCollection(GeneralUtils.createIntegerListFromBeanList(projectBeans));
				ancestorProjects = selectedProjects;
			}
		} else {
			ancestorProjects = ProjectBL.getAncestorProjects(selectedProjects);
		}	
		//refresh watchers
		if (fieldID.equals(FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID)) {
			return getConsultantsInformants(selectedProjects, ancestorProjects, personBean.getObjectID(), withParameter, null, locale);
		}
		//other selects except some could be loaded without specific code 
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
		if (fieldTypeRT!=null) {
			IMatcherValue matcherValue = new MatcherExpressionBase(fieldID, null);
			MatcherDatasourceContext matcherDatasourceContext = new MatcherDatasourceContext(
					selectedProjects, ancestorProjects, itemTypeIDs, personBean, locale, withParameter, false, false, false);
			return fieldTypeRT.getMatcherDataSource(matcherValue, matcherDatasourceContext, null);
		}
		return null;
	}
	
	/**
	 * Gets the upper select custom simple fields
	 */
	public static Map<Integer, Integer> getUpperCustomSelectSimpleFieldsMap(List<Integer> upperFields) {
		Map<Integer, Integer> upperCustomSelectSimpleFields = new HashMap<Integer, Integer>();
		Set<Integer> directProcesFields = FilterBL.getDirectProcessFields();
		for (Integer fieldID: upperFields) {
			if (!directProcesFields.contains(fieldID)) {
				boolean isCustomSelect = FieldBL.isCustomSelect(fieldID);
				if (isCustomSelect) {
					//get the custom select simple fields from the upper part
					upperCustomSelectSimpleFields.put(fieldID, FieldBL.getSystemOptionType(fieldID));
				} 
			}
		}
		return upperCustomSelectSimpleFields;
	}
}
