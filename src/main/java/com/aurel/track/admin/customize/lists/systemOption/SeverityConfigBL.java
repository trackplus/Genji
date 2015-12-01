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
import com.aurel.track.beans.TSeverityBean;
import com.aurel.track.beans.TSeverityBean.TYPEFLAGS;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.SeverityDAO;
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
public class SeverityConfigBL extends SystemOptionBaseBL {

	private static SeverityDAO severityDAO = DAOFactory.getFactory().getSeverityDAO();
	
	
	private static SeverityConfigBL instance;
	
	public static SeverityConfigBL getInstance(){
		if (instance==null){
			instance=new SeverityConfigBL();
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
		return SeverityBL.loadAll(locale);
	}
	
	
	/**
	 * Gets the specific label bean by optionID 
	 * @param optionID
	 * @return 
	 */
	@Override
	public ILocalizedLabelBean getExistingLabelBeanByOptionID(Integer optionID) {
		return SeverityBL.loadByPrimaryKey(optionID);
	}
	
	/**
	 * Gets the specific label bean by label
	 * @param listID
	 * @param label
	 * @return 
	 */
	@Override
	public List<ILabelBean> getExistingLabelBeanByLabel(Integer listID, String label) {
		return (List)SeverityBL.loadByLabel(label);
	}
	
	/**
	 * Gets the specific new label bean
	 * @param listID
	 * @return
	 */
	@Override
	public ILabelBean getNewLabelBean(Integer listID) {
		return new TSeverityBean();
	}
	
	/**
	 * Sets the label
	 * @param labelBean
	 * @return
	 */
	@Override
	public void setLabel(ILabelBean labelBean, String label) {
		TSeverityBean severityBean = (TSeverityBean)labelBean;
		severityBean.setLabel(label);
	}
	
	/**
	 * Gets the background color
	 * @param labelBean
	 * @return
	 */
	@Override
	public String getCSSStyle(ILabelBean labelBean) {
		TSeverityBean severityBean = (TSeverityBean)labelBean;
		return severityBean.getCSSStyle();
	}
	
	/**
	 * Sets the background color
	 * @param labelBean
	 * @param CSSStyle
	 * @return
	 */
	@Override
	public void setCSSStyle(ILabelBean labelBean, String CSSStyle) {
		TSeverityBean severityBean = (TSeverityBean)labelBean;
		severityBean.setCSSStyle(CSSStyle);
	}
	
	/**
	 * Gets the typeflag
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getTypeflag(ILabelBean labelBean) {
		TSeverityBean severityBean = (TSeverityBean)labelBean;
		return severityBean.getWlevel();
	}
	
	/**
	 * Sets the typeflag
	 * @param labelBean
	 * @param typeflag
	 * @return
	 */
	@Override
	public void setTypeflag(ILabelBean labelBean, Integer typeflag) {
		TSeverityBean severityBean = (TSeverityBean)labelBean;
		severityBean.setWlevel(typeflag);
	}
	
	/**
	 * Gets the possible typeflag prefix for a system field
	 * @param fieldID
	 * @return
	 */
	@Override
	public String getTypeflagPrefix(int fieldID) {
		/*return LocalizationKeyPrefixes.FIELD_SYSTEMSELECT_KEY_PREFIX + 
			SystemFields.SEVERITY + LocalizationKeyPrefixes.STATEFLAG;*/
		return "admin.customize.list.opt.severity.";
	}
	
	/**
	 * Gets the possible typeflags
	 * @param listID
	 * @param actualFlag
	 * @return
	 */
	@Override
	protected int[] getPossibleTypeFlags(Integer listID, Integer actualFlag) {
		return new int[] {TYPEFLAGS.WARNING_LEVEL1, 
				TYPEFLAGS.WARNING_LEVEL2, 
				TYPEFLAGS.WARNING_LEVEL3};
	}
	
	/**
	 * Gets the symbol
	 * @param labelBean
	 * @return
	 */
	@Override
	public String getSymbol(ILabelBean labelBean) {
		TSeverityBean severityBean = (TSeverityBean)labelBean;
		return severityBean.getSymbol();
	}
	
	/**
	 * Sets the symbol
	 * @param labelBean
	 * @param symbol
	 * @return
	 */
	@Override
	public void setSymbol(ILabelBean labelBean, String symbol) {
		TSeverityBean severityBean = (TSeverityBean)labelBean;
		severityBean.setSymbol(symbol);
	}
	
	/**
	 * Gets the iconKey
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getIconKey(ILabelBean labelBean) {
		TSeverityBean severityBean = (TSeverityBean)labelBean;
		return severityBean.getIconKey();
	}
		
	/**
	 * Sets the iconKey
	 * @param labelBean
	 * @param iconKey
	 * @return
	 */
	@Override
	public void setIconKey(ILabelBean labelBean, Integer iconKey) {
		TSeverityBean severityBean = (TSeverityBean)labelBean;
		severityBean.setIconKey(iconKey);
	}
	
	/**
	 * Sets the iconChanged flag
	 * @param labelBean
	 * @param iconChanged
	 * @return
	 */
	@Override
	public void setIconChanged(ILabelBean labelBean, boolean iconChanged) {
		TSeverityBean severityBean = (TSeverityBean)labelBean;
		severityBean.setIconChanged(BooleanFields.fromBooleanToString(iconChanged));
	}
/******************* sort order specific methods **********************/	
	
	/**
	 * Gets the sort order of the bean
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getSortOrder(ILabelBean labelBean) {
		TSeverityBean severityBean = (TSeverityBean)labelBean;
		return severityBean.getSortorder();
	}
	
	/**
	 * Gets the sort order of the bean
	 * @param labelBean
	 * @return
	 */
	@Override
	public void setSortOrder(ILabelBean labelBean, Integer sortOrder) {
		TSeverityBean severityBean = (TSeverityBean)labelBean;
		severityBean.setSortorder(sortOrder);
	}
	
	/**
	 * Gets the sort order column name 
	 * @return
	 */
	@Override
	public String getSortOrderColumn() {
		return severityDAO.getSortOrderColumn();
	}
	
	/**
	 * Gets the table the sorting is made in 
	 * @return
	 */
	@Override
	public String getTableName() {
		return severityDAO.getTableName();
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
		return SeverityBL.save(labelBean, locale);
	}
	
	/**
	 * Saves the label bean into the database 
	 * @param labelBean
	 */
	@Override
	public Integer saveSimple(ILabelBean labelBean) {
		return SeverityBL.saveSimple((TSeverityBean)labelBean);
	}
	
	/**
	 * Whether the option has dependent data
	 * @param objectID
	 * @return
	 */
	@Override
	public boolean hasDependentData(Integer objectID) {
		if (severityDAO.hasDependentData(objectID)) {
			return true;
		}		
		return FieldChangeBL.isSystemOptionInHistory(objectID, SystemFields.INTEGER_SEVERITY, true) ||
				FieldChangeBL.isSystemOptionInHistory(objectID, SystemFields.INTEGER_SEVERITY, false);
	}

	/**
	 * Deletes the severity
	 * @param objectID
	 */
	@Override
	public void delete(Integer objectID) {
		SeverityBL.delete(objectID);
		/*if (objectID!=null) {
			TSeverityBean severityBean = SeverityBL.loadByPrimaryKey(objectID);
			Integer iconKey = null;
			if (severityBean!=null) {
				iconKey = severityBean.getIconKey();
			}
			severityDAO.delete(objectID);
			if (iconKey!=null) {
				BlobBL.delete(iconKey);
			}
			//delete the severity from lucene index
			LocalizedListIndexer.getInstance().deleteByKeyAndType(objectID, 
					LuceneUtil.LOOKUPENTITYTYPES.SEVERITY);
			//remove localized resource
			LocaleEditorBL.removeLocalizedResources(new TSeverityBean().getKeyPrefix(), objectID);
		}*/
	}
	
	/** 
	 * This method replaces all occurrences of severity value oldOID with
	 * state value newOID and then deletes the severity
	 * @param oldOID
	 * @param newOID
	 */
	@Override
	public void replaceAndDelete(Integer oldOID, Integer newOID) {
		SeverityBL.replaceAndDelete(oldOID, newOID);
		/*if (newOID!=null) {
			int[] reindexWorkItems = workItemDAO.loadBySystemList(SystemFields.SEVERITY, oldOID);
			severityDAO.replace(oldOID, newOID);
			FieldChangeBL.replaceSystemOptionInHistory(oldOID, newOID, SystemFields.INTEGER_SEVERITY, true);
			FieldChangeBL.replaceSystemOptionInHistory(oldOID, newOID, SystemFields.INTEGER_SEVERITY, false);
			//reindex the affected workItems 
			LuceneIndexer.updateWorkItemIndex(reindexWorkItems, SystemFields.INTEGER_SEVERITY);
		}
		delete(oldOID);*/
	}


}
