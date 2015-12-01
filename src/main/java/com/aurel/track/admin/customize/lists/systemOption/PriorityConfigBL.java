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


package com.aurel.track.admin.customize.lists.systemOption;

import java.util.List;
import java.util.Locale;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ILocalizedLabelBean;
import com.aurel.track.beans.TPriorityBean;
import com.aurel.track.beans.TPriorityBean.TYPEFLAGS;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.PriorityDAO;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.item.history.FieldChangeBL;
import com.aurel.track.prop.ApplicationBean;

/**
 * Base class for configuring the lists
 * Contains the methods for configuring the list entries (insert, update, delete etc.)
 * This class abstracts the list type i.e. these methods have the same signature 
 * independent of the select type: simple select, tree select, cascading select
 * @author Tamas Ruff
 *
 */
public class PriorityConfigBL extends SystemOptionBaseBL {

	private static PriorityDAO priorityDAO = DAOFactory.getFactory().getPriorityDAO();
	
	
	private static PriorityConfigBL instance;
	
	public static PriorityConfigBL getInstance(){
		if (instance==null){
			 instance=new PriorityConfigBL();
		}
		return instance;
	}
	
	/**
	 * Gets the list options
	 * @param locale
	 * @return
	 */
	@Override
	public List<ILabelBean> getOptions(Integer listID, Locale locale, ApplicationBean applicationBean) {
		return PriorityBL.loadAll(locale);
	}
	
	
		
	/**
	 * Gets the specific label bean by optionID 
	 * @param optionID
	 * @return 
	 */
	@Override
	public ILocalizedLabelBean getExistingLabelBeanByOptionID(Integer optionID) {
		return PriorityBL.loadByPrimaryKey(optionID);
	}
	
	/**
	 * Gets the specific label bean by label
	 * @param listID
	 * @param label
	 * @return 
	 */
	@Override
	public List<ILabelBean> getExistingLabelBeanByLabel(Integer listID, String label) {
		return (List)PriorityBL.loadByLabel(label);
	}
	
	/**
	 * Gets the specific new label bean
	 * @param listID
	 * @return
	 */
	@Override
	public ILabelBean getNewLabelBean(Integer listID) {
		return new TPriorityBean();
	}
	
	/**
	 * Sets the label
	 * @param labelBean
	 * @return
	 */
	@Override
	public void setLabel(ILabelBean labelBean, String label) {
		TPriorityBean priorityBean = (TPriorityBean)labelBean;
		priorityBean.setLabel(label);
	}
	
	/**
	 * Gets the background color
	 * @param labelBean
	 * @return
	 */
	@Override
	public String getCSSStyle(ILabelBean labelBean) {
		TPriorityBean priorityBean = (TPriorityBean)labelBean;
		return priorityBean.getCSSStyle();
	}
	
	/**
	 * Sets the background color
	 * @param labelBean
	 * @param CSSStyle
	 * @return
	 */
	@Override
	public void setCSSStyle(ILabelBean labelBean, String CSSStyle) {
		TPriorityBean priorityBean = (TPriorityBean)labelBean;
		priorityBean.setCSSStyle(CSSStyle);
	}
	
	/**
	 * Gets the typeflag
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getTypeflag(ILabelBean labelBean) {
		TPriorityBean priorityBean = (TPriorityBean)labelBean;
		return priorityBean.getWlevel();
	}
	
	/**
	 * Sets the typeflag
	 * @param labelBean
	 * @param typeflag
	 * @return
	 */
	@Override
	public void setTypeflag(ILabelBean labelBean, Integer typeflag) {
		TPriorityBean priorityBean = (TPriorityBean)labelBean;
		priorityBean.setWlevel(typeflag);
	}
	
	/**
	 * Gets the possible typeflag prefix for a system field
	 * @param fieldID
	 * @return
	 */
	@Override
	public String getTypeflagPrefix(int fieldID) {
		/*return LocalizationKeyPrefixes.FIELD_SYSTEMSELECT_KEY_PREFIX + 
			SystemFields.PRIORITY + LocalizationKeyPrefixes.STATEFLAG;*/
		return "admin.customize.list.opt.priority.";
	}
	
	/**
	 * Gets the possible typeflags
	 * @param listID
	 * @param actualFlag
	 * @return
	 */
	@Override
	protected int[] getPossibleTypeFlags(Integer listID, Integer actualFlag) {
		return new int[] {TYPEFLAGS.LEVEL1, 
				TYPEFLAGS.LEVEL2, 
				TYPEFLAGS.LEVEL3, 
				TYPEFLAGS.LEVEL4};
	}
	
