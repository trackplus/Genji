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

package com.aurel.track.admin.customize.category.report;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.CategoryBL;
import com.aurel.track.admin.customize.category.CategoryGridRowTO;
import com.aurel.track.admin.customize.category.CategoryNodeTO;
import com.aurel.track.admin.customize.category.CategoryTO;
import com.aurel.track.admin.customize.category.CategoryTokens;
import com.aurel.track.admin.customize.category.LeafFacade;
import com.aurel.track.admin.customize.category.report.execute.IDescriptionAttributes;
import com.aurel.track.admin.customize.category.report.execute.ReportExecuteBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ILocalizedLabelBean;
import com.aurel.track.beans.TExportTemplateBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ExportTemplateDAO;
import com.aurel.track.plugin.DatasourceDescriptor;
import com.aurel.track.report.datasource.DatasourceDescriptorUtil;
import com.aurel.track.report.datasource.IPluggableDatasource;
import com.aurel.track.resources.LocalizeUtil;

/**
 * An implementation of CategoryFacade for report categories
 * @author Tamas
 *
 */
public class ReportFacade extends LeafFacade {
	
	 private static final Logger LOGGER = LogManager.getLogger(ReportFacade.class);
	
	static interface ICONS {
		static String PDF = "pdf-ticon";
		static String EXCEL = "xls-ticon";
	}

	static interface EXPORT_FORMATS {
		static String PDF = "pdf";
		static String EXCEL = "xls";
	}
		
	private static ExportTemplateDAO exportTemplateDAO = DAOFactory.getFactory().getExportTemplateDAO();	
	private static ReportFacade instance;
	
	private ReportFacade() {
		super();
	}
	
	/**
	 * Get a singleton instance
	 * @return
	 */
	public static ReportFacade getInstance(){
		if(instance==null){
			instance=new ReportFacade();
		}
		return instance;
	}
	
	/**
	 * Gets the label key for this type
	 */
	@Override
	public String getLabelKey() {
		return "admin.customize.reportTemplate.lbl";
	}
	
	/**
	 * Get the human readable format of the field expression
	 * @param filterExpression
	 */	
	@Override
	public String getFilterExpressionString(Integer objectID, Locale locale) {
		return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.list.lbl.selectBranch", locale);
	}
		
	/**
	 * Add leaf nodes made from leaf beans
	 * @param repository
	 * @param leafBeans
	 * @param personID
	 * @param modifiable
	 * @param tree
	 * @param projectID
	 * @param locale
	 * @param nodes 
	 */
	@Override
	public void addLeafs(Integer repository, List<ILabelBean> leafBeans, Integer personID,
			boolean modifiable, boolean tree, Integer projectID, Locale locale, List<CategoryTO> nodes) {
		if (leafBeans!=null) {
			for (TExportTemplateBean exportTemplateBean : (List<TExportTemplateBean>)(List)leafBeans) {
				CategoryTokens categoryTokens = new CategoryTokens(CategoryBL.CATEGORY_TYPE.REPORT_CATEGORY,
						repository, Integer.valueOf(CategoryBL.TYPE.LEAF), exportTemplateBean.getObjectID());
				CategoryTO categoryTO = null;
				if (tree) {
					categoryTO = new CategoryNodeTO(CategoryTokens.encodeNode(categoryTokens),
							CategoryBL.CATEGORY_TYPE.REPORT_CATEGORY, exportTemplateBean.getName(), modifiable, modifiable, true, true);					
					((CategoryNodeTO)categoryTO).setIconCls(getIconCls(exportTemplateBean));
				} else {
					categoryTO = new CategoryGridRowTO(CategoryTokens.encodeNode(categoryTokens), CategoryBL.CATEGORY_TYPE.REPORT_CATEGORY,
							exportTemplateBean.getName(), exportTemplateBean.getExportFormat(), modifiable, true);
					categoryTO.setIconCls(getIconCls(exportTemplateBean));
					Map<String, Object> reportDescriptionMap = ReportExecuteBL.getTemplateMap(exportTemplateBean.getObjectID());
					String icon = (String) reportDescriptionMap.get(IDescriptionAttributes.ICON_GIF);
					File template = ReportBL.getDirTemplate(exportTemplateBean.getObjectID());
					if (icon!=null && !"".equals(icon)) {
						((CategoryGridRowTO)categoryTO).setReportIcon(template.getAbsolutePath() + File.separator + icon);
					}
				}
				categoryTO.setReportConfigNeeded(exportTemplateBean.isConfigNeeded());
				nodes.add(categoryTO);
			}
		}
	}
	
