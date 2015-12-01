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

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dbase.HandleHome;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.itemNavigator.IssueListViewPlugin;
import com.aurel.track.itemNavigator.layout.LayoutFieldTO;
import com.aurel.track.itemNavigator.layout.LayoutTO;
import com.aurel.track.itemNavigator.layout.NavigatorLayoutBL;
import com.aurel.track.itemNavigator.layout.column.ColumnFieldTO;
import com.aurel.track.itemNavigator.layout.column.ColumnFieldsBL;
import com.aurel.track.itemNavigator.layout.group.GroupFieldTO;
import com.aurel.track.itemNavigator.layout.group.SortFieldTO;
import com.aurel.track.itemNavigator.viewPlugin.ViewDescriptorBL;
import com.aurel.track.itemNavigator.viewPlugin.ViewPluginBL;
import com.aurel.track.plugin.IssueListViewDescriptor;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.datasource.IPluggableDatasource.CONTEXT_ATTRIBUTE;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.numberFormatter.DoubleNumberFormatUtil;

import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignSortField;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.HorizontalImageAlignEnum;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.type.PositionTypeEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;
import net.sf.jasperreports.engine.type.WhenResourceMissingTypeEnum;

/**
 * Programmatically generating the JasperDesign object for the actual report
 * overview layout
 * 
 * @author Tamas
 * 
 */
public class ReportOverviewJasperDesign {

	private static final Logger LOGGER = LogManager.getLogger(ReportOverviewJasperDesign.class);

	// page sizes
	private static int pageWidth = 842;
	private static int pageHeight = 585;
	private static int leftMargin = 30;
	private static int rightMargin = 30;
	private static int topMargin = 20;
	private static int bottomMargin = 20;
	private static int columnWidth = pageWidth - leftMargin - rightMargin;

	// field paddings
	private static int groupTextFirstLeftPadding = 5;
	private static int groupTextLeftPaddingPace = 20;
	private static int imageTopPadding = 2;
	private static int imageBottomPadding = 2;
	private static int textLeftPadding = 3;
	private static int textRightPadding = 0;
	private static int issueOverdueIconWidth = 35;
	private static int budgetExpenseUnitWidth = 35;

	// band heights
	private static int detailBandHeight = 13;
	private static int groupBandHeight = 13;
	private static int pageHeaderBandHeight = 13;

	// font sizes
	private static float detailFontSize = 9;
	private static float groupFontSize = 9;
	private static float pageHeaderFontSize = 9;

	// scaling between the pdf pixels and the field width from reportLayoutBeans
	// (782/930)
	private static double scalingFactor = 0.85;

	/*
	 * the long fields width for grid (excel)
	 */
	private static int longFieldWidthGrid = 500;

	/*
	 * whether the long text will be plain (remove HTML tags)
	 */
	private static boolean longFieldIsPlain = true;

	/*
	 * whether to show the image fields in grid (excel)
	 */
	private static boolean showImageInGrid = false;

	/*
	 * whether to show all fields in grid, not just those selected in the
	 * current report overview
	 */
	private static boolean showAllFieldsInGrid = true;

	/**
	 * The logo in the title section
	 */
	private static String logo = "trackLogo.png";

	/*
	 * in some cases the fieldWidth is 0 (probably because of a report layout
	 * bug) in this case set it automatically to this value or width for fields
	 * not present in report layout (in case of grid)
	 */
	private static int defaultFieldWidth = 150;

	/*
	 * the total width of all columns for free form page content (pdf)
	 */
	private static int freeFormPageColumsWidth = 0;

	/*
	 * the total width of all columns for grid based layout (xls)
	 */
	private static int gridLayoutColumsWidth = 0;

	private static interface RESOURCE_KEYS {
		static String TITLE = "report.jasper.title";
		static String RIGHTS = "report.jasper.rights";
		static String PAGE = "report.jasper.page";
		static String TOTAL_NUMBER = "itemov.lbl.totalNumber";
	}

	private static interface STYLES {
		static String PAGE_HEADER_STYLE = "PageHeaderStyle";
		static String DETAIL_SHORT_STYLE = "DetailShortStyle";
		static String DETAIL_LONG_STYLE = "DetailLongStyle";
		static String GROUP_STYLE = "GroupStyle";
	}

	private static interface ICONS {
		static String WORK_TO_DO = "work-to-do.png";
		static String CALENDAR_OVERFLOW = "calendarOverflow.gif";
		static String BUDGET_OVERFLOW = "budgetOverflow.gif";
		static String CALENDAR_BUDGET_OVERFLOW = "calendarBudgetOverflow.gif";
		static String CHECKED_GREEN = "checkgreen.gif";
		static String CHECKED_RED = "checkred.gif";
		static String ARCHIVE = "archive.png";
		static String DELETE = "delete.png";
	}

