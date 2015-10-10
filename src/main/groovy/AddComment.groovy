import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.util.event.IEventHandler;

public class CommentByReopen implements IEventHandler {			
	
	private static String REOPEN_SEPARATOR  = "**************************REOPEN*******************************";
	
	public Map<String, Object> handleEvent(Map<String, Object> inputBinding) {
		TWorkItemBean workItemBean = (TWorkItemBean)inputBinding.get("issue");
		String comment = workItemBean.getComment();
		if (comment==null || comment.trim().length()==0) {
			List<ErrorData> errorList = new LinkedList<ErrorData>();
			errorList.add(new ErrorData("Comment is required by reopen"));
			inputBinding.put("errorList", errorList);	
		} else {
			String description = workItemBean.getDescription();
			if (description==null) {
				description = "";
			}
			description = description + REOPEN_SEPARATOR + comment;
			workItemBean.setDescription(description);
			workItemBean.setComment(null);			
		}
		return inputBinding;
	}
}