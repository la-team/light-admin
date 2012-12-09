<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="form" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/breadcrumb" %>

<tiles:useAttribute name="domainTypeAdministrationConfiguration"/>

<c:set var="domainTypeName" value="${domainTypeAdministrationConfiguration.domainTypeName}"/>
<spring:url var="domainBaseUrl" value="${light:domainBaseUrl(domainTypeName)}" scope="page"/>
<c:set var="domainTypeEntityMetadata" value="${domainTypeAdministrationConfiguration.domainTypeEntityMetadata}"/>
<jsp:useBean id="domainTypeEntityMetadata" type="org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata"/>
<jsp:useBean id="entity" type="java.lang.Object" scope="request"/>
<c:set var="entityId" value="<%= domainTypeEntityMetadata.getIdAttribute().getValue( entity ) %>"/>
<spring:url var="domainObjectUrl" value="${light:domainRestEntityBaseUrl(domainTypeName, entityId)}" scope="page"/>


<breadcrumb:breadcrumb>
	<breadcrumb:breadcrumb-item name="List ${domainTypeName}" link="${domainBaseUrl}"/>
	<breadcrumb:breadcrumb-item name="Edit ${domainTypeName}"/>
</breadcrumb:breadcrumb>

<div class="page-header">
	<h2>Edit <c:out value="${domainTypeName}"/> #<c:out value="${entityId}"/></h2>
</div>

<form id="editForm" onsubmit="return updateDomainObject(this)" class="form-horizontal">
	<fieldset>
		<c:forEach var="attributeEntry" items="${domainTypeAdministrationConfiguration.domainTypeEntityMetadata.attributes}">
			<div id="${attributeEntry.name}-control-group" class="control-group">
				<label class="control-label" for="${attributeEntry.name}"><c:out value="${light:capitalize(attributeEntry.name)}"/></label>
				<div class="controls">
					<form:edit-control attributeMetadata="${attributeEntry}" cssClass="input-xlarge" errorCssClass="help-inline"/>
				</div>
			</div>
		</c:forEach>
	</fieldset>
	<div class="form-actions">
		<button type="submit" class="btn btn-primary">Save changes</button>
		<button type="button" class="btn" onclick="history.back();">Cancel</button>
	</div>
</form>

<script type="text/javascript">
<!--
loadDomainObject($('#editForm'), '${domainObjectUrl}');
//-->
</script>
