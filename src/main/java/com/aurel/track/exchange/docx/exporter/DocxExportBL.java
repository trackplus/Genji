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

package com.aurel.track.exchange.docx.exporter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FieldExpressionSimpleTO;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.itemNavigator.ItemNavigatorBL;
import com.aurel.track.itemNavigator.QueryContext;
import com.aurel.track.linkType.ILinkType;
import com.aurel.track.linkType.ILinkType.PARENT_CHILD_EXPRESSION;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.util.DownloadUtil;
import com.aurel.track.util.GeneralUtils;

/**
 * Logic for exporting a docx file
 * @author Tamas
 *
 */
public class DocxExportBL {

	private static final Logger LOGGER = LogManager.getLogger(DocxExportBL.class);
	
	/**
	 * Gets the last executed filter result
	 * @param personBean
	 * @param locale
	 * @return
	 */
	static ReportBeans getReportBeans(String workItemIDs, TPersonBean personBean, Locale locale) {
		List<Integer> selectedItemIDs = null;
		if (workItemIDs!=null) {
			selectedItemIDs = GeneralUtils.createIntegerListFromStringArr(workItemIDs.split(","));
		}
		if (selectedItemIDs!=null && !selectedItemIDs.isEmpty()) {
			List<ReportBean> reportBeanList = LoadItemIDListItems.getReportBeansByWorkItemIDs(GeneralUtils.createIntArrFromIntegerList(selectedItemIDs), false,
					personBean.getObjectID(), locale, true, true, true, true, true, true, false, true, false);
			return new ReportBeans(reportBeanList, locale, null, true);
		}
		QueryContext queryContext=ItemNavigatorBL.loadLastQuery(personBean.getObjectID(),locale);
		if (queryContext!=null){
			return ItemNavigatorBL.executeQuery(personBean, locale, queryContext, null, false);
		}
		return null;
	}
	
	/**
	 * Gets the report beans for a document
	 * @param workItemBean
	 * @param personID
	 * @param locale
	 * @return
	 */
	public static ReportBeans getReportBeansForDocument(TWorkItemBean workItemBean, TPersonBean personBean, Locale locale) {
		return getReportBeansForDocument(workItemBean, personBean, locale,true);

	}
	public static ReportBeans getReportBeansForDocument(TWorkItemBean workItemBean, TPersonBean personBean, Locale locale,boolean includeSections) {
		if (workItemBean==null) {
			LOGGER.warn("Item not found");
			return new ReportBeans(new LinkedList<ReportBean>(), locale);
		}
		Integer workItemID = workItemBean.getObjectID();
		FilterUpperTO filterUpperTO = new FilterUpperTO();
		if (workItemBean!=null) {
			filterUpperTO.setSelectedProjects(new Integer[] {workItemBean.getProjectID()});
		}
		filterUpperTO.setLinkTypeFilterSuperset(MergeUtil.mergeKey(ILinkType.PARENT_CHILD, PARENT_CHILD_EXPRESSION.ALL_NOT_CLOSED_CHILDREN));
		List<FieldExpressionSimpleTO> fieldExpressionSimpleList = new ArrayList<FieldExpressionSimpleTO>(1);
		FieldExpressionSimpleTO fieldExpressionSimpleTO = new FieldExpressionSimpleTO();
		fieldExpressionSimpleTO.setField(SystemFields.INTEGER_SUPERIORWORKITEM);
		fieldExpressionSimpleTO.setSelectedMatcher(MatchRelations.EQUAL);
		fieldExpressionSimpleTO.setValue(workItemID);
		fieldExpressionSimpleList.add(fieldExpressionSimpleTO);
		filterUpperTO.setFieldExpressionSimpleList(fieldExpressionSimpleList);

		List<TListTypeBean> documentIssueTypes = IssueTypeBL.loadByTypeFlag(TListTypeBean.TYPEFLAGS.DOCUMENT);
		List<TListTypeBean> issueTypes=new ArrayList<TListTypeBean>();
		issueTypes.addAll(documentIssueTypes);
		if(includeSections){
			List<TListTypeBean> documentSectionsIssueTypes = IssueTypeBL.loadByTypeFlag(TListTypeBean.TYPEFLAGS.DOCUMENT_SECTION);
			issueTypes.addAll(documentSectionsIssueTypes);
		} else {
			List<TListTypeBean> folderIssueTypes = IssueTypeBL.loadByTypeFlag(TListTypeBean.TYPEFLAGS.DOCUMENT_FOLDER);
			issueTypes.addAll(folderIssueTypes);
			filterUpperTO.setItemTypeIDsForLinkType(GeneralUtils.createIntegerListFromBeanList(issueTypes));
		}
		filterUpperTO.setSelectedIssueTypes(GeneralUtils.createIntegerArrFromCollection(GeneralUtils.createIntegerListFromBeanList(issueTypes)));

		List<ReportBean> reportBeanList = null;
		try {
			reportBeanList = LoadTreeFilterItems.getTreeFilterReportBeans(filterUpperTO, null, null, false, personBean, locale, true, true, false, false, false, false, false, true, false);
		} catch (TooManyItemsToLoadException e) {
			LOGGER.info("Number of items to load " + e.getItemCount());
		}
		return new ReportBeans(reportBeanList, locale);
	}
	
	/**
	 * Serializes the docx content into the response's output stream
	 * @param response
	 * @param wordMLPackage
	 * @return
	 */
	static String prepareReportResponse(HttpServletResponse response, String baseFileName , WordprocessingMLPackage wordMLPackage) {
		if (baseFileName==null) {
			baseFileName = "TrackReport";
		} else {
			baseFileName = StringEscapeUtils.unescapeHtml4(baseFileName);
			StringBuffer sb = new StringBuffer(baseFileName.replaceAll("[^a-zA-Z0-9_]", "_"));
			Pattern regex = Pattern.compile("(_[a-z])");
			Matcher regexMatcher = regex.matcher(sb);
			StringBuffer sb2 = new StringBuffer();
	
			try {
				while (regexMatcher.find()) {
					regexMatcher.appendReplacement(sb2, regexMatcher.group(1).toUpperCase());
				}
				regexMatcher.appendTail(sb2);
	
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
			baseFileName = sb2.toString().replaceAll("_", "");
		}
		response.reset();
		response.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		response.setHeader("Content-Disposition", "attachment; filename=\""+baseFileName+".docx\"");
		DownloadUtil.prepareCacheControlHeader(ServletActionContext.getRequest(), response);
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
		} catch (IOException e) {
			LOGGER.error("Getting the output stream failed with " + e.getMessage());
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}	
		try {
			Docx4J.save(wordMLPackage, outputStream, Docx4J.FLAG_NONE);
			/*SaveToZipFile saver = new SaveToZipFile(wordMLPackage);
			saver.save(outputStream);*/
		} catch (Exception e) {
			LOGGER.error("Exporting the docx failed with throwable " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	/**
	 * Save the last selected docxTemplateFile in the user's profile
	 * @param personID
	 * @param docxTemplateFileName
	 */
	static void setDocxLastTemplate(Integer personID, String docxTemplateFileName) {
		TPersonBean personBean = PersonBL.loadByPrimaryKey(personID);
		if (personBean!=null && docxTemplateFileName!=null) {
			personBean.setDocxLastTemplate(docxTemplateFileName);
			PersonBL.saveSimple(personBean);
		}
	}
}
