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


package com.aurel.track.fieldType.runtime.matchers.run;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.util.Criteria;

import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.persist.TFieldChangePeer;
import com.aurel.track.persist.THistoryTransactionPeer;
import com.aurel.track.persist.TWorkItemPeer;


public class StringMatcherRT extends AbstractMatcherRT {
	
	private static final Logger LOGGER = LogManager.getLogger(StringMatcherRT.class);
	
	private Pattern pattern = null;
		
	public StringMatcherRT(Integer fieldID, int relation, Object matchValue,
			MatcherContext matcherContext) {		
		super(fieldID, relation, matchValue, matcherContext);
	}

	private void setPattern(String searchText) throws PatternSyntaxException {
		pattern = Pattern.compile(searchText, Pattern.CASE_INSENSITIVE| Pattern.MULTILINE);
	}
			
	/**
	 * Whether the value matches or not
	 * @param attributeValue
	 * @return
	 */
	public boolean match(Object attributeValue) {		
		Boolean nullMatch = nullMatcher(attributeValue);
		if (nullMatch!=null) {
			return nullMatch.booleanValue();
		}
		if (attributeValue==null || matchValue==null){
			return false;
		}
		String attributeValueString = null;
		String matcherValueString = null;
		try {
			attributeValueString = (String)attributeValue;		
		} catch(Exception e) {
			LOGGER.warn("Converting the attribute value " + attributeValue + " of type " + 
					attributeValue.getClass().getName() + " to String failed with " + e.getMessage(), e);
			return false;
		}
		try {
			matcherValueString =  (String)matchValue;
		} catch(Exception e) {
			LOGGER.warn("Converting the matcher value " + matchValue + " of type " + 
					matchValue.getClass().getName() + " to String failed with " + e.getMessage(), e);
			return false;
		}
		switch (relation) {
		case MatchRelations.EQUAL:			
			return attributeValueString.equals(matcherValueString);
		case MatchRelations.NOT_EQUAL:			
			return !attributeValueString.equals(matcherValueString);	
		case MatchRelations.EQUAL_IGNORE_CASE:			
			return attributeValueString.equalsIgnoreCase(matcherValueString);			
		case MatchRelations.PERL_PATTERN:
			if (pattern==null) {
				try {
					setPattern(matcherValueString);
				} catch (PatternSyntaxException e) {
					return false;				
				}				
			}			
			Matcher matcher = pattern.matcher(attributeValueString);
			return matcher.find();
		default:
			return false;
		}
	}

	/**
	 * Add a match expression to the criteria
	 */
	public void addCriteria(Criteria crit) {
		String columnName = null;
		switch (fieldID.intValue()) {
		case SystemFields.SYNOPSIS:
			columnName = TWorkItemPeer.PACKAGESYNOPSYS;
			break;
		case SystemFields.DESCRIPTION:
			columnName = TWorkItemPeer.PACKAGEDESCRIPTION;
			break;
		case SystemFields.BUILD:
			columnName = TWorkItemPeer.BUILD;
			break;
		case SystemFields.COMMENT:
			columnName = addCommentJoins(crit);
			break;
		default:
			String alias = addAliasAndJoin(crit);
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			if (fieldTypeRT.isLong()) {
				columnName = alias + "." + "LONGTEXTVALUE";
			} else {
				columnName = alias + "." + "TEXTVALUE";
			}
			break;
		}
		if (addNullExpressionToCriteria(crit, columnName)) {
			return;
		}
		String matcherValueString = null;
		try {
			matcherValueString = (String)matchValue;
		} catch(Exception e) {
			LOGGER.warn("Converting the matcher value " + matchValue + " of type " + 
					matchValue.getClass().getName() + " to String failed with " + e.getMessage(), e);
			
		}
		switch (relation) {
		case MatchRelations.EQUAL:	
			crit.add(columnName, matcherValueString);
			break;
		case MatchRelations.NOT_EQUAL:			
			crit.add(columnName, (Object)matcherValueString, Criteria.NOT_EQUAL);
			break;
		case MatchRelations.EQUAL_IGNORE_CASE:			
			crit.add(columnName, matcherValueString);
			crit.getCriterion(columnName).setIgnoreCase(true);
			break;
		case MatchRelations.LIKE:			
			crit.add(columnName, (Object)matcherValueString, Criteria.LIKE);
			break;
		case MatchRelations.NOT_LIKE:			
			crit.add(columnName, (Object)matcherValueString, Criteria.NOT_LIKE);
			break;
		}
	}
	
	/**
	 * Adds an alias for TATTRIBUTEVALUE, joins with TWORKITEM and returns the alias
	 * @param crit
	 * @param fieldID
	 * @return
	 */
	protected String addCommentJoins(Criteria crit) {
		crit.addJoin(TWorkItemPeer.WORKITEMKEY, THistoryTransactionPeer.WORKITEM);
		crit.addJoin(THistoryTransactionPeer.OBJECTID, TFieldChangePeer.HISTORYTRANSACTION);
		crit.add(TFieldChangePeer.FIELDKEY, SystemFields.INTEGER_COMMENT);
		//crit.setDistinct();
		return TFieldChangePeer.NEWLONGTEXTVALUE;
	}
}
