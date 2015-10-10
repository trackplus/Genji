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

package com.aurel.track.report.datasource.faqs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.aurel.track.admin.customize.category.filter.TreeFilterFacade;
import com.aurel.track.admin.customize.category.filter.execute.FilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.ReportBeanLoader;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dbase.HandleHome;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.itemNavigator.ItemNavigatorBL;
import com.aurel.track.itemNavigator.ItemNavigatorBL.QUERY_TYPE;
import com.aurel.track.itemNavigator.QueryContext;
import com.aurel.track.lucene.util.FileUtil;
import com.aurel.track.plugin.DatasourceDescriptor;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.datasource.IPluggableDatasource;
import com.aurel.track.report.datasource.ReportBeanDatasource;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.HTMLSanitiser;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.TreeNode;
import com.aurel.track.util.emailHandling.Html2Text;
import com.xmlmind.util.Console;
import com.xmlmind.whc.Compiler;


public class FaqsDatasource extends ReportBeanDatasource {

	private static final Logger LOGGER = LogManager.getLogger(FaqsDatasource.class);
	private static String FAQ_DIR = null;
	private static String CRLF = System.getProperty("line.separator");
	private static String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + CRLF 
	+"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">"+CRLF
	+"<html xmlns=\"http://www.w3.org/1999/xhtml\">"+ CRLF
	+"    <head>";
	//        <title>FAQ: The title here</title>			
	private static String styles = 
	 "        <style type=\"text/css\">body{font-family:Helvetica,Arial,Tahoma,SansSerif}"+CRLF
	+"            h1{font-weight: bold; font-size:12pt}"+CRLF
	+"            .faq{font-size:10pt}"+CRLF
	+"            .bottom{margin-top:2em}</style>"+CRLF
	+"        <link href=\"styles.css\" rel=\"stylesheet\" type=\"text/css\" />"+CRLF
	+"    </head>"+CRLF
	+"    <body>"+CRLF;
	
	private static String footer = 
	 "        <div class=\"bottom\" />"+CRLF
	+"    </body>"+CRLF
	+"</html>";
	

