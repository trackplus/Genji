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

package com.aurel.track.exchange.track.exporter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.aurel.track.accessControl.AccessControlBL;
import com.aurel.track.admin.customize.account.AccountBL;
import com.aurel.track.admin.customize.category.report.ReportBL;
import com.aurel.track.admin.customize.category.report.execute.ReportBeansToXML;
import com.aurel.track.admin.customize.lists.customOption.OptionBL;
import com.aurel.track.admin.customize.objectStatus.SystemStatusBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.admin.customize.role.RoleBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigBL;
import com.aurel.track.admin.customize.treeConfig.field.GeneralSettingsBL;
import com.aurel.track.admin.customize.treeConfig.field.OptionSettingsBL;
import com.aurel.track.admin.customize.treeConfig.field.TextBoxSettingsBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.user.department.DepartmentBL;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ISerializableLabelBean;
import com.aurel.track.beans.TAccessControlListBean;
import com.aurel.track.beans.TAccountBean;
import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.beans.TCostCenterBean;
import com.aurel.track.beans.TDepartmentBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TListBean;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TOptionSettingsBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.beans.TRoleBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.exchange.track.ExchangeFieldNames;
import com.aurel.track.exchange.track.ExchangeHistoryFieldEntry;
import com.aurel.track.exchange.track.ExchangeHistoryTransactionEntry;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.DropDownContainer;
import com.aurel.track.fieldType.runtime.base.ICustomFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.bl.CustomSelectUtil;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ILookup;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.runtime.system.select.SystemIssueTypeRT;
import com.aurel.track.fieldType.runtime.system.select.SystemManagerRT;
import com.aurel.track.fieldType.runtime.system.select.SystemPriorityRT;
import com.aurel.track.fieldType.runtime.system.select.SystemProjectRT;
import com.aurel.track.fieldType.runtime.system.select.SystemResponsibleRT;
import com.aurel.track.fieldType.runtime.system.select.SystemSeverityRT;
import com.aurel.track.fieldType.runtime.system.select.SystemStateRT;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.history.HistoryLoaderBL;
import com.aurel.track.item.history.HistoryLoaderBL.LONG_TEXT_TYPE;
import com.aurel.track.item.history.HistoryValues;
import com.aurel.track.persist.HistoryDropdownContainerLoader;
import com.aurel.track.persist.ReportBeanHistoryLoader;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanWithHistory;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.PropertiesHelper;
import com.aurel.track.util.TagReplacer;
import com.aurel.track.util.emailHandling.Html2Text;

/**
 * Exporter implementation for trackplus
 * @author Tamas
 *
 */
public class TrackExportBL {

    static Logger LOGGER = LogManager.getLogger(TrackExportBL.class);
    
    public static ZipOutputStream exportWorkItemsWithAttachments(List<ReportBean> reportBeanList, 
            Integer personID, OutputStream outputStream) {
        
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        
        Document document = exportWorkItems(reportBeanList, personID);
        ZipEntry dataEntry = new ZipEntry(ExchangeFieldNames.EXCHANGE_ZIP_ENTRY);
        try {
            zipOutputStream.putNextEntry(dataEntry);
        } catch (IOException e) {
            LOGGER.error("Adding the XML data to the zip failed with " + e.getMessage(), e);
        }
        ReportBeansToXML.convertToXml(zipOutputStream, document);
        //zipOutputStream.closeEntry();
        //get the attachments for each workItem (if exist)
        if (reportBeanList!=null) {
            for (ReportBean reportBean : reportBeanList) {
                TWorkItemBean workItemBean = reportBean.getWorkItemBean();
                Integer workItemID = workItemBean.getObjectID();
                String workItemAttachmentsDirectory = AttachBL.getFullDirName(null, workItemID);
                File file=new File(workItemAttachmentsDirectory);
                if(file.exists() && file.isDirectory()) {
                    ReportBL.zipFiles(file, zipOutputStream, file.getAbsolutePath());
                }
            }
        }
        try {
            zipOutputStream.close();
        } catch (IOException e) {
            LOGGER.warn("Closing the zip failed with " + e.getMessage(), e);
        }
        return zipOutputStream;
    }
    
    /**
     * Exports the reportBeans to an xml file
     * @param reportBeans
     * @param personID
     * @return
     */
    private static Document exportWorkItems(List<ReportBean> reportBeansList, Integer personID) {
        Document dom = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            dom = builder.newDocument();
        } catch (FactoryConfigurationError e){
            LOGGER.error("Creating the DOM document failed with FactoryConfigurationError:" + e.getMessage(), e);
            return null;
        } catch (ParserConfigurationException e){
            LOGGER.error("Creating the DOM document failed with ParserConfigurationException: " + e.getMessage(), e);
            return null;
        }
        //create the DOM object
        Element root = dom.createElement(ExchangeFieldNames.TRACKPLUS_EXCHANGE);
        dom.appendChild(root); 
        if (reportBeansList==null || reportBeansList.isEmpty()) {
            return dom;
        }
        LOGGER.info("Number of workItems exported: " + reportBeansList.size());
        //load also the full history 
        List<ReportBeanWithHistory> reportBeansWithHistoryList = ReportBeanHistoryLoader.getReportBeanWithHistoryList(reportBeansList, 
                Locale.ENGLISH, true, false, true, null, true, true, true, true, true, personID, null, null, null, true, LONG_TEXT_TYPE.ISFULLHTML);
        
        //get the workItemIDs
        List<Integer> workItemIDsList = new ArrayList<Integer>();
        Set<Integer> issueTypeSet = new HashSet<Integer>();
        Set<Integer> projectTypeSet = new HashSet<Integer>();
        Set<Integer> projectSet = new HashSet<Integer>();
        for (ReportBean reportBean : reportBeansWithHistoryList) {
            TWorkItemBean workItemBean = reportBean.getWorkItemBean();
            workItemIDsList.add(workItemBean.getObjectID());
            issueTypeSet.add(workItemBean.getListTypeID());
            projectSet.add(workItemBean.getProjectID());
        }
        List<TProjectBean> projectBeans = ProjectBL.loadByProjectIDs(GeneralUtils.createListFromSet(projectSet));
        if (projectBeans!=null) {
            for (TProjectBean projectBean : projectBeans) {
                projectTypeSet.add(projectBean.getProjectType());
            }
        }
        
        //load the dropdown container based on the workItemIDsList
        DropDownContainer dropDownContainer = HistoryDropdownContainerLoader.loadExporterDropDownContainer(
                GeneralUtils.createIntArrFromIntegerList(workItemIDsList));
        
        /**
         * fieldType based lookup values will be gathered in the lookup map:
         * fieldID_parametercode keyed -> lookup objectID keyed -> attribute name to attribute string value map
         */
        Map<Integer, ILabelBean> personBeansMap = dropDownContainer.getDataSourceMap(MergeUtil.mergeKey(SystemFields.INTEGER_PERSON, null));
        Map<String, Map<Integer, Map<String, String>>> serializedLookupsMap = new HashMap<String, Map<Integer, Map<String, String>>>();
        //initialize the serialized person map explicitly because it will be loaded also 
        //from other parts (consultants/informants, budget, cost etc.)
        Map<Integer, Map<String, String>> serializedPersonsMap = new HashMap<Integer, Map<String,String>>(); 
        serializedLookupsMap.put(MergeUtil.mergeKey(SystemFields.INTEGER_PERSON, null), serializedPersonsMap);
        
        /**
         * gather all non-fieldType based lookup values in the additionalSerializedEntitiesMap map: 
         *    accounts, costcenteres, departments, system states, projectTypes and lists     
         */
        Map<String, Map<Integer, Map<String, String>>> additionalSerializedLookupsMap = new HashMap<String, Map<Integer, Map<String, String>>>();

        Map<Integer, TCostCenterBean> costCentersMap = GeneralUtils.createMapFromList(AccountBL.getAllCostcenters());
        Map<Integer, TAccountBean> accountsMap = GeneralUtils.createMapFromList(AccountBL.getAllAccounts());
        
        //get the custom fields map
        Map<Integer, ISerializableLabelBean> customFieldBeansMap = GeneralUtils.createMapFromList(FieldBL.loadCustom());
        
