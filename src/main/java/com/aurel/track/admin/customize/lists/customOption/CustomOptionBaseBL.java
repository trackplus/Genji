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


package com.aurel.track.admin.customize.lists.customOption;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.lists.BlobBL;
import com.aurel.track.admin.customize.lists.DetailBaseTO;
import com.aurel.track.admin.customize.lists.OptionBaseBL;
import com.aurel.track.admin.customize.localize.LocalizeBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ILocalizedLabelBean;
import com.aurel.track.beans.TListBean;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.OptionDAO;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.design.custom.select.config.ListHierarchy;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.listFields.LocalizedListIndexer;
import com.aurel.track.resources.LocalizeUtil;

/**
 * Base class for configuring the lists
 * Contains the methods for configuring the list entries (insert, update, delete etc.)
 * This class abstracts the list type i.e. these methods have the same signature 
 * independent of the select type: simple select, tree select, cascading select
 * @author Tamas Ruff
 *
 */
public abstract class CustomOptionBaseBL extends OptionBaseBL {
	
	private static OptionDAO optionDAO = DAOFactory.getFactory().getOptionDAO();
	
	public static CustomOptionBaseBL getInstance(Integer listType) {
		switch (listType) {
		case TListBean.LIST_TYPE.SIMPLE:
			return CustomOptionSimpleSelectBL.getInstance();
		case TListBean.LIST_TYPE.CASCADINGCHILD:
		case TListBean.LIST_TYPE.CASCADINGPARENT:
			return CustomOptionCascadingSelectBL.getInstance();
		case TListBean.LIST_TYPE.TREE:
			return CustomOptionTreeSelectBL.getInstance();
		default:
			return null;
		}
	}
	
	/**
	 * Gets the type of the entry
	 * @param repository
	 * @return
	 */
	@Override
	public int getEntityType(Integer repository) {
		return OptionBaseBL.ENTRY_TYPE.CUSTOM_OPTION;
	}
	
	/**
	 * Whether the option has typeflag
	 * @return
	 */
	@Override
	public boolean hasTypeFlag() {
		return false;
	}
	
	/**
	 * Whether editing the type flag is disabled 
	 * @return
	 */
	@Override
	public boolean isDisableTypeflag() {
		return false;
	}
	
	/**
	 * Gets the typeflag if hasTypeFlag
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getTypeflag(ILabelBean labelBean) {
		return null;
	}
	
	/**
	 * Gets the typeflag label if hasTypeFlag
	 * @param listID
	 * @param labelBean
	 * @param locale
	 * @return
	 */
	@Override
	public String getTypeflagLabel(Integer listID, ILabelBean labelBean, Locale locale) {
		return null;
	}
	
	/**
	 * Whether the option has symbol
	 * @param leaf
	 * @return
	 */
	@Override
	public boolean hasSymbol(boolean leaf) {
		//only the leaf nodes has symbol
		return leaf;
	}
	
	/**
	 * Gets the symbol
	 * @param labelBean
	 * @return
	 */
	@Override
	public String getSymbol(ILabelBean labelBean) {
		TOptionBean optionBean = (TOptionBean)labelBean;
		return optionBean.getSymbol();
	}
	
	/**
	 * Sets the symbol
	 * @param labelBean
	 * @param symbol
	 * @return
	 */
	@Override
	public void setSymbol(ILabelBean labelBean, String symbol) {
		TOptionBean optionBean = (TOptionBean)labelBean;
		optionBean.setSymbol(symbol);
	}
	

	/**
	 * Gets the iconKey
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getIconKey(ILabelBean labelBean) {
		TOptionBean optionBean = (TOptionBean)labelBean;
		return optionBean.getIconKey();
	}
		
	/**
	 * Sets the iconKey
	 * @param labelBean
	 * @param iconKey
	 * @return
	 */
	@Override
	public void setIconKey(ILabelBean labelBean, Integer iconKey) {
		TOptionBean optionBean = (TOptionBean)labelBean;
		optionBean.setIconKey(iconKey);
	}
	
	/**
	 * Sets the iconChanged flag
	 * @param labelBean
	 * @param iconChanged
	 * @return
	 */
	@Override
	public void setIconChanged(ILabelBean labelBean, boolean iconChanged) {
		TOptionBean optionBean = (TOptionBean)labelBean;
		optionBean.setIconChanged(BooleanFields.fromBooleanToString(iconChanged));
	}
	
