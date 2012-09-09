<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="static com.google.common.collect.Sets.newLinkedHashSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.lightadmin.core.util.Pair" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ taglib prefix="light" tagdir="/WEB-INF/tags" %>

<ul class="breadcrumb">
	<li><a href="<spring:url value="/"/>"><spring:message code="application.menu.dashboard"/></a></li><span class="divider">/</span>
	<li class="active">Entries</li>
</ul>

	<light:data-table entityName="entry" columns="<%= entryColumns() %>"/>

<%!
	private Set<org.lightadmin.core.util.Pair<String, String>> entryColumns() {
		Set<org.lightadmin.core.util.Pair<String, String>> result = newLinkedHashSet();
		result.add( org.lightadmin.core.util.Pair.stringPair( "name", "Name" ) );
		result.add( org.lightadmin.core.util.Pair.stringPair( "email", "Email" ) );
		result.add( org.lightadmin.core.util.Pair.stringPair( "favoriteColor", "Favorite Color" ) );
		return result;
	}
%>