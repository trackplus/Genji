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


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Pattern;
import org.apache.oro.text.regex.Perl5Substitution;
import org.apache.oro.text.regex.Util;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.item.link.ItemLinkBL;
import com.aurel.track.item.link.ItemLinkListEntry;
import com.aurel.track.persist.TProject;

public class TagReplacer {
	 private static final Logger LOGGER = LogManager.getLogger(TagReplacer.class);
    private static final long serialVersionUID = 1L;
    private static String	EMPTY		= "";
	private static String[] BOLD		= {"<strong>", "</strong>", "[b]", "[/b]" };
	private static String[] ITALIC		= {"<em>", "</em>", "[i]", "[/i]" };
	private static String[] UNDERLINE	= {"<u>", "</u>", "[u]", "[/u]" };
	/*private static String[] SRC			= {	"<SPAN style=\"font-size:3pt\"><BR><BR></SPAN><CENTER><TABLE BORDER=\"0\" BGCOLOR=\"BLACK\" CELLPADDING=\"0\" CELLSPACING=\"1\" WIDTH=\"95%\" ><TR><TD><TABLE BORDER=\"0\" BGCOLOR=\"#FAFAD2\" CELLPADDING=\"10\" CELLSPACING=\"0\" WIDTH=\"100%\"><TR><TD CLASS=\"code-text\"><SPAN>",
											"</SPAN></TD></TR></TABLE></TD></TR></TABLE></CENTER><SPAN style=\"font-size:3pt\"><BR></SPAN>",
											"[src]", "[/src]" };*/
	private static String[] SRC			= {"<div style=\"border: 1px solid rgb(140,140,140); padding: 5px; background-color: rgb(250, 250, 210);\">",
											"</div>",
											"[src]", "[/src]" };
    private static String[] LISTUS       = {"<ul>", "</ul>", "[ul]", "[/ul]"};
    private static String[] LISTOS       = {"<ol>", "</ol>", "[ol]", "[/ol]"};
    private static String[] LISTOA       = {"<ol type=\"a\">", "</ol>", "[ola]", "[/ola]"};
    
    private static String[] LIST        = {"<li>", "</li>", "[li]", "[/li]"};

    private static String[] RED			= {"<FONT COLOR=\"red\">","</FONT>","[red]","[/red]"};
	private static String[] GREEN 		= {"<FONT COLOR=\"green\">","</FONT>","[green]","[/green]"};
	private static String[] BLUE		= {"<FONT COLOR=\"blue\">","</FONT>","[blue]","[/blue]"};
	private static String[] PURPLE		= {"<FONT COLOR=\"purple\">","</FONT>","[purple]","[/purple]"};
	private static String[] ORANGE		= {"<FONT COLOR=\"orange\">","</FONT>","[orange]","[/orange]"};
	private static String[] TEAL		= {"<FONT COLOR=\"teal\">","</FONT>","[teal]","[/teal]"};
	private static String[] BROWN		= {"<FONT COLOR=\"brown\">","</FONT>","[brown]","[/brown]"};
	private static String[] GRAY		= {"<FONT COLOR=\"gray\">","</FONT>","[gray]","[/gray]"};
	
	private static String	ISSUE_TAG	= "[issue";
	private static String	OPEN		= "[/";
	private static String	CLOSE		= "/]";
	private static String	CLOSE2		= "]";
	private static String	A_OPEN		= "<a class=\"body-text\" target=\"_blank\" href=\"";
	private static String	PARAM		= "/printItem.action?key=";
	private static String	HTML_CLOSE	= "\">";
	private static String   VCHTML_CLOSE= "\" target=\"_blank\">";
	private static String	NBSP		= "&nbsp;";
	private static String	A_CLOSE		= "</a>";
	private static String	URL			= "[url=";
	private static String	URL_CLOSE	= "[/url]";
    private static String   VC_TAG      = "[vc=";
	private static String   VC_CLOSE    = "[/vc]";
	private static String   REV         = "rev=";
	private static String   REV_START   = "?rev=";
	private static String   REV_END     = "&content-type=text/vnd.viewcvs-markup";
	
