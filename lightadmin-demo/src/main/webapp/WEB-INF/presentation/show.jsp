<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/breadcrumb" %>

<tiles:useAttribute name="domainTypeAdministrationConfiguration"/>

<c:set var="domainTypeName" value="${domainTypeAdministrationConfiguration.domainTypeName}"/>

<jsp:useBean id="entity" type="java.lang.Object" scope="request"/>

<c:set var="domainTypeEntityMetadata" value="${domainTypeAdministrationConfiguration.domainTypeEntityMetadata}"/>

<jsp:useBean id="domainTypeEntityMetadata" type="org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata"/>

<spring:url var="domainBaseUrl" value="/domain" scope="page"/>

<breadcrumb:breadcrumb>
	<breadcrumb:breadcrumb-item name="List ${domainTypeName}" link="${domainBaseUrl}/${domainTypeName}"/>
	<breadcrumb:breadcrumb-item name="Show ${domainTypeName}"/>
</breadcrumb:breadcrumb>

<div class="page-header">
	<h2>Show <c:out value="${domainTypeName}"/></h2>
</div>

<table class="table table-striped table-bordered table-hover">
	<thead>
	<tr>
		<th>Attribute Name</th>
		<th>Attribute Type</th>
		<th>Attribute Value</th>
	</tr>
	</thead>

	<tbody>
	<tr>
		<td><c:out value="${domainTypeEntityMetadata.idAttribute.name}"/></td>
		<td><c:out value="${domainTypeEntityMetadata.idAttribute.type.name}"/></td>
		<td><c:out value="<%= domainTypeEntityMetadata.getIdAttribute().getValue( entity ) %>"/></td>
	</tr>
	<c:forEach var="attributeEntry" items="${domainTypeAdministrationConfiguration.domainTypeEntityMetadata.attributes}">
		<jsp:useBean id="attributeEntry" type="org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata"/>
		<tr>
			<td><c:out value="${attributeEntry.name}"/></td>
			<td><c:out value="${attributeEntry.type.name}"/></td>
			<td><c:out value="<%= attributeEntry.getValue( entity ) %>"/></td>
		</tr>
	</c:forEach>
	</tbody>
</table>