	/**
	 * Create the JasperDesign object dynamically
	 * 
	 * @param personBean
	 * @param locale
	 * @param exportFormat
	 * @param contextMap
	 * @return
	 */
	static JasperDesign getJasperDesign(TPersonBean personBean, Locale locale, String exportFormat, Map<String, Object> contextMap) {
		loadSizesFromFile();
		boolean isExcel = ReportExporter.FORMAT_XLS.equals(exportFormat);
		boolean isCSV = ReportExporter.FORMAT_CSV.equals(exportFormat);
		boolean isGrid = isExcel || isCSV;
		// get the not exported fields
		Set<Integer> notExportedFields = getNotExportedFields();
		// get all ReportField's map
		// actual report layout
		Integer queryType =  (Integer) contextMap.get(CONTEXT_ATTRIBUTE.QUERY_TYPE);
		Integer queryID = (Integer) contextMap.get(CONTEXT_ATTRIBUTE.QUERY_ID);
		LayoutTO layoutTO = NavigatorLayoutBL.loadLayout(personBean, locale, queryType,	queryID, true);
		List<ColumnFieldTO> columnFields = layoutTO.getColumnFields();
		List<GroupFieldTO> groupFields = layoutTO.getGroupFields();	
		String viewID = NavigatorLayoutBL.getSavedItemFilterView(queryID, queryType);
		if (viewID==null || "".equals(viewID)) {
			viewID = personBean.getLastSelectedView();
			LOGGER.debug("Get the last used view by person: " + personBean.getLabel() + ": " +  viewID);
		}
		if (viewID!=null) {
			IssueListViewDescriptor issueListViewDescriptor = ViewDescriptorBL.getDescriptor(viewID);
			if (issueListViewDescriptor!=null) {
				IssueListViewPlugin plugin = ViewPluginBL.getPlugin(issueListViewDescriptor.getTheClassName());
				if (plugin!=null) {
					List<GroupFieldTO> pluginGroupFields = plugin.getGroupFieldBeans(locale);
					if (pluginGroupFields!=null) {
						groupFields = pluginGroupFields;
					}
				}
			}
		}
		if (isGrid && showAllFieldsInGrid) {
			// show all fields not only those from the actual layout
			Set<Integer> columnFieldIDsSet = new HashSet<Integer>();
			for (ColumnFieldTO layoutField : columnFields) {
				columnFieldIDsSet.add(layoutField.getFieldID());
			}
			// get all available fields, do not render the history fields
			List<ColumnFieldTO> layoutFieldsList = ColumnFieldsBL.loadAvailableColumnFields(personBean.getObjectID(), locale, true);
			for (ColumnFieldTO columnFieldTO : layoutFieldsList) {
				if (!columnFieldIDsSet.contains(columnFieldTO.getFieldID())) {
					// the field is not in the current report overview layout
					// we still add the short fields if showAllFieldsInGrid is
					// true
					if (!notExportedFields.contains(columnFieldTO.getFieldID()) && !columnFieldTO.isRenderContentAsImg() && !columnFieldTO.isRenderAsLong()) {
						// it is an exportable field, but not an image
						columnFields.add(columnFieldTO);
					}
				}
			}
		}
		if (longFieldIsPlain) {
			for (ColumnFieldTO columnFieldTO : columnFields) {
				Integer fieldID = columnFieldTO.getFieldID();
				if ((fieldID != null) && (fieldID.intValue() > 0)) {
					IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
					if ((fieldTypeRT != null) && fieldTypeRT.isLong()) {
						String fieldName = columnFieldTO.getName();
						columnFieldTO.setName(fieldName + ReportBeansToXML.PLAIN_SUFFIX);
					}
				}
			}
		}
		Set<Integer> budgetExpenseFields = getBudgetExpenseFields();
		int totalWidth = 0;
		if (columnFields != null) {
			Iterator<ColumnFieldTO> iterator = columnFields.iterator();
			while (iterator.hasNext()) {
				ColumnFieldTO layoutField = iterator.next();
				Integer fieldID = layoutField.getFieldID();
				Integer fieldWidth = layoutField.getFieldWidth();
				// get the width for each field
				if (layoutField.isRenderAsLong()) {
					// independently of the fieldWidth in layout the
					// long field will have a default value for long fields
					fieldWidth = Integer.valueOf(longFieldWidthGrid);
				} else {
					if ((fieldWidth == null) || (fieldWidth.intValue() == 0)) {
						// when no width is specified in the database
						// take a default value for short fields
						fieldWidth = Integer.valueOf(defaultFieldWidth);
					} else {
						// otherwise scale the fieldWidth from database to jasper pdf
						fieldWidth = new Integer(new Double(fieldWidth.intValue() * scalingFactor).intValue());
					}
				}
				layoutField.setFieldWidth(fieldWidth);
				if (notExportedFields.contains(fieldID)) {
					// neglect some fields and do not add the width to the total
					iterator.remove();
					continue;
				}
				if (layoutField.isRenderAsLong()) {
					// do not remove from the report layout list but increase to
					// the total width if excel
					if (isGrid) {
						totalWidth += layoutField.getFieldWidth().intValue();
					}
					continue;
				}
				if (!layoutField.isRenderContentAsImg() || !isGrid || showImageInGrid) {
					totalWidth += layoutField.getFieldWidth().intValue();
				}
				if (budgetExpenseFields.contains(fieldID)) {
					totalWidth += budgetExpenseUnitWidth;
				} else {
					if (SystemFields.INTEGER_ISSUENO.equals(fieldID) && (!isGrid || showImageInGrid)) {
						totalWidth += issueOverdueIconWidth;
					}
				}
				if (!isGrid && (totalWidth > columnWidth)) {
					// break out if pdf is not wide enough
					// remove the other columns as if they would not exist
					// by creating the page header and detail bands
					iterator.remove();
				} else {
					
					freeFormPageColumsWidth = totalWidth;
				}
			}
			gridLayoutColumsWidth = totalWidth;
		}

		JasperDesign jasperDesign = new JasperDesign();
		jasperDesign.setName("ReportOverview");
		if (isGrid) {
			jasperDesign.setPageWidth(gridLayoutColumsWidth);
			jasperDesign.setColumnWidth(gridLayoutColumsWidth);
			jasperDesign.setColumnSpacing(0);
			jasperDesign.setLeftMargin(0);
			jasperDesign.setRightMargin(0);
			jasperDesign.setTopMargin(0);
			jasperDesign.setBottomMargin(0);
			jasperDesign.setIgnorePagination(true);
		} else {
			jasperDesign.setPageWidth(pageWidth);
			jasperDesign.setPageHeight(pageHeight);
			jasperDesign.setColumnWidth(columnWidth);
			jasperDesign.setColumnSpacing(0);
			jasperDesign.setLeftMargin(leftMargin);
			jasperDesign.setRightMargin(rightMargin);
			jasperDesign.setTopMargin(topMargin);
			jasperDesign.setBottomMargin(bottomMargin);
			jasperDesign.setIgnorePagination(false);
		}
		jasperDesign.setWhenResourceMissingType(WhenResourceMissingTypeEnum.KEY);
		addJRDesignStyles(jasperDesign, isGrid);
		addParameters(jasperDesign);
		// add the report fields
		List<LayoutFieldTO> layoutFields = new LinkedList<LayoutFieldTO>();
		if (columnFields != null) {
			layoutFields.addAll(columnFields);
		}
		if (groupFields != null) {
			for (LayoutFieldTO groupFieldTO : groupFields) {
				Integer groupFieldID = groupFieldTO.getFieldID();
				boolean found = false;
				for (LayoutFieldTO layoutFieldTO : columnFields) {
					Integer columnFieldID = layoutFieldTO.getFieldID();
					if (EqualUtils.equal(groupFieldID, columnFieldID)) {
						found = true;
						break;
					}
				}
				if (!found) {
					//add group field only if not in layout as column already to avoid adding the same field twice
					layoutFields.add(groupFieldTO);
				}
			}
		}
		addFields(layoutFields, jasperDesign, isGrid);
		// add the groups, group variables and group bands
		addGroupsWithVariablesAndBands(groupFields, locale, jasperDesign, isGrid);
		// add the other bands
		addBands(columnFields, jasperDesign, isGrid, locale);
		// the data source should be already sorted because sorting them here in report would destroy the item hierarchy
		// set report query
		JRDesignQuery query = new JRDesignQuery();
		query.setLanguage("xPath");
		query.setText("/track-report/item/.");
		jasperDesign.setQuery(query);
		return jasperDesign;
	}

