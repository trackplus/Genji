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

package com.aurel.track.persist.map;

import org.apache.torque.TorqueException;

/**
 * This is a Torque Generated class that is used to load all database map 
 * information at once.  This is useful because Torque's default behaviour
 * is to do a "lazy" load of mapping information, e.g. loading it only
 * when it is needed.<p>
 *
 * @see org.apache.torque.map.DatabaseMap#initialize() DatabaseMap.initialize() 
 */
public class TrackMapInit
{
    public static final void init()
        throws TorqueException
    {
        com.aurel.track.persist.TAccessControlListPeer.getMapBuilder();
        com.aurel.track.persist.TBaseLinePeer.getMapBuilder();
        com.aurel.track.persist.TListTypePeer.getMapBuilder();
        com.aurel.track.persist.TClassPeer.getMapBuilder();
        com.aurel.track.persist.TDepartmentPeer.getMapBuilder();
        com.aurel.track.persist.TNotifyPeer.getMapBuilder();
        com.aurel.track.persist.TPersonPeer.getMapBuilder();
        com.aurel.track.persist.TPriorityPeer.getMapBuilder();
        com.aurel.track.persist.TPpriorityPeer.getMapBuilder();
        com.aurel.track.persist.TProjectCategoryPeer.getMapBuilder();
        com.aurel.track.persist.TProjectPeer.getMapBuilder();
        com.aurel.track.persist.TReleasePeer.getMapBuilder();
        com.aurel.track.persist.TRolePeer.getMapBuilder();
        com.aurel.track.persist.TSeverityPeer.getMapBuilder();
        com.aurel.track.persist.TPseverityPeer.getMapBuilder();
        com.aurel.track.persist.TStatePeer.getMapBuilder();
        com.aurel.track.persist.TStateChangePeer.getMapBuilder();
        com.aurel.track.persist.TTrailPeer.getMapBuilder();
        com.aurel.track.persist.TWorkItemPeer.getMapBuilder();
        com.aurel.track.persist.TComputedValuesPeer.getMapBuilder();
        com.aurel.track.persist.TPrivateReportRepositoryPeer.getMapBuilder();
        com.aurel.track.persist.TPublicReportRepositoryPeer.getMapBuilder();
        com.aurel.track.persist.TProjectReportRepositoryPeer.getMapBuilder();
        com.aurel.track.persist.TAccountPeer.getMapBuilder();
        com.aurel.track.persist.TAttachmentPeer.getMapBuilder();
        com.aurel.track.persist.TCostPeer.getMapBuilder();
        com.aurel.track.persist.TEffortTypePeer.getMapBuilder();
        com.aurel.track.persist.TEffortUnitPeer.getMapBuilder();
        com.aurel.track.persist.TDocStatePeer.getMapBuilder();
        com.aurel.track.persist.TDisableFieldPeer.getMapBuilder();
        com.aurel.track.persist.TPlistTypePeer.getMapBuilder();
        com.aurel.track.persist.TProjectTypePeer.getMapBuilder();
        com.aurel.track.persist.TPstatePeer.getMapBuilder();
        com.aurel.track.persist.TSitePeer.getMapBuilder();
        com.aurel.track.persist.TWorkFlowPeer.getMapBuilder();
        com.aurel.track.persist.TWorkFlowRolePeer.getMapBuilder();
        com.aurel.track.persist.TWorkFlowCategoryPeer.getMapBuilder();
        com.aurel.track.persist.TRoleListTypePeer.getMapBuilder();
        com.aurel.track.persist.TIssueAttributeValuePeer.getMapBuilder();
        com.aurel.track.persist.TAttributeOptionPeer.getMapBuilder();
        com.aurel.track.persist.TAttributeClassPeer.getMapBuilder();
        com.aurel.track.persist.TAttributeTypePeer.getMapBuilder();
        com.aurel.track.persist.TAttributePeer.getMapBuilder();
        com.aurel.track.persist.TReportLayoutPeer.getMapBuilder();
        com.aurel.track.persist.TSchedulerPeer.getMapBuilder();
        com.aurel.track.persist.TProjectAccountPeer.getMapBuilder();
        com.aurel.track.persist.TGroupMemberPeer.getMapBuilder();
        com.aurel.track.persist.TBudgetPeer.getMapBuilder();
        com.aurel.track.persist.TActualEstimatedBudgetPeer.getMapBuilder();
        com.aurel.track.persist.TSystemStatePeer.getMapBuilder();
        com.aurel.track.persist.TCostCenterPeer.getMapBuilder();
        com.aurel.track.persist.TMotdPeer.getMapBuilder();
        com.aurel.track.persist.TDashboardScreenPeer.getMapBuilder();
        com.aurel.track.persist.TDashboardTabPeer.getMapBuilder();
        com.aurel.track.persist.TDashboardPanelPeer.getMapBuilder();
        com.aurel.track.persist.TDashboardFieldPeer.getMapBuilder();
        com.aurel.track.persist.TDashboardParameterPeer.getMapBuilder();
        com.aurel.track.persist.TVersionControlParameterPeer.getMapBuilder();
        com.aurel.track.persist.TFieldPeer.getMapBuilder();
        com.aurel.track.persist.TFieldConfigPeer.getMapBuilder();
        com.aurel.track.persist.TRoleFieldPeer.getMapBuilder();
        com.aurel.track.persist.TConfigOptionsRolePeer.getMapBuilder();
        com.aurel.track.persist.TTextBoxSettingsPeer.getMapBuilder();
        com.aurel.track.persist.TGeneralSettingsPeer.getMapBuilder();
        com.aurel.track.persist.TListPeer.getMapBuilder();
        com.aurel.track.persist.TOptionPeer.getMapBuilder();
        com.aurel.track.persist.TOptionSettingsPeer.getMapBuilder();
        com.aurel.track.persist.TAttributeValuePeer.getMapBuilder();
        com.aurel.track.persist.TScreenPeer.getMapBuilder();
        com.aurel.track.persist.TScreenTabPeer.getMapBuilder();
        com.aurel.track.persist.TScreenPanelPeer.getMapBuilder();
        com.aurel.track.persist.TScreenFieldPeer.getMapBuilder();
        com.aurel.track.persist.TActionPeer.getMapBuilder();
        com.aurel.track.persist.TScreenConfigPeer.getMapBuilder();
        com.aurel.track.persist.TInitStatePeer.getMapBuilder();
        com.aurel.track.persist.TEventPeer.getMapBuilder();
        com.aurel.track.persist.TCLOBPeer.getMapBuilder();
        com.aurel.track.persist.TNotifyFieldPeer.getMapBuilder();
        com.aurel.track.persist.TNotifyTriggerPeer.getMapBuilder();
        com.aurel.track.persist.TNotifySettingsPeer.getMapBuilder();
        com.aurel.track.persist.TQueryRepositoryPeer.getMapBuilder();
        com.aurel.track.persist.TLocalizedResourcesPeer.getMapBuilder();
        com.aurel.track.persist.TLinkTypePeer.getMapBuilder();
        com.aurel.track.persist.TWorkItemLinkPeer.getMapBuilder();
        com.aurel.track.persist.TLoggingLevelPeer.getMapBuilder();
        com.aurel.track.persist.TWorkItemLockPeer.getMapBuilder();
        com.aurel.track.persist.TExportTemplatePeer.getMapBuilder();
        com.aurel.track.persist.TEmailProcessedPeer.getMapBuilder();
        com.aurel.track.persist.TApplicationContextPeer.getMapBuilder();
        com.aurel.track.persist.TLoggedInUsersPeer.getMapBuilder();
        com.aurel.track.persist.TClusterNodePeer.getMapBuilder();
        com.aurel.track.persist.TEntityChangesPeer.getMapBuilder();
        com.aurel.track.persist.TSummaryMailPeer.getMapBuilder();
        com.aurel.track.persist.TOutlineCodePeer.getMapBuilder();
        com.aurel.track.persist.TOutlineTemplateDefPeer.getMapBuilder();
        com.aurel.track.persist.TOutlineTemplatePeer.getMapBuilder();
        com.aurel.track.persist.THistoryTransactionPeer.getMapBuilder();
        com.aurel.track.persist.TFieldChangePeer.getMapBuilder();
        com.aurel.track.persist.TScriptsPeer.getMapBuilder();
        com.aurel.track.persist.TRevisionPeer.getMapBuilder();
        com.aurel.track.persist.TRevisionWorkitemsPeer.getMapBuilder();
        com.aurel.track.persist.TRepositoryPeer.getMapBuilder();
        com.aurel.track.persist.TTemplatePersonPeer.getMapBuilder();
        com.aurel.track.persist.TBLOBPeer.getMapBuilder();
        com.aurel.track.persist.TRecurrencePatternPeer.getMapBuilder();
        com.aurel.track.persist.TReportPersonSettingsPeer.getMapBuilder();
        com.aurel.track.persist.TReportParameterPeer.getMapBuilder();
        com.aurel.track.persist.TMSProjectTaskPeer.getMapBuilder();
        com.aurel.track.persist.TMSProjectExchangePeer.getMapBuilder();
        com.aurel.track.persist.TFilterCategoryPeer.getMapBuilder();
        com.aurel.track.persist.TReportCategoryPeer.getMapBuilder();
        com.aurel.track.persist.TMenuitemQueryPeer.getMapBuilder();
        com.aurel.track.persist.TChildIssueTypePeer.getMapBuilder();
        com.aurel.track.persist.TPersonBasketPeer.getMapBuilder();
        com.aurel.track.persist.TBasketPeer.getMapBuilder();
        com.aurel.track.persist.TMailTemplateConfigPeer.getMapBuilder();
        com.aurel.track.persist.TMailTemplatePeer.getMapBuilder();
        com.aurel.track.persist.TMailTemplateDefPeer.getMapBuilder();
        com.aurel.track.persist.TLastVisitedItemPeer.getMapBuilder();
        com.aurel.track.persist.TWorkflowDefPeer.getMapBuilder();
        com.aurel.track.persist.TWorkflowTransitionPeer.getMapBuilder();
        com.aurel.track.persist.TWorkflowStationPeer.getMapBuilder();
        com.aurel.track.persist.TWorkflowActivityPeer.getMapBuilder();
        com.aurel.track.persist.TWfActivityContextParamsPeer.getMapBuilder();
        com.aurel.track.persist.TWorkflowGuardPeer.getMapBuilder();
        com.aurel.track.persist.TWorkflowConnectPeer.getMapBuilder();
        com.aurel.track.persist.TWorkflowCommentPeer.getMapBuilder();
        com.aurel.track.persist.TSLAPeer.getMapBuilder();
        com.aurel.track.persist.TEscalationEntryPeer.getMapBuilder();
        com.aurel.track.persist.TEscalationStatePeer.getMapBuilder();
        com.aurel.track.persist.TOrgProjectSLAPeer.getMapBuilder();
        com.aurel.track.persist.TReadIssuePeer.getMapBuilder();
        com.aurel.track.persist.TLastExecutedQueryPeer.getMapBuilder();
        com.aurel.track.persist.TGlobalCssStylePeer.getMapBuilder();
        com.aurel.track.persist.TReportSubscribePeer.getMapBuilder();
        com.aurel.track.persist.TGridLayoutPeer.getMapBuilder();
        com.aurel.track.persist.TGridFieldPeer.getMapBuilder();
        com.aurel.track.persist.TGridGroupingSortingPeer.getMapBuilder();
        com.aurel.track.persist.TNavigatorLayoutPeer.getMapBuilder();
        com.aurel.track.persist.TNavigatorColumnPeer.getMapBuilder();
        com.aurel.track.persist.TNavigatorGroupingSortingPeer.getMapBuilder();
        com.aurel.track.persist.TCardGroupingFieldPeer.getMapBuilder();
        com.aurel.track.persist.TCardFieldOptionPeer.getMapBuilder();
        com.aurel.track.persist.TCardPanelPeer.getMapBuilder();
        com.aurel.track.persist.TCardFieldPeer.getMapBuilder();
        com.aurel.track.persist.TViewParamPeer.getMapBuilder();
        com.aurel.track.persist.TGeneralParamPeer.getMapBuilder();
        com.aurel.track.persist.TVersionControlPeer.getMapBuilder();
        com.aurel.track.persist.TShortcutPeer.getMapBuilder();
        com.aurel.track.persist.TMailTextBlockPeer.getMapBuilder();
        com.aurel.track.persist.TPRolePeer.getMapBuilder();
        com.aurel.track.persist.TChildProjectTypePeer.getMapBuilder();
        com.aurel.track.persist.TDomainPeer.getMapBuilder();
        com.aurel.track.persist.TPersonInDomainPeer.getMapBuilder();
        com.aurel.track.persist.TAttachmentVersionPeer.getMapBuilder();
        com.aurel.track.persist.TUserLevelPeer.getMapBuilder();
        com.aurel.track.persist.TUserLevelSettingPeer.getMapBuilder();
        com.aurel.track.persist.TUserFeaturePeer.getMapBuilder();
        com.aurel.track.persist.TItemTransitionPeer.getMapBuilder();
    }
}
