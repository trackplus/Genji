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

package com.aurel.track.admin.customize.category.filter.execute;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.category.filter.QNode;
import com.aurel.track.admin.customize.category.filter.TreeFilterFacade;
import com.aurel.track.admin.customize.category.filter.parameters.FilterSelectsParametersUtil;
import com.aurel.track.admin.customize.category.filter.parameters.NotExistingBeanException;
import com.aurel.track.admin.customize.category.filter.parameters.QNodeParametersUtil;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperFromQNodeTransformer;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.TreeFilterLoaderBL;
import com.aurel.track.admin.customize.category.filter.tree.design.TreeFilterSaverBL;
import com.aurel.track.admin.customize.category.filter.tree.io.TreeFilterReader;
import com.aurel.track.admin.customize.category.filter.tree.io.TreeFilterWriter;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TQueryRepositoryBean.QUERY_PURPOSE;
import com.aurel.track.itemNavigator.ItemNavigatorBL;
import com.aurel.track.itemNavigator.QueryContext;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.IntegerStringBean;

/**
 * BL for executing a saved filter
 * @author Tamas
 *
 */
public class SavedFilterExecuteBL {
	
	private static final Logger LOGGER = LogManager.getLogger(SavedFilterExecuteBL.class);
	
	private static final int NO_PARAMETER = 1;
	private static final int CONTAINS_PARAMETER_ORIGINAL = 2;
	private static final int CONTAINS_PARAMETER_AFTER_REQUEST_REPLACE = 3;
	
	/**
	 * Execute a saved filter
	 * @return
	 */
	static IntegerStringBean encodedQueryContainsNotSpecifiedParameter(
			String query, TPersonBean personBean, Locale locale) {
		IntegerStringBean integerStringBean = new IntegerStringBean(null, Integer.valueOf(NO_PARAMETER));
		//boolean keepMeLogged=false;
		if (query!=null && query.length()>0) {
			String linkReport = ReportQueryBL.b(query);
			Map<String,String> queryEncodedMap = ReportQueryBL.decodeMapFromUrl(linkReport);
			//String keepMeLoggedStr = queryEncodedMap.get("keepMeLogged");
			//keepMeLogged=keepMeLoggedStr!=null && keepMeLoggedStr.equalsIgnoreCase("true");
			boolean clear = false; 
			String queryIDStr= queryEncodedMap.get("queryID");
			Integer filterID=null;
			if(queryIDStr!=null){
				filterID=Integer.decode(queryIDStr);
				if (filterID!=null) {
					TQueryRepositoryBean queryRepositoryBean = 
							(TQueryRepositoryBean)TreeFilterFacade.getInstance().getByKey(filterID);
					if (queryRepositoryBean!=null) {
						Integer queryType = queryRepositoryBean.getQueryType();
						String filterExpression = FilterBL.getFilterExpression(queryRepositoryBean);
						if (queryType!=null) {
							switch (queryType.intValue()) {
							case QUERY_PURPOSE.TREE_FILTER:
								HttpServletRequest request = null;
								QNode extendedRootNode = TreeFilterReader.getInstance().readQueryTree(filterExpression);
								FilterUpperTO filterUpperTO = FilterUpperFromQNodeTransformer.getFilterSelectsFromTree(
										extendedRootNode, true, true, personBean, locale, true);
								QNode rootNode = TreeFilterLoaderBL.getOriginalTree(extendedRootNode);
								boolean hasListWithParameter = false;
								
								if (FilterSelectsParametersUtil.containsParameter(filterUpperTO)) {
									hasListWithParameter = true;
									//replace the corresponding query parameters with corresponding request parameters
									request = ServletActionContext.getRequest();
									integerStringBean.setValue(Integer.valueOf(CONTAINS_PARAMETER_ORIGINAL));
									try {
										//uncomment the "if" (and keepMeLogged initialization above) if we want to handle the case when
										//by executing a parameterized query the not all parameters are specified in as request parameters
										//and the user should be prompted to a new page to specify the parameters
										//this new page would mean a new tile (the track+ system menus should not be available)
											clear = true;
	
										FilterSelectsParametersUtil.replaceFilterSelectsParameters(
												filterUpperTO, request, personBean, locale, clear);
									} catch (NotExistingBeanException e) {
										LOGGER.warn(e.getMessage(), e);
									}
								}
								if ((FilterSelectsParametersUtil.containsParameter(filterUpperTO) ||
										QNodeParametersUtil.containsParameter(rootNode))) {
									//if there are parameters from an encoded query but with keepMeLogged
									//and not all parameters were set with request parameters
									//go to parameters specifying page
									integerStringBean.setValue(Integer.valueOf(CONTAINS_PARAMETER_AFTER_REQUEST_REPLACE));
								} else {
									if (hasListWithParameter) {
										try {
											QNode extendedNode = TreeFilterSaverBL.createQNodeWithQueryListsTO(rootNode, filterUpperTO, locale);
											filterExpression = TreeFilterWriter.getInstance().toXML(extendedNode);
										} catch (Exception e) {
											String errorKey = e.getMessage();
											String errorMessage = LocalizeUtil.getLocalizedTextFromApplicationResources(errorKey, locale);
											LOGGER.warn("Transforming the instant query to expression failed with " + errorMessage);
										}
										integerStringBean.setLabel(filterExpression);
									}
								}
								break;
							case QUERY_PURPOSE.TQLPLUS_FILTER:
							case QUERY_PURPOSE.TQL_FILTER:
								return integerStringBean;
							}
						}
					}
				}
			}
		}
		return integerStringBean;
	}
	
