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
import com.aurel.track.beans.TMailTemplateBean;

import com.aurel.track.beans.TMailTemplateConfigBean;
import com.aurel.track.beans.TMailTemplateDefBean;


/**
 * Locale independent template names for notification emails
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TMailTemplate
 */
public abstract class BaseTMailTemplate extends TpBaseObject
{
    /** The Peer class */
    private static final TMailTemplatePeer peer =
        new TMailTemplatePeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the templateType field */
    private Integer templateType;

    /** The value for the name field */
    private String name;

    /** The value for the description field */
    private String description;

    /** The value for the tagLabel field */
    private String tagLabel;

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



        // update associated TMailTemplateConfig
        if (collTMailTemplateConfigs != null)
        {
            for (int i = 0; i < collTMailTemplateConfigs.size(); i++)
            {
                ((TMailTemplateConfig) collTMailTemplateConfigs.get(i))
                        .setMailTemplate(v);
            }
        }

        // update associated TMailTemplateDef
        if (collTMailTemplateDefs != null)
        {
            for (int i = 0; i < collTMailTemplateDefs.size(); i++)
            {
                ((TMailTemplateDef) collTMailTemplateDefs.get(i))
                        .setMailTemplate(v);
            }
        }
    }

    /**
     * Get the TemplateType
     *
     * @return Integer
     */
    public Integer getTemplateType()
    {
        return templateType;
    }


    /**
     * Set the value of TemplateType
     *
     * @param v new value
     */
    public void setTemplateType(Integer v) 
    {

        if (!ObjectUtils.equals(this.templateType, v))
        {
            this.templateType = v;
            setModified(true);
        }


    }

    /**
     * Get the Name
     *
     * @return String
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set the value of Name
     *
     * @param v new value
     */
    public void setName(String v) 
    {

        if (!ObjectUtils.equals(this.name, v))
        {
            this.name = v;
            setModified(true);
        }


    }

    /**
     * Get the Description
     *
     * @return String
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set the value of Description
     *
     * @param v new value
     */
    public void setDescription(String v) 
    {

        if (!ObjectUtils.equals(this.description, v))
        {
            this.description = v;
            setModified(true);
        }


    }

    /**
     * Get the TagLabel
     *
     * @return String
     */
    public String getTagLabel()
    {
        return tagLabel;
    }


    /**
     * Set the value of TagLabel
     *
     * @param v new value
     */
    public void setTagLabel(String v) 
    {

        if (!ObjectUtils.equals(this.tagLabel, v))
        {
            this.tagLabel = v;
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
     * Collection to store aggregation of collTMailTemplateConfigs
     */
    protected List<TMailTemplateConfig> collTMailTemplateConfigs;

    /**
     * Temporary storage of collTMailTemplateConfigs to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTMailTemplateConfigs()
    {
        if (collTMailTemplateConfigs == null)
        {
            collTMailTemplateConfigs = new ArrayList<TMailTemplateConfig>();
        }
    }


    /**
     * Method called to associate a TMailTemplateConfig object to this object
     * through the TMailTemplateConfig foreign key attribute
     *
     * @param l TMailTemplateConfig
     * @throws TorqueException
     */
    public void addTMailTemplateConfig(TMailTemplateConfig l) throws TorqueException
    {
        getTMailTemplateConfigs().add(l);
        l.setTMailTemplate((TMailTemplate) this);
    }

    /**
     * Method called to associate a TMailTemplateConfig object to this object
     * through the TMailTemplateConfig foreign key attribute using connection.
     *
     * @param l TMailTemplateConfig
     * @throws TorqueException
     */
    public void addTMailTemplateConfig(TMailTemplateConfig l, Connection con) throws TorqueException
    {
        getTMailTemplateConfigs(con).add(l);
        l.setTMailTemplate((TMailTemplate) this);
    }

    /**
     * The criteria used to select the current contents of collTMailTemplateConfigs
     */
    private Criteria lastTMailTemplateConfigsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTMailTemplateConfigs(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TMailTemplateConfig> getTMailTemplateConfigs()
        throws TorqueException
    {
        if (collTMailTemplateConfigs == null)
        {
            collTMailTemplateConfigs = getTMailTemplateConfigs(new Criteria(10));
        }
        return collTMailTemplateConfigs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TMailTemplate has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     * If this TMailTemplate is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TMailTemplateConfig> getTMailTemplateConfigs(Criteria criteria) throws TorqueException
    {
        if (collTMailTemplateConfigs == null)
        {
            if (isNew())
            {
               collTMailTemplateConfigs = new ArrayList<TMailTemplateConfig>();
            }
            else
            {
                criteria.add(TMailTemplateConfigPeer.MAILTEMPLATE, getObjectID() );
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelect(criteria);
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
                criteria.add(TMailTemplateConfigPeer.MAILTEMPLATE, getObjectID());
                if (!lastTMailTemplateConfigsCriteria.equals(criteria))
                {
                    collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelect(criteria);
                }
            }
        }
        lastTMailTemplateConfigsCriteria = criteria;

        return collTMailTemplateConfigs;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTMailTemplateConfigs(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TMailTemplateConfig> getTMailTemplateConfigs(Connection con) throws TorqueException
    {
        if (collTMailTemplateConfigs == null)
        {
            collTMailTemplateConfigs = getTMailTemplateConfigs(new Criteria(10), con);
        }
        return collTMailTemplateConfigs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TMailTemplate has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     * If this TMailTemplate is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TMailTemplateConfig> getTMailTemplateConfigs(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTMailTemplateConfigs == null)
        {
            if (isNew())
            {
               collTMailTemplateConfigs = new ArrayList<TMailTemplateConfig>();
            }
            else
            {
                 criteria.add(TMailTemplateConfigPeer.MAILTEMPLATE, getObjectID());
                 collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelect(criteria, con);
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
                 criteria.add(TMailTemplateConfigPeer.MAILTEMPLATE, getObjectID());
                 if (!lastTMailTemplateConfigsCriteria.equals(criteria))
                 {
                     collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTMailTemplateConfigsCriteria = criteria;

         return collTMailTemplateConfigs;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TMailTemplate is new, it will return
     * an empty collection; or if this TMailTemplate has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TMailTemplate.
     */
    protected List<TMailTemplateConfig> getTMailTemplateConfigsJoinTMailTemplate(Criteria criteria)
        throws TorqueException
    {
        if (collTMailTemplateConfigs == null)
        {
            if (isNew())
            {
               collTMailTemplateConfigs = new ArrayList<TMailTemplateConfig>();
            }
            else
            {
                criteria.add(TMailTemplateConfigPeer.MAILTEMPLATE, getObjectID());
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTMailTemplate(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TMailTemplateConfigPeer.MAILTEMPLATE, getObjectID());
            if (!lastTMailTemplateConfigsCriteria.equals(criteria))
            {
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTMailTemplate(criteria);
            }
        }
        lastTMailTemplateConfigsCriteria = criteria;

        return collTMailTemplateConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TMailTemplate is new, it will return
     * an empty collection; or if this TMailTemplate has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TMailTemplate.
     */
    protected List<TMailTemplateConfig> getTMailTemplateConfigsJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTMailTemplateConfigs == null)
        {
            if (isNew())
            {
               collTMailTemplateConfigs = new ArrayList<TMailTemplateConfig>();
            }
            else
            {
                criteria.add(TMailTemplateConfigPeer.MAILTEMPLATE, getObjectID());
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TMailTemplateConfigPeer.MAILTEMPLATE, getObjectID());
            if (!lastTMailTemplateConfigsCriteria.equals(criteria))
            {
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTMailTemplateConfigsCriteria = criteria;

        return collTMailTemplateConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TMailTemplate is new, it will return
     * an empty collection; or if this TMailTemplate has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TMailTemplate.
     */
    protected List<TMailTemplateConfig> getTMailTemplateConfigsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTMailTemplateConfigs == null)
        {
            if (isNew())
            {
               collTMailTemplateConfigs = new ArrayList<TMailTemplateConfig>();
            }
            else
            {
                criteria.add(TMailTemplateConfigPeer.MAILTEMPLATE, getObjectID());
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TMailTemplateConfigPeer.MAILTEMPLATE, getObjectID());
            if (!lastTMailTemplateConfigsCriteria.equals(criteria))
            {
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTMailTemplateConfigsCriteria = criteria;

        return collTMailTemplateConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TMailTemplate is new, it will return
     * an empty collection; or if this TMailTemplate has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TMailTemplate.
     */
    protected List<TMailTemplateConfig> getTMailTemplateConfigsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTMailTemplateConfigs == null)
        {
            if (isNew())
            {
               collTMailTemplateConfigs = new ArrayList<TMailTemplateConfig>();
            }
            else
            {
                criteria.add(TMailTemplateConfigPeer.MAILTEMPLATE, getObjectID());
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TMailTemplateConfigPeer.MAILTEMPLATE, getObjectID());
            if (!lastTMailTemplateConfigsCriteria.equals(criteria))
            {
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTMailTemplateConfigsCriteria = criteria;

        return collTMailTemplateConfigs;
    }





    /**
     * Collection to store aggregation of collTMailTemplateDefs
     */
    protected List<TMailTemplateDef> collTMailTemplateDefs;

    /**
     * Temporary storage of collTMailTemplateDefs to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTMailTemplateDefs()
    {
        if (collTMailTemplateDefs == null)
        {
            collTMailTemplateDefs = new ArrayList<TMailTemplateDef>();
        }
    }


    /**
     * Method called to associate a TMailTemplateDef object to this object
     * through the TMailTemplateDef foreign key attribute
     *
     * @param l TMailTemplateDef
     * @throws TorqueException
     */
    public void addTMailTemplateDef(TMailTemplateDef l) throws TorqueException
    {
        getTMailTemplateDefs().add(l);
        l.setTMailTemplate((TMailTemplate) this);
    }

    /**
     * Method called to associate a TMailTemplateDef object to this object
     * through the TMailTemplateDef foreign key attribute using connection.
     *
     * @param l TMailTemplateDef
     * @throws TorqueException
     */
    public void addTMailTemplateDef(TMailTemplateDef l, Connection con) throws TorqueException
    {
        getTMailTemplateDefs(con).add(l);
        l.setTMailTemplate((TMailTemplate) this);
    }

    /**
     * The criteria used to select the current contents of collTMailTemplateDefs
     */
    private Criteria lastTMailTemplateDefsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTMailTemplateDefs(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TMailTemplateDef> getTMailTemplateDefs()
        throws TorqueException
    {
        if (collTMailTemplateDefs == null)
        {
            collTMailTemplateDefs = getTMailTemplateDefs(new Criteria(10));
        }
        return collTMailTemplateDefs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TMailTemplate has previously
     * been saved, it will retrieve related TMailTemplateDefs from storage.
     * If this TMailTemplate is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TMailTemplateDef> getTMailTemplateDefs(Criteria criteria) throws TorqueException
    {
        if (collTMailTemplateDefs == null)
        {
            if (isNew())
            {
               collTMailTemplateDefs = new ArrayList<TMailTemplateDef>();
            }
            else
            {
                criteria.add(TMailTemplateDefPeer.MAILTEMPLATE, getObjectID() );
                collTMailTemplateDefs = TMailTemplateDefPeer.doSelect(criteria);
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
                criteria.add(TMailTemplateDefPeer.MAILTEMPLATE, getObjectID());
                if (!lastTMailTemplateDefsCriteria.equals(criteria))
                {
                    collTMailTemplateDefs = TMailTemplateDefPeer.doSelect(criteria);
                }
            }
        }
        lastTMailTemplateDefsCriteria = criteria;

        return collTMailTemplateDefs;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTMailTemplateDefs(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TMailTemplateDef> getTMailTemplateDefs(Connection con) throws TorqueException
    {
        if (collTMailTemplateDefs == null)
        {
            collTMailTemplateDefs = getTMailTemplateDefs(new Criteria(10), con);
        }
        return collTMailTemplateDefs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TMailTemplate has previously
     * been saved, it will retrieve related TMailTemplateDefs from storage.
     * If this TMailTemplate is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TMailTemplateDef> getTMailTemplateDefs(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTMailTemplateDefs == null)
        {
            if (isNew())
            {
               collTMailTemplateDefs = new ArrayList<TMailTemplateDef>();
            }
            else
            {
                 criteria.add(TMailTemplateDefPeer.MAILTEMPLATE, getObjectID());
                 collTMailTemplateDefs = TMailTemplateDefPeer.doSelect(criteria, con);
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
                 criteria.add(TMailTemplateDefPeer.MAILTEMPLATE, getObjectID());
                 if (!lastTMailTemplateDefsCriteria.equals(criteria))
                 {
                     collTMailTemplateDefs = TMailTemplateDefPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTMailTemplateDefsCriteria = criteria;

         return collTMailTemplateDefs;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TMailTemplate is new, it will return
     * an empty collection; or if this TMailTemplate has previously
     * been saved, it will retrieve related TMailTemplateDefs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TMailTemplate.
     */
    protected List<TMailTemplateDef> getTMailTemplateDefsJoinTMailTemplate(Criteria criteria)
        throws TorqueException
    {
        if (collTMailTemplateDefs == null)
        {
            if (isNew())
            {
               collTMailTemplateDefs = new ArrayList<TMailTemplateDef>();
            }
            else
            {
                criteria.add(TMailTemplateDefPeer.MAILTEMPLATE, getObjectID());
                collTMailTemplateDefs = TMailTemplateDefPeer.doSelectJoinTMailTemplate(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TMailTemplateDefPeer.MAILTEMPLATE, getObjectID());
            if (!lastTMailTemplateDefsCriteria.equals(criteria))
            {
                collTMailTemplateDefs = TMailTemplateDefPeer.doSelectJoinTMailTemplate(criteria);
            }
        }
        lastTMailTemplateDefsCriteria = criteria;

        return collTMailTemplateDefs;
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
            fieldNames.add("TemplateType");
            fieldNames.add("Name");
            fieldNames.add("Description");
            fieldNames.add("TagLabel");
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
        if (name.equals("TemplateType"))
        {
            return getTemplateType();
        }
        if (name.equals("Name"))
        {
            return getName();
        }
        if (name.equals("Description"))
        {
            return getDescription();
        }
        if (name.equals("TagLabel"))
        {
            return getTagLabel();
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
        if (name.equals("TemplateType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTemplateType((Integer) value);
            return true;
        }
        if (name.equals("Name"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setName((String) value);
            return true;
        }
        if (name.equals("Description"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDescription((String) value);
            return true;
        }
        if (name.equals("TagLabel"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTagLabel((String) value);
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
        if (name.equals(TMailTemplatePeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TMailTemplatePeer.TEMPLATETYPE))
        {
            return getTemplateType();
        }
        if (name.equals(TMailTemplatePeer.NAME))
        {
            return getName();
        }
        if (name.equals(TMailTemplatePeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TMailTemplatePeer.TAGLABEL))
        {
            return getTagLabel();
        }
        if (name.equals(TMailTemplatePeer.TPUUID))
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
      if (TMailTemplatePeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TMailTemplatePeer.TEMPLATETYPE.equals(name))
        {
            return setByName("TemplateType", value);
        }
      if (TMailTemplatePeer.NAME.equals(name))
        {
            return setByName("Name", value);
        }
      if (TMailTemplatePeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TMailTemplatePeer.TAGLABEL.equals(name))
        {
            return setByName("TagLabel", value);
        }
      if (TMailTemplatePeer.TPUUID.equals(name))
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
            return getTemplateType();
        }
        if (pos == 2)
        {
            return getName();
        }
        if (pos == 3)
        {
            return getDescription();
        }
        if (pos == 4)
        {
            return getTagLabel();
        }
        if (pos == 5)
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
            return setByName("TemplateType", value);
        }
    if (position == 2)
        {
            return setByName("Name", value);
        }
    if (position == 3)
        {
            return setByName("Description", value);
        }
    if (position == 4)
        {
            return setByName("TagLabel", value);
        }
    if (position == 5)
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
        save(TMailTemplatePeer.DATABASE_NAME);
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
                    TMailTemplatePeer.doInsert((TMailTemplate) this, con);
                    setNew(false);
                }
                else
                {
                    TMailTemplatePeer.doUpdate((TMailTemplate) this, con);
                }
            }


            if (collTMailTemplateConfigs != null)
            {
                for (int i = 0; i < collTMailTemplateConfigs.size(); i++)
                {
                    ((TMailTemplateConfig) collTMailTemplateConfigs.get(i)).save(con);
                }
            }

            if (collTMailTemplateDefs != null)
            {
                for (int i = 0; i < collTMailTemplateDefs.size(); i++)
                {
                    ((TMailTemplateDef) collTMailTemplateDefs.get(i)).save(con);
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
    public TMailTemplate copy() throws TorqueException
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
    public TMailTemplate copy(Connection con) throws TorqueException
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
    public TMailTemplate copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TMailTemplate(), deepcopy);
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
    public TMailTemplate copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TMailTemplate(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TMailTemplate copyInto(TMailTemplate copyObj) throws TorqueException
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
    protected TMailTemplate copyInto(TMailTemplate copyObj, Connection con) throws TorqueException
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
    protected TMailTemplate copyInto(TMailTemplate copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setTemplateType(templateType);
        copyObj.setName(name);
        copyObj.setDescription(description);
        copyObj.setTagLabel(tagLabel);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TMailTemplateConfig> vTMailTemplateConfigs = getTMailTemplateConfigs();
        if (vTMailTemplateConfigs != null)
        {
            for (int i = 0; i < vTMailTemplateConfigs.size(); i++)
            {
                TMailTemplateConfig obj =  vTMailTemplateConfigs.get(i);
                copyObj.addTMailTemplateConfig(obj.copy());
            }
        }
        else
        {
            copyObj.collTMailTemplateConfigs = null;
        }


        List<TMailTemplateDef> vTMailTemplateDefs = getTMailTemplateDefs();
        if (vTMailTemplateDefs != null)
        {
            for (int i = 0; i < vTMailTemplateDefs.size(); i++)
            {
                TMailTemplateDef obj =  vTMailTemplateDefs.get(i);
                copyObj.addTMailTemplateDef(obj.copy());
            }
        }
        else
        {
            copyObj.collTMailTemplateDefs = null;
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
    protected TMailTemplate copyInto(TMailTemplate copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setTemplateType(templateType);
        copyObj.setName(name);
        copyObj.setDescription(description);
        copyObj.setTagLabel(tagLabel);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TMailTemplateConfig> vTMailTemplateConfigs = getTMailTemplateConfigs(con);
        if (vTMailTemplateConfigs != null)
        {
            for (int i = 0; i < vTMailTemplateConfigs.size(); i++)
            {
                TMailTemplateConfig obj =  vTMailTemplateConfigs.get(i);
                copyObj.addTMailTemplateConfig(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTMailTemplateConfigs = null;
        }


        List<TMailTemplateDef> vTMailTemplateDefs = getTMailTemplateDefs(con);
        if (vTMailTemplateDefs != null)
        {
            for (int i = 0; i < vTMailTemplateDefs.size(); i++)
            {
                TMailTemplateDef obj =  vTMailTemplateDefs.get(i);
                copyObj.addTMailTemplateDef(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTMailTemplateDefs = null;
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
    public TMailTemplatePeer getPeer()
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
        return TMailTemplatePeer.getTableMap();
    }

  
    /**
     * Creates a TMailTemplateBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TMailTemplateBean with the contents of this object
     */
    public TMailTemplateBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TMailTemplateBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TMailTemplateBean with the contents of this object
     */
    public TMailTemplateBean getBean(IdentityMap createdBeans)
    {
        TMailTemplateBean result = (TMailTemplateBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TMailTemplateBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setTemplateType(getTemplateType());
        result.setName(getName());
        result.setDescription(getDescription());
        result.setTagLabel(getTagLabel());
        result.setUuid(getUuid());



        if (collTMailTemplateConfigs != null)
        {
            List<TMailTemplateConfigBean> relatedBeans = new ArrayList<TMailTemplateConfigBean>(collTMailTemplateConfigs.size());
            for (Iterator<TMailTemplateConfig> collTMailTemplateConfigsIt = collTMailTemplateConfigs.iterator(); collTMailTemplateConfigsIt.hasNext(); )
            {
                TMailTemplateConfig related = (TMailTemplateConfig) collTMailTemplateConfigsIt.next();
                TMailTemplateConfigBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTMailTemplateConfigBeans(relatedBeans);
        }


        if (collTMailTemplateDefs != null)
        {
            List<TMailTemplateDefBean> relatedBeans = new ArrayList<TMailTemplateDefBean>(collTMailTemplateDefs.size());
            for (Iterator<TMailTemplateDef> collTMailTemplateDefsIt = collTMailTemplateDefs.iterator(); collTMailTemplateDefsIt.hasNext(); )
            {
                TMailTemplateDef related = (TMailTemplateDef) collTMailTemplateDefsIt.next();
                TMailTemplateDefBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTMailTemplateDefBeans(relatedBeans);
        }

        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TMailTemplate with the contents
     * of a TMailTemplateBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TMailTemplateBean which contents are used to create
     *        the resulting class
     * @return an instance of TMailTemplate with the contents of bean
     */
    public static TMailTemplate createTMailTemplate(TMailTemplateBean bean)
        throws TorqueException
    {
        return createTMailTemplate(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TMailTemplate with the contents
     * of a TMailTemplateBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TMailTemplateBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TMailTemplate with the contents of bean
     */

    public static TMailTemplate createTMailTemplate(TMailTemplateBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TMailTemplate result = (TMailTemplate) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TMailTemplate();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setTemplateType(bean.getTemplateType());
        result.setName(bean.getName());
        result.setDescription(bean.getDescription());
        result.setTagLabel(bean.getTagLabel());
        result.setUuid(bean.getUuid());



        {
            List<TMailTemplateConfigBean> relatedBeans = bean.getTMailTemplateConfigBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TMailTemplateConfigBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TMailTemplateConfigBean relatedBean =  relatedBeansIt.next();
                    TMailTemplateConfig related = TMailTemplateConfig.createTMailTemplateConfig(relatedBean, createdObjects);
                    result.addTMailTemplateConfigFromBean(related);
                }
            }
        }


        {
            List<TMailTemplateDefBean> relatedBeans = bean.getTMailTemplateDefBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TMailTemplateDefBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TMailTemplateDefBean relatedBean =  relatedBeansIt.next();
                    TMailTemplateDef related = TMailTemplateDef.createTMailTemplateDef(relatedBean, createdObjects);
                    result.addTMailTemplateDefFromBean(related);
                }
            }
        }

    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TMailTemplateConfig object to this object.
     * through the TMailTemplateConfig foreign key attribute
     *
     * @param toAdd TMailTemplateConfig
     */
    protected void addTMailTemplateConfigFromBean(TMailTemplateConfig toAdd)
    {
        initTMailTemplateConfigs();
        collTMailTemplateConfigs.add(toAdd);
    }


    /**
     * Method called to associate a TMailTemplateDef object to this object.
     * through the TMailTemplateDef foreign key attribute
     *
     * @param toAdd TMailTemplateDef
     */
    protected void addTMailTemplateDefFromBean(TMailTemplateDef toAdd)
    {
        initTMailTemplateDefs();
        collTMailTemplateDefs.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TMailTemplate:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("TemplateType = ")
           .append(getTemplateType())
           .append("\n");
        str.append("Name = ")
           .append(getName())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("TagLabel = ")
           .append(getTagLabel())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
