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

package com.aurel.track.admin.customize.category.report.execute;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.AgeFileFilter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.ReportBeanLoader;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dbase.HandleHome;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.linkType.ILinkType;
import com.aurel.track.linkType.LinkTypeBL;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanLink;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.util.PdfUtils;
import com.trackplus.track.util.html.Html2LaTeX;
import com.xmlmind.util.Console;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * ReportBeansToLaTeXConverter provides the methods to generate a PDF document
 * based on a LaTeX template from a set of report beans. This class can thus
 * generate nicely formatted PDF documents from Wiki documents.
 *
 */
public class ReportBeansToLaTeXConverter implements Console {

	private static final Logger LOGGER = LogManager.getLogger(ReportBeansToLaTeXConverter.class);

	private static String LATEXDIR = null;
	private static final String CRLF = System.getProperty("line.separator");

	private String latexTmpDir = null;
	private String latexCmd = ApplicationBean.getInstance().getLatexCommand();
	private StringBuilder debugTrace;
	private File errorFile;
	private static Pattern inlineItemNoPattern = Pattern.compile(".*\\[issue ([0-9]+)/\\]");

	/**
	 *
	 * @param reportBeans
	 * @param withHistory
	 * @param personBean
	 * @param locale
	 * @param queryName
	 * @param queryExpression
	 * @param longTextIsPlain
	 * @param useProjectSpecificID
	 * @return
	 */
	public static Document getDocumentFromReportBeans(ReportBeans reportBeans, boolean withHistory, TPersonBean personBean, Locale locale, String queryName,
			String queryExpression, boolean longTextIsPlain, boolean useProjectSpecificID, File template, File templateDir) {

		if (reportBeans == null) {
			return null;
		}

		personBean.getObjectID();
		List<ReportBean> items = reportBeans.getItems();
		if (items == null) {
			return null;
		}

		ReportBeanLoader.addISOValuesToReportBeans(items, personBean.getObjectID(), locale);

		// add the history to each ReportBean: the long fields history elements
		// can be transformed to the longTextType
		// needed and consequently we can spare storing also the real attribute
		// values in the HistoryValues objects
		// (near the showValues) but the actual long fields cannot be
		// transformed at this moment because
		// they are stored in the session and it would affect the future
		// renderings of the ReportBeans

		ReportBeansToLaTeXConverter rc = new ReportBeansToLaTeXConverter();

		rc.generatePdf(null, items, withHistory, locale, personBean, queryName, queryExpression, useProjectSpecificID, template, templateDir);

		return rc.convertToDOM(items, withHistory, locale, personBean, queryName, queryExpression, useProjectSpecificID, template.getName());

	}

