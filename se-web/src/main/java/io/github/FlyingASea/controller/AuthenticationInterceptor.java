package io.github.FlyingASea.controller;

//import io.github.FlyingASea.result.ApiException;
//import io.github.FlyingASea.result.Errors;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
//import org.springframework.security.web.csrf.CsrfToken;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//public class AuthenticationInterceptor implements HandlerInterceptor {
//    @Override
//    public boolean preHandle(HttpServletRequest request,
//                             HttpServletResponse response,
//                             Object handler) {
//        HttpSession session = request.getSession();
//        String id = (String) session.getAttribute("session-id");
//
//        CsrfToken obj=new CookieCsrfTokenRepository().loadToken(request);
//
//        if (id == null) {
//            throw new ApiException(Errors.UNAUTHORIZED);
//        } else {
//
//            return true;
//        }
//    }
//}
