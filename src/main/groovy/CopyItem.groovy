

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.TLinkTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.link.ItemLinkConfigBL;
import com.aurel.track.linkType.LinkTypeBL;
import com.aurel.track.util.event.IEventHandler;

public class CopyItem implements IEventHandler {			
	
	/**
	 * Copies an item and creates a link between the original item and copied item
	 */
	public Map<String, Object> handleEvent(Map<String, Object> inputBinding) {
		WorkItemContext workItemContext = (WorkItemContext)inputBinding.get("workItemContext");
		TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
		Integer copiedFromItemID = workItemBean.getObjectID();
		TPersonBean personBean = (TPersonBean)inputBinding.get("user");
		Locale locale = (Locale)inputBinding.get("locale");
		List<ErrorData> errorList = new LinkedList<ErrorData>();
		//create a copy of an item
		WorkItemContext copyContext =  ItemBL.editCopyWorkItem(copiedFromItemID, personBean.getObjectID(), locale);
		TWorkItemBean workItemBeanCopy = copyContext.getWorkItemBean();
		//change the copied item as needed
		workItemBeanCopy.setSynopsis("(Copy) " + workItemBeanCopy.getSynopsis());
		//save the copied item in db
		Integer copiedItemID = ItemBL.copyWorkItem(copyContext, errorList, false);
		//look up the linktype ID in Link types config, ID column
		Integer linkTypeID = 1001;
		//1=Unidirectional outward and bidirectional links, 2=Unidirectional inward (MsProject) 
		Integer linkDirection = 1; 
		List<ErrorData> linkErrors = ItemLinkConfigBL.saveItemLink(copiedFromItemID, copiedItemID, linkTypeID, linkDirection, null, null, personBean, locale, null, workItemContext);
		if (linkErrors!=null) {
			errorList.addAll(linkErrors);
		}
		inputBinding.put("errorList", errorList);	
		return inputBinding;
	}
}