	/**
	 * Generate the PDF from this Genji document via a Freemarker tagged LaTeX
	 * template.
	 *
	 * @param items
	 * @param withHistory
	 * @param locale
	 * @param fullName
	 * @param queryName
	 * @param queryExpression
	 * @param useProjectSpecificID
	 */
	public File generatePdf(TWorkItemBean workItem, List<ReportBean> items, boolean withHistory, Locale locale, TPersonBean user, String queryName,
			String queryExpression, boolean useProjectSpecificID, File template, File templateDir) {

		prepareDirectories(templateDir);

		StringBuilder templateBuffer = new StringBuilder();
		StringBuilder inlineMacroBuffer = new StringBuilder();

		// First remove comments for Freemarker commands from template.
		// Replace LaTeX escaped characters by their unescaped values
		try {
			List<String> lines = FileUtils.readLines(template, "UTF-8");
			if (lines.size() < 1) {
				debugTrace.append("The template file " + template.getName() + " has no lines or does not exist.\n");
				debugTrace.append("Make sure you have named the master template file like the tlx package.\n");
			}
			for (String line : lines) {
				// The following template snippet determines how inline items
				// are being rendered.
				// This permits to create live links to the original item, for
				// example.
				if (line.startsWith("% %%ITP ")) {
					line = line.replace("% %%ITP ", "");
					inlineMacroBuffer.append(line + CRLF);
				} else {
					line = line.replace("\\${", "${").replace("% %%TP", "").replace("\\${", "${").replace("% %%TP", "");
					templateBuffer = templateBuffer.append(line + CRLF);
				}
			}
		} catch (IOException e) {
			LOGGER.error("Could not prepare template file for Freemarker processing " + template.getAbsolutePath());
			debugTrace.append(ExceptionUtils.getStackTrace(e) + CRLF);
		}

		debugTrace.append("Just before processing by Freemarker: " + CRLF + CRLF + templateBuffer + CRLF);
		debugTrace.append(templateBuffer + CRLF);

		// Create the context map for Freemarker using the result set.
		Map<String, Object> context = fillFreemarkerContext(workItem, items, user, locale, inlineMacroBuffer);

		// Create the LaTeX file using the Freemarker enriched LaTeX template
		// file
		File latexFile = createProcessedLaTeXFile(context, templateBuffer, template.getName());

		try {
			Thread.sleep(1000);
		} catch (Exception ex) {
			LOGGER.debug(ExceptionUtils.getStackTrace(ex), ex);
		}
		// Run twice to get labels and figures right
		int exitValue = runPdflatex(new File(latexTmpDir), latexFile, 1);
		exitValue = runPdflatex(new File(latexTmpDir), latexFile, 2);
		String pdf = latexFile.getAbsolutePath().replace(".tex", ".pdf");

		if (exitValue == -99) {
			File pdfFile = new File(pdf);
			try {
				FileUtils.copyFile(HandleHome.getMissingLaTeXPdf(), pdfFile);
			} catch (Exception ex) {
				LOGGER.error("Problem writing missing LaTeX information.", ex);
			}
			return pdfFile;
		}

		File pdfFile = new File(pdf);
		if (!pdfFile.exists() || pdfFile.length() < 10 || exitValue != 0 ) {
			try {
				createDebugInfoPdf(pdf, latexFile);
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		}
		return new File(pdf);
	}

	/**
	 * Create a map containing all objects for the Freemarker context. This
	 * resolves inline linked items as well. All rich text in this map has been
	 * converted to LaTeX format. The map is being used by the Freemarker
	 * processor to generate a complete LaTeX document, which then is processed
	 * by the LaTeX processor.
	 *
	 * @param items
	 * @param parser
	 * @return
	 */
	protected Map<String, Object> fillFreemarkerContext(TWorkItemBean workItem, List<ReportBean> items, TPersonBean user, Locale locale,
			StringBuilder inlineMacroBuffer) {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> topicList = new ArrayList<Map<String, Object>>();
		List<TFieldBean> fields = FieldBL.loadAll();
		Map<Integer, ILinkType> linkTypeIDToLinkTypeMap = LinkTypeBL.getLinkTypeIDToLinkTypeMap();
		if (workItem != null && items != null) {
			LOGGER.debug("Collecting Freemarker context for document " + workItem.getObjectID() + " and " + items.size() + " items.");
		}
		boolean isDocument = false;
		// Get the document itself
		if (workItem != null) {
			TListTypeBean lt = LookupContainer.getItemTypeBean(workItem.getListTypeID());
			if (lt != null && (lt.getTypeflag().equals(TListTypeBean.TYPEFLAGS.MEETING) || lt.getTypeflag().equals(TListTypeBean.TYPEFLAGS.DOCUMENT))) {
				int[] ids = { workItem.getObjectID() };
				items.addAll(LoadItemIDListItems.getReportBeansByWorkItemIDs(ids, false, user.getObjectID(), locale, null, SystemFields.INTEGER_PROJECT, true,
						true, true, true, true, true, true, true, false));
			}
			// Get the document attachments
			processAttachments(workItem);
		}

		Map<Integer, ReportBean> inlineItems = getInlineLinkedItems(items, user, locale, fields);

		int noOfTables = 0;
		int noOfFigures = 0;

		for (ReportBean reportBean : items) {

			LOGGER.debug("Processing document or section '" + reportBean.getWorkItemBean().getSynopsis() + "'");

			isDocument = false;

			TWorkItemBean workItemBean = reportBean.getWorkItemBean();

			// Get the type of workItemBean (meeting, document, etc.)
			TListTypeBean lt = LookupContainer.getItemTypeBean(workItemBean.getListTypeID());

			if (lt != null && (lt.getTypeflag().equals(TListTypeBean.TYPEFLAGS.MEETING) || lt.getTypeflag().equals(TListTypeBean.TYPEFLAGS.DOCUMENT))) {

				LOGGER.info("Document: " + workItemBean.getSynopsis());
				isDocument = true;
				context.put("meetingTopics", topicList);
				context.put("topics", topicList);
			}

			processAttachments(workItemBean);

			String ptitle = Jsoup.parse(workItemBean.getSynopsis()).text();

			// Get the corresponding project or workspace
			TProjectBean project = LookupContainer.getProjectBean(workItemBean.getProjectID());
			String prefix = null;
			if (project != null) {
				prefix = project.getPrefix();
			}
			if (prefix == null) {
				prefix = "";
			}

			HashMap<String, Object> fieldMap = null;

			if (isDocument) {
				context.put("title", texify(ptitle));
				context.put("project", texify(project.getLabel()));
				String licenseHolder = texify(ApplicationBean.getInstance().getLicenseHolder());
				context.put("licenseHolder", licenseHolder);
				context.put("locale", locale.getLanguage());
				context.put("serverurl", texify(ApplicationBean.getInstance().getSiteBean().getServerURL()
						+ ApplicationBean.getInstance().getServletContext().getContextPath()));

				// Get the field map (Freemarker context) for this document
				fieldMap = getFieldMap(reportBean, inlineItems, fields, inlineMacroBuffer);

				for (Entry<String, Object> entry : fieldMap.entrySet()) {
					if (entry.getValue().getClass().equals(String.class)) {
						context.put(entry.getKey(), texify(entry.getValue().toString()));
					} else {
						context.put(entry.getKey(), entry.getValue());
					}
				}
			} else {
				LOGGER.debug("Section title: " + workItemBean.getSynopsis());

				// Get the field map (Freemarker context) for this section
				fieldMap = getFieldMap(reportBean, inlineItems, fields, inlineMacroBuffer);

				SortedSet<ReportBeanLink> reportBeanLinks = reportBean.getReportBeanLinksSet();
				if (reportBeanLinks!=null) {
					for (ReportBeanLink reportBeanLink : reportBeanLinks) {
						Map<String, String> itemLinkSpecificMap = ReportBeansToXML.getLinkSpecificData(reportBeanLink, linkTypeIDToLinkTypeMap, locale);
						if (itemLinkSpecificMap!=null) {
							for (Map.Entry<String, String> entry : itemLinkSpecificMap.entrySet()) {
								if (entry.getValue().getClass().equals(String.class)) {
									fieldMap.put(entry.getKey(), texify(entry.getValue().toString()));
								} else {
									fieldMap.put(entry.getKey(), entry.getValue());
								}
							}
						}
					}
				}
				fieldMap.put("title", texify(ptitle));

				fieldMap.put("Title", texify(ptitle));
				fieldMap.put("project", texify(project.getLabel()));
				fieldMap.put("Project", texify(project.getLabel()));
				fieldMap.put("serverurl", texify(ApplicationBean.getInstance().getSiteBean().getServerURL()
						+ ApplicationBean.getInstance().getServletContext().getContextPath()));
				noOfFigures = noOfFigures + Integer.valueOf((String) fieldMap.get("noOfFigures"));
				noOfTables = noOfTables + Integer.valueOf((String) fieldMap.get("noOfTables"));
			}

			fieldMap.put("isBudgetOrPlanConflict", reportBean.isBudgetOrPlanConflict());
			fieldMap.put("isBudgetConflict", reportBean.isBudgetConflict());
			fieldMap.put("isPlannedValueConflict", reportBean.isPlannedValueConflict());

			fieldMap.put("StateFlag", reportBean.getStateFlag());
			fieldMap.put("TopDownDateDueFlag", reportBean.getTopDownDateDueFlag());
			fieldMap.put("DateConflict", reportBean.isDateConflict());
			fieldMap.put("isCommittedDateConflict", reportBean.isCommittedDateConflict());
			fieldMap.put("isSummary", reportBean.isSummary());
			fieldMap.put("isTargetDateConflict", reportBean.isTargetDateConflict());

			// TODO Add actual amount of work, budget, planned value, estimated remaining values

			if (!isDocument) {
				topicList.add(fieldMap);
			}

		}
		if (context.get("topics") == null) {
			context.put("topics", topicList);
			context.put("items", topicList);
			context.put("licenseHolder", texify(ApplicationBean.getInstance().getLicenseHolder()));
			context.put("locale", locale.getLanguage());
			context.put("serverurl", texify(ApplicationBean.getInstance().getSiteBean().getServerURL()
					+ ApplicationBean.getInstance().getServletContext().getContextPath()));

		}
		context.put("noOfFigures", noOfFigures);
		context.put("noOfTables", noOfTables);

		return context;
	}

	/**
	 * This method creates for a single topic (or document without sections) a
	 * map with field names as key and values in LaTeX format. Inline items are
	 * resolved for long text fields. Inline items are furthermore accessible
	 * via field name "inlineItems" as a map with the item oid as a key and the
	 * map of fields as a value.
	 *
	 * @param reportBean
	 * @param inlineItems
	 * @param fields
	 * @return
	 */
	private static HashMap<String, Object> getFieldMap(ReportBean reportBean, Map<Integer, ReportBean> inlineItems, List<TFieldBean> fields,
			StringBuilder inlineMacroBuffer) {
		Integer inlineOid;

		LOGGER.debug("Creating the context (field map) for report bean '" + reportBean.getWorkItemBean().getSynopsis() + "'");
		HashMap<String, Object> topicFieldMap = new HashMap<String, Object>();
		HashMap<Integer, Object> inlineItemMap = new HashMap<Integer, Object>();

		int noOfTables = 0;
		int noOfFigures = 0;

		String TOKEN = ".X27645154678345875";
		int index = 0;

		for (TFieldBean fieldBean : fields) {
			Integer fieldID = fieldBean.getObjectID();
			Object value = reportBean.getShowValue(fieldID);
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			if (fieldTypeRT != null) {
				if (fieldTypeRT.isLong()) {
					String s = value.toString();
					s = s.trim();
					Matcher matcher = inlineItemNoPattern.matcher(s);
					HashMap<Integer, String> inlines = new HashMap<Integer, String>();

					while (matcher.find()) {
						LOGGER.debug("Found inline item here...");
						inlineOid = Integer.valueOf(matcher.group(1));
						ReportBean irb = inlineItems.get(inlineOid);
						if (irb != null) {
							// Render the inline item here
							HashMap<String, Object> inlineItemFieldMap = getFieldMap(irb, inlineItems, fields, inlineMacroBuffer);

							inlineItemFieldMap.put("serverurl", ApplicationBean.getInstance().getSiteBean().getServerURL()
									+ ApplicationBean.getInstance().getServletContext().getContextPath());

							// Render the inline item here
							LOGGER.debug("Rendering inline item here...");
							inlines.put(index, renderInlineItem(irb, inlineItemFieldMap, inlineMacroBuffer));

							s = s.replaceFirst("\\[issue [0-9]+\\/\\]", index + TOKEN);
							inlineItemMap.put(inlineOid, inlineItemFieldMap);
						} else {
							s = s.replaceAll("\\[issue [0-9]+\\/\\]", "");
						}
						++index;
					}

					value = Html2LaTeX.getLaTeX(s);
					String sl = value.toString();
					for (Entry<Integer, String> entry : inlines.entrySet()) {
						sl = sl.replace(entry.getKey() + TOKEN, entry.getValue());
					}
					if (sl.contains("\\begin{tabular")) {
						++noOfTables;
					}
					if (sl.contains("\\begin{fig")) {
						++noOfFigures;
					}
					value = sl;
				}
				if (fieldID.equals(SystemFields.WBS)) { // replace WBS by level
					String[] wbstokens = ((String) value).split("\\.");
					value = wbstokens.length;
				}
				topicFieldMap.put(fieldBean.getName(), value);
				if ("Synopsis".equals(fieldBean.getName())) {
					topicFieldMap.put("Title", value);
				}
			}
		}
		topicFieldMap.put("inlineItems", inlineItemMap);
		topicFieldMap.put("noOfFigures", String.valueOf(noOfFigures));
		topicFieldMap.put("noOfTables", String.valueOf(noOfTables));

		return topicFieldMap;
	}

	/**
	 * Creates a map with all inline linked items
	 *
	 * @param items
	 * @param user
	 * @param locale
	 * @param fields
	 * @return
	 */
	private static Map<Integer, ReportBean> getInlineLinkedItems(List<ReportBean> items, TPersonBean user, Locale locale, List<TFieldBean> fields) {

		HashMap<Integer, ReportBean> result = new HashMap<Integer, ReportBean>();

		ArrayList<Integer> ids = new ArrayList<Integer>();

		for (ReportBean reportBean : items) {

			for (TFieldBean fieldBean : fields) {
				Integer fieldID = fieldBean.getObjectID();
				Object value = reportBean.getShowValue(fieldID);
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldTypeRT != null && fieldTypeRT.isLong()) {
					Matcher matcher = inlineItemNoPattern.matcher(value.toString());
					while (matcher.find()) {
						ids.add(Integer.valueOf(matcher.group(1)));
					}
				}
			}
		}

		int[] iids = new int[ids.size()];
		for (int i = 0; i < ids.size(); ++i) {
			iids[i] = ids.get(i).intValue();
		}

		List<ReportBean> linkedBeans = LoadItemIDListItems.getReportBeansByWorkItemIDs(iids, false, user.getObjectID(), locale, null,
				SystemFields.INTEGER_PROJECT, true, true, true, true, true, true, true, true, false);
		for (ReportBean rb : linkedBeans) {
			result.put(rb.getWorkItemBean().getObjectID(), rb);
		}
		LOGGER.debug("Collected " + linkedBeans.size() + " inline items");
		return result;
	}

