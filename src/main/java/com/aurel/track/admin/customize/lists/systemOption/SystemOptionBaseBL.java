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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.aurel.track.admin.customize.lists.DetailBaseTO;
import com.aurel.track.admin.customize.lists.ListBL.RESOURCE_TYPES;
import com.aurel.track.admin.customize.lists.OptionBaseBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ILocalizedLabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.IntegerStringBean;

/**
 * Base class for configuring the system lists
 * @author Tamas Ruff
 */
public abstract class SystemOptionBaseBL extends OptionBaseBL {

	public static SystemOptionBaseBL getInstance(Integer listType) {
		if (listType!=null) {
			switch (listType.intValue()) {
			case RESOURCE_TYPES.STATUS:
				return StatusConfigBL.getInstance();
			case RESOURCE_TYPES.PRIORITY:
				return PriorityConfigBL.getInstance();
			case RESOURCE_TYPES.SEVERITY:
				return SeverityConfigBL.getInstance();
			case RESOURCE_TYPES.ISSUETYPE:
				return IssueTypeConfigBL.getInstance();
			}
		}
		return null;
	}	
	
	/**
	 * Whether the background color is defined
	 * @param leaf	
	 * @return
	 */
	@Override
	public boolean hasCssStyle(boolean leaf) {
		return true;
	}

	
	/**
	 * Gets the type of the entry
	 * @param repository
	 * @return
	 */
	public int getEntityType(Integer repository) {
		return OptionBaseBL.ENTRY_TYPE.SYSTEM_OPTION;
	}
	
