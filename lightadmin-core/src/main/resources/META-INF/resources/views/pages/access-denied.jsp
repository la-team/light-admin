<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="bean" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="back.to.login" var="back_to_login"/>

<div class="wrapper">
	<div class="errorPage">
		<h2 class="red errorTitle"><span><bean:message key="something.went.wrong.here"/></span></h2>

		<h1>403</h1>
		<span class="bubbles"></span>

		<p><bean:message key="oops.sorry.an.error.has.occured"/><br/><bean:message key="access.forbidden"/></p>

		<div class="backToDash"><a href="<light:url value='/login'/>" title="${back_to_login}" class="seaBtn button"><bean:message key="back.to.login"/></a></div>
	</div>
</div>