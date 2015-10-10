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

package com.aurel.track.admin.customize.category.filter.tree.io;

import java.util.List;
import java.util.Locale;

import com.aurel.track.admin.customize.category.filter.FieldExpressionBL;
import com.aurel.track.admin.customize.category.filter.QNode;
import com.aurel.track.admin.customize.category.filter.QNodeExpression;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.project.release.ReleaseConfigBL;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.fieldType.runtime.matchers.converter.MultipleSelectMatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.itemNavigator.layout.column.ColumnFieldsBL;
import com.aurel.track.linkType.LinkTypeBL;
import com.aurel.track.resources.LocalizeUtil;

public class TreeFilterString {

	
	
	/**
	 * Transform a query node into a query String
	 * @param node
	 * @return
	 */
	public static String toQueryString(QNode node, Locale locale){
		StringBuilder sb=new StringBuilder();
		if(node==null) {
			return "";
		}
		if (node.isNegate()){
			sb.append("<B>!</B>(");
		}
		if(node.getType()==QNode.EXP){
			return getTitle(node, locale);
		}
		List<QNode> children = node.getChildren();
		if (children!=null && !children.isEmpty()){
			QNode qNodeFirstChild = children.get(0);
			if (children.size()>1) {
				sb.append(getQNodeString(qNodeFirstChild, locale));
				sb.append(wrap(OPERATOR_COLOR, getTitle(node, locale), true));
			}
			for (int i = 1; i < children.size()-1; i++) {
				QNode qNodeMiddleChildren = children.get(i);
				sb.append(getQNodeString(qNodeMiddleChildren, locale));
				sb.append(wrap(OPERATOR_COLOR, getTitle(node, locale), true));
			}
			QNode qNodeLastChild = children.get(children.size()-1);
			sb.append(getQNodeString(qNodeLastChild, locale));
		}
		if(node.isNegate()){
			sb.append(")");
		}
		return sb.toString();
	}
	
	/**
	 * Gets the string for a qNode
	 * @param qNode
	 * @param locale
	 * @return
	 */
	private static String getQNodeString(QNode qNode, Locale locale) {
		String q=toQueryString(qNode, locale);
		if (qNode.getType()==QNode.EXP){
			return q;
		} else {
			return "("+q+")";
		}
	}
	
	private static final String OPERATOR_COLOR="blue";
	private static final String MATCHER_COLOR="blue";
	private static final String FIELD_COLOR="green";
	private static final String OLD_NEW_COLOR="gray";
	
	private static String wrap(String color, String value, boolean first){
		String wrappedString = "<font color='"+color+"'>"+value+"</font> ";
		if (first) {
			wrappedString = " " + wrappedString;
		}
		return wrappedString;
	}
		
	private static String wrapOldNew(String s){
		if (s!=null) {
			return "<I><font color='"+OLD_NEW_COLOR+"'>"+s+"</font></I> ";
		} else {
			return "";
		}
	}

	private static String getTitle(QNode node, Locale locale) {
		int type=node.getType();
		switch(type){
			case QNode.AND:{
				return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.queryFilter.opt.operation.and", locale);
			}
			case QNode.OR:{
				return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.queryFilter.opt.operation.or", locale);
			}
			case QNode.EXP:{
				return getNodeExpressionTitle((QNodeExpression)node,locale);
			}
		}
		return "";
	}
	
