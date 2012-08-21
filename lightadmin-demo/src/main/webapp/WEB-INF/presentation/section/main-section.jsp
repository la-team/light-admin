<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="page-header">
	<h1>Entry</h1>
</div>

<ul class="breadcrumb">
	<li><a href="<spring:url value="/"/>"><spring:message code="application.menu.dashboard"/></a></li><span class="divider">/</span>
	<li class="active">Entry</li>
</ul>

<c:choose>
	<c:when test="${not empty entries}">
		<table class="table table-striped table-bordered table-hover">
			<thead>
			<tr>
				<th>#</th>
				<th>Name</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach var="entry" items="${entries}">
				<tr>
					<td><c:out value="${entry.id}"/></td>
					<td><c:out value="${entry.name}" default="N/A" escapeXml="true"/></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</c:when>
	<c:otherwise>
		<p><spring:message code="message.nothing-found"/></p>
	</c:otherwise>
</c:choose>