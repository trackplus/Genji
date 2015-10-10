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

package com.aurel.track.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Config;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.EndTagType;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.HTMLElements;
import net.htmlparser.jericho.LoggerProvider;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.StartTagType;
import net.htmlparser.jericho.Tag;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HTMLSanitiser {
	// list of HTML elements that will be retained in the final output:
	private static final Set<String> VALID_ELEMENT_NAMES=new HashSet<String>(Arrays.asList(new String[] {
		HTMLElementName.BR,
		HTMLElementName.P,
		HTMLElementName.B,
		HTMLElementName.I,
		HTMLElementName.OL,
		HTMLElementName.UL,
		HTMLElementName.LI,
		HTMLElementName.H1,
		HTMLElementName.H2,
		HTMLElementName.H3,
		HTMLElementName.H4,
		HTMLElementName.H5,
		HTMLElementName.A,
		HTMLElementName.DIV,
		HTMLElementName.SPAN,
		HTMLElementName.FONT,
		HTMLElementName.TABLE,
		HTMLElementName.THEAD,
		HTMLElementName.TBODY,
		HTMLElementName.TR,
		HTMLElementName.TD,
		HTMLElementName.TH,
		
		HTMLElementName.STRONG,
		HTMLElementName.EM,
		HTMLElementName.U,
		
		HTMLElementName.DEL,
		HTMLElementName.SUB,
		HTMLElementName.SUP,
		HTMLElementName.BIG,
		HTMLElementName.SMALL,
		HTMLElementName.CODE,
		HTMLElementName.ADDRESS,
		HTMLElementName.STRIKE,
		HTMLElementName.IMG,

		HTMLElementName.FIGURE,
		HTMLElementName.FIGCAPTION
	}));

	// list of HTML attributes that will be retained in the final output:
	private static final Set<String> VALID_ATTRIBUTE_NAMES=new HashSet<String>(Arrays.asList(new String[] {
		"id","class","href","target","title","style","src","alt","width","height","type"
	}));

	private static final Object VALID_MARKER=new Object();

	static{
		Config.LoggerProvider=LoggerProvider.DISABLED;
	}

	private static String sanitise(String pseudoHTML, boolean formatWhiteSpace, boolean stripInvalidElements) {
		Source source=new Source(pseudoHTML);
		source.fullSequentialParse();
		OutputDocument outputDocument=new OutputDocument(source);
		List<Tag> tags=source.getAllTags();
		int pos=0;
		for (Tag tag : tags) {
			if (processTag(tag,outputDocument)) {
				tag.setUserData(VALID_MARKER);
			} else {
				if (!stripInvalidElements) continue; // element will be encoded along with surrounding text
				if (!stripInvalidElements) continue; // element will be encoded along with surrounding text
				if(tag.getName().equalsIgnoreCase("style")){
					Tag nextTag=tag.getNextTag();
					int endPos=0;
					if(nextTag!=null){
						endPos=nextTag.getBegin()-1;
					}else{
						endPos=source.getEnd();
					}
					outputDocument.remove(tag.getBegin(),endPos);
				}else{
					outputDocument.remove(tag);
				}
			}
			//reencodeTextSegment(source,outputDocument,pos,tag.getBegin(),formatWhiteSpace);
			pos=tag.getEnd();
		}
		//reencodeTextSegment(source,outputDocument,pos,source.getEnd(),formatWhiteSpace);
		return outputDocument.toString();
	}

	private static boolean processTag(Tag tag, OutputDocument outputDocument) {
		String elementName=tag.getName();
		if (!VALID_ELEMENT_NAMES.contains(elementName)){
			//System.out.println("Not ok tag:!"+elementName+"!");
			return false;
		}
		if (tag.getTagType()==StartTagType.NORMAL) {
			Element element=tag.getElement();
			if (elementName==HTMLElementName.THEAD && !isValidTbodyTHeadTag(tag)) return false;
			if (elementName==HTMLElementName.TBODY && !isValidTbodyTHeadTag(tag)) return false;
			if (elementName==HTMLElementName.TR && !isValidTRTag(tag)) return false;
			if (elementName==HTMLElementName.TD && !isValidTDTHTag(tag)) return false;
			if (elementName==HTMLElementName.TH && !isValidTDTHTag(tag)) return false;
			if (HTMLElements.getEndTagRequiredElementNames().contains(elementName)) {
				if (element.getEndTag()==null) return false; // reject start tag if its required end tag is missing
			} else if (HTMLElements.getEndTagOptionalElementNames().contains(elementName)) {
				if (elementName==HTMLElementName.LI && !isValidLITag(tag)) return false; // reject invalid LI tags
				if (element.getEndTag()==null) outputDocument.insert(element.getEnd(),getEndTagHTML(elementName)); // insert optional end tag if it is missing
			}
			outputDocument.replace(tag,getStartTagHTML(element.getStartTag()));
		} else if (tag.getTagType()==EndTagType.NORMAL) {
			if (tag.getElement()==null) return false; // reject end tags that aren't associated with a start tag
			if (elementName==HTMLElementName.THEAD && !isValidTbodyTHeadTag(tag)) return false;
			if (elementName==HTMLElementName.TBODY && !isValidTbodyTHeadTag(tag)) return false;
			if (elementName==HTMLElementName.TR && !isValidTRTag(tag)) return false;
			if (elementName==HTMLElementName.TD && !isValidTDTHTag(tag)) return false;
			if (elementName==HTMLElementName.TH && !isValidTDTHTag(tag)) return false;
			if (elementName==HTMLElementName.LI && !isValidLITag(tag)) return false; // reject invalid LI tags
			outputDocument.replace(tag,getEndTagHTML(elementName));
		} else {
			return false; // reject abnormal tags
		}
		return true;
	}

	private static boolean isValidLITag(Tag tag) {
		Element parentElement=tag.getElement().getParentElement();
		if (parentElement==null) return false; // ignore LI elements without a parent
		if (parentElement.getStartTag().getUserData()!=VALID_MARKER) return false; // ignore LI elements who's parent is not valid
		return parentElement.getName()==HTMLElementName.UL || parentElement.getName()==HTMLElementName.OL; // only accept LI tags who's immediate parent is UL or OL.
	}
	private static boolean isValidTRTag(Tag tag) {
		Element parentElement=tag.getElement().getParentElement();
		if (parentElement==null) return false; // ignore TR elements without a parent
		if (parentElement.getStartTag().getUserData()!=VALID_MARKER) return false; // ignore TR elements who's parent is not valid
		if(parentElement.getName()==HTMLElementName.TABLE){
			return true;
		}
		if(parentElement.getName()==HTMLElementName.TBODY
				||parentElement.getName()==HTMLElementName.THEAD){
			Element gradParent=parentElement.getParentElement();
			if(gradParent==null){
				return false;
			}
			if (gradParent.getStartTag().getUserData()!=VALID_MARKER) return false;
			return (gradParent.getName()==HTMLElementName.TABLE);
		}else{
			return false;
		}
	}
	private static boolean isValidTDTHTag(Tag tag) {
		Element parentElement=tag.getElement().getParentElement();
		if (parentElement==null) return false; // ignore TD, TH elements without a parent
		if (parentElement.getStartTag().getUserData()!=VALID_MARKER) return false; // ignore TD,TH elements who's parent is not valid
		return parentElement.getName()==HTMLElementName.TR; // only accept TD,TH tags who's immediate parent is TR.
	}
	private static boolean isValidTbodyTHeadTag(Tag tag) {
		Element parentElement=tag.getElement().getParentElement();
		if (parentElement==null) return false;
		if (parentElement.getStartTag().getUserData()!=VALID_MARKER) return false;
		return parentElement.getName()==HTMLElementName.TABLE;
	}
	private static void reencodeTextSegment(Source source, OutputDocument outputDocument, int begin, int end, boolean formatWhiteSpace) {
	  if (begin>=end) return;
	  Segment textSegment=new Segment(source,begin,end);
		String decodedText=CharacterReference.decode(textSegment);
		String encodedText=formatWhiteSpace ? CharacterReference.encodeWithWhiteSpaceFormatting(decodedText) : CharacterReference.encode(decodedText);
    outputDocument.replace(textSegment,encodedText);
	}

	private static CharSequence getStartTagHTML(StartTag startTag) {
		// tidies and filters out non-approved attributes
		StringBuilder sb=new StringBuilder();
		sb.append('<').append(startTag.getName());
	  for (Attribute attribute : startTag.getAttributes()) {
	    if (VALID_ATTRIBUTE_NAMES.contains(attribute.getKey())) {
				sb.append(' ').append(attribute.getName());
				if (attribute.getValue()!=null) {
					sb.append("=\"");
				  sb.append(CharacterReference.encode(attribute.getValue()));
					sb.append('"');
				}
			}
	  }
	  if (startTag.getElement().getEndTag()==null && !HTMLElements.getEndTagOptionalElementNames().contains(startTag.getName())) sb.append(" /");
		sb.append('>');
		return sb;
	}

	private static String getEndTagHTML(String tagName) {
		return "</"+tagName+'>';
	}
	
	public static String stripHTML(String input){
		if(input==null){
			return null;
		}
		/*
		 String replaced=Pattern.compile("<script[^>]*>.*</script>",Pattern.DOTALL).matcher(content).replaceAll("");
		replaced=Pattern.compile("<style[^>]*>.*</style>",Pattern.DOTALL).matcher(replaced).replaceAll("");
		replaced=Pattern.compile("<\\!--.*-->",Pattern.DOTALL).matcher(replaced).replaceAll("");
        return replaced; 
		 */
		String html=sanitise(input,false,true);
		Document doc = Jsoup.parse(html);
		return doc.body().html();
	}
	
	
	public static void main(String[] args) throws Exception {
		System.out.println("Examples of HTMLSanitiser.encodeInvalidMarkup:");
		displayStripInvalidMarkup("<Script>aa</script> <P>abc<table><tr><tD>aa bb bb cc</td></tr></table><ul><li>ss</li></ul><table><tbody><tr><td>sss</tr></table>","add optional end tag");
	}

	private static void displayStripInvalidMarkup(String input, String explanation) {
		display(input,explanation,sanitise(input,false,true));
	}

	private static void display(String input, String explanation, String output) {
		System.out.println(explanation+":\ninput : "+input+"\noutput: "+output+"\n");
	}
}
