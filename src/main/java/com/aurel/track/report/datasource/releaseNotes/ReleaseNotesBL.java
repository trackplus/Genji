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

package com.aurel.track.report.datasource.releaseNotes;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.report.dashboard.ReleaseNoteWrapper;

public class ReleaseNotesBL {
	
	private static final Logger LOGGER = LogManager.getLogger(ReleaseNotesBL.class);		
	
	static Document convertToDOM(TReleaseBean releaseBean, List<ReleaseNoteWrapper> releaseNotes, Locale locale,String personName) {		
		Document dom = null;
		try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
			DocumentBuilder builder = factory.newDocumentBuilder ();
			dom = builder.newDocument ();	
		}catch (FactoryConfigurationError e){
			LOGGER.error ("Creating the DOM document failed with FactoryConfigurationError:" + e.getMessage());
			return null;
		}catch (ParserConfigurationException e){
			LOGGER.error ("Creating the DOM document failed with ParserConfigurationException: " + e.getMessage());
			return null;
		}
		Element root = dom.createElement ("track-report");
        Element createdBy =createDomElement("createdBy",personName,dom);
        root.appendChild(createdBy);
        Element release = createDomElement("release", releaseBean.getLabel(),dom);

        Iterator<ReleaseNoteWrapper> iterator = releaseNotes.iterator();
        while (iterator.hasNext()) {
        	ReleaseNoteWrapper relwrap = iterator.next();
        	TListTypeBean issueTypeBean = relwrap.getIssueType();
        	Element issueType = createDomElement("issueType", issueTypeBean.getLabel(), dom);

        	List<TWorkItemBean> witems = relwrap.getWorkItems();
        	Iterator<TWorkItemBean> witemIterator = witems.iterator();
        	while (witemIterator.hasNext()) {
        		TWorkItemBean twbean = witemIterator.next();
            	Element item = createDomElement("item","",dom);
            	item.appendChild(createDomElement("issueno", twbean.getObjectID().toString(), dom));
            	item.appendChild(createDomElement("title", twbean.getSynopsis(), dom));
            	release.appendChild(item);        		
        	}
        	release.appendChild(issueType);
        }
        root.appendChild(release);
        dom.appendChild (root);
        return dom;
	}
	
	/**
	 * @param elementName
	 * @param elementValue
	 * @param dom
	 * @return
	 */
	private static Element createDomElement (String elementName, String elementValue, Document dom) {
		Element element = dom.createElement (elementName);
		if (elementValue == null || "".equals(elementValue.trim())) {
			element.appendChild (dom.createTextNode (""));
		} else {
			element.appendChild (dom.createTextNode(elementValue));
		}						
		return element;
	}
}
