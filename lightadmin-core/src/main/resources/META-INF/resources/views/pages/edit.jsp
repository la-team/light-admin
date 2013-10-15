<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="light-jsp" uri="http://www.lightadmin.org/jsp" %>

<tiles:useAttribute name="domainTypeAdministrationConfiguration"/>
<tiles:useAttribute name="domainTypeEntityMetadata"/>

<tiles:useAttribute name="fields"/>

<tiles:useAttribute name="entityId"/>

<tiles:useAttribute name="entitySingularName"/>
<tiles:useAttribute name="entityPluralName"/>

<light:url var="domainBaseUrl" value="${light:domainBaseUrl(domainTypeAdministrationConfiguration)}" scope="page"/>
<light:url var="domainObjectUrl" value="${light:domainRestEntityBaseUrl(domainTypeAdministrationConfiguration, entityId)}" scope="page"/>

<tiles:useAttribute name="dialogMode"/>

<c:set var="dialogMode" value="<%= Boolean.valueOf((String) dialogMode) %>"/>

<c:set var="domainTypeName" value="${domainTypeAdministrationConfiguration.domainTypeName}" scope="page"/>

<c:if test="${not dialogMode}">
    <div class="title">
        <h5><c:out value="Edit ${light:capitalize(light:cutLongText(entitySingularName))}"/></h5>
    </div>

    <light-jsp:breadcrumb>
        <light-jsp:breadcrumb-item name="${light:capitalize(light:cutLongText(entityPluralName))}" link="${domainBaseUrl}"/>
        <light-jsp:breadcrumb-item name="${light:capitalize(light:cutLongText(entitySingularName))}"/>
    </light-jsp:breadcrumb>
</c:if>

<form id="${domainTypeName}-form" class="mainForm">
    <div class="widget">
        <div class="head">
            <h5 class="iCreate">
                <c:out value="${light:capitalize(light:cutLongText(entitySingularName))}"/>
            </h5>
        </div>
        <fieldset>
            <c:forEach var="fieldEntry" items="${fields}" varStatus="status">
                <div id="${fieldEntry.uuid}-control-group" class="rowElem ${status.first ? 'noborder' : ''}">
                    <label><strong><c:out value="${light:capitalize(fieldEntry.name)}"/>:</strong></label>

                    <div class="formRight">
                        <light-jsp:edit-control fieldMetadata="${fieldEntry}" cssClass="input-xlarge" errorCssClass="error" disabled="${fieldEntry.primaryKey}"/>
                    </div>
                    <div class="fix"></div>
                </div>
            </c:forEach>
        </fieldset>
        <div class="wizNav">
            <input name="cancel-changes" class="basicBtn" value="Cancel" type="button">
            <input name="save-changes" class="blueBtn" value="Save changes" type="button">
        </div>
    </div>
</form>

<script type="text/javascript">
    $(function () {
        var domain_form = $("#${domainTypeName}-form");

        formViewVisualDecoration(domain_form);

        $(domain_form).data('lightadmin.domain-type-metadata', <light:domain-type-metadata-json domainTypeMetadata="${domainTypeEntityMetadata}"  includeFields="${fields}"/>);
        $(domain_form).data('lightadmin.domain-rest-base-url', "${domainObjectUrl}");

        loadDomainObjectForFormView($(domain_form));

        <c:if test="${not dialogMode}">
        $(":button[name='cancel-changes']", $(domain_form)).click(function () {
            history.back();
        });

        $(":button[name='save-changes']", $(domain_form)).click(function () {
            $(domain_form).submit();
        });

        $(domain_form).submit(function () {
            return updateDomainObject(this, function (data) {
                var link = $.grep(data.links, function (link) {
                    return link.rel == 'selfDomainLink';
                })[0];
                window.location = link.href + '?updateSuccess=true';
            });
        });
        </c:if>
    });
</script>