package io.github.FlyingASea.controller;

import com.auth0.jwt.interfaces.Claim;
import io.github.FlyingASea.result.ApiException;
import io.github.FlyingASea.result.Errors;
import io.github.FlyingASea.service.UserAuthenticationService;
import io.github.FlyingASea.util.RandomUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Resource
    private UserAuthenticationService userAuthenticationService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        NeedAuthenticated needAuthenticated;

        if (handler instanceof HandlerMethod method) {
            needAuthenticated = method.getMethodAnnotation(NeedAuthenticated.class);
            if (needAuthenticated == null)
                needAuthenticated = method.getMethod().getDeclaringClass().getAnnotation(NeedAuthenticated.class);
            if (needAuthenticated == null) {
                return true;
            }
        } else {
            return true;
        }


        Cookie cookie = getCookie(request, "session");
        String session;
        boolean checked;

        if (cookie == null) {
            checked = false;
        } else {
            session = cookie.getValue();
            Map<String, Claim> claims = RandomUtils.verifySession(session);
            if (claims == null)
                checked = false;
            else {
                String value = userAuthenticationService.createOrGetSession(claims.get("id").asString());
                if (value == null || !value.equals(session)) {
                    checked = false;
                } else {
                    return true;
                }
            }
        }
        return true;
    }

    private Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return null;
        for (Cookie i : cookies) {
            if (name.equals(i.getName())) {
                return i;
            }
        }
        return null;
    }

}
