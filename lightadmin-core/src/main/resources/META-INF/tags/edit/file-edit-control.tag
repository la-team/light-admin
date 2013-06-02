<%@ tag body-content="empty" %>
<%@ attribute name="attributeMetadata" required="true"
			  type="org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="errorCssClass" required="false" type="java.lang.String" %>
<%@ attribute name="disabled" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:useAttribute name="domainTypeAdministrationConfiguration"/>
<tiles:useAttribute name="entityId"/>

<light:url var="fileUploadUrl" value="/rest/upload" scope="page"/>

<light:url var="domainObjectUrl" value="${light:domainRestEntityBaseUrl(domainTypeAdministrationConfiguration, entityId)}" scope="page"/>
<c:set var="filePropertyUrl" value="${domainObjectUrl}/${attributeMetadata.name}/file" scope="page"/>

<div id="${attributeMetadata.name}-file-container">
	<div class="files"></div>
	<input id="${attributeMetadata.name}" name="${attributeMetadata.name}" type="hidden"/>
	<label id="${attributeMetadata.name}-error" for="${attributeMetadata.name}" class="${errorCssClass}"></label>

	<a id="${attributeMetadata.name}-pickfiles" href="#">[Select file]</a>
</div>
<script type="text/javascript">
	$( function () {
		var ${attributeMetadata.name}Uploader = new plupload.Uploader( {
																		   runtimes: 'html5,html4',
																		   url: '${fileUploadUrl}',
																		   container: '${attributeMetadata.name}-file-container',
																		   max_file_size: '10mb',
																		   file_data_name: '${attributeMetadata.name}',
																		   unique_names: true,
																		   filters: [
																			   {title: "Image files", extensions: "jpg,jpeg,png"}
																		   ],
																		   browse_button: '${attributeMetadata.name}-pickfiles'
																	   } );

		${attributeMetadata.name}Uploader.init();

		${attributeMetadata.name}Uploader.bind( 'FilesAdded', function ( up, files ) {
			$.each( files, function ( i, file ) {
				var filesContainer = $( 'div.files', '#${attributeMetadata.name}-file-container' );
				filesContainer.empty();
				filesContainer.prepend( $( '<div id="' + file.id + '">' + file.name + ' (' + plupload.formatSize( file.size ) + ') <b></b>' + '</div>' ) );
			} );
			up.refresh();

			${attributeMetadata.name}Uploader.start();
		} );

		${attributeMetadata.name}Uploader.bind( 'UploadProgress', function ( up, file ) {
			$( '#' + file.id + " b" ).html( file.percent + "%" );
		} );

		${attributeMetadata.name}Uploader.bind( 'Error', function ( up, err ) {
			var errorCode = err.code;
			var errorMessage = err.message + (err.file ? " File: " + err.file.name : "");

			if ( errorCode == -600 ) {
				errorMessage = 'Selected file ' + (err.file ? err.file.name + ' ' : "") + 'exceeds file size limit of 10MB';
			} else if ( errorCode == -601 || errorCode == -700 ) {
				errorMessage = "Selected file type is not supported " + (err.file ? err.file.name : "") + ". Please select JPG or PNG file.";
			}

			jAlert( errorMessage, 'Upload file operation failure' );
			up.refresh();
		} );

		${attributeMetadata.name}Uploader.bind( 'FileUploaded', function ( up, file, response ) {
			$( '#' + file.id + " b" ).html( "100%" );

			var result = $.parseJSON( response.response );

			$( '#${attributeMetadata.name}' ).val( result['fileContent'] );
		} );

	} );
</script>