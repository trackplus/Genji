<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
  version="1.0">
  <xsl:output method="xml" indent="yes"/>

  <xsl:key name="originator_key" match="item" use="Originator"/>
  <xsl:key name="responsible_key" match="item" use="Responsible"/>
  <xsl:key name="manager_key" match="item" use="owner"/>
    
  <xsl:template match="/">
  	<xsl:element name="track-report">
  	<xsl:for-each select="//track-report/item[generate-id(.) = generate-id(key('originator_key', Originator)[1])]">
		<xsl:element name="itemO">
            <xsl:element name="Originator">
                <xsl:value-of select="Originator"/>
            </xsl:element>
            <xsl:variable name="itemsCount" select="//item[Originator=current()/Originator]" />
            <xsl:element name="itemCount">
                <xsl:value-of select="count($itemsCount)"/>
            </xsl:element>
        </xsl:element>
    </xsl:for-each>

  	<xsl:for-each select="//track-report/item[generate-id(.) = generate-id(key('responsible_key', Responsible)[1])]">
		<xsl:element name="itemR">
            <xsl:element name="Responsible">
                <xsl:value-of select="Responsible"/>
            </xsl:element>
            <xsl:variable name="itemsCount" select="//item[Responsible=current()/Responsible]" />
            <xsl:element name="itemCount">
                <xsl:value-of select="count($itemsCount)"/>
            </xsl:element>
          </xsl:element>
    </xsl:for-each>
  
    <xsl:for-each select="//track-report/item[generate-id(.) = generate-id(key('manager_key', owner)[1])]">
		<xsl:element name="itemM">
            <xsl:element name="manager">
                <xsl:value-of select="owner"/>
            </xsl:element>
            <xsl:variable name="itemsCount" select="//item[owner=current()/owner]" />
            <xsl:element name="itemCount">
                <xsl:value-of select="count($itemsCount)"/>
            </xsl:element>
          </xsl:element>
    </xsl:for-each>
   </xsl:element>
  </xsl:template>  
 
</xsl:stylesheet>

