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

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TBLOBBean;

import com.aurel.track.beans.TPpriorityBean;
import com.aurel.track.beans.TPseverityBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TDisableFieldBean;
import com.aurel.track.beans.TPlistTypeBean;
import com.aurel.track.beans.TPstateBean;
import com.aurel.track.beans.TWorkFlowCategoryBean;
import com.aurel.track.beans.TRoleListTypeBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TScreenConfigBean;
import com.aurel.track.beans.TInitStateBean;
import com.aurel.track.beans.TChildIssueTypeBean;
import com.aurel.track.beans.TChildIssueTypeBean;
import com.aurel.track.beans.TMailTemplateConfigBean;
import com.aurel.track.beans.TWfActivityContextParamsBean;
import com.aurel.track.beans.TWorkflowConnectBean;


/**
 * Holds all known issue types.
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TListType
 */
public abstract class BaseTListType extends TpBaseObject
{
    /** The Peer class */
    private static final TListTypePeer peer =
        new TListTypePeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the label field */
    private String label;

    /** The value for the tooltip field */
    private String tooltip;

    /** The value for the typeflag field */
    private Integer typeflag;

    /** The value for the sortorder field */
    private Integer sortorder;

    /** The value for the symbol field */
    private String symbol;

    /** The value for the iconKey field */
    private Integer iconKey;

    /** The value for the iconChanged field */
    private String iconChanged = "N";

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



        // update associated TPpriority
        if (collTPprioritys != null)
        {
            for (int i = 0; i < collTPprioritys.size(); i++)
            {
                ((TPpriority) collTPprioritys.get(i))
                        .setListType(v);
            }
        }

        // update associated TPseverity
        if (collTPseveritys != null)
        {
            for (int i = 0; i < collTPseveritys.size(); i++)
            {
                ((TPseverity) collTPseveritys.get(i))
                        .setListType(v);
            }
        }

        // update associated TWorkItem
        if (collTWorkItems != null)
        {
            for (int i = 0; i < collTWorkItems.size(); i++)
            {
                ((TWorkItem) collTWorkItems.get(i))
                        .setListTypeID(v);
            }
        }

        // update associated TDisableField
        if (collTDisableFields != null)
        {
            for (int i = 0; i < collTDisableFields.size(); i++)
            {
                ((TDisableField) collTDisableFields.get(i))
                        .setListType(v);
            }
        }

        // update associated TPlistType
        if (collTPlistTypes != null)
        {
            for (int i = 0; i < collTPlistTypes.size(); i++)
            {
                ((TPlistType) collTPlistTypes.get(i))
                        .setCategory(v);
            }
        }

        // update associated TPstate
        if (collTPstates != null)
        {
            for (int i = 0; i < collTPstates.size(); i++)
            {
                ((TPstate) collTPstates.get(i))
                        .setListType(v);
            }
        }

        // update associated TWorkFlowCategory
        if (collTWorkFlowCategorys != null)
        {
            for (int i = 0; i < collTWorkFlowCategorys.size(); i++)
            {
                ((TWorkFlowCategory) collTWorkFlowCategorys.get(i))
                        .setCategory(v);
            }
        }

        // update associated TRoleListType
        if (collTRoleListTypes != null)
        {
            for (int i = 0; i < collTRoleListTypes.size(); i++)
            {
                ((TRoleListType) collTRoleListTypes.get(i))
                        .setListType(v);
            }
        }

        // update associated TFieldConfig
        if (collTFieldConfigs != null)
        {
            for (int i = 0; i < collTFieldConfigs.size(); i++)
            {
                ((TFieldConfig) collTFieldConfigs.get(i))
                        .setIssueType(v);
            }
        }

        // update associated TScreenConfig
        if (collTScreenConfigs != null)
        {
            for (int i = 0; i < collTScreenConfigs.size(); i++)
            {
                ((TScreenConfig) collTScreenConfigs.get(i))
                        .setIssueType(v);
            }
        }

        // update associated TInitState
        if (collTInitStates != null)
        {
            for (int i = 0; i < collTInitStates.size(); i++)
            {
                ((TInitState) collTInitStates.get(i))
                        .setListType(v);
            }
        }

        // update associated TChildIssueType
        if (collTChildIssueTypesRelatedByIssueTypeParent != null)
        {
            for (int i = 0; i < collTChildIssueTypesRelatedByIssueTypeParent.size(); i++)
            {
                ((TChildIssueType) collTChildIssueTypesRelatedByIssueTypeParent.get(i))
                        .setIssueTypeParent(v);
            }
        }

        // update associated TChildIssueType
        if (collTChildIssueTypesRelatedByIssueTypeChild != null)
        {
            for (int i = 0; i < collTChildIssueTypesRelatedByIssueTypeChild.size(); i++)
            {
                ((TChildIssueType) collTChildIssueTypesRelatedByIssueTypeChild.get(i))
                        .setIssueTypeChild(v);
            }
        }

        // update associated TMailTemplateConfig
        if (collTMailTemplateConfigs != null)
        {
            for (int i = 0; i < collTMailTemplateConfigs.size(); i++)
            {
                ((TMailTemplateConfig) collTMailTemplateConfigs.get(i))
                        .setIssueType(v);
            }
        }

        // update associated TWfActivityContextParams
        if (collTWfActivityContextParamss != null)
        {
            for (int i = 0; i < collTWfActivityContextParamss.size(); i++)
            {
                ((TWfActivityContextParams) collTWfActivityContextParamss.get(i))
                        .setIssueType(v);
            }
        }

