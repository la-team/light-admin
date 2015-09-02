<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="light-jsp" uri="http://www.lightadmin.org/jsp" %>

<tiles:importAttribute name="domainTypeAdministrationConfiguration"/>
<tiles:importAttribute name="persistentEntity"/>

<tiles:importAttribute name="entitySingularName"/>
<tiles:importAttribute name="entityPluralName"/>

<tiles:importAttribute name="fields"/>

<tiles:importAttribute name="dialogMode" ignore="true"/>

<light:url var="domainBaseUrl" value="${light:domainBaseUrl(domainTypeAdministrationConfiguration)}"/>

<c:set var="dialogMode" value="${dialogMode eq null ? false : true}"/>
<c:set var="domainTypeFormName" value="${domainTypeAdministrationConfiguration.pluralDomainTypeName}${dialogMode ? '-dialog-form' : '-form'}"/>
<spring:message code="cancel" var="cancel"/>
<spring:message code="save.changes" var="save_changes"/>

<c:if test="${not dialogMode}">
    <div class="title">
        <h5><c:out value="Create ${light:capitalize(light:cutLongText(entitySingularName))}"/></h5>
    </div>

    <light-jsp:breadcrumb>
        <light-jsp:breadcrumb-item name="${light:capitalize(light:cutLongText(entityPluralName))}" link="${domainBaseUrl}"/>
        <light-jsp:breadcrumb-item name="${light:capitalize(light:cutLongText(entitySingularName))}"/>
    </light-jsp:breadcrumb>
</c:if>

<form id="${domainTypeFormName}" class="mainForm">
    <div class="widget">
        <div class="head"><h5 class="iCreate">
            <c:out value="${light:capitalize(light:cutLongText(entitySingularName))}"/></h5></div>
        <fieldset>
            <c:forEach var="fieldEntry" items="${fields}" varStatus="status">
                <c:if test="${!fieldEntry.generatedValue}">
                    <div id="${fieldEntry.uuid}-control-group" class="rowElem ${status.first ? 'noborder' : ''}">
                        <label>
                            <strong>
                                <c:out value="${light:capitalize(fieldEntry.name)}"/>:<c:if
                                    test="${fieldEntry.required}"><span class="req">*</span></c:if>
                            </strong>
                        </label>

                        <div class="formRight">
                            <light-jsp:edit-control domainType="${domainTypeAdministrationConfiguration.domainType}"
                                                    fieldMetadata="${fieldEntry}" cssClass="input-xlarge"
                                                    errorCssClass="error"/>
                        </div>
                        <div class="fix"></div>
                    </div>
                </c:if>
            </c:forEach>
        </fieldset>
        <div class="wizNav">
            <input name="cancel-changes" class="basicBtn" value="${cancel}" type="button">
            <input name="save-changes" class="blueBtn" value="${save_changes}" type="button">
        </div>
    </div>
</form>

<script type="text/javascript">
    $(function () {
        var domain_form = $("#${domainTypeFormName}");

        formViewVisualDecoration($(domain_form));

        new FormViewController(ApplicationConfig.RESOURCE_NAME).loadDomainEntity(null, $(domain_form));

        <c:if test="${not dialogMode}">
        $(":button[name='cancel-changes']", $(domain_form)).click(function () {
            history.back();
        });

        $(":button[name='save-changes']", $(domain_form)).click(function () {
            $(domain_form).submit();
        });

        $(domain_form).submit(function () {
            return new FormViewController(ApplicationConfig.RESOURCE_NAME).saveDomainEntity(this, function(domainEntity) {
                window.location = domainEntity.getDomainLink() + '?updateSuccess=true';
            });
        });
        </c:if>
    });
</script>