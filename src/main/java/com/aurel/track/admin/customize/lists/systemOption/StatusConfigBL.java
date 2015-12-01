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
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TStateBean.STATEFLAGS;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.StateDAO;
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
public class StatusConfigBL extends SystemOptionBaseBL {

	private static StateDAO stateDAO = DAOFactory.getFactory().getStateDAO();
	
	
	private static StatusConfigBL instance;
	
	public static StatusConfigBL getInstance(){
		if (instance==null){
			instance=new StatusConfigBL();
		}
		return instance;
	}
	
	
	
	/**
	 * Whether the percent complete flag is defined
	 * @return
	 */
	@Override
	public boolean hasPercentComplete() {
		return true;
	}
	
	/**
	 * Gets the percent complete value
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getPercentComplete(ILabelBean labelBean) {
		if (labelBean!=null) {
			TStateBean stateBean = (TStateBean)labelBean;
			return stateBean.getPercentComplete();
		}
		return null;
	}
	
	/**
	 * Gets the percent complete value
	 * @param labelBean
	 * @param percentComplete
	 * @return
	 */
	@Override
	public void setPercentComplete(ILabelBean labelBean, Integer percentComplete) {
		TStateBean stateBean = (TStateBean)labelBean;
		stateBean.setPercentComplete(percentComplete);
	}
	
	/**
	 * Gets the list options
	 * @param locale
	 * @return
	 */
	@Override
	public List<ILabelBean> getOptions(Integer listID, Locale locale, ApplicationBean applicationBean) {
		//return LocalizeUtil.localizeDropDownList(stateDAO.loadAll(), locale);
		return StatusBL.loadAll(locale);
	}
		
	/**
	 * Gets the specific label bean by optionID 
	 * @param optionID
	 * @return 
	 */
	@Override
	public ILocalizedLabelBean getExistingLabelBeanByOptionID(Integer optionID) {
		return StatusBL.loadByPrimaryKey(optionID);
	}
	
	/**
	 * Gets the specific label bean by label
	 * @param listID
	 * @param label
	 * @return 
	 */
	@Override
	public List<ILabelBean> getExistingLabelBeanByLabel(Integer listID, String label) {
		return (List)StatusBL.loadByLabel(label);
	}
	
	/**
	 * Gets the specific new label bean
	 * @param listID
	 * @return
	 */
	@Override
	public ILabelBean getNewLabelBean(Integer listID) {
		return new TStateBean();
	}
	
	/**
	 * Sets the label
	 * @param labelBean
	 * @return
	 */
	@Override
	public void setLabel(ILabelBean labelBean, String label) {
		TStateBean stateBean = (TStateBean)labelBean;
		stateBean.setLabel(label);
	}
	
	/**
	 * Gets the background color
	 * @param labelBean
	 * @return
	 */
	@Override
	public String getCSSStyle(ILabelBean labelBean) {
		TStateBean stateBean = (TStateBean)labelBean;
		return stateBean.getCSSStyle();
	}
	
	/**
	 * Sets the background color
	 * @param labelBean
	 * @param CSSStyle
	 * @return
	 */
	@Override
	public void setCSSStyle(ILabelBean labelBean, String CSSStyle) {
		TStateBean stateBean = (TStateBean)labelBean;
		stateBean.setCSSStyle(CSSStyle);
	}
	
	/**
	 * Gets the typeflag
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getTypeflag(ILabelBean labelBean) {
		TStateBean stateBean = (TStateBean)labelBean;
		return stateBean.getStateflag();
	}
	
	/**
	 * Sets the typeflag
	 * @param labelBean
	 * @param typeflag
	 * @return
	 */
	@Override
	public void setTypeflag(ILabelBean labelBean, Integer typeflag) {
		TStateBean stateBean = (TStateBean)labelBean;
		stateBean.setStateflag(typeflag);
	}
	/**
	 * Gets the possible typeflag prefix for a system field
	 * @param fieldID
	 * @return
	 */
	@Override
	public String getTypeflagPrefix(int fieldID) {
		/*return LocalizationKeyPrefixes.FIELD_SYSTEMSELECT_KEY_PREFIX + 
			SystemFields.STATE + LocalizationKeyPrefixes.STATEFLAG;*/
		return "admin.customize.list.opt.status.";
	}
	
	/**
	 * Gets the possible typeflags
	 * @param listID
	 * @param actualFlag
	 * @return
	 */
	@Override
	protected int[] getPossibleTypeFlags(Integer listID, Integer actualFlag) {
		return new int[] {STATEFLAGS.ACTIVE,
				STATEFLAGS.INACTIVE, 
				STATEFLAGS.CLOSED,
				STATEFLAGS.DISABLED};
	}
	
	/**
	 * Gets the symbol
	 * @param labelBean
	 * @return
	 */
	@Override
	public String getSymbol(ILabelBean labelBean) {
		TStateBean stateBean = (TStateBean)labelBean;
		return stateBean.getSymbol();
	}
	