	/**
	 * Gets the data source object (an XHTML Document object in this case) retrieved using the parameters settings
	 * @param parameters
	 * @param datasourceDescriptor
	 * @param contextMap
	 * @param templateDescriptionMap
	 * @param templateID
	 * @param personBean
	 * @param locale
	 * @return
	 * @throws TooManyItemsToLoadException 
	 */
	public Object getDatasource(Map<String, String[]> parameters, DatasourceDescriptor datasourceDescriptor,
			Map<String, Object> contextMap, Map<String, Object> templateDescriptionMap,
			Integer templateID, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException {
		ReportBeans reportBeans = null;
		Boolean fromIssueNavigator = (Boolean)contextMap.get(CONTEXT_ATTRIBUTE.FROM_ISSUE_NAVIGATOR);
		Integer dashboardProjectOrReleaseID = (Integer)contextMap.get(CONTEXT_ATTRIBUTE.DASHBOARD_PROJECT_RELEASE_ID);
		boolean useProjectSpecificID = false;
		Boolean projectSpecificID = (Boolean)contextMap.get(IPluggableDatasource.CONTEXT_ATTRIBUTE.USE_PROJETC_SPECIFIC_ID);
		if (projectSpecificID!=null) {
			useProjectSpecificID = projectSpecificID.booleanValue();
		}
		String queryName = null;
		String queryExpression = null;
		if (fromIssueNavigator!=null && fromIssueNavigator.booleanValue()) {
			//from issue navigator: datasource is the last query
			List<Integer> workItemIDs = (List<Integer>)contextMap.get(CONTEXT_ATTRIBUTE.WORKITEMIDS);
			if (workItemIDs!=null && !workItemIDs.isEmpty()) {
				List<ReportBean> reportBeanList = LoadItemIDListItems.getReportBeansByWorkItemIDs(
						GeneralUtils.createIntArrFromIntegerList(workItemIDs), false, personBean.getObjectID(), locale, false, false, false, false, false, false, false, false, false);
				reportBeans = new ReportBeans(reportBeanList, locale, null, false);
			} else {
				/*Integer workItemID = (Integer)contextMap.get(CONTEXT_ATTRIBUTE.WORKITEMID);
				if (workItemID==null) {*/
					QueryContext queryContext=ItemNavigatorBL.loadLastQuery(personBean.getObjectID(),locale);
					if (queryContext!=null){
						reportBeans = ItemNavigatorBL.executeQuery(personBean,locale,queryContext, null, false);
						queryName = queryContext.getQueryName();
						contextMap.put(CONTEXT_ATTRIBUTE.QUERY_ID, queryContext.getQueryID());
						contextMap.put(CONTEXT_ATTRIBUTE.QUERY_TYPE, queryContext.getQueryType());
					}
				/*} else {
					List<ReportBean> reportBeanList = ReportBeanLoader.getReportBeansByWorkItemIDsNoFilterNoLink(new int[] {workItemID}, personBean.getObjectID(), locale);
					reportBeans= new ReportBeans(reportBeanList, personBean.getObjectID(), locale, null, false);
				}*/
			}
		} else {
			if (dashboardProjectOrReleaseID!=null) {
				//from dashboard: the project/release of the project/release specific dashboard
				//or the project/release set as datasource for "global" dashboard
				Integer dashboardID = (Integer)contextMap.get(CONTEXT_ATTRIBUTE.DASHBOARD_ID);
				queryName = ItemNavigatorBL.getQueryName(QUERY_TYPE.DASHBOARD, dashboardID,-1, locale);
				FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(dashboardProjectOrReleaseID, true, true, false);
				List<ReportBean> reportBeanList = LoadTreeFilterItems.getTreeFilterReportBeans(filterUpperTO, null, null, personBean, locale);
				reportBeans = new ReportBeans(reportBeanList, locale, null, true);
				contextMap.put(CONTEXT_ATTRIBUTE.QUERY_ID, dashboardProjectOrReleaseID);
				contextMap.put(CONTEXT_ATTRIBUTE.QUERY_TYPE, ItemNavigatorBL.QUERY_TYPE.DASHBOARD);
			} else {
				//from report configuration page: first save the parameters into the database, then get the reportBeans
				Map<String, Object> savedParamsMap = new HashMap<String, Object>();
				String paramSettings = loadParamObjectsAndPropertyStringsAndFromStringArrParams(parameters, locale, savedParamsMap);
				saveParameters(paramSettings, personBean.getObjectID(), templateID);
				Integer datasourceType = (Integer)savedParamsMap.get(PARAMETER_NAME.DATASOURCETYPE);
				if (datasourceType==null) {
					LOGGER.warn("No datasourceType was selected");
					return null;
				}
				if (datasourceType.intValue()==DATASOURCE_TYPE.PROJECT_RELEASE) {
					Integer projectOrReleaseID = (Integer)savedParamsMap.get(PARAMETER_NAME.PROJECT_OR_RELEASE_ID);
					if (projectOrReleaseID==null) {
						LOGGER.warn("No project/release was selected");
						return null;
					} else {
						FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(projectOrReleaseID, true, true, false);
						List<ReportBean> reportBeanList = LoadTreeFilterItems.getTreeFilterReportBeans(filterUpperTO, null, null, personBean, locale);
						reportBeans = new ReportBeans(reportBeanList, locale, null, true);
						queryName = ItemNavigatorBL.getQueryName(QUERY_TYPE.PROJECT_RELEASE, projectOrReleaseID,-1, locale);
						contextMap.put(CONTEXT_ATTRIBUTE.QUERY_ID, projectOrReleaseID);
						contextMap.put(CONTEXT_ATTRIBUTE.QUERY_TYPE, ItemNavigatorBL.QUERY_TYPE.PROJECT_RELEASE);
					}
				} else {
					Integer filterID = (Integer)savedParamsMap.get(PARAMETER_NAME.FILTER_ID);
					if (filterID==null) {
						LOGGER.warn("No filter was selected");
						return null;
					} else {
						reportBeans = FilterExecuterFacade.getSavedFilterReportBeans(filterID, locale,
									personBean, new LinkedList<ErrorData>(), null, null, null);
						contextMap.put(CONTEXT_ATTRIBUTE.QUERY_ID, filterID);
						contextMap.put(CONTEXT_ATTRIBUTE.QUERY_TYPE, ItemNavigatorBL.QUERY_TYPE.SAVED);
						ILabelBean labelBean = TreeFilterFacade.getInstance().getByKey(filterID);
						if (labelBean!=null) {
							queryName = labelBean.getLabel();
						}
					}
				}
			}
		}

		boolean withHistory = false;
		boolean longTextIsPlain = false;

		return getDocumentFromReportBeans(reportBeans, withHistory,
				personBean, locale, queryName, queryExpression, longTextIsPlain, useProjectSpecificID);
	}

	/**
	 * Transform the reportBeans into DOM Document
	 * @return
	 */
	protected static Document getDocumentFromReportBeans(ReportBeans reportBeans, boolean withHistory, TPersonBean personBean, 
			Locale locale, String queryName, String queryExpression, boolean longTextIsPlain, boolean useProjectSpecificID) {
		if (reportBeans == null) {
			return null;
		}
		personBean.getObjectID();
		List<ReportBean> items = reportBeans.getItems();
		if (items == null) {
			return null;
		}
		ReportBeanLoader.addISOValuesToReportBeans(items, personBean.getObjectID(), locale);
		if (longTextIsPlain) {
		}
		//add the history to each ReportBean: the long fields history elements can be transformed to the longTextType
		//needed and consequently we can spare storing also the real attribute values in the HistoryValues objects
		//(near the showValues) but the actual long fields cannot be transformed at this moment because 
		//they are stored in the session and it would affect the future renderings of the ReportBeans

		ReportBeansToXHTMLConverter rc = new FaqsDatasource().new ReportBeansToXHTMLConverter();
		
		//if (personBean.isSysAdmin() || personBean.isSysManager()) { // write to disk
			rc.writeFiles(items, withHistory, locale, personBean.getFullName(), 
					queryName, queryExpression, useProjectSpecificID);
		//}
		
		return rc.convertToDOM(items, withHistory, locale, personBean.getFullName(), 
		           queryName, queryExpression, useProjectSpecificID);

	}

	/**
	 * @param items
	 * @return
	 */
	public static void convertToXml (OutputStream outputStream, Document dom) {
		Transformer transformer = null;
		try{
			TransformerFactory factory = TransformerFactory.newInstance();
			transformer = factory.newTransformer ();
			transformer.setOutputProperty (OutputKeys.INDENT, "yes");
			transformer.setOutputProperty (OutputKeys.METHOD, "xml");
			transformer.setOutputProperty (OutputKeys.ENCODING,"UTF-8");
			transformer.setOutputProperty ("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//W3C//DTD XHTML 1.0 Transitional//EN");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
		} catch (TransformerConfigurationException e){
			LOGGER.error ("Creating the transformer failed with TransformerConfigurationException: " + e.getMessage(), e);
			return;
		}
		try{
			transformer.transform (new DOMSource(dom), new StreamResult(outputStream));
		} catch (TransformerException e){
			LOGGER.error ("Transform failed with TransformerException: " + e.getMessage(), e);
		}
	}

	/**
	 * @param os
	 * write the zipped FAQ directory into the download output stream
	 */
	public static void streamZippedFAQs(OutputStream os) {
		try {
			File tmpDir = FileUtils.getTempDirectory();
			File zipFile = new File(tmpDir.getAbsolutePath()+File.separator+"FAQs.zip");
			
			try {
				//FileUtils.forceDelete(zipFile);
				zipFile.delete();
			} catch (/*FileNotFound*/Exception fne) {
				// ignore, that is okay
			}
			
			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);
			FileUtil.zipFiles(new File(FAQ_DIR), zos);
			zos.close();
			fos.close();
			
			InputStream is = new FileInputStream(zipFile);
			IOUtils.copy(is, os);
			//FileUtils.forceDelete(zipFile);
			zipFile.delete();
			is.close();
		} catch (FileNotFoundException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			
		}	
	}
	

