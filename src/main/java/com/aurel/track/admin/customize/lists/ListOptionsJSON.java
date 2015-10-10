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

package com.aurel.track.admin.customize.lists;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.LabelValueBean;

public class ListOptionsJSON {
	
	static interface JSON_FIELDS {
		//tree nodes and common
		static final String TYPE = "type";
		static final String TOP_PARENT_LIST = "topParentList";
		static final String CHILDLISTID = "childListID";
		static final String OPTIONID = "optionID";
		static final String CAN_EDIT = "canEdit";
		static final String MIGHT_EDIT_CHILD = "mightEditChild";
		static final String CAN_DELETE = "canDelete";
		static final String MIGHT_DELETE_CHILD = "mightDeleteChild";
		static final String NODE_LIST_FOR_LABEL = "nodeListForLabel";
		static final String CAN_ADD_CHILD = "canAddChild";
		static final String CAN_COPY = "canCopy";
		static final String MIGHT_COPY_CHILD = "mightCopyChild";
		static final String NODE_CHILDREN_LIST_FOR_LABEL = "nodeChildrenListForLabel";
		static final String HAS_TYPE_FLAG = "hasTypeflag";
		static final String CHILDREN_HAVE_TYPE_FLAG = "childrenHaveTypeflag";
		static final String DISABLE_TYPE_FLAG = "disableTypeflag";
		static final String HAS_ICON = "hasIcon";
		static final String CHILDREN_HAVE_ICON = "childrenHaveIcon";
		static final String HAS_CSS_STYLE = "hasCssStyle";
		static final String CHILDREN_HAVE_CSS_STYLE = "childrenHaveCssStyle";
		static final String HAS_CHILD_FILTER = "hasChildFilter";
		static final String HAS_PERCENT_COMPLETE = "hasPercentComplete";
		static final String CHILDREN_HAVE_PERCENT_COMPLETE = "childrenHavePercentComplete";
		static final String HAS_DEFAULT_OPTION = "hasDefaultOption";
		static final String CHILDREN_HAVE_DEFAULT_OPTION = "childrenHaveDefaultOption";
		
		static final String LAST_CASCADING_CHILD_DELETED  = "lastCascadingChildDeleted";
		
		//grid rows
		static final String ROW_LIST_FOR_LABEL = "rowListForLabel";	
		static final String CUSTOM_LIST_TYPE = "customListType";
		static final String CASCADING_TYPE = "cascadingType";
		static final String TYPE_FLAG_LABEL = "typeflagLabel";
		
		static final String PERCENT_COMPLETE = "percentComplete";
		static final String DEFAULT_OPTION = "defaultOption";
		static final String ICON_NAME = "iconName";
		
		static final String CSS_STYLE = "cssStyle";
		static final String CSS_STYLE_BEAN = "cssStyleBean";
		static final String BACKGROUND_COLOR = CSS_STYLE_BEAN + "." + CssStyleBean.BGR_COLOR;
		static final String FOREGROUND_COLOR = CSS_STYLE_BEAN + "." + CssStyleBean.COLOR;
		static final String FONT_WEIGHT = CSS_STYLE_BEAN + "." + CssStyleBean.FONT_WEIGHT;
		static final String FONT_WEIGHT_LIST = CssStyleBean.FONT_WEIGHT + "sList";
		static final String FONT_STYLE = CSS_STYLE_BEAN + "." + CssStyleBean.FONT_STYLE;
		static final String FONT_STYLE_LIST = CssStyleBean.FONT_STYLE + "sList";
		static final String TEXT_DECORATION = CSS_STYLE_BEAN + "." + CssStyleBean.TEXT_DECORATION;
		static final String TEXT_DECORATION_LIST = CssStyleBean.TEXT_DECORATION + "sList";
		