	/**
	 * Get the icon for a leaf
	 * @param labelBean
	 * @return
	 */
	@Override
	public String getIconCls(ILabelBean labelBean) {
		String exportFormat = ((TExportTemplateBean)labelBean).getExportFormat();
		String iconName = null;
		if (exportFormat!=null) {
			if (EXPORT_FORMATS.PDF.equals(exportFormat)) {
				iconName = ICONS.PDF;
			} else {
				if (EXPORT_FORMATS.EXCEL.equals(exportFormat)) {
					iconName = ICONS.EXCEL;
				}
			}
		}		
		return iconName;
	}
		
	
	/**
	 * Gets the root categories/leafs
	 * @param repository
	 * @param projectID
	 * @param personID
	 * @param locale
	 * @return
	 */
	@Override
	public List<ILabelBean> getRootObjects(Integer repository, Integer projectID, Integer personID, Locale locale) {		
		List<ILabelBean> rootReports = (List)exportTemplateDAO.loadRootReports(repository, projectID, personID);
		//TODO do we need internal templates?
		return LocalizeUtil.localizeDropDownList(rootReports, locale);
	}
	
	/**
	 * Gets the root categories/leafs by projects
	 * @param projectIDs
	 * @param locale
	 * @return
	 */
	@Override
	public List<ILabelBean> getRootObjectsByProjects(List<Integer> projectIDs, Locale locale) {
		List<ILabelBean> projectRootReports = (List)exportTemplateDAO.loadProjectRootCategories(projectIDs);
		return LocalizeUtil.localizeDropDownList(projectRootReports, locale);
	}
	
	/**
	 * Get the reports with layouts in .jar files
	 * Those with the data source specified but no page attribute (internal template) will
	 * not be taken into account here because it belongs most probably in the external templates list  
	 * @return
	 */
	/*private static List<ILabelBean> getInternalTemplates(Integer repository, Integer projectID, Integer personID) {
		List<ILabelBean> internalDatasources = new LinkedList<ILabelBean>();
		List<DatasourceDescriptor> datasources = DatasourceDescriptorUtil.getDatasourceDescriptors();		
		if (datasources!=null) {
			for (DatasourceDescriptor datasourceDescriptor : datasources) {
				if (datasourceDescriptor.getPersonID()!=null) {
					if (datasourceDescriptor.getPersonID().equals(personID)) {
						//if person specified it is private, i.e. visible only for the owner
						LOGGER.debug("Private datasource descriptor " + datasourceDescriptor.toString() + " found for person " + personID);
					} else {
						continue;
					}
				}
				if (datasourceDescriptor.getProjectID()!=null) {
					if (projectID!=null &&  datasourceDescriptor.getProjectID().equals(projectID) && 
						AccessBeans.hasPersonRightInProjectForIssueType(personID, projectID, null, AccessFlagMigrationIndexes.READANYTASK, true)) {
					//if project specified, visible only for those who have read right on that project
					LOGGER.debug("Project specific datasource descriptor " + datasourceDescriptor.toString() + 
							" found for project " + projectID + datasourceDescriptor.getProjectID());
					} else {
						continue;
					}
				}
				if (datasourceDescriptor.getJsClass()!=null) {
					//add the data source to the list only if it has his own page attribute (internal rendering)
					//(otherwise it is probably only a data source, with the rendering template in the .zip file)
					//if (!fromReportOverview  || (fromReportOverview && datasourceDescriptor.getClass()==null)) {
						TExportTemplateBean exportTemplateBean = createInternal(datasourceDescriptor, personID);
						LOGGER.debug("Datasource descriptor " + datasourceDescriptor.toString() + " added to the renderable list");					
						internalDatasources.add(exportTemplateBean);
					//}					
				} else {
					LOGGER.debug("Datasource descriptor " + datasourceDescriptor.toString() + 
							" has no page attribute consequently was not added to the renderable list");
				}				
			}
		}
		return internalDatasources;
		
	}*/
	
