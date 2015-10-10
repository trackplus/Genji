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

package com.aurel.track.configExchange.exporter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.xml.sax.SAXException;

import com.aurel.track.configExchange.exporter.jaxb.Attribute;
import com.aurel.track.configExchange.exporter.jaxb.DependencyData;
import com.aurel.track.configExchange.exporter.jaxb.Entity;
import com.aurel.track.configExchange.exporter.jaxb.ObjectFactory;
import com.aurel.track.configExchange.exporter.jaxb.Reference;
import com.aurel.track.configExchange.exporter.jaxb.TrackplusRoot;

/**
 * Helper class used to serialize am entity
 * @author  Adrian Bojani
 * @since 4.0.0
 */
public class EntityExporter {
	static Logger LOGGER = LogManager.getLogger(EntityExporter.class);
	public static final String VERSION="4.0.0";
	
	private static SchemaFactory schemaFactory;
	
	private static Schema schema;
	
	private static Marshaller marshaller;
	
	private static JAXBContext jaxbContext;
	private static Unmarshaller unmarshaller;
	
	public static SchemaFactory getSchemaFactory() {
		if (schemaFactory==null) {
			schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		}
		return schemaFactory;
	}
	
	public static Schema getSchema() throws SAXException {
		if (schema==null) {
			SchemaFactory schemaFactory = getSchemaFactory();
			InputStream schemaResourceStream = EntityExporter.class.getResourceAsStream("exporter.xsd");
			Source xsdSxhemaSource = new StreamSource(schemaResourceStream);
			schema = schemaFactory.newSchema( xsdSxhemaSource ); 			
		}
		return schema;
	}
	
	public static Marshaller getMarshaller() throws JAXBException, SAXException {
		if (marshaller==null) {
			JAXBContext context = getJaxbContext();
		    marshaller = context.createMarshaller();
		    marshaller.setSchema(getSchema());
		    marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		}
		return marshaller;
	}

	public static JAXBContext getJaxbContext() throws JAXBException {
		if (jaxbContext==null) {
			Class<?> clazz = TrackplusRoot.class;
			jaxbContext = JAXBContext.newInstance( clazz.getPackage().getName() );
		}
		return jaxbContext;
	}
	
	public static Unmarshaller getUnmarshaller() throws JAXBException, SAXException {
		if (unmarshaller==null) {
			JAXBContext context = getJaxbContext();
			unmarshaller = context.createUnmarshaller();
			unmarshaller.setSchema(getSchema());
		}
		return unmarshaller;
	}

