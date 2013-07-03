<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="attributeMetadata" type="org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata"
             scope="request"/>
<textarea id="${attributeMetadata.name}" name="${attributeMetadata.name}" rows="8">
</textarea>