<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>

<tiles:useAttribute name="domainTypeAdministrationConfiguration" ignore="true"/>

<div id="header" class="wrapper">
	<div class="logo"><a href="<spring:url value='/'/>" title=""><img src="<spring:url value='/images/loginLogo_2-1.png'/>" alt="" /></a></div>
	<div class="middleNav">
		<c:if test="${not empty domainTypeAdministrationConfiguration}">
			<spring:url value="${light:domainBaseUrl(domainTypeAdministrationConfiguration.domainTypeName)}" var="domainBaseUrl" />
			<ul>
				<li class="iCreate"><a href="${domainBaseUrl}/create" title=""><span>Create New</span></a></li>
				<li class="iArchive"><a href="#" title=""><span>Templates</span></a></li>
				<li class="iZipFiles"><a href="#" title=""><span>Export Data</span></a></li>
			</ul>
		</c:if>
	</div>
	<div class="fix"></div>
</div>
