package com.exemplo.algamoney.api.token;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken> { //dentro do < > é o tipo de dado a ser interceptado qando tiver voltando
//    Esse ResponseBodyAdvice é para que ele venha alterar a resposta (no caso tirar o refresh_token)
//    um pouco antes de aparecer... ele só vai ocultar para poder esconder no cookie



//    o método suports ai é para ser um filtro extra... porque se não toda requisião que tiver o
//    objeto OAuth2AccessToken, ele iria fazer... mas vc quer apenas quando fizer o .../oauth/token
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getMethod().getName().equals("postAccessToken");
    }

    @Override
    public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        //Preparando para criar o cookie
        HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
        HttpServletResponse resp = ((ServletServerHttpResponse) response).getServletResponse();

        String refreshToken = body.getRefreshToken().getValue();

        adicionarRefreshTokenNoCookie(refreshToken, req, resp);

        //preparando para tirar o refresh token do body
        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body;
        removerRefreshTokenDoBody(token);
        return body;

    }

    private void adicionarRefreshTokenNoCookie(String refreshToken, HttpServletRequest req, HttpServletResponse resp) {
        //CRIANDO O COOKIE
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); //aqui vai definir se ele vai conseguir acessar apenas quando for HTTPS.... para produção, colocar essa propriedade como TRUE
        refreshTokenCookie.setPath(req.getContextPath()+ "/oauth/token");
        refreshTokenCookie.setMaxAge(2592000);
        resp.addCookie(refreshTokenCookie);
    }

    private void removerRefreshTokenDoBody(DefaultOAuth2AccessToken token){
        token.setRefreshToken(null);
    }
}