	/**
	 * Add a sort order field to jasperDesign
	 * 
	 * @param jasperDesign
	 * @param sortField
	 * @param sortOrder
	 */
	private static void addSortOrder(JasperDesign jasperDesign, SortFieldTO sortField) {
		if (sortField != null) {
			// not a pseudo field
			// the name of the field from XML to sort by
			String sortFieldName = sortField.getName();
			if (ReportBeansToXML.hasExtraSortField(sortField.getFieldID())) {
				sortFieldName = sortFieldName + TReportLayoutBean.PSEUDO_COLUMN_NAMES.ORDER;
			}
			SortOrderEnum sortOrderEnum = null;
			if (sortField.isDescending()) {
				sortOrderEnum = SortOrderEnum.DESCENDING;
			} else {
				sortOrderEnum = SortOrderEnum.ASCENDING;
			}
			try {
				jasperDesign.addSortField(new JRDesignSortField(sortFieldName, SortFieldTypeEnum.FIELD, sortOrderEnum));
			} catch (JRException e) {
				LOGGER.warn("Adding the sort field " + sortFieldName + " for sorting failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
	}

	/**
	 * Loads the sizes from a property file
	 * 
	 * @return
	 */
	private static void loadSizesFromFile() {
		PropertiesConfiguration properties= null;
		try {
			properties = HandleHome.getProperties(HandleHome.PDF_EXCEL_EXPORT_FILE, ApplicationBean.getInstance().getServletContext());
		} catch (ServletException e) {
			LOGGER.info("Getting the " + HandleHome.PDF_EXCEL_EXPORT_FILE + " configutartions failed with " + e.getMessage());
		}
		try {
			pageWidth = properties.getInt("pageWidth", 842);
		} catch (Exception e) {
			LOGGER.warn("Loading the pageWidth failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			pageHeight = properties.getInt("pageHeight", 585);
		} catch (Exception e) {
			LOGGER.warn("Loading the pageHeight failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			leftMargin = properties.getInt("leftMargin", 30);
		} catch (Exception e) {
			LOGGER.warn("Loading the leftMargin failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			rightMargin = properties.getInt("rightMargin", 30);
		} catch (Exception e) {
			LOGGER.warn("Loading the rightMargin failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		columnWidth = pageWidth - leftMargin - rightMargin;
		try {
			topMargin = properties.getInt("topMargin", 20);
		} catch (Exception e) {
			LOGGER.warn("Loading the topMargin failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			bottomMargin = properties.getInt("bottomMargin", 20);
		} catch (Exception e) {
			LOGGER.warn("Loading the bottomMargin failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}

		try {
			groupTextFirstLeftPadding = properties.getInt("groupTextFirstLeftPadding", 5);
		} catch (Exception e) {
			LOGGER.warn("Loading the groupTextFirstLeftPadding failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			groupTextLeftPaddingPace = properties.getInt("groupTextLeftPaddingPace", 20);
		} catch (Exception e) {
			LOGGER.warn("Loading the groupTextLeftPaddingPace failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			imageTopPadding = properties.getInt("imageTopPadding", 2);
		} catch (Exception e) {
			LOGGER.warn("Loading the imageTopPadding failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			imageBottomPadding = properties.getInt("imageBottomPadding", 2);
		} catch (Exception e) {
			LOGGER.warn("Loading the imageBottomPadding failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			textLeftPadding = properties.getInt("textLeftPadding", 3);
		} catch (Exception e) {
			LOGGER.warn("Loading the textLeftPadding failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			textRightPadding = properties.getInt("textRightPadding", 0);
		} catch (Exception e) {
			LOGGER.warn("Loading the textRightPadding failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}

		try {
			issueOverdueIconWidth = properties.getInt("issueOverdueIconWidth", 35);
		} catch (Exception e) {
			LOGGER.warn("Loading the issueOverdueIconWidth failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			budgetExpenseUnitWidth = properties.getInt("budgetExpenseUnitWidth", 35);
		} catch (Exception e) {
			LOGGER.warn("Loading the budgetExpenseUnitWidth failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}

		try {
			detailBandHeight = properties.getInt("detailBandHeight", 13);
		} catch (Exception e) {
			LOGGER.warn("Loading the detailBandHeight failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			groupBandHeight = properties.getInt("groupBandHeight", 13);
		} catch (Exception e) {
			LOGGER.warn("Loading the groupBandHeight failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			pageHeaderBandHeight = properties.getInt("pageHeaderBandHeight", 13);
		} catch (Exception e) {
			LOGGER.warn("Loading the pageHeaderBandHeight failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			detailFontSize = properties.getInt("detailFontSize", 9);
		} catch (Exception e) {
			LOGGER.warn("Loading the detailFontSize failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			groupFontSize = properties.getInt("groupFontSize", 9);
		} catch (Exception e) {
			LOGGER.warn("Loading the groupFontSize failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			pageHeaderFontSize = properties.getInt("pageHeaderFontSize", 9);
		} catch (Exception e) {
			LOGGER.warn("Loading the pageHeaderFontSize failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}

		try {
			scalingFactor = properties.getDouble("scalingFactor", 0.85);
		} catch (Exception e) {
			LOGGER.warn("Loading the scalingFactor failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			longFieldWidthGrid = properties.getInt("longFieldWidthGrid", 500);
		} catch (Exception e) {
			LOGGER.warn("Loading the longFieldWidthGrid failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			longFieldIsPlain = properties.getBoolean("longFieldIsPlain", true);
		} catch (Exception e) {
			LOGGER.warn("Loading the longFieldIsPlain failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			defaultFieldWidth = properties.getInt("defaultFieldWidth", 150);
		} catch (Exception e) {
			LOGGER.warn("Loading the defaultFieldWidth failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			showImageInGrid = properties.getBoolean("showImageInGrid", false);
		} catch (Exception e) {
			LOGGER.warn("Loading the showImageInGrid failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			showAllFieldsInGrid = properties.getBoolean("showAllFieldsInGrid", false);
		} catch (Exception e) {
			LOGGER.warn("Loading the showAllFieldsInGrid failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			logo = properties.getString("logo", "trackLogo.png").trim();
		} catch (Exception e) {
			LOGGER.warn("Loading the logo failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			
		}
	}

	/**
	 * The set of fields whose export is not (yet) implemented
	 * 
	 * @return
	 */
	private static Set<Integer> getNotExportedFields() {
		Set<Integer> notExportedFields = new HashSet<Integer>();
		notExportedFields.add(TReportLayoutBean.PSEUDO_COLUMNS.CHECKBOX_FIELD_ID);
		notExportedFields.add(TReportLayoutBean.PSEUDO_COLUMNS.INDEX_NUMBER);
		// TODO remove them from this list as soon as they will be implemented
		notExportedFields.add(TReportLayoutBean.PSEUDO_COLUMNS.HISTORY_LIST);
		notExportedFields.add(TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_HISTORY_LIST);
		notExportedFields.add(TReportLayoutBean.PSEUDO_COLUMNS.PLAN_HISTORY_LIST);
		notExportedFields.add(TReportLayoutBean.PSEUDO_COLUMNS.COST_LIST);
		notExportedFields.add(TReportLayoutBean.PSEUDO_COLUMNS.ATTACHMENT_SYMBOL);
		return notExportedFields;
	}

	/**
	 * Get the set of expense/budget fields whose values are not direct (value
	 * and unit)
	 * 
	 * @return
	 */
	private static Set<Integer> getBudgetExpenseFields() {
		Set<Integer> budgetExpenseFields = new HashSet<Integer>();
		budgetExpenseFields.add(TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_COST);
		budgetExpenseFields.add(TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_TIME);
		budgetExpenseFields.add(TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_COST);
		budgetExpenseFields.add(TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_TIME);
		budgetExpenseFields.add(TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_COST);
		budgetExpenseFields.add(TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME);
		budgetExpenseFields.add(TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_COST);
		budgetExpenseFields.add(TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_TIME);
		budgetExpenseFields.add(TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_COST);
		budgetExpenseFields.add(TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_TIME);
		return budgetExpenseFields;
	}

	private static int getValueType(Integer fieldID) {
		if (fieldID.intValue() > 0) {
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			if (fieldTypeRT != null) {
				return fieldTypeRT.getValueType();
			}
		}
		return ValueType.NOSTORE;
	}

	/**
	 * Get the value class for a field set for fields
	 * 
	 * @param valueType
	 * @return
	 */
	private static Class getValueClass(int valueType) {
		// get the class for types which need special formatting patterns
		switch (valueType) {
		case ValueType.INTEGER:
			return Integer.class;
		case ValueType.DOUBLE:
			return Double.class;
		case ValueType.DATE:
		case ValueType.DATETIME:
			return Date.class;
		default:
			return String.class;
		}

	}

	/**
	 * Add the parameters to jasperDesign
	 * 
	 * @param jasperDesign
	 */
	private static void addParameters(JasperDesign jasperDesign) {
		JRDesignParameter parameter;
		parameter = new JRDesignParameter();
		parameter.setName(JasperReportExporter.REPORT_PARAMETERS.BASE_URL);
		parameter.setValueClass(java.lang.String.class);
		try {
			jasperDesign.addParameter(parameter);
		} catch (JRException e) {
			LOGGER.error("Adding the parameter BASE_URL to jasperDesign failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		parameter = new JRDesignParameter();
		parameter.setName(JasperReportExporter.REPORT_PARAMETERS.DYNAMIC_ICONS_URL);
		parameter.setValueClass(java.lang.String.class);
		try {
			jasperDesign.addParameter(parameter);
		} catch (JRException e) {
			LOGGER.error("Adding the parameter DYNAMIC_ICONS_URL to jasperDesign failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		parameter = new JRDesignParameter();
		parameter.setName(JasperReportExporter.REPORT_PARAMETERS.LOGO_FOLDER_URL);
		parameter.setValueClass(java.lang.String.class);
		try {
			jasperDesign.addParameter(parameter);
		} catch (JRException e) {
			LOGGER.error("Adding the parameter LOGO_FOLDER to jasperDesign failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Add all used fields to jasperDesign
	 * @param layoutFields
	 * @param jasperDesign
	 * @param isGrid
	 */
	private static void addFields(List<LayoutFieldTO> layoutFields, JasperDesign jasperDesign, boolean isGrid) {
		Set<Integer> budgetExpenseFields = getBudgetExpenseFields();
		for (LayoutFieldTO layoutField : layoutFields) {
			Integer fieldID = layoutField.getFieldID();
			String fieldName = layoutField.getName();
			JRDesignField jsDesignfield = new JRDesignField();
			jsDesignfield.setName(fieldName);
			jsDesignfield.setValueClass(getValueClass(getValueType(fieldID)));
			if (budgetExpenseFields.contains(fieldID)) {
				jsDesignfield.setDescription(fieldName + "/" + ReportBeansToXML.BUDGET_EXPENSE_VALUE);
				jsDesignfield.setValueClass(Double.class);
				// creates an auxiliary field for unit
				JRDesignField unitField = new JRDesignField();
				unitField.setName(fieldName + "Unit");
				unitField.setValueClass(String.class);
				unitField.setDescription(fieldName + "/" + ReportBeansToXML.BUDGET_EXPENSE_UNIT);
				try {
					jasperDesign.addField(unitField);
				} catch (JRException e) {
					LOGGER.error("Adding the unit field for " + fieldID + " to jasperDesign failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			} else {
				jsDesignfield.setDescription(fieldName);
			}
			try {
				jasperDesign.addField(jsDesignfield);
			} catch (JRException e) {
				LOGGER.error("Adding the field " + fieldID + " to jasperDesign failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			if (ReportBeansToXML.hasExtraSortField(fieldID)) {
				JRDesignField jsDesignFieldSortOrder = new JRDesignField();
				String sortOrderFieldName = fieldName + TReportLayoutBean.PSEUDO_COLUMN_NAMES.ORDER;
				jsDesignFieldSortOrder.setName(sortOrderFieldName);
				jsDesignFieldSortOrder.setValueClass(Integer.class);
				jsDesignFieldSortOrder.setDescription(sortOrderFieldName);
				try {
					jasperDesign.addField(jsDesignFieldSortOrder);
				} catch (JRException e) {
					LOGGER.error("Adding the sort field for " + fieldID + " to jasperDesign failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
		// additional fields on due plan, on budget plan: for issueNumber status
		// image but also for conditional style
		// committed date conflict
		JRDesignField jsDesignField = new JRDesignField();
		jsDesignField.setName(TReportLayoutBean.PSEUDO_COLUMN_NAMES.COMMITTED_DATE_CONFLICT);
		jsDesignField.setValueClass(Boolean.class);
		jsDesignField.setDescription(TReportLayoutBean.PSEUDO_COLUMN_NAMES.COMMITTED_DATE_CONFLICT);
		try {
			jasperDesign.addField(jsDesignField);
		} catch (JRException e) {
			LOGGER.error("Adding the field " + TReportLayoutBean.PSEUDO_COLUMN_NAMES.COMMITTED_DATE_CONFLICT + " to jasperDesign failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		// target date conflict
		jsDesignField = new JRDesignField();
		jsDesignField.setName(TReportLayoutBean.PSEUDO_COLUMN_NAMES.TARGET_DATE_CONFLICT);
		jsDesignField.setValueClass(Boolean.class);
		jsDesignField.setDescription(TReportLayoutBean.PSEUDO_COLUMN_NAMES.TARGET_DATE_CONFLICT);
		try {
			jasperDesign.addField(jsDesignField);
		} catch (JRException e) {
			LOGGER.error("Adding the field " + TReportLayoutBean.PSEUDO_COLUMN_NAMES.TARGET_DATE_CONFLICT + " to jasperDesign failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		// planned value conflict
		jsDesignField = new JRDesignField();
		jsDesignField.setName(TReportLayoutBean.PSEUDO_COLUMN_NAMES.PLANNED_VALUE_CONFLICT);
		jsDesignField.setValueClass(Boolean.class);
		jsDesignField.setDescription(TReportLayoutBean.PSEUDO_COLUMN_NAMES.PLANNED_VALUE_CONFLICT);
		try {
			jasperDesign.addField(jsDesignField);
		} catch (JRException e) {
			LOGGER.error("Adding the field " + TReportLayoutBean.PSEUDO_COLUMN_NAMES.PLANNED_VALUE_CONFLICT + " to jasperDesign failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		// budget conflict
		jsDesignField = new JRDesignField();
		jsDesignField.setName(TReportLayoutBean.PSEUDO_COLUMN_NAMES.BUDGET_CONFLICT);
		jsDesignField.setValueClass(Boolean.class);
		jsDesignField.setDescription(TReportLayoutBean.PSEUDO_COLUMN_NAMES.BUDGET_CONFLICT);
		try {
			jasperDesign.addField(jsDesignField);
		} catch (JRException e) {
			LOGGER.error("Adding the field " + TReportLayoutBean.PSEUDO_COLUMN_NAMES.BUDGET_CONFLICT + " to jasperDesign failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		// status flag
		jsDesignField = new JRDesignField();
		jsDesignField.setName(TReportLayoutBean.PSEUDO_COLUMN_NAMES.STATUS_FLAG);
		jsDesignField.setValueClass(Integer.class);
		jsDesignField.setDescription(TReportLayoutBean.PSEUDO_COLUMN_NAMES.STATUS_FLAG);
		try {
			jasperDesign.addField(jsDesignField);
		} catch (JRException e) {
			LOGGER.error("Adding the field " + TReportLayoutBean.PSEUDO_COLUMN_NAMES.STATUS_FLAG + " to jasperDesign failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		// archived flag
		jsDesignField = new JRDesignField();
		jsDesignField.setName(TReportLayoutBean.PSEUDO_COLUMN_NAMES.ARCHIVE_LEVEL);
		jsDesignField.setValueClass(Integer.class);
		jsDesignField.setDescription(TReportLayoutBean.PSEUDO_COLUMN_NAMES.ARCHIVE_LEVEL);
		try {
			jasperDesign.addField(jsDesignField);
		} catch (JRException e) {
			LOGGER.error("Adding the field " + TReportLayoutBean.PSEUDO_COLUMN_NAMES.ARCHIVE_LEVEL + " to jasperDesign failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		// if issueNumber is present we need also indent level
		// indent level
		jsDesignField = new JRDesignField();
		jsDesignField.setName(TReportLayoutBean.PSEUDO_COLUMN_NAMES.INDENT_LEVEL);
		jsDesignField.setValueClass(Integer.class);
		jsDesignField.setDescription(TReportLayoutBean.PSEUDO_COLUMN_NAMES.INDENT_LEVEL);
		try {
			jasperDesign.addField(jsDesignField);
		} catch (JRException e) {
			LOGGER.error("Adding the field " + TReportLayoutBean.PSEUDO_COLUMN_NAMES.INDENT_LEVEL + " to jasperDesign failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Add the groups and the group bands
	 * 
	 * @param groupFieldBeanList
	 * @param locale
	 * @param jasperDesign
	 * @param layoutFieldsMap
	 * @param isGrid
	 */
	private static void addGroupsWithVariablesAndBands(List<GroupFieldTO> groupFields, Locale locale, 
			              JasperDesign jasperDesign, boolean isGrid) {
		JRDesignExpression expression;
		if (groupFields != null) {
			int groupPadding = groupTextFirstLeftPadding;
			for (GroupFieldTO groupField : groupFields) {
				JRDesignGroup group = new JRDesignGroup();
				String fieldName = groupField.getName();
				String groupName = groupField.getLabel();
				group.setName(fieldName);
				group.setMinHeightToStartNewPage(60);
				// add sort fields for each group level, otherwise the groups
				// are not rendered correctly
				addSortOrder(jasperDesign, groupField);
				expression = new JRDesignExpression();
				expression.setText("$F{" + fieldName + "}");
				group.setExpression(expression);
				/* add the band */
				JRDesignBand band = new JRDesignBand();
				band.setHeight(groupBandHeight);
				JRDesignTextField textField = new JRDesignTextField();
				textField.setX(0);
				textField.setY(0);
				// left padding
				textField.getLineBox().setLeftPadding(groupPadding);
				textField.setWidth(freeFormPageColumsWidth - 1);
				textField.setHeight(groupBandHeight);
				textField.setMode(ModeEnum.OPAQUE);
				textField.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
				expression = new JRDesignExpression();
				expression.setText("\"" + groupName + ": \" + " + "$F{" + fieldName + "} + \" (\" + $V{" + fieldName + "_COUNT}" + " + \")\"");
				textField.setExpression(expression);
				textField.setEvaluationTime(EvaluationTimeEnum.GROUP);
				textField.setEvaluationGroup(group);
				textField.setStyle(jasperDesign.getStylesMap().get(STYLES.GROUP_STYLE));
				band.addElement(textField);
				if (!isGrid) {
					JRDesignLine line;
					// left vertical line
					line = new JRDesignLine();
					line.setX(0);
					line.setY(0);
					line.setWidth(0);
					line.setHeight(groupBandHeight);
					band.addElement(line);
					// right vertical line
					line = new JRDesignLine();
					line.setX(freeFormPageColumsWidth - 1);
					line.setY(0);
					line.setWidth(0);
					line.setHeight(groupBandHeight);
					band.addElement(line);

					// bottom horizontal line
					line = new JRDesignLine();
					line.setX(0);
					line.setY(groupBandHeight - 1);
					line.setWidth(freeFormPageColumsWidth - 1);
					line.setHeight(1);
					band.addElement(line);
				}
				((JRDesignSection) group.getGroupHeaderSection()).addBand(band);
				groupPadding += groupTextLeftPaddingPace;
				try {
					jasperDesign.addGroup(group);
				} catch (JRException e) {
					LOGGER.error("Adding the group " + fieldName + " failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
	}

	/**
	 * Build the title band
	 * 
	 * @param jasperDesign
	 * @return
	 */
	private static JRDesignBand buildTitleBand(JasperDesign jasperDesign) {
		JRDesignBand band = new JRDesignBand();
		band.setHeight(60);
		int logoWidth = 147;
		JRDesignTextField textField;
		textField = new JRDesignTextField();
		textField.setBlankWhenNull(true);
		textField.setX(250);
		textField.setY(5);
		textField.setWidth(250);
		textField.setHeight(30);
		textField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
		JRDesignExpression expression;
		expression = new JRDesignExpression();
		expression.setText("$R{" + RESOURCE_KEYS.TITLE + "}");
		textField.setExpression(expression);
		band.addElement(textField);
		JRDesignImage jDesignImage = new JRDesignImage(jasperDesign);
		jDesignImage.setX(635);
		jDesignImage.setY(0);
		jDesignImage.setWidth(logoWidth);
		jDesignImage.setHeight(50);
		jDesignImage.setScaleImage(ScaleImageEnum.RETAIN_SHAPE);
		JRDesignExpression jRDesignExpression = new JRDesignExpression();
		jRDesignExpression.setText("$P{" + JasperReportExporter.REPORT_PARAMETERS.LOGO_FOLDER_URL + "}+\"" + logo + "\"");
		jDesignImage.setExpression(jRDesignExpression);
		jDesignImage.setOnErrorType(OnErrorTypeEnum.BLANK);
		textField = new JRDesignTextField();
		textField.setX(0);
		textField.setY(35);
		textField.setWidth(300);
		textField.setHeight(25);
		expression = new JRDesignExpression();
		expression.setText("$R{" + RESOURCE_KEYS.TOTAL_NUMBER + "} + \" \" + $V{REPORT_COUNT}");
		textField.setExpression(expression);
		textField.setEvaluationTime(EvaluationTimeEnum.REPORT);
		band.addElement(textField);
		band.addElement(jDesignImage);
		return band;
	}

	/**
	 * Build the page header band
	 * 
	 * @param columnFields
	 * @return
	 */
	private static JRDesignBand buildPageHeaderBand(List<ColumnFieldTO> columnFields, JasperDesign jasperDesign, boolean isGrid) {
		JRDesignBand band = new JRDesignBand();
		band.setHeight(pageHeaderBandHeight);
		Set<Integer> budgetExpenseFields = getBudgetExpenseFields();
		if (columnFields != null) {
			Iterator<ColumnFieldTO> iterator = columnFields.iterator();
			int widthSum = 0;
			while (iterator.hasNext()) {
				ColumnFieldTO columnField = iterator.next();
				Integer fieldID = columnField.getFieldID();
				// no header for long fields in pdf, but in excel we need also
				// for long fields
				if (columnField.isRenderAsLong() && !isGrid) {
					continue;
				}
				int width = columnField.getFieldWidth().intValue();
				if (width == 0) {
					width = ColumnFieldsBL.DEFAULT_WIDTHS.TEXT_DEFAULT_WIDTH;
				}
				JRDesignElement jRDesignElement;
				JRDesignStaticText jRDesignElementUnit = null;
				if (columnField.isRenderHeaderAsImg()) {
					if (!isGrid || showImageInGrid) {
						JRDesignImage jDesignImage = new JRDesignImage(jasperDesign);
						jRDesignElement = jDesignImage;
						jDesignImage.setHorizontalImageAlign(HorizontalImageAlignEnum.CENTER);
						jDesignImage.setScaleImage(ScaleImageEnum.RETAIN_SHAPE);
						jDesignImage.getLineBox().setTopPadding(imageTopPadding);
						jDesignImage.getLineBox().setBottomPadding(imageBottomPadding);
						JRDesignExpression expression = new JRDesignExpression();
						expression.setText("$P{BASE_URL}+" + "\"" + columnField.getHeaderImgName() + "\"");
						jDesignImage.setExpression(expression);
						jDesignImage.setOnErrorType(OnErrorTypeEnum.BLANK);
					} else {
						continue;
					}
				} else {
					JRDesignStaticText staticText = new JRDesignStaticText();
					jRDesignElement = staticText;
					staticText.getLineBox().setLeftPadding(textLeftPadding);
					staticText.getLineBox().setRightPadding(textRightPadding);
					staticText.setText(columnField.getLabel());					
					if (budgetExpenseFields.contains(fieldID) || (SystemFields.INTEGER_ISSUENO.equals(fieldID) && (!isGrid || showImageInGrid))) {
						jRDesignElementUnit = new JRDesignStaticText();
						String companionColumnText;
						int companionColumnWidth;
						if (budgetExpenseFields.contains(fieldID)) {
							companionColumnText = "Unit";
							companionColumnWidth = budgetExpenseUnitWidth;
						} else {
							companionColumnText = "Icon";
							companionColumnWidth = issueOverdueIconWidth;
						}
						jRDesignElementUnit.setText(companionColumnText);
						jRDesignElementUnit.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
						jRDesignElementUnit.setWidth(companionColumnWidth);
						jRDesignElementUnit.setHeight(pageHeaderBandHeight);
						jRDesignElementUnit.setStyle(jasperDesign.getStylesMap().get(STYLES.PAGE_HEADER_STYLE));
					}
				}
				jRDesignElement.setX(widthSum);
				jRDesignElement.setWidth(width);
				jRDesignElement.setHeight(pageHeaderBandHeight);
				jRDesignElement.setStyle(jasperDesign.getStylesMap().get(STYLES.PAGE_HEADER_STYLE));
				band.addElement(jRDesignElement);
				if (!isGrid) {
					// vertical lines
					JRDesignLine line;
					line = new JRDesignLine();
					line.setX(widthSum);
					line.setY(0);
					line.setWidth(0);
					line.setHeight(pageHeaderBandHeight);
					line.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
					band.addElement(line);
				}

				widthSum += width;
				if (jRDesignElementUnit != null) {
					jRDesignElementUnit.setX(widthSum);
					widthSum += budgetExpenseUnitWidth;
					band.addElement(jRDesignElementUnit);
				}
			}
			if (!isGrid) {
				JRDesignLine line;
				// last vertical line
				line = new JRDesignLine();
				line.setX(widthSum - 1);
				line.setY(0);
				line.setWidth(0);
				line.setHeight(pageHeaderBandHeight);
				line.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
				band.addElement(line);

				// top horizontal line
				line = new JRDesignLine();
				line.setX(0);
				line.setY(0);
				line.setWidth(widthSum - 1);
				line.setHeight(0);
				band.addElement(line);

				// bottom horizontal line
				line = new JRDesignLine();
				line.setX(0);
				line.setY(pageHeaderBandHeight - 1);
				line.setWidth(widthSum - 1);
				line.setHeight(0);
				band.addElement(line);
			}
		}
		return band;
	}

	/**
	 * Build the page footer band
	 * 
	 * @return
	 */
	private static JRDesignBand buildPageFooterBand() {
		JRDesignBand band = new JRDesignBand();
		band.setHeight(20);
		JRDesignTextField rightText = new JRDesignTextField();
		rightText.setY(0);
		rightText.setWidth(300);
		rightText.setHeight(15);
		rightText.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
		JRDesignExpression rightExpression = new JRDesignExpression();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		rightExpression.setText("\" (C) " + calendar.get(Calendar.YEAR) + " \" + $R{" +
		ApplicationBean.getInstance().getLicenseHolder() + " - " + RESOURCE_KEYS.RIGHTS + "}");
		rightText.setExpression(rightExpression);
		band.addElement(rightText);
		JRDesignTextField pageText = new JRDesignTextField();
		pageText.setX(720);
		pageText.setY(0);
		pageText.setWidth(60);
		pageText.setHeight(15);
		pageText.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
		JRDesignExpression pageExpression = new JRDesignExpression();
		pageExpression.setText("$R{" + RESOURCE_KEYS.PAGE + "} + \" \" + $V{PAGE_NUMBER}");
		pageText.setExpression(pageExpression);
		band.addElement(pageText);
		return band;
	}

	/**
	 * Build the detail band
	 * 
	 * @param layoutFields
	 * @return
	 */
	private static JRDesignBand buildDetailBand(List<ColumnFieldTO> layoutFields, JasperDesign jasperDesign, boolean isGrid, Locale locale) {
		JRDesignBand band = new JRDesignBand();
		band.setHeight(detailBandHeight);
		JRDesignElementGroup jDesignElementGroupShortFields = new JRDesignElementGroup();
		JRDesignElementGroup jDesignElementGroupLongFields = new JRDesignElementGroup();
		int widthSum = 0;
		Set<Integer> budgetExpenseFields = getBudgetExpenseFields();
		if (layoutFields != null) {
			Iterator<ColumnFieldTO> iterator = layoutFields.iterator();
			while (iterator.hasNext()) {
				ColumnFieldTO layoutField = iterator.next();
				int width = layoutField.getFieldWidth().intValue();
				if (width == 0) {
					width = ColumnFieldsBL.DEFAULT_WIDTHS.TEXT_DEFAULT_WIDTH;
				}
				Integer fieldID = layoutField.getFieldID();
				String fieldName = layoutField.getName();
				if (!isGrid && !layoutField.isRenderAsLong()) {
					JRDesignLine line;
					line = new JRDesignLine();
					line.setX(widthSum);
					line.setY(0);
					line.setWidth(0);
					line.setHeight(detailBandHeight);
					line.setPositionType(PositionTypeEnum.FLOAT);
					if (widthSum == 0) { // first vertical border line on the
											// very left
						line.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
						band.addElement(line);
					} else { // subsequent vertical lines
						line.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
						jDesignElementGroupShortFields.addElement(line);
					}
				}
				// field expression
				JRDesignExpression expression;
				if (layoutField.isRenderContentAsImg()) {
					// render as image
					if (!isGrid || showImageInGrid) {
						JRDesignImage jDesignImage = new JRDesignImage(jasperDesign);
						jDesignImage.setX(widthSum);
						jDesignImage.setY(0);
						jDesignImage.setWidth(width);
						jDesignImage.setHeight(detailBandHeight);
						jDesignImage.getLineBox().setTopPadding(imageTopPadding);
						jDesignImage.getLineBox().setBottomPadding(imageBottomPadding);
						jDesignImage.setHorizontalImageAlign(HorizontalImageAlignEnum.CENTER);
						jDesignImage.setScaleImage(ScaleImageEnum.RETAIN_SHAPE);
						expression = new JRDesignExpression();
						// the icon name is always text
						expression.setText("$P{" + JasperReportExporter.REPORT_PARAMETERS.DYNAMIC_ICONS_URL + "}" + "+" + "$F{" + fieldName + "}");
						jDesignImage.setExpression(expression);
						jDesignImage.setOnErrorType(OnErrorTypeEnum.BLANK);
						jDesignElementGroupShortFields.addElement(jDesignImage);
						// if image add width to withSum
						widthSum += width;
					}
				} else {
					JRDesignTextField textField = new JRDesignTextField();
					expression = new JRDesignExpression();
					int valueType = getValueType(fieldID);
					if (layoutField.isRenderAsLong()) {
						// render as long text
						if ((fieldID != null) && (TReportLayoutBean.PSEUDO_COLUMNS.HISTORY_LIST == fieldID.intValue())) {
							// history begins at 0
							textField.setX(0);
							textField.setY(detailBandHeight);
							// and stretches over the entire available width
							textField.setWidth(columnWidth);
							textField.setStretchWithOverflow(true);
							textField.setMarkup(JRCommonText.MARKUP_STYLED_TEXT);
						} else {
							int labelLength = 0;
							ColumnFieldTO firstLayoutField = layoutFields.get(0);
							if ((firstLayoutField != null) && (firstLayoutField.getFieldWidth() != null)) {
								labelLength = firstLayoutField.getFieldWidth().intValue();
							}
							if (labelLength == 0) {
								labelLength = ColumnFieldsBL.DEFAULT_WIDTHS.TEXT_DEFAULT_WIDTH;
							}

							if (!isGrid) {
								textField.setX(1);
								textField.setWidth(freeFormPageColumsWidth - 2);
								jDesignElementGroupLongFields.addElement(textField);
								textField.setY(detailBandHeight);
							} else {
								textField.setX(widthSum);
								textField.setY(0);
								textField.setHeight(detailBandHeight);
								textField.setWidth(longFieldWidthGrid);
								widthSum += longFieldWidthGrid;
								jDesignElementGroupShortFields.addElement(textField);
							}
							textField.setStretchWithOverflow(true);
							textField.setMarkup(JRCommonText.MARKUP_STYLED_TEXT);
							textField.setPositionType(PositionTypeEnum.FLOAT);
							textField.setStyle(jasperDesign.getStylesMap().get(STYLES.DETAIL_LONG_STYLE));
						}
						// if long text do not add the width because it is
						// renderer in a separate row
					} else {
						// render as short text
						textField.setX(widthSum);
						textField.setY(0);
						textField.setWidth(width);
						textField.setHeight(detailBandHeight);
						textField.setStretchWithOverflow(true);
						textField.setStyle(jasperDesign.getStylesMap().get(STYLES.DETAIL_SHORT_STYLE));
						jDesignElementGroupShortFields.addElement(textField);
						// if image add width to withSum
						widthSum += width;
						switch (valueType) {
						case ValueType.DATE:
							textField.setPattern(DateTimeUtils.getInstance().getGUIDatePattern(locale));
							break;
						case ValueType.DATETIME:
							textField.setPattern(DateTimeUtils.getInstance().getGUIDateTimePattern(locale));
							break;
						case ValueType.DOUBLE:
							textField.setPattern(DoubleNumberFormatUtil.getInstance().getGUINumberattern(locale));
							break;
						}
					}

					if ((fieldID != null) && (fieldID.intValue() == SystemFields.ISSUENO)) {
						// the issueNo should be indented
						expression.setText("new String(new char[]"
								+ "{' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' '}, 0, " + "$F{"
								+ TReportLayoutBean.PSEUDO_COLUMN_NAMES.INDENT_LEVEL + "}.intValue()*2)" + " + $F{" + fieldName + "}");
						// status images
						textField.setWidth(width);
						if (!isGrid || showImageInGrid) {
							JRDesignImage jDesignImage = new JRDesignImage(jasperDesign);
							jDesignImage.setX(widthSum);
							widthSum += issueOverdueIconWidth;
							jDesignImage.setY(0);
							jDesignImage.setWidth(issueOverdueIconWidth);
							jDesignImage.setHeight(detailBandHeight);
							jDesignImage.setHorizontalImageAlign(HorizontalImageAlignEnum.CENTER);
							jDesignImage.getLineBox().setTopPadding(imageTopPadding);
							jDesignImage.getLineBox().setBottomPadding(imageBottomPadding);
							jDesignImage.setScaleImage(ScaleImageEnum.RETAIN_SHAPE);
							JRDesignExpression imageExpression = new JRDesignExpression();
							// the icon name is always text
							String committedDateConflict = TReportLayoutBean.PSEUDO_COLUMN_NAMES.COMMITTED_DATE_CONFLICT;
							String targetDateConflict = TReportLayoutBean.PSEUDO_COLUMN_NAMES.TARGET_DATE_CONFLICT;
							String plannedValueConflict = TReportLayoutBean.PSEUDO_COLUMN_NAMES.PLANNED_VALUE_CONFLICT;
							String bugdetConflict = TReportLayoutBean.PSEUDO_COLUMN_NAMES.BUDGET_CONFLICT;

							imageExpression.setText("$P{BASE_URL} + " + "($F{ArchiveLevel}==null || $F{ArchiveLevel}.intValue()=="
									+ TWorkItemBean.ARCHIVE_LEVEL_UNARCHIVED.intValue() + "? " + "($F{StateFlag}.intValue()!=" + TStateBean.STATEFLAGS.CLOSED
									+ " ? " + "(" + "(" + getConflictExpression(committedDateConflict, targetDateConflict, false) + " && "
									+ getConflictExpression(plannedValueConflict, bugdetConflict, false) + " ? \"" + ICONS.WORK_TO_DO + "\" :" + "("
									+ getConflictExpression(committedDateConflict, targetDateConflict, true) + " && "
									+ getConflictExpression(plannedValueConflict, bugdetConflict, false) + " ? \"" + ICONS.CALENDAR_OVERFLOW + "\" : " + "("
									+ getConflictExpression(committedDateConflict, targetDateConflict, false) + " && "
									+ getConflictExpression(plannedValueConflict, bugdetConflict, true) + " ?" + "\"" + ICONS.BUDGET_OVERFLOW + "\" : \""
									+ ICONS.CALENDAR_BUDGET_OVERFLOW + "\")))" + ")" + ":" + "("
									+ getConflictExpression(committedDateConflict, targetDateConflict, false) + " && "
									+ getConflictExpression(plannedValueConflict, bugdetConflict, false) + " ? " + "\"" + ICONS.CHECKED_GREEN + "\" : \""
									+ ICONS.CHECKED_RED + "\"))" + " : " + "($F{ArchiveLevel}.intValue()==" + TWorkItemBean.ARCHIVE_LEVEL_ARCHIVED.intValue()
									+ " ? \"" + ICONS.ARCHIVE + "\" : \"" + ICONS.DELETE + "\"" + "))");
							jDesignImage.setExpression(imageExpression);
							jDesignImage.setOnErrorType(OnErrorTypeEnum.BLANK);
							jDesignElementGroupShortFields.addElement(jDesignImage);
						}
					} else {
						if (budgetExpenseFields.contains(fieldID)) {
							JRDesignTextField unitTextField = new JRDesignTextField(jasperDesign);
							unitTextField.setX(widthSum);
							widthSum += budgetExpenseUnitWidth;
							unitTextField.setY(0);
							unitTextField.setWidth(budgetExpenseUnitWidth);
							unitTextField.setHeight(detailBandHeight);
							unitTextField.setStyle(jasperDesign.getStylesMap().get(STYLES.DETAIL_SHORT_STYLE));
							JRDesignExpression unitExpression = new JRDesignExpression();
							unitExpression.setText("$F{" + fieldName + "Unit}");
							unitTextField.setExpression(unitExpression);
							unitTextField.setBlankWhenNull(true);
							unitTextField.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
							unitTextField.getLineBox().setRightPadding(2);
							jDesignElementGroupShortFields.addElement(unitTextField);
						}
						expression.setText("$F{" + fieldName + "}");
					}
					textField.getLineBox().setLeftPadding(textLeftPadding);
					textField.getLineBox().setRightPadding(textRightPadding);
					textField.setExpression(expression);
					textField.setBlankWhenNull(true);
				}
			}
			if (!isGrid) {
				JRDesignLine line;
				line = new JRDesignLine();
				line.setX(widthSum - 1);
				line.setY(0);
				line.setWidth(0);
				line.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
				line.setHeight(detailBandHeight - 1);
				line.setPositionType(PositionTypeEnum.FLOAT);
				band.addElement(line);
			}
			band.addElementGroup(jDesignElementGroupShortFields);
			if (!jDesignElementGroupLongFields.getChildren().isEmpty()) {
				band.addElementGroup(jDesignElementGroupLongFields);
			}
		}
		if (!isGrid) {
			JRDesignLine line;
			line = new JRDesignLine();
			line.setX(0);
			line.setY(detailBandHeight - 1);
			line.setWidth(widthSum);
			line.setHeight(0);
			line.setPositionType(PositionTypeEnum.FLOAT);
			band.addElement(line);
		}
		return band;
	}

	/**
	 * Gets the conflict expression for a committed (planned) value and a target
	 * (budget) value
	 * 
	 * @param commitedConflict
	 * @param targetConflict
	 * @param conflict
	 * @return
	 */
	private static String getConflictExpression(String commitedConflict, String targetConflict, boolean conflict) {
		if (conflict) {
			return "($F{" + commitedConflict + "}.booleanValue()==true || $F{" + targetConflict + "}.booleanValue()==true)";

		} else {
			return "($F{" + commitedConflict + "}.booleanValue()==false && $F{" + targetConflict + "}.booleanValue()==false)";
		}
	}

	private static void addBands(List<ColumnFieldTO> columnFields, JasperDesign jasperDesign, boolean isGrid, Locale locale) {
		JRDesignBand band;
		// Title
		if (!isGrid) {
			band = buildTitleBand(jasperDesign);
			jasperDesign.setTitle(band);
		}
		// Page header
		band = buildPageHeaderBand(columnFields, jasperDesign, isGrid);
		jasperDesign.setPageHeader(band);
		// Column header
		// Detail
		band = buildDetailBand(columnFields, jasperDesign, isGrid, locale);
		((JRDesignSection) jasperDesign.getDetailSection()).addBand(band);
		// Column footer
		// Page footer
		if (!isGrid) {
			band = buildPageFooterBand();
			jasperDesign.setPageFooter(band);
		}
		// Summary
	}

	private static void addJRDesignStyles(JasperDesign jasperDesign, boolean isGrid) {
		try {
			JRDesignStyle detailShortStyle = new JRDesignStyle();
			detailShortStyle.setName(STYLES.DETAIL_SHORT_STYLE);
			detailShortStyle.setFontSize(detailFontSize);
			detailShortStyle.setForecolor(new Color(0x39, 0x3B, 0x40));
			JRDesignConditionalStyle conditionalStyle = new JRDesignConditionalStyle();
			conditionalStyle.setForecolor(new Color(0xBF, 0x21, 0x28));
			JRDesignExpression jDesignExpression = new JRDesignExpression();
			String committedDateConflict = TReportLayoutBean.PSEUDO_COLUMN_NAMES.COMMITTED_DATE_CONFLICT;
			String targetDateConflict = TReportLayoutBean.PSEUDO_COLUMN_NAMES.TARGET_DATE_CONFLICT;
			String plannedValueConflict = TReportLayoutBean.PSEUDO_COLUMN_NAMES.PLANNED_VALUE_CONFLICT;
			String bugdetConflict = TReportLayoutBean.PSEUDO_COLUMN_NAMES.BUDGET_CONFLICT;
			String statusFlag = TReportLayoutBean.PSEUDO_COLUMN_NAMES.STATUS_FLAG;
			String archiveLevel = TReportLayoutBean.PSEUDO_COLUMN_NAMES.ARCHIVE_LEVEL;
			jDesignExpression.setText("new Boolean((" + getConflictExpression(committedDateConflict, targetDateConflict, true) + " || "
					+ getConflictExpression(plannedValueConflict, bugdetConflict, true) + ") && $F{" + statusFlag + "}.intValue()!="
					+ TStateBean.STATEFLAGS.CLOSED + " && ($F{" + archiveLevel + "}==null || " + "$F{" + archiveLevel + "}.intValue()=="
					+ TWorkItemBean.ARCHIVE_LEVEL_UNARCHIVED.intValue() + "))");
			conditionalStyle.setConditionExpression(jDesignExpression);
			detailShortStyle.addConditionalStyle(conditionalStyle);
			jasperDesign.addStyle(detailShortStyle);

			JRDesignStyle detailLongStyle = new JRDesignStyle();
			detailLongStyle.setName(STYLES.DETAIL_LONG_STYLE);
			detailLongStyle.setFontSize(detailFontSize);
			jasperDesign.addStyle(detailLongStyle);

			JRDesignStyle groupStyle = new JRDesignStyle();
			groupStyle.setName(STYLES.GROUP_STYLE);
			groupStyle.setFontSize(groupFontSize);
			if (!isGrid) {
				groupStyle.getLinePen().setLineWidth(1);
				groupStyle.setBackcolor(new Color(0x99, 0x99, 0x99));
				groupStyle.setMode(ModeEnum.OPAQUE);
			}
			jasperDesign.addStyle(groupStyle);

			JRDesignStyle headerStyle = new JRDesignStyle();
			headerStyle.setName(STYLES.PAGE_HEADER_STYLE);
			headerStyle.setFontSize(pageHeaderFontSize);
			headerStyle.setBold(true);
			if (!isGrid) {
				headerStyle.setBackcolor(new Color(0xCC, 0xCC, 0xCC));
				headerStyle.setMode(ModeEnum.OPAQUE);
			}
			jasperDesign.addStyle(headerStyle);
		} catch (Exception e) {
			LOGGER.error("Adding the JRDesignStyles failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
	}
}
