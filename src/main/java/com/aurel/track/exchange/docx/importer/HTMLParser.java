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

package com.aurel.track.exchange.docx.importer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DefaultHandler2;


/**
 * SAX parser to parse the query tree
 */
public class HTMLParser extends DefaultHandler2 {
	private static final Logger LOGGER = LogManager.getLogger(HTMLParser.class);
	
	public static void main(String[] args) {
		HTMLParser htmlParser = new HTMLParser();
		ByteArrayOutputStream byteArrayOutputStream = HtmlConverter.convertToHTML("d:/docx/PN164_SC_Track.docx");
		htmlParser.parse(byteArrayOutputStream, Locale.GERMAN);
		LOGGER.info("Main chapters found:");
		List<ItemNode> mainChapters = htmlContent.getChapters();
		if (mainChapters!=null) {
			for (ItemNode itemNode : mainChapters) {
				LOGGER.info(itemNode.getTitle());
			}
		}
		//htmlParser.parseDocument("d:/docx/PN164_SC_Track.docx.html", Locale.GERMAN);
	}
	
	private static String HEADING_KEY = "admin.actions.importDocx.heading";
	private static HTMLContent htmlContent;
	private StringBuilder contentBuffer;
	//private StringBuilder description;
	private ItemNode currentNode;
	private static int MAX_HEADING_LEVEL = 9;
	boolean insideStyle = false;
	boolean insideContent = false;
	boolean insideHeadingParagraph = false;
	private Stack<ItemNode> stack;
	private String localizedHeading;
	//private ItemNode root;
	
	enum ELEMENT_NAME {
		P("p"),
		STYLE("style");
		private String elementName;
		private ELEMENT_NAME(String name) {
			elementName = name;
		}
		public String getElementName() {
			return elementName;
		}
	}
	
	enum ATTRIBUTE_NAME {
		CLASS("class");
		private String attributeName;
		private ATTRIBUTE_NAME(String name) {
			attributeName = name;
		}
		public String getElementName() {
			return attributeName;
		}
	}
	
	private static String convertToFileURL(String filename) {
        String path = new File(filename).getAbsolutePath();
        if (File.separatorChar != '/') {
            path = path.replace(File.separatorChar, '/');
        }

        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return "file:" + path;
    }
	
	public HTMLContent parseDocument(ByteArrayOutputStream byteArrayOutputStream, Locale locale){
		//stack = new Stack<QNode>();
		parse(byteArrayOutputStream, locale);
		LOGGER.info("Main chapters found:");
		List<ItemNode> mainChapters = htmlContent.getChapters();
		if (mainChapters!=null) {
			for (ItemNode itemNode : mainChapters) {
				LOGGER.info(itemNode.getTitle());
			}
		}
		return htmlContent;
	}
	
	public HTMLContent parseDocument(String fileName, Locale locale){
		parse(fileName, locale);
		LOGGER.info("Main chapters found:");
		List<ItemNode> mainChapters = htmlContent.getChapters();
		if (mainChapters!=null) {
			for (ItemNode itemNode : mainChapters) {
				LOGGER.info(itemNode.getTitle());
			}
		}
		return htmlContent;
	}

