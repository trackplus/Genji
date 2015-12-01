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


package com.aurel.track.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.SystemStateDAO;
import com.aurel.track.resources.LocalizationKeyPrefixes;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TSystemStateBean
    extends com.aurel.track.beans.base.BaseTSystemStateBean
    implements Serializable, ISerializableLabelBean, ILocalizedLabelBean
{	
	/**
	 * defines the possible entityflags
	 */
	public static class ENTITYFLAGS
	{
		public static final int PROJECTSTATE = 1;
		public static final int RELEASESTATE = 2;
		public static final int ACCOUNTSTATE = 3;
	}

	public static class STATEFLAGS
	{
		public static final int ACTIVE = 0;
		public static final int INACTIVE = 1;
		public static final int CLOSED = 2;
		public static final int NOT_PLANNED = 3;
	}

	private static final long serialVersionUID = 1L;
	private static SystemStateDAO systemStateDAO = DAOFactory.getFactory().getSystemStateDAO();
		
	
	/**
	 * Gets the possible typeflags
	 * @return
	 */
	 public int[] getPossibleTypeFlags(Integer entityFlag) {
		 int[] possibleSpecials = new int[0];
		 if (entityFlag==null) {
    		return possibleSpecials;
		 }	    	
		 switch (entityFlag.intValue()) {
		 case ENTITYFLAGS.PROJECTSTATE:
			 possibleSpecials = new int[] {STATEFLAGS.ACTIVE, 
    				STATEFLAGS.INACTIVE, STATEFLAGS.CLOSED};
    		break;
		 case ENTITYFLAGS.RELEASESTATE:    		
    		possibleSpecials = new int[] {STATEFLAGS.ACTIVE, 
    				STATEFLAGS.INACTIVE, STATEFLAGS.NOT_PLANNED, STATEFLAGS.CLOSED};    		        	
        	break;    	
		 case ENTITYFLAGS.ACCOUNTSTATE:
    		possibleSpecials = new int[] {STATEFLAGS.ACTIVE, STATEFLAGS.CLOSED};
        	break;
		 }    	
		 return possibleSpecials;
	 }
	
	@Override
	public String getKeyPrefix() {
		return LocalizationKeyPrefixes.SYSTEM_STATUS_KEY_PREFIX;
	}
	
	/**
	 * Serialize a label bean to a dom element
	 * @param labelBean
	 * @return
	 */
	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("label", getLabel());		
		Integer stateflag = getStateflag();
		if (stateflag!=null) {
			attributesMap.put("stateflag", stateflag.toString());
		}
		attributesMap.put("symbol", getSymbol());		
		Integer entityflag = getEntityflag();
		if (entityflag!=null) {
			attributesMap.put("entityflag", entityflag.toString());
		}
		Integer sortorder = getSortorder();
		if (sortorder!=null) {
			attributesMap.put("sortorder", sortorder.toString());
		}
		attributesMap.put("uuid", getUuid());
		return attributesMap;		
	}
	
	/**
	 * Deserialze the labelBean 
	 * @param attributes
	 * @return
	 */
	@Override
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TSystemStateBean systemStateBean = new TSystemStateBean();				
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			systemStateBean.setObjectID(new Integer(strObjectID));
		}
		systemStateBean.setLabel(attributes.get("label"));
		String strStateflag = attributes.get("stateflag");
		if (strStateflag!=null) {
			systemStateBean.setStateflag(new Integer(strStateflag));
		}
		systemStateBean.setSymbol(attributes.get("symbol"));
		String strEntityFlag = attributes.get("entityflag");
		if (strEntityFlag!=null) {
			systemStateBean.setEntityflag(new Integer(strEntityFlag));
		}
		String strSortorder = attributes.get("sortorder");
		if (strSortorder!=null) {
			systemStateBean.setSortorder(new Integer(strSortorder));
		}
		systemStateBean.setUuid(attributes.get("uuid"));	
		return systemStateBean;
	}
	
	/**
	 * Whether two label beans are equivalent 
	 * @param serializableLabelBean
	 * @param matchesMap	key: fieldID_paramaterCode
	 * 						value: map of already mapped external vs. internal objectIDs 
	 * @return
	 */
	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		if (serializableLabelBean==null) {
			return false;
		}				
		TSystemStateBean systemStateBean = (TSystemStateBean) serializableLabelBean;
		String externalLabel = getLabel();
		String internalLabel = systemStateBean.getLabel();
		Integer externalEntity = getEntityflag();
		Integer internalEntity = systemStateBean.getEntityflag();
		//a SystemStateBean  matches only it the entity matches and the label matches 		 
		if (externalEntity!=null && internalEntity!=null && externalLabel!=null && internalLabel!=null) {
			return externalEntity.equals(internalEntity) && externalLabel.equals(internalLabel);			
		}		
		return false;
	}
	
	/**
	 * Saves a serializableLabelBean into the database
	 * @param serializableLabelBean
	 * @param matchesMap
	 * @return
	 */
	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, 
			Map<String, Map<Integer, Integer>> matchesMap) {		
		return systemStateDAO.save((TSystemStateBean)serializableLabelBean);
	}
}
