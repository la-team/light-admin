<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="domainTypeName" required="true" type="java.lang.String"%>
<%@ attribute name="columns" required="true" type="java.util.Set"%>
<%@ attribute name="domainTypeEntityMetadata" required="true" rtexprvalue="true" type="org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata"%>

<spring:url value="/rest/${domainTypeName}" var="restServiceUrl" />

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
	$(function() {
		var tableElement = $('#${domainTypeName}Table');

		var oTable = tableElement.dataTable({
			"bStateSave": true,
		   "sAjaxSource" : '${restServiceUrl}',
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

					   var viewEntityUrl = "<spring:url value='/domain/${domainTypeName}/'/>" + entityId;

					   var editEntityUrl = "<spring:url value='/domain/${domainTypeName}/'/>" + entityId + "/edit";

					   return '<a href="' + viewEntityUrl + '">View</a>' + '&nbsp;&nbsp;' + '<a href="' + editEntityUrl + '">Edit</a>';
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

		$( document ).data('lightadmin.dataTable', oTable );

		bindInfoClickHandlers( tableElement, oTable );
	});
</script>