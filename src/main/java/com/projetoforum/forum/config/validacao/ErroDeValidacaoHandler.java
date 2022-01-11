package com.projetoforum.forum.config.validacao;

import com.projetoforum.forum.controller.UsuarioController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class ErroDeValidacaoHandler {

    @Autowired
    private MessageSource messageSource;
    private NoSuchElementException exception;

    private static final Logger log = LoggerFactory.getLogger(ErroDeValidacaoHandler.class);

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErroDeFormularioDto> handle(MethodArgumentNotValidException exception){

        log.info("Houve um erro na validação dos dados: Verifique se os dados estão preenchidos corretamente.");

        List<ErroDeFormularioDto> dto = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        fieldErrors.forEach(e-> {
            String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            ErroDeFormularioDto erro = new ErroDeFormularioDto(e.getField(),mensagem);
            dto.add(erro);
        });
        return dto;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateKeyException.class)

    public String handle(DuplicateKeyException exception) {

        log.info("Houve um erro na validação dos dados: Verifique se não há dados duplicados.");

        String[] erro = exception.getCause().getLocalizedMessage().split("dup key: ");
        String mensagem = erro[1].replace('{', ' ').replace('}', ' ') + "já existe.";
        return mensagem;
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String handle(NoSuchElementException exception){

        log.info("Houve um erro na validação dos dados: Verifique se os dados existem na base de dados.");

        String mensagem = exception.getMessage();
        return mensagem;
}
}
