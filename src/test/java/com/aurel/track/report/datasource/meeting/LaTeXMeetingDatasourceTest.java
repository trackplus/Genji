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

package com.aurel.track.report.datasource.meeting;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.aurel.track.admin.customize.category.report.execute.ReportBeansToLaTeXConverter;


/**
 * The class <code>LaTeXMeetingDatasourceTest</code> contains tests for the class <code>{@link LaTeXMeetingDatasource}</code>.
 *
 * @generatedBy CodePro at 03.05.15 12:19
 * @author friedj
 * @version $Revision: 1.0 $
 */
public class LaTeXMeetingDatasourceTest {
	/**
	 * Run the LaTeXMeetingDatasource() constructor test.
	 *
	 * @generatedBy CodePro at 03.05.15 12:19
	 */
	@Test
	public void testLaTeXMeetingDatasourceTestPrepareDirs()
		throws Exception {
		LaTeXMeetingDatasource ds = new LaTeXMeetingDatasource();
		String LATEXDIR = "homet" + File.separator + "Reports" + File.separator + "LaTeX";
		String LATEX_TMP_DIR = LATEXDIR + File.separator + "tmp" + String.valueOf(new Date().getTime());
		String templateDir = "homet" + File.separator + "ExportTemplates" + File.separator + "latexTemplates"+File.separator + "Requirements";
		ReportBeansToLaTeXConverter rc = new ReportBeansToLaTeXConverter();

		rc.prepareDirectories(new File(templateDir));

		Collection<File> files = FileUtils.listFilesAndDirs(new File(LATEXDIR), FileFilterUtils.trueFileFilter(),
				                                                                FileFilterUtils.trueFileFilter());
//		for (File file: files) {
//			System.out.println(file.getAbsoluteFile());
//		}

		assertEquals(2, files.size());
	}
//
//	/**
//	 * Run the Object getDatasource(Map<String,String[]>,DatasourceDescriptor,Map<String,Object>,Map<String,Object>,Integer,TPersonBean,Locale) method test.
//	 *
//	 * @throws Exception
//	 *
//	 * @generatedBy CodePro at 03.05.15 12:19
//	 */
//	@Test
//	public void testGetDatasource_1()
//		throws Exception {
//		LaTeXMeetingDatasource fixture = new LaTeXMeetingDatasource();
//		Map<String, String[]> parameters = new HashMap();
//		DatasourceDescriptor datasourceDescriptor = new DatasourceDescriptor();
//		Map<String, Object> contextMap = new HashMap();
//		Map<String, Object> templateDescriptionMap = new HashMap();
//		Integer templateID = new Integer(1);
//		TPersonBean personBean = new TPersonBean();
//		Locale locale = Locale.getDefault();
//
//		Object result = fixture.getDatasource(parameters, datasourceDescriptor, contextMap, templateDescriptionMap, templateID, personBean, locale);
//
//		// add additional test code here
//		assertEquals(null, result);
//	}
//
//	/**
//	 * Run the Object getDatasource(Map<String,String[]>,DatasourceDescriptor,Map<String,Object>,Map<String,Object>,Integer,TPersonBean,Locale) method test.
//	 *
//	 * @throws Exception
//	 *
//	 * @generatedBy CodePro at 03.05.15 12:19
//	 */
//	@Test
//	public void testGetDatasource_2()
//		throws Exception {
//		LaTeXMeetingDatasource fixture = new LaTeXMeetingDatasource();
//		Map<String, String[]> parameters = new HashMap();
//		DatasourceDescriptor datasourceDescriptor = new DatasourceDescriptor();
//		Map<String, Object> contextMap = new HashMap();
//		Map<String, Object> templateDescriptionMap = null;
//		Integer templateID = new Integer(1);
//		TPersonBean personBean = new TPersonBean();
//		Locale locale = Locale.getDefault();
//
//		Object result = fixture.getDatasource(parameters, datasourceDescriptor, contextMap, templateDescriptionMap, templateID, personBean, locale);
//
//		// add additional test code here
//		// An unexpected exception was thrown in user code while executing this test:
//		//    java.lang.NullPointerException
//		//       at com.aurel.track.report.datasource.meeting.LaTeXMeetingDatasource$ReportBeansToLaTeXConverter.generatePdf(LaTeXMeetingDatasource.java:328)
//		//       at com.aurel.track.report.datasource.meeting.LaTeXMeetingDatasource.getDocumentFromReportBeans(LaTeXMeetingDatasource.java:250)
//		//       at com.aurel.track.report.datasource.meeting.LaTeXMeetingDatasource.getDatasource(LaTeXMeetingDatasource.java:208)
//		assertNotNull(result);
//	}
//
//	/**
//	 * Run the Document getDocumentFromReportBeans(ReportBeans,boolean,boolean,TPersonBean,Locale,String,String,boolean,boolean,File,File) method test.
//	 *
//	 * @throws Exception
//	 *
//	 * @generatedBy CodePro at 03.05.15 12:19
//	 */
//	@Test
//	public void testGetDocumentFromReportBeans_1()
//		throws Exception {
//		ReportBeans reportBeans = null;
//		boolean withHistory = true;
//		boolean withInlineComment = true;
//		TPersonBean personBean = new TPersonBean();
//		Locale locale = Locale.getDefault();
//		String queryName = "";
//		String queryExpression = "";
//		boolean longTextIsPlain = true;
//		boolean useProjectSpecificID = true;
//		File template = new File("");
//		File templateDir = new File("");
//
//		Document result = LaTeXMeetingDatasource.getDocumentFromReportBeans(reportBeans, withHistory, withInlineComment, personBean, locale, queryName, queryExpression, longTextIsPlain, useProjectSpecificID, template, templateDir);
//
//		// add additional test code here
//		assertEquals(null, result);
//	}
//
//	/**
//	 * Run the Document getDocumentFromReportBeans(ReportBeans,boolean,boolean,TPersonBean,Locale,String,String,boolean,boolean,File,File) method test.
//	 *
//	 * @throws Exception
//	 *
//	 * @generatedBy CodePro at 03.05.15 12:19
//	 */
//	@Test
//	public void testGetDocumentFromReportBeans_2()
//		throws Exception {
//		ReportBeans reportBeans = new ReportBeans(new LinkedList(), Locale.getDefault());
//		boolean withHistory = true;
//		boolean withInlineComment = true;
//		TPersonBean personBean = new TPersonBean();
//		Locale locale = Locale.getDefault();
//		String queryName = "";
//		String queryExpression = "";
//		boolean longTextIsPlain = true;
//		boolean useProjectSpecificID = true;
//		File template = new File("");
//		File templateDir = new File("");
//
//		Document result = LaTeXMeetingDatasource.getDocumentFromReportBeans(reportBeans, withHistory, withInlineComment, personBean, locale, queryName, queryExpression, longTextIsPlain, useProjectSpecificID, template, templateDir);
//
//		// add additional test code here
//		assertNotNull(result);
//		assertEquals(null, result.getDocumentURI());
//		assertEquals(true, result.getStrictErrorChecking());
//		assertEquals(null, result.getInputEncoding());
//		assertEquals(null, result.getXmlEncoding());
//		assertEquals("1.0", result.getXmlVersion());
//		assertEquals(false, result.getXmlStandalone());
//		assertEquals(null, result.getDoctype());
//		assertEquals("#document", result.getNodeName());
//		assertEquals(null, result.getNodeValue());
//		assertEquals((short) 9, result.getNodeType());
//		assertEquals(null, result.getParentNode());
//		assertEquals(null, result.getPreviousSibling());
//		assertEquals(null, result.getNextSibling());
//		assertEquals(null, result.getOwnerDocument());
//		assertEquals(true, result.hasChildNodes());
//		assertEquals(null, result.getNamespaceURI());
//		assertEquals(null, result.getLocalName());
//		assertEquals(false, result.hasAttributes());
//		assertEquals(null, result.getBaseURI());
//		assertEquals(null, result.getTextContent());
//		assertEquals(null, result.getPrefix());
//		assertEquals(null, result.getAttributes());
//	}
//
//	/**
//	 * Run the void streamPdf(OutputStream,File) method test.
//	 *
//	 * @throws Exception
//	 *
//	 * @generatedBy CodePro at 03.05.15 12:19
//	 */
//	@Test
//	public void testStreamPdf_1()
//		throws Exception {
//		OutputStream os = new ByteArrayOutputStream();
//		File pdfFile = new File("");
//
//		LaTeXMeetingDatasource.streamPdf(os, pdfFile);
//
//		// add additional test code here
//	}
//
//	/**
//	 * Run the void streamPdf(OutputStream,File) method test.
//	 *
//	 * @throws Exception
//	 *
//	 * @generatedBy CodePro at 03.05.15 12:19
//	 */
//	@Test
//	public void testStreamPdf_2()
//		throws Exception {
//		OutputStream os = new ByteArrayOutputStream();
//		File pdfFile = new File("");
//
//		LaTeXMeetingDatasource.streamPdf(os, pdfFile);
//
//		// add additional test code here
//	}
//
//	/**
//	 * Run the void streamPdf(OutputStream,File) method test.
//	 *
//	 * @throws Exception
//	 *
//	 * @generatedBy CodePro at 03.05.15 12:19
//	 */
//	@Test
//	public void testStreamPdf_3()
//		throws Exception {
//		OutputStream os = new ByteArrayOutputStream();
//		File pdfFile = new File("");
//
//		LaTeXMeetingDatasource.streamPdf(os, pdfFile);
//
//		// add additional test code here
//	}
//
//	/**
//	 * Run the void streamPdf(OutputStream,File) method test.
//	 *
//	 * @throws Exception
//	 *
//	 * @generatedBy CodePro at 03.05.15 12:19
//	 */
//	@Test
//	public void testStreamPdf_4()
//		throws Exception {
//		OutputStream os = new ByteArrayOutputStream();
//		File pdfFile = new File("");
//
//		LaTeXMeetingDatasource.streamPdf(os, pdfFile);
//
//		// add additional test code here
//	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 03.05.15 12:19
	 */
	@Before
	public void setUp()
		throws Exception {
		// add additional set up code here
	}

	/**
	 * Perform post-test clean-up.
	 *
	 * @throws Exception
	 *         if the clean-up fails for some reason
	 *
	 * @generatedBy CodePro at 03.05.15 12:19
	 */
	@After
	public void tearDown()
		throws Exception {
		// Add additional tear down code here
	}

	/**
	 * Launch the test.
	 *
	 * @param args the command line arguments
	 *
	 * @generatedBy CodePro at 03.05.15 12:19
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(LaTeXMeetingDatasourceTest.class);
	}
}