        // update associated TWorkflowConnect
        if (collTWorkflowConnects != null)
        {
            for (int i = 0; i < collTWorkflowConnects.size(); i++)
            {
                ((TWorkflowConnect) collTWorkflowConnects.get(i))
                        .setIssueType(v);
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
     * Get the Tooltip
     *
     * @return String
     */
    public String getTooltip()
    {
        return tooltip;
    }


    /**
     * Set the value of Tooltip
     *
     * @param v new value
     */
    public void setTooltip(String v) 
    {

        if (!ObjectUtils.equals(this.tooltip, v))
        {
            this.tooltip = v;
            setModified(true);
        }


    }

    /**
     * Get the Typeflag
     *
     * @return Integer
     */
    public Integer getTypeflag()
    {
        return typeflag;
    }


    /**
     * Set the value of Typeflag
     *
     * @param v new value
     */
    public void setTypeflag(Integer v) 
    {

        if (!ObjectUtils.equals(this.typeflag, v))
        {
            this.typeflag = v;
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
     * Get the IconChanged
     *
     * @return String
     */
    public String getIconChanged()
    {
        return iconChanged;
    }


    /**
     * Set the value of IconChanged
     *
     * @param v new value
     */
    public void setIconChanged(String v) 
    {

        if (!ObjectUtils.equals(this.iconChanged, v))
        {
            this.iconChanged = v;
            setModified(true);
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
     * Collection to store aggregation of collTPprioritys
     */
    protected List<TPpriority> collTPprioritys;

    /**
     * Temporary storage of collTPprioritys to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTPprioritys()
    {
        if (collTPprioritys == null)
        {
            collTPprioritys = new ArrayList<TPpriority>();
        }
    }


    /**
     * Method called to associate a TPpriority object to this object
     * through the TPpriority foreign key attribute
     *
     * @param l TPpriority
     * @throws TorqueException
     */
    public void addTPpriority(TPpriority l) throws TorqueException
    {
        getTPprioritys().add(l);
        l.setTListType((TListType) this);
    }

    /**
     * Method called to associate a TPpriority object to this object
     * through the TPpriority foreign key attribute using connection.
     *
     * @param l TPpriority
     * @throws TorqueException
     */
    public void addTPpriority(TPpriority l, Connection con) throws TorqueException
    {
        getTPprioritys(con).add(l);
        l.setTListType((TListType) this);
    }

    /**
     * The criteria used to select the current contents of collTPprioritys
     */
    private Criteria lastTPprioritysCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPprioritys(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TPpriority> getTPprioritys()
        throws TorqueException
    {
        if (collTPprioritys == null)
        {
            collTPprioritys = getTPprioritys(new Criteria(10));
        }
        return collTPprioritys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TPprioritys from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TPpriority> getTPprioritys(Criteria criteria) throws TorqueException
    {
        if (collTPprioritys == null)
        {
            if (isNew())
            {
               collTPprioritys = new ArrayList<TPpriority>();
            }
            else
            {
                criteria.add(TPpriorityPeer.LISTTYPE, getObjectID() );
                collTPprioritys = TPpriorityPeer.doSelect(criteria);
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
                criteria.add(TPpriorityPeer.LISTTYPE, getObjectID());
                if (!lastTPprioritysCriteria.equals(criteria))
                {
                    collTPprioritys = TPpriorityPeer.doSelect(criteria);
                }
            }
        }
        lastTPprioritysCriteria = criteria;

        return collTPprioritys;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPprioritys(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPpriority> getTPprioritys(Connection con) throws TorqueException
    {
        if (collTPprioritys == null)
        {
            collTPprioritys = getTPprioritys(new Criteria(10), con);
        }
        return collTPprioritys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TPprioritys from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPpriority> getTPprioritys(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTPprioritys == null)
        {
            if (isNew())
            {
               collTPprioritys = new ArrayList<TPpriority>();
            }
            else
            {
                 criteria.add(TPpriorityPeer.LISTTYPE, getObjectID());
                 collTPprioritys = TPpriorityPeer.doSelect(criteria, con);
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
                 criteria.add(TPpriorityPeer.LISTTYPE, getObjectID());
                 if (!lastTPprioritysCriteria.equals(criteria))
                 {
                     collTPprioritys = TPpriorityPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTPprioritysCriteria = criteria;

         return collTPprioritys;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TPprioritys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TPpriority> getTPprioritysJoinTPriority(Criteria criteria)
        throws TorqueException
    {
        if (collTPprioritys == null)
        {
            if (isNew())
            {
               collTPprioritys = new ArrayList<TPpriority>();
            }
            else
            {
                criteria.add(TPpriorityPeer.LISTTYPE, getObjectID());
                collTPprioritys = TPpriorityPeer.doSelectJoinTPriority(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPpriorityPeer.LISTTYPE, getObjectID());
            if (!lastTPprioritysCriteria.equals(criteria))
            {
                collTPprioritys = TPpriorityPeer.doSelectJoinTPriority(criteria);
            }
        }
        lastTPprioritysCriteria = criteria;

        return collTPprioritys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TPprioritys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TPpriority> getTPprioritysJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTPprioritys == null)
        {
            if (isNew())
            {
               collTPprioritys = new ArrayList<TPpriority>();
            }
            else
            {
                criteria.add(TPpriorityPeer.LISTTYPE, getObjectID());
                collTPprioritys = TPpriorityPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPpriorityPeer.LISTTYPE, getObjectID());
            if (!lastTPprioritysCriteria.equals(criteria))
            {
                collTPprioritys = TPpriorityPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTPprioritysCriteria = criteria;

        return collTPprioritys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TPprioritys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TPpriority> getTPprioritysJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTPprioritys == null)
        {
            if (isNew())
            {
               collTPprioritys = new ArrayList<TPpriority>();
            }
            else
            {
                criteria.add(TPpriorityPeer.LISTTYPE, getObjectID());
                collTPprioritys = TPpriorityPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPpriorityPeer.LISTTYPE, getObjectID());
            if (!lastTPprioritysCriteria.equals(criteria))
            {
                collTPprioritys = TPpriorityPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTPprioritysCriteria = criteria;

        return collTPprioritys;
    }





    /**
     * Collection to store aggregation of collTPseveritys
     */
    protected List<TPseverity> collTPseveritys;

    /**
     * Temporary storage of collTPseveritys to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTPseveritys()
    {
        if (collTPseveritys == null)
        {
            collTPseveritys = new ArrayList<TPseverity>();
        }
    }


    /**
     * Method called to associate a TPseverity object to this object
     * through the TPseverity foreign key attribute
     *
     * @param l TPseverity
     * @throws TorqueException
     */
    public void addTPseverity(TPseverity l) throws TorqueException
    {
        getTPseveritys().add(l);
        l.setTListType((TListType) this);
    }

    /**
     * Method called to associate a TPseverity object to this object
     * through the TPseverity foreign key attribute using connection.
     *
     * @param l TPseverity
     * @throws TorqueException
     */
    public void addTPseverity(TPseverity l, Connection con) throws TorqueException
    {
        getTPseveritys(con).add(l);
        l.setTListType((TListType) this);
    }

    /**
     * The criteria used to select the current contents of collTPseveritys
     */
    private Criteria lastTPseveritysCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPseveritys(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TPseverity> getTPseveritys()
        throws TorqueException
    {
        if (collTPseveritys == null)
        {
            collTPseveritys = getTPseveritys(new Criteria(10));
        }
        return collTPseveritys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TPseveritys from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TPseverity> getTPseveritys(Criteria criteria) throws TorqueException
    {
        if (collTPseveritys == null)
        {
            if (isNew())
            {
               collTPseveritys = new ArrayList<TPseverity>();
            }
            else
            {
                criteria.add(TPseverityPeer.LISTTYPE, getObjectID() );
                collTPseveritys = TPseverityPeer.doSelect(criteria);
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
                criteria.add(TPseverityPeer.LISTTYPE, getObjectID());
                if (!lastTPseveritysCriteria.equals(criteria))
                {
                    collTPseveritys = TPseverityPeer.doSelect(criteria);
                }
            }
        }
        lastTPseveritysCriteria = criteria;

        return collTPseveritys;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPseveritys(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPseverity> getTPseveritys(Connection con) throws TorqueException
    {
        if (collTPseveritys == null)
        {
            collTPseveritys = getTPseveritys(new Criteria(10), con);
        }
        return collTPseveritys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TPseveritys from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPseverity> getTPseveritys(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTPseveritys == null)
        {
            if (isNew())
            {
               collTPseveritys = new ArrayList<TPseverity>();
            }
            else
            {
                 criteria.add(TPseverityPeer.LISTTYPE, getObjectID());
                 collTPseveritys = TPseverityPeer.doSelect(criteria, con);
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
                 criteria.add(TPseverityPeer.LISTTYPE, getObjectID());
                 if (!lastTPseveritysCriteria.equals(criteria))
                 {
                     collTPseveritys = TPseverityPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTPseveritysCriteria = criteria;

         return collTPseveritys;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TPseveritys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TPseverity> getTPseveritysJoinTSeverity(Criteria criteria)
        throws TorqueException
    {
        if (collTPseveritys == null)
        {
            if (isNew())
            {
               collTPseveritys = new ArrayList<TPseverity>();
            }
            else
            {
                criteria.add(TPseverityPeer.LISTTYPE, getObjectID());
                collTPseveritys = TPseverityPeer.doSelectJoinTSeverity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPseverityPeer.LISTTYPE, getObjectID());
            if (!lastTPseveritysCriteria.equals(criteria))
            {
                collTPseveritys = TPseverityPeer.doSelectJoinTSeverity(criteria);
            }
        }
        lastTPseveritysCriteria = criteria;

        return collTPseveritys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TPseveritys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TPseverity> getTPseveritysJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTPseveritys == null)
        {
            if (isNew())
            {
               collTPseveritys = new ArrayList<TPseverity>();
            }
            else
            {
                criteria.add(TPseverityPeer.LISTTYPE, getObjectID());
                collTPseveritys = TPseverityPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPseverityPeer.LISTTYPE, getObjectID());
            if (!lastTPseveritysCriteria.equals(criteria))
            {
                collTPseveritys = TPseverityPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTPseveritysCriteria = criteria;

        return collTPseveritys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TPseveritys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TPseverity> getTPseveritysJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTPseveritys == null)
        {
            if (isNew())
            {
               collTPseveritys = new ArrayList<TPseverity>();
            }
            else
            {
                criteria.add(TPseverityPeer.LISTTYPE, getObjectID());
                collTPseveritys = TPseverityPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPseverityPeer.LISTTYPE, getObjectID());
            if (!lastTPseveritysCriteria.equals(criteria))
            {
                collTPseveritys = TPseverityPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTPseveritysCriteria = criteria;

        return collTPseveritys;
    }





    /**
     * Collection to store aggregation of collTWorkItems
     */
    protected List<TWorkItem> collTWorkItems;

    /**
     * Temporary storage of collTWorkItems to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkItems()
    {
        if (collTWorkItems == null)
        {
            collTWorkItems = new ArrayList<TWorkItem>();
        }
    }


    /**
     * Method called to associate a TWorkItem object to this object
     * through the TWorkItem foreign key attribute
     *
     * @param l TWorkItem
     * @throws TorqueException
     */
    public void addTWorkItem(TWorkItem l) throws TorqueException
    {
        getTWorkItems().add(l);
        l.setTListType((TListType) this);
    }

    /**
     * Method called to associate a TWorkItem object to this object
     * through the TWorkItem foreign key attribute using connection.
     *
     * @param l TWorkItem
     * @throws TorqueException
     */
    public void addTWorkItem(TWorkItem l, Connection con) throws TorqueException
    {
        getTWorkItems(con).add(l);
        l.setTListType((TListType) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkItems
     */
    private Criteria lastTWorkItemsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkItems(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkItem> getTWorkItems()
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            collTWorkItems = getTWorkItems(new Criteria(10));
        }
        return collTWorkItems;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TWorkItems from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkItem> getTWorkItems(Criteria criteria) throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID() );
                collTWorkItems = TWorkItemPeer.doSelect(criteria);
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
                criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
                if (!lastTWorkItemsCriteria.equals(criteria))
                {
                    collTWorkItems = TWorkItemPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkItems(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkItem> getTWorkItems(Connection con) throws TorqueException
    {
        if (collTWorkItems == null)
        {
            collTWorkItems = getTWorkItems(new Criteria(10), con);
        }
        return collTWorkItems;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TWorkItems from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkItem> getTWorkItems(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                 criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
                 collTWorkItems = TWorkItemPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
                 if (!lastTWorkItemsCriteria.equals(criteria))
                 {
                     collTWorkItems = TWorkItemPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkItemsCriteria = criteria;

         return collTWorkItems;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWorkItem> getTWorkItemsJoinTPersonRelatedByOwnerID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByOwnerID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByOwnerID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWorkItem> getTWorkItemsJoinTPersonRelatedByChangedByID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByChangedByID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByChangedByID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWorkItem> getTWorkItemsJoinTPersonRelatedByOriginatorID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByOriginatorID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByOriginatorID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWorkItem> getTWorkItemsJoinTPersonRelatedByResponsibleID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByResponsibleID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByResponsibleID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWorkItem> getTWorkItemsJoinTProjectCategory(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTProjectCategory(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTProjectCategory(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWorkItem> getTWorkItemsJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWorkItem> getTWorkItemsJoinTClass(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTClass(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTClass(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWorkItem> getTWorkItemsJoinTPriority(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPriority(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTPriority(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWorkItem> getTWorkItemsJoinTSeverity(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTSeverity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTSeverity(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWorkItem> getTWorkItemsJoinTReleaseRelatedByReleaseNoticedID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTReleaseRelatedByReleaseNoticedID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTReleaseRelatedByReleaseNoticedID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWorkItem> getTWorkItemsJoinTReleaseRelatedByReleaseScheduledID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTReleaseRelatedByReleaseScheduledID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTReleaseRelatedByReleaseScheduledID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWorkItem> getTWorkItemsJoinTState(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTState(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWorkItem> getTWorkItemsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.CATEGORYKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }













    /**
     * Collection to store aggregation of collTDisableFields
     */
    protected List<TDisableField> collTDisableFields;

    /**
     * Temporary storage of collTDisableFields to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTDisableFields()
    {
        if (collTDisableFields == null)
        {
            collTDisableFields = new ArrayList<TDisableField>();
        }
    }


    /**
     * Method called to associate a TDisableField object to this object
     * through the TDisableField foreign key attribute
     *
     * @param l TDisableField
     * @throws TorqueException
     */
    public void addTDisableField(TDisableField l) throws TorqueException
    {
        getTDisableFields().add(l);
        l.setTListType((TListType) this);
    }

    /**
     * Method called to associate a TDisableField object to this object
     * through the TDisableField foreign key attribute using connection.
     *
     * @param l TDisableField
     * @throws TorqueException
     */
    public void addTDisableField(TDisableField l, Connection con) throws TorqueException
    {
        getTDisableFields(con).add(l);
        l.setTListType((TListType) this);
    }

    /**
     * The criteria used to select the current contents of collTDisableFields
     */
    private Criteria lastTDisableFieldsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTDisableFields(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TDisableField> getTDisableFields()
        throws TorqueException
    {
        if (collTDisableFields == null)
        {
            collTDisableFields = getTDisableFields(new Criteria(10));
        }
        return collTDisableFields;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TDisableFields from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TDisableField> getTDisableFields(Criteria criteria) throws TorqueException
    {
        if (collTDisableFields == null)
        {
            if (isNew())
            {
               collTDisableFields = new ArrayList<TDisableField>();
            }
            else
            {
                criteria.add(TDisableFieldPeer.LISTTYPE, getObjectID() );
                collTDisableFields = TDisableFieldPeer.doSelect(criteria);
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
                criteria.add(TDisableFieldPeer.LISTTYPE, getObjectID());
                if (!lastTDisableFieldsCriteria.equals(criteria))
                {
                    collTDisableFields = TDisableFieldPeer.doSelect(criteria);
                }
            }
        }
        lastTDisableFieldsCriteria = criteria;

        return collTDisableFields;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTDisableFields(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TDisableField> getTDisableFields(Connection con) throws TorqueException
    {
        if (collTDisableFields == null)
        {
            collTDisableFields = getTDisableFields(new Criteria(10), con);
        }
        return collTDisableFields;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TDisableFields from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TDisableField> getTDisableFields(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTDisableFields == null)
        {
            if (isNew())
            {
               collTDisableFields = new ArrayList<TDisableField>();
            }
            else
            {
                 criteria.add(TDisableFieldPeer.LISTTYPE, getObjectID());
                 collTDisableFields = TDisableFieldPeer.doSelect(criteria, con);
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
                 criteria.add(TDisableFieldPeer.LISTTYPE, getObjectID());
                 if (!lastTDisableFieldsCriteria.equals(criteria))
                 {
                     collTDisableFields = TDisableFieldPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTDisableFieldsCriteria = criteria;

         return collTDisableFields;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TDisableFields from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TDisableField> getTDisableFieldsJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTDisableFields == null)
        {
            if (isNew())
            {
               collTDisableFields = new ArrayList<TDisableField>();
            }
            else
            {
                criteria.add(TDisableFieldPeer.LISTTYPE, getObjectID());
                collTDisableFields = TDisableFieldPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TDisableFieldPeer.LISTTYPE, getObjectID());
            if (!lastTDisableFieldsCriteria.equals(criteria))
            {
                collTDisableFields = TDisableFieldPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTDisableFieldsCriteria = criteria;

        return collTDisableFields;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TDisableFields from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TDisableField> getTDisableFieldsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTDisableFields == null)
        {
            if (isNew())
            {
               collTDisableFields = new ArrayList<TDisableField>();
            }
            else
            {
                criteria.add(TDisableFieldPeer.LISTTYPE, getObjectID());
                collTDisableFields = TDisableFieldPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TDisableFieldPeer.LISTTYPE, getObjectID());
            if (!lastTDisableFieldsCriteria.equals(criteria))
            {
                collTDisableFields = TDisableFieldPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTDisableFieldsCriteria = criteria;

        return collTDisableFields;
    }





    /**
     * Collection to store aggregation of collTPlistTypes
     */
    protected List<TPlistType> collTPlistTypes;

    /**
     * Temporary storage of collTPlistTypes to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTPlistTypes()
    {
        if (collTPlistTypes == null)
        {
            collTPlistTypes = new ArrayList<TPlistType>();
        }
    }


    /**
     * Method called to associate a TPlistType object to this object
     * through the TPlistType foreign key attribute
     *
     * @param l TPlistType
     * @throws TorqueException
     */
    public void addTPlistType(TPlistType l) throws TorqueException
    {
        getTPlistTypes().add(l);
        l.setTListType((TListType) this);
    }

    /**
     * Method called to associate a TPlistType object to this object
     * through the TPlistType foreign key attribute using connection.
     *
     * @param l TPlistType
     * @throws TorqueException
     */
    public void addTPlistType(TPlistType l, Connection con) throws TorqueException
    {
        getTPlistTypes(con).add(l);
        l.setTListType((TListType) this);
    }

    /**
     * The criteria used to select the current contents of collTPlistTypes
     */
    private Criteria lastTPlistTypesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPlistTypes(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TPlistType> getTPlistTypes()
        throws TorqueException
    {
        if (collTPlistTypes == null)
        {
            collTPlistTypes = getTPlistTypes(new Criteria(10));
        }
        return collTPlistTypes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TPlistTypes from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TPlistType> getTPlistTypes(Criteria criteria) throws TorqueException
    {
        if (collTPlistTypes == null)
        {
            if (isNew())
            {
               collTPlistTypes = new ArrayList<TPlistType>();
            }
            else
            {
                criteria.add(TPlistTypePeer.CATEGORY, getObjectID() );
                collTPlistTypes = TPlistTypePeer.doSelect(criteria);
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
                criteria.add(TPlistTypePeer.CATEGORY, getObjectID());
                if (!lastTPlistTypesCriteria.equals(criteria))
                {
                    collTPlistTypes = TPlistTypePeer.doSelect(criteria);
                }
            }
        }
        lastTPlistTypesCriteria = criteria;

        return collTPlistTypes;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPlistTypes(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPlistType> getTPlistTypes(Connection con) throws TorqueException
    {
        if (collTPlistTypes == null)
        {
            collTPlistTypes = getTPlistTypes(new Criteria(10), con);
        }
        return collTPlistTypes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TPlistTypes from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPlistType> getTPlistTypes(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTPlistTypes == null)
        {
            if (isNew())
            {
               collTPlistTypes = new ArrayList<TPlistType>();
            }
            else
            {
                 criteria.add(TPlistTypePeer.CATEGORY, getObjectID());
                 collTPlistTypes = TPlistTypePeer.doSelect(criteria, con);
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
                 criteria.add(TPlistTypePeer.CATEGORY, getObjectID());
                 if (!lastTPlistTypesCriteria.equals(criteria))
                 {
                     collTPlistTypes = TPlistTypePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTPlistTypesCriteria = criteria;

         return collTPlistTypes;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TPlistTypes from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TPlistType> getTPlistTypesJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTPlistTypes == null)
        {
            if (isNew())
            {
               collTPlistTypes = new ArrayList<TPlistType>();
            }
            else
            {
                criteria.add(TPlistTypePeer.CATEGORY, getObjectID());
                collTPlistTypes = TPlistTypePeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPlistTypePeer.CATEGORY, getObjectID());
            if (!lastTPlistTypesCriteria.equals(criteria))
            {
                collTPlistTypes = TPlistTypePeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTPlistTypesCriteria = criteria;

        return collTPlistTypes;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TPlistTypes from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TPlistType> getTPlistTypesJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTPlistTypes == null)
        {
            if (isNew())
            {
               collTPlistTypes = new ArrayList<TPlistType>();
            }
            else
            {
                criteria.add(TPlistTypePeer.CATEGORY, getObjectID());
                collTPlistTypes = TPlistTypePeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPlistTypePeer.CATEGORY, getObjectID());
            if (!lastTPlistTypesCriteria.equals(criteria))
            {
                collTPlistTypes = TPlistTypePeer.doSelectJoinTListType(criteria);
            }
        }
        lastTPlistTypesCriteria = criteria;

        return collTPlistTypes;
    }





    /**
     * Collection to store aggregation of collTPstates
     */
    protected List<TPstate> collTPstates;

    /**
     * Temporary storage of collTPstates to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTPstates()
    {
        if (collTPstates == null)
        {
            collTPstates = new ArrayList<TPstate>();
        }
    }


    /**
     * Method called to associate a TPstate object to this object
     * through the TPstate foreign key attribute
     *
     * @param l TPstate
     * @throws TorqueException
     */
    public void addTPstate(TPstate l) throws TorqueException
    {
        getTPstates().add(l);
        l.setTListType((TListType) this);
    }

    /**
     * Method called to associate a TPstate object to this object
     * through the TPstate foreign key attribute using connection.
     *
     * @param l TPstate
     * @throws TorqueException
     */
    public void addTPstate(TPstate l, Connection con) throws TorqueException
    {
        getTPstates(con).add(l);
        l.setTListType((TListType) this);
    }

    /**
     * The criteria used to select the current contents of collTPstates
     */
    private Criteria lastTPstatesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPstates(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TPstate> getTPstates()
        throws TorqueException
    {
        if (collTPstates == null)
        {
            collTPstates = getTPstates(new Criteria(10));
        }
        return collTPstates;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TPstates from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TPstate> getTPstates(Criteria criteria) throws TorqueException
    {
        if (collTPstates == null)
        {
            if (isNew())
            {
               collTPstates = new ArrayList<TPstate>();
            }
            else
            {
                criteria.add(TPstatePeer.LISTTYPE, getObjectID() );
                collTPstates = TPstatePeer.doSelect(criteria);
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
                criteria.add(TPstatePeer.LISTTYPE, getObjectID());
                if (!lastTPstatesCriteria.equals(criteria))
                {
                    collTPstates = TPstatePeer.doSelect(criteria);
                }
            }
        }
        lastTPstatesCriteria = criteria;

        return collTPstates;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPstates(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPstate> getTPstates(Connection con) throws TorqueException
    {
        if (collTPstates == null)
        {
            collTPstates = getTPstates(new Criteria(10), con);
        }
        return collTPstates;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TPstates from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPstate> getTPstates(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTPstates == null)
        {
            if (isNew())
            {
               collTPstates = new ArrayList<TPstate>();
            }
            else
            {
                 criteria.add(TPstatePeer.LISTTYPE, getObjectID());
                 collTPstates = TPstatePeer.doSelect(criteria, con);
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
                 criteria.add(TPstatePeer.LISTTYPE, getObjectID());
                 if (!lastTPstatesCriteria.equals(criteria))
                 {
                     collTPstates = TPstatePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTPstatesCriteria = criteria;

         return collTPstates;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TPstates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TPstate> getTPstatesJoinTState(Criteria criteria)
        throws TorqueException
    {
        if (collTPstates == null)
        {
            if (isNew())
            {
               collTPstates = new ArrayList<TPstate>();
            }
            else
            {
                criteria.add(TPstatePeer.LISTTYPE, getObjectID());
                collTPstates = TPstatePeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPstatePeer.LISTTYPE, getObjectID());
            if (!lastTPstatesCriteria.equals(criteria))
            {
                collTPstates = TPstatePeer.doSelectJoinTState(criteria);
            }
        }
        lastTPstatesCriteria = criteria;

        return collTPstates;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TPstates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TPstate> getTPstatesJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTPstates == null)
        {
            if (isNew())
            {
               collTPstates = new ArrayList<TPstate>();
            }
            else
            {
                criteria.add(TPstatePeer.LISTTYPE, getObjectID());
                collTPstates = TPstatePeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPstatePeer.LISTTYPE, getObjectID());
            if (!lastTPstatesCriteria.equals(criteria))
            {
                collTPstates = TPstatePeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTPstatesCriteria = criteria;

        return collTPstates;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TPstates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TPstate> getTPstatesJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTPstates == null)
        {
            if (isNew())
            {
               collTPstates = new ArrayList<TPstate>();
            }
            else
            {
                criteria.add(TPstatePeer.LISTTYPE, getObjectID());
                collTPstates = TPstatePeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPstatePeer.LISTTYPE, getObjectID());
            if (!lastTPstatesCriteria.equals(criteria))
            {
                collTPstates = TPstatePeer.doSelectJoinTListType(criteria);
            }
        }
        lastTPstatesCriteria = criteria;

        return collTPstates;
    }





    /**
     * Collection to store aggregation of collTWorkFlowCategorys
     */
    protected List<TWorkFlowCategory> collTWorkFlowCategorys;

    /**
     * Temporary storage of collTWorkFlowCategorys to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkFlowCategorys()
    {
        if (collTWorkFlowCategorys == null)
        {
            collTWorkFlowCategorys = new ArrayList<TWorkFlowCategory>();
        }
    }


    /**
     * Method called to associate a TWorkFlowCategory object to this object
     * through the TWorkFlowCategory foreign key attribute
     *
     * @param l TWorkFlowCategory
     * @throws TorqueException
     */
    public void addTWorkFlowCategory(TWorkFlowCategory l) throws TorqueException
    {
        getTWorkFlowCategorys().add(l);
        l.setTListType((TListType) this);
    }

    /**
     * Method called to associate a TWorkFlowCategory object to this object
     * through the TWorkFlowCategory foreign key attribute using connection.
     *
     * @param l TWorkFlowCategory
     * @throws TorqueException
     */
    public void addTWorkFlowCategory(TWorkFlowCategory l, Connection con) throws TorqueException
    {
        getTWorkFlowCategorys(con).add(l);
        l.setTListType((TListType) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkFlowCategorys
     */
    private Criteria lastTWorkFlowCategorysCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkFlowCategorys(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkFlowCategory> getTWorkFlowCategorys()
        throws TorqueException
    {
        if (collTWorkFlowCategorys == null)
        {
            collTWorkFlowCategorys = getTWorkFlowCategorys(new Criteria(10));
        }
        return collTWorkFlowCategorys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TWorkFlowCategorys from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkFlowCategory> getTWorkFlowCategorys(Criteria criteria) throws TorqueException
    {
        if (collTWorkFlowCategorys == null)
        {
            if (isNew())
            {
               collTWorkFlowCategorys = new ArrayList<TWorkFlowCategory>();
            }
            else
            {
                criteria.add(TWorkFlowCategoryPeer.CATEGORY, getObjectID() );
                collTWorkFlowCategorys = TWorkFlowCategoryPeer.doSelect(criteria);
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
                criteria.add(TWorkFlowCategoryPeer.CATEGORY, getObjectID());
                if (!lastTWorkFlowCategorysCriteria.equals(criteria))
                {
                    collTWorkFlowCategorys = TWorkFlowCategoryPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkFlowCategorysCriteria = criteria;

        return collTWorkFlowCategorys;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkFlowCategorys(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkFlowCategory> getTWorkFlowCategorys(Connection con) throws TorqueException
    {
        if (collTWorkFlowCategorys == null)
        {
            collTWorkFlowCategorys = getTWorkFlowCategorys(new Criteria(10), con);
        }
        return collTWorkFlowCategorys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TWorkFlowCategorys from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkFlowCategory> getTWorkFlowCategorys(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkFlowCategorys == null)
        {
            if (isNew())
            {
               collTWorkFlowCategorys = new ArrayList<TWorkFlowCategory>();
            }
            else
            {
                 criteria.add(TWorkFlowCategoryPeer.CATEGORY, getObjectID());
                 collTWorkFlowCategorys = TWorkFlowCategoryPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkFlowCategoryPeer.CATEGORY, getObjectID());
                 if (!lastTWorkFlowCategorysCriteria.equals(criteria))
                 {
                     collTWorkFlowCategorys = TWorkFlowCategoryPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkFlowCategorysCriteria = criteria;

         return collTWorkFlowCategorys;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWorkFlowCategorys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWorkFlowCategory> getTWorkFlowCategorysJoinTWorkFlow(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkFlowCategorys == null)
        {
            if (isNew())
            {
               collTWorkFlowCategorys = new ArrayList<TWorkFlowCategory>();
            }
            else
            {
                criteria.add(TWorkFlowCategoryPeer.CATEGORY, getObjectID());
                collTWorkFlowCategorys = TWorkFlowCategoryPeer.doSelectJoinTWorkFlow(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkFlowCategoryPeer.CATEGORY, getObjectID());
            if (!lastTWorkFlowCategorysCriteria.equals(criteria))
            {
                collTWorkFlowCategorys = TWorkFlowCategoryPeer.doSelectJoinTWorkFlow(criteria);
            }
        }
        lastTWorkFlowCategorysCriteria = criteria;

        return collTWorkFlowCategorys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWorkFlowCategorys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWorkFlowCategory> getTWorkFlowCategorysJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkFlowCategorys == null)
        {
            if (isNew())
            {
               collTWorkFlowCategorys = new ArrayList<TWorkFlowCategory>();
            }
            else
            {
                criteria.add(TWorkFlowCategoryPeer.CATEGORY, getObjectID());
                collTWorkFlowCategorys = TWorkFlowCategoryPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkFlowCategoryPeer.CATEGORY, getObjectID());
            if (!lastTWorkFlowCategorysCriteria.equals(criteria))
            {
                collTWorkFlowCategorys = TWorkFlowCategoryPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTWorkFlowCategorysCriteria = criteria;

        return collTWorkFlowCategorys;
    }





    /**
     * Collection to store aggregation of collTRoleListTypes
     */
    protected List<TRoleListType> collTRoleListTypes;

    /**
     * Temporary storage of collTRoleListTypes to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTRoleListTypes()
    {
        if (collTRoleListTypes == null)
        {
            collTRoleListTypes = new ArrayList<TRoleListType>();
        }
    }


    /**
     * Method called to associate a TRoleListType object to this object
     * through the TRoleListType foreign key attribute
     *
     * @param l TRoleListType
     * @throws TorqueException
     */
    public void addTRoleListType(TRoleListType l) throws TorqueException
    {
        getTRoleListTypes().add(l);
        l.setTListType((TListType) this);
    }

    /**
     * Method called to associate a TRoleListType object to this object
     * through the TRoleListType foreign key attribute using connection.
     *
     * @param l TRoleListType
     * @throws TorqueException
     */
    public void addTRoleListType(TRoleListType l, Connection con) throws TorqueException
    {
        getTRoleListTypes(con).add(l);
        l.setTListType((TListType) this);
    }

    /**
     * The criteria used to select the current contents of collTRoleListTypes
     */
    private Criteria lastTRoleListTypesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTRoleListTypes(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TRoleListType> getTRoleListTypes()
        throws TorqueException
    {
        if (collTRoleListTypes == null)
        {
            collTRoleListTypes = getTRoleListTypes(new Criteria(10));
        }
        return collTRoleListTypes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TRoleListTypes from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TRoleListType> getTRoleListTypes(Criteria criteria) throws TorqueException
    {
        if (collTRoleListTypes == null)
        {
            if (isNew())
            {
               collTRoleListTypes = new ArrayList<TRoleListType>();
            }
            else
            {
                criteria.add(TRoleListTypePeer.LISTTYPE, getObjectID() );
                collTRoleListTypes = TRoleListTypePeer.doSelect(criteria);
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
                criteria.add(TRoleListTypePeer.LISTTYPE, getObjectID());
                if (!lastTRoleListTypesCriteria.equals(criteria))
                {
                    collTRoleListTypes = TRoleListTypePeer.doSelect(criteria);
                }
            }
        }
        lastTRoleListTypesCriteria = criteria;

        return collTRoleListTypes;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTRoleListTypes(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TRoleListType> getTRoleListTypes(Connection con) throws TorqueException
    {
        if (collTRoleListTypes == null)
        {
            collTRoleListTypes = getTRoleListTypes(new Criteria(10), con);
        }
        return collTRoleListTypes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TRoleListTypes from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TRoleListType> getTRoleListTypes(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTRoleListTypes == null)
        {
            if (isNew())
            {
               collTRoleListTypes = new ArrayList<TRoleListType>();
            }
            else
            {
                 criteria.add(TRoleListTypePeer.LISTTYPE, getObjectID());
                 collTRoleListTypes = TRoleListTypePeer.doSelect(criteria, con);
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
                 criteria.add(TRoleListTypePeer.LISTTYPE, getObjectID());
                 if (!lastTRoleListTypesCriteria.equals(criteria))
                 {
                     collTRoleListTypes = TRoleListTypePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTRoleListTypesCriteria = criteria;

         return collTRoleListTypes;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TRoleListTypes from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TRoleListType> getTRoleListTypesJoinTRole(Criteria criteria)
        throws TorqueException
    {
        if (collTRoleListTypes == null)
        {
            if (isNew())
            {
               collTRoleListTypes = new ArrayList<TRoleListType>();
            }
            else
            {
                criteria.add(TRoleListTypePeer.LISTTYPE, getObjectID());
                collTRoleListTypes = TRoleListTypePeer.doSelectJoinTRole(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TRoleListTypePeer.LISTTYPE, getObjectID());
            if (!lastTRoleListTypesCriteria.equals(criteria))
            {
                collTRoleListTypes = TRoleListTypePeer.doSelectJoinTRole(criteria);
            }
        }
        lastTRoleListTypesCriteria = criteria;

        return collTRoleListTypes;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TRoleListTypes from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TRoleListType> getTRoleListTypesJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTRoleListTypes == null)
        {
            if (isNew())
            {
               collTRoleListTypes = new ArrayList<TRoleListType>();
            }
            else
            {
                criteria.add(TRoleListTypePeer.LISTTYPE, getObjectID());
                collTRoleListTypes = TRoleListTypePeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TRoleListTypePeer.LISTTYPE, getObjectID());
            if (!lastTRoleListTypesCriteria.equals(criteria))
            {
                collTRoleListTypes = TRoleListTypePeer.doSelectJoinTListType(criteria);
            }
        }
        lastTRoleListTypesCriteria = criteria;

        return collTRoleListTypes;
    }





    /**
     * Collection to store aggregation of collTFieldConfigs
     */
    protected List<TFieldConfig> collTFieldConfigs;

    /**
     * Temporary storage of collTFieldConfigs to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTFieldConfigs()
    {
        if (collTFieldConfigs == null)
        {
            collTFieldConfigs = new ArrayList<TFieldConfig>();
        }
    }


    /**
     * Method called to associate a TFieldConfig object to this object
     * through the TFieldConfig foreign key attribute
     *
     * @param l TFieldConfig
     * @throws TorqueException
     */
    public void addTFieldConfig(TFieldConfig l) throws TorqueException
    {
        getTFieldConfigs().add(l);
        l.setTListType((TListType) this);
    }

    /**
     * Method called to associate a TFieldConfig object to this object
     * through the TFieldConfig foreign key attribute using connection.
     *
     * @param l TFieldConfig
     * @throws TorqueException
     */
    public void addTFieldConfig(TFieldConfig l, Connection con) throws TorqueException
    {
        getTFieldConfigs(con).add(l);
        l.setTListType((TListType) this);
    }

    /**
     * The criteria used to select the current contents of collTFieldConfigs
     */
    private Criteria lastTFieldConfigsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTFieldConfigs(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TFieldConfig> getTFieldConfigs()
        throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            collTFieldConfigs = getTFieldConfigs(new Criteria(10));
        }
        return collTFieldConfigs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TFieldConfig> getTFieldConfigs(Criteria criteria) throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            if (isNew())
            {
               collTFieldConfigs = new ArrayList<TFieldConfig>();
            }
            else
            {
                criteria.add(TFieldConfigPeer.ISSUETYPE, getObjectID() );
                collTFieldConfigs = TFieldConfigPeer.doSelect(criteria);
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
                criteria.add(TFieldConfigPeer.ISSUETYPE, getObjectID());
                if (!lastTFieldConfigsCriteria.equals(criteria))
                {
                    collTFieldConfigs = TFieldConfigPeer.doSelect(criteria);
                }
            }
        }
        lastTFieldConfigsCriteria = criteria;

        return collTFieldConfigs;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTFieldConfigs(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TFieldConfig> getTFieldConfigs(Connection con) throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            collTFieldConfigs = getTFieldConfigs(new Criteria(10), con);
        }
        return collTFieldConfigs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TFieldConfig> getTFieldConfigs(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            if (isNew())
            {
               collTFieldConfigs = new ArrayList<TFieldConfig>();
            }
            else
            {
                 criteria.add(TFieldConfigPeer.ISSUETYPE, getObjectID());
                 collTFieldConfigs = TFieldConfigPeer.doSelect(criteria, con);
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
                 criteria.add(TFieldConfigPeer.ISSUETYPE, getObjectID());
                 if (!lastTFieldConfigsCriteria.equals(criteria))
                 {
                     collTFieldConfigs = TFieldConfigPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTFieldConfigsCriteria = criteria;

         return collTFieldConfigs;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TFieldConfig> getTFieldConfigsJoinTField(Criteria criteria)
        throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            if (isNew())
            {
               collTFieldConfigs = new ArrayList<TFieldConfig>();
            }
            else
            {
                criteria.add(TFieldConfigPeer.ISSUETYPE, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTField(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.ISSUETYPE, getObjectID());
            if (!lastTFieldConfigsCriteria.equals(criteria))
            {
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTField(criteria);
            }
        }
        lastTFieldConfigsCriteria = criteria;

        return collTFieldConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TFieldConfig> getTFieldConfigsJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            if (isNew())
            {
               collTFieldConfigs = new ArrayList<TFieldConfig>();
            }
            else
            {
                criteria.add(TFieldConfigPeer.ISSUETYPE, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.ISSUETYPE, getObjectID());
            if (!lastTFieldConfigsCriteria.equals(criteria))
            {
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTFieldConfigsCriteria = criteria;

        return collTFieldConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TFieldConfig> getTFieldConfigsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            if (isNew())
            {
               collTFieldConfigs = new ArrayList<TFieldConfig>();
            }
            else
            {
                criteria.add(TFieldConfigPeer.ISSUETYPE, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.ISSUETYPE, getObjectID());
            if (!lastTFieldConfigsCriteria.equals(criteria))
            {
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTFieldConfigsCriteria = criteria;

        return collTFieldConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TFieldConfig> getTFieldConfigsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            if (isNew())
            {
               collTFieldConfigs = new ArrayList<TFieldConfig>();
            }
            else
            {
                criteria.add(TFieldConfigPeer.ISSUETYPE, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.ISSUETYPE, getObjectID());
            if (!lastTFieldConfigsCriteria.equals(criteria))
            {
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTFieldConfigsCriteria = criteria;

        return collTFieldConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TFieldConfig> getTFieldConfigsJoinTScripts(Criteria criteria)
        throws TorqueException
    {
        if (collTFieldConfigs == null)
        {
            if (isNew())
            {
               collTFieldConfigs = new ArrayList<TFieldConfig>();
            }
            else
            {
                criteria.add(TFieldConfigPeer.ISSUETYPE, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTScripts(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.ISSUETYPE, getObjectID());
            if (!lastTFieldConfigsCriteria.equals(criteria))
            {
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTScripts(criteria);
            }
        }
        lastTFieldConfigsCriteria = criteria;

        return collTFieldConfigs;
    }





    /**
     * Collection to store aggregation of collTScreenConfigs
     */
    protected List<TScreenConfig> collTScreenConfigs;

    /**
     * Temporary storage of collTScreenConfigs to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTScreenConfigs()
    {
        if (collTScreenConfigs == null)
        {
            collTScreenConfigs = new ArrayList<TScreenConfig>();
        }
    }


    /**
     * Method called to associate a TScreenConfig object to this object
     * through the TScreenConfig foreign key attribute
     *
     * @param l TScreenConfig
     * @throws TorqueException
     */
    public void addTScreenConfig(TScreenConfig l) throws TorqueException
    {
        getTScreenConfigs().add(l);
        l.setTListType((TListType) this);
    }

    /**
     * Method called to associate a TScreenConfig object to this object
     * through the TScreenConfig foreign key attribute using connection.
     *
     * @param l TScreenConfig
     * @throws TorqueException
     */
    public void addTScreenConfig(TScreenConfig l, Connection con) throws TorqueException
    {
        getTScreenConfigs(con).add(l);
        l.setTListType((TListType) this);
    }

    /**
     * The criteria used to select the current contents of collTScreenConfigs
     */
    private Criteria lastTScreenConfigsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScreenConfigs(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TScreenConfig> getTScreenConfigs()
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            collTScreenConfigs = getTScreenConfigs(new Criteria(10));
        }
        return collTScreenConfigs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TScreenConfig> getTScreenConfigs(Criteria criteria) throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.ISSUETYPE, getObjectID() );
                collTScreenConfigs = TScreenConfigPeer.doSelect(criteria);
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
                criteria.add(TScreenConfigPeer.ISSUETYPE, getObjectID());
                if (!lastTScreenConfigsCriteria.equals(criteria))
                {
                    collTScreenConfigs = TScreenConfigPeer.doSelect(criteria);
                }
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScreenConfigs(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScreenConfig> getTScreenConfigs(Connection con) throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            collTScreenConfigs = getTScreenConfigs(new Criteria(10), con);
        }
        return collTScreenConfigs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScreenConfig> getTScreenConfigs(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                 criteria.add(TScreenConfigPeer.ISSUETYPE, getObjectID());
                 collTScreenConfigs = TScreenConfigPeer.doSelect(criteria, con);
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
                 criteria.add(TScreenConfigPeer.ISSUETYPE, getObjectID());
                 if (!lastTScreenConfigsCriteria.equals(criteria))
                 {
                     collTScreenConfigs = TScreenConfigPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTScreenConfigsCriteria = criteria;

         return collTScreenConfigs;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TScreenConfig> getTScreenConfigsJoinTScreen(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.ISSUETYPE, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTScreen(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.ISSUETYPE, getObjectID());
            if (!lastTScreenConfigsCriteria.equals(criteria))
            {
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTScreen(criteria);
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TScreenConfig> getTScreenConfigsJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.ISSUETYPE, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.ISSUETYPE, getObjectID());
            if (!lastTScreenConfigsCriteria.equals(criteria))
            {
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TScreenConfig> getTScreenConfigsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.ISSUETYPE, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.ISSUETYPE, getObjectID());
            if (!lastTScreenConfigsCriteria.equals(criteria))
            {
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TScreenConfig> getTScreenConfigsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.ISSUETYPE, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.ISSUETYPE, getObjectID());
            if (!lastTScreenConfigsCriteria.equals(criteria))
            {
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TScreenConfig> getTScreenConfigsJoinTAction(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.ISSUETYPE, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTAction(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.ISSUETYPE, getObjectID());
            if (!lastTScreenConfigsCriteria.equals(criteria))
            {
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTAction(criteria);
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }





    /**
     * Collection to store aggregation of collTInitStates
     */
    protected List<TInitState> collTInitStates;

    /**
     * Temporary storage of collTInitStates to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTInitStates()
    {
        if (collTInitStates == null)
        {
            collTInitStates = new ArrayList<TInitState>();
        }
    }


    /**
     * Method called to associate a TInitState object to this object
     * through the TInitState foreign key attribute
     *
     * @param l TInitState
     * @throws TorqueException
     */
    public void addTInitState(TInitState l) throws TorqueException
    {
        getTInitStates().add(l);
        l.setTListType((TListType) this);
    }

    /**
     * Method called to associate a TInitState object to this object
     * through the TInitState foreign key attribute using connection.
     *
     * @param l TInitState
     * @throws TorqueException
     */
    public void addTInitState(TInitState l, Connection con) throws TorqueException
    {
        getTInitStates(con).add(l);
        l.setTListType((TListType) this);
    }

    /**
     * The criteria used to select the current contents of collTInitStates
     */
    private Criteria lastTInitStatesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTInitStates(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TInitState> getTInitStates()
        throws TorqueException
    {
        if (collTInitStates == null)
        {
            collTInitStates = getTInitStates(new Criteria(10));
        }
        return collTInitStates;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TInitStates from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TInitState> getTInitStates(Criteria criteria) throws TorqueException
    {
        if (collTInitStates == null)
        {
            if (isNew())
            {
               collTInitStates = new ArrayList<TInitState>();
            }
            else
            {
                criteria.add(TInitStatePeer.LISTTYPE, getObjectID() );
                collTInitStates = TInitStatePeer.doSelect(criteria);
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
                criteria.add(TInitStatePeer.LISTTYPE, getObjectID());
                if (!lastTInitStatesCriteria.equals(criteria))
                {
                    collTInitStates = TInitStatePeer.doSelect(criteria);
                }
            }
        }
        lastTInitStatesCriteria = criteria;

        return collTInitStates;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTInitStates(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TInitState> getTInitStates(Connection con) throws TorqueException
    {
        if (collTInitStates == null)
        {
            collTInitStates = getTInitStates(new Criteria(10), con);
        }
        return collTInitStates;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TInitStates from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TInitState> getTInitStates(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTInitStates == null)
        {
            if (isNew())
            {
               collTInitStates = new ArrayList<TInitState>();
            }
            else
            {
                 criteria.add(TInitStatePeer.LISTTYPE, getObjectID());
                 collTInitStates = TInitStatePeer.doSelect(criteria, con);
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
                 criteria.add(TInitStatePeer.LISTTYPE, getObjectID());
                 if (!lastTInitStatesCriteria.equals(criteria))
                 {
                     collTInitStates = TInitStatePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTInitStatesCriteria = criteria;

         return collTInitStates;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TInitStates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TInitState> getTInitStatesJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTInitStates == null)
        {
            if (isNew())
            {
               collTInitStates = new ArrayList<TInitState>();
            }
            else
            {
                criteria.add(TInitStatePeer.LISTTYPE, getObjectID());
                collTInitStates = TInitStatePeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TInitStatePeer.LISTTYPE, getObjectID());
            if (!lastTInitStatesCriteria.equals(criteria))
            {
                collTInitStates = TInitStatePeer.doSelectJoinTProject(criteria);
            }
        }
        lastTInitStatesCriteria = criteria;

        return collTInitStates;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TInitStates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TInitState> getTInitStatesJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTInitStates == null)
        {
            if (isNew())
            {
               collTInitStates = new ArrayList<TInitState>();
            }
            else
            {
                criteria.add(TInitStatePeer.LISTTYPE, getObjectID());
                collTInitStates = TInitStatePeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TInitStatePeer.LISTTYPE, getObjectID());
            if (!lastTInitStatesCriteria.equals(criteria))
            {
                collTInitStates = TInitStatePeer.doSelectJoinTListType(criteria);
            }
        }
        lastTInitStatesCriteria = criteria;

        return collTInitStates;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TInitStates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TInitState> getTInitStatesJoinTState(Criteria criteria)
        throws TorqueException
    {
        if (collTInitStates == null)
        {
            if (isNew())
            {
               collTInitStates = new ArrayList<TInitState>();
            }
            else
            {
                criteria.add(TInitStatePeer.LISTTYPE, getObjectID());
                collTInitStates = TInitStatePeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TInitStatePeer.LISTTYPE, getObjectID());
            if (!lastTInitStatesCriteria.equals(criteria))
            {
                collTInitStates = TInitStatePeer.doSelectJoinTState(criteria);
            }
        }
        lastTInitStatesCriteria = criteria;

        return collTInitStates;
    }





    /**
     * Collection to store aggregation of collTChildIssueTypesRelatedByIssueTypeParent
     */
    protected List<TChildIssueType> collTChildIssueTypesRelatedByIssueTypeParent;

    /**
     * Temporary storage of collTChildIssueTypesRelatedByIssueTypeParent to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTChildIssueTypesRelatedByIssueTypeParent()
    {
        if (collTChildIssueTypesRelatedByIssueTypeParent == null)
        {
            collTChildIssueTypesRelatedByIssueTypeParent = new ArrayList<TChildIssueType>();
        }
    }


    /**
     * Method called to associate a TChildIssueType object to this object
     * through the TChildIssueType foreign key attribute
     *
     * @param l TChildIssueType
     * @throws TorqueException
     */
    public void addTChildIssueTypeRelatedByIssueTypeParent(TChildIssueType l) throws TorqueException
    {
        getTChildIssueTypesRelatedByIssueTypeParent().add(l);
        l.setTListTypeRelatedByIssueTypeParent((TListType) this);
    }

    /**
     * Method called to associate a TChildIssueType object to this object
     * through the TChildIssueType foreign key attribute using connection.
     *
     * @param l TChildIssueType
     * @throws TorqueException
     */
    public void addTChildIssueTypeRelatedByIssueTypeParent(TChildIssueType l, Connection con) throws TorqueException
    {
        getTChildIssueTypesRelatedByIssueTypeParent(con).add(l);
        l.setTListTypeRelatedByIssueTypeParent((TListType) this);
    }

    /**
     * The criteria used to select the current contents of collTChildIssueTypesRelatedByIssueTypeParent
     */
    private Criteria lastTChildIssueTypesRelatedByIssueTypeParentCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTChildIssueTypesRelatedByIssueTypeParent(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TChildIssueType> getTChildIssueTypesRelatedByIssueTypeParent()
        throws TorqueException
    {
        if (collTChildIssueTypesRelatedByIssueTypeParent == null)
        {
            collTChildIssueTypesRelatedByIssueTypeParent = getTChildIssueTypesRelatedByIssueTypeParent(new Criteria(10));
        }
        return collTChildIssueTypesRelatedByIssueTypeParent;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TChildIssueTypesRelatedByIssueTypeParent from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TChildIssueType> getTChildIssueTypesRelatedByIssueTypeParent(Criteria criteria) throws TorqueException
    {
        if (collTChildIssueTypesRelatedByIssueTypeParent == null)
        {
            if (isNew())
            {
               collTChildIssueTypesRelatedByIssueTypeParent = new ArrayList<TChildIssueType>();
            }
            else
            {
                criteria.add(TChildIssueTypePeer.ISSUETYPEPARENT, getObjectID() );
                collTChildIssueTypesRelatedByIssueTypeParent = TChildIssueTypePeer.doSelect(criteria);
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
                criteria.add(TChildIssueTypePeer.ISSUETYPEPARENT, getObjectID());
                if (!lastTChildIssueTypesRelatedByIssueTypeParentCriteria.equals(criteria))
                {
                    collTChildIssueTypesRelatedByIssueTypeParent = TChildIssueTypePeer.doSelect(criteria);
                }
            }
        }
        lastTChildIssueTypesRelatedByIssueTypeParentCriteria = criteria;

        return collTChildIssueTypesRelatedByIssueTypeParent;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTChildIssueTypesRelatedByIssueTypeParent(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TChildIssueType> getTChildIssueTypesRelatedByIssueTypeParent(Connection con) throws TorqueException
    {
        if (collTChildIssueTypesRelatedByIssueTypeParent == null)
        {
            collTChildIssueTypesRelatedByIssueTypeParent = getTChildIssueTypesRelatedByIssueTypeParent(new Criteria(10), con);
        }
        return collTChildIssueTypesRelatedByIssueTypeParent;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TChildIssueTypesRelatedByIssueTypeParent from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TChildIssueType> getTChildIssueTypesRelatedByIssueTypeParent(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTChildIssueTypesRelatedByIssueTypeParent == null)
        {
            if (isNew())
            {
               collTChildIssueTypesRelatedByIssueTypeParent = new ArrayList<TChildIssueType>();
            }
            else
            {
                 criteria.add(TChildIssueTypePeer.ISSUETYPEPARENT, getObjectID());
                 collTChildIssueTypesRelatedByIssueTypeParent = TChildIssueTypePeer.doSelect(criteria, con);
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
                 criteria.add(TChildIssueTypePeer.ISSUETYPEPARENT, getObjectID());
                 if (!lastTChildIssueTypesRelatedByIssueTypeParentCriteria.equals(criteria))
                 {
                     collTChildIssueTypesRelatedByIssueTypeParent = TChildIssueTypePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTChildIssueTypesRelatedByIssueTypeParentCriteria = criteria;

         return collTChildIssueTypesRelatedByIssueTypeParent;
     }



















    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TChildIssueTypesRelatedByIssueTypeParent from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TChildIssueType> getTChildIssueTypesRelatedByIssueTypeParentJoinTListTypeRelatedByIssueTypeChild(Criteria criteria)
        throws TorqueException
    {
        if (collTChildIssueTypesRelatedByIssueTypeParent == null)
        {
            if (isNew())
            {
               collTChildIssueTypesRelatedByIssueTypeParent = new ArrayList<TChildIssueType>();
            }
            else
            {
                criteria.add(TChildIssueTypePeer.ISSUETYPEPARENT, getObjectID());
                collTChildIssueTypesRelatedByIssueTypeParent = TChildIssueTypePeer.doSelectJoinTListTypeRelatedByIssueTypeChild(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TChildIssueTypePeer.ISSUETYPEPARENT, getObjectID());
            if (!lastTChildIssueTypesRelatedByIssueTypeParentCriteria.equals(criteria))
            {
                collTChildIssueTypesRelatedByIssueTypeParent = TChildIssueTypePeer.doSelectJoinTListTypeRelatedByIssueTypeChild(criteria);
            }
        }
        lastTChildIssueTypesRelatedByIssueTypeParentCriteria = criteria;

        return collTChildIssueTypesRelatedByIssueTypeParent;
    }





    /**
     * Collection to store aggregation of collTChildIssueTypesRelatedByIssueTypeChild
     */
    protected List<TChildIssueType> collTChildIssueTypesRelatedByIssueTypeChild;

    /**
     * Temporary storage of collTChildIssueTypesRelatedByIssueTypeChild to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTChildIssueTypesRelatedByIssueTypeChild()
    {
        if (collTChildIssueTypesRelatedByIssueTypeChild == null)
        {
            collTChildIssueTypesRelatedByIssueTypeChild = new ArrayList<TChildIssueType>();
        }
    }


    /**
     * Method called to associate a TChildIssueType object to this object
     * through the TChildIssueType foreign key attribute
     *
     * @param l TChildIssueType
     * @throws TorqueException
     */
    public void addTChildIssueTypeRelatedByIssueTypeChild(TChildIssueType l) throws TorqueException
    {
        getTChildIssueTypesRelatedByIssueTypeChild().add(l);
        l.setTListTypeRelatedByIssueTypeChild((TListType) this);
    }

    /**
     * Method called to associate a TChildIssueType object to this object
     * through the TChildIssueType foreign key attribute using connection.
     *
     * @param l TChildIssueType
     * @throws TorqueException
     */
    public void addTChildIssueTypeRelatedByIssueTypeChild(TChildIssueType l, Connection con) throws TorqueException
    {
        getTChildIssueTypesRelatedByIssueTypeChild(con).add(l);
        l.setTListTypeRelatedByIssueTypeChild((TListType) this);
    }

    /**
     * The criteria used to select the current contents of collTChildIssueTypesRelatedByIssueTypeChild
     */
    private Criteria lastTChildIssueTypesRelatedByIssueTypeChildCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTChildIssueTypesRelatedByIssueTypeChild(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TChildIssueType> getTChildIssueTypesRelatedByIssueTypeChild()
        throws TorqueException
    {
        if (collTChildIssueTypesRelatedByIssueTypeChild == null)
        {
            collTChildIssueTypesRelatedByIssueTypeChild = getTChildIssueTypesRelatedByIssueTypeChild(new Criteria(10));
        }
        return collTChildIssueTypesRelatedByIssueTypeChild;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TChildIssueTypesRelatedByIssueTypeChild from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TChildIssueType> getTChildIssueTypesRelatedByIssueTypeChild(Criteria criteria) throws TorqueException
    {
        if (collTChildIssueTypesRelatedByIssueTypeChild == null)
        {
            if (isNew())
            {
               collTChildIssueTypesRelatedByIssueTypeChild = new ArrayList<TChildIssueType>();
            }
            else
            {
                criteria.add(TChildIssueTypePeer.ISSUETYPECHILD, getObjectID() );
                collTChildIssueTypesRelatedByIssueTypeChild = TChildIssueTypePeer.doSelect(criteria);
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
                criteria.add(TChildIssueTypePeer.ISSUETYPECHILD, getObjectID());
                if (!lastTChildIssueTypesRelatedByIssueTypeChildCriteria.equals(criteria))
                {
                    collTChildIssueTypesRelatedByIssueTypeChild = TChildIssueTypePeer.doSelect(criteria);
                }
            }
        }
        lastTChildIssueTypesRelatedByIssueTypeChildCriteria = criteria;

        return collTChildIssueTypesRelatedByIssueTypeChild;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTChildIssueTypesRelatedByIssueTypeChild(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TChildIssueType> getTChildIssueTypesRelatedByIssueTypeChild(Connection con) throws TorqueException
    {
        if (collTChildIssueTypesRelatedByIssueTypeChild == null)
        {
            collTChildIssueTypesRelatedByIssueTypeChild = getTChildIssueTypesRelatedByIssueTypeChild(new Criteria(10), con);
        }
        return collTChildIssueTypesRelatedByIssueTypeChild;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TChildIssueTypesRelatedByIssueTypeChild from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TChildIssueType> getTChildIssueTypesRelatedByIssueTypeChild(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTChildIssueTypesRelatedByIssueTypeChild == null)
        {
            if (isNew())
            {
               collTChildIssueTypesRelatedByIssueTypeChild = new ArrayList<TChildIssueType>();
            }
            else
            {
                 criteria.add(TChildIssueTypePeer.ISSUETYPECHILD, getObjectID());
                 collTChildIssueTypesRelatedByIssueTypeChild = TChildIssueTypePeer.doSelect(criteria, con);
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
                 criteria.add(TChildIssueTypePeer.ISSUETYPECHILD, getObjectID());
                 if (!lastTChildIssueTypesRelatedByIssueTypeChildCriteria.equals(criteria))
                 {
                     collTChildIssueTypesRelatedByIssueTypeChild = TChildIssueTypePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTChildIssueTypesRelatedByIssueTypeChildCriteria = criteria;

         return collTChildIssueTypesRelatedByIssueTypeChild;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TChildIssueTypesRelatedByIssueTypeChild from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TChildIssueType> getTChildIssueTypesRelatedByIssueTypeChildJoinTListTypeRelatedByIssueTypeParent(Criteria criteria)
        throws TorqueException
    {
        if (collTChildIssueTypesRelatedByIssueTypeChild == null)
        {
            if (isNew())
            {
               collTChildIssueTypesRelatedByIssueTypeChild = new ArrayList<TChildIssueType>();
            }
            else
            {
                criteria.add(TChildIssueTypePeer.ISSUETYPECHILD, getObjectID());
                collTChildIssueTypesRelatedByIssueTypeChild = TChildIssueTypePeer.doSelectJoinTListTypeRelatedByIssueTypeParent(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TChildIssueTypePeer.ISSUETYPECHILD, getObjectID());
            if (!lastTChildIssueTypesRelatedByIssueTypeChildCriteria.equals(criteria))
            {
                collTChildIssueTypesRelatedByIssueTypeChild = TChildIssueTypePeer.doSelectJoinTListTypeRelatedByIssueTypeParent(criteria);
            }
        }
        lastTChildIssueTypesRelatedByIssueTypeChildCriteria = criteria;

        return collTChildIssueTypesRelatedByIssueTypeChild;
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
        l.setTListType((TListType) this);
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
        l.setTListType((TListType) this);
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
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     * If this TListType is new, it will return
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
                criteria.add(TMailTemplateConfigPeer.ISSUETYPE, getObjectID() );
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
                criteria.add(TMailTemplateConfigPeer.ISSUETYPE, getObjectID());
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
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     * If this TListType is new, it will return
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
                 criteria.add(TMailTemplateConfigPeer.ISSUETYPE, getObjectID());
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
                 criteria.add(TMailTemplateConfigPeer.ISSUETYPE, getObjectID());
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
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
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
                criteria.add(TMailTemplateConfigPeer.ISSUETYPE, getObjectID());
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTMailTemplate(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TMailTemplateConfigPeer.ISSUETYPE, getObjectID());
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
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
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
                criteria.add(TMailTemplateConfigPeer.ISSUETYPE, getObjectID());
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TMailTemplateConfigPeer.ISSUETYPE, getObjectID());
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
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
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
                criteria.add(TMailTemplateConfigPeer.ISSUETYPE, getObjectID());
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TMailTemplateConfigPeer.ISSUETYPE, getObjectID());
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
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
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
                criteria.add(TMailTemplateConfigPeer.ISSUETYPE, getObjectID());
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TMailTemplateConfigPeer.ISSUETYPE, getObjectID());
            if (!lastTMailTemplateConfigsCriteria.equals(criteria))
            {
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTMailTemplateConfigsCriteria = criteria;

        return collTMailTemplateConfigs;
    }





    /**
     * Collection to store aggregation of collTWfActivityContextParamss
     */
    protected List<TWfActivityContextParams> collTWfActivityContextParamss;

    /**
     * Temporary storage of collTWfActivityContextParamss to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWfActivityContextParamss()
    {
        if (collTWfActivityContextParamss == null)
        {
            collTWfActivityContextParamss = new ArrayList<TWfActivityContextParams>();
        }
    }


    /**
     * Method called to associate a TWfActivityContextParams object to this object
     * through the TWfActivityContextParams foreign key attribute
     *
     * @param l TWfActivityContextParams
     * @throws TorqueException
     */
    public void addTWfActivityContextParams(TWfActivityContextParams l) throws TorqueException
    {
        getTWfActivityContextParamss().add(l);
        l.setTListType((TListType) this);
    }

    /**
     * Method called to associate a TWfActivityContextParams object to this object
     * through the TWfActivityContextParams foreign key attribute using connection.
     *
     * @param l TWfActivityContextParams
     * @throws TorqueException
     */
    public void addTWfActivityContextParams(TWfActivityContextParams l, Connection con) throws TorqueException
    {
        getTWfActivityContextParamss(con).add(l);
        l.setTListType((TListType) this);
    }

    /**
     * The criteria used to select the current contents of collTWfActivityContextParamss
     */
    private Criteria lastTWfActivityContextParamssCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWfActivityContextParamss(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWfActivityContextParams> getTWfActivityContextParamss()
        throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            collTWfActivityContextParamss = getTWfActivityContextParamss(new Criteria(10));
        }
        return collTWfActivityContextParamss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWfActivityContextParams> getTWfActivityContextParamss(Criteria criteria) throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            if (isNew())
            {
               collTWfActivityContextParamss = new ArrayList<TWfActivityContextParams>();
            }
            else
            {
                criteria.add(TWfActivityContextParamsPeer.ISSUETYPE, getObjectID() );
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelect(criteria);
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
                criteria.add(TWfActivityContextParamsPeer.ISSUETYPE, getObjectID());
                if (!lastTWfActivityContextParamssCriteria.equals(criteria))
                {
                    collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelect(criteria);
                }
            }
        }
        lastTWfActivityContextParamssCriteria = criteria;

        return collTWfActivityContextParamss;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWfActivityContextParamss(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWfActivityContextParams> getTWfActivityContextParamss(Connection con) throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            collTWfActivityContextParamss = getTWfActivityContextParamss(new Criteria(10), con);
        }
        return collTWfActivityContextParamss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWfActivityContextParams> getTWfActivityContextParamss(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            if (isNew())
            {
               collTWfActivityContextParamss = new ArrayList<TWfActivityContextParams>();
            }
            else
            {
                 criteria.add(TWfActivityContextParamsPeer.ISSUETYPE, getObjectID());
                 collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelect(criteria, con);
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
                 criteria.add(TWfActivityContextParamsPeer.ISSUETYPE, getObjectID());
                 if (!lastTWfActivityContextParamssCriteria.equals(criteria))
                 {
                     collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWfActivityContextParamssCriteria = criteria;

         return collTWfActivityContextParamss;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWfActivityContextParams> getTWfActivityContextParamssJoinTWorkflowActivity(Criteria criteria)
        throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            if (isNew())
            {
               collTWfActivityContextParamss = new ArrayList<TWfActivityContextParams>();
            }
            else
            {
                criteria.add(TWfActivityContextParamsPeer.ISSUETYPE, getObjectID());
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTWorkflowActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWfActivityContextParamsPeer.ISSUETYPE, getObjectID());
            if (!lastTWfActivityContextParamssCriteria.equals(criteria))
            {
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTWorkflowActivity(criteria);
            }
        }
        lastTWfActivityContextParamssCriteria = criteria;

        return collTWfActivityContextParamss;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWfActivityContextParams> getTWfActivityContextParamssJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            if (isNew())
            {
               collTWfActivityContextParamss = new ArrayList<TWfActivityContextParams>();
            }
            else
            {
                criteria.add(TWfActivityContextParamsPeer.ISSUETYPE, getObjectID());
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWfActivityContextParamsPeer.ISSUETYPE, getObjectID());
            if (!lastTWfActivityContextParamssCriteria.equals(criteria))
            {
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTWfActivityContextParamssCriteria = criteria;

        return collTWfActivityContextParamss;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWfActivityContextParams> getTWfActivityContextParamssJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            if (isNew())
            {
               collTWfActivityContextParamss = new ArrayList<TWfActivityContextParams>();
            }
            else
            {
                criteria.add(TWfActivityContextParamsPeer.ISSUETYPE, getObjectID());
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWfActivityContextParamsPeer.ISSUETYPE, getObjectID());
            if (!lastTWfActivityContextParamssCriteria.equals(criteria))
            {
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTWfActivityContextParamssCriteria = criteria;

        return collTWfActivityContextParamss;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWfActivityContextParams> getTWfActivityContextParamssJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            if (isNew())
            {
               collTWfActivityContextParamss = new ArrayList<TWfActivityContextParams>();
            }
            else
            {
                criteria.add(TWfActivityContextParamsPeer.ISSUETYPE, getObjectID());
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWfActivityContextParamsPeer.ISSUETYPE, getObjectID());
            if (!lastTWfActivityContextParamssCriteria.equals(criteria))
            {
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTWfActivityContextParamssCriteria = criteria;

        return collTWfActivityContextParamss;
    }





    /**
     * Collection to store aggregation of collTWorkflowConnects
     */
    protected List<TWorkflowConnect> collTWorkflowConnects;

    /**
     * Temporary storage of collTWorkflowConnects to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkflowConnects()
    {
        if (collTWorkflowConnects == null)
        {
            collTWorkflowConnects = new ArrayList<TWorkflowConnect>();
        }
    }


    /**
     * Method called to associate a TWorkflowConnect object to this object
     * through the TWorkflowConnect foreign key attribute
     *
     * @param l TWorkflowConnect
     * @throws TorqueException
     */
    public void addTWorkflowConnect(TWorkflowConnect l) throws TorqueException
    {
        getTWorkflowConnects().add(l);
        l.setTListType((TListType) this);
    }

    /**
     * Method called to associate a TWorkflowConnect object to this object
     * through the TWorkflowConnect foreign key attribute using connection.
     *
     * @param l TWorkflowConnect
     * @throws TorqueException
     */
    public void addTWorkflowConnect(TWorkflowConnect l, Connection con) throws TorqueException
    {
        getTWorkflowConnects(con).add(l);
        l.setTListType((TListType) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkflowConnects
     */
    private Criteria lastTWorkflowConnectsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowConnects(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkflowConnect> getTWorkflowConnects()
        throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            collTWorkflowConnects = getTWorkflowConnects(new Criteria(10));
        }
        return collTWorkflowConnects;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkflowConnect> getTWorkflowConnects(Criteria criteria) throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            if (isNew())
            {
               collTWorkflowConnects = new ArrayList<TWorkflowConnect>();
            }
            else
            {
                criteria.add(TWorkflowConnectPeer.ISSUETYPE, getObjectID() );
                collTWorkflowConnects = TWorkflowConnectPeer.doSelect(criteria);
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
                criteria.add(TWorkflowConnectPeer.ISSUETYPE, getObjectID());
                if (!lastTWorkflowConnectsCriteria.equals(criteria))
                {
                    collTWorkflowConnects = TWorkflowConnectPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkflowConnectsCriteria = criteria;

        return collTWorkflowConnects;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowConnects(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowConnect> getTWorkflowConnects(Connection con) throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            collTWorkflowConnects = getTWorkflowConnects(new Criteria(10), con);
        }
        return collTWorkflowConnects;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     * If this TListType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowConnect> getTWorkflowConnects(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            if (isNew())
            {
               collTWorkflowConnects = new ArrayList<TWorkflowConnect>();
            }
            else
            {
                 criteria.add(TWorkflowConnectPeer.ISSUETYPE, getObjectID());
                 collTWorkflowConnects = TWorkflowConnectPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkflowConnectPeer.ISSUETYPE, getObjectID());
                 if (!lastTWorkflowConnectsCriteria.equals(criteria))
                 {
                     collTWorkflowConnects = TWorkflowConnectPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkflowConnectsCriteria = criteria;

         return collTWorkflowConnects;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWorkflowConnect> getTWorkflowConnectsJoinTWorkflowDef(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            if (isNew())
            {
               collTWorkflowConnects = new ArrayList<TWorkflowConnect>();
            }
            else
            {
                criteria.add(TWorkflowConnectPeer.ISSUETYPE, getObjectID());
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTWorkflowDef(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowConnectPeer.ISSUETYPE, getObjectID());
            if (!lastTWorkflowConnectsCriteria.equals(criteria))
            {
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTWorkflowDef(criteria);
            }
        }
        lastTWorkflowConnectsCriteria = criteria;

        return collTWorkflowConnects;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWorkflowConnect> getTWorkflowConnectsJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            if (isNew())
            {
               collTWorkflowConnects = new ArrayList<TWorkflowConnect>();
            }
            else
            {
                criteria.add(TWorkflowConnectPeer.ISSUETYPE, getObjectID());
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowConnectPeer.ISSUETYPE, getObjectID());
            if (!lastTWorkflowConnectsCriteria.equals(criteria))
            {
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTWorkflowConnectsCriteria = criteria;

        return collTWorkflowConnects;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWorkflowConnect> getTWorkflowConnectsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            if (isNew())
            {
               collTWorkflowConnects = new ArrayList<TWorkflowConnect>();
            }
            else
            {
                criteria.add(TWorkflowConnectPeer.ISSUETYPE, getObjectID());
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowConnectPeer.ISSUETYPE, getObjectID());
            if (!lastTWorkflowConnectsCriteria.equals(criteria))
            {
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTWorkflowConnectsCriteria = criteria;

        return collTWorkflowConnects;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TListType is new, it will return
     * an empty collection; or if this TListType has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TListType.
     */
    protected List<TWorkflowConnect> getTWorkflowConnectsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            if (isNew())
            {
               collTWorkflowConnects = new ArrayList<TWorkflowConnect>();
            }
            else
            {
                criteria.add(TWorkflowConnectPeer.ISSUETYPE, getObjectID());
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowConnectPeer.ISSUETYPE, getObjectID());
            if (!lastTWorkflowConnectsCriteria.equals(criteria))
            {
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTWorkflowConnectsCriteria = criteria;

        return collTWorkflowConnects;
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
            fieldNames.add("Tooltip");
            fieldNames.add("Typeflag");
            fieldNames.add("Sortorder");
            fieldNames.add("Symbol");
            fieldNames.add("IconKey");
            fieldNames.add("IconChanged");
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
        if (name.equals("Tooltip"))
        {
            return getTooltip();
        }
        if (name.equals("Typeflag"))
        {
            return getTypeflag();
        }
        if (name.equals("Sortorder"))
        {
            return getSortorder();
        }
        if (name.equals("Symbol"))
        {
            return getSymbol();
        }
        if (name.equals("IconKey"))
        {
            return getIconKey();
        }
        if (name.equals("IconChanged"))
        {
            return getIconChanged();
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
        if (name.equals("Tooltip"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTooltip((String) value);
            return true;
        }
        if (name.equals("Typeflag"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTypeflag((Integer) value);
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
        if (name.equals("IconChanged"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIconChanged((String) value);
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
        if (name.equals(TListTypePeer.PKEY))
        {
            return getObjectID();
        }
        if (name.equals(TListTypePeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TListTypePeer.TOOLTIP))
        {
            return getTooltip();
        }
        if (name.equals(TListTypePeer.TYPEFLAG))
        {
            return getTypeflag();
        }
        if (name.equals(TListTypePeer.SORTORDER))
        {
            return getSortorder();
        }
        if (name.equals(TListTypePeer.SYMBOL))
        {
            return getSymbol();
        }
        if (name.equals(TListTypePeer.ICONKEY))
        {
            return getIconKey();
        }
        if (name.equals(TListTypePeer.ICONCHANGED))
        {
            return getIconChanged();
        }
        if (name.equals(TListTypePeer.CSSSTYLE))
        {
            return getCSSStyle();
        }
        if (name.equals(TListTypePeer.TPUUID))
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
      if (TListTypePeer.PKEY.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TListTypePeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TListTypePeer.TOOLTIP.equals(name))
        {
            return setByName("Tooltip", value);
        }
      if (TListTypePeer.TYPEFLAG.equals(name))
        {
            return setByName("Typeflag", value);
        }
      if (TListTypePeer.SORTORDER.equals(name))
        {
            return setByName("Sortorder", value);
        }
      if (TListTypePeer.SYMBOL.equals(name))
        {
            return setByName("Symbol", value);
        }
      if (TListTypePeer.ICONKEY.equals(name))
        {
            return setByName("IconKey", value);
        }
      if (TListTypePeer.ICONCHANGED.equals(name))
        {
            return setByName("IconChanged", value);
        }
      if (TListTypePeer.CSSSTYLE.equals(name))
        {
            return setByName("CSSStyle", value);
        }
      if (TListTypePeer.TPUUID.equals(name))
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
            return getTooltip();
        }
        if (pos == 3)
        {
            return getTypeflag();
        }
        if (pos == 4)
        {
            return getSortorder();
        }
        if (pos == 5)
        {
            return getSymbol();
        }
        if (pos == 6)
        {
            return getIconKey();
        }
        if (pos == 7)
        {
            return getIconChanged();
        }
        if (pos == 8)
        {
            return getCSSStyle();
        }
        if (pos == 9)
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
            return setByName("Tooltip", value);
        }
    if (position == 3)
        {
            return setByName("Typeflag", value);
        }
    if (position == 4)
        {
            return setByName("Sortorder", value);
        }
    if (position == 5)
        {
            return setByName("Symbol", value);
        }
    if (position == 6)
        {
            return setByName("IconKey", value);
        }
    if (position == 7)
        {
            return setByName("IconChanged", value);
        }
    if (position == 8)
        {
            return setByName("CSSStyle", value);
        }
    if (position == 9)
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
        save(TListTypePeer.DATABASE_NAME);
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
                    TListTypePeer.doInsert((TListType) this, con);
                    setNew(false);
                }
                else
                {
                    TListTypePeer.doUpdate((TListType) this, con);
                }
            }


            if (collTPprioritys != null)
            {
                for (int i = 0; i < collTPprioritys.size(); i++)
                {
                    ((TPpriority) collTPprioritys.get(i)).save(con);
                }
            }

            if (collTPseveritys != null)
            {
                for (int i = 0; i < collTPseveritys.size(); i++)
                {
                    ((TPseverity) collTPseveritys.get(i)).save(con);
                }
            }

            if (collTWorkItems != null)
            {
                for (int i = 0; i < collTWorkItems.size(); i++)
                {
                    ((TWorkItem) collTWorkItems.get(i)).save(con);
                }
            }

            if (collTDisableFields != null)
            {
                for (int i = 0; i < collTDisableFields.size(); i++)
                {
                    ((TDisableField) collTDisableFields.get(i)).save(con);
                }
            }

            if (collTPlistTypes != null)
            {
                for (int i = 0; i < collTPlistTypes.size(); i++)
                {
                    ((TPlistType) collTPlistTypes.get(i)).save(con);
                }
            }

            if (collTPstates != null)
            {
                for (int i = 0; i < collTPstates.size(); i++)
                {
                    ((TPstate) collTPstates.get(i)).save(con);
                }
            }

            if (collTWorkFlowCategorys != null)
            {
                for (int i = 0; i < collTWorkFlowCategorys.size(); i++)
                {
                    ((TWorkFlowCategory) collTWorkFlowCategorys.get(i)).save(con);
                }
            }

            if (collTRoleListTypes != null)
            {
                for (int i = 0; i < collTRoleListTypes.size(); i++)
                {
                    ((TRoleListType) collTRoleListTypes.get(i)).save(con);
                }
            }

            if (collTFieldConfigs != null)
            {
                for (int i = 0; i < collTFieldConfigs.size(); i++)
                {
                    ((TFieldConfig) collTFieldConfigs.get(i)).save(con);
                }
            }

            if (collTScreenConfigs != null)
            {
                for (int i = 0; i < collTScreenConfigs.size(); i++)
                {
                    ((TScreenConfig) collTScreenConfigs.get(i)).save(con);
                }
            }

            if (collTInitStates != null)
            {
                for (int i = 0; i < collTInitStates.size(); i++)
                {
                    ((TInitState) collTInitStates.get(i)).save(con);
                }
            }

            if (collTChildIssueTypesRelatedByIssueTypeParent != null)
            {
                for (int i = 0; i < collTChildIssueTypesRelatedByIssueTypeParent.size(); i++)
                {
                    ((TChildIssueType) collTChildIssueTypesRelatedByIssueTypeParent.get(i)).save(con);
                }
            }

            if (collTChildIssueTypesRelatedByIssueTypeChild != null)
            {
                for (int i = 0; i < collTChildIssueTypesRelatedByIssueTypeChild.size(); i++)
                {
                    ((TChildIssueType) collTChildIssueTypesRelatedByIssueTypeChild.get(i)).save(con);
                }
            }

            if (collTMailTemplateConfigs != null)
            {
                for (int i = 0; i < collTMailTemplateConfigs.size(); i++)
                {
                    ((TMailTemplateConfig) collTMailTemplateConfigs.get(i)).save(con);
                }
            }

            if (collTWfActivityContextParamss != null)
            {
                for (int i = 0; i < collTWfActivityContextParamss.size(); i++)
                {
                    ((TWfActivityContextParams) collTWfActivityContextParamss.get(i)).save(con);
                }
            }

            if (collTWorkflowConnects != null)
            {
                for (int i = 0; i < collTWorkflowConnects.size(); i++)
                {
                    ((TWorkflowConnect) collTWorkflowConnects.get(i)).save(con);
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
    public TListType copy() throws TorqueException
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
    public TListType copy(Connection con) throws TorqueException
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
    public TListType copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TListType(), deepcopy);
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
    public TListType copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TListType(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TListType copyInto(TListType copyObj) throws TorqueException
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
    protected TListType copyInto(TListType copyObj, Connection con) throws TorqueException
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
    protected TListType copyInto(TListType copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setTooltip(tooltip);
        copyObj.setTypeflag(typeflag);
        copyObj.setSortorder(sortorder);
        copyObj.setSymbol(symbol);
        copyObj.setIconKey(iconKey);
        copyObj.setIconChanged(iconChanged);
        copyObj.setCSSStyle(cSSStyle);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TPpriority> vTPprioritys = getTPprioritys();
        if (vTPprioritys != null)
        {
            for (int i = 0; i < vTPprioritys.size(); i++)
            {
                TPpriority obj =  vTPprioritys.get(i);
                copyObj.addTPpriority(obj.copy());
            }
        }
        else
        {
            copyObj.collTPprioritys = null;
        }


        List<TPseverity> vTPseveritys = getTPseveritys();
        if (vTPseveritys != null)
        {
            for (int i = 0; i < vTPseveritys.size(); i++)
            {
                TPseverity obj =  vTPseveritys.get(i);
                copyObj.addTPseverity(obj.copy());
            }
        }
        else
        {
            copyObj.collTPseveritys = null;
        }


        List<TWorkItem> vTWorkItems = getTWorkItems();
        if (vTWorkItems != null)
        {
            for (int i = 0; i < vTWorkItems.size(); i++)
            {
                TWorkItem obj =  vTWorkItems.get(i);
                copyObj.addTWorkItem(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkItems = null;
        }


        List<TDisableField> vTDisableFields = getTDisableFields();
        if (vTDisableFields != null)
        {
            for (int i = 0; i < vTDisableFields.size(); i++)
            {
                TDisableField obj =  vTDisableFields.get(i);
                copyObj.addTDisableField(obj.copy());
            }
        }
        else
        {
            copyObj.collTDisableFields = null;
        }


        List<TPlistType> vTPlistTypes = getTPlistTypes();
        if (vTPlistTypes != null)
        {
            for (int i = 0; i < vTPlistTypes.size(); i++)
            {
                TPlistType obj =  vTPlistTypes.get(i);
                copyObj.addTPlistType(obj.copy());
            }
        }
        else
        {
            copyObj.collTPlistTypes = null;
        }


        List<TPstate> vTPstates = getTPstates();
        if (vTPstates != null)
        {
            for (int i = 0; i < vTPstates.size(); i++)
            {
                TPstate obj =  vTPstates.get(i);
                copyObj.addTPstate(obj.copy());
            }
        }
        else
        {
            copyObj.collTPstates = null;
        }


        List<TWorkFlowCategory> vTWorkFlowCategorys = getTWorkFlowCategorys();
        if (vTWorkFlowCategorys != null)
        {
            for (int i = 0; i < vTWorkFlowCategorys.size(); i++)
            {
                TWorkFlowCategory obj =  vTWorkFlowCategorys.get(i);
                copyObj.addTWorkFlowCategory(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkFlowCategorys = null;
        }


        List<TRoleListType> vTRoleListTypes = getTRoleListTypes();
        if (vTRoleListTypes != null)
        {
            for (int i = 0; i < vTRoleListTypes.size(); i++)
            {
                TRoleListType obj =  vTRoleListTypes.get(i);
                copyObj.addTRoleListType(obj.copy());
            }
        }
        else
        {
            copyObj.collTRoleListTypes = null;
        }


        List<TFieldConfig> vTFieldConfigs = getTFieldConfigs();
        if (vTFieldConfigs != null)
        {
            for (int i = 0; i < vTFieldConfigs.size(); i++)
            {
                TFieldConfig obj =  vTFieldConfigs.get(i);
                copyObj.addTFieldConfig(obj.copy());
            }
        }
        else
        {
            copyObj.collTFieldConfigs = null;
        }


        List<TScreenConfig> vTScreenConfigs = getTScreenConfigs();
        if (vTScreenConfigs != null)
        {
            for (int i = 0; i < vTScreenConfigs.size(); i++)
            {
                TScreenConfig obj =  vTScreenConfigs.get(i);
                copyObj.addTScreenConfig(obj.copy());
            }
        }
        else
        {
            copyObj.collTScreenConfigs = null;
        }


        List<TInitState> vTInitStates = getTInitStates();
        if (vTInitStates != null)
        {
            for (int i = 0; i < vTInitStates.size(); i++)
            {
                TInitState obj =  vTInitStates.get(i);
                copyObj.addTInitState(obj.copy());
            }
        }
        else
        {
            copyObj.collTInitStates = null;
        }


        List<TChildIssueType> vTChildIssueTypesRelatedByIssueTypeParent = getTChildIssueTypesRelatedByIssueTypeParent();
        if (vTChildIssueTypesRelatedByIssueTypeParent != null)
        {
            for (int i = 0; i < vTChildIssueTypesRelatedByIssueTypeParent.size(); i++)
            {
                TChildIssueType obj =  vTChildIssueTypesRelatedByIssueTypeParent.get(i);
                copyObj.addTChildIssueTypeRelatedByIssueTypeParent(obj.copy());
            }
        }
        else
        {
            copyObj.collTChildIssueTypesRelatedByIssueTypeParent = null;
        }


        List<TChildIssueType> vTChildIssueTypesRelatedByIssueTypeChild = getTChildIssueTypesRelatedByIssueTypeChild();
        if (vTChildIssueTypesRelatedByIssueTypeChild != null)
        {
            for (int i = 0; i < vTChildIssueTypesRelatedByIssueTypeChild.size(); i++)
            {
                TChildIssueType obj =  vTChildIssueTypesRelatedByIssueTypeChild.get(i);
                copyObj.addTChildIssueTypeRelatedByIssueTypeChild(obj.copy());
            }
        }
        else
        {
            copyObj.collTChildIssueTypesRelatedByIssueTypeChild = null;
        }


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


        List<TWfActivityContextParams> vTWfActivityContextParamss = getTWfActivityContextParamss();
        if (vTWfActivityContextParamss != null)
        {
            for (int i = 0; i < vTWfActivityContextParamss.size(); i++)
            {
                TWfActivityContextParams obj =  vTWfActivityContextParamss.get(i);
                copyObj.addTWfActivityContextParams(obj.copy());
            }
        }
        else
        {
            copyObj.collTWfActivityContextParamss = null;
        }


        List<TWorkflowConnect> vTWorkflowConnects = getTWorkflowConnects();
        if (vTWorkflowConnects != null)
        {
            for (int i = 0; i < vTWorkflowConnects.size(); i++)
            {
                TWorkflowConnect obj =  vTWorkflowConnects.get(i);
                copyObj.addTWorkflowConnect(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkflowConnects = null;
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
    protected TListType copyInto(TListType copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setTooltip(tooltip);
        copyObj.setTypeflag(typeflag);
        copyObj.setSortorder(sortorder);
        copyObj.setSymbol(symbol);
        copyObj.setIconKey(iconKey);
        copyObj.setIconChanged(iconChanged);
        copyObj.setCSSStyle(cSSStyle);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TPpriority> vTPprioritys = getTPprioritys(con);
        if (vTPprioritys != null)
        {
            for (int i = 0; i < vTPprioritys.size(); i++)
            {
                TPpriority obj =  vTPprioritys.get(i);
                copyObj.addTPpriority(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTPprioritys = null;
        }


        List<TPseverity> vTPseveritys = getTPseveritys(con);
        if (vTPseveritys != null)
        {
            for (int i = 0; i < vTPseveritys.size(); i++)
            {
                TPseverity obj =  vTPseveritys.get(i);
                copyObj.addTPseverity(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTPseveritys = null;
        }


        List<TWorkItem> vTWorkItems = getTWorkItems(con);
        if (vTWorkItems != null)
        {
            for (int i = 0; i < vTWorkItems.size(); i++)
            {
                TWorkItem obj =  vTWorkItems.get(i);
                copyObj.addTWorkItem(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkItems = null;
        }


        List<TDisableField> vTDisableFields = getTDisableFields(con);
        if (vTDisableFields != null)
        {
            for (int i = 0; i < vTDisableFields.size(); i++)
            {
                TDisableField obj =  vTDisableFields.get(i);
                copyObj.addTDisableField(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTDisableFields = null;
        }


        List<TPlistType> vTPlistTypes = getTPlistTypes(con);
        if (vTPlistTypes != null)
        {
            for (int i = 0; i < vTPlistTypes.size(); i++)
            {
                TPlistType obj =  vTPlistTypes.get(i);
                copyObj.addTPlistType(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTPlistTypes = null;
        }


        List<TPstate> vTPstates = getTPstates(con);
        if (vTPstates != null)
        {
            for (int i = 0; i < vTPstates.size(); i++)
            {
                TPstate obj =  vTPstates.get(i);
                copyObj.addTPstate(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTPstates = null;
        }


        List<TWorkFlowCategory> vTWorkFlowCategorys = getTWorkFlowCategorys(con);
        if (vTWorkFlowCategorys != null)
        {
            for (int i = 0; i < vTWorkFlowCategorys.size(); i++)
            {
                TWorkFlowCategory obj =  vTWorkFlowCategorys.get(i);
                copyObj.addTWorkFlowCategory(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkFlowCategorys = null;
        }


        List<TRoleListType> vTRoleListTypes = getTRoleListTypes(con);
        if (vTRoleListTypes != null)
        {
            for (int i = 0; i < vTRoleListTypes.size(); i++)
            {
                TRoleListType obj =  vTRoleListTypes.get(i);
                copyObj.addTRoleListType(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTRoleListTypes = null;
        }


        List<TFieldConfig> vTFieldConfigs = getTFieldConfigs(con);
        if (vTFieldConfigs != null)
        {
            for (int i = 0; i < vTFieldConfigs.size(); i++)
            {
                TFieldConfig obj =  vTFieldConfigs.get(i);
                copyObj.addTFieldConfig(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTFieldConfigs = null;
        }


        List<TScreenConfig> vTScreenConfigs = getTScreenConfigs(con);
        if (vTScreenConfigs != null)
        {
            for (int i = 0; i < vTScreenConfigs.size(); i++)
            {
                TScreenConfig obj =  vTScreenConfigs.get(i);
                copyObj.addTScreenConfig(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTScreenConfigs = null;
        }


        List<TInitState> vTInitStates = getTInitStates(con);
        if (vTInitStates != null)
        {
            for (int i = 0; i < vTInitStates.size(); i++)
            {
                TInitState obj =  vTInitStates.get(i);
                copyObj.addTInitState(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTInitStates = null;
        }


        List<TChildIssueType> vTChildIssueTypesRelatedByIssueTypeParent = getTChildIssueTypesRelatedByIssueTypeParent(con);
        if (vTChildIssueTypesRelatedByIssueTypeParent != null)
        {
            for (int i = 0; i < vTChildIssueTypesRelatedByIssueTypeParent.size(); i++)
            {
                TChildIssueType obj =  vTChildIssueTypesRelatedByIssueTypeParent.get(i);
                copyObj.addTChildIssueTypeRelatedByIssueTypeParent(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTChildIssueTypesRelatedByIssueTypeParent = null;
        }


        List<TChildIssueType> vTChildIssueTypesRelatedByIssueTypeChild = getTChildIssueTypesRelatedByIssueTypeChild(con);
        if (vTChildIssueTypesRelatedByIssueTypeChild != null)
        {
            for (int i = 0; i < vTChildIssueTypesRelatedByIssueTypeChild.size(); i++)
            {
                TChildIssueType obj =  vTChildIssueTypesRelatedByIssueTypeChild.get(i);
                copyObj.addTChildIssueTypeRelatedByIssueTypeChild(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTChildIssueTypesRelatedByIssueTypeChild = null;
        }


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


        List<TWfActivityContextParams> vTWfActivityContextParamss = getTWfActivityContextParamss(con);
        if (vTWfActivityContextParamss != null)
        {
            for (int i = 0; i < vTWfActivityContextParamss.size(); i++)
            {
                TWfActivityContextParams obj =  vTWfActivityContextParamss.get(i);
                copyObj.addTWfActivityContextParams(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWfActivityContextParamss = null;
        }


        List<TWorkflowConnect> vTWorkflowConnects = getTWorkflowConnects(con);
        if (vTWorkflowConnects != null)
        {
            for (int i = 0; i < vTWorkflowConnects.size(); i++)
            {
                TWorkflowConnect obj =  vTWorkflowConnects.get(i);
                copyObj.addTWorkflowConnect(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkflowConnects = null;
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
    public TListTypePeer getPeer()
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
        return TListTypePeer.getTableMap();
    }

  
    /**
     * Creates a TListTypeBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TListTypeBean with the contents of this object
     */
    public TListTypeBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TListTypeBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TListTypeBean with the contents of this object
     */
    public TListTypeBean getBean(IdentityMap createdBeans)
    {
        TListTypeBean result = (TListTypeBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TListTypeBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setLabel(getLabel());
        result.setTooltip(getTooltip());
        result.setTypeflag(getTypeflag());
        result.setSortorder(getSortorder());
        result.setSymbol(getSymbol());
        result.setIconKey(getIconKey());
        result.setIconChanged(getIconChanged());
        result.setCSSStyle(getCSSStyle());
        result.setUuid(getUuid());



        if (collTPprioritys != null)
        {
            List<TPpriorityBean> relatedBeans = new ArrayList<TPpriorityBean>(collTPprioritys.size());
            for (Iterator<TPpriority> collTPprioritysIt = collTPprioritys.iterator(); collTPprioritysIt.hasNext(); )
            {
                TPpriority related = (TPpriority) collTPprioritysIt.next();
                TPpriorityBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTPpriorityBeans(relatedBeans);
        }


        if (collTPseveritys != null)
        {
            List<TPseverityBean> relatedBeans = new ArrayList<TPseverityBean>(collTPseveritys.size());
            for (Iterator<TPseverity> collTPseveritysIt = collTPseveritys.iterator(); collTPseveritysIt.hasNext(); )
            {
                TPseverity related = (TPseverity) collTPseveritysIt.next();
                TPseverityBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTPseverityBeans(relatedBeans);
        }


        if (collTWorkItems != null)
        {
            List<TWorkItemBean> relatedBeans = new ArrayList<TWorkItemBean>(collTWorkItems.size());
            for (Iterator<TWorkItem> collTWorkItemsIt = collTWorkItems.iterator(); collTWorkItemsIt.hasNext(); )
            {
                TWorkItem related = (TWorkItem) collTWorkItemsIt.next();
                TWorkItemBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkItemBeans(relatedBeans);
        }


        if (collTDisableFields != null)
        {
            List<TDisableFieldBean> relatedBeans = new ArrayList<TDisableFieldBean>(collTDisableFields.size());
            for (Iterator<TDisableField> collTDisableFieldsIt = collTDisableFields.iterator(); collTDisableFieldsIt.hasNext(); )
            {
                TDisableField related = (TDisableField) collTDisableFieldsIt.next();
                TDisableFieldBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTDisableFieldBeans(relatedBeans);
        }


        if (collTPlistTypes != null)
        {
            List<TPlistTypeBean> relatedBeans = new ArrayList<TPlistTypeBean>(collTPlistTypes.size());
            for (Iterator<TPlistType> collTPlistTypesIt = collTPlistTypes.iterator(); collTPlistTypesIt.hasNext(); )
            {
                TPlistType related = (TPlistType) collTPlistTypesIt.next();
                TPlistTypeBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTPlistTypeBeans(relatedBeans);
        }


        if (collTPstates != null)
        {
            List<TPstateBean> relatedBeans = new ArrayList<TPstateBean>(collTPstates.size());
            for (Iterator<TPstate> collTPstatesIt = collTPstates.iterator(); collTPstatesIt.hasNext(); )
            {
                TPstate related = (TPstate) collTPstatesIt.next();
                TPstateBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTPstateBeans(relatedBeans);
        }


        if (collTWorkFlowCategorys != null)
        {
            List<TWorkFlowCategoryBean> relatedBeans = new ArrayList<TWorkFlowCategoryBean>(collTWorkFlowCategorys.size());
            for (Iterator<TWorkFlowCategory> collTWorkFlowCategorysIt = collTWorkFlowCategorys.iterator(); collTWorkFlowCategorysIt.hasNext(); )
            {
                TWorkFlowCategory related = (TWorkFlowCategory) collTWorkFlowCategorysIt.next();
                TWorkFlowCategoryBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkFlowCategoryBeans(relatedBeans);
        }


        if (collTRoleListTypes != null)
        {
            List<TRoleListTypeBean> relatedBeans = new ArrayList<TRoleListTypeBean>(collTRoleListTypes.size());
            for (Iterator<TRoleListType> collTRoleListTypesIt = collTRoleListTypes.iterator(); collTRoleListTypesIt.hasNext(); )
            {
                TRoleListType related = (TRoleListType) collTRoleListTypesIt.next();
                TRoleListTypeBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTRoleListTypeBeans(relatedBeans);
        }


        if (collTFieldConfigs != null)
        {
            List<TFieldConfigBean> relatedBeans = new ArrayList<TFieldConfigBean>(collTFieldConfigs.size());
            for (Iterator<TFieldConfig> collTFieldConfigsIt = collTFieldConfigs.iterator(); collTFieldConfigsIt.hasNext(); )
            {
                TFieldConfig related = (TFieldConfig) collTFieldConfigsIt.next();
                TFieldConfigBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTFieldConfigBeans(relatedBeans);
        }


        if (collTScreenConfigs != null)
        {
            List<TScreenConfigBean> relatedBeans = new ArrayList<TScreenConfigBean>(collTScreenConfigs.size());
            for (Iterator<TScreenConfig> collTScreenConfigsIt = collTScreenConfigs.iterator(); collTScreenConfigsIt.hasNext(); )
            {
                TScreenConfig related = (TScreenConfig) collTScreenConfigsIt.next();
                TScreenConfigBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTScreenConfigBeans(relatedBeans);
        }


        if (collTInitStates != null)
        {
            List<TInitStateBean> relatedBeans = new ArrayList<TInitStateBean>(collTInitStates.size());
            for (Iterator<TInitState> collTInitStatesIt = collTInitStates.iterator(); collTInitStatesIt.hasNext(); )
            {
                TInitState related = (TInitState) collTInitStatesIt.next();
                TInitStateBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTInitStateBeans(relatedBeans);
        }


        if (collTChildIssueTypesRelatedByIssueTypeParent != null)
        {
            List<TChildIssueTypeBean> relatedBeans = new ArrayList<TChildIssueTypeBean>(collTChildIssueTypesRelatedByIssueTypeParent.size());
            for (Iterator<TChildIssueType> collTChildIssueTypesRelatedByIssueTypeParentIt = collTChildIssueTypesRelatedByIssueTypeParent.iterator(); collTChildIssueTypesRelatedByIssueTypeParentIt.hasNext(); )
            {
                TChildIssueType related = (TChildIssueType) collTChildIssueTypesRelatedByIssueTypeParentIt.next();
                TChildIssueTypeBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTChildIssueTypeBeansRelatedByIssueTypeParent(relatedBeans);
        }


        if (collTChildIssueTypesRelatedByIssueTypeChild != null)
        {
            List<TChildIssueTypeBean> relatedBeans = new ArrayList<TChildIssueTypeBean>(collTChildIssueTypesRelatedByIssueTypeChild.size());
            for (Iterator<TChildIssueType> collTChildIssueTypesRelatedByIssueTypeChildIt = collTChildIssueTypesRelatedByIssueTypeChild.iterator(); collTChildIssueTypesRelatedByIssueTypeChildIt.hasNext(); )
            {
                TChildIssueType related = (TChildIssueType) collTChildIssueTypesRelatedByIssueTypeChildIt.next();
                TChildIssueTypeBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTChildIssueTypeBeansRelatedByIssueTypeChild(relatedBeans);
        }


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


        if (collTWfActivityContextParamss != null)
        {
            List<TWfActivityContextParamsBean> relatedBeans = new ArrayList<TWfActivityContextParamsBean>(collTWfActivityContextParamss.size());
            for (Iterator<TWfActivityContextParams> collTWfActivityContextParamssIt = collTWfActivityContextParamss.iterator(); collTWfActivityContextParamssIt.hasNext(); )
            {
                TWfActivityContextParams related = (TWfActivityContextParams) collTWfActivityContextParamssIt.next();
                TWfActivityContextParamsBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWfActivityContextParamsBeans(relatedBeans);
        }


        if (collTWorkflowConnects != null)
        {
            List<TWorkflowConnectBean> relatedBeans = new ArrayList<TWorkflowConnectBean>(collTWorkflowConnects.size());
            for (Iterator<TWorkflowConnect> collTWorkflowConnectsIt = collTWorkflowConnects.iterator(); collTWorkflowConnectsIt.hasNext(); )
            {
                TWorkflowConnect related = (TWorkflowConnect) collTWorkflowConnectsIt.next();
                TWorkflowConnectBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkflowConnectBeans(relatedBeans);
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
     * Creates an instance of TListType with the contents
     * of a TListTypeBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TListTypeBean which contents are used to create
     *        the resulting class
     * @return an instance of TListType with the contents of bean
     */
    public static TListType createTListType(TListTypeBean bean)
        throws TorqueException
    {
        return createTListType(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TListType with the contents
     * of a TListTypeBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TListTypeBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TListType with the contents of bean
     */

    public static TListType createTListType(TListTypeBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TListType result = (TListType) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TListType();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setLabel(bean.getLabel());
        result.setTooltip(bean.getTooltip());
        result.setTypeflag(bean.getTypeflag());
        result.setSortorder(bean.getSortorder());
        result.setSymbol(bean.getSymbol());
        result.setIconKey(bean.getIconKey());
        result.setIconChanged(bean.getIconChanged());
        result.setCSSStyle(bean.getCSSStyle());
        result.setUuid(bean.getUuid());



        {
            List<TPpriorityBean> relatedBeans = bean.getTPpriorityBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TPpriorityBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TPpriorityBean relatedBean =  relatedBeansIt.next();
                    TPpriority related = TPpriority.createTPpriority(relatedBean, createdObjects);
                    result.addTPpriorityFromBean(related);
                }
            }
        }


        {
            List<TPseverityBean> relatedBeans = bean.getTPseverityBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TPseverityBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TPseverityBean relatedBean =  relatedBeansIt.next();
                    TPseverity related = TPseverity.createTPseverity(relatedBean, createdObjects);
                    result.addTPseverityFromBean(related);
                }
            }
        }


        {
            List<TWorkItemBean> relatedBeans = bean.getTWorkItemBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkItemBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkItemBean relatedBean =  relatedBeansIt.next();
                    TWorkItem related = TWorkItem.createTWorkItem(relatedBean, createdObjects);
                    result.addTWorkItemFromBean(related);
                }
            }
        }


        {
            List<TDisableFieldBean> relatedBeans = bean.getTDisableFieldBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TDisableFieldBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TDisableFieldBean relatedBean =  relatedBeansIt.next();
                    TDisableField related = TDisableField.createTDisableField(relatedBean, createdObjects);
                    result.addTDisableFieldFromBean(related);
                }
            }
        }


        {
            List<TPlistTypeBean> relatedBeans = bean.getTPlistTypeBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TPlistTypeBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TPlistTypeBean relatedBean =  relatedBeansIt.next();
                    TPlistType related = TPlistType.createTPlistType(relatedBean, createdObjects);
                    result.addTPlistTypeFromBean(related);
                }
            }
        }


        {
            List<TPstateBean> relatedBeans = bean.getTPstateBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TPstateBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TPstateBean relatedBean =  relatedBeansIt.next();
                    TPstate related = TPstate.createTPstate(relatedBean, createdObjects);
                    result.addTPstateFromBean(related);
                }
            }
        }


        {
            List<TWorkFlowCategoryBean> relatedBeans = bean.getTWorkFlowCategoryBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkFlowCategoryBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkFlowCategoryBean relatedBean =  relatedBeansIt.next();
                    TWorkFlowCategory related = TWorkFlowCategory.createTWorkFlowCategory(relatedBean, createdObjects);
                    result.addTWorkFlowCategoryFromBean(related);
                }
            }
        }


        {
            List<TRoleListTypeBean> relatedBeans = bean.getTRoleListTypeBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TRoleListTypeBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TRoleListTypeBean relatedBean =  relatedBeansIt.next();
                    TRoleListType related = TRoleListType.createTRoleListType(relatedBean, createdObjects);
                    result.addTRoleListTypeFromBean(related);
                }
            }
        }


        {
            List<TFieldConfigBean> relatedBeans = bean.getTFieldConfigBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TFieldConfigBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TFieldConfigBean relatedBean =  relatedBeansIt.next();
                    TFieldConfig related = TFieldConfig.createTFieldConfig(relatedBean, createdObjects);
                    result.addTFieldConfigFromBean(related);
                }
            }
        }


        {
            List<TScreenConfigBean> relatedBeans = bean.getTScreenConfigBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TScreenConfigBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TScreenConfigBean relatedBean =  relatedBeansIt.next();
                    TScreenConfig related = TScreenConfig.createTScreenConfig(relatedBean, createdObjects);
                    result.addTScreenConfigFromBean(related);
                }
            }
        }


        {
            List<TInitStateBean> relatedBeans = bean.getTInitStateBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TInitStateBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TInitStateBean relatedBean =  relatedBeansIt.next();
                    TInitState related = TInitState.createTInitState(relatedBean, createdObjects);
                    result.addTInitStateFromBean(related);
                }
            }
        }


        {
            List<TChildIssueTypeBean> relatedBeans = bean.getTChildIssueTypeBeansRelatedByIssueTypeParent();
            if (relatedBeans != null)
            {
                for (Iterator<TChildIssueTypeBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TChildIssueTypeBean relatedBean =  relatedBeansIt.next();
                    TChildIssueType related = TChildIssueType.createTChildIssueType(relatedBean, createdObjects);
                    result.addTChildIssueTypeRelatedByIssueTypeParentFromBean(related);
                }
            }
        }


        {
            List<TChildIssueTypeBean> relatedBeans = bean.getTChildIssueTypeBeansRelatedByIssueTypeChild();
            if (relatedBeans != null)
            {
                for (Iterator<TChildIssueTypeBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TChildIssueTypeBean relatedBean =  relatedBeansIt.next();
                    TChildIssueType related = TChildIssueType.createTChildIssueType(relatedBean, createdObjects);
                    result.addTChildIssueTypeRelatedByIssueTypeChildFromBean(related);
                }
            }
        }


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
            List<TWfActivityContextParamsBean> relatedBeans = bean.getTWfActivityContextParamsBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWfActivityContextParamsBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWfActivityContextParamsBean relatedBean =  relatedBeansIt.next();
                    TWfActivityContextParams related = TWfActivityContextParams.createTWfActivityContextParams(relatedBean, createdObjects);
                    result.addTWfActivityContextParamsFromBean(related);
                }
            }
        }


        {
            List<TWorkflowConnectBean> relatedBeans = bean.getTWorkflowConnectBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkflowConnectBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkflowConnectBean relatedBean =  relatedBeansIt.next();
                    TWorkflowConnect related = TWorkflowConnect.createTWorkflowConnect(relatedBean, createdObjects);
                    result.addTWorkflowConnectFromBean(related);
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
     * Method called to associate a TPpriority object to this object.
     * through the TPpriority foreign key attribute
     *
     * @param toAdd TPpriority
     */
    protected void addTPpriorityFromBean(TPpriority toAdd)
    {
        initTPprioritys();
        collTPprioritys.add(toAdd);
    }


    /**
     * Method called to associate a TPseverity object to this object.
     * through the TPseverity foreign key attribute
     *
     * @param toAdd TPseverity
     */
    protected void addTPseverityFromBean(TPseverity toAdd)
    {
        initTPseveritys();
        collTPseveritys.add(toAdd);
    }


    /**
     * Method called to associate a TWorkItem object to this object.
     * through the TWorkItem foreign key attribute
     *
     * @param toAdd TWorkItem
     */
    protected void addTWorkItemFromBean(TWorkItem toAdd)
    {
        initTWorkItems();
        collTWorkItems.add(toAdd);
    }


    /**
     * Method called to associate a TDisableField object to this object.
     * through the TDisableField foreign key attribute
     *
     * @param toAdd TDisableField
     */
    protected void addTDisableFieldFromBean(TDisableField toAdd)
    {
        initTDisableFields();
        collTDisableFields.add(toAdd);
    }


    /**
     * Method called to associate a TPlistType object to this object.
     * through the TPlistType foreign key attribute
     *
     * @param toAdd TPlistType
     */
    protected void addTPlistTypeFromBean(TPlistType toAdd)
    {
        initTPlistTypes();
        collTPlistTypes.add(toAdd);
    }


    /**
     * Method called to associate a TPstate object to this object.
     * through the TPstate foreign key attribute
     *
     * @param toAdd TPstate
     */
    protected void addTPstateFromBean(TPstate toAdd)
    {
        initTPstates();
        collTPstates.add(toAdd);
    }


    /**
     * Method called to associate a TWorkFlowCategory object to this object.
     * through the TWorkFlowCategory foreign key attribute
     *
     * @param toAdd TWorkFlowCategory
     */
    protected void addTWorkFlowCategoryFromBean(TWorkFlowCategory toAdd)
    {
        initTWorkFlowCategorys();
        collTWorkFlowCategorys.add(toAdd);
    }


    /**
     * Method called to associate a TRoleListType object to this object.
     * through the TRoleListType foreign key attribute
     *
     * @param toAdd TRoleListType
     */
    protected void addTRoleListTypeFromBean(TRoleListType toAdd)
    {
        initTRoleListTypes();
        collTRoleListTypes.add(toAdd);
    }


    /**
     * Method called to associate a TFieldConfig object to this object.
     * through the TFieldConfig foreign key attribute
     *
     * @param toAdd TFieldConfig
     */
    protected void addTFieldConfigFromBean(TFieldConfig toAdd)
    {
        initTFieldConfigs();
        collTFieldConfigs.add(toAdd);
    }


    /**
     * Method called to associate a TScreenConfig object to this object.
     * through the TScreenConfig foreign key attribute
     *
     * @param toAdd TScreenConfig
     */
    protected void addTScreenConfigFromBean(TScreenConfig toAdd)
    {
        initTScreenConfigs();
        collTScreenConfigs.add(toAdd);
    }


    /**
     * Method called to associate a TInitState object to this object.
     * through the TInitState foreign key attribute
     *
     * @param toAdd TInitState
     */
    protected void addTInitStateFromBean(TInitState toAdd)
    {
        initTInitStates();
        collTInitStates.add(toAdd);
    }


    /**
     * Method called to associate a TChildIssueType object to this object.
     * through the TChildIssueType foreign key attribute
     *
     * @param toAdd TChildIssueType
     */
    protected void addTChildIssueTypeRelatedByIssueTypeParentFromBean(TChildIssueType toAdd)
    {
        initTChildIssueTypesRelatedByIssueTypeParent();
        collTChildIssueTypesRelatedByIssueTypeParent.add(toAdd);
    }


    /**
     * Method called to associate a TChildIssueType object to this object.
     * through the TChildIssueType foreign key attribute
     *
     * @param toAdd TChildIssueType
     */
    protected void addTChildIssueTypeRelatedByIssueTypeChildFromBean(TChildIssueType toAdd)
    {
        initTChildIssueTypesRelatedByIssueTypeChild();
        collTChildIssueTypesRelatedByIssueTypeChild.add(toAdd);
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
     * Method called to associate a TWfActivityContextParams object to this object.
     * through the TWfActivityContextParams foreign key attribute
     *
     * @param toAdd TWfActivityContextParams
     */
    protected void addTWfActivityContextParamsFromBean(TWfActivityContextParams toAdd)
    {
        initTWfActivityContextParamss();
        collTWfActivityContextParamss.add(toAdd);
    }


    /**
     * Method called to associate a TWorkflowConnect object to this object.
     * through the TWorkflowConnect foreign key attribute
     *
     * @param toAdd TWorkflowConnect
     */
    protected void addTWorkflowConnectFromBean(TWorkflowConnect toAdd)
    {
        initTWorkflowConnects();
        collTWorkflowConnects.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TListType:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("Tooltip = ")
           .append(getTooltip())
           .append("\n");
        str.append("Typeflag = ")
           .append(getTypeflag())
           .append("\n");
        str.append("Sortorder = ")
           .append(getSortorder())
           .append("\n");
        str.append("Symbol = ")
           .append(getSymbol())
           .append("\n");
        str.append("IconKey = ")
           .append(getIconKey())
           .append("\n");
        str.append("IconChanged = ")
           .append(getIconChanged())
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
