<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/breadcrumb" %>

<tiles:useAttribute name="domainTypeAdministrationConfiguration"/>
<tiles:useAttribute name="fields"/>

<c:set var="domainTypeName" value="${domainTypeAdministrationConfiguration.domainTypeName}"/>

<jsp:useBean id="entity" type="java.lang.Object" scope="request"/>

<c:set var="domainTypeEntityMetadata" value="${domainTypeAdministrationConfiguration.domainTypeEntityMetadata}"/>

<jsp:useBean id="domainTypeEntityMetadata" type="org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata"/>

<spring:url var="domainBaseUrl" value="${light:domainBaseUrl(domainTypeName)}" scope="page"/>

<breadcrumb:breadcrumb>
	<breadcrumb:breadcrumb-item name="List ${domainTypeName}" link="${domainBaseUrl}"/>
	<breadcrumb:breadcrumb-item name="Show ${domainTypeName}"/>
</breadcrumb:breadcrumb>

<c:set var="entityId" value="<%= domainTypeEntityMetadata.getIdAttribute().getValue( entity ) %>"/>

<spring:url var="domainRestEntityBaseUrl" value="${light:domainRestEntityBaseUrl(domainTypeName, entityId)}" scope="page"/>

<div class="page-header">
	<h2>Show <c:out value="${domainTypeName}"/> #<c:out value="${entityId}"/></h2>
</div>

<table class="table table-striped table-bordered table-hover">
	<thead>
	<tr>
		<th>Field Name</th>
		<th>Field Value</th>
	</tr>
	</thead>

	<tbody id="data-section"></tbody>

</table>

<script type="text/javascript">

	function buildShowView( data ) {
		for (var prop in data) {
			if ( prop != 'links' && prop != 'stringRepresentation') {
				var name = data[prop]['name'] !== undefined ? data[prop]['name'] : prop;
				var value = data[prop]['value'] !== undefined ? data[prop]['value'] : data[prop];

				var tr = "<tr><td>" + name + "</td><td>" + renderValue(value) + "</td></tr>";

				$("#data-section" ).append(tr);
			}
		}
	}

	$(function() {
		jQuery.ajax( {
			 "dataType" : 'json',
			 "type" : "GET",
			 "url" : '${domainRestEntityBaseUrl}',
			 "success":function ( data ) {
				 buildShowView( data );
			 }
		 } );
	});
</script>