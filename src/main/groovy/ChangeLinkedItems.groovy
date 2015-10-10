

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.beans.TLinkTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.link.ItemLinkBL;
import com.aurel.track.item.link.ItemLinkConfigBL;
import com.aurel.track.linkType.LinkTypeBL;
import com.aurel.track.util.event.IEventHandler;

public class ChangeLinkedItems implements IEventHandler {			
	
	/**
	 * Closes the linked items of the current item
	 */
	public Map<String, Object> handleEvent(Map<String, Object> inputBinding) {
		WorkItemContext workItemContext = (WorkItemContext)inputBinding.get("workItemContext");
		TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
		Integer itemID = workItemBean.getObjectID();
		TPersonBean personBean = (TPersonBean)inputBinding.get("user");
		Locale locale = (Locale)inputBinding.get("locale");
		List<ErrorData> errorList = new LinkedList<ErrorData>();
		//look up the linktype ID in Link types config, ID column
		Integer linkTypeID = 1001;
		//1=Unidirectional outward and bidirectional links, 2=Unidirectional inward (MsProject) 
		Integer linkDirection = 1; 
		List<Integer> linkedItemIDs = ItemLinkBL.getLinkedItemIDs(itemID, linkTypeID, linkDirection);
		if (linkedItemIDs.size()>0) {
			List<TStateBean> targetStates = StatusBL.loadClosedStates();
			//alternatively look up target state(s) by other criteria
			//List<TStateBean> labelStates = StatusBL.loadByLabel("<label>");
			//TStateBean stateLabel = StatusBL.loadByPrimaryKey(<objectID>);
			if (targetStates!=null && targetStates.size()>0) {
				Integer targetStateID = targetStates.get(0).getObjectID();
				for (linkedItemID in linkedItemIDs) {
					WorkItemContext linkedContext = ItemBL.editWorkItem(linkedItemID, personBean.getObjectID(), locale, false);
					TWorkItemBean linkedItem = linkedContext.getWorkItemBean();
					linkedItem.setStateID(targetStateID);
					//linkedItem.setComment("<Comment to add>");
					ItemBL.saveWorkItem(linkedContext, errorList, true);
				}
			}
		}
		inputBinding.put("errorList", errorList);	
		return inputBinding;
	}
}