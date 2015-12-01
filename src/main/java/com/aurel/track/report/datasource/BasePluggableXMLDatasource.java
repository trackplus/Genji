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

package com.aurel.track.report.datasource;

import java.io.OutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;
/**
 * Base class for IPluggableDatasouce which have an XML as raw data source
 * @author Tamas
 *
 */
public abstract class BasePluggableXMLDatasource implements IPluggableDatasource {

	private static final Logger LOGGER = LogManager.getLogger(BasePluggableXMLDatasource.class);
	/**
	 * Serializes the raw data source object into an OutputStream (typically an XML file) 
	 * -	For jar templates (freemarker) it might be not needed to serialize the
	 * datasource object because it is a proprietary object built specifically for the template 
	 * -	For zip templates (jasper, fop, etc.) it is typically needed to take the serialized raw 
	 * datasource as a file and use it as data source by editing the template file using a template editing tool
	 * If the serialized raw data is not needed then just return null, but then implementSerialization() should also return false
	 * @param outputStream typically the OutputStream of the HttpResponse object 
	 * @param datasource typically DOM Document object or any other proprietary object
	 * @return typically an XML file
	 */
	@Override
	public void serializeDatasource(OutputStream outputStream, Object datasource) {		
		Transformer transformer = null;
		try{
			TransformerFactory factory = TransformerFactory.newInstance();
			transformer = factory.newTransformer ();
			transformer.setOutputProperty (OutputKeys.INDENT, "yes");
			transformer.setOutputProperty (OutputKeys.METHOD, "xml");
			transformer.setOutputProperty (OutputKeys.ENCODING,"UTF-8");
			transformer.setOutputProperty ("{http://xml.apache.org/xslt}indent-amount", "4");
		}
		catch (TransformerConfigurationException e){
			LOGGER.error ("Creating the transformer failed with TransformerConfigurationException: " + e.getMessage());
			return;
		}
		try{
			transformer.transform (new DOMSource ((Document)datasource), new StreamResult (outputStream));
		}
		catch (TransformerException e){
			LOGGER.error ("Transform failed with TransformerException: " + e.getMessage());
		}
	}

}
