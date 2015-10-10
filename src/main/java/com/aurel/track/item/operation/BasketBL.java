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

package com.aurel.track.item.operation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.user.userLevel.UserLevelBL;
import com.aurel.track.beans.TBasketBean;
import com.aurel.track.beans.TPersonBasketBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.BasketDAO;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.PersonBasketDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.item.massOperation.MassOperationBL;
import com.aurel.track.item.massOperation.MassOperationException;
import com.aurel.track.itemNavigator.ItemNavigatorFilterBL;
import com.aurel.track.itemNavigator.navigator.MenuItem;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.GeneralUtils;

/**
 *
 */
public class BasketBL {
	private static final Logger LOGGER = LogManager.getLogger(BasketBL.class);
	private static BasketDAO basketDAO=DAOFactory.getFactory().getBasketDAO();
	private static PersonBasketDAO personBasketDAO=DAOFactory.getFactory().getPersonBasketDAO();
	
	
	public static interface ICON_CLS {
		//the general basket icon 
		static String BASKET_ICON="basket-ticon";
		//icons specific to each basket
		static String IN_BASKET="basket-ticon";
		static String PLANNED_ITEMS="projectActive16-ticon";
		static String CALENDAR="calendar-ticon";
		static String TRASH="trash-ticon";
		static String INCUBATOR="incubator-ticon";
		static String REFERENCE="reference-ticon";
		static String NEXT_ACTIONS="action-ticon";
		static String DELEGATED="delegated-ticon";
	}
	
	public static List<TBasketBean> getRootBaskets(){
		return basketDAO.loadMainBaskets();
	}
	
	/**
	 * Loads child baskets of a parent basket   
	 * @return
	 */
	public static List<TBasketBean> loadChildBaskets(Integer basketID) {
		return basketDAO.loadChildBaskets(basketID);
	}
	
