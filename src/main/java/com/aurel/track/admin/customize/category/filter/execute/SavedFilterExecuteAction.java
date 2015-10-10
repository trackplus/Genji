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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.category.filter.FilterFacade;
import com.aurel.track.admin.customize.category.filter.QNode;
import com.aurel.track.admin.customize.category.filter.TreeFilterFacade;
import com.aurel.track.admin.customize.category.filter.parameters.FilterSelectsParametersUtil;
import com.aurel.track.admin.customize.category.filter.parameters.NotExistingBeanException;
import com.aurel.track.admin.customize.category.filter.parameters.QNodeParametersUtil;
import com.aurel.track.admin.customize.category.filter.tree.design.FieldExpressionInTreeTO;
import com.aurel.track.admin.customize.category.filter.tree.design.FieldExpressionSimpleTO;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterSubmitHandler;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperFromQNodeTransformer;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.TreeFilterLoaderBL;
import com.aurel.track.admin.customize.category.filter.tree.design.TreeFilterSaverBL;
import com.aurel.track.admin.customize.category.filter.tree.io.TreeFilterReader;
import com.aurel.track.admin.customize.category.filter.tree.io.TreeFilterWriter;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TQueryRepositoryBean.QUERY_PURPOSE;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.itemNavigator.ItemNavigatorBL;
import com.aurel.track.itemNavigator.QueryContext;
import com.aurel.track.itemNavigator.lastExecuted.LastExecutedBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeJSON;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.LocaleHandler;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * The action for executing saved issue filters (Tree and TQL)
 * The filter is not opened for editing before execute (not instantly modified) but may contain parameters 
 * @author Tamas Ruff
 *
 */
public class SavedFilterExecuteAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware/*, ServletRequestAware*/{
	private static final long serialVersionUID = 1;
	private static final Logger LOGGER = LogManager.getLogger(SavedFilterExecuteAction.class);
		
	private static final String FILTER_PARAMETERS = "filterParameters";
	private static final String LOGON = "logon";
	
	public static String FILTER_UPPER_WITH_PARAM = "filterUpperWithParam";
	public static String TREE_WITH_PARAM = "treeWithParam";
	 
	
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale;
	protected TPersonBean personBean;
	private boolean instant;
	//the ID of the currently edited/executed/deleted filter
	private Integer filterID;
	private Integer filterType;
	private Integer projectID;
	private Integer entityFlag;
	//the encoded query URL
	private String query;
	private Integer filterSubType;
	private FilterUpperTO filterUpperTO;
	//in simpleDisplayValueMap key is also string because for composite fields it is fieldID_parameterCode
	private Map<String, String> simpleDisplayValueMap;
	private Map<Integer, Integer> simpleMatcherRelationMap;
	private Map<String, String> inTreeDisplayValueMap;
	private Map<Integer, Integer> inTreeMatcherRelationMap;
	private Map<Integer, Integer> fieldMap;
	private Map<Integer, Integer> fieldMomentMap;
	private Map<Integer, Integer> operationMap;
	private Map<Integer, Integer> parenthesisOpenedMap;
	private Map<Integer, Integer> parenthesisClosedMap;
	private SortedMap<Integer, Integer> fieldExpressionOrderMap = new TreeMap<Integer, Integer>();
	private String tqlExpression;
	private boolean ajax;
	private boolean lite=false;
	private boolean forceAllItems;


	private boolean hasInitData=false;
	private String initData;
	//redirect to itemNavigator

	private String layoutCls="com.trackplus.layout.ItemNavigatorLayout";
	private String pageTitle="menu.findItems";

