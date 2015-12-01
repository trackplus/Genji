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

package com.aurel.track.linkType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean.LAG_FORMAT;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean.PREDECESSOR_ELEMENT_TYPE;
import com.aurel.track.exchange.msProject.importer.LinkLagBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.item.budgetCost.AccountingBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.ParameterMetadata;
import com.aurel.track.util.numberFormatter.DoubleNumberFormatUtil;

public class MsProjectLinkType extends AbstractSpecificLinkType {
	private static final Logger LOGGER = Logger.getLogger(MsProjectLinkType.class);
	/*public static Integer DEPENDENCY_TYPE_PARAM = Integer.valueOf(0);
	public static Integer LINKLAG_PARAM = Integer.valueOf(1);
	public static Integer LAGFORMAT_PARAM = Integer.valueOf(2);*/
	
	public static String DEPENDENCY_TYPE = "DependencyType";
	public static String LINKLAG = "Lag";
	public static String LAGFORMAT = "Lagformat";

	private static MsProjectLinkType instance;

	/**
	 * Return a FieldConfigItemFacade instance which implements the ConfigItemFacade
	 * @return
	 */
	public static MsProjectLinkType getInstance(){
		if(instance==null){
			instance=new MsProjectLinkType();
		}
		return instance;
	}

	@Override
	public int getPossibleDirection() {
		return LINK_DIRECTION.RIGHT_TO_LEFT;
	}

	/**
	 * Whether this link type appears in gantt view
	 * @return
	 */
	@Override
	public boolean isGanttSpecific() {
		return true;
	}




	/**
	 * Get the date value of the dependent workItem
	 *
	 * @param workItemBean
	 * @return
	 */
	/*protected Date getDependentDate(TWorkItemBean workItemBean, Integer dependencyType) {
		if (dependencyType==null) {
			dependencyType = PREDECESSOR_ELEMENT_TYPE.FS;
		}
		switch (dependencyType.intValue()) {
		case PREDECESSOR_ELEMENT_TYPE.FS:
			return workItemBean.getStartDate();
		case PREDECESSOR_ELEMENT_TYPE.SS:
			return workItemBean.getStartDate();
		case PREDECESSOR_ELEMENT_TYPE.FF:
			return workItemBean.getEndDate();
		case PREDECESSOR_ELEMENT_TYPE.SF:
			return workItemBean.getEndDate();
		default:
			return workItemBean.getStartDate();
		}
	}*/

	/**
	 * Gets the item type specific data for a reportBeanLink
	 * @param workItemLinkBean
	 * @return
	 */
	@Override
	public ItemLinkSpecificData getItemLinkSpecificData(TWorkItemLinkBean workItemLinkBean) {
		MsProjectLinkSpecificData msProjectLinkSpecificData = new MsProjectLinkSpecificData();
		msProjectLinkSpecificData.setDependencyType(decodeLinkTypeForGantt(workItemLinkBean.getIntegerValue1()));
		msProjectLinkSpecificData.setLinkLag(workItemLinkBean.getLinkLag());
		msProjectLinkSpecificData.setLinkLagFormat(workItemLinkBean.getLinkLagFormat());
		return msProjectLinkSpecificData;
	}

