package com.exemplo.algamoney.api.cors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    private String originPermitida = ""; // TODO

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        //Convertendo o requeste e o response que vem do implements
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        response.setHeader("Access-Control-Allow-Origin", originPermitida); //qual a origin permitida
        response.setHeader("Access-Control-Allow-Credentials", "true"); //Isso é para vc permitir que o cookie seja enviado


        if("OPTIONS".equals(request.getMethod()) && originPermitida.equals(request.getHeader("Origin"))){
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
            response.setHeader("Access-Control-Max-Age", "3600");

            response.setStatus(HttpServletResponse.SC_OK);
        }else {
            //para ele seguir o fluxo normal se entrar dentro desse else
            chain.doFilter(req, resp);
        }
    }







    //Não vai utilizar esses dois abaixo
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
    @Override
    public void destroy() {

    }
}
