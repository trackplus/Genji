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


package com.aurel.track.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aurel.track.beans.screen.IScreen;
import com.aurel.track.beans.screen.ITab;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ScreenDAO;
import com.aurel.track.util.EqualUtils;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TScreenBean
    extends com.aurel.track.beans.base.BaseTScreenBean
    implements Serializable, ILabelBean, IScreen, ISerializableLabelBean {
	private static final long serialVersionUID = 1042880578481390292L;
	
	private static ScreenDAO screenDAO = DAOFactory.getFactory().getScreenDAO();
	
	private List<ITab> tabs;
	//if the screen can be deleted or not
	private boolean deletable;
	
	private String ownerName;

	/**
	 * @return the tabs
	 */
	public List<ITab> getTabs() {
		return tabs;
	}

	/**
	 * @param tabs the tabs to set
	 */
	public void setTabs(List<ITab> tabs) {
		this.tabs = tabs;
	}

	public Integer getPersonID() {
		return getOwner();
	}

	public void setPersonID(Integer personID) {
		setOwner(personID);
	}
	public boolean isDeletable() {
		return deletable;
	}
	public void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	/**
	 * Serialize the labelBean 
	 * @param attributes
	 * @return
	 */
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("name", getName());
		attributesMap.put("label", getLabel());
		attributesMap.put("tagLabel", getTagLabel());
		String description = getDescription();
		if (description!=null && !"".equals(description)) {
			attributesMap.put("description", description);
		}
		String tagLabel = getTagLabel();
		if (tagLabel!=null && !"".equals(tagLabel)) {
			attributesMap.put("tagLabel", tagLabel);
		}
		//the persons are by field match not yet available
		/*Integer owner = getOwner();
		if (owner!=null ) {
			attributesMap.put("owner", owner.toString());
		}	*/	
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}
	
	/**
	 * Deserialize the labelBean 
	 * @param attributes
	 * @return
	 */
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			this.setObjectID(new Integer(strObjectID));
		}		
		this.setName(attributes.get("name"));
		this.setLabel(attributes.get("label"));
		
		String tagLabel = attributes.get("tagLabel");
		if (tagLabel!=null) {
			this.setTagLabel(attributes.get("tagLabel"));
		}
		
		this.setDescription(attributes.get("description"));
		//the persons are by field match not yet available
		/*String strOwner = attributes.get("owner");
		if (strOwner!=null) {
			fieldBean.setOwner(new Integer(strOwner));
		}*/	
		this.setUuid(attributes.get("uuid"));
		return this;
	}
	
	
	
	//
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
									Map<String, Map<Integer, Integer>> matchesMap) {
		if (serializableLabelBean==null) {
			return false;
		}		
		TScreenBean internalScreenBean = (TScreenBean) serializableLabelBean;
		String externalUuid = getUuid();
		String internalUuid = internalScreenBean.getUuid();
		if(EqualUtils.equalStrict(externalUuid, internalUuid)){
			return true;
		}
		String externalName = getName();
		String internalName = internalScreenBean.getName();
		//a field matches if the name matches 		
		if (externalName!=null && internalName!=null) {
			return externalName.equals(internalName);
		}		
		return false;
	}
	
	//
	public Integer saveBean(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		TScreenBean screenBean = (TScreenBean)serializableLabelBean;
		return screenDAO.save(screenBean);
	}

	
	
	
	
}
