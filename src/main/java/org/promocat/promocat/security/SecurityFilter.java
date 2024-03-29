package org.promocat.promocat.security;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.util_entities.TokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */
@Slf4j
public class SecurityFilter extends AbstractAuthenticationProcessingFilter {

    public static final String TOKEN_HEADER = "token";

    public SecurityFilter(final RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String token = request.getHeader(TOKEN_HEADER);
        Authentication requestAuth = new UsernamePasswordAuthenticationToken(token, token, TokenService.getAuthorities(token));
        return getAuthenticationManager().authenticate(requestAuth);
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request,
                                            final HttpServletResponse response,
                                            final FilterChain chain,
                                            final Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
}
