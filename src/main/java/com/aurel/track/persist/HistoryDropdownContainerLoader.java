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

package com.aurel.track.persist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.torque.util.Criteria;

import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.lists.systemOption.PriorityBL;
import com.aurel.track.admin.customize.lists.systemOption.SeverityBL;
import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TAttributeValueBean;
import com.aurel.track.beans.TFieldChangeBean;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.OptionDAO;
import com.aurel.track.dao.ProjectDAO;
import com.aurel.track.dao.ReleaseDAO;
import com.aurel.track.exchange.track.ExchangeFieldNames;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.DropDownContainer;
import com.aurel.track.fieldType.runtime.bl.AttributeValueBL;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.item.history.FieldChangeBL;
import com.aurel.track.util.GeneralUtils;

public class HistoryDropdownContainerLoader {
	
	private static ProjectDAO projectDAO = DAOFactory.getFactory().getProjectDAO();
	private static ReleaseDAO releaseDAO = DAOFactory.getFactory().getReleaseDAO();
	private static OptionDAO optionDAO = DAOFactory.getFactory().getOptionDAO();
	/**
	 * We do not bother for getting only the really needed beans in the dropDown container
	 * for some system selects because they contain anyway just a few records
	 * @return
	 */
	/*private static DropDownContainer initHistoryDropdown(Locale locale) {
		DropDownContainer dropDownContainer = new DropDownContainer();
		if (locale!=null) {
			dropDownContainer.setLocale(locale);
			dropDownContainer.setDataSourceMap(MergeUtil.mergeKey(new Integer(SystemFields.ISSUETYPE), null), 
					GeneralUtils.createMapFromList(IssueTypeBL.loadAll(locale)));
			dropDownContainer.setDataSourceMap(MergeUtil.mergeKey(new Integer(SystemFields.STATE), null), 
					GeneralUtils.createMapFromList(StatusBL.loadAll(locale)));
			dropDownContainer.setDataSourceMap(MergeUtil.mergeKey(new Integer(SystemFields.PRIORITY), null), 
					GeneralUtils.createMapFromList(PriorityBL.loadAll(locale)));
			dropDownContainer.setDataSourceMap(MergeUtil.mergeKey(new Integer(SystemFields.SEVERITY), null), 
					GeneralUtils.createMapFromList(LocalizeUtil.localizeDropDownList(SeverityBL.loadAll(), locale)));
		}
		return dropDownContainer;
	}*/
	
	
	
	/**
	 * Loads the history selects in the DropDownContainer 
	 * @param workItemIDs
	 * @param locale
	 * @return
	 */
	/*public static DropDownContainer loadHistoryDropDownContainer(int[] workItemIDs, Locale locale) {
		DropDownContainer dropDownContainer = initHistoryDropdown(locale);		
		dropDownContainer.setDataSourceMap(MergeUtil.mergeKey(new Integer(SystemFields.PERSON), null), 
				GeneralUtils.createMapFromList(personDAO.loadHistoryPersons(workItemIDs)));

		dropDownContainer.setDataSourceMap(MergeUtil.mergeKey(new Integer(SystemFields.PROJECT), null), 
				GeneralUtils.createMapFromList(projectDAO.loadHistoryProjects(workItemIDs)));

		dropDownContainer.setDataSourceMap(MergeUtil.mergeKey(new Integer(SystemFields.RELEASE), null), 
				(Map)releaseDAO.loadHistoryReleases(workItemIDs));
		
		List<TFieldChangeBean> customOptionFieldChangesBeanList = 
			fieldChangeDAO.loadHistoryCustomOptionFieldChanges(workItemIDs);
		Map<Integer, TOptionBean> customOptionsMap = optionDAO.loadHistoryOptions(workItemIDs);
		setCustomOptions(dropDownContainer, customOptionFieldChangesBeanList, null, customOptionsMap);
		return dropDownContainer;
	}*/
	