	/**
	 * Gets the symbol
	 * @param labelBean
	 * @return
	 */
	@Override
	public String getSymbol(ILabelBean labelBean) {
		TPriorityBean priorityBean = (TPriorityBean)labelBean;
		return priorityBean.getSymbol();
	}
	
	/**
	 * Sets the symbol
	 * @param labelBean
	 * @param symbol
	 * @return
	 */
	@Override
	public void setSymbol(ILabelBean labelBean, String symbol) {
		TPriorityBean priorityBean = (TPriorityBean)labelBean;
		priorityBean.setSymbol(symbol);
	}
	
	
	/**
	 * Gets the iconKey
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getIconKey(ILabelBean labelBean) {
		TPriorityBean priorityBean = (TPriorityBean)labelBean;
		return priorityBean.getIconKey();
	}
		
	/**
	 * Sets the iconKey
	 * @param labelBean
	 * @param iconKey
	 * @return
	 */
	@Override
	public void setIconKey(ILabelBean labelBean, Integer iconKey) {
		TPriorityBean priorityBean = (TPriorityBean)labelBean;
		priorityBean.setIconKey(iconKey);
	}
	
	/**
	 * Sets the iconChanged flag
	 * @param labelBean
	 * @param iconChanged
	 * @return
	 */
	@Override
	public void setIconChanged(ILabelBean labelBean, boolean iconChanged) {
		TPriorityBean priorityBean = (TPriorityBean)labelBean;
		priorityBean.setIconChanged(BooleanFields.fromBooleanToString(iconChanged));
	}
/******************* sort order specific methods **********************/	
	
	/**
	 * Gets the sort order of the bean
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getSortOrder(ILabelBean labelBean) {
		TPriorityBean priorityBean = (TPriorityBean)labelBean;
		return priorityBean.getSortorder();
	}
	
	/**
	 * Gets the sort order of the bean
	 * @param labelBean
	 * @return
	 */
	@Override
	public void setSortOrder(ILabelBean labelBean, Integer sortOrder) {
		TPriorityBean priorityBean = (TPriorityBean)labelBean;
		priorityBean.setSortorder(sortOrder);
	}
	
	/**
	 * Gets the sort order column name 
	 * @return
	 */
	@Override
	public String getSortOrderColumn() {
		return priorityDAO.getSortOrderColumn();
	}
	
	/**
	 * Gets the table the sorting is made in 
	 * @return
	 */
	@Override
	public String getTableName() {
		return priorityDAO.getTableName();
	}
	
	/**
	 * Saves a new/modified list entry
	 * If sortOrder is not set it will be set to be the next available sortOrder
	 * @param labelBean
	 * @param copy
	 * @param personID
	 * @param locale
	 * @return
	 */
	@Override
	public Integer save(ILabelBean labelBean, boolean copy, Integer personID, Locale locale) {
		return PriorityBL.save(labelBean, locale);
	}	
	
	/**
	 * Saves the label bean into the database 
	 * @param labelBean
	 */
	@Override
	public Integer saveSimple(ILabelBean labelBean) {
		return PriorityBL.saveSimple((TPriorityBean)labelBean);
	}
	
	/**
	 * Whether the option has dependent data
	 * @param objectID
	 * @return
	 */
	@Override
	public boolean hasDependentData(Integer objectID) {
		if (priorityDAO.hasDependentData(objectID)) {
			return true;
		}		
		return FieldChangeBL.isSystemOptionInHistory(objectID, SystemFields.INTEGER_PRIORITY, true) ||
				FieldChangeBL.isSystemOptionInHistory(objectID, SystemFields.INTEGER_PRIORITY, false);
	}
	
	/**
	 * Deletes the priority
	 * @param objectID
	 */
	@Override
	public void delete(Integer objectID) {
		PriorityBL.delete(objectID);
	}
	
	/** 
	 * This method replaces all occurrences of oldOID priority with
	 * newOID priority and then deletes the priority
	 * @param oldOID
	 * @param newOID
	 */
	@Override
	public void replaceAndDelete(Integer oldOID, Integer newOID) {
		PriorityBL.replaceAndDelete(oldOID, newOID);
		/*if (newOID!=null) {
			int[] reindexWorkItems = workItemDAO.loadBySystemList(SystemFields.PRIORITY, oldOID);
			priorityDAO.replace(oldOID, newOID);
			FieldChangeBL.replaceSystemOptionInHistory(oldOID, newOID, SystemFields.INTEGER_PRIORITY, true);
			FieldChangeBL.replaceSystemOptionInHistory(oldOID, newOID, SystemFields.INTEGER_PRIORITY, false);
			//reindex the affected workItems 
			LuceneIndexer.updateWorkItemIndex(reindexWorkItems, SystemFields.INTEGER_PRIORITY);
		}
		delete(oldOID);*/
	}
	
	
}