	/**
	 * Get the title for  a node expression
	 * @param node
	 * @param locale
	 * @return
	 */
	private static String getNodeExpressionTitle(QNodeExpression node, Locale locale) {
		String fieldName = "";
		Integer fieldID = node.getField();
		Integer matcherID = node.getMatcherID();
		if (fieldID != null) {
			String matcher = "";
			String displayValue ="";
			String strFieldMoment=null;
			if (!isPseudoField(fieldID)) {
				IFieldTypeRT fieldTypeRT = null;
				if (fieldID.intValue()>0) {
					TFieldConfigBean fieldConfigBean = FieldRuntimeBL.getDefaultFieldConfigIfMatcher(fieldID, locale);
					if (fieldConfigBean!=null) {
						fieldName = fieldConfigBean.getLabel();
						fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
					} else {
						//no default fieldConfigBean for a real field with matcher found 
						//should never occur (the deleted fields are filtered out 
						//by loading the tree from the database)
						return "";
					}
				} else {
					fieldName = ColumnFieldsBL.getPseudoColumnLabel(fieldID, locale);
					fieldTypeRT = FieldExpressionBL.getPseudoFieldFieldTypeRT(fieldID);
					fieldID = FieldExpressionBL.getPseudoFieldSystemOption(fieldID);
				}
				matcher = LocalizeUtil.getLocalizedTextFromApplicationResources(FieldExpressionBL.MATCH_RELATION_PREFIX + matcherID, locale);
				if (fieldTypeRT==null || matcherID==null || matcherID.intValue()==MatchRelations.IS_NULL || 
						matcherID.intValue()==MatchRelations.NOT_IS_NULL || 
						matcherID.intValue()==MatchRelations.SET ||
						matcherID.intValue()==MatchRelations.RESET || 
						MatcherContext.PARAMETER.equals(node.getMatcherID())) {
					displayValue="";
				} else {
					displayValue= fieldTypeRT.getMatcherShowValue(fieldID, node.getValue(), locale);
				}
				Integer fieldMoment = node.getFieldMoment();
				if (fieldMoment!=null) {
					strFieldMoment=LocalizeUtil.getLocalizedTextFromApplicationResources("query.queryNodeExpressionDetail.fieldMoment."+fieldMoment, locale);
					if (strFieldMoment!=null && !"".equals(strFieldMoment)) {
						strFieldMoment+=" ";
					}
				}
			} else {
				fieldName = getPseudoFieldLabel(fieldID, locale);
				matcher = LocalizeUtil.getLocalizedTextFromApplicationResources(FieldExpressionBL.MATCH_RELATION_PREFIX + MatchRelations.EQUAL, locale);
				if (node.getValue()!=null) {
					displayValue = getPseudoFieldValue(fieldID, node.getValue(), locale);
				}
			}
			return wrapOldNew(strFieldMoment) + wrap(FIELD_COLOR, fieldName, false) + wrap(MATCHER_COLOR, matcher, false)+displayValue;
		}
		return "";
	}
	
