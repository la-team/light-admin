<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>

<%@ attribute name="scopes" required="true" rtexprvalue="true" type="java.util.List" %>
<%@ attribute name="domainTypeAdministrationConfiguration" required="true" type="org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration" %>

<light:url var="domainRestScopeBaseUrl" value="${light:domainRestScopeBaseUrl(domainTypeAdministrationConfiguration)}" scope="page"/>

<c:if test="${not empty scopes}">
	<div class="scopes" id="scopes">
		<ul>
			<c:forEach var="scope" items="${scopes}">
				<li>
					<a scope-name="${scope.first.name}"
					   class="scope ${scope.first.defaultScope ? 'active green' : 'blue' }" href="#${scope.first.name}"><c:out
							value="${scope.first.name}"/>&nbsp;(<c:out value="${scope.second}"/>)</a>
				</li>
			</c:forEach>
		</ul>
		<div class="fix"></div>
	</div>

	<script type="text/javascript">
		var SCOPES_COMPONENT = new ScopesComponent( '#scopes', getSearcher(), '${domainRestScopeBaseUrl}' );
	</script>
</c:if>