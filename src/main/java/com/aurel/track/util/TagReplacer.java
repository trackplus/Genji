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

package com.aurel.track.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.item.link.ItemLinkBL;
import com.aurel.track.item.link.ItemLinkListEntry;

public class TagReplacer {
	private static final Logger LOGGER = LogManager.getLogger(TagReplacer.class);

    private static final String	EMPTY		= "";
	
	private static final String	ISSUE_TAG	= "[issue";

	private static final String	CLOSE		= "/]";

	private static final String	A_OPEN		= "<a class=\"body-text\" target=\"_blank\" href=\"";
	private static final String	PARAM		= "/printItem.action?key=";
	private static final String	HTML_CLOSE	= "\">";

	private static final String	NBSP		= "&nbsp;";
	private static final String	A_CLOSE		= "</a>";

	
    private Locale locale = null;
    
    private String contextPath;

    /**
     * @param _locale the currently active locale
     */    
    public TagReplacer(Locale _locale) {
        this.locale = _locale;
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
    	
	public String formatDescription(String source){
		return formatDescription(source,true,false,false);
	}
	public String formatDescription(String source,boolean simple,boolean export,boolean useProjectSpecificID){
		if (source==null) {
			return null;
		}
		return processIssueLinks(new StringBuilder(source),simple,export,useProjectSpecificID).toString();
	}
	
	private StringBuilder processIssueLinks( StringBuilder src,boolean simple,boolean export,boolean useProjectSpecificID) {
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
						LOGGER.warn("Loading the workItemID " + itemID + " failed with " + e.getMessage());
					}
					if(itemBean==null){
						src.replace(index, endIndex + CLOSE.length(), EMPTY );
					}
					else{
						linkText=key+":"+itemBean.getSynopsis();
						StringBuilder atag = new StringBuilder();
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
										atag.append(itemLinkListEntry.getLinkTypeName()+NBSP+NBSP);
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

}
