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

package com.aurel.track.itemNavigator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.category.CategoryBL;
import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.category.filter.IssueFilterCategoryFacade;
import com.aurel.track.admin.customize.category.filter.TQLFilterFacade;
import com.aurel.track.admin.customize.category.filter.TreeFilterFacade;
import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.admin.user.userLevel.UserLevelBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TBasketBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.item.massOperation.MassOperationBL;
import com.aurel.track.item.massOperation.MassOperationException;
import com.aurel.track.item.operation.BasketBL;
import com.aurel.track.item.operation.BasketOperation;
import com.aurel.track.item.operation.ItemOperation;
import com.aurel.track.item.operation.ItemOperationException;
import com.aurel.track.item.operation.ItemOperationManager;
import com.aurel.track.item.operation.ItemOperationScheduling;
import com.aurel.track.item.operation.ItemOperationSelectIssueType;
import com.aurel.track.item.operation.ItemOperationSelectPriority;
import com.aurel.track.item.operation.ItemOperationSelectProject;
import com.aurel.track.item.operation.ItemOperationSelectState;
import com.aurel.track.itemNavigator.layout.NavigatorLayoutBL;
import com.aurel.track.itemNavigator.navigator.MenuItem;
import com.aurel.track.itemNavigator.navigator.Navigator;
import com.aurel.track.itemNavigator.navigator.Section;
import com.aurel.track.itemNavigator.navigator.View;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.TreeNode;

/**
 *
 *
 */
public class ItemNavigatorFilterBL {
	private static final Logger LOGGER = LogManager.getLogger(ItemNavigatorFilterBL.class);

	public static final String NODE_TYPE_SEPARATOR="_";
	public static interface NODE_TYPE_PREFIX{
		public static final int ITEM_OPERATION=1;
		public static final int QUERY=2;
	}
	public static interface NODE_TYPE{
		public static final int QUERY=2;
		public static final int QUERY_CATEGORY=3;
		public static final int QUERY_PROJECT=4;
		public static final int QUERY_FILTER=5;
		public static final int QUERY_PREDEFINED=6;
		public static final int QUERY_BASKET=7;
		public static final int QUERY_PROJECT_RELEASE=8;
		public static final int QUERY_SCHEDULED=9;
		public static final int QUERY_STATUS=10;

	}
	public static  interface SCHEDULE_TYPE{
		public static final int TODAY=0;
		public static final int TOMORROW=1;
		public static final int NEXT_WEEK=2;
		public static final int NEXT_MONTH=3;
		public static final int NOT_SCHEDULED=4;
		public static final int OVERDUE=5;
	}

	public static List<ReportBean> filter(List<ReportBean> reportBeansList,
			String filterNodeType, Integer filterNodeObjectID){
		LOGGER.debug("filter(): filterNodeType="+filterNodeType+" filterNodeObjectID="+filterNodeObjectID);
		if(reportBeansList==null){
			return null;
		}
		List<ReportBean> reportBeansListFilter=new ArrayList<ReportBean>();
		if(filterNodeType!=null){
			int nodeTypePrefix= getNodeTypePrefix(filterNodeType);
			switch (nodeTypePrefix){
				case NODE_TYPE_PREFIX.ITEM_OPERATION:{
					String operationType=getNodeType(filterNodeType);
					ItemOperation operation= ItemOperationManager.getOperation(operationType);
					if(operation!=null){
						reportBeansListFilter=operation.filter(reportBeansList,filterNodeObjectID);
					}else{
						reportBeansListFilter.addAll(reportBeansList);
					}
					break;
				}
				case NODE_TYPE_PREFIX.QUERY:{
					reportBeansListFilter.addAll(reportBeansList);
					break;
				}
			}
		}else{
			reportBeansListFilter.addAll(reportBeansList);
		}
		return reportBeansListFilter;
	}

