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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.lists.ChildIssueTypeAssignmentsBL;
import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.customize.lists.ListBL.RESOURCE_TYPES;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ILocalizedLabelBean;
import com.aurel.track.beans.TChildIssueTypeBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TListTypeBean.TYPEFLAGS;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.IssueTypeDAO;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.item.history.FieldChangeBL;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;

/**
 * Base class for configuring the lists
 * Contains the methods for configuring the list entries (insert, update, delete etc.)
 * This class abstracts the list type i.e. these methods have the same signature 
 * independent of the select type: simple select, tree select, cascading select
 * @author Tamas Ruff
 *
 */
public class IssueTypeConfigBL extends SystemOptionBaseBL {

	private static IssueTypeDAO issueTypeDAO = DAOFactory.getFactory().getIssueTypeDAO();
	
	private static IssueTypeConfigBL instance;
	
	public static IssueTypeConfigBL getInstance(){
		if (instance==null){
			instance=new IssueTypeConfigBL();
		}
		return instance;
	}
	
	/**
	 * Whether the label bean is deletable
	 * @param labelBean
	 * @return
	 */
	@Override
	public boolean isDeletable(ILabelBean labelBean) {
		TListTypeBean listTypeBean = (TListTypeBean)labelBean;
		if (listTypeBean.getObjectID().intValue()<=0 &&
				listTypeBean.getTypeflag()!=TListTypeBean.TYPEFLAGS.SPRINT) {
			//task, meeting are not deletable
			//allow sprint to be deleted from older track+ versions 
			return false;
		}
		return true;
	}
	
	/**
	 * Check if it is permitted to add a new item type
	 * @param applicationBean
	 * @param locale
	 * @return
	 */
	public boolean addIssueTypeAllowed(ApplicationBean applicationBean, Locale locale) {
		if (applicationBean.getLicenseManager() != null) {
			return applicationBean.getLicenseManager().getFeatureDetailChecker("getAddIssueTypeAllowed");
		}
		return true; 
	}
	
	
	/**
	 * Gets the list options
	 * @param locale
	 * @return
	 */
	@Override
	public List<ILabelBean> getOptions(Integer listID, Locale locale, ApplicationBean applicationBean) {
		return (List)IssueTypeBL.loadAll(locale);
	}
		
	
	/**
	 * Gets the specific label bean by optionID 
	 * @param optionID
	 * @return 
	 */
	@Override
	public ILocalizedLabelBean getExistingLabelBeanByOptionID(Integer optionID) {
		return IssueTypeBL.loadByPrimaryKey(optionID);
	}
	
	/**
	 * Gets the specific label bean by label
	 * @param listID
	 * @param label
	 * @return 
	 */
	@Override
	public List<ILabelBean> getExistingLabelBeanByLabel(Integer listID, String label) {
		return (List)issueTypeDAO.loadByLabel(label);
	}
	
	/**
	 * Gets the specific new label bean
	 * @param listID
	 * @return
	 */
	@Override
	public ILabelBean getNewLabelBean(Integer listID) {
		return new TListTypeBean();
	}
	
	/**
	 * Sets the label
	 * @param labelBean
	 * @return
	 */
	@Override
	public void setLabel(ILabelBean labelBean, String label) {
		TListTypeBean listTypeBean = (TListTypeBean)labelBean;
		listTypeBean.setLabel(label);
	}
	
	/**
	 * Gets the background color
	 * @param labelBean
	 * @return
	 */
	@Override
	public String getCSSStyle(ILabelBean labelBean) {
		TListTypeBean listTypeBean = (TListTypeBean)labelBean;
		return listTypeBean.getCSSStyle();
	}
	
	/**
	 * Sets the background color
	 * @param labelBean
	 * @param CSSStyle
	 * @return
	 */
	@Override
	public void setCSSStyle(ILabelBean labelBean, String CSSStyle) {
		TListTypeBean listTypeBean = (TListTypeBean)labelBean;
		listTypeBean.setCSSStyle(CSSStyle);
	}
	
	/**
	 * Gets the typeflag
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getTypeflag(ILabelBean labelBean) {
		TListTypeBean listTypeBean = (TListTypeBean)labelBean;
		return listTypeBean.getTypeflag();
	}
	
	/**
	 * Sets the typeflag
	 * @param labelBean
	 * @param typeflag
	 * @return
	 */
	@Override
	public void setTypeflag(ILabelBean labelBean, Integer typeflag) {
		TListTypeBean listTypeBean = (TListTypeBean)labelBean;
		listTypeBean.setTypeflag(typeflag);
	}
	
	/**
	 * Gets the possible typeflag prefix for a system field
	 * @param fieldID
	 * @return
	 */
	@Override
	public String getTypeflagPrefix(int fieldID) {
		return "admin.customize.list.opt.issueType.";
	}
	
	/**
	 * Gets the possible typeflags
	 * @param listID
	 * @param add
	 * @return
	 */
	@Override
	protected int[] getPossibleTypeFlags(Integer listID, Integer actualFlag) {
		if (actualFlag==null) {
			return new int[] {
					TYPEFLAGS.GENERAL
					};
		} else {
			switch(actualFlag.intValue()) {
			case TYPEFLAGS.GENERAL:
			case TYPEFLAGS.DOCUMENT:
			case TYPEFLAGS.DOCUMENT_SECTION:
				return  new int[] {
						TYPEFLAGS.GENERAL
						};
			default: return new int[] {actualFlag};
			}
		}
	}	
	
	/**
	 * Gets the symbol
	 * @param labelBean
	 * @return
	 */
	@Override
	public String getSymbol(ILabelBean labelBean) {
		TListTypeBean listTypeBean = (TListTypeBean)labelBean;
		return listTypeBean.getSymbol();
	}
	
