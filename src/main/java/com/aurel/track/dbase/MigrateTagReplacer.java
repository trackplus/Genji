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

package com.aurel.track.dbase;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aurel.track.persist.TProject;

public class MigrateTagReplacer {
	private static final Logger LOGGER = LogManager.getLogger(MigrateTagReplacer.class);

    private static final String	EMPTY		= "";
	private static final String[] BOLD		= {"<strong>", "</strong>", "[b]", "[/b]" };
	private static final String[] ITALIC		= {"<em>", "</em>", "[i]", "[/i]" };
	private static final String[] UNDERLINE	= {"<u>", "</u>", "[u]", "[/u]" };

	private static final String[] SRC			= {"<div style=\"border: 1px solid rgb(140,140,140); padding: 5px; background-color: rgb(250, 250, 210);\">",
											"</div>",
											"[src]", "[/src]" };
    private static final String[] LISTUS       = {"<ul>", "</ul>", "[ul]", "[/ul]"};
    private static final String[] LISTOS       = {"<ol>", "</ol>", "[ol]", "[/ol]"};
    private static final String[] LISTOA       = {"<ol type=\"a\">", "</ol>", "[ola]", "[/ola]"};
    
    private static final String[] LIST        = {"<li>", "</li>", "[li]", "[/li]"};

    private static final String[] RED			= {"<FONT COLOR=\"red\">","</FONT>","[red]","[/red]"};
	private static final String[] GREEN 		= {"<FONT COLOR=\"green\">","</FONT>","[green]","[/green]"};
	private static final String[] BLUE		= {"<FONT COLOR=\"blue\">","</FONT>","[blue]","[/blue]"};
	private static final String[] PURPLE		= {"<FONT COLOR=\"purple\">","</FONT>","[purple]","[/purple]"};
	private static final String[] ORANGE		= {"<FONT COLOR=\"orange\">","</FONT>","[orange]","[/orange]"};
	private static final String[] TEAL		= {"<FONT COLOR=\"teal\">","</FONT>","[teal]","[/teal]"};
	private static final String[] BROWN		= {"<FONT COLOR=\"brown\">","</FONT>","[brown]","[/brown]"};
	private static final String[] GRAY		= {"<FONT COLOR=\"gray\">","</FONT>","[gray]","[/gray]"};
	

	private static final String	OPEN		= "[/";

	private static final String	CLOSE2		= "]";
	private static final String	A_OPEN		= "<a class=\"body-text\" target=\"_blank\" href=\"";

	private static final String	HTML_CLOSE	= "\">";
	private static final String VCHTML_CLOSE= "\" target=\"_blank\">";
	private static final String	NBSP		= "&nbsp;";
	private static final String	A_CLOSE		= "</a>";
	private static final String	URL			= "[url=";
	private static final String	URL_CLOSE	= "[/url]";
    private static final String VC_TAG      = "[vc=";
	private static final String VC_CLOSE    = "[/vc]";
	private static final String REV         = "rev=";
	private static final String REV_START   = "?rev=";
	private static final String REV_END     = "&content-type=text/vnd.viewcvs-markup";
	
    private TProject project = null;
    
    private String contextPath;

    /**
     * @param _locale the currently active locale
     */    
    public MigrateTagReplacer(Locale _locale) {
    	LOGGER.debug("Creating MigrateTagReplacer");
    }
    	
    public void setContextPath(String contextPath){
    	this.contextPath=contextPath;
    	if(this.contextPath==null){
    		this.contextPath="";
    	}
    	if(!this.contextPath.endsWith("/")){
    		this.contextPath=this.contextPath+"";
    	}
    }
	
	public String replace (String input, TProject _project){
		if (input == null)
            return (null);
		this.project = _project;
        char content[] = new char[input.length()];
        input.getChars(0, input.length(), content, 0);
        StringBuilder result = new StringBuilder(content.length + 50);
		
        Pattern pattern = Pattern.compile("(\015\n|\n\015|\n|\015)",
        		Pattern.CASE_INSENSITIVE );

        Matcher matcher = pattern.matcher(input);
        
        return result.append(matcher.replaceAll("<br/>\n")).toString();
	}
	
	
	public String processSquareBracketsTags(String toFormat ) {
	    if (toFormat == null) return null;				
	    
	    StringBuilder res = new StringBuilder(toFormat );
	    
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

	    res = processURL(res);
	    res = processVC(res);
	    
	    return removeListLineBreaks(res.toString());
	}

