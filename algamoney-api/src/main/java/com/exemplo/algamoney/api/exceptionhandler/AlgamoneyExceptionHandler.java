package com.exemplo.algamoney.api.exceptionhandler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Com esse Controller Adivce ele vai observar todos os controllers da aplicação
@ControllerAdvice
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler {
    //ResponseEntityExceptionHandler : captura exceções de respostas de entidades

    @Autowired
    private MessageSource messageSource;

//    Quando você manda mais informações que não são mapeadas no banco de dados.
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable
            (HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

//      esse mensagem.invalida ta vindo lá do arquivo messages.properties
        String mensagemUsuario = messageSource.getMessage("mensagem.invalida",null, LocaleContextHolder.getLocale());
        String mensagemDesenvolvedor = ex.getCause() != null ? ex.getCause().toString() : ex.toString();

//      no lugar de especificar que eh um badRequest vc pode deixar só status, que eh o badrequest que ele lança para o handleHttpMessageNotReadable
       List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
        return handleExceptionInternal(ex, erros , headers, HttpStatus.BAD_REQUEST, request);
    }

//Quando o usuário enviar as informações invalidas.
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<Erro> erros = criarListaDeErros(ex.getBindingResult());
        return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
    }


    //esse binding result é aonde você tem a lista de todos os erros.
    private List<Erro> criarListaDeErros(BindingResult bindingResult){
        List<Erro> erros = new ArrayList<>();

        //esse for é para você interagir sobre a lista de erros
        for(FieldError fieldError : bindingResult.getFieldErrors()) {
            String mensagemUsuario = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
            String mensagemDesenvolvedor = fieldError.toString();
            erros.add(new Erro(mensagemUsuario, mensagemDesenvolvedor));
        }
        return erros;
    }

    public static class Erro {
        @Setter @Getter
        private String mensagemUsuario;
        @Setter @Getter
        private String mensagemDesenvolvedor;

        public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
            this.mensagemUsuario = mensagemUsuario;
            this.mensagemDesenvolvedor = mensagemDesenvolvedor;
        }
    }


//    Criando um método para tratar de quando deletar algo que não tem no banco de dados.
//    Para você pode dizer ao spring que ele deve usar esse método para tratar algum problema,
//    você deve usar esse @ExceptionHandler e colocar quais erros você está se referindo.
    @ExceptionHandler({EmptyResultDataAccessException.class})
//    @ResponseStatus(HttpStatus.NOT_FOUND) Você pode usar só essa linha de comando sem as demais dentro dessa função se não quiser enviar nenhuma mensagem de retorno
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request){

        String mensagemUsuario = messageSource.getMessage("recurso.nao-encontrado",null, LocaleContextHolder.getLocale());
        String mensagemDesenvolvedor = ex.toString();
        List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

        return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);

    }



}
