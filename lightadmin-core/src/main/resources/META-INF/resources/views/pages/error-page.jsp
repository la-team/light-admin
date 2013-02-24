<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>

<jsp:useBean id="exception" type="java.lang.Exception" scope="request"/>

<div class="wrapper">
	<div class="errorPage">
		<h2 class="red errorTitle"><span>Something went wrong here</span></h2>

		<h1>400</h1>
		<span class="bubbles"></span>

		<p>Oops! Sorry, an error has occured.<br/><c:out value="${exception.message}"/></p>

		<div class="backToDash"><a href="<light:url value='/dashboard'/>" title="Back to Dashboard"
								   class="seaBtn button">Back to Dashboard</a></div>
	</div>
</div>