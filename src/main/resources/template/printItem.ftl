<style>
	.printItemCell{
		padding: 1px;
	}
	.fieldLabel{
		color: #6B6B6B
	}
	#contentPrintItem .flatValueLabel{
		font-weight: bold;
		font-size: 12px;
	}

	td.dataName{
	}
	td.dataValue{
	}
	#printItemScreenDiv .screenPanel-odd {
		background-color: white;
		border: 1px solid #D0D0D0;
	}
	.emphasize {
		color: #BF2128;
		font-weight: bold;
	}
	.dataEmphasize {
		color: green;
	}
</style>
<div id="contentPrintItem">
	<div id="printItemHeader">
		<h4 style="margin: 5px">
			${issueNoLabel}
			<strong>
			 <span class="emphasize">
			   &nbsp;${itemID}
			 </span>:
			<span class="dataEmphasize">
			  ${statusDisplay}
			 </span>
				:&nbsp;${synopsis}
			</strong>
		</h4>
	</div>
	<div id="printItemScreenDiv">

			<#list screenBean.tabs[0].panels as panel>
				<div  style="border:1px solid #C0C0C0;margin: 0 0 5px 5px; padding: 5px">
					<table class="containerTable" border="0" style="width:100%">
						<#list panel.fieldWrappers as fieldRow >
							<TR class="printItemRow">
								<#list fieldRow as fieldWarpper>
									<#if fieldWarpper?has_content>
										<#if fieldWarpper.field?has_content>
											<td width="100px;" style="white-space: nowrap" class="printItemCell fieldLabel"
												align='<s:property value="labelHAlign"/>'
												valign='<s:property value="labelVAlign"/>'
												rowspan='<s:property value="field.rowSpan"/>'>
												${fieldLabels["f_"+fieldWarpper.fieldID?c]}&nbsp:

											</td>
											<td nowrap class="printItemCell fieldValue"
												align='<s:property value="valueHAlign"/>'
												valign='<s:property value="valueVAlign"/>'
												rowspan='<s:property value="field.rowSpan"/>'
												colspan='<s:property value="2*field.colSpan-1"/>' >
												${fieldDisplayValues["f_"+fieldWarpper.fieldID?c]}
											</td>
										<#else>
											<TD>&nbsp;</TD>
										</#if>
									</#if>
								</#list>
							</TR>
						</#list>
					</table>
				</div>
				<#if panel_has_next>
					<div class="containerSeparator">&nbsp;</div>
				</#if>
			</#list>

	</div>
</div>
