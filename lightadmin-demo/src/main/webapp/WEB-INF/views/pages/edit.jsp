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
<c:set var="formViewFields" value="${domainTypeAdministrationConfiguration.formViewFragment.fields}"/>
<jsp:useBean id="domainTypeEntityMetadata" type="org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata"/>
<jsp:useBean id="entity" type="java.lang.Object" scope="request"/>
<c:set var="entityId" value="<%= domainTypeEntityMetadata.getIdAttribute().getValue( entity ) %>"/>
<spring:url var="domainObjectUrl" value="${light:domainRestEntityBaseUrl(domainTypeName, entityId)}" scope="page"/>

<div class="title"><h5>Edit <c:out value="${light:capitalize(domainTypeName)}"/> #<c:out value="${entityId}"/></h5></div>

<breadcrumb:breadcrumb>
	<breadcrumb:breadcrumb-item name="List ${domainTypeName}" link="${domainBaseUrl}"/>
	<breadcrumb:breadcrumb-item name="Edit ${domainTypeName}"/>
</breadcrumb:breadcrumb>

<form id="editForm" onsubmit="return updateDomainObject(this)" class="mainForm">
	<div class="widget">
		<div class="head"><h5 class="iCreate"><c:out value="${light:capitalize(domainTypeName)}"/> #<c:out value="${entityId}"/></h5></div>
		<fieldset>
			<c:forEach var="fieldEntry" items="${formViewFields}" varStatus="status">
				<c:if test="${!fieldEntry.primaryKey}">
				<div id="${fieldEntry.uuid}-control-group" class="rowElem ${status.first ? 'noborder' : ''}">
					<label><c:out value="${light:capitalize(fieldEntry.name)}"/>:</label>
					<div class="formRight">
						<form:edit-control attributeMetadata="${fieldEntry.attributeMetadata}" cssClass="input-xlarge" errorCssClass="error"/>
					</div>
					<div class="fix"></div>
				</div>
				</c:if>
			</c:forEach>
		</fieldset>
		<div class="wizNav">
			<input id="cancel-changes" class="basicBtn" value="Cancel" type="button" onclick="history.back();">
			<input id="save-changes" class="blueBtn" value="Save changes" type="submit">
		</div>
	</div>
</form>

<script type="text/javascript">
	$(function() {
		$(".chzn-select").chosen();

		$("select, input:checkbox, input:radio, input:file").uniform();

		DOMAIN_TYPE_METADATA = <light:domain-type-metadata-json domainTypeMetadata="${domainTypeEntityMetadata}" includeFields="${formViewFields}"/>;

		loadDomainObjectForFormView($('#editForm'), '${domainObjectUrl}');
	});
</script>