	/**
	 * Create a new TExportTemplateBean for each data source found in the plug-ins
	 * The bean will have no correspondence in the database
	 * @param datasourceDescriptor
	 * @param locale
	 * @return
	 */
	/*private static TExportTemplateBean createInternal(DatasourceDescriptor datasourceDescriptor, 
			Integer personID) {
		TExportTemplateBean exportTemplateBean = new TExportTemplateBean();
		//the internal format is always html
		exportTemplateBean.setExportFormat("html"); //html.gif		
		exportTemplateBean.setModifiable(false); //the internal report is not modifiable through the web-interface
		boolean defaultDatasource = datasourceDescriptor.getTheClassName()==null; 
		if (!defaultDatasource) {
			IPluggableDatasource pluggableDatasouce = ReportExportBL.pluggableDatasouceFactory(datasourceDescriptor.getTheClassName());
			if (pluggableDatasouce!=null) {
				//if the data source serialization is implemented then make it available through a button
				exportTemplateBean.setSerializableDatasource(pluggableDatasouce.implementSerialization());
			} else {
				LOGGER.warn("The class " + datasourceDescriptor.getTheClassName() + 
						" specified in trackplus-plugin.xml for datasourcePluginID " +
						datasourceDescriptor.getId() + " can't be instanciated");
				exportTemplateBean.setMissingDatasource(true);
			}
		}
		exportTemplateBean.setName(datasourceDescriptor.getName());
		//the rendering engine type: information on the jsp 
		exportTemplateBean.setReportType("ftl");
		exportTemplateBean.setDatasourceID(datasourceDescriptor.getId());
		exportTemplateBean.setConfigNeeded(datasourceDescriptor.getJsConfigClass()!=null);
		//set the repository type: public if not specified otherwise
		if (datasourceDescriptor.getPersonID()!=null) {
			if (personID.equals(datasourceDescriptor.getPersonID())) {
				exportTemplateBean.setRepositoryType(new Integer(TExportTemplateBean.REPOSITORY_TYPE.PRIVATE));
				exportTemplateBean.setPerson(datasourceDescriptor.getPersonID());
			}
		} else {
			if (datasourceDescriptor.getProjectID()!=null) {
				exportTemplateBean.setRepositoryType(new Integer(TExportTemplateBean.REPOSITORY_TYPE.PROJECT));
				exportTemplateBean.setProject(datasourceDescriptor.getProjectID());
			} else {
				exportTemplateBean.setRepositoryType(new Integer(TExportTemplateBean.REPOSITORY_TYPE.PUBLIC));
			}
		}
		//create the description map also for the internal templates to show the big tool-tip
		Map descriptionMap = new HashMap();
		descriptionMap.put(IDescriptionAttributes.DESCRIPTION, datasourceDescriptor.getDescription());
		descriptionMap.put(IDescriptionAttributes.LISTING, datasourceDescriptor.getListing());
		descriptionMap.put(IDescriptionAttributes.PREVIEW_GIF, datasourceDescriptor.getPreviewImg());
		exportTemplateBean.setDescriptionMap(descriptionMap);		
		return exportTemplateBean;
	}*/
	
	/**
	 * Gets the leafs by parent
	 * @param parentCategoryID
	 * @param locale
	 * @return
	 */
	@Override
	public List<ILabelBean> getByParent(Integer parentCategoryID, Locale locale) {
		List<ILabelBean> categoryReports = (List)exportTemplateDAO.loadByCategory(parentCategoryID);
		return LocalizeUtil.localizeDropDownList(categoryReports, locale);
	}
	
