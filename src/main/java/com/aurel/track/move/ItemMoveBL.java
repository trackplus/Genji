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

package com.aurel.track.move;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.calendar.WorkDaysConfig;
import com.aurel.track.calendar.WorkDaysConfigImplementation;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.dao.WorkItemLinkDAO;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean.PREDECESSOR_ELEMENT_TYPE;
import com.aurel.track.exchange.msProject.importer.LinkLagBL;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.item.ItemPersisterException;
import com.aurel.track.item.budgetCost.AccountingBL;
import com.aurel.track.item.link.ItemLinkBL;
import com.aurel.track.itemNavigator.ItemNavigatorBL;
import com.aurel.track.itemNavigator.QueryContext;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.GeneralUtils;

public class ItemMoveBL {

	private static final Logger LOGGER = LogManager.getLogger(ItemMoveBL.class);

	private static TWorkItemBean movedWorkItemWithOriginalDates;
	private static TWorkItemLinkBean newOrChangedLink;
	private static Date newStartDate;
	private static Date newEndDate;
	private static Locale locale;
	private static TPersonBean personBean;
	private static ArrayList<Tuple> paths;
	private static WorkItemLinkDAO workItemLinkDao = DAOFactory.getFactory().getWorkItemLinkDAO();
	private static HashMap<Integer, TWorkItemBean> workItemIDToWorkItemBean;
	private static ArrayList<TWorkItemLinkBean> allLinks;

	private static boolean itemMoved; // true if item moved(start and end date
										// changed); false if item resized (only
										// start OR end date changed)
	private static boolean directionIsNegative; // if true the item was moved or
												// resized toward negative
												// direction
	private static boolean startDateChanged;
	private static boolean endDateChanged;
	private static int numberOfWorkedDaysFromMovedDays;
	private static List<Tuple>childWorkItemsTuplesList;

	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();


	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static Set<Integer> moveItem(TWorkItemBean movedWorkItemWithOriginalDatesParam, Date startDate, Date endDate, boolean saveChanges, boolean afterActualizingAncestorBottomUpDate, TWorkItemLinkBean theNewOrChangedLink, TPersonBean person, Locale loc) {
		Set<Integer>violatedDependencies = new HashSet<Integer>();
		movedWorkItemWithOriginalDates = movedWorkItemWithOriginalDatesParam;

		newOrChangedLink = theNewOrChangedLink;
		newStartDate = startDate;
		if(movedWorkItemWithOriginalDates.isMilestone()) {
			newEndDate = newStartDate;
		}else {
			newEndDate = endDate;
		}
		analyzeAndInit();
		LOGGER.debug("itemMoved: " + itemMoved);
		LOGGER.debug("directionIsNegative: " + directionIsNegative);
		LOGGER.debug("startDateChanged: " + startDateChanged);
		LOGGER.debug("endDateChanged: " + endDateChanged);
		LOGGER.debug("numberOfWorkedDaysFromMovedDays: " + numberOfWorkedDaysFromMovedDays);
		LOGGER.debug("Moved work item dates: S: " + newStartDate==null?"null":sdf.format(newStartDate) + " E: " + newEndDate==null?"null":sdf.format(newEndDate));
		LOGGER.debug("Item is milestone: " + movedWorkItemWithOriginalDates.isMilestone());
		personBean = person;
		locale = loc;
		workItemIDToWorkItemBean = new HashMap<Integer, TWorkItemBean>();
		allLinks = new ArrayList<TWorkItemLinkBean>();
		createTable();
		LOGGER.debug("Paths size: " + paths.size());
		writeOutPahs();
		childWorkItemsTuplesList = getMovedWorkItemsChildWorkItems(movedWorkItemWithOriginalDates.getObjectID());
		setActualMovedTupleStartEndDate();
		if (itemMoved || endDateChanged) {
			if(afterActualizingAncestorBottomUpDate) {
				for (Tuple aTupleFromAll : paths) {
					if(aTupleFromAll.getSource().getObjectID().equals(movedWorkItemWithOriginalDates.getObjectID()) && aTupleFromAll.getEdge().getEdgeType() == Edge.EDGE_TYPES.LINK) {
						LOGGER.debug("Call move for when afterActualizingAncestorBottomUpDate: " + aTupleFromAll.getSource().getObjectID() + " " + aTupleFromAll.getTarget().getObjectID());
						moveNonParentItem(aTupleFromAll);
					}
				}

			}else {
				if(childWorkItemsTuplesList.isEmpty()) {
					LOGGER.debug("***************");
					moveNonParentItem(getTupleWhereEdgeIsDurationByID(movedWorkItemWithOriginalDates.getObjectID()));
				}else {
					moveNonParentItem(getTupleWhereEdgeIsDurationByID(movedWorkItemWithOriginalDates.getObjectID()));
					for (Tuple oneChild : childWorkItemsTuplesList) {
						for (Tuple aTupleFromAll : paths) {
							if(aTupleFromAll.getSource().getObjectID().equals(oneChild.getTarget().getObjectID())) {
								LOGGER.debug("Call move for: " + aTupleFromAll.getSource().getObjectID() + " " + aTupleFromAll.getTarget().getObjectID());
								moveNonParentItem(aTupleFromAll);
							}
						}
					}
					/*for (Tuple aTupleFromAll : paths) {
						if(aTupleFromAll.getSource().getObjectID().equals(movedWorkItem.getObjectID()) && aTupleFromAll.getEdge().getEdgeType() == Edge.EDGE_TYPES.LINK) {
							LOGGER.debug("Move linked item when source is a parent: " + aTupleFromAll.getTarget().getObjectID());
							moveNonParentItem(getTupleWhereEdgeIsDurationByID(aTupleFromAll.getTarget().getObjectID()));

						}
					}*/
				}
			}
		}
		violatedDependencies = checkLinksValidity();
		if(saveChanges) {
			if(violatedDependencies.isEmpty()) {
				updateAndSaveWorkItemBeans();
			}
		}
		return violatedDependencies;
	}

