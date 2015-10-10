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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.beans.screen.ITab;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ScreenTabDAO;
import com.aurel.track.exchange.track.ExchangeFieldNames;
import com.aurel.track.util.EqualUtils;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TScreenTabBean
    extends com.aurel.track.beans.base.BaseTScreenTabBean
    implements Serializable, ITab, ISerializableLabelBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8369548353898699655L;
	private List<IPanel> panels;
	private static ScreenTabDAO screenTabDAO = DAOFactory.getFactory().getScreenTabDAO();	
	public List<IPanel> getPanels() {
		return panels;
	}

	public void setPanels(List<IPanel> panels) {
		this.panels = panels;
	}
	public ITab cloneMe(){
		TScreenTabBean tabBean=new TScreenTabBean();
		tabBean.setDescription(this.getDescription());
		tabBean.setIndex(this.getIndex());
		tabBean.setLabel(this.getLabel());
		tabBean.setName(this.getName());
		tabBean.setParent(this.getParent());

		List<IPanel> panelsClone=new ArrayList<IPanel>();
		List<IPanel> panels=this.getPanels();
		if(panels!=null){
			for(IPanel p:panels){
				panelsClone.add(p.cloneMe());
			}
		}
		tabBean.setPanels(panelsClone);

		return tabBean;
	}
	
		
	/**A
	 * Serialize the labelBean 
	 * @return
	 */
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("name", getName());
		attributesMap.put("label", getLabel());		
		attributesMap.put("index", getIndex().toString());
		attributesMap.put("parent", getParent().toString());	
		
		String description = getDescription();
		if (description!=null && !"".equals(description)) {
			attributesMap.put("description", description);
		}
		attributesMap.put("uuid", getUuid());
		return attributesMap;				
	}
	
	/**A
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
		this.setIndex(Integer.parseInt(attributes.get("index")));
		this.setParent(Integer.parseInt(attributes.get("parent")));
		this.setDescription(attributes.get("description"));
		
		this.setUuid(attributes.get("uuid"));
		return this;
	}
	
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		if (serializableLabelBean==null) {
			return false;
		}		
		
		TScreenTabBean internalScreenTabBean = (TScreenTabBean) serializableLabelBean;
		String externalUuid = getUuid();
		String internalUuid = internalScreenTabBean.getUuid();
		if(EqualUtils.equalStrict(externalUuid, internalUuid)){
			return true;
		}
		
		/*String externalName = getName();
		String internalName = internalScreenTabBean.getName();	
		Integer externalParent = getParent();
		Integer internalParent = internalScreenTabBean.getParent();
		Map<Integer, Integer> parentMatches = matchesMap.get(ExchangeFieldNames.SCREEN);
		
		//a field matches if the name and the parent matches 		
		if (externalName!=null && internalName!=null && externalParent!=null &&
				internalParent!=null && parentMatches!=null && parentMatches.get(externalParent)!=null) {
			return externalName.equals(internalName) && 
					parentMatches.get(externalParent).equals(internalParent);			
		}*/
		return false;
	}
	
	
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, 
			Map<String, Map<Integer, Integer>> matchesMap) {		
		TScreenTabBean screenTabBean = (TScreenTabBean)serializableLabelBean;		
		Integer externalParent = screenTabBean.getParent();
		Map<Integer, Integer> parentMatches = 
				matchesMap.get(ExchangeFieldNames.SCREEN);
		if (externalParent!=null && parentMatches.get(externalParent)!=null) {
			
			screenTabBean.setParent(parentMatches.get(externalParent));
		}
		return screenTabDAO.save(screenTabBean);
	}
	
	/**
	 * This attribute is used with Dashboard tabs;
	 */
	public void setFieldTypes(HashSet<String>fieldTypes) {		
	}
	/**
	 * This attribute is used with Dashboard tabs;
	 */
	public Set<String> getFieldTypes() {
		return null;
	}
	
	
   
}
