<%@ tag import="com.google.common.collect.Iterables" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>

<%@ attribute name="scopes" required="true" rtexprvalue="true" type="org.lightadmin.core.config.domain.scope.ScopesConfigurationUnit"%>

<tiles:useAttribute name="domainTypeAdministrationConfiguration"/>

<c:set var="domainTypeName" value="${domainTypeAdministrationConfiguration.domainTypeName}"/>

<spring:url var="domainRestScopeBaseUrl" value="${light:domainRestScopeBaseUrl(domainTypeName)}" scope="page"/>

<c:set var="tag_scopes_scopeList" value="<%= Iterables.toArray( scopes, org.lightadmin.core.config.domain.scope.ScopeMetadata.class ) %>"/>

<c:if test="${not empty tag_scopes_scopeList}">
	<div class="scopes" id="scopes">
		<ul>
			<c:forEach var="scope" items="${tag_scopes_scopeList}">
				<li>
					<a scope-name="${scope.name}" class="scope ${scope.defaultScope ? 'active green' : 'blue' }" href="#${scope.name}"><c:out value="${scope.name}"/></a>
				</li>
			</c:forEach>
		</ul>
		<div class="fix"></div>
	</div>
</c:if>

<script type="text/javascript">
	function domainRestScopeSearchBaseUrl( scopeName ) {
		return '${domainRestScopeBaseUrl}' + '/' + scopeName + '/search';
	}

	function activeScopeName() {
		return $("a.scope.active" ).attr('scope-name');
	}

	function activateScope( scope ) {
		$("a.scope.active").removeClass("active green").addClass("blue");

		$(scope).addClass("active green" ).removeClass("blue");
	}

	function searchScope( scopeName, search_criteria ) {
		var dataTable = $(document ).data('lightadmin.dataTable');

		var restScopeSearchUrl = domainRestScopeSearchBaseUrl( scopeName );
		if ( typeof(search_criteria) != 'undefined' ) {
			restScopeSearchUrl += '?' + search_criteria;
		}

		dataTable.fnReloadAjax( restScopeSearchUrl );
	}

	$(function() {
		$("a.scope" ).click(function(event) {
			event.preventDefault();

			var dataTable = $(document ).data('lightadmin.dataTable');
			var scopeName = $( this ).attr('scope-name');

			activateScope($( this ));

			if ( $("form[name='filter-form']" ).size() > 0) {
				var filter_criteria = $("form[name='filter-form']" ).serialize();
				searchScope( scopeName, filter_criteria );
			} else {
				searchScope( scopeName );
			}
		});
	});
</script>