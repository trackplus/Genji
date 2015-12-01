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


package com.aurel.track.tql;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanQuery;
import org.apache.torque.util.Criteria;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.criteria.CriteriaUtil;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.errors.ErrorParameter;
import com.aurel.track.lucene.search.LuceneSearcher;
import com.aurel.track.tql.exceptions.TqlCriteriaStackIsNotEmptyException;
import com.aurel.track.tql.exceptions.TqlDateFormatException;
import com.aurel.track.tql.exceptions.TqlJoinSchemaClassNotFoundException;
import com.aurel.track.tql.exceptions.TqlJoinSchemaFieldNotFoundException;
import com.aurel.track.tql.exceptions.TqlJoinSchemaXmlNotFoundException;
import com.aurel.track.tql.exceptions.TqlJoinSchemaXmlParseException;
import com.aurel.track.tql.exceptions.TqlNumberFormatException;
import com.aurel.track.tql.exceptions.TqlUnknownAliasException;
import com.aurel.track.tql.exceptions.TqlUnknownOperatorException;
import com.aurel.track.tql.exceptions.TqlUnknownValueException;
import com.aurel.track.tql.interpreter.TqlInterpreter;
import com.aurel.track.tql.parser.SimpleNode;
import com.aurel.track.tql.parser.TokenMgrError;
import com.aurel.track.tql.parser.TqlParser;

/**
 * Business logic helper for getting the results of a TQL or a TQLPlus (lucene) filter
 */
public class TqlBL {
	private static TqlJoinSchema joinSchema;
	private static final Logger LOGGER = LogManager.getLogger (TqlBL.class);
	
	/**
	 * Return the found workItemIDs as a result of the lucene search
	 * @param searchString
	 * @param external whether track+ item search or wiki search
	 * @param locale
	 * @param highlightedTextMap
	 * @param errors
	 * @return
	 */
	public static int[] luceneQuery(String searchString, boolean external, Locale locale, Map<Integer, String> highlightedTextMap, List<ErrorData> errors) {
		int[] workItemIDs = null;
		try {
			workItemIDs = LuceneSearcher.searchWorkItems(searchString, external, locale, highlightedTextMap);
		} catch (BooleanQuery.TooManyClauses e) {
			ErrorData rd=new ErrorData("item.err.luceneTooManyClauses", e.getMessage());
			errors.add(rd);
			return workItemIDs;
		} catch (ParseException e) {
			//error by parsing the lucene query
			ErrorData rd=new ErrorData("item.err.luceneParseError", e.getMessage());
			errors.add(rd);
			return workItemIDs;
		}
		//no item found
		if (workItemIDs==null || workItemIDs.length==0){
			errors.add(new ErrorData("report.reportError.error.noWorkItem"));
		}
		return workItemIDs;
	}

	public static Criteria createCriteria(String tqlQueryString, TPersonBean user, Locale locale, List<ErrorData> errors){
		if(joinSchema==null){
			initTqlJoinSchema(errors);
		}
		// Parse the queryString and create an abstract syntax tree for the given TQL statement.
		SimpleNode tqlTree;
		TqlParser parser = new TqlParser (new StringReader(tqlQueryString));
		try{
			tqlTree = parser.start();
			if (LOGGER.isDebugEnabled ()){
				LOGGER.debug (tqlTree.dumpGraphViz());
			}
		}
		catch (com.aurel.track.tql.parser.ParseException e){
			LOGGER.warn(e);
			errors.add(new ErrorData("report.reportQueryTql.query.err.queryString.syntax",e.getMessage()));
			return null;
		}
		catch (TokenMgrError e){
			LOGGER.warn (e);
			errors.add(new ErrorData("report.reportQueryTql.query.err.queryString.syntax",e.getMessage()));
			return null;
		}

		// Now traverse the abstract syntax tree of the TQL statement and build the corresponding Criteria object.
		Criteria tqlCriteria = null;
		TqlInterpreter interpreter = new TqlInterpreter (joinSchema, user);
		try	{
			tqlCriteria = interpreter.start(tqlTree, locale);
		}
		catch (TqlCriteriaStackIsNotEmptyException e){
			LOGGER.warn (e);
			errors.add(new ErrorData("report.reportQueryTql.query.err.criteriaStackIsNotEmpty"));
			return null;
		}
		catch (TqlUnknownAliasException e){
			LOGGER.warn (e);
			errors.add(new ErrorData("report.reportQueryTql.query.err.unknownAlias",e.getMessage()));
			return null;
		}
		catch (TqlUnknownValueException e){
			LOGGER.warn (e);
			ErrorData rd=new ErrorData("report.reportQueryTql.query.err.unknownValue");
			List<ErrorParameter> params=new ArrayList<ErrorParameter>();
			params.add(new ErrorParameter(false,e.getValue()));
			params.add(new ErrorParameter(false,e.getAlias()));
			rd.setResourceParameters(params);
			errors.add(rd);
			return null;
		}
		catch (TqlDateFormatException e){
			LOGGER.warn (e);
			errors.add(new ErrorData("report.reportQueryTql.query.err.dateFormat",e.getMessage()));
			return null;
		}
		catch (TqlNumberFormatException e){
			LOGGER.warn (e);
			errors.add(new ErrorData("report.reportQueryTql.query.err.numberFormat",e.getMessage()));
			return null;
		}
		catch (TqlUnknownOperatorException e){
			LOGGER.warn (e);
			errors.add(new ErrorData("report.reportQueryTql.query.err.unknownOperator",e.getMessage()));
			return null;
		}
		LOGGER.debug (tqlCriteria);
		CriteriaUtil.addArchivedDeletedFilter(tqlCriteria);
		return tqlCriteria;
	}
	private static void initTqlJoinSchema(List<ErrorData> errors){
		try{
			joinSchema = new TqlJoinSchema ();
		}
		catch (TqlJoinSchemaXmlNotFoundException e){
			LOGGER.warn (e);
			errors.add (new ErrorData("report.reportQueryTql.joinSchema.error.xmlNotFound", e.getMessage ()));
			return;
		}
		catch (TqlJoinSchemaXmlParseException e){
			LOGGER.warn (e);
			errors.add (new ErrorData("report.reportQueryTql.joinSchema.error.xmlParse", e.getMessage ()));
			return;
		}
		catch (TqlJoinSchemaClassNotFoundException e){
			LOGGER.warn (e);
			errors.add (new ErrorData("report.reportQueryTql.joinSchema.error.classNotFound", e.getMessage ()));
			return;
		}
		catch (TqlJoinSchemaFieldNotFoundException e){
			LOGGER.warn (e);
			errors.add (new ErrorData("report.reportQueryTql.joinSchema.error.fieldNotFound", e.getMessage ()));
			return;
		}
	}
	
}