	/**
	 * We do not bother for getting only the really needed beans in the dropDown container
	 * for some system selects because they contain anyway just a few records
	 * @return
	 */
	private static DropDownContainer initExporterDropdown() {
		DropDownContainer dropDownContainer = new DropDownContainer();
		dropDownContainer.setDataSourceMap(MergeUtil.mergeKey(SystemFields.INTEGER_ISSUETYPE, null),
				GeneralUtils.createMapFromList(IssueTypeBL.loadAll()));
		dropDownContainer.setDataSourceMap(MergeUtil.mergeKey(SystemFields.INTEGER_STATE, null),
				GeneralUtils.createMapFromList(StatusBL.loadAll()));
		dropDownContainer.setDataSourceMap(MergeUtil.mergeKey(SystemFields.INTEGER_PRIORITY, null),
				GeneralUtils.createMapFromList(PriorityBL.loadAll()));
		dropDownContainer.setDataSourceMap(MergeUtil.mergeKey(SystemFields.INTEGER_SEVERITY, null),
				GeneralUtils.createMapFromList(SeverityBL.loadAll()));
		dropDownContainer.setDataSourceMap(ExchangeFieldNames.LIST,
				GeneralUtils.createMapFromList(ListBL.loadAll()));
		return dropDownContainer;
	}
	
	/**
	 * Loads the actual selects and the history selects in the 
	 * DropDownContainer for preparing the exporter 
	 * @param workItemIDs
	 * @return
	 */
	public static DropDownContainer loadExporterDropDownContainer(int[] workItemIDs) {
		DropDownContainer dropDownContainer = initExporterDropdown();
		//load all persons because otherwise they should be get from a lot of places: workItems, history, budget, cost etc. 
		List<TPersonBean> concernedPersons = PersonBL.loadPersonsAndGroups();
		dropDownContainer.setDataSourceMap(MergeUtil.mergeKey(SystemFields.INTEGER_PERSON, null), 
				GeneralUtils.createMapFromList(concernedPersons));
		Map historyValueProjects = GeneralUtils.createMapFromList(projectDAO.loadHistoryProjects(workItemIDs));
		historyValueProjects.putAll(GeneralUtils.createMapFromList(ProjectBL.loadByWorkItemKeys(workItemIDs)));
		dropDownContainer.setDataSourceMap(MergeUtil.mergeKey(SystemFields.INTEGER_PROJECT, null), 
				historyValueProjects);
		Map historyValueReleases = releaseDAO.loadHistoryReleases(workItemIDs);
		historyValueReleases.putAll(GeneralUtils.createMapFromList(releaseDAO.loadNoticedByWorkItemKeys(workItemIDs)));
		historyValueReleases.putAll(GeneralUtils.createMapFromList(releaseDAO.loadScheduledByWorkItemKeys(workItemIDs)));
		dropDownContainer.setDataSourceMap(MergeUtil.mergeKey(SystemFields.INTEGER_RELEASE, null), 
				historyValueReleases);
		List<TFieldChangeBean> historyValuesCustomOptionFieldChangesBeanList = 
				FieldChangeBL.loadHistoryCustomOptionFieldChanges(workItemIDs);
		List<TAttributeValueBean> attributeValueBeanList = AttributeValueBL.loadLuceneCustomOptionAttributeValues(workItemIDs);
		Map<Integer, TOptionBean> customOptionsMap = optionDAO.loadHistoryOptions(workItemIDs);
		customOptionsMap.putAll(GeneralUtils.createMapFromList(optionDAO.loadLuceneOptions(workItemIDs)));
		setCustomOptions(dropDownContainer, historyValuesCustomOptionFieldChangesBeanList, attributeValueBeanList, customOptionsMap);
		return dropDownContainer;
	}
	/**
	 * Sets the custom options in the DropDownContainer by fieldID_parameterCode
	 * @param dropDownContainer
	 * @param attributeValueBeanList
	 * @param customOptionList
	 */
	private static void setCustomOptions(DropDownContainer dropDownContainer, 
			List<TFieldChangeBean> fieldChangesBeanList, List<TAttributeValueBean> attributeValueBeanList,
			Map<Integer, TOptionBean> customOptionMap) {
		//put the history custom options
		if (fieldChangesBeanList!=null) {
			for (TFieldChangeBean fieldChangeBean : fieldChangesBeanList) {
				String mergeKey = MergeUtil.mergeKey(fieldChangeBean.getFieldKey(), fieldChangeBean.getParameterCode());
				Map<Integer, ILabelBean> dataSourceMap = dropDownContainer.getDataSourceMap(mergeKey);
				if (dataSourceMap==null) {
					dataSourceMap = new HashMap<Integer, ILabelBean>();
					dropDownContainer.setDataSourceMap(mergeKey, dataSourceMap);
				}
				Integer newCustomOptionID = fieldChangeBean.getNewCustomOptionID();
				if (dataSourceMap.get(newCustomOptionID)==null) {
					dataSourceMap.put(newCustomOptionID, customOptionMap.get(newCustomOptionID));
				}
				Integer oldCustomOptionID = fieldChangeBean.getOldCustomOptionID();
				if (dataSourceMap.get(oldCustomOptionID)==null) {
					dataSourceMap.put(oldCustomOptionID, customOptionMap.get(oldCustomOptionID));
				}
			}
		}
		//put the actual custom options
		if (attributeValueBeanList!=null) {
			for (TAttributeValueBean attributeValueBean : attributeValueBeanList) {
				String mergeKey = MergeUtil.mergeKey(attributeValueBean.getField(), attributeValueBean.getParameterCode());
				Map<Integer, ILabelBean> dataSourceMap = dropDownContainer.getDataSourceMap(mergeKey);
				if (dataSourceMap==null) {
					dataSourceMap = new HashMap<Integer, ILabelBean>();
					dropDownContainer.setDataSourceMap(mergeKey, dataSourceMap);
				}
				Integer customOptionID = attributeValueBean.getCustomOptionID();
				if (dataSourceMap.get(customOptionID)==null) {
					dataSourceMap.put(customOptionID, customOptionMap.get(customOptionID));
				}				
			}
		}
	}
	
