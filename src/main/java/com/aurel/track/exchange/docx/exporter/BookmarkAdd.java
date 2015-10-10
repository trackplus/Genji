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

package com.aurel.track.exchange.docx.exporter;


import java.math.BigInteger;

import javax.xml.bind.JAXBElement;

import org.docx4j.jaxb.Context;
import org.docx4j.wml.CTBookmark;
import org.docx4j.wml.CTMarkupRange;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;


public class BookmarkAdd  {
	
	/**
	 * Surround the specified r in the specified p
	 * with a bookmark with specified name and id
	 * @param p
	 * @param r
	 * @param name
	 * @param id
	 */
	public static void bookmarkRun(P p, R r, String name, int id) {
		// Find the index for the run to put the bookmark start and end before and after the run
		int index = p.getContent().indexOf(r);
		if (index<0) {
			System.out.println("P does not contain R!");
			return;
		}
		ObjectFactory factory = Context.getWmlObjectFactory();
		BigInteger ID = BigInteger.valueOf(id);
		// Add bookmark end first after the run
		CTMarkupRange mr = factory.createCTMarkupRange();
		mr.setId(ID);
		JAXBElement<CTMarkupRange> bmEnd = factory.createBodyBookmarkEnd(mr);
		p.getContent().add(index+1, bmEnd); 
		// Next, bookmark start before the run
		CTBookmark bm = factory.createCTBookmark();
		bm.setId(ID);
		bm.setName(name);		
		JAXBElement<CTBookmark> bmStart =  factory.createBodyBookmarkStart(bm);
		p.getContent().add(index, bmStart);
	}

}
