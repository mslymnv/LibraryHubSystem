package az.company.users.security;

import az.company.users.service.JwtService;
import az.company.users.service.TokenStorageService;
import az.company.users.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenStorageService tokenStorageService;
    private final CustomUserDetailsService customUserDetailsService;
    private final ObjectMapper objectMapper;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");


        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }


        String token = authHeader.substring(7);


        try {

            if (!jwtService.isTokenValid(token)) {
                sendUnauthorized(response, "Invalid or expired token");
                return;
            }


            String username = jwtService.extractUsername(token);


            if (username == null) {
                sendUnauthorized(response, "Invalid token payload");
                return;
            }


            if (!tokenStorageService.isAccessTokenValid(username, token)) {
                sendUnauthorized(response, "Token revoked");
                return;
            }


            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );


                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            System.out.println("Authorities = " + auth.getAuthorities());
            System.out.println("Principal = " + auth.getPrincipal());

            filterChain.doFilter(request, response);


        } catch (Exception e) {

            SecurityContextHolder.clearContext();
            sendUnauthorized(response, "Authentication failed");
        }
    }


    private void sendUnauthorized(HttpServletResponse response,
                                  String message) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);


        objectMapper.writeValue(
                response.getWriter(),
                Map.of(
                        "status", 401,
                        "timestamp", LocalDateTime.now(),
                        "error", "Unauthorized",
                        "message", message
                )
        );
    }
}