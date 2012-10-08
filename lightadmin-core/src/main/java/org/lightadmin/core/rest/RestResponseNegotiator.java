package org.lightadmin.core.rest;

import org.springframework.data.rest.webmvc.MediaTypes;
import org.springframework.data.rest.webmvc.RepositoryAwareMappingHttpMessageConverter;
import org.springframework.data.rest.webmvc.RepositoryRestConfiguration;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class RestResponseNegotiator {

	private final RepositoryRestConfiguration config;
	private final List<HttpMessageConverter> httpMessageConverters;
	private final RepositoryAwareMappingHttpMessageConverter mappingHttpMessageConverter;

	public RestResponseNegotiator( final RepositoryRestConfiguration config, final List<HttpMessageConverter> httpMessageConverters, final RepositoryAwareMappingHttpMessageConverter mappingHttpMessageConverter ) {
		this.config = config;
		this.httpMessageConverters = httpMessageConverters;
		this.mappingHttpMessageConverter = mappingHttpMessageConverter;
	}

	@SuppressWarnings( {"unchecked"} )
	public ResponseEntity<byte[]> negotiateResponse( final ServletServerHttpRequest request, final HttpStatus status, final HttpHeaders headers, final Object resource ) throws IOException {

		String jsonpParam = request.getServletRequest().getParameter( config.getJsonpParamName() );
		String jsonpOnErrParam = null;
		if ( null != config.getJsonpOnErrParamName() ) {
			jsonpOnErrParam = request.getServletRequest().getParameter( config.getJsonpOnErrParamName() );
		}

		if ( null == resource ) {
			return maybeWrapJsonp( status, jsonpParam, jsonpOnErrParam, headers, null );
		}

		MediaType acceptType = config.getDefaultMediaType();
		HttpMessageConverter converter = findWriteConverter( resource.getClass(), acceptType );
		// If an Accept header is specified that isn't the catch-all, try and find a converter for it.
		if ( null == converter ) {
			for ( MediaType mt : request.getHeaders().getAccept() ) {
				if ( MediaType.ALL.equals( mt ) ) {
					continue;
				}

				HttpMessageConverter hmc;
				if ( null == ( hmc = findWriteConverter( resource.getClass(), mt ) ) ) {
					continue;
				}

				if ( !"*".equals( mt.getSubtype() ) ) {
					acceptType = mt;
					headers.setContentType( acceptType );
					converter = hmc;
				}
				break;
			}
		}

		if ( null == converter ) {
			converter = mappingHttpMessageConverter;
			headers.setContentType( MediaType.APPLICATION_JSON );
		}

		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		converter.write( resource, headers.getContentType(), new HttpOutputMessage() {
			@Override
			public OutputStream getBody() throws IOException {
				return bout;
			}

			@Override
			public HttpHeaders getHeaders() {
				return headers;
			}
		} );

		return maybeWrapJsonp( status, jsonpParam, jsonpOnErrParam, headers, bout.toByteArray() );
	}

	@SuppressWarnings( {"unchecked"} )
	private HttpMessageConverter findWriteConverter( Class<?> type, MediaType mediaType ) {
		for ( HttpMessageConverter conv : config.getCustomConverters() ) {
			if ( conv.canWrite( type, mediaType ) ) {
				return conv;
			}
		}
		for ( HttpMessageConverter conv : httpMessageConverters ) {
			if ( conv.canWrite( type, mediaType ) ) {
				return conv;
			}
		}
		return null;
	}

	private ResponseEntity<byte[]> maybeWrapJsonp( HttpStatus status, String jsonpParam, String jsonpOnErrParam, HttpHeaders headers, byte[] body ) {
		byte[] responseBody = ( null == body ? new byte[0] : body );
		if ( status.value() >= 400 && null != jsonpOnErrParam ) {
			status = HttpStatus.OK;
			responseBody = String.format( "%s(%s, %s)", jsonpOnErrParam, status.value(), ( null != body ? new String( body ) : null ) ).getBytes();
			headers.setContentType( MediaTypes.APPLICATION_JAVASCRIPT );
		} else if ( null != jsonpParam ) {
			responseBody = String.format( "%s(%s)", jsonpParam, ( null != body ? new String( body ) : null ) ).getBytes();
			headers.setContentType( MediaTypes.APPLICATION_JAVASCRIPT );
		}
		headers.setContentLength( responseBody.length );

		return new ResponseEntity<byte[]>( responseBody, headers, status );
	}
}