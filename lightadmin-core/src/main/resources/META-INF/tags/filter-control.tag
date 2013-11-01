<%@ tag body-content="empty" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="light-jsp" uri="http://www.lightadmin.org/jsp" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>

<%@ attribute name="domainType" required="true" type="java.lang.Class" %>
<%@ attribute name="attributeMetadata" required="true" type="org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="errorCssClass" required="false" type="java.lang.String" %>

<light:edit-control-dispatcher attributeMetadata="${attributeMetadata}">
    <jsp:attribute name="numberEditControl">
        <light-jsp:number-edit-control attributeMetadata="${attributeMetadata}" cssClass="${cssClass}" allowEmpty="${true}"/>
    </jsp:attribute>
    <jsp:attribute name="simpleEditControl">
        <light-jsp:simple-edit-control attributeMetadata="${attributeMetadata}" cssClass="${cssClass}"/>
    </jsp:attribute>
    <jsp:attribute name="booleanEditControl">
        <light-jsp:boolean-filter-control attributeMetadata="${attributeMetadata}" cssClass="${cssClass}"/>
    </jsp:attribute>
    <jsp:attribute name="dateEditControl">
        <light-jsp:date-edit-control attributeMetadata="${attributeMetadata}" cssClass="${cssClass}"/>
    </jsp:attribute>
    <jsp:attribute name="fileEditControl">
        <jsp:text>File is not supported</jsp:text>
    </jsp:attribute>
    <jsp:attribute name="n2oneEditControl">
        <light-jsp:n2one-edit-control domainType="${domainType}" attributeMetadata="${attributeMetadata}" cssClass="${cssClass}" modalViewEnabled="${false}"/>
    </jsp:attribute>
    <jsp:attribute name="n2manyEditControl">
        <light-jsp:n2many-edit-control domainType="${domainType}" attributeMetadata="${attributeMetadata}" cssClass="${cssClass}" modalViewEnabled="${false}"/>
    </jsp:attribute>
    <jsp:attribute name="mapEditControl">
        <jsp:text>Map is not supported</jsp:text>
    </jsp:attribute>
</light:edit-control-dispatcher>