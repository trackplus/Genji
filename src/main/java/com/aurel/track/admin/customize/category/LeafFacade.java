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

package com.aurel.track.admin.customize.category;

import java.util.List;
import java.util.Locale;

import com.aurel.track.beans.ILabelBean;

/**
 * Abstract facade for leafs (filter or report)
 * @author Tamas
 *
 */
public abstract class LeafFacade extends CategoryBaseFacade {
	
	/**
	 * Differentiates the query type for issue filters: tree, TQL or TQLPlus
	 */
	@Override
	public abstract Integer getSubtype(ILabelBean labelBeanFrom);
	
	/**
	 * Filters those leafs which should be available also coming from an issueNavigator
	 * (uses only by report templates)
	 * @param leafLabelBeans
	 * @param fromIssueNavigator
	 * @return
	 */
	public List<ILabelBean> filterFromIssueNavigator(List<ILabelBean> leafLabelBeans, boolean fromIssueNavigator) {
		return leafLabelBeans;
	}
	
	/**
	 * Add leaf nodes made from leaf beans
	 * @param repository
	 * @param leafBeans
	 * @param personID
	 * @param modifiable
	 * @param tree
	 * @param projectID
	 * @param locale
	 * @param nodes
	 */
	public abstract void addLeafs(Integer repository, List<ILabelBean> leafBeans,
			Integer personID, boolean modifiable, boolean tree, Integer projectID, Locale locale, List<CategoryTO> nodes);
	
	/**
	 * Get the icon for a leaf
	 * @param labelBean
	 * @return
	 */
	public abstract String getIconCls(ILabelBean labelBean);
	
	/**
	 * Get the human readable format of the field expression
	 * @param objectID
     * @param locale
	 */	
	public abstract String getFilterExpressionString(Integer objectID, Locale locale);
}
