<%@ tag import="org.lightadmin.core.persistence.metamodel.DomainTypeAttributeType" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag body-content="empty" %>
<%@ attribute name="attributeMetadata" required="true"
			type="org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="errorCssClass" required="false" type="java.lang.String" %>
<%@ attribute name="allowEmpty" required="false" type="java.lang.Boolean" %>
<%@ attribute name="disabled" required="false" type="java.lang.Boolean" %>
<input id="${attributeMetadata.name}" name="${attributeMetadata.name}" ${disabled ? 'disabled' : ''} type="text"/>

<c:set var="numberIntegerType" value="<%= DomainTypeAttributeType.NUMBER_INTEGER %>"/>
<c:set var="numberFloatType" value="<%= DomainTypeAttributeType.NUMBER_FLOAT %>"/>

<script type="text/javascript">
	<c:if test="${not disabled}">
	<c:if test="${attributeMetadata.attributeType eq numberIntegerType}">
	$( "#${attributeMetadata.name}" ).spinner( { decimals: 0, stepping: 1 ${allowEmpty ? ', allowNull : true' : ''} } );
	</c:if>

	<c:if test="${attributeMetadata.attributeType eq numberFloatType}">
	$( "#${attributeMetadata.name}" ).spinner( { decimals: 2 ${allowEmpty ? ', allowNull : true' : ''}} );
	</c:if>
	</c:if>
</script>
