<%@ page import="org.lightadmin.core.persistence.metamodel.PersistentPropertyType" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="attributeMetadata" type="org.springframework.data.mapping.PersistentProperty" scope="request"/>
<c:set var="enumProperty" value="<%= PersistentPropertyType.forPersistentProperty(attributeMetadata) == PersistentPropertyType.ENUM%>"/>

<select name="${field}">
    <c:if test="${not enumProperty}"><option value=""></option></c:if>
    <c:forEach var="element" items="${elements}">
        <option value="${element.value}">${element.label}</option>
    </c:forEach>
</select>