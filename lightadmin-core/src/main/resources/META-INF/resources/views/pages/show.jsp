<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="light-jsp" uri="http://www.lightadmin.org/jsp" %>

<tiles:useAttribute name="domainTypeAdministrationConfiguration"/>
<tiles:useAttribute name="fields"/>

<tiles:useAttribute name="entitySingularName"/>
<tiles:useAttribute name="entityPluralName"/>

<tiles:useAttribute name="entityId"/>

<light:url var="domainBaseUrl" value="${light:domainBaseUrl(domainTypeAdministrationConfiguration)}" scope="page"/>
<light:url var="domainObjectUrl" value="${light:domainRestEntityBaseUrl(domainTypeAdministrationConfiguration, entityId)}" scope="page"/>

<div class="title">
	<h5><c:out value="Show ${light:capitalize(light:cutLongText(entitySingularName))}"/></h5>
</div>

<light-jsp:breadcrumb>
	<light-jsp:breadcrumb-item name="${light:capitalize(light:cutLongText(entityPluralName))}" link="${domainBaseUrl}"/>
	<light-jsp:breadcrumb-item name="${light:capitalize(light:cutLongText(entitySingularName))}"/>
</light-jsp:breadcrumb>

<div class="widget">
	<div class="head">
		<h5 class="iList"><c:out value="${light:capitalize(light:cutLongText(entitySingularName))}"/></h5>

		<div style="float: right;margin-top: 5px;display: inline-block;">
			<a href="${domainBaseUrl}/${entityId}/edit" title="Edit" class="btn14 mr5"><img src="<light:url value='/images/icons/dark/pencil.png'/>" alt="Edit"></a>
		</div>
	</div>
	<table cellpadding="0" cellspacing="0" width="100%" class="tableStatic">
		<tbody id="data-section">
		<c:forEach var="field" items="${fields}" varStatus="status">
			<tr class="${status.first ? 'noborder' : ''}">
				<td width="30%" align="right"><strong><c:out value="${field.name}"/>:</strong></td>
				<td name="field-${field.uuid}">&nbsp;</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</div>

<script type="text/javascript">
	loadDomainObjectForShowView( $( '#data-section' ), '${domainObjectUrl}/unit/showView' );

	<c:if test="${param.updateSuccess}">
	showSuccessMessageNote( '<c:out value="${light:capitalize(entitySingularName)}"/> update operation has been performed successfully!' );
	</c:if>
</script>