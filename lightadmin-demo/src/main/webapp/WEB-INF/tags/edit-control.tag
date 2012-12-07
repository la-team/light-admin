<jsp:root version="2.0" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:light="http://www.lightadmin.org/tags" xmlns:editor="urn:jsptagdir:/WEB-INF/tags/edit">
	<jsp:directive.attribute name="attributeMetadata" required="true" type="org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata"/>
	<jsp:directive.attribute name="cssClass" required="false" type="java.lang.String" />
	<jsp:directive.attribute name="errorCssClass" required="false" type="java.lang.String" />
	<jsp:directive.tag body-content="empty" trimDirectiveWhitespaces="true"/>
	<light:edit-control-dispatcher attributeMetadata="${attributeMetadata}">
		<jsp:attribute name="simpleEditControl">
			<editor:simple-edit-control attributeMetadata="${attributeMetadata}" cssClass="${cssClass}" errorCssClass="${errorCssClass}"/>
		</jsp:attribute>
		<jsp:attribute name="booleanEditControl">
			<editor:boolean-edit-control attributeMetadata="${attributeMetadata}" cssClass="${cssClass}" errorCssClass="${errorCssClass}"/>
		</jsp:attribute>
		<jsp:attribute name="dateEditControl">
			<editor:date-edit-control attributeMetadata="${attributeMetadata}" cssClass="${cssClass}" errorCssClass="${errorCssClass}"/>
		</jsp:attribute>
		<jsp:attribute name="n2oneEditControl">
			<editor:n2one-edit-control attributeMetadata="${attributeMetadata}" cssClass="${cssClass}" errorCssClass="${errorCssClass}"/>
		</jsp:attribute>
		<jsp:attribute name="n2manyEditControl">
			<editor:n2many-edit-control attributeMetadata="${attributeMetadata}" cssClass="${cssClass}" errorCssClass="${errorCssClass}"/>
		</jsp:attribute>
		<jsp:attribute name="mapEditControl">
			<jsp:text>Map is not supported</jsp:text>
		</jsp:attribute>
	</light:edit-control-dispatcher>
</jsp:root>
