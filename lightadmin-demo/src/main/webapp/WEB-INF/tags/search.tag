<%@ tag import="com.google.common.collect.Iterables" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="filters" required="true" rtexprvalue="true" type="org.lightadmin.core.view.support.filter.Filters"%>

<c:set var="tag_search_filterList" value="<%= Iterables.toArray( filters, org.lightadmin.core.view.support.filter.Filter.class ) %>"/>

<c:if test="${not empty tag_search_filterList}">
	<div class="well well-small">
		<table class="table table-striped table-bordered table-hover">
			<thead>
			<tr>
				<th>Filter attribute name</th>
				<th>Filter attribute type</th>
			</tr>
			</thead>

			<tbody>
			<c:forEach var="filter" items="${tag_search_filterList}">
				<jsp:useBean id="filter" type="org.lightadmin.core.view.support.filter.Filter"/>
				<tr>
					<td><c:out value="${filter.fieldName}"/></td>
					<td><c:out value="<%= filter.getAttributeMetadata().type().getName() %>"/></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
</c:if>