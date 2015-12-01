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

package com.aurel.track.report.datasource.meeting;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.report.execute.IDescriptionAttributes;
import com.aurel.track.admin.customize.category.report.execute.ReportExporter;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldDesignBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.link.ItemLinkBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.linkType.ILinkType.LINK_DIRECTION;
import com.aurel.track.linkType.ILinkType.PARENT_CHILD_EXPRESSION;
import com.aurel.track.linkType.LinkTypeBL;
import com.aurel.track.linkType.MeetingTopicLinkType;
import com.aurel.track.plugin.DatasourceDescriptor;
import com.aurel.track.report.datasource.IPluggableDatasource;
import com.aurel.track.report.datasource.ReportBeanDatasource;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanLink;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.PropertiesHelper;
import com.aurel.track.util.StringArrayParameterUtils;


/**
 * Datasource implementation for Meetings
 * @author Tamas
 *
 */
public class MeetingDatasource extends ReportBeanDatasource {

	private static final Logger LOGGER = LogManager.getLogger(MeetingDatasource.class);

	public static interface MEETING_PARAMETER_NAME {
		public static String MEETING = "meeting";
		public static String MEETING_OPTIONS = MEETING + PARAMETER_NAME.OPTION_SUFFIX;
		public static String MEETING_NAME_FIELD = MEETING + PARAMETER_NAME.NAME_SUFFIX;
		public static String MEETING_NAME_VALUE = PARAMETER_NAME.MAP_PREFIX + MEETING;

		public static String ONLY_ACTIVE_MEETINGS = "onlyActiveMeetings";
		public static String ONLY_ACTIVE_MEETINGS_FIELD = ONLY_ACTIVE_MEETINGS + PARAMETER_NAME.NAME_SUFFIX;
		public static String ONLY_ACTIVE_MEETINGS_VALUE = PARAMETER_NAME.MAP_PREFIX + ONLY_ACTIVE_MEETINGS;

		public static String ONLY_NOT_CLOSED_CHILDREN = "onlyNotClosedChildren";
		public static String ONLY_NOT_CLOSED_CHILDREN_FIELD = ONLY_NOT_CLOSED_CHILDREN + PARAMETER_NAME.NAME_SUFFIX;
		public static String ONLY_NOT_CLOSED_CHILDREN_VALUE = PARAMETER_NAME.MAP_PREFIX + ONLY_NOT_CLOSED_CHILDREN;

		public static String LINK_TYPE = "linkType";
		public static String LINK_TYPE_OPTIONS = LINK_TYPE + PARAMETER_NAME.OPTION_SUFFIX;
		public static String LINK_TYPE_NAME_FIELD = LINK_TYPE + PARAMETER_NAME.NAME_SUFFIX;
		public static String LINK_TYPE_NAME_VALUE = PARAMETER_NAME.MAP_PREFIX + LINK_TYPE;
	}

	/**
	 * The name of the long text custom field for Participants
	 */
	private static String PARTICIPANTS_FIELD_NAME = "Participants";
	private static String FROMTOTIME_FIELD_NAME = "FromToTime";

	/**
	 * The issue type name of the meeting issue (hard coded to differentiate from the children of the meeting)
	 */
	protected static String MEETING_ISSUETYPE = "MeetingIssue";

	public static interface MEETING_JASPER_PARAMETER_NAME {
		public static String PARTICIPANTS = "PARTICIPANTS";
		public static String MEETINGDATE = "MEETINGDATE";
		public static String NOTESBY = "NOTESBY";
		public static String MEETINGTITLE = "MEETINGTITLE";
		public static String FROMTOTIME = "FROMTOTIME";
	}

	// Necessary for the DocxReportExporter
	public static TWorkItemBean WORK_ITEM_BEAN = null;
	public static ReportBeans REPORT_BEANS = null;

