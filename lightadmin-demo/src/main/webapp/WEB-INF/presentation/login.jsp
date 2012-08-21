<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">

	<title><spring:message code="application.name"/></title>

	<link rel="stylesheet" type="text/css" href="<spring:url value="/styles/bootstrap.css"/>" media="screen">
	<link rel="stylesheet" type="text/css" href="<spring:url value="/styles/lightadmin.css"/>" media="screen">

	<!--[if lt IE 9]>
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js" type="text/javascript"></script>
	<![endif]-->
</head>

<body class="login">

<div class="container" style="padding-top: 200px;">

	<div class="row">
		<div class="span6 offset3">
			<div id="login">
				<div class="title">
					<img src="<spring:url value='/images/lightning-icone-7684-32.png'/>"/>
					<spring:message code="application.shortname"/>
				</div>
				<form class="form-horizontal" action="<spring:url value='j_spring_security_check'/>" method="POST">
					<fieldset>
						<div class="control-group">
							<label class="control-label" for="j_username">Username</label>
							<div class="controls">
								<input type="text" id="j_username" name="j_username" placeholder="Username" value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="j_password">Password</label>
							<div class="controls">
								<input type="password" id="j_password" name="j_password" placeholder="Password">
							</div>
						</div>
						<div class="control-group">
							<div class="controls">
								<button type="submit" class="btn">Sign in</button>
							</div>
						</div>
					</fieldset>
				</form>
			</div>
		</div>
	</div>
</div>

</body>
</html>