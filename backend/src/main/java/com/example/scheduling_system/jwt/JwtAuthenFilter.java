package com.example.scheduling_system.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtAuthenProvider jwtAuthenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    private final String Bearer = "Bearer";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var token = this.filterJwtFromRequest(request);
        try {
            if (token != null && jwtAuthenProvider.verifyToken(token)) {
                String username = jwtAuthenProvider.getUsernameByToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
                        userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            throw new IOException("Error!");
        } finally {
            filterChain.doFilter(request, response);
        }

    }

    private String filterJwtFromRequest(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith(Bearer)) {
            return headerAuth.replace(Bearer, "").trim();
        }
        return null;
    }

}