	/**
	 * Gets the data source object (a Document object in this case) retrieved using the parameters settings
	 * @param parameters
	 * @param datasourceDescriptor
	 * @param contextMap
	 * @param templateDescriptionMap
	 * @param templateID
	 * @param personBean
	 * @param locale
	 * @return
	 */
	@Override
	public Object getDatasource(Map<String, String[]> parameters, DatasourceDescriptor datasourceDescriptor,
			Map<String, Object> contextMap, Map<String, Object> templateDescriptionMap,
			Integer templateID, TPersonBean personBean, Locale locale) {
		ReportBeans reportBeans = null;
		boolean useProjectSpecificID = false;
		Boolean projectSpecificID = (Boolean)contextMap.get(IPluggableDatasource.CONTEXT_ATTRIBUTE.USE_PROJETC_SPECIFIC_ID);
		if (projectSpecificID!=null) {
			useProjectSpecificID = projectSpecificID.booleanValue();
		}
		Map<String, Object> savedParamsMap = new HashMap<String, Object>();
		String paramSettings = loadParamObjectsAndPropertyStringsAndFromStringArrParams(parameters, locale, savedParamsMap);
		saveParameters(paramSettings, personBean.getObjectID(), templateID);
		Integer meetingID = (Integer)savedParamsMap.get(MEETING_PARAMETER_NAME.MEETING);
		String linkType = (String)savedParamsMap.get(MEETING_PARAMETER_NAME.LINK_TYPE);
		Boolean onlyActiveChildren = (Boolean)savedParamsMap.get(MEETING_PARAMETER_NAME.ONLY_NOT_CLOSED_CHILDREN);
		reportBeans = getReportBeans(meetingID, onlyActiveChildren, linkType, personBean, locale);//  new ReportBeans(reportBeanList, locale, null, false);
		REPORT_BEANS = reportBeans;
		String exportFormat = (String)contextMap.get(CONTEXT_ATTRIBUTE.EXPORT_FORMAT);
		if (exportFormat==null) {
			//not dynamically generated, get it from description
			if (templateDescriptionMap!=null) {
				exportFormat = (String)templateDescriptionMap.get(IDescriptionAttributes.FORMAT);
			}
		}
		boolean withHistory = false;
		Boolean history = false;
		if (templateDescriptionMap!=null) {
			if (templateDescriptionMap.get(IDescriptionAttributes.HISTORY)!=null) {
				try {
					history = Boolean.valueOf((String)templateDescriptionMap.get(IDescriptionAttributes.HISTORY));
					if (history!=null) {
						withHistory = history.booleanValue();
					}
				} catch (Exception e) {
					LOGGER.warn("history should be a boolean string " + e.getMessage());
				}
			}
		}
		//if it will be exported to CSV or XLS then make the long texts plain, otherwise simplified HTML
		boolean longTextIsPlain = ReportExporter.FORMAT_CSV.equals(exportFormat) ||
			ReportExporter.FORMAT_XLS.equals(exportFormat) ||
			ReportExporter.FORMAT_XML.equals(exportFormat);
		return getDocumentFromReportBeans(reportBeans, withHistory, false,
			personBean, locale, null, null, longTextIsPlain, useProjectSpecificID);
	}