	/**
	 * Inner class to create the FAQ help file structure.
	 *
	 */
	class ReportBeansToXHTMLConverter implements Console{
		
		private String FAQ_TMP_DIR = null;
		
		public Document convertToDOM(List<ReportBean> items, boolean withHistory, Locale locale, 
				String personName, String filterName, String filterExpression, boolean useProjectSpecificID) {
			Document dom = null;
			try{
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
				DocumentBuilder builder = factory.newDocumentBuilder ();
				dom = builder.newDocument ();	
			}catch (FactoryConfigurationError e){
				LOGGER.error ("Creating the DOM document failed with FactoryConfigurationError:" + e.getMessage(), e);
				return null;
			}catch (ParserConfigurationException e){
				LOGGER.error ("Creating the DOM document failed with ParserConfigurationException: " + e.getMessage(), e);
				return null;
			}
			return dom;
		}

		/**
		 * @param items
		 * @param withHistory
		 * @param locale
		 * @param fullName
		 * @param queryName
		 * @param queryExpression
		 * @param useProjectSpecificID
		 */
		protected void writeFiles(List<ReportBean> items, boolean withHistory,
				Locale locale, String fullName, String queryName,
				String queryExpression, boolean useProjectSpecificID) {

			List<TFieldBean> fields = FieldBL.loadAll();
			FAQ_DIR = HandleHome.getTrackplus_Home()+File.separator+"Faqs";
			Long stamp = new Date().getTime();
			String sstamp = stamp.toString();
			FAQ_TMP_DIR = HandleHome.getTrackplus_Home()+File.separator+"Faqs"+File.separator+"tmp"+sstamp;
			HashMap<String,TWorkItemBean> fileNameMap = new HashMap<String,TWorkItemBean>();
			HashMap<Integer,String> workItemMap = new HashMap<Integer,String>();
		
			String title = null;
			String body = null;
			
			File faqDir = null;
			File faqOutDir = null;
			try {
				faqDir = new File(FAQ_DIR);
				faqOutDir = new File(FAQ_TMP_DIR);
				if (!faqDir.exists()  || !faqDir.isDirectory()) {
					Boolean success = faqDir.mkdirs();
					if (!success) {
						LOGGER.error("Could not create FAQ directory at "+ FAQ_DIR);
					}
				}
				if (!faqOutDir.exists()  || !faqOutDir.isDirectory()) {
					Boolean success = faqOutDir.mkdirs();
					if (!success) {
						LOGGER.error("Could not create FAQ out directory at "+ FAQ_TMP_DIR);
					}
				}
				cleanDirectory(FAQ_DIR, ".*\\.html");
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}

			for (ReportBean reportBean : items) {
				
				body = "";
				// Element item = dom.createElement ("item");
				TWorkItemBean workItemBean = reportBean.getWorkItemBean();
				String ptitle = workItemBean.getSynopsis();
				ptitle = StringEscapeUtils.escapeHtml(ptitle);
				ptitle = "<title>FAQ: " + ptitle +"</title>";
				
				String fileName = null;
				TProjectBean project = LookupContainer.getProjectBean(workItemBean.getProjectID());
				String prefix = null;
				if (project!=null) {
					prefix = project.getPrefix();
				}
				if (prefix==null) {
					prefix = "";
				}
				for (TFieldBean fieldBean : fields) {
					Integer fieldID = fieldBean.getObjectID();
					if (ApplicationBean.getApplicationBean().getSiteBean().getProjectSpecificIDsOn()) {
						if (fieldID == SystemFields.PROJECT_SPECIFIC_ISSUENO) {
							Object attributeValue = workItemBean.getAttribute(fieldID);
							if (attributeValue != null && !"null".equals(attributeValue)) {
								fileName = prefix+attributeValue.toString();
							}
						}
					} else {
						if (fieldID == SystemFields.ISSUENO) {
							Object attributeValue = workItemBean.getAttribute(fieldID);
							if (attributeValue != null && attributeValue != null && !attributeValue.equals("")
									&& !"null".equals(attributeValue)) {
								fileName = attributeValue.toString();
							}						
						}
					}
					if (fieldID == SystemFields.PROJECT) {
						Object attributeValue = workItemBean.getAttribute(fieldID);
						if (attributeValue != null) {
						}						
					}
					fieldBean.getName();
					Object attributeValue = workItemBean.getAttribute(fieldID);
					if (fieldID == SystemFields.SYNOPSIS || fieldID == SystemFields.DESCRIPTION) {
						if (attributeValue!=null) {
							IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
							if (fieldTypeRT!=null) {
								if (!fieldTypeRT.isLong()) {
									title = (String)attributeValue;
									title = StringEscapeUtils.escapeHtml(title);
									title = "<h1>"+ title + "</h1>";
								} else {
									body = workItemBean.getDescription();
									body = HTMLSanitiser.stripHTML(body);
									body = "<div class=\"faq\">" +  body + "</div>";
								}
							}
						}
					}
				}

				StringBuffer buf = new StringBuffer();
				buf.append(header);
				buf.append(ptitle);
				buf.append(styles);
				buf.append(title);
				buf.append(body);
				buf.append(footer);
				
				fileName = fileName+".html";

				URL url = writeFile(buf.toString(), fileName);
				if (url != null) {
					fileNameMap.put(fileName, workItemBean);
					workItemMap.put(workItemBean.getObjectID(),fileName);
				}
			}

			Map<Integer, List<TWorkItemBean>> projectMap = new HashMap<Integer, List<TWorkItemBean>>();
			Set<Integer> projectIDs = new HashSet<Integer>();
			for (Entry<String,TWorkItemBean> entry : fileNameMap.entrySet()) {
				TWorkItemBean workItemBean = entry.getValue();
				Integer projectID = workItemBean.getProjectID();
				if (!projectMap.containsKey(projectID)) {
					projectMap.put(projectID, new ArrayList<TWorkItemBean>());
					projectIDs.add(projectID);
				}
				List<TWorkItemBean> tmp = projectMap.get(projectID);
				tmp.add(workItemBean);
				projectMap.put(projectID,tmp);
			}

			//List<TProjectBean> projectBeanList = ProjectBL.loadByProjectIDs(projectIDs);
			LOGGER.info(projectIDs.size() + " workspaces for with items for FAQs");
			List<TreeNode> projectBeanTree = ProjectBL.getProjectTreeForReleasesWithCompletedPath(GeneralUtils.createIntegerListFromCollection(projectIDs), false, null, false, false, false, new TreeMap<Integer, TreeNode>());
			List<LabelValueBean> projects = new LinkedList<LabelValueBean>();
			loadAllInvolvedProjects(projectBeanTree, projects);
			LOGGER.info(projects.size() + " involved workspaces for FAQs.");

			// Create table of content from projects and workitems
			createTOC(projectBeanTree, projectMap, workItemMap );
			
			// Create the FAQ files from workitems
			File[] files = new File[fileNameMap.size() + projects.size()  + 1];
			

			int i = 0;
			for (String faq : fileNameMap.keySet() ) {
				try {
				files[i] = new File(FAQ_TMP_DIR+File.separator+faq);
				} catch (Exception e) {
					LOGGER.error("Problem creating file array for FAQs: " + e.getMessage(), e);
				}
				++i;
			}
			
			// Create a file for each project
			for (LabelValueBean proj : projects ) {
				try {
					String fname = "00PROJECT"+proj.getValue()+".html";
					File projFile = new File(FAQ_TMP_DIR+File.separator+fname);
					createProjFile(fname, proj.getLabel());
					files[i] = projFile;
				} catch (Exception e) {
					LOGGER.error("Problem creating file array for FAQs: " + e.getMessage(), e);
				}
				++i;
			}
			
			// Create the empty index.html file that will contain just the navigator
			createIndexHtmlFile("index.html");
			
			files[i] = new File(FAQ_TMP_DIR+File.separator+"index.html");
			
			createWebHelp(files);
			
			AttachBL.deleteDirectory(new File(FAQ_TMP_DIR));
			
			/*try {
				FileUtils.deleteDirectory(new File(FAQ_TMP_DIR));
			} catch (Exception e) {
				LOGGER.error("Can't delete temporary FAQ directory: " + e.getMessage(), e);
			}*/

			return;	
		}
		
