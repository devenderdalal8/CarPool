package com.carpool.carpool.filter;

import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class RateLimitFilter implements Filter {


    private final Bucket bucket;

    @Autowired
    public RateLimitFilter(Bucket bucket) {
        this.bucket = bucket;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilterRegistration(RateLimitFilter filter) {
        FilterRegistrationBean<RateLimitFilter> reg = new FilterRegistrationBean<>(filter);
        reg.addUrlPatterns("/rateLimit/*");           // bind to all /api/* endpoints
        reg.setOrder(1);                        // filter order if you have multiple filters
        return reg;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (bucket.tryConsume(1)) {
            // Allow the request to proceed
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            ((HttpServletResponse) servletResponse).setStatus(429);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
