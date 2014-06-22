<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="attributeMetadata" type="org.springframework.data.mapping.PersistentProperty"
             scope="request"/>
<textarea id="${attributeMetadata.name}" name="${attributeMetadata.name}" rows="8">
</textarea>