	private static void analyzeAndInit() {
		itemMoved = false;
		startDateChanged = false;
		endDateChanged = false;
		numberOfWorkedDaysFromMovedDays = 0;
		directionIsNegative = false;
		int numberOfMovedDays = 0;
		Date oldStartDate = getStartDate(movedWorkItemWithOriginalDates);
		Date oldEndDate = getEndDate(movedWorkItemWithOriginalDates);
		if(movedWorkItemWithOriginalDates.isMilestone()) {
			itemMoved = true;
			numberOfMovedDays = getNumberOfDaysBetweenDates(oldStartDate, newStartDate);
			if (numberOfMovedDays < 0) {
				numberOfWorkedDaysFromMovedDays = getNumberOfWorkDaysBetweenDates(newStartDate, oldStartDate);
			} else {
				numberOfWorkedDaysFromMovedDays = getNumberOfWorkDaysBetweenDates(oldStartDate, newStartDate);
			}
		}else {
			if (newStartDate != null && newEndDate != null && oldStartDate != null && oldEndDate != null) {
				if (DateTimeUtils.compareTwoDatesWithoutTimeValue(oldStartDate, newStartDate) != 0 && DateTimeUtils.compareTwoDatesWithoutTimeValue(oldEndDate, newEndDate) != 0) {
					itemMoved = true;
					numberOfMovedDays = getNumberOfDaysBetweenDates(oldStartDate, newStartDate);
					if (numberOfMovedDays < 0) {
						numberOfWorkedDaysFromMovedDays = getNumberOfWorkDaysBetweenDates(newEndDate, oldEndDate);
					} else {
						numberOfWorkedDaysFromMovedDays = getNumberOfWorkDaysBetweenDates(oldStartDate, newStartDate);
					}
				} else {
					if (DateTimeUtils.compareTwoDatesWithoutTimeValue(oldStartDate, newStartDate) != 0) {
						numberOfMovedDays = getNumberOfDaysBetweenDates(oldStartDate, newStartDate);
						startDateChanged = true;
						if (numberOfMovedDays < 0) {
							numberOfWorkedDaysFromMovedDays = getNumberOfWorkDaysBetweenDates(newStartDate, oldStartDate);
						} else {
							numberOfWorkedDaysFromMovedDays = getNumberOfWorkDaysBetweenDates(oldStartDate, newStartDate);
						}
					}
					if (DateTimeUtils.compareTwoDatesWithoutTimeValue(oldEndDate, newEndDate) != 0) {
						endDateChanged = true;
						numberOfMovedDays = getNumberOfDaysBetweenDates(oldEndDate, newEndDate);
						if (numberOfMovedDays < 0) {
							numberOfWorkedDaysFromMovedDays = getNumberOfWorkDaysBetweenDates(newEndDate, oldEndDate);
						} else {
							numberOfWorkedDaysFromMovedDays = getNumberOfWorkDaysBetweenDates(oldEndDate, newEndDate);
						}
					}
				}
			}
		}
		numberOfWorkedDaysFromMovedDays = Math.abs(numberOfWorkedDaysFromMovedDays);
		if (numberOfMovedDays < 0) {
			directionIsNegative = true;
		}
	}

	private static void moveNonParentItem(Tuple me) {
		moveBelowElements(me);
	}

	private static void updateAndSaveWorkItemBeans() {
		for (Tuple aTuple : paths) {
			if(aTuple.getSource().isNodeDateChanged() || aTuple.getTarget().isNodeDateChanged()) {
				saveWorkitemBean(aTuple);
			}
		}
	}

	private static void createTable() {
		Integer projectID = movedWorkItemWithOriginalDates.getProjectID() * -1;
		QueryContext queryContext = new QueryContext();
		queryContext.setQueryType(ItemNavigatorBL.QUERY_TYPE.PROJECT_RELEASE);
		queryContext.setQueryID(projectID);
		paths = new ArrayList<Tuple>();
		try {
			List<ReportBean> repoBeanList = ItemNavigatorBL.executeQuery(personBean, locale, queryContext);
			createMapFromReportBeanList(repoBeanList);
			ReportBeans reportBeans = new ReportBeans(repoBeanList, locale);
			List<ReportBean> firstLevel = reportBeans.getReportBeansFirstLevel();
			parseBeans(firstLevel);
			if(newOrChangedLink != null) {
				addNewLinkOrUpdateExisting();
			}
			insertAllLinksIntoTable(allLinks);
		} catch (TooManyItemsToLoadException e) {
			e.printStackTrace();
		}
	}

	private static void addNewLinkOrUpdateExisting() {
		//remove existing link if it was changed and not yet saved into db.
		if(newOrChangedLink.getObjectID() != null) {
			for (Iterator<TWorkItemLinkBean> iter = allLinks.listIterator(); iter.hasNext(); ) {
				TWorkItemLinkBean actualLink = iter.next();
			    if(actualLink.getObjectID().equals(newOrChangedLink.getObjectID())) {
			    	iter.remove();
			    }
			}
		}
		allLinks.add(newOrChangedLink);
	}

	public static void parseBeans(List<ReportBean> firstLevel) {
		for (ReportBean reportBean : firstLevel) {
			if(reportBean.getWorkItemBean().getObjectID().equals(movedWorkItemWithOriginalDates.getObjectID())){
				reportBean.setWorkItemBean(movedWorkItemWithOriginalDates);
			}
			List<TWorkItemLinkBean> links = workItemLinkDao.loadByWorkItemSucc(reportBean.getWorkItemBean().getObjectID());
			allLinks.addAll(links);
			if (reportBean.getChildren() != null) {
				List<ReportBean> children = reportBean.getChildren();
				inserNewParentChildTuples(reportBean, children);
				insertOwnTuple(reportBean);
				parseBeans(children);
			} else {
				if (hasReportBeanStartAndEndDate(reportBean)) {
					insertOwnTuple(reportBean);
				}
			}
		}
	}

