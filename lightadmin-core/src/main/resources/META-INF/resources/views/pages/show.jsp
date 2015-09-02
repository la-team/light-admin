<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="light-jsp" uri="http://www.lightadmin.org/jsp" %>
<%@ taglib prefix="bean" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tiles:importAttribute name="domainTypeAdministrationConfiguration"/>
<tiles:importAttribute name="fields"/>

<tiles:importAttribute name="entitySingularName"/>
<tiles:importAttribute name="entityPluralName"/>

<tiles:importAttribute name="entityId"/>

<light:url var="domainBaseUrl" value="${light:domainBaseUrl(domainTypeAdministrationConfiguration)}" scope="page"/>
<spring:message code="edit" var="edit"/>
<spring:message code="remove" var="remove"/>
<spring:message code="confirmation.dialog" var="confrmation_dialog"/>
<spring:message code="are.you.sure" var="are_you_sure"/>
<spring:message code="update.operation.has.been.performed.successfully" var="update_successfully"/>

<div class="title">
    <h5><c:out value="Show ${light:capitalize(light:cutLongText(entitySingularName))}"/></h5>
</div>

<light-jsp:breadcrumb>
    <light-jsp:breadcrumb-item name="${light:capitalize(light:cutLongText(entityPluralName))}" link="${domainBaseUrl}"/>
    <light-jsp:breadcrumb-item name="${light:capitalize(light:cutLongText(entitySingularName))}"/>
</light-jsp:breadcrumb>

<div class="widget">
    <div class="head">
        <h5 class="iList"><c:out value="${light:capitalize(light:cutLongText(entitySingularName))}"/></h5>

        <div style="float: right;margin-top: 5px;display: inline-block;">
            <a href="${domainBaseUrl}/${entityId}/edit" title="${edit}" class="btn14 mr5"><img src="<light:url value='/images/icons/dark/pencil.png'/>" alt="${edit}"></a>
            <a href="javascript:void(0);" title="${edit}" class="btn14 mr5 remove_button"><img src="<light:url value='/images/icons/dark/basket.png'/>" alt="${remove}"></a>
        </div>
    </div>
    <table cellpadding="0" cellspacing="0" width="100%" class="tableStatic">
        <tbody id="data-section">
        <c:forEach var="field" items="${fields}" varStatus="status">
            <tr class="${status.first ? 'noborder' : ''}">
                <td width="30%" align="right"><strong><c:out value="${field.name}"/>:</strong></td>
                <td name="field-${field.uuid}">&nbsp;</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<script type="text/javascript">
    ShowViewController.handle($('#data-section'), '${entityId}');

    <c:if test="${not empty (param['updateSuccess'])}">
    NotificationController.showSuccessMessageNote('<c:out value="${light:capitalize(entitySingularName)}"/> ${update_successfully}');
    </c:if>

    $(function () {
        var entity_id = '${entityId}';

        $("a.remove_button").click(function () {
            jConfirm('${are_you_sure}', '${confrmation_dialog}', function (r) {
                if (r) {
                    new FormViewController(ApplicationConfig.RESOURCE_NAME).removeDomainEntity(entity_id, function() {
                        window.location = ApplicationConfig.getDomainEntityCollectionUrl(ApplicationConfig.RESOURCE_NAME);
                    });
                }
            });
        });
    });
</script>