	private void parse(ByteArrayOutputStream byteArrayOutputStream, Locale locale) {
		localizedHeading ="berschrift";//LocalizeUtil.getLocalizedTextFromApplicationResources(HEADING_KEY, locale);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		Reader fileReader = null;
		try {
			fileReader = new FileReader(new File("d:/docx/PN164_SC_Track.docx.html"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		try {
			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			//InputSource is=new InputSource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
			InputSource is = new InputSource(fileReader);
			sp.parse(is, this);
		}catch(SAXException se) {
			LOGGER.error("Parsing failed with " + se.getMessage());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.error(ExceptionUtils.getStackTrace(se));
			}
		}catch(ParserConfigurationException pce) {
			LOGGER.error("Parsing failed with " + pce.getMessage());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.error(ExceptionUtils.getStackTrace(pce));
			}
		}catch (IOException ie) {
			LOGGER.error("Reading failed with " + ie.getMessage());
			LOGGER.error(ExceptionUtils.getStackTrace(ie));
		}
	}
	
	
	private void parse(String fileName, Locale locale) {
		//get a factory
		localizedHeading ="berschrift";//LocalizeUtil.getLocalizedTextFromApplicationResources(HEADING_KEY, locale);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();
		    //spf.setValidating(true);
			XMLReader xmlReader = sp.getXMLReader();
			xmlReader.setErrorHandler(new MyErrorHandler(System.err));
			xmlReader.setContentHandler(this);
			//parse the file and also register this class for call backs
			//InputSource is=new InputSource(new StringReader(xml));
			xmlReader.parse(convertToFileURL(fileName));
		}catch(SAXException se) {
			LOGGER.error("Parsing the file " + fileName + " failed with " + se.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(se));
		}catch(ParserConfigurationException pce) {
			LOGGER.error("Parsing the file " + fileName + " failed with " + pce.getMessage());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.error(ExceptionUtils.getStackTrace(pce));
			}
		}catch (IOException ie) {
			LOGGER.error("Reading the file " + fileName + " failed with " + ie.getMessage());
			LOGGER.error(ExceptionUtils.getStackTrace(ie));
		}
		
	}
	
	 @Override
	 public void startDocument() throws SAXException {
		 htmlContent = new HTMLContent();
		 stack = new Stack<ItemNode>();
	 }

	 @Override
	 public void endDocument() throws SAXException {
		 //add the description of the last item
		currentNode.setDescription(contentBuffer.toString());
        /*Enumeration e = tags.keys();
        while (e.hasMoreElements()) {
            String tag = (String)e.nextElement();
            int count = ((Integer)tags.get(tag)).intValue();
            System.out.println("Local Name \"" + tag + "\" occurs " 
                               + count + " times");
        }*/  
    }
	 
	//Event Handlers
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		if (qName.equalsIgnoreCase(ELEMENT_NAME.STYLE.getElementName())) {
			insideStyle = true;
			contentBuffer = new StringBuilder();
		} else {
			if (qName.equalsIgnoreCase(ELEMENT_NAME.P.getElementName())) {
				String clazz = attributes.getValue(ATTRIBUTE_NAME.CLASS.getElementName());
				if (clazz!=null && !"".equals(clazz)) {
					if (clazz.contains(localizedHeading)) {
						for (int i = 1; i <= MAX_HEADING_LEVEL; i++) {
							if (clazz.contains(localizedHeading+i)) {
								if (insideContent) {
									//not the first heading: add the gathered description of the previous item when the new heading begins
									currentNode.setDescription(contentBuffer.toString());
								}
								currentNode=new ItemNode();
								//currentNode.setElementType(ELEMENT_NAME.HEADING);
								currentNode.setHeadingLevel(i);
								if (!stack.isEmpty()) {
									ItemNode itemNode = stack.peek();
									if (i>itemNode.getHeadingLevel()) {
										//current heading level > stack heading level: first sub-chapter
										List<ItemNode> children = itemNode.getChildren();
										if (children==null) {
											children = new ArrayList<ItemNode>();
											itemNode.setChildren(children);
										}
										//add as child to the peek
										children.add(currentNode);
									} else {
										if (i==itemNode.getHeadingLevel()) {
											//sibling sub-chapter: remove sibling
											itemNode = stack.pop();
											//get the parent chapter
											itemNode = stack.peek();
											List<ItemNode> children = itemNode.getChildren();
											if (children==null) {
												//should never happen
												children = new ArrayList<ItemNode>();
												itemNode.setChildren(children);
											}
											//add as child to the peek
											children.add(currentNode);
										} else {
											if (i<itemNode.getHeadingLevel()) {
												//new parent chapter starts
												while (!stack.isEmpty()) {
													//remove all children/descendants of the new parent's previous sibling
													itemNode = stack.pop();
													if (itemNode.getHeadingLevel()==i) {
														break;
													}
													
												}
											}
										}
									}
								} 
								if (stack.isEmpty()) {
									htmlContent.getChapters().add(currentNode);
								}
								stack.push(currentNode);
								insideHeadingParagraph = true;
								insideContent = true;
								//gather the paragraph title
								contentBuffer = new StringBuilder();
								break;
							}
						}
					}
				}
			}
		}
		
		if (insideContent && !insideHeadingParagraph) {
			//gather the description content
			String eName = localName; // element name
	        if ("".equals(eName)) {
	            eName = qName; // not namespaceAware
	        }
	        contentBuffer.append("<" + eName);
	        if (attributes != null) {
	            for (int i = 0; i < attributes.getLength(); i++) {
	                String aName = attributes.getLocalName(i); // Attr name 
	                if ("".equals(aName)) {
	                    aName = attributes.getQName(i);
	                }
	                contentBuffer.append(" ");
	                contentBuffer.append(aName + "=\"" + attributes.getValue(i) + "\"");
	            }
	        }
	        contentBuffer.append(">");
		}
		
		
		
	}
	@Override
	public void endElement(String uri, String localName, String qName) {
		//when the heading paragraph is ended (</p>) it  should be not added to contentBuffer to build the description for the current paragraph (although insideHeadingParagraph is set to false)
		boolean headingParagraphEnd = false;
		if (qName.equalsIgnoreCase(ELEMENT_NAME.STYLE.getElementName())) {
			insideStyle = false;
			htmlContent.setStyle(contentBuffer.toString());
			return;
		} else {
			if (qName.equalsIgnoreCase(ELEMENT_NAME.P.getElementName()) && insideHeadingParagraph==true) {
				//end of heading
				insideHeadingParagraph = false;
				headingParagraphEnd = true;
				//save the paragraph title
				currentNode.setTitle(contentBuffer.toString());
				LOGGER.debug(currentNode.getTitle());
				//rester the bugger to gather the description details till the next heading
				contentBuffer = new StringBuilder();
				/*if(!stack.isEmpty()) {
					stack_node= stack.pop();
					stack_node.addChild(currentNode);
					currentNode=stack_node;
					return;
				}*/
			}
		}
		if (insideContent && !insideHeadingParagraph && !headingParagraphEnd) {
			String eName = localName; // element name
	        if ("".equals(eName)) {
	            eName = qName; // not namespaceAware
	        }
	        contentBuffer.append("</" + eName + ">");
			//
		}
	}

	@Override
	public void characters(char ch[], int start, int length)
            throws SAXException {
		contentBuffer.append(new String(ch, start, length));
	}
	
	@Override
	public void comment(char[] ch,
            int start,
            int length)
     throws SAXException {
		contentBuffer.append(new String(ch, start, length));
	}
	
}
