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

package com.aurel.track.dao.torque;


/**
 *  TODO: complete the implementation
 */
public class TqlTorqueDAO  {//implements TqlDAO {
	/*
    private  static TqlTorqueDAO instance;
    protected static Logger LOGGER = LogManager.getLogger(TqlTorqueDAO.class);

    public static TqlTorqueDAO getInstance(){
        if(instance==null){
            instance=new TqlTorqueDAO();
        }
        return instance;
    }
    private TqlTorqueDAO(){
        
    }
    public List getPublicTql() {
        List torqueList = new ArrayList();
        try
        {
            Criteria criteria = new Criteria ();
            criteria.addJoin (TPublicReportRepositoryPeer.OWNER, TPersonPeer.PKEY);
            criteria.setDistinct ();
            torqueList = TPublicReportRepositoryPeer.doSelect (criteria);
        }
        catch (TorqueException e){
            e.printStackTrace ();
        }
        return convertPublicTqlList(torqueList);
    }

    public List getPrivateTql(Integer userID) {
        List torqueList = new ArrayList ();
        try{
            Criteria criteria = new Criteria ();
            criteria.add (TPrivateReportRepositoryPeer.OWNER, userID, Criteria.EQUAL);
            torqueList = TPrivateReportRepositoryPeer.doSelect (criteria);
        }
        catch (TorqueException e){
            LOGGER.error("Error on getting privateTql with userID:="+userID);
            e.printStackTrace ();
        }
        return convertPrivateTqlList(torqueList);
    }

    public List getProjectTql(Integer projectID) {
        List torqueList = new ArrayList ();
		try{
			Criteria criteria = new Criteria ();
			criteria.add (TProjectReportRepositoryPeer.PROJECT, projectID, Criteria.EQUAL);
			torqueList = TProjectReportRepositoryPeer.doSelect (criteria);
		}
		catch (TorqueException e){
			LOGGER.error("Error on getting ProjectTql with projectId:="+projectID);
			e.printStackTrace ();
		}
        return convertProjectTqlList(torqueList);
    }

    public List getProjectTqlByUser(Integer userId) {
       List result=new ArrayList();
       List myProjects= DAOFactory.getFactory().getProjectDAO().loadMyProjects(userId);
       if(myProjects==null||myProjects.isEmpty()){
           return result;
       }
       for (Iterator iterator = myProjects.iterator(); iterator.hasNext();) {
            TProjectBean project =  (TProjectBean)iterator.next();
            List tqlProject=getProjectTql(project.getObjectID());
            result.addAll(tqlProject);
       }
       return result; 
    }

    public TProjectReportRepositoryBean getProjectTqlById(Integer id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public TPrivateReportRepositoryBean getPrivateTqlById(Integer id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public TPublicReportRepositoryBean getPublicTqlById(Integer id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
    private List convertPublicTqlList(List torqueList){
   		List beanList = new ArrayList();
		if (torqueList!=null){
			Iterator itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(((TPublicReportRepository)itrTorqueList.next()).getBean());
			}
		}
		return beanList;
    }
    private List convertPrivateTqlList(List torqueList){
   		List beanList = new ArrayList();
		if (torqueList!=null){
			Iterator itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(((TPrivateReportRepository)itrTorqueList.next()).getBean());
			}
		}
		return beanList;
    }
    private List convertProjectTqlList(List torqueList){
   		List beanList = new ArrayList();
		if (torqueList!=null){
			Iterator itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(((TProjectReportRepository)itrTorqueList.next()).getBean());
			}
		}
		return beanList;
    }*/
}