	private static String	APP_RES		= "resources.ApplicationResources";
	private static String	ITEM_KEY	= "item.lbl.itemNumber";
    private Locale locale = null;
    private boolean isMail = false;
    private TProject project = null;
    
    private String contextPath;

    /**
     * @param _locale the currently active locale
     */    
    public TagReplacer(Locale _locale) {
        this.locale = _locale;
        this.isMail = true;
    }
    	
    public void setContextPath(String contextPath){
    	this.isMail = false;
    	this.contextPath=contextPath;
    	if(this.contextPath==null){
    		this.contextPath="";
    	}
    	if(!this.contextPath.endsWith("/")){
    		this.contextPath=this.contextPath+"";
    	}
    }
    
//    public String replace (String input, HttpSession _session) {
//        return replace(input, (TProject) _session.getAttribute("currentProject"));
//    }
	
	public String replace (String input, TProject _project){
		if (input == null)
            return (null);
		this.project = _project;
        char content[] = new char[input.length()];
        input.getChars(0, input.length(), content, 0);
        StringBuffer result = new StringBuffer(content.length + 50);
	// String lineSeparator = System.getProperty("line.separator");
		
	Perl5Compiler pc = new Perl5Compiler();
	Perl5Matcher pm = new Perl5Matcher();
	Perl5Pattern perl5Pattern = null;
	Perl5Substitution perl5Sub;
	
	/*perl5Sub = new Perl5Substitution("s",
            Perl5Substitution.INTERPOLATE_NONE);
	
	try {
	    perl5Pattern = (Perl5Pattern) pc.compile("(\133ul\135k\133\135)",
                                     Perl5Compiler.CASE_INSENSITIVE_MASK | 
                                     Perl5Compiler.SINGLELINE_MASK);
	}
	catch (MalformedPatternException e) {
	    System.out.println("Malformed replace pattern.");
	}

	result.append(Util.substitute(pm, 
            perl5Pattern, 
            perl5Sub,
            input,
            Util.SUBSTITUTE_ALL));

	input = result.toString();
	*/
	perl5Sub = new Perl5Substitution("<br/>\n",
            Perl5Substitution.INTERPOLATE_NONE);
	
	try {
	    perl5Pattern = (Perl5Pattern) pc.compile("(\015\n|\n\015|\n|\015)",
                                     Perl5Compiler.CASE_INSENSITIVE_MASK | 
                                     Perl5Compiler.SINGLELINE_MASK);
	}
	catch (MalformedPatternException e) {
	    System.out.println("Malformed replace pattern.");
	}
        result.append(Util.substitute(pm, 
                                 perl5Pattern, 
                                 perl5Sub,
                                 input,
                                 Util.SUBSTITUTE_ALL));

        return (result.toString());
	}
	
	public String processSquareBracketsTags(String toFormat ) {
	    if (toFormat == null) return null;				
	    
	    StringBuffer res = new StringBuffer(toFormat );
	    
	    processSquareBracketsTag(res, BOLD );
	    processSquareBracketsTag(res, ITALIC );
	    processSquareBracketsTag(res, UNDERLINE );
        processSquareBracketsTag(res, LISTUS );
        processSquareBracketsTag(res, LISTOS );
        processSquareBracketsTag(res, LISTOA );
        processSquareBracketsTag(res, LIST );
	    
	    processSquareBracketsTag(res, RED );
	    processSquareBracketsTag(res, GREEN );
	    processSquareBracketsTag(res, BLUE );
	    processSquareBracketsTag(res, PURPLE );
	    processSquareBracketsTag(res, ORANGE );
	    processSquareBracketsTag(res, TEAL );
	    processSquareBracketsTag(res, BROWN );
	    processSquareBracketsTag(res, GRAY );
	    
	    processSquareBracketsTag(res, SRC );
	    //res = processIssueLinks(res);
	    res = processURL(res);
	    res = processVC(res);
	    
	    return removeListLineBreaks(res.toString());
	}

