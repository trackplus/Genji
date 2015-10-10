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
import com.aurel.track.beans.TProjectTypeBean;

import com.aurel.track.beans.TPpriorityBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TRoleBean;
import com.aurel.track.beans.TPseverityBean;
import com.aurel.track.beans.TDocStateBean;
import com.aurel.track.beans.TDisableFieldBean;
import com.aurel.track.beans.TPlistTypeBean;
import com.aurel.track.beans.TPstateBean;
import com.aurel.track.beans.TWorkFlowBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TScreenBean;
import com.aurel.track.beans.TScreenConfigBean;
import com.aurel.track.beans.TEventBean;
import com.aurel.track.beans.TScriptsBean;
import com.aurel.track.beans.TMailTemplateConfigBean;
import com.aurel.track.beans.TWfActivityContextParamsBean;
import com.aurel.track.beans.TWorkflowConnectBean;
import com.aurel.track.beans.TPRoleBean;
import com.aurel.track.beans.TChildProjectTypeBean;
import com.aurel.track.beans.TChildProjectTypeBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TProjectType
 */
public abstract class BaseTProjectType extends TpBaseObject
{
    /** The Peer class */
    private static final TProjectTypePeer peer =
        new TProjectTypePeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the label field */
    private String label;

    /** The value for the notifyOwnerLevel field */
    private Integer notifyOwnerLevel;

    /** The value for the notifyManagerLevel field */
    private Integer notifyManagerLevel;

    /** The value for the hoursPerWorkDay field */
    private Double hoursPerWorkDay;

    /** The value for the moreProps field */
    private String moreProps;