	/**
	 *
	 * @param irb
	 * @param inlineItemFieldMap
	 * @param inlineMacroBuffer
	 * @return
	 */
	private static String renderInlineItem(ReportBean irb, HashMap<String, Object> inlineItemFieldMap, StringBuilder inlineMacroBuffer) {
		if (inlineMacroBuffer == null || "".equals(inlineMacroBuffer.toString())) {
			return "<span class=\"inlineLink\">" + irb.getWorkItemBean().getDescription() + "</span>";
		}
		String result = createProcessedInlineItems(inlineItemFieldMap, inlineMacroBuffer);
		return result;
	}

	/**
	 * This method renders inline items based on a macro included in the main
	 * template. In the template file the macro must be marked like this: <code>
	 * % %%ITP ${Description} ${IssueNo}
	 * </code> The inline macro buffer passed here has the comment part removed.
	 *
	 * @param context
	 * @param inlineMacroBuffer
	 */
	private static String createProcessedInlineItems(Map<String, Object> context, StringBuilder inlineMacroBuffer) {
		if (inlineMacroBuffer == null || "".equals(inlineMacroBuffer)) {
			return null;
		}

		Template freemarkerTemplate = null;

		StringWriter texWriter = new StringWriter();
		StringBuffer debugMessages = new StringBuffer();
		try {
			debugMessages.append("Creating Freemarker template..." + CRLF);
			Configuration freemarkerConfig = new Configuration();
			freemarkerConfig.setTemplateExceptionHandler(new LaTeXFreemarkerExceptionHandler());
			freemarkerTemplate = new Template("InlineItems", new StringReader(inlineMacroBuffer.toString()), freemarkerConfig);
			debugMessages.append("Processing the Freemarker LaTeX template..." + CRLF);
			freemarkerTemplate.process(context, texWriter);
			debugMessages.append("Freemarker LaTeX template processed." + CRLF);
		} catch (Exception e) {
			LOGGER.error("Problem processing template: " + CRLF + debugMessages.toString());
			String st = ExceptionUtils.getStackTrace(e);
			LOGGER.debug(st, e);
			texWriter = new StringWriter();
			texWriter.append(inlineMacroBuffer.toString());

		}

		texWriter.flush();

		return texWriter.toString();
	}

