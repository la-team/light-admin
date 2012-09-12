<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
	</tr>
	</thead>
	<tbody/>
</table>

<script type="text/javascript">
	$(function() {
		$('#${entityName}Table').dataTable({
		   "sAjaxSource" : '${restServiceUrl}',
		   "sAjaxDataProp" : 'results',
		   "aoColumns" : [
			<c:forEach var="column" items="${columns}" varStatus="status">
			   {     mDataProp : '${column.first}'    }<c:out value="${!status.last ? ',' : ''}"/>
			</c:forEach>
		   ],
		   "bServerSide" : true,
		   "fnServerData" : dataTableRESTAdapter,
		   "sDom": "<'row'<'span6'l><'span6'f>r>t<'row'<'span6'i><'span6'p>>",
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