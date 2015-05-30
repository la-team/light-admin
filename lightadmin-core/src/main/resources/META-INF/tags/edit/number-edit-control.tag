<%@ tag import="org.lightadmin.core.persistence.metamodel.PersistentPropertyType" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ tag body-content="empty" %>
<%@ attribute name="attributeMetadata" required="true"
              type="org.springframework.data.mapping.PersistentProperty" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="allowEmpty" required="false" type="java.lang.Boolean" %>
<%@ attribute name="disabled" required="false" type="java.lang.Boolean" %>

<tiles:importAttribute name="dialogMode" ignore="true"/>
<c:set var="dialogMode" value="${dialogMode eq null ? false : true}"/>

<input id="${attributeMetadata.name}${dialogMode ? '-dialog' : ''}"
       name="${attributeMetadata.name}" ${disabled ? 'readonly' : ''} type="text"/>

<c:set var="numberIntegerType" value="<%= PersistentPropertyType.NUMBER_INTEGER %>"/>
<c:set var="numberFloatType" value="<%= PersistentPropertyType.NUMBER_FLOAT %>"/>
<c:set var="numericFieldId" value="${attributeMetadata.name}${dialogMode ? '-dialog' : ''}"/>

<script type="text/javascript">
    <c:if test="${not disabled}">
    <c:if test="${light:persistentPropertyTypeOf(attributeMetadata) eq numberIntegerType}">
    $("#${numericFieldId}").spinner({ decimals: 0, stepping: 1 ${allowEmpty ? ', allowNull : true' : ''} });
    </c:if>

    <c:if test="${light:persistentPropertyTypeOf(attributeMetadata) eq numberFloatType}">
    $("#${numericFieldId}").spinner({ decimals: 2 ${allowEmpty ? ', allowNull : true' : ''}});
    </c:if>
    </c:if>
</script>
