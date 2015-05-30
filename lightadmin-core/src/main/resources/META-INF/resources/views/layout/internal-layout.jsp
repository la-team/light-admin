<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:importAttribute name="screenContext" ignore="true"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta name="robots" content="noindex">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>

    <title><spring:message code="application.name"/>
        <c:out value="${ not empty(screenContext) ? screenContext.screenName : ''}"/></title>

    <link rel="stylesheet" type="text/css" href="<light:url value="/styles/main.css"/>">
    <link rel="stylesheet" type="text/css" href="<light:url value="/styles/lightadmin.css"/>">
    <link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/css?family=Cuprum">

    <tiles:insertAttribute name="common-scripts-include"/>

    <tiles:insertAttribute name="domain-scripts-include" ignore="true"/>
</head>

<body>

<tiles:insertAttribute name="top-navigation-section"/>

<tiles:insertAttribute name="header-section"/>

<div class="wrapper">

    <tiles:insertAttribute name="left-section"/>

    <div class="content">
        <tiles:insertAttribute name="main-section"/>
    </div>

    <div class="fix"></div>
</div>

<tiles:insertAttribute name="footer-section"/>

</body>
</html>