	/**
	 * Whether a field is pseudo field (not a real field)
	 * Used to avoid getting the field type using the FieldTypeManager
	 * when it will be surely not found there.  
	 * @param fieldID
	 * @return
	 */
	public static boolean isPseudoField(Integer fieldID) {
		if (fieldID==null) {
			return false;
		}
		switch (fieldID.intValue()) {
		case FilterUpperTO.PSEUDO_FIELDS.SHOW_CLOSED_RELEASES:
		case FilterUpperTO.PSEUDO_FIELDS.RELEASE_TYPE_SELECTOR:
		case FilterUpperTO.PSEUDO_FIELDS.KEYWORD_FIELD_ID:
		case FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID:
		case FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_OR_INFORMANT_SELECTOR:
		case FilterUpperTO.PSEUDO_FIELDS.ARCHIVED_FIELD_ID:
		case FilterUpperTO.PSEUDO_FIELDS.DELETED_FIELD_ID:
		case FilterUpperTO.PSEUDO_FIELDS.LINKTYPE_FILTER_SUPERSET:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Whether a field is pseudo field (not a real field)
	 * Used to avoid getting the field type using the FieldTypeManager
	 * when it will be surely not found there.  
	 * @param fieldID
	 * @return
	 */
	public static Object getPseudoFieldFromXMLString(Integer fieldID, String value, Integer matcherRelation) {
		if (fieldID==null || value==null) {
			return null;
		}
		switch (fieldID.intValue()) {
		case FilterUpperTO.PSEUDO_FIELDS.SHOW_CLOSED_RELEASES:
			return Boolean.parseBoolean(value);
		case FilterUpperTO.PSEUDO_FIELDS.RELEASE_TYPE_SELECTOR:
			return Integer.valueOf(value);
		case FilterUpperTO.PSEUDO_FIELDS.KEYWORD_FIELD_ID:
			return value;
		case FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID:
			return MultipleSelectMatcherConverter.getInstance().fromXMLString(value, matcherRelation);
		case FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_OR_INFORMANT_SELECTOR:
			return Integer.valueOf(value);
		case FilterUpperTO.PSEUDO_FIELDS.ARCHIVED_FIELD_ID:
			return Integer.valueOf(value);
		case FilterUpperTO.PSEUDO_FIELDS.DELETED_FIELD_ID:
			return Integer.valueOf(value);
		case FilterUpperTO.PSEUDO_FIELDS.LINKTYPE_FILTER_SUPERSET:
			return value;
		}
		return value;
	}
	
	/**
	 * Whether a field is pseudo field (not a real field)
	 * Used to avoid getting the field type using the FieldTypeManager
	 * when it will be surely not found there.  
	 * @param fieldID
	 * @return
	 */
	public static String getPseudoFieldFToXMLString(Integer fieldID, Object value, Integer matcherRelation) {
		if (fieldID==null || value==null) {
			return "";
		}
		if (fieldID.intValue()==FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID) {
			return MultipleSelectMatcherConverter.getInstance().toXMLString(value, matcherRelation);
		} else {
			return value.toString();
		}
	}
	
	/**
	 * Gets the label for a pseudo field 
	 * @param fieldID
	 * @param locale
	 * @return
	 */
	private static String getPseudoFieldLabel(Integer fieldID, Locale locale) {
		if (fieldID==null) {
			return null;
		}
		String pseudoFieldLabel = null;
		switch (fieldID.intValue()) {
		case FilterUpperTO.PSEUDO_FIELDS.RELEASE_TYPE_SELECTOR:
			return LocalizeUtil.getParametrizedString("admin.customize.queryFilter.lbl.releaseSelector",
					new Object[] {LocalizeUtil.getLocalizedTextFromApplicationResources(ReleaseConfigBL.RELEAESE_KEY, locale)}, locale) ;
		case FilterUpperTO.PSEUDO_FIELDS.SHOW_CLOSED_RELEASES:
			pseudoFieldLabel = "admin.customize.queryFilter.lbl.showClosedReleases";
			break;
		case FilterUpperTO.PSEUDO_FIELDS.KEYWORD_FIELD_ID:
			pseudoFieldLabel = "admin.customize.queryFilter.lbl.keyword";
			break;
		case FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID:
			pseudoFieldLabel = "admin.customize.queryFilter.lbl.consInf";
			break;
		case FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_OR_INFORMANT_SELECTOR:
			pseudoFieldLabel = "admin.customize.queryFilter.lbl.consInfSelector";
			break;
		case FilterUpperTO.PSEUDO_FIELDS.ARCHIVED_FIELD_ID:
			pseudoFieldLabel = "admin.customize.queryFilter.lbl.archived";
			break;
		case FilterUpperTO.PSEUDO_FIELDS.DELETED_FIELD_ID:
			pseudoFieldLabel = "admin.customize.queryFilter.lbl.deleted";
			break;
		case FilterUpperTO.PSEUDO_FIELDS.LINKTYPE_FILTER_SUPERSET:
			pseudoFieldLabel = "admin.customize.queryFilter.lbl.linkTypeFilterSuperset";
			break;
		}
		if (pseudoFieldLabel!=null) {
			return LocalizeUtil.getLocalizedTextFromApplicationResources(pseudoFieldLabel, locale);
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the string value for a pseudo field value 
	 * @param fieldID
	 * @param value
	 * @param locale
	 * @return
	 */
	private static String getPseudoFieldValue(Integer fieldID, Object value, Locale locale) {
		if (fieldID==null || value==null) {
			return null;
		}
		switch (fieldID.intValue()) {
		case FilterUpperTO.PSEUDO_FIELDS.RELEASE_TYPE_SELECTOR:
			try {
				return getReleaseTypeSelectorLabel((Integer)value, locale);
			} catch (Exception e) {
			}
			break;
		case FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID:
			try {
				Integer[] watcherIDs = (Integer[])value;
				return FieldTypeManager.getFieldTypeRT(SystemFields.INTEGER_RESPONSIBLE).getMatcherShowValue(SystemFields.INTEGER_RESPONSIBLE, watcherIDs, locale); 
			} catch (Exception e) {
			}
			break;
		case FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_OR_INFORMANT_SELECTOR:
			try {
				return getWatcherSelectorLabel((Integer)value, locale);
			} catch (Exception e) {
			}
			break;
		case FilterUpperTO.PSEUDO_FIELDS.ARCHIVED_FIELD_ID:
			try {
				return getArchivedLabel((Integer)value, locale);
			} catch (Exception e) {
			}
			break;
		case FilterUpperTO.PSEUDO_FIELDS.DELETED_FIELD_ID:
			try {
				return getDeleteLabel((Integer)value, locale);
			} catch (Exception e) {
			}
			break;
		case FilterUpperTO.PSEUDO_FIELDS.LINKTYPE_FILTER_SUPERSET:
			Integer[] linkParts = MergeUtil.getParts((String)value);
			
			Integer linkID = linkParts[0];
			Integer direction = linkParts[1];
			return LinkTypeBL.getLocalizedLinkExpressionLabel(linkID, direction, true, locale);
		}
		return value.toString();
	}
	
	/**
	 * Get the label for the release type selector
	 * @param selectorValue
	 * @param locale
	 * @return
	 */
	private static String getReleaseTypeSelectorLabel(Integer selectorValue, Locale locale) {
		if (selectorValue==null) {
			return "";
		}
		String selectorLabelKey = null;
		switch (selectorValue.intValue()) {
		case FilterUpperTO.RELEASE_SELECTOR.NOTICED:
			selectorLabelKey = "admin.customize.queryFilter.opt.release.noticed";
			break;
		case FilterUpperTO.RELEASE_SELECTOR.SCHEDULED:
			selectorLabelKey = "admin.customize.queryFilter.opt.release.scheduled";
			break;
		case FilterUpperTO.RELEASE_SELECTOR.NOTICED_OR_SCHEDULED:
			selectorLabelKey = "admin.customize.queryFilter.opt.release.noticedOrScheduled";
			break;
		}
		if (selectorLabelKey!=null) {
			return LocalizeUtil.getLocalizedTextFromApplicationResources(selectorLabelKey, locale);
		} else {
			return "";
		}
	}
	
	/**
	 * Get the label for the watcher selector
	 * @param selectorValue
	 * @param locale
	 * @return
	 */
	private static String getWatcherSelectorLabel(Integer selectorValue, Locale locale) {
		if (selectorValue==null) {
			return "";
		}
		String selectorLabelKey = null;
		switch (selectorValue.intValue()) {		
		case FilterUpperTO.CONSINF_SELECTOR.CONSULTANT:
			selectorLabelKey = "admin.customize.queryFilter.opt.watcher.consultant";
			break;
		case FilterUpperTO.CONSINF_SELECTOR.INFORMANT:
			selectorLabelKey = "admin.customize.queryFilter.opt.watcher.informant";
			break;
		case FilterUpperTO.CONSINF_SELECTOR.CONSULTANT_OR_INFORMANT:
			selectorLabelKey = "admin.customize.queryFilter.opt.watcher.consOrInf";
			break;
		}
		if (selectorLabelKey!=null) {
			return LocalizeUtil.getLocalizedTextFromApplicationResources(selectorLabelKey, locale);
		} else {
			return "";
		}
	}
	
	/**
	 * Get the label for archived flag
	 * @param value
	 * @param locale
	 * @return
	 */
	private static String getArchivedLabel(Integer value, Locale locale) {
		if (value==null) {
			return "";
		}
		String selectorLabelKey = null;
		switch (value.intValue()) {		
		case FilterUpperTO.ARCHIVED_FILTER.EXCLUDE_ARCHIVED:
			selectorLabelKey = "admin.customize.queryFilter.opt.archived.exclude";
			break;
		case FilterUpperTO.ARCHIVED_FILTER.INCLUDE_ARCHIVED:
			selectorLabelKey = "admin.customize.queryFilter.opt.archived.inlude";
			break;
		case FilterUpperTO.ARCHIVED_FILTER.ONLY_ARCHIVED:
			selectorLabelKey = "admin.customize.queryFilter.opt.archived.only";
			break;
		}
		if (selectorLabelKey!=null) {
			return LocalizeUtil.getLocalizedTextFromApplicationResources(selectorLabelKey, locale);
		} else {
			return "";
		}
	}
	
	/**
	 * Get the label for deleted flag
	 * @param value
	 * @param locale
	 * @return
	 */
	private static String getDeleteLabel(Integer value, Locale locale) {
		if (value==null) {
			return "";
		}
		String selectorLabelKey = null;
		switch (value.intValue()) {		
		case FilterUpperTO.DELETED_FILTER.EXCLUDE_DELETED:
			selectorLabelKey = "admin.customize.queryFilter.opt.deleted.exclude";
			break;
		case FilterUpperTO.DELETED_FILTER.INCLUDE_DELETED:
			selectorLabelKey = "admin.customize.queryFilter.opt.deleted.inlude";
			break;
		case FilterUpperTO.DELETED_FILTER.ONLY_DELETED:
			selectorLabelKey = "admin.customize.queryFilter.opt.deleted.only";
			break;
		}
		if (selectorLabelKey!=null) {
			return LocalizeUtil.getLocalizedTextFromApplicationResources(selectorLabelKey, locale);
		} else {
			return "";
		}
	}
}
