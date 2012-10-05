<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="entityName" required="true" type="java.lang.String"%>
<%@ attribute name="columns" required="true" type="java.util.Set"%>

<spring:url value="/rest/${entityName}" var="restServiceUrl" />

<table id="${entityName}Table" class="table table-striped table-bordered table-hover">
	<thead>
	<tr>
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
		$('#${entityName}Table').dataTable({
		   "sAjaxSource" : '${restServiceUrl}',
		   "sAjaxDataProp" : 'content',
		   "aoColumns" : [
			<c:forEach var="column" items="${columns}" varStatus="status">
			   {     mDataProp : '${column.first}'    }<c:out value="${!status.last ? ',' : ''}"/>
			</c:forEach>
		   ],
		   "aoColumnDefs":[
			   {
				   "aTargets":[ ${fn:length(columns) } ],
				   "mData":null,
				   "mRender":function ( data, type, full ) {
					   var restEntityServiceUrl = full.links[0]['href'];
					   var entityId = new RegExp( /${entityName}\/(\d+)/ ).exec(restEntityServiceUrl)[1];
					   var viewEntityUrl = "<spring:url value='/domain/${entityName}/'/>" + entityId;
					   var editEntityUrl = viewEntityUrl + "/edit";

					   return '<a href="' + viewEntityUrl + '">View</a>' + '&nbsp;&nbsp;' + '<a href="' + editEntityUrl + '">Edit</a>';
				   }
			   }
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
	});
</script>