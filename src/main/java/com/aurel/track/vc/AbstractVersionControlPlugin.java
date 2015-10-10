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

package com.aurel.track.vc;

import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.LabelValueBean;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * An abstract implementation for VersionControlPlugin
 * @author Adrian Bojani
 *
 */
public abstract class AbstractVersionControlPlugin implements VersionControlPlugin{

	
	
	public List<LabelValueBean> verify(Map<String,String> parameters,Locale locale){
		return null;
	}
	public String getRepository(Map<String,String> parameters){
		String accesMethod= parameters.get("accessMethod");
     	String serverName=parameters.get("serverName");
     	String repositoryPath=parameters.get("repositoryPath");
     	String port= parameters.get("port");
     	
         StringBuffer sb=new StringBuffer();
     	sb.append(accesMethod);
     	sb.append("://");
     	sb.append(serverName);
     	if(port!=null&&port.length()>0){
     		sb.append(":");
     		sb.append(port);
     	}
     	sb.append(repositoryPath);
     	String url=sb.toString();
     	return url;
	}
	public Map<Integer,List<Revision>> getLogs(Map<String,String> parameters,String prefixIssueNumber){
        Map<Integer,List<Revision>> map=new HashMap<Integer, List<Revision>>();
        List<Revision> revisions=getRevisions(parameters,prefixIssueNumber);
        for(int i=0;i<revisions.size();i++){
            Revision revision=revisions.get(i);
            Set<Integer> workItemIDs=getWorkItemIDs(revision.getRevisionComment(),prefixIssueNumber);
            if(workItemIDs==null||workItemIDs.isEmpty()){
            	continue;
            }
            for (Iterator<Integer> iterator = workItemIDs.iterator(); iterator.hasNext();) {
            	Integer workItemID = iterator.next();
            	List<Revision> l=map.get(workItemID);
                if(l==null){
                    l=new ArrayList<Revision>();
                }
                l.add(revision);
                map.put(workItemID, l);
			}
        }
        return map;
	}
	
	/**
	 * Obtain the ids of items from string
	 * an id is a int number preceded by "#" 
	 * @param s
	 * @return
	 */
	protected Set<Integer> getWorkItemIDs(String s,String prefixIssueNumber){
        Set<Integer> integers = new HashSet<Integer>();
        if(s==null||s.length()==0){
            return integers;
        }
        int index=s.indexOf(prefixIssueNumber);
        int sizePrefixIssueNumber=prefixIssueNumber.length();
        if (index==-1||index==s.length()-sizePrefixIssueNumber) {
        	return integers;
        }
        int count;
        while(index!=-1&&index<s.length()){
            index=index+sizePrefixIssueNumber;
        	count = index;
            while (count < s.length() && Character.isDigit(s.charAt(count))){
                count++;
            }
            if (count > index){
            	try{
            		integers.add(new Integer(s.substring(index,count)));
            	}catch (Exception e) {
					// just ignore
				}
            }
            index=s.indexOf(prefixIssueNumber,count-sizePrefixIssueNumber+1);
        }
        return integers;
	}
	
	/**
	 * Obtain the revisions
	 * @param parameteres
	 * @param prefixIssueNumber
	 * 
	 * @return
	 */
	protected abstract List<Revision> getRevisions(Map<String,String> parameteres,String prefixIssueNumber);
	
	/**
	 * Format a date in iso dateTime format
	 * @param date
	 * @return
	 */
	protected static String formatDate(Date date){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd H:mm:ss.S");
		return sdf.format(date);
	}
	protected static String getText(String s, Locale locale){
		return LocalizeUtil.getLocalizedTextFromApplicationResources(s, locale);
	}
}
