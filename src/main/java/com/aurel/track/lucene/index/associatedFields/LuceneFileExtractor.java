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

package com.aurel.track.lucene.index.associatedFields;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 6385 $
 *
 */
public class LuceneFileExtractor {

	private static final Log LOGGER = LogFactory.getLog(LuceneFileExtractor.class);

	public static class INDEXABLE_EXTENSIONS {
		//MS Word
		public static String DOC = ".doc";
		public static String DOCX = ".docx";
		public static String HTM = ".htm";
		public static String HTML = ".html";
		public static String PDF = ".pdf";
		//rich text format
		public static String RTF = ".rtf";
		//MS Excel
		public static String XLS = ".xls";
		public static String XLSX = ".xlsx";
		public static String TXT = ".txt";
		public static String PROPERTIES = ".properties";
		//general XML
		public static String XML = ".xml";
		//open office text
		//OpenDocument Text
		public static String ODT = ".odt";
		//StarWriter
		//public static String SDW = ".sdw";

		//open office sheet
		//OpenDocument Table
		public static String ODS = ".ods";
		//StarCalc
		//public static String SDC = ".sdc";
		//open office impress
		//OpenDocument presentation
		public static String ODP = ".odp";
		//StarImpress
		//public static String SDD = ".sdd";
	}

	private static String[] STRINGEXTENSIONS = new String[] {
		INDEXABLE_EXTENSIONS.DOC,
		INDEXABLE_EXTENSIONS.DOCX,
		INDEXABLE_EXTENSIONS.HTM,
		INDEXABLE_EXTENSIONS.HTML,
		INDEXABLE_EXTENSIONS.PDF,
		INDEXABLE_EXTENSIONS.RTF,
		INDEXABLE_EXTENSIONS.XLS,
		INDEXABLE_EXTENSIONS.XLSX,
		INDEXABLE_EXTENSIONS.XML,
		INDEXABLE_EXTENSIONS.ODT,
		//INDEXABLE_EXTENSIONS.SDV,
		INDEXABLE_EXTENSIONS.ODS,
		//INDEXABLE_EXTENSIONS.SDC,
		INDEXABLE_EXTENSIONS.ODP,
		//INDEXABLE_EXTENSIONS.SDD,
	};

	private static String[] READEREXTENSIONS = new String[] {
		INDEXABLE_EXTENSIONS.TXT,
		INDEXABLE_EXTENSIONS.PROPERTIES
	};


	/**
	 * Gets the extension if it is recognized 
	 * @param fileName
	 * @return
	 */
	public static String getExtension(String fileName) {
		if (fileName==null) {
			return "";
		}
		int extensionIndex = fileName.lastIndexOf(".");
		if (extensionIndex<=0) {
			return "";
		}
		return fileName.substring(extensionIndex);
	}

