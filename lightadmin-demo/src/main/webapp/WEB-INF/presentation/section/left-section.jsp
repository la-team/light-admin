<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:useAttribute name="menuItems"/>
<tiles:useAttribute name="domainTypeName" ignore="true"/>

<ul class="nav nav-list menu-sidenav">
	<c:forEach var="menuItem" items="${menuItems}" varStatus="status">
		<li class="${menuItem.first eq domainTypeName ? 'active' : ''}"><a href="<spring:url value='${menuItem.second}'/>"><i class="icon-chevron-right"></i> <c:out value="${menuItem.first}"/></a></li>
	</c:forEach>
</ul>