	private static void inserNewParentChildTuples(ReportBean parent, List<ReportBean> rpBeans) {
		Node source = new Node(getStartDate(parent.getWorkItemBean()), parent.getWorkItemBean().getObjectID(), Node.NODE_TYPES.START_DATE);
		for (ReportBean reportBean : rpBeans) {
			if (hasReportBeanStartAndEndDate(reportBean)) {
				Node target = new Node(getStartDate(reportBean.getWorkItemBean()), reportBean.getWorkItemBean().getObjectID(), Node.NODE_TYPES.START_DATE);
				Edge edge = new Edge(Edge.EDGE_TYPES.VIRTUAL_EDGE);
				Tuple tuple = new Tuple(source, target, edge);
				paths.add(tuple);
			}
		}
	}

	private static void insertOwnTuple(ReportBean reportBean) {
		Node source = new Node(getStartDate(reportBean.getWorkItemBean()), reportBean.getWorkItemBean().getObjectID(), Node.NODE_TYPES.START_DATE);
		Node target = new Node(getEndDate(reportBean.getWorkItemBean()), reportBean.getWorkItemBean().getObjectID(), Node.NODE_TYPES.END_DATE);
		int duration = getNumberOfDaysBetweenDates(getStartDate(reportBean.getWorkItemBean()), getEndDate(reportBean.getWorkItemBean())).intValue();
		Edge edge = new Edge(duration, null, Edge.EDGE_TYPES.DURATION);
		paths.add(new Tuple(source, target, edge));
	}

	private static void insertAllLinksIntoTable(List<TWorkItemLinkBean> links) {
		for (TWorkItemLinkBean tWorkItemLinkBean : links) {
			Integer dependencyType = tWorkItemLinkBean.getIntegerValue1();
			if (dependencyType!=null) {
				Node source = null;
				Node target = null;
				TWorkItemBean pred = workItemIDToWorkItemBean.get(tWorkItemLinkBean.getLinkPred());
				TWorkItemBean succ = workItemIDToWorkItemBean.get(tWorkItemLinkBean.getLinkSucc());
				if (hasWorkItemBeanStartAndEndDate(pred) && hasWorkItemBeanStartAndEndDate(succ)) {
					switch (dependencyType) {
					case PREDECESSOR_ELEMENT_TYPE.FF:
						source = new Node(getEndDate(pred), tWorkItemLinkBean.getLinkPred(), Node.NODE_TYPES.END_DATE);
						target = new Node(getEndDate(succ), tWorkItemLinkBean.getLinkSucc(), Node.NODE_TYPES.END_DATE);
						break;
					case PREDECESSOR_ELEMENT_TYPE.FS:
						source = new Node(getEndDate(pred), tWorkItemLinkBean.getLinkPred(), Node.NODE_TYPES.END_DATE);
						target = new Node(getStartDate(succ), tWorkItemLinkBean.getLinkSucc(), Node.NODE_TYPES.START_DATE);
						break;
					case PREDECESSOR_ELEMENT_TYPE.SF:
						source = new Node(getStartDate(pred), tWorkItemLinkBean.getLinkPred(), Node.NODE_TYPES.START_DATE);
						target = new Node(getEndDate(succ), tWorkItemLinkBean.getLinkSucc(), Node.NODE_TYPES.END_DATE);
						break;
					case PREDECESSOR_ELEMENT_TYPE.SS:
						source = new Node(getStartDate(pred), tWorkItemLinkBean.getLinkPred(), Node.NODE_TYPES.START_DATE);
						target = new Node(getStartDate(succ), tWorkItemLinkBean.getLinkSucc(), Node.NODE_TYPES.START_DATE);
						break;
					default:
						break;
					}
					if (source != null && target != null) {
						Edge edge = new Edge(tWorkItemLinkBean, Edge.EDGE_TYPES.LINK);
						Tuple tuple = new Tuple(source, target, edge);
						paths.add(tuple);
					}
				}
			}
		}
	}

	public static void createMapFromReportBeanList(List<ReportBean> repoBeanList) {
		for (ReportBean reportBean : repoBeanList) {
			workItemIDToWorkItemBean.put(reportBean.getWorkItemBean().getObjectID(), reportBean.getWorkItemBean());
		}
	}


//	private static void moveBelowElements(Tuple me) {
//		if(!me.getSource().isNodeDateChanged() && !me.getTarget().isNodeDateChanged()) {
//			if(me.getEdge().getEdgeType() == Edge.EDGE_TYPES.LINK) {
//				LOGGER.debug("Change element where source is: " + me.getSource().getObjectID() + " Target is: " + getTupleWhereEdgeIsDurationByID(me.getTarget().getObjectID()).getSource().getObjectID());
//				Tuple tupleTmp = getTupleWhereEdgeIsDurationByID(me.getTarget().getObjectID());
//				moveOneTupleSourceAndTargetNodeConsideringDirection(tupleTmp);
//				tupleTmp.getSource().setNodeDateChanged(true);
//				tupleTmp.getTarget().setNodeDateChanged(true);
//			}
//		}else {
//			LOGGER.debug("Below! Node date it was changed: " + me.getSource().getObjectID() + " " + me.getTarget().getObjectID());
//		}
//		ArrayList<Tuple> belowTuples = getBelowItems(me);
//		for (Tuple belowTuple : belowTuples) {
//			LOGGER.debug("Recursion for: " + belowTuple.getTarget().getObjectID());
////			moveBelowElements(getTupleWhereSourceAndTargetAreSame(belowTuple.getTarget().getObjectID()));
//			moveBelowElements(belowTuple);
//		}
//	}

