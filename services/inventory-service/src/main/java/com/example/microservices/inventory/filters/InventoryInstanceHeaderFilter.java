package com.example.microservices.inventory.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class InventoryInstanceHeaderFilter extends OncePerRequestFilter {
    static final String INSTANCE_HEADER = "X-Instance-Port";

    private final int serverPort;

    public InventoryInstanceHeaderFilter(@Value("${server.port}") int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        response.setHeader(INSTANCE_HEADER, String.valueOf(serverPort));
        filterChain.doFilter(request, response);
    }
}
