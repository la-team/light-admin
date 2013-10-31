<%@ tag import="static com.google.common.collect.Lists.newArrayList" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>

<c:set var="tag_breadcrumb_breadcrumbItems" value="<%= newArrayList() %>" scope="request"/>

<jsp:doBody/>

<div class="breadCrumbHolder module">
    <div class="breadCrumb module">
        <ul id="breadcrumb">
            <li class="firstB">
                <a href="<light:url value="/dashboard"/>"><spring:message code="application.menu.dashboard"/></a></li>

            <c:forEach var="breadcrumbItem" items="${tag_breadcrumb_breadcrumbItems}" varStatus="status">
                <jsp:useBean id="breadcrumbItem" type="org.apache.tiles.beans.MenuItem"/>
                <c:if test="${status.last}">
                    <li class="lastB"><c:out value="${breadcrumbItem.value}"/></li>
                </c:if>
                <c:if test="${not status.last}">
                    <li>
                        <a href="${breadcrumbItem.link}" title="${breadcrumbItem.tooltip}"><c:out value="${breadcrumbItem.value}"/></a>
                    </li>
                </c:if>
            </c:forEach>
        </ul>
    </div>
</div>