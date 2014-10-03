<%@ tag body-content="empty" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="light-jsp" uri="http://www.lightadmin.org/jsp" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>

<%@ attribute name="domainType" required="true" type="java.lang.Class" %>
<%@ attribute name="fieldMetadata" required="true"
              type="org.lightadmin.core.config.domain.field.PersistentFieldMetadata" %>
<%@ attribute name="customControl" required="false" type="javax.servlet.jsp.tagext.SimpleTag" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="disabled" required="false" type="java.lang.Boolean" %>
<%@ attribute name="errorCssClass" required="false" type="java.lang.String" %>

<c:set var="attributeMetadata" value="${fieldMetadata.persistentProperty}" scope="page"/>
<light:edit-control-dispatcher persistentProperty="${attributeMetadata}" customControl="${fieldMetadata.customControl}">
    <jsp:attribute name="numberEditControl">
        <light-jsp:number-edit-control attributeMetadata="${attributeMetadata}" cssClass="${cssClass}"
                                       disabled="${disabled}"/>
    </jsp:attribute>
    <jsp:attribute name="simpleEditControl">
        <light-jsp:simple-edit-control attributeMetadata="${attributeMetadata}" cssClass="${cssClass}"
                                       disabled="${disabled}"/>
    </jsp:attribute>
    <jsp:attribute name="booleanEditControl">
        <light-jsp:boolean-edit-control attributeMetadata="${attributeMetadata}" cssClass="${cssClass}"
                                        disabled="${disabled}"/>
    </jsp:attribute>
    <jsp:attribute name="fileEditControl">
        <light-jsp:file-edit-control attributeMetadata="${attributeMetadata}" cssClass="${cssClass}"
                                     disabled="${disabled}"/>
    </jsp:attribute>
    <jsp:attribute name="dateEditControl">
        <light-jsp:date-edit-control attributeMetadata="${attributeMetadata}" cssClass="${cssClass}"
                                     disabled="${disabled}"/>
    </jsp:attribute>
    <jsp:attribute name="timeEditControl">
        <light-jsp:time-edit-control attributeMetadata="${attributeMetadata}" cssClass="${cssClass}"
                                     disabled="${disabled}"/>
    </jsp:attribute>
    <jsp:attribute name="dateTimeEditControl">
        <light-jsp:datetime-edit-control attributeMetadata="${attributeMetadata}" cssClass="${cssClass}"
                                     disabled="${disabled}"/>
    </jsp:attribute>
    <jsp:attribute name="n2oneEditControl">
        <light-jsp:n2one-edit-control domainType="${domainType}" attributeMetadata="${attributeMetadata}" title="${fieldMetadata.name}"
                                      cssClass="${cssClass}" disabled="${disabled}" modalViewEnabled="${true}"/>
    </jsp:attribute>
    <jsp:attribute name="n2manyEditControl">
        <light-jsp:n2many-edit-control domainType="${domainType}" attributeMetadata="${attributeMetadata}"
                                       cssClass="${cssClass}" disabled="${disabled}" modalViewEnabled="${true}"/>
    </jsp:attribute>
    <jsp:attribute name="mapEditControl">
        <jsp:text>Map is not supported</jsp:text>
    </jsp:attribute>
</light:edit-control-dispatcher>
<label id="${attributeMetadata.name}-error" for="${attributeMetadata.name}" class="${errorCssClass}"
       style="text-align: left;"></label>