        Set<Integer> pseudoHistoryFields = HistoryLoaderBL.getPseudoHistoryFields();
        /**
         * add the actual attribute values 
         */
        Set<Integer> nonNullFields = new HashSet<Integer>();
        //linked through parent, issueNos in descriptions/comments and later issueLinks
        Set<Integer> linkedWorkItemIDs = new HashSet<Integer>(); 
        for (int n=0; n<reportBeansWithHistoryList.size (); n++){
            ReportBean reportBean = (ReportBean) reportBeansWithHistoryList.get (n);
            TWorkItemBean workItemBean = reportBean.getWorkItemBean();
            Map<String, String> attributes = new HashMap<String, String>();
            attributes.put(ExchangeFieldNames.UUID, workItemBean.getUuid());
            attributes.put(ExchangeFieldNames.WORKITEMID, workItemBean.getObjectID().toString());
            Element itemElement = createElementWithAttributes(ExchangeFieldNames.ITEM, null, false, attributes, dom);
            //system field nodes
            for (int i = 0; i < SystemFields.getSystemFieldsArray().length; i++) {
                Integer fieldID = Integer.valueOf(SystemFields.getSystemFieldsArray()[i]);
                if (!fieldID.equals(SystemFields.INTEGER_ISSUENO)) { //the issueNo is added directly to item element  {
                    Object workItemAttribute = workItemBean.getAttribute(fieldID, null);
                    if (workItemAttribute!=null) {
                        IFieldTypeRT  fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
                        if (fieldTypeRT!=null) {
	                        nonNullFields.add(fieldID);
	                        gatherLinkedWorkItems(workItemAttribute, fieldTypeRT, linkedWorkItemIDs);
	                        Map<String, String> fieldAndParameterCodeAttributes = new HashMap<String, String>();
	                        fieldAndParameterCodeAttributes.put(ExchangeFieldNames.FIELDID, fieldID.toString());
	                        appendAttributeElement(ExchangeFieldNames.ITEM_ATTRIBUTE, itemElement, workItemAttribute, 
	                            fieldTypeRT, fieldID, null, dropDownContainer, serializedLookupsMap, 
	                            additionalSerializedLookupsMap, fieldAndParameterCodeAttributes, dom);
                        }
                    }
                }
            }
            //custom field nodes
            Map<String, Object> customAttributes = workItemBean.getCustomAttributeValues();
            Integer parameterCode;
            if (customAttributes!=null && !customAttributes.isEmpty()) {
                Iterator<String> iterator = customAttributes.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String[] splittedKey = MergeUtil.splitKey(key);
                    String strFieldID = splittedKey[0].substring(1); //get rid of "f"
                    Integer fieldID = Integer.valueOf(strFieldID);
                    String strParameterCode = null;
                    parameterCode = null;
                    if (splittedKey.length>1) {
                        //whether the workItemAttribute is a part of a composite custom field
                        strParameterCode = splittedKey[1];
                        if (strParameterCode!=null && !"null".equals(strParameterCode)) {
                            try {
                                parameterCode = Integer.valueOf(strParameterCode);
                            } catch (Exception e) {
                                LOGGER.error("Converting the parameterCode " + strParameterCode + " to an integer failed with " + e.getMessage(), e);
                            }
                        }
                    }
                    //add to custom field beans
                    addToAdditionalSerializedLookupsMap(customFieldBeansMap.get(fieldID), ExchangeFieldNames.FIELD, additionalSerializedLookupsMap);                                        
                    Object workItemAttribute = workItemBean.getAttribute(fieldID, parameterCode);
                    if (workItemAttribute!=null) {
                        IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID, parameterCode);
                        nonNullFields.add(fieldID);
                        gatherLinkedWorkItems(workItemAttribute, fieldTypeRT, linkedWorkItemIDs);
                        Map<String, String> fieldAndParameterCodeAttributes = new HashMap<String, String>();
                        if (strFieldID!=null) {
                            fieldAndParameterCodeAttributes.put(ExchangeFieldNames.FIELDID, strFieldID);
                        }
                        if (strParameterCode!=null) {
                            fieldAndParameterCodeAttributes.put(ExchangeFieldNames.PARAMETERCODE, strParameterCode);
                        }
                        appendAttributeElement(ExchangeFieldNames.ITEM_ATTRIBUTE, itemElement, workItemAttribute, fieldTypeRT, fieldID, 
                                parameterCode, dropDownContainer, serializedLookupsMap, additionalSerializedLookupsMap,
                                fieldAndParameterCodeAttributes, dom);
                    }
                }
            }
            /**
             * add the consulted and informed nodes
             */
            Set<Integer> consultedList = reportBean.getConsultedList();
            if (consultedList!=null && !consultedList.isEmpty()) {
                itemElement.appendChild(createConsInfElement(ExchangeFieldNames.CONSULTANT_LIST, ExchangeFieldNames.CONSULTANT, 
                    consultedList, personBeansMap, serializedPersonsMap, dom));
            }
            Set<Integer> informedList = reportBean.getInformedList();
            if (informedList!=null && !informedList.isEmpty()) {
                itemElement.appendChild(createConsInfElement(ExchangeFieldNames.INFORMANT_LIST, ExchangeFieldNames.INFORMANT, 
                    informedList, personBeansMap, serializedPersonsMap, dom));
            }
            /**
             * add the history nodes
             */
            ReportBeanWithHistory reportBeanWithHistory=(ReportBeanWithHistory)reportBean;
            Element historyElement = createHistoryElement(reportBeanWithHistory.getHistoryValuesMap(), 
                    dropDownContainer, dom, serializedLookupsMap, additionalSerializedLookupsMap, pseudoHistoryFields, linkedWorkItemIDs);
            if (historyElement!=null) {
                itemElement.appendChild(historyElement);
            }
            /**
             * add the budget nodes
             */
            Element budgetElement = createBudgetPlanElement(reportBeanWithHistory.getBudgetHistory(),
                    personBeansMap, serializedPersonsMap, dom, false);
            if (budgetElement!=null) {
                itemElement.appendChild(budgetElement);
            }
            Element plannedValueElement = createBudgetPlanElement(reportBeanWithHistory.getPlannedValueHistory(),
                    personBeansMap, serializedPersonsMap, dom, true);
            if (plannedValueElement!=null) {
                itemElement.appendChild(plannedValueElement);
            }
            
            if (budgetElement!=null) {
                //do not add estimated remaining budget element if no budget exists
                Element remainingBudgetElement = createRemainingBudgetElement(
                        reportBeanWithHistory.getActualEstimatedBudgetBean(), personBeansMap, serializedPersonsMap, dom);
                if (remainingBudgetElement!=null) {
                    itemElement.appendChild(remainingBudgetElement);
                }
            }
            /**
             * add the expense nodes
             */
            Element costElement = createExpenseElement(reportBeanWithHistory.getCosts(), 
                    personBeansMap, accountsMap, costCentersMap, serializedPersonsMap, dom, additionalSerializedLookupsMap);
            if (costElement!=null) {
                itemElement.appendChild(costElement);
            }
            
            List<TAttachmentBean> existingAttachments = AttachBL.getExistingAttachments(reportBeanWithHistory.getAttachments());
            Element attachmentElement = createAttachmentElement(existingAttachments, 
                    personBeansMap, serializedPersonsMap, dom);
            if (attachmentElement!=null) {
                itemElement.appendChild(attachmentElement);
            }
            root.appendChild (itemElement);
        }
        
        /**
         * load the not explicitly exported linked workItems
         */
        linkedWorkItemIDs.removeAll(workItemIDsList);
        if (!linkedWorkItemIDs.isEmpty()) {
            List<TWorkItemBean> workItemBeanList = ItemBL.loadByWorkItemKeys(GeneralUtils.createIntArrFromSet(linkedWorkItemIDs));
            if (workItemIDsList!=null) {
                Iterator<TWorkItemBean> itrWorkItemBean = workItemBeanList.iterator();
                while (itrWorkItemBean.hasNext()) {
                    TWorkItemBean workItemBean = itrWorkItemBean.next();
                    Map<String, String> attributeValues = workItemBean.serializeBean();
                    root.appendChild(createElementWithAttributes(ExchangeFieldNames.LINKED_ITEMS, "", false, attributeValues, dom));
                }
            }
        }
        
        /**
         * add the systemState entries to the additionalSerializedEntitiesMap
         */
        Map<Integer, Map<String, String>> serializedProjectMap = serializedLookupsMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PROJECT, null));
        Map<Integer, Map<String, String>> serializedReleaseMap = serializedLookupsMap.get(MergeUtil.mergeKey(SystemFields.RELEASE, null));
        Map<Integer, Map<String, String>> serializedAccountsMap = additionalSerializedLookupsMap.get(ExchangeFieldNames.ACCOUNT);
        Map<Integer, TSystemStateBean> systemStateMap = GeneralUtils.createMapFromList(SystemStatusBL.loadAll());
        addSystemState(serializedProjectMap, systemStateMap,  additionalSerializedLookupsMap);
        addSystemState(serializedReleaseMap, systemStateMap, additionalSerializedLookupsMap);
        addSystemState(serializedAccountsMap, systemStateMap,  additionalSerializedLookupsMap);
        /**
         * add each option for the list (which have at least one option set on an issue)
         */
        Map<Integer, Map<String, String>> serializedListMap = additionalSerializedLookupsMap.get(ExchangeFieldNames.LIST);
        addListAdditional(serializedListMap, dropDownContainer, serializedLookupsMap, additionalSerializedLookupsMap);
        /**
         * add the project related additional entities to serializedEntitiesMap and additionalSerializedEntitiesMap 
         */
        Map<Integer, TProjectTypeBean> projetcTypesMap = GeneralUtils.createMapFromList(ProjectTypesBL.loadAll());
        addProjectAdditional(serializedProjectMap, projetcTypesMap, serializedLookupsMap,
        		additionalSerializedLookupsMap, accountsMap, costCentersMap);
        /**
         * add field related entities: the owners
         */
        Map<Integer, Map<String, String>> serializedCustomFields = additionalSerializedLookupsMap.get(ExchangeFieldNames.FIELD);
        addFieldAdditional(serializedCustomFields, serializedLookupsMap);
        /**
         * add the used fieldConfigs
         */
        List<TFieldConfigBean> fieldConfigBeans = FieldConfigBL.loadAllForFields(nonNullFields);
        for (Iterator<TFieldConfigBean> iterator = fieldConfigBeans.iterator(); iterator.hasNext();) {
            //remove the field configurations for not used issueTypes, projectTypes and projects 
            TFieldConfigBean fieldConfigBean = iterator.next();
            Integer issueTypeID = fieldConfigBean.getIssueType();
            Integer projectTypeID = fieldConfigBean.getProjectType();
            Integer projectID = fieldConfigBean.getProject();
            if (issueTypeID!=null && !issueTypeSet.contains(issueTypeID)) {
                iterator.remove();
                continue;
            }
            if (projectTypeID!=null && !projectTypeSet.contains(projectTypeID)) {
                iterator.remove();
                continue;
            }
            if (projectID!=null && !projectSet.contains(projectID)) {
                iterator.remove();
                continue;
            }
        }
        
        //comment this and uncomment the coming block to add fewer field configs
        addToAdditionalSerializedLookupsMap((List)fieldConfigBeans, ExchangeFieldNames.FIELDCONFIG, additionalSerializedLookupsMap);
        Map<Integer, Map<String, String>> serializedFieldConfigs = additionalSerializedLookupsMap.get(ExchangeFieldNames.FIELDCONFIG);    
        addFieldConfigsAdditional(serializedFieldConfigs, projetcTypesMap,
                serializedLookupsMap, additionalSerializedLookupsMap);
        /**
         * add the field settings
         */            
        List<Integer> userFieldConfigIDs = GeneralUtils.createListFromSet(serializedFieldConfigs.keySet());
        //option settings
        List<ISerializableLabelBean> optionSettingsList = (List)OptionSettingsBL.loadByConfigList(userFieldConfigIDs);
        if (optionSettingsList!=null) {
            Iterator<ISerializableLabelBean> itrOptionSettings = optionSettingsList.iterator();
            while (itrOptionSettings.hasNext()) {
                TOptionSettingsBean optionSettingsBean = (TOptionSettingsBean) itrOptionSettings.next();
                Integer listID = optionSettingsBean.getList();
                if (serializedListMap==null || !serializedListMap.containsKey(listID)) {
                    //do not export the option settings if the corresponding list is not exported 
                    //(none of the selected workItems has a value from this list)
                    //because creating the optionSetings will fail without the listID 
                    itrOptionSettings.remove();
                }
                
            }
        }
        addToAdditionalSerializedLookupsMap(optionSettingsList, ExchangeFieldNames.OPTIONSETTINGS, additionalSerializedLookupsMap);
        //textbox settings
        List<ISerializableLabelBean> textBoxSettingsList = (List)TextBoxSettingsBL.loadByConfigList(userFieldConfigIDs);
        addToAdditionalSerializedLookupsMap(textBoxSettingsList, ExchangeFieldNames.TEXTBOXSETTINGS, additionalSerializedLookupsMap);
        //general settings
        List<ISerializableLabelBean> generalSettingsList = (List)GeneralSettingsBL.loadByConfigList(userFieldConfigIDs);
        addToAdditionalSerializedLookupsMap(generalSettingsList, ExchangeFieldNames.GENERALSETTINGS, additionalSerializedLookupsMap);
        
        /**
         * add the person related additional entities to serializedEntitiesMap and additionalSerializedEntitiesMap 
         */
        addPersonAdditional(serializedPersonsMap, GeneralUtils.createMapFromList(DepartmentBL.getAllDepartments()), 
                costCentersMap, serializedLookupsMap, additionalSerializedLookupsMap);
        
        /**
         * add the role assignments and the roles
         * the role assignments cannot be added to the additionalSerializedLookupsMap because they have no objectID
         * consequently they will be serialized directly
         */
        List<ISerializableLabelBean> accessControlListBeanList =
             (List)AccessControlBL.loadByPersonsAndProjects(GeneralUtils.createIntegerListFromCollection(serializedPersonsMap.keySet()), 
                     GeneralUtils.createIntegerListFromCollection(serializedProjectMap.keySet()));
        if (accessControlListBeanList!=null) {
            Map<Integer, TRoleBean> roleMap = GeneralUtils.createMapFromList(RoleBL.loadAll());
            Iterator<ISerializableLabelBean> iterator = accessControlListBeanList.iterator();
            while (iterator.hasNext()) {
                TAccessControlListBean accessControlListBean = (TAccessControlListBean) iterator.next();
                //serialize the role assignment directly        
                root.appendChild(createElementWithAttributes(ExchangeFieldNames.ACL, null, false, accessControlListBean.serializeBean(), dom));
                Integer role = accessControlListBean.getRoleID();
                TRoleBean roleBean = roleMap.get(role);
                if (roleBean!=null) {
                    //add the roles which are present in the role assignments
                    addToAdditionalSerializedLookupsMap(roleBean, ExchangeFieldNames.ROLE, additionalSerializedLookupsMap);
                }
            }
        }
        
        /**
         * first add the additional serialized entities first because the fieldID mapping is needed by parsing the serialized entities
         */
        Iterator<String> iterator = additionalSerializedLookupsMap.keySet().iterator();
        while (iterator.hasNext()) {
            String entityName = iterator.next();
            Map<Integer, Map<String, String>> lookupEntityMap = additionalSerializedLookupsMap.get(entityName);
            if (lookupEntityMap!=null && !lookupEntityMap.isEmpty()) {
                Iterator<Integer> itrObjectID = lookupEntityMap.keySet().iterator();
                while (itrObjectID.hasNext()) {
                    Integer objectID = itrObjectID.next();
                    Map<String, String> attributes = lookupEntityMap.get(objectID);
                    if (attributes!=null && !attributes.isEmpty()) {
                        root.appendChild(createElementWithAttributes(entityName, null, false, attributes, dom));
                    }
                }
            }
        }
        
        /**
         * add the serialized entities
         */
        iterator = serializedLookupsMap.keySet().iterator();
        while (iterator.hasNext()) {
            String mergedKey = iterator.next();
            Integer[] parts = MergeUtil.getParts(mergedKey);
            Integer fieldID = parts[0];
            Integer parameterCode = parts[1];
            Map<Integer, Map<String, String>> lookupEntityMap = serializedLookupsMap.get(mergedKey);
            if (lookupEntityMap!=null && !lookupEntityMap.isEmpty()) {
                Iterator<Integer> itrObjectID = lookupEntityMap.keySet().iterator();
                while (itrObjectID.hasNext()) {
                    Integer objectID = itrObjectID.next();
                    Map<String, String> attributes = lookupEntityMap.get(objectID);
                    if (attributes!=null && !attributes.isEmpty()) {
                        if (fieldID!=null) {
                            attributes.put(ExchangeFieldNames.FIELDID, fieldID.toString());
                        }
                        if (parameterCode!=null) {
                            attributes.put(ExchangeFieldNames.PARAMETERCODE, parameterCode.toString());
                        }
                        root.appendChild(createElementWithAttributes(ExchangeFieldNames.DROPDOWN_ELEMENT, null, false, attributes, dom));
                    }
                }
            }
        }
        return dom;
    }
    
    /**
     * Gather the issueType and project attributes for each field
     * @param issueTypeID
     * @param projectID
     * @param fieldID
     * @param issueTypeProjectForFields
     */
    private static void gatherLinkedWorkItems(Object workItemAttribute, IFieldTypeRT fieldTypeRT, Set<Integer> linkedWorkItemIDs) {
        if (fieldTypeRT!=null) {
            if (fieldTypeRT.getValueType()==ValueType.LONGTEXT) {
                String longText = "";
                try {
                    longText = (String)workItemAttribute;
                } catch (Exception e) {
                    LOGGER.warn("Long text " + workItemAttribute + " is not a String");
                }
                List<Integer> workItemIDs = TagReplacer.gatherIssueLinks(new StringBuilder(longText));
                if (!workItemIDs.isEmpty()) {
                    linkedWorkItemIDs.addAll(workItemIDs);
                }
            } else {
                if (fieldTypeRT.getValueType()==ValueType.SYSTEMOPTION && 
                                SystemFields.INTEGER_ISSUENO.equals(fieldTypeRT.getSystemOptionType())) {
                	if (fieldTypeRT.isCustom()) {
                		//item picker
        	        	Object[] pickedItemsArr = (Object[])workItemAttribute;
        	        	for (Object object : pickedItemsArr) {
							Integer itemID = null;
        	        		try {
        	        			itemID = (Integer)object;
        	        			if (itemID!=null) {
        	        				linkedWorkItemIDs.add(itemID);
        	        			}
        	        		} catch (Exception e) {
        	        			LOGGER.warn("Picked item ID " + workItemAttribute + " is not an Integer");
        	        		}
        				}
                	} else {
	                    Integer parentID = null;
	                    try {
	                        parentID = (Integer)workItemAttribute;
	                    } catch (Exception e) {
	                        LOGGER.warn("Parent ID " + workItemAttribute + " is not an Integer");
	                    }
	                    if (parentID!=null) {
	                        linkedWorkItemIDs.add(parentID);
	                    }
                	}
                }
            }
        }
    }
    
    /**
     * creates an attribute element: either direct attribute or lookup attribute.
     * If lookup attribute then creates also the detailed lookup element 
     * and adds to the lookupMap if not yet added
     * @param value
     * @param fieldTypeRT
     * @param fieldID
     * @param parameterCode
     * @param dropDownContainer
     * @param serializedLookupsMap
     * @param dom
     * @return
     */
    private static void appendAttributeElement(String elementName, Element parentElement, Object value, IFieldTypeRT fieldTypeRT, 
            Integer fieldID, Integer parameterCode, DropDownContainer dropDownContainer,
            Map<String, Map<Integer, Map<String, String>>> serializedLookupsMap, 
            Map<String, Map<Integer, Map<String, String>>> additionalSerializedLookupsMap, 
            Map<String, String> attributes, Document dom) {
        if (fieldTypeRT==null) {
            LOGGER.warn("No fieldTypeRT found for fieldID " + fieldID + " and parameterCode " + parameterCode);
            return;
        }
        int valueType=fieldTypeRT.getValueType();
        switch (valueType) {
        case ValueType.CUSTOMOPTION:
            //the custom selects are with multiple values
            Integer[] dropDownIDs = CustomSelectUtil.getSelectedOptions(value);
            if (dropDownIDs!=null && dropDownIDs.length>0) {
                for (int i = 0; i < dropDownIDs.length; i++) {
                    //do not add only the actual option values but also the possible values
                    //consequently here we gather only the lists used and later all the options of each gathered list will be added
                    //addToSerializedLookupsMap(dropDownIDs[i], fieldID, parameterCode, dropDownContainer, fieldTypeRT, serializedLookupsMap);
                    String customOptionMapKey = MergeUtil.mergeKey(fieldID, parameterCode);
                    Map customOptionMap = dropDownContainer.getDataSourceMap(customOptionMapKey);
                    if (customOptionMap!=null) {
                        TOptionBean optionBean = (TOptionBean)customOptionMap.get(dropDownIDs[i]);
                        if (optionBean!=null) {
                            Integer listID = optionBean.getList();
                            Map listBeanMap = dropDownContainer.getDataSourceMap(ExchangeFieldNames.LIST);
                            if (listBeanMap!=null && listID!=null) {
                                TListBean listBean = (TListBean)listBeanMap.get(listID);
                                if (listBean!=null)
                                    addToAdditionalSerializedLookupsMap(listBean, ExchangeFieldNames.LIST, additionalSerializedLookupsMap);
                            }
                        }
                    }
                    parentElement.appendChild(createElementWithAttributes(elementName, dropDownIDs[i].toString(), false, attributes, dom));
                }
            }
            break;
        case ValueType.SYSTEMOPTION:
            //the system selects have always single values
            Integer dropDownID = getObjectID(value, fieldTypeRT);
            if (dropDownID!=null) {
                if (!SystemFields.INTEGER_ISSUENO.equals(fieldTypeRT.getSystemOptionType())) {
                    //do not add the issueNo and parentNo into the lookup map (they not even implement ILookup)
                    addToSerializedLookupsMap(dropDownID, fieldID, parameterCode, (ISerializableLabelBean)LookupContainer.getNotLocalizedLabelBean(fieldID, dropDownID), fieldTypeRT, serializedLookupsMap);
                }
                parentElement.appendChild(createElementWithAttributes(elementName, dropDownID.toString(), false, attributes,  dom));
            }
            break;
        case ValueType.TEXT:
        case ValueType.LONGTEXT:
            //short text or long text the value itself (not the simplified value as it is in SystemLongTextRT.getShowISOValue())
                if (value!=null) {
                    //toString instead of (String) conversion because SystemProjectSpecoficRT's value type is text but the value is string
                    parentElement.appendChild(createElementWithAttributes(elementName, value.toString(), true, attributes, dom));
                }
            break;    
        default:
            //other non text
            String showValue=fieldTypeRT.getShowISOValue(fieldID, parameterCode, value, null, null, null);
            parentElement.appendChild(createElementWithAttributes(elementName, showValue, false, attributes,  dom));
        }
    }
        
    /**
     * Add to lookup map if not yet added
     * @param dropDownID
     * @param fieldID
     * @param parameterCode
     * @param dropDownContainer
     * @param fieldTypeRT
     * @param lookupMap
     */
    private static void addToSerializedLookupMap(Integer objectID, 
            Map<Integer, ILabelBean> dropDownMap,
             Map<Integer, Map<String, String>> serializedLookupsMap) {
        if (objectID!=null && serializedLookupsMap!=null && dropDownMap!=null) {
            ISerializableLabelBean serializableLabelBean = (ISerializableLabelBean)dropDownMap.get(objectID);
            if (serializedLookupsMap.get(objectID)==null && serializableLabelBean!=null) {
                serializedLookupsMap.put(objectID, serializableLabelBean.serializeBean());
            }
        }
    }
    
    /**
     * Gets the objectID of a lookup field
     * @param workItemAttribute
     * @param fieldTypeRT
     * @return
     */
    private static Integer getObjectID(Object workItemAttribute, IFieldTypeRT fieldTypeRT) {
        if (workItemAttribute!=null && fieldTypeRT!=null) {
            Integer objectID = null;
            if (fieldTypeRT.isCustom() && ((ICustomFieldTypeRT)fieldTypeRT).isCustomSelect()) {
                //TODO refactorize when other system pickers are defined  
                Integer[] optionIDs = CustomSelectUtil.getSelectedOptions(workItemAttribute);
                if (optionIDs!=null && optionIDs.length>0) {
                    objectID = optionIDs[0];
                }    
            } else {
                try {
                    objectID = (Integer)workItemAttribute;
                } catch (Exception e) {
                    LOGGER.warn("Value for field type " + fieldTypeRT.getClass()  + 
                            " and value type " + fieldTypeRT.getValueType() + " is not an integer " + e.getMessage(), e);
                    return null;
                }
            }
            return objectID;
        }
        return null;
    }
    
    /**
     * Creates the trail history element
     * @param historyList
     * @param dom
     * @return
     */
    private static Element createConsInfElement(String parentName, String childName,
            Set<Integer> consInfList, Map<Integer, ILabelBean> personMap,
            Map<Integer, Map<String, String>> personsLookupMap, Document dom) {
        //get the sorted list from map
        Integer personID;
        if (consInfList!=null && !consInfList.isEmpty()) {
            Element consInfListElement = dom.createElement(parentName);
            Iterator<Integer> iterator = consInfList.iterator();
            while (iterator.hasNext()) {
                personID = iterator.next();
                //create the system field node
                Map<String, String> attributes = new HashMap<String, String>();
                attributes.put(ExchangeFieldNames.PERSON, personID.toString());
                consInfListElement.appendChild(createElementWithAttributes(childName, null, false, attributes, dom));
                addToSerializedLookupMap(personID, personMap, personsLookupMap);
            }
            return consInfListElement;
        }
        return null;
    }
    
    /**
     * Creates the history element
     * @param historyList
     * @param dom
     * @return
     */
    private static Element createHistoryElement(Map<Integer, Map<Integer, HistoryValues>> historyTransactionsMap, 
    		DropDownContainer dropDownContainer, Document dom, 
            Map<String, Map<Integer, Map<String, String>>> serializedLookupsMap,
            Map<String, Map<Integer, Map<String, String>>> additionalSerializedLookupsMap,
            Set<Integer> pseudoHistoryFields,
            Set<Integer> linkedWorkItemIDs) {
        if (historyTransactionsMap!=null && !historyTransactionsMap.isEmpty()) {
            Element transactionElement = null;
            Element historyTransactionsList = dom.createElement(ExchangeFieldNames.HISTORY_LIST);
            Iterator<Integer> transactionsItr = historyTransactionsMap.keySet().iterator();
            while (transactionsItr.hasNext()) {
                Integer transactionID = transactionsItr.next();
                Map<Integer, HistoryValues> fieldChanges = historyTransactionsMap.get(transactionID);
                if (fieldChanges!=null) {
                    Iterator<Integer> fieldChangeItr = fieldChanges.keySet().iterator();
                    boolean firstFieldChange = true;
                    while (fieldChangeItr.hasNext()) {
                        Integer fieldChangeID = fieldChangeItr.next();
                        HistoryValues historyValues = fieldChanges.get(fieldChangeID);
                        if (firstFieldChange) {
                            ExchangeHistoryTransactionEntry exchangeHistoryTransactionEntry =  new ExchangeHistoryTransactionEntry();
                            Integer changedBy = historyValues.getChangedByID();
                            if (changedBy!=null) {
                                exchangeHistoryTransactionEntry.setChangedBy(changedBy.toString());
                            }
                            Date changedAt = historyValues.getLastEdit();
                            if (changedAt!=null) {
                                exchangeHistoryTransactionEntry.setChangedAt(DateTimeUtils.getInstance().formatISODateTime(changedAt));
                            }
                            exchangeHistoryTransactionEntry.setUuid(historyValues.getTransactionUuid());
                            Map<String, String> attributesMap = exchangeHistoryTransactionEntry.serializeBean();
                            transactionElement = createElementWithAttributes(ExchangeFieldNames.TRANSACTION, null, false, attributesMap, dom);                            
                        }
                        firstFieldChange = false;
                        Integer fieldID = historyValues.getFieldID();
                        IFieldTypeRT fieldTypeRT = null;
                        if (pseudoHistoryFields.contains(fieldID)) {                
                            fieldTypeRT = FieldTypeManager.getFieldTypeRT(SystemFields.INTEGER_DESCRIPTION);
                        } else {
                            fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
                        }
                        Object newValue = historyValues.getNewValue();
                        Object oldValue = historyValues.getOldValue();
                        Integer timesEdited = null;
                        if (SystemFields.INTEGER_COMMENT.equals(fieldID)) {
                            timesEdited = historyValues.getTimesEdited();
                        }
                        appendHistoryValue(transactionElement, newValue, oldValue, fieldID, timesEdited, fieldTypeRT, dropDownContainer, 
                                dom, serializedLookupsMap, additionalSerializedLookupsMap, linkedWorkItemIDs);
                        if (transactionElement.hasChildNodes()) {
                            //add only if there are child elements
                            historyTransactionsList.appendChild(transactionElement);
                        }
                    }
                }
            }
            return historyTransactionsList;
        }
        return null;
    }
    
    /**
     * Appends the history attribute element
     * @param transactionElement
     * @param elementName
     * @param historyValue
     * @param fieldID
     * @param fieldTypeRT
     * @param dropDownContainer
     * @param dom
     * @param serializedLookupsMap
     */
    private static void appendHistoryValue(Element transactionElement, Object newValue, Object oldValue,
            Integer fieldID, Integer timesEdited, IFieldTypeRT fieldTypeRT, DropDownContainer dropDownContainer, Document dom, 
            Map<String, Map<Integer, Map<String, String>>> serializedLookupsMap, 
            Map<String, Map<Integer, Map<String, String>>> additionalSerializedLookupsMap,
            Set<Integer> linkedWorkItemIDs) {
        if (fieldTypeRT.isComposite()) {
            Map<Integer, Object> newValuesMap = (Map)newValue;
            Map<Integer, Object> oldValuesMap = (Map)oldValue;
            if (newValuesMap==null) {
                newValuesMap = new HashMap<Integer, Object>();
            }
            if (oldValuesMap==null) {
                oldValuesMap = new HashMap<Integer, Object>();
            }
            Set<Integer> keySet = new HashSet<Integer>();
            gatherParameters(keySet, newValuesMap.keySet());
            gatherParameters(keySet, oldValuesMap.keySet());
            Iterator<Integer> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                Integer parameterCode = iterator.next();
                Object newPartialValue = newValuesMap.get(parameterCode);
                Object oldPartialValue = oldValuesMap.get(parameterCode);
                IFieldTypeRT compositePartFieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID, parameterCode);
                Element fieldChangeElement = createFieldChangeElement(newPartialValue, oldPartialValue,
                        fieldID, parameterCode, timesEdited, compositePartFieldTypeRT, dropDownContainer, 
                        dom, serializedLookupsMap, additionalSerializedLookupsMap, linkedWorkItemIDs);
                if (fieldChangeElement!=null) {
                    transactionElement.appendChild(fieldChangeElement);
                }
            }
        }
        else {
            Element fieldChangeElement = createFieldChangeElement(newValue, oldValue,
                    fieldID, null, timesEdited, fieldTypeRT, dropDownContainer, 
                    dom, serializedLookupsMap, additionalSerializedLookupsMap, linkedWorkItemIDs);
            if (fieldChangeElement!=null) {
                transactionElement.appendChild(fieldChangeElement);
            }
        }
    }
    
    private static void gatherParameters(Set<Integer> gatheredParameters, Set<Integer> addParameters) {
        Iterator<Integer> newValueItr = addParameters.iterator();
        while (newValueItr.hasNext()) {
            Integer parameter = newValueItr.next();
            gatheredParameters.add(parameter);
        }
    }
    
    private static Element createFieldChangeElement(Object newValue, Object oldValue, 
            Integer fieldID, Integer parameterCode, Integer timesEdited, 
            IFieldTypeRT fieldTypeRT, DropDownContainer dropDownContainer, Document dom, 
            Map<String, Map<Integer, Map<String, String>>> serializedLookupsMap,
            Map<String, Map<Integer, Map<String, String>>> additionalSerializedLookupsMap,
            Set<Integer> linkedWorkItemIDs) {
        Element fieldChangeElement = null;
        if (newValue!=null || oldValue!=null) {
            ExchangeHistoryFieldEntry exchangeHistoryFieldEntry = new ExchangeHistoryFieldEntry();
            exchangeHistoryFieldEntry.setFieldID(fieldID.toString());
            if (parameterCode!=null) {
                exchangeHistoryFieldEntry.setParameterCode(parameterCode.toString());
            }
            if (timesEdited!=null) {
                exchangeHistoryFieldEntry.setTimesEdited(timesEdited.toString());
            }
            fieldChangeElement = createElementWithAttributes(ExchangeFieldNames.FIELDCHANGE, 
                    null, false, exchangeHistoryFieldEntry.serializeBean(), dom);
            if (newValue!=null) {
                gatherLinkedWorkItems(newValue, fieldTypeRT, linkedWorkItemIDs);
                appendAttributeElement(ExchangeFieldNames.NEWVALUE, fieldChangeElement, newValue, 
                        fieldTypeRT, fieldID, parameterCode, dropDownContainer, 
                        serializedLookupsMap, additionalSerializedLookupsMap, null, dom);
            }
            if (oldValue!=null) {
                gatherLinkedWorkItems(oldValue, fieldTypeRT, linkedWorkItemIDs);
                appendAttributeElement(ExchangeFieldNames.OLDVALUE, fieldChangeElement, oldValue, 
                        fieldTypeRT, fieldID, parameterCode, dropDownContainer, 
                        serializedLookupsMap, additionalSerializedLookupsMap, null, dom);
            }
        }
        return fieldChangeElement;
    }
    
    /**
     * Creates the budget history elements
     * @param budgetBeanList
     * @param dom
     * @return
     */
    private static Element createBudgetPlanElement(List<TBudgetBean> budgetBeanList, Map<Integer, ILabelBean> personMap,
            Map<Integer, Map<String, String>> personLookupMap, Document dom, boolean plan) {
        if (budgetBeanList!=null && !budgetBeanList.isEmpty()) {
            String listName = null;
            String elementName = null;
            if (plan) {
                listName = ExchangeFieldNames.PLANNED_VALUE_LIST;
                elementName = ExchangeFieldNames.PLANNED_VALUE_ELEMENT;
            } else {
                listName = ExchangeFieldNames.BUDGET_LIST;
                elementName = ExchangeFieldNames.BUDGET_ELEMENT;
            }
            Element budgetList = dom.createElement(listName);
            Iterator<TBudgetBean> iterator = budgetBeanList.iterator();
            while (iterator.hasNext()) {
                TBudgetBean budgetBean = iterator.next();
                Integer changedByID = budgetBean.getChangedByID();
                addToSerializedLookupMap(changedByID, personMap, personLookupMap);
                Map<String, String> budgetAttributes = budgetBean.serializeBean();
                String budgetDescription = budgetAttributes.remove("changeDescription");
                Element budgetElement = createElementWithAttributes(elementName, budgetDescription, true, budgetAttributes, dom);
                budgetList.appendChild(budgetElement);
            }
            return budgetList;
        }
        return null;
    }
    
    /**
     * Creates the estimated remaining budget element
     * @param remainingBudgetBean
     * @param dom
     * @return
     */
    private static Element createRemainingBudgetElement (TActualEstimatedBudgetBean remainingBudgetBean, 
            Map<Integer, ILabelBean> personMap,
            Map<Integer, Map<String, String>> personLookupMap, Document dom) {
        if (remainingBudgetBean!=null) {
            Integer changedByID = remainingBudgetBean.getChangedByID();
            addToSerializedLookupMap(changedByID, personMap, personLookupMap);
            Map<String, String> remainingBudgetAttributes = remainingBudgetBean.serializeBean();
            return createElementWithAttributes(ExchangeFieldNames.REMAINING_BUDGET_ELEMENT, null, false, remainingBudgetAttributes, dom);
        }
        return null;
    }
    
    /**
     * Creates the expense elements
     * @param costBeanList
     * @param dom
     * @return
     */
    private static Element createExpenseElement(List<TCostBean> costBeanList, Map<Integer, ILabelBean> personsMap, 
            Map<Integer, TAccountBean> accountsMap, Map<Integer, TCostCenterBean> costCentersMap, 
            Map<Integer, Map<String, String>> personLookupMap, Document dom, 
            Map<String, Map<Integer, Map<String, String>>> additionalSerializedLookupsMap) {
        if (costBeanList!=null && !costBeanList.isEmpty()) {
            Element expenseList = dom.createElement(ExchangeFieldNames.EXPENSE_LIST);
            Iterator<TCostBean> iterator = costBeanList.iterator();
            while (iterator.hasNext()) {
                TCostBean costBean = iterator.next();
                Map<String, String> costAttributes = costBean.serializeBean();
                String costDescription = costAttributes.remove("description");
                Element expenseElement = createElementWithAttributes(ExchangeFieldNames.EXPENSE_ELEMENT, costDescription, true, costAttributes, dom);
                Integer changedByID = costBean.getChangedByID();
                addToSerializedLookupMap(changedByID, personsMap, personLookupMap);
                Integer accountID = costBean.getAccount();
                addCostcentersAccountsToAdditionalLookup(accountID, accountsMap, costCentersMap, additionalSerializedLookupsMap);
                expenseList.appendChild(expenseElement);
            }
            return expenseList;
            
        }
        return null;
    }

    /**
     * Creates the attachment elements
     * @param attachmentBeanList
     * @param personBeansMap
     * @param personLookupMap
     * @param dom
     * @return
     */
    private static Element createAttachmentElement(List<TAttachmentBean> attachmentBeanList, 
            Map<Integer, ILabelBean> personBeansMap, Map<Integer, Map<String, String>> personLookupMap, Document dom) {
        if (attachmentBeanList!=null && !attachmentBeanList.isEmpty()) {
            Element attachmentList = dom.createElement(ExchangeFieldNames.ATTACHMENT_LIST);
            Iterator<TAttachmentBean> iterator = attachmentBeanList.iterator();
            while (iterator.hasNext()) {
                TAttachmentBean attachmentBean = iterator.next();
                Map<String, String> attachmentAttributes = attachmentBean.serializeBean();
                String attachmentDescription = attachmentAttributes.remove("description");
                Element attachmentElement = createElementWithAttributes(ExchangeFieldNames.ATTACHMENT_ELEMENT, 
                        attachmentDescription, true, attachmentAttributes, dom);
                Integer changedByID = attachmentBean.getChangedBy();
                addToSerializedLookupMap(changedByID, personBeansMap, personLookupMap);
                attachmentList.appendChild(attachmentElement);
            }
            return attachmentList;
        }
        return null;
    }
    
    /**
     * Add costcenters and accounts
     * @param account
     * @param accountsMap
     * @param costCentersMap
     * @param additionalSerializedEntitiesMap
     */
    private static void addCostcentersAccountsToAdditionalLookup(Integer account, Map<Integer, TAccountBean> accountsMap, 
            Map<Integer, TCostCenterBean> costCentersMap, 
            Map<String, Map<Integer, Map<String, String>>> additionalSerializedEntitiesMap) {
        TAccountBean accountBean = accountsMap.get(account);
        if (accountBean!=null) {
            addToAdditionalSerializedLookupsMap(accountBean, ExchangeFieldNames.ACCOUNT, additionalSerializedEntitiesMap);
            Integer costCenter = accountBean.getCostCenter();
            if (costCenter!=null) {
                TCostCenterBean costCenterBean = costCentersMap.get(costCenter);
                if (costCenterBean!=null) {
                    addToAdditionalSerializedLookupsMap(costCenterBean, ExchangeFieldNames.COSTCENTER, additionalSerializedEntitiesMap);
                }
            }
        }
    }
    
    /**
     * Add the additional project related entities
     * @param projetcTypeMap
     * @param serializedProjectMap
     * @param dropDownContainer
     * @param serializedLookupsMap
     * @param additionalSerializedLookupsMap
     */
    private static void addProjectAdditional(Map<Integer, Map<String, String>> serializedProjectMap, 
            Map<Integer, TProjectTypeBean> projetcTypeMap, 
            Map<String,    Map<Integer, Map<String, String>>> serializedLookupsMap,
            Map<String,    Map<Integer, Map<String, String>>> additionalSerializedLookupsMap,
            Map<Integer, TAccountBean> accountsMap, Map<Integer, TCostCenterBean> costCentersMap) {
        if (serializedProjectMap!=null) {
            Iterator<Map<String, String>> itrProject = serializedProjectMap.values().iterator();
            while (itrProject.hasNext()) {
                Map<String, String> projectAttributes = itrProject.next();
                String strProjectType = projectAttributes.get("projectType");
                if (strProjectType!=null) {
                    Integer projectType = new Integer(strProjectType);
                    TProjectTypeBean projectTypeBean = projetcTypeMap.get(projectType);
                    if (projectTypeBean!=null) {
                        addToAdditionalSerializedLookupsMap(projectTypeBean, ExchangeFieldNames.PROJECTTYPE, additionalSerializedLookupsMap);
                    }
                }
                String strProjectInitialStatus = projectAttributes.get("defaultInitStateID");
                if (strProjectInitialStatus!=null) {
                    Integer projectInitialStatus = Integer.valueOf(strProjectInitialStatus);
                    addToSerializedLookupsMap(projectInitialStatus, SystemFields.INTEGER_STATE,
                            null, LookupContainer.getStatusBean(projectInitialStatus), new SystemStateRT(), serializedLookupsMap);
                }
                String strDefaultOwnerID = projectAttributes.get("defaultOwnerID");
                if (strDefaultOwnerID!=null) {
                    Integer defaultOwnerID = Integer.valueOf(strDefaultOwnerID);
                    addToSerializedLookupsMap(defaultOwnerID, SystemFields.INTEGER_PERSON, null,
                            LookupContainer.getPersonBean(defaultOwnerID), new SystemResponsibleRT(), serializedLookupsMap);
                }
                String strDefaultManagerID = projectAttributes.get("defaultManagerID");
                if (strDefaultManagerID!=null) {
                    Integer defaultManagerID = Integer.valueOf(strDefaultManagerID);
                    addToSerializedLookupsMap(defaultManagerID, SystemFields.INTEGER_PERSON, null,
                            LookupContainer.getPersonBean(defaultManagerID), new SystemManagerRT(), serializedLookupsMap);
                }
                String moreProps = projectAttributes.get("moreProps");
                if (moreProps!=null) {
                    Integer defaultIssueType = PropertiesHelper.getIntegerProperty(moreProps, TProjectBean.MOREPPROPS.DEFAULT_ISSUETYPE);
                    if (defaultIssueType!=null) {
                        addToSerializedLookupsMap(defaultIssueType, SystemFields.INTEGER_ISSUETYPE, null,
                                LookupContainer.getItemTypeBean(defaultIssueType), new SystemIssueTypeRT(), serializedLookupsMap);
                    }
                    Integer defaultPriority = PropertiesHelper.getIntegerProperty(moreProps, TProjectBean.MOREPPROPS.DEFAULT_PRIORITY);
                    if (defaultPriority!=null) {
                        addToSerializedLookupsMap(defaultPriority, SystemFields.INTEGER_PRIORITY, null,
                                LookupContainer.getPriorityBean(defaultPriority), new SystemPriorityRT(), serializedLookupsMap);
                    }
                    Integer defaultSeverity = PropertiesHelper.getIntegerProperty(moreProps, TProjectBean.MOREPPROPS.DEFAULT_SEVERITY);
                    if (defaultSeverity!=null) {
                        addToSerializedLookupsMap(defaultSeverity, SystemFields.INTEGER_SEVERITY, null,
                                LookupContainer.getSeverityBean(defaultSeverity), new SystemSeverityRT(), serializedLookupsMap);
                    }
                    Integer defaultAccount = PropertiesHelper.getIntegerProperty(moreProps, TProjectBean.MOREPPROPS.DEFAULT_ACCOUNT);
                    if (defaultAccount!=null) {
                        addCostcentersAccountsToAdditionalLookup(defaultAccount, accountsMap, costCentersMap, additionalSerializedLookupsMap);
                    }
                }
                if (strDefaultManagerID!=null) {
                    Integer defaultManagerID = new Integer(strDefaultManagerID);
                    addToSerializedLookupsMap(defaultManagerID, SystemFields.INTEGER_PERSON, null,
                            LookupContainer.getPersonBean(defaultManagerID), new SystemManagerRT(), serializedLookupsMap);
                }
            }
        }
    }
    
    private static void addFieldAdditional(Map<Integer, Map<String, String>> serializedFieldsMap,
            Map<String,    Map<Integer, Map<String, String>>> serializedLookupsMap) {
        if (serializedFieldsMap!=null) {
            Iterator<Map<String, String>> itrField = serializedFieldsMap.values().iterator();
            while (itrField.hasNext()) {
                Map<String, String> fieldAttributes = itrField.next();
                String strOwnerID = fieldAttributes.get("owner");
                if (strOwnerID!=null) {
                    Integer ownerID = new Integer(strOwnerID);
                    addToSerializedLookupsMap(ownerID, SystemFields.INTEGER_PERSON, null, LookupContainer.getPersonBean(ownerID), new SystemManagerRT(), serializedLookupsMap);
                }
            }
        }
    }
    
    /**
     * Add the additional project related entities
     * @param departmentMap
     * @param serializedPersonMap
     * @param dropDownContainer
     * @param serializedLookupsMap
     * @param additionalSerializedLookupsMap
     */
    private static void addPersonAdditional(Map<Integer, Map<String, String>> serializedPersonMap,
            Map<Integer, TDepartmentBean> departmentMap, Map<Integer, TCostCenterBean> costCenterMap,
            Map<String,    Map<Integer, Map<String, String>>> serializedLookupsMap,
            Map<String,    Map<Integer, Map<String, String>>> additionalSerializedLookupsMap) {
        if (serializedPersonMap!=null) {
            Iterator<Map<String, String>> itrPerson = serializedPersonMap.values().iterator();
            while (itrPerson.hasNext()) {
                Map<String, String> personAttributes = itrPerson.next();
                String strDepartment = personAttributes.get("departmentID");
                if (strDepartment!=null) {
                    Integer departmentID = Integer.valueOf(strDepartment);
                    TDepartmentBean departmentBean = departmentMap.get(departmentID);
                    if (departmentBean!=null) {
                        Integer costCenterID = departmentBean.getCostcenter();
                        if (costCenterID!=null) {
                            TCostCenterBean costCenterBean = costCenterMap.get(costCenterID);
                            if (costCenterBean!=null) {
                            addToAdditionalSerializedLookupsMap(costCenterBean, ExchangeFieldNames.COSTCENTER, additionalSerializedLookupsMap);
                        }
                        }
                        addToAdditionalSerializedLookupsMap(departmentBean, ExchangeFieldNames.DEPARTMENT, additionalSerializedLookupsMap);
                    }
                }
                String strEmailRemindPriorityLevel = personAttributes.get("emailRemindPriorityLevel");
                if (strEmailRemindPriorityLevel!=null) {
                    Integer emailRemindPriorityLevel = new Integer(strEmailRemindPriorityLevel);
                    addToSerializedLookupsMap(emailRemindPriorityLevel, SystemFields.INTEGER_PRIORITY, null, LookupContainer.getSeverityBean(emailRemindPriorityLevel), new SystemPriorityRT(), serializedLookupsMap);
                }
                String strEmailRemindSeverityLevel = personAttributes.get("emailRemindSeverityLevel");
                if (strEmailRemindSeverityLevel!=null) {
                    Integer emailRemindSeverityLevel = new Integer(strEmailRemindSeverityLevel);
                    addToSerializedLookupsMap(emailRemindSeverityLevel, SystemFields.INTEGER_SEVERITY, null, LookupContainer.getSeverityBean(emailRemindSeverityLevel), new SystemSeverityRT(), serializedLookupsMap);
                }
            }
        }
    }
    
    /**
     * Add the additional field config related entities
     * @param serializedFieldConfigs
     * @param projectTypeMap
     * @param dropDownContainer
     * @param serializedLookupsMap
     * @param additionalSerializedLookupsMap
     */
    private static void addFieldConfigsAdditional(Map<Integer, Map<String, String>> serializedFieldConfigs, 
            Map<Integer, TProjectTypeBean> projectTypeMap,
            Map<String, Map<Integer, Map<String, String>>> serializedLookupsMap,
            Map<String,    Map<Integer, Map<String, String>>> additionalSerializedLookupsMap) {
        if (serializedFieldConfigs!=null) {
            Iterator<Map<String, String>> itrPerson = serializedFieldConfigs.values().iterator();
            while (itrPerson.hasNext()) {
                Map<String, String> fieldConfigAttributes = itrPerson.next();
                String strIssueType = fieldConfigAttributes.get("issueType");
                if (strIssueType!=null) {
                    Integer issueType = new Integer(strIssueType);
                    addToSerializedLookupsMap(issueType, SystemFields.INTEGER_ISSUETYPE, null, LookupContainer.getItemTypeBean(issueType), new SystemIssueTypeRT(), serializedLookupsMap);
                }
                String strProjectType = fieldConfigAttributes.get("projectType");
                if (strProjectType!=null) {
                    Integer projectType = new Integer(strProjectType);
                    TProjectTypeBean projectTypeBean = projectTypeMap.get(projectType);
                    if (projectTypeBean!=null) {
                        addToAdditionalSerializedLookupsMap(projectTypeBean, ExchangeFieldNames.PROJECTTYPE, additionalSerializedLookupsMap);
                    }
                }
                String strProject = fieldConfigAttributes.get("project");
                if (strProject!=null) {
                    Integer project = new Integer(strProject);
                    addToSerializedLookupsMap(project, SystemFields.INTEGER_PROJECT, null, LookupContainer.getProjectBean(project), new SystemProjectRT(), serializedLookupsMap);
                }
            }
        }
    }
    
    /**
     * Add the system states
     * @param systemStateMap
     * @param serializedSystemStateRelatedBeanMap
     * @param additionalSerializedLookupsMap
     */
    private static void addSystemState(Map<Integer, Map<String, String>> serializedSystemStateRelatedBeanMap, 
            Map<Integer, TSystemStateBean> systemStateMap,
            Map<String,    Map<Integer, Map<String, String>>> additionalSerializedLookupsMap) {
        if (serializedSystemStateRelatedBeanMap!=null) {
            Iterator<Map<String, String>> itrSystemStateRelatedBean = serializedSystemStateRelatedBeanMap.values().iterator();
            while (itrSystemStateRelatedBean.hasNext()) {
                Map<String, String> beanAttributes = itrSystemStateRelatedBean.next();
                String strBeanSystemStatus = beanAttributes.get("status");
                if (strBeanSystemStatus!=null) {
                    Integer systemStatus = new Integer(strBeanSystemStatus);
                    TSystemStateBean systemStateBean = systemStateMap.get(systemStatus);
                    if (systemStateBean!=null) {
                        addToAdditionalSerializedLookupsMap(systemStateBean, ExchangeFieldNames.SYSTEMSTATE, additionalSerializedLookupsMap);
                    }
                }
            }
        }
    }
    
    /**
     * Add the additional list related entities
     * @param serializedListMap
     * @param dropDownContainer
     * @param serializedLookupsMap
     * @param additionalSerializedLookupsMap
     */
    private static void addListAdditional(Map<Integer, Map<String, String>> serializedListMap,
            DropDownContainer dropDownContainer,
            Map<String,    Map<Integer, Map<String, String>>> serializedLookupsMap,
            Map<String,    Map<Integer, Map<String, String>>> additionalSerializedLookupsMap) {
        if (serializedListMap!=null) {
            Iterator<Map<String, String>> itrList = serializedListMap.values().iterator();
            while (itrList.hasNext()) {
                Map<String, String> listAttributes = itrList.next();
                //for each list add each option
                String strListID = listAttributes.get("objectID");
                if (strListID!=null) {
                    Integer listID = new Integer(strListID);
                    List<TOptionBean> listOptions = OptionBL.loadDataSourceByList(listID);
                    if (listOptions!=null) {
                        Iterator<TOptionBean> itrOptions = listOptions.iterator();
                        while (itrOptions.hasNext()) {
                            TOptionBean optionBean = itrOptions.next();
                            addToAdditionalSerializedLookupsMap(optionBean, ExchangeFieldNames.OPTION, additionalSerializedLookupsMap);
                        }
                    }
                }
                //add the project if it is project specific
                String strProjectID = listAttributes.get("project");
                if (strProjectID!=null) {
                    Integer projectID = new Integer(strProjectID);
                    TProjectBean projectBean = LookupContainer.getProjectBean(projectID);// (TProjectBean)dropDownContainer.getDataSourceMap(MergeUtil.mergeKey(SystemFields.PROJECT, null)).get(projectID);
                    if (projectBean!=null) {
                        addToSerializedLookupsMap(projectID, SystemFields.INTEGER_PROJECT, null, projectBean, new SystemProjectRT(), serializedLookupsMap);
                    }
                }
                //add the project if it is project specific
                String strOwnerID = listAttributes.get("owner");
                if (strOwnerID!=null) {
                    Integer ownerID = new Integer(strOwnerID);
                    TPersonBean personBean = LookupContainer.getPersonBean(ownerID);// (TPersonBean)dropDownContainer.getDataSourceMap(MergeUtil.mergeKey(SystemFields.PERSON, null)).get(ownerID);
                    if (personBean!=null) {
                        addToSerializedLookupsMap(ownerID, SystemFields.INTEGER_PERSON, null, personBean, new SystemManagerRT(), serializedLookupsMap);
                    }
                }
            }
        }
    }
    
    /*private static List<Map<String, String>> addAccessControlListBeanListAdditional(List<ISerializableLabelBean> accessControlListBeanList, 
            Map<Integer, TRoleBean> roleMap, Map<String,    Map<Integer, Map<String, String>>> additionalSerializedLookupsMap) {
        List<Map<String, String>> aclList = new ArrayList<Map<String,String>>();
        if (accessControlListBeanList!=null) {
            Iterator<ISerializableLabelBean> iterator = accessControlListBeanList.iterator();
            while (iterator.hasNext()) {
                TAccessControlListBean accessControlListBean = (TAccessControlListBean) iterator.next();
                aclList.add(accessControlListBean.serializeBean());
                Integer role = accessControlListBean.getRoleID();
                TRoleBean roleBean = roleMap.get(role);
                if (roleBean!=null) {
                    addToAdditionalSerializedLookupsMap(roleBean, ExchangeFieldNames.ROLE, additionalSerializedLookupsMap);
                }
            }
        }
        return aclList;
    }*/
    
    /**
     * Add to lookup map if not yet added
     * @param objectID
     * @param fieldID
     * @param parameterCode
     * @param dropDownContainer
     * @param fieldTypeRT
     * @param serializedLookupsMap
     */
    private static void addToSerializedLookupsMap(Integer objectID, Integer fieldID, Integer parameterCode, 
            ISerializableLabelBean serializableLabelBean,/*DropDownContainer dropDownContainer,*/ IFieldTypeRT fieldTypeRT,
             Map<String, Map<Integer, Map<String, String>>> serializedLookupsMap) {
        if (objectID!=null && fieldTypeRT!=null) {
            ILookup lookup = (ILookup)fieldTypeRT;
            //get the dropDownFieldID for fieldID (especially for person and release)
            Integer dropDownFieldID = lookup.getDropDownMapFieldKey(fieldID);
            String mergeKey = MergeUtil.mergeKey(dropDownFieldID, parameterCode);
            Map<Integer, Map<String, String>>  serializedEntityMap = serializedLookupsMap.get(mergeKey);
            if (serializedEntityMap==null) {
                serializedEntityMap = new HashMap<Integer, Map<String, String>>();
                serializedLookupsMap.put(mergeKey, serializedEntityMap);
            }
            if (serializedEntityMap.get(objectID)==null) {
                //ISerializableLabelBean serializableLabelBean = dropDownContainer.getDataSourceEntry(mergeKey, objectID);
                if (serializableLabelBean!=null) {
                    serializedEntityMap.put(objectID, lookup.serializeBean(serializableLabelBean));
                }
            }
        }
    }
    
    /**
     * Add an additional entity if not yet added
     * @param serializableLabelBean
     * @param additionalEntityName
     * @param additionalSerializedLookupsMap
     */
    private static void addToAdditionalSerializedLookupsMap(List<ISerializableLabelBean> serializableLabelBeanList, 
            String additionalEntityName, Map<String, Map<Integer, Map<String, String>>> additionalSerializedLookupsMap) {
        if (serializableLabelBeanList!=null) {
            for (ISerializableLabelBean serializableLabelBean : serializableLabelBeanList) {
                addToAdditionalSerializedLookupsMap(serializableLabelBean, additionalEntityName, additionalSerializedLookupsMap);
            }
        }
    }
    
    /**
     * Add an additional entity if not yet added
     * @param serializableLabelBean
     * @param additionalEntityName
     * @param additionalSerializedLookupsMap
     */
    private static void addToAdditionalSerializedLookupsMap(ISerializableLabelBean  serializableLabelBean, String additionalEntityName,
            Map<String, Map<Integer, Map<String, String>>> additionalSerializedLookupsMap) {
        Map<Integer, Map<String, String>> systemStatesLookupMap = additionalSerializedLookupsMap.get(additionalEntityName);
        if (systemStatesLookupMap==null) {
            systemStatesLookupMap = new HashMap<Integer, Map<String, String>>();
            additionalSerializedLookupsMap.put(additionalEntityName, systemStatesLookupMap);
        }
        if (systemStatesLookupMap.get(serializableLabelBean.getObjectID())==null) {
            //if not yet added
            systemStatesLookupMap.put(serializableLabelBean.getObjectID(), serializableLabelBean.serializeBean());
        }
    }
    
    /**
     * Create a dom element with text content and attributes
     * @param elementName
     * @param textContent
     * @param attributeValues
     * @param dom
     * @return
     */
    private static Element createElementWithAttributes(String elementName, String textContent, boolean needCDATA,
            Map<String, String> attributeValues, Document dom) {
        //create the main element
        Element element = null;
        try {
            element = dom.createElement(elementName);
        } catch (DOMException e) {
            LOGGER.warn("Creating an XML node with the element name " + elementName + " failed with " + e);
        }
        if (element==null) {
            return null;
        }
        
        //create the text element
        if (textContent!=null) {
            if (needCDATA) {
                CDATASection cdataText = dom.createCDATASection(Html2Text.stripNonValidXMLCharacters(textContent));
                element.appendChild(cdataText);
            } else {
                Text textElement;
                try {
                    textElement = dom.createTextNode(textContent);
                } catch (DOMException e) {
                    LOGGER.info("Creating the text node for the element " + elementName + " and text " + textContent + " failed with " + e);
                    textElement = dom.createTextNode("");
                }
                //append the text to the element
                element.appendChild(textElement);
            }
        }
        
        //set the attributes
        if (attributeValues!=null) {
            Iterator<String> iterator = attributeValues.keySet().iterator();
            while (iterator.hasNext()) {
                String attributeName = iterator.next();
                String attributeValue = attributeValues.get(attributeName);
                if (attributeValue!=null) {
                    try {
                        element.setAttribute(attributeName, attributeValue);
                    } catch (DOMException e) {
                        LOGGER.warn("Setting the attribute name " + attributeName + 
                                " to attribute value " + attributeValue + " faield with " + e.getMessage(), e);
                    }
                }
            }
        }
        return element;
    }
}
