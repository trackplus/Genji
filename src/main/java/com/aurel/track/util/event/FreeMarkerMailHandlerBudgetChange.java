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



package com.aurel.track.util.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.account.AccountBL;
import com.aurel.track.admin.customize.category.filter.execute.ExecuteMatcherBL;
import com.aurel.track.admin.customize.mailTemplate.MailTemplateBL;
import com.aurel.track.admin.customize.notify.settings.NotifySettingsBL;
import com.aurel.track.admin.customize.notify.trigger.NotifyTriggerBL;
import com.aurel.track.admin.customize.role.FieldsRestrictionsToRoleBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.user.group.GroupMemberBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TAccountBean;
import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.beans.TMailTemplateBean;
import com.aurel.track.beans.TNotifyFieldBean;
import com.aurel.track.beans.TNotifySettingsBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TRoleFieldBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.base.FieldChange;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.item.budgetCost.AccountingBL;
import com.aurel.track.item.consInf.ConsInfBL;
import com.aurel.track.item.consInf.RaciRole;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.event.parameters.AfterBudgetExpenseChangeEventParam;
import com.aurel.track.util.event.parameters.AfterItemSaveEventParam;


/**
 * Mail handler listener for budget/plan/expense change
 * It could be called from more places: adding expense/modifying budget/plan
 * through web interface or through webservice
 * @author Tamas Ruff
 *
 */
public class FreeMarkerMailHandlerBudgetChange implements IEventSubscriber {

	private static final Logger LOGGER = LogManager.getLogger(FreeMarkerMailHandlerBudgetChange.class);