	/**
	 * Gets the leafs by parents
	 * @param parentCategoryIDs
	 * @return
	 */
	@Override
	public List<ILabelBean> getByParents(List<Integer> parentCategoryIDs) {
		return (List)exportTemplateDAO.loadByCategories(parentCategoryIDs);
	}
	
	/**
	 * Filters those leafs which should be available also coming from an issueNavigator
	 * (uses only by report templates)
	 * @param leafLabelBeans
	 * @param fromIssueNavigator
	 * @return
	 */
	@Override
	public List<ILabelBean> filterFromIssueNavigator(List<ILabelBean> leafLabelBeans, boolean fromIssueNavigator) {
		for (Iterator<ILabelBean> iterator = leafLabelBeans.iterator(); iterator.hasNext();) {
			TExportTemplateBean exportTemplateBean = (TExportTemplateBean) iterator.next();
			File templateFile=ReportBL.getDirTemplate(exportTemplateBean.getObjectID());
			if (templateFile.exists()) {
				Map<String, Object> descriptionMap = ReportBL.getTemplateDescription(templateFile);
				if (descriptionMap==null || descriptionMap.isEmpty()) {
					LOGGER.info("The template with ID " + exportTemplateBean.getObjectID() + 
							" exists only in the database but the but the corresponding description.xml file is not found on the disk ");
					iterator.remove();
				} else {
					String format = (String) descriptionMap.get(IDescriptionAttributes.FORMAT);
					if (format!=null && !"".equals(format)) {
						//get the format directly from template, not from database because in database 
						//it could be out-dated if the format was changed manually in description.xml 
						exportTemplateBean.setExportFormat(format);
					}
					String datasourcePluginID = (String) descriptionMap.get(IDescriptionAttributes.DATASOURCEPLUGIN);
					if (datasourcePluginID==null) {
						//implicit datasource: last executed query
						exportTemplateBean.setConfigNeeded(!fromIssueNavigator);
					} else {
						//explicit datasource
						exportTemplateBean.setConfigNeeded(true);
						DatasourceDescriptor datasourceDescriptor = DatasourceDescriptorUtil.getDatasourceDescriptor(datasourcePluginID);
						if (datasourceDescriptor==null) {
							LOGGER.warn("The  datasourcePlugin " + datasourcePluginID +
										" specified in the template with ID " + exportTemplateBean.getObjectID() +
										" is not found among in the trackplus-plugin.xml file's valid datasource elements"); 
						} else {
							if (datasourceDescriptor.getTheClassName()==null) {
								LOGGER.warn("The  datasourcePlugin " + datasourcePluginID +
										" specified in the template with ID " + exportTemplateBean.getObjectID() +
										" has no class element");
							} else {
								IPluggableDatasource pluggableDatasouce = ReportExecuteBL.pluggableDatasouceFactory(datasourceDescriptor.getTheClassName());
								if (pluggableDatasouce!=null) {
									//if the data source serialization is implemented then make it available through a button
									//exportTemplateBean.setSerializableDatasource(pluggableDatasouce.implementSerialization());
								} else {
									LOGGER.warn("The class " + datasourceDescriptor.getTheClassName() + 
											" specified in trackplus-plugin.xml for datasourcePluginID " +
											datasourcePluginID + " can't be instantiated");
									continue;
								}
							}
							//set the datasourceID only if datasourceDescriptor was not null 
							//(the datasource element in trackplus-plugin.xml is valid)
                                                  }
					}
				}
			} else {
				LOGGER.info("The template with ID " + exportTemplateBean.getObjectID() + 
						" exists only in the database but the corresponding directory is not found on the disk");

					//if we are coming from a report overview then do not show 
					//the external templates with explicit data source
					iterator.remove();

				//remove the template if it exists only in the database, but the directory does not exists in the disk 
			}
		}
		return leafLabelBeans;
	}
	
