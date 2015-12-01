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

package com.aurel.track.configExchange.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.xml.sax.SAXException;

import com.aurel.track.beans.ISerializableLabelBean;
import com.aurel.track.configExchange.exporter.EntityExporter;
import com.aurel.track.configExchange.exporter.jaxb.Attribute;
import com.aurel.track.configExchange.exporter.jaxb.DependencyData;
import com.aurel.track.configExchange.exporter.jaxb.Entity;
import com.aurel.track.configExchange.exporter.jaxb.Reference;
import com.aurel.track.configExchange.exporter.jaxb.TrackplusRoot;

/**
 * Import entities from XML
 * @author  Adrian Bojani
 * @since 4.0.0
 */
public class EntityImporter {
	private static final Logger LOGGER = LogManager.getLogger(EntityImporter.class);
	
	// maybe i should keep internal saved bean here
	private final Map<String, Map<Integer, Integer>> typeToExternalIdToSavedIdMap = new HashMap<String, Map<Integer, Integer>>();
	Map<String, Map<Integer, Entity>> globalDependencies=new HashMap<String, Map<Integer, Entity>>();
	public EntityImporter(){
	}
	public List<ImportResult> importFile(File uploadFile,ImportContext importContext ) throws EntityImporterException {
		try {
			InputStream is=new FileInputStream(uploadFile);
			return importFile(is,importContext);
		} catch (FileNotFoundException e) {
			LOGGER.warn(ExceptionUtils.getStackTrace(e));  //To change body of catch statement use File | Settings | File Templates.
		}
		return new ArrayList<ImportResult>();
	}
	public List<ImportResult> importFile(InputStream is,ImportContext importContext ) throws EntityImporterException {
		boolean overwriteExisting=importContext.isOverrideExisting();
		boolean overrideOnlyNotModifiedByUser=importContext.isOverrideOnlyNotModifiedByUser();
		boolean clearChildren=importContext.isClearChildren();
		String type=importContext.getEntityType();
		Map<String,String> extraAttributes=importContext.getAttributeMap();
		LOGGER.debug("Import file overwriteExisting="+overwriteExisting+" type="+type);
		List<ImportResult> result=new ArrayList<ImportResult>();
		Unmarshaller unmarshaller = null;
		try {
			unmarshaller = EntityExporter.getUnmarshaller();
		} catch (JAXBException e) {
			throw new EntityImporterException("JAXBException:"+e.getMessage(),e);
		} catch (SAXException e) {
			throw new EntityImporterException("SAXException:"+e.getMessage(),e);
		}
		JAXBElement<TrackplusRoot> root = null;
		try {
			root = (JAXBElement<TrackplusRoot>) unmarshaller.unmarshal(is);
		} catch (JAXBException e) {
			throw new EntityImporterException("JAXBException:"+e.getMessage(),e);
		}
		TrackplusRoot trackplusExchangeRoot = root.getValue();

		//collect all dependences as map
		List<DependencyData> globalDependencyList = trackplusExchangeRoot.getEntityDependency();
		for (DependencyData dependency : globalDependencyList) {
			String dependencyType=dependency.getType();
			Map<Integer, Entity> entityMap=globalDependencies.get(dependencyType);
			if(entityMap==null){
				entityMap=new HashMap<Integer, Entity>();
				globalDependencies.put(dependencyType,entityMap);
			}
			List<Entity> dependencyEntityList = dependency.getTrackEntity();
			for (Entity dependencyEntity : dependencyEntityList) {
				entityMap.put(Integer.parseInt(dependencyEntity.getEntityId()),dependencyEntity);
			}
		}
		// save dependency first
		LOGGER.debug("Saving dependences....");
		for (DependencyData dependency : globalDependencyList) {
			List<Entity> dependencyEntityList = dependency.getTrackEntity();
			for (Entity dependencyEntity : dependencyEntityList) {
				EntityImportContext entityImportContext =new EntityImportContext();
				entityImportContext.setEntity(dependencyEntity);
				entityImportContext.setAttributeMap(toAttributeMap(dependencyEntity.getEntityAttribute()));
				entityImportContext.setOverrideExisting(false);
				entityImportContext.setClearChildren(false);
				saveEntity(entityImportContext);
			}
		}
		LOGGER.debug("Dependences saved!");
		
		// save entity list
		List<Entity> entityExchangeList = trackplusExchangeRoot.getEntityExchange();
		LOGGER.debug("entityExchangeList size:"+entityExchangeList.size());
		Map<String, String> attributes;
		for (Entity entity : entityExchangeList) {
			LOGGER.debug("entity "+entity.getType()+" id:"+entity.getEntityId());
			if(type!=null&&!type.equals(entity.getType())){
				LOGGER.debug("invalid entity type");
				continue;
			}
			attributes=toAttributeMap(entity.getEntityAttribute());
			//override  attributes
			if(extraAttributes!=null){
				Iterator<String> it= extraAttributes.keySet().iterator();
				while (it.hasNext()){
					String key=it.next();
					String value=extraAttributes.get(key);
					if(value==null){
						attributes.remove(key);
					}else{
						attributes.put(key,value);
					}
				}
			}

			EntityImportContext entityImportContext =new EntityImportContext();
			entityImportContext.setEntity(entity);
			entityImportContext.setAttributeMap(attributes);
			entityImportContext.setOverrideExisting(overwriteExisting);
			entityImportContext.setClearChildren(clearChildren);
			entityImportContext.setOverrideOnlyNotModifiedByUser(overrideOnlyNotModifiedByUser);
			ImportResult importResult=saveEntity(entityImportContext);
			result.add(importResult);

		}
		return result;
	}
	private ImportResult saveEntity(EntityImportContext entityImportContext) throws EntityImporterException {

		Entity entity= entityImportContext.getEntity();
		Map<String, String> attributeMap= entityImportContext.getAttributeMap();
		boolean overrideExisting= entityImportContext.isOverrideExisting();
		boolean overrideOnlyNotModifiedByUser=entityImportContext.isOverrideOnlyNotModifiedByUser();
		boolean clearChildren= entityImportContext.isClearChildren();

		Integer entityID = toInteger(entity.getEntityId());
		String entityType = entity.getType();
		LOGGER.debug("Saving entity: "+entityType+" id:"+entityID+" overrideExisting="+overrideExisting+" overrideOnlyNotModifiedByUser="+overrideOnlyNotModifiedByUser);
		
		//updateReferredDependency
		updateReferredDependency(entity, attributeMap);

		//saving the entity attributes
		IEntityImporter entityImporter=EntityImporterFactory.getInstance().getImporter(entityType);
		if(entityImporter==null){
			LOGGER.error("No entity importer found for :"+entityType);
			return new ImportResult(null,entityType, entityID, ImportResult.ERROR_NO_IMPORTER_FOUND,null);
		}
		ISerializableLabelBean	serializableLabelBean = entityImporter.createInstance(attributeMap);
		// save into db
		ISerializableLabelBean dbBean = entityImporter.queryForMatchingEntity(serializableLabelBean);

		ImportResult importResult;
		if (dbBean==null) {
			LOGGER.debug("No matching found. Insert new one.");
			// do insert
			importResult=insertToDB(serializableLabelBean, entityType, entityID, entityImporter);
		} else {
			LOGGER.debug("Matching found");
			Integer dbObjectID=dbBean.getObjectID();
			if(overrideExisting){
				boolean dbBeanChanged=entityImporter.isChanged(dbBean);
				if(overrideOnlyNotModifiedByUser&&dbBeanChanged){
					LOGGER.debug("No dot override. Bean is changed");
					importResult=new ImportResult(dbBean,entityType, entityID, ImportResult.SUCCESS_TAKE_EXISTING,dbObjectID);
				}else{
					LOGGER.debug("Matching found. Overwrite:"+dbObjectID);
					Map<String, String> newAttributes = serializableLabelBean.serializeBean();
					dbBean=dbBean.deserializeBean(newAttributes);
					dbBean.setObjectID(dbObjectID);
					Integer dbEntityID = entityImporter.save(dbBean);
					if(dbEntityID==null){
						LOGGER.error("Error saving bean to DB");
						importResult=new ImportResult(null,entityType, entityID, ImportResult.ERROR_DB_SAVE,null);
					}else{
						importResult=new ImportResult(dbBean,entityType, entityID, ImportResult.SUCCESS_OVERRIDE,dbEntityID);
					}
				}
			}else{
				LOGGER.debug("Matching found. Do not overwrite");
				importResult=new ImportResult(dbBean,entityType, entityID, ImportResult.SUCCESS_TAKE_EXISTING,dbObjectID);
			}
		}

		if(importResult.isSuccess()){
			Integer dbEntityID=importResult.getNewObjectID();
			markAsSaved(entityType, entityID, dbEntityID);
			boolean doClearChildren=false;
			boolean saveRelations=false;
			int code=importResult.getCode();
			switch (code){
				case ImportResult.SUCCESS_NEW_ENTITY:{
					doClearChildren=false;
					saveRelations=true;
					break;
				}
				case ImportResult.SUCCESS_OVERRIDE:{
					doClearChildren=clearChildren;
					saveRelations=true;
					break;
				}
				case ImportResult.SUCCESS_TAKE_EXISTING:{
					if(overrideExisting){
						doClearChildren=clearChildren;
						saveRelations=true;
					}
					break;
				}
			}
			if(doClearChildren){
				LOGGER.debug("Do clear children for entity: "+entityType);
				entityImporter.clearChildren(dbEntityID);
			}else{
				LOGGER.debug("NOT clear children for entity: "+entityType);
			}
			if(saveRelations){
				LOGGER.debug("Save relations for entity: "+entityType);
				saveRelations(entity, overrideExisting, overrideOnlyNotModifiedByUser,dbEntityID);
			}else{
				LOGGER.debug("NOT save relations for entity: "+entityType);
			}
		}
		return importResult;
	}
	public ImportResult insertToDB(ISerializableLabelBean serializableLabelBean,String entityType,Integer entityID, IEntityImporter entityImporter){
		serializableLabelBean.setObjectID(null);
		Integer dbEntityId = entityImporter.save(serializableLabelBean);
		if(dbEntityId==null){
			LOGGER.error("Error saving bean:'"+entityType+"' to DB: "+ serializableLabelBean);
			return new ImportResult(null,entityType, entityID, ImportResult.ERROR_DB_SAVE,null);
		}else{
			LOGGER.debug("Success insert bean to DB, dbEntityId="+dbEntityId);
			serializableLabelBean.setObjectID(dbEntityId);
			ImportResult result=new ImportResult(serializableLabelBean,entityType, entityID, ImportResult.SUCCESS_NEW_ENTITY,dbEntityId);
			markAsSaved(entityType, entityID, dbEntityId);
			return result;
		}
	}

