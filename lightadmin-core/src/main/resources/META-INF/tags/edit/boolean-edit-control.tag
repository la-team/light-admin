<%@ tag body-content="empty" %>
<%@ attribute name="attributeMetadata" required="true"
              type="org.springframework.data.mapping.PersistentProperty" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="disabled" required="false" type="java.lang.Boolean" %>

<input type="checkbox" id="${attributeMetadata.name}" name="${attributeMetadata.name}" style="opacity: 0; "
       value="true" ${disabled ? 'disabled' : ''}/>
