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



import com.aurel.track.persist.TBLOB;
import com.aurel.track.persist.TBLOBPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.beans.TBLOBBean;

import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TAccountBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TSystemState
 */
public abstract class BaseTSystemState extends TpBaseObject
{
    /** The Peer class */
    private static final TSystemStatePeer peer =
        new TSystemStatePeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the label field */
    private String label;

    /** The value for the stateflag field */
    private Integer stateflag;

    /** The value for the symbol field */
    private String symbol;

    /** The value for the entityflag field */
    private Integer entityflag;

    /** The value for the sortorder field */
    private Integer sortorder;

    /** The value for the iconKey field */
    private Integer iconKey;

    /** The value for the cSSStyle field */
    private String cSSStyle;

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



        // update associated TProject
        if (collTProjects != null)
        {
            for (int i = 0; i < collTProjects.size(); i++)
            {
                ((TProject) collTProjects.get(i))
                        .setStatus(v);
            }
        }

        // update associated TRelease
        if (collTReleases != null)
        {
            for (int i = 0; i < collTReleases.size(); i++)
            {
                ((TRelease) collTReleases.get(i))
                        .setStatus(v);
            }
        }

        // update associated TAccount
        if (collTAccounts != null)
        {
            for (int i = 0; i < collTAccounts.size(); i++)
            {
                ((TAccount) collTAccounts.get(i))
                        .setStatus(v);
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
     * Get the Stateflag
     *
     * @return Integer
     */
    public Integer getStateflag()
    {
        return stateflag;
    }


    /**
     * Set the value of Stateflag
     *
     * @param v new value
     */
    public void setStateflag(Integer v) 
    {

        if (!ObjectUtils.equals(this.stateflag, v))
        {
            this.stateflag = v;
            setModified(true);
        }


    }

    /**
     * Get the Symbol
     *
     * @return String
     */
    public String getSymbol()
    {
        return symbol;
    }


    /**
     * Set the value of Symbol
     *
     * @param v new value
     */
    public void setSymbol(String v) 
    {

        if (!ObjectUtils.equals(this.symbol, v))
        {
            this.symbol = v;
            setModified(true);
        }


    }

    /**
     * Get the Entityflag
     *
     * @return Integer
     */
    public Integer getEntityflag()
    {
        return entityflag;
    }


    /**
     * Set the value of Entityflag
     *
     * @param v new value
     */
    public void setEntityflag(Integer v) 
    {

        if (!ObjectUtils.equals(this.entityflag, v))
        {
            this.entityflag = v;
            setModified(true);
        }


    }

    /**
     * Get the Sortorder
     *
     * @return Integer
     */
    public Integer getSortorder()
    {
        return sortorder;
    }


    /**
     * Set the value of Sortorder
     *
     * @param v new value
     */
    public void setSortorder(Integer v) 
    {

        if (!ObjectUtils.equals(this.sortorder, v))
        {
            this.sortorder = v;
            setModified(true);
        }


    }

    /**
     * Get the IconKey
     *
     * @return Integer
     */
    public Integer getIconKey()
    {
        return iconKey;
    }


    /**
     * Set the value of IconKey
     *
     * @param v new value
     */
    public void setIconKey(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.iconKey, v))
        {
            this.iconKey = v;
            setModified(true);
        }


        if (aTBLOB != null && !ObjectUtils.equals(aTBLOB.getObjectID(), v))
        {
            aTBLOB = null;
        }

    }

    /**
     * Get the CSSStyle
     *
     * @return String
     */
    public String getCSSStyle()
    {
        return cSSStyle;
    }