	String removeListLineBreaks(String input)
	{	    
	    
		Perl5Compiler pc = new Perl5Compiler();
		Perl5Matcher pm = new Perl5Matcher();
		Perl5Pattern perl5Pattern = null;
		
		Perl5Substitution perl5Sub;
		//remove <br>, space and tab between <ul> and <li>
		perl5Sub = new Perl5Substitution("\074ul\076\074li\076",
	            Perl5Substitution.INTERPOLATE_NONE);
		
		try {
		    perl5Pattern = (Perl5Pattern) pc.compile("\074ul\076(\074br\076|\040|\011)*\074li\076",
	                                     Perl5Compiler.CASE_INSENSITIVE_MASK | 
	                                     Perl5Compiler.SINGLELINE_MASK);
		}
		catch (MalformedPatternException e) {
		    System.out.println("Malformed replace pattern.");
		}		
		input = Util.substitute(pm, 
	            perl5Pattern, 
	            perl5Sub,
	            input,
	            Util.SUBSTITUTE_ALL);
				
		//remove <br>, space and tab between <ol> and <li>
		perl5Sub = new Perl5Substitution("\074ol\076\074li\076",
	            Perl5Substitution.INTERPOLATE_NONE);
		
		try {
		    perl5Pattern = (Perl5Pattern) pc.compile("\074ol\076(\074br\076|\040|\011)*\074li\076",
	                                     Perl5Compiler.CASE_INSENSITIVE_MASK | 
	                                     Perl5Compiler.SINGLELINE_MASK);
		}
		catch (MalformedPatternException e) {
		    System.out.println("Malformed replace pattern.");
		}		
		input = Util.substitute(pm, 
	            perl5Pattern, 
	            perl5Sub,
	            input,
	            Util.SUBSTITUTE_ALL);				


		//remove <br>, space and tab between <ol type="a"> and <li>
		perl5Sub = new Perl5Substitution("\074ol\040type=\042a\042\076\074li\076",
	            Perl5Substitution.INTERPOLATE_NONE);
		
		try {
		    perl5Pattern = (Perl5Pattern) pc.compile("\074ol\040type=\042a\042\076(\074br\076|\040|\011)*\074li\076",
	                                     Perl5Compiler.CASE_INSENSITIVE_MASK | 
	                                     Perl5Compiler.SINGLELINE_MASK);
		}
		catch (MalformedPatternException e) {
		    System.out.println("Malformed replace pattern.");
		}		
		input = Util.substitute(pm, 
	            perl5Pattern, 
	            perl5Sub,
	            input,
	            Util.SUBSTITUTE_ALL);	
		
		//remove <br>, space and tab between </li> and <li>
		perl5Sub = new Perl5Substitution("\074\057li\076\074li\076",
	            Perl5Substitution.INTERPOLATE_NONE);
		
		try {
		    perl5Pattern = (Perl5Pattern) pc.compile("\074\057li\076(\074br\076|\040|\011)*\074li\076",
	                                     Perl5Compiler.CASE_INSENSITIVE_MASK | 
	                                     Perl5Compiler.SINGLELINE_MASK);
		}
		catch (MalformedPatternException e) {
		    System.out.println("Malformed replace pattern.");
		}		
		input = Util.substitute(pm, 
	            perl5Pattern, 
	            perl5Sub,
	            input,
	            Util.SUBSTITUTE_ALL);				

		//remove <br>, space and tab between </li> and </ul>
		perl5Sub = new Perl5Substitution("\074\057li\076\074\057ul\076",
	            Perl5Substitution.INTERPOLATE_NONE);
		
		try {
		    perl5Pattern = (Perl5Pattern) pc.compile("\074\057li\076(\074br\076|\040|\011)*\074\057ul\076",
	                                     Perl5Compiler.CASE_INSENSITIVE_MASK | 
	                                     Perl5Compiler.SINGLELINE_MASK);
		}
		catch (MalformedPatternException e) {
		    System.out.println("Malformed replace pattern.");
		}		
		input = Util.substitute(pm, 
	            perl5Pattern, 
	            perl5Sub,
	            input,
	            Util.SUBSTITUTE_ALL);
		
		//remove <br>, space and tab between </li> and </ol>
		perl5Sub = new Perl5Substitution("\074\057li\076\074\057ol\076",
	            Perl5Substitution.INTERPOLATE_NONE);
		
		try {
		    perl5Pattern = (Perl5Pattern) pc.compile("\074\057li\076(\074br\076|\040|\011)*\074\057ol\076",
	                                     Perl5Compiler.CASE_INSENSITIVE_MASK | 
	                                     Perl5Compiler.SINGLELINE_MASK);
		}
		catch (MalformedPatternException e) {
		    System.out.println("Malformed replace pattern.");
		}		
		input = Util.substitute(pm, 
	            perl5Pattern, 
	            perl5Sub,
	            input,
	            Util.SUBSTITUTE_ALL);
		
		return input;
	}
	