	public void prepare() throws Exception {
		personBean = (TPersonBean)session.get(Constants.USER_KEY);
		locale = (Locale)session.get(Constants.LOCALE_KEY);
	}
	/**
	 * Executes an encoded query
	 * @return
	 */
	public String executeEncodedQuery(){
		try {
			EncodedQueryCtx encodedQueryCtx=SavedFilterExecuteBL.executeEncodedQuery(query,session);
			if(encodedQueryCtx!=null){
				TPersonBean loggedPerson=encodedQueryCtx.getPersonBean();
				Locale loggedLocale=encodedQueryCtx.getLocale();
				QueryContext queryContext=encodedQueryCtx.getQueryContext();
				boolean keepMeLogged=encodedQueryCtx.isKeepMeLogged();
				if(keepMeLogged){
					LastExecutedBL.storeLastExecutedQuery(loggedPerson.getObjectID(), queryContext);
					session.put(Constants.USER_KEY,loggedPerson);
					session.put(Constants.LOCALE_KEY,loggedLocale);
					if (loggedLocale!=null && !loggedLocale.equals(locale)) {
						LocaleHandler.exportLocaleToSession(session, loggedLocale);
					}
					return "itemNavigator";
				}else{
					hasInitData=true;
					initData=ItemNavigatorBL.prepareInitData(ApplicationBean.getApplicationBean(),loggedPerson, loggedLocale, queryContext, session, true,null,null,forceAllItems,false);
					session.remove(Constants.USER_KEY);
					session.put(Constants.USER_KEY+"-permLink",loggedPerson);
					session.put(Constants.LOCALE_KEY+"-permLink",loggedLocale);
					session.put("localizationJSON", LocalizeJSON.encodeLocalization(loggedLocale));
					layoutCls="com.trackplus.layout.ItemNavigatorLiteLayout";
					return "itemNavigatorLite";
				}
			}else{
				return LOGON;
			}
		} catch (HasParametersException e) {
			return FILTER_PARAMETERS;
		} catch (NotLoggedException e) {
			String uri=ServletActionContext.getRequest().getRequestURI();
			String queryEncoded=null;
			try {
				queryEncoded=URLEncoder.encode(query, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				LOGGER.error(ExceptionUtils.getStackTrace(e1));  //To change body of catch statement use File | Settings | File Templates.
			}
			String forwardURL=uri+"?query="+ queryEncoded;
			session.put(Constants.POSTLOGINFORWARD,forwardURL);
			return LOGON;
		}
	}
			
	/**
	 * Whether the unwrapped filter contains parameter or not 
	 * @return
	 */
	public String unwrappedContainsParameter() {
		boolean containsParameter = false;
		if (filterSubType!=null && filterSubType.intValue()==QUERY_PURPOSE.TREE_FILTER) {
			if (filterUpperTO==null) {
				//nothing was submitted to selects
				filterUpperTO = new FilterUpperTO();
			}
			filterUpperTO.setFieldExpressionSimpleList(FilterSubmitHandler.createFieldExpressionSimpleListAfterSubmit(
					simpleMatcherRelationMap, simpleDisplayValueMap, locale));
			List<FieldExpressionInTreeTO> fieldExpressionsInTreeList =
				FilterSubmitHandler.createFieldExpressionInTreeListAfterSubmit(
						inTreeDisplayValueMap,
						inTreeMatcherRelationMap,
						fieldMap,
						fieldMomentMap,
						operationMap,
						parenthesisOpenedMap,
						parenthesisClosedMap,
						fieldExpressionOrderMap,
						locale);
			QNode rootNode = null;
			try {
				rootNode = TreeFilterSaverBL.transformExpressionListToTree(fieldExpressionsInTreeList, new Stack<QNode>());
				if (!instant) {
					//not instant but possibly instantly modified saved filters with parameters
					//(instant filters never have parameters)
					if (FilterSelectsParametersUtil.containsParameter(filterUpperTO) ||
							QNodeParametersUtil.containsParameter(rootNode))  {
						//even after being instantly edited it still contains unspecified parameters 
						containsParameter = true;
						session.put(SavedFilterExecuteAction.FILTER_UPPER_WITH_PARAM, filterUpperTO);
						session.put(SavedFilterExecuteAction.TREE_WITH_PARAM, rootNode);
					} else {
						//no parameters: redirect to item navigator after  saving
						String filterExpression = null;
						try {
							filterExpression = TreeFilterFacade.getInstance().getFilterExpression(null, filterUpperTO, fieldExpressionsInTreeList);
						} catch (Exception e) {
							String errorKey = e.getMessage();
							String errorMessage = LocalizeUtil.getLocalizedTextFromApplicationResources(errorKey, locale);
							LOGGER.warn("Transforming the instant query to expression failed with " + errorMessage);
						}
						QueryContext queryContext = new QueryContext();
						queryContext.setQueryType(ItemNavigatorBL.QUERY_TYPE.INSTANT);
						queryContext.setQueryID(filterSubType);
						queryContext.setFilterExpression(filterExpression);
						LastExecutedBL.storeLastExecutedQuery(personBean.getObjectID(), queryContext);
					}
				}
			} catch (Exception e) {
				String errorMessage = LocalizeUtil.getLocalizedTextFromApplicationResources(e.getMessage(), locale);
				JSONUtility.encodeJSON(servletResponse,  JSONUtility.encodeJSONFailure(errorMessage));
				return null;
			}
		}
		JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONBoolean(containsParameter));
		return null;
	}
	
