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

package com.aurel.track.admin.customize.category.filter.parameters;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.aurel.track.admin.customize.category.filter.FieldExpressionBL;
import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.category.filter.FilterFacade;
import com.aurel.track.admin.customize.category.filter.FilterJSON;
import com.aurel.track.admin.customize.category.filter.QNode;
import com.aurel.track.admin.customize.category.filter.QNodeExpression;
import com.aurel.track.admin.customize.category.filter.TreeFilterFacade;
import com.aurel.track.admin.customize.category.filter.execute.ReportQueryBL;
import com.aurel.track.admin.customize.category.filter.execute.SavedFilterExecuteAction;
import com.aurel.track.admin.customize.category.filter.tree.design.FieldExpressionSimpleTO;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterSelectsListsLoader;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperFromQNodeTransformer;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.TreeFilterLoaderBL;
import com.aurel.track.admin.customize.category.filter.tree.io.TreeFilterReader;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.util.GeneralUtils;

public class RenderParametersUtil {
	/**
	 * Render the parameters page after executing a parameterized filter 
	 * (either first opened for edit possibly "instantly" changed before or directly)
	 * (After a project change in parameters change FilterUpperRefreshAction's 
	 * execute is called, just like by "normal" filter edit) 
	 * @return
	 */
	public static String renderParameters(Integer filterID, String query,
			HttpServletRequest servletRequest, Map<String, Object> session, TPersonBean personBean, Locale locale) {
		FilterUpperTO originalFilterUpperTO=null;
		QNode rootNodeOriginal=null;
		if (query!=null && query.length()>0) {
			//encoded query URL
			String linkReport = ReportQueryBL.b(query);
			Map<String,String>  queryEncodedMap = ReportQueryBL.decodeMapFromUrl(linkReport);
			String queryIDStr= queryEncodedMap.get("queryID");
			Integer encodedFilterID=null;
			if (queryIDStr!=null){
				encodedFilterID=Integer.decode(queryIDStr);
				if (encodedFilterID!=null) {
					TQueryRepositoryBean queryRepositoryBean = 
							(TQueryRepositoryBean)TreeFilterFacade.getInstance().getByKey(encodedFilterID);
					if (queryRepositoryBean!=null) {
						String filterExpression = FilterBL.getFilterExpression(queryRepositoryBean);
					QNode extendedRootNode = TreeFilterReader.getInstance().readQueryTree(filterExpression);
					originalFilterUpperTO = FilterUpperFromQNodeTransformer.getFilterSelectsFromTree(
								extendedRootNode, true, false, personBean, locale, true);
						rootNodeOriginal = TreeFilterLoaderBL.getOriginalTree(extendedRootNode);
						if (FilterSelectsParametersUtil.containsParameter(originalFilterUpperTO)) {
							//set the request 
							//replace the corresponding query parameters with corresponding request parameters 
							try {
								FilterSelectsParametersUtil.replaceFilterSelectsParameters(
										originalFilterUpperTO, servletRequest, personBean, locale, false);
							} catch (NotExistingBeanException e) {
							}
						}
					}
				}
			}
		} else {
			if (filterID==null) {
				//from an eventually instantly changed filter already put into the session in the FilterAction
				//(even though filterID could be sent also in this case from the client it might be instantly modified)
				originalFilterUpperTO = (FilterUpperTO)session.get(SavedFilterExecuteAction.FILTER_UPPER_WITH_PARAM);
				rootNodeOriginal = (QNode)session.get(SavedFilterExecuteAction.TREE_WITH_PARAM);
			} else {
				//directly executed (not opened for edit)
				FilterFacade filterFacade = TreeFilterFacade.getInstance();
				TQueryRepositoryBean queryRepositoryBean = (TQueryRepositoryBean)filterFacade.getByKey(filterID);
				if (queryRepositoryBean!=null) {
					String filterExpression = FilterBL.getFilterExpression(queryRepositoryBean);
					QNode extendedRootNode = TreeFilterReader.getInstance().readQueryTree(filterExpression);
					originalFilterUpperTO = FilterUpperFromQNodeTransformer.getFilterSelectsFromTree(
						extendedRootNode, true, false, personBean, locale, true);
					rootNodeOriginal = TreeFilterLoaderBL.getOriginalTree(extendedRootNode);
				}
			}
		}
		if (originalFilterUpperTO==null && rootNodeOriginal==null) {
			//no parameters at all. Should never happen
			return null;
		}
		if (originalFilterUpperTO == null) {
			//in the original upper part was nothing selected.
			originalFilterUpperTO = new FilterUpperTO();
		}
		FilterUpperTO parameterFilterUpperTO = new FilterUpperTO();
		//1. prepare the parameterized upper selects
		Integer[] projects = FilterSelectsParametersUtil.prepareParameterFilterSelects(
				originalFilterUpperTO, parameterFilterUpperTO, personBean, locale);
		//2. prepare the parameterized FieldExpressionSimple fields in the upper part
		List<FieldExpressionSimpleTO> fieldExpressionSimpleListForUpperPart = originalFilterUpperTO.getFieldExpressionSimpleList();
		if (fieldExpressionSimpleListForUpperPart!=null) {
			for (Iterator<FieldExpressionSimpleTO> iterator = fieldExpressionSimpleListForUpperPart.iterator(); iterator.hasNext();) {
				FieldExpressionSimpleTO fieldExpressionSimpleTO = iterator.next();
				Integer matcherID = fieldExpressionSimpleTO.getSelectedMatcher();
				if (matcherID==null || !matcherID.equals(MatcherContext.PARAMETER)) {
					//leave only those with matchers set as parameter
					iterator.remove();
				} else {
					//get the matchers again, this time without $PARAMETER
					fieldExpressionSimpleTO.setFieldLabel(FieldRuntimeBL.getLocalizedDefaultFieldLabel(fieldExpressionSimpleTO.getField(), locale));				
					FieldExpressionBL.setMatchers(fieldExpressionSimpleTO, false, locale);
				}
			}
			parameterFilterUpperTO.setFieldExpressionSimpleList(fieldExpressionSimpleListForUpperPart);
		}
		//3. prepare the parameterized FieldExpressionSimple fields from the tree
		List<QNodeExpression> parameterizedNodes = new LinkedList<QNodeExpression>();
		QNodeParametersUtil.gatherNodesWithParameter(rootNodeOriginal, parameterizedNodes);	
		Set<Integer> parameterizedFields = new HashSet<Integer>();
		for (QNodeExpression qNodeExpression : parameterizedNodes) {
			parameterizedFields.add(qNodeExpression.getField());
		}
		Map<Integer, String> fieldLabelsMap = FieldRuntimeBL.getLocalizedDefaultFieldLabels(GeneralUtils.createListFromSet(parameterizedFields), locale);		
		List<FieldExpressionSimpleTO> fieldExpressionSimpleListFromTree = new LinkedList<FieldExpressionSimpleTO>();
		for (int i = 0; i < parameterizedNodes.size(); i++) {
			QNodeExpression qNodeExpression = parameterizedNodes.get(i);
			FieldExpressionSimpleTO fieldExpressionSimpleTO = new FieldExpressionSimpleTO(qNodeExpression);
			fieldExpressionSimpleListFromTree.add(FieldExpressionBL.loadFieldExpressionSimpleForInTreeParameter(fieldExpressionSimpleTO, 
					projects, originalFilterUpperTO.getSelectedIssueTypes(), personBean, locale, fieldLabelsMap, i));
		}
		boolean hasViewWatcherRightInAnyProject =
				FilterSelectsListsLoader.hasViewWatcherRightInAnyProject(personBean);
		return FilterJSON.getTreeFilterParametersJSON(projects, originalFilterUpperTO.getSelectedIssueTypes(), parameterFilterUpperTO,
				hasViewWatcherRightInAnyProject,
				fieldExpressionSimpleListFromTree, personBean.getObjectID(), locale);
		
	}
}