	private void processSquareBracketsTag(	StringBuffer src, final String[] tags ) {
	    processSquareBracketsTag(	src, tags[0], tags[1], tags[2], tags[3] );
	}

	private void processSquareBracketsTag(	StringBuffer src, 
	        final String openTag, final String closeTag,
	        final String openSquareTag, final String closeSquareTag  ) {
	    
	    int index = 0;
	    while ((index = src.toString().indexOf(openSquareTag, index ) ) != -1 ) {
	        int closeIndex = src.toString().indexOf(closeSquareTag, index );
	        
	        if (closeIndex == -1 || (closeIndex <= index)) {
	            src.replace(index, index + openSquareTag.length(), EMPTY );				
	        } else {
	            if (SRC[2].equals(openSquareTag)) {
	                String theSource = src.substring(index,closeIndex).toString();
	                theSource = theSource.replaceAll(" ","&nbsp;");
	                theSource = theSource.replaceAll("\\t","&nbsp;&nbsp;&nbsp;&nbsp;");
	                src.replace(index,closeIndex,theSource);
	                index = src.toString().indexOf(openSquareTag, index );			   
	            }
	            src.replace(index, index + openSquareTag.length(), openTag );
	            closeIndex = src.toString().indexOf(closeSquareTag, index );
	            src.replace(closeIndex, closeIndex + closeSquareTag.length(), closeTag);				
	            
	            index = closeIndex;
	        }
	    }
	    
	    index = 0;
	    while ((index = src.toString().indexOf(closeSquareTag, index ) ) != -1 ) {
	        src.replace(index, index + closeSquareTag.length(), EMPTY );
	    }
	    
	    index = 0;
	    while ((index = src.toString().indexOf(openSquareTag, index ) ) != -1 ) {
	        src.replace(index, index + openSquareTag.length(), EMPTY );
	    }	
	}
	public String formatDescription(String source){
		return formatDescription(source,true,false,false);
	}
	public String formatDescription(String source,boolean simple,boolean export,boolean useProjectSpecificID){
		if (source==null) {
			return null;
		}
		return processIssueLinks(new StringBuffer(source),simple,export,useProjectSpecificID).toString();
	}
	
