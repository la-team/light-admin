<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="bean" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tiles:importAttribute name="dashboardDomainTypes"/>

<div class="title">
	<h5><spring:message code="application.menu.dashboard"/></h5>
</div>

<div class="widget">
	<div class="head"><h5 class="iChart8"><bean:message key="domain.type.statistics"/></h5></div>
	<c:if test="${not empty dashboardDomainTypes}">
		<table id="dashboard-statistics" cellpadding="0" cellspacing="0" width="100%" class="tableStatic">
			<thead>
			<tr>
				<td><bean:message key="domain.type"/></td>
				<td width="21%"><bean:message key="amount"/></td>
			</tr>
			</thead>
			<tbody>
			<c:forEach var="dashboardDomainType" items="${dashboardDomainTypes}">
				<tr id="stat-row-${dashboardDomainType.first.value}" class="stat-row">
					<td style="padding-left: 35px;">
						<a class="domain-link" href="<light:url value='${dashboardDomainType.first.link}'/>">
							<c:out value="${dashboardDomainType.first.value}"/>
						</a>
					</td>
					<td align="center">
						<a href="<light:url value='${dashboardDomainType.first.link}'/>" class="webStatsLink">
							<span class="record-count"><c:out value="${dashboardDomainType.second}"/></span>
						</a>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</c:if>
	<c:if test="${empty dashboardDomainTypes}">
		<div class="body aligncenter">
			No Domain Type <strong>@Administration</strong> configurations registered.
		</div>
	</c:if>
</div>