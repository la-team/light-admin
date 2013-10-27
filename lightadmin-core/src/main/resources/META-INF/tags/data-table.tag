<%@ tag import="org.lightadmin.core.config.domain.field.FieldMetadataUtils" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="light-jsp" uri="http://www.lightadmin.org/jsp" %>

<%@ attribute name="domainTypeAdministrationConfiguration" required="true" type="org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration" %>
<%@ attribute name="fields" required="true" type="java.util.Set" %>
<%@ attribute name="scopes" required="true" type="java.util.List" %>

<c:set var="primaryKeyField" value="<%= FieldMetadataUtils.primaryKeyPersistentField( fields ) %>"/>

<light:url var="domainBaseUrl" value="${light:domainBaseUrl(domainTypeAdministrationConfiguration)}"/>
<light:url var="domainRestUrl" value="${light:domainRestBaseUrl(domainTypeAdministrationConfiguration)}" scope="page"/>

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
            <th>Actions</th>
        </tr>
        </thead>
        <tbody/>
    </table>
</div>

<script type="text/javascript">
    function viewEntityUrl(entityId) {
        return '${domainBaseUrl}' + '/' + entityId;
    }

    function editEntityUrl(entityId) {
        return '${domainBaseUrl}' + '/' + entityId + '/edit';
    }

    $(function () {
        var tableElement = $('#listViewTable');

        var dataTable = tableElement.dataTable({
            "bJQueryUI": true,
            "bStateSave": true,
            "sAjaxDataProp": 'content',
            "aoColumnDefs": [
                {
                    "bSortable": false,
                    "aTargets": [ 0 ],
                    "mData": null,
                    "sClass": "center",
                    "mRender": function (data, type, full) {
                        return '<img class="quickView" src="<light:url value='/images/aNormal.png'/>" style="cursor:pointer;" title="Click for Quick View"/>';
                    }
                },
                <c:forEach var="field" items="${fields}" varStatus="status">
                {
                    "bSortable": ${field.sortable},
                    "aTargets": [ ${status.index + 1 } ],
                    "mData": '${field.uuid}',
                    "mRender": function (data) {
                        return FieldValueRenderer.render(data, 'listView');
                    },
                    "sClass": "data-cell"
                },
                </c:forEach>
                {
                    "bSortable": false,
                    "aTargets": [ ${fn:length(fields) + 1 } ],
                    "mData": null,
                    "sClass": "center",
                    "mRender": function (data, type, full) {
                        var entityId = full['${primaryKeyField.uuid}']['value'];
                        return renderActions(entityId);
                    }
                }
            ],
            "aaSorting": [
                [1, 'asc']
            ],
            "bServerSide": true,
            "fnServerData": dataTableRESTAdapter,
            "sPaginationType": "full_numbers",
            "oLanguage": {
                "sProcessing": "Processing...",
                "sLengthMenu": "<span class='itemsPerPage'>Items per page:</span> <span style='font-size: 11px;'>_MENU_</span>",
                "sZeroRecords": "No matching records found",
                "sEmptyTable": "No data available in table",
                "sLoadingRecords": "Loading...",
                "sInfo": "Showing _START_ to _END_ of _TOTAL_ entries",
                "sInfoEmpty": "Showing 0 to 0 of 0 entries",
                "sInfoFiltered": "(_MAX_ in total)",
                "sInfoPostFix": "",
                "sInfoThousands": ",",
                "sSearch": "Search:",
                "sUrl": "",
                "oPaginate": {
                    "sFirst": "First",
                    "sPrevious": "Prev",
                    "sNext": "Next",
                    "sLast": "Last"
                }
            },
            "bLengthChange": true,
            "bFilter": false,
            "bInfo": false,
            "sDom": '<""f>t<"F"lp>',
            "fnDrawCallback": function (oSettings) {
                $("a.removeBtn").click(function () {
                    var entityId = $(this).attr('data-entity-id');
                    jConfirm('Are you sure?', 'Confirmation Dialog', function (r) {
                        if (r) {
                            removeDomainObject(entityId, '${domainRestUrl}', function () {
                                getSearcher().search();
                            });
                        }
                    });
                });
                $("a[rel^='prettyPhoto']").prettyPhoto({ social_tools: ''});
            },
            "fnInitComplete": function () {
                this.fnAdjustColumnSizing();
            }
        });

        getSearcher().addDataTable(dataTable);

        getSearcher().search();

        bindInfoClickHandlers(tableElement, dataTable);

        $(".chzn-select").chosen({allow_single_deselect: true});

        $("select, input:checkbox, input:radio, input:file").uniform();

        $(".input-date").datepicker({
            autoSize: true,
            appendText: '(YYYY-MM-DD)',
            dateFormat: 'yy-mm-dd'
        });

    });

    function renderActions(entityId) {
        var viewImg = '<light:url value='/images/icons/dark/info.png'/>';
        var editImg = '<light:url value='/images/icons/dark/pencil.png'/>';
        var removeImg = '<light:url value='/images/icons/dark/basket.png'/>';

        var html = "<a href='" + viewEntityUrl(entityId) + "' title='View' class='btn14 mr5'><img src='" + viewImg + "' alt='View'></a>";
        html += "<a href='" + editEntityUrl(entityId) + "' title='Edit' class='btn14 mr5'><img src='" + editImg + "' alt='Edit'></a>";
        html += "<a href='#' title='Remove' class='btn14 mr5 removeBtn' data-entity-id='" + entityId + "'><img src='" + removeImg + "' alt='Remove'></a>";

        return html;
    }
</script>