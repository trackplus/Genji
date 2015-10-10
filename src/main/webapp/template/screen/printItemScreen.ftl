<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<#assign app=JspTaglibs["/WEB-INF/tlds/app.tld"]>
<@s.set name="iconsPath"
    	value="'${request.getContextPath()}'+'/design/'+#session.userDesignPath+'/icons/'"/>
<@s.fielderror/>
<@s.token/>
<@s.if test="tabs.size()==1">
	<div id="tabConted1">
		<!--this DIV is "trick" for IE6-->
		<div class="containerSeparator" style="height:1px;">&nbsp;</div>
		<@s.iterator value="tabs[0].panels" status="status">
			<@s.if test="!#status.first">
				<div class="containerSeparator">&nbsp;</div>
			</@s.if>
			<div  class="<@s.if test="#status.even">containerRow1</@s.if><@s.else>containerRow2</@s.else>"
				id="<@s.property value="panel.objectID"/>">
				<table class="containerTable">
					<@s.iterator value="fields" status="status1">
					<TR>
						<@s.iterator value="top" status="status">
							<@s.if test="top!=null">
								<@s.if test="field!=null">
									<@s.if test="labelInsideTemplate">
									     <td colspan="${2*field.colSpan}" rowspan="${field.rowSpan}">
									        <#-- <#include "field/${templateName}"> -->
									        <@s.if test="readOnlyMode">
									        	<#include "../../WEB-INF/classes/plugins/fieldType/templates/readOnly_${templateName}">
									        </@s.if>
									        <@s.else>
									        	<#include "../../WEB-INF/classes/plugins/fieldType/templates/${templateName}">
									        </@s.else>
									     </td>
									</@s.if>
									<@s.else>										
									    <td nowrap class="screenFromDataName" align="${labelHAlign}" valign="${labelVAlign}" rowspan="${field.rowSpan}">
									    	<@s.if test="readOnlyMode">
		                                    	<b><label for="${nameMapping}">${label}&nbsp:</label></b>
		                                    </@s.if>
									        <@s.else>
									        	<label class="<@s.if test="required">required</@s.if>" for="${nameMapping}">${label}&nbsp:</label>
									        </@s.else>
		                                </td>		                                
		                                <td nowrap align="${valueHAlign}" valign="${valueVAlign}" class="screenFromDataValue" colspan="${2*field.colSpan-1}" rowspan="${field.rowSpan}">		                                                                       
		                                    <#-- <#include "field/${templateName}"> -->
		                                    <@s.if test="readOnlyMode">
									        	<#include "../../WEB-INF/classes/plugins/fieldType/templates/readOnly_${templateName}">
									        </@s.if>
									        <@s.else>
									        	<#include "../../WEB-INF/classes/plugins/fieldType/templates/${templateName}">
									        </@s.else>
		                                </td>
								    </@s.else>
								</@s.if>
								<@s.else>
									<#-- empty field -->
									<TD colspan="2">&nbsp;</TD>
								</@s.else>
							</@s.if>
						</@s.iterator>
					</TR>
					</@s.iterator>
				</table>
			</div>
		</@s.iterator>
	</div>
</@s.if>
<@s.else>
	<div id="itemTabs1" style="width:100%;position: relative;">
		<@s.iterator value="tabs" status="status1">
			<div id="tab_<@s.property value="tab.objectID"/>" class="x-hide-display">
				<!--this DIV is "trick" for IE6-->
				<div class="containerSeparator">&nbsp;</div>
				<@s.iterator value="panels" status="status">
					<@s.if test="!#status.first">
						<div class="containerSeparator">&nbsp;</div>
					</@s.if>
					<div  class="<@s.if test="#status.even">containerRow1</@s.if><@s.else>containerRow2</@s.else>"
						id="<@s.property value="panel.objectID"/>">
						<table class="containerTable">
							<@s.iterator value="fields" status="status1">
							<TR>
								<@s.iterator value="top" status="status">
									<@s.if test="top!=null">
										<@s.if test="field!=null">
											<@s.if test="labelInsideTemplate">
											     <td colspan="${2*field.colSpan}" rowspan="${field.rowSpan}">
											        <#-- <#include "field/${templateName}"> -->
											        <@s.if test="readOnlyMode">
									        			<#include "../../WEB-INF/classes/plugins/fieldType/templates/readOnly_${templateName}">
											        </@s.if>
											        <@s.else>
											        	<#include "../../WEB-INF/classes/plugins/fieldType/templates/${templateName}">
											        </@s.else>
											     </td>
											</@s.if>
											<@s.else>
											    <td nowrap class="screenFromDataName" align="${labelHAlign}" valign="${labelVAlign}" rowspan="${field.rowSpan}">
				                                    <@s.if test="readOnlyMode">
				                                    	<b><label for="${nameMapping}">${label}&nbsp:</label></b>
				                                    </@s.if>
											        <@s.else>
											        	<label class="<@s.if test="required">required</@s.if>" for="${nameMapping}">${label}&nbsp:</label>
											        </@s.else>				                                    
				                                </td>
				                                <TD nowrap align="${valueHAlign}" valign="${valueVAlign}" class="screenFromDataValue" colspan="${2*field.colSpan-1}" rowspan="${field.rowSpan}">				                                                                        
				                                    <#-- <#include "field/${templateName}"> -->
				                                    <@s.if test="readOnlyMode">
											        	<#include "../../WEB-INF/classes/plugins/fieldType/templates/readOnly_${templateName}">
											        </@s.if>
											        <@s.else>
											        	<#include "../../WEB-INF/classes/plugins/fieldType/templates/${templateName}">
											        </@s.else>
				                                </TD>
										    </@s.else>
										</@s.if>
										<@s.else>
											<#-- empty field -->
											<TD colspan="2">&nbsp;</TD>
										</@s.else>
									</@s.if>
								</@s.iterator>
							</TR>
							</@s.iterator>
						</table>
					</div>
				</@s.iterator><#--end it on panels-->
			</div>
		</@s.iterator><#--end it on tabs--> 	
    </div>    
</@s.else>
