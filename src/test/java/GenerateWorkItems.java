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

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.torque.Torque;

import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.IBeanID;
import com.aurel.track.beans.TStateChangeBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.IssueTypeDAO;
import com.aurel.track.dao.PriorityDAO;
import com.aurel.track.dao.SeverityDAO;
import com.aurel.track.dao.StateChangeDAO;
import com.aurel.track.dao.StateDAO;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.item.ItemPersisterException;


public class GenerateWorkItems {

	public static int numberOfItems = 609;
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
	private static StateChangeDAO stateChangeDAO = DAOFactory.getFactory().getStateChangeDAO();	
	private static IssueTypeDAO issueTypeDAO = DAOFactory.getFactory().getIssueTypeDAO();	
	private static StateDAO stateDAO = DAOFactory.getFactory().getStateDAO();	
	private static PriorityDAO priorityDAO = DAOFactory.getFactory().getPriorityDAO();
	private static SeverityDAO severityDAO = DAOFactory.getFactory().getSeverityDAO();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		initTorque();
		List issueTypes = issueTypeDAO.loadAll();
		List states = stateDAO.loadAll();
		List persons = PersonBL.loadPersons();		
		List priorities = priorityDAO.loadAll();
		List severities = severityDAO.loadAll();
		Integer lastWorkItemID = null;
		for (int i = 0; i < numberOfItems; i++) {
			TWorkItemBean workItemBean = new TWorkItemBean();
			Integer randomProject = new Integer(1);// getRandomObjectIdFromList(projects);
			workItemBean.setListTypeID(getRandomObjectIdFromList(issueTypes));
			/*workItemBean.setProjectCategoryID(
					getRandomObjectIdFromList(subprojectDAO.loadByProject1(randomProject)));*/
			workItemBean.setStateID(getRandomObjectIdFromList(states));
			workItemBean.setOwnerID(getRandomObjectIdFromList(persons));
			workItemBean.setResponsibleID(getRandomObjectIdFromList(persons));
			/*workItemBean.setClassID(
					getRandomObjectIdFromList(classDAO.loadByProject1(randomProject)));*/			
			/*workItemBean.setReleaseNoticedID(
					getRandomObjectIdFromList(ReleaseBL.loadNotClosedByProject(randomProject)));
			workItemBean.setReleaseScheduledID(
					getRandomObjectIdFromList(releaseDAO.loadNotClosedByProject(randomProject)));*/
			workItemBean.setPriorityID(getRandomObjectIdFromList(priorities));
			workItemBean.setSeverityID(getRandomObjectIdFromList(severities));			
			workItemBean.setOriginatorID(getRandomObjectIdFromList(persons));
			workItemBean.setCreated(new Date());
			workItemBean.setLastEdit(new Date());
			workItemBean.setChangedByID(getRandomObjectIdFromList(persons));
			workItemBean.setSynopsis("Title" + i);			
			workItemBean.setBuild("Build" + i);
			if (i%2==1) {
				workItemBean.setSuperiorworkitem(lastWorkItemID);
			}
			Date date = new Date();
			workItemBean.setStartDate(new Date(date.getYear(), date.getMonth(), date.getDate()));
			workItemBean.setEndDate(new Date(date.getYear(), date.getMonth(), date.getDate() + 20));
			workItemBean.setDescription("Some descriptuion for item " + i);
			try {
				lastWorkItemID = workItemDAO.save(workItemBean);
			} catch (ItemPersisterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			TStateChangeBean stateChangeBean = new TStateChangeBean();
	    	stateChangeBean.setWorkItemID(workItemBean.getObjectID());
	    	stateChangeBean.setChangedToID(workItemBean.getStateID());
	    	stateChangeBean.setChangedByID(workItemBean.getChangedByID());
	        stateChangeBean.setLastEdit(workItemBean.getLastEdit());
	        stateChangeBean.setChangeDescription("Generated status change text for " + workItemBean.getObjectID());
	        stateChangeDAO.save(stateChangeBean); 
	        System.out.println("Issue number " + workItemBean.getObjectID() + " created.");
		}

	}

	private static Integer getRandomObjectIdFromList(List beanList) {
		Random generator = new Random();
		int random = generator.nextInt(beanList.size());
		return ((IBeanID)beanList.get(random)).getObjectID();
	}
	
	 private static void initTorque() {
	        try	{
	        	PropertiesConfiguration tcfg = new PropertiesConfiguration();
	        	URL torqueURL = GenerateWorkItems.class.getResource("/Torque.properties");	        	
	        	InputStream in = torqueURL.openStream();
	        	tcfg.load(in);
	        		        	
	        	/*tcfg.setProperty("torque.dsfactory.track.connection.user", "sysdba");
	        	tcfg.setProperty("torque.dsfactory.track.connection.password", "masterkey");
	        	tcfg.setProperty("torque.database.track.adapter", "firebird");
	        	tcfg.setProperty("torque.dsfactory.track.connection.driver", "org.firebirdsql.jdbc.FBDriver");
	        	tcfg.setProperty("torque.dsfactory.track.connection.url", "jdbc:firebirdsql://localhost/C:/Firebird_1_5/databases/TEST34.GDB");
	        	tcfg.setProperty("torque.dsfactory.track.factory", "org.apache.torque.dsfactory.SharedPoolDataSourceFactory");
	        	tcfg.setProperty("torque.dsfactory.track.pool.maxActive", "30");
	        	tcfg.setProperty("torque.dsfactory.track.pool.testOnBorrow","true");
	        	tcfg.setProperty("torque.dsfactory.track.pool.validationQuery","SELECT PKEY FROM TSTATE");
	        	*/
	        		
	        	tcfg.setProperty("torque.applicationRoot",".");
	        	tcfg.setProperty("torque.database.default","track");
	        	tcfg.setProperty("torque.idbroker.clever.quantity",new Boolean(false));
	        	tcfg.setProperty("torque.idbroker.prefetch",new Boolean(false));
	        	tcfg.setProperty("torque.manager.useCache",new Boolean(true));	        
	        	in.close();        	
	        	Torque.init(tcfg);

	        }
	        catch (Exception e) {
	        	e.printStackTrace();
	        }	       
	    	
	    }
}
