package com.exemplo.algamoney.api.config;
//
//AQUI FOI O CÓDIGO DA AULA DE SEGURANÇA BASICA

//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig  extends WebSecurityConfigurerAdapter {
//
//    //aqui ta colocando a senha e o usuário
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("admin").password("{noop}admin").roles("ROLE");
//    }
//
//    //aqui vc vai configurar como que a segurança vai ocorrer
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/categoria").permitAll()
//                .anyRequest().authenticated()
////        Para categoria, qualquer um pode acessar, para o restante somente quem estiver
////        autenticado... quando quiser liberar algo eh so usar esse antMatchers
//                .and().httpBasic()
//                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////        Esse stateless é para que a api não mantenha estado de nada no servidor
//                .and().csrf().disable();
////nao sei o que eh esse csrf
//
//    }
//}
