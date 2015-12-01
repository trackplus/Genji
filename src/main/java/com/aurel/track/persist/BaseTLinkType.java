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



import com.aurel.track.persist.TBLOB;
import com.aurel.track.persist.TBLOBPeer;
import com.aurel.track.persist.TBLOB;
import com.aurel.track.persist.TBLOBPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TLinkTypeBean;
import com.aurel.track.beans.TBLOBBean;
import com.aurel.track.beans.TBLOBBean;

import com.aurel.track.beans.TWorkItemLinkBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TLinkType
 */
public abstract class BaseTLinkType extends TpBaseObject
{
    /** The Peer class */
    private static final TLinkTypePeer peer =
        new TLinkTypePeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the name field */
    private String name;

    /** The value for the reverseName field */
    private String reverseName;

    /** The value for the leftToRightFirst field */
    private String leftToRightFirst;

    /** The value for the leftToRightLevel field */
    private String leftToRightLevel;

    /** The value for the leftToRightAll field */
    private String leftToRightAll;

    /** The value for the rightToLeftFirst field */
    private String rightToLeftFirst;

    /** The value for the rightToLeftLevel field */
    private String rightToLeftLevel;

    /** The value for the rightToLeftAll field */
    private String rightToLeftAll;

    /** The value for the linkDirection field */
    private Integer linkDirection;

    /** The value for the outwardIconKey field */
    private Integer outwardIconKey;

    /** The value for the inwardIconKey field */
    private Integer inwardIconKey;

    /** The value for the linkTypePlugin field */
    private String linkTypePlugin;

    /** The value for the moreProps field */
    private String moreProps;

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



        // update associated TWorkItemLink
        if (collTWorkItemLinks != null)
        {
            for (int i = 0; i < collTWorkItemLinks.size(); i++)
            {
                ((TWorkItemLink) collTWorkItemLinks.get(i))
                        .setLinkType(v);
            }
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
     * Get the ReverseName
     *
     * @return String
     */
    public String getReverseName()
    {
        return reverseName;
    }


    /**
     * Set the value of ReverseName
     *
     * @param v new value
     */
    public void setReverseName(String v) 
    {

        if (!ObjectUtils.equals(this.reverseName, v))
        {
            this.reverseName = v;
            setModified(true);
        }


    }

    /**
     * Get the LeftToRightFirst
     *
     * @return String
     */
    public String getLeftToRightFirst()
    {
        return leftToRightFirst;
    }


    /**
     * Set the value of LeftToRightFirst
     *
     * @param v new value
     */
    public void setLeftToRightFirst(String v) 
    {

        if (!ObjectUtils.equals(this.leftToRightFirst, v))
        {
            this.leftToRightFirst = v;
            setModified(true);
        }


    }

    /**
     * Get the LeftToRightLevel
     *
     * @return String
     */
    public String getLeftToRightLevel()
    {
        return leftToRightLevel;
    }


    /**
     * Set the value of LeftToRightLevel
     *
     * @param v new value
     */
    public void setLeftToRightLevel(String v) 
    {

        if (!ObjectUtils.equals(this.leftToRightLevel, v))
        {
            this.leftToRightLevel = v;
            setModified(true);
        }


    }

    /**
     * Get the LeftToRightAll
     *
     * @return String
     */
    public String getLeftToRightAll()
    {
        return leftToRightAll;
    }


    /**
     * Set the value of LeftToRightAll
     *
     * @param v new value
     */
    public void setLeftToRightAll(String v) 
    {

        if (!ObjectUtils.equals(this.leftToRightAll, v))
        {
            this.leftToRightAll = v;
            setModified(true);
        }


    }

    /**
     * Get the RightToLeftFirst
     *
     * @return String
     */
    public String getRightToLeftFirst()
    {
        return rightToLeftFirst;
    }


    /**
     * Set the value of RightToLeftFirst
     *
     * @param v new value
     */
    public void setRightToLeftFirst(String v) 
    {

        if (!ObjectUtils.equals(this.rightToLeftFirst, v))
        {
            this.rightToLeftFirst = v;
            setModified(true);
        }


    }

    /**
     * Get the RightToLeftLevel
     *
     * @return String
     */
    public String getRightToLeftLevel()
    {
        return rightToLeftLevel;
    }


    /**
     * Set the value of RightToLeftLevel
     *
     * @param v new value
     */
    public void setRightToLeftLevel(String v) 
    {

        if (!ObjectUtils.equals(this.rightToLeftLevel, v))
        {
            this.rightToLeftLevel = v;
            setModified(true);
        }


    }

    /**
     * Get the RightToLeftAll
     *
     * @return String
     */
    public String getRightToLeftAll()
    {
        return rightToLeftAll;
    }


    /**
     * Set the value of RightToLeftAll
     *
     * @param v new value
     */
    public void setRightToLeftAll(String v) 
    {

        if (!ObjectUtils.equals(this.rightToLeftAll, v))
        {
            this.rightToLeftAll = v;
            setModified(true);
        }


    }

    /**
     * Get the LinkDirection
     *
     * @return Integer
     */
    public Integer getLinkDirection()
    {
        return linkDirection;
    }


    /**
     * Set the value of LinkDirection
     *
     * @param v new value
     */
    public void setLinkDirection(Integer v) 
    {

        if (!ObjectUtils.equals(this.linkDirection, v))
        {
            this.linkDirection = v;
            setModified(true);
        }


    }

    /**
     * Get the OutwardIconKey
     *
     * @return Integer
     */
    public Integer getOutwardIconKey()
    {
        return outwardIconKey;
    }


    /**
     * Set the value of OutwardIconKey
     *
     * @param v new value
     */
    public void setOutwardIconKey(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.outwardIconKey, v))
        {
            this.outwardIconKey = v;
            setModified(true);
        }


