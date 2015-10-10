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



import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TNavigatorLayoutBean;
import com.aurel.track.beans.TPersonBean;

import com.aurel.track.beans.TNavigatorColumnBean;
import com.aurel.track.beans.TNavigatorGroupingSortingBean;
import com.aurel.track.beans.TCardGroupingFieldBean;
import com.aurel.track.beans.TViewParamBean;


/**
 * layout for item navigator
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TNavigatorLayout
 */
public abstract class BaseTNavigatorLayout extends TpBaseObject
{
    /** The Peer class */
    private static final TNavigatorLayoutPeer peer =
        new TNavigatorLayoutPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the person field */
    private Integer person;

    /** The value for the filterID field */
    private Integer filterID;

    /** The value for the filterType field */
    private Integer filterType;

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



        // update associated TNavigatorColumn
        if (collTNavigatorColumns != null)
        {
            for (int i = 0; i < collTNavigatorColumns.size(); i++)
            {
                ((TNavigatorColumn) collTNavigatorColumns.get(i))
                        .setNavigatorLayout(v);
            }
        }

        // update associated TNavigatorGroupingSorting
        if (collTNavigatorGroupingSortings != null)
        {
            for (int i = 0; i < collTNavigatorGroupingSortings.size(); i++)
            {
                ((TNavigatorGroupingSorting) collTNavigatorGroupingSortings.get(i))
                        .setNavigatorLayout(v);
            }
        }

        // update associated TCardGroupingField
        if (collTCardGroupingFields != null)
        {
            for (int i = 0; i < collTCardGroupingFields.size(); i++)
            {
                ((TCardGroupingField) collTCardGroupingFields.get(i))
                        .setNavigatorLayout(v);
            }
        }