	public static void moveBelowElements(Tuple me) {
		Tuple tupleTmp = getTupleWhereEdgeIsDurationByID(me.getTarget().getObjectID());
		if(me.getEdge().getEdgeType() == Edge.EDGE_TYPES.LINK && !tupleDatesWereChanged(tupleTmp)) {
			LOGGER.debug("Change element where source is: " + me.getSource().getObjectID() + " Target is: " + me.getTarget().getObjectID() + " Link is: " + getEdgeTypeName(me.getEdge()));
			int nrOfDaysNeedeToMove = 0;
			if(directionIsNegative) {
				nrOfDaysNeedeToMove = numberOfWorkedDaysFromMovedDays;
			}else {
				int linkLagInDays = getLinkLagInDays(me.getEdge().getLink(), getHoursPerWorkingDayForWorkItem(me.getEdge().getLink().getLinkPred()));
				nrOfDaysNeedeToMove = getNrOfDaysNeededToMove(me.getSource().getNodeDate(), me.getTarget().getNodeDate(), linkLagInDays);
			}
			moveOneTupleSourceAndTargetNodeConsideringDirection(tupleTmp, nrOfDaysNeedeToMove);
			tupleTmp.getSource().setNodeDateChanged(true);
			tupleTmp.getTarget().setNodeDateChanged(true);
		}

		if(me.getEdge().getEdgeType() == Edge.EDGE_TYPES.DURATION && !tupleDatesWereChanged(tupleTmp)) {
			LOGGER.debug("Change element where source is: " + me.getSource().getObjectID() + " Target is: " + me.getTarget().getObjectID() + " Link is: " + getEdgeTypeName(me.getEdge()));
//			Tuple tupleTmp = getTupleWhereEdgeIsDurationByID(me.getTarget().getObjectID());
			moveOneTupleSourceAndTargetNodeConsideringDirection(tupleTmp, numberOfWorkedDaysFromMovedDays);
			tupleTmp.getSource().setNodeDateChanged(true);
			tupleTmp.getTarget().setNodeDateChanged(true);
		}

		if(me.getEdge().getEdgeType() == Edge.EDGE_TYPES.VIRTUAL_EDGE) {
			LOGGER.debug("Change element where source is: " + me.getSource().getObjectID() + " Target is: " + me.getTarget().getObjectID() + " Link is: " + getEdgeTypeName(me.getEdge()));
		}


//		ArrayList<Tuple> belowTuples = getBelowItems(me);
		for (Tuple belowTuple : paths) {
			if (!belowTuple.equals(me)) {
//				if(belowTuple.getSource().getObjectID().equals(me.getTarget().getObjectID())) {
//				if(belowTuple.getSource().equals(me.getTarget())) {
				if(belowTuple.getSource().getObjectID().equals(me.getTarget().getObjectID())) {
					LOGGER.debug("Recursion for: " + belowTuple.getSource().getObjectID() + "  " + belowTuple.getTarget().getObjectID());
					moveBelowElements(belowTuple);
				}
			}
		}
	}

	private static void moveAboveElements(Tuple me) {
		if(me.getEdge().getEdgeType() == Edge.EDGE_TYPES.DURATION) {
			if(me.getEdge().getEdgeType() == Edge.EDGE_TYPES.DURATION) {
				if(!me.getSource().isNodeDateChanged() && !me.getTarget().isNodeDateChanged()) {
					LOGGER.debug("Above! Change node date for items: " +  me.getSource().getObjectID() + "  " + me.getTarget().getObjectID() + "Edget type: " + getEdgeTypeName(me.getEdge()));
					me.getSource().setNodeDateChanged(true);
					me.getTarget().setNodeDateChanged(true);
				}else {
					LOGGER.debug("Above! Node date it was changed: " + me.getSource().getObjectID() + " " + me.getTarget().getObjectID());
				}
			}
		}
		ArrayList<Tuple> aboveTuples = getAboveItems(me);
		for (Tuple aboveTuple : aboveTuples) {
			LOGGER.debug("Recursion for above: " + aboveTuple.getSource().getObjectID());
//			moveAboveElements(getTupleWhereSourceAndTargetAreSame(aboveTuple.getSource().getObjectID()));
			moveAboveElements(aboveTuple);
		}
	}


	private static ArrayList<Tuple> getBelowItems(Tuple me) {
		ArrayList<Tuple> belowItems = new ArrayList<Tuple>();
		for (Tuple aTuple : paths) {
			if (!aTuple.equals(me)) {
//				LOGGER.debug("BELOW: " + me.getTarget().getObjectID() + "  " + aTuple.getSource().getObjectID());
//				if (me.getTarget().equals(aTuple.getSource())) {
				if (me.getTarget().getObjectID().equals(aTuple.getSource().getObjectID())) {
					belowItems.add(aTuple);
				}
			}
		}
		return belowItems;
	}

	private static ArrayList<Tuple> getAboveItems(Tuple me) {
		ArrayList<Tuple> aboveItems = new ArrayList<Tuple>();
		for (Tuple aTuple : paths) {
			if (!aTuple.equals(me)) {
//				LOGGER.debug("BELOW: " + me.getTarget().getObjectID() + "  " + aTuple.getSource().getObjectID());
//				if (me.getTarget().equals(aTuple.getSource())) {
				if (me.getSource().getObjectID().equals(aTuple.getTarget().getObjectID())) {
					aboveItems.add(aTuple);
				}
			}
		}
		return aboveItems;
	}

	private static void setActualMovedTupleStartEndDate() {
		getTupleWhereEdgeIsDurationByID(movedWorkItemWithOriginalDates.getObjectID()).getSource().setNodeDate(newStartDate);
		getTupleWhereEdgeIsDurationByID(movedWorkItemWithOriginalDates.getObjectID()).getSource().setNodeDateChanged(true);
		getTupleWhereEdgeIsDurationByID(movedWorkItemWithOriginalDates.getObjectID()).getTarget().setNodeDate(newEndDate);
		getTupleWhereEdgeIsDurationByID(movedWorkItemWithOriginalDates.getObjectID()).getTarget().setNodeDateChanged(true);
		updateAllTupleDatesWhereIDIsSame(getTupleWhereEdgeIsDurationByID(movedWorkItemWithOriginalDates.getObjectID()));
	}

