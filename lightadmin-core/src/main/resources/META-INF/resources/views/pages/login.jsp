<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>

<spring:message code="log.me.in" var="log_me_in"/>
<spring:message code="please.enter.your.password" var="please_password"/>
<spring:message code="please.enter.your.name" var="please_name"/>

<div class="loginWrapper" style="width: 320px;">
    <div class="loginLogo"><img src="<light:url value='/dynamic/logo'/>"/></div>

    <c:if test="${not empty param.login_error}">
        <div class="nNote nWarning hideit">
            <p id="alert-message">
                <spring:message code="message.security.bad-credentials"/><br/>
                <strong><spring:message code="message.security.reason"/></strong>&nbsp;<c:out
                    value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
            </p>
        </div>
    </c:if>

    <div class="loginPanel">
        <div class="head"><h5 class="iUser"><spring:message code="login"/></h5></div>
        <form action="<light:url value='/j_spring_security_check'/>" id="login-form" class="mainForm" method="POST">
            <fieldset>
                <div class="loginRow noborder">
                    <label for="req1"><spring:message code="label.username"/>:</label>

                    <div class="loginInput">
                        <input type="text" name="j_username" class="validate[required]" id="req1"
                               placeholder="${please_name}"
                               value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'/>
                    </div>
                    <div class="fix"></div>
                </div>

                <div class="loginRow">
                    <label for="req2"><spring:message code="label.password"/>:</label>

                    <div class="loginInput">
                        <input type="password" name="j_password" class="validate[required]" id="req2"
                               placeholder="${please_password}"/>
                    </div>
                    <div class="fix"></div>
                </div>

                <div class="loginRow">
                    <div class="rememberMe"><input type="checkbox" id="_spring_security_remember_me"
                                                   name="_spring_security_remember_me"/><label
                            for="_spring_security_remember_me">Remember me</label></div>
                    <input id="signIn" type="submit" value="${log_me_in}" class="greyishBtn submitForm"/>

                    <div class="fix"></div>
                </div>
            </fieldset>
        </form>
    </div>
</div>

<script type="text/javascript">
    $(function () {
        $("input[name='j_username']").focus();

        $("select, input:checkbox, input:radio, input:file").uniform();

        $("#login-form").validationEngine();

        $(".hideit").click(function () {
            $(this).fadeTo(200, 0.00, function () { //fade
                $(this).slideUp(300, function () { //slide up
                    $(this).remove(); //then remove from the DOM
                });
            });
        });
    });
</script>