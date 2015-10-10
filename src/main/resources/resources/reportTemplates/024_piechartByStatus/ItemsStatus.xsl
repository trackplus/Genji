<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
  version="1.0">
  <xsl:output method="xml" indent="yes"/>

  <xsl:key name="status_key" match="item" use="Status"/>
    
  <xsl:template match="/">
  	<xsl:element name="track-report">
  	<xsl:for-each select="//track-report/item[generate-id(.) = generate-id(key('status_key', Status)[1])]">
		<xsl:element name="itemO">
            <xsl:element name="Status">
                <xsl:value-of select="Status"/>
            </xsl:element>
            <xsl:variable name="itemsCount" select="//item[Status=current()/Status]" />
            <xsl:element name="itemCount">
                <xsl:value-of select="count($itemsCount)"/>
            </xsl:element>
        </xsl:element>
    </xsl:for-each>
   </xsl:element>
  </xsl:template>  
 
</xsl:stylesheet>

