<jsp:root version="2.0" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core">
	<select name="${field}">
		<option value=""></option>
		<c:forEach var="element" items="${elements}">
			<option value="${element.value}">${element.label}</option>
		</c:forEach>
	</select>
</jsp:root>
