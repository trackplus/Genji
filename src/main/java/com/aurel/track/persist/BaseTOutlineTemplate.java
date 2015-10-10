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

package com.aurel.track.persist;


import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.torque.TorqueException;
import org.apache.torque.map.TableMap;
import org.apache.torque.om.BaseObject;
import org.apache.torque.om.ComboKey;
import org.apache.torque.om.DateKey;
import org.apache.torque.om.NumberKey;
import org.apache.torque.om.ObjectKey;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.om.StringKey;
import org.apache.torque.om.Persistent;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Transaction;




  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TOutlineTemplateBean;

import com.aurel.track.beans.TOutlineCodeBean;
import com.aurel.track.beans.TOutlineTemplateDefBean;


/**
 * Holds the association between the entity and the outline template
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TOutlineTemplate
 */
public abstract class BaseTOutlineTemplate extends TpBaseObject
{
    /** The Peer class */
    private static final TOutlineTemplatePeer peer =
        new TOutlineTemplatePeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the label field */
    private String label;

    /** The value for the entityType field */
    private Integer entityType;

    /** The value for the uuid field */
    private String uuid;


    /**
     * Get the ObjectID
     *
     * @return Integer
     */
    public Integer getObjectID()
    {
        return objectID;
    }


    /**
     * Set the value of ObjectID
     *
     * @param v new value
     */
    public void setObjectID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.objectID, v))
        {
            this.objectID = v;
            setModified(true);
        }



        // update associated TOutlineCode
        if (collTOutlineCodes != null)
        {
            for (int i = 0; i < collTOutlineCodes.size(); i++)
            {
                ((TOutlineCode) collTOutlineCodes.get(i))
                        .setOutlineTemplate(v);
            }
        }

        // update associated TOutlineTemplateDef
        if (collTOutlineTemplateDefs != null)
        {
            for (int i = 0; i < collTOutlineTemplateDefs.size(); i++)
            {
                ((TOutlineTemplateDef) collTOutlineTemplateDefs.get(i))
                        .setOutlineTemplate(v);
            }
        }
    }

    /**
     * Get the Label
     *
     * @return String
     */
    public String getLabel()
    {
        return label;
    }


    /**
     * Set the value of Label
     *
     * @param v new value
     */
    public void setLabel(String v) 
    {

        if (!ObjectUtils.equals(this.label, v))
        {
            this.label = v;
            setModified(true);
        }


    }

    /**
     * Get the EntityType
     *
     * @return Integer
     */
    public Integer getEntityType()
    {
        return entityType;
    }


    /**
     * Set the value of EntityType
     *
     * @param v new value
     */
    public void setEntityType(Integer v) 
    {

        if (!ObjectUtils.equals(this.entityType, v))
        {
            this.entityType = v;
            setModified(true);
        }


    }

    /**
     * Get the Uuid
     *
     * @return String
     */
    public String getUuid()
    {
        return uuid;
    }


    /**
     * Set the value of Uuid
     *
     * @param v new value
     */
    public void setUuid(String v) 
    {

        if (!ObjectUtils.equals(this.uuid, v))
        {
            this.uuid = v;
            setModified(true);
        }


    }

       


    /**
     * Collection to store aggregation of collTOutlineCodes
     */
    protected List<TOutlineCode> collTOutlineCodes;

    /**
     * Temporary storage of collTOutlineCodes to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTOutlineCodes()
    {
        if (collTOutlineCodes == null)
        {
            collTOutlineCodes = new ArrayList<TOutlineCode>();
        }
    }


    /**
     * Method called to associate a TOutlineCode object to this object
     * through the TOutlineCode foreign key attribute
     *
     * @param l TOutlineCode
     * @throws TorqueException
     */
    public void addTOutlineCode(TOutlineCode l) throws TorqueException
    {
        getTOutlineCodes().add(l);
        l.setTOutlineTemplate((TOutlineTemplate) this);
    }

    /**
     * Method called to associate a TOutlineCode object to this object
     * through the TOutlineCode foreign key attribute using connection.
     *
     * @param l TOutlineCode
     * @throws TorqueException
     */
    public void addTOutlineCode(TOutlineCode l, Connection con) throws TorqueException
    {
        getTOutlineCodes(con).add(l);
        l.setTOutlineTemplate((TOutlineTemplate) this);
    }

    /**
     * The criteria used to select the current contents of collTOutlineCodes
     */
    private Criteria lastTOutlineCodesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTOutlineCodes(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TOutlineCode> getTOutlineCodes()
        throws TorqueException
    {
        if (collTOutlineCodes == null)
        {
            collTOutlineCodes = getTOutlineCodes(new Criteria(10));
        }
        return collTOutlineCodes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TOutlineTemplate has previously
     * been saved, it will retrieve related TOutlineCodes from storage.
     * If this TOutlineTemplate is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TOutlineCode> getTOutlineCodes(Criteria criteria) throws TorqueException
    {
        if (collTOutlineCodes == null)
        {
            if (isNew())
            {
               collTOutlineCodes = new ArrayList<TOutlineCode>();
            }
            else
            {
                criteria.add(TOutlineCodePeer.OUTLINETEMPLATE, getObjectID() );
                collTOutlineCodes = TOutlineCodePeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TOutlineCodePeer.OUTLINETEMPLATE, getObjectID());
                if (!lastTOutlineCodesCriteria.equals(criteria))
                {
                    collTOutlineCodes = TOutlineCodePeer.doSelect(criteria);
                }
            }
        }
        lastTOutlineCodesCriteria = criteria;

        return collTOutlineCodes;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTOutlineCodes(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TOutlineCode> getTOutlineCodes(Connection con) throws TorqueException
    {
        if (collTOutlineCodes == null)
        {
            collTOutlineCodes = getTOutlineCodes(new Criteria(10), con);
        }
        return collTOutlineCodes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TOutlineTemplate has previously
     * been saved, it will retrieve related TOutlineCodes from storage.
     * If this TOutlineTemplate is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TOutlineCode> getTOutlineCodes(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTOutlineCodes == null)
        {
            if (isNew())
            {
               collTOutlineCodes = new ArrayList<TOutlineCode>();
            }
            else
            {
                 criteria.add(TOutlineCodePeer.OUTLINETEMPLATE, getObjectID());
                 collTOutlineCodes = TOutlineCodePeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TOutlineCodePeer.OUTLINETEMPLATE, getObjectID());
                 if (!lastTOutlineCodesCriteria.equals(criteria))
                 {
                     collTOutlineCodes = TOutlineCodePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTOutlineCodesCriteria = criteria;

         return collTOutlineCodes;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TOutlineTemplate is new, it will return
     * an empty collection; or if this TOutlineTemplate has previously
     * been saved, it will retrieve related TOutlineCodes from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TOutlineTemplate.
     */
    protected List<TOutlineCode> getTOutlineCodesJoinTOutlineTemplate(Criteria criteria)
        throws TorqueException
    {
        if (collTOutlineCodes == null)
        {
            if (isNew())
            {
               collTOutlineCodes = new ArrayList<TOutlineCode>();
            }
            else
            {
                criteria.add(TOutlineCodePeer.OUTLINETEMPLATE, getObjectID());
                collTOutlineCodes = TOutlineCodePeer.doSelectJoinTOutlineTemplate(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TOutlineCodePeer.OUTLINETEMPLATE, getObjectID());
            if (!lastTOutlineCodesCriteria.equals(criteria))
            {
                collTOutlineCodes = TOutlineCodePeer.doSelectJoinTOutlineTemplate(criteria);
            }
        }
        lastTOutlineCodesCriteria = criteria;

        return collTOutlineCodes;
    }





    /**
     * Collection to store aggregation of collTOutlineTemplateDefs
     */
    protected List<TOutlineTemplateDef> collTOutlineTemplateDefs;

    /**
     * Temporary storage of collTOutlineTemplateDefs to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTOutlineTemplateDefs()
    {
        if (collTOutlineTemplateDefs == null)
        {
            collTOutlineTemplateDefs = new ArrayList<TOutlineTemplateDef>();
        }
    }


    /**
     * Method called to associate a TOutlineTemplateDef object to this object
     * through the TOutlineTemplateDef foreign key attribute
     *
     * @param l TOutlineTemplateDef
     * @throws TorqueException
     */
    public void addTOutlineTemplateDef(TOutlineTemplateDef l) throws TorqueException
    {
        getTOutlineTemplateDefs().add(l);
        l.setTOutlineTemplate((TOutlineTemplate) this);
    }

    /**
     * Method called to associate a TOutlineTemplateDef object to this object
     * through the TOutlineTemplateDef foreign key attribute using connection.
     *
     * @param l TOutlineTemplateDef
     * @throws TorqueException
     */
    public void addTOutlineTemplateDef(TOutlineTemplateDef l, Connection con) throws TorqueException
    {
        getTOutlineTemplateDefs(con).add(l);
        l.setTOutlineTemplate((TOutlineTemplate) this);
    }

    /**
     * The criteria used to select the current contents of collTOutlineTemplateDefs
     */
    private Criteria lastTOutlineTemplateDefsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTOutlineTemplateDefs(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TOutlineTemplateDef> getTOutlineTemplateDefs()
        throws TorqueException
    {
        if (collTOutlineTemplateDefs == null)
        {
            collTOutlineTemplateDefs = getTOutlineTemplateDefs(new Criteria(10));
        }
        return collTOutlineTemplateDefs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TOutlineTemplate has previously
     * been saved, it will retrieve related TOutlineTemplateDefs from storage.
     * If this TOutlineTemplate is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TOutlineTemplateDef> getTOutlineTemplateDefs(Criteria criteria) throws TorqueException
    {
        if (collTOutlineTemplateDefs == null)
        {
            if (isNew())
            {
               collTOutlineTemplateDefs = new ArrayList<TOutlineTemplateDef>();
            }
            else
            {
                criteria.add(TOutlineTemplateDefPeer.OUTLINETEMPLATE, getObjectID() );
                collTOutlineTemplateDefs = TOutlineTemplateDefPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TOutlineTemplateDefPeer.OUTLINETEMPLATE, getObjectID());
                if (!lastTOutlineTemplateDefsCriteria.equals(criteria))
                {
                    collTOutlineTemplateDefs = TOutlineTemplateDefPeer.doSelect(criteria);
                }
            }
        }
        lastTOutlineTemplateDefsCriteria = criteria;

        return collTOutlineTemplateDefs;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTOutlineTemplateDefs(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TOutlineTemplateDef> getTOutlineTemplateDefs(Connection con) throws TorqueException
    {
        if (collTOutlineTemplateDefs == null)
        {
            collTOutlineTemplateDefs = getTOutlineTemplateDefs(new Criteria(10), con);
        }
        return collTOutlineTemplateDefs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TOutlineTemplate has previously
     * been saved, it will retrieve related TOutlineTemplateDefs from storage.
     * If this TOutlineTemplate is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TOutlineTemplateDef> getTOutlineTemplateDefs(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTOutlineTemplateDefs == null)
        {
            if (isNew())
            {
               collTOutlineTemplateDefs = new ArrayList<TOutlineTemplateDef>();
            }
            else
            {
                 criteria.add(TOutlineTemplateDefPeer.OUTLINETEMPLATE, getObjectID());
                 collTOutlineTemplateDefs = TOutlineTemplateDefPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TOutlineTemplateDefPeer.OUTLINETEMPLATE, getObjectID());
                 if (!lastTOutlineTemplateDefsCriteria.equals(criteria))
                 {
                     collTOutlineTemplateDefs = TOutlineTemplateDefPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTOutlineTemplateDefsCriteria = criteria;

         return collTOutlineTemplateDefs;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TOutlineTemplate is new, it will return
     * an empty collection; or if this TOutlineTemplate has previously
     * been saved, it will retrieve related TOutlineTemplateDefs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TOutlineTemplate.
     */
    protected List<TOutlineTemplateDef> getTOutlineTemplateDefsJoinTOutlineTemplate(Criteria criteria)
        throws TorqueException
    {
        if (collTOutlineTemplateDefs == null)
        {
            if (isNew())
            {
               collTOutlineTemplateDefs = new ArrayList<TOutlineTemplateDef>();
            }
            else
            {
                criteria.add(TOutlineTemplateDefPeer.OUTLINETEMPLATE, getObjectID());
                collTOutlineTemplateDefs = TOutlineTemplateDefPeer.doSelectJoinTOutlineTemplate(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TOutlineTemplateDefPeer.OUTLINETEMPLATE, getObjectID());
            if (!lastTOutlineTemplateDefsCriteria.equals(criteria))
            {
                collTOutlineTemplateDefs = TOutlineTemplateDefPeer.doSelectJoinTOutlineTemplate(criteria);
            }
        }
        lastTOutlineTemplateDefsCriteria = criteria;

        return collTOutlineTemplateDefs;
    }



        
    private static List<String> fieldNames = null;

    /**
     * Generate a list of field names.
     *
     * @return a list of field names
     */
    public static synchronized List<String> getFieldNames()
    {
        if (fieldNames == null)
        {
            fieldNames = new ArrayList<String>();
            fieldNames.add("ObjectID");
            fieldNames.add("Label");
            fieldNames.add("EntityType");
            fieldNames.add("Uuid");
            fieldNames = Collections.unmodifiableList(fieldNames);
        }
        return fieldNames;
    }

    /**
     * Retrieves a field from the object by field (Java) name passed in as a String.
     *
     * @param name field name
     * @return value
     */
    public Object getByName(String name)
    {
        if (name.equals("ObjectID"))
        {
            return getObjectID();
        }
        if (name.equals("Label"))
        {
            return getLabel();
        }
        if (name.equals("EntityType"))
        {
            return getEntityType();
        }
        if (name.equals("Uuid"))
        {
            return getUuid();
        }
        return null;
    }

    /**
     * Set a field in the object by field (Java) name.
     *
     * @param name field name
     * @param value field value
     * @return True if value was set, false if not (invalid name / protected field).
     * @throws IllegalArgumentException if object type of value does not match field object type.
     * @throws TorqueException If a problem occurs with the set[Field] method.
     */
    public boolean setByName(String name, Object value )
        throws TorqueException, IllegalArgumentException
    {
        if (name.equals("ObjectID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setObjectID((Integer) value);
            return true;
        }
        if (name.equals("Label"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLabel((String) value);
            return true;
        }
        if (name.equals("EntityType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setEntityType((Integer) value);
            return true;
        }
        if (name.equals("Uuid"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setUuid((String) value);
            return true;
        }
        return false;
    }

    /**
     * Retrieves a field from the object by name passed in
     * as a String.  The String must be one of the static
     * Strings defined in this Class' Peer.
     *
     * @param name peer name
     * @return value
     */
    public Object getByPeerName(String name)
    {
        if (name.equals(TOutlineTemplatePeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TOutlineTemplatePeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TOutlineTemplatePeer.ENTITYTYPE))
        {
            return getEntityType();
        }
        if (name.equals(TOutlineTemplatePeer.TPUUID))
        {
            return getUuid();
        }
        return null;
    }

    /**
     * Set field values by Peer Field Name
     *
     * @param name field name
     * @param value field value
     * @return True if value was set, false if not (invalid name / protected field).
     * @throws IllegalArgumentException if object type of value does not match field object type.
     * @throws TorqueException If a problem occurs with the set[Field] method.
     */
    public boolean setByPeerName(String name, Object value)
        throws TorqueException, IllegalArgumentException
    {
      if (TOutlineTemplatePeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TOutlineTemplatePeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TOutlineTemplatePeer.ENTITYTYPE.equals(name))
        {
            return setByName("EntityType", value);
        }
      if (TOutlineTemplatePeer.TPUUID.equals(name))
        {
            return setByName("Uuid", value);
        }
        return false;
    }

    /**
     * Retrieves a field from the object by Position as specified
     * in the xml schema.  Zero-based.
     *
     * @param pos position in xml schema
     * @return value
     */
    public Object getByPosition(int pos)
    {
        if (pos == 0)
        {
            return getObjectID();
        }
        if (pos == 1)
        {
            return getLabel();
        }
        if (pos == 2)
        {
            return getEntityType();
        }
        if (pos == 3)
        {
            return getUuid();
        }
        return null;
    }

    /**
     * Set field values by its position (zero based) in the XML schema.
     *
     * @param position The field position
     * @param value field value
     * @return True if value was set, false if not (invalid position / protected field).
     * @throws IllegalArgumentException if object type of value does not match field object type.
     * @throws TorqueException If a problem occurs with the set[Field] method.
     */
    public boolean setByPosition(int position, Object value)
        throws TorqueException, IllegalArgumentException
    {
    if (position == 0)
        {
            return setByName("ObjectID", value);
        }
    if (position == 1)
        {
            return setByName("Label", value);
        }
    if (position == 2)
        {
            return setByName("EntityType", value);
        }
    if (position == 3)
        {
            return setByName("Uuid", value);
        }
        return false;
    }
     
    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.
     *
     * @throws Exception
     */
    public void save() throws Exception
    {
        save(TOutlineTemplatePeer.DATABASE_NAME);
    }

    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.
     * Note: this code is here because the method body is
     * auto-generated conditionally and therefore needs to be
     * in this file instead of in the super class, BaseObject.
     *
     * @param dbName
     * @throws TorqueException
     */
    public void save(String dbName) throws TorqueException
    {
        Connection con = null;
        try
        {
            con = Transaction.begin(dbName);
            save(con);
            Transaction.commit(con);
        }
        catch(TorqueException e)
        {
            Transaction.safeRollback(con);
            throw e;
        }
    }

    /** flag to prevent endless save loop, if this object is referenced
        by another object which falls in this transaction. */
    private boolean alreadyInSave = false;
    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.  This method
     * is meant to be used as part of a transaction, otherwise use
     * the save() method and the connection details will be handled
     * internally
     *
     * @param con
     * @throws TorqueException
     */
    public void save(Connection con) throws TorqueException
    {
        if (!alreadyInSave)
        {
            alreadyInSave = true;



            // If this object has been modified, then save it to the database.
            if (isModified())
            {
                if (isNew())
                {
                    TOutlineTemplatePeer.doInsert((TOutlineTemplate) this, con);
                    setNew(false);
                }
                else
                {
                    TOutlineTemplatePeer.doUpdate((TOutlineTemplate) this, con);
                }
            }


            if (collTOutlineCodes != null)
            {
                for (int i = 0; i < collTOutlineCodes.size(); i++)
                {
                    ((TOutlineCode) collTOutlineCodes.get(i)).save(con);
                }
            }

            if (collTOutlineTemplateDefs != null)
            {
                for (int i = 0; i < collTOutlineTemplateDefs.size(); i++)
                {
                    ((TOutlineTemplateDef) collTOutlineTemplateDefs.get(i)).save(con);
                }
            }
            alreadyInSave = false;
        }
    }


    /**
     * Set the PrimaryKey using ObjectKey.
     *
     * @param key objectID ObjectKey
     */
    public void setPrimaryKey(ObjectKey key)
        throws TorqueException
    {
        setObjectID(new Integer(((NumberKey) key).intValue()));
    }

    /**
     * Set the PrimaryKey using a String.
     *
     * @param key
     */
    public void setPrimaryKey(String key) throws TorqueException
    {
        setObjectID(new Integer(key));
    }


    /**
     * returns an id that differentiates this object from others
     * of its class.
     */
    public ObjectKey getPrimaryKey()
    {
        return SimpleKey.keyFor(getObjectID());
    }
 

    /**
     * Makes a copy of this object.
     * It creates a new object filling in the simple attributes.
     * It then fills all the association collections and sets the
     * related objects to isNew=true.
     */
    public TOutlineTemplate copy() throws TorqueException
    {
        return copy(true);
    }

    /**
     * Makes a copy of this object using connection.
     * It creates a new object filling in the simple attributes.
     * It then fills all the association collections and sets the
     * related objects to isNew=true.
     *
     * @param con the database connection to read associated objects.
     */
    public TOutlineTemplate copy(Connection con) throws TorqueException
    {
        return copy(true, con);
    }

    /**
     * Makes a copy of this object.
     * It creates a new object filling in the simple attributes.
     * If the parameter deepcopy is true, it then fills all the
     * association collections and sets the related objects to
     * isNew=true.
     *
     * @param deepcopy whether to copy the associated objects.
     */
    public TOutlineTemplate copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TOutlineTemplate(), deepcopy);
    }

    /**
     * Makes a copy of this object using connection.
     * It creates a new object filling in the simple attributes.
     * If the parameter deepcopy is true, it then fills all the
     * association collections and sets the related objects to
     * isNew=true.
     *
     * @param deepcopy whether to copy the associated objects.
     * @param con the database connection to read associated objects.
     */
    public TOutlineTemplate copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TOutlineTemplate(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TOutlineTemplate copyInto(TOutlineTemplate copyObj) throws TorqueException
    {
        return copyInto(copyObj, true);
    }

  
    /**
     * Fills the copyObj with the contents of this object using connection.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     * @param con the database connection to read associated objects.
     */
    protected TOutlineTemplate copyInto(TOutlineTemplate copyObj, Connection con) throws TorqueException
    {
        return copyInto(copyObj, true, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * If deepcopy is true, The associated objects are also copied
     * and treated as new objects.
     *
     * @param copyObj the object to fill.
     * @param deepcopy whether the associated objects should be copied.
     */
    protected TOutlineTemplate copyInto(TOutlineTemplate copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setEntityType(entityType);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TOutlineCode> vTOutlineCodes = getTOutlineCodes();
        if (vTOutlineCodes != null)
        {
            for (int i = 0; i < vTOutlineCodes.size(); i++)
            {
                TOutlineCode obj =  vTOutlineCodes.get(i);
                copyObj.addTOutlineCode(obj.copy());
            }
        }
        else
        {
            copyObj.collTOutlineCodes = null;
        }


        List<TOutlineTemplateDef> vTOutlineTemplateDefs = getTOutlineTemplateDefs();
        if (vTOutlineTemplateDefs != null)
        {
            for (int i = 0; i < vTOutlineTemplateDefs.size(); i++)
            {
                TOutlineTemplateDef obj =  vTOutlineTemplateDefs.get(i);
                copyObj.addTOutlineTemplateDef(obj.copy());
            }
        }
        else
        {
            copyObj.collTOutlineTemplateDefs = null;
        }
        }
        return copyObj;
    }
        
    
    /**
     * Fills the copyObj with the contents of this object using connection.
     * If deepcopy is true, The associated objects are also copied
     * and treated as new objects.
     *
     * @param copyObj the object to fill.
     * @param deepcopy whether the associated objects should be copied.
     * @param con the database connection to read associated objects.
     */
    protected TOutlineTemplate copyInto(TOutlineTemplate copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setEntityType(entityType);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TOutlineCode> vTOutlineCodes = getTOutlineCodes(con);
        if (vTOutlineCodes != null)
        {
            for (int i = 0; i < vTOutlineCodes.size(); i++)
            {
                TOutlineCode obj =  vTOutlineCodes.get(i);
                copyObj.addTOutlineCode(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTOutlineCodes = null;
        }


        List<TOutlineTemplateDef> vTOutlineTemplateDefs = getTOutlineTemplateDefs(con);
        if (vTOutlineTemplateDefs != null)
        {
            for (int i = 0; i < vTOutlineTemplateDefs.size(); i++)
            {
                TOutlineTemplateDef obj =  vTOutlineTemplateDefs.get(i);
                copyObj.addTOutlineTemplateDef(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTOutlineTemplateDefs = null;
        }
        }
        return copyObj;
    }
    
    

    /**
     * returns a peer instance associated with this om.  Since Peer classes
     * are not to have any instance attributes, this method returns the
     * same instance for all member of this class. The method could therefore
     * be static, but this would prevent one from overriding the behavior.
     */
    public TOutlineTemplatePeer getPeer()
    {
        return peer;
    }

    /**
     * Retrieves the TableMap object related to this Table data without
     * compiler warnings of using getPeer().getTableMap().
     *
     * @return The associated TableMap object.
     */
    public TableMap getTableMap() throws TorqueException
    {
        return TOutlineTemplatePeer.getTableMap();
    }

  
    /**
     * Creates a TOutlineTemplateBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TOutlineTemplateBean with the contents of this object
     */
    public TOutlineTemplateBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TOutlineTemplateBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TOutlineTemplateBean with the contents of this object
     */
    public TOutlineTemplateBean getBean(IdentityMap createdBeans)
    {
        TOutlineTemplateBean result = (TOutlineTemplateBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TOutlineTemplateBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setLabel(getLabel());
        result.setEntityType(getEntityType());
        result.setUuid(getUuid());



        if (collTOutlineCodes != null)
        {
            List<TOutlineCodeBean> relatedBeans = new ArrayList<TOutlineCodeBean>(collTOutlineCodes.size());
            for (Iterator<TOutlineCode> collTOutlineCodesIt = collTOutlineCodes.iterator(); collTOutlineCodesIt.hasNext(); )
            {
                TOutlineCode related = (TOutlineCode) collTOutlineCodesIt.next();
                TOutlineCodeBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTOutlineCodeBeans(relatedBeans);
        }


        if (collTOutlineTemplateDefs != null)
        {
            List<TOutlineTemplateDefBean> relatedBeans = new ArrayList<TOutlineTemplateDefBean>(collTOutlineTemplateDefs.size());
            for (Iterator<TOutlineTemplateDef> collTOutlineTemplateDefsIt = collTOutlineTemplateDefs.iterator(); collTOutlineTemplateDefsIt.hasNext(); )
            {
                TOutlineTemplateDef related = (TOutlineTemplateDef) collTOutlineTemplateDefsIt.next();
                TOutlineTemplateDefBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTOutlineTemplateDefBeans(relatedBeans);
        }

        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TOutlineTemplate with the contents
     * of a TOutlineTemplateBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TOutlineTemplateBean which contents are used to create
     *        the resulting class
     * @return an instance of TOutlineTemplate with the contents of bean
     */
    public static TOutlineTemplate createTOutlineTemplate(TOutlineTemplateBean bean)
        throws TorqueException
    {
        return createTOutlineTemplate(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TOutlineTemplate with the contents
     * of a TOutlineTemplateBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TOutlineTemplateBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TOutlineTemplate with the contents of bean
     */

    public static TOutlineTemplate createTOutlineTemplate(TOutlineTemplateBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TOutlineTemplate result = (TOutlineTemplate) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TOutlineTemplate();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setLabel(bean.getLabel());
        result.setEntityType(bean.getEntityType());
        result.setUuid(bean.getUuid());



        {
            List<TOutlineCodeBean> relatedBeans = bean.getTOutlineCodeBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TOutlineCodeBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TOutlineCodeBean relatedBean =  relatedBeansIt.next();
                    TOutlineCode related = TOutlineCode.createTOutlineCode(relatedBean, createdObjects);
                    result.addTOutlineCodeFromBean(related);
                }
            }
        }


        {
            List<TOutlineTemplateDefBean> relatedBeans = bean.getTOutlineTemplateDefBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TOutlineTemplateDefBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TOutlineTemplateDefBean relatedBean =  relatedBeansIt.next();
                    TOutlineTemplateDef related = TOutlineTemplateDef.createTOutlineTemplateDef(relatedBean, createdObjects);
                    result.addTOutlineTemplateDefFromBean(related);
                }
            }
        }

    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TOutlineCode object to this object.
     * through the TOutlineCode foreign key attribute
     *
     * @param toAdd TOutlineCode
     */
    protected void addTOutlineCodeFromBean(TOutlineCode toAdd)
    {
        initTOutlineCodes();
        collTOutlineCodes.add(toAdd);
    }


    /**
     * Method called to associate a TOutlineTemplateDef object to this object.
     * through the TOutlineTemplateDef foreign key attribute
     *
     * @param toAdd TOutlineTemplateDef
     */
    protected void addTOutlineTemplateDefFromBean(TOutlineTemplateDef toAdd)
    {
        initTOutlineTemplateDefs();
        collTOutlineTemplateDefs.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TOutlineTemplate:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("EntityType = ")
           .append(getEntityType())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
