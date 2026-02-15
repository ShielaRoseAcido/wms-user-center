<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" indent="yes"/>

    <xsl:template match="/">
        <select name="role" class="form-control" id="role">
            <xsl:for-each select="wmsUserRoles/roles/role">
                <option>
                    <xsl:attribute name="value">
                        <xsl:value-of select="."/>
                    </xsl:attribute>
                    <xsl:value-of select="."/>
                </option>
            </xsl:for-each>
        </select>
    </xsl:template>
</xsl:stylesheet>