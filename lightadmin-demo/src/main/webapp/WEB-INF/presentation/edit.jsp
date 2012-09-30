<%@ page import="org.springframework.data.rest.repository.EntityMetadata" %>
<%@ page import="org.springframework.util.StringUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/breadcrumb" %>

<tiles:useAttribute name="domainTypeName"/>
<tiles:useAttribute name="entityMetadata"/>

<jsp:useBean id="entity" type="java.lang.Object" scope="request"/>

<c:set var="entityAttributes" value="<%= ((EntityMetadata) entityMetadata).embeddedAttributes().values() %>"/>

<spring:url var="domainBaseUrl" value="/domain" scope="page"/>

<breadcrumb:breadcrumb>
	<breadcrumb:breadcrumb-item name="List ${domainTypeName}" link="${domainBaseUrl}/${domainTypeName}"/>
	<breadcrumb:breadcrumb-item name="Edit ${domainTypeName}"/>
</breadcrumb:breadcrumb>

<div class="page-header">
	<h2>Edit <c:out value="${domainTypeName}"/></h2>
</div>

<form:form modelAttribute="entity" cssClass="form-horizontal" method="POST">
	<fieldset>
		<c:forEach var="attributeEntry" items="${entityAttributes}">
			<jsp:useBean id="attributeEntry" type="org.springframework.data.rest.repository.jpa.JpaAttributeMetadata"/>

			<div class="control-group">
				<label class="control-label" for="<%= attributeEntry.name()%>"><c:out value="<%= StringUtils.capitalize( attributeEntry.name() ) %>"/></label>
				<div class="controls">
					<light:input attributeMetadata="${attributeEntry}" cssClass="input-xlarge"/>
				</div>
			</div>
		</c:forEach>
	</fieldset>
	<div class="form-actions">
		<button type="submit" class="btn btn-primary">Save changes</button>
		<button type="button" class="btn">Cancel</button>
	</div>
</form:form>