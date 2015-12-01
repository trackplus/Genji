<#list flatHistoryList as x>
  
  <#if x_index=0>
 	<div class="historyRow olist-ulist" style="border-top: 0; margin-left: 0px;margin-right:0px;margin-top:5px;">
  <#else>
	<div class="historyRow olist-ulist" style="margin-left: 0px;margin-right:0px;margin-top:5px;">
  </#if>    
		<table width="100%" border="0" cellPadding="0" cellSpacing="0" style="table-layout:fixed;">
			<tr style="height:0px;padding:0px;border:none;margin:0px;">
				<td style="width:60px;padding:0px;border:none;margin:0px;"></td>
			    <td style="width:20px;padding:0px;border:none;margin:0px;"></td>
				<td style="width:120px;padding:0px;border:none;margin:0px;"></td>
				<td style="width:50%;padding:0px;border:none;margin:0px;"></td>
				<td style="width:50%;padding:0px;border:none;margin:0px;"></td>
			</tr>
			<tr class="dataRow1">
				<td rowspan = 0 style="vertical-align: top">
  					<img width="50" height="50" src="avatar!download.action?personID=${x.personID}"/>
  				</td>
				<td class="dataName" style="padding-bottom:5px">
					<img width="16" height="16" src="${iconsPath}/16x16/${x.iconName}"/>
				</td>
				<td class="dataName" style="width:100%; padding-bottom:5px;" colspan="3">
					${x.lastEdit}&nbsp;&nbsp;${x.changedByName}
				</td>
			</tr>
			<#if x.historyEntries??>
				<#list x.historyEntries as y>
					<tr class="dataRow1">
						<td></td>
						<#if y.changedText?has_content>
							<td class="dataName" colspan="3" style="white-space: normal;overflow:hidden;">
								${y.changedText}
							</td>
						<#else>
							<td class="dataValue" style="white-space: normal;">
								<span class="dataEmphasize">
									<#if y.newValue??>
										${y.newValue}
									</#if>
								</span>
							</td>
							<td class="dataValue" style="white-space: normal;">
								<span class="dataEmphasize">
									<#if y.oldValue??>
										${y.oldValue}
									</#if>
								</span>
							</td>
						</#if>
					</tr>
				</#list>
			</#if>
			<#if x.historyLongEntries??>
				<#list x.historyLongEntries as z>
					<tr>
						<td>
							&nbsp;
						</td>
						<#if z.diff??>
							<td colspan="3" class="dataValue" style="white-space: normal;vertical-align: top;padding-bottom: 2px;">
								${z.diff}
							</td>
						<#else>
							<#if z.fieldLabel?has_content>
								<td colspan="3" class="dataName" style="white-space: normal;height: 19px">
									<strong>
										<#if z.fieldLabel??>
											${z.fieldLabel}
										</#if>
									</strong>
								</td>
							</#if>
							<#if z.newValue?has_content>
								<td colspan="3" class="dataValue" style="white-space: normal;vertical-align: top;padding-bottom: 2px;">
									<#if z.newValue??>
										${z.newValue}
									</#if>
								</td>
							</#if>
							<#if z.oldValue?has_content>
								<td colspan="3" class="dataValue" style="white-space: normal;vertical-align: top;padding-bottom: 2px;">
									<#if z.oldValue??>
										${z.oldValue}
									</#if>
								</td>
							</#if>
						</#if>
					</tr>
				</#list>
			</#if>
		</table>
	</div>
</#list>