	/**
	 * Whether the background color is defined
	 * @param leaf
	 * @return
	 */
	@Override
	public boolean hasCssStyle(boolean leaf) {
		return leaf;
	}
	
	/**
	 * Gets the background color
	 * @param labelBean
	 * @return
	 */
	@Override
	public String getCSSStyle(ILabelBean labelBean) {
		TOptionBean optionBean = (TOptionBean)labelBean;
		return optionBean.getCSSStyle();
	}
	
	/**
	 * Sets the background color
	 * @param labelBean
	 * @param CSSStyle
	 * @return
	 */
	@Override
	public void setCSSStyle(ILabelBean labelBean, String CSSStyle) {
		TOptionBean optionBean = (TOptionBean)labelBean;
		optionBean.setCSSStyle(CSSStyle);
	}
	
	/**
	 * Whether the percent complete flag is defined
	 * @return
	 */
	@Override
	public boolean hasPercentComplete() {
		return false;
	}
	
	/**
	 * Gets the percent complete value
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getPercentComplete(ILabelBean labelBean) {
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
	}
	
	/**
	 * Whether the default option is defined for this option
	 * @return
	 */
	@Override
	public boolean hasDefaultOption() {
		return true;
	}
	
	/**
	 * Whether this option is set as default
	 * @param labelBean
	 * @return
	 */
	@Override
	public boolean isDefaultOption(ILabelBean labelBean) {
		TOptionBean optionBean = (TOptionBean)labelBean;
		return optionBean.isDefault();
	}
	
	/**
	 * Gets the percent complete value
	 * @param labelBean
	 * @param defaultOption
	 * @return
	 */
	@Override
	public void setDefaultOption(ILabelBean labelBean, boolean defaultOption) {
		TOptionBean optionBean = (TOptionBean)labelBean;
		optionBean.setDefault(defaultOption);
	}
	
	public List<TOptionBean> getLocalizedOptions(Integer listID, Integer parentOptionID, Locale locale) {
		return LocalizeUtil.localizeDropDownList(getOptions(listID, parentOptionID), locale);
	}
	
	public List<TOptionBean> getOptions(Integer listID, Integer parentOptionID) {
		if (parentOptionID==null ) {
			//simple custom list or first parent list of a composite 
			return optionDAO.loadActiveByListOrderedBySortorder(listID);
		} else {
			//child list for a parent entry
			return optionDAO.loadActiveByListAndParentOrderedBySortorder(listID, parentOptionID);
		}
	}
	
	/**
	 * Creates a detail transfer object
	 * @param optionID
	 * @param listID
	 * @param add
	 * @param copy
	 * @param personBean
	 * @param locale
	 * @return
	 */
	@Override
	public DetailBaseTO loadDetailTO(Integer optionID, Integer listID,
			boolean add, boolean copy, TPersonBean personBean, Locale locale) {
		TOptionBean optionBean=null;
		if (!add && optionID!=null) {
			optionBean = OptionBL.loadByPrimaryKey(optionID);
		}
		if (optionBean==null) {
			optionBean = new TOptionBean();
		}
		CustomOptionDetailTO customOptionDetailTO = new CustomOptionDetailTO();
		//a custom entry is modifiable if the custom list it is member of is modifiable
		customOptionDetailTO.setOptionID(optionID);	
		customOptionDetailTO.setLabel(LocalizeUtil.localizeDropDownEntry(optionBean, locale));
		if (add) {
			customOptionDetailTO.setParentOptionID(optionID);
		} 
		customOptionDetailTO.setListID(listID);
		customOptionDetailTO.setCssStyle(getCSSStyle(optionBean));
		customOptionDetailTO.setDefaultOption(isDefaultOption(optionBean));
		return customOptionDetailTO;
	}	
	
	/**
	 * Gets the bean by optionID and list or creates a new bean
	 * @param optionID
	 * @param listID
	 * @return
	 */
	@Override
	public ILabelBean getLabelBean(Integer optionID, Integer listID) {
		ILabelBean labelBean = null;
		if (optionID!=null) {
			labelBean = OptionBL.loadByPrimaryKey(optionID);
		}
		if (labelBean==null) {
			//new bean
			labelBean = new TOptionBean();
		}
		return labelBean;
	}
	
