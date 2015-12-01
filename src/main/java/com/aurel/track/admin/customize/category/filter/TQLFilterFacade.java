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

package com.aurel.track.admin.customize.category.filter;


import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.aurel.track.admin.customize.category.filter.tree.design.FieldExpressionInTreeTO;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.LabelValueBean;

/**
 * An implementation of FilterFacade for TQL filters
 * @author Tamas Ruff
 *
 */
public class TQLFilterFacade extends IssueFilterFacade {	
		
	private static TQLFilterFacade instance;		
	
	protected TQLFilterFacade() {
		super();		
	}

	/**
	 * Return a FieldConfigItemFacade instance which implements the ConfigItemFacade 
	 * @return
	 */
	public static TQLFilterFacade getInstance(){
		if(instance==null){
			instance=new TQLFilterFacade();
		}
		return instance;
	}
	
	/**
	 * Gets the label key for this type
	 */
	@Override
	public String getLabelKey() {
		return "admin.customize.queryFilter.lbl.tql";
	}
	
	/**
	 * Get the human readable format of the field expression
	 * @param filterExpression
	 * @param locale
	 */	
	@Override
	public String getFilterExpressionString(String filterExpression, Locale locale) {
		return filterExpression;
	}
	
	/**
	 * Gets the detail json for a TQL filter
	 * It is preferable to be declared as abstract but then the
	 * class should be also abstract and then it can't be instantiated 
	 * @param includeHeader whether to include the header part (name and other settings)
	 * @param includePathPicker whether to include the patch picker
	 * @param label
	 * @param filterType
	 * @param filterSubtype
	 * @param styleField
	 * @param includeInMenu
	 * @param filterExpression
	 * @param add
	 * @param modifiable
	 * @param withParameter
	 * @param personBean
	 * @param projectID
	 * @param locale
	 * @return
	 */
	@Override
	public String getFilterJSON(boolean includeHeader, boolean includePathPicker, String label, Integer filterType, Integer filterSubtype, Integer styleField,
			boolean includeInMenu,  String viewID, String filterExpression,
			boolean add, boolean modifiable, boolean withParameter,
			TPersonBean personBean, Integer projectID, Locale locale) {
		List<IntegerStringBean> styleFields = null;
		List<LabelValueBean> views = null;
		if (includeHeader) {
			//the style and view are not rendered in item navigator
			styleFields = getStyleFields(projectID, locale);
			views = getViews(personBean, locale);
		}
		return FilterJSON.getTQLFilterJSON(label, filterSubtype, getTQLFilterTypes(locale),
			styleField, styleFields, viewID, views, includeInMenu, filterExpression, modifiable);
	}
	
	/**
	 * Gets the TQL types
	 * @param locale
	 * @return
	 */
	private List<IntegerStringBean> getTQLFilterTypes(Locale locale) {
		List<IntegerStringBean> filterTypes = new LinkedList<IntegerStringBean>();
		filterTypes.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.queryFilter.lbl.tqlPlus", locale),TQueryRepositoryBean.QUERY_PURPOSE.TQLPLUS_FILTER));
		filterTypes.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.customize.queryFilter.lbl.tql", locale), TQueryRepositoryBean.QUERY_PURPOSE.TQL_FILTER));
		return filterTypes;
	}
	
	/**
	 * Gets the specific expression
	 * @param tqlExpression
	 * @param filterSelectsTO
	 * @param fieldExpressionInTreeList
	 * @return
	 */
	@Override
	public String getFilterExpression(String tqlExpression,
			FilterUpperTO filterSelectsTO, List<FieldExpressionInTreeTO> fieldExpressionInTreeList) {
		return tqlExpression;
	}
}