	/**
	 * Gets the itemLinkSpecificData as a string map for serializing
	 * @param itemLinkSpecificData
	 * @param locale
	 * @return
	 */
	@Override
	public Map<String, String> serializeItemLinkSpecificData(ItemLinkSpecificData itemLinkSpecificData, Locale locale) {
		if (itemLinkSpecificData!=null) {
			try {
				MsProjectLinkSpecificData msProjectLinkSpecificData = (MsProjectLinkSpecificData)itemLinkSpecificData;
				if (msProjectLinkSpecificData!=null) {
					Integer dependencyType = msProjectLinkSpecificData.getDependencyType();
					if (dependencyType!=null) {
						Map<String, String> itemLinkSpecificDataMap = new HashMap<String, String>();
						itemLinkSpecificDataMap.put("dependencyType", getDependencyTypeName(encodeLinkTypeFromGantt(dependencyType), locale));
						Double lag = msProjectLinkSpecificData.getLinkLag();
						if (lag!=null) {
							itemLinkSpecificDataMap.put("lag", String.valueOf(lag) + " " + getLocalizedLagFormat(msProjectLinkSpecificData.getLinkLagFormat(), locale));
						}
						return itemLinkSpecificDataMap;
					}
				}
			} catch (Exception e) {
				LOGGER.warn("Converting the itemLinkSpecificData to MeetingTopicLinkSpecificData failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return null;
	}

	/**
	 * Translate the internal link type to the link type expected by Gantt
	 * @param linkType
	 * @return
	 */
	private static int decodeLinkTypeForGantt(Integer linkType) {
		int ganttDependencyType = 2;
		if (linkType!=null) {
			switch(linkType.intValue()) {
				case PREDECESSOR_ELEMENT_TYPE.FF:
					ganttDependencyType = 3;
					break;
				case PREDECESSOR_ELEMENT_TYPE.FS:
					ganttDependencyType = 2;
					break;
				case PREDECESSOR_ELEMENT_TYPE.SF:
					ganttDependencyType = 1;
					break;
				case PREDECESSOR_ELEMENT_TYPE.SS:
					ganttDependencyType = 0;
					break;
			}
		}
		return ganttDependencyType;
	}

	/**
	 * Translate the internal link type to the link type expected by Gantt
	 * @param linkType
	 * @return
	 */
	private static int encodeLinkTypeFromGantt(Integer linkType) {
		int dependencyType = PREDECESSOR_ELEMENT_TYPE.FS;
		if (linkType!=null) {
			switch(linkType.intValue()) {
				case 3:
					dependencyType = PREDECESSOR_ELEMENT_TYPE.FF;
					break;
				case 2:
					dependencyType = PREDECESSOR_ELEMENT_TYPE.FS;
					break;
				case 1:
					dependencyType = PREDECESSOR_ELEMENT_TYPE.SF;
					break;
				case 0:
					dependencyType = PREDECESSOR_ELEMENT_TYPE.SS;
					break;
			}
		}
		return dependencyType;
	}

	/**
	 * Validation called before saving the issue
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 * @param personID
	 * @param predToSuccLinksOfType
	 * @param succToPredLinksOfType
	 * @param confirm
	 * @param locale
	 * @return
	 */
	/*public List<ErrorData> validateBeforeIssueSave(TWorkItemBean workItemBean,
			TWorkItemBean workItemBeanOriginal, Integer personID,
			List<TWorkItemLinkBean> predToSuccLinksOfType,
			List<TWorkItemLinkBean> succToPredLinksOfType, boolean confirm, Locale locale) {
		List<ErrorData> errorsList = new ArrayList<ErrorData>();
		//verify only if start or end date is specified
		if (MsProjectLinkTypeBL.getStartDate(workItemBean) != null || MsProjectLinkTypeBL.getEndDate(workItemBean)!=null) {
			//verify only if start or end date was really changed now. If the conflict is not caused by current changes then
			//no date related error message should come which has nothing to do with the current changes
			if (workItemBeanOriginal!=null && (EqualUtils.notEqual(ItemMoveBL.getStartDate(workItemBean), ItemMoveBL.getStartDate(workItemBeanOriginal)) ||
					EqualUtils.notEqual(ItemMoveBL.getEndDate(workItemBean), ItemMoveBL.getEndDate(workItemBeanOriginal)))) {

//				Set<Integer> problems = MsProjectLinkTypeBL.checkSuccMoveValidity(MsProjectLinkTypeBL.getStartDate(workItemBean), MsProjectLinkTypeBL.getEndDate(workItemBean),
//				Set<Integer> botomUpAnchestorProblem = MsProjectLinkTypeBL.validateAncestorBottomUpLinks(workItemBean, MsProjectLinkTypeBL.getStartDate(workItemBean),
			}
		}
		return errorsList;
	}*/

	/**
	 * Validates the workItemLink before save
	 * @param workItemLinkBean
	 * @param workItemLinkOriginal
	 * @param workItemsLinksMap
	 * @param predWorkItem
	 * @param succWorkItem
	 * @param personBean
	 * @param locale
	 * @return
	 */
	@Override
	public List<ErrorData> validateBeforeSave(TWorkItemLinkBean workItemLinkBean, TWorkItemLinkBean workItemLinkOriginal,
			SortedMap<Integer, TWorkItemLinkBean> workItemsLinksMap,
			TWorkItemBean predWorkItem, TWorkItemBean succWorkItem, TPersonBean personBean, Locale locale) {
		List<ErrorData> errorDataList = super.validateBeforeSave(workItemLinkBean, workItemLinkOriginal, workItemsLinksMap, predWorkItem, succWorkItem, personBean, locale);

/*		int lagDifference;
		if (workItemLinkOriginal == null) {
			lagDifference = ItemMoveBL.getLinkLagInDays(workItemLinkBean, ItemMoveBL.getHoursPerWorkingDayForWorkItem(workItemLinkBean.getLinkPred()));
		} else {

			int lagInDays = ItemMoveBL.getLinkLagInDays(workItemLinkBean, ItemMoveBL.getHoursPerWorkingDayForWorkItem(workItemLinkBean.getLinkPred()));
			int oldLinkLagInDays = ItemMoveBL.getLinkLagInDays(workItemLinkOriginal, ItemMoveBL.getHoursPerWorkingDayForWorkItem(workItemLinkBean.getLinkPred()));

			if((lagInDays > 0 && oldLinkLagInDays > 0) || (lagInDays < 0 && oldLinkLagInDays < 0)) {
				lagDifference = Math.abs(lagInDays - oldLinkLagInDays);
			}else if(lagInDays < 0) {
				lagDifference = Math.abs(lagInDays - oldLinkLagInDays);
			}else {
				lagDifference = Math.abs(oldLinkLagInDays - lagInDays);
			}

			if(lagInDays < oldLinkLagInDays) {
				lagDifference *= -1;
			}
		}
		Date startDate = null;
		Date endDate = null;
		if(lagDifference < 0) {
			startDate = ItemMoveBL.stepBack(ItemMoveBL.getStartDate(succWorkItem), Math.abs(lagDifference), true);
			endDate = ItemMoveBL.stepBack(ItemMoveBL.getEndDate(succWorkItem), Math.abs(lagDifference), true);
		}
		if(lagDifference > 0) {
			startDate = ItemMoveBL.stepForward(ItemMoveBL.getStartDate(succWorkItem), lagDifference, true);
			endDate = ItemMoveBL.stepForward(ItemMoveBL.getEndDate(succWorkItem), lagDifference, true);
		}

		if(workItemLinkOriginal == null) {
			Tuple tuple = ItemMoveBL.getTupleFromLinkedBeans(predWorkItem, succWorkItem, workItemLinkBean);
			if(tuple != null) {
				int numOfDaysToMove = ItemMoveBL.getNrOfDaysNeededToMove(tuple.getSource().getNodeDate(), tuple.getTarget().getNodeDate(), lagDifference);
				if(numOfDaysToMove != 0) {
					startDate = ItemMoveBL.stepForward(ItemMoveBL.getStartDate(succWorkItem), numOfDaysToMove, true);
					endDate = ItemMoveBL.stepForward(ItemMoveBL.getEndDate(succWorkItem), numOfDaysToMove, true);
				}
			}
		}
		if(startDate != null && endDate != null) {
			Set<Integer> problems = ItemMoveBL.moveItem(succWorkItem, startDate, endDate, true, false, workItemLinkBean, personBean, locale);
			if (problems.size() > 0) {
				ErrorData errorDataViol = new ErrorData("itemov.ganttView.dependency.violation.without.remove");
				errorDataList.add(errorDataViol);
			}
		}*/
		/*Set<Integer> problems = MsProjectLinkTypeBL.handleLagAndCascade(lagDifference, workItemLinkBean, predWorkItem, succWorkItem, personBean.getObjectID(), locale, workItemLinkOriginal==null);
		if (problems.size() > 0) {
			ErrorData errorDataViol = new ErrorData("itemov.ganttView.dependency.violation.without.remove");
			errorDataList.add(errorDataViol);
		}*/
		return errorDataList;
	}

	/**
	 * Gets the start and end dates for the ancestor items
	 * @param parentID
	 * @param workItemBean
	 * @return
	 */
	/*private static Map<Integer, Date[]> createParentOldStartEndDateMap(Integer parentID, TWorkItemBean workItemBean) {
		Map<Integer, Date[]>  parents = new HashMap<Integer, Date[]>();
		while (parentID != null) {
			TWorkItemBean parentWorkItem = null;
			try {
				parentWorkItem = ItemBL.loadWorkItem(parentID);
				if(parentWorkItem != null) {
					Date[] tmp = new Date[2];
					tmp[0] = MsProjectLinkTypeBL.getStartDate(parentWorkItem);
					tmp[1] = MsProjectLinkTypeBL.getEndDate(parentWorkItem);
					parents.put(parentWorkItem.getObjectID(), tmp);
				}
			} catch (ItemLoaderException e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
			parentID = null;
			parentID = parentWorkItem.getSuperiorworkitem();
		}
		return parents;
	}*/

	/**
	 *  Called after the issue is saved
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 * @param personID
	 * @param predToSuccLinksOfType
	 * @param succToPredLinksOfType
	 * @param locale
	 */
	@Override
	public void afterIssueSave(TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal, Integer personID, List<TWorkItemLinkBean> predToSuccLinksOfType,
			List<TWorkItemLinkBean> succToPredLinksOfType, Locale locale) {
		//MsProjectLinkTypeBL.planItem(workItemBeanOriginal, oldStartDate, oldEndDate, startDate, endDate, locale, msProjectLinkTypeIDs, personID, storeCurrentWorkitem);

		//MsProjectLinkType msProjectLinkType = MsProjectLinkType.getInstance();

		//List<Integer> msProjectLinkTypeIDs = LinkTypeBL.getLinkTypesByPluginClass(msProjectLinkType);

//		MsProjectLinkTypeBL.planItem(workItemBean, MsProjectLinkTypeBL.getStartDate(workItemBeanOriginal), MsProjectLinkTypeBL.getEndDate(workItemBeanOriginal),


		/*Integer parent = workItemBeanOriginal.getSuperiorworkitem();
		Map<Integer, Date[]> allParentsAllSuccessorWorkItemIDs = null;
		allParentsAllSuccessorWorkItemIDs = createParentOldStartEndDateMap(parent, workItemBean);
		if(allParentsAllSuccessorWorkItemIDs != null) {
			for(Map.Entry<Integer, Date[]> entry : allParentsAllSuccessorWorkItemIDs.entrySet()) {
				try {
					TWorkItemBean currentWorkItem = ItemBL.loadWorkItem(entry.getKey());
					ItemMoveBL.moveItem(currentWorkItem, entry.getValue()[0], entry.getValue()[1], true, null, PersonBL.loadByPrimaryKey(personID), locale);
				} catch (ItemLoaderException e) {
					LOGGER.error(ExceptionUtils.getStackTrace(e));
					LOGGER.error("Error while cascading parent work items successors: " + e.getMessage());
				}
			}
		}*/
	}

	/**
	 * Whether the dependent task's date is before the linked task's date
	 * @param dependentTaskDate
	 * @param linkedTaskDate
	 * @param linkLag
	 * @param linkLagFormat
	 * @return
	 */
	/*protected boolean isBefore(Date dependentTaskDate, Date linkedTaskDate,
			Double linkLag, Integer lagFormat, double hoursPerWorkingDay,
			Double predecessorDuration) {
		Calendar calDependentTask = Calendar.getInstance();
		calDependentTask.setTime(dependentTaskDate);
		calDependentTask.add(Calendar.DATE, 1);
		CalendarUtil.clearTime(calDependentTask);
		if (linkLag==null || linkLag.intValue()==0) {
			return calDependentTask.getTime().before(linkedTaskDate);
		} else {
			int daysLag = LinkLagBL.getDaysFromLinkLag(linkLag, lagFormat,
					hoursPerWorkingDay, predecessorDuration);
			Calendar calLinkedTask = Calendar.getInstance();
			calLinkedTask.setTime(linkedTaskDate);
			calLinkedTask.add(Calendar.DATE, daysLag);
			return calDependentTask.getTime().before(calLinkedTask.getTime()) ||
				calDependentTask.getTime().getTime()==calLinkedTask.getTime().getTime();
		}
	}*/

	@Override
	public boolean selectableForQueryResultSuperset() {
		return true;
	}

	@Override
	public boolean isHierarchical() {
		return false;
	}

	@Override
	public String prepareParametersOnLinkTab(
			TWorkItemLinkBean workItemLinkBean, Integer linkDirection,
			Locale locale) {
		String lagFormat = getLocalizedLagFormat(
				workItemLinkBean.getLinkLagFormat(), locale);
		Double hoursPerWorkDay = AccountingBL.DEFAULTHOURSPERWORKINGDAY;
		if (LinkLagBL.isHoursPerWorkdayNeeded(workItemLinkBean.getLinkLagFormat())) {
			hoursPerWorkDay = getHoursPerWorkingDayForWorkItem(workItemLinkBean.getLinkPred());
		}
		double linkLag = LinkLagBL.getUILinkLagFromMinutes(workItemLinkBean.getLinkLag(),
				workItemLinkBean.getLinkLagFormat(), hoursPerWorkDay);
		return getDependencyTypeName(workItemLinkBean.getIntegerValue1(), locale) + " " +
				LocalizeUtil.getLocalizedTextFromApplicationResources(
				"item.tabs.itemLink.lbl.lag", locale) + " " +
				DoubleNumberFormatUtil.getInstance().formatGUI(
						linkLag, locale) + " " + lagFormat;
	}

	/**
	 * Prepare the parameters map
	 * Used in webservice
	 * @param workItemLinkBean
	 * @return
	 */
	@Override
	public Map<String, String> prepareParametersMap(TWorkItemLinkBean workItemLinkBean) {
		Map<String, String> parametersMap = new HashMap<String, String>();
		Integer dependencyType = workItemLinkBean.getIntegerValue1();
		if (dependencyType!=null) {
			parametersMap.put(DEPENDENCY_TYPE, String.valueOf(dependencyType));
		}
		Integer lagFormat = workItemLinkBean.getLinkLagFormat();
		if (lagFormat!=null) {
			parametersMap.put(LAGFORMAT, String.valueOf(lagFormat));
		}
		Double hoursPerWorkDay = AccountingBL.DEFAULTHOURSPERWORKINGDAY;
		if (LinkLagBL.isHoursPerWorkdayNeeded(workItemLinkBean.getLinkLagFormat())) {
			hoursPerWorkDay = getHoursPerWorkingDayForWorkItem(workItemLinkBean.getLinkPred());
		}
		double linkLag = LinkLagBL.getUILinkLagFromMinutes(workItemLinkBean.getLinkLag(),
				workItemLinkBean.getLinkLagFormat(), hoursPerWorkDay);
		parametersMap.put(LINKLAG, String.valueOf(linkLag));
		return parametersMap;
	}
	
	/**
	 * Gets the parameter metadata as a list
	 * @param locale
	 * @return
	 */
	@Override
	public List<ParameterMetadata> getParameterMetadata(Locale locale) {
		List<ParameterMetadata> parameterList = new LinkedList<ParameterMetadata>();
		ParameterMetadata dependencyType = new ParameterMetadata(DEPENDENCY_TYPE, "int", true);
		dependencyType.setOptions(getDependencyTypeList(locale));
		parameterList.add(dependencyType);
		ParameterMetadata lag = new ParameterMetadata(LINKLAG, "double", false);
		parameterList.add(lag);
		ParameterMetadata lagFormat = new ParameterMetadata(LAGFORMAT, "int", true);
		lagFormat.setOptions(getLinkLagFormatList(locale));
		parameterList.add(lagFormat);
		return parameterList;
	}
	
	
	static interface JSON_FIELDS {
		static final String DEPENDENCY_TYPE_LABEL = "dependencyTypeLabel";
		static final String DEPENDENCY_TYPES_LIST = "dependencyTypesList";
		static final String DEPENDENCY_TYPE = "dependencyType";
		static final String LINK_LAG_LABEL = "linkLagLabel";
		static final String LINK_LAG = "linkLag";
		static final String LINK_LAG_FORMATS_LIST = "linkLagFormatsList";
		static final String LINK_LAG_FORMAT = "linkLagFormat";
	}

	/**
	 * Gets the JavaScript class for configuring the link type specific part
	 * @return
	 */
	@Override
	public String getLinkTypeJSClass() {
		return "js.ext.com.track.linkType.MSProject";
	}

	/**
	 * Gets the link type specific configuration as JSON
	 * @param workItemLinkBean
	 * @param personBean
	 * @param locale
	 * @return
	 */
	@Override
	public String getSpecificJSON(TWorkItemLinkBean workItemLinkBean, TPersonBean personBean, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder("{");
		JSONUtility.appendStringValue(stringBuilder,
				JSON_FIELDS.DEPENDENCY_TYPE_LABEL, LocalizeUtil.getLocalizedTextFromApplicationResources(
					"item.tabs.itemLink.lbl.dependencyType", locale));
		JSONUtility.appendIntegerStringBeanList(stringBuilder,
				JSON_FIELDS.DEPENDENCY_TYPES_LIST, getDependencyTypeList(locale));
		JSONUtility.appendStringValue(stringBuilder,
				JSON_FIELDS.LINK_LAG_LABEL, LocalizeUtil.getLocalizedTextFromApplicationResources(
					"item.tabs.itemLink.lbl.lag", locale));
		JSONUtility.appendIntegerStringBeanList(stringBuilder,
				JSON_FIELDS.LINK_LAG_FORMATS_LIST, getLinkLagFormatList(locale));
		Integer dependencyType = null;
		Integer linkLagFormat = null;
		if (workItemLinkBean!=null) {
			dependencyType = workItemLinkBean.getIntegerValue1();
			linkLagFormat = workItemLinkBean.getLinkLagFormat();
			if (linkLagFormat == null) {
				linkLagFormat = LAG_FORMAT.d;
			}
			//if (!linkTypeChange) {
			Double hoursPerWorkday = getHoursPerWorkingDayForWorkItem(workItemLinkBean.getLinkPred());
			//Previous version of MS. project the lag value was converted.
			//In Track side it was necessary to convert this value.
			double linkLag = LinkLagBL.getUILinkLagFromMinutes(
					workItemLinkBean.getLinkLag(), linkLagFormat, hoursPerWorkday);

			JSONUtility.appendStringValue(stringBuilder,
					JSON_FIELDS.LINK_LAG, DoubleNumberFormatUtil.getInstance().formatGUI(linkLag, locale));
		}
		if (dependencyType == null) {
			// default is FS
			dependencyType = PREDECESSOR_ELEMENT_TYPE.FS;
		}
		JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.DEPENDENCY_TYPE, dependencyType);
		if (linkLagFormat==null) {
			linkLagFormat = LAG_FORMAT.d;
		}
		JSONUtility.appendIntegerValue(stringBuilder,
				JSON_FIELDS.LINK_LAG_FORMAT, linkLagFormat, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}

	/**
	 * Sets the workItemLinkBean according to the values submitted in the parameters map
	 * @param parametersMap the parameters from link configuration
	 * @param workItemLinkBean
	 * @param personBean the current user
	 * @param locale
	 * @return
	 */
	@Override
	public List<ErrorData> unwrapParametersBeforeSave(
			Map<String, String> parametersMap,
			TWorkItemLinkBean workItemLinkBean, TPersonBean personBean, Locale locale) {
		Integer dependencyType = PREDECESSOR_ELEMENT_TYPE.FS;
		try {
			if (parametersMap!=null) {
				dependencyType = Integer.valueOf(parametersMap.get(DEPENDENCY_TYPE));
			}
		} catch (Exception e) {
			LOGGER.warn("Getting the dependency type parameter failed with " + e.getMessage());
		}
		workItemLinkBean.setIntegerValue1(dependencyType);
		Double linkLag = Double.valueOf(0);
		if (parametersMap!=null) {
			String linkLagStr = parametersMap.get(LINKLAG);
			if (linkLagStr!=null) {
				try {
					linkLag = new Double(DoubleNumberFormatUtil.getInstance().parseGUI(linkLagStr, locale));
				} catch (Exception e) {
					try {
						linkLag = new Double(linkLagStr);
					} catch (Exception e1) {
					}
				}
			}
		}
		Integer lagFormat = LAG_FORMAT.h;
		if (parametersMap!=null) {
			String lagFormatStr = parametersMap.get(LAGFORMAT);
			if (lagFormatStr != null) {
				try {
					lagFormat = Integer.valueOf(lagFormatStr);
				} catch (Exception e) {
				}
			}
		}
		Double hoursPerWorkday = AccountingBL.DEFAULTHOURSPERWORKINGDAY;
		if (LinkLagBL.isHoursPerWorkdayNeeded(lagFormat)) {
			hoursPerWorkday = getHoursPerWorkingDayForWorkItem(workItemLinkBean.getLinkPred());
		}
		int linkLagMinutes = LinkLagBL.getMinutesFromUILinkLag(linkLag,
				lagFormat, hoursPerWorkday);
		workItemLinkBean.setLinkLag(new Double(linkLagMinutes));
		workItemLinkBean.setLinkLagFormat(lagFormat);
		return null;
	}

	/**
	 * Returns the the number of working hours for a workItem's project
	 * @param workItemID
	 * @return
	 */
	public static Double getHoursPerWorkingDayForWorkItem(Integer workItemID) {
		if (workItemID!=null) {
			TWorkItemBean workItemBean = null;
			WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
			try {
				workItemBean = workItemDAO.loadByPrimaryKey(workItemID);
			} catch (ItemLoaderException e) {
			}
			if (workItemBean!=null) {
				Integer projectID = workItemBean.getProjectID();
				if (projectID!=null) {
					return ProjectBL.getHoursPerWorkingDay(projectID);
				}
			}
		}
		return new Double(AccountingBL.DEFAULTHOURSPERWORKINGDAY);
	}

	private static String getLocalizedLagFormat(Integer lagFormat, Locale locale) {
		if (lagFormat==null) {
			return "";
		}
		String prefix = "admin.actions.importMSProject.lagFormat.opt.";
		return LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + lagFormat, locale);
	}

	/**
	 * Gets the possible lag formats
	 * @param locale
	 * @return
	 */
	private static List<IntegerStringBean> getLinkLagFormatList(Locale locale) {
		List<IntegerStringBean> linkLagFormatList = new ArrayList<IntegerStringBean>();
		String prefix = "admin.actions.importMSProject.lagFormat.opt.";
		/*linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.m, locale),
				LAG_FORMAT.m));
		linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.em, locale),
				LAG_FORMAT.em));
		linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix +  LAG_FORMAT.h, locale),
				LAG_FORMAT.h));
		linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.eh, locale),
				LAG_FORMAT.eh));*/
		linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.d, locale),
				LAG_FORMAT.d));
		/*linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.ed, locale),
				LAG_FORMAT.ed));*/
		linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.w, locale),
				LAG_FORMAT.w));
		/*linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.ew, locale),
				LAG_FORMAT.ew));*/
		linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.mo, locale),
				LAG_FORMAT.mo));
		/*linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.emo, locale),
				LAG_FORMAT.emo));
		linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.PERCENT, locale),
				LAG_FORMAT.PERCENT));
		linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.ePERCENT, locale),
				LAG_FORMAT.ePERCENT));
		linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.mEST, locale),
				LAG_FORMAT.mEST));
		linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.emEST, locale),
				LAG_FORMAT.emEST));
		linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.hEST, locale),
				LAG_FORMAT.hEST));
		linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.ehEST, locale),
				LAG_FORMAT.ehEST));
		linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.dEST, locale),
				LAG_FORMAT.dEST));
		linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.edEST, locale),
				LAG_FORMAT.edEST));
		linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.wEST, locale),
				LAG_FORMAT.wEST));
		linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.ewEST, locale),
				LAG_FORMAT.ewEST));
		linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.moEST, locale),
				LAG_FORMAT.moEST));
		linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.emoEST, locale),
				LAG_FORMAT.emoEST));
		linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.PERCENTEST, locale),
				LAG_FORMAT.PERCENTEST));
		linkLagFormatList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(prefix + LAG_FORMAT.ePERCENTEST, locale),
				LAG_FORMAT.ePERCENTEST));*/
		return linkLagFormatList;
	}

	/**
	 * Gets the localized dependency name
	 * @param dependencyType
	 * @param locale
	 * @return
	 */
	private static String getDependencyTypeName(Integer dependencyType, Locale locale) {
		if (dependencyType!=null) {
			switch (dependencyType.intValue()) {
			case PREDECESSOR_ELEMENT_TYPE.FS:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(
					"admin.actions.importMSProject.opt.finishToStart", locale);
			case PREDECESSOR_ELEMENT_TYPE.SS:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(
					"admin.actions.importMSProject.opt.startToStart", locale);
			case PREDECESSOR_ELEMENT_TYPE.FF:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(
					"admin.actions.importMSProject.opt.finishToFinish", locale);
			case PREDECESSOR_ELEMENT_TYPE.SF:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(
					"admin.actions.importMSProject.opt.startToFinish", locale);
			}
		}
		return LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.actions.importMSProject.opt.finishToStart", locale);
	}

	/**
	 * The dependency type list
	 * @param locale
	 * @return
	 */
	private static List<IntegerStringBean> getDependencyTypeList(Locale locale) {
		List<IntegerStringBean> taskDependencyList = new ArrayList<IntegerStringBean>();
		taskDependencyList.add(new IntegerStringBean(
				getDependencyTypeName(PREDECESSOR_ELEMENT_TYPE.FS, locale),
				PREDECESSOR_ELEMENT_TYPE.FS));
		taskDependencyList.add(new IntegerStringBean(
				getDependencyTypeName(PREDECESSOR_ELEMENT_TYPE.SS, locale),
				PREDECESSOR_ELEMENT_TYPE.SS));
		taskDependencyList.add(new IntegerStringBean(
				getDependencyTypeName(PREDECESSOR_ELEMENT_TYPE.FF, locale),
				PREDECESSOR_ELEMENT_TYPE.FF));
		taskDependencyList.add(new IntegerStringBean(
				getDependencyTypeName(PREDECESSOR_ELEMENT_TYPE.SF, locale),
				PREDECESSOR_ELEMENT_TYPE.SF));
		return taskDependencyList;
	}
}