	public static void moveOneTupleSourceAndTargetNodeConsideringDirection(Tuple me, int nrOfDays) {
		if(nrOfDays != 0) {
			if(directionIsNegative) {
				me.getSource().setNodeDate(stepBack(me.getSource().getNodeDate(), nrOfDays, true));
				me.getTarget().setNodeDate(stepBack(me.getTarget().getNodeDate(), nrOfDays, true));
			}else{
				me.getSource().setNodeDate(stepForward(me.getSource().getNodeDate(), nrOfDays, true));
				me.getTarget().setNodeDate(stepForward(me.getTarget().getNodeDate(), nrOfDays, true));
			}
			updateAllTupleDatesWhereIDIsSame(me);
		}
	}

	/**
	 * In this case changeTuple is a duration tuple (source and target object ID is same)
	 * @param changedTuple
	 */
	public static void updateAllTupleDatesWhereIDIsSame(Tuple changedTuple) {
		Integer changedObjectID = changedTuple.getSource().getObjectID();
		for (Tuple aTuple : paths) {
			if(!changedTuple.equals(aTuple)) {
				if(aTuple.getSource().getObjectID().equals(changedObjectID)) {
					if(aTuple.getSource().getNodeType() == changedTuple.getSource().getNodeType()) {
						aTuple.getSource().setNodeDate(changedTuple.getSource().getNodeDate());
					}
					if(aTuple.getSource().getNodeType() == changedTuple.getTarget().getNodeType()) {
						aTuple.getSource().setNodeDate(changedTuple.getTarget().getNodeDate());
					}
				}
				if(aTuple.getTarget().getObjectID().equals(changedObjectID)) {
					if(aTuple.getTarget().getNodeType() == changedTuple.getSource().getNodeType()) {
						aTuple.getTarget().setNodeDate(changedTuple.getSource().getNodeDate());
					}
					if(aTuple.getTarget().getNodeType() == changedTuple.getTarget().getNodeType()) {
						aTuple.getTarget().setNodeDate(changedTuple.getTarget().getNodeDate());
					}
				}
			}
		}
	}

	public static void setNodeTypeDate() {

	}


//	public static void updateWorkItemBeanDates(Node me) {
//		TWorkItemBean workItemBeanToChange = workItemIDToWorkItemBean.get(me.getObjectID());
//		if(workItemBeanToChange != null) {
//			if(me.getNodeType() == Node.NODE_TYPES.START_DATE) {
//				setStartDate(workItemBeanToChange, me.getNodeDate());
//			}
//			if(me.getNodeType() == Node.NODE_TYPES.END_DATE) {
//				setEndDate(workItemBeanToChange, me.getNodeDate());
//			}
//		}
//	}

	public static void saveWorkitemBean(Tuple tuple) {
		TWorkItemBean workItemBeanToChange = workItemIDToWorkItemBean.get(tuple.getSource().getObjectID());

		if(workItemBeanToChange != null && !workItemBeanToChange.getObjectID().equals(-99)) {
			LOGGER.debug("Work Item: " + workItemBeanToChange.getObjectID() + " " + sdf.format(getStartDate(workItemBeanToChange)) + "-" + sdf.format(getEndDate(workItemBeanToChange))
					+ " new: " + sdf.format(tuple.getSource().getNodeDate()) + "-" + sdf.format(tuple.getTarget().getNodeDate()));
			setStartDate(workItemBeanToChange, tuple.getSource().getNodeDate());
			setEndDate(workItemBeanToChange, tuple.getTarget().getNodeDate());
			try {
				workItemDAO.saveSimple(workItemBeanToChange);
			}catch(Exception ex) {
				LOGGER.error("Saving work item failed: " + workItemBeanToChange.getObjectID());
				LOGGER.error(ExceptionUtils.getStackTrace(ex));
			}

		}
	}

	public static void setPresentedFields(Set<Integer> presentField) {
		boolean showBaseline = ApplicationBean.getInstance().getSiteBean().getShowBaseline();
		if(showBaseline) {
			presentField.add(SystemFields.TOP_DOWN_START_DATE);
			presentField.add(SystemFields.TOP_DOWN_END_DATE);
		}else {
			presentField.add(SystemFields.INTEGER_STARTDATE);
			presentField.add(SystemFields.INTEGER_ENDDATE);
		}
	}