        if (aTBLOBRelatedByOutwardIconKey != null && !ObjectUtils.equals(aTBLOBRelatedByOutwardIconKey.getObjectID(), v))
        {
            aTBLOBRelatedByOutwardIconKey = null;
        }

    }

    /**
     * Get the InwardIconKey
     *
     * @return Integer
     */
    public Integer getInwardIconKey()
    {
        return inwardIconKey;
    }


    /**
     * Set the value of InwardIconKey
     *
     * @param v new value
     */
    public void setInwardIconKey(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.inwardIconKey, v))
        {
            this.inwardIconKey = v;
            setModified(true);
        }


        if (aTBLOBRelatedByInwardIconKey != null && !ObjectUtils.equals(aTBLOBRelatedByInwardIconKey.getObjectID(), v))
        {
            aTBLOBRelatedByInwardIconKey = null;
        }

    }

    /**
     * Get the LinkTypePlugin
     *
     * @return String
     */
    public String getLinkTypePlugin()
    {
        return linkTypePlugin;
    }


    /**
     * Set the value of LinkTypePlugin
     *
     * @param v new value
     */
    public void setLinkTypePlugin(String v) 
    {

        if (!ObjectUtils.equals(this.linkTypePlugin, v))
        {
            this.linkTypePlugin = v;
            setModified(true);
        }


    }

    /**
     * Get the MoreProps
     *
     * @return String
     */
    public String getMoreProps()
    {
        return moreProps;
    }


    /**
     * Set the value of MoreProps
     *
     * @param v new value
     */
    public void setMoreProps(String v) 
    {

        if (!ObjectUtils.equals(this.moreProps, v))
        {
            this.moreProps = v;
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

    



    private TBLOB aTBLOBRelatedByOutwardIconKey;

    /**
     * Declares an association between this object and a TBLOB object
     *
     * @param v TBLOB
     * @throws TorqueException
     */
    public void setTBLOBRelatedByOutwardIconKey(TBLOB v) throws TorqueException
    {
        if (v == null)
        {
            setOutwardIconKey((Integer) null);
        }
        else
        {
            setOutwardIconKey(v.getObjectID());
        }
        aTBLOBRelatedByOutwardIconKey = v;
    }


    /**
     * Returns the associated TBLOB object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TBLOB object
     * @throws TorqueException
     */
    public TBLOB getTBLOBRelatedByOutwardIconKey()
        throws TorqueException
    {
        if (aTBLOBRelatedByOutwardIconKey == null && (!ObjectUtils.equals(this.outwardIconKey, null)))
        {
            aTBLOBRelatedByOutwardIconKey = TBLOBPeer.retrieveByPK(SimpleKey.keyFor(this.outwardIconKey));
        }
        return aTBLOBRelatedByOutwardIconKey;
    }

    /**
     * Return the associated TBLOB object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TBLOB object
     * @throws TorqueException
     */
    public TBLOB getTBLOBRelatedByOutwardIconKey(Connection connection)
        throws TorqueException
    {
        if (aTBLOBRelatedByOutwardIconKey == null && (!ObjectUtils.equals(this.outwardIconKey, null)))
        {
            aTBLOBRelatedByOutwardIconKey = TBLOBPeer.retrieveByPK(SimpleKey.keyFor(this.outwardIconKey), connection);
        }
        return aTBLOBRelatedByOutwardIconKey;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTBLOBRelatedByOutwardIconKeyKey(ObjectKey key) throws TorqueException
    {

        setOutwardIconKey(new Integer(((NumberKey) key).intValue()));
    }




    private TBLOB aTBLOBRelatedByInwardIconKey;

    /**
     * Declares an association between this object and a TBLOB object
     *
     * @param v TBLOB
     * @throws TorqueException
     */
    public void setTBLOBRelatedByInwardIconKey(TBLOB v) throws TorqueException
    {
        if (v == null)
        {
            setInwardIconKey((Integer) null);
        }
        else
        {
            setInwardIconKey(v.getObjectID());
        }
        aTBLOBRelatedByInwardIconKey = v;
    }


    /**
     * Returns the associated TBLOB object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TBLOB object
     * @throws TorqueException
     */
    public TBLOB getTBLOBRelatedByInwardIconKey()
        throws TorqueException
    {
        if (aTBLOBRelatedByInwardIconKey == null && (!ObjectUtils.equals(this.inwardIconKey, null)))
        {
            aTBLOBRelatedByInwardIconKey = TBLOBPeer.retrieveByPK(SimpleKey.keyFor(this.inwardIconKey));
        }
        return aTBLOBRelatedByInwardIconKey;
    }

    /**
     * Return the associated TBLOB object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TBLOB object
     * @throws TorqueException
     */
    public TBLOB getTBLOBRelatedByInwardIconKey(Connection connection)
        throws TorqueException
    {
        if (aTBLOBRelatedByInwardIconKey == null && (!ObjectUtils.equals(this.inwardIconKey, null)))
        {
            aTBLOBRelatedByInwardIconKey = TBLOBPeer.retrieveByPK(SimpleKey.keyFor(this.inwardIconKey), connection);
        }
        return aTBLOBRelatedByInwardIconKey;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTBLOBRelatedByInwardIconKeyKey(ObjectKey key) throws TorqueException
    {

        setInwardIconKey(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTWorkItemLinks
     */
    protected List<TWorkItemLink> collTWorkItemLinks;

    /**
     * Temporary storage of collTWorkItemLinks to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkItemLinks()
    {
        if (collTWorkItemLinks == null)
        {
            collTWorkItemLinks = new ArrayList<TWorkItemLink>();
        }
    }


    /**
     * Method called to associate a TWorkItemLink object to this object
     * through the TWorkItemLink foreign key attribute
     *
     * @param l TWorkItemLink
     * @throws TorqueException
     */
    public void addTWorkItemLink(TWorkItemLink l) throws TorqueException
    {
        getTWorkItemLinks().add(l);
        l.setTLinkType((TLinkType) this);
    }

    /**
     * Method called to associate a TWorkItemLink object to this object
     * through the TWorkItemLink foreign key attribute using connection.
     *
     * @param l TWorkItemLink
     * @throws TorqueException
     */
    public void addTWorkItemLink(TWorkItemLink l, Connection con) throws TorqueException
    {
        getTWorkItemLinks(con).add(l);
        l.setTLinkType((TLinkType) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkItemLinks
     */
    private Criteria lastTWorkItemLinksCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkItemLinks(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkItemLink> getTWorkItemLinks()
        throws TorqueException
    {
        if (collTWorkItemLinks == null)
        {
            collTWorkItemLinks = getTWorkItemLinks(new Criteria(10));
        }
        return collTWorkItemLinks;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TLinkType has previously
     * been saved, it will retrieve related TWorkItemLinks from storage.
     * If this TLinkType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkItemLink> getTWorkItemLinks(Criteria criteria) throws TorqueException
    {
        if (collTWorkItemLinks == null)
        {
            if (isNew())
            {
               collTWorkItemLinks = new ArrayList<TWorkItemLink>();
            }
            else
            {
                criteria.add(TWorkItemLinkPeer.LINKTYPE, getObjectID() );
                collTWorkItemLinks = TWorkItemLinkPeer.doSelect(criteria);
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
                criteria.add(TWorkItemLinkPeer.LINKTYPE, getObjectID());
                if (!lastTWorkItemLinksCriteria.equals(criteria))
                {
                    collTWorkItemLinks = TWorkItemLinkPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkItemLinksCriteria = criteria;

        return collTWorkItemLinks;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkItemLinks(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkItemLink> getTWorkItemLinks(Connection con) throws TorqueException
    {
        if (collTWorkItemLinks == null)
        {
            collTWorkItemLinks = getTWorkItemLinks(new Criteria(10), con);
        }
        return collTWorkItemLinks;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TLinkType has previously
     * been saved, it will retrieve related TWorkItemLinks from storage.
     * If this TLinkType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkItemLink> getTWorkItemLinks(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkItemLinks == null)
        {
            if (isNew())
            {
               collTWorkItemLinks = new ArrayList<TWorkItemLink>();
            }
            else
            {
                 criteria.add(TWorkItemLinkPeer.LINKTYPE, getObjectID());
                 collTWorkItemLinks = TWorkItemLinkPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkItemLinkPeer.LINKTYPE, getObjectID());
                 if (!lastTWorkItemLinksCriteria.equals(criteria))
                 {
                     collTWorkItemLinks = TWorkItemLinkPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkItemLinksCriteria = criteria;

         return collTWorkItemLinks;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TLinkType is new, it will return
     * an empty collection; or if this TLinkType has previously
     * been saved, it will retrieve related TWorkItemLinks from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TLinkType.
     */
    protected List<TWorkItemLink> getTWorkItemLinksJoinTWorkItemRelatedByLinkPred(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemLinks == null)
        {
            if (isNew())
            {
               collTWorkItemLinks = new ArrayList<TWorkItemLink>();
            }
            else
            {
                criteria.add(TWorkItemLinkPeer.LINKTYPE, getObjectID());
                collTWorkItemLinks = TWorkItemLinkPeer.doSelectJoinTWorkItemRelatedByLinkPred(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemLinkPeer.LINKTYPE, getObjectID());
            if (!lastTWorkItemLinksCriteria.equals(criteria))
            {
                collTWorkItemLinks = TWorkItemLinkPeer.doSelectJoinTWorkItemRelatedByLinkPred(criteria);
            }
        }
        lastTWorkItemLinksCriteria = criteria;

        return collTWorkItemLinks;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TLinkType is new, it will return
     * an empty collection; or if this TLinkType has previously
     * been saved, it will retrieve related TWorkItemLinks from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TLinkType.
     */
    protected List<TWorkItemLink> getTWorkItemLinksJoinTWorkItemRelatedByLinkSucc(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemLinks == null)
        {
            if (isNew())
            {
               collTWorkItemLinks = new ArrayList<TWorkItemLink>();
            }
            else
            {
                criteria.add(TWorkItemLinkPeer.LINKTYPE, getObjectID());
                collTWorkItemLinks = TWorkItemLinkPeer.doSelectJoinTWorkItemRelatedByLinkSucc(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemLinkPeer.LINKTYPE, getObjectID());
            if (!lastTWorkItemLinksCriteria.equals(criteria))
            {
                collTWorkItemLinks = TWorkItemLinkPeer.doSelectJoinTWorkItemRelatedByLinkSucc(criteria);
            }
        }
        lastTWorkItemLinksCriteria = criteria;

        return collTWorkItemLinks;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TLinkType is new, it will return
     * an empty collection; or if this TLinkType has previously
     * been saved, it will retrieve related TWorkItemLinks from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TLinkType.
     */
    protected List<TWorkItemLink> getTWorkItemLinksJoinTLinkType(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemLinks == null)
        {
            if (isNew())
            {
               collTWorkItemLinks = new ArrayList<TWorkItemLink>();
            }
            else
            {
                criteria.add(TWorkItemLinkPeer.LINKTYPE, getObjectID());
                collTWorkItemLinks = TWorkItemLinkPeer.doSelectJoinTLinkType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemLinkPeer.LINKTYPE, getObjectID());
            if (!lastTWorkItemLinksCriteria.equals(criteria))
            {
                collTWorkItemLinks = TWorkItemLinkPeer.doSelectJoinTLinkType(criteria);
            }
        }
        lastTWorkItemLinksCriteria = criteria;

        return collTWorkItemLinks;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TLinkType is new, it will return
     * an empty collection; or if this TLinkType has previously
     * been saved, it will retrieve related TWorkItemLinks from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TLinkType.
     */
    protected List<TWorkItemLink> getTWorkItemLinksJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemLinks == null)
        {
            if (isNew())
            {
               collTWorkItemLinks = new ArrayList<TWorkItemLink>();
            }
            else
            {
                criteria.add(TWorkItemLinkPeer.LINKTYPE, getObjectID());
                collTWorkItemLinks = TWorkItemLinkPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemLinkPeer.LINKTYPE, getObjectID());
            if (!lastTWorkItemLinksCriteria.equals(criteria))
            {
                collTWorkItemLinks = TWorkItemLinkPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTWorkItemLinksCriteria = criteria;

        return collTWorkItemLinks;
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
            fieldNames.add("Name");
            fieldNames.add("ReverseName");
            fieldNames.add("LeftToRightFirst");
            fieldNames.add("LeftToRightLevel");
            fieldNames.add("LeftToRightAll");
            fieldNames.add("RightToLeftFirst");
            fieldNames.add("RightToLeftLevel");
            fieldNames.add("RightToLeftAll");
            fieldNames.add("LinkDirection");
            fieldNames.add("OutwardIconKey");
            fieldNames.add("InwardIconKey");
            fieldNames.add("LinkTypePlugin");
            fieldNames.add("MoreProps");
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
        if (name.equals("Name"))
        {
            return getName();
        }
        if (name.equals("ReverseName"))
        {
            return getReverseName();
        }
        if (name.equals("LeftToRightFirst"))
        {
            return getLeftToRightFirst();
        }
        if (name.equals("LeftToRightLevel"))
        {
            return getLeftToRightLevel();
        }
        if (name.equals("LeftToRightAll"))
        {
            return getLeftToRightAll();
        }
        if (name.equals("RightToLeftFirst"))
        {
            return getRightToLeftFirst();
        }
        if (name.equals("RightToLeftLevel"))
        {
            return getRightToLeftLevel();
        }
        if (name.equals("RightToLeftAll"))
        {
            return getRightToLeftAll();
        }
        if (name.equals("LinkDirection"))
        {
            return getLinkDirection();
        }
        if (name.equals("OutwardIconKey"))
        {
            return getOutwardIconKey();
        }
        if (name.equals("InwardIconKey"))
        {
            return getInwardIconKey();
        }
        if (name.equals("LinkTypePlugin"))
        {
            return getLinkTypePlugin();
        }
        if (name.equals("MoreProps"))
        {
            return getMoreProps();
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
        if (name.equals("ReverseName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setReverseName((String) value);
            return true;
        }
        if (name.equals("LeftToRightFirst"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLeftToRightFirst((String) value);
            return true;
        }
        if (name.equals("LeftToRightLevel"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLeftToRightLevel((String) value);
            return true;
        }
        if (name.equals("LeftToRightAll"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLeftToRightAll((String) value);
            return true;
        }
        if (name.equals("RightToLeftFirst"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRightToLeftFirst((String) value);
            return true;
        }
        if (name.equals("RightToLeftLevel"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRightToLeftLevel((String) value);
            return true;
        }
        if (name.equals("RightToLeftAll"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRightToLeftAll((String) value);
            return true;
        }
        if (name.equals("LinkDirection"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLinkDirection((Integer) value);
            return true;
        }
        if (name.equals("OutwardIconKey"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOutwardIconKey((Integer) value);
            return true;
        }
        if (name.equals("InwardIconKey"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setInwardIconKey((Integer) value);
            return true;
        }
        if (name.equals("LinkTypePlugin"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLinkTypePlugin((String) value);
            return true;
        }
        if (name.equals("MoreProps"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMoreProps((String) value);
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
        if (name.equals(TLinkTypePeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TLinkTypePeer.NAME))
        {
            return getName();
        }
        if (name.equals(TLinkTypePeer.REVERSENAME))
        {
            return getReverseName();
        }
        if (name.equals(TLinkTypePeer.LEFTTORIGHTFIRST))
        {
            return getLeftToRightFirst();
        }
        if (name.equals(TLinkTypePeer.LEFTTORIGHTLEVEL))
        {
            return getLeftToRightLevel();
        }
        if (name.equals(TLinkTypePeer.LEFTTORIGHTALL))
        {
            return getLeftToRightAll();
        }
        if (name.equals(TLinkTypePeer.RIGHTTOLEFTFIRST))
        {
            return getRightToLeftFirst();
        }
        if (name.equals(TLinkTypePeer.RIGHTTOLEFTLEVEL))
        {
            return getRightToLeftLevel();
        }
        if (name.equals(TLinkTypePeer.RIGHTTOLEFTALL))
        {
            return getRightToLeftAll();
        }
        if (name.equals(TLinkTypePeer.LINKDIRECTION))
        {
            return getLinkDirection();
        }
        if (name.equals(TLinkTypePeer.OUTWARDICONKEY))
        {
            return getOutwardIconKey();
        }
        if (name.equals(TLinkTypePeer.INWARDICONKEY))
        {
            return getInwardIconKey();
        }
        if (name.equals(TLinkTypePeer.LINKTYPEPLUGIN))
        {
            return getLinkTypePlugin();
        }
        if (name.equals(TLinkTypePeer.MOREPROPS))
        {
            return getMoreProps();
        }
        if (name.equals(TLinkTypePeer.TPUUID))
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
      if (TLinkTypePeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TLinkTypePeer.NAME.equals(name))
        {
            return setByName("Name", value);
        }
      if (TLinkTypePeer.REVERSENAME.equals(name))
        {
            return setByName("ReverseName", value);
        }
      if (TLinkTypePeer.LEFTTORIGHTFIRST.equals(name))
        {
            return setByName("LeftToRightFirst", value);
        }
      if (TLinkTypePeer.LEFTTORIGHTLEVEL.equals(name))
        {
            return setByName("LeftToRightLevel", value);
        }
      if (TLinkTypePeer.LEFTTORIGHTALL.equals(name))
        {
            return setByName("LeftToRightAll", value);
        }
      if (TLinkTypePeer.RIGHTTOLEFTFIRST.equals(name))
        {
            return setByName("RightToLeftFirst", value);
        }
      if (TLinkTypePeer.RIGHTTOLEFTLEVEL.equals(name))
        {
            return setByName("RightToLeftLevel", value);
        }
      if (TLinkTypePeer.RIGHTTOLEFTALL.equals(name))
        {
            return setByName("RightToLeftAll", value);
        }
      if (TLinkTypePeer.LINKDIRECTION.equals(name))
        {
            return setByName("LinkDirection", value);
        }
      if (TLinkTypePeer.OUTWARDICONKEY.equals(name))
        {
            return setByName("OutwardIconKey", value);
        }
      if (TLinkTypePeer.INWARDICONKEY.equals(name))
        {
            return setByName("InwardIconKey", value);
        }
      if (TLinkTypePeer.LINKTYPEPLUGIN.equals(name))
        {
            return setByName("LinkTypePlugin", value);
        }
      if (TLinkTypePeer.MOREPROPS.equals(name))
        {
            return setByName("MoreProps", value);
        }
      if (TLinkTypePeer.TPUUID.equals(name))
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
            return getName();
        }
        if (pos == 2)
        {
            return getReverseName();
        }
        if (pos == 3)
        {
            return getLeftToRightFirst();
        }
        if (pos == 4)
        {
            return getLeftToRightLevel();
        }
        if (pos == 5)
        {
            return getLeftToRightAll();
        }
        if (pos == 6)
        {
            return getRightToLeftFirst();
        }
        if (pos == 7)
        {
            return getRightToLeftLevel();
        }
        if (pos == 8)
        {
            return getRightToLeftAll();
        }
        if (pos == 9)
        {
            return getLinkDirection();
        }
        if (pos == 10)
        {
            return getOutwardIconKey();
        }
        if (pos == 11)
        {
            return getInwardIconKey();
        }
        if (pos == 12)
        {
            return getLinkTypePlugin();
        }
        if (pos == 13)
        {
            return getMoreProps();
        }
        if (pos == 14)
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
            return setByName("Name", value);
        }
    if (position == 2)
        {
            return setByName("ReverseName", value);
        }
    if (position == 3)
        {
            return setByName("LeftToRightFirst", value);
        }
    if (position == 4)
        {
            return setByName("LeftToRightLevel", value);
        }
    if (position == 5)
        {
            return setByName("LeftToRightAll", value);
        }
    if (position == 6)
        {
            return setByName("RightToLeftFirst", value);
        }
    if (position == 7)
        {
            return setByName("RightToLeftLevel", value);
        }
    if (position == 8)
        {
            return setByName("RightToLeftAll", value);
        }
    if (position == 9)
        {
            return setByName("LinkDirection", value);
        }
    if (position == 10)
        {
            return setByName("OutwardIconKey", value);
        }
    if (position == 11)
        {
            return setByName("InwardIconKey", value);
        }
    if (position == 12)
        {
            return setByName("LinkTypePlugin", value);
        }
    if (position == 13)
        {
            return setByName("MoreProps", value);
        }
    if (position == 14)
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
        save(TLinkTypePeer.DATABASE_NAME);
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
                    TLinkTypePeer.doInsert((TLinkType) this, con);
                    setNew(false);
                }
                else
                {
                    TLinkTypePeer.doUpdate((TLinkType) this, con);
                }
            }


            if (collTWorkItemLinks != null)
            {
                for (int i = 0; i < collTWorkItemLinks.size(); i++)
                {
                    ((TWorkItemLink) collTWorkItemLinks.get(i)).save(con);
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
    public TLinkType copy() throws TorqueException
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
    public TLinkType copy(Connection con) throws TorqueException
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
    public TLinkType copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TLinkType(), deepcopy);
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
    public TLinkType copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TLinkType(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TLinkType copyInto(TLinkType copyObj) throws TorqueException
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
    protected TLinkType copyInto(TLinkType copyObj, Connection con) throws TorqueException
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
    protected TLinkType copyInto(TLinkType copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setReverseName(reverseName);
        copyObj.setLeftToRightFirst(leftToRightFirst);
        copyObj.setLeftToRightLevel(leftToRightLevel);
        copyObj.setLeftToRightAll(leftToRightAll);
        copyObj.setRightToLeftFirst(rightToLeftFirst);
        copyObj.setRightToLeftLevel(rightToLeftLevel);
        copyObj.setRightToLeftAll(rightToLeftAll);
        copyObj.setLinkDirection(linkDirection);
        copyObj.setOutwardIconKey(outwardIconKey);
        copyObj.setInwardIconKey(inwardIconKey);
        copyObj.setLinkTypePlugin(linkTypePlugin);
        copyObj.setMoreProps(moreProps);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TWorkItemLink> vTWorkItemLinks = getTWorkItemLinks();
        if (vTWorkItemLinks != null)
        {
            for (int i = 0; i < vTWorkItemLinks.size(); i++)
            {
                TWorkItemLink obj =  vTWorkItemLinks.get(i);
                copyObj.addTWorkItemLink(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkItemLinks = null;
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
    protected TLinkType copyInto(TLinkType copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setReverseName(reverseName);
        copyObj.setLeftToRightFirst(leftToRightFirst);
        copyObj.setLeftToRightLevel(leftToRightLevel);
        copyObj.setLeftToRightAll(leftToRightAll);
        copyObj.setRightToLeftFirst(rightToLeftFirst);
        copyObj.setRightToLeftLevel(rightToLeftLevel);
        copyObj.setRightToLeftAll(rightToLeftAll);
        copyObj.setLinkDirection(linkDirection);
        copyObj.setOutwardIconKey(outwardIconKey);
        copyObj.setInwardIconKey(inwardIconKey);
        copyObj.setLinkTypePlugin(linkTypePlugin);
        copyObj.setMoreProps(moreProps);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TWorkItemLink> vTWorkItemLinks = getTWorkItemLinks(con);
        if (vTWorkItemLinks != null)
        {
            for (int i = 0; i < vTWorkItemLinks.size(); i++)
            {
                TWorkItemLink obj =  vTWorkItemLinks.get(i);
                copyObj.addTWorkItemLink(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkItemLinks = null;
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
    public TLinkTypePeer getPeer()
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
        return TLinkTypePeer.getTableMap();
    }

  
    /**
     * Creates a TLinkTypeBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TLinkTypeBean with the contents of this object
     */
    public TLinkTypeBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TLinkTypeBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TLinkTypeBean with the contents of this object
     */
    public TLinkTypeBean getBean(IdentityMap createdBeans)
    {
        TLinkTypeBean result = (TLinkTypeBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TLinkTypeBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setName(getName());
        result.setReverseName(getReverseName());
        result.setLeftToRightFirst(getLeftToRightFirst());
        result.setLeftToRightLevel(getLeftToRightLevel());
        result.setLeftToRightAll(getLeftToRightAll());
        result.setRightToLeftFirst(getRightToLeftFirst());
        result.setRightToLeftLevel(getRightToLeftLevel());
        result.setRightToLeftAll(getRightToLeftAll());
        result.setLinkDirection(getLinkDirection());
        result.setOutwardIconKey(getOutwardIconKey());
        result.setInwardIconKey(getInwardIconKey());
        result.setLinkTypePlugin(getLinkTypePlugin());
        result.setMoreProps(getMoreProps());
        result.setUuid(getUuid());



        if (collTWorkItemLinks != null)
        {
            List<TWorkItemLinkBean> relatedBeans = new ArrayList<TWorkItemLinkBean>(collTWorkItemLinks.size());
            for (Iterator<TWorkItemLink> collTWorkItemLinksIt = collTWorkItemLinks.iterator(); collTWorkItemLinksIt.hasNext(); )
            {
                TWorkItemLink related = (TWorkItemLink) collTWorkItemLinksIt.next();
                TWorkItemLinkBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkItemLinkBeans(relatedBeans);
        }




        if (aTBLOBRelatedByOutwardIconKey != null)
        {
            TBLOBBean relatedBean = aTBLOBRelatedByOutwardIconKey.getBean(createdBeans);
            result.setTBLOBBeanRelatedByOutwardIconKey(relatedBean);
        }



        if (aTBLOBRelatedByInwardIconKey != null)
        {
            TBLOBBean relatedBean = aTBLOBRelatedByInwardIconKey.getBean(createdBeans);
            result.setTBLOBBeanRelatedByInwardIconKey(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TLinkType with the contents
     * of a TLinkTypeBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TLinkTypeBean which contents are used to create
     *        the resulting class
     * @return an instance of TLinkType with the contents of bean
     */
    public static TLinkType createTLinkType(TLinkTypeBean bean)
        throws TorqueException
    {
        return createTLinkType(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TLinkType with the contents
     * of a TLinkTypeBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TLinkTypeBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TLinkType with the contents of bean
     */

    public static TLinkType createTLinkType(TLinkTypeBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TLinkType result = (TLinkType) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TLinkType();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setName(bean.getName());
        result.setReverseName(bean.getReverseName());
        result.setLeftToRightFirst(bean.getLeftToRightFirst());
        result.setLeftToRightLevel(bean.getLeftToRightLevel());
        result.setLeftToRightAll(bean.getLeftToRightAll());
        result.setRightToLeftFirst(bean.getRightToLeftFirst());
        result.setRightToLeftLevel(bean.getRightToLeftLevel());
        result.setRightToLeftAll(bean.getRightToLeftAll());
        result.setLinkDirection(bean.getLinkDirection());
        result.setOutwardIconKey(bean.getOutwardIconKey());
        result.setInwardIconKey(bean.getInwardIconKey());
        result.setLinkTypePlugin(bean.getLinkTypePlugin());
        result.setMoreProps(bean.getMoreProps());
        result.setUuid(bean.getUuid());



        {
            List<TWorkItemLinkBean> relatedBeans = bean.getTWorkItemLinkBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkItemLinkBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkItemLinkBean relatedBean =  relatedBeansIt.next();
                    TWorkItemLink related = TWorkItemLink.createTWorkItemLink(relatedBean, createdObjects);
                    result.addTWorkItemLinkFromBean(related);
                }
            }
        }




        {
            TBLOBBean relatedBean = bean.getTBLOBBeanRelatedByOutwardIconKey();
            if (relatedBean != null)
            {
                TBLOB relatedObject = TBLOB.createTBLOB(relatedBean, createdObjects);
                result.setTBLOBRelatedByOutwardIconKey(relatedObject);
            }
        }



        {
            TBLOBBean relatedBean = bean.getTBLOBBeanRelatedByInwardIconKey();
            if (relatedBean != null)
            {
                TBLOB relatedObject = TBLOB.createTBLOB(relatedBean, createdObjects);
                result.setTBLOBRelatedByInwardIconKey(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TWorkItemLink object to this object.
     * through the TWorkItemLink foreign key attribute
     *
     * @param toAdd TWorkItemLink
     */
    protected void addTWorkItemLinkFromBean(TWorkItemLink toAdd)
    {
        initTWorkItemLinks();
        collTWorkItemLinks.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TLinkType:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Name = ")
           .append(getName())
           .append("\n");
        str.append("ReverseName = ")
           .append(getReverseName())
           .append("\n");
        str.append("LeftToRightFirst = ")
           .append(getLeftToRightFirst())
           .append("\n");
        str.append("LeftToRightLevel = ")
           .append(getLeftToRightLevel())
           .append("\n");
        str.append("LeftToRightAll = ")
           .append(getLeftToRightAll())
           .append("\n");
        str.append("RightToLeftFirst = ")
           .append(getRightToLeftFirst())
           .append("\n");
        str.append("RightToLeftLevel = ")
           .append(getRightToLeftLevel())
           .append("\n");
        str.append("RightToLeftAll = ")
           .append(getRightToLeftAll())
           .append("\n");
        str.append("LinkDirection = ")
           .append(getLinkDirection())
           .append("\n");
        str.append("OutwardIconKey = ")
           .append(getOutwardIconKey())
           .append("\n");
        str.append("InwardIconKey = ")
           .append(getInwardIconKey())
           .append("\n");
        str.append("LinkTypePlugin = ")
           .append(getLinkTypePlugin())
           .append("\n");
        str.append("MoreProps = ")
           .append(getMoreProps())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
