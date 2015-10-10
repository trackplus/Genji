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

package com.trackplus.persist;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.trackplus.model.BaseEntity;

public class BasePeer<T extends BaseEntity> {
	
	private static final Logger LOGGER = LogManager.getLogger(BasePeer.class);
	
	/**
	 * Load all entities of this type from the database
	 * @return
	 */
	public List<T> loadAll() {
		Class clazz = (Class) ((ParameterizedType) getClass().getGenericSuperclass())
			                    					.getActualTypeArguments()[0];
		EntityManager em = TpEm.getEntityManager();
		CriteriaBuilder qb = em.getCriteriaBuilder();
		CriteriaQuery<T> query = qb.createQuery(clazz);
		
		Root<T> object = query.from(clazz);
		
		List<T> entities = em.createQuery(query).getResultList();
		em.close();
		if (entities != null && entities.size() > 0) {
			return entities;
		} else {
			return null;
		}
	}
	
	/**
	 * Load an object by its primary key
	 * @param key
	 * @return
	 */
	public T loadByPrimaryKey(Integer key) {
		Class clazz = (Class) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		EntityManager em = TpEm.getEntityManager();
		T result = (T) em.find(clazz, key.intValue());
	    em.close();
	    return result;
	}
	
	/**
	 * Load an object by its primary key
	 * @param key
	 * @return
	 */
	public T load(Integer key) {
		T result = loadByPrimaryKey(key);
	    return result;
	}
	
	/**
	 * Delete the entity with this primary key from the database
	 * @param objectId
	 */
	public void delete(Integer primaryKey) {
		Class clazz = (Class) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		EntityManager em = TpEm.getEntityManager();
	    try{
	        em.getTransaction().begin();
	        T delob = (T) em.find(clazz, primaryKey.intValue());
	        em.remove(delob);
	        em.getTransaction().commit();
	    } catch (Exception e) {
	    	LOGGER.error("Problem deleting object with primary key " + primaryKey + ": " + e.getMessage(), e);
	    } finally {
	        em.close();
	    }		
	}

	/**
	 * Delete this entity from the database
	 * @param objectId
	 */
	public void delete(T object) {
		Class clazz = object.getClass();
		EntityManager em = TpEm.getEntityManager();
	    try{
	        em.getTransaction().begin();
	        T delob = (T) em.find(clazz, object.getObjectID());
	        em.remove(delob);
	        em.getTransaction().commit();
	    } catch (Exception e) {
	    	LOGGER.error("Problem deleting object " + object + ": " + e.getMessage(), e);
	    } finally {
	        em.close();
	    }			
	}
	
	/**
	 * Save this object to the database
	 * @param object
	 */
	public void save(T object) {
		EntityManager em = TpEm.getEntityManager();
	    try{
	        em.getTransaction().begin();
	        // We have to find a way whether an object is new or whether
	        // it merely needs to be updated.
	        if (object.isNew()) {
	        	em.persist(object);
	        } else {
		        em.merge(object);
	        }
	        em.getTransaction().commit();
	    }finally{
	        em.close();
	    }		
	}

}