        // update associated TViewParam
        if (collTViewParams != null)
        {
            for (int i = 0; i < collTViewParams.size(); i++)
            {
                ((TViewParam) collTViewParams.get(i))
                        .setNavigatorLayout(v);
            }
        }
    }

    /**
     * Get the Person
     *
     * @return Integer
     */
    public Integer getPerson()
    {
        return person;
    }


    /**
     * Set the value of Person
     *
     * @param v new value
     */
    public void setPerson(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.person, v))
        {
            this.person = v;
            setModified(true);
        }


        if (aTPerson != null && !ObjectUtils.equals(aTPerson.getObjectID(), v))
        {
            aTPerson = null;
        }

    }

    /**
     * Get the FilterID
     *
     * @return Integer
     */
    public Integer getFilterID()
    {
        return filterID;
    }


    /**
     * Set the value of FilterID
     *
     * @param v new value
     */
    public void setFilterID(Integer v) 
    {

        if (!ObjectUtils.equals(this.filterID, v))
        {
            this.filterID = v;
            setModified(true);
        }


    }

    /**
     * Get the FilterType
     *
     * @return Integer
     */
    public Integer getFilterType()
    {
        return filterType;
    }


    /**
     * Set the value of FilterType
     *
     * @param v new value
     */
    public void setFilterType(Integer v) 
    {

        if (!ObjectUtils.equals(this.filterType, v))
        {
            this.filterType = v;
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

    



    private TPerson aTPerson;

    /**
     * Declares an association between this object and a TPerson object
     *
     * @param v TPerson
     * @throws TorqueException
     */
    public void setTPerson(TPerson v) throws TorqueException
    {
        if (v == null)
        {
            setPerson((Integer) null);
        }
        else
        {
            setPerson(v.getObjectID());
        }
        aTPerson = v;
    }


    /**
     * Returns the associated TPerson object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPerson()
        throws TorqueException
    {
        if (aTPerson == null && (!ObjectUtils.equals(this.person, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.person));
        }
        return aTPerson;
    }

    /**
     * Return the associated TPerson object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPerson(Connection connection)
        throws TorqueException
    {
        if (aTPerson == null && (!ObjectUtils.equals(this.person, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.person), connection);
        }
        return aTPerson;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTPersonKey(ObjectKey key) throws TorqueException
    {

        setPerson(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTNavigatorColumns
     */
    protected List<TNavigatorColumn> collTNavigatorColumns;

    /**
     * Temporary storage of collTNavigatorColumns to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTNavigatorColumns()
    {
        if (collTNavigatorColumns == null)
        {
            collTNavigatorColumns = new ArrayList<TNavigatorColumn>();
        }
    }


    /**
     * Method called to associate a TNavigatorColumn object to this object
     * through the TNavigatorColumn foreign key attribute
     *
     * @param l TNavigatorColumn
     * @throws TorqueException
     */
    public void addTNavigatorColumn(TNavigatorColumn l) throws TorqueException
    {
        getTNavigatorColumns().add(l);
        l.setTNavigatorLayout((TNavigatorLayout) this);
    }

    /**
     * Method called to associate a TNavigatorColumn object to this object
     * through the TNavigatorColumn foreign key attribute using connection.
     *
     * @param l TNavigatorColumn
     * @throws TorqueException
     */
    public void addTNavigatorColumn(TNavigatorColumn l, Connection con) throws TorqueException
    {
        getTNavigatorColumns(con).add(l);
        l.setTNavigatorLayout((TNavigatorLayout) this);
    }

    /**
     * The criteria used to select the current contents of collTNavigatorColumns
     */
    private Criteria lastTNavigatorColumnsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTNavigatorColumns(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TNavigatorColumn> getTNavigatorColumns()
        throws TorqueException
    {
        if (collTNavigatorColumns == null)
        {
            collTNavigatorColumns = getTNavigatorColumns(new Criteria(10));
        }
        return collTNavigatorColumns;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TNavigatorLayout has previously
     * been saved, it will retrieve related TNavigatorColumns from storage.
     * If this TNavigatorLayout is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TNavigatorColumn> getTNavigatorColumns(Criteria criteria) throws TorqueException
    {
        if (collTNavigatorColumns == null)
        {
            if (isNew())
            {
               collTNavigatorColumns = new ArrayList<TNavigatorColumn>();
            }
            else
            {
                criteria.add(TNavigatorColumnPeer.NAVIGATORLAYOUT, getObjectID() );
                collTNavigatorColumns = TNavigatorColumnPeer.doSelect(criteria);
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
                criteria.add(TNavigatorColumnPeer.NAVIGATORLAYOUT, getObjectID());
                if (!lastTNavigatorColumnsCriteria.equals(criteria))
                {
                    collTNavigatorColumns = TNavigatorColumnPeer.doSelect(criteria);
                }
            }
        }
        lastTNavigatorColumnsCriteria = criteria;

        return collTNavigatorColumns;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTNavigatorColumns(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TNavigatorColumn> getTNavigatorColumns(Connection con) throws TorqueException
    {
        if (collTNavigatorColumns == null)
        {
            collTNavigatorColumns = getTNavigatorColumns(new Criteria(10), con);
        }
        return collTNavigatorColumns;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TNavigatorLayout has previously
     * been saved, it will retrieve related TNavigatorColumns from storage.
     * If this TNavigatorLayout is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TNavigatorColumn> getTNavigatorColumns(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTNavigatorColumns == null)
        {
            if (isNew())
            {
               collTNavigatorColumns = new ArrayList<TNavigatorColumn>();
            }
            else
            {
                 criteria.add(TNavigatorColumnPeer.NAVIGATORLAYOUT, getObjectID());
                 collTNavigatorColumns = TNavigatorColumnPeer.doSelect(criteria, con);
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
                 criteria.add(TNavigatorColumnPeer.NAVIGATORLAYOUT, getObjectID());
                 if (!lastTNavigatorColumnsCriteria.equals(criteria))
                 {
                     collTNavigatorColumns = TNavigatorColumnPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTNavigatorColumnsCriteria = criteria;

         return collTNavigatorColumns;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TNavigatorLayout is new, it will return
     * an empty collection; or if this TNavigatorLayout has previously
     * been saved, it will retrieve related TNavigatorColumns from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TNavigatorLayout.
     */
    protected List<TNavigatorColumn> getTNavigatorColumnsJoinTNavigatorLayout(Criteria criteria)
        throws TorqueException
    {
        if (collTNavigatorColumns == null)
        {
            if (isNew())
            {
               collTNavigatorColumns = new ArrayList<TNavigatorColumn>();
            }
            else
            {
                criteria.add(TNavigatorColumnPeer.NAVIGATORLAYOUT, getObjectID());
                collTNavigatorColumns = TNavigatorColumnPeer.doSelectJoinTNavigatorLayout(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TNavigatorColumnPeer.NAVIGATORLAYOUT, getObjectID());
            if (!lastTNavigatorColumnsCriteria.equals(criteria))
            {
                collTNavigatorColumns = TNavigatorColumnPeer.doSelectJoinTNavigatorLayout(criteria);
            }
        }
        lastTNavigatorColumnsCriteria = criteria;

        return collTNavigatorColumns;
    }





    /**
     * Collection to store aggregation of collTNavigatorGroupingSortings
     */
    protected List<TNavigatorGroupingSorting> collTNavigatorGroupingSortings;

    /**
     * Temporary storage of collTNavigatorGroupingSortings to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTNavigatorGroupingSortings()
    {
        if (collTNavigatorGroupingSortings == null)
        {
            collTNavigatorGroupingSortings = new ArrayList<TNavigatorGroupingSorting>();
        }
    }


    /**
     * Method called to associate a TNavigatorGroupingSorting object to this object
     * through the TNavigatorGroupingSorting foreign key attribute
     *
     * @param l TNavigatorGroupingSorting
     * @throws TorqueException
     */
    public void addTNavigatorGroupingSorting(TNavigatorGroupingSorting l) throws TorqueException
    {
        getTNavigatorGroupingSortings().add(l);
        l.setTNavigatorLayout((TNavigatorLayout) this);
    }

    /**
     * Method called to associate a TNavigatorGroupingSorting object to this object
     * through the TNavigatorGroupingSorting foreign key attribute using connection.
     *
     * @param l TNavigatorGroupingSorting
     * @throws TorqueException
     */
    public void addTNavigatorGroupingSorting(TNavigatorGroupingSorting l, Connection con) throws TorqueException
    {
        getTNavigatorGroupingSortings(con).add(l);
        l.setTNavigatorLayout((TNavigatorLayout) this);
    }

    /**
     * The criteria used to select the current contents of collTNavigatorGroupingSortings
     */
    private Criteria lastTNavigatorGroupingSortingsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTNavigatorGroupingSortings(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TNavigatorGroupingSorting> getTNavigatorGroupingSortings()
        throws TorqueException
    {
        if (collTNavigatorGroupingSortings == null)
        {
            collTNavigatorGroupingSortings = getTNavigatorGroupingSortings(new Criteria(10));
        }
        return collTNavigatorGroupingSortings;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TNavigatorLayout has previously
     * been saved, it will retrieve related TNavigatorGroupingSortings from storage.
     * If this TNavigatorLayout is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TNavigatorGroupingSorting> getTNavigatorGroupingSortings(Criteria criteria) throws TorqueException
    {
        if (collTNavigatorGroupingSortings == null)
        {
            if (isNew())
            {
               collTNavigatorGroupingSortings = new ArrayList<TNavigatorGroupingSorting>();
            }
            else
            {
                criteria.add(TNavigatorGroupingSortingPeer.NAVIGATORLAYOUT, getObjectID() );
                collTNavigatorGroupingSortings = TNavigatorGroupingSortingPeer.doSelect(criteria);
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
                criteria.add(TNavigatorGroupingSortingPeer.NAVIGATORLAYOUT, getObjectID());
                if (!lastTNavigatorGroupingSortingsCriteria.equals(criteria))
                {
                    collTNavigatorGroupingSortings = TNavigatorGroupingSortingPeer.doSelect(criteria);
                }
            }
        }
        lastTNavigatorGroupingSortingsCriteria = criteria;

        return collTNavigatorGroupingSortings;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTNavigatorGroupingSortings(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TNavigatorGroupingSorting> getTNavigatorGroupingSortings(Connection con) throws TorqueException
    {
        if (collTNavigatorGroupingSortings == null)
        {
            collTNavigatorGroupingSortings = getTNavigatorGroupingSortings(new Criteria(10), con);
        }
        return collTNavigatorGroupingSortings;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TNavigatorLayout has previously
     * been saved, it will retrieve related TNavigatorGroupingSortings from storage.
     * If this TNavigatorLayout is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TNavigatorGroupingSorting> getTNavigatorGroupingSortings(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTNavigatorGroupingSortings == null)
        {
            if (isNew())
            {
               collTNavigatorGroupingSortings = new ArrayList<TNavigatorGroupingSorting>();
            }
            else
            {
                 criteria.add(TNavigatorGroupingSortingPeer.NAVIGATORLAYOUT, getObjectID());
                 collTNavigatorGroupingSortings = TNavigatorGroupingSortingPeer.doSelect(criteria, con);
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
                 criteria.add(TNavigatorGroupingSortingPeer.NAVIGATORLAYOUT, getObjectID());
                 if (!lastTNavigatorGroupingSortingsCriteria.equals(criteria))
                 {
                     collTNavigatorGroupingSortings = TNavigatorGroupingSortingPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTNavigatorGroupingSortingsCriteria = criteria;

         return collTNavigatorGroupingSortings;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TNavigatorLayout is new, it will return
     * an empty collection; or if this TNavigatorLayout has previously
     * been saved, it will retrieve related TNavigatorGroupingSortings from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TNavigatorLayout.
     */
    protected List<TNavigatorGroupingSorting> getTNavigatorGroupingSortingsJoinTNavigatorLayout(Criteria criteria)
        throws TorqueException
    {
        if (collTNavigatorGroupingSortings == null)
        {
            if (isNew())
            {
               collTNavigatorGroupingSortings = new ArrayList<TNavigatorGroupingSorting>();
            }
            else
            {
                criteria.add(TNavigatorGroupingSortingPeer.NAVIGATORLAYOUT, getObjectID());
                collTNavigatorGroupingSortings = TNavigatorGroupingSortingPeer.doSelectJoinTNavigatorLayout(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TNavigatorGroupingSortingPeer.NAVIGATORLAYOUT, getObjectID());
            if (!lastTNavigatorGroupingSortingsCriteria.equals(criteria))
            {
                collTNavigatorGroupingSortings = TNavigatorGroupingSortingPeer.doSelectJoinTNavigatorLayout(criteria);
            }
        }
        lastTNavigatorGroupingSortingsCriteria = criteria;

        return collTNavigatorGroupingSortings;
    }





    /**
     * Collection to store aggregation of collTCardGroupingFields
     */
    protected List<TCardGroupingField> collTCardGroupingFields;

    /**
     * Temporary storage of collTCardGroupingFields to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTCardGroupingFields()
    {
        if (collTCardGroupingFields == null)
        {
            collTCardGroupingFields = new ArrayList<TCardGroupingField>();
        }
    }


    /**
     * Method called to associate a TCardGroupingField object to this object
     * through the TCardGroupingField foreign key attribute
     *
     * @param l TCardGroupingField
     * @throws TorqueException
     */
    public void addTCardGroupingField(TCardGroupingField l) throws TorqueException
    {
        getTCardGroupingFields().add(l);
        l.setTNavigatorLayout((TNavigatorLayout) this);
    }

    /**
     * Method called to associate a TCardGroupingField object to this object
     * through the TCardGroupingField foreign key attribute using connection.
     *
     * @param l TCardGroupingField
     * @throws TorqueException
     */
    public void addTCardGroupingField(TCardGroupingField l, Connection con) throws TorqueException
    {
        getTCardGroupingFields(con).add(l);
        l.setTNavigatorLayout((TNavigatorLayout) this);
    }

    /**
     * The criteria used to select the current contents of collTCardGroupingFields
     */
    private Criteria lastTCardGroupingFieldsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTCardGroupingFields(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TCardGroupingField> getTCardGroupingFields()
        throws TorqueException
    {
        if (collTCardGroupingFields == null)
        {
            collTCardGroupingFields = getTCardGroupingFields(new Criteria(10));
        }
        return collTCardGroupingFields;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TNavigatorLayout has previously
     * been saved, it will retrieve related TCardGroupingFields from storage.
     * If this TNavigatorLayout is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TCardGroupingField> getTCardGroupingFields(Criteria criteria) throws TorqueException
    {
        if (collTCardGroupingFields == null)
        {
            if (isNew())
            {
               collTCardGroupingFields = new ArrayList<TCardGroupingField>();
            }
            else
            {
                criteria.add(TCardGroupingFieldPeer.NAVIGATORLAYOUT, getObjectID() );
                collTCardGroupingFields = TCardGroupingFieldPeer.doSelect(criteria);
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
                criteria.add(TCardGroupingFieldPeer.NAVIGATORLAYOUT, getObjectID());
                if (!lastTCardGroupingFieldsCriteria.equals(criteria))
                {
                    collTCardGroupingFields = TCardGroupingFieldPeer.doSelect(criteria);
                }
            }
        }
        lastTCardGroupingFieldsCriteria = criteria;

        return collTCardGroupingFields;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTCardGroupingFields(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TCardGroupingField> getTCardGroupingFields(Connection con) throws TorqueException
    {
        if (collTCardGroupingFields == null)
        {
            collTCardGroupingFields = getTCardGroupingFields(new Criteria(10), con);
        }
        return collTCardGroupingFields;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TNavigatorLayout has previously
     * been saved, it will retrieve related TCardGroupingFields from storage.
     * If this TNavigatorLayout is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TCardGroupingField> getTCardGroupingFields(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTCardGroupingFields == null)
        {
            if (isNew())
            {
               collTCardGroupingFields = new ArrayList<TCardGroupingField>();
            }
            else
            {
                 criteria.add(TCardGroupingFieldPeer.NAVIGATORLAYOUT, getObjectID());
                 collTCardGroupingFields = TCardGroupingFieldPeer.doSelect(criteria, con);
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
                 criteria.add(TCardGroupingFieldPeer.NAVIGATORLAYOUT, getObjectID());
                 if (!lastTCardGroupingFieldsCriteria.equals(criteria))
                 {
                     collTCardGroupingFields = TCardGroupingFieldPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTCardGroupingFieldsCriteria = criteria;

         return collTCardGroupingFields;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TNavigatorLayout is new, it will return
     * an empty collection; or if this TNavigatorLayout has previously
     * been saved, it will retrieve related TCardGroupingFields from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TNavigatorLayout.
     */
    protected List<TCardGroupingField> getTCardGroupingFieldsJoinTNavigatorLayout(Criteria criteria)
        throws TorqueException
    {
        if (collTCardGroupingFields == null)
        {
            if (isNew())
            {
               collTCardGroupingFields = new ArrayList<TCardGroupingField>();
            }
            else
            {
                criteria.add(TCardGroupingFieldPeer.NAVIGATORLAYOUT, getObjectID());
                collTCardGroupingFields = TCardGroupingFieldPeer.doSelectJoinTNavigatorLayout(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TCardGroupingFieldPeer.NAVIGATORLAYOUT, getObjectID());
            if (!lastTCardGroupingFieldsCriteria.equals(criteria))
            {
                collTCardGroupingFields = TCardGroupingFieldPeer.doSelectJoinTNavigatorLayout(criteria);
            }
        }
        lastTCardGroupingFieldsCriteria = criteria;

        return collTCardGroupingFields;
    }





    /**
     * Collection to store aggregation of collTViewParams
     */
    protected List<TViewParam> collTViewParams;

    /**
     * Temporary storage of collTViewParams to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTViewParams()
    {
        if (collTViewParams == null)
        {
            collTViewParams = new ArrayList<TViewParam>();
        }
    }


    /**
     * Method called to associate a TViewParam object to this object
     * through the TViewParam foreign key attribute
     *
     * @param l TViewParam
     * @throws TorqueException
     */
    public void addTViewParam(TViewParam l) throws TorqueException
    {
        getTViewParams().add(l);
        l.setTNavigatorLayout((TNavigatorLayout) this);
    }

    /**
     * Method called to associate a TViewParam object to this object
     * through the TViewParam foreign key attribute using connection.
     *
     * @param l TViewParam
     * @throws TorqueException
     */
    public void addTViewParam(TViewParam l, Connection con) throws TorqueException
    {
        getTViewParams(con).add(l);
        l.setTNavigatorLayout((TNavigatorLayout) this);
    }

    /**
     * The criteria used to select the current contents of collTViewParams
     */
    private Criteria lastTViewParamsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTViewParams(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TViewParam> getTViewParams()
        throws TorqueException
    {
        if (collTViewParams == null)
        {
            collTViewParams = getTViewParams(new Criteria(10));
        }
        return collTViewParams;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TNavigatorLayout has previously
     * been saved, it will retrieve related TViewParams from storage.
     * If this TNavigatorLayout is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TViewParam> getTViewParams(Criteria criteria) throws TorqueException
    {
        if (collTViewParams == null)
        {
            if (isNew())
            {
               collTViewParams = new ArrayList<TViewParam>();
            }
            else
            {
                criteria.add(TViewParamPeer.NAVIGATORLAYOUT, getObjectID() );
                collTViewParams = TViewParamPeer.doSelect(criteria);
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
                criteria.add(TViewParamPeer.NAVIGATORLAYOUT, getObjectID());
                if (!lastTViewParamsCriteria.equals(criteria))
                {
                    collTViewParams = TViewParamPeer.doSelect(criteria);
                }
            }
        }
        lastTViewParamsCriteria = criteria;

        return collTViewParams;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTViewParams(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TViewParam> getTViewParams(Connection con) throws TorqueException
    {
        if (collTViewParams == null)
        {
            collTViewParams = getTViewParams(new Criteria(10), con);
        }
        return collTViewParams;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TNavigatorLayout has previously
     * been saved, it will retrieve related TViewParams from storage.
     * If this TNavigatorLayout is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TViewParam> getTViewParams(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTViewParams == null)
        {
            if (isNew())
            {
               collTViewParams = new ArrayList<TViewParam>();
            }
            else
            {
                 criteria.add(TViewParamPeer.NAVIGATORLAYOUT, getObjectID());
                 collTViewParams = TViewParamPeer.doSelect(criteria, con);
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
                 criteria.add(TViewParamPeer.NAVIGATORLAYOUT, getObjectID());
                 if (!lastTViewParamsCriteria.equals(criteria))
                 {
                     collTViewParams = TViewParamPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTViewParamsCriteria = criteria;

         return collTViewParams;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TNavigatorLayout is new, it will return
     * an empty collection; or if this TNavigatorLayout has previously
     * been saved, it will retrieve related TViewParams from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TNavigatorLayout.
     */
    protected List<TViewParam> getTViewParamsJoinTNavigatorLayout(Criteria criteria)
        throws TorqueException
    {
        if (collTViewParams == null)
        {
            if (isNew())
            {
               collTViewParams = new ArrayList<TViewParam>();
            }
            else
            {
                criteria.add(TViewParamPeer.NAVIGATORLAYOUT, getObjectID());
                collTViewParams = TViewParamPeer.doSelectJoinTNavigatorLayout(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TViewParamPeer.NAVIGATORLAYOUT, getObjectID());
            if (!lastTViewParamsCriteria.equals(criteria))
            {
                collTViewParams = TViewParamPeer.doSelectJoinTNavigatorLayout(criteria);
            }
        }
        lastTViewParamsCriteria = criteria;

        return collTViewParams;
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
            fieldNames.add("Person");
            fieldNames.add("FilterID");
            fieldNames.add("FilterType");
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
        if (name.equals("Person"))
        {
            return getPerson();
        }
        if (name.equals("FilterID"))
        {
            return getFilterID();
        }
        if (name.equals("FilterType"))
        {
            return getFilterType();
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
        if (name.equals("Person"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setPerson((Integer) value);
            return true;
        }
        if (name.equals("FilterID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFilterID((Integer) value);
            return true;
        }
        if (name.equals("FilterType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFilterType((Integer) value);
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
        if (name.equals(TNavigatorLayoutPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TNavigatorLayoutPeer.PERSON))
        {
            return getPerson();
        }
        if (name.equals(TNavigatorLayoutPeer.FILTERID))
        {
            return getFilterID();
        }
        if (name.equals(TNavigatorLayoutPeer.FILTERTYPE))
        {
            return getFilterType();
        }
        if (name.equals(TNavigatorLayoutPeer.TPUUID))
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
      if (TNavigatorLayoutPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TNavigatorLayoutPeer.PERSON.equals(name))
        {
            return setByName("Person", value);
        }
      if (TNavigatorLayoutPeer.FILTERID.equals(name))
        {
            return setByName("FilterID", value);
        }
      if (TNavigatorLayoutPeer.FILTERTYPE.equals(name))
        {
            return setByName("FilterType", value);
        }
      if (TNavigatorLayoutPeer.TPUUID.equals(name))
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
            return getPerson();
        }
        if (pos == 2)
        {
            return getFilterID();
        }
        if (pos == 3)
        {
            return getFilterType();
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
            return setByName("Person", value);
        }
    if (position == 2)
        {
            return setByName("FilterID", value);
        }
    if (position == 3)
        {
            return setByName("FilterType", value);
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
        save(TNavigatorLayoutPeer.DATABASE_NAME);
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
                    TNavigatorLayoutPeer.doInsert((TNavigatorLayout) this, con);
                    setNew(false);
                }
                else
                {
                    TNavigatorLayoutPeer.doUpdate((TNavigatorLayout) this, con);
                }
            }


            if (collTNavigatorColumns != null)
            {
                for (int i = 0; i < collTNavigatorColumns.size(); i++)
                {
                    ((TNavigatorColumn) collTNavigatorColumns.get(i)).save(con);
                }
            }

            if (collTNavigatorGroupingSortings != null)
            {
                for (int i = 0; i < collTNavigatorGroupingSortings.size(); i++)
                {
                    ((TNavigatorGroupingSorting) collTNavigatorGroupingSortings.get(i)).save(con);
                }
            }

            if (collTCardGroupingFields != null)
            {
                for (int i = 0; i < collTCardGroupingFields.size(); i++)
                {
                    ((TCardGroupingField) collTCardGroupingFields.get(i)).save(con);
                }
            }

            if (collTViewParams != null)
            {
                for (int i = 0; i < collTViewParams.size(); i++)
                {
                    ((TViewParam) collTViewParams.get(i)).save(con);
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
    public TNavigatorLayout copy() throws TorqueException
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
    public TNavigatorLayout copy(Connection con) throws TorqueException
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
    public TNavigatorLayout copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TNavigatorLayout(), deepcopy);
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
    public TNavigatorLayout copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TNavigatorLayout(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TNavigatorLayout copyInto(TNavigatorLayout copyObj) throws TorqueException
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
    protected TNavigatorLayout copyInto(TNavigatorLayout copyObj, Connection con) throws TorqueException
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
    protected TNavigatorLayout copyInto(TNavigatorLayout copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setPerson(person);
        copyObj.setFilterID(filterID);
        copyObj.setFilterType(filterType);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TNavigatorColumn> vTNavigatorColumns = getTNavigatorColumns();
        if (vTNavigatorColumns != null)
        {
            for (int i = 0; i < vTNavigatorColumns.size(); i++)
            {
                TNavigatorColumn obj =  vTNavigatorColumns.get(i);
                copyObj.addTNavigatorColumn(obj.copy());
            }
        }
        else
        {
            copyObj.collTNavigatorColumns = null;
        }


        List<TNavigatorGroupingSorting> vTNavigatorGroupingSortings = getTNavigatorGroupingSortings();
        if (vTNavigatorGroupingSortings != null)
        {
            for (int i = 0; i < vTNavigatorGroupingSortings.size(); i++)
            {
                TNavigatorGroupingSorting obj =  vTNavigatorGroupingSortings.get(i);
                copyObj.addTNavigatorGroupingSorting(obj.copy());
            }
        }
        else
        {
            copyObj.collTNavigatorGroupingSortings = null;
        }


        List<TCardGroupingField> vTCardGroupingFields = getTCardGroupingFields();
        if (vTCardGroupingFields != null)
        {
            for (int i = 0; i < vTCardGroupingFields.size(); i++)
            {
                TCardGroupingField obj =  vTCardGroupingFields.get(i);
                copyObj.addTCardGroupingField(obj.copy());
            }
        }
        else
        {
            copyObj.collTCardGroupingFields = null;
        }


        List<TViewParam> vTViewParams = getTViewParams();
        if (vTViewParams != null)
        {
            for (int i = 0; i < vTViewParams.size(); i++)
            {
                TViewParam obj =  vTViewParams.get(i);
                copyObj.addTViewParam(obj.copy());
            }
        }
        else
        {
            copyObj.collTViewParams = null;
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
    protected TNavigatorLayout copyInto(TNavigatorLayout copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setPerson(person);
        copyObj.setFilterID(filterID);
        copyObj.setFilterType(filterType);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TNavigatorColumn> vTNavigatorColumns = getTNavigatorColumns(con);
        if (vTNavigatorColumns != null)
        {
            for (int i = 0; i < vTNavigatorColumns.size(); i++)
            {
                TNavigatorColumn obj =  vTNavigatorColumns.get(i);
                copyObj.addTNavigatorColumn(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTNavigatorColumns = null;
        }


        List<TNavigatorGroupingSorting> vTNavigatorGroupingSortings = getTNavigatorGroupingSortings(con);
        if (vTNavigatorGroupingSortings != null)
        {
            for (int i = 0; i < vTNavigatorGroupingSortings.size(); i++)
            {
                TNavigatorGroupingSorting obj =  vTNavigatorGroupingSortings.get(i);
                copyObj.addTNavigatorGroupingSorting(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTNavigatorGroupingSortings = null;
        }


        List<TCardGroupingField> vTCardGroupingFields = getTCardGroupingFields(con);
        if (vTCardGroupingFields != null)
        {
            for (int i = 0; i < vTCardGroupingFields.size(); i++)
            {
                TCardGroupingField obj =  vTCardGroupingFields.get(i);
                copyObj.addTCardGroupingField(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTCardGroupingFields = null;
        }


        List<TViewParam> vTViewParams = getTViewParams(con);
        if (vTViewParams != null)
        {
            for (int i = 0; i < vTViewParams.size(); i++)
            {
                TViewParam obj =  vTViewParams.get(i);
                copyObj.addTViewParam(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTViewParams = null;
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
    public TNavigatorLayoutPeer getPeer()
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
        return TNavigatorLayoutPeer.getTableMap();
    }

  
    /**
     * Creates a TNavigatorLayoutBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TNavigatorLayoutBean with the contents of this object
     */
    public TNavigatorLayoutBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TNavigatorLayoutBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TNavigatorLayoutBean with the contents of this object
     */
    public TNavigatorLayoutBean getBean(IdentityMap createdBeans)
    {
        TNavigatorLayoutBean result = (TNavigatorLayoutBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TNavigatorLayoutBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setPerson(getPerson());
        result.setFilterID(getFilterID());
        result.setFilterType(getFilterType());
        result.setUuid(getUuid());



        if (collTNavigatorColumns != null)
        {
            List<TNavigatorColumnBean> relatedBeans = new ArrayList<TNavigatorColumnBean>(collTNavigatorColumns.size());
            for (Iterator<TNavigatorColumn> collTNavigatorColumnsIt = collTNavigatorColumns.iterator(); collTNavigatorColumnsIt.hasNext(); )
            {
                TNavigatorColumn related = (TNavigatorColumn) collTNavigatorColumnsIt.next();
                TNavigatorColumnBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTNavigatorColumnBeans(relatedBeans);
        }


        if (collTNavigatorGroupingSortings != null)
        {
            List<TNavigatorGroupingSortingBean> relatedBeans = new ArrayList<TNavigatorGroupingSortingBean>(collTNavigatorGroupingSortings.size());
            for (Iterator<TNavigatorGroupingSorting> collTNavigatorGroupingSortingsIt = collTNavigatorGroupingSortings.iterator(); collTNavigatorGroupingSortingsIt.hasNext(); )
            {
                TNavigatorGroupingSorting related = (TNavigatorGroupingSorting) collTNavigatorGroupingSortingsIt.next();
                TNavigatorGroupingSortingBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTNavigatorGroupingSortingBeans(relatedBeans);
        }


        if (collTCardGroupingFields != null)
        {
            List<TCardGroupingFieldBean> relatedBeans = new ArrayList<TCardGroupingFieldBean>(collTCardGroupingFields.size());
            for (Iterator<TCardGroupingField> collTCardGroupingFieldsIt = collTCardGroupingFields.iterator(); collTCardGroupingFieldsIt.hasNext(); )
            {
                TCardGroupingField related = (TCardGroupingField) collTCardGroupingFieldsIt.next();
                TCardGroupingFieldBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTCardGroupingFieldBeans(relatedBeans);
        }


        if (collTViewParams != null)
        {
            List<TViewParamBean> relatedBeans = new ArrayList<TViewParamBean>(collTViewParams.size());
            for (Iterator<TViewParam> collTViewParamsIt = collTViewParams.iterator(); collTViewParamsIt.hasNext(); )
            {
                TViewParam related = (TViewParam) collTViewParamsIt.next();
                TViewParamBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTViewParamBeans(relatedBeans);
        }




        if (aTPerson != null)
        {
            TPersonBean relatedBean = aTPerson.getBean(createdBeans);
            result.setTPersonBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TNavigatorLayout with the contents
     * of a TNavigatorLayoutBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TNavigatorLayoutBean which contents are used to create
     *        the resulting class
     * @return an instance of TNavigatorLayout with the contents of bean
     */
    public static TNavigatorLayout createTNavigatorLayout(TNavigatorLayoutBean bean)
        throws TorqueException
    {
        return createTNavigatorLayout(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TNavigatorLayout with the contents
     * of a TNavigatorLayoutBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TNavigatorLayoutBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TNavigatorLayout with the contents of bean
     */

    public static TNavigatorLayout createTNavigatorLayout(TNavigatorLayoutBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TNavigatorLayout result = (TNavigatorLayout) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TNavigatorLayout();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setPerson(bean.getPerson());
        result.setFilterID(bean.getFilterID());
        result.setFilterType(bean.getFilterType());
        result.setUuid(bean.getUuid());



        {
            List<TNavigatorColumnBean> relatedBeans = bean.getTNavigatorColumnBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TNavigatorColumnBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TNavigatorColumnBean relatedBean =  relatedBeansIt.next();
                    TNavigatorColumn related = TNavigatorColumn.createTNavigatorColumn(relatedBean, createdObjects);
                    result.addTNavigatorColumnFromBean(related);
                }
            }
        }


        {
            List<TNavigatorGroupingSortingBean> relatedBeans = bean.getTNavigatorGroupingSortingBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TNavigatorGroupingSortingBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TNavigatorGroupingSortingBean relatedBean =  relatedBeansIt.next();
                    TNavigatorGroupingSorting related = TNavigatorGroupingSorting.createTNavigatorGroupingSorting(relatedBean, createdObjects);
                    result.addTNavigatorGroupingSortingFromBean(related);
                }
            }
        }


        {
            List<TCardGroupingFieldBean> relatedBeans = bean.getTCardGroupingFieldBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TCardGroupingFieldBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TCardGroupingFieldBean relatedBean =  relatedBeansIt.next();
                    TCardGroupingField related = TCardGroupingField.createTCardGroupingField(relatedBean, createdObjects);
                    result.addTCardGroupingFieldFromBean(related);
                }
            }
        }


        {
            List<TViewParamBean> relatedBeans = bean.getTViewParamBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TViewParamBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TViewParamBean relatedBean =  relatedBeansIt.next();
                    TViewParam related = TViewParam.createTViewParam(relatedBean, createdObjects);
                    result.addTViewParamFromBean(related);
                }
            }
        }




        {
            TPersonBean relatedBean = bean.getTPersonBean();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPerson(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TNavigatorColumn object to this object.
     * through the TNavigatorColumn foreign key attribute
     *
     * @param toAdd TNavigatorColumn
     */
    protected void addTNavigatorColumnFromBean(TNavigatorColumn toAdd)
    {
        initTNavigatorColumns();
        collTNavigatorColumns.add(toAdd);
    }


    /**
     * Method called to associate a TNavigatorGroupingSorting object to this object.
     * through the TNavigatorGroupingSorting foreign key attribute
     *
     * @param toAdd TNavigatorGroupingSorting
     */
    protected void addTNavigatorGroupingSortingFromBean(TNavigatorGroupingSorting toAdd)
    {
        initTNavigatorGroupingSortings();
        collTNavigatorGroupingSortings.add(toAdd);
    }


    /**
     * Method called to associate a TCardGroupingField object to this object.
     * through the TCardGroupingField foreign key attribute
     *
     * @param toAdd TCardGroupingField
     */
    protected void addTCardGroupingFieldFromBean(TCardGroupingField toAdd)
    {
        initTCardGroupingFields();
        collTCardGroupingFields.add(toAdd);
    }


    /**
     * Method called to associate a TViewParam object to this object.
     * through the TViewParam foreign key attribute
     *
     * @param toAdd TViewParam
     */
    protected void addTViewParamFromBean(TViewParam toAdd)
    {
        initTViewParams();
        collTViewParams.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TNavigatorLayout:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Person = ")
           .append(getPerson())
           .append("\n");
        str.append("FilterID = ")
           .append(getFilterID())
           .append("\n");
        str.append("FilterType = ")
           .append(getFilterType())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
