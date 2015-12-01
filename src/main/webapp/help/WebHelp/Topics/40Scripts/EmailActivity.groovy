


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aurel.track.admin.customize.scripting.BINDING_PARAMS;
import com.aurel.track.admin.customize.scripting.GroovyScriptExecuter;
import com.aurel.track.admin.customize.treeConfig.field.FieldDesignBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.ProjectConfigBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ILookup;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.util.emailHandling.EmailCleanUtils;

	
public class EmailActivityScript {
	private static String SUBPROJECT = "Subproject";
	private static String PARAMETER_SCRIPT_NAME = "EmailActivityParams";
	private static String PARAMETER_SCRIPT_CUT_PATTERNS_METHOD = "getCutPatterns";
	private static String PARAMETER_SCRIPT_REJECT_ATTACHMENT_PATTERNS_METHOD = "getRejectAttachmnentPatterns";
	private static String PARAMETER_SCRIPT_PATTERN_TO_FIELDS_METHOD = "getPatternToFields";
	
	public Map handleEvent(Map inputBinding) {						
		TWorkItemBean workItemBean = (TWorkItemBean)inputBinding.get(BINDING_PARAMS.ISSUE);
		WorkItemContext workItemContext = (WorkItemContext)inputBinding.get(BINDING_PARAMS.WORKITEM_CONTEXT);
		 Set<Integer> presentFieldIDs = workItemContext.getPresentFieldIDs();
		 if (presentFieldIDs==null) {
			 presentFieldIDs = new HashSet<Integer>();
			 workItemContext.setPresentFieldIDs(presentFieldIDs);
		 }
		//TWorkItemBean workItemBeanOriginal = (TWorkItemBean)inputBinding.get(BINDING_PARAMS.ISSUE_ORIGINAL);		
		String subject = (String)inputBinding.get(BINDING_PARAMS.EMAIL_SUBJECT);
		if (subject==null) {
			return inputBinding;
		}
		List attachments = (List)inputBinding.get(BINDING_PARAMS.EMAIL_ATTACHMENTS);
		TProjectBean originalProjectBean = (TProjectBean)inputBinding.get(BINDING_PARAMS.EMAIL_PROJECT);
		Integer sender = (Integer)inputBinding.get(BINDING_PARAMS.USER_ID);
		
		//cut the description below if match for any cutPattern
		List<Pattern> cutPatterns = (List<Pattern>)GroovyScriptExecuter.getParameterInstanceGroovyHandler(
				PARAMETER_SCRIPT_NAME, PARAMETER_SCRIPT_CUT_PATTERNS_METHOD);
		if (cutPatterns!=null) {
			boolean existingIssue = false;
			String comment = workItemBean.getComment();			
			String content = null;
			if (comment!=null) {
				//added comment to existing issue
				content = comment;
				existingIssue = true;
			} else {
				//added description to a new issue
				content = workItemBean.getDescription();				
			}			
			if (content!=null) {
				StringBuilder stringBuilder = new StringBuilder(content);
				while (EmailCleanUtils.removeShortest(stringBuilder, cutPatterns)) {										
				}
				content = stringBuilder.toString();				
				if (existingIssue) {
					workItemBean.setComment(content);
				} else {
					workItemBean.setDescription(content);
				}
			}
		}
		
		//reject attachment if matches any rejectAttachmentPattern
		List<Pattern> rejectAttachmentPatterns = (List<Pattern>)GroovyScriptExecuter.getParameterInstanceGroovyHandler(
				PARAMETER_SCRIPT_NAME, PARAMETER_SCRIPT_REJECT_ATTACHMENT_PATTERNS_METHOD);
		EmailCleanUtils.removeMatchingAttachments(attachments, rejectAttachmentPatterns);										
				
		if (workItemBean.getObjectID()!=null) {
			return inputBinding;
		}
		
		//set the workItemBean if match found
		Map<Pattern, List<String>> patternToFields = (Map<Pattern, List<String>>)GroovyScriptExecuter.getParameterInstanceGroovyHandler(
				PARAMETER_SCRIPT_NAME, PARAMETER_SCRIPT_PATTERN_TO_FIELDS_METHOD);
		Pattern firstPattern = null;	
		if (patternToFields!=null) {
			int minStartIndex = subject.length();
			for (Pattern pattern :  patternToFields.keySet()) {
				try {
					Matcher matcher = pattern.matcher(subject);
					boolean find = matcher.find();
					if (find) {				
						int startIndex = matcher.start();
						//System.out.println("subject " + subject + " subjectPattern " + startIndex);
						if (startIndex<minStartIndex) {
							minStartIndex = startIndex;
							firstPattern = pattern;
						}
					}
				} catch (Exception e) {
				}
			}
		}
		//System.out.println("subject " + subject + " subjectPattern " + subjectPattern + " matches " + matches);
		if (firstPattern!=null) {
			List<String> issueFields = (List<String>)patternToFields.get(firstPattern);
			if (issueFields!=null) {
				TProjectBean projectBean = ProjectBL.loadByLabel((String)issueFields.get(0));
				if (projectBean!=null) {
					if (originalProjectBean!=null && !projectBean.getObjectID().equals(originalProjectBean.getObjectID())) {
						//the project identified by the mail To address differs from the configured according to parameter list
						//project differs from the original one, set the project dependent fields again
						//to avoid inconsistent releases 
						workItemBean.setReleaseNoticedID(ProjectConfigBL.getDefaultFieldValueForProject(
								SystemFields.RELEASENOTICED, projectBean, sender, workItemBean.getListTypeID(), null));
						workItemBean.setReleaseScheduledID(ProjectConfigBL.getDefaultFieldValueForProject(
								SystemFields.RELEASESCHEDULED, projectBean, sender, workItemBean.getListTypeID(), null));
					}						
					workItemBean.setProjectID(projectBean.getObjectID());
					List<TFieldBean> fieldBeans = FieldDesignBL.loadByName(SUBPROJECT);
					if (fieldBeans!=null && fieldBeans.size()>0) {
						TFieldBean fieldBean = fieldBeans.get(0);
						if (fieldBean!=null) {
							Integer subprojectFieldID = fieldBean.getObjectID();
							presentFieldIDs.add(subprojectFieldID);
							ILookup lookup = (ILookup)FieldTypeManager.getFieldTypeRT(subprojectFieldID);
							Integer subprojectID = lookup.getLookupIDByLabel(subprojectFieldID, projectBean.getObjectID(),
									workItemBean.getListTypeID(), null, (String)issueFields.get(1), null, null);
							if (subprojectID!=null) {
								workItemBean.setAttribute(subprojectFieldID, [subprojectID].toArray());
							}
						}
					}
					String managerLabel = (String)issueFields.get(2);
					if (managerLabel!=null) {
						TPersonBean managerBean = PersonBL.loadByLabel(managerLabel);
						if (managerBean!=null) {
							workItemBean.setOwnerID(managerBean.getObjectID());
						}
					}
					String responsibleLabel = (String)issueFields.get(3);
					if (responsibleLabel!=null) {
						TPersonBean responsibleBean = PersonBL.loadByLabel(responsibleLabel);
						if (responsibleBean!=null) {
							workItemBean.setResponsibleID(responsibleBean.getObjectID());
						}
					}
				}
			}								
		}													
		return inputBinding;
	}
	
}