	String removeListLineBreaks(String input)
	{	    
		//remove <br>, space and tab between <ul> and <li>
        Pattern pattern = Pattern.compile("\074ul\076(\074br\076|\040|\011)*\074li\076",
        		Pattern.CASE_INSENSITIVE );
        Matcher matcher = pattern.matcher(input);
        input = matcher.replaceAll("\074ul\076\074li\076");
        matcher.reset(input);
				
		//remove <br>, space and tab between <ol> and <li>
        pattern = Pattern.compile("\074ol\076(\074br\076|\040|\011)*\074li\076",
        		Pattern.CASE_INSENSITIVE );
        matcher = pattern.matcher(input);
        input = matcher.replaceAll("\074ol\076\074li\076");
        matcher.reset(input);				
        
		//remove <br>, space and tab between <ol type="a"> and <li>
        pattern = Pattern.compile("\074ol\040type=\042a\042\076(\074br\076|\040|\011)*\074li\076",
        		Pattern.CASE_INSENSITIVE );
        matcher = pattern.matcher(input);
        input = matcher.replaceAll("\074ol\040type=\042a\042\076\074li\076");
        matcher.reset(input);
		
		//remove <br>, space and tab between </li> and <li>
        pattern = Pattern.compile("\074\057li\076(\074br\076|\040|\011)*\074li\076",
        		Pattern.CASE_INSENSITIVE );
        matcher = pattern.matcher(input);
        input = matcher.replaceAll("\074\057li\076\074li\076");
        matcher.reset(input);			

		//remove <br>, space and tab between </li> and </ul>
        pattern = Pattern.compile("\074\057li\076(\074br\076|\040|\011)*\074\057ul\076",
        		Pattern.CASE_INSENSITIVE );
        matcher = pattern.matcher(input);
        input = matcher.replaceAll("\074\057li\076\074\057ul\076");
        matcher.reset(input);	
		
		//remove <br>, space and tab between </li> and </ol>
        pattern = Pattern.compile("\074\057li\076(\074br\076|\040|\011)*\074\057ol\076",
        		Pattern.CASE_INSENSITIVE );
        matcher = pattern.matcher(input);
        input = matcher.replaceAll("\074\057li\076\074\057ol\076");
		
		return input;
	}
	
	private void processSquareBracketsTag(	StringBuilder src, final String[] tags ) {
	    processSquareBracketsTag(	src, tags[0], tags[1], tags[2], tags[3] );
	}

	private void processSquareBracketsTag(	StringBuilder src, 
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
	                theSource = theSource.replaceAll(" ",NBSP);
	                theSource = theSource.replaceAll("\\t",NBSP+NBSP+NBSP+NBSP);
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
	
	
	private StringBuilder processURL( StringBuilder src ) {
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
	                
	                StringBuilder atag = new StringBuilder(A_OPEN );
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
	
	private StringBuilder processVC(	StringBuilder src ) {
		StringBuilder vcURL = new StringBuilder();	

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
				StringBuilder url = new StringBuilder(vcURL.toString() 
				             + src.substring(index + VC_TAG.length(), endIndex ).trim());									
				String revision = null;
				int startRevIndex = url.indexOf(REV, 0);
				if (startRevIndex != -1) {
				  revision = url.substring(startRevIndex+REV.length(),url.length()).trim();
				  url.replace(startRevIndex, url.length(), EMPTY);
				  url = new StringBuilder(url.toString().trim());
				  url.append(REV_START);
				  url.append(revision);
				  url.append(REV_END);
				}				
				int closeIndex = src.toString().indexOf(VC_CLOSE, endIndex + CLOSE2.length() ); 				
				if (closeIndex == -1 ) {
					src.replace(index, index + VC_TAG.length(), EMPTY );
				} else {
					String urlText = src.substring(endIndex + CLOSE2.length(), closeIndex ).trim();
					
					StringBuilder atag = new StringBuilder(A_OPEN );					
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
