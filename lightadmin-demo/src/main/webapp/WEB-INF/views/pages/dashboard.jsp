<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%@ taglib prefix="light" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/breadcrumb" %>

<div class="title"><h5><spring:message code="application.menu.dashboard"/></h5></div>

<tiles:useAttribute name="dashboardDomainTypes"/>

<c:if test="${not empty dashboardDomainTypes}">
	<div class="widget">
		<div class="head"><h5 class="iChart8">Domains statistics</h5><div class="num"><a id="total-count-change" href="#" class="blueNum">+100500</a></div></div>
		<table id="dashboard-statistics" cellpadding="0" cellspacing="0" width="100%" class="tableStatic">
			<thead>
			<tr>
				<td>Domain Type</td>
				<td width="21%">Amount</td>
				<td width="21%">Changes</td>
			</tr>
			</thead>
			<tbody>
			<c:forEach var="dashboardDomainType" items="${dashboardDomainTypes}">
			<tr id="stat-row-${dashboardDomainType.first.value}" class="stat-row">
				<td><a class="domain-link" href="<spring:url value='${dashboardDomainType.first.link}'/>" title=""><c:out value="${dashboardDomainType.first.value}"/></a></td>
				<td align="center">
					<a href="<spring:url value='${dashboardDomainType.first.link}'/>" title="" class="webStatsLink">
						<span class="record-count"><c:out value="${dashboardDomainType.second}"/></span>
					</a>
				</td>
				<td><span class="statPlus record-count-change">0.<c:out value="${dashboardDomainType.second}"/>%</span></td>
			</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
</c:if>