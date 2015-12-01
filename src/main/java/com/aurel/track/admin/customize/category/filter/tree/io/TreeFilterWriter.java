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

package com.aurel.track.admin.customize.category.filter.tree.io;

import java.util.List;

import com.aurel.track.admin.customize.category.filter.FieldExpressionBL;
import com.aurel.track.admin.customize.category.filter.QNode;
import com.aurel.track.admin.customize.category.filter.QNodeExpression;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.matchers.converter.MatcherConverter;
import com.aurel.track.fieldType.types.FieldTypeManager;

/**
 * This class is used to write in an xml string an Query tree node
 */
public class TreeFilterWriter {
	private static final String INDENT_UNIT=" ";
	private static TreeFilterWriter instance;
	public static TreeFilterWriter getInstance(){
		if(instance==null){
			instance=new TreeFilterWriter() ;
		}
		return instance;
	}

	/**
	 * Write an query tree to XML String
	 * @param root -the root node af query tree
	 * @return -the XML data string
	 */
	public String toXML(QNode root){
		StringBuffer sb=new StringBuffer();
		sb.append("<QUERY>\n");
		sb.append(encodeNode(root,INDENT_UNIT));
		sb.append("</QUERY>");
		return sb.toString();
	}
	private StringBuffer encodeNode(QNode node, String indent){
		StringBuffer sb=new StringBuffer();
		if (node!=null) {
			int type=node.getType();
			if(type==QNode.EXP && ((QNodeExpression)node).getField()==null){
				return sb;
			}
			String nodeName="";
			switch(type){
				case QNode.AND:{
					nodeName="AND";
					break;
				}
				case QNode.OR:{
					nodeName="OR";
					break;
				}
				case QNode.EXP:{
					nodeName="EXP";
				}
			}
			sb.append(indent+"<"+nodeName);
			if (node.isNegate()) {
				sb.append(" negate=\""+node.isNegate()+"\"");
			}
			if(type==QNode.EXP){
				QNodeExpression qNodeExpression = (QNodeExpression)node;
				Integer fieldID = qNodeExpression.getField();
				Integer matcherID = qNodeExpression.getMatcherID();
				if (fieldID!=null) {
					sb.append(" fieldId=\""+(fieldID)+"\"");
				}
				if (qNodeExpression.getFieldMoment()!=null) {
					sb.append(" fieldMoment=\""+(qNodeExpression.getFieldMoment())+"\"");
				}
				if (matcherID!=null) {
					sb.append(" matcherId=\""+(matcherID)+"\"");
				}
				String xmlValue = "";
				if (TreeFilterString.isPseudoField(fieldID)) {
					//not a real field: like the keyword or consultants/informants: 
					//get the string value because no matcher is called to convert it to other type 
					xmlValue = TreeFilterString.getPseudoFieldFToXMLString(fieldID, qNodeExpression.getValue(), matcherID);
				} else {
					MatcherConverter matcherConverter = null;
					if (fieldID.intValue()>0) {
						IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
						if (fieldTypeRT!=null) {
							matcherConverter= fieldTypeRT.getMatcherConverter();	
						}
					} else {
						matcherConverter = FieldExpressionBL.getPseudoFieldMatcherConverter(fieldID);
					}
					if (matcherConverter!=null) {
						xmlValue=matcherConverter.toXMLString(qNodeExpression.getValue(), matcherID);
					}
				}
				if (xmlValue!=null) {
					sb.append(" value=\""+xmlValue+"\"");
				}
			}
			sb.append(">\n");
			List<QNode> children=node.getChildren();
			if(children!=null && !children.isEmpty()){
				for (QNode childNode : children) {
					StringBuffer sbC = encodeNode(childNode,indent+INDENT_UNIT);
					sb.append(sbC);
				}
			}
			sb.append(indent+"</"+nodeName+">\n");
		}
		return sb;
	}
}