	/**
	 * Executes a "saved but possibly instantly modified" filter without parameters or an "instant" filter
	 */
	public String executeUnwrapped() {
		String filterExpression = null;
		if (filterSubType!=null) {
			switch (filterSubType.intValue()) {
			case QUERY_PURPOSE.TREE_FILTER:
				if (filterUpperTO==null) {
					//nothing was submitted to selects
					filterUpperTO = new FilterUpperTO();
				}
				filterUpperTO.setFieldExpressionSimpleList(FilterSubmitHandler.createFieldExpressionSimpleListAfterSubmit(
						simpleMatcherRelationMap, simpleDisplayValueMap, locale));
				List<FieldExpressionInTreeTO> fieldExpressionsInTreeList =
					FilterSubmitHandler.createFieldExpressionInTreeListAfterSubmit(
							inTreeDisplayValueMap,
							inTreeMatcherRelationMap,
							fieldMap,
							fieldMomentMap,
							operationMap,
							parenthesisOpenedMap,
							parenthesisClosedMap,
							fieldExpressionOrderMap,
							locale);
				try {
					filterExpression = TreeFilterFacade.getInstance().getFilterExpression(null, filterUpperTO, fieldExpressionsInTreeList);
				} catch (Exception e) {
					String errorKey = e.getMessage();
					String errorMessage = LocalizeUtil.getLocalizedTextFromApplicationResources(errorKey, locale);
					LOGGER.warn("Transforming the instant query to expression failed with " + errorMessage);
					JSONUtility.encodeJSONFailure(servletResponse, errorMessage);
				}
				break;
			case QUERY_PURPOSE.TQLPLUS_FILTER:
			case QUERY_PURPOSE.TQL_FILTER:
				filterExpression = tqlExpression;
				break;
			}
		}
		QueryContext queryContext = new QueryContext();
		queryContext.setQueryType(ItemNavigatorBL.QUERY_TYPE.INSTANT);
		queryContext.setQueryID(filterSubType);
		queryContext.setFilterExpression(filterExpression);
		LastExecutedBL.storeLastExecutedQuery(personBean.getObjectID(), queryContext);
		if(ajax){
			JSONUtility.encodeJSONSuccess(servletResponse);
			return null;
		}else{
			return "itemNavigator";
		}
	}
	
