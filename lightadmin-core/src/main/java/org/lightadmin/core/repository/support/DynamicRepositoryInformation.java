package org.lightadmin.core.repository.support;

import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.AbstractRepositoryMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.data.repository.util.ClassUtils.isGenericRepositoryInterface;

public class DynamicRepositoryInformation extends AbstractRepositoryMetadata implements RepositoryInformation {

	private final Map<Method, Method> methodCache = new ConcurrentHashMap<Method, Method>();

	private final RepositoryMetadata metadata;
	private final Class<?> repositoryBaseClass;
	private final Class<?> customImplementationClass;

	public DynamicRepositoryInformation( RepositoryMetadata metadata, Class<?> repositoryBaseClass, Class<?> customImplementationClass ) {
		super( metadata.getRepositoryInterface() );

		Assert.notNull( metadata );
		Assert.notNull( repositoryBaseClass );

		this.metadata = metadata;
		this.repositoryBaseClass = repositoryBaseClass;
		this.customImplementationClass = customImplementationClass;
	}

	public Class<?> getRepositoryInterface() {
		return metadata.getRepositoryInterface();
	}

	public Class<?> getDomainType() {
		return metadata.getDomainType();
	}

	public Class<? extends Serializable> getIdType() {
		return metadata.getIdType();
	}

	public Class<?> getRepositoryBaseClass() {
		return this.repositoryBaseClass;
	}

	public Method getTargetClassMethod( Method method ) {
		if ( methodCache.containsKey( method ) ) {
			return methodCache.get( method );
		}

		Method result = getTargetClassMethod( method, customImplementationClass );

		if ( !result.equals( method ) ) {
			methodCache.put( method, result );
			return result;
		}

		result = getTargetClassMethod( method, repositoryBaseClass );
		methodCache.put( method, result );
		return result;
	}

	private boolean isTargetClassMethod( Method method, Class<?> targetType ) {
		Assert.notNull( method );

		if ( targetType == null ) {
			return false;
		}

		if ( method.getDeclaringClass().isAssignableFrom( targetType ) ) {
			return true;
		}

		return !method.equals( getTargetClassMethod( method, targetType ) );
	}

	public Iterable<Method> getQueryMethods() {
		Set<Method> result = new HashSet<Method>();

		for ( Method method : getRepositoryInterface().getMethods() ) {
			method = ClassUtils.getMostSpecificMethod( method, getRepositoryInterface() );
			if ( !isCustomMethod( method ) && !isBaseClassMethod( method ) ) {
				result.add( method );
			}
		}

		return result;
	}

	public boolean isCustomMethod( Method method ) {
		return isTargetClassMethod( method, customImplementationClass );
	}

	public boolean isBaseClassMethod( Method method ) {
		return isTargetClassMethod( method, repositoryBaseClass );
	}

	Method getTargetClassMethod(Method method, Class<?> baseClass) {
		if (baseClass == null) {
			return method;
		}

		for (Method baseClassMethod : baseClass.getMethods()) {

			// Wrong name
			if (!method.getName().equals(baseClassMethod.getName())) {
				continue;
			}

			// Wrong number of arguments
			if (!(method.getParameterTypes().length == baseClassMethod.getParameterTypes().length)) {
				continue;
			}

			// Check whether all parameters match
			if (!parametersMatch(method, baseClassMethod)) {
				continue;
			}

			return baseClassMethod;
		}

		return method;
	}

	public boolean hasCustomMethod() {
		Class<?> repositoryInterface = getRepositoryInterface();

		// No detection required if no typing interface was configured
		if ( isGenericRepositoryInterface( repositoryInterface ) ) {
			return false;
		}

		for ( Method method : repositoryInterface.getMethods() ) {
			if ( isCustomMethod( method ) && !isBaseClassMethod( method ) ) {
				return true;
			}
		}

		return false;
	}

	private boolean parametersMatch(Method method, Method baseClassMethod) {
		Type[] genericTypes = baseClassMethod.getGenericParameterTypes();
		Class<?>[] types = baseClassMethod.getParameterTypes();

		for (int i = 0; i < genericTypes.length; i++) {

			Type type = genericTypes[i];
			MethodParameter parameter = new MethodParameter(method, i);
			Class<?> parameterType = GenericTypeResolver.resolveParameterType( parameter, metadata.getRepositoryInterface() );

			if (type instanceof TypeVariable<?> ) {
				if (!matchesGenericType((TypeVariable<?>) type, parameterType)) {
					return false;
				}
			} else {
				if (!types[i].equals(parameterType)) {
					return false;
				}
			}
		}

		return true;
	}

	private boolean matchesGenericType(TypeVariable<?> variable, Class<?> parameterType) {
		Class<?> entityType = getDomainType();
		Class<?> idClass = getIdType();

		if (idClass.getSimpleName().equals(variable.getName()) && parameterType.isAssignableFrom(idClass)) {
			return true;
		}

		Type boundType = variable.getBounds()[0];
		String referenceName = boundType instanceof TypeVariable ? boundType.toString() : variable.toString();

		if (entityType.getSimpleName().equals(referenceName) && parameterType.isAssignableFrom(entityType)) {
			return true;
		}

		return false;
	}
}