	/**
	 *  Verify drop items on node. Return the invalid items
	 * @param nodeType
	 * @param nodeObjectID
	 * @param workItems
	 * @param personID
	 * @param locale
	 * @return invalid items
	 */
	public static Set<TWorkItemBean> canDropItemOnNode(String nodeType,Integer nodeObjectID,int[] workItems,Integer personID,Locale locale){
		Set<TWorkItemBean> result=new HashSet<TWorkItemBean>();
		if(workItems!=null){
			int nodeTypePrefix= getNodeTypePrefix(nodeType);
			switch (nodeTypePrefix){
				case NODE_TYPE_PREFIX.ITEM_OPERATION:{
					String operationType=getNodeType(nodeType);
					ItemOperation operation= ItemOperationManager.getOperation(operationType);
					if(operation!=null){
							if(!operation.canDrop(workItems,nodeObjectID)){
							}

					}
					break;
				}
			}
		}
		return result;
	}

	public static void dropItemOnNode(String nodeType,Integer nodeObjectID,int[] workItems,Integer personID,Locale locale,Map<String,String> params) throws ItemOperationException{
		int nodeTypePrefix= getNodeTypePrefix(nodeType);
		switch (nodeTypePrefix){
			case NODE_TYPE_PREFIX.ITEM_OPERATION:{
				String operationType=getNodeType(nodeType);
				ItemOperation operation= ItemOperationManager.getOperation(operationType);
				if(operation!=null){
					operation.execute(workItems,nodeObjectID,params, personID, locale);
				}
				break;
			}
			case NODE_TYPE_PREFIX.QUERY:{
				int queryTypeFromNode=ItemNavigatorFilterBL.getNodeTypeInt(nodeType);
				switch (queryTypeFromNode){
					case  NODE_TYPE.QUERY_PROJECT_RELEASE:{
						if(nodeObjectID!=null&&nodeObjectID.intValue()>0){
							//release
							Integer targetFieldID= SystemFields.INTEGER_RELEASE;
							Integer projectID=null;
							TReleaseBean releaseBean=LookupContainer.getReleaseBean(nodeObjectID);
							if (releaseBean!=null) {
								projectID=releaseBean.getProjectID();
								HashMap targetValue=new HashMap<Integer,Integer>();
								targetValue.put(projectID,nodeObjectID);
								try{
									MassOperationBL.saveExtern(workItems, targetFieldID, targetValue, personID, locale, false);
								}catch (MassOperationException ex){
									throw  new ItemOperationException(ItemOperationException.TYPE_MASS_OPERATION, ex);
								}
							}
						}else{
							//project
						}

						break;
					}
					case NODE_TYPE.QUERY_BASKET:{
						BasketBL.replaceBasket(workItems, personID, nodeObjectID,params,locale);
						break;
					}
					case NODE_TYPE.QUERY_STATUS:{
						try{
							boolean confirmSave=false;
							String confirmSaveStr=null;
							if(params!=null){
								confirmSaveStr=params.get("confirmSave");
							}
							if(confirmSaveStr!=null && "true".equalsIgnoreCase(confirmSaveStr)){
								confirmSave=true;
							}
							MassOperationBL.saveExtern(workItems,SystemFields.INTEGER_STATE,nodeObjectID,personID,locale,confirmSave);
						}catch (MassOperationException ex){
							throw  new ItemOperationException(ItemOperationException.TYPE_MASS_OPERATION, ex);
						}
						break;
					}
				}
			}
		}
	}

