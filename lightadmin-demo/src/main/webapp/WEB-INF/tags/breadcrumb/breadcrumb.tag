<%@ tag import="static com.google.common.collect.Lists.newArrayList" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="tag_breadcrumb_breadcrumbItems" value="<%= newArrayList() %>" scope="request"/>

<jsp:doBody/>

<ul class="breadcrumb" id="breadcrumb">

	<spring:message var="dashboardLabel" code="application.menu.dashboard" />

	<c:if test="${empty tag_breadcrumb_breadcrumbItems}">
		<li><i class="icon-home"></i><c:out value="${dashboardLabel}"/></li>
	</c:if>

	<c:if test="${not empty tag_breadcrumb_breadcrumbItems}">
		<li><a href="<spring:url value="/"/>" title="${dashboardLabel}"><i class="icon-home"></i><c:out value="${dashboardLabel}"/></a></li>
	</c:if>

	<c:forEach var="breadcrumbItem" items="${tag_breadcrumb_breadcrumbItems}" varStatus="status">
		<jsp:useBean id="breadcrumbItem" type="org.apache.tiles.beans.MenuItem"/>
		<span class="divider">/</span>
		<c:if test="${status.last}">
			<li><c:out value="${breadcrumbItem.value}"/></li>
		</c:if>
		<c:if test="${not status.last}">
			<li><a href="${breadcrumbItem.link}" title="${breadcrumbItem.tooltip}"><c:out value="${breadcrumbItem.value}"/></a></li>
		</c:if>
	</c:forEach>
</ul>