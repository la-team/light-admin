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
	jQuery(function() {

		var datatable2Rest = function(sSource, aoData, fnCallback) {

			//extract name/value pairs into a simpler map for use later
			var paramMap = {};
			for ( var i = 0; i < aoData.length; i++) {
				paramMap[aoData[i].name] = aoData[i].value;
			}

			//page calculations
			var pageSize = paramMap.iDisplayLength;
			var start = paramMap.iDisplayStart;
			var pageNum = (start == 0) ? 1 : (start / pageSize) + 1; // pageNum is 1 based

			// extract sort information
			var sortCol = paramMap.iSortCol_0;
			var sortDir = paramMap.sSortDir_0;
			var sortName = paramMap['mDataProp_' + sortCol];

			//create new json structure for parameters for REST request
			var restParams = new Array();
			restParams.push({"name" : "limit", "value" : pageSize});
			restParams.push({"name" : "page", "value" : pageNum });
			restParams.push({ "name" : "sort", "value" : sortName });
			restParams.push({ "name" : sortName + ".dir", "value" : sortDir });

			//if we are searching by name, override the url and add the name parameter
			var url = sSource;
			<%--if (paramMap.sSearch != '') {--%>
			<%--url = "${baseUrl}rest/entry/search/findByNameContains";--%>
			<%--restParams.push({ "name" : "name", "value" : paramMap.sSearch});--%>
			<%--}--%>

			//finally, make the request
			jQuery.ajax({
							"dataType" : 'json',
							"type" : "GET",
							"url" : url,
							"data" : restParams,
							"success" : function(data) {
								data.iTotalRecords = data.totalCount;
								data.iTotalDisplayRecords = data.totalCount;

								fnCallback(data);
							}
						});
		};

		jQuery('#${entityName}Table').dataTable({
		   "sAjaxSource" : '${restServiceUrl}',
		   "sAjaxDataProp" : 'results',
		   "aoColumns" : [
			<c:forEach var="column" items="${columns}" varStatus="status">
			   {     mDataProp : '${column.first}'    }<c:out value="${!status.last ? ',' : ''}"/>
			</c:forEach>
		   ],
		   "bServerSide" : true,
		   "fnServerData" : datatable2Rest,
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