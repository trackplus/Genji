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

package com.aurel.track.lucene.index.associatedFields.textExctractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextExtractorFactory {

	private Map<String, ITextExtractor> textExtractorsMap;
	private static TextExtractorFactory instance;
	
	/**
	 * Return a FieldConfigItemFacade instance which implements the ConfigItemFacade 
	 * @return
	 */
	public static TextExtractorFactory getInstance() {
		if(instance==null){
			instance=new TextExtractorFactory();
		}
		return instance;
	}
	
	/**
	 * Get the proper configItem for configType
	 * @param categoryType
	 * @return
	 */
	public TextExtractorFactory() {
		textExtractorsMap = new HashMap<String, ITextExtractor>();
		List<ITextExtractor> allExtractors = new ArrayList<ITextExtractor>();
		allExtractors.add(new DocExtractor());
		allExtractors.add(new DocxExtractor());
		allExtractors.add(new HTMLExtractor());
		allExtractors.add(new OpenOfficeExtractor());
		allExtractors.add(new PdfExtractor());
		allExtractors.add(new RTFExtractor());
		allExtractors.add(new XLSExtractor());
		allExtractors.add(new XMLExtractor());
		for (ITextExtractor textExtractor : allExtractors) {
			List<String> extensions = textExtractor.getFileTypes();
			for (String extension : extensions) {
				textExtractorsMap.put(extension, textExtractor);
			}
		}
	}
	
	/**
	 * Gets the text extractor implementation
	 * @param extension
	 * @return
	 */
	public ITextExtractor getTextExtractor(String extension) {
		return textExtractorsMap.get(extension);
	}
}