	static String writeDocument( JAXBElement<TrackplusRoot> document ) throws EntityExporterException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try{
			XMLOutputFactory xof = XMLOutputFactory.newInstance();
			XMLStreamWriter streamWriter = xof.createXMLStreamWriter(baos);
			CDataXMLStreamWriter cdataStreamWriter = new CDataXMLStreamWriter( streamWriter );

			getMarshaller().marshal( document, cdataStreamWriter );
		}catch (JAXBException ex){
			throw  new EntityExporterException("JAXBException: "+ex.getMessage(),ex);
		}catch(SAXException ex){
			throw  new EntityExporterException("SAXException: "+ex.getMessage(),ex);
		}catch(XMLStreamException ex){
			throw  new EntityExporterException("XMLStreamException: "+ex.getMessage(),ex);
		}
	    return baos.toString();
	}
	
	/**
	 * export using jaxb
	 * @param entityContextList
	 * @return
	 * @throws JAXBException
	 * @throws IOException
	 * @throws SAXException 
	 */
	public static String export2(List<EntityContext> entityContextList ) throws EntityExporterException {
		ObjectFactory objectFactory = new ObjectFactory();
		TrackplusRoot trackplusExchangeRoot = objectFactory.createTrackplusRoot();
		JAXBElement<TrackplusRoot> trackplusExchange = objectFactory.createTrackplusExchange(trackplusExchangeRoot);
		
		trackplusExchangeRoot.setVersion(VERSION);

		Map<String, Map<Integer, EntityContext>> globalDependencies=new HashMap<String, Map<Integer, EntityContext>>();
		
		for (EntityContext entityContext : entityContextList) {
			trackplusExchangeRoot.getEntityExchange().add(createEntity(objectFactory, entityContext));
			collectDependencies(entityContext,globalDependencies);
		}

		Collection<DependencyData> allDependencyes = createDependecyData(objectFactory, globalDependencies);
		if (!allDependencyes.isEmpty()) {
			trackplusExchangeRoot.getEntityDependency().addAll(allDependencyes);
		}
		
		return writeDocument(trackplusExchange);
	}
	private static void collectDependencies(EntityContext entityContext,Map<String, Map<Integer, EntityContext>> globalDependencies){
		List<EntityDependency> dependencies=entityContext.getDependencies();
		if(dependencies!=null){
			String type;
			Integer id;
			Map<Integer, EntityContext> entityContextMap;
			for(EntityDependency entityDependency:dependencies){
				if(entityDependency.isEntityAdded()){
					continue;
				}
				type=entityDependency.getDependencyType();
				id=entityDependency.getDependencyID();
				entityContextMap=globalDependencies.get(type);
				if(entityContextMap==null){
					entityContextMap=new HashMap<Integer, EntityContext>();
					globalDependencies.put(type,entityContextMap);
				}
				if(!entityContextMap.keySet().contains(id)){
					entityContextMap.put(id,entityDependency.getEntityContext());
				}
				collectDependencies(entityDependency.getEntityContext(),globalDependencies);
			}
		}
		List<EntityRelation>  relations=entityContext.getRelations();
		if(relations!=null){
			for(EntityRelation entityRelation:relations){
				List<EntityContext> entityContextList=entityRelation.getEntities();
				for(EntityContext entityContextRel:entityContextList){
					collectDependencies(entityContextRel,globalDependencies);
				}
			}
		}
	}

	private static Collection<DependencyData> createDependecyData(ObjectFactory objectFactory, Map<String, Map<Integer, EntityContext>> globalDependencies) {
		Collection<DependencyData> allDependencyes = new ArrayList<DependencyData>();
		for (Map.Entry<String, Map<Integer, EntityContext>> dependecyTypeToEntityEntry : globalDependencies.entrySet()) {
			String dependencyType = dependecyTypeToEntityEntry.getKey();
			Map<Integer, EntityContext> dependencyesMap = dependecyTypeToEntityEntry.getValue();
			if (!dependencyesMap.isEmpty()) {
				DependencyData dependencyData = objectFactory.createDependencyData();
				dependencyData.setType(dependencyType);
				dependencyData.setParentAttributeName(""); // TODO get a real name ?

				for (Map.Entry<Integer, EntityContext> dependencyEntry : dependencyesMap.entrySet()) {
					EntityContext entityContext = dependencyEntry.getValue();
					dependencyData.getTrackEntity().add(createEntity(objectFactory, entityContext));
				}
				allDependencyes.add(dependencyData);
			}
		}
		return allDependencyes;
	}
	
	private static Entity createEntity(ObjectFactory objectFactory, EntityContext entityContext ) {
		Entity entity = objectFactory.createEntity();
		entity.setType(entityContext.getName());
		entity.setEntityId(entityContext.getSerializableLabelBeans().getObjectID().toString());
		addEntityAttributes(entity, entityContext, objectFactory);
		addRelationsData(objectFactory, entityContext, entity);
		
		addReferredDependency(objectFactory, entityContext, entity);

		//globalEntityDependecy.mergeDependencies(entityContext.getDependencies());
		return entity;
	}

	private static void addReferredDependency(ObjectFactory objectFactory,
			EntityContext entityContext, Entity entity) {
		
		List<EntityDependency> dependencies = entityContext.getDependencies();
		
		for (EntityDependency dependecy : dependencies) {
			Reference reference = objectFactory.createReference();
			reference.setAttributeName(dependecy.getAttributeName());
			reference.setDependencyEntityType(dependecy.getDependencyType());
			reference.setDependencyId(dependecy.getDependencyID().toString());
			entity.getReferredDependency().add(reference);
			/*for (Map.Entry<String, Map<Integer, EntityContext>> dependecyTypeToBeanIdToEntity : dependecyTypeToBeanIdToEntityMap.entrySet()) {
				String dependencyType = dependecyTypeToBeanIdToEntity.getKey();
				Map<Integer, EntityContext> beanIdToEntityMap = dependecyTypeToBeanIdToEntity.getValue();
				for (Map.Entry<Integer, EntityContext> beanIdToEntity : beanIdToEntityMap.entrySet()) {

				}
			}*/
		}
	}

	private static void addRelationsData(ObjectFactory objectFactory,
			EntityContext entityContext, Entity entity) {
		List<EntityRelation> subEntityRelations = entityContext.getRelations();
		for (EntityRelation subEntityRelation: subEntityRelations) {
			List<EntityContext> subEntityEntities = subEntityRelation.getEntities();
			if (! subEntityEntities.isEmpty()) {
				String subEntityType = subEntityEntities.get(0).getName();
				DependencyData dependencyData = objectFactory.createDependencyData();
				dependencyData.setType(subEntityType);
				dependencyData.setParentAttributeName(subEntityRelation.getParentAttributeName());
				entity.getSubEntityRelation().add(dependencyData);
				
				for (EntityContext subEntity : subEntityEntities) {
					dependencyData.getTrackEntity().add(createEntity(objectFactory, subEntity));
				}
			}
		}	
	}

	private static void addEntityAttributes(Entity entity,
			EntityContext entityContext, ObjectFactory objectFactory) {
		Map<String, String> serializeBean = entityContext.getSerializableLabelBeans().serializeBean();
		for (Map.Entry<String, String> serializeAttribute : serializeBean.entrySet()) {
			Attribute enityAttribute = objectFactory.createAttribute();
			enityAttribute.setName(serializeAttribute.getKey());
			enityAttribute.setValue(serializeAttribute.getValue());
			entity.getEntityAttribute().add(enityAttribute);
		}
	}


}
