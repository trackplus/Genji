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

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.Locale;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.outerj.daisy.diff.HtmlCleaner;
import org.outerj.daisy.diff.XslFilter;
import org.outerj.daisy.diff.html.HTMLDiffer;
import org.outerj.daisy.diff.html.HtmlSaxDiffOutput;
import org.outerj.daisy.diff.html.TextNodeComparator;
import org.outerj.daisy.diff.html.dom.DomTreeBuilder;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 *
 */
public class HTMLDiff {

	private static final Logger LOGGER = LogManager.getLogger(HTMLDiff.class);

	private HTMLDiff() {
		// just to hide it
	}

	public static String makeDiff(String newValue,String oldValue,Locale locale) throws URISyntaxException {
		boolean htmlOut = true;
		if(newValue==null){
			newValue="";
		}else{
			newValue=newValue.replaceAll("&nbsp;"," ");
		}
		if(oldValue==null||(oldValue!=null&&oldValue.length()==0)){
			return newValue;
		}else{
			oldValue=oldValue.replaceAll("&nbsp;"," ");
		}

		try {
			SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory
					.newInstance();

			TransformerHandler result = tf.newTransformerHandler();
			StringWriter stringWriter=new StringWriter();
			result.setResult(new StreamResult(stringWriter));

			XslFilter filter = new XslFilter();

			ContentHandler postProcess = htmlOut? filter.xsl(result,
					"com/aurel/track/util/htmlheader.xsl"):result;

			String prefix = "diff";

			HtmlCleaner cleaner = new HtmlCleaner();

			InputSource oldSource = new InputSource(new StringReader(oldValue));
			InputSource newSource = new InputSource(new StringReader(newValue));

			DomTreeBuilder oldHandler = new DomTreeBuilder();
			cleaner.cleanAndParse(oldSource, oldHandler);
			TextNodeComparator leftComparator = new TextNodeComparator(
					oldHandler, locale);

			DomTreeBuilder newHandler = new DomTreeBuilder();
			cleaner.cleanAndParse(newSource, newHandler);
			TextNodeComparator rightComparator = new TextNodeComparator(
					newHandler, locale);

			postProcess.startDocument();
			postProcess.startElement("", "diffreport", "diffreport",
					new AttributesImpl());
			postProcess.startElement("", "diff", "diff",
					new AttributesImpl());
			HtmlSaxDiffOutput output = new HtmlSaxDiffOutput(postProcess,
					prefix);

			HTMLDiffer differ = new HTMLDiffer(output);
			differ.diff(leftComparator, rightComparator);
			postProcess.endElement("", "diff", "diff");
			postProcess.endElement("", "diffreport", "diffreport");
			postProcess.endDocument();
			return stringWriter.toString();
		} catch (Exception e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
				if (e.getCause() != null) {
					LOGGER.error(ExceptionUtils.getStackTrace(e.getCause()));
				}
				if (e instanceof SAXException) {
					LOGGER.error(ExceptionUtils.getStackTrace(((SAXException) e).getException()));
				}
		}
		return null;
	}
}
