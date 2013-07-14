<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:useAttribute name="webContext" ignore="true"/>

<!-- Top navigation bar -->
<div id="topNav">
    <div class="fixed">
        <div class="wrapper">
            <div class="userNav" style="float:left;">
                <ul>
                    <li><a href="<c:out value='${webContext.backToSiteUrl}'/>" title="Back to Site"><img
                            src="<light:url value='/images/icons/topnav/mainWebsite.png'/>"
                            alt=""><span>Back to Site</span></a></li>
                </ul>
            </div>

            <sec:authorize ifAllGranted="ROLE_ADMIN">
                <div class="welcome"><a href="#" title=""><img src="<light:url value='/images/userPic.png'/>"
                                                               alt=""/></a><span>Hello, <sec:authentication
                        property="principal.username"/>!</span></div>
                <div class="userNav">
                    <ul>
                        <li><a href="#" class="not-implemented" title=""><img
                                src="<light:url value='/images/icons/topnav/profile.png'/>" alt=""/><span>Profile</span></a>
                        </li>
                        <li><a href="<c:out value='${webContext.securityLogoutUrl}'/>" title=""><img
                                src="<light:url value='/images/icons/topnav/logout.png'/>"
                                alt=""/><span>Logout</span></a></li>
                    </ul>
                </div>
            </sec:authorize>

            <sec:authorize ifNotGranted="ROLE_ADMIN">
                <div class="userNav">
                    <ul>
                        <li><a href="#" class="not-implemented" title=""><img
                                src="<light:url value='/images/icons/topnav/contactAdmin.png'/>"
                                alt=""><span>Contact admin</span></a></li>
                        <li><a href="#" class="not-implemented" title=""><img
                                src="<light:url value='/images/icons/topnav/help.png'/>"
                                alt=""><span>Help</span></a></li>
                    </ul>
                </div>
            </sec:authorize>
            <div class="fix"></div>
        </div>
    </div>
</div>