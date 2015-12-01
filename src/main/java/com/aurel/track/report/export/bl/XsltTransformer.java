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

package com.aurel.track.report.export.bl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * An util class that transform an xml to another xml using xslt
 */
public class XsltTransformer {
	static Logger LOGGER = LogManager.getLogger(XsltTransformer.class);
	
	public static XsltTransformer instance;

	public static XsltTransformer getInstance() {
		if(instance==null){
			instance=new XsltTransformer();
		}
		return instance;
	}
	public XsltTransformer() {
	}
	public Document transform(Document dom,String xsltPath){
		Document domResult=null;
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			File stylesheet=new File(xsltPath);
			StreamSource stylesource = new StreamSource(stylesheet);
			Transformer transformer = tFactory.newTransformer(stylesource);
			DOMSource source = new DOMSource(dom);
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			StreamResult sr=new StreamResult(result);
			transformer.transform(source, sr);
			LOGGER.debug("--------------------");
			LOGGER.debug(result.toString());
			LOGGER.debug("--------------------");
			DocumentBuilderFactory factory =DocumentBuilderFactory.newInstance();
			try {
			     DocumentBuilder builder = factory.newDocumentBuilder();
			     domResult = builder.parse(new InputSource(new ByteArrayInputStream(result.toByteArray())));
			} catch (Exception e) {
			     LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
		} catch (TransformerConfigurationException tce) {
			LOGGER.error("Can't transform dom using xslt path :"+xsltPath,tce);
		}catch (TransformerException te) {
			LOGGER.error("Can't transform dom using xslt path :"+xsltPath,te);
		}
		return domResult;
	}
	
	public void transform(File source,File target,File stylesheet){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document dom=null;
		try {
			 DocumentBuilder builder = factory.newDocumentBuilder();
			 dom = builder.parse(source);
		} catch (Exception e) {
			 LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			StreamSource stylesource = new StreamSource(stylesheet);
			Transformer transformer = tFactory.newTransformer(stylesource);
			StreamResult result = new StreamResult(new FileOutputStream(target));
			DOMSource domsource = new DOMSource(dom);
			transformer.transform(domsource, result);
		} catch (TransformerConfigurationException tce) {
			LOGGER.error(ExceptionUtils.getStackTrace(tce));
		}catch (TransformerException te) {
			LOGGER.error(ExceptionUtils.getStackTrace(te));
		} catch (FileNotFoundException e) {
			 LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}
}
