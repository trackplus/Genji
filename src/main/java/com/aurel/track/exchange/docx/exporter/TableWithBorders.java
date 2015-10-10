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

package com.aurel.track.exchange.docx.exporter;

import java.math.BigInteger;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;

import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.P.Hyperlink;
import org.docx4j.wml.STBorder;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblBorders;
import org.docx4j.wml.TblPr;
import org.docx4j.wml.Tc;
import org.docx4j.wml.TcPr;
import org.docx4j.wml.TcPrInner;
import org.docx4j.wml.Tr;

import com.aurel.track.Constants;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanLink;
import com.aurel.track.resources.LocalizeUtil;

public class TableWithBorders {
	
	//private static WordprocessingMLPackage  wordMLPackage;
	//private static ObjectFactory factory;

	/*public static void main (String[] args) throws Docx4JException {
		wordMLPackage = WordprocessingMLPackage.createPackage();
		//factory = Context.getWmlObjectFactory();

		Tbl table = createTableWithContent();

		addBorders(table);

		wordMLPackage.getMainDocumentPart().addObject(table);
		wordMLPackage.save(new java.io.File(
			"src/main/files/HelloWord5.docx") );
	}*/
	
	static void addLinkedItemsTable(List<ReportBean> reportBeansWithLinks, Locale locale, MainDocumentPart mainDocumentPart) {
		P linkedItemsP = mainDocumentPart.createParagraphOfText(LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.export.docx.linkedItems", locale));
		mainDocumentPart.addObject(linkedItemsP);
		Tbl table = createTableWithContent(reportBeansWithLinks, locale, mainDocumentPart);
		addBorders(table);
		mainDocumentPart.addObject(table);
	}
	
	/**
	 * Add border to table
	 * @param table
	 */
	private static void addBorders(Tbl table) {
		table.setTblPr(new TblPr());
		CTBorder border = new CTBorder();
		border.setColor("auto");
		border.setSz(new BigInteger("4"));
		border.setSpace(new BigInteger("0"));
		border.setVal(STBorder.SINGLE);
		TblBorders borders = new TblBorders();
		borders.setBottom(border);
		borders.setLeft(border);
		borders.setRight(border);
		borders.setTop(border);
		borders.setInsideH(border);
		borders.setInsideV(border);
		table.getTblPr().setTblBorders(borders);
	}

	private static Tbl createTableWithContent(List<ReportBean> reportBeansWithLinks, Locale locale, MainDocumentPart mainDocumentPart) {
		ObjectFactory factory = Context.getWmlObjectFactory();
		Tbl table = factory.createTbl();
		Tr tableHeaderRow = factory.createTr();
		addTableCell(tableHeaderRow, mainDocumentPart.createParagraphOfText(LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.export.docx.linkedFrom", locale)), factory);
		addTableCell(tableHeaderRow,  mainDocumentPart.createParagraphOfText(LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.export.docx.linkType", locale)), factory);
		addTableCell(tableHeaderRow, mainDocumentPart.createParagraphOfText(LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.export.docx.linkedItem", locale)), factory);
		table.getContent().add(tableHeaderRow);
		for (ReportBean reportBean : reportBeansWithLinks) {
			TWorkItemBean workItemBean = reportBean.getWorkItemBean();
			SortedSet<ReportBeanLink> reportBeanLinks = reportBean.getReportBeanLinksSet();
			boolean start = true;
			for (ReportBeanLink reportBeanLink : reportBeanLinks) {
				Tr tableRow = factory.createTr();
				P itemTitle = mainDocumentPart.createParagraphOfText(AssembleWordprocessingMLPackage.getItemNo(workItemBean) + workItemBean.getSynopsis());
				//addTableCell(tableRow, itemTitle, factory);
				if (start) {
					addTableCellWithMergeStart(tableRow, itemTitle, factory);
				} else {
					addTableCellWithMergeContinue(tableRow, factory);
				}
				start = false;
				//add 
				Integer linkedItemID = reportBeanLink.getWorkItemID();
				String linkedItemTitle = reportBeanLink.getLinkedItemTitle();
				String projectSpecificIssueNo = reportBeanLink.getProjectSpecificIssueNo();
				String issueNo = null;
				if (projectSpecificIssueNo==null) {
					if (linkedItemID!=null) {
						issueNo = linkedItemID.toString();
					}
				} else {
					issueNo = projectSpecificIssueNo;
				}
				if (issueNo!=null) {
					linkedItemTitle = AssembleWordprocessingMLPackage.getItemNo(issueNo) + linkedItemTitle;
				}
				boolean linkedItemIncluded = reportBeanLink.isLinkedItemIncluded();
				String linkTypeName = reportBeanLink.getLinkTypeName();
				P linkTypeP = mainDocumentPart.createParagraphOfText(linkTypeName);
				addTableCell(tableRow, linkTypeP, factory);
				Hyperlink link;
				if (linkedItemIncluded) {
					//linked item is included in this document: add bookmark 
					link = MainDocumentPart.hyperlinkToBookmark(AssembleWordprocessingMLPackage.ITEM_BOOKMARK_PREFIX+linkedItemID, linkedItemTitle);
				} else {
					//linked item is not included in this document: add hyperlink 
					link = HyperlinkUtil.createHyperlink(mainDocumentPart, Constants.getHyperLink()+linkedItemID, linkedItemTitle);
				}
				P linkedItemTitleP = Context.getWmlObjectFactory().createP();
				linkedItemTitleP.getContent().add(link);
				addTableCell(tableRow, linkedItemTitleP, factory);
				table.getContent().add(tableRow);
			}
		}
		return table;
	}

	/**
	 * Adds a table cell to a row
	 * @param tableRow
	 * @param content
	 * @param factory
	 */
	private static void addTableCell(Tr tableRow, P content, ObjectFactory factory) {
		Tc tableCell = factory.createTc();
		tableCell.getContent().add(content);
		tableRow.getContent().add(tableCell);
	}
	
	/**
	 * Adds a table cell to a row
	 * @param tableRow
	 * @param content
	 * @param factory
	 */
	private static void addTableCellWithMergeStart(Tr tableRow, P content, ObjectFactory factory) {
		Tc tc = factory.createTc();		
		TcPr tcpr = factory.createTcPr(); 
        tc.setTcPr(tcpr); 
        // Create object for vMerge
        TcPrInner.VMerge tcprinnervmerge = factory.createTcPrInnerVMerge(); 
        tcpr.setVMerge(tcprinnervmerge);
       	tcprinnervmerge.setVal( "restart");
       	tc.getContent().add(content);
		tableRow.getContent().add(tc);
	}
	
	/**
	 * Adds a table cell to a row
	 * @param tableRow
	 * @param content
	 * @param factory
	 */
	private static void addTableCellWithMergeContinue(Tr tableRow, ObjectFactory factory) {
		Tc tc = factory.createTc();		
		TcPr tcpr = factory.createTcPr(); 
        tc.setTcPr(tcpr); 
        // Create object for vMerge
        TcPrInner.VMerge tcprinnervmerge = factory.createTcPrInnerVMerge(); 
        tcpr.setVMerge(tcprinnervmerge);
    	P p = factory.createP(); 
    	tc.getContent().add( p); 
		tableRow.getContent().add(tc);
	}
	
	
}
