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

import java.io.File;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.report.execute.ReportBeans;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.aurel.track.exchange.latex.exporter.LaTeXExportBL;

/**
 * Action class for exporting a docx file
 * @author Tamas
 *
 */
public class DocxExportAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware  {
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private TPersonBean personBean;
	private Locale locale;
	/**
	 * the selected document from wiki
	 */
	private Integer workItemID;
	/**
	 * row selections from issue navigator 
	 */
	private String workItemIDs;
	private String templateFileName;
	
	@Override
	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale = (Locale) session.get(Constants.LOCALE_KEY);
	}
	
	/**
	 * Generates the docx into the servlet response's output stream
	 */
	@Override
	public String execute() {
		ReportBeans reportBeans = null;
		TWorkItemBean workItemBean = null;
		String baseFileName = null;
		if (workItemID==null) {
			//from item navigator
			reportBeans = DocxExportBL.getReportBeans(workItemIDs, personBean, locale);
		} else {
			try {
				workItemBean = ItemBL.loadWorkItem(workItemID);
				if (workItemBean!=null) {
					baseFileName = workItemBean.getSynopsis();
				}
			} catch (ItemLoaderException e) {
			}
			//from wiki
			reportBeans = DocxExportBL.getReportBeansForDocument(workItemBean, personBean, locale);
		}
		
		if (templateFileName!=null) {
			DocxExportBL.setDocxLastTemplate(personBean.getObjectID(), templateFileName);
		}
		
		String templateFilePath = null;
		if (templateFileName.endsWith(".docx")) {
			templateFilePath = DocxTemplateBL.getWordTemplatesDir() + templateFileName;
			DocxExportBL.prepareReportResponse(servletResponse, baseFileName, AssembleWordprocessingMLPackage.getWordMLPackage(workItemBean, reportBeans, templateFilePath, personBean.getObjectID(), locale));
		} 
		
		else if (templateFileName.endsWith(".tex")) {
			templateFilePath = DocxTemplateBL.getLatexTemplatesDir() + templateFileName;
			LaTeXExportBL.prepareReportResponse(servletResponse, workItemBean, reportBeans, personBean, locale, DocxTemplateBL.getWordTemplatesDir(), templateFilePath);
		}
		
		else if (templateFileName.endsWith("tlx")) {
			String templateBaseName = templateFileName.substring(0, templateFileName.lastIndexOf(".tlx"));
			String templateDir = DocxTemplateBL.getLatexTemplatesDir()+File.separator+templateBaseName;
			LaTeXExportBL.prepareReportResponse(servletResponse, workItemBean, reportBeans, personBean, locale, templateDir, templateDir+File.separator+templateBaseName+".tex");
		}
		return null;
	}
	
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}

	public void setWorkItemIDs(String workItemIDs) {
		this.workItemIDs = workItemIDs;
	}

	public void setDocxTemplateFileName(String docxTemplateFileName) {
		this.templateFileName = docxTemplateFileName;
	}

	
	
}
