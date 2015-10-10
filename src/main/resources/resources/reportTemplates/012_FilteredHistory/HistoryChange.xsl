<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
+ new String(new java.text.SimpleDateFormat("dd/MM/yyyy").format($F{startDate}, new StringBuffer(), new java.text.FieldPosition(0)))
-->
<xsl:stylesheet 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
  version="1.0">
  <xsl:output method="xml" indent="yes"/>
	<!-- history change  transformation -->
	<xsl:template match="fieldChange">
		<xsl:element name="fieldChange">
			<!-- 					
        	<xsl:if test="@firstInTransaction">  -->
            	<xsl:element name="changed-by">
                	<xsl:value-of select="changed-by"/>
	            </xsl:element>
				<xsl:element name="changed-at">
	                <xsl:value-of select="changed-at"/>
	            </xsl:element>	           		
          	<!-- </xsl:if>-->
            <xsl:choose>
	        	<xsl:when test="@isDate">
	            	<xsl:element name="labelType">DateChange</xsl:element>
	            	<xsl:element name="labelTypeDateChange"><xsl:value-of select="field"/></xsl:element>
	            	<xsl:element name="firstDateValue">
           				<xsl:value-of select="newValue"/>
           			</xsl:element>
           			<xsl:element name="secondDateValue">           	
           				<xsl:value-of select="oldValue"/>
           			</xsl:element> 			           		
	          	</xsl:when>
	          	<xsl:otherwise>
	            	<xsl:element name="labelType"><xsl:value-of select="field"/></xsl:element>
	            	<xsl:element name="firstValue">
           				<xsl:value-of select="newValue"/>
           			</xsl:element>
           			<xsl:element name="secondValue">           	
           				<xsl:value-of select="oldValue"/>
           			</xsl:element> 				
	          	</xsl:otherwise>
	        </xsl:choose>                                         						            
           	<xsl:element name="firstLabel">
           		NewValue
           	</xsl:element>			
           	<xsl:element name="secondLabel">
           		OldValue
           	</xsl:element>
           	<xsl:element name="change-description"/>                          
		</xsl:element>	
	</xsl:template>	
	<!-- comment -->
	<xsl:template match="commentElement">
		<xsl:element name="commentElement">
			<xsl:element name="changed-by">
                <xsl:value-of select="changed-by"/>
            </xsl:element>
			<xsl:element name="changed-at">
                <xsl:value-of select="changed-at"/>
            </xsl:element>            			
			<xsl:element name="labelType">Comment</xsl:element>            			            
           	<xsl:element name="firstLabel"/>           	       
           	<xsl:element name="firstValue"/>	
           	<xsl:element name="secondLabel"/>           		
           	<xsl:element name="secondValue"/>
           	<xsl:element name="change-description">
                <xsl:value-of select="comment"/>
            </xsl:element>           	           		           
		</xsl:element>	
	</xsl:template>
		
	<!--budget-history-element> transformatiom -->
	<xsl:template match="budget-history-element">
		<xsl:element name="budget-history-element">
			<xsl:element name="changed-by">
                <xsl:value-of select="changed-by"/>
            </xsl:element>
			<xsl:element name="changed-at">
                <xsl:value-of select="changed-at"/>
            </xsl:element>
            <xsl:element name="currency">
                <xsl:value-of select="currency"/>
            </xsl:element>
            <xsl:element name="unit">
                <xsl:value-of select="unit"/>
            </xsl:element>			
			<xsl:element name="labelType">BudgetChanged</xsl:element>			
            <!-- firstLabel-->
           	<xsl:element name="firstLabel">
				Cost
			</xsl:element>
			<!-- firstValue-->
           	<xsl:element name="firstValue">
           		<xsl:value-of select="cost-value"/>
           	</xsl:element>	
			<!-- secondLabel-->
           	<xsl:element name="secondLabel">    
				Effort
			</xsl:element>
			<!-- secondValue-->
           	<xsl:element name="secondValue">
           		<xsl:value-of select="effort-value"/>
           	</xsl:element>
           	<xsl:element name="change-description">
                <xsl:value-of select="change-description"/>
            </xsl:element>
		</xsl:element>
	</xsl:template>
	
	<!--remaining-budget-element> transformatiom -->
	<xsl:template match="remaining-budget-element">
		<xsl:element name="remaining-budget-element">
			<xsl:element name="changed-by">
                <xsl:value-of select="changed-by"/>
            </xsl:element>
			<xsl:element name="changed-at">
                <xsl:value-of select="changed-at"/>
            </xsl:element>
            <xsl:element name="currency">
                <xsl:value-of select="currency"/>
            </xsl:element>
            <xsl:element name="unit">
                <xsl:value-of select="unit"/>
            </xsl:element>			
			<xsl:element name="labelType">RemainingBudget</xsl:element>			
            <!-- firstLabel-->
           	<xsl:element name="firstLabel">
				Cost
			</xsl:element>		
			<!-- firstValue-->
           	<xsl:element name="firstValue">
           		<xsl:value-of select="cost-value"/>
           	</xsl:element>	
			<!-- secondLabel-->
           	<xsl:element name="secondLabel">    
				Effort
			</xsl:element>
			<!-- secondValue-->
           	<xsl:element name="secondValue">
           		<xsl:value-of select="effort-value"/>
           	</xsl:element>
           	<xsl:element name="change-description">
                <xsl:value-of select="change-description"/>
            </xsl:element>
		</xsl:element>
	</xsl:template>
	
	<!--expense-element> transformatiom -->
	<xsl:template match="expense-element">
		<xsl:element name="expense-element">
			<xsl:element name="changed-by">
                <xsl:value-of select="changed-by"/>
            </xsl:element>
			<xsl:element name="changed-at">
                <xsl:value-of select="changed-at"/>
            </xsl:element>
            <xsl:element name="account">
                <xsl:value-of select="account"/>
            </xsl:element>
            <xsl:element name="effortDate">
                <xsl:value-of select="effortDate"/>
            </xsl:element>            
            <xsl:element name="subject">
                <xsl:value-of select="subject"/>
            </xsl:element>
            <xsl:element name="currency">
                <xsl:value-of select="currency"/>
            </xsl:element>
            <xsl:element name="unit">
                <xsl:value-of select="unit"/>
            </xsl:element>			
			<xsl:element name="labelType">Expense</xsl:element>			
            <!-- firstLabel-->
           	<xsl:element name="firstLabel">
				Cost
			</xsl:element>		
			<!-- firstValue-->
           	<xsl:element name="firstValue">
           		<xsl:value-of select="cost-value"/>
           	</xsl:element>	
			<!-- secondLabel-->
           	<xsl:element name="secondLabel">    
				Effort
			</xsl:element>
			<!-- secondValue-->
           	<xsl:element name="secondValue">
           		<xsl:value-of select="effort-value"/>
           	</xsl:element>
           	<xsl:element name="change-description">				
                <xsl:value-of select="change-description"/> 
            </xsl:element>
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
	
</xsl:stylesheet>

