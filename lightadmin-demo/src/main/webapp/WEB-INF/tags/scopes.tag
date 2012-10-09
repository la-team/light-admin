<%@ tag import="com.google.common.collect.Iterables" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>

<%@ attribute name="scopes" required="true" rtexprvalue="true" type="org.lightadmin.core.config.domain.scope.Scopes"%>

<tiles:useAttribute name="domainTypeAdministrationConfiguration"/>

<c:set var="domainTypeName" value="${domainTypeAdministrationConfiguration.domainTypeName}"/>

<spring:url var="domainRestScopeBaseUrl" value="${light:domainRestScopeBaseUrl(domainTypeName)}" scope="page"/>

<c:set var="tag_scopes_scopeList" value="<%= Iterables.toArray( scopes, org.lightadmin.core.config.domain.scope.Scope.class ) %>"/>

<c:if test="${not empty tag_scopes_scopeList}">
	<div class="well well-small">
		<c:forEach var="scope" items="${tag_scopes_scopeList}">
			<a scope-name="${scope.name}" class="scope label ${scope.defaultScope ? 'label-success' : 'label-warning' }" href="#${scope.name}"><c:out value="${scope.name}"/></a>&nbsp;
		</c:forEach>
	</div>
</c:if>

<script type="text/javascript">
	function domainRestScopeBaseUrl( scopeName ) {
		return '${domainRestScopeBaseUrl}' + '/' + scopeName;
	}

	$(function() {
		$("a.scope" ).click(function() {
			var dataTable = $(document ).data('lightadmin.dataTable');
			var scopeName = $( this ).attr('scope-name');
			dataTable.fnReloadAjax( domainRestScopeBaseUrl( scopeName ) );
		});
	});
</script>