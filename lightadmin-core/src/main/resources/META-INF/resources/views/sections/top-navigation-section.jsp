<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!-- Top navigation bar -->
<div id="topNav">
	<div class="fixed">
		<div class="wrapper">
			<sec:authorize ifAllGranted="admin">
				<div class="welcome"><a href="#" title=""><img src="<spring:url value='/images/userPic.png'/>" alt=""/></a><span>Hello, <sec:authentication
						property="principal.username"/>!</span></div>
				<div class="userNav">
					<ul>
						<li><a href="#" title=""><img src="<spring:url value='/images/icons/topnav/profile.png'/>"
													  alt=""/><span>Profile</span></a></li>
						<li><a href="<spring:url value='/logout'/>" title=""><img
								src="<spring:url value='/images/icons/topnav/logout.png'/>" alt=""/><span>Logout</span></a>
						</li>
					</ul>
				</div>
			</sec:authorize>

			<sec:authorize ifNotGranted="admin">
				<div class="userNav">
					<ul>
						<li><a href="#" title=""><img src="<spring:url value='/images/icons/topnav/register.png'/>"
													  alt=""><span>Register</span></a></li>
						<li><a href="#" title=""><img src="<spring:url value='/images/icons/topnav/contactAdmin.png'/>"
													  alt=""><span>Contact admin</span></a></li>
						<li><a href="#" title=""><img src="<spring:url value='/images/icons/topnav/help.png'/>"
													  alt=""><span>Help</span></a></li>
					</ul>
				</div>
			</sec:authorize>
			<div class="fix"></div>
		</div>
	</div>
</div>