	/**
	 * Whether the option has typeflag
	 * @return
	 */
	@Override
	public boolean hasTypeFlag() {
		return true;
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
	 * Gets the background color
	 * @param labelBean
	 * @return
	 */
	@Override
	public abstract Integer getTypeflag(ILabelBean labelBean);
	
	/**
	 * Sets the typeflag
	 * @param labelBean
	 * @param typeflag
	 * @return
	 */
	public abstract void setTypeflag(ILabelBean labelBean, Integer typeflag);
	
	/**
	 * Gets the possible typeflag prefix for a system field
	 * @param fieldID
	 * @return
	 */
	public abstract String getTypeflagPrefix(int fieldID);
	
	/**
	 * Gets the possible typeflags for a system field
	 * @param listID
	 * @param actualFlag
	 * @return
	 */
	protected abstract int[] getPossibleTypeFlags(Integer listID, Integer actualFlag);
	
	/**
	 * Gets the typeflag label if hasTypeFlag
	 * @param listID
	 * @param labelBean
	 * @param locale
	 * @return
	 */
	@Override
	public String getTypeflagLabel(Integer listID, ILabelBean labelBean, Locale locale) {
		return LocalizeUtil.getLocalizedTextFromApplicationResources(
				getTypeflagPrefix(listID) + getTypeflag(labelBean), locale);
	}
	
	
	/**
	 * Whether the option has symbol
	 * @param leaf
	 * @return
	 */
	@Override
	public boolean hasSymbol(boolean leaf) {
		return true;
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
		return false;
	}
	
	/**
	 * Whether this option is set as default
	 * @param labelBean
	 * @return
	 */
	@Override
	public boolean isDefaultOption(ILabelBean labelBean) {		
		return false;
	}
	
	/**
	 * Gets the percent complete value
	 * @param labelBean
	 * @param defaultOption
	 * @return
	 */
	@Override
	public void setDefaultOption(ILabelBean labelBean, boolean defaultOption) {
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
	public DetailBaseTO loadDetailTO(Integer optionID, Integer listID,
			boolean add, boolean copy, TPersonBean personBean, Locale locale) {
		ILabelBean labelBean = getLocalizedLabelBean(optionID, listID, locale);
		SystemOptionDetailTO systemOptionDetailTO = new SystemOptionDetailTO();
		systemOptionDetailTO.setOptionID(optionID);
		systemOptionDetailTO.setListID(listID);
		Integer typeFlag = null;
		if (labelBean!=null) {
			systemOptionDetailTO.setLabel(labelBean.getLabel());
			systemOptionDetailTO.setCssStyle(getCSSStyle(labelBean));
			typeFlag = getTypeflag(labelBean);
		}
		List<IntegerStringBean> typeflagsList = new ArrayList<IntegerStringBean>();
		int[] possibleTypeflags = getPossibleTypeFlags(listID, typeFlag);
		if (possibleTypeflags!=null) {
			for (int i = 0; i < possibleTypeflags.length; i++) {
				int possibleValue = possibleTypeflags[i];
				typeflagsList.add(new IntegerStringBean(
					LocalizeUtil.getLocalizedTextFromApplicationResources(getTypeflagPrefix(listID) + possibleValue, locale), 
					Integer.valueOf(possibleValue)));
			}
		}
		systemOptionDetailTO.setTypeflagsList(typeflagsList);
		if (typeFlag==null) {
			//new option
			typeFlag = typeflagsList.get(0).getValue();
		}
		systemOptionDetailTO.setTypeflag(typeFlag);
		systemOptionDetailTO.setPercentComplete(getPercentComplete(labelBean));
		return systemOptionDetailTO;
	}
	
	/**
	 * Gets the bean by optionID and list or creates a new bean
	 * @param optionID
	 * @param listID
	 * @return
	 */
	public ILabelBean getLabelBean(Integer optionID, Integer listID) {
		ILabelBean labelBean = null;
		if (optionID!=null) {
			labelBean = getExistingLabelBeanByOptionID(optionID);
			//existing bean
			return labelBean;
		}
		//new bean
		return getNewLabelBean(listID);
	}
	
	/**
	 * Gets the localized bean by optionID and list or creates a new bean
	 * @param optionID
	 * @param listID
	 * @param locale
	 * @return
	 */
	public ILabelBean getLocalizedLabelBean(Integer optionID, Integer listID, Locale locale) {
		ILocalizedLabelBean labelBean = null;
		if (optionID!=null) {
			labelBean = getExistingLabelBeanByOptionID(optionID);
			if (labelBean!=null) {
				labelBean.setLabel(LocalizeUtil.localizeDropDownEntry(labelBean, locale));
			}
			//existing bean
			return labelBean;
		}
		//new bean
		return getNewLabelBean(listID);
	}

	/**
	 * Whether the new label is valid (not empty not duplicate)
	 * @param optionID
	 * @param label
	 * @return
	 */
	public String isValidLabel(Integer listID, Integer optionID, String label)  {
		if (label==null || "".equals(label)) {
			return "common.err.required";
		}
		List<ILabelBean> labelBeans = getExistingLabelBeanByLabel(listID, label);
		ILabelBean labelBean = null;
		if (labelBeans!=null && !labelBeans.isEmpty()) {
			if (labelBeans.size()>1) {
				return "common.err.unique";
			} else {
				labelBean = labelBeans.get(0);
				if (optionID==null || !labelBean.getObjectID().equals(optionID)) {
					return "common.err.unique";
				}
			}
		}
		return null;
	}
	
	
	
	/**
	 * Prepares the system list option for save after edit
	 * @param optionID
	 * @param listID
	 * @param label
	 * @param typeflag
	 * @return
	 */
	public ILabelBean prepareLabelBean(Integer optionID, Integer listID, 
			String label, String cssStyle, Integer typeflag, Integer percentComplete, boolean leaf) {
		ILabelBean labelBean;
		if (optionID==null) {
			labelBean = getNewLabelBean(listID);
		} else {
			labelBean = getExistingLabelBeanByOptionID(optionID);
		}
		if (labelBean!=null) {
			setLabel(labelBean, label);
			setCSSStyle(labelBean, cssStyle);
			if (hasTypeFlag()) {
				if (typeflag!=null) {
					setTypeflag(labelBean, typeflag);
				} else {
					if (getTypeflag(labelBean)==null) {
						//not sent as request parameter because directly added from tree
						int[] typeflags = getPossibleTypeFlags(listID, null);
						if (typeflags.length>0) {
							setTypeflag(labelBean, typeflags[0]);
						}
					}
				}
			}
			if (hasPercentComplete()) {
				setPercentComplete(labelBean, percentComplete);
			}
		}
		return labelBean;
	}

	/**
	 * Whether the label bean is deletable
	 * @param labelBean
	 * @return
	 */
	public boolean isDeletable(ILabelBean labelBean) {
		return true;
	}	
	
	/**
	 * Gets the list options
	 * @param locale
	 * @return
	 */
	public abstract List<ILabelBean> getOptions(Integer listID, Locale locale, ApplicationBean applicationBean);
	
	/**
	 * Gets the specific label bean by optionID 
	 * @param optionID
	 * @return 
	 */
	public abstract ILocalizedLabelBean getExistingLabelBeanByOptionID(Integer optionID);
	
	/**
	 * Gets the specific label bean by label
	 * @param listID
	 * @param label
	 * @return 
	 */
	public abstract List<ILabelBean> getExistingLabelBeanByLabel(Integer listID, String label);
	
	/**
	 * Gets the specific new label bean
	 * @param listID
	 * @return
	 */
	public abstract ILabelBean getNewLabelBean(Integer listID);
	
	/**
	 * Sets the label
	 * @param labelBean
	 * @param label
	 * @return
	 */
	public abstract void setLabel(ILabelBean labelBean, String label);
	
	
	/** 
     * This method replaces all occurrences of oldOID with
     * newOID and then deletes the oldOID
     * @param oldOID
     * @param newOID
     */
	public abstract void replaceAndDelete(Integer oldOID, Integer newOID);
		
	/**
	 * Gets the specific extra constraints by setting the sort order
	 * @param labelBean
	 * @return
	 */
	@Override
	public String getSpecificCriteria(ILabelBean labelBean) {
		return "";		
	}
		
}
