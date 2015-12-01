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

import java.io.IOException;
import java.io.StringReader;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.aurel.track.admin.customize.category.filter.FieldExpressionBL;
import com.aurel.track.admin.customize.category.filter.QNode;
import com.aurel.track.admin.customize.category.filter.QNodeExpression;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.matchers.converter.MatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherDT;
import com.aurel.track.fieldType.types.FieldTypeManager;

/**
 * SAX parser to parse the query tree
 */
public class TreeFilterParser extends DefaultHandler {
	private static final Logger LOGGER = LogManager.getLogger(TreeFilterParser.class);
	private QNode currentNode;
	private Stack<QNode> stack;
	private QNode root;
	
	public QNode parseDocument(String xml){
		stack = new Stack<QNode>();
		parse(xml);
		return root;
	}

	private void parse(String xml) {
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			//parse the file and also register this class for call backs
			InputSource is=new InputSource(new StringReader(xml));
			sp.parse(is, this);
		}catch(SAXException se) {
			LOGGER.warn("Parsing expression: " + xml + " failed with " + se.getMessage());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(ExceptionUtils.getStackTrace(se));
			}
		}catch(ParserConfigurationException pce) {
			LOGGER.warn("Parsing expression: " + xml + " failed with " + pce.getMessage());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(ExceptionUtils.getStackTrace(pce));
			}
		}catch (IOException ie) {
			LOGGER.warn("Reading expression: " + xml + " failed with " + ie.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(ie));
		}
	}
	//Event Handlers
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		if(qName.equalsIgnoreCase(TreeFilterReader.ROOT_NODE)){
			return;
		}
		boolean negate = false;
		String negateString = attributes.getValue("negate"); 
		if (negateString!=null) {
			negate = negateString.equalsIgnoreCase("true");
		}
		if(qName.equalsIgnoreCase(TreeFilterReader.AND_NODE)) {
			currentNode=new QNode();
			currentNode.setType(QNode.AND);
			currentNode.setNegate(negate);
		}
		if(qName.equalsIgnoreCase(TreeFilterReader.OR_NODE)) {
			currentNode=new QNode();
			currentNode.setType(QNode.OR);
			currentNode.setNegate(negate);
		}
		if(qName.equalsIgnoreCase(TreeFilterReader.EXP_NODE)) {
			currentNode=new QNodeExpression();
			currentNode.setType(QNode.EXP);
			String strFieldMoment=attributes.getValue("fieldMoment");
			if (strFieldMoment!=null) {
				Integer fieldMoment = null;
				try {
					fieldMoment = Integer.valueOf(strFieldMoment);
					((QNodeExpression)currentNode).setFieldMoment(fieldMoment);
				} catch (Exception e) {
					LOGGER.info("Parsing the fieldMoment from " + strFieldMoment + " failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
			String strFieldID = attributes.getValue("fieldId");
			Integer fieldID = null;
			if (strFieldID!=null) {
				try {
					fieldID = Integer.valueOf(strFieldID);
				} catch (Exception e) {
					LOGGER.info("Parsing the fieldID from " + strFieldID + " failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
			String strMatcher = attributes.getValue("matcherId");
			Integer matcherID = null;
			if (strMatcher!=null && !"".equals(strMatcher)) {
				try {
					matcherID = Integer.valueOf(strMatcher);
				} catch (Exception e) {
					LOGGER.info("Parsing the matcher from " + strMatcher + " failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
			((QNodeExpression)currentNode).setField(fieldID);
			((QNodeExpression)currentNode).setMatcherID(matcherID);
			String value=attributes.getValue("value");
			if (!TreeFilterString.isPseudoField(fieldID)) {
				MatcherConverter mc = null;
				if (fieldID.intValue()>0) {
					IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
					//It can be that this field is already deleted. 
					//The query containing this field was not deleted because the foreign key 
					//relationship is not direct (just inside the clob).
					if (fieldTypeRT!=null) {
						//real (system or custom) and existing field
						mc = fieldTypeRT.getMatcherConverter();
					}
	
				} else {
					mc = FieldExpressionBL.getPseudoFieldMatcherConverter(fieldID);
				}
				if (mc!=null) {
					((QNodeExpression)currentNode).setValue(mc.fromXMLString(value, matcherID));
				} else {
					//the field does not exists any more
					//and mark this with setting the fieldID as null
					//to avoid adding it to the tree (see endElement())
					((QNodeExpression)currentNode).setField(null);
					return;
				}
			} else {
				//pseudo field
				((QNodeExpression)currentNode).setValue(TreeFilterString.getPseudoFieldFromXMLString(fieldID, value, matcherID));
			}
			if (negate) {
				removeNegate((QNodeExpression)currentNode);
			}
		}
		stack.push(currentNode);
		if(root==null){
			root=currentNode;
		}
	}
	@Override
	public void endElement(String uri, String localName, String qName) {
		QNode stack_node=null;
		if(!stack.isEmpty()){
			stack_node=(QNode) stack.peek();
		}
		if(qName.equalsIgnoreCase(TreeFilterReader.AND_NODE)||
			qName.equalsIgnoreCase(TreeFilterReader.OR_NODE)||
			qName.equalsIgnoreCase(TreeFilterReader.EXP_NODE)) {
			if(stack_node==currentNode){
				stack.pop();
				if(stack.isEmpty()){
					return;
				}
				stack_node= (QNode) stack.peek();
			}
			if(stack_node!=null){
				if(qName.equalsIgnoreCase(TreeFilterReader.EXP_NODE)){
					//extra verification for expression node
					//need because the field associated was deleted
					if(((QNodeExpression)currentNode).getField()!=null){
						//add only nodes with valid field
						stack_node.addChild(currentNode);
					}
				}else{
					stack_node.addChild(currentNode);
				}
			}
			currentNode=stack_node;
		}
	}

	/**
	 * The QNodeExpressions can't be negated any more
	 * For backward compatibility if the QNodeExpression was negated
	 * remove the negation and set the analog reverse matcher  
	 * @param qNodeExpression
	 */
	public static void removeNegate(QNodeExpression qNodeExpression) {
		if (qNodeExpression.isNegate()) {
			//try to make the negated leaf expressions backward compatible
			//otherwise selectedMatcher is set in super
			Integer fieldID = qNodeExpression.getField();
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			if (fieldTypeRT!=null) {
				IMatcherDT matcherDT = fieldTypeRT.processLoadMatcherDT(fieldID);
				if (matcherDT!=null) {
					qNodeExpression.setMatcherID(matcherDT.getNegatedMatcher(qNodeExpression.getMatcherID()));
					qNodeExpression.setNegate(false);
				}
			}
		}
	}
}