    /** The value for the defaultForPrivate field */
    private String defaultForPrivate = "N";

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
                        .setProjectType(v);
            }
        }

        // update associated TProject
        if (collTProjects != null)
        {
            for (int i = 0; i < collTProjects.size(); i++)
            {
                ((TProject) collTProjects.get(i))
                        .setProjectType(v);
            }
        }

        // update associated TRole
        if (collTRoles != null)
        {
            for (int i = 0; i < collTRoles.size(); i++)
            {
                ((TRole) collTRoles.get(i))
                        .setProjecttype(v);
            }
        }

        // update associated TPseverity
        if (collTPseveritys != null)
        {
            for (int i = 0; i < collTPseveritys.size(); i++)
            {
                ((TPseverity) collTPseveritys.get(i))
                        .setProjectType(v);
            }
        }

        // update associated TDocState
        if (collTDocStates != null)
        {
            for (int i = 0; i < collTDocStates.size(); i++)
            {
                ((TDocState) collTDocStates.get(i))
                        .setProjectType(v);
            }
        }

        // update associated TDisableField
        if (collTDisableFields != null)
        {
            for (int i = 0; i < collTDisableFields.size(); i++)
            {
                ((TDisableField) collTDisableFields.get(i))
                        .setProjectType(v);
            }
        }

        // update associated TPlistType
        if (collTPlistTypes != null)
        {
            for (int i = 0; i < collTPlistTypes.size(); i++)
            {
                ((TPlistType) collTPlistTypes.get(i))
                        .setProjectType(v);
            }
        }

        // update associated TPstate
        if (collTPstates != null)
        {
            for (int i = 0; i < collTPstates.size(); i++)
            {
                ((TPstate) collTPstates.get(i))
                        .setProjectType(v);
            }
        }

        // update associated TWorkFlow
        if (collTWorkFlows != null)
        {
            for (int i = 0; i < collTWorkFlows.size(); i++)
            {
                ((TWorkFlow) collTWorkFlows.get(i))
                        .setProjectType(v);
            }
        }

        // update associated TReportLayout
        if (collTReportLayouts != null)
        {
            for (int i = 0; i < collTReportLayouts.size(); i++)
            {
                ((TReportLayout) collTReportLayouts.get(i))
                        .setProjectType(v);
            }
        }

        // update associated TField
        if (collTFields != null)
        {
            for (int i = 0; i < collTFields.size(); i++)
            {
                ((TField) collTFields.get(i))
                        .setProjectType(v);
            }
        }

        // update associated TFieldConfig
        if (collTFieldConfigs != null)
        {
            for (int i = 0; i < collTFieldConfigs.size(); i++)
            {
                ((TFieldConfig) collTFieldConfigs.get(i))
                        .setProjectType(v);
            }
        }

        // update associated TScreen
        if (collTScreens != null)
        {
            for (int i = 0; i < collTScreens.size(); i++)
            {
                ((TScreen) collTScreens.get(i))
                        .setProjectType(v);
            }
        }

        // update associated TScreenConfig
        if (collTScreenConfigs != null)
        {
            for (int i = 0; i < collTScreenConfigs.size(); i++)
            {
                ((TScreenConfig) collTScreenConfigs.get(i))
                        .setProjectType(v);
            }
        }

        // update associated TEvent
        if (collTEvents != null)
        {
            for (int i = 0; i < collTEvents.size(); i++)
            {
                ((TEvent) collTEvents.get(i))
                        .setProjectType(v);
            }
        }

        // update associated TScripts
        if (collTScriptss != null)
        {
            for (int i = 0; i < collTScriptss.size(); i++)
            {
                ((TScripts) collTScriptss.get(i))
                        .setProjectType(v);
            }
        }

        // update associated TMailTemplateConfig
        if (collTMailTemplateConfigs != null)
        {
            for (int i = 0; i < collTMailTemplateConfigs.size(); i++)
            {
                ((TMailTemplateConfig) collTMailTemplateConfigs.get(i))
                        .setProjectType(v);
            }
        }

        // update associated TWfActivityContextParams
        if (collTWfActivityContextParamss != null)
        {
            for (int i = 0; i < collTWfActivityContextParamss.size(); i++)
            {
                ((TWfActivityContextParams) collTWfActivityContextParamss.get(i))
                        .setProjectType(v);
            }
        }

        // update associated TWorkflowConnect
        if (collTWorkflowConnects != null)
        {
            for (int i = 0; i < collTWorkflowConnects.size(); i++)
            {
                ((TWorkflowConnect) collTWorkflowConnects.get(i))
                        .setProjectType(v);
            }
        }

        // update associated TPRole
        if (collTPRoles != null)
        {
            for (int i = 0; i < collTPRoles.size(); i++)
            {
                ((TPRole) collTPRoles.get(i))
                        .setProjectType(v);
            }
        }

        // update associated TChildProjectType
        if (collTChildProjectTypesRelatedByProjectTypeParent != null)
        {
            for (int i = 0; i < collTChildProjectTypesRelatedByProjectTypeParent.size(); i++)
            {
                ((TChildProjectType) collTChildProjectTypesRelatedByProjectTypeParent.get(i))
                        .setProjectTypeParent(v);
            }
        }

        // update associated TChildProjectType
        if (collTChildProjectTypesRelatedByProjectTypeChild != null)
        {
            for (int i = 0; i < collTChildProjectTypesRelatedByProjectTypeChild.size(); i++)
            {
                ((TChildProjectType) collTChildProjectTypesRelatedByProjectTypeChild.get(i))
                        .setProjectTypeChild(v);
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
     * Get the NotifyOwnerLevel
     *
     * @return Integer
     */
    public Integer getNotifyOwnerLevel()
    {
        return notifyOwnerLevel;
    }


    /**
     * Set the value of NotifyOwnerLevel
     *
     * @param v new value
     */
    public void setNotifyOwnerLevel(Integer v) 
    {

        if (!ObjectUtils.equals(this.notifyOwnerLevel, v))
        {
            this.notifyOwnerLevel = v;
            setModified(true);
        }


    }

    /**
     * Get the NotifyManagerLevel
     *
     * @return Integer
     */
    public Integer getNotifyManagerLevel()
    {
        return notifyManagerLevel;
    }


    /**
     * Set the value of NotifyManagerLevel
     *
     * @param v new value
     */
    public void setNotifyManagerLevel(Integer v) 
    {

        if (!ObjectUtils.equals(this.notifyManagerLevel, v))
        {
            this.notifyManagerLevel = v;
            setModified(true);
        }


    }

    /**
     * Get the HoursPerWorkDay
     *
     * @return Double
     */
    public Double getHoursPerWorkDay()
    {
        return hoursPerWorkDay;
    }


    /**
     * Set the value of HoursPerWorkDay
     *
     * @param v new value
     */
    public void setHoursPerWorkDay(Double v) 
    {

        if (!ObjectUtils.equals(this.hoursPerWorkDay, v))
        {
            this.hoursPerWorkDay = v;
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
     * Get the DefaultForPrivate
     *
     * @return String
     */
    public String getDefaultForPrivate()
    {
        return defaultForPrivate;
    }


    /**
     * Set the value of DefaultForPrivate
     *
     * @param v new value
     */
    public void setDefaultForPrivate(String v) 
    {

        if (!ObjectUtils.equals(this.defaultForPrivate, v))
        {
            this.defaultForPrivate = v;
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
        l.setTProjectType((TProjectType) this);
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
        l.setTProjectType((TProjectType) this);
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
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TPprioritys from storage.
     * If this TProjectType is new, it will return
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
                criteria.add(TPpriorityPeer.PROJECTTYPE, getObjectID() );
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
                criteria.add(TPpriorityPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TPprioritys from storage.
     * If this TProjectType is new, it will return
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
                 criteria.add(TPpriorityPeer.PROJECTTYPE, getObjectID());
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
                 criteria.add(TPpriorityPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TPprioritys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TPpriorityPeer.PROJECTTYPE, getObjectID());
                collTPprioritys = TPpriorityPeer.doSelectJoinTPriority(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPpriorityPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TPprioritys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TPpriorityPeer.PROJECTTYPE, getObjectID());
                collTPprioritys = TPpriorityPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPpriorityPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TPprioritys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TPpriorityPeer.PROJECTTYPE, getObjectID());
                collTPprioritys = TPpriorityPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPpriorityPeer.PROJECTTYPE, getObjectID());
            if (!lastTPprioritysCriteria.equals(criteria))
            {
                collTPprioritys = TPpriorityPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTPprioritysCriteria = criteria;

        return collTPprioritys;
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
        l.setTProjectType((TProjectType) this);
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
        l.setTProjectType((TProjectType) this);
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
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TProjects from storage.
     * If this TProjectType is new, it will return
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
                criteria.add(TProjectPeer.PROJECTTYPE, getObjectID() );
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
                criteria.add(TProjectPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TProjects from storage.
     * If this TProjectType is new, it will return
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
                 criteria.add(TProjectPeer.PROJECTTYPE, getObjectID());
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
                 criteria.add(TProjectPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TProjectPeer.PROJECTTYPE, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTPersonRelatedByDefaultOwnerID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TProjectPeer.PROJECTTYPE, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTPersonRelatedByDefaultManagerID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TProjectPeer.PROJECTTYPE, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TProjectPeer.PROJECTTYPE, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TProjectPeer.PROJECTTYPE, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTSystemState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TProjectPeer.PROJECTTYPE, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTDomain(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.PROJECTTYPE, getObjectID());
            if (!lastTProjectsCriteria.equals(criteria))
            {
                collTProjects = TProjectPeer.doSelectJoinTDomain(criteria);
            }
        }
        lastTProjectsCriteria = criteria;

        return collTProjects;
    }





    /**
     * Collection to store aggregation of collTRoles
     */
    protected List<TRole> collTRoles;

    /**
     * Temporary storage of collTRoles to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTRoles()
    {
        if (collTRoles == null)
        {
            collTRoles = new ArrayList<TRole>();
        }
    }


    /**
     * Method called to associate a TRole object to this object
     * through the TRole foreign key attribute
     *
     * @param l TRole
     * @throws TorqueException
     */
    public void addTRole(TRole l) throws TorqueException
    {
        getTRoles().add(l);
        l.setTProjectType((TProjectType) this);
    }

    /**
     * Method called to associate a TRole object to this object
     * through the TRole foreign key attribute using connection.
     *
     * @param l TRole
     * @throws TorqueException
     */
    public void addTRole(TRole l, Connection con) throws TorqueException
    {
        getTRoles(con).add(l);
        l.setTProjectType((TProjectType) this);
    }

    /**
     * The criteria used to select the current contents of collTRoles
     */
    private Criteria lastTRolesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTRoles(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TRole> getTRoles()
        throws TorqueException
    {
        if (collTRoles == null)
        {
            collTRoles = getTRoles(new Criteria(10));
        }
        return collTRoles;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TRoles from storage.
     * If this TProjectType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TRole> getTRoles(Criteria criteria) throws TorqueException
    {
        if (collTRoles == null)
        {
            if (isNew())
            {
               collTRoles = new ArrayList<TRole>();
            }
            else
            {
                criteria.add(TRolePeer.PROJECTTYPE, getObjectID() );
                collTRoles = TRolePeer.doSelect(criteria);
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
                criteria.add(TRolePeer.PROJECTTYPE, getObjectID());
                if (!lastTRolesCriteria.equals(criteria))
                {
                    collTRoles = TRolePeer.doSelect(criteria);
                }
            }
        }
        lastTRolesCriteria = criteria;

        return collTRoles;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTRoles(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TRole> getTRoles(Connection con) throws TorqueException
    {
        if (collTRoles == null)
        {
            collTRoles = getTRoles(new Criteria(10), con);
        }
        return collTRoles;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TRoles from storage.
     * If this TProjectType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TRole> getTRoles(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTRoles == null)
        {
            if (isNew())
            {
               collTRoles = new ArrayList<TRole>();
            }
            else
            {
                 criteria.add(TRolePeer.PROJECTTYPE, getObjectID());
                 collTRoles = TRolePeer.doSelect(criteria, con);
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
                 criteria.add(TRolePeer.PROJECTTYPE, getObjectID());
                 if (!lastTRolesCriteria.equals(criteria))
                 {
                     collTRoles = TRolePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTRolesCriteria = criteria;

         return collTRoles;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TRoles from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TRole> getTRolesJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTRoles == null)
        {
            if (isNew())
            {
               collTRoles = new ArrayList<TRole>();
            }
            else
            {
                criteria.add(TRolePeer.PROJECTTYPE, getObjectID());
                collTRoles = TRolePeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TRolePeer.PROJECTTYPE, getObjectID());
            if (!lastTRolesCriteria.equals(criteria))
            {
                collTRoles = TRolePeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTRolesCriteria = criteria;

        return collTRoles;
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
        l.setTProjectType((TProjectType) this);
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
        l.setTProjectType((TProjectType) this);
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
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TPseveritys from storage.
     * If this TProjectType is new, it will return
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
                criteria.add(TPseverityPeer.PROJECTTYPE, getObjectID() );
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
                criteria.add(TPseverityPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TPseveritys from storage.
     * If this TProjectType is new, it will return
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
                 criteria.add(TPseverityPeer.PROJECTTYPE, getObjectID());
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
                 criteria.add(TPseverityPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TPseveritys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TPseverityPeer.PROJECTTYPE, getObjectID());
                collTPseveritys = TPseverityPeer.doSelectJoinTSeverity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPseverityPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TPseveritys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TPseverityPeer.PROJECTTYPE, getObjectID());
                collTPseveritys = TPseverityPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPseverityPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TPseveritys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TPseverityPeer.PROJECTTYPE, getObjectID());
                collTPseveritys = TPseverityPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPseverityPeer.PROJECTTYPE, getObjectID());
            if (!lastTPseveritysCriteria.equals(criteria))
            {
                collTPseveritys = TPseverityPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTPseveritysCriteria = criteria;

        return collTPseveritys;
    }





    /**
     * Collection to store aggregation of collTDocStates
     */
    protected List<TDocState> collTDocStates;

    /**
     * Temporary storage of collTDocStates to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTDocStates()
    {
        if (collTDocStates == null)
        {
            collTDocStates = new ArrayList<TDocState>();
        }
    }


    /**
     * Method called to associate a TDocState object to this object
     * through the TDocState foreign key attribute
     *
     * @param l TDocState
     * @throws TorqueException
     */
    public void addTDocState(TDocState l) throws TorqueException
    {
        getTDocStates().add(l);
        l.setTProjectType((TProjectType) this);
    }

    /**
     * Method called to associate a TDocState object to this object
     * through the TDocState foreign key attribute using connection.
     *
     * @param l TDocState
     * @throws TorqueException
     */
    public void addTDocState(TDocState l, Connection con) throws TorqueException
    {
        getTDocStates(con).add(l);
        l.setTProjectType((TProjectType) this);
    }

    /**
     * The criteria used to select the current contents of collTDocStates
     */
    private Criteria lastTDocStatesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTDocStates(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TDocState> getTDocStates()
        throws TorqueException
    {
        if (collTDocStates == null)
        {
            collTDocStates = getTDocStates(new Criteria(10));
        }
        return collTDocStates;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TDocStates from storage.
     * If this TProjectType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TDocState> getTDocStates(Criteria criteria) throws TorqueException
    {
        if (collTDocStates == null)
        {
            if (isNew())
            {
               collTDocStates = new ArrayList<TDocState>();
            }
            else
            {
                criteria.add(TDocStatePeer.PROJECTTYPE, getObjectID() );
                collTDocStates = TDocStatePeer.doSelect(criteria);
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
                criteria.add(TDocStatePeer.PROJECTTYPE, getObjectID());
                if (!lastTDocStatesCriteria.equals(criteria))
                {
                    collTDocStates = TDocStatePeer.doSelect(criteria);
                }
            }
        }
        lastTDocStatesCriteria = criteria;

        return collTDocStates;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTDocStates(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TDocState> getTDocStates(Connection con) throws TorqueException
    {
        if (collTDocStates == null)
        {
            collTDocStates = getTDocStates(new Criteria(10), con);
        }
        return collTDocStates;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TDocStates from storage.
     * If this TProjectType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TDocState> getTDocStates(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTDocStates == null)
        {
            if (isNew())
            {
               collTDocStates = new ArrayList<TDocState>();
            }
            else
            {
                 criteria.add(TDocStatePeer.PROJECTTYPE, getObjectID());
                 collTDocStates = TDocStatePeer.doSelect(criteria, con);
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
                 criteria.add(TDocStatePeer.PROJECTTYPE, getObjectID());
                 if (!lastTDocStatesCriteria.equals(criteria))
                 {
                     collTDocStates = TDocStatePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTDocStatesCriteria = criteria;

         return collTDocStates;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TDocStates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TDocState> getTDocStatesJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTDocStates == null)
        {
            if (isNew())
            {
               collTDocStates = new ArrayList<TDocState>();
            }
            else
            {
                criteria.add(TDocStatePeer.PROJECTTYPE, getObjectID());
                collTDocStates = TDocStatePeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TDocStatePeer.PROJECTTYPE, getObjectID());
            if (!lastTDocStatesCriteria.equals(criteria))
            {
                collTDocStates = TDocStatePeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTDocStatesCriteria = criteria;

        return collTDocStates;
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
        l.setTProjectType((TProjectType) this);
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
        l.setTProjectType((TProjectType) this);
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
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TDisableFields from storage.
     * If this TProjectType is new, it will return
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
                criteria.add(TDisableFieldPeer.PROJECTTYPE, getObjectID() );
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
                criteria.add(TDisableFieldPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TDisableFields from storage.
     * If this TProjectType is new, it will return
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
                 criteria.add(TDisableFieldPeer.PROJECTTYPE, getObjectID());
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
                 criteria.add(TDisableFieldPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TDisableFields from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TDisableFieldPeer.PROJECTTYPE, getObjectID());
                collTDisableFields = TDisableFieldPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TDisableFieldPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TDisableFields from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TDisableFieldPeer.PROJECTTYPE, getObjectID());
                collTDisableFields = TDisableFieldPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TDisableFieldPeer.PROJECTTYPE, getObjectID());
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
        l.setTProjectType((TProjectType) this);
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
        l.setTProjectType((TProjectType) this);
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
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TPlistTypes from storage.
     * If this TProjectType is new, it will return
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
                criteria.add(TPlistTypePeer.PROJECTTYPE, getObjectID() );
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
                criteria.add(TPlistTypePeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TPlistTypes from storage.
     * If this TProjectType is new, it will return
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
                 criteria.add(TPlistTypePeer.PROJECTTYPE, getObjectID());
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
                 criteria.add(TPlistTypePeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TPlistTypes from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TPlistTypePeer.PROJECTTYPE, getObjectID());
                collTPlistTypes = TPlistTypePeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPlistTypePeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TPlistTypes from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TPlistTypePeer.PROJECTTYPE, getObjectID());
                collTPlistTypes = TPlistTypePeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPlistTypePeer.PROJECTTYPE, getObjectID());
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
        l.setTProjectType((TProjectType) this);
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
        l.setTProjectType((TProjectType) this);
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
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TPstates from storage.
     * If this TProjectType is new, it will return
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
                criteria.add(TPstatePeer.PROJECTTYPE, getObjectID() );
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
                criteria.add(TPstatePeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TPstates from storage.
     * If this TProjectType is new, it will return
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
                 criteria.add(TPstatePeer.PROJECTTYPE, getObjectID());
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
                 criteria.add(TPstatePeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TPstates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TPstatePeer.PROJECTTYPE, getObjectID());
                collTPstates = TPstatePeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPstatePeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TPstates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TPstatePeer.PROJECTTYPE, getObjectID());
                collTPstates = TPstatePeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPstatePeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TPstates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TPstatePeer.PROJECTTYPE, getObjectID());
                collTPstates = TPstatePeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPstatePeer.PROJECTTYPE, getObjectID());
            if (!lastTPstatesCriteria.equals(criteria))
            {
                collTPstates = TPstatePeer.doSelectJoinTListType(criteria);
            }
        }
        lastTPstatesCriteria = criteria;

        return collTPstates;
    }





    /**
     * Collection to store aggregation of collTWorkFlows
     */
    protected List<TWorkFlow> collTWorkFlows;

    /**
     * Temporary storage of collTWorkFlows to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkFlows()
    {
        if (collTWorkFlows == null)
        {
            collTWorkFlows = new ArrayList<TWorkFlow>();
        }
    }


    /**
     * Method called to associate a TWorkFlow object to this object
     * through the TWorkFlow foreign key attribute
     *
     * @param l TWorkFlow
     * @throws TorqueException
     */
    public void addTWorkFlow(TWorkFlow l) throws TorqueException
    {
        getTWorkFlows().add(l);
        l.setTProjectType((TProjectType) this);
    }

    /**
     * Method called to associate a TWorkFlow object to this object
     * through the TWorkFlow foreign key attribute using connection.
     *
     * @param l TWorkFlow
     * @throws TorqueException
     */
    public void addTWorkFlow(TWorkFlow l, Connection con) throws TorqueException
    {
        getTWorkFlows(con).add(l);
        l.setTProjectType((TProjectType) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkFlows
     */
    private Criteria lastTWorkFlowsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkFlows(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkFlow> getTWorkFlows()
        throws TorqueException
    {
        if (collTWorkFlows == null)
        {
            collTWorkFlows = getTWorkFlows(new Criteria(10));
        }
        return collTWorkFlows;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TWorkFlows from storage.
     * If this TProjectType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkFlow> getTWorkFlows(Criteria criteria) throws TorqueException
    {
        if (collTWorkFlows == null)
        {
            if (isNew())
            {
               collTWorkFlows = new ArrayList<TWorkFlow>();
            }
            else
            {
                criteria.add(TWorkFlowPeer.PROJECTTYPE, getObjectID() );
                collTWorkFlows = TWorkFlowPeer.doSelect(criteria);
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
                criteria.add(TWorkFlowPeer.PROJECTTYPE, getObjectID());
                if (!lastTWorkFlowsCriteria.equals(criteria))
                {
                    collTWorkFlows = TWorkFlowPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkFlowsCriteria = criteria;

        return collTWorkFlows;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkFlows(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkFlow> getTWorkFlows(Connection con) throws TorqueException
    {
        if (collTWorkFlows == null)
        {
            collTWorkFlows = getTWorkFlows(new Criteria(10), con);
        }
        return collTWorkFlows;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TWorkFlows from storage.
     * If this TProjectType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkFlow> getTWorkFlows(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkFlows == null)
        {
            if (isNew())
            {
               collTWorkFlows = new ArrayList<TWorkFlow>();
            }
            else
            {
                 criteria.add(TWorkFlowPeer.PROJECTTYPE, getObjectID());
                 collTWorkFlows = TWorkFlowPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkFlowPeer.PROJECTTYPE, getObjectID());
                 if (!lastTWorkFlowsCriteria.equals(criteria))
                 {
                     collTWorkFlows = TWorkFlowPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkFlowsCriteria = criteria;

         return collTWorkFlows;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TWorkFlows from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TWorkFlow> getTWorkFlowsJoinTStateRelatedByStateFrom(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkFlows == null)
        {
            if (isNew())
            {
               collTWorkFlows = new ArrayList<TWorkFlow>();
            }
            else
            {
                criteria.add(TWorkFlowPeer.PROJECTTYPE, getObjectID());
                collTWorkFlows = TWorkFlowPeer.doSelectJoinTStateRelatedByStateFrom(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkFlowPeer.PROJECTTYPE, getObjectID());
            if (!lastTWorkFlowsCriteria.equals(criteria))
            {
                collTWorkFlows = TWorkFlowPeer.doSelectJoinTStateRelatedByStateFrom(criteria);
            }
        }
        lastTWorkFlowsCriteria = criteria;

        return collTWorkFlows;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TWorkFlows from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TWorkFlow> getTWorkFlowsJoinTStateRelatedByStateTo(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkFlows == null)
        {
            if (isNew())
            {
               collTWorkFlows = new ArrayList<TWorkFlow>();
            }
            else
            {
                criteria.add(TWorkFlowPeer.PROJECTTYPE, getObjectID());
                collTWorkFlows = TWorkFlowPeer.doSelectJoinTStateRelatedByStateTo(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkFlowPeer.PROJECTTYPE, getObjectID());
            if (!lastTWorkFlowsCriteria.equals(criteria))
            {
                collTWorkFlows = TWorkFlowPeer.doSelectJoinTStateRelatedByStateTo(criteria);
            }
        }
        lastTWorkFlowsCriteria = criteria;

        return collTWorkFlows;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TWorkFlows from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TWorkFlow> getTWorkFlowsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkFlows == null)
        {
            if (isNew())
            {
               collTWorkFlows = new ArrayList<TWorkFlow>();
            }
            else
            {
                criteria.add(TWorkFlowPeer.PROJECTTYPE, getObjectID());
                collTWorkFlows = TWorkFlowPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkFlowPeer.PROJECTTYPE, getObjectID());
            if (!lastTWorkFlowsCriteria.equals(criteria))
            {
                collTWorkFlows = TWorkFlowPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTWorkFlowsCriteria = criteria;

        return collTWorkFlows;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TWorkFlows from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TWorkFlow> getTWorkFlowsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkFlows == null)
        {
            if (isNew())
            {
               collTWorkFlows = new ArrayList<TWorkFlow>();
            }
            else
            {
                criteria.add(TWorkFlowPeer.PROJECTTYPE, getObjectID());
                collTWorkFlows = TWorkFlowPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkFlowPeer.PROJECTTYPE, getObjectID());
            if (!lastTWorkFlowsCriteria.equals(criteria))
            {
                collTWorkFlows = TWorkFlowPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTWorkFlowsCriteria = criteria;

        return collTWorkFlows;
    }





    /**
     * Collection to store aggregation of collTReportLayouts
     */
    protected List<TReportLayout> collTReportLayouts;

    /**
     * Temporary storage of collTReportLayouts to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTReportLayouts()
    {
        if (collTReportLayouts == null)
        {
            collTReportLayouts = new ArrayList<TReportLayout>();
        }
    }


    /**
     * Method called to associate a TReportLayout object to this object
     * through the TReportLayout foreign key attribute
     *
     * @param l TReportLayout
     * @throws TorqueException
     */
    public void addTReportLayout(TReportLayout l) throws TorqueException
    {
        getTReportLayouts().add(l);
        l.setTProjectType((TProjectType) this);
    }

    /**
     * Method called to associate a TReportLayout object to this object
     * through the TReportLayout foreign key attribute using connection.
     *
     * @param l TReportLayout
     * @throws TorqueException
     */
    public void addTReportLayout(TReportLayout l, Connection con) throws TorqueException
    {
        getTReportLayouts(con).add(l);
        l.setTProjectType((TProjectType) this);
    }

    /**
     * The criteria used to select the current contents of collTReportLayouts
     */
    private Criteria lastTReportLayoutsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTReportLayouts(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TReportLayout> getTReportLayouts()
        throws TorqueException
    {
        if (collTReportLayouts == null)
        {
            collTReportLayouts = getTReportLayouts(new Criteria(10));
        }
        return collTReportLayouts;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TReportLayouts from storage.
     * If this TProjectType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TReportLayout> getTReportLayouts(Criteria criteria) throws TorqueException
    {
        if (collTReportLayouts == null)
        {
            if (isNew())
            {
               collTReportLayouts = new ArrayList<TReportLayout>();
            }
            else
            {
                criteria.add(TReportLayoutPeer.PROJECTTYPE, getObjectID() );
                collTReportLayouts = TReportLayoutPeer.doSelect(criteria);
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
                criteria.add(TReportLayoutPeer.PROJECTTYPE, getObjectID());
                if (!lastTReportLayoutsCriteria.equals(criteria))
                {
                    collTReportLayouts = TReportLayoutPeer.doSelect(criteria);
                }
            }
        }
        lastTReportLayoutsCriteria = criteria;

        return collTReportLayouts;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTReportLayouts(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TReportLayout> getTReportLayouts(Connection con) throws TorqueException
    {
        if (collTReportLayouts == null)
        {
            collTReportLayouts = getTReportLayouts(new Criteria(10), con);
        }
        return collTReportLayouts;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TReportLayouts from storage.
     * If this TProjectType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TReportLayout> getTReportLayouts(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTReportLayouts == null)
        {
            if (isNew())
            {
               collTReportLayouts = new ArrayList<TReportLayout>();
            }
            else
            {
                 criteria.add(TReportLayoutPeer.PROJECTTYPE, getObjectID());
                 collTReportLayouts = TReportLayoutPeer.doSelect(criteria, con);
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
                 criteria.add(TReportLayoutPeer.PROJECTTYPE, getObjectID());
                 if (!lastTReportLayoutsCriteria.equals(criteria))
                 {
                     collTReportLayouts = TReportLayoutPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTReportLayoutsCriteria = criteria;

         return collTReportLayouts;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TReportLayouts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TReportLayout> getTReportLayoutsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTReportLayouts == null)
        {
            if (isNew())
            {
               collTReportLayouts = new ArrayList<TReportLayout>();
            }
            else
            {
                criteria.add(TReportLayoutPeer.PROJECTTYPE, getObjectID());
                collTReportLayouts = TReportLayoutPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReportLayoutPeer.PROJECTTYPE, getObjectID());
            if (!lastTReportLayoutsCriteria.equals(criteria))
            {
                collTReportLayouts = TReportLayoutPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTReportLayoutsCriteria = criteria;

        return collTReportLayouts;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TReportLayouts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TReportLayout> getTReportLayoutsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTReportLayouts == null)
        {
            if (isNew())
            {
               collTReportLayouts = new ArrayList<TReportLayout>();
            }
            else
            {
                criteria.add(TReportLayoutPeer.PROJECTTYPE, getObjectID());
                collTReportLayouts = TReportLayoutPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReportLayoutPeer.PROJECTTYPE, getObjectID());
            if (!lastTReportLayoutsCriteria.equals(criteria))
            {
                collTReportLayouts = TReportLayoutPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTReportLayoutsCriteria = criteria;

        return collTReportLayouts;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TReportLayouts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TReportLayout> getTReportLayoutsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTReportLayouts == null)
        {
            if (isNew())
            {
               collTReportLayouts = new ArrayList<TReportLayout>();
            }
            else
            {
                criteria.add(TReportLayoutPeer.PROJECTTYPE, getObjectID());
                collTReportLayouts = TReportLayoutPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReportLayoutPeer.PROJECTTYPE, getObjectID());
            if (!lastTReportLayoutsCriteria.equals(criteria))
            {
                collTReportLayouts = TReportLayoutPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTReportLayoutsCriteria = criteria;

        return collTReportLayouts;
    }





    /**
     * Collection to store aggregation of collTFields
     */
    protected List<TField> collTFields;

    /**
     * Temporary storage of collTFields to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTFields()
    {
        if (collTFields == null)
        {
            collTFields = new ArrayList<TField>();
        }
    }


    /**
     * Method called to associate a TField object to this object
     * through the TField foreign key attribute
     *
     * @param l TField
     * @throws TorqueException
     */
    public void addTField(TField l) throws TorqueException
    {
        getTFields().add(l);
        l.setTProjectType((TProjectType) this);
    }

    /**
     * Method called to associate a TField object to this object
     * through the TField foreign key attribute using connection.
     *
     * @param l TField
     * @throws TorqueException
     */
    public void addTField(TField l, Connection con) throws TorqueException
    {
        getTFields(con).add(l);
        l.setTProjectType((TProjectType) this);
    }

    /**
     * The criteria used to select the current contents of collTFields
     */
    private Criteria lastTFieldsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTFields(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TField> getTFields()
        throws TorqueException
    {
        if (collTFields == null)
        {
            collTFields = getTFields(new Criteria(10));
        }
        return collTFields;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TFields from storage.
     * If this TProjectType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TField> getTFields(Criteria criteria) throws TorqueException
    {
        if (collTFields == null)
        {
            if (isNew())
            {
               collTFields = new ArrayList<TField>();
            }
            else
            {
                criteria.add(TFieldPeer.PROJECTTYPE, getObjectID() );
                collTFields = TFieldPeer.doSelect(criteria);
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
                criteria.add(TFieldPeer.PROJECTTYPE, getObjectID());
                if (!lastTFieldsCriteria.equals(criteria))
                {
                    collTFields = TFieldPeer.doSelect(criteria);
                }
            }
        }
        lastTFieldsCriteria = criteria;

        return collTFields;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTFields(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TField> getTFields(Connection con) throws TorqueException
    {
        if (collTFields == null)
        {
            collTFields = getTFields(new Criteria(10), con);
        }
        return collTFields;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TFields from storage.
     * If this TProjectType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TField> getTFields(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTFields == null)
        {
            if (isNew())
            {
               collTFields = new ArrayList<TField>();
            }
            else
            {
                 criteria.add(TFieldPeer.PROJECTTYPE, getObjectID());
                 collTFields = TFieldPeer.doSelect(criteria, con);
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
                 criteria.add(TFieldPeer.PROJECTTYPE, getObjectID());
                 if (!lastTFieldsCriteria.equals(criteria))
                 {
                     collTFields = TFieldPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTFieldsCriteria = criteria;

         return collTFields;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TFields from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TField> getTFieldsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTFields == null)
        {
            if (isNew())
            {
               collTFields = new ArrayList<TField>();
            }
            else
            {
                criteria.add(TFieldPeer.PROJECTTYPE, getObjectID());
                collTFields = TFieldPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldPeer.PROJECTTYPE, getObjectID());
            if (!lastTFieldsCriteria.equals(criteria))
            {
                collTFields = TFieldPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTFieldsCriteria = criteria;

        return collTFields;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TFields from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TField> getTFieldsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTFields == null)
        {
            if (isNew())
            {
               collTFields = new ArrayList<TField>();
            }
            else
            {
                criteria.add(TFieldPeer.PROJECTTYPE, getObjectID());
                collTFields = TFieldPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldPeer.PROJECTTYPE, getObjectID());
            if (!lastTFieldsCriteria.equals(criteria))
            {
                collTFields = TFieldPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTFieldsCriteria = criteria;

        return collTFields;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TFields from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TField> getTFieldsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTFields == null)
        {
            if (isNew())
            {
               collTFields = new ArrayList<TField>();
            }
            else
            {
                criteria.add(TFieldPeer.PROJECTTYPE, getObjectID());
                collTFields = TFieldPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldPeer.PROJECTTYPE, getObjectID());
            if (!lastTFieldsCriteria.equals(criteria))
            {
                collTFields = TFieldPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTFieldsCriteria = criteria;

        return collTFields;
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
        l.setTProjectType((TProjectType) this);
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
        l.setTProjectType((TProjectType) this);
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
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     * If this TProjectType is new, it will return
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
                criteria.add(TFieldConfigPeer.PROJECTTYPE, getObjectID() );
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
                criteria.add(TFieldConfigPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     * If this TProjectType is new, it will return
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
                 criteria.add(TFieldConfigPeer.PROJECTTYPE, getObjectID());
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
                 criteria.add(TFieldConfigPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TFieldConfigPeer.PROJECTTYPE, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTField(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TFieldConfigPeer.PROJECTTYPE, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TFieldConfigPeer.PROJECTTYPE, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TFieldConfigPeer.PROJECTTYPE, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TFieldConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TFieldConfigPeer.PROJECTTYPE, getObjectID());
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTScripts(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldConfigPeer.PROJECTTYPE, getObjectID());
            if (!lastTFieldConfigsCriteria.equals(criteria))
            {
                collTFieldConfigs = TFieldConfigPeer.doSelectJoinTScripts(criteria);
            }
        }
        lastTFieldConfigsCriteria = criteria;

        return collTFieldConfigs;
    }





    /**
     * Collection to store aggregation of collTScreens
     */
    protected List<TScreen> collTScreens;

    /**
     * Temporary storage of collTScreens to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTScreens()
    {
        if (collTScreens == null)
        {
            collTScreens = new ArrayList<TScreen>();
        }
    }


    /**
     * Method called to associate a TScreen object to this object
     * through the TScreen foreign key attribute
     *
     * @param l TScreen
     * @throws TorqueException
     */
    public void addTScreen(TScreen l) throws TorqueException
    {
        getTScreens().add(l);
        l.setTProjectType((TProjectType) this);
    }

    /**
     * Method called to associate a TScreen object to this object
     * through the TScreen foreign key attribute using connection.
     *
     * @param l TScreen
     * @throws TorqueException
     */
    public void addTScreen(TScreen l, Connection con) throws TorqueException
    {
        getTScreens(con).add(l);
        l.setTProjectType((TProjectType) this);
    }

    /**
     * The criteria used to select the current contents of collTScreens
     */
    private Criteria lastTScreensCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScreens(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TScreen> getTScreens()
        throws TorqueException
    {
        if (collTScreens == null)
        {
            collTScreens = getTScreens(new Criteria(10));
        }
        return collTScreens;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TScreens from storage.
     * If this TProjectType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TScreen> getTScreens(Criteria criteria) throws TorqueException
    {
        if (collTScreens == null)
        {
            if (isNew())
            {
               collTScreens = new ArrayList<TScreen>();
            }
            else
            {
                criteria.add(TScreenPeer.PROJECTTYPE, getObjectID() );
                collTScreens = TScreenPeer.doSelect(criteria);
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
                criteria.add(TScreenPeer.PROJECTTYPE, getObjectID());
                if (!lastTScreensCriteria.equals(criteria))
                {
                    collTScreens = TScreenPeer.doSelect(criteria);
                }
            }
        }
        lastTScreensCriteria = criteria;

        return collTScreens;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScreens(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScreen> getTScreens(Connection con) throws TorqueException
    {
        if (collTScreens == null)
        {
            collTScreens = getTScreens(new Criteria(10), con);
        }
        return collTScreens;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TScreens from storage.
     * If this TProjectType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScreen> getTScreens(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTScreens == null)
        {
            if (isNew())
            {
               collTScreens = new ArrayList<TScreen>();
            }
            else
            {
                 criteria.add(TScreenPeer.PROJECTTYPE, getObjectID());
                 collTScreens = TScreenPeer.doSelect(criteria, con);
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
                 criteria.add(TScreenPeer.PROJECTTYPE, getObjectID());
                 if (!lastTScreensCriteria.equals(criteria))
                 {
                     collTScreens = TScreenPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTScreensCriteria = criteria;

         return collTScreens;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TScreens from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TScreen> getTScreensJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTScreens == null)
        {
            if (isNew())
            {
               collTScreens = new ArrayList<TScreen>();
            }
            else
            {
                criteria.add(TScreenPeer.PROJECTTYPE, getObjectID());
                collTScreens = TScreenPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenPeer.PROJECTTYPE, getObjectID());
            if (!lastTScreensCriteria.equals(criteria))
            {
                collTScreens = TScreenPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTScreensCriteria = criteria;

        return collTScreens;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TScreens from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TScreen> getTScreensJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTScreens == null)
        {
            if (isNew())
            {
               collTScreens = new ArrayList<TScreen>();
            }
            else
            {
                criteria.add(TScreenPeer.PROJECTTYPE, getObjectID());
                collTScreens = TScreenPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenPeer.PROJECTTYPE, getObjectID());
            if (!lastTScreensCriteria.equals(criteria))
            {
                collTScreens = TScreenPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTScreensCriteria = criteria;

        return collTScreens;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TScreens from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TScreen> getTScreensJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTScreens == null)
        {
            if (isNew())
            {
               collTScreens = new ArrayList<TScreen>();
            }
            else
            {
                criteria.add(TScreenPeer.PROJECTTYPE, getObjectID());
                collTScreens = TScreenPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenPeer.PROJECTTYPE, getObjectID());
            if (!lastTScreensCriteria.equals(criteria))
            {
                collTScreens = TScreenPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTScreensCriteria = criteria;

        return collTScreens;
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
        l.setTProjectType((TProjectType) this);
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
        l.setTProjectType((TProjectType) this);
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
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     * If this TProjectType is new, it will return
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
                criteria.add(TScreenConfigPeer.PROJECTTYPE, getObjectID() );
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
                criteria.add(TScreenConfigPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     * If this TProjectType is new, it will return
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
                 criteria.add(TScreenConfigPeer.PROJECTTYPE, getObjectID());
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
                 criteria.add(TScreenConfigPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TScreenConfigPeer.PROJECTTYPE, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTScreen(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TScreenConfigPeer.PROJECTTYPE, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TScreenConfigPeer.PROJECTTYPE, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TScreenConfigPeer.PROJECTTYPE, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TScreenConfigPeer.PROJECTTYPE, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTAction(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.PROJECTTYPE, getObjectID());
            if (!lastTScreenConfigsCriteria.equals(criteria))
            {
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTAction(criteria);
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }





    /**
     * Collection to store aggregation of collTEvents
     */
    protected List<TEvent> collTEvents;

    /**
     * Temporary storage of collTEvents to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTEvents()
    {
        if (collTEvents == null)
        {
            collTEvents = new ArrayList<TEvent>();
        }
    }


    /**
     * Method called to associate a TEvent object to this object
     * through the TEvent foreign key attribute
     *
     * @param l TEvent
     * @throws TorqueException
     */
    public void addTEvent(TEvent l) throws TorqueException
    {
        getTEvents().add(l);
        l.setTProjectType((TProjectType) this);
    }

    /**
     * Method called to associate a TEvent object to this object
     * through the TEvent foreign key attribute using connection.
     *
     * @param l TEvent
     * @throws TorqueException
     */
    public void addTEvent(TEvent l, Connection con) throws TorqueException
    {
        getTEvents(con).add(l);
        l.setTProjectType((TProjectType) this);
    }

    /**
     * The criteria used to select the current contents of collTEvents
     */
    private Criteria lastTEventsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTEvents(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TEvent> getTEvents()
        throws TorqueException
    {
        if (collTEvents == null)
        {
            collTEvents = getTEvents(new Criteria(10));
        }
        return collTEvents;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TEvents from storage.
     * If this TProjectType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TEvent> getTEvents(Criteria criteria) throws TorqueException
    {
        if (collTEvents == null)
        {
            if (isNew())
            {
               collTEvents = new ArrayList<TEvent>();
            }
            else
            {
                criteria.add(TEventPeer.PROJECTTYPE, getObjectID() );
                collTEvents = TEventPeer.doSelect(criteria);
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
                criteria.add(TEventPeer.PROJECTTYPE, getObjectID());
                if (!lastTEventsCriteria.equals(criteria))
                {
                    collTEvents = TEventPeer.doSelect(criteria);
                }
            }
        }
        lastTEventsCriteria = criteria;

        return collTEvents;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTEvents(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TEvent> getTEvents(Connection con) throws TorqueException
    {
        if (collTEvents == null)
        {
            collTEvents = getTEvents(new Criteria(10), con);
        }
        return collTEvents;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TEvents from storage.
     * If this TProjectType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TEvent> getTEvents(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTEvents == null)
        {
            if (isNew())
            {
               collTEvents = new ArrayList<TEvent>();
            }
            else
            {
                 criteria.add(TEventPeer.PROJECTTYPE, getObjectID());
                 collTEvents = TEventPeer.doSelect(criteria, con);
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
                 criteria.add(TEventPeer.PROJECTTYPE, getObjectID());
                 if (!lastTEventsCriteria.equals(criteria))
                 {
                     collTEvents = TEventPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTEventsCriteria = criteria;

         return collTEvents;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TEvents from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TEvent> getTEventsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTEvents == null)
        {
            if (isNew())
            {
               collTEvents = new ArrayList<TEvent>();
            }
            else
            {
                criteria.add(TEventPeer.PROJECTTYPE, getObjectID());
                collTEvents = TEventPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TEventPeer.PROJECTTYPE, getObjectID());
            if (!lastTEventsCriteria.equals(criteria))
            {
                collTEvents = TEventPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTEventsCriteria = criteria;

        return collTEvents;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TEvents from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TEvent> getTEventsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTEvents == null)
        {
            if (isNew())
            {
               collTEvents = new ArrayList<TEvent>();
            }
            else
            {
                criteria.add(TEventPeer.PROJECTTYPE, getObjectID());
                collTEvents = TEventPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TEventPeer.PROJECTTYPE, getObjectID());
            if (!lastTEventsCriteria.equals(criteria))
            {
                collTEvents = TEventPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTEventsCriteria = criteria;

        return collTEvents;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TEvents from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TEvent> getTEventsJoinTCLOB(Criteria criteria)
        throws TorqueException
    {
        if (collTEvents == null)
        {
            if (isNew())
            {
               collTEvents = new ArrayList<TEvent>();
            }
            else
            {
                criteria.add(TEventPeer.PROJECTTYPE, getObjectID());
                collTEvents = TEventPeer.doSelectJoinTCLOB(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TEventPeer.PROJECTTYPE, getObjectID());
            if (!lastTEventsCriteria.equals(criteria))
            {
                collTEvents = TEventPeer.doSelectJoinTCLOB(criteria);
            }
        }
        lastTEventsCriteria = criteria;

        return collTEvents;
    }





    /**
     * Collection to store aggregation of collTScriptss
     */
    protected List<TScripts> collTScriptss;

    /**
     * Temporary storage of collTScriptss to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTScriptss()
    {
        if (collTScriptss == null)
        {
            collTScriptss = new ArrayList<TScripts>();
        }
    }


    /**
     * Method called to associate a TScripts object to this object
     * through the TScripts foreign key attribute
     *
     * @param l TScripts
     * @throws TorqueException
     */
    public void addTScripts(TScripts l) throws TorqueException
    {
        getTScriptss().add(l);
        l.setTProjectType((TProjectType) this);
    }

    /**
     * Method called to associate a TScripts object to this object
     * through the TScripts foreign key attribute using connection.
     *
     * @param l TScripts
     * @throws TorqueException
     */
    public void addTScripts(TScripts l, Connection con) throws TorqueException
    {
        getTScriptss(con).add(l);
        l.setTProjectType((TProjectType) this);
    }

    /**
     * The criteria used to select the current contents of collTScriptss
     */
    private Criteria lastTScriptssCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScriptss(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TScripts> getTScriptss()
        throws TorqueException
    {
        if (collTScriptss == null)
        {
            collTScriptss = getTScriptss(new Criteria(10));
        }
        return collTScriptss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TScriptss from storage.
     * If this TProjectType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TScripts> getTScriptss(Criteria criteria) throws TorqueException
    {
        if (collTScriptss == null)
        {
            if (isNew())
            {
               collTScriptss = new ArrayList<TScripts>();
            }
            else
            {
                criteria.add(TScriptsPeer.PROJECTTYPE, getObjectID() );
                collTScriptss = TScriptsPeer.doSelect(criteria);
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
                criteria.add(TScriptsPeer.PROJECTTYPE, getObjectID());
                if (!lastTScriptssCriteria.equals(criteria))
                {
                    collTScriptss = TScriptsPeer.doSelect(criteria);
                }
            }
        }
        lastTScriptssCriteria = criteria;

        return collTScriptss;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScriptss(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScripts> getTScriptss(Connection con) throws TorqueException
    {
        if (collTScriptss == null)
        {
            collTScriptss = getTScriptss(new Criteria(10), con);
        }
        return collTScriptss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TScriptss from storage.
     * If this TProjectType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScripts> getTScriptss(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTScriptss == null)
        {
            if (isNew())
            {
               collTScriptss = new ArrayList<TScripts>();
            }
            else
            {
                 criteria.add(TScriptsPeer.PROJECTTYPE, getObjectID());
                 collTScriptss = TScriptsPeer.doSelect(criteria, con);
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
                 criteria.add(TScriptsPeer.PROJECTTYPE, getObjectID());
                 if (!lastTScriptssCriteria.equals(criteria))
                 {
                     collTScriptss = TScriptsPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTScriptssCriteria = criteria;

         return collTScriptss;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TScriptss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TScripts> getTScriptssJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTScriptss == null)
        {
            if (isNew())
            {
               collTScriptss = new ArrayList<TScripts>();
            }
            else
            {
                criteria.add(TScriptsPeer.PROJECTTYPE, getObjectID());
                collTScriptss = TScriptsPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScriptsPeer.PROJECTTYPE, getObjectID());
            if (!lastTScriptssCriteria.equals(criteria))
            {
                collTScriptss = TScriptsPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTScriptssCriteria = criteria;

        return collTScriptss;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TScriptss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TScripts> getTScriptssJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTScriptss == null)
        {
            if (isNew())
            {
               collTScriptss = new ArrayList<TScripts>();
            }
            else
            {
                criteria.add(TScriptsPeer.PROJECTTYPE, getObjectID());
                collTScriptss = TScriptsPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScriptsPeer.PROJECTTYPE, getObjectID());
            if (!lastTScriptssCriteria.equals(criteria))
            {
                collTScriptss = TScriptsPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTScriptssCriteria = criteria;

        return collTScriptss;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TScriptss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TScripts> getTScriptssJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTScriptss == null)
        {
            if (isNew())
            {
               collTScriptss = new ArrayList<TScripts>();
            }
            else
            {
                criteria.add(TScriptsPeer.PROJECTTYPE, getObjectID());
                collTScriptss = TScriptsPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScriptsPeer.PROJECTTYPE, getObjectID());
            if (!lastTScriptssCriteria.equals(criteria))
            {
                collTScriptss = TScriptsPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTScriptssCriteria = criteria;

        return collTScriptss;
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
        l.setTProjectType((TProjectType) this);
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
        l.setTProjectType((TProjectType) this);
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
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     * If this TProjectType is new, it will return
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
                criteria.add(TMailTemplateConfigPeer.PROJECTTYPE, getObjectID() );
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
                criteria.add(TMailTemplateConfigPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     * If this TProjectType is new, it will return
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
                 criteria.add(TMailTemplateConfigPeer.PROJECTTYPE, getObjectID());
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
                 criteria.add(TMailTemplateConfigPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TMailTemplateConfigPeer.PROJECTTYPE, getObjectID());
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTMailTemplate(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TMailTemplateConfigPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TMailTemplateConfigPeer.PROJECTTYPE, getObjectID());
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TMailTemplateConfigPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TMailTemplateConfigPeer.PROJECTTYPE, getObjectID());
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TMailTemplateConfigPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TMailTemplateConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TMailTemplateConfigPeer.PROJECTTYPE, getObjectID());
                collTMailTemplateConfigs = TMailTemplateConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TMailTemplateConfigPeer.PROJECTTYPE, getObjectID());
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
        l.setTProjectType((TProjectType) this);
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
        l.setTProjectType((TProjectType) this);
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
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     * If this TProjectType is new, it will return
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
                criteria.add(TWfActivityContextParamsPeer.PROJECTTYPE, getObjectID() );
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
                criteria.add(TWfActivityContextParamsPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     * If this TProjectType is new, it will return
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
                 criteria.add(TWfActivityContextParamsPeer.PROJECTTYPE, getObjectID());
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
                 criteria.add(TWfActivityContextParamsPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TWfActivityContextParamsPeer.PROJECTTYPE, getObjectID());
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTWorkflowActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWfActivityContextParamsPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TWfActivityContextParamsPeer.PROJECTTYPE, getObjectID());
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWfActivityContextParamsPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TWfActivityContextParamsPeer.PROJECTTYPE, getObjectID());
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWfActivityContextParamsPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TWfActivityContextParamsPeer.PROJECTTYPE, getObjectID());
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWfActivityContextParamsPeer.PROJECTTYPE, getObjectID());
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
        l.setTProjectType((TProjectType) this);
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
        l.setTProjectType((TProjectType) this);
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
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     * If this TProjectType is new, it will return
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
                criteria.add(TWorkflowConnectPeer.PROJECTTYPE, getObjectID() );
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
                criteria.add(TWorkflowConnectPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     * If this TProjectType is new, it will return
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
                 criteria.add(TWorkflowConnectPeer.PROJECTTYPE, getObjectID());
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
                 criteria.add(TWorkflowConnectPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TWorkflowConnectPeer.PROJECTTYPE, getObjectID());
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTWorkflowDef(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowConnectPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TWorkflowConnectPeer.PROJECTTYPE, getObjectID());
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowConnectPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TWorkflowConnectPeer.PROJECTTYPE, getObjectID());
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowConnectPeer.PROJECTTYPE, getObjectID());
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
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
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
                criteria.add(TWorkflowConnectPeer.PROJECTTYPE, getObjectID());
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowConnectPeer.PROJECTTYPE, getObjectID());
            if (!lastTWorkflowConnectsCriteria.equals(criteria))
            {
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTWorkflowConnectsCriteria = criteria;

        return collTWorkflowConnects;
    }





    /**
     * Collection to store aggregation of collTPRoles
     */
    protected List<TPRole> collTPRoles;

    /**
     * Temporary storage of collTPRoles to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTPRoles()
    {
        if (collTPRoles == null)
        {
            collTPRoles = new ArrayList<TPRole>();
        }
    }


    /**
     * Method called to associate a TPRole object to this object
     * through the TPRole foreign key attribute
     *
     * @param l TPRole
     * @throws TorqueException
     */
    public void addTPRole(TPRole l) throws TorqueException
    {
        getTPRoles().add(l);
        l.setTProjectType((TProjectType) this);
    }

    /**
     * Method called to associate a TPRole object to this object
     * through the TPRole foreign key attribute using connection.
     *
     * @param l TPRole
     * @throws TorqueException
     */
    public void addTPRole(TPRole l, Connection con) throws TorqueException
    {
        getTPRoles(con).add(l);
        l.setTProjectType((TProjectType) this);
    }

    /**
     * The criteria used to select the current contents of collTPRoles
     */
    private Criteria lastTPRolesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPRoles(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TPRole> getTPRoles()
        throws TorqueException
    {
        if (collTPRoles == null)
        {
            collTPRoles = getTPRoles(new Criteria(10));
        }
        return collTPRoles;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TPRoles from storage.
     * If this TProjectType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TPRole> getTPRoles(Criteria criteria) throws TorqueException
    {
        if (collTPRoles == null)
        {
            if (isNew())
            {
               collTPRoles = new ArrayList<TPRole>();
            }
            else
            {
                criteria.add(TPRolePeer.PROJECTTYPE, getObjectID() );
                collTPRoles = TPRolePeer.doSelect(criteria);
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
                criteria.add(TPRolePeer.PROJECTTYPE, getObjectID());
                if (!lastTPRolesCriteria.equals(criteria))
                {
                    collTPRoles = TPRolePeer.doSelect(criteria);
                }
            }
        }
        lastTPRolesCriteria = criteria;

        return collTPRoles;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPRoles(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPRole> getTPRoles(Connection con) throws TorqueException
    {
        if (collTPRoles == null)
        {
            collTPRoles = getTPRoles(new Criteria(10), con);
        }
        return collTPRoles;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TPRoles from storage.
     * If this TProjectType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPRole> getTPRoles(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTPRoles == null)
        {
            if (isNew())
            {
               collTPRoles = new ArrayList<TPRole>();
            }
            else
            {
                 criteria.add(TPRolePeer.PROJECTTYPE, getObjectID());
                 collTPRoles = TPRolePeer.doSelect(criteria, con);
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
                 criteria.add(TPRolePeer.PROJECTTYPE, getObjectID());
                 if (!lastTPRolesCriteria.equals(criteria))
                 {
                     collTPRoles = TPRolePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTPRolesCriteria = criteria;

         return collTPRoles;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TPRoles from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TPRole> getTPRolesJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTPRoles == null)
        {
            if (isNew())
            {
               collTPRoles = new ArrayList<TPRole>();
            }
            else
            {
                criteria.add(TPRolePeer.PROJECTTYPE, getObjectID());
                collTPRoles = TPRolePeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPRolePeer.PROJECTTYPE, getObjectID());
            if (!lastTPRolesCriteria.equals(criteria))
            {
                collTPRoles = TPRolePeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTPRolesCriteria = criteria;

        return collTPRoles;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TPRoles from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TPRole> getTPRolesJoinTRole(Criteria criteria)
        throws TorqueException
    {
        if (collTPRoles == null)
        {
            if (isNew())
            {
               collTPRoles = new ArrayList<TPRole>();
            }
            else
            {
                criteria.add(TPRolePeer.PROJECTTYPE, getObjectID());
                collTPRoles = TPRolePeer.doSelectJoinTRole(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPRolePeer.PROJECTTYPE, getObjectID());
            if (!lastTPRolesCriteria.equals(criteria))
            {
                collTPRoles = TPRolePeer.doSelectJoinTRole(criteria);
            }
        }
        lastTPRolesCriteria = criteria;

        return collTPRoles;
    }





    /**
     * Collection to store aggregation of collTChildProjectTypesRelatedByProjectTypeParent
     */
    protected List<TChildProjectType> collTChildProjectTypesRelatedByProjectTypeParent;

    /**
     * Temporary storage of collTChildProjectTypesRelatedByProjectTypeParent to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTChildProjectTypesRelatedByProjectTypeParent()
    {
        if (collTChildProjectTypesRelatedByProjectTypeParent == null)
        {
            collTChildProjectTypesRelatedByProjectTypeParent = new ArrayList<TChildProjectType>();
        }
    }


    /**
     * Method called to associate a TChildProjectType object to this object
     * through the TChildProjectType foreign key attribute
     *
     * @param l TChildProjectType
     * @throws TorqueException
     */
    public void addTChildProjectTypeRelatedByProjectTypeParent(TChildProjectType l) throws TorqueException
    {
        getTChildProjectTypesRelatedByProjectTypeParent().add(l);
        l.setTProjectTypeRelatedByProjectTypeParent((TProjectType) this);
    }

    /**
     * Method called to associate a TChildProjectType object to this object
     * through the TChildProjectType foreign key attribute using connection.
     *
     * @param l TChildProjectType
     * @throws TorqueException
     */
    public void addTChildProjectTypeRelatedByProjectTypeParent(TChildProjectType l, Connection con) throws TorqueException
    {
        getTChildProjectTypesRelatedByProjectTypeParent(con).add(l);
        l.setTProjectTypeRelatedByProjectTypeParent((TProjectType) this);
    }

    /**
     * The criteria used to select the current contents of collTChildProjectTypesRelatedByProjectTypeParent
     */
    private Criteria lastTChildProjectTypesRelatedByProjectTypeParentCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTChildProjectTypesRelatedByProjectTypeParent(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TChildProjectType> getTChildProjectTypesRelatedByProjectTypeParent()
        throws TorqueException
    {
        if (collTChildProjectTypesRelatedByProjectTypeParent == null)
        {
            collTChildProjectTypesRelatedByProjectTypeParent = getTChildProjectTypesRelatedByProjectTypeParent(new Criteria(10));
        }
        return collTChildProjectTypesRelatedByProjectTypeParent;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TChildProjectTypesRelatedByProjectTypeParent from storage.
     * If this TProjectType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TChildProjectType> getTChildProjectTypesRelatedByProjectTypeParent(Criteria criteria) throws TorqueException
    {
        if (collTChildProjectTypesRelatedByProjectTypeParent == null)
        {
            if (isNew())
            {
               collTChildProjectTypesRelatedByProjectTypeParent = new ArrayList<TChildProjectType>();
            }
            else
            {
                criteria.add(TChildProjectTypePeer.PROJECTTYPEPARENT, getObjectID() );
                collTChildProjectTypesRelatedByProjectTypeParent = TChildProjectTypePeer.doSelect(criteria);
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
                criteria.add(TChildProjectTypePeer.PROJECTTYPEPARENT, getObjectID());
                if (!lastTChildProjectTypesRelatedByProjectTypeParentCriteria.equals(criteria))
                {
                    collTChildProjectTypesRelatedByProjectTypeParent = TChildProjectTypePeer.doSelect(criteria);
                }
            }
        }
        lastTChildProjectTypesRelatedByProjectTypeParentCriteria = criteria;

        return collTChildProjectTypesRelatedByProjectTypeParent;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTChildProjectTypesRelatedByProjectTypeParent(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TChildProjectType> getTChildProjectTypesRelatedByProjectTypeParent(Connection con) throws TorqueException
    {
        if (collTChildProjectTypesRelatedByProjectTypeParent == null)
        {
            collTChildProjectTypesRelatedByProjectTypeParent = getTChildProjectTypesRelatedByProjectTypeParent(new Criteria(10), con);
        }
        return collTChildProjectTypesRelatedByProjectTypeParent;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TChildProjectTypesRelatedByProjectTypeParent from storage.
     * If this TProjectType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TChildProjectType> getTChildProjectTypesRelatedByProjectTypeParent(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTChildProjectTypesRelatedByProjectTypeParent == null)
        {
            if (isNew())
            {
               collTChildProjectTypesRelatedByProjectTypeParent = new ArrayList<TChildProjectType>();
            }
            else
            {
                 criteria.add(TChildProjectTypePeer.PROJECTTYPEPARENT, getObjectID());
                 collTChildProjectTypesRelatedByProjectTypeParent = TChildProjectTypePeer.doSelect(criteria, con);
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
                 criteria.add(TChildProjectTypePeer.PROJECTTYPEPARENT, getObjectID());
                 if (!lastTChildProjectTypesRelatedByProjectTypeParentCriteria.equals(criteria))
                 {
                     collTChildProjectTypesRelatedByProjectTypeParent = TChildProjectTypePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTChildProjectTypesRelatedByProjectTypeParentCriteria = criteria;

         return collTChildProjectTypesRelatedByProjectTypeParent;
     }



















    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TChildProjectTypesRelatedByProjectTypeParent from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TChildProjectType> getTChildProjectTypesRelatedByProjectTypeParentJoinTProjectTypeRelatedByProjectTypeChild(Criteria criteria)
        throws TorqueException
    {
        if (collTChildProjectTypesRelatedByProjectTypeParent == null)
        {
            if (isNew())
            {
               collTChildProjectTypesRelatedByProjectTypeParent = new ArrayList<TChildProjectType>();
            }
            else
            {
                criteria.add(TChildProjectTypePeer.PROJECTTYPEPARENT, getObjectID());
                collTChildProjectTypesRelatedByProjectTypeParent = TChildProjectTypePeer.doSelectJoinTProjectTypeRelatedByProjectTypeChild(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TChildProjectTypePeer.PROJECTTYPEPARENT, getObjectID());
            if (!lastTChildProjectTypesRelatedByProjectTypeParentCriteria.equals(criteria))
            {
                collTChildProjectTypesRelatedByProjectTypeParent = TChildProjectTypePeer.doSelectJoinTProjectTypeRelatedByProjectTypeChild(criteria);
            }
        }
        lastTChildProjectTypesRelatedByProjectTypeParentCriteria = criteria;

        return collTChildProjectTypesRelatedByProjectTypeParent;
    }





    /**
     * Collection to store aggregation of collTChildProjectTypesRelatedByProjectTypeChild
     */
    protected List<TChildProjectType> collTChildProjectTypesRelatedByProjectTypeChild;

    /**
     * Temporary storage of collTChildProjectTypesRelatedByProjectTypeChild to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTChildProjectTypesRelatedByProjectTypeChild()
    {
        if (collTChildProjectTypesRelatedByProjectTypeChild == null)
        {
            collTChildProjectTypesRelatedByProjectTypeChild = new ArrayList<TChildProjectType>();
        }
    }


    /**
     * Method called to associate a TChildProjectType object to this object
     * through the TChildProjectType foreign key attribute
     *
     * @param l TChildProjectType
     * @throws TorqueException
     */
    public void addTChildProjectTypeRelatedByProjectTypeChild(TChildProjectType l) throws TorqueException
    {
        getTChildProjectTypesRelatedByProjectTypeChild().add(l);
        l.setTProjectTypeRelatedByProjectTypeChild((TProjectType) this);
    }

    /**
     * Method called to associate a TChildProjectType object to this object
     * through the TChildProjectType foreign key attribute using connection.
     *
     * @param l TChildProjectType
     * @throws TorqueException
     */
    public void addTChildProjectTypeRelatedByProjectTypeChild(TChildProjectType l, Connection con) throws TorqueException
    {
        getTChildProjectTypesRelatedByProjectTypeChild(con).add(l);
        l.setTProjectTypeRelatedByProjectTypeChild((TProjectType) this);
    }

    /**
     * The criteria used to select the current contents of collTChildProjectTypesRelatedByProjectTypeChild
     */
    private Criteria lastTChildProjectTypesRelatedByProjectTypeChildCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTChildProjectTypesRelatedByProjectTypeChild(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TChildProjectType> getTChildProjectTypesRelatedByProjectTypeChild()
        throws TorqueException
    {
        if (collTChildProjectTypesRelatedByProjectTypeChild == null)
        {
            collTChildProjectTypesRelatedByProjectTypeChild = getTChildProjectTypesRelatedByProjectTypeChild(new Criteria(10));
        }
        return collTChildProjectTypesRelatedByProjectTypeChild;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TChildProjectTypesRelatedByProjectTypeChild from storage.
     * If this TProjectType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TChildProjectType> getTChildProjectTypesRelatedByProjectTypeChild(Criteria criteria) throws TorqueException
    {
        if (collTChildProjectTypesRelatedByProjectTypeChild == null)
        {
            if (isNew())
            {
               collTChildProjectTypesRelatedByProjectTypeChild = new ArrayList<TChildProjectType>();
            }
            else
            {
                criteria.add(TChildProjectTypePeer.PROJECTTYPECHILD, getObjectID() );
                collTChildProjectTypesRelatedByProjectTypeChild = TChildProjectTypePeer.doSelect(criteria);
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
                criteria.add(TChildProjectTypePeer.PROJECTTYPECHILD, getObjectID());
                if (!lastTChildProjectTypesRelatedByProjectTypeChildCriteria.equals(criteria))
                {
                    collTChildProjectTypesRelatedByProjectTypeChild = TChildProjectTypePeer.doSelect(criteria);
                }
            }
        }
        lastTChildProjectTypesRelatedByProjectTypeChildCriteria = criteria;

        return collTChildProjectTypesRelatedByProjectTypeChild;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTChildProjectTypesRelatedByProjectTypeChild(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TChildProjectType> getTChildProjectTypesRelatedByProjectTypeChild(Connection con) throws TorqueException
    {
        if (collTChildProjectTypesRelatedByProjectTypeChild == null)
        {
            collTChildProjectTypesRelatedByProjectTypeChild = getTChildProjectTypesRelatedByProjectTypeChild(new Criteria(10), con);
        }
        return collTChildProjectTypesRelatedByProjectTypeChild;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType has previously
     * been saved, it will retrieve related TChildProjectTypesRelatedByProjectTypeChild from storage.
     * If this TProjectType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TChildProjectType> getTChildProjectTypesRelatedByProjectTypeChild(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTChildProjectTypesRelatedByProjectTypeChild == null)
        {
            if (isNew())
            {
               collTChildProjectTypesRelatedByProjectTypeChild = new ArrayList<TChildProjectType>();
            }
            else
            {
                 criteria.add(TChildProjectTypePeer.PROJECTTYPECHILD, getObjectID());
                 collTChildProjectTypesRelatedByProjectTypeChild = TChildProjectTypePeer.doSelect(criteria, con);
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
                 criteria.add(TChildProjectTypePeer.PROJECTTYPECHILD, getObjectID());
                 if (!lastTChildProjectTypesRelatedByProjectTypeChildCriteria.equals(criteria))
                 {
                     collTChildProjectTypesRelatedByProjectTypeChild = TChildProjectTypePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTChildProjectTypesRelatedByProjectTypeChildCriteria = criteria;

         return collTChildProjectTypesRelatedByProjectTypeChild;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectType is new, it will return
     * an empty collection; or if this TProjectType has previously
     * been saved, it will retrieve related TChildProjectTypesRelatedByProjectTypeChild from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectType.
     */
    protected List<TChildProjectType> getTChildProjectTypesRelatedByProjectTypeChildJoinTProjectTypeRelatedByProjectTypeParent(Criteria criteria)
        throws TorqueException
    {
        if (collTChildProjectTypesRelatedByProjectTypeChild == null)
        {
            if (isNew())
            {
               collTChildProjectTypesRelatedByProjectTypeChild = new ArrayList<TChildProjectType>();
            }
            else
            {
                criteria.add(TChildProjectTypePeer.PROJECTTYPECHILD, getObjectID());
                collTChildProjectTypesRelatedByProjectTypeChild = TChildProjectTypePeer.doSelectJoinTProjectTypeRelatedByProjectTypeParent(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TChildProjectTypePeer.PROJECTTYPECHILD, getObjectID());
            if (!lastTChildProjectTypesRelatedByProjectTypeChildCriteria.equals(criteria))
            {
                collTChildProjectTypesRelatedByProjectTypeChild = TChildProjectTypePeer.doSelectJoinTProjectTypeRelatedByProjectTypeParent(criteria);
            }
        }
        lastTChildProjectTypesRelatedByProjectTypeChildCriteria = criteria;

        return collTChildProjectTypesRelatedByProjectTypeChild;
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
            fieldNames.add("NotifyOwnerLevel");
            fieldNames.add("NotifyManagerLevel");
            fieldNames.add("HoursPerWorkDay");
            fieldNames.add("MoreProps");
            fieldNames.add("DefaultForPrivate");
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
        if (name.equals("NotifyOwnerLevel"))
        {
            return getNotifyOwnerLevel();
        }
        if (name.equals("NotifyManagerLevel"))
        {
            return getNotifyManagerLevel();
        }
        if (name.equals("HoursPerWorkDay"))
        {
            return getHoursPerWorkDay();
        }
        if (name.equals("MoreProps"))
        {
            return getMoreProps();
        }
        if (name.equals("DefaultForPrivate"))
        {
            return getDefaultForPrivate();
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
        if (name.equals("NotifyOwnerLevel"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNotifyOwnerLevel((Integer) value);
            return true;
        }
        if (name.equals("NotifyManagerLevel"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNotifyManagerLevel((Integer) value);
            return true;
        }
        if (name.equals("HoursPerWorkDay"))
        {
            // Object fields can be null
            if (value != null && ! Double.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setHoursPerWorkDay((Double) value);
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
        if (name.equals("DefaultForPrivate"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDefaultForPrivate((String) value);
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
        if (name.equals(TProjectTypePeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TProjectTypePeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TProjectTypePeer.NOTIFYOWNERLEVEL))
        {
            return getNotifyOwnerLevel();
        }
        if (name.equals(TProjectTypePeer.NOTIFYMANAGERLEVEL))
        {
            return getNotifyManagerLevel();
        }
        if (name.equals(TProjectTypePeer.HOURSPERWORKDAY))
        {
            return getHoursPerWorkDay();
        }
        if (name.equals(TProjectTypePeer.MOREPROPS))
        {
            return getMoreProps();
        }
        if (name.equals(TProjectTypePeer.DEFAULTFORPRIVATE))
        {
            return getDefaultForPrivate();
        }
        if (name.equals(TProjectTypePeer.TPUUID))
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
      if (TProjectTypePeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TProjectTypePeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TProjectTypePeer.NOTIFYOWNERLEVEL.equals(name))
        {
            return setByName("NotifyOwnerLevel", value);
        }
      if (TProjectTypePeer.NOTIFYMANAGERLEVEL.equals(name))
        {
            return setByName("NotifyManagerLevel", value);
        }
      if (TProjectTypePeer.HOURSPERWORKDAY.equals(name))
        {
            return setByName("HoursPerWorkDay", value);
        }
      if (TProjectTypePeer.MOREPROPS.equals(name))
        {
            return setByName("MoreProps", value);
        }
      if (TProjectTypePeer.DEFAULTFORPRIVATE.equals(name))
        {
            return setByName("DefaultForPrivate", value);
        }
      if (TProjectTypePeer.TPUUID.equals(name))
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
            return getNotifyOwnerLevel();
        }
        if (pos == 3)
        {
            return getNotifyManagerLevel();
        }
        if (pos == 4)
        {
            return getHoursPerWorkDay();
        }
        if (pos == 5)
        {
            return getMoreProps();
        }
        if (pos == 6)
        {
            return getDefaultForPrivate();
        }
        if (pos == 7)
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
            return setByName("NotifyOwnerLevel", value);
        }
    if (position == 3)
        {
            return setByName("NotifyManagerLevel", value);
        }
    if (position == 4)
        {
            return setByName("HoursPerWorkDay", value);
        }
    if (position == 5)
        {
            return setByName("MoreProps", value);
        }
    if (position == 6)
        {
            return setByName("DefaultForPrivate", value);
        }
    if (position == 7)
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
        save(TProjectTypePeer.DATABASE_NAME);
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
                    TProjectTypePeer.doInsert((TProjectType) this, con);
                    setNew(false);
                }
                else
                {
                    TProjectTypePeer.doUpdate((TProjectType) this, con);
                }
            }


            if (collTPprioritys != null)
            {
                for (int i = 0; i < collTPprioritys.size(); i++)
                {
                    ((TPpriority) collTPprioritys.get(i)).save(con);
                }
            }

            if (collTProjects != null)
            {
                for (int i = 0; i < collTProjects.size(); i++)
                {
                    ((TProject) collTProjects.get(i)).save(con);
                }
            }

            if (collTRoles != null)
            {
                for (int i = 0; i < collTRoles.size(); i++)
                {
                    ((TRole) collTRoles.get(i)).save(con);
                }
            }

            if (collTPseveritys != null)
            {
                for (int i = 0; i < collTPseveritys.size(); i++)
                {
                    ((TPseverity) collTPseveritys.get(i)).save(con);
                }
            }

            if (collTDocStates != null)
            {
                for (int i = 0; i < collTDocStates.size(); i++)
                {
                    ((TDocState) collTDocStates.get(i)).save(con);
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

            if (collTWorkFlows != null)
            {
                for (int i = 0; i < collTWorkFlows.size(); i++)
                {
                    ((TWorkFlow) collTWorkFlows.get(i)).save(con);
                }
            }

            if (collTReportLayouts != null)
            {
                for (int i = 0; i < collTReportLayouts.size(); i++)
                {
                    ((TReportLayout) collTReportLayouts.get(i)).save(con);
                }
            }

            if (collTFields != null)
            {
                for (int i = 0; i < collTFields.size(); i++)
                {
                    ((TField) collTFields.get(i)).save(con);
                }
            }

            if (collTFieldConfigs != null)
            {
                for (int i = 0; i < collTFieldConfigs.size(); i++)
                {
                    ((TFieldConfig) collTFieldConfigs.get(i)).save(con);
                }
            }

            if (collTScreens != null)
            {
                for (int i = 0; i < collTScreens.size(); i++)
                {
                    ((TScreen) collTScreens.get(i)).save(con);
                }
            }

            if (collTScreenConfigs != null)
            {
                for (int i = 0; i < collTScreenConfigs.size(); i++)
                {
                    ((TScreenConfig) collTScreenConfigs.get(i)).save(con);
                }
            }

            if (collTEvents != null)
            {
                for (int i = 0; i < collTEvents.size(); i++)
                {
                    ((TEvent) collTEvents.get(i)).save(con);
                }
            }

            if (collTScriptss != null)
            {
                for (int i = 0; i < collTScriptss.size(); i++)
                {
                    ((TScripts) collTScriptss.get(i)).save(con);
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

            if (collTPRoles != null)
            {
                for (int i = 0; i < collTPRoles.size(); i++)
                {
                    ((TPRole) collTPRoles.get(i)).save(con);
                }
            }

            if (collTChildProjectTypesRelatedByProjectTypeParent != null)
            {
                for (int i = 0; i < collTChildProjectTypesRelatedByProjectTypeParent.size(); i++)
                {
                    ((TChildProjectType) collTChildProjectTypesRelatedByProjectTypeParent.get(i)).save(con);
                }
            }

            if (collTChildProjectTypesRelatedByProjectTypeChild != null)
            {
                for (int i = 0; i < collTChildProjectTypesRelatedByProjectTypeChild.size(); i++)
                {
                    ((TChildProjectType) collTChildProjectTypesRelatedByProjectTypeChild.get(i)).save(con);
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
    public TProjectType copy() throws TorqueException
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
    public TProjectType copy(Connection con) throws TorqueException
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
    public TProjectType copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TProjectType(), deepcopy);
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
    public TProjectType copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TProjectType(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TProjectType copyInto(TProjectType copyObj) throws TorqueException
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
    protected TProjectType copyInto(TProjectType copyObj, Connection con) throws TorqueException
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
    protected TProjectType copyInto(TProjectType copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setNotifyOwnerLevel(notifyOwnerLevel);
        copyObj.setNotifyManagerLevel(notifyManagerLevel);
        copyObj.setHoursPerWorkDay(hoursPerWorkDay);
        copyObj.setMoreProps(moreProps);
        copyObj.setDefaultForPrivate(defaultForPrivate);
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


        List<TRole> vTRoles = getTRoles();
        if (vTRoles != null)
        {
            for (int i = 0; i < vTRoles.size(); i++)
            {
                TRole obj =  vTRoles.get(i);
                copyObj.addTRole(obj.copy());
            }
        }
        else
        {
            copyObj.collTRoles = null;
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


        List<TDocState> vTDocStates = getTDocStates();
        if (vTDocStates != null)
        {
            for (int i = 0; i < vTDocStates.size(); i++)
            {
                TDocState obj =  vTDocStates.get(i);
                copyObj.addTDocState(obj.copy());
            }
        }
        else
        {
            copyObj.collTDocStates = null;
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


        List<TWorkFlow> vTWorkFlows = getTWorkFlows();
        if (vTWorkFlows != null)
        {
            for (int i = 0; i < vTWorkFlows.size(); i++)
            {
                TWorkFlow obj =  vTWorkFlows.get(i);
                copyObj.addTWorkFlow(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkFlows = null;
        }


        List<TReportLayout> vTReportLayouts = getTReportLayouts();
        if (vTReportLayouts != null)
        {
            for (int i = 0; i < vTReportLayouts.size(); i++)
            {
                TReportLayout obj =  vTReportLayouts.get(i);
                copyObj.addTReportLayout(obj.copy());
            }
        }
        else
        {
            copyObj.collTReportLayouts = null;
        }


        List<TField> vTFields = getTFields();
        if (vTFields != null)
        {
            for (int i = 0; i < vTFields.size(); i++)
            {
                TField obj =  vTFields.get(i);
                copyObj.addTField(obj.copy());
            }
        }
        else
        {
            copyObj.collTFields = null;
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


        List<TScreen> vTScreens = getTScreens();
        if (vTScreens != null)
        {
            for (int i = 0; i < vTScreens.size(); i++)
            {
                TScreen obj =  vTScreens.get(i);
                copyObj.addTScreen(obj.copy());
            }
        }
        else
        {
            copyObj.collTScreens = null;
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


        List<TEvent> vTEvents = getTEvents();
        if (vTEvents != null)
        {
            for (int i = 0; i < vTEvents.size(); i++)
            {
                TEvent obj =  vTEvents.get(i);
                copyObj.addTEvent(obj.copy());
            }
        }
        else
        {
            copyObj.collTEvents = null;
        }


        List<TScripts> vTScriptss = getTScriptss();
        if (vTScriptss != null)
        {
            for (int i = 0; i < vTScriptss.size(); i++)
            {
                TScripts obj =  vTScriptss.get(i);
                copyObj.addTScripts(obj.copy());
            }
        }
        else
        {
            copyObj.collTScriptss = null;
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


        List<TPRole> vTPRoles = getTPRoles();
        if (vTPRoles != null)
        {
            for (int i = 0; i < vTPRoles.size(); i++)
            {
                TPRole obj =  vTPRoles.get(i);
                copyObj.addTPRole(obj.copy());
            }
        }
        else
        {
            copyObj.collTPRoles = null;
        }


        List<TChildProjectType> vTChildProjectTypesRelatedByProjectTypeParent = getTChildProjectTypesRelatedByProjectTypeParent();
        if (vTChildProjectTypesRelatedByProjectTypeParent != null)
        {
            for (int i = 0; i < vTChildProjectTypesRelatedByProjectTypeParent.size(); i++)
            {
                TChildProjectType obj =  vTChildProjectTypesRelatedByProjectTypeParent.get(i);
                copyObj.addTChildProjectTypeRelatedByProjectTypeParent(obj.copy());
            }
        }
        else
        {
            copyObj.collTChildProjectTypesRelatedByProjectTypeParent = null;
        }


        List<TChildProjectType> vTChildProjectTypesRelatedByProjectTypeChild = getTChildProjectTypesRelatedByProjectTypeChild();
        if (vTChildProjectTypesRelatedByProjectTypeChild != null)
        {
            for (int i = 0; i < vTChildProjectTypesRelatedByProjectTypeChild.size(); i++)
            {
                TChildProjectType obj =  vTChildProjectTypesRelatedByProjectTypeChild.get(i);
                copyObj.addTChildProjectTypeRelatedByProjectTypeChild(obj.copy());
            }
        }
        else
        {
            copyObj.collTChildProjectTypesRelatedByProjectTypeChild = null;
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
    protected TProjectType copyInto(TProjectType copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setNotifyOwnerLevel(notifyOwnerLevel);
        copyObj.setNotifyManagerLevel(notifyManagerLevel);
        copyObj.setHoursPerWorkDay(hoursPerWorkDay);
        copyObj.setMoreProps(moreProps);
        copyObj.setDefaultForPrivate(defaultForPrivate);
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


        List<TRole> vTRoles = getTRoles(con);
        if (vTRoles != null)
        {
            for (int i = 0; i < vTRoles.size(); i++)
            {
                TRole obj =  vTRoles.get(i);
                copyObj.addTRole(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTRoles = null;
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


        List<TDocState> vTDocStates = getTDocStates(con);
        if (vTDocStates != null)
        {
            for (int i = 0; i < vTDocStates.size(); i++)
            {
                TDocState obj =  vTDocStates.get(i);
                copyObj.addTDocState(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTDocStates = null;
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


        List<TWorkFlow> vTWorkFlows = getTWorkFlows(con);
        if (vTWorkFlows != null)
        {
            for (int i = 0; i < vTWorkFlows.size(); i++)
            {
                TWorkFlow obj =  vTWorkFlows.get(i);
                copyObj.addTWorkFlow(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkFlows = null;
        }


        List<TReportLayout> vTReportLayouts = getTReportLayouts(con);
        if (vTReportLayouts != null)
        {
            for (int i = 0; i < vTReportLayouts.size(); i++)
            {
                TReportLayout obj =  vTReportLayouts.get(i);
                copyObj.addTReportLayout(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTReportLayouts = null;
        }


        List<TField> vTFields = getTFields(con);
        if (vTFields != null)
        {
            for (int i = 0; i < vTFields.size(); i++)
            {
                TField obj =  vTFields.get(i);
                copyObj.addTField(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTFields = null;
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


        List<TScreen> vTScreens = getTScreens(con);
        if (vTScreens != null)
        {
            for (int i = 0; i < vTScreens.size(); i++)
            {
                TScreen obj =  vTScreens.get(i);
                copyObj.addTScreen(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTScreens = null;
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


        List<TEvent> vTEvents = getTEvents(con);
        if (vTEvents != null)
        {
            for (int i = 0; i < vTEvents.size(); i++)
            {
                TEvent obj =  vTEvents.get(i);
                copyObj.addTEvent(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTEvents = null;
        }


        List<TScripts> vTScriptss = getTScriptss(con);
        if (vTScriptss != null)
        {
            for (int i = 0; i < vTScriptss.size(); i++)
            {
                TScripts obj =  vTScriptss.get(i);
                copyObj.addTScripts(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTScriptss = null;
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


        List<TPRole> vTPRoles = getTPRoles(con);
        if (vTPRoles != null)
        {
            for (int i = 0; i < vTPRoles.size(); i++)
            {
                TPRole obj =  vTPRoles.get(i);
                copyObj.addTPRole(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTPRoles = null;
        }


        List<TChildProjectType> vTChildProjectTypesRelatedByProjectTypeParent = getTChildProjectTypesRelatedByProjectTypeParent(con);
        if (vTChildProjectTypesRelatedByProjectTypeParent != null)
        {
            for (int i = 0; i < vTChildProjectTypesRelatedByProjectTypeParent.size(); i++)
            {
                TChildProjectType obj =  vTChildProjectTypesRelatedByProjectTypeParent.get(i);
                copyObj.addTChildProjectTypeRelatedByProjectTypeParent(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTChildProjectTypesRelatedByProjectTypeParent = null;
        }


        List<TChildProjectType> vTChildProjectTypesRelatedByProjectTypeChild = getTChildProjectTypesRelatedByProjectTypeChild(con);
        if (vTChildProjectTypesRelatedByProjectTypeChild != null)
        {
            for (int i = 0; i < vTChildProjectTypesRelatedByProjectTypeChild.size(); i++)
            {
                TChildProjectType obj =  vTChildProjectTypesRelatedByProjectTypeChild.get(i);
                copyObj.addTChildProjectTypeRelatedByProjectTypeChild(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTChildProjectTypesRelatedByProjectTypeChild = null;
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
    public TProjectTypePeer getPeer()
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
        return TProjectTypePeer.getTableMap();
    }

  
    /**
     * Creates a TProjectTypeBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TProjectTypeBean with the contents of this object
     */
    public TProjectTypeBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TProjectTypeBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TProjectTypeBean with the contents of this object
     */
    public TProjectTypeBean getBean(IdentityMap createdBeans)
    {
        TProjectTypeBean result = (TProjectTypeBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TProjectTypeBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setLabel(getLabel());
        result.setNotifyOwnerLevel(getNotifyOwnerLevel());
        result.setNotifyManagerLevel(getNotifyManagerLevel());
        result.setHoursPerWorkDay(getHoursPerWorkDay());
        result.setMoreProps(getMoreProps());
        result.setDefaultForPrivate(getDefaultForPrivate());
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


        if (collTRoles != null)
        {
            List<TRoleBean> relatedBeans = new ArrayList<TRoleBean>(collTRoles.size());
            for (Iterator<TRole> collTRolesIt = collTRoles.iterator(); collTRolesIt.hasNext(); )
            {
                TRole related = (TRole) collTRolesIt.next();
                TRoleBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTRoleBeans(relatedBeans);
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


        if (collTDocStates != null)
        {
            List<TDocStateBean> relatedBeans = new ArrayList<TDocStateBean>(collTDocStates.size());
            for (Iterator<TDocState> collTDocStatesIt = collTDocStates.iterator(); collTDocStatesIt.hasNext(); )
            {
                TDocState related = (TDocState) collTDocStatesIt.next();
                TDocStateBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTDocStateBeans(relatedBeans);
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


        if (collTWorkFlows != null)
        {
            List<TWorkFlowBean> relatedBeans = new ArrayList<TWorkFlowBean>(collTWorkFlows.size());
            for (Iterator<TWorkFlow> collTWorkFlowsIt = collTWorkFlows.iterator(); collTWorkFlowsIt.hasNext(); )
            {
                TWorkFlow related = (TWorkFlow) collTWorkFlowsIt.next();
                TWorkFlowBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkFlowBeans(relatedBeans);
        }


        if (collTReportLayouts != null)
        {
            List<TReportLayoutBean> relatedBeans = new ArrayList<TReportLayoutBean>(collTReportLayouts.size());
            for (Iterator<TReportLayout> collTReportLayoutsIt = collTReportLayouts.iterator(); collTReportLayoutsIt.hasNext(); )
            {
                TReportLayout related = (TReportLayout) collTReportLayoutsIt.next();
                TReportLayoutBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTReportLayoutBeans(relatedBeans);
        }


        if (collTFields != null)
        {
            List<TFieldBean> relatedBeans = new ArrayList<TFieldBean>(collTFields.size());
            for (Iterator<TField> collTFieldsIt = collTFields.iterator(); collTFieldsIt.hasNext(); )
            {
                TField related = (TField) collTFieldsIt.next();
                TFieldBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTFieldBeans(relatedBeans);
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


        if (collTScreens != null)
        {
            List<TScreenBean> relatedBeans = new ArrayList<TScreenBean>(collTScreens.size());
            for (Iterator<TScreen> collTScreensIt = collTScreens.iterator(); collTScreensIt.hasNext(); )
            {
                TScreen related = (TScreen) collTScreensIt.next();
                TScreenBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTScreenBeans(relatedBeans);
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


        if (collTEvents != null)
        {
            List<TEventBean> relatedBeans = new ArrayList<TEventBean>(collTEvents.size());
            for (Iterator<TEvent> collTEventsIt = collTEvents.iterator(); collTEventsIt.hasNext(); )
            {
                TEvent related = (TEvent) collTEventsIt.next();
                TEventBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTEventBeans(relatedBeans);
        }


        if (collTScriptss != null)
        {
            List<TScriptsBean> relatedBeans = new ArrayList<TScriptsBean>(collTScriptss.size());
            for (Iterator<TScripts> collTScriptssIt = collTScriptss.iterator(); collTScriptssIt.hasNext(); )
            {
                TScripts related = (TScripts) collTScriptssIt.next();
                TScriptsBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTScriptsBeans(relatedBeans);
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


        if (collTPRoles != null)
        {
            List<TPRoleBean> relatedBeans = new ArrayList<TPRoleBean>(collTPRoles.size());
            for (Iterator<TPRole> collTPRolesIt = collTPRoles.iterator(); collTPRolesIt.hasNext(); )
            {
                TPRole related = (TPRole) collTPRolesIt.next();
                TPRoleBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTPRoleBeans(relatedBeans);
        }


        if (collTChildProjectTypesRelatedByProjectTypeParent != null)
        {
            List<TChildProjectTypeBean> relatedBeans = new ArrayList<TChildProjectTypeBean>(collTChildProjectTypesRelatedByProjectTypeParent.size());
            for (Iterator<TChildProjectType> collTChildProjectTypesRelatedByProjectTypeParentIt = collTChildProjectTypesRelatedByProjectTypeParent.iterator(); collTChildProjectTypesRelatedByProjectTypeParentIt.hasNext(); )
            {
                TChildProjectType related = (TChildProjectType) collTChildProjectTypesRelatedByProjectTypeParentIt.next();
                TChildProjectTypeBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTChildProjectTypeBeansRelatedByProjectTypeParent(relatedBeans);
        }


        if (collTChildProjectTypesRelatedByProjectTypeChild != null)
        {
            List<TChildProjectTypeBean> relatedBeans = new ArrayList<TChildProjectTypeBean>(collTChildProjectTypesRelatedByProjectTypeChild.size());
            for (Iterator<TChildProjectType> collTChildProjectTypesRelatedByProjectTypeChildIt = collTChildProjectTypesRelatedByProjectTypeChild.iterator(); collTChildProjectTypesRelatedByProjectTypeChildIt.hasNext(); )
            {
                TChildProjectType related = (TChildProjectType) collTChildProjectTypesRelatedByProjectTypeChildIt.next();
                TChildProjectTypeBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTChildProjectTypeBeansRelatedByProjectTypeChild(relatedBeans);
        }

        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TProjectType with the contents
     * of a TProjectTypeBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TProjectTypeBean which contents are used to create
     *        the resulting class
     * @return an instance of TProjectType with the contents of bean
     */
    public static TProjectType createTProjectType(TProjectTypeBean bean)
        throws TorqueException
    {
        return createTProjectType(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TProjectType with the contents
     * of a TProjectTypeBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TProjectTypeBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TProjectType with the contents of bean
     */

    public static TProjectType createTProjectType(TProjectTypeBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TProjectType result = (TProjectType) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TProjectType();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setLabel(bean.getLabel());
        result.setNotifyOwnerLevel(bean.getNotifyOwnerLevel());
        result.setNotifyManagerLevel(bean.getNotifyManagerLevel());
        result.setHoursPerWorkDay(bean.getHoursPerWorkDay());
        result.setMoreProps(bean.getMoreProps());
        result.setDefaultForPrivate(bean.getDefaultForPrivate());
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
            List<TRoleBean> relatedBeans = bean.getTRoleBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TRoleBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TRoleBean relatedBean =  relatedBeansIt.next();
                    TRole related = TRole.createTRole(relatedBean, createdObjects);
                    result.addTRoleFromBean(related);
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
            List<TDocStateBean> relatedBeans = bean.getTDocStateBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TDocStateBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TDocStateBean relatedBean =  relatedBeansIt.next();
                    TDocState related = TDocState.createTDocState(relatedBean, createdObjects);
                    result.addTDocStateFromBean(related);
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
            List<TWorkFlowBean> relatedBeans = bean.getTWorkFlowBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkFlowBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkFlowBean relatedBean =  relatedBeansIt.next();
                    TWorkFlow related = TWorkFlow.createTWorkFlow(relatedBean, createdObjects);
                    result.addTWorkFlowFromBean(related);
                }
            }
        }


        {
            List<TReportLayoutBean> relatedBeans = bean.getTReportLayoutBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TReportLayoutBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TReportLayoutBean relatedBean =  relatedBeansIt.next();
                    TReportLayout related = TReportLayout.createTReportLayout(relatedBean, createdObjects);
                    result.addTReportLayoutFromBean(related);
                }
            }
        }


        {
            List<TFieldBean> relatedBeans = bean.getTFieldBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TFieldBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TFieldBean relatedBean =  relatedBeansIt.next();
                    TField related = TField.createTField(relatedBean, createdObjects);
                    result.addTFieldFromBean(related);
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
            List<TScreenBean> relatedBeans = bean.getTScreenBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TScreenBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TScreenBean relatedBean =  relatedBeansIt.next();
                    TScreen related = TScreen.createTScreen(relatedBean, createdObjects);
                    result.addTScreenFromBean(related);
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
            List<TEventBean> relatedBeans = bean.getTEventBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TEventBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TEventBean relatedBean =  relatedBeansIt.next();
                    TEvent related = TEvent.createTEvent(relatedBean, createdObjects);
                    result.addTEventFromBean(related);
                }
            }
        }


        {
            List<TScriptsBean> relatedBeans = bean.getTScriptsBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TScriptsBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TScriptsBean relatedBean =  relatedBeansIt.next();
                    TScripts related = TScripts.createTScripts(relatedBean, createdObjects);
                    result.addTScriptsFromBean(related);
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
            List<TPRoleBean> relatedBeans = bean.getTPRoleBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TPRoleBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TPRoleBean relatedBean =  relatedBeansIt.next();
                    TPRole related = TPRole.createTPRole(relatedBean, createdObjects);
                    result.addTPRoleFromBean(related);
                }
            }
        }


        {
            List<TChildProjectTypeBean> relatedBeans = bean.getTChildProjectTypeBeansRelatedByProjectTypeParent();
            if (relatedBeans != null)
            {
                for (Iterator<TChildProjectTypeBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TChildProjectTypeBean relatedBean =  relatedBeansIt.next();
                    TChildProjectType related = TChildProjectType.createTChildProjectType(relatedBean, createdObjects);
                    result.addTChildProjectTypeRelatedByProjectTypeParentFromBean(related);
                }
            }
        }


        {
            List<TChildProjectTypeBean> relatedBeans = bean.getTChildProjectTypeBeansRelatedByProjectTypeChild();
            if (relatedBeans != null)
            {
                for (Iterator<TChildProjectTypeBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TChildProjectTypeBean relatedBean =  relatedBeansIt.next();
                    TChildProjectType related = TChildProjectType.createTChildProjectType(relatedBean, createdObjects);
                    result.addTChildProjectTypeRelatedByProjectTypeChildFromBean(related);
                }
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
     * Method called to associate a TRole object to this object.
     * through the TRole foreign key attribute
     *
     * @param toAdd TRole
     */
    protected void addTRoleFromBean(TRole toAdd)
    {
        initTRoles();
        collTRoles.add(toAdd);
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
     * Method called to associate a TDocState object to this object.
     * through the TDocState foreign key attribute
     *
     * @param toAdd TDocState
     */
    protected void addTDocStateFromBean(TDocState toAdd)
    {
        initTDocStates();
        collTDocStates.add(toAdd);
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
     * Method called to associate a TWorkFlow object to this object.
     * through the TWorkFlow foreign key attribute
     *
     * @param toAdd TWorkFlow
     */
    protected void addTWorkFlowFromBean(TWorkFlow toAdd)
    {
        initTWorkFlows();
        collTWorkFlows.add(toAdd);
    }


    /**
     * Method called to associate a TReportLayout object to this object.
     * through the TReportLayout foreign key attribute
     *
     * @param toAdd TReportLayout
     */
    protected void addTReportLayoutFromBean(TReportLayout toAdd)
    {
        initTReportLayouts();
        collTReportLayouts.add(toAdd);
    }


    /**
     * Method called to associate a TField object to this object.
     * through the TField foreign key attribute
     *
     * @param toAdd TField
     */
    protected void addTFieldFromBean(TField toAdd)
    {
        initTFields();
        collTFields.add(toAdd);
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
     * Method called to associate a TScreen object to this object.
     * through the TScreen foreign key attribute
     *
     * @param toAdd TScreen
     */
    protected void addTScreenFromBean(TScreen toAdd)
    {
        initTScreens();
        collTScreens.add(toAdd);
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
     * Method called to associate a TEvent object to this object.
     * through the TEvent foreign key attribute
     *
     * @param toAdd TEvent
     */
    protected void addTEventFromBean(TEvent toAdd)
    {
        initTEvents();
        collTEvents.add(toAdd);
    }


    /**
     * Method called to associate a TScripts object to this object.
     * through the TScripts foreign key attribute
     *
     * @param toAdd TScripts
     */
    protected void addTScriptsFromBean(TScripts toAdd)
    {
        initTScriptss();
        collTScriptss.add(toAdd);
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


    /**
     * Method called to associate a TPRole object to this object.
     * through the TPRole foreign key attribute
     *
     * @param toAdd TPRole
     */
    protected void addTPRoleFromBean(TPRole toAdd)
    {
        initTPRoles();
        collTPRoles.add(toAdd);
    }


    /**
     * Method called to associate a TChildProjectType object to this object.
     * through the TChildProjectType foreign key attribute
     *
     * @param toAdd TChildProjectType
     */
    protected void addTChildProjectTypeRelatedByProjectTypeParentFromBean(TChildProjectType toAdd)
    {
        initTChildProjectTypesRelatedByProjectTypeParent();
        collTChildProjectTypesRelatedByProjectTypeParent.add(toAdd);
    }


    /**
     * Method called to associate a TChildProjectType object to this object.
     * through the TChildProjectType foreign key attribute
     *
     * @param toAdd TChildProjectType
     */
    protected void addTChildProjectTypeRelatedByProjectTypeChildFromBean(TChildProjectType toAdd)
    {
        initTChildProjectTypesRelatedByProjectTypeChild();
        collTChildProjectTypesRelatedByProjectTypeChild.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TProjectType:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("NotifyOwnerLevel = ")
           .append(getNotifyOwnerLevel())
           .append("\n");
        str.append("NotifyManagerLevel = ")
           .append(getNotifyManagerLevel())
           .append("\n");
        str.append("HoursPerWorkDay = ")
           .append(getHoursPerWorkDay())
           .append("\n");
        str.append("MoreProps = ")
           .append(getMoreProps())
           .append("\n");
        str.append("DefaultForPrivate = ")
           .append(getDefaultForPrivate())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
