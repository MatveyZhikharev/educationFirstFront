package com.educationalplatform.filter;

import com.educationalplatform.domain.model.User;
import com.educationalplatform.service.JwtService;
import com.educationalplatform.service.UserService;
import com.educationalplatform.utils.IdAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtCookieAuthenticationFilter extends OncePerRequestFilter {

  @Value("${jwt.name}")
  private String jwtName;

  private final JwtService jwtService;
  private final UserService usersService;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    try {
      Cookie[] cookies = request.getCookies();

      if (cookies != null) {
        Optional<Cookie> jwtCookie = Arrays.stream(cookies)
            .filter(c -> c.getName().equals(jwtName))
            .findFirst();

        if (jwtCookie.isPresent()) {
          String token = jwtCookie.get().getValue();

          UUID userId = jwtService.extractUserId(token);
          Optional<User> userOptional = usersService.findById(userId);

          if (userOptional.isPresent()) {
            User user = userOptional.get();

            Collection<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
            );

            IdAuthenticationToken authToken =
                new IdAuthenticationToken(user, authorities);

            SecurityContextHolder.getContext().setAuthentication(authToken);
          }
        }
      }

    } catch (Exception e) {
      log.error("JWT filter error", e);
    }
    filterChain.doFilter(request, response);
  }
}