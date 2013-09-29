package org.lightadmin.core.config;

import org.lightadmin.core.context.WebContext;
import org.lightadmin.core.web.security.LightAdminRequestCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.util.AntPathRequestMatcher;
import org.springframework.security.web.util.AnyRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;

import javax.servlet.Filter;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.asList;

@Configuration
public class LightAdminSecurityConfiguration {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String REMEMBER_ME_DIGEST_KEY = "LightAdmin";

    private static final String[] PUBLIC_RESOURCES = {
            "/images/**", "/scripts/**", "/styles/**", "/rest/**/file",
            "/login", "/page-not-found", "/access-denied"
    };

    @Value("classpath:users.properties")
    private Resource usersResource;

    @Autowired
    private WebContext webContext;

    @Bean
    @Autowired
    public FilterChainProxy springSecurityFilterChain(Filter filterSecurityInterceptor, Filter authenticationFilter, Filter rememberMeAuthenticationFilter, Filter logoutFilter, Filter exceptionTranslationFilter, Filter securityContextPersistenceFilter) {

        List<SecurityFilterChain> filterChains = new ArrayList<SecurityFilterChain>();
        for (String pattern : PUBLIC_RESOURCES) {
            filterChains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher(applicationUrl(pattern))));
        }

        filterChains.add(new DefaultSecurityFilterChain(new AnyRequestMatcher(), securityContextPersistenceFilter, exceptionTranslationFilter, logoutFilter, authenticationFilter, rememberMeAuthenticationFilter, filterSecurityInterceptor));

        return new FilterChainProxy(filterChains);
    }

    @Bean
    @Autowired
    public Filter filterSecurityInterceptor(AuthenticationManager authenticationManager) throws Exception {
        FilterSecurityInterceptor filter = new FilterSecurityInterceptor();
        filter.setAuthenticationManager(authenticationManager);
        filter.setAccessDecisionManager(new AffirmativeBased(asList((AccessDecisionVoter) new RoleVoter())));
        filter.setSecurityMetadataSource(securityMetadataSource());
        filter.afterPropertiesSet();
        return filter;
    }

    private FilterInvocationSecurityMetadataSource securityMetadataSource() {
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> map = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
        map.put(new AnyRequestMatcher(), asList((ConfigAttribute) new SecurityConfig(ROLE_ADMIN)));
        return new DefaultFilterInvocationSecurityMetadataSource(map);
    }

    @Bean
    @Autowired
    public Filter authenticationFilter(AuthenticationManager authenticationManager, RequestCache requestCache) {
        UsernamePasswordAuthenticationFilter authenticationFilter = new UsernamePasswordAuthenticationFilter();
        authenticationFilter.setFilterProcessesUrl(applicationUrl("/j_spring_security_check"));
        authenticationFilter.setAuthenticationManager(authenticationManager);
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setRequestCache(requestCache);
        authenticationFilter.setAuthenticationSuccessHandler(successHandler);
        authenticationFilter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(applicationUrl("/login?login_error=1")));
        return authenticationFilter;
    }

    @Bean
    public Filter exceptionTranslationFilter(RequestCache requestCache) {
        AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
        accessDeniedHandler.setErrorPage(applicationUrl("/access-denied"));
        LoginUrlAuthenticationEntryPoint authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint(applicationUrl("/login"));
        ExceptionTranslationFilter exceptionTranslationFilter = new ExceptionTranslationFilter(authenticationEntryPoint, requestCache);
        exceptionTranslationFilter.setAccessDeniedHandler(accessDeniedHandler);
        return exceptionTranslationFilter;
    }

    @Bean
    public Filter logoutFilter() {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.setInvalidateHttpSession(false);
        LogoutFilter logoutFilter = new LogoutFilter(applicationUrl("/"), logoutHandler);
        logoutFilter.setFilterProcessesUrl(applicationUrl("/logout"));
        return logoutFilter;
    }

    @Bean
    public Filter securityContextPersistenceFilter() {
        HttpSessionSecurityContextRepository repo = new HttpSessionSecurityContextRepository();
        repo.setSpringSecurityContextKey(keyWithNamespace(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY));
        return new SecurityContextPersistenceFilter(repo);
    }

    @Bean
    public Filter rememberMeAuthenticationFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(REMEMBER_ME_DIGEST_KEY, userDetailsService);
        rememberMeServices.setCookieName(keyWithNamespace(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY));
        return new RememberMeAuthenticationFilter(authenticationManager, rememberMeServices);
    }

    @Bean
    public RequestCache requestCache() {
        return new LightAdminRequestCache();
    }

    @Bean
    @Autowired
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider, AuthenticationProvider rememberMeAuthenticationProvider) {
        return new ProviderManager(asList(authenticationProvider, rememberMeAuthenticationProvider));
    }

    @Bean
    @Autowired
    public AuthenticationProvider authenticationProvider(UserDetailsService usersService) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new ShaPasswordEncoder());
        provider.setUserDetailsService(usersService);
        provider.afterPropertiesSet();
        return provider;
    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService() throws IOException {
        Properties usersPproperties = PropertiesLoaderUtils.loadProperties(usersResource);
        return new InMemoryUserDetailsManager(usersPproperties);
    }

    @Bean
    public AuthenticationProvider rememberMeAuthenticationProvider() {
        return new RememberMeAuthenticationProvider(REMEMBER_ME_DIGEST_KEY);
    }

    private String applicationUrl(String path) {
        return webContext.getApplicationUrl(path);
    }

    private String keyWithNamespace(String key) {
        return "lightadmin:" + key;
    }

}