		/**
		 * Loads all projectNodes from the tree into projects list 
		 * @param projectBeanTree
		 * @param projects output parameter
		 */
		private void loadAllInvolvedProjects(List<TreeNode> projectBeanTree, List<LabelValueBean> projects) {
			for (TreeNode projectNode : projectBeanTree ) {
				projects.add(new LabelValueBean(projectNode.getLabel(), projectNode.getId()));
				List<TreeNode> children = projectNode.getChildren();
				if (children!=null && !children.isEmpty()) {
					loadAllInvolvedProjects(children, projects);
				}
			}
		}
		
		/**
		 * @param text 
		 * @param fileName
		 * @return
		 */
		public URL writeFile(String text, String fileName) {
			OutputStream outputStream = null;
			URL url = null;
			try {
				byte data[] = text.getBytes("UTF-8");
				File out = new File(FAQ_TMP_DIR + File.separator + fileName);
				outputStream = new FileOutputStream(out);
				outputStream.write(data, 0, data.length);
				url = out.toURI().toURL();
				LOGGER.debug("Wrote FAQ file " + out.getAbsolutePath());
			} catch (Exception e) {
				LOGGER.error ("Writing FAQs failed with exception: " + e.getMessage(), e);
			} finally {
				try {
					// Close the stream regardless of what happens...
					outputStream.close();
				} catch (Exception e) {
					LOGGER.error("Could not close output stream for " + fileName);
				}
			}
			return url;
		}
		