	public static Date stepBack(Date dateParam, int steps, boolean withCheckingFreeDays) {
		if (dateParam == null) {
			return null;
		}
		int actualSteps = 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateParam);
		Date newDate = new Date();
		while (actualSteps < steps) {
			cal.add(Calendar.DAY_OF_YEAR, -1);
			newDate = cal.getTime();
			if (withCheckingFreeDays) {
				if (!WorkDaysConfigImplementation.isSaturday(newDate) && !WorkDaysConfigImplementation.isSunday(newDate)
						&& !WorkDaysConfigImplementation.isFreeDay(newDate)) {
					actualSteps++;
				}
			} else {
				actualSteps++;
			}
		}
		return newDate;
	}

	public static Date stepForward(Date dateParam, int steps, boolean withCheckingFreeDays) {
		if (dateParam == null) {
			return null;
		}
		int actualSteps = 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateParam);
		Date newDate = new Date();
		while (actualSteps < steps) {
			cal.add(Calendar.DAY_OF_YEAR, 1);
			newDate = cal.getTime();
			if (withCheckingFreeDays) {
				if (!WorkDaysConfigImplementation.isSaturday(newDate) && !WorkDaysConfigImplementation.isSunday(newDate)
						&& !WorkDaysConfigImplementation.isFreeDay(newDate)) {
					actualSteps++;
				}
			} else {
				actualSteps++;
			}
		}
		return newDate;
	}

	private static Tuple getTupleWhereEdgeIsDurationByID(Integer objectID) {
		for (Tuple aTuple : paths) {
			if (aTuple.getSource().getObjectID().equals(objectID) &&
					aTuple.getTarget().getObjectID().equals(objectID) && aTuple.getEdge().getEdgeType() == Edge.EDGE_TYPES.DURATION) {
				return aTuple;
			}
		}
		return null;
	}

	public static int getNumberOfWorkDaysBetweenDates(Date startDateParam, Date endDateParam) {
		int numberOfDays = getNumberOfDaysBetweenDates(startDateParam, endDateParam);
		if (!WorkDaysConfig.SATURDAY_WORK_DAY) {
			numberOfDays = numberOfDays - getNumberOfSaturdaysFromInterval(startDateParam, endDateParam);
		}
		if (!WorkDaysConfig.SUNDAY_WORK_DAY) {
			numberOfDays = numberOfDays - getNumberOfSundaysFromInterval(startDateParam, endDateParam);
		}
		numberOfDays = numberOfDays - getNumberOfFreeDaysFromIntervall(startDateParam, endDateParam);
		return numberOfDays;
	}

	public static Integer getNumberOfSundaysFromInterval(Date startDateParam, Date endDateParam) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDateParam);
		int numOfSunday = 0;
		while (DateTimeUtils.less(calendar.getTime(), endDateParam)) {
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				numOfSunday++;
			}
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		return numOfSunday;
	}

	public static Integer getNumberOfFreeDaysFromIntervall(Date startDateParam, Date endDateParam) {
		int numberOfFreeDays = 0;
		for (Map.Entry<Date, String> entry : WorkDaysConfig.exceptionFromWorkDays.entrySet()) {
			if (DateTimeUtils.lessOrEqual(entry.getKey(), endDateParam) && DateTimeUtils.greaterOrEqual(entry.getKey(), startDateParam)) {
				numberOfFreeDays++;
			}
		}
		return numberOfFreeDays;
	}

	public static Integer getNumberOfSaturdaysFromInterval(Date startDateParam, Date endDateParam) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDateParam);
		int numOfSaturdays = 0;
		while (DateTimeUtils.lessOrEqual(calendar.getTime(), endDateParam)) {
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				numOfSaturdays++;
			}
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		return numOfSaturdays;
	}

	public static Integer getNumberOfDaysBetweenDates(Date startDateParam, Date endDateParam) {
		DateTime dateTime1 = new DateTime(startDateParam);
		DateTime dateTime2 = new DateTime(endDateParam);
		int numberOfDays = Days.daysBetween(DateTimeUtils.getZeroTimeDateTime(dateTime1), DateTimeUtils.getZeroTimeDateTime(dateTime2)).getDays();
		return numberOfDays;
	}

	public static Set<Integer> checkLinksValidity() {
		Set<Integer>violatedLinkIDs = new HashSet<Integer>();
		for (Tuple oneTuple : paths) {
			if(oneTuple.getEdge().getEdgeType() == Edge.EDGE_TYPES.LINK) {
				Date sourceDate = null;
				Date targetDate = null;
				TWorkItemLinkBean link = oneTuple.getEdge().getLink();
				switch (link.getIntegerValue1()) {

				case PREDECESSOR_ELEMENT_TYPE.FS:
					sourceDate = getTupleWhereEdgeIsDurationByID(oneTuple.getSource().getObjectID()).getTarget().getNodeDate();
					targetDate =  getTupleWhereEdgeIsDurationByID(oneTuple.getTarget().getObjectID()).getSource().getNodeDate();
					break;
				case PREDECESSOR_ELEMENT_TYPE.FF:
					sourceDate = getTupleWhereEdgeIsDurationByID(oneTuple.getSource().getObjectID()).getTarget().getNodeDate();
					targetDate =  getTupleWhereEdgeIsDurationByID(oneTuple.getTarget().getObjectID()).getTarget().getNodeDate();
					break;
				case PREDECESSOR_ELEMENT_TYPE.SF:
					sourceDate = getTupleWhereEdgeIsDurationByID(oneTuple.getSource().getObjectID()).getSource().getNodeDate();
					targetDate =  getTupleWhereEdgeIsDurationByID(oneTuple.getTarget().getObjectID()).getTarget().getNodeDate();
					break;
				case PREDECESSOR_ELEMENT_TYPE.SS:
					sourceDate = getTupleWhereEdgeIsDurationByID(oneTuple.getSource().getObjectID()).getSource().getNodeDate();
					targetDate =  getTupleWhereEdgeIsDurationByID(oneTuple.getTarget().getObjectID()).getSource().getNodeDate();
					break;
				}
				if(isLinkViolatesConsideringLinkLag(sourceDate, targetDate, link)) {
					violatedLinkIDs.add(oneTuple.getEdge().getLink().getObjectID());
				}
			}
		}
		return violatedLinkIDs;
	}

	public static int getNrOfDaysNeededToMove(Date sourceDate, Date targetDate, int linkLagInDays) {
//		Date sourceDate = tuple.getSource().getNodeDate();
//		Date targetDate = tuple.getTarget().getNodeDate();
//		TWorkItemLinkBean link = tuple.getEdge().getLink();
		Date targetDateTmp = stepBack(targetDate, 1, true);

		int nrOfDaysNeededToMove = 0;
		if(DateTimeUtils.less(sourceDate, targetDateTmp)) {
			if(getNumberOfWorkDaysBetweenDates(sourceDate, targetDateTmp) < linkLagInDays) {
				nrOfDaysNeededToMove = linkLagInDays - getNumberOfWorkDaysBetweenDates(sourceDate, targetDateTmp);
			}
		}else if(DateTimeUtils.less(targetDateTmp, sourceDate)) {
			if(getNumberOfWorkDaysBetweenDates(targetDateTmp, sourceDate) * (-1) < linkLagInDays) {
				nrOfDaysNeededToMove = linkLagInDays - getNumberOfWorkDaysBetweenDates(targetDateTmp, sourceDate) * (-1);
			}
		}else {
			if(linkLagInDays > 0) {
				nrOfDaysNeededToMove = linkLagInDays;
			}
		}
		return nrOfDaysNeededToMove;
	}

	public static boolean isLinkViolatesConsideringLinkLag(Date sourceDate, Date targetDate, TWorkItemLinkBean link) {
		//getNumberOfWorkDaysBetweenDates returns for. ex: start: 2015-03-24 end: 2015-03-25 returned value: 1 Because between this dates there are one day.
		//when validating links: for ex: links is FS: the task1 end date is: 2015-03-24 and task2 start date is 2015-03-25 this means between tasks there is not any days.
		//   |____|
		//         |____| but the getNumberOfWorkDaysBetweenDates return 1.

		Date targetDateTmp = stepBack(targetDate, 1, true);
		int linkLagInDays = getLinkLagInDays(link, getHoursPerWorkingDayForWorkItem(link.getLinkPred()));
		boolean violates = false;
		if(DateTimeUtils.less(sourceDate, targetDateTmp)) {
			if(getNumberOfWorkDaysBetweenDates(sourceDate, targetDateTmp) < linkLagInDays) {
				violates = true;
			}
		}else if(DateTimeUtils.less(targetDateTmp, sourceDate)) {
			if(getNumberOfWorkDaysBetweenDates(targetDateTmp, sourceDate) * (-1) < linkLagInDays) {
				violates = true;
			}
		}else {
			if(linkLagInDays > 0) {
				violates = true;
			}
		}
		return violates;
	}

	/**
	 * In Gantt we are handling link lags: day, week, month,
	 * This method returns link lag in days.
	 * Foe ex: -1) 1 mo link lag is 20 work day.
	 * 		   -2) 1 w link lag is 5 work days
	 * @param actualLinkBean
	 * @param workItemID
	 * @return
	 */
	public static int getLinkLagInDays(TWorkItemLinkBean actualLinkBean, Double hoursPerWorkday) {
		Double convertedLinkLag = LinkLagBL.getUILinkLagFromMinutes(actualLinkBean.getLinkLag(), actualLinkBean.getLinkLagFormat(), hoursPerWorkday);
		int linkLagInDays = 0;
		Integer actualLinkLagFormat = actualLinkBean.getLinkLagFormat();
		if (actualLinkLagFormat==null) {
			actualLinkLagFormat = MsProjectExchangeDataStoreBean.LAG_FORMAT.d;
		}
		switch(actualLinkLagFormat) {
			case  MsProjectExchangeDataStoreBean.LAG_FORMAT.d:
			case  MsProjectExchangeDataStoreBean.LAG_FORMAT.ed:
				linkLagInDays = convertedLinkLag.intValue();
				break;
			case  MsProjectExchangeDataStoreBean.LAG_FORMAT.mo:
			case  MsProjectExchangeDataStoreBean.LAG_FORMAT.emo:
				linkLagInDays = convertedLinkLag.intValue() * 20;
				break;
			case  MsProjectExchangeDataStoreBean.LAG_FORMAT.w:
			case  MsProjectExchangeDataStoreBean.LAG_FORMAT.ew:
				linkLagInDays = convertedLinkLag.intValue() * 5;
				break;
		}
		return linkLagInDays;
	}

	/**
	 * Returns the the number of working hours for a workItem's project
	 * @param workItemID
	 * @return
	 */
	public static Double getHoursPerWorkingDayForWorkItem(Integer workItemID) {
		TWorkItemBean workItemBean = null;
		if(workItemIDToWorkItemBean != null) {
			if(workItemIDToWorkItemBean.get(workItemID) != null) {
				workItemBean = workItemIDToWorkItemBean.get(workItemID);
			}
		}
		if(workItemBean == null) {
			if (workItemID != null) {
				WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
				try {
					workItemBean = workItemDAO.loadByPrimaryKey(workItemID);
				} catch (ItemLoaderException e) {
				}
			}
		}

		if (workItemBean != null) {
			Integer projectID = workItemBean.getProjectID();
			if (projectID!=null) {
				return ProjectBL.getHoursPerWorkingDay(projectID);
			}
		}
		return new Double(AccountingBL.DEFAULTHOURSPERWORKINGDAY);
	}

	public static Tuple getTupleFromLinkedBeans(TWorkItemBean pred, TWorkItemBean succ, TWorkItemLinkBean tWorkItemLinkBean) {
		Integer dependencyType = tWorkItemLinkBean.getIntegerValue1();
		Node source = null;
		Node target = null;
		if (hasWorkItemBeanStartAndEndDate(pred) && hasWorkItemBeanStartAndEndDate(succ)) {
			switch (dependencyType) {
			case PREDECESSOR_ELEMENT_TYPE.FF:
				source = new Node(getEndDate(pred), tWorkItemLinkBean.getLinkPred(), Node.NODE_TYPES.END_DATE);
				target = new Node(getEndDate(succ), tWorkItemLinkBean.getLinkSucc(), Node.NODE_TYPES.END_DATE);
				break;
			case PREDECESSOR_ELEMENT_TYPE.FS:
				source = new Node(getEndDate(pred), tWorkItemLinkBean.getLinkPred(), Node.NODE_TYPES.END_DATE);
				target = new Node(getStartDate(succ), tWorkItemLinkBean.getLinkSucc(), Node.NODE_TYPES.START_DATE);
				break;
			case PREDECESSOR_ELEMENT_TYPE.SF:
				source = new Node(getStartDate(pred), tWorkItemLinkBean.getLinkPred(), Node.NODE_TYPES.START_DATE);
				target = new Node(getEndDate(succ), tWorkItemLinkBean.getLinkSucc(), Node.NODE_TYPES.END_DATE);
				break;
			case PREDECESSOR_ELEMENT_TYPE.SS:
				source = new Node(getStartDate(pred), tWorkItemLinkBean.getLinkPred(), Node.NODE_TYPES.START_DATE);
				target = new Node(getStartDate(succ), tWorkItemLinkBean.getLinkSucc(), Node.NODE_TYPES.START_DATE);
				break;
			default:
				break;
			}
			if (source != null && target != null) {
				Edge edge = new Edge(tWorkItemLinkBean, Edge.EDGE_TYPES.LINK);
				Tuple tuple = new Tuple(source, target, edge);
				return tuple;
			}
		}
		return null;

	}

	public static boolean hasReportBeanStartAndEndDate(ReportBean rp) {
		if (getStartDate(rp.getWorkItemBean()) != null && getEndDate(rp.getWorkItemBean()) != null) {
			return true;
		}
		return false;
	}

	public static boolean hasWorkItemBeanStartAndEndDate(TWorkItemBean workItemBean) {
		if (getStartDate(workItemBean) != null && getEndDate(workItemBean) != null) {
			return true;
		}
		return false;
	}

	private static List<Tuple>getMovedWorkItemsChildWorkItems(Integer movedWorkItemID) {
		List<Tuple>childWorkItemsTuplesList = new ArrayList<Tuple>();
		for (Tuple aTuple : paths) {
			if(aTuple.getSource().getObjectID().equals(movedWorkItemID) && aTuple.getEdge().getEdgeType() == Edge.EDGE_TYPES.VIRTUAL_EDGE) {
				childWorkItemsTuplesList.add(aTuple);
			}
		}
		return childWorkItemsTuplesList;
	}

	public static boolean tupleDatesWereChanged(Tuple t) {
		if(t.getSource().isNodeDateChanged() && t.getTarget().isNodeDateChanged()) {
			return true;
		}
		return false;

	}

	public static Date getStartDate(TWorkItemBean bean) {
		boolean showBaseline = ApplicationBean.getInstance().getSiteBean().getShowBaseline();
		if (bean!=null) {
			if (showBaseline) {
				return bean.getTopDownStartDate();
			}else {
				return bean.getStartDate();
			}
		} else {
			return null;
		}
	}

	public static Date getEndDate(TWorkItemBean bean) {
		boolean showBaseline = ApplicationBean.getInstance().getSiteBean().getShowBaseline();
		if (bean!=null) {
			if(bean.isMilestone()) {
				return getStartDate(bean);
			}else {
				if(showBaseline) {
					return bean.getTopDownEndDate();
				}else {
					return bean.getEndDate();
				}
			}
		} else {
			return null;
		}
	}


	public static void setStartDate(TWorkItemBean bean, Date newStartDate) {
		if (bean!=null) {
			boolean showBaseline = ApplicationBean.getInstance().getSiteBean().getShowBaseline();
			if(showBaseline) {
				bean.setTopDownStartDate(newStartDate);
			}else {
				bean.setStartDate(newStartDate);
			}
		}
	}

	public static void setEndDate(TWorkItemBean bean, Date newEndDate) {
		if (bean!=null) {
			if(!bean.isMilestone()) {
				boolean showBaseline = ApplicationBean.getInstance().getSiteBean().getShowBaseline();
				if(showBaseline) {
					bean.setTopDownEndDate(newEndDate);
				}else {
					bean.setEndDate(newEndDate);
				}
			}
		}
	}

	public static void removeViolatedParentDependencies(Set<Integer> conflictingLinkIDs) {
		if (conflictingLinkIDs!=null) {
			for (Integer conflictingLinkID : conflictingLinkIDs) {
				ItemLinkBL.deleteLink(conflictingLinkID);
			}
		}
	}

	public static String getNodeTypeName(Node n) {
		if (n.getNodeType() == Node.NODE_TYPES.END_DATE) {
			return "F";
		}
		if (n.getNodeType() == Node.NODE_TYPES.START_DATE) {
			return "S";
		}
		return null;
	}

	public static String getEdgeTypeName(Edge e) {
		String retValue = "";
		switch (e.getEdgeType()) {
		case Edge.EDGE_TYPES.DURATION:
			retValue = "D";
			break;
		case Edge.EDGE_TYPES.LINK:
			retValue = "LINK";
			break;
		case Edge.EDGE_TYPES.VIRTUAL_EDGE:
			retValue = "VE";
			break;
		default:
			break;
		}
		return retValue;
	}

	public static void writeOutPahs() {
		for (Tuple tuple : paths) {
			LOGGER.debug(getNodeTypeName(tuple.getSource()) + tuple.getSource().getObjectID() + " " + getNodeTypeName(tuple.getTarget())
					+ tuple.getTarget().getObjectID() + " " + getEdgeTypeName(tuple.getEdge()));
		}
	}


	/************************************ BRYNTUM CASCADE ************************************/
	public static void saveChangedItems(HashSet<Integer>projectIDs, HashMap<Integer, GanttTaskBean>workItemIDToTaskStore, TPersonBean pers, Locale loc) {
		FilterUpperTO filterUpperTO = new FilterUpperTO();
		filterUpperTO.setSelectedProjects(GeneralUtils.createIntegerArrFromCollection(projectIDs));
		List<TWorkItemBean> workItemBeans = null;
		try {
			workItemBeans = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, pers, loc, false);
			if(!workItemBeans.isEmpty()) {
				HashMap<Integer, TWorkItemBean>workItemIDToWorkItemBean = createWorkItemIDToWorkitemBeanMap(workItemBeans);
				for (Map.Entry<Integer, GanttTaskBean> entry : workItemIDToTaskStore.entrySet()) {
					TWorkItemBean workItemBeanToChange = workItemIDToWorkItemBean.get(entry.getKey());
					if(workItemBeanToChange != null) {
						if(entry.getValue().getStartDate() != null && entry.getValue().getEndDate() != null) {
							setStartDate(workItemBeanToChange, entry.getValue().getStartDate());
							setEndDate(workItemBeanToChange, entry.getValue().getEndDate());
						}
						try {
							LOGGER.debug("Saving work item after cascade: " + workItemBeanToChange.getObjectID());
							workItemDAO.saveSimple(workItemBeanToChange);
						} catch (ItemPersisterException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}catch(TooManyItemsToLoadException ex) {
			ex.printStackTrace();
		}
	}

	public static HashMap<Integer, TWorkItemBean> createWorkItemIDToWorkitemBeanMap(List<TWorkItemBean>workItemBeans) {
		HashMap<Integer, TWorkItemBean> workItemIDToWOrkItemBean = new HashMap<Integer, TWorkItemBean>();
		for (TWorkItemBean tWorkItemBean : workItemBeans) {
			workItemIDToWOrkItemBean.put(tWorkItemBean.getObjectID(), tWorkItemBean);
		}
		return workItemIDToWOrkItemBean;
	}
}
