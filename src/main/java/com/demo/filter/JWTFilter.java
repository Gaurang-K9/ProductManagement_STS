package com.demo.filter;

import com.demo.model.exception.ExceptionResponse;
import com.demo.service.auth.AuthUserDetailsService;
import com.demo.service.auth.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    JWTService jwtService;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    JsonMapper jsonMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String username = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                username = jwtService.extractUsername(token);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = applicationContext.getBean(AuthUserDetailsService.class).loadUserByUsername(username);

                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception exception) {
            handleJwtException(response, exception);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void handleJwtException(HttpServletResponse response, Exception ex) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        ExceptionResponse error = ExceptionResponse.of(
                "Unauthorized",
                getErrorMessage(ex)
        );

        response.getWriter().write(jsonMapper.writeValueAsString(error));
        response.getWriter().flush();
    }

    private String getErrorMessage(Exception ex) {
        if (ex instanceof io.jsonwebtoken.ExpiredJwtException) {
            return "Token has expired";
        }
        if (ex instanceof io.jsonwebtoken.MalformedJwtException) {
            return "Invalid token format";
        }
        if (ex instanceof io.jsonwebtoken.security.SignatureException) {
            return "Invalid token signature";
        }
        return "Invalid authentication token";
    }
}
