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


package com.aurel.track.exchange.docx.importer;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.docx4j.Docx4J;
import org.docx4j.Docx4jProperties;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import com.aurel.track.admin.server.logging.LoggingConfigBL;

/**
 * This sample uses XSLT (and Xalan) to
 * produce HTML output.  (There is also
 * HtmlExporterNonXSLT for environments where
 * that is not desirable eg Android).
 *
 * If the source docx contained a WMF, that
 * will get converted to inline SVG.  In order
 * to see the SVG in your browser, you'll need
 * to rename the file to .xml or serve
 * it with MIME type application/xhtml+xml
 *
 */
public class HtmlConverter {
		private static final Logger LOGGER = LogManager.getLogger(HtmlConverter.class);
		
		public static ByteArrayOutputStream convertToHTML(String docxFileName) {
		
		LoggingConfigBL.setLevel(LOGGER, Level.DEBUG);
		LOGGER.debug("Creating html from " + docxFileName + "...");

		// HTML exporter setup (required)
		// .. the HTMLSettings object
    	HTMLSettings htmlSettings = Docx4J.createHTMLSettings();
    	htmlSettings.setImageDirPath(docxFileName + "_files");
    	htmlSettings.setImageTargetUri(docxFileName.substring(docxFileName.lastIndexOf("/")+1)+ "_files");
    	WordprocessingMLPackage wordMLPackage = null;
		try {
			wordMLPackage = Docx4J.load(new java.io.File(docxFileName));
		} catch (Docx4JException e) {
			LOGGER.error("Loading the wordMLPackage failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return null;
		}
		LOGGER.debug(docxFileName + "loaded");
    	htmlSettings.setWmlPackage(wordMLPackage);
    	//Other settings (optional)
		
		// Sample sdt tag handler (tag handlers insert specific
		// html depending on the contents of an sdt's tag).
		// This will only have an effect if the sdt tag contains
		// the string @class=XXX
		
		
		// output to an OutputStream.
    	
    	
		OutputStream outputStream = null; 
		

		// If you want XHTML output
    	Docx4jProperties.setProperty("docx4j.Convert.Out.HTML.OutputMethodXML", true);

    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.debug("Saving the html file to disk...");
    		try {
				outputStream = new FileOutputStream(docxFileName + ".html");
			} catch (FileNotFoundException e) {
				LOGGER.error("Creating the outpot stream failed with " + e.getMessage());
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
    		if (outputStream!=null) {
    			try {
    				Docx4J.toHTML(htmlSettings, outputStream, Docx4J.FLAG_EXPORT_PREFER_XSL);
    			} catch (Docx4JException e) {
    				LOGGER.error("Creating the html failed with " + e.getMessage());
    				LOGGER.debug(ExceptionUtils.getStackTrace(e));
    				return null;
    			}
    			try {
					outputStream.close();
				} catch (IOException e) {
					LOGGER.error("Closing the FileOutputStream failed with " + e.getMessage());
				}
    		}
    	}
    	outputStream = new ByteArrayOutputStream();
		//Don't care what type of exporter you use
		//Prefer the exporter, that uses a xsl transformation
		try {
			Docx4J.toHTML(htmlSettings, outputStream, Docx4J.FLAG_EXPORT_PREFER_XSL);
		} catch (Docx4JException e) {
			LOGGER.error("Creating the html failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return null;
		}
		//Prefer the exporter, that doesn't use a xsl transformation (= uses a visitor)
		return (ByteArrayOutputStream)outputStream;

		}
}
