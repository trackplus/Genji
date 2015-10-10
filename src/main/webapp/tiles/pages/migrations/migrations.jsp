<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- $Source: /cvsroot/trackplus/track/web/tiles/pages/migrations/migrations.jsp,v $ -->


<s:form id="editMigration" name="editMigration" theme="simple"
	action="addRaciRole!save">
	<s:hidden name="itemID" value="%{itemID}"/>
	<s:hidden name="migration.objectID" value="%{migration.objectID}"/>
	<s:hidden name="migration.workItemID" value="%{migration.workItemID}"/>
	<div class="containerRow1" style="padding: 0;">
		<table border="0" cellspacing="0" cellpadding="0">
			<tr class="dataRow1">
				<td class="dataName">
					<s:text name="item.tabs.migration.lbl.moduleName" />
				</td>
				<td  class="dataValue">
					<s:textfield style="width:200px;"  name="migration.moduleName" theme="simple" />
				</td>
				<td class="dataName" style="text-align: right;">
					<s:text name="item.tabs.migration.lbl.moduleType" />
				</td>
				<td>
					<s:select id="migrationType" name="migration.moduleType"		    		
			        	list="migrationTypes" listKey="value" listValue="label"
			        	theme="simple"/>
				</td>
			</tr>
			
			<s:if test="fieldErrors['migration.moduleName']!=null||fieldErrors['migration.moduleType']!=null">
			<tr class="dataRow1">
				<td class="dataName"></td>
		  		<td class="dataValue" colspan="3">
			    	<s:fielderror>
	         			<s:param value="%{'migration.moduleName'}" />        
	   				</s:fielderror>		    
		  		</td>
		  	</tr>
			</s:if>
			
			<tr class="dataRow1">	
				<td class="dataName" style="text-align: right;">
					<s:text name="item.tabs.migration.lbl.status" />
				</td>
				<td class="dataValue">
					<s:select id="migrationStatus" name="migration.status"		    		
			        	list="migrationStates" listKey="value" listValue="label"
			        	theme="simple"/>
				</td>
				
				<td class="dataName" style="text-align: right;">
					<s:text name="item.tabs.migration.lbl.action" />
				</td>
				<td class="dataValue">
					<s:select id="migrationAction" name="migration.action"		    		
			        	list="migrationActions" listKey="value" listValue="label"
			        	theme="simple"/>
				</td>
			</tr>
			<tr class="dataRow1">
				<td class="dataName" style="text-align: right;vertical-align: top;">
					<s:text name="item.tabs.migration.lbl.comment" />
				</td>
				<td class="dataValue" colspan="3">
					<s:textarea  cssStyle="width:100%;height:100%" name="migration.comment" cols="60" rows="4" theme="simple"></s:textarea>
				</td>
			</tr>
			<tr class="dataRow1">
				<td class="dataName" style="text-align: right;">
					<s:text name="item.tabs.migration.lbl.order" />
				</td>
				<td class="dataValue">
					<s:textfield name="migration.order" maxlength="5" size="5"  theme="simple"/>
				</td>
				<td class="dataName" nowrap="nowrap" colspan="2">
					<s:checkbox name="migration.test" theme="simple"/>
	  				<s:text name="item.tabs.migration.lbl.test"/>
	  				&nbsp;&nbsp;&nbsp;
	  				<s:checkbox name="migration.qa" theme="simple"/>
	  				<s:text name="item.tabs.migration.lbl.qa"/>
	  				&nbsp;&nbsp;&nbsp;
	  				<s:checkbox name="migration.prod" theme="simple"/>
	  				<s:text name="item.tabs.migration.lbl.prod"/>
	  				&nbsp;&nbsp;&nbsp;
	  				<s:checkbox name="migration.ops" theme="simple"/>
	  				<s:text name="item.tabs.migration.lbl.ops"/>
				</td>
			</tr>
			<s:if test="fieldErrors['migration.order']!=null">
			<tr class="dataRow1">
				<td class="dataName"></td>
		  		<td class="dataValue" colspan="3">
			    	<s:fielderror>
	         			<s:param value="%{'migration.order'}" />        
	   				</s:fielderror>		    
		  		</td>
		  	</tr>
			</s:if>
		</table>
	</div>
</s:form>
<s:actionerror />