	public static Navigator createNavigator(TPersonBean personBean,List<ILabelBean> usedStatusList,Locale locale){
		Map<Integer, Boolean> userLevelMap = personBean.getUserLevelMap();
		Boolean hasAccessFiltersBoolean=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_MAIN_FILTER);		
		Boolean hasAccessSubFiltersBoolean=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_SUBFILTER);
		
		boolean hasAccessFilters=hasAccessFiltersBoolean!=null&&hasAccessFiltersBoolean.booleanValue();
		boolean hasAccessSubFilters=hasAccessSubFiltersBoolean!=null&&hasAccessSubFiltersBoolean.booleanValue();
		
		if(hasAccessFilters==false&&hasAccessSubFilters==false){
			return null;
		}

		Navigator nav=new Navigator();
		if(hasAccessFilters){
			View queryView=createQueryView(personBean,usedStatusList,locale);
			nav.setQueryView(queryView);
		}
		if(hasAccessSubFilters){
			Boolean hasAccessSubFiltersWorkspacesBoolean=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.SUBFILTER_PROJECT);
			Boolean hasAccessSubFiltersBasketsBoolean=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.SUBFILTER_BASKET);
			Boolean hasAccessSubFiltersStatesBoolean=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.SUBFILTER_STATUS);		
			Boolean hasAccessSubFiltersItemTypesBoolean=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.SUBFILTER_ITEMTYPE);
			Boolean hasAccessSubFiltersPrioritiesBoolean=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.SUBFILTER_PRIORITY);
			Boolean hasAccessSubFiltersScheduleBoolean=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.SUBFILTER_SCHEDULE);

			boolean hasAccessSubFiltersWorkspaces=hasAccessSubFiltersWorkspacesBoolean != null && hasAccessSubFiltersWorkspacesBoolean.booleanValue();
			boolean hasAccessSubFiltersBaskets=hasAccessSubFiltersBasketsBoolean != null && hasAccessSubFiltersBasketsBoolean.booleanValue();
			boolean hasAccessSubFiltersStates=hasAccessSubFiltersStatesBoolean != null && hasAccessSubFiltersStatesBoolean.booleanValue();
			boolean hasAccessSubFiltersItemTypes=hasAccessSubFiltersItemTypesBoolean != null &&  hasAccessSubFiltersItemTypesBoolean.booleanValue();
			boolean hasAccessSubFiltersPriorities=hasAccessSubFiltersPrioritiesBoolean != null && hasAccessSubFiltersPrioritiesBoolean.booleanValue();
			boolean hasAccessSubFiltersSchedule=hasAccessSubFiltersScheduleBoolean != null && hasAccessSubFiltersScheduleBoolean.booleanValue();

			View subFilterView=new View();
			subFilterView.setName(LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.subfilter", locale));
			subFilterView.setObjectID(1);
			subFilterView.setIconCls("subfilterView");

			List<ItemOperation> itemOperationList=new ArrayList<ItemOperation>();
			if (hasAccessSubFiltersWorkspaces) {
				itemOperationList.add(new ItemOperationSelectProject(locale));
			}
			if (hasAccessSubFiltersStates) {
				itemOperationList.add(new ItemOperationSelectState(usedStatusList,locale));
			}
			if (hasAccessSubFiltersPriorities) {
				itemOperationList.add(new ItemOperationSelectPriority(locale));
			}
			if (hasAccessSubFiltersItemTypes){
				itemOperationList.add(new ItemOperationSelectIssueType(locale));
			}
			if(hasAccessSubFiltersBaskets) {
				itemOperationList.add(new BasketOperation(personBean,locale));
			}
			if (hasAccessSubFiltersSchedule){				
				itemOperationList.add(new ItemOperationScheduling(locale));
			}

			List<Section> sections=new ArrayList<Section>();

			for (int i=0;i<itemOperationList.size();i++){
				ItemOperation operation=itemOperationList.get(i);
				ItemOperationManager.registerOperation(operation);
				sections.add(createSection(operation,personBean,locale));
			}
			subFilterView.setSections(sections);

			nav.setSubFilterView(subFilterView);
		}

		return nav;
	}
	private static Section createSection(ItemOperation operation,TPersonBean personBean,Locale locale){
		Section section=new Section();
		section.setId("section_operation_"+operation.getPluginID());
		section.setName(operation.getName());
		section.setIcon(operation.getIcon());
		section.setIconCls(operation.getIconCls());
		section.setMenu(operation.getChildren(personBean, locale, null));
		return section;
	}

	private static View createQueryView(TPersonBean personBean,List<ILabelBean> usedStatusList,Locale locale){
		View queryView=new View();
		queryView.setName(getText("common.lbl.queries",locale));
		queryView.setObjectID(101);
		queryView.setIconCls("queryView");
		
		Map<Integer, Boolean> userLevelMap = personBean.getUserLevelMap();
		
		Boolean hasAccessFiltersWorkspacesBoolean=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.MAIN_FILTER_PROJECT);
		Boolean hasAccessFiltersFiltersBoolean=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.MAIN_FILTER_FILTER);
		Boolean hasAccessFiltersBasketsBoolean=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.MAIN_FILTER_BASKET);
		Boolean hasAccessFiltersStatesBoolean=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.MAIN_FILTER_STATUS);

		boolean hasAccessFiltersWorkspaces=hasAccessFiltersWorkspacesBoolean != null && hasAccessFiltersWorkspacesBoolean.booleanValue();
		boolean hasAccessFiltersFilters=hasAccessFiltersFiltersBoolean != null &&  hasAccessFiltersFiltersBoolean.booleanValue();
		boolean hasAccessFiltersBaskets=hasAccessFiltersBasketsBoolean != null && hasAccessFiltersBasketsBoolean.booleanValue();
		boolean hasAccessFiltersStates=hasAccessFiltersStatesBoolean != null && hasAccessFiltersStatesBoolean.booleanValue();


		List<Section> sections=new ArrayList<Section>();

		if (hasAccessFiltersWorkspaces) {
			Section projectSection = createProjectSection(personBean, locale);
			if(projectSection!=null){
				sections.add(projectSection);
			}
		}
		
		if (hasAccessFiltersFilters){
			sections.add(createQuerySection(personBean, locale));
		}
		
		if (hasAccessFiltersStates) {
			sections.add(createStatusSection(personBean,usedStatusList, locale));
		}
		if(hasAccessFiltersBaskets){
			Section basketSection = createBasketSection(userLevelMap,locale);
			if(basketSection!=null){
				sections.add(basketSection);
			}
		}


		queryView.setSections(sections);
		return queryView;
	}
	private static  MenuItem createMenuItemProjectRelease(TreeNode treeNode,boolean allowDrop,String type){
		MenuItem mit=new MenuItem();
		Integer objectID=Integer.parseInt(treeNode.getId());
		mit.setId(ItemNavigatorBL.QUERY_TYPE.PROJECT_RELEASE+NODE_TYPE_SEPARATOR+objectID);
		mit.setType(type);

		mit.setObjectID(objectID);
		mit.setName(treeNode.getLabel());
		mit.setAllowDrop(allowDrop&&objectID.intValue()>0);
		mit.setUseFilter(true);
		mit.setLazyChildren(false);
		mit.setIconCls(treeNode.getIcon());
		List<TreeNode> children=treeNode.getChildren();
		if(children!=null && !children.isEmpty()){
			List<MenuItem> mnuChildren=createMenuItemsFromProjectNodes(children,type);
			mit.setChildren(mnuChildren);
		}
		return mit;
	}
	public static Section createProjectSection(TPersonBean personBean,Locale locale){
		Section s=null;
		int [] rights=new int[] {
				AccessBeans.AccessFlagIndexes.PROJECTADMIN,
				AccessBeans.AccessFlagIndexes.READANYTASK
		};
		List<TreeNode> projects=ReleaseBL.getReleaseNodesEager(personBean, null,false,true,true,true,null,false,true,false,null,locale);
		if(projects!=null && !projects.isEmpty()){
			s=new Section();
			s.setName(getText("menu.admin.project",locale));
			s.setId("section_project");
			s.setIconCls(ProjectBL.PROJECT_ICON_CLASS);
			List<MenuItem> menuItems=createMenuItemsFromProjectNodes(projects,NODE_TYPE_PREFIX.QUERY+NODE_TYPE_SEPARATOR+NODE_TYPE.QUERY_PROJECT_RELEASE);
			s.setMenu(menuItems);
		}
		return s;
	}
	public static List<MenuItem> createMenuItemsFromProjectNodes(List<TreeNode> projects,String type){
		List<MenuItem> menuItemList=new ArrayList<MenuItem>();
		for(TreeNode treeNode:projects){
			MenuItem menuItem=createMenuItemProjectRelease(treeNode,true,type);
			menuItemList.add(menuItem);
		}
		return menuItemList;
	}
	private static Section createStatusSection(TPersonBean personBean,List<ILabelBean> usedStatusList, Locale locale) {
		Section statusSection=null;
		if(usedStatusList!=null && !usedStatusList.isEmpty()){
			statusSection=new Section();
			statusSection.setName(LocalizeUtil.getLocalizedEntity(LocalizationKeyPrefixes.FIELD_LABEL_KEY_PREFIX, SystemFields.INTEGER_STATE, locale));
			statusSection.setId("section_status");
			statusSection.setIconCls(ListBL.ICONS_CLS.STATUS_ICON);
			List<MenuItem> menuItems=new ArrayList<MenuItem>();
			for(ILabelBean labelBean:usedStatusList){
				menuItems.add(createQueryNodeStatus(labelBean));
			}
			statusSection.setMenu(menuItems);
		}
		return statusSection;
	}

	private static Section createBasketSection(Map<Integer, Boolean> userLevelMap,Locale locale) {
		Section basketSection=null;
		List<TBasketBean> baskets= BasketBL.getRootBaskets();
		if(baskets!=null && !baskets.isEmpty()){
			basketSection=new Section();
			basketSection.setName(getText("common.lbl.basket",locale));
			basketSection.setId("section_basket");
			basketSection.setIconCls(BasketBL.ICON_CLS.BASKET_ICON);
			List<MenuItem> menuItems=new ArrayList<MenuItem>();
			for(TBasketBean basketBean:baskets){
				if(!BasketBL.hasBasketAccess(userLevelMap,basketBean.getObjectID())){
					continue;
				}
				menuItems.add(createQueryNodeBasket(basketBean,locale));
			}
			basketSection.setMenu(menuItems);
		}
		return basketSection;
	}
	private static Section createScheduledSection(Locale locale) {
		Section section=new Section();
		section.setName(getText("itemov.lbl.scheduled", locale));
		section.setId("section_schedule");
		section.setIconCls(ItemOperationScheduling.ICON_CLS.SCHEDULE_ICON);
		List<MenuItem> menuItems=new ArrayList<MenuItem>();
		String type= NODE_TYPE_PREFIX.QUERY+ItemNavigatorFilterBL.NODE_TYPE_SEPARATOR+NODE_TYPE.QUERY_SCHEDULED;
		menuItems.add(new MenuItem(type,ItemNavigatorFilterBL.SCHEDULE_TYPE.TODAY,
				getText("itemov.lbl.scheduled.today", locale),ItemOperationScheduling.ICON_CLS.SCHEDULE_TODAY,true,false,
				ItemNavigatorBL.QUERY_TYPE.SCHEDULED+"_"+ItemNavigatorFilterBL.SCHEDULE_TYPE.TODAY));
		menuItems.add(new MenuItem(type,ItemNavigatorFilterBL.SCHEDULE_TYPE.TOMORROW,
				getText("itemov.lbl.scheduled.tomorrow", locale),ItemOperationScheduling.ICON_CLS.SCHEDULE_TOMORROW,true,false,
				ItemNavigatorBL.QUERY_TYPE.SCHEDULED+"_"+ SCHEDULE_TYPE.TOMORROW));
		menuItems.add(new MenuItem(type,ItemNavigatorFilterBL.SCHEDULE_TYPE.NEXT_WEEK,
				getText("itemov.lbl.scheduled.nextWeek", locale),ItemOperationScheduling.ICON_CLS.SCHEDULE_NEXT_WEEK,true,false,
				ItemNavigatorBL.QUERY_TYPE.SCHEDULED+"_"+ SCHEDULE_TYPE.NEXT_WEEK));
		menuItems.add(new MenuItem(type,ItemNavigatorFilterBL.SCHEDULE_TYPE.NEXT_MONTH,
				getText("itemov.lbl.scheduled.nextMonth", locale),ItemOperationScheduling.ICON_CLS.SCHEDULE_NEXT_MONTH,true,false,
				ItemNavigatorBL.QUERY_TYPE.SCHEDULED+"_"+ SCHEDULE_TYPE.NEXT_MONTH));
		menuItems.add(new MenuItem(type,ItemNavigatorFilterBL.SCHEDULE_TYPE.NOT_SCHEDULED,
				getText("itemov.lbl.scheduled.notScheduled", locale),ItemOperationScheduling.ICON_CLS.NOT_SCHEDULED,true,false,
				ItemNavigatorBL.QUERY_TYPE.SCHEDULED+"_"+ SCHEDULE_TYPE.NOT_SCHEDULED));
		menuItems.add(new MenuItem(type,ItemNavigatorFilterBL.SCHEDULE_TYPE.OVERDUE,
				getText("itemov.lbl.scheduled.overdue", locale),ItemOperationScheduling.ICON_CLS.OVERDUE,true,false,
				ItemNavigatorBL.QUERY_TYPE.SCHEDULED+"_"+ SCHEDULE_TYPE.OVERDUE));

		section.setMenu(menuItems);

		return section;
	}

	private static Section createQuerySection(TPersonBean personBean, Locale locale) {
		Section s=new Section();
		s.setName(getText("common.lbl.queries",locale));
		s.setId("section_query");
		s.setIconCls(FilterBL.ICONS.TREE_FILTER);
		List<MenuItem> menuItems=new ArrayList<MenuItem>();
		Integer personID = personBean.getObjectID();
		List<TQueryRepositoryBean> myMenuFilters = FilterBL.loadMyMenuFilters(personID, locale);
		if (myMenuFilters!=null) {
			for (TQueryRepositoryBean queryRepositoryBean : myMenuFilters) {
				MenuItem mit=new MenuItem();
				mit.setId(ItemNavigatorBL.QUERY_TYPE.SAVED+NODE_TYPE_SEPARATOR+queryRepositoryBean.getObjectID());
				mit.setType(""+ NODE_TYPE_PREFIX.QUERY+NODE_TYPE_SEPARATOR+ NODE_TYPE.QUERY_FILTER);
				mit.setObjectID(queryRepositoryBean.getObjectID());
				mit.setUseFilter(true);
				mit.setFilterViewID(queryRepositoryBean.getViewID());
				mit.setMaySaveFilterLayout(queryRepositoryBean.getViewID()!=null && NavigatorLayoutBL.isModifiable(queryRepositoryBean, personBean));
				mit.setIconCls(FilterBL.getItemFilterIconCls(queryRepositoryBean));
				mit.setName(queryRepositoryBean.getLabel());
				menuItems.add(mit);
			}
		}
		s.setMenu(menuItems);
		return s;
	}


	public static MenuItem createQueryNodeStatus(ILabelBean labelBean){
		MenuItem mit=new MenuItem();
		mit.setId(ItemNavigatorBL.QUERY_TYPE.STATUS+NODE_TYPE_SEPARATOR+labelBean.getObjectID());
		mit.setType(""+NODE_TYPE_PREFIX.QUERY+NODE_TYPE_SEPARATOR+NODE_TYPE.QUERY_STATUS);
		mit.setObjectID(labelBean.getObjectID());
		mit.setName(labelBean.getLabel());
		mit.setAllowDrop(true);
		mit.setUseFilter(true);
		String iconName = null;
		try{
			iconName=((TStateBean)labelBean).getSymbol();
		}catch (Exception e){
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		if(iconName!=null){
		    mit.setIcon("optionIconStream.action?fieldID=-4&optionID="+labelBean.getObjectID() + "&time="+new Date().getTime());
		}
		return mit;
	}
	
	private static MenuItem createQueryNodeBasket(TBasketBean basketBean, Locale locale){
		MenuItem mit=new MenuItem();
		mit.setId(ItemNavigatorBL.QUERY_TYPE.BASKET+NODE_TYPE_SEPARATOR+basketBean.getObjectID());
		mit.setType(""+NODE_TYPE_PREFIX.QUERY+NODE_TYPE_SEPARATOR+NODE_TYPE.QUERY_BASKET);
		mit.setObjectID(basketBean.getObjectID());
		mit.setIconCls(BasketBL.getIconCls(basketBean.getObjectID()));
		mit.setName(LocalizeUtil.localizeDropDownEntry(basketBean, locale));
		mit.setUseFilter(true);
		mit.setAllowDrop(true);
		mit.setDropHandlerCls("com.trackplus.itemNavigator.BasketDropHandler");
		return mit;

	}

	public static int getNodeTypePrefix(String nodeTypeStr){
		if(nodeTypeStr==null){
			return -1;
		}
		int idxSeparator= nodeTypeStr.indexOf(NODE_TYPE_SEPARATOR);
		if(idxSeparator==-1){
			return -1;
		}
		String nodePrefixStr=nodeTypeStr.substring(0,idxSeparator);
		int nodePrefix=-1;
		try{
			nodePrefix=Integer.parseInt(nodePrefixStr);
		}catch (Exception ex){
		}
		return nodePrefix;
	}
	public static String getNodeType(String nodeTypeStr){
		if(nodeTypeStr==null){
			return null;
		}
		int idxSeparator= nodeTypeStr.indexOf(NODE_TYPE_SEPARATOR);
		if(idxSeparator==-1){
			return null;
		}
		return nodeTypeStr.substring(idxSeparator+1);
	}
	public static int getNodeTypeInt(String nodeTypeStr){
		if(nodeTypeStr==null){
			return -1;
		}
		int idxSeparator= nodeTypeStr.indexOf(NODE_TYPE_SEPARATOR);
		if(idxSeparator==-1){
			return -1;
		}
		int result=-1;
		String nodeType=nodeTypeStr.substring(idxSeparator+1);
		try{
			result=Integer.parseInt(nodeType);
		}catch (Exception ex){}
		return result;
	}
	public static List<MenuItem> expandNode(String nodeTypeStr,Integer nodeObjectID,TPersonBean personBean,Locale locale){
		Integer personID=personBean.getObjectID();
		if(nodeTypeStr==null){
			return null;
		}
		int nodePrefix=getNodeTypePrefix(nodeTypeStr);
		List<MenuItem> result=null;
		switch (nodePrefix){
			case NODE_TYPE_PREFIX.ITEM_OPERATION:{
				String operationType=getNodeType(nodeTypeStr);
				ItemOperation operation= ItemOperationManager.getOperation(operationType);
				if(operation!=null){
					result=operation.getChildren(personBean,locale,nodeObjectID);
				}
				break;
			}
			case NODE_TYPE_PREFIX.QUERY:{
				int nodeType=getNodeTypeInt(nodeTypeStr);
				switch (nodeType){
					case NODE_TYPE.QUERY:
						result=getQueryNodes(nodeObjectID,personID, locale);
						break;
					case NODE_TYPE.QUERY_CATEGORY:
						result=getCategoryNodes(nodeObjectID,personID, locale);
						break;
					case NODE_TYPE.QUERY_PROJECT:
						result=getRootNodes(CategoryBL.REPOSITORY_TYPE.PROJECT,nodeObjectID,personID, locale);
						break;
					case NODE_TYPE.QUERY_PROJECT_RELEASE:
						break;
				}
				break;
			}
		}
		return result;
	}
	private static List<MenuItem> getQueryNodes(Integer nodeObjectID,Integer personID, Locale locale){
		List<MenuItem> result=null;
		if(nodeObjectID!=null){
			switch (nodeObjectID.intValue()){
				case CategoryBL.REPOSITORY_TYPE.PRIVATE:{
					result=getRootNodes(CategoryBL.REPOSITORY_TYPE.PRIVATE, null, personID, locale);
					break;
				}
				case CategoryBL.REPOSITORY_TYPE.PUBLIC:{
					result=getRootNodes(CategoryBL.REPOSITORY_TYPE.PUBLIC, null, personID, locale);
					break;
				}
				case CategoryBL.REPOSITORY_TYPE.PROJECT:{
					result= getQueryProjectNodes(personID);
					break;
				}
			}
		}
		return  result;
	}
	private static List<MenuItem> getQueryProjectNodes(Integer personID){
		List<MenuItem> result=new ArrayList<MenuItem>();
		List<TProjectBean> projects= ProjectBL.loadProjectsWithReadIssueRight(personID);
		if(projects!=null){
			for (int i=0;i<projects.size();i++){
				result.add(createMenuItemFromQueryProject(projects.get(i)));
			}
		}
		return result;
	}
	private static List<MenuItem> getCategoryNodes(Integer categoryID, Integer personID, Locale locale){
		IssueFilterCategoryFacade issueFilterCategoryFacade=IssueFilterCategoryFacade.getInstance();
		TreeFilterFacade treeFilterFacade=TreeFilterFacade.getInstance();
		TQLFilterFacade tqlFilterFacade=TQLFilterFacade.getInstance();
		List<ILabelBean> categories=issueFilterCategoryFacade.getByParent(categoryID, locale);
		List<ILabelBean> treeFilters=treeFilterFacade.getByParent(categoryID, locale);
		List<MenuItem> result = procesQueryObjectsAsNode(categories, treeFilters);
		return result;
	}
	private static List<MenuItem> getRootNodes(Integer repository, Integer projectID, Integer personID, Locale locale){
		IssueFilterCategoryFacade issueFilterCategoryFacade=IssueFilterCategoryFacade.getInstance();
		TreeFilterFacade treeFilterFacade=TreeFilterFacade.getInstance();
		List<ILabelBean> categories=issueFilterCategoryFacade.getRootObjects(repository, projectID, personID, locale);
		List<ILabelBean> treeFilters=treeFilterFacade.getRootObjects(repository,projectID, personID, locale);
		List<MenuItem> result = procesQueryObjectsAsNode(categories, treeFilters);
		return  result;
	}

	private static List<MenuItem> procesQueryObjectsAsNode(List<ILabelBean> categories, List<ILabelBean> filters) {
		List<MenuItem> result=new ArrayList<MenuItem>();
		if(categories!=null){
			for(int i=0;i<categories.size();i++){
				result.add(createMenuItemFromCategory(categories.get(i)));
			}
		}
		if(filters!=null){
			for(int i=0;i<filters.size();i++){
				result.add(createMenuItemFromFilter(filters.get(i)));
			}
		}
		return result;
	}

	private static MenuItem createMenuItemFromQueryProject(TProjectBean projectBean){
		MenuItem mit=new MenuItem();
		mit.setType(""+NODE_TYPE_PREFIX.QUERY+NODE_TYPE_SEPARATOR+NODE_TYPE.QUERY_PROJECT);
		mit.setObjectID(projectBean.getObjectID());
		mit.setLazyChildren(true);
		mit.setName(projectBean.getLabel());
		return  mit;
	}
	private static MenuItem createMenuItemFromCategory(ILabelBean labelBean){
		MenuItem mit=new MenuItem();
		mit.setType(""+NODE_TYPE_PREFIX.QUERY+NODE_TYPE_SEPARATOR+NODE_TYPE.QUERY_CATEGORY);
		mit.setObjectID(labelBean.getObjectID());
		mit.setLazyChildren(true);
		mit.setName(labelBean.getLabel());
		return  mit;
	}
	private static MenuItem createMenuItemFromFilter(ILabelBean labelBean){
		MenuItem mit=new MenuItem();
		mit.setId(ItemNavigatorBL.QUERY_TYPE.SAVED+NODE_TYPE_SEPARATOR+labelBean.getObjectID());
		mit.setType(""+NODE_TYPE_PREFIX.QUERY+NODE_TYPE_SEPARATOR+NODE_TYPE.QUERY_FILTER);
		mit.setObjectID(labelBean.getObjectID());
		mit.setLazyChildren(false);
		mit.setName(labelBean.getLabel());
		mit.setUseFilter(true);
		mit.setIconCls(FilterBL.ICONS.TREE_FILTER);
		return  mit;
	}
	private static String getText(String s, Locale locale){
		return LocalizeUtil.getLocalizedTextFromApplicationResources(s, locale);
	}



}
