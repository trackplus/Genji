//
// This script changes the responsible to "guest" when a status change occurs
//
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.util.event.IEventHandler;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.PersonDAO;
import com.aurel.track.beans.TPersonBean;

public class ChangeResponsible implements IEventHandler {

   def public Map handleEvent(Map inputBinding) {
        TWorkItemBean workItemBean = inputBinding.get("issue");
        PersonDAO personDAO =
                 DAOFactory.getFactory().getPersonDAO();
        TPersonBean personBean =
                         personDAO.loadByLoginName("guest");
        workItemBean.setResponsibleID(personBean.getObjectID());  
   }
   
}