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

package com.aurel.track.browseProjects;

import java.util.Locale;

import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.admin.project.ProjectAccountingTO;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TPriorityBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TSeverityBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.beans.TSystemStateBean.ENTITYFLAGS;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.PropertiesHelper;

public class BrowseProjectsBL {
	public static final String STATE_ACTIVE="Active";
	public static final String STATE_INACTIVE="Inactive";
	public static final String STATE_UNSCHEDULED="Unscheduled";


	private static String getStateFlag(Integer stateFlag){
		switch (stateFlag.intValue()) {
		case TSystemStateBean.STATEFLAGS.ACTIVE:
			return STATE_ACTIVE;
		case TSystemStateBean.STATEFLAGS.INACTIVE:
			return STATE_INACTIVE;
		case TSystemStateBean.STATEFLAGS.NOT_PLANNED:
			return STATE_UNSCHEDULED;	
		default:
			return STATE_ACTIVE;
		}
	}
	public static ReleaseDetailTO loadReleaseDetail(Integer personID, Integer releaseID,Locale locale){
		ReleaseDetailTO releaseDetailTO=new ReleaseDetailTO();
		
		TReleaseBean releaseBean= LookupContainer.getReleaseBean(releaseID);
		TSystemStateBean statusBean= DAOFactory.getFactory().getSystemStateDAO().loadByPrimaryKey(releaseBean.getStatus());
		String releaseState = LocalizeUtil.localizeSystemStateEntry(statusBean, ENTITYFLAGS.RELEASESTATE, locale);
		String strDefaultReleaseNoticed = PropertiesHelper.getProperty(releaseBean.getMoreProperties(), 
				TReleaseBean.MOREPPROPS.DEFAULT_RELEASENOTICED);
		boolean releaseNoticedDefault=(strDefaultReleaseNoticed!=null && strDefaultReleaseNoticed.equals(Boolean.TRUE.toString()));
		String strDefaultReleaseScheduled = PropertiesHelper.getProperty(releaseBean.getMoreProperties(), 
				TReleaseBean.MOREPPROPS.DEFAULT_RELEASESCHEDULED);
		boolean releaseScheduledDefault=(strDefaultReleaseScheduled!=null && strDefaultReleaseScheduled.equals(Boolean.TRUE.toString()));
		String releaseDueDate=DateTimeUtils.getInstance().formatGUIDate(releaseBean.getDueDate(), locale); 
		
		releaseDetailTO.setDescription(releaseBean.getDescription());
		releaseDetailTO.setDueDate(releaseDueDate);
		releaseDetailTO.setNoticedDefault(releaseNoticedDefault);
		releaseDetailTO.setObjectID(releaseBean.getObjectID());
		releaseDetailTO.setScheduledDefault(releaseScheduledDefault);
		releaseDetailTO.setState(releaseState);
		releaseDetailTO.setStateFlag(getStateFlag(statusBean.getStateflag()));
		releaseDetailTO.setLabel(releaseBean.getLabel());

		boolean canEdit= PersonBL.isProjectAdmin(personID, releaseBean.getProjectID());
		releaseDetailTO.setCanEdit(canEdit);

		return releaseDetailTO;
	}

	
	public static BrowseProjectDetailTO loadProjectDetail(Integer personID,Integer projectID,Locale locale){
		BrowseProjectDetailTO result=new BrowseProjectDetailTO();
		TProjectBean project=LookupContainer.getProjectBean(projectID);
		if(project!=null){
			result.setProjectID(project.getObjectID());
			result.setProjectLabel(project.getLabel());
			result.setProjectDescription(project.getDescription());
			result.setProjectType(getProjectType(project));
			Integer projectStatusID=project.getStatus();
			TSystemStateBean statusBean= DAOFactory.getFactory().getSystemStateDAO().loadByPrimaryKey(projectStatusID);
			result.setProjectState(LocalizeUtil.localizeSystemStateEntry(statusBean, ENTITYFLAGS.PROJECTSTATE, locale));//(statusBean.getLabel(), "TPROJECTSTATUS",locale));
			boolean active;
			if(statusBean.getStateflag().intValue()==TSystemStateBean.STATEFLAGS.ACTIVE){
				active=true;
			}else{
				active=false;
			}
			result.setActive(active);
			result.setProjectLinking(project.isLinkingActive());
			
			ProjectAccountingTO projectAccountingTO = ProjectBL.getProjectAccountingTO(project.getObjectID());
			//String accountingString = PropertiesHelper.getProperty(project.getMoreProps(), TProjectBean.MOREPPROPS.ACCOUNTING_PROPERTY);
			result.setProjectWorkCost(projectAccountingTO.isWorkTracking() || projectAccountingTO.isCostTracking());
			
			//manager
			TPersonBean defaultManager = LookupContainer.getPersonBean(project.getDefaultManagerID());
			if(defaultManager!=null){
				result.setDefaultManager(defaultManager.getFullName());
			}
			//responsible
			TPersonBean defaultResponsible = LookupContainer.getPersonBean(project.getDefaultOwnerID());
			if(defaultManager!=null){
				result.setDefaultResponsible(defaultResponsible.getFullName());
			}
			
			//state
			Integer stateID=project.getDefaultInitStateID();
			if(stateID!=null){
				ILabelBean bean = LookupContainer.getLocalizedLabelBean(SystemFields.INTEGER_STATE, stateID, locale);
				if(bean!=null){
					result.setDefaultItemState(bean.getLabel());
					result.setDefaultItemStateID(bean.getObjectID());
				}
			}
			
			//issueType
			 String strDefaultIssueType = PropertiesHelper.getProperty(project.getMoreProperties(), TProjectBean.MOREPPROPS.DEFAULT_ISSUETYPE);
			 if (strDefaultIssueType!=null && !"".equals(strDefaultIssueType)) {
				 Integer issueTypeID=(new Integer(strDefaultIssueType));
				 TListTypeBean bean=LookupContainer.getItemTypeBean(issueTypeID, locale);
				 if(bean!=null){
					 result.setDefaultIssueType(bean.getLabel());
					 result.setDefaultIssueTypeID(bean.getObjectID());
				 }
			 }
			 //priority
			 String strDefaultPriority = PropertiesHelper.getProperty(project.getMoreProperties(), TProjectBean.MOREPPROPS.DEFAULT_PRIORITY);
			 if (strDefaultPriority!=null && !"".equals(strDefaultPriority)) {
				 Integer beanID=new Integer(strDefaultPriority);
				 TPriorityBean bean = LookupContainer.getPriorityBean(beanID, locale);
				 if(bean!=null){
					 result.setDefaultPriority(bean.getLabel());
					 result.setDefaultPriorityID(bean.getObjectID());
				 }
			 }
			//severity
			String strDefaultSeverity = PropertiesHelper.getProperty(project.getMoreProperties(), TProjectBean.MOREPPROPS.DEFAULT_SEVERITY);
			if (strDefaultSeverity!=null && !"".equals(strDefaultSeverity)) {
				Integer beanID=new Integer(strDefaultSeverity);
				TSeverityBean bean = LookupContainer.getSeverityBean(beanID, locale);
				if(bean!=null){
					result.setDefaultSeverity(bean.getLabel());
					result.setDefaultSeverityID(bean.getObjectID());
				}
			}
			boolean canEdit= PersonBL.isProjectAdmin(personID, projectID);
			result.setCanEdit(canEdit);

		}
		return result;
	}

	public static String getProjectType(TProjectBean project){
		String result=null;
		Integer projectTypeID=project.getProjectType();
		if(projectTypeID!=null){
			TProjectTypeBean projectTypeBean= ProjectTypesBL.loadByPrimaryKey(projectTypeID);
			if(projectTypeBean!=null){
				result=projectTypeBean.getLabel();
			}
		}
		return result;
	}

	/**
	* FIXME move from here
	* @deprecated move from here
	* @param fieldID
	* @param iconName
	* @return
	*/
	@Deprecated
	public static String getListIconPath(Integer fieldID,String iconName){
		return "list/-"+fieldID+"/"+iconName;
	}
}