	private StringBuffer processIssueLinks( StringBuffer src,boolean simple,boolean export,boolean useProjectSpecificID) {
		String linkText ="";
		int index = 0;
		while ((index = src.toString().indexOf(ISSUE_TAG, index ) ) != -1 ) {
			int endIndex = src.toString().indexOf(CLOSE, index );
			if (endIndex == -1 || (endIndex <= index)) {
				src.replace(index, index + ISSUE_TAG.length(), EMPTY );
			} else {
				String key = src.substring(
						index + ISSUE_TAG.length(), endIndex ).trim();
				try {
					Integer itemID=Integer.decode(key );
					TWorkItemBean itemBean = null;
					try {
						itemBean = ItemBL.loadWorkItem(itemID);
					} catch (ItemLoaderException e) {
						LOGGER.warn("Loading the workItemID " + itemID + " failed with " + e.getMessage(), e);
					}
					if(itemBean==null){
						src.replace(index, endIndex + CLOSE.length(), EMPTY );
					}
					else{
						linkText=key+":"+itemBean.getSynopsis();
						StringBuffer atag = new StringBuffer();
						if(export) {
							atag.append("<div class=\"inlineItem\">");
							String description = itemBean.getDescription();
							if (description == null || description.length() == 0) {
								atag.append(itemBean.getSynopsis());
							} else {
								atag.append(formatDescription(description, true,false,false));
							}
							atag.append("</div>");
						}else{
							if (simple) {
								atag.append(A_OPEN);
								atag.append(contextPath + PARAM + key);
								atag.append(HTML_CLOSE).append(linkText).append(A_CLOSE);
							} else {
								atag.append("<div class=\"inlineItem\">");
								atag.append("<div class=\"inlineItem\">");
								atag.append("<div class=\"inlineItemIcon\">");
								atag.append(A_OPEN);
								atag.append(contextPath + PARAM + key);
								atag.append(HTML_CLOSE).append("<img src=\"optionIconStream.action?fieldID=-2&optionID=" + itemBean.getListTypeID() + "\"/>").append(A_CLOSE);
								atag.append("<div class=\"inlineItemBody\">");
								String description = itemBean.getDescription();
								if (description == null || description.length() == 0) {
									atag.append(itemBean.getSynopsis());
								} else {
									atag.append(formatDescription(description, true,false,false));
								}
								atag.append("</div>");
								List<ItemLinkListEntry> links= ItemLinkBL.getLinks(itemBean.getObjectID(), false, locale);
								if(links!=null && !links.isEmpty()){
									atag.append("<div class=\"inlineItemLinks\">");
									for(ItemLinkListEntry itemLinkListEntry:links){
										atag.append("<div class=\"inlineLink\">");
										Integer linkedWorkItemID = itemLinkListEntry.getLinkedWorkItemID();
										String itemIDStr = ItemBL.getItemNo(linkedWorkItemID);
										atag.append(itemLinkListEntry.getLinkTypeName()+"&nbsp;&nbsp;");
										atag.append(A_OPEN);
										atag.append(contextPath + PARAM + linkedWorkItemID);
										atag.append(HTML_CLOSE).append(itemIDStr).append(":").append(itemLinkListEntry.getLinkedWorkItemTitle()).append(A_CLOSE);
										atag.append("</div>");
									}
									atag.append("</div>");
								}
								atag.append("</div>");
							}
						}

						src.replace(index, endIndex + CLOSE.length(), atag.toString() );

						atag = null;

						index = endIndex;
					}
				} catch (NumberFormatException e ) {
					src=src.replace(index, index + ISSUE_TAG.length(), EMPTY );
					//recalculate end index
					endIndex = src.toString().indexOf(CLOSE, index );
					src=src.replace(endIndex, endIndex + CLOSE.length(), EMPTY );
				}
			}
		}
		return src;
	}

	
	
	
	
	
	
