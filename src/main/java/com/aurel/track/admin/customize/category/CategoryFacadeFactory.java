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

package com.aurel.track.admin.customize.category;

import com.aurel.track.admin.customize.category.filter.FilterFacade;
import com.aurel.track.admin.customize.category.filter.IssueFilterCategoryFacade;
import com.aurel.track.admin.customize.category.filter.NotifyFilterCategoryFacade;
import com.aurel.track.admin.customize.category.filter.NotifyFilterFacade;
import com.aurel.track.admin.customize.category.filter.TQLFilterFacade;
import com.aurel.track.admin.customize.category.filter.TreeFilterFacade;
import com.aurel.track.admin.customize.category.report.ReportCategoryFacade;
import com.aurel.track.admin.customize.category.report.ReportFacade;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TQueryRepositoryBean;

/**
 * Factory for report or filter categories
 * @author Tamas
 *
 */
public class CategoryFacadeFactory {
	
	private static CategoryFacadeFactory instance;
	public static CategoryFacadeFactory getInstance(){
		if(instance==null){
			instance=new CategoryFacadeFactory();
		}
		return instance;
	}

	
	/**
	 * Get the proper configItem for configType
	 * @param categoryType
	 * @return
	 */
	public CategoryFacade getCategoryFacade(String categoryType){
		if(categoryType.equals(CategoryBL.CATEGORY_TYPE.ISSUE_FILTER_CATEGORY)){
			return IssueFilterCategoryFacade.getInstance();
		}
		if(categoryType.equals(CategoryBL.CATEGORY_TYPE.NOTIFICATION_FILTER_CATEGORY)){
			return NotifyFilterCategoryFacade.getInstance();
		}
		if(categoryType.equals(CategoryBL.CATEGORY_TYPE.REPORT_CATEGORY)){
			return ReportCategoryFacade.getInstance();
		}
		return null;
	}
	
	/**
	 * Get the proper configItem for configType
	 * @param categoryType
	 * @return
	 */
	public LeafFacade getLeafFacade(String categoryType){
		if (categoryType.equals(CategoryBL.CATEGORY_TYPE.ISSUE_FILTER_CATEGORY)){
			return TreeFilterFacade.getInstance();
		}
		if (categoryType.equals(CategoryBL.CATEGORY_TYPE.NOTIFICATION_FILTER_CATEGORY)){
			return NotifyFilterFacade.getInstance();
		}
		if (categoryType.equals(CategoryBL.CATEGORY_TYPE.REPORT_CATEGORY)){
			return ReportFacade.getInstance();
		}
		return null;
	}
	
	
	/**
	 * Get the proper configItem for configType
	 * @param categoryType
	 * @param filterID 
	 * @return
	 */
	public LeafFacade getLeafFacade(String categoryType, Integer filterID){
		if (categoryType.equals(CategoryBL.CATEGORY_TYPE.ISSUE_FILTER_CATEGORY)){
			FilterFacade filterFacade = TreeFilterFacade.getInstance();
			ILabelBean labelBean = filterFacade.getByKey(filterID);
			Integer subType = filterFacade.getSubtype(labelBean);
			if (subType==null || TQueryRepositoryBean.QUERY_PURPOSE.NOTIFY_FILTER==subType.intValue() ||
					TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER==subType.intValue()) {
				return TreeFilterFacade.getInstance();
			} else {
				return TQLFilterFacade.getInstance();
			}
		}
		if (categoryType.equals(CategoryBL.CATEGORY_TYPE.NOTIFICATION_FILTER_CATEGORY)){
			return NotifyFilterFacade.getInstance();
		}
		if(categoryType.equals(CategoryBL.CATEGORY_TYPE.REPORT_CATEGORY)){
			return ReportFacade.getInstance();
		}
		return null;
	}
	
}