	/**
	 * Saves the instant filter settings into the session
	 */
	public String applyInstant() {
		if (filterUpperTO==null) {
			//nothing was submitted to selects
			filterUpperTO = new FilterUpperTO();
		}
		filterUpperTO.setFieldExpressionSimpleList(FilterSubmitHandler.createFieldExpressionSimpleListAfterSubmit(
				simpleMatcherRelationMap, simpleDisplayValueMap, locale));
		List<FieldExpressionInTreeTO> fieldExpressionsInTreeListExecute =
			FilterSubmitHandler.createFieldExpressionInTreeListAfterSubmit(
					inTreeDisplayValueMap,
					inTreeMatcherRelationMap,
					fieldMap,
					fieldMomentMap,
					operationMap,
					parenthesisOpenedMap,
					parenthesisClosedMap,
					fieldExpressionOrderMap,
					locale);
		/*List<FieldExpressionInTreeTO> fieldExpressionsInTreeListEdit =
				FilterSubmitHandler.createFieldExpressionInTreeListAfterSubmit(
						inTreeDisplayValueMap,
						inTreeMatcherRelationMap,
						fieldMap,
						fieldMomentMap,
						operationMap,
						parenthesisOpenedMap,
						parenthesisClosedMap,
						fieldExpressionOrderMap,
						locale);*/
		/**
		 * transform the custom selections strings to Integer[] because struts can't submit to Map<Integer, Integer[]>
		 */
		Map<Integer, Integer> customSelectSimpleFields = new HashMap<Integer, Integer>();
		Map<Integer, String> selectedCustomSelectsStr = filterUpperTO.getSelectedCustomSelectsStr();
		Map<Integer, Integer[]> selectedCustomSelects = filterUpperTO.getSelectedCustomSelects();
		if (selectedCustomSelectsStr!=null) {
			if (selectedCustomSelects==null) {
				selectedCustomSelects = new HashMap<Integer, Integer[]>();
				filterUpperTO.setSelectedCustomSelects(selectedCustomSelects);
			}
			for (Integer fieldID : selectedCustomSelectsStr.keySet()) {
				String selections = selectedCustomSelectsStr.get(fieldID);
				if (selections!=null) {
					selectedCustomSelects.put(fieldID, FilterUpperTO.createIntegerArrFromString(selections));
				}
				customSelectSimpleFields.put(fieldID, FieldBL.getSystemOptionType(fieldID));
			}
			filterUpperTO.setCustomSelectSimpleFields(customSelectSimpleFields);
		}
		session.put(FilterBL.FILTER_UPPER_APPLY_EXECUTE + filterID + "_" + filterType, filterUpperTO);
		//FilterUpperTO filterUpperTOForEdit = filterUpperTO.copy();
		//session.put(FilterBL.FILTER_UPPER_APPLY_EDIT + filterID + "_" + filterType, filterUpperTOForEdit);
		try {
			QNode rootNode = TreeFilterSaverBL.transformExpressionListToTree(fieldExpressionsInTreeListExecute, new Stack<QNode>());
			if (rootNode!=null) {
				session.put(FilterBL.TREE_APPLY_EXECUTE + filterID + "_" + filterType, rootNode);
			} else {
				session.remove(FilterBL.TREE_APPLY_EXECUTE + filterID + "_" + filterType);
			}
 		} catch (Exception e) {
			String errorKey = e.getMessage();
			String errorMessage = LocalizeUtil.getLocalizedTextFromApplicationResources(errorKey, locale);
			LOGGER.warn("Transforming the instant query to expression failed with " + errorMessage);
			JSONUtility.encodeJSONFailure(servletResponse, errorMessage);
		}
		/*try {
			QNode rootNode = TreeFilterSaverBL.transformExpressionListToTree(fieldExpressionsInTreeListEdit, new Stack<QNode>());
			if (rootNode!=null) {
				session.put(FilterBL.TREE_APPLY_EDIT + filterID + "_" + filterType, rootNode);
			}
 		} catch (Exception e) {
			String errorKey = e.getMessage();
			String errorMessage = LocalizeUtil.getLocalizedTextFromApplicationResources(errorKey, locale);
			LOGGER.warn("Transforming the instant query to expression failed with " + errorMessage);
			JSONUtility.encodeJSONFailure(servletResponse, errorMessage);
		}*/
		JSONUtility.encodeJSONSuccess(servletResponse);	
		return null;
	}
	
