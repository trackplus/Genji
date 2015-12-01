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

package com.aurel.track.admin.customize.category.filter.execute.loadItems.criteria;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;

import com.aurel.track.admin.customize.category.filter.tree.design.FieldExpressionSimpleTO;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.admin.user.group.GroupMemberBL;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.custom.picker.CustomPickerRT;
import com.aurel.track.fieldType.runtime.custom.select.CustomSelectBaseRT;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.fieldType.runtime.matchers.run.CustomSelectMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.IMatcherRT;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.consInf.RaciRole;
import com.aurel.track.persist.TNotifyPeer;
import com.aurel.track.persist.TWorkItemPeer;


/**
 * Gets the criteria for tree filter
 * @author Tamas
 *
 */
public class TreeFilterCriteria {

    private static final Logger LOGGER = LogManager.getLogger(TreeFilterCriteria.class);

    private TreeFilterCriteria() {
    }

    /**
     * Prepares a criteria by filterUpperTO and raciBean
     * @param filterUpperTO
     * @param raciBean
     * @return
     * @throws EmptyResultCriteriaException
     */
    public static Criteria prepareTreeFilterCriteria(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID){
        Criteria crit = new Criteria();
        if (raciBean!=null) {
            Integer[] selectedResponsibles = filterUpperTO.getSelectedResponsibles();
            Integer[] selectedManagers = filterUpperTO.getSelectedManagers();
            Integer[] selectedAuthors = filterUpperTO.getSelectedOriginators();
            boolean reponsiblesInFilter = selectedResponsibles!=null && selectedResponsibles.length>0;
            boolean managersInFilter = selectedManagers!=null && selectedManagers.length>0;
            boolean authorsInFilter = selectedAuthors!=null && selectedAuthors.length>0;
            Criterion raciCriterion = null;
            Set<Integer> raciResponsibles = raciBean.getResponsibles();
            if (raciResponsibles!=null && !raciResponsibles.isEmpty() && !reponsiblesInFilter) {
                //add responsible raciCriterion only only if the filter has no responsible selection
                //(limitation of Torque criteria object to appear the same column more than on times in the same criteria)
                Criterion responsibleCriterion = crit.getNewCriterion(TWorkItemPeer.RESPONSIBLE, raciResponsibles.toArray(), Criteria.IN);
                raciCriterion = responsibleCriterion;
            }
            Set<Integer> raciManagers = raciBean.getManagers();
            if (raciManagers!=null && !raciManagers.isEmpty() && !managersInFilter) {
                //add manager raciCriterion only only if the filter has no manager selection
                //(limitation of Torque criteria object to appear the same column more than on times in the same criteria)
                Criterion managerCriterion = crit.getNewCriterion(TWorkItemPeer.OWNER, raciManagers.toArray(), Criteria.IN);
                if (raciCriterion==null) {
                    raciCriterion = managerCriterion;
                 } else {
                     raciCriterion = raciCriterion.or(managerCriterion);
                 }
            }
            Set<Integer> raciAuthors = raciBean.getAuthors();
            if (raciAuthors!=null && !raciAuthors.isEmpty() && !authorsInFilter) {
                //add author raciCriterion only only if the filter has no author selection
                //(limitation of Torque criteria object to appear the same column more than on times in the same criteria)
                Criterion authorCriterion = crit.getNewCriterion(TWorkItemPeer.ORIGINATOR, raciAuthors.toArray(), Criteria.IN);
                if (raciCriterion==null) {
                    raciCriterion = authorCriterion;
                } else {
                    raciCriterion = raciCriterion.or(authorCriterion);
                }
            }
            if (raciCriterion!=null) {
                crit.add(raciCriterion);
            } else {
                LOGGER.error("All managers/reponsibles/authors are selected in filter. We should never get here");
            }
        }
        /**
         * Transform the list selections into criteria expressions
         */
        boolean multipleListHasSelection = false;
        List<Integer> listsWithSelection = filterUpperTO.getListFieldsWithSelection();
        for (Integer fieldID : listsWithSelection) {
            IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
            if (fieldTypeRT!=null) {
                IMatcherRT matcherRT = fieldTypeRT.processLoadMatcherRT(fieldID, MatchRelations.IN, filterUpperTO.getSelectedValuesForField(fieldID), filterUpperTO.getMatcherContext());
                if (matcherRT!=null) {
                    if (fieldTypeRT.isCustom()) {
                        CustomSelectBaseRT customSelectBaseRT = (CustomSelectBaseRT)fieldTypeRT;
                        if (customSelectBaseRT.isCustomPicker()) {
                            CustomPickerRT customPickerRT = (CustomPickerRT)customSelectBaseRT;
                            Integer systemOptionType = customPickerRT.getSystemOptionType();
                            CustomSelectMatcherRT customSelectMatcherRT = (CustomSelectMatcherRT)matcherRT;
                            //set systemOptionType before adding the criteria
                            customSelectMatcherRT.setSystemOptionType(systemOptionType);
                        }
                        if (customSelectBaseRT.isReallyMultiple()) {
                            multipleListHasSelection = true;
                        }
                    }
                    matcherRT.addCriteria(crit);
                }
            }
        }
        /**
         * Transform the field expression simple lists into criteria expressions
         */
        boolean accessLevelInFilter = false;
        List<FieldExpressionSimpleTO> fieldExpressionSimpleList = filterUpperTO.getFieldExpressionSimpleList();
        if (fieldExpressionSimpleList!=null) {
            for (FieldExpressionSimpleTO fieldExpressionSimpleTO : fieldExpressionSimpleList) {
                Integer fieldID = fieldExpressionSimpleTO.getField();
                if (SystemFields.INTEGER_ACCESSLEVEL.equals(fieldID)) {
                    accessLevelInFilter = true;
                }
                IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
                if (fieldTypeRT.isComposite()) {
                    multipleListHasSelection = true;
                }
                Integer matcherID = fieldExpressionSimpleTO.getSelectedMatcher();
                Object value = fieldExpressionSimpleTO.getValue();
                IMatcherRT matcherRT = fieldTypeRT.processLoadMatcherRT(fieldID, matcherID, value, filterUpperTO.getMatcherContext());
                if (matcherRT!=null) {
                    matcherRT.addCriteria(crit);
                }
            }
        }

        //addProjectCriteria(crit, filterUpperTO.getSelectedProjects());//Project
        //addReleaseScheduledCriteria(crit, filterUpperTO.getSelectedReleases(), filterUpperTO.getReleaseTypeSelector()); //Release
        //addManagerCriteria(crit, filterUpperTO.getSelectedManagers());//Manager
        //addResponsibleCriteria(crit, filterUpperTO.getSelectedResponsibles(), filterUpperTO.isIncludeResponsiblesThroughGroup());//Responsible
        //addOriginatorCriteria(crit, filterUpperTO.getSelectedOriginators());//Originators
        //addChangedByCriteria(crit, filterUpperTO.getSelectedChangedBys());//Last changed by
        addConsultantInformantCriteria(crit, filterUpperTO.getSelectedConsultantsInformants(),
                filterUpperTO.getWatcherSelector());//Watchers
        //addCategoryCriteria(crit, filterUpperTO.getSelectedIssueTypes());//Issue type
        //addStateCriteria(crit, filterUpperTO.getSelectedStates());//State
        boolean includeOpen = filterUpperTO.isIncludeOpen();
        boolean includeClosed = filterUpperTO.isIncludeClosed();
        if (includeOpen ^ includeClosed) {
            if (includeOpen) {
                crit = CriteriaUtil.addStateFlagUnclosedCriteria(crit);
            } else {
                crit = CriteriaUtil.addStateFlagClosedCriteria(crit);
            }
        }
        CriteriaUtil.addArchivedCriteria(crit, filterUpperTO.getArchived(), filterUpperTO.getDeleted()); //archived/deleted
        if (!accessLevelInFilter) {
            CriteriaUtil.addAccessLevelFilter(crit, personID);
        }
        //especially for multiple selection lists filtered in criteria: the workItem could appear more times
        //if it has multiple selections which appear in the filter also, consequently
        //the same custom attributes are get more than one times
        if (multipleListHasSelection) {
            crit.setDistinct();
        }
        return crit;
    }

