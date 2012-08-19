<jsp:root version="2.0" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" >
	<html xmlns="http://www.w3.org/1999/xhtml">
		<header>
			<title>Example</title>
		</header>
		<body>
			<h2>Example: Entries</h2>
			<table>
				<tr>
					<th>ID</th>
					<th>NAME</th>					
				</tr>
				<c:forEach var="entry" items="${entries}">
					<tr>
						<td>${entry.id}</td>
						<td><c:out value="${entry.name}" default="N/A" escapeXml="true" /></td>
					</tr>
				</c:forEach>
			</table>
		</body>
	</html>
</jsp:root>