	/**
	 * Get the personBean by login name 
	 * @param user
	 * @param pswd
	 * @return
	 */
	static TPersonBean getByLoginName(String user, String pswd){
		TPersonBean personBean = PersonBL.loadByLoginName(user);
		if (personBean!=null && pswd.equals(personBean.getPasswd())) {
			return personBean;
		} else {
			//try guest login
			if(ApplicationBean.getInstance().getSiteBean().getAutomaticGuestLogin()){
				return PersonBL.getAnonymousIfActive();
			}
		}
		return null;
	}
	
	/**
	 * Executes an encoded query
	 * @return
	 */
	static EncodedQueryCtx executeEncodedQuery(String query,  Map<String, Object> session) throws NotLoggedException, HasParametersException {
		EncodedQueryCtx encodedQueryCtx=new EncodedQueryCtx();
		boolean keepMeLogged=false;
		TPersonBean personBean;
		Locale locale;
		if (query!=null && query.length()>0) {
			String linkReport = ReportQueryBL.b(query);
			Map<String,String> queryEncodedMap = ReportQueryBL.decodeMapFromUrl(linkReport);
			String user = queryEncodedMap.get("user");
			String pswd = queryEncodedMap.get("pswd");
			String keepMeLoggedStr = queryEncodedMap.get("keepMeLogged");
			if(user==null){
				personBean=(TPersonBean) session.get(Constants.USER_KEY);
				keepMeLogged=true;
			}else{
				keepMeLogged=keepMeLoggedStr!=null && keepMeLoggedStr.equalsIgnoreCase("true");
				personBean = SavedFilterExecuteBL.getByLoginName(user, pswd);
			}
			if(keepMeLogged){
				session.put("forceLoggoffAfterReportOverview", Boolean.FALSE);
			}else{
				session.put("forceLoggoffAfterReportOverview", Boolean.TRUE);
			}
			if (personBean==null){
				throw new NotLoggedException();
			}
			locale=personBean.getLocale();
			String queryIDStr= queryEncodedMap.get("queryID");
			Integer filterID=null;
			if(queryIDStr!=null){
				filterID=Integer.decode(queryIDStr);
			}
			IntegerStringBean result = encodedQueryContainsNotSpecifiedParameter(query, personBean, locale);
			QueryContext queryContext=new QueryContext();
			switch (result.getValue()) {
			case NO_PARAMETER:
				queryContext=new QueryContext();
				queryContext.setQueryID(filterID);
				queryContext.setQueryType(ItemNavigatorBL.QUERY_TYPE.SAVED);
				encodedQueryCtx.setKeepMeLogged(keepMeLogged);
				encodedQueryCtx.setParametrized(false);
				encodedQueryCtx.setPersonBean(personBean);
				encodedQueryCtx.setLocale(locale);
				encodedQueryCtx.setQueryContext(queryContext);
				return encodedQueryCtx;
			case CONTAINS_PARAMETER_AFTER_REQUEST_REPLACE:
				//throw new HasParametersException();
			case CONTAINS_PARAMETER_ORIGINAL:
				queryContext=new QueryContext();
				//although a parameterized filter is a saved filter, it is saved in QueryContext as INSTANT_REPORT_FILTER
				//independently whether it was executed from edit mode (possibly "instantly" modified)
				//or directly because the parameters have to be replaced anyway and then the resulting expression should be saved in QueryContext
				queryContext.setQueryType(ItemNavigatorBL.QUERY_TYPE.INSTANT);
				queryContext.setQueryID(TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER);
				queryContext.setFilterExpression(result.getLabel());
				encodedQueryCtx.setKeepMeLogged(keepMeLogged);
				encodedQueryCtx.setParametrized(false);
				encodedQueryCtx.setPersonBean(personBean);
				encodedQueryCtx.setLocale(locale);
				encodedQueryCtx.setQueryContext(queryContext);
				return encodedQueryCtx;
			}			
		}
		return null;
	}

}
