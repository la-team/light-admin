<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:useAttribute name="menuItems"/>
<tiles:useAttribute name="selectedMenuItemName" ignore="true"/>

<!-- Left navigation -->
<div class="leftNav">
	<ul id="menu">
		<li class="dash"><a href="<light:url value='/dashboard'/>" title=""
							class="${empty selectedMenuItemName ? 'active' : ''}"><span><spring:message
				code="application.menu.dashboard"/></span></a></li>
		<c:forEach var="menuItem" items="${menuItems}" varStatus="status">
			<li class="typo"><a href="<light:url value='${menuItem.link}'/>" title=""
								class="${(not empty(selectedMenuItemName)) and (menuItem.value eq selectedMenuItemName) ? 'active' : ''}"><span><c:out
					value="${menuItem.value}"/></span></a></li>
		</c:forEach>
	</ul>
</div>