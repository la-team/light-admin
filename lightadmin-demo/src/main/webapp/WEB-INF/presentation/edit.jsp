<%@ page import="org.lightadmin.core.config.DomainTypeAdministrationConfiguration" %>
<%@ page import="org.springframework.util.StringUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/breadcrumb" %>

<jsp:useBean id="entity" type="java.lang.Object" scope="request"/>

<tiles:useAttribute name="domainTypeAdministrationConfiguration"/>

<c:set var="domainTypeName" value="${domainTypeAdministrationConfiguration.domainTypeName}"/>
<c:set var="entityAttributes" value="<%= ((DomainTypeAdministrationConfiguration) domainTypeAdministrationConfiguration).getDomainTypeEntityMetadata().getAttributes().values() %>"/>

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
			<jsp:useBean id="attributeEntry" type="org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata"/>

			<div class="control-group">
				<label class="control-label" for="${attributeEntry.name}"><c:out value="<%= StringUtils.capitalize( attributeEntry.getName() ) %>"/></label>
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