<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>

<%@ attribute name="domainTypeName" required="true" type="java.lang.String"%>
<%@ attribute name="columns" required="true" type="java.util.Set"%>
<%@ attribute name="domainTypeEntityMetadata" required="true" rtexprvalue="true" type="org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata"%>

<spring:url value="${light:domainBaseUrl(domainTypeName)}" var="domainBaseUrl" />
<spring:url value="${light:domainRestBaseUrl(domainTypeName)}" var="domainRestBaseUrl" />

<table id="${domainTypeName}Table" class="table table-bordered table-hover">
	<thead>
	<tr>
		<th class="info"></th>
		<c:forEach var="column" items="${columns}">
			<th><c:out value="${column.second}"/></th>
		</c:forEach>
		<th>Actions</th>
	</tr>
	</thead>
	<tbody/>
</table>

<script type="text/javascript">
	function viewEntityUrl( entityId ) {
		return '${domainBaseUrl}' + '/' + entityId;
	}

	function editEntityUrl( entityId ) {
		return '${domainBaseUrl}' + '/' + entityId + '/edit';
	}

	$(function() {
		var tableElement = $('#${domainTypeName}Table');

		var dataTable = tableElement.dataTable({
			"bStateSave": true,
		   "sAjaxSource" : '${domainRestBaseUrl}',
		   "sAjaxDataProp" : 'content',
		   "aoColumnDefs":[
			   {
				   "bSortable":false,
				   "aTargets":[ 0 ],
				   "mData": null,
				   "sClass": "center",
				   "mRender": function ( data, type, full ) {
					   return '<img src="<spring:url value='/images/details_open.png'/>" style="cursor:pointer;" title="Click to show Info"/>';
				   }
			   },
			   <c:forEach var="column" items="${columns}" varStatus="status">
			   {
				   "aTargets":[ ${status.index + 1 } ],
				   "mData" : '${column.first}'
			   },
			   </c:forEach>
			   {
				   "bSortable":false,
				   "aTargets":[ ${fn:length(columns) + 1 } ],
				   "mData":null,
				   "mRender":function ( data, type, full ) {
					   var entityId = full['${domainTypeEntityMetadata.idAttribute.name}'];

					   return '<a href="' + viewEntityUrl( entityId ) + '">View</a>'
									  + '&nbsp;&nbsp;'
									  + '<a href="' + editEntityUrl( entityId ) + '">Edit</a>';
				   }
			   }
		   ],
		   "aaSorting":[
			   [1, 'asc']
		   ],
		   "bServerSide" : true,
		   "fnServerData" : dataTableRESTAdapter,
		   "sPaginationType": "bootstrap",
		   "oLanguage": {
			   "sLengthMenu": "_MENU_ records per page"
		   },
		   "bLengthChange": false,
		   "bFilter": false,
		   "bInfo": false
	   });

		$( document ).data('lightadmin.dataTable', dataTable );

		bindInfoClickHandlers( tableElement, dataTable );
	});
</script>