	/**
	 * Sets the symbol
	 * @param labelBean
	 * @param symbol
	 * @return
	 */
	@Override
	public void setSymbol(ILabelBean labelBean, String symbol) {
		TListTypeBean listTypeBean = (TListTypeBean)labelBean;
		listTypeBean.setSymbol(symbol);
	}	
	
	/**
	 * Gets the iconKey
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getIconKey(ILabelBean labelBean) {
		TListTypeBean listTypeBean = (TListTypeBean)labelBean;
		return listTypeBean.getIconKey();
	}
		
	/**
	 * Sets the iconKey
	 * @param labelBean
	 * @param iconKey
	 * @return
	 */
	@Override
	public void setIconKey(ILabelBean labelBean, Integer iconKey) {
		TListTypeBean listTypeBean = (TListTypeBean)labelBean;
		listTypeBean.setIconKey(iconKey);
	}
	
	/**
	 * Sets the iconChanged flag
	 * @param labelBean
	 * @param iconChanged
	 * @return
	 */
	@Override
	public void setIconChanged(ILabelBean labelBean, boolean iconChanged) {
		TListTypeBean listTypeBean = (TListTypeBean)labelBean;
		listTypeBean.setIconChanged(BooleanFields.fromBooleanToString(iconChanged));
	}
	
	
	/**
	 * Gets the sort order of the bean
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getSortOrder(ILabelBean labelBean) {
		TListTypeBean listTypeBean = (TListTypeBean)labelBean;
		return listTypeBean.getSortorder();
	}
	
	/**
	 * Gets the sort order of the bean
	 * @param labelBean
	 * @return
	 */
	@Override
	public void setSortOrder(ILabelBean labelBean, Integer sortOrder) {
		TListTypeBean listTypeBean = (TListTypeBean)labelBean;
		listTypeBean.setSortorder(sortOrder);
	}
	
	/**
	 * Gets the sort order column name 
	 * @return
	 */
	@Override
	public String getSortOrderColumn() {
		return issueTypeDAO.getSortOrderColumn();
	}
	
	/**
	 * Gets the table the sorting is made in 
	 * @return
	 */
	@Override
	public String getTableName() {
		return issueTypeDAO.getTableName();
	}
	
	/**
	 * Saves the label bean into the database 
	 * @param labelBean
	 */
	@Override
	public Integer saveSimple(ILabelBean labelBean) {
		return IssueTypeBL.saveSimple((TListTypeBean)labelBean);
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
		return IssueTypeBL.save(labelBean, locale);
	}
	
	/**
	 * Whether the option has dependent data
	 * @param objectID
	 * @return
	 */
	@Override
	public boolean hasDependentData(Integer objectID) {
		if (issueTypeDAO.hasDependentData(objectID)) {
			return true;
		}		
		return FieldChangeBL.isSystemOptionInHistory(objectID, SystemFields.INTEGER_ISSUETYPE, true) ||
				FieldChangeBL.isSystemOptionInHistory(objectID, SystemFields.INTEGER_ISSUETYPE, false);
		
	}
	
	/**
	 * Deletes the issue type
	 * @param objectID
	 */
	@Override
	public void delete(Integer objectID) {
		IssueTypeBL.delete(objectID);
	}
	
	/** 
	 * This method replaces all occurrences of issue type value oldOID with
	 * state value newOID and then deletes the issue type
	 * @param oldOID
	 * @param newOID
	 */
	@Override
	public void replaceAndDelete(Integer oldOID, Integer newOID) {
		IssueTypeBL.replaceAndDelete(oldOID, newOID);
	}
	
	/**
	 * Loads the filter child assignments for issue type
	 * @param locale
	 * @param applicationBean
	 * @return
	 */
	public Map<Integer, Map<Integer, Boolean>> loadExistingChildAssignments(Locale locale, ApplicationBean applicationBean) {
		Map<Integer, Map<Integer, Boolean>> issueTypeToIssueTypeByProjectTypeMap = new HashMap<Integer, Map<Integer,Boolean>>();
		List<ILabelBean> issueTypeBeans = getOptions(ListBL.RESOURCE_TYPES.ISSUETYPE, locale, applicationBean);
		for (ILabelBean parentIssueTypeBean : issueTypeBeans) {
			Integer parentIssueTypeID = parentIssueTypeBean.getObjectID();
			Map<Integer, Boolean> issueTypesForRow = new HashMap<Integer, Boolean>();
			issueTypeToIssueTypeByProjectTypeMap.put(parentIssueTypeID, issueTypesForRow);
			for (ILabelBean childIssusTypeBean : issueTypeBeans) {
				Integer chilsIssueTypeID = childIssusTypeBean.getObjectID();
				issueTypesForRow.put(chilsIssueTypeID, Boolean.FALSE);
			}
		}
		List<TChildIssueTypeBean> childIssueTypeToParentIssueType = ChildIssueTypeAssignmentsBL.loadAll();
		for (TChildIssueTypeBean childIssueTypeBean : childIssueTypeToParentIssueType) {
			Integer parentIssueType = childIssueTypeBean.getIssueTypeParent();
			Integer childIssueType = childIssueTypeBean.getIssueTypeChild();
			Map<Integer, Boolean> issueTypesForRow = issueTypeToIssueTypeByProjectTypeMap.get(childIssueType);
			if (issueTypesForRow!=null) {
				issueTypesForRow.put(parentIssueType, Boolean.TRUE);
			}
		}
		return issueTypeToIssueTypeByProjectTypeMap;
	}
	
	
	/**
	 * Deletes a child assignment	
	 * @param objectID
	 * @return
	 */
}
