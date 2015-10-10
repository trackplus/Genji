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


package com.aurel.track.lucene.util.openOffice;

/*
 * LIUS - Lucene Index Update and Search
 * http://sourceforge.net/projects/lius/
 *
 * Copyright (c) 2005, Laval University Library.  All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
import org.xml.sax.SAXException;

/**
 * 
 * @author Rida Benjelloun (rida.benjelloun@bibl.ulaval.ca)
 *  
 */

public class OOIndexer {
	

	private static final Logger LOGGER = LogManager.getLogger(OOIndexer.class);

	private final String TMP_UNZIP_DIR = "tmpUnzip";

	/**
	 * Get the string content of an xml file
	 */
	public String parseXML(File file)
	{
		Document document = null;		
		DocumentBuilderFactory documentBuilderfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = documentBuilderfactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LOGGER.info("Building the document builder from factory failed with " + e.getMessage(), e);
			return null;
		}
		try
		{
			document = builder.parse(file);				
		} catch (SAXException e) {
			LOGGER.info("Parsing the XML document " + file.getPath() + " failed with " + e.getMessage(), e);				
		} catch (IOException e) {
			LOGGER.info("Reading the XML document " + file.getPath() + " failed with " + e.getMessage(), e);
		}		
		if (document==null)
		{
			return null;
		}
		StringWriter result = new StringWriter ();
		Transformer transformer = null; 
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty (OutputKeys.INDENT, "yes");
			transformer.setOutputProperty (OutputKeys.METHOD, "xml");
			transformer.setOutputProperty (OutputKeys.ENCODING,"UTF-8");
			transformer.setOutputProperty ("{http://xml.apache.org/xslt}indent-amount", "2");
		}
		catch (TransformerConfigurationException e) {
			LOGGER.warn("Creating the transformer failed with TransformerConfigurationException: " + e.getMessage(), e);
			return null;
		}						
		try {
			transformer.transform (new DOMSource (document), new StreamResult (result));
		}  catch (TransformerException e) {
			LOGGER.warn("Transform failed with TransformerException: " + e.getMessage(), e);
		}
		return result.getBuffer().toString();								
	}
	
	/**
	 * Get the string content of an openOffice file by concatenating the 
	 * contents of the content.xml and meta.xml files
	 */
	public String parseOpenOffice(File file)
	{
		String openOfficeText = null;
		try
		{
			List filesToIndex = getFilesToIndex(file.getPath());
			openOfficeText = getOpenOfficeText(filesToIndex);
		}
		catch(Exception ex)
		{
			LOGGER.warn("Getting the open office content failed with " + ex.getMessage());
		}
		finally
		{
			try
			{
				deleteTmp(file.getPath());
			}
			catch(Exception ex)
			{
				LOGGER.warn("Deleting the unzipped open office content failed with " + ex.getMessage());
			}
		}
		return openOfficeText;
	}

	private String getOpenOfficeText(List fileNames)
	{		
		String fileName;
		String fileContent;
		if (fileNames==null || fileNames.isEmpty())
		{
			return null;
		}
		StringBuffer fileContentBuffer = new StringBuffer();

		Iterator itrFileNames = fileNames.iterator();
		while (itrFileNames.hasNext())
		{
			fileName = (String)itrFileNames.next();			
			fileContent = parseXML(new File(fileName));				
			fileContentBuffer.append(fileContent);
		}
		return fileContentBuffer.toString();
	}
	
	private List getFilesToIndex(String file) {
		String dir = (file.substring(0, (file)
				.lastIndexOf(File.separator)));
		unzip(file, dir + File.separator + TMP_UNZIP_DIR
				+ File.separator);
		List ls = new ArrayList();
		ls.add(0, dir + File.separator + TMP_UNZIP_DIR + File.separator
				+ "content.xml");
		ls.add(1, dir + File.separator + TMP_UNZIP_DIR + File.separator
				+ "meta.xml");

		return ls;
	}

	private List unzip(String zip, String destination) {
		List destLs = new ArrayList();
		Enumeration entries;
		ZipFile zipFile;
		File dest = new File(destination);
		dest.mkdir();
		if (dest.isDirectory()) {

			try {
				zipFile = new ZipFile(zip);

				entries = zipFile.entries();

				while (entries.hasMoreElements()) {
					ZipEntry entry = (ZipEntry) entries.nextElement();

					if (entry.isDirectory()) {

						(new File(dest.getAbsolutePath() + File.separator
								+ entry.getName())).mkdirs();
						continue;
					}

					if (entry.getName().lastIndexOf("/") > 0) {
						File f = new File(dest.getAbsolutePath()
								+ File.separator
								+ entry.getName().substring(0,
										entry.getName().lastIndexOf("/")));
						f.mkdirs();
					}
					copyInputStream(zipFile.getInputStream(entry),
							new BufferedOutputStream(new FileOutputStream(dest
									.getAbsolutePath()
									+ File.separator + entry.getName())));
					destLs.add(dest.getAbsolutePath() + File.separator
							+ TMP_UNZIP_DIR + File.separator + entry.getName());
				}

				zipFile.close();
			} catch (IOException e) {
				deleteDir(new File(destination));
				LOGGER.error(e.getMessage(), e);
			}
		} else {
			LOGGER.info("There is already a file by that name");
		}
		return destLs;
	}

	private void copyInputStream(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while ((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);

		in.close();
		out.close();
	}

	public boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	protected void deleteTmp(String file) {
		String dir = ((file).substring(0, (file)
				.lastIndexOf(File.separator)))
				+ File.separator + TMP_UNZIP_DIR;
		deleteDir(new File(dir));
	}

}
