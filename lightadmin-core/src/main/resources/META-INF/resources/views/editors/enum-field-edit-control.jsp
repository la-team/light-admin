<jsp:root version="2.0" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core">
	<select name="${field}">
		<option value=""></option>
		<c:forEach var="enumItem" items="${enumValues}">
			<option value="${enumItem}">${enumItem}</option>
		</c:forEach>
	</select>
</jsp:root>