	/**
	 *  Gets the localized bean by optionID and list or creates a new bean
	 * @param optionID
	 * @param listID
	 * @param locale
	 * @return
	 */
	public ILabelBean getLocalizedLabelBean(Integer optionID, Integer listID, Locale locale) {
		ILocalizedLabelBean labelBean = null;
		if (optionID!=null) {
			labelBean = OptionBL.loadByPrimaryKey(optionID);
			if (labelBean!=null) {
				labelBean.setLabel(LocalizeUtil.localizeDropDownEntry(labelBean, locale));
			}
			//existing bean
		}
		if (labelBean==null) {
			//new bean
			labelBean = new TOptionBean();
		}
		return labelBean;
	}
	
	/**
	 * Prepares the custom list option for save after edit
	 * @param optionID
	 * @param listID
	 * @param label
	 * @param add
	 * @param leaf
	 * @return
	 */
	public ILabelBean prepareLabelBean(Integer optionID, Integer listID, 
			String label, String CSSStyle, boolean defaultOption, boolean add, boolean leaf) {
		TOptionBean optionBean = null;
		if (add) {
			optionBean = new TOptionBean();
			if (optionID!=null) {
				//not the top parent, i.e. not added from a list node but added from a parent list option node
				optionBean.setParentOption(optionID);
			}
			//by adding listID refers to the parent's node childListID so exactly the listID needed
			optionBean.setList(listID);
		} else {
			optionBean = OptionBL.loadByPrimaryKey(optionID);
		}
		if (optionBean!=null) {
			optionBean.setLabel(label);
			optionBean.setCSSStyle(CSSStyle);
			optionBean.setDefault(defaultOption);
		}
		return optionBean;
	}
	
	/**
	 * Whether the label is valid
	 * @param listID
	 * @param optionID
	 * @param label
	 * @param add
	 * @return
	 */
	public String isValidLabel(Integer listID, Integer optionID, String label, boolean add, boolean leaf) {
		if (label==null || "".equals(label)) {
			return "common.err.required";
		}
		if (add) {
			if (!leaf) {
				//optionID refers to a parentID
				List<TOptionBean> optionBeans = OptionBL.loadByLabel(listID, optionID, label);
				if (optionBeans!=null && !optionBeans.isEmpty()) {
					for (TOptionBean optionBean : optionBeans) {
						if (!optionBean.isDeleted()) {
							return "common.err.unique";
						}
					}
					
				}
			}
			//if add && leaf the a new composite level is created: label is valid anyway
		} else {
			//edit
			if (optionID!=null) {
				TOptionBean editedOptionBean = OptionBL.loadByPrimaryKey(optionID);
				List<TOptionBean> optionBeans = OptionBL.loadByLabel(listID, editedOptionBean.getParentOption(), label);
				if (optionBeans!=null && !optionBeans.isEmpty()) {
					for (TOptionBean optionBean : optionBeans) {
						if (!optionBean.isDeleted() && !optionID.equals(optionBean.getObjectID())) {
							return "common.err.unique";
						}
					}
				}
			}
		}
		return null;
	}
	
	/******************* sort order specific methods **********************/
	/**
	 * Gets the specific extra constraints
	 * @param labelBean
	 * @return
	 */
	@Override
	public String getSpecificCriteria(ILabelBean labelBean) {
		TOptionBean optionBean = (TOptionBean)labelBean;
		Integer parentOfDraggedBean = optionBean.getParentOption();
		Integer listOfDraggedBean = optionBean.getList();
		String listCriteria = " AND LIST = " + listOfDraggedBean;
		String parentCriteria = "";		
		if (parentOfDraggedBean!=null) {
			parentCriteria = " AND PARENTOPTION = " + parentOfDraggedBean;
		} else {
			parentCriteria = " AND PARENTOPTION IS NULL ";
		}
		return listCriteria + parentCriteria;
	}
	
	/**
	 * Gets the sort order of the bean
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getSortOrder(ILabelBean labelBean) {
		TOptionBean optionBean = (TOptionBean)labelBean;
		return optionBean.getSortOrder();
	}
	
	/**
	 * Gets the sort order of the bean
	 * @param labelBean
	 * @return
	 */
	@Override
	public void setSortOrder(ILabelBean labelBean, Integer sortOrder) {
		TOptionBean optionBean = (TOptionBean)labelBean;
		optionBean.setSortOrder(sortOrder);
	}
	
