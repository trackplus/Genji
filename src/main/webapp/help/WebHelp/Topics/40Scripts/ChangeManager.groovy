//
// Change the manager to the first person that belongs to a group in role "roleNew"
// upon a status change
//
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.util.event.IEventHandler;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.PersonDAO;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.dao.RoleDAO;
import com.aurel.track.beans.TRoleBean;

public class ChangeManager implements IEventHandler {

    def public Map handleEvent(Map inputBinding) {
        
        TWorkItemBean workItemBean = inputBinding.get("issue");
        PersonDAO personDAO =
                   DAOFactory.getFactory().getPersonDAO();
            
        RoleDAO roleDAO= DAOFactory.getFactory().getRoleDAO();
        TRoleBean roleBean = roleDAO.loadByName("roleNew");
            
        if (roleBean!=null) {
           List<TPersonBean> personsWithExchangeRole =
                   personDAO.loadByProjectAndRoles(
                                   workItemBean.getProjectID(),
                                   roleBean.getObjectID());

           if (personsWithExchangeRole!=null &&
                personsWithExchangeRole.size() > 0) {
                   TPersonBean personBean =
                      (TPersonBean)personsWithExchangeRole.get(0);
                   workItemBean.setOwnerID(
                         personBean.getObjectID());
           }
       }
    }
}