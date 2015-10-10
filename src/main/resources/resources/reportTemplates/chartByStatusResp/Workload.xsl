<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
	<xsl:output method="xml" indent="yes" />

	<xsl:key name="responsible_key" match="item" use="responsible" />
	<xsl:key name="status_key" match="item" use="state" />

	<xsl:template match="/">
		<xsl:element name="track-report">
			<xsl:for-each
				select="//track-report/item[generate-id(.) = generate-id(key('responsible_key', responsible)[1])]">
				<xsl:element name="item">
					<xsl:element name="responsible">
						<xsl:value-of select="responsible" />
						<xsl:for-each
							select="//track-report/item[generate-id(.) = generate-id(key('status_key', state)[1])]">
							<xsl:element name="status">
								<xsl:value-of select="state" />
								<xsl:variable name="stCounts"
									select="//item[responsible=current()/responsible and state=current()/state]" />
								<xsl:element name="stCount">
									<xsl:value-of select="count($stCounts)" />
								</xsl:element>
							</xsl:element>
						</xsl:for-each>
					</xsl:element>
				</xsl:element>
			</xsl:for-each>
		</xsl:element>
	</xsl:template>

</xsl:stylesheet>

