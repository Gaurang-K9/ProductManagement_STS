package com.demo.filter;

import com.demo.model.user.UserPrincipal;
import com.demo.constants.AuthEndpointConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class FirstLoginFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserPrincipal user) {

            boolean isFirstLogin = user.user().isFirstLogin();
            String path = request.getRequestURI();

            boolean isAllowedPath = Arrays.stream(AuthEndpointConstants.FIRST_LOGIN_ALLOWED).anyMatch(path::startsWith);

            if (isFirstLogin && !isAllowedPath) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType("application/json");
                response.getWriter().write("""
                {
                  "message": "Password change required",
                  "code": "FIRST_LOGIN"
                }
                """);
                response.getWriter().flush();
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