		/**
		 * @param dom
		 * @param fileName
		 * @return
		 */
		public URL writeFile(Document dom, String fileName) {
			return writeFile(dom,fileName,"-//W3C//DTD XHTML 1.0 Strict//EN",
					"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "xhtml");
		}

		/**
		 * @param dom
		 * @param fileName
		 * @param publicID
		 * @param systemID
		 * @return
		 */
		private URL writeFile(Document dom, String fileName, String publicID, String systemID, String method) {
			Transformer transformer = null;
			try{
				TransformerFactory factory = TransformerFactory.newInstance();
				transformer = factory.newTransformer ();
				transformer.setOutputProperty (OutputKeys.INDENT, "yes");
				transformer.setOutputProperty (OutputKeys.METHOD, method);
				transformer.setOutputProperty (OutputKeys.ENCODING,"UTF-8");
				transformer.setOutputProperty ("{http://xml.apache.org/xslt}indent-amount", "4");
				if (publicID != null) {
					transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, publicID);
				}
				if (systemID != null) {
					transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, systemID);
				}
			} catch (TransformerConfigurationException e){
				LOGGER.error ("Creating the transformer failed with TransformerConfigurationException: " + e.getMessage(), e);
				return null;
			}
			StreamResult outputStream = null;
			try{
				File out = new File(FAQ_TMP_DIR + File.separator + fileName);
				outputStream = new StreamResult(out.toURI().getPath());  
				transformer.transform (new DOMSource(dom), outputStream);
				URL url = out.toURI().toURL();
				LOGGER.debug("Wrote FAQ file " + out.getAbsolutePath());
				return url;
			} catch (TransformerException e){
				LOGGER.error ("Transform failed with TransformerException: " + e.getMessage(), e);
			} catch (Exception ex) {
				LOGGER.error ("Transform failed with exception: " + ex.getMessage());				
			}
			return null;
		}

