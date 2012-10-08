<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:useAttribute name="screenContext" ignore="true"/>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">

	<title><spring:message code="application.name"/> <c:out value="${ (not empty(screenContext)) and (fn:length(screenContext.screenName) > 0) ? screenContext.screenName : ''}"/></title>

	<link rel="stylesheet" type="text/css" href="<spring:url value="/styles/bootstrap.css"/>" media="screen">
	<link rel="stylesheet" type="text/css" href="<spring:url value="/styles/lightadmin.css"/>" media="screen">
	<link rel="stylesheet" type="text/css" href="<spring:url value="/styles/bootstrap-responsive.css"/>" media="screen">
	<link rel="stylesheet" type="text/css" href="<spring:url value="/styles/dataTables.bootstrapPagination.css"/>" media="screen">

	<script type="text/javascript" src="<spring:url value="/scripts/jquery-1.8.0.min.js"/>"></script>

	<script type="text/javascript" src="<spring:url value="/scripts/bootstrap.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/scripts/jquery.dataTables.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/scripts/dataTables.bootstrapPagination.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/scripts/dataTables.fnReloadAjax.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/scripts/lightadmin.js"/>"></script>

	<!--[if lt IE 9]>
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js" type="text/javascript"></script>
	<![endif]-->
</head>

<body>

<tiles:insertAttribute name="header.section"/>

<div class="container-fluid">

	<div class="row-fluid">
		<div class="span3">
			<tiles:insertAttribute name="left.section"/>
		</div>
		<div class="span9">
			<tiles:insertAttribute name="main.section"/>
		</div>
	</div>
</div>

<tiles:insertAttribute name="footer.section"/>

</body>
</html>