	/**
	 * Additionally to the cost and effort fields (and more expense types in the future) we may show also the subject
	 * and account for an expense in the mail. But they are not real fields from the viewpoint of the notification triggers
	 * @author Tamas Ruff
	 *
	 */
	private static interface ADDITIONAL_MAIL_FIELDS {
		public static Integer SUBJECT = Integer.valueOf(-1);
		public static Integer ACCOUNT = Integer.valueOf(-2);
		public static Integer EFFORTDATE = Integer.valueOf(-3);
	}
	/**
	 * Get the events this class is interested in
	 * Always one event is present at the same time
	 * (IEventSubscriber.getInterestedEvents())
	 */
	public List<Integer> getInterestedEvents() {
		List<Integer> events = new ArrayList<Integer>();
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_UPDATEBUDGET));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_UPDATEPLANNEDVALUE));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_UPDATEREMAININGPLAN));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_ADDEXPENSE));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_UPDATEEXPENSE));
		events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_DELETEEXPENSE));
		return events;
	}

	/**
	 * The operations to be executed on event
	 * IEventSubscriber.update()
	 */
	public boolean update(List<Integer> events, Object eventContextObject) {
		AfterBudgetExpenseChangeEventParam afterBudgetExpenseChangeEventParam =
				(AfterBudgetExpenseChangeEventParam)eventContextObject;
		MailHandlerBudgetChange mailHandler = new MailHandlerBudgetChange(events, afterBudgetExpenseChangeEventParam);
		//start the handler thread
		mailHandler.start();
		return true;
	}

	/**
	 * Mail handler for budget change
	 * @author Tamas Ruff
	 *
	 */
	private static class MailHandlerBudgetChange extends MailHandler {
		private TWorkItemBean workItemBean = null;
		private TBudgetBean newBudget;
		private TBudgetBean oldBudget;
		private TBudgetBean newPlannedValue;
		private TBudgetBean oldPlannedValue;
		private TActualEstimatedBudgetBean newRemainingBudget;
		private TActualEstimatedBudgetBean oldRemainingBudget;
		private TCostBean newExpense;
		private TCostBean oldExpense;
		//the event that was triggered
		private int event;


		public MailHandlerBudgetChange(List<Integer> events, AfterBudgetExpenseChangeEventParam afterBudgetChangeEventParam) {
			super(afterBudgetChangeEventParam.getWorkItemBean(), afterBudgetChangeEventParam.getWorkItemOld(), afterBudgetChangeEventParam.getLocale());
			this.afterItemSaveEventParam = afterBudgetChangeEventParam;
			workItemBean = afterBudgetChangeEventParam.getWorkItemBean();
			newBudget = afterBudgetChangeEventParam.getNewBudget();
			oldBudget = afterBudgetChangeEventParam.getOldBudget();
			newPlannedValue = afterBudgetChangeEventParam.getNewPlannedValue();
			oldPlannedValue = afterBudgetChangeEventParam.getOldPlannedValue();
			newRemainingBudget = afterBudgetChangeEventParam.getNewRemainingBudget();
			oldRemainingBudget = afterBudgetChangeEventParam.getOldRemainingBudget();
			newExpense = afterBudgetChangeEventParam.getNewExpense();
			oldExpense = afterBudgetChangeEventParam.getOldExpense();
			changedByPerson = afterBudgetChangeEventParam.getPersonBean();
			//some error detecting code
			if (events==null || events.isEmpty()) {
				LOGGER.error("No budget/expense event");
				return;
			} else {
				if (events.size()>1) {
					LOGGER.error("More than one budget/expense event triggered");
					return;
				}
			}
			event = ((Integer)events.get(0)).intValue();
			switch (event) {
			case IEventSubscriber.EVENT_POST_ISSUE_UPDATEBUDGET:
				if (newBudget==null) {
					LOGGER.error("No new budget present by EVENT_POST_ISSUE_UPDATEBUDGET event");
				}
				break;
			case IEventSubscriber.EVENT_POST_ISSUE_UPDATEPLANNEDVALUE:
				if (newPlannedValue==null) {
					LOGGER.error("No new planned value present by EVENT_POST_ISSUE_UPDATEPLANNEDVALUE event");
				}
				break;
			case IEventSubscriber.EVENT_POST_ISSUE_UPDATEREMAININGPLAN:
				if (newRemainingBudget==null) {
					LOGGER.error("No new remaining budget present by EVENT_POST_ISSUE_UPDATEREMAININGBUDGET event");
				}
				break;
			case IEventSubscriber.EVENT_POST_ISSUE_ADDEXPENSE:
				if (newExpense==null) {
					LOGGER.error("No new expense present by EVENT_POST_ISSUE_ADDEXPENSE event");
				}
				break;
			case IEventSubscriber.EVENT_POST_ISSUE_UPDATEEXPENSE:
				if (newExpense==null) {
					LOGGER.error("No new expense present by EVENT_POST_ISSUE_UPDATEEXPENSE event");
				}
				if (oldExpense==null) {
					LOGGER.error("No old expense present by EVENT_POST_ISSUE_UPDATEEXPENSE event");
				}
				break;
			case IEventSubscriber.EVENT_POST_ISSUE_DELETEEXPENSE:
				if (oldExpense==null) {
					LOGGER.error("No old expense present by EVENT_POST_ISSUE_DELETEEXPENSE event");
				}
				break;
			default:

			}
		}

		/**
		 * Get the resource key of the detailed explanation
		 * @return
		 */
		protected String getChangeDetailKey() {
			String changeDetailKey="";
			switch (event) {
			case IEventSubscriber.EVENT_POST_ISSUE_UPDATEBUDGET:
				changeDetailKey = "item.mail.subject.budget";
				break;
			case IEventSubscriber.EVENT_POST_ISSUE_UPDATEPLANNEDVALUE:
				changeDetailKey = "item.mail.subject.plannedValue";
				break;
			case IEventSubscriber.EVENT_POST_ISSUE_UPDATEREMAININGPLAN:
				changeDetailKey = "item.mail.subject.remainingPlan";
				break;
			case IEventSubscriber.EVENT_POST_ISSUE_ADDEXPENSE:
				changeDetailKey = "item.mail.subject.expenseAdd";
				break;
			case IEventSubscriber.EVENT_POST_ISSUE_UPDATEEXPENSE:
				changeDetailKey = "item.mail.subject.expenseUpdate";
				break;
			case IEventSubscriber.EVENT_POST_ISSUE_DELETEEXPENSE:
				changeDetailKey = "item.mail.subject.expenseDelete";
				break;
			}
			return changeDetailKey;
		}


		/**
		 * Get the parameters of the parameterized detailed explanation
		 * @param itemID
		 * @param workItemBean
		 * @param fieldChangesMap
		 * @param locale
		 * @return
		 */
		protected Object[] getChangeDetailParameters(String itemID, TWorkItemBean workItemBean, SortedMap<Integer, FieldChange> fieldChangesMap,  Locale locale) {
			return new Object[] {
					workItemBean.getSynopsis()};
		}

		/**
		 * Prepares the velocity context
		 * @param context
		 * @param personBean
		 * @param personLocale
		 * @param isPlain
		 * @return
		 */
		@Override
		protected Map<String, Object> getRootContext(TPersonBean personBean,
				Locale actualLocale, boolean isPlain, Set<Integer> pickerRaciRolesSet, SortedMap<Integer, FieldChange> localizedFieldChangesMap) {
			Integer personID = personBean.getObjectID();
			List<Integer> fieldIDs = new LinkedList<Integer>();
			fieldIDs.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.PLAN);
			fieldIDs.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.BUDGET);
			fieldIDs.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.MY_EXPENSES);
			fieldIDs.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES);
			Map<Integer, Integer> restrictedFieldsMap = AccessBeans.getFieldRestrictions(
					personID, workItemBean.getProjectID(), workItemBean.getListTypeID(), fieldIDs, false);
			//remove all the fieldIDs for which the person has no read right
			if (restrictedFieldsMap!=null && !restrictedFieldsMap.isEmpty()) {
				for (Integer fieldID : restrictedFieldsMap.keySet()) {
					Integer accessFlag = restrictedFieldsMap.get(fieldID);
					if (accessFlag.intValue()==TRoleFieldBean.ACCESSFLAG.NOACCESS) {
						fieldIDs.remove(fieldID);
					}
				}
			}
			SortedMap<Integer, FieldChange> localizedFieldChangesCloneMap = null;
			if (fieldIDs.size()<4) {
				localizedFieldChangesCloneMap = new TreeMap<Integer, FieldChange>();
				for (Map.Entry<Integer, FieldChange> entry : localizedFieldChangesMap.entrySet()) {
					Integer fieldID = entry.getKey();
					if (fieldIDs.contains(fieldID)) {
						localizedFieldChangesCloneMap.put(fieldID, entry.getValue());
					}
				}
			} else {
				localizedFieldChangesCloneMap = localizedFieldChangesMap;
			}
			if (fieldIDs.isEmpty()) {
				LOGGER.debug("Notification not needed for person " + personID + " and budget/plan/expense: no right to see the field ");
				return null;
			}
			//get the description change
			FieldChange descriptionChange = getDescriptionChange(actualLocale);
			//whether do the person really needs notification
			/**
			 * get the fields for which needs notification and see whether a notification is really needed
			 */
			Set<Integer> fieldsRegisteredForNotify = getFieldsForNotify(personID, workItemBean, actualLocale, pickerRaciRolesSet, fieldIDs);
			if (fieldsRegisteredForNotify!=null && fieldsRegisteredForNotify.isEmpty()) {
				LOGGER.debug("Notification not needed for person " + personID + " and budget/plan/expense: no fields registered for notify");
				return null;
			}
			boolean notifyNeeded = false;
			for (Integer fieldID : localizedFieldChangesCloneMap.keySet()) {
				FieldChange fieldChange = localizedFieldChangesCloneMap.get(fieldID);
				if (fieldsRegisteredForNotify==null || //no notification settings
						(fieldsRegisteredForNotify.contains(fieldChange.getFieldID()) && //the field needs notification
								(fieldChange.isChanged() || //if modified
										event==IEventSubscriber.EVENT_POST_ISSUE_ADDEXPENSE || //if was added
										event==IEventSubscriber.EVENT_POST_ISSUE_DELETEEXPENSE))) { //if was deleted
					notifyNeeded = true;
					break;
				}
			}
			if (!notifyNeeded) {
				//no notification is needed for this changes
				LOGGER.debug("No notification needed for person " + personID + ": nothing was changed" );
				return null;
			}
			Collection<FieldChange> shortFieldChangesList = new ArrayList<FieldChange>();
			Collection<FieldChange> longFieldChangesList = new ArrayList<FieldChange>();
			if (event==IEventSubscriber.EVENT_POST_ISSUE_DELETEEXPENSE) {
				//if expense was deleted set the newShowValues to the oldShowValues because the newShowValues are shown in the template
				setOldValuesAsNewValues(localizedFieldChangesCloneMap);
			}
			if (isPlain) {
				/**
				 * prepare lists for plain mail
				 */
				setLocalizedChanges(localizedFieldChangesCloneMap, actualLocale);
				if (descriptionChange!=null && descriptionChange.isChanged()) {
					setLocalizedChangeDetail(descriptionChange, new ArrayList<Integer>(), actualLocale, true);
					descriptionChange.setNewShowValue(convertHTML2Text(descriptionChange.getNewShowValue()));
					longFieldChangesList.add(descriptionChange);
				}
				LOGGER.debug("Preparing the plain mail data model for " + personID);
			} else {
				/**
				 * prepare lists for HTML mail
				 */
				if (descriptionChange!=null && descriptionChange.isChanged()) {
					setLocalizedChangeDetail(descriptionChange, new ArrayList<Integer>(), actualLocale, true);
					longFieldChangesList.add(descriptionChange);
				}
				LOGGER.debug("Preparing the HTML mail data model for " + personID);
			}
			shortFieldChangesList = localizedFieldChangesCloneMap.values();
			Map<String, Object> root = prepareRootContext(localizedFieldChangesCloneMap, actualLocale);
			root.put("event", event);
			root.put("shortFieldChanges", shortFieldChangesList);
			root.put("longFieldChanges", longFieldChangesList);
			return root;
		}

		/**
		 * Sets the localized change detail texts if changed for short fields
		 * @param allFieldChangesMap Map with FieldChanges for both short and long values. It is also an "out" parameter
		 * @param locale
		 * @return
		 */
		private static void setLocalizedChanges(SortedMap<Integer, FieldChange> allFieldChangesMap, Locale locale) {
			for (Integer fieldID : allFieldChangesMap.keySet()) {
				FieldChange fieldChange = allFieldChangesMap.get(fieldID);
				if (fieldChange.isChanged()) {
					//if it has been changed then the localized change detail text will be added
					setLocalizedChangeDetail(fieldChange, new ArrayList<Integer>(), locale, false);
				}
			}
		}

		/**
		 * Sets the newShowValues to the oldShowValues because the Freemarker template shows the new values
		 * It is the case by deleting an expense
		 * @param allFieldChangesMap Map with FieldChanges for both short and long values. It is also an "out" parameter
		 * @return
		 */
		private static void setOldValuesAsNewValues(SortedMap<Integer, FieldChange> allFieldChangesMap) {
			for (Integer fieldID : allFieldChangesMap.keySet()) {
				FieldChange fieldChange = allFieldChangesMap.get(fieldID);
				fieldChange.setNewShowValue(fieldChange.getOldShowValue());
			}
		}

		@Override
		protected TMailTemplateBean getTemplate() {
			Integer eventID=IEventSubscriber.EVENT_POST_ISSUE_UPDATEPLANNEDVALUE;
			//TProjectBean project=getProjectBean();
			Integer projectID = workItemBean.getProjectID();
			Integer issuTypeID=workItemBean.getListTypeID();
			Integer templateID= MailTemplateBL.findMailTemplateID(eventID, projectID, issuTypeID);
			TMailTemplateBean mailTemplateBean=MailTemplateBL.getMailTemplate(templateID);
			return mailTemplateBean;
		}

		/**
		 * The full name of the person who made the action
		 * @return
		 */
		@Override
		protected TPersonBean getChangedByPerson() {
			return changedByPerson;
		}


		/**
		 * The project bean the modified issue belongs to
		 * @return
		 */
		@Override
		protected TProjectBean getProjectBean() {
			return LookupContainer.getProjectBean(workItemBean.getProjectID());
		}


		/**
		 * Whether the email sending is really needed
		 * (it is tested previously)
		 * @return
		 */
		@Override
		protected boolean sendCondition() {
			return true;
		}

		/**
		 * Gets the list of FieldChanges objects
		 * Take care that all FieldChange has not null newShowValue and oldShowValue because it results in Freemarker error
		 * @param workItemContext
		 * @param actualLocale
		 * @param remainedFields
		 * @return
		 */
		protected SortedMap<Integer, FieldChange> getLocalizedFieldChangesMap(AfterItemSaveEventParam afterItemSaveEventParam, Locale actualLocale) {
			AfterBudgetExpenseChangeEventParam afterBudgetExpenseChangeEventParam = (AfterBudgetExpenseChangeEventParam)afterItemSaveEventParam;
			Integer projectID = afterBudgetExpenseChangeEventParam.getWorkItemBean().getProjectID();
			SortedMap<Integer, FieldChange> changedAttributes = new TreeMap<Integer, FieldChange>();
			Double newEffortValue=null;
			Double oldEffortValue=null;
			Double newCostValue=null;
			Double oldCostValue=null;
			Integer newEffortUnit=null;
			Integer oldEffortUnit=null;
			boolean isNew = true;
			boolean isDeleted = false;
			boolean isChanged = false;
			switch (event) {
			case IEventSubscriber.EVENT_POST_ISSUE_UPDATEBUDGET:
				if (newBudget!=null) {
					newEffortValue = newBudget.getEstimatedHours();
					newEffortUnit = newBudget.getTimeUnit();
					newCostValue = newBudget.getEstimatedCost();
				}
				if (oldBudget!=null) {
					oldEffortValue = oldBudget.getEstimatedHours();
					oldEffortUnit = oldBudget.getTimeUnit();
					oldCostValue = oldBudget.getEstimatedCost();
				}
				isNew = false;
				break;
			case IEventSubscriber.EVENT_POST_ISSUE_UPDATEPLANNEDVALUE:
				if (newPlannedValue!=null) {
					newEffortValue = newPlannedValue.getEstimatedHours();
					newEffortUnit = newPlannedValue.getTimeUnit();
					newCostValue = newPlannedValue.getEstimatedCost();
				}
				if (oldPlannedValue!=null) {
					oldEffortValue = oldPlannedValue.getEstimatedHours();
					oldEffortUnit = oldPlannedValue.getTimeUnit();
					oldCostValue = oldPlannedValue.getEstimatedCost();
				}
				isNew = false;
				break;
			case IEventSubscriber.EVENT_POST_ISSUE_UPDATEREMAININGPLAN:
				if (newRemainingBudget!=null) {
					newEffortValue = newRemainingBudget.getEstimatedHours();
					newEffortUnit = newRemainingBudget.getTimeUnit();
					newCostValue = newRemainingBudget.getEstimatedCost();
				}
				if (oldRemainingBudget!=null) {
					oldEffortValue = oldRemainingBudget.getEstimatedHours();
					oldEffortUnit = oldRemainingBudget.getTimeUnit();
					oldCostValue = oldRemainingBudget.getEstimatedCost();

				}
				isNew = false;
				//always false for remaining budget. We consider the starting values 0.0 (not null)
				break;
			case IEventSubscriber.EVENT_POST_ISSUE_ADDEXPENSE:
			case IEventSubscriber.EVENT_POST_ISSUE_UPDATEEXPENSE:
			case IEventSubscriber.EVENT_POST_ISSUE_DELETEEXPENSE:
				if (newExpense!=null) {
					newEffortValue = newExpense.getHours();
					newCostValue = newExpense.getCost();
				} else {
					isDeleted = true;
				}
				if (oldExpense!=null) {
					oldEffortValue = oldExpense.getHours();
					oldCostValue = oldExpense.getCost();
					isNew = false;
				}
				break;
			default:
			}
			if (newEffortUnit==null) {
				newEffortUnit = AccountingBL.TIMEUNITS.HOURS;
			}
			if (oldEffortUnit==null) {
				oldEffortUnit = AccountingBL.TIMEUNITS.HOURS;
			}
			//effort changes
			//do not add effort change to changedAttributes map if new/delete and no value specified because otherwise
			//we get into trouble by applying the trigger for the field: see prepareMailContext(), notifyNeeded part
			//fieldChange.isChanged() is not meaningful by add and delete budget/expense so better do not include it as fieldChange
			if (!(event==IEventSubscriber.EVENT_POST_ISSUE_ADDEXPENSE && (newEffortValue==null || isZero(newEffortValue.doubleValue()))) &&
					!(event==IEventSubscriber.EVENT_POST_ISSUE_DELETEEXPENSE && (oldEffortValue==null || isZero(oldEffortValue.doubleValue())))) {
				FieldChange fieldChange = new FieldChange();
				fieldChange.setFieldID(AccountingBL.FIELDS.EFFORT);
				String fieldLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.effort", actualLocale);
				fieldChange.setLocalizedFieldLabel(fieldLabel);
				isChanged = EqualUtils.valueModified(newEffortValue, oldEffortValue) ||
						EqualUtils.valueModified(newEffortUnit, oldEffortUnit);

				String newUnitLabel = LocalizeUtil.getLocalizedTextFromApplicationResources(LocalizationKeyPrefixes.TIME_UNIT_KEY_PREFIX + newEffortUnit, actualLocale);
				String oldUnitLabel = LocalizeUtil.getLocalizedTextFromApplicationResources(LocalizationKeyPrefixes.TIME_UNIT_KEY_PREFIX + oldEffortUnit, actualLocale);
				String guiNewEffortValue;
				if (newEffortValue==null) {
					guiNewEffortValue = "";
				} else {
					guiNewEffortValue = AccountingBL.getFormattedDouble(newEffortValue, actualLocale, true) + " " + newUnitLabel;
				}
				fieldChange.setNewShowValue(guiNewEffortValue);
				String guiOldEffortValue;
				if (oldEffortValue==null || !isChanged) {
					guiOldEffortValue = "";
				} else {
					guiOldEffortValue = AccountingBL.getFormattedDouble(oldEffortValue, actualLocale, true) + " " + oldUnitLabel;
				}
				fieldChange.setOldShowValue(guiOldEffortValue);
				//if we are here a change happened anyway so we add the field change to the changedAttributes list
				//but we use the changed flag to see whether the bean is new or modified
				if (!isNew && !isDeleted) {
					fieldChange.setChanged(isChanged);
				}
				changedAttributes.put(AccountingBL.FIELDS.EFFORT, fieldChange);
			}
			//cost changes
			//do not add cost change to changedAttributes map if new/delete and no value specified because otherwise
			//we get into trouble by applying the trigger for the field: see prepareMailContext(), notifyNeeded part
			//fieldChange.isChanged() is not meaningful by add and delete budget/expense so better do not include it as fieldChange
			if (!(event==IEventSubscriber.EVENT_POST_ISSUE_ADDEXPENSE && (newCostValue==null || isZero(newCostValue.doubleValue()))) &&
					!(event==IEventSubscriber.EVENT_POST_ISSUE_DELETEEXPENSE && (oldCostValue==null || isZero(oldCostValue.doubleValue())))) {
				FieldChange fieldChange = new FieldChange();
				fieldChange.setFieldID(AccountingBL.FIELDS.COST);
				//get the localized label
				String fieldLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.cost", actualLocale);
				fieldChange.setLocalizedFieldLabel(fieldLabel);
				String currency = ProjectBL.getCurrency(projectID);
				isChanged = EqualUtils.valueModified(newCostValue, oldCostValue);

				String guiNewCostValue;
				if (newCostValue==null) {
					guiNewCostValue = "";
				} else {
					guiNewCostValue = AccountingBL.getFormattedDouble(newCostValue, actualLocale, false) + " " + currency;
				}
				fieldChange.setNewShowValue(guiNewCostValue);
				String guiOldCostValue;
				if (oldCostValue==null || !isChanged) {
					guiOldCostValue = "";
				} else {
					guiOldCostValue = AccountingBL.getFormattedDouble(oldCostValue, actualLocale, false) + " " + currency;
				}
				fieldChange.setOldShowValue(guiOldCostValue);
				if (!isNew && !isDeleted) {
					fieldChange.setChanged(isChanged);
				}
				changedAttributes.put(AccountingBL.FIELDS.COST, fieldChange);
			}
			//extra expense fields
			if (event==IEventSubscriber.EVENT_POST_ISSUE_ADDEXPENSE ||
					event==IEventSubscriber.EVENT_POST_ISSUE_UPDATEEXPENSE ||
					event==IEventSubscriber.EVENT_POST_ISSUE_DELETEEXPENSE) {
				//subject for expense
				String newSubject = null;
				String oldSubject = null;
				if (newExpense!=null) {
					newSubject = newExpense.getSubject();
				}
				if (oldExpense!=null) {
					oldSubject = oldExpense.getSubject();
				}

				if (newSubject==null) {
					newSubject = "";
				}
				if (oldSubject==null) {
					oldSubject = "";
				}
				isChanged = EqualUtils.valueModified(newSubject, oldSubject);

				FieldChange fieldChange = new FieldChange();
				fieldChange.setFieldID(ADDITIONAL_MAIL_FIELDS.SUBJECT);
				//get the localized label
				String fieldLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("item.tabs.expense.editExpense.lbl.subject", actualLocale);
				fieldChange.setLocalizedFieldLabel(fieldLabel);
				fieldChange.setNewShowValue(newSubject);
				if (isChanged) {
					fieldChange.setOldShowValue(oldSubject);
				} else {
					fieldChange.setOldShowValue("");
				}
				if (!isNew && !isDeleted) {
					fieldChange.setChanged(isChanged);
				}
				changedAttributes.put(ADDITIONAL_MAIL_FIELDS.SUBJECT, fieldChange);
				//account for expense
				Integer newAccount = null;
				Integer oldAccount = null;
				if (newExpense!=null) {
					newAccount = newExpense.getAccount();
				}
				if (oldExpense!=null) {
					oldAccount = oldExpense.getAccount();
				}
				isChanged = EqualUtils.valueModified(newAccount, oldAccount);
				fieldChange = new FieldChange();
				fieldChange.setFieldID(ADDITIONAL_MAIL_FIELDS.ACCOUNT);
				//get the localized label
				fieldLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("item.tabs.expense.editExpense.lbl.account", actualLocale);
				fieldChange.setLocalizedFieldLabel(fieldLabel);
				TAccountBean newAccountBean=null;
				TAccountBean oldAccountBean=null;
				if (newAccount!=null) {
					newAccountBean = AccountBL.loadByPrimaryKey(newAccount);
				}
				if (oldAccount!=null) {
					oldAccountBean = AccountBL.loadByPrimaryKey(oldAccount);
				}
				if (newAccountBean==null) {
					fieldChange.setNewShowValue("");
				} else {
					fieldChange.setNewShowValue(newAccountBean.getFullName());
				}
				if (oldAccountBean==null || !isChanged) {
					fieldChange.setOldShowValue("");
				} else {
					fieldChange.setOldShowValue(oldAccountBean.getFullName());
				}
				if (!isNew && !isDeleted) {
					fieldChange.setChanged(isChanged);
				}
				changedAttributes.put(ADDITIONAL_MAIL_FIELDS.ACCOUNT, fieldChange);
				//effort date for expense
				Date newEffortDate = null;
				Date oldEffortDate = null;
				if (newExpense!=null) {
					newEffortDate = newExpense.getEffortdate();
				}
				if (oldExpense!=null) {
					oldEffortDate = oldExpense.getEffortdate();
				}
				isChanged = EqualUtils.notEqual(newEffortDate, oldEffortDate);
				//DateTimeUtils dateTimeUtils = new DateTimeUtils(actualLocale);
				fieldChange = new FieldChange();
				fieldChange.setFieldID(ADDITIONAL_MAIL_FIELDS.EFFORTDATE);
				//get the localized label
				fieldLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("item.tabs.expense.editExpense.lbl.effortDate", actualLocale);
				fieldChange.setLocalizedFieldLabel(fieldLabel);
				String guiNewEffortDate = DateTimeUtils.getInstance().formatGUIDate(newEffortDate, actualLocale);
				if (guiNewEffortDate==null) {
					guiNewEffortDate = "";
				}
				fieldChange.setNewShowValue(guiNewEffortDate);

				String guiOldEffortDate = DateTimeUtils.getInstance().formatGUIDate(oldEffortDate, actualLocale);
				if (guiOldEffortDate==null || !isChanged) {
					guiOldEffortDate = "";
				}
				fieldChange.setOldShowValue(guiOldEffortDate);
				if (!isNew && !isDeleted) {
					fieldChange.setChanged(isChanged);
				}
				changedAttributes.put(ADDITIONAL_MAIL_FIELDS.EFFORTDATE, fieldChange);
			}
			return changedAttributes;
		}

		/**
		 * Gets the list of FieldChanges objects
		 * @param workItemContext
		 * @param actualLocale
		 * @param remainedFields
		 * @return
		 */
		private FieldChange getDescriptionChange(Locale actualLocale) {
			String descriptionLabelKey = "";
			FieldChange fieldChange=null;
			String newDescription = null;
			String oldDescription=null;
			switch (event) {
			case IEventSubscriber.EVENT_POST_ISSUE_UPDATEBUDGET:
				newDescription = newBudget.getChangeDescription();
				descriptionLabelKey = "common.lbl.description";
				if (oldBudget!=null) {
					oldDescription = oldBudget.getChangeDescription();
				}
				break;
			case IEventSubscriber.EVENT_POST_ISSUE_UPDATEPLANNEDVALUE:
				newDescription = newPlannedValue.getChangeDescription();
				descriptionLabelKey = "common.lbl.description";
				if (oldPlannedValue!=null) {
					oldDescription = oldPlannedValue.getChangeDescription();
				}
				break;
			case IEventSubscriber.EVENT_POST_ISSUE_ADDEXPENSE:
			case IEventSubscriber.EVENT_POST_ISSUE_UPDATEEXPENSE:
			case IEventSubscriber.EVENT_POST_ISSUE_DELETEEXPENSE:
				descriptionLabelKey = "common.lbl.description";
				if (newExpense!=null) {
					newDescription = newExpense.getDescription();
				} else {
					//no description shown in e-mail after deleting the expense
					return null;
				}
				if (oldExpense!=null) {
					oldDescription = oldExpense.getDescription();
				}
				break;
			}
			if (newDescription==null) {
				newDescription = "";
			}
			if (oldDescription==null) {
				oldDescription = "";
			}
			if (EqualUtils.valueModified(newDescription, oldDescription)) {
				fieldChange = new FieldChange();
				//get the localized label
				String descriptionLabel = LocalizeUtil.getLocalizedTextFromApplicationResources(descriptionLabelKey, actualLocale);
				fieldChange.setLocalizedFieldLabel(descriptionLabel);
				fieldChange.setNewShowValue(newDescription);
				//the old value will not be shown but is needed by setting the localizedChangeDetail string
				fieldChange.setOldShowValue(oldDescription);
				fieldChange.setChanged(true);
			}
			return fieldChange;
		}

		/**
		 * Get the set of fields for which the person in a RACI role (or more roles) needs notification
		 * IMPORTANT: There is a strict difference between returning null or an empty set:
		 * null means any change triggers email, an empty set means no change will trigger
		 * Both the filter and the trigger will be applied: first the filter, then the trigger
		 * @param personID
		 * @param workItemBeanOld
		 * @param workItemBean
		 * @param isCreated is that a new issue or a modified one
		 * @return
		 */
		private Set<Integer> getFieldsForNotify(Integer personID, TWorkItemBean workItemBean,
				Locale locale, Set<Integer> pickerRaciRolesSet, List<Integer> notRestrictedFields) {
			Set<Integer> fields = new HashSet<Integer>();
			boolean notifySubmitter = false;
			if (personID==null) {
				//unknown submitter (POP3/IMAP)
				//the unknown submitter person will be notified at submitterEmail according to the guest user
				TPersonBean personBean = PersonBL.loadByLoginName(TPersonBean.GUEST_USER);
				if (personBean==null) {
					//if guest user does not exists then notify according to originator of the item:
					//this could be also the guest user if it was e-mail submission, otherwise the helpdesk technician entered the e-mail address manually
					personBean = LookupContainer.getPersonBean(workItemBean.getOriginatorID());
					LOGGER.debug("The e-mail to submitter address is computed based on item originator's notification settings");
				} else {
					LOGGER.debug("The e-mail to submitter address is computed based on guest user's notification settings");
				}
				if (personBean==null) {
					//the guest user was deleted (probably after this issue was created) -> empty set (do not trigger email)
					LOGGER.debug("Do not trigger confirmation mail for the e-mail submitter for an existing issue");
					return fields;
				} else {
					//notify as the unknown submitter as it would be the guest
					personID = personBean.getObjectID();
					notifySubmitter = true;
				}
			}
			/**********************************************************************************************************************************/
			/******* if the expense was added/updated/modified by other person and the actual person neither has "view all expenses" right ****/
			/********************** nor is the "owner" of the expense then no mail will be sent independently of filter/trigger settings*******/
			/**********************************************************************************************************************************/
			if ((event==IEventSubscriber.EVENT_POST_ISSUE_ADDEXPENSE ||
					event==IEventSubscriber.EVENT_POST_ISSUE_UPDATEEXPENSE ||
					event==IEventSubscriber.EVENT_POST_ISSUE_DELETEEXPENSE) &&
					changedByPerson!=null && !changedByPerson.getObjectID().equals(personID)) {
				//the expense was modified by other person but the owner of the expense is/was this person
				if ((newExpense!=null && !personID.equals(newExpense.getPerson())) ||
						oldExpense!=null && !personID.equals(oldExpense.getPerson()))
					if (!notRestrictedFields.contains(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES)) {
						return fields;
					}
			}
			Integer projectID = workItemBean.getProjectID();
			//own notifySettingsBean for the specific project
			TNotifySettingsBean mostSpecificNotifySettingsBean = getNearestNotifySettings(personID, projectID);
			if (mostSpecificNotifySettingsBean!=null) {
				//not necessarily projectID but it can be an ancestor of that
				LOGGER.debug("Found own specific settings for the project " + projectIDs);
			} else {
				//own notifySettingsBean for the generic project
				mostSpecificNotifySettingsBean = NotifySettingsBL.loadOwnByPersonAndProject(personID, NotifySettingsBL.OTHERPROJECTSID);
				if (mostSpecificNotifySettingsBean!=null) {
					LOGGER.debug("Found own generic settings");
				} else {
					//default notifySettingsBean for the specific project
					mostSpecificNotifySettingsBean = getNearestNotifySettings(null, projectID);
					if (mostSpecificNotifySettingsBean!=null) {
						//not necessarily projectID but it can be an ancestor of that
						LOGGER.debug("Found default specific settings for the project " + projectID);
					} else {
						//default notifySettingsBean for the generic project
						mostSpecificNotifySettingsBean = NotifySettingsBL.loadDefaultByProject(NotifySettingsBL.OTHERPROJECTSID);
						if (mostSpecificNotifySettingsBean!=null) {
							LOGGER.debug("Found default generic settings");
						}
					}
				}
			}
			if (mostSpecificNotifySettingsBean==null) {
				//no trigger/filter defined for the own or default specific or generic project
				//return null which means any change will trigger a mail
				return null;
			}
			/*********************************************************************************************************************************/
			/**************apply the filter and return an empty set if doesn't match, otherwise go further to applying the trigger************/
			/*********************************************************************************************************************************/
			//load the matcher context with actual values
			MatcherContext matcherContext = new MatcherContext();
			matcherContext.setLoggedInUser(personID);
			TPersonBean personBean = LookupContainer.getPersonBean(personID);
			matcherContext.setLastLoggedDate(personBean.getLastButOneLogin());
			matcherContext.setLocale(locale);
			boolean filterMatch = false;
			Integer notifyFilter = mostSpecificNotifySettingsBean.getNotifyFilter();
			//apply specific filter
			//try the filter associated with the new project
			if (notifyFilter==null) {
				//no filter specified for this notify settings means match any
				filterMatch = true;
			} else {
				//apply the filter specified for this notify settings
				filterMatch = ExecuteMatcherBL.matchNotifyFilter(notifyFilter, workItemBean, workItemBean, matcherContext);
			}

			if (!filterMatch) {
				//no match, return an empty set
				return fields;
			}
			/*********************************************************************************************************************************/
			/*********************************************************apply the trigger*******************************************************/
			/*********************************************************************************************************************************/
			int fieldType = NotifyTriggerBL.FIELDTYPE.PLANNED_VALUE_FIELD;
			switch (event) {
			case IEventSubscriber.EVENT_POST_ISSUE_UPDATEBUDGET:
				fieldType =	NotifyTriggerBL.FIELDTYPE.BUDGET_FIELD;
				break;
			case IEventSubscriber.EVENT_POST_ISSUE_UPDATEPLANNEDVALUE:
				fieldType =	NotifyTriggerBL.FIELDTYPE.PLANNED_VALUE_FIELD;
				break;
			case IEventSubscriber.EVENT_POST_ISSUE_UPDATEREMAININGPLAN:
				fieldType =	NotifyTriggerBL.FIELDTYPE.REMAINING_PLAN_FIELD;
				break;
			case IEventSubscriber.EVENT_POST_ISSUE_ADDEXPENSE:
			case IEventSubscriber.EVENT_POST_ISSUE_UPDATEEXPENSE:
			case IEventSubscriber.EVENT_POST_ISSUE_DELETEEXPENSE:
				fieldType =	NotifyTriggerBL.FIELDTYPE.EXPENSE_FIELD;
				break;
			}
			//specific project
			if (mostSpecificNotifySettingsBean.getNotifyTrigger()==null) {
				//no trigger specified for this notify settings means trigger any
				return null;
			}
			List<Integer> projectIDList = new LinkedList<Integer>();
			//not necessarily projectID but it can be an ancestor of that
			projectIDList.add(mostSpecificNotifySettingsBean.getProject());
			//trigger surely specified
			List<TNotifyFieldBean> notifyFieldBeans = NotifyTriggerBL.getTriggerFieldsForRaciRole(
					mostSpecificNotifySettingsBean.getPerson(), projectIDList,
					Integer.valueOf(NotifyTriggerBL.ACTIONTYPE.EDIT_ISSUE),
					Integer.valueOf(fieldType));
			Map<Integer, List<Integer>> notifyFieldsMap = NotifyTriggerBL.organizeByRaciRole(notifyFieldBeans);
			if (personID.equals(workItemBean.getOriginatorID()) || notifySubmitter ||
					pickerRaciRolesSet.contains(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.ORIGINATOR))) {
				List<Integer> originatorFields = notifyFieldsMap.get(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.ORIGINATOR));
				fields.addAll(originatorFields);
				if (notifySubmitter) {
					//notify the unknown submitter according to originator settings for guest user
					return fields;
				}
			}
			if (personID.equals(workItemBean.getOwnerID()) ||
					pickerRaciRolesSet.contains(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.MANAGER))) {
				List<Integer> managerFields = notifyFieldsMap.get(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.MANAGER));
				fields.addAll(managerFields);
			}
			if (personID.equals(workItemBean.getResponsibleID()) ||
					GroupMemberBL.isPersonMemberInGroup(personID, workItemBean.getResponsibleID()) ||
					pickerRaciRolesSet.contains(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.RESPONSIBLE))) {
				List<Integer> responsibleFields = notifyFieldsMap.get(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.RESPONSIBLE));
				fields.addAll(responsibleFields);
			}
			List<Integer> personIDs = new LinkedList<Integer>();
			personIDs.add(personID);
			if (ConsInfBL.hasRaciRole(workItemBean.getObjectID(), personIDs, RaciRole.CONSULTANT) ||
					pickerRaciRolesSet.contains(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.CONSULTANT))) {
				List<Integer> consultantFields = notifyFieldsMap.get(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.CONSULTANT));
				fields.addAll(consultantFields);
			}
			if (ConsInfBL.hasRaciRole(workItemBean.getObjectID(), personIDs, RaciRole.INFORMANT) ||
					pickerRaciRolesSet.contains(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.INFORMANT))) {
				List<Integer> informantFields = notifyFieldsMap.get(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.INFORMANT));
				fields.addAll(informantFields);
			}

			List<Integer> observantFields = notifyFieldsMap.get(Integer.valueOf(NotifyTriggerBL.RACI_ROLES.OBSERVER));
			fields.addAll(observantFields);

			return fields;
		}
	}

	private static boolean isZero(double val) {
		if(Double.doubleToRawLongBits(val) == 0) {
			return true;
		}
		return false;
	}

}
