package com.mjc.school.controller.utils;

import com.mjc.school.service.errorsexceptions.ApiUriException;
import com.mjc.school.service.validation.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestInterceptor implements HandlerInterceptor, HandlerExceptionResolver {
    private final RequestValidator requestValidator;

    @Autowired
    public RequestInterceptor(RequestValidator requestValidator) {
        this.requestValidator = requestValidator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestUri = request.getRequestURI();
        //requestValidator.validateApiRootPath(requestUri);
        return true;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception ex) {
        if (ex instanceof ApiUriException) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json");
            try {
                response.getWriter().write(ex.getMessage());
            } catch (IOException e) {
            }
            return new ModelAndView();
        }
        return null;
    }
}