	public static List<MenuItem> computeChildren(TPersonBean personBean, Locale locale) {
		List<TBasketBean> baskets=getRootBaskets();
		List<MenuItem> menuItems=new ArrayList<MenuItem>();
		Map<Integer, Boolean> userLevelMap = personBean.getUserLevelMap();
		for (int i = 0; i < baskets.size(); i++) {
			TBasketBean basket =  baskets.get(i);
			if(!hasBasketAccess(userLevelMap,basket.getObjectID())){
				continue;
			}
			MenuItem mit=new MenuItem();
			mit.setType(ItemNavigatorFilterBL.NODE_TYPE_PREFIX.ITEM_OPERATION+ItemNavigatorFilterBL.NODE_TYPE_SEPARATOR+BasketOperation.PLUGIN_ID);
			mit.setObjectID(basket.getObjectID());
			mit.setName(LocalizeUtil.localizeDropDownEntry(basket, locale));
			mit.setLevel(1);
			boolean allowDrop=true;
			boolean verifyAllowDropAJAX=true;
			if(basket.getObjectID()==TBasketBean.BASKET_TYPES.IN_BASKET){
				allowDrop=false;
				verifyAllowDropAJAX=false;
			}
			mit.setAllowDrop(allowDrop);
			mit.setVerifyAllowDropAJAX(verifyAllowDropAJAX);
			mit.setUseFilter(true);
			mit.setDropHandlerCls("com.trackplus.itemNavigator.BasketDropHandler");
			String iconCls=getIconCls(basket.getObjectID());
			mit.setIconCls(iconCls);
			menuItems.add(mit);
		}
		return menuItems;
	}
	public static boolean hasBasketAccess(Map<Integer, Boolean> userLevelMap, Integer basketID){
		Boolean result=false;
		switch (basketID){
			case TBasketBean.BASKET_TYPES.IN_BASKET:{
				result=true;
				break;
			}
			case TBasketBean.BASKET_TYPES.PLANNED_ITEMS:{
				result=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.PLANNED_ITEMS_BASKET);
				break;
			}
			case TBasketBean.BASKET_TYPES.CALENDAR:{
				result=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.REMINDER_BASKET);
				break;
			}
			case TBasketBean.BASKET_TYPES.TRASH:{
				result=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.TRASH_BASKET);
				break;
			}
			case TBasketBean.BASKET_TYPES.INCUBATOR:{
				result=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.INCUBATOR_BASKET);
				break;
			}
			case TBasketBean.BASKET_TYPES.REFERENCE:{
				result=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.REFERENCE_BASKET);
				break;
			}
			case TBasketBean.BASKET_TYPES.NEXT_ACTIONS:{
				result=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.NEXT_ACTIONS_BASKET);
				break;
			}
			case TBasketBean.BASKET_TYPES.DELEGATED:{
				result=userLevelMap.get(UserLevelBL.USER_LEVEL_ACTION_IDS.DELEGATED_BASKET);
				break;
			}
		}
		return result==null?false:result.booleanValue();
	}
	public static String getIconCls(Integer basketID){
		String icon=null;
		switch (basketID){
			case TBasketBean.BASKET_TYPES.IN_BASKET:{
				icon=ICON_CLS.IN_BASKET;
				break;
			}
			case TBasketBean.BASKET_TYPES.PLANNED_ITEMS:{
				icon=ICON_CLS.PLANNED_ITEMS;
				break;
			}
			case TBasketBean.BASKET_TYPES.CALENDAR:{
				icon=ICON_CLS.CALENDAR;
				break;
			}
			case TBasketBean.BASKET_TYPES.TRASH:{
				icon=ICON_CLS.TRASH;
				break;
			}
			case TBasketBean.BASKET_TYPES.INCUBATOR:{
				icon=ICON_CLS.INCUBATOR;
				break;
			}
			case TBasketBean.BASKET_TYPES.REFERENCE:{
				icon=ICON_CLS.REFERENCE;
				break;
			}
			case TBasketBean.BASKET_TYPES.NEXT_ACTIONS:{
				icon=ICON_CLS.NEXT_ACTIONS;
				break;
			}
			case TBasketBean.BASKET_TYPES.DELEGATED:{
				icon=ICON_CLS.DELEGATED;
				break;
			}
			default:
				icon=null;
				break;
		}
		return icon;
	}
	public static boolean canDrop(int[] workItemIds, Integer basketID,Integer personID){
		boolean result=true;
		/*if(basketID==null){
			result=false;
		}else{
			Map<Integer,TBasketBean> basketBeanMap=loadBasketMap(workItemIds,personID);
			for(int i=0;i<workItemIds.length;i++){
				TBasketBean basket=basketBeanMap.get(workItemIds[i]);
				if(basket==null){
					//implicitly issues are in IN_BASKET
					result=true;
				}else{
					if(!basketID.equals(basket.getObjectID())){
						return  false;
					};
				}
			}
		}*/
		return  result;
	}
	public static List<ReportBean> filter(List<ReportBean> reportBeans, Integer basketID,Integer personID){
		LOGGER.debug("Filter ");
		List<ReportBean>  result=new ArrayList<ReportBean>();
		if(reportBeans==null){
			LOGGER.debug("Filter null reportBeans!");
		}else{
			LOGGER.debug("Filter reportBeans:"+reportBeans.size()+" by basketID="+basketID);
			if(basketID==null){
				result.addAll(reportBeans);
			}else{
				Map<Integer,TPersonBasketBean> basketBeanMap= loadPersonBasketMap(reportBeans, personID);
				ReportBean reportBean;
				for(int i=0;i<reportBeans.size();i++){
					reportBean=reportBeans.get(i);
					TPersonBasketBean personBasket=basketBeanMap.get(reportBean.getWorkItemBean().getObjectID());
					if(personBasket==null){
						//implicitly issues are in IN_BASKET
						if(basketID==TBasketBean.BASKET_TYPES.IN_BASKET){
							if(personID.equals(reportBean.getWorkItemBean().getResponsibleID())){
								result.add(reportBean);
							}
						}
					}else{
						if(basketID.equals(personBasket.getBasket())){
							result.add(reportBean);
						}
					}
				}
			}
		}
		LOGGER.debug("Filtered beans size:"+result.size());
		return result;
	}
	public static List<TWorkItemBean> filterWorkItems(List<TWorkItemBean> workItemBeans, Integer basketID,Integer personID){
		LOGGER.debug("Filter ");
		List<TWorkItemBean>  result=new ArrayList<TWorkItemBean>();
		if(workItemBeans==null){
			LOGGER.debug("Filter null workItemBeans!");
		}else{
			LOGGER.debug("Filter workItemBeans:"+workItemBeans.size()+" by basketID="+basketID);
			if(basketID==null){
				result.addAll(workItemBeans);
			}else{
				Map<Integer,TPersonBasketBean> basketBeanMap= loadPersonBasketMapByWorkItems(workItemBeans, personID);
				TWorkItemBean workItemBean;
				for(int i=0;i<workItemBeans.size();i++){
					workItemBean=workItemBeans.get(i);
					TPersonBasketBean personBasket=basketBeanMap.get(workItemBean.getObjectID());
					if(personBasket==null){
						//implicitly issues are in IN_BASKET
						if(basketID==TBasketBean.BASKET_TYPES.IN_BASKET){
							result.add(workItemBean);
						}
					}else{
						if(basketID.equals(personBasket.getBasket())){
							result.add(workItemBean);
						}
					}
				}
			}
		}
		LOGGER.debug("Filtered beans size:"+result.size());
		return result;
	}
	private static Map<Integer,TBasketBean> loadBasketMap(int[] workItemIDs,Integer personID){
		List<TBasketBean> basketBeanList=basketDAO.loadBasketsByItemsAndPerson(workItemIDs,personID);
		Map<Integer,TBasketBean> basketBeanMap=new HashMap<Integer, TBasketBean>();
		TBasketBean basketBean;
		for(int i=0;i<basketBeanList.size();i++){
			basketBean=basketBeanList.get(i);
			basketBeanMap.put(basketBean.getObjectID(),basketBean);
		}
		return  basketBeanMap;
	}

	private static Map<Integer,TPersonBasketBean> loadPersonBasketMap(List<ReportBean> reportBeans, Integer personID){
		List<Integer> workItemIDList= new ArrayList<Integer>();
		for(int i=0;i<reportBeans.size();i++){
			workItemIDList.add(reportBeans.get(i).getWorkItemBean().getObjectID());
		}
		return loadPersonBasketMapByItemIDs(workItemIDList, personID);
	}
	private static Map<Integer,TPersonBasketBean> loadPersonBasketMapByWorkItems(List<TWorkItemBean> workItemBeans, Integer personID){
		List<Integer> workItemIDList= new ArrayList<Integer>();
		for(int i=0;i<workItemBeans.size();i++){
			workItemIDList.add(workItemBeans.get(i).getObjectID());
		}
		return loadPersonBasketMapByItemIDs(workItemIDList, personID);
	}
	private static Map<Integer,TPersonBasketBean> loadPersonBasketMapByItemIDs(List<Integer> workItemIDList, Integer personID){
		List<TPersonBasketBean> basketBeanList=personBasketDAO.loadBasketsByItemsAndPerson(workItemIDList,personID);
		Map<Integer,TPersonBasketBean> basketBeanMap=new HashMap<Integer, TPersonBasketBean>();
		TPersonBasketBean basketBean;
		for(int i=0;i<basketBeanList.size();i++){
			basketBean=basketBeanList.get(i);
			basketBeanMap.put(basketBean.getWorkItem(),basketBean);
		}
		return  basketBeanMap;
	}


	public static TBasketBean getBasketByItem(TWorkItemBean workItemBean,Integer personID){
		TBasketBean basketBean=basketDAO.loadBasketByItemAndPerson(workItemBean.getObjectID(),personID);
		return basketBean;
	}
	
	public static void replaceBasket(int[] workItemIDs,Integer personID,Integer basketID,Map<String,String> params,Locale locale) throws ItemOperationException {
		List<Integer> workItemIDList= GeneralUtils.createIntegerListFromIntArr(workItemIDs);
		Map<Integer,TPersonBasketBean> basketBeanMap=loadPersonBasketMapByItemIDs(workItemIDList,personID);
		String resp=null;
		String dateStr=null;
		String timeStr=null;
		String comment=null;
		Date date=null;
		if(params!=null){
			resp=params.get("responsible");
			dateStr=params.get("date");
			if(dateStr!=null) {
				timeStr=params.get("time");
				if (timeStr!=null) {
					date=DateTimeUtils.getInstance().parseGUIDateTimeNoSeconds(dateStr + " " + timeStr, locale);
				} else {
					date=DateTimeUtils.getInstance().parseGUIDate(dateStr, locale);
				}
			}
			comment=params.get("comment");
			
		}
		TPersonBasketBean personBasketBean;
		Integer workItemID;
		if(basketID==TBasketBean.BASKET_TYPES.DELEGATED){
			Integer responsibleID=null;
			try{
				responsibleID=Integer.valueOf(resp);
			}catch (Exception ex){
				responsibleID=null;
			}
			LOGGER.debug("Replace basket to delegated. ResponsibleID="+responsibleID);
			if(responsibleID==null){
				String respLabel= FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_RESPONSIBLE,locale);
				String message = LocalizeUtil.getParametrizedString("common.err.required",
						new Object[]{respLabel}, locale);
				throw  new ItemOperationException(ItemOperationException.TYPE_COMMON,message);
			}else{
				boolean confirmSave=false;
				String confirmSaveStr=null;
				if(params!=null){
					confirmSaveStr=params.get("confirmSave");
				}
				if(confirmSaveStr!=null&&confirmSaveStr.equalsIgnoreCase("true")){
					confirmSave=true;
				}
				try{
					MassOperationBL.saveExtern(workItemIDs, SystemFields.INTEGER_RESPONSIBLE, responsibleID, personID, locale,confirmSave);
				}catch (MassOperationException ex){
					throw  new ItemOperationException(ItemOperationException.TYPE_MASS_OPERATION,ex);
				}
			}
		}
		if(basketID==TBasketBean.BASKET_TYPES.CALENDAR){
			if(date==null){
				String dateLabel= LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.basketOperation.calendar.date",locale);
				String message = LocalizeUtil.getParametrizedString("common.err.required",
						new Object[]{dateLabel}, locale);
				throw  new ItemOperationException(ItemOperationException.TYPE_COMMON,message);
			}
		}
		for(int i=0;i<workItemIDList.size();i++){
			workItemID=workItemIDList.get(i);
			personBasketBean=basketBeanMap.get(workItemID);
			if(personBasketBean==null){
				personBasketBean=new TPersonBasketBean();
				personBasketBean.setWorkItem(workItemID);
				personBasketBean.setPerson(personID);
			}
			personBasketBean.setBasket(basketID);
			personBasketBean.setDelegateText(comment);
			personBasketBean.setReminderDate(date);
			personBasketDAO.save(personBasketBean);
		}
	}

	/**
	 * Loads a personBasketBeans for person for a date
	 * @param personID
	 * @param date
	 * @return
	 */
	public static List<TPersonBasketBean> loadPersonBasketItemsByDate(Integer personID, Date date) {
		return personBasketDAO.loadPersonBasketItemsByDate(personID, date);
	}
	
	/**
	 * Loads a personBasketBeans for person in a time interval
	 * @param personID
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 */
	public static List<TPersonBasketBean> loadPersonBasketItemsByTime(Integer personID, Date dateFrom, Date dateTo) {
		return personBasketDAO.loadPersonBasketItemsByTime(personID, dateFrom, dateTo);
	}
	
	public static TBasketBean getBasketByID(Integer basketID){
		return basketDAO.loadByPrimaryKey(basketID);
	}
	
	/**
	 * Loads a basketBeans by keys 
	 * @param objectID
	 * @return
	 */
	public static List<TBasketBean> loadByPrimaryKeys(Set<Integer> objectIDs) {
		return basketDAO.loadByPrimaryKeys(objectIDs);
	}
	
	public static void emptyTrash(Integer personID){
		personBasketDAO.emptyTrash(personID);
	}
	public static Integer save(TBasketBean basketBean){
		return basketDAO.save(basketBean);
	}
}
