<%@ tag import="org.lightadmin.core.persistence.metamodel.PersistentPropertyType" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="light-jsp" uri="http://www.lightadmin.org/jsp" %>
<%@ taglib prefix="bean" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ attribute name="domainTypeAdministrationConfiguration" required="true" type="org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration" %>
<%@ attribute name="fields" required="true" type="java.util.Set" %>
<%@ attribute name="scopes" required="true" type="java.util.List" %>

<c:set var="ASSOC" value="<%= PersistentPropertyType.ASSOC %>"/>
<c:set var="ASSOC_MULT" value="<%= PersistentPropertyType.ASSOC_MULTI %>"/>
<c:set var="FILE" value="<%= PersistentPropertyType.FILE %>"/>

<spring:message code="click.for.quick.view" var="click_quick"/>
<spring:message code="no.data.available.in.table" var="no_data"/>
<spring:message code="no.matching.records.found" var="no_matching"/>
<spring:message code="loading" var="loading"/>
<spring:message code="search" var="search"/>
<spring:message code="processing" var="processing"/>
<spring:message code="page.next" var="page_next"/>
<spring:message code="page.prev" var="page_prev"/>
<spring:message code="page.first" var="page_first"/>
<spring:message code="page.last" var="page_last"/>
<spring:message code="are.you.sure" var="are_sure"/>

<spring:message code="edit" var="edit"/>
<spring:message code="remove" var="remove"/>
<spring:message code="view" var="view"/>

<div class="table">
    <div class="head">
        <light-jsp:scopes scopes="${scopes}" domainTypeAdministrationConfiguration="${domainTypeAdministrationConfiguration}"/>
    </div>

    <table cellpadding="0" cellspacing="0" border="0" class="display" id="listViewTable">
        <thead>
        <tr>
            <th class="info"></th>
            <c:forEach var="field" items="${fields}">
                <th class="header"><c:out value="${field.name}"/></th>
            </c:forEach>
            <th><bean:message key="actions"/></th>
        </tr>
        </thead>
        <tbody/>
    </table>
</div>