	/**
	 * Replace the specified parameters from the submitted parameters and then execute the filter  
	 * The filter can be an encoded query a saved query (directly executed or after a possible instant modification) 
	 * @return
	 */
	public String replaceSubmittedParameters() {
		FilterUpperTO originalFilterUpperTO=null;
		QNode rootNodeOriginal=null;
		if (query!=null && query.length()>0) {
			//parameters for encoded query URL
			String linkReport = ReportQueryBL.b(query);
			Map<String,String>  queryEncodedMap = ReportQueryBL.decodeMapFromUrl(linkReport);
			String queryIDStr= queryEncodedMap.get("queryID");
			Integer filterID=null;
			if (queryIDStr!=null){
				filterID=Integer.decode(queryIDStr);
				if (filterID!=null) {
					TQueryRepositoryBean queryRepositoryBean = 
							(TQueryRepositoryBean)TreeFilterFacade.getInstance().getByKey(filterID);
					if (queryRepositoryBean!=null) {
						String filterExpression = FilterBL.getFilterExpression(queryRepositoryBean);
						HttpServletRequest request = null;
						QNode extendedRootNode = TreeFilterReader.getInstance().readQueryTree(filterExpression);
						originalFilterUpperTO = FilterUpperFromQNodeTransformer.getFilterSelectsFromTree(
								extendedRootNode, true, true, personBean, locale, true);
						rootNodeOriginal = TreeFilterLoaderBL.getOriginalTree(extendedRootNode);
						if (FilterSelectsParametersUtil.containsParameter(originalFilterUpperTO)) {
							//set the request 
							request = ServletActionContext.getRequest();
							//replace the corresponding query parameters with corresponding request parameters 
							try {
								FilterSelectsParametersUtil.replaceFilterSelectsParameters(
										originalFilterUpperTO, request, personBean, locale, false);
							} catch (NotExistingBeanException e) {
							}
						}
					}
				}
			}
		} else {
			if (filterID==null) {
				//from an eventually "instantly" modified filter (put into the session in the FilterAction)
				originalFilterUpperTO = (FilterUpperTO)session.get(FILTER_UPPER_WITH_PARAM);
				rootNodeOriginal = (QNode)session.get(TREE_WITH_PARAM);
			} else {
				//directly executed (not opened for edit)
				FilterFacade filterFacade = TreeFilterFacade.getInstance();
				TQueryRepositoryBean queryRepositoryBean = (TQueryRepositoryBean)filterFacade.getByKey(filterID);
				if (queryRepositoryBean!=null) {
					String filterExpression = FilterBL.getFilterExpression(queryRepositoryBean);
					QNode extendedRootNode = TreeFilterReader.getInstance().readQueryTree(filterExpression);
					originalFilterUpperTO = FilterUpperFromQNodeTransformer.getFilterSelectsFromTree(
						extendedRootNode, true, true, personBean, locale, true);
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
		if (filterUpperTO==null) {
			//nothing was submitted to parameterized selects (or none of the selects were parameterized)
			filterUpperTO = new FilterUpperTO();
		}
		//1. replace the upper select parameters  
		FilterUpperTO replacedFilterSelectsTO = FilterSelectsParametersUtil.replaceParameters(
				originalFilterUpperTO, filterUpperTO, locale);
		
		//2. replace the matchers and values in FieldExpressionSimple fields
		List<FieldExpressionSimpleTO> originalFieldExpressionSimpleList = originalFilterUpperTO.getFieldExpressionSimpleList();
		List<FieldExpressionSimpleTO> parameterizedFieldExpressionSimpleList = FilterSubmitHandler.createFieldExpressionSimpleListAfterSubmit(
				simpleMatcherRelationMap, simpleDisplayValueMap, locale);
		Map<Integer, FieldExpressionSimpleTO> fieldBasedFieldExpressionSimpleMap = new HashMap<Integer, FieldExpressionSimpleTO>();
		for (FieldExpressionSimpleTO fieldExpressionSimpleTO : parameterizedFieldExpressionSimpleList) {
			Integer fieldID = fieldExpressionSimpleTO.getField();
			fieldBasedFieldExpressionSimpleMap.put(fieldID, fieldExpressionSimpleTO);
		}
		if (originalFieldExpressionSimpleList!=null) {
			for (FieldExpressionSimpleTO fieldExpressionSimpleTO : originalFieldExpressionSimpleList) {
				Integer fieldID = fieldExpressionSimpleTO.getField();
				Integer matcherID = fieldExpressionSimpleTO.getSelectedMatcher();
				if (fieldID!=null && matcherID!=null && matcherID.equals(MatcherContext.PARAMETER)) {
					FieldExpressionSimpleTO parameterFieldExpressionSimpleTO = fieldBasedFieldExpressionSimpleMap.get(fieldID);
					if (parameterFieldExpressionSimpleTO!=null) {
						fieldExpressionSimpleTO.setSelectedMatcher(parameterFieldExpressionSimpleTO.getSelectedMatcher());
						fieldExpressionSimpleTO.setValue(parameterFieldExpressionSimpleTO.getValue());
					}
				}
			}
		}
		//3. replace the matchers and values in the tree part
		List<FieldExpressionSimpleTO> treePartFieldExpressionSimpleList =
			FilterSubmitHandler.createFieldExpressionSimpleListAfterSubmit(fieldMap,
				inTreeMatcherRelationMap, inTreeDisplayValueMap, locale);
		QNode replacedTree = QNodeParametersUtil.replaceParameters(
				rootNodeOriginal, treePartFieldExpressionSimpleList);
		String filterExpression = null;
		try {
			QNode extendedNode = TreeFilterSaverBL.createQNodeWithQueryListsTO(replacedTree, replacedFilterSelectsTO, null);
			filterExpression = TreeFilterWriter.getInstance().toXML(extendedNode);
		} catch (Exception e) {
			String errorKey = e.getMessage();
			String errorMessage = LocalizeUtil.getLocalizedTextFromApplicationResources(errorKey, locale);
			LOGGER.warn("Transforming the instant query to expression failed with " + errorMessage);
		}
		QueryContext queryContext=new QueryContext();
		//although a parameterized filter is a saved filter, it is saved in QueryContext as INSTANT_REPORT_FILTER 
		//independently whether it was executed from edit mode (possibly "instantly" modified)  
		//or directly because the parameters have to be replaced anyway and then the resulting expression should be saved in QueryContext   
		queryContext.setQueryType(ItemNavigatorBL.QUERY_TYPE.INSTANT);
		queryContext.setQueryID(TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER);
		queryContext.setFilterExpression(filterExpression);
		LastExecutedBL.storeLastExecutedQuery(personBean.getObjectID(), queryContext);
		if(ajax){
			JSONUtility.encodeJSONSuccess(servletResponse);
			return null;
		}else{
			return "itemNavigator";
		}
	}
	
	
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	public void setFilterID(Integer filterID) {
		this.filterID = filterID;
	}
	
	public void setFilterType(Integer filterType) {
		this.filterType = filterType;
	}
	public Integer getProjectID() {
		return projectID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public Integer getEntityFlag() {
		return entityFlag;
	}

	public void setEntityFlag(Integer entityFlag) {
		this.entityFlag = entityFlag;
	}
	
	/**
	 * Needed in struts.xml for redirect
	 * @return
	 */
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	
	public FilterUpperTO getFilterUpperTO() {
		return filterUpperTO;
	}

	public void setFilterUpperTO(FilterUpperTO filterUpperTO) {
		this.filterUpperTO = filterUpperTO;
	}

	public Map<String, String> getSimpleDisplayValueMap() {
		return simpleDisplayValueMap;
	}

	public void setSimpleDisplayValueMap(Map<String, String> simpleDisplayValueMap) {
		this.simpleDisplayValueMap = simpleDisplayValueMap;
	}

	public Map<Integer, Integer> getSimpleMatcherRelationMap() {
		return simpleMatcherRelationMap;
	}

	public void setSimpleMatcherRelationMap(
			Map<Integer, Integer> simpleMatcherRelationMap) {
		this.simpleMatcherRelationMap = simpleMatcherRelationMap;
	}

	public Map<String, String> getInTreeDisplayValueMap() {
		return inTreeDisplayValueMap;
	}

	public void setInTreeDisplayValueMap(Map<String, String> inTreeDisplayValueMap) {
		this.inTreeDisplayValueMap = inTreeDisplayValueMap;
	}

	public Map<Integer, Integer> getInTreeMatcherRelationMap() {
		return inTreeMatcherRelationMap;
	}

	public Map<Integer, Integer> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map<Integer, Integer> fieldMap) {
		this.fieldMap = fieldMap;
	}

	public Map<Integer, Integer> getFieldMomentMap() {
		return fieldMomentMap;
	}

	public void setFieldMomentMap(Map<Integer, Integer> fieldMomentMap) {
		this.fieldMomentMap = fieldMomentMap;
	}

	public Map<Integer, Integer> getOperationMap() {
		return operationMap;
	}

	public void setOperationMap(Map<Integer, Integer> operationMap) {
		this.operationMap = operationMap;
	}

	public Map<Integer, Integer> getParenthesisOpenedMap() {
		return parenthesisOpenedMap;
	}

	public void setParenthesisOpenedMap(Map<Integer, Integer> parenthesisOpenedMap) {
		this.parenthesisOpenedMap = parenthesisOpenedMap;
	}

	public Map<Integer, Integer> getParenthesisClosedMap() {
		return parenthesisClosedMap;
	}

	public void setParenthesisClosedMap(Map<Integer, Integer> parenthesisClosedMap) {
		this.parenthesisClosedMap = parenthesisClosedMap;
	}

	public void setTqlExpression(String tqlExpression) {
		this.tqlExpression = tqlExpression;
	}

	public void setInTreeMatcherRelationMap(
			Map<Integer, Integer> inTreeMatcherRelationMap) {
		this.inTreeMatcherRelationMap = inTreeMatcherRelationMap;
	}


	public void setFilterSubType(Integer filterSubType) {
		this.filterSubType = filterSubType;
	}
	public void setInstant(boolean instant) {
		this.instant = instant;
	}

	public SortedMap<Integer, Integer> getFieldExpressionOrderMap() {
		return fieldExpressionOrderMap;
	}

	public void setFieldExpressionOrderMap(
			SortedMap<Integer, Integer> fieldExpressionOrderMap) {
		this.fieldExpressionOrderMap = fieldExpressionOrderMap;
	}	


	public void setAjax(boolean ajax) {
		this.ajax = ajax;
	}

	public boolean isLite() {
		return lite;
	}
	public boolean isHasInitData() {
		return hasInitData;
	}
	public String getInitData() {
		return initData;
	}

	public void setForceAllItems(boolean forceAllItems) {
		this.forceAllItems = forceAllItems;
	}

	public String getLayoutCls() {
		return layoutCls;
	}

	public String getPageTitle() {
		return pageTitle;
	}
}