	/**
	 *
	 * @param context
	 * @param templateBuffer
	 */
	protected File createProcessedLaTeXFile(Map<String, Object> context, StringBuilder templateBuffer, String fileName) {
		Template freemarkerTemplate = null;
		File latexFile = new File(latexTmpDir + File.separator + fileName);

		StringWriter texWriter = new StringWriter();
		StringBuffer debugMessages = new StringBuffer();
		try {
			debugMessages.append("Creating Freemarker template..." + CRLF);
			Configuration freemarkerConfig = new Configuration();
			freemarkerConfig.setTemplateExceptionHandler(new LaTeXFreemarkerExceptionHandler());
			freemarkerTemplate = new Template(fileName, new StringReader(templateBuffer.toString()), freemarkerConfig);
			debugMessages.append("Processing the Freemarker LaTeX template..." + CRLF);
			freemarkerTemplate.process(context, texWriter);
			debugMessages.append("Freemmarker LaTeX template processed." + CRLF);
		} catch (Exception e) {
			LOGGER.error("Problem processing template: " + CRLF + debugMessages.toString());
			String st = ExceptionUtils.getStackTrace(e);
			debugTrace.append(debugMessages.toString() + CRLF);
			debugTrace.append(e.getMessage());
			LOGGER.debug("Problem processing template:", e);
			texWriter = new StringWriter();
			String tb = templateBuffer.toString();
			tb.replace("\\end{document}",
					"Problem processing template: " + CRLF + CRLF + debugMessages.toString() + CRLF + CRLF + st + CRLF + CRLF + "\\end{document}");
			texWriter.append(templateBuffer.toString());
		}

		texWriter.append(CRLF + "% The following keys are available for the enclosing document itself:" + CRLF);

		for (String item : context.keySet()) {
			texWriter.append("% " + item + CRLF);
		}
		texWriter.append(CRLF + "% The following keys are available for the document sections:" + CRLF);
		if (!(context.get("meetingTopics") == null) && !((ArrayList<Map<String, Object>>) context.get("meetingTopics")).isEmpty()) {
			for (String item : ((ArrayList<Map<String, Object>>) context.get("meetingTopics")).get(0).keySet()) {
				texWriter.append("% meetingTopics." + item + CRLF);
			}
		} else if (!(context.get("items") == null) && !((ArrayList<Map<String, Object>>) context.get("items")).isEmpty()) {
			for (String item : ((ArrayList<Map<String, Object>>) context.get("items")).get(0).keySet()) {
				texWriter.append("% items." + item + CRLF);
			}
		}

		texWriter.flush();
		String processedTex = texWriter.toString();

		LOGGER.debug(processedTex);

		try {
			FileUtils.writeStringToFile(latexFile, processedTex.toString(), "UTF-8");
		} catch (Exception e) {
			LOGGER.error("Cannot write file :" + latexFile.getAbsolutePath());
		}
		return latexFile;
	}

