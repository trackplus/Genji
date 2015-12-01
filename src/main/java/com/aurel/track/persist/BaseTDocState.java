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



import com.aurel.track.persist.TProjectType;
import com.aurel.track.persist.TProjectTypePeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TDocStateBean;
import com.aurel.track.beans.TProjectTypeBean;

import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TAttachmentVersionBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TDocState
 */
public abstract class BaseTDocState extends TpBaseObject
{
    /** The Peer class */
    private static final TDocStatePeer peer =
        new TDocStatePeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the label field */
    private String label;

    /** The value for the stateFlag field */
    private Integer stateFlag;

    /** The value for the projectType field */
    private Integer projectType;

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



        // update associated TAttachment
        if (collTAttachments != null)
        {
            for (int i = 0; i < collTAttachments.size(); i++)
            {
                ((TAttachment) collTAttachments.get(i))
                        .setDocumentState(v);
            }
        }

        // update associated TAttachmentVersion
        if (collTAttachmentVersions != null)
        {
            for (int i = 0; i < collTAttachmentVersions.size(); i++)
            {
                ((TAttachmentVersion) collTAttachmentVersions.get(i))
                        .setDocumentState(v);
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
     * Get the StateFlag
     *
     * @return Integer
     */
    public Integer getStateFlag()
    {
        return stateFlag;
    }


    /**
     * Set the value of StateFlag
     *
     * @param v new value
     */
    public void setStateFlag(Integer v) 
    {

        if (!ObjectUtils.equals(this.stateFlag, v))
        {
            this.stateFlag = v;
            setModified(true);
        }


    }

    /**
     * Get the ProjectType
     *
     * @return Integer
     */
    public Integer getProjectType()
    {
        return projectType;
    }


    /**
     * Set the value of ProjectType
     *
     * @param v new value
     */
    public void setProjectType(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.projectType, v))
        {
            this.projectType = v;
            setModified(true);
        }


        if (aTProjectType != null && !ObjectUtils.equals(aTProjectType.getObjectID(), v))
        {
            aTProjectType = null;
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

    



    private TProjectType aTProjectType;

    /**
     * Declares an association between this object and a TProjectType object
     *
     * @param v TProjectType
     * @throws TorqueException
     */
    public void setTProjectType(TProjectType v) throws TorqueException
    {
        if (v == null)
        {
            setProjectType((Integer) null);
        }
        else
        {
            setProjectType(v.getObjectID());
        }
        aTProjectType = v;
    }


    /**
     * Returns the associated TProjectType object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TProjectType object
     * @throws TorqueException
     */
    public TProjectType getTProjectType()
        throws TorqueException
    {
        if (aTProjectType == null && (!ObjectUtils.equals(this.projectType, null)))
        {
            aTProjectType = TProjectTypePeer.retrieveByPK(SimpleKey.keyFor(this.projectType));
        }
        return aTProjectType;
    }

    /**
     * Return the associated TProjectType object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TProjectType object
     * @throws TorqueException
     */
    public TProjectType getTProjectType(Connection connection)
        throws TorqueException
    {
        if (aTProjectType == null && (!ObjectUtils.equals(this.projectType, null)))
        {
            aTProjectType = TProjectTypePeer.retrieveByPK(SimpleKey.keyFor(this.projectType), connection);
        }
        return aTProjectType;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTProjectTypeKey(ObjectKey key) throws TorqueException
    {

        setProjectType(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTAttachments
     */
    protected List<TAttachment> collTAttachments;

    /**
     * Temporary storage of collTAttachments to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTAttachments()
    {
        if (collTAttachments == null)
        {
            collTAttachments = new ArrayList<TAttachment>();
        }
    }


    /**
     * Method called to associate a TAttachment object to this object
     * through the TAttachment foreign key attribute
     *
     * @param l TAttachment
     * @throws TorqueException
     */
    public void addTAttachment(TAttachment l) throws TorqueException
    {
        getTAttachments().add(l);
        l.setTDocState((TDocState) this);
    }

    /**
     * Method called to associate a TAttachment object to this object
     * through the TAttachment foreign key attribute using connection.
     *
     * @param l TAttachment
     * @throws TorqueException
     */
    public void addTAttachment(TAttachment l, Connection con) throws TorqueException
    {
        getTAttachments(con).add(l);
        l.setTDocState((TDocState) this);
    }

    /**
     * The criteria used to select the current contents of collTAttachments
     */
    private Criteria lastTAttachmentsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAttachments(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TAttachment> getTAttachments()
        throws TorqueException
    {
        if (collTAttachments == null)
        {
            collTAttachments = getTAttachments(new Criteria(10));
        }
        return collTAttachments;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDocState has previously
     * been saved, it will retrieve related TAttachments from storage.
     * If this TDocState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TAttachment> getTAttachments(Criteria criteria) throws TorqueException
    {
        if (collTAttachments == null)
        {
            if (isNew())
            {
               collTAttachments = new ArrayList<TAttachment>();
            }
            else
            {
                criteria.add(TAttachmentPeer.DOCUMENTSTATE, getObjectID() );
                collTAttachments = TAttachmentPeer.doSelect(criteria);
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
                criteria.add(TAttachmentPeer.DOCUMENTSTATE, getObjectID());
                if (!lastTAttachmentsCriteria.equals(criteria))
                {
                    collTAttachments = TAttachmentPeer.doSelect(criteria);
                }
            }
        }
        lastTAttachmentsCriteria = criteria;

        return collTAttachments;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAttachments(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAttachment> getTAttachments(Connection con) throws TorqueException
    {
        if (collTAttachments == null)
        {
            collTAttachments = getTAttachments(new Criteria(10), con);
        }
        return collTAttachments;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDocState has previously
     * been saved, it will retrieve related TAttachments from storage.
     * If this TDocState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAttachment> getTAttachments(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTAttachments == null)
        {
            if (isNew())
            {
               collTAttachments = new ArrayList<TAttachment>();
            }
            else
            {
                 criteria.add(TAttachmentPeer.DOCUMENTSTATE, getObjectID());
                 collTAttachments = TAttachmentPeer.doSelect(criteria, con);
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
                 criteria.add(TAttachmentPeer.DOCUMENTSTATE, getObjectID());
                 if (!lastTAttachmentsCriteria.equals(criteria))
                 {
                     collTAttachments = TAttachmentPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTAttachmentsCriteria = criteria;

         return collTAttachments;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDocState is new, it will return
     * an empty collection; or if this TDocState has previously
     * been saved, it will retrieve related TAttachments from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDocState.
     */
    protected List<TAttachment> getTAttachmentsJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTAttachments == null)
        {
            if (isNew())
            {
               collTAttachments = new ArrayList<TAttachment>();
            }
            else
            {
                criteria.add(TAttachmentPeer.DOCUMENTSTATE, getObjectID());
                collTAttachments = TAttachmentPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAttachmentPeer.DOCUMENTSTATE, getObjectID());
            if (!lastTAttachmentsCriteria.equals(criteria))
            {
                collTAttachments = TAttachmentPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTAttachmentsCriteria = criteria;

        return collTAttachments;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDocState is new, it will return
     * an empty collection; or if this TDocState has previously
     * been saved, it will retrieve related TAttachments from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDocState.
     */
    protected List<TAttachment> getTAttachmentsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTAttachments == null)
        {
            if (isNew())
            {
               collTAttachments = new ArrayList<TAttachment>();
            }
            else
            {
                criteria.add(TAttachmentPeer.DOCUMENTSTATE, getObjectID());
                collTAttachments = TAttachmentPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAttachmentPeer.DOCUMENTSTATE, getObjectID());
            if (!lastTAttachmentsCriteria.equals(criteria))
            {
                collTAttachments = TAttachmentPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTAttachmentsCriteria = criteria;

        return collTAttachments;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDocState is new, it will return
     * an empty collection; or if this TDocState has previously
     * been saved, it will retrieve related TAttachments from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDocState.
     */
    protected List<TAttachment> getTAttachmentsJoinTDocState(Criteria criteria)
        throws TorqueException
    {
        if (collTAttachments == null)
        {
            if (isNew())
            {
               collTAttachments = new ArrayList<TAttachment>();
            }
            else
            {
                criteria.add(TAttachmentPeer.DOCUMENTSTATE, getObjectID());
                collTAttachments = TAttachmentPeer.doSelectJoinTDocState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAttachmentPeer.DOCUMENTSTATE, getObjectID());
            if (!lastTAttachmentsCriteria.equals(criteria))
            {
                collTAttachments = TAttachmentPeer.doSelectJoinTDocState(criteria);
            }
        }
        lastTAttachmentsCriteria = criteria;

        return collTAttachments;
    }





    /**
     * Collection to store aggregation of collTAttachmentVersions
     */
    protected List<TAttachmentVersion> collTAttachmentVersions;

    /**
     * Temporary storage of collTAttachmentVersions to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTAttachmentVersions()
    {
        if (collTAttachmentVersions == null)
        {
            collTAttachmentVersions = new ArrayList<TAttachmentVersion>();
        }
    }


    /**
     * Method called to associate a TAttachmentVersion object to this object
     * through the TAttachmentVersion foreign key attribute
     *
     * @param l TAttachmentVersion
     * @throws TorqueException
     */
    public void addTAttachmentVersion(TAttachmentVersion l) throws TorqueException
    {
        getTAttachmentVersions().add(l);
        l.setTDocState((TDocState) this);
    }

    /**
     * Method called to associate a TAttachmentVersion object to this object
     * through the TAttachmentVersion foreign key attribute using connection.
     *
     * @param l TAttachmentVersion
     * @throws TorqueException
     */
    public void addTAttachmentVersion(TAttachmentVersion l, Connection con) throws TorqueException
    {
        getTAttachmentVersions(con).add(l);
        l.setTDocState((TDocState) this);
    }

    /**
     * The criteria used to select the current contents of collTAttachmentVersions
     */
    private Criteria lastTAttachmentVersionsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAttachmentVersions(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TAttachmentVersion> getTAttachmentVersions()
        throws TorqueException
    {
        if (collTAttachmentVersions == null)
        {
            collTAttachmentVersions = getTAttachmentVersions(new Criteria(10));
        }
        return collTAttachmentVersions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDocState has previously
     * been saved, it will retrieve related TAttachmentVersions from storage.
     * If this TDocState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TAttachmentVersion> getTAttachmentVersions(Criteria criteria) throws TorqueException
    {
        if (collTAttachmentVersions == null)
        {
            if (isNew())
            {
               collTAttachmentVersions = new ArrayList<TAttachmentVersion>();
            }
            else
            {
                criteria.add(TAttachmentVersionPeer.DOCUMENTSTATE, getObjectID() );
                collTAttachmentVersions = TAttachmentVersionPeer.doSelect(criteria);
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
                criteria.add(TAttachmentVersionPeer.DOCUMENTSTATE, getObjectID());
                if (!lastTAttachmentVersionsCriteria.equals(criteria))
                {
                    collTAttachmentVersions = TAttachmentVersionPeer.doSelect(criteria);
                }
            }
        }
        lastTAttachmentVersionsCriteria = criteria;

        return collTAttachmentVersions;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAttachmentVersions(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAttachmentVersion> getTAttachmentVersions(Connection con) throws TorqueException
    {
        if (collTAttachmentVersions == null)
        {
            collTAttachmentVersions = getTAttachmentVersions(new Criteria(10), con);
        }
        return collTAttachmentVersions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDocState has previously
     * been saved, it will retrieve related TAttachmentVersions from storage.
     * If this TDocState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAttachmentVersion> getTAttachmentVersions(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTAttachmentVersions == null)
        {
            if (isNew())
            {
               collTAttachmentVersions = new ArrayList<TAttachmentVersion>();
            }
            else
            {
                 criteria.add(TAttachmentVersionPeer.DOCUMENTSTATE, getObjectID());
                 collTAttachmentVersions = TAttachmentVersionPeer.doSelect(criteria, con);
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
                 criteria.add(TAttachmentVersionPeer.DOCUMENTSTATE, getObjectID());
                 if (!lastTAttachmentVersionsCriteria.equals(criteria))
                 {
                     collTAttachmentVersions = TAttachmentVersionPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTAttachmentVersionsCriteria = criteria;

         return collTAttachmentVersions;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDocState is new, it will return
     * an empty collection; or if this TDocState has previously
     * been saved, it will retrieve related TAttachmentVersions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDocState.
     */
    protected List<TAttachmentVersion> getTAttachmentVersionsJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTAttachmentVersions == null)
        {
            if (isNew())
            {
               collTAttachmentVersions = new ArrayList<TAttachmentVersion>();
            }
            else
            {
                criteria.add(TAttachmentVersionPeer.DOCUMENTSTATE, getObjectID());
                collTAttachmentVersions = TAttachmentVersionPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAttachmentVersionPeer.DOCUMENTSTATE, getObjectID());
            if (!lastTAttachmentVersionsCriteria.equals(criteria))
            {
                collTAttachmentVersions = TAttachmentVersionPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTAttachmentVersionsCriteria = criteria;

        return collTAttachmentVersions;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDocState is new, it will return
     * an empty collection; or if this TDocState has previously
     * been saved, it will retrieve related TAttachmentVersions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDocState.
     */
    protected List<TAttachmentVersion> getTAttachmentVersionsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTAttachmentVersions == null)
        {
            if (isNew())
            {
               collTAttachmentVersions = new ArrayList<TAttachmentVersion>();
            }
            else
            {
                criteria.add(TAttachmentVersionPeer.DOCUMENTSTATE, getObjectID());
                collTAttachmentVersions = TAttachmentVersionPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAttachmentVersionPeer.DOCUMENTSTATE, getObjectID());
            if (!lastTAttachmentVersionsCriteria.equals(criteria))
            {
                collTAttachmentVersions = TAttachmentVersionPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTAttachmentVersionsCriteria = criteria;

        return collTAttachmentVersions;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDocState is new, it will return
     * an empty collection; or if this TDocState has previously
     * been saved, it will retrieve related TAttachmentVersions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDocState.
     */
    protected List<TAttachmentVersion> getTAttachmentVersionsJoinTDocState(Criteria criteria)
        throws TorqueException
    {
        if (collTAttachmentVersions == null)
        {
            if (isNew())
            {
               collTAttachmentVersions = new ArrayList<TAttachmentVersion>();
            }
            else
            {
                criteria.add(TAttachmentVersionPeer.DOCUMENTSTATE, getObjectID());
                collTAttachmentVersions = TAttachmentVersionPeer.doSelectJoinTDocState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAttachmentVersionPeer.DOCUMENTSTATE, getObjectID());
            if (!lastTAttachmentVersionsCriteria.equals(criteria))
            {
                collTAttachmentVersions = TAttachmentVersionPeer.doSelectJoinTDocState(criteria);
            }
        }
        lastTAttachmentVersionsCriteria = criteria;

        return collTAttachmentVersions;
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
            fieldNames.add("StateFlag");
            fieldNames.add("ProjectType");
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
        if (name.equals("StateFlag"))
        {
            return getStateFlag();
        }
        if (name.equals("ProjectType"))
        {
            return getProjectType();
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
        if (name.equals("StateFlag"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setStateFlag((Integer) value);
            return true;
        }
        if (name.equals("ProjectType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setProjectType((Integer) value);
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
        if (name.equals(TDocStatePeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TDocStatePeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TDocStatePeer.STATEFLAG))
        {
            return getStateFlag();
        }
        if (name.equals(TDocStatePeer.PROJECTTYPE))
        {
            return getProjectType();
        }
        if (name.equals(TDocStatePeer.TPUUID))
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
      if (TDocStatePeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TDocStatePeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TDocStatePeer.STATEFLAG.equals(name))
        {
            return setByName("StateFlag", value);
        }
      if (TDocStatePeer.PROJECTTYPE.equals(name))
        {
            return setByName("ProjectType", value);
        }
      if (TDocStatePeer.TPUUID.equals(name))
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
            return getStateFlag();
        }
        if (pos == 3)
        {
            return getProjectType();
        }
        if (pos == 4)
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
            return setByName("StateFlag", value);
        }
    if (position == 3)
        {
            return setByName("ProjectType", value);
        }
    if (position == 4)
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
        save(TDocStatePeer.DATABASE_NAME);
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
                    TDocStatePeer.doInsert((TDocState) this, con);
                    setNew(false);
                }
                else
                {
                    TDocStatePeer.doUpdate((TDocState) this, con);
                }
            }


            if (collTAttachments != null)
            {
                for (int i = 0; i < collTAttachments.size(); i++)
                {
                    ((TAttachment) collTAttachments.get(i)).save(con);
                }
            }

            if (collTAttachmentVersions != null)
            {
                for (int i = 0; i < collTAttachmentVersions.size(); i++)
                {
                    ((TAttachmentVersion) collTAttachmentVersions.get(i)).save(con);
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
    public TDocState copy() throws TorqueException
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
    public TDocState copy(Connection con) throws TorqueException
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
    public TDocState copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TDocState(), deepcopy);
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
    public TDocState copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TDocState(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TDocState copyInto(TDocState copyObj) throws TorqueException
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
    protected TDocState copyInto(TDocState copyObj, Connection con) throws TorqueException
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
    protected TDocState copyInto(TDocState copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setStateFlag(stateFlag);
        copyObj.setProjectType(projectType);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TAttachment> vTAttachments = getTAttachments();
        if (vTAttachments != null)
        {
            for (int i = 0; i < vTAttachments.size(); i++)
            {
                TAttachment obj =  vTAttachments.get(i);
                copyObj.addTAttachment(obj.copy());
            }
        }
        else
        {
            copyObj.collTAttachments = null;
        }


        List<TAttachmentVersion> vTAttachmentVersions = getTAttachmentVersions();
        if (vTAttachmentVersions != null)
        {
            for (int i = 0; i < vTAttachmentVersions.size(); i++)
            {
                TAttachmentVersion obj =  vTAttachmentVersions.get(i);
                copyObj.addTAttachmentVersion(obj.copy());
            }
        }
        else
        {
            copyObj.collTAttachmentVersions = null;
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
    protected TDocState copyInto(TDocState copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setStateFlag(stateFlag);
        copyObj.setProjectType(projectType);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TAttachment> vTAttachments = getTAttachments(con);
        if (vTAttachments != null)
        {
            for (int i = 0; i < vTAttachments.size(); i++)
            {
                TAttachment obj =  vTAttachments.get(i);
                copyObj.addTAttachment(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTAttachments = null;
        }


        List<TAttachmentVersion> vTAttachmentVersions = getTAttachmentVersions(con);
        if (vTAttachmentVersions != null)
        {
            for (int i = 0; i < vTAttachmentVersions.size(); i++)
            {
                TAttachmentVersion obj =  vTAttachmentVersions.get(i);
                copyObj.addTAttachmentVersion(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTAttachmentVersions = null;
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
    public TDocStatePeer getPeer()
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
        return TDocStatePeer.getTableMap();
    }

  
    /**
     * Creates a TDocStateBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TDocStateBean with the contents of this object
     */
    public TDocStateBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TDocStateBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TDocStateBean with the contents of this object
     */
    public TDocStateBean getBean(IdentityMap createdBeans)
    {
        TDocStateBean result = (TDocStateBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TDocStateBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setLabel(getLabel());
        result.setStateFlag(getStateFlag());
        result.setProjectType(getProjectType());
        result.setUuid(getUuid());



        if (collTAttachments != null)
        {
            List<TAttachmentBean> relatedBeans = new ArrayList<TAttachmentBean>(collTAttachments.size());
            for (Iterator<TAttachment> collTAttachmentsIt = collTAttachments.iterator(); collTAttachmentsIt.hasNext(); )
            {
                TAttachment related = (TAttachment) collTAttachmentsIt.next();
                TAttachmentBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTAttachmentBeans(relatedBeans);
        }


        if (collTAttachmentVersions != null)
        {
            List<TAttachmentVersionBean> relatedBeans = new ArrayList<TAttachmentVersionBean>(collTAttachmentVersions.size());
            for (Iterator<TAttachmentVersion> collTAttachmentVersionsIt = collTAttachmentVersions.iterator(); collTAttachmentVersionsIt.hasNext(); )
            {
                TAttachmentVersion related = (TAttachmentVersion) collTAttachmentVersionsIt.next();
                TAttachmentVersionBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTAttachmentVersionBeans(relatedBeans);
        }




        if (aTProjectType != null)
        {
            TProjectTypeBean relatedBean = aTProjectType.getBean(createdBeans);
            result.setTProjectTypeBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TDocState with the contents
     * of a TDocStateBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TDocStateBean which contents are used to create
     *        the resulting class
     * @return an instance of TDocState with the contents of bean
     */
    public static TDocState createTDocState(TDocStateBean bean)
        throws TorqueException
    {
        return createTDocState(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TDocState with the contents
     * of a TDocStateBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TDocStateBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TDocState with the contents of bean
     */

    public static TDocState createTDocState(TDocStateBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TDocState result = (TDocState) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TDocState();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setLabel(bean.getLabel());
        result.setStateFlag(bean.getStateFlag());
        result.setProjectType(bean.getProjectType());
        result.setUuid(bean.getUuid());



        {
            List<TAttachmentBean> relatedBeans = bean.getTAttachmentBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TAttachmentBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TAttachmentBean relatedBean =  relatedBeansIt.next();
                    TAttachment related = TAttachment.createTAttachment(relatedBean, createdObjects);
                    result.addTAttachmentFromBean(related);
                }
            }
        }


        {
            List<TAttachmentVersionBean> relatedBeans = bean.getTAttachmentVersionBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TAttachmentVersionBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TAttachmentVersionBean relatedBean =  relatedBeansIt.next();
                    TAttachmentVersion related = TAttachmentVersion.createTAttachmentVersion(relatedBean, createdObjects);
                    result.addTAttachmentVersionFromBean(related);
                }
            }
        }




        {
            TProjectTypeBean relatedBean = bean.getTProjectTypeBean();
            if (relatedBean != null)
            {
                TProjectType relatedObject = TProjectType.createTProjectType(relatedBean, createdObjects);
                result.setTProjectType(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TAttachment object to this object.
     * through the TAttachment foreign key attribute
     *
     * @param toAdd TAttachment
     */
    protected void addTAttachmentFromBean(TAttachment toAdd)
    {
        initTAttachments();
        collTAttachments.add(toAdd);
    }


    /**
     * Method called to associate a TAttachmentVersion object to this object.
     * through the TAttachmentVersion foreign key attribute
     *
     * @param toAdd TAttachmentVersion
     */
    protected void addTAttachmentVersionFromBean(TAttachmentVersion toAdd)
    {
        initTAttachmentVersions();
        collTAttachmentVersions.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TDocState:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("StateFlag = ")
           .append(getStateFlag())
           .append("\n");
        str.append("ProjectType = ")
           .append(getProjectType())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
