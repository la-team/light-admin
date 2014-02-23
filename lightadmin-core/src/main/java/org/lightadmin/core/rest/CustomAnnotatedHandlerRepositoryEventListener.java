package org.lightadmin.core.rest;

//import org.springframework.data.rest.repository.annotation.*;
//import org.springframework.data.rest.repository.context.*;

/**
 * This mirrors {@code AnnotatedHandlerRepositoryEventListener} just with one change: Event listeners are scanned from the administration base package.
 * <p/>
 * PS: {@code AnnotatedHandlerRepositoryEventListener} doesn't take into account Hierarchical Bean Factories, which is crucial for us.
 * <br>
 * Will be refactored during M3 redesign phase.
 * <p/>
 * Based on spring-data-rest 1.0.0.RELEASE
 * <p/>
 * // * @see org.springframework.data.rest.repository.context.AnnotatedHandlerRepositoryEventListener
 */
public class CustomAnnotatedHandlerRepositoryEventListener {
//        implements ApplicationListener<RepositoryEvent>, InitializingBean {
//
//    private final LightAdminConfiguration lightAdminConfiguration;
//    private final AutowireCapableBeanFactory beanFactory;
//
//    private final Multimap<Class<? extends RepositoryEvent>, EventHandlerMethod> handlerMethods = ArrayListMultimap.create();
//
//    public CustomAnnotatedHandlerRepositoryEventListener(LightAdminConfiguration lightAdminConfiguration, AutowireCapableBeanFactory beanFactory) {
//        this.lightAdminConfiguration = lightAdminConfiguration;
//        this.beanFactory = beanFactory;
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        final TypeFilterClassScanner typeFilterClassScanner = new TypeFilterClassScanner(new AnnotationTypeFilter(RepositoryEventHandler.class, true, true));
//        final Set<Class> classes = typeFilterClassScanner.scan(lightAdminConfiguration.getBasePackage());
//
//        for (Class<?> handlerType : classes) {
//            RepositoryEventHandler typeAnno = handlerType.getAnnotation(RepositoryEventHandler.class);
//            Class<?>[] targetTypes = typeAnno.value();
//            if (targetTypes.length == 0) {
//                targetTypes = new Class<?>[]{null};
//            }
//
//            final Object handler = autowiredBeanInstance(handlerType, beanFactory);
//
//            for (final Class<?> targetType : targetTypes) {
//                doWithMethods(
//                        handler.getClass(),
//                        new ReflectionUtils.MethodCallback() {
//                            @Override
//                            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
//                                inspect(targetType, handler, method, HandleBeforeSave.class, BeforeSaveEvent.class);
//                                inspect(targetType, handler, method, HandleAfterSave.class, AfterSaveEvent.class);
//                                inspect(targetType, handler, method, HandleBeforeLinkSave.class, BeforeLinkSaveEvent.class);
//                                inspect(targetType, handler, method, HandleAfterLinkSave.class, AfterLinkSaveEvent.class);
//                                inspect(targetType, handler, method, HandleBeforeDelete.class, BeforeDeleteEvent.class);
//                                inspect(targetType, handler, method, HandleAfterDelete.class, AfterDeleteEvent.class);
//                            }
//                        },
//                        new ReflectionUtils.MethodFilter() {
//                            @Override
//                            public boolean matches(Method method) {
//                                return (!method.isSynthetic()
//                                        && !method.isBridge()
//                                        && method.getDeclaringClass() != Object.class
//                                        && !method.getName().contains("$"));
//                            }
//                        }
//                );
//            }
//        }
//    }
//
//    @Override
//    public void onApplicationEvent(RepositoryEvent event) {
//        Class<? extends RepositoryEvent> eventType = event.getClass();
//        if (!handlerMethods.containsKey(eventType)) {
//            return;
//        }
//
//        for (EventHandlerMethod handlerMethod : handlerMethods.get(eventType)) {
//            try {
//                Object src = event.getSource();
//
//                if (!ClassUtils.isAssignable(handlerMethod.targetType, src.getClass())) {
//                    continue;
//                }
//
//                List<Object> params = new ArrayList<Object>();
//                params.add(src);
//                if (event instanceof BeforeLinkSaveEvent) {
//                    params.add(((BeforeLinkSaveEvent) event).getLinked());
//                } else if (event instanceof AfterLinkSaveEvent) {
//                    params.add(((AfterLinkSaveEvent) event).getLinked());
//                }
//
//                handlerMethod.method.invoke(handlerMethod.handler, params.toArray());
//
//            } catch (Exception e) {
//                throw new IllegalStateException(e);
//            }
//        }
//    }
//
//    private Object autowiredBeanInstance(final Class handlerType, AutowireCapableBeanFactory beanFactory) {
//        final Object handler = BeanUtils.instantiateClass(handlerType);
//        beanFactory.autowireBean(handler);
//        return handler;
//    }
//
//    private <T extends Annotation> void inspect(Class<?> targetType,
//                                                Object handler,
//                                                Method method,
//                                                Class<T> annoType,
//                                                Class<? extends RepositoryEvent> eventType) {
//        T anno = method.getAnnotation(annoType);
//        if (null != anno) {
//            try {
//                Class<?>[] targetTypes;
//                if (null == targetType) {
//                    targetTypes = (Class<?>[]) anno.getClass().getMethod("value", new Class[0]).invoke(anno);
//                } else {
//                    targetTypes = new Class<?>[]{targetType};
//                }
//                for (Class<?> type : targetTypes) {
//                    handlerMethods.put(eventType,
//                            new EventHandlerMethod(type,
//                                    handler,
//                                    method));
//                }
//            } catch (NoSuchMethodException ignored) {
//            } catch (InvocationTargetException ignored) {
//            } catch (IllegalAccessException ignored) {
//            }
//        }
//    }
//
//    private class EventHandlerMethod {
//        final Class<?> targetType;
//        final Method method;
//        final Object handler;
//
//        private EventHandlerMethod(Class<?> targetType,
//                                   Object handler,
//                                   Method method) {
//            this.targetType = targetType;
//            this.method = method;
//            this.handler = handler;
//        }
//    }

}
