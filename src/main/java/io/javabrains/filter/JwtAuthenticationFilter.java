package io.javabrains.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.javabrains.Entities.User;
import io.javabrains.Repositories.UserRepository;
import io.javabrains.Utilities.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(1)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    public JwtAuthenticationFilter(JwtUtil jwtUtil,UserRepository userRepository){
        this.jwtUtil=jwtUtil;
        this.userRepository=userRepository;
        System.out.println("JwtAuthenticationFilter initialized");
    }
  
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer")) {
                chain.doFilter(request, response);
                return;
            }
    
            String token = authHeader.substring(7);
            String path = request.getRequestURI();
            System.out.println("Request URI: " + path);
    
            if (path.startsWith("/auth/")) {
                chain.doFilter(request, response);  // Skip JWT validation for /auth/ paths
                return;
            }
    
            if (token != null) {
                String username = jwtUtil.extractUsername(token);
                System.out.println("Username extracted: " + username);
    
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtUtil.isTokenValid(token, username)) {
                        User userDetails = userRepository.findByUsername(username).orElse(null);
                        if (userDetails != null) {
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                }
            }
    
            chain.doFilter(request, response);  // Ensure this is only called once
    
        } catch (ExpiredJwtException e) {
            handleException(response, HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
            return; // Stop further processing
        } catch (MalformedJwtException e) {
            handleException(response, HttpServletResponse.SC_BAD_REQUEST, "Malformed token");
            return;
        } catch (io.jsonwebtoken.security.SecurityException e) {
            handleException(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token signature");
            return;
        } catch (Exception e) {
            handleException(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the token");
            return;
        }
    }
    
    private void handleException(HttpServletResponse response, int status, String message) throws IOException {
        if (!response.isCommitted()) {
            response.setStatus(status);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + message + "\"}");
            response.getWriter().flush();
        }
    }
}