	private void saveRelations(Entity entity, boolean overwriteExisting, boolean overrideOnlyNotModifiedByUser, Integer parentID) throws  EntityImporterException {
		List<DependencyData> subEntityRelations = entity.getSubEntityRelation();
		Map<String, String> childAttributesToSave;
		for (DependencyData childrenDependecy : subEntityRelations) {
			String parentFkAttributeName = childrenDependecy.getParentAttributeName();
			List<Entity> childrenEntityList = childrenDependecy.getTrackEntity();
			for (Entity childEntity : childrenEntityList) {
				childAttributesToSave = toAttributeMap(childEntity.getEntityAttribute());
				childAttributesToSave.put(parentFkAttributeName,parentID.toString());

				EntityImportContext entityImportContext =new EntityImportContext();
				entityImportContext.setEntity(childEntity);
				entityImportContext.setAttributeMap(childAttributesToSave);
				entityImportContext.setOverrideExisting(overwriteExisting);
				entityImportContext.setOverrideOnlyNotModifiedByUser(overrideOnlyNotModifiedByUser);
				entityImportContext.setClearChildren(false);
				saveEntity(entityImportContext);
			}
		}
	}

	private void updateReferredDependency(Entity entity, Map<String, String> attributeMap) throws  EntityImporterException {
		List<Reference> referredDependency = entity.getReferredDependency();
		for (Reference reference : referredDependency) {
			String referenceType = reference.getDependencyEntityType();
			Integer referenceId = toInteger(reference.getDependencyId());
			String referenceName = reference.getAttributeName();

			Integer referenceInternalId = getSavedDependecyId(referenceType, referenceId);
			if (referenceInternalId==null) {
				LOGGER.debug("Dependence:'"+referenceType+"' with ID:"+referenceId+" not saved yet!");

				Entity  dependencyEntity=globalDependencies.get(referenceType).get(referenceId);
				if(dependencyEntity!=null){
					LOGGER.debug("Dependence not saved found");
					EntityImportContext entityImportContext =new EntityImportContext();
					entityImportContext.setEntity(dependencyEntity);
					entityImportContext.setAttributeMap(toAttributeMap(dependencyEntity.getEntityAttribute()));
					entityImportContext.setOverrideExisting(false);
					entityImportContext.setClearChildren(false);
					ImportResult importResult=saveEntity(entityImportContext);
					if(importResult.isSuccess()){
						referenceInternalId = getSavedDependecyId(referenceType, referenceId);
					}
				}
			}
			attributeMap.put(referenceName, referenceInternalId.toString());
		}
	}
	private Integer getSavedDependecyId(String referenceType,
			Integer externalId) {
		Map<Integer, Integer> idTable = typeToExternalIdToSavedIdMap.get(referenceType);
		if (idTable!=null) {
			return idTable.get(externalId);
		}
		return null;
	}

	private void markAsSaved(String entityType, Integer externalEntityId,
			Integer internalEntityId) {
		Map<Integer, Integer> idTable = typeToExternalIdToSavedIdMap.get(entityType);
		if (idTable==null) {
			idTable = new HashMap<Integer, Integer>();
			typeToExternalIdToSavedIdMap.put(entityType, idTable);
		}
		idTable.put(externalEntityId, internalEntityId);
	}

	private Integer toInteger(String entityId) {
		try {
			return Integer.parseInt(entityId);
		} catch (NumberFormatException nfe) {
			return null;
		}
	}

	private Map<String, String> toAttributeMap(List<Attribute> entityAttributeList) {
		Map<String, String> attributeMap = new HashMap<String, String>();
		for (Attribute attribute : entityAttributeList) {
			String name = attribute.getName();
			String value = attribute.getValue();
			attributeMap.put(name, value);
		}
		Error w;
		return attributeMap;
	}
}
