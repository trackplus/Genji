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

package com.aurel.track.persist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TLinkTypeBean;
import com.aurel.track.dao.LinkTypeDAO;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.util.GeneralUtils;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TLinkTypePeer
	extends com.aurel.track.persist.BaseTLinkTypePeer
	implements LinkTypeDAO{
	private static final Logger LOGGER = LogManager.getLogger(LinkTypeDAO.class);
	
	public static final long serialVersionUID = 400L;
	
	private static Class[] replacePeerClasses = {
		TWorkItemLinkPeer.class
	};
	
	private static String[] replaceFields = {
		TWorkItemLinkPeer.LINKTYPE
	};
	
	private static Class[] deletePeerClasses = {
	   //TWorkItemLinkPeer.class,
		TLinkTypePeer.class
	};
	
	private static String[] deleteFields = {
		//TWorkItemLinkPeer.LINKTYPE,
		TLinkTypePeer.OBJECTID
	};
	
	/**
	 * Loads a linkTypeBean by primary key
	 * @param objectID
	 * @return 
	 */
	public TLinkTypeBean loadByPrimaryKey(Integer objectID) {
		TLinkType tLinkType = null;
		try {
			tLinkType = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.info("Loading of a linkType by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
		if (tLinkType!=null) {
			return tLinkType.getBean();
		}
		return null;
	}
	
	/**
	 * Whether exist links of a type
	 * @param linkTypeID
	 * @return 
	 */
	public boolean hasLinksOfType(Integer linkTypeID) {
		return ReflectionHelper.hasDependentData(replacePeerClasses, replaceFields, linkTypeID);
	}
	
	/**
	 * Load all link types of a specific type
	 * @param linkTypeClass
	 * @param exceptLinkTypeID
	 * @return
	 */
	public List<TLinkTypeBean> loadByLinkType(String linkTypeClass, Integer exceptLinkTypeID) {
		Criteria crit = new Criteria();
		if (exceptLinkTypeID!=null) {
			crit.add(OBJECTID, exceptLinkTypeID, Criteria.NOT_EQUAL);
		}
		crit.add(LINKTYPEPLUGIN, linkTypeClass);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		}
		catch(Exception e) {
			LOGGER.error("Loading all linkTypes of type " + linkTypeClass + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads all linkTypeBeans
	 * @return 
	 */
	public List<TLinkTypeBean> loadAll() {
		Criteria crit = new Criteria();
		crit.addAscendingOrderByColumn(NAME);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		}
		catch(Exception e) {
			LOGGER.error("Loading all linkTypes failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets the list of TWorkItemLinkBean by linkIDsList
	 * @param linkTypeIDsList
	 * @return
	 */
	public List<TLinkTypeBean> loadByLinkIDs(List<Integer> linkTypeIDsList) {
		List<TLinkTypeBean> list = new ArrayList<TLinkTypeBean>();
		if (linkTypeIDsList==null || linkTypeIDsList.isEmpty()) {
			return list;
		}
		Criteria criteria;
		List<int[]> fieldConfigIDChunksList = GeneralUtils.getListOfChunks(linkTypeIDsList);
		if (fieldConfigIDChunksList==null) {
			return list;
		}
		list = new ArrayList<TLinkTypeBean>();
		Iterator<int[]> iterator = fieldConfigIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] linkIDChunk = iterator.next();
			criteria = new Criteria();
			criteria.addIn(OBJECTID, linkIDChunk);
			try {
				list.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
				LOGGER.error("Loading the linktypes for " + linkIDChunk.length +  " failed with " + e.getMessage(), e);}
		}
		return list;
	}
	
	/**
	 * Saves a new/existing linkTypeBean
	 * @param linkTypeBean
	 * @return the created optionID
	 */
	public Integer save(TLinkTypeBean linkTypeBean) {
		try {
			TLinkType tLinkType = BaseTLinkType.createTLinkType(linkTypeBean);
			tLinkType.save();
			return tLinkType.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a linkType failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Deletes a linkTypeBean
	 * @param objectID
	 */
	public void delete(Integer objectID) {
		ReflectionHelper.delete(replacePeerClasses, replaceFields, objectID);
		deleteLinkType(objectID);
	}
		
	/**
	 * Replaces the dependences with a new linkTypeID and 
	 * deletes the old linkTypeID from the TLinkType table 
	 * @param oldLinkTypeID
	 * @param newLinkTypeID
	 */
	public void replaceAndDelete(Integer oldLinkTypeID, Integer newLinkTypeID) {
		ReflectionHelper.replace(replacePeerClasses, replaceFields, oldLinkTypeID, newLinkTypeID);
		deleteLinkType(oldLinkTypeID);
	}
	
	/**
	 * Deletes a linkTypeBean
	 * @param objectID
	 */
	private void deleteLinkType(Integer objectID) {
		TLinkTypeBean linkTypeBean = loadByPrimaryKey(objectID);
		ReflectionHelper.delete(deletePeerClasses, deleteFields, objectID);
		if (linkTypeBean!=null) {
			Integer outwardIconKey = linkTypeBean.getOutwardIconKey();
			if (outwardIconKey!=null) {
				new TBLOBPeer().delete(outwardIconKey);
			}
			Integer inwardIconKey = linkTypeBean.getInwardIconKey();
			if (inwardIconKey!=null) {
				new TBLOBPeer().delete(inwardIconKey);
			}
			new TLocalizedResourcesPeer().deleteLocalizedResourcesForFieldNameLikeAndKey(
					LocalizationKeyPrefixes.LINKTYPE_NAME+"*", objectID);
			new TLocalizedResourcesPeer().deleteLocalizedResourcesForFieldNameLikeAndKey(
					LocalizationKeyPrefixes.LINKTYPE_SUPERSET+"*", objectID);
		}
	}
	
	private static List<TLinkTypeBean> convertTorqueListToBeanList(List<TLinkType> torqueList) {
		List<TLinkTypeBean> beanList = new LinkedList<TLinkTypeBean>();
		if (torqueList!=null){
			for (TLinkType tLinkType : torqueList) {
				beanList.add(tLinkType.getBean());
			}
		}
		return beanList;
	}
}
