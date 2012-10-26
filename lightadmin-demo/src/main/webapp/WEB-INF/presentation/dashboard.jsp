<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%@ taglib prefix="light" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/breadcrumb" %>

<breadcrumb:breadcrumb/>

<tiles:useAttribute name="dashboardDomainTypes"/>

<c:if test="${not empty dashboardDomainTypes}">
	<table id="dashboardLinks" class="table">
		<thead>
		<tr>
			<th class="span3">Domain Type</th>
			<th>Records</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach var="dashboardDomainType" items="${dashboardDomainTypes}">
			<tr>
				<td><a href="<spring:url value='${dashboardDomainType.first.link}'/>"><c:out value="${dashboardDomainType.first.value}"/></a></td>
				<td>
					<div class="progress">
						<div class="bar" style="width: ${dashboardDomainType.second}%;"><c:out value="${dashboardDomainType.second}"/></div>
					</div>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</c:if>