    /**
     * Set the value of CSSStyle
     *
     * @param v new value
     */
    public void setCSSStyle(String v) 
    {

        if (!ObjectUtils.equals(this.cSSStyle, v))
        {
            this.cSSStyle = v;
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

    



    private TBLOB aTBLOB;

    /**
     * Declares an association between this object and a TBLOB object
     *
     * @param v TBLOB
     * @throws TorqueException
     */
    public void setTBLOB(TBLOB v) throws TorqueException
    {
        if (v == null)
        {
            setIconKey((Integer) null);
        }
        else
        {
            setIconKey(v.getObjectID());
        }
        aTBLOB = v;
    }


    /**
     * Returns the associated TBLOB object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TBLOB object
     * @throws TorqueException
     */
    public TBLOB getTBLOB()
        throws TorqueException
    {
        if (aTBLOB == null && (!ObjectUtils.equals(this.iconKey, null)))
        {
            aTBLOB = TBLOBPeer.retrieveByPK(SimpleKey.keyFor(this.iconKey));
        }
        return aTBLOB;
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
    public TBLOB getTBLOB(Connection connection)
        throws TorqueException
    {
        if (aTBLOB == null && (!ObjectUtils.equals(this.iconKey, null)))
        {
            aTBLOB = TBLOBPeer.retrieveByPK(SimpleKey.keyFor(this.iconKey), connection);
        }
        return aTBLOB;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTBLOBKey(ObjectKey key) throws TorqueException
    {

        setIconKey(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTProjects
     */
    protected List<TProject> collTProjects;

    /**
     * Temporary storage of collTProjects to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTProjects()
    {
        if (collTProjects == null)
        {
            collTProjects = new ArrayList<TProject>();
        }
    }


    /**
     * Method called to associate a TProject object to this object
     * through the TProject foreign key attribute
     *
     * @param l TProject
     * @throws TorqueException
     */
    public void addTProject(TProject l) throws TorqueException
    {
        getTProjects().add(l);
        l.setTSystemState((TSystemState) this);
    }

    /**
     * Method called to associate a TProject object to this object
     * through the TProject foreign key attribute using connection.
     *
     * @param l TProject
     * @throws TorqueException
     */
    public void addTProject(TProject l, Connection con) throws TorqueException
    {
        getTProjects(con).add(l);
        l.setTSystemState((TSystemState) this);
    }

    /**
     * The criteria used to select the current contents of collTProjects
     */
    private Criteria lastTProjectsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTProjects(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TProject> getTProjects()
        throws TorqueException
    {
        if (collTProjects == null)
        {
            collTProjects = getTProjects(new Criteria(10));
        }
        return collTProjects;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSystemState has previously
     * been saved, it will retrieve related TProjects from storage.
     * If this TSystemState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TProject> getTProjects(Criteria criteria) throws TorqueException
    {
        if (collTProjects == null)
        {
            if (isNew())
            {
               collTProjects = new ArrayList<TProject>();
            }
            else
            {
                criteria.add(TProjectPeer.STATUS, getObjectID() );
                collTProjects = TProjectPeer.doSelect(criteria);
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
                criteria.add(TProjectPeer.STATUS, getObjectID());
                if (!lastTProjectsCriteria.equals(criteria))
                {
                    collTProjects = TProjectPeer.doSelect(criteria);
                }
            }
        }
        lastTProjectsCriteria = criteria;

        return collTProjects;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTProjects(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TProject> getTProjects(Connection con) throws TorqueException
    {
        if (collTProjects == null)
        {
            collTProjects = getTProjects(new Criteria(10), con);
        }
        return collTProjects;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSystemState has previously
     * been saved, it will retrieve related TProjects from storage.
     * If this TSystemState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TProject> getTProjects(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTProjects == null)
        {
            if (isNew())
            {
               collTProjects = new ArrayList<TProject>();
            }
            else
            {
                 criteria.add(TProjectPeer.STATUS, getObjectID());
                 collTProjects = TProjectPeer.doSelect(criteria, con);
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
                 criteria.add(TProjectPeer.STATUS, getObjectID());
                 if (!lastTProjectsCriteria.equals(criteria))
                 {
                     collTProjects = TProjectPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTProjectsCriteria = criteria;

         return collTProjects;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSystemState is new, it will return
     * an empty collection; or if this TSystemState has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSystemState.
     */
    protected List<TProject> getTProjectsJoinTPersonRelatedByDefaultOwnerID(Criteria criteria)
        throws TorqueException
    {
        if (collTProjects == null)
        {
            if (isNew())
            {
               collTProjects = new ArrayList<TProject>();
            }
            else
            {
                criteria.add(TProjectPeer.STATUS, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTPersonRelatedByDefaultOwnerID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.STATUS, getObjectID());
            if (!lastTProjectsCriteria.equals(criteria))
            {
                collTProjects = TProjectPeer.doSelectJoinTPersonRelatedByDefaultOwnerID(criteria);
            }
        }
        lastTProjectsCriteria = criteria;

        return collTProjects;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSystemState is new, it will return
     * an empty collection; or if this TSystemState has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSystemState.
     */
    protected List<TProject> getTProjectsJoinTPersonRelatedByDefaultManagerID(Criteria criteria)
        throws TorqueException
    {
        if (collTProjects == null)
        {
            if (isNew())
            {
               collTProjects = new ArrayList<TProject>();
            }
            else
            {
                criteria.add(TProjectPeer.STATUS, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTPersonRelatedByDefaultManagerID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.STATUS, getObjectID());
            if (!lastTProjectsCriteria.equals(criteria))
            {
                collTProjects = TProjectPeer.doSelectJoinTPersonRelatedByDefaultManagerID(criteria);
            }
        }
        lastTProjectsCriteria = criteria;

        return collTProjects;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSystemState is new, it will return
     * an empty collection; or if this TSystemState has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSystemState.
     */
    protected List<TProject> getTProjectsJoinTState(Criteria criteria)
        throws TorqueException
    {
        if (collTProjects == null)
        {
            if (isNew())
            {
               collTProjects = new ArrayList<TProject>();
            }
            else
            {
                criteria.add(TProjectPeer.STATUS, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.STATUS, getObjectID());
            if (!lastTProjectsCriteria.equals(criteria))
            {
                collTProjects = TProjectPeer.doSelectJoinTState(criteria);
            }
        }
        lastTProjectsCriteria = criteria;

        return collTProjects;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSystemState is new, it will return
     * an empty collection; or if this TSystemState has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSystemState.
     */
    protected List<TProject> getTProjectsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTProjects == null)
        {
            if (isNew())
            {
               collTProjects = new ArrayList<TProject>();
            }
            else
            {
                criteria.add(TProjectPeer.STATUS, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.STATUS, getObjectID());
            if (!lastTProjectsCriteria.equals(criteria))
            {
                collTProjects = TProjectPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTProjectsCriteria = criteria;

        return collTProjects;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSystemState is new, it will return
     * an empty collection; or if this TSystemState has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSystemState.
     */
    protected List<TProject> getTProjectsJoinTSystemState(Criteria criteria)
        throws TorqueException
    {
        if (collTProjects == null)
        {
            if (isNew())
            {
               collTProjects = new ArrayList<TProject>();
            }
            else
            {
                criteria.add(TProjectPeer.STATUS, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTSystemState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.STATUS, getObjectID());
            if (!lastTProjectsCriteria.equals(criteria))
            {
                collTProjects = TProjectPeer.doSelectJoinTSystemState(criteria);
            }
        }
        lastTProjectsCriteria = criteria;

        return collTProjects;
    }

















    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSystemState is new, it will return
     * an empty collection; or if this TSystemState has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSystemState.
     */
    protected List<TProject> getTProjectsJoinTDomain(Criteria criteria)
        throws TorqueException
    {
        if (collTProjects == null)
        {
            if (isNew())
            {
               collTProjects = new ArrayList<TProject>();
            }
            else
            {
                criteria.add(TProjectPeer.STATUS, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTDomain(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.STATUS, getObjectID());
            if (!lastTProjectsCriteria.equals(criteria))
            {
                collTProjects = TProjectPeer.doSelectJoinTDomain(criteria);
            }
        }
        lastTProjectsCriteria = criteria;

        return collTProjects;
    }





    /**
     * Collection to store aggregation of collTReleases
     */
    protected List<TRelease> collTReleases;

    /**
     * Temporary storage of collTReleases to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTReleases()
    {
        if (collTReleases == null)
        {
            collTReleases = new ArrayList<TRelease>();
        }
    }


    /**
     * Method called to associate a TRelease object to this object
     * through the TRelease foreign key attribute
     *
     * @param l TRelease
     * @throws TorqueException
     */
    public void addTRelease(TRelease l) throws TorqueException
    {
        getTReleases().add(l);
        l.setTSystemState((TSystemState) this);
    }

    /**
     * Method called to associate a TRelease object to this object
     * through the TRelease foreign key attribute using connection.
     *
     * @param l TRelease
     * @throws TorqueException
     */
    public void addTRelease(TRelease l, Connection con) throws TorqueException
    {
        getTReleases(con).add(l);
        l.setTSystemState((TSystemState) this);
    }

    /**
     * The criteria used to select the current contents of collTReleases
     */
    private Criteria lastTReleasesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTReleases(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TRelease> getTReleases()
        throws TorqueException
    {
        if (collTReleases == null)
        {
            collTReleases = getTReleases(new Criteria(10));
        }
        return collTReleases;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSystemState has previously
     * been saved, it will retrieve related TReleases from storage.
     * If this TSystemState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TRelease> getTReleases(Criteria criteria) throws TorqueException
    {
        if (collTReleases == null)
        {
            if (isNew())
            {
               collTReleases = new ArrayList<TRelease>();
            }
            else
            {
                criteria.add(TReleasePeer.STATUS, getObjectID() );
                collTReleases = TReleasePeer.doSelect(criteria);
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
                criteria.add(TReleasePeer.STATUS, getObjectID());
                if (!lastTReleasesCriteria.equals(criteria))
                {
                    collTReleases = TReleasePeer.doSelect(criteria);
                }
            }
        }
        lastTReleasesCriteria = criteria;

        return collTReleases;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTReleases(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TRelease> getTReleases(Connection con) throws TorqueException
    {
        if (collTReleases == null)
        {
            collTReleases = getTReleases(new Criteria(10), con);
        }
        return collTReleases;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSystemState has previously
     * been saved, it will retrieve related TReleases from storage.
     * If this TSystemState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TRelease> getTReleases(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTReleases == null)
        {
            if (isNew())
            {
               collTReleases = new ArrayList<TRelease>();
            }
            else
            {
                 criteria.add(TReleasePeer.STATUS, getObjectID());
                 collTReleases = TReleasePeer.doSelect(criteria, con);
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
                 criteria.add(TReleasePeer.STATUS, getObjectID());
                 if (!lastTReleasesCriteria.equals(criteria))
                 {
                     collTReleases = TReleasePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTReleasesCriteria = criteria;

         return collTReleases;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSystemState is new, it will return
     * an empty collection; or if this TSystemState has previously
     * been saved, it will retrieve related TReleases from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSystemState.
     */
    protected List<TRelease> getTReleasesJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTReleases == null)
        {
            if (isNew())
            {
               collTReleases = new ArrayList<TRelease>();
            }
            else
            {
                criteria.add(TReleasePeer.STATUS, getObjectID());
                collTReleases = TReleasePeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReleasePeer.STATUS, getObjectID());
            if (!lastTReleasesCriteria.equals(criteria))
            {
                collTReleases = TReleasePeer.doSelectJoinTProject(criteria);
            }
        }
        lastTReleasesCriteria = criteria;

        return collTReleases;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSystemState is new, it will return
     * an empty collection; or if this TSystemState has previously
     * been saved, it will retrieve related TReleases from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSystemState.
     */
    protected List<TRelease> getTReleasesJoinTSystemState(Criteria criteria)
        throws TorqueException
    {
        if (collTReleases == null)
        {
            if (isNew())
            {
               collTReleases = new ArrayList<TRelease>();
            }
            else
            {
                criteria.add(TReleasePeer.STATUS, getObjectID());
                collTReleases = TReleasePeer.doSelectJoinTSystemState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReleasePeer.STATUS, getObjectID());
            if (!lastTReleasesCriteria.equals(criteria))
            {
                collTReleases = TReleasePeer.doSelectJoinTSystemState(criteria);
            }
        }
        lastTReleasesCriteria = criteria;

        return collTReleases;
    }













    /**
     * Collection to store aggregation of collTAccounts
     */
    protected List<TAccount> collTAccounts;

    /**
     * Temporary storage of collTAccounts to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTAccounts()
    {
        if (collTAccounts == null)
        {
            collTAccounts = new ArrayList<TAccount>();
        }
    }


    /**
     * Method called to associate a TAccount object to this object
     * through the TAccount foreign key attribute
     *
     * @param l TAccount
     * @throws TorqueException
     */
    public void addTAccount(TAccount l) throws TorqueException
    {
        getTAccounts().add(l);
        l.setTSystemState((TSystemState) this);
    }

    /**
     * Method called to associate a TAccount object to this object
     * through the TAccount foreign key attribute using connection.
     *
     * @param l TAccount
     * @throws TorqueException
     */
    public void addTAccount(TAccount l, Connection con) throws TorqueException
    {
        getTAccounts(con).add(l);
        l.setTSystemState((TSystemState) this);
    }

    /**
     * The criteria used to select the current contents of collTAccounts
     */
    private Criteria lastTAccountsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAccounts(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TAccount> getTAccounts()
        throws TorqueException
    {
        if (collTAccounts == null)
        {
            collTAccounts = getTAccounts(new Criteria(10));
        }
        return collTAccounts;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSystemState has previously
     * been saved, it will retrieve related TAccounts from storage.
     * If this TSystemState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TAccount> getTAccounts(Criteria criteria) throws TorqueException
    {
        if (collTAccounts == null)
        {
            if (isNew())
            {
               collTAccounts = new ArrayList<TAccount>();
            }
            else
            {
                criteria.add(TAccountPeer.STATUS, getObjectID() );
                collTAccounts = TAccountPeer.doSelect(criteria);
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
                criteria.add(TAccountPeer.STATUS, getObjectID());
                if (!lastTAccountsCriteria.equals(criteria))
                {
                    collTAccounts = TAccountPeer.doSelect(criteria);
                }
            }
        }
        lastTAccountsCriteria = criteria;

        return collTAccounts;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAccounts(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAccount> getTAccounts(Connection con) throws TorqueException
    {
        if (collTAccounts == null)
        {
            collTAccounts = getTAccounts(new Criteria(10), con);
        }
        return collTAccounts;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSystemState has previously
     * been saved, it will retrieve related TAccounts from storage.
     * If this TSystemState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAccount> getTAccounts(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTAccounts == null)
        {
            if (isNew())
            {
               collTAccounts = new ArrayList<TAccount>();
            }
            else
            {
                 criteria.add(TAccountPeer.STATUS, getObjectID());
                 collTAccounts = TAccountPeer.doSelect(criteria, con);
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
                 criteria.add(TAccountPeer.STATUS, getObjectID());
                 if (!lastTAccountsCriteria.equals(criteria))
                 {
                     collTAccounts = TAccountPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTAccountsCriteria = criteria;

         return collTAccounts;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSystemState is new, it will return
     * an empty collection; or if this TSystemState has previously
     * been saved, it will retrieve related TAccounts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSystemState.
     */
    protected List<TAccount> getTAccountsJoinTSystemState(Criteria criteria)
        throws TorqueException
    {
        if (collTAccounts == null)
        {
            if (isNew())
            {
               collTAccounts = new ArrayList<TAccount>();
            }
            else
            {
                criteria.add(TAccountPeer.STATUS, getObjectID());
                collTAccounts = TAccountPeer.doSelectJoinTSystemState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAccountPeer.STATUS, getObjectID());
            if (!lastTAccountsCriteria.equals(criteria))
            {
                collTAccounts = TAccountPeer.doSelectJoinTSystemState(criteria);
            }
        }
        lastTAccountsCriteria = criteria;

        return collTAccounts;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSystemState is new, it will return
     * an empty collection; or if this TSystemState has previously
     * been saved, it will retrieve related TAccounts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSystemState.
     */
    protected List<TAccount> getTAccountsJoinTCostCenter(Criteria criteria)
        throws TorqueException
    {
        if (collTAccounts == null)
        {
            if (isNew())
            {
               collTAccounts = new ArrayList<TAccount>();
            }
            else
            {
                criteria.add(TAccountPeer.STATUS, getObjectID());
                collTAccounts = TAccountPeer.doSelectJoinTCostCenter(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAccountPeer.STATUS, getObjectID());
            if (!lastTAccountsCriteria.equals(criteria))
            {
                collTAccounts = TAccountPeer.doSelectJoinTCostCenter(criteria);
            }
        }
        lastTAccountsCriteria = criteria;

        return collTAccounts;
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
            fieldNames.add("Stateflag");
            fieldNames.add("Symbol");
            fieldNames.add("Entityflag");
            fieldNames.add("Sortorder");
            fieldNames.add("IconKey");
            fieldNames.add("CSSStyle");
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
        if (name.equals("Stateflag"))
        {
            return getStateflag();
        }
        if (name.equals("Symbol"))
        {
            return getSymbol();
        }
        if (name.equals("Entityflag"))
        {
            return getEntityflag();
        }
        if (name.equals("Sortorder"))
        {
            return getSortorder();
        }
        if (name.equals("IconKey"))
        {
            return getIconKey();
        }
        if (name.equals("CSSStyle"))
        {
            return getCSSStyle();
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
        if (name.equals("Stateflag"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setStateflag((Integer) value);
            return true;
        }
        if (name.equals("Symbol"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSymbol((String) value);
            return true;
        }
        if (name.equals("Entityflag"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setEntityflag((Integer) value);
            return true;
        }
        if (name.equals("Sortorder"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSortorder((Integer) value);
            return true;
        }
        if (name.equals("IconKey"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIconKey((Integer) value);
            return true;
        }
        if (name.equals("CSSStyle"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCSSStyle((String) value);
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
        if (name.equals(TSystemStatePeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TSystemStatePeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TSystemStatePeer.STATEFLAG))
        {
            return getStateflag();
        }
        if (name.equals(TSystemStatePeer.SYMBOL))
        {
            return getSymbol();
        }
        if (name.equals(TSystemStatePeer.ENTITYFLAG))
        {
            return getEntityflag();
        }
        if (name.equals(TSystemStatePeer.SORTORDER))
        {
            return getSortorder();
        }
        if (name.equals(TSystemStatePeer.ICONKEY))
        {
            return getIconKey();
        }
        if (name.equals(TSystemStatePeer.CSSSTYLE))
        {
            return getCSSStyle();
        }
        if (name.equals(TSystemStatePeer.TPUUID))
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
      if (TSystemStatePeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TSystemStatePeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TSystemStatePeer.STATEFLAG.equals(name))
        {
            return setByName("Stateflag", value);
        }
      if (TSystemStatePeer.SYMBOL.equals(name))
        {
            return setByName("Symbol", value);
        }
      if (TSystemStatePeer.ENTITYFLAG.equals(name))
        {
            return setByName("Entityflag", value);
        }
      if (TSystemStatePeer.SORTORDER.equals(name))
        {
            return setByName("Sortorder", value);
        }
      if (TSystemStatePeer.ICONKEY.equals(name))
        {
            return setByName("IconKey", value);
        }
      if (TSystemStatePeer.CSSSTYLE.equals(name))
        {
            return setByName("CSSStyle", value);
        }
      if (TSystemStatePeer.TPUUID.equals(name))
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
            return getStateflag();
        }
        if (pos == 3)
        {
            return getSymbol();
        }
        if (pos == 4)
        {
            return getEntityflag();
        }
        if (pos == 5)
        {
            return getSortorder();
        }
        if (pos == 6)
        {
            return getIconKey();
        }
        if (pos == 7)
        {
            return getCSSStyle();
        }
        if (pos == 8)
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
            return setByName("Stateflag", value);
        }
    if (position == 3)
        {
            return setByName("Symbol", value);
        }
    if (position == 4)
        {
            return setByName("Entityflag", value);
        }
    if (position == 5)
        {
            return setByName("Sortorder", value);
        }
    if (position == 6)
        {
            return setByName("IconKey", value);
        }
    if (position == 7)
        {
            return setByName("CSSStyle", value);
        }
    if (position == 8)
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
        save(TSystemStatePeer.DATABASE_NAME);
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
                    TSystemStatePeer.doInsert((TSystemState) this, con);
                    setNew(false);
                }
                else
                {
                    TSystemStatePeer.doUpdate((TSystemState) this, con);
                }
            }


            if (collTProjects != null)
            {
                for (int i = 0; i < collTProjects.size(); i++)
                {
                    ((TProject) collTProjects.get(i)).save(con);
                }
            }

            if (collTReleases != null)
            {
                for (int i = 0; i < collTReleases.size(); i++)
                {
                    ((TRelease) collTReleases.get(i)).save(con);
                }
            }

            if (collTAccounts != null)
            {
                for (int i = 0; i < collTAccounts.size(); i++)
                {
                    ((TAccount) collTAccounts.get(i)).save(con);
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
    public TSystemState copy() throws TorqueException
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
    public TSystemState copy(Connection con) throws TorqueException
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
    public TSystemState copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TSystemState(), deepcopy);
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
    public TSystemState copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TSystemState(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TSystemState copyInto(TSystemState copyObj) throws TorqueException
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
    protected TSystemState copyInto(TSystemState copyObj, Connection con) throws TorqueException
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
    protected TSystemState copyInto(TSystemState copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setStateflag(stateflag);
        copyObj.setSymbol(symbol);
        copyObj.setEntityflag(entityflag);
        copyObj.setSortorder(sortorder);
        copyObj.setIconKey(iconKey);
        copyObj.setCSSStyle(cSSStyle);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TProject> vTProjects = getTProjects();
        if (vTProjects != null)
        {
            for (int i = 0; i < vTProjects.size(); i++)
            {
                TProject obj =  vTProjects.get(i);
                copyObj.addTProject(obj.copy());
            }
        }
        else
        {
            copyObj.collTProjects = null;
        }


        List<TRelease> vTReleases = getTReleases();
        if (vTReleases != null)
        {
            for (int i = 0; i < vTReleases.size(); i++)
            {
                TRelease obj =  vTReleases.get(i);
                copyObj.addTRelease(obj.copy());
            }
        }
        else
        {
            copyObj.collTReleases = null;
        }


        List<TAccount> vTAccounts = getTAccounts();
        if (vTAccounts != null)
        {
            for (int i = 0; i < vTAccounts.size(); i++)
            {
                TAccount obj =  vTAccounts.get(i);
                copyObj.addTAccount(obj.copy());
            }
        }
        else
        {
            copyObj.collTAccounts = null;
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
    protected TSystemState copyInto(TSystemState copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setStateflag(stateflag);
        copyObj.setSymbol(symbol);
        copyObj.setEntityflag(entityflag);
        copyObj.setSortorder(sortorder);
        copyObj.setIconKey(iconKey);
        copyObj.setCSSStyle(cSSStyle);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TProject> vTProjects = getTProjects(con);
        if (vTProjects != null)
        {
            for (int i = 0; i < vTProjects.size(); i++)
            {
                TProject obj =  vTProjects.get(i);
                copyObj.addTProject(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTProjects = null;
        }


        List<TRelease> vTReleases = getTReleases(con);
        if (vTReleases != null)
        {
            for (int i = 0; i < vTReleases.size(); i++)
            {
                TRelease obj =  vTReleases.get(i);
                copyObj.addTRelease(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTReleases = null;
        }


        List<TAccount> vTAccounts = getTAccounts(con);
        if (vTAccounts != null)
        {
            for (int i = 0; i < vTAccounts.size(); i++)
            {
                TAccount obj =  vTAccounts.get(i);
                copyObj.addTAccount(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTAccounts = null;
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
    public TSystemStatePeer getPeer()
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
        return TSystemStatePeer.getTableMap();
    }

  
    /**
     * Creates a TSystemStateBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TSystemStateBean with the contents of this object
     */
    public TSystemStateBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TSystemStateBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TSystemStateBean with the contents of this object
     */
    public TSystemStateBean getBean(IdentityMap createdBeans)
    {
        TSystemStateBean result = (TSystemStateBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TSystemStateBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setLabel(getLabel());
        result.setStateflag(getStateflag());
        result.setSymbol(getSymbol());
        result.setEntityflag(getEntityflag());
        result.setSortorder(getSortorder());
        result.setIconKey(getIconKey());
        result.setCSSStyle(getCSSStyle());
        result.setUuid(getUuid());



        if (collTProjects != null)
        {
            List<TProjectBean> relatedBeans = new ArrayList<TProjectBean>(collTProjects.size());
            for (Iterator<TProject> collTProjectsIt = collTProjects.iterator(); collTProjectsIt.hasNext(); )
            {
                TProject related = (TProject) collTProjectsIt.next();
                TProjectBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTProjectBeans(relatedBeans);
        }


        if (collTReleases != null)
        {
            List<TReleaseBean> relatedBeans = new ArrayList<TReleaseBean>(collTReleases.size());
            for (Iterator<TRelease> collTReleasesIt = collTReleases.iterator(); collTReleasesIt.hasNext(); )
            {
                TRelease related = (TRelease) collTReleasesIt.next();
                TReleaseBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTReleaseBeans(relatedBeans);
        }


        if (collTAccounts != null)
        {
            List<TAccountBean> relatedBeans = new ArrayList<TAccountBean>(collTAccounts.size());
            for (Iterator<TAccount> collTAccountsIt = collTAccounts.iterator(); collTAccountsIt.hasNext(); )
            {
                TAccount related = (TAccount) collTAccountsIt.next();
                TAccountBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTAccountBeans(relatedBeans);
        }




        if (aTBLOB != null)
        {
            TBLOBBean relatedBean = aTBLOB.getBean(createdBeans);
            result.setTBLOBBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TSystemState with the contents
     * of a TSystemStateBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TSystemStateBean which contents are used to create
     *        the resulting class
     * @return an instance of TSystemState with the contents of bean
     */
    public static TSystemState createTSystemState(TSystemStateBean bean)
        throws TorqueException
    {
        return createTSystemState(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TSystemState with the contents
     * of a TSystemStateBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TSystemStateBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TSystemState with the contents of bean
     */

    public static TSystemState createTSystemState(TSystemStateBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TSystemState result = (TSystemState) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TSystemState();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setLabel(bean.getLabel());
        result.setStateflag(bean.getStateflag());
        result.setSymbol(bean.getSymbol());
        result.setEntityflag(bean.getEntityflag());
        result.setSortorder(bean.getSortorder());
        result.setIconKey(bean.getIconKey());
        result.setCSSStyle(bean.getCSSStyle());
        result.setUuid(bean.getUuid());



        {
            List<TProjectBean> relatedBeans = bean.getTProjectBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TProjectBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TProjectBean relatedBean =  relatedBeansIt.next();
                    TProject related = TProject.createTProject(relatedBean, createdObjects);
                    result.addTProjectFromBean(related);
                }
            }
        }


        {
            List<TReleaseBean> relatedBeans = bean.getTReleaseBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TReleaseBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TReleaseBean relatedBean =  relatedBeansIt.next();
                    TRelease related = TRelease.createTRelease(relatedBean, createdObjects);
                    result.addTReleaseFromBean(related);
                }
            }
        }


        {
            List<TAccountBean> relatedBeans = bean.getTAccountBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TAccountBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TAccountBean relatedBean =  relatedBeansIt.next();
                    TAccount related = TAccount.createTAccount(relatedBean, createdObjects);
                    result.addTAccountFromBean(related);
                }
            }
        }




        {
            TBLOBBean relatedBean = bean.getTBLOBBean();
            if (relatedBean != null)
            {
                TBLOB relatedObject = TBLOB.createTBLOB(relatedBean, createdObjects);
                result.setTBLOB(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TProject object to this object.
     * through the TProject foreign key attribute
     *
     * @param toAdd TProject
     */
    protected void addTProjectFromBean(TProject toAdd)
    {
        initTProjects();
        collTProjects.add(toAdd);
    }


    /**
     * Method called to associate a TRelease object to this object.
     * through the TRelease foreign key attribute
     *
     * @param toAdd TRelease
     */
    protected void addTReleaseFromBean(TRelease toAdd)
    {
        initTReleases();
        collTReleases.add(toAdd);
    }


    /**
     * Method called to associate a TAccount object to this object.
     * through the TAccount foreign key attribute
     *
     * @param toAdd TAccount
     */
    protected void addTAccountFromBean(TAccount toAdd)
    {
        initTAccounts();
        collTAccounts.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TSystemState:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("Stateflag = ")
           .append(getStateflag())
           .append("\n");
        str.append("Symbol = ")
           .append(getSymbol())
           .append("\n");
        str.append("Entityflag = ")
           .append(getEntityflag())
           .append("\n");
        str.append("Sortorder = ")
           .append(getSortorder())
           .append("\n");
        str.append("IconKey = ")
           .append(getIconKey())
           .append("\n");
        str.append("CSSStyle = ")
           .append(getCSSStyle())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
