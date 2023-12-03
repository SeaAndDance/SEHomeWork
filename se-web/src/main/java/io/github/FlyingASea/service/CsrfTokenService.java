//package io.github.FlyingASea.service;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.web.csrf.CsrfToken;
//import org.springframework.security.web.csrf.CsrfTokenRepository;
//import org.springframework.security.web.csrf.DefaultCsrfToken;
//import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
//
//import java.io.IOException;
//import java.util.Base64;
//import java.util.Map;
//import java.util.UUID;
//
//public class CsrfTokenService implements CsrfTokenRepository {
//    private static final String DEFAULT_CSRF_HEADER_NAME = "X-CSRF-TOKEN";
//    private static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";
//
//    @Override
//    public CsrfToken generateToken(HttpServletRequest request) {
//        return new DefaultCsrfToken(DEFAULT_CSRF_HEADER_NAME, DEFAULT_CSRF_PARAMETER_NAME, createNewToken());
//    }
//
//    @Override
//    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
//        try {
//            response.getWriter().write(Map.of(
//                    "csrf_token", token
//            ).toString());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public CsrfToken loadToken(HttpServletRequest request) {
//        // Always return null so that a new token is created for every request
//
//        return null;
//    }
//
//    private String createNewToken() {
//        return new String(Base64.getUrlEncoder().withoutPadding().encode(UUID.randomUUID().toString().getBytes()));
//    }
//}