	public static List<Integer> gatherIssueLinks(StringBuilder src ) {
		List<Integer> workItemIDs = new ArrayList<Integer>();
		//String linkText ="";
		int index = 0;
		while ((index = src.toString().indexOf(ISSUE_TAG, index ) ) != -1 ) {
			int endIndex = src.toString().indexOf(CLOSE, index );
			if (endIndex == -1 || (endIndex <= index)) {
				src.replace(index, index + ISSUE_TAG.length(), EMPTY );
			} else {
				String key = src.substring(
						index + ISSUE_TAG.length(), endIndex ).trim();
				try {
					Integer itemID=Integer.decode(key);
					if (itemID==null) {
						src.replace(index, endIndex + CLOSE.length(), EMPTY);
					} else{
						workItemIDs.add(itemID);
						index = endIndex;
					}
				} catch (NumberFormatException e ) {
					src=src.replace(index, index + ISSUE_TAG.length(), EMPTY );
					//recalculate end index
					endIndex = src.toString().indexOf(CLOSE, index );
					src=src.replace(endIndex, endIndex + CLOSE.length(), EMPTY );
				}
			}
		}
		return workItemIDs;
	}

	
	public static boolean replaceIssueLinks(StringBuilder src, Map<Integer, Integer> workItemIDsMap) {	    	        
	    boolean replaced = false;
		int index = 0;
	    while ((index = src.toString().indexOf(ISSUE_TAG, index ) ) != -1 ) {
	        int endIndex = src.toString().indexOf(CLOSE, index );
	        if (endIndex == -1 || (endIndex <= index)) {
	            src.replace(index, index + ISSUE_TAG.length(), EMPTY );
	        } else {
	            String key = src.substring(
	                    index + ISSUE_TAG.length(), endIndex ).trim();					
	            try {																
	                Integer itemID=Integer.decode(key );	                
	                if(itemID==null){
	                	src.replace(index, endIndex + CLOSE.length(), EMPTY );
	                } else{	
	                	Integer replacementItemID = workItemIDsMap.get(itemID);
	                	if (replacementItemID!=null) {
	                		replaced = true;
	                		src.replace(index + ISSUE_TAG.length(), endIndex, " " + replacementItemID.toString());
	                	}
		                index = endIndex;
	                }
	            } catch (NumberFormatException e ) {
	            	src=src.replace(index, index + ISSUE_TAG.length(), EMPTY );
	            	//recalculate end index
	            	endIndex = src.toString().indexOf(CLOSE, index );
	            	src=src.replace(endIndex, endIndex + CLOSE.length(), EMPTY );
	            }
	        }
	    }
	    return replaced;
	}
	
	public static boolean replaceIssueLinks(StringBuffer src, Map<Integer, Integer> workItemIDsMap) {	    	        
	    boolean replaced = false;
		int index = 0;
	    while ((index = src.toString().indexOf(ISSUE_TAG, index ) ) != -1 ) {
	        int endIndex = src.toString().indexOf(CLOSE, index );
	        if (endIndex == -1 || (endIndex <= index)) {
	            src.replace(index, index + ISSUE_TAG.length(), EMPTY );
	        } else {
	            String key = src.substring(
	                    index + ISSUE_TAG.length(), endIndex ).trim();					
	            try {																
	                Integer itemID=Integer.decode(key );	                
	                if(itemID==null){
	                	src.replace(index, endIndex + CLOSE.length(), EMPTY );
	                } else{	
	                	Integer replacementItemID = workItemIDsMap.get(itemID);
	                	if (replacementItemID!=null) {
	                		replaced = true;
	                		src.replace(index + ISSUE_TAG.length(), endIndex, " " + replacementItemID.toString());
	                	}
		                index = endIndex;
	                }
	            } catch (NumberFormatException e ) {
	            	src=src.replace(index, index + ISSUE_TAG.length(), EMPTY );
	            	//recalculate end index
	            	endIndex = src.toString().indexOf(CLOSE, index );
	            	src=src.replace(endIndex, endIndex + CLOSE.length(), EMPTY );
	            }
	        }
	    }
	    return replaced;
	}
	