    /**
     * add the criteria for consultant informant
     * @param crit, the criteria to add to
     * @param ids, ids of the reponsibles
     */
    private static void addConsultantInformantCriteria(Criteria crit, Integer[] ids, Integer consultantInformantSelector) {
        if (ids!=null && ids.length!=0) {
            crit.addJoin(TWorkItemPeer.WORKITEMKEY, TNotifyPeer.WORKITEM);
            Set<Integer> watcherGroupIDs = GroupMemberBL.getGroupsIDsForPersons(ids);
            Set<Integer> watchersSet = new HashSet<Integer>();
            for (int i = 0; i < ids.length; i++) {
                watchersSet.add(ids[i]);
            }
            if (watcherGroupIDs!=null) {
                watchersSet.addAll(watcherGroupIDs);
            }
            crit.addIn(TNotifyPeer.PERSONKEY, watchersSet.toArray());
            if (consultantInformantSelector==null) {
                consultantInformantSelector = Integer.valueOf(FilterUpperTO.CONSINF_SELECTOR.CONSULTANT_OR_INFORMANT);
            }
            switch (consultantInformantSelector.intValue()) {
            case FilterUpperTO.CONSINF_SELECTOR.CONSULTANT:
                crit.add(TNotifyPeer.RACIROLE, RaciRole.CONSULTANT);
                break;
            case FilterUpperTO.CONSINF_SELECTOR.INFORMANT:
                crit.add(TNotifyPeer.RACIROLE, RaciRole.INFORMANT);
                break;
            default:
                crit.add(TNotifyPeer.RACIROLE, (Object)null, Criteria.ISNOTNULL);
                break;
            }
            //when consultant/informant isimplied setting the distinct flag is mandatory!
            crit.setDistinct();
        }
    }
}
