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

package com.aurel.track.item;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TScreenBean;
import com.aurel.track.beans.screen.ITab;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ScreenDAO;
import com.aurel.track.screen.item.bl.runtime.ScreenPanelRuntimeBL;

/**
 *Used to cache the item screens
 */
public class ItemScreenCache {
	private static final Logger LOGGER = LogManager.getLogger(ItemScreenCache.class);
	private static ScreenDAO screenDAO= DAOFactory.getFactory().getScreenDAO();
	private static ItemScreenCache instance;
	private Map<Integer,TScreenBean> cache;
	private ItemScreenCache(){
		cache=new HashMap<Integer, TScreenBean>();
	}
	public static  ItemScreenCache getInstance(){
		if(instance==null){
			instance=new ItemScreenCache();
		}
		return instance;
	}
	public TScreenBean getScreen(Integer screenID){
		TScreenBean screenBean=cache.get(screenID);
		if(screenBean==null){
			screenBean=loadFullRuntimeScreenBean(screenID);
			cache.put(screenID,screenBean);
			LOGGER.debug("Screen:"+screenID+" loaded from DB");
		}else{
			LOGGER.debug("Screen:"+screenID+" loaded from cache");
		}
		return screenBean;
	}
	private TScreenBean loadFullRuntimeScreenBean(Integer screenID){
		TScreenBean screenBeanDb=screenDAO.loadFullByPrimaryKey(screenID);
		ScreenPanelRuntimeBL screenPanelRuntimeBL=ScreenPanelRuntimeBL.getInstance();
		List<ITab> tabs= screenBeanDb.getTabs();
		if(tabs!=null){
			for (Iterator<ITab> iterator = tabs.iterator(); iterator.hasNext();) {
				ITab tab = iterator.next();
				screenPanelRuntimeBL.calculateFieldWrappers(tab);
			}
		}
		return screenBeanDb;
	}
	
	public void clearCache(){
		this.cache.clear();
	}
	public void removeScreen(Integer screenID){
		LOGGER.debug("removeScreen: screenID="+screenID);
		this.cache.remove(screenID);
	}
}