	/**
	 * Gets the report beans
	 * @param meetingID
	 * @param onlyActiveChildren
	 * @param linkType
	 * @param personBean
	 * @param locale
	 * @return
	 */
	protected ReportBeans getReportBeans(Integer meetingID, Boolean onlyActiveChildren, String linkType, TPersonBean personBean, Locale locale) {
		Integer direction = null;
		if (onlyActiveChildren==null || onlyActiveChildren.booleanValue()) {
			direction = PARENT_CHILD_EXPRESSION.ALL_NOT_CLOSED_CHILDREN;
		} else {
			direction = PARENT_CHILD_EXPRESSION.ALL_CHILDREN;
		}
		List<ReportBean> reportBeanList = null;
		if (meetingID!=null) {
			Set<Integer> childrenSet = ItemBL.getChildHierarchy(
					new int[] {meetingID}, direction, null, null, null);
			childrenSet.add(meetingID);
			List<TWorkItemBean> workItemBeanList = new LinkedList<TWorkItemBean>();
			reportBeanList = LoadItemIDListItems.getReportBeansByWorkItemIDs(
					GeneralUtils.createIntArrFromIntegerCollection(childrenSet),
					false, personBean.getObjectID(), locale, false);
			if (reportBeanList!=null) {
				//set a hardcoded issue type for the parent meeting
				for (ReportBean reportBean : reportBeanList) {
					TWorkItemBean workItemBean = reportBean.getWorkItemBean();
					if (meetingID.equals(workItemBean.getObjectID())) {
						//remove all links from meeting
						reportBean.setReportBeanLinksSet(null);
					}
					WORK_ITEM_BEAN = workItemBean;
					workItemBeanList.add(workItemBean);
					Integer objectID = workItemBean.getObjectID();
					if (objectID.equals(meetingID)) {
						reportBean.getShowISOValuesMap().put(SystemFields.INTEGER_ISSUETYPE, MEETING_ISSUETYPE);
						break;
					}
				}
			}
			if (linkType!=null) {
				Integer linkTypeID = null;
				Integer linkDirection = null;
				if (linkType!=null) {
					Integer[] parts = MergeUtil.getParts(linkType);
					if (parts!=null && parts.length==2) {
						linkTypeID = parts[0];
						linkDirection = parts[1];
					}
				}
				//the linked itemIDs sorted by sortOrder
				List<Integer> linkedWorkItemIDs = ItemLinkBL.getLinkedItemIDs(meetingID, linkTypeID, linkDirection);
				if (linkedWorkItemIDs!=null && !linkedWorkItemIDs.isEmpty()) {
					List<ReportBean> linkedReportBeans = LoadItemIDListItems.getReportBeansByWorkItemIDs(
						GeneralUtils.createIntArrFromIntegerList(linkedWorkItemIDs), false, personBean.getObjectID(), locale, false);
					Map<Integer, ReportBean> reportBeanMap = new HashMap<Integer, ReportBean>();
					for (ReportBean reportBean : linkedReportBeans) {
						SortedSet<ReportBeanLink> reportBeanLinks = reportBean.getReportBeanLinksSet();
						if (reportBeanLinks!=null) {
							for (Iterator<ReportBeanLink> iterator = reportBeanLinks.iterator(); iterator.hasNext();) {
								ReportBeanLink reportBeanLink = iterator.next();
								Integer linkedItemID = reportBeanLink.getWorkItemID();
								if (!meetingID.equals(linkedItemID)) {
									//leave only the meeting topic links to meeting
									iterator.remove();
								}
							}
						}
						reportBeanMap.put(reportBean.getWorkItemBean().getObjectID(), reportBean);
					}
					for (Integer linkedItemID : linkedWorkItemIDs) {
						ReportBean reportBean = reportBeanMap.get(linkedItemID);
						if (reportBean!=null) {
							//enforce the link order
							reportBeanList.add(reportBean);
						}
					}
				}
			}
		}
		return new ReportBeans(reportBeanList, locale, null, false);
	}

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
	@Override
	public Map<String, Object> getJasperReportParameters(Map<String, String[]> params, DatasourceDescriptor datasourceDescriptor,
			Map<String, Object> contextMap, Map<String, Object> templateDescriptionMap,
			Integer templateID, TPersonBean personBean, Locale locale) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		Boolean fromIssueNavigator = (Boolean)contextMap.get(CONTEXT_ATTRIBUTE.FROM_ISSUE_NAVIGATOR);
		Integer meetingID = null;
		List<Integer> workItemIDs = (List<Integer>)contextMap.get(CONTEXT_ATTRIBUTE.WORKITEMIDS);
		if (fromIssueNavigator!=null && fromIssueNavigator.booleanValue() && workItemIDs!=null && !workItemIDs.isEmpty()) {
			meetingID = workItemIDs.get(0);
		} else {
			meetingID = StringArrayParameterUtils.parseIntegerValue(params, MEETING_PARAMETER_NAME.MEETING);
		}
		TWorkItemBean workItemBean = null;
		if (meetingID!=null) {
			workItemBean = ItemBL.loadWorkItemWithCustomFields(meetingID);
			if (workItemBean!=null) {
				Date meetingDate = workItemBean.getEndDate();
				if (meetingDate==null) {
					meetingDate = workItemBean.getStartDate();
					if (meetingDate==null) {
						meetingDate = workItemBean.getCreated();
					}
				}
				parameters.put(MEETING_JASPER_PARAMETER_NAME.MEETINGDATE, meetingDate);
				Integer responsibleID = workItemBean.getResponsibleID();
				TPersonBean reponsiblePersonBean = LookupContainer.getPersonBean(responsibleID);
				if (reponsiblePersonBean!=null) {
					parameters.put(MEETING_JASPER_PARAMETER_NAME.NOTESBY, reponsiblePersonBean.getLabel());
				}
				parameters.put(MEETING_JASPER_PARAMETER_NAME.MEETINGTITLE, workItemBean.getSynopsis());
				Integer participantFieldID = null;
				List<TFieldBean> participantsFieldBeans = FieldDesignBL.loadByName(PARTICIPANTS_FIELD_NAME);
				if (participantsFieldBeans!=null && !participantsFieldBeans.isEmpty()) {
					participantFieldID = participantsFieldBeans.get(0).getObjectID();
					parameters.put(MEETING_JASPER_PARAMETER_NAME.PARTICIPANTS, workItemBean.getAttribute(participantFieldID));
				}
				Integer fromToTimeFieldID = null;
				List<TFieldBean> fromToTimeFieldBeans = FieldDesignBL.loadByName(FROMTOTIME_FIELD_NAME);
				if (fromToTimeFieldBeans!=null && !fromToTimeFieldBeans.isEmpty()) {
					fromToTimeFieldID = fromToTimeFieldBeans.get(0).getObjectID();
					parameters.put(MEETING_JASPER_PARAMETER_NAME.FROMTOTIME, workItemBean.getAttribute(fromToTimeFieldID));
				}
			}
		}
		return parameters;
	}

	/**
	 * Gets the datasource parameters
	 * @param datasourceDescriptor
	 * @param contextMap
	 * @param savedParamsMap
	 * @param personBean
	 * @param locale
	 * @return
	 */
	@Override
	protected String getDatasourceParams(DatasourceDescriptor datasourceDescriptor,
			Map<String, Object> contextMap, Map<String, Object> savedParamsMap, TPersonBean personBean, Locale locale, Boolean fromIssueNavigator) {
		//override the default because the classic datasource (project/filter) is not needed only extra parameters
		return getDatasourceExtraParams(savedParamsMap, datasourceDescriptor, contextMap, personBean, locale);
	}

	/**
	 * Extra datasource configurations
	 * Whether beyond the item list also other datasource configuration parameters are needed (important when coming from item navigator)
	 * @param savedParamsMap
	 * @param datasourceDescriptor
	 * @param personBean
	 * @param locale
	 * @return
	 */
	@Override
	public String getDatasourceExtraParams(Map<String, Object> savedParamsMap,
			DatasourceDescriptor datasourceDescriptor, Map<String, Object> contextMap,
			TPersonBean personBean, Locale locale) {
		//not null to trigger
		StringBuilder stringBuilder = new StringBuilder();
		List<Integer> workItemIDs = (List<Integer>)contextMap.get(CONTEXT_ATTRIBUTE.WORKITEMIDS);
		Integer meetingID = null;
		if (workItemIDs==null || workItemIDs.isEmpty()) {
			//previous meeting executed if any
			meetingID = (Integer)savedParamsMap.get(MEETING_PARAMETER_NAME.MEETING);
		} else {
			//report called by right clicking an issue in issue navigator
			meetingID = workItemIDs.get(0);
		}
		JSONUtility.appendStringValue(stringBuilder, MEETING_PARAMETER_NAME.MEETING_NAME_FIELD, MEETING_PARAMETER_NAME.MEETING_NAME_VALUE);
		Boolean onlyActiveMeetings = (Boolean)savedParamsMap.get(MEETING_PARAMETER_NAME.ONLY_ACTIVE_MEETINGS);
		if (onlyActiveMeetings==null) {
			onlyActiveMeetings = Boolean.TRUE;
		}
		JSONUtility.appendBooleanValue(stringBuilder, MEETING_PARAMETER_NAME.ONLY_ACTIVE_MEETINGS, onlyActiveMeetings.booleanValue());
		JSONUtility.appendStringValue(stringBuilder, MEETING_PARAMETER_NAME.ONLY_ACTIVE_MEETINGS_FIELD,
				MEETING_PARAMETER_NAME.ONLY_ACTIVE_MEETINGS_VALUE);

		Boolean onlyOpenChilderen = (Boolean)savedParamsMap.get(MEETING_PARAMETER_NAME.ONLY_NOT_CLOSED_CHILDREN);
		if (onlyOpenChilderen==null) {
			onlyOpenChilderen = Boolean.TRUE;
		}
		JSONUtility.appendBooleanValue(stringBuilder, MEETING_PARAMETER_NAME.ONLY_NOT_CLOSED_CHILDREN, onlyOpenChilderen.booleanValue());
		JSONUtility.appendStringValue(stringBuilder, MEETING_PARAMETER_NAME.ONLY_NOT_CLOSED_CHILDREN_FIELD,
				MEETING_PARAMETER_NAME.ONLY_NOT_CLOSED_CHILDREN_VALUE);
		List<IntegerStringBean> meetingOptions = new LinkedList<IntegerStringBean>();
		meetingID = getMeetingOptions(meetingID, personBean, locale, onlyActiveMeetings.booleanValue(), meetingOptions);
		JSONUtility.appendIntegerValue(stringBuilder, MEETING_PARAMETER_NAME.MEETING, meetingID);
		JSONUtility.appendIntegerStringBeanList(stringBuilder,
				MEETING_PARAMETER_NAME.MEETING_OPTIONS, meetingOptions);

		JSONUtility.appendStringValue(stringBuilder, MEETING_PARAMETER_NAME.LINK_TYPE_NAME_FIELD, MEETING_PARAMETER_NAME.LINK_TYPE_NAME_VALUE);
		List<LabelValueBean> linkTypeOptions = LinkTypeBL.getLinkTypeFilterSupersetExpressions(locale, false);
		String linkTypeID = (String)savedParamsMap.get(MEETING_PARAMETER_NAME.LINK_TYPE);
		if (linkTypeID==null) {
			List<Integer> meetingLinkTypes = LinkTypeBL.getLinkTypesByPluginClass(MeetingTopicLinkType.getInstance());
			if (meetingLinkTypes!=null) {
				for (LabelValueBean labelValueBean : linkTypeOptions) {
					String linkTypeIDAndDirection = labelValueBean.getValue();
					if (linkTypeIDAndDirection!=null && linkTypeIDAndDirection.length()>0) {
						Integer[] parts = MergeUtil.getParts(linkTypeIDAndDirection);
						if (parts!=null && parts.length==2) {
							Integer linkTypeIDCrt = parts[0];
							Integer direction = parts[1];
							if (linkTypeIDCrt!=null && meetingLinkTypes.contains(linkTypeIDCrt) && direction!=null && direction.equals(LINK_DIRECTION.LEFT_TO_RIGHT)) {
								linkTypeID = linkTypeIDAndDirection;
								break;
							}
						}
					}
				}
			}
		}
		JSONUtility.appendStringValue(stringBuilder, MEETING_PARAMETER_NAME.LINK_TYPE, linkTypeID);
		JSONUtility.appendLabelValueBeanList(stringBuilder,
				MEETING_PARAMETER_NAME.LINK_TYPE_OPTIONS, linkTypeOptions, true);
		return stringBuilder.toString();
	}

	/**
	 * Gets the meeting options
	 * @param personID
	 * @param locale
	 * @param onlyActiveMeetings
	 * @return
	 */
	private static Integer getMeetingOptions(Integer currentMeetingID, TPersonBean personBean,
			Locale locale, boolean onlyActiveMeetings, List<IntegerStringBean> meetingOptions) {
		FilterUpperTO filterUpperTO = new FilterUpperTO();
		List<TProjectBean> usedProjects = ProjectBL.loadUsedProjectsFlat(personBean.getObjectID());
		filterUpperTO.setSelectedProjects(GeneralUtils.createIntegerArrFromCollection(GeneralUtils.createIntegerListFromBeanList(usedProjects)));
		List<TListTypeBean> meetingIssueTypes = IssueTypeBL.loadByTypeFlag(TListTypeBean.TYPEFLAGS.MEETING);
		filterUpperTO.setSelectedIssueTypes(GeneralUtils.createIntegerArrFromCollection(GeneralUtils.createIntegerListFromBeanList(meetingIssueTypes)));
		if (onlyActiveMeetings) {
			List<TStateBean> activeStateBeans = StatusBL.loadActiveStates();
			filterUpperTO.setSelectedStates(GeneralUtils.createIntegerArrFromCollection(GeneralUtils.createIntegerListFromBeanList(activeStateBeans)));
		}
		//get and sort the meetings by date
		List<TWorkItemBean> meetingWorkItems = null;
		try {
			meetingWorkItems = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, personBean, locale, false);
		} catch (TooManyItemsToLoadException e) {
			LOGGER.info("Number of items to load " + e.getItemCount());
		}
		SortedMap<Date , List<IntegerStringBean>> meetingsByDate = new TreeMap<Date, List<IntegerStringBean>>();
		boolean meetingFound = false;
		if (meetingWorkItems!=null) {
			for (TWorkItemBean workItemBean : meetingWorkItems) {
				Integer workItemID = workItemBean.getObjectID();
				String title = workItemBean.getSynopsis();
				Date meetingDate = workItemBean.getEndDate();
				if (meetingDate==null) {
					meetingDate = workItemBean.getStartDate();
					if (meetingDate==null) {
						meetingDate = workItemBean.getCreated();
					}
				}
				if (currentMeetingID!=null && !meetingFound) {
					if (currentMeetingID.equals(workItemID)) {
						meetingFound = true;
					}
				}
				String meetingTitle = DateTimeUtils.getInstance().formatGUIDate(meetingDate, locale) + " " + title;
				List<IntegerStringBean> meetingsOnDate = meetingsByDate.get(meetingDate);
				if (meetingsOnDate==null) {
					meetingsOnDate = new LinkedList<IntegerStringBean>();
					meetingsByDate.put(meetingDate, meetingsOnDate);
				}
				meetingsOnDate.add(new IntegerStringBean(meetingTitle, workItemID));
			}
		}

		for (Date date : meetingsByDate.keySet()) {
			List<IntegerStringBean> meetingsOnDate = meetingsByDate.get(date);
			if (meetingsOnDate!=null) {
				if (meetingsOnDate.size()>1) {
					Collections.sort(meetingsOnDate);
				}
				meetingOptions.addAll(0, meetingsOnDate);
			}
		}
		if (meetingFound) {
			return currentMeetingID;
		} else {
			if (meetingOptions!=null && !meetingOptions.isEmpty()) {
				return meetingOptions.get(0).getValue();
			}
			return null;
		}
	}

	/**
	 *
	 * @param paramSettings
	 * @return
	 */
	@Override
	protected Map<String, Object> loadParamObjectsFromPropertyStrings(String paramSettings) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		if (paramSettings!=null) {
			Integer meetingID = PropertiesHelper.getIntegerProperty(paramSettings, MEETING_PARAMETER_NAME.MEETING);
			Boolean onlyOpenMeetings = PropertiesHelper.getBooleanProperty(paramSettings, MEETING_PARAMETER_NAME.ONLY_ACTIVE_MEETINGS);
			Boolean onlyOpenChildren = PropertiesHelper.getBooleanProperty(paramSettings, MEETING_PARAMETER_NAME.ONLY_NOT_CLOSED_CHILDREN);
			String linkType = PropertiesHelper.getProperty(paramSettings, MEETING_PARAMETER_NAME.LINK_TYPE);
			paramsMap.put(MEETING_PARAMETER_NAME.MEETING, meetingID);
			paramsMap.put(MEETING_PARAMETER_NAME.ONLY_ACTIVE_MEETINGS, onlyOpenMeetings);
			paramsMap.put(MEETING_PARAMETER_NAME.ONLY_NOT_CLOSED_CHILDREN, onlyOpenChildren);
			paramsMap.put(MEETING_PARAMETER_NAME.LINK_TYPE, linkType);
		}
		return paramsMap;
	}

	/**
	 * Gets the parameter settings for report to be executed and the parameters to be saved
	 * @param parameters parameters as String[]
	 * @param locale
	 * @param savedParamsMap output parameter: parameters transformed to the actual types
	 * @return
	 */
	@Override
	protected String loadParamObjectsAndPropertyStringsAndFromStringArrParams(
			Map<String, String[]> params, Locale locale, Map<String, Object> savedParamsMap) {
		String paramSettings = "";
		Integer meeting = StringArrayParameterUtils.parseIntegerValue(params, MEETING_PARAMETER_NAME.MEETING);
		savedParamsMap.put(MEETING_PARAMETER_NAME.MEETING, meeting);
		Boolean onlyActiveMeetings = StringArrayParameterUtils.getBooleanValue(params, MEETING_PARAMETER_NAME.ONLY_ACTIVE_MEETINGS);
		if (onlyActiveMeetings==null) {
			onlyActiveMeetings = Boolean.FALSE;
		}
		savedParamsMap.put(MEETING_PARAMETER_NAME.ONLY_ACTIVE_MEETINGS, onlyActiveMeetings);
		Boolean onlyNotClosedChildren = StringArrayParameterUtils.getBooleanValue(params, MEETING_PARAMETER_NAME.ONLY_NOT_CLOSED_CHILDREN);
		if (onlyNotClosedChildren==null) {
			onlyNotClosedChildren = Boolean.FALSE;
		}
		savedParamsMap.put(MEETING_PARAMETER_NAME.ONLY_NOT_CLOSED_CHILDREN, onlyNotClosedChildren);
		String linkType = StringArrayParameterUtils.getStringValue(params, MEETING_PARAMETER_NAME.LINK_TYPE);
		savedParamsMap.put(MEETING_PARAMETER_NAME.LINK_TYPE, linkType);

		paramSettings = PropertiesHelper.setIntegerProperty(paramSettings, MEETING_PARAMETER_NAME.MEETING, meeting);
		paramSettings = PropertiesHelper.setBooleanProperty(paramSettings, MEETING_PARAMETER_NAME.ONLY_ACTIVE_MEETINGS, onlyActiveMeetings);
		paramSettings = PropertiesHelper.setBooleanProperty(paramSettings, MEETING_PARAMETER_NAME.ONLY_NOT_CLOSED_CHILDREN, onlyNotClosedChildren);
		paramSettings = PropertiesHelper.setProperty(paramSettings, MEETING_PARAMETER_NAME.LINK_TYPE, linkType);
		return paramSettings;
	}

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
	@Override
	public String refreshParameters(Map<String, String[]> parameters, Integer templateID,
			DatasourceDescriptor datasourceDescriptor, Map<String, Object> contextMap,
			TPersonBean personBean, Locale locale) {
		Integer meeting = StringArrayParameterUtils.parseIntegerValue(parameters, MEETING_PARAMETER_NAME.MEETING);
		Boolean onlyActiveMeetings = StringArrayParameterUtils.getBooleanValue(parameters, MEETING_PARAMETER_NAME.ONLY_ACTIVE_MEETINGS);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");
		List<IntegerStringBean> meetingOptions = new LinkedList<IntegerStringBean>();
		meeting = getMeetingOptions(meeting,
				personBean, locale, onlyActiveMeetings.booleanValue(), meetingOptions);
		if (meetingOptions!=null && !meetingOptions.isEmpty()) {
			JSONUtility.appendIntegerValue(stringBuilder, MEETING_PARAMETER_NAME.MEETING_NAME_VALUE, meeting);
			JSONUtility.appendIntegerStringBeanList(stringBuilder, MEETING_PARAMETER_NAME.MEETING_OPTIONS, meetingOptions, true);
		}
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
}
