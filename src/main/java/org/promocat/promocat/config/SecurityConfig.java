package org.promocat.promocat.config;

import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.security.SecurityFilter;
import org.promocat.promocat.security.SecurityProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private static final RequestMatcher PROTECTED_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/**"),
            new AntPathRequestMatcher("/data/examples/admin/**")
    );

    private static final RequestMatcher ADMIN_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/data/examples/admin/**")
    );

    private static final RequestMatcher USER_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/user/**")
    );

    private static final RequestMatcher COMPANY_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/company/**")
    );


    SecurityProvider provider;

    public SecurityConfig(final SecurityProvider authenticationProvider) {
        super();
        this.provider = authenticationProvider;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(provider);
    }


    /**
     * Задаёт адреса, по которым не требуется авторизация
     */
    @Override
    public void configure(final WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers("/auth/**", "/swagger-ui.html#/**", "/h2-console/**", "/data/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .exceptionHandling()
                .and()
                .authenticationProvider(provider)
                .addFilterBefore(authenticationFilter(), AnonymousAuthenticationFilter.class)
                .authorizeRequests()
                .requestMatchers(ADMIN_URLS)
                .hasAuthority("ROLE_" + AccountType.ADMIN.getType().toUpperCase())
                .requestMatchers(USER_URLS)
                .hasAuthority("ROLE_" + AccountType.USER.getType().toUpperCase())
                .requestMatchers(COMPANY_URLS)
                .hasAuthority("ROLE_" + AccountType.COMPANY.getType().toUpperCase())
                .requestMatchers(PROTECTED_URLS)
                .authenticated()
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .logout().disable();
    }

    @Bean
    SecurityFilter authenticationFilter() throws Exception {
        final SecurityFilter filter = new SecurityFilter(PROTECTED_URLS);
        filter.setAuthenticationManager(authenticationManager());
//        filter.setAuthenticationSuccessHandler(successHandler());
        return filter;
    }

    @Bean
    AuthenticationEntryPoint forbiddenEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.FORBIDDEN);
    }

}
