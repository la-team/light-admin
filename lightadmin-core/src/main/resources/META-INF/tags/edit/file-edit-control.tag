<%@ tag body-content="empty" %>
<%@ attribute name="attributeMetadata" required="true"
			  type="org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="errorCssClass" required="false" type="java.lang.String" %>
<%@ attribute name="disabled" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>

<light:url var="domainObjectUrl" value="${light:domainRestEntityBaseUrl(domainTypeName, entityId)}" scope="page"/>

<div id="${attributeMetadata.name}-file-container">
	<%--<input id="${attributeMetadata.name}" name="${attributeMetadata.name}" ${disabled} type="file" class="fileInput"/>--%>
	<%--<label id="${attributeMetadata.name}-error" for="${attributeMetadata.name}" class="${errorCssClass}"></label>--%>

	<a id="${attributeMetadata.name}-pickfiles" name="${attributeMetadata.name}" href="#">[Select files]</a>
</div>
<script type="text/javascript">
	$( function () {
		var uploader = new plupload.Uploader( {
												  runtimes: 'html5,html4',
												  url: '${domainObjectUrl}/${attributeMetadata.name}',
												  container: '${attributeMetadata.name}-file-container',
												  max_file_size: '10mb',
												  unique_names: true,
												  filters: [
													  {title: "Image files", extensions: "jpg,jpeg,gif,png"}
												  ],
												  browse_button: '${attributeMetadata.name}-pickfiles'
											  } );

		uploader.init();

		uploader.bind( 'FilesAdded', function ( up, files ) {
			$.each( files, function ( i, file ) {
				$( '#${attributeMetadata.name}-file-container' ).prepend( $( '<div id="' + file.id + '">' + file.name + ' (' + plupload.formatSize( file.size ) + ') <b></b>' + '</div>' ) );
			} );
			up.refresh();

			$( '#${attributeMetadata.name}-file-container' ).addClass( 'filesAdded' );
		} );

		uploader.bind( 'UploadProgress', function ( up, file ) {
			$( '#' + file.id + " b" ).html( file.percent + "%" );
		} );

		uploader.bind( 'FileUploaded', function ( up, file ) {
			$( '#' + file.id + " b" ).html( "100%" );
		} );

		$( '#${attributeMetadata.name}-file-container' ).data( 'uploader', uploader );
	} );
</script>