		/**
		 * @param projectNodes
		 * @param projectMap
		 */
		protected void createTOC(List<TreeNode> projectNodes, Map<Integer,List<TWorkItemBean>> projectMap,
				Map<Integer,String> workItemMap) {
			Document dom = null;
			try{
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
				DocumentBuilder builder = factory.newDocumentBuilder ();
				dom = builder.newDocument ();	
			}catch (FactoryConfigurationError e){
				LOGGER.error ("Creating the DOM document failed with FactoryConfigurationError:" + e.getMessage(), e);
				return;
			}catch (ParserConfigurationException e){
				LOGGER.error ("Creating the DOM document failed with ParserConfigurationException: " + e.getMessage(), e);
				return;
			}

			Element model = dom.createElement("xml-model");
			model.setAttribute("href", "schema/toc.rnc"); 
			model.setAttribute("type","application/relax-ng-compact-syntax");
			
			Element root = dom.createElement ("whc:toc");
			root.setAttribute("xmlns:whc","http://www.xmlmind.com/whc/schema/whc");
			root.setAttribute("xmlns","http://www.w3.org/1999/xhtml");	

			for (TreeNode projectNode : projectNodes) {
				appendProject(projectNode, projectMap, workItemMap, dom, root);
				/*Integer projecID = projectBean.getObjectID();
				Element entry = dom.createElement ("whc:entry");
				entry.setAttribute("href","00PROJECT"+projecID.toString()+".html");  //Project page
				Element title = createDomElement("whc:title",projectBean.getLabel(),dom);
				entry.appendChild(title);
				List<TWorkItemBean> faqs = projectMap.get(projecID);
				Integer count = 0;
				for (TWorkItemBean faq : faqs) {
					++count;
					Element fentry = dom.createElement("whc:entry");
					String fname = workItemMap.get(faq.getObjectID());
					fentry.setAttribute("href", fname);
					Element ftitle = createDomElement("whc:title",faq.getSynopsis(),dom);
					fentry.appendChild(ftitle);
					entry.appendChild(fentry);
				}
				root.appendChild(entry);*/
			}
			// dom.appendChild(model);
			dom.appendChild (root);
			
			writeFile(dom, "toc.xml", null, null, "xml");
			
			patchFile("toc.xml");
			
			writeIndexFile();
			return;
		}
		
