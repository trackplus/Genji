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

package com.aurel.track.admin.customize.category.filter.execute;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.CategoryTokens;
import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.category.filter.FilterJSON;
import com.aurel.track.admin.customize.category.filter.QNode;
import com.aurel.track.admin.customize.category.filter.TreeFilterFacade;
import com.aurel.track.admin.customize.category.filter.parameters.FilterSelectsParametersUtil;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperFromQNodeTransformer;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TQueryRepositoryBean.QUERY_PURPOSE;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.IntegerStringBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * The action for generating permanent links for saved filters. The filter might be parameterized
 * @author Tamas Ruff
 *
 */
public class FilterLinkAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware{
	private static final long serialVersionUID = 1;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale;
	protected TPersonBean personBean;
	private String node;
	private static final Logger LOGGER = LogManager.getLogger(FilterLinkAction.class);

	@Override
	public void prepare() throws Exception {
		personBean = (TPersonBean)session.get(Constants.USER_KEY);
		locale = (Locale)session.get(Constants.LOCALE_KEY);
	}

	/**
	 * Generates an encoded filter URL
	 * @return
	 */
	@Override
	public String execute() {
		final String ENCODING_TYPE = "UTF-8";
		CategoryTokens categoryTokens = CategoryTokens.decodeNode(node);
		Integer filterID = categoryTokens.getObjectID();
		String baseUrlReport=Constants.getBaseURL()+"/encodedQuery.action?query=";
		String baseUrlMaven=Constants.getBaseURL()+"/xml/report?query=";
		Map<String,String> mapParams=new HashMap<String, String>();
		mapParams.put("queryID", filterID.toString());
		String queryIDEncoded=ReportQueryBL.a(ReportQueryBL.encodeMapAsUrl(mapParams));
		try {
			queryIDEncoded=URLEncoder.encode(queryIDEncoded, ENCODING_TYPE);
		} catch (UnsupportedEncodingException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		mapParams.put("user", personBean.getLoginName());
		mapParams.put("pswd", personBean.getPasswd());
		String paramsEncoded=ReportQueryBL.a(ReportQueryBL.encodeMapAsUrl(mapParams));
		try {
			paramsEncoded=URLEncoder.encode(paramsEncoded,ENCODING_TYPE);
		} catch (UnsupportedEncodingException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		//keep logged
		mapParams.put("keepMeLogged", "true");
		String paramsKeepEncoded=ReportQueryBL.a(ReportQueryBL.encodeMapAsUrl(mapParams));
		try {
			paramsKeepEncoded=URLEncoder.encode(paramsKeepEncoded, ENCODING_TYPE);
		} catch (UnsupportedEncodingException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		TQueryRepositoryBean queryRepositoryBean = (TQueryRepositoryBean)TreeFilterFacade.getInstance().getByKey(filterID);
		Integer queryType = queryRepositoryBean.getQueryType();
		String filterParams = null;
		if (queryType!=null && queryType.intValue()==QUERY_PURPOSE.TREE_FILTER) {
			QNode extendedRootNode = FilterBL.loadNode(queryRepositoryBean);
			FilterUpperTO filterUpperTO = FilterUpperFromQNodeTransformer.getFilterSelectsFromTree(extendedRootNode, true, true, personBean, locale, true);
			List<IntegerStringBean> parametrizedFields=FilterSelectsParametersUtil.getParameterizedFields(filterUpperTO);
			if(parametrizedFields!=null && !parametrizedFields.isEmpty()){
				StringBuffer sb=new StringBuffer();
				for (int i = 0; i < parametrizedFields.size(); i++) {
					if(i>0){
						sb.append("&");
					}
					sb.append(parametrizedFields.get(i).getLabel());
					sb.append("=");
				}
				filterParams=sb.toString();
			}
		}
		String encodedFilterUrlIssueNavigatorNoUser=baseUrlReport+queryIDEncoded;
		String encodedFilterUrlIssueNavigator=baseUrlReport+paramsEncoded;
		String encodedFilterUrlIssueNavigatorKeep=baseUrlReport+paramsKeepEncoded;
		String encodedFilterUrlMavenPlugin=baseUrlMaven+paramsEncoded;
		JSONUtility.encodeJSON(servletResponse, FilterJSON.getFilterLinkJSON(encodedFilterUrlIssueNavigatorNoUser,
				encodedFilterUrlIssueNavigator, encodedFilterUrlIssueNavigatorKeep, encodedFilterUrlMavenPlugin, filterParams));
		return null;
	}


	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setNode(String node) {
		this.node = node;
	}

}