	public static boolean isStringExtension(String extension) {
		for (int i=0; i<STRINGEXTENSIONS.length; i++) {
			if (STRINGEXTENSIONS[i].equalsIgnoreCase(extension)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isReaderExtension(String extension) {
		for (int i=0; i<READEREXTENSIONS.length; i++) {
			if (READEREXTENSIONS[i].equalsIgnoreCase(extension)) {
				return true;
			}
		}
		return false;
	}

	public static Reader getReader(File file) {
		Reader reader = null;
		if (file==null || !file.exists()) {
			return null;
		}
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			LOGGER.info("File " + file.getName() + " not found. " + e.getMessage(), e);
		}
		reader = new BufferedReader(reader);
		return reader;
	}

	/*public static String getStringContent(File file, String fileExt) {
		fileExt = fileExt.toLowerCase();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			LOGGER.info("File " + file.getName() + " not found. " + e.getMessage(), e);
			return null;
		}
		Reader reader = new BufferedReader(new InputStreamReader(fis));
		String text = null;
		try {
			if (fileExt.equalsIgnoreCase(INDEXABLE_EXTENSIONS.DOC)) {
				try {
//					WordTextExtractorFactory wordTextExtractorFactory = new WordTextExtractorFactory();
//					text = wordTextExtractorFactory.textExtractor(fis).getText();
//					WordExtractor wordExtractor = new WordExtractor();
//					text = wordExtractor.extractText(fis);
//					text = GetterUtil.getString(text);
//					WordDocument wordDocument = new WordDocument(fis);
//					StringWriter stringWriter = new StringWriter();
//					wordDocument.writeAllText(stringWriter);
//					text = stringWriter.toString();
//					stringWriter.close();
				}
				catch (Exception e) {
					LOGGER.debug("Extracting text from the .doc file " + file.getName() + " failed with " + e.getMessage());
					LOGGER.error(ExceptionUtils.getStackTrace(e));
				}
			}
			else if (fileExt.equalsIgnoreCase(INDEXABLE_EXTENSIONS.DOCX) ) {
				XWPFDocument doc = null;
				XWPFWordExtractor ex = null;
				try {
					doc = new XWPFDocument(fis);
					if (doc!=null) {
						ex = new XWPFWordExtractor(doc);
						text = ex.getText();
					}
				} catch (IOException e) {
					LOGGER.debug("Extracting text from the .docx file " + file.getName() + " failed with " + e.getMessage());
					LOGGER.error(ExceptionUtils.getStackTrace(e));
				} finally {
					if (ex!=null) {
						try {
							ex.close();
						} catch (IOException e) {
							LOGGER.debug("Closing the text exctracor from the .docx file " + file.getName() + " failed with " + e.getMessage());
							LOGGER.error(ExceptionUtils.getStackTrace(e));
						}
					}
				}
			}
			else if (fileExt.equalsIgnoreCase(INDEXABLE_EXTENSIONS.HTM) || fileExt.equalsIgnoreCase(INDEXABLE_EXTENSIONS.HTML)) {
				try {
					DefaultStyledDocument dsd = new DefaultStyledDocument();
					HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
					htmlEditorKit.read(reader, dsd, 0);
					text = dsd.getText(0, dsd.getLength());
				}
				catch (Exception e) {
					LOGGER.debug("Extracting text from the .htm or .html  file " + file.getName() + " failed with " + e.getMessage());
					LOGGER.error(ExceptionUtils.getStackTrace(e));
				}
			}
			else if (fileExt.equalsIgnoreCase(INDEXABLE_EXTENSIONS.PDF)) {
				PDDocument pdDoc= null;
				StringWriter stringWriter = new StringWriter();
				try {
					PDFParser parser = new PDFParser(fis);
					parser.parse();
					pdDoc = parser.getPDDocument();
					PDFTextStripper stripper = new PDFTextStripper();
					stripper.setLineSeparator("\n");
					stripper.writeText(pdDoc, stringWriter);
					text = stringWriter.toString();
				} catch (Exception e) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Extracting text from the .pdf  file " + file.getName() + " failed with " + e.getMessage());
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
				} finally {
					try {
						stringWriter.close();
					} catch (Exception e) {
					}
					try {
						if (pdDoc!=null) {
							pdDoc.close();
						}
					} catch (Exception e) {
					}
				}
			}
			else if (fileExt.equalsIgnoreCase(INDEXABLE_EXTENSIONS.RTF)) {
				try {
					DefaultStyledDocument dsd = new DefaultStyledDocument();
					RTFEditorKit rtfEditorKit = new RTFEditorKit();
					rtfEditorKit.read(reader, dsd, 0);
					text = dsd.getText(0, dsd.getLength());
				}
				catch (Exception e) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Extracting text from the .rtf  file " + file.getName() + " failed with " + e.getMessage());
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
				}
			}
			else if (fileExt.equalsIgnoreCase(INDEXABLE_EXTENSIONS.XLS) ||
					fileExt.equalsIgnoreCase(INDEXABLE_EXTENSIONS.XLSX)) {
				try {
					XLSTextStripper stripper = new XLSTextStripper(fis, fileExt);
					text = stripper.getText();
				} catch (Exception e) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Extracting text from the .xls  file " + file.getName() + " failed with " + e.getMessage());
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
				}
			}
			else if (fileExt.equalsIgnoreCase(".txt")) {
				try {
					//text = reader.read();
				}
				catch (Exception e) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Extracting text from the .txt  file " + file.getName() + " failed with " + e.getMessage());
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
				}
			} else {
				if (fileExt.equalsIgnoreCase(INDEXABLE_EXTENSIONS.XML)) {

					try {
						OOIndexer ooIndexer = new OOIndexer();
						text = ooIndexer.parseXML(file);
					}
					catch (Exception e) {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Extracting text from the .xml  file " + file.getName() + " failed with " + e.getMessage());
							LOGGER.debug(ExceptionUtils.getStackTrace(e));
						}
					}
				} else if (//writer
						fileExt.equalsIgnoreCase(".sxw")
						|| fileExt.equalsIgnoreCase(".stv")
						|| fileExt.equalsIgnoreCase(INDEXABLE_EXTENSIONS.ODT)
						|| fileExt.equalsIgnoreCase(".ott")
						//calc
						|| fileExt.equalsIgnoreCase(INDEXABLE_EXTENSIONS.ODS)
						|| fileExt.equalsIgnoreCase(".ots")
						|| fileExt.equalsIgnoreCase(".sxc")
						|| fileExt.equalsIgnoreCase(".stc")
						//impress
						|| fileExt.equalsIgnoreCase(INDEXABLE_EXTENSIONS.ODP)
						|| fileExt.equalsIgnoreCase(".otp")
						|| fileExt.equalsIgnoreCase(".sxi")
						|| fileExt.equalsIgnoreCase(".sti")
						) {
					try {
						OOIndexer ooIndexer = new OOIndexer();
						text = ooIndexer.parseOpenOffice(file);
					} catch (Exception e) {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Extracting text from the open office file " + file.getName() + " failed with " + e.getMessage());
							LOGGER.debug(ExceptionUtils.getStackTrace(e));
						}
					}
				}
			}
		} finally {
			try {
				reader.close();
			}
			catch (Exception e) {
				LOGGER.info("Can't close reader");
			}
			try {
				fis.close();
			}
			catch (Exception e) {
				LOGGER.info("Can't close fis");
			}
		}
		return text;
	}*/

}