		/**
		 * Append a project with items and subprojects recursively
		 * @param projectNode
		 * @param projectMap
		 * @param workItemMap
		 * @param dom
		 * @param parent
		 */
		private void appendProject(TreeNode projectNode, Map<Integer,List<TWorkItemBean>> projectMap,
			Map<Integer,String> workItemMap, Document dom, Element parent) {
			Integer projecID = Integer.valueOf(projectNode.getId());
			Element entry = dom.createElement ("whc:entry");
			entry.setAttribute("href","00PROJECT"+projecID.toString()+".html");  //Project page
			Element title = createDomElement("whc:title",projectNode.getLabel(),dom);
			entry.appendChild(title);
			List<TWorkItemBean> faqs = projectMap.get(projecID);
			if (faqs!=null) {
				//Integer count = 0;
				for (TWorkItemBean faq : faqs) {
					//++count;
					Element fentry = dom.createElement("whc:entry");
					String fname = workItemMap.get(faq.getObjectID());
					fentry.setAttribute("href", fname);
					Element ftitle = createDomElement("whc:title",faq.getSynopsis(),dom);
					fentry.appendChild(ftitle);
					entry.appendChild(fentry);
				}
			}
			List<TreeNode> childProjects = projectNode.getChildren();
			if (childProjects!=null && !childProjects.isEmpty()) {
				for (TreeNode childProject : childProjects) {
					appendProject(childProject, projectMap, workItemMap, dom, entry);
				}
			}
			parent.appendChild(entry);
		}
		
		/*
		 * Create the project specific HTML file from a template
		 */
		private void createProjFile(String file, String projectLabel) {
			File template = new File(FAQ_DIR+File.separator+"whc_template"+File.separator+"project.html");
			StringBuffer buf = new StringBuffer();
			String title = StringEscapeUtils.escapeHtml(projectLabel);
			LineIterator it = null;
			try {
				it = FileUtils.lineIterator(template, "UTF-8");
				try {
					while (it.hasNext()) {
						String line = it.nextLine();
						line = line.replaceAll("\\$title", title);
						buf.append(line+CRLF); 
					}
				} finally {
					it.close();
				}
			} catch (Exception e) {
				LOGGER.error("Can't copy project.html template file: " + e.getMessage(), e);
			}
			writeFile(buf.toString(), file);
		}
		
		/*
		 * Copy the index.html template to the temporary working directory
		 */
		private void createIndexHtmlFile(String file) {
			try {
			FileUtils.copyFile(new File(FAQ_DIR+File.separator+"whc_template"+File.separator+"index.html"), 
					           new File(FAQ_TMP_DIR+File.separator+"index.html"));
			} catch (Exception e) {
				LOGGER.error("Can't copy index.html template file: " + e.getMessage(), e);
			}
		}


		/**
		 * Creates a dom element
		 * @param elementName
		 * @param elementValue
		 * @param dom
		 * @return
		 */
		private Element createDomElement(String elementName, String elementValue, Document dom) {
			Element element = null;
			try {
				try {
					element = dom.createElement(elementName);
				} catch (DOMException e) {
					LOGGER.warn("Creating an XML node with the element name " + elementName + " failed with dom exception " + e);
				}
				if (element==null) {
					return null;
				}
				if (elementValue == null || "".equals(elementValue.trim())) {
					element.appendChild (dom.createTextNode (""));
				} else {
					try {
						element.appendChild(dom.createTextNode(Html2Text.stripNonValidXMLCharacters(elementValue)));
					} catch (DOMException e) {
						LOGGER.info("Creating the node for text element " + elementName + " and the value " + 
								elementValue + " failed with dom exception " + e);
						element.appendChild(dom.createTextNode (""));
					}
				}
			} catch (Exception e) {
				LOGGER.warn("Creating an XML node with the element name " + elementName + " failed with " + e);
			}					
			return element;
		}
		
		public void showMessage(String message, MessageType type) {
			if (type.equals(MessageType.ERROR)) {
				LOGGER.error(message);
			}
			if (type.equals(MessageType.DEBUG)) {
				LOGGER.debug(message);
			}
			if (type.equals(MessageType.WARNING)) {
				LOGGER.warn(message);
			}
			if (type.equals(MessageType.INFO)) {
				LOGGER.info(message);
			}
			if (type.equals(MessageType.VERBOSE)) {
				LOGGER.info(message);
			}	
		}
		

