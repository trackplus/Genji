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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.aurel.track.admin.customize.category.filter.execute.TreeFilterExecuterFacade;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.TreeNode;

/**
 * Filtering bean for custom filters
 * @author Tamas Ruff
 *
 */
public final class FilterUpperTO implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * the Pseudo column identifiers
	 * They should be different from any existing field number
	 */
	public interface PSEUDO_FIELDS {
		//the symbolic fieldID of the keyword string from the ReportConfigBean
		//this value will be searched in all fields using lucene
		public static final int KEYWORD_FIELD_ID = 0;
		public static final int CONSULTANT_INFORMNAT_FIELD_ID = -1001;
		public static final int CONSULTANT_OR_INFORMANT_SELECTOR = -1002;
		public static final int ARCHIVED_FIELD_ID = -1011;
		public static final int DELETED_FIELD_ID = -1012;
		public static final int LINKTYPE_FILTER_SUPERSET = -1021;
		public static final int RELEASE_TYPE_SELECTOR = -1031;
		public static final int SHOW_CLOSED_RELEASES = -1032;
	}

	public interface RELEASE_SELECTOR {
		public static final int NOTICED_OR_SCHEDULED = 0;
		public static final int NOTICED = 1;
		public static final int SCHEDULED = 2;
	}

	public interface CONSINF_SELECTOR {
		public static final int CONSULTANT_OR_INFORMANT = 0;
		public static final int CONSULTANT = 1;
		public static final int INFORMANT = 2;
	}

	public interface ARCHIVED_FILTER {
		public static final int EXCLUDE_ARCHIVED = 0;
		public static final int INCLUDE_ARCHIVED = 1;
		public static final int ONLY_ARCHIVED = 2;
	}

	public interface DELETED_FILTER {
		public static final int EXCLUDE_DELETED = 0;
		public static final int INCLUDE_DELETED = 1;
		public static final int ONLY_DELETED = 2;
	}

	//list of all fields present in the upper part
	private List<Integer> upperFields;
	//custom select simple fields in the upper part: it can be a custom list or a system list picker (userPicker, projectPicker)
	//for custom list the value is null, for system list the value is the system option type (user, project etc.)
	private Map<Integer, Integer> customSelectSimpleFieldsMap;

	/**
	 * 1. System selects
	 */
	private Integer[] selectedProjects = null;
	private List<TreeNode> projectTree = null;
	private Integer[] selectedReleases = null;
	private List<TreeNode> releaseTree = null;
	//should get a value from RELEASE_SELECTOR
	private Integer releaseTypeSelector;
	private List<IntegerStringBean> releaseTypeSelectors = new ArrayList<IntegerStringBean>();
	//whether to show closed releases
	private boolean showClosedReleases;
	private Integer[] selectedManagers = null;
	private List<ILabelBean> managers = new ArrayList<ILabelBean>();
	private Integer[] selectedOriginators = null;
	private List<ILabelBean> originators = new ArrayList<ILabelBean>();
	private Integer[] selectedChangedBys = null;
	private List<ILabelBean> changedBys = new ArrayList<ILabelBean>();
	private Integer[] selectedResponsibles = null;
	private List<ILabelBean> responsibles = new ArrayList<ILabelBean>();
	//whether to add also the responsibles through group
	private boolean includeResponsiblesThroughGroup = true;
	private Integer[] selectedConsultantsInformants = null;
	private List<ILabelBean> consultantsInformants  = new ArrayList<ILabelBean>();
	//should get a value from CONSINF_SELECTOR
	private Integer watcherSelector;
	private List<IntegerStringBean> watcherSelectorList  = new ArrayList<IntegerStringBean>();
	private Integer[] selectedStates = null;
	private List<ILabelBean> states = new ArrayList<ILabelBean>();
	private boolean includeOpen = true;
	private boolean includeClosed = true;
	private Integer[] selectedIssueTypes = null;
	private List<ILabelBean> issueTypes = new ArrayList<ILabelBean>();
	private Integer[] selectedPriorities = null;
	private List<ILabelBean> priorities = new ArrayList<ILabelBean>();
	private Integer[] selectedSeverities = null;
	private List<ILabelBean> severities = new ArrayList<ILabelBean>();

	/**
	 * 2. Custom simple selects
	 */
	//for receiving the submitted custom simple selects
	private Map<Integer, String>  selectedCustomSelectsStr;
	//selected values for each custom select
	private Map<Integer, Integer[]> selectedCustomSelects;
	//datasource for each custom select
	private Map<Integer, List<ILabelBean>> customSelects;
	//custom trees, now only the custom project picker
	private Map<Integer, List<TreeNode>> customTrees;
	/**
	 * 3. Miscellaneous filtering attributes
	 */
	//keyword if lucene is active
	private boolean keywordIncluded;
	private String keyword;

	//the watchers selection is available only for persons with viewWatchers right
	//private boolean viewWatchers;

	//archived and deleted selections are available only for project or system admins
	private boolean admin;
	private Integer archived;
	private List<IntegerStringBean> archivedOptions = new ArrayList<IntegerStringBean>();
	private Integer deleted;
	private List<IntegerStringBean> deletedOptions = new ArrayList<IntegerStringBean>();

	//include also all issues which might not match the filter but are
	//linked to an issue from the filter result with this link type expression
	private String linkTypeFilterSuperset;
	private List<LabelValueBean> linkTypeFilterSupersetList = new ArrayList<LabelValueBean>();
	//if not null add only linked items of type linkTypeFilterSuperset of this item types.
	//not selectable from user interface by defining a filter
	private List<Integer> itemTypeIDsForLinkType = null;

	private MatcherContext matcherContext;

	/**
	 * 4. List of simple field expressions (except custom simple selects) present in upper part
	 */
	private List<FieldExpressionSimpleTO> fieldExpressionSimpleList;

	/**
	 * Whether there are simple field expressions configured
	 * @return
	 */
	public boolean hasSimpleFieldExpressions() {
		if (fieldExpressionSimpleList!=null) {
			for (FieldExpressionSimpleTO fieldExpressionSimpleTO : fieldExpressionSimpleList) {
				Integer selectedMatcher = fieldExpressionSimpleTO.getSelectedMatcher();
				if (selectedMatcher!=null && selectedMatcher.intValue()!=MatchRelations.NO_MATCHER) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets the lists with selections
	 * @return
	 */
	public List<Integer> getListFieldsWithSelection() {
		List<Integer> existingLists = new LinkedList<Integer>();
		existingLists.add(SystemFields.INTEGER_PROJECT);
		existingLists.add(SystemFields.INTEGER_RELEASE);
		existingLists.add(SystemFields.INTEGER_MANAGER);
		existingLists.add(SystemFields.INTEGER_RESPONSIBLE);
		existingLists.add(SystemFields.INTEGER_ORIGINATOR);
		existingLists.add(SystemFields.INTEGER_CHANGEDBY);
		existingLists.add(SystemFields.INTEGER_STATE);
		existingLists.add(SystemFields.INTEGER_ISSUETYPE);
		existingLists.add(SystemFields.INTEGER_PRIORITY);
		existingLists.add(SystemFields.INTEGER_SEVERITY);
		if (this.getSelectedCustomSelects()!=null) {
			for (Integer fieldID : this.getSelectedCustomSelects().keySet()) {
				existingLists.add(fieldID);
			}
		}
		List<Integer> listsWithSelections = new LinkedList<Integer>();
		for (Integer fieldID : existingLists) {
			Integer[] selections = getSelectedValuesForField(fieldID);
			if (selections!=null && selections.length>0) {
				listsWithSelections.add(fieldID);
			}
		}
		return listsWithSelections;
	}

	/**
	 * Gets the selected options for a select
	 * @param fieldID
	 * @return
	 */
	public Integer[] getSelectedValuesForField(Integer fieldID) {
		if (fieldID!=null) {
			switch (fieldID.intValue()) {
			case SystemFields.PROJECT:
				return this.getSelectedProjects();
			case SystemFields.RELEASE:
				return this.getSelectedReleases();
			case SystemFields.MANAGER:
				return this.getSelectedManagers();
			case SystemFields.RESPONSIBLE:
				return this.getSelectedResponsibles();
			case SystemFields.ORIGINATOR:
				return this.getSelectedOriginators();
			case SystemFields.CHANGEDBY:
				return this.getSelectedChangedBys();
			case PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID:
				return this.getSelectedConsultantsInformants();
			case SystemFields.STATE:
				return this.getSelectedStates();
			case SystemFields.ISSUETYPE:
				return this.getSelectedIssueTypes();
			case SystemFields.PRIORITY:
				return this.getSelectedPriorities();
			case SystemFields.SEVERITY:
				return this.getSelectedSeverities();
			default:
				if (this.getSelectedCustomSelects()!=null) {
					return this.getSelectedCustomSelects().get(fieldID);
				}
				break;
			}
		}
		return null;
	}

	/**
	 * Sets the selected options for a select
	 * @param fieldID
	 * @param selectedValues
	 * @return
	 */
	public Integer[] setSelectedValuesForField(Integer fieldID, Integer[] selectedValues) {
		if (fieldID!=null) {
			switch (fieldID.intValue()) {
			case SystemFields.PROJECT:
				setSelectedProjects(selectedValues);
				break;
			case SystemFields.RELEASE:
				setSelectedReleases(selectedValues);
				break;
			case SystemFields.MANAGER:
				setSelectedManagers(selectedValues);
				break;
			case SystemFields.RESPONSIBLE:
				setSelectedResponsibles(selectedValues);
				break;
			case SystemFields.ORIGINATOR:
				setSelectedOriginators(selectedValues);
				break;
			case SystemFields.CHANGEDBY:
				setSelectedChangedBys(selectedValues);
				break;
			case PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID:
				setSelectedConsultantsInformants(selectedValues);
				break;
			case SystemFields.STATE:
				setSelectedStates(selectedValues);
				break;
			case SystemFields.ISSUETYPE:
				setSelectedIssueTypes(selectedValues);
				break;
			case SystemFields.PRIORITY:
				setSelectedPriorities(selectedValues);
				break;
			case SystemFields.SEVERITY:
				setSelectedSeverities(selectedValues);
				break;
			default:
				if (this.getSelectedCustomSelects()==null) {
					this.setSelectedCustomSelects(new HashMap<Integer, Integer[]>());
				}
				this.getSelectedCustomSelects().put(fieldID, selectedValues);
				break;
			}
		}
		return null;
	}

	/**
	 * Gets the datasource for a select
	 * @param fieldID
	 * @return
	 */
	public List<ILabelBean> getListDatasourceForField(Integer fieldID) {
		if (fieldID!=null) {
			switch (fieldID.intValue()) {
			case SystemFields.MANAGER:
				return this.getManagers();
			case SystemFields.RESPONSIBLE:
				return this.getResponsibles();
			case SystemFields.ORIGINATOR:
				return this.getOriginators();
			case SystemFields.CHANGEDBY:
				return this.getChangedBys();
			case PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID:
				return this.getConsultantsInformants();
			case SystemFields.STATE:
				return this.getStates();
			case SystemFields.ISSUETYPE:
				return this.getIssueTypes();
			case SystemFields.PRIORITY:
				return this.getPriorities();
			case SystemFields.SEVERITY:
				return this.getSeverities();
			default:
				if (this.getCustomSelects()!=null) {
					return this.getCustomSelects().get(fieldID);
				}
				break;
			}
		}
		return null;
	}

	/**
	 * Sets the datasource for a select
	 * @param fieldID
	 * @param datasource
	 * @return
	 */
	public void setListDatasourceForField(Integer fieldID, List<ILabelBean> datasource) {
		if (fieldID!=null) {
			switch (fieldID.intValue()) {
			case SystemFields.MANAGER:
				setManagers(datasource);
				break;
			case SystemFields.RESPONSIBLE:
				setResponsibles(datasource);
				break;
			case SystemFields.ORIGINATOR:
				setOriginators(datasource);
				break;
			case SystemFields.CHANGEDBY:
				setChangedBys(datasource);
				break;
			case PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID:
				setConsultantsInformants(datasource);
				break;
			case SystemFields.STATE:
				setStates(datasource);
				break;
			case SystemFields.ISSUETYPE:
				setIssueTypes(datasource);
				break;
			case SystemFields.PRIORITY:
				setPriorities(datasource);
				break;
			case SystemFields.SEVERITY:
				setSeverities(datasource);
				break;
			default:
				if (this.getCustomSelects()==null) {
					this.setCustomSelects(new HashMap<Integer, List<ILabelBean>>());
				}
				this.getCustomSelects().put(fieldID, datasource);
				break;
			}
		}
	}

	/**
	 * Gets the datasource for a select
	 * @param fieldID
	 * @return
	 */
	public List<TreeNode> getTreeDatasourceForField(Integer fieldID) {
		if (fieldID!=null ) {
			if (fieldID.equals(SystemFields.INTEGER_RELEASE)) {
				return this.getReleaseTree();
			} else {
				if (this.getCustomTrees()!=null) {
					return this.getCustomTrees().get(fieldID);
				}
			}
		}
		return null;
	}

	/**
	 * Sets the datasource for a select
	 * @param fieldID
	 * @param datasource
	 * @return
	 */
	public void setTreeDatasourceForField(Integer fieldID, List<TreeNode> datasource) {
		if (fieldID!=null) {
			if (fieldID.equals(SystemFields.INTEGER_RELEASE)) {
				this.setReleaseTree(datasource);
			} else {
				if (this.getCustomTrees()==null) {
					this.setCustomTrees(new HashMap<Integer, List<TreeNode>>());
				}
				this.getCustomTrees().put(fieldID, datasource);
			}
		}
	}

	/**
	 * Whether at least one list selection exist
	 * to start executing the filter from the database
	 * @return
	 */
	public boolean hasListSelection() {
		/**
		 * 1. System selects
		 */
		if (selectedProjects!=null || selectedReleases!=null ||
				selectedManagers!=null || selectedOriginators!=null || selectedChangedBys!=null || selectedResponsibles!=null ||
				selectedConsultantsInformants!=null ||
				selectedStates!=null || selectedIssueTypes!=null || selectedPriorities!=null || selectedSeverities!=null) {
			return true;
		}
		/**
		 * 2. Custom simple selects
		 */
		if (selectedCustomSelectsStr!=null || selectedCustomSelects!=null) {
			return true;
		}
		return false;
	}

	/**
	 * Reset when there is only $PARAMETER selection (and no other selection)
	 * although at this moment the $PARAMETER's should be already replaced by user entered values,
	 * there are cases where the user is not prompted for parameters (for ex. filter called from dashboard)
	 * in this case the $PARAMETER value should be neglected otherwise the result will be empty
	 * If there are also other selections as $PARAMETER, then the $PARAMETER will be not removed because in SQL IN clauses it does not hurt
	 */
	public void cleanParameters() {
		if (selectedProjects!=null && selectedProjects.length==1 && MatcherContext.PARAMETER.equals(selectedProjects[0])) {
			selectedProjects = null;
		}
		if (selectedReleases!=null && selectedReleases.length==1 && MatcherContext.PARAMETER.equals(selectedReleases[0])) {
			selectedReleases = null;
		}
		if (selectedManagers!=null && selectedManagers.length==1 && MatcherContext.PARAMETER.equals(selectedManagers[0])) {
			selectedManagers = null;
		}
		if (selectedOriginators!=null && selectedOriginators.length==1 && MatcherContext.PARAMETER.equals(selectedOriginators[0])) {
			selectedOriginators = null;
		}
		if (selectedChangedBys!=null && selectedChangedBys.length==1 && MatcherContext.PARAMETER.equals(selectedChangedBys[0])) {
			selectedChangedBys = null;
		}
		if (selectedResponsibles!=null && selectedResponsibles.length==1 && MatcherContext.PARAMETER.equals(selectedResponsibles[0])) {
			selectedResponsibles = null;
		}
		if (selectedConsultantsInformants!=null && selectedConsultantsInformants.length==1 && MatcherContext.PARAMETER.equals(selectedConsultantsInformants[0])) {
			selectedConsultantsInformants = null;
		}
		if (selectedStates!=null && selectedStates.length==1 && MatcherContext.PARAMETER.equals(selectedStates[0])) {
			selectedStates = null;
		}
		if (selectedIssueTypes!=null && selectedIssueTypes.length==1 && MatcherContext.PARAMETER.equals(selectedIssueTypes[0])) {
			selectedIssueTypes = null;
		}
		if (selectedPriorities!=null && selectedPriorities.length==1 && MatcherContext.PARAMETER.equals(selectedPriorities[0])) {
			selectedPriorities = null;
		}
		if (selectedSeverities!=null && selectedSeverities.length==1 && MatcherContext.PARAMETER.equals(selectedSeverities[0])) {
			selectedSeverities = null;
		}
		if (selectedCustomSelects!=null) {
			for (Iterator<Map.Entry<Integer, Integer[]>> iterator = selectedCustomSelects.entrySet().iterator(); iterator.hasNext();) {
				Map.Entry<Integer, Integer[]> customSelectionEntries = iterator.next();
				Integer[] customSelections = customSelectionEntries.getValue();
				if (customSelections!=null && customSelections.length==1 && MatcherContext.PARAMETER.equals(customSelections[0])) {
					iterator.remove();
				}
			}
		}
	}

	/**
	 * Whether a TQL or a TQLPlus expression is specified
	 * @return
	 */
	public boolean hasTQLExpression() {
		return keyword!=null && !"".equals(keyword.trim());
	}

	/**
	 * @return the consultantsInformants
	 */
	public List<ILabelBean> getConsultantsInformants() {
		return consultantsInformants;
	}
	/**
	 * @param consultantsInformants the consultantsInformants to set
	 */
	public void setConsultantsInformants(List<ILabelBean> consultantsInformants) {
		this.consultantsInformants = consultantsInformants;
	}
	/**
	 * @return the selectedConsultantsInformants
	 */
	public Integer[] getSelectedConsultantsInformants() {
		return selectedConsultantsInformants;
	}
	/**
	 * @param selectedConsultantsInformants the selectedConsultantsInformants to set
	 */
	public void setSelectedConsultantsInformants(
			Integer[] selectedConsultantsInformants) {
		this.selectedConsultantsInformants = selectedConsultantsInformants;
	}
	/**
	 * @param selectedConsultantsInformants the selectedConsultantsInformants to set
	 */
	public void setSelectedConsultantsInformantsStr(String selectedConsultantsInformants) {
		this.selectedConsultantsInformants = createIntegerArrFromString(selectedConsultantsInformants);
	}
	/**
	 * @return the issueTypes
	 */
	public List<ILabelBean> getIssueTypes() {
		return issueTypes;
	}
	/**
	 * @param issueTypes the issueTypes to set
	 */
	public void setIssueTypes(List<ILabelBean> issueTypes) {
		this.issueTypes = issueTypes;
	}
	/**
	 * @return the managers
	 */
	public List<ILabelBean> getManagers() {
		return managers;
	}
	/**
	 * @param managers the managers to set
	 */
	public void setManagers(List<ILabelBean> managers) {
		this.managers = managers;
	}
	/**
	 * @return the originators
	 */
	public List<ILabelBean> getOriginators() {
		return originators;
	}
	/**
	 * @param originators the originators to set
	 */
	public void setOriginators(List<ILabelBean> originators) {
		this.originators = originators;
	}
	/**
	 * @return the priorities
	 */
	public List<ILabelBean> getPriorities() {
		return priorities;
	}
	/**
	 * @param priorities the priorities to set
	 */
	public void setPriorities(List<ILabelBean> priorities) {
		this.priorities = priorities;
	}

	/**
	 * @return the responsibles
	 */
	public List<ILabelBean> getResponsibles() {
		return responsibles;
	}
	/**
	 * @param responsibles the responsibles to set
	 */
	public void setResponsibles(List<ILabelBean> responsibles) {
		this.responsibles = responsibles;
	}

	public boolean isIncludeResponsiblesThroughGroup() {
		return includeResponsiblesThroughGroup;
	}

	public void setIncludeResponsiblesThroughGroup(
			boolean includeResponsiblesThroughGroup) {
		this.includeResponsiblesThroughGroup = includeResponsiblesThroughGroup;
	}

	/**
	 * @return the selectedIssueTypes
	 */
	public Integer[] getSelectedIssueTypes() {
		return selectedIssueTypes;
	}
	/**
	 * @param selectedIssueTypes the selectedIssueTypes to set
	 */
	public void setSelectedIssueTypes(Integer[] selectedIssueTypes) {
		this.selectedIssueTypes = selectedIssueTypes;
	}
	/**
	 * @param selectedIssueTypes the selectedIssueTypes to set
	 */
	public void setSelectedIssueTypesStr(String selectedIssueTypes) {
		this.selectedIssueTypes = createIntegerArrFromString(selectedIssueTypes);
	}
	/**
	 * @return the selectedManagers
	 */
	public Integer[] getSelectedManagers() {
		return selectedManagers;
	}
	/**
	 * @param selectedManagers the selectedManagers to set
	 */
	public void setSelectedManagers(Integer[] selectedManagers) {
		this.selectedManagers = selectedManagers;
	}
	/**
	 * @param selectedManagers the selectedManagers to set
	 */
	public void setSelectedManagersStr(String selectedManagers) {
		this.selectedManagers = createIntegerArrFromString(selectedManagers);
	}
	/**
	 * @return the selectedOriginators
	 */
	public Integer[] getSelectedOriginators() {
		return selectedOriginators;
	}
	/**
	 * @param selectedOriginators the selectedOriginators to set
	 */
	public void setSelectedOriginators(Integer[] selectedOriginators) {
		this.selectedOriginators = selectedOriginators;
	}
	/**
	 * @param selectedOriginators the selectedOriginators to set
	 */
	public void setSelectedOriginatorsStr(String selectedOriginators) {
		this.selectedOriginators = createIntegerArrFromString(selectedOriginators);
	}

	public Integer[] getSelectedChangedBys() {
		return selectedChangedBys;
	}

	public void setSelectedChangedBys(Integer[] selectedChangedBys) {
		this.selectedChangedBys = selectedChangedBys;
	}

	public void setSelectedChangedBysStr(String selectedChangedBysStr) {
		this.selectedChangedBys = createIntegerArrFromString(selectedChangedBysStr);
	}

	public List<ILabelBean> getChangedBys() {
		return changedBys;
	}

	public void setChangedBys(List<ILabelBean> lastChangedBys) {
		this.changedBys = lastChangedBys;
	}

	/**
	 * @return the selectedPriorities
	 */
	public Integer[] getSelectedPriorities() {
		return selectedPriorities;
	}
	/**
	 * @param selectedPriorities the selectedPriorities to set
	 */
	public void setSelectedPriorities(Integer[] selectedPriorities) {
		this.selectedPriorities = selectedPriorities;
	}
	/**
	 * @param selectedPriorities the selectedPriorities to set
	 */
	public void setSelectedPrioritiesStr(String selectedPriorities) {
		this.selectedPriorities = createIntegerArrFromString(selectedPriorities);
	}
	/**
	 * @return the selectedProjects
	 */
	public Integer[] getSelectedProjects() {
		return selectedProjects;
	}
	/**
	 * @param selectedProjects the selectedProjects to set
	 */
	public void setSelectedProjects(Integer[] selectedProjects) {
		this.selectedProjects = selectedProjects;
	}

	/**
	 * @param selectedProjects the selectedProjects to set
	 */
	public void setSelectedProjectsStr(String selectedProjects) {
		this.selectedProjects = createIntegerArrFromString(selectedProjects);
	}

	public List<TreeNode> getProjectTree() {
		return projectTree;
	}

	public void setProjectTree(List<TreeNode> projectTreeNodes) {
		this.projectTree = projectTreeNodes;
	}

	/**
	 * @return the selectedRelScheduled
	 */
	public Integer[] getSelectedReleases() {
		return selectedReleases;
	}
	/**
	 * @param selectedReleases the selectedRelScheduled to set
	 */
	public void setSelectedReleases(Integer[] selectedReleases) {
		this.selectedReleases = selectedReleases;
	}
	/**
	 * @param selectedReleases the selectedReleases to set
	 */
	public void setSelectedReleasesStr(String selectedReleases) {
		this.selectedReleases = createIntegerArrFromString(selectedReleases);
	}

	public List<TreeNode> getReleaseTree() {
		return releaseTree;
	}

	public void setReleaseTree(List<TreeNode> releaseTree) {
		this.releaseTree = releaseTree;
	}

	/**
	 * @return the selectedResponsibles
	 */
	public Integer[] getSelectedResponsibles() {
		return selectedResponsibles;
	}
	/**
	 * @param selectedResponsibles the selectedResponsibles to set
	 */
	public void setSelectedResponsibles(Integer[] selectedResponsibles) {
		this.selectedResponsibles = selectedResponsibles;
	}
	/**
	 * @param selectedResponsibles the selectedResponsibles to set
	 */
	public void setSelectedResponsiblesStr(String selectedResponsibles) {
		this.selectedResponsibles = createIntegerArrFromString(selectedResponsibles);
	}
	/**
	 * @return the selectedSeverities
	 */
	public Integer[] getSelectedSeverities() {
		return selectedSeverities;
	}
	/**
	 * @param selectedSeverities the selectedSeverities to set
	 */
	public void setSelectedSeverities(Integer[] selectedSeverities) {
		this.selectedSeverities = selectedSeverities;
	}
	/**
	 * @param selectedSeverities the selectedSeverities to set
	 */
	public void setSelectedSeveritiesStr(String selectedSeverities) {
		this.selectedSeverities = createIntegerArrFromString(selectedSeverities);
	}
	/**
	 * @return the selectedStates
	 */
	public Integer[] getSelectedStates() {
		return selectedStates;
	}
	/**
	 * @param selectedStates the selectedStates to set
	 */
	public void setSelectedStates(Integer[] selectedStates) {
		this.selectedStates = selectedStates;
	}
	/**
	 * @param selectedStates the selectedStates to set
	 */
	public void setSelectedStatesStr(String selectedStates) {
		this.selectedStates = createIntegerArrFromString(selectedStates);
	}

	/**
	 * @return the severities
	 */
	public List<ILabelBean> getSeverities() {
		return severities;
	}
	/**
	 * @param severities the severities to set
	 */
	public void setSeverities(List<ILabelBean> severities) {
		this.severities = severities;
	}
	/**
	 * @return the states
	 */
	public List<ILabelBean> getStates() {
		return states;
	}
	/**
	 * @param states the states to set
	 */
	public void setStates(List<ILabelBean> states) {
		this.states = states;
	}
	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}
	/**
	 * @param keyword the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getArchived() {
		return archived;
	}

	public void setArchived(Integer archived) {
		this.archived = archived;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}


	public List<IntegerStringBean> getArchivedOptions() {
		return archivedOptions;
	}

	public void setArchivedOptions(List<IntegerStringBean> archivedOptions) {
		this.archivedOptions = archivedOptions;
	}

	public List<IntegerStringBean> getDeletedOptions() {
		return deletedOptions;
	}

	public void setDeletedOptions(List<IntegerStringBean> deletedOptions) {
		this.deletedOptions = deletedOptions;
	}

	public String getLinkTypeFilterSuperset() {
		return linkTypeFilterSuperset;
	}

	public void setLinkTypeFilterSuperset(
			String selectedLinkTypeFilterSuperset) {
		this.linkTypeFilterSuperset = selectedLinkTypeFilterSuperset;
	}

	public List<LabelValueBean> getLinkTypeFilterSupersetList() {
		return linkTypeFilterSupersetList;
	}

	public void setLinkTypeFilterSupersetList(
			List<LabelValueBean> linkTypeFilterSupersetList) {
		this.linkTypeFilterSupersetList = linkTypeFilterSupersetList;
	}

	public List<Integer> getItemTypeIDsForLinkType() {
		return itemTypeIDsForLinkType;
	}

	public void setItemTypeIDsForLinkType(List<Integer> itemTypeIDsForLinkType) {
		this.itemTypeIDsForLinkType = itemTypeIDsForLinkType;
	}

	public Integer getWatcherSelector() {
		return watcherSelector;
	}

	public void setWatcherSelector(Integer watcherSelector) {
		this.watcherSelector = watcherSelector;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public List<IntegerStringBean> getWatcherSelectorList() {
		return watcherSelectorList;
	}

	public void setWatcherSelectorList(
			List<IntegerStringBean> watcherSelectorList) {
		this.watcherSelectorList = watcherSelectorList;
	}

	public Integer getReleaseTypeSelector() {
		return releaseTypeSelector;
	}

	public void setReleaseTypeSelector(Integer releaseTypeSelector) {
		this.releaseTypeSelector = releaseTypeSelector;
	}

	public List<IntegerStringBean> getReleaseTypeSelectors() {
		return releaseTypeSelectors;
	}

	public void setReleaseTypeSelectors(List<IntegerStringBean> releaseSelectors) {
		this.releaseTypeSelectors = releaseSelectors;
	}

	public boolean isShowClosedReleases() {
		return showClosedReleases;
	}

	public void setShowClosedReleases(boolean showClosedReleases) {
		this.showClosedReleases = showClosedReleases;
	}

	public MatcherContext getMatcherContext() {
		return matcherContext;
	}

	public void setMatcherContext(MatcherContext matcherContext) {
		this.matcherContext = matcherContext;
		TreeFilterExecuterFacade.replaceSymbolicWithActual(this, matcherContext);
	}

	public List<Integer> getUpperFields() {
		return upperFields;
	}

	public void setUpperFields(List<Integer> upperFields) {
		this.upperFields = upperFields;
	}

	public List<FieldExpressionSimpleTO> getFieldExpressionSimpleList() {
		return fieldExpressionSimpleList;
	}

	public void setFieldExpressionSimpleList(
			List<FieldExpressionSimpleTO> filterFieldsWithMatcher) {
		this.fieldExpressionSimpleList = filterFieldsWithMatcher;
	}

	public boolean isKeywordIncluded() {
		return keywordIncluded;
	}

	public void setKeywordIncluded(boolean keywordIncluded) {
		this.keywordIncluded = keywordIncluded;
	}

	public Map<Integer, Integer> getCustomSelectSimpleFields() {
		return customSelectSimpleFieldsMap;
	}

	public void setCustomSelectSimpleFields(Map<Integer, Integer> customSelectSimpleFields) {
		this.customSelectSimpleFieldsMap = customSelectSimpleFields;
	}

	public Map<Integer, List<ILabelBean>> getCustomSelects() {
		return customSelects;
	}

	public void setCustomSelects(Map<Integer, List<ILabelBean>> customSelects) {
		this.customSelects = customSelects;
	}

	public Map<Integer, List<TreeNode>> getCustomTrees() {
		return customTrees;
	}

	public void setCustomTrees(Map<Integer, List<TreeNode>> customTrees) {
		this.customTrees = customTrees;
	}

	public Map<Integer, String> getSelectedCustomSelectsStr() {
		return selectedCustomSelectsStr;
	}

	public void setSelectedCustomSelectsStr(
			Map<Integer, String> selectedCustomSelectsStr) {
		this.selectedCustomSelectsStr = selectedCustomSelectsStr;
	}

	public Map<Integer, Integer[]> getSelectedCustomSelects() {
		return selectedCustomSelects;
	}

	public void setSelectedCustomSelects(
			Map<Integer, Integer[]> selectedCustomSelects) {
		this.selectedCustomSelects = selectedCustomSelects;
	}

	public boolean isIncludeOpen() {
		return includeOpen;
	}

	public void setIncludeOpen(boolean includeOpen) {
		this.includeOpen = includeOpen;
	}

	public boolean isIncludeClosed() {
		return includeClosed;
	}

	public void setIncludeClosed(boolean includeClosed) {
		this.includeClosed = includeClosed;
	}

	/**
	 * Creates an list of Integers from an array of Strings
	 * @param string
	 * @return
	 */
	public static Integer[] createIntegerArrFromString(String string) {
		if (string == null) {
			return null;
		}
		List<Integer> integerList = new LinkedList<Integer>();
		String[] strArr = string.split(",");
		for (int i = 0; i < strArr.length; i++) {
			if (strArr[i]!=null) {
				try {
					Integer intValue = Integer.valueOf(strArr[i]);
					//if the string is null or empty it will not be included in the result
					integerList.add(intValue);
				} catch(Exception e) {}
			}
		}
		return GeneralUtils.createIntegerArrFromCollection(integerList);
	}

	/**
	 * Copies the content of the current object to a new object
	 * @return
	 */
	/*public FilterUpperTO copy() {
		FilterUpperTO filterUpperTOCopy = new FilterUpperTO();
		try {
			PropertyUtils.copyProperties(filterUpperTOCopy, this);
		} catch (Exception e) {
			LOGGER.error("Making a shallow copy of a FilterUpperTO failed with " + e.getMessage(), e);
		}
		//PropertyUtils.copyProperties copies only the array objects but should copy the array content
		//to not overwrite the original arrays by setting the matcher context
		List<Integer> listFieldIDs = getListFieldsWithSelection();
		for (Integer fieldID : listFieldIDs) {
			Integer[] selectedValues = getSelectedValuesForField(fieldID);
			filterUpperTOCopy.setSelectedValuesForField(fieldID, copyContent(selectedValues));
		}
		Integer[] selectedConsultantsInformants = getSelectedConsultantsInformants();
		filterUpperTOCopy.setSelectedConsultantsInformants(copyContent(selectedConsultantsInformants));
		if (selectedCustomSelects!=null) {
			Map<Integer, Integer[]> copiedSelectedCustomSelects = new HashMap<Integer, Integer[]>();
			for (Integer fieldID : selectedCustomSelects.keySet()) {
				copiedSelectedCustomSelects.put(fieldID, copyContent(selectedCustomSelects.get(fieldID)));
			}
			filterUpperTOCopy.setSelectedCustomSelects(copiedSelectedCustomSelects);
		}
		return filterUpperTOCopy;
	}*/

	/**
	 * Explicitly copies the selected values array because if only the references are copied
	 * then the MatcherContext related values are replaced in both the original and the copied FilterUpperTO
	 * @param selectedValues
	 * @return
	 */
	/*private Integer[] copyContent(Integer[] selectedValues) {
		Integer[] selectedValuesCopy = null;
		if (selectedValues!=null && selectedValues.length>0) {
			selectedValuesCopy = new Integer[selectedValues.length];
			for (int i = 0; i < selectedValues.length; i++) {
				try {
					selectedValuesCopy[i] = selectedValues[i];
				} catch(Exception e) {
				}
			}
		}
		return selectedValuesCopy;
	}*/

}
