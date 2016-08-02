<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" doctype-public="html"/>
	
	<xsl:template match="root">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
				<link rel="stylesheet" type="text/css" href="example.css"/>
				<title>Example HTML page</title>
			</head>
			<body>
				<div>
					<h2>Example page</h2>
				</div>
				<table>
					<thead>
						<tr>
							<th>&#160;</th>
							<xsl:for-each select="headers/header">
								<th><xsl:value-of select="current()/text()"/></th>
							</xsl:for-each>
						</tr>
					</thead>
					<tbody>
						<xsl:for-each select="rows/row">
							<tr class="Row">
								<th><xsl:value-of select="@name"/></th>
								<xsl:for-each select="column">
									<td><xsl:value-of select="current()/text()"/></td>
								</xsl:for-each>
							</tr>
							<tr class="Row rowBottom">
								<td colspan="8"></td>
							</tr>
						</xsl:for-each>
					</tbody>
				</table>
			</body>
		</html>
	</xsl:template>
	
</xsl:stylesheet>
