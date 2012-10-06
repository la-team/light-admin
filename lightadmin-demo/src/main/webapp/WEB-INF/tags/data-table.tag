<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="entityName" required="true" type="java.lang.String"%>
<%@ attribute name="columns" required="true" type="java.util.Set"%>
<%@ attribute name="domainTypeEntityMetadata" required="true" rtexprvalue="true" type="org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata"%>

<spring:url value="/rest/${entityName}" var="restServiceUrl" />

<table id="${entityName}Table" class="table table-striped table-bordered table-hover">
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
		var tableElement = $('#${entityName}Table');

		var oTable = tableElement.dataTable({
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

					   var viewEntityUrl = "<spring:url value='/domain/${entityName}/'/>" + entityId;

					   var editEntityUrl = "<spring:url value='/domain/${entityName}/'/>" + entityId + "/edit";

					   return '<a href="' + viewEntityUrl + '">View</a>' + '&nbsp;&nbsp;' + '<a href="' + editEntityUrl + '">Edit</a>';
				   }
			   }
		   ],
		   "aaSorting":[
			   [1, 'asc']
		   ],
			"sScrollY": "600px",
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

		bindInfoClinkHanlders( tableElement, oTable );
	});
</script>

<script type="text/javascript">


</script>