	/**
	 * Creates a new leaf bean
	 */
	@Override
	public ILabelBean getNewBean() {
		return new TExportTemplateBean();
	}
	
	/**
	 * Gets the leaf (filter or report) by key
	 * @param objectID
	 * @return
	 */
	@Override
	public ILabelBean getByKey(Integer objectID) {
		return ReportBL.loadByPrimaryKey(objectID);
	}
	
	/**
	 * Loads a leaf by key and locale
	 * @param objectID
	 * @param locale
	 * @return
	 */
	@Override
	public ILabelBean getByKey(Integer objectID, Locale locale) {
		ILocalizedLabelBean localizedLabelBean = ReportBL.loadByPrimaryKey(objectID);
		localizedLabelBean.setLabel(LocalizeUtil.localizeDropDownEntry(localizedLabelBean, locale));
		return localizedLabelBean;
	}
	
	/**
	 * Gets the categories/leafs by label
	 * @param repository
	 * @param parentID
	 * @param projectID
	 * @param personID filter by only if set
	 * @param subtype
	 * @param label
	 * @return
	 */
	@Override
	public List<ILabelBean> getListByLabel(Integer repository, Integer parentID,
			Integer projectID, Integer personID, Integer subtype, String label) {
		return (List)exportTemplateDAO.loadByLabel(repository, parentID, projectID, personID, label);
	}
			
	/**
	 * Gets the project for a leaf (filter or repository)
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getProjectID(ILabelBean labelBean) {
		TExportTemplateBean exportTemplateBean = (TExportTemplateBean)labelBean;
		if (exportTemplateBean!=null) {
			return exportTemplateBean.getProject();
		} else {
			return null;
		}
	}
	
	/**
	 * Sets the project
	 * @param labelBean
	 * @return
	 */
	@Override
	public void setProjectID(ILabelBean labelBean, Integer projectID) {
		TExportTemplateBean exportTemplateBean = (TExportTemplateBean)labelBean;
		if (exportTemplateBean!=null) {
			exportTemplateBean.setProject(projectID);
		}
	}
	
	/**
	 * Gets the category for a leaf
	 * @param labelBean
	 * @return
	 */
	@Override
	public Integer getParentID(ILabelBean labelBean) {
		TExportTemplateBean exportTemplateBean = (TExportTemplateBean)labelBean;
		if (exportTemplateBean!=null) {
			return exportTemplateBean.getCategoryKey();
		}
		return null;
	}
	
	/**
	 * Sets the parent for a category
	 * @param labelBean
	 * @param parentID
	 * @return
	 */
	@Override
	public void setParentID(ILabelBean labelBean, Integer parentID) {
		TExportTemplateBean exportTemplateBean = (TExportTemplateBean)labelBean;
		if (exportTemplateBean!=null) {
			exportTemplateBean.setCategoryKey(parentID);
		}
	}
	
	/**
	 * Sets the repository
	 * @param labelBean
	 * @param repository
	 * @return
	 */
	@Override
	public void setRepository(ILabelBean labelBean, Integer repository) {
		TExportTemplateBean exportTemplateBean = (TExportTemplateBean)labelBean;
		if (exportTemplateBean!=null) {
			exportTemplateBean.setRepositoryType(repository);
		}
	}
	
	/**
	 * Sets the label for a category
	 * @param labelBean
	 * @param label
	 * @return
	 */
	@Override
	public void setLabel(ILabelBean labelBean, String label) {
		TExportTemplateBean exportTemplateBean = (TExportTemplateBean)labelBean;
		if (exportTemplateBean!=null) {
			exportTemplateBean.setName(label);
		}
	}

	/**
	 * Gets the person
	 * @param labelBean
	 * @param person
	 * @return
	 */
	@Override
	public Integer getCreatedBy(ILabelBean labelBean) {
		TExportTemplateBean exportTemplateBean = (TExportTemplateBean)labelBean;
		if (exportTemplateBean!=null) {
			return exportTemplateBean.getPerson();
		}
		return null;
	}
		