	/**
	 * Sets the symbol
	 * @param labelBean
	 * @param symbol
	 * @return
	 */
	@Override
	public void setSymbol(ILabelBean labelBean, String symbol) {
		TStateBean stateBean = (TStateBean)labelBean;
		stateBean.setSymbol(symbol);
	}		
	
	/**
	 * Gets the label beans with a certain symbol
	 * @param listID
	 * @param symbol
	 * @param excludeObjectID
	 * @return
	 */
	/*public List<ILabelBean> getLabelBeansBySymbol(Integer listID, String symbol, Integer excludeObjectID) {
		return (List)stateDAO.loadBySymbol(listID, symbol, excludeObjectID);
	}*/
	
	/**
	 * Gets the iconKey
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getIconKey(ILabelBean labelBean) {
		TStateBean stateBean = (TStateBean)labelBean;
		return stateBean.getIconKey();
	}
		
	/**
	 * Sets the iconKey
	 * @param labelBean
	 * @param iconKey
	 * @return
	 */
	@Override
	public void setIconKey(ILabelBean labelBean, Integer iconKey) {
		TStateBean stateBean = (TStateBean)labelBean;
		stateBean.setIconKey(iconKey);
	}
	
	/**
	 * Sets the iconChanged flag
	 * @param labelBean
	 * @param iconChanged
	 * @return
	 */
	@Override
	public void setIconChanged(ILabelBean labelBean, boolean iconChanged) {
		TStateBean stateBean = (TStateBean)labelBean;
		stateBean.setIconChanged(BooleanFields.fromBooleanToString(iconChanged));
	}
/******************* sort order specific methods **********************/	
	
	/**
	 * Gets the sort order of the bean
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getSortOrder(ILabelBean labelBean) {
		TStateBean stateBean = (TStateBean)labelBean;
		return stateBean.getSortorder();
	}
	
	/**
	 * Gets the sort order of the bean
	 * @param labelBean
	 * @return
	 */
	@Override
	public void setSortOrder(ILabelBean labelBean, Integer sortOrder) {
		TStateBean stateBean = (TStateBean)labelBean;
		stateBean.setSortorder(sortOrder);
	}
	
	/**
	 * Gets the sort order column name 
	 * @return
	 */
	@Override
	public String getSortOrderColumn() {
		return stateDAO.getSortOrderColumn();
	}
	
	/**
	 * Gets the table the sorting is made in 
	 * @return
	 */
	@Override
	public String getTableName() {
		return stateDAO.getTableName();
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
		return StatusBL.save(labelBean, locale);
	}
	
	/**
	 * Saves the label bean into the database 
	 * @param labelBean
	 */
	@Override
	public Integer saveSimple(ILabelBean labelBean) {
		return StatusBL.saveSimple((TStateBean)labelBean);
	}
	
	/**
	 * Whether the option has dependent data
	 * @param objectID
	 * @return
	 */
	@Override
	public boolean hasDependentData(Integer objectID) {
		if (stateDAO.hasDependentData(objectID)) {
			return true;
		}
		return FieldChangeBL.isSystemOptionInHistory(objectID, SystemFields.INTEGER_STATE, true) ||
				FieldChangeBL.isSystemOptionInHistory(objectID, SystemFields.INTEGER_STATE, false);
	}
	
	/**
	 * Deletes the status
	 * @param objectID
	 */
	@Override
	public void delete(Integer objectID) {
		StatusBL.delete(objectID);
		/*if (objectID!=null) {
			TStateBean stateBean = stateDAO.loadByPrimaryKey(objectID);
			Integer iconKey = null;
			if (stateBean!=null) {
				iconKey = stateBean.getIconKey();
			}
			stateDAO.delete(objectID);
			if (iconKey!=null) {
				BlobBL.delete(iconKey);
			}
			//delete the state from lucene index
			LocalizedListIndexer.getInstance().deleteByKeyAndType(objectID, 
					LuceneUtil.LOOKUPENTITYTYPES.STATE);
			//remove localized resource
			LocaleEditorBL.removeLocalizedResources(new TStateBean().getKeyPrefix(), objectID);
		}*/
	}
	
	/** 
	 * This method replaces all occurrences of state value oldOID with
	 * state value newOID and then deletes the state
	 * @param oldOID
	 * @param newOID
	 */
	@Override
	public void replaceAndDelete(Integer oldOID, Integer newOID) {
		StatusBL.replaceAndDelete(oldOID, newOID);
		/*if (newOID!=null) {
			int[] reindexWorkItems = workItemDAO.loadBySystemList(SystemFields.STATE, oldOID);						
			stateDAO.replace(oldOID, newOID);
			FieldChangeBL.replaceSystemOptionInHistory(oldOID, newOID, SystemFields.INTEGER_STATE, true);
			FieldChangeBL.replaceSystemOptionInHistory(oldOID, newOID, SystemFields.INTEGER_STATE, false);						
			//reindex the affected workItems 
			LuceneIndexer.updateWorkItemIndex(reindexWorkItems, SystemFields.INTEGER_STATE);
		}
		delete(oldOID);*/
	}
	
	

}