	/**
	 * Gets the sort order column name 
	 * @return
	 */
	@Override
	public String getSortOrderColumn() {
		return optionDAO.getSortOrderColumn();
	}
	
	/**
	 * Gets the table the sorting is made in 
	 * @return
	 */
	@Override
	public String getTableName() {
		return optionDAO.getTableName();
	}
	
	/**
	 * Saves the label bean into the database
	 * @param labelBean
	 */
	@Override
	public Integer saveSimple(ILabelBean labelBean) {
		return OptionBL.save((TOptionBean)labelBean);
	}
	
	/**
	 * Saves a new/modified list entry
	 * If sortOrder is not set it will be set to be the 
	 * next available sortOrder according to listID and parentID
	 * @param labelBean
	 * @param copy
	 * @param personID
	 * @param locale
	 * @return
	 */
	public synchronized Integer save(ILabelBean labelBean, boolean copy, Integer personID, Locale locale) {
		TOptionBean optionBean = (TOptionBean)labelBean;
		boolean isNew = optionBean.getObjectID()==null;
		if (optionBean.getSortOrder()==null) {
			Integer sortOrder;
			if (optionBean.getParentOption()==null) {
				sortOrder = optionDAO.getNextSortOrder(optionBean.getList()); 
			} else {
				sortOrder = optionDAO.getNextSortOrder(optionBean.getList(), optionBean.getParentOption());
			}
			optionBean.setSortOrder(sortOrder);
		}
		Integer optionID = OptionBL.save(optionBean);
		if (isNew) {
			optionBean.setObjectID(optionID);
			LocalizedListIndexer.getInstance().addLabelBean(optionBean,
				LuceneUtil.LOOKUPENTITYTYPES.CUSTOMOPTION, isNew);
		} else {
			//The localization is loaded from localized resources table, and if not found, 
			//from the localized property files, and if not found from the "native" table. 
			//The value should be saved also with the default locale only if it is edited 
			//(new entities have no correspondence in the localized property files)
			LocalizeBL.saveCustomFieldLocalizedResource(optionBean.getList(), 
					optionID, optionBean.getLabel(), locale);
			LocalizedListIndexer.getInstance().updateLabelBean(optionBean,
				LuceneUtil.LOOKUPENTITYTYPES.CUSTOMOPTION);
		}
		//possible lucene update, custom option is not cached
		ClusterMarkChangesBL.markDirtyCustomListEntryInCluster(optionBean.getList(), optionID, ClusterMarkChangesBL.getChangeTypeByAddOrUpdateIndex(isNew));
		return optionID;
	}
		
	/**
	 * Deletes an option from the list when 
	 * no workItem refers has this option (or one of their suboptions)
	 * (All descendant lists of this option will also be deleted!)
	 * Otherwise the deleted flag will be set using the setDeleted() method 
	 * @param optionID
	 * @return	- true if delete is successfull
	 * 			- false if delete is unsuccessful: workItem refers to this option or one of their suboption
	 * 			In this case the deleted flag will be set
	 */	
	@Override
	public void delete(Integer optionID) {
		TOptionBean optionBean = OptionBL.loadByPrimaryKey(optionID);
		if (hasDependentData(optionID)) {
			setDeleted(optionBean, true);
		} else {
			if (optionBean!=null) {
				deleteOptionWithChild(optionID, optionBean.getList());
			}
		}
	}
	
	protected void deleteOptionAndBlob(Integer optionID) {
		TOptionBean optionBean = OptionBL.loadByPrimaryKey(optionID);
		Integer iconKey = null;
		if (optionBean!=null) {
			iconKey = optionBean.getIconKey();
		}
		OptionBL.delete(optionID);
		if (iconKey!=null) {
			BlobBL.delete(iconKey);
		}
	}
	
	/**
	 * Delete an option with all his childs (when it is the case) (childs first)
	 * @param optionID
	 * @param listID
	 */
	public abstract void deleteOptionWithChild(Integer optionID, Integer listID);
	
	
	/**
	 * Sets an option and all its descendants as deleted
	 * @param optionBean
	 * @param flag
	 */
	public abstract void setDeleted(TOptionBean optionBean, boolean flag);
	
	/**
	 * Makes a copy of the entire list hierarchically (when it is the case) 
	 * @param listHierarchy
	 * @param copiedOptionsMap out parameter
	 */
	public abstract void copyOptions(ListHierarchy listHierarchy, Map<Integer, Integer> copiedOptionsMap);
}
