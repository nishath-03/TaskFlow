package com.taskflow.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // This runs ONCE per HTTP request, BEFORE the controller is called
    // It reads the JWT, validates it, and sets the user in SecurityContext
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Step 1: Read the Authorization header
        String authHeader = request.getHeader("Authorization");

        // Step 2: Check it starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // no token — skip, Security will block if route is protected
            return;
        }

        // Step 3: Strip "Bearer " prefix to get the raw token
        String token = authHeader.substring(7);

        // Step 4: Validate token
        if (!jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response); // invalid token — skip
            return;
        }

        // Step 5: Extract user info from token
        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);

        // Step 6: Build Spring Security authentication object with role
        // This is what @PreAuthorize reads when checking permissions
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role))
                );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Step 7: Put authentication in SecurityContext — now the user is "logged in" for this request
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
