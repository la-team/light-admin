<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<select name="${field}">
    <option value=""></option>
    <c:forEach var="element" items="${elements}">
        <option value="${element.value}">${element.label}</option>
    </c:forEach>
</select>