		//detail fields
		static final String TYPE_FLAG = "typeflag";
		static final String TYPE_FLAG_LIST = "typeflagsList";
	}
	
	
	/**
	 * Creates the JSON string with localized list names
	 * @param localizedLabels
	 * @return
	 */
	static String createLocalizedLabelsJSON(Map<Integer, String> localizedLabels) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);	
		JSONUtility.appendIntegerStringMap(sb, JSONUtility.JSON_FIELDS.RECORDS, localizedLabels, true);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Creates the JSON string children for system and custom lists tree 
	 * @param children
	 * @return
	 */
	public static String createChildrenJSON(List<ListOptionTreeNodeTO> children){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		for (Iterator<ListOptionTreeNodeTO> iterator = children.iterator(); iterator.hasNext();) {
			ListOptionTreeNodeTO listOptionTreeNodeTO = iterator.next();
			sb.append("{");	
			sb.append(getChildJSON(listOptionTreeNodeTO));
			sb.append("}");
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");//end data
		return sb.toString();
	}	
	
	public static String getChildJSON(ListOptionTreeNodeTO listOptionTreeNodeTO) {
		StringBuilder sb=new StringBuilder();
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.TEXT, listOptionTreeNodeTO.getLabel());
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.TOP_PARENT_LIST, listOptionTreeNodeTO.getTopParentListID());
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.TYPE, listOptionTreeNodeTO.getType());
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.CHILDLISTID, listOptionTreeNodeTO.getChildListID());
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.OPTIONID, listOptionTreeNodeTO.getOptionID());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CAN_EDIT, listOptionTreeNodeTO.isCanEdit());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.MIGHT_EDIT_CHILD, listOptionTreeNodeTO.isMightEditChild());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CAN_DELETE, listOptionTreeNodeTO.isCanDelete());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.MIGHT_DELETE_CHILD, listOptionTreeNodeTO.isMightDeleteChild());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CAN_ADD_CHILD, listOptionTreeNodeTO.isCanAddChild());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CAN_COPY, listOptionTreeNodeTO.isCanCopy());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.MIGHT_COPY_CHILD, listOptionTreeNodeTO.isMightCopyChild());
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.NODE_LIST_FOR_LABEL, listOptionTreeNodeTO.getNodeListForLabel());
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.NODE_CHILDREN_LIST_FOR_LABEL, listOptionTreeNodeTO.getNodeChildrenListForLabel());
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ICON, listOptionTreeNodeTO.getIcon());
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ICONCLS, listOptionTreeNodeTO.getIconCls());
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.LEAF, listOptionTreeNodeTO.isLeaf());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.HAS_TYPE_FLAG, listOptionTreeNodeTO.isHasTypeflag());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CHILDREN_HAVE_TYPE_FLAG, listOptionTreeNodeTO.isChildrenHaveTypeflag());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.DISABLE_TYPE_FLAG, listOptionTreeNodeTO.isDisableTypeflag());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.HAS_ICON, listOptionTreeNodeTO.isHasIcon());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CHILDREN_HAVE_ICON, listOptionTreeNodeTO.isChildrenHaveIcon());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.HAS_CSS_STYLE, listOptionTreeNodeTO.isHasBgrColor());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CHILDREN_HAVE_CSS_STYLE, listOptionTreeNodeTO.isChildrenHaveCssStyle());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.HAS_CHILD_FILTER, listOptionTreeNodeTO.isChildrenHaveIssueChildFilter());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.HAS_PERCENT_COMPLETE, listOptionTreeNodeTO.isHasPercentComplete());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CHILDREN_HAVE_PERCENT_COMPLETE, listOptionTreeNodeTO.isChildrenHavePercentComplete());	
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.HAS_DEFAULT_OPTION, listOptionTreeNodeTO.isHasDefaultOption());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CHILDREN_HAVE_DEFAULT_OPTION, listOptionTreeNodeTO.isChildrenHaveDefaultOption());
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ID, listOptionTreeNodeTO.getId(), true);
		return sb.toString();
	}
	
	/**
	 * Creates the JSON string list for custom lists 
	 * @param gridRows 
	 * @return
	 */
	static String createListGridRowsJSON(List<ListGridRowTO> gridRows){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		for (Iterator<ListGridRowTO> iterator = gridRows.iterator(); iterator.hasNext();) {
			ListGridRowTO listGridRowTO = iterator.next();
			sb.append("{");
			JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, listGridRowTO.getObjectID());
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, listGridRowTO.getLabel());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CAN_EDIT, listGridRowTO.isCanEdit());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CAN_DELETE, listGridRowTO.isCanDelete());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CAN_COPY, listGridRowTO.isCanCopy());
			JSONUtility.appendIntegerValue(sb, JSON_FIELDS.ROW_LIST_FOR_LABEL, listGridRowTO.getRowListForLabel());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.CUSTOM_LIST_TYPE, listGridRowTO.getCustomListType());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.CASCADING_TYPE, listGridRowTO.getCascadingType());
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ICONCLS, listGridRowTO.getIconCls());
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, listGridRowTO.getNode(), true);
			sb.append("}");
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");//end data
		return sb.toString();
	}
	
	/**
	 * Creates the JSON string list for list options 
	 * @param gridRows
	 * @return
	 */
	static String createListOptionGridRowsJSON(List<ListOptionGridRowTO> gridRows){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		for (Iterator<ListOptionGridRowTO> iterator = gridRows.iterator(); iterator.hasNext();) {
			ListOptionGridRowTO listOptionGridRowTO = iterator.next();
			sb.append("{");
			JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, listOptionGridRowTO.getObjectID());
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, listOptionGridRowTO.getLabel());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CAN_EDIT, listOptionGridRowTO.isCanEdit());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CAN_DELETE, listOptionGridRowTO.isCanDelete());
			JSONUtility.appendIntegerValue(sb, JSON_FIELDS.ROW_LIST_FOR_LABEL, listOptionGridRowTO.getRowListForLabel());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.HAS_TYPE_FLAG, listOptionGridRowTO.isHasTypeflag());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.DISABLE_TYPE_FLAG, listOptionGridRowTO.isDisableTypeflag());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.TYPE_FLAG_LABEL, listOptionGridRowTO.getTypeflagLabel());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.HAS_ICON, listOptionGridRowTO.isHasIcon());
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ICON, listOptionGridRowTO.getIcon());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.ICON_NAME, listOptionGridRowTO.getIconName());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.HAS_CSS_STYLE, listOptionGridRowTO.isHasCssStyle());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.CSS_STYLE, CssStyleBean.decodeCssStyle(listOptionGridRowTO.getCssStyle()).getCssStyle());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.HAS_CHILD_FILTER, listOptionGridRowTO.isHasChildFilter());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.HAS_PERCENT_COMPLETE, listOptionGridRowTO.isHasPercentComplete());
			JSONUtility.appendIntegerValue(sb, JSON_FIELDS.PERCENT_COMPLETE, listOptionGridRowTO.getPercentComplete());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.HAS_DEFAULT_OPTION, listOptionGridRowTO.isHasDefaultOption());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.DEFAULT_OPTION, listOptionGridRowTO.isDefaultOption());
			JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.LEAF, listOptionGridRowTO.isLeaf());
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, listOptionGridRowTO.getNode(), true);
			sb.append("}");
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");//end data
		return sb.toString();
	}
	
	/**
	 * Creates the JSON string for custom list detail 
	 * @param label
	 * @param description
	 * @return
	 */
	static String createCustomListDetailJSON(String label, String description){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, label);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.DESCRIPTION, description,true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Creates the JSON string for custom list option detail
	 * @param label
	 * @param cssStyleBean
	 * @param defaultOption
	 * @return
	 */
	static String createCustomOptionDetailJSON(String label,
			CssStyleBean cssStyleBean, boolean defaultOption){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, label);
		addCssStyleBean(sb, cssStyleBean);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.DEFAULT_OPTION, defaultOption, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Creates the JSON string for system list option detail
	 * @param label
	 * @param typeflag
	 * @param typeflagsList
	 * @param cssStyle
	 * @param percentComplete
	 * @return
	 */
	static String createSystemOptionDetailJSON(String label, Integer typeflag, 
			List<IntegerStringBean> typeflagsList, CssStyleBean cssStyleBean, Integer percentComplete){	
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, label);
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.TYPE_FLAG, typeflag);
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.PERCENT_COMPLETE, percentComplete);
		addCssStyleBean(sb, cssStyleBean);
		JSONUtility.appendIntegerStringBeanList(sb, JSON_FIELDS.TYPE_FLAG_LIST, typeflagsList, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Append the cssStyle fields to JSON
	 * @param sb
	 * @param cssStyleBean
	 */
	static void addCssStyleBean(StringBuilder sb, CssStyleBean cssStyleBean) {
		List<LabelValueBean> fontWeightList = new LinkedList<LabelValueBean>();
		fontWeightList.add(new LabelValueBean("-", ""));
		fontWeightList.add(new LabelValueBean("normal", "normal"));
		fontWeightList.add(new LabelValueBean("bold", "bold"));
		JSONUtility.appendLabelValueBeanList(sb, JSON_FIELDS.FONT_WEIGHT_LIST, fontWeightList);
		List<LabelValueBean> fontStyleList = new LinkedList<LabelValueBean>();
		fontStyleList.add(new LabelValueBean("-", ""));
		fontStyleList.add(new LabelValueBean("normal", "normal"));
		fontStyleList.add(new LabelValueBean("italic", "italic"));
		fontStyleList.add(new LabelValueBean("oblique", "oblique"));
		JSONUtility.appendLabelValueBeanList(sb, JSON_FIELDS.FONT_STYLE_LIST, fontStyleList);
		List<LabelValueBean> textDecorationList = new LinkedList<LabelValueBean>();
		textDecorationList.add(new LabelValueBean("-", "")); //for none
		textDecorationList.add(new LabelValueBean("overline", "overline"));
		textDecorationList.add(new LabelValueBean("line-through", "line-through"));
		textDecorationList.add(new LabelValueBean("underline", "underline"));
		textDecorationList.add(new LabelValueBean("blink", "blink"));
		JSONUtility.appendLabelValueBeanList(sb, JSON_FIELDS.TEXT_DECORATION_LIST, textDecorationList);
		JSONUtility.appendStringValue(sb, JSON_FIELDS.BACKGROUND_COLOR, cssStyleBean.getBgrColor());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.FOREGROUND_COLOR, cssStyleBean.getColor());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.FONT_WEIGHT, cssStyleBean.getFontWeight());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.FONT_STYLE, cssStyleBean.getFontStyle());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.TEXT_DECORATION, cssStyleBean.getTextDecoration());
	}
	
	
	/**
	 * Creates the JSON string after a save/delete operation with error message 
	 * @param success
	 * @param node
	 * @param errorMessage 
	 * @return
	 */
	static String createNodeResultJSON(boolean success, String node, String errorMessage){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, success);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ERROR_MESSAGE, errorMessage);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, node, true);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Creates the save JSON string for system list option detail 
	 * @param success
	 * @param node identify the node to be selected in the tree
	 * @param objectID identify the node to be selected in the grid 
	 * @return
	 */
	static String createSaveResultJSON(boolean success, String node, Integer objectID){	
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, success);
		JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, objectID);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, node, true);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Creates the save JSON string for system list option detail 
	 * @param success
	 * @param node identify the node to be selected in the tree
	 * @param objectID identify the node to be selected in the grid 
	 * @param lastCascadingChildDeleted whether the last cascading child list was deleted
	 * @return
	 */
	static String createDeleteResultJSON(boolean success, String node, Integer objectID, boolean lastCascadingChildDeleted){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, success);
		JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, objectID);
		if (lastCascadingChildDeleted) {
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.LAST_CASCADING_CHILD_DELETED, lastCascadingChildDeleted);
		}
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NODE, node, true);
		sb.append("}");
		return sb.toString();
	}
	
	
	
	/**
	 * Creates the JSON string with localized list names
	 * @param localizedLabels
	 * @return
	 */
	static String createAssignmentColumnJSON(String listName, List<ILabelBean> columns) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, listName);
		JSONUtility.appendILabelBeanList(sb, JSONUtility.JSON_FIELDS.RECORDS, columns);
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true, true);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Creates the JSON string with localized list names
	 * @param localizedLabels
	 * @return
	 */
	static String createAssignmentCellsJSON(List<ILabelBean> rows, List<ILabelBean> columns, Map<Integer, Map<Integer, Boolean>> assignments) {							
		StringBuilder sb=new StringBuilder();
		sb.append("{" + JSONUtility.JSON_FIELDS.RECORDS + ":[");
		for (Iterator<ILabelBean> itrRow = rows.iterator(); itrRow.hasNext();) {
			ILabelBean row = itrRow.next();
			sb.append("{");
			JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, row.getObjectID());
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, row.getLabel());
			Map<Integer, Boolean> rowAssignments = assignments.get(row.getObjectID());
			for (Iterator<ILabelBean> itrColumn = columns.iterator(); itrColumn.hasNext();) {
				ILabelBean column = itrColumn.next();
				boolean lastColumn = false;
				if (!itrColumn.hasNext()) {
					lastColumn = true;
				}
				JSONUtility.appendBooleanValue(sb, "assigned"+column.getObjectID(), rowAssignments.get(column.getObjectID()), lastColumn);
			}
			if (itrRow.hasNext()) {
				sb.append("},");
			} else {
				sb.append("}],");
			}
		}		
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true, true);
		sb.append("}");
		return sb.toString();
	}
}
