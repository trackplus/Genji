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

package com.trackplus.track.util.html;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class HtmlCleaner {
	
	public final static Whitelist whiteList  =  Whitelist.relaxed();
	
	public static HtmlCleaner cleaner = null;
	
	private HtmlCleaner() {
		// here we can add or remove tags from the standard list
	}
	
	/**
	 * Get an HtmlCleaner instance to clean untrusted HTML based on 
	 * a white-list of permitted tags and attributes. These are the
	 * text nodes: a, b, blockquote, br, caption, cite, code, col, 
	 * colgroup, dd, div, dl, dt, em, h1, h2, h3, h4, h5, h6, i, img, 
	 * li, ol, p, pre, q, small, span, strike, strong, sub, sup, 
	 * table, tbody, td, tfoot, th, thead, tr, u, ul
	 * @return
	 */
	public HtmlCleaner getHtmlCleaner() {
		
		if (cleaner == null) {
			cleaner = new HtmlCleaner();
		}
		return cleaner;
	}
	
	/**
	 * Get safe HTML from untrusted input HTML, by parsing input HTML and filtering it 
	 * through a white-list of permitted tags and attributes.
	 * @param bodyHtml
	 * @return the clean HTML
	 */
	public String clean (String bodyHtml) {
		return Jsoup.clean(bodyHtml, whiteList);
	}
	
	/**
	 * Get simple text with all HTML removed.
	 * @param bodyHtml
	 * @return the simple text representation of the bodyHtml
	 */
	public String getSimpleText (String bodyHtml) {
		return Jsoup.clean(bodyHtml, Whitelist.simpleText());
	}

}
