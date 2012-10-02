<%@ tag import="com.google.common.collect.Iterables" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="scopes" required="true" rtexprvalue="true" type="org.lightadmin.core.view.support.scope.Scopes"%>

<c:set var="tag_scopes_scopeList" value="<%= Iterables.toArray( scopes, org.lightadmin.core.view.support.scope.Scope.class ) %>"/>

<c:if test="${not empty tag_scopes_scopeList}">
	<div class="well well-small">
		<c:forEach var="scope" items="${tag_scopes_scopeList}">
			<a class="label ${scope.defaultScope ? 'label-success' : 'label-warning' }" href="#${scope.name}"><c:out value="${scope.name}"/></a>&nbsp;
		</c:forEach>
	</div>
</c:if>
