<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ tag import="org.apache.tiles.beans.MenuItem" %>
<%@ tag import="org.apache.tiles.beans.SimpleMenuItem" %>

<%@ attribute name="name" type="java.lang.String" rtexprvalue="true" required="false"
			  description="Breadcrumb item name" %>
<%@ attribute name="link" type="java.lang.String" rtexprvalue="true" required="false" description="Breadcrumb link" %>
<%@ attribute name="tooltip" type="java.lang.String" rtexprvalue="true" required="false"
			  description="Breadcrumb tooltip" %>

<jsp:useBean id="tag_breadcrumb_breadcrumbItems" type="java.util.List" scope="request"/>

<%
	MenuItem menuItem = new SimpleMenuItem();
	menuItem.setValue( name != null ? name : "Undefined" );
	menuItem.setLink( link != null ? link : "#" );
	menuItem.setTooltip( tooltip != null ? tooltip : menuItem.getValue() );

	tag_breadcrumb_breadcrumbItems.add( menuItem );
%>