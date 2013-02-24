<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>

<div class="wrapper">
	<div class="errorPage">
		<h2 class="red errorTitle"><span>Something went wrong here</span></h2>

		<h1>403</h1>
		<span class="bubbles"></span>

		<p>Oops! Sorry, an error has occured.<br/> Access forbidden!</p>

		<div class="backToDash"><a href="<light:url value='/login'/>" title="Back to Login" class="seaBtn button">Back
			to Login</a></div>
	</div>
</div>