	/**
	 * Sets the person
	 * @param labelBean
	 * @param person
	 * @return
	 */
	@Override
	public void setCreatedBy(ILabelBean labelBean, Integer person) {
		TExportTemplateBean exportTemplateBean = (TExportTemplateBean)labelBean;
		if (exportTemplateBean!=null) {
			exportTemplateBean.setPerson(person);
		}
	}

	/**
	 * Saves a modified leaf 
	 * @param labelBean	
	 * @return
	 */
	@Override
	public Integer save(ILabelBean labelBean) {
		TExportTemplateBean exportTemplateBean = (TExportTemplateBean)labelBean;
		if (exportTemplateBean!=null) {
			return ReportBL.saveReport(exportTemplateBean);
		}
		return null;
	}	
		
	/**
	 * Copy specific data if copy (not cut)
	 * @param labelBeanFrom
	 * @param labelBeanTo
	 */
	@Override
	public void copySpecific(ILabelBean labelBeanFrom, ILabelBean labelBeanTo) {
		TExportTemplateBean exportTemplateBeanFrom = (TExportTemplateBean)labelBeanFrom;
		TExportTemplateBean exportTemplateBeanTo = (TExportTemplateBean)labelBeanTo;
		if (exportTemplateBeanFrom!=null && exportTemplateBeanTo!=null) {
			exportTemplateBeanTo.setReportType(exportTemplateBeanFrom.getReportType());
			exportTemplateBeanTo.setExportFormat(exportTemplateBeanFrom.getExportFormat());
			exportTemplateBeanTo.setDescription(exportTemplateBeanFrom.getDescription());
		}
	}
	
	/**
	 * Get the subtype the next unique name is looked by
	 * @param labelBeanFrom
	 * @return
	 */
	@Override
	public Integer getSubtype(ILabelBean labelBeanFrom) {
		return null;
	}

	
	/**
	 * Deletes an object by key (and also the eventual descendants if not leaf)
	 * @param objectID
	 * @param categoryType
	 * @return
	 */
	@Override
	public void delete(Integer objectID, String categoryType) {
		//ReportBL.delete(objectID);
		deleteRecursively(objectID);
	}
	
	/**
	 * Deletes all derived templates first then the template
	 * @param projectBean
	 */
	private static void deleteRecursively(Integer exportTemplateID) {
		
		List<TExportTemplateBean> derivedReports = ReportBL.loadDerived(exportTemplateID);
		if (derivedReports!=null) {
			for (TExportTemplateBean exportTemplateBean : derivedReports) {
				LOGGER.fatal("Deleting the derived report " + exportTemplateBean.getLabel()+ " with ID " +
						exportTemplateBean.getObjectID() + " parent ID " + exportTemplateID + " at " + new Date());
				deleteRecursively(exportTemplateBean.getObjectID());
			}
		}
		ReportBL.delete(exportTemplateID);
	}
	
	/**
	 * Does the category has content (subcategories or leafs)
	 * @param objectID
	 * @return
	 */
	@Override
	public boolean replaceNeeded(Integer objectID) {
		return false;
	}
	
	/**
	 * Replace the node (leaf) or leafs below the node (category)
	 * by deleting the node
	 * @param objectID: can be a categoryID or a leafID
	 * @param replacementID: is always a leafID
	 * @return
	 */
	@Override
	public void replace(Integer objectID, Integer replacementID) {
	}
	
	/**
	 * Gets the detail json for a leaf
	 * @param objectID
	 * @param add
	 * @param modifiable
	 * @param personID
	 * @param locale
	 * @return
	 */
	public String getDetailJSON(Integer objectID, boolean add,
			boolean modifiable, Integer personID, Locale locale) {
		String label = null;
		if (!add) {
			TExportTemplateBean exportTemplateBean = (TExportTemplateBean)getByKey(objectID);
			if (exportTemplateBean!=null) {
				label = exportTemplateBean.getLabel();
			}
		}
		return ReportJSON.getReportDetailJSON(label, modifiable);
	}
}
