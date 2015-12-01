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

package com.aurel.track.exchange.docx.exporter;

import java.util.Iterator;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.aurel.track.exchange.docx.exporter.ImageOrTableCaption.ALIGN;

/**
 * Preprocess the html tables before feeding into XHTMLImporterImpl
 * @author Tamas
 *
 */
public class PreprocessTable extends PreprocessBase {
	
	
	/**
	 * Preprocess tables by removing the caption tag but saving it in tableCaptionsMap
	 * @param htmlContent
	 * @param tableCaptionsMap
	 * @return
	 */
	String prepocessTableCaption(String htmlContent, Map<String, ImageOrTableCaption> tableCaptionsMap) {
		Document doc = Jsoup.parseBodyFragment(htmlContent);
		Elements tableElements = doc.select("table");
		if (tableElements!=null) {
			for (Iterator<Element> iterator = tableElements.iterator(); iterator.hasNext();) {
				globalCounter++;
				counterWithinChapter++;
				Element tableElement = iterator.next();
				String tableID = "table"+globalCounter;
				//the id attribute of the table element is "translated" by XHTMLImporterImpl in word to a bookmark around table 
				//so the correspondence between table and his caption is stored in tableCaptionsMap based on this ID 
				tableElement.attr("id", tableID);
				String alignText = tableElement.attr("align");
				ALIGN align = null;
				if (alignText!=null) {
					if ("left".equals(alignText)) {
						align = ALIGN.LEFT;
					} else {
						if ("right".equals(alignText)) {
							align = ALIGN.RIGHT;
						} 
					}
				}
				String tableCaption = null;
				Elements tableChildren = tableElement.getAllElements();
				if (tableChildren!=null) {
					for (Element figureChild : tableChildren) {
						if ("caption".equals(figureChild.nodeName())) {
							tableCaption = figureChild.text();
							//remove caption because although it is transformed by XHTMLImporterImpl but
							//with wrong style and before table instead of after and without automatic count
							figureChild.remove();
						}	
					}
				}
				if (tableCaption==null) {
					//even if no caption is present in html <table> force the generated tableID to be present in map
					//to ensure that by PostprocessTable an automatic caption is added to the table and
					//consequently the table will be included in the "table of tables"
					tableCaption = "";
				}
				tableCaptionsMap.put(tableID, new ImageOrTableCaption(chapterNo, counterWithinChapter, tableCaption, align));
			}
		}
		return doc.html();
	}

	

	@Override
	public void setChapterNo(int chapterNo) {
		this.chapterNo = chapterNo;
	}



	@Override
	public void setCounterWithinChapter(int counterWithinChapter) {
		this.counterWithinChapter = counterWithinChapter;
	}
	
	
}
