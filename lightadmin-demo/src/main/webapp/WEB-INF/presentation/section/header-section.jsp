<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="currentUsername" value="Administrator"/>

<div class="navbar navbar-inverse navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container-fluid">
			<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</a>
			<a class="brand" href="<spring:url value='/'/>">
				<img src="<spring:url value='/images/lightning-icone-7684-32.png'/>"/>
				<spring:message code="application.shortname"/>
			</a>
			<div class="nav-collapse collapse" style="padding-top: 5px;">
				<ul class="nav">
					<li class="active"><a href="<spring:url value='/'/>">
						<i class="icon-home icon-white"></i>
						<spring:message code="application.menu.dashboard"/>
					</a></li>
				</ul>
				<ul class="nav pull-right">
					<li id="setting-menu" class="dropdown">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown">
							<i class="icon-cog icon-white"></i>
							<spring:message code="application.menu.settings"/>
							<b class="caret"></b>
						</a>
						<ul class="dropdown-menu">
							<li><a href="#"><spring:message code="application.menu.account-settings"/></a></li>
							<li class="divider"></li>
							<li><a href="#"><spring:message code="application.menu.help"/></a></li>
						</ul>
					</li>
					<li id="user-menu" class="dropdown">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown">
							<i class="icon-user icon-white"></i>
							<c:out value="${currentUsername}"/>
							<b class="caret"></b>
						</a>
						<ul class="dropdown-menu">
							<li><a href="#"><spring:message code="application.menu.profile"/></a></li>
							<li class="divider"></li>
							<li><a href="<spring:url value='/logout'/>"><spring:message code="application.menu.logout"/></a></li>
						</ul>
					</li>
				</ul>
				<form class="navbar-search pull-right">
					<input type="text" class="search-query" placeholder="Search"/>
				</form>
			</div>
		</div>
	</div>
</div>