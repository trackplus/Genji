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

package com.aurel.track.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.aurel.track.admin.customize.category.report.ReportBL;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.util.EqualUtils;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TExportTemplateBean
    extends com.aurel.track.beans.base.BaseTExportTemplateBean
    implements Serializable, IBeanID, ILocalizedLabelBean, ISerializableLabelBean, ILabelBeanWithCategory {
   
	private static final long serialVersionUID = 1L;

	public static interface REPOSITORY_TYPE {
        public static final int PRIVATE=1;
        public static final int PUBLIC=2;
        public static final int PROJECT=3;
	}

	/**
	 * 
	 */
	public String getKeyPrefix() {
		return LocalizationKeyPrefixes.REPORT_LABEL_PREFIX;
	}
	
	/**
	 * whether the report can be executed directly 
	 * or a configuration is needed first
	 * (whether to show a cogwheel or a filter symbol at the user interface)
	 */
	private boolean configNeeded;

	public boolean isConfigNeeded() {
		return configNeeded;
	}

	public void setConfigNeeded(boolean configNeeded) {
		this.configNeeded = configNeeded;
	}

	public String getLabel() {
		return getName();
	}
	
	public void setLabel(String label) {
		setName(label);
	}
	/**A
	 * Serialize the labelBean 
	 * @param attributes
	 * @return
	 */ 
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("name", getName());
		String reportType = getReportType();
		if (reportType!=null)
			attributesMap.put("reportType", reportType);
		
		attributesMap.put("exportFormat", getExportFormat());
		attributesMap.put("repositoryType", getRepositoryType().toString());
		
		String description = getDescription();	
		if (description!=null && !"".equals(description)) {
			attributesMap.put("description", description);
		}
		Integer project = getProject();
		if (project!=null)
			attributesMap.put("project", project.toString());
		Integer categoryKey = getCategoryKey();
		if (categoryKey!=null)
		attributesMap.put("categoryKey", categoryKey.toString());
		attributesMap.put("person", getPerson().toString());
		attributesMap.put("deleted", getDeleted());
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}
	
	
	/**A
	 * deserialize the labelBean 
	 * @param attributes
	 * @return
	 */
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TExportTemplateBean exportTemplateBean = new TExportTemplateBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			exportTemplateBean.setObjectID(new Integer(strObjectID));
		}
		exportTemplateBean.setName(attributes.get("name"));
		exportTemplateBean.setReportType(attributes.get("reportType"));	
		exportTemplateBean.setExportFormat(attributes.get("exportFormat"));	
		exportTemplateBean.setRepositoryType(Integer.parseInt(attributes.get("repositoryType")));
		exportTemplateBean.setDescription(attributes.get("description"));
		exportTemplateBean.setProject(Integer.parseInt(attributes.get("project")));
		exportTemplateBean.setPerson(Integer.parseInt(attributes.get("person")));
		exportTemplateBean.setCategoryKey(Integer.parseInt(attributes.get("categoryKey")));
		exportTemplateBean.setDeleted(attributes.get("deleted"));
		exportTemplateBean.setUuid(attributes.get("uuid"));
		return exportTemplateBean;
	}
	
	/**
	 * check if we should consider two beans the same
	 * @param serializableLabelBean
	 * @return
	 */
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean) {
		if (serializableLabelBean==null) {
			return false;
		}
		TExportTemplateBean exportTemplateBean = (TExportTemplateBean) serializableLabelBean;
		if (getUuid() != null && exportTemplateBean.getUuid()!=null)
			if (getUuid().equals(exportTemplateBean.getUuid()))
				return true;
		String externalName = getName();
		String internalName = exportTemplateBean.getName();	
		String externalExportFormat = getExportFormat();
		String internalExportFormat = exportTemplateBean.getExportFormat();
		String externalReportType = getReportType();
		String internalReportType = exportTemplateBean.getReportType();
		Integer externalRepositoryType = getRepositoryType();
		Integer internalRepositoryType =exportTemplateBean.getRepositoryType();
		return externalName == internalName && EqualUtils.equal(externalExportFormat, internalExportFormat) &&
				EqualUtils.equal(externalReportType, internalReportType) && EqualUtils.equal(internalRepositoryType, externalRepositoryType);
	}
		
	/**
	 * save the bean
	 * @param serializableLabelBean
	 * @return
	 */
	public Integer saveBean(ISerializableLabelBean serializableLabelBean){
		return ReportBL.saveReport((TExportTemplateBean)serializableLabelBean);
	}
	
	/**
	 * Not implemented yet
	 */
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		return false;
	}
	
	/**
	 * not implemented yet
	 */
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, 
			Map<String, Map<Integer, Integer>> matchesMap) {
		return 0;
	}

}
