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

package com.aurel.track.util.emailHandling;

import java.util.Set;

public class MailImageBL {

	public static String replaceInlineImages(String message,Set<String> attachments){
		StringBuffer s=new StringBuffer(message);
		int index = 0;
		int idxStart=-1;
		int idxEnd=-1;
		String IMAGE="<img ";
		String INLINE_IMAGE="downloadAttachment.action?workItemID=";//&attachKey=
		while ((index = s.indexOf(IMAGE, index ) ) != -1 ) {
			idxStart=index;
			int idxData=s.indexOf(INLINE_IMAGE,index);
			if(idxData!=-1){
				idxEnd=s.indexOf(">",index);
				if(idxEnd>idxData){
					int idxAnd=s.indexOf("&amp;",idxData);
					if(idxAnd!=-1){
						String workItemID=s.substring(idxData+INLINE_IMAGE.length(),idxAnd);
						int idxAttachIdStart=idxAnd+"attachKey=".length()+5;
						int idxAttachIdEnd=s.indexOf("\"",idxAttachIdStart);
						String attachID=s.substring(idxAttachIdStart,idxAttachIdEnd);
						idxEnd=s.indexOf(">",idxAttachIdEnd)+1;
						s=s.replace(idxStart,idxEnd,formatImageTag(workItemID+"_"+attachID));
						attachments.add(workItemID+"_"+attachID);
					}
				}
			}
			index=index+5;
		}
		return s.toString();
	}
	public static String formatImageTag(String id){
		return "<img src=\"cid:"+id+"\">";
	}
}
