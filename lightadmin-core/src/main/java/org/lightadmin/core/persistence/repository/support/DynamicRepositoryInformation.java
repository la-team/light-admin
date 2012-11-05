package org.lightadmin.core.persistence.repository.support;

import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.collect.Sets.newHashSet;

class DynamicRepositoryInformation implements RepositoryInformation {

	private final Map<Method, Method> methodCache = new ConcurrentHashMap<Method, Method>();

	private final RepositoryMetadata metadata;

	public DynamicRepositoryInformation( final RepositoryMetadata metadata ) {
		Assert.notNull( metadata );
		this.metadata = metadata;
	}

	@Override
	public Class<?> getRepositoryInterface() {
		return metadata.getRepositoryInterface();
	}

	@Override
	public Class<?> getReturnedDomainClass( final Method method ) {
		return metadata.getDomainType();
	}

	@Override
	public Class<?> getDomainType() {
		return metadata.getDomainType();
	}

	@Override
	public Class<? extends Serializable> getIdType() {
		return metadata.getIdType();
	}

	@Override
	public Class<?> getRepositoryBaseClass() {
		return metadata.getRepositoryInterface();
	}

	@Override
	public Method getTargetClassMethod( Method method ) {
		if ( methodCache.containsKey( method ) ) {
			return methodCache.get( method );
		}

		Method result = getTargetClassMethod( method, getRepositoryBaseClass() );
		methodCache.put( method, result );
		return result;
	}

	@Override
	public Iterable<Method> getQueryMethods() {
		return newHashSet();
	}

	@Override
	public boolean isCustomMethod( Method method ) {
		return false;
	}

	@Override
	public boolean isQueryMethod( final Method method ) {
		return false;
	}

	@Override
	public boolean hasCustomMethod() {
		return false;
	}

	private Method getTargetClassMethod( Method method, Class<?> baseClass ) {
		if ( baseClass == null ) {
			return method;
		}

		for ( Method baseClassMethod : baseClass.getMethods() ) {
			// Wrong name
			if ( !method.getName().equals( baseClassMethod.getName() ) ) {
				continue;
			}

			// Wrong number of arguments
			if ( !( method.getParameterTypes().length == baseClassMethod.getParameterTypes().length ) ) {
				continue;
			}

			// Check whether all parameters match
			if ( !parametersMatch( method, baseClassMethod ) ) {
				continue;
			}

			return baseClassMethod;
		}

		return method;
	}

	private boolean parametersMatch( Method method, Method baseClassMethod ) {
		Type[] genericTypes = baseClassMethod.getGenericParameterTypes();
		Class<?>[] types = baseClassMethod.getParameterTypes();

		for ( int i = 0; i < genericTypes.length; i++ ) {

			Type type = genericTypes[i];
			MethodParameter parameter = new MethodParameter( method, i );
			Class<?> parameterType = GenericTypeResolver.resolveParameterType( parameter, metadata.getRepositoryInterface() );

			if ( type instanceof TypeVariable<?> ) {
				if ( !matchesGenericType( ( TypeVariable<?> ) type, parameterType ) ) {
					return false;
				}
			} else {
				if ( !types[i].equals( parameterType ) ) {
					return false;
				}
			}
		}

		return true;
	}

	private boolean matchesGenericType( TypeVariable<?> variable, Class<?> parameterType ) {
		Class<?> entityType = getDomainType();
		Class<?> idClass = getIdType();

		if ( idClass.getSimpleName().equals( variable.getName() ) && parameterType.isAssignableFrom( idClass ) ) {
			return true;
		}

		Type boundType = variable.getBounds()[0];
		String referenceName = boundType instanceof TypeVariable ? boundType.toString() : variable.toString();

		return entityType.getSimpleName().equals( referenceName ) && parameterType.isAssignableFrom( entityType );
	}
}