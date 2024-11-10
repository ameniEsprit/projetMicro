package com.esprit.userservice.securityconfig;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class IpFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("testing into the init of filter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String remoteAddr = httpRequest.getRemoteAddr();
        System.out.println("remoteAddr: " + remoteAddr);
        String apiGatewayIp = "192.168.56.1";
        if (!(remoteAddr.equals(apiGatewayIp) )) {
            throw new ServletException("Direct access unauthorized");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Destruction du filtre
    }
    

}