<script type="text/javascript">
    $(function () {
        var tableElement = $('#listViewTable');

        var dataTable = tableElement.dataTable({
            "bJQueryUI": true,
            "bStateSave": true,
            "sAjaxDataProp": '_embedded.persistentEntityWrappers',
            "aoColumnDefs": [
                {
                    "bSortable": false,
                    "aTargets": [ 0 ],
                    "mData": null,
                    "sClass": "center",
                    "mRender": function () {
                        return '<img class="quickView" src="<light:url value='/images/aNormal.png'/>" style="cursor:pointer;" title="${click_quick}"/>';
                    }
                },
            <c:forEach var="field" items="${fields}" varStatus="status">
                <c:set var="propertyName" value="${field.uuid}"/>
                {
                    "bSortable": ${field.sortable},
                    "aTargets": [ ${status.index + 1 } ],
            <c:choose>
                <c:when test="${(not field.dynamic) and (light:persistentPropertyTypeOf(field.persistentProperty) eq ASSOC or light:persistentPropertyTypeOf(field.persistentProperty) eq ASSOC_MULT)}">
                    "mData": function(source) {
                        var domainEntity = new DomainEntity(source);
                        var propertyMetadata = ConfigurationMetadataService.getProperty(ApplicationConfig.RESOURCE_NAME, '${propertyName}', 'listView');
                        return domainEntity.getPropertyValue(propertyMetadata, 'listView');
                    },
                </c:when>
                <c:otherwise>
                    "mData": '${field.dynamic or light:persistentPropertyTypeOf(field.persistentProperty) eq FILE? 'dynamic_properties.listView.' : 'original_properties.'}${propertyName}',
                </c:otherwise>
            </c:choose>
                    "mRender": function (innerData) {
                        return mRenderFieldValue(innerData, '${propertyName}');
                    },
                    "sClass": "data-cell"
                },
            </c:forEach>
                {
                    "bSortable": false,
                    "aTargets": [ ${fn:length(fields) + 1 } ],
                    "mData": null,
                    "sClass": "center",
                    "mRender": mRenderActions
                }
            ],
            "aaSorting": [
                [1, 'asc']
            ],
            "bServerSide": true,
            "fnServerData": dataTableRESTAdapter,
            "sPaginationType": "full_numbers",
            "oLanguage": {
                "sProcessing": "${processing}",
                "sLengthMenu": "<span class='itemsPerPage'>Items per page:</span> <span style='font-size: 11px;'>_MENU_</span>",
                "sZeroRecords": "${no_matching}",
                "sEmptyTable": "${no_data}",
                "sLoadingRecords": "${loading}",
                "sInfo": "Showing _START_ to _END_ of _TOTAL_ entries",
                "sInfoEmpty": "Showing 0 to 0 of 0 entries",
                "sInfoFiltered": "(_MAX_ in total)",
                "sInfoPostFix": "",
                "sInfoThousands": ",",
                "sSearch": "${search}",
                "sUrl": "",
                "oPaginate": {
                    "sFirst": "${page_first}",
                    "sPrevious": "${page_prev}",
                    "sNext": "${page_next}",
                    "sLast": "${page_last}"
                }
            },
            "sScrollX": "100%",
            "bLengthChange": true,
            "bFilter": false,
            "bInfo": false,
            "sDom": '<""f>t<"F"lp>',
            "fnDrawCallback": fnDrawCallback,
            "fnInitComplete": function () {
                this.fnAdjustColumnSizing();
            }
        });

        getSearcher().addDataTable(dataTable);

        getSearcher().search();

        bindInfoClickHandlers(tableElement, dataTable);

        decorateUIControls($('#listViewTable_length'));

        $(window).bind('resize', function () {
            dataTable.fnAdjustColumnSizing();
        });

    });

    function fnDrawCallback(oSettings) {
        $("#listViewTable").attr("data-loaded", true);
        $("a.removeBtn").click(function () {
            var entityId = $(this).attr('data-entity-id');
            jConfirm('${are_sure}', '${confirm_dialog}', function (r) {
                if (r) {
                    new FormViewController(ApplicationConfig.RESOURCE_NAME).removeDomainEntity(entityId, function () {
                        getSearcher().search();
                    });
                }
            });
        });
        $("a[rel^='prettyPhoto']").prettyPhoto({ social_tools: ''});
    }

    function mRenderFieldValue(innerData, propertyName) {
        var propertyMetadata = ConfigurationMetadataService.getProperty(ApplicationConfig.RESOURCE_NAME, propertyName, 'listView');

        return FieldValueRenderer.render(propertyName, innerData, propertyMetadata['type'], 'listView');
    }

    function mRenderActions(data, type, full) {
        var domainEntity = new DomainEntity(full);
        var primaryKeyProperty = ConfigurationMetadataService.getPrimaryKeyProperty(ApplicationConfig.RESOURCE_NAME);
        var entityId = domainEntity.getPropertyValue(primaryKeyProperty, 'listView');

        var viewEntityUrl = ApplicationConfig.getDomainEntityUrl(ApplicationConfig.RESOURCE_NAME, entityId);
        var editEntityUrl = ApplicationConfig.getEditDomainEntityUrl(ApplicationConfig.RESOURCE_NAME, entityId);

        var viewImg = '<light:url value='/images/icons/dark/info.png'/>';
        var editImg = '<light:url value='/images/icons/dark/pencil.png'/>';
        var removeImg = '<light:url value='/images/icons/dark/basket.png'/>';

        var html = "<a href='" + viewEntityUrl + "' title='${view}' class='btn14 mr5'><img src='" + viewImg + "' alt='${view}'></a>";
        html += "<a href='" + editEntityUrl + "' title='${edit}' class='btn14 mr5'><img src='" + editImg + "' alt='${edit}'></a>";
        html += "<a href='#' title='${remove}' class='btn14 mr5 removeBtn' data-entity-id='" + entityId + "'><img src='" + removeImg + "' alt='${remove}'></a>";

        return html;
    }
</script>