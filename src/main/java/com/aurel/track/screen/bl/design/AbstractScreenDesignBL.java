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

package com.aurel.track.screen.bl.design;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.aurel.track.beans.screen.IField;
import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.beans.screen.IScreen;
import com.aurel.track.beans.screen.ITab;
import com.aurel.track.screen.adapterDAO.IFieldDAO;
import com.aurel.track.screen.adapterDAO.IPanelDAO;
import com.aurel.track.screen.bl.AbstractScreenBL;

/**
 * An abstract business logic for screen. 
 * Is used on design time. 
 */
public abstract class AbstractScreenDesignBL extends AbstractScreenBL {
    protected String  NAME_DB = "TSCREEN.NAME";
	protected String DESCRIPTION_DB = "TSCREEN.DESCRIPTION";
	protected String  NAME = "name";
	protected String DESCRIPTION = "description";
	protected IPanelDAO panelDAO;
	protected IFieldDAO fieldDAO;
	
    protected AbstractScreenDesignBL(){
        super();
        panelDAO = getScreenFactory().getPanelDAO();
		fieldDAO = getScreenFactory().getFieldDAO();
	}
	/**
	 * Get the screens order by the column name, ascending or descending
	 * @param orderKey
	 * @param ascending
	 * @return
	 */
	public List getScreens(String orderKey,boolean ascending){
		String orderKeyDB;
		if(orderKey==null||orderKey.equals(NAME)){
			orderKeyDB=NAME_DB;
		}else{
			orderKeyDB=DESCRIPTION_DB;
		}
		return screenDAO.loadAllOrdered(orderKeyDB,ascending);
	}

    

    /**
	 * Create and save a  new Screen
	 * @param name the name of the screen
	 * @param description the description of the screen
	 * @param personID the originator of the new screen
	 */
	public  Integer createNewScreen(String name, String tagLabel, String description,Integer personID){
		IScreen s=getScreenFactory().createIScreeenInstance();
        s.setName(name);
		s.setLabel(name);
		s.setTagLabel(tagLabel);
		s.setDescription(description);
		s.setPersonID(personID);
		Integer newPk=screenDAO.save(s);
		ITab tab=getScreenFactory().createITabInstance();
		tab.setDescription("Main tab");
		tab.setName("Main");
		tab.setLabel("Main");
		tab.setParent(newPk);
		Integer tabPk=tabDAO.save(tab);
		IPanel pan=getScreenFactory().createIPanelInstance();
		pan.setParent(tabPk);
		panelDAO.save(pan);
		return newPk;
	}
	/**
	 * Verify if a screen can be deleted
	 * @param id the identifier of the screen
	 * @return
	 */
	
    public boolean isDeletable(Integer id){
		return screenDAO.isDeletable(id);
	}
	/**
	 * Delete a screen
	 * @param id the identifier of the screen
	 */
	public boolean deleteScreen(Integer id){
		if(!screenDAO.isDeletable(id)){
			LOGGER.debug("The screen is not deletable");
			return false;
		}
		screenDAO.delete(id);
		return true;
	}
	
	/**
	 * Save the screen
	 * @param screen the screen oject to be saved
	 */
	public Integer saveScreen(IScreen screen){
		return screenDAO.save(screen);
	}
	/**
	 * Create and save a clone of the screen having given id
	 * @param screenID the identifier of the screen
	 */
	public Integer copyScreen(Integer screenID, String name,
			String description, String tagLabel, Integer userID){
		IScreen screen=loadScreen(screenID);
		IScreen clone=getScreenFactory().createIScreeenInstance();
		clone.setName(name);
		clone.setLabel(name==null?screen.getLabel()+"(1)":name);
		clone.setDescription(description==null?screen.getDescription():description);
		clone.setPersonID(userID);
		clone.setTagLabel(tagLabel);
		return saveCopyClone(screen, clone);
	}
	protected Integer saveCopyClone(IScreen screen, IScreen clone) {
		Integer newPK=saveScreen(clone);
		List<ITab> tabs=screen.getTabs();
		for (Iterator<ITab> iter = tabs.iterator(); iter.hasNext();) {
			ITab tab = iter.next();
			List<IPanel> panels= panelDAO.loadByParent(tab.getObjectID());
			tab.setObjectID(null);
			tab.setParent(newPK);
			tab.setNew(true);
			tab.setUuid(UUID.randomUUID().toString());

			Integer tabPk= tabDAO.save(tab);
			for (Iterator<IPanel> iterator = panels.iterator(); iterator.hasNext();) {
				IPanel pan = iterator.next();
				List<IField> fields= fieldDAO.loadByParent(pan.getObjectID());
				pan.setObjectID(null);
				pan.setParent(tabPk);
				pan.setNew(true);
				pan.setUuid(UUID.randomUUID().toString());
				Integer panPk= panelDAO.save(pan);
				for (Iterator<IField> iterFields = fields.iterator(); iterFields.hasNext();) {
					IField field = iterFields.next();
					field.setObjectID(null);
					field.setParent(panPk);
					field.setNew(true);
					field.setUuid(UUID.randomUUID().toString());
					fieldDAO.save(field);
				}
			}
		}
		return newPK;
	}

	/**
	 * Move a panel from an index to another index
	 * @param screenId
     * @param tabId
	 * @param newIndex
	 */
	public void moveTab(Integer screenId,Integer tabId,Integer newIndex){
		ITab tab=tabDAO.loadByPrimaryKey(tabId);
		Integer oldIndex=tab.getIndex();
		if(oldIndex.equals(newIndex)){
			return;
		}
		//panel are sorted by index
		List tabs=tabDAO.loadByParent(screenId);
		if(oldIndex.intValue()<newIndex.intValue()){
			for (int i=oldIndex.intValue()+1;i<=newIndex.intValue();i++){
				ITab aTab = (ITab) tabs.get(i);
				aTab.setIndex(new Integer(aTab.getIndex().intValue()-1));
				tabDAO.save(aTab);
			}
		}else{
			for (int i=newIndex.intValue();i<oldIndex.intValue();i++){
				ITab aTab = (ITab) tabs.get(i);
				aTab.setIndex(new Integer(aTab.getIndex().intValue()+1));
				tabDAO.save(aTab);
			}
		}
		tab.setIndex(newIndex);
		tabDAO.save(tab);
	}

	public  void setScreenProperty(IScreen screen,IScreen screenScheme){
		screen.setName(screenScheme.getName());
		screen.setDescription(screenScheme.getDescription());
		screen.setLabel(screenScheme.getLabel());
	}
	/**
	 * Add a new tab to a given screen
	 *
	 * @param screen 
	 * @param tabName name of the new tab 
	 */
	public void addTab(IScreen screen, String tabName) {
		ITab tab = getScreenFactory().createITabInstance();
		tab.setLabel(tabName);
		tab.setDescription(tabName + " description");
		tab.setParent(screen.getObjectID());
		tabDAO.save(tab);
	}
	public abstract String encodeJSON_ScreenProperies(IScreen screen);
}