		protected void createWebHelp(File[] xhtmlURLs) {
			Compiler compiler = new Compiler(this);
			
			if (LOGGER.isDebugEnabled()) {
				compiler.setVerbose(true);
			}
		 	compiler.setLocalJQuery(true);
		 	
			//		if (!compiler.parseParameters(params)) {
			//		    LOGGER.error("Problem running WebHelp compiler");
			//		}
			try {
				File indexURL = new File(FAQ_TMP_DIR+ File.separator + "index.xml");
				File tocURL = new File(FAQ_TMP_DIR+ File.separator + "toc.xml");
				URL fileList = new File(FAQ_DIR+ File.separator + "whc_template" + File.separator + "file.list").toURI().toURL();
				compiler.setTemplateManifest(fileList);
				if (!compiler.compile(xhtmlURLs, tocURL, indexURL, new File(FAQ_DIR))) {
					LOGGER.error("Problem compiling WebHelp");
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		
		private void writeIndexFile() {
			File out = new File(FAQ_TMP_DIR+File.separator+"index.xml");
			FileOutputStream os = null;	
			
			try {

				os = FileUtils.openOutputStream(out);
				
				String dummyIndex = 
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+"<?xml-model href=\"schema/index.rnc\" "
				+"    type=\"application/relax-ng-compact-syntax\"?>"
				+"<index xmlns=\"http://www.xmlmind.com/whc/schema/whc\""
				+"       xmlns:htm=\"http://www.w3.org/1999/xhtml\">"
			    +"	  <entry xml:id=\"dummy1\">"
			    +"	     <term>TBD 1</term>"
			    +"       <entry>"
			    +"           <term>TBD 2</term>"
			    +"           <anchor href=\"page_a.html#cbf1000f\"/>"
			    +"       </entry>"
			    +"    </entry>"
			    +"    <entry xml:id=\"cbr1000rr\">"
			    +"      <term>CBR1000RR</term>"
			    +"      <anchor href=\"page_a.html#cbr1000rr\"/>"
			    +"      <seeAlso ref=\"dummy1\"/>"
			    +"    </entry>"
			    +"</index>";
				
				String emptyIndex = 
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+"<?xml-model href=\"schema/index.rnc\" "
				+"    type=\"application/relax-ng-compact-syntax\"?>"
				+"<index xmlns=\"http://www.xmlmind.com/whc/schema/whc\""
				+"       xmlns:htm=\"http://www.w3.org/1999/xhtml\">"
			    +"</index>";

				os.write(emptyIndex.getBytes(Charset.forName("UTF-8")));

			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			} finally {
				try {
					os.flush();
					os.close();
				} catch (Exception te) {
					LOGGER.error(te.getMessage(), te);
				}
			}
		}
		
		private void patchFile(String file) {
			File in = new File(FAQ_TMP_DIR+File.separator+file);
			File out = new File(FAQ_TMP_DIR+File.separator+"tmp.xml");
			
			FileInputStream is = null;
			FileOutputStream os = null;
			
			try {
				is = new FileInputStream(in.toURI().getPath()); //FileUtils.openInputStream(in);
				os = new FileOutputStream(out.toURI().getPath());//FileUtils.openOutputStream(out);
				LineIterator li = FileUtils.lineIterator(in, "UTF-8");
				int i = 0;
				while (li.hasNext()) {
					String line = li.nextLine();
					if (i < 5) {
						line = line.replaceFirst("<whc:toc",
								"<?xml-model href=\"schema/toc.rnc\" \n type=\"application/relax-ng-compact-syntax\"?> \n<whc:toc");				
					}
					os.write(line.getBytes(Charset.forName("UTF-8")));
					++i;
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			} finally {
				try {
					os.flush();
					is.close();
					os.close();
					in.delete();
					//FileUtils.forceDelete(in);
					//FileUtils.copyFile(out, in);
				} catch (Exception te) {
					LOGGER.error(te.getMessage(), te);
				}
				try {
					FileUtils.copyFile(out, in);
				} catch (Exception te) {
					LOGGER.error(te.getMessage(), te);
				}
			}
		}
		
		
		private void cleanDirectory(String directory, String pattern) {
		    File dir = new File(directory);
		    if (!dir.exists()) {
		      LOGGER.warn(directory + " does not exist");
		      return;
		    }
		    String patt = pattern;

		    String[] info = dir.list();
		    for (int i = 0; i < info.length; i++) {
		      File n = new File(directory + File.separator + info[i]);
		      if (!n.isFile()) { // skip ., .., other directories, etc.
		        continue;
		      }
		      if (!info[i].matches(patt)) { // name doesn't match
		        continue;
		      }
		      LOGGER.debug("removing " + n.getPath());
		      if (!n.delete())
		        LOGGER.error("Couldn't remove " + n.getPath());
		    }
		}
	}

}