	public static Criteria prepareHistoryCriteria(int[] workItemIDs/*, Integer personID*/) {
		Criteria criteria = new Criteria();
		criteria.addJoin(BaseTHistoryTransactionPeer.OBJECTID, BaseTFieldChangePeer.HISTORYTRANSACTION);
		criteria.addIn(BaseTHistoryTransactionPeer.WORKITEM, workItemIDs);
		/*if (personID!=null) {
			criteria.add(THistoryTransactionPeer.CHANGEDBY, personID);
        }*/			
		return criteria;
	}
	
	public static Criteria prepareHistoryCustomOptionCriteria(int[] workItemIDChunk, /*Integer personID,*/ boolean isNew) {
		Criteria criteria = HistoryDropdownContainerLoader.prepareHistoryCriteria(workItemIDChunk/*, personID*/);		
		if (isNew) {
			criteria.addJoin(BaseTFieldChangePeer.NEWCUSTOMOPTIONID, BaseTOptionPeer.OBJECTID);
		} else {
			criteria.addJoin(BaseTFieldChangePeer.OLDCUSTOMOPTIONID, BaseTOptionPeer.OBJECTID);
		}
		criteria.add(BaseTFieldChangePeer.VALIDVALUE, ValueType.CUSTOMOPTION);
		criteria.setDistinct();	
		return criteria;
	}
	
	public static Criteria prepareHistorySystemOptionCriteria(int[] workItemIDChunk, /*Integer personID,*/ 
			boolean isNew, String fieldName, int systemOptionType) {
		Criteria crit = HistoryDropdownContainerLoader.prepareHistoryCriteria(workItemIDChunk/*, personID*/);		
		if (isNew) {
			crit.addJoin(BaseTFieldChangePeer.NEWSYSTEMOPTIONID, fieldName);
		} else {
			crit.addJoin(BaseTFieldChangePeer.OLDSYSTEMOPTIONID, fieldName);
		}
		crit.add(BaseTFieldChangePeer.VALIDVALUE, ValueType.SYSTEMOPTION);	
		crit.add(BaseTFieldChangePeer.SYSTEMOPTIONTYPE, systemOptionType);
		crit.setDistinct();	
		return crit;
	}
}
