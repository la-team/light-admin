<%@ page import="org.springframework.util.StringUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>

<tiles:useAttribute name="domainTypeName"/>

<jsp:useBean id="entityMetadata" type="org.springframework.data.rest.repository.EntityMetadata" scope="request"/>
<jsp:useBean id="entity" type="java.lang.Object" scope="request"/>

<c:set var="entityAttributes" value="<%= entityMetadata.embeddedAttributes().values() %>"/>

<ul class="breadcrumb">
	<li><a href="<spring:url value="/"/>"><spring:message code="application.menu.dashboard"/></a></li><span class="divider">/</span><li class="active"><c:out value="${domainTypeName}"/></li>
</ul>

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