	/**
	 * Write the LateX generated PDF into the output stream. In case the file
	 * could not be generated, write the debug info file.
	 *
	 * @param os
	 *            the output stream to write the file to
	 * @param pdfFile
	 *            the PDF file to write to the output stream
	 */
	public static void streamPdf(OutputStream os, File pdfFile) {
		if (pdfFile.length() < 2) {
			pdfFile = new File(pdfFile.getParent() + "/errors.pdf");
		}
		try {
			InputStream is = new FileInputStream(pdfFile);
			IOUtils.copy(is, os);
			is.close();
		} catch (FileNotFoundException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Convert the result set into an XML structure.
	 *
	 * @param items
	 * @param withHistory
	 * @param locale
	 * @param personBean
	 * @param filterName
	 * @param filterExpression
	 * @param useProjectSpecificID
	 * @param outfileName
	 * @return
	 */
	public Document convertToDOM(List<ReportBean> items, boolean withHistory, Locale locale, TPersonBean personBean, String filterName, String filterExpression,
			boolean useProjectSpecificID, String outfileName) {
		Document doc = null;

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("pdfFile");
			doc.appendChild(rootElement);

			// set attribute to staff element
			Attr attr = doc.createAttribute("file");
			outfileName = outfileName.replace(".tex", ".pdf");
			attr.setValue(latexTmpDir + File.separator + outfileName);
			rootElement.setAttributeNode(attr);

		} catch (FactoryConfigurationError e) {
			LOGGER.error("Creating the DOM document failed with FactoryConfigurationError:" + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return null;
		} catch (ParserConfigurationException e) {
			LOGGER.error("Creating the DOM document failed with ParserConfigurationException: " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return null;
		}

		return doc;
	}

	/*
	 * Create some useful output in case the PDF file could not be generated.
	 */
	protected void createDebugInfoPdf(String pdf, File latexFile) {
		Integer lineNo = 0;
		Pattern lineNoPattern = Pattern.compile("l\\.([0-9]+)");

		try {
			StringBuilder latexout = new StringBuilder();
			File stdout = new File(latexTmpDir+File.separator+"stdout.log");
			if (stdout.exists()) {
				List<String> lines = FileUtils.readLines(stdout, "UTF-8");
				int i = 0;
				int j = 0;
				boolean waitForErrorLine = false;
				for (String line: lines) {
					if (i < 2) {
						latexout.append(line+"\n");
					}

					if (line.startsWith("!")) {
						waitForErrorLine = true;
					}

					if (waitForErrorLine) {
						Matcher m = lineNoPattern.matcher(line);
						if (m.find()) {
							lineNo = Integer.valueOf(m.group(1));
							waitForErrorLine = false;
							j = 2;
						}
						latexout.append(line+"\n");
					} else if (j > 0) {
						latexout.append(line+"\n");
						--j;
						if (j == 0) break;
					}
					++i;
				}
				debugTrace = latexout;
			}

			debugTrace.append("% ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + CRLF);
			debugTrace.append("There was a problem creating the PDF" + CRLF);
			debugTrace.append("The problematic area is marked below." + CRLF + CRLF);
			List<String> lines = FileUtils.readLines(latexFile, "UTF-8");
			int i = 1;
			for (String line : lines) {
				if (i == lineNo) {
					line = "% vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv " + CRLF + line + CRLF + "% ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ ";
				}
				debugTrace.append(line + CRLF);
				++i;
			}

			FileUtils.writeStringToFile(errorFile, debugTrace.toString(), "UTF-8");
			PdfUtils.createPdfFromText(debugTrace, new File(pdf));
		} catch (Exception ex) {
			LOGGER.error("Problem writing debug trace.", ex);
		}
	}

	protected void processAttachments(TWorkItemBean workItem) {
		List<TAttachmentBean> figures = null;
		if (workItem != null) {
			figures = AttachBL.getAttachmentsImage(workItem.getObjectID());
		}

		if (figures != null && !figures.isEmpty()) {
			for (TAttachmentBean figure : figures) {
				File imageFile = new File(figure.getFullFileNameOnDisk());
				try {
					String figSuffix = "." + FilenameUtils.getExtension(figure.getFileName());
					FileUtils.copyFile(imageFile, new File(latexTmpDir + File.separator + "fig" + figure.getObjectID() + figSuffix));

				} catch (IOException e) {
					LOGGER.error("Could not read attached image file " + imageFile.getAbsolutePath());
				}
			}
		}
	}

	@Override
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

	/**
	 * Create a temporary working directory and copy all template files there.
	 *
	 * @param templateDir
	 *            the directory containing the original template files
	 */
	public void prepareDirectories(File templateDir) {

		LATEXDIR = HandleHome.getTrackplus_Home() + File.separator + "Reports" + File.separator + "LaTeX";
		latexTmpDir = LATEXDIR + File.separator + "tmp" + String.valueOf(new Date().getTime());
		errorFile = new File(latexTmpDir + "/errors.txt");
		debugTrace = new StringBuilder();

		File flatexDir = null;
		File flatexTmpDir = null;
		try {
			flatexDir = new File(LATEXDIR);
			flatexTmpDir = new File(latexTmpDir);
			if (!flatexDir.exists() || !flatexDir.isDirectory()) {
				Boolean success = flatexDir.mkdirs();
				if (!success) {
					LOGGER.error("Could not create LaTeX directory at " + LATEXDIR);
				}
			}
			if (!flatexTmpDir.exists() || !flatexTmpDir.isDirectory()) {
				Boolean success = flatexTmpDir.mkdirs();
				if (!success) {
					LOGGER.error("Could not create LaTeX out directory at " + latexTmpDir);
				}
			}
			Date thresholdDate = new Date(new Date().getTime() - 1000 * 60 * 3);

			Iterator<File> filesToDelete = FileUtils.iterateFilesAndDirs(flatexDir, new AgeFileFilter(thresholdDate), new AgeFileFilter(thresholdDate));

			while (filesToDelete.hasNext()) {
				File aFile = filesToDelete.next();
				if (aFile.isDirectory() && !aFile.getAbsolutePath().equals(flatexDir.getAbsolutePath())) {
					try {
						FileUtils.deleteDirectory(aFile);
					} catch (Exception e) {
						LOGGER.info("Problem deleting " + aFile.getAbsolutePath(), e);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		/*
		 * Copy the template files including logos, styles, etc. to the
		 * temporary working directory
		 */
		try {
			FileUtils.copyDirectory(new File(templateDir.getAbsolutePath()), flatexTmpDir);
		} catch (IOException e) {
			LOGGER.error("Could not copy template files from " + templateDir.getAbsolutePath());
		}
	}

	/**
	 * Escape LaTeX special characters like $, %, and &
	 * @param text
	 * @return the text with LaTeX special characters escaped
	 */

	protected static String texify(String text) {
		text = text.replace("&", "\\&");
		text = text.replace("%", "\\%");
		text = text.replace("$", "\\$");
		return text;
	}

	/**
	 *
	 * @param workDir
	 * @param latexFile
	 */
	protected int runPdflatex(File workDir, File latexFile, int nrOfRuns) {

		if (latexCmd == null) {
			return -99;
		}

		int exitValue = 0;

		try {

			String[] cmd = new String[] { latexCmd, "--halt-on-error", "-output-directory=" + workDir, latexFile.getAbsolutePath() };

			String texpath = new File((new File(latexCmd)).getParent()).getAbsolutePath();

			ProcessBuilder latexProcessBuilder = new ProcessBuilder(cmd);
			latexProcessBuilder.directory(workDir);
			Map <String,String> env = latexProcessBuilder.environment();
			String path = env.get("PATH");
			if  (path != null) {
				path = texpath+":"+path;
				env.put("PATH", path);
			}

			File stdoutlog = new File(workDir+File.separator+"stdout.log");
			latexProcessBuilder.redirectOutput(Redirect.appendTo(stdoutlog));

			File stderrlog = new File(workDir+File.separator+"stderr.log");
			latexProcessBuilder.redirectError(Redirect.appendTo(stderrlog));

			ProcessExecutor latexProcessExecutor = new ProcessExecutor(latexProcessBuilder);

			Thread executionThread = new Thread(latexProcessExecutor);

			long timeout = 20000;

			LOGGER.debug("Run xelatex thread started!");

			long startTime = System.currentTimeMillis();

			executionThread.start();

			int imod = 0;
			while (executionThread.isAlive()) {
				++imod;
				if (imod % 5 == 0) {
					LOGGER.debug("Run xelatex thread is alive");
				}

				if (((System.currentTimeMillis() - startTime) > timeout) && executionThread.isAlive()) {
					executionThread.interrupt();

					LOGGER.debug("Run xelatex thread interrupted!");

					latexProcessExecutor.killProcess();
				}
				Thread.sleep(100);
			}

			LOGGER.debug("Run xelatex done!");

			exitValue = latexProcessExecutor.getExitValue();

			try {
				Thread.sleep(1000);
			} catch (Exception ex) {
				LOGGER.error(ExceptionUtils.getStackTrace(ex), ex);
			}
		} catch (Exception ex) {
			LOGGER.error(ExceptionUtils.getStackTrace(ex), ex);
		}

		return exitValue;
	}

	/**
	 * This class executes in a separate Thread the xelatex process. This
	 * thread is controls by ProcessTimeoutThread.
	 *
	 * @author Bence
	 *
	 */
	private class ProcessExecutor implements Runnable {
		private Process theProcess;
		private ProcessBuilder processBuilder;
		private int exitValue = 0;

		public ProcessExecutor(ProcessBuilder processBuilder) {
			this.processBuilder = processBuilder;
		}

		@Override
		public void run() {

			try {
				theProcess = processBuilder.start();

				theProcess.waitFor();
				exitValue = theProcess.exitValue();
			} catch (Exception ex) {
				LOGGER.info(ex.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(ex), ex);
			}
		}


		public void killProcess() {
			try {
				if (theProcess != null) {
					theProcess.destroy();
					theProcess.waitFor();
					LOGGER.debug("Killing the process!");
				}
			} catch (InterruptedException e) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			}
		}

		public int getExitValue() {
			return exitValue;
		}

	}

}