	private StringBuffer processURL( StringBuffer src ) {
	    int index = 0;
	    while ((index = src.toString().indexOf(URL, index ) ) != -1 ) {
	        int endIndex = src.toString().indexOf(CLOSE2, index );
	        int startCloseIndex = src.toString().indexOf(OPEN, index + URL.length() );
	        if (endIndex == -1 || endIndex > startCloseIndex ) {
	            src.replace(index, index + URL.length(), EMPTY );
	        } else {
	            String url = src.substring(index + URL.length(), endIndex ).trim();
	            int closeIndex = src.toString().indexOf(URL_CLOSE, endIndex + CLOSE2.length() );
	            if (closeIndex == -1 ) {
	                src.replace(index, index + URL.length(), EMPTY );
	            } else {
	                String urlText = src.substring(endIndex + CLOSE2.length(), closeIndex ).trim();
	                
	                StringBuffer atag = new StringBuffer(A_OPEN );
	                atag.append(url ).append(HTML_CLOSE );
	                atag.append(urlText).append(A_CLOSE );
	                
	                src.replace(index, closeIndex + URL_CLOSE.length(), atag.toString() );	
	                atag = null;
	            }
	            index = endIndex;
	        }
	    }
	    
	    index = 0;
	    while ((index = src.toString().indexOf(URL_CLOSE, index ) ) != -1 ) {
	        src.replace(index, index + URL_CLOSE.length(), EMPTY );
	    }
	    
	    index = 0;
	    while ((index = src.toString().indexOf(URL, index ) ) != -1 ) {
	        src.replace(index, index + URL_CLOSE.length(), EMPTY );
	    }
	    return src;
	}
	
	private StringBuffer processVC(	StringBuffer src ) {
		StringBuffer vcURL = new StringBuffer();	

		if (project != null) {
		  vcURL = vcURL.append(project.getVersionSystemField0() 
		          + "/" + project.getVersionSystemField1() + "/");  
		}
		
		int index = 0;
		while ((index = src.toString().indexOf(VC_TAG, index ) ) != -1 ) {
			int endIndex = src.toString().indexOf(CLOSE2, index );
			int startCloseIndex = src.toString().indexOf(OPEN, index + VC_TAG.length() );
			if (endIndex == -1 || endIndex > startCloseIndex ) {
				src.replace(index, index + VC_TAG.length(), EMPTY );
			} else {
				StringBuffer url = new StringBuffer(vcURL.toString() 
				             + src.substring(index + VC_TAG.length(), endIndex ).trim());									
				String revision = null;
				int startRevIndex = url.indexOf(REV, 0);
				if (startRevIndex != -1) {
				  revision = url.substring(startRevIndex+REV.length(),url.length()).trim();
				  url.replace(startRevIndex, url.length(), EMPTY);
				  url = new StringBuffer(url.toString().trim());
				  url.append(REV_START);
				  url.append(revision);
				  url.append(REV_END);
				}				
				int closeIndex = src.toString().indexOf(VC_CLOSE, endIndex + CLOSE2.length() ); 				
				if (closeIndex == -1 ) {
					src.replace(index, index + VC_TAG.length(), EMPTY );
				} else {
					String urlText = src.substring(endIndex + CLOSE2.length(), closeIndex ).trim();
					
					StringBuffer atag = new StringBuffer(A_OPEN );					
					atag.append(url ).append(VCHTML_CLOSE );
					atag.append(urlText).append(A_CLOSE );
														
					src.replace(index, closeIndex + VC_CLOSE.length(), atag.toString() );	
					atag = null;
				}																										
				index = endIndex;						
			}
		}
		
		index = 0;
		while ((index = src.toString().indexOf(VC_CLOSE, index ) ) != -1 ) {
			src.replace(index, index + VC_CLOSE.length(), EMPTY );
		}
		
		index = 0;
		while ((index = src.toString().indexOf(VC_TAG, index ) ) != -1 ) {
			src.replace(index, index + VC_CLOSE.length(), EMPTY );
		}
	return src;
	}
}
