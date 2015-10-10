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

package com.aurel.track.linkType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.util.GeneralUtils;


public class CloseDependsOnLinkType extends AbstractSpecificLinkType {
	
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
	
private static CloseDependsOnLinkType instance;
	
	/**
	 * Return a FieldConfigItemFacade instance which implements the ConfigItemFacade 
	 * @return
	 */
	public static CloseDependsOnLinkType getInstance(){
		if(instance==null){
			instance=new CloseDependsOnLinkType();
		}
		return instance;
	}
	
	public int getPossibleDirection() {
		//return LINK_DIRECTION.LEFT_TO_RIGHT;
		return LINK_DIRECTION.RIGHT_TO_LEFT;
	}

	/**
	 * Validation called before saving the issue
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 * @param person
	 * @param mePredToSuccLinksOfType
	 * @param meSuccFromPredLinksOfType
	 * @param confirm
	 * @param locale
	 * @return
	 */
	public List<ErrorData> validateBeforeIssueSave(TWorkItemBean workItemBean, 
			TWorkItemBean workItemBeanOriginal, Integer person, 
			List<TWorkItemLinkBean> mePredToSuccLinksOfType,
			List<TWorkItemLinkBean> meSuccFromPredLinksOfType, boolean confirm, Locale locale) {
		List<ErrorData> errorsList = new ArrayList<ErrorData>();
		if (workItemBeanOriginal != null && workItemBean.getStateID()!=null && workItemBeanOriginal.getStateID()!=null &&
				workItemBean.getStateID().intValue()!= workItemBeanOriginal.getStateID().intValue()) {
			TStateBean stateBeanNew = LookupContainer.getStatusBean(workItemBean.getStateID());
			TStateBean stateBeanOld = LookupContainer.getStatusBean(workItemBeanOriginal.getStateID());
			Integer newStatusFlag = stateBeanNew.getStateflag();
			Integer oldStatusFlag = stateBeanOld.getStateflag();
			if (newStatusFlag.intValue()==TStateBean.STATEFLAGS.CLOSED && oldStatusFlag.intValue()!=TStateBean.STATEFLAGS.CLOSED) {
				//test whether is has open linked issues in the getRestrictedDirectionBeforeIssueSave() direction
				if (workItemBean.getObjectID()!=null) {
					//Iterator<TWorkItemLinkBean> iterator;
					//int restrictedDirection = getPossibleDirection();
					//gather the link successors to validate
					Set<Integer> linkedWorkItemIDs = new HashSet<Integer>();
					/*if (mePredToSuccLinksOfType!=null) {
						iterator = mePredToSuccLinksOfType.iterator();
						while (iterator.hasNext()) {
							TWorkItemLinkBean workItemLinkBean = iterator.next();
							if (workItemLinkBean.getLinkDirection().intValue()==restrictedDirection) {
								linkedWorkItemIDs.add(workItemLinkBean.getLinkSucc());
							}
						}
					}*/
					if (meSuccFromPredLinksOfType!=null) {
						//if this is not null the link is bidirectional
						//int reverseRestrictedDirection = LinkTypeBL.getReverseDirection(restrictedDirection);
						//if (restrictedDirection!=reverseRestrictedDirection) {
							Iterator<TWorkItemLinkBean> iterator = meSuccFromPredLinksOfType.iterator();
							while (iterator.hasNext()) {
								TWorkItemLinkBean workItemLinkBean = iterator.next();
								//if (workItemLinkBean.getLinkDirection().intValue()==reverseRestrictedDirection) {
									linkedWorkItemIDs.add(workItemLinkBean.getLinkPred());
								//}
							}
						//}
					}
					int[] workItemIDs = GeneralUtils.createIntArrFromSet(linkedWorkItemIDs);
					if (workItemDAO.isAnyWorkItemOpen(workItemIDs)) {
						ErrorData errorData = new ErrorData("item.err.closeOpenLinks");
						errorsList.add(errorData);
					}
				}
			}
		}
		return errorsList;
	}
	
	public boolean selectableForQueryResultSuperset() {
		return true;
	}

	public boolean isHierarchical() {
		return false;
	}
}
